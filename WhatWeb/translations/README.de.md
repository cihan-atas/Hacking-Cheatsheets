<!-- language-home-navigation -->
[🏠 Deutsche Startseite](../../README.de.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🌐 WhatWeb – Vollständiger Cheatsheet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ██╗    ██╗██╗  ██╗ █████╗ ████████╗██╗    ██╗███████╗██████╗ 
  ██║    ██║██║  ██║██╔══██╗╚══██╔══╝██║    ██║██╔════╝██╔══██╗
  ██║ █╗ ██║███████║███████║   ██║   ██║ █╗ ██║█████╗  ██████╔╝
  ██║███╗██║██╔══██║██╔══██║   ██║   ██║███╗██║██╔══╝  ██╔══██╗
  ╚███╔███╔╝██║  ██║██║  ██║   ██║   ╚███╔███╔╝███████╗██████╔╝
   ╚══╝╚══╝ ╚═╝  ╚═╝╚═╝  ╚═╝   ╚═╝    ╚══╝╚══╝ ╚══════╝╚═════╝ 
       Next Generation Web Scanner & Technology Fingerprinter
```
<p align="center">
  <img src="https://img.shields.io/badge/WhatWeb-Web_Fingerprinter-blue?style=for-the-badge" alt="WhatWeb">
  <img src="https://img.shields.io/badge/Author-Andrew_Horton-orange?style=for-the-badge" alt="urbanadventurer">
  <img src="https://img.shields.io/badge/Category-Recon_&_Scanning-green?style=for-the-badge" alt="Recon & Scanning">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="License GPLv2">
</p>

<p align="center">
  <b>🌐 Webscanner der nächsten Generation zur Identifizierung von Content-Management-Systemen (CMS), Blogging-Plattformen, JavaScript-Bibliotheken und Servertechnologien</b>
</p>

---

## 📋 Inhaltsverzeichnis

- [Überblick & Technologie-Fingerprinting](#-overview--technology-fingerprinting)
- [Installation](#-Installation)
- [Grundlegendes Website-Fingerprinting](#-basic-website-fingerprinting)
- [Aggressionsstufen erklärt (`-a`)](#-aggression-levels-explained--a)
  - [Stealthy Passive Mode (`-a 1`)](#stealthy-passive-mode--a-1)
  - [Aggressiver Modus (`-a 3`)](#aggressive-mode--a-3)
  - [Heavy-/Brute-Modus (`-a 4`)](#heavy--brute-mode--a-4)
- [Subnetze und Zieldateien scannen](#-scanning-subnets--target-files)
- [Plugin-System und selektive Prüfung](#-plugin-system--selective-auditing)
- [Ausgabeprotokollierung und Berichtsexport](#-output-logging--report-exporting)
- [Kurzreferenztabelle](#-quick-reference-table)
- [Verteidigungsüberlegungen und OPSEC](#-defensive-considerations--opsec)
- [Rechtlicher Haftungsausschluss](#-legal-disclaimer)

---

## 🎯 Überblick & Technologie-Fingerprinting

Bevor Exploits oder Brute-Force-Fuzzer gegen eine Webanwendung gestartet werden, ist es unbedingt erforderlich, deren genauen Technologie-Stack zu identifizieren.

**WhatWeb** wurde von **Andrew Horton (urbanadventurer)** erstellt und erkennt über 1.800 verschiedene Webtechnologien:
- **Content-Management-Systeme (CMS):** WordPress, Drupal, Joomla, Ghost, Magento.
- **Webserver und Proxys:** Apache, Nginx, Microsoft-IIS, Cloudflare, Envoy.
- **Backend-Frameworks:** Laravel, Django, Ruby on Rails, ASP.NET, Spring Boot.
- **Frontend-Bibliotheken:** React, Vue.js, Angular, jQuery, Bootstrap.
- **Eingebettete Geräte:** Webcams, Router, Drucker, SCADA-Schnittstellen.

---

## 📥 Installation
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y whatweb

# Arch Linux
sudo pacman -S whatweb

# macOS (Homebrew)
brew install whatweb
```
---

## 🚀 Grundlegendes Website-Fingerprinting
```bash
# Scan a single domain
whatweb https://target.local

# Brief one-line output format
whatweb --color=never -q https://target.local

# Verbose detailed report showing evidence matches
whatweb -v https://target.local
```
---

## ⚡ Aggressionsstufen erklärt (`-a`)

WhatWeb balanciert Heimlichkeit und Tiefe mithilfe von vier Aggressionsstufen:

### Stealthy Passive Mode (`-a 1`)
Sendet eine einzelne HTTP-GET-Anfrage an die angegebene URL und analysiert die zurückgegebenen Antwortheader, Cookies und HTML-Tags, ohne zusätzliche Seiten anzufordern:
```bash
whatweb -a 1 https://target.local
```
### Aggressiver Modus (`-a 3`)
Empfohlen für Penetrationstests. Folgt Weiterleitungen und fordert gängige Plugin-Dateien an (z. B. `wp-login.php`, `robots.txt`, Favicon-Hashes):
```bash
whatweb -a 3 https://target.local
```
### Schwerer / Brute-Modus (`-a 4`)
Versucht Tausende von Plugin-Vermutungen und bekannte Fingerabdruck-URLs:
```bash
whatweb -a 4 https://target.local
```
---

## 🌐 Subnetze und Zieldateien scannen
```bash
# Scan an entire CIDR network block
whatweb 192.168.1.0/24

# Scan multiple URLs listed in a text file
whatweb -i targets.txt

# Pipe live web endpoints from httpx into WhatWeb
cat live_hosts.txt | whatweb --no-errors
```
---

## 🧩 Plugin-System und selektive Prüfung

WhatWeb verfügt über eine umfangreiche Plugin-Architektur:
```bash
# List all 1,800+ available plugins
whatweb -l

# Search for specific plugins (e.g., WordPress or Apache)
whatweb --grep wordpress

# Run ONLY a specific plugin against a target
whatweb --plugins="apache,php,wordpress" https://target.local
```
---

## 📊 Ausgabeprotokollierung und Berichtsexport
```bash
# Export results to clean JSON format
whatweb https://target.local --log-json results.json

# Export to XML format
whatweb https://target.local --log-xml results.xml

# Export to compact CSV format
whatweb -i targets.txt --log-brief results.txt
```
---

## ⚡ Kurzreferenztabelle

| Ziel | Befehl |
|-----------|---------|
| **Standardscan** | `whatweb <URL>` |
| **Ausführlicher detaillierter Bericht** | `whatweb -v <URL>` |
| **Stealth Passiv Scan** | `whatweb -a 1 <URL>` |
| **Aggressiver Tiefenscan** | `whatweb -a 3 <URL>` |
| **Subnetz scannen (CIDR)** | `whatweb <SUBNET>/24` |
| **Ziele aus Datei scannen** | `whatweb -i targets.txt` |
| **Nach JSON exportieren** | `whatweb <URL> --log-json report.json` |
| **Nach bestimmten Plugins filtern**| `whatweb --plugins="wordpress,nginx" <URL>` |

---

## 🛡️ Defensive Überlegungen und OPSEC

1. **Server-Header-Maskierung:** Entfernen oder randomisieren Sie die HTTP-Antwortheader `Server` und `X-Powered-By` in Apache/Nginx, um passives Banner-Fingerprinting zu verhindern.
2. **Favicon-Hash-Maskierung:** WhatWeb verwendet MD5-Hashes von `/favicon.ico`, um Web-Frameworks zu identifizieren. Passen Sie die Standard-Framework-Favicons an.

---

## ⚠️ Rechtlicher Haftungsausschluss

> [!ACHTUNG]
> WhatWeb ist nur für autorisierte Aufklärung, Sicherheitsüberprüfungen und Systeminventarisierung bestimmt. Das unerlaubte Scannen nicht autorisierter Netzwerke oder Webanwendungen kann gegen geltende Gesetze verstoßen.