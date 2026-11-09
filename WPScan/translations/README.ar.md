<!-- language-home-navigation -->
[🏠 الصفحة الرئيسية العربية](../../README.ar.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🛡️ WPScan - ورقة الغش الكاملة

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ██╗    ██╗██████╗ ███████╗ ██████╗ █████╗ ███╗   ██╗
  ██║    ██║██╔══██╗██╔════╝██╔════╝██╔══██╗████╗  ██║
  ██║ █╗ ██║██████╔╝███████╗██║     ███████║██╔██╗ ██║
  ██║███╗██║██╔═══╝ ╚════██║██║     ██╔══██║██║╚██╗██║
  ╚███╔███╔╝██║     ███████║╚██████╗██║  ██║██║ ╚████║
   ╚══╝╚══╝ ╚═╝     ╚══════╝ ╚═════╝╚═╝  ╚═╝╚═╝  ╚═══╝
       The WordPress Security & Vulnerability Scanner
```
<p align="center">
  <img src="https://img.shields.io/badge/WPScan-WordPress_Security-blue?style=for-the-badge" alt="WPScan">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-orange?style=for-the-badge" alt="أمان الويب">
  <img src="https://img.shields.io/badge/Database-WPVDB_API-green?style=for-the-badge" alt="WPVDB API">
  <img src="https://img.shields.io/badge/License-GPLv3-red?style=for-the-badge" alt="License GPLv3">
</ص>

<p align="center">
  <b>🌐 أداة فحص الثغرات الأمنية في الصندوق الأسود مصممة لاكتشاف نقاط ضعف WordPress الأساسية والمكونات الإضافية والموضوعات والمستخدمين</b>
</ص>

---

## 📋 جدول المحتويات

- [نظرة عامة والهندسة المعمارية](#-نظرة عامة--الهندسة المعمارية)
- [التثبيت](#-التثبيت)
- [تكامل رمز API (WPVDB)](#-api-token-integration-wpvdb)
- [المسح الأساسي وتعداد الهدف](#-المسح الأساسي--تعداد الهدف)
- [تقسيم أعلام التعداد (`-e`)](#-enumeration-flags-breakdown--e)
  - [الإضافات الضعيفة (`-e vp`)](#vulnerable-plugins--e-vp)
  - [جميع المكونات الإضافية (`-e ap`)](#all-plugins--e-ap)
  - [الموضوعات (`-e vt`, `-e at`)](#themes--e-vt--e-at)
  - [أسماء المستخدمين (`-e u`)](#usernames--e-u)
  - [النسخ الاحتياطية الحساسة (`-e cb`، `-e dbe`)](#النسخ الاحتياطية الحساسة--e-cb--e-dbe)
- [أوضاع الكشف (`--plugins-detection`)](#-أوضاع الكشف---اكتشاف المكونات الإضافية)
- [فرض كلمة المرور الغاشمة (`wp-login.php` وXML-RPC)](#-password-brute-forcing-wp-loginphp--xml-rpc)
- [التهرب من التسلل وWAF](#-التسلل--التهرب من WAF)
- [الجدول المرجعي السريع](#-الجدول المرجعي السريع)
- [الاعتبارات الدفاعية والتصلب](#-الاعتبارات الدفاعية--التصلب)
- [إخلاء المسؤولية القانونية](#-إخلاء المسؤولية القانونية)

---

## 🎯 نظرة عامة والهندسة المعمارية

مع تشغيل WordPress لأكثر من 40% من أفضل 10 ملايين موقع ويب، فهو نظام إدارة المحتوى (CMS) الأكثر استهدافًا في العالم.

**WPScan** المكتوب بلغة روبي، هو أداة فحص الثغرات الأمنية للصندوق الأسود المتوافقة مع معايير الصناعة لعمليات تثبيت WordPress. يقوم بفهرسة الإصدارات الأساسية النشطة، وتعداد المكونات الإضافية والموضوعات المثبتة، واستخراج أسماء المستخدمين الإدارية المسجلة، وربطها مع **قاعدة بيانات الثغرات الأمنية في WPScan (WPVDB)** التي تحتوي على الآلاف من التهديدات الخطيرة المعروفة.

---

## 📥 التثبيت
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y wpscan

# Arch Linux
sudo pacman -S wpscan

# Ruby Gem (Any platform)
gem install wpscan

# Docker Container
docker run -it --rm wpscan/wpscan --url https://target.local
```
---

## 🔑 تكامل رمز واجهة برمجة التطبيقات (WPVDB)

لعرض نقاط الضعف المعروفة ونتائج CVE، يتطلب WPScan رمزًا مميزًا مجانيًا لواجهة برمجة التطبيقات من [wpscan.com](https://wpscan.com/):
```bash
# Provide API token via CLI
wpscan --url https://target.local --api-token YOUR_API_TOKEN

# Or export to environment variable
export WPSCAN_API_TOKEN="YOUR_API_TOKEN"
```
---

## 🚀 المسح الأساسي وتعداد الأهداف
```bash
# Quick non-intrusive scan
wpscan --url https://target.local

# Update the local database before scanning
wpscan --update
```
---

## 🔍 تفصيل أعلام التعداد (`-e`)

يسمح WPScan بتعداد الهدف المعياري باستخدام علامة `--enumerate` (أو `-e`):

### المكونات الإضافية الضعيفة (`-e vp`)
عمليات البحث عن المكونات الإضافية التي لديها ثغرات أمنية معروفة في قاعدة بيانات WPVDB:
```bash
wpscan --url https://target.local -e vp --api-token YOUR_TOKEN
```
### جميع المكونات الإضافية (`-e ap`)
تحقيقات لآلاف من المكونات الإضافية المعروفة عبر القوة الغاشمة للقاموس:
```bash
wpscan --url https://target.local -e ap --plugins-detection aggressive
```
### السمات (`-e vt`، `-e at`)
```bash
# Enumerate vulnerable themes
wpscan --url https://target.local -e vt

# Enumerate all installed themes
wpscan --url https://target.local -e at
```
### أسماء المستخدمين (`-e u`)
يستخرج أسماء مستخدمي المؤلف المسجلين من خلال نقاط نهاية REST API (`/wp-json/wp/v2/users`)، وأرشيفات المؤلف (`/?author=1`)، ورسائل خطأ تسجيل الدخول:
```bash
wpscan --url https://target.local -e u1-10
```
### النسخ الاحتياطية الحساسة (`-e cb`، `-e dbe`)
عمليات البحث عن `wp-config.php.bak` المكشوفة، وتفريغ قاعدة البيانات، وأرشيفات النسخ الاحتياطي:
```bash
wpscan --url https://target.local -e cb,dbe
```
---

## ⚙️ أوضاع الكشف (`--plugins-detection`)

يتحكم في مدى دقة بحث WPScan عن المكونات الإضافية:
```bash
# Passive (Fast, inspects HTML source, script tags, style links)
wpscan --url https://target.local --plugins-detection passive

# Aggressive (Sends HTTP requests for thousands of plugin directories)
wpscan --url https://target.local --plugins-detection aggressive

# Mixed (Recommended default - passive followed by targeted active requests)
wpscan --url https://target.local --plugins-detection mixed
```
---

## 🔓 التأثير الغاضب لكلمة المرور (`wp-login.php` وXML-RPC)

بمجرد تعداد أسماء المستخدمين الصالحة، قم بتشغيل القوة الغاشمة لبيانات الاعتماد المستهدفة:
```bash
# Brute force login using enumerated username list
wpscan --url https://target.local -U users.txt -P /usr/share/wordlists/rockyou.txt

# Password spray a single password across all users
wpscan --url https://target.local -U users.txt -p 'Winter2026!'

# Multi-threaded password attack
wpscan --url https://target.local -U admin -P passwords.txt -t 20
```
> [!ملاحظة]
> إذا تم تمكين `xmlrpc.php` على الخادم، فإن WPScan يستفيد تلقائيًا من أساليب الاتصال المتعدد XML-RPC لاختبار مئات مجموعات كلمات المرور لكل طلب HTTP واحد!

---

## 🥷 التهرب من التخفي وWAF
```bash
# Randomize User-Agent header to evade simple bot signatures
wpscan --url https://target.local --random-user-agent

# Throttle request rate (milliseconds between requests) to bypass rate limits
wpscan --url https://target.local --throttle 1000

# Route through SOCKS proxy / Tor
wpscan --url https://target.local --proxy socks5://127.0.0.1:9050

# Ignore SSL/TLS certificate warnings
wpscan --url https://target.local --disable-tls-checks
```
---

## ⚡ جدول مرجعي سريع

| الهدف | الأمر |
|-----------|--------|
| **المسح القياسي** | `wpscan --url <URL> --api-token <TOKEN>` |
| **تعداد المكونات الإضافية الضعيفة** | `wpscan --url <URL> -e vp --api-token <TOKEN>` |
| **تعداد كافة الإضافات** | `wpscan --url <URL> -e ap --plugins-detection aggressive` |
| **تعداد المستخدمين** | `wpscan --url <URL> -e u` |
| **تعداد النسخ الاحتياطية والتكوينات** | `wpscan --url <URL> -e cb,dbe` |
| ** كلمة المرور الغاشمة ** | `wpscan --url <URL> -U <USERS> -P <PASSWORDS>` |
| ** وكيل مستخدم عشوائي وإخفاء ** | `wpscan --url <URL> --random-user-agent --stealthy` |
| **تصدير إلى ملف JSON** | `wpscan --url <URL> -f json -o wpscan_report.json` |

---

## 🛡️ الاعتبارات الدفاعية والتصلب

1. **تعطيل XML-RPC:** قم بحظر الوصول إلى `xmlrpc.php` عبر تكوين `.htaccess` أو Nginx لتحييد هجمات القوة الغاشمة متعددة المكالمات عالية السرعة.
2. ** تقييد نقطة نهاية مستخدمي REST API: ** قم بتعطيل الوصول العام إلى `/wp-json/wp/v2/users` لمنع تعداد المؤلف التافه.
3. **تصحيح المكونات الإضافية بانتظام:** ما يزيد عن 90% من ثغرات WordPress موجودة في المكونات الإضافية والموضوعات التابعة لجهات خارجية بدلاً من WordPress الأساسية.

---

## ⚠️إخلاء المسؤولية القانونية

> [!تنبيه]
> تم تصميم ورقة الغش هذه لاختبار الاختراق المعتمد وتقييمات الضعف وتدقيق تطبيقات الويب فقط. يعد فرض كلمات المرور أو أنظمة المسح دون الحصول على إذن صريح أمرًا غير قانوني.