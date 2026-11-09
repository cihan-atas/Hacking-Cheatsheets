<!-- language-home-navigation -->
[🏠 الصفحة الرئيسية العربية](../../README.ar.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🦀 RustScan - ورقة الغش الكاملة

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ██████╗ ██╗   ██╗███████╗████████╗███████╗ ██████╗ █████╗ ███╗   ██╗
  ██╔══██╗██║   ██║██╔════╝╚══██╔══╝██╔════╝██╔════╝██╔══██╗████╗  ██║
  ██████╔╝██║   ██║███████╗   ██║   ███████╗██║     ███████║██╔██╗ ██║
  ██╔══██╗██║   ██║╚════██║   ██║   ╚════██║██║     ██╔══██║██║╚██╗██║
  ██║  ██║╚██████╔╝███████║   ██║   ███████║╚██████╗██║  ██║██║ ╚████║
  ╚═╝  ╚═╝ ╚═════╝ ╚══════╝   ╚═╝   ╚══════╝ ╚═════╝╚═╝  ╚═╝╚═╝  ╚═══╝
       The Modern Port Scanner - Scan All 65k Ports in 3 Seconds into Nmap
```
<p align="center">
  <img src="https://img.shields.io/badge/RustScan-Port_Scanner-blue?style=for-the-badge" alt="RustScan">
  <img src="https://img.shields.io/badge/Language-Rust-orange?style=for-the-badge" alt="Language Rust">
  <img src="https://img.shields.io/badge/Category-Recon_&_Scanning-green?style=for-the-badge" alt="Recon & Scanning">
  <img src="https://img.shields.io/badge/License-GPLv3-red?style=for-the-badge" alt="License GPLv3">
</ص>

<p align="center">
  <b>🌐 ماسح ضوئي سريع وقابل للتخصيص وآلي للمنافذ مصمم لتوجيه المنافذ المفتوحة المكتشفة مباشرةً إلى البرامج النصية لـ Nmap</b>
</ص>

---

## 📋 جدول المحتويات

- [نظرة عامة والهندسة المعمارية](#-نظرة عامة--الهندسة المعمارية)
- [مقارنة الأدوات: RustScan vs Nmap vs Masscan](#-tool-comparison-rustscan-vs-nmap-vs-masscan)
- [التثبيت](#-التثبيت)
- [الاستخدام الأساسي وفحص المضيف الفردي](#-الاستخدام الأساسي--المسح الضوئي للمضيف الفردي)
- [تكامل Nmap الآلي](#-تكامل nmap الآلي)
- [ضبط الأداء (`ulimit` وحجم الدفعة)](#-ضبط-الأداء-حدود--حجم الدفعة)
- [نطاقات منافذ المسح وشبكات CIDR الفرعية](#-scanning-port-ranges--cidr-subnets)
- [البرمجة وتنسيق الإخراج](#-البرمجة النصية--تنسيق الإخراج)
- [الجدول المرجعي السريع](#-الجدول المرجعي السريع)
- [الاعتبارات الدفاعية وOPSEC](#-الاعتبارات الدفاعية--opsec)
- [إخلاء المسؤولية القانونية](#-إخلاء المسؤولية القانونية)

---

## 🎯 نظرة عامة والهندسة المعمارية

**RustScan** عبارة عن ماسح ضوئي حديث وعالي السرعة للمنافذ تم تصميمه باستخدام **Rust**. فلسفتها الأساسية بسيطة: **فحص المنافذ بسرعة مذهلة، والعثور على المنافذ المفتوحة عبر نطاق 1-65535 بالكامل في 3 ثوانٍ، وتسليم تلك المنافذ المفتوحة على الفور إلى Nmap للحصول على خدمة شاملة وفحص الثغرات الأمنية**.

بدلاً من الانتظار لمدة 10-20 دقيقة حتى يكمل `nmap -p-` عمليات مسح مصافحة TCP المتسلسلة، يستفيد RustScan من تعدد الخيوط غير المتزامنة وتجميع مقابس نظام التشغيل لتحديد المنافذ المفتوحة بشكل فوري تقريبًا.

---

## ⚖️ مقارنة الأدوات: RustScan vs Nmap vs Masscan

| ميزة | روستسكان | نماب | ماسكان |
|---------|---------|------|---------|
| **الهدف الأساسي** | اكتشاف سريع -> أنبوب Nmap | الخدمة العميقة والبرامج النصية NSE | مسح SYN عالي السرعة على مستوى الإنترنت |
| **سرعة جميع المنافذ (1-65535)**| ⭐⭐⭐⭐⭐ (~3 ثواني) | ⭐⭐ (5 – 20 دقيقة) | ⭐⭐⭐⭐⭐ (~2 ثانية) |
| **الكشف عن إصدار الخدمة**| عبر أنبوب Nmap | أصلي (`-sV`) | ❌ الحد الأدنى |
| ** محرك البرنامج النصي NSE ** | عبر أنبوب Nmap | أصلي (`-sC`، `--script`) | ❌ لا |
| **محرك الحزمة** | مآخذ TCP غير المتزامنة | IP الخام / مكدس TCP المخصص | مكدس SYN غير المتزامن المخصص |
| **سهولة الأتمتة** | وسيطات CLI الأصلية (`--`) | خيارات CLI واسعة النطاق | تعقيد عالي |

---

## 📥 التثبيت
```bash
# Debian / Ubuntu / Kali Linux (.deb package)
wget https://github.com/RustScan/RustScan/releases/download/2.3.0/rustscan_2.3.0_amd64.deb
sudo dpkg -i rustscan_2.3.0_amd64.deb

# Arch Linux (AUR)
yay -S rustscan

# Cargo (Rust Package Manager)
cargo install rustscan

# Docker Container
docker run -it --rm --name rustscan rustscan/rustscan:2.1.1 -a 10.10.10.50
```
---

## 🚀 الاستخدام الأساسي والمسح الضوئي للمضيف الفردي
```bash
# Scan all 65,535 ports on a single IP
rustscan -a 10.10.10.50

# Scan with greppable one-line output
rustscan -a 10.10.10.50 --greppable

# Quiet mode (suppresses the ASCII banner)
rustscan -q -a 10.10.10.50
```
---

## 🔗 التكامل الآلي مع Nmap

أقوى ميزة في RustScan هي تمرير المنافذ المفتوحة مباشرة إلى Nmap باستخدام الفاصل `--` اللاحق:
```bash
# Scan target and execute default safe scripts & version detection on open ports
rustscan -a 10.10.10.50 -- -sC -sV

# Pass custom Nmap NSE scripts and output options
rustscan -a 10.10.10.50 -- -sC -sV -oN nmap_report.txt

# Run vulnerability audit with Nmap Vuln scripts
rustscan -a 10.10.10.50 -- --script vuln -oA target_vulns
```
---

## ⚡ ضبط الأداء (`ulimit` وحجم الدفعة)

يعتمد RustScan على حدود واصف الملفات لنظام التشغيل الخاص بك. افتراضيًا، يتم ضبطه تلقائيًا، لكن الضبط اليدوي يوفر أعلى أداء:
```bash
# Increase batch size and socket ulimit for high-speed local networks
rustscan -a 10.10.10.50 -b 5000 -u 5000

# Lower batch size for high-latency or packet-dropping WiFi networks
rustscan -a 10.10.10.50 -b 500 --scan-order "random"
```
---

## 🌐 مسح نطاقات المنافذ وشبكات CIDR الفرعية
```bash
# Scan a specific port range instead of all ports
rustscan -a 10.10.10.50 -r 1-1000

# Scan multiple IP addresses
rustscan -a 10.10.10.50,10.10.10.51

# Scan an entire CIDR subnet range
rustscan -a 192.168.1.0/24
```
---

## 📊 البرمجة النصية وتنسيق الإخراج
```bash
# Output discovered open ports in JSON format
rustscan -a 10.10.10.50 --accessible

# Read target hosts from a file
rustscan -a 'hosts.txt'
```
---

## ⚡ جدول مرجعي سريع

| الهدف | الأمر |
|-----------|--------|
| ** مسح كافة المنافذ 65 ألف ** | `rustscan -a <IP>` |
| ** مسح كافة المنافذ + البرامج النصية Nmap **| `rustscan -a <IP> -- -sC -sV` |
| ** مسح شبكة CIDR الفرعية ** | `rustscan -a <SUBNET>/24` |
| **نطاق منفذ المسح** | `rustscan -a <IP> -r 1-1000` |
| ** ضبط حجم الدفعة والحدود ** | `rustscan -a <IP> -b 3000 -u 3000` |
| ** الوضع الهادئ (بدون لافتة) ** | `rustscan -q -a <IP>` |
| ** إخراج قابل للسحب ** | `rustscan -a <IP> --greppable` |
| **تصدير إلى ملف Nmap** | `rustscan -a <IP> -- -sC -sV -oN nmap.txt` |

---

## 🛡️ الاعتبارات الدفاعية وOPSEC

1. **تنبيهات هائلة للضوضاء ومعرفات الهوية:** يؤدي مسح منافذ 65 ألفًا في 3 ثوانٍ إلى توليد الآلاف من حزم TCP SYN على الفور. سيؤدي ذلك إلى تشغيل تحديد معدل Snort وSuricata وجدار الحماية على الفور.
2. **تدفق المزامنة والحزم المسقطة:** في روابط VPN غير المستقرة (على سبيل المثال، HackTheBox، وTryHackMe)، يمكن أن تتسبب أحجام الدُفعات الكبيرة (`-b 4000`) في فقدان الحزمة. إذا بدت النتائج غير متناسقة، فقم بخفض حجم الدفعة إلى `-b 1000`.

---

## ⚠️إخلاء المسؤولية القانونية

> [!تنبيه]
> ورقة الغش هذه مخصصة لاختبارات الاختراق والبحث التعليمي فقط. يمنع منعا باتا مسح الشبكات دون إذن.