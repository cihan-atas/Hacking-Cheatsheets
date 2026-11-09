<!-- language-home-navigation -->
[🏠 中文主页](../../README.zh-CN.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🕷️ CeWL - 完整备忘单

[🇹🇷 Türkçe Sürüm]（翻译/README.tr.md）
```
   ██████╗███████╗██╗    ██╗██╗     
  ██╔════╝██╔════╝██║    ██║██║     
  ██║     █████╗  ██║ █╗ ██║██║     
  ██║     ██╔══╝  ██║███╗██║██║     
  ╚██████╗███████╗╚███╔███╔╝███████╗
   ╚═════╝╚══════╝ ╚══╝╚══╝ ╚══════╝
       Custom Word List Generator by Robin Wood (DigiNinja)
```
<p对齐=“中心”>
  <img src="https://img.shields.io/badge/CeWL-Custom_Wordlist_Generator-blue?style=for-the-badge" alt="CeWL">
  <img src="https://img.shields.io/badge/Author-DigiNinja-orange?style=for-the-badge" alt="DigiNinja">
  <img src="https://img.shields.io/badge/Category-Password_Cracking-green?style=for-the-badge" alt="密码破解">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="许可证 GPLv2">
</p>

<p对齐=“中心”>
  <b>🌐 针对特定目标的密码字典生成器，可通过蜘蛛程序抓取网站以获取专门的公司术语、品牌名称和员工电子邮件</b>
</p>

---

## 📋 目录

- [概述和目标密码哲学](#-overview--the-targeted-password-philosophy)
- [安装](#-安装)
- [基本网站抓取](#-basic-website-crawling)
- [Spider 深度和字长控制](#-spider-depth--word-length-controls)
- [收获电子邮件地址 (`-e`)](#-harvesting-email-addresses--e)
- [包括数字和小写](#-include-numbers--lowercasing)
- [文档元数据提取（`-a`）](#-document-metadata-extraction--a)
- [字数和频率分析 (`-c`)](#-word-count--Frequency-profiling--c)
- [用 Hashcat 和 John 规则链接 CeWL](#-chaining-cewl-with-hashcat--john-rules)
- [快速参考表](#-quick-reference-table)
- [防御考虑因素和 OPSEC](#-defective-considerations--opsec)
- [法律免责声明](#-legal-disclaimer)

---

## 🎯 概述和目标密码原理

像 `rockyou.txt` 这样的通用单词列表对于消费者密码来说是有效的，但是**它们经常无法满足要求 12 个以上字符、特殊符号和季节性轮换的公司密码策略**。

员工通常根据以下因素设置企业密码：
- 公司项目名称、内部代号和口号（`Pegasus2026!`）。
- 特定行业术语、技术术语或产品品牌。
- 高管姓名、地标和办公地址。

**CeWL**（自定义单词列表生成器）由 **Robin Wood (DigiNinja)** 创建，是一种 Ruby 蜘蛛，可以将指定的目标 URL 爬行到定义的深度，提取符合最小长度要求的唯一单词，并构建一个定制的密码字典。

---

## 📥 安装
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y cewl

# Arch Linux (AUR)
yay -S cewl

# Ruby Gem
gem install cewl
```
---

## 🚀 基本网站抓取
```bash
# Crawl website and output words to console
cewl https://target.local

# Crawl and save to a custom wordlist file
cewl https://target.local -w company_wordlist.txt
```
---

## 🕸️ Spider 深度和字长控制

控制 CeWL 跟踪链接的深度以及它捕获的字长：
```bash
# Crawl 2 links deep (-d 2) and enforce minimum word length of 8 characters (-m 8)
cewl https://target.local -d 2 -m 8 -w wordlist_min8.txt

# Aggressive 3-level deep crawl for comprehensive corporate terminology
cewl https://target.local -d 3 -m 7 --no-words -w deep_crawl.txt
```
---

## 📧 收集电子邮件地址 (`-e`)

提取公司电子邮件地址以进行后续的网络钓鱼模拟或凭据喷洒：
```bash
# Extract emails to a separate dedicated file
cewl https://target.local -e --email_file emails.txt -w words.txt
```
---

## 🔡 包括数字和小写
```bash
# Include words containing alphanumeric combinations (e.g., product names like V300, X86)
cewl https://target.local --with-numbers -m 6 -w words_with_numbers.txt

# Convert all discovered words to lowercase (great for rule generation)
cewl https://target.local --lowercase -w lowercase_words.txt
```
---

## 📄 文档元数据提取（`-a`）

从下载的 PDF 和 Microsoft Office 文档中提取作者和创建者用户名：
```bash
# Download and analyze document metadata to extract employee usernames
cewl https://target.local -a --meta_file extracted_authors.txt
```
---

## 📊 字数统计和频率分析 (`-c`)

按频率对单词进行排序，以确定最普遍的企业流行语：
```bash
# Append occurrence count next to each word
cewl https://target.local -c -m 6 -w frequency.txt

# Sort by most frequent words in descending order
sort -k2 -nr frequency.txt | head -n 30
```
---

## ⚡ 使用 Hashcat 和 John Rules 链接 CeWL

原始 CeWL 单词列表包含基本词典词根。要生成可行的密码，请使用 **Hashcat / John the Ripper 突变规则** 扩展单词列表：
```bash
# 1. Harvest base words from corporate website
cewl https://target.local -m 5 --lowercase -w base_words.txt

# 2. Mutate words using Hashcat's best64 rule set
hashcat --force base_words.txt -r /usr/share/hashcat/rules/best64.rule --stdout | sort -u > mutated_passwords.txt

# Example mutations produced:
# Acmetool -> Acmetool2026!, !Acmetool123, 4cmet00l, Acmetool#1
```
---

## ⚡ 快速参考表

|目标|命令 |
|------------|---------|
| **基本爬行到文件** | `cewl <URL> -w wordlist.txt` |
| **设置最小长度和深度** | `cewl <URL> -m 8 -d 2 -w wordlist.txt` |
| **提取公司电子邮件**| `cewl <URL> -e --email_file emails.txt` |
| **提取文档元数据**| `cewl <URL> -a --meta_file authors.txt` |
| **包括字母数字单词**| `cewl <URL> --with-numbers -w wordlist.txt` |
| **小写输出** | `cewl <URL> --lowercase -w wordlist.txt` |
| **统计词频** | `cewl <URL> -c -w frequencies.txt` |
| **登录后进行身份验证**| `cewl <URL> -a "Cookie: session=xyz"` |

---

## 🛡️ 防御考虑和 OPSEC

1. **Web 服务器访问日志：** CeWL 生成数百个快速 HTTP GET 请求，生成可识别的蜘蛛访问日志。使用自定义用户代理字符串 (`-u "Mozilla/5.0..."`) 进行混合。
2. **蜜语陷阱：** 防守蓝队可以嵌入不可见或隐藏的 HTML 链接，从而获得金丝雀凭证来标记自动蜘蛛。

---

## ⚠️ 法律免责声明

> [!警告]
> CeWL 专为授权安全评估、渗透测试和密码审计研究而设计。严格禁止抓取或抓取未经授权的系统。