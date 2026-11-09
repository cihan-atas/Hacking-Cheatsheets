<!-- language-home-navigation -->
[🏠 Ελληνική Αρχική Σελίδα](../../README.el.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🌐 WhatWeb - Ολοκληρωμένο Cheatsheet

[🇹🇷 Türkçe Sürüm](μεταφράσεις/README.tr.md)
```
  ██╗    ██╗██╗  ██╗ █████╗ ████████╗██╗    ██╗███████╗██████╗ 
  ██║    ██║██║  ██║██╔══██╗╚══██╔══╝██║    ██║██╔════╝██╔══██╗
  ██║ █╗ ██║███████║███████║   ██║   ██║ █╗ ██║█████╗  ██████╔╝
  ██║███╗██║██╔══██║██╔══██║   ██║   ██║███╗██║██╔══╝  ██╔══██╗
  ╚███╔███╔╝██║  ██║██║  ██║   ██║   ╚███╔███╔╝███████╗██████╔╝
   ╚══╝╚══╝ ╚═╝  ╚═╝╚═╝  ╚═╝   ╚═╝    ╚══╝╚══╝ ╚══════╝╚═════╝ 
       Next Generation Web Scanner & Technology Fingerprinter
```
<p align="center">
  <img src="https://img.shields.io/badge/WhatWeb-Web_Fingerprinter-blue?style=for-the-badge" alt="WhatWeb">
  <img src="https://img.shields.io/badge/Author-Andrew_Horton-orange?style=for-the-badge" alt="urbanadventurer">
  <img src="https://img.shields.io/badge/Category-Recon_&_Scanning-green?style=for-the-badge" alt="Recon & Scanning">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="License GPLv2">
</p>

<p align="center">
  <b>🌐 Σαρωτής Ιστού επόμενης γενιάς που έχει σχεδιαστεί για να αναγνωρίζει συστήματα διαχείρισης περιεχομένου (CMS), πλατφόρμες ιστολογίου, βιβλιοθήκες JavaScript και τεχνολογίες διακομιστών</b>
</p>

---

## 📋 Πίνακας περιεχομένων

- [Επισκόπηση και τεχνολογία δακτυλικών αποτυπωμάτων](#-επισκόπηση--τεχνολογία-δακτυλικό αποτύπωμα)
- [Εγκατάσταση] (#-εγκατάσταση)
- [Βασικό δακτυλικό αποτύπωμα ιστότοπου](#-basic-website-fingerprinting)
- [Επεξήγηση επιπέδων επιθετικότητας (`-a`)](#-επίπεδα-επιθετικότητας-εξηγούνται--α)
  - [Stealthy Passive Mode (`-a 1`)](#stealthy-passive-mode--a-1)
  - [Επιθετική λειτουργία (`-a 3`)](#aggressive-mode--a-3)
  - [Βαρύ / ωμή λειτουργία (`-a 4`)](#βαρύ--brute-mode--a-4)
- [Σάρωση υποδικτύων και αρχείων στόχων](#-scanning-subnets--target-files)
- [Σύστημα προσθήκης και επιλεκτικός έλεγχος] (#-πρόσθετο σύστημα--επιλεκτικός έλεγχος)
- [Καταγραφή εξόδου και εξαγωγή αναφοράς](#-output-logging--report-exporting)
- [Πίνακας γρήγορης αναφοράς] (#-ταχεία αναφορά-πίνακας)
- [Defensive Considerations & OPSEC](#-defensive-considerations--opsec)
- [Νομική αποποίηση ευθυνών](#-νομική αποποίηση ευθυνών)

---

## 🎯 Επισκόπηση & Τεχνολογία Δακτυλικών αποτυπωμάτων

Πριν ξεκινήσετε εκμεταλλεύσεις ή fuzzers brute-force εναντίον μιας διαδικτυακής εφαρμογής, είναι επιτακτική η αναγνώριση της ακριβούς στοίβας τεχνολογίας της.

Δημιουργήθηκε από τον **Andrew Horton (urbanadventurer)**, το **WhatWeb** αναγνωρίζει πάνω από 1.800 ξεχωριστές τεχνολογίες ιστού:
- **Συστήματα Διαχείρισης Περιεχομένου (CMS):** WordPress, Drupal, Joomla, Ghost, Magento.
- **Διακομιστές Ιστού και Διακομιστές μεσολάβησης:** Apache, Nginx, Microsoft-IIS, Cloudflare, Envoy.
- **Πλαίσια Backend:** Laravel, Django, Ruby on Rails, ASP.NET, Spring Boot.
- **Μπροστινές βιβλιοθήκες:** React, Vue.js, Angular, jQuery, Bootstrap.
- **Ενσωματωμένες συσκευές:** Κάμερες web, δρομολογητές, εκτυπωτές, διασυνδέσεις SCADA.

---

## 📥 Εγκατάσταση
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y whatweb

# Arch Linux
sudo pacman -S whatweb

# macOS (Homebrew)
brew install whatweb
```
---

## 🚀 Βασικό δακτυλικό αποτύπωμα ιστότοπου
```bash
# Scan a single domain
whatweb https://target.local

# Brief one-line output format
whatweb --color=never -q https://target.local

# Verbose detailed report showing evidence matches
whatweb -v https://target.local
```
---

## ⚡ Επεξηγημένα επίπεδα επιθετικότητας (`-a`)

Το WhatWeb εξισορροπεί το stealth με το βάθος χρησιμοποιώντας τέσσερα επίπεδα επιθετικότητας:

### Stealthy Passive Mode (`-a 1`)
Στέλνει ένα αίτημα HTTP GET στην παρεχόμενη διεύθυνση URL και αναλύει τις επιστρεφόμενες κεφαλίδες απόκρισης, τα cookie και τις ετικέτες HTML χωρίς να ζητήσει επιπλέον σελίδες:
```bash
whatweb -a 1 https://target.local
```
### Επιθετική λειτουργία (`-a 3`)
Συνιστάται για δοκιμές διείσδυσης. Ακολουθεί ανακατευθύνσεις και ζητά κοινά αρχεία προσθηκών (π.χ. `wp-login.php`, `robots.txt`, κατακερματισμοί favicon):
```bash
whatweb -a 3 https://target.local
```
### Λειτουργία βαριάς / ωμής (`-a 4`)
Προσπαθεί χιλιάδες εικασίες προσθηκών και γνωστές διευθύνσεις URL δακτυλικών αποτυπωμάτων:
```bash
whatweb -a 4 https://target.local
```
---

## 🌐 Σάρωση υποδικτύων & αρχείων στόχων
```bash
# Scan an entire CIDR network block
whatweb 192.168.1.0/24

# Scan multiple URLs listed in a text file
whatweb -i targets.txt

# Pipe live web endpoints from httpx into WhatWeb
cat live_hosts.txt | whatweb --no-errors
```
---

## 🧩 Σύστημα προσθηκών και επιλεκτικός έλεγχος

Το WhatWeb διαθέτει μια πλούσια αρχιτεκτονική προσθηκών:
```bash
# List all 1,800+ available plugins
whatweb -l

# Search for specific plugins (e.g., WordPress or Apache)
whatweb --grep wordpress

# Run ONLY a specific plugin against a target
whatweb --plugins="apache,php,wordpress" https://target.local
```
---

## 📊 Καταγραφή εξόδου και εξαγωγή αναφορών
```bash
# Export results to clean JSON format
whatweb https://target.local --log-json results.json

# Export to XML format
whatweb https://target.local --log-xml results.xml

# Export to compact CSV format
whatweb -i targets.txt --log-brief results.txt
```
---

## ⚡ Γρήγορος Πίνακας Αναφοράς

| Στόχος | Εντολή |
|-----------|---------|
| **Τυπική σάρωση** | `whatweb <URL>` |
| **Περιεχτική Αναλυτική Έκθεση** | `whatweb -v <URL>` |
| **Παθητική σάρωση Stealth** | `whatweb -a 1 <URL>` |
| **Επιθετική βαθιά σάρωση** | `whatweb -a 3 <URL>` |
| **Σάρωση υποδικτύου (CIDR)** | `whatweb <SUBNET>/24` |
| **Σάρωση στόχων από αρχείο** | `whatweb -i targets.txt` |
| **Εξαγωγή σε JSON** | `whatweb <URL> --log-json report.json` |
| **Φιλτράρισμα κατά συγκεκριμένες προσθήκες**| `whatweb --plugins="wordpress,nginx" <URL>` |

---

## 🛡️ Αμυντικές Θεωρήσεις & OPSEC

1. **Κάλυψη κεφαλίδας διακομιστή:** Καταργήστε ή τυχαιοποιήστε τις κεφαλίδες απόκρισης `Server` και `X-Powered-By` HTTP στο Apache/Nginx για να καταργήσετε το παθητικό δακτυλικό αποτύπωμα banner.
2. **Κάλυψη κατακερματισμού Favicon:** Το WhatWeb χρησιμοποιεί κατακερματισμούς MD5 του `/favicon.ico` για την αναγνώριση πλαισίων ιστού. Προσαρμόστε τα προεπιλεγμένα favicons πλαισίου.

---

## ⚠️ Νομική αποποίηση ευθύνης

> [!ΠΡΟΣΟΧΗ]
> Το WhatWeb προορίζεται μόνο για εξουσιοδοτημένη αναγνώριση, έλεγχο ασφαλείας και απογραφή συστήματος. Η σάρωση μη εξουσιοδοτημένων δικτύων ή εφαρμογών ιστού χωρίς άδεια ενδέχεται να παραβιάζει την ισχύουσα νομοθεσία.