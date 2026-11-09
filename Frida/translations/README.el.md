<!-- language-home-navigation -->
[🏠 Ελληνική Αρχική Σελίδα](../../README.el.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 📱 Frida & Objection - Ολοκληρωμένο Cheatsheet

[🇹🇷 Türkçe Sürüm](μεταφράσεις/README.tr.md)
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
  <b>📱 Κιτ εργαλειοθήκης δυναμικών δυαδικών οργάνων βιομηχανικού προτύπου για σύνδεση με μέθοδο εκτέλεσης Android και iOS, παράκαμψη καρφίτσωμα SSL, αποφυγή root/jailbreak και ανάλυση μνήμης</b>
</p>

---

## 📋 Πίνακας περιεχομένων

- [Επισκόπηση & Αρχιτεκτονική](#-επισκόπηση--αρχιτεκτονική)
- [Εγκατάσταση & Ρύθμιση συσκευής](#-εγκατάσταση--ρύθμιση συσκευής)
  - [Εγκατάσταση εργαλείων κεντρικού υπολογιστή] (#host-tools-installation)
  - [Ρύθμιση συσκευής Android (`frida-server`)](#android-device-setup-frida-server)
  - [Ρύθμιση συσκευής iOS](#ios-device-setup)
- [Frida CLI Fundamentals](#-frida-cli-fundamentals)
  - [Listing Devices and Running Processes](#listing-devices-and-running-processes)
  - [Attaching vs Spawning](#attaching-vs-spawning)
- [Σενάρια προσάρτησης JavaScript](#-javascript-hooking-scripts)
  - [Bypassing Android SSL Pinning](#bypassing-android-ssl-pinning)
  - [Bypassing Root Detection] (#bypassing-root-detection)
  - [Παρακολούθηση μεθόδου Java & παραβίαση επιχειρημάτων](#java-method-interception--argument-tampering)
- [Objection Runtime Toolkit](#-objection-runtime-toolkit)
  - [Έναρξη συνεδρίας αντιρρήσεων](#starting-an-objection-session)
  - [Καρφίτσωμα SSL με μία εντολή και παράκαμψη ρίζας](#one-command-ssl-pinning--root-bypass)
  - [Exploring Keystore, SQLite, and Shared Preferences](#exploring-keystore-sqlite-and-shared-preferences)
  - [Μαθήματα Hooking and Listing Activities](#hooking-classes-and-listing-activities)
- [Πίνακας γρήγορης αναφοράς] (#-ταχεία αναφορά-πίνακας)
- [Defensive Considerations & Anti-Frida](#-defensive-considerations--anti-frida)
- [Νομική αποποίηση ευθυνών](#-νομική αποποίηση ευθυνών)

---

## 🎯 Επισκόπηση & Αρχιτεκτονική

Η **Frida** εισάγει το χρόνο εκτέλεσης JavaScript V8 της Google σε στοχευόμενες διαδικασίες σε Android, iOS, Windows, macOS και Linux. Εξουσιοδοτεί τους ερευνητές ασφάλειας να:
- Συνδέστε αυθαίρετες εγγενείς λειτουργίες (`libc`, OpenSSL) και διαχειριζόμενες (`Java`, `Objective-C`, `Swift`).
- Επιθεωρήστε και τροποποιήστε τις παραμέτρους μνήμης και λειτουργίας εν κινήσει.
- Ανιχνεύστε κρυπτογραφικά API και αποκρυπτογραφήστε την κρυπτογραφημένη κίνηση στη μνήμη.

Το **Objection** είναι ένα πλαίσιο αξιολόγησης χρόνου εκτέλεσης ασφάλειας για κινητά που υποστηρίζεται από τη Frida που αυτοματοποιεί κοινές εργασίες διεκπεραίωσης κινητών (απενεργοποίηση καρφιτσώματος πιστοποιητικού, απόρριψη μνήμης, παράκαμψη βιομετρικού ελέγχου ταυτότητας) χωρίς να απαιτείται χειροκίνητος κώδικας JavaScript.

---

## 📥 Εγκατάσταση & Ρύθμιση συσκευής

### Εγκατάσταση εργαλείων κεντρικού υπολογιστή
```bash
pip install frida frida-tools objection
```
### Ρύθμιση συσκευής Android (`frida-server`)
Κατεβάστε το `frida-server` που ταιριάζει με την έκδοση του κεντρικού υπολογιστή Frida και την αρχιτεκτονική CPU της συσκευής σας (x86_64, arm64, κ.λπ.):
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
### Ρύθμιση συσκευής iOS
Σε μια συσκευή iOS με jailbrock, προσθέστε το αποθετήριο Frida στο Cydia / Sileo (`https://build.frida.re`) και εγκαταστήστε το πακέτο `Frida`.

---

## 🚀 Frida CLI Fundamentals

### Συσκευές λίστας και διεργασίες που εκτελούνται
```bash
# List all connected USB devices
frida-ls-devices

# List all running applications on USB-connected Android/iOS
frida-ps -Uai

# List all system processes on USB device
frida-ps -U
```
### Προσάρτηση έναντι ωοτοκίας
- **Επισύναψη (`-n` ή `-p`):** Επισυνάπτεται σε μια εφαρμογή που ήδη εκτελείται.
- **Spawn (`-f`):** Ξεκινά την εφαρμογή από την πρώιμη εκκίνηση, διακόπτοντας την εκτέλεση πριν από την εκτέλεση των αρχικών κατασκευαστών:
```bash
# Attach to running process
frida -U -n com.example.app -l hook.js

# Spawn process with early instrumentation
frida -U -f com.example.app -l hook.js
```
---

## 💻 Σενάρια σύνδεσης JavaScript

### Παράκαμψη του καρφιτσώματος Android SSL
Σενάριο παράκαμψης Universal TrustManager (`ssl_bypass.js`):
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
Τρέξτε με τη Φρίντα:
```bash
frida -U -f com.example.app -l ssl_bypass.js
```
### Παράκαμψη ανίχνευσης ρίζας
Hook standard RootBeer / Κοινοί έλεγχοι:
```javascript
Java.perform(function() {
    var RootBeer = Java.use("com.scottyab.rootbeer.RootBeer");
    RootBeer.isRooted.implementation = function() {
        console.log("[+] Overriding RootBeer.isRooted() -> false");
        return false;
    };
});
```
### Παρακολούθηση μεθόδου Java & παραβίαση επιχειρημάτων
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

## 🛠️ Εργαλειοθήκη χρόνου εκτέλεσης αντιρρήσεων

Η αντίρρηση επιτρέπει την αβίαστη δοκιμή κινητού μέσω ενός διαδραστικού κελύφους REPL:

### Έναρξη μιας συνεδρίας αντιρρήσεων
```bash
objection -g com.example.app explore
```
### Καρφίτσωμα SSL με μία εντολή και παράκαμψη ρίζας
Μέσα στο κέλυφος αντιρρήσεων:
```text
# Disable SSL Pinning immediately
android sslpinning disable

# Disable Root Detection checks
android root disable

# iOS equivalents
ios sslpinning disable
ios jailbreak disable
```
### Εξερεύνηση Keystore, SQLite και Shared Preferences
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
### Μαθήματα Hooking και Δραστηριότητες Καταχώρισης
```text
# Search classes matching keyword
android hooking search classes auth

# Watch method invocations in real time
android hooking watch class_method com.example.app.Auth.login --dump-args --dump-return
```
---

## ⚡ Γρήγορος Πίνακας Αναφοράς

| Στόχος | Διοίκηση Φρίντα | Εντολή αντιρρήσεων |
|-----------|---------------|-------------------|
| **Λίστα εφαρμογών USB** | `frida-ps -Uai` | `frida-ps -Uai` |
| **Spawn App with Hook**| `frida -U -f <PKG> -l hook.js` | `objection -g <PKG> explore` |
| **Παράκαμψη καρφιτσώματος SSL** | `frida -U -f <PKG> -l ssl_bypass.js` | `android sslpinning disable` |
| **Παράκαμψη ριζικού ελέγχου** | Προσαρμοσμένο σενάριο Java hook | `android root disable` |
| **Απόθεση συμβολοσειρών μνήμης**| `frida-dump` | `memory dump all strings.txt` |
| **Κατάλογος Δραστηριοτήτων** | `frida-ps` | `android hooking list activities` |
| **Μέθοδοι Κατηγορίας Ιχνών**| `frida-trace -U -j "com.pkg.Class!*"` | `android hooking watch class ...` |

---

## 🛡️ Αμυντικές Θεωρήσεις & Αντι-Φρίντα

1. **Σάρωση θύρας για Frida:** Ελέγξτε για ανοιχτή θύρα localhost `27042` ή εξετάστε το `/proc/self/maps` για `frida-agent.so`.
2. **Εγγενής προστασία Ptrace:** Καλέστε το `ptrace(PTRACE_TRACEME, 0, 1, 0)` σε εγγενείς βιβλιοθήκες C/JNI για να αποκλείσετε την προσάρτηση των προγραμμάτων εντοπισμού σφαλμάτων και των μηχανών οργάνων.

---

## ⚠️ Νομική αποποίηση ευθύνης

> [!ΠΡΟΣΟΧΗ]
> Τα Frida και Objection είναι εξειδικευμένα εργαλεία δοκιμών αντίστροφης μηχανικής και διείσδυσης που έχουν σχεδιαστεί αυστηρά για εξουσιοδοτημένες αξιολογήσεις ασφαλείας. Η χρήση εργαλείων σε εφαρμογές τρίτων για κινητά χωρίς άδεια παραβιάζει τους Όρους Παροχής Υπηρεσιών και τους νόμους περί πνευματικής ιδιοκτησίας.