<!-- language-home-navigation -->
[🏠 Home Page Italiana](../../README.it.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🕷️ CeWL - Cheatsheet completo

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
<palign="centro">
  <img src="https://img.shields.io/badge/CeWL-Custom_Wordlist_Generator-blue?style=for-the-badge" alt="CeWL">
  <img src="https://img.shields.io/badge/Author-DigiNinja-orange?style=for-the-badge" alt="DigiNinja">
  <img src="https://img.shields.io/badge/Category-Password_Cracking-green?style=for-the-badge" alt="Cracking password">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="Licenza GPLv2">
</p>

<palign="centro">
  <b>🌐 Generatore di dizionari di password specifici per target che esegue lo spider dei siti Web per raccogliere terminologia aziendale specializzata, nomi di marchi ed e-mail dei dipendenti</b>
</p>

---

## 📋 Sommario

- [Panoramica e filosofia della password mirata](#-panoramica--filosofia-della-password-mirata)
- [Installazione](#-installazione)
- [Scansione di base del sito web](#-basic-website-crawling)
- [Controlli profondità spider e lunghezza parole](#-spider-profondità--controlli-lunghezza-parola)
- [Raccolta di indirizzi email (`-e`)](#-harvesting-email-addresses--e)
- [Numeri compresi e lettere minuscole](#-numeri-compresi--minuscole)
- [Estrazione metadati documento (`-a`)](#-document-metadata-extraction--a)
- [Conteggio parole e profilazione frequenza (`-c`)](#-conteggio parole--profilatura-frequenza--c)
- [Concatenare CeWL con Hashcat e John Rules](#-chaining-cewl-with-hashcat--john-rules)
- [Tabella di riferimento rapido](#-tabella-di-riferimento-rapido)
- [Considerazioni difensive e OPSEC](#-considerazioni-difensive--opsec)
- [Disclaimer legale](#-legal-disclaimer)

---

## 🎯 Panoramica e filosofia della password mirata

Elenchi di parole generici come `rockyou.txt` sono efficaci per le password dei consumatori, ma **spesso non riescono a rispettare le policy aziendali sulle password** che richiedono più di 12 caratteri, simboli speciali e rotazioni stagionali.

I dipendenti basano abitualmente le proprie password aziendali su:
- Nomi dei progetti aziendali, nomi in codice interni e slogan (`Pegasus2026!`).
- Terminologia specifica del settore, gergo tecnico o marchi di prodotto.
- Nomi dei dirigenti, punti di riferimento della posizione e indirizzi degli uffici.

Creato da **Robin Wood (DigiNinja)**, **CeWL** (generatore di elenchi di parole personalizzate) è uno spider Ruby che esegue la scansione di un URL di destinazione specificato fino a una profondità definita, estrae parole univoche corrispondenti ai requisiti di lunghezza minima e crea un dizionario di password personalizzato per il coinvolgimento.

---

## 📥Installazione
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y cewl

# Arch Linux (AUR)
yay -S cewl

# Ruby Gem
gem install cewl
```
---

## 🚀 Scansione di base del sito web
```bash
# Crawl website and output words to console
cewl https://target.local

# Crawl and save to a custom wordlist file
cewl https://target.local -w company_wordlist.txt
```
---

## 🕸️ Controlli della profondità del ragno e della lunghezza delle parole

Controlla quanto profondamente CeWL segue i collegamenti e quali dimensioni delle parole cattura:
```bash
# Crawl 2 links deep (-d 2) and enforce minimum word length of 8 characters (-m 8)
cewl https://target.local -d 2 -m 8 -w wordlist_min8.txt

# Aggressive 3-level deep crawl for comprehensive corporate terminology
cewl https://target.local -d 3 -m 7 --no-words -w deep_crawl.txt
```
---

## 📧 Raccolta di indirizzi email (`-e`)

Estrai indirizzi e-mail aziendali per successive simulazioni di phishing o spraying di credenziali:
```bash
# Extract emails to a separate dedicated file
cewl https://target.local -e --email_file emails.txt -w words.txt
```
---

## 🔡 Compresi numeri e lettere minuscole
```bash
# Include words containing alphanumeric combinations (e.g., product names like V300, X86)
cewl https://target.local --with-numbers -m 6 -w words_with_numbers.txt

# Convert all discovered words to lowercase (great for rule generation)
cewl https://target.local --lowercase -w lowercase_words.txt
```
---

## 📄 Estrazione metadati documento (`-a`)

Estrai i nomi utente dell'autore e del creatore dai documenti PDF e Microsoft Office scaricati:
```bash
# Download and analyze document metadata to extract employee usernames
cewl https://target.local -a --meta_file extracted_authors.txt
```
---

## 📊 Conteggio parole e profilazione frequenza (`-c`)

Ordina le parole per frequenza per identificare le parole d'ordine aziendali più pervasive:
```bash
# Append occurrence count next to each word
cewl https://target.local -c -m 6 -w frequency.txt

# Sort by most frequent words in descending order
sort -k2 -nr frequency.txt | head -n 30
```
---

## ⚡ Concatenamento di CeWL con Hashcat e John Rules

Una lista di parole CeWL grezza contiene le radici del dizionario di base. Per produrre password valide, espandere l'elenco di parole utilizzando le **regole di mutazione Hashcat/John lo Squartatore**:
```bash
# 1. Harvest base words from corporate website
cewl https://target.local -m 5 --lowercase -w base_words.txt

# 2. Mutate words using Hashcat's best64 rule set
hashcat --force base_words.txt -r /usr/share/hashcat/rules/best64.rule --stdout | sort -u > mutated_passwords.txt

# Example mutations produced:
# Acmetool -> Acmetool2026!, !Acmetool123, 4cmet00l, Acmetool#1
```
---

## ⚡ Tabella di consultazione rapida

| Obiettivo | Comando |
|-----------|---------|
| **Scansione di base su file** | `cewl <URL> -w wordlist.txt` |
| **Imposta lunghezza e profondità minime** | `cewl <URL> -m 8 -d 2 -w wordlist.txt` |
| **Estrai email aziendali**| `cewl <URL> -e --email_file emails.txt` |
| **Estrai metadati del documento**| `cewl <URL> -a --meta_file authors.txt` |
| **Includi parole alfanumeriche**| `cewl <URL> --with-numbers -w wordlist.txt` |
| **Uscita in minuscolo** | `cewl <URL> --lowercase -w wordlist.txt` |
| **Conteggio delle frequenze delle parole** | `cewl <URL> -c -w frequencies.txt` |
| **Autentica dietro l'accesso**| `cewl <URL> -a "Cookie: session=xyz"` |

---

## 🛡️ Considerazioni difensive e OPSEC

1. **Log di accesso al server Web:** CeWL genera centinaia di richieste HTTP GET rapide, producendo log di accesso spider identificabili. Utilizza stringhe user-agent personalizzate (`-u "Mozilla/5.0..."`) per integrarti.
2. **Trappole per parole d'amore:** le squadre blu difensive possono incorporare collegamenti HTML invisibili o nascosti che portano a credenziali canary per contrassegnare gli spider automatizzati.

---

## ⚠️ Disclaimer legale

> [!ATTENZIONE]
> CeWL è progettato esclusivamente per valutazioni di sicurezza autorizzate, test di penetrazione e ricerche di controllo delle password. È severamente vietato lo scraping o lo spidering di sistemi non autorizzati.