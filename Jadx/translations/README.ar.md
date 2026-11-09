<!-- language-home-navigation -->
[🏠 الصفحة الرئيسية العربية](../../README.ar.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🔍 Jadx & Apktool - ورقة الغش الكاملة

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
       ██╗ █████╗ ██████╗ ██╗  ██╗    ███████╗    █████╗ ██████╗ ██╗  ██╗████████╗ ██████╗  ██████╗ ██╗     
       ██║██╔══██╗██╔══██╗╚██╗██╔╝    ██╔════╝   ██╔══██╗██╔══██╗██║ ██╔╝╚══██╔══╝██╔═══██╗██╔═══██╗██║     
       ██║███████║██║  ██║ ╚███╔╝     ███████╗   ███████║██████╔╝█████╔╝    ██║   ██║   ██║██║   ██║██║     
  ██   ██║██╔══██║██║  ██║ ██╔██╗     ╚════██║   ██╔══██║██╔═══╝ ██╔═██╗    ██║   ██║   ██║██║   ██║██║     
  ╚█████╔╝██║  ██║██████╔╝██╔╝ ██╗    ███████║██╗██║  ██║██║     ██║  ██╗   ██║   ╚██████╔╝╚██████╔╝███████╗
   ╚════╝ ╚═╝  ╚═╝╚═════╝ ╚═╝  ╚═╝    ╚══════╝╚═╝╚═╝  ╚═╝╚═╝     ╚═╝  ╚═╝   ╚═╝    ╚═════╝  ╚═════╝ ╚══════╝
           Android APK Decompilation, Smali Patching & Repackaging
```
<p align="center">
  <img src="https://img.shields.io/badge/Jadx-DEX_to_Java-blue?style=for-the-badge" alt="Jadx">
  <img src="https://img.shields.io/badge/Apktool-Smali_Disassembler-orange?style=for-the-badge" alt="Apktool">
  <img src="https://img.shields.io/badge/Category-Mobile_Reverse_Engineering-green?style=for-the-badge" alt="Mobile RE">
  <img src="https://img.shields.io/badge/Platform-Android-purple?style=for-the-badge" alt="Android">
</ص>

<p align="center">
  <b>🔍 دليل كامل للهندسة العكسية لنظام Android: إلغاء ترجمة ملفات APK/DEX/AAR إلى كود مصدر Java يمكن قراءته باستخدام Jadx، وتفكيك/تصحيح كود Smali وإعادة حزمه باستخدام Apktool</b>
</ص>

---

## 📋 جدول المحتويات

- [نظرة عامة والهندسة المعمارية](#-نظرة عامة--الهندسة المعمارية)
- [التثبيت](#-التثبيت)
- [Jadx: فك الترجمة إلى مصدر Java](#-jadx-decompiling-to-java-source)
  - [وضع Jadx GUI](#jadx-gui-mode)
  - [تفكيك دفعة Jadx CLI](#jadx-cli-batch-decompilation)
  - [استخراج الأصول ذات القيمة العالية والبيان](#استخراج-الأصول ذات القيمة العالية--البيان)
- [Apktool: التفكيك واستخراج Smali](#-apktool-disassembly--smali-extraction)
  - [التحويل إلى Smali والموارد](#decompiling-to-smali--resources)
  - [تحليل وتصحيح كود Smali](#analyzing--patching-smali-code)
  - [تجاوز تكوين أمان الشبكة (Cleartext/Burp)](#bypassing-network-security-config-cleartextburp)
- [إعادة بناء ملف APK المصحح ومحاذاته وتوقيعه](#-rebuilding-aligning--signing-the-patched-apk)
  - [1. إعادة البناء باستخدام Apktool](#1-rebuild-with-apktool)
  - [2. إنشاء شهادة مخزن المفاتيح](#2-إنشاء-شهادة-مخزن المفاتيح)
  - [3. تحسين Zipalign](#3-تحسين Zipalign)
  - [4. قم بالتسجيل باستخدام Apksigner](#4-sign-with-apksigner)
- [الجدول المرجعي السريع](#-الجدول المرجعي السريع)
- [الاعتبارات الدفاعية والتعتيم على الكود](#-الاعتبارات الدفاعية--التعتيم على الكود)
- [إخلاء المسؤولية القانونية](#-إخلاء المسؤولية القانونية)

---

## 🎯 نظرة عامة والهندسة المعمارية

تطبيقات Android (`.apk`) هي في الأساس أرشيفات ZIP تحتوي على كود Dalvik القابل للتنفيذ المترجم (`classes.dex`)، والموارد المترجمة مسبقًا (`resources.arsc`)، وبيانات XML الثنائية (`AndroidManifest.xml`)، ومكتبات C/C++ الأصلية (`lib/`).

هناك أداتان متكاملتان أساسيتان تشكلان العمود الفقري لتحليل Android:
1. ** Jadx (Dex to Java decompiler): ** يحول كود Dalvik الثانوي مباشرةً إلى كود مصدر Java نظيف يمكن قراءته بواسطة الإنسان. ويتميز بواجهة مستخدم رسومية سريعة مع إسناد ترافقي للكود (xrefs)، وعمليات بحث عن الأساليب، وخوارزميات إزالة التشويش.
2. **Apktool:** تفكيك موارد APK إلى شكلها شبه الأصلي وتحويل رمز Dalvik الثانوي إلى تجميع وسيط **Smali**. فهو يسمح بتعديل منطق التطبيق، وإدخال خطافات Smali مخصصة، وإعادة تعبئة ملف APK المعدل.

---

## 📥 التثبيت
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y jadx apktool apksigner zipalign

# macOS (Homebrew)
brew install jadx apktool android-commandlinetools

# Arch Linux
sudo pacman -S jadx apktool
```
---

## ☕ Jadx: فك الترجمة إلى مصدر Java

### وضع واجهة المستخدم الرسومية Jadx
أسهل طريقة لتحليل APK بشكل تفاعلي:
```bash
jadx-gui target.apk
```
*اختصارات لوحة المفاتيح في Jadx-GUI:*
- `Ctrl + Shift + F`: البحث الشامل عن النص والفصل.
- `R`: البحث عن المراجع التبادلية (حيث يتم استخدام فئة أو أسلوب).
- `N`: إعادة تسمية المتغير/الطريقة (مع إعادة البناء الآلي).

### فك دفعة Jadx CLI
قم بتصدير كود مصدر Java وأصوله وموارده إلى دليل الإخراج دون تشغيل واجهة المستخدم الرسومية:
```bash
# Decompile entire APK into src/ directory
jadx -d output_src target.apk

# Decompile without decoding resources (faster)
jadx -r -d output_src target.apk

# Enable automated deobfuscation of scrambled names (a, b, c -> p001_a)
jadx --deobf -d output_deobf target.apk
```
### استخراج الأصول ذات القيمة العالية والبيان
- `resources/AndroidManifest.xml`: يحدد الأنشطة المصدرة والخدمات وأجهزة استقبال البث ومرشحات الهدف.
- `resources/res/values/strings.xml`: يحتوي بشكل متكرر على مفاتيح API المشفرة وعناوين URL للخادم وأسرار عميل OAuth.

---

## 🔨 Apktool: التفكيك واستخراج Smali

### فك الترجمة إلى Smali والموارد
```bash
# Disassemble APK into a work folder
apktool d target.apk -o apk_workdir

# Keep broken resources without crashing
apktool d -r target.apk -o apk_workdir
```
داخل `apk_workdir`:
- `smali/`: تعليمات تجميع Dalvik.
- `res/`: موارد Android التي تم فك تشفيرها (التخطيطات والسلاسل والرسومات).
- `AndroidManifest.xml`: بيان XML للنص العادي.

### تحليل وتصحيح كود Smali
لتجاوز الفحص داخل التطبيق (على سبيل المثال، `isPremium()` أو `isRooted()`):
افتح ملف `.smali` المقابل وحدد الطريقة:
```smali
.method public isRooted()Z
    .registers 2

    # Original verification logic...
    # Return false (0x0)
    const/4 v0, 0x0
    return v0
.end method
```
يؤدي تغيير `const/4 v0, 0x1` إلى `0x0` إلى إجبار الطريقة على إرجاع `false` دائمًا.

### تجاوز تكوين أمان الشبكة (Cleartext/Burp)
لالتقاط حركة مرور HTTPS من تطبيقات Android 7+ (API 24+) في Burp Suite:
1. في `AndroidManifest.xml`، تحقق من علامة `application`:
   ```xml
   android:networkSecurityConfig="@xml/network_security_config"
   ```
2. في `res/xml/network_security_config.xml`، السماح بشهادات المستخدم:
   ```xml
   <?xml version="1.0" encoding="utf-8"?>
   <network-security-config>
       <base-config>
           <trust-anchors>
               <certificates src="system" />
               <certificates src="user" />
           </trust-anchors>
       </base-config>
   </network-security-config>
   ```
---

## 🔄 إعادة بناء ملف APK المصحح ومواءمته وتوقيعه

يتطلب Android بشكل صارم أن يكون كل ملف APK مثبتًا بمحاذاة الرمز البريدي وموقعًا بالتشفير.

### 1. إعادة البناء باستخدام Apktool
```bash
apktool b apk_workdir -o modified.apk
```
### 2. إنشاء شهادة Keystore (لمرة واحدة)
```bash
keytool -genkey -v -keystore my-release-key.jks -alias alias_name \
  -keyalg RSA -keysize 2048 -validity 10000 -storepass password123 -keypass password123 \
  -dname "CN=Security, OU=Testing, O=Audit, L=City, S=State, C=US"
```
### 3. تحسين Zipalign
محاذاة البيانات غير المضغوطة على حدود 4 بايت لتحقيق كفاءة الذاكرة المعينة:
```bash
zipalign -p -f -v 4 modified.apk modified_aligned.apk
```
### 4. قم بالتسجيل باستخدام Apksigner
قم بالتسجيل باستخدام APK Signature Scheme v2/v3:
```bash
apksigner sign --ks my-release-key.jks --ks-pass pass:password123 --out modified_signed.apk modified_aligned.apk

# Verify signature
apksigner verify modified_signed.apk
```
التثبيت على جهاز Android المتصل:
```bash
adb install -r modified_signed.apk
```
---

## ⚡ جدول مرجعي سريع

| الهدف | الأمر |
|-----------|--------|
| ** فك واجهة المستخدم الرسومية التفاعلية ** | `jadx-gui <target.apk>` |
| ** تصدير جافا CLI ** | `jadx -d out_dir <target.apk>` |
| **وضع إزالة تشويش واجهة سطر الأوامر** | `jadx --deobf -d out_dir <target.apk>` |
| **تفكيك إلى سمالي** | `apktool d <target.apk> -o workdir` |
| ** إعادة بناء APK ** | `apktool b workdir -o patched.apk` |
| ** ثنائي Zipalign (4 بايت) ** | `zipalign -p -f -v 4 in.apk aligned.apk` |
| ** تسجيل مصحح APK ** | `apksigner sign --ks key.jks --out final.apk aligned.apk` |
| **التحقق من توقيع APK** | `apksigner verify final.apk` |

---

## 🛡️ الاعتبارات الدفاعية والتعتيم على الكود

1. **تصغير R8 / ProGuard:** إزالة الفئات غير المستخدمة وإعادة تسمية المعرفات إلى أحرف مفردة غير وصفية (`a.b.c`).
2. **DexGuard / التعتيم التجاري:** تشفير السلاسل وإخفاء استدعاءات واجهة برمجة التطبيقات (API) ديناميكيًا وإدخال عمليات التحقق الذاتي من السلامة الأصلية (اكتشاف العبث).

---

## ⚠️إخلاء المسؤولية القانونية

> [!تنبيه]
> تم تصميم Jadx وApktool حصريًا لأبحاث الثغرات الأمنية المعتمدة، والهندسة العكسية للبرامج المملوكة ذاتيًا، والتحليل الدفاعي. قد يؤدي إلغاء ترجمة برامج الطرف الثالث دون تصريح إلى انتهاك اتفاقيات الترخيص وقوانين حقوق الطبع والنشر.