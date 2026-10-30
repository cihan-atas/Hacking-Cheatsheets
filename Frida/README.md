# 📱 Frida & Objection - Complete Cheatsheet

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
  <img src="https://img.shields.io/badge/Frida-Runtime_Hooking-blue?style=for-the-badge" alt="Frida">
  <img src="https://img.shields.io/badge/Objection-Mobile_Exploration-orange?style=for-the-badge" alt="Objection">
  <img src="https://img.shields.io/badge/Platforms-Android_%7C_iOS-green?style=for-the-badge" alt="Android | iOS">
  <img src="https://img.shields.io/badge/Category-Mobile_Security-red?style=for-the-badge" alt="Mobile Security">
</p>

<p align="center">
  <b>📱 Industry-standard dynamic binary instrumentation toolkit for Android and iOS runtime method hooking, SSL Pinning bypass, root/jailbreak evasion, and memory analysis</b>
</p>

---

## 📋 Table of Contents

- [Overview & Architecture](#-overview--architecture)
- [Installation & Device Setup](#-installation--device-setup)
  - [Host Tools Installation](#host-tools-installation)
  - [Android Device Setup (`frida-server`)](#android-device-setup-frida-server)
  - [iOS Device Setup](#ios-device-setup)
- [Frida CLI Fundamentals](#-frida-cli-fundamentals)
  - [Listing Devices and Running Processes](#listing-devices-and-running-processes)
  - [Attaching vs Spawning](#attaching-vs-spawning)
- [JavaScript Hooking Scripts](#-javascript-hooking-scripts)
  - [Bypassing Android SSL Pinning](#bypassing-android-ssl-pinning)
  - [Bypassing Root Detection](#bypassing-root-detection)
  - [Java Method Interception & Argument Tampering](#java-method-interception--argument-tampering)
- [Objection Runtime Toolkit](#-objection-runtime-toolkit)
  - [Starting an Objection Session](#starting-an-objection-session)
  - [One-Command SSL Pinning & Root Bypass](#one-command-ssl-pinning--root-bypass)
  - [Exploring Keystore, SQLite, and Shared Preferences](#exploring-keystore-sqlite-and-shared-preferences)
  - [Hooking Classes and Listing Activities](#hooking-classes-and-listing-activities)
- [Quick Reference Table](#-quick-reference-table)
- [Defensive Considerations & Anti-Frida](#-defensive-considerations--anti-frida)
- [Legal Disclaimer](#-legal-disclaimer)

---

## 🎯 Overview & Architecture

**Frida** injects Google's V8 JavaScript runtime into target processes on Android, iOS, Windows, macOS, and Linux. It empowers security researchers to:
- Hook arbitrary native (`libc`, OpenSSL) and managed (`Java`, `Objective-C`, `Swift`) functions.
- Inspect and modify memory and function parameters on the fly.
- Trace cryptographic APIs and decrypt encrypted traffic in memory.

**Objection** is a mobile security runtime assessment framework powered by Frida that automates common mobile pentesting tasks (disabling certificate pinning, dumping memory, bypassing biometric authentication) without requiring manual JavaScript code.

---

## 📥 Installation & Device Setup

### Host Tools Installation
```bash
pip install frida frida-tools objection
```

### Android Device Setup (`frida-server`)
Download the `frida-server` matching your host Frida version and device CPU architecture (x86_64, arm64, etc.):
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

### iOS Device Setup
On a jailbroken iOS device, add the Frida repository in Cydia / Sileo (`https://build.frida.re`) and install the `Frida` package.

---

## 🚀 Frida CLI Fundamentals

### Listing Devices and Running Processes
```bash
# List all connected USB devices
frida-ls-devices

# List all running applications on USB-connected Android/iOS
frida-ps -Uai

# List all system processes on USB device
frida-ps -U
```

### Attaching vs Spawning
- **Attach (`-n` or `-p`):** Attaches to an already running app.
- **Spawn (`-f`):** Starts the application from early boot, pausing execution before initial constructors execute:
```bash
# Attach to running process
frida -U -n com.example.app -l hook.js

# Spawn process with early instrumentation
frida -U -f com.example.app -l hook.js
```

---

## 💻 JavaScript Hooking Scripts

### Bypassing Android SSL Pinning
Universal TrustManager bypass script (`ssl_bypass.js`):
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

Run with Frida:
```bash
frida -U -f com.example.app -l ssl_bypass.js
```

### Bypassing Root Detection
Hook standard RootBeer / Common checks:
```javascript
Java.perform(function() {
    var RootBeer = Java.use("com.scottyab.rootbeer.RootBeer");
    RootBeer.isRooted.implementation = function() {
        console.log("[+] Overriding RootBeer.isRooted() -> false");
        return false;
    };
});
```

### Java Method Interception & Argument Tampering
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

## 🛠️ Objection Runtime Toolkit

Objection enables effortless mobile testing through an interactive REPL shell:

### Starting an Objection Session
```bash
objection -g com.example.app explore
```

### One-Command SSL Pinning & Root Bypass
Inside the objection shell:
```text
# Disable SSL Pinning immediately
android sslpinning disable

# Disable Root Detection checks
android root disable

# iOS equivalents
ios sslpinning disable
ios jailbreak disable
```

### Exploring Keystore, SQLite, and Shared Preferences
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

### Hooking Classes and Listing Activities
```text
# Search classes matching keyword
android hooking search classes auth

# Watch method invocations in real time
android hooking watch class_method com.example.app.Auth.login --dump-args --dump-return
```

---

## ⚡ Quick Reference Table

| Objective | Frida Command | Objection Command |
|-----------|---------------|-------------------|
| **List USB Apps** | `frida-ps -Uai` | `frida-ps -Uai` |
| **Spawn App with Hook**| `frida -U -f <PKG> -l hook.js` | `objection -g <PKG> explore` |
| **Bypass SSL Pinning** | `frida -U -f <PKG> -l ssl_bypass.js` | `android sslpinning disable` |
| **Bypass Root Check** | Custom Java hook script | `android root disable` |
| **Dump Memory Strings**| `frida-dump` | `memory dump all strings.txt` |
| **List Activities** | `frida-ps` | `android hooking list activities` |
| **Trace Class Methods**| `frida-trace -U -j "com.pkg.Class!*"` | `android hooking watch class ...` |

---

## 🛡️ Defensive Considerations & Anti-Frida

1. **Port Scanning for Frida:** Check for open localhost port `27042` or examine `/proc/self/maps` for `frida-agent.so`.
2. **Native Ptrace Protection:** Call `ptrace(PTRACE_TRACEME, 0, 1, 0)` in native C/JNI libraries to block debuggers and instrumentation engines from attaching.

---

## ⚠️ Legal Disclaimer

> [!CAUTION]
> Frida and Objection are specialized reverse engineering and penetration testing tools designed strictly for authorized security assessments. Instrumenting third-party mobile applications without permission violates terms of service and intellectual property laws.
