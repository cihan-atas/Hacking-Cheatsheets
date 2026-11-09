<!-- language-home-navigation -->
[🏠 中文主页](../../README.zh-CN.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🌲 adidnsdump - 完整备忘单

[🇹🇷 Türkçe Sürüm]（翻译/README.tr.md）
```
   █████╗ ██████╗ ██╗██████╗ ███╗   ██╗███████╗██████╗ ██╗   ██╗███╗   ███╗██████╗ 
  ██╔══██╗██╔══██╗██║██╔══██╗████╗  ██║██╔════╝██╔══██╗██║   ██║████╗ ████║██╔══██╗
  ███████║██║  ██║██║██║  ██║██╔██╗ ██║███████╗██║  ██║██║   ██║██╔████╔██║██████╔╝
  ██╔══██║██║  ██║██║██║  ██║██║╚██╗██║╚════██║██║  ██║██║   ██║██║╚██╔╝██║██╔═══╝ 
  ██║  ██║██████╔╝██║██████╔╝██║ ╚████║███████║██████╔╝╚██████╔╝██║ ╚═╝ ██║██║     
  ╚═╝  ╚═╝╚═════╝ ╚═╝╚═════╝ ╚═╝  ╚═══╝╚══════╝╚═════╝  ╚═════╝ ╚═╝     ╚═╝╚═╝     
           Active Directory Integrated DNS (ADIDNS) Reconnaissance Tool
```
<p对齐=“中心”>
  <img src="https://img.shields.io/badge/Tool-adidnsdump-blue?style=for-the-badge" alt="adidnsdump">
  <img src="https://img.shields.io/badge/Author-Dirk--jan_Mollema-orange?style=for-the-badge" alt="dirkjanm">
  <img src="https://img.shields.io/badge/Category-Active_Directory-green?style=for-the-badge" alt="Active Directory">
  <img src="https://img.shields.io/badge/Protocol-LDAP_%2F_LDAPS-purple?style=for-the-badge" alt="LDAP">
</p>

<p对齐=“中心”>
  <b>🌲 通过 LDAP 枚举和转储 Active Directory 集成 DNS (ADIDNS) 区域和记录，无需 DNS 区域传输 (AXFR)</b>
</p>

---

## 📋 目录

- [概述和技术背景](#-overview--technical-background)
- [安装](#-安装)
- [身份验证方法](#-authentication-methods)
  - [密码认证](#password-authentication)
  - [传递哈希 (NTLM)](#pass-the-hash-ntlm)
  - [Kerberos 票证 (ccache)](#kerberos-ticket-ccache)
- [核心转储命令](#-core-dumping-commands)
  - [标准 Active Directory DNS 区域转储](#standard-active-directory-dns-zone-dump)
  - [转储逻辑删除/删除的记录](#dumping-tombstoned--deleted-records)
  - [转储林 DNS 和域 DNS 区域](#dumping-forest-dns--domain-dns-zones)
  - [定位特定 DNS 区域](#targeting-specific-dns-zones)
- [导出格式和后处理](#-export-formats--post-processing)
  - [CSV 输出](#csv-output)
  - [与 BloodHound 和 Nmap 集成](#integrating-with-bloodhound--nmap)
- [快速参考表](#-quick-reference-table)
- [防守考虑与检测](#-defective-considerations--detection)
- [法律免责声明](#-legal-disclaimer)

---

## 🎯 概述和技术背景

在标准 Windows Active Directory 环境中，DNS 通常集成到 Active Directory 中（**Active Directory 集成 DNS / ADIDNS**）。在这些配置中，DNS 记录不是存储在简单的文本平面文件中，而是存储为域分区内的 LDAP 对象（例如 `DC=DomainDnsZones` 和 `DC=ForestDnsZones`）。

传统的 DNS 区域传输 (`AXFR`) 几乎在域控制器上普遍被禁用。但是，**任何经过身份验证的域用户**（甚至是低权限帐户或受损的服务帐户）默认情况下都具有对存储 ADIDNS 记录的 LDAP 节点的读取权限。

由 **Dirk-jan Mollema (@dirkjanm)** 开发，**`adidnsdump`** 通过 LDAP 或 LDAPS 连接到域控制器，查询 ADIDNS 分区，并提取所有记录（A、AAAA、CNAME、SRV、TXT、MX 和逻辑删除记录），创建域中每台计算机、工作站和内部 Web 服务的准确清单。

---

## 📥 安装
```bash
# Clone the repository and install with pip
git clone https://github.com/dirkjanm/adidnsdump.git
cd adidnsdump
pip install .

# Or install directly via pipx / pip
pipx install git+https://github.com/dirkjanm/adidnsdump.git
```
---

## 🔑 身份验证方法

### 密码验证
```bash
adidnsdump -u 'CORP\jdoe' -p 'P@ssw0rd123!' 192.168.1.10
```
### 哈希传递 (NTLM)
如果您通过 Mimikatz 或 Secretsdump 提取 NTLM 哈希值：
```bash
adidnsdump -u 'CORP\jdoe' --hashes :31d6cfe0d16ae931b73c59d7e0c089c0 192.168.1.10
```
### Kerberos 票证 (ccache)
使用存储在 `KRB5CCNAME` 中的现有 Kerberos TGT/TGS 票证：
```bash
export KRB5CCNAME=/tmp/jdoe.ccache
adidnsdump -k -u 'CORP\jdoe' dc01.corp.local
```
---

## 🚀 核心转储命令

### 标准 Active Directory DNS 区域转储
从默认 DomainDnsZones 分区转储所有活动 DNS 记录：
```bash
adidnsdump -u 'corp.local\jdoe' -p 'P@ssw0rd123!' ldap://dc01.corp.local
```
这会在当前工作目录中生成一个名为 `records.csv` 的文件。

### 转储逻辑删除/删除的记录
Windows DNS 在物理删除之前将已删除的记录标记为“逻辑删除”数天。这些记录通常揭示退役的系统、遗留服务器或测试环境：
```bash
adidnsdump -u 'corp.local\jdoe' -p 'P@ssw0rd123!' --include-tombstoned ldap://dc01.corp.local
```
### 转储林 DNS 和域 DNS 区域
从林级 DNS 分区中提取记录：
```bash
adidnsdump -u 'corp.local\jdoe' -p 'P@ssw0rd123!' --forest ldap://dc01.corp.local
```
### 针对特定 DNS 区域
如果目标运行多个裂脑 DNS 区域或子域：
```bash
adidnsdump -u 'corp.local\jdoe' -p 'P@ssw0rd123!' --zone dev.corp.local ldap://dc01.corp.local
```
### 安全 LDAPS 加密通信
防止凭据以明文形式穿越网络：
```bash
adidnsdump --ssl -u 'corp.local\jdoe' -p 'P@ssw0rd123!' ldaps://dc01.corp.local
```
---

## 📊 导出格式和后处理

### CSV 输出
默认情况下，`adidnsdump` 输出 `records.csv`。这些字段包括：
- `zone`：记录所属的 DNS 区域。
- `name`：记录主机名或标签。
- `type`：记录类型（A、AAAA、CNAME、SRV 等）。
- `value`：目标 IP 地址、主机名或字符串。

### 与 BloodHound 和 Nmap 集成
从转储记录中提取所有唯一的 IP 地址以进行自动扫描：
```bash
# Filter unique IP addresses (Type A records)
awk -F',' '$3=="A" {print $4}' records.csv | sort -u > live_ips.txt

# Run fast port scan against newly discovered internal hosts
nmap -sS -Pn -iL live_ips.txt -T4 -p 80,443,445,3389 -oA internal_assets
```
---

## ⚡ 快速参考表

|目标|命令 |
|------------|---------|
| **标准 ADIDNS 转储** | `adidnsdump -u 'DOMAIN\User' -p 'Pass' <DC_IP>` |
| **传递哈希 (NTLM)** | `adidnsdump -u 'DOMAIN\User' --hashes :<NTLM> <DC_IP>` |
| **Kerberos 票证身份验证** | `adidnsdump -k -u 'DOMAIN\User' <DC_FQDN>` |
| **转储墓碑记录**| `adidnsdump -u 'DOMAIN\User' -p 'Pass' --include-tombstoned <DC_IP>` |
| **废弃林区** | `adidnsdump -u 'DOMAIN\User' -p 'Pass' --forest <DC_IP>` |
| **指定目标区域** | `adidnsdump -u 'DOMAIN\User' -p 'Pass' --zone <ZONE> <DC_IP>` |
| **强制实施 LDAPS (SSL)** | `adidnsdump --ssl -u 'DOMAIN\User' -p 'Pass' ldaps://<DC_IP>` |

---

## 🛡️ 防守考虑和检测

1. **LDAP 查询监控：** 启用目录服务访问的高级审核策略（事件 ID 4662），以针对针对 `CN=MicrosoftDNS,DC=DomainDnsZones` 的大量查询发出警报。
2. **限制 ADIDNS ACL：** 如果非管理员不需要内部 DNS 自省，则将 `MicrosoftDNS` 容器的读取权限限制为域管理员或特定管理角色。
3. **蜜币：** 植入指向蜜罐 IP 地址的有吸引力的 DNS 记录（例如 `backup-vault.corp.local` 或 `admin-portal.corp.local`）。

---

## ⚠️ 法律免责声明

> [!警告]
> adidnsdump 严格用于授权的安全评估、Active Directory 审核和允许范围内的渗透测试。在未经授权的网络上查询目录服务是非法的。