<!-- language-home-navigation -->
[🏠 Home Page Italiana](../../README.it.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 📱Frida e l'Obiezione - Cheatsheet completo

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
<palign="centro">
  <img src="https://img.shields.io/badge/Frida-Runtime_Hooking-blue?style=for-the-badge" alt="Frida">
  <img src="https://img.shields.io/badge/Objection-Mobile_Exploration-orange?style=for-the-badge" alt="Obiezione">
  <img src="https://img.shields.io/badge/Platforms-Android_%7C_iOS-green?style=for-the-badge" alt="Android | iOS">
  <img src="https://img.shields.io/badge/Category-Mobile_Security-red?style=for-the-badge" alt="Sicurezza mobile">
</p>

<palign="centro">
  <b>📱 Toolkit di strumentazione binaria dinamica standard del settore per l'hooking del metodo runtime Android e iOS, bypass del pinning SSL, evasione root/jailbreak e analisi della memoria</b>
</p>

---

## 📋 Sommario

- [Panoramica e architettura](#-panoramica--architettura)
- [Installazione e configurazione del dispositivo](#-installazione--configurazione del dispositivo)
  - [Installazione degli strumenti host](#installazione-strumenti-host)
  - [Configurazione dispositivo Android (`frida-server`)](#configurazione-dispositivo-android-frida-server)
  - [Configurazione dispositivo iOS](#configurazione-dispositivo-ios)
- [Frida CLI Fondamenti di base](#-frida-cli-fondamenti)
  - [Elenco dei dispositivi e dei processi in esecuzione](#elenco dei dispositivi e dei processi in esecuzione)
  - [Attaching vs Spawning](#attaching-vs-spawning)
- [Script di aggancio JavaScript](#-javascript-hooking-scripts)
  - [Ignorare il blocco SSL di Android](#bypassing-android-ssl-pinning)
  - [Evitare il rilevamento della radice](#evitare-rilevamento-root)
  - [Intercettazione metodo Java e manomissione argomento](#intercettazione-metodo Java--manomissione argomento)
- [Kit degli strumenti di runtime delle obiezioni](#-objection-runtime-toolkit)
  - [Avvio di una sessione di obiezione](#avvio-di-una-sessione-di-obiezione)
  - [Binning SSL con un comando e bypass root](#one-command-ssl-pinning--root-bypass)
  - [Esplorazione di Keystore, SQLite e Preferenze condivise](#exploring-keystore-sqlite-and-shared-preferences)
  - [Corsi di aggancio e attività di inserimento](#corsi-di-aggancio-e-attività-di-inserimento)
- [Tabella di riferimento rapido](#-tabella-di-riferimento-rapido)
- [Considerazioni difensive e anti-Frida](#-considerazioni-difensive--anti-frida)
- [Disclaimer legale](#-legal-disclaimer)

---

## 🎯 Panoramica e architettura

**Frida** inserisce il runtime JavaScript V8 di Google nei processi target su Android, iOS, Windows, macOS e Linux. Consente ai ricercatori di sicurezza di:
- Aggancio di funzioni native arbitrarie (`libc`, OpenSSL) e gestite (`Java`, `Objective-C`, `Swift`).
- Ispeziona e modifica al volo i parametri della memoria e delle funzioni.
- Traccia le API crittografiche e decrittografa il traffico crittografato in memoria.

**Objection** è un framework di valutazione del runtime di sicurezza mobile basato su Frida che automatizza le attività comuni di pentesting mobile (disabilitare il blocco dei certificati, scaricare la memoria, bypassare l'autenticazione biometrica) senza richiedere il codice JavaScript manuale.

---

## 📥 Installazione e configurazione del dispositivo

### Installazione degli strumenti host
```bash
pip install frida frida-tools objection
```
### Configurazione del dispositivo Android (`frida-server`)
Scarica l'`frida-server` corrispondente alla versione Frida del tuo host e all'architettura della CPU del dispositivo (x86_64, arm64, ecc.):
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
### Configurazione del dispositivo iOS
Su un dispositivo iOS jailbroken, aggiungi il repository Frida in Cydia / Sileo (`https://build.frida.re`) e installa il pacchetto `Frida`.

---

## 🚀Frida CLI Fondamenti

### Elenco dei dispositivi e dei processi in esecuzione
```bash
# List all connected USB devices
frida-ls-devices

# List all running applications on USB-connected Android/iOS
frida-ps -Uai

# List all system processes on USB device
frida-ps -U
```
### Attaccamento vs Generazione
- **Allega (`-n` o `-p`):** Si collega a un'app già in esecuzione.
- **Spawn (`-f`):** Avvia l'applicazione dall'avvio anticipato, sospendendo l'esecuzione prima che i costruttori iniziali eseguano:
```bash
# Attach to running process
frida -U -n com.example.app -l hook.js

# Spawn process with early instrumentation
frida -U -f com.example.app -l hook.js
```
---

## 💻 Script di aggancio JavaScript

### Bypassare il blocco SSL di Android
Script di bypass di Universal TrustManager (`ssl_bypass.js`):
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
Corri con Frida:
```bash
frida -U -f com.example.app -l ssl_bypass.js
```
### Bypassare il rilevamento delle radici
Aggancia RootBeer standard / Controlli comuni:
```javascript
Java.perform(function() {
    var RootBeer = Java.use("com.scottyab.rootbeer.RootBeer");
    RootBeer.isRooted.implementation = function() {
        console.log("[+] Overriding RootBeer.isRooted() -> false");
        return false;
    };
});
```
### Intercettazione del metodo Java e manomissione degli argomenti
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

L'obiezione consente test mobili senza sforzo attraverso una shell REPL interattiva:

### Avvio di una sessione di obiezione
```bash
objection -g com.example.app explore
```
### Blocco SSL con un solo comando e bypass root
All'interno del guscio delle obiezioni:
```text
# Disable SSL Pinning immediately
android sslpinning disable

# Disable Root Detection checks
android root disable

# iOS equivalents
ios sslpinning disable
ios jailbreak disable
```
### Esplorazione di Keystore, SQLite e Preferenze condivise
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
### Classi di aggancio e attività di elenco
```text
# Search classes matching keyword
android hooking search classes auth

# Watch method invocations in real time
android hooking watch class_method com.example.app.Auth.login --dump-args --dump-return
```
---

## ⚡ Tabella di consultazione rapida

| Obiettivo | Comando Frida | Comando di obiezione |
|-----------|---------------|-------------|
| **Elenco app USB** | `frida-ps -Uai` | `frida-ps -Uai` |
| **App di spawn con gancio**| `frida -U -f <PKG> -l hook.js` | `objection -g <PKG> explore` |
| **Ignora il blocco SSL** | `frida -U -f <PKG> -l ssl_bypass.js` | `android sslpinning disable` |
| **Ignora controllo root** | Script hook Java personalizzato | `android root disable` |
| **Scarica stringhe di memoria**| `frida-dump` | `memory dump all strings.txt` |
| **Elenco attività** | `frida-ps` | `android hooking list activities` |
| **Metodi della classe di traccia**| `frida-trace -U -j "com.pkg.Class!*"` | `android hooking watch class ...` |

---

## 🛡️ Considerazioni difensive e anti-Frida

1. **Scansione delle porte per Frida:** Controlla la porta `27042` dell'host locale aperta o esamina `/proc/self/maps` per `frida-agent.so`.
2. **Protezione Ptrace nativa:** chiama `ptrace(PTRACE_TRACEME, 0, 1, 0)` nelle librerie C/JNI native per impedire il collegamento di debugger e motori di strumentazione.

---

## ⚠️ Disclaimer legale

> [!ATTENZIONE]
> Frida e Objection sono strumenti specializzati di reverse engineering e test di penetrazione progettati rigorosamente per valutazioni di sicurezza autorizzate. L'utilizzo di applicazioni mobili di terze parti senza autorizzazione viola i termini di servizio e le leggi sulla proprietà intellettuale.