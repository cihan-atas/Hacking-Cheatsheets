# 🌲 adidnsdump - Complete Cheatsheet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)

```
   █████╗ ██████╗ ██╗██████╗ ███╗   ██╗███████╗██████╗ ██╗   ██╗███╗   ███╗██████╗ 
  ██╔══██╗██╔══██╗██║██╔══██╗████╗  ██║██╔════╝██╔══██╗██║   ██║████╗ ████║██╔══██╗
  ███████║██║  ██║██║██║  ██║██╔██╗ ██║███████╗██║  ██║██║   ██║██╔████╔██║██████╔╝
  ██╔══██║██║  ██║██║██║  ██║██║╚██╗██║╚════██║██║  ██║██║   ██║██║╚██╔╝██║██╔═══╝ 
  ██║  ██║██████╔╝██║██████╔╝██║ ╚████║███████║██████╔╝╚██████╔╝██║ ╚═╝ ██║██║     
  ╚═╝  ╚═╝╚═════╝ ╚═╝╚═════╝ ╚═╝  ╚═══╝╚══════╝╚═════╝  ╚═════╝ ╚═╝     ╚═╝╚═╝     
           Active Directory Integrated DNS (ADIDNS) Reconnaissance Tool
```

<p align="center">
  <img src="https://img.shields.io/badge/Tool-adidnsdump-blue?style=for-the-badge" alt="adidnsdump">
  <img src="https://img.shields.io/badge/Author-Dirk--jan_Mollema-orange?style=for-the-badge" alt="dirkjanm">
  <img src="https://img.shields.io/badge/Category-Active_Directory-green?style=for-the-badge" alt="Active Directory">
  <img src="https://img.shields.io/badge/Protocol-LDAP_%2F_LDAPS-purple?style=for-the-badge" alt="LDAP">
</p>

<p align="center">
  <b>🌲 Enumerate and dump Active Directory Integrated DNS (ADIDNS) zones and records over LDAP without DNS zone transfers (AXFR)</b>
</p>

---

## 📋 Table of Contents

- [Overview & Technical Background](#-overview--technical-background)
- [Installation](#-installation)
- [Authentication Methods](#-authentication-methods)
  - [Password Authentication](#password-authentication)
  - [Pass-the-Hash (NTLM)](#pass-the-hash-ntlm)
  - [Kerberos Ticket (ccache)](#kerberos-ticket-ccache)
- [Core Dumping Commands](#-core-dumping-commands)
  - [Standard Active Directory DNS Zone Dump](#standard-active-directory-dns-zone-dump)
  - [Dumping Tombstoned / Deleted Records](#dumping-tombstoned--deleted-records)
  - [Dumping Forest DNS & Domain DNS Zones](#dumping-forest-dns--domain-dns-zones)
  - [Targeting Specific DNS Zones](#targeting-specific-dns-zones)
- [Export Formats & Post-Processing](#-export-formats--post-processing)
  - [CSV Output](#csv-output)
  - [Integrating with BloodHound & Nmap](#integrating-with-bloodhound--nmap)
- [Quick Reference Table](#-quick-reference-table)
- [Defensive Considerations & Detection](#-defensive-considerations--detection)
- [Legal Disclaimer](#-legal-disclaimer)

---

## 🎯 Overview & Technical Background

In standard Windows Active Directory environments, DNS is usually integrated into Active Directory (**Active Directory-Integrated DNS / ADIDNS**). In these configurations, DNS records are not stored in simple text flat-files, but as LDAP objects within the domain partition (such as `DC=DomainDnsZones` and `DC=ForestDnsZones`).

Traditional DNS zone transfers (`AXFR`) are almost universally disabled on domain controllers. However, **any authenticated domain user** (even low-privileged accounts or compromised service accounts) has read permissions over the LDAP nodes storing ADIDNS records by default.

Developed by **Dirk-jan Mollema (@dirkjanm)**, **`adidnsdump`** connects to the Domain Controller via LDAP or LDAPS, queries the ADIDNS partitions, and extracts all records (A, AAAA, CNAME, SRV, TXT, MX, and tombstoned records), creating an accurate inventory of every computer, workstation, and internal web service in the domain.

---

## 📥 Installation

```bash
# Clone the repository and install with pip
git clone https://github.com/dirkjanm/adidnsdump.git
cd adidnsdump
pip install .

# Or install directly via pipx / pip
pipx install git+https://github.com/dirkjanm/adidnsdump.git
```

---

## 🔑 Authentication Methods

### Password Authentication
```bash
adidnsdump -u 'CORP\jdoe' -p 'P@ssw0rd123!' 192.168.1.10
```

### Pass-the-Hash (NTLM)
If you extracted NTLM hashes via Mimikatz or Secretsdump:
```bash
adidnsdump -u 'CORP\jdoe' --hashes :31d6cfe0d16ae931b73c59d7e0c089c0 192.168.1.10
```

### Kerberos Ticket (ccache)
Using an existing Kerberos TGT/TGS ticket stored in `KRB5CCNAME`:
```bash
export KRB5CCNAME=/tmp/jdoe.ccache
adidnsdump -k -u 'CORP\jdoe' dc01.corp.local
```

---

## 🚀 Core Dumping Commands

### Standard Active Directory DNS Zone Dump
Dumps all active DNS records from the default DomainDnsZones partition:
```bash
adidnsdump -u 'corp.local\jdoe' -p 'P@ssw0rd123!' ldap://dc01.corp.local
```
This generates a file named `records.csv` in your current working directory.

### Dumping Tombstoned / Deleted Records
Windows DNS marks deleted records as "tombstoned" for several days before physical deletion. These records often reveal decommissioned systems, legacy servers, or test environments:
```bash
adidnsdump -u 'corp.local\jdoe' -p 'P@ssw0rd123!' --include-tombstoned ldap://dc01.corp.local
```

### Dumping Forest DNS & Domain DNS Zones
Extract records from Forest-level DNS partitions:
```bash
adidnsdump -u 'corp.local\jdoe' -p 'P@ssw0rd123!' --forest ldap://dc01.corp.local
```

### Targeting Specific DNS Zones
If the target operates multiple split-brain DNS zones or subdomains:
```bash
adidnsdump -u 'corp.local\jdoe' -p 'P@ssw0rd123!' --zone dev.corp.local ldap://dc01.corp.local
```

### Secure LDAPS Encrypted Communication
Prevent credentials from traversing the network in plaintext:
```bash
adidnsdump --ssl -u 'corp.local\jdoe' -p 'P@ssw0rd123!' ldaps://dc01.corp.local
```

---

## 📊 Export Formats & Post-Processing

### CSV Output
By default, `adidnsdump` outputs `records.csv`. The fields include:
- `zone`: The DNS zone the record belongs to.
- `name`: Record hostname or label.
- `type`: Record type (A, AAAA, CNAME, SRV, etc.).
- `value`: Target IP address, hostname, or string.

### Integrating with BloodHound & Nmap
Extract all unique IP addresses from the dumped records for automated scanning:
```bash
# Filter unique IP addresses (Type A records)
awk -F',' '$3=="A" {print $4}' records.csv | sort -u > live_ips.txt

# Run fast port scan against newly discovered internal hosts
nmap -sS -Pn -iL live_ips.txt -T4 -p 80,443,445,3389 -oA internal_assets
```

---

## ⚡ Quick Reference Table

| Objective | Command |
|-----------|---------|
| **Standard ADIDNS Dump** | `adidnsdump -u 'DOMAIN\User' -p 'Pass' <DC_IP>` |
| **Pass-the-Hash (NTLM)** | `adidnsdump -u 'DOMAIN\User' --hashes :<NTLM> <DC_IP>` |
| **Kerberos Ticket Auth** | `adidnsdump -k -u 'DOMAIN\User' <DC_FQDN>` |
| **Dump Tombstoned Records**| `adidnsdump -u 'DOMAIN\User' -p 'Pass' --include-tombstoned <DC_IP>` |
| **Dump Forest Zones** | `adidnsdump -u 'DOMAIN\User' -p 'Pass' --forest <DC_IP>` |
| **Specify Target Zone** | `adidnsdump -u 'DOMAIN\User' -p 'Pass' --zone <ZONE> <DC_IP>` |
| **Enforce LDAPS (SSL)** | `adidnsdump --ssl -u 'DOMAIN\User' -p 'Pass' ldaps://<DC_IP>` |

---

## 🛡️ Defensive Considerations & Detection

1. **LDAP Query Monitoring:** Enable Advanced Audit Policy for Directory Service Access (Event ID 4662) to alert on high-volume queries against `CN=MicrosoftDNS,DC=DomainDnsZones`.
2. **Restrict ADIDNS ACLs:** Restrict read permissions on the `MicrosoftDNS` container to domain administrators or specific administrative roles if non-admins do not require internal DNS introspection.
3. **Honeytokens:** Plant attractive DNS records (e.g., `backup-vault.corp.local` or `admin-portal.corp.local`) pointing to honeypot IP addresses.

---

## ⚠️ Legal Disclaimer

> [!CAUTION]
> adidnsdump is intended strictly for authorized security assessments, Active Directory audits, and penetration testing within permitted scopes. Querying directory services on unauthorized networks is illegal.
