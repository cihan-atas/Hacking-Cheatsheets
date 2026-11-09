<!-- language-home-navigation -->
[🏠 Ελληνική Αρχική Σελίδα](../../README.el.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🔍 dirsearch - Ολοκληρωμένο Cheatsheet

[🇹🇷 Türkçe Sürüm](μεταφράσεις/README.tr.md)
```
  ██████╗ ██╗██████╗ ███████╗███████╗ █████╗ ██████╗  ██████╗██╗  ██╗
  ██╔══██╗██║██╔══██╗██╔════╝██╔════╝██╔══██╗██╔══██╗██╔════╝██║  ██║
  ██║  ██║██║██████╔╝███████╗█████╗  ███████║██████╔╝██║     ███████║
  ██║  ██║██║██╔══██╗╚════██║██╔══╝  ██╔══██║██╔══██╗██║     ██╔══██║
  ██████╔╝██║██║  ██║███████║███████╗██║  ██║██║  ██║╚██████╗██║  ██║
  ╚═════╝ ╚═╝╚═╝  ╚═╝╚══════╝╚══════╝╚═╝  ╚═╝╚═╝  ╚═╝ ╚═════╝╚═╝  ╚═╝
       Advanced Web Path Scanner & Directory Brute-Forcer
```
<p align="center">
  <img src="https://img.shields.io/badge/dirsearch-Web_Bruteforcer-blue?style=for-the-badge" alt="dirsearch">
  <img src="https://img.shields.io/badge/Language-Python_Async-green?style=for-the-badge" alt="Python Async">
  <img src="https://img.shields.io/badge/Recon-Web_Discovery-orange?style=for-the-badge" alt="Ανακάλυψη Ιστού">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="License GPLv2">
</p>

<p align="center">
  <b>🌐 Γρήγορος, γεμάτος δυνατότητες σαρωτής διαδρομής ιστού γραμμής εντολών με έξυπνο χειρισμό επεκτάσεων, αναδρομή και ανίχνευση</b>
</p>

---

## 📋 Πίνακας περιεχομένων

- [Εισαγωγή](#-εισαγωγή)
- [Σύγκριση εργαλείων](#-εργαλείο-σύγκριση)
- [Εγκατάσταση] (#-εγκατάσταση)
- [Βασική χρήση και επιλογή στόχου](#-βασική χρήση--επιλογή στόχου)
- [Διαχείριση επεκτάσεων και επιθημάτων](#-επέκταση--διαχείριση επιθημάτων)
- [Λίστες λέξεων και λεξικά](#-λίστες λέξεων--λεξικά)
- [Αναδρομή και ανίχνευση ιστού](#-recursion--web-crawling)
- [Threading, Rate Limiting & Delays](#-threading-rate-limiting--delays)
- [Filtering & Matching Responses](#-filtering--matching-responses)
- [Προσαρμογή αιτήματος HTTP](#-http-request-customization)
  - [Custom Headers & User-Agents](#custom-headers--user-agents)
  - [Cookies & Authentication](#cookies--authentication)
  - [Δεδομένα POST & Μέθοδοι αιτήματος](#post-data--request-methods)
- [Proxies, Tor & Evasion](#-proxies-tor--evasion)
- [Μορφές εξόδου και αναφορά](#-μορφές εξόδου--αναφορές)
- [Πραγματικές συνταγές Pentest & Bug Bounty] (#-real-world-pentest--bug-bounty-recipes)
- [Πίνακας γρήγορης αναφοράς] (#-ταχεία αναφορά-πίνακας)
- [Συμβουλές και βέλτιστες πρακτικές] (#-συμβουλές--βέλτιστες πρακτικές)
- [Νομική αποποίηση ευθυνών](#-νομική αποποίηση ευθυνών)

---

## 🎯 Εισαγωγή

Η **dirsearch** (δημιουργήθηκε από τον Mauro Soria) είναι ένα από τα πιο δημοφιλή και ευέλικτα εργαλεία γραμμής εντολών για την ανακάλυψη κρυφών αρχείων και καταλόγων σε διακομιστές ιστού. Γραμμένο σε Python, παρέχει απαράμιλλη ευελιξία με τα πλούσια ορίσματα γραμμής εντολών, την έγχρωμη έξοδο τερματικού, την αυτόματη αναδρομική ανίχνευση, την έξυπνη επέκταση επέκτασης χαρακτήρων μπαλαντέρ και τα ενσωματωμένα στοιχεία ελέγχου αποφυγής.

Είτε εκτελείτε δοκιμές διείσδυσης ιστού, αναζήτηση επικηρυγμένων σφαλμάτων ή εσωτερικές αξιολογήσεις CTF, το `dirsearch` είναι προσαρμοσμένο για να αποκαλύπτει κρυφές διαχειριστικές διεπαφές, βάσεις δεδομένων αντιγράφων ασφαλείας, ξεχασμένα τελικά σημεία API και ευαίσθητα διαπιστευτήρια.

### Βασικά χαρακτηριστικά

| Χαρακτηριστικό | Περιγραφή |
|---------|-------------|
| **Πολλαπλών νημάτων** | Μηχανή ασύγχρονης και σπειροειδούς αιτήματος υψηλής απόδοσης |
| **Επέκταση μπαλαντέρ** | Αναπτύξτε το `-e *` για να δοκιμάσετε αυτόματα όλες τις γνωστές επεκτάσεις αρχείων ιστού |
| **Ολοκληρωμένη ανίχνευση** | Ακολουθεί συνδέσμους HTML/JS σε αναδρομικές ουρές καταλόγου |
| **Πλούσιο φιλτράρισμα** | Φιλτράρετε τις απαντήσεις κατά κωδικό κατάστασης, μέγεθος byte, πλήθος λέξεων, αριθμό γραμμών ή regex |
| **Ακατέργαστη αίτηση επανάληψης** | Φόρτωση πλήρων αιτημάτων HTTP Burp Suite μέσω `--raw` |
| **Πολλαπλές μορφές** | Δημιουργεί αναφορές σε Plaintext, JSON, CSV, Markdown και HTML |

---

## ⚖️ Σύγκριση εργαλείων

| Χαρακτηριστικό | dirsearch | ffuf | Feroxbuster | Gobuster | DIRB |
|---------|-----------|------|-------------|----------|------|
| **Γλώσσα** | Python | Πήγαινε | Σκουριά | Πήγαινε | C |
| **Ταχύτητα** | ⭐⭐⭐⭐ (Γρήγορα) | ⭐⭐⭐⭐⭐ (Εξαιρετικά γρήγορο) | ⭐⭐⭐⭐⭐ (Απίστευτα γρήγορο) | ⭐⭐⭐⭐ (Γρήγορα) | ⭐⭐ (Χαμηλό νήμα) |
| **Προεπιλεγμένη αναδρομή** | ✅ Αυτόματο (`-r`) | Προαιρετικό (`-recursion`) | ✅ Αυτόματο (`-d`) | Προαιρετικό (`-r`) | ✅ Αυτόματο |
| **Επέκταση μπαλαντέρ (`*`)**| ✅ Εγγενής (`-e *`) | ❌ Όχι | ❌ Όχι | ❌ Όχι | ❌ Όχι |
| **Ενσωματωμένος ανιχνευτής Ιστού** | ✅ Ναι (`--crawl`) | ❌ Όχι | ✅ Ναι (`--extract-links`) | ❌ Όχι | ❌ Όχι |
| **POST / Data Fuzzing** | ✅ Ναι (`--data`) | ✅ Πλήρες HTTP | ❌ Περιορισμένη | ❌ Όχι | ❌ Όχι |
| **Επανάληψη Αίτησης Burp Raw**| ✅ Ναι (`--raw`) | ✅ Ναι (`-request`) | ❌ Όχι | ❌ Όχι | ❌ Όχι |
| **Ενσωματωμένη λίστα λέξεων** | ✅ Επιμέλεια (`db/dicc.txt`)| ❌ Κανένα | ❌ Κανένα | ❌ Κανένα | ✅ Μικρή λίστα |

---

## 📥 Εγκατάσταση

### Χρήση pipx (Συνιστάται για Kali & Modern Linux)
```bash
# Install via pipx (isolated Python environment)
pipx install dirsearch

# Verify installation
dirsearch --version
```
### Χρήση pip
```bash
# Install via standard pip
pip3 install dirsearch
```
### Χρήση Git Clone (απευθείας από την πηγή)
```bash
# Clone the official repository
git clone https://github.com/maurosoria/dirsearch.git --depth 1
cd dirsearch

# Install requirements and run
pip3 install -r requirements.txt
python3 dirsearch.py --version
```
### Kali Linux Package & Docker
```bash
# Kali / Debian repository
sudo apt update && sudo apt install -y dirsearch

# Docker container
docker run -it --rm maurosoria/dirsearch -u https://example.com -e php,html
```
---

## 🚀 Βασική χρήση και επιλογή στόχου

### Μονός στόχος
```bash
# Basic scan against a single URL specifying extensions
dirsearch -u https://target.com -e php,html,js,txt

# Scan with trailing slash forced
dirsearch -u https://target.com -e php -f
```
### Πολλαπλοί στόχοι
```bash
# Scan a list of targets from file
dirsearch -l targets.txt -e php,aspx,txt

# Scan stdin input from subfinder / httpx
cat live_hosts.txt | dirsearch --stdin -e php
```
### Σάρωση θυρών και στόχευση CIDR
```bash
# Scan specific ports on a host
dirsearch -u https://target.com --ports 80,443,8080,8443 -e php

# Scan an entire subnet range
dirsearch --cidr 192.168.1.0/24 -e html
```
---

## 🧩 Διαχείριση επεκτάσεων και επιθημάτων

Το `dirsearch` διαθέτει το πιο ισχυρό σύστημα διαχείρισης επεκτάσεων μεταξύ όλων των σαρωτών καταλόγου:
```bash
# Specify multiple extensions
dirsearch -u https://target.com -e php,html,json,txt,zip

# Use the wildcard (*) to test ALL known extensions automatically
dirsearch -u https://target.com -e *

# Add prefixes to wordlist entries (e.g. test_admin, dev_admin)
dirsearch -u https://target.com -e php --prefixes test_,dev_,staging_,old_

# Add suffixes to wordlist entries (e.g. admin_backup, admin_old)
dirsearch -u https://target.com -e php --suffixes _bak,~,_2026,_old
```
---

## 📚 Λίστες λέξεων και λεξικά

Το `dirsearch` ομαδοποιεί μια ολοκληρωμένη, δοκιμασμένη στη μάχη λίστα λέξεων στο `db/dicc.txt`:
```bash
# Run using the default bundled dictionary
dirsearch -u https://target.com -e php

# Use SecLists Common or Medium wordlists
dirsearch -u https://target.com -e php -w /usr/share/seclists/Discovery/Web-Content/directory-list-2.3-medium.txt

# Use multiple wordlists simultaneously
dirsearch -u https://target.com -e php -w list1.txt,list2.txt

# Force all wordlist entries to lowercase or uppercase
dirsearch -u https://target.com -e php --lowercase
dirsearch -u https://target.com -e php --uppercase
```
---

## 🔄 Αναδρομή και ανίχνευση ιστού

### Αναδρομική σάρωση
```bash
# Recursively scan newly found directories
dirsearch -u https://target.com -e php -r

# Limit recursion depth to 2 levels
dirsearch -u https://target.com -e php -r --recursion-depth 2

# Recursively scan only if HTTP status is 200, 301, or 302
dirsearch -u https://target.com -e php -r --recursion-status 200,301,302
```
### Ενσωματωμένος ανιχνευτής Ιστού
```bash
# Crawl the target website for internal URLs and feed them into the scan queue
dirsearch -u https://target.com -e php --crawl
```
---

## ⚡ Threading, Rate Limiting & Delays

Ελέγξτε την επιθετικότητα της σάρωσης για να αποτρέψετε την ενεργοποίηση τείχη προστασίας ρυθμού περιορισμού ή εφαρμογών Ιστού (WAF):
```bash
# Set number of concurrent threads (default: 25)
dirsearch -u https://target.com -e php -t 50

# Add a delay between requests (e.g. 0.5 seconds)
dirsearch -u https://target.com -e php -s 0.5

# Enforce a maximum request rate per second (e.g. max 15 req/sec)
dirsearch -u https://target.com -e php --max-rate 15

# Customize HTTP connection timeouts and retries
dirsearch -u https://target.com -e php --timeout 8 --retries 3

# Exit scan immediately on excessive connection errors
dirsearch -u https://target.com -e php --exit-on-error
```
---

## 🎯 Φιλτράρισμα και αντιστοίχιση απαντήσεων

### Συμπερίληψη & Εξαίρεση Κωδικού Κατάστασης
```bash
# Include only specific status codes (e.g., ignore everything except 200, 301, 403)
dirsearch -u https://target.com -e php -i 200,301,302,403

# Exclude specific status codes (e.g., exclude 404 and 500)
dirsearch -u https://target.com -e php -x 404,400,500,502,503
```
### Φιλτράρισμα κατά μέγεθος, λέξεις και περιεχόμενο
```bash
# Exclude responses by specific byte size (e.g. ignore custom 404 pages)
dirsearch -u https://target.com -e php --exclude-sizes 0B,1542B,3421B

# Exclude responses matching specific text on the page
dirsearch -u https://target.com -e php --exclude-texts "Not Found","Access Denied"

# Exclude responses matching a regular expression
dirsearch -u https://target.com -e php --exclude-regex "^Error:.*"
```
---

## 🛠️ Προσαρμογή αιτήματος HTTP

### Προσαρμοσμένες κεφαλίδες & πράκτορες χρήστη
```bash
# Set a custom header (e.g., authorization token or internal bypass header)
dirsearch -u https://target.com -e php -H "X-Forwarded-For: 127.0.0.1" -H "X-Requested-With: XMLHttpRequest"

# Load headers from a file
dirsearch -u https://target.com -e php --header-list custom_headers.txt

# Use a randomized User-Agent per request to bypass basic WAFs
dirsearch -u https://target.com -e php --random-agent

# Set a custom User-Agent
dirsearch -u https://target.com -e php --user-agent "Mozilla/5.0 (Windows NT 10.0; Win64; x64)"
```
### Cookies και έλεγχος ταυτότητας
```bash
# Provide session cookies for authenticated scanning
dirsearch -u https://target.com -e php -b "PHPSESSID=abc123xyz; admin_logged=true"

# HTTP Basic Authentication (username:password)
dirsearch -u https://target.com -e php --auth admin:SecretPass123 --auth-type basic

# NTLM Authentication for corporate Windows portals
dirsearch -u https://target.com -e php --auth CORP\\alice:Welcome2026 --auth-type ntlm
```
### ΑΝΑΡΤΗΣΗ Δεδομένων & Μέθοδοι Αιτήματος
```bash
# Perform fuzzing using HTTP POST requests
dirsearch -u https://target.com/api -m POST -d "action=test&token=xyz" -e json

# Replay an entire raw HTTP request saved from Burp Suite
dirsearch -u https://target.com --raw request.txt -e php
```
---

## 🛡️ Proxies, Tor & Evasion
```bash
# Route all traffic through an HTTP or SOCKS5 proxy (e.g., Burp Suite)
dirsearch -u https://target.com -e php --proxy http://127.0.0.1:8080

# Route all traffic through the local Tor SOCKS proxy
dirsearch -u https://target.com -e php --tor

# Skip SSL/TLS certificate verification
dirsearch -u https://target.com -e php --no-check-certificate
```
---

## 📄 Μορφές εξόδου και αναφορά
```bash
# Save output as a clean text file
dirsearch -u https://target.com -e php -o results.txt

# Export results to JSON format
dirsearch -u https://target.com -e php -o results.json --format json

# Export results to HTML, CSV, or Markdown
dirsearch -u https://target.com -e php -o report.html --format html
dirsearch -u https://target.com -e php -o results.csv --format csv
dirsearch -u https://target.com -e php -o report.md --format markdown
```
---

## 🎯 Πραγματικές συνταγές Pentest & Bug Bounty

### 1. Ταχεία Αναγνώριση Ιστού πλήρους εμβέλειας
```bash
# Scan with common web extensions, follow redirects, random UA, 40 threads
dirsearch -u https://target.com \
  -e php,html,js,json,txt,zip \
  -t 40 \
  --random-agent \
  -i 200,301,302,403 \
  -o target_recon.txt
```
### 2. Κυνήγι για ξεχασμένα αντίγραφα ασφαλείας και διαρροές πηγών
```bash
# Test for database dumps, git repos, env files, and zip archives
dirsearch -u https://target.com \
  -e bak,old,orig,zip,tar.gz,sql,env,git \
  -w /usr/share/seclists/Discovery/Web-Content/raft-medium-files.txt \
  -i 200 \
  --exclude-sizes 0B
```
### 3. Deep Recursive Discovery Directory
```bash
# 2-level recursion, crawling pages for new paths, ignoring error status codes
dirsearch -u https://target.com \
  -e php,html \
  -r --recursion-depth 2 \
  --crawl \
  -x 404,500,502 \
  -o deep_scan.json --format json
```
### 4. Επικυρωμένη αξιολόγηση εσωτερικής πύλης
```bash
# Scan through Burp Suite with internal session cookies and WAF bypass headers
dirsearch -u https://intranet.corp.local \
  -e aspx,ashx,config,txt \
  -b "ASP.NET_SessionId=xyz123; AuthToken=ey..." \
  -H "X-Forwarded-For: 127.0.0.1" \
  --proxy http://127.0.0.1:8080
```
---

## ⚡ Γρήγορος Πίνακας Αναφοράς

| Εργασία | Εντολή |
|------|---------|
| **Βασική σάρωση με επεκτάσεις** | `dirsearch -u https://target.com -e php,html,txt` |
| **Όλες οι επεκτάσεις μπαλαντέρ** | `dirsearch -u https://target.com -e *` |
| **Προσαρμοσμένη λίστα λέξεων** | `dirsearch -u https://target.com -e php -w /path/to/wordlist.txt` |
| **Σάρωση πολλών στόχων** | `dirsearch -l targets.txt -e php,aspx` |
| **Αναδρομική ανακάλυψη** | `dirsearch -u https://target.com -e php -r --recursion-depth 2` |
| **Ανίχνευση συνδέσμων στόχου** | `dirsearch -u https://target.com -e php --crawl` |
| **Συμπερίληψη κωδικών κατάστασης** | `dirsearch -u https://target.com -e php -i 200,301,302,403` |
| **Εξαίρεση κωδικών κατάστασης** | `dirsearch -u https://target.com -e php -x 404,500,502` |
| **Εξαιρέστε τα ψευδώς θετικά μεγέθη**| `dirsearch -u https://target.com -e php --exclude-sizes 0B,1240B` |
| **Επαλήθευση ταυτότητας με cookies** | `dirsearch -u https://target.com -e php -b "session=abc123"` |
| **Διαδρομή μέσω Burp Suite** | `dirsearch -u https://target.com -e php --proxy http://127.0.0.1:8080` |
| **Αποθήκευση αναφοράς στο JSON** | `dirsearch -u https://target.com -e php -o report.json --format json` |

---

## 💡 Συμβουλές και βέλτιστες πρακτικές

1. **Χρησιμοποιήστε το `-e *` με φειδώ σε μεγάλες λίστες λέξεων:** Η επέκταση επέκτασης μπαλαντέρ πολλαπλασιάζει κάθε λέξη με 20+ επεκτάσεις. Χρησιμοποιήστε το `-e *` μόνο με μικρότερες λίστες όπως `common.txt` ή `dicc.txt`.
2. **Φιλτράρετε τα ψευδώς θετικά μεγέθη:** Τα σύγχρονα πλαίσια Ιστού επιστρέφουν προσαρμοσμένες σελίδες 404 με HTTP 200. Επιθεωρήστε το μέγεθος της προεπιλεγμένης απόκρισης και προσθέστε `--exclude-sizes <size>` για να εξαλείψετε το θόρυβο.
3. **Να ελέγχετε πάντα τις απαγορευμένες διαδρομές 403:** Σε σύγχρονα περιβάλλοντα cloud και επιχειρήσεων, οι αποκρίσεις HTTP 403 υποδεικνύουν ότι ο κατάλογος ή το τελικό σημείο υπάρχει. Τεκμηριώστε όλα τα 403 για δοκιμή παράκαμψης κεφαλίδας και διέλευσης διαδρομής.
4. **Μόχλευση προθέματα και επιθήματα για αντίγραφα ασφαλείας:** Οι προγραμματιστές αποθηκεύουν συχνά αντίγραφα ασφαλείας ευαίσθητων αρχείων (π.χ. `config.php.bak`, `old_database.sql`). Χρησιμοποιήστε τα `--prefixes` και `--suffixes` για να τα ανακαλύψετε αυτόματα.

---

## ⚠️ Νομική αποποίηση ευθύνης

> **ΣΗΜΑΝΤΙΚΟ:** Η μη εξουσιοδοτημένη σάρωση θυρών, η ωμή επιβολή καταλόγου και η απαρίθμηση ιστού σε διακομιστές χωρίς ρητή, γραπτή άδεια είναι παράνομη. Χρησιμοποιήστε το `dirsearch` αυστηρά σε νομικές δεσμεύσεις bounty bug, εξουσιοδοτημένες δοκιμές διείσδυσης και περιβάλλοντα εργαστηρίου CTF.