<!-- language-home-navigation -->
[🏠 Deutsche Startseite](../../README.de.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# Sherlock-Spickzettel

[Sherlock](https://github.com/sherlock-project/sherlock) ist ein OSINT-Aufklärungstool, das Social-Media-Konten anhand des Benutzernamens auf mehr als 400 sozialen Plattformen, Foren, Code-Hosting-Sites und Online-Communities gleichzeitig aufspürt.

---

## Inhaltsverzeichnis

- [Schlüsselfunktionen](#key-capabilities)
- [Installation](#installation)
- [Einzel- und Mehrfachbenutzernamensuche](#single--multiple-username-search)
- [Sites und Plattformen filtern](#filtering-sites--platforms)
- [Proxy- und Tor-Routing](#proxy--tor-routing)
- [Timeout & Threading-Tuning](#timeout--threading-tuning)
- [Formate exportieren (CSV, XLSX, JSON)](#export-formats-csv-xlsx-json)
- [Automatisierung und OSINT-Pipeline-Integration](#automation--osint-pipeline-integration)

---

## Schlüsselfunktionen

- **Über 400 Plattformen:** Abfragen bei GitHub, Twitter/X, Instagram, Reddit, TikTok, LinkedIn, Telegram, Discord, Steam, Spotify, Docker Hub, Pastebin und Hunderten mehr.
- **Asynchrone Parallelität:** Paralleles Hochgeschwindigkeitsscannen mit asynchronen Python-Anfragen.
- **Genaue Erkennung:** Erkennt HTTP-Statuscodes, Fehlermeldungen und Weiterleitungen, um Fehlalarme zu vermeiden.
- **Tor- und SOCKS5-Unterstützung:** Schützt die Anonymität des Ermittlers und umgeht Ratenbeschränkungen.

---

## Installation
```bash
# Recommended: Install via pipx
pipx install sherlock-project

# Or install from GitHub repository
git clone https://github.com/sherlock-project/sherlock.git
cd sherlock
python3 -m pip install -r requirements.txt

# Debian / Kali Linux repository
sudo apt-get update && sudo apt-get install -y sherlock

# Verify command
sherlock --version
```
---

## Suche nach einem oder mehreren Benutzernamen

### 1. Einfache Benutzernamensuche
Suchen Sie auf allen über 400 Websites nach einem einzelnen Ziel-Handle:
```bash
sherlock target_user
```
### 2. Suchen Sie gleichzeitig nach mehreren Benutzernamen
```bash
sherlock target_user1 target_user2 alias_dev
```
### 3. Nur gefundene Konten drucken (Nicht gefundene/Fehler unterdrücken)
```bash
sherlock --print-found target_user
```
---

## Websites und Plattformen filtern

Grenzen Sie die Suche auf bestimmte Plattformen ein, um Zeit zu sparen oder sich auf bestimmte Informationskategorien zu konzentrieren:

### 1. Nur bestimmte Websites abfragen
```bash
sherlock --site github --site twitter --site reddit --site gitlab target_user
```
### 2. Suche ohne bestimmte Plattformen (falsch positive schwere Websites ignorieren)
```bash
sherlock target_user --site-list targets.txt
```
---

## Proxy- und Tor-Routing

Um IP-Verbote und Ratenbegrenzungen zu verhindern und die Betriebssicherheit aufrechtzuerhalten:

### 1. Route durch das Tor-Netzwerk
Anfragen automatisch über den lokalen Tor SOCKS-Proxy (`127.0.0.1:9050`) weiterleiten:
```bash
sherlock --tor target_user
```
### 2. Benutzerdefinierter HTTP-/SOCKS5-Proxy
```bash
# SOCKS5 proxy
sherlock --proxy socks5://127.0.0.1:1080 target_user

# Authenticated HTTP proxy
sherlock --proxy http://user:pass@proxy.corp.internal:8080 target_user
```
---

## Zeitüberschreitung und Threading-Optimierung

Steuerung der Anforderungsgeschwindigkeiten und Latenztoleranzen:
```bash
# Set HTTP timeout to 5 seconds per site (default is usually 60s)
sherlock target_user --timeout 5

# Add delay between requests to avoid rate limits
sherlock target_user --delay 1
```
---

## Exportformate (CSV, XLSX, JSON)

Speichern Sie Entdeckungen für Untersuchungen, Beweisbündel oder Berichte:

### 1. Direkt in einer CSV-Datei speichern
```bash
sherlock --csv --output /tmp/sherlock_results target_user
```
### 2. Speichern Sie die Ausgabe in einem bestimmten Verzeichnis
```bash
sherlock --folderoutput /tmp/recon_data/ target_user
```
Erzeugt `/tmp/recon_data/target_user.txt` mit vollständigen URLs bestätigter Profile.

### 3. Als Excel-Tabelle speichern (XLSX)
```bash
sherlock --xlsx target_user
```
---

## Automatisierung und OSINT-Pipeline-Integration

Verketten Sie Sherlock in einem automatisierten Ermittlerskript:
```bash
#!/usr/bin/env bash
TARGET="$1"
OUTDIR="/tmp/osint_${TARGET}"
mkdir -p "$OUTDIR"

echo "[*] Scanning for username: $TARGET"
sherlock --print-found --timeout 10 --folderoutput "$OUTDIR" "$TARGET"

if [ -f "$OUTDIR/${TARGET}.txt" ]; then
    echo "[+] Found $(wc -l < "$OUTDIR/${TARGET}.txt") accounts:"
    cat "$OUTDIR/${TARGET}.txt"
fi
```