<!-- language-home-navigation -->
[🏠 Página Principal Española](../../README.es.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 📱 Frida y la objeción - Hoja de referencia completa

[🇹🇷 Türkçe Sürüm](traducciones/README.tr.md)
```
  ███████╗██████╗ ██╗██████╗  █████╗      ██████╗ ██████╗      ██╗███████╗ ██████╗████████╗██╗ ██████╗ ███╗   ██╗
  ██╔════╝██╔══██╗██║██╔══██╗██╔══██╗    ██╔═══██╗██╔══██╗     ██║██╔════╝██╔════╝╚══██╔══╝██║██╔═══██╗████╗  ██║
  █████╗  ██████╔╝██║██║  ██║███████║    ██║   ██║██████╔╝     ██║█████╗  ██║        ██║   ██║██║   ██║██╔██╗ ██║
  ██╔══╝  ██╔══██╗██║██║  ██║██╔══██║    ██║   ██║██╔══██╗██   ██║██╔══╝  ██║        ██║   ██║██║   ██║██║╚██╗██║
  ██║     ██║  ██║██║██████╔╝██║  ██║    ╚██████╔╝██████╔╝╚█████╔╝███████╗╚██████╗   ██║   ██║╚██████╔╝██║ ╚████║
  ╚═╝     ╚═╝  ╚═╝╚═╝╚═════╝ ╚═╝  ╚═╝     ╚═════╝ ╚═════╝  ╚════╝ ╚══════╝ ╚═════╝   ╚═╝   ╚═╝ ╚═════╝ ╚═╝  ╚═══╝
           Dynamic Binary Instrumentation & Mobile Security Assessment
```
<p align="centro">
  <img src="https://img.shields.io/badge/Frida-Runtime_Hooking-blue?style=for-the-badge" alt="Frida">
  <img src="https://img.shields.io/badge/Objection-Mobile_Exploration-orange?style=for-the-badge" alt="Objeción">
  <img src="https://img.shields.io/badge/Platforms-Android_%7C_iOS-green?style=for-the-badge" alt="Android | iOS">
  <img src="https://img.shields.io/badge/Category-Mobile_Security-red?style=for-the-badge" alt="Seguridad móvil">
</p>

<p align="centro">
  <b>📱 Kit de herramientas de instrumentación binaria dinámica estándar de la industria para conexión de métodos de tiempo de ejecución de Android e iOS, omisión de fijación de SSL, evasión de root/jailbreak y análisis de memoria</b>
</p>

---

## 📋 Tabla de contenidos

- [Descripción general y arquitectura] (#-descripción general--arquitectura)
- [Instalación y configuración del dispositivo] (#-instalación--configuración del dispositivo)
  - [Instalación de herramientas de host](#host-tools-installation)
  - [Configuración del dispositivo Android (`frida-server`)](#android-device-setup-frida-server)
  - [Configuración del dispositivo iOS](#ios-device-setup)
- [Fundamentos de Frida CLI](#-frida-cli-fundamentos)
  - [Listado de dispositivos y procesos en ejecución](#listado-de-dispositivos-y-procesos-en-ejecución)
  - [Adjuntar versus generar] (#adjuntar-vs-generar)
- [Secuencias de comandos de enlace de JavaScript](#-javascript-hooking-scripts)
  - [Omitir la fijación SSL de Android](#bypassing-android-ssl-pinning)
  - [Omitir la detección de raíz] (#bypassing-root-detection)
  - [Intercepción del método Java y manipulación de argumentos] (#intercepción-del-método-java--manipulación-de-argumentos)
- [Kit de herramientas de ejecución de objeciones](#-objection-runtime-toolkit)
  - [Iniciar una sesión de objeción](#iniciar-una-sesión-de-objeción)
  - [Fijación SSL con un solo comando y omisión de raíz] (#one-command-ssl-pinning--root-bypass)
  - [Explorando el almacén de claves, SQLite y las preferencias compartidas](#explorando-el-almacén-de-claves-sqlite-y-las-preferencias-compartidas)
  - [Clases de enganche y actividades de listado](#clases-de-enganche-y-actividades-de-listado)
- [Tabla de referencia rápida](#-tabla-de-referencia-rápida)
- [Consideraciones defensivas y Anti-Frida](#-consideraciones-defensivas--anti-frida)
- [Aviso legal](#-aviso-legal)

---

## 🎯 Descripción general y arquitectura

**Frida** inyecta el tiempo de ejecución JavaScript V8 de Google en procesos de destino en Android, iOS, Windows, macOS y Linux. Permite a los investigadores de seguridad:
- Enganche funciones arbitrarias nativas (`libc`, OpenSSL) y administradas (`Java`, `Objective-C`, `Swift`).
- Inspeccionar y modificar parámetros de memoria y funciones sobre la marcha.
- Rastree API criptográficas y descifre el tráfico cifrado en la memoria.

**Objetion** es un marco de evaluación de tiempo de ejecución de seguridad móvil impulsado por Frida que automatiza tareas comunes de pentesting móvil (deshabilitar la fijación de certificados, volcar memoria, omitir la autenticación biométrica) sin requerir código JavaScript manual.

---

## 📥 Instalación y configuración del dispositivo

### Instalación de herramientas de host
```bash
pip install frida frida-tools objection
```
### Configuración del dispositivo Android (`frida-server`)
Descargue el `frida-server` que coincida con la versión de Frida de su host y la arquitectura de CPU del dispositivo (x86_64, arm64, etc.):
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
### Configuración del dispositivo iOS
En un dispositivo iOS con jailbreak, agregue el repositorio Frida en Cydia/Sileo (`https://build.frida.re`) e instale el paquete `Frida`.

---

## 🚀 Fundamentos de Frida CLI

### Listado de dispositivos y procesos en ejecución
```bash
# List all connected USB devices
frida-ls-devices

# List all running applications on USB-connected Android/iOS
frida-ps -Uai

# List all system processes on USB device
frida-ps -U
```
### Adjuntar vs desovar
- **Adjuntar (`-n` o `-p`):** Se adjunta a una aplicación que ya se está ejecutando.
- **Spawn (`-f`):** Inicia la aplicación desde el inicio temprano, pausando la ejecución antes de que se ejecuten los constructores iniciales:
```bash
# Attach to running process
frida -U -n com.example.app -l hook.js

# Spawn process with early instrumentation
frida -U -f com.example.app -l hook.js
```
---

## 💻 Scripts de enlace de JavaScript

### Omitir la fijación SSL de Android
Script de omisión de Universal TrustManager (`ssl_bypass.js`):
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