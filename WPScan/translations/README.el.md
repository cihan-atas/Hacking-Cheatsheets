<!-- language-home-navigation -->
[🏠 Ελληνική Αρχική Σελίδα](../../README.el.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🛡️ WPScan - Πλήρες φύλλο εξαπάτησης

[🇹🇷 Türkçe Sürüm](μεταφράσεις/README.tr.md)
```
  ██╗    ██╗██████╗ ███████╗ ██████╗ █████╗ ███╗   ██╗
  ██║    ██║██╔══██╗██╔════╝██╔════╝██╔══██╗████╗  ██║
  ██║ █╗ ██║██████╔╝███████╗██║     ███████║██╔██╗ ██║
  ██║███╗██║██╔═══╝ ╚════██║██║     ██╔══██║██║╚██╗██║
  ╚███╔███╔╝██║     ███████║╚██████╗██║  ██║██║ ╚████║
   ╚══╝╚══╝ ╚═╝     ╚══════╝ ╚═════╝╚═╝  ╚═╝╚═╝  ╚═══╝
       The WordPress Security & Vulnerability Scanner
```
<p align="center">
  <img src="https://img.shields.io/badge/WPScan-WordPress_Security-blue?style=for-the-badge" alt="WPScan">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-orange?style=for-the-badge" alt="Ασφάλεια Ιστού">
  <img src="https://img.shields.io/badge/Database-WPVDB_API-green?style=for-the-badge" alt="WPVDB API">
  <img src="https://img.shields.io/badge/License-GPLv3-red?style=for-the-badge" alt="License GPLv3">
</p>

<p align="center">
  <b>🌐 Σαρωτής ευπάθειας μαύρου κουτιού που έχει σχεδιαστεί για να ανιχνεύει τον πυρήνα του WordPress, την προσθήκη, το θέμα και τα τρωτά σημεία του χρήστη</b>
</p>

---

## 📋 Πίνακας περιεχομένων

- [Επισκόπηση & Αρχιτεκτονική](#-επισκόπηση--αρχιτεκτονική)
- [Εγκατάσταση] (#-εγκατάσταση)
- [API Token Integration (WPVDB)](#-api-token-integration-wpvdb)
- [Βασική σάρωση και απαρίθμηση στόχου](#-βασική-σάρωση--αριθμός στόχου)
- [Ανάλυση σημαιών απαρίθμησης (`-e`)](#-enumeration-flags-breakdown--e)
  - [Vulnerable Plugins (`-e vp`)](#vulnerable-plugins--e-vp)
  - [Όλα τα πρόσθετα (`-e ap`)] (#all-plugins--e-ap)
  - [Θέματα (`-e vt`, `-e at`)](#themes--e-vt--e-at)
  - [Ονόματα χρήστη (`-e u`)](#usernames--e-u)
  - [Ευαίσθητα αντίγραφα ασφαλείας (`-e cb`, `-e dbe`)](#sensitive-backups--e-cb--e-dbe)
- [Λειτουργίες ανίχνευσης (`--plugins-detection`)](#-detection-modes---plugins-detection)
- [Password Brute-Forcing (`wp-login.php` & XML-RPC)](#-password-brute-forcing-wp-loginphp--xml-rpc)
- [Stealth & WAF Evasion](#-stealth--waf-evasion)
- [Πίνακας γρήγορης αναφοράς] (#-ταχεία αναφορά-πίνακας)
- [Defensive Considerations & Hardening](#-defensive-considerations--hardening)
- [Νομική αποποίηση ευθυνών](#-νομική αποποίηση ευθυνών)

---

## 🎯 Επισκόπηση & Αρχιτεκτονική

Με το WordPress να τροφοδοτεί περισσότερο από το 40% των κορυφαίων 10 εκατομμυρίων ιστότοπων, είναι το πιο στοχευμένο Σύστημα Διαχείρισης Περιεχομένου (CMS) στον κόσμο.

Γραμμένο σε Ruby, το **WPScan** είναι ο βιομηχανικός σαρωτής ευπάθειας μαύρου κουτιού για εγκαταστάσεις WordPress. Καταγράφει ενεργές βασικές εκδόσεις, απαριθμεί εγκατεστημένα πρόσθετα και θέματα, εξάγει καταχωρημένα ονόματα χρήστη διαχειριστή και τα συσχετίζει με τη **Βάση δεδομένων ευπάθειας WPScan (WPVDB)** που περιέχει χιλιάδες γνωστά CVE.

---

## 📥 Εγκατάσταση
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y wpscan

# Arch Linux
sudo pacman -S wpscan

# Ruby Gem (Any platform)
gem install wpscan

# Docker Container
docker run -it --rm wpscan/wpscan --url https://target.local
```
---

## 🔑 Ενσωμάτωση διακριτικού API (WPVDB)

Για να εμφανίσει γνωστά τρωτά σημεία και βαθμολογίες CVE, το WPScan απαιτεί ένα δωρεάν διακριτικό API από το [wpscan.com](https://wpscan.com/):
```bash
# Provide API token via CLI
wpscan --url https://target.local --api-token YOUR_API_TOKEN

# Or export to environment variable
export WPSCAN_API_TOKEN="YOUR_API_TOKEN"
```
---

## 🚀 Βασική σάρωση και απαρίθμηση στόχων
```bash
# Quick non-intrusive scan
wpscan --url https://target.local

# Update the local database before scanning
wpscan --update
```
---

## 🔍 Ανάλυση σημαιών απαρίθμησης (`-e`)

Το WPScan επιτρέπει την αρθρωτή απαρίθμηση στόχων χρησιμοποιώντας τη σημαία `--enumerate` (ή `-e`):

### Ευάλωτα πρόσθετα (`-e vp`)
Σαρώνει για προσθήκες που έχουν γνωστές ευπάθειες ασφαλείας στη βάση δεδομένων WPVDB:
```bash
wpscan --url https://target.local -e vp --api-token YOUR_TOKEN
```
### Όλες οι προσθήκες (`-e ap`)
Ανιχνευτές για χιλιάδες γνωστά πρόσθετα μέσω λεξικού brute-force:
```bash
wpscan --url https://target.local -e ap --plugins-detection aggressive
```
### Θέματα (`-e vt`, `-e at`)
```bash
# Enumerate vulnerable themes
wpscan --url https://target.local -e vt

# Enumerate all installed themes
wpscan --url https://target.local -e at
```
### Ονόματα χρήστη (`-e u`)
Εξάγει τα εγγεγραμμένα ονόματα χρήστη δημιουργών μέσω των τελικών σημείων REST API (`/wp-json/wp/v2/users`), των αρχείων συντακτών (`/?author=1`) και των μηνυμάτων σφάλματος σύνδεσης:
```bash
wpscan --url https://target.local -e u1-10
```
### Sensitive Backup (`-e cb`, `-e dbe`)
Αναζητήσεις για εκτεθειμένα `wp-config.php.bak`, αποτυπώματα βάσεων δεδομένων και αρχεία αντιγράφων ασφαλείας:
```bash
wpscan --url https://target.local -e cb,dbe
```
---

## ⚙️ Λειτουργίες ανίχνευσης (`--plugins-detection`)

Ελέγχει πόσο διεξοδικά το WPScan αναζητά προσθήκες:
```bash
# Passive (Fast, inspects HTML source, script tags, style links)
wpscan --url https://target.local --plugins-detection passive

# Aggressive (Sends HTTP requests for thousands of plugin directories)
wpscan --url https://target.local --plugins-detection aggressive

# Mixed (Recommended default - passive followed by targeted active requests)
wpscan --url https://target.local --plugins-detection mixed
```
---

## 🔓 Βίαιος εξαναγκασμός κωδικού πρόσβασης (`wp-login.php` & XML-RPC)

Μόλις απαριθμηθούν έγκυρα ονόματα χρήστη, εκκινήστε το στοχευμένο brute-forcing διαπιστευτηρίων:
```bash
# Brute force login using enumerated username list
wpscan --url https://target.local -U users.txt -P /usr/share/wordlists/rockyou.txt

# Password spray a single password across all users
wpscan --url https://target.local -U users.txt -p 'Winter2026!'

# Multi-threaded password attack
wpscan --url https://target.local -U admin -P passwords.txt -t 20
```
> [!ΣΗΜΕΙΩΣΗ]
> Εάν το `xmlrpc.php` είναι ενεργοποιημένο στο διακομιστή, το WPScan αξιοποιεί αυτόματα τις μεθόδους πολλαπλών κλήσεων XML-RPC για να δοκιμάσει εκατοντάδες συνδυασμούς κωδικών πρόσβασης ανά αίτημα HTTP!

---

## 🥷 Stealth & WAF Evasion
```bash
# Randomize User-Agent header to evade simple bot signatures
wpscan --url https://target.local --random-user-agent

# Throttle request rate (milliseconds between requests) to bypass rate limits
wpscan --url https://target.local --throttle 1000

# Route through SOCKS proxy / Tor
wpscan --url https://target.local --proxy socks5://127.0.0.1:9050

# Ignore SSL/TLS certificate warnings
wpscan --url https://target.local --disable-tls-checks
```
---

## ⚡ Γρήγορος Πίνακας Αναφοράς

| Στόχος | Εντολή |
|-----------|---------|
| **Τυπική σάρωση** | `wpscan --url <URL> --api-token <TOKEN>` |
| **Αριθμήστε ευάλωτα πρόσθετα** | `wpscan --url <URL> -e vp --api-token <TOKEN>` |
| **Αριθμήστε όλα τα πρόσθετα** | `wpscan --url <URL> -e ap --plugins-detection aggressive` |
| **Αριθμήστε Χρήστες** | `wpscan --url <URL> -e u` |
| **Αριθμήστε αντίγραφα ασφαλείας και ρυθμίσεις παραμέτρων** | `wpscan --url <URL> -e cb,dbe` |
| **Κωδικός Brute Force** | `wpscan --url <URL> -U <USERS> -P <PASSWORDS>` |
| **Τυχαίος πράκτορας χρήστη & Stealth** | `wpscan --url <URL> --random-user-agent --stealthy` |
| **Εξαγωγή σε αρχείο JSON** | `wpscan --url <URL> -f json -o wpscan_report.json` |

---

## 🛡️ Αμυντικές Θεωρήσεις & Σκλήρυνση

1. **Απενεργοποίηση XML-RPC:** Αποκλεισμός πρόσβασης στο `xmlrpc.php` μέσω διαμόρφωσης `.htaccess` ή Nginx για εξουδετέρωση επιθέσεων ωμής βίας πολλαπλών κλήσεων υψηλής ταχύτητας.
2. **Περιορίστε το τελικό σημείο των χρηστών REST API:** Απενεργοποιήστε τη δημόσια πρόσβαση στο `/wp-json/wp/v2/users` για να αποτρέψετε την ασήμαντη απαρίθμηση συντακτών.
3. **Προσθέτες ενημέρωσης κώδικα τακτικά:** Πάνω από το 90% των ευπαθειών του WordPress βρίσκονται σε προσθήκες και θέματα τρίτων και όχι στον πυρήνα του WordPress.

---

## ⚠️ Νομική αποποίηση ευθύνης

> [!ΠΡΟΣΟΧΗ]
> Αυτό το cheatsheet προορίζεται μόνο για εξουσιοδοτημένες δοκιμές διείσδυσης, αξιολογήσεις ευπάθειας και έλεγχο εφαρμογών ιστού. Η ωμή επιβολή κωδικών πρόσβασης ή συστημάτων σάρωσης χωρίς ρητή εξουσιοδότηση είναι παράνομη.