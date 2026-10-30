# 📡 dnsx - Complete Cheatsheet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)

```
  ██████╗ ███╗   ██╗███████╗██╗  ██╗
  ██╔══██╗████╗  ██║██╔════╝╚██╗██╔╝
  ██║  ██║██╔██╗ ██║███████╗ ╚███╔╝ 
  ██║  ██║██║╚██╗██║╚════██║ ██╔██╗ 
  ██████╔╝██║ ╚████║███████║██╔╝ ██╗
  ╚═════╝ ╚═╝  ╚═══╝╚══════╝╚═╝  ╚═╝
       Fast & Multi-Purpose DNS Toolkit by ProjectDiscovery
```

<p align="center">
  <img src="https://img.shields.io/badge/dnsx-DNS_Toolkit-blue?style=for-the-badge" alt="dnsx">
  <img src="https://img.shields.io/badge/Developer-ProjectDiscovery-orange?style=for-the-badge" alt="ProjectDiscovery">
  <img src="https://img.shields.io/badge/Category-Recon_&_Scanning-green?style=for-the-badge" alt="Recon & Scanning">
  <img src="https://img.shields.io/badge/License-MIT-red?style=for-the-badge" alt="License MIT">
</p>

<p align="center">
  <b>🌐 High-performance DNS resolution, wildcard filtering, reverse DNS lookups, and multi-query extraction engine</b>
</p>

---

## 📋 Table of Contents

- [Overview & Architecture](#-overview--architecture)
- [Why dnsx? (Wildcard Handling & Speed)](#-why-dnsx-wildcard-handling--speed)
- [Installation](#-installation)
- [Basic Subdomain Probing & Resolution](#-basic-subdomain-probing--resolution)
- [Wildcard DNS Filtering (`-wd`)](#-wildcard-dns-filtering---wd)
- [Querying Specific DNS Record Types](#-querying-specific-dns-record-types)
- [Reverse DNS Lookups (`-ptr`)](#-reverse-dns-lookups---ptr)
- [Using Custom Resolvers & Rate Limiting](#-using-custom-resolvers--rate-limiting)
- [Pipeline Automation (Subfinder + dnsx + httpx)](#-pipeline-automation-subfinder--dnsx--httpx)
- [Quick Reference Table](#-quick-reference-table)
- [Defensive Considerations & OPSEC](#-defensive-considerations--opsec)
- [Legal Disclaimer](#-legal-disclaimer)

---

## 🎯 Overview & Architecture

Engineered in Go by **ProjectDiscovery**, **dnsx** is a fast and multipurpose DNS toolkit designed for reconnaissance, bug bounty hunting, and network discovery.

While tools like `dig`, `nslookup`, and `host` resolve records one by one, `dnsx` processes thousands of queries asynchronously, automatically retries failed resolvers, extracts clean response IPs, and eliminates wildcard noise that pollutes subdomain enumeration.

---

## ⚡ Why dnsx? (Wildcard Handling & Speed)

During bug bounty reconnaissance, passive tools (`subfinder`, `amass`) often return thousands of dead or wildcard subdomains. Testing HTTP endpoints on dead DNS entries wastes massive bandwidth.

`dnsx` solves this by:
1. **Filtering out Wildcards (`-wd`):** Detects if `*.domain.com` resolves to a catch-all IP and filters out phantom subdomains.
2. **Speed & Concurrency:** Resolves 10,000+ domains per minute across public and private resolver pools.
3. **Clean Output Piping:** Feeds directly into `httpx` or `nmap`.

---

## 📥 Installation

```bash
# Go Install (Recommended)
go install -v github.com/projectdiscovery/dnsx/cmd/dnsx@latest

# Kali Linux & Debian
sudo apt update && sudo apt install -y dnsx

# MacOS (Homebrew)
brew install dnsx

# Verify installation
dnsx -version
```

---

## 🚀 Basic Subdomain Probing & Resolution

```bash
# Resolve a single domain
echo "target.com" | dnsx

# Resolve a list of subdomains from file
dnsx -l subdomains.txt

# Display domain and its corresponding A record IP
dnsx -l subdomains.txt -a -resp

# Output only resolved IP addresses (ready for Nmap)
dnsx -l subdomains.txt -resp-only
```

---

## 🚫 Wildcard DNS Filtering (`-wd`)

Wildcards return a valid IP for non-existent subdomains (`random123xyz.target.com`), poisoning reconnaissance datasets.

```bash
# Filter wildcard subdomains and print only genuine hosts
dnsx -l subdomains.txt -wd target.com -o valid_hosts.txt

# Filter wildcards with custom threshold
dnsx -l subdomains.txt -wd target.com -wt 3
```

---

## 📋 Querying Specific DNS Record Types

Query specific record types across thousands of domains:

```bash
# Query CNAME records (Crucial for Subdomain Takeover detection)
dnsx -l subdomains.txt -cname -resp

# Query Mail Exchanger (MX) records
dnsx -l domains.txt -mx -resp

# Query TXT records (SPF, DKIM, verification tokens)
dnsx -l domains.txt -txt -resp

# Query All standard records (A, AAAA, CNAME, NS, TXT, MX, SOA)
dnsx -l domains.txt -recon
```

---

## 🔄 Reverse DNS Lookups (`-ptr`)

Discover active infrastructure and virtual hosts by performing PTR lookups on an IP range:

```bash
# Reverse resolve a CIDR IP range to hostnames
dnsx -ptr -l ip_ranges.txt -resp

# Reverse resolve a single subnet
echo "192.168.1.0/24" | dnsx -ptr -resp
```

---

## ⚙️ Using Custom Resolvers & Rate Limiting

Avoid being rate-limited by your ISP or Google DNS by utilizing a high-speed curated resolver pool:

```bash
# Use a custom trusted resolver list
dnsx -l subdomains.txt -r resolvers.txt

# Set thread concurrency and query rate limit (queries per second)
dnsx -l subdomains.txt -t 100 -rl 300
```

---

## ⛓️ Pipeline Automation (Subfinder + dnsx + httpx)

The ultimate recon workflow for bug bounty hunters and penetration testers:

```bash
# Discover subdomains -> Validate via dnsx -> Probe live HTTP web services
subfinder -d target.com -silent | \
  dnsx -silent -wd target.com | \
  httpx -silent -status-code -title -tech-detect
```

---

## ⚡ Quick Reference Table

| Objective | Command |
|-----------|---------|
| **Resolve Subdomains** | `dnsx -l subdomains.txt` |
| **Extract IP Addresses Only** | `dnsx -l subdomains.txt -resp-only` |
| **Filter Wildcard Domains** | `dnsx -l subdomains.txt -wd <DOMAIN>` |
| **Extract CNAME (Takeover)** | `dnsx -l subdomains.txt -cname -resp` |
| **Query All Records (Recon)** | `dnsx -l domains.txt -recon` |
| **Reverse DNS Lookup (PTR)** | `dnsx -l ips.txt -ptr -resp` |
| **Custom Resolvers & Threads**| `dnsx -l list.txt -r resolvers.txt -t 150` |
| **JSON Output** | `dnsx -l list.txt -json -o results.json` |

---

## 🛡️ Defensive Considerations & OPSEC

1. **DNS Flooding:** Firing tens of thousands of DNS queries through corporate internal DNS servers will spike CPU loads and trigger SIEM alerts for DNS tunneling/DDoS.
2. **Resolver Poisoning:** Always use verified, trusted resolver lists to prevent malicious resolvers from intercepting queries.

---

## ⚠️ Legal Disclaimer

> [!CAUTION]
> This cheatsheet is for authorized penetration testing and bug bounty engagements only. Conducting high-volume DNS probes against unauthorized infrastructure is strictly prohibited.