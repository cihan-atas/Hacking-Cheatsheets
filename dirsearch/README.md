# 🔍 dirsearch - Complete Cheatsheet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)

```
  ██████╗ ██╗██████╗ ███████╗███████╗ █████╗ ██████╗  ██████╗██╗  ██╗
  ██╔══██╗██║██╔══██╗██╔════╝██╔════╝██╔══██╗██╔══██╗██╔════╝██║  ██║
  ██║  ██║██║██████╔╝███████╗█████╗  ███████║██████╔╝██║     ███████║
  ██║  ██║██║██╔══██╗╚════██║██╔══╝  ██╔══██║██╔══██╗██║     ██╔══██║
  ██████╔╝██║██║  ██║███████║███████╗██║  ██║██║  ██║╚██████╗██║  ██║
  ╚═════╝ ╚═╝╚═╝  ╚═╝╚══════╝╚══════╝╚═╝  ╚═╝╚═╝  ╚═╝ ╚═════╝╚═╝  ╚═╝
       Advanced Web Path Scanner & Directory Brute-Forcer
```

<p align="center">
  <img src="https://img.shields.io/badge/dirsearch-Web_Bruteforcer-blue?style=for-the-badge" alt="dirsearch">
  <img src="https://img.shields.io/badge/Language-Python_Async-green?style=for-the-badge" alt="Python Async">
  <img src="https://img.shields.io/badge/Recon-Web_Discovery-orange?style=for-the-badge" alt="Web Discovery">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="License GPLv2">
</p>

<p align="center">
  <b>🌐 Fast, feature-packed command-line web path scanner with intelligent extension handling, recursion, and crawling</b>
</p>

---

## 📋 Table of Contents

- [Introduction](#-introduction)
- [Tool Comparison](#-tool-comparison)
- [Installation](#-installation)
- [Basic Usage & Target Selection](#-basic-usage--target-selection)
- [Extension & Suffix Management](#-extension--suffix-management)
- [Wordlists & Dictionaries](#-wordlists--dictionaries)
- [Recursion & Web Crawling](#-recursion--web-crawling)
- [Threading, Rate Limiting & Delays](#-threading-rate-limiting--delays)
- [Filtering & Matching Responses](#-filtering--matching-responses)
- [HTTP Request Customization](#-http-request-customization)
  - [Custom Headers & User-Agents](#custom-headers--user-agents)
  - [Cookies & Authentication](#cookies--authentication)
  - [POST Data & Request Methods](#post-data--request-methods)
- [Proxies, Tor & Evasion](#-proxies-tor--evasion)
- [Output Formats & Reporting](#-output-formats--reporting)
- [Real-World Pentest & Bug Bounty Recipes](#-real-world-pentest--bug-bounty-recipes)
- [Quick Reference Table](#-quick-reference-table)
- [Tips & Best Practices](#-tips--best-practices)
- [Legal Disclaimer](#-legal-disclaimer)

---

## 🎯 Introduction

**dirsearch** (created by Mauro Soria) is one of the most popular and versatile command-line tools for discovering hidden files and directories on web servers. Written in Python, it provides unmatched flexibility with its rich command-line arguments, colorized terminal output, automatic recursive crawling, intelligent wildcard extension expansion, and built-in evasion controls.

Whether you are performing web penetration testing, bug bounty hunting, or internal CTF assessments, `dirsearch` is tailored to uncover hidden administrative interfaces, backup databases, forgotten API endpoints, and sensitive credentials.

### Key Features

| Feature | Description |
|---------|-------------|
| **Multi-Threaded** | High-performance asynchronous and threaded request engine |
| **Extension Wildcards** | Expand `-e *` to test all known web file extensions automatically |
| **Integrated Crawling** | Follows HTML/JS links to seed recursive directory queues |
| **Rich Filtering** | Filter responses by status code, byte size, word count, line count, or regex |
| **Raw Request Replay** | Load complete Burp Suite HTTP requests via `--raw` |
| **Multiple Formats** | Generates reports in Plaintext, JSON, CSV, Markdown, and HTML |

---

## ⚖️ Tool Comparison

| Feature | dirsearch | ffuf | Feroxbuster | Gobuster | DIRB |
|---------|-----------|------|-------------|----------|------|
| **Language** | Python | Go | Rust | Go | C |
| **Speed** | ⭐⭐⭐⭐ (Fast) | ⭐⭐⭐⭐⭐ (Extremely fast) | ⭐⭐⭐⭐⭐ (Blazingly fast) | ⭐⭐⭐⭐ (Fast) | ⭐⭐ (Low thread) |
| **Default Recursion** | ✅ Automatic (`-r`) | Optional (`-recursion`) | ✅ Automatic (`-d`) | Optional (`-r`) | ✅ Automatic |
| **Extension Wildcard (`*`)**| ✅ Native (`-e *`) | ❌ No | ❌ No | ❌ No | ❌ No |
| **Built-in Web Crawler** | ✅ Yes (`--crawl`) | ❌ No | ✅ Yes (`--extract-links`) | ❌ No | ❌ No |
| **POST / Data Fuzzing** | ✅ Yes (`--data`) | ✅ Full HTTP | ❌ Limited | ❌ No | ❌ No |
| **Burp Raw Request Replay**| ✅ Yes (`--raw`) | ✅ Yes (`-request`) | ❌ No | ❌ No | ❌ No |
| **Built-in Wordlist** | ✅ Curated (`db/dicc.txt`)| ❌ None | ❌ None | ❌ None | ✅ Small list |

---

## 📥 Installation

### Using pipx (Recommended for Kali & Modern Linux)

```bash
# Install via pipx (isolated Python environment)
pipx install dirsearch

# Verify installation
dirsearch --version
```

### Using pip

```bash
# Install via standard pip
pip3 install dirsearch
```

### Using Git Clone (Direct from Source)

```bash
# Clone the official repository
git clone https://github.com/maurosoria/dirsearch.git --depth 1
cd dirsearch

# Install requirements and run
pip3 install -r requirements.txt
python3 dirsearch.py --version
```

### Kali Linux Package & Docker

```bash
# Kali / Debian repository
sudo apt update && sudo apt install -y dirsearch

# Docker container
docker run -it --rm maurosoria/dirsearch -u https://example.com -e php,html
```

---

## 🚀 Basic Usage & Target Selection

### Single Target

```bash
# Basic scan against a single URL specifying extensions
dirsearch -u https://target.com -e php,html,js,txt

# Scan with trailing slash forced
dirsearch -u https://target.com -e php -f
```

### Multiple Targets

```bash
# Scan a list of targets from file
dirsearch -l targets.txt -e php,aspx,txt

# Scan stdin input from subfinder / httpx
cat live_hosts.txt | dirsearch --stdin -e php
```

### Port Scanning & CIDR Targeting

```bash
# Scan specific ports on a host
dirsearch -u https://target.com --ports 80,443,8080,8443 -e php

# Scan an entire subnet range
dirsearch --cidr 192.168.1.0/24 -e html
```

---

## 🧩 Extension & Suffix Management

`dirsearch` has the most powerful extension management system among all directory scanners:

```bash
# Specify multiple extensions
dirsearch -u https://target.com -e php,html,json,txt,zip

# Use the wildcard (*) to test ALL known extensions automatically
dirsearch -u https://target.com -e *

# Add prefixes to wordlist entries (e.g. test_admin, dev_admin)
dirsearch -u https://target.com -e php --prefixes test_,dev_,staging_,old_

# Add suffixes to wordlist entries (e.g. admin_backup, admin_old)
dirsearch -u https://target.com -e php --suffixes _bak,~,_2026,_old
```

---

## 📚 Wordlists & Dictionaries

`dirsearch` bundles a comprehensive, battle-tested wordlist at `db/dicc.txt`:

```bash
# Run using the default bundled dictionary
dirsearch -u https://target.com -e php

# Use SecLists Common or Medium wordlists
dirsearch -u https://target.com -e php -w /usr/share/seclists/Discovery/Web-Content/directory-list-2.3-medium.txt

# Use multiple wordlists simultaneously
dirsearch -u https://target.com -e php -w list1.txt,list2.txt

# Force all wordlist entries to lowercase or uppercase
dirsearch -u https://target.com -e php --lowercase
dirsearch -u https://target.com -e php --uppercase
```

---

## 🔄 Recursion & Web Crawling

### Recursive Scanning

```bash
# Recursively scan newly found directories
dirsearch -u https://target.com -e php -r

# Limit recursion depth to 2 levels
dirsearch -u https://target.com -e php -r --recursion-depth 2

# Recursively scan only if HTTP status is 200, 301, or 302
dirsearch -u https://target.com -e php -r --recursion-status 200,301,302
```

### Integrated Web Crawler

```bash
# Crawl the target website for internal URLs and feed them into the scan queue
dirsearch -u https://target.com -e php --crawl
```

---

## ⚡ Threading, Rate Limiting & Delays

Control scan aggressiveness to prevent triggering rate-limiting or Web Application Firewalls (WAF):

```bash
# Set number of concurrent threads (default: 25)
dirsearch -u https://target.com -e php -t 50

# Add a delay between requests (e.g. 0.5 seconds)
dirsearch -u https://target.com -e php -s 0.5

# Enforce a maximum request rate per second (e.g. max 15 req/sec)
dirsearch -u https://target.com -e php --max-rate 15

# Customize HTTP connection timeouts and retries
dirsearch -u https://target.com -e php --timeout 8 --retries 3

# Exit scan immediately on excessive connection errors
dirsearch -u https://target.com -e php --exit-on-error
```

---

## 🎯 Filtering & Matching Responses

### Status Code Inclusion & Exclusion

```bash
# Include only specific status codes (e.g., ignore everything except 200, 301, 403)
dirsearch -u https://target.com -e php -i 200,301,302,403

# Exclude specific status codes (e.g., exclude 404 and 500)
dirsearch -u https://target.com -e php -x 404,400,500,502,503
```

### Filtering by Size, Words, and Content

```bash
# Exclude responses by specific byte size (e.g. ignore custom 404 pages)
dirsearch -u https://target.com -e php --exclude-sizes 0B,1542B,3421B

# Exclude responses matching specific text on the page
dirsearch -u https://target.com -e php --exclude-texts "Not Found","Access Denied"

# Exclude responses matching a regular expression
dirsearch -u https://target.com -e php --exclude-regex "^Error:.*"
```

---

## 🛠️ HTTP Request Customization

### Custom Headers & User-Agents

```bash
# Set a custom header (e.g., authorization token or internal bypass header)
dirsearch -u https://target.com -e php -H "X-Forwarded-For: 127.0.0.1" -H "X-Requested-With: XMLHttpRequest"

# Load headers from a file
dirsearch -u https://target.com -e php --header-list custom_headers.txt

# Use a randomized User-Agent per request to bypass basic WAFs
dirsearch -u https://target.com -e php --random-agent

# Set a custom User-Agent
dirsearch -u https://target.com -e php --user-agent "Mozilla/5.0 (Windows NT 10.0; Win64; x64)"
```

### Cookies & Authentication

```bash
# Provide session cookies for authenticated scanning
dirsearch -u https://target.com -e php -b "PHPSESSID=abc123xyz; admin_logged=true"

# HTTP Basic Authentication (username:password)
dirsearch -u https://target.com -e php --auth admin:SecretPass123 --auth-type basic

# NTLM Authentication for corporate Windows portals
dirsearch -u https://target.com -e php --auth CORP\\alice:Welcome2026 --auth-type ntlm
```

### POST Data & Request Methods

```bash
# Perform fuzzing using HTTP POST requests
dirsearch -u https://target.com/api -m POST -d "action=test&token=xyz" -e json

# Replay an entire raw HTTP request saved from Burp Suite
dirsearch -u https://target.com --raw request.txt -e php
```

---

## 🛡️ Proxies, Tor & Evasion

```bash
# Route all traffic through an HTTP or SOCKS5 proxy (e.g., Burp Suite)
dirsearch -u https://target.com -e php --proxy http://127.0.0.1:8080

# Route all traffic through the local Tor SOCKS proxy
dirsearch -u https://target.com -e php --tor

# Skip SSL/TLS certificate verification
dirsearch -u https://target.com -e php --no-check-certificate
```

---

## 📄 Output Formats & Reporting

```bash
# Save output as a clean text file
dirsearch -u https://target.com -e php -o results.txt

# Export results to JSON format
dirsearch -u https://target.com -e php -o results.json --format json

# Export results to HTML, CSV, or Markdown
dirsearch -u https://target.com -e php -o report.html --format html
dirsearch -u https://target.com -e php -o results.csv --format csv
dirsearch -u https://target.com -e php -o report.md --format markdown
```

---

## 🎯 Real-World Pentest & Bug Bounty Recipes

### 1. Rapid Full-Scope Web Reconnaissance

```bash
# Scan with common web extensions, follow redirects, random UA, 40 threads
dirsearch -u https://target.com \
  -e php,html,js,json,txt,zip \
  -t 40 \
  --random-agent \
  -i 200,301,302,403 \
  -o target_recon.txt
```

### 2. Hunting for Forgotten Backups & Source Leaks

```bash
# Test for database dumps, git repos, env files, and zip archives
dirsearch -u https://target.com \
  -e bak,old,orig,zip,tar.gz,sql,env,git \
  -w /usr/share/seclists/Discovery/Web-Content/raft-medium-files.txt \
  -i 200 \
  --exclude-sizes 0B
```

### 3. Deep Recursive Directory Discovery

```bash
# 2-level recursion, crawling pages for new paths, ignoring error status codes
dirsearch -u https://target.com \
  -e php,html \
  -r --recursion-depth 2 \
  --crawl \
  -x 404,500,502 \
  -o deep_scan.json --format json
```

### 4. Authenticated Internal Portal Assessment

```bash
# Scan through Burp Suite with internal session cookies and WAF bypass headers
dirsearch -u https://intranet.corp.local \
  -e aspx,ashx,config,txt \
  -b "ASP.NET_SessionId=xyz123; AuthToken=ey..." \
  -H "X-Forwarded-For: 127.0.0.1" \
  --proxy http://127.0.0.1:8080
```

---

## ⚡ Quick Reference Table

| Task | Command |
|------|---------|
| **Basic scan with extensions** | `dirsearch -u https://target.com -e php,html,txt` |
| **All extensions wildcard** | `dirsearch -u https://target.com -e *` |
| **Custom wordlist** | `dirsearch -u https://target.com -e php -w /path/to/wordlist.txt` |
| **Scan multiple targets** | `dirsearch -l targets.txt -e php,aspx` |
| **Recursive discovery** | `dirsearch -u https://target.com -e php -r --recursion-depth 2` |
| **Crawl target links** | `dirsearch -u https://target.com -e php --crawl` |
| **Include status codes** | `dirsearch -u https://target.com -e php -i 200,301,302,403` |
| **Exclude status codes** | `dirsearch -u https://target.com -e php -x 404,500,502` |
| **Exclude false positive sizes**| `dirsearch -u https://target.com -e php --exclude-sizes 0B,1240B` |
| **Authenticate with cookies** | `dirsearch -u https://target.com -e php -b "session=abc123"` |
| **Route through Burp Suite** | `dirsearch -u https://target.com -e php --proxy http://127.0.0.1:8080` |
| **Save report to JSON** | `dirsearch -u https://target.com -e php -o report.json --format json` |

---

## 💡 Tips & Best Practices

1. **Use `-e *` Sparingly on Large Wordlists:** Wildcard extension expansion multiplies each word by 20+ extensions. Use `-e *` only with smaller lists like `common.txt` or `dicc.txt`.
2. **Filter Out False-Positive Sizes:** Modern web frameworks return custom 404 pages with HTTP 200. Inspect the size of the default response and add `--exclude-sizes <size>` to eliminate noise.
3. **Always Check 403 Forbidden Paths:** In modern cloud and enterprise environments, HTTP 403 responses indicate that the directory or endpoint exists. Document all 403s for header bypass and path traversal testing.
4. **Leverage Prefixes & Suffixes for Backups:** Developers frequently save backup copies of sensitive files (e.g. `config.php.bak`, `old_database.sql`). Use `--prefixes` and `--suffixes` to automatically discover them.

---

## ⚠️ Legal Disclaimer

> **IMPORTANT:** Unauthorized port scanning, directory brute-forcing, and web enumeration against servers without explicit, written permission is illegal. Use `dirsearch` strictly in legal bug bounty engagements, authorized penetration tests, and CTF lab environments.
