<!-- language-home-navigation -->
[🏠 Ελληνική Αρχική Σελίδα](../../README.el.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🎯 Wfuzz - Πλήρες φύλλο εξαπάτησης

[🇹🇷 Türkçe Sürüm](μεταφράσεις/README.tr.md)
```
  ██╗    ██╗███████╗██╗   ██╗███████╗███████╗
  ██║    ██║██╔════╝██║   ██║╚══███╔╝╚══███╔╝
  ██║ █╗ ██║█████╗  ██║   ██║  ███╔╝   ███╔╝ 
  ██║███╗██║██╔══╝  ██║   ██║ ███╔╝   ███╔╝  
  ╚███╔███╔╝██║     ╚██████╔╝███████╗███████╗
   ╚══╝╚══╝ ╚═╝      ╚═════╝ ╚══════╝╚══════╝
       The Flexible Web Application Fuzzer & Parameter Explorer
```
<p align="center">
  <img src="https://img.shields.io/badge/Wfuzz-Web_Fuzzer-blue?style=for-the-badge" alt="Wfuzz">
  <img src="https://img.shields.io/badge/Language-Python-orange?style=for-the-badge" alt="Language Python">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-green?style=for-the-badge" alt="Web Security & API">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="License GPLv2">
</p>

<p align="center">
  <b>🌐 Εξαιρετικά προσαρμόσιμο fuzzer HTTP σχεδιασμένο για την ανακάλυψη κρυφών παραμέτρων, παρακάμψεων, κεφαλίδων και πίνακες πολλαπλών φορτίων</b>
</p>

---

## 📋 Πίνακας περιεχομένων

- [Overview & The Keyword Architecture](#-επισκόπηση--the-keyword-architecture)
- [Σύγκριση Wfuzz vs ffuf](#-wfuzz-vs-ffuf-comparison)
- [Εγκατάσταση] (#-εγκατάσταση)
- [Βασικός κατάλογος & Ανακάλυψη αρχείων](#-basic-directory--file-discovery)
- [Θόρυβος φιλτραρίσματος και απόκρυψης (`--hc`, `--hl`, `--hw`, `--hh`)](#-φιλτράρισμα--απόκρυψη-θορύβου---hc---hl---hw---hh)
- [Τύποι ωφέλιμου φορτίου και δυναμικοί κωδικοποιητές (`-z`)](#-τύποι ωφέλιμου φορτίου--δυναμικοί-κωδικοποιητές--z)
- [Parameter & Hidden Variable Fuzzing](#-parameter--hidden-variable-fuzzing)
- [Κεφαλίδες, cookie και έλεγχος ταυτότητας](#-headers-cookies--authentication)
- [Πολυμεταβλητή ασάφεια (`FUZZ` και `FUZ2Z`)](#-multi-variable-fuzzing-fuzz-and-fuz2z)
- [Routing Through Burp Suite (`-p`)](#-routing-through-burp-suite--p)
- [Πίνακας γρήγορης αναφοράς] (#-ταχεία αναφορά-πίνακας)
- [Defensive Considerations & OPSEC](#-defensive-considerations--opsec)
- [Νομική αποποίηση ευθυνών](#-νομική αποποίηση ευθυνών)

---

## 🎯 Επισκόπηση & Αρχιτεκτονική των Λέξεων-κλειδιών

Γραμμένο σε Python από τον **Xavi Mendez**, το **Wfuzz** είναι ένα κλασικό, δοκιμασμένο στη μάχη εργαλείο για την αξιολόγηση εφαρμογών Ιστού. Αντικαθιστά τα καθορισμένα σύμβολα κράτησης θέσης (προεπιλογή: `FUZZ`) μέσα σε διευθύνσεις URL, παραμέτρους, κεφαλίδες και σώματα POST με καταχωρίσεις λεξικού ή ωφέλιμα φορτία που δημιουργούνται αλγοριθμικά.

Το Wfuzz ξεχωρίζει για την απαράμιλλη **ευελιξία ωφέλιμου φορτίου** (εύρη, γεννήτριες IP, συμβολοσειρές regex, κωδικοποιητές on-the-fly όπως MD5 ή Base64) και τη μηχανική αρθρωτού φιλτραρίσματος.

---

## ⚖️ Σύγκριση Wfuzz vs ffuf

| Χαρακτηριστικό | Wfuzz | ffuf |
|---------|-------|------|
| **Γλώσσα υλοποίησης** | Python | Πήγαινε |
| **Ακατέργαστη ταχύτητα** | ⭐⭐⭐ (Καλό) | ⭐⭐⭐⭐⭐ (Blazingly Fast) |
| **Δυναμικοί κωδικοποιητές ωφέλιμου φορτίου**| ✅ Εγγενής (`-z file,pass.txt,md5`)| ❌ Απαιτεί εξωτερικά σενάρια |
| **Συνδυασμοί πολλαπλών ωφέλιμων φορτίων**| ✅ Εγγενής (`FUZZ`, `FUZ2Z`, `FUZ3Z`)| ✅ Υποστηρίζεται (`-w w1:W1 -w w2:W2`)|
| **Ενσωματωμένες γεννήτριες ωφέλιμου φορτίου**| ✅ Εύρος, IP, Hex, Λεξικά| ❌ Αρχειοθετήστε μόνο λίστες λέξεων |
| **Χρήση πόρων** | Μέτρια RAM / CPU | Χαμηλό αποτύπωμα μνήμης |

---

## 📥 Εγκατάσταση
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y wfuzz

# Python Pip
pip3 install wfuzz

# Arch Linux
sudo pacman -S wfuzz
```
---

## 🚀 Βασικός κατάλογος & Ανακάλυψη αρχείων
```bash
# Basic directory fuzzing
wfuzz -w /usr/share/wordlists/dirb/common.txt http://target.local/FUZZ

# Fuzzing file extensions
wfuzz -w extensions.txt http://target.local/index.FUZZ

# Fuzzing hidden backup files
wfuzz -w words.txt http://target.local/FUZZ.bak
```
---

## 🎯 Φιλτράρισμα και απόκρυψη θορύβου (`--hc`, `--hl`, `--hw`, `--hh`)

Οι διακομιστές πραγματικού κόσμου επιστρέφουν απαντήσεις ακόμη και για ανύπαρκτες σελίδες (προσαρμοσμένες σελίδες 404 ή ανακατευθύνσεις). Το Wfuzz παρέχει λεπτομερείς σημαίες φιλτραρίσματος:
```bash
# Hide specific HTTP status codes (e.g. 404, 403)
wfuzz -w wordlist.txt --hc 404,403 http://target.local/FUZZ

# Hide responses by line count (filters out static custom 404 pages)
wfuzz -w wordlist.txt --hl 42 http://target.local/FUZZ

# Hide responses by word count
wfuzz -w wordlist.txt --hw 120 http://target.local/FUZZ

# Hide responses by character count / body length
wfuzz -w wordlist.txt --hh 0,1052 http://target.local/FUZZ

# Show ONLY HTTP 200 and 302 responses
wfuzz -w wordlist.txt --sc 200,302 http://target.local/FUZZ
```
---

## 📦 Τύποι ωφέλιμου φορτίου και δυναμικοί κωδικοποιητές (`-z`)

Το Wfuzz παρέχει ενσωματωμένες γεννήτριες που εξαλείφουν την ανάγκη για προ-δημιουργημένα αρχεία κειμένου:
```bash
# Numeric Range Fuzzing (e.g., IDOR testing from 1 to 500)
wfuzz -z range,1-500 http://target.local/user.php?id=FUZZ

# Custom Inline List
wfuzz -z list,admin-root-test-dev http://target.local/FUZZ/

# On-the-fly MD5 encoding of wordlist entries
wfuzz -z file,passwords.txt,md5 http://target.local/login?hash=FUZZ

# On-the-fly Base64 encoding
wfuzz -z file,usernames.txt,base64 http://target.local/auth?token=FUZZ
```
---

## 🔍 Παράμετρος & κρυφή ασάφεια μεταβλητής

Ανακαλύψτε μη συνδεδεμένες παραμέτρους GET/POST:
```bash
# GET parameter discovery
wfuzz -w /usr/share/seclists/Discovery/Web-Content/burp-parameter-names.txt \
  --hc 404 --hh 2340 "http://target.local/search.php?FUZZ=test"

# POST body parameter discovery
wfuzz -w parameters.txt -d "FUZZ=admin" --hc 404 http://target.local/api/update
```
---

## 🍪 Κεφαλίδες, cookies και έλεγχος ταυτότητας
```bash
# Cookie Fuzzing (Session hijacking / privilege testing)
wfuzz -w wordlist.txt -b "session=FUZZ" http://target.local/dashboard

# HTTP Header Fuzzing (IP bypasses via X-Forwarded-For)
wfuzz -z file,ips.txt -H "X-Forwarded-For: FUZZ" http://target.local/admin

# Basic Authentication brute force
wfuzz -z file,users.txt -z file,passwords.txt --basic "FUZZ:FUZ2Z" http://target.local/protected/
```
---

## ⚡ Multi-variable Fuzzing (`FUZZ` και `FUZ2Z`)

Συνδυάστε πολλές λίστες λέξεων χρησιμοποιώντας διακριτές ετικέτες κράτησης θέσης:
```bash
# Test username and password matrix simultaneously
wfuzz -w users.txt -w passwords.txt -d "user=FUZZ&pass=FUZ2Z" --hc 200 http://target.local/login.php

# Fuzz path and extension together
wfuzz -w words.txt -w extensions.txt http://target.local/FUZZ.FUZ2Z
```
---

## 🔁 Δρομολόγηση μέσω Burp Suite (`-p`)

Ελέγξτε τα ωφέλιμα φορτία και τις απαντήσεις στο Burp Suite:
```bash
# Route requests through local Burp Suite proxy
wfuzz -p 127.0.0.1:8080:HTTP -w wordlist.txt http://target.local/FUZZ
```
---

## ⚡ Γρήγορος Πίνακας Αναφοράς

| Στόχος | Εντολή |
|-----------|---------|
| **Κατάλογος Fuzzing** | `wfuzz -w <DICT> <URL>/FUZZ` |
| **Απόκρυψη κωδικών κατάστασης HTTP** | `wfuzz -w <DICT> --hc 404,403 <URL>/FUZZ` |
| **Απόκρυψη κατά μήκος απόκρισης** | `wfuzz -w <DICT> --hh <LENGTH> <URL>/FUZZ` |
| **Παράμετρος Fuzz GET** | `wfuzz -w <PARAMS> "<URL>?FUZZ=test"` |
| **Fuzz POST Body** | `wfuzz -w <PARAMS> -d "FUZZ=test" <URL>` |
| **Αριθμητικό εύρος ωφέλιμων φορτίων** | `wfuzz -z range,1-100 "<URL>?id=FUZZ"` |
| **Κωδικοποιήστε ωφέλιμα φορτία on the Fly**| `wfuzz -z file,<DICT>,base64 "<URL>?token=FUZZ"` |
| **Πολλαπλών λέξεων-κλειδιών Fuzzing** | `wfuzz -w <W1> -w <W2> -d "u=FUZZ&p=FUZ2Z" <URL>` |
| **Διαδρομή μέσω Burp Proxy** | `wfuzz -p 127.0.0.1:8080:HTTP -w <DICT> <URL>/FUZZ` |

---

## 🛡️ Αμυντικές Θεωρήσεις & OPSEC

1. **Αποκλεισμός WAF:** Η ασάφεια εκατοντάδων αιτημάτων ανά δευτερόλεπτο ενεργοποιεί τον περιορισμό του ρυθμού Cloudflare, AWS WAF και ModSecurity. Χρησιμοποιήστε το `-s <seconds>` για να μειώσετε την ταχύτητα αιτήματος του γκαζιού.
2. **Εξάντληση πόρων:** Η έντονη ασάφεια σε ερωτήματα βάσης δεδομένων χωρίς ευρετήριο μπορεί να προκαλέσει συνθήκες άρνησης υπηρεσίας (DoS) σε διακομιστές ιστού παλαιού τύπου.

---

## ⚠️ Νομική αποποίηση ευθύνης

> [!ΠΡΟΣΟΧΗ]
> Αυτό το cheatsheet προορίζεται μόνο για εξουσιοδοτημένες δοκιμές διείσδυσης και έλεγχο ευπάθειας. Απαγορεύεται αυστηρά το fuzzy μη εξουσιοδοτημένων εφαρμογών.