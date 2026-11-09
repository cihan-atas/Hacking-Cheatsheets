<!-- language-home-navigation -->
[🏠 中文主页](../../README.zh-CN.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🦀 RustScan - 完整备忘单

[🇹🇷 Türkçe Sürüm]（翻译/README.tr.md）
```
  ██████╗ ██╗   ██╗███████╗████████╗███████╗ ██████╗ █████╗ ███╗   ██╗
  ██╔══██╗██║   ██║██╔════╝╚══██╔══╝██╔════╝██╔════╝██╔══██╗████╗  ██║
  ██████╔╝██║   ██║███████╗   ██║   ███████╗██║     ███████║██╔██╗ ██║
  ██╔══██╗██║   ██║╚════██║   ██║   ╚════██║██║     ██╔══██║██║╚██╗██║
  ██║  ██║╚██████╔╝███████║   ██║   ███████║╚██████╗██║  ██║██║ ╚████║
  ╚═╝  ╚═╝ ╚═════╝ ╚══════╝   ╚═╝   ╚══════╝ ╚═════╝╚═╝  ╚═╝╚═╝  ╚═══╝
       The Modern Port Scanner - Scan All 65k Ports in 3 Seconds into Nmap
```
<p对齐=“中心”>
  <img src="https://img.shields.io/badge/RustScan-Port_Scanner-blue?style=for-the-badge" alt="RustScan">
  <img src="https://img.shields.io/badge/Language-Rust-orange?style=for-the-badge" alt="Language Rust">
  <img src="https://img.shields.io/badge/Category-Recon_&_Scanning-green?style=for-the-badge" alt="侦察与扫描">
  <img src="https://img.shields.io/badge/License-GPLv3-red?style=for-the-badge" alt="许可证 GPLv3">
</p>

<p对齐=“中心”>
  <b>🌐快速、可定制和自动化的端口扫描器，旨在将发现的开放端口直接传输到 Nmap 脚本中</b>
</p>

---

## 📋 目录

- [概述与架构](#-overview--architecture)
- [工具比较：RustScan vs Nmap vs Masscan](#-tool-comparison-rustscan-vs-nmap-vs-masscan)
- [安装](#-安装)
- [基本用法&单主机扫描](#-basic-usage--single-host-scanning)
- [自动 Nmap 集成](#-automated-nmap-integration)
- [性能调整（`ulimit` 和批量大小）](#-performance-tuning-ulimit--batch-size)
- [扫描端口范围和 CIDR 子网](#-scanning-port-ranges--cidr-subnets)
- [脚本和输出格式](#-scripting--output-formatting)
- [快速参考表](#-quick-reference-table)
- [防御考虑因素和 OPSEC](#-defective-considerations--opsec)
- [法律免责声明](#-legal-disclaimer)

---

## 🎯 概述与架构

**RustScan** 是一款采用 **Rust** 设计的现代高速端口扫描仪。其核心理念很简单：**快速扫描端口，在 3 秒内找到整个 1-65535 频谱的开放端口，并立即将这些开放端口移交给 Nmap 进行全面的服务和漏洞扫描**。

RustScan 无需等待 10-20 分钟让 `nmap -p-` 完成其顺序 TCP 握手扫描，而是利用异步多线程和操作系统套接字批处理几乎立即查明开放端口。

---

## ⚖️ 工具比较：RustScan、Nmap、Masscan

|特色 | Rust 扫描 |地图 |马斯坎|
|---------|----------|------|---------|
| **主要目标** |快速发现 -> Nmap 管道 |深度服务和 NSE 脚本 |高速互联网范围 SYN 扫描 |
| **全端口速度 (1-65535)**| ⭐⭐⭐⭐⭐（约 3 秒）| ⭐⭐（5-20​​ 分钟）| ⭐⭐⭐⭐⭐（约 2 秒）|
| **服务版本检测**|通过 Nmap 管道 |本机 (`-sV`) | ❌ 最小 |
| **NSE 脚本引擎** |通过 Nmap 管道 |本机（`-sC`、`--script`）| ❌ 否 |
| **数据包引擎** |异步 TCP 套接字 |原始 IP / 自定义 TCP 堆栈 |自定义异步 SYN 堆栈 |
| **易于自动化** |本机 CLI 参数 (`--`) |广泛的 CLI 选项 |高复杂度|

---

## 📥 安装
```bash
# Debian / Ubuntu / Kali Linux (.deb package)
wget https://github.com/RustScan/RustScan/releases/download/2.3.0/rustscan_2.3.0_amd64.deb
sudo dpkg -i rustscan_2.3.0_amd64.deb

# Arch Linux (AUR)
yay -S rustscan

# Cargo (Rust Package Manager)
cargo install rustscan

# Docker Container
docker run -it --rm --name rustscan rustscan/rustscan:2.1.1 -a 10.10.10.50
```
---

## 🚀 基本使用和单主机扫描
```bash
# Scan all 65,535 ports on a single IP
rustscan -a 10.10.10.50

# Scan with greppable one-line output
rustscan -a 10.10.10.50 --greppable

# Quiet mode (suppresses the ASCII banner)
rustscan -q -a 10.10.10.50
```
---

## 🔗 自动 Nmap 集成

RustScan 最强大的功能是使用尾部 `--` 分隔符将开放端口直接传递到 Nmap：
```bash
# Scan target and execute default safe scripts & version detection on open ports
rustscan -a 10.10.10.50 -- -sC -sV

# Pass custom Nmap NSE scripts and output options
rustscan -a 10.10.10.50 -- -sC -sV -oN nmap_report.txt

# Run vulnerability audit with Nmap Vuln scripts
rustscan -a 10.10.10.50 -- --script vuln -oA target_vulns
```
---

## ⚡ 性能调优（`ulimit` 和批量大小）

RustScan 依赖于操作系统的文件描述符限制。默认情况下，它会自动调整，但手动调整可提供最佳性能：
```bash
# Increase batch size and socket ulimit for high-speed local networks
rustscan -a 10.10.10.50 -b 5000 -u 5000

# Lower batch size for high-latency or packet-dropping WiFi networks
rustscan -a 10.10.10.50 -b 500 --scan-order "random"
```
---

## 🌐 扫描端口范围和 CIDR 子网
```bash
# Scan a specific port range instead of all ports
rustscan -a 10.10.10.50 -r 1-1000

# Scan multiple IP addresses
rustscan -a 10.10.10.50,10.10.10.51

# Scan an entire CIDR subnet range
rustscan -a 192.168.1.0/24
```
---

## 📊 脚本编写和输出格式
```bash
# Output discovered open ports in JSON format
rustscan -a 10.10.10.50 --accessible

# Read target hosts from a file
rustscan -a 'hosts.txt'
```
---

## ⚡ 快速参考表

|目标 |命令|
|------------|---------|
| **扫描所有 65k 端口** | `rustscan -a <IP>` |
| **扫描所有端口 + Nmap 脚本**| `rustscan -a <IP> -- -sC -sV` |
| **扫描 CIDR 子网** | `rustscan -a <SUBNET>/24` |
| **扫描端口范围** | `rustscan -a <IP> -r 1-1000` |
| **调整批量大小和 ulimit** | `rustscan -a <IP> -b 3000 -u 3000` |
| **安静模式（无横幅）** | `rustscan -q -a <IP>` |
| **Greppable 输出** | `rustscan -a <IP> --greppable` |
| **导出到 Nmap 文件** | `rustscan -a <IP> -- -sC -sV -oN nmap.txt` |

---

## 🛡️ 防御考虑和 OPSEC

1. **大量噪音和 IDS 警报：** 在 3 秒内扫描 65k 端口，瞬间生成数千个 TCP SYN 数据包。它将立即触发 Snort、Suricata 和防火墙速率限制。
2. **SYN 洪泛和丢包：** 在不稳定的 VPN 链路（例如 HackTheBox、TryHackMe）上，高批量大小 (`-b 4000`) 可能会导致数据包丢失。如果结果看起来不一致，请将批量大小降低至 `-b 1000`。

---

## ⚠️ 法律免责声明

> [!警告]
> 本备忘单仅用于授权的渗透测试和教育研究。严禁未经授权扫描网络。