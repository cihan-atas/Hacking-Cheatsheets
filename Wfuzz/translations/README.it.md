<!-- language-home-navigation -->
[🏠 Home Page Italiana](../../README.it.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🎯 Wfuzz - Cheatsheet completo

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ██╗    ██╗███████╗██╗   ██╗███████╗███████╗
  ██║    ██║██╔════╝██║   ██║╚══███╔╝╚══███╔╝
  ██║ █╗ ██║█████╗  ██║   ██║  ███╔╝   ███╔╝ 
  ██║███╗██║██╔══╝  ██║   ██║ ███╔╝   ███╔╝  
  ╚███╔███╔╝██║     ╚██████╔╝███████╗███████╗
   ╚══╝╚══╝ ╚═╝      ╚═════╝ ╚══════╝╚══════╝
       The Flexible Web Application Fuzzer & Parameter Explorer
```
<palign="centro">
  <img src="https://img.shields.io/badge/Wfuzz-Web_Fuzzer-blue?style=for-the-badge" alt="Wfuzz">
  <img src="https://img.shields.io/badge/Language-Python-orange?style=for-the-badge" alt="Lingua Python">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-green?style=for-the-badge" alt="Sicurezza web e API">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="Licenza GPLv2">
</p>

<palign="centro">
  <b>🌐 Fuzzer HTTP altamente personalizzabile progettato per scoprire parametri nascosti, bypass, intestazioni e matrici multi-payload</b>
</p>

---

## 📋 Sommario

- [Panoramica e architettura delle parole chiave](#-panoramica--l'architettura delle parole chiave)
- [Confronto Wfuzz vs ffuf](#-wfuzz-vs-ffuf-confronto)
- [Installazione](#-installazione)
- [Directory di base e rilevamento file](#-directory-base--rilevamento file)
- [Filtro e nascondi rumore (`--hc`, `--hl`, `--hw`, `--hh`)](#-filtro--nascondi-rumore---hc---hl---hw---hh)
- [Tipi di carico utile e codificatori dinamici (`-z`)](#-tipi di carico utile--encoder-dinamici--z)
- [Fuzzing parametri e variabili nascoste](#-parametro--fuzzing-variabile-nascosta)
- [Intestazioni, cookie e autenticazione](#-headers-cookies--autenticazione)
- [Fuzzing multivariabile (`FUZZ` e `FUZ2Z`)](#-multi-variable-fuzzing-fuzz-and-fuz2z)
- [Routing tramite Burp Suite (`-p`)](#-routing-through-burp-suite--p)
- [Tabella di riferimento rapido](#-tabella-di-riferimento-rapido)
- [Considerazioni difensive e OPSEC](#-considerazioni-difensive--opsec)
- [Disclaimer legale](#-legal-disclaimer)

---

## 🎯 Panoramica e architettura delle parole chiave

Scritto in Python da **Xavi Mendez**, **Wfuzz** è uno strumento classico e testato sul campo per la valutazione delle applicazioni web. Sostituisce i segnaposto designati (impostazione predefinita: `FUZZ`) all'interno di URL, parametri, intestazioni e corpi POST con voci di dizionario o payload generati algoritmicamente.

Wfuzz si distingue per la sua impareggiabile **versatilità del payload** (intervalli, generatori IP, stringhe regex, codificatori al volo come MD5 o Base64) e la meccanica di filtraggio modulare.

---

## ⚖️ Confronto tra Wfuzz e ffuf

| Caratteristica | Wfuzz | uff |
|---------|-------|------|
| **Linguaggio di implementazione** | Pitone | Vai |
| **Velocità pura** | ⭐⭐⭐ (Buono) | ⭐⭐⭐⭐⭐ (Incredibilmente veloce) |
| **Encoder di carico utile dinamico**| ✅ Nativo (`-z file,pass.txt,md5`)| ❌ Richiede script esterni |
| **Combinazioni di carico utile multiplo**| ✅ Nativo (`FUZZ`, `FUZ2Z`, `FUZ3Z`)| ✅ Supportato (`-w w1:W1 -w w2:W2`)|
| **Generatori di carico utile integrati**| ✅ Intervalli, IP, esadecimali, dizionari| ❌ File solo elenchi di parole |
| **Utilizzo delle risorse** | RAM/CPU moderata | Impronta di memoria ridotta |

---

## 📥Installazione
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y wfuzz

# Python Pip
pip3 install wfuzz

# Arch Linux
sudo pacman -S wfuzz
```
---

## 🚀 Directory di base e scoperta di file
```bash
# Basic directory fuzzing
wfuzz -w /usr/share/wordlists/dirb/common.txt http://target.local/FUZZ

# Fuzzing file extensions
wfuzz -w extensions.txt http://target.local/index.FUZZ

# Fuzzing hidden backup files
wfuzz -w words.txt http://target.local/FUZZ.bak
```
---

## 🎯 Filtrare e nascondere il rumore (`--hc`, `--hl`, `--hw`, `--hh`)

I server del mondo reale restituiscono risposte anche per pagine inesistenti (pagine 404 personalizzate o reindirizzamenti). Wfuzz fornisce flag di filtraggio a grana fine:
```bash
# Hide specific HTTP status codes (e.g. 404, 403)
wfuzz -w wordlist.txt --hc 404,403 http://target.local/FUZZ

# Hide responses by line count (filters out static custom 404 pages)
wfuzz -w wordlist.txt --hl 42 http://target.local/FUZZ

# Hide responses by word count
wfuzz -w wordlist.txt --hw 120 http://target.local/FUZZ

# Hide responses by character count / body length
wfuzz -w wordlist.txt --hh 0,1052 http://target.local/FUZZ

# Show ONLY HTTP 200 and 302 responses
wfuzz -w wordlist.txt --sc 200,302 http://target.local/FUZZ
```
---

## 📦 Tipi di carico utile ed encoder dinamici (`-z`)

Wfuzz fornisce generatori integrati che eliminano la necessità di file di testo pregenerati:
```bash
# Numeric Range Fuzzing (e.g., IDOR testing from 1 to 500)
wfuzz -z range,1-500 http://target.local/user.php?id=FUZZ

# Custom Inline List
wfuzz -z list,admin-root-test-dev http://target.local/FUZZ/

# On-the-fly MD5 encoding of wordlist entries
wfuzz -z file,passwords.txt,md5 http://target.local/login?hash=FUZZ

# On-the-fly Base64 encoding
wfuzz -z file,usernames.txt,base64 http://target.local/auth?token=FUZZ
```
---

## 🔍 Fuzzing di parametri e variabili nascoste

Scopri i parametri GET/POST non collegati:
```bash
# GET parameter discovery
wfuzz -w /usr/share/seclists/Discovery/Web-Content/burp-parameter-names.txt \
  --hc 404 --hh 2340 "http://target.local/search.php?FUZZ=test"

# POST body parameter discovery
wfuzz -w parameters.txt -d "FUZZ=admin" --hc 404 http://target.local/api/update
```
---

## 🍪 Intestazioni, cookie e autenticazione
```bash
# Cookie Fuzzing (Session hijacking / privilege testing)
wfuzz -w wordlist.txt -b "session=FUZZ" http://target.local/dashboard

# HTTP Header Fuzzing (IP bypasses via X-Forwarded-For)
wfuzz -z file,ips.txt -H "X-Forwarded-For: FUZZ" http://target.local/admin

# Basic Authentication brute force
wfuzz -z file,users.txt -z file,passwords.txt --basic "FUZZ:FUZ2Z" http://target.local/protected/
```
---

## ⚡ Fuzzing multivariabile (`FUZZ` e `FUZ2Z`)

Combina più elenchi di parole utilizzando tag segnaposto distinti:
```bash
# Test username and password matrix simultaneously
wfuzz -w users.txt -w passwords.txt -d "user=FUZZ&pass=FUZ2Z" --hc 200 http://target.local/login.php

# Fuzz path and extension together
wfuzz -w words.txt -w extensions.txt http://target.local/FUZZ.FUZ2Z
```
---

## 🔁 Instradamento tramite Burp Suite (`-p`)

Ispeziona i payload e le risposte all'interno di Burp Suite:
```bash
# Route requests through local Burp Suite proxy
wfuzz -p 127.0.0.1:8080:HTTP -w wordlist.txt http://target.local/FUZZ
```
---

## ⚡ Tabella di consultazione rapida

| Obiettivo | Comando |
|-----------|---------|
| **Fuzzing delle directory** | `wfuzz -w <DICT> <URL>/FUZZ` |
| **Nascondi codici di stato HTTP** | `wfuzz -w <DICT> --hc 404,403 <URL>/FUZZ` |
| **Nascondi per lunghezza della risposta** | `wfuzz -w <DICT> --hh <LENGTH> <URL>/FUZZ` |
| **Parametro Fuzz GET** | `wfuzz -w <PARAMS> "<URL>?FUZZ=test"` |
| **Corpo POST Fuzz** | `wfuzz -w <PARAMS> -d "FUZZ=test" <URL>` |
| **Carichi utili con intervallo numerico** | `wfuzz -z range,1-100 "<URL>?id=FUZZ"` |
| **Codifica i carichi utili al volo**| `wfuzz -z file,<DICT>,base64 "<URL>?token=FUZZ"` |
| **Fuzzing di più parole chiave** | `wfuzz -w <W1> -w <W2> -d "u=FUZZ&p=FUZ2Z" <URL>` |
| **Percorso tramite Burp Proxy** | `wfuzz -p 127.0.0.1:8080:HTTP -w <DICT> <URL>/FUZZ` |

---

## 🛡️ Considerazioni difensive e OPSEC

1. **Blocco WAF:** Il fuzzing di centinaia di richieste al secondo attiva la limitazione della velocità di Cloudflare, AWS WAF e ModSecurity. Utilizzare `-s <seconds>` per limitare la velocità della richiesta.
2. **Esaurimento delle risorse:** un intenso fuzzing sulle query di database non indicizzati può causare condizioni di Denial of Service (DoS) sui server Web legacy.

---

## ⚠️ Disclaimer legale

> [!ATTENZIONE]
> Questo cheatsheet è destinato esclusivamente ai test di penetrazione autorizzati e al controllo delle vulnerabilità. È severamente vietato il fuzzing di applicazioni non autorizzate.