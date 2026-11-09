<!-- language-home-navigation -->
[🏠 Deutsche Startseite](../../README.de.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🔍 dirsearch – Vollständiger Cheatsheet

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
  <img src="https://img.shields.io/badge/Recon-Web_Discovery-orange?style=for-the-badge" alt="Web Discovery">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="License GPLv2">
</p>

<p align="center">
  <b>🌐 Schneller, funktionsreicher Befehlszeilen-Webpfadscanner mit intelligenter Erweiterungsverarbeitung, Rekursion und Crawling</b>
</p>

---

## 📋 Inhaltsverzeichnis

- [Einführung](#-Einführung)
- [Werkzeugvergleich](#-tool-comparison)
- [Installation](#-Installation)
- [Grundlegende Nutzung und Zielauswahl](#-basic-usage--target-selection)
- [Erweiterungs- und Suffixverwaltung](#-extension--suffix-management)
- [Wortlisten und Wörterbücher](#-wordlists--dictionaries)
- [Rekursion und Web-Crawling](#-recursion--web-crawling)
- [Threading, Ratenbegrenzung und Verzögerungen](#-threading-rate-limiting--delays)
- [Antworten filtern und abgleichen](#-filtering--matching-responses)
- [Anpassung der HTTP-Anfrage](#-http-request-customization)
  - [Benutzerdefinierte Header und Benutzeragenten](#custom-headers--user-agents)
  - [Cookies & Authentifizierung](#cookies--authentication)
  - [POST-Daten und Anforderungsmethoden](#post-data--request-methods)
- [Proxies, Tor & Evasion](#-proxies-tor--evasion)
- [Ausgabeformate und Berichterstattung](#-output-formats--reporting)
- [Pentest- und Bug-Bounty-Rezepte aus der realen Welt](#-real-world-pentest--bug-bounty-recipes)
- [Kurzreferenztabelle](#-quick-reference-table)
- [Tipps und Best Practices](#-tips--best-practices)
- [Rechtlicher Haftungsausschluss](#-legal-disclaimer)

---

## 🎯 Einführung

**dirsearch** (erstellt von Mauro Soria) ist eines der beliebtesten und vielseitigsten Befehlszeilentools zum Auffinden versteckter Dateien und Verzeichnisse auf Webservern. Es wurde in Python geschrieben und bietet mit seinen umfangreichen Befehlszeilenargumenten, der kolorierten Terminalausgabe, dem automatischen rekursiven Crawling, der intelligenten Wildcard-Erweiterungserweiterung und den integrierten Umgehungskontrollen unübertroffene Flexibilität.

Ob Sie Web-Penetrationstests, Bug-Bounty-Jagd oder interne CTF-Bewertungen durchführen, `dirsearch` ist darauf zugeschnitten, versteckte Verwaltungsschnittstellen, Backup-Datenbanken, vergessene API-Endpunkte und vertrauliche Anmeldeinformationen aufzudecken.

### Hauptmerkmale

| Funktion | Beschreibung |
|---------|-------------|
| **Multithreaded** | Leistungsstarke asynchrone und Thread-Anfrage-Engine |
| **Erweiterungsplatzhalter** | Erweitern Sie `-e *`, um alle bekannten Webdateierweiterungen automatisch zu testen |
| **Integriertes Crawling** | Folgt HTML/JS-Links zum Seeding rekursiver Verzeichniswarteschlangen |
| **Umfangreiche Filterung** | Filtern Sie Antworten nach Statuscode, Bytegröße, Wortanzahl, Zeilenanzahl oder Regex |
| **Wiederholung der Rohanfrage** | Laden Sie vollständige HTTP-Anfragen der Burp Suite über `--raw` |
| **Mehrere Formate** | Erstellt Berichte in Klartext, JSON, CSV, Markdown und HTML |

---

## ⚖️ Werkzeugvergleich

| Funktion | dirsearch | ffuf | Feroxbuster | Gobuster | DIRB |
|---------|-----------|------|-------------|----------|------|
| **Sprache** | Python | Geh | Rost | Geh | C |
| **Geschwindigkeit** | ⭐⭐⭐⭐ (Schnell) | ⭐⭐⭐⭐⭐ (Extrem schnell) | ⭐⭐⭐⭐⭐ (Blitzschnell) | ⭐⭐⭐⭐ (Schnell) | ⭐⭐ (Niedriger Faden) |
| **Standardrekursion** | ✅ Automatisch (`-r`) | Optional (`-recursion`) | ✅ Automatisch (`-d`) | Optional (`-r`) | ✅ Automatisch |
| **Erweiterungsplatzhalter (`*`)**| ✅ Native (`-e *`) | ❌ Nein | ❌ Nein | ❌ Nein | ❌ Nein |
| **Integrierter Webcrawler** | ✅ Ja (`--crawl`) | ❌ Nein | ✅ Ja (`--extract-links`) | ❌ Nein | ❌ Nein |
| **POST / Daten-Fuzzzing** | ✅ Ja (`--data`) | ✅ Vollständiges HTTP | ❌ Begrenzt | ❌ Nein | ❌ Nein |
| **Burp Raw Request Replay**| ✅ Ja (`--raw`) | ✅ Ja (`-request`) | ❌ Nein | ❌ Nein | ❌ Nein |
| **Integrierte Wortliste** | ✅ Kuratiert (`db/dicc.txt`)| ❌ Keine | ❌ Keine | ❌ Keine | ✅ Kleine Liste |

---

## 📥 Installation

### Verwendung von pipx (empfohlen für Kali und modernes Linux)
```bash
# Install via pipx (isolated Python environment)
pipx install dirsearch

# Verify installation
dirsearch --version
```
### Verwenden von pip
```bash
# Install via standard pip
pip3 install dirsearch
```
### Verwenden von Git Clone (direkt aus der Quelle)
```bash
# Clone the official repository
git clone https://github.com/maurosoria/dirsearch.git --depth 1
cd dirsearch

# Install requirements and run
pip3 install -r requirements.txt
python3 dirsearch.py --version
```
### Kali Linux-Paket und Docker
```bash
# Kali / Debian repository
sudo apt update && sudo apt install -y dirsearch

# Docker container
docker run -it --rm maurosoria/dirsearch -u https://example.com -e php,html
```
---

## 🚀 Grundlegende Verwendung und Zielauswahl

### Einzelnes Ziel
```bash
# Basic scan against a single URL specifying extensions
dirsearch -u https://target.com -e php,html,js,txt

# Scan with trailing slash forced
dirsearch -u https://target.com -e php -f
```
### Mehrere Ziele
```bash
# Scan a list of targets from file
dirsearch -l targets.txt -e php,aspx,txt

# Scan stdin input from subfinder / httpx
cat live_hosts.txt | dirsearch --stdin -e php
```
### Port-Scanning und CIDR-Targeting
```bash
# Scan specific ports on a host
dirsearch -u https://target.com --ports 80,443,8080,8443 -e php

# Scan an entire subnet range
dirsearch --cidr 192.168.1.0/24 -e html
```
---

## 🧩 Erweiterungs- und Suffixverwaltung

`dirsearch` verfügt über das leistungsstärkste Erweiterungsverwaltungssystem unter allen Verzeichnisscannern:
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

## 📚 Wortlisten und Wörterbücher

`dirsearch` bündelt eine umfassende, kampferprobte Wortliste bei `db/dicc.txt`:
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

## 🔄 Rekursion und Web-Crawling

### Rekursives Scannen
```bash
# Recursively scan newly found directories
dirsearch -u https://target.com -e php -r

# Limit recursion depth to 2 levels
dirsearch -u https://target.com -e php -r --recursion-depth 2

# Recursively scan only if HTTP status is 200, 301, or 302
dirsearch -u https://target.com -e php -r --recursion-status 200,301,302
```
### Integrierter Webcrawler
```bash
# Crawl the target website for internal URLs and feed them into the scan queue
dirsearch -u https://target.com -e php --crawl
```
---

## ⚡ Threading, Geschwindigkeitsbegrenzung und Verzögerungen

Kontrollieren Sie die Scan-Aggressivität, um die Auslösung von Ratenbegrenzungs- oder Web Application Firewalls (WAF) zu verhindern:
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

## 🎯 Antworten filtern und abgleichen

### Statuscode-Einschluss und -Ausschluss
```bash
# Include only specific status codes (e.g., ignore everything except 200, 301, 403)
dirsearch -u https://target.com -e php -i 200,301,302,403

# Exclude specific status codes (e.g., exclude 404 and 500)
dirsearch -u https://target.com -e php -x 404,400,500,502,503
```
### Filtern nach Größe, Wörtern und Inhalt
```bash
# Exclude responses by specific byte size (e.g. ignore custom 404 pages)
dirsearch -u https://target.com -e php --exclude-sizes 0B,1542B,3421B

# Exclude responses matching specific text on the page
dirsearch -u https://target.com -e php --exclude-texts "Not Found","Access Denied"

# Exclude responses matching a regular expression
dirsearch -u https://target.com -e php --exclude-regex "^Error:.*"
```
---

## 🛠️ HTTP-Anforderungsanpassung

### Benutzerdefinierte Header und Benutzeragenten
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
### Cookies und Authentifizierung
```bash
# Provide session cookies for authenticated scanning
dirsearch -u https://target.com -e php -b "PHPSESSID=abc123xyz; admin_logged=true"

# HTTP Basic Authentication (username:password)
dirsearch -u https://target.com -e php --auth admin:SecretPass123 --auth-type basic

# NTLM Authentication for corporate Windows portals
dirsearch -u https://target.com -e php --auth CORP\\alice:Welcome2026 --auth-type ntlm
```
### POST-Daten und Anforderungsmethoden
```bash
# Perform fuzzing using HTTP POST requests
dirsearch -u https://target.com/api -m POST -d "action=test&token=xyz" -e json

# Replay an entire raw HTTP request saved from Burp Suite
dirsearch -u https://target.com --raw request.txt -e php
```
---

## 🛡️ Proxys, Tor & Evasion
```bash
# Route all traffic through an HTTP or SOCKS5 proxy (e.g., Burp Suite)
dirsearch -u https://target.com -e php --proxy http://127.0.0.1:8080

# Route all traffic through the local Tor SOCKS proxy
dirsearch -u https://target.com -e php --tor

# Skip SSL/TLS certificate verification
dirsearch -u https://target.com -e php --no-check-certificate
```
---

## 📄 Ausgabeformate und Berichte
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

## 🎯 Pentest- und Bug-Bounty-Rezepte aus der Praxis

### 1. Schnelle, umfassende Webaufklärung
```bash
# Scan with common web extensions, follow redirects, random UA, 40 threads
dirsearch -u https://target.com \
  -e php,html,js,json,txt,zip \
  -t 40 \
  --random-agent \
  -i 200,301,302,403 \
  -o target_recon.txt
```
### 2. Auf der Suche nach vergessenen Backups und Quelllecks
```bash
# Test for database dumps, git repos, env files, and zip archives
dirsearch -u https://target.com \
  -e bak,old,orig,zip,tar.gz,sql,env,git \
  -w /usr/share/seclists/Discovery/Web-Content/raft-medium-files.txt \
  -i 200 \
  --exclude-sizes 0B
```
### 3. Tiefgreifende rekursive Verzeichniserkennung
```bash
# 2-level recursion, crawling pages for new paths, ignoring error status codes
dirsearch -u https://target.com \
  -e php,html \
  -r --recursion-depth 2 \
  --crawl \
  -x 404,500,502 \
  -o deep_scan.json --format json
```
### 4. Authentifizierte interne Portalbewertung
```bash
# Scan through Burp Suite with internal session cookies and WAF bypass headers
dirsearch -u https://intranet.corp.local \
  -e aspx,ashx,config,txt \
  -b "ASP.NET_SessionId=xyz123; AuthToken=ey..." \
  -H "X-Forwarded-For: 127.0.0.1" \
  --proxy http://127.0.0.1:8080
```
---

## ⚡ Kurzreferenztabelle

| Aufgabe | Befehl |
|------|---------|
| **Einfacher Scan mit Erweiterungen** | `dirsearch -u https://target.com -e php,html,txt` |
| **Platzhalter für alle Erweiterungen** | `dirsearch -u https://target.com -e *` |
| **Benutzerdefinierte Wortliste** | `dirsearch -u https://target.com -e php -w /path/to/wordlist.txt` |
| **Mehrere Ziele scannen** | `dirsearch -l targets.txt -e php,aspx` |
| **Rekursive Entdeckung** | `dirsearch -u https://target.com -e php -r --recursion-depth 2` |
| **Crawling-Ziel-Links** | `dirsearch -u https://target.com -e php --crawl` |
| **Statuscodes einschließen** | `dirsearch -u https://target.com -e php -i 200,301,302,403` |
| **Statuscodes ausschließen** | `dirsearch -u https://target.com -e php -x 404,500,502` |
| **Falsch-positive Größen ausschließen**| `dirsearch -u https://target.com -e php --exclude-sizes 0B,1240B` |
| **Mit Cookies authentifizieren** | `dirsearch -u https://target.com -e php -b "session=abc123"` |
| **Route durch Burp Suite** | `dirsearch -u https://target.com -e php --proxy http://127.0.0.1:8080` |
| **Bericht in JSON speichern** | `dirsearch -u https://target.com -e php -o report.json --format json` |

---

## 💡 Tipps und Best Practices

1. **Verwenden Sie `-e *` sparsam bei großen Wortlisten:** Die Erweiterung der Platzhaltererweiterung multipliziert jedes Wort mit mehr als 20 Erweiterungen. Verwenden Sie `-e *` nur mit kleineren Listen wie `common.txt` oder `dicc.txt`.
2. **Falsch-positive Größen herausfiltern:** Moderne Web-Frameworks geben benutzerdefinierte 404-Seiten mit HTTP 200 zurück. Überprüfen Sie die Größe der Standardantwort und fügen Sie `--exclude-sizes <size>` hinzu, um Rauschen zu beseitigen.
3. **Überprüfen Sie immer verbotene 403-Pfade:** In modernen Cloud- und Unternehmensumgebungen weisen HTTP 403-Antworten darauf hin, dass das Verzeichnis oder der Endpunkt vorhanden ist. Dokumentieren Sie alle 403s für Header-Bypass- und Path-Traversal-Tests.
4. **Präfixe und Suffixe für Backups nutzen:** Entwickler speichern häufig Sicherungskopien sensibler Dateien (z. B. `config.php.bak`, `old_database.sql`). Verwenden Sie `--prefixes` und `--suffixes`, um sie automatisch zu erkennen.

---

## ⚠️ Rechtlicher Haftungsausschluss

> **WICHTIG:** Unbefugtes Port-Scannen, Verzeichnis-Brute-Forcing und Web-Enumeration gegen Server ohne ausdrückliche schriftliche Genehmigung sind illegal. Verwenden Sie `dirsearch` ausschließlich bei legalen Bug-Bounty-Einsätzen, autorisierten Penetrationstests und CTF-Laborumgebungen.