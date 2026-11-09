<!-- language-home-navigation -->
[🏠 中文主页](../../README.zh-CN.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ⚡ Radare2 (r2) - 完整备忘单

[🇹🇷 Türkçe Sürüm]（翻译/README.tr.md）
```
  ██████╗  █████╗ ██████╗  █████╗ ██████╗ ███████╗██████╗ 
  ██╔══██╗██╔══██╗██╔══██╗██╔══██╗██╔══██╗██╔════╝╚════██╗
  ██████╔╝███████║██║  ██║███████║██████╔╝█████╗   █████╔╝
  ██╔══██╗██╔══██║██║  ██║██╔══██║██╔══██╗██╔══╝  ██╔═══╝ 
  ██║  ██║██║  ██║██████╔╝██║  ██║██║  ██║███████╗███████╗
  ╚═╝  ╚═╝╚═╝  ╚═╝╚═════╝ ╚═╝  ╚═╝╚═╝  ╚═╝╚══════╝╚══════╝
      UNIX-like Command-Line Reverse Engineering Framework
```
<p对齐=“中心”>
  <img src="https://img.shields.io/badge/Radare2-CLI_Reversing-blue?style=for-the-badge" alt="Radare2">
  <img src="https://img.shields.io/badge/Author-Sergi_Álvarez-orange?style=for-the-badge" alt="pancake">
  <img src="https://img.shields.io/badge/Category-Reverse_Engineering-green?style=for-the-badge" alt="逆向工程">
  <img src="https://img.shields.io/badge/License-LGPLv3-purple?style=for-the-badge" alt="LGPLv3">
</p>

<p对齐=“中心”>
  <b>⚡功能强大、开源、基于终端的框架，用于二进制分析、反汇编、调试、十六进制编辑和二进制修补</b>
</p>

---

## 📋 目录

- [概述与架构](#-overview--architecture)
- [安装](#-安装)
- [打开文件和会话模式](#-opening-files--session-modes)
- [二进制分析命令 (`a`)](#-binary-analysis-commands-a)
- [导航和搜索(`s`)](#-navigation--seeking-s)
- [打印&拆解(`p`)](#-printing--disassemble-p)
- [交互式视觉和图形模式（`V` 和 `VV`）](#-interactive-visual--graph-modes-v--vv)
- [二进制修补和修改 (`w`)](#-binary-patching--modification-w)
- [Radare2 配套工具集](#-the-radare2-companion-toolset)
  - [rabin2：二进制信息提取](#rabin2-binary-information-extraction)
  - [rasm2：汇编器和反汇编器](#rasm2-汇编器--反汇编器)
  - [radiff2：二进制差异引擎](#radiff2-binary-difference-engine)
  - [rafind2：字节和字符串搜索](#rafind2-byte--string-search)
  - [rax2：基数和数字转换器](#rax2-base--numeric-converter)
- [使用 r2 进行调试基础知识 (`-d`)](#-debugging-basics-with-r2--d)
- [快速参考表](#-quick-reference-table)
- [防御考虑与反逆转](#-defective-considerations--anti-reversing)
- [法律免责声明](#-legal-disclaimer)

---

## 🎯 概述与架构

**Radare2** (`r2`) 由 **Sergi Álvarez (pancake)** 创建，是一个完全围绕 UNIX 哲学构建的可移植、模块化逆向工程框架。它完全在您的终端内运行，不需要大量的图形依赖项：
- **完整架构支持：** x86/x64、ARM、ARM64、MIPS、PowerPC、AVR、SPARC、WebAssembly、Java、Dalvik 和 EVM。
- **可组合的 CLI 实用程序：** 与专用命令行工具（`rabin2`、`rasm2`、`radiff2`、`rafind2`、`rax2`）捆绑在一起。
- **交互式视觉模式：** 分屏十六进制编辑器、ASCII 控制流图、寄存器监视器和反编译器视图。
- **可编写脚本且可扩展：** 与 Python、r2pipe、Ghidra (`r2ghidra`) 和 Rizin 集成。

---

## 📥 安装
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y radare2

# Arch Linux
sudo pacman -S radare2

# Install latest Git master (Recommended for most features)
git clone https://github.com/radareorg/radare2.git
cd radare2 && sys/install.sh

# Install r2ghidra decompiler plugin
r2pm update
r2pm -i r2ghidra
```
---

## 🚀 打开文件和会话模式
```bash
# Open binary in read-only mode
r2 /bin/ls

# Open binary in read-write mode (required for patching)
r2 -w target.bin

# Open binary in debugger mode
r2 -d target.bin

# Open binary without parsing headers (raw flat binary / shellcode)
r2 -b 32 -n /path/to/raw.bin

# Open in quiet mode (no greeting banner)
r2 -q target.bin
```
---

## 🔬 二进制分析命令 (`a`)

在 `r2` shell 内，命令是单个字母或短前缀。您可以将 `?` 附加到任何命令以查看内联帮助：
```text
[0x00000000]> a?        # Show analysis help
[0x00000000]> aa        # Basic analysis (symbols, basic blocks)
[0x00000000]> aaa       # Deep analysis (functions, cross-references, calls)
[0x00000000]> aaaa      # Experimental auto-analysis (takes longer)

[0x00000000]> afl       # List all identified functions
[0x00000000]> afi       # Display function info at current address
[0x00000000]> afvn      # Rename local variable: afvn <new_name> <old_name>
[0x00000000]> axt <addr># Find cross-references (xrefs) TO address
[0x00000000]> axf <addr># Find cross-references (xrefs) FROM address
```
---

## 🧭 导航和搜索 (`s`)

search 命令更改当前光标地址：
```text
[0x00000000]> s main        # Seek to main function
[0x00401122]> s 0x401200    # Seek to absolute address
[0x00401200]> s+ 32         # Seek 32 bytes forward
[0x00401220]> s- 16         # Seek 16 bytes backward
[0x00401210]> s/ hit0_0     # Seek to search hit
[0x00401210]> u             # Undo seek (go back to previous address)
[0x00401200]> U             # Redo seek
```
---

## 📜 打印和反汇编 (`p`)
```text
[0x00401122]> pd 10     # Disassemble 10 instructions
[0x00401122]> pdf       # Disassemble entire current function
[0x00401122]> pdc       # Decompile function using r2ghidra plugin
[0x00401122]> px 64     # Print 64 bytes in hex dump format
[0x00401122]> ps        # Print string at current address
[0x00401122]> pz        # Print zero-terminated string
[0x00401122]> pi 5      # Print 5 instructions (concise format)
```
---

## 🖥️ 交互式视觉和图形模式（`V` 和 `VV`）

按 `V` 或 `VV` 将 Radare2 变成一个丰富的交互式终端应用程序：

- **进入可视模式：** 输入 `V`
  - 按 `p` / `P` 循环视图（十六进制转储 -> 反汇编 -> 调试器 -> 反编译器）。
  - 按 `c` 切换光标模式（使用箭头键或 `hjkl` 选择字节）。
  - 按 `d` 将字节重新定义为代码、数据或字符串。
  - 按 `q` 返回标准提示符。
- **进入图形视图：** 输入 `VV`
  - 呈现 ASCII 控制流图 (CFG)。
  - 使用`Tab` / `Shift+Tab`在条件分支节点之间跳转。
  - 按`t`跟随真正的分支； `f`跟随假分支。
  - 按 `u` 退出分支。

---

## ✍️ 二进制修补和修改 (`w`)

要修改磁盘上的二进制文件，请确保 r2 以写入模式打开（`r2 -w target` 或在活动会话中键入 `oo+`）：
```text
[0x00401150]> oo+                           # Re-open current binary with write permissions
[0x00401150]> wa nop                        # Assemble 'nop' instruction at current address
[0x00401150]> wa jmp 0x401190               # Patch a jump instruction
[0x00401150]> wx 90909090                   # Write hex bytes (4 NOPs)
[0x00401150]> w "Modified string\x00"       # Overwrite string with null-terminator
[0x00401150]> w0 16                         # Zero-fill 16 bytes (NUL padding)
```
---

## 🛠️ Radare2 配套工具集

Radare2 包括独立的 CLI 实用程序：

### rabin2：二进制信息提取
```bash
# Print general binary architecture, bits, and security mitigations (NX, PIE, Canary)
rabin2 -I target.bin

# List imported symbols and functions (libc calls)
rabin2 -i target.bin

# List exported symbols
rabin2 -E target.bin

# Extract ASCII & UTF-16 strings
rabin2 -z target.bin

# List sections (.text, .data, .rodata)
rabin2 -S target.bin
```
### rasm2：汇编器和反汇编器
```bash
# Assemble x64 instructions into hexadecimal opcode
rasm2 -a x86 -b 64 "xor eax, eax; ret"
# Output: 31c0c3

# Disassemble hex opcodes into human-readable assembly
rasm2 -a x86 -b 64 -d "31c0c3"
# Output:
# xor eax, eax
# ret
```
### radiff2：二进制差分引擎
```bash
# Compare two binaries and count differing bytes
radiff2 -c original.bin patched.bin

# Detailed code comparison graph
radiff2 -g main original.bin patched.bin | xdot -
```
### rafind2：字节和字符串搜索
```bash
# Search for ASCII string in file
rafind2 -s "admin_password" target.bin

# Search for hexadecimal pattern
rafind2 -x "4889e5" target.bin
```
### rax2：基数和数字转换器
```bash
# Convert hex to decimal
rax2 0x1337      # Output: 4919

# Convert decimal to hex
rax2 4919        # Output: 0x1337

# Convert string to hex
rax2 -S "admin"  # Output: 61646d696e
```
---

## 🐛 使用 r2 进行调试基础知识 (`-d`)
```bash
# Start debugging binary with arguments
r2 -d target.bin arg1 arg2
```
在调试器 shell 内部：
```text
[0x7f112233]> db main       # Set software breakpoint at main
[0x7f112233]> db 0x401150   # Set breakpoint at specific address
[0x7f112233]> dbl           # List all active breakpoints
[0x7f112233]> dc            # Continue execution until breakpoint
[0x7f112233]> ds            # Step into (single instruction)
[0x7f112233]> dso           # Step over function calls
[0x7f112233]> dr            # Print all CPU registers
[0x7f112233]> dr rax        # Print specific register value
[0x7f112233]> dr rax = 0x1  # Modify register value
```
---

## ⚡ 快速参考表

|目标|命令 |
|------------|---------|
| **深度分析** | `aaa` |
| **列表功能** | `afl` |
| **寻求功能/地址** | `s <addr>` / `s main` |
| **反汇编功能** | `pdf` |
| **反编译功能(r2ghidra)** | `pdc` |
| **交互式视觉拆解** | `V` |
| **ASCII 流程图** | `VV` |
| **组装说明** | `wa <instruction>` |
| **写入十六进制字节** | `wx <bytes>` |
| **以读写方式重新打开** | `oo+`|
| **提取二进制信息** | `rabin2 -I <binary>`|
| **组装 CLI** | `rasm2 -a x86 -b 64 "<asm>"` |

---

## 🛡️ 防守考虑和防逆转

1. **反调试检测：** Radare2 的调试器依赖于标准 Linux `ptrace`。具有 `ptrace(PTRACE_TRACEME)` 自检功能的二进制文件可能会阻止附件，除非进行修补。
2. **混淆和控制流扁平化：** 高度混淆的二进制文件 (OLLVM) 混淆了基本的块恢复。使用 `aaaa` 或符号执行引擎（如 angr）来简化扁平化函数。

---

## ⚠️ 法律免责声明

> [!警告]
> Radare2 是一款逆向工程和分析工具包，严格用于授权安全评估、恶意软件分析、漏洞研究和个人教育用途。未经许可对专有软件进行逆向工程可能违反最终用户许可协议和版权法。