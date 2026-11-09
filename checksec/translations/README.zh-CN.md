<!-- language-home-navigation -->
[🏠 中文主页](../../README.zh-CN.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🛡️ checksec 和 Binutils - 完整备忘单

[🇹🇷 Türkçe Sürüm]（翻译/README.tr.md）
```
   ██████╗██╗  ██╗███████╗ ██████╗██╗  ██╗███████╗███████╗ ██████╗
  ██╔════╝██║  ██║██╔════╝██╔════╝██║ ██╔╝██╔════╝██╔════╝██╔════╝
  ██║     ███████║█████╗  ██║     █████╔╝ ███████╗█████╗  ██║     
  ██║     ██╔══██║██╔══╝  ██║     ██╔═██╗ ╚════██║██╔══╝  ██║     
  ╚██████╗██║  ██║███████╗╚██████╗██║  ██╗███████║███████╗╚██████╗
   ╚═════╝╚═╝  ╚═╝╚══════╝ ╚═════╝╚═╝  ╚═╝╚══════╝╚══════╝ ╚═════╝
          Linux ELF Binary Mitigation Auditor & Inspection Toolkit
```
<p对齐=“中心”>
  <img src="https://img.shields.io/badge/checksec-Security_Mitigations-blue?style=for-the-badge" alt="checksec">
  <img src="https://img.shields.io/badge/readelf-ELF_Headers-orange?style=for-the-badge" alt="readelf">
  <img src="https://img.shields.io/badge/objdump-Disassemble-green?style=for-the-badge" alt="objdump">
  <img src="https://img.shields.io/badge/Platform-Linux_ELF-purple?style=for-the-badge" alt="Linux ELF">
</p>

<p对齐=“中心”>
  <b>🛡️ 用于审核 Linux 可执行安全缓解措施（RELRO、Stack Canary、NX、PIE、Fortify）和剖析 ELF 二进制头、节和符号表的基本命令行工具</b>
</p>

---

## 📋 目录

- [概述和二进制缓解矩阵](#-overview--binary-mitigations-matrix)
- [安装](#-安装)
- [checksec：快速缓解审核](#-checksec-rapid-mitigation-auditing)
  - [审核单个和批量二进制文件](#auditing-single--batch-binaries)
  - [审核运行进程](#auditing-running-processes)
  - [JSON 和 CSV 输出](#json--csv-output)
- [readelf：ELF 标头和节剖析](#-readelf-elf-header--section-dissection)
  - [ELF 文件头 (`-h`)](#elf-file-header--h)
  - [程序头和段 (`-l`)](#program-headers--segments--l)
  - [节标题 (`-S`)](#section-headers--s)
  - [符号表 (`-s` / `--syms`)](#symbol-tables--s----syms)
  - [动态依赖项和库 (`-d`)](#dynamic-dependencies--libraries--d)
- [objdump：反汇编和重定位](#-objdump-disassemble--重定位)
  - [Intel 反汇编 (`-d -M intel`)](#intel-disassemble--d--m-intel)
  - [重定位条目和 GOT (`-R`)](#relocation-entries--got--r)
- [nm 和字符串：符号解析和字符串](#-nm--strings-symbol-resolution--strings)
  - [使用 `nm` 列出 demangled 符号](#listing-demangled-symbols-with-nm)
  - [使用 `strings` 提取混淆字符串](#extracting-obfuscated-strings-with-strings)
- [强化编译参考](#-hardened-compilation-reference)
- [快速参考表](#-quick-reference-table)
- [法律免责声明](#-legal-disclaimer)

---

## 🎯 概述和二进制缓解矩阵

在开始任何逆向工程或利用工作流程之前，必须了解启用了哪些安全缓解措施：

|缓解措施 |全名 |保护机制|绕过策略|
|------------|----------|----------------------------------|-----------------|
| **RELRO** |重定位只读 |使 `.got` 为只读 (`Full RELRO`) 以防止指针覆盖 |目标堆结构、返回地址或函数挂钩 |
| **金丝雀** |堆栈保护器|将随机 cookie 放置在返回指针之前；退货时检查完整性 |通过内存泄漏或暴力破解（分叉守护进程）来泄漏金丝雀值 |
| **NX** |不执行/DEP |将堆栈、堆和数据段标记为不可执行 |面向返回的编程 (ROP) 或面向跳转的编程 (JOP) |
| **馅饼** |位置无关的可执行文件|在虚拟内存中的随机位置加载二进制基地址 (ASLR) |泄露代码指针计算基地址偏移|
| **强化** | `_FORTIFY_SOURCE` |用边界检查替换不安全的字符串函数 (`strcpy` -> `__strcpy_chk`) |非溢出逻辑错误，格式化字符串无缓冲区溢出 |

---

## 📥 安装
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y checksec binutils

# Arch Linux
sudo pacman -S checksec binutils

# Python pip (standalone checksec)
pip install checksec.py
```
---

## ⚡ checksec：快速缓解审核

### 审核单个和批量二进制文件
```bash
# Audit a single executable
checksec --file=./target_binary

# Audit all binaries in directory
checksec --dir=/usr/bin/
```
*标准输出示例：*
```text
RELRO           STACK CANARY      NX            PIE             RPATH      RUNPATH      FILE
Full RELRO      Canary found      NX enabled    PIE enabled     No RPATH   No RUNPATH   ./target_binary
```
### 审核正在运行的进程
审核活动守护进程的内存缓解和 ASLR 状态：
```bash
# Audit by Process ID (PID)
checksec --proc=1337

# Audit process by name
checksec --proc-all
```
### JSON 和 CSV 输出
导出结果以进行自动分析：
```bash
checksec --file=./target_binary --output=json > security.json
checksec --file=./target_binary --output=csv > security.csv
```
---

## 🔬 readelf：ELF 标头和节剖析

`readelf` 实用程序显示 ELF 文件中的信息，而不调用任何执行：

### ELF 文件头 (`-h`)
揭示 CPU 架构、32 位与 64 位类别、入口点地址和字节顺序：
```bash
readelf -h ./target_binary
```
### 程序头和段 (`-l`)
显示运行时加载到内存中的段和标志（`R`、`W`、`E`）。判断堆栈（`GNU_STACK`）是否有执行权限至关重要：
```bash
readelf -l ./target_binary
```
*（如果`GNU_STACK`有`RWE`，则允许堆栈执行；NX被禁用）。*

### 节标题 (`-S`)
检查 `.text`（代码）、`.data`（初始化数据）、`.rodata`（常量）、`.bss`（未初始化数据）、`.plt` 和 `.got` 的内存布局、地址和偏移量：
```bash
readelf -S ./target_binary
```
### 符号表 (`-s` / `--syms`)
列出所有导出的函数、全局变量和导入的 libc 符号：
```bash
# List all symbols
readelf -s ./target_binary

# Filter dynamic symbols used at runtime
readelf --dyn-syms ./target_binary
```
### 动态依赖项和库 (`-d`)
列出所需的共享库（`NEEDED` 标签，例如 `libc.so.6`、`libssl.so`）：
```bash
readelf -d ./target_binary
```
---

## 🧩 objdump：反汇编和重定位

### Intel 反汇编（`-d -M intel`）
将可执行部分反汇编为干净的 Intel 语法程序集：
```bash
# Disassemble .text section in Intel syntax
objdump -d -M intel ./target_binary

# Disassemble specific function (e.g. main)
objdump -d -M intel ./target_binary | grep -A 30 "<main>:"
```
### 重定位条目和 GOT (`-R`)
显示导入库调用的全局偏移表 (GOT) 重定位地址：
```bash
objdump -R ./target_binary
```
*显示 `puts`、`printf` 和 `system` 等函数的地址目标。*

---

## 🔍 nm 和字符串：符号解析和字符串

### 使用 `nm` 列出已拆解的符号
```bash
# List defined symbols only (ignore external imports)
nm --defined-only -C ./target_binary

# List undefined external symbols (functions resolved by dynamic linker)
nm -u -C ./target_binary
```
### 使用 `strings` 提取混淆字符串
```bash
# Extract ASCII strings with minimum 8 characters
strings -a -n 8 ./target_binary

# Extract 16-bit Little Endian wide strings (UTF-16 e.g., Windows / Java)
strings -e l -a ./target_binary

# Print byte offset of each string in hex
strings -a -t x ./target_binary
```
---

## 🛡️ 强化编译参考

要构建具有最大安全缓解措施的 ELF 二进制文件：
```bash
gcc -O2 \
  -Wall \
  -fstack-protector-strong \
  -D_FORTIFY_SOURCE=2 \
  -Wl,-z,relro,-z,now \
  -fPIE -pie \
  -Wl,-z,noexecstack \
  source.c -o secure_binary
```
---

## ⚡ 快速参考表

|目标|命令 |
|------------|---------|
| **审计缓解措施** | `checksec --file=<bin>` |
| **审核运行PID** | `checksec --proc=<pid>` |
| **查看 ELF 标题** | `readelf -h <bin>` |
| **查看堆栈 NX 标志** | `readelf -l <bin> \| grep -A 1 GNU_STACK` |
| **列出部分（.got、.plt）**| `readelf -S <bin>` |
| **查看动态符号** | `readelf --dyn-syms <bin>` |
| **Intel拆解** | `objdump -d -M intel <bin>` |
| **检查 GOT 搬迁** | `objdump -R <bin>` |
| **列表符号（已分解）** | `nm -C <bin>` |
| **提取 UTF-16 字符串** | `strings -e l -a <bin>` |
| **显示字符串偏移** | `strings -a -t x <bin>`|

---

## ⚠️ 法律免责声明

> [!警告]
> checksec 和 GNU Binutils 是诊断和逆向工程工具，旨在用于软件安全审计、防御验证和授权二进制研究。检查未经授权的软件必须遵守当地知识产权法规。