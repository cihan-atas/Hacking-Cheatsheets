<!-- language-home-navigation -->
[🏠 Ελληνική Αρχική Σελίδα](../../README.el.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🔍 Jadx & Apktool - Ολοκληρωμένο Cheatsheet

[🇹🇷 Türkçe Sürüm](μεταφράσεις/README.tr.md)
```
       ██╗ █████╗ ██████╗ ██╗  ██╗    ███████╗    █████╗ ██████╗ ██╗  ██╗████████╗ ██████╗  ██████╗ ██╗     
       ██║██╔══██╗██╔══██╗╚██╗██╔╝    ██╔════╝   ██╔══██╗██╔══██╗██║ ██╔╝╚══██╔══╝██╔═══██╗██╔═══██╗██║     
       ██║███████║██║  ██║ ╚███╔╝     ███████╗   ███████║██████╔╝█████╔╝    ██║   ██║   ██║██║   ██║██║     
  ██   ██║██╔══██║██║  ██║ ██╔██╗     ╚════██║   ██╔══██║██╔═══╝ ██╔═██╗    ██║   ██║   ██║██║   ██║██║     
  ╚█████╔╝██║  ██║██████╔╝██╔╝ ██╗    ███████║██╗██║  ██║██║     ██║  ██╗   ██║   ╚██████╔╝╚██████╔╝███████╗
   ╚════╝ ╚═╝  ╚═╝╚═════╝ ╚═╝  ╚═╝    ╚══════╝╚═╝╚═╝  ╚═╝╚═╝     ╚═╝  ╚═╝   ╚═╝    ╚═════╝  ╚═════╝ ╚══════╝
           Android APK Decompilation, Smali Patching & Repackaging
```
<p align="center">
  <img src="https://img.shields.io/badge/Jadx-DEX_to_Java-blue?style=for-the-badge" alt="Jadx">
  <img src="https://img.shields.io/badge/Apktool-Smali_Disassembler-orange?style=for-the-badge" alt="Apktool">
  <img src="https://img.shields.io/badge/Category-Mobile_Reverse_Engineering-green?style=for-the-badge" alt="Mobile RE">
  <img src="https://img.shields.io/badge/Platform-Android-purple?style=for-the-badge" alt="Android">
</p>

<p align="center">
  <b>🔍 Πλήρης οδηγός για την αντίστροφη μηχανική Android: αποσυμπίληση αρχείων APK/DEX/AAR σε αναγνώσιμο πηγαίο κώδικα Java με το Jadx και αποσυναρμολόγηση/επιδιόρθωση κώδικα Smali και επανασυσκευασία με το Apktool</b>
</p>

---

## 📋 Πίνακας περιεχομένων

- [Επισκόπηση & Αρχιτεκτονική](#-επισκόπηση--αρχιτεκτονική)
- [Εγκατάσταση] (#-εγκατάσταση)
- [Jadx: Decompiling σε Java Source](#-jadx-decompiling-to-java-source)
  - [Λειτουργία GUI Jadx] (#jadx-gui-mode)
  - [Jadx CLI Batch Decompilation](#jadx-cli-batch-decompilation)
  - [Extracting High-Value Assets & Manifest](#extracting-high-value-assets--manifest)
- [Apktool: Disassembly & Smali Extraction](#-apktool-disassembly--smali-extraction)
  - [Απομεταγλώττιση σε Smali & Resources](#decompiling-to-smali--resources)
  - [Analysing & Patching Smali Code](#analyzing--patching-smali-code)
  - [Bypassing Network Security Config (Cleartext/Burp)](#bypassing-network-security-config-cleartextburp)
- [Rebuilding, Aligning & Signing the Patched APK](#-rebuilding-aligning--signing-the-patched-apk)
  - [1. Rebuild with Apktool](#1-rebuild-with-apktool)
  - [2. Generate Keystore Certificate](#2-generate-keystore-certificate)
  - [3. Zipalign Optimization](#3-zipalign-optimization)
  - [4. Υπογραφή με Apksigner](#4-sign-with-apksigner)
- [Πίνακας γρήγορης αναφοράς] (#-ταχεία αναφορά-πίνακας)
- [Defensive Considerations & Code Obfuscation](#-defensive-considerations--code-obfuscation)
- [Νομική αποποίηση ευθυνών](#-νομική αποποίηση ευθυνών)

---

## 🎯 Επισκόπηση & Αρχιτεκτονική

Οι εφαρμογές Android (`.apk`) είναι ουσιαστικά αρχεία ZIP που περιέχουν μεταγλωττισμένο Dalvik Executable bytecode (`classes.dex`), προμεταγλωττισμένους πόρους (`resources.arsc`), δυαδικές δηλώσεις XML (`AndroidManifest.xml`) και εγγενείς βιβλιοθήκες C/C++ (`lib/`).

Δύο βασικά συμπληρωματικά εργαλεία αποτελούν τη ραχοκοκαλιά της ανάλυσης Android:
1. **Jadx (Dex to Java decompiler):** Μετατρέπει τον bytecode Dalvik απευθείας σε καθαρό, αναγνώσιμο από τον άνθρωπο πηγαίο κώδικα Java. Διαθέτει γρήγορο GUI με παραπομπές κώδικα (xrefs), αναζητήσεις μεθόδων και αλγόριθμους απεμπλοκής.
2. **Apktool:** Αποσυναρμολογεί τους πόρους APK στην σχεδόν αρχική τους μορφή και μετατρέπει τον bytecode Dalvik σε **Smali** ενδιάμεση συγκρότηση. Επιτρέπει την τροποποίηση της λογικής της εφαρμογής, την έγχυση προσαρμοσμένων αγκίστρων Smali και την επανασυσκευασία του τροποποιημένου APK.

---

## 📥 Εγκατάσταση
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y jadx apktool apksigner zipalign

# macOS (Homebrew)
brew install jadx apktool android-commandlinetools

# Arch Linux
sudo pacman -S jadx apktool
```
---

## ☕ Jadx: Απομεταγλώττιση σε πηγή Java

### Jadx GUI Mode
Ο ευκολότερος τρόπος για να αναλύσετε ένα APK διαδραστικά:
```bash
jadx-gui target.apk
```
*Συντομεύσεις πληκτρολογίου σε Jadx-GUI:*
- `Ctrl + Shift + F`: Καθολική αναζήτηση κειμένου και τάξης.
- `R`: Βρείτε παραπομπές (όπου χρησιμοποιείται κλάση ή μέθοδος).
- `N`: Μετονομασία μεταβλητής / μεθόδου (με αυτοματοποιημένη ανακατασκευή).

### Jadx CLI Batch Decompilation
Εξάγετε τον πηγαίο κώδικα Java, τα στοιχεία και τους πόρους σε έναν κατάλογο εξόδου χωρίς να εκκινήσετε το GUI:
```bash
# Decompile entire APK into src/ directory
jadx -d output_src target.apk

# Decompile without decoding resources (faster)
jadx -r -d output_src target.apk

# Enable automated deobfuscation of scrambled names (a, b, c -> p001_a)
jadx --deobf -d output_deobf target.apk
```
### Εξαγωγή περιουσιακών στοιχείων υψηλής αξίας & μανιφέστου
- `resources/AndroidManifest.xml`: Προσδιορίζει εξαγόμενες δραστηριότητες, υπηρεσίες, δέκτες εκπομπής και φίλτρα πρόθεσης.
- `resources/res/values/strings.xml`: Συχνά περιέχει κωδικοποιημένα κλειδιά API, διευθύνσεις URL διακομιστή και μυστικά πελάτη OAuth.

---

## 🔨 Apktool: Αποσυναρμολόγηση & Εξαγωγή Smali

### Απομεταγλώττιση σε Smali & Resources
```bash
# Disassemble APK into a work folder
apktool d target.apk -o apk_workdir

# Keep broken resources without crashing
apktool d -r target.apk -o apk_workdir
```
Μέσα στο `apk_workdir`:
- `smali/`: Οδηγίες συναρμολόγησης Dalvik.
- `res/`: Αποκωδικοποιημένοι πόροι Android (διατάξεις, συμβολοσειρές, σχέδια σχεδίασης).
- `AndroidManifest.xml`: Μανιφέστο XML απλού κειμένου.

### Ανάλυση & επιδιόρθωση κώδικα Smali
Για να παρακάμψετε έναν έλεγχο εντός εφαρμογής (π.χ. `isPremium()` ή `isRooted()`):
Ανοίξτε το αντίστοιχο αρχείο `.smali` και εντοπίστε τη μέθοδο:
```smali
.method public isRooted()Z
    .registers 2

    # Original verification logic...
    # Return false (0x0)
    const/4 v0, 0x0
    return v0
.end method
```
Η αλλαγή του `const/4 v0, 0x1` σε `0x0` αναγκάζει τη μέθοδο να επιστρέφει πάντα το `false`.

### Παράκαμψη διαμόρφωσης ασφάλειας δικτύου (Cleartext/Burp)
Για να καταγράψετε επισκεψιμότητα HTTPS από εφαρμογές Android 7+ (API 24+) στο Burp Suite:
1. Στο `AndroidManifest.xml`, ελέγξτε την ετικέτα `application`:
   ```xml
   android:networkSecurityConfig="@xml/network_security_config"
   ```
2. Στο `res/xml/network_security_config.xml`, επιτρέψτε τα πιστοποιητικά χρήστη:
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

## 🔄 Ανοικοδόμηση, ευθυγράμμιση και υπογραφή του επιδιορθωμένου APK

Το Android απαιτεί αυστηρά κάθε εγκατεστημένο APK να είναι ευθυγραμμισμένο με zip και κρυπτογραφική υπογραφή.

### 1. Ανακατασκευή με το Apktool
```bash
apktool b apk_workdir -o modified.apk
```
### 2. Δημιουργία πιστοποιητικού Keystore (Μία φορά)
```bash
keytool -genkey -v -keystore my-release-key.jks -alias alias_name \
  -keyalg RSA -keysize 2048 -validity 10000 -storepass password123 -keypass password123 \
  -dname "CN=Security, OU=Testing, O=Audit, L=City, S=State, C=US"
```
### 3. Βελτιστοποίηση Zipalign
Ευθυγραμμίστε τα ασυμπίεστα δεδομένα σε όρια 4 byte για αποτελεσματικότητα χαρτογράφησης μνήμης:
```bash
zipalign -p -f -v 4 modified.apk modified_aligned.apk
```
### 4. Υπογράψτε με το Apksigner
Υπογράψτε χρησιμοποιώντας το APK Signature Scheme v2/v3:
```bash
apksigner sign --ks my-release-key.jks --ks-pass pass:password123 --out modified_signed.apk modified_aligned.apk

# Verify signature
apksigner verify modified_signed.apk
```
Εγκατάσταση σε συνδεδεμένη συσκευή Android:
```bash
adb install -r modified_signed.apk
```
---

## ⚡ Γρήγορος Πίνακας Αναφοράς

| Στόχος | Εντολή |
|-----------|---------|
| **Διαδραστική απομεταγλώττιση GUI** | `jadx-gui <target.apk>` |
| **CLI Java Export** | `jadx -d out_dir <target.apk>` |
| **Λειτουργία απομπουκώματος CLI** | `jadx --deobf -d out_dir <target.apk>` |
| **Αποσυναρμολόγηση στο Σμάλι** | `apktool d <target.apk> -o workdir` |
| **Ανακατασκευή APK** | `apktool b workdir -o patched.apk` |
| **Zipalign Binary (4-byte)** | `zipalign -p -f -v 4 in.apk aligned.apk` |
| **Υπογραφή διορθωμένο APK** | `apksigner sign --ks key.jks --out final.apk aligned.apk` |
| **Επαλήθευση υπογραφής APK** | `apksigner verify final.apk` |

---

## 🛡️ Αμυντικές Θεωρήσεις & Συσκότιση Κώδικα

1. **R8 / ProGuard Minification:** Απογυμνώστε τις αχρησιμοποίητες κλάσεις και μετονομάστε τα αναγνωριστικά σε μεμονωμένους μη περιγραφικούς χαρακτήρες (`a.b.c`).
2. **DexGuard / Commercial Obfuscation:** Κρυπτογράφηση συμβολοσειρών, απόκρυψη κλήσεων API δυναμικά και εισαγωγή εγγενών αυτοελέγχων ακεραιότητας (ανίχνευση παραβίασης).

---

## ⚠️ Νομική αποποίηση ευθύνης

> [!ΠΡΟΣΟΧΗ]
> Το Jadx και το Apktool προορίζονται αυστηρά για εξουσιοδοτημένη έρευνα ευπάθειας, αντίστροφη μηχανική ιδιόκτητου λογισμικού και αμυντική ανάλυση. Η αποσυμπίληση ιδιόκτητου λογισμικού τρίτων χωρίς εξουσιοδότηση ενδέχεται να παραβιάζει τις συμφωνίες αδειοδότησης και τους νόμους περί πνευματικών δικαιωμάτων.