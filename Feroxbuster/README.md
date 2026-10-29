# 🦀 Feroxbuster - Complete Cheatsheet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)

```
  ███████╗███████╗██████╗  ██████╗ ██╗  ██╗██████╗ ██╗   ██╗███████╗████████╗███████╗██████╗ 
  ██╔════╝██╔════╝██╔══██╗██╔═══██╗╚██╗██╔╝██╔══██╗██║   ██║██╔════╝╚══██╔══╝██╔════╝██╔══██╗
  █████╗  █████╗  ██████╔╝██║   ██║ ╚███╔╝ ██████╔╝██║   ██║███████╗   ██║   █████╗  ██████╔╝
  ██╔══╝  ██╔══╝  ██╔══██╗██║   ██║ ██╔██╗ ██╔══██╗██║   ██║╚════██║   ██║   ██╔══╝  ██╔══██╗
  ██║     ███████╗██║  ██║╚██████╔╝██╔╝ ██╗██████╔╝╚██████╔╝███████║   ██║   ███████╗██║  ██║
  ╚═╝     ╚══════╝╚═╝  ╚═╝ ╚═════╝ ╚═╝  ╚═╝╚═════╝  ╚═════╝ ╚══════╝   ╚═╝   ╚══════╝╚═╝  ╚═╝
                Fast, Simple, Recursive Content Discovery
```

<p align="center">
  <img src="https://img.shields.io/badge/Feroxbuster-Content_Discovery-orange?style=for-the-badge" alt="Feroxbuster">
  <img src="https://img.shields.io/badge/Rust-Blazingly_Fast-blue?style=for-the-badge" alt="Rust">
  <img src="https://img.shields.io/badge/Auto_Tune-Recursive-green?style=for-the-badge" alt="Recursive">
  <img src="https://img.shields.io/badge/Bug_Bounty-Web_Pentest-red?style=for-the-badge" alt="Web Pentest">
</p>

<p align="center">
  <b>⚡ A fast, simple, recursive content discovery tool written in Rust</b>
</p>

---

## 📋 Table of Contents

- [Introduction](#-introduction)
- [Key Features](#-key-features)
- [Tool Comparison](#-tool-comparison)
- [Installation](#-installation)
- [Basic Syntax & Quick Start](#-basic-syntax--quick-start)
- [Core Command Options](#-core-command-options)
  - [Recursion Control](#recursion-control)
  - [Concurrency & Performance](#concurrency--performance)
  - [File Extensions](#file-extensions)
  - [Filtering & Matching](#filtering--matching)
  - [Smart Features (Auto-Tune, Link Extraction, Backups)](#smart-features)
  - [Headers, Cookies & Auth](#headers-cookies--auth)
  - [Proxy & Burp Suite Integration](#proxy--burp-suite-integration)
- [Multi-Target Scanning & Pipelines](#-multi-target-scanning--pipelines)
- [Real-World Workflows](#-real-world-workflows)
- [Quick Reference Table](#-quick-reference-table)
- [Tips & Best Practices](#-tips--best-practices)
- [Legal Disclaimer](#-legal-disclaimer)
- [Resources](#-resources)

---

## 🎯 Introduction

**Feroxbuster** is a high-performance, multi-threaded, recursive web content scanner written in Rust. Designed to be fast, simple, and intuitive, it combines the speed of Go/Rust-based fuzzers with native automatic recursive directory discovery.

It shines during penetration tests and bug bounty engagements where deep website trees and hidden directories must be discovered rapidly without spawning complex manual scripts.

---

## 🌟 Key Features

- ⚡ **Blazingly Fast:** Powered by asynchronous Rust (`tokio` and `reqwest`), capable of thousands of requests per second.
- 🔄 **Native Recursive Discovery:** Automatically discovers new directories and recursively scans them up to configurable depth limits.
- 🧠 **Auto-Tune & Auto-Bail:** Automatically throttles scan speed if the target web server slows down or aborts if excessive connection errors occur.
- 🔗 **In-Line Link Extraction:** Parses HTML and JavaScript responses to discover hidden endpoints and feeds them back into the scan queue.
- 📦 **Automated Backup Hunting:** Appends `.bak`, `~`, `.old`, and `.orig` variations automatically to discovered files.
- 🎯 **Pipeable Input:** Seamlessly accepts target lists from `stdin` (from tools like `subfinder`, `httpx`, or `gau`).

---

## ⚖️ Tool Comparison

| Feature | Feroxbuster | ffuf | Gobuster | Dirb |
|---------|-------------|------|----------|------|
| **Language** | Rust | Go | Go | C |
| **Speed** | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐ |
| **Default Recursion** | ✅ Automatic | ❌ Manual flag | ❌ None | ✅ Automatic |
| **Link Extraction** | ✅ Yes (`--extract-links`) | ❌ No | ❌ No | ❌ No |
| **Auto Rate Tuning** | ✅ Yes (`--auto-tune`) | ❌ No | ❌ No | ❌ No |
| **Multi-Target Stdin** | ✅ Native | ❌ Limited | ❌ No | ❌ No |
| **Backup File Detection**| ✅ Native (`--collect-backups`)| ❌ No | ❌ No | ❌ No |

---

## 📥 Installation

### Kali Linux & Debian / Ubuntu

```bash
# Update repository and install
sudo apt update && sudo apt install -y feroxbuster
```

### Using Cargo (Rust Package Manager)

```bash
cargo install feroxbuster
```

### macOS (Homebrew)

```bash
brew install feroxbuster
```

### Docker

```bash
docker run --init -it epi052/feroxbuster -u https://target.com -w /usr/share/seclists/Discovery/Web-Content/common.txt
```

### Precompiled Binary (GitHub Releases)

```bash
# Download latest Linux release
curl -sL https://raw.githubusercontent.com/epi052/feroxbuster/main/install-nix.sh | bash
sudo mv feroxbuster /usr/local/bin/
```

---

## 🚀 Basic Syntax & Quick Start

```bash
feroxbuster -u <TARGET_URL> -w <WORDLIST> [OPTIONS]
```

### Quick Scan Examples

```bash
# Scan a single target with default settings
feroxbuster -u http://10.10.10.10

# Scan with custom SecLists directory list
feroxbuster -u https://example.com -w /usr/share/seclists/Discovery/Web-Content/directory-list-2.3-medium.txt

# Scan target including specific file extensions
feroxbuster -u https://example.com -w /usr/share/wordlists/dirb/common.txt -x php,html,txt,json
```

---

## ⚙️ Core Command Options

### Recursion Control

```bash
# Specify maximum recursion depth (default is 4)
feroxbuster -u https://example.com -w common.txt -d 2

# Completely disable recursion (scan root directory only)
feroxbuster -u https://example.com -w common.txt -n

# Don't scan nested subdirectories automatically (interactive prompt)
feroxbuster -u https://example.com -w common.txt --dont-scan /api,/static
```

### Concurrency & Performance

```bash
# Set number of concurrent threads per directory (default: 50)
feroxbuster -u https://example.com -t 100

# Limit total concurrent directory scans running simultaneously (default: unlimited)
feroxbuster -u https://example.com -L 3

# Rate limit requests per second to avoid triggering WAF/firewall blocks
feroxbuster -u https://example.com --rate-limit 150

# Request timeout in seconds (default: 7)
feroxbuster -u https://example.com -T 5
```

### File Extensions

```bash
# Append extensions to each wordlist item
feroxbuster -u https://example.com -x php,asp,aspx,jsp,txt,bak

# Automatically collect and fuzz file extensions found during crawling
feroxbuster -u https://example.com --collect-extensions
```

### Filtering & Matching

```bash
# Match only specific status codes (e.g., 200 OK and 301 Redirects)
feroxbuster -u https://example.com -s 200 301 302

# Filter out specific status codes (e.g., exclude 404 and 403)
feroxbuster -u https://example.com -C 404 403 500

# Filter responses matching exact size in bytes
feroxbuster -u https://example.com -S 4092 1024

# Filter responses matching exact word count
feroxbuster -u https://example.com -W 42

# Filter responses matching exact line count
feroxbuster -u https://example.com -N 12

# Filter responses matching regular expressions (e.g., generic error pages)
feroxbuster -u https://example.com --filter-regex "Under Construction|Page Not Found"

# Filter responses similar to a known 404/wildcard page
feroxbuster -u https://example.com --filter-similar-to https://example.com/definitelynotfoundpage404
```

### Smart Features

```bash
# Auto-tune: Automatically adjusts request concurrency based on server response latency
feroxbuster -u https://example.com --auto-tune

# Auto-bail: Aborts scanning automatically when more than 100 consecutive network errors occur
feroxbuster -u https://example.com --auto-bail

# Extract links: Parses HTML and JavaScript to automatically uncover endpoints
feroxbuster -u https://example.com --extract-links

# Collect backups: Automatically tests for backup copies (.bak, ~, .old) of every discovered file
feroxbuster -u https://example.com --collect-backups
```

### Headers, Cookies & Auth

```bash
# Custom HTTP Header
feroxbuster -u https://example.com -H "Authorization: Bearer <TOKEN>" -H "X-Forwarded-For: 127.0.0.1"

# Session Cookie
feroxbuster -u https://example.com -b "session=abc123xyz; role=admin"

# Custom User-Agent string
feroxbuster -u https://example.com -a "Mozilla/5.0 (Windows NT 10.0; Win64; x64)"

# Ignore invalid / self-signed TLS certificates
feroxbuster -u https://10.10.10.10 -k
```

### Proxy & Burp Suite Integration

```bash
# Route all requests through Burp Suite proxy
feroxbuster -u https://example.com -p http://127.0.0.1:8080 -k

# Convenience shortcut flag for Burp Suite (enables -k and sets proxy to localhost:8080)
feroxbuster -u https://example.com --burp
```

---

## 🔁 Multi-Target Scanning & Pipelines

Feroxbuster natively reads target URLs from standard input (`stdin`), making it ideal for integration with asset discovery pipelines:

```bash
# Pipeline: Subdomain discovery -> HTTP Probing -> Feroxbuster Fuzzing
subfinder -d example.com -silent | \
  httpx -silent -mc 200,301,302 | \
  feroxbuster --stdin -w /usr/share/seclists/Discovery/Web-Content/raft-medium-directories.txt -t 30 -L 4
```

### Scanning URLs from a File

```bash
cat targets.txt | feroxbuster --stdin -w common.txt -v
```

---

## 🎬 Real-World Workflows

### Workflow 1: Comprehensive Bug Bounty Web Reconnaissance

High concurrency, automatic rate tuning, link extraction, and backup detection:

```bash
feroxbuster -u https://target.com \
  -w /usr/share/seclists/Discovery/Web-Content/directory-list-2.3-medium.txt \
  -x php,js,json,txt,html \
  --extract-links \
  --collect-backups \
  --auto-tune \
  -o ferox_target_scan.txt
```

### Workflow 2: Internal Enterprise / Active Directory Portal Discovery

Scanning an internal web application via Burp Suite with authentication and self-signed certificates:

```bash
feroxbuster -u https://internal-portal.corp.local \
  -w /usr/share/seclists/Discovery/Web-Content/common.txt \
  -x aspx,ashx,asmx,config,txt \
  -b "ASP.NET_SessionId=k2m9fl3p8..." \
  --burp \
  -d 3
```

### Workflow 3: Fast Non-Recursive API & Endpoint Discovery

```bash
feroxbuster -u https://api.target.com/v1 \
  -w /usr/share/seclists/Discovery/Web-Content/api/api-endpoints.txt \
  -n \
  -s 200,201,204,401,403 \
  -t 100 \
  --json \
  -o api_results.json
```

---

## 📊 Quick Reference Table

| Flag | Short | Description | Example |
|------|-------|-------------|---------|
| `--url` | `-u` | Target URL to scan | `feroxbuster -u https://target.com` |
| `--wordlist` | `-w` | Path to wordlist | `feroxbuster -w /path/to/list.txt` |
| `--threads` | `-t` | Number of concurrent threads per directory | `feroxbuster -t 80` |
| `--scan-limit` | `-L` | Limit total simultaneous directory scans | `feroxbuster -L 3` |
| `--depth` | `-d` | Maximum recursion depth (default 4) | `feroxbuster -d 2` |
| `--no-recursion` | `-n` | Disable recursive directory scanning | `feroxbuster -n` |
| `--extensions` | `-x` | File extensions to append | `feroxbuster -x php,html,bak` |
| `--status-codes` | `-s` | Status codes to include | `feroxbuster -s 200 301 302` |
| `--filter-status` | `-C` | Status codes to exclude | `feroxbuster -C 404 500` |
| `--filter-size` | `-S` | Response sizes to exclude | `feroxbuster -S 4096` |
| `--filter-words` | `-W` | Word counts to exclude | `feroxbuster -W 15` |
| `--filter-lines` | `-N` | Line counts to exclude | `feroxbuster -N 8` |
| `--auto-tune` | None | Dynamically adjust speed based on latency | `feroxbuster --auto-tune` |
| `--auto-bail` | None | Abort scan on excessive network errors | `feroxbuster --auto-bail` |
| `--extract-links`| None | Parse HTML/JS for new endpoints | `feroxbuster --extract-links` |
| `--collect-backups` | None | Check for backup variations (`.bak`, `.old`) | `feroxbuster --collect-backups` |
| `--proxy` | `-p` | Send traffic through HTTP proxy | `feroxbuster -p http://127.0.0.1:8080` |
| `--burp` | None | Shortcut for `-p http://127.0.0.1:8080 -k` | `feroxbuster --burp` |
| `--stdin` | None | Read URLs from standard input | `cat urls.txt \| feroxbuster --stdin` |
| `--output` | `-o` | Output file path | `feroxbuster -o results.txt` |
| `--json` | None | Format output as JSON | `feroxbuster --json -o out.json` |

---

## 💡 Tips & Best Practices

1. **Use `--auto-tune` for Remote Targets:**
   - Instead of guessing thread counts (`-t`), `--auto-tune` actively measures target response times and throttles or accelerates dynamically, avoiding accidental server denial of service.
2. **Combine `--extract-links` and `--collect-backups`:**
   - This combination is exceptionally lethal in bug bounty assessments: Feroxbuster extracts hidden JS routes, scans them, and immediately checks if developer backups (`.bak`, `.swp`, `~`) were left on the server.
3. **Limit Scans on Large Wordlists with `-L`:**
   - If using a 200,000+ word dictionary, unrestricted recursion can launch dozens of simultaneous threads. Use `-L 2` or `-L 3` to keep memory and bandwidth within safe operating boundaries.
4. **Use State Files for Resuming Large Audits:**
   - Feroxbuster periodically saves state files (`.state`). If an interrupted scan occurs, resume instantly with `feroxbuster --resume-from <FILE>.state`.

---

## ⚠️ Legal Disclaimer

> **IMPORTANT:** Feroxbuster is built strictly for authorized security assessments, CTF competitions, and penetration tests conducted with written permission. Scanning unauthorized targets can saturate network infrastructure and is strictly prohibited by law.

---

## 📚 Resources

- [Feroxbuster GitHub Repository](https://github.com/epi052/feroxbuster)
- [Official Feroxbuster Documentation](https://epi052.github.io/feroxbuster-docs/)
- [SecLists Discovery Lists](https://github.com/danielmiessler/SecLists)
