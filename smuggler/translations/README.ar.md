<!-- language-home-navigation -->
[🏠 الصفحة الرئيسية العربية](../../README.ar.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 📦 المهرب - ورقة الغش كاملة

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ███████╗███╗   ███╗██╗   ██╗ ██████╗  ██████╗ ██╗     ███████╗██████╗ 
  ██╔════╝████╗ ████║██║   ██║██╔════╝ ██╔════╝ ██║     ██╔════╝██╔══██╗
  ███████╗██╔████╔██║██║   ██║██║  ███╗██║  ███╗██║     █████╗  ██████╔╝
  ╚════██║██║╚██╔╝██║██║   ██║██║   ██║██║   ██║██║     ██╔══╝  ██╔══██╗
  ███████║██║ ╚═╝ ██║╚██████╔╝╚██████╔╝╚██████╔╝███████╗███████╗██║  ██║
  ╚══════╝╚═╝     ╚═╝ ╚═════╝  ╚═════╝  ╚═════╝ ╚══════╝╚══════╝╚═╝  ╚═╝
             High-Performance HTTP Request Smuggling Scanner
```
<p align="center">
  <img src="https://img.shields.io/badge/smuggler-HTTP_Smuggling-blue?style=for-the-badge" alt="smuggler">
  <img src="https://img.shields.io/badge/Author-Evan_Ricaurte-orange?style=for-the-badge" alt="defparam">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-green?style=for-the-badge" alt="أمان الويب">
  <img src="https://img.shields.io/badge/License-MIT-purple?style=for-the-badge" alt="MIT">
</ص>

<p align="center">
  <b>📦 أداة Python معيارية فائقة السرعة مصممة لاكتشاف الثغرات الأمنية في عمليات تهريب طلبات HTTP والتحقق منها (CL.TE وTE.CL وعمليات إلغاء مزامنة طفرة الرأس) عبر البنية التحتية الحديثة للويب</b>
</ص>

---

## 📋 جدول المحتويات

- [نظرة عامة ونظرية تهريب HTTP](#-نظرة عامة--http-smuggling-theory)
  - [تناقضات CL.TE مقابل TE.CL](#تناقضات clte-vs-tecl)
- [التثبيت](#-التثبيت)
- [المسح الأساسي للهدف](#-المسح الأساسي للهدف)
- [تكوينات المسح ومحركات الطفرة](#-تكوينات المسح--محركات الطفرة)
  - [الافتراضي مقابل الفحص الشامل](#default-vs-exhaustive-scan)
  - [رؤوس وملفات تعريف الارتباط المخصصة](#custom-headers--cookies)
- [مسح الأهداف المتعددة وخطوط الأنابيب](#-مسح الأهداف المتعددة--مسح خطوط الأنابيب)
- [تحليل النتائج ومسارات الاستغلال](#-تحليل-النتائج--مسارات-الاستغلال)
  - [تسميم الكاش عن طريق التهريب](#تسمم الكاش عن طريق التهريب)
  - [تجاوز عناصر التحكم في أمان الواجهة الأمامية](#bypassing-front-end-security-controls)
  - [اختطاف طلبات المستخدمين الآخرين](#hijacking-other-users-requests)
- [الجدول المرجعي السريع](#-الجدول المرجعي السريع)
- [الاعتبارات الدفاعية والمعالجة](#-الاعتبارات الدفاعية--العلاج)
- [إخلاء المسؤولية القانونية](#-إخلاء المسؤولية القانونية)

---

## 🎯 نظرة عامة ونظرية تهريب HTTP

يحدث تهريب طلب HTTP عندما يختلف وكيل الواجهة الأمامية/موازن التحميل وخادم الويب الخلفي على حدود طلب HTTP:

### التناقضات بين CL.TE وTE.CL
- **CL.TE:** تستخدم الواجهة الأمامية `Content-Length`، وتستخدم الواجهة الخلفية `Transfer-Encoding: chunked`.
- **TE.CL:** الواجهة الأمامية تستخدم `Transfer-Encoding: chunked`، والواجهة الخلفية تستخدم `Content-Length`.
- **TE.TE:** كلاهما يدعم `Transfer-Encoding`، ولكن يمكن خداع المرء لتجاهله عن طريق تشويش الرأس (على سبيل المثال، `Transfer-Encoding: xchunked`، علامات التبويب، أو طي الأسطر).

تم إنشاء **defparam (Evan Ricaurte)**، **smuggler** بإدخال حمولات مصممة بعناية لمراقبة تأخيرات التوقيت أو استجابات إلغاء التزامن دون الإخلال باستقرار الخدمة الخلفية.

---

## 📥 التثبيت
```bash
# Clone the repository
git clone https://github.com/defparam/smuggler.git
cd smuggler

# Run using Python 3
python3 smuggler.py -h
```
---

## 🚀 المسح الأساسي للهدف

مسح نقطة نهاية تطبيق ويب واحد:
```bash
# Basic scan against a single target
python3 smuggler.py -u https://target.com/
```
افتراضيًا، يقوم المهرب باختبار سلوكيات `CL.TE` و`TE.CL` باستخدام حمولات التوقيت الآمنة.

---

## ⚙️ مسح التكوينات ومحركات الطفرات

### الفحص الافتراضي مقابل الفحص الشامل
يتضمن المهرب ملفات تعريف مختلفة للحمولة:
- **الملف الافتراضي:** طفرات عدم التزامن السريعة والمشتركة.
- **ملف تعريف شامل:** طفرات شاملة تختبر اختلافات المسافات البيضاء، والرؤوس المتعددة، ورؤوس ترميز النقل المبهمة:
```bash
# Run exhaustive scan testing all mutation gadgets
python3 smuggler.py -u https://target.com/ -c exhaustive
```
### الرؤوس وملفات تعريف الارتباط المخصصة
عندما تكون المصادقة أو رؤوس مضيفة محددة مطلوبة:
```bash
python3 smuggler.py -u https://target.com/api/ \
  -H "Cookie: session=xyz123" \
  -H "X-Forwarded-Host: target.com"
```
---

## 🌐 مسح متعدد الأهداف وخطوط الأنابيب

نقاط نهاية الأنابيب المكتشفة من `httpx` أو `subfinder` مباشرة إلى المهرب:
```bash
# Scan a list of URLs from file
python3 smuggler.py -f live_hosts.txt -t 10

# Adjust timeout per probe (default: 10s)
python3 smuggler.py -u https://target.com/ --timeout 5
```
---

## 💥تحليل النتائج ومسارات الاستغلال

عندما يبلغ المهرب عن تطابق إيجابي:
```text
[CRITICAL] CL.TE Desync Detected: https://target.com/
```
### تسميم المخبأ عن طريق التهريب
قم بتهريب طلب يؤدي إلى إنشاء عملية إعادة توجيه خارج الموقع (على سبيل المثال، معالجة رأس المضيف) لتسميم ذاكرة التخزين المؤقت على الويب التي تخدم مكتبات JavaScript لجميع المستخدمين.

### تجاوز ضوابط الأمان الأمامية
إذا قام وكيل الواجهة الأمامية بحظر `/admin`، فقم بتهريب طلب داخلي ثانوي يصل إلى الواجهة الخلفية مباشرةً دون فحص الوكيل:
```http
POST / HTTP/1.1
Host: target.com
Content-Length: 48
Transfer-Encoding: chunked

0

GET /admin HTTP/1.1
Host: localhost
```
### اختطاف طلبات المستخدمين الآخرين
تهريب بادئة لطلب HTTP. عندما يرسل المستخدم التالي طلبه، يتم إلحاقه بطلبك المهرب، مما يؤدي إلى الكشف عن ملفات تعريف الارتباط الخاصة به ورموز الجلسة.

---

## ⚡ جدول مرجعي سريع

| الهدف | الأمر |
|-----------|--------|
| ** هدف واحد سريع ** | `python3 smuggler.py -u https://target.com/` |
| ** المسح العميق الشامل **| `python3 smuggler.py -u https://target.com/ -c exhaustive` |
| ** مسح قائمة الأهداف ** | `python3 smuggler.py -f targets.txt -t 15` |
| **رؤوس مخصصة/ملفات تعريف الارتباط**| `python3 smuggler.py -u <URL> -H "Cookie: sess=123"` |
| **مهلة مخصصة** | `python3 smuggler.py -u <URL> --timeout 15` |

---

## 🛡️ الاعتبارات الدفاعية والعلاج

1. **فرض HTTP/2 أو HTTP/3 من طرف إلى طرف:** يحدد HTTP/2 أطوال إطار لا لبس فيها، مما يزيل غموض الرأس.
2. **تطبيع رؤوس HTTP:** قم بتكوين الوكلاء العكسيين لرفض الطلبات التي تحتوي على كل من `Content-Length` و`Transfer-Encoding`.
3. **إسقاط الطفرات الغامضة:** إسقاط الطلبات ذات رؤوس `Transfer-Encoding` المبهمة (على سبيل المثال، مسافات قبل النقطتين، قيم متعددة).

---

## ⚠️إخلاء المسؤولية القانونية

> [!تنبيه]
> يمكن أن يؤدي تهريب طلب HTTP إلى تعطيل قوائم الانتظار الخلفية المباشرة وإفساد عقد التخزين المؤقت المشتركة. يجب استخدام المهرب حصريًا على أهداف اختبار مرخصة صراحةً بإذن صارم من مالكي الأصول.