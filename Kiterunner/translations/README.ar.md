<!-- language-home-navigation -->
[🏠 الصفحة الرئيسية العربية](../../README.ar.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🪁 Kiterunner - ورقة الغش الكاملة

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ██╗  ██╗██╗████████╗███████╗██████╗ ██╗   ██╗███╗   ██╗███╗   ██╗███████╗██████╗ 
  ██║ ██╔╝██║╚══██╔══╝██╔════╝██╔══██╗██║   ██║████╗  ██║████╗  ██║██╔════╝██╔══██╗
  █████╔╝ ██║   ██║   █████╗  ██████╔╝██║   ██║██╔██╗ ██║██╔██╗ ██║█████╗  ██████╔╝
  ██╔═██╗ ██║   ██║   ██╔══╝  ██╔══██╗██║   ██║██║╚██╗██║██║╚██╗██║██╔══╝  ██╔══██╗
  ██║  ██╗██║   ██║   ███████╗██║  ██║╚██████╔╝██║ ╚████║██║ ╚████║███████╗██║  ██║
  ╚═╝  ╚═╝╚═╝   ╚═╝   ╚══════╝╚═╝  ╚═╝ ╚═════╝ ╚═╝  ╚═══╝╚═╝  ╚═══╝╚══════╝╚═╝  ╚═╝
       Context-Aware API Endpoint & Route Discovery Engine by Assetnote
```
<p align="center">
  <img src="https://img.shields.io/badge/Kiterunner-API_Discovery-blue?style=for-the-badge" alt="Kiterunner">
  <img src="https://img.shields.io/badge/Author-Assetnote-orange?style=for-the-badge" alt="Assetnote">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-green?style=for-the-badge" alt="أمان الويب وواجهة برمجة التطبيقات">
  <img src="https://img.shields.io/badge/License-Apache_2.0-red?style=for-the-badge" alt="License Apache 2.0">
</ص>

<p align="center">
  <b>🌐 أداة توجيه REST وGraphQL API عالية الأداء مصممة لرسم خريطة لبنيات واجهة برمجة التطبيقات الحديثة باستخدام مجموعات البيانات السياقية</b>
</ص>

---

## 📋 جدول المحتويات

- [نظرة عامة ومشكلة اكتشاف واجهة برمجة التطبيقات](#-نظرة عامة--مشكلة-اكتشاف واجهة برمجة التطبيقات)
- [مقارنة الأدوات: Kiterunner vs ffuf vs dirsearch](#-tool-comparison-kiterunner-vs-ffuf-vs-dirsearch)
- [التثبيت](#-التثبيت)
- [تنزيل مجموعات بيانات Assetnote Kite](#-downloading-assetnote-kite-datasets)
- [واجهات برمجة التطبيقات للمسح (`kr scan`)](#-scanning-apis-kr-scan)
- [التأثير الغاشم لقائمة الكلمات القياسية (`kr brute`)](#-قائمة الكلمات القياسية-brute-forcing-kr-brute)
- [العناوين وملفات تعريف الارتباط والمصادقة](#-الرؤوس-ملفات تعريف الارتباط--المصادقة)
- [التصفية وأكواد الحالة](#-التصفية--رموز الحالة)
- [إعادة تشغيل المسارات المكتشفة (إخراج `curl`)](#-إعادة تشغيل المسارات المكتشفة-curl-output)
- [الجدول المرجعي السريع](#-الجدول المرجعي السريع)
- [الاعتبارات الدفاعية وOPSEC](#-الاعتبارات الدفاعية--opsec)
- [إخلاء المسؤولية القانونية](#-إخلاء المسؤولية القانونية)

---

## 🎯 نظرة عامة ومشكلة اكتشاف واجهة برمجة التطبيقات

تستخدم أدوات اكتشاف محتوى الويب التقليدية (`ffuf`، و`dirsearch`، و`gobuster`) قواميس مسطحة أحادية الكلمة (على سبيل المثال، `admin`، و`api`، و`login`). على الرغم من أن هذا يعمل مع التسلسلات الهرمية للملفات التقليدية، إلا أنه يفشل في مواجهة واجهات برمجة تطبيقات REST وGraphQL الحديثة**.

تستخدم واجهات برمجة التطبيقات الحديثة مسارات متداخلة ومتعددة المستويات مثل:
`/api/v2/organizations/{org_id}/users/export?format=json`

**Kiterunner**، التي أنشأتها **Assetnote**، تحل هذه المشكلة من خلال تجميع تعريفات OpenAPI وSwagger وPostman والمخططات الواقعية في مجموعات بيانات ثنائية مدمجة (`.kite`). بدلاً من تخمين الكلمات العشوائية، يقوم Kiterunner بإعادة بناء هياكل استدعاء API الهرمية بالكامل باستخدام أساليب HTTP الصحيحة (`GET`، `POST`، `PUT`، `DELETE`).

---

## ⚖️ مقارنة الأدوات: Kiterunner vs ffuf vs dirsearch

| ميزة | كيتيرنر (`kr`) | ففوف | ديرسيرتش |
|---------|-------------------|------|-----------|
| ** التركيز على الهدف الأساسي ** | REST، GraphQL، OpenAPI | تزجيج الويب العام | دليل القوة الغاشمة |
| **التوعية بالتسلسل الهرمي للطريق**| ✅ أصلية (ملفات `.kite`) | ❌ قائمة الكلمات المسطحة | ❌ قائمة الكلمات المسطحة |
| ** تبديل فعل HTTP ** | ✅ الحصول على الاختبارات ونشرها ووضعها وحذفها| ❌ التكوين اليدوي | ❌ محدودة |
| ** تحليل Swagger / OpenAPI ** | ✅ استيعاب المخطط الأصلي | ❌ لا | ❌ لا |
| ** السرعة والتزامن ** | ⭐⭐⭐⭐⭐ (محرك الذهاب) | ⭐⭐⭐⭐⭐ (محرك الذهاب) | ⭐⭐⭐⭐(بايثون) |

---

## 📥 التثبيت
```bash
# Download precompiled binary from GitHub Releases
wget https://github.com/assetnote/kiterunner/releases/download/v1.0.2/kiterunner_1.0.2_linux_amd64.tar.gz
tar -xzvf kiterunner_1.0.2_linux_amd64.tar.gz
sudo mv kr /usr/local/bin/

# Verify installation
kr version
```
---

## 📦 تنزيل مجموعات بيانات Assetnote Kite

يعتمد Kiterunner على مجموعات بيانات مسار `.kite` المعدة مسبقًا والتي تم إنشاؤها من ملايين واجهات برمجة التطبيقات العامة التي تم تحليلها:
```bash
# Create wordlists directory
mkdir -p ~/.kiterunner && cd ~/.kiterunner

# Small dataset (~40MB, 2.5 million API routes - Recommended for fast scans)
wget https://wordlists-cdn.assetnote.io/data/kiterunner/routes-small.kite.tar.gz
tar -xzvf routes-small.kite.tar.gz

# Large dataset (~130MB, 8.5 million API routes - Exhaustive testing)
wget https://wordlists-cdn.assetnote.io/data/kiterunner/routes-large.kite.tar.gz
tar -xzvf routes-large.kite.tar.gz
```
---

## 🚀 مسح واجهات برمجة التطبيقات (`kr scan`)

يستخدم الأمر `scan` مجموعات بيانات `.kite` لاستكشاف الأهداف باستخدام التسلسلات الهرمية للمسار السياقي:
```bash
# Scan a single target using the small dataset
kr scan https://target.local/api -w ~/.kiterunner/routes-small.kite

# Scan multiple hosts from a file with 100 concurrent workers
kr scan targets.txt -w ~/.kiterunner/routes-small.kite -c 100 -j 50

# Scan with maximum verbosity
kr scan https://target.local -w ~/.kiterunner/routes-small.kite -v
```
---

## 🔨 التأثير الغاشم لقائمة الكلمات القياسية (`kr brute`)

إذا كنت تريد استخدام قوائم الكلمات القياسية `.txt` مع الاستفادة من محرك Kiterunner متعدد الأساليب API:
```bash
# Brute force standard paths using a text dictionary
kr brute https://target.local -w /usr/share/wordlists/dirb/common.txt

# Brute force with custom API prefixes
kr brute https://target.local/api/v1 -w endpoints.txt
```
---

## 🔑 العناوين وملفات تعريف الارتباط والمصادقة

تتطلب واجهات برمجة التطبيقات في كثير من الأحيان JWTs أو الرموز المميزة لحاملها أو رؤوس المؤسسة المخصصة:
```bash
# Pass custom Authorization header
kr scan https://target.local/api -w routes-small.kite -H "Authorization: Bearer eyJhbGci..."

# Pass custom API key and Cookie
kr scan https://target.local -w routes-small.kite -H "X-API-Key: secret123" -H "Cookie: session=xyz"
```
---

## 🎯 رموز التصفية والحالة
```bash
# Exclude standard error codes (404, 403, 502)
kr scan https://target.local -w routes-small.kite -x 404,403,502

# Filter out responses by length / content size
kr scan https://target.local -w routes-small.kite --filter-length 0,324
```
---

## 🔄 إعادة تشغيل المسارات المكتشفة (مخرج `curl`)

بإمكان Kiterunner إخراج المسارات المكتشفة كأوامر `curl` قابلة للتنفيذ، مما يسمح بإعادة الإنتاج الفوري في الوحدة الطرفية أو Burp Suite:
```bash
# Print reproducible curl command lines for all successful matches
kr scan https://target.local -w routes-small.kite -o json | jq -r '.[] | .curl'
```
---

## ⚡ جدول مرجعي سريع

| الهدف | الأمر |
|-----------|--------|
| **مسح واجهة برمجة التطبيقات باستخدام مجموعة بيانات صغيرة** | `kr scan <URL> -w routes-small.kite` |
| **مسح واجهة برمجة التطبيقات باستخدام مجموعة بيانات كبيرة** | `kr scan <URL> -w routes-large.kite` |
| ** مسح المضيفين المتعددين ** | `kr scan hosts.txt -w routes-small.kite -c 100` |
| ** قائمة الكلمات النصية للقوة الغاشمة ** | `kr brute <URL> -w wordlist.txt` |
| **المصادقة باستخدام الرمز المميز لحاملها**| `kr scan <URL> -w routes-small.kite -H "Authorization: Bearer <TOKEN>"` |
| **استبعاد رموز الحالة** | `kr scan <URL> -w routes-small.kite -x 404,403` |
| **الإخراج إلى ملف JSON** | `kr scan <URL> -w routes-small.kite -o json -oFile results.json` |

---

## 🛡️ الاعتبارات الدفاعية وOPSEC

1. ** حجم الطلب الضخم: ** يولد قاموس `routes-large.kite` ملايين الطلبات. تأكد من تمكين تحديد المعدل (`-q 200`) عند اختبار بيئات الإنتاج.
2. **طرق تغيير الحالة:** يرسل Kiterunner طلبات `POST` و`PUT` و`DELETE`. في واجهات برمجة التطبيقات الاختبارية غير المقيدة، يمكن أن يؤدي ذلك إلى تعديل أو حذف سجلات الواجهة الخلفية عن غير قصد.

---

## ⚠️إخلاء المسؤولية القانونية

> [!تنبيه]
> تم تصميم Kiterunner لاختبار أمان API المعتمد وارتباطات مكافآت الأخطاء فقط. يعد تشويش واجهات برمجة التطبيقات غير المصرح بها أمرًا غير قانوني تمامًا.