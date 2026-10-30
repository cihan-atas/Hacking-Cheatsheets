# 🎯 pwndbg & GEF - Complete Cheatsheet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)

```
  ██████╗ ██╗    ██╗███╗   ██╗██████╗ ██████╗  ██████╗      ██████╗ ███████╗███████╗
  ██╔══██╗██║    ██║████╗  ██║██╔══██╗██╔══██╗██╔════╝     ██╔════╝ ██╔════╝██╔════╝
  ██████╔╝██║ █╗ ██║██╔██╗ ██║██║  ██║██████╔╝██║  ███╗    ██║  ███╗█████╗  █████╗  
  ██╔═══╝ ██║███╗██║██║╚██╗██║██║  ██║██╔══██╗██║   ██║    ██║   ██║██╔══╝  ██╔══╝  
  ██║     ╚███╔███╔╝██║ ╚████║██████╔╝██████╔╝╚██████╔╝    ╚██████╔╝███████╗██║     
  ╚═╝      ╚══╝╚══╝ ╚═╝  ╚═══╝╚═════╝ ╚═════╝  ╚═════╝      ╚═════╝ ╚══════╝╚═╝     
         Next-Gen GDB Enhancements for Reversing & Binary Exploitation
```

<p align="center">
  <img src="https://img.shields.io/badge/pwndbg-GDB_Plugin-blue?style=for-the-badge" alt="pwndbg">
  <img src="https://img.shields.io/badge/GEF-GDB_Enhanced_Features-orange?style=for-the-badge" alt="GEF">
  <img src="https://img.shields.io/badge/Category-Binary_Exploitation_&_RE-green?style=for-the-badge" alt="Reversing & Pwn">
  <img src="https://img.shields.io/badge/License-MIT_%7C_FOSS-purple?style=for-the-badge" alt="License FOSS">
</p>

<p align="center">
  <b>🎯 The premier open-source GDB terminal enhancements designed for binary reverse engineering, memory corruption analysis, heap layout inspection, and CTF exploit development</b>
</p>

---

## 📋 Table of Contents

- [Overview & Why Vanilla GDB Needs Enhancements](#-overview--why-vanilla-gdb-needs-enhancements)
- [Installation & Switching Between Frameworks](#-installation--switching-between-frameworks)
  - [Installing pwndbg](#installing-pwndbg)
  - [Installing GEF](#installing-gef)
  - [Managing `.gdbinit`](#managing-gdbinit)
- [The Context Display Engine](#-the-context-display-engine)
  - [Registers Section](#registers-section)
  - [Disassembly & Jump Predictions](#disassembly--jump-predictions)
  - [Stack & Telescope](#stack--telescope)
  - [Backtrace & Threads](#backtrace--threads)
- [Memory Inspection & Navigation](#-memory-inspection--navigation)
  - [Telescoping Pointers (`telescope`)](#telescoping-pointers-telescope)
  - [Process Memory Mappings (`vmmap`)](#process-memory-mappings-vmmap)
  - [Searching Memory (`search`)](#searching-memory-search)
- [Heap Exploitation & Inspection](#-heap-exploitation--inspection)
  - [Viewing Chunks & Arenas](#viewing-chunks--arenas)
  - [Inspecting Free Bins (Tcache, Fastbins, Smallbins)](#inspecting-free-bins-tcache-fastbins-smallbins)
  - [Visualizing Heap Layout (`vis_heap_chunks`)](#visualizing-heap-layout-vis_heap_chunks)
- [Exploit Development Helpers](#-exploit-development-helpers)
  - [Checksec & Mitigation Audit](#checksec--mitigation-audit)
  - [Stack Canary Locator (`canary`)](#stack-canary-locator-canary)
  - [Cyclic Patterns & Offset Calculation](#cyclic-patterns--offset-calculation)
  - [In-Memory ROP Gadget Hunting](#in-memory-rop-gadget-hunting)
- [Quick Reference Table](#-quick-reference-table)
- [Defensive Considerations & Binary Protections](#-defensive-considerations--binary-protections)
- [Legal Disclaimer](#-legal-disclaimer)

---

## 🎯 Overview & Why Vanilla GDB Needs Enhancements

Standard GNU Debugger (`gdb`) is an industrial-strength tool, but its out-of-the-box user experience is visually austere. It does not automatically update register states, dereference multi-level pointers, or display stack views upon stepping through instructions.

**pwndbg** and **GEF** turn GDB into an elite reverse engineering and binary exploitation suite:
- **pwndbg:** Specifically engineered for low-level software exploitation, heap debugging (glibc 2.27 - 2.39+ allocator internals), and automated pointer dereferencing.
- **GEF (GDB Enhanced Features):** A single standalone Python script with zero external dependencies, offering architecture-agnostic features (ARM, MIPS, PowerPC, x86) and dynamic structure parsing.

---

## 📥 Installation & Switching Between Frameworks

### Installing pwndbg
```bash
git clone https://github.com/pwndbg/pwndbg
cd pwndbg
./setup.sh
```

### Installing GEF
```bash
# Automated one-liner download
bash -c "$(curl -fsSL https://gef.blah.cat/sh)"
```

### Managing `.gdbinit`
To switch between pwndbg and GEF without conflict, configure your `~/.gdbinit`:
```bash
# To use pwndbg:
echo "source /path/to/pwndbg/gdbinit.py" > ~/.gdbinit

# To use GEF:
echo "source ~/.gdbinit-gef.py" > ~/.gdbinit
```

---

## 🖥️ The Context Display Engine

Every time execution halts at a breakpoint or step, both pwndbg and GEF print an automated, multi-panel terminal view:

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

## 🔍 Memory Inspection & Navigation

### Telescoping Pointers (`telescope`)
Dereferences memory pointers recursively, displaying ASCII text, function symbols, or target pointers:
```text
pwndbg> telescope $rsp 16       # Dereference 16 entries starting at RSP
pwndbg> telescope 0x404000 8    # Dereference data section
```

### Process Memory Mappings (`vmmap`)
Displays complete virtual memory segments with read/write/execute permissions:
```text
pwndbg> vmmap
```
*Quickly spot segments with `rwx` permissions (vital for shellcode injection).*

### Searching Memory (`search`)
Locate strings, integers, or pointers anywhere across loaded memory pages:
```text
pwndbg> search -s "/bin/sh"     # Search for string "/bin/sh"
pwndbg> search -p 0x401135      # Search for pointers to main()
pwndbg> search -x "41414141"    # Search for raw hexadecimal bytes
```

---

## 🧱 Heap Exploitation & Inspection

Analyzing modern `glibc` heap allocators is straightforward with dedicated heap commands:

### Viewing Chunks & Arenas
```text
pwndbg> heap                    # Display entire heap layout
pwndbg> chunk 0x602000          # Parse chunk headers (size, flags: PREV_INUSE, IS_MMAPPED, NON_MAIN_ARENA)
pwndbg> arenas                  # List all active heap arenas
```

### Inspecting Free Bins (Tcache, Fastbins, Smallbins)
```text
pwndbg> bins                    # Show all free bins simultaneously
pwndbg> tcache                  # Display per-thread tcache entries
pwndbg> fastbins                # List single-linked fastbins
pwndbg> unsortedbin             # List unsorted bin chunks (used for libc leak)
```

### Visualizing Heap Layout (`vis_heap_chunks`)
```text
pwndbg> vis_heap_chunks 10      # Draw color-coded visual representation of consecutive heap chunks
```

---

## 🛠️ Exploit Development Helpers

### Checksec & Mitigation Audit
Check compiler security mitigations directly from the GDB prompt:
```text
pwndbg> checksec
```
*Inspects:*
- **RELRO:** Partial vs Full RELRO (protects Global Offset Table `.got.plt`)
- **Stack:** Canary found vs No canary
- **NX:** NX enabled (Non-executable stack/heap)
- **PIE:** PIE enabled (Position Independent Executable / ASLR)

### Stack Canary Locator (`canary`)
Locate the thread canary value in the Thread Local Storage (TLS) and verify where it resides on the current stack frame:
```text
pwndbg> canary
```

### Cyclic Patterns & Offset Calculation
Determine the exact offset to overwrite the instruction pointer (EIP/RIP):
```text
# Generate a 100-byte De Bruijn cyclic sequence
pwndbg> cyclic 100

# After the crash (SIGSEGV), calculate offset from faulty address or register value
pwndbg> cyclic -l $rip
# Output: Finding cyclic pattern at offset 72
```

### In-Memory ROP Gadget Hunting
Search loaded executable segments for specific gadgets:
```text
pwndbg> rop --grep "pop rdi"
```

---

## ⚡ Quick Reference Table

| Objective | Command |
|-----------|---------|
| **Start with pwndbg/GEF**| `gdb ./target_binary` |
| **Inspect Registers/Stack** | `context` |
| **Recursive Pointer Deref**| `telescope $rsp 10` |
| **Memory Map & RWX Check**| `vmmap` |
| **Search String in Memory**| `search -s "/bin/sh"` |
| **Audit Mitigations** | `checksec` |
| **Find Stack Canary** | `canary` |
| **Heap Chunks Overview** | `heap` |
| **Inspect Free Bins** | `bins` |
| **Calculate Buffer Offset**| `cyclic 100` -> `cyclic -l <val>` |
| **Set Breakpoint at Symbol**| `b *main` |
| **Run / Continue / Step** | `r` / `c` / `si` / `ni` |

---

## 🛡️ Defensive Considerations & Binary Protections

1. **Full RELRO:** Compiling with `-Wl,-z,relro,-z,now` makes `.got` completely read-only, defeating GOT overwrite attacks.
2. **Stack Canaries:** Compiling with `-fstack-protector-all` places cookies before return addresses, causing `__stack_chk_fail` on buffer overflows.
3. **PIE & ASLR:** Randomizes code, heap, and library base addresses on every execution.

---

## ⚠️ Legal Disclaimer

> [!CAUTION]
> pwndbg and GEF are binary analysis and exploit engineering tools intended strictly for authorized vulnerability research, defensive software auditing, and academic CTF competitions. Exploiting software without permission is illegal.
