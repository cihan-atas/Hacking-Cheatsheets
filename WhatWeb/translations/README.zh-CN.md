<!-- language-home-navigation -->
[🏠 中文主页](../../README.zh-CN.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🌐 WhatWeb - 完整备忘单

[🇹🇷 Türkçe Sürüm]（翻译/README.tr.md）
```
  ██╗    ██╗██╗  ██╗ █████╗ ████████╗██╗    ██╗███████╗██████╗ 
  ██║    ██║██║  ██║██╔══██╗╚══██╔══╝██║    ██║██╔════╝██╔══██╗
  ██║ █╗ ██║███████║███████║   ██║   ██║ █╗ ██║█████╗  ██████╔╝
  ██║███╗██║██╔══██║██╔══██║   ██║   ██║███╗██║██╔══╝  ██╔══██╗
  ╚███╔███╔╝██║  ██║██║  ██║   ██║   ╚███╔███╔╝███████╗██████╔╝
   ╚══╝╚══╝ ╚═╝  ╚═╝╚═╝  ╚═╝   ╚═╝    ╚══╝╚══╝ ╚══════╝╚═════╝ 
       Next Generation Web Scanner & Technology Fingerprinter
```
<p对齐=“中心”>
  <img src="https://img.shields.io/badge/WhatWeb-Web_Fingerprinter-blue?style=for-the-badge" alt="WhatWeb">
  <img src="https://img.shields.io/badge/Author-Andrew_Horton-orange?style=for-the-badge" alt="urbanadventurer">
  <img src="https://img.shields.io/badge/Category-Recon_&_Scanning-green?style=for-the-badge" alt="侦察与扫描">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="许可证 GPLv2">
</p>

<p对齐=“中心”>
  <b>🌐下一代网络扫描仪，旨在识别内容管理系统（CMS）、博客平台、JavaScript 库和服务器技术</b>
</p>

---

## 📋 目录

- [指纹识别概述与技术](#-overview--technology-fingerprinting)
- [安装](#-安装)
- [基本网站指纹](#-basic-website-fingerprinting)
- [攻击级别解释 (`-a`)](#-aggression-levels-explained--a)
  - [隐形被动模式 (`-a 1`)](#stealthy-passive-mode--a-1)
  - [攻击模式 (`-a 3`)](#aggressive-mode--a-3)
  - [重载/暴力模式 (`-a 4`)](#heavy--brute-mode--a-4)
- [扫描子网和目标文件](#-scanning-subnets--target-files)
- [插件系统和选择性审计](#-plugin-system--selective-auditing)
- [输出日志记录和报告导出](#-output-logging--report-exporting)
- [快速参考表](#-quick-reference-table)
- [防御考虑因素和 OPSEC](#-defective-considerations--opsec)
- [法律免责声明](#-legal-disclaimer)

---

## 🎯 概述和技术指纹识别

在针对 Web 应用程序启动漏洞利用或强力模糊测试之前，必须确定其确切的技术堆栈。

**WhatWeb** 由 **Andrew Horton（urbanadventurer）** 创建，可识别 1,800 多种不同的网络技术：
- **内容管理系统 (CMS)：** WordPress、Drupal、Joomla、Ghost、Magento。
- **Web 服务器和代理：** Apache、Nginx、Microsoft-IIS、Cloudflare、Envoy。
- **后端框架：** Laravel、Django、Ruby on Rails、ASP.NET、Spring Boot。
- **前端库：** React、Vue.js、Angular、jQuery、Bootstrap。
- **嵌入式设备：**网络摄像头、路由器、打印机、SCADA 接口。

---

## 📥 安装
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y whatweb

# Arch Linux
sudo pacman -S whatweb

# macOS (Homebrew)
brew install whatweb
```
---

## 🚀 基本网站指纹识别
```bash
# Scan a single domain
whatweb https://target.local

# Brief one-line output format
whatweb --color=never -q https://target.local

# Verbose detailed report showing evidence matches
whatweb -v https://target.local
```
---

## ⚡ 攻击性级别解释 (`-a`)

WhatWeb 使用四个攻击级别来平衡隐秘性和深度：

### 隐秘被动模式 (`-a 1`)
向提供的 URL 发送单个 HTTP GET 请求并分析返回的响应标头、cookie 和 HTML 标记，而不请求额外的页面：
```bash
whatweb -a 1 https://target.local
```
### 攻击模式 (`-a 3`)
推荐用于渗透测试。遵循重定向并请求常见插件文件（例如 `wp-login.php`、`robots.txt`、favicon 哈希）：
```bash
whatweb -a 3 https://target.local
```
### 重型/暴力模式 (`-a 4`)
尝试数千次插件猜测和已知指纹 URL：
```bash
whatweb -a 4 https://target.local
```
---

## 🌐 扫描子网和目标文件
```bash
# Scan an entire CIDR network block
whatweb 192.168.1.0/24

# Scan multiple URLs listed in a text file
whatweb -i targets.txt

# Pipe live web endpoints from httpx into WhatWeb
cat live_hosts.txt | whatweb --no-errors
```
---

## 🧩 插件系统和选择性审核

WhatWeb 具有丰富的插件架构：
```bash
# List all 1,800+ available plugins
whatweb -l

# Search for specific plugins (e.g., WordPress or Apache)
whatweb --grep wordpress

# Run ONLY a specific plugin against a target
whatweb --plugins="apache,php,wordpress" https://target.local
```
---

## 📊 输出记录和报告导出
```bash
# Export results to clean JSON format
whatweb https://target.local --log-json results.json

# Export to XML format
whatweb https://target.local --log-xml results.xml

# Export to compact CSV format
whatweb -i targets.txt --log-brief results.txt
```
---

## ⚡ 快速参考表

|目标|命令 |
|------------|---------|
| **标准扫描** | `whatweb <URL>` |
| **详细的详细报告** | `whatweb -v <URL>` |
| **隐形被动扫描** | `whatweb -a 1 <URL>` |
| **积极的深度扫描** | `whatweb -a 3 <URL>` |
| **扫描子网 (CIDR)** | `whatweb <SUBNET>/24` |
| **从文件扫描目标** | `whatweb -i targets.txt` |
| **导出为 JSON** | `whatweb <URL> --log-json report.json` |
| **按特定插件过滤**| `whatweb --plugins="wordpress,nginx" <URL>` |

---

## 🛡️ 防御考虑和 OPSEC

1. **服务器标头屏蔽：** 删除或随机化 Apache/Nginx 中的 `Server` 和 `X-Powered-By` HTTP 响应标头，以击败被动横幅指纹识别。
2. **Favicon 哈希屏蔽：** WhatWeb 使用 `/favicon.ico` 的 MD5 哈希来识别 Web 框架。自定义默认框架图标。

---

## ⚠️ 法律免责声明

> [!警告]
> WhatWeb 仅用于授权侦察、安全审计和系统清单。未经许可扫描未经授权的网络或网络应用程序可能会违反适用的法律。