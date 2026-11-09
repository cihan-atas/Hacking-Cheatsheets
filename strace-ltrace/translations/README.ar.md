<!-- language-home-navigation -->
[🏠 الصفحة الرئيسية العربية](../../README.ar.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🔬 strace & ltrace - ورقة الغش كاملة

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ███████╗████████╗██████╗  █████╗  ██████╗███████╗    ██╗  ████████╗██████╗  █████╗  ██████╗███████╗
  ██╔════╝╚══██╔══╝██╔══██╗██╔══██╗██╔════╝██╔════╝    ██║  ╚══██╔══╝██╔══██╗██╔══██╗██╔════╝██╔════╝
  ███████╗   ██║   ██████╔╝███████║██║     █████╗      ██║     ██║   ██████╔╝███████║██║     █████╗  
  ╚════██║   ██║   ██╔══██╗██╔══██║██║     ██╔══╝      ██║     ██║   ██╔══██╗██╔══██║██║     ██╔══╝  
  ███████║   ██║   ██║  ██║██║  ██║╚██████╗███████╗    ███████╗██║   ██║  ██║██║  ██║╚██████╗███████╗
  ╚══════╝   ╚═╝   ╚═╝  ╚═╝╚═╝  ╚═╝ ╚═════╝╚══════╝    ╚══════╝╚═╝   ╚═╝  ╚═╝╚═╝  ╚═╝ ╚═════╝╚══════╝
            Linux Dynamic Binary Analysis: System & Library Call Tracers
```
<p align="center">
  <img src="https://img.shields.io/badge/strace-System_Calls-blue?style=for-the-badge" alt="strace">
  <img src="https://img.shields.io/badge/ltrace-Library_Calls-orange?style=for-the-badge" alt="ltrace">
  <img src="https://img.shields.io/badge/Platform-Linux_ELF-green?style=for-the-badge" alt="Linux">
  <img src="https://img.shields.io/badge/Category-Dynamic_Analogy-purple?style=for-the-badge" alt="التحليل الديناميكي">
</ص>

<p align="center">
  <b>🔬 الأدوات المساعدة الأساسية لسطر الأوامر للتحليل الثنائي الديناميكي: اعتراض استدعاءات نظام Linux kernel (strace) واستدعاءات المكتبة الديناميكية لمساحة المستخدم (ltrace)</b>
</ص>

---

## 📋 جدول المحتويات

- [نظرة عامة: مكالمات النظام مقابل مكالمات المكتبة](#-نظرة عامة-مكالمات النظام مقابل مكالمات المكتبة)
- [التثبيت](#-التثبيت)
- [strace: تتبع مكالمات النظام](#-strace-system-call-tracing)
  - [تنفيذ التتبع الأساسي](#basic-tracing-execution)
  - [تصفية مكالمات النظام حسب الفئة أو الاسم (`-e`)](#filtering-syscalls-by-class-or-name--e)
  - [تتبع العمليات التابعة والعمليات متعددة الخيوط (`-f`)](#tracing-child--multi-threaded-processes--f)
  - [الإرفاق بالعمليات الجارية (`-p`)](#الإرفاق بالعمليات الجارية--p)
  - [توسيع السلاسل ومخرجات المخزن المؤقت (`-s`)](#expanding-strings--buffer-outputs--s)
  - [التنميط الإحصائي وتحليل الاختناقات (`-c`)](#التنميط الإحصائي--تحليل الاختناق--ج)
  - [الحقن الخاطئ للتشويش والاختبار (`-e fault`)](#حقن-الخطأ للتشويش--الاختبار--الخطأ الإلكتروني)
- [ltrace: تتبع مكالمات المكتبة الديناميكية](#-ltrace-dynamic-library-call-tracing)
  - [اعتراض المكتبة الأساسية](#basic-library-interception)
  - [تكسير عمليات التحقق من كلمة المرور المشفرة (`strcmp`)](#cracking-hardcoded-password-checks-strcmp)
  - [تفكيك رموز C++ (`-C`)](#demangling-c-symbols--c)
  - [تصفية وظائف المكتبة المحددة](#filtering-special-library-functions)
- [الجمع بين مهام سير عمل strace وltrace](#-combining-strace-and-ltrace-workflows)
- [الجدول المرجعي السريع](#-الجدول المرجعي السريع)
- [الاعتبارات الدفاعية ومكافحة التتبع](#-الاعتبارات الدفاعية--مكافحة التتبع)
- [إخلاء المسؤولية القانونية](#-إخلاء المسؤولية القانونية)

---

## 🎯 نظرة عامة: استدعاءات النظام مقابل استدعاءات المكتبة

عند تحليل ثنائيات Linux ELF المجمعة، يمكن إعاقة التحليل الثابت عن طريق التشويش أو التجريد أو التعبئة. يوفر التتبع الديناميكي رؤية فورية لإجراءات وقت التشغيل:

1. **مكالمات النظام (`strace`):**
   - الواجهة بين تطبيقات مساحة المستخدم ونواة Linux Linux.
   - يتتبع إدخال/إخراج القرص (`openat`، `read`، `write`)، والشبكات (`socket`، `connect`)، وتخصيص الذاكرة (`mmap`، `brk`)، وإدارة العمليات (`fork`، `execve`، `ptrace`).
2. **استدعاءات المكتبة (`ltrace`):**
   - الواجهة بين المكتبات المشتركة الثنائية والديناميكية (مثل `libc.so`، `libssl.so`).
   - اعتراض المكالمات مثل `strcmp()`، و`strlen()`، و`malloc()`، و`system()`، وإجراءات التشفير، مما يؤدي في كثير من الأحيان إلى كشف بيانات اعتماد النص العادي في الذاكرة قبل التشفير.

---

## 📥 التثبيت
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y strace ltrace

# Arch Linux
sudo pacman -S strace ltrace

# Fedora / Red Hat / CentOS
sudo dnf install -y strace ltrace
```
---

## ⚡strace: تتبع مكالمات النظام

### تنفيذ التتبع الأساسي
قم بتشغيل ثنائي مستهدف ودفق جميع مكالمات النظام إلى الخطأ القياسي:
```bash
strace ./target_binary
```
أعد توجيه الإخراج إلى ملف حتى لا يؤدي إلى تشويش تنفيذ البرنامج:
```bash
strace -o trace.log ./target_binary
```
### تصفية مكالمات النظام حسب الفئة أو الاسم (`-e`)
تجنب الضوضاء عن طريق تصفية فئات syscall ذات الصلة فقط:
```bash
# Trace ONLY file-related operations (open, read, write, stat, access)
strace -e trace=file ./target_binary

# Trace ONLY network socket connections and data transfers
strace -e trace=network ./target_binary

# Trace process execution and fork/clone operations
strace -e trace=process ./target_binary

# Filter specific syscalls by exact name
strace -e trace=open,openat,read,write ./target_binary
```
### تتبع العمليات التابعة والعمليات متعددة الخيوط (`-f`)
تؤدي الثنائيات والبرامج الضارة الحديثة إلى ظهور عمليات أو سلاسل عمليات عاملة فرعية. بدون `-f`، سوف يفتقدهم strace:
```bash
# Trace all child forks and threads
strace -f ./target_binary

# Trace children and save each process output into separate files (trace.pid)
strace -ff -o trace ./target_binary
```
### إرفاق العمليات الجارية (`-p`)
مراقبة خدمة أو عملية برامج ضارة نشطة دون إعادة تشغيلها:
```bash
# Attach by Process ID (PID)
sudo strace -p 1337

# Attach to process and follow all newly spawned threads
sudo strace -f -p 1337 -o attached.log
```
### توسيع السلاسل ومخرجات المخزن المؤقت (`-s`)
بشكل افتراضي، يقوم strace باقتطاع السلاسل المطبوعة المكونة من 32 حرفًا. قم بزيادة طول السلسلة لفحص مسارات الملفات الكاملة أو عناوين URL أو الحمولات:
```bash
# Display up to 2048 characters of strings and buffers
strace -s 2048 -e trace=read,write ./target_binary
```
### التصنيف الإحصائي وتحليل عنق الزجاجة (`-c`)
حساب الوقت المستغرق وعدد المكالمات والأخطاء لكل مكالمة نظام:
```bash
strace -c ./target_binary
```
*مثال ملخص الإخراج:*
```text
% time     seconds  usecs/call     calls    errors syscall
------ ----------- ----------- --------- --------- ----------------
 54.32    0.001200          40        30           read
 32.10    0.000710          71        10         2 openat
 13.58    0.000300          15        20           write
```
### الحقن الخاطئ للتشويش والاختبار (`-e fault`)
أدخل أخطاء تعسفية في مكالمات النظام لتقييم المرونة:
```bash
# Force the 3rd invocation of openat() to fail with Permission Denied (EACCES)
strace -e fault=openat:error=EACCES:when=3 ./target_binary
```
---

## 🧩 ltrace: تتبع مكالمات المكتبة الديناميكية

### اعتراض المكتبة الأساسية
اعتراض المكالمات التي تم إجراؤها على مكتبات `.so` الديناميكية:
```bash
ltrace ./target_binary
```
### كسر عمليات التحقق من كلمات المرور المشفرة (`strcmp`)
في الهندسة العكسية لـ CTFs وثنائيات التحدي، تكشف وظائف مقارنة كلمات المرور (`strcmp`، `strncmp`، `memcmp`) عن كلمة المرور المستهدفة في وسيطات النص العادي:
```bash
ltrace -s 128 -e strcmp,strncmp ./license_check
```
*مثال الإخراج:*
```text
strcmp("user_input", "SUPER_SECRET_FLAG_2026") = -1
```
### فك رموز C++ (`-C`)
أسماء أساليب C++ المشوهة للمترجم (`_ZNK4Test3fooEv` -> `Test::foo()`):
```bash
ltrace -C ./cpp_binary
```
### تصفية وظائف مكتبة محددة
```bash
# Intercept only memory allocation and string functions
ltrace -e malloc+free+strdup ./target_binary

# Intercept calls originating from specific dynamic libraries
ltrace -l /lib/x86_64-linux-gnu/libcrypto.so.3 ./target_binary
```
---

## 🔄 الجمع بين سير العمل strace و ltrace

عند إجراء الفرز الأولي على ثنائي غير معروف:

1. **الخطوة 1:** قم بتشغيل `strace -e trace=file,network ./binary` لمعرفة الملفات والمآخذ التي يتفاعل معها.
2. **الخطوة 2:** قم بتشغيل `ltrace -s 256 ./binary` للكشف عن مقارنات سلسلة المكتبة الداخلية واستدعاءات التشفير ومنطق الذاكرة.
3. **الخطوة 3:** اجمع بين الأداتين:
   ```bash
   ltrace -S ./binary
   ```
*(تخبر علامة `-S` ltrace بعرض مكالمات النظام جنبًا إلى جنب مع مكالمات المكتبة في عرض موحد واحد).*

---

## ⚡ جدول مرجعي سريع

| الهدف | الأمر |
|-----------|--------|
| **تتبع جميع مكالمات النظام** | `strace ./binary` |
| **تسجيل الدخول إلى الملف** | `strace -o log.txt ./binary` |
| **تتبع عمليات الملف** | `strace -e trace=file ./binary` |
| **تتبع نشاط الشبكة** | `strace -e trace=network ./binary` |
| ** اتبع العمليات التابعة ** | `strace -f ./binary` |
| **إرفاق تشغيل PID** | `sudo strace -f -p <PID>` |
| **توسيع طول السلسلة** | `strace -s 2048 ./binary` |
| ** إحصائيات ملخص Syscall ** | `strace -c ./binary` |
| **تتبع مكالمات المكتبة** | `ltrace ./binary` |
| **التحقق من كلمة المرور** | `ltrace -s 128 -e strcmp,strncmp ./binary` |
| **تفكيك أسماء C++** | `ltrace -C ./binary` |
| ** الجمع بين Lib + Syscall **| `ltrace -S ./binary` |

---

## 🛡️ الاعتبارات الدفاعية ومكافحة التتبع

1. **الفحص الذاتي لـ Ptrace:** يسمح Linux لمتتبع واحد فقط بالربط بعملية عبر `ptrace`. سوف يفشل الاتصال بـ `ptrace(PTRACE_TRACEME, 0, 1, 0)` إذا كان `strace` أو `ltrace` متصلاً بالفعل.
2. **الارتباط الثابت (`-static`):** لا تحتوي الثنائيات المجمعة باستخدام الارتباط الثابت على مراجع مكتبة ديناميكية (`DT_NEEDED`)، مما يجعل `ltrace` غير فعال تمامًا (استخدم `strace` بدلاً من ذلك).

---

## ⚠️إخلاء المسؤولية القانونية

> [!تنبيه]
> strace وltrace عبارة عن أدوات تشخيصية ديناميكية مصممة لتطوير البرامج وتصحيح الأخطاء وتقييمات الأمان المعتمدة. يُمنع منعا باتا تتبع العمليات غير المصرح بها أو اعتراض اتصالات الملكية دون إذن.