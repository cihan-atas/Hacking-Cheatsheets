# 🌐 WhatWeb - Complete Cheatsheet

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

<p align="center">
  <img src="https://img.shields.io/badge/WhatWeb-Web_Fingerprinter-blue?style=for-the-badge" alt="WhatWeb">
  <img src="https://img.shields.io/badge/Author-Andrew_Horton-orange?style=for-the-badge" alt="urbanadventurer">
  <img src="https://img.shields.io/badge/Category-Recon_&_Scanning-green?style=for-the-badge" alt="Recon & Scanning">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="License GPLv2">
</p>

<p align="center">
  <b>🌐 Next-generation web scanner designed to identify content management systems (CMS), blogging platforms, JavaScript libraries, and server technologies</b>
</p>

---

## 📋 Table of Contents

- [Overview & Technology Fingerprinting](#-overview--technology-fingerprinting)
- [Installation](#-installation)
- [Basic Website Fingerprinting](#-basic-website-fingerprinting)
- [Aggression Levels Explained (`-a`)](#-aggression-levels-explained--a)
  - [Stealthy Passive Mode (`-a 1`)](#stealthy-passive-mode--a-1)
  - [Aggressive Mode (`-a 3`)](#aggressive-mode--a-3)
  - [Heavy / Brute Mode (`-a 4`)](#heavy--brute-mode--a-4)
- [Scanning Subnets & Target Files](#-scanning-subnets--target-files)
- [Plugin System & Selective Auditing](#-plugin-system--selective-auditing)
- [Output Logging & Report Exporting](#-output-logging--report-exporting)
- [Quick Reference Table](#-quick-reference-table)
- [Defensive Considerations & OPSEC](#-defensive-considerations--opsec)
- [Legal Disclaimer](#-legal-disclaimer)

---

## 🎯 Overview & Technology Fingerprinting

Before launching exploits or brute-force fuzzers against a web application, identifying its exact technology stack is imperative. 

Created by **Andrew Horton (urbanadventurer)**, **WhatWeb** recognizes over 1,800 distinct web technologies:
- **Content Management Systems (CMS):** WordPress, Drupal, Joomla, Ghost, Magento.
- **Web Servers & Proxies:** Apache, Nginx, Microsoft-IIS, Cloudflare, Envoy.
- **Backend Frameworks:** Laravel, Django, Ruby on Rails, ASP.NET, Spring Boot.
- **Frontend Libraries:** React, Vue.js, Angular, jQuery, Bootstrap.
- **Embedded Devices:** Webcams, routers, printers, SCADA interfaces.

---

## 📥 Installation

```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y whatweb

# Arch Linux
sudo pacman -S whatweb

# macOS (Homebrew)
brew install whatweb
```

---

## 🚀 Basic Website Fingerprinting

```bash
# Scan a single domain
whatweb https://target.local

# Brief one-line output format
whatweb --color=never -q https://target.local

# Verbose detailed report showing evidence matches
whatweb -v https://target.local
```

---

## ⚡ Aggression Levels Explained (`-a`)

WhatWeb balances stealth against depth using four aggression levels:

### Stealthy Passive Mode (`-a 1`)
Sends a single HTTP GET request to the provided URL and analyzes the returned response headers, cookies, and HTML tags without requesting extra pages:
```bash
whatweb -a 1 https://target.local
```

### Aggressive Mode (`-a 3`)
Recommended for penetration tests. Follows redirects and requests common plugin files (e.g., `wp-login.php`, `robots.txt`, favicon hashes):
```bash
whatweb -a 3 https://target.local
```

### Heavy / Brute Mode (`-a 4`)
Attempts thousands of plugin guesses and known fingerprint URLs:
```bash
whatweb -a 4 https://target.local
```

---

## 🌐 Scanning Subnets & Target Files

```bash
# Scan an entire CIDR network block
whatweb 192.168.1.0/24

# Scan multiple URLs listed in a text file
whatweb -i targets.txt

# Pipe live web endpoints from httpx into WhatWeb
cat live_hosts.txt | whatweb --no-errors
```

---

## 🧩 Plugin System & Selective Auditing

WhatWeb features a rich plugin architecture:

```bash
# List all 1,800+ available plugins
whatweb -l

# Search for specific plugins (e.g., WordPress or Apache)
whatweb --grep wordpress

# Run ONLY a specific plugin against a target
whatweb --plugins="apache,php,wordpress" https://target.local
```

---

## 📊 Output Logging & Report Exporting

```bash
# Export results to clean JSON format
whatweb https://target.local --log-json results.json

# Export to XML format
whatweb https://target.local --log-xml results.xml

# Export to compact CSV format
whatweb -i targets.txt --log-brief results.txt
```

---

## ⚡ Quick Reference Table

| Objective | Command |
|-----------|---------|
| **Standard Scan** | `whatweb <URL>` |
| **Verbose Detailed Report** | `whatweb -v <URL>` |
| **Stealth Passive Scan** | `whatweb -a 1 <URL>` |
| **Aggressive Deep Scan** | `whatweb -a 3 <URL>` |
| **Scan Subnet (CIDR)** | `whatweb <SUBNET>/24` |
| **Scan Targets from File** | `whatweb -i targets.txt` |
| **Export to JSON** | `whatweb <URL> --log-json report.json` |
| **Filter by Specific Plugins**| `whatweb --plugins="wordpress,nginx" <URL>` |

---

## 🛡️ Defensive Considerations & OPSEC

1. **Server Header Masking:** Remove or randomize the `Server` and `X-Powered-By` HTTP response headers in Apache/Nginx to defeat passive banner fingerprinting.
2. **Favicon Hash Masking:** WhatWeb uses MD5 hashes of `/favicon.ico` to identify web frameworks. Customize default framework favicons.

---

## ⚠️ Legal Disclaimer

> [!CAUTION]
> WhatWeb is intended for authorized reconnaissance, security auditing, and system inventory only. Scanning unauthorized networks or web applications without permission may violate applicable laws.