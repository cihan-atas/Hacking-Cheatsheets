<!-- language-home-navigation -->
[🏠 中文主页](../../README.zh-CN.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🛰️ sshuttle - 完整备忘单

[🇹🇷 Türkçe Sürüm]（翻译/README.tr.md）
```
  ███████╗███████╗██╗  ██╗██╗   ██╗████████╗████████╗██╗     ███████╗
  ██╔════╝██╔════╝██║  ██║██║   ██║╚══██╔══╝╚══██╔══╝██║     ██╔════╝
  ███████╗███████╗███████║██║   ██║   ██║      ██║   ██║     █████╗  
  ╚════██║╚════██║██╔══██║██║   ██║   ██║      ██║   ██║     ██╔══╝  
  ███████║███████║██║  ██║╚██████╔╝   ██║      ██║   ███████╗███████╗
  ╚══════╝╚══════╝╚═╝  ╚═╝ ╚═════╝    ╚═╝      ╚═╝   ╚══════╝╚══════╝
       Transparent Proxy Server that Works as a Poor Man's VPN over SSH
```
<p对齐=“中心”>
  <img src="https://img.shields.io/badge/sshuttle-Poor_Man's_VPN-blue?style=for-the-badge" alt="sshuttle">
  <img src="https://img.shields.io/badge/Category-Pivoting_&_Tunneling-orange?style=for-the-badge" alt="旋转和隧道">
  <img src="https://img.shields.io/badge/Protocols-TCP_DNS_SSH-green?style=for-the-badge" alt="协议">
  <img src="https://img.shields.io/badge/License-LGPLv2.1-red?style=for-the-badge" alt="许可证 LGPLv2.1">
</p>

<p对齐=“中心”>
  <b>🌐 通过非特权 SSH 会话转发整个内部子网和 DNS 请求，无需远程服务器上的管理访问权限</b>
</p>

---

## 📋 目录

- [概述和“穷人的 VPN”哲学](#-overview--the-poor-mans-vpn-philosophy)
- [sshuttle 的工作原理](#-how-sshuttle-works)
- [工具比较：sshuttle vs SOCKS vs Ligolo-ng vs OpenVPN](#-tool-comparison-sshuttle-vs-socks-vs-ligolo-ng-vs-openvpn)
- [安装](#-安装)
- [基本子网路由](#-basic-subnet-routing)
- [DNS 流量转发 (`--dns`)](#-dns-traffic-forwarding---dns)
- [完整网关重定向（默认路由）](#-full-gateway-redirection-default-route)
- [子网排除 (`-x`)](#-subnet-exclusions--x)
- [SSH 选项和基于密钥的身份验证 (`-e`)](#-ssh-options--key-based-authentication--e)
- [守护进程后台执行 (`-D`)](#-daemon-background-execution--d)
- [快速参考表](#-quick-reference-table)
- [防御考虑因素和 OPSEC](#-defective-considerations--opsec)
- [法律免责声明](#-legal-disclaimer)

---

## 🎯 概述和“穷人的 VPN”理念

**sshuttle** 是一种透明代理服务器，通过标准 SSH 连接作为轻量级、零配置 VPN 运行。

在典型的企业渗透测试和红队评估中，转向内部网络受到严格限制的阻碍：
- ❌ 您很少在初始 SSH 堡垒/枢纽主机上拥有 `root` 或 `SYSTEM` 访问权限。
- ❌ 传统 VPN 解决方案（OpenVPN、WireGuard）需要两个端点上的管理权限和虚拟 TUN/TAP 网络设备。
- ❌ 标准 SOCKS 代理（如 `ssh -D`）需要为每个工具配置 `proxychains` 或单独的应用程序代理设置。

**sshuttle 同时解决了所有三个限制：**
- ✅ **不需要远程根：** Pivot 服务器只需要标准的 Python 安装（`python3`）。
- ✅ **无需远程 TUN/TAP：** 在 SSH 会话上使用纯 TCP 流复用。
- ✅ **系统范围的透明路由：** 修改攻击者机器上的本地 `iptables` / `nftables`，使工具（`nmap`、`curl`、`smbclient`、Web 浏览器）无需包装器即可直接定位内部 IP！

---

## ⚙️ sshuttle 的工作原理
```
[ Attacker Box (Local Root) ]
  ├── iptables / nftables redirects internal IP traffic (10.10.10.0/24) to local port
  └── sshuttle client multiplexes TCP packets over SSH tunnel
              │
              ▼ (Encrypted SSH Channel - Port 22)
[ Remote Pivot Server (Unprivileged User + Python) ]
  └── Assembles TCP connections and forwards them directly to internal hosts
              │
              ▼
[ Target Internal Network (10.10.10.0/24) ]
```
---

## ⚖️ 工具比较：sshuttle、SOCKS、Ligolo-ng、OpenVPN

|特色 | `sshuttle` |袜子 (`ssh -D`) | `Ligolo-ng` | `OpenVPN` |
|--------|------------|------------------|-------------|------------|
| **需要远程根** | ❌ 否 | ❌ 否 | ❌ 否 | ✅ 是的 |
| **需要本地根** | ✅ 是（iptables）| ❌ 否 | ✅ 有（TUN 接口）| ✅ 是的 |
| **透明路由** | ✅ 是的 | ❌ 需要代理链 | ✅ 是（原生 TUN）| ✅ 是的 |
| **UDP 流量支持** | ❌ 仅 DNS | ❌ 否 | ✅ 是的 | ✅ 完整的 UDP |
| **需要目标上有 Python**| ✅ 是（Python 3）| ❌ 否 | ❌ 否（独立代理）| ❌ 否 |
| **Nmap SYN 扫描 (`-sS`)** | ❌ TCP 连接 (`-sT`)| ❌ 仅 TCP 连接 | ✅ 原始数据包支持 | ✅ 完整的原始包装 |

---

## 📥 安装
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y sshuttle

# Arch Linux
sudo pacman -S sshuttle

# macOS (Homebrew)
brew install sshuttle

# Python Pipx / Pip
pipx install sshuttle
```
---

## 🚀 基本子网路由

通过 SSH Pivot 主机转发整个远程 C 类子网：
```bash
# Route 10.10.10.0/24 through user@192.168.1.50
sshuttle -r user@192.168.1.50 10.10.10.0/24

# Route multiple disjoint subnets simultaneously
sshuttle -r user@192.168.1.50 10.10.10.0/24 172.16.0.0/16 192.168.100.0/24
```
连接后，您可以直接与内部机器通信：
```bash
# No proxychains needed!
curl http://10.10.10.25:8080/
smbclient -L //10.10.10.10/ -U 'domain/user%pass'
```
---

## 🌐 DNS 流量转发 (`--dns`)

将内部公司 DNS 查询（`.corp`、`.local`）转发到远程网络的内部 DNS 服务器：
```bash
# Route subnet and proxy internal DNS queries
sshuttle --dns -r user@192.168.1.50 10.10.10.0/24

# Query internal domain directly via nslookup/dig
dig dc01.internal.corp
```
---

## 🌍 完全网关重定向（默认路由）

通过 SSH 连接路由所有出站攻击者流量（完整 VPN 行为）：
```bash
# Route all traffic through remote SSH server (0.0.0.0/0)
sshuttle -r user@remote-vps.com 0.0.0.0/0

# Route all traffic except local network
sshuttle -r user@remote-vps.com 0.0.0.0/0 -x 192.168.1.0/24
```
---

## 🚫 子网排除 (`-x`)

防止捕获特定的敏感或环回子网：
```bash
# Route entire 10.0.0.0/8 network, but exclude the pivot host itself
sshuttle -r user@10.0.0.50 10.0.0.0/8 -x 10.0.0.50/32
```
---

## 🔑 SSH 选项和基于密钥的身份验证 (`-e`)

传递自定义 SSH 标志（私有身份密钥、非标准 SSH 端口）：
```bash
# Use custom identity file and non-standard SSH port 2222
sshuttle -e 'ssh -i /path/to/id_rsa -p 2222' -r user@192.168.1.50 10.10.10.0/24
```
---

## 🤖 守护进程后台执行 (`-D`)

在正在进行的活动期间将 sshuttle 作为后台守护进程运行：
```bash
# Start background daemon with pidfile
sudo sshuttle -D --pidfile /tmp/sshuttle.pid -r user@192.168.1.50 10.10.10.0/24

# Terminate daemon when finished
sudo kill $(cat /tmp/sshuttle.pid)
```
---

## ⚡ 快速参考表

|目标|命令 |
|------------|---------|
| **路由单个子网** | `sshuttle -r <USER>@<PIVOT> <SUBNET>/24` |
| **使用 DNS 路由子网** | `sshuttle --dns -r <USER>@<PIVOT> <SUBNET>/24` |
| **路由多个子网** | `sshuttle -r <USER>@<PIVOT> <NET1>/24 <NET2>/16` |
| **排除 IP/子网** | `sshuttle -r <USER>@<PIVOT> <SUBNET>/24 -x <EXCLUDE_IP>` |
| **完全流量重定向** | `sshuttle -r <USER>@<PIVOT> 0.0.0.0/0` |
| **自定义 SSH 密钥/端口** | `sshuttle -e 'ssh -i <KEY> -p <PORT>' -r <USER>@<PIVOT> <SUBNET>` |
| **在后台运行** | `sshuttle -D --pidfile /tmp/sshuttle.pid -r <USER>@<PIVOT> <SUBNET>` |

---

## 🛡️ 防御考虑和 OPSEC

1. **防火墙修改：** sshuttle 修改本地防火墙表（Linux 上为 `iptables`，macOS 上为 `pf`）。如果 sshuttle 意外崩溃，请使用 `iptables -F` 或重新启动防火墙服务来刷新过时的重定向规则。
2. **仅 TCP 限制：** 不支持非 DNS UDP 数据包、ICMP 回显请求 (ping) 和 SYN 扫描。始终使用 `nmap -sT -Pn -n` 通过 sshuttle 进行端口扫描。
3. **目标足迹：** sshuttle 在会话初始化期间自动将一个小型 Python 汇编脚本上传到远程 Pivot 主机，并通过 `python3` 运行它。

---

## ⚠️ 法律免责声明

> [!警告]
> 本备忘单仅用于授权的渗透测试和系统管理。未经授权的路由和拦截网络流量是非法的。