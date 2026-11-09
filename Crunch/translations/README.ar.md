<!-- language-home-navigation -->
[🏠 الصفحة الرئيسية العربية](../../README.ar.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🔢 أزمة - ورقة غش كاملة

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
   ██████╗██████╗ ██╗   ██╗███╗   ██╗ ██████╗██╗  ██╗
  ██╔════╝██╔══██╗██║   ██║████╗  ██║██╔════╝██║  ██║
  ██║     ██████╔╝██║   ██║██╔██╗ ██║██║     ███████║
  ██║     ██╔══██╗██║   ██║██║╚██╗██║██║     ██╔══██║
  ╚██████╗██║  ██║╚██████╔╝██║ ╚████║╚██████╗██║  ██║
   ╚═════╝╚═╝  ╚═╝ ╚═════╝ ╚═╝  ╚═══╝ ╚═════╝╚═╝  ╚═╝
       High-Speed Pattern-Based Wordlist & Character Matrix Generator
```
<p align="center">
  <img src="https://img.shields.io/badge/Crunch-Wordlist_Generator-blue?style=for-the-badge" alt="Crunch">
  <img src="https://img.shields.io/badge/Language-C-orange?style=for-the-badge" alt="اللغة C">
  <img src="https://img.shields.io/badge/Category-Password_Cracking-green?style=for-the-badge" alt="تكسير كلمة المرور">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="License GPLv2">
</ص>

<p align="center">
  <b>🌐 مُنشئ قائمة الكلمات الذي يُنشئ جميع المجموعات الممكنة من مجموعات الأحرف باستخدام الأطوال والأنماط والرموز المخصصة</b>
</ص>

---

## 📋 جدول المحتويات

- [نظرة عامة ومحرك التقليب](#-نظرة عامة--محرك-التقليب)
- [التثبيت](#-التثبيت)
- [بناء الجملة الأساسي ومجموعات الأحرف](#-بناء الجملة الأساسي--مجموعات الأحرف)
- [مطابقة الأنماط والعناصر النائبة (`-t`)](#-مطابقة الأنماط--العناصر النائبة--t)
- [استخدام مجموعات محارف النظام (`charset.lst`)](#-using-system-charsets-charsetlst)
- [تحديد الأحرف المكررة (`-d`)](#-تحديد الأحرف المكررة--d)
- [تقسيم قوائم الكلمات إلى أجزاء (`-b` & `-c`)](#-تقسيم-قوائم الكلمات-إلى-قطع--b---c)
- [توصيل الأنابيب مباشرة إلى أدوات الهجوم (مساحة القرص صفر)](#-piping-directly-into-attack-tools-zero-disk-space)
- [الجدول المرجعي السريع](#-الجدول المرجعي السريع)
- [الاعتبارات الدفاعية وتحذيرات الحساب](#-اعتبارات-دفاعية--تحذيرات-الحساب)
- [إخلاء المسؤولية القانونية](#-إخلاء المسؤولية القانونية)

---

## 🎯 نظرة عامة ومحرك التقليب

عند مهاجمة أنماط كلمات المرور المعروفة (على سبيل المثال، أرقام التعريف الشخصية للشركات أو أرقام الهواتف أو كلمات المرور المطابقة لقواعد التعقيد القياسية مثل `Word2026!`)، فإن القواميس الثابتة القياسية مثل `rockyou.txt` إما تفتقد الهدف أو تكون غير فعالة.

تم كتابته بلغة C بواسطة **bofheaded**، **Crunch** وهو منشئ قائمة كلمات سريع للغاية. فهو يحسب التباديل رياضيًا، مما يسمح لك بتحديد مواضع الأحرف الدقيقة، والأطوال، ومجموعات الأحرف، وقيود التكرار.

---

## 📥 التثبيت
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y crunch

# Arch Linux (AUR)
yay -S crunch
```
---

## 🚀 بناء الجملة الأساسي ومجموعات الأحرف

يتطلب بناء الجملة الأساسي الحد الأدنى للطول والحد الأقصى للطول والأبجدية اختياريًا:
```bash
# crunch <min> <max> [characterset] [options]

# Generate all 4-digit numeric PINs (0000 - 9999)
crunch 4 4 0123456789 -o 4digit_pins.txt

# Generate 5-character words using specific custom letters
crunch 5 5 abcdef -o custom_words.txt

# Generate lowercase alphabet combinations from length 3 to 4
crunch 3 4 abcdefghijklmnopqrstuvwxyz -o alpha_3_4.txt
```
---

## 🎭 مطابقة الأنماط والعناصر النائبة (`-t`)

تسمح علامة `-t` بإصلاح الأحرف المعروفة أثناء تشويش الفتحات غير المعروفة باستخدام عناصر نائبة خاصة:

| العنصر النائب | نوع الحرف |
|-------------|----------------|
| `@` | الحروف الصغيرة (`a-z`) |
| `,` | الحروف الكبيرة (`A-Z`) |
| `%` | أرقام (`0-9`) |
| `^` | الرموز الخاصة/علامات الترقيم (`!@#$%^&*...`) |

### أمثلة على الأنماط العملية:
```bash
# Generate Turkish phone numbers (e.g., 0532XXXXXXX)
crunch 11 11 -t 0532%%%%%%% -o phone_numbers.txt

# Generate passwords conforming to: Capital letter + 4 lowercase + 2 digits + 1 symbol
# e.g., Admin24! or Super99#
crunch 8 8 -t ,@@@@%%^ -o policy_passwords.txt

# Target known prefix: Spring2026! or Spring2026#
crunch 11 11 -t Spring2026^ -o spring_passwords.txt
```
---

## 📚 استخدام محارف النظام (`charset.lst`)

حزم مصفوفات الأحرف التي تم تكوينها مسبقًا في `/usr/share/crunch/charset.lst`:
```bash
# Use pre-defined alphanumeric set (mixalpha-numeric)
crunch 6 6 -f /usr/share/crunch/charset.lst mixalpha-numeric -o out.txt

# Use hexadecimal lowercase characters (0-9, a-f)
crunch 8 8 -f /usr/share/crunch/charset.lst hex-lower -o hex_keys.txt
```
---

## 🚫 تحديد عدد الأحرف المكررة (`-d`)

فرض حدود على الأحرف المكررة المتتالية للتخلص من كلمات المرور غير المحتملة:
```bash
# Generate 8-character lowercase words with at most 2 consecutive identical letters
# (e.g., prevents 'aaabbb' while allowing 'aab')
crunch 8 8 abcdefghijklmnopqrstuvwxyz -d 2@ -o deduped.txt

# Limit consecutive numbers to at most 1 (prevents '11' or '22')
crunch 6 6 -t %%%%%% -d 1% -o no_repeated_digits.txt
```
---

## 📦 تقسيم قوائم الكلمات إلى أجزاء (`-b` و`-c`)

منع الملفات متعددة التيرابايت من استنفاد مساحة القرص عن طريق تقسيمها إلى وحدات تخزين يمكن التحكم فيها:
```bash
# Split wordlist into 500MB chunks
crunch 8 8 -f /usr/share/crunch/charset.lst mixalpha-numeric -b 500mb -o START

# Split by line count (every 1,000,000 lines)
crunch 8 8 0123456789 -c 1000000 -o START
```
---

## ⚡ التوصيل مباشرة إلى أدوات الهجوم (صفر مساحة على القرص)

لا تكتب أبدًا قوائم كلمات ضخمة على القرص عندما يمكنك دفق التباديل مباشرة إلى أدوات الاختراق:
```bash
# Stream 8-digit numeric PINs straight into Aircrack-ng
crunch 8 8 0123456789 | aircrack-ng -w - -b 00:11:22:33:44:55 capture.cap

# Stream policy passwords directly into Hydra SSH brute-force
crunch 8 8 -t Admin%%^ | hydra -l admin -P - 10.10.10.50 ssh

# Stream into John the Ripper
crunch 6 6 0123456789 | john --stdin --format=raw-md5 hashes.txt
```
---

## ⚡ جدول مرجعي سريع

| الهدف | الأمر |
|-----------|--------|
| **أرقام التعريف الشخصية الرقمية (4 أرقام)** | `crunch 4 4 0123456789 -o pins.txt` |
| **النمط: رأس المال + الكلمة + الأرقام**| `crunch 8 8 -t ,@@@@%%^ -o passwords.txt` |
| **النمط: بادئة محددة** | `crunch 10 10 -t Company%%% -o wordlist.txt` |
| ** استخدم ملف مجموعة الأحرف ** | `crunch 6 6 -f charset.lst mixalpha-numeric -o out.txt` |
| **الحد المتطابق المتتالي**| `crunch 8 8 -t @@@@@@@@ -d 2@ -o out.txt` |
| ** مقسم إلى ملفات بحجم 1 جيجابايت ** | `crunch 8 8 <CHARS> -b 1gb -o START` |
| ** الأنابيب إلى Aircrack-ng ** | `crunch 8 8 0123456789 \| aircrack-ng -w - target.cap` |
| ** الأنابيب إلى هيدرا ** | `crunch 7 7 -t User%%% \| hydra -l user -P - <IP> ssh` |

---

## 🛡️ الاعتبارات الدفاعية والتحذيرات الحسابية

> [!تحذير]
> ** انفجارات التقليب: ** تحقق دائمًا من حجم الإخراج المتوقع لـ Crunch قبل التأكد من إنشاء القرص. تنتج قائمة الكلمات غير المقيدة المكونة من 8 أحرف أبجدية رقمية **أكثر من 200 تيرابايت** من البيانات النصية! إخراج الأنابيب كلما أمكن ذلك.

---

## ⚠️إخلاء المسؤولية القانونية

> [!تنبيه]
> تم تصميم Crunch لاستعادة كلمة المرور المصرح بها واختبار الاختراق والتدقيق الأمني فقط. يعد فرض كلمات المرور الغاشمة على الأنظمة غير المصرح بها أمرًا غير قانوني تمامًا.