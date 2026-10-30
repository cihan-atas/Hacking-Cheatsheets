# 🛰️ sshuttle - Complete Cheatsheet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)

```
  ███████╗███████╗██╗  ██╗██╗   ██╗████████╗████████╗██╗     ███████╗
  ██╔════╝██╔════╝██║  ██║██║   ██║╚══██╔══╝╚══██╔══╝██║     ██╔════╝
  ███████╗███████╗███████║██║   ██║   ██║      ██║   ██║     █████╗  
  ╚════██║╚════██║██╔══██║██║   ██║   ██║      ██║   ██║     ██╔══╝  
  ███████║███████║██║  ██║╚██████╔╝   ██║      ██║   ███████╗███████╗
  ╚══════╝╚══════╝╚═╝  ╚═╝ ╚═════╝    ╚═╝      ╚═╝   ╚══════╝╚══════╝
       Transparent Proxy Server that Works as a Poor Man's VPN over SSH
```

<p align="center">
  <img src="https://img.shields.io/badge/sshuttle-Poor_Man's_VPN-blue?style=for-the-badge" alt="sshuttle">
  <img src="https://img.shields.io/badge/Category-Pivoting_&_Tunneling-orange?style=for-the-badge" alt="Pivoting & Tunneling">
  <img src="https://img.shields.io/badge/Protocols-TCP_DNS_SSH-green?style=for-the-badge" alt="Protocols">
  <img src="https://img.shields.io/badge/License-LGPLv2.1-red?style=for-the-badge" alt="License LGPLv2.1">
</p>

<p align="center">
  <b>🌐 Forward entire internal subnets and DNS requests through an unprivileged SSH session without administrative access on the remote server</b>
</p>

---

## 📋 Table of Contents

- [Overview & The "Poor Man's VPN" Philosophy](#-overview--the-poor-mans-vpn-philosophy)
- [How sshuttle Works](#-how-sshuttle-works)
- [Tool Comparison: sshuttle vs SOCKS vs Ligolo-ng vs OpenVPN](#-tool-comparison-sshuttle-vs-socks-vs-ligolo-ng-vs-openvpn)
- [Installation](#-installation)
- [Basic Subnet Routing](#-basic-subnet-routing)
- [DNS Traffic Forwarding (`--dns`)](#-dns-traffic-forwarding---dns)
- [Full Gateway Redirection (Default Route)](#-full-gateway-redirection-default-route)
- [Subnet Exclusions (`-x`)](#-subnet-exclusions--x)
- [SSH Options & Key-Based Authentication (`-e`)](#-ssh-options--key-based-authentication--e)
- [Daemon Background Execution (`-D`)](#-daemon-background-execution--d)
- [Quick Reference Table](#-quick-reference-table)
- [Defensive Considerations & OPSEC](#-defensive-considerations--opsec)
- [Legal Disclaimer](#-legal-disclaimer)

---

## 🎯 Overview & The "Poor Man's VPN" Philosophy

**sshuttle** is a transparent proxy server that operates as a lightweight, zero-configuration VPN over a standard SSH connection.

In typical corporate penetration testing and Red Team assessments, pivoting into internal networks is hampered by strict constraints:
- ❌ You rarely possess `root` or `SYSTEM` access on the initial SSH bastion / pivot host.
- ❌ Traditional VPN solutions (OpenVPN, WireGuard) require administrative rights and virtual TUN/TAP network devices on both endpoints.
- ❌ Standard SOCKS proxies (like `ssh -D`) require configuring `proxychains` or individual application proxy settings for every single tool.

**sshuttle solves all three limitations simultaneously:**
- ✅ **No remote root required:** The pivot server only needs a standard Python installation (`python3`).
- ✅ **No remote TUN/TAP needed:** Uses pure TCP stream multiplexing over the SSH session.
- ✅ **System-wide transparent routing:** Modifies local `iptables` / `nftables` on the attacker box, enabling tools (`nmap`, `curl`, `smbclient`, web browsers) to target internal IPs directly without wrappers!

---

## ⚙️ How sshuttle Works

```
[ Attacker Box (Local Root) ]
  ├── iptables / nftables redirects internal IP traffic (10.10.10.0/24) to local port
  └── sshuttle client multiplexes TCP packets over SSH tunnel
              │
              ▼ (Encrypted SSH Channel - Port 22)
[ Remote Pivot Server (Unprivileged User + Python) ]
  └── Assembles TCP connections and forwards them directly to internal hosts
              │
              ▼
[ Target Internal Network (10.10.10.0/24) ]
```

---

## ⚖️ Tool Comparison: sshuttle vs SOCKS vs Ligolo-ng vs OpenVPN

| Feature | `sshuttle` | SOCKS (`ssh -D`) | `Ligolo-ng` | `OpenVPN` |
|---------|------------|------------------|-------------|-----------|
| **Remote Root Required** | ❌ No | ❌ No | ❌ No | ✅ Yes |
| **Local Root Required** | ✅ Yes (iptables) | ❌ No | ✅ Yes (TUN interface) | ✅ Yes |
| **Transparent Routing** | ✅ Yes | ❌ Requires Proxychains | ✅ Yes (Native TUN) | ✅ Yes |
| **UDP Traffic Support** | ❌ DNS only | ❌ No | ✅ Yes | ✅ Full UDP |
| **Requires Python on Target**| ✅ Yes (Python 3) | ❌ No | ❌ No (Standalone agent)| ❌ No |
| **Nmap SYN Scan (`-sS`)** | ❌ TCP connect (`-sT`)| ❌ TCP connect only | ✅ Raw packet support | ✅ Full raw packets |

---

## 📥 Installation

```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y sshuttle

# Arch Linux
sudo pacman -S sshuttle

# macOS (Homebrew)
brew install sshuttle

# Python Pipx / Pip
pipx install sshuttle
```

---

## 🚀 Basic Subnet Routing

Forward an entire remote Class C subnet through the SSH pivot host:

```bash
# Route 10.10.10.0/24 through user@192.168.1.50
sshuttle -r user@192.168.1.50 10.10.10.0/24

# Route multiple disjoint subnets simultaneously
sshuttle -r user@192.168.1.50 10.10.10.0/24 172.16.0.0/16 192.168.100.0/24
```

Once connected, you can communicate with internal machines directly:
```bash
# No proxychains needed!
curl http://10.10.10.25:8080/
smbclient -L //10.10.10.10/ -U 'domain/user%pass'
```

---

## 🌐 DNS Traffic Forwarding (`--dns`)

Forward internal corporate DNS queries (`.corp`, `.local`) to the remote network's internal DNS server:

```bash
# Route subnet and proxy internal DNS queries
sshuttle --dns -r user@192.168.1.50 10.10.10.0/24

# Query internal domain directly via nslookup/dig
dig dc01.internal.corp
```

---

## 🌍 Full Gateway Redirection (Default Route)

Route all outbound attacker traffic through the SSH connection (full VPN behavior):

```bash
# Route all traffic through remote SSH server (0.0.0.0/0)
sshuttle -r user@remote-vps.com 0.0.0.0/0

# Route all traffic except local network
sshuttle -r user@remote-vps.com 0.0.0.0/0 -x 192.168.1.0/24
```

---

## 🚫 Subnet Exclusions (`-x`)

Prevent specific sensitive or loopback subnets from being captured:

```bash
# Route entire 10.0.0.0/8 network, but exclude the pivot host itself
sshuttle -r user@10.0.0.50 10.0.0.0/8 -x 10.0.0.50/32
```

---

## 🔑 SSH Options & Key-Based Authentication (`-e`)

Pass custom SSH flags (private identity keys, non-standard SSH ports):

```bash
# Use custom identity file and non-standard SSH port 2222
sshuttle -e 'ssh -i /path/to/id_rsa -p 2222' -r user@192.168.1.50 10.10.10.0/24
```

---

## 🤖 Daemon Background Execution (`-D`)

Run sshuttle as a background daemon during ongoing engagements:

```bash
# Start background daemon with pidfile
sudo sshuttle -D --pidfile /tmp/sshuttle.pid -r user@192.168.1.50 10.10.10.0/24

# Terminate daemon when finished
sudo kill $(cat /tmp/sshuttle.pid)
```

---

## ⚡ Quick Reference Table

| Objective | Command |
|-----------|---------|
| **Route Single Subnet** | `sshuttle -r <USER>@<PIVOT> <SUBNET>/24` |
| **Route Subnet with DNS** | `sshuttle --dns -r <USER>@<PIVOT> <SUBNET>/24` |
| **Route Multiple Subnets** | `sshuttle -r <USER>@<PIVOT> <NET1>/24 <NET2>/16` |
| **Exclude IP/Subnet** | `sshuttle -r <USER>@<PIVOT> <SUBNET>/24 -x <EXCLUDE_IP>` |
| **Full Traffic Redirection** | `sshuttle -r <USER>@<PIVOT> 0.0.0.0/0` |
| **Custom SSH Key / Port** | `sshuttle -e 'ssh -i <KEY> -p <PORT>' -r <USER>@<PIVOT> <SUBNET>` |
| **Run in Background** | `sshuttle -D --pidfile /tmp/sshuttle.pid -r <USER>@<PIVOT> <SUBNET>` |

---

## 🛡️ Defensive Considerations & OPSEC

1. **Firewall Modification:** sshuttle modifies local firewall tables (`iptables` on Linux, `pf` on macOS). If sshuttle crashes uncleanly, use `iptables -F` or restart the firewall service to flush stale redirection rules.
2. **TCP-Only Limitation:** Non-DNS UDP packets, ICMP echo requests (ping), and SYN scans are not supported. Always use `nmap -sT -Pn -n` for port scanning over sshuttle.
3. **Target Footprint:** sshuttle automatically uploads a small Python assembly script to the remote pivot host during session initialization and runs it via `python3`.

---

## ⚠️ Legal Disclaimer

> [!CAUTION]
> This cheatsheet is intended for authorized penetration testing and system administration only. Unauthorized routing and interception of network traffic is illegal.