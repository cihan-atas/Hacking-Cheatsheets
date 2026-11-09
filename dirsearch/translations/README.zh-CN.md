<!-- language-home-navigation -->
[🏠 中文主页](../../README.zh-CN.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🔍 dirsearch - 完整备忘单

[🇹🇷 Türkçe Sürüm]（翻译/README.tr.md）
```
  ██████╗ ██╗██████╗ ███████╗███████╗ █████╗ ██████╗  ██████╗██╗  ██╗
  ██╔══██╗██║██╔══██╗██╔════╝██╔════╝██╔══██╗██╔══██╗██╔════╝██║  ██║
  ██║  ██║██║██████╔╝███████╗█████╗  ███████║██████╔╝██║     ███████║
  ██║  ██║██║██╔══██╗╚════██║██╔══╝  ██╔══██║██╔══██╗██║     ██╔══██║
  ██████╔╝██║██║  ██║███████║███████╗██║  ██║██║  ██║╚██████╗██║  ██║
  ╚═════╝ ╚═╝╚═╝  ╚═╝╚══════╝╚══════╝╚═╝  ╚═╝╚═╝  ╚═╝ ╚═════╝╚═╝  ╚═╝
       Advanced Web Path Scanner & Directory Brute-Forcer
```
<p对齐=“中心”>
  <img src="https://img.shields.io/badge/dirsearch-Web_Bruteforcer-blue?style=for-the-badge" alt="dirsearch">
  <img src="https://img.shields.io/badge/Language-Python_Async-green?style=for-the-badge" alt="Python 异步">
  <img src="https://img.shields.io/badge/Recon-Web_Discovery-orange?style=for-the-badge" alt="网络发现">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="许可证 GPLv2">
</p>

<p对齐=“中心”>
  <b>🌐 快速、功能丰富的命令行 Web 路径扫描器，具有智能扩展处理、递归和爬网</b>
</p>

---

## 📋 目录

- [简介](#-简介)
- [工具比较](#-tool-comparison)
- [安装](#-安装)
- [基本用法和目标选择](#-basic-usage--target-selection)
- [扩展名和后缀管理](#-extension--suffix-management)
- [单词表和词典](#-单词表--词典)
- [递归和网络爬行](#-recursion--web-crawling)
- [线程、速率限制和延迟](#-threading-rate-limiting--delays)
- [过滤和匹配响应](#-filtering--matching-responses)
- [HTTP 请求定制](#-http-request-customization)
  - [自定义标头和用户代理](#custom-headers--user-agents)
  - [Cookie 与身份验证](#cookies--身份验证)
  - [POST 数据和请求方法](#post-data--request-methods)
- [代理、Tor 和规避](#-proxies-tor--evasion)
- [输出格式和报告](#-output-formats--reporting)
- [真实世界渗透测试和错误赏金食谱](#-real-world-pentest--bug-bounty-recipes)
- [快速参考表](#-quick-reference-table)
- [提示和最佳实践](#-tips--best-practices)
- [法律免责声明](#-legal-disclaimer)

---

## 🎯 简介

**dirsearch**（由 Mauro Soria 创建）是最流行、最通用的命令行工具之一，用于发现 Web 服务器上的隐藏文件和目录。它用 Python 编写，具有丰富的命令行参数、彩色终端输出、自动递归爬行、智能通配符扩展扩展和内置规避控件，提供了无与伦比的灵活性。

无论您是执行 Web 渗透测试、错误赏金狩猎还是内部 CTF 评估，`dirsearch` 都可以量身定制，以发现隐藏的管理界面、备份数据库、被遗忘的 API 端点和敏感凭据。

### 主要特点

|特色 |描述 |
|---------|-------------|
| **多线程** |高性能异步和线程请求引擎|
| **扩展通配符** |扩展 `-e *` 自动测试所有已知的 Web 文件扩展名 |
| **综合爬取** |遵循 HTML/JS 链接来种子递归目录队列 |
| **丰富的过滤** |按状态代码、字节大小、字数、行数或正则表达式过滤响应 |
| **原始请求重播** |通过 `--raw` 加载完整的 Burp Suite HTTP 请求 |
| **多种格式** |生成纯文本、JSON、CSV、Markdown 和 HTML 格式的报告 |

---

## ⚖️ 工具比较

|特色 |搜索 |噗噗|铁火克星 |戈巴斯特 |迪尔布 |
|--------|------------|------|-------------|----------|------|
| **语言** |蟒蛇 |去 |铁锈|去 | C |
| **速度** | ⭐⭐⭐⭐（快）| ⭐⭐⭐⭐⭐（极快）| ⭐⭐⭐⭐⭐（速度极快）| ⭐⭐⭐⭐（快）| ⭐⭐（低线）|
| **默认递归** | ✅ 自动 (`-r`) |可选（`-recursion`）| ✅ 自动 (`-d`) |可选（`-r`）| ✅ 自动 |
| **扩展通配符 (`*`)**| ✅ 原生 (`-e *`) | ❌ 否 | ❌ 否 | ❌ 否 | ❌ 否 |
| **内置网络爬虫** | ✅ 是 (`--crawl`) | ❌ 否 | ✅ 是 (`--extract-links`) | ❌ 否 | ❌ 否 |
| **POST/数据模糊测试** | ✅ 是 (`--data`) | ✅ 完整的 HTTP | ❌ 有限| ❌ 否 | ❌ 否 |
| **Burp 原始请求重播**| ✅ 是 (`--raw`) | ✅ 是 (`-request`) | ❌ 否 | ❌ 否 | ❌ 否 |
| **内置单词表** | ✅ 策划（`db/dicc.txt`）| ❌ 无 | ❌ 无 | ❌ 无 | ✅ 小清单 |

---

## 📥 安装

### 使用 pipx（推荐用于 Kali 和现代 Linux）
```bash
# Install via pipx (isolated Python environment)
pipx install dirsearch

# Verify installation
dirsearch --version
```
### 使用点
```bash
# Install via standard pip
pip3 install dirsearch
```
### 使用 Git 克隆（直接来自源）
```bash
# Clone the official repository
git clone https://github.com/maurosoria/dirsearch.git --depth 1
cd dirsearch

# Install requirements and run
pip3 install -r requirements.txt
python3 dirsearch.py --version
```
### Kali Linux 包和 Docker
```bash
# Kali / Debian repository
sudo apt update && sudo apt install -y dirsearch

# Docker container
docker run -it --rm maurosoria/dirsearch -u https://example.com -e php,html
```
---

## 🚀 基本用法和目标选择

### 单一目标
```bash
# Basic scan against a single URL specifying extensions
dirsearch -u https://target.com -e php,html,js,txt

# Scan with trailing slash forced
dirsearch -u https://target.com -e php -f
```
### 多个目标
```bash
# Scan a list of targets from file
dirsearch -l targets.txt -e php,aspx,txt

# Scan stdin input from subfinder / httpx
cat live_hosts.txt | dirsearch --stdin -e php
```
### 端口扫描和 CIDR 定位
```bash
# Scan specific ports on a host
dirsearch -u https://target.com --ports 80,443,8080,8443 -e php

# Scan an entire subnet range
dirsearch --cidr 192.168.1.0/24 -e html
```
---

## 🧩 扩展名和后缀管理

`dirsearch`拥有所有目录扫描仪中最强大的扩展管理系统：
```bash
# Specify multiple extensions
dirsearch -u https://target.com -e php,html,json,txt,zip

# Use the wildcard (*) to test ALL known extensions automatically
dirsearch -u https://target.com -e *

# Add prefixes to wordlist entries (e.g. test_admin, dev_admin)
dirsearch -u https://target.com -e php --prefixes test_,dev_,staging_,old_

# Add suffixes to wordlist entries (e.g. admin_backup, admin_old)
dirsearch -u https://target.com -e php --suffixes _bak,~,_2026,_old
```
---

## 📚 单词表和词典

`dirsearch` 在 `db/dicc.txt` 上捆绑了一个全面的、经过实战检验的单词列表：
```bash
# Run using the default bundled dictionary
dirsearch -u https://target.com -e php

# Use SecLists Common or Medium wordlists
dirsearch -u https://target.com -e php -w /usr/share/seclists/Discovery/Web-Content/directory-list-2.3-medium.txt

# Use multiple wordlists simultaneously
dirsearch -u https://target.com -e php -w list1.txt,list2.txt

# Force all wordlist entries to lowercase or uppercase
dirsearch -u https://target.com -e php --lowercase
dirsearch -u https://target.com -e php --uppercase
```
---

## 🔄 递归和网络爬行

### 递归扫描
```bash
# Recursively scan newly found directories
dirsearch -u https://target.com -e php -r

# Limit recursion depth to 2 levels
dirsearch -u https://target.com -e php -r --recursion-depth 2

# Recursively scan only if HTTP status is 200, 301, or 302
dirsearch -u https://target.com -e php -r --recursion-status 200,301,302
```
### 集成网络爬虫
```bash
# Crawl the target website for internal URLs and feed them into the scan queue
dirsearch -u https://target.com -e php --crawl
```
---

## ⚡ 线程、速率限制和延迟

控制扫描攻击性以防止触发速率限制或 Web 应用程序防火墙 (WAF)：
```bash
# Set number of concurrent threads (default: 25)
dirsearch -u https://target.com -e php -t 50

# Add a delay between requests (e.g. 0.5 seconds)
dirsearch -u https://target.com -e php -s 0.5

# Enforce a maximum request rate per second (e.g. max 15 req/sec)
dirsearch -u https://target.com -e php --max-rate 15

# Customize HTTP connection timeouts and retries
dirsearch -u https://target.com -e php --timeout 8 --retries 3

# Exit scan immediately on excessive connection errors
dirsearch -u https://target.com -e php --exit-on-error
```
---

## 🎯 过滤和匹配响应

### 状态代码包含和排除
```bash
# Include only specific status codes (e.g., ignore everything except 200, 301, 403)
dirsearch -u https://target.com -e php -i 200,301,302,403

# Exclude specific status codes (e.g., exclude 404 and 500)
dirsearch -u https://target.com -e php -x 404,400,500,502,503
```
### 按大小、字数和内容过滤
```bash
# Exclude responses by specific byte size (e.g. ignore custom 404 pages)
dirsearch -u https://target.com -e php --exclude-sizes 0B,1542B,3421B

# Exclude responses matching specific text on the page
dirsearch -u https://target.com -e php --exclude-texts "Not Found","Access Denied"

# Exclude responses matching a regular expression
dirsearch -u https://target.com -e php --exclude-regex "^Error:.*"
```
---

## 🛠️ HTTP 请求自定义

### 自定义标头和用户代理
```bash
# Set a custom header (e.g., authorization token or internal bypass header)
dirsearch -u https://target.com -e php -H "X-Forwarded-For: 127.0.0.1" -H "X-Requested-With: XMLHttpRequest"

# Load headers from a file
dirsearch -u https://target.com -e php --header-list custom_headers.txt

# Use a randomized User-Agent per request to bypass basic WAFs
dirsearch -u https://target.com -e php --random-agent

# Set a custom User-Agent
dirsearch -u https://target.com -e php --user-agent "Mozilla/5.0 (Windows NT 10.0; Win64; x64)"
```
### Cookie 和身份验证
```bash
# Provide session cookies for authenticated scanning
dirsearch -u https://target.com -e php -b "PHPSESSID=abc123xyz; admin_logged=true"

# HTTP Basic Authentication (username:password)
dirsearch -u https://target.com -e php --auth admin:SecretPass123 --auth-type basic

# NTLM Authentication for corporate Windows portals
dirsearch -u https://target.com -e php --auth CORP\\alice:Welcome2026 --auth-type ntlm
```
### POST 数据和请求方法
```bash
# Perform fuzzing using HTTP POST requests
dirsearch -u https://target.com/api -m POST -d "action=test&token=xyz" -e json

# Replay an entire raw HTTP request saved from Burp Suite
dirsearch -u https://target.com --raw request.txt -e php
```
---

## 🛡️ 代理、Tor 和规避
```bash
# Route all traffic through an HTTP or SOCKS5 proxy (e.g., Burp Suite)
dirsearch -u https://target.com -e php --proxy http://127.0.0.1:8080

# Route all traffic through the local Tor SOCKS proxy
dirsearch -u https://target.com -e php --tor

# Skip SSL/TLS certificate verification
dirsearch -u https://target.com -e php --no-check-certificate
```
---

## 📄 输出格式和报告
```bash
# Save output as a clean text file
dirsearch -u https://target.com -e php -o results.txt

# Export results to JSON format
dirsearch -u https://target.com -e php -o results.json --format json

# Export results to HTML, CSV, or Markdown
dirsearch -u https://target.com -e php -o report.html --format html
dirsearch -u https://target.com -e php -o results.csv --format csv
dirsearch -u https://target.com -e php -o report.md --format markdown
```
---

## 🎯 真实世界的渗透测试和错误赏金食谱

### 1. 快速全方位网络侦察
```bash
# Scan with common web extensions, follow redirects, random UA, 40 threads
dirsearch -u https://target.com \
  -e php,html,js,json,txt,zip \
  -t 40 \
  --random-agent \
  -i 200,301,302,403 \
  -o target_recon.txt
```
### 2. 寻找被遗忘的备份和源代码泄漏
```bash
# Test for database dumps, git repos, env files, and zip archives
dirsearch -u https://target.com \
  -e bak,old,orig,zip,tar.gz,sql,env,git \
  -w /usr/share/seclists/Discovery/Web-Content/raft-medium-files.txt \
  -i 200 \
  --exclude-sizes 0B
```
### 3.深度递归目录发现
```bash
# 2-level recursion, crawling pages for new paths, ignoring error status codes
dirsearch -u https://target.com \
  -e php,html \
  -r --recursion-depth 2 \
  --crawl \
  -x 404,500,502 \
  -o deep_scan.json --format json
```
### 4. 经过身份验证的内部门户评估
```bash
# Scan through Burp Suite with internal session cookies and WAF bypass headers
dirsearch -u https://intranet.corp.local \
  -e aspx,ashx,config,txt \
  -b "ASP.NET_SessionId=xyz123; AuthToken=ey..." \
  -H "X-Forwarded-For: 127.0.0.1" \
  --proxy http://127.0.0.1:8080
```
---

## ⚡ 快速参考表

|任务|命令 |
|------|---------|
| **带有扩展的基本扫描** | `dirsearch -u https://target.com -e php,html,txt` |
| **所有扩展名通配符** | `dirsearch -u https://target.com -e *` |
| **自定义单词表** | `dirsearch -u https://target.com -e php -w /path/to/wordlist.txt` |
| **扫描多个目标** | `dirsearch -l targets.txt -e php,aspx` |
| **递归发现** | `dirsearch -u https://target.com -e php -r --recursion-depth 2` |
| **抓取目标链接** | `dirsearch -u https://target.com -e php --crawl` |
| **包括状态代码** | `dirsearch -u https://target.com -e php -i 200,301,302,403` |
| **排除状态代码** | `dirsearch -u https://target.com -e php -x 404,500,502` |
| **排除误报尺寸**| `dirsearch -u https://target.com -e php --exclude-sizes 0B,1240B` |
| **使用 cookie 进行身份验证** | `dirsearch -u https://target.com -e php -b "session=abc123"` |
| **通过 Burp Suite 的路线** | `dirsearch -u https://target.com -e php --proxy http://127.0.0.1:8080`|
| **将报告保存为 JSON** | `dirsearch -u https://target.com -e php -o report.json --format json`|

---

## 💡 提示和最佳实践

1. **在大型单词列表上谨慎使用 `-e *`：** 通配符扩展扩展将每个单词乘以 20 多个扩展。仅将 `-e *` 用于较小的列表，例如 `common.txt` 或 `dicc.txt`。
2. **过滤掉误报大小：** 现代 Web 框架使用 HTTP 200 返回自定义 404 页面。检查默认响应的大小并添加 `--exclude-sizes <size>` 以消除噪音。
3. **始终检查 403 禁止路径：** 在现代云和企业环境中，HTTP 403 响应表明目录或端点存在。记录所有 403 以进行标头旁路和路径遍历测试。
4. **利用前缀和后缀进行备份：** 开发人员经常保存敏感文件的备份副本（例如 `config.php.bak`、`old_database.sql`）。使用 `--prefixes` 和 `--suffixes` 自动发现它们。

---

## ⚠️ 法律免责声明

> **重要：** 在没有明确书面许可的情况下对服务器进行未经授权的端口扫描、目录暴力破解和 Web 枚举是非法的。严格在合法的漏洞赏金活动、授权渗透测试和 CTF 实验室环境中使用 `dirsearch`。