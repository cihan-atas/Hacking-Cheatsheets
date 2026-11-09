<!-- language-home-navigation -->
[🏠 中文主页](../../README.zh-CN.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🧩 Ropper 和 ROPgadget - 完整备忘单

[🇹🇷 Türkçe Sürüm]（翻译/README.tr.md）
```
  ██████╗  ██████╗ ██████╗ ██████╗ ███████╗██████╗ 
  ██╔══██╗██╔═══██╗██╔══██╗██╔══██╗██╔════╝██╔══██╗
  ██████╔╝██║   ██║██████╔╝██████╔╝█████╗  ██████╔╝
  ██╔══██╗██║   ██║██╔═══╝ ██╔═══╝ ██╔══╝  ██╔══██╗
  ██║  ██║╚██████╔╝██║     ██║     ███████╗██║  ██║
  ╚═╝  ╚═╝ ╚═════╝ ╚═╝     ╚═╝     ╚══════╝╚═╝  ╚═╝
     Advanced ROP & JOP Gadget Finder and Chain Generator
```
<p对齐=“中心”>
  <img src="https://img.shields.io/badge/Ropper-Gadget_Finder-blue?style=for-the-badge" alt="Ropper">
  <img src="https://img.shields.io/badge/ROPgadget-Exploit_Tool-orange?style=for-the-badge" alt="ROPgadget">
  <img src="https://img.shields.io/badge/Bypass-NX_%7C_DEP-red?style=for-the-badge" alt="NX 绕过">
  <img src="https://img.shields.io/badge/Category-Exploit_Development-green?style=for-the-badge" alt="利用开发">
</p>

<p对齐=“中心”>
  <b>🧩 基本的命令行实用程序，用于发现面向返回编程（ROP）和面向跳转编程（JOP）的小工具，以绕过不可执行（NX / DEP）内存保护</b>
</p>

---

## 📋 目录

- [概述与 ROP 理论](#-overview--rop-theory)
- [安装](#-安装)
- [基本小工具发现](#-basic-gadget-discovery)
  - [提取所有小工具](#extracting-all-gadgets)
  - [限制指令深度](#limiting-instruction-深度)
- [高级搜索和模式过滤](#-advanced-search--pattern-filtering)
  - [定位特定寄存器（`rdi`、`rsi`、`rdx`、`rax`）](#targeting-specific-registers-rdi-rsi-rdx-rax)
  - [内存写入小工具（Write-What-Where）](#memory-write-gadgets-write-what-where)
  - [系统调用和中断小工具](#syscall--interrupt-gadgets)
- [排除坏字符 (`--badbytes`)](#-排除坏字符---坏字节)
- [自动 ROP 链生成](#-automated-rop-chain- Generation)
  - [Ropper 自动链条](#ropper-automated-chains)
  - [ROPgadget 自动链](#ropgadget-automated-chains)
- [分析加载的库（libc ROP 链）](#-analyzing-loaded-libraries-libc-rop-chains)
- [快速参考表](#-quick-reference-table)
- [防御考虑因素和缓解措施](#-defective-considerations--mitigations)
- [法律免责声明](#-legal-disclaimer)

---

## 🎯 概述和 ROP 理论

现代操作系统强制执行 **NX（不执行）/DEP（数据执行保护）**，这会阻止直接执行放置在堆栈或堆上的 shellcode。

**面向返回的编程 (ROP)** 通过重用可执行内存中已存在的小型现有机器指令（二进制或共享库（如 `libc.so`）的 `.text` 部分）来规避此限制。每个以 `ret` 指令结尾的指令序列称为 **gadget**。通过在堆栈上排列地址，攻击者将小工具链接在一起以：
1. 将参数加载到 CPU 寄存器中（例如，x64 调用约定的 `pop rdi; ret`）。
2. 将字符串（如 `/bin/sh`）写入可写的 `.bss` 或 `.data` 部分。
3. 触发`execve("/bin/sh", NULL, NULL)`或调用`system()`。

---

## 📥 安装
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y ropper python3-ropgadget

# Python pip (Latest Releases)
pip install ropper capstone
pip install ROPgadget

# Arch Linux
sudo pacman -S ropper python-ropgadget
```
---

## 🚀 基本小工具发现

### 提取所有小工具
```bash
# Using Ropper (Faster multi-threaded engine)
ropper --file ./target_binary --nocolor

# Using ROPgadget
ROPgadget --binary ./target_binary
```
### 限制指令深度
通过限制 gadget 长度（`ret` 之前的指令数量）来减少嘈杂的长链：
```bash
# Limit to maximum 3 instructions before return
ropper --file ./target_binary --inst-count 3
ROPgadget --binary ./target_binary --depth 3
```
---

## 🔍 高级搜索和模式过滤

### 针对特定寄存器（`rdi`、`rsi`、`rdx`、`rax`）
在 64 位 Linux 调用约定中，函数或系统调用的前三个参数必须位于 `rdi`、`rsi` 和 `rdx` 中：
```bash
# Find pop rdi gadgets (1st argument)
ropper --file ./target_binary --search "pop rdi"

# Find pop rsi gadgets (2nd argument)
ropper --file ./target_binary --search "pop rsi"

# Find pop rdx gadgets (3rd argument)
ropper --file ./target_binary --search "pop rdx"

# Find gadgets that control RAX (Syscall number: 59 for execve)
ropper --file ./target_binary --search "pop rax"
```
使用 ROPgadget 正则表达式匹配：
```bash
ROPgadget --binary ./target_binary --only "pop|ret" | grep -E "rdi|rsi|rdx|rax"
```
### 内存写入小工具（Write-What-Where）
当您需要将 `/bin/sh` 或任意参数存储到内存中时：
```bash
# Find instructions moving registers into pointer memory
ropper --file ./target_binary --search "mov [???], ???"
```
*示例匹配：* `mov qword ptr [rdi], rsi; ret` 允许将 `rsi` 中的任何内容写入 `rdi` 指向的地址。

### 系统调用和中断小工具
```bash
# Find raw syscall instructions
ropper --file ./target_binary --search "syscall"

# x86 32-bit software interrupt
ropper --file ./target_binary --search "int 0x80"
```
---

## 🚫 排除不良字符 (`--badbytes`)

字符串格式漏洞或 `strcpy()` 等函数会在空字节 (`\x00`)、换行符 (`\x0a`) 或回车符 (`\x0d`) 处终止。排除包含这些字节的地址：
```bash
# Filter out gadgets whose addresses contain bad characters
ropper --file ./target_binary --badbytes 000a0d --search "pop rdi"
ROPgadget --binary ./target_binary --badbytes "00|0a|0d"
```
---

## ⚡ 自动 ROP 链生成

这两个工具都可以自动构建调用 `execve("/bin/sh")` 的功能性 Python 漏洞利用模板：

### Ropper 自动链条
```bash
ropper --file ./target_binary --chain "execve"
```
### ROPgadget 自动链
```bash
ROPgadget --binary ./target_binary --ropchain
```
*这会生成一个链接 `mprotect()` 或 `execve()` 的即用型 Python 有效负载骨架。*

---

## 📚 分析加载的库（libc ROP 链）

当二进制文件本身编译为 **PIE** 或包含很少有用的小工具时，直接从目标系统的共享 C 库 (`libc.so.6`) 中提取小工具：
```bash
# Search gadgets in libc
ropper --file /lib/x86_64-linux-gnu/libc.so.6 --search "pop rdi; ret"

# Find "/bin/sh" string offset inside libc
strings -a -t x /lib/x86_64-linux-gnu/libc.so.6 | grep "/bin/sh"
```
*（在运行时将发现的偏移量添加到泄漏的 libc 基地址中）。*

---

## ⚡ 快速参考表

|目标|罗珀指挥 | ROPgadget 命令 |
|------------------------|----------------|--------------------|
| **转储所有小工具** | `ropper --file <bin>` | `ROPgadget --binary <bin>` |
| **搜索特定小工具** | `ropper --file <bin> --search "pop rdi"` | `ROPgadget --binary <bin> \| grep "pop rdi"` |
| **过滤坏字节** | `ropper --file <bin> --badbytes 000a` | `ROPgadget --binary <bin> --badbytes "00\|0a"` |
| **限制深度/长度** | `ropper --file <bin> --inst-count 3` | `ROPgadget --binary <bin> --depth 3` |
| **搜索 JOP 小工具** | `ropper --file <bin> --type jop` | `ROPgadget --binary <bin> --norop` |
| **自动构建ROP链** | `ropper --file <bin> --chain "execve"`| `ROPgadget --binary <bin> --ropchain`|
| **搜索内存写入** | `ropper --file <bin> --search "mov [???], ???"` | `ROPgadget --binary <bin> \| grep "mov \["` |

---

## 🛡️ 防御考虑和缓解措施

1. **控制流执行技术（Intel CET / Shadow Stack）：** 现代处理器维护一个隔离的硬件影子堆栈，用于验证 `ret` 上的返回地址，立即终止 ROP 链。
2. **Clang / GCC 控制流完整性 (CFI)：** 在间接调用之前根据前向边缘图验证函数指针目标。

---

## ⚠️ 法律免责声明

> [!警告]
> Ropper 和 ROPgadget 是漏洞利用开发和软件安全评估实用程序。它们必须严格用于授权渗透测试、CTF 挑战或防御安全研究。未经明确授权而使用软件是非法的。