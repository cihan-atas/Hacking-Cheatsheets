<!-- language-home-navigation -->
[🏠 Home Page Italiana](../../README.it.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🕶️ mitmproxy - Cheatsheet completo

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
<palign="centro">
  <img src="https://img.shields.io/badge/mitmproxy-Terminal_Proxy-blue?style=for-the-badge" alt="mitmproxy">
  <img src="https://img.shields.io/badge/Author-mitmproxy_Project-orange?style=for-the-badge" alt="mitmproxy">
  <img src="https://img.shields.io/badge/Category-Network_&_Web_Analysis-green?style=for-the-badge" alt="Analisi di rete">
  <img src="https://img.shields.io/badge/License-MIT-purple?style=for-the-badge" alt="MIT">
</p>

<palign="centro">
  <b>🕶️ Un proxy HTTP di intercettazione interattivo, compatibile con SSL/TLS per penetration tester, ricercatori di sicurezza e ingegneri del software con una ricca interfaccia della console e API di scripting Python</b>
</p>

---

## 📋 Sommario

- [Panoramica e i tre binari](#-panoramica--i-tre-binari)
- [Installazione e configurazione del certificato](#-installation--certificate-setup)
  - [Installazione su Linux/macOS](#installazione-su-linux--macos)
  - [Installazione del certificato CA per l'intercettazione SSL/TLS](#installing-ca-certificate-for-ssltls-interception)
- [Modalità operative: normale, trasparente e proxy inverso](#-modalità-operative-regolare-trasparente--proxy inverso)
- [Navigazione TUI interattiva (`mitmproxy`)](#-interactive-tui-navigation-mitmproxy)
  - [Scorciatoie da tastiera](#scorciatoie da tastiera)
  - [Filtro e intercettazione dei flussi](#filtro--intercettazione-flussi)
- [Automazione non interattiva (`mitmdump`)](#-non-interactive-automation-mitmdump)
  - [Traffico in registrazione e riproduzione](#recording--replay-traffico)
- [Scripting e componenti aggiuntivi Python](#-python-scripting--addons)
  - [Script di manipolazione automatica dell'intestazione](#script-manipolazione-intestazione-automatica)
  - [Live Response Body Tampering](#live-response-body-tampering)
- [Tabella di riferimento rapido](#-tabella-di-riferimento-rapido)
- [Considerazioni difensive e rilevamento](#-considerazioni-difensive--rilevamento)
- [Disclaimer legale](#-legal-disclaimer)

---

## 🎯 Panoramica e i tre binari

A differenza dei ingombranti proxy desktop grafici, **mitmproxy** offre intercettazioni HTTP/1, HTTP/2 e WebSocket rapidissime, programmabili e ad alta fedeltà direttamente all'interno del terminale o della pipeline di automazione.

Viene fornito con tre file binari entry-point:
1. **`mitmproxy`:** L'interfaccia utente terminale interattiva basata su ncurses (TUI).
2. **`mitmdump`:** La versione non interattiva della riga di comando (l'equivalente `tcpdump` per HTTP).
3. **`mitmweb`:** un'interfaccia web grafica leggera basata su browser.

---

## 📥 Installazione e configurazione del certificato

### Installazione su Linux/macOS
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y mitmproxy

# Arch Linux
sudo pacman -S mitmproxy

# Python pip / pipx
pipx install mitmproxy
```
### Installazione del certificato CA per l'intercettazione SSL/TLS
Per intercettare il traffico HTTPS, le destinazioni devono fidarsi dell'autorità di certificazione dinamica di mitmproxy:
1. Avvia mitmproxy una volta: `mitmproxy` (crea certificati in `~/.mitmproxy/`).
2. Sul dispositivo/browser client, configurare il proxy `127.0.0.1:8080`.
3. Passare a `http://mitm.it` e scaricare/installare il certificato per Android, iOS, Windows o Linux.

---

## ⚙️ Modalità operative: Proxy normale, trasparente e inverso

### Modalità proxy normale (predefinita)
Ascolto proxy forward standard sulla porta 8080:
```bash
mitmproxy -p 8080
```
### Modalità proxy inverso
Inoltra il traffico in entrata a un server di destinazione specifico (ideale per il test API):
```bash
mitmproxy --mode reverse:https://api.target.com -p 8080
```
### Modalità trasparente
Intercetta il traffico instradato attraverso iptables senza configurare le impostazioni del proxy client:
```bash
mitmproxy --mode transparent --showhost
```
---

## 🖥️ Navigazione TUI interattiva (`mitmproxy`)

### Scorciatoie da tastiera
- `?`: apre la sovrapposizione della guida.
- `Enter`: ispeziona il flusso HTTP selezionato (Richiesta/Risposta/Dettagli).
- `q`: torna indietro/esci.
- `d`: cancella il flusso corrente.
- `r`: riproduce la richiesta selezionata al server.
- `e`: modifica le intestazioni, il percorso o il corpo della richiesta/risposta.
- `a`: riprende il flusso sospeso (durante l'intercettazione).
- `A`: riprende tutti i flussi sospesi.

### Filtraggio e intercettazione dei flussi
Premere `i` per inserire un'espressione di filtro di intercettazione:
```text
~u /api/v1/ & ~m POST      # Intercept only POST requests to /api/v1/
~c 401 | ~c 403            # Filter flows by HTTP status code
~b "password"              # Filter flows where body contains "password"
```
---

## ⚡ Automazione non interattiva (`mitmdump`)

### Registrazione e riproduzione del traffico
Cattura il traffico in tempo reale direttamente in un file di dump binario:
```bash
# Record all HTTP traffic to file
mitmdump -w traffic.dump

# Read and inspect captured dump
mitmdump -n -r traffic.dump

# Replay client requests from dump against target
mitmdump --client-replay traffic.dump
```
---

## 🐍 Scripting e componenti aggiuntivi Python

mitmproxy dispone di un'API Python nativa per la modifica del traffico a livello di codice:

### Script di manipolazione automatica dell'intestazione (`add_headers.py`)
```python
from mitmproxy import http

class AddHeader:
    def request(self, flow: http.HTTPFlow):
        # Inject custom authorization or testing header into every outbound request
        flow.request.headers["X-Security-Audit"] = "Authorized-Pentest"
        flow.request.headers["Authorization"] = "Bearer injected_token_xyz"

addons = [AddHeader()]
```
Corri con:
```bash
mitmproxy -s add_headers.py
```
### Manomissione corpo Live Response (`tamper_response.py`)
```python
from mitmproxy import http

def response(flow: http.HTTPFlow):
    # Force admin privilege in returned JSON response
    if "api/user/me" in flow.request.url:
        flow.response.text = flow.response.text.replace('"isAdmin": false', '"isAdmin": true')
```
Corri con:
```bash
mitmdump -s tamper_response.py
```
---

## ⚡ Tabella di consultazione rapida

| Obiettivo | Comando |
|-----------|---------|
| **Avvia interfaccia utente interattiva** | `mitmproxy -p 8080` |
| **Avvia GUI Web** | `mitmweb -p 8080` |
| **Logger CLI silenzioso** | `mitmdump -p 8080` |
| **Target proxy inverso** | `mitmproxy --mode reverse:https://target.com` |
| **Registra il traffico sul file** | `mitmdump -w capture.mitm` |
| **Carica lo script aggiuntivo Python**| `mitmproxy -s script.py` |
| **Ignora host di destinazione** | `mitmproxy --ignore-hosts ".*apple\.com"` |

---

## 🛡️ Considerazioni difensive e rilevamento

1. **Binning del certificato:** le app mobili che impongono il pinning SSL rifiutano il certificato CA di mitmproxy. Abbinalo a Frida/Obiezione per aggirare i controlli lato client.
2. **Rilevamento proxy nell'handshake TLS:** i proxy introducono suite di crittografia TLS personalizzate e impronte digitali di negoziazione ALPN che WAF e gestori di bot possono rilevare.

---

## ⚠️ Disclaimer legale

> [!ATTENZIONE]
> mitmproxy intercetta e decrittografa le comunicazioni di rete sensibili. Deve essere rigorosamente distribuito su sistemi e reti in cui è stata garantita l'autorizzazione esplicita. Intercettare le comunicazioni senza consenso è illegale.