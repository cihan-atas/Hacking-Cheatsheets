<!-- language-home-navigation -->
[🏠 中文主页](../../README.zh-CN.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🕶️ mitmproxy - 完整备忘单

[🇹🇷 Türkçe Sürüm]（翻译/README.tr.md）
```
  ███╗   ███╗██╗████████╗███╗   ███╗██████╗ ██████╗  ██████╗ ██╗  ██╗██╗   ██╗
  ████╗ ████║██║╚══██╔══╝████╗ ████║██╔══██╗██╔══██╗██╔═══██╗╚██╗██╔╝╚██╗ ██╔╝
  ██╔████╔██║██║   ██║   ██╔████╔██║██████╔╝██████╔╝██║   ██║ ╚███╔╝  ╚████╔╝ 
  ██║╚██╔╝██║██║   ██║   ██║╚██╔╝██║██╔═══╝ ██╔══██╗██║   ██║ ██╔██╗   ╚██╔╝  
  ██║ ╚═╝ ██║██║   ██║   ██║ ╚═╝ ██║██║     ██║  ██║╚██████╔╝██╔╝ ██╗   ██║   
  ╚═╝     ╚═╝╚═╝   ╚═╝   ╚═╝     ╚═╝╚═╝     ╚═╝  ╚═╝ ╚═════╝ ╚═╝  ╚═╝   ╚═╝   
       Interactive, Scriptable Terminal HTTP/HTTPS Proxy & Traffic Engine
```
<p对齐=“中心”>
  <img src="https://img.shields.io/badge/mitmproxy-Terminal_Proxy-blue?style=for-the-badge" alt="mitmproxy">
  <img src="https://img.shields.io/badge/Author-mitmproxy_Project-orange?style=for-the-badge" alt="mitmproxy">
  <img src="https://img.shields.io/badge/Category-Network_&_Web_Analysis-green?style=for-the-badge" alt="网络分析">
  <img src="https://img.shields.io/badge/License-MIT-purple?style=for-the-badge" alt="MIT">
</p>

<p对齐=“中心”>
  <b>🕶️ 一个交互式、支持 SSL/TLS 的拦截 HTTP 代理，为渗透测试人员、安全研究人员和软件工程师提供丰富的控制台界面和 Python 脚本 API</b>
</p>

---

## 📋 目录

- [概述和三个二进制文件](#-overview--the-two-binaries)
- [安装和证书设置](#-installation--certificate-setup)
  - [在 Linux / macOS 上安装](#installing-on-linux--macos)
  - [安装用于 SSL/TLS 拦截的 CA 证书](#installing-ca-certificate-for-ssltls-interception)
- [操作模式：常规、透明和反向代理](#-operating-modes-regular-transparent--reverse-proxy)
- [交互式 TUI 导航 (`mitmproxy`)](#-interactive-tui-navigation-mitmproxy)
  - [键盘快捷键](#keyboard-shortcuts)
  - [过滤和拦截流](#filtering--intercepting-flows)
- [非交互式自动化 (`mitmdump`)](#-non-interactive-automation-mitmdump)
  - [录制和回放流量](#recording--replaying-traffic)
- [Python 脚本和插件](#-python-scripting--addons)
  - [自动标头操作脚本](#automated-header-manipulation-script)
  - [实时响应身体篡改](#live-response-body-tampering)
- [快速参考表](#-quick-reference-table)
- [防守考虑与检测](#-defective-considerations--detection)
- [法律免责声明](#-legal-disclaimer)

---

## 🎯 概述和三个二进制文件

与庞大的图形桌面代理不同，**mitmproxy** 直接在终端或自动化管道内提供闪电般快速、可编写脚本、全保真 HTTP/1、HTTP/2 和 WebSockets 拦截。

它附带三个入口点二进制文件：
1. **`mitmproxy`：** 基于 ncurses 的交互式终端用户界面 (TUI)。
2. **`mitmdump`：** 非交互式命令行版本（HTTP 的 `tcpdump` 等效项）。
3. **`mitmweb`：** 基于浏览器的轻量级图形 Web 界面。

---

## 📥 安装和证书设置

### 在 Linux / macOS 上安装
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y mitmproxy

# Arch Linux
sudo pacman -S mitmproxy

# Python pip / pipx
pipx install mitmproxy
```
### 安装 CA 证书以进行 SSL/TLS 拦截
要拦截 HTTPS 流量，目标必须信任 mitmproxy 的动态证书颁发机构：
1. 启动 mitmproxy 一次：`mitmproxy`（在 `~/.mitmproxy/` 中创建证书）。
2. 在客户端设备/浏览器上，配置代理 `127.0.0.1:8080`。
3. 浏览到 `http://mitm.it` 并下载/安装适用于 Android、iOS、Windows 或 Linux 的证书。

---

## ⚙️ 操作模式：常规、透明和反向代理

### 常规代理模式（默认）
标准转发代理侦听端口 8080：
```bash
mitmproxy -p 8080
```
### 反向代理模式
将传入流量中继到特定目标服务器（非常适合 API 测试）：
```bash
mitmproxy --mode reverse:https://api.target.com -p 8080
```
### 透明模式
拦截通过 iptables 路由的流量，无需配置客户端代理设置：
```bash
mitmproxy --mode transparent --showhost
```
---

## 🖥️ 交互式 TUI 导航 (`mitmproxy`)

### 键盘快捷键
- `?`：打开帮助叠加层。
- `Enter`：检查选定的 HTTP 流（请求/响应/详细信息）。
- `q`：返回/退出。
- `d`：删除当前流。
- `r`：将选定的请求重播到服务器。
- `e`：编辑请求/响应标头、路径或正文。
- `a`：恢复暂停的流程（拦截时）。
- `A`：恢复所有暂停的流。

### 过滤和拦截流
按 `i` 输入拦截过滤表达式：
```text
~u /api/v1/ & ~m POST      # Intercept only POST requests to /api/v1/
~c 401 | ~c 403            # Filter flows by HTTP status code
~b "password"              # Filter flows where body contains "password"
```
---

## ⚡ 非交互式自动化 (`mitmdump`)

### 记录和重放流量
将实时流量直接捕获到二进制转储文件中：
```bash
# Record all HTTP traffic to file
mitmdump -w traffic.dump

# Read and inspect captured dump
mitmdump -n -r traffic.dump

# Replay client requests from dump against target
mitmdump --client-replay traffic.dump
```
---

## 🐍 Python 脚本和插件

mitmproxy 具有原生 Python API，用于以编程方式修改流量：

### 自动标头操作脚本 (`add_headers.py`)
```python
from mitmproxy import http

class AddHeader:
    def request(self, flow: http.HTTPFlow):
        # Inject custom authorization or testing header into every outbound request
        flow.request.headers["X-Security-Audit"] = "Authorized-Pentest"
        flow.request.headers["Authorization"] = "Bearer injected_token_xyz"

addons = [AddHeader()]
```
运行：
```bash
mitmproxy -s add_headers.py
```
### 实时响应身体篡改 (`tamper_response.py`)
```python
from mitmproxy import http

def response(flow: http.HTTPFlow):
    # Force admin privilege in returned JSON response
    if "api/user/me" in flow.request.url:
        flow.response.text = flow.response.text.replace('"isAdmin": false', '"isAdmin": true')
```
运行：
```bash
mitmdump -s tamper_response.py
```
---

## ⚡ 快速参考表

|目标|命令 |
|------------|---------|
| **启动交互式 TUI** | `mitmproxy -p 8080` |
| **启动 Web GUI** | `mitmweb -p 8080` |
| **静默 CLI 记录器** | `mitmdump -p 8080` |
| **反向代理目标** | `mitmproxy --mode reverse:https://target.com` |
| **将流量记录到文件** | `mitmdump -w capture.mitm` |
| **加载Python插件脚本**| `mitmproxy -s script.py` |
| **忽略目标主机** | `mitmproxy --ignore-hosts ".*apple\.com"` |

---

## 🛡️ 防守考虑和检测

1. **证书固定：** 执行 SSL Pinning 的移动应用程序拒绝 mitmproxy 的 CA 证书。与 Frida / Objection 配对以绕过客户端检查。
2. **TLS 握手中的代理检测：** 代理引入了 WAF 和机器人管理器可以检测到的自定义 TLS 密码套件和 ALPN 协商指纹。

---

## ⚠️ 法律免责声明

> [!警告]
> mitmproxy 拦截并解密敏感网络通信。它必须严格部署在已获得明确授权的系统和网络上。未经同意拦截通信是非法的。