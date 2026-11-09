<!-- language-home-navigation -->
[🏠 Ελληνική Αρχική Σελίδα](../../README.el.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# wafw00f Cheatsheet

Το [wafw00f](https://github.com/EnableSecurity/wafw00f) είναι ένα εργαλείο ανοιχτού κώδικα γραμμής εντολών Τείχος προστασίας εφαρμογών Web (WAF) δακτυλικών αποτυπωμάτων και ανίχνευσης. Προσδιορίζει και αποτυπώνει πάνω από 150 εμπορικές και ανοιχτού κώδικα λύσεις ασφάλειας WAF που προστατεύουν τους ιστότοπους-στόχους στέλνοντας κανονικά και προκλητικά αιτήματα HTTP και αναλύοντας αποκρίσεις συμπεριφοράς, κεφαλίδες και cookies.

---

## Πίνακας περιεχομένων

- [Why Fingerprint WAFs;](#why-fingerprint-wafs)
- [Εγκατάσταση](#installation)
- [Βασική σάρωση και στόχος] (#basic--target-scanning)
- [Εξαντλητική ανίχνευση πολλαπλών βαφών (`-a`)](#εξαντλητική ανίχνευση πολλαπλών βαφών--a)
- [Μαζική σάρωση στόχου από αρχείο (`-i`)](#bulk-target-scanning-from-file--i)
- [Δρομολόγηση μεσολάβησης και Tor (`-p`)](#proxy--tor-routing--p)
- [Εξαγωγή Μορφών (JSON, CSV, Κείμενο)](#export-formats-json-csv-text)
- [Λίστα υποστηριζόμενων υπογραφών WAF (`-l`)](#list-supported-waf-signatures--l)
- [Ενσωμάτωση Bug Bounty & Penetration Testing](#bug-bounty--penetration-testing-integration)

---

## Γιατί WAF δακτυλικών αποτυπωμάτων;

Η εκτόξευση ενεργών επιθέσεων Ιστού (SQL injection, XSS, SSRF) εναντίον ενός προστατευμένου στόχου χωρίς προηγούμενη αναγνώριση οδηγεί συχνά σε άμεσες απαγορεύσεις IP ή ψευδώς αρνητικά αποτελέσματα ευπάθειας. Η αναγνώριση του ακριβούς WAF επιτρέπει:
- **Προσαρμοσμένη φοροδιαφυγή:** Επιλογή τεχνικών παράκαμψης για συγκεκριμένο προμηθευτή (π.χ. κανονικοποίηση Unicode για ModSecurity, κωδικοποίηση τμηματικής μεταφοράς για Cloudflare).
- **Κυνήγι IP προέλευσης:** Εάν εντοπιστεί Cloudflare ή Akamai, περιστρέψτε για να βρείτε την IP προέλευσης backend πριν από τη δοκιμή.
- **Ενημέρωση κατωφλίου κανόνων:** Συντονισμός ορίων συγχρονισμού σαρωτή και ρυθμού κάτω από τα όρια αποκλεισμού.

---

## Εγκατάσταση
```bash
# Debian / Ubuntu / Kali Linux
sudo apt-get update && sudo apt-get install -y wafw00f

# Recommended: Install via pipx
pipx install wafw00f

# Via standard Python pip
python3 -m pip install --user wafw00f

# Verify installation
wafw00f --version
```
---

## Βασική σάρωση και στόχος

### 1. Σάρωση ενός μεμονωμένου τομέα
```bash
wafw00f https://example.com
```
*Το wafw00f στέλνει βασικά αιτήματα HTTP ακολουθούμενα από προσαρμοσμένα κακόβουλα μοτίβα για την ενεργοποίηση φίλτρων ασφαλείας, αναγνώριση υπογραφών σε κεφαλίδες, κωδικούς κατάστασης και σελίδες σφαλμάτων.*

### 2. Λεπτή λειτουργία εξόδου (`-v` / `-vv`)
Επιθεωρήστε συγκεκριμένες κεφαλίδες που ταιριάζουν, κωδικούς κατάστασης HTTP και ευρετικά:
```bash
wafw00f https://example.com -v
```
### 3. Απενεργοποιήστε την ανακατεύθυνση που ακολουθεί (`-r`)
Αποτρέψτε το wafw00f από το να ακολουθεί ανακατευθύνσεις 301/302 για να διαχωρίσετε τους αντίστροφους διακομιστή μεσολάβησης άκρων από τις εφαρμογές υποστήριξης:
```bash
wafw00f https://example.com -r
```
---

## Εξαντλητική ανίχνευση πολλαπλών WAF (`-a`)

Από προεπιλογή, το wafw00f σταματά τη σάρωση μόλις επιβεβαιωθεί η πρώτη αντιστοίχιση WAF. Χρησιμοποιήστε το `-a` για να ελέγξετε όλες τις γνωστές υπογραφές προμηθευτών (απαραίτητο όταν μια εφαρμογή βρίσκεται πίσω από μια άμυνα πολλαπλών επιπέδων όπως το Cloudflare CDN + AWS WAF):
```bash
wafw00f https://example.com -a
```
---

## Μαζική σάρωση στόχου από αρχείο (`-i`)

Σαρώστε μια ολόκληρη λίστα υποτομέων ή στοχευόμενων στοιχείων που συγκεντρώθηκαν από το `subfinder` ή το `amass`:
```bash
wafw00f -i /path/to/domains.txt -o /tmp/waf_results.txt
```
---

## Δρομολόγηση διακομιστή μεσολάβησης και Tor (`-p`)

Δρομολογήστε την κυκλοφορία ανίχνευσης μέσω διακομιστών μεσολάβησης για να αποτρέψετε τη μαύρη λίστα IP ή επιθεωρήστε ερωτήματα στο Burp Suite:

### 1. Διαδρομή μέσω διακομιστή μεσολάβησης Burp Suite
```bash
wafw00f https://example.com -p http://127.0.0.1:8080
```
### 2. Διαδρομή μέσω τοπικού διακομιστή μεσολάβησης Tor SOCKS5
```bash
wafw00f https://example.com -p socks5://127.0.0.1:9050
```
---

## Μορφές εξαγωγής (JSON, CSV, Κείμενο)

Εξαγωγή αποτελεσμάτων για αυτοματοποιημένη αναφορά ή εργαλεία μεταγενέστερης αναγνώρισης:

### 1. Εξαγωγή σε μορφή JSON
```bash
wafw00f https://example.com -o results.json --format json
```
### 2. Εξαγωγή σε υπολογιστικό φύλλο CSV
```bash
wafw00f -i targets.txt -o results.csv --format csv
```
---

## Λίστα υποστηριζόμενων υπογραφών WAF (`-l`)

Δείτε και τα 150+ ανιχνεύσιμα προϊόντα WAF:
```bash
wafw00f -l
```
Τα βασικά υποστηριζόμενα προϊόντα περιλαμβάνουν:
- **Παροχείς Cloud:** Cloudflare, AWS WAF, Akamai, Τείχος προστασίας εφαρμογών Web Microsoft Azure, Google Cloud Armor.
- **Εταιρικές συσκευές:** F5 BIG-IP ASM, Imperva SecureSphere, Barracuda, Fortinet FortiWeb, Citrix NetScaler.
- **Ανοιχτού κώδικα / Λογισμικό:** ModSecurity, NAXSI, Shadow Daemon, OpenResty.

---

## Bug Bounty & Penetration Testing Integration

Ενσωματώστε το wafw00f σε έναν αυτοματοποιημένο διαδικτυακό αγωγό επίθεσης:
```bash
#!/usr/bin/env bash
TARGET="https://app.target.corp"

echo "[*] Detecting WAF for $TARGET..."
WAF=$(wafw00f "$TARGET" -o - --format json | jq -r '.[0].detected')

if [ "$WAF" == "true" ]; then
    WAF_NAME=$(wafw00f "$TARGET" -o - --format json | jq -r '.[0].firewall')
    echo "[!] Active WAF detected: $WAF_NAME"
    echo "[+] Adjusting sqlmap to use vendor-specific tamper scripts..."
    if [[ "$WAF_NAME" =~ "Cloudflare" ]]; then
        sqlmap -u "$TARGET/item?id=1" --tamper=between,randomcase,space2comment
    fi
else
    echo "[+] No active WAF detected. Proceeding with standard scanner."
    sqlmap -u "$TARGET/item?id=1" --batch
fi
```