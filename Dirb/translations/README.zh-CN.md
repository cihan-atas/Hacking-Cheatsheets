<!-- language-home-navigation -->
[🏠 中文主页](../../README.zh-CN.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 📁 DIRB - 完整备忘单

[🇹🇷 Türkçe Sürüm]（翻译/README.tr.md）
```
  ██████╗ ██╗██████╗ ██████╗ 
  ██╔══██╗██║██╔══██╗██╔══██╗
  ██║  ██║██║██████╔╝██████╔╝
  ██║  ██║██║██╔══██╗██╔══██╗
  ██████╔╝██║██║  ██║██████╔╝
  ╚═════╝ ╚═╝╚═╝  ╚═╝╚═════╝ 
        Web Content Scanner
```
<p对齐=“中心”>
  <img src="https://img.shields.io/badge/DIRB-Content_Scanner-orange?style=for-the-badge" alt="DIRB">
  <img src="https://img.shields.io/badge/Wordlists-Bruteforce-blue?style=for-the-badge" alt="Wordlists">
  <img src="https://img.shields.io/badge/Kali_Linux-Preinstalled-green?style=for-the-badge" alt="Kali Linux">
  <img src="https://img.shields.io/badge/Web-Pentesting-red?style=for-the-badge" alt="Web 渗透测试">
</p>

<p对齐=“中心”>
  <b>🔍经典网页内容扫描器对网页服务器发起基于字典的攻击</b>
</p>

---

## 📋 目录

- [简介](#-简介)
- [DIRB 与现代替代品](#-dirb-vs-modern-alternatives)
- [安装](#-安装)
- [默认单词列表](#-default-wordlists)
- [基本语法](#-basic-syntax)
- [常见扫描场景](#-common-scanning-scenarios)
- [高级选项和调整](#-advanced-options--tuning)
- [使用内置列表进行漏洞搜寻](#-vulnerability-hunting-with-built-in-lists)
- [真实世界工作流程](#-real-world-workflows)
- [快速参考表](#-quick-reference-table)
- [提示和最佳实践](#-tips--best-practices)
- [法律免责声明](#-legal-disclaimer)
- [资源](#-资源)

---

## 🎯 简介

**DIRB** (Directory Buster) 是一款用 C 语言编写的经典、轻量级、基于 URL 的 Web 内容扫描器。它对 Web 服务器发起基于字典的攻击，查找现有和隐藏的 Web 对象（文件、目录、脚本和端点）。

DIRB 的工作原理是为单词列表中的每个单词生成 HTTP 请求，分析服务器的响应代码（200、301、302、403 等），并确定资源是否存在。

### 主要特征

- ✅ **自动递归扫描：** 默认情况下自动扫描发现的子目录。
- ✅ **内置专用词汇表：** 附带针对常见 Web 服务器、CMS 和漏洞的目标词汇表。
- ✅ **低资源占用：** 轻量级二进制文件，即使在低规格硬件或旧系统上也能顺利运行。
- ✅ **自定义标头和代理支持：** 与 Burp Suite 和上游 HTTP 代理轻松集成。

---

## ⚖️ DIRB 与现代替代品

|特色 |迪尔布 |戈巴斯特 |噗噗|铁火克星 |搜索 |
|--------|------|----------|------|-------------|------------|
| **语言** | C |去 |去 |铁锈|蟒蛇 |
| **速度** | ⭐⭐（低线）| ⭐⭐⭐⭐⭐（快）| ⭐⭐⭐⭐⭐（极快）| ⭐⭐⭐⭐⭐（异步 Rust）| ⭐⭐⭐⭐（快）|
| **默认递归** | ✅ 是（自动）| ❌ 否 | ❌ 否（可选 `-recursion`）| ✅ 是（自动）| ✅ 是 (`-r`) |
| **包含自定义词汇表** | ✅ 是 (`/usr/share/dirb/`) | ❌ 否 | ❌ 否 | ❌ 否 | ✅ 是 (`db/dicc.txt`) |
| **VHost 模糊测试** | ❌ 否 | ✅ 是的 | ✅ 是的 | ❌ 否 | ✅ 是 (`--subdirs`) |
| **POST/数据模糊测试** | ❌ 否 | ❌ 否 | ✅ 是的 | ❌ 否 | ✅ 是 (`-d`) |
| **内存/CPU 使用率** |极低 |低|低|极低 |中等|

---

## 📥 安装

### Kali Linux 和 Parrot 操作系统

DIRB 预装在 Kali Linux 和 Parrot OS 中：
```bash
# Verify installation
dirb

# Update to latest package
sudo apt update && sudo apt install -y dirb
```
### Debian / Ubuntu
```bash
sudo apt update
sudo apt install -y dirb
```
### 从源代码编译
```bash
# Install build dependencies
sudo apt install -y build-essential libcurl4-openssl-dev

# Download and extract source
wget https://sourceforge.net/projects/dirb/files/dirb/2.22/dirb222.tar.gz
tar -xzvf dirb222.tar.gz
cd dirb222

# Configure permissions and compile
chmod -R 755 conf
chmod 755 configure
./configure
make
sudo make install
```
---

## 📚 默认单词列表

当安装在 Kali Linux 上时，DIRB 包含位于 `/usr/share/dirb/wordlists/` 的专用单词列表：

|单词列表路径 |条目 |目的|
|----------------|---------|---------|
| `/usr/share/dirb/wordlists/common.txt` | 〜4,614 |未指定单词列表时使用的默认列表 |
| `/usr/share/dirb/wordlists/small.txt` | 〜959 |快速发现高概率路径 |
| `/usr/share/dirb/wordlists/big.txt` | 〜20,469 |详尽的目录和文件搜索 |
| `/usr/share/dirb/wordlists/catala.txt` | 〜6,846 |加泰罗尼亚语词汇表 |
| `/usr/share/dirb/wordlists/spanish.txt` | 〜11,720 |西班牙语单词表 |
| `/usr/share/dirb/wordlists/mutations_common.txt` | 〜1,000 |常见的前缀/后缀变体 |
| `/usr/share/dirb/wordlists/extensions_common.txt` | 〜40 |标准网络扩展（`.php`、`.asp`、`.html` 等）|

### 内置漏洞词表 (`/usr/share/dirb/wordlists/vulns/`)

- `apache.txt` - Apache 特定的文件、状态页面和已知目录
- `iis.txt` - Microsoft IIS Web 服务器文件（`web.config`、`aspnet_client` 等）
- `tomcat.txt` - Apache Tomcat 管理和管理器路径
- `cgis.txt` - 通用网关接口 (CGI) 脚本和端点
- `fatwire.txt`、`hpsmh.txt`、`hyperion.txt`、`iplanet.txt`、`netware.txt`、`oracle.txt`

---

## ⌨️ 基本语法
```bash
dirb <url_base> [<wordlist_file(s)>] [options]
```
### 基本示例
```bash
# Basic scan using default common.txt wordlist
dirb http://10.10.10.10

# Scan using a specific custom wordlist
dirb https://example.com /usr/share/wordlists/dirb/big.txt

# Scan with custom SecLists directory list
dirb http://target.local /usr/share/seclists/Discovery/Web-Content/directory-list-2.3-medium.txt
```
---

## 🔍 常见扫描场景

### 1. 使用文件扩展名进行扫描（`-X` 和 `-x`）

搜索附加到词典中每个单词的特定文件扩展名：
```bash
# Test specific extensions on every wordlist entry
dirb http://target.local -X .php,.html,.txt,.bak

# Use an extensions list file
dirb http://target.local -x /usr/share/dirb/wordlists/extensions_common.txt

# Combine directory brute-force with extension brute-force
dirb http://target.local /usr/share/dirb/wordlists/common.txt -X .php,.json,.zip
```
### 2. 控制递归（`-r` 和 `-R`）

默认情况下，DIRB 递归地爬网每个发现的目录。您可以控制此行为：
```bash
# Disable recursion completely (scan root level only)
dirb http://target.local -r

# Interactive recursion (DIRB asks before descending into each directory)
dirb http://target.local -R
```
### 3. 不区分大小写扫描 (`-i`)

当针对文件路径不区分大小写的 Windows / IIS 服务器时很有用：
```bash
dirb http://target.local -i
```
### 4. 忽略特定的 HTTP 状态代码 (`-N`)

过滤掉不需要的响应代码（例如，重定向循环或禁止路径）：
```bash
# Ignore 302 redirects and 403 forbidden responses
dirb http://target.local -N 302,403
```
---

## ⚙️ 高级选项和调整

### 身份验证 (`-u`)

DIRB 支持 HTTP 基本和摘要式身份验证：
```bash
# Supply HTTP authentication credentials
dirb http://target.local -u "admin:SecretP@ssw0rd!"

# Combine auth with custom wordlist
dirb http://target.local /usr/share/dirb/wordlists/big.txt -u "pentester:Summer2026"
```
### 自定义 Cookie 和会话令牌 (`-c`)

通过提供会话 cookie 来测试经过身份验证的 Web 应用程序路由：
```bash
# Pass session cookie
dirb http://target.local -c "PHPSESSID=d92j4fn82mdf82ncb184; logged_in=true"
```
### 自定义用户代理和 HTTP 标头（`-a` 和 `-H`）

绕过基本WAF规则或模拟合法浏览器：
```bash
# Set custom User-Agent
dirb http://target.local -a "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36"

# Add custom HTTP Header
dirb http://target.local -H "Authorization: Bearer eyJhbGciOi..."
dirb http://target.local -H "X-Forwarded-For: 127.0.0.1"
```
### 代理路由和 Burp Suite 集成（`-p` 和 `-P`）

通过 Burp Suite 或 OWASP ZAP 等上游 HTTP 代理发送 DIRB 流量：
```bash
# Route through local Burp Suite proxy
dirb http://target.local -p 127.0.0.1:8080

# Authenticated proxy
dirb http://target.local -p 10.10.10.1:3128 -P "proxyuser:proxypass"
```
### 速率限制和规避 (`-z`)

通过在请求之间添加延迟（以毫秒为单位），避免触发 IDS/IPS 警报或 DoSing 脆弱的 Web 服务：
```bash
# Wait 250 milliseconds between each request
dirb http://target.local -z 250

# Slow, stealthy scan (1 second delay)
dirb http://target.local -z 1000
```
### 输出记录（`-o` 和 `-w`）
```bash
# Save results to a file
dirb http://target.local -o /home/zeus/scans/dirb_target.txt

# Do not halt on WARNING messages (useful for uninterrupted scans)
dirb http://target.local -w -o dirb_output.txt
```
---

## 🎯 使用内置列表进行漏洞搜寻

DIRB 在 `/usr/share/dirb/wordlists/vulns/` 中包含目标列表，用于检测未修补的服务器和危险端点：
```bash
# Target Apache server vulnerabilities and status pages
dirb http://target.local /usr/share/dirb/wordlists/vulns/apache.txt

# Target IIS endpoints and misconfigurations
dirb http://target.local /usr/share/dirb/wordlists/vulns/iis.txt

# Target Tomcat management consoles
dirb http://target.local /usr/share/dirb/wordlists/vulns/tomcat.txt

# Search for old CGI scripts and shellshock entrypoints
dirb http://target.local /usr/share/dirb/wordlists/vulns/cgis.txt
```
---

## 🎬 现实世界的工作流程

### 工作流程 1：快速初步侦察

当您第一次在端口 80/443 上遇到 Web 服务器时：
```bash
# Rapid scan without recursion using small wordlist
dirb http://10.10.10.50 /usr/share/dirb/wordlists/small.txt -r
```
### 工作流程 2：详尽的 Web 应用程序评估

执行深度扫描以查找配置备份、源代码和 API 端点：
```bash
dirb https://app.target.local /usr/share/dirb/wordlists/common.txt \
  -X .php,.bak,.sql,.config,.txt \
  -a "Mozilla/5.0" \
  -w \
  -o dirb_app_scan.txt
```
### 工作流程 3：通过 Burp Suite 进行经过身份验证的门户爬网
```bash
dirb https://internal.target.local/portal/ /usr/share/dirb/wordlists/big.txt \
  -c "session=s%3A_kJw81..." \
  -p 127.0.0.1:8080 \
  -X .jsp,.do,.action \
  -z 100
```
---

## 📊 快速参考表

|旗帜|论证|描述 |示例|
|------|----------|-------------|---------|
| `-a` | `<user_agent>` |指定自定义用户代理字符串 | `dirb http://target -a "Mozilla/5.0"` |
| `-c` | `<cookie>` |设置 HTTP Cookie 标头 | `dirb http://target -c "id=123"` |
| `-H` | `<header>` |添加自定义 HTTP 标头 | `dirb http://target -H "X-API-Key: secret"` |
| `-i` |无 |不区分大小写的搜索 | `dirb http://target -i`|
| `-N`| `<codes>` |忽略匹配状态代码的响应 | `dirb http://target -N 302,403` |
| `-o` | `<output_file>`|将扫描结果保存到文件 | `dirb http://target -o results.txt`|
| `-p`| `<proxy:port>`|使用 HTTP 代理 | `dirb http://target -p 127.0.0.1:8080` |
| `-P`| `<user:pass>` |代理身份验证凭据 | `dirb http://target -p proxy:8080 -P u:p` |
| `-r` |无 |不要递归搜索 | `dirb http://target -r` |
| `-R`|无 |交互式递归搜索 | `dirb http://target -R` |
| `-u` | `<user:pass>` | HTTP 基本/摘要式身份验证 | `dirb http://target -u "admin:1234"` |
| `-w`|无 |出现警告消息时不要停止扫描 | `dirb http://target -w`|
| `-X`| `<ext_list>` |追加以逗号分隔的扩展名 | `dirb http://target -X .php,.html` |
| `-x`| `<ext_file>` |从文件追加扩展名 | `dirb http://target -x ext.txt` |
| `-z` | `<millisecs>` |在请求之间添加延迟 | `dirb http://target -z 250`|

---

## 💡 提示和最佳实践

1. **注意通配符 200/302 响应：**
   - 某些 Web 应用程序为每个请求返回 HTTP 200 或 302（自定义 404 处理程序）。 DIRB 将报告找到的每个条目。使用 `-N 302` 或切换到 `ffuf` / `feroxbuster` 进行高级大小/字过滤。
2. **与`-r`结合使用大词典：**
   - 如果使用 `directory-list-2.3-medium.txt`（220k 字），请始终禁用 `-r` 的递归。否则，DIRB 可能需要几天的时间来爬行深层嵌套的目录。
3. **始终检查扩展名：**
   - 现代后端经常暴露敏感文件，例如 `.env`、`.git`、`.bak`、`.conf` 或 `.sql`。始终使用 `-X` 测试常见扩展。
4. **使用 `-w` 进行无人值守扫描：**
   - 默认情况下，如果遇到 SSL 或网络警告，DIRB 可能会暂停。通过 `-w` 可确保测试在无需手动交互的情况下完成。

---

## ⚠️ 法律免责声明

> **重要提示：** DIRB 旨在对您拥有或具有明确、记录的测试许可的系统进行安全审核、渗透测试和道德研究。对网络服务器进行未经授权的字典攻击可能会导致服务中断，并且根据全球计算机犯罪立法，这是非法的。

---

## 📚 资源

- [DIRB SourceForge 项目](https://sourceforge.net/projects/dirb/)
- [Kali Linux DIRB 包文档](https://www.kali.org/tools/dirb/)
- [SecLists - 网页内容发现词汇表](https://github.com/danielmiessler/SecLists)