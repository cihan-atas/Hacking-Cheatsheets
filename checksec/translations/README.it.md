<!-- language-home-navigation -->
[🏠 Home Page Italiana](../../README.it.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🛡️ checksec e Binutils - Cheatsheet completo

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
   ██████╗██╗  ██╗███████╗ ██████╗██╗  ██╗███████╗███████╗ ██████╗
  ██╔════╝██║  ██║██╔════╝██╔════╝██║ ██╔╝██╔════╝██╔════╝██╔════╝
  ██║     ███████║█████╗  ██║     █████╔╝ ███████╗█████╗  ██║     
  ██║     ██╔══██║██╔══╝  ██║     ██╔═██╗ ╚════██║██╔══╝  ██║     
  ╚██████╗██║  ██║███████╗╚██████╗██║  ██╗███████║███████╗╚██████╗
   ╚═════╝╚═╝  ╚═╝╚══════╝ ╚═════╝╚═╝  ╚═╝╚══════╝╚══════╝ ╚═════╝
          Linux ELF Binary Mitigation Auditor & Inspection Toolkit
```
<palign="centro">
  <img src="https://img.shields.io/badge/checksec-Security_Mitigations-blue?style=for-the-badge" alt="checksec">
  <img src="https://img.shields.io/badge/readelf-ELF_Headers-orange?style=for-the-badge" alt="readelf">
  <img src="https://img.shields.io/badge/objdump-Disassembly-green?style=for-the-badge" alt="objdump">
  <img src="https://img.shields.io/badge/Platform-Linux_ELF-purple?style=for-the-badge" alt="Linux ELF">
</p>

<palign="centro">
  <b>🛡️ Strumenti essenziali da riga di comando per il controllo delle mitigazioni della sicurezza degli eseguibili Linux (RELRO, Stack Canary, NX, PIE, Fortify) e l'analisi di intestazioni binarie, sezioni e tabelle di simboli ELF</b>
</p>

---

## 📋 Sommario

- [Panoramica e matrice delle mitigazioni binarie](#-overview--binary-mitigations-matrix)
- [Installazione](#-installazione)
- [checksec: controllo di mitigazione rapida](#-checksec-rapid-mitigation-auditing)
  - [Auditing binari singoli e batch](#auditing-single--batch-binaries)
  - [Controllo dei processi in esecuzione](#processi-in-esecuzione)
  - [Uscita JSON e CSV](#json--uscita-csv)
- [readelf: Intestazione ELF e dissezione della sezione](#-readelf-elf-header--section-dissection)
  - [Intestazione file ELF (`-h`)](#elf-file-header--h)
  - [Intestazioni e segmenti del programma (`-l`)](#intestazioni-programma--segmenti--l)
  - [Intestazioni di sezione (`-S`)](#section-headers--s)
  - [Tabelle dei simboli (`-s` / `--syms`)](#tabelle-simboli--s----simboli)
  - [Dipendenze e librerie dinamiche (`-d`)](#dipendenze-dinamiche--librerie--d)
- [objdump: smontaggio e riposizionamento](#-objdump-disassembly--relocations)
  - [Disassemblaggio Intel (`-d -M intel`)](#disassemblaggio-intel--d--m-intel)
  - [Voci di trasferimento e GOT (`-R`)](#voci-di-trasferimento--got--r)
- [nm e stringhe: risoluzione dei simboli e stringhe](#-nm--strings-symbol-solving--strings)
  - [Elenco dei simboli demangled con `nm`](#listing-demangled-symbols-with-nm)
  - [Estrazione di stringhe offuscate con `strings`](#estrazione-stringhe-offuscate-con-stringhe)
- [Riferimento compilazione rinforzata](#-riferimento-compilazione-hardened)
- [Tabella di riferimento rapido](#-tabella-di-riferimento-rapido)
- [Disclaimer legale](#-legal-disclaimer)

---

## 🎯 Panoramica e matrice delle mitigazioni binarie

Prima di iniziare qualsiasi flusso di lavoro di reverse engineering o sfruttamento, è obbligatorio comprendere quali mitigazioni della sicurezza sono abilitate:

| Mitigazione | Nome completo | Meccanismo di protezione | Strategia di bypass |
|------------|-----------|----------------------|-----------|
| **RELRO** | Riposizionamento di sola lettura | Rende `.got` di sola lettura (`Full RELRO`) per impedire la sovrascrittura del puntatore | Strutture heap di destinazione, indirizzi di ritorno o hook di funzioni |
| **Canarie** | Protezione dello stack | Inserisce un cookie randomizzato prima del puntatore di ritorno; verifica l'integrità al momento della restituzione | Perdita del valore canary tramite perdita di memoria o forza bruta (biforcazione dei demoni) |
| **NX** | Nessuna esecuzione / DEP | Contrassegna stack, heap e segmenti di dati come non eseguibili | Programmazione orientata al ritorno (ROP) o Programmazione orientata al salto (JOP) |
| **TORTA** | Posizione Eseguibile Indipendente | Carica l'indirizzo di base binario in una posizione casuale nella memoria virtuale (ASLR) | Puntatore del codice di perdita per calcolare l'offset dell'indirizzo di base |
| **Fortifica** | `_FORTIFY_SOURCE` | Sostituisce le funzioni di stringa non sicure (`strcpy` -> `__strcpy_chk`) con il controllo dei limiti | Bug logici di non overflow, stringa di formato senza buffer overflow |

---

## 📥Installazione
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y checksec binutils

# Arch Linux
sudo pacman -S checksec binutils

# Python pip (standalone checksec)
pip install checksec.py
```
---

## ⚡ checksec: audit di mitigazione rapida

### Controllo di file binari singoli e batch
```bash
# Audit a single executable
checksec --file=./target_binary

# Audit all binaries in directory
checksec --dir=/usr/bin/
```
*Esempio di output standard:*
```text
RELRO           STACK CANARY      NX            PIE             RPATH      RUNPATH      FILE
Full RELRO      Canary found      NX enabled    PIE enabled     No RPATH   No RUNPATH   ./target_binary
```
### Controllo dei processi in esecuzione
Controlla le mitigazioni della memoria e lo stato ASLR di un demone attivo:
```bash
# Audit by Process ID (PID)
checksec --proc=1337

# Audit process by name
checksec --proc-all
```
### Output JSON e CSV
Esportare i risultati per l'analisi automatizzata:
```bash
checksec --file=./target_binary --output=json > security.json
checksec --file=./target_binary --output=csv > security.csv
```
---

## 🔬 readelf: intestazione ELF e dissezione della sezione

L'utilità `readelf` visualizza le informazioni dai file ELF senza invocare alcuna esecuzione:

### Intestazione file ELF (`-h`)
Rivela l'architettura della CPU, la classe a 32 o 64 bit, l'indirizzo del punto di ingresso e l'endianness:
```bash
readelf -h ./target_binary
```
### Intestazioni e segmenti di programma (`-l`)
Visualizza i segmenti caricati in memoria in fase di esecuzione e i flag (`R`, `W`, `E`). Fondamentale per identificare se lo stack (`GNU_STACK`) dispone dei permessi di esecuzione:
```bash
readelf -l ./target_binary
```
*(Se `GNU_STACK` ha `RWE`, l'esecuzione dello stack è consentita; NX è disabilitato).*

### Intestazioni delle sezioni (`-S`)
Ispeziona layout, indirizzi e offset della memoria per `.text` (codice), `.data` (dati inizializzati), `.rodata` (costanti), `.bss` (dati non inizializzati), `.plt` e `.got`:
```bash
readelf -S ./target_binary
```
### Tabelle dei simboli (`-s` / `--syms`)
Elenca tutte le funzioni esportate, le variabili globali e i simboli libc importati:
```bash
# List all symbols
readelf -s ./target_binary

# Filter dynamic symbols used at runtime
readelf --dyn-syms ./target_binary
```
### Dipendenze e librerie dinamiche (`-d`)
Elenca le librerie condivise richieste (tag `NEEDED`, ad esempio `libc.so.6`, `libssl.so`):
```bash
readelf -d ./target_binary
```
---

## 🧩 objdump: Smontaggio e trasloco

### Disassemblaggio Intel (`-d -M intel`)
Disassemblare le sezioni eseguibili in un assembly pulito di sintassi Intel:
```bash
# Disassemble .text section in Intel syntax
objdump -d -M intel ./target_binary

# Disassemble specific function (e.g. main)
objdump -d -M intel ./target_binary | grep -A 30 "<main>:"
```
### Voci di trasferimento e GOT (`-R`)
Visualizzare gli indirizzi di riposizionamento della Global Offset Table (GOT) per le chiamate di libreria importate:
```bash
objdump -R ./target_binary
```
*Mostra gli indirizzi target per funzioni come `puts`, `printf` e `system`.*

---

## 🔍 nm e stringhe: risoluzione dei simboli e stringhe

### Elenco dei simboli sminuzzati con `nm`
```bash
# List defined symbols only (ignore external imports)
nm --defined-only -C ./target_binary

# List undefined external symbols (functions resolved by dynamic linker)
nm -u -C ./target_binary
```
### Estrazione di stringhe offuscate con `strings`
```bash
# Extract ASCII strings with minimum 8 characters
strings -a -n 8 ./target_binary

# Extract 16-bit Little Endian wide strings (UTF-16 e.g., Windows / Java)
strings -e l -a ./target_binary

# Print byte offset of each string in hex
strings -a -t x ./target_binary
```
---

## 🛡️ Riferimento alla compilazione Hardened

Per creare un binario ELF con le massime mitigazioni della sicurezza:
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

## ⚡ Tabella di consultazione rapida

| Obiettivo | Comando |
|-----------|---------|
| **Mitigazioni del controllo** | `checksec --file=<bin>` |
| **Audit in esecuzione PID** | `checksec --proc=<pid>` |
| **Visualizza intestazione ELF** | `readelf -h <bin>` |
| **Visualizza flag Stack NX** | `readelf -l <bin> \| grep -A 1 GNU_STACK` |
| **Sezioni elenco (.got, .plt)**| `readelf -S <bin>` |
| **Visualizza simboli dinamici** | `readelf --dyn-syms <bin>` |
| **Smontaggio Intel** | `objdump -d -M intel <bin>` |
| **Ispeziona i trasferimenti GOT** | `objdump -R <bin>` |
| **Elenco Simboli (Demangled)** | `nm -C <bin>` |
| **Estrai stringhe UTF-16** | `strings -e l -a <bin>` |
| **Mostra offset stringa** | `strings -a -t x <bin>` |

---

## ⚠️ Disclaimer legale

> [!ATTENZIONE]
> checksec e GNU Binutils sono strumenti diagnostici e di reverse engineering destinati al controllo della sicurezza del software, alla convalida della difesa e alla ricerca binaria autorizzata. L'ispezione del software non autorizzato deve rispettare le normative locali sulla proprietà intellettuale.