<!-- language-home-navigation -->
[🏠 الصفحة الرئيسية العربية](../../README.ar.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🌐 WhatWeb - ورقة الغش الكاملة

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ██╗    ██╗██╗  ██╗ █████╗ ████████╗██╗    ██╗███████╗██████╗ 
  ██║    ██║██║  ██║██╔══██╗╚══██╔══╝██║    ██║██╔════╝██╔══██╗
  ██║ █╗ ██║███████║███████║   ██║   ██║ █╗ ██║█████╗  ██████╔╝
  ██║███╗██║██╔══██║██╔══██║   ██║   ██║███╗██║██╔══╝  ██╔══██╗
  ╚███╔███╔╝██║  ██║██║  ██║   ██║   ╚███╔███╔╝███████╗██████╔╝
   ╚══╝╚══╝ ╚═╝  ╚═╝╚═╝  ╚═╝   ╚═╝    ╚══╝╚══╝ ╚══════╝╚═════╝ 
       Next Generation Web Scanner & Technology Fingerprinter
```
<p align="center">
  <img src="https://img.shields.io/badge/WhatWeb-Web_Fingerprinter-blue?style=for-the-badge" alt="WhatWeb">
  <img src="https://img.shields.io/badge/Author-Andrew_Horton-orange?style=for-the-badge" alt="urbanadventurer">
  <img src="https://img.shields.io/badge/Category-Recon_&_Scanning-green?style=for-the-badge" alt="Recon & Scanning">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="License GPLv2">
</ص>

<p align="center">
  <b>🌐 ماسح الويب من الجيل التالي المصمم للتعرف على أنظمة إدارة المحتوى (CMS) ومنصات التدوين ومكتبات JavaScript وتقنيات الخادم</b>
</ص>

---

## 📋 جدول المحتويات

- [نظرة عامة وبصمات التكنولوجيا](#-نظرة عامة--بصمات الأصابع التقنية)
- [التثبيت](#-التثبيت)
- [بصمة موقع الويب الأساسية](#-بصمة موقع الويب الأساسية)
- [شرح مستويات العدوان (`-a`)](#-شرح-مستويات-العدوان--أ)
  - [الوضع السلبي التخفي (`-a 1`)](#الوضع السلبي التخفي--a-1)
  - [الوضع العدواني (`-a 3`)](#الوضع العدواني--a-3)
  - [الوضع الثقيل / الغاشم (`-a 4`)](#heavy--الوضع الغاشم--a-4)
- [فحص الشبكات الفرعية والملفات المستهدفة](#-scanning-subnets--target-files)
- [نظام البرنامج المساعد والتدقيق الانتقائي](#-نظام البرنامج المساعد-التدقيق الانتقائي)
- [تسجيل المخرجات وتصدير التقارير](#-تسجيل المخرجات--تصدير التقارير)
- [الجدول المرجعي السريع](#-الجدول المرجعي السريع)
- [الاعتبارات الدفاعية وOPSEC](#-الاعتبارات الدفاعية--opsec)
- [إخلاء المسؤولية القانونية](#-إخلاء المسؤولية القانونية)

---

## 🎯 نظرة عامة وتقنية بصمات الأصابع

قبل إطلاق عمليات استغلال أو تشويش القوة الغاشمة ضد تطبيق ويب، يعد تحديد مجموعته التكنولوجية الدقيقة أمرًا ضروريًا.

تم إنشاء **WhatWeb** بواسطة **Andrew Horton (urbanadventurer)**، وهو يتعرف على أكثر من 1800 تقنية ويب متميزة:
- **أنظمة إدارة المحتوى (CMS):** WordPress، Drupal، Joomla، Ghost، Magento.
- **خوادم الويب والوكلاء:** Apache، Nginx، Microsoft-IIS، Cloudflare، Envoy.
- **الأطر الخلفية:** Laravel، وDjango، وRuby on Rails، وASP.NET، وSpring Boot.
- **مكتبات الواجهة الأمامية:** React، Vue.js، Angular، jQuery، Bootstrap.
- **الأجهزة المضمنة:** كاميرات الويب وأجهزة التوجيه والطابعات وواجهات SCADA.

---

## 📥 التثبيت
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y whatweb

# Arch Linux
sudo pacman -S whatweb

# macOS (Homebrew)
brew install whatweb
```
---

## 🚀 بصمة الموقع الأساسية
```bash
# Scan a single domain
whatweb https://target.local

# Brief one-line output format
whatweb --color=never -q https://target.local

# Verbose detailed report showing evidence matches
whatweb -v https://target.local
```
---

## ⚡ شرح مستويات العدوان (`-a`)

يوازن WhatWeb بين التخفي والعمق باستخدام أربعة مستويات من العدوان:

### الوضع السلبي التخفي (`-a 1`)
يرسل طلب HTTP GET واحدًا إلى عنوان URL المقدم ويحلل رؤوس الاستجابة التي تم إرجاعها وملفات تعريف الارتباط وعلامات HTML دون طلب صفحات إضافية:
```bash
whatweb -a 1 https://target.local
```
### الوضع العدواني (`-a 3`)
يوصى به لاختبارات الاختراق. يتبع عمليات إعادة التوجيه ويطلب ملفات المكونات الإضافية الشائعة (على سبيل المثال، `wp-login.php`، `robots.txt`، تجزئة الأيقونة المفضلة):
```bash
whatweb -a 3 https://target.local
```
### الوضع الثقيل / الغاشم (`-a 4`)
محاولات الآلاف من تخمينات المكونات الإضافية وعناوين URL لبصمات الأصابع المعروفة:
```bash
whatweb -a 4 https://target.local
```
---

## 🌐 فحص الشبكات الفرعية والملفات المستهدفة
```bash
# Scan an entire CIDR network block
whatweb 192.168.1.0/24

# Scan multiple URLs listed in a text file
whatweb -i targets.txt

# Pipe live web endpoints from httpx into WhatWeb
cat live_hosts.txt | whatweb --no-errors
```
---

## 🧩 نظام المكونات الإضافية والتدقيق الانتقائي

يتميز WhatWeb ببنية البرنامج المساعد الغنية:
```bash
# List all 1,800+ available plugins
whatweb -l

# Search for specific plugins (e.g., WordPress or Apache)
whatweb --grep wordpress

# Run ONLY a specific plugin against a target
whatweb --plugins="apache,php,wordpress" https://target.local
```
---

## 📊 تسجيل المخرجات وتصدير التقارير
```bash
# Export results to clean JSON format
whatweb https://target.local --log-json results.json

# Export to XML format
whatweb https://target.local --log-xml results.xml

# Export to compact CSV format
whatweb -i targets.txt --log-brief results.txt
```
---

## ⚡ جدول مرجعي سريع

| الهدف | الأمر |
|-----------|--------|
| **المسح القياسي** | `whatweb <URL>` |
| **تقرير مفصل مطول** | `whatweb -v <URL>` |
| ** المسح السلبي الخفي ** | `whatweb -a 1 <URL>` |
| ** المسح العميق العدواني ** | `whatweb -a 3 <URL>` |
| **مسح الشبكة الفرعية (CIDR)** | `whatweb <SUBNET>/24` |
| **مسح الأهداف من الملف** | `whatweb -i targets.txt` |
| **تصدير إلى JSON** | `whatweb <URL> --log-json report.json` |
| ** التصفية حسب المكونات الإضافية المحددة **| `whatweb --plugins="wordpress,nginx" <URL>` |

---

## 🛡️ الاعتبارات الدفاعية وOPSEC

1. **إخفاء رأس الخادم:** قم بإزالة رؤوس استجابة `Server` و`X-Powered-By` HTTP أو ترتيبها بشكل عشوائي في Apache/Nginx للتغلب على بصمة البانر السلبية.
2. ** إخفاء تجزئة الأيقونة المفضلة: ** يستخدم WhatWeb تجزئات MD5 لـ `/favicon.ico` لتحديد أطر عمل الويب. تخصيص أيقونات الإطار الافتراضي.

---

## ⚠️إخلاء المسؤولية القانونية

> [!تنبيه]
> WhatWeb مخصص للاستطلاع المصرح به، والتدقيق الأمني، وجرد النظام فقط. قد يؤدي فحص الشبكات أو تطبيقات الويب غير المصرح بها دون إذن إلى انتهاك القوانين المعمول بها.