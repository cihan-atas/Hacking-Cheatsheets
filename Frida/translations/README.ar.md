<!-- language-home-navigation -->
[🏠 الصفحة الرئيسية العربية](../../README.ar.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 📱 فريدا والاعتراض - ورقة الغش الكاملة

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ███████╗██████╗ ██╗██████╗  █████╗      ██████╗ ██████╗      ██╗███████╗ ██████╗████████╗██╗ ██████╗ ███╗   ██╗
  ██╔════╝██╔══██╗██║██╔══██╗██╔══██╗    ██╔═══██╗██╔══██╗     ██║██╔════╝██╔════╝╚══██╔══╝██║██╔═══██╗████╗  ██║
  █████╗  ██████╔╝██║██║  ██║███████║    ██║   ██║██████╔╝     ██║█████╗  ██║        ██║   ██║██║   ██║██╔██╗ ██║
  ██╔══╝  ██╔══██╗██║██║  ██║██╔══██║    ██║   ██║██╔══██╗██   ██║██╔══╝  ██║        ██║   ██║██║   ██║██║╚██╗██║
  ██║     ██║  ██║██║██████╔╝██║  ██║    ╚██████╔╝██████╔╝╚█████╔╝███████╗╚██████╗   ██║   ██║╚██████╔╝██║ ╚████║
  ╚═╝     ╚═╝  ╚═╝╚═╝╚═════╝ ╚═╝  ╚═╝     ╚═════╝ ╚═════╝  ╚════╝ ╚══════╝ ╚═════╝   ╚═╝   ╚═╝ ╚═════╝ ╚═╝  ╚═══╝
           Dynamic Binary Instrumentation & Mobile Security Assessment
```
<p align="center">
  <img src="https://img.shields.io/badge/Frida-Runtime_Hooking-blue?style=for-the-badge" alt="فريدا">
  <img src="https://img.shields.io/badge/Objection-Mobile_Exploration-orange?style=for-the-badge" alt="Objection">
  <img src="https://img.shields.io/badge/Platforms-Android_%7C_iOS-green?style=for-the-badge" alt="Android | iOS">
  <img src="https://img.shields.io/badge/Category-Mobile_Security-red?style=for-the-badge" alt="Mobile Security">
</ص>

<p align="center">
  <b>📱 مجموعة أدوات الأدوات الثنائية الديناميكية المتوافقة مع معايير الصناعة لربط أساليب التشغيل في نظامي التشغيل Android وiOS، وتجاوز تثبيت SSL، والتهرب من الجذر/كسر الحماية، وتحليل الذاكرة</b>
</ص>

---

## 📋 جدول المحتويات

- [نظرة عامة والهندسة المعمارية](#-نظرة عامة--الهندسة المعمارية)
- [التثبيت وإعداد الجهاز](#-التثبيت--إعداد الجهاز)
  - [تثبيت أدوات المضيف](#host-tools-installation)
  - [إعداد جهاز Android (`frida-server`)](#android-device-setup-frida-server)
  - [إعداد جهاز iOS](#ios-device-setup)
- [أساسيات فريدا CLI](#-frida-cli-fundamentals)
  - [إدراج الأجهزة والعمليات الجارية](#listing-devices and-running-processes)
  - [الإرفاق مقابل النشر](#الإرفاق مقابل النشر)
- [برامج ربط جافا سكريبت](#-javascript-hooking-scripts)
  - [تجاوز تثبيت SSL لنظام Android](#bypassing-android-ssl-pinning)
  - [تجاوز اكتشاف الجذر](#bypassing-root-detection)
  - [اعتراض طريقة جافا والتلاعب بالوسيطة](#java-method-interception--argument-tampering)
- [مجموعة أدوات وقت تشغيل الاعتراض](#-مجموعة أدوات وقت تشغيل الاعتراض)
  - [بدء جلسة الاعتراض](#starting-an-objection-session)
  - [تثبيت SSL بأمر واحد وتجاوز الجذر](#one-command-ssl-pinning--root-bypass)
  - [استكشاف Keystore وSQLite والتفضيلات المشتركة](#exploring-keystore-sqlite-and-shared-preferences)
  - [فصول الربط وأنشطة الإدراج](#فصول-الربط وأنشطة الإدراج)
- [الجدول المرجعي السريع](#-الجدول المرجعي السريع)
- [الاعتبارات الدفاعية ومكافحة فريدا](#-الاعتبارات الدفاعية--مكافحة فريدا)
- [إخلاء المسؤولية القانونية](#-إخلاء المسؤولية القانونية)

---

## 🎯 نظرة عامة والهندسة المعمارية

تعمل **Frida** على إدخال وقت تشغيل Google V8 JavaScript في العمليات المستهدفة على أنظمة Android وiOS وWindows وmacOS وLinux. إنه يمكّن الباحثين الأمنيين من:
- ربط الوظائف الأصلية (`libc`، OpenSSL) والوظائف المُدارة (`Java`، `Objective-C`، `Swift`) بشكل عشوائي.
- فحص وتعديل الذاكرة ومعلمات الوظيفة على الطاير.
- تتبع واجهات برمجة التطبيقات المشفرة وفك تشفير حركة المرور المشفرة في الذاكرة.

**الاعتراض** هو إطار عمل لتقييم وقت تشغيل أمان الهاتف المحمول مدعوم من Frida والذي يعمل على أتمتة مهام اختبار الاختراق الشائعة على الهاتف المحمول (تعطيل تثبيت الشهادة، وتفريغ الذاكرة، وتجاوز المصادقة البيومترية) دون الحاجة إلى كود JavaScript يدوي.

---

## 📥 التثبيت وإعداد الجهاز

### تثبيت أدوات المضيف
```bash
pip install frida frida-tools objection
```
### إعداد جهاز Android (`frida-server`)
قم بتنزيل `frida-server` المطابق لإصدار Frida المضيف الخاص بك وبنية وحدة المعالجة المركزية للجهاز (x86_64، وarm64، وما إلى ذلك):
```bash
# Verify architecture
adb shell getprop ro.product.cpu.abi

# Push and run frida-server on rooted Android device
unxz frida-server-*-android-arm64.xz
adb root
adb push frida-server-*-android-arm64 /data/local/tmp/frida-server
adb shell "chmod 755 /data/local/tmp/frida-server"
adb shell "/data/local/tmp/frida-server &"
```
### إعداد جهاز iOS
على جهاز iOS مكسور الحماية، أضف مستودع Frida في Cydia / Sileo (`https://build.frida.re`) وقم بتثبيت الحزمة `Frida`.

---

## 🚀 أساسيات فريدا CLI

### قائمة الأجهزة والعمليات الجارية
```bash
# List all connected USB devices
frida-ls-devices

# List all running applications on USB-connected Android/iOS
frida-ps -Uai

# List all system processes on USB device
frida-ps -U
```
### الإرفاق مقابل النشر
- **إرفاق (`-n` أو `-p`):** يتم إرفاقه بتطبيق قيد التشغيل بالفعل.
- **Spawn (`-f`):** يبدأ التطبيق من التمهيد المبكر، ويوقف التنفيذ مؤقتًا قبل تنفيذ المُنشئين الأوليين:
```bash
# Attach to running process
frida -U -n com.example.app -l hook.js

# Spawn process with early instrumentation
frida -U -f com.example.app -l hook.js
```
---

## 💻 نصوص ربط جافا سكريبت

### تجاوز تثبيت SSL لنظام Android
البرنامج النصي لتجاوز TrustManager العالمي (`ssl_bypass.js`):
```javascript
Java.perform(function() {
    var X509TrustManager = Java.use('javax.net.ssl.X509TrustManager');
    var SSLContext = Java.use('javax.net.ssl.SSLContext');

    var TrustManager = Java.registerClass({
        name: 'com.custom.TrustManager',
        implements: [X509TrustManager],
        methods: {
            checkClientTrusted: function(chain, authType) {},
            checkServerTrusted: function(chain, authType) {},
            getAcceptedIssuers: function() { return []; }
        }
    });

    var TrustManagers = [TrustManager.$new()];
    var SSLContext_init = SSLContext.init.overload(
        '[Ljavax.net.ssl.KeyManager;', '[Ljavax.net.ssl.TrustManager;', 'java.security.SecureRandom');
    
    SSLContext_init.implementation = function(km, tm, random) {
        console.log('[+] Bypassing SSLContext.init() with custom TrustManager');
        SSLContext_init.call(this, km, TrustManagers, random);
    };
});
```
اركض مع فريدا:
```bash
frida -U -f com.example.app -l ssl_bypass.js
```
### تجاوز الكشف عن الجذر
ربط RootBeer القياسي / الشيكات المشتركة:
```javascript
Java.perform(function() {
    var RootBeer = Java.use("com.scottyab.rootbeer.RootBeer");
    RootBeer.isRooted.implementation = function() {
        console.log("[+] Overriding RootBeer.isRooted() -> false");
        return false;
    };
});
```
### اعتراض أسلوب جافا والتلاعب بالحجج
```javascript
Java.perform(function() {
    var LoginClass = Java.use("com.example.app.AuthManager");
    LoginClass.verifyToken.implementation = function(token) {
        console.log("[*] Original Token: " + token);
        // Force method to return true regardless of server response
        return true;
    };
});
```
---

## 🛠️ مجموعة أدوات وقت الاعتراض

يمكّن الاعتراض من إجراء اختبار متنقل سهل من خلال غلاف REPL التفاعلي:

### بدء جلسة الاعتراض
```bash
objection -g com.example.app explore
```
### تثبيت SSL بأمر واحد وتجاوز الجذر
داخل غلاف الاعتراض:
```text
# Disable SSL Pinning immediately
android sslpinning disable

# Disable Root Detection checks
android root disable

# iOS equivalents
ios sslpinning disable
ios jailbreak disable
```
### استكشاف Keystore وSQLite والتفضيلات المشتركة
```text
# Dump Keystore certificates & keys
android keystore list

# List application SQLite databases
sqlite sync
sqlite execute schema

# Inspect Android SharedPreferences XML files
env
android hooking list activities
```
### دروس الربط وأنشطة الإدراج
```text
# Search classes matching keyword
android hooking search classes auth

# Watch method invocations in real time
android hooking watch class_method com.example.app.Auth.login --dump-args --dump-return
```
---

## ⚡ جدول مرجعي سريع

| الهدف | أمر فريدا | أمر الاعتراض |
|-----------|-------------|-------------------|
| ** قائمة تطبيقات USB ** | `frida-ps -Uai` | `frida-ps -Uai` |
| ** تفرخ التطبيق مع هوك **| `frida -U -f <PKG> -l hook.js` | `objection -g <PKG> explore` |
| ** تجاوز تثبيت SSL ** | `frida -U -f <PKG> -l ssl_bypass.js` | `android sslpinning disable` |
| ** تجاوز فحص الجذر ** | برنامج ربط جافا مخصص | `android root disable` |
| ** تفريغ سلاسل الذاكرة **| `frida-dump` | `memory dump all strings.txt` |
| **قائمة الأنشطة** | `frida-ps` | `android hooking list activities` |
| ** أساليب فئة التتبع **| `frida-trace -U -j "com.pkg.Class!*"` | `android hooking watch class ...` |

---

## 🛡️ الاعتبارات الدفاعية ومكافحة فريدا

1. **فحص المنفذ بحثًا عن Frida:** تحقق من وجود منفذ مضيف محلي مفتوح `27042` أو افحص `/proc/self/maps` بحثًا عن `frida-agent.so`.
2. ** حماية Ptrace الأصلية: ** اتصل بـ `ptrace(PTRACE_TRACEME, 0, 1, 0)` في مكتبات C/JNI الأصلية لمنع مصححات الأخطاء ومحركات الأجهزة من الإرفاق.

---

## ⚠️إخلاء المسؤولية القانونية

> [!تنبيه]
> Frida وObjection هما أداتان متخصصتان في الهندسة العكسية واختبار الاختراق تم تصميمهما بشكل صارم للتقييمات الأمنية المعتمدة. إن استخدام تطبيقات الهاتف المحمول التابعة لجهات خارجية دون إذن ينتهك شروط الخدمة وقوانين الملكية الفكرية.