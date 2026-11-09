<!-- language-home-navigation -->
[🏠 Home Page Italiana](../../README.it.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🎯 pwndbg & GEF - Cheatsheet completo

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
<palign="centro">
  <img src="https://img.shields.io/badge/pwndbg-GDB_Plugin-blue?style=for-the-badge" alt="pwndbg">
  <img src="https://img.shields.io/badge/GEF-GDB_Enhanced_Features-orange?style=for-the-badge" alt="GEF">
  <img src="https://img.shields.io/badge/Category-Binary_Exploitation_&_RE-green?style=for-the-badge" alt="Inversione e Pwn">
  <img src="https://img.shields.io/badge/License-MIT_%7C_FOSS-purple?style=for-the-badge" alt="Licenza FOSS">
</p>

<palign="centro">
  <b>🎯 I principali miglioramenti del terminale GDB open source progettati per il reverse engineering binario, l'analisi del danneggiamento della memoria, l'ispezione del layout dell'heap e lo sviluppo di exploit CTF</b>
</p>

---

## 📋 Sommario

- [Panoramica e perché Vanilla GDB necessita di miglioramenti](#-overview--why-vanilla-gdb-needs-enhancements)
- [Installazione e passaggio da un framework all'altro](#-installazione--scambio-da-un-framework all'altro)
  - [Installazione di pwndbg](#installazione-pwndbg)
  - [Installazione di GEF](#installazione-gef)
  - [Gestione di `.gdbinit`](#managing-gdbinit)
- [Il motore di visualizzazione del contesto](#-the-context-display-engine)
  - [Sezione Registri](#sezione-registri)
  - [Disassemblaggio e previsioni di salto](#disassemblaggio--previsioni di salto)
  - [Stack e telescopio](#stack--telescopio)
  - [Backtrace e discussioni](#backtrace--discussioni)
- [Ispezione memoria e navigazione](#-ispezione-memoria--navigazione)
  - [Puntatori telescopici (`telescope`)](#puntatori-telescopici-telescopio)
  - [Mappature memoria processo (`vmmap`)](#process-memory-mappings-vmmap)
  - [Ricerca memoria (`search`)](#ricerca-memoria-ricerca)
- [Sfruttamento e ispezione dell'heap](#-heap-exploitation--inspection)
  - [Visualizzazione di blocchi e arene](#viewing-chunks--arenas)
  - [Ispezione dei contenitori liberi (Tcache, Fastbins, Smallbins)](#inspecting-free-bins-tcache-fastbins-smallbins)
  - [Visualizzazione del layout dell'heap (`vis_heap_chunks`)](#visualizing-heap-layout-vis_heap_chunks)
- [Aiutanti per lo sviluppo di exploit](#-exploit-development-helpers)
  - [Checksec e controllo di mitigazione](#checksec--controllo-di-mitigazione)
  - [Locatore stack canary (`canary`)](#stack-canary-locator-canary)
  - [Schemi ciclici e calcolo dell'offset](#schemi-ciclici--calcolo-offset)
  - [Ricerca di gadget ROP in memoria](#ricerca-di-gadget-in-memoria-rop)
- [Tabella di riferimento rapido](#-tabella-di-riferimento-rapido)
- [Considerazioni difensive e protezioni binarie](#-considerazioni-difensive--protezioni-binarie)
- [Disclaimer legale](#-legal-disclaimer)

---

## 🎯 Panoramica e perché Vanilla GDB necessita di miglioramenti

Standard GNU Debugger (`gdb`) è uno strumento di livello industriale, ma la sua esperienza utente immediata è visivamente austera. Non aggiorna automaticamente gli stati dei registri, non dereferenzia i puntatori multilivello né visualizza le visualizzazioni dello stack durante l'esecuzione delle istruzioni.

**pwndbg** e **GEF** trasformano GDB in una suite d'élite di reverse engineering e sfruttamento binario:
- **pwndbg:** Progettato specificamente per lo sfruttamento del software di basso livello, il debugging dell'heap (interni dell'allocatore di glibc 2.27 - 2.39+) e il dereferenziamento automatico dei puntatori.
- **GEF (GDB Enhanced Features):** un singolo script Python autonomo con zero dipendenze esterne, che offre funzionalità indipendenti dall'architettura (ARM, MIPS, PowerPC, x86) e analisi dinamica della struttura.

---

## 📥 Installazione e passaggio da un framework all'altro

### Installazione di pwndbg
```bash
git clone https://github.com/pwndbg/pwndbg
cd pwndbg
./setup.sh
```
### Installazione di GEF
```bash
# Automated one-liner download
bash -c "$(curl -fsSL https://gef.blah.cat/sh)"
```
### Gestione di `.gdbinit`
Per passare da pwndbg a GEF senza conflitti, configura il tuo `~/.gdbinit`:
```bash
# To use pwndbg:
echo "source /path/to/pwndbg/gdbinit.py" > ~/.gdbinit

# To use GEF:
echo "source ~/.gdbinit-gef.py" > ~/.gdbinit
```
---

## 🖥️Il motore di visualizzazione del contesto

Ogni volta che l'esecuzione si interrompe in corrispondenza di un punto di interruzione o di un passaggio, sia pwndbg che GEF stampano una visualizzazione terminale automatizzata a più pannelli:
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

## 🔍 Ispezione e navigazione della memoria

### Puntatori telescopici (`telescope`)
Dereferenzia i puntatori di memoria in modo ricorsivo, visualizzando testo ASCII, simboli di funzione o puntatori di destinazione:
```text
pwndbg> telescope $rsp 16       # Dereference 16 entries starting at RSP
pwndbg> telescope 0x404000 8    # Dereference data section
```
### Mappature della memoria del processo (`vmmap`)
Visualizza segmenti di memoria virtuale completi con permessi di lettura/scrittura/esecuzione:
```text
pwndbg> vmmap
```
*Individua rapidamente i segmenti con i permessi `rwx` (fondamentale per l'iniezione di shellcode).*

### Ricerca nella memoria (`search`)
Individua stringhe, numeri interi o puntatori ovunque nelle pagine di memoria caricate:
```text
pwndbg> search -s "/bin/sh"     # Search for string "/bin/sh"
pwndbg> search -p 0x401135      # Search for pointers to main()
pwndbg> search -x "41414141"    # Search for raw hexadecimal bytes
```
---

## 🧱 Sfruttamento e ispezione dell'heap

L'analisi dei moderni allocatori heap `glibc` è semplice con i comandi heap dedicati:

### Visualizzazione di blocchi e arene
```text
pwndbg> heap                    # Display entire heap layout
pwndbg> chunk 0x602000          # Parse chunk headers (size, flags: PREV_INUSE, IS_MMAPPED, NON_MAIN_ARENA)
pwndbg> arenas                  # List all active heap arenas
```
### Ispezione dei contenitori liberi (Tcache, Fastbins, Smallbins)
```text
pwndbg> bins                    # Show all free bins simultaneously
pwndbg> tcache                  # Display per-thread tcache entries
pwndbg> fastbins                # List single-linked fastbins
pwndbg> unsortedbin             # List unsorted bin chunks (used for libc leak)
```
### Visualizzazione del layout dell'heap (`vis_heap_chunks`)
```text
pwndbg> vis_heap_chunks 10      # Draw color-coded visual representation of consecutive heap chunks
```
---

## 🛠️ Sfrutta gli aiutanti dello sviluppo

### Controllo di verifica e mitigazione
Controlla le mitigazioni della sicurezza del compilatore direttamente dal prompt di GDB:
```text
pwndbg> checksec
```
*Ispeziona:*
- **RELRO:** RELRO parziale vs completo (protegge la tabella di offset globale `.got.plt`)
- **Stack:** Canarino trovato vs Nessun canarino
- **NX:** NX abilitato (stack/heap non eseguibile)
- **PIE:** PIE abilitato (eseguibile indipendente dalla posizione/ASLR)

### Localizzatore Stack Canary (`canary`)
Individua il valore canary del thread nel Thread Local Storage (TLS) e verifica dove risiede nello stack frame corrente:
```text
pwndbg> canary
```
### Schemi ciclici e calcolo dell'offset
Determinare l'offset esatto per sovrascrivere il puntatore dell'istruzione (EIP/RIP):
```text
# Generate a 100-byte De Bruijn cyclic sequence
pwndbg> cyclic 100

# After the crash (SIGSEGV), calculate offset from faulty address or register value
pwndbg> cyclic -l $rip
# Output: Finding cyclic pattern at offset 72
```
### Ricerca di gadget ROP in memoria
Cerca i segmenti eseguibili caricati per gadget specifici:
```text
pwndbg> rop --grep "pop rdi"
```
---

## ⚡ Tabella di consultazione rapida

| Obiettivo | Comando |
|-----------|---------|
| **Inizia con pwndbg/GEF**| `gdb ./target_binary` |
| **Ispeziona registri/stack** | `context` |
| **Puntatore ricorsivo Deref**| `telescope $rsp 10` |
| **Mappa memoria e controllo RWX**| `vmmap` |
| **Cerca stringa in memoria**| `search -s "/bin/sh"` |
| **Mitigazioni del controllo** | `checksec` |
| **Trova Stack Canary** | `canary` |
| **Panoramica dei blocchi heap** | `heap` |
| **Ispeziona i contenitori liberi** | `bins` |
| **Calcola l'offset del buffer**| `cyclic 100` -> `cyclic -l <val>` |
| **Imposta punto di interruzione sul simbolo**| `b *main` |
| **Esegui/Continua/Passa** | `r` / `c` / `si` / `ni` |

---

## 🛡️ Considerazioni difensive e protezioni binarie

1. **RELRO completo:** La compilazione con `-Wl,-z,relro,-z,now` rende `.got` completamente di sola lettura, sconfiggendo gli attacchi di sovrascrittura GOT.
2. **Stack Canarini:** La compilazione con `-fstack-protector-all` inserisce i cookie prima degli indirizzi di ritorno, causando `__stack_chk_fail` in overflow del buffer.
3. **PIE e ASLR:** randomizza il codice, l'heap e gli indirizzi di base della libreria ad ogni esecuzione.

---

## ⚠️ Disclaimer legale

> [!ATTENZIONE]
> pwndbg e GEF sono strumenti di analisi binaria e di ingegneria degli exploit destinati esclusivamente alla ricerca autorizzata sulle vulnerabilità, al controllo difensivo del software e alle competizioni accademiche CTF. Lo sfruttamento del software senza autorizzazione è illegale.