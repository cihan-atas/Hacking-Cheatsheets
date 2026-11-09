<!-- language-home-navigation -->
[🏠 الصفحة الرئيسية العربية](../../README.ar.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ⚡ Masscan - ورقة الغش كاملة

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ███╗   ███╗ █████╗ ███████╗███████╗ ██████╗ █████╗ ███╗   ██╗
  ████╗ ████║██╔══██╗██╔════╝██╔════╝██╔════╝██╔══██╗████╗  ██║
  ██╔████╔██║███████║███████╗███████╗██║     ███████║██╔██╗ ██║
  ██║╚██╔╝██║██╔══██║╚════██║╚════██║██║     ██╔══██║██║╚██╗██║
  ██║ ╚═╝ ██║██║  ██║███████║███████║╚██████╗██║  ██║██║ ╚████║
  ╚═╝     ╚═╝╚═╝  ╚═╝╚══════╝╚══════╝ ╚═════╝╚═╝  ╚═╝╚═╝  ╚═══╝
       The Fastest Internet Port Scanner - Asynchronous 10M Packets/Second
```
<p align="center">
  <img src="https://img.shields.io/badge/Masscan-Internet_Port_Scanner-blue?style=for-the-badge" alt="Masscan">
  <img src="https://img.shields.io/badge/Language-C-orange?style=for-the-badge" alt="اللغة C">
  <img src="https://img.shields.io/badge/Category-Recon_&_Scanning-green?style=for-the-badge" alt="Recon & Scanning">
  <img src="https://img.shields.io/badge/License-AGPLv3-red?style=for-the-badge" alt="License AGPLv3">
</ص>

<p align="center">
  <b>🌐 ماسح ضوئي غير متزامن عالي الأداء لمنفذ TCP قادر على فحص إنترنت IPv4 بالكامل في أقل من 5 دقائق</b>
</ص>

---

## 📋 جدول المحتويات

- [نظرة عامة والهندسة غير المتزامنة](#-نظرة عامة--الهندسة غير المتزامنة)
- [مقارنة الأدوات: Masscan vs Nmap vs RustScan](#-tool-comparison-masscan-vs-nmap-vs-rustscan)
- [التثبيت](#-التثبيت)
- [الاستخدام الأساسي وأهداف المسح](#-الاستخدام الأساسي--أهداف المسح)
- [التحكم في معدل الإرسال (`--rate`)](#-التحكم في معدل الإرسال---المعدل)
- [مواصفات المنفذ (TCP وUDP)](#-مواصفات المنفذ-tcp--udp)
- [التقاط الشعارات واختبار البروتوكول الأساسي](#-التقاط الشعارات--فحص البروتوكول الأساسي)
- [استبعادات الهدف والقوائم السوداء (`--excludefile`)](#-استبعادات الهدف--القوائم السوداء---استبعاد الملف)
- [تنسيقات الإخراج والأنابيب إلى Nmap](#-تنسيقات الإخراج--الأنابيب إلى nmap)
- [الجدول المرجعي السريع](#-الجدول المرجعي السريع)
- [الاعتبارات الدفاعية وحدود الأجهزة](#-الاعتبارات الدفاعية--حدود الأجهزة)
- [إخلاء المسؤولية القانونية](#-إخلاء المسؤولية القانونية)

---

## 🎯 نظرة عامة والهندسة غير المتزامنة

تم إنشاء **Masscan** بواسطة **Robert Graham**، ويُعتبر أسرع ماسح ضوئي للمنافذ في الوجود. وينتج نتائج مشابهة لـ Nmap، ولكنه يعمل داخليًا مثل `scanrand` و`unicornscan` و`ZMap`، باستخدام **الإرسال غير المتزامن**.

على عكس Nmap، الذي يستخدم مكدس TCP الخاص بنواة نظام التشغيل وينتظر الاستجابات قبل فحص المنافذ اللاحقة، فإن Masscan يدمج مكدس TCP/IP المخصص لمساحة المستخدم الخاصة به. يقوم بتفجير حزم SYN الخام بمعدلات تتجاوز **10 ملايين حزمة في الثانية** دون الحفاظ على جداول حالة الاتصال.

---

## ⚖️ مقارنة الأدوات: Masscan vs Nmap vs RustScan

| ميزة | ماسكان | نماب | روستسكان |
|---------|--------|------|----------|
| ** الحد الأقصى لمعدل الحزمة ** | 10,000,000 باكت/ثانية | ~1000 باكت/ثانية | حد مقبس نظام التشغيل |
| **فحص الإنترنت بالكامل (/0)**| ✅ أقل من 5 دقائق | ❌ أيام / أسابيع | ❌ غير مصمم لـ /0 |
| ** تنفيذ مكدس TCP **| مساحة مستخدم مخصصة Raw SYN| نواة نظام التشغيل / المقابس الأولية | مقابس طوكيو TCP غير المتزامنة |
| ** الإصدار العميق والبرامج النصية NSE **| ❌ لافتات بدائية | ✅ 600+ نصوص NSE | ✅ عبر Nmap handoff |
| **تتبع الحالة** | عديمي الجنسية (بلاك روك PRNG) | تجمع اتصال ذو حالة | مهام غير متزامنة |
| **النطاق المستهدف المثالي** | كتل الفئة A/B (/8، /16)| أهداف فردية و/24 شبكة فرعية | أجهزة فردية و/24 شبكة فرعية |

---

## 📥 التثبيت
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y masscan

# Arch Linux
sudo pacman -S masscan

# Build from source (Latest GitHub version)
sudo apt install -y git gcc make libpcap-dev
git clone https://github.com/robertdavidgraham/masscan
cd masscan && make -j$(nproc)
sudo make install
```
---

## 🚀 الاستخدام الأساسي وأهداف المسح
```bash
# Scan a single IP for top web ports
sudo masscan 10.10.10.50 -p80,443,8080 --rate 1000

# Scan an entire Class B internal subnet (172.16.0.0/16) for SMB port 445
sudo masscan 172.16.0.0/16 -p445 --rate 5000

# Scan all 65,535 ports on a specific IP
sudo masscan 10.10.10.50 -p1-65535 --rate 2000
```
> [!هام]
> نظرًا لأن Masscan ينشئ حزمًا أولية عبر `libpcap`، **يجب تشغيله بامتيازات الجذر / sudo**.

---

## ⚡ التحكم في معدل الإرسال (`--rate`)

تحدد المعلمة `--rate` عدد الحزم التي يرسلها Masscan في الثانية. تعد معايرة ذلك أمرًا بالغ الأهمية لتجنب تشبع معدات الشبكة:
```bash
# Gentle rate for home routers / WiFi (100–500 pkts/sec)
sudo masscan 10.10.10.0/24 -p1-1000 --rate 300

# Standard corporate gigabit internal network rate (5,000–10,000 pkts/sec)
sudo masscan 10.0.0.0/8 -p80,443 --rate 10000

# High-speed data center 10Gbps uplink rate (100,000+ pkts/sec)
sudo masscan 0.0.0.0/0 -p443 --rate 100000
```
---

## 🔌 مواصفات المنفذ (TCP وUDP)

يدعم Masscan كلاً من قوائم منافذ TCP وUDP في وقت واحد:
```bash
# Specific TCP ports
sudo masscan 10.10.10.50 -p21,22,80,443,445,3389

# Top ports shortcut (similar to Nmap)
sudo masscan 10.10.10.50 --top-ports 100

# UDP ports (prefixed with U:)
sudo masscan 10.10.10.50 -p U:53,U:161,U:123 --rate 500
```
---

## 🔍 الاستيلاء على الشعارات وفحص البروتوكول الأساسي

على الرغم من أن Masscan هو في المقام الأول ماسح ضوئي SYN، إلا أنه يمكنه إكمال المصافحة ثلاثية الاتجاهات للحصول على لافتات التطبيق:
```bash
# Grab HTTP, FTP, and SSH banners
sudo masscan 10.10.10.0/24 -p80,21,22 --banners --rate 1000
```
---

## 🚫 استبعادات الأهداف والقوائم السوداء (`--excludefile`)

عند إجراء اختبارات اختراق واسعة النطاق، استبعد دائمًا البنية التحتية الحيوية والعناوين المحظورة:
```bash
# Exclude government and military ranges or local gateway
sudo masscan 10.0.0.0/8 -p80 --rate 2000 --exclude 10.0.0.1,10.1.1.1

# Use an exclusion file (e.g., standard exclude.conf)
sudo masscan 192.168.0.0/16 -p445 --rate 2000 --excludefile /etc/masscan/exclude.conf
```
---

## 📊 تنسيقات الإخراج والأنابيب في Nmap

احفظ المضيفين المكتشفين بتنسيقات greppable أو JSON، ثم أدخلهم في Nmap لتقييم الثغرات الأمنية بالكامل:
```bash
# Save to JSON format
sudo masscan 10.10.10.0/24 -p80,443 -oJ masscan_results.json

# Save to simple List format (-oL)
sudo masscan 10.10.10.0/24 -p1-65535 --rate 2000 -oL open_ports.txt

# Extract discovered ports and feed directly to Nmap
cat open_ports.txt | awk '{print $4}' | sort -u | paste -sd, - | xargs -I{} nmap -sC -sV -p{} 10.10.10.50
```
---

## ⚡ جدول مرجعي سريع

| الهدف | الأمر |
|-----------|--------|
| ** مسح مضيف واحد (جميع المنافذ) ** | `sudo masscan <IP> -p1-65535 --rate 2000` |
| ** مسح شبكة CIDR الفرعية بحثًا عن المنفذ ** | `sudo masscan <SUBNET>/16 -p<PORT> --rate 5000` |
| ** المسح الضوئي باستخدام التقاط الشعارات ** | `sudo masscan <IP> -p80,22 --banners --rate 1000` |
| **مسح منافذ UDP** | `sudo masscan <IP> -p U:53,U:161 --rate 500` |
| **استبعاد عناوين IP** | `sudo masscan <NET> -p80 --exclude <IP1>,<IP2>` |
| **الإخراج إلى JSON** | `sudo masscan <IP> -p80,443 -oJ output.json` |
| **الإخراج إلى قائمة بسيطة** | `sudo masscan <IP> -p80,443 -oL output.txt` |

---

## 🛡️ الاعتبارات الدفاعية وحدود الأجهزة

1. **تعطل جهاز توجيه المستهلك:** سيؤدي تعيين `--rate 100000` على جهاز توجيه DSL/كابل منزلي أو شبكة Wi-Fi قياسية إلى إرباك جدول حالة تتبع اتصال NAT الخاص به وتعطل جهاز التوجيه. احتفظ بـ `--rate` أسفل `1000` على الاتصالات السكنية.
2. **استهلاك النطاق الترددي:** يستهلك الإرسال بمعدل 10 ملايين حزمة في الثانية حوالي 5.4 جيجابت في الثانية من النطاق الترددي للشبكة.
3. **رفض جدار الحماية المحلي (RST):** نظرًا لأن Masscan يستخدم مكدس TCP مخصصًا لمساحة المستخدم، فإن نواة Linux لديك لا تعرف شيئًا عن الاتصالات المفتوحة وقد ترسل حزم TCP RST تلقائية إلى الأهداف. لتجنب ذلك أثناء الاستيلاء على الشعار:
   ```bash
   sudo iptables -A INPUT -p tcp --dport 60000 -j DROP
   sudo masscan ... --source-port 60000
   ```
---

## ⚠️إخلاء المسؤولية القانونية

> [!تنبيه]
> يعد Masscan أداة مساعدة قوية للغاية لاستطلاع الشبكة. يمكن أن يؤدي فحص الشبكات غير المصرح بها أو نطاقات IP الحكومية أو شبكات الإنترنت الفرعية العامة إلى انتهاك قوانين الاتصالات والأمن السيبراني. تحقق دائمًا من التفويض الكتابي قبل بدء عمليات الفحص.