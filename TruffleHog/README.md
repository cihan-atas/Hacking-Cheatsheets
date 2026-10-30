# 🐽 TruffleHog - Complete Cheatsheet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)

```
  ████████╗██████╗ ██╗   ██╗███████╗███████╗██╗     ███████╗██╗  ██╗ ██████╗  ██████╗ 
  ╚══██╔══╝██╔══██╗██║   ██║██╔════╝██╔════╝██║     ██╔════╝██║  ██║██╔═══██╗██╔════╝ 
     ██║   ██████╔╝██║   ██║█████╗  █████╗  ██║     █████╗  ███████║██║   ██║██║  ███╗
     ██║   ██╔══██╗██║   ██║██╔══╝  ██╔══╝  ██║     ██╔══╝  ██╔══██║██║   ██║██║   ██║
     ██║   ██║  ██║╚██████╔╝██║     ██║     ███████╗███████╗██║  ██║╚██████╔╝╚██████╔╝
     ╚═╝   ╚═╝  ╚═╝ ╚═════╝ ╚═╝     ╚═╝     ╚══════╝╚══════╝╚═╝  ╚═╝ ╚═════╝  ╚═════╝ 
       Find Exposed Credentials & Secrets with Real-Time Active Verification
```

<p align="center">
  <img src="https://img.shields.io/badge/TruffleHog-Secret_Scanner-blue?style=for-the-badge" alt="TruffleHog">
  <img src="https://img.shields.io/badge/Developer-Truffle_Security-orange?style=for-the-badge" alt="Truffle Security">
  <img src="https://img.shields.io/badge/Category-OSINT_&_Code_Security-green?style=for-the-badge" alt="Code Security">
  <img src="https://img.shields.io/badge/License-AGPLv3-red?style=for-the-badge" alt="License AGPLv3">
</p>

<p align="center">
  <b>🌐 High-accuracy secret scanner designed to hunt down leaked API keys, tokens, and private certificates across Git, S3, Docker, and filesystems with live validation</b>
</p>

---

## 📋 Table of Contents

- [Overview & The Live Verification Superpower](#-overview--the-live-verification-superpower)
- [TruffleHog vs Gitleaks Comparison](#-trufflehog-vs-gitleaks-comparison)
- [Installation](#-installation)
- [Scanning Git Repositories](#-scanning-git-repositories)
- [Scanning Local Filesystems & Directories](#-scanning-local-filesystems--directories)
- [Scanning Entire GitHub Organizations](#-scanning-entire-github-organizations)
- [Scanning S3 Buckets & Docker Images](#-scanning-s3-buckets--docker-images)
- [Filtering False Positives (`--only-verified`)](#-filtering-false-positives---only-verified)
- [JSON Output & CI/CD Integration](#-json-output--cicd-integration)
- [Quick Reference Table](#-quick-reference-table)
- [Defensive Considerations & OPSEC](#-defensive-considerations--opsec)
- [Legal Disclaimer](#-legal-disclaimer)

---

## 🎯 Overview & The Live Verification Superpower

Developers frequently commit high-privilege credentials into Git repositories (AWS access keys, Stripe private keys, Slack webhooks, SSH private keys, database passwords). Even when deleted in subsequent commits, **these secrets remain permanently buried in the repository commit history**.

Written in Go by **Truffle Security**, **TruffleHog** scans commit histories, filesystems, and cloud storage to uncover leaked secrets.

Its defining superpower is **Live Verification**:
Unlike traditional regex scanners that flood security teams with thousands of false positives (e.g., example documentation keys), TruffleHog contacts the respective cloud provider (AWS, GitHub, Slack, OpenAI) to **verify whether the extracted secret is actively functional**.

---

## ⚖️ TruffleHog vs Gitleaks Comparison

| Feature | TruffleHog | Gitleaks |
|---------|------------|----------|
| **Live Credential Verification** | ✅ Yes (750+ detectors tested live)| ❌ No (Regex only) |
| **Target Data Sources** | Git, GitHub, S3, Docker, Filesystem| Git, Local Files |
| **Speed on Huge Repos** | Fast (Go Multi-threaded) | Extremely Fast (Minimal regex) |
| **False Positive Rate** | Near Zero (with `--only-verified`) | Moderate (Requires allowlists)|
| **Pre-Commit Hook Friendly** | ✅ Supported | ✅ Highly Optimized |

---

## 📥 Installation

```bash
# Debian / Ubuntu / Kali Linux (via curl installer)
curl -sSfL https://raw.githubusercontent.com/trufflesecurity/trufflehog/main/scripts/install.sh | sudo sh -s -- -b /usr/local/bin

# Arch Linux (AUR)
yay -S trufflehog-bin

# Homebrew (macOS / Linux)
brew install trufflehog

# Docker Container
docker run -it --rm trufflesecurity/trufflehog:latest git https://github.com/target/repo
```

---

## 🚀 Scanning Git Repositories

```bash
# Scan a remote Git repository (including entire commit history)
trufflehog git https://github.com/targetcorp/public-project

# Scan an internal on-premise Git repository with authentication
trufflehog git https://username:token@gitlab.corp.local/repo.git

# Scan only the most recent commit (great for pre-commit hooks)
trufflehog git file://./ --since-commit HEAD~1
```

---

## 📂 Scanning Local Filesystems & Directories

Scan local directories, extracted backup archives, or deindexed web roots:

```bash
# Scan current directory recursively
trufflehog filesystem .

# Scan specific sensitive configuration directory
trufflehog filesystem /var/www/html/ --exclude-paths tests/,vendor/
```

---

## 🏢 Scanning Entire GitHub Organizations

Audit all public repositories belonging to an organization:

```bash
# Scan all public repos of a GitHub organization
trufflehog github --org=targetorg --token=YOUR_GITHUB_TOKEN

# Scan all private and public repositories across a personal account
trufflehog github --user=targetuser --token=YOUR_GITHUB_TOKEN
```

---

## ☁️ Scanning S3 Buckets & Docker Images

```bash
# Scan an AWS S3 bucket for exposed credentials in stored logs and backups
trufflehog s3 --bucket=target-backup-bucket

# Scan a Docker container image layers for baked-in secrets
trufflehog docker --image=targetcorp/api-server:latest
```

---

## 🎯 Filtering False Positives (`--only-verified`)

Suppress all unverified regex hits and display **only confirmed active credentials**:

```bash
# Report ONLY confirmed functional credentials verified against providers
trufflehog git https://github.com/target/repo --only-verified

# Scan filesystem for verified secrets only
trufflehog filesystem /opt/backups/ --only-verified
```

---

## 📊 JSON Output & CI/CD Integration

```bash
# Export findings to structured JSON format
trufflehog git https://github.com/target/repo --json > secrets_report.json

# Parse live AWS keys with jq
cat secrets_report.json | jq -r 'select(.Verified == true) | .Raw'
```

---

## ⚡ Quick Reference Table

| Objective | Command |
|-----------|---------|
| **Scan Remote Git Repo** | `trufflehog git <GIT_URL>` |
| **Scan Verified Secrets Only** | `trufflehog git <GIT_URL> --only-verified` |
| **Scan Local Filesystem** | `trufflehog filesystem <DIRECTORY>` |
| **Scan GitHub Organization** | `trufflehog github --org=<ORG> --token=<PAT>` |
| **Scan Docker Image** | `trufflehog docker --image=<IMAGE>` |
| **Scan S3 Bucket** | `trufflehog s3 --bucket=<BUCKET_NAME>` |
| **Export to JSON** | `trufflehog git <GIT_URL> --json` |

---

## 🛡️ Defensive Considerations & OPSEC

1. **Outbound API Verification Traffic:** When running without `--no-verification`, TruffleHog connects to third-party endpoints (Slack, AWS, Stripe) to test keys. This triggers authentication logs in the victim's cloud tenant! If stealth is paramount, disable live validation:
   ```bash
   trufflehog git <URL> --no-verification
   ```
2. **Key Revocation & Incident Response:** When valid secrets are discovered in public repositories, immediately rotate the credentials and check CloudTrail / audit logs for unauthorized access.

---

## ⚠️ Legal Disclaimer

> [!CAUTION]
> This cheatsheet is intended for authorized security audits, Red Team operations, and credential hygiene assessments. Utilizing discovered credentials to access unauthorized accounts or private cloud environments is strictly illegal.