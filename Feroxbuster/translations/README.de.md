<!-- language-home-navigation -->
[🏠 Deutsche Startseite](../../README.de.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🦀 Feroxbuster – Vollständiger Cheatsheet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ███████╗███████╗██████╗  ██████╗ ██╗  ██╗██████╗ ██╗   ██╗███████╗████████╗███████╗██████╗ 
  ██╔════╝██╔════╝██╔══██╗██╔═══██╗╚██╗██╔╝██╔══██╗██║   ██║██╔════╝╚══██╔══╝██╔════╝██╔══██╗
  █████╗  █████╗  ██████╔╝██║   ██║ ╚███╔╝ ██████╔╝██║   ██║███████╗   ██║   █████╗  ██████╔╝
  ██╔══╝  ██╔══╝  ██╔══██╗██║   ██║ ██╔██╗ ██╔══██╗██║   ██║╚════██║   ██║   ██╔══╝  ██╔══██╗
  ██║     ███████╗██║  ██║╚██████╔╝██╔╝ ██╗██████╔╝╚██████╔╝███████║   ██║   ███████╗██║  ██║
  ╚═╝     ╚══════╝╚═╝  ╚═╝ ╚═════╝ ╚═╝  ╚═╝╚═════╝  ╚═════╝ ╚══════╝   ╚═╝   ╚══════╝╚═╝  ╚═╝
                Fast, Simple, Recursive Content Discovery
```
<p align="center">
  <img src="https://img.shields.io/badge/Feroxbuster-Content_Discovery-orange?style=for-the-badge" alt="Feroxbuster">
  <img src="https://img.shields.io/badge/Rust-Blazingly_Fast-blue?style=for-the-badge" alt="Rust">
  <img src="https://img.shields.io/badge/Auto_Tune-Recursive-green?style=for-the-badge" alt="Recursive">
  <img src="https://img.shields.io/badge/Bug_Bounty-Web_Pentest-red?style=for-the-badge" alt="Web Pentest">
</p>

<p align="center">
  <b>⚡ Ein schnelles, einfaches, rekursives Tool zur Inhaltserkennung, geschrieben in Rust</b>
</p>

---

## 📋 Inhaltsverzeichnis

- [Einführung](#-Einführung)
- [Hauptfunktionen](#-key-features)
- [Werkzeugvergleich](#-tool-comparison)
- [Installation](#-Installation)
- [Grundlegende Syntax und Schnellstart](#-basic-syntax--quick-start)
- [Kernbefehlsoptionen](#-core-command-options)
  - [Rekursionskontrolle](#recursion-control)
  - [Parallelität und Leistung](#concurrency--performance)
  - [Dateierweiterungen](#file-extensions)
  - [Filtern & Matching](#filtering--matching)
  - [Intelligente Funktionen (Auto-Tune, Link-Extraktion, Backups)](#smart-features)
  - [Header, Cookies & Auth](#headers-cookies--auth)
  - [Proxy- und Burp-Suite-Integration](#proxy--burp-suite-integration)
- [Multi-Target-Scanning und Pipelines](#-multi-target-scanning--pipelines)
- [Real-World-Workflows](#-real-world-workflows)
- [Kurzreferenztabelle](#-quick-reference-table)
- [Tipps und Best Practices](#-tips--best-practices)
- [Rechtlicher Haftungsausschluss](#-legal-disclaimer)
- [Ressourcen](#-Ressourcen)

---

## 🎯 Einführung

**Feroxbuster** ist ein leistungsstarker, rekursiver Multithread-Scanner für Webinhalte, der in Rust geschrieben wurde. Es ist schnell, einfach und intuitiv konzipiert und kombiniert die Geschwindigkeit von Go/Rust-basierten Fuzzern mit der nativen automatischen rekursiven Verzeichniserkennung.

Es glänzt bei Penetrationstests und Bug-Bounty-Einsätzen, bei denen tiefe Website-Strukturen und versteckte Verzeichnisse schnell entdeckt werden müssen, ohne dass komplexe manuelle Skripte erstellt werden müssen.

---

## 🌟 Hauptmerkmale

- ⚡ **Blitzschnell:** Unterstützt durch asynchrones Rust (`tokio` und `reqwest`), das Tausende von Anfragen pro Sekunde verarbeiten kann.
- 🔄 **Native Recursive Discovery:** Erkennt automatisch neue Verzeichnisse und durchsucht sie rekursiv bis zu konfigurierbaren Tiefengrenzen.
- 🧠 **Auto-Tune & Auto-Bail:** Drosselt automatisch die Scan-Geschwindigkeit, wenn der Ziel-Webserver langsamer wird, oder bricht ab, wenn übermäßige Verbindungsfehler auftreten.
- 🔗 **Inline-Link-Extraktion:** Analysiert HTML- und JavaScript-Antworten, um versteckte Endpunkte zu erkennen, und führt sie zurück in die Scan-Warteschlange.
- 📦 **Automatisierte Backup-Suche:** Hängt die Varianten `.bak`, `~`, `.old` und `.orig` automatisch an erkannte Dateien an.
- 🎯 **Pipeable Input:** Akzeptiert nahtlos Ziellisten von `stdin` (von Tools wie `subfinder`, `httpx` oder `gau`).

---

## ⚖️ Werkzeugvergleich

| Funktion | Feroxbuster | ffuf | dirsearch | Gobuster | Dirb |
|---------|-------------|------|-----------|----------|------|
| **Sprache** | Rost | Geh | Python | Geh | C |
| **Geschwindigkeit** | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐ |
| **Standardrekursion** | ✅ Automatisch | ❌ Manuelle Markierung | ✅ Automatisch (`-r`) | ❌ Keine | ✅ Automatisch |
| **Link-Extraktion** | ✅ Ja (`--extract-links`) | ❌ Nein | ❌ Nein | ❌ Nein | ❌ Nein |
| **Automatische Ratenanpassung** | ✅ Ja (`--auto-tune`) | ❌ Nein | ❌ Nein | ❌ Nein | ❌ Nein |
| **POST Fuzzing** | ❌ Nein | ✅ Einheimisch | ✅ Native (`--data`) | ❌ Nein | ❌ Nein |
| **Multi-Target-Stdin** | ✅ Einheimisch | ❌ Begrenzt | ✅ Ja (`-l`) | ❌ Nein | ❌ Nein |
| **Erkennung von Sicherungsdateien**| ✅ Native (`--collect-backups`)| ❌ Nein | ✅ Native (`-e *`) | ❌ Nein | ❌ Nein |

---

## 📥 Installation

### Kali Linux & Debian / Ubuntu
```bash
# Update repository and install
sudo apt update && sudo apt install -y feroxbuster
```
### Verwenden von Cargo (Rust Package Manager)
```bash
cargo install feroxbuster
```
### macOS (Homebrew)
```bash
brew install feroxbuster
```
### Docker
```bash
docker run --init -it epi052/feroxbuster -u https://target.com -w /usr/share/seclists/Discovery/Web-Content/common.txt
```
### Vorkompilierte Binärdatei (GitHub-Versionen)
```bash
# Download latest Linux release
curl -sL https://raw.githubusercontent.com/epi052/feroxbuster/main/install-nix.sh | bash
sudo mv feroxbuster /usr/local/bin/
```
---

## 🚀 Grundlegende Syntax und Schnellstart
```bash
feroxbuster -u <TARGET_URL> -w <WORDLIST> [OPTIONS]
```
### Beispiele für schnelle Scans
```bash
# Scan a single target with default settings
feroxbuster -u http://10.10.10.10

# Scan with custom SecLists directory list
feroxbuster -u https://example.com -w /usr/share/seclists/Discovery/Web-Content/directory-list-2.3-medium.txt

# Scan target including specific file extensions
feroxbuster -u https://example.com -w /usr/share/wordlists/dirb/common.txt -x php,html,txt,json
```
---

## ⚙️ Kernbefehlsoptionen

### Rekursionskontrolle
```bash
# Specify maximum recursion depth (default is 4)
feroxbuster -u https://example.com -w common.txt -d 2

# Completely disable recursion (scan root directory only)
feroxbuster -u https://example.com -w common.txt -n

# Don't scan nested subdirectories automatically (interactive prompt)
feroxbuster -u https://example.com -w common.txt --dont-scan /api,/static
```
### Parallelität und Leistung
```bash
# Set number of concurrent threads per directory (default: 50)
feroxbuster -u https://example.com -t 100

# Limit total concurrent directory scans running simultaneously (default: unlimited)
feroxbuster -u https://example.com -L 3

# Rate limit requests per second to avoid triggering WAF/firewall blocks
feroxbuster -u https://example.com --rate-limit 150

# Request timeout in seconds (default: 7)
feroxbuster -u https://example.com -T 5
```
### Dateierweiterungen
```bash
# Append extensions to each wordlist item
feroxbuster -u https://example.com -x php,asp,aspx,jsp,txt,bak

# Automatically collect and fuzz file extensions found during crawling
feroxbuster -u https://example.com --collect-extensions
```
### Filtern und Abgleichen
```bash
# Match only specific status codes (e.g., 200 OK and 301 Redirects)
feroxbuster -u https://example.com -s 200 301 302

# Filter out specific status codes (e.g., exclude 404 and 403)
feroxbuster -u https://example.com -C 404 403 500

# Filter responses matching exact size in bytes
feroxbuster -u https://example.com -S 4092 1024

# Filter responses matching exact word count
feroxbuster -u https://example.com -W 42

# Filter responses matching exact line count
feroxbuster -u https://example.com -N 12

# Filter responses matching regular expressions (e.g., generic error pages)
feroxbuster -u https://example.com --filter-regex "Under Construction|Page Not Found"

# Filter responses similar to a known 404/wildcard page
feroxbuster -u https://example.com --filter-similar-to https://example.com/definitelynotfoundpage404
```
### Intelligente Funktionen
```bash
# Auto-tune: Automatically adjusts request concurrency based on server response latency
feroxbuster -u https://example.com --auto-tune

# Auto-bail: Aborts scanning automatically when more than 100 consecutive network errors occur
feroxbuster -u https://example.com --auto-bail

# Extract links: Parses HTML and JavaScript to automatically uncover endpoints
feroxbuster -u https://example.com --extract-links

# Collect backups: Automatically tests for backup copies (.bak, ~, .old) of every discovered file
feroxbuster -u https://example.com --collect-backups
```
### Header, Cookies und Auth
```bash
# Custom HTTP Header
feroxbuster -u https://example.com -H "Authorization: Bearer <TOKEN>" -H "X-Forwarded-For: 127.0.0.1"

# Session Cookie
feroxbuster -u https://example.com -b "session=abc123xyz; role=admin"

# Custom User-Agent string
feroxbuster -u https://example.com -a "Mozilla/5.0 (Windows NT 10.0; Win64; x64)"

# Ignore invalid / self-signed TLS certificates
feroxbuster -u https://10.10.10.10 -k
```
### Proxy- und Burp-Suite-Integration
```bash
# Route all requests through Burp Suite proxy
feroxbuster -u https://example.com -p http://127.0.0.1:8080 -k

# Convenience shortcut flag for Burp Suite (enables -k and sets proxy to localhost:8080)
feroxbuster -u https://example.com --burp
```
---

## 🔁 Multi-Target-Scanning und Pipelines

Feroxbuster liest Ziel-URLs nativ aus der Standardeingabe (`stdin`) und eignet sich daher ideal für die Integration in Asset-Discovery-Pipelines:
```bash
# Pipeline: Subdomain discovery -> HTTP Probing -> Feroxbuster Fuzzing
subfinder -d example.com -silent | \
  httpx -silent -mc 200,301,302 | \
  feroxbuster --stdin -w /usr/share/seclists/Discovery/Web-Content/raft-medium-directories.txt -t 30 -L 4
```
### URLs aus einer Datei scannen
```bash
cat targets.txt | feroxbuster --stdin -w common.txt -v
```
---

## 🎬 Arbeitsabläufe in der realen Welt

### Workflow 1: Umfassende Bug Bounty Web Reconnaissance

Hohe Parallelität, automatische Ratenoptimierung, Link-Extraktion und Backup-Erkennung:
```bash
feroxbuster -u https://target.com \
  -w /usr/share/seclists/Discovery/Web-Content/directory-list-2.3-medium.txt \
  -x php,js,json,txt,html \
  --extract-links \
  --collect-backups \
  --auto-tune \
  -o ferox_target_scan.txt
```
### Workflow 2: Interne Unternehmens-/Active Directory-Portalerkennung

Scannen einer internen Webanwendung über Burp Suite mit Authentifizierung und selbstsignierten Zertifikaten:
```bash
feroxbuster -u https://internal-portal.corp.local \
  -w /usr/share/seclists/Discovery/Web-Content/common.txt \
  -x aspx,ashx,asmx,config,txt \
  -b "ASP.NET_SessionId=k2m9fl3p8..." \
  --burp \
  -d 3
```
### Workflow 3: Schnelle nicht-rekursive API- und Endpunkterkennung
```bash
feroxbuster -u https://api.target.com/v1 \
  -w /usr/share/seclists/Discovery/Web-Content/api/api-endpoints.txt \
  -n \
  -s 200,201,204,401,403 \
  -t 100 \
  --json \
  -o api_results.json
```
---

## 📊 Kurzreferenztabelle

| Flagge | Kurz | Beschreibung | Beispiel |
|------|-------|-------------|---------|
| `--url` | `-u` | Zu scannende Ziel-URL | `feroxbuster -u https://target.com` |
| `--wordlist` | `-w` | Pfad zur Wortliste | `feroxbuster -w /path/to/list.txt` |
| `--threads` | `-t` | Anzahl gleichzeitiger Threads pro Verzeichnis | `feroxbuster -t 80` |
| `--scan-limit` | `-L` | Begrenzen Sie die Gesamtzahl der gleichzeitigen Verzeichnisscans | `feroxbuster -L 3` |
| `--depth` | `-d` | Maximale Rekursionstiefe (Standard 4) | `feroxbuster -d 2` |
| `--no-recursion` | `-n` | Rekursive Verzeichnisüberprüfung deaktivieren | `feroxbuster -n` |
| `--extensions` | `-x` | Dateierweiterungen zum Anhängen | `feroxbuster -x php,html,bak` |
| `--status-codes` | `-s` | Einzuschließende Statuscodes | `feroxbuster -s 200 301 302` |
| `--filter-status` | `-C` | Auszuschließende Statuscodes | `feroxbuster -C 404 500` |
| `--filter-size` | `-S` | Auszuschließende Antwortgrößen | `feroxbuster -S 4096` |
| `--filter-words` | `-W` | Wortanzahl zum Ausschließen | `feroxbuster -W 15` |
| `--filter-lines` | `-N` | Anzahl der auszuschließenden Zeilen | `feroxbuster -N 8` |
| `--auto-tune` | Keine | Geschwindigkeit basierend auf der Latenz dynamisch anpassen | `feroxbuster --auto-tune` |
| `--auto-bail` | Keine | Scan bei übermäßigen Netzwerkfehlern abbrechen | `feroxbuster --auto-bail` |
| `--extract-links`| Keine | HTML/JS für neue Endpunkte analysieren | `feroxbuster --extract-links` |
| `--collect-backups` | Keine | Auf Backup-Varianten prüfen (`.bak`, `.old`) | `feroxbuster --collect-backups` |
| `--proxy` | `-p` | Datenverkehr über HTTP-Proxy senden | `feroxbuster -p http://127.0.0.1:8080` |
| `--burp` | Keine | Verknüpfung für `-p http://127.0.0.1:8080 -k` | `feroxbuster --burp` |
| `--stdin` | Keine | URLs aus der Standardeingabe lesen | `cat urls.txt \| feroxbuster --stdin` |
| `--output` | `-o` | Pfad der Ausgabedatei | `feroxbuster -o results.txt` |
| `--json` | Keine | Ausgabe als JSON formatieren | `feroxbuster --json -o out.json` |

---

## 💡 Tipps und Best Practices

1. **Verwenden Sie `--auto-tune` für Remote-Ziele:**
   - Anstatt die Anzahl der Threads zu erraten (`-t`), misst `--auto-tune` aktiv die Antwortzeiten des Ziels und drosselt oder beschleunigt dynamisch, um einen versehentlichen Denial-of-Service des Servers zu vermeiden.
2. **Kombinieren Sie `--extract-links` und `--collect-backups`:**
   – Diese Kombination ist bei Bug-Bounty-Bewertungen außerordentlich tödlich: Feroxbuster extrahiert versteckte JS-Routen, scannt sie und prüft sofort, ob Entwickler-Backups (`.bak`, `.swp`, `~`) auf dem Server verblieben sind.
3. **Beschränken Sie Scans auf große Wortlisten mit `-L`:**
   - Wenn Sie ein Wörterbuch mit mehr als 200.000 Wörtern verwenden, kann die uneingeschränkte Rekursion Dutzende gleichzeitiger Threads starten. Verwenden Sie `-L 2` oder `-L 3`, um Speicher und Bandbreite innerhalb sicherer Betriebsgrenzen zu halten.
4. **Verwenden Sie Statusdateien zur Wiederaufnahme großer Prüfungen:**
   - Feroxbuster speichert regelmäßig Statusdateien (`.state`). Wenn ein Scan unterbrochen wird, können Sie ihn sofort mit `feroxbuster --resume-from <FILE>.state` fortsetzen.

---

## ⚠️ Rechtlicher Haftungsausschluss

> **WICHTIG:** Feroxbuster wurde ausschließlich für autorisierte Sicherheitsbewertungen, CTF-Wettbewerbe und Penetrationstests entwickelt, die mit schriftlicher Genehmigung durchgeführt werden. Das Scannen nicht autorisierter Ziele kann die Netzwerkinfrastruktur überlasten und ist gesetzlich strengstens verboten.

---

## 📚 Ressourcen

- [Feroxbuster GitHub Repository](https://github.com/epi052/feroxbuster)
- [Offizielle Feroxbuster-Dokumentation](https://epi052.github.io/feroxbuster-docs/)
- [SecLists-Erkennungslisten](https://github.com/danielmiessler/SecLists)