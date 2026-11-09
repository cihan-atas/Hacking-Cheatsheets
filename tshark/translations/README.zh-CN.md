<!-- language-home-navigation -->
[🏠 中文主页](../../README.zh-CN.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🦈 tshark - 完整备忘单

[🇹🇷 Türkçe Sürüm]（翻译/README.tr.md）
```
  ████████╗███████╗██╗  ██╗ █████╗ ██████╗ ██╗  ██╗
  ╚══██╔══╝██╔════╝██║  ██║██╔══██╗██╔══██╗██║ ██╔╝
     ██║   ███████╗███████║███████║██████╔╝█████╔╝ 
     ██║   ╚════██║██╔══██║██╔══██║██╔══██╗██╔═██╗ 
     ██║   ███████║██║  ██║██║  ██║██║  ██║██║  ██╗
     ╚═╝   ╚══════╝╚═╝  ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝
         Terminal Packet Analysis & Dissection Engine
```
<p对齐=“中心”>
  <img src="https://img.shields.io/badge/tshark-Packet_Analyzer-blue?style=for-the-badge" alt="tshark">
  <img src="https://img.shields.io/badge/CLI-Network_Sniffer-red?style=for-the-badge" alt="CLI 网络嗅探器">
  <img src="https://img.shields.io/badge/Wireshark-Engine-green?style=for-the-badge" alt="Wireshark 引擎">
  <img src="https://img.shields.io/badge/PCAP-Dissection-orange?style=for-the-badge" alt="PCAP 解剖">
</p>

<p对齐=“中心”>
  <b>🌐 由 Wireshark 提供支持的基于终端的深度网络数据包解析、流分析和字段提取</b>
</p>

---

## 📋 目录

- [简介](#-简介)
- [工具比较：tshark、tcpdump、Wireshark](#-tool-comparison)
- [安装和权限](#-安装--权限)
- [基本捕捉和回放](#-basic-capture--playback)
- [过滤流量：捕获与显示](#-filtering-traffic)
- [字段提取 (`-T fields`)](#-字段提取)
- [实用渗透测试和法医食谱](#-practical-pentest--forensic-recipes)
  - [HTTP 凭证和 URI 狩猎](#http-credential--uri-hunting)
  - [DNS 查询提取和隧道检测](#dns-query-extraction--tunneling-detection)
  - [TLS / HTTPS SNI 提取](#tls--https-sni-extraction)
  - [Kerberos AS-REQ / AS-REP 提取](#kerberos-as-req--as-rep-extraction)
  - [SMB 和 NTLM 身份验证跟踪](#smb--ntlm-authentication-tracking)
  - [明文协议审计（FTP、Telnet、SMTP）](#cleartext-protocol-auditing)
  - [WPA / EAPOL 4 路握手狩猎](#wpa--eapol-4-way-handshake-hunting)
- [流跟踪和重组](#-stream-following--重组)
- [统计、对话和端点](#-statistics-conversations--endpoints)
- [TLS / SSL 流量解密](#-tls--ssl-traffic-decryption)
- [导出格式（JSON、CSV、Elasticsearch）](#-export-formats)
- [快速参考表](#-quick-reference-table)
- [提示和最佳实践](#-tips--best-practices)
- [法律免责声明](#-legal-disclaimer)

---

## 🎯 简介

**tshark** 是 **Wireshark** 的终端和命令行对应项。 It relies on the identical packet capture library (libpcap/Npcap) and packet dissection engine as the graphical Wireshark, making it one of the most powerful network analysis tools available in Linux, macOS, and Windows.

Unlike basic packet sniffer tools that only inspect protocol headers, `tshark` features deep packet inspection capabilities across thousands of protocols, native Wireshark display filter syntax (`-Y`), robust protocol field extraction (`-T fields`), and flexible output formatting for automation pipelines (`jq`, shell scripts, Python).

### 主要特点

|特色 |描述 |
|---------|-------------|
| **深度剖析** |检查数千个应用程序协议的有效负载数据 |
| **Wireshark 过滤器** |全面支持丰富的 Wireshark 显示过滤器语法 (`-Y`) |
| **字段提取** |直接提取单个协议字段（`-T fields -e ...`）|
| **直播关注** |以 ASCII、十六进制或原始格式重新组装完整的 TCP/UDP 会话 |
| **自动取证**|可在 shell、Python 中编写脚本，或通过 `-T json` 通过管道传输到 `jq` |
| **解密** |支持使用 `SSLKEYLOGFILE` 进行运行时 TLS 解密 |

---

## ⚖️ 工具比较

|特色 |沙克 | tcpdump | Wireshark |
|--------|--------|---------|------------|
| **界面** | CLI（终端）| CLI（终端）| GUI（图形）|
| **资源使用** |低/中|极简|更高（RAM 和 CPU）|
| **远程（SSH/无头）** |原生无头/CLI |本机 SSH 管道 |需要 X11 / 远程代理 |
| **剖析引擎** |完整的 Wireshark 解剖器 |基本标题 |完整的 Wireshark 解剖器 |
| **捕获过滤器** |仅 BPF (`-f`) |仅 BPF (`-i`) |仅 BPF |
| **显示过滤器** | ✅ 完整的 Wireshark 语法 (`-Y`) | ❌ 无 | ✅ 完整的 GUI 过滤栏 |
| **导出格式** | PCAP、JSON、CSV、弹性、XML | PCAP、ASCII、十六进制 | PCAP、CSV、XML、JSON |
| **脚本和自动化**| Shell、Python、jq |外壳/awk/sed | Lua 插件、GUI |

---

## 📥 安装和权限

### Linux（Debian / Ubuntu / Kali Linux）
```bash
# Update and install tshark
sudo apt update && sudo apt install -y tshark

# Verify installation and version
tshark --version
```
### 允许非根数据包捕获

为了防止将 `tshark` 作为 `root` 运行（由于解剖器表面巨大，这可能会带来安全风险），请将您的用户添加到 `wireshark` 组：
```bash
# Configure wireshark-common for non-superusers
sudo dpkg-reconfigure wireshark-common
# (Select "Yes" when prompted)

# Add your user to the wireshark group
sudo usermod -aG wireshark $USER

# Apply group changes to current session
newgrp wireshark
```
### macOS（自制）和 Windows（巧克力）
```bash
# macOS
brew install wireshark

# Windows
choco install wireshark
```
---

## 🚀 基本捕获和回放

### 列出可用接口
```bash
# List interfaces with numeric IDs
tshark -D
```
### 捕获实时数据包
```bash
# Capture packets on interface eth0
tshark -i eth0

# Capture only 100 packets and stop
tshark -i eth0 -c 100

# Capture and save traffic directly to a pcap file
tshark -i eth0 -w capture.pcap

# Capture with ring buffer (rotate files every 100MB, keep max 5 files)
tshark -i eth0 -b filesize:100000 -b files:5 -w rolling_traffic.pcap

# Capture with a duration trigger (capture for 60 seconds)
tshark -i eth0 -a duration:60 -w one_minute.pcap
```
### 读取 PCAP 文件
```bash
# Read and display packets from a pcap file
tshark -r capture.pcap

# Read PCAP without resolving IP addresses to DNS hostnames (speeds up processing)
tshark -r capture.pcap -n

# Suppress packet count summary upon completion
tshark -r capture.pcap -q
```
---

## 🔍 过滤流量：捕获与显示

`tshark` 采用两种不同的过滤引擎：

1. **捕获过滤器 (`-f`)：** Berkeley 数据包过滤器 (BPF) 在数据包到达用户空间之前由内核评估。高性能，但仅限于基本 IP/端口原语。
2. **显示过滤器 (`-Y`)：** 由 Wireshark 解析引擎评估的深层协议过滤器。支持应用层、字符串搜索、正则表达式和协议标志。
```bash
# Capture filter: Capture only TCP traffic to port 80 or 443
tshark -i eth0 -f "tcp port 80 or tcp port 443" -w web_traffic.pcap

# Display filter: Show only successful HTTP GET responses (Status 200)
tshark -r capture.pcap -Y "http.request.method == \"GET\" and http.response.code == 200"

# Display filter: Filter by specific subnet and exclude broadcast
tshark -r capture.pcap -Y "ip.addr == 192.168.1.0/24 and not eth.dst == ff:ff:ff:ff:ff:ff"

# Read-filter (-R): Single-pass filter used during capture
tshark -r capture.pcap -2 -R "dns.flags.response == 1"
```
---

## 📊 字段提取 (`-T fields`)

`-T fields` 模式是 `tshark` 最强大的功能，适用于自动侦察、脚本编写和 CTF 挑战。
```bash
# Basic syntax for field extraction
tshark -r capture.pcap -T fields -e <field1> -e <field2> -E header=y -E separator=,
```
### 密钥提取格式化标志

|旗帜|描述 |
|------|-------------|
| `-T fields` |将输出模式切换为自定义字段提取 |
| `-e <field>` |命名要提取的协议字段（可以重复）|
| `-E header=y` |将列名称打印为标题行 |
| `-E separator=,` |设置字段分隔符（CSV 为 `,`，TSV 为 `\t`）|
| `-E occurrence=f` |仅提取第一个 (`f`)、最后一个 (`l`) 或所有 (`a`) 出现的位置 |
| `-E quote=d`|将值括在双引号 | 中

### 核心协议字段速查表
```bash
# IP layer fields
ip.src, ip.dst, ip.proto, ip.ttl

# TCP layer fields
tcp.srcport, tcp.dstport, tcp.flags, tcp.stream, tcp.payload

# HTTP fields
http.host, http.request.method, http.request.uri, http.response.code, http.user_agent, http.cookie

# DNS fields
dns.qry.name, dns.qry.type, dns.a, dns.flags.response

# TLS fields
tls.handshake.extensions_server_name, tls.handshake.ciphersuite
```
---

## 🎯 实用渗透测试和法医食谱

### HTTP 凭证和 URI 搜索
```bash
# Extract all visited URLs and HTTP Hosts
tshark -r capture.pcap -Y "http.request" -T fields -e ip.src -e http.host -e http.request.method -e http.request.uri -E header=y

# Extract User-Agent strings across internal clients
tshark -r capture.pcap -Y "http.user_agent" -T fields -e ip.src -e http.user_agent | sort -u

# Hunt for HTTP POST data (logins, passwords, tokens)
tshark -r capture.pcap -Y "http.request.method == \"POST\"" -T fields -e ip.src -e ip.dst -e http.host -e http.file_data

# Hunt for HTTP Basic Authentication headers (Base64 encoded)
tshark -r capture.pcap -Y "http.authorization" -T fields -e ip.src -e http.authorization
```
### DNS 查询提取和隧道检测
```bash
# List all requested domain names (DNS queries)
tshark -r capture.pcap -Y "dns.flags.response == 0" -T fields -e ip.src -e dns.qry.name | sort -u

# Detect potential DNS tunneling (display long subdomains > 30 characters)
tshark -r capture.pcap -Y "dns.flags.response == 0" -T fields -e dns.qry.name | awk 'length($1) > 30' | sort -u

# Correlate DNS queries with their resolved IP addresses
tshark -r capture.pcap -Y "dns.flags.response == 1 and dns.a" -T fields -e dns.qry.name -e dns.a -E header=y
```
### TLS / HTTPS SNI 提取

即使 HTTPS 有效负载已加密，初始 TLS 客户端 Hello 握手中的 **服务器名称指示 (SNI)** 也会显示目标域名：
```bash
# Extract TLS Server Name Indication (SNI) hostnames
tshark -r capture.pcap -Y "tls.handshake.type == 1" -T fields -e ip.src -e ip.dst -e tls.handshake.extensions_server_name | sort -u
```
### Kerberos AS-REQ / AS-REP 提取
```bash
# Extract Kerberos domain client names (hunting for user accounts)
tshark -r capture.pcap -Y "kerberos.CNameString" -T fields -e ip.src -e kerberos.realm -e kerberos.CNameString | sort -u

# Hunt for Kerberos errors (e.g. KRB5KDC_ERR_PREAUTH_REQUIRED, bad passwords)
tshark -r capture.pcap -Y "kerberos.error_code" -T fields -e ip.src -e ip.dst -e kerberos.error_code
```
### SMB 和 NTLM 身份验证跟踪
```bash
# Extract SMB accessed file paths and shares
tshark -r capture.pcap -Y "smb2.filename" -T fields -e ip.src -e ip.dst -e smb2.filename | sort -u

# Extract NTLMSSP usernames and domain names during authentication
tshark -r capture.pcap -Y "ntlmssp.auth.username" -T fields -e ip.src -e ntlmssp.auth.domain -e ntlmssp.auth.username
```
### 明文协议审计
```bash
# Extract FTP usernames and passwords
tshark -r capture.pcap -Y "ftp.request.command == \"USER\" or ftp.request.command == \"PASS\"" -T fields -e ip.src -e ftp.request.command -e ftp.request.arg

# Extract Telnet session input
tshark -r capture.pcap -Y "telnet" -T fields -e telnet.data
```
### WPA / EAPOL 4 路握手狩猎
```bash
# Filter for 802.11 EAPOL handshakes to crack with Hashcat/Aircrack-ng
tshark -r wifi.pcap -Y "eapol" -w handshakes_only.pcap
```
---

## 🧵 流跟踪和重组

`tshark` 允许重新组装整个 TCP 和 UDP 流，而无需 Wireshark 的 GUI。
```bash
# List all TCP conversations to find stream IDs
tshark -r capture.pcap -q -z conv,tcp

# Follow TCP stream 0 in ASCII format
tshark -r capture.pcap -q -z follow,tcp,ascii,0

# Follow TCP stream 2 in Hexdump format
tshark -r capture.pcap -q -z follow,tcp,hex,2

# Follow HTTP stream (stream index 1)
tshark -r capture.pcap -q -z follow,http,ascii,1
```
---

## 📈 统计、对话和端点
```bash
# Protocol hierarchy statistics (breakdown of all protocols in PCAP)
tshark -r capture.pcap -q -z io,phs

# IP conversations table (who talked to whom, packet count, bytes)
tshark -r capture.pcap -q -z conv,ip

# Port conversation statistics (TCP ports)
tshark -r capture.pcap -q -z conv,tcp

# List all unique IP endpoints in PCAP
tshark -r capture.pcap -q -z endpoints,ip

# HTTP request statistics tree
tshark -r capture.pcap -q -z http,tree

# Wireshark Expert Info warnings and errors
tshark -r capture.pcap -q -z expert
```
---

## 🔐 TLS / SSL 流量解密

如果您有预主密钥（通过浏览器 `SSLKEYLOGFILE` 导出或在恶意软件沙箱期间检索）：
```bash
# Decrypt TLS traffic on the fly using keylog file
tshark -r https_traffic.pcap -o "tls.keylog_file:/path/to/sslkeys.log" -Y "http" -T fields -e http.host -e http.request.uri

# Follow decrypted HTTP stream inside TLS
tshark -r https_traffic.pcap -o "tls.keylog_file:/path/to/sslkeys.log" -q -z follow,http,ascii,0
```
---

## 📦 导出格式
```bash
# Output as structured JSON (ideal for piping into jq or Python)
tshark -r capture.pcap -c 5 -T json

# Extract specific JSON field with jq
tshark -r capture.pcap -T json | jq '.[]._source.layers.ip["ip.src"]'

# Output in Elasticsearch Bulk Format (-T ek)
tshark -r capture.pcap -T ek > packets_for_elastic.json

# Output as CSV table
tshark -r capture.pcap -T fields -e frame.number -e ip.src -e ip.dst -e _ws.col.Protocol -E header=y -E separator=, > traffic.csv
```
---

## ⚡ 快速参考表

|任务|命令 |
|------|---------|
| **捕获实时流量** | `tshark -i eth0 -w output.pcap` |
| **使用 BPF 滤波器捕获** | `tshark -i eth0 -f "port 80" -w http.pcap` |
| **无需 DNS 即可读取 PCAP** | `tshark -r capture.pcap -n` |
| **应用显示过滤器** | `tshark -r capture.pcap -Y "http.response.code >= 400"` |
| **提取 IP 和端口** | `tshark -r capture.pcap -T fields -e ip.src -e tcp.srcport -e ip.dst -e tcp.dstport` |
| **提取 DNS 查询** | `tshark -r capture.pcap -Y "dns.flags.response == 0" -T fields -e dns.qry.name` |
| **提取 TLS SNI 主机名**| `tshark -r capture.pcap -Y "tls.handshake.type == 1" -T fields -e tls.handshake.extensions_server_name` |
| **提取 HTTP URL** | `tshark -r capture.pcap -Y "http.request" -T fields -e http.host -e http.request.uri` |
| **遵循 TCP 流** | `tshark -r capture.pcap -q -z follow,tcp,ascii,0` |
| **协议层次结构** | `tshark -r capture.pcap -q -z io,phs` |
| **使用键盘日志解密** | `tshark -r traffic.pcap -o "tls.keylog_file:keys.log"`|
| **导出为 JSON** | `tshark -r capture.pcap -T json > packets.json`|

---

## 💡 提示和最佳实践

1. **对于大型 PCAP 始终使用 `-n`：** DNS 名称解析会显着减慢解析速度。添加 `-n` 以防止解析 IP 地址。
2. **组合 `-f` 和 `-Y`：** 在实时捕获期间使用 `-f` 捕获过滤器以减少数据包丢失，并在后分析期间使用 `-Y` 显示过滤器以进行深度协议检查。
3. **与 `jq` 进行管道传输：** 将 `tshark -T json` 管道化到 `jq` 允许查询具有完整布尔逻辑和转换的嵌套协议字段。
4. **用于 24/7 嗅探的环形缓冲区：** 在枢轴主机上运行连续网络捕获时，始终强制执行大小和文件限制 (`-b filesize:50000 -b files:10`) 以防止目标磁盘崩溃。

---

## ⚠️ 法律免责声明

> **重要提示：** 未经授权的数据包捕获、窃听网络流量以及在未经明确、书面许可的情况下拦截网络上的凭据均属非法行为，并可能受到刑事起诉。严格在您拥有或合同授权评估的系统和网络上使用 `tshark`。