<!-- language-home-navigation -->
[🏠 中文主页](../../README.zh-CN.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🌩️ Prowler - 完整备忘单

[🇹🇷 Türkçe Sürüm]（翻译/README.tr.md）
```
  ██████╗ ██████╗  ██████╗ ██╗    ██╗██╗     ███████╗██████╗ 
  ██╔══██╗██╔══██╗██╔═══██╗██║    ██║██║     ██╔════╝██╔══██╗
  ██████╔╝██████╔╝██║   ██║██║ █╗ ██║██║     █████╗  ██████╔╝
  ██╔═══╝ ██╔══██╗██║   ██║██║███╗██║██║     ██╔══╝  ██╔══██╗
  ██║     ██║  ██║╚██████╔╝╚███╔███╔╝███████╗███████╗██║  ██║
  ╚═╝     ╚═╝  ╚═╝ ╚═════╝  ╚══╝╚══╝ ╚══════╝╚══════╝╚═╝  ╚═╝
       Multi-Cloud Security Posture Assessment & CIS Benchmark Auditor
```
<p对齐=“中心”>
  <img src="https://img.shields.io/badge/Prowler-Cloud_Security-blue?style=for-the-badge" alt="Prowler">
  <img src="https://img.shields.io/badge/Author-Toni_de_la_Fuente-orange?style=for-the-badge" alt="tonbillyx">
  <img src="https://img.shields.io/badge/Clouds-AWS_%7C_Azure_%7C_GCP_%7C_K8s-gold?style=for-the-badge" alt="多云">
  <img src="https://img.shields.io/badge/Compliance-CIS_Benchmark-green?style=for-the-badge" alt="CIS">
</p>

<p对齐=“中心”>
  <b>🌩️ 适用于 AWS、Azure、Google Cloud (GCP) 和 Kubernetes 状态评估、错误配置审核和合规性验证的行业标准开源安全工具</b>
</p>

---

## 📋 目录

- [概述与架构](#-overview--architecture)
- [安装](#-安装)
- [AWS 安全审核](#-aws-security-auditing)
  - [默认账户审核](#default-account-audit)
  - [定位特定服务（S3、IAM、EC2）](#targeting-specific-services-s3-iam-ec2)
  - [定位特定 AWS 区域](#targeting-specific-aws-regions)
  - [使用 AWS Organizations 进行多账户扫描](#multi-account-scanning-with-aws-organizations)
- [Azure 和 Google Cloud (GCP) 审核](#-azure--google-cloud-gcp-auditing)
  - [Microsoft Azure 评估](#microsoft-azure-assessment)
  - [Google Cloud Platform (GCP) 评估](#google-cloud-platform-gcp-assessment)
- [合规框架（CIS、ISO27001、PCI-DSS）](#-compliance-frameworks-cis-iso27001-pci-dss)
- [报告和输出格式（HTML、JSON、CSV）](#-reporting--output-formats-html-json-csv)
- [快速参考表](#-quick-reference-table)
- [防御考虑与补救](#-defective-considerations--remediation)
- [法律免责声明](#-legal-disclaimer)

---

## 🎯 概述与架构

**Prowler** 最初由 **Toni de la Fuente (@tonbillyx)** 创建，是一款重要的多云安全评估工具。它跨基础设施即服务 (IaaS) 和平台服务执行自动评估，以检测：
- 公开可读/可写的S3存储桶和存储帐户。
- 根帐户缺少多重身份验证 (MFA)。
- 具有通配符 `*` 权限的过度特权 IAM 策略。
- 未加密的数据库、磁盘和备份。
- 打开向 `0.0.0.0/0` 公开管理端口（SSH 22、RDP 3389）的安全组。
- 不符合 CIS 基准、ISO 27001、HIPAA、PCI-DSS 和 GDPR。

---

## 📥 安装
```bash
# Using pip / pipx (Recommended)
pipx install prowler
# Or
pip install prowler

# Using Docker
docker pull prowlercloud/prowler:latest

# Verify installation
prowler -v
```
---

## ☁️ AWS 安全审核

确保您的环境配置了有效的 AWS 凭证（`aws configure`、环境变量或 IAM 实例配置文件）：

### 默认账户审核
在所有启用的服务和区域中运行所有内置检查：
```bash
prowler aws
```
### 针对特定服务（S3、IAM、EC2）
```bash
# Run only S3 bucket checks
prowler aws --services s3

# Run IAM and EC2 checks together
prowler aws --services iam,ec2
```
### 定位特定 AWS 区域
避免扫描空的未使用区域以显着提高扫描速度：
```bash
prowler aws --region us-east-1,eu-west-1
```
### 使用 AWS Organizations 进行多账户扫描
通过担任集中管理角色来审核 AWS 组织下的所有账户：
```bash
prowler aws --sp-role-arn arn:aws:iam::111122223333:role/SecurityAuditRole
```
---

## 🔷 Azure 和 Google Cloud (GCP) 审核

### Microsoft Azure 评估
通过 Azure CLI (`az login`) 进行身份验证：
```bash
# Audit entire Azure subscription
prowler azure --subscription-ids "00000000-0000-0000-0000-000000000000"

# Target specific services (Storage, Entra ID / IAM)
prowler azure --services storage,iam
```
### Google Cloud Platform (GCP) 评估
通过 Google Cloud SDK (`gcloud auth application-default login`) 进行身份验证：
```bash
# Audit specific GCP Project
prowler gcp --project-ids my-production-project

# Target Google Cloud Storage
prowler gcp --services storage
```
---

## 📜 合规框架（CIS、ISO27001、PCI-DSS）

Prowler 将检查分为官方合规标准：
```bash
# Run CIS AWS Foundations Benchmark v3.0
prowler aws --compliance cis_3.0_aws

# Run PCI-DSS Compliance Audit
prowler aws --compliance pci_3.2.1_aws

# Run ISO 27001 Evaluation
prowler aws --compliance iso27001_2013_aws

# List all supported compliance frameworks
prowler aws --list-compliance
```
---

## 📊 报告和输出格式（HTML、JSON、CSV）

默认情况下，Prowler 将报告输出到 `output/` 目录：
- **交互式 HTML：** 可视化仪表板显示失败、通过和静音检查。
- **CSV：** 易于导入电子表格或票务系统。
- **JSON-ASFF / JSON-OCSF：** AWS Security Hub 和 SIEM 兼容遥测。
```bash
# Generate HTML and JSON reports in a custom folder
prowler aws -M html,json,csv -o /path/to/audit-reports/
```
---

## ⚡ 快速参考表

|目标|命令 |
|------------|---------|
| **全面的 AWS 审计** | `prowler aws` |
| **审计特定服务** | `prowler aws --services s3,iam` |
| **审核单一区域** | `prowler aws --region us-east-1` |
| **CIS 基准审核** | `prowler aws --compliance cis_3.0_aws` |
| **全面的 Azure 审核** | `prowler azure --subscription-ids <SUB_ID>` |
| **全面的 GCP 审核** | `prowler gcp --project-ids <PROJECT_ID>` |
| **导出 HTML/CSV/JSON** | `prowler aws -M html,csv,json -o ./reports` |
| **列出可用支票**| `prowler aws --list-checks` |

---

## 🛡️ 防御注意事项和补救措施

1. **自动修复：** 查看 Prowler 的输出建议，其中提供了修复每个错误配置所需的确切 AWS CLI 命令或 Terraform 参数。
2. **AWS Security Hub 集成：** 将 Prowler JSON-ASFF 结果直接馈送到 AWS Security Hub 中，以进行集中式多账户漏洞警报。

---

## ⚠️ 法律免责声明

> [!警告]
> Prowler 是一种审计和态势评估工具。在跨生产环境执行合规性扫描之前，请确保您已获得云租户和订阅所有者的适当授权。