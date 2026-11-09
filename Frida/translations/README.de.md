<!-- language-home-navigation -->
[🏠 Deutsche Startseite](../../README.de.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 📱 Frida & Einspruch – Vollständiger Cheatsheet

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
  <img src="https://img.shields.io/badge/Objection-Mobile_Exploration-orange?style=for-the-badge" alt="Einspruch">
  <img src="https://img.shields.io/badge/Platforms-Android_%7C_iOS-green?style=for-the-badge" alt="Android | iOS">
  <img src="https://img.shields.io/badge/Category-Mobile_Security-red?style=for-the-badge" alt="Mobile Sicherheit">
</p>

<p align="center">
  <b>📱 Branchenstandardisiertes dynamisches binäres Instrumentierungs-Toolkit für Android- und iOS-Laufzeitmethoden-Hooking, SSL-Pinning-Umgehung, Root-/Jailbreak-Umgehung und Speicheranalyse</b>
</p>

---

## 📋 Inhaltsverzeichnis

- [Übersicht & Architektur](#-overview--architecture)
- [Installation & Geräteeinrichtung](#-installation--device-setup)
  - [Installation der Host-Tools](#host-tools-installation)
  - [Android-Geräte-Setup (`frida-server`)](#android-device-setup-frida-server)
  - [iOS-Geräte-Setup](#ios-device-setup)
- [Frida CLI-Grundlagen](#-frida-cli-fundamentals)
  - [Geräte und laufende Prozesse auflisten](#listing-devices-and-running-processes)
  - [Anhängen vs. Spawnen](#attaching-vs-spawning)
- [JavaScript-Hooking-Skripte](#-javascript-hooking-scripts)
  - [Android SSL-Pinning umgehen](#bypassing-android-ssl-pinning)
  - [Root-Erkennung umgehen](#bypassing-root-detection)
  - [Abfangen von Java-Methoden und Manipulation von Argumenten](#java-method-interception--argument-tampering)
- [Objection Runtime Toolkit](#-objection-runtime-toolkit)
  - [Einspruchssitzung starten](#starting-an-objection-session)
  - [SSL-Pinning und Root-Bypass mit einem Befehl](#one-command-ssl-pinning--root-bypass)
  - [Keystore, SQLite und Shared Preferences erkunden](#exploring-keystore-sqlite-and-shared-preferences)
  - [Hooking-Kurse und Listing-Aktivitäten](#hooking-classes-and-listing-activities)
- [Kurzreferenztabelle](#-quick-reference-table)
- [Verteidigungsüberlegungen und Anti-Frida](#-defensive-überlegungen--anti-frida)
- [Rechtlicher Haftungsausschluss](#-legal-disclaimer)

---

## 🎯 Übersicht & Architektur

**Frida** fügt Googles V8-JavaScript-Laufzeitumgebung in Zielprozesse auf Android, iOS, Windows, macOS und Linux ein. Es ermöglicht Sicherheitsforschern:
- Haken Sie beliebige native (`libc`, OpenSSL) und verwaltete (`Java`, `Objective-C`, `Swift`) Funktionen ein.
- Überprüfen und ändern Sie Speicher- und Funktionsparameter im Handumdrehen.
- Verfolgen Sie kryptografische APIs und entschlüsseln Sie verschlüsselten Datenverkehr im Speicher.

**Objection** ist ein von Frida bereitgestelltes Laufzeitbewertungs-Framework für die Sicherheit mobiler Geräte, das häufige Pentesting-Aufgaben für Mobilgeräte automatisiert (Deaktivieren des Anheftens von Zertifikaten, Speicherauszug, Umgehen der biometrischen Authentifizierung), ohne dass manueller JavaScript-Code erforderlich ist.

---

## 📥 Installation und Geräteeinrichtung

### Host-Tools-Installation
```bash
pip install frida frida-tools objection
```
### Android-Geräte-Setup (`frida-server`)
Laden Sie den `frida-server` herunter, der zu Ihrer Host-Frida-Version und Geräte-CPU-Architektur (x86_64, arm64 usw.) passt:
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
### iOS-Geräte-Setup
Fügen Sie auf einem iOS-Gerät mit Jailbreak das Frida-Repository in Cydia/Sileo (`https://build.frida.re`) hinzu und installieren Sie das `Frida`-Paket.

---

## 🚀 Frida CLI-Grundlagen

### Auflisten von Geräten und laufenden Prozessen
```bash
# List all connected USB devices
frida-ls-devices

# List all running applications on USB-connected Android/iOS
frida-ps -Uai

# List all system processes on USB device
frida-ps -U
```
### Anheften vs. Spawnen
- **Anhängen (`-n` oder `-p`):** Wird an eine bereits laufende App angehängt.
- **Spawn (`-f`):** Startet die Anwendung beim frühen Start und hält die Ausführung an, bevor die ersten Konstruktoren ausgeführt werden:
```bash
# Attach to running process
frida -U -n com.example.app -l hook.js

# Spawn process with early instrumentation
frida -U -f com.example.app -l hook.js
```
---

## 💻 JavaScript-Hooking-Skripte

### Android SSL-Pinning umgehen
Universelles TrustManager-Umgehungsskript (`ssl_bypass.js`):
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
Laufen Sie mit Frida:
```bash
frida -U -f com.example.app -l ssl_bypass.js
```
### Root-Erkennung umgehen
Hook-Standard-RootBeer / Allgemeine Prüfungen:
```javascript
Java.perform(function() {
    var RootBeer = Java.use("com.scottyab.rootbeer.RootBeer");
    RootBeer.isRooted.implementation = function() {
        console.log("[+] Overriding RootBeer.isRooted() -> false");
        return false;
    };
});
```
### Abfangen von Java-Methoden und Manipulation von Argumenten
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

Objection ermöglicht müheloses mobiles Testen über eine interaktive REPL-Shell:

### Einspruchssitzung starten
```bash
objection -g com.example.app explore
```
### SSL-Pinning und Root-Umgehung mit einem Befehl
In der Einspruchshülle:
```text
# Disable SSL Pinning immediately
android sslpinning disable

# Disable Root Detection checks
android root disable

# iOS equivalents
ios sslpinning disable
ios jailbreak disable
```
### Erkundung von Keystore, SQLite und freigegebenen Einstellungen
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
### Hooking-Kurse und Listing-Aktivitäten
```text
# Search classes matching keyword
android hooking search classes auth

# Watch method invocations in real time
android hooking watch class_method com.example.app.Auth.login --dump-args --dump-return
```
---

## ⚡ Kurzreferenztabelle

| Ziel | Frida-Befehl | Einspruchsbefehl |
|-----------|---------------|-------------------|
| **USB-Apps auflisten** | `frida-ps -Uai` | `frida-ps -Uai` |
| **Spawn-App mit Hook**| `frida -U -f <PKG> -l hook.js` | `objection -g <PKG> explore` |
| **SSL-Pinning umgehen** | `frida -U -f <PKG> -l ssl_bypass.js` | `android sslpinning disable` |
| **Root-Prüfung umgehen** | Benutzerdefiniertes Java-Hook-Skript | `android root disable` |
| **Speicherzeichenfolgen sichern**| `frida-dump` | `memory dump all strings.txt` |
| **Aktivitäten auflisten** | `frida-ps` | `android hooking list activities` |
| **Trace-Klassenmethoden**| `frida-trace -U -j "com.pkg.Class!*"` | `android hooking watch class ...` |

---

## 🛡️ Defensive Überlegungen und Anti-Frida

1. **Port-Scanning für Frida:** Überprüfen Sie den offenen Localhost-Port `27042` oder prüfen Sie `/proc/self/maps` auf `frida-agent.so`.
2. **Nativer Ptrace-Schutz:** Rufen Sie `ptrace(PTRACE_TRACEME, 0, 1, 0)` in nativen C/JNI-Bibliotheken auf, um das Anhängen von Debuggern und Instrumentierungs-Engines zu verhindern.

---

## ⚠️ Rechtlicher Haftungsausschluss

> [!ACHTUNG]
> Frida und Objection sind spezialisierte Reverse-Engineering- und Penetrationstest-Tools, die ausschließlich für autorisierte Sicherheitsbewertungen entwickelt wurden. Die Instrumentierung mobiler Anwendungen Dritter ohne Genehmigung verstößt gegen die Nutzungsbedingungen und die Gesetze zum Schutz des geistigen Eigentums.