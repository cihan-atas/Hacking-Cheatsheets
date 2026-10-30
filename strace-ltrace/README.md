# 🔬 strace & ltrace - Complete Cheatsheet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)

```
  ███████╗████████╗██████╗  █████╗  ██████╗███████╗    ██╗  ████████╗██████╗  █████╗  ██████╗███████╗
  ██╔════╝╚══██╔══╝██╔══██╗██╔══██╗██╔════╝██╔════╝    ██║  ╚══██╔══╝██╔══██╗██╔══██╗██╔════╝██╔════╝
  ███████╗   ██║   ██████╔╝███████║██║     █████╗      ██║     ██║   ██████╔╝███████║██║     █████╗  
  ╚════██║   ██║   ██╔══██╗██╔══██║██║     ██╔══╝      ██║     ██║   ██╔══██╗██╔══██║██║     ██╔══╝  
  ███████║   ██║   ██║  ██║██║  ██║╚██████╗███████╗    ███████╗██║   ██║  ██║██║  ██║╚██████╗███████╗
  ╚══════╝   ╚═╝   ╚═╝  ╚═╝╚═╝  ╚═╝ ╚═════╝╚══════╝    ╚══════╝╚═╝   ╚═╝  ╚═╝╚═╝  ╚═╝ ╚═════╝╚══════╝
            Linux Dynamic Binary Analysis: System & Library Call Tracers
```

<p align="center">
  <img src="https://img.shields.io/badge/strace-System_Calls-blue?style=for-the-badge" alt="strace">
  <img src="https://img.shields.io/badge/ltrace-Library_Calls-orange?style=for-the-badge" alt="ltrace">
  <img src="https://img.shields.io/badge/Platform-Linux_ELF-green?style=for-the-badge" alt="Linux">
  <img src="https://img.shields.io/badge/Category-Dynamic_Analysis-purple?style=for-the-badge" alt="Dynamic Analysis">
</p>

<p align="center">
  <b>🔬 Essential command-line utilities for dynamic binary analysis: intercepting Linux kernel system calls (strace) and user-space dynamic library calls (ltrace)</b>
</p>

---

## 📋 Table of Contents

- [Overview: System Calls vs Library Calls](#-overview-system-calls-vs-library-calls)
- [Installation](#-installation)
- [strace: System Call Tracing](#-strace-system-call-tracing)
  - [Basic Tracing Execution](#basic-tracing-execution)
  - [Filtering Syscalls by Class or Name (`-e`)](#filtering-syscalls-by-class-or-name--e)
  - [Tracing Child & Multi-Threaded Processes (`-f`)](#tracing-child--multi-threaded-processes--f)
  - [Attaching to Running Processes (`-p`)](#attaching-to-running-processes--p)
  - [Expanding Strings & Buffer Outputs (`-s`)](#expanding-strings--buffer-outputs--s)
  - [Statistical Profiling & Bottleneck Analysis (`-c`)](#statistical-profiling--bottleneck-analysis--c)
  - [Fault Injection for Fuzzing & Testing (`-e fault`)](#fault-injection-for-fuzzing--testing--e-fault)
- [ltrace: Dynamic Library Call Tracing](#-ltrace-dynamic-library-call-tracing)
  - [Basic Library Interception](#basic-library-interception)
  - [Cracking Hardcoded Password Checks (`strcmp`)](#cracking-hardcoded-password-checks-strcmp)
  - [Demangling C++ Symbols (`-C`)](#demangling-c-symbols--c)
  - [Filtering Specific Library Functions](#filtering-specific-library-functions)
- [Combining strace and ltrace Workflows](#-combining-strace-and-ltrace-workflows)
- [Quick Reference Table](#-quick-reference-table)
- [Defensive Considerations & Anti-Tracing](#-defensive-considerations--anti-tracing)
- [Legal Disclaimer](#-legal-disclaimer)

---

## 🎯 Overview: System Calls vs Library Calls

When analyzing compiled Linux ELF binaries, static analysis can be impeded by obfuscation, stripping, or packing. Dynamic tracing provides immediate visibility into runtime actions:

1. **System Calls (`strace`):**
   - The interface between user-space applications and the Linux Linux kernel.
   - Traces disk I/O (`openat`, `read`, `write`), networking (`socket`, `connect`), memory allocation (`mmap`, `brk`), and process management (`fork`, `execve`, `ptrace`).
2. **Library Calls (`ltrace`):**
   - The interface between the binary and dynamic shared libraries (such as `libc.so`, `libssl.so`).
   - Intercepts calls like `strcmp()`, `strlen()`, `malloc()`, `system()`, and cryptographic routines, frequently exposing plaintext credentials in memory before encryption.

---

## 📥 Installation

```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y strace ltrace

# Arch Linux
sudo pacman -S strace ltrace

# Fedora / Red Hat / CentOS
sudo dnf install -y strace ltrace
```

---

## ⚡ strace: System Call Tracing

### Basic Tracing Execution
Run a target binary and stream all system calls to standard error:
```bash
strace ./target_binary
```

Redirect output to a file so it doesn't clutter program execution:
```bash
strace -o trace.log ./target_binary
```

### Filtering Syscalls by Class or Name (`-e`)
Avoid noise by filtering only relevant syscall classes:

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

### Tracing Child & Multi-Threaded Processes (`-f`)
Modern binaries and malware spawn child worker processes or threads. Without `-f`, strace will miss them:
```bash
# Trace all child forks and threads
strace -f ./target_binary

# Trace children and save each process output into separate files (trace.pid)
strace -ff -o trace ./target_binary
```

### Attaching to Running Processes (`-p`)
Monitor a service or active malware process without restarting it:
```bash
# Attach by Process ID (PID)
sudo strace -p 1337

# Attach to process and follow all newly spawned threads
sudo strace -f -p 1337 -o attached.log
```

### Expanding Strings & Buffer Outputs (`-s`)
By default, strace truncates printed strings at 32 characters. Increase string length to inspect full file paths, URLs, or payloads:
```bash
# Display up to 2048 characters of strings and buffers
strace -s 2048 -e trace=read,write ./target_binary
```

### Statistical Profiling & Bottleneck Analysis (`-c`)
Count time spent, number of calls, and errors per syscall:
```bash
strace -c ./target_binary
```
*Output Summary Example:*
```text
% time     seconds  usecs/call     calls    errors syscall
------ ----------- ----------- --------- --------- ----------------
 54.32    0.001200          40        30           read
 32.10    0.000710          71        10         2 openat
 13.58    0.000300          15        20           write
```

### Fault Injection for Fuzzing & Testing (`-e fault`)
Inject arbitrary errors into syscalls to evaluate resilience:
```bash
# Force the 3rd invocation of openat() to fail with Permission Denied (EACCES)
strace -e fault=openat:error=EACCES:when=3 ./target_binary
```

---

## 🧩 ltrace: Dynamic Library Call Tracing

### Basic Library Interception
Intercept calls made to dynamic `.so` libraries:
```bash
ltrace ./target_binary
```

### Cracking Hardcoded Password Checks (`strcmp`)
In reverse engineering CTFs and challenge binaries, password comparison functions (`strcmp`, `strncmp`, `memcmp`) reveal the target password in plaintext arguments:
```bash
ltrace -s 128 -e strcmp,strncmp ./license_check
```
*Output Example:*
```text
strcmp("user_input", "SUPER_SECRET_FLAG_2026") = -1
```

### Demangling C++ Symbols (`-C`)
Demangle compiler-mangled C++ method names (`_ZNK4Test3fooEv` -> `Test::foo()`):
```bash
ltrace -C ./cpp_binary
```

### Filtering Specific Library Functions
```bash
# Intercept only memory allocation and string functions
ltrace -e malloc+free+strdup ./target_binary

# Intercept calls originating from specific dynamic libraries
ltrace -l /lib/x86_64-linux-gnu/libcrypto.so.3 ./target_binary
```

---

## 🔄 Combining strace and ltrace Workflows

When performing initial triage on an unknown binary:

1. **Step 1:** Run `strace -e trace=file,network ./binary` to see what files and sockets it interacts with.
2. **Step 2:** Run `ltrace -s 256 ./binary` to uncover internal library string comparisons, crypto calls, and memory logic.
3. **Step 3:** Combine both tools:
   ```bash
   ltrace -S ./binary
   ```
   *(The `-S` flag tells ltrace to display system calls alongside library calls in a single unified view).*

---

## ⚡ Quick Reference Table

| Objective | Command |
|-----------|---------|
| **Trace All Syscalls** | `strace ./binary` |
| **Log to File** | `strace -o log.txt ./binary` |
| **Trace File Operations** | `strace -e trace=file ./binary` |
| **Trace Network Activity** | `strace -e trace=network ./binary` |
| **Follow Child Processes** | `strace -f ./binary` |
| **Attach to Running PID** | `sudo strace -f -p <PID>` |
| **Expand String Length** | `strace -s 2048 ./binary` |
| **Syscall Summary Stats** | `strace -c ./binary` |
| **Trace Library Calls** | `ltrace ./binary` |
| **Catch Password Check** | `ltrace -s 128 -e strcmp,strncmp ./binary` |
| **Demangle C++ Names** | `ltrace -C ./binary` |
| **Combined Lib + Syscall**| `ltrace -S ./binary` |

---

## 🛡️ Defensive Considerations & Anti-Tracing

1. **Ptrace Self-Checking:** Linux allows only a single tracer to attach to a process via `ptrace`. Calling `ptrace(PTRACE_TRACEME, 0, 1, 0)` will fail if `strace` or `ltrace` is already attached.
2. **Static Linking (`-static`):** Binaries compiled with static linking contain no dynamic library references (`DT_NEEDED`), rendering `ltrace` completely ineffective (use `strace` instead).

---

## ⚠️ Legal Disclaimer

> [!CAUTION]
> strace and ltrace are dynamic diagnostic tools designed for software development, debugging, and authorized security assessments. Tracing unauthorized processes or intercepting proprietary communication without permission is strictly prohibited.
