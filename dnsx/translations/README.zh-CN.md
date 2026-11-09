<!-- language-home-navigation -->
[🏠 中文主页](../../README.zh-CN.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 📡 dnsx - 完整备忘单

[🇹🇷 Türkçe Sürüm]（翻译/README.tr.md）
```
  ██████╗ ███╗   ██╗███████╗██╗  ██╗
  ██╔══██╗████╗  ██║██╔════╝╚██╗██╔╝
  ██║  ██║██╔██╗ ██║███████╗ ╚███╔╝ 
  ██║  ██║██║╚██╗██║╚════██║ ██╔██╗ 
  ██████╔╝██║ ╚████║███████║██╔╝ ██╗
  ╚═════╝ ╚═╝  ╚═══╝╚══════╝╚═╝  ╚═╝
       Fast & Multi-Purpose DNS Toolkit by ProjectDiscovery
```
<p对齐=“中心”>
  <img src="https://img.shields.io/badge/dnsx-DNS_Toolkit-blue?style=for-the-badge" alt="dnsx">
  <img src="https://img.shields.io/badge/Developer-ProjectDiscovery-orange?style=for-the-badge" alt="ProjectDiscovery">
  <img src="https://img.shields.io/badge/Category-Recon_&_Scanning-green?style=for-the-badge" alt="侦察与扫描">
  <img src="https://img.shields.io/badge/License-MIT-red?style=for-the-badge" alt="许可证 MIT">
</p>

<p对齐=“中心”>
  <b>🌐高性能DNS解析、通配符过滤、反向DNS查找和多查询提取引擎</b>
</p>

---

## 📋 目录

- [概述与架构](#-overview--architecture)
- [为什么是 dnsx？ （通配符处理和速度）]（#-why-dnsx-wildcard-handling--speed）
- [安装](#-安装)
- [基本子域探测和解析](#-basic-subdomain-probing--resolution)
- [通配符 DNS 过滤 (`-wd`)](#-wildcard-dns-filtering---wd)
- [查询特定 DNS 记录类型](#-querying-specific-dns-record-types)
- [反向 DNS 查找 (`-ptr`)](#-reverse-dns-lookups---ptr)
- [使用自定义解析器和速率限制](#-using-custom-resolvers--rate-limiting)
- [管道自动化（Subfinder + dnsx + httpx）]（#-pipeline-automation-subfinder--dnsx--httpx）
- [快速参考表](#-quick-reference-table)
- [防御考虑因素和 OPSEC](#-defective-considerations--opsec)
- [法律免责声明](#-legal-disclaimer)

---

## 🎯 概述与架构

**dnsx** 由 **ProjectDiscovery** 在 Go 中设计，是一个快速且多用途的 DNS 工具包，专为侦察、错误赏金狩猎和网络发现而设计。

`dig`、`nslookup` 和 `host` 等工具可以一一解析记录，而 `dnsx` 可以异步处理数千个查询，自动重试失败的解析器，提取干净的响应 IP，并消除污染子域枚举的通配符噪音。

---

## ⚡ 为什么是 dnsx？ （通配符处理和速度）

在错误赏金侦察期间，被动工具（`subfinder`、`amass`）通常会返回数千个死亡或通配符子域。在死 DNS 条目上测试 HTTP 端点会浪费大量带宽。

`dnsx` 通过以下方式解决了这个问题：
1. **过滤通配符 (`-wd`)：** 检测 `*.domain.com` 是否解析为包罗万象的 IP 并过滤掉幻像子域。
2. **速度和并发性：** 每分钟在公共和私有解析器池中解析 10,000 多个域名。
3. **清洁输出管道：** 直接馈入 `httpx` 或 `nmap`。

---

## 📥 安装
```bash
# Go Install (Recommended)
go install -v github.com/projectdiscovery/dnsx/cmd/dnsx@latest

# Kali Linux & Debian
sudo apt update && sudo apt install -y dnsx

# MacOS (Homebrew)
brew install dnsx

# Verify installation
dnsx -version
```
---

## 🚀 基本子域探测和解析
```bash
# Resolve a single domain
echo "target.com" | dnsx

# Resolve a list of subdomains from file
dnsx -l subdomains.txt

# Display domain and its corresponding A record IP
dnsx -l subdomains.txt -a -resp

# Output only resolved IP addresses (ready for Nmap)
dnsx -l subdomains.txt -resp-only
```
---

## 🚫 通配符 DNS 过滤 (`-wd`)

通配符会为不存在的子域 (`random123xyz.target.com`) 返回有效的 IP，从而污染侦察数据集。
```bash
# Filter wildcard subdomains and print only genuine hosts
dnsx -l subdomains.txt -wd target.com -o valid_hosts.txt

# Filter wildcards with custom threshold
dnsx -l subdomains.txt -wd target.com -wt 3
```
---

## 📋 查询特定 DNS 记录类型

查询数千个域中的特定记录类型：
```bash
# Query CNAME records (Crucial for Subdomain Takeover detection)
dnsx -l subdomains.txt -cname -resp

# Query Mail Exchanger (MX) records
dnsx -l domains.txt -mx -resp

# Query TXT records (SPF, DKIM, verification tokens)
dnsx -l domains.txt -txt -resp

# Query All standard records (A, AAAA, CNAME, NS, TXT, MX, SOA)
dnsx -l domains.txt -recon
```
---

## 🔄 反向 DNS 查找 (`-ptr`)

通过在 IP 范围上执行 PTR 查找来发现活动基础架构和虚拟主机：
```bash
# Reverse resolve a CIDR IP range to hostnames
dnsx -ptr -l ip_ranges.txt -resp

# Reverse resolve a single subnet
echo "192.168.1.0/24" | dnsx -ptr -resp
```
---

## ⚙️ 使用自定义解析器和速率限制

通过利用高速策划的解析器池，避免受到 ISP 或 Google DNS 的速率限制：
```bash
# Use a custom trusted resolver list
dnsx -l subdomains.txt -r resolvers.txt

# Set thread concurrency and query rate limit (queries per second)
dnsx -l subdomains.txt -t 100 -rl 300
```
---

## ⛓️ 管道自动化（Subfinder + dnsx + httpx）

漏洞赏金猎人和渗透测试人员的终极侦察工作流程：
```bash
# Discover subdomains -> Validate via dnsx -> Probe live HTTP web services
subfinder -d target.com -silent | \
  dnsx -silent -wd target.com | \
  httpx -silent -status-code -title -tech-detect
```
---

## ⚡ 快速参考表

|目标|命令 |
|------------|---------|
| **解析子域** | `dnsx -l subdomains.txt` |
| **仅提取 IP 地址** | `dnsx -l subdomains.txt -resp-only` |
| **过滤通配符域** | `dnsx -l subdomains.txt -wd <DOMAIN>` |
| **提取 CNAME（接管）** | `dnsx -l subdomains.txt -cname -resp` |
| **查询所有记录（Recon）** | `dnsx -l domains.txt -recon` |
| **反向 DNS 查找 (PTR)** | `dnsx -l ips.txt -ptr -resp` |
| **自定义解析器和线程**| `dnsx -l list.txt -r resolvers.txt -t 150` |
| **JSON 输出** | `dnsx -l list.txt -json -o results.json` |

---

## 🛡️ 防御考虑和 OPSEC

1. **DNS 泛洪：** 通过企业内部 DNS 服务器发出数以万计的 DNS 查询将导致 CPU 负载激增并触发 DNS 隧道/DDoS 的 SIEM 警报。
2. **解析器中毒：** 始终使用经过验证、可信的解析器列表，以防止恶意解析器拦截查询。

---

## ⚠️ 法律免责声明

> [!警告]
> 此备忘单仅适用于授权的渗透测试和错误赏金活动。严格禁止针对未经授权的基础设施进行大量 DNS 探测。