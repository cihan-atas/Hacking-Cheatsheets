<!-- language-home-navigation -->
[🏠 中文主页](../../README.zh-CN.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ⚡ Masscan - 完整备忘单

[🇹🇷 Türkçe Sürüm]（翻译/README.tr.md）
```
  ███╗   ███╗ █████╗ ███████╗███████╗ ██████╗ █████╗ ███╗   ██╗
  ████╗ ████║██╔══██╗██╔════╝██╔════╝██╔════╝██╔══██╗████╗  ██║
  ██╔████╔██║███████║███████╗███████╗██║     ███████║██╔██╗ ██║
  ██║╚██╔╝██║██╔══██║╚════██║╚════██║██║     ██╔══██║██║╚██╗██║
  ██║ ╚═╝ ██║██║  ██║███████║███████║╚██████╗██║  ██║██║ ╚████║
  ╚═╝     ╚═╝╚═╝  ╚═╝╚══════╝╚══════╝ ╚═════╝╚═╝  ╚═╝╚═╝  ╚═══╝
       The Fastest Internet Port Scanner - Asynchronous 10M Packets/Second
```
<p对齐=“中心”>
  <img src="https://img.shields.io/badge/Masscan-Internet_Port_Scanner-blue?style=for-the-badge" alt="Masscan">
  <img src="https://img.shields.io/badge/Language-C-orange?style=for-the-badge" alt="语言 C">
  <img src="https://img.shields.io/badge/Category-Recon_&_Scanning-green?style=for-the-badge" alt="侦察与扫描">
  <img src="https://img.shields.io/badge/License-AGPLv3-red?style=for-the-badge" alt="许可证 AGPLv3">
</p>

<p对齐=“中心”>
  <b>🌐高性能异步 TCP 端口扫描器，能够在 5 分钟内扫描整个 IPv4 互联网</b>
</p>

---

## 📋 目录

- [概述和异步架构](#-overview--the-asynchronous-architecture)
- [工具比较：Masscan vs Nmap vs RustScan](#-tool-comparison-masscan-vs-nmap-vs-rustscan)
- [安装](#-安装)
- [基本用法和扫描目标](#-basic-usage--scanning-targets)
- [传输速率控制(`--rate`)](#-传输速率控制---速率)
- [端口规范（TCP 和 UDP）](#-port-specations-tcp--udp)
- [横幅抓取和基本协议探测](#-banner-grabbing--basic-protocol-probing)
- [目标排除和黑名单 (`--excludefile`)](#-target-exclusions--blacklists---excludefile)
- [输出格式和管道传输到 Nmap](#-output-formats--piping-into-nmap)
- [快速参考表](#-quick-reference-table)
- [防御考虑因素和硬件限制](#-defective-considerations--hardware-limits)
- [法律免责声明](#-legal-disclaimer)

---

## 🎯 概述和异步架构

**Masscan** 由 **Robert Graham** 创建，被誉为现有最快的端口扫描仪。它产生的结果与 Nmap 类似，但内部功能类似于 `scanrand`、`unicornscan` 和 `ZMap`，利用**异步传输**。

与 Nmap 使用操作系统的内核 TCP 堆栈并在探测后续端口之前等待响应不同，Masscan 合并了自己的自定义用户空间 TCP/IP 堆栈。它以超过**每秒 1000 万个数据包**的速率爆炸原始 SYN 数据包，而无需维护连接状态表。

---

## ⚖️ 工具比较：Masscan、Nmap、RustScan

|特色 |马斯坎|地图 | Rust 扫描 |
|---------|---------|------|----------|
| **最大数据包速率** | 10,000,000 个包/秒 | ~1,000 个包/秒 |操作系统套接字限制 |
| **整个互联网扫描 (/0)**| ✅ 5 分钟以内 | ❌ 天/周 | ❌ 不是为 /0 设计的 |
| **TCP 堆栈实现**|自定义用户空间原始 SYN|操作系统内核/原始套接字|异步 Tokio TCP 套接字 |
| **深层版本和 NSE 脚本**| ❌ 原始横幅| ✅ 600+ NSE 脚本 | ✅ 通过 Nmap 切换 |
| **状态跟踪** |无状态（Blackrock PRNG）|有状态连接池|有状态异步任务 |
| **理想目标范围** | A/B 级块（/8、/16）|单一目标和 /24 个子网 |单机&/24个子网|

---

## 📥 安装
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y masscan

# Arch Linux
sudo pacman -S masscan

# Build from source (Latest GitHub version)
sudo apt install -y git gcc make libpcap-dev
git clone https://github.com/robertdavidgraham/masscan
cd masscan && make -j$(nproc)
sudo make install
```
---

## 🚀 基本使用和扫描目标
```bash
# Scan a single IP for top web ports
sudo masscan 10.10.10.50 -p80,443,8080 --rate 1000

# Scan an entire Class B internal subnet (172.16.0.0/16) for SMB port 445
sudo masscan 172.16.0.0/16 -p445 --rate 5000

# Scan all 65,535 ports on a specific IP
sudo masscan 10.10.10.50 -p1-65535 --rate 2000
```
> [!重要]
> 由于 Masscan 通过 `libpcap` 生成原始数据包，因此它**必须以 root / sudo 权限运行**。

---

## ⚡ 传输速率控制 (`--rate`)

`--rate` 参数指示 Masscan 每秒传输的数据包数量。校准这一点对于避免网络设备饱和至关重要：
```bash
# Gentle rate for home routers / WiFi (100–500 pkts/sec)
sudo masscan 10.10.10.0/24 -p1-1000 --rate 300

# Standard corporate gigabit internal network rate (5,000–10,000 pkts/sec)
sudo masscan 10.0.0.0/8 -p80,443 --rate 10000

# High-speed data center 10Gbps uplink rate (100,000+ pkts/sec)
sudo masscan 0.0.0.0/0 -p443 --rate 100000
```
---

## 🔌 端口规范（TCP 和 UDP）

Masscan 同时支持 TCP 和 UDP 端口列表：
```bash
# Specific TCP ports
sudo masscan 10.10.10.50 -p21,22,80,443,445,3389

# Top ports shortcut (similar to Nmap)
sudo masscan 10.10.10.50 --top-ports 100

# UDP ports (prefixed with U:)
sudo masscan 10.10.10.50 -p U:53,U:161,U:123 --rate 500
```
---

## 🔍 横幅抓取和基本协议探测

虽然 Masscan 主要是一个 SYN 扫描器，但它可以完成三向握手来抓取应用程序横幅：
```bash
# Grab HTTP, FTP, and SSH banners
sudo masscan 10.10.10.0/24 -p80,21,22 --banners --rate 1000
```
---

## 🚫 目标排除和黑名单 (`--excludefile`)

在进行大范围渗透测试时，请始终排除关键基础设施和禁区地址：
```bash
# Exclude government and military ranges or local gateway
sudo masscan 10.0.0.0/8 -p80 --rate 2000 --exclude 10.0.0.1,10.1.1.1

# Use an exclusion file (e.g., standard exclude.conf)
sudo masscan 192.168.0.0/16 -p445 --rate 2000 --excludefile /etc/masscan/exclude.conf
```
---

## 📊 输出格式和管道传输到 Nmap

以 grepable 或 JSON 格式保存发现的主机，然后将它们输入 Nmap 进行完整的漏洞评估：
```bash
# Save to JSON format
sudo masscan 10.10.10.0/24 -p80,443 -oJ masscan_results.json

# Save to simple List format (-oL)
sudo masscan 10.10.10.0/24 -p1-65535 --rate 2000 -oL open_ports.txt

# Extract discovered ports and feed directly to Nmap
cat open_ports.txt | awk '{print $4}' | sort -u | paste -sd, - | xargs -I{} nmap -sC -sV -p{} 10.10.10.50
```
---

## ⚡ 快速参考表

|目标|命令 |
|------------|---------|
| **扫描单个主机（所有端口）** | `sudo masscan <IP> -p1-65535 --rate 2000` |
| **扫描 CIDR 子网的端口** | `sudo masscan <SUBNET>/16 -p<PORT> --rate 5000` |
| **使用横幅抓取进行扫描** | `sudo masscan <IP> -p80,22 --banners --rate 1000` |
| **扫描 UDP 端口** | `sudo masscan <IP> -p U:53,U:161 --rate 500` |
| **排除 IP 地址** | `sudo masscan <NET> -p80 --exclude <IP1>,<IP2>` |
| **输出为 JSON** | `sudo masscan <IP> -p80,443 -oJ output.json` |
| **输出到简单列表** | `sudo masscan <IP> -p80,443 -oL output.txt` |

---

## 🛡️ 防御考虑和硬件限制

1. **消费者路由器崩溃：** 在家庭 DSL/有线路由器或标准 Wi-Fi 上设置 `--rate 100000` 将淹没其 NAT 连接跟踪状态表并使路由器崩溃。在住宅连接上，将 `--rate` 保持在 `1000` 以下。
2. **带宽消耗：** 以每秒 1000 万个数据包的速度传输大约消耗 5.4 Gbps 的网络带宽。
3. **本地防火墙拒绝 (RST)：** 由于 Masscan 使用自定义用户空间 TCP 堆栈，因此您的 Linux 内核不知道打开的连接，并且可能会自动向目标发送 TCP RST 数据包。为了避免在横幅抓取过程中出现这种情况：
   ```bash
   sudo iptables -A INPUT -p tcp --dport 60000 -j DROP
   sudo masscan ... --source-port 60000
   ```
---

## ⚠️ 法律免责声明

> [!警告]
> Masscan 是一款功能异常强大的网络侦察实用程序。扫描未经授权的网络、政府 IP 范围或公共互联网子网可能违反电信和网络安全法。在开始扫描之前务必验证书面授权。