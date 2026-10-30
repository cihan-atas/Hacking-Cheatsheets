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

<p align="center">
  <img src="https://img.shields.io/badge/WPScan-WordPress_Security-blue?style=for-the-badge" alt="WPScan">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-orange?style=for-the-badge" alt="Web Security">
  <img src="https://img.shields.io/badge/Database-WPVDB_API-green?style=for-the-badge" alt="WPVDB API">
  <img src="https://img.shields.io/badge/License-GPLv3-red?style=for-the-badge" alt="License GPLv3">
</p>

<p align="center">
  <b>🌐 Black box vulnerability scanner designed to detect WordPress core, plugin, theme, and user vulnerabilities</b>
</p>

---

## 📋 Table of Contents

- [Overview & Architecture](#-overview--architecture)
- [Installation](#-installation)
- [API Token Integration (WPVDB)](#-api-token-integration-wpvdb)
- [Basic Scan & Target Enumeration](#-basic-scan--target-enumeration)
- [Enumeration Flags Breakdown (`-e`)](#-enumeration-flags-breakdown--e)
  - [Vulnerable Plugins (`-e vp`)](#vulnerable-plugins--e-vp)
  - [All Plugins (`-e ap`)](#all-plugins--e-ap)
  - [Themes (`-e vt`, `-e at`)](#themes--e-vt--e-at)
  - [Usernames (`-e u`)](#usernames--e-u)
  - [Sensitive Backups (`-e cb`, `-e dbe`)](#sensitive-backups--e-cb--e-dbe)
- [Detection Modes (`--plugins-detection`)](#-detection-modes---plugins-detection)
- [Password Brute-Forcing (`wp-login.php` & XML-RPC)](#-password-brute-forcing-wp-loginphp--xml-rpc)
- [Stealth & WAF Evasion](#-stealth--waf-evasion)
- [Quick Reference Table](#-quick-reference-table)
- [Defensive Considerations & Hardening](#-defensive-considerations--hardening)
- [Legal Disclaimer](#-legal-disclaimer)

---

## 🎯 Overview & Architecture

With WordPress powering more than 40% of the top 10 million websites, it is the most targeted Content Management System (CMS) in the world.

Written in Ruby, **WPScan** is the industry-standard black box vulnerability scanner for WordPress installations. It catalogues active core versions, enumerates installed plugins and themes, extracts registered administrative usernames, and correlates them against the **WPScan Vulnerability Database (WPVDB)** containing thousands of known CVEs.

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

## 🔑 API Token Integration (WPVDB)

To display known vulnerabilities and CVE scores, WPScan requires a free API token from [wpscan.com](https://wpscan.com/):

```bash
# Provide API token via CLI
wpscan --url https://target.local --api-token YOUR_API_TOKEN

# Or export to environment variable
export WPSCAN_API_TOKEN="YOUR_API_TOKEN"
```

---

## 🚀 Basic Scan & Target Enumeration

```bash
# Quick non-intrusive scan
wpscan --url https://target.local

# Update the local database before scanning
wpscan --update
```

---

## 🔍 Enumeration Flags Breakdown (`-e`)

WPScan allows modular target enumeration using the `--enumerate` (or `-e`) flag:

### Vulnerable Plugins (`-e vp`)
Scans for plugins that have known security vulnerabilities in the WPVDB database:
```bash
wpscan --url https://target.local -e vp --api-token YOUR_TOKEN
```

### All Plugins (`-e ap`)
Probes for thousands of known plugins via dictionary brute-force:
```bash
wpscan --url https://target.local -e ap --plugins-detection aggressive
```

### Themes (`-e vt`, `-e at`)
```bash
# Enumerate vulnerable themes
wpscan --url https://target.local -e vt

# Enumerate all installed themes
wpscan --url https://target.local -e at
```

### Usernames (`-e u`)
Extracts registered author usernames through REST API endpoints (`/wp-json/wp/v2/users`), author archives (`/?author=1`), and login error messages:
```bash
wpscan --url https://target.local -e u1-10
```

### Sensitive Backups (`-e cb`, `-e dbe`)
Searches for exposed `wp-config.php.bak`, database dumps, and backup archives:
```bash
wpscan --url https://target.local -e cb,dbe
```

---

## ⚙️ Detection Modes (`--plugins-detection`)

Controls how thoroughly WPScan searches for plugins:

```bash
# Passive (Fast, inspects HTML source, script tags, style links)
wpscan --url https://target.local --plugins-detection passive

# Aggressive (Sends HTTP requests for thousands of plugin directories)
wpscan --url https://target.local --plugins-detection aggressive

# Mixed (Recommended default - passive followed by targeted active requests)
wpscan --url https://target.local --plugins-detection mixed
```

---

## 🔓 Password Brute-Forcing (`wp-login.php` & XML-RPC)

Once valid usernames are enumerated, launch targeted credential brute-forcing:

```bash
# Brute force login using enumerated username list
wpscan --url https://target.local -U users.txt -P /usr/share/wordlists/rockyou.txt

# Password spray a single password across all users
wpscan --url https://target.local -U users.txt -p 'Winter2026!'

# Multi-threaded password attack
wpscan --url https://target.local -U admin -P passwords.txt -t 20
```

> [!NOTE]
> If `xmlrpc.php` is enabled on the server, WPScan automatically leverages XML-RPC multicall methods to test hundreds of password combinations per single HTTP request!

---

## 🥷 Stealth & WAF Evasion

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

## ⚡ Quick Reference Table

| Objective | Command |
|-----------|---------|
| **Standard Scan** | `wpscan --url <URL> --api-token <TOKEN>` |
| **Enumerate Vulnerable Plugins** | `wpscan --url <URL> -e vp --api-token <TOKEN>` |
| **Enumerate All Plugins** | `wpscan --url <URL> -e ap --plugins-detection aggressive` |
| **Enumerate Users** | `wpscan --url <URL> -e u` |
| **Enumerate Backups & Configs** | `wpscan --url <URL> -e cb,dbe` |
| **Password Brute Force** | `wpscan --url <URL> -U <USERS> -P <PASSWORDS>` |
| **Random User-Agent & Stealth** | `wpscan --url <URL> --random-user-agent --stealthy` |
| **Export to JSON File** | `wpscan --url <URL> -f json -o wpscan_report.json` |

---

## 🛡️ Defensive Considerations & Hardening

1. **Disable XML-RPC:** Block access to `xmlrpc.php` via `.htaccess` or Nginx configuration to neutralize high-speed multicall brute-force attacks.
2. **Restrict REST API Users Endpoint:** Disable public access to `/wp-json/wp/v2/users` to prevent trivial author enumeration.
3. **Patch Plugins Regularly:** Over 90% of WordPress vulnerabilities reside in third-party plugins and themes rather than WordPress core.

---

## ⚠️ Legal Disclaimer

> [!CAUTION]
> This cheatsheet is intended for authorized penetration testing, vulnerability assessments, and web application auditing only. Brute-forcing passwords or scanning systems without explicit authorization is illegal.