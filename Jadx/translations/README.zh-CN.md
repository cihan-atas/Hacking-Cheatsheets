<!-- language-home-navigation -->
[🏠 中文主页](../../README.zh-CN.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🔍 Jadx 和 Apktool - 完整备忘单

[🇹🇷 Türkçe Sürüm]（翻译/README.tr.md）
```
       ██╗ █████╗ ██████╗ ██╗  ██╗    ███████╗    █████╗ ██████╗ ██╗  ██╗████████╗ ██████╗  ██████╗ ██╗     
       ██║██╔══██╗██╔══██╗╚██╗██╔╝    ██╔════╝   ██╔══██╗██╔══██╗██║ ██╔╝╚══██╔══╝██╔═══██╗██╔═══██╗██║     
       ██║███████║██║  ██║ ╚███╔╝     ███████╗   ███████║██████╔╝█████╔╝    ██║   ██║   ██║██║   ██║██║     
  ██   ██║██╔══██║██║  ██║ ██╔██╗     ╚════██║   ██╔══██║██╔═══╝ ██╔═██╗    ██║   ██║   ██║██║   ██║██║     
  ╚█████╔╝██║  ██║██████╔╝██╔╝ ██╗    ███████║██╗██║  ██║██║     ██║  ██╗   ██║   ╚██████╔╝╚██████╔╝███████╗
   ╚════╝ ╚═╝  ╚═╝╚═════╝ ╚═╝  ╚═╝    ╚══════╝╚═╝╚═╝  ╚═╝╚═╝     ╚═╝  ╚═╝   ╚═╝    ╚═════╝  ╚═════╝ ╚══════╝
           Android APK Decompilation, Smali Patching & Repackaging
```
<p对齐=“中心”>
  <img src="https://img.shields.io/badge/Jadx-DEX_to_Java-blue?style=for-the-badge" alt="Jadx">
  <img src="https://img.shields.io/badge/Apktool-Smali_Disassembler-orange?style=for-the-badge" alt="Apktool">
  <img src="https://img.shields.io/badge/Category-Mobile_Reverse_Engineering-green?style=for-the-badge" alt="Mobile RE">
  <img src="https://img.shields.io/badge/Platform-Android-purple?style=for-the-badge" alt="Android">
</p>

<p对齐=“中心”>
  <b>🔍 Android 逆向工程完整指南：使用 Jadx 将 APK/DEX/AAR 文件反编译为可读的 Java 源代码，并使用 Apktool 反汇编/修补 Smali 代码并重新打包</b>
</p>

---

## 📋 目录

- [概述与架构](#-overview--architecture)
- [安装](#-安装)
- [Jadx：反编译为 Java 源代码](#-jadx-decompiling-to-java-source)
  - [Jadx GUI 模式](#jadx-gui-mode)
  - [Jadx CLI 批量反编译](#jadx-cli-batch-decompilation)
  - [提取高价值资产和清单](#extracting-high-value-assets--manifest)
- [Apktool：反汇编和 Smali 提取](#-apktool-disassemble--smali-extraction)
  - [反编译为 Smali 和资源](#decompiling-to-smali--resources)
  - [分析和修补 Smali 代码](#analyzing--patching-smali-code)
  - [绕过网络安全配置（Cleartext/Burp）](#bypassing-network-security-config-cleartextburp)
- [重建、对齐和签署已修补的 APK](#-rebuilding-aligning--signing-the-patched-apk)
  - [1.使用 Apktool 重建](#1-rebuild-with-apktool)
  - [2.生成密钥库证书](#2-generate-keystore-certificate)
  - [3. Zipalign 优化](#3-zipalign-optimization)
  - [4.使用 Apksigner 签名](#4-sign-with-apksigner)
- [快速参考表](#-quick-reference-table)
- [防御注意事项和代码混淆](#-defective-considerations--code-obfuscation)
- [法律免责声明](#-legal-disclaimer)

---

## 🎯 概述与架构

Android 应用程序 (`.apk`) 本质上是 ZIP 存档，其中包含已编译的 Dalvik 可执行字节码 (`classes.dex`)、预编译资源 (`resources.arsc`)、二进制 XML 清单 (`AndroidManifest.xml`) 和本机 C/C++ 库 (`lib/`)。

两个重要的补充工具构成了 Android 分析的支柱：
1. **Jadx（Dex 到 Java 反编译器）：** 将 Dalvik 字节码直接转换为干净、人类可读的 Java 源代码。它具有快速 GUI，具有代码交叉引用 (xref)、方法搜索和反混淆算法。
2. **Apktool：** 将 APK 资源反汇编成接近原始的形式，并将 Dalvik 字节码转换为 **Smali** 中间程序集。它允许修改应用程序逻辑、注入自定义 Smali 挂钩以及重新打包修改后的 APK。

---

## 📥 安装
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y jadx apktool apksigner zipalign

# macOS (Homebrew)
brew install jadx apktool android-commandlinetools

# Arch Linux
sudo pacman -S jadx apktool
```
---

## ☕ Jadx：反编译为 Java 源代码

### Jadx GUI 模式
交互式分析 APK 的最简单方法：
```bash
jadx-gui target.apk
```
*Jadx-GUI 中的键盘快捷键：*
- `Ctrl + Shift + F`：全局文本和类别搜索。
- `R`：查找交叉引用（使用类或方法的地方）。
- `N`：重命名变量/方法（使用自动重构）。

### Jadx CLI批量反编译
将 Java 源代码、资产和资源导出到输出目录，而无需启动 GUI：
```bash
# Decompile entire APK into src/ directory
jadx -d output_src target.apk

# Decompile without decoding resources (faster)
jadx -r -d output_src target.apk

# Enable automated deobfuscation of scrambled names (a, b, c -> p001_a)
jadx --deobf -d output_deobf target.apk
```
### 提取高价值资产和清单
- `resources/AndroidManifest.xml`：标识导出的活动、服务、广播接收器和意图过滤器。
- `resources/res/values/strings.xml`：经常包含硬编码的 API 密钥、服务器 URL 和 OAuth 客户端机密。

---

## 🔨 Apktool：反汇编和 Smali 提取

### 反编译为 Smali 和资源
```bash
# Disassemble APK into a work folder
apktool d target.apk -o apk_workdir

# Keep broken resources without crashing
apktool d -r target.apk -o apk_workdir
```
`apk_workdir`内部：
- `smali/`：Dalvik 组装说明。
- `res/`：解码的 Android 资源（布局、字符串、绘图）。
- `AndroidManifest.xml`：纯文本 XML 清单。

### 分析和修补 Smali 代码
要绕过应用内检查（例如 `isPremium()` 或 `isRooted()`）：
打开对应的`.smali`文件，找到方法：
```smali
.method public isRooted()Z
    .registers 2

    # Original verification logic...
    # Return false (0x0)
    const/4 v0, 0x0
    return v0
.end method
```
将 `const/4 v0, 0x1` 更改为 `0x0` 会强制该方法始终返回 `false`。

### 绕过网络安全配置（明文/Burp）
要在 Burp Suite 中捕获来自 Android 7+ (API 24+) 应用程序的 HTTPS 流量：
1. 在`AndroidManifest.xml`中，检查`application`标签：
   ```xml
   android:networkSecurityConfig="@xml/network_security_config"
   ```
2. 在`res/xml/network_security_config.xml`中，允许用户证书：
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

## 🔄 重建、对齐并签署已修补的 APK

Android 严格要求每个安装的 APK 都经过 zip 对齐和加密签名。

### 1. 使用 Apktool 重建
```bash
apktool b apk_workdir -o modified.apk
```
### 2. 生成密钥库证书（一次性）
```bash
keytool -genkey -v -keystore my-release-key.jks -alias alias_name \
  -keyalg RSA -keysize 2048 -validity 10000 -storepass password123 -keypass password123 \
  -dname "CN=Security, OU=Testing, O=Audit, L=City, S=State, C=US"
```
### 3.Zipalign 优化
在 4 字节边界上对齐未压缩数据以提高内存映射效率：
```bash
zipalign -p -f -v 4 modified.apk modified_aligned.apk
```
### 4. 使用 Apksigner 签名
使用 APK 签名方案 v2/v3 进行签名：
```bash
apksigner sign --ks my-release-key.jks --ks-pass pass:password123 --out modified_signed.apk modified_aligned.apk

# Verify signature
apksigner verify modified_signed.apk
```
在连接的 Android 设备上安装：
```bash
adb install -r modified_signed.apk
```
---

## ⚡ 快速参考表

|目标|命令 |
|------------|---------|
| **交互式GUI反编译** | `jadx-gui <target.apk>` |
| **CLI Java 导出** | `jadx -d out_dir <target.apk>` |
| **CLI 反混淆模式** | `jadx --deobf -d out_dir <target.apk>` |
| **反汇编成Smali** | `apktool d <target.apk> -o workdir` |
| **重建 APK** | `apktool b workdir -o patched.apk` |
| **Zipalign 二进制（4 字节）** | `zipalign -p -f -v 4 in.apk aligned.apk` |
| **签署已修补的 APK** | `apksigner sign --ks key.jks --out final.apk aligned.apk` |
| **验证 APK 签名** | `apksigner verify final.apk` |

---

## 🛡️ 防御考虑和代码混淆

1. **R8 / ProGuard 缩小：** 剥离未使用的类并将标识符重命名为单个非描述性字符 (`a.b.c`)。
2. **DexGuard / 商业混淆：** 加密字符串，动态隐藏 API 调用，并注入本机完整性自检查（篡改检测）。

---

## ⚠️ 法律免责声明

> [!警告]
> Jadx 和 Apktool 严格用于授权漏洞研究、自有软件逆向工程和防御分析。未经授权反编译第三方专有软件可能违反许可协议和版权法。