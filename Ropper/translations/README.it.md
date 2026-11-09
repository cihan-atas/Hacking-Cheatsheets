<!-- language-home-navigation -->
[🏠 Home Page Italiana](../../README.it.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🧩 Ropper & ROPgadget - Cheatsheet completo

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ██████╗  ██████╗ ██████╗ ██████╗ ███████╗██████╗ 
  ██╔══██╗██╔═══██╗██╔══██╗██╔══██╗██╔════╝██╔══██╗
  ██████╔╝██║   ██║██████╔╝██████╔╝█████╗  ██████╔╝
  ██╔══██╗██║   ██║██╔═══╝ ██╔═══╝ ██╔══╝  ██╔══██╗
  ██║  ██║╚██████╔╝██║     ██║     ███████╗██║  ██║
  ╚═╝  ╚═╝ ╚═════╝ ╚═╝     ╚═╝     ╚══════╝╚═╝  ╚═╝
     Advanced ROP & JOP Gadget Finder and Chain Generator
```
<palign="centro">
  <img src="https://img.shields.io/badge/Ropper-Gadget_Finder-blue?style=for-the-badge" alt="Ropper">
  <img src="https://img.shields.io/badge/ROPgadget-Exploit_Tool-orange?style=for-the-badge" alt="ROPgadget">
  <img src="https://img.shields.io/badge/Bypass-NX_%7C_DEP-red?style=for-the-badge" alt="NX Bypass">
  <img src="https://img.shields.io/badge/Category-Exploit_Development-green?style=for-the-badge" alt="Exploit Dev">
</p>

<palign="centro">
  <b>🧩 Utilità essenziali della riga di comando per scoprire i gadget di programmazione orientata al ritorno (ROP) e di programmazione orientata al salto (JOP) per aggirare le protezioni della memoria non eseguibile (NX / DEP)</b>
</p>

---

## 📋 Sommario

- [Panoramica e teoria ROP](#-panoramica--teoria-ROP)
- [Installazione](#-installazione)
- [Individuazione gadget di base](#-basic-gadget-discovery)
  - [Estrazione di tutti i gadget](#extracting-all-gadgets)
  - [Limitazione della profondità delle istruzioni](#limitazione-profondità delle istruzioni)
- [Ricerca avanzata e filtro pattern](#-ricerca-avanzata--filtro-pattern)
  - [Targeting di registri specifici (`rdi`, `rsi`, `rdx`, `rax`)](#targeting-specific-registers-rdi-rsi-rdx-rax)
  - [Gadget di scrittura in memoria (Scrivi-cosa-dove)](#memory-write-gadget-scrivi-cosa-dove)
  - [Gadget di syscall e interruzione](#syscall--interrupt-gadgets)
- [Esclusi i caratteri errati (`--badbytes`)](#-esclusi-caratteri-errati---badbytes)
- [Generazione automatizzata della catena ROP](#-automated-rop-chain- generation)
  - [Catene automatizzate Ropper](#catene-automatiche-ropper)
  - [Catene automatizzate ROPgadget](#ropgadget-catene-automatiche)
- [Analisi delle librerie caricate (libc ROP Chains)](#-analyzing-loaded-libraries-libc-rop-chains)
- [Tabella di riferimento rapido](#-tabella-di-riferimento-rapido)
- [Considerazioni difensive e mitigazioni](#-considerazioni-difensive--mitigazioni)
- [Disclaimer legale](#-legal-disclaimer)

---

## 🎯 Panoramica e teoria ROP

I moderni sistemi operativi applicano **NX (No-Execute) / DEP (Data Execution Prevention)**, che impedisce l'esecuzione diretta dello shellcode inserito nello stack o nell'heap.

La **Programmazione orientata al ritorno (ROP)** aggira questa restrizione riutilizzando piccole istruzioni macchina esistenti già presenti nella memoria eseguibile (sezione `.text` del binario o librerie condivise come `libc.so`). Ogni sequenza di istruzioni che termina con un'istruzione `ret` è chiamata **gadget**. Disponendo gli indirizzi in pila, gli aggressori concatenano i gadget per:
1. Caricare gli argomenti nei registri della CPU (ad esempio, `pop rdi; ret` per le convenzioni di chiamata x64).
2. Scrivere stringhe (come `/bin/sh`) in sezioni `.bss` o `.data` scrivibili.
3. Attiva `execve("/bin/sh", NULL, NULL)` o richiama `system()`.

---

## 📥Installazione
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y ropper python3-ropgadget

# Python pip (Latest Releases)
pip install ropper capstone
pip install ROPgadget

# Arch Linux
sudo pacman -S ropper python-ropgadget
```
---

## 🚀 Scoperta di gadget di base

### Estrazione di tutti i gadget
```bash
# Using Ropper (Faster multi-threaded engine)
ropper --file ./target_binary --nocolor

# Using ROPgadget
ROPgadget --binary ./target_binary
```
### Limitazione della profondità delle istruzioni
Riduci le catene lunghe e rumorose limitando la lunghezza del gadget (numero di istruzioni prima di `ret`):
```bash
# Limit to maximum 3 instructions before return
ropper --file ./target_binary --inst-count 3
ROPgadget --binary ./target_binary --depth 3
```
---

## 🔍 Ricerca avanzata e filtro pattern

### Targeting di registri specifici (`rdi`, `rsi`, `rdx`, `rax`)
Nella convenzione di chiamata Linux a 64 bit, i primi tre argomenti di una funzione o chiamata di sistema devono risiedere in `rdi`, `rsi` e `rdx`:
```bash
# Find pop rdi gadgets (1st argument)
ropper --file ./target_binary --search "pop rdi"

# Find pop rsi gadgets (2nd argument)
ropper --file ./target_binary --search "pop rsi"

# Find pop rdx gadgets (3rd argument)
ropper --file ./target_binary --search "pop rdx"

# Find gadgets that control RAX (Syscall number: 59 for execve)
ropper --file ./target_binary --search "pop rax"
```
Utilizzo della corrispondenza regex ROPgadget:
```bash
ROPgadget --binary ./target_binary --only "pop|ret" | grep -E "rdi|rsi|rdx|rax"
```
### Gadget di scrittura in memoria (Scrivi cosa-dove)
Quando è necessario archiviare `/bin/sh` o argomenti arbitrari in memoria:
```bash
# Find instructions moving registers into pointer memory
ropper --file ./target_binary --search "mov [???], ???"
```
*Corrispondenza di esempio:* `mov qword ptr [rdi], rsi; ret` consente di scrivere tutto ciò che è in `rsi` nell'indirizzo indicato da `rdi`.

### Gadget di syscall e interruzione
```bash
# Find raw syscall instructions
ropper --file ./target_binary --search "syscall"

# x86 32-bit software interrupt
ropper --file ./target_binary --search "int 0x80"
```
---

## 🚫 Esclusi i personaggi cattivi (`--badbytes`)

Le vulnerabilità o le funzioni di formattazione delle stringhe come `strcpy()` terminano con byte nulli (`\x00`), ritorni a capo (`\x0a`) o ritorni a capo (`\x0d`). Escludi indirizzi contenenti questi byte:
```bash
# Filter out gadgets whose addresses contain bad characters
ropper --file ./target_binary --badbytes 000a0d --search "pop rdi"
ROPgadget --binary ./target_binary --badbytes "00|0a|0d"
```
---

## ⚡ Generazione automatizzata della catena ROP

Entrambi gli strumenti possono creare automaticamente modelli di exploit Python funzionali che invocano `execve("/bin/sh")`:

### Catene automatizzate Ropper
```bash
ropper --file ./target_binary --chain "execve"
```
### Catene automatizzate ROPgadget
```bash
ROPgadget --binary ./target_binary --ropchain
```
*Questo produce uno scheletro di payload Python pronto all'uso che concatena `mprotect()` o `execve()`.*

---

## 📚 Analisi delle librerie caricate (catene ROP libc)

Quando il file binario stesso viene compilato come **PIE** o contiene pochi gadget utili, estrai i gadget direttamente dalla libreria C condivisa del sistema di destinazione (`libc.so.6`):
```bash
# Search gadgets in libc
ropper --file /lib/x86_64-linux-gnu/libc.so.6 --search "pop rdi; ret"

# Find "/bin/sh" string offset inside libc
strings -a -t x /lib/x86_64-linux-gnu/libc.so.6 | grep "/bin/sh"
```
*(Aggiungi gli offset rilevati all'indirizzo base trapelato di libc in fase di runtime).*

---

## ⚡ Tabella di consultazione rapida

| Obiettivo | Comando Ropper | Comando ROPgadget |
|-----------|----------------|---------------------------|
| **Scarica tutti i gadget** | `ropper --file <bin>` | `ROPgadget --binary <bin>` |
| **Cerca gadget specifici** | `ropper --file <bin> --search "pop rdi"` | `ROPgadget --binary <bin> \| grep "pop rdi"` |
| **Filtra byte danneggiati** | `ropper --file <bin> --badbytes 000a` | `ROPgadget --binary <bin> --badbytes "00\|0a"` |
| **Limite profondità/lunghezza** | `ropper --file <bin> --inst-count 3` | `ROPgadget --binary <bin> --depth 3` |
| **Cerca gadget JOP** | `ropper --file <bin> --type jop` | `ROPgadget --binary <bin> --norop` |
| **Costruisci automaticamente la catena ROP** | `ropper --file <bin> --chain "execve"`| `ROPgadget --binary <bin> --ropchain` |
| **Cerca scritture memoria** | `ropper --file <bin> --search "mov [???], ???"` | `ROPgadget --binary <bin> \| grep "mov \["` |

---

## 🛡️ Considerazioni difensive e mitigazioni

1. **Tecnologia di applicazione del flusso di controllo (Intel CET/Shack Stack):** i processori moderni mantengono uno stack shadow hardware isolato che verifica gli indirizzi di ritorno su `ret`, terminando immediatamente le catene ROP.
2. **Clang / GCC Control Flow Integrity (CFI):** Convalida gli obiettivi del puntatore a funzione rispetto a un grafico forward-edge prima delle chiamate indirette.

---

## ⚠️ Disclaimer legale

> [!ATTENZIONE]
> Ropper e ROPgadget sono utilità di sviluppo di exploit e di valutazione della sicurezza del software. Devono essere rigorosamente utilizzati nei test di penetrazione autorizzati, nelle sfide CTF o nella ricerca sulla sicurezza difensiva. Lo sfruttamento del software senza esplicita autorizzazione è illegale.