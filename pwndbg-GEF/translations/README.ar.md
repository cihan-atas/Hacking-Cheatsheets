<!-- language-home-navigation -->
[🏠 الصفحة الرئيسية العربية](../../README.ar.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🎯 pwndbg & GEF - ورقة الغش الكاملة

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ██████╗ ██╗    ██╗███╗   ██╗██████╗ ██████╗  ██████╗      ██████╗ ███████╗███████╗
  ██╔══██╗██║    ██║████╗  ██║██╔══██╗██╔══██╗██╔════╝     ██╔════╝ ██╔════╝██╔════╝
  ██████╔╝██║ █╗ ██║██╔██╗ ██║██║  ██║██████╔╝██║  ███╗    ██║  ███╗█████╗  █████╗  
  ██╔═══╝ ██║███╗██║██║╚██╗██║██║  ██║██╔══██╗██║   ██║    ██║   ██║██╔══╝  ██╔══╝  
  ██║     ╚███╔███╔╝██║ ╚████║██████╔╝██████╔╝╚██████╔╝    ╚██████╔╝███████╗██║     
  ╚═╝      ╚══╝╚══╝ ╚═╝  ╚═══╝╚═════╝ ╚═════╝  ╚═════╝      ╚═════╝ ╚══════╝╚═╝     
         Next-Gen GDB Enhancements for Reversing & Binary Exploitation
```
<p align="center">
  <img src="https://img.shields.io/badge/pwndbg-GDB_Plugin-blue?style=for-the-badge" alt="pwndbg">
  <img src="https://img.shields.io/badge/GEF-GDB_Enhanced_Features-orange?style=for-the-badge" alt="مرفق البيئة العالمية">
  <img src="https://img.shields.io/badge/Category-Binary_Exploitation_&_RE-green?style=for-the-badge" alt="Reversing & Pwn">
  <img src="https://img.shields.io/badge/License-MIT_%7C_FOSS-purple?style=for-the-badge" alt="License FOSS">
</ص>

<p align="center">
  <b>🎯 التحسينات الرئيسية لمحطة GDB مفتوحة المصدر والمصممة للهندسة العكسية الثنائية، وتحليل تلف الذاكرة، وفحص تخطيط الكومة، وتطوير استغلال CTF</b>
</ص>

---

## 📋 جدول المحتويات

- [نظرة عامة ولماذا تحتاج Vanilla GDB إلى تحسينات](#-نظرة عامة--لماذا-vanilla-gdb-needs-enhancements)
- [التثبيت والتبديل بين الإطارات](#-التثبيت--التبديل بين الإطارات)
  - [تثبيت pwndbg](#installing-pwndbg)
  - [تثبيت مرفق البيئة العالمية](#installing-gef)
  - [إدارة `.gdbinit`](#managing-gdbinit)
- [محرك عرض السياق](#-the-context-display-engine)
  - [قسم السجلات](#قسم السجلات)
  - [توقعات التفكيك والقفز](#التفكيك--توقعات القفز)
  - [المكدس والتلسكوب](#stack--telescope)
  - [Backtrace والخيوط](#backtrace--threads)
- [فحص الذاكرة والتنقل](#-فحص الذاكرة--التنقل)
  - [مؤشرات التلسكوب (`telescope`)](#تلسكوب مؤشرات التلسكوب)
  - [تعيينات ذاكرة العملية (`vmmap`)](#process-memory-mappings-vmmap)
  - [البحث في الذاكرة (`search`)](#searching-memory-search)
- [استغلال الكومة وفحصها](#-استغلال الكومة--الفحص)
  - [عرض القطع والساحات](#عرض-الأجزاء--الساحات)
  - [فحص الصناديق المجانية (Tcache، Fastbins، Smallbins)](#inspecting-free-bins-tcache-fastbins-smallbins)
  - [تصور تخطيط الكومة (`vis_heap_chunks`)](#visualizing-heap-layout-vis_heap_chunks)
- [مساعدو تطوير الاستغلال](#-مساعدو تطوير الاستغلال)
  - [تدقيق الفحص والتخفيف](#checksec--mitigation-audit)
  - [محدد موقع Stack Canary (`canary`)](#stack-canary-locator-canary)
  - [الأنماط الدورية وحساب الأوفست](#الأنماط الدورية--حساب الأوفست)
  - [صيد أدوات ROP داخل الذاكرة](#in-memory-rop-gadget-hunting)
- [الجدول المرجعي السريع](#-الجدول المرجعي السريع)
- [الاعتبارات الدفاعية والحماية الثنائية](#-الاعتبارات الدفاعية--الحماية الثنائية)
- [إخلاء المسؤولية القانونية](#-إخلاء المسؤولية القانونية)

---

## 🎯 نظرة عامة ولماذا تحتاج Vanilla GDB إلى تحسينات

يعد برنامج GNU Debugger القياسي (`gdb`) أداة ذات قوة صناعية، ولكن تجربة المستخدم المبتكرة الخاصة بها بسيطة بصريًا. ولا يقوم تلقائيًا بتحديث حالات التسجيل أو إلغاء الإشارة إلى المؤشرات متعددة المستويات أو عرض طرق عرض المكدس عند التنقل عبر التعليمات.

**pwndbg** و**GEF** يحولان GDB إلى مجموعة متميزة من الهندسة العكسية والاستغلال الثنائي:
- **pwndbg:** تم تصميمه خصيصًا لاستغلال البرامج ذات المستوى المنخفض، وتصحيح أخطاء الكومة (glibc 2.27 - 2.39+ الأجزاء الداخلية للمخصص)، وإلغاء مرجعية المؤشر تلقائيًا.
- **GEF (ميزات GDB المحسنة):** برنامج نصي واحد مستقل لـ Python بدون أي تبعيات خارجية، ويقدم ميزات غير محددة للهندسة المعمارية (ARM، MIPS، PowerPC، x86) وتحليل البنية الديناميكية.

---

## 📥 التثبيت والتبديل بين الأطر

### تثبيت pwndbg
```bash
git clone https://github.com/pwndbg/pwndbg
cd pwndbg
./setup.sh
```
### تثبيت مرفق البيئة العالمية
```bash
# Automated one-liner download
bash -c "$(curl -fsSL https://gef.blah.cat/sh)"
```
### إدارة `.gdbinit`
للتبديل بين pwndbg وGEF بدون تعارض، قم بتكوين `~/.gdbinit` الخاص بك:
```bash
# To use pwndbg:
echo "source /path/to/pwndbg/gdbinit.py" > ~/.gdbinit

# To use GEF:
echo "source ~/.gdbinit-gef.py" > ~/.gdbinit
```
---

## 🖥️ محرك عرض السياق

في كل مرة يتوقف فيها التنفيذ عند نقطة توقف أو خطوة، يقوم كل من pwndbg وGEF بطباعة عرض طرفي آلي متعدد اللوحات:
```text
──────────────────────────[ REGISTERS / FLAGS ]──────────────────────────
RAX: 0x0 
RBX: 0x7fffffffe000 
RCX: 0x7ffff7ec2000 --> 0x00010102464c457f 
RDX: 0x7fffffffe1a0 --> 0x7fffffffe494 ('USER=zeus')
RSI: 0x7fffffffe198 --> 0x7fffffffe47e ('/bin/target')
RDI: 0x1 
RBP: 0x7fffffffe0a0 --> 0x7fffffffe0c0 --> 0x0
RSP: 0x7fffffffe080 --> 0x401156 (<main+34>: mov eax, 0)
RIP: 0x401140 (<main+12>: cmp QWORD PTR [rbp-0x8], 0x0)
──────────────────────────[ DISASM / CODE ]──────────────────────────
   0x401135 <main+1>     push   rbp
   0x401136 <main+2>     mov    rbp, rsp
   0x401139 <main+5>     sub    rsp, 0x10
 ► 0x401140 <main+12>    cmp    QWORD PTR [rbp-0x8], 0
   0x401145 <main+17>    je     0x401151 <main+29>  <-- JUMP IS TAKEN
   0x401147 <main+19>    mov    eax, 0x1
──────────────────────────[ STACK ]──────────────────────────
00:0000│ rsp 0x7fffffffe080 --> 0x401156 (<main+34>: mov eax, 0)
01:0008│     0x7fffffffe088 --> 0x0
02:0010│ rbp 0x7fffffffe0a0 --> 0x7fffffffe0c0 --> 0x0
03:0018│     0x7fffffffe0a8 --> 0x7ffff7df6d90 (<__libc_start_call_main+128>: mov edi, eax)
──────────────────────────[ BACKTRACE ]──────────────────────────
 ► 0   0x401140 main+12
   1   0x7ffff7df6d90 __libc_start_call_main+128
   2   0x7ffff7df6e40 __libc_start_main+128
   3   0x401065 _start+37
```
---

## 🔍 فحص الذاكرة والملاحة

### مؤشرات متداخلة (`telescope`)
قم بإلغاء الإشارة إلى مؤشرات الذاكرة بشكل متكرر، أو عرض نص ASCII، أو رموز الوظائف، أو مؤشرات الهدف:
```text
pwndbg> telescope $rsp 16       # Dereference 16 entries starting at RSP
pwndbg> telescope 0x404000 8    # Dereference data section
```
### تعيينات ذاكرة العملية (`vmmap`)
يعرض أجزاء كاملة من الذاكرة الافتراضية مع أذونات القراءة/الكتابة/التنفيذ:
```text
pwndbg> vmmap
```
*اكتشاف المقاطع بسرعة باستخدام أذونات `rwx` (أمر حيوي لحقن كود القشرة).*

### ذاكرة البحث (`search`)
حدد موقع السلاسل أو الأعداد الصحيحة أو المؤشرات في أي مكان عبر صفحات الذاكرة المحملة:
```text
pwndbg> search -s "/bin/sh"     # Search for string "/bin/sh"
pwndbg> search -p 0x401135      # Search for pointers to main()
pwndbg> search -x "41414141"    # Search for raw hexadecimal bytes
```
---

## 🧱 استغلال وفحص الكومة

يعد تحليل مخصصات الكومة `glibc` الحديثة أمرًا مباشرًا باستخدام أوامر الكومة المخصصة:

### عرض القطع والساحات
```text
pwndbg> heap                    # Display entire heap layout
pwndbg> chunk 0x602000          # Parse chunk headers (size, flags: PREV_INUSE, IS_MMAPPED, NON_MAIN_ARENA)
pwndbg> arenas                  # List all active heap arenas
```
### فحص الصناديق المجانية (Tcache، Fastbins، Smallbins)
```text
pwndbg> bins                    # Show all free bins simultaneously
pwndbg> tcache                  # Display per-thread tcache entries
pwndbg> fastbins                # List single-linked fastbins
pwndbg> unsortedbin             # List unsorted bin chunks (used for libc leak)
```
### تصور تخطيط الكومة (`vis_heap_chunks`)
```text
pwndbg> vis_heap_chunks 10      # Draw color-coded visual representation of consecutive heap chunks
```
---

## 🛠️ استغلال مساعدي التطوير

### تدقيق الفحص والتخفيف
تحقق من إجراءات تخفيف أمان المترجم مباشرةً من موجه GDB:
```text
pwndbg> checksec
```
*تفقد:*
- **RELRO:** جزئي مقابل RELRO كامل (يحمي جدول الأوفست العالمي `.got.plt`)
- **المكدس:** تم العثور على كناري مقابل عدم وجود كناري
- **NX:** تم تمكين NX (مكدس/كومة غير قابلة للتنفيذ)
- **PIE:** تم تمكين PIE (الموضع المستقل القابل للتنفيذ / ASLR)

### محدد موقع ستاك كناري (`canary`)
حدد موقع قيمة الكناري لمؤشر الترابط في التخزين المحلي لمؤشر الترابط (TLS) وتحقق من مكان وجودها في إطار المكدس الحالي:
```text
pwndbg> canary
```
### الأنماط الدورية وحساب الأوفست
تحديد الإزاحة الدقيقة للكتابة فوق مؤشر التعليمات (EIP/RIP):
```text
# Generate a 100-byte De Bruijn cyclic sequence
pwndbg> cyclic 100

# After the crash (SIGSEGV), calculate offset from faulty address or register value
pwndbg> cyclic -l $rip
# Output: Finding cyclic pattern at offset 72
```
### صيد أدوات ROP داخل الذاكرة
البحث في المقاطع القابلة للتنفيذ المحملة عن أدوات محددة:
```text
pwndbg> rop --grep "pop rdi"
```
---

## ⚡ جدول مرجعي سريع

| الهدف | الأمر |
|-----------|--------|
| **ابدأ بـ pwndbg/GEF**| `gdb ./target_binary` |
| **فحص السجلات/المكدس** | `context` |
| ** مؤشر عودي Deref **| `telescope $rsp 10` |
| **خريطة الذاكرة وفحص RWX**| `vmmap` |
| **سلسلة البحث في الذاكرة**| `search -s "/bin/sh"` |
| ** عمليات تخفيف آثار التدقيق ** | `checksec` |
| ** البحث عن ستاك كناري ** | `canary` |
| **نظرة عامة على قطع الكومة** | `heap` |
| **فحص الصناديق الحرة** | `bins` |
| ** حساب إزاحة المخزن المؤقت **| `cyclic 100` -> `cyclic -l <val>` |
| **تعيين نقطة التوقف عند الرمز**| `b *main` |
| ** تشغيل / متابعة / خطوة ** | `r` / `c` / `si` / `ni` |

---

## 🛡️ الاعتبارات الدفاعية والحماية الثنائية

1. **Full RELRO:** التجميع باستخدام `-Wl,-z,relro,-z,now` يجعل `.got` للقراءة فقط بالكامل، مما يؤدي إلى هزيمة هجمات الكتابة الفوقية GOT.
2. **Stack Canaries:** التجميع باستخدام `-fstack-protector-all` يضع ملفات تعريف الارتباط قبل عناوين الإرجاع، مما يتسبب في تجاوز سعة المخزن المؤقت لـ `__stack_chk_fail`.
3. **PIE & ASLR:** يقوم بترتيب التعليمات البرمجية والكومة وعناوين قاعدة المكتبة بشكل عشوائي عند كل عملية تنفيذ.

---

## ⚠️إخلاء المسؤولية القانونية

> [!تنبيه]
> pwndbg وGEF عبارة عن أدوات تحليل ثنائية وهندسة استغلال مخصصة حصريًا لأبحاث الثغرات الأمنية المصرح بها، وتدقيق البرامج الدفاعية، ومسابقات CTF الأكاديمية. يعد استغلال البرامج دون إذن أمرًا غير قانوني.