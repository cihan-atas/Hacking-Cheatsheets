<!-- language-home-navigation -->
[🏠 الصفحة الرئيسية العربية](../../README.ar.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ورقة الغش الخاصة بالاسم-التجزئة (الرقم ).

[Name-That-Hash](https://github.com/HashPals/Name-That-Hash) (`nth`) هو محرك حديث وسريع للغاية لتحديد التجزئة يحدد بدقة التجزئات غير المعروفة والتجزئات المضمنة في النص أو الملفات، ويخرج أوضاع Hashcat (`-m`) وJohn the Ripper (`--format`) المباشرة. يقوم تلقائيًا بفك تشفير الترميزات المتداخلة مثل Base64 وHex.

---

## جدول المحتويات

- [الميزات الرئيسية](#key-features)
- [التثبيت](#التثبيت)
- [تعريف تجزئة CLI](#cli-hash-identification)
- [التجزئة المتعددة ومعالجة الملفات](#multi-hash--معالجة الملفات)
- [إخراج تنسيق Hashcat وJohn the Ripper](#hashcat--john-the-ripper-format-output)
- [فك التشفير التلقائي ومرشحات الشعبية](#auto-decode--popularity-filters)
- [التكامل مع تكسير خطوط الأنابيب](#التكامل-مع-تكسير-خطوط الأنابيب)

---

## الميزات الرئيسية

- **300+ أنواع التجزئة:** يغطي الخوارزميات الحديثة، وتنسيقات Active Directory، وتجزئة قاعدة البيانات، وتوقيعات CMS، والأصفار الباطنية.
- ** تعيين Hashcat & John Flag: ** يعرض على الفور قيم `-m <mode>` و`--format=<format>`.
- **فك التشفير التلقائي:** يزيل تشفير Base64 وHex وURL قبل حساب نوع التجزئة.
- **سلسلة فرعية / بحث نصي:** يبحث عن التجزئات المدفونة داخل السجلات أو تفريغ الذاكرة أو ملفات التكوين.
- **فرز الشعبية:** ترتيب النتائج حسب احتمالية الاستخدام في العالم الحقيقي.

---

## تثبيت
```bash
# Via pipx (recommended for isolated CLI tools)
pipx install name-that-hash

# Via standard Python pip
python3 -m pip install --user name-that-hash

# Kali Linux / Debian repository
sudo apt-get update && sudo apt-get install -y python3-name-that-hash

# Verify installation
nth --version
```
---

## تحديد تجزئة CLI

### 1. تحديد تجزئة واحدة
```bash
nth -t "5d41402abc4b2a76b9719d911017c592"
```
### 2. تحديد أوضاع تنسيق Hashcat وJohn مباشرة
اعرض فقط أنواع التجزئة التي تدعم أوضاع Hashcat أو John cracking:
```bash
nth -t "b8c37e33defde51cf91e1e03e51657da" --hashcat --john
```
### 3. عرض المعلومات التفصيلية (الوصف، السرعة، الأمثلة)
```bash
nth -t "$6$rounds=5000$usesomesalt$..." -v
```
---

## معالجة الملفات والتجزئة المتعددة

### 1. تحديد التجزئة داخل الملف (سطرًا بسطر)
```bash
nth -f /path/to/hashes.txt
```
### 2. قم بتصفية النتائج لأعلى التصنيفات الشائعة فقط
قلل النتائج الإيجابية الخاطئة من أنواع التجزئة الغامضة عن طريق التصفية حسب الشعبية:
```bash
# Level 1: Most popular hashes only (MD5, SHA1, SHA256, NTLM, bcrypt, etc.)
nth -f hashes.txt --popularity 1
```
### 3. بحث واستخراج التجزئة المضمنة في النص غير المنظم
فحص سلاسل الذاكرة الأولية أو ملفات السجل أو التعليمات البرمجية المصدر بحثًا عن التجزئة:
```bash
nth --text "Found hash in config: 098f6bcd4621d373cade4e832627b4f6 on port 8080"
```
---

## إخراج تنسيق Hashcat & John the Ripper

يجعل `nth` من السهل إدخال التجزئة في المفرقعات دون حفظ أرقام الأوضاع:

| نموذج لتنسيق التجزئة | الخوارزمية المكتشفة | وضع التجزئة (`-m`) | تنسيق جون (`--format`) |
|---|---|---|---|
| `e80b5017098950fc58aad83c8c14978e` | ام دي 5 | `0` | `raw-md5` |
| `b4b9b02e6f09a9bd760f388b67351e2b` | NTLM | `1000` | `nt` |
| `284ddc0d513372c7d764a0ff813473f2...` | شا-256 | `1400` | `raw-sha256` |
| `$2a$12$DOW8W6...` | بكريبت | `3200` | `bcrypt` |
| `$6$salt$hash...` | SHA-512 (يونكس) | `1800` | `sha512crypt` |
| `$krb5tgs$23$*...` | كيربيروست (TGS-REP) | `13100` | `krb5tgs` |
| `$krb5asrep$23$...` | تحميص AS-REP | `18200` | `krb5asrep` |

---

## فك التشفير التلقائي ومرشحات الشعبية

تقوم العديد من التطبيقات بتخزين التجزئة في Base64 أو Hex قبل إدراج قاعدة البيانات. يمكن لـ `nth` عكس هذا تلقائيًا:
```bash
# Decode Base64 hash before checking
nth -t "XUkQS8xLJnY5cZ0REXxFkg==" --base64

# Decode hex encoded string before checking
nth -t "3564343134303261626334623261373662393731396439313130313763353932" --hex
```
---

## التكامل مع تكسير خطوط الأنابيب

قم بإرسال تجزئة غير معروفة بسرعة إلى Hashcat باستخدام التعريف الآلي:
```bash
# 1. Check hash and capture Hashcat mode
HASH="b4b9b02e6f09a9bd760f388b67351e2b"
MODE=$(nth -t "$HASH" --hashcat --popularity 1 | grep -oP '(?<=Hashcat: -m )\d+' | head -n 1)

# 2. Launch Hashcat with identified mode
hashcat -m "$MODE" -a 0 "$HASH" /usr/share/wordlists/rockyou.txt
```