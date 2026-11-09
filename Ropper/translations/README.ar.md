<!-- language-home-navigation -->
[🏠 الصفحة الرئيسية العربية](../../README.ar.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🧩 Ropper & ROPgadget - ورقة الغش الكاملة

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ██████╗  ██████╗ ██████╗ ██████╗ ███████╗██████╗ 
  ██╔══██╗██╔═══██╗██╔══██╗██╔══██╗██╔════╝██╔══██╗
  ██████╔╝██║   ██║██████╔╝██████╔╝█████╗  ██████╔╝
  ██╔══██╗██║   ██║██╔═══╝ ██╔═══╝ ██╔══╝  ██╔══██╗
  ██║  ██║╚██████╔╝██║     ██║     ███████╗██║  ██║
  ╚═╝  ╚═╝ ╚═════╝ ╚═╝     ╚═╝     ╚══════╝╚═╝  ╚═╝
     Advanced ROP & JOP Gadget Finder and Chain Generator
```
<p align="center">
  <img src="https://img.shields.io/badge/Ropper-Gadget_Finder-blue?style=for-the-badge" alt="Ropper">
  <img src="https://img.shields.io/badge/ROPgadget-Exploit_Tool-orange?style=for-the-badge" alt="ROPgadget">
  <img src="https://img.shields.io/badge/Bypass-NX_%7C_DEP-red?style=for-the-badge" alt="NX Bypass">
  <img src="https://img.shields.io/badge/Category-Exploit_Development-green?style=for-the-badge" alt="استغلال Dev">
</ص>

<p align="center">
  <b>🧩 الأدوات المساعدة الأساسية لسطر الأوامر لاكتشاف أدوات البرمجة الموجهة للعودة (ROP) والبرمجة الموجهة للقفز (JOP) لتجاوز حماية الذاكرة غير القابلة للتنفيذ (NX / DEP)</b>
</ص>

---

## 📋 جدول المحتويات

- [نظرة عامة ونظرية ROP](#-نظرة عامة--نظرية ROP)
- [التثبيت](#-التثبيت)
- [اكتشاف الأداة الأساسية](#-اكتشاف الأداة الأساسية)
  - [استخراج كافة الأدوات](#extracting-all-gadgets)
  - [الحد من عمق التعليمات](#الحد من عمق التعليمات)
- [بحث متقدم وتصفية الأنماط](#-بحث متقدم--تصفية الأنماط)
  - [استهداف السجلات المحددة (`rdi`، `rsi`، `rdx`، `rax`)](#targeting-special-registers-rdi-rsi-rdx-rax)
  - [أدوات الكتابة في الذاكرة (اكتب-ماذا-أين)](#أدوات-الكتابة-الذاكرة-اكتب-ماذا-أين)
  - [Syscall & Interrupt Gadgets](#syscall--interrupt-gadgets)
- [استبعاد الأحرف السيئة (`--badbytes`)](#-استبعاد-الأحرف السيئة---بادبايت)
- [توليد سلسلة ROP الآلي](#-توليد سلسلة ROP الآلي)
  - [سلاسل روبر الآلية](#ropper-automated-chains)
  - [سلاسل ROPgadget الآلية](#ropgadget-automated-chains)
- [تحليل المكتبات المحملة (سلاسل libc ROP)](#-تحليل-المكتبات-المحملة-libc-rop-chains)
- [الجدول المرجعي السريع](#-الجدول المرجعي السريع)
- [الاعتبارات الدفاعية وعمليات التخفيف](#-الاعتبارات الدفاعية--التخفيفات)
- [إخلاء المسؤولية القانونية](#-إخلاء المسؤولية القانونية)

---

## 🎯 نظرة عامة ونظرية شرطة عمان السلطانية

تفرض أنظمة التشغيل الحديثة **NX (عدم التنفيذ) / DEP (منع تنفيذ البيانات)**، مما يمنع التنفيذ المباشر لكود القشرة الموضوع على المكدس أو الكومة.

تتحايل **البرمجة الموجهة نحو العودة (ROP)** على هذا التقييد عن طريق إعادة استخدام تعليمات الآلة الصغيرة الموجودة بالفعل في الذاكرة القابلة للتنفيذ (قسم `.text` من المكتبات الثنائية أو المشتركة مثل `libc.so`). يُطلق على كل تسلسل تعليمات ينتهي بتعليمات `ret` اسم **gadget**. من خلال ترتيب العناوين على المكدس، يقوم المهاجمون بربط الأدوات معًا من أجل:
1. قم بتحميل الوسائط في سجلات وحدة المعالجة المركزية (على سبيل المثال، `pop rdi; ret` لاتفاقيات الاتصال x64).
2. اكتب سلاسل (مثل `/bin/sh`) في أقسام `.bss` أو `.data` قابلة للكتابة.
3. قم بتشغيل `execve("/bin/sh", NULL, NULL)` أو استدعاء `system()`.

---

## 📥 التثبيت
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y ropper python3-ropgadget

# Python pip (Latest Releases)
pip install ropper capstone
pip install ROPgadget

# Arch Linux
sudo pacman -S ropper python-ropgadget
```
---

## 🚀 اكتشاف الأدوات الأساسية

### استخراج كافة الأدوات
```bash
# Using Ropper (Faster multi-threaded engine)
ropper --file ./target_binary --nocolor

# Using ROPgadget
ROPgadget --binary ./target_binary
```
### الحد من عمق التعليمات
قلل من السلاسل الطويلة المزعجة عن طريق تقييد طول الأداة (عدد التعليمات قبل `ret`):
```bash
# Limit to maximum 3 instructions before return
ropper --file ./target_binary --inst-count 3
ROPgadget --binary ./target_binary --depth 3
```
---

## 🔍 البحث المتقدم وتصفية الأنماط

### استهداف سجلات محددة (`rdi`، `rsi`، `rdx`، `rax`)
في اتفاقية الاتصال بنظام Linux 64 بت، يجب أن تكون الوسائط الثلاثة الأولى لوظيفة أو syscall موجودة في `rdi` و`rsi` و`rdx`:
```bash
# Find pop rdi gadgets (1st argument)
ropper --file ./target_binary --search "pop rdi"

# Find pop rsi gadgets (2nd argument)
ropper --file ./target_binary --search "pop rsi"

# Find pop rdx gadgets (3rd argument)
ropper --file ./target_binary --search "pop rdx"

# Find gadgets that control RAX (Syscall number: 59 for execve)
ropper --file ./target_binary --search "pop rax"
```
باستخدام مطابقة ROPgadget regex:
```bash
ROPgadget --binary ./target_binary --only "pop|ret" | grep -E "rdi|rsi|rdx|rax"
```
### أدوات كتابة الذاكرة (الكتابة-ماذا-أين)
عندما تحتاج إلى تخزين `/bin/sh` أو وسيطات عشوائية في الذاكرة:
```bash
# Find instructions moving registers into pointer memory
ropper --file ./target_binary --search "mov [???], ???"
```
*مثال للمطابقة:* يسمح `mov qword ptr [rdi], rsi; ret` بكتابة كل ما هو موجود في `rsi` إلى العنوان المشار إليه بواسطة `rdi`.

### أدوات النظام والمقاطعة
```bash
# Find raw syscall instructions
ropper --file ./target_binary --search "syscall"

# x86 32-bit software interrupt
ropper --file ./target_binary --search "int 0x80"
```
---

## 🚫 استبعاد الشخصيات السيئة (`--badbytes`)

تنتهي الثغرات الأمنية أو وظائف تنسيق السلسلة مثل `strcpy()` عند وحدات البايت الخالية (`\x00`) أو الأسطر الجديدة (`\x0a`) أو إرجاع السطر (`\x0d`). استبعاد العناوين التي تحتوي على وحدات البايت هذه:
```bash
# Filter out gadgets whose addresses contain bad characters
ropper --file ./target_binary --badbytes 000a0d --search "pop rdi"
ROPgadget --binary ./target_binary --badbytes "00|0a|0d"
```
---

## ⚡ إنشاء سلسلة ROP آليًا

يمكن لكلتا الأداتين إنشاء قوالب استغلال Python الوظيفية التي تستدعي `execve("/bin/sh")` تلقائيًا:

### سلاسل روبر الآلية
```bash
ropper --file ./target_binary --chain "execve"
```
### سلاسل ROPgadget الآلية
```bash
ROPgadget --binary ./target_binary --ropchain
```
*ينتج عن ذلك تسلسل هيكلي لحمولة Python جاهزة للاستخدام `mprotect()` أو `execve()`.*

---

## 📚 تحليل المكتبات المحملة (سلاسل libc ROP)

عندما يتم تجميع الملف الثنائي نفسه كـ **PIE** أو يحتوي على عدد قليل من الأدوات المفيدة، فاستخرج الأدوات الذكية مباشرةً من مكتبة C المشتركة للنظام المستهدف (`libc.so.6`):
```bash
# Search gadgets in libc
ropper --file /lib/x86_64-linux-gnu/libc.so.6 --search "pop rdi; ret"

# Find "/bin/sh" string offset inside libc
strings -a -t x /lib/x86_64-linux-gnu/libc.so.6 | grep "/bin/sh"
```
*(أضف الإزاحات المكتشفة إلى العنوان الأساسي المسرب لـ libc في وقت التشغيل).*

---

## ⚡ جدول مرجعي سريع

| الهدف | أمر روبر | أمر ROPgadget |
|-----------|----------------|-------------------|
| **تفريغ كافة الأدوات** | `ropper --file <bin>` | `ROPgadget --binary <bin>` |
| ** البحث عن أداة محددة ** | `ropper --file <bin> --search "pop rdi"` | `ROPgadget --binary <bin> \| grep "pop rdi"` |
| **تصفية البايتات السيئة** | `ropper --file <bin> --badbytes 000a` | `ROPgadget --binary <bin> --badbytes "00\|0a"` |
| **حد العمق/الطول** | `ropper --file <bin> --inst-count 3` | `ROPgadget --binary <bin> --depth 3` |
| ** البحث في أدوات JOP ** | `ropper --file <bin> --type jop` | `ROPgadget --binary <bin> --norop` |
| ** بناء سلسلة ROP تلقائيًا ** | `ropper --file <bin> --chain "execve"`| `ROPgadget --binary <bin> --ropchain` |
| ** البحث في ذاكرة الكتابة ** | `ropper --file <bin> --search "mov [???], ???"` | `ROPgadget --binary <bin> \| grep "mov \["` |

---

## 🛡️ الاعتبارات الدفاعية والتخفيفات

1. **تقنية التحكم في التدفق (Intel CET / Shadow Stack):** تحتفظ المعالجات الحديثة بمكدس ظل معزول للأجهزة يتحقق من عناوين الإرجاع على `ret`، وينهي سلاسل ROP على الفور.
2. ** Clang / تكامل تدفق التحكم في مجلس التعاون الخليجي (CFI): ** التحقق من صحة أهداف مؤشر الوظيفة مقابل رسم بياني للحافة الأمامية قبل المكالمات غير المباشرة.

---

## ⚠️إخلاء المسؤولية القانونية

> [!تنبيه]
> Ropper وROPgadget هما أدوات مساعدة لتطوير البرمجيات وتقييم أمانها. ويجب استخدامها بشكل صارم في اختبارات الاختراق المصرح بها، أو تحديات CTF، أو الأبحاث الأمنية الدفاعية. يعد استغلال البرامج دون إذن صريح أمرًا غير قانوني.