<!-- language-home-navigation -->
[🏠 الصفحة الرئيسية العربية](../../README.ar.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ⚡ نابو - ورقة الغش الكاملة

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ███╗   ██╗ █████╗  █████╗ ██████╗ ██╗   ██╗
  ████╗  ██║██╔══██╗██╔══██╗██╔══██╗██║   ██║
  ██╔██╗ ██║███████║███████║██████╔╝██║   ██║
  ██║╚██╗██║██╔══██║██╔══██║██╔══██╗██║   ██║
  ██║ ╚████║██║  ██║██║  ██║██████╔╝╚██████╔╝
  ╚═╝  ╚═══╝╚═╝  ╚═╝╚═╝  ╚═╝╚═════╝  ╚═════╝ 
        Fast & Reliable SYN/CONNECT Port Scanner
```
<p align="center">
  <img src="https://img.shields.io/badge/Naabu-Port_Scanner-blue?style=for-the-badge" alt="Naabu">
  <img src="https://img.shields.io/badge/Author-ProjectDiscovery-orange?style=for-the-badge" alt="ProjectDiscovery">
  <img src="https://img.shields.io/badge/Category-Recon_&_Scanning-green?style=for-the-badge" alt="Recon & Scanning">
  <img src="https://img.shields.io/badge/Language-Go-cyan?style=for-the-badge" alt="Go">
</ص>

<p align="center">
  <b>⚡ ماسح ضوئي سريع وموثوق للمنافذ يركز على البساطة والتوصيل السلس باستخدام أدوات ProjectDiscovery (httpx، النواة)</b>
</ص>

---

## 📋 جدول المحتويات

- [نظرة عامة والميزات الرئيسية](#-نظرة عامة--الميزات الرئيسية)
- [التثبيت](#-التثبيت)
- [مسح المنفذ الأساسي](#-مسح المنفذ الأساسي)
  - [فحص المضيف الفردي](#single-host-scan)
  - [أعلى المنافذ ونطاق المنافذ الكامل](#top-ports--full-port-range)
- [أوضاع المسح: SYN (مقبس خام) مقابل CONNECT](#-أوضاع المسح-syn-raw-socket-vs-connect)
- [تحديد المعدل وضبط الأداء](#-تحديد المعدل--ضبط الأداء)
- [المسح متعدد الأهداف وكتل CIDR](#-المسح متعدد الأهداف--كتل cidr)
- [سير عمل خطوط الأنابيب السلسة](#-seamless-pipeline-workflows)
  - [نابو + Httpx + نوى](#naabu--httpx--nuclei)
  - [Subfinder + Naabu](#subfinder--naabu)
- [تكامل Nmap (`-nmap`)](#-nmap-integration--nmap)
- [الجدول المرجعي السريع](#-الجدول المرجعي السريع)
- [الاعتبارات الدفاعية والكشف](#-الاعتبارات الدفاعية--الكشف)
- [إخلاء المسؤولية القانونية](#-إخلاء المسؤولية القانونية)

---

## 🎯 نظرة عامة والميزات الرئيسية

تم تطوير **Naabu** بواسطة **ProjectDiscovery**، وهو عبارة عن ماسح ضوئي للمنافذ مصمم ليكون سريعًا ودقيقًا ومُحسّنًا للتكامل مع خطوط أنابيب أتمتة الاستطلاع الحديثة:
- **فحص SYN & CONNECT:** الرجوع التلقائي إلى TCP CONNECT عند غياب امتيازات المقبس الأولية (`libpcap` / root).
- **نظام ProjectDiscovery البيئي:** بنية خطوط أنابيب أصلية تسمح بتوصيل الأنابيب بشكل سلس من `subfinder` إلى `naabu`، ثم إلى `httpx` و`nuclei`.
- **محرك Nmap المتكامل:** القدرة على تشغيل خدمة Nmap النشطة وفحص الإصدارات على المنافذ المفتوحة المكتشفة تلقائيًا.
- **استبعاد CDN:** يتجاوز تلقائيًا عقد حافة CDN السحابية (Cloudflare وAkamai وCloudFront) لمنع إهدار النطاق الترددي والإيجابيات الكاذبة.

---

## 📥 التثبيت
```bash
# Using Go (Recommended)
go install -v github.com/projectdiscovery/naabu/v2/cmd/naabu@latest

# Binary download via pdtm (ProjectDiscovery Tool Manager)
pdtm -i naabu

# Arch Linux (AUR)
yay -S naabu-bin

# Kali Linux
sudo apt update && sudo apt install -y naabu
```
> [!ملاحظة]
> في Linux، يتيح منح `CAP_NET_RAW` تشغيل عمليات فحص SYN بدون جذر:
> `sudo setcap cap_net_raw=+ep $(which naabu)`

---

## 🚀 المسح الأساسي للمنافذ

### فحص مضيف واحد
```bash
# Scan default top 100 ports
naabu -host example.com

# Scan specific ports
naabu -host example.com -p 80,443,8080,8443
```
### أفضل المنافذ ونطاق المنافذ الكامل
```bash
# Scan top 1000 ports
naabu -host example.com -top-ports 1000

# Full port scan (1-65535)
naabu -host example.com -p -
```
---

## ⚡ أوضاع المسح: SYN (مقبس خام) مقابل CONNECT

- **مسح SYN (`-s syn` / الجذر مطلوب):** يرسل حزم TCP SYN الأولية دون إنهاء المصافحة ثلاثية الاتجاهات (مسح نصف مفتوح). أسرع بكثير وأقل ضوضاء في سجلات التطبيق.
- ** CONNECT Scan (`-s conn`): ** يستخدم استدعاء مقبس OS `connect()` القياسي. يعمل بدون امتيازات الجذر ويخترق البيئات الافتراضية المقيدة.
```bash
# Explicit SYN Scan (Requires sudo or CAP_NET_RAW)
sudo naabu -host example.com -s syn

# Explicit TCP Connect Scan (No root required)
naabu -host example.com -s conn
```
---

## ⚙️ تحديد المعدل وضبط الأداء
```bash
# Adjust packet rate per second (default: 1000)
sudo naabu -host example.com -rate 2500

# Set connection timeout in milliseconds
naabu -host example.com -timeout 1000

# Exclude CDN edge servers (Cloudflare, CloudFront, Fastly)
naabu -host example.com -exclude-cdn
```
---

## 🌐 المسح متعدد الأهداف وكتل CIDR
```bash
# Scan targets from list
naabu -list targets.txt -p 80,443,8080 -o open_ports.txt

# Scan a /24 subnet block
sudo naabu -host 192.168.1.0/24 -p 22,80,443,445,3389
```
---

## 🔄 سير عمل سلس لخطوط الأنابيب

يتألق Naabu عند توصيله بأدوات ProjectDiscovery الأخرى:

### مكتشف فرعي + نابو
قم بتعداد النطاقات الفرعية واكتشف على الفور أي منها يحتوي على منافذ ويب/SSH مفتوحة:
```bash
subfinder -d example.com -silent | naabu -silent -p 80,443,8080,8443
```
### نابو + هتبكس + نوى
خط أنابيب استطلاع مكافأة الأخطاء الهجومية الأساسي:
```bash
# 1. Port scan subdomains -> 2. Probe HTTP services -> 3. Vulnerability scan
subfinder -d target.com -silent \
  | naabu -silent -p 80,443,8080,8443 \
  | httpx -silent \
  | nuclei -severity medium,high,critical -o findings.txt
```
---

## 🔍 تكامل Nmap (`-nmap`)

قم بتمرير المنافذ المفتوحة المكتشفة مباشرةً إلى Nmap لاكتشاف الخدمة (`-sV`) وفحص البرنامج النصي (`-sC`):
```bash
sudo naabu -host target.local -top-ports 1000 -nmap-cli 'nmap -sV -sC -oN naabu_nmap.txt'
```
---

## ⚡ جدول مرجعي سريع

| الهدف | الأمر |
|-----------|--------|
| ** المسح الافتراضي (أعلى 100) ** | `naabu -host <TARGET>` |
| **مسح كامل المدى (1-65535)**| `naabu -host <TARGET> -p -` |
| **أفضل 1000 منفذ** | `naabu -host <TARGET> -top-ports 1000` |
| **مسح SYN (سريع، خفي)**| `sudo naabu -host <TARGET> -s syn` |
| **استبعاد عناوين IP الخاصة بـ CDN Edge** | `naabu -host <TARGET> -exclude-cdn` |
| **الإخراج إلى JSON** | `naabu -host <TARGET> -json -o results.json` |
| **التوجيه إلى HTTPX** | `naabu -host <TARGET> -silent \| httpx` |
| ** تشغيل فحص خدمة Nmap **| `sudo naabu -host <TARGET> -nmap-cli 'nmap -sV'` |

---

## 🛡️ الاعتبارات الدفاعية والكشوفات

1. ** جدار الحماية وقواعد IDS: ** تنتج عمليات مسح SYN عالية المعدل التي تم إنشاؤها بواسطة Naabu نسب TCP RST/SYN عالية يمكن اكتشافها بسهولة بواسطة قواعد Snort/Suricata.
2. ** تحديد معدل الشبكة: ** فرض تحديد معدل الاتصال الداخلي (`iptables -m limit --limit 50/s`) لإبطاء وتعطيل المسح الآلي.

---

## ⚠️إخلاء المسؤولية القانونية

> [!تنبيه]
> تم تطوير Naabu بشكل صارم للتقييمات الأمنية المعتمدة واختبار الاختراق واكتشاف الأصول الدفاعية. يُحظر تمامًا فحص الشبكات دون الحصول على إذن صريح.