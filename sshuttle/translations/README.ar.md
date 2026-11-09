<!-- language-home-navigation -->
[🏠 الصفحة الرئيسية العربية](../../README.ar.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🛰️ المكوك - ورقة الغش كاملة

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ███████╗███████╗██╗  ██╗██╗   ██╗████████╗████████╗██╗     ███████╗
  ██╔════╝██╔════╝██║  ██║██║   ██║╚══██╔══╝╚══██╔══╝██║     ██╔════╝
  ███████╗███████╗███████║██║   ██║   ██║      ██║   ██║     █████╗  
  ╚════██║╚════██║██╔══██║██║   ██║   ██║      ██║   ██║     ██╔══╝  
  ███████║███████║██║  ██║╚██████╔╝   ██║      ██║   ███████╗███████╗
  ╚══════╝╚══════╝╚═╝  ╚═╝ ╚═════╝    ╚═╝      ╚═╝   ╚══════╝╚══════╝
       Transparent Proxy Server that Works as a Poor Man's VPN over SSH
```
<p align="center">
  <img src="https://img.shields.io/badge/sshuttle-Poor_Man's_VPN-blue?style=for-the-badge" alt="sshuttle">
  <img src="https://img.shields.io/badge/Category-Pivoting_&_Tunneling-orange?style=for-the-badge" alt="التمحور وحفر الأنفاق">
  <img src="https://img.shields.io/badge/Protocols-TCP_DNS_SSH-green?style=for-the-badge" alt="البروتوكولات">
  <img src="https://img.shields.io/badge/License-LGPLv2.1-red?style=for-the-badge" alt="License LGPLv2.1">
</ص>

<p align="center">
  <b>🌐 إعادة توجيه طلبات الشبكات الفرعية الداخلية وطلبات DNS بالكامل من خلال جلسة SSH غير مميزة دون الوصول الإداري على الخادم البعيد</b>
</ص>

---

## 📋 جدول المحتويات

- [نظرة عامة وفلسفة "Poor Man's VPN"](#-نظرة عامة--فلسفة-poor-mans-vpn)
- [كيف يعمل المكوك](#-كيف يعمل المكوك)
- [مقارنة الأدوات: sshuttle vs SOCKS vs Ligolo-ng vs OpenVPN](#-tool-comparison-sshuttle-vs-socks-vs-ligolo-ng-vs-openvpn)
- [التثبيت](#-التثبيت)
- [توجيه الشبكة الفرعية الأساسية](#-توجيه الشبكة الفرعية الأساسية)
- [إعادة توجيه حركة مرور DNS (`--dns`)](#-dns-traffic-forwarding---dns)
- [إعادة توجيه البوابة بالكامل (المسار الافتراضي)](#-مسار إعادة توجيه البوابة بالكامل)
- [استبعادات الشبكة الفرعية (`-x`)](#-استبعادات الشبكة الفرعية--x)
- [خيارات SSH والمصادقة المستندة إلى المفتاح (`-e`)](#-ssh-options--key-based-authentication--e)
- [تنفيذ البرنامج الخفي في الخلفية (`-D`)](#-daemon-background-execution--d)
- [الجدول المرجعي السريع](#-الجدول المرجعي السريع)
- [الاعتبارات الدفاعية وOPSEC](#-الاعتبارات الدفاعية--opsec)
- [إخلاء المسؤولية القانونية](#-إخلاء المسؤولية القانونية)

---

## 🎯 نظرة عامة وفلسفة "Poor Man's VPN".

**sshuttle** هو خادم وكيل شفاف يعمل كشبكة VPN خفيفة الوزن وبدون تكوين عبر اتصال SSH قياسي.

في اختبارات الاختراق النموذجية للشركات وتقييمات الفريق الأحمر، يتم إعاقة التحول إلى الشبكات الداخلية بسبب قيود صارمة:
- ❌ نادرًا ما تمتلك حق الوصول إلى `root` أو `SYSTEM` على مضيف SSH الأساسي/المحوري.
- ❌ تتطلب حلول VPN التقليدية (OpenVPN، WireGuard) حقوقًا إدارية وأجهزة شبكة TUN/TAP افتراضية على كلا نقطتي النهاية.
- ❌ تتطلب وكلاء SOCKS القياسية (مثل `ssh -D`) تكوين `proxychains` أو إعدادات وكيل التطبيق الفردي لكل أداة على حدة.

**يحل المكوك جميع القيود الثلاثة في وقت واحد:**
- ✅ **لا يلزم وجود جذر عن بعد:** يحتاج الخادم المحوري فقط إلى تثبيت Python القياسي (`python3`).
- ✅ ** ليست هناك حاجة إلى TUN/TAP عن بعد: ** يستخدم تعدد إرسال دفق TCP النقي عبر جلسة SSH.
- ✅ ** التوجيه الشفاف على مستوى النظام: ** يعدل `iptables` / `nftables` المحلي في صندوق المهاجم، مما يتيح الأدوات (`nmap`، `curl`، `smbclient`، متصفحات الويب) لاستهداف عناوين IP الداخلية مباشرة دون أغلفة!

---

## ⚙️ كيف يعمل المكوك
```
[ Attacker Box (Local Root) ]
  ├── iptables / nftables redirects internal IP traffic (10.10.10.0/24) to local port
  └── sshuttle client multiplexes TCP packets over SSH tunnel
              │
              ▼ (Encrypted SSH Channel - Port 22)
[ Remote Pivot Server (Unprivileged User + Python) ]
  └── Assembles TCP connections and forwards them directly to internal hosts
              │
              ▼
[ Target Internal Network (10.10.10.0/24) ]
```
---

## ⚖️ مقارنة الأدوات: sshuttle vs SOCKS vs Ligolo-ng vs OpenVPN

| ميزة | `sshuttle` | جوارب (`ssh -D`) | `Ligolo-ng` | `OpenVPN` |
|---------|------------|------------------|-------------|----------|
| ** مطلوب الجذر البعيد ** | ❌ لا | ❌ لا | ❌ لا | ✅ نعم |
| **الجذر المحلي مطلوب** | ✅ نعم (iptables) | ❌ لا | ✅ نعم (واجهة TUN) | ✅ نعم |
| **توجيه شفاف** | ✅ نعم | ❌ يتطلب Proxychains | ✅ نعم (Native TUN) | ✅ نعم |
| **دعم حركة مرور UDP** | ❌ DNS فقط | ❌ لا | ✅ نعم | ✅ UDP كامل |
| ** يتطلب بايثون على الهدف **| ✅ نعم (بايثون 3) | ❌ لا | ❌ لا (وكيل مستقل)| ❌ لا |
| **Nmap SYN Scan (`-sS`)** | ❌ اتصال TCP (`-sT`)| ❌ اتصال TCP فقط | ✅ دعم الحزمة الخام | ✅ عبوات خام كاملة |

---

## 📥 التثبيت
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y sshuttle

# Arch Linux
sudo pacman -S sshuttle

# macOS (Homebrew)
brew install sshuttle

# Python Pipx / Pip
pipx install sshuttle
```
---

## 🚀 التوجيه الأساسي للشبكة الفرعية

قم بإعادة توجيه شبكة فرعية بعيدة من الفئة C بالكامل من خلال المضيف المحوري SSH:
```bash
# Route 10.10.10.0/24 through user@192.168.1.50
sshuttle -r user@192.168.1.50 10.10.10.0/24

# Route multiple disjoint subnets simultaneously
sshuttle -r user@192.168.1.50 10.10.10.0/24 172.16.0.0/16 192.168.100.0/24
```
بمجرد الاتصال، يمكنك التواصل مع الأجهزة الداخلية مباشرة:
```bash
# No proxychains needed!
curl http://10.10.10.25:8080/
smbclient -L //10.10.10.10/ -U 'domain/user%pass'
```
---

## 🌐 إعادة توجيه حركة مرور DNS (`--dns`)

إعادة توجيه استعلامات DNS الداخلية للشركة (`.corp`، `.local`) إلى خادم DNS الداخلي للشبكة البعيدة:
```bash
# Route subnet and proxy internal DNS queries
sshuttle --dns -r user@192.168.1.50 10.10.10.0/24

# Query internal domain directly via nslookup/dig
dig dc01.internal.corp
```
---

## 🌍 إعادة توجيه البوابة بالكامل (المسار الافتراضي)

قم بتوجيه كل حركة مرور المهاجم الصادرة عبر اتصال SSH (سلوك VPN الكامل):
```bash
# Route all traffic through remote SSH server (0.0.0.0/0)
sshuttle -r user@remote-vps.com 0.0.0.0/0

# Route all traffic except local network
sshuttle -r user@remote-vps.com 0.0.0.0/0 -x 192.168.1.0/24
```
---

## 🚫 استبعادات الشبكة الفرعية (`-x`)

منع التقاط شبكات فرعية حساسة أو استرجاعية معينة:
```bash
# Route entire 10.0.0.0/8 network, but exclude the pivot host itself
sshuttle -r user@10.0.0.50 10.0.0.0/8 -x 10.0.0.50/32
```
---

## 🔑 خيارات SSH والمصادقة المستندة إلى المفتاح (`-e`)

تمرير إشارات SSH المخصصة (مفاتيح الهوية الخاصة، منافذ SSH غير القياسية):
```bash
# Use custom identity file and non-standard SSH port 2222
sshuttle -e 'ssh -i /path/to/id_rsa -p 2222' -r user@192.168.1.50 10.10.10.0/24
```
---

## 🤖 تنفيذ البرنامج الخفي في الخلفية (`-D`)

قم بتشغيل sshuttle كبرنامج خفي في الخلفية أثناء المشاركات المستمرة:
```bash
# Start background daemon with pidfile
sudo sshuttle -D --pidfile /tmp/sshuttle.pid -r user@192.168.1.50 10.10.10.0/24

# Terminate daemon when finished
sudo kill $(cat /tmp/sshuttle.pid)
```
---

## ⚡ جدول مرجعي سريع

| الهدف | الأمر |
|-----------|--------|
| ** توجيه شبكة فرعية واحدة ** | `sshuttle -r <USER>@<PIVOT> <SUBNET>/24` |
| ** توجيه الشبكة الفرعية باستخدام DNS ** | `sshuttle --dns -r <USER>@<PIVOT> <SUBNET>/24` |
| ** توجيه شبكات فرعية متعددة ** | `sshuttle -r <USER>@<PIVOT> <NET1>/24 <NET2>/16` |
| **استبعاد IP/الشبكة الفرعية** | `sshuttle -r <USER>@<PIVOT> <SUBNET>/24 -x <EXCLUDE_IP>` |
| ** إعادة توجيه حركة المرور بالكامل ** | `sshuttle -r <USER>@<PIVOT> 0.0.0.0/0` |
| ** مفتاح / منفذ SSH مخصص ** | `sshuttle -e 'ssh -i <KEY> -p <PORT>' -r <USER>@<PIVOT> <SUBNET>` |
| ** تشغيل في الخلفية ** | `sshuttle -D --pidfile /tmp/sshuttle.pid -r <USER>@<PIVOT> <SUBNET>` |

---

## 🛡️ الاعتبارات الدفاعية وOPSEC

1. ** تعديل جدار الحماية: ** يعدل sshuttle جداول جدار الحماية المحلية (`iptables` على Linux، و`pf` على macOS). إذا تعطل المكوك بشكل غير نظيف، فاستخدم `iptables -F` أو أعد تشغيل خدمة جدار الحماية لمسح قواعد إعادة التوجيه التي لا معنى لها.
2. **حدود TCP فقط:** لا يتم دعم حزم UDP بخلاف DNS وطلبات صدى ICMP (ping) وعمليات فحص SYN. استخدم دائمًا `nmap -sT -Pn -n` لمسح المنافذ عبر المكوك.
3. **البصمة المستهدفة:** يقوم sshuttle تلقائيًا بتحميل برنامج نصي صغير لتجميع Python إلى المضيف المحوري البعيد أثناء تهيئة الجلسة وتشغيله عبر `python3`.

---

## ⚠️إخلاء المسؤولية القانونية

> [!تنبيه]
> ورقة الغش هذه مخصصة لاختبار الاختراق وإدارة النظام فقط. يعتبر التوجيه غير المصرح به واعتراض حركة مرور الشبكة أمرًا غير قانوني.