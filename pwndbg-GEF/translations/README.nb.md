<!-- language-home-navigation -->
[🏠 Norsk Hovedside](../../README.nb.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇬🇧 English](../README.md)

---


# 🎯 pwndbg & GEF - Komplett jukseark

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
  <b>🎯 De fremste åpen kildekode-forbedringene for GDB-terminaler designet for binær omvendt utvikling, minnekorrupsjonsanalyse, heap-layoutinspeksjon og utvikling av CTF-utnyttelse</b>
</p>

---

## 📋 Innholdsfortegnelse

- [Oversikt og hvorfor Vanilla GDB trenger forbedringer](#-overview--why-vanilla-gdb-needs-enhancements)
- [Installasjon og veksling mellom rammer](#-installasjon--bytte-mellom-rammeverk)
  - [Installerer pwndbg](#installing-pwndbg)
  - [Installerer GEF](#installing-gef)
  - [Managing `.gdbinit`](#managing-gdbinit)
- [The Context Display Engine](#-the-context-display-engine)
  - [Registerseksjon](#registers-section)
  - [Demontering og hoppprediksjoner](#disassembly--jump-prediksjoner)
  - [Stabel og teleskop](#stabel--teleskop)
  - [Backtrace & Threads](#backtrace--threads)
– [Minneinspeksjon og -navigasjon](#-minneinspeksjon--navigasjon)
  - [Telescoping Pointers (`telescope`)](#telescoping-pointers-telescope)
  - [Process Memory Mappings (`vmmap`)](#process-memory-mappings-vmmap)
  - [Searching Memory (`search`)](#searching-memory-search)
– [haug-utnyttelse og inspeksjon](#-haug-utnyttelse--inspeksjon)
  - [Viewing Chunks & Arenas](#viewing-chunks--arenas)
  - [Inspeksjon av gratiskasser (Tcache, Fastbins, Smallbins)](#inspecting-free-bins-tcache-fastbins-smallbins)
  - [Visualizing Heap Layout (`vis_heap_chunks`)](#visualizing-heap-layout-vis_heap_chunks)
– [Exploit Development Helpers](#-exploit-development-helpers)
  - [Checksec & Mitigation Audit](#checksec--mitigation-audit)
  - [Stack Canary Locator (`canary`)](#stack-canary-locator-canary)
  - [Cyclic Patterns & Offset Calculation](#cyclic-patterns--offset-calculation)
  - [In-Memory ROP Gadget Hunting](#in-memory-rop-gadget-hunting)
- [Hurtigreferansetabell](#-hurtigreferansetabell)
- [Defensive betraktninger og binære beskyttelser](#-defensive-betraktninger--binære-beskyttelser)
– [Juridisk ansvarsfraskrivelse](#-juridisk ansvarsfraskrivelse)

---

## 🎯 Oversikt og hvorfor Vanilla GDB trenger forbedringer

Standard GNU Debugger (`gdb`) er et verktøy med industriell styrke, men dens ut-av-boksen brukeropplevelse er visuelt streng. Den oppdaterer ikke automatisk registertilstander, fjerner pekere på flere nivåer eller viser stabelvisninger når du går gjennom instruksjonene.

**pwndbg** og **GEF** gjør GDB til en eliteserie for omvendt konstruksjon og binær utnyttelse:
- **pwndbg:** Spesielt konstruert for programvareutnyttelse på lavt nivå, heap-feilsøking (glibc 2.27 - 2.39+ allokatorinternal) og automatisert pekereferering.
- **GEF (GDB Enhanced Features):** Et enkelt frittstående Python-skript med null eksterne avhengigheter, og tilbyr arkitekturagnostiske funksjoner (ARM, MIPS, PowerPC, x86) og dynamisk strukturanalyse.

---

## 📥 Installasjon og veksling mellom rammeverk

### Installerer pwndbg
```bash
git clone https://github.com/pwndbg/pwndbg
cd pwndbg
./setup.sh
```
### Installerer GEF
```bash
# Automated one-liner download
bash -c "$(curl -fsSL https://gef.blah.cat/sh)"
```
### Administrere `.gdbinit`
For å bytte mellom pwndbg og GEF uten konflikt, konfigurer din `~/.gdbinit`:
```bash
# To use pwndbg:
echo "source /path/to/pwndbg/gdbinit.py" > ~/.gdbinit

# To use GEF:
echo "source ~/.gdbinit-gef.py" > ~/.gdbinit
```
---

## 🖥️ Context Display Engine

Hver gang kjøringen stopper ved et bruddpunkt eller et trinn, skriver både pwndbg og GEF ut en automatisert terminalvisning med flere paneler:
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

## 🔍 Minneinspeksjon og navigering

### Teleskoppekere (`telescope`)
Frareferanser minnepekere rekursivt, viser ASCII-tekst, funksjonssymboler eller målpekere:
```text
pwndbg> telescope $rsp 16       # Dereference 16 entries starting at RSP
pwndbg> telescope 0x404000 8    # Dereference data section
```
### Prosess minnetilordninger (`vmmap`)
Viser komplette virtuelle minnesegmenter med lese-/skrive-/utføringstillatelser:
```text
pwndbg> vmmap
```
*Søk raskt segmenter med `rwx`-tillatelser (viktig for shellcode-injeksjon).*

### Søker etter minne (`search`)
Finn strenger, heltall eller pekere hvor som helst på innlastede minnesider:
```text
pwndbg> search -s "/bin/sh"     # Search for string "/bin/sh"
pwndbg> search -p 0x401135      # Search for pointers to main()
pwndbg> search -x "41414141"    # Search for raw hexadecimal bytes
```
---

## 🧱 Heap-utnyttelse og inspeksjon

Det er enkelt å analysere moderne `glibc` heap-tildelere med dedikerte heap-kommandoer:

### Visningsbiter og arenaer
```text
pwndbg> heap                    # Display entire heap layout
pwndbg> chunk 0x602000          # Parse chunk headers (size, flags: PREV_INUSE, IS_MMAPPED, NON_MAIN_ARENA)
pwndbg> arenas                  # List all active heap arenas
```
### Inspiserer gratis søppelkasser (Tcache, Fastbins, Smallbins)
```text
pwndbg> bins                    # Show all free bins simultaneously
pwndbg> tcache                  # Display per-thread tcache entries
pwndbg> fastbins                # List single-linked fastbins
pwndbg> unsortedbin             # List unsorted bin chunks (used for libc leak)
```
### Visualiserer heap-layout (`vis_heap_chunks`)
```text
pwndbg> vis_heap_chunks 10      # Draw color-coded visual representation of consecutive heap chunks
```
---

## 🛠️ Utnytt utviklingshjelpere

### Checksec & Mitigation Audit
Sjekk kompilatorsikkerhetsbegrensninger direkte fra GDB-ledeteksten:
```text
pwndbg> checksec
```
*Inspiserer:*
- **RELRO:** Delvis vs Full RELRO (beskytter Global Offset Table `.got.plt`)
- **Stabel:** Kanarifugl funnet vs ingen kanarifugl
- **NX:** NX aktivert (ikke-kjørbar stack/heap)
- **PIE:** PIE aktivert (posisjonsuavhengig kjørbar / ASLR)

### Stack Canary Locator (`canary`)
Finn trådkanarieverdien i Thread Local Storage (TLS) og kontroller hvor den ligger på gjeldende stabelramme:
```text
pwndbg> canary
```
### Sykliske mønstre og forskyvningsberegning
Bestem den nøyaktige forskyvningen for å overskrive instruksjonspekeren (EIP/RIP):
```text
# Generate a 100-byte De Bruijn cyclic sequence
pwndbg> cyclic 100

# After the crash (SIGSEGV), calculate offset from faulty address or register value
pwndbg> cyclic -l $rip
# Output: Finding cyclic pattern at offset 72
```
### In-Memory ROP Gadget Jakt
Søk i lastede kjørbare segmenter etter spesifikke gadgets:
```text
pwndbg> rop --grep "pop rdi"
```
---

## ⚡ Hurtigreferansetabell

| Mål | Kommando |
|----------------|--------|
| **Start med pwndbg/GEF**| `gdb ./target_binary` |
| **Inspiser registre/stabel** | `context` |
| **Rekursiv Pointer Deref**| `telescope $rsp 10` |
| **Minnekart og RWX-sjekk**| `vmmap` |
| **Søkestreng i minne**| `search -s "/bin/sh"` |
| **Revisjonsbegrensninger** | `checksec` |
| **Finn Stack Canary** | `canary` |
| **Haap Chunks Oversikt** | `heap` |
| **Inspiser gratis søppelkasser** | `bins` |
| **Beregn bufferforskyvning**| `cyclic 100` -> `cyclic -l <val>` |
| **Sett brytepunkt ved symbol**| `b *main` |
| **Kjør / Fortsett / Trinn** | `r` / `c` / `si` / `ni` |

---

## 🛡️ Defensive betraktninger og binær beskyttelse

1. **Full RELRO:** Kompilering med `-Wl,-z,relro,-z,now` gjør `.got` fullstendig skrivebeskyttet, og beseirer GOT-overskriveangrep.
2. **Stack Canaries:** Kompilering med `-fstack-protector-all` plasserer informasjonskapsler før returadresser, noe som forårsaker `__stack_chk_fail` på bufferoverflyt.
3. **PIE & ASLR:** Randomiserer kode-, heap- og bibliotekbaseadresser ved hver kjøring.

---

## ⚠️ Juridisk ansvarsfraskrivelse

> [!FORSIKTIG]
> pwndbg og GEF er binære analyse- og utnyttelsestekniske verktøy ment for autorisert sårbarhetsforskning, defensiv programvarerevisjon og akademiske CTF-konkurranser. Å utnytte programvare uten tillatelse er ulovlig.