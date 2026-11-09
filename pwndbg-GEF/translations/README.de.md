<!-- language-home-navigation -->
[🏠 Deutsche Startseite](../../README.de.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🎯 pwndbg & GEF – Vollständiger Cheatsheet

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
  <b>🎯 Die führenden Open-Source-GDB-Terminalerweiterungen für binäres Reverse Engineering, Speicherbeschädigungsanalyse, Heap-Layout-Inspektion und CTF-Exploit-Entwicklung</b>
</p>

---

## 📋 Inhaltsverzeichnis

- [Übersicht und warum Vanilla GDB Verbesserungen benötigt](#-overview--why-vanilla-gdb-needs-enhancements)
- [Installation und Wechsel zwischen Frameworks](#-installation--switching-between-frameworks)
  - [pwndbg wird installiert](#installing-pwndbg)
  - [GEF wird installiert](#installing-gef)
  - [Verwaltung von `.gdbinit`](#managing-gdbinit)
- [Die Kontextanzeige-Engine](#-the-context-display-engine)
  - [Registerabschnitt](#registers-section)
  - [Disassembly & Jump Predictions](#disassembly--jump-predictions)
  - [Stapel & Teleskop](#stack--teleskop)
  - [Backtrace & Threads](#backtrace--threads)
- [Speicherinspektion und Navigation](#-memory-inspection--navigation)
  - [Teleskopzeiger (`telescope`)](#telescoping-pointers-telescope)
  - [Prozessspeicherzuordnungen (`vmmap`)](#process-memory-mappings-vmmap)
  - [Speicher durchsuchen (`search`)](#searching-memory-search)
- [Heap-Ausbeutung und -Inspektion](#-heap-exploitation--inspection)
  - [Anzeigen von Chunks und Arenen](#viewing-chunks--arenas)
  - [Inspizieren freier Bins (Tcache, Fastbins, Smallbins)](#inspecting-free-bins-tcache-fastbins-smallbins)
  - [Visualisierung des Heap-Layouts (`vis_heap_chunks`)](#visualizing-heap-layout-vis_heap_chunks)
- [Exploit Development Helpers](#-exploit-development-helpers)
  - [Checksec & Mitigation Audit](#checksec--mitigation-audit)
  - [Stack Canary Locator (`canary`)](#stack-canary-locator-canary)
  - [Zyklische Muster und Offset-Berechnung](#symmetric-patterns--offset-calculation)
  - [In-Memory-ROP-Gadget-Jagd](#in-memory-rop-gadget-hunting)
- [Kurzreferenztabelle](#-quick-reference-table)
- [Verteidigungsüberlegungen und binäre Schutzmaßnahmen](#-defensive-considerations--binary-protections)
- [Rechtlicher Haftungsausschluss](#-legal-disclaimer)

---

## 🎯 Übersicht und warum Vanilla GDB Verbesserungen benötigt

Standard GNU Debugger (`gdb`) ist ein industrietaugliches Tool, dessen Benutzererfahrung jedoch sofort einsatzbereit ist, ist optisch dürftig. Beim schrittweisen Durchlaufen von Anweisungen werden Registerzustände nicht automatisch aktualisiert, Mehrebenenzeiger dereferenziert oder Stapelansichten angezeigt.

**pwndbg** und **GEF** verwandeln GDB in eine Elite-Suite für Reverse Engineering und binäre Ausbeutung:
- **pwndbg:** Speziell für Low-Level-Software-Ausnutzung, Heap-Debugging (Glibc 2.27 - 2.39+ Allocator-Interna) und automatische Zeiger-Dereferenzierung entwickelt.
- **GEF (GDB Enhanced Features):** Ein einzelnes eigenständiges Python-Skript ohne externe Abhängigkeiten, das architekturunabhängige Funktionen (ARM, MIPS, PowerPC, x86) und dynamische Strukturanalyse bietet.

---

## 📥 Installation und Wechsel zwischen Frameworks

### pwndbg installieren
```bash
git clone https://github.com/pwndbg/pwndbg
cd pwndbg
./setup.sh
```
### GEF installieren
```bash
# Automated one-liner download
bash -c "$(curl -fsSL https://gef.blah.cat/sh)"
```
### Verwalten von `.gdbinit`
Um konfliktfrei zwischen pwndbg und GEF zu wechseln, konfigurieren Sie Ihren `~/.gdbinit`:
```bash
# To use pwndbg:
echo "source /path/to/pwndbg/gdbinit.py" > ~/.gdbinit

# To use GEF:
echo "source ~/.gdbinit-gef.py" > ~/.gdbinit
```
---

## 🖥️ Die Context Display Engine

Jedes Mal, wenn die Ausführung an einem Haltepunkt oder Schritt anhält, drucken sowohl pwndbg als auch GEF eine automatisierte Terminalansicht mit mehreren Panels:
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

## 🔍 Speicherinspektion und Navigation

### Teleskopzeiger (`telescope`)
Dereferenziert Speicherzeiger rekursiv und zeigt ASCII-Text, Funktionssymbole oder Zielzeiger an:
```text
pwndbg> telescope $rsp 16       # Dereference 16 entries starting at RSP
pwndbg> telescope 0x404000 8    # Dereference data section
```
### Prozessspeicherzuordnungen (`vmmap`)
Zeigt komplette virtuelle Speichersegmente mit Lese-/Schreib-/Ausführungsberechtigungen an:
```text
pwndbg> vmmap
```
*Segmente mit `rwx`-Berechtigungen schnell erkennen (wichtig für die Shellcode-Injektion).*

### Speicher durchsuchen (`search`)
Suchen Sie nach Zeichenfolgen, Ganzzahlen oder Zeigern an einer beliebigen Stelle auf geladenen Speicherseiten:
```text
pwndbg> search -s "/bin/sh"     # Search for string "/bin/sh"
pwndbg> search -p 0x401135      # Search for pointers to main()
pwndbg> search -x "41414141"    # Search for raw hexadecimal bytes
```
---

## 🧱 Heap-Ausbeutung und -Inspektion

Die Analyse moderner `glibc`-Heap-Allokatoren ist mit dedizierten Heap-Befehlen unkompliziert:

### Anzeigen von Chunks und Arenen
```text
pwndbg> heap                    # Display entire heap layout
pwndbg> chunk 0x602000          # Parse chunk headers (size, flags: PREV_INUSE, IS_MMAPPED, NON_MAIN_ARENA)
pwndbg> arenas                  # List all active heap arenas
```
### Überprüfen freier Bins (Tcache, Fastbins, Smallbins)
```text
pwndbg> bins                    # Show all free bins simultaneously
pwndbg> tcache                  # Display per-thread tcache entries
pwndbg> fastbins                # List single-linked fastbins
pwndbg> unsortedbin             # List unsorted bin chunks (used for libc leak)
```
### Heap-Layout visualisieren (`vis_heap_chunks`)
```text
pwndbg> vis_heap_chunks 10      # Draw color-coded visual representation of consecutive heap chunks
```
---

## 🛠️ Nutzen Sie Entwicklungshelfer

### Checksec & Mitigation Audit
Überprüfen Sie die Sicherheitsmaßnahmen des Compilers direkt an der GDB-Eingabeaufforderung:
```text
pwndbg> checksec
```
*Inspiziert:*
- **RELRO:** Teilweise vs. vollständige RELRO (schützt die globale Offset-Tabelle `.got.plt`)
- **Stapel:** Kanarienvogel gefunden vs. Kein Kanarienvogel gefunden
- **NX:** NX aktiviert (nicht ausführbarer Stack/Heap)
- **PIE:** PIE aktiviert (Position Independent Executable / ASLR)

### Stack Canary Locator (`canary`)
Suchen Sie den Thread-Canary-Wert im Thread Local Storage (TLS) und überprüfen Sie, wo er sich im aktuellen Stack-Frame befindet:
```text
pwndbg> canary
```
### Zyklische Muster und Offset-Berechnung
Bestimmen Sie den genauen Offset zum Überschreiben des Befehlszeigers (EIP/RIP):
```text
# Generate a 100-byte De Bruijn cyclic sequence
pwndbg> cyclic 100

# After the crash (SIGSEGV), calculate offset from faulty address or register value
pwndbg> cyclic -l $rip
# Output: Finding cyclic pattern at offset 72
```
### In-Memory-ROP-Gadget-Jagd
Durchsuchen Sie geladene ausführbare Segmente nach bestimmten Gadgets:
```text
pwndbg> rop --grep "pop rdi"
```
---

## ⚡ Kurzreferenztabelle

| Ziel | Befehl |
|-----------|---------|
| **Beginnen Sie mit pwndbg/GEF**| `gdb ./target_binary` |
| **Register/Stack prüfen** | `context` |
| **Rekursiver Zeiger-Deref**| `telescope $rsp 10` |
| **Speicherbelegung und RWX-Prüfung**| `vmmap` |
| **Suchzeichenfolge im Speicher**| `search -s "/bin/sh"` |
| **Audit-Abhilfemaßnahmen** | `checksec` |
| **Stack Canary finden** | `canary` |
| **Übersicht über Heap-Chunks** | `heap` |
| **Freie Behälter prüfen** | `bins` |
| **Pufferoffset berechnen**| `cyclic 100` -> `cyclic -l <val>` |
| **Haltepunkt auf Symbol setzen**| `b *main` |
| **Ausführen / Fortfahren / Schritt** | `r` / `c` / `si` / `ni` |

---

## 🛡️ Verteidigungsüberlegungen und binärer Schutz

1. **Vollständiges RELRO:** Das Kompilieren mit `-Wl,-z,relro,-z,now` macht `.got` vollständig schreibgeschützt und verhindert so GOT-Überschreibangriffe.
2. **Stack Canaries:** Beim Kompilieren mit `-fstack-protector-all` werden Cookies vor Rücksprungadressen platziert, was zu `__stack_chk_fail` bei Pufferüberläufen führt.
3. **PIE & ASLR:** Randomisiert Code-, Heap- und Bibliotheksbasisadressen bei jeder Ausführung.

---

## ⚠️ Rechtlicher Haftungsausschluss

> [!ACHTUNG]
> pwndbg und GEF sind binäre Analyse- und Exploit-Engineering-Tools, die ausschließlich für autorisierte Schwachstellenforschung, defensive Softwareprüfungen und akademische CTF-Wettbewerbe gedacht sind. Die Nutzung von Software ohne Erlaubnis ist illegal.