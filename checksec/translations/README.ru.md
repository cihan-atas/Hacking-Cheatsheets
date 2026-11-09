# 🛡️ checksec — полная шпаргалка

<!-- language-home-navigation -->
[🏠 Русская главная страница](../../README.ru.md) • [🌐 Languages](../../README.md#-available-languages)

[🇬🇧 English](../README.md) • [🇷🇺 Русский раздел](../../README.ru.md)



```
   ██████╗██╗  ██╗███████╗ ██████╗██╗  ██╗███████╗███████╗ ██████╗
  ██╔════╝██║  ██║██╔════╝██╔════╝██║ ██╔╝██╔════╝██╔════╝██╔════╝
  ██║     ███████║█████╗  ██║     █████╔╝ ███████╗█████╗  ██║     
  ██║     ██╔══██║██╔══╝  ██║     ██╔═██╗ ╚════██║██╔══╝  ██║     
  ╚██████╗██║  ██║███████╗╚██████╗██║  ██╗███████║███████╗╚██████╗
   ╚═════╝╚═╝  ╚═╝╚══════╝ ╚═════╝╚═╝  ╚═╝╚══════╝╚══════╝ ╚═════╝
          Linux ELF Binary Mitigation Auditor & Inspection Toolkit
```

<p align="center">
  <img src="https://img.shields.io/badge/checksec-Security_Mitigations-blue?style=for-the-badge" alt="checksec">
  <img src="https://img.shields.io/badge/readelf-ELF_Headers-orange?style=for-the-badge" alt="readelf">
  <img src="https://img.shields.io/badge/objdump-Disassembly-green?style=for-the-badge" alt="objdump">
  <img src="https://img.shields.io/badge/Platform-Linux_ELF-purple?style=for-the-badge" alt="Linux ELF">
</p>

<p align="center">
  <b>🛡️ Essential command-line tools for auditing Linux executable security mitigations (RELRO, Stack Canary, NX, PIE, Fortify) and dissecting ELF binary headers, sections, and symbol tables</b>
</p>

---

## 📋 Содержание

- [Введение](#-overview--binary-mitigations-matrix)
- [Установка](#-installation)
- [checksec: Rapid Mitigation Auditing](#-checksec-rapid-mitigation-auditing)
  - [Auditing Single & Batch Binaries](#auditing-single--batch-binaries)
  - [Auditing Running Processes](#auditing-running-processes)
  - [JSON & CSV Output](#json--csv-output)
- [readelf: ELF Header & Section Dissection](#-readelf-elf-header--section-dissection)
  - [ELF File Header (`-h`)](#elf-file-header--h)
  - [The `readelf` utility displays information from ELF files without invoking any execution:](#program-headers--segments--l)
  - [Section Headers (`-S`)](#section-headers--s)
  - [(If `GNU_STACK` has `RWE`, stack execution is permitted; NX is disabled).](#symbol-tables--s----syms)
  - [Dynamic Dependencies & Libraries (`-d`)](#dynamic-dependencies--libraries--d)
- [objdump: Disassembly & Relocations](#-objdump-disassembly--relocations)
  - [Intel Disassembly (`-d -M intel`)](#intel-disassembly--d--m-intel)
  - [🧩 objdump: Disassembly & Relocations](#relocation-entries--got--r)
- [nm & strings: Symbol Resolution & Strings](#-nm--strings-symbol-resolution--strings)
  - [Shows address targets for functions like `puts`, `printf`, and `system`.](#listing-demangled-symbols-with-nm)
  - [Extracting Obfuscated Strings with `strings`](#extracting-obfuscated-strings-with-strings)
- [Ресурсы и ссылки](#-hardened-compilation-reference)
- [Краткий справочник](#-quick-reference-table)
- [Legal Disclaimer](#-legal-disclaimer)

---

## 🎯 Введение

[Legal Disclaimer](#-legal-disclaimer)

|Mitigation|Full Name|Protection Mechanism|Bypass Strategy|
|------------|-----------|----------------------|-----------------|
|**RELRO**|Relocation Read-Only|Makes `.got` read-only (`Full RELRO`) to prevent pointer overwriting|Target heap structures, return addresses, or function hooks|
|**Canary**|Stack Protector|Places randomized cookie before return pointer; checks integrity upon return|Leak canary value via memory leak, or brute-force (forking daemons)|
|**NX**|No-Execute / DEP|Marks stack, heap, and data segments as non-executable|Return-Oriented Programming (ROP) or Jump-Oriented Programming (JOP)|
|**PIE**|Position Independent Executable|Loads binary base address at random location in virtual memory (ASLR)|Leak code pointer to calculate base address offset|
|**Fortify**|`_FORTIFY_SOURCE`|Replaces unsafe string functions (`strcpy` -> `__strcpy_chk`) with bounds checking|Non-overflow logic bugs, format string without buffer overflow|

---

## 📥 Установка

```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y checksec binutils

# Arch Linux
sudo pacman -S checksec binutils

# Python pip (standalone checksec)
pip install checksec.py
```

---

## ⚡ checksec: Rapid Mitigation Auditing

### Auditing Single & Batch Binaries
```bash
# Audit a single executable
checksec --file=./target_binary

# Audit all binaries in directory
checksec --dir=/usr/bin/
```
*Standard Output Example:*
```text
RELRO           STACK CANARY      NX            PIE             RPATH      RUNPATH      FILE
Full RELRO      Canary found      NX enabled    PIE enabled     No RPATH   No RUNPATH   ./target_binary
```

### Auditing Running Processes
Auditing Single & Batch Binaries
```bash
# Audit by Process ID (PID)
checksec --proc=1337

# Audit process by name
checksec --proc-all
```

### JSON & CSV Output
Export findings for automated analysis:
```bash
checksec --file=./target_binary --output=json > security.json
checksec --file=./target_binary --output=csv > security.csv
```

---

## 🔬 readelf: ELF Header & Section Dissection

The `readelf` utility displays information from ELF files without invoking any execution:

### ELF File Header (`-h`)
Reveals CPU architecture, 32 vs 64-bit class, entry point address, and endianness:
```bash
readelf -h ./target_binary
```

### The `readelf` utility displays information from ELF files without invoking any execution:
Displays segments loaded into memory at runtime and flags (`R`, `W`, `E`). Crucial for identifying whether the stack (`GNU_STACK`) has execution permissions:
```bash
readelf -l ./target_binary
```
*(If `GNU_STACK` has `RWE`, stack execution is permitted; NX is disabled).*

### Section Headers (`-S`)
Inspect memory layouts, addresses, and offsets for `.text` (code), `.data` (initialized data), `.rodata` (constants), `.bss` (uninitialized data), `.plt`, and `.got`:
```bash
readelf -S ./target_binary
```

### (If `GNU_STACK` has `RWE`, stack execution is permitted; NX is disabled).
List all exported functions, global variables, and imported libc symbols:
```bash
# List all symbols
readelf -s ./target_binary

# Filter dynamic symbols used at runtime
readelf --dyn-syms ./target_binary
```

### Dynamic Dependencies & Libraries (`-d`)
Lists required shared libraries (`NEEDED` tags, e.g. `libc.so.6`, `libssl.so`):
```bash
readelf -d ./target_binary
```

---

## 🧩 objdump: Disassembly & Relocations

### Intel Disassembly (`-d -M intel`)
Disassemble executable sections into clean Intel-syntax assembly:
```bash
# Disassemble .text section in Intel syntax
objdump -d -M intel ./target_binary

# Disassemble specific function (e.g. main)
objdump -d -M intel ./target_binary | grep -A 30 "<main>:"
```

### 🧩 objdump: Disassembly & Relocations
Display the Global Offset Table (GOT) relocation addresses for imported library calls:
```bash
objdump -R ./target_binary
```
*Shows address targets for functions like `puts`, `printf`, and `system`.*

---

## 🔍 nm & strings: Symbol Resolution & Strings

### Shows address targets for functions like `puts`, `printf`, and `system`.
```bash
# List defined symbols only (ignore external imports)
nm --defined-only -C ./target_binary

# List undefined external symbols (functions resolved by dynamic linker)
nm -u -C ./target_binary
```

### Extracting Obfuscated Strings with `strings`
```bash
# Extract ASCII strings with minimum 8 characters
strings -a -n 8 ./target_binary

# Extract 16-bit Little Endian wide strings (UTF-16 e.g., Windows / Java)
strings -e l -a ./target_binary

# Print byte offset of each string in hex
strings -a -t x ./target_binary
```

---

## 🛡️ Ресурсы и ссылки

To build an ELF binary with maximum security mitigations:

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

## ⚡ Краткий справочник

|Objective|Команда|
|-----------|---------|
|**Audit Mitigations**|`checksec --file=<bin>`|
|**Audit Running PID**|`checksec --proc=<pid>`|
|**View ELF Header**|`readelf -h <bin>`|
|**View Stack NX Flag**|`readelf -l <bin> \|grep -A 1 GNU_STACK`|
|**List Sections (.got, .plt)**|`readelf -S <bin>`|
|**View Dynamic Symbols**|`readelf --dyn-syms <bin>`|
|**Intel Disassembly**|`objdump -d -M intel <bin>`|
|**Inspect GOT Relocations**|`objdump -R <bin>`|
|**List Symbols (Demangled)**|`nm -C <bin>`|
|**Extract UTF-16 Strings**|`strings -e l -a <bin>`|
|**Show String Offsets**|`strings -a -t x <bin>`|

---

## ⚠️ Legal Disclaimer

3. **Honeytokens:** Plant attractive DNS records (e.g., `backup-vault.corp.local` or `admin-portal.corp.local`) pointing to honeypot IP addresses.
> checksec and GNU Binutils are diagnostic and reverse engineering tools intended for software security auditing, defense validation, and authorized binary research. Inspecting unauthorized software must adhere to local intellectual property regulations.
