<!-- language-home-navigation -->
[🏠 الصفحة الرئيسية العربية](../../README.ar.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🕶️ mitmproxy - ورقة الغش كاملة

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ███╗   ███╗██╗████████╗███╗   ███╗██████╗ ██████╗  ██████╗ ██╗  ██╗██╗   ██╗
  ████╗ ████║██║╚══██╔══╝████╗ ████║██╔══██╗██╔══██╗██╔═══██╗╚██╗██╔╝╚██╗ ██╔╝
  ██╔████╔██║██║   ██║   ██╔████╔██║██████╔╝██████╔╝██║   ██║ ╚███╔╝  ╚████╔╝ 
  ██║╚██╔╝██║██║   ██║   ██║╚██╔╝██║██╔═══╝ ██╔══██╗██║   ██║ ██╔██╗   ╚██╔╝  
  ██║ ╚═╝ ██║██║   ██║   ██║ ╚═╝ ██║██║     ██║  ██║╚██████╔╝██╔╝ ██╗   ██║   
  ╚═╝     ╚═╝╚═╝   ╚═╝   ╚═╝     ╚═╝╚═╝     ╚═╝  ╚═╝ ╚═════╝ ╚═╝  ╚═╝   ╚═╝   
       Interactive, Scriptable Terminal HTTP/HTTPS Proxy & Traffic Engine
```
<p align="center">
  <img src="https://img.shields.io/badge/mitmproxy-Terminal_Proxy-blue?style=for-the-badge" alt="mitmproxy">
  <img src="https://img.shields.io/badge/Author-mitmproxy_Project-orange?style=for-the-badge" alt="mitmproxy">
  <img src="https://img.shields.io/badge/Category-Network_&_Web_Analogy-green?style=for-the-badge" alt="تحليل الشبكة">
  <img src="https://img.shields.io/badge/License-MIT-purple?style=for-the-badge" alt="MIT">
</ص>

<p align="center">
  <b>🕶️ وكيل HTTP تفاعلي قادر على اعتراض SSL/TLS لمختبري الاختراق والباحثين الأمنيين ومهندسي البرمجيات مع واجهة وحدة تحكم غنية وواجهة برمجة تطبيقات البرمجة النصية Python</b>
</ص>

---

## 📋 جدول المحتويات

- [نظرة عامة والثنائيات الثلاثة](#-نظرة عامة--الثنائيات الثلاثة)
- [التثبيت وإعداد الشهادة](#-التثبيت--إعداد الشهادة)
  - [التثبيت على Linux / macOS](#install-on-linux--macos)
  - [تثبيت شهادة CA لاعتراض SSL/TLS](#install-ca-certificate-for-ssltls-interception)
- [أوضاع التشغيل: عادي، شفاف، وكيل عكسي](#-أوضاع التشغيل-عادي-شفاف--عكس-وكيل)
- [ملاحة TUI التفاعلية (`mitmproxy`)](#-interactive-tui-navigation-mitmproxy)
  - [اختصارات لوحة المفاتيح](#keyboard-shortcuts)
  - [تصفية واعتراض التدفقات](#filtering--intercepting-flows)
- [الأتمتة غير التفاعلية (`mitmdump`)](#-الأتمتة غير التفاعلية-mitmdump)
  - [تسجيل وإعادة تشغيل حركة المرور](#تسجيل--إعادة تشغيل-حركة المرور)
- [برمجة بايثون والإضافات](#-برمجة بايثون--إضافات)
  - [البرنامج النصي لمعالجة الرأس الآلي](#automated-header-manipulation-script)
  - [التلاعب بالجسم من خلال الاستجابة المباشرة](#التلاعب بالجسم من خلال الاستجابة المباشرة)
- [الجدول المرجعي السريع](#-الجدول المرجعي السريع)
- [الاعتبارات الدفاعية والكشف](#-الاعتبارات الدفاعية--الكشف)
- [إخلاء المسؤولية القانونية](#-إخلاء المسؤولية القانونية)

---

## 🎯 نظرة عامة والثنائيات الثلاثة

على عكس وكلاء سطح المكتب الرسوميين الضخمين، يوفر **mitmproxy** اعتراضًا سريعًا للغاية وقابلاً للبرمجة وكامل الدقة لـ HTTP/1 وHTTP/2 وWebSockets مباشرة داخل المحطة الطرفية أو خط أنابيب الأتمتة.

يأتي مع ثلاث ثنائيات لنقطة الدخول:
1. **`mitmproxy`:** واجهة المستخدم الطرفية (TUI) التفاعلية القائمة على ncurses.
2. **`mitmdump`:** إصدار سطر الأوامر غير التفاعلي (ما يعادل `tcpdump` لـ HTTP).
3. **`mitmweb`:** واجهة ويب رسومية خفيفة الوزن تعتمد على المتصفح.

---

## 📥 التثبيت وإعداد الشهادة

### التثبيت على Linux / macOS
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y mitmproxy

# Arch Linux
sudo pacman -S mitmproxy

# Python pip / pipx
pipx install mitmproxy
```
### تثبيت شهادة CA لاعتراض SSL/TLS
لاعتراض حركة مرور HTTPS، يجب أن تثق الأهداف في المرجع المصدق الديناميكي لـ mitmproxy:
1. ابدأ تشغيل mitmproxy مرة واحدة: `mitmproxy` (ينشئ شهادات في `~/.mitmproxy/`).
2. على جهاز العميل/المتصفح، قم بتكوين الوكيل `127.0.0.1:8080`.
3. استعرض للوصول إلى `http://mitm.it` وقم بتنزيل/تثبيت الشهادة لنظام التشغيل Android، أو iOS، أو Windows، أو Linux.

---

## ⚙️ أوضاع التشغيل: الوكيل العادي والشفاف والعكس

### وضع الوكيل العادي (افتراضي)
الاستماع الوكيل القياسي للأمام على المنفذ 8080:
```bash
mitmproxy -p 8080
```
### وضع الوكيل العكسي
ينقل حركة المرور الواردة إلى خادم مستهدف محدد (مثالي لاختبار واجهة برمجة التطبيقات):
```bash
mitmproxy --mode reverse:https://api.target.com -p 8080
```
### الوضع الشفاف
يعترض حركة المرور الموجهة عبر iptables دون تكوين إعدادات وكيل العميل:
```bash
mitmproxy --mode transparent --showhost
```
---

## 🖥️ نظام ملاحة TUI التفاعلي (`mitmproxy`)

### اختصارات لوحة المفاتيح
- `?`: فتح تراكب المساعدة.
- `Enter`: فحص تدفق HTTP المحدد (الطلب / الاستجابة / التفاصيل).
- `q`: العودة / الخروج.
- `d`: حذف التدفق الحالي.
- `r`: إعادة تشغيل الطلب المحدد إلى الخادم.
- `e`: تحرير رؤوس الطلب/الاستجابة أو المسار أو النص.
- `a`: استئناف التدفق المتوقف مؤقتًا (عند الاعتراض).
- `A`: استئناف جميع التدفقات المتوقفة مؤقتًا.

### تصفية واعتراض التدفقات
اضغط على `i` لإدخال تعبير مرشح الاعتراض:
```text
~u /api/v1/ & ~m POST      # Intercept only POST requests to /api/v1/
~c 401 | ~c 403            # Filter flows by HTTP status code
~b "password"              # Filter flows where body contains "password"
```
---

## ⚡ الأتمتة غير التفاعلية (`mitmdump`)

### تسجيل وإعادة تشغيل حركة المرور
التقاط حركة المرور المباشرة مباشرة في ملف تفريغ ثنائي:
```bash
# Record all HTTP traffic to file
mitmdump -w traffic.dump

# Read and inspect captured dump
mitmdump -n -r traffic.dump

# Replay client requests from dump against target
mitmdump --client-replay traffic.dump
```
---

## 🐍 البرمجة النصية والإضافات بلغة بايثون

يتميز mitmproxy بواجهة برمجة تطبيقات Python الأصلية لتعديل حركة المرور برمجيًا:

### البرنامج النصي لمعالجة الرأس تلقائيًا (`add_headers.py`)
```python
from mitmproxy import http

class AddHeader:
    def request(self, flow: http.HTTPFlow):
        # Inject custom authorization or testing header into every outbound request
        flow.request.headers["X-Security-Audit"] = "Authorized-Pentest"
        flow.request.headers["Authorization"] = "Bearer injected_token_xyz"

addons = [AddHeader()]
```
تشغيل مع:
```bash
mitmproxy -s add_headers.py
```
### التلاعب بجسم الاستجابة المباشرة (`tamper_response.py`)
```python
from mitmproxy import http

def response(flow: http.HTTPFlow):
    # Force admin privilege in returned JSON response
    if "api/user/me" in flow.request.url:
        flow.response.text = flow.response.text.replace('"isAdmin": false', '"isAdmin": true')
```
تشغيل مع:
```bash
mitmdump -s tamper_response.py
```
---

## ⚡ جدول مرجعي سريع

| الهدف | الأمر |
|-----------|--------|
| **بدء TUI التفاعلي** | `mitmproxy -p 8080` |
| **بدء تشغيل واجهة المستخدم الرسومية للويب** | `mitmweb -p 8080` |
| ** مسجل CLI الصامت ** | `mitmdump -p 8080` |
| ** عكس هدف الوكيل ** | `mitmproxy --mode reverse:https://target.com` |
| ** سجل حركة المرور إلى الملف ** | `mitmdump -w capture.mitm` |
| ** تحميل البرنامج النصي الإضافي لبايثون **| `mitmproxy -s script.py` |
| **تجاهل المضيفين المستهدفين** | `mitmproxy --ignore-hosts ".*apple\.com"` |

---

## 🛡️ الاعتبارات الدفاعية والكشوفات

1. **تثبيت الشهادة:** ترفض تطبيقات الهاتف المحمول التي تفرض تثبيت SSL شهادة CA الخاصة بـ mitmproxy. الاقتران مع فريدا / الاعتراض على تجاوز عمليات التحقق من جانب العميل.
2. ** اكتشاف الوكيل في TLS Handshake: ** يقدم الوكلاء مجموعات تشفير TLS مخصصة وبصمات تفاوض ALPN التي يمكن لـ WAFs ومديري الروبوتات اكتشافها.

---

## ⚠️إخلاء المسؤولية القانونية

> [!تنبيه]
> mitmproxy يعترض ويفك تشفير اتصالات الشبكة الحساسة. ويجب أن يتم نشره بشكل صارم على الأنظمة والشبكات التي تم الحصول على ترخيص صريح منها. اعتراض الاتصالات دون موافقة أمر غير قانوني.