# ⚡ Radare2 (r2) - Complete Cheatsheet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)

```
  ██████╗  █████╗ ██████╗  █████╗ ██████╗ ███████╗██████╗ 
  ██╔══██╗██╔══██╗██╔══██╗██╔══██╗██╔══██╗██╔════╝╚════██╗
  ██████╔╝███████║██║  ██║███████║██████╔╝█████╗   █████╔╝
  ██╔══██╗██╔══██║██║  ██║██╔══██║██╔══██╗██╔══╝  ██╔═══╝ 
  ██║  ██║██║  ██║██████╔╝██║  ██║██║  ██║███████╗███████╗
  ╚═╝  ╚═╝╚═╝  ╚═╝╚═════╝ ╚═╝  ╚═╝╚═╝  ╚═╝╚══════╝╚══════╝
      UNIX-like Command-Line Reverse Engineering Framework
```

<p align="center">
  <img src="https://img.shields.io/badge/Radare2-CLI_Reversing-blue?style=for-the-badge" alt="Radare2">
  <img src="https://img.shields.io/badge/Author-Sergi_Álvarez-orange?style=for-the-badge" alt="pancake">
  <img src="https://img.shields.io/badge/Category-Reverse_Engineering-green?style=for-the-badge" alt="Reverse Engineering">
  <img src="https://img.shields.io/badge/License-LGPLv3-purple?style=for-the-badge" alt="LGPLv3">
</p>

<p align="center">
  <b>⚡ Powerful, open-source, terminal-based framework for binary analysis, disassembly, debugging, hex editing, and binary patching</b>
</p>

---

## 📋 Table of Contents

- [Overview & Architecture](#-overview--architecture)
- [Installation](#-installation)
- [Opening Files & Session Modes](#-opening-files--session-modes)
- [Binary Analysis Commands (`a`)](#-binary-analysis-commands-a)
- [Navigation & Seeking (`s`)](#-navigation--seeking-s)
- [Printing & Disassembly (`p`)](#-printing--disassembly-p)
- [Interactive Visual & Graph Modes (`V` & `VV`)](#-interactive-visual--graph-modes-v--vv)
- [Binary Patching & Modification (`w`)](#-binary-patching--modification-w)
- [The Radare2 Companion Toolset](#-the-radare2-companion-toolset)
  - [rabin2: Binary Information Extraction](#rabin2-binary-information-extraction)
  - [rasm2: Assembler & Disassembler](#rasm2-assembler--disassembler)
  - [radiff2: Binary Difference Engine](#radiff2-binary-difference-engine)
  - [rafind2: Byte & String Search](#rafind2-byte--string-search)
  - [rax2: Base & Numeric Converter](#rax2-base--numeric-converter)
- [Debugging Basics with r2 (`-d`)](#-debugging-basics-with-r2--d)
- [Quick Reference Table](#-quick-reference-table)
- [Defensive Considerations & Anti-Reversing](#-defensive-considerations--anti-reversing)
- [Legal Disclaimer](#-legal-disclaimer)

---

## 🎯 Overview & Architecture

Created by **Sergi Álvarez (pancake)**, **Radare2** (`r2`) is a portable, modular reverse engineering framework built entirely around the UNIX philosophy. It runs entirely inside your terminal without requiring heavy graphical dependencies:
- **Full Architecture Support:** x86/x64, ARM, ARM64, MIPS, PowerPC, AVR, SPARC, WebAssembly, Java, Dalvik, and EVM.
- **Composable CLI Utilities:** Comes bundled with specialized command-line tools (`rabin2`, `rasm2`, `radiff2`, `rafind2`, `rax2`).
- **Interactive Visual Modes:** Split-screen hex editors, ASCII control flow graphs, register monitors, and decompiler views.
- **Scriptable & Extensible:** Integrates with Python, r2pipe, Ghidra (`r2ghidra`), and Rizin.

---

## 📥 Installation

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

## 🚀 Opening Files & Session Modes

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

## 🔬 Binary Analysis Commands (`a`)

Inside the `r2` shell, commands are single letters or short prefixes. You can append `?` to any command to see inline help:

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

## 🧭 Navigation & Seeking (`s`)

The seek command changes the current cursor address:

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

## 📜 Printing & Disassembly (`p`)

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

## 🖥️ Interactive Visual & Graph Modes (`V` & `VV`)

Pressing `V` or `VV` turns Radare2 into a rich interactive terminal application:

- **Enter Visual Mode:** Type `V`
  - Press `p` / `P` to cycle views (Hex dump -> Disassembly -> Debugger -> Decompiler).
  - Press `c` to toggle cursor mode (use arrow keys or `hjkl` to select bytes).
  - Press `d` to redefine bytes as code, data, or strings.
  - Press `q` to return to the standard prompt.
- **Enter Graph View:** Type `VV`
  - Renders ASCII control flow graph (CFG).
  - Use `Tab` / `Shift+Tab` to jump between conditional branch nodes.
  - Press `t` to follow the true branch; `f` to follow the false branch.
  - Press `u` to step back out of a branch.

---

## ✍️ Binary Patching & Modification (`w`)

To modify a binary on disk, ensure r2 is opened in write mode (`r2 -w target` or type `oo+` inside an active session):

```text
[0x00401150]> oo+                           # Re-open current binary with write permissions
[0x00401150]> wa nop                        # Assemble 'nop' instruction at current address
[0x00401150]> wa jmp 0x401190               # Patch a jump instruction
[0x00401150]> wx 90909090                   # Write hex bytes (4 NOPs)
[0x00401150]> w "Modified string\x00"       # Overwrite string with null-terminator
[0x00401150]> w0 16                         # Zero-fill 16 bytes (NUL padding)
```

---

## 🛠️ The Radare2 Companion Toolset

Radare2 includes standalone CLI utilities:

### rabin2: Binary Information Extraction
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

### rasm2: Assembler & Disassembler
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

### radiff2: Binary Difference Engine
```bash
# Compare two binaries and count differing bytes
radiff2 -c original.bin patched.bin

# Detailed code comparison graph
radiff2 -g main original.bin patched.bin | xdot -
```

### rafind2: Byte & String Search
```bash
# Search for ASCII string in file
rafind2 -s "admin_password" target.bin

# Search for hexadecimal pattern
rafind2 -x "4889e5" target.bin
```

### rax2: Base & Numeric Converter
```bash
# Convert hex to decimal
rax2 0x1337      # Output: 4919

# Convert decimal to hex
rax2 4919        # Output: 0x1337

# Convert string to hex
rax2 -S "admin"  # Output: 61646d696e
```

---

## 🐛 Debugging Basics with r2 (`-d`)

```bash
# Start debugging binary with arguments
r2 -d target.bin arg1 arg2
```

Inside the debugger shell:
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

## ⚡ Quick Reference Table

| Objective | Command |
|-----------|---------|
| **Deep Analysis** | `aaa` |
| **List Functions** | `afl` |
| **Seek to Function / Addr** | `s <addr>` / `s main` |
| **Disassemble Function** | `pdf` |
| **Decompile Function (r2ghidra)** | `pdc` |
| **Interactive Visual Disassembly** | `V` |
| **ASCII Flow Graph** | `VV` |
| **Assemble Instructions** | `wa <instruction>` |
| **Write Hex Bytes** | `wx <bytes>` |
| **Reopen in Read-Write** | `oo+` |
| **Extract Binary Info** | `rabin2 -I <binary>` |
| **Assemble CLI** | `rasm2 -a x86 -b 64 "<asm>"` |

---

## 🛡️ Defensive Considerations & Anti-Reversing

1. **Anti-Debugging Detection:** Radare2's debugger relies on standard Linux `ptrace`. Binaries with `ptrace(PTRACE_TRACEME)` self-checks can block attachment unless patched.
2. **Obfuscation & Control Flow Flattening:** Heavily obfuscated binaries (OLLVM) confuse basic block recovery. Use `aaaa` or symbolic execution engines (like angr) to simplify flattened functions.

---

## ⚠️ Legal Disclaimer

> [!CAUTION]
> Radare2 is a reverse engineering and analysis toolkit intended strictly for authorized security assessments, malware analysis, vulnerability research, and personal educational use. Reverse engineering proprietary software without permission may violate end-user license agreements and copyright laws.
