<!-- language-home-navigation -->
[🏠 الصفحة الرئيسية العربية](../../README.ar.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🐶 Snaffler - ورقة الغش الكاملة

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ███████╗███╗   ██╗ █████╗ ███████╗███████╗██╗     ███████╗██████╗ 
  ██╔════╝████╗  ██║██╔══██╗██╔════╝██╔════╝██║     ██╔════╝██╔══██╗
  ███████╗██╔██╗ ██║███████║█████╗  █████╗  ██║     █████╗  ██████╔╝
  ╚════██║██║╚██╗██║██╔══██║██╔══╝  ██╔══╝  ██║     ██╔══╝  ██╔══██╗
  ███████║██║ ╚████║██║  ██║██║     ██║     ███████╗███████╗██║  ██║
  ╚══════╝╚═╝  ╚═══╝╚═╝  ╚═╝╚═╝     ╚═╝     ╚══════╝╚══════╝╚═╝  ╚═╝
       Active Directory Domain SMB Share Credential & Secret Hunter
```
<p align="center">
  <img src="https://img.shields.io/badge/Snaffler-Secret_Hunter-blue?style=for-the-badge" alt="Snaffler">
  <img src="https://img.shields.io/badge/Language-C%23_/_NET-orange?style=for-the-badge" alt="C# / .NET">
  <img src="https://img.shields.io/badge/Category-Active_Directory_&_Network-green?style=for-the-badge" alt="Active Directory">
  <img src="https://img.shields.io/badge/License-GPLv3-red?style=for-the-badge" alt="License GPLv3">
</ص>

<p align="center">
  <b>🌐 أداة نطاق Windows آلية مصممة للزحف إلى مشاركات الشركات الصغيرة والمتوسطة، وتحديد موقع ملفات التكوين المهمة، وقواعد بيانات KeePass، وبيانات اعتماد النص العادي</b>
</ص>

---

## 📋 جدول المحتويات

- [نظرة عامة ومشكلة صيد الأسهم](#-نظرة عامة--مشكلة-صيد الأسهم)
- [كيف يعمل السنافلر](#-كيف يعمل السنافلر)
- [خيارات التثبيت والتنفيذ](#-التثبيت--خيارات-التنفيذ)
- [فرز السنافلر ومستويات الخطورة](#-فرز السنافلر--مستويات الخطورة)
- [سير عمل المسح المشترك](#-common-scanning-workflow)
  - [الزحف إلى المشاركة على مستوى النطاق](#domain-wide-share-crawl)
  - [استهداف مضيفين أو مشاركات محددة](#targeting-special-hosts-or-shares)
  - [باستثناء أجهزة الكمبيوتر وجذور DFS](#exclusion-computers--dfs-roots)
- [قواعد مطابقة الأهداف والملفات](#-juicy-targets--file-match-rules)
- [تسجيل المخرجات وتحليلها](#-تسجيل المخرجات--التحليل)
- [الجدول المرجعي السريع](#-الجدول المرجعي السريع)
- [الاعتبارات الدفاعية وOPSEC](#-الاعتبارات الدفاعية--opsec)
- [إخلاء المسؤولية القانونية](#-إخلاء المسؤولية القانونية)

---

## 🎯 نظرة عامة ومشكلة صيد الأسهم

في بيئات Active Directory المتوسطة والكبيرة، تستضيف خوادم ملفات المؤسسة ومحطات العمل الآلاف من مشاركات شبكة SMB المفتوحة. كثيرًا ما يترك مسؤولو النظام والمطورون والموظفون وراءهم ما يلي:

- 🔑 **قواعد بيانات KeePass (`.kdbx`):** غالبًا ما تحتوي على كلمات مرور رئيسية أو مفاتيح رئيسية يمكن اختراقها بسهولة.
- ⚙️ **ملفات التكوين (`web.config`، `appsettings.json`):** تحتوي على سلاسل اتصال SQL مشفرة وكلمات مرور حساب الخدمة ومفاتيح API.
- 📜 **النصوص البرمجية للنشر (`unattend.xml`، `.ps1`، `.bat`):** بيانات الاعتماد الإدارية المشفرة.
- 🔏 **الشهادات الخاصة (`.pfx`، `.p12`، `.pem`، `.ppk`):** يمكن استخدامها لتصعيد الامتيازات أو الحركة الجانبية.

من المستحيل تصفح آلاف المشاركات يدويًا. **Snaffler**، الذي تم تطويره بواسطة **l33tb455**، يقوم بأتمتة هذه العملية بأكملها: فهو يستعلم عن Active Directory لكائنات الكمبيوتر، ويعدد مشاركات SMB القابلة للقراءة، ويتجول في شجرة الدليل، ويقيم الملفات مقابل مجموعات قواعد regex المنسقة.

---

## ⚙️ كيف يعمل سنافلر
```
[ Compromised Domain Machine ]
         │
         ├── 1. Queries Domain Controller via LDAP for Computer Objects
         ├── 2. Connects to port 445 (SMB) across all discovered hosts
         ├── 3. Enumerates Read/Write permissions on C$, SYSVOL, NETLOGON, Custom Shares
         └── 4. Evaluates file extensions, file names, and file contents against rules
```
---

## 📥 خيارات التثبيت والتنفيذ

يتم تجميع Snaffler باعتباره ثنائي .NET مستقل:
```bash
# Clone and build using Visual Studio or dotnet CLI on Windows
git clone https://github.com/SnaffCon/Snaffler.git
cd Snaffler
dotnet build -c Release
```
### يعمل على نظام التشغيل Windows:
```cmd
# Standard execution outputting to screen and log file
Snaffler.exe -s -o snaffler_log.txt
```
### التشغيل عبر Cobalt Strike / C2 (التجميع التنفيذي):
```text
beacon> execute-assembly C:\Tools\Snaffler.exe -s -o C:\Temp\loot.txt
```
---

## 🚦 فرز السنافلر ومستويات الخطورة

يقوم Snaffler بتصنيف النتائج إلى مجموعات عاجلة مرمزة بالألوان:

- 🔴 **`{Red}` / Black:** اكتشاف واضح ذو قيمة عالية! كلمات مرور نصية عادية، ومفاتيح خاصة (`.pfx`، و`.id_rsa`)، ورموز AWS المميزة، وملفات التثبيت غير المراقبة.
- 🟡 **`{Yellow}`:** نتيجة محتملة. ملفات التكوين مع سلاسل كلمة المرور، وسلاسل اتصال قاعدة البيانات، والنسخ الاحتياطية.
- 🟢 **`{Green}`:** إعلامية/فائدة. أسماء الملفات المطابقة للأنماط الإدارية (`passwords.xlsx`، `vpn.ovpn`).

---

## 🚀 سير عمل المسح المشترك

### الزحف على مستوى النطاق

فحص كافة المشاركات التي يمكن الوصول إليها عبر مجال Active Directory بأكمله:
```cmd
# Run with live console streaming (-s) and save to disk
Snaffler.exe -s -o C:\loot\snaffler_domain.txt

# Run with multi-threading tuning (Default: 60 threads)
Snaffler.exe -s -t 100 -o C:\loot\snaffler_fast.txt
```
### استهداف مضيفين أو مشاركات محددة

عندما تريد تجنب فحص النطاق بأكمله والتركيز على خادم الملفات أو وحدة تحكم المجال:
```cmd
# Scan a single targeted file server
Snaffler.exe -c fs01.corp.local -s

# Scan a comma-separated list of machines
Snaffler.exe -c dc01.corp.local,fs02.corp.local,sql01.corp.local -s

# Scan a specific shared folder path directly
Snaffler.exe -i \\fs01.corp.local\Departments\IT\ -s
```
### باستثناء أجهزة الكمبيوتر وجذور DFS

تجنب فحص أجهزة الكمبيوتر البطيئة أو الحساسة:
```cmd
# Exclude specific backup or surveillance servers
Snaffler.exe -s -c- backup01.corp.local,cctv01.corp.local
```
---

## 🎯 الأهداف المثيرة وقواعد مطابقة الملفات

افتراضيًا، يبحث Snaffler عن الامتدادات الهامة التالية:

| ملحق / ملف | محتوى عالي القيمة |
|------------------|--------------------|
| `.kdbx` / `.kdb` | خزائن مدير كلمة المرور KeePass |
| `web.config` / `app.config` | كلمات مرور قاعدة بيانات Cleartext |
| `unattend.xml` / `sysprep.inf` | كلمات مرور مسؤول Windows |
| `.ppk` / `.pem` / `id_rsa` | مفاتيح SSH الخاصة |
| `.pfx` / `.p12` | شهادات الدليل النشط |
| `.rdp` / `.vnc` | ملفات تعريف الاتصال بسطح المكتب البعيد |
| `passwords.xlsx` / `creds.txt` | قوائم كلمة مرور الموظف |

---

## 📊 تسجيل المخرجات وتحليلها
```bash
# Grep for immediate RED credentials from a Linux terminal
grep -i "{Red}" snaffler_domain.txt

# Find all discovered KeePass safes
grep -i "\.kdbx" snaffler_domain.txt

# Filter out database connection strings
grep -i "password=" snaffler_domain.txt
```
---

## ⚡ جدول مرجعي سريع

| الهدف | الأمر |
|-----------|--------|
| ** المسح على مستوى المجال ** | `Snaffler.exe -s -o out.log` |
| ** كمبيوتر واحد الهدف ** | `Snaffler.exe -c <HOSTNAME> -s` |
| ** حصة محددة الهدف ** | `Snaffler.exe -i \\<HOST>\<SHARE> -s` |
| **ضبط سلاسل التزامن**| `Snaffler.exe -s -t 120 -o out.log` |
| **استبعاد المضيف من الفحص** | `Snaffler.exe -s -c- <EXCLUDE_HOST>` |
| **الحد الأقصى لحجم الملف**| `Snaffler.exe -s -z 5000000` *(الحد الأقصى لملفات 5 ميجابايت)* |
| ** التنفيذ عبر C2 Assembly **| `execute-assembly Snaffler.exe -s` |

---

## 🛡️ الاعتبارات الدفاعية وOPSEC

1. ** ارتفاعات هائلة في اتصال SMB: ** يتصل Snaffler بالمنفذ 445 على كل جهاز مجال في غضون دقائق. يؤدي هذا إلى إنشاء الآلاف من معرّف الحدث 4624 (نوع تسجيل الدخول الناجح 3 - الشبكة) ومعرّف الحدث 5140 (تم الوصول إلى كائن مشاركة الشبكة).
2. **Honeyfiles & Decoys:** يقوم المدافعون الحديثون عن Active Directory بنشر مشاركات Canary وملفات `.kdbx` الخادعة التي تؤدي إلى تنبيهات SOC فورية عند فتحها بواسطة أدوات مثل Snaffler.
3. **حدود التنفيذ:** قم بتعيين `-z` (الحد الأقصى لحجم بايت الملف) لتجنب تعطل مشاركات الشبكة من خلال قراءة ملفات النسخ الاحتياطي متعددة الجيجابايت ISO أو VHD.

---

## ⚠️إخلاء المسؤولية القانونية

> [!تنبيه]
> تم تصميم Snaffler حصريًا لاختبارات الاختراق المعتمدة، وارتباطات الفريق الأحمر، وعمليات تدقيق الأمن الداخلي. يعد فحص مشاركات المجال دون إذن تعديًا غير قانوني على الشبكة.