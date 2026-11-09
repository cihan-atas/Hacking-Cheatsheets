<!-- language-home-navigation -->
[🏠 Ελληνική Αρχική Σελίδα](../../README.el.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# Name-That-Hash (nth) Cheatsheet

Το [Name-That-Hash](https://github.com/HashPals/Name-That-Hash) (`nth`) είναι μια σύγχρονη, εξαιρετικά γρήγορη μηχανή αναγνώρισης κατακερματισμού που εντοπίζει με ακρίβεια άγνωστους κατακερματισμούς, κατακερματισμούς που είναι ενσωματωμένοι σε κείμενο ή αρχεία και εξάγει απευθείας Hashcat (`-m` και John the Ripper.X) Αποκωδικοποιεί αυτόματα ένθετες κωδικοποιήσεις όπως Base64 και Hex.

---

## Πίνακας περιεχομένων

- [Βασικά χαρακτηριστικά] (#key-features)
- [Εγκατάσταση](#installation)
- [CLI Hash Identification](#cli-hash-identification)
- [Multi-Hash & File Processing](#multi-hash--file-processing)
- [Έξοδος μορφής Hashcat & John the Ripper](#hashcat--john-the-ripper-format-output)
- [Αυτόματη αποκωδικοποίηση και φίλτρα δημοτικότητας] (#auto-decode--popularity-filters)
- [Integration with Cracking Pipelines](#integration-with-cracking-pipelines)

---

## Βασικά χαρακτηριστικά

- **300+ Τύποι κατακερματισμού:** Καλύπτει σύγχρονους αλγόριθμους, μορφές Active Directory, κατακερματισμούς βάσεων δεδομένων, υπογραφές CMS και εσωτερικούς κρυπτογράφους.
- **Hashcat & John Flag Mapping:** Εμφανίζει αμέσως τις τιμές `-m <mode>` και `--format=<format>`.
- **Αυτόματη αποκωδικοποίηση:** Ξεχωρίζει την κωδικοποίηση Base64, Hex και URL πριν από τον υπολογισμό του τύπου κατακερματισμού.
- **Δευτερεύουσα συμβολοσειρά / Αναζήτηση κειμένου:** Βρίσκει κατακερματισμούς που είναι θαμμένοι μέσα σε αρχεία καταγραφής, αποθήκες μνήμης ή αρχεία διαμόρφωσης.
- **Ταξινόμηση δημοτικότητας:** Κατατάσσει τα αποτελέσματα με βάση την πιθανότητα χρήσης σε πραγματικό κόσμο.

---

## Εγκατάσταση
```bash
# Via pipx (recommended for isolated CLI tools)
pipx install name-that-hash

# Via standard Python pip
python3 -m pip install --user name-that-hash

# Kali Linux / Debian repository
sudo apt-get update && sudo apt-get install -y python3-name-that-hash

# Verify installation
nth --version
```
---

## Αναγνώριση κατακερματισμού CLI

### 1. Προσδιορίστε ένα μεμονωμένο κατακερματισμό
```bash
nth -t "5d41402abc4b2a76b9719d911017c592"
```
### 2. Προσδιορίστε απευθείας τις λειτουργίες μορφής Hashcat & John
Εμφάνιση μόνο τύπων κατακερματισμού που έχουν υποστηρίξει λειτουργίες διάσπασης Hashcat ή John:
```bash
nth -t "b8c37e33defde51cf91e1e03e51657da" --hashcat --john
```
### 3. Εμφάνιση λεπτομερών πληροφοριών (Περιγραφή, Ταχύτητα, Παραδείγματα)
```bash
nth -t "$6$rounds=5000$usesomesalt$..." -v
```
---

## Multi-Hash & Επεξεργασία αρχείων

### 1. Προσδιορισμός κατακερματισμών μέσα σε ένα αρχείο (γραμμή προς γραμμή)
```bash
nth -f /path/to/hashes.txt
```
### 2. Φιλτράρετε τα αποτελέσματα μόνο στα κορυφαία δημοφιλή Hashes
Μειώστε τα ψευδώς θετικά στοιχεία από τους σκοτεινούς τύπους κατακερματισμού φιλτράροντας κατά δημοτικότητα:
```bash
# Level 1: Most popular hashes only (MD5, SHA1, SHA256, NTLM, bcrypt, etc.)
nth -f hashes.txt --popularity 1
```
### 3. Αναζήτηση και εξαγωγή κατακερματισμών που είναι ενσωματωμένοι σε μη δομημένο κείμενο
Σάρωση ακατέργαστων συμβολοσειρών μνήμης, αρχείων καταγραφής ή πηγαίο κώδικα για κατακερματισμούς:
```bash
nth --text "Found hash in config: 098f6bcd4621d373cade4e832627b4f6 on port 8080"
```
---

## Έξοδος μορφής Hashcat & John the Ripper

Το `nth` καθιστά ασήμαντο την τροφοδοσία hashes σε κροτίδες χωρίς να απομνημονεύετε αριθμούς λειτουργίας:

| Δείγμα μορφής κατακερματισμού | Ανιχνευμένος αλγόριθμος | Λειτουργία Hashcat (`-m`) | John Format (`--format`) |
|---|---|---|---|
| `e80b5017098950fc58aad83c8c14978e` | MD5 | `0` | `raw-md5` |
| `b4b9b02e6f09a9bd760f388b67351e2b` | NTLM | `1000` | `nt` |
| `284ddc0d513372c7d764a0ff813473f2...` | SHA-256 | `1400` | `raw-sha256` |
| `$2a$12$DOW8W6...` | bcrypt | `3200` | `bcrypt` |
| `$6$salt$hash...` | SHA-512 (Unix) | `1800` | `sha512crypt` |
| `$krb5tgs$23$*...` | Kerberoast (TGS-REP) | `13100` | `krb5tgs` |
| `$krb5asrep$23$...` | AS-REP Roasting | `18200` | `krb5asrep` |

---

## Φίλτρα αυτόματης αποκωδικοποίησης και δημοτικότητας

Πολλές εφαρμογές αποθηκεύουν κατακερματισμούς σε Base64 ή Hex πριν από την εισαγωγή της βάσης δεδομένων. Το `nth` μπορεί να το αντιστρέψει αυτόματα:
```bash
# Decode Base64 hash before checking
nth -t "XUkQS8xLJnY5cZ0REXxFkg==" --base64

# Decode hex encoded string before checking
nth -t "3564343134303261626334623261373662393731396439313130313763353932" --hex
```
---

## Ενσωμάτωση με σωλήνες πυρόλυσης

Μεταφέρετε γρήγορα έναν άγνωστο κατακερματισμό στο Hashcat χρησιμοποιώντας αυτοματοποιημένη αναγνώριση:
```bash
# 1. Check hash and capture Hashcat mode
HASH="b4b9b02e6f09a9bd760f388b67351e2b"
MODE=$(nth -t "$HASH" --hashcat --popularity 1 | grep -oP '(?<=Hashcat: -m )\d+' | head -n 1)

# 2. Launch Hashcat with identified mode
hashcat -m "$MODE" -a 0 "$HASH" /usr/share/wordlists/rockyou.txt
```