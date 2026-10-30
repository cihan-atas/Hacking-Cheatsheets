# dnsrecon Cheatsheet

[dnsrecon](https://github.com/darkoperator/dnsrecon) is a comprehensive DNS reconnaissance and enumeration tool written in Python. It supports zone transfers (AXFR), reverse lookups, domain brute-forcing, SRV record discovery, wildcard detection, cache snooping, and TLD expansion.

---

## Table of Contents

- [Core Capabilities](#core-capabilities)
- [Installation](#installation)
- [Standard Enumeration & Zone Transfers (`-d -t std / axfr`)](#standard-enumeration--zone-transfers--d--t-std--axfr)
- [DNS Subdomain Brute-Forcing (`-t brt`)](#dns-subdomain-brute-forcing--t-brt)
- [Reverse Lookup Range Scanning (`-t rvl`)](#reverse-lookup-range-scanning--t-rvl)
- [DNS Cache Snooping (`-t snoop`)](#dns-cache-snooping--t-snoop)
- [Targeting Specific Nameservers (`-n`)](#targeting-specific-nameservers--n)
- [Output Formats (CSV, JSON, SQLite, XML)](#output-formats-csv-json-sqlite-xml)
- [Defensive Hardening for DNS Servers](#defensive-hardening-for-dns-servers)

---

## Core Capabilities

- **Zone Transfer Testing (AXFR):** Automatically tests all authoritative name servers for unauthenticated zone transfers.
- **Record Enumeration:** Retrieves SOA, NS, A, AAAA, MX, TXT, SPF, and SRV records.
- **Subdomain Brute-Force:** Multi-threaded dictionary lookups with wildcard filtering.
- **Reverse IP Range Scanning:** PTR record extraction across arbitrary CIDR blocks.
- **Cache Snooping:** Tests if recursive name servers have specific domains cached in memory.
- **TLD Expansion:** Discovers matching domains across country-code and generic top-level domains.

---

## Installation

```bash
# Debian / Ubuntu / Kali Linux
sudo apt-get update && sudo apt-get install -y dnsrecon

# Python pip / pipx
pipx install dnsrecon

# From source
git clone https://github.com/darkoperator/dnsrecon.git
cd dnsrecon
pip install -r requirements.txt

# Verify installation
dnsrecon -h
```

---

## Standard Enumeration & Zone Transfers (`-d -t std / axfr`)

### 1. Standard DNS Record Enumeration
Collects SOA, NS, A, AAAA, MX, and TXT records, and automatically attempts zone transfers against each authoritative NS:
```bash
dnsrecon -d example.com -t std
```

### 2. Dedicated Zone Transfer (AXFR) Test Only
Test only for AXFR vulnerability against all authoritative nameservers:
```bash
dnsrecon -d example.com -t axfr
```
*If successful, the entire DNS database of the target is dumped without brute force.*

---

## DNS Subdomain Brute-Forcing (`-t brt`)

Brute-force subdomains using custom or default wordlists:

### 1. Subdomain Brute-Force with Custom Wordlist
```bash
dnsrecon -d example.com -t brt -D /usr/share/wordlists/dnsmap.txt
```
- `-d <domain>`: Target domain.
- `-t brt`: Brute-force mode.
- `-D <file>`: Subdomain dictionary file.

### 2. Brute-Force with Wildcard Filtering
Prevents wildcard DNS configurations (`*.example.com` resolving to a single catch-all IP) from polluting results:
```bash
dnsrecon -d example.com -t brt -D /usr/share/seclists/Discovery/DNS/subdomains-top1million-5000.txt --iw
```
- `--iw`: Ignore wildcard resolution results.

---

## Reverse Lookup Range Scanning (`-t rvl`)

Enumerate PTR records for an IP block to identify internal hosts, mail gateways, and staging instances:

```bash
dnsrecon -r 192.168.1.0/24 -t rvl
```
- `-r <range>`: IP range in CIDR notation (e.g. `192.168.1.0/24`) or start-end notation (`192.168.1.1-192.168.1.254`).
- `-t rvl`: Reverse lookup mode.

---

## DNS Cache Snooping (`-t snoop`)

Determine whether clients on a target network have recently resolved specific domains through their internal resolver:

```bash
dnsrecon -t snoop -n 192.168.1.1 -D sensitive_domains.txt
```
- `-n <resolver_ip>`: Target DNS server to query.
- `-D <domains_file>`: List of domains to test (e.g. competitors, malicious C2s, internal portals).

---

## Targeting Specific Nameservers (`-n`)

Query internal or target-specific DNS servers directly rather than public upstream resolvers:

```bash
# Query the company's internal DNS server directly
dnsrecon -d corp.internal -n 10.10.10.2 -t std
```

---

## Output Formats (CSV, JSON, SQLite, XML)

Save results for integration with ticketing, SIEM, or further attack phases:

### 1. Export to JSON
```bash
dnsrecon -d example.com -t std --json /tmp/dns_recon.json
```

### 2. Export to SQLite Database
```bash
dnsrecon -d example.com -t std --db /tmp/dns_results.sqlite
```

### 3. Export to CSV & XML
```bash
dnsrecon -d example.com -t std --csv /tmp/dns_results.csv --xml /tmp/dns_results.xml
```

---

## Defensive Hardening for DNS Servers

| Vulnerability | Exploitation Risk | Recommended Defense |
|---|---|---|
| **Unrestricted AXFR** | Full internal infrastructure mapping | Restrict zone transfers to authorized secondary nameserver IPs only (`allow-transfer { 10.0.0.2; };` in BIND). |
| **Open Recursive Resolver** | DNS Amplification DDoS & Snooping | Disable external recursion (`recursion no;` in BIND). |
| **Predictable Hostnames** | Subdomain brute-forcing | Use randomized subdomains for sensitive staging environments. |
| **Subdomain Takeover** | CNAME pointing to unclaimed AWS/GitHub/Azure services | Audit dangling DNS CNAME records and remove unused mappings immediately. |
