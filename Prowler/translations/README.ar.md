<!-- language-home-navigation -->
[🏠 الصفحة الرئيسية العربية](../../README.ar.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🌩️ المتصيد - ورقة الغش الكاملة

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
  <img src="https://img.shields.io/badge/Author-Toni_de_la_Fuente-orange?style=for-the-badge" alt="toniblex">
  <img src="https://img.shields.io/badge/Clouds-AWS_%7C_Azure_%7C_GCP_%7C_K8s-gold?style=for-the-badge" alt="Multi-Cloud">
  <img src="https://img.shields.io/badge/Compliance-CIS_Benchmark-green?style=for-the-badge" alt="CIS">
</ص>

<p align="center">
  <b>🌩️ أداة أمان مفتوحة المصدر متوافقة مع معايير الصناعة لتقييم وضعية AWS وAzure وGoogle Cloud (GCP) وKubernetes، وتدقيق التكوين الخاطئ، والتحقق من الامتثال</b>
</ص>

---

## 📋 جدول المحتويات

- [نظرة عامة والهندسة المعمارية](#-نظرة عامة--الهندسة المعمارية)
- [التثبيت](#-التثبيت)
- [AWS Security Auditing](#-aws-security-auditing)
  - [تدقيق الحساب الافتراضي](#default-account-audit)
  - [استهداف الخدمات المحددة (S3، IAM، EC2)](#targeting-specified-services-s3-iam-ec2)
  - [استهداف مناطق AWS محددة](#targeting-specified-aws-regions)
  - [فحص الحسابات المتعددة باستخدام مؤسسات AWS](#فحص الحسابات المتعددة باستخدام مؤسسات AWS)
- [تدقيق Azure وGoogle Cloud (GCP)](#-azure--google-cloud-gcp-auditing)
  - [تقييم Microsoft Azure](#microsoft-azure-assessment)
  - [تقييم Google Cloud Platform (GCP)](#google-cloud-platform-gcp-assessment)
- [أطر عمل الامتثال (CIS، ISO27001، PCI-DSS)](#-compliance-frameworks-cis-iso27001-pci-dss)
- [تنسيقات التقارير والإخراج (HTML، JSON، CSV)](#-reporting--output-formats-html-json-csv)
- [الجدول المرجعي السريع](#-الجدول المرجعي السريع)
- [الاعتبارات الدفاعية والمعالجة](#-الاعتبارات الدفاعية--العلاج)
- [إخلاء المسؤولية القانونية](#-إخلاء المسؤولية القانونية)

---

## 🎯 نظرة عامة والهندسة المعمارية

تم إنشاء **Prowler** في الأصل بواسطة **Toni de la Fuente (@toniblex)**، وهي أداة أساسية لتقييم الأمان متعدد السحابة. يقوم بإجراء تقييمات تلقائية عبر البنية التحتية كخدمة (IaaS) وخدمات النظام الأساسي لاكتشاف:
- مجموعات S3 وحسابات التخزين القابلة للقراءة/الكتابة بشكل عام.
- الحسابات الجذرية تفتقد المصادقة متعددة العوامل (MFA).
- سياسات IAM ذات الامتيازات الزائدة مع أذونات حرف البدل `*`.
- قواعد البيانات والأقراص والنسخ الاحتياطية غير المشفرة.
- فتح مجموعات الأمان التي تعرض منافذ الإدارة (SSH 22، RDP 3389) إلى `0.0.0.0/0`.
- عدم الامتثال لمعايير CIS، ISO 27001، HIPAA، PCI-DSS، وGDPR.

---

## 📥 التثبيت
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

تأكد من أن بيئتك تحتوي على بيانات اعتماد AWS صالحة تم تكوينها (`aws configure` أو متغيرات البيئة أو ملف تعريف مثيل IAM):

### تدقيق الحساب الافتراضي
تشغيل جميع عمليات التحقق المضمنة عبر جميع الخدمات والمناطق الممكّنة:
```bash
prowler aws
```
### استهداف خدمات محددة (S3، IAM، EC2)
```bash
# Run only S3 bucket checks
prowler aws --services s3

# Run IAM and EC2 checks together
prowler aws --services iam,ec2
```
### استهداف مناطق AWS محددة
تجنب مسح المناطق الفارغة غير المستخدمة لزيادة سرعة المسح بشكل كبير:
```bash
prowler aws --region us-east-1,eu-west-1
```
### المسح متعدد الحسابات مع مؤسسات AWS
قم بمراجعة جميع الحسابات ضمن مؤسسة AWS من خلال تولي دور إداري مركزي:
```bash
prowler aws --sp-role-arn arn:aws:iam::111122223333:role/SecurityAuditRole
```
---

## 🔷 تدقيق Azure وGoogle Cloud (GCP).

### تقييم مايكروسوفت أزور
المصادقة عبر Azure CLI (`az login`):
```bash
# Audit entire Azure subscription
prowler azure --subscription-ids "00000000-0000-0000-0000-000000000000"

# Target specific services (Storage, Entra ID / IAM)
prowler azure --services storage,iam
```
### تقييم Google Cloud Platform (GCP).
المصادقة عبر Google Cloud SDK (`gcloud auth application-default login`):
```bash
# Audit specific GCP Project
prowler gcp --project-ids my-production-project

# Target Google Cloud Storage
prowler gcp --services storage
```
---

## 📜 أطر عمل الامتثال (CIS، ISO27001، PCI-DSS)

يصنف برولر الشيكات إلى معايير الامتثال الرسمية:
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

## 📊 تنسيقات التقارير والمخرجات (HTML، JSON، CSV)

افتراضيًا، يقوم Prowler بإخراج التقارير إلى دليل `output/`:
- **HTML التفاعلي:** لوحة معلومات مرئية تعرض عمليات التحقق الفاشلة والتي تم تمريرها وتجاهلها.
- **CSV:** من السهل استيراده إلى جداول البيانات أو أنظمة إصدار التذاكر.
- **JSON-ASFF / JSON-OCSF:** AWS Security Hub والقياس عن بعد المتوافق مع SIEM.
```bash
# Generate HTML and JSON reports in a custom folder
prowler aws -M html,json,csv -o /path/to/audit-reports/
```
---

## ⚡ جدول مرجعي سريع

| الهدف | الأمر |
|-----------|--------|
| **تدقيق AWS الكامل** | `prowler aws` |
| ** خدمة التدقيق الخاصة ** | `prowler aws --services s3,iam` |
| **تدقيق منطقة واحدة** | `prowler aws --region us-east-1` |
| ** التدقيق المعياري لرابطة الدول المستقلة ** | `prowler aws --compliance cis_3.0_aws` |
| **تدقيق Azure الكامل** | `prowler azure --subscription-ids <SUB_ID>` |
| **تدقيق Google Cloud Platform الكامل** | `prowler gcp --project-ids <PROJECT_ID>` |
| **تصدير HTML/CSV/JSON** | `prowler aws -M html,csv,json -o ./reports` |
| **قائمة الشيكات المتاحة**| `prowler aws --list-checks` |

---

## 🛡️ الاعتبارات الدفاعية والعلاج

1. **المعالجة التلقائية:** راجع توصيات مخرجات Prowler التي توفر أمر AWS CLI الدقيق أو معلمة Terraform المطلوبة لإصلاح كل تكوين خاطئ.
2. **تكامل AWS Security Hub:** قم بتغذية نتائج Prowler JSON-ASFF مباشرةً إلى AWS Security Hub للتنبيه المركزي بالثغرات الأمنية المتعددة الحسابات.

---

## ⚠️إخلاء المسؤولية القانونية

> [!تنبيه]
> Prowler هي أداة للتدقيق وتقييم الموقف. تأكد من حصولك على التفويض المناسب من مستأجر السحابة ومالكي الاشتراكات قبل تنفيذ عمليات فحص الامتثال عبر بيئات الإنتاج.