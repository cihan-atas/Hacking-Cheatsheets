<!-- language-home-navigation -->
[🏠 中文主页](../../README.zh-CN.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🎯 pwndbg 和 GEF - 完整备忘单

[🇹🇷 Türkçe Sürüm]（翻译/README.tr.md）
```
  ██████╗ ██╗    ██╗███╗   ██╗██████╗ ██████╗  ██████╗      ██████╗ ███████╗███████╗
  ██╔══██╗██║    ██║████╗  ██║██╔══██╗██╔══██╗██╔════╝     ██╔════╝ ██╔════╝██╔════╝
  ██████╔╝██║ █╗ ██║██╔██╗ ██║██║  ██║██████╔╝██║  ███╗    ██║  ███╗█████╗  █████╗  
  ██╔═══╝ ██║███╗██║██║╚██╗██║██║  ██║██╔══██╗██║   ██║    ██║   ██║██╔══╝  ██╔══╝  
  ██║     ╚███╔███╔╝██║ ╚████║██████╔╝██████╔╝╚██████╔╝    ╚██████╔╝███████╗██║     
  ╚═╝      ╚══╝╚══╝ ╚═╝  ╚═══╝╚═════╝ ╚═════╝  ╚═════╝      ╚═════╝ ╚══════╝╚═╝     
         Next-Gen GDB Enhancements for Reversing & Binary Exploitation
```
<p对齐=“中心”>
  <img src="https://img.shields.io/badge/pwndbg-GDB_Plugin-blue?style=for-the-badge" alt="pwndbg">
  <img src="https://img.shields.io/badge/GEF-GDB_Enhanced_Features-orange?style=for-the-badge" alt="GEF">
  <img src="https://img.shields.io/badge/Category-Binary_Exploitation_&_RE-green?style=for-the-badge" alt="反向与 Pwn">
  <img src="https://img.shields.io/badge/License-MIT_%7C_FOSS-purple?style=for-the-badge" alt="许可证 FOSS">
</p>

<p对齐=“中心”>
  <b>🎯 一流的开源 GDB 终端增强功能，专为二进制逆向工程、内存损坏分析、堆布局检查和 CTF 漏洞利用开发而设计</b>
</p>

---

## 📋 目录

- [概述以及为什么 Vanilla GDB 需要增强](#-overview--why-vanilla-gdb-needs-enhancements)
- [框架之间的安装和切换](#-installation--switching- Between-frameworks)
  - [安装 pwndbg](#installing-pwndbg)
  - [安装 GEF](#installing-gef)
  - [管理 `.gdbinit`](#managing-gdbinit)
- [上下文显示引擎](#-the-context-display-engine)
  - [寄存器部分](#registers-section)
  - [反汇编和跳转预测](#disassemble--jump-predictions)
  - [堆栈与望远镜](#stack--telescope)
  - [回溯和线程](#backtrace--线程)
- [内存检查和导航](#-内存检查--导航)
  - [伸缩指针 (`telescope`)](#telescoping-pointers-telescope)
  - [进程内存映射 (`vmmap`)](#process-memory-mappings-vmmap)
  - [搜索内存(`search`)](#searching-memory-search)
- [堆利用与检查](#-heap-exploitation--inspection)
  - [查看块和竞技场](#viewing-chunks--arena)
  - [检查空闲垃圾箱（Tcache、Fastbins、Smallbins）](#inspecting-free-bins-tcache-fastbins-smallbins)
  - [可视化堆布局 (`vis_heap_chunks`)](#visualizing-heap-layout-vis_heap_chunks)
- [利用开发助手](#-exploit-development-helpers)
  - [Checksec 和缓解审核](#checksec--mitigation-audit)
  - [堆栈金丝雀定位器 (`canary`)](#stack-canary-locator-canary)
  - [循环模式和偏移计算](#cycl-patterns--offset-calculation)
  - [内存中 ROP 小工具狩猎](#in-memory-rop-gadget-hunting)
- [快速参考表](#-quick-reference-table)
- [防御考虑因素和二元保护](#-defective-considerations--binary-protections)
- [法律免责声明](#-legal-disclaimer)

---

## 🎯 概述以及为什么 Vanilla GDB 需要增强

标准 GNU 调试器 (`gdb`) 是一款工业级工具，但其开箱即用的用户体验在视觉上非常简朴。它不会自动更新寄存器状态、取消引用多级指针或在单步执行指令时显示堆栈视图。

**pwndbg** 和 **GEF** 将 GDB 转变为精英逆向工程和二进制开发套件：
- **pwndbg：** 专为低级软件开发、堆调试（glibc 2.27 - 2.39+ 分配器内部）和自动指针取消引用而设计。
- **GEF（GDB 增强功能）：** 具有零外部依赖性的单个独立 Python 脚本，提供与体系结构无关的功能（ARM、MIPS、PowerPC、x86）和动态结构解析。

---

## 📥 安装和框架之间的切换

### 安装 pwndbg
```bash
git clone https://github.com/pwndbg/pwndbg
cd pwndbg
./setup.sh
```
### 安装 GEF
```bash
# Automated one-liner download
bash -c "$(curl -fsSL https://gef.blah.cat/sh)"
```
### 管理`.gdbinit`
要在 pwndbg 和 GEF 之间切换而不发生冲突，请配置您的 `~/.gdbinit`：
```bash
# To use pwndbg:
echo "source /path/to/pwndbg/gdbinit.py" > ~/.gdbinit

# To use GEF:
echo "source ~/.gdbinit-gef.py" > ~/.gdbinit
```
---

## 🖥️ 上下文显示引擎

每次执行在断点或步骤处停止时，pwndbg 和 GEF 都会打印一个自动的多面板终端视图：
```text
──────────────────────────[ REGISTERS / FLAGS ]──────────────────────────
RAX: 0x0 
RBX: 0x7fffffffe000 
RCX: 0x7ffff7ec2000 --> 0x00010102464c457f 
RDX: 0x7fffffffe1a0 --> 0x7fffffffe494 ('USER=zeus')
RSI: 0x7fffffffe198 --> 0x7fffffffe47e ('/bin/target')
RDI: 0x1 
RBP: 0x7fffffffe0a0 --> 0x7fffffffe0c0 --> 0x0
RSP: 0x7fffffffe080 --> 0x401156 (<main+34>: mov eax, 0)
RIP: 0x401140 (<main+12>: cmp QWORD PTR [rbp-0x8], 0x0)
──────────────────────────[ DISASM / CODE ]──────────────────────────
   0x401135 <main+1>     push   rbp
   0x401136 <main+2>     mov    rbp, rsp
   0x401139 <main+5>     sub    rsp, 0x10
 ► 0x401140 <main+12>    cmp    QWORD PTR [rbp-0x8], 0
   0x401145 <main+17>    je     0x401151 <main+29>  <-- JUMP IS TAKEN
   0x401147 <main+19>    mov    eax, 0x1
──────────────────────────[ STACK ]──────────────────────────
00:0000│ rsp 0x7fffffffe080 --> 0x401156 (<main+34>: mov eax, 0)
01:0008│     0x7fffffffe088 --> 0x0
02:0010│ rbp 0x7fffffffe0a0 --> 0x7fffffffe0c0 --> 0x0
03:0018│     0x7fffffffe0a8 --> 0x7ffff7df6d90 (<__libc_start_call_main+128>: mov edi, eax)
──────────────────────────[ BACKTRACE ]──────────────────────────
 ► 0   0x401140 main+12
   1   0x7ffff7df6d90 __libc_start_call_main+128
   2   0x7ffff7df6e40 __libc_start_main+128
   3   0x401065 _start+37
```
---

## 🔍 内存检查和导航

### 伸缩指针 (`telescope`)
递归地取消引用内存指针，显示 ASCII 文本、函数符号或目标指针：
```text
pwndbg> telescope $rsp 16       # Dereference 16 entries starting at RSP
pwndbg> telescope 0x404000 8    # Dereference data section
```
### 进程内存映射 (`vmmap`)
显示具有读/写/执行权限的完整虚拟内存段：
```text
pwndbg> vmmap
```
*快速发现具有 `rwx` 权限的段（对于 shellcode 注入至关重要）。*

### 搜索内存（`search`）
在加载的内存页中的任何位置定位字符串、整数或指针：
```text
pwndbg> search -s "/bin/sh"     # Search for string "/bin/sh"
pwndbg> search -p 0x401135      # Search for pointers to main()
pwndbg> search -x "41414141"    # Search for raw hexadecimal bytes
```
---

## 🧱 堆利用和检查

使用专用堆命令分析现代 `glibc` 堆分配器非常简单：

### 查看块和区域
```text
pwndbg> heap                    # Display entire heap layout
pwndbg> chunk 0x602000          # Parse chunk headers (size, flags: PREV_INUSE, IS_MMAPPED, NON_MAIN_ARENA)
pwndbg> arenas                  # List all active heap arenas
```
### 检查空闲垃圾箱（Tcache、Fastbins、Smallbins）
```text
pwndbg> bins                    # Show all free bins simultaneously
pwndbg> tcache                  # Display per-thread tcache entries
pwndbg> fastbins                # List single-linked fastbins
pwndbg> unsortedbin             # List unsorted bin chunks (used for libc leak)
```
### 可视化堆布局 (`vis_heap_chunks`)
```text
pwndbg> vis_heap_chunks 10      # Draw color-coded visual representation of consecutive heap chunks
```
---

## 🛠️ 利用开发助手

### 检查安全和缓解审计
直接从 GDB 提示符检查编译器安全缓解措施：
```text
pwndbg> checksec
```
*检查：*
- **RELRO：** 部分 RELRO 与完整 RELRO（保护全局偏移表 `.got.plt`）
- **堆栈：** 找到金丝雀 vs 没有金丝雀
- **NX：** 启用 NX（不可执行堆栈/堆）
- **PIE：** PIE 启用（位置独立的可执行文件/ASLR）

### 堆栈金丝雀定位器 (`canary`)
在线程本地存储 (TLS) 中找到线程金丝雀值并验证它在当前堆栈帧中的位置：
```text
pwndbg> canary
```
### 循环模式和偏移计算
确定覆盖指令指针 (EIP/RIP) 的确切偏移量：
```text
# Generate a 100-byte De Bruijn cyclic sequence
pwndbg> cyclic 100

# After the crash (SIGSEGV), calculate offset from faulty address or register value
pwndbg> cyclic -l $rip
# Output: Finding cyclic pattern at offset 72
```
### 内存中 ROP 小工具狩猎
搜索加载的可执行段以查找特定的小工具：
```text
pwndbg> rop --grep "pop rdi"
```
---

## ⚡ 快速参考表

|目标|命令 |
|------------|---------|
| **从 pwndbg/GEF 开始**| `gdb ./target_binary` |
| **检查寄存器/堆栈** | `context` |
| **递归指针解引用**| `telescope $rsp 10` |
| **内存映射和 RWX 检查**| `vmmap` |
| **在内存中搜索字符串**| `search -s "/bin/sh"` |
| **审计缓解措施** | `checksec` |
| **查找堆栈金丝雀** | `canary` |
| **堆块概述** | `heap` |
| **检查免费垃圾箱** | `bins` |
| **计算缓冲区偏移**| `cyclic 100` -> `cyclic -l <val>` |
| **在符号处设置断点**| `b *main`|
| **运行/继续/步骤** | `r` / `c` / `si` / `ni` |

---

## 🛡️ 防御考虑和二元保护

1. **完整RELRO：** 使用`-Wl,-z,relro,-z,now`编译使`.got`完全只读，击败GOT覆盖攻击。
2. **堆栈金丝雀：** 使用 `-fstack-protector-all` 编译将 cookie 放置在返回地址之前，导致 `__stack_chk_fail` 缓冲区溢出。
3. **PIE 和 ASLR：** 每次执行时随机化代码、堆和库基地址。

---

## ⚠️ 法律免责声明

> [!警告]
> pwndbg 和 GEF 是二进制分析和漏洞利用工程工具，严格用于授权漏洞研究、防御软件审计和学术 CTF 竞赛。未经许可使用软件是违法的。