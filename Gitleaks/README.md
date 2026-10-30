# 🔑 Gitleaks - Complete Cheatsheet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)

```
   ██████╗ ██╗████████╗██╗     ███████╗ █████╗ ██╗  ██╗███████╗
  ██╔════╝ ██║╚══██╔══╝██║     ██╔════╝██╔══██╗██║ ██╔╝██╔════╝
  ██║  ███╗██║   ██║   ██║     █████╗  ███████║█████╔╝ ███████╗
  ██║   ██║██║   ██║   ██║     ██╔══╝  ██╔══██║██╔═██╗ ╚════██║
  ╚██████╔╝██║   ██║   ███████╗███████╗██║  ██║██║  ██╗███████║
   ╚═════╝ ╚═╝   ╚═╝   ╚══════╝╚══════╝╚═╝  ╚═╝╚═╝  ╚═╝╚══════╝
           Fast & Automated Secret Detection for Git Repositories
```

<p align="center">
  <img src="https://img.shields.io/badge/Gitleaks-Secret_Scanner-blue?style=for-the-badge" alt="Gitleaks">
  <img src="https://img.shields.io/badge/Author-Zachary_Rice-orange?style=for-the-badge" alt="zricethezav">
  <img src="https://img.shields.io/badge/Category-Source_Code_&_Secrets-green?style=for-the-badge" alt="Secrets">
  <img src="https://img.shields.io/badge/Language-Go-cyan?style=for-the-badge" alt="Go">
</p>

<p align="center">
  <b>🔑 SAST tool designed to find and detect hardcoded secrets like passwords, API keys, and tokens in Git repositories and commit histories</b>
</p>

---

## 📋 Table of Contents

- [Overview & Detection Engine](#-overview--detection-engine)
- [Installation](#-installation)
- [Basic Secret Scanning](#-basic-secret-scanning)
  - [Scanning a Git Repository](#scanning-a-git-repository)
  - [Scanning Uncommitted Changes](#scanning-uncommitted-changes)
  - [Scanning Plain Files & Folders](#scanning-plain-files--folders)
- [Targeting Commit Ranges & History](#-targeting-commit-ranges--history)
- [CI/CD & Pre-commit Integration](#-cicd--pre-commit-integration)
- [Custom Detection Rules & Allowlisting](#-custom-detection-rules--allowlisting)
- [Output Formats & Reporting](#-output-formats--reporting)
- [Quick Reference Table](#-quick-reference-table)
- [Defensive Considerations & Remediation](#-defensive-considerations--remediation)
- [Legal Disclaimer](#-legal-disclaimer)

---

## 🎯 Overview & Detection Engine

Hardcoded secrets (AWS access keys, SSH private keys, GitHub tokens, Slack webhooks, database credentials) inadvertently committed to git repositories are one of the most common vectors for initial cloud compromise.

Created by **Zachary Rice (@zricethezav)**, **Gitleaks** provides ultra-fast regex-based and Shannon entropy analysis across:
- **Full Git History:** Audits every branch, tag, and historical commit.
- **Pre-commit Hooks:** Blocks engineers from accidentally committing credentials before code reaches remote servers.
- **CI/CD Pipelines:** Automatically fails pull requests containing credential leaks.

---

## 📥 Installation

```bash
# macOS (Homebrew)
brew install gitleaks

# Go install
go install github.com/zricethezav/gitleaks/v8@latest

# Docker
docker pull zricethezav/gitleaks:latest

# Linux binary download
curl -sSL https://github.com/zricethezav/gitleaks/releases/latest/download/gitleaks_linux_x64.tar.gz | tar -xz
sudo mv gitleaks /usr/local/bin/
```

---

## 🚀 Basic Secret Scanning

### Scanning a Git Repository
Audits the current repository's commit history:
```bash
# Run detection in current working repo
gitleaks detect -v

# Audit an external local repo path
gitleaks detect --source /path/to/repo -v
```

### Scanning Uncommitted Changes
Audits staged and unstaged code changes before committing:
```bash
# Inspect unstaged working directory changes
gitleaks protect -v

# Scan staged changes only
gitleaks protect --staged -v
```

### Scanning Plain Files & Folders
Scan non-git directories (e.g., extracted firmware, decompiled mobile apps, configuration folders):
```bash
gitleaks dir /path/to/directory -v
```

---

## ⚡ Targeting Commit Ranges & History

```bash
# Scan commits made in the last 7 days
gitleaks detect --log-opts="--since='7 days ago'" -v

# Scan specific branch or tag comparison
gitleaks detect --log-opts="origin/main..feature-branch" -v

# Target the latest 5 commits
gitleaks detect --log-opts="-n 5" -v
```

---

## ⚙️ CI/CD & Pre-commit Integration

### Pre-commit Framework Configuration (`.pre-commit-config.yaml`)
```yaml
repos:
  - repo: https://github.com/gitleaks/gitleaks
    rev: v8.18.2
    hooks:
      - id: gitleaks
```

### GitHub Actions Workflow
```yaml
name: Gitleaks Security Scan
on: [push, pull_request]
jobs:
  gitleaks:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
        with:
          fetch-depth: 0
      - uses: gitleaks/gitleaks-action@v2
        env:
          GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}
```

---

## 🧩 Custom Detection Rules & Allowlisting

Configure custom regex patterns or ignore false positives using `.gitleaks.toml`:

```toml
[extend]
useDefault = true

[allowlist]
description = "Ignore test fixtures and demo tokens"
paths = [
  '''tests/.*''',
  '''fixtures/.*'''
]
regexes = [
  '''TEST_API_KEY_.*'''
]

[[rules]]
id = "custom-internal-token"
description = "Internal Company JWT Secret"
regex = '''(?i)(corp_secret_[a-z0-9]{32})'''
secretGroup = 1
entropy = 3.5
keywords = ["corp_secret_"]
```

Run with custom configuration:
```bash
gitleaks detect -c .gitleaks.toml -v
```

---

## 📊 Output Formats & Reporting

```bash
# Export results to clean JSON format
gitleaks detect --report-path gitleaks-report.json --report-format json

# Export to SARIF (for GitHub Security tab integration)
gitleaks detect --report-path report.sarif --report-format sarif

# Export to CSV
gitleaks detect --report-path report.csv --report-format csv
```

---

## ⚡ Quick Reference Table

| Objective | Command |
|-----------|---------|
| **Scan Entire Git History** | `gitleaks detect -v` |
| **Scan Uncommitted Changes** | `gitleaks protect -v` |
| **Scan Staged Changes Only** | `gitleaks protect --staged -v` |
| **Scan Plain Directory (No Git)**| `gitleaks dir /path/to/folder -v` |
| **Export to JSON** | `gitleaks detect -r report.json -f json` |
| **Export to SARIF** | `gitleaks detect -r report.sarif -f sarif` |
| **Use Custom Rules Config** | `gitleaks detect -c .gitleaks.toml` |
| **Scan Specific Branch** | `gitleaks detect --log-opts="main..dev"` |

---

## 🛡️ Defensive Considerations & Remediation

1. **Immediate Revocation:** Once a secret is detected in git history, consider it fully compromised and rotate/revoke it immediately.
2. **Rewriting Git History:** Removing the commit or using `git filter-repo` / BFG Repo-Cleaner is required to scrub artifacts, but never substitute history rewrites for credential rotation.

---

## ⚠️ Legal Disclaimer

> [!CAUTION]
> Gitleaks is designed for authorized internal security audits, continuous integration vulnerability checking, and defensive code review. Scanning unauthorized proprietary repositories without permission is strictly prohibited.
