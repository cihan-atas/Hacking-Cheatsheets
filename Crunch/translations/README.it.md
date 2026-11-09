<!-- language-home-navigation -->
[🏠 Home Page Italiana](../../README.it.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🔢 Crunch: foglio informativo completo

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
<palign="centro">
  <img src="https://img.shields.io/badge/Crunch-Wordlist_Generator-blue?style=for-the-badge" alt="Crunch">
  <img src="https://img.shields.io/badge/Language-C-orange?style=for-the-badge" alt="Lingua C">
  <img src="https://img.shields.io/badge/Category-Password_Cracking-green?style=for-the-badge" alt="Cracking password">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="Licenza GPLv2">
</p>

<palign="centro">
  <b>🌐 Generatore di elenchi di parole che genera tutte le possibili combinazioni di set di caratteri utilizzando lunghezze, modelli e simboli personalizzati</b>
</p>

---

## 📋 Sommario

- [Panoramica e motore di permutazione](#-panoramica--il-motore-di-permutazione)
- [Installazione](#-installazione)
- [Sintassi di base e set di caratteri](#-basic-syntax--character-sets)
- [Corrispondenza pattern e segnaposto (`-t`)](#-pattern-matching--placeholders--t)
- [Utilizzo dei set di caratteri di sistema (`charset.lst`)](#-using-system-charsets-charsetlst)
- [Limitazione caratteri duplicati (`-d`)](#-limitazione-caratteri-duplicati--d)
- [Divisione di elenchi di parole in blocchi (`-b` e `-c`)](#-splitting-wordlists-into-chunks--b---c)
- [Piping direttamente negli strumenti di attacco (spazio su disco zero)](#-piping-direttamente-into-attack-tools-zero-disk-space)
- [Tabella di riferimento rapido](#-tabella-di-riferimento-rapido)
- [Considerazioni difensive e avvisi sui calcoli](#-considerazioni-difensive--calcolo-avvisi)
- [Disclaimer legale](#-legal-disclaimer)

---

## 🎯 Panoramica e motore di permutazione

Quando si attaccano modelli di password noti (ad esempio PIN aziendali, numeri di telefono o password conformi a regole di complessità standard come `Word2026!`), i dizionari statici standard come `rockyou.txt` non riescono a raggiungere l'obiettivo o sono inefficienti.

Scritto in C da **bofheaded**, **Crunch** è un generatore di elenchi di parole incredibilmente veloce. Calcola matematicamente le permutazioni, consentendoti di definire posizioni esatte dei caratteri, lunghezze, pool di caratteri e vincoli di duplicazione.

---

## 📥Installazione
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y crunch

# Arch Linux (AUR)
yay -S crunch
```
---

## 🚀 Sintassi e set di caratteri di base

La sintassi principale richiede una lunghezza minima, una lunghezza massima e, facoltativamente, un alfabeto:
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

## 🎭 Corrispondenza di modelli e segnaposti (`-t`)

Il flag `-t` consente di correggere i caratteri conosciuti mentre si effettua il fuzz degli slot sconosciuti utilizzando segnaposti speciali:

| Segnaposto | Tipo di carattere |
|-------------|----------------|
| `@` | Lettere minuscole (`a-z`) |
| `,` | Lettere maiuscole (`A-Z`) |
| `%` | Numeri (`0-9`) |
| `^` | Simboli speciali/punteggiatura (`!@#$%^&*...`) |

### Esempi pratici di modelli:
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

## 📚 Utilizzo dei set di caratteri di sistema (`charset.lst`)

Crunch raggruppa matrici di caratteri preconfigurate in `/usr/share/crunch/charset.lst`:
```bash
# Use pre-defined alphanumeric set (mixalpha-numeric)
crunch 6 6 -f /usr/share/crunch/charset.lst mixalpha-numeric -o out.txt

# Use hexadecimal lowercase characters (0-9, a-f)
crunch 8 8 -f /usr/share/crunch/charset.lst hex-lower -o hex_keys.txt
```
---

## 🚫 Limitazione dei caratteri duplicati (`-d`)

Applica limiti sui caratteri duplicati consecutivi per eliminare le password improbabili:
```bash
# Generate 8-character lowercase words with at most 2 consecutive identical letters
# (e.g., prevents 'aaabbb' while allowing 'aab')
crunch 8 8 abcdefghijklmnopqrstuvwxyz -d 2@ -o deduped.txt

# Limit consecutive numbers to at most 1 (prevents '11' or '22')
crunch 6 6 -t %%%%%% -d 1% -o no_repeated_digits.txt
```
---

## 📦 Suddivisione di elenchi di parole in blocchi (`-b` e `-c`)

Evita che i file multi-terabyte esauriscano lo spazio su disco suddividendoli in volumi gestibili:
```bash
# Split wordlist into 500MB chunks
crunch 8 8 -f /usr/share/crunch/charset.lst mixalpha-numeric -b 500mb -o START

# Split by line count (every 1,000,000 lines)
crunch 8 8 0123456789 -c 1000000 -o START
```
---

## ⚡ Collegamento diretto agli strumenti di attacco (spazio su disco zero)

Non scrivere mai elenchi di parole giganteschi su disco quando puoi trasmettere le permutazioni direttamente agli strumenti di cracking:
```bash
# Stream 8-digit numeric PINs straight into Aircrack-ng
crunch 8 8 0123456789 | aircrack-ng -w - -b 00:11:22:33:44:55 capture.cap

# Stream policy passwords directly into Hydra SSH brute-force
crunch 8 8 -t Admin%%^ | hydra -l admin -P - 10.10.10.50 ssh

# Stream into John the Ripper
crunch 6 6 0123456789 | john --stdin --format=raw-md5 hashes.txt
```
---

## ⚡ Tabella di consultazione rapida

| Obiettivo | Comando |
|-----------|---------|
| **PIN numerici (4 cifre)** | `crunch 4 4 0123456789 -o pins.txt` |
| **Modello: Maiuscolo + Parola + Numeri**| `crunch 8 8 -t ,@@@@%%^ -o passwords.txt` |
| **Modello: prefisso specifico** | `crunch 10 10 -t Company%%% -o wordlist.txt` |
| **Utilizza file di set di caratteri** | `crunch 6 6 -f charset.lst mixalpha-numeric -o out.txt` |
| **Limite consecutivo identico**| `crunch 8 8 -t @@@@@@@@ -d 2@ -o out.txt` |
| **Diviso in file da 1 GB** | `crunch 8 8 <CHARS> -b 1gb -o START` |
| **Pipe to Aircrack-ng** | `crunch 8 8 0123456789 \| aircrack-ng -w - target.cap` |
| **Tubo per Hydra** | `crunch 7 7 -t User%%% \| hydra -l user -P - <IP> ssh` |

---

## 🛡️ Considerazioni difensive e avvertenze sui calcoli

> [!ATTENZIONE]
> **Esplosioni di permutazioni:** controlla sempre la dimensione di output prevista di Crunch prima di confermare la generazione del disco. Un elenco di parole alfanumerico di 8 caratteri non vincolato produce **oltre 200 Terabyte** di dati di testo! Uscita del tubo quando possibile.

---

## ⚠️ Disclaimer legale

> [!ATTENZIONE]
> Crunch è destinato esclusivamente al recupero della password autorizzato, ai test di penetrazione e al controllo di sicurezza. La forzatura bruta delle password su sistemi non autorizzati è strettamente illegale.