<!-- language-home-navigation -->
[🏠 Norsk Hovedside](../../README.nb.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇬🇧 English](../README.md)

---


# 🔍 dirsearch - Komplett jukseark

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
<p align="center">
  <img src="https://img.shields.io/badge/dirsearch-Web_Bruteforcer-blue?style=for-the-badge" alt="dirsearch">
  <img src="https://img.shields.io/badge/Language-Python_Async-green?style=for-the-badge" alt="Python Async">
  <img src="https://img.shields.io/badge/Recon-Web_Discovery-orange?style=for-the-badge" alt="Nettoppdagelse">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="License GPLv2">
</p>

<p align="center">
  <b>🌐 Rask, funksjonsfylt kommandolinje-nettbaneskanner med intelligent utvidelseshåndtering, rekursjon og gjennomgang</b>
</p>

---

## 📋 Innholdsfortegnelse

– [Introduksjon](#-introduksjon)
- [Verktøysammenligning](#-verktøy-sammenligning)
- [Installasjon](#-installasjon)
– [Grunnleggende bruk og målvalg](#-grunnbruk--målvalg)
- [Extension & Suffix Management](#-extension--suffiks-administrasjon)
- [Ordlister og ordbøker](#-ordlister--ordbøker)
- [Rekursjon og nettgjennomgang](#-rekursjon--nettgjennomgang)
- [Threading, Rate Limiting & Delays](#-threading-rate-limiting--delays)
- [Filtrering og samsvarende svar](#-filtrering--samsvarende-svar)
– [Tilpasning av HTTP-forespørsel](#-http-tilpasning)
  - [Egendefinerte overskrifter og brukeragenter](#tilpassede overskrifter--brukeragenter)
  - [informasjonskapsler og autentisering](#informasjonskapsler--autentisering)
  – [POST-data og forespørselsmetoder](#post-data--request-methods)
- [Proxies, Tor & Evasion](#-proxies-tor--evasion)
- [Utdataformater og rapportering](#-utdataformater--rapportering)
- [Real-World Pentest & Bug Bounty Recipes](#-real-world-pentest--bug-bounty-recipes)
- [Hurtigreferansetabell](#-hurtigreferansetabell)
– [Tips og beste fremgangsmåter](#-tips--beste praksis)
– [Juridisk ansvarsfraskrivelse](#-juridisk ansvarsfraskrivelse)

---

## 🎯 Introduksjon

**dirsearch** (laget av Mauro Soria) er et av de mest populære og allsidige kommandolinjeverktøyene for å oppdage skjulte filer og kataloger på webservere. Den er skrevet i Python og gir uovertruffen fleksibilitet med sine rike kommandolinjeargumenter, fargede terminalutdata, automatisk rekursiv gjennomgang, intelligent utvidelse av jokertegn og innebygde unnvikelseskontroller.

Enten du utfører nettpenetrasjonstesting, skuddpremiejakt eller interne CTF-vurderinger, er `dirsearch` skreddersydd for å avdekke skjulte administrative grensesnitt, sikkerhetskopieringsdatabaser, glemte API-endepunkter og sensitive legitimasjoner.

### Nøkkelfunksjoner

| Funksjon | Beskrivelse |
|--------|-------------|
| **Multi-Thread** | Høyytelses asynkron og gjenget forespørselsmotor |
| **Jokertegn for utvidelse** | Utvid `-e *` for å teste alle kjente nettfilutvidelser automatisk |
| **Integrert Crawling** | Følger HTML/JS-lenker til rekursive katalogkøer |
| **Rik filtrering** | Filtrer svar etter statuskode, bytestørrelse, ordantall, linjeantall eller regulært uttrykk |
| **Raw Request Replay** | Last inn komplette Burp Suite HTTP-forespørsler via `--raw` |
| **Flere formater** | Genererer rapporter i rentekst, JSON, CSV, Markdown og HTML |

---

## ⚖️ Verktøysammenligning

| Funksjon | dirsearch | ffuf | Feroxbuster | Gobuster | DIRB |
|--------|------------|------|--------|--------|-----|
| **Språk** | Python | Gå | Rust | Gå | C |
| **Hastighet** | ⭐⭐⭐⭐ (Rask) | ⭐⭐⭐⭐⭐ (Ekstremt rask) | ⭐⭐⭐⭐⭐ (Braande rask) | ⭐⭐⭐⭐ (Rask) | ⭐⭐ (Lav tråd) |
| **Standard rekursjon** | ✅ Automatisk (`-r`) | Valgfritt (`-recursion`) | ✅ Automatisk (`-d`) | Valgfritt (`-r`) | ✅ Automatisk |
| **Jokertegn for utvidelse (`*`)**| ✅ Innfødt (`-e *`) | ❌ Nei | ❌ Nei | ❌ Nei | ❌ Nei |
| **Innebygd Web Crawler** | ✅ Ja (`--crawl`) | ❌ Nei | ✅ Ja (`--extract-links`) | ❌ Nei | ❌ Nei |
| **POST / Data fuzzing** | ✅ Ja (`--data`) | ✅ Full HTTP | ❌ Begrenset | ❌ Nei | ❌ Nei |
| **Burp Raw Request Replay**| ✅ Ja (`--raw`) | ✅ Ja (`-request`) | ❌ Nei | ❌ Nei | ❌ Nei |
| **Innebygd ordliste** | ✅ Kuratert (`db/dicc.txt`)| ❌ Ingen | ❌ Ingen | ❌ Ingen | ✅ Liten liste |

---

## 📥 Installasjon

### Bruke pipx (anbefalt for Kali og moderne Linux)
```bash
# Install via pipx (isolated Python environment)
pipx install dirsearch

# Verify installation
dirsearch --version
```
### Bruker pip
```bash
# Install via standard pip
pip3 install dirsearch
```
### Bruke Git Clone (direkte fra kilden)
```bash
# Clone the official repository
git clone https://github.com/maurosoria/dirsearch.git --depth 1
cd dirsearch

# Install requirements and run
pip3 install -r requirements.txt
python3 dirsearch.py --version
```
### Kali Linux-pakke og docker
```bash
# Kali / Debian repository
sudo apt update && sudo apt install -y dirsearch

# Docker container
docker run -it --rm maurosoria/dirsearch -u https://example.com -e php,html
```
---

## 🚀 Grunnleggende bruk og målvalg

### Enkelt mål
```bash
# Basic scan against a single URL specifying extensions
dirsearch -u https://target.com -e php,html,js,txt

# Scan with trailing slash forced
dirsearch -u https://target.com -e php -f
```
### Flere mål
```bash
# Scan a list of targets from file
dirsearch -l targets.txt -e php,aspx,txt

# Scan stdin input from subfinder / httpx
cat live_hosts.txt | dirsearch --stdin -e php
```
### Portskanning og CIDR-målretting
```bash
# Scan specific ports on a host
dirsearch -u https://target.com --ports 80,443,8080,8443 -e php

# Scan an entire subnet range
dirsearch --cidr 192.168.1.0/24 -e html
```
---

## 🧩 Utvidelses- og suffiksadministrasjon

`dirsearch` har det kraftigste utvidelsesadministrasjonssystemet blant alle katalogskannere:
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

## 📚 Ordlister og ordbøker

`dirsearch` samler en omfattende, kamptestet ordliste på `db/dicc.txt`:
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

## 🔄 Rekursjon og webcrawling

### Rekursiv skanning
```bash
# Recursively scan newly found directories
dirsearch -u https://target.com -e php -r

# Limit recursion depth to 2 levels
dirsearch -u https://target.com -e php -r --recursion-depth 2

# Recursively scan only if HTTP status is 200, 301, or 302
dirsearch -u https://target.com -e php -r --recursion-status 200,301,302
```
### Integrert Web Crawler
```bash
# Crawl the target website for internal URLs and feed them into the scan queue
dirsearch -u https://target.com -e php --crawl
```
---

## ⚡ Tråding, ratebegrensning og forsinkelser

Kontroller skanningens aggressivitet for å forhindre utløsning av hastighetsbegrensning eller brannmurer for nettapplikasjoner (WAF):
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

## 🎯 Filtrering og samsvarende svar

### Statuskode inkludering og ekskludering
```bash
# Include only specific status codes (e.g., ignore everything except 200, 301, 403)
dirsearch -u https://target.com -e php -i 200,301,302,403

# Exclude specific status codes (e.g., exclude 404 and 500)
dirsearch -u https://target.com -e php -x 404,400,500,502,503
```
### Filtrering etter størrelse, ord og innhold
```bash
# Exclude responses by specific byte size (e.g. ignore custom 404 pages)
dirsearch -u https://target.com -e php --exclude-sizes 0B,1542B,3421B

# Exclude responses matching specific text on the page
dirsearch -u https://target.com -e php --exclude-texts "Not Found","Access Denied"

# Exclude responses matching a regular expression
dirsearch -u https://target.com -e php --exclude-regex "^Error:.*"
```
---

## 🛠️ HTTP-forespørselstilpasning

### Egendefinerte overskrifter og brukeragenter
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
### Informasjonskapsler og autentisering
```bash
# Provide session cookies for authenticated scanning
dirsearch -u https://target.com -e php -b "PHPSESSID=abc123xyz; admin_logged=true"

# HTTP Basic Authentication (username:password)
dirsearch -u https://target.com -e php --auth admin:SecretPass123 --auth-type basic

# NTLM Authentication for corporate Windows portals
dirsearch -u https://target.com -e php --auth CORP\\alice:Welcome2026 --auth-type ntlm
```
### POST-data og forespørselsmetoder
```bash
# Perform fuzzing using HTTP POST requests
dirsearch -u https://target.com/api -m POST -d "action=test&token=xyz" -e json

# Replay an entire raw HTTP request saved from Burp Suite
dirsearch -u https://target.com --raw request.txt -e php
```
---

## 🛡️ Proxies, Tor & Evasion
```bash
# Route all traffic through an HTTP or SOCKS5 proxy (e.g., Burp Suite)
dirsearch -u https://target.com -e php --proxy http://127.0.0.1:8080

# Route all traffic through the local Tor SOCKS proxy
dirsearch -u https://target.com -e php --tor

# Skip SSL/TLS certificate verification
dirsearch -u https://target.com -e php --no-check-certificate
```
---

## 📄 Utdataformater og rapportering
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

## 🎯 Real-World Pentest & Bug Bounty-oppskrifter

### 1. Rask full-Scope web-rekognosering
```bash
# Scan with common web extensions, follow redirects, random UA, 40 threads
dirsearch -u https://target.com \
  -e php,html,js,json,txt,zip \
  -t 40 \
  --random-agent \
  -i 200,301,302,403 \
  -o target_recon.txt
```
### 2. Jakt på glemte sikkerhetskopier og kildelekkasjer
```bash
# Test for database dumps, git repos, env files, and zip archives
dirsearch -u https://target.com \
  -e bak,old,orig,zip,tar.gz,sql,env,git \
  -w /usr/share/seclists/Discovery/Web-Content/raft-medium-files.txt \
  -i 200 \
  --exclude-sizes 0B
```
### 3. Deep Recursive Directory Discovery
```bash
# 2-level recursion, crawling pages for new paths, ignoring error status codes
dirsearch -u https://target.com \
  -e php,html \
  -r --recursion-depth 2 \
  --crawl \
  -x 404,500,502 \
  -o deep_scan.json --format json
```
### 4. Autentisert intern portalvurdering
```bash
# Scan through Burp Suite with internal session cookies and WAF bypass headers
dirsearch -u https://intranet.corp.local \
  -e aspx,ashx,config,txt \
  -b "ASP.NET_SessionId=xyz123; AuthToken=ey..." \
  -H "X-Forwarded-For: 127.0.0.1" \
  --proxy http://127.0.0.1:8080
```
---

## ⚡ Hurtigreferansetabell

| Oppgave | Kommando |
|------|--------|
| **Grunnleggende skanning med utvidelser** | `dirsearch -u https://target.com -e php,html,txt` |
| **Jokertegn for alle utvidelser** | `dirsearch -u https://target.com -e *` |
| **Egendefinert ordliste** | `dirsearch -u https://target.com -e php -w /path/to/wordlist.txt` |
| **Skann flere mål** | `dirsearch -l targets.txt -e php,aspx` |
| **Rekursiv oppdagelse** | `dirsearch -u https://target.com -e php -r --recursion-depth 2` |
| **Gjennomsøk målkoblinger** | `dirsearch -u https://target.com -e php --crawl` |
| **Inkluder statuskoder** | `dirsearch -u https://target.com -e php -i 200,301,302,403` |
| **Ekskluder statuskoder** | `dirsearch -u https://target.com -e php -x 404,500,502` |
| **Ekskluder falske positive størrelser**| `dirsearch -u https://target.com -e php --exclude-sizes 0B,1240B` |
| **Autentiser med informasjonskapsler** | `dirsearch -u https://target.com -e php -b "session=abc123"` |
| **Rute gjennom Burp Suite** | `dirsearch -u https://target.com -e php --proxy http://127.0.0.1:8080` |
| **Lagre rapporten til JSON** | `dirsearch -u https://target.com -e php -o report.json --format json` |

---

## 💡 Tips og beste fremgangsmåter

1. **Bruk `-e *` sparsomt på store ordlister:** Utvidelse med jokertegn multipliserer hvert ord med 20+ utvidelser. Bruk `-e *` kun med mindre lister som `common.txt` eller `dicc.txt`.
2. **Filtrer ut falske positive størrelser:** Moderne nettrammeverk returnerer tilpassede 404-sider med HTTP 200. Inspiser størrelsen på standardsvaret og legg til `--exclude-sizes <size>` for å eliminere støy.
3. **Sjekk alltid 403 Forbidden Paths:** I moderne sky- og bedriftsmiljøer indikerer HTTP 403-svar at katalogen eller endepunktet eksisterer. Dokumenter alle 403-er for omkjøring av topptekst og testing av banegjennomgang.
4. **Utnytt prefikser og suffikser for sikkerhetskopier:** Utviklere lagrer ofte sikkerhetskopier av sensitive filer (f.eks. `config.php.bak`, `old_database.sql`). Bruk `--prefixes` og `--suffixes` for å oppdage dem automatisk.

---

## ⚠️ Juridisk ansvarsfraskrivelse

> **VIKTIG:** Uautorisert portskanning, katalog brute-forcing og nettoppregning mot servere uten eksplisitt, skriftlig tillatelse er ulovlig. Bruk `dirsearch` strengt tatt i lovlige bug bounty-engasjementer, autoriserte penetrasjonstester og CTF-labmiljøer.