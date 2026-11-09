<!-- language-home-navigation -->
[🏠 中文主页](../../README.zh-CN.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🎯 Wfuzz - 完整备忘单

[🇹🇷 Türkçe Sürüm]（翻译/README.tr.md）
```
  ██╗    ██╗███████╗██╗   ██╗███████╗███████╗
  ██║    ██║██╔════╝██║   ██║╚══███╔╝╚══███╔╝
  ██║ █╗ ██║█████╗  ██║   ██║  ███╔╝   ███╔╝ 
  ██║███╗██║██╔══╝  ██║   ██║ ███╔╝   ███╔╝  
  ╚███╔███╔╝██║     ╚██████╔╝███████╗███████╗
   ╚══╝╚══╝ ╚═╝      ╚═════╝ ╚══════╝╚══════╝
       The Flexible Web Application Fuzzer & Parameter Explorer
```
<p对齐=“中心”>
  <img src="https://img.shields.io/badge/Wfuzz-Web_Fuzzer-blue?style=for-the-badge" alt="Wfuzz">
  <img src="https://img.shields.io/badge/Language-Python-orange?style=for-the-badge" alt="Python 语言">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-green?style=for-the-badge" alt="网络安全和 API">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="许可证 GPLv2">
</p>

<p对齐=“中心”>
  <b>🌐 高度可定制的 HTTP 模糊器，旨在发现隐藏参数、绕过、标头和多负载矩阵</b>
</p>

---

## 📋 目录

- [概述和关键字架构](#-overview--the-keyword-architecture)
- [Wfuzz 与 ffuf 比较](#-wfuzz-vs-ffuf-comparison)
- [安装](#-安装)
- [基本目录和文件发现](#-basic-directory--file-discovery)
- [过滤和隐藏噪声（`--hc`、`--hl`、`--hw`、`--hh`）](#-filtering--hiding-noise---hc---hl---hw---hh)
- [有效负载类型和动态编码器 (`-z`)](#-有效负载类型--动态编码器--z)
- [参数和隐藏变量模糊测试](#-parameter--hidden-variable-fuzzing)
- [标头、Cookie 和身份验证](#-headers-cookies--authentication)
- [多变量模糊测试（`FUZZ` 和 `FUZ2Z`）](#-multi-variable-fuzzing-fuzz-and-fuz2z)
- [通过 Burp Suite 进行路由 (`-p`)](#-routing-through-burp-suite--p)
- [快速参考表](#-quick-reference-table)
- [防御考虑因素和 OPSEC](#-defective-considerations--opsec)
- [法律免责声明](#-legal-disclaimer)

---

## 🎯 概述和关键字架构

**Wfuzz** 由 **Xavi Mendez** 用 Python 编写，是一款经过实战检验的经典工具，用于评估 Web 应用程序。它将 URL、参数、标头和 POST 正文中的指定占位符（默认值：`FUZZ`）替换为字典条目或算法生成的有效负载。

Wfuzz 因其无与伦比的**有效负载多功能性**（范围、IP 生成器、正则表达式字符串、MD5 或 Base64 等动态编码器）和模块化过滤机制而脱颖而出。

---

## ⚖️ Wfuzz 与 ffuf 比较

|特色 |乌法兹 |噗噗|
|--------|--------|------|
| **实现语言** |蟒蛇 |去 |
| **原始速度** | ⭐⭐⭐（好）| ⭐⭐⭐⭐⭐（速度极快）|
| **动态有效负载编码器**| ✅ 原生 (`-z file,pass.txt,md5`)| ❌ 需要外部脚本 |
| **多有效负载组合**| ✅ 原生（`FUZZ`、`FUZ2Z`、`FUZ3Z`）| ✅ 支持（`-w w1:W1 -w w2:W2`）|
| **内置有效负载生成器**| ✅ 范围、IP、十六进制、字典| ❌ 仅文件单词列表 |
| **资源使用** |中等 RAM/CPU |低内存占用 |

---

## 📥 安装
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y wfuzz

# Python Pip
pip3 install wfuzz

# Arch Linux
sudo pacman -S wfuzz
```
---

## 🚀 基本目录和文件发现
```bash
# Basic directory fuzzing
wfuzz -w /usr/share/wordlists/dirb/common.txt http://target.local/FUZZ

# Fuzzing file extensions
wfuzz -w extensions.txt http://target.local/index.FUZZ

# Fuzzing hidden backup files
wfuzz -w words.txt http://target.local/FUZZ.bak
```
---

## 🎯 过滤和隐藏噪声（`--hc`、`--hl`、`--hw`、`--hh`）

现实世界的服务器甚至会返回不存在的页面（自定义 404 页面或重定向）的响应。 Wfuzz 提供细粒度的过滤标志：
```bash
# Hide specific HTTP status codes (e.g. 404, 403)
wfuzz -w wordlist.txt --hc 404,403 http://target.local/FUZZ

# Hide responses by line count (filters out static custom 404 pages)
wfuzz -w wordlist.txt --hl 42 http://target.local/FUZZ

# Hide responses by word count
wfuzz -w wordlist.txt --hw 120 http://target.local/FUZZ

# Hide responses by character count / body length
wfuzz -w wordlist.txt --hh 0,1052 http://target.local/FUZZ

# Show ONLY HTTP 200 and 302 responses
wfuzz -w wordlist.txt --sc 200,302 http://target.local/FUZZ
```
---

## 📦 有效负载类型和动态编码器 (`-z`)

Wfuzz 提供内置生成器，无需预先生成文本文件：
```bash
# Numeric Range Fuzzing (e.g., IDOR testing from 1 to 500)
wfuzz -z range,1-500 http://target.local/user.php?id=FUZZ

# Custom Inline List
wfuzz -z list,admin-root-test-dev http://target.local/FUZZ/

# On-the-fly MD5 encoding of wordlist entries
wfuzz -z file,passwords.txt,md5 http://target.local/login?hash=FUZZ

# On-the-fly Base64 encoding
wfuzz -z file,usernames.txt,base64 http://target.local/auth?token=FUZZ
```
---

## 🔍 参数和隐藏变量模糊测试

发现未链接的 GET/POST 参数：
```bash
# GET parameter discovery
wfuzz -w /usr/share/seclists/Discovery/Web-Content/burp-parameter-names.txt \
  --hc 404 --hh 2340 "http://target.local/search.php?FUZZ=test"

# POST body parameter discovery
wfuzz -w parameters.txt -d "FUZZ=admin" --hc 404 http://target.local/api/update
```
---

## 🍪 标头、Cookie 和身份验证
```bash
# Cookie Fuzzing (Session hijacking / privilege testing)
wfuzz -w wordlist.txt -b "session=FUZZ" http://target.local/dashboard

# HTTP Header Fuzzing (IP bypasses via X-Forwarded-For)
wfuzz -z file,ips.txt -H "X-Forwarded-For: FUZZ" http://target.local/admin

# Basic Authentication brute force
wfuzz -z file,users.txt -z file,passwords.txt --basic "FUZZ:FUZ2Z" http://target.local/protected/
```
---

## ⚡ 多变量模糊测试（`FUZZ` 和 `FUZ2Z`）

使用不同的占位符标签组合多个单词列表：
```bash
# Test username and password matrix simultaneously
wfuzz -w users.txt -w passwords.txt -d "user=FUZZ&pass=FUZ2Z" --hc 200 http://target.local/login.php

# Fuzz path and extension together
wfuzz -w words.txt -w extensions.txt http://target.local/FUZZ.FUZ2Z
```
---

## 🔁 通过 Burp Suite 进行路由 (`-p`)

检查 Burp Suite 内的有效负载和响应：
```bash
# Route requests through local Burp Suite proxy
wfuzz -p 127.0.0.1:8080:HTTP -w wordlist.txt http://target.local/FUZZ
```
---

## ⚡ 快速参考表

|目标|命令 |
|------------|---------|
| **目录模糊测试** | `wfuzz -w <DICT> <URL>/FUZZ` |
| **隐藏 HTTP 状态代码** | `wfuzz -w <DICT> --hc 404,403 <URL>/FUZZ` |
| **按响应长度隐藏** | `wfuzz -w <DICT> --hh <LENGTH> <URL>/FUZZ` |
| **模糊 GET 参数** | `wfuzz -w <PARAMS> "<URL>?FUZZ=test"` |
| **模糊后正文** | `wfuzz -w <PARAMS> -d "FUZZ=test" <URL>` |
| **数字范围有效负载** | `wfuzz -z range,1-100 "<URL>?id=FUZZ"` |
| **动态编码有效负载**| `wfuzz -z file,<DICT>,base64 "<URL>?token=FUZZ"` |
| **多关键字模糊测试** | `wfuzz -w <W1> -w <W2> -d "u=FUZZ&p=FUZ2Z" <URL>` |
| **通过 Burp 代理进行路由** | `wfuzz -p 127.0.0.1:8080:HTTP -w <DICT> <URL>/FUZZ` |

---

## 🛡️ 防御考虑和 OPSEC

1. **WAF 阻止：** 每秒模糊测试数百个请求会触发 Cloudflare、AWS WAF 和 ModSecurity 速率限制。使用 `-s <seconds>` 来限制请求速度。
2. **资源耗尽：** 对未索引的数据库查询进行大量模糊测试可能会导致旧版 Web 服务器出现拒绝服务 (DoS) 情况。

---

## ⚠️ 法律免责声明

> [!警告]
> 本备忘单仅用于授权的渗透测试和漏洞审核。严格禁止对未经授权的应用程序进行模糊测试。