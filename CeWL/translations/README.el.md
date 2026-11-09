<!-- language-home-navigation -->
[🏠 Ελληνική Αρχική Σελίδα](../../README.el.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🕷️ CeWL - Ολοκληρωμένο Cheatsheet

[🇹🇷 Türkçe Sürüm](μεταφράσεις/README.tr.md)
```
   ██████╗███████╗██╗    ██╗██╗     
  ██╔════╝██╔════╝██║    ██║██║     
  ██║     █████╗  ██║ █╗ ██║██║     
  ██║     ██╔══╝  ██║███╗██║██║     
  ╚██████╗███████╗╚███╔███╔╝███████╗
   ╚═════╝╚══════╝ ╚══╝╚══╝ ╚══════╝
       Custom Word List Generator by Robin Wood (DigiNinja)
```
<p align="center">
  <img src="https://img.shields.io/badge/CeWL-Custom_Wordlist_Generator-blue?style=for-the-badge" alt="CeWL">
  <img src="https://img.shields.io/badge/Author-DigiNinja-orange?style=for-the-badge" alt="DigiNinja">
  <img src="https://img.shields.io/badge/Category-Password_Cracking-green?style=for-the-badge" alt="Παράβαση κωδικού πρόσβασης">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="License GPLv2">
</p>

<p align="center">
  <b>🌐 Δημιουργία λεξικών κωδικών πρόσβασης για συγκεκριμένους στόχους που αράχνει τους ιστότοπους για τη συλλογή εξειδικευμένης εταιρικής ορολογίας, επωνυμιών και μηνυμάτων ηλεκτρονικού ταχυδρομείου εργαζομένων</b>
</p>

---

## 📋 Πίνακας περιεχομένων

- [Overview & The Targeted Password Philosophy](#-overview--the-targeted-password-philosophy)
- [Εγκατάσταση] (#-εγκατάσταση)
- [Βασική ανίχνευση ιστότοπου](#-basic-website-crawling)
- [Στοιχεία ελέγχου βάθους και μήκους λέξεων] (#-βάθος αράχνης--έλεγχος μήκους λέξης)
- [Διευθύνσεις ηλεκτρονικού ταχυδρομείου συγκομιδής (`-e`)](#-harvesting-email-addresses--e)
- [Συμπεριλαμβανομένων αριθμών και πεζών γραμμάτων](#-συμπεριλαμβανομένων αριθμών--πεζών)
- [Εξαγωγή μεταδεδομένων εγγράφου (`-a`)](#-document-metadata-extraction--a)
- [Αριθμός λέξεων και προφίλ συχνότητας (`-c`)](#-πλήθος λέξεων--συχνότητα-προφίλ--γ)
- [Chaining CeWL with Hashcat & John Rules](#-chaining-cewl-with-hashcat--john-rules)
- [Πίνακας γρήγορης αναφοράς] (#-ταχεία αναφορά-πίνακας)
- [Defensive Considerations & OPSEC](#-defensive-considerations--opsec)
- [Νομική αποποίηση ευθυνών](#-νομική αποποίηση ευθυνών)

---

## 🎯 Επισκόπηση & Η φιλοσοφία του στοχευμένου κωδικού πρόσβασης

Οι γενικές λίστες λέξεων όπως το `rockyou.txt` είναι αποτελεσματικές για τους κωδικούς πρόσβασης καταναλωτών, αλλά **συχνά αποτυγχάνουν έναντι των πολιτικών εταιρικών κωδικών πρόσβασης** που απαιτούν 12+ χαρακτήρες, ειδικά σύμβολα και εποχιακές εναλλαγές.

Οι εργαζόμενοι βασίζουν τακτικά τους εταιρικούς κωδικούς πρόσβασης σε:
- Ονόματα έργων, εσωτερικές κωδικές ονομασίες και συνθήματα (`Pegasus2026!`).
- Συγκεκριμένη ορολογία του κλάδου, τεχνική ορολογία ή επωνυμίες προϊόντων.
- Ονόματα στελεχών, ορόσημα τοποθεσιών και διευθύνσεις γραφείων.

Δημιουργήθηκε από τον **Robin Wood (DigiNinja)**, ο **CeWL** (Προσαρμοσμένη δημιουργία λίστας λέξεων) είναι μια αράχνη Ruby που ανιχνεύει μια καθορισμένη διεύθυνση URL στόχου σε ένα καθορισμένο βάθος, εξάγει μοναδικές λέξεις που ταιριάζουν με τις απαιτήσεις ελάχιστου μήκους και δημιουργεί ένα λεξικό κωδικού πρόσβασης προσαρμοσμένο στην αφοσίωση.

---

## 📥 Εγκατάσταση
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y cewl

# Arch Linux (AUR)
yay -S cewl

# Ruby Gem
gem install cewl
```
---

## 🚀 Βασική ανίχνευση ιστότοπου
```bash
# Crawl website and output words to console
cewl https://target.local

# Crawl and save to a custom wordlist file
cewl https://target.local -w company_wordlist.txt
```
---

## 🕸️ Στοιχεία ελέγχου βάθους και μήκους λέξεων αράχνης

Ελέγξτε πόσο βαθιά το CeWL ακολουθεί συνδέσμους και ποια μεγέθη λέξεων αποτυπώνει:
```bash
# Crawl 2 links deep (-d 2) and enforce minimum word length of 8 characters (-m 8)
cewl https://target.local -d 2 -m 8 -w wordlist_min8.txt

# Aggressive 3-level deep crawl for comprehensive corporate terminology
cewl https://target.local -d 3 -m 7 --no-words -w deep_crawl.txt
```
---

## 📧 Συγκομιδή διευθύνσεων ηλεκτρονικού ταχυδρομείου (`-e`)

Εξαγωγή εταιρικών διευθύνσεων email για επακόλουθες προσομοιώσεις phishing ή ψεκασμό διαπιστευτηρίων:
```bash
# Extract emails to a separate dedicated file
cewl https://target.local -e --email_file emails.txt -w words.txt
```
---

## 🔡 Συμπεριλαμβανομένων αριθμών και πεζών
```bash
# Include words containing alphanumeric combinations (e.g., product names like V300, X86)
cewl https://target.local --with-numbers -m 6 -w words_with_numbers.txt

# Convert all discovered words to lowercase (great for rule generation)
cewl https://target.local --lowercase -w lowercase_words.txt
```
---

## 📄 Εξαγωγή μεταδεδομένων εγγράφου (`-a`)

Εξαγωγή ονομάτων χρήστη συντάκτη και δημιουργού από έγγραφα PDF και Microsoft Office που έχετε λάβει:
```bash
# Download and analyze document metadata to extract employee usernames
cewl https://target.local -a --meta_file extracted_authors.txt
```
---

## 📊 Καταμέτρηση λέξεων και προφίλ συχνότητας (`-c`)

Ταξινόμηση λέξεων κατά συχνότητα για να προσδιορίσετε τις πιο διάχυτες εταιρικές λέξεις:
```bash
# Append occurrence count next to each word
cewl https://target.local -c -m 6 -w frequency.txt

# Sort by most frequent words in descending order
sort -k2 -nr frequency.txt | head -n 30
```
---

## ⚡ Αλυσίδα CeWL με Hashcat & John Rules

Μια ακατέργαστη λίστα λέξεων CeWL περιέχει βασικές ρίζες λεξικού. Για να δημιουργήσετε βιώσιμους κωδικούς πρόσβασης, επεκτείνετε τη λίστα λέξεων χρησιμοποιώντας **Κανόνες μετάλλαξης Hashcat / John the Ripper**:
```bash
# 1. Harvest base words from corporate website
cewl https://target.local -m 5 --lowercase -w base_words.txt

# 2. Mutate words using Hashcat's best64 rule set
hashcat --force base_words.txt -r /usr/share/hashcat/rules/best64.rule --stdout | sort -u > mutated_passwords.txt

# Example mutations produced:
# Acmetool -> Acmetool2026!, !Acmetool123, 4cmet00l, Acmetool#1
```
---

## ⚡ Γρήγορος Πίνακας Αναφοράς

| Στόχος | Εντολή |
|-----------|---------|
| **Βασική ανίχνευση σε αρχείο** | `cewl <URL> -w wordlist.txt` |
| **Ρύθμιση ελάχιστου μήκους & βάθους** | `cewl <URL> -m 8 -d 2 -w wordlist.txt` |
| **Εξαγωγή εταιρικών email**| `cewl <URL> -e --email_file emails.txt` |
| **Εξαγωγή Μεταδεδομένων Εγγράφου**| `cewl <URL> -a --meta_file authors.txt` |
| **Συμπεριλάβετε αλφαριθμητικές λέξεις**| `cewl <URL> --with-numbers -w wordlist.txt` |
| **Έξοδος πεζών** | `cewl <URL> --lowercase -w wordlist.txt` |
| **Μετρήστε συχνότητες λέξεων** | `cewl <URL> -c -w frequencies.txt` |
| **Επαλήθευση ταυτότητας πίσω από τη σύνδεση**| `cewl <URL> -a "Cookie: session=xyz"` |

---

## 🛡️ Αμυντικές Θεωρήσεις & OPSEC

1. **Καταγραφή πρόσβασης διακομιστή Ιστού:** Το CeWL δημιουργεί εκατοντάδες γρήγορες αιτήσεις HTTP GET, παράγοντας αναγνωρίσιμα αρχεία καταγραφής πρόσβασης αράχνης. Χρησιμοποιήστε προσαρμοσμένες συμβολοσειρές παράγοντα χρήστη (`-u "Mozilla/5.0..."`) για ανάμειξη.
2. **Παγίδες Honeyword:** Οι αμυντικές ομάδες μπλε μπορούν να ενσωματώσουν αόρατους ή κρυφούς συνδέσμους HTML που οδηγούν σε διαπιστευτήρια καναρίνι για να επισημάνουν αυτοματοποιημένες αράχνες.

---

## ⚠️ Νομική αποποίηση ευθύνης

> [!ΠΡΟΣΟΧΗ]
> Το CeWL έχει σχεδιαστεί μόνο για εξουσιοδοτημένες αξιολογήσεις ασφαλείας, δοκιμές διείσδυσης και έρευνα ελέγχου κωδικού πρόσβασης. Απαγορεύεται αυστηρά η απόξεση ή η διάτρηση μη εξουσιοδοτημένων συστημάτων.