<!-- language-home-navigation -->
[🏠 Русская главная страница](../../README.ru.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 📱 Фрида и возражение — полная шпаргалка

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
<p выравнивание="центр">
  <img src="https://img.shields.io/badge/Frida-Runtime_Hooking-blue?style=for-the-badge" alt="Frida">
  <img src="https://img.shields.io/badge/Objection-Mobile_Exploration-orange?style=for-the-badge" alt="Objection">
  <img src="https://img.shields.io/badge/Platforms-Android_%7C_iOS-green?style=for-the-badge" alt="Android | iOS">
  <img src="https://img.shields.io/badge/Category-Mobile_Security-red?style=for-the-badge" alt="Мобильная безопасность">
</p>

<p выравнивание="центр">
  <b>📱 Стандартный набор инструментов динамического двоичного инструментария для перехвата методов среды выполнения Android и iOS, обхода закрепления SSL, обхода root/jailbreak и анализа памяти</b>
</p>

---

## 📋 Содержание

- [Обзор и архитектура](#-обзор--архитектура)
- [Установка и настройка устройства](#-installation--device-setup)
  - [Установка Host Tools](#host-tools-installation)
  - [Настройка устройства Android (`frida-server`)](#android-device-setup-frida-server)
  - [Настройка устройства iOS](#ios-device-setup)
- [Основы Frida CLI](#-frida-cli-fundamentals)
  - [Список устройств и запущенных процессов](#listing-devices-and-running-processes)
  - [Прикрепление против появления](#attaching-vs-spawning)
- [Скрипты перехвата JavaScript](#-javascript-hooking-scripts)
  - [Обход закрепления Android SSL](#bypassing-android-ssl-pinning)
  - [Обход обнаружения корня](#bypassing-root-detection)
  - [Перехват метода Java и подделка аргументов](#java-method-interception--argument-tampering)
- [Набор инструментов для выполнения возражений] (#-objection-runtime-toolkit)
  - [Начало сессии возражений](#starting-an-objection-session)
  - [Закрепление SSL и обход корня одной командой](#one-command-ssl-pinning--root-bypass)
  - [Изучение хранилища ключей, SQLite и общих настроек](#exploring-keystore-sqlite-and-shared-preferences)
  - [Классы перехвата и действия по листингу](#hooking-classes-and-listing-activities)
- [Краткая справочная таблица](#-краткая справочная-таблица)
- [Защитные соображения и антифрида](#-защитные-соображения--анти-фрида)
- [Юридическая оговорка](#-юридическая оговорка)

---

## 🎯 Обзор и архитектура

**Frida** внедряет среду выполнения JavaScript V8 от Google в целевые процессы на Android, iOS, Windows, macOS и Linux. Это дает исследователям безопасности возможность:
- Перехват произвольных собственных (`libc`, OpenSSL) и управляемых (`Java`, `Objective-C`, `Swift`) функций.
- Проверяйте и изменяйте параметры памяти и функций на лету.
- Отслеживайте криптографические API и расшифровывайте зашифрованный трафик в памяти.

**Objection** — это среда оценки безопасности мобильных устройств на базе Frida, которая автоматизирует распространенные задачи мобильного тестирования на проникновение (отключение закрепления сертификата, сброс памяти, обход биометрической аутентификации) без необходимости использования ручного кода JavaScript.

---

## 📥 Установка и настройка устройства

### Установка инструментов хоста
```bash
pip install frida frida-tools objection
```
### Настройка устройства Android (`frida-server`)
Загрузите `frida-server`, соответствующий версии Frida вашего хоста и архитектуре ЦП устройства (x86_64, Arm64 и т. д.):
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
### Настройка устройства iOS
На взломанном iOS-устройстве добавьте репозиторий Frida в Cydia/Sileo (`https://build.frida.re`) и установите пакет `Frida`.

---

## 🚀 Основы Frida CLI

### Получение списка устройств и запущенных процессов
```bash
# List all connected USB devices
frida-ls-devices

# List all running applications on USB-connected Android/iOS
frida-ps -Uai

# List all system processes on USB device
frida-ps -U
```
### Прикрепление против появления
- **Прикрепить (`-n` или `-p`):** прикрепляется к уже запущенному приложению.
- **Spawn (`-f`):** запускает приложение с ранней загрузки, приостанавливая выполнение до выполнения начальных конструкторов:
```bash
# Attach to running process
frida -U -n com.example.app -l hook.js

# Spawn process with early instrumentation
frida -U -f com.example.app -l hook.js
```
---

## 💻 Скрипты перехвата JavaScript

### Обход закрепления SSL в Android
Скрипт обхода Universal TrustManager (`ssl_bypass.js`):
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