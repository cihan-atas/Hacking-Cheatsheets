<!-- language-home-navigation -->
[🏠 الصفحة الرئيسية العربية](../../README.ar.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🎯 Wfuzz - ورقة الغش الكاملة

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ██╗    ██╗███████╗██╗   ██╗███████╗███████╗
  ██║    ██║██╔════╝██║   ██║╚══███╔╝╚══███╔╝
  ██║ █╗ ██║█████╗  ██║   ██║  ███╔╝   ███╔╝ 
  ██║███╗██║██╔══╝  ██║   ██║ ███╔╝   ███╔╝  
  ╚███╔███╔╝██║     ╚██████╔╝███████╗███████╗
   ╚══╝╚══╝ ╚═╝      ╚═════╝ ╚══════╝╚══════╝
       The Flexible Web Application Fuzzer & Parameter Explorer
```
<p align="center">
  <img src="https://img.shields.io/badge/Wfuzz-Web_Fuzzer-blue?style=for-the-badge" alt="Wfuzz">
  <img src="https://img.shields.io/badge/Language-Python-orange?style=for-the-badge" alt="لغة بايثون">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-green?style=for-the-badge" alt="أمان الويب وواجهة برمجة التطبيقات">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="License GPLv2">
</ص>

<p align="center">
  <b>🌐 أداة ضبابية HTTP قابلة للتخصيص بشكل كبير ومصممة لاكتشاف المعلمات المخفية والتجاوزات والرؤوس ومصفوفات الحمولات المتعددة</b>
</ص>

---

## 📋 جدول المحتويات

- [نظرة عامة وهندسة الكلمات الرئيسية](#-نظرة عامة--بنية-الكلمات الرئيسية)
- [مقارنة بين Wfuzz و ffuf](#-wfuzz-vs-ffuf-comparison)
- [التثبيت](#-التثبيت)
- [الدليل الأساسي واكتشاف الملفات](#-الدليل الأساسي--اكتشاف الملفات)
- [تصفية وإخفاء الضوضاء (`--hc`، `--hl`، `--hw`، `--hh`)](#-تصفية--إخفاء-الضوضاء---hc---hl---hw---hh)
- [أنواع الحمولة النافعة وأجهزة التشفير الديناميكية (`-z`)](#-أنواع الحمولة النافعة--التشفيرات الديناميكية--z)
- [المعلمة وتشويش المتغير المخفي](#-المعلمة--إخفاء-المتغير-التشويش)
- [العناوين وملفات تعريف الارتباط والمصادقة](#-الرؤوس-ملفات تعريف الارتباط--المصادقة)
- [الضجيج متعدد المتغيرات (`FUZZ` و`FUZ2Z`)](#-الضجيج متعدد المتغيرات-الضباب-و-fuz2z)
- [التوجيه من خلال مجموعة التجشؤ (`-p`)](#-routing-through-burp-suite--p)
- [الجدول المرجعي السريع](#-الجدول المرجعي السريع)
- [الاعتبارات الدفاعية وOPSEC](#-الاعتبارات الدفاعية--opsec)
- [إخلاء المسؤولية القانونية](#-إخلاء المسؤولية القانونية)

---

## 🎯 نظرة عامة وهندسة الكلمات الرئيسية

كتب **Xavi Mendez** بلغة Python، **Wfuzz** هي أداة كلاسيكية تم اختبارها في المعركة لتقييم تطبيقات الويب. فهو يستبدل العناصر النائبة المعينة (الافتراضي: `FUZZ`) داخل عناوين URL والمعلمات والرؤوس ونصوص POST بإدخالات القاموس أو الحمولات النافعة التي تم إنشاؤها خوارزميًا.

يتميز Wfuzz بتعدد استخدامات الحمولة النافعة التي لا مثيل لها (النطاقات، ومولدات IP، وسلاسل regex، وأجهزة التشفير السريعة مثل MD5 أو Base64) وآليات التصفية المعيارية.

---

## ⚖️ مقارنة بين Wfuzz و ffuf

| ميزة | وفز | ففوف |
|---------|------|------|
| **لغة التنفيذ** | بايثون | اذهب |
| **السرعة الخام** | ⭐⭐⭐ (جيد) | ⭐⭐⭐⭐⭐ (سريع للغاية) |
| ** أجهزة ترميز الحمولة الصافية الديناميكية **| ✅ أصلي (`-z file,pass.txt,md5`)| ❌ يتطلب نصوصًا خارجية |
| **مجموعات الحمولة المتعددة**| ✅ أصلي (`FUZZ`, `FUZ2Z`, `FUZ3Z`)| ✅ مدعوم (`-w w1:W1 -w w2:W2`)|
| **مولدات الحمولة النافعة المدمجة**| ✅ النطاقات، عناوين IP، السداسية، القواميس| ❌قوائم كلمات الملف فقط |
| **استخدام الموارد** | ذاكرة الوصول العشوائي / وحدة المعالجة المركزية معتدلة | بصمة ذاكرة منخفضة |

---

## 📥 التثبيت
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y wfuzz

# Python Pip
pip3 install wfuzz

# Arch Linux
sudo pacman -S wfuzz
```
---

## 🚀 الدليل الأساسي واكتشاف الملفات
```bash
# Basic directory fuzzing
wfuzz -w /usr/share/wordlists/dirb/common.txt http://target.local/FUZZ

# Fuzzing file extensions
wfuzz -w extensions.txt http://target.local/index.FUZZ

# Fuzzing hidden backup files
wfuzz -w words.txt http://target.local/FUZZ.bak
```
---

## 🎯 تصفية وإخفاء الضوضاء (`--hc`، `--hl`، `--hw`، `--hh`)

تعرض خوادم العالم الحقيقي الاستجابات حتى للصفحات غير الموجودة (404 صفحة مخصصة أو عمليات إعادة التوجيه). يوفر Wfuzz إشارات تصفية دقيقة:
```bash
# Hide specific HTTP status codes (e.g. 404, 403)
wfuzz -w wordlist.txt --hc 404,403 http://target.local/FUZZ

# Hide responses by line count (filters out static custom 404 pages)
wfuzz -w wordlist.txt --hl 42 http://target.local/FUZZ

# Hide responses by word count
wfuzz -w wordlist.txt --hw 120 http://target.local/FUZZ

# Hide responses by character count / body length
wfuzz -w wordlist.txt --hh 0,1052 http://target.local/FUZZ

# Show ONLY HTTP 200 and 302 responses
wfuzz -w wordlist.txt --sc 200,302 http://target.local/FUZZ
```
---

## 📦 أنواع الحمولة وأجهزة التشفير الديناميكية (`-z`)

يوفر Wfuzz مولدات مدمجة تلغي الحاجة إلى الملفات النصية التي تم إنشاؤها مسبقًا:
```bash
# Numeric Range Fuzzing (e.g., IDOR testing from 1 to 500)
wfuzz -z range,1-500 http://target.local/user.php?id=FUZZ

# Custom Inline List
wfuzz -z list,admin-root-test-dev http://target.local/FUZZ/

# On-the-fly MD5 encoding of wordlist entries
wfuzz -z file,passwords.txt,md5 http://target.local/login?hash=FUZZ

# On-the-fly Base64 encoding
wfuzz -z file,usernames.txt,base64 http://target.local/auth?token=FUZZ
```
---

## 🔍 تشويش المعلمات والمتغيرات المخفية

اكتشف معلمات GET/POST غير المرتبطة:
```bash
# GET parameter discovery
wfuzz -w /usr/share/seclists/Discovery/Web-Content/burp-parameter-names.txt \
  --hc 404 --hh 2340 "http://target.local/search.php?FUZZ=test"

# POST body parameter discovery
wfuzz -w parameters.txt -d "FUZZ=admin" --hc 404 http://target.local/api/update
```
---

## 🍪 العناوين وملفات تعريف الارتباط والمصادقة
```bash
# Cookie Fuzzing (Session hijacking / privilege testing)
wfuzz -w wordlist.txt -b "session=FUZZ" http://target.local/dashboard

# HTTP Header Fuzzing (IP bypasses via X-Forwarded-For)
wfuzz -z file,ips.txt -H "X-Forwarded-For: FUZZ" http://target.local/admin

# Basic Authentication brute force
wfuzz -z file,users.txt -z file,passwords.txt --basic "FUZZ:FUZ2Z" http://target.local/protected/
```
---

## ⚡ تشويش متعدد المتغيرات (`FUZZ` و`FUZ2Z`)

دمج قوائم كلمات متعددة باستخدام علامات العناصر النائبة المميزة:
```bash
# Test username and password matrix simultaneously
wfuzz -w users.txt -w passwords.txt -d "user=FUZZ&pass=FUZ2Z" --hc 200 http://target.local/login.php

# Fuzz path and extension together
wfuzz -w words.txt -w extensions.txt http://target.local/FUZZ.FUZ2Z
```
---

## 🔁 التوجيه من خلال مجموعة التجشؤ (`-p`)

فحص الحمولات والاستجابات داخل Burp Suite:
```bash
# Route requests through local Burp Suite proxy
wfuzz -p 127.0.0.1:8080:HTTP -w wordlist.txt http://target.local/FUZZ
```
---

## ⚡ جدول مرجعي سريع

| الهدف | الأمر |
|-----------|--------|
| **تزجيج الدليل** | `wfuzz -w <DICT> <URL>/FUZZ` |
| **إخفاء رموز حالة HTTP** | `wfuzz -w <DICT> --hc 404,403 <URL>/FUZZ` |
| **إخفاء حسب طول الرد** | `wfuzz -w <DICT> --hh <LENGTH> <URL>/FUZZ` |
| **معلمة الحصول على الزغب** | `wfuzz -w <PARAMS> "<URL>?FUZZ=test"` |
| ** جسم ما بعد الزغب ** | `wfuzz -w <PARAMS> -d "FUZZ=test" <URL>` |
| **حمولات النطاق الرقمي** | `wfuzz -z range,1-100 "<URL>?id=FUZZ"` |
| **تشفير الحمولات بسرعة**| `wfuzz -z file,<DICT>,base64 "<URL>?token=FUZZ"` |
| ** تشويش الكلمات الرئيسية المتعددة ** | `wfuzz -w <W1> -w <W2> -d "u=FUZZ&p=FUZ2Z" <URL>` |
| **الطريق عبر Burp Proxy** | `wfuzz -p 127.0.0.1:8080:HTTP -w <DICT> <URL>/FUZZ` |

---

## 🛡️ الاعتبارات الدفاعية وOPSEC

1. **حظر WAF:** يؤدي تشويش مئات الطلبات في الثانية إلى تشغيل قيود معدل Cloudflare وAWS WAF وModSecurity. استخدم `-s <seconds>` لتقليل سرعة الطلب.
2. **استنفاد الموارد:** قد يؤدي التشويش الشديد على استعلامات قاعدة البيانات غير المفهرسة إلى حدوث حالات رفض الخدمة (DoS) على خوادم الويب القديمة.

---

## ⚠️إخلاء المسؤولية القانونية

> [!تنبيه]
> ورقة الغش هذه مخصصة لاختبار الاختراق المعتمد وتدقيق الثغرات الأمنية فقط. يمنع منعا باتا تشويش التطبيقات غير المصرح بها.