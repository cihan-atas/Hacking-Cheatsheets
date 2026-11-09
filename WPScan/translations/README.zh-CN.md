<!-- language-home-navigation -->
[🏠 中文主页](../../README.zh-CN.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🛡️ WPScan - 完整备忘单

[🇹🇷 Türkçe Sürüm]（翻译/README.tr.md）
```
  ██╗    ██╗██████╗ ███████╗ ██████╗ █████╗ ███╗   ██╗
  ██║    ██║██╔══██╗██╔════╝██╔════╝██╔══██╗████╗  ██║
  ██║ █╗ ██║██████╔╝███████╗██║     ███████║██╔██╗ ██║
  ██║███╗██║██╔═══╝ ╚════██║██║     ██╔══██║██║╚██╗██║
  ╚███╔███╔╝██║     ███████║╚██████╗██║  ██║██║ ╚████║
   ╚══╝╚══╝ ╚═╝     ╚══════╝ ╚═════╝╚═╝  ╚═╝╚═╝  ╚═══╝
       The WordPress Security & Vulnerability Scanner
```
<p对齐=“中心”>
  <img src="https://img.shields.io/badge/WPScan-WordPress_Security-blue?style=for-the-badge" alt="WPScan">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-orange?style=for-the-badge" alt="网络安全">
  <img src="https://img.shields.io/badge/Database-WPVDB_API-green?style=for-the-badge" alt="WPVDB API">
  <img src="https://img.shields.io/badge/License-GPLv3-red?style=for-the-badge" alt="许可证 GPLv3">
</p>

<p对齐=“中心”>
  <b>🌐黑盒漏洞扫描器旨在检测 WordPress 核心、插件、主题和用户漏洞</b>
</p>

---

## 📋 目录

- [概述与架构](#-overview--architecture)
- [安装](#-安装)
- [API 令牌集成 (WPVDB)](#-api-token-integration-wpvdb)
- [基本扫描和目标枚举](#-basic-scan--target-enumeration)
- [枚举标志细分 (`-e`)](#-enumeration-flags-breakdown--e)
  - [易受攻击的插件 (`-e vp`)](#vulnerable-plugins--e-vp)
  - [所有插件 (`-e ap`)](#all-plugins--e-ap)
  - [主题（`-e vt`、`-e at`）]（#主题--e-vt--e-at）
  - [用户名 (`-e u`)](#用户名--e-u)
  - [敏感备份（`-e cb`、`-e dbe`）](#sensitive-backups--e-cb--e-dbe)
- [检测模式(`--plugins-detection`)](#-检测模式---插件检测)
- [密码暴力破解（`wp-login.php` 和 XML-RPC）](#-password-brute-forcing-wp-loginphp--xml-rpc)
- [隐身和 WAF 规避](#-stealth--waf-evasion)
- [快速参考表](#-quick-reference-table)
- [防御考虑和强化](#-defective-considerations--hardening)
- [法律免责声明](#-legal-disclaimer)

---

## 🎯 概述与架构

WordPress 为前 1000 万个网站中超过 40% 的网站提供支持，因此它是世界上最有针对性的内容管理系统 (CMS)。

**WPScan** 用 Ruby 编写，是用于 WordPress 安装的行业标准黑盒漏洞扫描器。它对活动核心版本进行编目，枚举已安装的插件和主题，提取注册的管理用户名，并将它们与包含数千个已知 CVE 的 **WPScan 漏洞数据库 (WPVDB)** 关联起来。

---

## 📥 安装
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y wpscan

# Arch Linux
sudo pacman -S wpscan

# Ruby Gem (Any platform)
gem install wpscan

# Docker Container
docker run -it --rm wpscan/wpscan --url https://target.local
```
---

## 🔑 API 令牌集成 (WPVDB)

要显示已知漏洞和 CVE 分数，WPScan 需要来自 [wpscan.com](https://wpscan.com/) 的免费 API 令牌：
```bash
# Provide API token via CLI
wpscan --url https://target.local --api-token YOUR_API_TOKEN

# Or export to environment variable
export WPSCAN_API_TOKEN="YOUR_API_TOKEN"
```
---

## 🚀 基本扫描和目标枚举
```bash
# Quick non-intrusive scan
wpscan --url https://target.local

# Update the local database before scanning
wpscan --update
```
---

## 🔍 枚举标志细分 (`-e`)

WPScan 允许使用 `--enumerate`（或 `-e`）标志进行模块化目标枚举：

### 易受攻击的插件 (`-e vp`)
扫描 WPVDB 数据库中存在已知安全漏洞的插件：
```bash
wpscan --url https://target.local -e vp --api-token YOUR_TOKEN
```
### 所有插件 (`-e ap`)
通过字典暴力探测数千个已知插件：
```bash
wpscan --url https://target.local -e ap --plugins-detection aggressive
```
### 主题（`-e vt`、`-e at`）
```bash
# Enumerate vulnerable themes
wpscan --url https://target.local -e vt

# Enumerate all installed themes
wpscan --url https://target.local -e at
```
### 用户名 (`-e u`)
通过 REST API 端点 (`/wp-json/wp/v2/users`) 提取注册作者用户名 (`/wp-json/wp/v2/users`)、作者档案 (`/?author=1`) 和登录错误消息：
```bash
wpscan --url https://target.local -e u1-10
```
### 敏感备份（`-e cb`、`-e dbe`）
搜索暴露的 `wp-config.php.bak`、数据库转储和备份存档：
```bash
wpscan --url https://target.local -e cb,dbe
```
---

## ⚙️ 检测模式 (`--plugins-detection`)

控制 WPScan 搜索插件的彻底程度：
```bash
# Passive (Fast, inspects HTML source, script tags, style links)
wpscan --url https://target.local --plugins-detection passive

# Aggressive (Sends HTTP requests for thousands of plugin directories)
wpscan --url https://target.local --plugins-detection aggressive

# Mixed (Recommended default - passive followed by targeted active requests)
wpscan --url https://target.local --plugins-detection mixed
```
---

## 🔓 密码暴力破解（`wp-login.php` 和 XML-RPC）

枚举有效用户名后，启动目标凭证暴力破解：
```bash
# Brute force login using enumerated username list
wpscan --url https://target.local -U users.txt -P /usr/share/wordlists/rockyou.txt

# Password spray a single password across all users
wpscan --url https://target.local -U users.txt -p 'Winter2026!'

# Multi-threaded password attack
wpscan --url https://target.local -U admin -P passwords.txt -t 20
```
> [!注意]
> 如果在服务器上启用了 `xmlrpc.php`，WPScan 会自动利用 XML-RPC 多重调用方法来测试每个 HTTP 请求的数百个密码组合！

---

## 🥷 隐秘和 WAF 规避
```bash
# Randomize User-Agent header to evade simple bot signatures
wpscan --url https://target.local --random-user-agent

# Throttle request rate (milliseconds between requests) to bypass rate limits
wpscan --url https://target.local --throttle 1000

# Route through SOCKS proxy / Tor
wpscan --url https://target.local --proxy socks5://127.0.0.1:9050

# Ignore SSL/TLS certificate warnings
wpscan --url https://target.local --disable-tls-checks
```
---

## ⚡ 快速参考表

|目标|命令 |
|------------|---------|
| **标准扫描** | `wpscan --url <URL> --api-token <TOKEN>` |
| **枚举易受攻击的插件** | `wpscan --url <URL> -e vp --api-token <TOKEN>` |
| **枚举所有插件** | `wpscan --url <URL> -e ap --plugins-detection aggressive` |
| **枚举用户** | `wpscan --url <URL> -e u` |
| **枚举备份和配置** | `wpscan --url <URL> -e cb,dbe` |
| **密码暴力破解** | `wpscan --url <URL> -U <USERS> -P <PASSWORDS>` |
| **随机用户代理和隐形** | `wpscan --url <URL> --random-user-agent --stealthy` |
| **导出到 JSON 文件** | `wpscan --url <URL> -f json -o wpscan_report.json` |

---

## 🛡️ 防守考虑和强化

1. **禁用 XML-RPC：** 通过 `.htaccess` 或 Nginx 配置阻止对 `xmlrpc.php` 的访问，以抵消高速多呼叫暴力攻击。
2. **限制 REST API 用户端点：** 禁用对 `/wp-json/wp/v2/users` 的公共访问以防止琐碎的作者枚举。
3. **定期修补插件：** 超过 90% 的 WordPress 漏洞存在于第三方插件和主题中，而不是 WordPress 核心。

---

## ⚠️ 法律免责声明

> [!警告]
> 本备忘单仅适用于授权渗透测试、漏洞评估和 Web 应用程序审核。未经明确授权强行破解密码或扫描系统是非法的。