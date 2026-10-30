# Sherlock Cheatsheet

[Sherlock](https://github.com/sherlock-project/sherlock) is an OSINT reconnaissance tool that hunts down social media accounts by username across more than 400 social platforms, forums, code hosting sites, and online communities simultaneously.

---

## Table of Contents

- [Key Capabilities](#key-capabilities)
- [Installation](#installation)
- [Single & Multiple Username Search](#single--multiple-username-search)
- [Filtering Sites & Platforms](#filtering-sites--platforms)
- [Proxy & Tor Routing](#proxy--tor-routing)
- [Timeout & Threading Tuning](#timeout--threading-tuning)
- [Export Formats (CSV, XLSX, JSON)](#export-formats-csv-xlsx-json)
- [Automation & OSINT Pipeline Integration](#automation--osint-pipeline-integration)

---

## Key Capabilities

- **400+ Platforms:** Queries GitHub, Twitter/X, Instagram, Reddit, TikTok, LinkedIn, Telegram, Discord, Steam, Spotify, Docker Hub, Pastebin, and hundreds more.
- **Async Concurrency:** High-speed parallel scanning using asynchronous Python requests.
- **Accurate Detection:** Detects HTTP status codes, error messages, and redirects to avoid false positives.
- **Tor & SOCKS5 Support:** Protects investigator anonymity and bypasses rate limits.

---

## Installation

```bash
# Recommended: Install via pipx
pipx install sherlock-project

# Or install from GitHub repository
git clone https://github.com/sherlock-project/sherlock.git
cd sherlock
python3 -m pip install -r requirements.txt

# Debian / Kali Linux repository
sudo apt-get update && sudo apt-get install -y sherlock

# Verify command
sherlock --version
```

---

## Single & Multiple Username Search

### 1. Basic Username Search
Search for a single target handle across all 400+ sites:
```bash
sherlock target_user
```

### 2. Search Multiple Usernames Simultaneously
```bash
sherlock target_user1 target_user2 alias_dev
```

### 3. Print Only Found Accounts (Suppress Not Found / Errors)
```bash
sherlock --print-found target_user
```

---

## Filtering Sites & Platforms

Narrow searches to specific platforms to save time or focus on specific intelligence categories:

### 1. Query Specific Sites Only
```bash
sherlock --site github --site twitter --site reddit --site gitlab target_user
```

### 2. Search Without Specific Platforms (Ignore False-Positive Heavy Sites)
```bash
sherlock target_user --site-list targets.txt
```

---

## Proxy & Tor Routing

To prevent IP bans, rate limits, and maintain operational security:

### 1. Route Through Tor Network
Automatically route requests through local Tor SOCKS proxy (`127.0.0.1:9050`):
```bash
sherlock --tor target_user
```

### 2. Custom HTTP / SOCKS5 Proxy
```bash
# SOCKS5 proxy
sherlock --proxy socks5://127.0.0.1:1080 target_user

# Authenticated HTTP proxy
sherlock --proxy http://user:pass@proxy.corp.internal:8080 target_user
```

---

## Timeout & Threading Tuning

Control request speeds and latency tolerances:

```bash
# Set HTTP timeout to 5 seconds per site (default is usually 60s)
sherlock target_user --timeout 5

# Add delay between requests to avoid rate limits
sherlock target_user --delay 1
```

---

## Export Formats (CSV, XLSX, JSON)

Save discoveries for investigations, evidence bundles, or reporting:

### 1. Save Directly to CSV File
```bash
sherlock --csv --output /tmp/sherlock_results target_user
```

### 2. Save Output into Specific Directory
```bash
sherlock --folderoutput /tmp/recon_data/ target_user
```
Generates `/tmp/recon_data/target_user.txt` containing full URLs of confirmed profiles.

### 3. Save as Excel Spreadsheet (XLSX)
```bash
sherlock --xlsx target_user
```

---

## Automation & OSINT Pipeline Integration

Chain Sherlock into an automated investigator script:

```bash
#!/usr/bin/env bash
TARGET="$1"
OUTDIR="/tmp/osint_${TARGET}"
mkdir -p "$OUTDIR"

echo "[*] Scanning for username: $TARGET"
sherlock --print-found --timeout 10 --folderoutput "$OUTDIR" "$TARGET"

if [ -f "$OUTDIR/${TARGET}.txt" ]; then
    echo "[+] Found $(wc -l < "$OUTDIR/${TARGET}.txt") accounts:"
    cat "$OUTDIR/${TARGET}.txt"
fi
```
