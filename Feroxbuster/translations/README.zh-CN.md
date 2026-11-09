<!-- language-home-navigation -->
[🏠 中文主页](../../README.zh-CN.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🦀 Feroxbuster - 完整备忘单

[🇹🇷 Türkçe Sürüm]（翻译/README.tr.md）
```
  ███████╗███████╗██████╗  ██████╗ ██╗  ██╗██████╗ ██╗   ██╗███████╗████████╗███████╗██████╗ 
  ██╔════╝██╔════╝██╔══██╗██╔═══██╗╚██╗██╔╝██╔══██╗██║   ██║██╔════╝╚══██╔══╝██╔════╝██╔══██╗
  █████╗  █████╗  ██████╔╝██║   ██║ ╚███╔╝ ██████╔╝██║   ██║███████╗   ██║   █████╗  ██████╔╝
  ██╔══╝  ██╔══╝  ██╔══██╗██║   ██║ ██╔██╗ ██╔══██╗██║   ██║╚════██║   ██║   ██╔══╝  ██╔══██╗
  ██║     ███████╗██║  ██║╚██████╔╝██╔╝ ██╗██████╔╝╚██████╔╝███████║   ██║   ███████╗██║  ██║
  ╚═╝     ╚══════╝╚═╝  ╚═╝ ╚═════╝ ╚═╝  ╚═╝╚═════╝  ╚═════╝ ╚══════╝   ╚═╝   ╚══════╝╚═╝  ╚═╝
                Fast, Simple, Recursive Content Discovery
```
<p对齐=“中心”>
  <img src="https://img.shields.io/badge/Feroxbuster-Content_Discovery-orange?style=for-the-badge" alt="Feroxbuster">
  <img src="https://img.shields.io/badge/Rust-Blazingly_Fast-blue?style=for-the-badge" alt="Rust">
  <img src="https://img.shields.io/badge/Auto_Tune-Recursive-green?style=for-the-badge" alt="Recursive">
  <img src="https://img.shields.io/badge/Bug_Bounty-Web_Pentest-red?style=for-the-badge" alt="Web Pentest">
</p>

<p对齐=“中心”>
  <b>⚡ 用 Rust 编写的快速、简单、递归内容发现工具</b>
</p>

---

## 📋 目录

- [简介](#-简介)
- [主要功能](#-关键功能)
- [工具比较](#-tool-comparison)
- [安装](#-安装)
- [基本语法和快速入门](#-basic-syntax--quick-start)
- [核心命令选项](#-core-command-options)
  - [递归控制](#recursion-control)
  - [并发与性能](#concurrency--性能)
  - [文件扩展名](#file-extensions)
  - [过滤与匹配](#filtering--matching)
  - [智能功能（自动调整、链接提取、备份）](#smart-features)
  - [标头、Cookie 和身份验证](#headers-cookies--auth)
  - [代理和 Burp Suite 集成](#proxy--burp-suite-integration)
- [多目标扫描和管道](#-multi-target-scanning--pipelines)
- [真实世界工作流程](#-real-world-workflows)
- [快速参考表](#-quick-reference-table)
- [提示和最佳实践](#-tips--best-practices)
- [法律免责声明](#-legal-disclaimer)
- [资源](#-资源)

---

## 🎯 简介

**Feroxbuster** 是一款用 Rust 编写的高性能、多线程、递归网页内容扫描器。它被设计为快速、简单且直观，将基于 Go/Rust 的模糊器的速度与本机自动递归目录发现相结合。

它在渗透测试和错误赏金活动中大放异彩，必须快速发现深层网站树和隐藏目录，而无需生成复杂的手动脚本。

---

## 🌟 主要特点

- ⚡ **极快：** 由异步 Rust（`tokio` 和 `reqwest`）提供支持，每秒能够处理数千个请求。
- 🔄 **本机递归发现：** 自动发现新目录并递归扫描它们直至可配置的深度限制。
- 🧠 **自动调整和自动保释：** 如果目标 Web 服务器速度减慢或发生过多连接错误则自动中止扫描速度。
- 🔗 **内联链接提取：** 解析 HTML 和 JavaScript 响应以发现隐藏端点并将其反馈到扫描队列中。
- 📦 **自动备份搜索：** 将 `.bak`、`~`、`.old` 和 `.orig` 变体自动附加到发现的文件中。
- 🎯 **可管道输入：** 无缝接受来自 `stdin` 的目标列表（来自 `subfinder`、`httpx` 或 `gau` 等工具）。

---

## ⚖️ 工具比较

|特色 |铁火克星 |噗噗|搜索 |戈巴斯特 |德布 |
|--------|-------------|------|------------|----------|------|
| **语言** |铁锈|去 |蟒蛇 |去 | C |
| **速度** | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐ |
| **默认递归** | ✅ 自动 | ❌ 手动旗 | ✅ 自动 (`-r`) | ❌ 无 | ✅ 自动 |
| **链接提取** | ✅ 是 (`--extract-links`) | ❌ 否 | ❌ 否 | ❌ 否 | ❌ 否 |
| **自动速率调整** | ✅ 是 (`--auto-tune`) | ❌ 否 | ❌ 否 | ❌ 否 | ❌ 否 |
| **POST 模糊测试** | ❌ 否 | ✅ 本地人 | ✅ 原生 (`--data`) | ❌ 否 | ❌ 否 |
| **多目标标准输入** | ✅ 本地人 | ❌ 有限| ✅ 是 (`-l`) | ❌ 否 | ❌ 否 |
| **备份文件检测**| ✅ 原生 (`--collect-backups`)| ❌ 否 | ✅ 原生 (`-e *`) | ❌ 否 | ❌ 否 |

---

## 📥 安装

### Kali Linux 和 Debian / Ubuntu
```bash
# Update repository and install
sudo apt update && sudo apt install -y feroxbuster
```
### 使用 Cargo（Rust 包管理器）
```bash
cargo install feroxbuster
```
### macOS（自制）
```bash
brew install feroxbuster
```
### 码头工人
```bash
docker run --init -it epi052/feroxbuster -u https://target.com -w /usr/share/seclists/Discovery/Web-Content/common.txt
```
### 预编译二进制文件（GitHub 版本）
```bash
# Download latest Linux release
curl -sL https://raw.githubusercontent.com/epi052/feroxbuster/main/install-nix.sh | bash
sudo mv feroxbuster /usr/local/bin/
```
---

## 🚀 基本语法和快速入门
```bash
feroxbuster -u <TARGET_URL> -w <WORDLIST> [OPTIONS]
```
### 快速扫描示例
```bash
# Scan a single target with default settings
feroxbuster -u http://10.10.10.10

# Scan with custom SecLists directory list
feroxbuster -u https://example.com -w /usr/share/seclists/Discovery/Web-Content/directory-list-2.3-medium.txt

# Scan target including specific file extensions
feroxbuster -u https://example.com -w /usr/share/wordlists/dirb/common.txt -x php,html,txt,json
```
---

## ⚙️ 核心命令选项

### 递归控制
```bash
# Specify maximum recursion depth (default is 4)
feroxbuster -u https://example.com -w common.txt -d 2

# Completely disable recursion (scan root directory only)
feroxbuster -u https://example.com -w common.txt -n

# Don't scan nested subdirectories automatically (interactive prompt)
feroxbuster -u https://example.com -w common.txt --dont-scan /api,/static
```
### 并发和性能
```bash
# Set number of concurrent threads per directory (default: 50)
feroxbuster -u https://example.com -t 100

# Limit total concurrent directory scans running simultaneously (default: unlimited)
feroxbuster -u https://example.com -L 3

# Rate limit requests per second to avoid triggering WAF/firewall blocks
feroxbuster -u https://example.com --rate-limit 150

# Request timeout in seconds (default: 7)
feroxbuster -u https://example.com -T 5
```
### 文件扩展名
```bash
# Append extensions to each wordlist item
feroxbuster -u https://example.com -x php,asp,aspx,jsp,txt,bak

# Automatically collect and fuzz file extensions found during crawling
feroxbuster -u https://example.com --collect-extensions
```
### 过滤和匹配
```bash
# Match only specific status codes (e.g., 200 OK and 301 Redirects)
feroxbuster -u https://example.com -s 200 301 302

# Filter out specific status codes (e.g., exclude 404 and 403)
feroxbuster -u https://example.com -C 404 403 500

# Filter responses matching exact size in bytes
feroxbuster -u https://example.com -S 4092 1024

# Filter responses matching exact word count
feroxbuster -u https://example.com -W 42

# Filter responses matching exact line count
feroxbuster -u https://example.com -N 12

# Filter responses matching regular expressions (e.g., generic error pages)
feroxbuster -u https://example.com --filter-regex "Under Construction|Page Not Found"

# Filter responses similar to a known 404/wildcard page
feroxbuster -u https://example.com --filter-similar-to https://example.com/definitelynotfoundpage404
```
### 智能功能
```bash
# Auto-tune: Automatically adjusts request concurrency based on server response latency
feroxbuster -u https://example.com --auto-tune

# Auto-bail: Aborts scanning automatically when more than 100 consecutive network errors occur
feroxbuster -u https://example.com --auto-bail

# Extract links: Parses HTML and JavaScript to automatically uncover endpoints
feroxbuster -u https://example.com --extract-links

# Collect backups: Automatically tests for backup copies (.bak, ~, .old) of every discovered file
feroxbuster -u https://example.com --collect-backups
```
### 标头、Cookie 和身份验证
```bash
# Custom HTTP Header
feroxbuster -u https://example.com -H "Authorization: Bearer <TOKEN>" -H "X-Forwarded-For: 127.0.0.1"

# Session Cookie
feroxbuster -u https://example.com -b "session=abc123xyz; role=admin"

# Custom User-Agent string
feroxbuster -u https://example.com -a "Mozilla/5.0 (Windows NT 10.0; Win64; x64)"

# Ignore invalid / self-signed TLS certificates
feroxbuster -u https://10.10.10.10 -k
```
### 代理和 Burp Suite 集成
```bash
# Route all requests through Burp Suite proxy
feroxbuster -u https://example.com -p http://127.0.0.1:8080 -k

# Convenience shortcut flag for Burp Suite (enables -k and sets proxy to localhost:8080)
feroxbuster -u https://example.com --burp
```
---

## 🔁 多目标扫描和管道

Feroxbuster 本机从标准输入 (`stdin`) 读取目标 URL，使其成为与资产发现管道集成的理想选择：
```bash
# Pipeline: Subdomain discovery -> HTTP Probing -> Feroxbuster Fuzzing
subfinder -d example.com -silent | \
  httpx -silent -mc 200,301,302 | \
  feroxbuster --stdin -w /usr/share/seclists/Discovery/Web-Content/raft-medium-directories.txt -t 30 -L 4
```
### 从文件中扫描 URL
```bash
cat targets.txt | feroxbuster --stdin -w common.txt -v
```
---

## 🎬 现实世界的工作流程

### 工作流程 1：全面的 Bug 赏金网络侦察

高并发、自动调速、链接提取、备份检测：
```bash
feroxbuster -u https://target.com \
  -w /usr/share/seclists/Discovery/Web-Content/directory-list-2.3-medium.txt \
  -x php,js,json,txt,html \
  --extract-links \
  --collect-backups \
  --auto-tune \
  -o ferox_target_scan.txt
```
### 工作流程 2：内部企业/Active Directory 门户发现

通过 Burp Suite 使用身份验证和自签名证书扫描内部 Web 应用程序：
```bash
feroxbuster -u https://internal-portal.corp.local \
  -w /usr/share/seclists/Discovery/Web-Content/common.txt \
  -x aspx,ashx,asmx,config,txt \
  -b "ASP.NET_SessionId=k2m9fl3p8..." \
  --burp \
  -d 3
```
### 工作流程 3：快速非递归 API 和端点发现
```bash
feroxbuster -u https://api.target.com/v1 \
  -w /usr/share/seclists/Discovery/Web-Content/api/api-endpoints.txt \
  -n \
  -s 200,201,204,401,403 \
  -t 100 \
  --json \
  -o api_results.json
```
---

## 📊 快速参考表

|旗帜|短|描述 |示例|
|------|--------|-------------|---------|
| `--url` | `-u` |要扫描的目标 URL | `feroxbuster -u https://target.com` |
| `--wordlist` | `-w` |单词列表路径 | `feroxbuster -w /path/to/list.txt` |
| `--threads` | `-t` |每个目录的并发线程数 | `feroxbuster -t 80` |
| `--scan-limit` | `-L`|限制同时目录扫描总数 | `feroxbuster -L 3`|
| `--depth` | `-d` |最大递归深度（默认 4） | `feroxbuster -d 2` |
| `--no-recursion`| `-n`|禁用递归目录扫描 | `feroxbuster -n`|
| `--extensions`| `-x` |要附加的文件扩展名 | `feroxbuster -x php,html,bak`|
| `--status-codes` | `-s` |要包含的状态代码 | `feroxbuster -s 200 301 302` |
| `--filter-status` | `-C`|要排除的状态代码 | `feroxbuster -C 404 500` |
| `--filter-size` | `-S` |要排除的响应大小 | `feroxbuster -S 4096` |
| `--filter-words`| `-W`|要排除的字数 | `feroxbuster -W 15`|
| `--filter-lines` | `-N` |要排除的行数 | `feroxbuster -N 8`|
| `--auto-tune` |无 |根据延迟动态调整速度 | `feroxbuster --auto-tune` |
| `--auto-bail` |无 |网络错误过多时中止扫描 | `feroxbuster --auto-bail` |
| `--extract-links`|无 |解析 HTML/JS 以获得新端点 | `feroxbuster --extract-links` |
| `--collect-backups` |无 |检查备份变化（`.bak`、`.old`）| `feroxbuster --collect-backups` |
| `--proxy` | `-p` |通过 HTTP 代理发送流量 | `feroxbuster -p http://127.0.0.1:8080` |
| `--burp` |无 | `-p http://127.0.0.1:8080 -k`的快捷方式| `feroxbuster --burp`|
| `--stdin` |无 |从标准输入读取 URL | `cat urls.txt \| feroxbuster --stdin` |
| `--output` | `-o`|输出文件路径 | `feroxbuster -o results.txt` |
| `--json` |无 |将输出格式化为 JSON | `feroxbuster --json -o out.json` |

---

## 💡 提示和最佳实践

1. **将 `--auto-tune` 用于远程目标：**
   - `--auto-tune` 不是猜测线程计数 (`-t`)，而是主动测量目标响应时间并动态限制或加速，避免服务器意外拒绝服务。
2. **组合`--extract-links`和`--collect-backups`：**
   - 这种组合在错误赏金评估中异常致命：Feroxbuster 提取隐藏的 JS 路由，扫描它们，并立即检查服务器上是否留下了开发人员备份（`.bak`、`.swp`、`~`）。
3. **使用 `-L` 限制对大型单词列表的扫描：**
   - 如果使用超过 200,000 个单词的词典，无限制的递归可以启动数十个并发线程。使用 `-L 2` 或 `-L 3` 将内存和带宽保持在安全操作范围内。
4. **使用状态文件恢复大型审核：**
   - Feroxbuster 定期保存状态文件 (`.state`)。如果发生扫描中断，请使用 `feroxbuster --resume-from <FILE>.state` 立即恢复。

---

## ⚠️ 法律免责声明

> **重要提示：** Feroxbuster 严格针对授权安全评估、CTF 竞赛以及经书面许可进行的渗透测试而构建。扫描未经授权的目标可能会使网络基础设施饱和，这是法律严格禁止的。

---

## 📚 资源

- [Feroxbuster GitHub 存储库](https://github.com/epi052/feroxbuster)
- [Feroxbuster 官方文档](https://epi052.github.io/feroxbuster-docs/)
- [SecLists 发现列表](https://github.com/danielmiessler/SecLists)