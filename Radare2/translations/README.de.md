<!-- language-home-navigation -->
[🏠 Deutsche Startseite](../../README.de.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ⚡ Radare2 (r2) – Vollständiger Cheatsheet

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
  <b>⚡ Leistungsstarkes, terminalbasiertes Open-Source-Framework für Binäranalyse, Disassemblierung, Debugging, Hex-Bearbeitung und Binär-Patching</b>
</p>

---

## 📋 Inhaltsverzeichnis

- [Übersicht & Architektur](#-overview--architecture)
- [Installation](#-Installation)
- [Dateien öffnen und Sitzungsmodi](#-opening-files--session-modes)
- [Binäre Analysebefehle (`a`)](#-binary-analysis-commands-a)
- [Navigation & Suche (`s`)](#-navigation--seeking-s)
- [Drucken und Zerlegen (`p`)](#-printing--disassembly-p)
- [Interaktive visuelle & Grafikmodi (`V` & `VV`)](#-interactive-visual--graph-modes-v--vv)
- [Binäres Patchen und Modifizieren (`w`)](#-binary-patching--modification-w)
- [Das Radare2 Companion Toolset](#-the-radare2-companion-toolset)
  - [rabin2: Extraktion binärer Informationen](#rabin2-binary-information-extraction)
  - [rasm2: Assembler & Disassembler](#rasm2-assembler--disassembler)
  - [radiff2: Binary Difference Engine](#radiff2-binary-difference-engine)
  - [rafind2: Byte- und String-Suche](#rafind2-byte--string-search)
  - [rax2: Basis- und Zahlenkonverter](#rax2-base--numeric-converter)
- [Grundlagen des Debuggens mit r2 (`-d`)](#-debugging-basics-with-r2--d)
- [Kurzreferenztabelle](#-quick-reference-table)
- [Verteidigungsüberlegungen und Anti-Reversing](#-defensive-considerations--anti-reversing)
- [Rechtlicher Haftungsausschluss](#-legal-disclaimer)

---

## 🎯 Übersicht & Architektur

**Radare2** (`r2`) wurde von **Sergi Álvarez (Pancake)** entwickelt und ist ein tragbares, modulares Reverse-Engineering-Framework, das vollständig auf der UNIX-Philosophie basiert. Es läuft vollständig in Ihrem Terminal, ohne dass große grafische Abhängigkeiten erforderlich sind:
- **Vollständige Architekturunterstützung:** x86/x64, ARM, ARM64, MIPS, PowerPC, AVR, SPARC, WebAssembly, Java, Dalvik und EVM.
- **Zusammensetzbare CLI-Dienstprogramme:** Wird mit speziellen Befehlszeilentools (`rabin2`, `rasm2`, `radiff2`, `rafind2`, `rax2`) geliefert.
- **Interaktive visuelle Modi:** Hex-Editoren mit geteiltem Bildschirm, ASCII-Kontrollflussdiagramme, Registermonitore und Decompiler-Ansichten.
- **Skriptfähig und erweiterbar:** Integriert sich in Python, r2pipe, Ghidra (`r2ghidra`) und Rizin.

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

## 🚀 Öffnen von Dateien und Sitzungsmodi
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

## 🔬 Binäre Analysebefehle (`a`)

In der `r2`-Shell bestehen Befehle aus einzelnen Buchstaben oder kurzen Präfixen. Sie können `?` an jeden Befehl anhängen, um die Inline-Hilfe anzuzeigen:
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

## 🧭 Navigation & Suche (`s`)

Der Suchbefehl ändert die aktuelle Cursoradresse:
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

## 📜 Drucken und Zerlegen (`p`)
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

## 🖥️ Interaktive visuelle und grafische Modi (`V` und `VV`)

Durch Drücken von `V` oder `VV` wird Radare2 zu einer umfangreichen interaktiven Terminalanwendung:

- **Visuellen Modus aufrufen:** Geben Sie `V` ein
  - Drücken Sie `p` / `P`, um die Ansichten zu wechseln (Hex-Dump -> Disassembly -> Debugger -> Decompiler).
  - Drücken Sie `c`, um den Cursormodus umzuschalten (verwenden Sie die Pfeiltasten oder `hjkl`, um Bytes auszuwählen).
  - Drücken Sie `d`, um Bytes als Code, Daten oder Zeichenfolgen neu zu definieren.
  - Drücken Sie `q`, um zur Standardaufforderung zurückzukehren.
- **Graphansicht aufrufen:** Geben Sie `VV` ein
  - Rendert ASCII-Kontrollflussdiagramm (CFG).
  - Verwenden Sie `Tab` / `Shift+Tab`, um zwischen bedingten Verzweigungsknoten zu springen.
  - Drücken Sie `t`, um dem wahren Zweig zu folgen. `f`, um dem falschen Zweig zu folgen.
  - Drücken Sie `u`, um einen Zweig wieder zu verlassen.

---

## ✍️ Binäres Patchen und Modifizieren (`w`)

Um eine Binärdatei auf der Festplatte zu ändern, stellen Sie sicher, dass r2 im Schreibmodus geöffnet ist (`r2 -w target` oder geben Sie `oo+` in einer aktiven Sitzung ein):
```text
[0x00401150]> oo+                           # Re-open current binary with write permissions
[0x00401150]> wa nop                        # Assemble 'nop' instruction at current address
[0x00401150]> wa jmp 0x401190               # Patch a jump instruction
[0x00401150]> wx 90909090                   # Write hex bytes (4 NOPs)
[0x00401150]> w "Modified string\x00"       # Overwrite string with null-terminator
[0x00401150]> w0 16                         # Zero-fill 16 bytes (NUL padding)
```
---

## 🛠️ Das Radare2 Companion Toolset

Radare2 enthält eigenständige CLI-Dienstprogramme:

### rabin2: Extraktion binärer Informationen
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
### rasm2: Assembler und Disassembler
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
### radiff2: Binäre Differenz-Engine
```bash
# Compare two binaries and count differing bytes
radiff2 -c original.bin patched.bin

# Detailed code comparison graph
radiff2 -g main original.bin patched.bin | xdot -
```
### rafind2: Byte- und String-Suche
```bash
# Search for ASCII string in file
rafind2 -s "admin_password" target.bin

# Search for hexadecimal pattern
rafind2 -x "4889e5" target.bin
```
### rax2: Basis- und Zahlenkonverter
```bash
# Convert hex to decimal
rax2 0x1337      # Output: 4919

# Convert decimal to hex
rax2 4919        # Output: 0x1337

# Convert string to hex
rax2 -S "admin"  # Output: 61646d696e
```
---

## 🐛 Debugging-Grundlagen mit r2 (`-d`)
```bash
# Start debugging binary with arguments
r2 -d target.bin arg1 arg2
```
In der Debugger-Shell:
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

## ⚡ Kurzreferenztabelle

| Ziel | Befehl |
|-----------|---------|
| **Tiefe Analyse** | `aaa` |
| **Funktionen auflisten** | `afl` |
| **Suche nach Funktion / Addr** | `s <addr>` / `s main` |
| **Zerlegungsfunktion** | `pdf` |
| **Dekompilierungsfunktion (r2ghidra)** | `pdc` |
| **Interaktive visuelle Demontage** | `V` |
| **ASCII-Flussdiagramm** | `VV` |
| **Montageanleitung** | `wa <instruction>` |
| **Hex-Bytes schreiben** | `wx <bytes>` |
| **Erneut öffnen mit Lese-/Schreibzugriff** | `oo+` |
| **Binärinformationen extrahieren** | `rabin2 -I <binary>` |
| **CLI zusammenbauen** | `rasm2 -a x86 -b 64 "<asm>"` |

---

## 🛡️ Defensive Überlegungen und Anti-Reversing

1. **Anti-Debugging-Erkennung:** Der Debugger von Radare2 basiert auf dem Standard-Linux `ptrace`. Binärdateien mit `ptrace(PTRACE_TRACEME)`-Selbstprüfungen können Anhänge blockieren, sofern sie nicht gepatcht werden.
2. **Verschleierung und Abflachung des Kontrollflusses:** Stark verschleierte Binärdateien (OLLVM) verwirren die grundlegende Blockwiederherstellung. Verwenden Sie `aaaa` oder symbolische Ausführungs-Engines (wie angr), um abgeflachte Funktionen zu vereinfachen.

---

## ⚠️ Rechtlicher Haftungsausschluss

> [!ACHTUNG]
> Radare2 ist ein Reverse-Engineering- und Analyse-Toolkit, das ausschließlich für autorisierte Sicherheitsbewertungen, Malware-Analysen, Schwachstellenforschung und den persönlichen Bildungsgebrauch gedacht ist. Das Reverse Engineering proprietärer Software ohne Genehmigung kann gegen Endbenutzer-Lizenzvereinbarungen und Urheberrechte verstoßen.