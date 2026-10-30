# 🌩️ Prowler - Complete Cheatsheet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)

```
  ██████╗ ██████╗  ██████╗ ██╗    ██╗██╗     ███████╗██████╗ 
  ██╔══██╗██╔══██╗██╔═══██╗██║    ██║██║     ██╔════╝██╔══██╗
  ██████╔╝██████╔╝██║   ██║██║ █╗ ██║██║     █████╗  ██████╔╝
  ██╔═══╝ ██╔══██╗██║   ██║██║███╗██║██║     ██╔══╝  ██╔══██╗
  ██║     ██║  ██║╚██████╔╝╚███╔███╔╝███████╗███████╗██║  ██║
  ╚═╝     ╚═╝  ╚═╝ ╚═════╝  ╚══╝╚══╝ ╚══════╝╚══════╝╚═╝  ╚═╝
       Multi-Cloud Security Posture Assessment & CIS Benchmark Auditor
```

<p align="center">
  <img src="https://img.shields.io/badge/Prowler-Cloud_Security-blue?style=for-the-badge" alt="Prowler">
  <img src="https://img.shields.io/badge/Author-Toni_de_la_Fuente-orange?style=for-the-badge" alt="toniblyx">
  <img src="https://img.shields.io/badge/Clouds-AWS_%7C_Azure_%7C_GCP_%7C_K8s-gold?style=for-the-badge" alt="Multi-Cloud">
  <img src="https://img.shields.io/badge/Compliance-CIS_Benchmark-green?style=for-the-badge" alt="CIS">
</p>

<p align="center">
  <b>🌩️ Industry-standard open-source security tool for AWS, Azure, Google Cloud (GCP), and Kubernetes posture assessment, misconfiguration auditing, and compliance verification</b>
</p>

---

## 📋 Table of Contents

- [Overview & Architecture](#-overview--architecture)
- [Installation](#-installation)
- [AWS Security Auditing](#-aws-security-auditing)
  - [Default Account Audit](#default-account-audit)
  - [Targeting Specific Services (S3, IAM, EC2)](#targeting-specific-services-s3-iam-ec2)
  - [Targeting Specific AWS Regions](#targeting-specific-aws-regions)
  - [Multi-Account Scanning with AWS Organizations](#multi-account-scanning-with-aws-organizations)
- [Azure & Google Cloud (GCP) Auditing](#-azure--google-cloud-gcp-auditing)
  - [Microsoft Azure Assessment](#microsoft-azure-assessment)
  - [Google Cloud Platform (GCP) Assessment](#google-cloud-platform-gcp-assessment)
- [Compliance Frameworks (CIS, ISO27001, PCI-DSS)](#-compliance-frameworks-cis-iso27001-pci-dss)
- [Reporting & Output Formats (HTML, JSON, CSV)](#-reporting--output-formats-html-json-csv)
- [Quick Reference Table](#-quick-reference-table)
- [Defensive Considerations & Remediation](#-defensive-considerations--remediation)
- [Legal Disclaimer](#-legal-disclaimer)

---

## 🎯 Overview & Architecture

Originally created by **Toni de la Fuente (@toniblyx)**, **Prowler** is an essential multi-cloud security assessment tool. It performs automated evaluations across infrastructure-as-a-service (IaaS) and platform services to detect:
- Publicly readable/writable S3 buckets and storage accounts.
- Root accounts missing Multi-Factor Authentication (MFA).
- Over-privileged IAM policies with wildcard `*` permissions.
- Unencrypted databases, disks, and backups.
- Open security groups exposing management ports (SSH 22, RDP 3389) to `0.0.0.0/0`.
- Non-compliance with CIS Benchmarks, ISO 27001, HIPAA, PCI-DSS, and GDPR.

---

## 📥 Installation

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

## ☁️ AWS Security Auditing

Ensure your environment has valid AWS credentials configured (`aws configure`, environment variables, or IAM instance profile):

### Default Account Audit
Runs all built-in checks across all enabled services and regions:
```bash
prowler aws
```

### Targeting Specific Services (S3, IAM, EC2)
```bash
# Run only S3 bucket checks
prowler aws --services s3

# Run IAM and EC2 checks together
prowler aws --services iam,ec2
```

### Targeting Specific AWS Regions
Avoid scanning empty unused regions to dramatically increase scan speed:
```bash
prowler aws --region us-east-1,eu-west-1
```

### Multi-Account Scanning with AWS Organizations
Audit all accounts under an AWS Organization by assuming a centralized management role:
```bash
prowler aws --sp-role-arn arn:aws:iam::111122223333:role/SecurityAuditRole
```

---

## 🔷 Azure & Google Cloud (GCP) Auditing

### Microsoft Azure Assessment
Authenticate via Azure CLI (`az login`):
```bash
# Audit entire Azure subscription
prowler azure --subscription-ids "00000000-0000-0000-0000-000000000000"

# Target specific services (Storage, Entra ID / IAM)
prowler azure --services storage,iam
```

### Google Cloud Platform (GCP) Assessment
Authenticate via Google Cloud SDK (`gcloud auth application-default login`):
```bash
# Audit specific GCP Project
prowler gcp --project-ids my-production-project

# Target Google Cloud Storage
prowler gcp --services storage
```

---

## 📜 Compliance Frameworks (CIS, ISO27001, PCI-DSS)

Prowler categorizes checks into official compliance standards:

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

## 📊 Reporting & Output Formats (HTML, JSON, CSV)

By default, Prowler outputs reports to an `output/` directory:
- **Interactive HTML:** Visual dashboard showing failed, passed, and muted checks.
- **CSV:** Easy to import into spreadsheets or ticketing systems.
- **JSON-ASFF / JSON-OCSF:** AWS Security Hub and SIEM-compatible telemetry.

```bash
# Generate HTML and JSON reports in a custom folder
prowler aws -M html,json,csv -o /path/to/audit-reports/
```

---

## ⚡ Quick Reference Table

| Objective | Command |
|-----------|---------|
| **Full AWS Audit** | `prowler aws` |
| **Audit Specific Service** | `prowler aws --services s3,iam` |
| **Audit Single Region** | `prowler aws --region us-east-1` |
| **CIS Benchmark Audit** | `prowler aws --compliance cis_3.0_aws` |
| **Full Azure Audit** | `prowler azure --subscription-ids <SUB_ID>` |
| **Full GCP Audit** | `prowler gcp --project-ids <PROJECT_ID>` |
| **Export HTML/CSV/JSON** | `prowler aws -M html,csv,json -o ./reports` |
| **List Available Checks**| `prowler aws --list-checks` |

---

## 🛡️ Defensive Considerations & Remediation

1. **Automated Remediation:** Review Prowler's output recommendations which provide the exact AWS CLI command or Terraform parameter required to fix each misconfiguration.
2. **AWS Security Hub Integration:** Feed Prowler JSON-ASFF findings directly into AWS Security Hub for centralized multi-account vulnerability alerting.

---

## ⚠️ Legal Disclaimer

> [!CAUTION]
> Prowler is an audit and posture assessment tool. Ensure that you have proper authorization from the cloud tenant and subscription owners before executing compliance scans across production environments.
