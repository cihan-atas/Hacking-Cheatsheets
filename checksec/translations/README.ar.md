<!-- language-home-navigation -->
[🏠 الصفحة الرئيسية العربية](../../README.ar.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🛡️ checksec & Binutils - ورقة الغش الكاملة

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
   ██████╗██╗  ██╗███████╗ ██████╗██╗  ██╗███████╗███████╗ ██████╗
  ██╔════╝██║  ██║██╔════╝██╔════╝██║ ██╔╝██╔════╝██╔════╝██╔════╝
  ██║     ███████║█████╗  ██║     █████╔╝ ███████╗█████╗  ██║     
  ██║     ██╔══██║██╔══╝  ██║     ██╔═██╗ ╚════██║██╔══╝  ██║     
  ╚██████╗██║  ██║███████╗╚██████╗██║  ██╗███████║███████╗╚██████╗
   ╚═════╝╚═╝  ╚═╝╚══════╝ ╚═════╝╚═╝  ╚═╝╚══════╝╚══════╝ ╚═════╝
          Linux ELF Binary Mitigation Auditor & Inspection Toolkit
```
<p align="center">
  <img src="https://img.shields.io/badge/checksec-Security_Mitigations-blue?style=for-the-badge" alt="checksec">
  <img src="https://img.shields.io/badge/readelf-ELF_Headers-orange?style=for-the-badge" alt="readelf">
  <img src="https://img.shields.io/badge/objdump-Disassembly-green?style=for-the-badge" alt="objdump">
  <img src="https://img.shields.io/badge/Platform-Linux_ELF-purple?style=for-the-badge" alt="Linux ELF">
</ص>

<p align="center">
  <b>🛡️ أدوات سطر الأوامر الأساسية لتدقيق عمليات تخفيف الأمان القابلة للتنفيذ في Linux (RELRO وStack Canary وNX وPIE وFortify) وتشريح الرؤوس والأقسام وجداول الرموز الثنائية لـ ELF</b>
</ص>

---

## 📋 جدول المحتويات

- [نظرة عامة ومصفوفة التخفيف الثنائية](#-نظرة عامة--مصفوفة التخفيف الثنائية)
- [التثبيت](#-التثبيت)
- [checksec: تدقيق التخفيف السريع](#-checksec-rapid-mitigation-auditing)
  - [تدقيق الثنائيات الفردية والدفعية](#auditing-single-batch-binaries)
  - [تدقيق العمليات الجارية](#auditing-running-processes)
  - [إخراج JSON وCSV](#json--csv-output)
- [readelf: رأس ELF وتشريح القسم](#-readelf-elf-header--section-dissection)
  - [رأس ملف ELF (`-h`)](#elf-file-header--h)
  - [رؤوس البرامج ومقاطعها (`-l`)](#program-headers--segments--l)
  - [رؤوس الأقسام (`-S`)](#section-headers--s)
  - [جداول الرموز (`-s` / `--syms`)](#symbol-tables--s----syms)
  - [التبعيات الديناميكية والمكتبات (`-d`)](#التبعيات الديناميكية--المكتبات--d)
- [objdump: التفكيك والنقل](#-objdump-disassembly--relocations)
  - [تفكيك إنتل (`-d -M intel`)](#intel-disassembly--d-m-intel)
  - [إدخالات النقل وGOT (`-R`)](#relocation-entries--got--r)
- [nm & strings: دقة الرمز والسلاسل](#-nm--strings-symbol-resolution--strings)
  - [إدراج الرموز المفككة باستخدام `nm`](#listing-demangled-symbols-with-nm)
  - [استخراج السلاسل المبهمة باستخدام `strings`](#extracting-obfuscated-strings-with-strings)
- [مرجع التجميع المقوى](#-مرجع التجميع المقوى)
- [الجدول المرجعي السريع](#-الجدول المرجعي السريع)
- [إخلاء المسؤولية القانونية](#-إخلاء المسؤولية القانونية)

---

## 🎯 نظرة عامة ومصفوفة التخفيف الثنائية

قبل البدء في أي سير عمل للهندسة العكسية أو الاستغلال، يعد فهم وسائل تخفيف الأمان التي تم تمكينها أمرًا إلزاميًا:

| التخفيف | الاسم الكامل | آلية الحماية | استراتيجية التجاوز |
|------------|----------|---------------------|-----------------|
| **ريلرو** | نقل للقراءة فقط | يجعل `.got` للقراءة فقط (`Full RELRO`) لمنع الكتابة فوق المؤشر | هياكل الكومة المستهدفة أو عناوين الإرجاع أو خطافات الوظائف |
| **كناري** | حامي المكدس | يضع ملف تعريف الارتباط العشوائي قبل مؤشر الإرجاع؛ يتحقق من النزاهة عند العودة | تسرب قيمة الكناري عبر تسرب الذاكرة، أو القوة الغاشمة (تفرع الشياطين) |
| **إن إكس** | عدم التنفيذ / DEP | يضع علامات على أجزاء المكدس والكومة والبيانات على أنها غير قابلة للتنفيذ | البرمجة الموجهة نحو العودة (ROP) أو البرمجة الموجهة نحو القفز (JOP) |
| **فطيرة** | موقف مستقل قابل للتنفيذ | يقوم بتحميل العنوان الأساسي الثنائي في موقع عشوائي في الذاكرة الظاهرية (ASLR) | مؤشر رمز التسرب لحساب إزاحة العنوان الأساسي |
| **تحصن** | `_FORTIFY_SOURCE` | يستبدل وظائف السلسلة غير الآمنة (`strcpy` -> `__strcpy_chk`) بفحص الحدود | أخطاء منطقية غير تجاوز السعة، سلسلة تنسيق دون تجاوز سعة المخزن المؤقت |

---

## 📥 التثبيت
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y checksec binutils

# Arch Linux
sudo pacman -S checksec binutils

# Python pip (standalone checksec)
pip install checksec.py
```
---

## ⚡ checksec: تدقيق التخفيف السريع

### تدقيق الثنائيات الفردية والدفعية
```bash
# Audit a single executable
checksec --file=./target_binary

# Audit all binaries in directory
checksec --dir=/usr/bin/
```
*مثال الإخراج القياسي:*
```text
RELRO           STACK CANARY      NX            PIE             RPATH      RUNPATH      FILE
Full RELRO      Canary found      NX enabled    PIE enabled     No RPATH   No RUNPATH   ./target_binary
```
### تدقيق العمليات الجارية
تدقيق عمليات تخفيف الذاكرة وحالة ASLR للبرنامج الخفي النشط:
```bash
# Audit by Process ID (PID)
checksec --proc=1337

# Audit process by name
checksec --proc-all
```
### إخراج JSON وCSV
تصدير النتائج للتحليل الآلي:
```bash
checksec --file=./target_binary --output=json > security.json
checksec --file=./target_binary --output=csv > security.csv
```
---

## 🔬 readelf: رأس ELF وتشريح القسم

تعرض الأداة المساعدة `readelf` معلومات من ملفات ELF دون استدعاء أي تنفيذ:

### رأس ملف ELF (`-h`)
يكشف عن بنية وحدة المعالجة المركزية، وفئة 32 مقابل 64 بت، وعنوان نقطة الإدخال، والنهاية:
```bash
readelf -h ./target_binary
```
### رؤوس وقطاعات البرنامج (`-l`)
يعرض المقاطع المحملة في الذاكرة في وقت التشغيل والأعلام (`R`، `W`، `E`). ضروري لتحديد ما إذا كان المكدس (`GNU_STACK`) لديه أذونات التنفيذ:
```bash
readelf -l ./target_binary
```
*(إذا كان `GNU_STACK` يحتوي على `RWE`، فسيتم السماح بتنفيذ المكدس؛ ويتم تعطيل NX).*

### رؤوس الأقسام (`-S`)
افحص تخطيطات الذاكرة والعناوين والإزاحات لـ `.text` (الكود)، و`.data` (البيانات المهيأة)، و`.rodata` (الثوابت)، و`.bss` (البيانات غير المهيأة)، و`.plt`، و`.got`:
```bash
readelf -S ./target_binary
```
### جداول الرموز (`-s` / `--syms`)
قم بإدراج كافة الوظائف المصدرة والمتغيرات العامة ورموز libc المستوردة:
```bash
# List all symbols
readelf -s ./target_binary

# Filter dynamic symbols used at runtime
readelf --dyn-syms ./target_binary
```
### التبعيات الديناميكية والمكتبات (`-d`)
يسرد المكتبات المشتركة المطلوبة (علامات `NEEDED`، على سبيل المثال `libc.so.6`، `libssl.so`):
```bash
readelf -d ./target_binary
```
---

## 🧩 objdump: التفكيك والنقل

### تفكيك إنتل (`-d -M intel`)
قم بتفكيك الأقسام القابلة للتنفيذ إلى مجموعة بناء جملة Intel نظيفة:
```bash
# Disassemble .text section in Intel syntax
objdump -d -M intel ./target_binary

# Disassemble specific function (e.g. main)
objdump -d -M intel ./target_binary | grep -A 30 "<main>:"
```
### إدخالات النقل وGOT (`-R`)
عرض عناوين نقل جدول الأوفست العالمي (GOT) لاستدعاءات المكتبة المستوردة:
```bash
objdump -R ./target_binary
```
*يظهر أهداف العناوين لوظائف مثل `puts`، و`printf`، و`system`.*

---

## 🔍 nm & strings: دقة الرمز والسلاسل

### قائمة الرموز المفككة باستخدام `nm`
```bash
# List defined symbols only (ignore external imports)
nm --defined-only -C ./target_binary

# List undefined external symbols (functions resolved by dynamic linker)
nm -u -C ./target_binary
```
### استخراج السلاسل المبهمة باستخدام `strings`
```bash
# Extract ASCII strings with minimum 8 characters
strings -a -n 8 ./target_binary

# Extract 16-bit Little Endian wide strings (UTF-16 e.g., Windows / Java)
strings -e l -a ./target_binary

# Print byte offset of each string in hex
strings -a -t x ./target_binary
```
---

## 🛡️ مرجع التجميع المتشدد

لإنشاء ثنائي ELF مع الحد الأقصى من التخفيفات الأمنية:
```bash
gcc -O2 \
  -Wall \
  -fstack-protector-strong \
  -D_FORTIFY_SOURCE=2 \
  -Wl,-z,relro,-z,now \
  -fPIE -pie \
  -Wl,-z,noexecstack \
  source.c -o secure_binary
```
---

## ⚡ جدول مرجعي سريع

| الهدف | الأمر |
|-----------|--------|
| ** عمليات تخفيف آثار التدقيق ** | `checksec --file=<bin>` |
| ** تدقيق تشغيل PID ** | `checksec --proc=<pid>` |
| **عرض رأس ELF** | `readelf -h <bin>` |
| ** عرض علم Stack NX ** | `readelf -l <bin> \| grep -A 1 GNU_STACK` |
| ** قائمة الأقسام (.got, .plt)**| `readelf -S <bin>` |
| **عرض الرموز الديناميكية** | `readelf --dyn-syms <bin>` |
| ** إنتل التفكيك ** | `objdump -d -M intel <bin>` |
| ** فحص عمليات النقل التي تم الحصول عليها ** | `objdump -R <bin>` |
| **قائمة الرموز (المفككة)** | `nm -C <bin>` |
| **استخرج سلاسل UTF-16** | `strings -e l -a <bin>` |
| **إظهار إزاحات السلسلة** | `strings -a -t x <bin>` |

---

## ⚠️إخلاء المسؤولية القانونية

> [!تنبيه]
> checksec وGNU Binutils هما أدوات تشخيصية وهندسة عكسية مخصصة لتدقيق أمان البرامج والتحقق من الدفاع والأبحاث الثنائية المعتمدة. يجب أن يلتزم فحص البرامج غير المصرح بها بلوائح الملكية الفكرية المحلية.