# 🪁 Kiterunner - Complete Cheatsheet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)

```
  ██╗  ██╗██╗████████╗███████╗██████╗ ██╗   ██╗███╗   ██╗███╗   ██╗███████╗██████╗ 
  ██║ ██╔╝██║╚══██╔══╝██╔════╝██╔══██╗██║   ██║████╗  ██║████╗  ██║██╔════╝██╔══██╗
  █████╔╝ ██║   ██║   █████╗  ██████╔╝██║   ██║██╔██╗ ██║██╔██╗ ██║█████╗  ██████╔╝
  ██╔═██╗ ██║   ██║   ██╔══╝  ██╔══██╗██║   ██║██║╚██╗██║██║╚██╗██║██╔══╝  ██╔══██╗
  ██║  ██╗██║   ██║   ███████╗██║  ██║╚██████╔╝██║ ╚████║██║ ╚████║███████╗██║  ██║
  ╚═╝  ╚═╝╚═╝   ╚═╝   ╚══════╝╚═╝  ╚═╝ ╚═════╝ ╚═╝  ╚═══╝╚═╝  ╚═══╝╚══════╝╚═╝  ╚═╝
       Context-Aware API Endpoint & Route Discovery Engine by Assetnote
```

<p align="center">
  <img src="https://img.shields.io/badge/Kiterunner-API_Discovery-blue?style=for-the-badge" alt="Kiterunner">
  <img src="https://img.shields.io/badge/Author-Assetnote-orange?style=for-the-badge" alt="Assetnote">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-green?style=for-the-badge" alt="Web Security & API">
  <img src="https://img.shields.io/badge/License-Apache_2.0-red?style=for-the-badge" alt="License Apache 2.0">
</p>

<p align="center">
  <b>🌐 High-performance REST and GraphQL API route brute-forcer designed to map modern API architectures using contextual datasets</b>
</p>

---

## 📋 Table of Contents

- [Overview & The API Discovery Problem](#-overview--the-api-discovery-problem)
- [Tool Comparison: Kiterunner vs ffuf vs dirsearch](#-tool-comparison-kiterunner-vs-ffuf-vs-dirsearch)
- [Installation](#-installation)
- [Downloading Assetnote Kite Datasets](#-downloading-assetnote-kite-datasets)
- [Scanning APIs (`kr scan`)](#-scanning-apis-kr-scan)
- [Standard Wordlist Brute-Forcing (`kr brute`)](#-standard-wordlist-brute-forcing-kr-brute)
- [Headers, Cookies & Authentication](#-headers-cookies--authentication)
- [Filtering & Status Codes](#-filtering--status-codes)
- [Replaying Discovered Routes (`curl` output)](#-replaying-discovered-routes-curl-output)
- [Quick Reference Table](#-quick-reference-table)
- [Defensive Considerations & OPSEC](#-defensive-considerations--opsec)
- [Legal Disclaimer](#-legal-disclaimer)

---

## 🎯 Overview & The API Discovery Problem

Traditional web content discovery tools (`ffuf`, `dirsearch`, `gobuster`) use flat, single-word dictionaries (e.g., `admin`, `api`, `login`). While this works for traditional file hierarchies, **it fails against modern REST and GraphQL APIs**.

Modern APIs use nested, multi-tier routes such as:
`/api/v2/organizations/{org_id}/users/export?format=json`

**Kiterunner**, created by **Assetnote**, solves this by compiling real-world OpenAPI, Swagger, Postman, and schema definitions into compact binary datasets (`.kite`). Instead of guessing random words, Kiterunner reconstructs entire hierarchical API call structures with correct HTTP methods (`GET`, `POST`, `PUT`, `DELETE`).

---

## ⚖️ Tool Comparison: Kiterunner vs ffuf vs dirsearch

| Feature | Kiterunner (`kr`) | ffuf | dirsearch |
|---------|-------------------|------|-----------|
| **Core Target Focus** | REST, GraphQL, OpenAPI | General Web Fuzzing | Directory Brute-Forcing |
| **Route Hierarchy Awareness**| ✅ Native (`.kite` files) | ❌ Flat wordlist | ❌ Flat wordlist |
| **HTTP Verb Swapping** | ✅ Tests GET, POST, PUT, DELETE| ❌ Manual configuration | ❌ Limited |
| **Swagger / OpenAPI Parsing** | ✅ Native schema ingestion | ❌ No | ❌ No |
| **Speed & Concurrency** | ⭐⭐⭐⭐⭐ (Go Engine) | ⭐⭐⭐⭐⭐ (Go Engine) | ⭐⭐⭐⭐ (Python) |

---

## 📥 Installation

```bash
# Download precompiled binary from GitHub Releases
wget https://github.com/assetnote/kiterunner/releases/download/v1.0.2/kiterunner_1.0.2_linux_amd64.tar.gz
tar -xzvf kiterunner_1.0.2_linux_amd64.tar.gz
sudo mv kr /usr/local/bin/

# Verify installation
kr version
```

---

## 📦 Downloading Assetnote Kite Datasets

Kiterunner relies on pre-built `.kite` route datasets created from millions of analyzed public APIs:

```bash
# Create wordlists directory
mkdir -p ~/.kiterunner && cd ~/.kiterunner

# Small dataset (~40MB, 2.5 million API routes - Recommended for fast scans)
wget https://wordlists-cdn.assetnote.io/data/kiterunner/routes-small.kite.tar.gz
tar -xzvf routes-small.kite.tar.gz

# Large dataset (~130MB, 8.5 million API routes - Exhaustive testing)
wget https://wordlists-cdn.assetnote.io/data/kiterunner/routes-large.kite.tar.gz
tar -xzvf routes-large.kite.tar.gz
```

---

## 🚀 Scanning APIs (`kr scan`)

The `scan` command uses `.kite` datasets to probe targets using contextual route hierarchies:

```bash
# Scan a single target using the small dataset
kr scan https://target.local/api -w ~/.kiterunner/routes-small.kite

# Scan multiple hosts from a file with 100 concurrent workers
kr scan targets.txt -w ~/.kiterunner/routes-small.kite -c 100 -j 50

# Scan with maximum verbosity
kr scan https://target.local -w ~/.kiterunner/routes-small.kite -v
```

---

## 🔨 Standard Wordlist Brute-Forcing (`kr brute`)

If you want to use standard `.txt` wordlists while taking advantage of Kiterunner's multi-method API engine:

```bash
# Brute force standard paths using a text dictionary
kr brute https://target.local -w /usr/share/wordlists/dirb/common.txt

# Brute force with custom API prefixes
kr brute https://target.local/api/v1 -w endpoints.txt
```

---

## 🔑 Headers, Cookies & Authentication

APIs frequently require JWTs, Bearer tokens, or custom organization headers:

```bash
# Pass custom Authorization header
kr scan https://target.local/api -w routes-small.kite -H "Authorization: Bearer eyJhbGci..."

# Pass custom API key and Cookie
kr scan https://target.local -w routes-small.kite -H "X-API-Key: secret123" -H "Cookie: session=xyz"
```

---

## 🎯 Filtering & Status Codes

```bash
# Exclude standard error codes (404, 403, 502)
kr scan https://target.local -w routes-small.kite -x 404,403,502

# Filter out responses by length / content size
kr scan https://target.local -w routes-small.kite --filter-length 0,324
```

---

## 🔄 Replaying Discovered Routes (`curl` output)

Kiterunner can output discovered routes as actionable `curl` commands, allowing instant reproduction in terminal or Burp Suite:

```bash
# Print reproducible curl command lines for all successful matches
kr scan https://target.local -w routes-small.kite -o json | jq -r '.[] | .curl'
```

---

## ⚡ Quick Reference Table

| Objective | Command |
|-----------|---------|
| **Scan API with Small Dataset** | `kr scan <URL> -w routes-small.kite` |
| **Scan API with Large Dataset** | `kr scan <URL> -w routes-large.kite` |
| **Scan Multiple Hosts** | `kr scan hosts.txt -w routes-small.kite -c 100` |
| **Brute Force Text Wordlist** | `kr brute <URL> -w wordlist.txt` |
| **Authenticate with Bearer Token**| `kr scan <URL> -w routes-small.kite -H "Authorization: Bearer <TOKEN>"` |
| **Exclude Status Codes** | `kr scan <URL> -w routes-small.kite -x 404,403` |
| **Output to JSON File** | `kr scan <URL> -w routes-small.kite -o json -oFile results.json` |

---

## 🛡️ Defensive Considerations & OPSEC

1. **Massive Request Volume:** The `routes-large.kite` dictionary generates millions of requests. Ensure rate limiting (`-q 200`) is enabled when testing production environments.
2. **State-Changing Methods:** Kiterunner sends `POST`, `PUT`, and `DELETE` requests. On unhardened test APIs, this can inadvertently modify or delete backend records.

---

## ⚠️ Legal Disclaimer

> [!CAUTION]
> Kiterunner is designed for authorized API security testing and bug bounty engagements only. Fuzzing unauthorized APIs is strictly illegal.