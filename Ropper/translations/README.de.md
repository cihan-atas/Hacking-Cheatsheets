<!-- language-home-navigation -->
[🏠 Deutsche Startseite](../../README.de.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🧩 Ropper & ROPgadget – Vollständiger Cheatsheet

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
<p align="center">
  <img src="https://img.shields.io/badge/Ropper-Gadget_Finder-blue?style=for-the-badge" alt="Ropper">
  <img src="https://img.shields.io/badge/ROPgadget-Exploit_Tool-orange?style=for-the-badge" alt="ROPgadget">
  <img src="https://img.shields.io/badge/Bypass-NX_%7C_DEP-red?style=for-the-badge" alt="NX Bypass">
  <img src="https://img.shields.io/badge/Category-Exploit_Development-green?style=for-the-badge" alt="Exploit Dev">
</p>

<p align="center">
  <b>🧩 Wichtige Befehlszeilen-Dienstprogramme zum Erkennen von Return-Oriented Programming (ROP) und Jump-Oriented Programming (JOP)-Gadgets zur Umgehung von Non-Executable (NX/DEP)-Speicherschutzvorrichtungen</b>
</p>

---

## 📋 Inhaltsverzeichnis

- [Übersicht & ROP-Theorie](#-overview--rop-theory)
- [Installation](#-Installation)
- [Basic Gadget Discovery](#-basic-gadget-discovery)
  - [Alle Gadgets extrahieren](#extracting-all-gadgets)
  - [Limiting Instruction Depth](#limiting-instruction- Depth)
- [Erweiterte Suche und Musterfilterung](#-advanced-search--pattern-filtering)
  - [Targeting spezifischer Register (`rdi`, `rsi`, `rdx`,
  - [Speicherschreib-Gadgets (Write-What-Where)](#memory-write-gadgets-write-what-where)
  - [Syscall- und Interrupt-Gadgets](#syscall--interrupt-gadgets)
- [Ausschließen fehlerhafter Zeichen (`--badbytes`)](#-exclusive-bad-characters---badbytes)
- [Automatisierte ROP-Kettengenerierung](#-automated-rop-chain-generation)
  - [Ropper Automated Chains](#ropper-automated-chains)
  - [ROPgadget Automated Chains](#ropgadget-automated-chains)
- [Analysieren geladener Bibliotheken (libc ROP-Ketten)](#-analyzing-loaded-libraries-libc-rop-chains)
- [Kurzreferenztabelle](#-quick-reference-table)
- [Überlegungen und Abhilfemaßnahmen zur Verteidigung](#-defensive-überlegungen--mitigations)
- [Rechtlicher Haftungsausschluss](#-legal-disclaimer)

---

## 🎯 Übersicht und ROP-Theorie

Moderne Betriebssysteme erzwingen **NX (No-Execute) / DEP (Data Execution Prevention)**, was die direkte Ausführung von Shellcode verhindert, der auf dem Stapel oder Heap platziert ist.

**Rückgabeorientierte Programmierung (ROP)** umgeht diese Einschränkung, indem kleine vorhandene Maschinenanweisungen wiederverwendet werden, die bereits im ausführbaren Speicher vorhanden sind (`.text`-Abschnitt der Binär- oder gemeinsam genutzten Bibliotheken wie `libc.so`). Jede Befehlssequenz, die mit einer `ret`-Anweisung endet, wird als **Gadget** bezeichnet. Durch die Anordnung von Adressen auf dem Stapel verketten Angreifer Gadgets, um Folgendes zu erreichen:
1. Laden Sie Argumente in CPU-Register (z. B. `pop rdi; ret` für x64-Aufrufkonventionen).
2. Schreiben Sie Zeichenfolgen (wie `/bin/sh`) in beschreibbare `.bss`- oder `.data`-Abschnitte.
3. Lösen Sie `execve("/bin/sh", NULL, NULL)` aus oder rufen Sie `system()` auf.

---

## 📥 Installation
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

## 🚀 Grundlegende Gadget-Erkennung

### Alle Gadgets extrahieren
```bash
# Using Ropper (Faster multi-threaded engine)
ropper --file ./target_binary --nocolor

# Using ROPgadget
ROPgadget --binary ./target_binary
```
### Beschränkung der Anweisungstiefe
Reduzieren Sie laute lange Ketten, indem Sie die Gadget-Länge begrenzen (Anzahl der Anweisungen vor `ret`):
```bash
# Limit to maximum 3 instructions before return
ropper --file ./target_binary --inst-count 3
ROPgadget --binary ./target_binary --depth 3
```
---

## 🔍 Erweiterte Suche und Musterfilterung

### Ausrichtung auf bestimmte Register (`rdi`, `rsi`, `rdx`, `rax`)
In der 64-Bit-Linux-Aufrufkonvention müssen sich die ersten drei Argumente einer Funktion oder eines Systemaufrufs in `rdi`, `rsi` und `rdx` befinden:
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
Verwenden des ROPgadget-Regex-Matchings:
```bash
ROPgadget --binary ./target_binary --only "pop|ret" | grep -E "rdi|rsi|rdx|rax"
```
### Speicherschreib-Gadgets (Write-What-Where)
Wenn Sie `/bin/sh` oder beliebige Argumente im Speicher speichern müssen:
```bash
# Find instructions moving registers into pointer memory
ropper --file ./target_binary --search "mov [???], ???"
```
*Beispielübereinstimmung:* `mov qword ptr [rdi], rsi; ret` ermöglicht das Schreiben von allem, was in `rsi` steht, an die Adresse, auf die `rdi` zeigt.

### Syscall- und Interrupt-Gadgets
```bash
# Find raw syscall instructions
ropper --file ./target_binary --search "syscall"

# x86 32-bit software interrupt
ropper --file ./target_binary --search "int 0x80"
```
---

## 🚫 Ausschließen fehlerhafter Zeichen (`--badbytes`)

Schwachstellen bei der Zeichenfolgenformatierung oder Funktionen wie `strcpy()` enden bei Nullbytes (`\x00`), Zeilenumbrüchen (`\x0a`) oder Wagenrückläufen (`\x0d`). Schließen Sie Adressen aus, die diese Bytes enthalten:
```bash
# Filter out gadgets whose addresses contain bad characters
ropper --file ./target_binary --badbytes 000a0d --search "pop rdi"
ROPgadget --binary ./target_binary --badbytes "00|0a|0d"
```
---

## ⚡ Automatisierte ROP-Kettengenerierung

Beide Tools können automatisch funktionale Python-Exploit-Vorlagen erstellen, die `execve("/bin/sh")` aufrufen:

### Automatisierte Ketten von Roper
```bash
ropper --file ./target_binary --chain "execve"
```
### ROPgadget Automatisierte Ketten
```bash
ROPgadget --binary ./target_binary --ropchain
```
*Dadurch entsteht ein gebrauchsfertiges Python-Nutzlastskelett, das `mprotect()` oder `execve()` verkettet.*

---

## 📚 Analysieren geladener Bibliotheken (libc ROP Chains)

Wenn die Binärdatei selbst als **PIE** kompiliert ist oder nur wenige nützliche Gadgets enthält, extrahieren Sie Gadgets direkt aus der gemeinsam genutzten C-Bibliothek (`libc.so.6`) des Zielsystems:
```bash
# Search gadgets in libc
ropper --file /lib/x86_64-linux-gnu/libc.so.6 --search "pop rdi; ret"

# Find "/bin/sh" string offset inside libc
strings -a -t x /lib/x86_64-linux-gnu/libc.so.6 | grep "/bin/sh"
```
*(Erkannte Offsets zur durchgesickerten Basisadresse von libc zur Laufzeit hinzufügen).*

---

## ⚡ Kurzreferenztabelle

| Ziel | Roper-Befehl | ROPgadget-Befehl |
|-----------|----------------|-------------------|
| **Alle Gadgets entsorgen** | `ropper --file <bin>` | `ROPgadget --binary <bin>` |
| **Bestimmtes Gadget durchsuchen** | `ropper --file <bin> --search "pop rdi"` | `ROPgadget --binary <bin> \| grep "pop rdi"` |
| **Fehlerhafte Bytes filtern** | `ropper --file <bin> --badbytes 000a` | `ROPgadget --binary <bin> --badbytes "00\|0a"` |
| **Tiefe/Länge begrenzen** | `ropper --file <bin> --inst-count 3` | `ROPgadget --binary <bin> --depth 3` |
| **JOP-Gadgets durchsuchen** | `ropper --file <bin> --type jop` | `ROPgadget --binary <bin> --norop` |
| **ROP-Kette automatisch erstellen** | `ropper --file <bin> --chain "execve"`| `ROPgadget --binary <bin> --ropchain` |
| **Speicherschreibvorgänge durchsuchen** | `ropper --file <bin> --search "mov [???], ???"` | `ROPgadget --binary <bin> \| grep "mov \["` |

---

## 🛡️ Überlegungen und Abhilfemaßnahmen zur Verteidigung

1. **Control-Flow-Enforcement-Technologie (Intel CET / Shadow Stack):** Moderne Prozessoren unterhalten einen isolierten Hardware-Shadow-Stack, der Rücksprungadressen bei `ret` überprüft und ROP-Ketten sofort beendet.
2. **Clang / GCC Control Flow Integrity (CFI):** Validiert Funktionszeigerziele anhand eines Forward-Edge-Diagramms vor indirekten Aufrufen.

---

## ⚠️ Rechtlicher Haftungsausschluss

> [!ACHTUNG]
> Ropper und ROPgadget sind Dienstprogramme zur Exploit-Entwicklung und Software-Sicherheitsbewertung. Sie dürfen ausschließlich für autorisierte Penetrationstests, CTF-Herausforderungen oder defensive Sicherheitsforschung verwendet werden. Die Nutzung von Software ohne ausdrückliche Genehmigung ist illegal.