# 🕶️ mitmproxy - Complete Cheatsheet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)

```
  ███╗   ███╗██╗████████╗███╗   ███╗██████╗ ██████╗  ██████╗ ██╗  ██╗██╗   ██╗
  ████╗ ████║██║╚══██╔══╝████╗ ████║██╔══██╗██╔══██╗██╔═══██╗╚██╗██╔╝╚██╗ ██╔╝
  ██╔████╔██║██║   ██║   ██╔████╔██║██████╔╝██████╔╝██║   ██║ ╚███╔╝  ╚████╔╝ 
  ██║╚██╔╝██║██║   ██║   ██║╚██╔╝██║██╔═══╝ ██╔══██╗██║   ██║ ██╔██╗   ╚██╔╝  
  ██║ ╚═╝ ██║██║   ██║   ██║ ╚═╝ ██║██║     ██║  ██║╚██████╔╝██╔╝ ██╗   ██║   
  ╚═╝     ╚═╝╚═╝   ╚═╝   ╚═╝     ╚═╝╚═╝     ╚═╝  ╚═╝ ╚═════╝ ╚═╝  ╚═╝   ╚═╝   
       Interactive, Scriptable Terminal HTTP/HTTPS Proxy & Traffic Engine
```

<p align="center">
  <img src="https://img.shields.io/badge/mitmproxy-Terminal_Proxy-blue?style=for-the-badge" alt="mitmproxy">
  <img src="https://img.shields.io/badge/Author-mitmproxy_Project-orange?style=for-the-badge" alt="mitmproxy">
  <img src="https://img.shields.io/badge/Category-Network_&_Web_Analysis-green?style=for-the-badge" alt="Network Analysis">
  <img src="https://img.shields.io/badge/License-MIT-purple?style=for-the-badge" alt="MIT">
</p>

<p align="center">
  <b>🕶️ An interactive, SSL/TLS-capable intercepting HTTP proxy for penetration testers, security researchers, and software engineers with a rich console interface and Python scripting API</b>
</p>

---

## 📋 Table of Contents

- [Overview & The Three Binaries](#-overview--the-three-binaries)
- [Installation & Certificate Setup](#-installation--certificate-setup)
  - [Installing on Linux / macOS](#installing-on-linux--macos)
  - [Installing CA Certificate for SSL/TLS Interception](#installing-ca-certificate-for-ssltls-interception)
- [Operating Modes: Regular, Transparent & Reverse Proxy](#-operating-modes-regular-transparent--reverse-proxy)
- [Interactive TUI Navigation (`mitmproxy`)](#-interactive-tui-navigation-mitmproxy)
  - [Keyboard Shortcuts](#keyboard-shortcuts)
  - [Filtering & Intercepting Flows](#filtering--intercepting-flows)
- [Non-Interactive Automation (`mitmdump`)](#-non-interactive-automation-mitmdump)
  - [Recording & Replaying Traffic](#recording--replaying-traffic)
- [Python Scripting & Addons](#-python-scripting--addons)
  - [Automated Header Manipulation Script](#automated-header-manipulation-script)
  - [Live Response Body Tampering](#live-response-body-tampering)
- [Quick Reference Table](#-quick-reference-table)
- [Defensive Considerations & Detection](#-defensive-considerations--detection)
- [Legal Disclaimer](#-legal-disclaimer)

---

## 🎯 Overview & The Three Binaries

Unlike bulky graphical desktop proxies, **mitmproxy** delivers lightning-fast, scriptable, full-fidelity HTTP/1, HTTP/2, and WebSockets interception directly inside your terminal or automation pipeline.

It ships with three entry-point binaries:
1. **`mitmproxy`:** The interactive, ncurses-based Terminal User Interface (TUI).
2. **`mitmdump`:** The non-interactive command-line version (the `tcpdump` equivalent for HTTP).
3. **`mitmweb`:** A lightweight browser-based graphical web interface.

---

## 📥 Installation & Certificate Setup

### Installing on Linux / macOS
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y mitmproxy

# Arch Linux
sudo pacman -S mitmproxy

# Python pip / pipx
pipx install mitmproxy
```

### Installing CA Certificate for SSL/TLS Interception
To intercept HTTPS traffic, targets must trust mitmproxy's dynamic Certificate Authority:
1. Start mitmproxy once: `mitmproxy` (creates certificates in `~/.mitmproxy/`).
2. On the client device/browser, configure proxy `127.0.0.1:8080`.
3. Browse to `http://mitm.it` and download/install the certificate for Android, iOS, Windows, or Linux.

---

## ⚙️ Operating Modes: Regular, Transparent & Reverse Proxy

### Regular Proxy Mode (Default)
Standard forward proxy listening on port 8080:
```bash
mitmproxy -p 8080
```

### Reverse Proxy Mode
Relays incoming traffic to a specific target server (ideal for API testing):
```bash
mitmproxy --mode reverse:https://api.target.com -p 8080
```

### Transparent Mode
Intercepts traffic routed through iptables without configuring client proxy settings:
```bash
mitmproxy --mode transparent --showhost
```

---

## 🖥️ Interactive TUI Navigation (`mitmproxy`)

### Keyboard Shortcuts
- `?`: Open help overlay.
- `Enter`: Inspect selected HTTP flow (Request / Response / Detail).
- `q`: Go back / quit.
- `d`: Delete current flow.
- `r`: Replay selected request to server.
- `e`: Edit request/response headers, path, or body.
- `a`: Resume paused flow (when intercepting).
- `A`: Resume all paused flows.

### Filtering & Intercepting Flows
Press `i` to enter an interception filter expression:
```text
~u /api/v1/ & ~m POST      # Intercept only POST requests to /api/v1/
~c 401 | ~c 403            # Filter flows by HTTP status code
~b "password"              # Filter flows where body contains "password"
```

---

## ⚡ Non-Interactive Automation (`mitmdump`)

### Recording & Replaying Traffic
Capture live traffic directly into a binary dump file:
```bash
# Record all HTTP traffic to file
mitmdump -w traffic.dump

# Read and inspect captured dump
mitmdump -n -r traffic.dump

# Replay client requests from dump against target
mitmdump --client-replay traffic.dump
```

---

## 🐍 Python Scripting & Addons

mitmproxy features a native Python API for modifying traffic programmatically:

### Automated Header Manipulation Script (`add_headers.py`)
```python
from mitmproxy import http

class AddHeader:
    def request(self, flow: http.HTTPFlow):
        # Inject custom authorization or testing header into every outbound request
        flow.request.headers["X-Security-Audit"] = "Authorized-Pentest"
        flow.request.headers["Authorization"] = "Bearer injected_token_xyz"

addons = [AddHeader()]
```
Run with:
```bash
mitmproxy -s add_headers.py
```

### Live Response Body Tampering (`tamper_response.py`)
```python
from mitmproxy import http

def response(flow: http.HTTPFlow):
    # Force admin privilege in returned JSON response
    if "api/user/me" in flow.request.url:
        flow.response.text = flow.response.text.replace('"isAdmin": false', '"isAdmin": true')
```
Run with:
```bash
mitmdump -s tamper_response.py
```

---

## ⚡ Quick Reference Table

| Objective | Command |
|-----------|---------|
| **Start Interactive TUI** | `mitmproxy -p 8080` |
| **Start Web GUI** | `mitmweb -p 8080` |
| **Silent CLI Logger** | `mitmdump -p 8080` |
| **Reverse Proxy Target** | `mitmproxy --mode reverse:https://target.com` |
| **Record Traffic to File** | `mitmdump -w capture.mitm` |
| **Load Python Addon Script**| `mitmproxy -s script.py` |
| **Ignore Target Hosts** | `mitmproxy --ignore-hosts ".*apple\.com"` |

---

## 🛡️ Defensive Considerations & Detection

1. **Certificate Pinning:** Mobile apps enforcing SSL Pinning reject mitmproxy's CA certificate. Pair with Frida / Objection to bypass client-side checks.
2. **Proxy Detection in TLS Handshake:** Proxies introduce custom TLS cipher suites and ALPN negotiation fingerprints that WAFs and bot managers can detect.

---

## ⚠️ Legal Disclaimer

> [!CAUTION]
> mitmproxy intercepts and decrypts sensitive network communications. It must strictly be deployed on systems and networks where explicit authorization has been secured. Intercepting communications without consent is illegal.
