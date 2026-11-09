<!-- language-home-navigation -->
[🏠 الصفحة الرئيسية العربية](../../README.ar.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ورقة الغش fcrackzip

[fcrackzip](https://github.com/hyc/fcrackzip) عبارة عن أداة سريعة ومتعددة الخيوط لتكسير كلمات مرور سطر الأوامر لأرشيفات ZIP المشفرة. وهو يدعم كلا من الهجمات المستندة إلى القاموس وهجمات القوة الغاشمة المحسنة ضد ZipCrypto التقليدي (تشفير دفق PKZIP) ويمكنه التحقق من سلامة الأرشيف دون الاستخراج الكامل.

---

## جدول المحتويات

- [الميزات والقيود الأساسية](#الميزات الأساسية--القيود)
- [التثبيت](#التثبيت)
- [هجمات القاموس (`-D -p`)](#dictionary-attacks--d--p)
- [هجمات القوة الغاشمة (`-b`)](#هجمات القوة الغاشمة--ب)
- [مجموعات الأحرف ومواصفات الطول](#مجموعات الأحرف--مواصفات الطول)
- [تحسين الأداء والمعيار](#benchmark--Performance-optimization)
- [ZipCrypto مقابل AES-256 (John/Hashcat البديل)](#zipcrypto-vs-aes-256-johnhashcat-alternative)

---

## الميزات والقيود الأساسية

- **أداة ZipCrypto Cracker فائقة السرعة:** مكتوبة بلغة C، ومُحسَّنة لخوارزمية ZipCrypto القديمة (تشفير PKZIP القياسي).
- **وضع القاموس:** يقرأ كلمات المرور المرشحة بالتسلسل من قوائم الكلمات.
- **وضع القوة الغاشمة:** يُنشئ مرشحين سريعًا باستخدام مجموعات أحرف قابلة للتكوين وحدود الطول.
- **اختبار فك ضغط الأرشيف (`-u`):** يتحقق من كلمات المرور المرشحة عن طريق محاولة فك الضغط، والقضاء فعليًا على النتائج الإيجابية الخاطئة.
- **القيود:** لا **لا** يدعم تشفير WinZip AES-256 الحديث. بالنسبة إلى ملفات ZIP AES-256، استخدم `zip2john` + John the Ripper أو Hashcat (الوضع 13600).

---

## تثبيت
```bash
# Debian / Ubuntu / Kali Linux
sudo apt-get update && sudo apt-get install -y fcrackzip

# Arch Linux / BlackArch
sudo pacman -S fcrackzip

# Fedora / CentOS / RHEL
sudo dnf install -y fcrackzip

# Verify command
fcrackzip -h
```
---

## هجمات القاموس (`-D -p`)

أسرع طريقة للاختبار مقابل قوائم الكلمات الشائعة:

### 1. هجوم القاموس الأساسي مع التحقق من فك الضغط
```bash
fcrackzip -u -D -p /usr/share/wordlists/rockyou.txt secret_archive.zip
```
- `-u`: وضع فك الضغط (يتحقق من الملف المستخرج CRC لتجنب النتائج الإيجابية الخاطئة).
- `-D`: وضع هجوم القاموس.
- `-p <file>`: المسار إلى ملف القاموس.

### 2. وضع الإخراج المطول
عرض السرعة التي تم اختبارها وكلمة المرور الحالية ومعلومات التصحيح:
```bash
fcrackzip -v -u -D -p passwords.txt backup.zip
```
---

## هجمات القوة الغاشمة (`-b`)

عندما تفشل هجمات القاموس، استخدم القوة الغاشمة ضد كلمات المرور القصيرة غير المعروفة:

### 1. أرقام التعريف الشخصية الرقمية ذات القوة الغاشمة (4 إلى 6 أرقام)
```bash
fcrackzip -u -b -c 1 -l 4-6 secure.zip
```
- `-b`: وضع القوة الغاشمة.
- `-c 1`: مجموعة الأحرف: أرقام فقط (`0-9`).
- `-l 4-6`: نطاق الطول: الحد الأدنى 4، الحد الأقصى 6 أحرف.

### 2. أحرف أبجدية رقمية صغيرة (من 1 إلى 5 أحرف)
```bash
fcrackzip -u -b -c a1 -l 1-5 archive.zip
```
- `-c a1`: الحروف الصغيرة (`a-z`) والأرقام (`0-9`).

### 3. أحرف صغيرة + أحرف كبيرة + أرقام + رموز
```bash
fcrackzip -u -b -c aA1! -l 6-8 protected.zip
```
- `-c a`: الحروف الصغيرة (`a-z`).
- `-c A`: الحروف الكبيرة (`A-Z`).
- `-c 1`: الأرقام (`0-9`).
- `-c !`: الرموز الخاصة (`[!:$%&/()=?{[]}+*~#]`).

---

## مجموعات الأحرف ومواصفات الطول

| علم | مجموعة الأحرف متضمنة | المرشحين المثال |
|---|---|---|
| `-c a` | أحرف صغيرة (`a-z`) | `admin`، `secret` |
| `-c A` | الحروف الكبيرة (`A-Z`) | `SECRET`، `ROOT` |
| `-c 1` | أرقام (`0-9`) | `1234`، `2024` |
| `-c !` | أحرف الترقيم الخاصة | `!@#$` |
| `-c aA1` | الحروف الأبجدية الرقمية المدمجة | `Admin123` |
| `-p <prefix>` | سلسلة بذرة البداية الأولية | من المفيد استئناف القوة الغاشمة |

تحديد حدود الطول:
```bash
# Minimum length 6, Maximum length 8
-l 6-8

# Exact length 8
-l 8-8
```
---

## تحسين الأداء والأداء

اختبار إنتاجية وحدة المعالجة المركزية على جهازك:
```bash
fcrackzip -B
```
---

## ZipCrypto مقابل AES-256 (بديل John/Hashcat)

إذا قام `fcrackzip` بإرجاع `not a zipfile, or unsupported encryption type`، فسيتم تشفير الأرشيف باستخدام AES-256 (WinZip AES). استخدم `zip2john`:
```bash
# 1. Extract hash using zip2john
zip2john aes_protected.zip > zip.hash

# 2. Crack using John the Ripper
john --wordlist=/usr/share/wordlists/rockyou.txt zip.hash

# 3. Or crack using Hashcat (Mode 13600: WinZip)
cat zip.hash | cut -d ':' -f 2 > hashcat_zip.hash
hashcat -m 13600 hashcat_zip.hash /usr/share/wordlists/rockyou.txt
```