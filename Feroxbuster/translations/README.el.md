<!-- language-home-navigation -->
[🏠 Ελληνική Αρχική Σελίδα](../../README.el.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🦀 Feroxbuster - Ολοκληρωμένο Cheatsheet

[🇹🇷 Türkçe Sürüm](μεταφράσεις/README.tr.md)
```
  ███████╗███████╗██████╗  ██████╗ ██╗  ██╗██████╗ ██╗   ██╗███████╗████████╗███████╗██████╗ 
  ██╔════╝██╔════╝██╔══██╗██╔═══██╗╚██╗██╔╝██╔══██╗██║   ██║██╔════╝╚══██╔══╝██╔════╝██╔══██╗
  █████╗  █████╗  ██████╔╝██║   ██║ ╚███╔╝ ██████╔╝██║   ██║███████╗   ██║   █████╗  ██████╔╝
  ██╔══╝  ██╔══╝  ██╔══██╗██║   ██║ ██╔██╗ ██╔══██╗██║   ██║╚════██║   ██║   ██╔══╝  ██╔══██╗
  ██║     ███████╗██║  ██║╚██████╔╝██╔╝ ██╗██████╔╝╚██████╔╝███████║   ██║   ███████╗██║  ██║
  ╚═╝     ╚══════╝╚═╝  ╚═╝ ╚═════╝ ╚═╝  ╚═╝╚═════╝  ╚═════╝ ╚══════╝   ╚═╝   ╚══════╝╚═╝  ╚═╝
                Fast, Simple, Recursive Content Discovery
```
<p align="center">
  <img src="https://img.shields.io/badge/Feroxbuster-Content_Discovery-orange?style=for-the-badge" alt="Feroxbuster">
  <img src="https://img.shields.io/badge/Rust-Blazingly_Fast-blue?style=for-the-badge" alt="Rust">
  <img src="https://img.shields.io/badge/Auto_Tune-Recursive-green?style=for-the-badge" alt="Recursive">
  <img src="https://img.shields.io/badge/Bug_Bounty-Web_Pentest-red?style=for-the-badge" alt="Web Pentest">
</p>

<p align="center">
  <b>⚡ Ένα γρήγορο, απλό, αναδρομικό εργαλείο ανακάλυψης περιεχομένου γραμμένο σε Rust</b>
</p>

---

## 📋 Πίνακας περιεχομένων

- [Εισαγωγή](#-εισαγωγή)
- [Βασικά χαρακτηριστικά] (#-key-features)
- [Σύγκριση εργαλείων](#-εργαλείο-σύγκριση)
- [Εγκατάσταση] (#-εγκατάσταση)
- [Βασική σύνταξη και γρήγορη εκκίνηση] (#-βασική-σύνταξη--γρήγορη εκκίνηση)
- [Επιλογές εντολών πυρήνα](#-core-command-options)
  - [Έλεγχος αναδρομής](#recursion-control)
  - [Συγχρονισμός και απόδοση](#concurrency--performance)
  - [Επεκτάσεις αρχείων](#file-extensions)
  - [Filtering & Matching](#filtering--matching)
  - [Έξυπνες λειτουργίες (Αυτόματος συντονισμός, εξαγωγή συνδέσμων, δημιουργία αντιγράφων ασφαλείας)] (#smart-features)
  - [Headers, Cookies & Auth](#headers-cookies--auth)
  - [Ενσωμάτωση Proxy & Burp Suite](#proxy--burp-suite-integration)
- [Σάρωση και σωληνώσεις πολλαπλών στόχων](#-σάρωση πολλαπλών στόχων--σωλήνες)
- [Ροές εργασίας πραγματικού κόσμου](#-ρεαλιστικές ροές εργασίας)
- [Πίνακας γρήγορης αναφοράς] (#-ταχεία αναφορά-πίνακας)
- [Συμβουλές και βέλτιστες πρακτικές] (#-συμβουλές--βέλτιστες πρακτικές)
- [Νομική αποποίηση ευθυνών](#-νομική αποποίηση ευθυνών)
- [Πόροι](#-πόροι)

---

## 🎯 Εισαγωγή

Το **Feroxbuster** είναι ένας υψηλής απόδοσης, πολλαπλών νημάτων, αναδρομικός σαρωτής περιεχομένου ιστού γραμμένος σε Rust. Σχεδιασμένο για να είναι γρήγορο, απλό και διαισθητικό, συνδυάζει την ταχύτητα των fuzzer που βασίζονται σε Go/Rust με εγγενή αυτόματη αναδρομική ανακάλυψη καταλόγου.

Λάμπει κατά τη διάρκεια των δοκιμών διείσδυσης και των δεσμεύσεων bounty bug όπου τα βαθιά δέντρα ιστότοπων και οι κρυφοί κατάλογοι πρέπει να ανακαλύπτονται γρήγορα χωρίς να δημιουργούνται πολύπλοκα χειροκίνητα σενάρια.

---

## 🌟 Βασικά χαρακτηριστικά

- ⚡ **Καταπληκτικά Γρήγορο:** Τροφοδοτείται από ασύγχρονη Rust (`tokio` και `reqwest`), ικανή για χιλιάδες αιτήματα ανά δευτερόλεπτο.
- 🔄 **Εγγενής Αναδρομική Ανακάλυψη:** Ανακαλύπτει αυτόματα νέους καταλόγους και τους σαρώνει αναδρομικά μέχρι τα ρυθμιζόμενα όρια βάθους.
- 🧠 **Auto-Tune & Auto-Bail:** Μειώνει αυτόματα την ταχύτητα σάρωσης εάν ο στοχευόμενος διακομιστής web επιβραδύνει ή ματαιώνει εάν παρουσιαστούν υπερβολικά σφάλματα σύνδεσης.
- 🔗 **In-Line Link Extraction:** Αναλύει τις απαντήσεις HTML και JavaScript για να ανακαλύψει κρυφά τελικά σημεία και τα τροφοδοτεί ξανά στην ουρά σάρωσης.
- 📦 **Αυτόματη αναζήτηση αντιγράφων ασφαλείας:** Προσθέτει τις παραλλαγές `.bak`, `~`, `.old` και `.orig` αυτόματα σε αρχεία που ανακαλύφθηκαν.
- 🎯 **Είσοδος Pipeable:** Δέχεται απρόσκοπτα λίστες στόχων από το `stdin` (από εργαλεία όπως τα `subfinder`, `httpx` ή `gau`).

---

## ⚖️ Σύγκριση εργαλείων

| Χαρακτηριστικό | Feroxbuster | ffuf | dirsearch | Gobuster | Dirb |
|---------|-------------|------|-----------|----------|------|
| **Γλώσσα** | Σκουριά | Πήγαινε | Python | Πήγαινε | C |
| **Ταχύτητα** | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐ |
| **Προεπιλεγμένη αναδρομή** | ✅ Αυτόματο | ❌ Μη αυτόματη σημαία | ✅ Αυτόματο (`-r`) | ❌ Κανένα | ✅ Αυτόματο |
| **Εξαγωγή συνδέσμου** | ✅ Ναι (`--extract-links`) | ❌ Όχι | ❌ Όχι | ❌ Όχι | ❌ Όχι |
| **Auto Rate Tuning** | ✅ Ναι (`--auto-tune`) | ❌ Όχι | ❌ Όχι | ❌ Όχι | ❌ Όχι |
| **POST Fuzzing** | ❌ Όχι | ✅ Εγγενής | ✅ Εγγενής (`--data`) | ❌ Όχι | ❌ Όχι |
| **Πολλαπλοί στόχοι Stdin** | ✅ Εγγενής | ❌ Περιορισμένη | ✅ Ναι (`-l`) | ❌ Όχι | ❌ Όχι |
| **Ανίχνευση αρχείου αντιγράφων ασφαλείας**| ✅ Εγγενής (`--collect-backups`)| ❌ Όχι | ✅ Εγγενής (`-e *`) | ❌ Όχι | ❌ Όχι |

---

## 📥 Εγκατάσταση

### Kali Linux & Debian / Ubuntu
```bash
# Update repository and install
sudo apt update && sudo apt install -y feroxbuster
```
### Χρήση Cargo (Rust Package Manager)
```bash
cargo install feroxbuster
```
### macOS (Homebrew)
```bash
brew install feroxbuster
```
### Docker
```bash
docker run --init -it epi052/feroxbuster -u https://target.com -w /usr/share/seclists/Discovery/Web-Content/common.txt
```
### Προμεταγλωττισμένο δυαδικό (εκδόσεις GitHub)
```bash
# Download latest Linux release
curl -sL https://raw.githubusercontent.com/epi052/feroxbuster/main/install-nix.sh | bash
sudo mv feroxbuster /usr/local/bin/
```
---

## 🚀 Βασική σύνταξη και γρήγορη εκκίνηση
```bash
feroxbuster -u <TARGET_URL> -w <WORDLIST> [OPTIONS]
```
### Παραδείγματα γρήγορης σάρωσης
```bash
# Scan a single target with default settings
feroxbuster -u http://10.10.10.10

# Scan with custom SecLists directory list
feroxbuster -u https://example.com -w /usr/share/seclists/Discovery/Web-Content/directory-list-2.3-medium.txt

# Scan target including specific file extensions
feroxbuster -u https://example.com -w /usr/share/wordlists/dirb/common.txt -x php,html,txt,json
```
---

## ⚙️ Επιλογές βασικών εντολών

### Έλεγχος αναδρομής
```bash
# Specify maximum recursion depth (default is 4)
feroxbuster -u https://example.com -w common.txt -d 2

# Completely disable recursion (scan root directory only)
feroxbuster -u https://example.com -w common.txt -n

# Don't scan nested subdirectories automatically (interactive prompt)
feroxbuster -u https://example.com -w common.txt --dont-scan /api,/static
```
### Συγχρονισμός & Απόδοση
```bash
# Set number of concurrent threads per directory (default: 50)
feroxbuster -u https://example.com -t 100

# Limit total concurrent directory scans running simultaneously (default: unlimited)
feroxbuster -u https://example.com -L 3

# Rate limit requests per second to avoid triggering WAF/firewall blocks
feroxbuster -u https://example.com --rate-limit 150

# Request timeout in seconds (default: 7)
feroxbuster -u https://example.com -T 5
```
### Επεκτάσεις αρχείων
```bash
# Append extensions to each wordlist item
feroxbuster -u https://example.com -x php,asp,aspx,jsp,txt,bak

# Automatically collect and fuzz file extensions found during crawling
feroxbuster -u https://example.com --collect-extensions
```
### Φιλτράρισμα και αντιστοίχιση
```bash
# Match only specific status codes (e.g., 200 OK and 301 Redirects)
feroxbuster -u https://example.com -s 200 301 302

# Filter out specific status codes (e.g., exclude 404 and 403)
feroxbuster -u https://example.com -C 404 403 500

# Filter responses matching exact size in bytes
feroxbuster -u https://example.com -S 4092 1024

# Filter responses matching exact word count
feroxbuster -u https://example.com -W 42

# Filter responses matching exact line count
feroxbuster -u https://example.com -N 12

# Filter responses matching regular expressions (e.g., generic error pages)
feroxbuster -u https://example.com --filter-regex "Under Construction|Page Not Found"

# Filter responses similar to a known 404/wildcard page
feroxbuster -u https://example.com --filter-similar-to https://example.com/definitelynotfoundpage404
```
### Έξυπνες λειτουργίες
```bash
# Auto-tune: Automatically adjusts request concurrency based on server response latency
feroxbuster -u https://example.com --auto-tune

# Auto-bail: Aborts scanning automatically when more than 100 consecutive network errors occur
feroxbuster -u https://example.com --auto-bail

# Extract links: Parses HTML and JavaScript to automatically uncover endpoints
feroxbuster -u https://example.com --extract-links

# Collect backups: Automatically tests for backup copies (.bak, ~, .old) of every discovered file
feroxbuster -u https://example.com --collect-backups
```
### Κεφαλίδες, cookie και έλεγχος ταυτότητας
```bash
# Custom HTTP Header
feroxbuster -u https://example.com -H "Authorization: Bearer <TOKEN>" -H "X-Forwarded-For: 127.0.0.1"

# Session Cookie
feroxbuster -u https://example.com -b "session=abc123xyz; role=admin"

# Custom User-Agent string
feroxbuster -u https://example.com -a "Mozilla/5.0 (Windows NT 10.0; Win64; x64)"

# Ignore invalid / self-signed TLS certificates
feroxbuster -u https://10.10.10.10 -k
```
### Ενσωμάτωση Proxy & Burp Suite
```bash
# Route all requests through Burp Suite proxy
feroxbuster -u https://example.com -p http://127.0.0.1:8080 -k

# Convenience shortcut flag for Burp Suite (enables -k and sets proxy to localhost:8080)
feroxbuster -u https://example.com --burp
```
---

## 🔁 Σάρωση και σωληνώσεις πολλαπλών στόχων

Το Feroxbuster διαβάζει εγγενώς διευθύνσεις URL στόχων από τυπική είσοδο (`stdin`), καθιστώντας το ιδανικό για ενοποίηση με αγωγούς εντοπισμού περιουσιακών στοιχείων:
```bash
# Pipeline: Subdomain discovery -> HTTP Probing -> Feroxbuster Fuzzing
subfinder -d example.com -silent | \
  httpx -silent -mc 200,301,302 | \
  feroxbuster --stdin -w /usr/share/seclists/Discovery/Web-Content/raft-medium-directories.txt -t 30 -L 4
```
### Σάρωση διευθύνσεων URL από ένα αρχείο
```bash
cat targets.txt | feroxbuster --stdin -w common.txt -v
```
---

## 🎬 Ροές εργασίας στον πραγματικό κόσμο

### Ροή εργασιών 1: Ολοκληρωμένη ανίχνευση ιστοσελίδων Bug Bounty

Υψηλή ταυτόχρονη, αυτόματη ρύθμιση ρυθμού, εξαγωγή συνδέσμων και ανίχνευση αντιγράφων ασφαλείας:
```bash
feroxbuster -u https://target.com \
  -w /usr/share/seclists/Discovery/Web-Content/directory-list-2.3-medium.txt \
  -x php,js,json,txt,html \
  --extract-links \
  --collect-backups \
  --auto-tune \
  -o ferox_target_scan.txt
```
### Ροή εργασίας 2: Εσωτερική επιχείρηση / Ανακάλυψη πύλης Active Directory

Σάρωση εσωτερικής εφαρμογής Ιστού μέσω Burp Suite με έλεγχο ταυτότητας και αυτο-υπογεγραμμένα πιστοποιητικά:
```bash
feroxbuster -u https://internal-portal.corp.local \
  -w /usr/share/seclists/Discovery/Web-Content/common.txt \
  -x aspx,ashx,asmx,config,txt \
  -b "ASP.NET_SessionId=k2m9fl3p8..." \
  --burp \
  -d 3
```
### Ροή εργασιών 3: Γρήγορο μη αναδρομικό API και εντοπισμός τελικού σημείου
```bash
feroxbuster -u https://api.target.com/v1 \
  -w /usr/share/seclists/Discovery/Web-Content/api/api-endpoints.txt \
  -n \
  -s 200,201,204,401,403 \
  -t 100 \
  --json \
  -o api_results.json
```
---

## 📊 Γρήγορος πίνακας αναφοράς

| Σημαία | Σύντομη | Περιγραφή | Παράδειγμα |
|------|-------|-------------|---------|
| `--url` | `-u` | URL προορισμού για σάρωση | `feroxbuster -u https://target.com` |
| `--wordlist` | `-w` | Διαδρομή προς τη λίστα λέξεων | `feroxbuster -w /path/to/list.txt` |
| `--threads` | `-t` | Αριθμός ταυτόχρονων νημάτων ανά κατάλογο | `feroxbuster -t 80` |
| `--scan-limit` | `-L` | Περιορισμός συνολικών ταυτόχρονων σαρώσεων καταλόγου | `feroxbuster -L 3` |
| `--depth` | `-d` | Μέγιστο βάθος αναδρομής (προεπιλογή 4) | `feroxbuster -d 2` |
| `--no-recursion` | `-n` | Απενεργοποίηση αναδρομικής σάρωσης καταλόγου | `feroxbuster -n` |
| `--extensions` | `-x` | Επεκτάσεις αρχείου για προσθήκη | `feroxbuster -x php,html,bak` |
| `--status-codes` | `-s` | Κωδικοί κατάστασης για συμπερίληψη | `feroxbuster -s 200 301 302` |
| `--filter-status` | `-C` | Κωδικοί κατάστασης προς εξαίρεση | `feroxbuster -C 404 500` |
| `--filter-size` | `-S` | Μεγέθη απόκρισης προς εξαίρεση | `feroxbuster -S 4096` |
| `--filter-words` | `-W` | Μετράει λέξεις για εξαίρεση | `feroxbuster -W 15` |
| `--filter-lines` | `-N` | Μετρήσεις γραμμών για εξαίρεση | `feroxbuster -N 8` |
| `--auto-tune` | Κανένα | Προσαρμόστε δυναμικά την ταχύτητα με βάση την καθυστέρηση | `feroxbuster --auto-tune` |
| `--auto-bail` | Κανένα | Διακοπή σάρωσης σε υπερβολικά σφάλματα δικτύου | `feroxbuster --auto-bail` |
| `--extract-links`| Κανένα | Ανάλυση HTML/JS για νέα τελικά σημεία | `feroxbuster --extract-links` |
| `--collect-backups` | Κανένα | Ελέγξτε για παραλλαγές αντιγράφων ασφαλείας (`.bak`, `.old`) | `feroxbuster --collect-backups` |
| `--proxy` | `-p` | Αποστολή επισκεψιμότητας μέσω διακομιστή μεσολάβησης HTTP | `feroxbuster -p http://127.0.0.1:8080` |
| `--burp` | Κανένα | Συντόμευση για `-p http://127.0.0.1:8080 -k` | `feroxbuster --burp` |
| `--stdin` | Κανένα | Ανάγνωση διευθύνσεων URL από τυπική είσοδο | `cat urls.txt \| feroxbuster --stdin` |
| `--output` | `-o` | Διαδρομή αρχείου εξόδου | `feroxbuster -o results.txt` |
| `--json` | Κανένα | Μορφοποίηση εξόδου ως JSON | `feroxbuster --json -o out.json` |

---

## 💡 Συμβουλές και βέλτιστες πρακτικές

1. **Χρησιμοποιήστε το `--auto-tune` για απομακρυσμένους στόχους:**
   - Αντί να μαντεύει τον αριθμό των νημάτων (`-t`), το `--auto-tune` μετρά ενεργά τους χρόνους απόκρισης του στόχου και επιταχύνει ή επιταχύνει δυναμικά, αποφεύγοντας την τυχαία άρνηση εξυπηρέτησης του διακομιστή.
2. **Συνδυάστε `--extract-links` και `--collect-backups`:**
   - Αυτός ο συνδυασμός είναι εξαιρετικά θανατηφόρος σε αξιολογήσεις επιβράβευσης σφαλμάτων: το Feroxbuster εξάγει κρυφές διαδρομές JS, τις σαρώνει και ελέγχει αμέσως εάν τα αντίγραφα ασφαλείας του προγραμματιστή (`.bak`, `.swp`, `~`) έχουν μείνει στον διακομιστή.
3. **Περιορίστε τις σαρώσεις σε μεγάλες λίστες λέξεων με το `-L`:**
   - Εάν χρησιμοποιείτε ένα λεξικό 200.000+ λέξεων, η απεριόριστη αναδρομή μπορεί να ξεκινήσει δεκάδες ταυτόχρονα νήματα. Χρησιμοποιήστε `-L 2` ή `-L 3` για να διατηρήσετε τη μνήμη και το εύρος ζώνης εντός ασφαλών ορίων λειτουργίας.
4. **Χρησιμοποιήστε αρχεία κατάστασης για επανάληψη μεγάλων ελέγχων:**
   - Το Feroxbuster αποθηκεύει περιοδικά αρχεία κατάστασης (`.state`). Εάν παρουσιαστεί διακοπτόμενη σάρωση, συνεχίστε αμέσως με το `feroxbuster --resume-from <FILE>.state`.

---

## ⚠️ Νομική αποποίηση ευθύνης

> **ΣΗΜΑΝΤΙΚΟ:** Το Feroxbuster είναι κατασκευασμένο αυστηρά για εξουσιοδοτημένες αξιολογήσεις ασφαλείας, διαγωνισμούς CTF και δοκιμές διείσδυσης που διεξάγονται με γραπτή άδεια. Η σάρωση μη εξουσιοδοτημένων στόχων μπορεί να κορεστεί την υποδομή δικτύου και απαγορεύεται αυστηρά από το νόμο.

---

## 📚 Πόροι

- [Feroxbuster GitHub Repository](https://github.com/epi052/feroxbuster)
- [Επίσημη τεκμηρίωση Feroxbuster](https://epi052.github.io/feroxbuster-docs/)
- [Λίστες ανακάλυψης SecLists](https://github.com/danielmiessler/SecLists)