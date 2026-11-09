<!-- language-home-navigation -->
[🏠 中文主页](../../README.zh-CN.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🐶 Snaffler - 完整备忘单

[🇹🇷 Türkçe Sürüm]（翻译/README.tr.md）
```
  ███████╗███╗   ██╗ █████╗ ███████╗███████╗██╗     ███████╗██████╗ 
  ██╔════╝████╗  ██║██╔══██╗██╔════╝██╔════╝██║     ██╔════╝██╔══██╗
  ███████╗██╔██╗ ██║███████║█████╗  █████╗  ██║     █████╗  ██████╔╝
  ╚════██║██║╚██╗██║██╔══██║██╔══╝  ██╔══╝  ██║     ██╔══╝  ██╔══██╗
  ███████║██║ ╚████║██║  ██║██║     ██║     ███████╗███████╗██║  ██║
  ╚══════╝╚═╝  ╚═══╝╚═╝  ╚═╝╚═╝     ╚═╝     ╚══════╝╚══════╝╚═╝  ╚═╝
       Active Directory Domain SMB Share Credential & Secret Hunter
```
<p对齐=“中心”>
  <img src="https://img.shields.io/badge/Snaffler-Secret_Hunter-blue?style=for-the-badge" alt="Snaffler">
  <img src="https://img.shields.io/badge/Language-C%23_/_NET-orange?style=for-the-badge" alt="C# / .NET">
  <img src="https://img.shields.io/badge/Category-Active_Directory_&_Network-green?style=for-the-badge" alt="Active Directory">
  <img src="https://img.shields.io/badge/License-GPLv3-red?style=for-the-badge" alt="许可证 GPLv3">
</p>

<p对齐=“中心”>
  <b>🌐 自动化 Windows 域工具，旨在抓取 SMB 共享、查找有趣的配置文件、KeePass 数据库和纯文本凭据</b>
</p>

---

## 📋 目录

- [概述和份额狩猎问题](#-overview--the-share-hunting-problem)
- [Snaffler 的工作原理](#-how-snaffler-works)
- [安装和执行选项](#-installation--execution-options)
- [Snaffler 分类和严重性级别](#-snaffler-triage--severity-levels)
- [常用扫描工作流程](#-common-scanning-workflows)
  - [全域共享抓取](#domain-wide-share-crawl)
  - [定位特定主机或共享](#targeting-specific-hosts-or-shares)
  - [不包括计算机和 DFS 根](#exclusion-computers--dfs-roots)
- [Juicy 目标和文件匹配规则](#-juicy-targets--file-match-rules)
- [输出记录和分析](#-output-logging--analysis)
- [快速参考表](#-quick-reference-table)
- [防御考虑因素和 OPSEC](#-defective-considerations--opsec)
- [法律免责声明](#-legal-disclaimer)

---

## 🎯 概述和分享搜索问题

在大中型 Active Directory 环境中，企业文件服务器和工作站托管数千个开放 SMB 网络共享。系统管理员、开发人员和员工经常会留下：

- 🔑 **KeePass 数据库 (`.kdbx`)：** 通常持有主密码或易于破解的主密钥。
- ⚙️ **配置文件（`web.config`、`appsettings.json`）：** 包含硬编码的 SQL 连接字符串、服务帐户密码和 API 密钥。
- 📜 **部署脚本（`unattend.xml`、`.ps1`、`.bat`）：** 硬编码的管理凭据。
- 🔏 **私有证书（`.pfx`、`.p12`、`.pem`、`.ppk`）：** 可用于权限升级或横向移动。

手动浏览数千个分享是不可能的。 **Snaffler** 由 **l33tb455** 开发，可自动执行整个过程：它查询 Active Directory 中的计算机对象、枚举可读的 SMB 共享、遍历目录树以及根据策划的正则表达式规则集评估文件。

---

## ⚙️ Snaffler 的工作原理
```
[ Compromised Domain Machine ]
         │
         ├── 1. Queries Domain Controller via LDAP for Computer Objects
         ├── 2. Connects to port 445 (SMB) across all discovered hosts
         ├── 3. Enumerates Read/Write permissions on C$, SYSVOL, NETLOGON, Custom Shares
         └── 4. Evaluates file extensions, file names, and file contents against rules
```
---

## 📥 安装和执行选项

Snaffler 被编译为独立的 .NET 二进制文件：
```bash
# Clone and build using Visual Studio or dotnet CLI on Windows
git clone https://github.com/SnaffCon/Snaffler.git
cd Snaffler
dotnet build -c Release
```
### 在 Windows 上运行：
```cmd
# Standard execution outputting to screen and log file
Snaffler.exe -s -o snaffler_log.txt
```
### 通过 Cobalt Strike / C2 运行（执行组装）：
```text
beacon> execute-assembly C:\Tools\Snaffler.exe -s -o C:\Temp\loot.txt
```
---

## 🚦 Snaffler 分类和严重性级别

Snaffler 将调查结果按颜色编码的紧急程度进行分类：

- 🔴 **`{Red}` / 黑色：** 绝对的高价值发现！明文密码、私钥（`.pfx`、`.id_rsa`）、AWS 令牌、无人值守安装文件。
- 🟡 **`{Yellow}`：** 可能的发现。包含密码字符串、数据库连接字符串、卷影副本的配置文件。
- 🟢 **`{Green}`：** 信息/兴趣。与管理模式匹配的文件名（`passwords.xlsx`、`vpn.ovpn`）。

---

## 🚀 常见扫描工作流程

### 域范围共享抓取

扫描整个 Active Directory 域中的所有可访问共享：
```cmd
# Run with live console streaming (-s) and save to disk
Snaffler.exe -s -o C:\loot\snaffler_domain.txt

# Run with multi-threading tuning (Default: 60 threads)
Snaffler.exe -s -t 100 -o C:\loot\snaffler_fast.txt
```
### 针对特定主机或共享

当您想要避免扫描整个域并专注于文件服务器或域控制器时：
```cmd
# Scan a single targeted file server
Snaffler.exe -c fs01.corp.local -s

# Scan a comma-separated list of machines
Snaffler.exe -c dc01.corp.local,fs02.corp.local,sql01.corp.local -s

# Scan a specific shared folder path directly
Snaffler.exe -i \\fs01.corp.local\Departments\IT\ -s
```
### 排除计算机和 DFS 根

避免扫描速度慢或敏感的计算机：
```cmd
# Exclude specific backup or surveillance servers
Snaffler.exe -s -c- backup01.corp.local,cctv01.corp.local
```
---

## 🎯 有趣的目标和文件匹配规则

默认情况下，Snaffler 会寻找以下关键扩展：

|扩展名/文件 |高价值内容 |
|------------------|--------------------|
| `.kdbx` / `.kdb` | KeePass 密码管理器保险箱 |
| `web.config` / `app.config` |明文数据库密码 |
| `unattend.xml` / `sysprep.inf` | Windows 管理员密码 |
| `.ppk` / `.pem` / `id_rsa` | SSH 私钥 |
| `.pfx` / `.p12` |活动目录证书 |
| `.rdp` / `.vnc` |远程桌面连接配置文件 |
| `passwords.xlsx` / `creds.txt` |员工密码列表 |

---

## 📊 输出记录和分析
```bash
# Grep for immediate RED credentials from a Linux terminal
grep -i "{Red}" snaffler_domain.txt

# Find all discovered KeePass safes
grep -i "\.kdbx" snaffler_domain.txt

# Filter out database connection strings
grep -i "password=" snaffler_domain.txt
```
---

## ⚡ 快速参考表

|目标|命令 |
|------------|---------|
| **全域扫描** | `Snaffler.exe -s -o out.log` |
| **目标单台计算机** | `Snaffler.exe -c <HOSTNAME> -s` |
| **目标特定份额** | `Snaffler.exe -i \\<HOST>\<SHARE> -s` |
| **调整并发线程**| `Snaffler.exe -s -t 120 -o out.log` |
| **从扫描中排除主机** | `Snaffler.exe -s -c- <EXCLUDE_HOST>` |
| **最大文件大小限制**| `Snaffler.exe -s -z 5000000` *（限制为 5MB 文件）* |
| **通过C2程序集执行**| `execute-assembly Snaffler.exe -s` |

---

## 🛡️ 防御考虑和 OPSEC

1. **大量 SMB 连接峰值：** Snaffler 在几分钟内连接到每台域计算机上的端口 445。这会生成数千个事件 ID 4624（成功登录类型 3 - 网络）和事件 ID 5140（访问网络共享对象）。
2. **Honeyfiles 和诱饵：** 现代 Active Directory 防御者部署 Canary 共享和诱饵 `.kdbx` 文件，这些文件在被 Snaffler 等工具打开时会触发即时 SOC 警报。
3. **执行限制：** 设置 `-z`（最大文件字节大小），以避免因读取多 GB ISO 或 VHD 备份文件而导致网络共享崩溃。

---

## ⚠️ 法律免责声明

> [!警告]
> Snaffler 专为授权渗透测试、红队参与和内部安全审计而设计。未经授权扫描域名共享构成非法网络侵入。