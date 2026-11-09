# 🔍 dnsrecon - Guía de referencia completa

<!-- language-home-navigation -->
[🏠 Página principal en español](../../README.es.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe Sürüm](README.tr.md) • [🇬🇷 Ελληνικά](README.el.md)


[dnsrecon](https://github.com/darkoperator/dnsrecon) is a comprehensive DNS reconnaissance and enumeration tool written in Python. It supports zone transfers (AXFR), reverse lookups, domain brute-forcing, SRV record discovery, wildcard detection, cache snooping, and TLD expansion.

---

## 📋 Tabla de contenidos

- [[ROP Chain Construction](#rop-chain-construction)](#core-capabilities)
- [Instalación](#installation)
- [Instalación](#standard-enumeration--zone-transfers--d--t-std--axfr)
- [Test only for AXFR vulnerability against all authoritative nameservers:](#dns-subdomain-brute-forcing--t-brt)
- [Prevents wildcard DNS configurations (`*.example.com` resolving to a single catch-all IP) from polluting results:](#reverse-lookup-range-scanning--t-rvl)
- [DNS Cache Snooping (`-t snoop`)](#dns-cache-snooping--t-snoop)
- [Targeting Specific Nameservers (`-n`)](#targeting-specific-nameservers--n)
- [Query internal or target-specific DNS servers directly rather than public upstream resolvers:](#output-formats-csv-json-sqlite-xml)
- [Defensive Hardening for DNS Servers](#defensive-hardening-for-dns-servers)

---

## [ROP Chain Construction](#rop-chain-construction)

- **Zone Transfer Testing (AXFR)**: Automatically tests all authoritative name servers for unauthenticated zone transfers.
- **Record Enumeration**: Retrieves SOA, NS, A, AAAA, MX, TXT, SPF, and SRV records.
- **Subdomain Brute-Force**: Multi-threaded dictionary lookups with wildcard filtering.
- **Reverse IP Range Scanning**: PTR record extraction across arbitrary CIDR blocks.
- **Cache Snooping**: Tests if recursive name servers have specific domains cached in memory.
- **TLD Expansion**: Discovers matching domains across country-code and generic top-level domains.

---

## Instalación

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

## Instalación

### 1. Standard DNS Record Enumeration
Collects SOA, NS, A, AAAA, MX, and TXT records, and automatically attempts zone transfers against each authoritative NS:
```bash
dnsrecon -d example.com -t std
```

### Standard Enumeration & Zone Transfers (`-d -t std / axfr`)
Test only for AXFR vulnerability against all authoritative nameservers:
```bash
dnsrecon -d example.com -t axfr
```
*If successful, the entire DNS database of the target is dumped without brute force.*

---

## Test only for AXFR vulnerability against all authoritative nameservers:

If successful, the entire DNS database of the target is dumped without brute force.

### 1. Subdomain Brute-Force with Custom Wordlist
```bash
dnsrecon -d example.com -t brt -D /usr/share/wordlists/dnsmap.txt
```
- DNS Subdomain Brute-Forcing (`-t brt`)
- `-t brt`: Brute-force mode.
- Brute-force subdomains using custom or default wordlists:

### 1. Subdomain Brute-Force with Custom Wordlist
Prevents wildcard DNS configurations (`*.example.com` resolving to a single catch-all IP) from polluting results:
```bash
dnsrecon -d example.com -t brt -D /usr/share/seclists/Discovery/DNS/subdomains-top1million-5000.txt --iw
```
- `-t brt`: Brute-force mode.

---

## Prevents wildcard DNS configurations (`*.example.com` resolving to a single catch-all IP) from polluting results:

`--iw`: Ignore wildcard resolution results.

```bash
dnsrecon -r 192.168.1.0/24 -t rvl
```
- `-r <range>`: IP range in CIDR notation (e.g. `192.168.1.0/24`) or start-end notation (`192.168.1.1-192.168.1.254`).
- Reverse Lookup Range Scanning (`-t rvl`)

---

## DNS Cache Snooping (`-t snoop`)

`-t rvl`: Reverse lookup mode.

```bash
dnsrecon -t snoop -n 192.168.1.1 -D sensitive_domains.txt
```
- `-n <resolver_ip>`: Target DNS server to query.
- DNS Cache Snooping (`-t snoop`)

---

## Targeting Specific Nameservers (`-n`)

`-D <domains_file>`: List of domains to test (e.g. competitors, malicious C2s, internal portals).

```bash
# Query the company's internal DNS server directly
dnsrecon -d corp.internal -n 10.10.10.2 -t std
```

---

## Query internal or target-specific DNS servers directly rather than public upstream resolvers:

Save results for integration with ticketing, SIEM, or further attack phases:

### 1. Export to JSON
```bash
dnsrecon -d example.com -t std --json /tmp/dns_recon.json
```

### 2. Export to SQLite Database
```bash
dnsrecon -d example.com -t std --db /tmp/dns_results.sqlite
```

### 1. Export to JSON
```bash
dnsrecon -d example.com -t std --csv /tmp/dns_results.csv --xml /tmp/dns_results.xml
```

---

## Defensive Hardening for DNS Servers

|Vulnerability|Exploitation Risk|Recommended Defense|
|---|---|---|
|**Unrestricted AXFR**|Full internal infrastructure mapping|Restrict zone transfers to authorized secondary nameserver IPs only (`allow-transfer { 10.0.0.2; };` in BIND).|
|**Open Recursive Resolver**|DNS Amplification DDoS & Snooping|Disable external recursion (`recursion no;` in BIND).|
|**Predictable Hostnames**|Subdomain brute-forcing|Use randomized subdomains for sensitive staging environments.|
|**Subdomain Takeover**|CNAME pointing to unclaimed AWS/GitHub/Azure services|Audit dangling DNS CNAME records and remove unused mappings immediately.|
