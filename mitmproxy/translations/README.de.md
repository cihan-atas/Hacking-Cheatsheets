<!-- language-home-navigation -->
[🏠 Deutsche Startseite](../../README.de.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🕶️ mitmproxy – Vollständiger Cheatsheet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ███╗   ███╗██╗████████╗███╗   ███╗██████╗ ██████╗  ██████╗ ██╗  ██╗██╗   ██╗
  ████╗ ████║██║╚══██╔══╝████╗ ████║██╔══██╗██╔══██╗██╔═══██╗╚██╗██╔╝╚██╗ ██╔╝
  ██╔████╔██║██║   ██║   ██╔████╔██║██████╔╝██████╔╝██║   ██║ ╚███╔╝  ╚████╔╝ 
  ██║╚██╔╝██║██║   ██║   ██║╚██╔╝██║██╔═══╝ ██╔══██╗██║   ██║ ██╔██╗   ╚██╔╝  
  ██║ ╚═╝ ██║██║   ██║   ██║ ╚═╝ ██║██║     ██║  ██║╚██████╔╝██╔╝ ██╗   ██║   
  ╚═╝     ╚═╝╚═╝   ╚═╝   ╚═╝     ╚═╝╚═╝     ╚═╝  ╚═╝ ╚═════╝ ╚═╝  ╚═╝   ╚═╝   
       Interactive, Scriptable Terminal HTTP/HTTPS Proxy & Traffic Engine
```
<p align="center">
  <img src="https://img.shields.io/badge/mitmproxy-Terminal_Proxy-blue?style=for-the-badge" alt="mitmproxy">
  <img src="https://img.shields.io/badge/Author-mitmproxy_Project-orange?style=for-the-badge" alt="mitmproxy">
  <img src="https://img.shields.io/badge/Category-Network_&_Web_Analysis-green?style=for-the-badge" alt="Netzwerkanalyse">
  <img src="https://img.shields.io/badge/License-MIT-purple?style=for-the-badge" alt="MIT">
</p>

<p align="center">
  <b>🕶️ Ein interaktiver, SSL/TLS-fähiger abfangender HTTP-Proxy für Penetrationstester, Sicherheitsforscher und Softwareentwickler mit einer umfangreichen Konsolenschnittstelle und Python-Skript-API</b>
</p>

---

## 📋 Inhaltsverzeichnis

- [Übersicht und die drei Binärdateien](#-overview--the-drei-binärdateien)
- [Installation & Zertifikat-Setup](#-installation--certificate-setup)
  - [Installation unter Linux / macOS](#installing-on-linux--macos)
  - [CA-Zertifikat für SSL/TLS-Interception installieren](#installing-ca-certificate-for-ssltls-interception)
- [Betriebsmodi: Normal, Transparent und Reverse-Proxy](#-operating-modes-regular-transparent--reverse-proxy)
- [Interaktive TUI-Navigation (`mitmproxy`)](#-interactive-tui-navigation-mitmproxy)
  - [Tastaturkürzel](#keyboard-shortcuts)
  - [Flows filtern und abfangen](#filtering--intercepting-flows)
- [Nicht-interaktive Automatisierung (`mitmdump`)](#-non-interactive-automation-mitmdump)
  - [Verkehr aufzeichnen und wiedergeben](#recording--replaying-traffic)
- [Python-Scripting und Add-ons](#-python-scripting--addons)
  - [Automatisiertes Header-Manipulationsskript](#automated-header-manipulation-script)
  - [Live Response Body Tampering](#live-response-body-tampering)
- [Kurzreferenztabelle](#-quick-reference-table)
- [Verteidigungsüberlegungen und Erkennung](#-defensive-überlegungen--erkennung)
- [Rechtlicher Haftungsausschluss](#-legal-disclaimer)

---

## 🎯 Übersicht und die drei Binärdateien

Im Gegensatz zu sperrigen grafischen Desktop-Proxys bietet **mitmproxy** eine blitzschnelle, skriptfähige, originalgetreue HTTP/1-, HTTP/2- und WebSockets-Abfangfunktion direkt in Ihrem Terminal oder Ihrer Automatisierungspipeline.

Im Lieferumfang sind drei Einstiegs-Binärdateien enthalten:
1. **`mitmproxy`:** Die interaktive, ncurses-basierte Terminal-Benutzeroberfläche (TUI).
2. **`mitmdump`:** Die nicht interaktive Befehlszeilenversion (das `tcpdump`-Äquivalent für HTTP).
3. **`mitmweb`:** Eine schlanke, browserbasierte grafische Weboberfläche.

---

## 📥 Installation und Zertifikatseinrichtung

### Installation unter Linux / macOS
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y mitmproxy

# Arch Linux
sudo pacman -S mitmproxy

# Python pip / pipx
pipx install mitmproxy
```
### CA-Zertifikat für SSL/TLS-Abfangen installieren
Um HTTPS-Verkehr abzufangen, müssen Ziele der dynamischen Zertifizierungsstelle von mitmproxy vertrauen:
1. Mitmproxy einmal starten: `mitmproxy` (erstellt Zertifikate in `~/.mitmproxy/`).
2. Konfigurieren Sie auf dem Clientgerät/Browser den Proxy `127.0.0.1:8080`.
3. Navigieren Sie zu `http://mitm.it` und laden Sie das Zertifikat für Android, iOS, Windows oder Linux herunter/installieren Sie es.

---

## ⚙️ Betriebsmodi: Normal, Transparent und Reverse Proxy

### Regulärer Proxy-Modus (Standard)
Standard-Forward-Proxy, der Port 8080 überwacht:
```bash
mitmproxy -p 8080
```
### Reverse-Proxy-Modus
Leitet eingehenden Datenverkehr an einen bestimmten Zielserver weiter (ideal für API-Tests):
```bash
mitmproxy --mode reverse:https://api.target.com -p 8080
```
### Transparenter Modus
Fängt den über iptables weitergeleiteten Datenverkehr ab, ohne Client-Proxy-Einstellungen zu konfigurieren:
```bash
mitmproxy --mode transparent --showhost
```
---

## 🖥️ Interaktive TUI-Navigation (`mitmproxy`)

### Tastaturkürzel
- `?`: Hilfe-Overlay öffnen.
- `Enter`: Überprüfen Sie den ausgewählten HTTP-Fluss (Anfrage/Antwort/Detail).
- `q`: Zurück/Beenden.
- `d`: Aktuellen Fluss löschen.
- `r`: Ausgewählte Anfrage an den Server erneut abspielen.
- `e`: Anforderungs-/Antwort-Header, Pfad oder Text bearbeiten.
- `a`: Angehaltenen Fluss fortsetzen (beim Abfangen).
- `A`: Alle pausierten Flows fortsetzen.

### Flows filtern und abfangen
Drücken Sie `i`, um einen Abfangfilterausdruck einzugeben:
```text
~u /api/v1/ & ~m POST      # Intercept only POST requests to /api/v1/
~c 401 | ~c 403            # Filter flows by HTTP status code
~b "password"              # Filter flows where body contains "password"
```
---

## ⚡ Nicht-interaktive Automatisierung (`mitmdump`)

### Aufzeichnen und Wiedergeben von Datenverkehr
Erfassen Sie Live-Verkehr direkt in einer binären Dump-Datei:
```bash
# Record all HTTP traffic to file
mitmdump -w traffic.dump

# Read and inspect captured dump
mitmdump -n -r traffic.dump

# Replay client requests from dump against target
mitmdump --client-replay traffic.dump
```
---

## 🐍 Python-Skripte und Add-ons

mitmproxy verfügt über eine native Python-API zum programmgesteuerten Ändern des Datenverkehrs:

### Automatisiertes Header-Manipulationsskript (`add_headers.py`)
```python
from mitmproxy import http

class AddHeader:
    def request(self, flow: http.HTTPFlow):
        # Inject custom authorization or testing header into every outbound request
        flow.request.headers["X-Security-Audit"] = "Authorized-Pentest"
        flow.request.headers["Authorization"] = "Bearer injected_token_xyz"

addons = [AddHeader()]
```
Laufen mit:
```bash
mitmproxy -s add_headers.py
```
### Live-Response-Körpermanipulation (`tamper_response.py`)
```python
from mitmproxy import http

def response(flow: http.HTTPFlow):
    # Force admin privilege in returned JSON response
    if "api/user/me" in flow.request.url:
        flow.response.text = flow.response.text.replace('"isAdmin": false', '"isAdmin": true')
```
Laufen mit:
```bash
mitmdump -s tamper_response.py
```
---

## ⚡ Kurzreferenztabelle

| Ziel | Befehl |
|-----------|---------|
| **Interaktives TUI starten** | `mitmproxy -p 8080` |
| **Web-GUI starten** | `mitmweb -p 8080` |
| **Stiller CLI-Logger** | `mitmdump -p 8080` |
| **Reverse-Proxy-Ziel** | `mitmproxy --mode reverse:https://target.com` |
| **Datenverkehr in Datei aufzeichnen** | `mitmdump -w capture.mitm` |
| **Python-Addon-Skript laden**| `mitmproxy -s script.py` |
| **Zielhosts ignorieren** | `mitmproxy --ignore-hosts ".*apple\.com"` |

---

## 🛡️ Defensive Überlegungen und Erkennung

1. **Zertifikat-Pinning:** Mobile Apps, die SSL-Pinning erzwingen, lehnen das CA-Zertifikat von Mitmproxy ab. Koppeln Sie mit Frida / Objection, um clientseitige Überprüfungen zu umgehen.
2. **Proxy-Erkennung im TLS-Handshake:** Proxys führen benutzerdefinierte TLS-Verschlüsselungssammlungen und ALPN-Verhandlungs-Fingerabdrücke ein, die WAFs und Bot-Manager erkennen können.

---

## ⚠️ Rechtlicher Haftungsausschluss

> [!ACHTUNG]
> mitmproxy fängt sensible Netzwerkkommunikation ab und entschlüsselt sie. Es muss unbedingt auf Systemen und Netzwerken eingesetzt werden, für die eine ausdrückliche Autorisierung sichergestellt wurde. Das Abfangen von Kommunikation ohne Zustimmung ist illegal.