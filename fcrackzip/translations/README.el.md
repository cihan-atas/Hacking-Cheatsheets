<!-- language-home-navigation -->
[🏠 Ελληνική Αρχική Σελίδα](../../README.el.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# fcrackzip Cheatsheet

Το [fcrackzip](https://github.com/hyc/fcrackzip) είναι ένα γρήγορο πρόγραμμα επεξεργασίας κωδικού πρόσβασης γραμμής εντολών πολλαπλών νημάτων για κρυπτογραφημένα αρχεία ZIP. Υποστηρίζει τόσο επιθέσεις που βασίζονται σε λεξικό όσο και βελτιστοποιημένες επιθέσεις ωμής βίας κατά του παραδοσιακού ZipCrypto (κρυπτογράφηση ροής PKZIP) και μπορεί να επαληθεύσει την ακεραιότητα του αρχείου χωρίς πλήρη εξαγωγή.

---

## Πίνακας περιεχομένων

- [Βασικά χαρακτηριστικά & περιορισμοί](#core-features--limitations)
- [Εγκατάσταση](#installation)
- [Dictionary Attacks (`-D -p`)](#dictionary-attacks--d--p)
- [Brute-Force Attacks (`-b`)](#brute-force-attacks--b)
- [Character Sets & Length Specification](#character-sets--length-specification)
- [Benchmark & Performance Optimization](#benchmark--performance-optimization)
- [ZipCrypto vs. AES-256 (John/Hashcat Alternative)](#zipcrypto-vs-aes-256-johnhashcat-alternative)

---

## Βασικά Χαρακτηριστικά & Περιορισμοί

- **Ultra-Fast ZipCrypto Cracker:** Γραμμένο σε C, βελτιστοποιημένο για τον αλγόριθμο ZipCrypto παλαιού τύπου (τυπική κρυπτογράφηση PKZIP).
- **Λειτουργία λεξικού:** Διαβάζει διαδοχικά τους κωδικούς πρόσβασης υποψηφίων από λίστες λέξεων.
- **Λειτουργία Brute-Force:** Δημιουργεί υποψήφιους εν κινήσει χρησιμοποιώντας διαμορφώσιμα σύνολα χαρακτήρων και όρια μήκους.
- **Archive Unzip Test (`-u`):** Επαληθεύει τους υποψήφιους κωδικούς πρόσβασης επιχειρώντας αποσυμπίεση, εξαλείφοντας ουσιαστικά τα ψευδώς θετικά.
- **Περιορισμός:** **Δεν** υποστηρίζει σύγχρονη κρυπτογράφηση WinZip AES-256. Για τα ZIP AES-256, χρησιμοποιήστε `zip2john` + John the Ripper ή Hashcat (λειτουργία 13600).

---

## Εγκατάσταση
```bash
# Debian / Ubuntu / Kali Linux
sudo apt-get update && sudo apt-get install -y fcrackzip

# Arch Linux / BlackArch
sudo pacman -S fcrackzip

# Fedora / CentOS / RHEL
sudo dnf install -y fcrackzip

# Verify command
fcrackzip -h
```
---

## Επιθέσεις λεξικού (`-D -p`)

Ο πιο γρήγορος τρόπος για να δοκιμάσετε τις κοινές λίστες λέξεων:

### 1. Basic Dictionary Attack με Unzip Verification
```bash
fcrackzip -u -D -p /usr/share/wordlists/rockyou.txt secret_archive.zip
```
- `-u`: Λειτουργία αποσυμπίεσης (επαληθεύει το εξαγόμενο αρχείο CRC για την αποφυγή ψευδών θετικών αποτελεσμάτων).
- `-D`: Λειτουργία επίθεσης λεξικού.
- `-p <file>`: Διαδρομή προς το αρχείο λεξικού.

### 2. Λεπτή λειτουργία εξόδου
Εμφάνιση δοκιμασμένης ταχύτητας, τρέχοντος κωδικού πρόσβασης και πληροφοριών εντοπισμού σφαλμάτων:
```bash
fcrackzip -v -u -D -p passwords.txt backup.zip
```
---

## Επιθέσεις Brute-Force (`-b`)

Όταν οι επιθέσεις λεξικού αποτυγχάνουν, ωμή βία εναντίον άγνωστων σύντομων κωδικών πρόσβασης:

### 1. Αριθμητικά PIN ωμής δύναμης (4 έως 6 ψηφία)
```bash
fcrackzip -u -b -c 1 -l 4-6 secure.zip
```
- `-b`: Λειτουργία ωμής δύναμης.
- `-c 1`: Σύνολο χαρακτήρων: μόνο ψηφία (`0-9`).
- `-l 4-6`: Εύρος μήκους: min 4, max 6 χαρακτήρες.

### 2. Μικροί αλφαριθμητικοί χαρακτήρες (1 έως 5 χαρακτήρες)
```bash
fcrackzip -u -b -c a1 -l 1-5 archive.zip
```
- `-c a1`: Μικρά γράμματα (`a-z`) και ψηφία (`0-9`).

### 3. Πεζά + κεφαλαία + ψηφία + σύμβολα
```bash
fcrackzip -u -b -c aA1! -l 6-8 protected.zip
```
- `-c a`: Μικρά γράμματα (`a-z`).
- `-c A`: Κεφαλαία γράμματα (`A-Z`).
- `-c 1`: Αριθμοί (`0-9`).
- `-c !`: Ειδικά σύμβολα (`[!:$%&/()=?{[]}+*~#]`).

---

## Σύνολα χαρακτήρων & Προδιαγραφή μήκους

| Σημαία | Περιλαμβάνεται σετ χαρακτήρων | Παράδειγμα Υποψηφίων |
|---|---|---|
| `-c a` | Μικρά γράμματα (`a-z`) | `admin`, `secret` |
| `-c A` | Κεφάλαια γράμματα (`A-Z`) | `SECRET`, `ROOT` |
| `-c 1` | Ψηφία (`0-9`) | `1234`, `2024` |
| `-c !` | Ειδικοί χαρακτήρες στίξης | `!@#$` |
| `-c aA1` | Συνδυαστικοί αλφαριθμητικοί χαρακτήρες | `Admin123` |
| `-p <prefix>` | Αρχική αρχική σειρά σπόρων | Χρήσιμο για την επανέναρξη της ωμής βίας |

Καθορισμός ορίων μήκους:
```bash
# Minimum length 6, Maximum length 8
-l 6-8

# Exact length 8
-l 8-8
```
---

## Βελτιστοποίηση σημείων αναφοράς και απόδοσης

Δοκιμάστε την απόδοση της CPU στο μηχάνημά σας:
```bash
fcrackzip -B
```
---

## ZipCrypto εναντίον AES-256 (εναλλακτικό John/Hashcat)

Εάν το `fcrackzip` επιστρέψει το `not a zipfile, or unsupported encryption type`, το αρχείο κρυπτογραφείται με AES-256 (WinZip AES). Χρησιμοποιήστε το `zip2john`:
```bash
# 1. Extract hash using zip2john
zip2john aes_protected.zip > zip.hash

# 2. Crack using John the Ripper
john --wordlist=/usr/share/wordlists/rockyou.txt zip.hash

# 3. Or crack using Hashcat (Mode 13600: WinZip)
cat zip.hash | cut -d ':' -f 2 > hashcat_zip.hash
hashcat -m 13600 hashcat_zip.hash /usr/share/wordlists/rockyou.txt
```