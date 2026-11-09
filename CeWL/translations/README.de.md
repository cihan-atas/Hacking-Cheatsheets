<!-- language-home-navigation -->
[🏠 Deutsche Startseite](../../README.de.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🕷️ CeWL – Vollständiger Cheatsheet

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
  <b>🌐 Zielspezifischer Passwort-Wörterbuch-Generator, der Websites durchsucht, um spezielle Unternehmensterminologie, Markennamen und Mitarbeiter-E-Mails zu sammeln</b>
</p>

---

## 📋 Inhaltsverzeichnis

- [Überblick und die Targeted-Password-Philosophie](#-overview--the-targeted-password-philosophy)
- [Installation](#-Installation)
- [Grundlegendes Website-Crawling](#-basic-website-crawling)
- [Spider-Tiefe und Wortlängenkontrollen](#-spider- Depth--word-length-controls)
- [E-Mail-Adressen sammeln (`-e`)](#-harvesting-email-addresses--e)
- [Einschließlich Zahlen und Kleinschreibung](#-inklusive-Zahlen--Kleinschreibung)
- [Dokument-Metadaten-Extraktion (`-a`)](#-document-metadata-extraction--a)
- [Wortanzahl und Häufigkeitsprofilierung (`-c`)](#-word-count--frequenz-profiling--c)
- [CeWL mit Hashcat & John Rules verketten](#-chaining-cewl-with-hashcat--john-rules)
- [Kurzreferenztabelle](#-quick-reference-table)
- [Verteidigungsüberlegungen und OPSEC](#-defensive-considerations--opsec)
- [Rechtlicher Haftungsausschluss](#-legal-disclaimer)

---

## 🎯 Übersicht und die Targeted-Password-Philosophie

Generische Wortlisten wie

Mitarbeiter stützen ihre Unternehmenskennwörter routinemäßig auf Folgendes:
- Unternehmensprojektnamen, interne Codenamen und Slogans (`Pegasus2026!`).
- Spezifische Branchenterminologie, Fachjargon oder Produktmarken.
- Namen von Führungskräften, Sehenswürdigkeiten und Büroadressen.

**CeWL** (Custom Word List Generator) wurde von **Robin Wood (DigiNinja)** entwickelt und ist ein Ruby-Spider, der eine bestimmte Ziel-URL bis zu einer definierten Tiefe crawlt, eindeutige Wörter extrahiert, die den Mindestlängenanforderungen entsprechen, und ein auf das Engagement zugeschnittenes Passwortwörterbuch erstellt.

---

## 📥 Installation
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y cewl

# Arch Linux (AUR)
yay -S cewl

# Ruby Gem
gem install cewl
```
---

## 🚀 Grundlegendes Website-Crawling
```bash
# Crawl website and output words to console
cewl https://target.local

# Crawl and save to a custom wordlist file
cewl https://target.local -w company_wordlist.txt
```
---

## 🕸️ Steuerelemente für Spinnentiefe und Wortlänge

Steuern Sie, wie genau CeWL Links folgt und welche Wortgrößen es erfasst:
```bash
# Crawl 2 links deep (-d 2) and enforce minimum word length of 8 characters (-m 8)
cewl https://target.local -d 2 -m 8 -w wordlist_min8.txt

# Aggressive 3-level deep crawl for comprehensive corporate terminology
cewl https://target.local -d 3 -m 7 --no-words -w deep_crawl.txt
```
---

## 📧 E-Mail-Adressen sammeln (`-e`)

Extrahieren Sie Unternehmens-E-Mail-Adressen für nachfolgende Phishing-Simulationen oder Credential-Spraying:
```bash
# Extract emails to a separate dedicated file
cewl https://target.local -e --email_file emails.txt -w words.txt
```
---

## 🔡 Einschließlich Zahlen und Kleinschreibung
```bash
# Include words containing alphanumeric combinations (e.g., product names like V300, X86)
cewl https://target.local --with-numbers -m 6 -w words_with_numbers.txt

# Convert all discovered words to lowercase (great for rule generation)
cewl https://target.local --lowercase -w lowercase_words.txt
```
---

## 📄 Dokument-Metadaten-Extraktion (`-a`)

Extrahieren Sie Benutzernamen von Autoren und Erstellern aus heruntergeladenen PDF- und Microsoft Office-Dokumenten:
```bash
# Download and analyze document metadata to extract employee usernames
cewl https://target.local -a --meta_file extracted_authors.txt
```
---

## 📊 Wortzählung und Häufigkeitsprofilierung (`-c`)

Sortieren Sie Wörter nach Häufigkeit, um die am weitesten verbreiteten Unternehmensschlagworte zu identifizieren:
```bash
# Append occurrence count next to each word
cewl https://target.local -c -m 6 -w frequency.txt

# Sort by most frequent words in descending order
sort -k2 -nr frequency.txt | head -n 30
```
---

## ⚡ Verkettung von CeWL mit Hashcat & John Rules

Eine rohe CeWL-Wortliste enthält Basiswörterbuchwurzeln. Um brauchbare Passwörter zu erstellen, erweitern Sie die Wortliste mithilfe der **Hashcat/John the Ripper-Mutationsregeln**:
```bash
# 1. Harvest base words from corporate website
cewl https://target.local -m 5 --lowercase -w base_words.txt

# 2. Mutate words using Hashcat's best64 rule set
hashcat --force base_words.txt -r /usr/share/hashcat/rules/best64.rule --stdout | sort -u > mutated_passwords.txt

# Example mutations produced:
# Acmetool -> Acmetool2026!, !Acmetool123, 4cmet00l, Acmetool#1
```
---

## ⚡ Kurzreferenztabelle

| Ziel | Befehl |
|-----------|---------|
| **Grundlegendes Crawlen in eine Datei** | `cewl <URL> -w wordlist.txt` |
| **Mindestlänge und -tiefe festlegen** | `cewl <URL> -m 8 -d 2 -w wordlist.txt` |
| **Unternehmens-E-Mails extrahieren**| `cewl <URL> -e --email_file emails.txt` |
| **Dokumentmetadaten extrahieren**| `cewl <URL> -a --meta_file authors.txt` |
| **Alphanumerische Wörter einbeziehen**| `cewl <URL> --with-numbers -w wordlist.txt` |
| **Kleinbuchstabenausgabe** | `cewl <URL> --lowercase -w wordlist.txt` |
| **Worthäufigkeiten zählen** | `cewl <URL> -c -w frequencies.txt` |
| **Authentifizierung hinter der Anmeldung**| `cewl <URL> -a "Cookie: session=xyz"` |

---

## 🛡️ Defensive Überlegungen und OPSEC

1. **Webserver-Zugriffsprotokolle:** CeWL generiert Hunderte von schnellen HTTP-GET-Anfragen und erstellt identifizierbare Spider-Zugriffsprotokolle. Verwenden Sie zum Einbinden benutzerdefinierte User-Agent-Zeichenfolgen (`-u "Mozilla/5.0..."`).
2. **Honeyword-Fallen:** Defensive Blue-Teams können unsichtbare oder versteckte HTML-Links einbetten, die zu Canary-Anmeldeinformationen führen, um automatisierte Spider zu kennzeichnen.

---

## ⚠️ Rechtlicher Haftungsausschluss

> [!ACHTUNG]
> CeWL ist nur für autorisierte Sicherheitsbewertungen, Penetrationstests und Passwort-Audit-Forschung konzipiert. Das Scrapen oder Spidern nicht autorisierter Systeme ist strengstens untersagt.