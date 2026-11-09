<!-- language-home-navigation -->
[🏠 Ελληνική Αρχική Σελίδα](../../README.el.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🪁 Kiterunner - Ολοκληρωμένο Cheatsheet

[🇹🇷 Türkçe Sürüm](μεταφράσεις/README.tr.md)
```
  ██╗  ██╗██╗████████╗███████╗██████╗ ██╗   ██╗███╗   ██╗███╗   ██╗███████╗██████╗ 
  ██║ ██╔╝██║╚══██╔══╝██╔════╝██╔══██╗██║   ██║████╗  ██║████╗  ██║██╔════╝██╔══██╗
  █████╔╝ ██║   ██║   █████╗  ██████╔╝██║   ██║██╔██╗ ██║██╔██╗ ██║█████╗  ██████╔╝
  ██╔═██╗ ██║   ██║   ██╔══╝  ██╔══██╗██║   ██║██║╚██╗██║██║╚██╗██║██╔══╝  ██╔══██╗
  ██║  ██╗██║   ██║   ███████╗██║  ██║╚██████╔╝██║ ╚████║██║ ╚████║███████╗██║  ██║
  ╚═╝  ╚═╝╚═╝   ╚═╝   ╚══════╝╚═╝  ╚═╝ ╚═════╝ ╚═╝  ╚═══╝╚═╝  ╚═══╝╚══════╝╚═╝  ╚═╝
       Context-Aware API Endpoint & Route Discovery Engine by Assetnote
```
<p align="center">
  <img src="https://img.shields.io/badge/Kiterunner-API_Discovery-blue?style=for-the-badge" alt="Kiterunner">
  <img src="https://img.shields.io/badge/Author-Assetnote-orange?style=for-the-badge" alt="Assetnote">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-green?style=for-the-badge" alt="Web Security & API">
  <img src="https://img.shields.io/badge/License-Apache_2.0-red?style=for-the-badge" alt="License Apache 2.0">
</p>

<p align="center">
  <b>🌐 Υψηλής απόδοσης REST και GraphQL API δρομολογούν brute-forcer σχεδιασμένο να χαρτογραφεί σύγχρονες αρχιτεκτονικές API χρησιμοποιώντας σύνολα δεδομένων συμφραζομένων</b>
</p>

---

## 📋 Πίνακας περιεχομένων

- [Overview & The API Discovery Problem](#-overview--the-api-discovery-problem)
- [Σύγκριση εργαλείων: Kiterunner vs ffuf vs dirsearch](#-tool-comparison-kiterunner-vs-ffuf-vs-dirsearch)
- [Εγκατάσταση] (#-εγκατάσταση)
- [Λήψη συνόλων δεδομένων Kite Assetnote](#-downloading-assetnote-kite-datasets)
- [Σάρωση API (`kr scan`)](#-scanning-apis-kr-scan)
- [Τυπική λίστα λέξεων Brute-Forcing (`kr brute`)](#-standard-wordlist-brute-forcing-kr-brute)
- [Κεφαλίδες, cookie και έλεγχος ταυτότητας](#-headers-cookies--authentication)
- [Κωδικοί φιλτραρίσματος και κατάστασης](#-φιλτράρισμα--κωδικοί κατάστασης)
- [Επανάληψη Ανακαλυφμένων Διαδρομών (Έξοδος `curl`)](#-replaying-discovered-routes-curl-output)
- [Πίνακας γρήγορης αναφοράς] (#-ταχεία αναφορά-πίνακας)
- [Defensive Considerations & OPSEC](#-defensive-considerations--opsec)
- [Νομική αποποίηση ευθυνών](#-νομική αποποίηση ευθυνών)

---

## 🎯 Επισκόπηση & Το πρόβλημα ανακάλυψης API

Τα παραδοσιακά εργαλεία ανακάλυψης περιεχομένου ιστού (`ffuf`, `dirsearch`, `gobuster`) χρησιμοποιούν επίπεδα, μονολεκτικά λεξικά (π.χ. `admin`, `api`, `login`). Αν και αυτό λειτουργεί για παραδοσιακές ιεραρχίες αρχείων, **αποτυγχάνει έναντι των σύγχρονων API REST και GraphQL**.

Τα σύγχρονα API χρησιμοποιούν ένθετες διαδρομές πολλαπλών επιπέδων όπως:
`/api/v2/organizations/{org_id}/users/export?format=json`

Το **Kiterunner**, που δημιουργήθηκε από την **Assetnote**, το λύνει αυτό συντάσσοντας ορισμούς OpenAPI, Swagger, Postman και σχημάτων πραγματικού κόσμου σε συμπαγή δυαδικά σύνολα δεδομένων (`.kite`). Αντί να μαντεύει τυχαίες λέξεις, το Kiterunner ανακατασκευάζει ολόκληρες δομές κλήσεων API με σωστές μεθόδους HTTP (`GET`, `POST`, `PUT`, `DELETE`).

---

## ⚖️ Σύγκριση εργαλείων: Kiterunner vs ffuf vs dirsearch

| Χαρακτηριστικό | Kiterunner (`kr`) | ffuf | dirsearch |
|---------|-------------------|------|-----------|
| **Εστίαση βασικού στόχου** | REST, GraphQL, OpenAPI | Γενικό Web Fuzzing | Κατάλογος Brute-Forcing |
| **Ενημέρωση Ιεραρχίας Διαδρομών**| ✅ Εγγενής (αρχεία `.kite`) | ❌ Επίπεδη λίστα λέξεων | ❌ Επίπεδη λίστα λέξεων |
| **Εναλλαγή ρημάτων HTTP** | ✅ Δοκιμές GET, POST, PUT, DELETE| ❌ Μη αυτόματη διαμόρφωση | ❌ Περιορισμένη |
| **Swagger / OpenAPI Parsing** | ✅ Απορρόφηση εγγενούς σχήματος | ❌ Όχι | ❌ Όχι |
| **Ταχύτητα & Συγχρονισμός** | ⭐⭐⭐⭐⭐ (Go Engine) | ⭐⭐⭐⭐⭐ (Go Engine) | ⭐⭐⭐⭐ (Python) |

---

## 📥 Εγκατάσταση
```bash
# Download precompiled binary from GitHub Releases
wget https://github.com/assetnote/kiterunner/releases/download/v1.0.2/kiterunner_1.0.2_linux_amd64.tar.gz
tar -xzvf kiterunner_1.0.2_linux_amd64.tar.gz
sudo mv kr /usr/local/bin/

# Verify installation
kr version
```
---

## 📦 Λήψη συνόλων δεδομένων Assetnote Kite

Το Kiterunner βασίζεται σε προκατασκευασμένα σύνολα δεδομένων διαδρομής `.kite` που έχουν δημιουργηθεί από εκατομμύρια αναλυμένα δημόσια API:
```bash
# Create wordlists directory
mkdir -p ~/.kiterunner && cd ~/.kiterunner

# Small dataset (~40MB, 2.5 million API routes - Recommended for fast scans)
wget https://wordlists-cdn.assetnote.io/data/kiterunner/routes-small.kite.tar.gz
tar -xzvf routes-small.kite.tar.gz

# Large dataset (~130MB, 8.5 million API routes - Exhaustive testing)
wget https://wordlists-cdn.assetnote.io/data/kiterunner/routes-large.kite.tar.gz
tar -xzvf routes-large.kite.tar.gz
```
---

## 🚀 API σάρωσης (`kr scan`)

Η εντολή `scan` χρησιμοποιεί σύνολα δεδομένων `.kite` για να διερευνήσει στόχους χρησιμοποιώντας ιεραρχίες διαδρομής με βάση τα συμφραζόμενα:
```bash
# Scan a single target using the small dataset
kr scan https://target.local/api -w ~/.kiterunner/routes-small.kite

# Scan multiple hosts from a file with 100 concurrent workers
kr scan targets.txt -w ~/.kiterunner/routes-small.kite -c 100 -j 50

# Scan with maximum verbosity
kr scan https://target.local -w ~/.kiterunner/routes-small.kite -v
```
---

## 🔨 Τυπική λίστα λέξεων Brute-Forcing (`kr brute`)

Εάν θέλετε να χρησιμοποιήσετε τυπικές λίστες λέξεων `.txt` ενώ εκμεταλλεύεστε τη μηχανή API πολλαπλών μεθόδων του Kiterunner:
```bash
# Brute force standard paths using a text dictionary
kr brute https://target.local -w /usr/share/wordlists/dirb/common.txt

# Brute force with custom API prefixes
kr brute https://target.local/api/v1 -w endpoints.txt
```
---

## 🔑 Κεφαλίδες, cookies και έλεγχος ταυτότητας

Τα API απαιτούν συχνά JWT, διακριτικά Bearer ή προσαρμοσμένες κεφαλίδες οργανισμού:
```bash
# Pass custom Authorization header
kr scan https://target.local/api -w routes-small.kite -H "Authorization: Bearer eyJhbGci..."

# Pass custom API key and Cookie
kr scan https://target.local -w routes-small.kite -H "X-API-Key: secret123" -H "Cookie: session=xyz"
```
---

## 🎯 Κωδικοί φιλτραρίσματος και κατάστασης
```bash
# Exclude standard error codes (404, 403, 502)
kr scan https://target.local -w routes-small.kite -x 404,403,502

# Filter out responses by length / content size
kr scan https://target.local -w routes-small.kite --filter-length 0,324
```
---

## 🔄 Επανάληψη ανακαλυφμένων διαδρομών (Έξοδος `curl`)

Το Kiterunner μπορεί να εξάγει διαδρομές που ανακαλύφθηκαν ως εντολές `curl` με δυνατότητα δράσης, επιτρέποντας την άμεση αναπαραγωγή στο τερματικό ή στο Burp Suite:
```bash
# Print reproducible curl command lines for all successful matches
kr scan https://target.local -w routes-small.kite -o json | jq -r '.[] | .curl'
```
---

## ⚡ Γρήγορος Πίνακας Αναφοράς

| Στόχος | Εντολή |
|-----------|---------|
| **Σάρωση API με μικρό σύνολο δεδομένων** | `kr scan <URL> -w routes-small.kite` |
| **Σάρωση API με μεγάλο σύνολο δεδομένων** | `kr scan <URL> -w routes-large.kite` |
| **Σάρωση πολλών κεντρικών υπολογιστών** | `kr scan hosts.txt -w routes-small.kite -c 100` |
| **Λίστα λέξεων κειμένου Brute Force** | `kr brute <URL> -w wordlist.txt` |
| **Επαλήθευση ταυτότητας με Bearer Token**| `kr scan <URL> -w routes-small.kite -H "Authorization: Bearer <TOKEN>"` |
| **Εξαίρεση κωδικών κατάστασης** | `kr scan <URL> -w routes-small.kite -x 404,403` |
| **Έξοδος σε αρχείο JSON** | `kr scan <URL> -w routes-small.kite -o json -oFile results.json` |

---

## 🛡️ Αμυντικές Θεωρήσεις & OPSEC

1. **Μαζικός όγκος αιτήματος:** Το λεξικό `routes-large.kite` δημιουργεί εκατομμύρια αιτήματα. Βεβαιωθείτε ότι ο περιορισμός ρυθμού (`-q 200`) είναι ενεργοποιημένος κατά τη δοκιμή περιβαλλόντων παραγωγής.
2. **Μέθοδοι αλλαγής κατάστασης:** Το Kiterunner στέλνει αιτήματα `POST`, `PUT` και `DELETE`. Σε μη σκληρυμένα δοκιμαστικά API, αυτό μπορεί να τροποποιήσει ή να διαγράψει ακούσια εγγραφές backend.

---

## ⚠️ Νομική αποποίηση ευθύνης

> [!ΠΡΟΣΟΧΗ]
> Το Kiterunner έχει σχεδιαστεί μόνο για εξουσιοδοτημένες δοκιμές ασφαλείας API και δεσμεύσεις bounty bug. Το Fuzzing μη εξουσιοδοτημένων API είναι αυστηρά παράνομο.