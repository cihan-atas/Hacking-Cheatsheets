# 🦀 RustScan - Complete Cheatsheet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)

```
  ██████╗ ██╗   ██╗███████╗████████╗███████╗ ██████╗ █████╗ ███╗   ██╗
  ██╔══██╗██║   ██║██╔════╝╚══██╔══╝██╔════╝██╔════╝██╔══██╗████╗  ██║
  ██████╔╝██║   ██║███████╗   ██║   ███████╗██║     ███████║██╔██╗ ██║
  ██╔══██╗██║   ██║╚════██║   ██║   ╚════██║██║     ██╔══██║██║╚██╗██║
  ██║  ██║╚██████╔╝███████║   ██║   ███████║╚██████╗██║  ██║██║ ╚████║
  ╚═╝  ╚═╝ ╚═════╝ ╚══════╝   ╚═╝   ╚══════╝ ╚═════╝╚═╝  ╚═╝╚═╝  ╚═══╝
       The Modern Port Scanner - Scan All 65k Ports in 3 Seconds into Nmap
```

<p align="center">
  <img src="https://img.shields.io/badge/RustScan-Port_Scanner-blue?style=for-the-badge" alt="RustScan">
  <img src="https://img.shields.io/badge/Language-Rust-orange?style=for-the-badge" alt="Language Rust">
  <img src="https://img.shields.io/badge/Category-Recon_&_Scanning-green?style=for-the-badge" alt="Recon & Scanning">
  <img src="https://img.shields.io/badge/License-GPLv3-red?style=for-the-badge" alt="License GPLv3">
</p>

<p align="center">
  <b>🌐 Fast, customizable, and automated port scanner designed to pipe discovered open ports directly into Nmap scripts</b>
</p>

---

## 📋 Table of Contents

- [Overview & Architecture](#-overview--architecture)
- [Tool Comparison: RustScan vs Nmap vs Masscan](#-tool-comparison-rustscan-vs-nmap-vs-masscan)
- [Installation](#-installation)
- [Basic Usage & Single Host Scanning](#-basic-usage--single-host-scanning)
- [Automated Nmap Integration](#-automated-nmap-integration)
- [Performance Tuning (`ulimit` & Batch Size)](#-performance-tuning-ulimit--batch-size)
- [Scanning Port Ranges & CIDR Subnets](#-scanning-port-ranges--cidr-subnets)
- [Scripting & Output Formatting](#-scripting--output-formatting)
- [Quick Reference Table](#-quick-reference-table)
- [Defensive Considerations & OPSEC](#-defensive-considerations--opsec)
- [Legal Disclaimer](#-legal-disclaimer)

---

## 🎯 Overview & Architecture

**RustScan** is a modern, high-speed port scanner engineered in **Rust**. Its core philosophy is simple: **scan ports blazingly fast, find open ports across the entire 1-65535 spectrum in 3 seconds, and immediately hand off those open ports to Nmap for comprehensive service and vulnerability scanning**.

Instead of waiting 10–20 minutes for `nmap -p-` to complete its sequential TCP handshake sweeps, RustScan leverages asynchronous multi-threading and OS socket batching to pinpoint open ports almost instantaneously.

---

## ⚖️ Tool Comparison: RustScan vs Nmap vs Masscan

| Feature | RustScan | Nmap | Masscan |
|---------|----------|------|---------|
| **Primary Goal** | Fast discovery -> Nmap pipe | Deep service & NSE scripts | High-rate internet-wide SYN scan |
| **All-Ports Speed (1-65535)**| ⭐⭐⭐⭐⭐ (~3 seconds) | ⭐⭐ (5–20 minutes) | ⭐⭐⭐⭐⭐ (~2 seconds) |
| **Service Version Detection**| Via Nmap pipe | Native (`-sV`) | ❌ Minimal |
| **NSE Script Engine** | Via Nmap pipe | Native (`-sC`, `--script`) | ❌ No |
| **Packet Engine** | Async TCP Sockets | Raw IP / Custom TCP stack | Custom Asynchronous SYN stack |
| **Ease of Automation** | Native CLI arguments (`--`) | Extensive CLI options | High complexity |

---

## 📥 Installation

```bash
# Debian / Ubuntu / Kali Linux (.deb package)
wget https://github.com/RustScan/RustScan/releases/download/2.3.0/rustscan_2.3.0_amd64.deb
sudo dpkg -i rustscan_2.3.0_amd64.deb

# Arch Linux (AUR)
yay -S rustscan

# Cargo (Rust Package Manager)
cargo install rustscan

# Docker Container
docker run -it --rm --name rustscan rustscan/rustscan:2.1.1 -a 10.10.10.50
```

---

## 🚀 Basic Usage & Single Host Scanning

```bash
# Scan all 65,535 ports on a single IP
rustscan -a 10.10.10.50

# Scan with greppable one-line output
rustscan -a 10.10.10.50 --greppable

# Quiet mode (suppresses the ASCII banner)
rustscan -q -a 10.10.10.50
```

---

## 🔗 Automated Nmap Integration

RustScan's most powerful feature is passing open ports straight to Nmap using the trailing `--` separator:

```bash
# Scan target and execute default safe scripts & version detection on open ports
rustscan -a 10.10.10.50 -- -sC -sV

# Pass custom Nmap NSE scripts and output options
rustscan -a 10.10.10.50 -- -sC -sV -oN nmap_report.txt

# Run vulnerability audit with Nmap Vuln scripts
rustscan -a 10.10.10.50 -- --script vuln -oA target_vulns
```

---

## ⚡ Performance Tuning (`ulimit` & Batch Size)

RustScan relies on your operating system's file descriptor limits. By default, it auto-adjusts, but manual tuning provides peak performance:

```bash
# Increase batch size and socket ulimit for high-speed local networks
rustscan -a 10.10.10.50 -b 5000 -u 5000

# Lower batch size for high-latency or packet-dropping WiFi networks
rustscan -a 10.10.10.50 -b 500 --scan-order "random"
```

---

## 🌐 Scanning Port Ranges & CIDR Subnets

```bash
# Scan a specific port range instead of all ports
rustscan -a 10.10.10.50 -r 1-1000

# Scan multiple IP addresses
rustscan -a 10.10.10.50,10.10.10.51

# Scan an entire CIDR subnet range
rustscan -a 192.168.1.0/24
```

---

## 📊 Scripting & Output Formatting

```bash
# Output discovered open ports in JSON format
rustscan -a 10.10.10.50 --accessible

# Read target hosts from a file
rustscan -a 'hosts.txt'
```

---

## ⚡ Quick Reference Table

| Objective | Command |
|-----------|---------|
| **Scan All 65k Ports** | `rustscan -a <IP>` |
| **Scan All Ports + Nmap Scripts**| `rustscan -a <IP> -- -sC -sV` |
| **Scan CIDR Subnet** | `rustscan -a <SUBNET>/24` |
| **Scan Port Range** | `rustscan -a <IP> -r 1-1000` |
| **Tune Batch Size & ulimit** | `rustscan -a <IP> -b 3000 -u 3000` |
| **Quiet Mode (No Banner)** | `rustscan -q -a <IP>` |
| **Greppable Output** | `rustscan -a <IP> --greppable` |
| **Export to Nmap File** | `rustscan -a <IP> -- -sC -sV -oN nmap.txt` |

---

## 🛡️ Defensive Considerations & OPSEC

1. **Massive Noise & IDS Alerts:** Scanning 65k ports in 3 seconds generates thousands of TCP SYN packets instantaneously. It will trigger Snort, Suricata, and firewall rate-limiting immediately.
2. **SYN Flooding & Dropped Packets:** On unstable VPN links (e.g., HackTheBox, TryHackMe), high batch sizes (`-b 4000`) can cause packet loss. If results seem inconsistent, lower the batch size to `-b 1000`.

---

## ⚠️ Legal Disclaimer

> [!CAUTION]
> This cheatsheet is for authorized penetration testing and educational research only. Scanning networks without authorization is strictly prohibited.