<!-- language-home-navigation -->
[🏠 Ελληνική Αρχική Σελίδα](../../README.el.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 📁 DIRB - Ολοκληρωμένο Cheatsheet

[🇹🇷 Türkçe Sürüm](μεταφράσεις/README.tr.md)
```
  ██████╗ ██╗██████╗ ██████╗ 
  ██╔══██╗██║██╔══██╗██╔══██╗
  ██║  ██║██║██████╔╝██████╔╝
  ██║  ██║██║██╔══██╗██╔══██╗
  ██████╔╝██║██║  ██║██████╔╝
  ╚═════╝ ╚═╝╚═╝  ╚═╝╚═════╝ 
        Web Content Scanner
```
<p align="center">
  <img src="https://img.shields.io/badge/DIRB-Content_Scanner-orange?style=for-the-badge" alt="DIRB">
  <img src="https://img.shields.io/badge/Wordlists-Bruteforce-blue?style=for-the-badge" alt="Λίστες λέξεων">
  <img src="https://img.shields.io/badge/Kali_Linux-Preinstalled-green?style=for-the-badge" alt="Kali Linux">
  <img src="https://img.shields.io/badge/Web-Pentesting-red?style=for-the-badge" alt="Δοκιμή Ιστού">
</p>

<p align="center">
  <b>🔍 Κλασικός σαρωτής περιεχομένου ιστού που εκτοξεύει επιθέσεις βάσει λεξικών εναντίον διακομιστών ιστού</b>
</p>

---

## 📋 Πίνακας περιεχομένων

- [Εισαγωγή](#-εισαγωγή)
- [DIRB vs Modern Alternatives](#-dirb-vs-modern-alternatives)
- [Εγκατάσταση] (#-εγκατάσταση)
- [Προεπιλεγμένες λίστες λέξεων](#-default-wordlists)
- [Βασική σύνταξη](#-βασική-σύνταξη)
- [Κοινά σενάρια σάρωσης](#-συνήθη-σενάρια σάρωσης)
- [Σύνθετες επιλογές και συντονισμός] (#-προηγμένες-επιλογές--συντονισμός)
- [Vulnerability Hunting with Built-in Lists](#-vulnerability-hunting-with-built-in-lists)
- [Ροές εργασίας πραγματικού κόσμου](#-ρεαλιστικές ροές εργασίας)
- [Πίνακας γρήγορης αναφοράς] (#-ταχεία αναφορά-πίνακας)
- [Συμβουλές και βέλτιστες πρακτικές] (#-συμβουλές--βέλτιστες πρακτικές)
- [Νομική αποποίηση ευθυνών](#-νομική αποποίηση ευθυνών)
- [Πόροι](#-πόροι)

---

## 🎯 Εισαγωγή

Το **DIRB** (Directory Buster) είναι ένας κλασικός, ελαφρύς σαρωτής περιεχομένου Ιστού που βασίζεται σε URL γραμμένο σε C. Εκτοξεύει επιθέσεις βάσει λεξικών εναντίον διακομιστών Ιστού, αναζητώντας υπάρχοντα και κρυφά αντικείμενα Ιστού (αρχεία, καταλόγους, σενάρια και τελικά σημεία).

Το DIRB λειτουργεί δημιουργώντας αιτήματα HTTP για κάθε λέξη σε μια λίστα λέξεων, αναλύοντας τους κωδικούς απόκρισης του διακομιστή (200, 301, 302, 403, κ.λπ.) και προσδιορίζοντας εάν υπάρχει ένας πόρος.

### Βασικά Χαρακτηριστικά

- ✅ **Αυτόματη αναδρομική σάρωση:** Σαρώνει αυτόματα τους υποκαταλόγους που εντοπίστηκαν από προεπιλογή.
- ✅ **Ενσωματωμένες εξειδικευμένες λίστες λέξεων:** Αποστέλλονται με στοχευμένες λίστες λέξεων για κοινούς διακομιστές ιστού, CMS και ευπάθειες.
- ✅ **Χαμηλό αποτύπωμα πόρων:** Ελαφρύ δυαδικό που λειτουργεί ομαλά ακόμα και σε υλικό χαμηλών προδιαγραφών ή παλαιότερα συστήματα.
- ✅ **Υποστήριξη προσαρμοσμένης κεφαλίδας και διακομιστή μεσολάβησης:** Εύκολη ενσωμάτωση με το Burp Suite και τα upstream proxies HTTP.

---

## ⚖️ DIRB vs Modern Alternatives

| Χαρακτηριστικό | DIRB | Gobuster | ffuf | Feroxbuster | dirsearch |
|---------|------|----------|------|-------------|-----------|
| **Γλώσσα** | C | Πήγαινε | Πήγαινε | Σκουριά | Python |
| **Ταχύτητα** | ⭐⭐ (Χαμηλό νήμα) | ⭐⭐⭐⭐⭐ (Γρήγορο) | ⭐⭐⭐⭐⭐ (Εξαιρετικά γρήγορο) | ⭐⭐⭐⭐⭐ (Async Rust) | ⭐⭐⭐⭐ (Γρήγορα) |
| **Προεπιλεγμένη αναδρομή** | ✅ Ναι (Αυτόματο) | ❌ Όχι | ❌ Όχι (Προαιρετικό `-recursion`) | ✅ Ναι (Αυτόματο) | ✅ Ναι (`-r`) |
| **Περιλαμβάνονται προσαρμοσμένες λίστες λέξεων** | ✅ Ναι (`/usr/share/dirb/`) | ❌ Όχι | ❌ Όχι | ❌ Όχι | ✅ Ναι (`db/dicc.txt`) |
| **VHost Fuzzing** | ❌ Όχι | ✅ Ναι | ✅ Ναι | ❌ Όχι | ✅ Ναι (`--subdirs`) |
| **POST / Data Fuzzing** | ❌ Όχι | ❌ Όχι | ✅ Ναι | ❌ Όχι | ✅ Ναι (`-d`) |
| **Μνήμη / Χρήση CPU** | Πολύ Χαμηλό | Χαμηλό | Χαμηλό | Πολύ Χαμηλό | Μέτρια |

---

## 📥 Εγκατάσταση

### Kali Linux & Parrot OS

Το DIRB έρχεται προεγκατεστημένο σε Kali Linux και Parrot OS:
```bash
# Verify installation
dirb

# Update to latest package
sudo apt update && sudo apt install -y dirb
```
### Debian / Ubuntu
```bash
sudo apt update
sudo apt install -y dirb
```
### Μεταγλώττιση από την πηγή
```bash
# Install build dependencies
sudo apt install -y build-essential libcurl4-openssl-dev

# Download and extract source
wget https://sourceforge.net/projects/dirb/files/dirb/2.22/dirb222.tar.gz
tar -xzvf dirb222.tar.gz
cd dirb222

# Configure permissions and compile
chmod -R 755 conf
chmod 755 configure
./configure
make
sudo make install
```
---

## 📚 Προεπιλεγμένες λίστες λέξεων

Όταν εγκατασταθεί στο Kali Linux, το DIRB περιλαμβάνει αποκλειστικές λίστες λέξεων που βρίσκονται στο `/usr/share/dirb/wordlists/`:

| Διαδρομή λίστας λέξεων | Καταχωρήσεις | Σκοπός |
|---------------|---------|---------|
| `/usr/share/dirb/wordlists/common.txt` | ~4.614 | Η προεπιλεγμένη λίστα χρησιμοποιείται όταν δεν έχει καθοριστεί λίστα λέξεων |
| `/usr/share/dirb/wordlists/small.txt` | ~959 | Ταχεία ανακάλυψη μονοπατιών υψηλής πιθανότητας |
| `/usr/share/dirb/wordlists/big.txt` | ~20.469 | Εξαντλητικός κατάλογος και αναζήτηση αρχείων |
| `/usr/share/dirb/wordlists/catala.txt` | ~6.846 | Καταλανική λίστα λέξεων |
| `/usr/share/dirb/wordlists/spanish.txt` | ~11.720 | Ισπανική λίστα λέξεων |
| `/usr/share/dirb/wordlists/mutations_common.txt` | ~1.000 | Κοινές παραλλαγές προθέματος/επιθήματος |
| `/usr/share/dirb/wordlists/extensions_common.txt` | ~40 | Τυπικές επεκτάσεις ιστού (`.php`, `.asp`, `.html`, κ.λπ.) |

### Ενσωματωμένες λίστες λέξεων ευπάθειας (`/usr/share/dirb/wordlists/vulns/`)

- `apache.txt` - Αρχεία ειδικά για Apache, σελίδες κατάστασης και γνωστοί κατάλογοι
- `iis.txt` - Αρχεία διακομιστή web Microsoft IIS (`web.config`, `aspnet_client`, κ.λπ.)
- `tomcat.txt` - Διαδρομές διαχείρισης και διαχειριστών Apache Tomcat
- `cgis.txt` - Σενάρια και τελικά σημεία κοινής διεπαφής πύλης (CGI)
- `fatwire.txt`, `hpsmh.txt`, `hyperion.txt`, `iplanet.txt`, `netware.txt`, `oracle.txt`

---

## ⌨️ Βασική σύνταξη
```bash
dirb <url_base> [<wordlist_file(s)>] [options]
```
### Βασικά παραδείγματα
```bash
# Basic scan using default common.txt wordlist
dirb http://10.10.10.10

# Scan using a specific custom wordlist
dirb https://example.com /usr/share/wordlists/dirb/big.txt

# Scan with custom SecLists directory list
dirb http://target.local /usr/share/seclists/Discovery/Web-Content/directory-list-2.3-medium.txt
```
---

## 🔍 Συνηθισμένα σενάρια σάρωσης

### 1. Σάρωση με επεκτάσεις αρχείων (`-X` και `-x`)

Αναζήτηση για συγκεκριμένες επεκτάσεις αρχείων που επισυνάπτονται σε κάθε λέξη στο λεξικό:
```bash
# Test specific extensions on every wordlist entry
dirb http://target.local -X .php,.html,.txt,.bak

# Use an extensions list file
dirb http://target.local -x /usr/share/dirb/wordlists/extensions_common.txt

# Combine directory brute-force with extension brute-force
dirb http://target.local /usr/share/dirb/wordlists/common.txt -X .php,.json,.zip
```
### 2. Έλεγχος αναδρομής (`-r` και `-R`)

Από προεπιλογή, το DIRB ανιχνεύει αναδρομικά κάθε κατάλογο που ανακαλύφθηκε. Μπορείτε να ελέγξετε αυτήν τη συμπεριφορά:
```bash
# Disable recursion completely (scan root level only)
dirb http://target.local -r

# Interactive recursion (DIRB asks before descending into each directory)
dirb http://target.local -R
```
### 3. Σάρωση χωρίς διάκριση πεζών-κεφαλαίων (`-i`)

Χρήσιμο κατά τη στόχευση διακομιστών Windows / IIS όπου οι διαδρομές αρχείων δεν έχουν διάκριση πεζών-κεφαλαίων:
```bash
dirb http://target.local -i
```
### 4. Παράβλεψη συγκεκριμένων κωδικών κατάστασης HTTP (`-N`)

Φιλτράρετε τους ανεπιθύμητους κωδικούς απόκρισης (π.χ. βρόχους ανακατεύθυνσης ή απαγορευμένες διαδρομές):
```bash
# Ignore 302 redirects and 403 forbidden responses
dirb http://target.local -N 302,403
```
---

## ⚙️ Προηγμένες επιλογές και συντονισμός

### Έλεγχος ταυτότητας (`-u`)

Το DIRB υποστηρίζει έλεγχο ταυτότητας HTTP Basic και Digest:
```bash
# Supply HTTP authentication credentials
dirb http://target.local -u "admin:SecretP@ssw0rd!"

# Combine auth with custom wordlist
dirb http://target.local /usr/share/dirb/wordlists/big.txt -u "pentester:Summer2026"
```
### Προσαρμοσμένα cookie και διακριτικά περιόδου σύνδεσης (`-c`)

Δοκιμάστε τις πιστοποιημένες διαδρομές εφαρμογών ιστού παρέχοντας cookies περιόδου λειτουργίας:
```bash
# Pass session cookie
dirb http://target.local -c "PHPSESSID=d92j4fn82mdf82ncb184; logged_in=true"
```
### Προσαρμοσμένες κεφαλίδες παράγοντα χρήστη και HTTP (`-a` και `-H`)

Παράκαμψη βασικών κανόνων WAF ή προσομοίωση νόμιμων προγραμμάτων περιήγησης:
```bash
# Set custom User-Agent
dirb http://target.local -a "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36"

# Add custom HTTP Header
dirb http://target.local -H "Authorization: Bearer eyJhbGciOi..."
dirb http://target.local -H "X-Forwarded-For: 127.0.0.1"
```
### Δρομολόγηση διακομιστή μεσολάβησης και ενσωμάτωση σουίτας Burp (`-p` και `-P`)

Αποστολή επισκεψιμότητας DIRB μέσω ενός διακομιστή μεσολάβησης HTTP upstream όπως το Burp Suite ή το OWASP ZAP:
```bash
# Route through local Burp Suite proxy
dirb http://target.local -p 127.0.0.1:8080

# Authenticated proxy
dirb http://target.local -p 10.10.10.1:3128 -P "proxyuser:proxypass"
```
### Περιορισμός ποσοστού και φοροδιαφυγή (`-z`)

Αποφύγετε την ενεργοποίηση ειδοποιήσεων IDS/IPS ή εύθραυστων υπηρεσιών ιστού DoSing προσθέτοντας μια καθυστέρηση μεταξύ των αιτημάτων (σε χιλιοστά του δευτερολέπτου):
```bash
# Wait 250 milliseconds between each request
dirb http://target.local -z 250

# Slow, stealthy scan (1 second delay)
dirb http://target.local -z 1000
```
### Καταγραφή εξόδου (`-o` και `-w`)
```bash
# Save results to a file
dirb http://target.local -o /home/zeus/scans/dirb_target.txt

# Do not halt on WARNING messages (useful for uninterrupted scans)
dirb http://target.local -w -o dirb_output.txt
```
---

## 🎯 Κυνήγι ευπάθειας με ενσωματωμένες λίστες

Το DIRB περιλαμβάνει στοχευμένες λίστες στο `/usr/share/dirb/wordlists/vulns/` για τον εντοπισμό μη επιδιορθωμένων διακομιστών και επικίνδυνων τελικών σημείων:
```bash
# Target Apache server vulnerabilities and status pages
dirb http://target.local /usr/share/dirb/wordlists/vulns/apache.txt

# Target IIS endpoints and misconfigurations
dirb http://target.local /usr/share/dirb/wordlists/vulns/iis.txt

# Target Tomcat management consoles
dirb http://target.local /usr/share/dirb/wordlists/vulns/tomcat.txt

# Search for old CGI scripts and shellshock entrypoints
dirb http://target.local /usr/share/dirb/wordlists/vulns/cgis.txt
```
---

## 🎬 Ροές εργασίας στον πραγματικό κόσμο

### Ροή εργασιών 1: Γρήγορη αρχική αναγνώριση

Όταν συναντάτε για πρώτη φορά έναν διακομιστή web στη θύρα 80/443:
```bash
# Rapid scan without recursion using small wordlist
dirb http://10.10.10.50 /usr/share/dirb/wordlists/small.txt -r
```
### Ροή εργασιών 2: Εξαντλητική αξιολόγηση εφαρμογών Ιστού

Εκτελέστε μια βαθιά σάρωση αναζητώντας αντίγραφα ασφαλείας διαμόρφωσης, πηγαίο κώδικα και τερματικά σημεία API:
```bash
dirb https://app.target.local /usr/share/dirb/wordlists/common.txt \
  -X .php,.bak,.sql,.config,.txt \
  -a "Mozilla/5.0" \
  -w \
  -o dirb_app_scan.txt
```
### Ροή εργασίας 3: Έλεγχος ταυτότητας πύλης ανίχνευσης μέσω Burp Suite
```bash
dirb https://internal.target.local/portal/ /usr/share/dirb/wordlists/big.txt \
  -c "session=s%3A_kJw81..." \
  -p 127.0.0.1:8080 \
  -X .jsp,.do,.action \
  -z 100
```
---

## 📊 Γρήγορος πίνακας αναφοράς

| Σημαία | Επιχείρημα | Περιγραφή | Παράδειγμα |
|------|----------|-------------|---------|
| `-a` | `<user_agent>` | Καθορισμός προσαρμοσμένης συμβολοσειράς παράγοντα χρήστη | `dirb http://target -a "Mozilla/5.0"` |
| `-c` | `<cookie>` | Ορισμός κεφαλίδας cookie HTTP | `dirb http://target -c "id=123"` |
| `-H` | `<header>` | Προσθήκη προσαρμοσμένης κεφαλίδας HTTP | `dirb http://target -H "X-API-Key: secret"` |
| `-i` | Κανένα | Αναζήτηση χωρίς διάκριση πεζών-κεφαλαίων | `dirb http://target -i` |
| `-N` | `<codes>` | Παράβλεψη απαντήσεων που ταιριάζουν με τον κωδικό κατάστασης | `dirb http://target -N 302,403` |
| `-o` | `<output_file>` | Αποθήκευση αποτελεσμάτων σάρωσης στο αρχείο | `dirb http://target -o results.txt` |
| `-p` | `<proxy:port>` | Χρήση διακομιστή μεσολάβησης HTTP | `dirb http://target -p 127.0.0.1:8080` |
| `-P` | `<user:pass>` | Διαπιστευτήρια ελέγχου ταυτότητας διακομιστή μεσολάβησης | `dirb http://target -p proxy:8080 -P u:p` |
| `-r` | Κανένα | Μην κάνετε αναδρομική αναζήτηση | `dirb http://target -r` |
| `-R` | Κανένα | Διαδραστική αναδρομική αναζήτηση | `dirb http://target -R` |
| `-u` | `<user:pass>` | Έλεγχος ταυτότητας HTTP Basic/Digest | `dirb http://target -u "admin:1234"` |
| `-w` | Κανένα | Μην σταματάτε τη σάρωση σε μηνύματα ΠΡΟΕΙΔΟΠΟΙΗΣΗΣ | `dirb http://target -w` |
| `-X` | `<ext_list>` | Προσθήκη επεκτάσεων διαχωρισμένων με κόμμα | `dirb http://target -X .php,.html` |
| `-x` | `<ext_file>` | Προσθήκη επεκτάσεων από το αρχείο | `dirb http://target -x ext.txt` |
| `-z` | `<millisecs>` | Προσθήκη καθυστέρησης μεταξύ αιτημάτων | `dirb http://target -z 250` |

---

## 💡 Συμβουλές και βέλτιστες πρακτικές

1. **Προσοχή στις απαντήσεις Wildcard 200/302:**
   - Ορισμένες εφαρμογές Ιστού επιστρέφουν HTTP 200 ή 302 για κάθε αίτημα (προσαρμοσμένο πρόγραμμα χειρισμού 404). Το DIRB θα αναφέρει κάθε καταχώρηση όπως βρέθηκε. Χρησιμοποιήστε `-N 302` ή μεταβείτε σε `ffuf` / `feroxbuster` για προηγμένο φιλτράρισμα μεγέθους/λέξεων.
2. **Συνδυάστε με το `-r` για μεγάλα λεξικά:**
   - Εάν χρησιμοποιείτε `directory-list-2.3-medium.txt` (220 χιλιάδες λέξεις), απενεργοποιείτε πάντα την αναδρομή με το `-r`. Διαφορετικά, το DIRB μπορεί να πάρει μέρες για να ανιχνεύσει σε βαθιά ένθετους καταλόγους.
3. **Να ελέγχετε πάντα τις επεκτάσεις:**
   - Τα σύγχρονα backend εκθέτουν συχνά ευαίσθητα αρχεία όπως `.env`, `.git`, `.bak`, `.conf` ή `.sql`. Πάντα να δοκιμάζετε κοινές επεκτάσεις με το `-X`.
4. **Χρησιμοποιήστε το `-w` για σαρώσεις χωρίς παρακολούθηση:**
   - Από προεπιλογή, το DIRB μπορεί να σταματήσει εάν συναντήσει προειδοποιήσεις SSL ή δικτύου. Η επιτυχία του `-w` διασφαλίζει την ολοκλήρωση των δοκιμών χωρίς χειροκίνητη αλληλεπίδραση.

---

## ⚠️ Νομική αποποίηση ευθύνης

> **ΣΗΜΑΝΤΙΚΟ:** Το DIRB έχει σχεδιαστεί για έλεγχο ασφαλείας, δοκιμές διείσδυσης και ηθική έρευνα σε συστήματα που κατέχετε ή έχετε ρητή, τεκμηριωμένη άδεια δοκιμής. Οι μη εξουσιοδοτημένες επιθέσεις λεξικών κατά διακομιστών Ιστού μπορεί να προκαλέσουν διακοπή της υπηρεσίας και είναι παράνομες σύμφωνα με τη νομοθεσία για τα εγκλήματα ηλεκτρονικών υπολογιστών παγκοσμίως.

---

## 📚 Πόροι

- [DIRB SourceForge Project](https://sourceforge.net/projects/dirb/)
- [Τεκμηρίωση πακέτου Kali Linux DIRB](https://www.kali.org/tools/dirb/)
- [SecLists - Λίστες λέξεων ανακάλυψης περιεχομένου Ιστού](https://github.com/danielmiessler/SecLists)