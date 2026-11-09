<!-- language-home-navigation -->
[🏠 الصفحة الرئيسية العربية](../../README.ar.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🔍 dirsearch - ورقة الغش كاملة

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ██████╗ ██╗██████╗ ███████╗███████╗ █████╗ ██████╗  ██████╗██╗  ██╗
  ██╔══██╗██║██╔══██╗██╔════╝██╔════╝██╔══██╗██╔══██╗██╔════╝██║  ██║
  ██║  ██║██║██████╔╝███████╗█████╗  ███████║██████╔╝██║     ███████║
  ██║  ██║██║██╔══██╗╚════██║██╔══╝  ██╔══██║██╔══██╗██║     ██╔══██║
  ██████╔╝██║██║  ██║███████║███████╗██║  ██║██║  ██║╚██████╗██║  ██║
  ╚═════╝ ╚═╝╚═╝  ╚═╝╚══════╝╚══════╝╚═╝  ╚═╝╚═╝  ╚═╝ ╚═════╝╚═╝  ╚═╝
       Advanced Web Path Scanner & Directory Brute-Forcer
```
<p align="center">
  <img src="https://img.shields.io/badge/dirsearch-Web_Bruteforcer-blue?style=for-the-badge" alt="dirsearch">
  <img src="https://img.shields.io/badge/Language-Python_Async-green?style=for-the-badge" alt="Python Async">
  <img src="https://img.shields.io/badge/Recon-Web_Discovery-orange?style=for-the-badge" alt="اكتشاف الويب">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="License GPLv2">
</ص>

<p align="center">
  <b>🌐 ماسح مسار ويب سريع ومليء بالميزات مع معالجة ذكية للامتدادات والتكرار والزحف</b>
</ص>

---

## 📋 جدول المحتويات

- [المقدمة](#-المقدمة)
- [مقارنة الأدوات](#-مقارنة الأدوات)
- [التثبيت](#-التثبيت)
- [الاستخدام الأساسي واختيار الهدف](#-الاستخدام الأساسي--تحديد الهدف)
- [إدارة الامتدادات واللاحقات](#-إدارة-الامتداد--اللاحقة)
- [قوائم الكلمات والقواميس](#-قوائم الكلمات--القواميس)
- [العودة والزحف على الويب](#-العودة--الزحف على الويب)
- [الترابط، تحديد المعدل والتأخير](#-تحديد معدل الترابط--التأخير)
- [تصفية ومطابقة الردود](#-تصفية--مطابقة-الردود)
- [تخصيص طلب HTTP](#-http-request-customization)
  - [الترويسات المخصصة ووكلاء المستخدم](#custom-headers--user-agents)
  - [ملفات تعريف الارتباط والمصادقة](#ملفات تعريف الارتباط--المصادقة)
  - [بيانات النشر وطرق الطلب](#post-data--request-methods)
- [الوكلاء، تور والتهرب](#-proxies-tor--evasion)
- [تنسيقات الإخراج وإعداد التقارير](#-تنسيقات الإخراج--التقارير)
- [وصفات Pentest & Bug Bounty في العالم الحقيقي] (#-real-world-pentest--bug-bounty-recipes)
- [الجدول المرجعي السريع](#-الجدول المرجعي السريع)
- [النصائح وأفضل الممارسات](#-نصائح--أفضل-الممارسات)
- [إخلاء المسؤولية القانونية](#-إخلاء المسؤولية القانونية)

---

## 🎯 المقدمة

**dirsearch** (الذي أنشأه Mauro Soria) هو أحد أدوات سطر الأوامر الأكثر شيوعًا وتنوعًا لاكتشاف الملفات والأدلة المخفية على خوادم الويب. وهو مكتوب بلغة Python، وهو يوفر مرونة لا مثيل لها من خلال وسائط سطر الأوامر الغنية، والمخرجات الطرفية الملونة، والزحف العودي التلقائي، وتوسيع امتداد أحرف البدل الذكية، وعناصر التحكم المضمنة في التهرب.

سواء كنت تقوم بإجراء اختبار اختراق الويب، أو البحث عن مكافآت الأخطاء، أو تقييمات CTF الداخلية، فقد تم تصميم `dirsearch` للكشف عن الواجهات الإدارية المخفية، وقواعد البيانات الاحتياطية، ونقاط نهاية API المنسية، وبيانات الاعتماد الحساسة.

### الميزات الرئيسية

| ميزة | الوصف |
|---------|------------|
| **متعدد الخيوط** | محرك طلب غير متزامن ومترابط عالي الأداء |
| ** أحرف البدل الملحقة ** | قم بتوسيع `-e *` لاختبار جميع امتدادات ملفات الويب المعروفة تلقائيًا |
| ** الزحف المتكامل ** | يتبع روابط HTML/JS لقوائم انتظار الدليل العودية |
| **تصفية غنية** | قم بتصفية الاستجابات حسب رمز الحالة أو حجم البايت أو عدد الكلمات أو عدد الأسطر أو التعبير العادي |
| **إعادة الطلب الخام** | قم بتحميل طلبات Burp Suite HTTP الكاملة عبر `--raw` |
| **تنسيقات متعددة** | يُنشئ تقارير بتنسيق Plaintext وJSON وCSV وMarkdown وHTML |

---

## ⚖️ مقارنة الأدوات

| ميزة | ديرسيرتش | ففوف | فيروكسبوستر | جوبستر | ديرب |
|---------|----------|-------------|-----------|------|----------|--|
| **اللغة** | بايثون | اذهب | الصدأ | اذهب | ج |
| **السرعة** | ⭐⭐⭐⭐ (سريع) | ⭐⭐⭐⭐⭐ (سريع للغاية) | ⭐⭐⭐⭐⭐ (سريع للغاية) | ⭐⭐⭐⭐ (سريع) | ⭐⭐(الخيط المنخفض) |
| ** العودية الافتراضية ** | ✅ أوتوماتيك (`-r`) | اختياري (`-recursion`) | ✅ أوتوماتيك (`-d`) | اختياري (`-r`) | ✅ اوتوماتيك |
| **حرف بدل الامتداد (`*`)**| ✅ أصلي (`-e *`) | ❌ لا | ❌ لا | ❌ لا | ❌ لا |
| ** زاحف الويب المدمج ** | ✅ نعم (`--crawl`) | ❌ لا | ✅ نعم (`--extract-links`) | ❌ لا | ❌ لا |
| **POST / تشويش البيانات** | ✅ نعم (`--data`) | ✅ HTTP كامل | ❌ محدودة | ❌ لا | ❌ لا |
| ** إعادة تشغيل طلب التجشؤ الخام **| ✅ نعم (`--raw`) | ✅ نعم (`-request`) | ❌ لا | ❌ لا | ❌ لا |
| ** قائمة الكلمات المضمنة ** | ✅ برعاية (`db/dicc.txt`)| ❌ لا يوجد | ❌ لا يوجد | ❌ لا يوجد | ✅ قائمة صغيرة |

---

## 📥 التثبيت

### استخدام pipx (موصى به لنظامي Kali وModern Linux)
```bash
# Install via pipx (isolated Python environment)
pipx install dirsearch

# Verify installation
dirsearch --version
```
### استخدام النقطة
```bash
# Install via standard pip
pip3 install dirsearch
```
### استخدام Git Clone (مباشرة من المصدر)
```bash
# Clone the official repository
git clone https://github.com/maurosoria/dirsearch.git --depth 1
cd dirsearch

# Install requirements and run
pip3 install -r requirements.txt
python3 dirsearch.py --version
```
### حزمة كالي لينكس ودوكر
```bash
# Kali / Debian repository
sudo apt update && sudo apt install -y dirsearch

# Docker container
docker run -it --rm maurosoria/dirsearch -u https://example.com -e php,html
```
---

## 🚀 الاستخدام الأساسي واختيار الهدف

### هدف واحد
```bash
# Basic scan against a single URL specifying extensions
dirsearch -u https://target.com -e php,html,js,txt

# Scan with trailing slash forced
dirsearch -u https://target.com -e php -f
```
### أهداف متعددة
```bash
# Scan a list of targets from file
dirsearch -l targets.txt -e php,aspx,txt

# Scan stdin input from subfinder / httpx
cat live_hosts.txt | dirsearch --stdin -e php
```
### فحص المنافذ واستهداف CIDR
```bash
# Scan specific ports on a host
dirsearch -u https://target.com --ports 80,443,8080,8443 -e php

# Scan an entire subnet range
dirsearch --cidr 192.168.1.0/24 -e html
```
---

## 🧩 إدارة الامتدادات واللاحقات

يتمتع `dirsearch` بأقوى نظام لإدارة الامتدادات بين جميع ماسحات الدليل:
```bash
# Specify multiple extensions
dirsearch -u https://target.com -e php,html,json,txt,zip

# Use the wildcard (*) to test ALL known extensions automatically
dirsearch -u https://target.com -e *

# Add prefixes to wordlist entries (e.g. test_admin, dev_admin)
dirsearch -u https://target.com -e php --prefixes test_,dev_,staging_,old_

# Add suffixes to wordlist entries (e.g. admin_backup, admin_old)
dirsearch -u https://target.com -e php --suffixes _bak,~,_2026,_old
```
---

## 📚 قوائم الكلمات والقواميس

يحتوي `dirsearch` على قائمة كلمات شاملة تم اختبارها في المعركة في `db/dicc.txt`:
```bash
# Run using the default bundled dictionary
dirsearch -u https://target.com -e php

# Use SecLists Common or Medium wordlists
dirsearch -u https://target.com -e php -w /usr/share/seclists/Discovery/Web-Content/directory-list-2.3-medium.txt

# Use multiple wordlists simultaneously
dirsearch -u https://target.com -e php -w list1.txt,list2.txt

# Force all wordlist entries to lowercase or uppercase
dirsearch -u https://target.com -e php --lowercase
dirsearch -u https://target.com -e php --uppercase
```
---

## 🔄 العودية والزحف على الويب

### المسح العودي
```bash
# Recursively scan newly found directories
dirsearch -u https://target.com -e php -r

# Limit recursion depth to 2 levels
dirsearch -u https://target.com -e php -r --recursion-depth 2

# Recursively scan only if HTTP status is 200, 301, or 302
dirsearch -u https://target.com -e php -r --recursion-status 200,301,302
```
### زاحف الويب المتكامل
```bash
# Crawl the target website for internal URLs and feed them into the scan queue
dirsearch -u https://target.com -e php --crawl
```
---

## ⚡ الخيوط، تحديد المعدل والتأخير

التحكم في عدوانية الفحص لمنع تشغيل الحد من المعدل أو جدران حماية تطبيقات الويب (WAF):
```bash
# Set number of concurrent threads (default: 25)
dirsearch -u https://target.com -e php -t 50

# Add a delay between requests (e.g. 0.5 seconds)
dirsearch -u https://target.com -e php -s 0.5

# Enforce a maximum request rate per second (e.g. max 15 req/sec)
dirsearch -u https://target.com -e php --max-rate 15

# Customize HTTP connection timeouts and retries
dirsearch -u https://target.com -e php --timeout 8 --retries 3

# Exit scan immediately on excessive connection errors
dirsearch -u https://target.com -e php --exit-on-error
```
---

## 🎯 تصفية ومطابقة الردود

### إدراج واستبعاد رمز الحالة
```bash
# Include only specific status codes (e.g., ignore everything except 200, 301, 403)
dirsearch -u https://target.com -e php -i 200,301,302,403

# Exclude specific status codes (e.g., exclude 404 and 500)
dirsearch -u https://target.com -e php -x 404,400,500,502,503
```
### التصفية حسب الحجم والكلمات والمحتوى
```bash
# Exclude responses by specific byte size (e.g. ignore custom 404 pages)
dirsearch -u https://target.com -e php --exclude-sizes 0B,1542B,3421B

# Exclude responses matching specific text on the page
dirsearch -u https://target.com -e php --exclude-texts "Not Found","Access Denied"

# Exclude responses matching a regular expression
dirsearch -u https://target.com -e php --exclude-regex "^Error:.*"
```
---

## 🛠️ تخصيص طلب HTTP

### الرؤوس المخصصة ووكلاء المستخدم
```bash
# Set a custom header (e.g., authorization token or internal bypass header)
dirsearch -u https://target.com -e php -H "X-Forwarded-For: 127.0.0.1" -H "X-Requested-With: XMLHttpRequest"

# Load headers from a file
dirsearch -u https://target.com -e php --header-list custom_headers.txt

# Use a randomized User-Agent per request to bypass basic WAFs
dirsearch -u https://target.com -e php --random-agent

# Set a custom User-Agent
dirsearch -u https://target.com -e php --user-agent "Mozilla/5.0 (Windows NT 10.0; Win64; x64)"
```
### ملفات تعريف الارتباط والمصادقة
```bash
# Provide session cookies for authenticated scanning
dirsearch -u https://target.com -e php -b "PHPSESSID=abc123xyz; admin_logged=true"

# HTTP Basic Authentication (username:password)
dirsearch -u https://target.com -e php --auth admin:SecretPass123 --auth-type basic

# NTLM Authentication for corporate Windows portals
dirsearch -u https://target.com -e php --auth CORP\\alice:Welcome2026 --auth-type ntlm
```
### نشر البيانات وطرق الطلب
```bash
# Perform fuzzing using HTTP POST requests
dirsearch -u https://target.com/api -m POST -d "action=test&token=xyz" -e json

# Replay an entire raw HTTP request saved from Burp Suite
dirsearch -u https://target.com --raw request.txt -e php
```
---

## 🛡️ الوكلاء والتور والتهرب
```bash
# Route all traffic through an HTTP or SOCKS5 proxy (e.g., Burp Suite)
dirsearch -u https://target.com -e php --proxy http://127.0.0.1:8080

# Route all traffic through the local Tor SOCKS proxy
dirsearch -u https://target.com -e php --tor

# Skip SSL/TLS certificate verification
dirsearch -u https://target.com -e php --no-check-certificate
```
---

## 📄 تنسيقات الإخراج وإعداد التقارير
```bash
# Save output as a clean text file
dirsearch -u https://target.com -e php -o results.txt

# Export results to JSON format
dirsearch -u https://target.com -e php -o results.json --format json

# Export results to HTML, CSV, or Markdown
dirsearch -u https://target.com -e php -o report.html --format html
dirsearch -u https://target.com -e php -o results.csv --format csv
dirsearch -u https://target.com -e php -o report.md --format markdown
```
---

## 🎯 وصفات Pentest ومكافآت الأخطاء في العالم الحقيقي

### 1. استطلاع سريع واسع النطاق للويب
```bash
# Scan with common web extensions, follow redirects, random UA, 40 threads
dirsearch -u https://target.com \
  -e php,html,js,json,txt,zip \
  -t 40 \
  --random-agent \
  -i 200,301,302,403 \
  -o target_recon.txt
```
### 2. البحث عن النسخ الاحتياطية المنسية وتسريبات المصادر
```bash
# Test for database dumps, git repos, env files, and zip archives
dirsearch -u https://target.com \
  -e bak,old,orig,zip,tar.gz,sql,env,git \
  -w /usr/share/seclists/Discovery/Web-Content/raft-medium-files.txt \
  -i 200 \
  --exclude-sizes 0B
```
### 3. اكتشاف الدليل العودي العميق
```bash
# 2-level recursion, crawling pages for new paths, ignoring error status codes
dirsearch -u https://target.com \
  -e php,html \
  -r --recursion-depth 2 \
  --crawl \
  -x 404,500,502 \
  -o deep_scan.json --format json
```
### 4. تقييم البوابة الداخلية الموثقة
```bash
# Scan through Burp Suite with internal session cookies and WAF bypass headers
dirsearch -u https://intranet.corp.local \
  -e aspx,ashx,config,txt \
  -b "ASP.NET_SessionId=xyz123; AuthToken=ey..." \
  -H "X-Forwarded-For: 127.0.0.1" \
  --proxy http://127.0.0.1:8080
```
---

## ⚡ جدول مرجعي سريع

| مهمة | الأمر |
|------|---------|
| **المسح الأساسي مع الامتدادات** | `dirsearch -u https://target.com -e php,html,txt` |
| ** جميع أحرف البدل للامتدادات ** | `dirsearch -u https://target.com -e *` |
| **قائمة الكلمات المخصصة** | `dirsearch -u https://target.com -e php -w /path/to/wordlist.txt` |
| **مسح أهداف متعددة** | `dirsearch -l targets.txt -e php,aspx` |
| **الاكتشاف العودي** | `dirsearch -u https://target.com -e php -r --recursion-depth 2` |
| **الزحف إلى الروابط المستهدفة** | `dirsearch -u https://target.com -e php --crawl` |
| **تضمين رموز الحالة** | `dirsearch -u https://target.com -e php -i 200,301,302,403` |
| **استبعاد رموز الحالة** | `dirsearch -u https://target.com -e php -x 404,500,502` |
| **استبعاد الأحجام الإيجابية الخاطئة**| `dirsearch -u https://target.com -e php --exclude-sizes 0B,1240B` |
| **المصادقة باستخدام ملفات تعريف الارتباط** | `dirsearch -u https://target.com -e php -b "session=abc123"` |
| **الطريق عبر Burp Suite** | `dirsearch -u https://target.com -e php --proxy http://127.0.0.1:8080` |
| **حفظ التقرير إلى JSON** | `dirsearch -u https://target.com -e php -o report.json --format json` |

---

## 💡 النصائح وأفضل الممارسات

1. ** استخدم `-e *` بشكل مقتصد في قوائم الكلمات الكبيرة: ** يؤدي توسيع امتداد حرف البدل إلى مضاعفة كل كلمة بأكثر من 20 امتدادًا. استخدم `-e *` فقط مع القوائم الأصغر مثل `common.txt` أو `dicc.txt`.
2. **تصفية الأحجام الإيجابية الزائفة:** تُرجع أطر عمل الويب الحديثة 404 صفحة مخصصة مع HTTP 200. افحص حجم الاستجابة الافتراضية وأضف `--exclude-sizes <size>` للتخلص من الضوضاء.
3. **التحقق دائمًا من 403 المسارات المحظورة:** في البيئات السحابية والمؤسساتية الحديثة، تشير استجابات HTTP 403 إلى وجود الدليل أو نقطة النهاية. قم بتوثيق جميع 403s الخاصة بتجاوز الرأس واختبار اجتياز المسار.
4. ** الاستفادة من البادئات واللاحقات للنسخ الاحتياطية: ** يقوم المطورون بشكل متكرر بحفظ نسخ احتياطية من الملفات الحساسة (مثل `config.php.bak`، `old_database.sql`). استخدم `--prefixes` و`--suffixes` لاكتشافهما تلقائيًا.

---

## ⚠️إخلاء المسؤولية القانونية

> **هام:** يعد فحص المنافذ غير المصرح به، والضغط الغاشمة على الدليل، وتعداد الويب ضد الخوادم دون الحصول على إذن كتابي صريح أمرًا غير قانوني. استخدم `dirsearch` بشكل صارم في ارتباطات مكافآت الأخطاء القانونية، واختبارات الاختراق المعتمدة، وبيئات مختبرات CTF.