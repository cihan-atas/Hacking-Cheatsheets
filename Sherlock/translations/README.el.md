<!-- language-home-navigation -->
[🏠 Ελληνική Αρχική Σελίδα](../../README.el.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# Cheatsheet Sherlock

Το [Sherlock](https://github.com/sherlock-project/sherlock) είναι ένα εργαλείο αναγνώρισης OSINT που κυνηγάει λογαριασμούς μέσων κοινωνικής δικτύωσης ανά όνομα χρήστη σε περισσότερες από 400 πλατφόρμες κοινωνικής δικτύωσης, φόρουμ, ιστότοπους φιλοξενίας κώδικα και διαδικτυακές κοινότητες ταυτόχρονα.

---

## Πίνακας περιεχομένων

- [Key Capabilities] (#key-capabilities)
- [Εγκατάσταση](#installation)
- [Αναζήτηση με ένα και πολλαπλό όνομα χρήστη](#single--multiple-username-search)
- [Filtering Sites & Platforms](#filtering-sites--platforms)
- [Δρομολόγηση μεσολάβησης και Tor](#proxy--tor-routing)
- [Timeout & Threading Tuning] (#timeout--threading-tuning)
- [Μορφές εξαγωγής (CSV, XLSX, JSON)](#export-formats-csv-xlsx-json)
- [Automation & OSINT Pipeline Integration](#automation--osint-pipeline-integration)

---

## Βασικές Δυνατότητες

- **400+ πλατφόρμες:** Ερωτήματα GitHub, Twitter/X, Instagram, Reddit, TikTok, LinkedIn, Telegram, Discord, Steam, Spotify, Docker Hub, Pastebin και εκατοντάδες άλλα.
- **Async Concurrency:** Παράλληλη σάρωση υψηλής ταχύτητας με χρήση ασύγχρονων αιτημάτων Python.
- **Ακριβής ανίχνευση:** Εντοπίζει κωδικούς κατάστασης HTTP, μηνύματα σφάλματος και ανακατευθύνει για την αποφυγή ψευδών θετικών στοιχείων.
- **Υποστήριξη Tor & SOCKS5:** Προστατεύει την ανωνυμία του ερευνητή και παρακάμπτει τα όρια ποσοστών.

---

## Εγκατάσταση
```bash
# Recommended: Install via pipx
pipx install sherlock-project

# Or install from GitHub repository
git clone https://github.com/sherlock-project/sherlock.git
cd sherlock
python3 -m pip install -r requirements.txt

# Debian / Kali Linux repository
sudo apt-get update && sudo apt-get install -y sherlock

# Verify command
sherlock --version
```
---

## Αναζήτηση με ένα και πολλαπλό όνομα χρήστη

### 1. Βασική Αναζήτηση ονόματος χρήστη
Αναζητήστε μια λαβή στόχου και στους 400+ ιστότοπους:
```bash
sherlock target_user
```
### 2. Αναζήτηση πολλαπλών ονομάτων χρήστη ταυτόχρονα
```bash
sherlock target_user1 target_user2 alias_dev
```
### 3. Εκτύπωση μόνο λογαριασμών που βρέθηκαν (Suppress Not Found / Σφάλματα)
```bash
sherlock --print-found target_user
```
---

## Φιλτράρισμα τοποθεσιών και πλατφορμών

Περιορίστε τις αναζητήσεις σε συγκεκριμένες πλατφόρμες για εξοικονόμηση χρόνου ή εστίαση σε συγκεκριμένες κατηγορίες νοημοσύνης:

### 1. Ερώτημα μόνο για συγκεκριμένους ιστότοπους
```bash
sherlock --site github --site twitter --site reddit --site gitlab target_user
```
### 2. Αναζήτηση χωρίς συγκεκριμένες πλατφόρμες (Παράβλεψη ψευδώς θετικών βαριών τοποθεσιών)
```bash
sherlock target_user --site-list targets.txt
```
---

## Δρομολόγηση διακομιστή μεσολάβησης και Tor

Για να αποτρέψετε τις απαγορεύσεις IP, να ορίσετε όρια και να διατηρήσετε λειτουργική ασφάλεια:

### 1. Διαδρομή μέσω του δικτύου Tor
Αυτόματη δρομολόγηση αιτημάτων μέσω του τοπικού διακομιστή μεσολάβησης Tor SOCKS (`127.0.0.1:9050`):
```bash
sherlock --tor target_user
```
### 2. Προσαρμοσμένο διακομιστή μεσολάβησης HTTP / SOCKS5
```bash
# SOCKS5 proxy
sherlock --proxy socks5://127.0.0.1:1080 target_user

# Authenticated HTTP proxy
sherlock --proxy http://user:pass@proxy.corp.internal:8080 target_user
```
---

## Timeout & Threading Tuning

Ελέγξτε τις ταχύτητες αιτημάτων και τις ανοχές καθυστέρησης:
```bash
# Set HTTP timeout to 5 seconds per site (default is usually 60s)
sherlock target_user --timeout 5

# Add delay between requests to avoid rate limits
sherlock target_user --delay 1
```
---

## Μορφές εξαγωγής (CSV, XLSX, JSON)

Αποθηκεύστε ανακαλύψεις για έρευνες, πακέτα αποδεικτικών στοιχείων ή αναφορές:

### 1. Αποθήκευση απευθείας στο αρχείο CSV
```bash
sherlock --csv --output /tmp/sherlock_results target_user
```
### 2. Αποθηκεύστε την έξοδο σε συγκεκριμένο κατάλογο
```bash
sherlock --folderoutput /tmp/recon_data/ target_user
```
Δημιουργεί `/tmp/recon_data/target_user.txt` που περιέχει πλήρεις διευθύνσεις URL επιβεβαιωμένων προφίλ.

### 3. Αποθήκευση ως υπολογιστικό φύλλο του Excel (XLSX)
```bash
sherlock --xlsx target_user
```
---

## Αυτοματισμός & Ενσωμάτωση αγωγών OSINT

Chain Sherlock σε ένα αυτοματοποιημένο σενάριο ερευνητή:
```bash
#!/usr/bin/env bash
TARGET="$1"
OUTDIR="/tmp/osint_${TARGET}"
mkdir -p "$OUTDIR"

echo "[*] Scanning for username: $TARGET"
sherlock --print-found --timeout 10 --folderoutput "$OUTDIR" "$TARGET"

if [ -f "$OUTDIR/${TARGET}.txt" ]; then
    echo "[+] Found $(wc -l < "$OUTDIR/${TARGET}.txt") accounts:"
    cat "$OUTDIR/${TARGET}.txt"
fi
```