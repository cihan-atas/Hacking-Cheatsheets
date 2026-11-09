<!-- language-home-navigation -->
[🏠 Norsk Hovedside](../../README.nb.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇬🇧 English](../README.md)

---


# ⚡ Radare2 (r2) - Komplett jukseark

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
  <img src="https://img.shields.io/badge/Author-Sergi_Álvarez-orange?style=for-the-badge" alt="pannekake">
  <img src="https://img.shields.io/badge/Category-Reverse_Engineering-green?style=for-the-badge" alt="Reverse Engineering">
  <img src="https://img.shields.io/badge/License-LGPLv3-purple?style=for-the-badge" alt="LGPLv3">
</p>

<p align="center">
  <b>⚡ Kraftig, åpen kildekode, terminalbasert rammeverk for binær analyse, demontering, feilsøking, hex-redigering og binær patching</b>
</p>

---

## 📋 Innholdsfortegnelse

- [Oversikt og arkitektur](#-oversikt--arkitektur)
- [Installasjon](#-installasjon)
- [Åpningsfiler og øktmoduser](#-åpningsfiler--øktmoduser)
- [Binære analysekommandoer (`a`)](#-binære-analyse-kommandoer-a)
- [Navigasjon og søk (`s`)](#-navigasjon--søker-s)
- [Utskrift og demontering (`p`)](#-utskrift--demontering-p)
- [Interactive Visual & Graph Modes (`V` & `VV`)](#-interactive-visual--graph-modes-v--vv)
- [Binær oppdatering og modifikasjon (`w`)](#-binær-patching--modifikasjon-w)
- [The Radare2 Companion Toolset](#-the-radare2-companion-toolset)
  - [rabin2: utvinning av binær informasjon](#rabin2-binær-informasjonsutvinning)
  - [rasm2: Assembler & Disassembler](#rasm2-assembler--disassembler)
  - [radiff2: binær forskjellsmotor](#radiff2-binær forskjellsmotor)
  - [rafind2: Byte & String Search](#rafind2-byte--string-search)
  - [rax2: Base & Numeric Converter](#rax2-base--numeric-converter)
- [Grunnleggende feilsøking med r2 (`-d`)](#-debugging-basics-with-r2--d)
- [Hurtigreferansetabell](#-hurtigreferansetabell)
- [Defensive Betraktninger og anti-reversering](#-defensive-betraktninger--anti-reversering)
– [Juridisk ansvarsfraskrivelse](#-juridisk ansvarsfraskrivelse)

---

## 🎯 Oversikt og arkitektur

Laget av **Sergi Álvarez (pannekake)**, **Radare2** (`r2`) er et bærbart, modulært omvendt konstruksjonsrammeverk bygget helt rundt UNIX-filosofien. Den kjører helt inne i terminalen din uten å kreve store grafiske avhengigheter:
- **Full arkitekturstøtte:** x86/x64, ARM, ARM64, MIPS, PowerPC, AVR, SPARC, WebAssembly, Java, Dalvik og EVM.
- **Composable CLI Utilities:** Leveres med spesialiserte kommandolinjeverktøy (`rabin2`, `rasm2`, `radiff2`, `rafind2`, `rax2`).
- **Interaktive visuelle moduser:** Hexredigering med delt skjerm, ASCII-kontrollflytgrafer, registermonitorer og dekompileringsvisninger.
- **Skriptbar og utvidbar:** Integrerer med Python, r2pipe, Ghidra (`r2ghidra`) og Rizin.

---

## 📥 Installasjon
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

## 🚀 Åpning av filer og øktmoduser
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

## 🔬 Binære analysekommandoer (`a`)

Inne i `r2`-skallet er kommandoer enkeltbokstaver eller korte prefikser. Du kan legge til `?` til en hvilken som helst kommando for å se innebygd hjelp:
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

## 🧭 Navigasjon og søking (`s`)

Seek-kommandoen endrer gjeldende markøradresse:
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

## 📜 Utskrift og demontering (`p`)
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

## 🖥️ Interaktive visuelle og grafiske moduser (`V` & `VV`)

Ved å trykke `V` eller `VV` gjør Radare2 om til en rik interaktiv terminalapplikasjon:

- **Gå inn i visuell modus:** Skriv `V`
  - Trykk `p` / `P` for å bla gjennom visninger (Hex dump -> Demontering -> Debugger -> Dekompiler).
  - Trykk `c` for å bytte markørmodus (bruk piltastene eller `hjkl` for å velge byte).
  - Trykk `d` for å omdefinere byte som kode, data eller strenger.
  - Trykk `q` for å gå tilbake til standard ledetekst.
- **Enter Graph View:** Type `VV`
  - Gjengir ASCII-kontrollflytgraf (CFG).
  - Bruk `Tab` / `Shift+Tab` for å hoppe mellom betingede grennoder.
  - Trykk `t` for å følge den sanne grenen; `f` for å følge den falske grenen.
  - Trykk på `u` for å gå ut av en gren.

---

## ✍️ Binær patching og modifikasjon (`w`)

For å endre en binær på disk, sørg for at r2 er åpnet i skrivemodus (`r2 -w target` eller skriv `oo+` i en aktiv økt):
```text
[0x00401150]> oo+                           # Re-open current binary with write permissions
[0x00401150]> wa nop                        # Assemble 'nop' instruction at current address
[0x00401150]> wa jmp 0x401190               # Patch a jump instruction
[0x00401150]> wx 90909090                   # Write hex bytes (4 NOPs)
[0x00401150]> w "Modified string\x00"       # Overwrite string with null-terminator
[0x00401150]> w0 16                         # Zero-fill 16 bytes (NUL padding)
```
---

## 🛠️ Radare2 Companion Toolset

Radare2 inkluderer frittstående CLI-verktøy:

### rabin2: utvinning av binær informasjon
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
### rasm2: Monter og demonter
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
### radiff2: Binær forskjellsmotor
```bash
# Compare two binaries and count differing bytes
radiff2 -c original.bin patched.bin

# Detailed code comparison graph
radiff2 -g main original.bin patched.bin | xdot -
```
### rafind2: Byte- og strengsøk
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

## 🐛 Grunnleggende feilsøking med r2 (`-d`)
```bash
# Start debugging binary with arguments
r2 -d target.bin arg1 arg2
```
Inne i debugger-skallet:
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

## ⚡ Hurtigreferansetabell

| Mål | Kommando |
|----------------|--------|
| **Dyp analyse** | `aaa` |
| **Listefunksjoner** | `afl` |
| **Seek to Function / Addr** | `s <addr>` / `s main` |
| **Demonteringsfunksjon** | `pdf` |
| **Dekompilere funksjon (r2ghida)** | `pdc` |
| **Interaktiv visuell demontering** | `V` |
| **ASCII-flytgraf** | `VV` |
| **Monteringsveiledning** | `wa <instruction>` |
| **Skriv Hex Bytes** | `wx <bytes>` |
| **Åpne på nytt i Les-Skriv** | `oo+` |
| ** Trekk ut binær informasjon** | `rabin2 -I <binary>` |
| **Sett sammen CLI** | `rasm2 -a x86 -b 64 "<asm>"` |

---

## 🛡️ Defensive betraktninger og anti-reversering

1. **Anti-feilsøkingsdeteksjon:** Radare2s debugger er avhengig av standard Linux `ptrace`. Binærfiler med `ptrace(PTRACE_TRACEME)`-selvsjekker kan blokkere vedlegg med mindre de er lappet.
2. **Obfuscation & Control Flow Flattening:** Tungt obfuscated binærfiler (OLLVM) forvirrer grunnleggende blokkgjenoppretting. Bruk `aaaa` eller symbolske utførelsesmotorer (som angr) for å forenkle flate funksjoner.

---

## ⚠️ Juridisk ansvarsfraskrivelse

> [!FORSIKTIG]
> Radare2 er et omvendt utviklings- og analyseverktøy som er ment for autoriserte sikkerhetsvurderinger, malware-analyse, sårbarhetsforskning og personlig pedagogisk bruk. Omvendt utvikling av proprietær programvare uten tillatelse kan bryte sluttbrukerlisensavtaler og lover om opphavsrett.