<!-- language-home-navigation -->
[🏠 Norsk Hovedside](../../README.nb.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇬🇧 English](../README.md)

---


# 🛡️ checksec & Binutils - Komplett jukseark

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
  <b>🛡️ Essensielle kommandolinjeverktøy for å revidere Linux kjørbare sikkerhetsreduksjoner (RELRO, Stack Canary, NX, PIE, Fortify) og dissekere ELF binære overskrifter, seksjoner og symboltabeller</b>
</p>

---

## 📋 Innholdsfortegnelse

- [Overview & Binary Mitigations Matrix](#-overview--binary-mitigations-matrix)
- [Installasjon](#-installasjon)
- [checksec: Rapid Mitigation Auditing](#-checksec-rapid-mitigation-auditing)
  - [Revisjon av enkelt- og batchbinære filer](#revisjon-single--batch-binære filer)
  - [Revisjon kjørende prosesser](#revisjon-løpende-prosesser)
  - [JSON & CSV-utdata](#json--csv-utdata)
- [readelf: ELF Header & Section Dissection](#-readelf-elf-header--section-dissection)
  - [ELF File Header (`-h`)](#elf-file-header--h)
  - [Program Headers & Segments (`-l`)](#program-headers--segments--l)
  - [Seksjonsoverskrifter (`-S`)](#seksjonsoverskrifter--s)
  - [Symboltabeller (`-s` / `--syms`)](#symbol-tables--s----syms)
  - [Dynamiske avhengigheter og biblioteker (`-d`)](#dynamic-dependencies--libraries--d)
- [objdump: Disassembly & Relocations](#-objdump-disassembly--relocations)
  - [Intel-demontering (`-d -M intel`)](#intel-disassembly--d--m-intel)
  - [Relocation Entries & GOT (`-R`)](#relocation-entries--got--r)
- [nm & strings: Symbol Resolution & Strings](#-nm--strings-symbol-resolution--strings)
  - [Listing Deangled Symbols with `nm`](#listing-deangled-symbols-with-nm)
  - [Ekstrahere obfuscated-strenger med `strings`](#extracting-obfuscated-strings-with-strings)
- [Hardened Compilation Reference](#-hardened-compilation-reference)
- [Hurtigreferansetabell](#-hurtigreferansetabell)
– [Juridisk ansvarsfraskrivelse](#-juridisk ansvarsfraskrivelse)

---

## 🎯 Oversikt og binær reduksjonsmatrise

Før du starter en arbeidsflyt for omvendt utvikling eller utnyttelse, er det obligatorisk å forstå hvilke sikkerhetsbegrensninger som er aktivert:

| Redusering | Fullt navn | Beskyttelsesmekanisme | Bypass-strategi |
|------------|--------|----------------|----------------|
| **RELRO** | Flytting skrivebeskyttet | Gjør `.got` skrivebeskyttet (`Full RELRO`) for å hindre pekeroverskriving | Mål haugstrukturer, returadresser eller funksjonskroker |
| **Kanari** | Stabelbeskytter | Plasserer randomisert informasjonskapsel før returpekeren; kontrollerer integritet ved retur | Lekk kanarieverdi via minnelekkasje, eller brute-force (forking daemons) |
| **NX** | No-Execute / DEP | Markerer stabel-, heap- og datasegmenter som ikke-kjørbare | Returorientert programmering (ROP) eller Jump-Oriented Programmering (JOP) |
| **PIE** | Stilling uavhengig kjørbar | Laster binær baseadresse på tilfeldig plassering i virtuelt minne (ASLR) | Lekkasjekodepeker for å beregne baseadresseforskyvning |
| **Befeste** | `_FORTIFY_SOURCE` | Erstatter usikre strengfunksjoner (`strcpy` -> `__strcpy_chk`) med grensekontroll | Ikke-overflyt logikkfeil, formatstreng uten bufferoverflyt |

---

## 📥 Installasjon
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y checksec binutils

# Arch Linux
sudo pacman -S checksec binutils

# Python pip (standalone checksec)
pip install checksec.py
```
---

## ⚡ checksec: Rapid Mitigation Auditing

### Revisjon av enkelt- og batchbinære filer
```bash
# Audit a single executable
checksec --file=./target_binary

# Audit all binaries in directory
checksec --dir=/usr/bin/
```
*Standard Output Eksempel:*
```text
RELRO           STACK CANARY      NX            PIE             RPATH      RUNPATH      FILE
Full RELRO      Canary found      NX enabled    PIE enabled     No RPATH   No RUNPATH   ./target_binary
```
### Revisjon av løpende prosesser
Overvåk minnebegrensninger og ASLR-status for en aktiv demon:
```bash
# Audit by Process ID (PID)
checksec --proc=1337

# Audit process by name
checksec --proc-all
```
### JSON- og CSV-utdata
Eksporter funn for automatisert analyse:
```bash
checksec --file=./target_binary --output=json > security.json
checksec --file=./target_binary --output=csv > security.csv
```
---

## 🔬 readelf: ELF Header & Section Dissection

`readelf`-verktøyet viser informasjon fra ELF-filer uten å påkalle noen kjøring:

### ELF-filhode (`-h`)
Avslører CPU-arkitektur, 32 vs 64-bits klasse, inngangspunktadresse og endianness:
```bash
readelf -h ./target_binary
```
### Programoverskrifter og segmenter (`-l`)
Viser segmenter lastet inn i minnet ved kjøretid og flagg (`R`, `W`, `E`). Avgjørende for å identifisere om stabelen (`GNU_STACK`) har utførelsestillatelser:
```bash
readelf -l ./target_binary
```
*(Hvis `GNU_STACK` har `RWE`, er stackkjøring tillatt; NX er deaktivert).*

### Seksjonsoverskrifter (`-S`)
Inspiser minneoppsett, adresser og forskyvninger for `.text` (kode), `.data` (initialiserte data), `.rodata` (konstanter), `.bss` (uinitialiserte data), `.plt` og `.got`:
```bash
readelf -S ./target_binary
```
### Symboltabeller (`-s` / `--syms`)
Liste alle eksporterte funksjoner, globale variabler og importerte libc-symboler:
```bash
# List all symbols
readelf -s ./target_binary

# Filter dynamic symbols used at runtime
readelf --dyn-syms ./target_binary
```
### Dynamiske avhengigheter og biblioteker (`-d`)
Viser nødvendige delte biblioteker (`NEEDED`-tagger, f.eks. `libc.so.6`, `libssl.so`):
```bash
readelf -d ./target_binary
```
---

## 🧩 objdump: Demontering og flytting

### Intel-demontering (`-d -M intel`)
Demonter kjørbare seksjoner til en ren Intel-syntakssamling:
```bash
# Disassemble .text section in Intel syntax
objdump -d -M intel ./target_binary

# Disassemble specific function (e.g. main)
objdump -d -M intel ./target_binary | grep -A 30 "<main>:"
```
### Flytteoppføringer & GOT (`-R`)
Vis flyttingsadressene for Global Offset Table (GOT) for importerte biblioteksamtaler:
```bash
objdump -R ./target_binary
```
*Viser adressemål for funksjoner som `puts`, `printf` og `system`.*

---

## 🔍 nm og strenger: Symboloppløsning og strenger

### Viser demonterte symboler med `nm`
```bash
# List defined symbols only (ignore external imports)
nm --defined-only -C ./target_binary

# List undefined external symbols (functions resolved by dynamic linker)
nm -u -C ./target_binary
```
### Trekker ut obfuskerte strenger med `strings`
```bash
# Extract ASCII strings with minimum 8 characters
strings -a -n 8 ./target_binary

# Extract 16-bit Little Endian wide strings (UTF-16 e.g., Windows / Java)
strings -e l -a ./target_binary

# Print byte offset of each string in hex
strings -a -t x ./target_binary
```
---

## 🛡️ Hardened Compilation Reference

Slik bygger du en ELF-binær med maksimale sikkerhetsbegrensninger:
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

## ⚡ Hurtigreferansetabell

| Mål | Kommando |
|----------------|--------|
| **Revisjonsbegrensninger** | `checksec --file=<bin>` |
| **Revisjon kjører PID** | `checksec --proc=<pid>` |
| **Se ELF Header** | `readelf -h <bin>` |
| **Se Stack NX-flagg** | `readelf -l <bin> \| grep -A 1 GNU_STACK` |
| **Listeseksjoner (.got, .plt)**| `readelf -S <bin>` |
| **Se dynamiske symboler** | `readelf --dyn-syms <bin>` |
| **Intel-demontering** | `objdump -d -M intel <bin>` |
| **Inspiser GOT-flyttinger** | `objdump -R <bin>` |
| **Liste symboler (demanglet)** | `nm -C <bin>` |
| ** Trekk ut UTF-16-strenger** | `strings -e l -a <bin>` |
| **Vis strengforskyvninger** | `strings -a -t x <bin>` |

---

## ⚠️ Juridisk ansvarsfraskrivelse

> [!FORSIKTIG]
> checksec og GNU Binutils er diagnostiske og omvendt utviklingsverktøy beregnet for programvaresikkerhetsrevisjon, forsvarsvalidering og autorisert binær forskning. Inspeksjon av uautorisert programvare må følge lokale forskrifter for intellektuell eiendom.