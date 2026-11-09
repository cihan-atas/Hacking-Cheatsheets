<!-- language-home-navigation -->
[🏠 الصفحة الرئيسية العربية](../../README.ar.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ⌨️ rlwrap - ورقة الغش الكاملة

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ██████╗ ██╗     ██╗    ██╗██████╗  █████╗ ██████╗ 
  ██╔══██╗██║     ██║    ██║██╔══██╗██╔══██╗██╔══██╗
  ██████╔╝██║     ██║ █╗ ██║██████╔╝███████║██████╔╝
  ██╔══██╗██║     ██║███╗██║██╔══██╗██╔══██║██╔═══╝ 
  ██║  ██║███████╗╚███╔███╔╝██║  ██║██║  ██║██║     
  ╚═╝  ╚═╝╚══════╝ ╚══╝╚══╝ ╚═╝  ╚═╝╚═╝  ╚═╝╚═╝     
       Readline Wrapper for Reverse Shells & Dumb Terminals
```
<p align="center">
  <img src="https://img.shields.io/badge/rlwrap-Readline_Wrapper-blue?style=for-the-badge" alt="rlwrap">
  <img src="https://img.shields.io/badge/Utility-Shell_Stabilization-green?style=for-the-badge" alt="تثبيت Shell">
  <img src="https://img.shields.io/badge/Category-Exploitation_&_Shells-orange?style=for-the-badge" alt="الاستغلال والأصداف">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="License GPLv2">
</ص>

<p align="center">
  <b>🌐 تحويل مستمعي Netcat الخام والأصداف العكسية الغبية إلى واجهات سطر أوامر كاملة باستخدام مفاتيح الأسهم والسجل والإكمال التلقائي</b>
</ص>

---

## 📋 جدول المحتويات

- [مقدمة: مشكلة "الصدفة الغبية"](#-مقدمة-مشكلة-الصدفة الغبية)
- [التثبيت](#-التثبيت)
- [الاستخدام الأساسي](#-الاستخدام الأساسي)
- [التقاط الأصداف العكسية (`rlwrap + nc`)](#-catch-reverse-shells-rlwrap--nc)
- [إدارة التاريخ المستمر](#-إدارة-التاريخ المستمر)
- [الإكمال التلقائي للأوامر (`-f`)](#-الإكمال التلقائي للأوامر--f)
- [سير عمل ترقية TTY Shell الكامل المكون من 5 خطوات](#-full-5-step-tty-shell-upgrade-workflow)
- [استخدام rlwrap مع أدوات Pentest الأخرى](#-using-rlwrap-with-other-pentest-tools)
  - [Oracle SQL*Plus](#Oracle-sqlplus)
  - [Redis وTelnet](#redis--telnet)
  - [Socat والمقابس المخصصة](#socat--custom-sockets)
- [الجدول المرجعي السريع](#-الجدول المرجعي السريع)
- [النصائح وأفضل الممارسات](#-نصائح--أفضل-الممارسات)
- [إخلاء المسؤولية القانونية](#-إخلاء المسؤولية القانونية)

---

## 🎯 مقدمة: مشكلة "الصدفة الغبية".

عندما تلتقط الصدفة العكسية باستخدام الأدوات التقليدية مثل Netcat (`nc -lvnp 4444`)، فإنك تحصل على **محطة غبية**. تفتقر المحطة الغبية إلى إمكانات تحرير الخطوط القياسية:

- ❌ يؤدي الضغط على **مفاتيح الأسهم لأعلى/لأسفل** إلى طباعة تسلسلات الهروب مثل `^[[A` أو `^[[B` بدلاً من التنقل عبر سجل الأوامر.
- ❌ الضغط على **Backspace** أو **Delete** يؤدي إلى ظهور `^H` أو `^?` بدلاً من حذف الأحرف.
- ❌ يؤدي الضغط على **Ctrl + C** إلى قتل أداة الاستماع العكسية على الفور، مما يؤدي إلى فقدان الوصول الذي تم الحصول عليه بشق الأنفس.
- ❌ أوامر مثل `su`، أو `sudo`، أو `nano`، أو `vim`، أو `passwd` تفشل بسبب عدم وجود محطة زائفة (PTY) مرفقة.
- ❌ لا يتم الاحتفاظ بسجل الأوامر المستمر عبر الجلسات.

**rlwrap** (Readline Wrapper، الذي كتبه Hans Lub) يحل هذه المشكلة عن طريق الجلوس بين الجهاز الطرفي والأمر المستهدف. فهو يعترض ضربات لوحة المفاتيح، ويستفيد من **GNU Readline**، ويوفر التنقل باستخدام مفتاح الأسهم، وتحرير الأسطر، وملفات سجل الأوامر المستمرة، والإكمال التلقائي المستند إلى القاموس!

---

## 📥 التثبيت

`rlwrap` خفيف الوزن ومتوفر في مستودعات الحزم الافتراضية لكل توزيعة Linux تقريبًا:

### كالي لينكس وديبيان وأوبونتو
```bash
# Update and install rlwrap
sudo apt update && sudo apt install -y rlwrap

# Verify installation and version
rlwrap --version
```
### فيدورا، سينت أو إس، RHEL
```bash
# Fedora / CentOS / RHEL
sudo dnf install -y rlwrap || sudo yum install -y epel-release && sudo yum install -y rlwrap
```
### آرتش لينكس
```bash
# Arch Linux / Manjaro
sudo pacman -S rlwrap
```
### ماك (الجعة المنزلية)
```bash
# macOS
brew install rlwrap
```
---

## 🚀 الاستخدام الأساسي

بناء الجملة الأساسي هو ببساطة تغليف أي سطر أوامر باستخدام `rlwrap`:
```bash
# General syntax
rlwrap [options] <command> [command-arguments]
```
### الأعلام المشتركة

| علم | الوصف |
|------|------------|
| `-c` | تمكين إكمال اسم الملف والمسار |
| `-A` | تجاهل رموز الهروب من التنسيق الفوري (يمنع حدوث خلل في سطر المطالبة) |
| `-r` | تذكر جميع الكلمات التي تم إدخالها عند الإدخال ووضعها في قائمة الكلمات المكتملة |
| `-H <file>` | حدد ملف محفوظات مخصصًا لحفظ واستدعاء الأوامر |
| `-s <num>` | قم بتعيين الحد الأقصى لعدد أسطر السجل (الافتراضي: 300) |
| `-f <file>` | قم بتحميل ملف قاموس الإكمال المخصص |
| `-b <chars>` | تعريف أحرف فاصل الكلمات |
| `-m` | تمكين تحرير الإدخال متعدد الأسطر |
| `-p[color]` | تلوين موجه الأوامر |

---

## 🎣 الإمساك بالأصداف العكسية (`rlwrap + nc`)

حالة الاستخدام الأكثر شيوعًا في الأمان الهجومي هي تغليف مستمع Netcat (راجع [Netcat Cheatsheet](../Netcat/README.md) للتعرف على متغيرات وعلامات المستمع الكاملة):
```bash
# Basic Netcat listener with arrow-key editing & history
rlwrap nc -lvnp 4444

# Professional pentester flags: -c (file completion), -A (prompt fix), -r (remember words)
rlwrap -cAr nc -lvnp 4444
```
### شرح علامات `-cAr`:

1. **`-c` (إكمال الملف):** يسمح باستخدام `<TAB>` للإكمال التلقائي لأسماء الملفات والدلائل.
2. **`-A` (المطالبات):** يمنع التفاف الأسطر الغريبة والتكرار الفوري عندما يرسل الخادم البعيد سلاسل مطالبات غير قياسية (مثل `root@victim:/#`).
3. **`-r` (تذكر الكلمات):** يتعلم الكلمات التي تكتبها في الصدفة حتى تتمكن من إكمالها بعلامة تبويب لاحقًا.

---

## 💾 إدارة التاريخ المستمر

افتراضيًا، يحفظ `rlwrap` سجل الأوامر في `~/.<command>_history`. يمكنك تكوين ملفات سجل مخصصة لكل مشاركة:
```bash
# Save history to a dedicated engagement file
rlwrap -H ~/.history_target_corp nc -lvnp 4444

# Increase history size to 5,000 entries
rlwrap -H ~/.history_target -s 5000 nc -lvnp 4444

# Case-insensitive history search
rlwrap -i nc -lvnp 4444
```
---

## 🔍 الإكمال التلقائي للأوامر (`-f`)

يمكنك تزويد `rlwrap` بملف نصي يحتوي على أوامر Linux أو Windows الشائعة، وسيقوم بإكمالها تلقائيًا عند الضغط على `<TAB>`:
```bash
# Create a wordlist of essential post-exploitation commands
cat << 'EOF' > /tmp/pentest_commands.txt
whoami
id
hostname
uname -a
cat /etc/passwd
cat /etc/shadow
sudo -l
find / -perm -4000 2>/dev/null
netstat -tulnp
ss -tulnp
ps aux
ip a
route -n
crontab -l
EOF

# Start listener with custom tab-completion dictionary
rlwrap -f /tmp/pentest_commands.txt nc -lvnp 4444
```
---

## 🛠️ سير عمل ترقية TTY Shell الكامل المكون من 5 خطوات

في حين أن `rlwrap` يوفر التنقل الفوري باستخدام مفتاح الأسهم والتاريخ، إلا أن بعض البرامج التفاعلية (`su` و`sudo` و`vim`) لا تزال تتطلب محطة TTY مخصصة بالكامل. فيما يلي مسار الترقية المكون من 5 خطوات وفقًا لمعايير الصناعة:

### الخطوة 1: ابدأ تشغيل المستمع باستخدام rlwrap
```bash
# Attacker machine: start enhanced listener
rlwrap -cAr nc -lvnp 4444
```
### الخطوة 2: تشغيل Reverse Shell & Spawn PTY

بمجرد اتصال الصدفة العكسية بمستمعك، قم بإنشاء محطة زائفة على الهدف:
```bash
# Target shell: Spawn PTY via Python
python3 -c 'import pty; pty.spawn("/bin/bash")'

# OR via script if Python is unavailable:
/usr/bin/script -qc /bin/bash /dev/null
```
### الخطوة 3: خلفية الصدفة
```bash
# Press Ctrl + Z to put the reverse shell into the background
[1]+  Stopped                 rlwrap -cAr nc -lvnp 4444
```
### الخطوة 4: تكوين الوضع الخام للمحطة المحلية
```bash
# Attacker machine: disable local echo and enable raw keystrokes
stty raw -echo; fg
```
*(اضغط على Enter مرتين بعد تشغيل `fg` لاستعادة المطالبة)*

### الخطوة 5: إعادة ضبط وتعيين الأبعاد الطرفية
```bash
# Target shell: set terminal type and environment
reset
export TERM=xterm-256color
export SHELL=bash

# Check your attacker terminal size in another window:
# $ stty size  -> Output: 38 160

# Apply rows and columns to target shell:
stty rows 38 columns 160
```
> ✅ **النتيجة:** لديك الآن غلاف bash تفاعلي غير قابل للتدمير وكامل الميزات مع إكمال علامات التبويب وشاشة واضحة (`Ctrl+L`) ومعالجة الإشارة (لا يتم قطع اتصال `Ctrl+C`) وأبعاد طرفية كاملة!

---

## 🔧 استخدام rlwrap مع أدوات Pentest الأخرى

`rlwrap` لا يقتصر على Netcat. أي أداة CLI تفتقر إلى فوائد تحرير الأسطر منها:

### أوراكل SQL*Plus
```bash
# Oracle's sqlplus utility notoriously lacks line editing and history on Linux
rlwrap sqlplus scott/tiger@10.10.10.50:1521/ORCL
```
### ريديس وتيلنت
```bash
# Redis CLI interactive console
rlwrap redis-cli -h 10.10.10.50 -p 6379

# Raw Telnet or socket interaction
rlwrap telnet 10.10.10.50 25
```
### سوكات والمقابس المخصصة
```bash
# Socat listener with rlwrap
rlwrap socat file:`tty`,raw,echo=0 tcp-listen:4444

# Connecting to raw sockets / CTF pwn challenges
rlwrap nc ctf.challenge.local 1337
```
---

## ⚡ جدول مرجعي سريع

| مهمة | الأمر |
|------|---------|
| ** مستمع Netcat المحسن ** | `rlwrap nc -lvnp 4444` |
| ** الملف الكامل والإشارات الفورية ** | `rlwrap -cAr nc -lvnp 4444` |
| **ملف التاريخ المخصص** | `rlwrap -H ~/.nc_history nc -lvnp 4444` |
| **إكمال علامة التبويب المخصصة** | `rlwrap -f /path/to/wordlist.txt nc -lvnp 4444` |
| **زيادة حجم التاريخ** | `rlwrap -s 10000 nc -lvnp 4444` |
| ** التفاف أوراكل SQLPlus ** | `rlwrap sqlplus user/pass@host/db` |
| **التفاف Redis CLI** | `rlwrap redis-cli -h 10.10.10.50` |
| **لف مستمع سوكات** | `rlwrap socat TCP-LISTEN:4444,reuseaddr -` |

---

## 💡 النصائح وأفضل الممارسات

1. **إنشاء اسم مستعار لـ Bash:** أضف هذا إلى `~/.bashrc` أو `~/.zshrc` حتى لا تنسى استخدامه أبدًا:
   ```bash
   alias nc='rlwrap -cAr nc'
   ```
2. **لا تفوت سلامة `Ctrl+C` أبدًا:** عند اكتشاف الأصداف غير المستقرة، يعترض `rlwrap` مواطن الخلل الأساسية في لوحة المفاتيح، ولكن تذكر دائمًا إجراء ترقية `stty raw -echo; fg` المكونة من 5 خطوات إذا كنت تخطط لتشغيل أوامر تفاعلية طويلة.
3. **عزل سجلات العميل:** عند اختبار العديد من العملاء أو الأجهزة، استخدم دائمًا `-H ~/.client_name.hist` للاحتفاظ بمسارات تدقيق منفصلة وسجلات أوامر.

---

## ⚠️إخلاء المسؤولية القانونية

> **هام:** يجب استخدام الأصداف العكسية وأدوات ما بعد الاستغلال فقط في اختبارات الاختراق المصرح بها، وارتباطات الفريق الأحمر، وبيئات المختبرات التعليمية بموافقة كتابية صريحة.