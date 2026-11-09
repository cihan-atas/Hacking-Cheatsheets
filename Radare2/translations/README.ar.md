<!-- language-home-navigation -->
[🏠 الصفحة الرئيسية العربية](../../README.ar.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ⚡ Radare2 (r2) - ورقة الغش الكاملة

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ██████╗  █████╗ ██████╗  █████╗ ██████╗ ███████╗██████╗ 
  ██╔══██╗██╔══██╗██╔══██╗██╔══██╗██╔══██╗██╔════╝╚════██╗
  ██████╔╝███████║██║  ██║███████║██████╔╝█████╗   █████╔╝
  ██╔══██╗██╔══██║██║  ██║██╔══██║██╔══██╗██╔══╝  ██╔═══╝ 
  ██║  ██║██║  ██║██████╔╝██║  ██║██║  ██║███████╗███████╗
  ╚═╝  ╚═╝╚═╝  ╚═╝╚═════╝ ╚═╝  ╚═╝╚═╝  ╚═╝╚══════╝╚══════╝
      UNIX-like Command-Line Reverse Engineering Framework
```
<p align="center">
  <img src="https://img.shields.io/badge/Radare2-CLI_Reversing-blue?style=for-the-badge" alt="Radare2">
  <img src="https://img.shields.io/badge/Author-Sergi_Álvarez-orange?style=for-the-badge" alt="pancake">
  <img src="https://img.shields.io/badge/Category-Reverse_Engineering-green?style=for-the-badge" alt="الهندسة العكسية">
  <img src="https://img.shields.io/badge/License-LGPLv3-purple?style=for-the-badge" alt="LGPLv3">
</ص>

<p align="center">
  <b>⚡ إطار عمل قوي ومفتوح المصدر وقائم على المحطة الطرفية للتحليل الثنائي والتفكيك وتصحيح الأخطاء والتحرير السداسي والتصحيح الثنائي</b>
</ص>

---

## 📋 جدول المحتويات

- [نظرة عامة والهندسة المعمارية](#-نظرة عامة--الهندسة المعمارية)
- [التثبيت](#-التثبيت)
- [فتح الملفات وأوضاع الجلسة](#-فتح الملفات--أوضاع الجلسة)
- [أوامر التحليل الثنائي (`a`)](#-binary-analyse-commands-a)
- [الملاحة والبحث (`s`)](#-الملاحة--البحث-s)
- [الطباعة والتفكيك (`p`)](#-الطباعة--التفكيك-p)
- [أوضاع الرسم البياني والمرئي التفاعلي (`V` و`VV`)](#-interactive-visual--graph-modes-v--vv)
- [تصحيح وتعديل الثنائي (`w`)](#-تصحيح ثنائي--تعديل-w)
- [مجموعة أدوات Radare2 المصاحبة](#-مجموعة أدوات-radare2-companion)
  - [rabin2: استخراج المعلومات الثنائية](#rabin2-استخراج المعلومات الثنائية)
  - [rasm2: المجمع والمفكك](#rasm2-assembler--disassembler)
  - [radiff2: محرك الفرق الثنائي](#radiff2-binary-difference-engine)
  - [rafind2: البحث عن البايت والسلسلة](#rafind2-byte--string-search)
  - [rax2: المحول الأساسي والرقمي](#rax2-base--numeric-converter)
- [أساسيات التصحيح باستخدام r2 (`-d`)](#-debugging-basics-with-r2--d)
- [الجدول المرجعي السريع](#-الجدول المرجعي السريع)
- [الاعتبارات الدفاعية ومكافحة التراجع](#-الاعتبارات الدفاعية--مكافحة التراجع)
- [إخلاء المسؤولية القانونية](#-إخلاء المسؤولية القانونية)

---

## 🎯 نظرة عامة والهندسة المعمارية

تم إنشاء **Radare2** (`r2`) بواسطة **Sergi Álvarez (pancake)**، وهو عبارة عن إطار عمل هندسي عكسي معياري محمول تم تصميمه بالكامل حول فلسفة UNIX. يتم تشغيله بالكامل داخل جهازك دون الحاجة إلى تبعيات رسومية ثقيلة:
- **دعم كامل للبنية:** x86/x64، ARM، ARM64، MIPS، PowerPC، AVR، SPARC، WebAssembly، Java، Dalvik، وEVM.
- ** أدوات CLI المساعدة القابلة للتركيب: ** تأتي مرفقة بأدوات سطر الأوامر المتخصصة (`rabin2`، `rasm2`، `radiff2`، `rafind2`، `rax2`).
- ** الأوضاع المرئية التفاعلية: ** محررات سداسية ذات شاشة مقسمة، ورسوم بيانية لتدفق التحكم في ASCII، وشاشات تسجيل، وطرق عرض أداة فك التحويل البرمجي.
- **قابل للبرمجة وقابل للتوسيع:** يتكامل مع Python وr2pipe وGhidra (`r2ghidra`) وRizin.

---

## 📥 التثبيت
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y radare2

# Arch Linux
sudo pacman -S radare2

# Install latest Git master (Recommended for most features)
git clone https://github.com/radareorg/radare2.git
cd radare2 && sys/install.sh

# Install r2ghidra decompiler plugin
r2pm update
r2pm -i r2ghidra
```
---

## 🚀 فتح الملفات وأوضاع الجلسة
```bash
# Open binary in read-only mode
r2 /bin/ls

# Open binary in read-write mode (required for patching)
r2 -w target.bin

# Open binary in debugger mode
r2 -d target.bin

# Open binary without parsing headers (raw flat binary / shellcode)
r2 -b 32 -n /path/to/raw.bin

# Open in quiet mode (no greeting banner)
r2 -q target.bin
```
---

## 🔬 أوامر التحليل الثنائي (`a`)

داخل هيكل `r2`، تكون الأوامر عبارة عن أحرف مفردة أو بادئات قصيرة. يمكنك إلحاق `?` بأي أمر لرؤية المساعدة المضمنة:
```text
[0x00000000]> a?        # Show analysis help
[0x00000000]> aa        # Basic analysis (symbols, basic blocks)
[0x00000000]> aaa       # Deep analysis (functions, cross-references, calls)
[0x00000000]> aaaa      # Experimental auto-analysis (takes longer)

[0x00000000]> afl       # List all identified functions
[0x00000000]> afi       # Display function info at current address
[0x00000000]> afvn      # Rename local variable: afvn <new_name> <old_name>
[0x00000000]> axt <addr># Find cross-references (xrefs) TO address
[0x00000000]> axf <addr># Find cross-references (xrefs) FROM address
```
---

## 🧭 الملاحة والبحث (`s`)

يقوم أمر البحث بتغيير عنوان المؤشر الحالي:
```text
[0x00000000]> s main        # Seek to main function
[0x00401122]> s 0x401200    # Seek to absolute address
[0x00401200]> s+ 32         # Seek 32 bytes forward
[0x00401220]> s- 16         # Seek 16 bytes backward
[0x00401210]> s/ hit0_0     # Seek to search hit
[0x00401210]> u             # Undo seek (go back to previous address)
[0x00401200]> U             # Redo seek
```
---

## 📜 الطباعة والتفكيك (`p`)
```text
[0x00401122]> pd 10     # Disassemble 10 instructions
[0x00401122]> pdf       # Disassemble entire current function
[0x00401122]> pdc       # Decompile function using r2ghidra plugin
[0x00401122]> px 64     # Print 64 bytes in hex dump format
[0x00401122]> ps        # Print string at current address
[0x00401122]> pz        # Print zero-terminated string
[0x00401122]> pi 5      # Print 5 instructions (concise format)
```
---

## 🖥️ الأوضاع المرئية والرسمية التفاعلية (`V` و`VV`)

يؤدي الضغط على `V` أو `VV` إلى تحويل Radare2 إلى تطبيق طرفي تفاعلي غني:

- **الدخول إلى الوضع المرئي:** اكتب `V`
  - اضغط على `p` / `P` لتدوير طرق العرض (Hex dump -> Disassembly -> Debugger -> Decompiler).
  - اضغط على `c` لتبديل وضع المؤشر (استخدم مفاتيح الأسهم أو `hjkl` لتحديد البايتات).
  - اضغط على `d` لإعادة تعريف البايتات كرمز أو بيانات أو سلاسل.
  - اضغط على `q` للعودة إلى الموجه القياسي.
- **أدخل إلى عرض الرسم البياني:** اكتب `VV`
  - يعرض الرسم البياني لتدفق التحكم ASCII (CFG).
  - استخدم `Tab` / `Shift+Tab` للانتقال بين العقد الفرعية المشروطة.
  - اضغط على `t` لمتابعة الفرع الحقيقي؛ `f` لمتابعة الفرع الخاطئ.
  - اضغط على `u` للتراجع عن الفرع.

---

## ✍️ التصحيح والتعديل الثنائي (`w`)

لتعديل ثنائي على القرص، تأكد من فتح r2 في وضع الكتابة (`r2 -w target` أو اكتب `oo+` داخل جلسة نشطة):
```text
[0x00401150]> oo+                           # Re-open current binary with write permissions
[0x00401150]> wa nop                        # Assemble 'nop' instruction at current address
[0x00401150]> wa jmp 0x401190               # Patch a jump instruction
[0x00401150]> wx 90909090                   # Write hex bytes (4 NOPs)
[0x00401150]> w "Modified string\x00"       # Overwrite string with null-terminator
[0x00401150]> w0 16                         # Zero-fill 16 bytes (NUL padding)
```
---

## 🛠️ مجموعة الأدوات المصاحبة لـ Radare2

يتضمن Radare2 أدوات مساعدة CLI مستقلة:

### Rabin2: استخراج المعلومات الثنائية
```bash
# Print general binary architecture, bits, and security mitigations (NX, PIE, Canary)
rabin2 -I target.bin

# List imported symbols and functions (libc calls)
rabin2 -i target.bin

# List exported symbols
rabin2 -E target.bin

# Extract ASCII & UTF-16 strings
rabin2 -z target.bin

# List sections (.text, .data, .rodata)
rabin2 -S target.bin
```
### rasm2: المجمع والمفكك
```bash
# Assemble x64 instructions into hexadecimal opcode
rasm2 -a x86 -b 64 "xor eax, eax; ret"
# Output: 31c0c3

# Disassemble hex opcodes into human-readable assembly
rasm2 -a x86 -b 64 -d "31c0c3"
# Output:
# xor eax, eax
# ret
```
### radiff2: محرك الفرق الثنائي
```bash
# Compare two binaries and count differing bytes
radiff2 -c original.bin patched.bin

# Detailed code comparison graph
radiff2 -g main original.bin patched.bin | xdot -
```
### rafind2: البحث عن البايت والسلسلة
```bash
# Search for ASCII string in file
rafind2 -s "admin_password" target.bin

# Search for hexadecimal pattern
rafind2 -x "4889e5" target.bin
```
### rax2: المحول الأساسي والرقمي
```bash
# Convert hex to decimal
rax2 0x1337      # Output: 4919

# Convert decimal to hex
rax2 4919        # Output: 0x1337

# Convert string to hex
rax2 -S "admin"  # Output: 61646d696e
```
---

## 🐛 أساسيات تصحيح الأخطاء باستخدام r2 (`-d`)
```bash
# Start debugging binary with arguments
r2 -d target.bin arg1 arg2
```
داخل قذيفة المصحح:
```text
[0x7f112233]> db main       # Set software breakpoint at main
[0x7f112233]> db 0x401150   # Set breakpoint at specific address
[0x7f112233]> dbl           # List all active breakpoints
[0x7f112233]> dc            # Continue execution until breakpoint
[0x7f112233]> ds            # Step into (single instruction)
[0x7f112233]> dso           # Step over function calls
[0x7f112233]> dr            # Print all CPU registers
[0x7f112233]> dr rax        # Print specific register value
[0x7f112233]> dr rax = 0x1  # Modify register value
```
---

## ⚡ جدول مرجعي سريع

| الهدف | الأمر |
|-----------|--------|
| **تحليل عميق** | `aaa` |
| ** وظائف القائمة ** | `afl` |
| **البحث عن الوظيفة/العنوان** | `s <addr>` / `s main` |
| **وظيفة التفكيك** | `pdf` |
| **وظيفة التفكيك (r2ghidra)** | `pdc` |
| **التفكيك المرئي التفاعلي** | `V` |
| **الرسم البياني لتدفق ASCII** | `VV` |
| **تعليمات التجميع** | `wa <instruction>` |
| **اكتب بايت سداسي عشري** | `wx <bytes>` |
| ** إعادة الفتح في القراءة والكتابة ** | `oo+` |
| **استخراج المعلومات الثنائية** | `rabin2 -I <binary>` |
| **تجميع CLI** | `rasm2 -a x86 -b 64 "<asm>"` |

---

## 🛡️ الاعتبارات الدفاعية ومكافحة التراجع

1. ** اكتشاف مكافحة التصحيح: ** يعتمد مصحح أخطاء Radare2 على Linux `ptrace` القياسي. يمكن للثنائيات ذات عمليات الفحص الذاتي لـ `ptrace(PTRACE_TRACEME)` حظر المرفقات ما لم يتم تصحيحها.
2. ** التشويش والتحكم في تسطيح التدفق: ** الثنائيات المبهمة بشدة (OLLVM) تربك عملية استرداد الكتلة الأساسية. استخدم `aaaa` أو محركات التنفيذ الرمزية (مثل angr) لتبسيط الوظائف المسطحة.

---

## ⚠️إخلاء المسؤولية القانونية

> [!تنبيه]
> Radare2 عبارة عن مجموعة أدوات للهندسة العكسية والتحليل مخصصة حصريًا للتقييمات الأمنية المعتمدة وتحليل البرامج الضارة وأبحاث الثغرات الأمنية والاستخدام التعليمي الشخصي. قد تنتهك البرامج الاحتكارية ذات الهندسة العكسية دون إذن اتفاقيات ترخيص المستخدم النهائي وقوانين حقوق الطبع والنشر.