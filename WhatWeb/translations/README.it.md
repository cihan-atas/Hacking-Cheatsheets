<!-- language-home-navigation -->
[🏠 Home Page Italiana](../../README.it.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🌐 WhatWeb - Cheatsheet completo

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
<palign="centro">
  <img src="https://img.shields.io/badge/WhatWeb-Web_Fingerprinter-blue?style=for-the-badge" alt="WhatWeb">
  <img src="https://img.shields.io/badge/Author-Andrew_Horton-orange?style=for-the-badge" alt="urbanadventurer">
  <img src="https://img.shields.io/badge/Category-Recon_&_Scanning-green?style=for-the-badge" alt="Ricognizione e scansione">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="Licenza GPLv2">
</p>

<palign="centro">
  <b>🌐 Scanner web di nuova generazione progettato per identificare sistemi di gestione dei contenuti (CMS), piattaforme di blog, librerie JavaScript e tecnologie server</b>
</p>

---

## 📋 Sommario

- [Panoramica e tecnologia Fingerprinting](#-panoramica--tecnologia-fingerprinting)
- [Installazione](#-installazione)
- [Fingerprinting di base del sito web](#-basic-website-fingerprinting)
- [Spiegazione dei livelli di aggressività (`-a`)](#-spiegazione-dei-livelli-di-aggressività--a)
  - [Modalità passiva furtiva (`-a 1`)](#modalità-passiva-furtiva--a-1)
  - [Modalità aggressiva (`-a 3`)](#modalità-aggressiva--a-3)
  - [Modalità pesante/bruta (`-a 4`)](#pesante--modalità bruta--a-4)
- [Scansione di sottoreti e file di destinazione](#-scanning-subnets--target-files)
- [Sistema plugin e controllo selettivo](#-plug-system--controllo-selettivo)
- [Registrazione output ed esportazione report](#-output-logging--report-exporting)
- [Tabella di riferimento rapido](#-tabella-di-riferimento-rapido)
- [Considerazioni difensive e OPSEC](#-considerazioni-difensive--opsec)
- [Disclaimer legale](#-legal-disclaimer)

---

## 🎯 Panoramica e tecnologia Fingerprinting

Prima di lanciare exploit o fuzzer a forza bruta contro un'applicazione web, è fondamentale identificarne l'esatto stack tecnologico.

Creato da **Andrew Horton (urbanadventurer)**, **WhatWeb** riconosce oltre 1.800 tecnologie web distinte:
- **Sistemi di gestione dei contenuti (CMS):** WordPress, Drupal, Joomla, Ghost, Magento.
- **Server Web e proxy:** Apache, Nginx, Microsoft-IIS, Cloudflare, Envoy.
- **Framework backend:** Laravel, Django, Ruby on Rails, ASP.NET, Spring Boot.
- **Librerie frontend:** React, Vue.js, Angular, jQuery, Bootstrap.
- **Dispositivi incorporati:** Webcam, router, stampanti, interfacce SCADA.

---

## 📥Installazione
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y whatweb

# Arch Linux
sudo pacman -S whatweb

# macOS (Homebrew)
brew install whatweb
```
---

## 🚀 Impronta digitale di base del sito web
```bash
# Scan a single domain
whatweb https://target.local

# Brief one-line output format
whatweb --color=never -q https://target.local

# Verbose detailed report showing evidence matches
whatweb -v https://target.local
```
---

## ⚡ Spiegazione dei livelli di aggressività (`-a`)

WhatWeb bilancia la furtività con la profondità utilizzando quattro livelli di aggressione:

### Modalità passiva invisibile (`-a 1`)
Invia una singola richiesta HTTP GET all'URL fornito e analizza le intestazioni di risposta, i cookie e i tag HTML restituiti senza richiedere pagine aggiuntive:
```bash
whatweb -a 1 https://target.local
```
### Modalità aggressiva (`-a 3`)
Consigliato per test di penetrazione. Segue i reindirizzamenti e richiede file di plugin comuni (ad esempio, `wp-login.php`, `robots.txt`, hash favicon):
```bash
whatweb -a 3 https://target.local
```
### Modalità Pesante/Bruta (`-a 4`)
Tenta migliaia di ipotesi di plug-in e URL di impronte digitali noti:
```bash
whatweb -a 4 https://target.local
```
---

## 🌐 Scansione di sottoreti e file di destinazione
```bash
# Scan an entire CIDR network block
whatweb 192.168.1.0/24

# Scan multiple URLs listed in a text file
whatweb -i targets.txt

# Pipe live web endpoints from httpx into WhatWeb
cat live_hosts.txt | whatweb --no-errors
```
---

## 🧩 Sistema di plug-in e controllo selettivo

WhatWeb presenta una ricca architettura di plugin:
```bash
# List all 1,800+ available plugins
whatweb -l

# Search for specific plugins (e.g., WordPress or Apache)
whatweb --grep wordpress

# Run ONLY a specific plugin against a target
whatweb --plugins="apache,php,wordpress" https://target.local
```
---

## 📊 Registrazione degli output ed esportazione dei report
```bash
# Export results to clean JSON format
whatweb https://target.local --log-json results.json

# Export to XML format
whatweb https://target.local --log-xml results.xml

# Export to compact CSV format
whatweb -i targets.txt --log-brief results.txt
```
---

## ⚡ Tabella di consultazione rapida

| Obiettivo | Comando |
|-----------|---------|
| **Scansione standard** | `whatweb <URL>` |
| **Rapporto dettagliato dettagliato** | `whatweb -v <URL>` |
| **Scansione passiva invisibile** | `whatweb -a 1 <URL>` |
| **Scansione profonda aggressiva** | `whatweb -a 3 <URL>` |
| **Scansione sottorete (CIDR)** | `whatweb <SUBNET>/24` |
| **Scansiona target da file** | `whatweb -i targets.txt` |
| **Esporta in JSON** | `whatweb <URL> --log-json report.json` |
| **Filtra per plugin specifici**| `whatweb --plugins="wordpress,nginx" <URL>` |

---

## 🛡️ Considerazioni difensive e OPSEC

1. **Mascheramento intestazioni server:** rimuovi o randomizza le intestazioni di risposta HTTP `Server` e `X-Powered-By` in Apache/Nginx per sconfiggere il rilevamento passivo delle impronte digitali dei banner.
2. **Mascheramento hash favicon:** WhatWeb utilizza gli hash MD5 di `/favicon.ico` per identificare i framework web. Personalizza le favicon del framework predefinito.

---

## ⚠️ Disclaimer legale

> [!ATTENZIONE]
> WhatWeb è destinato esclusivamente alla ricognizione autorizzata, al controllo della sicurezza e all'inventario del sistema. La scansione di reti o applicazioni Web non autorizzate senza autorizzazione può violare le leggi applicabili.