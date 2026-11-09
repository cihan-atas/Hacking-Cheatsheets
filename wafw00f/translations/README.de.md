<!-- language-home-navigation -->
[🏠 Deutsche Startseite](../../README.de.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# wafw00f Cheatsheet

[wafw00f](https://github.com/EnableSecurity/wafw00f) ist ein Open-Source-Befehlszeilen-Fingerabdruck- und Erkennungstool der Web Application Firewall (WAF). Es identifiziert und erfasst über 150 kommerzielle und Open-Source-WAF-Sicherheitslösungen, die Zielwebsites schützen, indem es normale und provokative HTTP-Anfragen sendet und Verhaltensantworten, Header und Cookies analysiert.

---

## Inhaltsverzeichnis

- [Warum Fingerabdruck-WAFs?](#why-fingerprint-wafs)
- [Installation](#installation)
- [Basis- und Zielscannen](#basic--target-scanning)
- [Umfassende Multi-WAF-Erkennung (`-a`)](#exhaustive-multi-waf-detection--a)
- [Massen-Zielscannen aus Datei (`-i`)](#bulk-target-scanning-from-file--i)
- [Proxy- und Tor-Routing (`-p`)](#proxy--tor-routing--p)
- [Formate exportieren (JSON, CSV, Text)](#export-formats-json-csv-text)
- [Unterstützte WAF-Signaturen auflisten (`-l`)](#list-supported-waf-signatures--l)
- [Integration von Bug Bounty und Penetrationstests](#bug-bounty--penetration-testing-integration)

---

## Warum Fingerabdruck-WAFs?

Das Starten aktiver Webangriffe (SQL-Injection, XSS, SSRF) gegen ein geschütztes Ziel ohne vorherige Aufklärung führt häufig zu sofortigen IP-Sperren oder falsch-negativen Schwachstellenergebnissen. Die Identifizierung der genauen WAF ermöglicht Folgendes:
- **Maßgeschneiderte Umgehung:** Auswahl herstellerspezifischer Umgehungstechniken (z. B. Unicode-Normalisierung für ModSecurity, Chunked Transfer Encoding für Cloudflare).
- **Ursprungs-IP-Suche:** Wenn Cloudflare oder Akamai erkannt wird, wird vor dem Testen nach der Backend-Ursprungs-IP gesucht.
- **Regelschwellenwertbewusstsein:** Optimierung der Scanner-Parallelität und Ratengrenzen unterhalb der Blockierungsschwellenwerte.

---

## Installation
```bash
# Debian / Ubuntu / Kali Linux
sudo apt-get update && sudo apt-get install -y wafw00f

# Recommended: Install via pipx
pipx install wafw00f

# Via standard Python pip
python3 -m pip install --user wafw00f

# Verify installation
wafw00f --version
```
---

## Einfaches und gezieltes Scannen

### 1. Scannen Sie eine einzelne Domain
```bash
wafw00f https://example.com
```
*wafw00f sendet grundlegende HTTP-Anfragen, gefolgt von maßgeschneiderten Schadmustern, um Sicherheitsfilter auszulösen und Signaturen in Headern, Statuscodes und Fehlerseiten zu identifizieren.*

### 2. Ausführlicher Ausgabemodus (`-v` / `-vv`)
Überprüfen Sie bestimmte übereinstimmende Header, HTTP-Statuscodes und Heuristiken:
```bash
wafw00f https://example.com -v
```
### 3. Weiterleitungsverfolgung deaktivieren (`-r`)
Verhindern Sie, dass wafw00f 301/302-Weiterleitungen folgt, um Edge-Reverse-Proxys von Backend-Anwendungen zu trennen:
```bash
wafw00f https://example.com -r
```
---

## Umfassende Multi-WAF-Erkennung (`-a`)

Standardmäßig stoppt wafw00f den Scanvorgang, sobald die erste WAF-Übereinstimmung bestätigt wird. Verwenden Sie `-a`, um alle bekannten Anbietersignaturen zu überprüfen (wichtig, wenn eine Anwendung hinter einer mehrstufigen Verteidigung wie Cloudflare CDN + AWS WAF steht):
```bash
wafw00f https://example.com -a
```
---

## Massenzielscan aus Datei (`-i`)

Scannen Sie eine gesamte Liste von Subdomains oder Ziel-Assets, die von `subfinder` oder `amass` erfasst wurden:
```bash
wafw00f -i /path/to/domains.txt -o /tmp/waf_results.txt
```
---

## Proxy- und Tor-Routing (`-p`)

Leiten Sie prüfenden Datenverkehr über Proxys weiter, um IP-Blacklists zu verhindern oder Abfragen in Burp Suite zu überprüfen:

### 1. Route über den Burp Suite-Proxy
```bash
wafw00f https://example.com -p http://127.0.0.1:8080
```
### 2. Route über den lokalen Tor SOCKS5-Proxy
```bash
wafw00f https://example.com -p socks5://127.0.0.1:9050
```
---

## Exportformate (JSON, CSV, Text)

Exportieren Sie Ergebnisse für automatisierte Berichte oder nachgelagerte Aufklärungstools:

### 1. In das JSON-Format exportieren
```bash
wafw00f https://example.com -o results.json --format json
```
### 2. Export in eine CSV-Tabelle
```bash
wafw00f -i targets.txt -o results.csv --format csv
```
---

## Unterstützte WAF-Signaturen auflisten (`-l`)

Alle über 150 nachweisbaren WAF-Produkte anzeigen:
```bash
wafw00f -l
```
Zu den wichtigsten unterstützten Produkten gehören:
- **Cloud-Anbieter:** Cloudflare, AWS WAF, Akamai, Microsoft Azure Web Application Firewall, Google Cloud Armor.
- **Enterprise Appliances:** F5 BIG-IP ASM, Imperva SecureSphere, Barracuda, Fortinet FortiWeb, Citrix NetScaler.
- **Open Source / Software:** ModSecurity, NAXSI, Shadow Daemon, OpenResty.

---

## Integration von Bug Bounty und Penetrationstests

Integrieren Sie wafw00f in eine automatisierte Web-Angriffs-Pipeline:
```bash
#!/usr/bin/env bash
TARGET="https://app.target.corp"

echo "[*] Detecting WAF for $TARGET..."
WAF=$(wafw00f "$TARGET" -o - --format json | jq -r '.[0].detected')

if [ "$WAF" == "true" ]; then
    WAF_NAME=$(wafw00f "$TARGET" -o - --format json | jq -r '.[0].firewall')
    echo "[!] Active WAF detected: $WAF_NAME"
    echo "[+] Adjusting sqlmap to use vendor-specific tamper scripts..."
    if [[ "$WAF_NAME" =~ "Cloudflare" ]]; then
        sqlmap -u "$TARGET/item?id=1" --tamper=between,randomcase,space2comment
    fi
else
    echo "[+] No active WAF detected. Proceeding with standard scanner."
    sqlmap -u "$TARGET/item?id=1" --batch
fi
```