<!-- language-home-navigation -->
[🏠 Ελληνική Αρχική Σελίδα](../../README.el.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 📦 λαθρέμπορος - Πλήρες φύλλο εξαπάτησης

[🇹🇷 Türkçe Sürüm](μεταφράσεις/README.tr.md)
```
  ███████╗███╗   ███╗██╗   ██╗ ██████╗  ██████╗ ██╗     ███████╗██████╗ 
  ██╔════╝████╗ ████║██║   ██║██╔════╝ ██╔════╝ ██║     ██╔════╝██╔══██╗
  ███████╗██╔████╔██║██║   ██║██║  ███╗██║  ███╗██║     █████╗  ██████╔╝
  ╚════██║██║╚██╔╝██║██║   ██║██║   ██║██║   ██║██║     ██╔══╝  ██╔══██╗
  ███████║██║ ╚═╝ ██║╚██████╔╝╚██████╔╝╚██████╔╝███████╗███████╗██║  ██║
  ╚══════╝╚═╝     ╚═╝ ╚═════╝  ╚═════╝  ╚═════╝ ╚══════╝╚══════╝╚═╝  ╚═╝
             High-Performance HTTP Request Smuggling Scanner
```
<p align="center">
  <img src="https://img.shields.io/badge/smuggler-HTTP_Smuggling-blue?style=for-the-badge" alt="smuggler">
  <img src="https://img.shields.io/badge/Author-Evan_Ricaurte-orange?style=for-the-badge" alt="defparam">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-green?style=for-the-badge" alt="Ασφάλεια Ιστού">
  <img src="https://img.shields.io/badge/License-MIT-purple?style=for-the-badge" alt="MIT">
</p>

<p align="center">
  >
</p>

---

## 📋 Πίνακας περιεχομένων

- [Επισκόπηση & Θεωρία λαθρεμπορίου HTTP](#-επισκόπηση--http-smuggling-theory)
  - [CL.TE vs TE.CL Discrepancies](#clte-vs-tecl-discrepancies)
- [Εγκατάσταση] (#-εγκατάσταση)
- [Βασική σάρωση στόχου] (#-basic-target-scanning)
- [Διαμορφώσεις σάρωσης & Μηχανές μετάλλαξης](#-scan-configurations--mutation-engines)
  - [Προεπιλογή έναντι εξαντλητικής σάρωσης] (#default-vs-exhaustive-scan)
  - [Προσαρμοσμένες κεφαλίδες και cookies](#custom-headers--cookies)
- [Σάρωση πολλαπλών στόχων και αγωγών] (#-πολλαπλοί στόχοι--σάρωση σωλήνων)
- [Analysing Results & Exploitation Paths](#-analyzing-results--exploitation-paths)
  - [Δηλητηρίαση κρυφής μνήμης μέσω λαθρεμπορίου](#cache-poisoning-via-smuggling)
  - [Bypassing Front-End Security Controls](#bypassing-front-end-security-controls)
  - [Hijacking Other Users' Requests](#hijacking-other-users-requests)
- [Πίνακας γρήγορης αναφοράς] (#-ταχεία αναφορά-πίνακας)
- [Defensive Considerations & Remediation](#-defensive-considerations--remediation)
- [Νομική αποποίηση ευθυνών](#-νομική αποποίηση ευθυνών)

---

## 🎯 Επισκόπηση & Θεωρία λαθρεμπορίου HTTP

Το λαθρεμπόριο αιτήματος HTTP συμβαίνει όταν ένας διακομιστής μεσολάβησης/εξισορρόπησης φορτίου διεπαφής και ένας διακομιστής web back-end διαφωνούν σχετικά με τα όρια ενός αιτήματος HTTP:

### Ασυμφωνίες CL.TE έναντι TE.CL
- **CL.TE:** Το front-end χρησιμοποιεί `Content-Length`, το back-end χρησιμοποιεί το `Transfer-Encoding: chunked`.
- **TE.CL:** Το front-end χρησιμοποιεί `Transfer-Encoding: chunked`, το back-end χρησιμοποιεί το `Content-Length`.
- **TE.TE:** Και τα δύο υποστηρίζουν το `Transfer-Encoding`, αλλά μπορεί κανείς να εξαπατηθεί ώστε να το αγνοήσει μέσω συσκότισης κεφαλίδας (π.χ. `Transfer-Encoding: xchunked`, καρτέλες ή αναδίπλωση γραμμής).

Δημιουργήθηκε από τον **defparam (Evan Ricaurte)**, ο **λαθρεμπόρος** εγχύει προσεκτικά κατασκευασμένα ωφέλιμα φορτία για να παρατηρήσει καθυστερήσεις χρονισμού ή αποκρίσεις αποσυγχρονισμού χωρίς να σπάσει τη σταθερότητα της υπηρεσίας υποστήριξης.

---

## 📥 Εγκατάσταση
```bash
# Clone the repository
git clone https://github.com/defparam/smuggler.git
cd smuggler

# Run using Python 3
python3 smuggler.py -h
```
---

## 🚀 Βασική σάρωση στόχου

Σάρωση μιας μεμονωμένης εφαρμογής web:
```bash
# Basic scan against a single target
python3 smuggler.py -u https://target.com/
```
Από προεπιλογή, ο λαθρέμπορος ελέγχει για συμπεριφορές `CL.TE` και `TE.CL` χρησιμοποιώντας ωφέλιμα φορτία ασφαλούς χρονισμού.

---

## ⚙️ Σάρωση διαμορφώσεων & μηχανών μετάλλαξης

### Προεπιλογή έναντι Εξαντλητικής Σάρωσης
Το Smuggler περιλαμβάνει διαφορετικά προφίλ ωφέλιμου φορτίου:
- **Προεπιλεγμένο προφίλ:** Γρήγορες, κοινές μεταλλάξεις αποσυγχρονισμού.
- **Εξαντλητικό προφίλ:** Ολοκληρωμένες μεταλλάξεις που δοκιμάζουν παραλλαγές κενών διαστημάτων, πολλαπλές κεφαλίδες και ασαφείς κεφαλίδες κωδικοποίησης μεταφοράς:
```bash
# Run exhaustive scan testing all mutation gadgets
python3 smuggler.py -u https://target.com/ -c exhaustive
```
### Προσαρμοσμένες κεφαλίδες και cookie
Όταν απαιτείται έλεγχος ταυτότητας ή συγκεκριμένες κεφαλίδες κεντρικού υπολογιστή:
```bash
python3 smuggler.py -u https://target.com/api/ \
  -H "Cookie: session=xyz123" \
  -H "X-Forwarded-Host: target.com"
```
---

## 🌐 Σάρωση πολλαπλών στόχων και αγωγών

Τελικά σημεία σωλήνων που ανακαλύφθηκαν από το `httpx` ή το `subfinder` απευθείας στον λαθρέμπορο:
```bash
# Scan a list of URLs from file
python3 smuggler.py -f live_hosts.txt -t 10

# Adjust timeout per probe (default: 10s)
python3 smuggler.py -u https://target.com/ --timeout 5
```
---

## 💥 Ανάλυση Αποτελεσμάτων & Διαδρομών Εκμετάλλευσης

Όταν ο λαθρέμπορος αναφέρει θετικό ταίριασμα:
```text
[CRITICAL] CL.TE Desync Detected: https://target.com/
```
### Δηλητηρίαση κρυφής μνήμης μέσω λαθρεμπορίου
Μεταφέρετε παράνομα ένα αίτημα που δημιουργεί μια ανακατεύθυνση εκτός τοποθεσίας (π.χ. χειραγώγηση κεφαλίδας κεντρικού υπολογιστή) για να δηλητηριάσει τις κρυφές μνήμες ιστού που εξυπηρετούν βιβλιοθήκες JavaScript σε όλους τους χρήστες.

### Παράκαμψη των Ελέγχων Ασφαλείας Front-End
Εάν ο διακομιστής μεσολάβησης διεπαφής μπλοκάρει το `/admin`, μεταφέρετε ένα δευτερεύον εσωτερικό αίτημα που φθάνει απευθείας στο back-end χωρίς επιθεώρηση διακομιστή μεσολάβησης:
```http
POST / HTTP/1.1
Host: target.com
Content-Length: 48
Transfer-Encoding: chunked

0

GET /admin HTTP/1.1
Host: localhost
```
### Παραβίαση αιτημάτων άλλων χρηστών
Μεταφορά ενός προθέματος ενός αιτήματος HTTP. Όταν ο επόμενος χρήστης στέλνει το αίτημά του, αυτό προσαρτάται στο λαθραίο αίτημά σας, αποκαλύπτοντας τα cookie και τα διακριτικά περιόδου λειτουργίας του.

---

## ⚡ Γρήγορος Πίνακας Αναφοράς

| Στόχος | Εντολή |
|-----------|---------|
| **Γρήγορος μοναδικός στόχος** | `python3 smuggler.py -u https://target.com/` |
| **Εξαντλητική βαθιά σάρωση**| `python3 smuggler.py -u https://target.com/ -c exhaustive` |
| **Σάρωση λίστας στόχων** | `python3 smuggler.py -f targets.txt -t 15` |
| **Προσαρμοσμένες κεφαλίδες/Cookies**| `python3 smuggler.py -u <URL> -H "Cookie: sess=123"` |
| **Προσαρμοσμένο χρονικό όριο** | `python3 smuggler.py -u <URL> --timeout 15` |

---

## 🛡️ Αμυντικές Θεωρήσεις & Αποκατάσταση

1. **Εφαρμογή HTTP/2 ή HTTP/3 από άκρο σε άκρο:** Το HTTP/2 ορίζει ξεκάθαρα μήκη καρέ, εξαλείφοντας την ασάφεια της κεφαλίδας.
2. **Κανονοποίηση κεφαλίδων HTTP:** Διαμόρφωση αντίστροφων διακομιστών μεσολάβησης για απόρριψη αιτημάτων που περιέχουν `Content-Length` και `Transfer-Encoding`.
3. **Απόθεση διφορούμενων μεταλλάξεων:** Απόρριψη αιτημάτων με ασαφείς κεφαλίδες `Transfer-Encoding` (π.χ. κενά πριν από την άνω και κάτω τελεία, πολλαπλές τιμές).

---

## ⚠️ Νομική αποποίηση ευθύνης

> [!ΠΡΟΣΟΧΗ]
> Το λαθρεμπόριο αιτημάτων HTTP μπορεί να διαταράξει τις ζωντανές ουρές υποστήριξης και να δηλητηριάσει τους κοινόχρηστους κόμβους προσωρινής αποθήκευσης. λαθρέμπορος πρέπει να χρησιμοποιείται αποκλειστικά σε ρητά εξουσιοδοτημένους στόχους δοκιμής με αυστηρή άδεια από τους κατόχους περιουσιακών στοιχείων.