<!-- language-home-navigation -->
[🏠 الصفحة الرئيسية العربية](../../README.ar.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 📂 GitTools - ورقة الغش الكاملة

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
   ██████╗ ██╗████████╗████████╗ ██████╗  ██████╗ ██╗     ███████╗
  ██╔════╝ ██║╚══██╔══╝╚══██╔══╝██╔═══██╗██╔═══██╗██║     ██╔════╝
  ██║  ███╗██║   ██║      ██║   ██║   ██║██║   ██║██║     ███████╗
  ██║   ██║██║   ██║      ██║   ██║   ██║██║   ██║██║     ╚════██║
  ╚██████╔╝██║   ██║      ██║   ╚██████╔╝╚██████╔╝███████╗███████║
   ╚═════╝ ╚═╝   ╚═╝      ╚═╝    ╚═════╝  ╚═════╝ ╚══════╝╚══════╝
      Exposed /.git/ Repository Dumping, Extraction & Secret Hunting
```
<p align="center">
  <img src="https://img.shields.io/badge/GitTools-Git_Dumping-blue?style=for-the-badge" alt="GitTools">
  <img src="https://img.shields.io/badge/Author-Internetwache-orange?style=for-the-badge" alt="internetwache">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_Secrets-green?style=for-the-badge" alt="أمان الويب">
  <img src="https://img.shields.io/badge/License-MIT-purple?style=for-the-badge" alt="MIT">
</ص>

<p align="center">
  <b>📂 مجموعة أدوات أساسية لاكتشاف مجلدات <code>/.git/</code> المكشوفة على خوادم الويب، وتنزيل مخازن كائنات Git الأولية، وإعادة بناء كود المصدر الأصلي وسجل الالتزام بشكل كامل</b>
</ص>

---

## 📋 جدول المحتويات

- [نظرة عامة ومخاطر .git المكشوفة](#-نظرة عامة--مكشوفة-git-hazard)
- [التثبيت](#-التثبيت)
- [وحدات GitTools الثلاثة](#-the-three-gittools-modules)
  - [1. شاحنة قلابة (`gitdumper.sh`)](#1-شاحنة قلابة-gitdumpersh)
  - [2. النازع (`extractor.sh`)](#2-المستخرج-المستخرج)
  - [3. الباحث (`finder.py`)](#3-الباحث-الباحث)
- [أداة قلابة بايثون البديلة: `git-dumper`](#-alternative-python-dumper-git-dumper)
- [سر ما بعد إعادة الإعمار والبحث عن بيانات الاعتماد](#-سر ما بعد إعادة الإعمار--صيد بيانات الاعتماد)
- [الجدول المرجعي السريع](#-الجدول المرجعي السريع)
- [الاعتبارات الدفاعية وتقوية خادم الويب](#-الاعتبارات الدفاعية--تقوية خادم الويب)
- [إخلاء المسؤولية القانونية](#-إخلاء المسؤولية القانونية)

---

## 🎯 نظرة عامة ومخاطر .git المكشوفة

عندما ينشر المطورون تطبيقات الويب عبر `git clone` أو البرامج النصية الآلية دون تقييد الوصول إلى الأدلة المخفية بشكل صحيح، يصبح المجلد **`/.git/`** قابلاً للوصول عبر HTTP/HTTPS:
`https://target.com/.git/`

حتى إذا تم تعطيل قائمة الدليل (الفهرسة)، فإن البنية الحتمية لـ Git تسمح للمهاجم بتنزيل `/.git/HEAD` و`/.git/config` و`/.git/index` واجتياز جميع كائنات Git السائبة (`.git/objects/xx/xxxx...`). وهذا يتيح إعادة الإعمار الكامل لما يلي:
- كود مصدر التطبيق الكامل وواجهات برمجة التطبيقات الداخلية.
- بيانات اعتماد قاعدة البيانات ورموز API ومفاتيح SSH الخاصة في ملفات التكوين.
- سجلات التزام git الكاملة ورسائل البريد الإلكتروني للمؤلف وفروع التطوير.

---

## 📥 التثبيت
```bash
# Clone GitTools repository
git clone https://github.com/internetwache/GitTools.git
cd GitTools
chmod +x Dumper/gitdumper.sh Extractor/extractor.sh

# Install alternative standalone Python git-dumper via pip
pip install git-dumper
```
---

## 🚀 وحدات GitTools الثلاث

يتم تنظيم GitTools في ثلاث أدوات مساعدة متخصصة:

### 1. شاحنة قلابة (`gitdumper.sh`)
قم بتنزيل أكبر قدر ممكن من دليل `.git` من خادم الويب:
```bash
# Syntax: ./gitdumper.sh <URL_TO_GIT> <LOCAL_DESTINATION>
./Dumper/gitdumper.sh https://target.com/.git/ /tmp/target_repo/
```
### 2. النازع (`extractor.sh`)
إعادة بناء الملفات الأصلية وأشجار الالتزام والفروع من كائنات `.git` السائبة التي تم تفريغها:
```bash
# Syntax: ./extractor.sh <DUMPED_DIR> <OUTPUT_CODE_DIR>
./Extractor/extractor.sh /tmp/target_repo/ /tmp/extracted_source/
```
داخل `/tmp/extracted_source/`، تتم استعادة كل التزام إلى دليل معزول يحتوي على شجرة الملفات الكاملة في ذلك الوقت.

### 3. الباحث (`finder.py`)
يقوم بمسح قوائم النطاقات أو كتل CIDR لاكتشاف أدلة `.git` المكشوفة:
```bash
python3 Finder/finder.py -i targets.txt -u "Mozilla/5.0"
```
---

## 🐍 أداة قلابة بايثون البديلة: `git-dumper`

`git-dumper` هو بديل Python متعدد الخيوط الذي يعيد بناء مستودع git حي وعامل تلقائيًا في خطوة واحدة:
```bash
# Dump and reconstruct working repository in one command
git-dumper https://target.com/.git/ ./recovered_repo/

# Run through HTTP/SOCKS proxy
git-dumper --proxy http://127.0.0.1:8080 https://target.com/.git/ ./recovered_repo/
```
بمجرد الانتهاء، يمكنك استخدام أوامر `git` العادية داخل المجلد:
```bash
cd ./recovered_repo/
git log -p              # Review all commit diffs and historical edits
git status              # Check untracked files
git branch -a           # List all remote and local branches
```
---

## 🔍 البحث عن الأسرار وأوراق الاعتماد بعد إعادة الإعمار

بمجرد استرداد المستودع، ابحث عن بيانات الاعتماد والملفات المخفية:
```bash
cd ./recovered_repo/

# Search for database strings and API keys
grep -Erio "password|secret|api_key|token|AWS" .

# Scan the reconstructed repo with Gitleaks / TruffleHog
gitleaks detect -v
trufflehog git file://$(pwd)
```
---

## ⚡ جدول مرجعي سريع

| الهدف | الأمر |
|-----------|--------|
| ** تفريغ باستخدام git-dumper (Python) **| `git-dumper https://target.com/.git/ ./repo/` |
| ** تفريغ باستخدام GitTools Bash ** | `./Dumper/gitdumper.sh https://target.com/.git/ ./dump/` |
| **استخراج الالتزامات إلى المصدر** | `./Extractor/extractor.sh ./dump/ ./source/` |
| **مسح قائمة المضيفين بحثًا عن .git** | `python3 Finder/finder.py -i domains.txt` |
| **عرض سجل الالتزام** | `git log -p --color` |
| ** اصطياد الأسرار المشفرة ** | `gitleaks detect -v` |

---

## 🛡️ الاعتبارات الدفاعية وتقوية خادم الويب

1. **تصلب Nginx:**
   ```nginx
   location ~ /\.git {
       deny all;
       return 404;
   }
   ```
2. **تصلب أباتشي (`.htaccess`):**
   ```apache
   RedirectMatch 404 /\.git
   ```
3. **أفضل ممارسات CI/CD:** لا تنشر التعليمات البرمجية مطلقًا عن طريق استنساخ git مباشرة في جذر الويب (`/var/www/html/`). استخدم حزم القطع الأثرية النظيفة أو حاويات Docker.

---

## ⚠️إخلاء المسؤولية القانونية

> [!تنبيه]
> تم تصميم GitTools وgit-dumper لتقييمات الثغرات الأمنية المعتمدة وتدقيق البنية التحتية الدفاعية. يعد تنزيل كود المصدر الخاص من تطبيقات الويب غير المصرح بها دون إذن أمرًا غير قانوني.