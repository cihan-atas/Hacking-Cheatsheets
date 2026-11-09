<!-- language-home-navigation -->
[🏠 中文主页](../../README.zh-CN.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 📱 Frida & Objection - 完整备忘单

[🇹🇷 Türkçe Sürüm]（翻译/README.tr.md）
```
  ███████╗██████╗ ██╗██████╗  █████╗      ██████╗ ██████╗      ██╗███████╗ ██████╗████████╗██╗ ██████╗ ███╗   ██╗
  ██╔════╝██╔══██╗██║██╔══██╗██╔══██╗    ██╔═══██╗██╔══██╗     ██║██╔════╝██╔════╝╚══██╔══╝██║██╔═══██╗████╗  ██║
  █████╗  ██████╔╝██║██║  ██║███████║    ██║   ██║██████╔╝     ██║█████╗  ██║        ██║   ██║██║   ██║██╔██╗ ██║
  ██╔══╝  ██╔══██╗██║██║  ██║██╔══██║    ██║   ██║██╔══██╗██   ██║██╔══╝  ██║        ██║   ██║██║   ██║██║╚██╗██║
  ██║     ██║  ██║██║██████╔╝██║  ██║    ╚██████╔╝██████╔╝╚█████╔╝███████╗╚██████╗   ██║   ██║╚██████╔╝██║ ╚████║
  ╚═╝     ╚═╝  ╚═╝╚═╝╚═════╝ ╚═╝  ╚═╝     ╚═════╝ ╚═════╝  ╚════╝ ╚══════╝ ╚═════╝   ╚═╝   ╚═╝ ╚═════╝ ╚═╝  ╚═══╝
           Dynamic Binary Instrumentation & Mobile Security Assessment
```
<p对齐=“中心”>
  <img src="https://img.shields.io/badge/Frida-Runtime_Hooking-blue?style=for-the-badge" alt="Frida">
  <img src="https://img.shields.io/badge/Objection-Mobile_Exploration-orange?style=for-the-badge" alt="Objection">
  <img src="https://img.shields.io/badge/Platforms-Android_%7C_iOS-green?style=for-the-badge" alt="Android | iOS">
  <img src="https://img.shields.io/badge/Category-Mobile_Security-red?style=for-the-badge" alt="移动安全">
</p>

<p对齐=“中心”>
  <b>📱行业标准动态二进制检测工具包，用于 Android 和 iOS 运行时方法挂钩、SSL Pinning 绕过、root/越狱规避和内存分析</b>
</p>

---

## 📋 目录

- [概述与架构](#-overview--architecture)
- [安装和设备设置](#-installation--device-setup)
  - [主机工具安装](#host-tools-installation)
  - [Android 设备设置 (`frida-server`)](#android-device-setup-frida-server)
  - [iOS 设备设置](#ios-device-setup)
- [Frida CLI 基础知识](#-frida-cli-fundamentals)
  - [列出设备和正在运行的进程](#listing-devices-and-running-processes)
  - [附着 vs 生成](#attaching-vs-spawning)
- [JavaScript 挂钩脚本](#-javascript-hooking-scripts)
  - [绕过 Android SSL Pinning](#bypassing-android-ssl-pinning)
  - [绕过根检测](#bypassing-root-detection)
  - [Java 方法拦截和参数篡改](#java-method-interception--argument-tampering)
- [异议运行时工具包](#-objection-runtime-toolkit)
  - [开始异议会话](#starting-an-objection-session)
  - [一键 SSL 固定和根绕过](#one-command-ssl-pinning--root-bypass)
  - [探索密钥库、SQLite 和共享首选项](#exploring-keystore-sqlite-and-shared-preferences)
  - [挂钩类和列出活动](#hooking-classes-and-listing-activities)
- [快速参考表](#-quick-reference-table)
- [防御考虑与反弗里达](#-defective-considerations--anti-frida)
- [法律免责声明](#-legal-disclaimer)

---

## 🎯 概述与架构

**Frida** 将 Google 的 V8 JavaScript 运行时注入 Android、iOS、Windows、macOS 和 Linux 上的目标进程。它使安全研究人员能够：
- 挂钩任意本机（`libc`、OpenSSL）和托管（`Java`、`Objective-C`、`Swift`）函数。
- 即时检查和修改内存和功能参数。
- 跟踪加密 API 并解密内存中的加密流量。

**Objection** 是由 Frida 支持的移动安全运行时评估框架，可自动执行常见的移动渗透测试任务（禁用证书固定、转储内存、绕过生物识别身份验证），而无需手动 JavaScript 代码。

---

## 📥 安装和设备设置

### 主机工具安装
```bash
pip install frida frida-tools objection
```
### Android 设备设置 (`frida-server`)
下载与您的主机Frida版本和设备CPU架构（x86_64、arm64等）匹配的`frida-server`：
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
### iOS 设备设置
在越狱的 iOS 设备上，在 Cydia / Sileo (`https://build.frida.re`) 中添加 Frida 存储库并安装 `Frida` 软件包。

---

## 🚀 Frida CLI 基础知识

### 列出设备和正在运行的进程
```bash
# List all connected USB devices
frida-ls-devices

# List all running applications on USB-connected Android/iOS
frida-ps -Uai

# List all system processes on USB device
frida-ps -U
```
### 附着与产卵
- **附加（`-n` 或 `-p`）：** 附加到已运行的应用程序。
- **Spawn (`-f`):** 从早期启动启动应用程序，在初始构造函数执行之前暂停执行：
```bash
# Attach to running process
frida -U -n com.example.app -l hook.js

# Spawn process with early instrumentation
frida -U -f com.example.app -l hook.js
```
---

## 💻 JavaScript 挂钩脚本

### 绕过 Android SSL 固定
通用 TrustManager 绕过脚本 (`ssl_bypass.js`)：
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
和弗里达一起跑步：
```bash
frida -U -f com.example.app -l ssl_bypass.js
```
### 绕过根检测
Hook 标准 RootBeer / 常见检查：
```javascript
Java.perform(function() {
    var RootBeer = Java.use("com.scottyab.rootbeer.RootBeer");
    RootBeer.isRooted.implementation = function() {
        console.log("[+] Overriding RootBeer.isRooted() -> false");
        return false;
    };
});
```
### Java 方法拦截和参数篡改
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

## 🛠️ 异议运行时工具包

Objection 通过交互式 REPL shell 实现轻松的移动测试：

### 开始异议会议
```bash
objection -g com.example.app explore
```
### 一命令 SSL 固定和根绕过
在异议外壳内：
```text
# Disable SSL Pinning immediately
android sslpinning disable

# Disable Root Detection checks
android root disable

# iOS equivalents
ios sslpinning disable
ios jailbreak disable
```
### 探索密钥库、SQLite 和共享首选项
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
### 挂钩类和列表活动
```text
# Search classes matching keyword
android hooking search classes auth

# Watch method invocations in real time
android hooking watch class_method com.example.app.Auth.login --dump-args --dump-return
```
---

## ⚡ 快速参考表

|目标|弗里达指挥部 |反对命令|
|----------|--------------|--------------------|
| **列出 USB 应用程序** | `frida-ps -Uai` | `frida-ps -Uai` |
| **带 Hook 的 Spawn 应用程序**| `frida -U -f <PKG> -l hook.js` | `objection -g <PKG> explore` |
| **绕过 SSL 固定** | `frida -U -f <PKG> -l ssl_bypass.js` | `android sslpinning disable` |
| **绕过根检查** |自定义Java钩子脚本| `android root disable` |
| **转储内存字符串**| `frida-dump` | `memory dump all strings.txt` |
| **列出活动** | `frida-ps` | `android hooking list activities`|
| **跟踪类方法**| `frida-trace -U -j "com.pkg.Class!*"`| `android hooking watch class ...` |

---

## 🛡️ 防御性考虑和反弗里达

1. **Frida 的端口扫描：** 检查开放的本地主机端口 `27042` 或检查 `/proc/self/maps` 中的 `frida-agent.so`。
2. **本机 Ptrace 保护：** 在本机 C/JNI 库中调用 `ptrace(PTRACE_TRACEME, 0, 1, 0)` 以阻止调试器和仪器引擎附加。

---

## ⚠️ 法律免责声明

> [!警告]
> Frida 和 Objection 是专门为授权安全评估而设计的专业逆向工程和渗透测试工具。未经许可对第三方移动应用程序进行检测违反了服务条款和知识产权法。