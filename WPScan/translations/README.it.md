<!-- language-home-navigation -->
[🏠 Home Page Italiana](../../README.it.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🛡️ WPScan - Complete Cheatsheet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ██╗    ██╗██████╗ ███████╗ ██████╗ █████╗ ███╗   ██╗
  ██║    ██║██╔══██╗██╔════╝██╔════╝██╔══██╗████╗  ██║
  ██║ █╗ ██║██████╔╝███████╗██║     ███████║██╔██╗ ██║
  ██║███╗██║██╔═══╝ ╚════██║██║     ██╔══██║██║╚██╗██║
  ╚███╔███╔╝██║     ███████║╚██████╗██║  ██║██║ ╚████║
   ╚══╝╚══╝ ╚═╝     ╚══════╝ ╚═════╝╚═╝  ╚═╝╚═╝  ╚═══╝
       The WordPress Security & Vulnerability Scanner
```
<palign="centro">
  <img src="https://img.shields.io/badge/WPScan-WordPress_Security-blue?style=for-the-badge" alt="WPScan">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-orange?style=for-the-badge" alt="Sicurezza Web">
  <img src="https://img.shields.io/badge/Database-WPVDB_API-green?style=for-the-badge" alt="API WPVDB">
  <img src="https://img.shields.io/badge/License-GPLv3-red?style=for-the-badge" alt="Licenza GPLv3">
</p>

<palign="centro">
  <b>🌐 Scanner di vulnerabilità scatola nera progettato per rilevare le vulnerabilità del core, dei plugin, dei temi e degli utenti di WordPress</b>
</p>

---

## 📋 Sommario

- [Panoramica e architettura](#-panoramica--architettura)
- [Installazione](#-installazione)
- [Integrazione token API (WPVDB)](#-api-token-integration-wpvdb)
- [Scansione di base ed enumerazione target](#-basic-scan--target-enumeration)
- [Ripartizione flag di enumerazione (`-e`)](#-enumeration-flags-breakdown--e)
  - [Plugin vulnerabili (`-e vp`)](#plugin-vulnerabili--e-vp)
  - [Tutti i plugin (`-e ap`)](#all-plugins--e-ap)
  - [Temi (`-e vt`, `-e at`)](#temi--e-vt--e-at)
  - [Nomi utente (`-e u`)](#nomiutente--e-u)
  - [Backup sensibili (`-e cb`, `-e dbe`)](#sensitive-backup--e-cb--e-dbe)
- [Modalità di rilevamento (`--plugins-detection`)](#-modalità-di-rilevamento---rilevamento-plugin)
- [Forzatura bruta della password (`wp-login.php` e XML-RPC)](#-password-brute-forcing-wp-loginphp--xml-rpc)
- [Stealth ed evasione WAF](#-stealth--waf-evasione)
- [Tabella di riferimento rapido](#-tabella-di-riferimento-rapido)
- [Considerazioni difensive e rafforzamento](#-considerazioni-difensive--rinforzo)
- [Disclaimer legale](#-legal-disclaimer)

---

## 🎯 Panoramica e architettura

Con WordPress che alimenta oltre il 40% dei 10 milioni di siti web più importanti, è il sistema di gestione dei contenuti (CMS) più mirato al mondo.

Scritto in Ruby, **WPScan** è lo scanner di vulnerabilità della scatola nera standard del settore per le installazioni di WordPress. Cataloga le versioni core attive, enumera plugin e temi installati, estrae i nomi utente amministrativi registrati e li mette in correlazione con il **WPScan Vulnerability Database (WPVDB)** contenente migliaia di CVE noti.

---

## 📥Installazione
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y wpscan

# Arch Linux
sudo pacman -S wpscan

# Ruby Gem (Any platform)
gem install wpscan

# Docker Container
docker run -it --rm wpscan/wpscan --url https://target.local
```
---

## 🔑 Integrazione token API (WPVDB)

Per visualizzare le vulnerabilità note e i punteggi CVE, WPScan richiede un token API gratuito da [wpscan.com](https://wpscan.com/):
```bash
# Provide API token via CLI
wpscan --url https://target.local --api-token YOUR_API_TOKEN

# Or export to environment variable
export WPSCAN_API_TOKEN="YOUR_API_TOKEN"
```
---

## 🚀 Scansione di base ed enumerazione dei target
```bash
# Quick non-intrusive scan
wpscan --url https://target.local

# Update the local database before scanning
wpscan --update
```
---

## 🔍 Ripartizione dei flag di enumerazione (`-e`)

WPScan consente l'enumerazione modulare dei target utilizzando il flag `--enumerate` (o `-e`):

### Plugin vulnerabili (`-e vp`)
Esegue la scansione dei plug-in che presentano vulnerabilità di sicurezza note nel database WPVDB:
```bash
wpscan --url https://target.local -e vp --api-token YOUR_TOKEN
```
### Tutti i plugin (`-e ap`)
Cerca migliaia di plugin conosciuti tramite la forza bruta del dizionario:
```bash
wpscan --url https://target.local -e ap --plugins-detection aggressive
```
### Temi (`-e vt`, `-e at`)
```bash
# Enumerate vulnerable themes
wpscan --url https://target.local -e vt

# Enumerate all installed themes
wpscan --url https://target.local -e at
```
### Nomi utente (`-e u`)
Estrae i nomi utente degli autori registrati tramite endpoint API REST (`/wp-json/wp/v2/users`), archivi degli autori (`/?author=1`) e messaggi di errore di accesso:
```bash
wpscan --url https://target.local -e u1-10
```
### Backup sensibili (`-e cb`, `-e dbe`)
Cerca `wp-config.php.bak` esposto, dump del database e archivi di backup:
```bash
wpscan --url https://target.local -e cb,dbe
```
---

## ⚙️ Modalità di rilevamento (`--plugins-detection`)

Controlla la precisione con cui WPScan ricerca i plugin:
```bash
# Passive (Fast, inspects HTML source, script tags, style links)
wpscan --url https://target.local --plugins-detection passive

# Aggressive (Sends HTTP requests for thousands of plugin directories)
wpscan --url https://target.local --plugins-detection aggressive

# Mixed (Recommended default - passive followed by targeted active requests)
wpscan --url https://target.local --plugins-detection mixed
```
---

## 🔓 Forzatura bruta della password (`wp-login.php` e XML-RPC)

Una volta enumerati i nomi utente validi, avvia la forzatura bruta delle credenziali mirate:
```bash
# Brute force login using enumerated username list
wpscan --url https://target.local -U users.txt -P /usr/share/wordlists/rockyou.txt

# Password spray a single password across all users
wpscan --url https://target.local -U users.txt -p 'Winter2026!'

# Multi-threaded password attack
wpscan --url https://target.local -U admin -P passwords.txt -t 20
```
> [!NOTA]
> Se `xmlrpc.php` è abilitato sul server, WPScan sfrutta automaticamente i metodi multichiamata XML-RPC per testare centinaia di combinazioni di password per singola richiesta HTTP!

---

## 🥷 Stealth ed evasione WAF
```bash
# Randomize User-Agent header to evade simple bot signatures
wpscan --url https://target.local --random-user-agent

# Throttle request rate (milliseconds between requests) to bypass rate limits
wpscan --url https://target.local --throttle 1000

# Route through SOCKS proxy / Tor
wpscan --url https://target.local --proxy socks5://127.0.0.1:9050

# Ignore SSL/TLS certificate warnings
wpscan --url https://target.local --disable-tls-checks
```
---

## ⚡ Tabella di consultazione rapida

| Obiettivo | Comando |
|-----------|---------|
| **Scansione standard** | `wpscan --url <URL> --api-token <TOKEN>` |
| **Elencare i plugin vulnerabili** | `wpscan --url <URL> -e vp --api-token <TOKEN>` |
| **Enumera tutti i plugin** | `wpscan --url <URL> -e ap --plugins-detection aggressive` |
| **Elencare gli utenti** | `wpscan --url <URL> -e u` |
| **Enumera backup e configurazioni** | `wpscan --url <URL> -e cb,dbe` |
| **Password Forza Bruta** | `wpscan --url <URL> -U <USERS> -P <PASSWORDS>` |
| **Agente utente casuale e Stealth** | `wpscan --url <URL> --random-user-agent --stealthy` |
| **Esporta in file JSON** | `wpscan --url <URL> -f json -o wpscan_report.json` |

---

## 🛡️ Considerazioni difensive e rafforzamento

1. **Disabilita XML-RPC:** Blocca l'accesso a `xmlrpc.php` tramite la configurazione `.htaccess` o Nginx per neutralizzare gli attacchi di forza bruta multichiamata ad alta velocità.
2. **Limita endpoint utenti API REST:** Disabilita l'accesso pubblico a `/wp-json/wp/v2/users` per impedire un'enumerazione banale degli autori.
3. **Atch plugin regolarmente:** Oltre il 90% delle vulnerabilità di WordPress risiedono in plugin e temi di terze parti anziché nel core di WordPress.

---

## ⚠️ Disclaimer legale

> [!ATTENZIONE]
> Questo cheatsheet è destinato esclusivamente ai test di penetrazione autorizzati, alle valutazioni delle vulnerabilità e al controllo delle applicazioni web. La forzatura brutale delle password o dei sistemi di scansione senza autorizzazione esplicita è illegale.