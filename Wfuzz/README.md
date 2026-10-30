# 🎯 Wfuzz - Complete Cheatsheet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)

```
  ██╗    ██╗███████╗██╗   ██╗███████╗███████╗
  ██║    ██║██╔════╝██║   ██║╚══███╔╝╚══███╔╝
  ██║ █╗ ██║█████╗  ██║   ██║  ███╔╝   ███╔╝ 
  ██║███╗██║██╔══╝  ██║   ██║ ███╔╝   ███╔╝  
  ╚███╔███╔╝██║     ╚██████╔╝███████╗███████╗
   ╚══╝╚══╝ ╚═╝      ╚═════╝ ╚══════╝╚══════╝
       The Flexible Web Application Fuzzer & Parameter Explorer
```

<p align="center">
  <img src="https://img.shields.io/badge/Wfuzz-Web_Fuzzer-blue?style=for-the-badge" alt="Wfuzz">
  <img src="https://img.shields.io/badge/Language-Python-orange?style=for-the-badge" alt="Language Python">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-green?style=for-the-badge" alt="Web Security & API">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="License GPLv2">
</p>

<p align="center">
  <b>🌐 Highly customizable HTTP fuzzer designed for discovering hidden parameters, bypasses, headers, and multi-payload matrices</b>
</p>

---

## 📋 Table of Contents

- [Overview & The Keyword Architecture](#-overview--the-keyword-architecture)
- [Wfuzz vs ffuf Comparison](#-wfuzz-vs-ffuf-comparison)
- [Installation](#-installation)
- [Basic Directory & File Discovery](#-basic-directory--file-discovery)
- [Filtering & Hiding Noise (`--hc`, `--hl`, `--hw`, `--hh`)](#-filtering--hiding-noise---hc---hl---hw---hh)
- [Payload Types & Dynamic Encoders (`-z`)](#-payload-types--dynamic-encoders--z)
- [Parameter & Hidden Variable Fuzzing](#-parameter--hidden-variable-fuzzing)
- [Headers, Cookies & Authentication](#-headers-cookies--authentication)
- [Multi-Variable Fuzzing (`FUZZ` and `FUZ2Z`)](#-multi-variable-fuzzing-fuzz-and-fuz2z)
- [Routing Through Burp Suite (`-p`)](#-routing-through-burp-suite--p)
- [Quick Reference Table](#-quick-reference-table)
- [Defensive Considerations & OPSEC](#-defensive-considerations--opsec)
- [Legal Disclaimer](#-legal-disclaimer)

---

## 🎯 Overview & The Keyword Architecture

Written in Python by **Xavi Mendez**, **Wfuzz** is a classic, battle-tested tool for assessing web applications. It replaces designated placeholders (default: `FUZZ`) inside URLs, parameters, headers, and POST bodies with dictionary entries or algorithmically generated payloads.

Wfuzz stands out for its unmatched **payload versatility** (ranges, IP generators, regex strings, on-the-fly encoders like MD5 or Base64) and modular filtering mechanics.

---

## ⚖️ Wfuzz vs ffuf Comparison

| Feature | Wfuzz | ffuf |
|---------|-------|------|
| **Implementation Language** | Python | Go |
| **Raw Speed** | ⭐⭐⭐ (Good) | ⭐⭐⭐⭐⭐ (Blazingly Fast) |
| **Dynamic Payload Encoders**| ✅ Native (`-z file,pass.txt,md5`)| ❌ Requires external scripts |
| **Multi-Payload Combinations**| ✅ Native (`FUZZ`, `FUZ2Z`, `FUZ3Z`)| ✅ Supported (`-w w1:W1 -w w2:W2`)|
| **Built-in Payload Generators**| ✅ Ranges, IPs, Hex, Dictionaries| ❌ File wordlists only |
| **Resource Usage** | Moderate RAM / CPU | Low Memory footprint |

---

## 📥 Installation

```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y wfuzz

# Python Pip
pip3 install wfuzz

# Arch Linux
sudo pacman -S wfuzz
```

---

## 🚀 Basic Directory & File Discovery

```bash
# Basic directory fuzzing
wfuzz -w /usr/share/wordlists/dirb/common.txt http://target.local/FUZZ

# Fuzzing file extensions
wfuzz -w extensions.txt http://target.local/index.FUZZ

# Fuzzing hidden backup files
wfuzz -w words.txt http://target.local/FUZZ.bak
```

---

## 🎯 Filtering & Hiding Noise (`--hc`, `--hl`, `--hw`, `--hh`)

Real-world servers return responses even for non-existent pages (custom 404 pages or redirects). Wfuzz provides fine-grained filtering flags:

```bash
# Hide specific HTTP status codes (e.g. 404, 403)
wfuzz -w wordlist.txt --hc 404,403 http://target.local/FUZZ

# Hide responses by line count (filters out static custom 404 pages)
wfuzz -w wordlist.txt --hl 42 http://target.local/FUZZ

# Hide responses by word count
wfuzz -w wordlist.txt --hw 120 http://target.local/FUZZ

# Hide responses by character count / body length
wfuzz -w wordlist.txt --hh 0,1052 http://target.local/FUZZ

# Show ONLY HTTP 200 and 302 responses
wfuzz -w wordlist.txt --sc 200,302 http://target.local/FUZZ
```

---

## 📦 Payload Types & Dynamic Encoders (`-z`)

Wfuzz provides built-in generators that eliminate the need for pre-generated text files:

```bash
# Numeric Range Fuzzing (e.g., IDOR testing from 1 to 500)
wfuzz -z range,1-500 http://target.local/user.php?id=FUZZ

# Custom Inline List
wfuzz -z list,admin-root-test-dev http://target.local/FUZZ/

# On-the-fly MD5 encoding of wordlist entries
wfuzz -z file,passwords.txt,md5 http://target.local/login?hash=FUZZ

# On-the-fly Base64 encoding
wfuzz -z file,usernames.txt,base64 http://target.local/auth?token=FUZZ
```

---

## 🔍 Parameter & Hidden Variable Fuzzing

Discover unlinked GET/POST parameters:

```bash
# GET parameter discovery
wfuzz -w /usr/share/seclists/Discovery/Web-Content/burp-parameter-names.txt \
  --hc 404 --hh 2340 "http://target.local/search.php?FUZZ=test"

# POST body parameter discovery
wfuzz -w parameters.txt -d "FUZZ=admin" --hc 404 http://target.local/api/update
```

---

## 🍪 Headers, Cookies & Authentication

```bash
# Cookie Fuzzing (Session hijacking / privilege testing)
wfuzz -w wordlist.txt -b "session=FUZZ" http://target.local/dashboard

# HTTP Header Fuzzing (IP bypasses via X-Forwarded-For)
wfuzz -z file,ips.txt -H "X-Forwarded-For: FUZZ" http://target.local/admin

# Basic Authentication brute force
wfuzz -z file,users.txt -z file,passwords.txt --basic "FUZZ:FUZ2Z" http://target.local/protected/
```

---

## ⚡ Multi-Variable Fuzzing (`FUZZ` and `FUZ2Z`)

Combine multiple wordlists using distinct placeholder tags:

```bash
# Test username and password matrix simultaneously
wfuzz -w users.txt -w passwords.txt -d "user=FUZZ&pass=FUZ2Z" --hc 200 http://target.local/login.php

# Fuzz path and extension together
wfuzz -w words.txt -w extensions.txt http://target.local/FUZZ.FUZ2Z
```

---

## 🔁 Routing Through Burp Suite (`-p`)

Inspect payloads and responses inside Burp Suite:

```bash
# Route requests through local Burp Suite proxy
wfuzz -p 127.0.0.1:8080:HTTP -w wordlist.txt http://target.local/FUZZ
```

---

## ⚡ Quick Reference Table

| Objective | Command |
|-----------|---------|
| **Directory Fuzzing** | `wfuzz -w <DICT> <URL>/FUZZ` |
| **Hide HTTP Status Codes** | `wfuzz -w <DICT> --hc 404,403 <URL>/FUZZ` |
| **Hide by Response Length** | `wfuzz -w <DICT> --hh <LENGTH> <URL>/FUZZ` |
| **Fuzz GET Parameter** | `wfuzz -w <PARAMS> "<URL>?FUZZ=test"` |
| **Fuzz POST Body** | `wfuzz -w <PARAMS> -d "FUZZ=test" <URL>` |
| **Numeric Range Payloads** | `wfuzz -z range,1-100 "<URL>?id=FUZZ"` |
| **Encode Payloads on the Fly**| `wfuzz -z file,<DICT>,base64 "<URL>?token=FUZZ"` |
| **Multi-Keyword Fuzzing** | `wfuzz -w <W1> -w <W2> -d "u=FUZZ&p=FUZ2Z" <URL>` |
| **Route via Burp Proxy** | `wfuzz -p 127.0.0.1:8080:HTTP -w <DICT> <URL>/FUZZ` |

---

## 🛡️ Defensive Considerations & OPSEC

1. **WAF Blocking:** Fuzzing hundreds of requests per second triggers Cloudflare, AWS WAF, and ModSecurity rate limiting. Use `-s <seconds>` to throttle request velocity.
2. **Resource Exhaustion:** Heavy fuzzing on unindexed database queries can cause Denial of Service (DoS) conditions on legacy web servers.

---

## ⚠️ Legal Disclaimer

> [!CAUTION]
> This cheatsheet is for authorized penetration testing and vulnerability auditing only. Fuzzing unauthorized applications is strictly prohibited.