# 📦 smuggler - Complete Cheatsheet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)

```
  ███████╗███╗   ███╗██╗   ██╗ ██████╗  ██████╗ ██╗     ███████╗██████╗ 
  ██╔════╝████╗ ████║██║   ██║██╔════╝ ██╔════╝ ██║     ██╔════╝██╔══██╗
  ███████╗██╔████╔██║██║   ██║██║  ███╗██║  ███╗██║     █████╗  ██████╔╝
  ╚════██║██║╚██╔╝██║██║   ██║██║   ██║██║   ██║██║     ██╔══╝  ██╔══██╗
  ███████║██║ ╚═╝ ██║╚██████╔╝╚██████╔╝╚██████╔╝███████╗███████╗██║  ██║
  ╚══════╝╚═╝     ╚═╝ ╚═════╝  ╚═════╝  ╚═════╝ ╚══════╝╚══════╝╚═╝  ╚═╝
             High-Performance HTTP Request Smuggling Scanner
```

<p align="center">
  <img src="https://img.shields.io/badge/smuggler-HTTP_Smuggling-blue?style=for-the-badge" alt="smuggler">
  <img src="https://img.shields.io/badge/Author-Evan_Ricaurte-orange?style=for-the-badge" alt="defparam">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-green?style=for-the-badge" alt="Web Security">
  <img src="https://img.shields.io/badge/License-MIT-purple?style=for-the-badge" alt="MIT">
</p>

<p align="center">
  <b>📦 Ultra-fast, modular Python tool designed to detect and verify HTTP Request Smuggling vulnerabilities (CL.TE, TE.CL, and header mutation desyncs) across modern web infrastructure</b>
</p>

---

## 📋 Table of Contents

- [Overview & HTTP Smuggling Theory](#-overview--http-smuggling-theory)
  - [CL.TE vs TE.CL Discrepancies](#clte-vs-tecl-discrepancies)
- [Installation](#-installation)
- [Basic Target Scanning](#-basic-target-scanning)
- [Scan Configurations & Mutation Engines](#-scan-configurations--mutation-engines)
  - [Default vs Exhaustive Scan](#default-vs-exhaustive-scan)
  - [Custom Headers & Cookies](#custom-headers--cookies)
- [Multi-Target & Pipeline Scanning](#-multi-target--pipeline-scanning)
- [Analyzing Results & Exploitation Paths](#-analyzing-results--exploitation-paths)
  - [Cache Poisoning via Smuggling](#cache-poisoning-via-smuggling)
  - [Bypassing Front-End Security Controls](#bypassing-front-end-security-controls)
  - [Hijacking Other Users' Requests](#hijacking-other-users-requests)
- [Quick Reference Table](#-quick-reference-table)
- [Defensive Considerations & Remediation](#-defensive-considerations--remediation)
- [Legal Disclaimer](#-legal-disclaimer)

---

## 🎯 Overview & HTTP Smuggling Theory

HTTP Request Smuggling occurs when a front-end proxy/load balancer and a back-end web server disagree on the boundaries of an HTTP request:

### CL.TE vs TE.CL Discrepancies
- **CL.TE:** Front-end uses `Content-Length`, back-end uses `Transfer-Encoding: chunked`.
- **TE.CL:** Front-end uses `Transfer-Encoding: chunked`, back-end uses `Content-Length`.
- **TE.TE:** Both support `Transfer-Encoding`, but one can be tricked into ignoring it via header obfuscation (e.g., `Transfer-Encoding: xchunked`, tabs, or line folding).

Created by **defparam (Evan Ricaurte)**, **smuggler** injects carefully crafted payloads to observe timing delays or desynchronization responses without breaking backend service stability.

---

## 📥 Installation

```bash
# Clone the repository
git clone https://github.com/defparam/smuggler.git
cd smuggler

# Run using Python 3
python3 smuggler.py -h
```

---

## 🚀 Basic Target Scanning

Scan a single web application endpoint:

```bash
# Basic scan against a single target
python3 smuggler.py -u https://target.com/
```

By default, smuggler tests for both `CL.TE` and `TE.CL` behaviors using safe timing payloads.

---

## ⚙️ Scan Configurations & Mutation Engines

### Default vs Exhaustive Scan
Smuggler includes different payload profiles:
- **Default profile:** Fast, common desync mutations.
- **Exhaustive profile:** Comprehensive mutations testing whitespace variations, multiple headers, and obfuscated transfer-encoding headers:

```bash
# Run exhaustive scan testing all mutation gadgets
python3 smuggler.py -u https://target.com/ -c exhaustive
```

### Custom Headers & Cookies
When authentication or specific host headers are required:
```bash
python3 smuggler.py -u https://target.com/api/ \
  -H "Cookie: session=xyz123" \
  -H "X-Forwarded-Host: target.com"
```

---

## 🌐 Multi-Target & Pipeline Scanning

Pipe endpoints discovered from `httpx` or `subfinder` directly into smuggler:

```bash
# Scan a list of URLs from file
python3 smuggler.py -f live_hosts.txt -t 10

# Adjust timeout per probe (default: 10s)
python3 smuggler.py -u https://target.com/ --timeout 5
```

---

## 💥 Analyzing Results & Exploitation Paths

When smuggler reports a positive match:
```text
[CRITICAL] CL.TE Desync Detected: https://target.com/
```

### Cache Poisoning via Smuggling
Smuggle a request that generates an off-site redirect (e.g. Host header manipulation) to poison web caches serving JavaScript libraries to all users.

### Bypassing Front-End Security Controls
If the front-end proxy blocks `/admin`, smuggle a secondary internal request that reaches the back-end directly without proxy inspection:
```http
POST / HTTP/1.1
Host: target.com
Content-Length: 48
Transfer-Encoding: chunked

0

GET /admin HTTP/1.1
Host: localhost
```

### Hijacking Other Users' Requests
Smuggle a prefix of an HTTP request. When the next user sends their request, it gets appended to your smuggled request, exposing their cookies and session tokens.

---

## ⚡ Quick Reference Table

| Objective | Command |
|-----------|---------|
| **Quick Single Target** | `python3 smuggler.py -u https://target.com/` |
| **Exhaustive Deep Scan**| `python3 smuggler.py -u https://target.com/ -c exhaustive` |
| **Scan Target List** | `python3 smuggler.py -f targets.txt -t 15` |
| **Custom Headers/Cookies**| `python3 smuggler.py -u <URL> -H "Cookie: sess=123"` |
| **Custom Timeout** | `python3 smuggler.py -u <URL> --timeout 15` |

---

## 🛡️ Defensive Considerations & Remediation

1. **Enforce HTTP/2 or HTTP/3 End-to-End:** HTTP/2 defines unambiguous frame lengths, eliminating header ambiguity.
2. **Normalize HTTP Headers:** Configure reverse proxies to reject requests containing both `Content-Length` and `Transfer-Encoding`.
3. **Drop Ambiguous Mutations:** Drop requests with obfuscated `Transfer-Encoding` headers (e.g., spaces before colon, multiple values).

---

## ⚠️ Legal Disclaimer

> [!CAUTION]
> HTTP Request Smuggling can disrupt live backend queues and poison shared caching nodes. smuggler must be used exclusively on explicitly authorized test targets with strict permission from asset owners.
