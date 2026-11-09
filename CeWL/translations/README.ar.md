<!-- language-home-navigation -->
[🏠 الصفحة الرئيسية العربية](../../README.ar.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🕷️ CeWL - ورقة الغش الكاملة

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
   ██████╗███████╗██╗    ██╗██╗     
  ██╔════╝██╔════╝██║    ██║██║     
  ██║     █████╗  ██║ █╗ ██║██║     
  ██║     ██╔══╝  ██║███╗██║██║     
  ╚██████╗███████╗╚███╔███╔╝███████╗
   ╚═════╝╚══════╝ ╚══╝╚══╝ ╚══════╝
       Custom Word List Generator by Robin Wood (DigiNinja)
```
<p align="center">
  <img src="https://img.shields.io/badge/CeWL-Custom_Wordlist_Generator-blue?style=for-the-badge" alt="CeWL">
  <img src="https://img.shields.io/badge/Author-DigiNinja-orange?style=for-the-badge" alt="DigiNinja">
  <img src="https://img.shields.io/badge/Category-Password_Cracking-green?style=for-the-badge" alt="تكسير كلمة المرور">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="License GPLv2">
</ص>

<p align="center">
  <b>🌐 منشئ قاموس كلمات المرور الخاص بالهدف والذي يتصفح مواقع الويب لجمع مصطلحات الشركة المتخصصة وأسماء العلامات التجارية ورسائل البريد الإلكتروني للموظفين</b>
</ص>

---

## 📋 جدول المحتويات

- [نظرة عامة وفلسفة كلمة المرور المستهدفة](#-نظرة عامة--فلسفة-كلمة المرور المستهدفة)
- [التثبيت](#-التثبيت)
- [الزحف الأساسي إلى موقع الويب](#-الزحف الأساسي إلى موقع الويب)
- [عناصر التحكم في عمق العنكبوت وطول الكلمة](#-عمق العنكبوت--عناصر التحكم في طول الكلمة)
- [حصاد عناوين البريد الإلكتروني (`-e`)](#-harvesting-email-addresses--e)
- [بما في ذلك الأرقام والأحرف الصغيرة](#-بما في ذلك-الأرقام--أحرف صغيرة)
- [استخراج بيانات تعريف المستند (`-a`)](#-استخراج بيانات تعريف المستند--أ)
- [عدد الكلمات وتوصيف التكرار (`-c`)](#-عدد الكلمات--توصيف التكرار--c)
- [تسلسل CeWL مع Hashcat وقواعد John](#-chaining-cewl-with-hashcat--john-rules)
- [الجدول المرجعي السريع](#-الجدول المرجعي السريع)
- [الاعتبارات الدفاعية وOPSEC](#-الاعتبارات الدفاعية--opsec)
- [إخلاء المسؤولية القانونية](#-إخلاء المسؤولية القانونية)

---

## 🎯 نظرة عامة وفلسفة كلمة المرور المستهدفة

تعد قوائم الكلمات العامة مثل `rockyou.txt` فعالة بالنسبة لكلمات مرور المستهلك، ولكنها ** كثيرًا ما تفشل في مواجهة سياسات كلمات المرور الخاصة بالشركة ** والتي تتطلب أكثر من 12 حرفًا ورموز خاصة ودورات موسمية.

يقوم الموظفون بشكل روتيني ببناء كلمات مرور مؤسستهم على:
- أسماء مشاريع الشركة والأسماء الرمزية الداخلية والشعارات (`Pegasus2026!`).
- مصطلحات صناعية محددة، أو مصطلحات فنية، أو العلامات التجارية للمنتج.
- الأسماء التنفيذية ومعالم الموقع وعناوين المكاتب.

تم إنشاؤه بواسطة **Robin Wood (DigiNinja)**، **CeWL** (مولد قائمة الكلمات المخصصة) هو عنكبوت روبي يزحف إلى عنوان URL مستهدف محدد إلى عمق محدد، ويستخرج كلمات فريدة تتوافق مع الحد الأدنى من متطلبات الطول، ويبني قاموس كلمات مرور مخصص للمشاركة.

---

## 📥 التثبيت
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y cewl

# Arch Linux (AUR)
yay -S cewl

# Ruby Gem
gem install cewl
```
---

## 🚀 الزحف الأساسي إلى موقع الويب
```bash
# Crawl website and output words to console
cewl https://target.local

# Crawl and save to a custom wordlist file
cewl https://target.local -w company_wordlist.txt
```
---

## 🕸️ التحكم في عمق العنكبوت وطول الكلمة

التحكم في مدى عمق تتبع CeWL للروابط وأحجام الكلمات التي تلتقطها:
```bash
# Crawl 2 links deep (-d 2) and enforce minimum word length of 8 characters (-m 8)
cewl https://target.local -d 2 -m 8 -w wordlist_min8.txt

# Aggressive 3-level deep crawl for comprehensive corporate terminology
cewl https://target.local -d 3 -m 7 --no-words -w deep_crawl.txt
```
---

## 📧 حصاد عناوين البريد الإلكتروني (`-e`)

استخرج عناوين البريد الإلكتروني الخاصة بالشركة من أجل عمليات محاكاة التصيد الاحتيالي اللاحقة أو رش بيانات الاعتماد:
```bash
# Extract emails to a separate dedicated file
cewl https://target.local -e --email_file emails.txt -w words.txt
```
---

## 🔡 شامل الأرقام والأحرف الصغيرة
```bash
# Include words containing alphanumeric combinations (e.g., product names like V300, X86)
cewl https://target.local --with-numbers -m 6 -w words_with_numbers.txt

# Convert all discovered words to lowercase (great for rule generation)
cewl https://target.local --lowercase -w lowercase_words.txt
```
---

## 📄 استخراج البيانات التعريفية للوثيقة (`-a`)

قم باستخراج أسماء المستخدمين للمؤلف والمنشئ من مستندات PDF وMicrosoft Office التي تم تنزيلها:
```bash
# Download and analyze document metadata to extract employee usernames
cewl https://target.local -a --meta_file extracted_authors.txt
```
---

## 📊 عدد الكلمات وتوصيف التكرار (`-c`)

قم بفرز الكلمات حسب تكرارها لتحديد الكلمات الطنانة الأكثر انتشارًا في الشركات:
```bash
# Append occurrence count next to each word
cewl https://target.local -c -m 6 -w frequency.txt

# Sort by most frequent words in descending order
sort -k2 -nr frequency.txt | head -n 30
```
---

## ⚡ تسلسل CeWL مع Hashcat & John Rules

تحتوي قائمة كلمات CeWL الأولية على جذور القاموس الأساسية. لإنتاج كلمات مرور قابلة للتطبيق، قم بتوسيع قائمة الكلمات باستخدام **قواعد طفرة Hashcat / John the Ripper**:
```bash
# 1. Harvest base words from corporate website
cewl https://target.local -m 5 --lowercase -w base_words.txt

# 2. Mutate words using Hashcat's best64 rule set
hashcat --force base_words.txt -r /usr/share/hashcat/rules/best64.rule --stdout | sort -u > mutated_passwords.txt

# Example mutations produced:
# Acmetool -> Acmetool2026!, !Acmetool123, 4cmet00l, Acmetool#1
```
---

## ⚡ جدول مرجعي سريع

| الهدف | الأمر |
|-----------|--------|
| ** الزحف الأساسي إلى الملف ** | `cewl <URL> -w wordlist.txt` |
| **ضبط الحد الأدنى للطول والعمق** | `cewl <URL> -m 8 -d 2 -w wordlist.txt` |
| **استخراج رسائل البريد الإلكتروني الخاصة بالشركة**| `cewl <URL> -e --email_file emails.txt` |
| **استخراج البيانات التعريفية للمستند**| `cewl <URL> -a --meta_file authors.txt` |
| ** تضمين الكلمات الأبجدية الرقمية **| `cewl <URL> --with-numbers -w wordlist.txt` |
| **الإخراج بأحرف صغيرة** | `cewl <URL> --lowercase -w wordlist.txt` |
| **عد ترددات الكلمات** | `cewl <URL> -c -w frequencies.txt` |
| **المصادقة خلف تسجيل الدخول**| `cewl <URL> -a "Cookie: session=xyz"` |

---

## 🛡️ الاعتبارات الدفاعية وOPSEC

1. **سجلات الوصول إلى خادم الويب:** تنشئ CeWL المئات من طلبات HTTP GET السريعة، مما يؤدي إلى إنتاج سجلات وصول عنكبوتية يمكن تحديدها. استخدم سلاسل وكيل المستخدم المخصصة (`-u "Mozilla/5.0..."`) للدمج.
2. **مصائد الكلمات العسلية:** يمكن للفرق الزرقاء الدفاعية تضمين روابط HTML غير مرئية أو مخفية تؤدي إلى بيانات اعتماد الكناري للإبلاغ عن العناكب الآلية.

---

## ⚠️إخلاء المسؤولية القانونية

> [!تنبيه]
> تم تصميم CeWL للتقييمات الأمنية المعتمدة واختبار الاختراق وأبحاث تدقيق كلمات المرور فقط. يمنع منعا باتا تجريف أو تلاعب الأنظمة غير المصرح بها.