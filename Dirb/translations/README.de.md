<!-- language-home-navigation -->
[🏠 Deutsche Startseite](../../README.de.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 📁 DIRB – Vollständiger Cheatsheet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ██████╗ ██╗██████╗ ██████╗ 
  ██╔══██╗██║██╔══██╗██╔══██╗
  ██║  ██║██║██████╔╝██████╔╝
  ██║  ██║██║██╔══██╗██╔══██╗
  ██████╔╝██║██║  ██║██████╔╝
  ╚═════╝ ╚═╝╚═╝  ╚═╝╚═════╝ 
        Web Content Scanner
```
<p align="center">
  <img src="https://img.shields.io/badge/DIRB-Content_Scanner-orange?style=for-the-badge" alt="DIRB">
  <img src="https://img.shields.io/badge/Wordlists-Bruteforce-blue?style=for-the-badge" alt="Wordlists">
  <img src="https://img.shields.io/badge/Kali_Linux-Preinstalled-green?style=for-the-badge" alt="Kali Linux">
  <img src="https://img.shields.io/badge/Web-Pentesting-red?style=for-the-badge" alt="Web Pentesting">
</p>

<p align="center">
  <b>🔍 Klassischer Web-Content-Scanner, der wörterbuchbasierte Angriffe gegen Webserver startet</b>
</p>

---

## 📋 Inhaltsverzeichnis

- [Einführung](#-Einführung)
- [DIRB vs. moderne Alternativen](#-dirb-vs-modern-alternatives)
- [Installation](#-Installation)
- [Standardwortlisten](#-default-wordlists)
- [Grundlegende Syntax](#-grundlegende Syntax)
- [Häufige Scan-Szenarien](#-common-scanning-scenarios)
- [Erweiterte Optionen und Tuning](#-advanced-options--tuning)
- [Schwachstellensuche mit integrierten Listen](#-vulnerability-hunting-with-built-in-lists)
- [Real-World-Workflows](#-real-world-workflows)
- [Kurzreferenztabelle](#-quick-reference-table)
- [Tipps und Best Practices](#-tips--best-practices)
- [Rechtlicher Haftungsausschluss](#-legal-disclaimer)
- [Ressourcen](#-Ressourcen)

---

## 🎯 Einführung

**DIRB** (Directory Buster) ist ein klassischer, leichter, URL-basierter Web-Content-Scanner, der in C geschrieben ist. Er startet wörterbuchbasierte Angriffe gegen Webserver und sucht nach vorhandenen und versteckten Webobjekten (Dateien, Verzeichnisse, Skripte und Endpunkte).

DIRB generiert HTTP-Anfragen für jedes Wort in einer Wortliste, analysiert die Antwortcodes des Servers (200, 301, 302, 403 usw.) und ermittelt, ob eine Ressource vorhanden ist.

### Hauptmerkmale

- ✅ **Automatisches rekursives Scannen:** Scannt erkannte Unterverzeichnisse standardmäßig automatisch.
- ✅ **Integrierte spezialisierte Wortlisten:** Wird mit gezielten Wortlisten für gängige Webserver, CMSs und Schwachstellen geliefert.
- ✅ **Geringer Ressourcenbedarf:** Leichte Binärdatei, die auch auf Hardware mit geringer Spezifikation oder älteren Systemen reibungslos läuft.
- ✅ **Benutzerdefinierte Header- und Proxy-Unterstützung:** Einfache Integration mit Burp Suite und Upstream-HTTP-Proxys.

---

## ⚖️ DIRB vs. moderne Alternativen

| Funktion | DIRB | Gobuster | ffuf | Feroxbuster | dirsearch |
|---------|------|----------|------|-------------|-----------|
| **Sprache** | C | Geh | Geh | Rost | Python |
| **Geschwindigkeit** | ⭐⭐ (Niedriger Faden) | ⭐⭐⭐⭐⭐ (Schnell) | ⭐⭐⭐⭐⭐ (Extrem schnell) | ⭐⭐⭐⭐⭐ (Async Rust) | ⭐⭐⭐⭐ (Schnell) |
| **Standardrekursion** | ✅ Ja (Automatisch) | ❌ Nein | ❌ Nein (Optional `-recursion`) | ✅ Ja (Automatisch) | ✅ Ja (`-r`) |
| **Benutzerdefinierte Wortlisten enthalten** | ✅ Ja (`/usr/share/dirb/`) | ❌ Nein | ❌ Nein | ❌ Nein | ✅ Ja (`db/dicc.txt`) |
| **VHost-Fuzzing** | ❌ Nein | ✅ Ja | ✅ Ja | ❌ Nein | ✅ Ja (`--subdirs`) |
| **POST / Daten-Fuzzzing** | ❌ Nein | ❌ Nein | ✅ Ja | ❌ Nein | ✅ Ja (`-d`) |
| **Speicher-/CPU-Auslastung** | Sehr niedrig | Niedrig | Niedrig | Sehr niedrig | Mäßig |

---

## 📥 Installation

### Kali Linux & Parrot OS

DIRB ist in Kali Linux und Parrot OS vorinstalliert:
```bash
# Verify installation
dirb

# Update to latest package
sudo apt update && sudo apt install -y dirb
```
### Debian / Ubuntu
```bash
sudo apt update
sudo apt install -y dirb
```
### Kompilieren aus dem Quellcode
```bash
# Install build dependencies
sudo apt install -y build-essential libcurl4-openssl-dev

# Download and extract source
wget https://sourceforge.net/projects/dirb/files/dirb/2.22/dirb222.tar.gz
tar -xzvf dirb222.tar.gz
cd dirb222

# Configure permissions and compile
chmod -R 755 conf
chmod 755 configure
./configure
make
sudo make install
```
---

## 📚 Standardwortlisten

Bei der Installation unter Kali Linux enthält DIRB dedizierte Wortlisten unter `/usr/share/dirb/wordlists/`:

| Wortlistenpfad | Einträge | Zweck |
|---------------|---------|---------|
| `/usr/share/dirb/wordlists/common.txt` | ~4.614 | Standardliste, die verwendet wird, wenn keine Wortliste angegeben ist |
| `/usr/share/dirb/wordlists/small.txt` | ~959 | Schnelle Entdeckung von Pfaden mit hoher Wahrscheinlichkeit |
| `/usr/share/dirb/wordlists/big.txt` | ~20.469 | Umfassende Verzeichnis- und Dateisuche |
| `/usr/share/dirb/wordlists/catala.txt` | ~6.846 | Katalanische Wortliste |
| `/usr/share/dirb/wordlists/spanish.txt` | ~11.720 | Spanische Wortliste |
| `/usr/share/dirb/wordlists/mutations_common.txt` | ~1.000 | Häufige Präfix-/Suffix-Variationen |
| `/usr/share/dirb/wordlists/extensions_common.txt` | ~40 | Standard-Weberweiterungen (`.php`, `.asp`, `.html` usw.) |

### Integrierte Schwachstellen-Wortlisten (`/usr/share/dirb/wordlists/vulns/`)

- `apache.txt` – Apache-spezifische Dateien, Statusseiten und bekannte Verzeichnisse
- `iis.txt` – Microsoft IIS-Webserverdateien (`web.config`, `aspnet_client` usw.)
- `tomcat.txt` – Apache Tomcat-Verwaltungs- und Managerpfade
- `cgis.txt` – Common Gateway Interface (CGI)-Skripte und Endpunkte
- `fatwire.txt`, `hpsmh.txt`, `hyperion.txt`, `iplanet.txt`, `netware.txt`, `oracle.txt`

---

## ⌨️ Grundlegende Syntax
```bash
dirb <url_base> [<wordlist_file(s)>] [options]
```
### Wesentliche Beispiele
```bash
# Basic scan using default common.txt wordlist
dirb http://10.10.10.10

# Scan using a specific custom wordlist
dirb https://example.com /usr/share/wordlists/dirb/big.txt

# Scan with custom SecLists directory list
dirb http://target.local /usr/share/seclists/Discovery/Web-Content/directory-list-2.3-medium.txt
```
---

## 🔍 Häufige Scan-Szenarien

### 1. Scannen mit Dateierweiterungen (`-X` und `-x`)

Suchen Sie nach bestimmten Dateierweiterungen, die jedem Wort im Wörterbuch beigefügt sind:
```bash
# Test specific extensions on every wordlist entry
dirb http://target.local -X .php,.html,.txt,.bak

# Use an extensions list file
dirb http://target.local -x /usr/share/dirb/wordlists/extensions_common.txt

# Combine directory brute-force with extension brute-force
dirb http://target.local /usr/share/dirb/wordlists/common.txt -X .php,.json,.zip
```
### 2. Rekursion steuern (`-r` und `-R`)

Standardmäßig durchsucht DIRB jedes erkannte Verzeichnis rekursiv. Sie können dieses Verhalten steuern:
```bash
# Disable recursion completely (scan root level only)
dirb http://target.local -r

# Interactive recursion (DIRB asks before descending into each directory)
dirb http://target.local -R
```
### 3. Scannen ohne Berücksichtigung der Groß- und Kleinschreibung (`-i`)

Nützlich bei der Ausrichtung auf Windows-/IIS-Server, bei denen bei Dateipfaden die Groß-/Kleinschreibung nicht berücksichtigt wird:
```bash
dirb http://target.local -i
```
### 4. Ignorieren spezifischer HTTP-Statuscodes (`-N`)

Filtern Sie unerwünschte Antwortcodes heraus (z. B. Umleitungsschleifen oder verbotene Pfade):
```bash
# Ignore 302 redirects and 403 forbidden responses
dirb http://target.local -N 302,403
```
---

## ⚙️ Erweiterte Optionen und Tuning

### Authentifizierung (`-u`)

DIRB unterstützt HTTP Basic- und Digest-Authentifizierung:
```bash
# Supply HTTP authentication credentials
dirb http://target.local -u "admin:SecretP@ssw0rd!"

# Combine auth with custom wordlist
dirb http://target.local /usr/share/dirb/wordlists/big.txt -u "pentester:Summer2026"
```
### Benutzerdefinierte Cookies und Sitzungstoken (`-c`)

Testen Sie authentifizierte Webanwendungsrouten, indem Sie Sitzungscookies bereitstellen:
```bash
# Pass session cookie
dirb http://target.local -c "PHPSESSID=d92j4fn82mdf82ncb184; logged_in=true"
```
### Benutzerdefinierte User-Agent- und HTTP-Header (`-a` und `-H`)

Umgehen Sie grundlegende WAF-Regeln oder simulieren Sie legitime Browser:
```bash
# Set custom User-Agent
dirb http://target.local -a "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36"

# Add custom HTTP Header
dirb http://target.local -H "Authorization: Bearer eyJhbGciOi..."
dirb http://target.local -H "X-Forwarded-For: 127.0.0.1"
```
### Proxy-Routing und Burp-Suite-Integration (`-p` und `-P`)

Senden Sie DIRB-Verkehr über einen Upstream-HTTP-Proxy wie Burp Suite oder OWASP ZAP:
```bash
# Route through local Burp Suite proxy
dirb http://target.local -p 127.0.0.1:8080

# Authenticated proxy
dirb http://target.local -p 10.10.10.1:3128 -P "proxyuser:proxypass"
```
### Ratenbegrenzung und Umgehung (`-z`)

Vermeiden Sie das Auslösen von IDS/IPS-Warnungen oder das DoSing fragiler Webdienste, indem Sie eine Verzögerung zwischen den Anfragen hinzufügen (in Millisekunden):
```bash
# Wait 250 milliseconds between each request
dirb http://target.local -z 250

# Slow, stealthy scan (1 second delay)
dirb http://target.local -z 1000
```
### Ausgabeprotokollierung (`-o` und `-w`)
```bash
# Save results to a file
dirb http://target.local -o /home/zeus/scans/dirb_target.txt

# Do not halt on WARNING messages (useful for uninterrupted scans)
dirb http://target.local -w -o dirb_output.txt
```
---

## 🎯 Schwachstellensuche mit integrierten Listen

DIRB enthält in `/usr/share/dirb/wordlists/vulns/` gezielte Listen zur Erkennung ungepatchter Server und gefährlicher Endpunkte:
```bash
# Target Apache server vulnerabilities and status pages
dirb http://target.local /usr/share/dirb/wordlists/vulns/apache.txt

# Target IIS endpoints and misconfigurations
dirb http://target.local /usr/share/dirb/wordlists/vulns/iis.txt

# Target Tomcat management consoles
dirb http://target.local /usr/share/dirb/wordlists/vulns/tomcat.txt

# Search for old CGI scripts and shellshock entrypoints
dirb http://target.local /usr/share/dirb/wordlists/vulns/cgis.txt
```
---

## 🎬 Arbeitsabläufe in der realen Welt

### Arbeitsablauf 1: Schnelle Ersterkundung

Wenn Sie zum ersten Mal auf einen Webserver auf Port 80/443 stoßen:
```bash
# Rapid scan without recursion using small wordlist
dirb http://10.10.10.50 /usr/share/dirb/wordlists/small.txt -r
```
### Workflow 2: Umfassende Webanwendungsbewertung

Führen Sie einen Tiefenscan durch und suchen Sie nach Konfigurationssicherungen, Quellcode und API-Endpunkten:
```bash
dirb https://app.target.local /usr/share/dirb/wordlists/common.txt \
  -X .php,.bak,.sql,.config,.txt \
  -a "Mozilla/5.0" \
  -w \
  -o dirb_app_scan.txt
```
### Workflow 3: Authentifizierter Portal-Crawl über Burp Suite
```bash
dirb https://internal.target.local/portal/ /usr/share/dirb/wordlists/big.txt \
  -c "session=s%3A_kJw81..." \
  -p 127.0.0.1:8080 \
  -X .jsp,.do,.action \
  -z 100
```
---

## 📊 Kurzreferenztabelle

| Flagge | Argument | Beschreibung | Beispiel |
|------|----------|-------------|---------|
| `-a` | `<user_agent>` | Geben Sie eine benutzerdefinierte User-Agent-Zeichenfolge | an `dirb http://target -a "Mozilla/5.0"` |
| `-c` | `<cookie>` | HTTP-Cookie-Header festlegen | `dirb http://target -c "id=123"` |
| `-H` | `<header>` | Benutzerdefinierten HTTP-Header hinzufügen | `dirb http://target -H "X-API-Key: secret"` |
| `-i` | Keine | Suche ohne Berücksichtigung der Groß-/Kleinschreibung | `dirb http://target -i` |
| `-N` | `<codes>` | Antworten mit Statuscode ignorieren | `dirb http://target -N 302,403` |
| `-o` | `<output_file>` | Scan-Ergebnisse in Datei speichern | `dirb http://target -o results.txt` |
| `-p` | `<proxy:port>` | HTTP-Proxy verwenden | `dirb http://target -p 127.0.0.1:8080` |
| `-P` | `<user:pass>` | Anmeldeinformationen für die Proxy-Authentifizierung | `dirb http://target -p proxy:8080 -P u:p` |
| `-r` | Keine | Nicht rekursiv suchen | `dirb http://target -r` |
| `-R` | Keine | Interaktive rekursive Suche | `dirb http://target -R` |
| `-u` | `<user:pass>` | HTTP Basic/Digest-Authentifizierung | `dirb http://target -u "admin:1234"` |
| `-w` | Keine | Stoppen Sie den Scan nicht bei WARNUNG-Meldungen | `dirb http://target -w` |
| `-X` | `<ext_list>` | Durch Kommas getrennte Erweiterungen anhängen | `dirb http://target -X .php,.html` |
| `-x` | `<ext_file>` | Erweiterungen aus Datei anhängen | `dirb http://target -x ext.txt` |
| `-z` | `<millisecs>` | Verzögerung zwischen Anfragen hinzufügen | `dirb http://target -z 250` |

---

## 💡 Tipps und Best Practices

1. **Achten Sie auf Wildcard 200/302-Antworten:**
   – Einige Webanwendungen geben für jede Anfrage HTTP 200 oder 302 zurück (benutzerdefinierter 404-Handler). DIRB meldet jeden einzelnen Eintrag als gefunden. Verwenden Sie `-N 302` oder wechseln Sie zu `ffuf` / `feroxbuster` für erweiterte Größen-/Wortfilterung.
2. **Kombinieren Sie es mit `-r` für große Wörterbücher:**
   - Wenn Sie `directory-list-2.3-medium.txt` (220.000 Wörter) verwenden, deaktivieren Sie immer die Rekursion mit `-r`. Andernfalls kann es Tage dauern, bis DIRB tief verschachtelte Verzeichnisse durchforstet.
3. **Erweiterungen immer überprüfen:**
   – Moderne Backends legen häufig sensible Dateien wie `.env`, `.git`, `.bak`, `.conf` oder `.sql` offen. Testen Sie gängige Erweiterungen immer mit `-X`.
4. **Verwenden Sie `-w` für unbeaufsichtigte Scans:**
   – Standardmäßig kann DIRB pausieren, wenn es auf SSL- oder Netzwerkwarnungen stößt. Das Bestehen von `-w` stellt sicher, dass die Tests ohne manuelle Interaktion abgeschlossen werden.

---

## ⚠️ Rechtlicher Haftungsausschluss

> **WICHTIG:** DIRB ist für Sicherheitsüberprüfungen, Penetrationstests und ethische Forschung auf Systemen konzipiert, die Sie besitzen oder über eine ausdrückliche, dokumentierte Erlaubnis zum Testen verfügen. Unbefugte Wörterbuchangriffe auf Webserver können zu Dienstunterbrechungen führen und sind gemäß der weltweiten Computerkriminalitätsgesetzgebung illegal.

---

## 📚 Ressourcen

- [DIRB SourceForge-Projekt](https://sourceforge.net/projects/dirb/)
- [Kali Linux DIRB-Paketdokumentation](https://www.kali.org/tools/dirb/)
- [SecLists – Web Content Discovery-Wortlisten](https://github.com/danielmiessler/SecLists)