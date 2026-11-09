<!-- language-home-navigation -->
[🏠 Deutsche Startseite](../../README.de.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 📦 Schmuggler – Vollständiger Cheatsheet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ███████╗███╗   ███╗██╗   ██╗ ██████╗  ██████╗ ██╗     ███████╗██████╗ 
  ██╔════╝████╗ ████║██║   ██║██╔════╝ ██╔════╝ ██║     ██╔════╝██╔══██╗
  ███████╗██╔████╔██║██║   ██║██║  ███╗██║  ███╗██║     █████╗  ██████╔╝
  ╚════██║██║╚██╔╝██║██║   ██║██║   ██║██║   ██║██║     ██╔══╝  ██╔══██╗
  ███████║██║ ╚═╝ ██║╚██████╔╝╚██████╔╝╚██████╔╝███████╗███████╗██║  ██║
  ╚══════╝╚═╝     ╚═╝ ╚═════╝  ╚═════╝  ╚═════╝ ╚══════╝╚══════╝╚═╝  ╚═╝
             High-Performance HTTP Request Smuggling Scanner
```
<p align="center">
  <img src="https://img.shields.io/badge/smuggler-HTTP_Smuggling-blue?style=for-the-badge" alt="smuggler">
  <img src="https://img.shields.io/badge/Author-Evan_Ricaurte-orange?style=for-the-badge" alt="defparam">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-green?style=for-the-badge" alt="Web Security">
  <img src="https://img.shields.io/badge/License-MIT-purple?style=for-the-badge" alt="MIT">
</p>

<p align="center">
  <b>📦 Ultraschnelles, modulares Python-Tool zur Erkennung und Überprüfung von Schwachstellen beim Schmuggeln von HTTP-Anfragen (CL.TE, TE.CL und Header-Mutation-Desyncs) in der modernen Web-Infrastruktur</b>
</p>

---

## 📋 Inhaltsverzeichnis

- [Überblick & HTTP-Schmuggel-Theorie](#-overview--http-smuggling-theory)
  - [CL.TE vs. TE.CL-Diskrepanzen](#clte-vs-tecl-discrepancies)
- [Installation](#-Installation)
- [Grundlegendes Zielscannen](#-basic-target-scanning)
- [Scan-Konfigurationen und Mutations-Engines](#-scan-configurations--mutation-engines)
  - [Standard vs. erschöpfender Scan](#default-vs-exhaustive-scan)
  - [Benutzerdefinierte Header und Cookies](#custom-headers--cookies)
- [Multi-Target- und Pipeline-Scanning](#-multi-target--pipeline-scanning)
- [Ergebnisse und Ausnutzungspfade analysieren](#-analyzing-results--exploitation-paths)
  - [Cache-Vergiftung durch Schmuggel](#cache-poisoning-via-smuggling)
  - [Umgehen von Front-End-Sicherheitskontrollen](#bypassing-front-end-security-controls)
  - [Anfragen anderer Benutzer kapern](#hijacking-other-users-requests)
- [Kurzreferenztabelle](#-quick-reference-table)
- [Verteidigungsüberlegungen und Abhilfemaßnahmen](#-defensive-überlegungen--remediation)
- [Rechtlicher Haftungsausschluss](#-legal-disclaimer)

---

## 🎯 Übersicht und HTTP-Schmuggel-Theorie

HTTP Request Smuggling tritt auf, wenn ein Front-End-Proxy/Load Balancer und ein Back-End-Webserver sich über die Grenzen einer HTTP-Anfrage nicht einig sind:

### CL.TE vs. TE.CL Diskrepanzen
- **CL.TE:** Front-End verwendet `Content-Length`, Back-End verwendet `Transfer-Encoding: chunked`.
- **TE.CL:** Front-End verwendet `Transfer-Encoding: chunked`, Back-End verwendet `Content-Length`.
- **TE.TE:** Beide unterstützen `Transfer-Encoding`, aber man kann durch Header-Verschleierung (z. B. `Transfer-Encoding: xchunked`, Tabulatoren oder Zeilenfaltung) dazu verleitet werden, es zu ignorieren.

**Smuggler** wurde von **defparam (Evan Ricaurte)** erstellt und fügt sorgfältig gestaltete Nutzlasten ein, um Zeitverzögerungen oder Desynchronisierungsreaktionen zu beobachten, ohne die Stabilität des Backend-Dienstes zu beeinträchtigen.

---

## 📥 Installation
```bash
# Clone the repository
git clone https://github.com/defparam/smuggler.git
cd smuggler

# Run using Python 3
python3 smuggler.py -h
```
---

## 🚀 Grundlegendes Zielscannen

Scannen Sie einen einzelnen Webanwendungsendpunkt:
```bash
# Basic scan against a single target
python3 smuggler.py -u https://target.com/
```
Standardmäßig testet der Schmuggler das Verhalten von `CL.TE` und `TE.CL` mithilfe sicherer Timing-Payloads.

---

## ⚙️ Scan-Konfigurationen und Mutations-Engines

### Standard- oder umfassender Scan
Smuggler umfasst verschiedene Nutzlastprofile:
- **Standardprofil:** Schnelle, häufige Desync-Mutationen.
- **Umfassendes Profil:** Umfassende Mutationen, die Whitespace-Variationen, mehrere Header und verschleierte Transfer-Codierungs-Header testen:
```bash
# Run exhaustive scan testing all mutation gadgets
python3 smuggler.py -u https://target.com/ -c exhaustive
```
### Benutzerdefinierte Header und Cookies
Wenn Authentifizierung oder bestimmte Host-Header erforderlich sind:
```bash
python3 smuggler.py -u https://target.com/api/ \
  -H "Cookie: session=xyz123" \
  -H "X-Forwarded-Host: target.com"
```
---

## 🌐 Multi-Target- und Pipeline-Scanning

Von `httpx` oder `subfinder` entdeckte Endpunkte direkt an den Schmuggler weiterleiten:
```bash
# Scan a list of URLs from file
python3 smuggler.py -f live_hosts.txt -t 10

# Adjust timeout per probe (default: 10s)
python3 smuggler.py -u https://target.com/ --timeout 5
```
---

## 💥 Analyse von Ergebnissen und Verwertungspfaden

Wenn der Schmuggler eine positive Übereinstimmung meldet:
```text
[CRITICAL] CL.TE Desync Detected: https://target.com/
```
### Cache-Vergiftung durch Schmuggel
Einschleusen einer Anfrage, die eine Off-Site-Weiterleitung generiert (z. B. Host-Header-Manipulation), um Web-Caches zu vergiften, die allen Benutzern JavaScript-Bibliotheken bereitstellen.

### Umgehen von Front-End-Sicherheitskontrollen
Wenn der Front-End-Proxy `/admin` blockiert, schmuggeln Sie eine sekundäre interne Anfrage, die das Back-End direkt ohne Proxy-Inspektion erreicht:
```http
POST / HTTP/1.1
Host: target.com
Content-Length: 48
Transfer-Encoding: chunked

0

GET /admin HTTP/1.1
Host: localhost
```
### Die Anfragen anderer Benutzer kapern
Ein Präfix einer HTTP-Anfrage einschleusen. Wenn der nächste Benutzer seine Anfrage sendet, wird diese an Ihre geschmuggelte Anfrage angehängt, wodurch seine Cookies und Sitzungstoken offengelegt werden.

---

## ⚡ Kurzreferenztabelle

| Ziel | Befehl |
|-----------|---------|
| **Schnelles Einzelziel** | `python3 smuggler.py -u https://target.com/` |
| **Umfassender Tiefenscan**| `python3 smuggler.py -u https://target.com/ -c exhaustive` |
| **Zielliste scannen** | `python3 smuggler.py -f targets.txt -t 15` |
| **Benutzerdefinierte Header/Cookies**| `python3 smuggler.py -u <URL> -H "Cookie: sess=123"` |
| **Benutzerdefiniertes Zeitlimit** | `python3 smuggler.py -u <URL> --timeout 15` |

---

## 🛡️ Defensive Überlegungen und Abhilfemaßnahmen

1. **HTTP/2 oder HTTP/3 End-to-End erzwingen:** HTTP/2 definiert eindeutige Frame-Längen und eliminiert so Mehrdeutigkeiten im Header.
2. **HTTP-Header normalisieren:** Konfigurieren Sie Reverse-Proxys, um Anfragen abzulehnen, die sowohl `Content-Length` als auch `Transfer-Encoding` enthalten.
3. **Mehrdeutige Mutationen löschen:** Anfragen mit verschleierten `Transfer-Encoding`-Headern löschen (z. B. Leerzeichen vor Doppelpunkt, mehrere Werte).

---

## ⚠️ Rechtlicher Haftungsausschluss

> [!ACHTUNG]
> Das Schmuggeln von HTTP-Anfragen kann Live-Backend-Warteschlangen unterbrechen und gemeinsam genutzte Caching-Knoten vergiften. Schmuggler darf ausschließlich auf ausdrücklich autorisierten Testzielen mit strikter Genehmigung der Asset-Eigentümer verwendet werden.