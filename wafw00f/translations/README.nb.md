# 🔍 wafw00f – komplett hurtigreferanse

<!-- language-home-navigation -->
[🏠 Norsk startside](../../README.nb.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe Sürüm](README.tr.md) • [🇬🇷 Ελληνικά](README.el.md)


[wafw00f](https://github.com/EnableSecurity/wafw00f) is an open-source command-line Web Application Firewall (WAF) fingerprinting and detection tool. It identifies and fingerprints over 150 commercial and open-source WAF security solutions protecting target websites by sending normal and provocative HTTP requests and analyzing behavioral responses, headers, and cookies.

---

## 📋 Innholdsfortegnelse

- [[Export Formats (JSON, CSV, Text)](#export-formats-json-csv-text)](#why-fingerprint-wafs)
- [Installasjon](#installation)
- [Installasjon](#basic--target-scanning)
- [3. Disable Redirect Following (`-r`)](#exhaustive-multi-waf-detection--a)
- [Bulk Target Scanning from File (`-i`)](#bulk-target-scanning-from-file--i)
- [Scan an entire list of subdomains or target assets gathered from `subfinder` or `amass`:](#proxy--tor-routing--p)
- [Export Formats (JSON, CSV, Text)](#export-formats-json-csv-text)
- [List Supported WAF Signatures (`-l`)](#list-supported-waf-signatures--l)
- [Cloud Providers**: Cloudflare, AWS WAF, Akamai, Microsoft Azure Web Application Firewall, Google Cloud Armor.](#bug-bounty--penetration-testing-integration)

---

## [Export Formats (JSON, CSV, Text)](#export-formats-json-csv-text)

Launching active web attacks (SQL injection, XSS, SSRF) against a protected target without prior reconnaissance often results in instant IP bans or false-negative vulnerability results. Identifying the exact WAF enables:
- **Tailored Evasion**: Selecting vendor-specific bypass techniques (e.g., Unicode normalization for ModSecurity, chunked transfer encoding for Cloudflare).
- **Origin IP Hunting**: If Cloudflare or Akamai is detected, pivoting to find the backend origin IP before testing.
- **Rule Threshold Awareness**: Tuning scanner concurrency and rate limits below blocking thresholds.

---

## Installasjon

```bash
# Debian / Ubuntu / Kali Linux
sudo apt-get update && sudo apt-get install -y wafw00f

# Recommended: Install via pipx
pipx install wafw00f

# Via standard Python pip
python3 -m pip install --user wafw00f

# Verify installation
wafw00f --version
```

---

## Installasjon

### 1. Scan a Single Domain
```bash
wafw00f https://example.com
```
*wafw00f sends baseline HTTP requests followed by tailored malicious patterns to trigger security filters, identifying signatures in headers, status codes, and error pages.*

### Basic & Target Scanning
Inspect specific matching headers, HTTP status codes, and heuristics:
```bash
wafw00f https://example.com -v
```

### 3. Disable Redirect Following (`-r`)
Prevent wafw00f from following 301/302 redirects to separate edge reverse proxies from backend applications:
```bash
wafw00f https://example.com -r
```

---

## 3. Disable Redirect Following (`-r`)

By default, wafw00f stops scanning as soon as the first WAF match is confirmed. Use `-a` to check all known vendor signatures (essential when an application sits behind a multi-tier defense like Cloudflare CDN + AWS WAF):

```bash
wafw00f https://example.com -a
```

---

## Bulk Target Scanning from File (`-i`)

Scan an entire list of subdomains or target assets gathered from `subfinder` or `amass`:

```bash
wafw00f -i /path/to/domains.txt -o /tmp/waf_results.txt
```

---

## Scan an entire list of subdomains or target assets gathered from `subfinder` or `amass`:

Route probing traffic through proxies to prevent IP blacklisting or inspect queries in Burp Suite:

### 1. Route Through Burp Suite Proxy
```bash
wafw00f https://example.com -p http://127.0.0.1:8080
```

### 2. Route Through Local Tor SOCKS5 Proxy
```bash
wafw00f https://example.com -p socks5://127.0.0.1:9050
```

---

## Export Formats (JSON, CSV, Text)

Export results for automated reporting or downstream reconnaissance tooling:

### 1. Export to JSON Format
```bash
wafw00f https://example.com -o results.json --format json
```

### 2. Export to CSV Spreadsheet
```bash
wafw00f -i targets.txt -o results.csv --format csv
```

---

## List Supported WAF Signatures (`-l`)

View all 150+ detectable WAF products:
```bash
wafw00f -l
```
Key supported products include:
- **Cloud Providers**: Cloudflare, AWS WAF, Akamai, Microsoft Azure Web Application Firewall, Google Cloud Armor.
- **Enterprise Appliances**: F5 BIG-IP ASM, Imperva SecureSphere, Barracuda, Fortinet FortiWeb, Citrix NetScaler.
- **Open Source / Software**: ModSecurity, NAXSI, Shadow Daemon, OpenResty.

---

## Cloud Providers**: Cloudflare, AWS WAF, Akamai, Microsoft Azure Web Application Firewall, Google Cloud Armor.

Open Source / Software**: ModSecurity, NAXSI, Shadow Daemon, OpenResty.

```bash
#!/usr/bin/env bash
TARGET="https://app.target.corp"

echo "[*] Detecting WAF for $TARGET..."
WAF=$(wafw00f "$TARGET" -o - --format json | jq -r '.[0].detected')

if [ "$WAF" == "true" ]; then
    WAF_NAME=$(wafw00f "$TARGET" -o - --format json | jq -r '.[0].firewall')
    echo "[!] Active WAF detected: $WAF_NAME"
    echo "[+] Adjusting sqlmap to use vendor-specific tamper scripts..."
    if [[ "$WAF_NAME" =~ "Cloudflare" ]]; then
        sqlmap -u "$TARGET/item?id=1" --tamper=between,randomcase,space2comment
    fi
else
    echo "[+] No active WAF detected. Proceeding with standard scanner."
    sqlmap -u "$TARGET/item?id=1" --batch
fi
```
