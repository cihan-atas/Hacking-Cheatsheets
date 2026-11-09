<!-- language-home-navigation -->
[🏠 Ελληνική Αρχική Σελίδα](../../README.el.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🔢 Crunch - Ολοκληρωμένο Cheatsheet

[🇹🇷 Türkçe Sürüm](μεταφράσεις/README.tr.md)
```
   ██████╗██████╗ ██╗   ██╗███╗   ██╗ ██████╗██╗  ██╗
  ██╔════╝██╔══██╗██║   ██║████╗  ██║██╔════╝██║  ██║
  ██║     ██████╔╝██║   ██║██╔██╗ ██║██║     ███████║
  ██║     ██╔══██╗██║   ██║██║╚██╗██║██║     ██╔══██║
  ╚██████╗██║  ██║╚██████╔╝██║ ╚████║╚██████╗██║  ██║
   ╚═════╝╚═╝  ╚═╝ ╚═════╝ ╚═╝  ╚═══╝ ╚═════╝╚═╝  ╚═╝
       High-Speed Pattern-Based Wordlist & Character Matrix Generator
```
<p align="center">
  <img src="https://img.shields.io/badge/Crunch-Wordlist_Generator-blue?style=for-the-badge" alt="Crunch">
  <img src="https://img.shields.io/badge/Language-C-orange?style=for-the-badge" alt="Γλώσσα Γ">
  <img src="https://img.shields.io/badge/Category-Password_Cracking-green?style=for-the-badge" alt="Παράβαση κωδικού πρόσβασης">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="License GPLv2">
</p>

<p align="center">
  <b>🌐 Δημιουργία λίστας λέξεων που δημιουργεί όλους τους πιθανούς συνδυασμούς συνόλων χαρακτήρων χρησιμοποιώντας προσαρμοσμένα μήκη, μοτίβα και σύμβολα</b>
</p>

---

## 📋 Πίνακας περιεχομένων

- [Overview & The Permutation Engine](#-επισκόπηση--the-permutation-engine)
- [Εγκατάσταση] (#-εγκατάσταση)
- [Βασική σύνταξη και σύνολα χαρακτήρων](#-βασική-σύνταξη--σύνολα χαρακτήρων)
- [Αντιστοίχιση μοτίβων και κράτη μέλη θέσης (`-t`)](#-pattern-matching--placeholders--t)
- [Χρήση συνόλων χαρακτήρων συστήματος (`charset.lst`)](#-using-system-charsets-charsetlst)
- [Περιορισμός διπλότυπου χαρακτήρων (`-d`)](#-duplicate-character-limiting--d)
- [Διαίρεση λιστών λέξεων σε κομμάτια (`-b` & `-c`)](#-splitting-wordlists-into-chunks--b---c)
- [Σωληνώσεις απευθείας στα εργαλεία επίθεσης (μηδενικός χώρος στο δίσκο)] (#-piping-directly-into-attack-tools-zero-disk-space)
- [Πίνακας γρήγορης αναφοράς] (#-ταχεία αναφορά-πίνακας)
- [Defensive Considerations & Calculation Warnings](#-defensive-considerations--calculation-warnings)
- [Νομική αποποίηση ευθυνών](#-νομική αποποίηση ευθυνών)

---

## 🎯 Επισκόπηση & The Permutation Engine

Όταν επιτίθενται σε γνωστά μοτίβα κωδικών πρόσβασης (π.χ. εταιρικά PIN, αριθμοί τηλεφώνου ή κωδικοί πρόσβασης που συμμορφώνονται με τυπικούς κανόνες πολυπλοκότητας όπως το `Word2026!`), τα τυπικά στατικά λεξικά όπως το `rockyou.txt` είτε δεν έχουν στόχο είτε είναι αναποτελεσματικά.

Γραμμένο σε C από τον **bofheaded**, το **Crunch** είναι μια απίστευτα γρήγορη δημιουργία λιστών λέξεων. Υπολογίζει τις μεταθέσεις μαθηματικά, επιτρέποντάς σας να ορίσετε ακριβείς θέσεις χαρακτήρων, μήκη, ομάδες χαρακτήρων και περιορισμούς αντιγραφής.

---

## 📥 Εγκατάσταση
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y crunch

# Arch Linux (AUR)
yay -S crunch
```
---

## 🚀 Βασικά σύνολα σύνταξης και χαρακτήρων

Η σύνταξη του πυρήνα απαιτεί ελάχιστο μήκος, μέγιστο μήκος και προαιρετικά ένα αλφάβητο:
```bash
# crunch <min> <max> [characterset] [options]

# Generate all 4-digit numeric PINs (0000 - 9999)
crunch 4 4 0123456789 -o 4digit_pins.txt

# Generate 5-character words using specific custom letters
crunch 5 5 abcdef -o custom_words.txt

# Generate lowercase alphabet combinations from length 3 to 4
crunch 3 4 abcdefghijklmnopqrstuvwxyz -o alpha_3_4.txt
```
---

## 🎭 Αντιστοίχιση μοτίβων και θέσεις κράτησης θέσης (`-t`)

Η σημαία `-t` επιτρέπει τη διόρθωση γνωστών χαρακτήρων ενώ συγχέονται οι άγνωστες υποδοχές χρησιμοποιώντας ειδικά σύμβολα κράτησης θέσης:

| Placeholder | Τύπος χαρακτήρα |
|-------------|----------------|
| `@` | Μικρά γράμματα (`a-z`) |
| `,` | Κεφάλαια γράμματα (`A-Z`) |
| `%` | Αριθμοί (`0-9`) |
| `^` | Ειδικά σύμβολα / σημεία στίξης (`!@#$%^&*...`) |

### Παραδείγματα πρακτικών προτύπων:
```bash
# Generate Turkish phone numbers (e.g., 0532XXXXXXX)
crunch 11 11 -t 0532%%%%%%% -o phone_numbers.txt

# Generate passwords conforming to: Capital letter + 4 lowercase + 2 digits + 1 symbol
# e.g., Admin24! or Super99#
crunch 8 8 -t ,@@@@%%^ -o policy_passwords.txt

# Target known prefix: Spring2026! or Spring2026#
crunch 11 11 -t Spring2026^ -o spring_passwords.txt
```
---

## 📚 Χρήση συνόλων χαρακτήρων συστήματος (`charset.lst`)

Crunch πακέτα προδιαμορφωμένων πινάκων χαρακτήρων στο `/usr/share/crunch/charset.lst`:
```bash
# Use pre-defined alphanumeric set (mixalpha-numeric)
crunch 6 6 -f /usr/share/crunch/charset.lst mixalpha-numeric -o out.txt

# Use hexadecimal lowercase characters (0-9, a-f)
crunch 8 8 -f /usr/share/crunch/charset.lst hex-lower -o hex_keys.txt
```
---

## 🚫 Περιορισμός διπλότυπων χαρακτήρων (`-d`)

Επιβάλετε όρια στους διαδοχικούς διπλότυπους χαρακτήρες για να εξαλείψετε τους απίθανους κωδικούς πρόσβασης:
```bash
# Generate 8-character lowercase words with at most 2 consecutive identical letters
# (e.g., prevents 'aaabbb' while allowing 'aab')
crunch 8 8 abcdefghijklmnopqrstuvwxyz -d 2@ -o deduped.txt

# Limit consecutive numbers to at most 1 (prevents '11' or '22')
crunch 6 6 -t %%%%%% -d 1% -o no_repeated_digits.txt
```
---

## 📦 Διαχωρισμός λιστών λέξεων σε κομμάτια (`-b` & `-c`)

Αποτρέψτε τα αρχεία πολλών terabyte από την εξάντληση του χώρου στο δίσκο χωρίζοντάς τα σε διαχειρίσιμους τόμους:
```bash
# Split wordlist into 500MB chunks
crunch 8 8 -f /usr/share/crunch/charset.lst mixalpha-numeric -b 500mb -o START

# Split by line count (every 1,000,000 lines)
crunch 8 8 0123456789 -c 1000000 -o START
```
---

## ⚡ Διοχέτευση απευθείας στα εργαλεία επίθεσης (μηδενικός χώρος στο δίσκο)

Ποτέ μην γράφετε γιγαντιαίες λίστες λέξεων στο δίσκο όταν μπορείτε να μεταφέρετε μεταθέσεις απευθείας σε εργαλεία διάρρηξης:
```bash
# Stream 8-digit numeric PINs straight into Aircrack-ng
crunch 8 8 0123456789 | aircrack-ng -w - -b 00:11:22:33:44:55 capture.cap

# Stream policy passwords directly into Hydra SSH brute-force
crunch 8 8 -t Admin%%^ | hydra -l admin -P - 10.10.10.50 ssh

# Stream into John the Ripper
crunch 6 6 0123456789 | john --stdin --format=raw-md5 hashes.txt
```
---

## ⚡ Γρήγορος Πίνακας Αναφοράς

| Στόχος | Εντολή |
|-----------|---------|
| **Αριθμητικά PIN (4ψήφιο)** | `crunch 4 4 0123456789 -o pins.txt` |
| **Μοτίβο: Κεφάλαιο + Λέξη + Αριθμοί**| `crunch 8 8 -t ,@@@@%%^ -o passwords.txt` |
| **Μοτίβο: Ειδικό πρόθεμα** | `crunch 10 10 -t Company%%% -o wordlist.txt` |
| **Χρήση αρχείου συνόλου χαρακτήρων** | `crunch 6 6 -f charset.lst mixalpha-numeric -o out.txt` |
| **Όριο διαδοχικών πανομοιότυπων**| `crunch 8 8 -t @@@@@@@@ -d 2@ -o out.txt` |
| **Διαχωρισμός σε αρχεία 1 GB** | `crunch 8 8 <CHARS> -b 1gb -o START` |
| **Σωλήνας προς Aircrack-ng** | `crunch 8 8 0123456789 \| aircrack-ng -w - target.cap` |
| **Σωλήνας προς Ύδρα** | `crunch 7 7 -t User%%% \| hydra -l user -P - <IP> ssh` |

---

## 🛡️ Αμυντικές εκτιμήσεις και προειδοποιήσεις υπολογισμού

> [!ΠΡΟΕΙΔΟΠΟΙΗΣΗ]
> **Εκρήξεις μετάθεσης:** Ελέγχετε πάντα το προβλεπόμενο μέγεθος εξόδου του Crunch πριν επιβεβαιώσετε τη δημιουργία δίσκου. Μια αλφαριθμητική λίστα λέξεων χωρίς περιορισμούς 8 χαρακτήρων παράγει **πάνω από 200 Terabyte** δεδομένων κειμένου! Έξοδος σωλήνα όποτε είναι δυνατόν.

---

## ⚠️ Νομική αποποίηση ευθύνης

> [!ΠΡΟΣΟΧΗ]
> Το Crunch προορίζεται μόνο για εξουσιοδοτημένη ανάκτηση κωδικού πρόσβασης, δοκιμή διείσδυσης και έλεγχο ασφαλείας. Η ωμή επιβολή κωδικών πρόσβασης σε μη εξουσιοδοτημένα συστήματα είναι αυστηρά παράνομη.