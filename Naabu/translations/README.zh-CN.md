<!-- language-home-navigation -->
[🏠 中文主页](../../README.zh-CN.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ⚡ Naabu - 完整备忘单

[🇹🇷 Türkçe Sürüm]（翻译/README.tr.md）
```
  ███╗   ██╗ █████╗  █████╗ ██████╗ ██╗   ██╗
  ████╗  ██║██╔══██╗██╔══██╗██╔══██╗██║   ██║
  ██╔██╗ ██║███████║███████║██████╔╝██║   ██║
  ██║╚██╗██║██╔══██║██╔══██║██╔══██╗██║   ██║
  ██║ ╚████║██║  ██║██║  ██║██████╔╝╚██████╔╝
  ╚═╝  ╚═══╝╚═╝  ╚═╝╚═╝  ╚═╝╚═════╝  ╚═════╝ 
        Fast & Reliable SYN/CONNECT Port Scanner
```
<p对齐=“中心”>
  <img src="https://img.shields.io/badge/Naabu-Port_Scanner-blue?style=for-the-badge" alt="Naabu">
  <img src="https://img.shields.io/badge/Author-ProjectDiscovery-orange?style=for-the-badge" alt="ProjectDiscovery">
  <img src="https://img.shields.io/badge/Category-Recon_&_Scanning-green?style=for-the-badge" alt="侦察与扫描">
  <img src="https://img.shields.io/badge/Language-Go-cyan?style=for-the-badge" alt="Go">
</p>

<p对齐=“中心”>
  <b>⚡快速可靠的端口扫描器，专注于使用 ProjectDiscovery 工具（httpx、nuclei）的简单性和无缝管道</b>
</p>

---

## 📋 目录

- [概述和主要功能](#-概述--主要功能)
- [安装](#-安装)
- [基本端口扫描](#-basic-port-scanning)
  - [单主机扫描](#single-host-scan)
  - [热门端口和完整端口范围](#top-ports--full-port-range)
- [扫描模式：SYN（原始套接字）与 CONNECT](#-scan-modes-syn-raw-socket-vs-connect)
- [速率限制和性能调整](#-rate-limiting--performance-tuning)
- [多目标扫描和 CIDR 块](#-multi-target-scanning--cidr-blocks)
- [无缝管道工作流程](#-seamless-pipeline-workflows)
  - [Naabu + Httpx + Nuclei](#naabu--httpx--nuclei)
  - [Subfinder + Naabu](#subfinder--naabu)
- [Nmap 集成 (`-nmap`)](#-nmap-integration--nmap)
- [快速参考表](#-quick-reference-table)
- [防守考虑与检测](#-defective-considerations--detection)
- [法律免责声明](#-legal-disclaimer)

---

## 🎯 概述和主要特点

**Naabu** 由 **ProjectDiscovery** 开发，是一款端口扫描仪，旨在快速、准确且经过优化，可集成到现代侦察自动化管道中：
- **SYN 和 CONNECT 扫描：** 当原始套接字权限（`libpcap` / root）不存在时，自动回退到 TCP CONNECT。
- **ProjectDiscovery 生态系统：** 原生管道架构允许从 `subfinder` 到 `naabu`，然后到 `httpx` 和 `nuclei` 的无缝管道。
- **集成 Nmap 引擎：** 能够在发现的开放端口上自动触发活动 Nmap 服务和版本扫描。
- **CDN 排除：** 自动绕过云 CDN 边缘节点（Cloudflare、Akamai、CloudFront），以防止带宽浪费和误报。

---

## 📥 安装
```bash
# Using Go (Recommended)
go install -v github.com/projectdiscovery/naabu/v2/cmd/naabu@latest

# Binary download via pdtm (ProjectDiscovery Tool Manager)
pdtm -i naabu

# Arch Linux (AUR)
yay -S naabu-bin

# Kali Linux
sudo apt update && sudo apt install -y naabu
```
> [!注意]
> 在 Linux 上，授予 `CAP_NET_RAW` 允许在没有 root 的情况下运行 SYN 扫描：
> `sudo setcap cap_net_raw=+ep $(which naabu)`

---

## 🚀 基本端口扫描

### 单主机扫描
```bash
# Scan default top 100 ports
naabu -host example.com

# Scan specific ports
naabu -host example.com -p 80,443,8080,8443
```
### 顶级端口和完整端口范围
```bash
# Scan top 1000 ports
naabu -host example.com -top-ports 1000

# Full port scan (1-65535)
naabu -host example.com -p -
```
---

## ⚡ 扫描模式：SYN（原始套接字）与 CONNECT

- **SYN 扫描（需要 `-s syn`/root）：** 发送原始 TCP SYN 数据包，而不完成 3 路握手（半开扫描）。应用程序日志中的速度更快且噪音更少。
- **CONNECT 扫描 (`-s conn`)：** 使用标准 OS `connect()` 套接字调用。无需 root 权限即可运行，并可穿透限制性虚拟环境。
```bash
# Explicit SYN Scan (Requires sudo or CAP_NET_RAW)
sudo naabu -host example.com -s syn

# Explicit TCP Connect Scan (No root required)
naabu -host example.com -s conn
```
---

## ⚙️ 速率限制和性能调整
```bash
# Adjust packet rate per second (default: 1000)
sudo naabu -host example.com -rate 2500

# Set connection timeout in milliseconds
naabu -host example.com -timeout 1000

# Exclude CDN edge servers (Cloudflare, CloudFront, Fastly)
naabu -host example.com -exclude-cdn
```
---

## 🌐 多目标扫描和 CIDR 块
```bash
# Scan targets from list
naabu -list targets.txt -p 80,443,8080 -o open_ports.txt

# Scan a /24 subnet block
sudo naabu -host 192.168.1.0/24 -p 22,80,443,445,3389
```
---

## 🔄 无缝管道工作流程

Naabu 在与其他 ProjectDiscovery 工具进行管道传输时表现出色：

### 辅助查找器 + Naabu
枚举子域并立即发现哪些子域具有开放的 Web/SSH 端口：
```bash
subfinder -d example.com -silent | naabu -silent -p 80,443,8080,8443
```
### Naabu + Httpx + Nuclei
规范的攻击性错误赏金侦察管道：
```bash
# 1. Port scan subdomains -> 2. Probe HTTP services -> 3. Vulnerability scan
subfinder -d target.com -silent \
  | naabu -silent -p 80,443,8080,8443 \
  | httpx -silent \
  | nuclei -severity medium,high,critical -o findings.txt
```
---

## 🔍 Nmap 集成 (`-nmap`)

将发现的开放端口直接传递给 Nmap 进行服务检测（`-sV`）和脚本扫描（`-sC`）：
```bash
sudo naabu -host target.local -top-ports 1000 -nmap-cli 'nmap -sV -sC -oN naabu_nmap.txt'
```
---

## ⚡ 快速参考表

|目标|命令 |
|------------|---------|
| **默认扫描（前 100 名）** | `naabu -host <TARGET>` |
| **全范围扫描 (1-65535)**| `naabu -host <TARGET> -p -` |
| **前 1000 个端口** | `naabu -host <TARGET> -top-ports 1000` |
| **SYN 扫描（快速、隐蔽）**| `sudo naabu -host <TARGET> -s syn` |
| **排除 CDN 边缘 IP** | `naabu -host <TARGET> -exclude-cdn` |
| **输出为 JSON** | `naabu -host <TARGET> -json -o results.json` |
| **管道到 HTTPX** | `naabu -host <TARGET> -silent \| httpx` |
| **触发Nmap服务扫描**| `sudo naabu -host <TARGET> -nmap-cli 'nmap -sV'` |

---

## 🛡️ 防守考虑和检测

1. **防火墙和 IDS 规则：** Naabu 生成的高速率 SYN 扫描会产生高 TCP RST/SYN 比率，很容易被 Snort/Suricata 规则捕获。
2. **网络速率限制：** 强制入站连接速率限制 (`iptables -m limit --limit 50/s`) 以减慢并中断自动扫描。

---

## ⚠️ 法律免责声明

> [!警告]
> Naabu 严格针对授权安全评估、渗透测试和防御资产发现而开发。未经明确许可，严禁扫描网络。