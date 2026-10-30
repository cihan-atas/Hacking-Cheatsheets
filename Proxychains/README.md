# ⛓️ Proxychains - Complete Cheatsheet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)

```
  ██████╗ ██████╗  ██████╗ ██╗  ██╗██╗   ██╗ ██████╗██╗  ██╗ █████╗ ██╗███╗   ██╗███████╗
  ██╔══██╗██╔══██╗██╔═══██╗╚██╗██╔╝╚██╗ ██╔╝██╔════╝██║  ██║██╔══██╗██║████╗  ██║██╔════╝
  ██████╔╝██████╔╝██║   ██║ ╚███╔╝  ╚████╔╝ ██║     ███████║███████║██║██╔██╗ ██║███████╗
  ██╔═══╝ ██╔══██╗██║   ██║ ██╔██╗   ╚██╔╝  ██║     ██╔══██║██╔══██║██║██║╚██╗██║╚════██║
  ██║     ██║  ██║╚██████╔╝██╔╝ ██╗   ██║   ╚██████╗██║  ██║██║  ██║██║██║ ╚████║███████║
  ╚═╝     ╚═╝  ╚═╝ ╚═════╝ ╚═╝  ╚═╝   ╚═╝    ╚═════╝╚═╝  ╚═╝╚═╝  ╚═╝╚═╝╚═╝  ╚═══╝╚══════╝
       Force Any TCP Connection Through SOCKS4, SOCKS5, and HTTP Proxies
```

<p align="center">
  <img src="https://img.shields.io/badge/Proxychains-Proxychains--NG-blue?style=for-the-badge" alt="Proxychains">
  <img src="https://img.shields.io/badge/Category-Pivoting_&_Tunneling-orange?style=for-the-badge" alt="Pivoting & Tunneling">
  <img src="https://img.shields.io/badge/Protocols-SOCKS4_SOCKS5_HTTP-green?style=for-the-badge" alt="Protocols">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="License GPLv2">
</p>

<p align="center">
  <b>🌐 Route standard Linux CLI networking tools dynamically through multi-hop SOCKS and HTTP proxies via LD_PRELOAD</b>
</p>

---

## 📋 Table of Contents

- [Overview & How Proxychains Works](#-overview--how-proxychains-works)
- [Installation](#-installation)
- [Configuration Files & Lookup Hierarchy](#-configuration-files--lookup-hierarchy)
- [Chaining Modes Explained](#-chaining-modes-explained)
  - [`dynamic_chain` (Recommended)](#dynamic_chain-recommended)
  - [`strict_chain`](#strict_chain)
  - [`round_robin_chain`](#round_robin_chain)
  - [`random_chain`](#random_chain)
- [DNS Leaks Prevention (`proxy_dns`)](#-dns-leaks-prevention-proxy_dns)
- [Configuring Proxy Lists](#-configuring-proxy-lists)
- [Pivoting Workflows](#-pivoting-workflows)
  - [SSH Dynamic SOCKS Proxy (`ssh -D`)](#ssh-dynamic-socks-proxy-ssh--d)
  - [Chisel & Ligolo-ng SOCKS Relay](#chisel--ligolo-ng-socks-relay)
- [Tool Integration & Gotchas](#-tool-integration--gotchas)
  - [Nmap with Proxychains (Why `-sS` Fails)](#nmap-with-proxychains-why--ss-fails)
  - [Active Directory Tools (NetExec, Evil-WinRM, Impacket)](#active-directory-tools-netexec-evil-winrm-impacket)
- [Quick Reference Table](#-quick-reference-table)
- [Defensive Considerations & OPSEC](#-defensive-considerations--opsec)
- [Legal Disclaimer](#-legal-disclaimer)

---

## 🎯 Overview & How Proxychains Works

**Proxychains** (modern active fork: **Proxychains-NG** / `proxychains4`) allows running any dynamic command through single or multiple chained SOCKS4, SOCKS5, or HTTP(S) proxies.

It functions by preloading a custom hook library (`libproxychains4.so`) using the dynamic linker mechanism (`LD_PRELOAD`). This intercepts socket API calls (`connect()`, `gethostbyname()`) made by target applications and redirects their TCP traffic into the configured proxy chain transparently.

---

## 📥 Installation

```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y proxychains4

# Arch Linux
sudo pacman -S proxychains-ng

# macOS (Homebrew)
brew install proxychains-ng
```

---

## ⚙️ Configuration Files & Lookup Hierarchy

Proxychains searches for its configuration file in the following strict order of priority:

1. Path set in `PROXYCHAINS_CONF_FILE` environment variable.
2. File passed via the `-f` flag (`proxychains4 -f ./myproxy.conf <command>`).
3. `./proxychains.conf` in current working directory.
4. `$(HOME)/.proxychains/proxychains.conf`.
5. `/etc/proxychains4.conf` (System default).

---

## 🔗 Chaining Modes Explained

Edit `/etc/proxychains4.conf` to choose the chain routing algorithm:

### `dynamic_chain` (Recommended)
Skips dead proxies in the list automatically without aborting the connection. If Proxy #1 is up, Proxy #2 is down, and Proxy #3 is up, traffic routes through 1 -> 3.
```ini
dynamic_chain
# strict_chain
```

### `strict_chain`
Strictly follows the configured proxy order. If any proxy in the chain fails or times out, the entire connection is refused.
```ini
strict_chain
```

### `round_robin_chain`
Cycles through the proxy list sequentially for every new connection request.
```ini
round_robin_chain
chain_len = 2
```

### `random_chain`
Randomly selects proxies from the list up to `chain_len`.

---

## 🛡️ DNS Leaks Prevention (`proxy_dns`)

One of the most dangerous OPSEC failures during external reconnaissance or internal pivoting is leaking DNS queries to the local ISP or target gateway.

Ensure `proxy_dns` is active in `/etc/proxychains4.conf`:
```ini
# Proxy all DNS requests through SOCKS5 proxy
proxy_dns

# Suppress annoying hook library stdout logs
quiet_mode
```

---

## 📝 Configuring Proxy Lists

At the bottom of `/etc/proxychains4.conf`, under `[ProxyList]`:

```ini
[ProxyList]
# Protocol   IP               Port   [Username]  [Password]
socks5       127.0.0.1        1080
socks4       192.168.1.100    1080
http         10.10.10.50      8080   admin       SecretPass123
```

---

## 🚀 Pivoting Workflows

### SSH Dynamic SOCKS Proxy (`ssh -D`)

```bash
# 1. Open background dynamic SOCKS5 proxy on local port 1080 through compromised pivot host
ssh -f -N -D 127.0.0.1:1080 user@10.10.10.50

# 2. Add entry to /etc/proxychains4.conf:
# socks5 127.0.0.1 1080

# 3. Access internal machines unreachable directly from attacker box
proxychains4 curl http://172.16.20.10
```

### Chisel & Ligolo-ng SOCKS Relay

When running Chisel client-server tunnels:
```bash
# Connect Chisel client with reverse SOCKS
chisel client 10.10.14.5:8000 R:1080:socks

# Route tools through Chisel tunnel
proxychains4 crackmapexec smb 172.16.50.0/24
```

---

## ⚠️ Tool Integration & Gotchas

### Nmap with Proxychains (Why `-sS` Fails)

> [!WARNING]
> SOCKS proxies only support established TCP sessions. **SYN Stealth Scans (`-sS`) will fail or hang** because they require raw socket manipulation not supported over SOCKS!

```bash
# Correct Nmap command with Proxychains (Full TCP Connect, No Ping, No DNS)
proxychains4 nmap -sT -Pn -n -sV -p 80,443,445,3389 172.16.20.10

# Scan Top 100 ports over proxy
proxychains4 nmap -sT -Pn -n --top-ports 100 172.16.20.10
```

### Active Directory Tools (NetExec, Evil-WinRM, Impacket)

```bash
# SMB enumeration through internal pivot
proxychains4 nxc smb 172.16.20.10 -u Administrator -p 'Password123'

# WinRM shell through proxychains
proxychains4 evil-winrm -i 172.16.20.10 -u Administrator -p 'Password123'

# Impacket secretsdump over SOCKS proxy
proxychains4 impacket-secretsdump domain.local/Administrator:Pass@172.16.20.10 -no-pass
```

---

## ⚡ Quick Reference Table

| Objective | Command |
|-----------|---------|
| **Run Command via Proxy** | `proxychains4 <command> [args]` |
| **Custom Config File** | `proxychains4 -f ./custom.conf <command>` |
| **Quiet Execution (No banner)** | `proxychains4 -q <command>` |
| **TCP Scan Internal Target** | `proxychains4 nmap -sT -Pn -n -p 80,445 <IP>` |
| **Internal Web Request** | `proxychains4 curl -i http://<INTERNAL_IP>` |
| **Internal SQLMap Test** | `proxychains4 sqlmap -u "http://<IP>/id=1" --batch` |
| **Internal NetExec Probe** | `proxychains4 nxc smb <SUBNET>/24` |

---

## 🛡️ Defensive Considerations & OPSEC

1. **DNS Leaks:** If `proxy_dns` is commented out, your system will resolve hostnames via local DNS, alerting Blue Teams to targeted hostnames.
2. **Speed & Latency:** Chaining multiple proxies compounds round-trip time (RTT). Set reasonable timeouts (`tcp_read_time_out 15000`, `tcp_connect_time_out 8000`).
3. **Static Binaries:** Statically compiled binaries (e.g., Go/Rust binaries that do not link `libc.so`) bypass `LD_PRELOAD` hooks and will not be routed through Proxychains.

---

## ⚠️ Legal Disclaimer

> [!CAUTION]
> This cheatsheet is intended for authorized penetration testing, Red Team engagements, and educational research only. Routing traffic without authorization is strictly prohibited.