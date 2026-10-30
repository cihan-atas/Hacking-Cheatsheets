# 📁 DIRB - Complete Cheatsheet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)

```
  ██████╗ ██╗██████╗ ██████╗ 
  ██╔══██╗██║██╔══██╗██╔══██╗
  ██║  ██║██║██████╔╝██████╔╝
  ██║  ██║██║██╔══██╗██╔══██╗
  ██████╔╝██║██║  ██║██████╔╝
  ╚═════╝ ╚═╝╚═╝  ╚═╝╚═════╝ 
        Web Content Scanner
```

<p align="center">
  <img src="https://img.shields.io/badge/DIRB-Content_Scanner-orange?style=for-the-badge" alt="DIRB">
  <img src="https://img.shields.io/badge/Wordlists-Bruteforce-blue?style=for-the-badge" alt="Wordlists">
  <img src="https://img.shields.io/badge/Kali_Linux-Preinstalled-green?style=for-the-badge" alt="Kali Linux">
  <img src="https://img.shields.io/badge/Web-Pentesting-red?style=for-the-badge" alt="Web Pentesting">
</p>

<p align="center">
  <b>🔍 Classic web content scanner launching dictionary-based attacks against web servers</b>
</p>

---

## 📋 Table of Contents

- [Introduction](#-introduction)
- [DIRB vs Modern Alternatives](#-dirb-vs-modern-alternatives)
- [Installation](#-installation)
- [Default Wordlists](#-default-wordlists)
- [Basic Syntax](#-basic-syntax)
- [Common Scanning Scenarios](#-common-scanning-scenarios)
- [Advanced Options & Tuning](#-advanced-options--tuning)
- [Vulnerability Hunting with Built-in Lists](#-vulnerability-hunting-with-built-in-lists)
- [Real-World Workflows](#-real-world-workflows)
- [Quick Reference Table](#-quick-reference-table)
- [Tips & Best Practices](#-tips--best-practices)
- [Legal Disclaimer](#-legal-disclaimer)
- [Resources](#-resources)

---

## 🎯 Introduction

**DIRB** (Directory Buster) is a classic, lightweight, URL-based web content scanner written in C. It launches dictionary-based attacks against web servers, looking for existing and hidden web objects (files, directories, scripts, and endpoints).

DIRB works by generating HTTP requests for each word in a wordlist, analyzing the server's response codes (200, 301, 302, 403, etc.), and determining whether a resource exists.

### Key Characteristics

- ✅ **Automatic Recursive Scanning:** Scans discovered subdirectories automatically by default.
- ✅ **Built-in Specialized Wordlists:** Ships with targeted wordlists for common web servers, CMSs, and vulnerabilities.
- ✅ **Low Resource Footprint:** Lightweight binary that runs smoothly even on low-spec hardware or older systems.
- ✅ **Custom Header & Proxy Support:** Easy integration with Burp Suite and upstream HTTP proxies.

---

## ⚖️ DIRB vs Modern Alternatives

| Feature | DIRB | Gobuster | ffuf | Feroxbuster | dirsearch |
|---------|------|----------|------|-------------|-----------|
| **Language** | C | Go | Go | Rust | Python |
| **Speed** | ⭐⭐ (Low thread) | ⭐⭐⭐⭐⭐ (Fast) | ⭐⭐⭐⭐⭐ (Extremely fast) | ⭐⭐⭐⭐⭐ (Async Rust) | ⭐⭐⭐⭐ (Fast) |
| **Default Recursion** | ✅ Yes (Automatic) | ❌ No | ❌ No (Optional `-recursion`) | ✅ Yes (Automatic) | ✅ Yes (`-r`) |
| **Custom Wordlists Included** | ✅ Yes (`/usr/share/dirb/`) | ❌ No | ❌ No | ❌ No | ✅ Yes (`db/dicc.txt`) |
| **VHost Fuzzing** | ❌ No | ✅ Yes | ✅ Yes | ❌ No | ✅ Yes (`--subdirs`) |
| **POST / Data Fuzzing** | ❌ No | ❌ No | ✅ Yes | ❌ No | ✅ Yes (`-d`) |
| **Memory / CPU Usage** | Very Low | Low | Low | Very Low | Moderate |

---

## 📥 Installation

### Kali Linux & Parrot OS

DIRB comes pre-installed in Kali Linux and Parrot OS:

```bash
# Verify installation
dirb

# Update to latest package
sudo apt update && sudo apt install -y dirb
```

### Debian / Ubuntu

```bash
sudo apt update
sudo apt install -y dirb
```

### Compiling from Source

```bash
# Install build dependencies
sudo apt install -y build-essential libcurl4-openssl-dev

# Download and extract source
wget https://sourceforge.net/projects/dirb/files/dirb/2.22/dirb222.tar.gz
tar -xzvf dirb222.tar.gz
cd dirb222

# Configure permissions and compile
chmod -R 755 conf
chmod 755 configure
./configure
make
sudo make install
```

---

## 📚 Default Wordlists

When installed on Kali Linux, DIRB includes dedicated wordlists located at `/usr/share/dirb/wordlists/`:

| Wordlist Path | Entries | Purpose |
|---------------|---------|---------|
| `/usr/share/dirb/wordlists/common.txt` | ~4,614 | Default list used when no wordlist is specified |
| `/usr/share/dirb/wordlists/small.txt` | ~959 | Rapid discovery of high-probability paths |
| `/usr/share/dirb/wordlists/big.txt` | ~20,469 | Exhaustive directory and file search |
| `/usr/share/dirb/wordlists/catala.txt` | ~6,846 | Catalan wordlist |
| `/usr/share/dirb/wordlists/spanish.txt` | ~11,720 | Spanish wordlist |
| `/usr/share/dirb/wordlists/mutations_common.txt` | ~1,000 | Common prefix/suffix variations |
| `/usr/share/dirb/wordlists/extensions_common.txt` | ~40 | Standard web extensions (`.php`, `.asp`, `.html`, etc.) |

### Built-in Vulnerability Wordlists (`/usr/share/dirb/wordlists/vulns/`)

- `apache.txt` - Apache-specific files, status pages, and known directories
- `iis.txt` - Microsoft IIS web server files (`web.config`, `aspnet_client`, etc.)
- `tomcat.txt` - Apache Tomcat administration and manager paths
- `cgis.txt` - Common Gateway Interface (CGI) scripts and endpoints
- `fatwire.txt`, `hpsmh.txt`, `hyperion.txt`, `iplanet.txt`, `netware.txt`, `oracle.txt`

---

## ⌨️ Basic Syntax

```bash
dirb <url_base> [<wordlist_file(s)>] [options]
```

### Essential Examples

```bash
# Basic scan using default common.txt wordlist
dirb http://10.10.10.10

# Scan using a specific custom wordlist
dirb https://example.com /usr/share/wordlists/dirb/big.txt

# Scan with custom SecLists directory list
dirb http://target.local /usr/share/seclists/Discovery/Web-Content/directory-list-2.3-medium.txt
```

---

## 🔍 Common Scanning Scenarios

### 1. Scanning with File Extensions (`-X` and `-x`)

Search for specific file extensions attached to each word in the dictionary:

```bash
# Test specific extensions on every wordlist entry
dirb http://target.local -X .php,.html,.txt,.bak

# Use an extensions list file
dirb http://target.local -x /usr/share/dirb/wordlists/extensions_common.txt

# Combine directory brute-force with extension brute-force
dirb http://target.local /usr/share/dirb/wordlists/common.txt -X .php,.json,.zip
```

### 2. Controlling Recursion (`-r` and `-R`)

By default, DIRB recursively crawls every discovered directory. You can control this behavior:

```bash
# Disable recursion completely (scan root level only)
dirb http://target.local -r

# Interactive recursion (DIRB asks before descending into each directory)
dirb http://target.local -R
```

### 3. Case-Insensitive Scanning (`-i`)

Useful when targeting Windows / IIS servers where file paths are case-insensitive:

```bash
dirb http://target.local -i
```

### 4. Ignoring Specific HTTP Status Codes (`-N`)

Filter out unwanted response codes (e.g., redirect loops or forbidden paths):

```bash
# Ignore 302 redirects and 403 forbidden responses
dirb http://target.local -N 302,403
```

---

## ⚙️ Advanced Options & Tuning

### Authentication (`-u`)

DIRB supports HTTP Basic and Digest authentication:

```bash
# Supply HTTP authentication credentials
dirb http://target.local -u "admin:SecretP@ssw0rd!"

# Combine auth with custom wordlist
dirb http://target.local /usr/share/dirb/wordlists/big.txt -u "pentester:Summer2026"
```

### Custom Cookies & Session Tokens (`-c`)

Test authenticated web application routes by providing session cookies:

```bash
# Pass session cookie
dirb http://target.local -c "PHPSESSID=d92j4fn82mdf82ncb184; logged_in=true"
```

### Custom User-Agent & HTTP Headers (`-a` and `-H`)

Bypass basic WAF rules or simulate legitimate browsers:

```bash
# Set custom User-Agent
dirb http://target.local -a "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36"

# Add custom HTTP Header
dirb http://target.local -H "Authorization: Bearer eyJhbGciOi..."
dirb http://target.local -H "X-Forwarded-For: 127.0.0.1"
```

### Proxy Routing & Burp Suite Integration (`-p` and `-P`)

Send DIRB traffic through an upstream HTTP proxy like Burp Suite or OWASP ZAP:

```bash
# Route through local Burp Suite proxy
dirb http://target.local -p 127.0.0.1:8080

# Authenticated proxy
dirb http://target.local -p 10.10.10.1:3128 -P "proxyuser:proxypass"
```

### Rate Limiting & Evasion (`-z`)

Avoid triggering IDS/IPS alerts or DoSing fragile web services by adding a delay between requests (in milliseconds):

```bash
# Wait 250 milliseconds between each request
dirb http://target.local -z 250

# Slow, stealthy scan (1 second delay)
dirb http://target.local -z 1000
```

### Output Logging (`-o` and `-w`)

```bash
# Save results to a file
dirb http://target.local -o /home/zeus/scans/dirb_target.txt

# Do not halt on WARNING messages (useful for uninterrupted scans)
dirb http://target.local -w -o dirb_output.txt
```

---

## 🎯 Vulnerability Hunting with Built-in Lists

DIRB includes targeted lists in `/usr/share/dirb/wordlists/vulns/` for detecting unpatched servers and dangerous endpoints:

```bash
# Target Apache server vulnerabilities and status pages
dirb http://target.local /usr/share/dirb/wordlists/vulns/apache.txt

# Target IIS endpoints and misconfigurations
dirb http://target.local /usr/share/dirb/wordlists/vulns/iis.txt

# Target Tomcat management consoles
dirb http://target.local /usr/share/dirb/wordlists/vulns/tomcat.txt

# Search for old CGI scripts and shellshock entrypoints
dirb http://target.local /usr/share/dirb/wordlists/vulns/cgis.txt
```

---

## 🎬 Real-World Workflows

### Workflow 1: Quick Initial Reconnaissance

When you first encounter a web server on port 80/443:

```bash
# Rapid scan without recursion using small wordlist
dirb http://10.10.10.50 /usr/share/dirb/wordlists/small.txt -r
```

### Workflow 2: Exhaustive Web Application Assessment

Perform a deep scan looking for configuration backups, source code, and API endpoints:

```bash
dirb https://app.target.local /usr/share/dirb/wordlists/common.txt \
  -X .php,.bak,.sql,.config,.txt \
  -a "Mozilla/5.0" \
  -w \
  -o dirb_app_scan.txt
```

### Workflow 3: Authenticated Portal Crawl via Burp Suite

```bash
dirb https://internal.target.local/portal/ /usr/share/dirb/wordlists/big.txt \
  -c "session=s%3A_kJw81..." \
  -p 127.0.0.1:8080 \
  -X .jsp,.do,.action \
  -z 100
```

---

## 📊 Quick Reference Table

| Flag | Argument | Description | Example |
|------|----------|-------------|---------|
| `-a` | `<user_agent>` | Specify custom User-Agent string | `dirb http://target -a "Mozilla/5.0"` |
| `-c` | `<cookie>` | Set HTTP Cookie header | `dirb http://target -c "id=123"` |
| `-H` | `<header>` | Add custom HTTP header | `dirb http://target -H "X-API-Key: secret"` |
| `-i` | None | Case-insensitive search | `dirb http://target -i` |
| `-N` | `<codes>` | Ignore responses matching status code | `dirb http://target -N 302,403` |
| `-o` | `<output_file>` | Save scan results to file | `dirb http://target -o results.txt` |
| `-p` | `<proxy:port>` | Use HTTP proxy | `dirb http://target -p 127.0.0.1:8080` |
| `-P` | `<user:pass>` | Proxy authentication credentials | `dirb http://target -p proxy:8080 -P u:p` |
| `-r` | None | Don't search recursively | `dirb http://target -r` |
| `-R` | None | Interactive recursive search | `dirb http://target -R` |
| `-u` | `<user:pass>` | HTTP Basic/Digest authentication | `dirb http://target -u "admin:1234"` |
| `-w` | None | Don't stop scan on WARNING messages | `dirb http://target -w` |
| `-X` | `<ext_list>` | Append comma-separated extensions | `dirb http://target -X .php,.html` |
| `-x` | `<ext_file>` | Append extensions from file | `dirb http://target -x ext.txt` |
| `-z` | `<millisecs>` | Add delay between requests | `dirb http://target -z 250` |

---

## 💡 Tips & Best Practices

1. **Watch Out for Wildcard 200/302 Responses:**
   - Some web applications return HTTP 200 or 302 for every request (custom 404 handler). DIRB will report every single entry as found. Use `-N 302` or switch to `ffuf` / `feroxbuster` for advanced size/word filtering.
2. **Combine with `-r` for Large Dictionaries:**
   - If using `directory-list-2.3-medium.txt` (220k words), always disable recursion with `-r`. Otherwise, DIRB may take days to crawl deeply nested directories.
3. **Always Check Extensions:**
   - Modern backends frequently expose sensitive files like `.env`, `.git`, `.bak`, `.conf`, or `.sql`. Always test common extensions with `-X`.
4. **Use `-w` for Unattended Scans:**
   - By default, DIRB may pause if it encounters SSL or network warnings. Passing `-w` ensures tests complete without manual interaction.

---

## ⚠️ Legal Disclaimer

> **IMPORTANT:** DIRB is designed for security auditing, penetration testing, and ethical research on systems you own or have explicit, documented permission to test. Unauthorized dictionary attacks against web servers can cause service disruption and are illegal under computer crime legislation worldwide.

---

## 📚 Resources

- [DIRB SourceForge Project](https://sourceforge.net/projects/dirb/)
- [Kali Linux DIRB Package Documentation](https://www.kali.org/tools/dirb/)
- [SecLists - Web Content Discovery Wordlists](https://github.com/danielmiessler/SecLists)
