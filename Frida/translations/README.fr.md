<!-- language-home-navigation -->
[🏠 Page d'accueil Française](../../README.fr.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 📱 Frida & Objeko - Aide-mémoire complet

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
  <img src="https://img.shields.io/badge/Category-Mobile_Security-red?style=for-the-badge" alt="Sécurité mobile">
</p>

<p align="center">
  <b>📱 Boîte à outils d'instrumentation binaire dynamique standard de l'industrie pour l'accrochage de méthodes d'exécution Android et iOS, le contournement de l'épinglage SSL, l'évasion de root/jailbreak et l'analyse de la mémoire</b>
</p>

---

## 📋 Table des matières

- [Vue d'ensemble et architecture](#-overview--architecture)
- [Installation et configuration du périphérique](#-installation--configuration du périphérique)
  - [Installation des outils hôtes](#host-tools-installation)
  - [Configuration de l'appareil Android (`frida-server`)](#android-device-setup-frida-server)
  - [Configuration du périphérique iOS](#ios-device-setup)
- [Frida CLI Fondamentaux](#-frida-cli-fundamentals)
  - [Liste des appareils et des processus en cours d'exécution](#listing-devices-and-running-processes)
  - [Attaching vs Spawning](#attaching-vs-spawning)
- [Scripts de hooking JavaScript](#-javascript-hooking-scripts)
  - [Contourner l'épinglage SSL Android](#bypassing-android-ssl-pinning)
  - [Contourner la détection de racine](#bypassing-root-detection)
  - [Interception de méthode Java et falsification d'arguments](#java-method-interception--argument-falsification)
- [Objection Runtime Toolkit](#-objection-runtime-toolkit)
  - [Démarrer une session d'objection](#starting-an-objection-session)
  - [Épinglage SSL et contournement racine en une seule commande](#one-command-ssl-pinning--root-bypass)
  - [Exploration du Keystore, SQLite et des préférences partagées](#exploring-keystore-sqlite-and-shared-preferences)
  - [Cours de hooking et activités de listing](#hooking-classes-and-listing-activities)
- [Tableau de référence rapide](#-quick-reference-table)
- [Considérations défensives et anti-Frida](#-considérations-defensives--anti-frida)
- [Mentions légales](#-legal-disclaimer)

---

## 🎯 Présentation et architecture

**Frida** injecte le runtime JavaScript V8 de Google dans les processus cibles sur Android, iOS, Windows, macOS et Linux. Il permet aux chercheurs en sécurité de :
- Accrochez des fonctions arbitraires natives (`libc`, OpenSSL) et gérées (`Java`, `Objective-C`, `Swift`).
- Inspectez et modifiez les paramètres de mémoire et de fonction à la volée.
- Tracez les API cryptographiques et décryptez le trafic chiffré en mémoire.

**Objection** est un cadre d'évaluation de l'exécution de la sécurité mobile optimisé par Frida qui automatise les tâches courantes de test d'intrusion mobile (désactivation de l'épinglage de certificat, vidage de la mémoire, contournement de l'authentification biométrique) sans nécessiter de code JavaScript manuel.

---

## 📥 Installation et configuration de l'appareil

### Installation des outils hôtes
```bash
pip install frida frida-tools objection
```
### Configuration de l'appareil Android (`frida-server`)
Téléchargez le `frida-server` correspondant à la version de votre hôte Frida et à l'architecture du processeur de votre appareil (x86_64, arm64, etc.) :
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
### Configuration de l'appareil iOS
Sur un appareil iOS jailbreaké, ajoutez le référentiel Frida dans Cydia/Sileo (`https://build.frida.re`) et installez le package `Frida`.

---

## 🚀 Fondamentaux de Frida CLI

### Liste des appareils et des processus en cours d'exécution
```bash
# List all connected USB devices
frida-ls-devices

# List all running applications on USB-connected Android/iOS
frida-ps -Uai

# List all system processes on USB device
frida-ps -U
```
### Attachement ou apparition
- **Joindre (`-n` ou `-p`) :** Se connecte à une application déjà en cours d'exécution.
- **Spawn (`-f`) :** Démarre l'application à partir du démarrage anticipé, en suspendant l'exécution avant l'exécution des constructeurs initiaux :
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