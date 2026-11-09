<!-- language-home-navigation -->
[🏠 Norsk Hovedside](../../README.nb.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇬🇧 English](../README.md)

---


# fcrackzip Jukseark

[fcrackzip](https://github.com/hyc/fcrackzip) er en rask flertråds kommandolinjepassordknekker for krypterte ZIP-arkiver. Den støtter både ordbokbaserte angrep og optimaliserte brute-force-angrep mot tradisjonell ZipCrypto (PKZIP-strømchiffer) og kan verifisere arkivintegritet uten full utvinning.

---

## Innholdsfortegnelse

– [Kjernefunksjoner og begrensninger](#kjernefunksjoner--begrensninger)
- [Installasjon](#installation)
- [Ordbokangrep (`-D -p`)](#dictionary-attacks--d--p)
- [Brute-Force Attacks (`-b`)](#brute-force-attacks--b)
– [Tegnsett og lengdespesifikasjon](#tegnsett--lengdespesifikasjon)
– [Referanse- og ytelsesoptimalisering](#benchmark--ytelsesoptimalisering)
- [ZipCrypto vs. AES-256 (John/Hashcat-alternativ)](#zipcrypto-vs-aes-256-johnhashcat-alternative)

---

## Kjernefunksjoner og begrensninger

- **Ultra-Fast ZipCrypto Cracker:** Skrevet i C, optimert for eldre ZipCrypto-algoritme (standard PKZIP-kryptering).
- **Ordbokmodus:** Leser kandidatpassord sekvensielt fra ordlister.
- **Brute-Force-modus:** Genererer kandidater på farten ved hjelp av konfigurerbare tegnsett og lengdegrenser.
- **Archive Unzip Test (`-u`):** Verifiserer kandidatpassord ved å forsøke dekompresjon, og eliminerer praktisk talt falske positiver.
- **Begrensning:** Støtter **ikke** moderne WinZip AES-256-kryptering. For AES-256 ZIP-er, bruk `zip2john` + John the Ripper eller Hashcat (modus 13600).

---

## Installasjon
```bash
# Debian / Ubuntu / Kali Linux
sudo apt-get update && sudo apt-get install -y fcrackzip

# Arch Linux / BlackArch
sudo pacman -S fcrackzip

# Fedora / CentOS / RHEL
sudo dnf install -y fcrackzip

# Verify command
fcrackzip -h
```
---

## Ordbokangrep (`-D -p`)

Den raskeste måten å teste mot vanlige ordlister:

### 1. Grunnleggende ordbokangrep med unzip-verifisering
```bash
fcrackzip -u -D -p /usr/share/wordlists/rockyou.txt secret_archive.zip
```
- `-u`: Unzip-modus (verifiserer den utpakkede filen CRC for å unngå falske positiver).
- `-D`: Ordbokangrepsmodus.
- `-p <file>`: Bane til ordbokfilen.

### 2. Utførlig utgangsmodus
Vis testet hastighet, gjeldende passord og feilsøkingsinformasjon:
```bash
fcrackzip -v -u -D -p passwords.txt backup.zip
```
---

## Brute-Force Attacks (`-b`)

Når ordbokangrep mislykkes, kan du bruke brute-force mot ukjente korte passord:

### 1. Brute-Force numeriske PIN-koder (4 til 6 sifre)
```bash
fcrackzip -u -b -c 1 -l 4-6 secure.zip
```
- `-b`: Brute-force-modus.
- `-c 1`: Tegnsett: kun sifre (`0-9`).
- `-l 4-6`: Lengdeområde: min 4, maks 6 tegn.

### 2. Alfanumeriske tegn med små bokstaver (1 til 5 tegn)
```bash
fcrackzip -u -b -c a1 -l 1-5 archive.zip
```
- `-c a1`: Små bokstaver (`a-z`) og sifre (`0-9`).

### 3. Små bokstaver + Store bokstaver + Siffer + Symboler
```bash
fcrackzip -u -b -c aA1! -l 6-8 protected.zip
```
- `-c a`: Små bokstaver (`a-z`).
- `-c A`: Store bokstaver (`A-Z`).
- `-c 1`: Tall (`0-9`).
- `-c !`: Spesialsymboler (`[!:$%&/()=?{[]}+*~#]`).

---

## Tegnsett og lengdespesifikasjon

| Flagg | Tegnsett inkludert | Eksempelkandidater |
|---|---|---|
| `-c a` | Små bokstaver (`a-z`) | `admin`, `secret` |
| `-c A` | Store bokstaver (`A-Z`) | `SECRET`, `ROOT` |
| `-c 1` | Sifre (`0-9`) | `1234`, `2024` |
| `-c !` | Spesielle tegnsettingstegn | `!@#$` |
| `-c aA1` | Kombinerte alfanumeriske tegn | `Admin123` |
| `-p <prefix>` | Første startfrøstreng | Nyttig for å gjenoppta brute force |

Angi lengdegrenser:
```bash
# Minimum length 6, Maximum length 8
-l 6-8

# Exact length 8
-l 8-8
```
---

## Benchmark og ytelsesoptimalisering

Test CPU-gjennomstrømning på maskinen din:
```bash
fcrackzip -B
```
---

## ZipCrypto vs. AES-256 (John/Hashcat-alternativ)

Hvis `fcrackzip` returnerer `not a zipfile, or unsupported encryption type`, krypteres arkivet med AES-256 (WinZip AES). Bruk `zip2john`:
```bash
# 1. Extract hash using zip2john
zip2john aes_protected.zip > zip.hash

# 2. Crack using John the Ripper
john --wordlist=/usr/share/wordlists/rockyou.txt zip.hash

# 3. Or crack using Hashcat (Mode 13600: WinZip)
cat zip.hash | cut -d ':' -f 2 > hashcat_zip.hash
hashcat -m 13600 hashcat_zip.hash /usr/share/wordlists/rockyou.txt
```