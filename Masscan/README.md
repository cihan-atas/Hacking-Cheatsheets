# ⚡ Masscan - Complete Cheatsheet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)

```
  ███╗   ███╗ █████╗ ███████╗███████╗ ██████╗ █████╗ ███╗   ██╗
  ████╗ ████║██╔══██╗██╔════╝██╔════╝██╔════╝██╔══██╗████╗  ██║
  ██╔████╔██║███████║███████╗███████╗██║     ███████║██╔██╗ ██║
  ██║╚██╔╝██║██╔══██║╚════██║╚════██║██║     ██╔══██║██║╚██╗██║
  ██║ ╚═╝ ██║██║  ██║███████║███████║╚██████╗██║  ██║██║ ╚████║
  ╚═╝     ╚═╝╚═╝  ╚═╝╚══════╝╚══════╝ ╚═════╝╚═╝  ╚═╝╚═╝  ╚═══╝
       The Fastest Internet Port Scanner - Asynchronous 10M Packets/Second
```

<p align="center">
  <img src="https://img.shields.io/badge/Masscan-Internet_Port_Scanner-blue?style=for-the-badge" alt="Masscan">
  <img src="https://img.shields.io/badge/Language-C-orange?style=for-the-badge" alt="Language C">
  <img src="https://img.shields.io/badge/Category-Recon_&_Scanning-green?style=for-the-badge" alt="Recon & Scanning">
  <img src="https://img.shields.io/badge/License-AGPLv3-red?style=for-the-badge" alt="License AGPLv3">
</p>

<p align="center">
  <b>🌐 High-performance asynchronous TCP port scanner capable of scanning the entire IPv4 Internet in under 5 minutes</b>
</p>

---

## 📋 Table of Contents

- [Overview & The Asynchronous Architecture](#-overview--the-asynchronous-architecture)
- [Tool Comparison: Masscan vs Nmap vs RustScan](#-tool-comparison-masscan-vs-nmap-vs-rustscan)
- [Installation](#-installation)
- [Basic Usage & Scanning Targets](#-basic-usage--scanning-targets)
- [Transmission Rate Control (`--rate`)](#-transmission-rate-control---rate)
- [Port Specifications (TCP & UDP)](#-port-specifications-tcp--udp)
- [Banner Grabbing & Basic Protocol Probing](#-banner-grabbing--basic-protocol-probing)
- [Target Exclusions & Blacklists (`--excludefile`)](#-target-exclusions--blacklists---excludefile)
- [Output Formats & Piping into Nmap](#-output-formats--piping-into-nmap)
- [Quick Reference Table](#-quick-reference-table)
- [Defensive Considerations & Hardware Limits](#-defensive-considerations--hardware-limits)
- [Legal Disclaimer](#-legal-disclaimer)

---

## 🎯 Overview & The Asynchronous Architecture

Created by **Robert Graham**, **Masscan** is hailed as the fastest port scanner in existence. It produces results similar to Nmap, but functions internally like `scanrand`, `unicornscan`, and `ZMap`, utilizing **asynchronous transmission**.

Unlike Nmap, which uses the operating system's kernel TCP stack and waits for responses before probing subsequent ports, Masscan incorporates its own custom user-space TCP/IP stack. It blasts raw SYN packets at rates exceeding **10 million packets per second** without maintaining connection state tables.

---

## ⚖️ Tool Comparison: Masscan vs Nmap vs RustScan

| Feature | Masscan | Nmap | RustScan |
|---------|---------|------|----------|
| **Maximum Packet Rate** | 10,000,000 pkts/sec | ~1,000 pkts/sec | OS socket limit |
| **Entire Internet Scan (/0)**| ✅ Under 5 minutes | ❌ Days / Weeks | ❌ Not designed for /0 |
| **TCP Stack Implementation**| Custom User-space Raw SYN| OS Kernel / Raw Sockets| Async Tokio TCP Sockets |
| **Deep Version & NSE Scripts**| ❌ Primitive Banners | ✅ 600+ NSE scripts | ✅ Via Nmap handoff |
| **State Tracking** | Stateless (Blackrock PRNG) | Stateful connection pool| Stateful async tasks |
| **Ideal Target Scope** | Class A/B blocks (/8, /16)| Single targets & /24 subnets | Single machines & /24 subnets |

---

## 📥 Installation

```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y masscan

# Arch Linux
sudo pacman -S masscan

# Build from source (Latest GitHub version)
sudo apt install -y git gcc make libpcap-dev
git clone https://github.com/robertdavidgraham/masscan
cd masscan && make -j$(nproc)
sudo make install
```

---

## 🚀 Basic Usage & Scanning Targets

```bash
# Scan a single IP for top web ports
sudo masscan 10.10.10.50 -p80,443,8080 --rate 1000

# Scan an entire Class B internal subnet (172.16.0.0/16) for SMB port 445
sudo masscan 172.16.0.0/16 -p445 --rate 5000

# Scan all 65,535 ports on a specific IP
sudo masscan 10.10.10.50 -p1-65535 --rate 2000
```

> [!IMPORTANT]
> Because Masscan generates raw packets via `libpcap`, it **must be run with root / sudo privileges**.

---

## ⚡ Transmission Rate Control (`--rate`)

The `--rate` parameter dictates how many packets per second Masscan transmits. Calibrating this is critical to avoid saturating network equipment:

```bash
# Gentle rate for home routers / WiFi (100–500 pkts/sec)
sudo masscan 10.10.10.0/24 -p1-1000 --rate 300

# Standard corporate gigabit internal network rate (5,000–10,000 pkts/sec)
sudo masscan 10.0.0.0/8 -p80,443 --rate 10000

# High-speed data center 10Gbps uplink rate (100,000+ pkts/sec)
sudo masscan 0.0.0.0/0 -p443 --rate 100000
```

---

## 🔌 Port Specifications (TCP & UDP)

Masscan supports both TCP and UDP port lists simultaneously:

```bash
# Specific TCP ports
sudo masscan 10.10.10.50 -p21,22,80,443,445,3389

# Top ports shortcut (similar to Nmap)
sudo masscan 10.10.10.50 --top-ports 100

# UDP ports (prefixed with U:)
sudo masscan 10.10.10.50 -p U:53,U:161,U:123 --rate 500
```

---

## 🔍 Banner Grabbing & Basic Protocol Probing

While Masscan is primarily a SYN scanner, it can complete three-way handshakes to grab application banners:

```bash
# Grab HTTP, FTP, and SSH banners
sudo masscan 10.10.10.0/24 -p80,21,22 --banners --rate 1000
```

---

## 🚫 Target Exclusions & Blacklists (`--excludefile`)

When conducting wide-scope penetration tests, always exclude critical infrastructure and off-limit addresses:

```bash
# Exclude government and military ranges or local gateway
sudo masscan 10.0.0.0/8 -p80 --rate 2000 --exclude 10.0.0.1,10.1.1.1

# Use an exclusion file (e.g., standard exclude.conf)
sudo masscan 192.168.0.0/16 -p445 --rate 2000 --excludefile /etc/masscan/exclude.conf
```

---

## 📊 Output Formats & Piping into Nmap

Save discovered hosts in greppable or JSON formats, then feed them into Nmap for full vulnerability assessment:

```bash
# Save to JSON format
sudo masscan 10.10.10.0/24 -p80,443 -oJ masscan_results.json

# Save to simple List format (-oL)
sudo masscan 10.10.10.0/24 -p1-65535 --rate 2000 -oL open_ports.txt

# Extract discovered ports and feed directly to Nmap
cat open_ports.txt | awk '{print $4}' | sort -u | paste -sd, - | xargs -I{} nmap -sC -sV -p{} 10.10.10.50
```

---

## ⚡ Quick Reference Table

| Objective | Command |
|-----------|---------|
| **Scan Single Host (All Ports)** | `sudo masscan <IP> -p1-65535 --rate 2000` |
| **Scan CIDR Subnet for Port** | `sudo masscan <SUBNET>/16 -p<PORT> --rate 5000` |
| **Scan with Banner Grabbing** | `sudo masscan <IP> -p80,22 --banners --rate 1000` |
| **Scan UDP Ports** | `sudo masscan <IP> -p U:53,U:161 --rate 500` |
| **Exclude IP Addresses** | `sudo masscan <NET> -p80 --exclude <IP1>,<IP2>` |
| **Output to JSON** | `sudo masscan <IP> -p80,443 -oJ output.json` |
| **Output to Simple List** | `sudo masscan <IP> -p80,443 -oL output.txt` |

---

## 🛡️ Defensive Considerations & Hardware Limits

1. **Consumer Router Crash:** Setting `--rate 100000` on a home DSL/cable router or standard Wi-Fi will overwhelm its NAT connection tracking state table and crash the router. Keep `--rate` below `1000` on residential connections.
2. **Bandwidth Consumption:** Transmitting at 10 million packets per second consumes approximately 5.4 Gbps of network bandwidth.
3. **Local Firewall Rejection (RST):** Because Masscan uses a custom user-space TCP stack, your Linux kernel doesn't know about open connections and may send automatic TCP RST packets to targets. To avoid this during banner grabs:
   ```bash
   sudo iptables -A INPUT -p tcp --dport 60000 -j DROP
   sudo masscan ... --source-port 60000
   ```

---

## ⚠️ Legal Disclaimer

> [!CAUTION]
> Masscan is an exceptionally powerful network reconnaissance utility. Scanning unauthorized networks, government IP ranges, or public Internet subnets can violate telecommunications and cybersecurity laws. Always verify written authorization before initiating scans.