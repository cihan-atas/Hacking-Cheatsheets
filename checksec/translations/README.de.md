<!-- language-home-navigation -->
[🏠 Deutsche Startseite](../../README.de.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🛡️ checksec & Binutils – Vollständiger Cheatsheet

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
<p align="center">
  <img src="https://img.shields.io/badge/checksec-Security_Mitigations-blue?style=for-the-badge" alt="checksec">
  <img src="https://img.shields.io/badge/readelf-ELF_Headers-orange?style=for-the-badge" alt="readelf">
  <img src="https://img.shields.io/badge/objdump-Disassembly-green?style=for-the-badge" alt="objdump">
  <img src="https://img.shields.io/badge/Platform-Linux_ELF-purple?style=for-the-badge" alt="Linux ELF">
</p>

<p align="center">
  <b>🛡️ Wichtige Befehlszeilentools für die Prüfung von Sicherheitsmaßnahmen für ausführbare Linux-Dateien (RELRO, Stack Canary, NX, PIE, Fortify) und die Analyse von ELF-Binärheadern, Abschnitten und Symboltabellen</b>
</p>

---

## 📋 Inhaltsverzeichnis

- [Übersicht und binäre Mitigationsmatrix](#-overview--binary-mitigations-matrix)
- [Installation](#-Installation)
- [checksec: Rapid Mitigation Auditing](#-checksec-rapid-mitigation-auditing)
  - [Überprüfung einzelner und Batch-Binärdateien](#auditing-single--batch-binaries)
  - [Überwachen laufender Prozesse](#auditing-running-processes)
  - [JSON- und CSV-Ausgabe](#json--csv-output)
- [readelf: ELF Header & Section Dissection](#-readelf-elf-header--section-dissection)
  - [ELF-Dateiheader (`-h`)](#elf-file-header--h)
  - [Programm-Header und -Segmente (`-l`)](#program-headers--segments--l)
  - [Abschnittsüberschriften (`-S`)](#section-headers--s)
  - [Symboltabellen (`-s` / `--syms`)](#symbol-tables--s----syms)
  - [Dynamische Abhängigkeiten und Bibliotheken (`-d`)](#dynamic-dependencies--libraries--d)
- [objdump: Disassembly & Relocations](#-objdump-disassembly--relocations)
  - [Intel Disassembly (`-d -M intel`)](#intel-disassembly--d--m-intel)
  - [Umzugseinträge & GOT (`-R`)](#relocation-entries--got--r)
- [nm & strings: Symbolauflösung & strings](#-nm--strings-symbol-resolution--strings)
  - [Demangled Symbols mit `nm` auflisten](#listing-demangled-symbols-with-nm)
  - [Verschleierte Strings mit `strings` extrahieren](#extracting-obfuscated-strings-with-strings)
- [Referenz zur gehärteten Kompilierung](#-hardened-compilation-reference)
- [Kurzreferenztabelle](#-quick-reference-table)
- [Rechtlicher Haftungsausschluss](#-legal-disclaimer)

---

## 🎯 Übersicht und binäre Schadensbegrenzungsmatrix

Bevor Sie mit einem Reverse-Engineering- oder Exploitation-Workflow beginnen, müssen Sie unbedingt wissen, welche Sicherheitsmaßnahmen aktiviert sind:

| Schadensbegrenzung | Vollständiger Name | Schutzmechanismus | Bypass-Strategie |
|------------|-----------|--------|---|
| **RELRO** | Verlagerung schreibgeschützt | Macht `.got` schreibgeschützt (`Full RELRO`), um ein Überschreiben des Zeigers zu verhindern | Ziel-Heap-Strukturen, Rücksprungadressen oder Funktions-Hooks |
| **Kanarienvogel** | Stapelschutz | Platziert ein zufälliges Cookie vor dem Rückgabezeiger; prüft die Integrität bei der Rückgabe | Canary-Wert durch Speicherverlust oder Brute-Force (Forking-Daemons) verloren gehen |
| **NX** | No-Execute / DEP | Markiert Stack-, Heap- und Datensegmente als nicht ausführbar | Rückkehrorientierte Programmierung (ROP) oder Sprungorientierte Programmierung (JOP) |
| **KUCHEN** | Unabhängige ausführbare Datei positionieren | Lädt die binäre Basisadresse an einer zufälligen Stelle im virtuellen Speicher (ASLR) | Leak-Code-Zeiger zur Berechnung des Basisadressen-Offsets |
| **Befestigen** | `_FORTIFY_SOURCE` | Ersetzt unsichere Zeichenfolgenfunktionen (`strcpy` -> `__strcpy_chk`) durch Grenzüberprüfung | Fehler in der Nicht-Überlauf-Logik, Zeichenfolge ohne Pufferüberlauf formatieren |

---

## 📥 Installation
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y checksec binutils

# Arch Linux
sudo pacman -S checksec binutils

# Python pip (standalone checksec)
pip install checksec.py
```
---

## ⚡ checksec: Schnelle Schadensbegrenzungsprüfung

### Prüfung einzelner und Batch-Binärdateien
```bash
# Audit a single executable
checksec --file=./target_binary

# Audit all binaries in directory
checksec --dir=/usr/bin/
```
*Beispiel für eine Standardausgabe:*
```text
RELRO           STACK CANARY      NX            PIE             RPATH      RUNPATH      FILE
Full RELRO      Canary found      NX enabled    PIE enabled     No RPATH   No RUNPATH   ./target_binary
```
### Überwachung laufender Prozesse
Überwachen Sie Speicherminderungen und ASLR-Status eines aktiven Daemons:
```bash
# Audit by Process ID (PID)
checksec --proc=1337

# Audit process by name
checksec --proc-all
```
### JSON- und CSV-Ausgabe
Ergebnisse zur automatisierten Analyse exportieren:
```bash
checksec --file=./target_binary --output=json > security.json
checksec --file=./target_binary --output=csv > security.csv
```
---

## 🔬 readelf: ELF-Header- und Abschnittszerlegung

Das Dienstprogramm `readelf` zeigt Informationen aus ELF-Dateien an, ohne eine Ausführung aufzurufen:

### ELF-Datei-Header (`-h`)
Zeigt CPU-Architektur, 32- oder 64-Bit-Klasse, Einstiegspunktadresse und Endianness:
```bash
readelf -h ./target_binary
```
### Programmköpfe und -segmente (`-l`)
Zeigt zur Laufzeit in den Speicher geladene Segmente und Flags (`R`, `W`, `E`) an. Entscheidend für die Feststellung, ob der Stack (`GNU_STACK`) über Ausführungsberechtigungen verfügt:
```bash
readelf -l ./target_binary
```
*(Wenn `GNU_STACK` über `RWE` verfügt, ist die Stapelausführung zulässig; NX ist deaktiviert.*

### Abschnittsüberschriften (`-S`)
Überprüfen Sie Speicherlayouts, Adressen und Offsets für `.text` (Code), `.data` (initialisierte Daten), `.rodata` (Konstanten), `.bss` (nicht initialisierte Daten), `.plt` und `.got`:
```bash
readelf -S ./target_binary
```
### Symboltabellen (`-s` / `--syms`)
Listen Sie alle exportierten Funktionen, globalen Variablen und importierten libc-Symbole auf:
```bash
# List all symbols
readelf -s ./target_binary

# Filter dynamic symbols used at runtime
readelf --dyn-syms ./target_binary
```
### Dynamische Abhängigkeiten und Bibliotheken (`-d`)
Listet erforderliche gemeinsam genutzte Bibliotheken auf (`NEEDED`-Tags, z. B. `libc.so.6`, `libssl.so`):
```bash
readelf -d ./target_binary
```
---

## 🧩 objdump: Demontage & Umzüge

### Intel-Demontage (`-d -M intel`)
Zerlegen Sie ausführbare Abschnitte in eine saubere Intel-Syntax-Assembly:
```bash
# Disassemble .text section in Intel syntax
objdump -d -M intel ./target_binary

# Disassemble specific function (e.g. main)
objdump -d -M intel ./target_binary | grep -A 30 "<main>:"
```
### Umzugseinträge & GOT (`-R`)
Zeigen Sie die GOT-Verschiebungsadressen (Global Offset Table) für importierte Bibliotheksaufrufe an:
```bash
objdump -R ./target_binary
```
*Zeigt Adressziele für Funktionen wie `puts`, `printf` und `system`.*

---

## 🔍 nm & Strings: Symbolauflösung & Strings

### Auflisten entstörter Symbole mit `nm`
```bash
# List defined symbols only (ignore external imports)
nm --defined-only -C ./target_binary

# List undefined external symbols (functions resolved by dynamic linker)
nm -u -C ./target_binary
```
### Verschleierte Zeichenfolgen mit `strings` extrahieren
```bash
# Extract ASCII strings with minimum 8 characters
strings -a -n 8 ./target_binary

# Extract 16-bit Little Endian wide strings (UTF-16 e.g., Windows / Java)
strings -e l -a ./target_binary

# Print byte offset of each string in hex
strings -a -t x ./target_binary
```
---

## 🛡️ Gehärtete Kompilierungsreferenz

So erstellen Sie eine ELF-Binärdatei mit maximalen Sicherheitsmaßnahmen:
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

## ⚡ Kurzreferenztabelle

| Ziel | Befehl |
|-----------|---------|
| **Audit-Abhilfemaßnahmen** | `checksec --file=<bin>` |
| **Prüfung der laufenden PID** | `checksec --proc=<pid>` |
| **ELF-Header anzeigen** | `readelf -h <bin>` |
| **Stack NX-Flag anzeigen** | `readelf -l <bin> \| grep -A 1 GNU_STACK` |
| **Abschnitte auflisten (.got, .plt)**| `readelf -S <bin>` |
| **Dynamische Symbole anzeigen** | `readelf --dyn-syms <bin>` |
| **Intel-Demontage** | `objdump -d -M intel <bin>` |
| **GOT-Umzüge prüfen** | `objdump -R <bin>` |
| **Symbole auflisten (entstellt)** | `nm -C <bin>` |
| **UTF-16-Strings extrahieren** | `strings -e l -a <bin>` |
| **String-Offsets anzeigen** | `strings -a -t x <bin>` |

---

## ⚠️ Rechtlicher Haftungsausschluss

> [!ACHTUNG]
> checksec und GNU Binutils sind Diagnose- und Reverse-Engineering-Tools für Software-Sicherheitsprüfungen, Verteidigungsvalidierungen und autorisierte Binärforschung. Bei der Überprüfung nicht autorisierter Software müssen die örtlichen Vorschriften zum Schutz des geistigen Eigentums eingehalten werden.