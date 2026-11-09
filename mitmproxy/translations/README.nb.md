<!-- language-home-navigation -->
[🏠 Norsk Hovedside](../../README.nb.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇬🇧 English](../README.md)

---


# 🕶️ mitmproxy - Komplett jukseark

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
  <img src="https://img.shields.io/badge/Category-Network_&_Web_Analysis-green?style=for-the-badge" alt="Nettverksanalyse">
  <img src="https://img.shields.io/badge/License-MIT-purple?style=for-the-badge" alt="MIT">
</p>

<p align="center">
  <b>🕶️ En interaktiv, SSL/TLS-kompatibel avskjærende HTTP-proxy for penetrasjonstestere, sikkerhetsforskere og programvareingeniører med et rikt konsollgrensesnitt og Python scripting API</b>
</p>

---

## 📋 Innholdsfortegnelse

- [Oversikt og de tre binærene](#-oversikt--de-tre-binærene)
- [Installasjon og sertifikatoppsett](#-installasjon--sertifikat-oppsett)
  - [Installerer på Linux / macOS](#installing-on-linux--macos)
  - [Installerer CA-sertifikat for SSL/TLS-avskjæring](#installing-ca-certificate-for-ssltls-interception)
- [Operating Modes: Regular, Transparent & Reverse Proxy](#-driftsmoduser-regular-transparent--reverse-proxy)
- [Interactive TUI Navigation (`mitmproxy`)](#-interactive-tui-navigation-mitmproxy)
  - [Tastatursnarveier](#keyboard-snarveier)
  - [Filtrering og avskjæring av strømmer](#filtrering--avskjærer-strømmer)
- [Non-Interactive Automation (`mitmdump`)](#-non-interactive-automation-mitmdump)
  - [Recording & Replay Traffic](#recording--replaying-traffic)
- [Python-skripting og tillegg](#-python-skripting--tillegg)
  - [Automated Header Manipulation Script](#automated-header-manipulation-script)
  – [Live Response Body Tampering](#live-response-body-tampering)
- [Hurtigreferansetabell](#-hurtigreferansetabell)
– [Defensive Considerations & Detection](#-defensive-considerations--detection)
– [Juridisk ansvarsfraskrivelse](#-juridisk ansvarsfraskrivelse)

---

## 🎯 Oversikt og de tre binærene

I motsetning til store grafiske stasjonære proxyer, leverer **mitmproxy** lynrask, skriptbar, full-fidelity HTTP/1, HTTP/2 og WebSockets-avlytting direkte inne i terminalen eller automatiseringsrørledningen.

Den leveres med tre inngangspunktbinærfiler:
1. **`mitmproxy`:** Det interaktive, ncurses-baserte terminalbrukergrensesnittet (TUI).
2. **`mitmdump`:** Den ikke-interaktive kommandolinjeversjonen (`tcpdump`-ekvivalenten for HTTP).
3. **`mitmweb`:** Et lett nettleserbasert grafisk nettgrensesnitt.

---

## 📥 Installasjon og sertifikatoppsett

### Installerer på Linux / macOS
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y mitmproxy

# Arch Linux
sudo pacman -S mitmproxy

# Python pip / pipx
pipx install mitmproxy
```
### Installerer CA-sertifikat for SSL/TLS-avskjæring
For å avskjære HTTPS-trafikk må mål stole på mitmproxys dynamiske sertifiseringsinstans:
1. Start mitmproxy én gang: `mitmproxy` (oppretter sertifikater i `~/.mitmproxy/`).
2. Konfigurer proxy `127.0.0.1:8080` på klientenheten/nettleseren.
3. Bla til `http://mitm.it` og last ned/installer sertifikatet for Android, iOS, Windows eller Linux.

---

## ⚙️ Driftsmoduser: Vanlig, gjennomsiktig og omvendt proxy

### Vanlig proxy-modus (standard)
Standard fremadrettet proxy-lytting på port 8080:
```bash
mitmproxy -p 8080
```
### Omvendt proxy-modus
Videresender innkommende trafikk til en spesifikk målserver (ideelt for API-testing):
```bash
mitmproxy --mode reverse:https://api.target.com -p 8080
```
### Transparent modus
Avskjærer trafikk rutet gjennom iptables uten å konfigurere klientproxy-innstillinger:
```bash
mitmproxy --mode transparent --showhost
```
---

## 🖥️ Interaktiv TUI-navigasjon (`mitmproxy`)

### Tastatursnarveier
- `?`: Åpne hjelpeoverlegg.
- `Enter`: Inspiser valgt HTTP-flyt (Request / Response / Detail).
- `q`: Gå tilbake / avslutt.
- `d`: Slett gjeldende flyt.
- `r`: Spill av valgt forespørsel på nytt til serveren.
- `e`: Rediger forespørsels-/svarhoder, bane eller brødtekst.
- `a`: Gjenoppta stoppet flyt (ved avskjæring).
- `A`: Gjenoppta alle stoppede flyter.

### Filtrering og avskjæring av strømmer
Trykk `i` for å angi et avskjæringsfilteruttrykk:
```text
~u /api/v1/ & ~m POST      # Intercept only POST requests to /api/v1/
~c 401 | ~c 403            # Filter flows by HTTP status code
~b "password"              # Filter flows where body contains "password"
```
---

## ⚡ Ikke-interaktiv automatisering (`mitmdump`)

### Ta opp og spille av trafikk
Fang live trafikk direkte inn i en binær dumpfil:
```bash
# Record all HTTP traffic to file
mitmdump -w traffic.dump

# Read and inspect captured dump
mitmdump -n -r traffic.dump

# Replay client requests from dump against target
mitmdump --client-replay traffic.dump
```
---

## 🐍 Python-skripting og tillegg

mitmproxy har en innebygd Python API for å endre trafikk programmatisk:

### Automated Header Manipulation Script (`add_headers.py`)
```python
from mitmproxy import http

class AddHeader:
    def request(self, flow: http.HTTPFlow):
        # Inject custom authorization or testing header into every outbound request
        flow.request.headers["X-Security-Audit"] = "Authorized-Pentest"
        flow.request.headers["Authorization"] = "Bearer injected_token_xyz"

addons = [AddHeader()]
```
Kjør med:
```bash
mitmproxy -s add_headers.py
```
### Live Response Body Tampering (`tamper_response.py`)
```python
from mitmproxy import http

def response(flow: http.HTTPFlow):
    # Force admin privilege in returned JSON response
    if "api/user/me" in flow.request.url:
        flow.response.text = flow.response.text.replace('"isAdmin": false', '"isAdmin": true')
```
Kjør med:
```bash
mitmdump -s tamper_response.py
```
---

## ⚡ Hurtigreferansetabell

| Mål | Kommando |
|----------------|--------|
| **Start Interactive TUI** | `mitmproxy -p 8080` |
| **Start Web GUI** | `mitmweb -p 8080` |
| **Silent CLI Logger** | `mitmdump -p 8080` |
| **Omvendt proxy-mål** | `mitmproxy --mode reverse:https://target.com` |
| **Ta opp trafikk til fil** | `mitmdump -w capture.mitm` |
| **Last Python Addon Script**| `mitmproxy -s script.py` |
| **Ignorer målverter** | `mitmproxy --ignore-hosts ".*apple\.com"` |

---

## 🛡️ Defensive vurderinger og gjenkjenning

1. **Sertifikatfesting:** Mobilapper som håndhever SSL-festing avviser mitmproxys CA-sertifikat. Par med Frida / protest mot å omgå kontroller på klientsiden.
2. **Proxy-deteksjon i TLS-håndtrykk:** Proxyer introduserer tilpassede TLS-chiffersuiter og ALPN-forhandlingsfingeravtrykk som WAF-er og bot-administratorer kan oppdage.

---

## ⚠️ Juridisk ansvarsfraskrivelse

> [!FORSIKTIG]
> mitmproxy fanger opp og dekrypterer sensitiv nettverkskommunikasjon. Det må strengt tatt distribueres på systemer og nettverk der eksplisitt autorisasjon er sikret. Å avskjære kommunikasjon uten samtykke er ulovlig.