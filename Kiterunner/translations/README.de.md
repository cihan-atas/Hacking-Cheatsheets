<!-- language-home-navigation -->
[🏠 Deutsche Startseite](../../README.de.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🪁 Kiterunner – Vollständiger Cheatsheet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ██╗  ██╗██╗████████╗███████╗██████╗ ██╗   ██╗███╗   ██╗███╗   ██╗███████╗██████╗ 
  ██║ ██╔╝██║╚══██╔══╝██╔════╝██╔══██╗██║   ██║████╗  ██║████╗  ██║██╔════╝██╔══██╗
  █████╔╝ ██║   ██║   █████╗  ██████╔╝██║   ██║██╔██╗ ██║██╔██╗ ██║█████╗  ██████╔╝
  ██╔═██╗ ██║   ██║   ██╔══╝  ██╔══██╗██║   ██║██║╚██╗██║██║╚██╗██║██╔══╝  ██╔══██╗
  ██║  ██╗██║   ██║   ███████╗██║  ██║╚██████╔╝██║ ╚████║██║ ╚████║███████╗██║  ██║
  ╚═╝  ╚═╝╚═╝   ╚═╝   ╚══════╝╚═╝  ╚═╝ ╚═════╝ ╚═╝  ╚═══╝╚═╝  ╚═══╝╚══════╝╚═╝  ╚═╝
       Context-Aware API Endpoint & Route Discovery Engine by Assetnote
```
<p align="center">
  <img src="https://img.shields.io/badge/Kiterunner-API_Discovery-blue?style=for-the-badge" alt="Kiterunner">
  <img src="https://img.shields.io/badge/Author-Assetnote-orange?style=for-the-badge" alt="Assetnote">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-green?style=for-the-badge" alt="Web-Sicherheit & API">
  <img src="https://img.shields.io/badge/License-Apache_2.0-red?style=for-the-badge" alt="License Apache 2.0">
</p>

<p align="center">
  <b>🌐 Hochleistungsfähiger REST- und GraphQL-API-Route-Brute-Force-Algorithmus zur Abbildung moderner API-Architekturen mithilfe kontextbezogener Datensätze</b>
</p>

---

## 📋 Inhaltsverzeichnis

- [Überblick und das API-Erkennungsproblem](#-overview--the-api-discovery-problem)
- [Tool-Vergleich: Kiterunner vs. ffuf vs. dirsearch](#-tool-comparison-kiterunner-vs-ffuf-vs-dirsearch)
- [Installation](#-Installation)
- [Herunterladen von Assetnote Kite-Datensätzen](#-downloading-assetnote-kite-datasets)
- [Scan-APIs (`kr scan`)](#-scanning-apis-kr-scan)
- [Standard Wordlist Brute-Forcing (`kr brute`)](#-standard-wordlist-brute-forcing-kr-brute)
- [Header, Cookies und Authentifizierung](#-headers-cookies--authentication)
- [Filterung und Statuscodes](#-filtering--status-codes)
- [Erkannte Routen wiedergeben (`curl`-Ausgabe)](#-replaying-discovered-routes-curl-output)
- [Kurzreferenztabelle](#-quick-reference-table)
- [Verteidigungsüberlegungen und OPSEC](#-defensive-considerations--opsec)
- [Rechtlicher Haftungsausschluss](#-legal-disclaimer)

---

## 🎯 Überblick und das API-Erkennungsproblem

Herkömmliche Tools zur Erkennung von Webinhalten (`ffuf`, `dirsearch`, `gobuster`) verwenden flache Wörterbücher mit nur einem Wort (z. B. `admin`, `api`, `login`). Während dies für herkömmliche Dateihierarchien funktioniert, schlägt es bei modernen REST- und GraphQL-APIs fehl**.

Moderne APIs verwenden verschachtelte, mehrstufige Routen wie:
`/api/v2/organizations/{org_id}/users/export?format=json`

**Kiterunner**, erstellt von **Assetnote**, löst dieses Problem, indem es reale OpenAPI-, Swagger-, Postman- und Schemadefinitionen in kompakte Binärdatensätze (`.kite`) kompiliert. Anstatt zufällige Wörter zu erraten, rekonstruiert Kiterunner ganze hierarchische API-Aufrufstrukturen mit korrekten HTTP-Methoden (`GET`, `POST`, `PUT`, `DELETE`).

---

## ⚖️ Tool-Vergleich: Kiterunner vs. ffuf vs. dirsearch

| Funktion | Kiterunner (`kr`) | ffuf | dirsearch |
|---------|-----|------|-----------|
| **Kernzielfokus** | REST, GraphQL, OpenAPI | Allgemeines Web-Fuzzing | Verzeichnis Brute-Forcing |
| **Routenhierarchiebewusstsein**| ✅ Nativ (`.kite`-Dateien) | ❌ Flache Wortliste | ❌ Flache Wortliste |
| **HTTP-Verbaustausch** | ✅ Testet GET, POST, PUT, DELETE| ❌ Manuelle Konfiguration | ❌ Begrenzt |
| **Swagger / OpenAPI-Analyse** | ✅ Native Schemaaufnahme | ❌ Nein | ❌ Nein |
| **Geschwindigkeit und Parallelität** | ⭐⭐⭐⭐⭐ (Go Engine) | ⭐⭐⭐⭐⭐ (Go Engine) | ⭐⭐⭐⭐ (Python) |

---

## 📥 Installation
```bash
# Download precompiled binary from GitHub Releases
wget https://github.com/assetnote/kiterunner/releases/download/v1.0.2/kiterunner_1.0.2_linux_amd64.tar.gz
tar -xzvf kiterunner_1.0.2_linux_amd64.tar.gz
sudo mv kr /usr/local/bin/

# Verify installation
kr version
```
---

## 📦 Herunterladen von Assetnote Kite-Datensätzen

Kiterunner verlässt sich auf vorgefertigte `.kite`-Routendatensätze, die aus Millionen analysierter öffentlicher APIs erstellt wurden:
```bash
# Create wordlists directory
mkdir -p ~/.kiterunner && cd ~/.kiterunner

# Small dataset (~40MB, 2.5 million API routes - Recommended for fast scans)
wget https://wordlists-cdn.assetnote.io/data/kiterunner/routes-small.kite.tar.gz
tar -xzvf routes-small.kite.tar.gz

# Large dataset (~130MB, 8.5 million API routes - Exhaustive testing)
wget https://wordlists-cdn.assetnote.io/data/kiterunner/routes-large.kite.tar.gz
tar -xzvf routes-large.kite.tar.gz
```
---

## 🚀 Scan-APIs (`kr scan`)

Der Befehl `scan` verwendet `.kite`-Datensätze, um Ziele mithilfe kontextbezogener Routenhierarchien zu untersuchen:
```bash
# Scan a single target using the small dataset
kr scan https://target.local/api -w ~/.kiterunner/routes-small.kite

# Scan multiple hosts from a file with 100 concurrent workers
kr scan targets.txt -w ~/.kiterunner/routes-small.kite -c 100 -j 50

# Scan with maximum verbosity
kr scan https://target.local -w ~/.kiterunner/routes-small.kite -v
```
---

## 🔨 Standardwortliste Brute-Forcing (`kr brute`)

Wenn Sie Standard-`.txt`-Wortlisten verwenden und gleichzeitig die Vorteile der Multi-Methoden-API-Engine von Kiterunner nutzen möchten:
```bash
# Brute force standard paths using a text dictionary
kr brute https://target.local -w /usr/share/wordlists/dirb/common.txt

# Brute force with custom API prefixes
kr brute https://target.local/api/v1 -w endpoints.txt
```
---

## 🔑 Header, Cookies und Authentifizierung

APIs erfordern häufig JWTs, Bearer-Tokens oder benutzerdefinierte Organisationsheader:
```bash
# Pass custom Authorization header
kr scan https://target.local/api -w routes-small.kite -H "Authorization: Bearer eyJhbGci..."

# Pass custom API key and Cookie
kr scan https://target.local -w routes-small.kite -H "X-API-Key: secret123" -H "Cookie: session=xyz"
```
---

## 🎯 Filter- und Statuscodes
```bash
# Exclude standard error codes (404, 403, 502)
kr scan https://target.local -w routes-small.kite -x 404,403,502

# Filter out responses by length / content size
kr scan https://target.local -w routes-small.kite --filter-length 0,324
```
---

## 🔄 Erkannte Routen wiedergeben (`curl`-Ausgabe)

Kiterunner kann erkannte Routen als umsetzbare `curl`-Befehle ausgeben und so eine sofortige Reproduktion im Terminal oder in der Burp Suite ermöglichen:
```bash
# Print reproducible curl command lines for all successful matches
kr scan https://target.local -w routes-small.kite -o json | jq -r '.[] | .curl'
```
---

## ⚡ Kurzreferenztabelle

| Ziel | Befehl |
|-----------|---------|
| **Scan-API mit kleinem Datensatz** | `kr scan <URL> -w routes-small.kite` |
| **Scan-API mit großem Datensatz** | `kr scan <URL> -w routes-large.kite` |
| **Mehrere Hosts scannen** | `kr scan hosts.txt -w routes-small.kite -c 100` |
| **Brute-Force-Text-Wortliste** | `kr brute <URL> -w wordlist.txt` |
| **Mit Bearer-Token authentifizieren**| `kr scan <URL> -w routes-small.kite -H "Authorization: Bearer <TOKEN>"` |
| **Statuscodes ausschließen** | `kr scan <URL> -w routes-small.kite -x 404,403` |
| **Ausgabe in JSON-Datei** | `kr scan <URL> -w routes-small.kite -o json -oFile results.json` |

---

## 🛡️ Defensive Überlegungen und OPSEC

1. **Massives Anfragevolumen:** Das `routes-large.kite`-Wörterbuch generiert Millionen von Anfragen. Stellen Sie sicher, dass die Ratenbegrenzung (`-q 200`) aktiviert ist, wenn Sie Produktionsumgebungen testen.
2. **Zustandsverändernde Methoden:** Kiterunner sendet `POST`-, `PUT`- und `DELETE`-Anfragen. Bei nicht gehärteten Test-APIs kann dies dazu führen, dass Back-End-Datensätze versehentlich geändert oder gelöscht werden.

---

## ⚠️ Rechtlicher Haftungsausschluss

> [!ACHTUNG]
> Kiterunner ist nur für autorisierte API-Sicherheitstests und Bug-Bounty-Einsätze konzipiert. Das Fuzzing nicht autorisierter APIs ist streng illegal.