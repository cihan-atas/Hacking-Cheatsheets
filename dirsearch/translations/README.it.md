<!-- language-home-navigation -->
[🏠 Home Page Italiana](../../README.it.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🔍 dirsearch - Cheatsheet completo

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ██████╗ ██╗██████╗ ███████╗███████╗ █████╗ ██████╗  ██████╗██╗  ██╗
  ██╔══██╗██║██╔══██╗██╔════╝██╔════╝██╔══██╗██╔══██╗██╔════╝██║  ██║
  ██║  ██║██║██████╔╝███████╗█████╗  ███████║██████╔╝██║     ███████║
  ██║  ██║██║██╔══██╗╚════██║██╔══╝  ██╔══██║██╔══██╗██║     ██╔══██║
  ██████╔╝██║██║  ██║███████║███████╗██║  ██║██║  ██║╚██████╗██║  ██║
  ╚═════╝ ╚═╝╚═╝  ╚═╝╚══════╝╚══════╝╚═╝  ╚═╝╚═╝  ╚═╝ ╚═════╝╚═╝  ╚═╝
       Advanced Web Path Scanner & Directory Brute-Forcer
```
<palign="centro">
  <img src="https://img.shields.io/badge/dirsearch-Web_Bruteforcer-blue?style=for-the-badge" alt="dirsearch">
  <img src="https://img.shields.io/badge/Language-Python_Async-green?style=for-the-badge" alt="Python Async">
  <img src="https://img.shields.io/badge/Recon-Web_Discovery-orange?style=for-the-badge" alt="Web Discovery">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="Licenza GPLv2">
</p>

<palign="centro">
  <b>🌐 Scanner del percorso web da riga di comando veloce e ricco di funzionalità con gestione intelligente delle estensioni, ricorsione e scansione</b>
</p>

---

## 📋 Sommario

- [Introduzione](#-introduzione)
- [Confronto strumenti](#-confronto-strumenti)
- [Installazione](#-installazione)
- [Utilizzo di base e selezione target](#-basic-usage--target-selection)
- [Gestione estensioni e suffissi](#-estensione--gestione-suffissi)
- [Elenchi di parole e dizionari](#-elenchi di parole--dizionari)
- [Ricursione e scansione web](#-recursion--web-crawling)
- [Threading, limitazione della velocità e ritardi](#-threading-rate-limiting--delays)
- [Filtro e corrispondenza delle risposte](#-filtro--risposte-corrispondenti)
- [Personalizzazione richiesta HTTP](#-http-request-customization)
  - [Intestazioni personalizzate e agenti utente](#intestazioni-personalizzate--agenti utente)
  - [Cookie e autenticazione](#cookie--autenticazione)
  - [Dati POST e metodi di richiesta](#post-data--request-methods)
- [Proxy, Tor ed evasione](#-proxies-tor--evasione)
- [Formati di output e reportistica](#-formati-output--reporting)
- [Ricette Pentest e Bug Bounty nel mondo reale](#-real-world-pentest--bug-bounty-recipes)
- [Tabella di riferimento rapido](#-tabella-di-riferimento-rapido)
- [Suggerimenti e best practice](#-tips--best-practice)
- [Disclaimer legale](#-legal-disclaimer)

---

## 🎯 Introduzione

**dirsearch** (creato da Mauro Soria) è uno degli strumenti da riga di comando più popolari e versatili per scoprire file e directory nascosti sui server web. Scritto in Python, offre una flessibilità senza pari con i suoi ricchi argomenti della riga di comando, output del terminale colorato, scansione ricorsiva automatica, espansione intelligente dell'estensione con caratteri jolly e controlli di evasione integrati.

Che tu stia eseguendo test di penetrazione web, caccia di bug o valutazioni CTF interne, `dirsearch` è personalizzato per scoprire interfacce amministrative nascoste, database di backup, endpoint API dimenticati e credenziali sensibili.

### Caratteristiche principali

| Caratteristica | Descrizione |
|---------|-------------|
| **Multi-thread** | Motore di richiesta asincrono e threaded ad alte prestazioni |
| **Caratteri jolly estensione** | Espandi `-e *` per testare automaticamente tutte le estensioni di file Web conosciute |
| **Scansione integrata** | Segue i collegamenti HTML/JS per seminare code di directory ricorsive |
| **Filtro ricco** | Filtra le risposte per codice di stato, dimensione in byte, conteggio di parole, conteggio di righe o regex |
| **Riproduzione richiesta grezza** | Carica le richieste HTTP complete di Burp Suite tramite `--raw` |
| **Formati multipli** | Genera report in testo normale, JSON, CSV, Markdown e HTML |

---

## ⚖️ Confronto degli strumenti

| Caratteristica | ricerca | uff | Feroxbuster | Gobuster | DIRB |
|---------|-----------|------|-------------|----------|------|
| **Lingua** | Pitone | Vai | Ruggine | Vai | C|
| **Velocità** | ⭐⭐⭐⭐ (Veloce) | ⭐⭐⭐⭐⭐ (Estremamente veloce) | ⭐⭐⭐⭐⭐ (Incredibilmente veloce) | ⭐⭐⭐⭐ (Veloce) | ⭐⭐ (Filo basso) |
| **Ricorsione predefinita** | ✅ Automatico (`-r`) | Opzionale (`-recursion`) | ✅ Automatico (`-d`) | Opzionale (`-r`) | ✅ Automatico |
| **Carattere jolly estensione (`*`)**| ✅ Nativo (`-e *`) | ❌No | ❌No | ❌No | ❌No |
| **Crowler web integrato** | ✅ Sì (`--crawl`) | ❌No | ✅ Sì (`--extract-links`) | ❌No | ❌No |
| **POST/Fuzzing dei dati** | ✅ Sì (`--data`) | ✅ HTTP completo | ❌Limitato | ❌No | ❌No |
| **Riproduzione richiesta Burp Raw**| ✅ Sì (`--raw`) | ✅ Sì (`-request`) | ❌No | ❌No | ❌No |
| **Elenco parole integrato** | ✅ Curato (`db/dicc.txt`)| ❌ Nessuno | ❌ Nessuno | ❌ Nessuno | ✅ Piccolo elenco |

---

## 📥Installazione

### Utilizzo di pipx (consigliato per Kali e Modern Linux)
```bash
# Install via pipx (isolated Python environment)
pipx install dirsearch

# Verify installation
dirsearch --version
```
### Usando pip
```bash
# Install via standard pip
pip3 install dirsearch
```
### Utilizzo di Git Clone (diretto dal sorgente)
```bash
# Clone the official repository
git clone https://github.com/maurosoria/dirsearch.git --depth 1
cd dirsearch

# Install requirements and run
pip3 install -r requirements.txt
python3 dirsearch.py --version
```
### Pacchetto Kali Linux e finestra mobile
```bash
# Kali / Debian repository
sudo apt update && sudo apt install -y dirsearch

# Docker container
docker run -it --rm maurosoria/dirsearch -u https://example.com -e php,html
```
---

## 🚀 Utilizzo di base e selezione del target

### Bersaglio singolo
```bash
# Basic scan against a single URL specifying extensions
dirsearch -u https://target.com -e php,html,js,txt

# Scan with trailing slash forced
dirsearch -u https://target.com -e php -f
```
### Bersagli multipli
```bash
# Scan a list of targets from file
dirsearch -l targets.txt -e php,aspx,txt

# Scan stdin input from subfinder / httpx
cat live_hosts.txt | dirsearch --stdin -e php
```
### Scansione delle porte e targeting CIDR
```bash
# Scan specific ports on a host
dirsearch -u https://target.com --ports 80,443,8080,8443 -e php

# Scan an entire subnet range
dirsearch --cidr 192.168.1.0/24 -e html
```
---

## 🧩 Gestione di estensioni e suffissi

`dirsearch` ha il sistema di gestione delle estensioni più potente tra tutti gli scanner di directory:
```bash
# Specify multiple extensions
dirsearch -u https://target.com -e php,html,json,txt,zip

# Use the wildcard (*) to test ALL known extensions automatically
dirsearch -u https://target.com -e *

# Add prefixes to wordlist entries (e.g. test_admin, dev_admin)
dirsearch -u https://target.com -e php --prefixes test_,dev_,staging_,old_

# Add suffixes to wordlist entries (e.g. admin_backup, admin_old)
dirsearch -u https://target.com -e php --suffixes _bak,~,_2026,_old
```
---

## 📚 Elenchi di parole e dizionari

`dirsearch` raggruppa un elenco di parole completo e testato in battaglia su `db/dicc.txt`:
```bash
# Run using the default bundled dictionary
dirsearch -u https://target.com -e php

# Use SecLists Common or Medium wordlists
dirsearch -u https://target.com -e php -w /usr/share/seclists/Discovery/Web-Content/directory-list-2.3-medium.txt

# Use multiple wordlists simultaneously
dirsearch -u https://target.com -e php -w list1.txt,list2.txt

# Force all wordlist entries to lowercase or uppercase
dirsearch -u https://target.com -e php --lowercase
dirsearch -u https://target.com -e php --uppercase
```
---

## 🔄 Ricorsione e scansione web

### Scansione ricorsiva
```bash
# Recursively scan newly found directories
dirsearch -u https://target.com -e php -r

# Limit recursion depth to 2 levels
dirsearch -u https://target.com -e php -r --recursion-depth 2

# Recursively scan only if HTTP status is 200, 301, or 302
dirsearch -u https://target.com -e php -r --recursion-status 200,301,302
```
### Crawler web integrato
```bash
# Crawl the target website for internal URLs and feed them into the scan queue
dirsearch -u https://target.com -e php --crawl
```
---

## ⚡ Threading, limitazione della velocità e ritardi

Controllare l'aggressività della scansione per evitare l'attivazione di limiti di velocità o Web Application Firewall (WAF):
```bash
# Set number of concurrent threads (default: 25)
dirsearch -u https://target.com -e php -t 50

# Add a delay between requests (e.g. 0.5 seconds)
dirsearch -u https://target.com -e php -s 0.5

# Enforce a maximum request rate per second (e.g. max 15 req/sec)
dirsearch -u https://target.com -e php --max-rate 15

# Customize HTTP connection timeouts and retries
dirsearch -u https://target.com -e php --timeout 8 --retries 3

# Exit scan immediately on excessive connection errors
dirsearch -u https://target.com -e php --exit-on-error
```
---

## 🎯 Filtraggio e corrispondenza delle risposte

### Inclusione ed esclusione del codice di stato
```bash
# Include only specific status codes (e.g., ignore everything except 200, 301, 403)
dirsearch -u https://target.com -e php -i 200,301,302,403

# Exclude specific status codes (e.g., exclude 404 and 500)
dirsearch -u https://target.com -e php -x 404,400,500,502,503
```
### Filtraggio per dimensione, parole e contenuto
```bash
# Exclude responses by specific byte size (e.g. ignore custom 404 pages)
dirsearch -u https://target.com -e php --exclude-sizes 0B,1542B,3421B

# Exclude responses matching specific text on the page
dirsearch -u https://target.com -e php --exclude-texts "Not Found","Access Denied"

# Exclude responses matching a regular expression
dirsearch -u https://target.com -e php --exclude-regex "^Error:.*"
```
---

## 🛠️ Personalizzazione della richiesta HTTP

### Intestazioni personalizzate e agenti utente
```bash
# Set a custom header (e.g., authorization token or internal bypass header)
dirsearch -u https://target.com -e php -H "X-Forwarded-For: 127.0.0.1" -H "X-Requested-With: XMLHttpRequest"

# Load headers from a file
dirsearch -u https://target.com -e php --header-list custom_headers.txt

# Use a randomized User-Agent per request to bypass basic WAFs
dirsearch -u https://target.com -e php --random-agent

# Set a custom User-Agent
dirsearch -u https://target.com -e php --user-agent "Mozilla/5.0 (Windows NT 10.0; Win64; x64)"
```
### Cookie e autenticazione
```bash
# Provide session cookies for authenticated scanning
dirsearch -u https://target.com -e php -b "PHPSESSID=abc123xyz; admin_logged=true"

# HTTP Basic Authentication (username:password)
dirsearch -u https://target.com -e php --auth admin:SecretPass123 --auth-type basic

# NTLM Authentication for corporate Windows portals
dirsearch -u https://target.com -e php --auth CORP\\alice:Welcome2026 --auth-type ntlm
```
### Dati POST e metodi di richiesta
```bash
# Perform fuzzing using HTTP POST requests
dirsearch -u https://target.com/api -m POST -d "action=test&token=xyz" -e json

# Replay an entire raw HTTP request saved from Burp Suite
dirsearch -u https://target.com --raw request.txt -e php
```
---

## 🛡️ Proxy, Tor ed Evasione
```bash
# Route all traffic through an HTTP or SOCKS5 proxy (e.g., Burp Suite)
dirsearch -u https://target.com -e php --proxy http://127.0.0.1:8080

# Route all traffic through the local Tor SOCKS proxy
dirsearch -u https://target.com -e php --tor

# Skip SSL/TLS certificate verification
dirsearch -u https://target.com -e php --no-check-certificate
```
---

## 📄 Formati di output e reportistica
```bash
# Save output as a clean text file
dirsearch -u https://target.com -e php -o results.txt

# Export results to JSON format
dirsearch -u https://target.com -e php -o results.json --format json

# Export results to HTML, CSV, or Markdown
dirsearch -u https://target.com -e php -o report.html --format html
dirsearch -u https://target.com -e php -o results.csv --format csv
dirsearch -u https://target.com -e php -o report.md --format markdown
```
---

## 🎯 Ricette Pentest e Bug Bounty dal mondo reale

### 1. Ricognizione web rapida e completa
```bash
# Scan with common web extensions, follow redirects, random UA, 40 threads
dirsearch -u https://target.com \
  -e php,html,js,json,txt,zip \
  -t 40 \
  --random-agent \
  -i 200,301,302,403 \
  -o target_recon.txt
```
### 2. A caccia di backup dimenticati e perdite di fonti
```bash
# Test for database dumps, git repos, env files, and zip archives
dirsearch -u https://target.com \
  -e bak,old,orig,zip,tar.gz,sql,env,git \
  -w /usr/share/seclists/Discovery/Web-Content/raft-medium-files.txt \
  -i 200 \
  --exclude-sizes 0B
```
### 3. Scoperta approfondita delle directory ricorsive
```bash
# 2-level recursion, crawling pages for new paths, ignoring error status codes
dirsearch -u https://target.com \
  -e php,html \
  -r --recursion-depth 2 \
  --crawl \
  -x 404,500,502 \
  -o deep_scan.json --format json
```
### 4. Valutazione del portale interno autenticato
```bash
# Scan through Burp Suite with internal session cookies and WAF bypass headers
dirsearch -u https://intranet.corp.local \
  -e aspx,ashx,config,txt \
  -b "ASP.NET_SessionId=xyz123; AuthToken=ey..." \
  -H "X-Forwarded-For: 127.0.0.1" \
  --proxy http://127.0.0.1:8080
```
---

## ⚡ Tabella di consultazione rapida

| Compito | Comando |
|------|---------|
| **Scansione di base con estensioni** | `dirsearch -u https://target.com -e php,html,txt` |
| **Carattere jolly per tutte le estensioni** | `dirsearch -u https://target.com -e *` |
| **Elenco di parole personalizzato** | `dirsearch -u https://target.com -e php -w /path/to/wordlist.txt` |
| **Scansiona più target** | `dirsearch -l targets.txt -e php,aspx` |
| **Scoperta ricorsiva** | `dirsearch -u https://target.com -e php -r --recursion-depth 2` |
| **Scansione link di destinazione** | `dirsearch -u https://target.com -e php --crawl` |
| **Includi codici di stato** | `dirsearch -u https://target.com -e php -i 200,301,302,403` |
| **Escludi codici di stato** | `dirsearch -u https://target.com -e php -x 404,500,502` |
| **Escludi dimensioni false positive**| `dirsearch -u https://target.com -e php --exclude-sizes 0B,1240B` |
| **Autenticazione con i cookie** | `dirsearch -u https://target.com -e php -b "session=abc123"` |
| **Percorso attraverso Burp Suite** | `dirsearch -u https://target.com -e php --proxy http://127.0.0.1:8080` |
| **Salva rapporto in JSON** | `dirsearch -u https://target.com -e php -o report.json --format json` |

---

## 💡 Suggerimenti e migliori pratiche

1. **Utilizza `-e *` con parsimonia su elenchi di parole di grandi dimensioni:** L'espansione dell'estensione con caratteri jolly moltiplica ogni parola per oltre 20 estensioni. Utilizza `-e *` solo con elenchi più piccoli come `common.txt` o `dicc.txt`.
2. **Filtra dimensioni false positive:** I moderni framework web restituiscono pagine 404 personalizzate con HTTP 200. Controlla la dimensione della risposta predefinita e aggiungi `--exclude-sizes <size>` per eliminare il rumore.
3. **Controlla sempre i percorsi vietati 403:** Nei moderni ambienti cloud e aziendali, le risposte HTTP 403 indicano che la directory o l'endpoint esiste. Documentare tutti i 403 per il bypass dell'intestazione e il test di attraversamento del percorso.
4. **Sfrutta prefissi e suffissi per i backup:** gli sviluppatori salvano spesso copie di backup di file sensibili (ad esempio `config.php.bak`, `old_database.sql`). Utilizza `--prefixes` e `--suffixes` per rilevarli automaticamente.

---

## ⚠️ Disclaimer legale

> **IMPORTANTE:** La scansione non autorizzata delle porte, il brute-forcing delle directory e l'enumerazione web contro i server senza un'esplicita autorizzazione scritta sono illegali. Utilizzare `dirsearch` rigorosamente in impegni di bug bounty legali, test di penetrazione autorizzati e ambienti di laboratorio CTF.