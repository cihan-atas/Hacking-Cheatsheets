<!-- language-home-navigation -->
[🏠 Norsk Hovedside](../../README.nb.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇬🇧 English](../README.md)

---


# 🕷️ CeWL - Komplett jukseark

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
   ██████╗███████╗██╗    ██╗██╗     
  ██╔════╝██╔════╝██║    ██║██║     
  ██║     █████╗  ██║ █╗ ██║██║     
  ██║     ██╔══╝  ██║███╗██║██║     
  ╚██████╗███████╗╚███╔███╔╝███████╗
   ╚═════╝╚══════╝ ╚══╝╚══╝ ╚══════╝
       Custom Word List Generator by Robin Wood (DigiNinja)
```
<p align="center">
  <img src="https://img.shields.io/badge/CeWL-Custom_Wordlist_Generator-blue?style=for-the-badge" alt="CeWL">
  <img src="https://img.shields.io/badge/Author-DigiNinja-orange?style=for-the-badge" alt="DigiNinja">
  <img src="https://img.shields.io/badge/Category-Password_Cracking-green?style=for-the-badge" alt="Password Cracking">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="License GPLv2">
</p>

<p align="center">
  <b>🌐 Målspesifikk passordordbokgenerator som edderkopper nettsteder for å høste spesialisert bedriftsterminologi, merkenavn og ansattes e-poster</b>
</p>

---

## 📋 Innholdsfortegnelse

- [Overview & The Targeted Password Philosophy](#-overview--the-targeted-password-philosophy)
- [Installasjon](#-installasjon)
- [Grunnleggende gjennomsøking av nettsted](#-grunnleggende gjennomsøking av nettsted)
- [Spider Depth & Word Length Controls](#-edderkoppdybde--ord-lengde-kontroller)
- [Harvesting Email Addresses (`-e`)](#-harvesting-email-adresser--e)
- [Inkludert tall og små bokstaver](#-inkludert-tall--små bokstaver)
- [Document Metadata Extraction (`-a`)](#-document-metadata-extraction--a)
- [Ordtelling og frekvensprofilering (`-c`)](#-ordtelling--frekvensprofilering--c)
- [kjede CeWL med Hashcat & John Rules](#-chaining-cewl-with-hashcat--john-rules)
- [Hurtigreferansetabell](#-hurtigreferansetabell)
– [Defensive Considerations & OPSEC](#-defensive-considerations--opsec)
– [Juridisk ansvarsfraskrivelse](#-juridisk ansvarsfraskrivelse)

---

## 🎯 Oversikt og den målrettede passordfilosofien

Generiske ordlister som `rockyou.txt` er effektive for forbrukerpassord, men **de mislykkes ofte i forhold til bedriftens passordpolicy** som krever 12+ tegn, spesialsymboler og sesongrotasjoner.

Ansatte baserer rutinemessig bedriftspassordene sine på:
- Bedriftsprosjektnavn, interne kodenavn og slagord (`Pegasus2026!`).
– Spesifikk bransjeterminologi, teknisk sjargong eller produktmerker.
- Ledende navn, landemerker og kontoradresser.

Laget av **Robin Wood (DigiNinja)**, **CeWL** (Custom Word List generator) er en Ruby-edderkopp som gjennomsøker en spesifisert mål-URL til en definert dybde, trekker ut unike ord som samsvarer med minimumslengdekravene, og bygger en engasjementstilpasset passordordbok.

---

## 📥 Installasjon
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y cewl

# Arch Linux (AUR)
yay -S cewl

# Ruby Gem
gem install cewl
```
---

## 🚀 Grunnleggende gjennomgang av nettsted
```bash
# Crawl website and output words to console
cewl https://target.local

# Crawl and save to a custom wordlist file
cewl https://target.local -w company_wordlist.txt
```
---

## 🕸️ Kontroller for edderkoppdybde og ordlengde

Kontroller hvor dypt CeWL følger koblinger og hvilke ordstørrelser det fanger:
```bash
# Crawl 2 links deep (-d 2) and enforce minimum word length of 8 characters (-m 8)
cewl https://target.local -d 2 -m 8 -w wordlist_min8.txt

# Aggressive 3-level deep crawl for comprehensive corporate terminology
cewl https://target.local -d 3 -m 7 --no-words -w deep_crawl.txt
```
---

## 📧 Innhøsting av e-postadresser (`-e`)

Trekk ut bedriftens e-postadresser for påfølgende phishing-simuleringer eller spraying av legitimasjon:
```bash
# Extract emails to a separate dedicated file
cewl https://target.local -e --email_file emails.txt -w words.txt
```
---

## 🔡 Inkludert tall og små bokstaver
```bash
# Include words containing alphanumeric combinations (e.g., product names like V300, X86)
cewl https://target.local --with-numbers -m 6 -w words_with_numbers.txt

# Convert all discovered words to lowercase (great for rule generation)
cewl https://target.local --lowercase -w lowercase_words.txt
```
---

## 📄 Uttrekk av dokumentmetadata (`-a`)

Trekk ut forfatter- og skaperbrukernavn fra nedlastede PDF- og Microsoft Office-dokumenter:
```bash
# Download and analyze document metadata to extract employee usernames
cewl https://target.local -a --meta_file extracted_authors.txt
```
---

## 📊 Ordtelling og frekvensprofilering (`-c`)

Sorter ord etter frekvens for å identifisere de mest utbredte forretningsordene:
```bash
# Append occurrence count next to each word
cewl https://target.local -c -m 6 -w frequency.txt

# Sort by most frequent words in descending order
sort -k2 -nr frequency.txt | head -n 30
```
---

## ⚡ Kjede CeWL med Hashcat & John Rules

En rå CeWL-ordliste inneholder basisordbokrøtter. For å produsere levedyktige passord, utvide ordlisten ved å bruke **Hashcat / John the Ripper mutasjonsregler**:
```bash
# 1. Harvest base words from corporate website
cewl https://target.local -m 5 --lowercase -w base_words.txt

# 2. Mutate words using Hashcat's best64 rule set
hashcat --force base_words.txt -r /usr/share/hashcat/rules/best64.rule --stdout | sort -u > mutated_passwords.txt

# Example mutations produced:
# Acmetool -> Acmetool2026!, !Acmetool123, 4cmet00l, Acmetool#1
```
---

## ⚡ Hurtigreferansetabell

| Mål | Kommando |
|----------------|--------|
| **Grunnleggende gjennomgang til fil** | `cewl <URL> -w wordlist.txt` |
| **Angi min lengde og dybde** | `cewl <URL> -m 8 -d 2 -w wordlist.txt` |
| ** Trekk ut bedriftens e-poster**| `cewl <URL> -e --email_file emails.txt` |
| ** Trekk ut dokumentmetadata**| `cewl <URL> -a --meta_file authors.txt` |
| **Ta med alfanumeriske ord**| `cewl <URL> --with-numbers -w wordlist.txt` |
| **Små bokstaver** | `cewl <URL> --lowercase -w wordlist.txt` |
| **Telle ordfrekvenser** | `cewl <URL> -c -w frequencies.txt` |
| **Autentiser bak pålogging**| `cewl <URL> -a "Cookie: session=xyz"` |

---

## 🛡️ Defensive betraktninger og OPSEC

1. **Nettservertilgangslogger:** CeWL genererer hundrevis av raske HTTP GET-forespørsler, og produserer identifiserbare edderkopptilgangslogger. Bruk egendefinerte User-Agent-strenger (`-u "Mozilla/5.0..."`) for å blande seg inn.
2. **Honeyword-feller:** Defensive blå lag kan legge inn usynlige eller skjulte HTML-lenker som fører til kanariøyeinformasjon for å flagge automatiserte edderkopper.

---

## ⚠️ Juridisk ansvarsfraskrivelse

> [!FORSIKTIG]
> CeWL er kun utviklet for autoriserte sikkerhetsvurderinger, penetrasjonstesting og passordrevisjonsundersøkelser. Skraping eller spidering av uautoriserte systemer er strengt forbudt.