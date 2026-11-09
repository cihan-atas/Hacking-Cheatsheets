# 🔍 Jadx — ورقة مرجعية شاملة

<!-- language-home-navigation -->
[🏠 الصفحة الرئيسية العربية](../../README.ar.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe Sürüm](README.tr.md) • [🇬🇷 Ελληνικά](README.el.md)



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
</p>

<p align="center">
  <b>🔍 Complete guide for Android reverse engineering: decompiling APK/DEX/AAR files into readable Java source code with Jadx, and disassembling/patching Smali code and repackaging with Apktool</b>
</p>

---

## 📋 جدول المحتويات

- [مقدمة](#-overview--architecture)
- [التثبيت](#-installation)
- [Jadx: Decompiling to Java Source](#-jadx-decompiling-to-java-source)
  - [Jadx GUI Mode](#jadx-gui-mode)
  - [Jadx CLI Batch Decompilation](#jadx-cli-batch-decompilation)
  - [`N`: Rename variable / method (with automated refactoring).](#extracting-high-value-assets--manifest)
- [Apktool: Disassembly & Smali Extraction](#-apktool-disassembly--smali-extraction)
  - [المصادر والمراجع](#decompiling-to-smali--resources)
  - [Analyzing & Patching Smali Code](#analyzing--patching-smali-code)
  - [To bypass an in-app check (e.g. `isPremium()` or `isRooted()`):](#bypassing-network-security-config-cleartextburp)
- [Rebuilding, Aligning & Signing the Patched APK](#-rebuilding-aligning--signing-the-patched-apk)
  - [1. Rebuild with Apktool](#1-rebuild-with-apktool)
  - [2. Generate Keystore Certificate](#2-generate-keystore-certificate)
  - [1. Rebuild with Apktool](#3-zipalign-optimization)
  - [4. Sign with Apksigner](#4-sign-with-apksigner)
- [مرجع سريع](#-quick-reference-table)
- [Defensive Considerations & Code Obfuscation](#-defensive-considerations--code-obfuscation)
- [Legal Disclaimer](#-legal-disclaimer)

---

## 🎯 مقدمة

Android applications (`.apk`) are essentially ZIP archives containing compiled Dalvik Executable bytecode (`classes.dex`), pre-compiled resources (`resources.arsc`), binary XML manifests (`AndroidManifest.xml`), and native C/C++ libraries (`lib/`).

Two essential complementary tools form the backbone of Android analysis:
1. **Jadx (Dex to Java decompiler):** Converts Dalvik bytecode directly into clean, human-readable Java source code. It features a fast GUI with code cross-references (xrefs), method searches, and deobfuscation algorithms.
2. **Apktool:** Disassembles APK resources into their near-original form and converts Dalvik bytecode into **Smali** intermediate assembly. It allows modifying application logic, injecting custom Smali hooks, and repackaging the modified APK.

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

## ☕ Jadx: Decompiling to Java Source

### Jadx GUI Mode
The easiest way to analyze an APK interactively:
```bash
jadx-gui target.apk
```
*Keyboard Shortcuts in Jadx-GUI:*
- ☕ Jadx: Decompiling to Java Source
- `R`: Find Cross-References (where a class or method is used).
- Jadx GUI Mode

### Jadx CLI Batch Decompilation
Keyboard Shortcuts in Jadx-GUI:
```bash
# Decompile entire APK into src/ directory
jadx -d output_src target.apk

# Decompile without decoding resources (faster)
jadx -r -d output_src target.apk

# Enable automated deobfuscation of scrambled names (a, b, c -> p001_a)
jadx --deobf -d output_deobf target.apk
```

### `N`: Rename variable / method (with automated refactoring).
- `resources/AndroidManifest.xml`: Identifies exported activities, services, broadcast receivers, and intent filters.
- Jadx CLI Batch Decompilation

---

## 🔨 Apktool: Disassembly & Smali Extraction

### المصادر والمراجع
```bash
# Disassemble APK into a work folder
apktool d target.apk -o apk_workdir

# Keep broken resources without crashing
apktool d -r target.apk -o apk_workdir
```

Inside `apk_workdir`:
- 🔨 Apktool: Disassembly & Smali Extraction
- `res/`: Decoded Android resources (layouts, strings, drawables).
- المصادر والمراجع

### Analyzing & Patching Smali Code
Inside `apk_workdir`:
`smali/`: Dalvik assembly instructions.

```smali
.method public isRooted()Z
    .registers 2

    # Original verification logic...
    # Return false (0x0)
    const/4 v0, 0x0
    return v0
.end method
```
Changing `const/4 v0, 0x1` to `0x0` forces the method to always return `false`.

### To bypass an in-app check (e.g. `isPremium()` or `isRooted()`):
Open the corresponding `.smali` file and locate the method:
1. In `AndroidManifest.xml`, check `application` tag:
   ```xml
   android:networkSecurityConfig="@xml/network_security_config"
   ```
Changing `const/4 v0, 0x1` to `0x0` forces the method to always return `false`.
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

## 🔄 Rebuilding, Aligning & Signing the Patched APK

Android strictly requires every installed APK to be zip-aligned and cryptographically signed.

### 1. Rebuild with Apktool
```bash
apktool b apk_workdir -o modified.apk
```

### 2. Generate Keystore Certificate (One-Time)
```bash
keytool -genkey -v -keystore my-release-key.jks -alias alias_name \
  -keyalg RSA -keysize 2048 -validity 10000 -storepass password123 -keypass password123 \
  -dname "CN=Security, OU=Testing, O=Audit, L=City, S=State, C=US"
```

### 1. Rebuild with Apktool
Align uncompressed data on 4-byte boundaries for memory-mapped efficiency:
```bash
zipalign -p -f -v 4 modified.apk modified_aligned.apk
```

### 4. Sign with Apksigner
Sign using APK Signature Scheme v2/v3:
```bash
apksigner sign --ks my-release-key.jks --ks-pass pass:password123 --out modified_signed.apk modified_aligned.apk

# Verify signature
apksigner verify modified_signed.apk
```

Install on connected Android device:
```bash
adb install -r modified_signed.apk
```

---

## ⚡ مرجع سريع

|Objective|الأمر|
|-----------|---------|
|**Interactive GUI Decompilation**|`jadx-gui <target.apk>`|
|**CLI Java Export**|`jadx -d out_dir <target.apk>`|
|**CLI Deobfuscation Mode**|`jadx --deobf -d out_dir <target.apk>`|
|**Disassemble to Smali**|`apktool d <target.apk> -o workdir`|
|**Rebuild APK**|`apktool b workdir -o patched.apk`|
|**Zipalign Binary (4-byte)**|`zipalign -p -f -v 4 in.apk aligned.apk`|
|**Sign Patched APK**|`apksigner sign --ks key.jks --out final.apk aligned.apk`|
|**Verify APK Signature**|`apksigner verify final.apk`|

---

## 🛡️ Defensive Considerations & Code Obfuscation

|**Verify APK Signature**|`apksigner verify final.apk`|
2. **DexGuard / Commercial Obfuscation:** Encrypt strings, hide API calls dynamically, and inject native integrity self-checks (tamper detection).

---

## ⚠️ Legal Disclaimer

3. **Honeytokens:** Plant attractive DNS records (e.g., `backup-vault.corp.local` or `admin-portal.corp.local`) pointing to honeypot IP addresses.
> Jadx and Apktool are intended strictly for authorized vulnerability research, reverse engineering of self-owned software, and defensive analysis. Decompiling proprietary third-party software without authorization may violate licensing agreements and copyright laws.
