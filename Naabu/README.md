# ⚡ Naabu - Complete Cheatsheet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)

```
  ███╗   ██╗ █████╗  █████╗ ██████╗ ██╗   ██╗
  ████╗  ██║██╔══██╗██╔══██╗██╔══██╗██║   ██║
  ██╔██╗ ██║███████║███████║██████╔╝██║   ██║
  ██║╚██╗██║██╔══██║██╔══██║██╔══██╗██║   ██║
  ██║ ╚████║██║  ██║██║  ██║██████╔╝╚██████╔╝
  ╚═╝  ╚═══╝╚═╝  ╚═╝╚═╝  ╚═╝╚═════╝  ╚═════╝ 
        Fast & Reliable SYN/CONNECT Port Scanner
```

<p align="center">
  <img src="https://img.shields.io/badge/Naabu-Port_Scanner-blue?style=for-the-badge" alt="Naabu">
  <img src="https://img.shields.io/badge/Author-ProjectDiscovery-orange?style=for-the-badge" alt="ProjectDiscovery">
  <img src="https://img.shields.io/badge/Category-Recon_&_Scanning-green?style=for-the-badge" alt="Recon & Scanning">
  <img src="https://img.shields.io/badge/Language-Go-cyan?style=for-the-badge" alt="Go">
</p>

<p align="center">
  <b>⚡ Fast and reliable port scanner focused on simplicity and seamless piping with ProjectDiscovery tools (httpx, nuclei)</b>
</p>

---

## 📋 Table of Contents

- [Overview & Key Features](#-overview--key-features)
- [Installation](#-installation)
- [Basic Port Scanning](#-basic-port-scanning)
  - [Single Host Scan](#single-host-scan)
  - [Top Ports & Full Port Range](#top-ports--full-port-range)
- [Scan Modes: SYN (Raw Socket) vs CONNECT](#-scan-modes-syn-raw-socket-vs-connect)
- [Rate Limiting & Performance Tuning](#-rate-limiting--performance-tuning)
- [Multi-Target Scanning & CIDR Blocks](#-multi-target-scanning--cidr-blocks)
- [Seamless Pipeline Workflows](#-seamless-pipeline-workflows)
  - [Naabu + Httpx + Nuclei](#naabu--httpx--nuclei)
  - [Subfinder + Naabu](#subfinder--naabu)
- [Nmap Integration (`-nmap`)](#-nmap-integration--nmap)
- [Quick Reference Table](#-quick-reference-table)
- [Defensive Considerations & Detection](#-defensive-considerations--detection)
- [Legal Disclaimer](#-legal-disclaimer)

---

## 🎯 Overview & Key Features

Developed by **ProjectDiscovery**, **Naabu** is a port scanner designed to be fast, accurate, and optimized for integration into modern reconnaissance automation pipelines:
- **SYN & CONNECT Scans:** Automatic fallback to TCP CONNECT when raw socket privileges (`libpcap` / root) are absent.
- **ProjectDiscovery Ecosystem:** Native pipeline architecture allowing seamless piping from `subfinder` into `naabu`, then into `httpx` and `nuclei`.
- **Integrated Nmap Engine:** Capability to trigger active Nmap service and version scanning on discovered open ports automatically.
- **CDN Exclusion:** Automatically bypasses cloud CDN edge nodes (Cloudflare, Akamai, CloudFront) to prevent wasted bandwidth and false positives.

---

## 📥 Installation

```bash
# Using Go (Recommended)
go install -v github.com/projectdiscovery/naabu/v2/cmd/naabu@latest

# Binary download via pdtm (ProjectDiscovery Tool Manager)
pdtm -i naabu

# Arch Linux (AUR)
yay -S naabu-bin

# Kali Linux
sudo apt update && sudo apt install -y naabu
```

> [!NOTE]
> On Linux, granting `CAP_NET_RAW` allows running SYN scans without root:
> `sudo setcap cap_net_raw=+ep $(which naabu)`

---

## 🚀 Basic Port Scanning

### Single Host Scan
```bash
# Scan default top 100 ports
naabu -host example.com

# Scan specific ports
naabu -host example.com -p 80,443,8080,8443
```

### Top Ports & Full Port Range
```bash
# Scan top 1000 ports
naabu -host example.com -top-ports 1000

# Full port scan (1-65535)
naabu -host example.com -p -
```

---

## ⚡ Scan Modes: SYN (Raw Socket) vs CONNECT

- **SYN Scan (`-s syn` / root required):** Sends raw TCP SYN packets without finishing the 3-way handshake (half-open scan). Much faster and less noisy in application logs.
- **CONNECT Scan (`-s conn`):** Uses the standard OS `connect()` socket call. Operates without root privileges and penetrates restrictive virtual environments.

```bash
# Explicit SYN Scan (Requires sudo or CAP_NET_RAW)
sudo naabu -host example.com -s syn

# Explicit TCP Connect Scan (No root required)
naabu -host example.com -s conn
```

---

## ⚙️ Rate Limiting & Performance Tuning

```bash
# Adjust packet rate per second (default: 1000)
sudo naabu -host example.com -rate 2500

# Set connection timeout in milliseconds
naabu -host example.com -timeout 1000

# Exclude CDN edge servers (Cloudflare, CloudFront, Fastly)
naabu -host example.com -exclude-cdn
```

---

## 🌐 Multi-Target Scanning & CIDR Blocks

```bash
# Scan targets from list
naabu -list targets.txt -p 80,443,8080 -o open_ports.txt

# Scan a /24 subnet block
sudo naabu -host 192.168.1.0/24 -p 22,80,443,445,3389
```

---

## 🔄 Seamless Pipeline Workflows

Naabu shines when piped with other ProjectDiscovery tools:

### Subfinder + Naabu
Enumerate subdomains and immediately discover which ones have open web/SSH ports:
```bash
subfinder -d example.com -silent | naabu -silent -p 80,443,8080,8443
```

### Naabu + Httpx + Nuclei
The canonical offensive bug bounty reconnaissance pipeline:
```bash
# 1. Port scan subdomains -> 2. Probe HTTP services -> 3. Vulnerability scan
subfinder -d target.com -silent \
  | naabu -silent -p 80,443,8080,8443 \
  | httpx -silent \
  | nuclei -severity medium,high,critical -o findings.txt
```

---

## 🔍 Nmap Integration (`-nmap`)

Pass discovered open ports directly to Nmap for service detection (`-sV`) and script scanning (`-sC`):

```bash
sudo naabu -host target.local -top-ports 1000 -nmap-cli 'nmap -sV -sC -oN naabu_nmap.txt'
```

---

## ⚡ Quick Reference Table

| Objective | Command |
|-----------|---------|
| **Default Scan (Top 100)** | `naabu -host <TARGET>` |
| **Full Range Scan (1-65535)**| `naabu -host <TARGET> -p -` |
| **Top 1000 Ports** | `naabu -host <TARGET> -top-ports 1000` |
| **SYN Scan (Fast, Stealthy)**| `sudo naabu -host <TARGET> -s syn` |
| **Exclude CDN Edge IPs** | `naabu -host <TARGET> -exclude-cdn` |
| **Output to JSON** | `naabu -host <TARGET> -json -o results.json` |
| **Pipe to HTTPX** | `naabu -host <TARGET> -silent \| httpx` |
| **Trigger Nmap Service Scan**| `sudo naabu -host <TARGET> -nmap-cli 'nmap -sV'` |

---

## 🛡️ Defensive Considerations & Detection

1. **Firewall & IDS Rules:** High-rate SYN sweeps generated by Naabu produce high TCP RST/SYN ratios easily caught by Snort/Suricata rules.
2. **Network Rate Limiting:** Enforce inbound connection rate-limiting (`iptables -m limit --limit 50/s`) to slow down and disrupt automated scanning.

---

## ⚠️ Legal Disclaimer

> [!CAUTION]
> Naabu is developed strictly for authorized security assessments, penetration testing, and defensive asset discovery. Scanning networks without explicit permission is strictly prohibited.
