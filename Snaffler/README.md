# 🐶 Snaffler - Complete Cheatsheet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)

```
  ███████╗███╗   ██╗ █████╗ ███████╗███████╗██╗     ███████╗██████╗ 
  ██╔════╝████╗  ██║██╔══██╗██╔════╝██╔════╝██║     ██╔════╝██╔══██╗
  ███████╗██╔██╗ ██║███████║█████╗  █████╗  ██║     █████╗  ██████╔╝
  ╚════██║██║╚██╗██║██╔══██║██╔══╝  ██╔══╝  ██║     ██╔══╝  ██╔══██╗
  ███████║██║ ╚████║██║  ██║██║     ██║     ███████╗███████╗██║  ██║
  ╚══════╝╚═╝  ╚═══╝╚═╝  ╚═╝╚═╝     ╚═╝     ╚══════╝╚══════╝╚═╝  ╚═╝
       Active Directory Domain SMB Share Credential & Secret Hunter
```

<p align="center">
  <img src="https://img.shields.io/badge/Snaffler-Secret_Hunter-blue?style=for-the-badge" alt="Snaffler">
  <img src="https://img.shields.io/badge/Language-C%23_/_NET-orange?style=for-the-badge" alt="C# / .NET">
  <img src="https://img.shields.io/badge/Category-Active_Directory_&_Network-green?style=for-the-badge" alt="Active Directory">
  <img src="https://img.shields.io/badge/License-GPLv3-red?style=for-the-badge" alt="License GPLv3">
</p>

<p align="center">
  <b>🌐 Automated Windows domain tool designed to crawl SMB shares, locate juicy configuration files, KeePass databases, and plaintext credentials</b>
</p>

---

## 📋 Table of Contents

- [Overview & The Share Hunting Problem](#-overview--the-share-hunting-problem)
- [How Snaffler Works](#-how-snaffler-works)
- [Installation & Execution Options](#-installation--execution-options)
- [Snaffler Triage & Severity Levels](#-snaffler-triage--severity-levels)
- [Common Scanning Workflows](#-common-scanning-workflows)
  - [Domain-Wide Share Crawl](#domain-wide-share-crawl)
  - [Targeting Specific Hosts or Shares](#targeting-specific-hosts-or-shares)
  - [Excluding Computers & DFS Roots](#excluding-computers--dfs-roots)
- [Juicy Targets & File Match Rules](#-juicy-targets--file-match-rules)
- [Output Logging & Analysis](#-output-logging--analysis)
- [Quick Reference Table](#-quick-reference-table)
- [Defensive Considerations & OPSEC](#-defensive-considerations--opsec)
- [Legal Disclaimer](#-legal-disclaimer)

---

## 🎯 Overview & The Share Hunting Problem

In medium to large Active Directory environments, enterprise file servers and workstations host thousands of open SMB network shares. System administrators, developers, and employees frequently leave behind:

- 🔑 **KeePass Databases (`.kdbx`):** Often holding Master passwords or easily crackable master keys.
- ⚙️ **Configuration Files (`web.config`, `appsettings.json`):** Containing hardcoded SQL connection strings, service account passwords, and API keys.
- 📜 **Deployment Scripts (`unattend.xml`, `.ps1`, `.bat`):** Hardcoded administrative credentials.
- 🔏 **Private Certificates (`.pfx`, `.p12`, `.pem`, `.ppk`):** Usable for privilege escalation or lateral movement.

Manually browsing thousands of shares is impossible. **Snaffler**, developed by **l33tb455**, automates this entire process: it queries Active Directory for computer objects, enumerates readable SMB shares, walks the directory tree, and evaluates files against curated regex rule sets.

---

## ⚙️ How Snaffler Works

```
[ Compromised Domain Machine ]
         │
         ├── 1. Queries Domain Controller via LDAP for Computer Objects
         ├── 2. Connects to port 445 (SMB) across all discovered hosts
         ├── 3. Enumerates Read/Write permissions on C$, SYSVOL, NETLOGON, Custom Shares
         └── 4. Evaluates file extensions, file names, and file contents against rules
```

---

## 📥 Installation & Execution Options

Snaffler is compiled as a standalone .NET binary:

```bash
# Clone and build using Visual Studio or dotnet CLI on Windows
git clone https://github.com/SnaffCon/Snaffler.git
cd Snaffler
dotnet build -c Release
```

### Running on Windows:
```cmd
# Standard execution outputting to screen and log file
Snaffler.exe -s -o snaffler_log.txt
```

### Running via Cobalt Strike / C2 (Execute-Assembly):
```text
beacon> execute-assembly C:\Tools\Snaffler.exe -s -o C:\Temp\loot.txt
```

---

## 🚦 Snaffler Triage & Severity Levels

Snaffler categorizes findings into color-coded urgency buckets:

- 🔴 **`{Red}` / Black:** Definite high-value finding! Plaintext passwords, private keys (`.pfx`, `.id_rsa`), AWS tokens, unattended install files.
- 🟡 **`{Yellow}`:** Probable finding. Configuration files with password strings, database connection strings, shadow copies.
- 🟢 **`{Green}`:** Informational / Interest. File names matching administrative patterns (`passwords.xlsx`, `vpn.ovpn`).

---

## 🚀 Common Scanning Workflows

### Domain-Wide Share Crawl

Scan all accessible shares across the entire Active Directory domain:

```cmd
# Run with live console streaming (-s) and save to disk
Snaffler.exe -s -o C:\loot\snaffler_domain.txt

# Run with multi-threading tuning (Default: 60 threads)
Snaffler.exe -s -t 100 -o C:\loot\snaffler_fast.txt
```

### Targeting Specific Hosts or Shares

When you want to avoid scanning the entire domain and focus on a file server or Domain Controller:

```cmd
# Scan a single targeted file server
Snaffler.exe -c fs01.corp.local -s

# Scan a comma-separated list of machines
Snaffler.exe -c dc01.corp.local,fs02.corp.local,sql01.corp.local -s

# Scan a specific shared folder path directly
Snaffler.exe -i \\fs01.corp.local\Departments\IT\ -s
```

### Excluding Computers & DFS Roots

Avoid scanning slow or sensitive computers:

```cmd
# Exclude specific backup or surveillance servers
Snaffler.exe -s -c- backup01.corp.local,cctv01.corp.local
```

---

## 🎯 Juicy Targets & File Match Rules

By default, Snaffler hunts for the following critical extensions:

| Extension / File | High-Value Content |
|------------------|--------------------|
| `.kdbx` / `.kdb` | KeePass password manager safes |
| `web.config` / `app.config` | Cleartext database passwords |
| `unattend.xml` / `sysprep.inf` | Windows administrator passwords |
| `.ppk` / `.pem` / `id_rsa` | SSH private keys |
| `.pfx` / `.p12` | Active Directory certificates |
| `.rdp` / `.vnc` | Remote desktop connection profiles |
| `passwords.xlsx` / `creds.txt` | Employee password lists |

---

## 📊 Output Logging & Analysis

```bash
# Grep for immediate RED credentials from a Linux terminal
grep -i "{Red}" snaffler_domain.txt

# Find all discovered KeePass safes
grep -i "\.kdbx" snaffler_domain.txt

# Filter out database connection strings
grep -i "password=" snaffler_domain.txt
```

---

## ⚡ Quick Reference Table

| Objective | Command |
|-----------|---------|
| **Domain-Wide Scan** | `Snaffler.exe -s -o out.log` |
| **Target Single Computer** | `Snaffler.exe -c <HOSTNAME> -s` |
| **Target Specific Share** | `Snaffler.exe -i \\<HOST>\<SHARE> -s` |
| **Tune Concurrency Threads**| `Snaffler.exe -s -t 120 -o out.log` |
| **Exclude Host from Scan** | `Snaffler.exe -s -c- <EXCLUDE_HOST>` |
| **Maximum File Size Limit**| `Snaffler.exe -s -z 5000000` *(Limit to 5MB files)* |
| **Execute via C2 Assembly**| `execute-assembly Snaffler.exe -s` |

---

## 🛡️ Defensive Considerations & OPSEC

1. **Massive SMB Connection Spikes:** Snaffler connects to port 445 on every domain machine within minutes. This generates thousands of Event ID 4624 (Successful Logon Type 3 - Network) and Event ID 5140 (A network share object was accessed).
2. **Honeyfiles & Decoys:** Modern Active Directory defenders deploy Canary shares and decoy `.kdbx` files that trigger instantaneous SOC alerts when opened by tools like Snaffler.
3. **Execution Limits:** Set `-z` (maximum file byte size) to avoid crashing network shares by reading multi-gigabyte ISO or VHD backup files.

---

## ⚠️ Legal Disclaimer

> [!CAUTION]
> Snaffler is designed exclusively for authorized penetration tests, red team engagements, and internal security audits. Scanning domain shares without authorization constitutes illegal network trespass.