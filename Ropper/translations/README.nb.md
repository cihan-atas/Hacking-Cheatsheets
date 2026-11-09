<!-- language-home-navigation -->
[🏠 Norsk Hovedside](../../README.nb.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇬🇧 English](../README.md)

---


# 🧩 Ropper & ROPgadget - komplett jukseark

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
  <b>🧩 Viktige kommandolinjeverktøy for å oppdage returorientert programmering (ROP) og hopporientert programmering (JOP) gadgets for å omgå ikke-kjørbare (NX / DEP) minnebeskyttelser</b>
</p>

---

## 📋 Innholdsfortegnelse

- [Oversikt og ROP-teori](#-oversikt--rop-teori)
- [Installasjon](#-installasjon)
– [Basic Gadget Discovery](#-basic-gadget-discovery)
  - [Extracting All Gadgets](#extracting-all-gadgets)
  - [Limiting Instruction Depth](#limiting-instruction-depth)
- [Avansert søk og mønsterfiltrering](#-avansert-søk--mønsterfiltrering)
  - [Målrettingsspesifikke registre (`rdi`, `rsi`, `rdx`, `rax`)](#targeting-specific-registers-rdi-rsi-rdx-rax)
  - [Memory Write Gadgets (Write-What-Where)](#memory-write-gadgets-write-what-where)
  - [Syscall & Interrupt Gadgets](#syscall--avbryt-gadgets)
- [Ekskluderer dårlige tegn (`--badbytes`)](#-ekskludert-dårlige tegn---badbyte)
- [Automatisert ROP-kjedegenerering](#-automated-rop-chain-generation)
  - [Ropper Automated Chains](#ropper-automated-chains)
  - [ROPgadget Automated Chains](#ropgadget-automated-chains)
- [Analyzing Loaded Libraries (libc ROP Chains)](#-analyzing-loaded-libraries-libc-rop-chains)
- [Hurtigreferansetabell](#-hurtigreferansetabell)
– [Defensive Considerations & Mitigations](#-defensive-considerations--mitigations)
– [Juridisk ansvarsfraskrivelse](#-juridisk ansvarsfraskrivelse)

---

## 🎯 Oversikt og ROP-teori

Moderne operativsystemer håndhever **NX (No-Execute) / DEP (Data Execution Prevention)**, som forhindrer direkte kjøring av shellcode plassert på stabelen eller heapen.

**Returorientert programmering (ROP)** omgår denne begrensningen ved å gjenbruke små eksisterende maskininstruksjoner som allerede finnes i kjørbart minne (`.text`-delen av binære eller delte biblioteker som `libc.so`). Hver instruksjonssekvens som slutter på en `ret`-instruksjon kalles en **gadget**. Ved å ordne adresser på stabelen, lenker angripere gadgets sammen for å:
1. Last inn argumenter i CPU-registre (f.eks. `pop rdi; ret` for x64-oppkallingskonvensjoner).
2. Skriv strenger (som `/bin/sh`) i skrivbare `.bss`- eller `.data`-seksjoner.
3. Utløs `execve("/bin/sh", NULL, NULL)` eller påkall `system()`.

---

## 📥 Installasjon
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

## 🚀 Grunnleggende Gadget Discovery

### Trekker ut alle gadgets
```bash
# Using Ropper (Faster multi-threaded engine)
ropper --file ./target_binary --nocolor

# Using ROPgadget
ROPgadget --binary ./target_binary
```
### Begrensende instruksjonsdybde
Reduser støyende lange kjeder ved å begrense gadgetlengden (antall instruksjoner før `ret`):
```bash
# Limit to maximum 3 instructions before return
ropper --file ./target_binary --inst-count 3
ROPgadget --binary ./target_binary --depth 3
```
---

## 🔍 Avansert søk og mønsterfiltrering

### Målrettingsspesifikke registre (`rdi`, `rsi`, `rdx`, `rax`)
I 64-biters Linux-anropskonvensjon må de tre første argumentene til en funksjon eller syskala ligge i `rdi`, `rsi` og `rdx`:
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
Bruke ROPgadget regex-samsvar:
```bash
ROPgadget --binary ./target_binary --only "pop|ret" | grep -E "rdi|rsi|rdx|rax"
```
### Memory Write Gadgets (Write-What-Where)
Når du trenger å lagre `/bin/sh` eller vilkårlige argumenter i minnet:
```bash
# Find instructions moving registers into pointer memory
ropper --file ./target_binary --search "mov [???], ???"
```
*Eksempel samsvar:* `mov qword ptr [rdi], rsi; ret` tillater å skrive det som er i `rsi` til adressen pekt på av `rdi`.

### Syscall og avbryt gadgets
```bash
# Find raw syscall instructions
ropper --file ./target_binary --search "syscall"

# x86 32-bit software interrupt
ropper --file ./target_binary --search "int 0x80"
```
---

## 🚫 Ekskluderer dårlige tegn (`--badbytes`)

Svakheter i strengformatering eller funksjoner som `strcpy()` avsluttes på nullbyte (`\x00`), nylinjer (`\x0a`) eller vognretur (`\x0d`). Ekskluder adresser som inneholder disse bytene:
```bash
# Filter out gadgets whose addresses contain bad characters
ropper --file ./target_binary --badbytes 000a0d --search "pop rdi"
ROPgadget --binary ./target_binary --badbytes "00|0a|0d"
```
---

## ⚡ Automatisert ROP-kjedegenerering

Begge verktøyene kan automatisk bygge funksjonelle Python-utnyttelsesmaler som påkaller `execve("/bin/sh")`:

### Ropper Automated Chains
```bash
ropper --file ./target_binary --chain "execve"
```
### ROPgadget automatiserte kjeder
```bash
ROPgadget --binary ./target_binary --ropchain
```
*Dette produserer et Python nyttelastskjelett som er klart til bruk `mprotect()` eller `execve()`.*

---

## 📚 Analyse av lastede biblioteker (libc ROP-kjeder)

Når selve binærfilen er kompilert som **PIE** eller inneholder få nyttige gadgets, trekk ut gadgets direkte fra målsystemets delte C-bibliotek (`libc.so.6`):
```bash
# Search gadgets in libc
ropper --file /lib/x86_64-linux-gnu/libc.so.6 --search "pop rdi; ret"

# Find "/bin/sh" string offset inside libc
strings -a -t x /lib/x86_64-linux-gnu/libc.so.6 | grep "/bin/sh"
```
*(Legg til oppdagede forskyvninger til den lekkede baseadressen til libc ved kjøretid).*

---

## ⚡ Hurtigreferansetabell

| Mål | Ropper Command | ROPgadget-kommando |
|-----------|----------------|------------------------|
| **Dump alle gadgets** | `ropper --file <bin>` | `ROPgadget --binary <bin>` |
| **Søkespesifikk gadget** | `ropper --file <bin> --search "pop rdi"` | `ROPgadget --binary <bin> \| grep "pop rdi"` |
| **Filtrer dårlige bytes** | `ropper --file <bin> --badbytes 000a` | `ROPgadget --binary <bin> --badbytes "00\|0a"` |
| **Begrens dybde/lengde** | `ropper --file <bin> --inst-count 3` | `ROPgadget --binary <bin> --depth 3` |
| **Søk JOP Gadgets** | `ropper --file <bin> --type jop` | `ROPgadget --binary <bin> --norop` |
| **Auto Build ROP-kjede** | `ropper --file <bin> --chain "execve"`| `ROPgadget --binary <bin> --ropchain` |
| **Søk minneskriver** | `ropper --file <bin> --search "mov [???], ???"` | `ROPgadget --binary <bin> \| grep "mov \["` |

---

## 🛡️ Defensive vurderinger og begrensninger

1. **Kontrollflyt-håndhevelsesteknologi (Intel CET / Shadow Stack):** Moderne prosessorer opprettholder en isolert maskinvare-skyggestabel som verifiserer returadresser på `ret`, og avslutter ROP-kjeder umiddelbart.
2. **Klang / GCC Control Flow Integrity (CFI):** Validerer funksjonspekermål mot en foroverkantgraf før indirekte anrop.

---

## ⚠️ Juridisk ansvarsfraskrivelse

> [!FORSIKTIG]
> Ropper og ROPgadget er verktøy for utvikling av utnyttelse og evaluering av programvaresikkerhet. De må strengt tatt brukes i autoriserte penetrasjonstester, CTF-utfordringer eller defensiv sikkerhetsforskning. Å utnytte programvare uten eksplisitt autorisasjon er ulovlig.