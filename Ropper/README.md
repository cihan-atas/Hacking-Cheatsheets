# 🧩 Ropper & ROPgadget - Complete Cheatsheet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)

```
  ██████╗  ██████╗ ██████╗ ██████╗ ███████╗██████╗ 
  ██╔══██╗██╔═══██╗██╔══██╗██╔══██╗██╔════╝██╔══██╗
  ██████╔╝██║   ██║██████╔╝██████╔╝█████╗  ██████╔╝
  ██╔══██╗██║   ██║██╔═══╝ ██╔═══╝ ██╔══╝  ██╔══██╗
  ██║  ██║╚██████╔╝██║     ██║     ███████╗██║  ██║
  ╚═╝  ╚═╝ ╚═════╝ ╚═╝     ╚═╝     ╚══════╝╚═╝  ╚═╝
     Advanced ROP & JOP Gadget Finder and Chain Generator
```

<p align="center">
  <img src="https://img.shields.io/badge/Ropper-Gadget_Finder-blue?style=for-the-badge" alt="Ropper">
  <img src="https://img.shields.io/badge/ROPgadget-Exploit_Tool-orange?style=for-the-badge" alt="ROPgadget">
  <img src="https://img.shields.io/badge/Bypass-NX_%7C_DEP-red?style=for-the-badge" alt="NX Bypass">
  <img src="https://img.shields.io/badge/Category-Exploit_Development-green?style=for-the-badge" alt="Exploit Dev">
</p>

<p align="center">
  <b>🧩 Essential command-line utilities for discovering Return-Oriented Programming (ROP) and Jump-Oriented Programming (JOP) gadgets to bypass Non-Executable (NX / DEP) memory protections</b>
</p>

---

## 📋 Table of Contents

- [Overview & ROP Theory](#-overview--rop-theory)
- [Installation](#-installation)
- [Basic Gadget Discovery](#-basic-gadget-discovery)
  - [Extracting All Gadgets](#extracting-all-gadgets)
  - [Limiting Instruction Depth](#limiting-instruction-depth)
- [Advanced Search & Pattern Filtering](#-advanced-search--pattern-filtering)
  - [Targeting Specific Registers (`rdi`, `rsi`, `rdx`, `rax`)](#targeting-specific-registers-rdi-rsi-rdx-rax)
  - [Memory Write Gadgets (Write-What-Where)](#memory-write-gadgets-write-what-where)
  - [Syscall & Interrupt Gadgets](#syscall--interrupt-gadgets)
- [Excluding Bad Characters (`--badbytes`)](#-excluding-bad-characters---badbytes)
- [Automated ROP Chain Generation](#-automated-rop-chain-generation)
  - [Ropper Automated Chains](#ropper-automated-chains)
  - [ROPgadget Automated Chains](#ropgadget-automated-chains)
- [Analyzing Loaded Libraries (libc ROP Chains)](#-analyzing-loaded-libraries-libc-rop-chains)
- [Quick Reference Table](#-quick-reference-table)
- [Defensive Considerations & Mitigations](#-defensive-considerations--mitigations)
- [Legal Disclaimer](#-legal-disclaimer)

---

## 🎯 Overview & ROP Theory

Modern operating systems enforce **NX (No-Execute) / DEP (Data Execution Prevention)**, which prevents direct execution of shellcode placed on the stack or heap.

**Return-Oriented Programming (ROP)** circumvents this restriction by reusing small existing machine instructions already present in executable memory (`.text` section of the binary or shared libraries like `libc.so`). Each instruction sequence ending in a `ret` instruction is called a **gadget**. By arranging addresses on the stack, attackers chain gadgets together to:
1. Load arguments into CPU registers (e.g., `pop rdi; ret` for x64 calling conventions).
2. Write strings (like `/bin/sh`) into writable `.bss` or `.data` sections.
3. Trigger `execve("/bin/sh", NULL, NULL)` or invoke `system()`.

---

## 📥 Installation

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

## 🚀 Basic Gadget Discovery

### Extracting All Gadgets
```bash
# Using Ropper (Faster multi-threaded engine)
ropper --file ./target_binary --nocolor

# Using ROPgadget
ROPgadget --binary ./target_binary
```

### Limiting Instruction Depth
Reduce noisy long chains by restricting gadget length (number of instructions before `ret`):
```bash
# Limit to maximum 3 instructions before return
ropper --file ./target_binary --inst-count 3
ROPgadget --binary ./target_binary --depth 3
```

---

## 🔍 Advanced Search & Pattern Filtering

### Targeting Specific Registers (`rdi`, `rsi`, `rdx`, `rax`)
In 64-bit Linux calling convention, the first three arguments to a function or syscall must reside in `rdi`, `rsi`, and `rdx`:

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

Using ROPgadget regex matching:
```bash
ROPgadget --binary ./target_binary --only "pop|ret" | grep -E "rdi|rsi|rdx|rax"
```

### Memory Write Gadgets (Write-What-Where)
When you need to store `/bin/sh` or arbitrary arguments into memory:
```bash
# Find instructions moving registers into pointer memory
ropper --file ./target_binary --search "mov [???], ???"
```
*Example match:* `mov qword ptr [rdi], rsi; ret` allows writing whatever is in `rsi` to the address pointed to by `rdi`.

### Syscall & Interrupt Gadgets
```bash
# Find raw syscall instructions
ropper --file ./target_binary --search "syscall"

# x86 32-bit software interrupt
ropper --file ./target_binary --search "int 0x80"
```

---

## 🚫 Excluding Bad Characters (`--badbytes`)

String formatting vulnerabilities or functions like `strcpy()` terminate on null bytes (`\x00`), newlines (`\x0a`), or carriage returns (`\x0d`). Exclude addresses containing these bytes:

```bash
# Filter out gadgets whose addresses contain bad characters
ropper --file ./target_binary --badbytes 000a0d --search "pop rdi"
ROPgadget --binary ./target_binary --badbytes "00|0a|0d"
```

---

## ⚡ Automated ROP Chain Generation

Both tools can automatically build functional Python exploit templates that invoke `execve("/bin/sh")`:

### Ropper Automated Chains
```bash
ropper --file ./target_binary --chain "execve"
```

### ROPgadget Automated Chains
```bash
ROPgadget --binary ./target_binary --ropchain
```
*This produces a ready-to-use Python payload skeleton chaining `mprotect()` or `execve()`.*

---

## 📚 Analyzing Loaded Libraries (libc ROP Chains)

When the binary itself is compiled as **PIE** or contains few useful gadgets, extract gadgets directly from the target system's shared C library (`libc.so.6`):

```bash
# Search gadgets in libc
ropper --file /lib/x86_64-linux-gnu/libc.so.6 --search "pop rdi; ret"

# Find "/bin/sh" string offset inside libc
strings -a -t x /lib/x86_64-linux-gnu/libc.so.6 | grep "/bin/sh"
```
*(Add discovered offsets to the leaked base address of libc at runtime).*

---

## ⚡ Quick Reference Table

| Objective | Ropper Command | ROPgadget Command |
|-----------|----------------|-------------------|
| **Dump All Gadgets** | `ropper --file <bin>` | `ROPgadget --binary <bin>` |
| **Search Specific Gadget** | `ropper --file <bin> --search "pop rdi"` | `ROPgadget --binary <bin> \| grep "pop rdi"` |
| **Filter Bad Bytes** | `ropper --file <bin> --badbytes 000a` | `ROPgadget --binary <bin> --badbytes "00\|0a"` |
| **Limit Depth / Length** | `ropper --file <bin> --inst-count 3` | `ROPgadget --binary <bin> --depth 3` |
| **Search JOP Gadgets** | `ropper --file <bin> --type jop` | `ROPgadget --binary <bin> --norop` |
| **Auto Build ROP Chain** | `ropper --file <bin> --chain "execve"`| `ROPgadget --binary <bin> --ropchain` |
| **Search Memory Writes** | `ropper --file <bin> --search "mov [???], ???"` | `ROPgadget --binary <bin> \| grep "mov \["` |

---

## 🛡️ Defensive Considerations & Mitigations

1. **Control-flow Enforcement Technology (Intel CET / Shadow Stack):** Modern processors maintain an isolated hardware shadow stack that verifies return addresses upon `ret`, terminating ROP chains immediately.
2. **Clang / GCC Control Flow Integrity (CFI):** Validates function pointer targets against a forward-edge graph before indirect calls.

---

## ⚠️ Legal Disclaimer

> [!CAUTION]
> Ropper and ROPgadget are exploit development and software security evaluation utilities. They must strictly be used in authorized penetration tests, CTF challenges, or defensive security research. Exploiting software without explicit authorization is illegal.
