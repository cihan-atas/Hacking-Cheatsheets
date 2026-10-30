# 🦈 tshark - Complete Cheatsheet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)

```
  ████████╗███████╗██╗  ██╗ █████╗ ██████╗ ██╗  ██╗
  ╚══██╔══╝██╔════╝██║  ██║██╔══██╗██╔══██╗██║ ██╔╝
     ██║   ███████╗███████║███████║██████╔╝█████╔╝ 
     ██║   ╚════██║██╔══██║██╔══██║██╔══██╗██╔═██╗ 
     ██║   ███████║██║  ██║██║  ██║██║  ██║██║  ██╗
     ╚═╝   ╚══════╝╚═╝  ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝
         Terminal Packet Analysis & Dissection Engine
```

<p align="center">
  <img src="https://img.shields.io/badge/tshark-Packet_Analyzer-blue?style=for-the-badge" alt="tshark">
  <img src="https://img.shields.io/badge/CLI-Network_Sniffer-red?style=for-the-badge" alt="CLI Network Sniffer">
  <img src="https://img.shields.io/badge/Wireshark-Engine-green?style=for-the-badge" alt="Wireshark Engine">
  <img src="https://img.shields.io/badge/PCAP-Dissection-orange?style=for-the-badge" alt="PCAP Dissection">
</p>

<p align="center">
  <b>🌐 Deep terminal-based network packet dissection, stream analysis, and field extraction powered by Wireshark</b>
</p>

---

## 📋 Table of Contents

- [Introduction](#-introduction)
- [Tool Comparison: tshark vs tcpdump vs Wireshark](#-tool-comparison)
- [Installation & Privileges](#-installation--privileges)
- [Basic Capture & Playback](#-basic-capture--playback)
- [Filtering Traffic: Capture vs Display](#-filtering-traffic)
- [Field Extraction (`-T fields`)](#-field-extraction)
- [Practical Pentest & Forensic Recipes](#-practical-pentest--forensic-recipes)
  - [HTTP Credential & URI Hunting](#http-credential--uri-hunting)
  - [DNS Query Extraction & Tunneling Detection](#dns-query-extraction--tunneling-detection)
  - [TLS / HTTPS SNI Extraction](#tls--https-sni-extraction)
  - [Kerberos AS-REQ / AS-REP Extraction](#kerberos-as-req--as-rep-extraction)
  - [SMB & NTLM Authentication Tracking](#smb--ntlm-authentication-tracking)
  - [Cleartext Protocol Auditing (FTP, Telnet, SMTP)](#cleartext-protocol-auditing)
  - [WPA / EAPOL 4-Way Handshake Hunting](#wpa--eapol-4-way-handshake-hunting)
- [Stream Following & Reassembly](#-stream-following--reassembly)
- [Statistics, Conversations & Endpoints](#-statistics-conversations--endpoints)
- [TLS / SSL Traffic Decryption](#-tls--ssl-traffic-decryption)
- [Export Formats (JSON, CSV, Elasticsearch)](#-export-formats)
- [Quick Reference Table](#-quick-reference-table)
- [Tips & Best Practices](#-tips--best-practices)
- [Legal Disclaimer](#-legal-disclaimer)

---

## 🎯 Introduction

**tshark** is the terminal and command-line counterpart of **Wireshark**. It relies on the identical packet capture library (libpcap/Npcap) and packet dissection engine as the graphical Wireshark, making it one of the most powerful network analysis tools available in Linux, macOS, and Windows.

Unlike basic packet sniffer tools that only inspect protocol headers, `tshark` features deep packet inspection capabilities across thousands of protocols, native Wireshark display filter syntax (`-Y`), robust protocol field extraction (`-T fields`), and flexible output formatting for automation pipelines (`jq`, shell scripts, Python).

### Key Features

| Feature | Description |
|---------|-------------|
| **Deep Dissection** | Inspects payload data across thousands of application protocols |
| **Wireshark Filters** | Full support for rich Wireshark display filter syntax (`-Y`) |
| **Field Extraction** | Extract individual protocol fields directly (`-T fields -e ...`) |
| **Stream Following** | Reassembles full TCP/UDP sessions in ASCII, Hex, or raw formats |
| **Automated Forensics**| Scriptable in shell, Python, or piped to `jq` via `-T json` |
| **Decryption** | Supports runtime TLS decryption with `SSLKEYLOGFILE` |

---

## ⚖️ Tool Comparison

| Feature | tshark | tcpdump | Wireshark |
|---------|--------|---------|-----------|
| **Interface** | CLI (Terminal) | CLI (Terminal) | GUI (Graphical) |
| **Resource Usage** | Low / Moderate | Extremely Minimal | Higher (RAM & CPU) |
| **Remote (SSH / Headless)** | Native headless / CLI | Native SSH pipe | Requires X11 / Remote agent |
| **Dissecting Engine** | Full Wireshark dissectors | Basic headers | Full Wireshark dissectors |
| **Capture Filters** | BPF only (`-f`) | BPF only (`-i`) | BPF only |
| **Display Filters** | ✅ Full Wireshark syntax (`-Y`) | ❌ None | ✅ Full GUI filter bar |
| **Export Formats** | PCAP, JSON, CSV, Elastic, XML | PCAP, ASCII, Hex | PCAP, CSV, XML, JSON |
| **Scripting & Automation**| Shell, Python, jq | Shell / Awk / Sed | Lua plugins, GUI |

---

## 📥 Installation & Privileges

### Linux (Debian / Ubuntu / Kali Linux)

```bash
# Update and install tshark
sudo apt update && sudo apt install -y tshark

# Verify installation and version
tshark --version
```

### Allowing Non-Root Packet Capture

To prevent running `tshark` as `root` (which can be a security risk due to the vast dissector surface), add your user to the `wireshark` group:

```bash
# Configure wireshark-common for non-superusers
sudo dpkg-reconfigure wireshark-common
# (Select "Yes" when prompted)

# Add your user to the wireshark group
sudo usermod -aG wireshark $USER

# Apply group changes to current session
newgrp wireshark
```

### macOS (Homebrew) & Windows (Chocolatey)

```bash
# macOS
brew install wireshark

# Windows
choco install wireshark
```

---

## 🚀 Basic Capture & Playback

### List Available Interfaces

```bash
# List interfaces with numeric IDs
tshark -D
```

### Capturing Live Packets

```bash
# Capture packets on interface eth0
tshark -i eth0

# Capture only 100 packets and stop
tshark -i eth0 -c 100

# Capture and save traffic directly to a pcap file
tshark -i eth0 -w capture.pcap

# Capture with ring buffer (rotate files every 100MB, keep max 5 files)
tshark -i eth0 -b filesize:100000 -b files:5 -w rolling_traffic.pcap

# Capture with a duration trigger (capture for 60 seconds)
tshark -i eth0 -a duration:60 -w one_minute.pcap
```

### Reading PCAP Files

```bash
# Read and display packets from a pcap file
tshark -r capture.pcap

# Read PCAP without resolving IP addresses to DNS hostnames (speeds up processing)
tshark -r capture.pcap -n

# Suppress packet count summary upon completion
tshark -r capture.pcap -q
```

---

## 🔍 Filtering Traffic: Capture vs Display

`tshark` employs two distinct filtering engines:

1. **Capture Filters (`-f`):** Berkeley Packet Filters (BPF) evaluated by the kernel before packets hit userspace. Highly performant, but limited to basic IP/port primitives.
2. **Display Filters (`-Y`):** Deep protocol filters evaluated by Wireshark's dissection engine. Supports application layers, string searching, regex, and protocol flags.

```bash
# Capture filter: Capture only TCP traffic to port 80 or 443
tshark -i eth0 -f "tcp port 80 or tcp port 443" -w web_traffic.pcap

# Display filter: Show only successful HTTP GET responses (Status 200)
tshark -r capture.pcap -Y "http.request.method == \"GET\" and http.response.code == 200"

# Display filter: Filter by specific subnet and exclude broadcast
tshark -r capture.pcap -Y "ip.addr == 192.168.1.0/24 and not eth.dst == ff:ff:ff:ff:ff:ff"

# Read-filter (-R): Single-pass filter used during capture
tshark -r capture.pcap -2 -R "dns.flags.response == 1"
```

---

## 📊 Field Extraction (`-T fields`)

The `-T fields` mode is `tshark`'s most powerful feature for automated reconnaissance, scripting, and CTF challenges.

```bash
# Basic syntax for field extraction
tshark -r capture.pcap -T fields -e <field1> -e <field2> -E header=y -E separator=,
```

### Key Extraction Formatting Flags

| Flag | Description |
|------|-------------|
| `-T fields` | Switches output mode to custom field extraction |
| `-e <field>` | Names the protocol field to extract (can be repeated) |
| `-E header=y` | Prints column names as header row |
| `-E separator=,` | Sets field separator (`,` for CSV, `\t` for TSV) |
| `-E occurrence=f` | Extracts only the first (`f`), last (`l`), or all (`a`) occurrences |
| `-E quote=d` | Encloses values in double quotes |

### Core Protocol Fields Cheatsheet

```bash
# IP layer fields
ip.src, ip.dst, ip.proto, ip.ttl

# TCP layer fields
tcp.srcport, tcp.dstport, tcp.flags, tcp.stream, tcp.payload

# HTTP fields
http.host, http.request.method, http.request.uri, http.response.code, http.user_agent, http.cookie

# DNS fields
dns.qry.name, dns.qry.type, dns.a, dns.flags.response

# TLS fields
tls.handshake.extensions_server_name, tls.handshake.ciphersuite
```

---

## 🎯 Practical Pentest & Forensic Recipes

### HTTP Credential & URI Hunting

```bash
# Extract all visited URLs and HTTP Hosts
tshark -r capture.pcap -Y "http.request" -T fields -e ip.src -e http.host -e http.request.method -e http.request.uri -E header=y

# Extract User-Agent strings across internal clients
tshark -r capture.pcap -Y "http.user_agent" -T fields -e ip.src -e http.user_agent | sort -u

# Hunt for HTTP POST data (logins, passwords, tokens)
tshark -r capture.pcap -Y "http.request.method == \"POST\"" -T fields -e ip.src -e ip.dst -e http.host -e http.file_data

# Hunt for HTTP Basic Authentication headers (Base64 encoded)
tshark -r capture.pcap -Y "http.authorization" -T fields -e ip.src -e http.authorization
```

### DNS Query Extraction & Tunneling Detection

```bash
# List all requested domain names (DNS queries)
tshark -r capture.pcap -Y "dns.flags.response == 0" -T fields -e ip.src -e dns.qry.name | sort -u

# Detect potential DNS tunneling (display long subdomains > 30 characters)
tshark -r capture.pcap -Y "dns.flags.response == 0" -T fields -e dns.qry.name | awk 'length($1) > 30' | sort -u

# Correlate DNS queries with their resolved IP addresses
tshark -r capture.pcap -Y "dns.flags.response == 1 and dns.a" -T fields -e dns.qry.name -e dns.a -E header=y
```

### TLS / HTTPS SNI Extraction

Even when HTTPS payloads are encrypted, the **Server Name Indication (SNI)** in the initial TLS Client Hello handshake reveals destination domain names:

```bash
# Extract TLS Server Name Indication (SNI) hostnames
tshark -r capture.pcap -Y "tls.handshake.type == 1" -T fields -e ip.src -e ip.dst -e tls.handshake.extensions_server_name | sort -u
```

### Kerberos AS-REQ / AS-REP Extraction

```bash
# Extract Kerberos domain client names (hunting for user accounts)
tshark -r capture.pcap -Y "kerberos.CNameString" -T fields -e ip.src -e kerberos.realm -e kerberos.CNameString | sort -u

# Hunt for Kerberos errors (e.g. KRB5KDC_ERR_PREAUTH_REQUIRED, bad passwords)
tshark -r capture.pcap -Y "kerberos.error_code" -T fields -e ip.src -e ip.dst -e kerberos.error_code
```

### SMB & NTLM Authentication Tracking

```bash
# Extract SMB accessed file paths and shares
tshark -r capture.pcap -Y "smb2.filename" -T fields -e ip.src -e ip.dst -e smb2.filename | sort -u

# Extract NTLMSSP usernames and domain names during authentication
tshark -r capture.pcap -Y "ntlmssp.auth.username" -T fields -e ip.src -e ntlmssp.auth.domain -e ntlmssp.auth.username
```

### Cleartext Protocol Auditing

```bash
# Extract FTP usernames and passwords
tshark -r capture.pcap -Y "ftp.request.command == \"USER\" or ftp.request.command == \"PASS\"" -T fields -e ip.src -e ftp.request.command -e ftp.request.arg

# Extract Telnet session input
tshark -r capture.pcap -Y "telnet" -T fields -e telnet.data
```

### WPA / EAPOL 4-Way Handshake Hunting

```bash
# Filter for 802.11 EAPOL handshakes to crack with Hashcat/Aircrack-ng
tshark -r wifi.pcap -Y "eapol" -w handshakes_only.pcap
```

---

## 🧵 Stream Following & Reassembly

`tshark` allows reassembling entire TCP and UDP streams without needing Wireshark's GUI.

```bash
# List all TCP conversations to find stream IDs
tshark -r capture.pcap -q -z conv,tcp

# Follow TCP stream 0 in ASCII format
tshark -r capture.pcap -q -z follow,tcp,ascii,0

# Follow TCP stream 2 in Hexdump format
tshark -r capture.pcap -q -z follow,tcp,hex,2

# Follow HTTP stream (stream index 1)
tshark -r capture.pcap -q -z follow,http,ascii,1
```

---

## 📈 Statistics, Conversations & Endpoints

```bash
# Protocol hierarchy statistics (breakdown of all protocols in PCAP)
tshark -r capture.pcap -q -z io,phs

# IP conversations table (who talked to whom, packet count, bytes)
tshark -r capture.pcap -q -z conv,ip

# Port conversation statistics (TCP ports)
tshark -r capture.pcap -q -z conv,tcp

# List all unique IP endpoints in PCAP
tshark -r capture.pcap -q -z endpoints,ip

# HTTP request statistics tree
tshark -r capture.pcap -q -z http,tree

# Wireshark Expert Info warnings and errors
tshark -r capture.pcap -q -z expert
```

---

## 🔐 TLS / SSL Traffic Decryption

If you have pre-master secret keys (exported via browser `SSLKEYLOGFILE` or retrieved during malware sandboxing):

```bash
# Decrypt TLS traffic on the fly using keylog file
tshark -r https_traffic.pcap -o "tls.keylog_file:/path/to/sslkeys.log" -Y "http" -T fields -e http.host -e http.request.uri

# Follow decrypted HTTP stream inside TLS
tshark -r https_traffic.pcap -o "tls.keylog_file:/path/to/sslkeys.log" -q -z follow,http,ascii,0
```

---

## 📦 Export Formats

```bash
# Output as structured JSON (ideal for piping into jq or Python)
tshark -r capture.pcap -c 5 -T json

# Extract specific JSON field with jq
tshark -r capture.pcap -T json | jq '.[]._source.layers.ip["ip.src"]'

# Output in Elasticsearch Bulk Format (-T ek)
tshark -r capture.pcap -T ek > packets_for_elastic.json

# Output as CSV table
tshark -r capture.pcap -T fields -e frame.number -e ip.src -e ip.dst -e _ws.col.Protocol -E header=y -E separator=, > traffic.csv
```

---

## ⚡ Quick Reference Table

| Task | Command |
|------|---------|
| **Capture live traffic** | `tshark -i eth0 -w output.pcap` |
| **Capture with BPF filter** | `tshark -i eth0 -f "port 80" -w http.pcap` |
| **Read PCAP without DNS** | `tshark -r capture.pcap -n` |
| **Apply display filter** | `tshark -r capture.pcap -Y "http.response.code >= 400"` |
| **Extract IPs and Ports** | `tshark -r capture.pcap -T fields -e ip.src -e tcp.srcport -e ip.dst -e tcp.dstport` |
| **Extract DNS Queries** | `tshark -r capture.pcap -Y "dns.flags.response == 0" -T fields -e dns.qry.name` |
| **Extract TLS SNI Hostnames**| `tshark -r capture.pcap -Y "tls.handshake.type == 1" -T fields -e tls.handshake.extensions_server_name` |
| **Extract HTTP URLs** | `tshark -r capture.pcap -Y "http.request" -T fields -e http.host -e http.request.uri` |
| **Follow TCP Stream** | `tshark -r capture.pcap -q -z follow,tcp,ascii,0` |
| **Protocol Hierarchy** | `tshark -r capture.pcap -q -z io,phs` |
| **Decrypt with Keylog** | `tshark -r traffic.pcap -o "tls.keylog_file:keys.log"` |
| **Export to JSON** | `tshark -r capture.pcap -T json > packets.json` |

---

## 💡 Tips & Best Practices

1. **Always Use `-n` for Large PCAPs:** DNS name resolution dramatically slows down parsing. Add `-n` to prevent resolving IP addresses.
2. **Combine `-f` and `-Y`:** Use `-f` capture filters during live capture to reduce packet drops, and `-Y` display filters during post-analysis for deep protocol inspection.
3. **Pipe with `jq`:** Pipelining `tshark -T json` into `jq` allows querying nested protocol fields with complete boolean logic and transformation.
4. **Ring Buffers for 24/7 Sniffing:** When running continuous network captures on a pivot host, always enforce size and file limits (`-b filesize:50000 -b files:10`) to prevent crashing the target's disk.

---

## ⚠️ Legal Disclaimer

> **IMPORTANT:** Unauthorized packet capture, eavesdropping on network traffic, and intercepting credentials on networks without explicit, documented permission is illegal and subject to criminal prosecution. Use `tshark` strictly on systems and networks you own or are contractually authorized to assess.
