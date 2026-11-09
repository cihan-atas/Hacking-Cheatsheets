<!-- language-home-navigation -->
[🏠 Home Page Italiana](../../README.it.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ⚡ Radare2 (r2) - Cheatsheet completo

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
<palign="centro">
  <img src="https://img.shields.io/badge/Radare2-CLI_Reversing-blue?style=for-the-badge" alt="Radare2">
  <img src="https://img.shields.io/badge/Author-Sergi_Álvarez-orange?style=for-the-badge" alt="pancake">
  <img src="https://img.shields.io/badge/Category-Reverse_Engineering-green?style=for-the-badge" alt="Reverse Engineering">
  <img src="https://img.shields.io/badge/License-LGPLv3-purple?style=for-the-badge" alt="LGPLv3">
</p>

<palign="centro">
  <b>⚡ Framework potente, open source, basato su terminale per analisi binaria, disassemblaggio, debug, modifica esadecimale e patching binario</b>
</p>

---

## 📋 Sommario

- [Panoramica e architettura](#-panoramica--architettura)
- [Installazione](#-installazione)
- [Apertura di file e modalità di sessione](#-opening-files--session-modes)
- [Comandi di analisi binaria (`a`)](#-comandi-di-analisi-binaria-a)
- [Navigazione e ricerca (`s`)](#-navigazione--cerca-s)
- [Stampa e smontaggio (`p`)](#-stampa--disassemblaggio-p)
- [Modalità grafica e grafica interattiva (`V` e `VV`)](#-visual-interattiva--modalità-grafico-v--vv)
- [Patch e modifiche binarie (`w`)](#-patch-binario--modifica-w)
- [Il set di strumenti del compagno Radare2](#-the-radare2-companion-toolset)
  - [rabin2: Estrazione di informazioni binarie](#rabin2-estrazione-di-informazioni-binarie)
  - [rasm2: assemblatore e disassemblatore](#rasm2-assemblatore--disassemblatore)
  - [radiff2: motore di differenza binaria](#radiff2-motore-differenza-binaria)
  - [rafind2: ricerca di byte e stringhe](#rafind2-byte--ricerca di stringhe)
  - [rax2: convertitore base e numerico](#rax2-base--convertitore numerico)
- [Nozioni di base sul debug con r2 (`-d`)](#-nozioni di base sul debug con-r2--d)
- [Tabella di riferimento rapido](#-tabella-di-riferimento-rapido)
- [Considerazioni difensive e anti-reversione](#-considerazioni-difensive--anti-reversione)
- [Disclaimer legale](#-legal-disclaimer)

---

## 🎯 Panoramica e architettura

Creato da **Sergi Álvarez (pancake)**, **Radare2** (`r2`) è un framework di reverse engineering portatile e modulare costruito interamente attorno alla filosofia UNIX. Funziona interamente all'interno del tuo terminale senza richiedere pesanti dipendenze grafiche:
- **Supporto completo dell'architettura:** x86/x64, ARM, ARM64, MIPS, PowerPC, AVR, SPARC, WebAssembly, Java, Dalvik ed EVM.
- **Utilità CLI componibili:** Viene fornito in bundle con strumenti da riga di comando specializzati (`rabin2`, `rasm2`, `radiff2`, `rafind2`, `rax2`).
- **Modalità visive interattive:** Editor esadecimali a schermo diviso, grafici del flusso di controllo ASCII, monitor di registro e visualizzazioni del decompilatore.
- **Scriptabile ed estensibile:** si integra con Python, r2pipe, Ghidra (`r2ghidra`) e Rizin.

---

## 📥Installazione
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

## 🚀 Apertura di file e modalità di sessione
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

## 🔬 Comandi di analisi binaria (`a`)

All'interno della shell `r2`, i comandi sono singole lettere o prefissi brevi. Puoi aggiungere `?` a qualsiasi comando per visualizzare la guida in linea:
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

## 🧭 Navigazione e ricerca (`s`)

Il comando di ricerca modifica l'indirizzo corrente del cursore:
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

## 📜 Stampa e smontaggio (`p`)
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

## 🖥️ Modalità grafica e visiva interattiva (`V` e `VV`)

Premendo `V` o `VV` si trasforma Radare2 in una ricca applicazione terminale interattiva:

- **Accedi alla modalità visiva:** digita `V`
  - Premi `p` / `P` per scorrere le visualizzazioni (Dump esadecimale -> Disassemblaggio -> Debugger -> Decompiler).
  - Premere `c` per attivare/disattivare la modalità cursore (utilizzare i tasti freccia o `hjkl` per selezionare i byte).
  - Premere `d` per ridefinire i byte come codice, dati o stringhe.
  - Premere `q` per tornare al prompt standard.
- **Accedi alla visualizzazione grafico:** digita `VV`
  - Rende il grafico del flusso di controllo ASCII (CFG).
  - Utilizzare `Tab` / `Shift+Tab` per saltare tra i nodi del ramo condizionale.
  - Premere `t` per seguire il ramo vero; `f` per seguire il falso ramo.
  - Premi `u` per uscire da un ramo.

---

## ✍️ Patching e modifica binaria (`w`)

Per modificare un file binario su disco, assicurati che r2 sia aperto in modalità scrittura (`r2 -w target` o digita `oo+` all'interno di una sessione attiva):
```text
[0x00401150]> oo+                           # Re-open current binary with write permissions
[0x00401150]> wa nop                        # Assemble 'nop' instruction at current address
[0x00401150]> wa jmp 0x401190               # Patch a jump instruction
[0x00401150]> wx 90909090                   # Write hex bytes (4 NOPs)
[0x00401150]> w "Modified string\x00"       # Overwrite string with null-terminator
[0x00401150]> w0 16                         # Zero-fill 16 bytes (NUL padding)
```
---

## 🛠️ Il set di strumenti complementare Radare2

Radare2 include utilità CLI autonome:

### rabin2: Estrazione binaria di informazioni
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
### rasm2: assemblatore e disassemblatore
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
### radiff2: motore delle differenze binarie
```bash
# Compare two binaries and count differing bytes
radiff2 -c original.bin patched.bin

# Detailed code comparison graph
radiff2 -g main original.bin patched.bin | xdot -
```
### rafind2: ricerca di byte e stringhe
```bash
# Search for ASCII string in file
rafind2 -s "admin_password" target.bin

# Search for hexadecimal pattern
rafind2 -x "4889e5" target.bin
```
### rax2: Convertitore base e numerico
```bash
# Convert hex to decimal
rax2 0x1337      # Output: 4919

# Convert decimal to hex
rax2 4919        # Output: 0x1337

# Convert string to hex
rax2 -S "admin"  # Output: 61646d696e
```
---

## 🐛 Nozioni di base sul debug con r2 (`-d`)
```bash
# Start debugging binary with arguments
r2 -d target.bin arg1 arg2
```
All'interno della shell del debugger:
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

## ⚡ Tabella di consultazione rapida

| Obiettivo | Comando |
|-----------|---------|
| **Analisi approfondita** | `aaa` |
| **Funzioni elenco** | `afl` |
| **Cerca funzione/Indirizzo** | `s <addr>` / `s main` |
| **Funzione di smontaggio** | `pdf` |
| **Funzione decompila (r2ghidra)** | `pdc` |
| **Smontaggio visivo interattivo** | `V` |
| **Grafico di flusso ASCII** | `VV` |
| **Istruzioni di montaggio** | `wa <instruction>` |
| **Scrivi byte esadecimali** | `wx <bytes>` |
| **Riapri in lettura-scrittura** | `oo+` |
| **Estrai informazioni binarie** | `rabin2 -I <binary>` |
| **Assembla CLI** | `rasm2 -a x86 -b 64 "<asm>"` |

---

## 🛡️ Considerazioni difensive e anti-reversing

1. **Rilevamento anti-debugging:** il debugger di Radare2 si basa sullo standard Linux `ptrace`. I file binari con controlli automatici `ptrace(PTRACE_TRACEME)` possono bloccare gli allegati a meno che non vengano applicati patch.
2. **Offuscamento e appiattimento del flusso di controllo:** i file binari fortemente offuscati (OLLVM) confondono il ripristino dei blocchi di base. Utilizza `aaaa` o motori di esecuzione simbolica (come angr) per semplificare le funzioni appiattite.

---

## ⚠️ Disclaimer legale

> [!ATTENZIONE]
> Radare2 è un kit di strumenti di reverse engineering e analisi destinato esclusivamente a valutazioni di sicurezza autorizzate, analisi di malware, ricerca di vulnerabilità e uso educativo personale. Il reverse engineering del software proprietario senza autorizzazione può violare i contratti di licenza con l'utente finale e le leggi sul copyright.