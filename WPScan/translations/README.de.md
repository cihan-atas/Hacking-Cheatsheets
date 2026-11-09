<!-- language-home-navigation -->
[🏠 Deutsche Startseite](../../README.de.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🛡️ WPScan – Vollständiger Cheatsheet

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
<p align="center">
  <img src="https://img.shields.io/badge/WPScan-WordPress_Security-blue?style=for-the-badge" alt="WPScan">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-orange?style=for-the-badge" alt="Web Security">
  <img src="https://img.shields.io/badge/Database-WPVDB_API-green?style=for-the-badge" alt="WPVDB API">
  <img src="https://img.shields.io/badge/License-GPLv3-red?style=for-the-badge" alt="License GPLv3">
</p>

<p align="center">
  <b>🌐 Black-Box-Schwachstellenscanner zur Erkennung von WordPress-Kern-, Plugin-, Theme- und Benutzerschwachstellen</b>
</p>

---

## 📋 Inhaltsverzeichnis

- [Übersicht & Architektur](#-overview--architecture)
- [Installation](#-Installation)
- [API-Token-Integration (WPVDB)](#-api-token-integration-wpvdb)
- [Basic Scan & Target Enumeration](#-basic-scan--target-enumeration)
- [Aufschlüsselung der Aufzählungsflags (`-e`)](#-enumeration-flags-breakdown--e)
  - [Vulnerable Plugins (`-e vp`)](#vulnerable-plugins--e-vp)
  - [Alle Plugins (`-e ap`)](#all-plugins--e-ap)
  - [Themen (`-e vt`, `-e at`)](#themes--e-vt--e-at)
  - [Benutzernamen (`-e u`)](#usernames--e-u)
  - [Sensible Backups (`-e cb`, `-e dbe`)](#sensitive-backups--e-cb--e-dbe)
- [Erkennungsmodi (`--plugins-detection`)](#-detection-modes---plugins-detection)
- [Password Brute-Forcing (`wp-login.php` & XML-RPC)](#-password-brute-forcing-wp-loginphp--xml-rpc)
- [Stealth & WAF-Umgehung](#-stealth--waf-evasion)
- [Kurzreferenztabelle](#-quick-reference-table)
- [Verteidigungsüberlegungen und Härtung](#-defensive-überlegungen--härtung)
- [Rechtlicher Haftungsausschluss](#-legal-disclaimer)

---

## 🎯 Übersicht & Architektur

Mit WordPress, das mehr als 40 % der Top-10-Millionen-Websites betreibt, ist es das zielgerichtetste Content-Management-System (CMS) der Welt.

**WPScan** wurde in Ruby geschrieben und ist der branchenübliche Black-Box-Schwachstellenscanner für WordPress-Installationen. Es katalogisiert aktive Kernversionen, zählt installierte Plugins und Themes auf, extrahiert registrierte administrative Benutzernamen und korreliert sie mit der **WPScan Vulnerability Database (WPVDB)**, die Tausende bekannter CVEs enthält.

---

## 📥 Installation
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

## 🔑 API-Token-Integration (WPVDB)

Um bekannte Schwachstellen und CVE-Scores anzuzeigen, benötigt WPScan ein kostenloses API-Token von [wpscan.com](https://wpscan.com/):
```bash
# Provide API token via CLI
wpscan --url https://target.local --api-token YOUR_API_TOKEN

# Or export to environment variable
export WPSCAN_API_TOKEN="YOUR_API_TOKEN"
```
---

## 🚀 Grundlegende Scan- und Zielaufzählung
```bash
# Quick non-intrusive scan
wpscan --url https://target.local

# Update the local database before scanning
wpscan --update
```
---

## 🔍 Aufschlüsselung der Aufzählungsflags (`-e`)

WPScan ermöglicht die modulare Zielaufzählung mithilfe des Flags `--enumerate` (oder `-e`):

### Anfällige Plugins (`-e vp`)
Sucht nach Plugins, die bekannte Sicherheitslücken in der WPVDB-Datenbank aufweisen:
```bash
wpscan --url https://target.local -e vp --api-token YOUR_TOKEN
```
### Alle Plugins (`-e ap`)
Sucht nach Tausenden bekannter Plugins per Wörterbuch-Brute-Force:
```bash
wpscan --url https://target.local -e ap --plugins-detection aggressive
```
### Themen (`-e vt`, `-e at`)
```bash
# Enumerate vulnerable themes
wpscan --url https://target.local -e vt

# Enumerate all installed themes
wpscan --url https://target.local -e at
```
### Benutzernamen (`-e u`)
Extrahiert registrierte Autorenbenutzernamen über REST-API-Endpunkte (`/wp-json/wp/v2/users`), Autorenarchive (`/?author=1`) und Anmeldefehlermeldungen:
```bash
wpscan --url https://target.local -e u1-10
```
### Sensible Backups (`-e cb`, `-e dbe`)
Sucht nach exponiertem `wp-config.php.bak`, Datenbank-Dumps und Backup-Archiven:
```bash
wpscan --url https://target.local -e cb,dbe
```
---

## ⚙️ Erkennungsmodi (`--plugins-detection`)

Steuert, wie gründlich WPScan nach Plugins sucht:
```bash
# Passive (Fast, inspects HTML source, script tags, style links)
wpscan --url https://target.local --plugins-detection passive

# Aggressive (Sends HTTP requests for thousands of plugin directories)
wpscan --url https://target.local --plugins-detection aggressive

# Mixed (Recommended default - passive followed by targeted active requests)
wpscan --url https://target.local --plugins-detection mixed
```
---

## 🔓 Passwort-Brute-Forcing (`wp-login.php` & XML-RPC)

Sobald gültige Benutzernamen aufgelistet sind, starten Sie gezieltes Brute-Forcing für Anmeldeinformationen:
```bash
# Brute force login using enumerated username list
wpscan --url https://target.local -U users.txt -P /usr/share/wordlists/rockyou.txt

# Password spray a single password across all users
wpscan --url https://target.local -U users.txt -p 'Winter2026!'

# Multi-threaded password attack
wpscan --url https://target.local -U admin -P passwords.txt -t 20
```
> [!NOTE]
> Wenn `xmlrpc.php` auf dem Server aktiviert ist, nutzt WPScan automatisch XML-RPC-Mehrfachaufrufmethoden, um Hunderte von Passwortkombinationen pro einzelner HTTP-Anfrage zu testen!

---

## 🥷 Stealth & WAF-Umgehung
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

## ⚡ Kurzreferenztabelle

| Ziel | Befehl |
|-----------|---------|
| **Standardscan** | `wpscan --url <URL> --api-token <TOKEN>` |
| **Anfällige Plugins aufzählen** | `wpscan --url <URL> -e vp --api-token <TOKEN>` |
| **Alle Plugins aufzählen** | `wpscan --url <URL> -e ap --plugins-detection aggressive` |
| **Benutzer aufzählen** | `wpscan --url <URL> -e u` |
| **Sicherungen und Konfigurationen aufzählen** | `wpscan --url <URL> -e cb,dbe` |
| **Passwort Brute Force** | `wpscan --url <URL> -U <USERS> -P <PASSWORDS>` |
| **Zufälliger User-Agent & Stealth** | `wpscan --url <URL> --random-user-agent --stealthy` |
| **In JSON-Datei exportieren** | `wpscan --url <URL> -f json -o wpscan_report.json` |

---

## 🛡️ Defensive Überlegungen und Abhärtung

1. **XML-RPC deaktivieren:** Blockieren Sie den Zugriff auf `xmlrpc.php` über die `.htaccess`- oder Nginx-Konfiguration, um Hochgeschwindigkeits-Multicall-Brute-Force-Angriffe zu neutralisieren.
2. **Endpunkt der REST-API-Benutzer einschränken:** Deaktivieren Sie den öffentlichen Zugriff auf `/wp-json/wp/v2/users`, um eine triviale Autorenaufzählung zu verhindern.
3. **Plugins regelmäßig patchen:** Über 90 % der WordPress-Schwachstellen liegen in Plugins und Themes von Drittanbietern und nicht im WordPress-Kern.

---

## ⚠️ Rechtlicher Haftungsausschluss

> [!ACHTUNG]
> Dieses Cheatsheet ist nur für autorisierte Penetrationstests, Schwachstellenbewertungen und die Prüfung von Webanwendungen gedacht. Das Erzwingen von Passwörtern oder das Scannen von Systemen ohne ausdrückliche Genehmigung ist illegal.