<!-- language-home-navigation -->
[🏠 中文主页](../../README.zh-CN.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🔬 strace 和 ltrace - 完整备忘单

[🇹🇷 Türkçe Sürüm]（翻译/README.tr.md）
```
  ███████╗████████╗██████╗  █████╗  ██████╗███████╗    ██╗  ████████╗██████╗  █████╗  ██████╗███████╗
  ██╔════╝╚══██╔══╝██╔══██╗██╔══██╗██╔════╝██╔════╝    ██║  ╚══██╔══╝██╔══██╗██╔══██╗██╔════╝██╔════╝
  ███████╗   ██║   ██████╔╝███████║██║     █████╗      ██║     ██║   ██████╔╝███████║██║     █████╗  
  ╚════██║   ██║   ██╔══██╗██╔══██║██║     ██╔══╝      ██║     ██║   ██╔══██╗██╔══██║██║     ██╔══╝  
  ███████║   ██║   ██║  ██║██║  ██║╚██████╗███████╗    ███████╗██║   ██║  ██║██║  ██║╚██████╗███████╗
  ╚══════╝   ╚═╝   ╚═╝  ╚═╝╚═╝  ╚═╝ ╚═════╝╚══════╝    ╚══════╝╚═╝   ╚═╝  ╚═╝╚═╝  ╚═╝ ╚═════╝╚══════╝
            Linux Dynamic Binary Analysis: System & Library Call Tracers
```
<p对齐=“中心”>
  <img src="https://img.shields.io/badge/strace-System_Calls-blue?style=for-the-badge" alt="strace">
  <img src="https://img.shields.io/badge/ltrace-Library_Calls-orange?style=for-the-badge" alt="ltrace">
  <img src="https://img.shields.io/badge/Platform-Linux_ELF-green?style=for-the-badge" alt="Linux">
  <img src="https://img.shields.io/badge/Category-Dynamic_Analysis-purple?style=for-the-badge" alt="动态分析">
</p>

<p对齐=“中心”>
  <b>🔬动态二进制分析的基本命令行实用程序：拦截Linux内核系统调用（strace）和用户空间动态库调用（ltrace）</b>
</p>

---

## 📋 目录

- [概述：系统调用与库调用](#-overview-system-calls-vs-library-calls)
- [安装](#-安装)
- [strace：系统调用跟踪](#-strace-system-call-tracing)
  - [基本跟踪执行](#basic-tracing-execution)
  - [按类或名称过滤系统调用 (`-e`)](#filtering-syscalls-by-class-or-name--e)
  - [跟踪子进程和多线程进程 (`-f`)](#tracing-child--多线程进程--f)
  - [附加到正在运行的进程 (`-p`)](#attaching-to-running-processes--p)
  - [扩展字符串和缓冲区输出 (`-s`)](#expanding-strings--buffer-outputs--s)
  - [统计分析和瓶颈分析 (`-c`)](#statistical-profiling--bottleneck-analysis--c)
  - [用于模糊测试和测试的故障注入 (`-e fault`)](#fault-injection-for-fuzzing--testing--e-fault)
- [ltrace：动态库调用跟踪](#-ltrace-dynamic-library-call-tracing)
  - [基本库拦截](#basic-library-interception)
  - [破解硬编码密码检查 (`strcmp`)](#cracking-hardcoded-password-checks-strcmp)
  - [分解 C++ 符号 (`-C`)](#demangling-c-symbols--c)
  - [过滤特定库函数](#filtering-specific-library-functions)
- [组合 strace 和 ltrace 工作流程](#-combining-strace-and-ltrace-workflows)
- [快速参考表](#-quick-reference-table)
- [防御考虑与反追踪](#-defective-considerations--anti-tracing)
- [法律免责声明](#-legal-disclaimer)

---

## 🎯 概述：系统调用与库调用

在分析已编译的 Linux ELF 二进制文件时，混淆、剥离或打包可能会阻碍静态分析。动态跟踪提供对运行时操作的即时可见性：

1. **系统调用（`strace`）：**
   - 用户空间应用程序和 Linux Linux 内核之间的接口。
   - 跟踪磁盘 I/O（`openat`、`read`、`write`）、网络（`socket`、`connect`）、内存分配（`mmap`、`brk`）和进程管理（`fork`、`execve`、`ptrace`）。
2. **库调用（`ltrace`）：**
   - 二进制和动态共享库（例如`libc.so`、`libssl.so`）之间的接口。
   - 拦截 `strcmp()`、`strlen()`、`malloc()`、`system()` 等调用和加密例程，经常在加密之前暴露内存中的明文凭据。

---

## 📥 安装
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y strace ltrace

# Arch Linux
sudo pacman -S strace ltrace

# Fedora / Red Hat / CentOS
sudo dnf install -y strace ltrace
```
---

## ⚡ strace：系统调用跟踪

### 基本跟踪执行
运行目标二进制文件并将所有系统调用流式传输到标准错误：
```bash
strace ./target_binary
```
将输出重定向到文件，以免扰乱程序执行：
```bash
strace -o trace.log ./target_binary
```
### 按类或名称过滤系统调用 (`-e`)
通过仅过滤相关的系统调用类来避免噪音：
```bash
# Trace ONLY file-related operations (open, read, write, stat, access)
strace -e trace=file ./target_binary

# Trace ONLY network socket connections and data transfers
strace -e trace=network ./target_binary

# Trace process execution and fork/clone operations
strace -e trace=process ./target_binary

# Filter specific syscalls by exact name
strace -e trace=open,openat,read,write ./target_binary
```
### 跟踪子进程和多线程进程 (`-f`)
现代二进制文件和恶意软件会产生子工作进程或线程。如果没有 `-f`，strace 将会错过它们：
```bash
# Trace all child forks and threads
strace -f ./target_binary

# Trace children and save each process output into separate files (trace.pid)
strace -ff -o trace ./target_binary
```
### 附加到正在运行的进程 (`-p`)
监控服务或活动恶意软件进程而不重新启动它：
```bash
# Attach by Process ID (PID)
sudo strace -p 1337

# Attach to process and follow all newly spawned threads
sudo strace -f -p 1337 -o attached.log
```
### 扩展字符串和缓冲区输出 (`-s`)
默认情况下，strace 将打印的字符串截断为 32 个字符。增加字符串长度以检查完整文件路径、URL 或有效负载：
```bash
# Display up to 2048 characters of strings and buffers
strace -s 2048 -e trace=read,write ./target_binary
```
### 统计分析和瓶颈分析 (`-c`)
计算每个系统调用所花费的时间、调用次数和错误：
```bash
strace -c ./target_binary
```
*输出摘要示例：*
```text
% time     seconds  usecs/call     calls    errors syscall
------ ----------- ----------- --------- --------- ----------------
 54.32    0.001200          40        30           read
 32.10    0.000710          71        10         2 openat
 13.58    0.000300          15        20           write
```
### 用于模糊测试和测试的故障注入 (`-e fault`)
将任意错误注入系统调用以评估弹性：
```bash
# Force the 3rd invocation of openat() to fail with Permission Denied (EACCES)
strace -e fault=openat:error=EACCES:when=3 ./target_binary
```
---

## 🧩 ltrace：动态库调用跟踪

### 基本库拦截
拦截对动态 `.so` 库的调用：
```bash
ltrace ./target_binary
```
### 破解硬编码密码检查 (`strcmp`)
在逆向工程 CTF 和质询二进制文件中，密码比较函数（`strcmp`、`strncmp`、`memcmp`）以明文参数揭示目标密码：
```bash
ltrace -s 128 -e strcmp,strncmp ./license_check
```
*输出示例：*
```text
strcmp("user_input", "SUPER_SECRET_FLAG_2026") = -1
```
### 分解 C++ 符号 (`-C`)
Demangle 编译器损坏的 C++ 方法名称 (`_ZNK4Test3fooEv` -> `Test::foo()`)：
```bash
ltrace -C ./cpp_binary
```
### 过滤特定的库函数
```bash
# Intercept only memory allocation and string functions
ltrace -e malloc+free+strdup ./target_binary

# Intercept calls originating from specific dynamic libraries
ltrace -l /lib/x86_64-linux-gnu/libcrypto.so.3 ./target_binary
```
---

## 🔄 组合 strace 和 ltrace 工作流程

对未知二进制文件执行初始分类时：

1. **第 1 步：** 运行 `strace -e trace=file,network ./binary` 以查看它与哪些文件和套接字交互。
2. **第 2 步：** 运行 `ltrace -s 256 ./binary` 以发现内部库字符串比较、加密调用和内存逻辑。
3. **第 3 步：** 组合这两个工具：
   ```bash
   ltrace -S ./binary
   ```
*（`-S` 标志告诉 ltrace 在单个统一视图中显示系统调用和库调用）。*

---

## ⚡ 快速参考表

|目标|命令 |
|------------|---------|
| **跟踪所有系统调用** | `strace ./binary` |
| **登录到文件** | `strace -o log.txt ./binary` |
| **跟踪文件操作** | `strace -e trace=file ./binary` |
| **跟踪网络活动** | `strace -e trace=network ./binary` |
| **跟随子进程** | `strace -f ./binary` |
| **附加到正在运行的 PID** | `sudo strace -f -p <PID>` |
| **扩展字符串长度** | `strace -s 2048 ./binary` |
| **系统调用摘要统计** | `strace -c ./binary` |
| **跟踪库调用** | `ltrace ./binary` |
| **捕获密码检查** | `ltrace -s 128 -e strcmp,strncmp ./binary`|
| **Demangle C++ 名称** | `ltrace -C ./binary`|
| **组合库 + 系统调用**| `ltrace -S ./binary` |

---

## 🛡️ 防御考虑和反追踪

1. **Ptrace 自检：** Linux 仅允许单个跟踪器通过 `ptrace` 附加到进程。如果 `strace` 或 `ltrace` 已连接，则调用 `ptrace(PTRACE_TRACEME, 0, 1, 0)` 将失败。
2. **静态链接（`-static`）：** 使用静态链接编译的二进制文件不包含动态库引用（`DT_NEEDED`），使`ltrace`完全无效（使用`strace`代替）。

---

## ⚠️ 法律免责声明

> [!警告]
> strace 和 ltrace 是动态诊断工具，专为软件开发、调试和授权安全评估而设计。严禁在未经许可的情况下跟踪未经授权的进程或拦截专有通信。