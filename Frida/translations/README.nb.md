<!-- language-home-navigation -->
[🏠 Norsk Hovedside](../../README.nb.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇬🇧 English](../README.md)

---


# 📱 Frida og innvending - Komplett jukseark

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
  <img src="https://img.shields.io/badge/Objection-Mobile_Exploration-orange?style=for-the-badge" alt="Innsigelse">
  <img src="https://img.shields.io/badge/Platforms-Android_%7C_iOS-green?style=for-the-badge" alt="Android | iOS">
  <img src="https://img.shields.io/badge/Category-Mobile_Security-red?style=for-the-badge" alt="Mobilsikkerhet">
</p>

<p align="center">
  <b>📱 Industristandard dynamisk binær instrumentering verktøysett for Android og iOS runtime method hooking, SSL Pinning bypass, root/jailbreak evasion og minneanalyse</b>
</p>

---

## 📋 Innholdsfortegnelse

- [Oversikt og arkitektur](#-oversikt--arkitektur)
- [Installasjon og enhetsoppsett](#-installasjon--enhetsoppsett)
  - [Installasjon av vertsverktøy](#host-tools-installation)
  - [Android-enhetsoppsett (`frida-server`)](#android-device-setup-frida-server)
  - [iOS-enhetsoppsett](#ios-device-setup)
- [Frida CLI Fundamentals](#-frida-cli-fundamentals)
  – [Listing Devices and Running Processes](#listing-devices-and-running-processes)
  - [Attaching vs Spawning](#attaching-vs-gyting)
- [JavaScript Hooking Scripts](#-javascript-hooking-scripts)
  - [Omgå Android SSL-pinning](#bypassing-android-ssl-pinning)
  - [Omgå rotdeteksjon](#bypassing-root-deteksjon)
  - [Java Method Interception & Argument Tampering](#java-method-interception--argument-tampering)
- [Objection Runtime Toolkit](#-objection-runtime-toolkit)
  - [Starter en innvendingsøkt](#starting-an-objection-session)
  - [One-Command SSL Pinning & Root Bypass](#one-command-ssl-pinning--root-bypass)
  - [Utforsking av Keystore, SQLite og Shared Preferences](#exploring-keystore-sqlite-and-shared-preferences)
  - [Hooking Classes and Listing Activities](#hooking-classes-and-listing-activities)
- [Hurtigreferansetabell](#-hurtigreferansetabell)
- [Defensive Considerations & Anti-Frida](#-defensive-considerations--anti-frida)
– [Juridisk ansvarsfraskrivelse](#-juridisk ansvarsfraskrivelse)

---

## 🎯 Oversikt og arkitektur

**Frida** injiserer Googles V8 JavaScript-kjøretid i målprosesser på Android, iOS, Windows, macOS og Linux. Det gir sikkerhetsforskere mulighet til å:
- Hook vilkårlige opprinnelige (`libc`, OpenSSL) og administrerte (`Java`, `Objective-C`, `Swift`) funksjoner.
- Inspiser og modifiser minne- og funksjonsparametere i farten.
- Spor kryptografiske APIer og dekrypter kryptert trafikk i minnet.

**Innsigelse** er et vurderingsrammeverk for mobil sikkerhetskjøring drevet av Frida som automatiserer vanlige mobile pentestingsoppgaver (deaktivere sertifikatfesting, dumping av minne, omgå biometrisk autentisering) uten å kreve manuell JavaScript-kode.

---

## 📥 Installasjon og enhetsoppsett

### Installasjon av vertsverktøy
```bash
pip install frida frida-tools objection
```
### Android-enhetsoppsett (`frida-server`)
Last ned `frida-server` som matcher din verts Frida-versjon og enhetens CPU-arkitektur (x86_64, arm64, etc.):
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
### iOS-enhetsoppsett
På en jailbroken iOS-enhet, legg til Frida-depotet i Cydia / Sileo (`https://build.frida.re`) og installer `Frida`-pakken.

---

## 🚀 Frida CLI Fundamentals

### Oppføring av enheter og kjørende prosesser
```bash
# List all connected USB devices
frida-ls-devices

# List all running applications on USB-connected Android/iOS
frida-ps -Uai

# List all system processes on USB device
frida-ps -U
```
### Feste vs gyting
- **Legg ved (`-n` eller `-p`):** Festes til en app som allerede kjører.
- **Spawn (`-f`):** Starter applikasjonen fra tidlig oppstart, og setter kjøringen på pause før de første konstruktørene kjører:
```bash
# Attach to running process
frida -U -n com.example.app -l hook.js

# Spawn process with early instrumentation
frida -U -f com.example.app -l hook.js
```
---

## 💻 JavaScript Hooking Scripts

### Omgå Android SSL-festing
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
Løp med Frida:
```bash
frida -U -f com.example.app -l ssl_bypass.js
```
### Omgå rotdeteksjon
Hook standard RootBeer / vanlige sjekker:
```javascript
Java.perform(function() {
    var RootBeer = Java.use("com.scottyab.rootbeer.RootBeer");
    RootBeer.isRooted.implementation = function() {
        console.log("[+] Overriding RootBeer.isRooted() -> false");
        return false;
    };
});
```
### Java-metodeavskjæring og manipulering av argumenter
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

Innvending muliggjør uanstrengt mobiltesting gjennom et interaktivt REPL-skall:

### Starte en innsigelsesøkt
```bash
objection -g com.example.app explore
```
### Enkommando SSL-festing og rotomgåelse
Inne i innsigelsesskallet:
```text
# Disable SSL Pinning immediately
android sslpinning disable

# Disable Root Detection checks
android root disable

# iOS equivalents
ios sslpinning disable
ios jailbreak disable
```
### Utforsker Keystore, SQLite og Shared Preferences
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
### Hooking-kurs og noteringsaktiviteter
```text
# Search classes matching keyword
android hooking search classes auth

# Watch method invocations in real time
android hooking watch class_method com.example.app.Auth.login --dump-args --dump-return
```
---

## ⚡ Hurtigreferansetabell

| Mål | Frida Command | Innvendingskommando |
|--------|---------------|----------------|
| **Liste USB-apper** | `frida-ps -Uai` | `frida-ps -Uai` |
| **Spawn-app med krok**| `frida -U -f <PKG> -l hook.js` | `objection -g <PKG> explore` |
| **Omgå SSL-festing** | `frida -U -f <PKG> -l ssl_bypass.js` | `android sslpinning disable` |
| **Omgå rotsjekk** | Egendefinert Java hook script | `android root disable` |
| **Dump minnestrenger**| `frida-dump` | `memory dump all strings.txt` |
| **Liste aktiviteter** | `frida-ps` | `android hooking list activities` |
| **Sporklassemetoder**| `frida-trace -U -j "com.pkg.Class!*"` | `android hooking watch class ...` |

---

## 🛡️ Defensive betraktninger og anti-Frida

1. **Portskanning for Frida:** Se etter åpen localhost-port `27042` eller undersøk `/proc/self/maps` for `frida-agent.so`.
2. **Native Ptrace Protection:** Ring `ptrace(PTRACE_TRACEME, 0, 1, 0)` i native C/JNI-biblioteker for å blokkere feilsøkingsprogrammer og instrumenteringsmotorer fra å kobles til.

---

## ⚠️ Juridisk ansvarsfraskrivelse

> [!FORSIKTIG]
> Frida og Objection er spesialiserte verktøy for revers engineering og penetrasjonstesting designet strengt for autoriserte sikkerhetsvurderinger. Instrumentering av tredjeparts mobilapplikasjoner uten tillatelse bryter med vilkår for bruk og lover om immaterielle rettigheter.