<!-- language-home-navigation -->
[🏠 Ελληνική Αρχική Σελίδα](../../README.el.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 📂 GitTools - Πλήρες φύλλο εξαπάτησης

[🇹🇷 Türkçe Sürüm](μεταφράσεις/README.tr.md)
```
   ██████╗ ██╗████████╗████████╗ ██████╗  ██████╗ ██╗     ███████╗
  ██╔════╝ ██║╚══██╔══╝╚══██╔══╝██╔═══██╗██╔═══██╗██║     ██╔════╝
  ██║  ███╗██║   ██║      ██║   ██║   ██║██║   ██║██║     ███████╗
  ██║   ██║██║   ██║      ██║   ██║   ██║██║   ██║██║     ╚════██║
  ╚██████╔╝██║   ██║      ██║   ╚██████╔╝╚██████╔╝███████╗███████║
   ╚═════╝ ╚═╝   ╚═╝      ╚═╝    ╚═════╝  ╚═════╝ ╚══════╝╚══════╝
      Exposed /.git/ Repository Dumping, Extraction & Secret Hunting
```
<p align="center">
  <img src="https://img.shields.io/badge/GitTools-Git_Dumping-blue?style=for-the-badge" alt="GitTools">
  <img src="https://img.shields.io/badge/Author-Internetwache-orange?style=for-the-badge" alt="internetwache">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_Secrets-green?style=for-the-badge" alt="Web Security">
  <img src="https://img.shields.io/badge/License-MIT-purple?style=for-the-badge" alt="MIT">
</p>

<p align="center">
  <b>📂 Βασική εργαλειοθήκη για την ανακάλυψη εκτεθειμένων φακέλων <code>/.git/</code> σε διακομιστές ιστού, τη λήψη ακατέργαστων καταστημάτων αντικειμένων Git και την πλήρη ανασύνθεση του αρχικού πηγαίου κώδικα και του ιστορικού δεσμεύσεων</b>
</p>

---

## 📋 Πίνακας περιεχομένων

- [Overview & The Exposure .git Hazard](#-overview--the-exposed-git-hazard)
- [Εγκατάσταση] (#-εγκατάσταση)
- [The Three GitTools Modules](#-the-three-gittools-modules)
  - [1. Dumper (`gitdumper.sh`)](#1-dumper-gitdumpersh)
  - [2. Extractor (`extractor.sh`)](#2-extractor-extractorsh)
  - [3. Finder (`finder.py`)](#3-finder-finderpy)
- [Alternative Python Dumper: `git-dumper`](#-alternative-python-dumper-git-dumper)
- [Μυστικό και κυνήγι διαπιστευτηρίων μετά την ανακατασκευή](#-post-reconstruction-secret--credential-hunting)
- [Πίνακας γρήγορης αναφοράς] (#-ταχεία αναφορά-πίνακας)
- [Defensive Considerations & Web Server Hardening](#-defensive-considerations--web-server-hardening)
- [Νομική αποποίηση ευθυνών](#-νομική αποποίηση ευθυνών)

---

## 🎯 Επισκόπηση & Ο εκτεθειμένος κίνδυνος .git

Όταν οι προγραμματιστές αναπτύσσουν εφαρμογές Ιστού μέσω `git clone` ή αυτοματοποιημένων σεναρίων χωρίς να περιορίζουν σωστά την πρόσβαση σε κρυφούς καταλόγους, ο φάκελος **`/.git/`** γίνεται προσβάσιμος μέσω HTTP/HTTPS:
`https://target.com/.git/`

Ακόμα κι αν η λίστα καταλόγου (ευρετηρίαση) είναι απενεργοποιημένη, η ντετερμινιστική δομή του Git επιτρέπει σε έναν εισβολέα να κατεβάσει τα `/.git/HEAD`, `/.git/config`, `/.git/index` και να διασχίσει όλα τα χαλαρά αντικείμενα του Git (`.git/objects/xx/xxxx...`). Αυτό επιτρέπει την πλήρη ανακατασκευή των:
- Πλήρης πηγαίος κώδικας εφαρμογής και εσωτερικά API.
- Διαπιστευτήρια βάσης δεδομένων, διακριτικά API και ιδιωτικά κλειδιά SSH σε αρχεία διαμόρφωσης.
- Πλήρη αρχεία καταγραφής δέσμευσης git, μηνύματα ηλεκτρονικού ταχυδρομείου συγγραφέων και κλάδοι ανάπτυξης.

---

## 📥 Εγκατάσταση
```bash
# Clone GitTools repository
git clone https://github.com/internetwache/GitTools.git
cd GitTools
chmod +x Dumper/gitdumper.sh Extractor/extractor.sh

# Install alternative standalone Python git-dumper via pip
pip install git-dumper
```
---

## 🚀 Οι τρεις μονάδες GitTools

Το GitTools είναι οργανωμένο σε τρία εξειδικευμένα βοηθητικά προγράμματα:

### 1. Dumper (`gitdumper.sh`)
Πραγματοποιεί λήψη όσο το δυνατόν μεγαλύτερου μέρους του καταλόγου `.git` από τον διακομιστή web:
```bash
# Syntax: ./gitdumper.sh <URL_TO_GIT> <LOCAL_DESTINATION>
./Dumper/gitdumper.sh https://target.com/.git/ /tmp/target_repo/
```
### 2. Εξαγωγέας (`extractor.sh`)
Ανακατασκευάζει τα αρχικά αρχεία, τα δέντρα δέσμευσης και τα κλαδιά από τα χαλαρά αντικείμενα `.git` που απορρίπτονται:
```bash
# Syntax: ./extractor.sh <DUMPED_DIR> <OUTPUT_CODE_DIR>
./Extractor/extractor.sh /tmp/target_repo/ /tmp/extracted_source/
```
Μέσα στο `/tmp/extracted_source/`, κάθε δέσμευση αποκαθίσταται σε έναν απομονωμένο κατάλογο που περιέχει το πλήρες δέντρο αρχείων σε αυτό το χρονικό σημείο.

### 3. Finder (`finder.py`)
Σαρώνει λίστες τομέων ή μπλοκ CIDR για να εντοπίσει εκτεθειμένους καταλόγους `.git`:
```bash
python3 Finder/finder.py -i targets.txt -u "Mozilla/5.0"
```
---

## 🐍 Εναλλακτικό Python Dumper: `git-dumper`

Το `git-dumper` είναι μια εναλλακτική Python πολλαπλών νημάτων που αναδομεί αυτόματα ένα ζωντανό αποθετήριο git που λειτουργεί σε ένα βήμα:
```bash
# Dump and reconstruct working repository in one command
git-dumper https://target.com/.git/ ./recovered_repo/

# Run through HTTP/SOCKS proxy
git-dumper --proxy http://127.0.0.1:8080 https://target.com/.git/ ./recovered_repo/
```
Μόλις ολοκληρωθεί, μπορείτε να χρησιμοποιήσετε τις κανονικές εντολές `git` μέσα στο φάκελο:
```bash
cd ./recovered_repo/
git log -p              # Review all commit diffs and historical edits
git status              # Check untracked files
git branch -a           # List all remote and local branches
```
---

## 🔍 Μετά την ανασυγκρότηση Μυστικό & Κυνήγι Διαπιστευτηρίων

Μόλις ανακτηθεί το αποθετήριο, αναζητήστε διαπιστευτήρια και κρυφά αρχεία:
```bash
cd ./recovered_repo/

# Search for database strings and API keys
grep -Erio "password|secret|api_key|token|AWS" .

# Scan the reconstructed repo with Gitleaks / TruffleHog
gitleaks detect -v
trufflehog git file://$(pwd)
```
---

## ⚡ Γρήγορος Πίνακας Αναφοράς

| Στόχος | Εντολή |
|-----------|---------|
| **Ντάμπ με git-dumper (Python)**| `git-dumper https://target.com/.git/ ./repo/` |
| **Απόθεση με το GitTools Bash** | `./Dumper/gitdumper.sh https://target.com/.git/ ./dump/` |
| **Εξαγωγή δεσμεύσεων στην πηγή** | `./Extractor/extractor.sh ./dump/ ./source/` |
| **Σάρωση λίστας κεντρικού υπολογιστή για .git** | `python3 Finder/finder.py -i domains.txt` |
| **Προβολή ιστορικού δέσμευσης** | `git log -p --color` |
| **Hunt Hardcoded Secrets** | `gitleaks detect -v` |

---

## 🛡️ Αμυντικά ζητήματα & σκλήρυνση διακομιστή Ιστού

1. ** Nginx Hardening:**
   ```nginx
   location ~ /\.git {
       deny all;
       return 404;
   }
   ```
2. **Apache Hardening (`.htaccess`):**
   ```apache
   RedirectMatch 404 /\.git
   ```
3. **Βέλτιστες πρακτικές **CI/CD:** Μην αναπτύσσετε ποτέ κώδικα κλωνοποιώντας το git απευθείας στη ρίζα ιστού (`/var/www/html/`). Χρησιμοποιήστε καθαρές δέσμες τεχνουργημάτων ή δοχεία Docker.

---

## ⚠️ Νομική αποποίηση ευθύνης

> [!ΠΡΟΣΟΧΗ]
> Τα GitTools και το git-dumper έχουν σχεδιαστεί για εξουσιοδοτημένες αξιολογήσεις ευπάθειας και έλεγχο αμυντικής υποδομής. Η λήψη αποκλειστικού πηγαίου κώδικα από μη εξουσιοδοτημένες εφαρμογές Ιστού χωρίς άδεια είναι παράνομη.