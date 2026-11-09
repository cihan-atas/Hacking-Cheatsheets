<!-- language-home-navigation -->
[🏠 中文主页](../../README.zh-CN.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🪁 Kiterunner - 完整备忘单

[🇹🇷 Türkçe Sürüm]（翻译/README.tr.md）
```
  ██╗  ██╗██╗████████╗███████╗██████╗ ██╗   ██╗███╗   ██╗███╗   ██╗███████╗██████╗ 
  ██║ ██╔╝██║╚══██╔══╝██╔════╝██╔══██╗██║   ██║████╗  ██║████╗  ██║██╔════╝██╔══██╗
  █████╔╝ ██║   ██║   █████╗  ██████╔╝██║   ██║██╔██╗ ██║██╔██╗ ██║█████╗  ██████╔╝
  ██╔═██╗ ██║   ██║   ██╔══╝  ██╔══██╗██║   ██║██║╚██╗██║██║╚██╗██║██╔══╝  ██╔══██╗
  ██║  ██╗██║   ██║   ███████╗██║  ██║╚██████╔╝██║ ╚████║██║ ╚████║███████╗██║  ██║
  ╚═╝  ╚═╝╚═╝   ╚═╝   ╚══════╝╚═╝  ╚═╝ ╚═════╝ ╚═╝  ╚═══╝╚═╝  ╚═══╝╚══════╝╚═╝  ╚═╝
       Context-Aware API Endpoint & Route Discovery Engine by Assetnote
```
<p对齐=“中心”>
  <img src="https://img.shields.io/badge/Kiterunner-API_Discovery-blue?style=for-the-badge" alt="Kiterunner">
  <img src="https://img.shields.io/badge/Author-Assetnote-orange?style=for-the-badge" alt="Assetnote">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-green?style=for-the-badge" alt="网络安全和 API">
  <img src="https://img.shields.io/badge/License-Apache_2.0-red?style=for-the-badge" alt="许可证 Apache 2.0">
</p>

<p对齐=“中心”>
  <b>🌐高性能 REST 和 GraphQL API 路由暴力破解器，旨在使用上下文数据集映射现代 API 架构</b>
</p>

---

## 📋 目录

- [概述和 API 发现问题](#-overview--the-api-discovery-problem)
- [工具比较：Kiterunner vs ffuf vs dirsearch](#-tool-comparison-kiterunner-vs-ffuf-vs-dirsearch)
- [安装](#-安装)
- [下载 Assetnote Kite 数据集](#-downloading-assetnote-kite-datasets)
- [扫描 API (`kr scan`)](#-scanning-apis-kr-scan)
- [标准词表暴力破解 (`kr brute`)](#-standard-wordlist-brute-forcing-kr-brute)
- [标头、Cookie 和身份验证](#-headers-cookies--authentication)
- [过滤和状态代码](#-filtering--status-codes)
- [重播发现的路由（`curl` 输出）](#-replaying-discovered-routes-curl-output)
- [快速参考表](#-quick-reference-table)
- [防御考虑因素和 OPSEC](#-defective-considerations--opsec)
- [法律免责声明](#-legal-disclaimer)

---

## 🎯 概述和 API 发现问题

传统的 Web 内容发现工具（`ffuf`、`dirsearch`、`gobuster`）使用平面单字词典（例如 `admin`、`api`、`login`）。虽然这适用于传统的文件层次结构，但**它无法适应现代 REST 和 GraphQL API**。

现代 API 使用嵌套的多层路由，例如：
`/api/v2/organizations/{org_id}/users/export?format=json`

**Kiterunner** 由 **Assetnote** 创建，通过将现实世界的 OpenAPI、Swagger、Postman 和模式定义编译成紧凑的二进制数据集 (`.kite`) 来解决这个问题。 Kiterunner 不是猜测随机单词，而是使用正确的 HTTP 方法（`GET`、`POST`、`PUT`、`DELETE`）重建整个分层 API 调用结构。

---

## ⚖️ 工具比较：Kiterunner、ffuf、dirsearch

|特色 |风筝行者 (`kr`) |噗噗|搜索 |
|--------|--------------------|------|------------|
| **核心目标焦点** | REST、GraphQL、OpenAPI |通用网络模糊测试 |目录暴力破解 |
| **路由层次意识**| ✅ 本机（`.kite` 文件）| ❌ 扁平词汇表 | ❌ 扁平词汇表 |
| **HTTP 动词交换** | ✅ 测试 GET、POST、PUT、DELETE| ❌ 手动配置 | ❌ 有限|
| **Swagger / OpenAPI 解析** | ✅ 本机模式摄取 | ❌ 否 | ❌ 否 |
| **速度和并发性** | ⭐⭐⭐⭐⭐（Go 引擎）| ⭐⭐⭐⭐⭐（Go 引擎）| ⭐⭐⭐⭐（Python）|

---

## 📥 安装
```bash
# Download precompiled binary from GitHub Releases
wget https://github.com/assetnote/kiterunner/releases/download/v1.0.2/kiterunner_1.0.2_linux_amd64.tar.gz
tar -xzvf kiterunner_1.0.2_linux_amd64.tar.gz
sudo mv kr /usr/local/bin/

# Verify installation
kr version
```
---

## 📦 下载 Assetnote Kite 数据集

Kiterunner 依赖于通过数百万个经过分析的公共 API 创建的预构建 `.kite` 路线数据集：
```bash
# Create wordlists directory
mkdir -p ~/.kiterunner && cd ~/.kiterunner

# Small dataset (~40MB, 2.5 million API routes - Recommended for fast scans)
wget https://wordlists-cdn.assetnote.io/data/kiterunner/routes-small.kite.tar.gz
tar -xzvf routes-small.kite.tar.gz

# Large dataset (~130MB, 8.5 million API routes - Exhaustive testing)
wget https://wordlists-cdn.assetnote.io/data/kiterunner/routes-large.kite.tar.gz
tar -xzvf routes-large.kite.tar.gz
```
---

## 🚀 扫描 API (`kr scan`)

`scan` 命令使用 `.kite` 数据集通过上下文路由层次结构探测目标：
```bash
# Scan a single target using the small dataset
kr scan https://target.local/api -w ~/.kiterunner/routes-small.kite

# Scan multiple hosts from a file with 100 concurrent workers
kr scan targets.txt -w ~/.kiterunner/routes-small.kite -c 100 -j 50

# Scan with maximum verbosity
kr scan https://target.local -w ~/.kiterunner/routes-small.kite -v
```
---

## 🔨 标准单词表暴力破解 (`kr brute`)

如果您想在利用 Kiterunner 的多方法 API 引擎的同时使用标准 `.txt` 单词列表：
```bash
# Brute force standard paths using a text dictionary
kr brute https://target.local -w /usr/share/wordlists/dirb/common.txt

# Brute force with custom API prefixes
kr brute https://target.local/api/v1 -w endpoints.txt
```
---

## 🔑 标头、Cookie 和身份验证

API 经常需要 JWT、承载令牌或自定义组织标头：
```bash
# Pass custom Authorization header
kr scan https://target.local/api -w routes-small.kite -H "Authorization: Bearer eyJhbGci..."

# Pass custom API key and Cookie
kr scan https://target.local -w routes-small.kite -H "X-API-Key: secret123" -H "Cookie: session=xyz"
```
---

## 🎯 过滤和状态代码
```bash
# Exclude standard error codes (404, 403, 502)
kr scan https://target.local -w routes-small.kite -x 404,403,502

# Filter out responses by length / content size
kr scan https://target.local -w routes-small.kite --filter-length 0,324
```
---

## 🔄 重放发现的路由（`curl` 输出）

Kiterunner 可以将发现的路线输出为可操作的 `curl` 命令，从而允许在终端或 Burp Suite 中即时再现：
```bash
# Print reproducible curl command lines for all successful matches
kr scan https://target.local -w routes-small.kite -o json | jq -r '.[] | .curl'
```
---

## ⚡ 快速参考表

|目标|命令 |
|------------|---------|
| **使用小数据集扫描 API** | `kr scan <URL> -w routes-small.kite` |
| **使用大数据集扫描 API** | `kr scan <URL> -w routes-large.kite` |
| **扫描多个主机** | `kr scan hosts.txt -w routes-small.kite -c 100` |
| **暴力文本单词表** | `kr brute <URL> -w wordlist.txt` |
| **使用承载令牌进行身份验证**| `kr scan <URL> -w routes-small.kite -H "Authorization: Bearer <TOKEN>"` |
| **排除状态代码** | `kr scan <URL> -w routes-small.kite -x 404,403` |
| **输出到 JSON 文件** | `kr scan <URL> -w routes-small.kite -o json -oFile results.json` |

---

## 🛡️ 防御考虑和 OPSEC

1. **海量请求量：** `routes-large.kite` 字典产生数百万个请求。确保在测试生产环境时启用速率限制 (`-q 200`)。
2. **状态更改方法：** Kiterunner 发送 `POST`、`PUT` 和 `DELETE` 请求。在未强化的测试 API 上，这可能会无意中修改或删除后端记录。

---

## ⚠️ 法律免责声明

> [!警告]
> Kiterunner 专为授权 API 安全测试和错误赏金活动而设计。对未经授权的 API 进行模糊测试是严格违法的。