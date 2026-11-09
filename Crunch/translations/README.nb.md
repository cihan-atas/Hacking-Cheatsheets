<!-- language-home-navigation -->
[🏠 Norsk Hovedside](../../README.nb.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇬🇧 English](../README.md)

---


# 🔢 Crunch - Komplett jukseark

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
   ██████╗██████╗ ██╗   ██╗███╗   ██╗ ██████╗██╗  ██╗
  ██╔════╝██╔══██╗██║   ██║████╗  ██║██╔════╝██║  ██║
  ██║     ██████╔╝██║   ██║██╔██╗ ██║██║     ███████║
  ██║     ██╔══██╗██║   ██║██║╚██╗██║██║     ██╔══██║
  ╚██████╗██║  ██║╚██████╔╝██║ ╚████║╚██████╗██║  ██║
   ╚═════╝╚═╝  ╚═╝ ╚═════╝ ╚═╝  ╚═══╝ ╚═════╝╚═╝  ╚═╝
       High-Speed Pattern-Based Wordlist & Character Matrix Generator
```
<p align="center">
  <img src="https://img.shields.io/badge/Crunch-Wordlist_Generator-blue?style=for-the-badge" alt="Crunch">
  <img src="https://img.shields.io/badge/Language-C-orange?style=for-the-badge" alt="Språk C">
  <img src="https://img.shields.io/badge/Category-Password_Cracking-green?style=for-the-badge" alt="Password Cracking">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="License GPLv2">
</p>

<p align="center">
  <b>🌐 Ordlistegenerator som genererer alle mulige kombinasjoner av tegnsett ved hjelp av egendefinerte lengder, mønstre og symboler</b>
</p>

---

## 📋 Innholdsfortegnelse

- [Oversikt og permutasjonsmotoren](#-oversikt--permutasjonsmotoren)
- [Installasjon](#-installasjon)
- [Grunnleggende syntaks og tegnsett](#-grunnsyntaks--tegnsett)
- [Pattern Matching & Placeholders (`-t`)](#-pattern-matching--placeholders--t)
- [Using System Charsets (`charset.lst`)](#-using-system-charsets-charsetlst)
- [Duplicate Character Limiting (`-d`)](#-duplicate-character-limiting--d)
- [Splitting Wordlists into Chunks (`-b` & `-c`)](#-splitting-wordlists-into-chunks--b---c)
- [Piping direkte inn i angrepsverktøy (null diskplass)](#-piping-direkte-inn i-attack-tools-null-diskplass)
- [Hurtigreferansetabell](#-hurtigreferansetabell)
- [Defensive Betraktninger & Calculation Warnings](#-defensive-considerations--calculation-advarsler)
– [Juridisk ansvarsfraskrivelse](#-juridisk ansvarsfraskrivelse)

---

## 🎯 Oversikt og permutasjonsmotoren

Når du angriper kjente passordmønstre (f.eks. bedriftens PIN-koder, telefonnumre eller passord som samsvarer med standard kompleksitetsregler som `Word2026!`), mangler standard statiske ordbøker som `rockyou.txt` enten målet eller er ineffektive.

Skrevet i C av **bofheaded**, er **Crunch** en lynrask ordlistegenerator. Den beregner permutasjoner matematisk, slik at du kan definere nøyaktige tegnposisjoner, lengder, tegnpooler og dupliseringsbegrensninger.

---

## 📥 Installasjon
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y crunch

# Arch Linux (AUR)
yay -S crunch
```
---

## 🚀 Grunnleggende syntaks og tegnsett

Kjernesyntaksen krever minimum lengde, maksimal lengde, og eventuelt et alfabet:
```bash
# crunch <min> <max> [characterset] [options]

# Generate all 4-digit numeric PINs (0000 - 9999)
crunch 4 4 0123456789 -o 4digit_pins.txt

# Generate 5-character words using specific custom letters
crunch 5 5 abcdef -o custom_words.txt

# Generate lowercase alphabet combinations from length 3 to 4
crunch 3 4 abcdefghijklmnopqrstuvwxyz -o alpha_3_4.txt
```
---

## 🎭 Mønstertilpasning og plassholdere (`-t`)

`-t`-flagget gjør det mulig å fikse kjente tegn mens de ukjente sporene fuzzes med spesielle plassholdere:

| Plassholder | Tegntype |
|-------------|----------------|
| `@` | Små bokstaver (`a-z`) |
| `,` | Store bokstaver (`A-Z`) |
| `%` | Tall (`0-9`) |
| `^` | Spesialsymboler / tegnsetting (`!@#$%^&*...`) |

### Praktiske mønstereksempler:
```bash
# Generate Turkish phone numbers (e.g., 0532XXXXXXX)
crunch 11 11 -t 0532%%%%%%% -o phone_numbers.txt

# Generate passwords conforming to: Capital letter + 4 lowercase + 2 digits + 1 symbol
# e.g., Admin24! or Super99#
crunch 8 8 -t ,@@@@%%^ -o policy_passwords.txt

# Target known prefix: Spring2026! or Spring2026#
crunch 11 11 -t Spring2026^ -o spring_passwords.txt
```
---

## 📚 Bruke systemtegnsett (`charset.lst`)

Crunch pakker forhåndskonfigurerte tegnmatriser i `/usr/share/crunch/charset.lst`:
```bash
# Use pre-defined alphanumeric set (mixalpha-numeric)
crunch 6 6 -f /usr/share/crunch/charset.lst mixalpha-numeric -o out.txt

# Use hexadecimal lowercase characters (0-9, a-f)
crunch 8 8 -f /usr/share/crunch/charset.lst hex-lower -o hex_keys.txt
```
---

## 🚫 Duplikattegnbegrensning (`-d`)

Håndheve grenser for påfølgende dupliserte tegn for å luke ut usannsynlige passord:
```bash
# Generate 8-character lowercase words with at most 2 consecutive identical letters
# (e.g., prevents 'aaabbb' while allowing 'aab')
crunch 8 8 abcdefghijklmnopqrstuvwxyz -d 2@ -o deduped.txt

# Limit consecutive numbers to at most 1 (prevents '11' or '22')
crunch 6 6 -t %%%%%% -d 1% -o no_repeated_digits.txt
```
---

## 📦 Deler opp ordlister i biter (`-b` & `-c`)

Hindre at filer med flere terabyte tar opp diskplass ved å dele dem opp i håndterbare volumer:
```bash
# Split wordlist into 500MB chunks
crunch 8 8 -f /usr/share/crunch/charset.lst mixalpha-numeric -b 500mb -o START

# Split by line count (every 1,000,000 lines)
crunch 8 8 0123456789 -c 1000000 -o START
```
---

## ⚡ Piping direkte inn i angrepsverktøy (null diskplass)

Skriv aldri gigantiske ordlister til disken når du kan streame permutasjoner direkte inn i crackingverktøy:
```bash
# Stream 8-digit numeric PINs straight into Aircrack-ng
crunch 8 8 0123456789 | aircrack-ng -w - -b 00:11:22:33:44:55 capture.cap

# Stream policy passwords directly into Hydra SSH brute-force
crunch 8 8 -t Admin%%^ | hydra -l admin -P - 10.10.10.50 ssh

# Stream into John the Ripper
crunch 6 6 0123456789 | john --stdin --format=raw-md5 hashes.txt
```
---

## ⚡ Hurtigreferansetabell

| Mål | Kommando |
|----------------|--------|
| **Numeriske PIN-koder (4-sifret)** | `crunch 4 4 0123456789 -o pins.txt` |
| **Mønster: Store + ord + tall**| `crunch 8 8 -t ,@@@@%%^ -o passwords.txt` |
| **Mønster: Spesifikt prefiks** | `crunch 10 10 -t Company%%% -o wordlist.txt` |
| **Bruk tegnsettfil** | `crunch 6 6 -f charset.lst mixalpha-numeric -o out.txt` |
| **Begrens fortløpende identisk**| `crunch 8 8 -t @@@@@@@@ -d 2@ -o out.txt` |
| **Deles opp i 1 GB filer** | `crunch 8 8 <CHARS> -b 1gb -o START` |
| **Rør til Aircrack-ng** | `crunch 8 8 0123456789 \| aircrack-ng -w - target.cap` |
| **Rør til Hydra** | `crunch 7 7 -t User%%% \| hydra -l user -P - <IP> ssh` |

---

## 🛡️ Defensive betraktninger og beregningsadvarsler

> [!ADVARSEL]
> **Permutasjonseksplosjoner:** Sjekk alltid Crunchs projiserte utdatastørrelse før du bekrefter diskgenerering. En ubegrenset 8-tegns alfanumerisk ordliste produserer **over 200 terabyte** med tekstdata! Rørutgang når det er mulig.

---

## ⚠️ Juridisk ansvarsfraskrivelse

> [!FORSIKTIG]
> Crunch er kun beregnet for autorisert passordgjenoppretting, penetrasjonstesting og sikkerhetsrevisjon. Brute-tvingende passord på uautoriserte systemer er strengt ulovlig.