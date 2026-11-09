<!-- language-home-navigation -->
[🏠 Ελληνική Αρχική Σελίδα](../../README.el.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# CUPP (Common User Passwords Profiler) Cheatsheet

Το [CUPP](https://github.com/Mebus/cupp) (Common User Passwords Profiler) είναι μια εξειδικευμένη δημιουργία λεξικών κωδικών πρόσβασης που έχει σχεδιαστεί για στοχευμένη δημιουργία προφίλ ανθρώπου. Αντί να εξαναγκάζει με γενικά λεξικά, το CUPP δημιουργεί μεταθέσεις με βάση τα προσωπικά στοιχεία (όνομα στόχου, συνεργάτης, κατοικίδια, ημερομηνίες γέννησης, εταιρεία, χόμπι, παραλλαγές leet speak και ειδικούς συνδυασμούς χαρακτήρων).

---

## Πίνακας περιεχομένων

- [Επισκόπηση και περιπτώσεις χρήσης](#overview--use-cases)
- [Εγκατάσταση](#installation)
- [Οδηγός διαδραστικής δημιουργίας προφίλ (`-i`)](#interactive-profiling-wizard--i)
- [Configuration & Leet Rules (`cupp.cfg`)](#configuration--leet-rules-cuppcfg)
- [Λήψη και συγχώνευση λεξικών (`-l` & `-w`)](#downloading--merging-dictionaries--l---w)
- [Piping Output to Hashcat & Hydra](#piping-output-to-hashcat--hydra)
- [Defensive Insights & Password Policy Guidelines](#defensive-insights--password-policy-guidelines)

---

## Επισκόπηση & περιπτώσεις χρήσης

Οι επιθέσεις παραδοσιακών λεξικών συχνά αποτυγχάνουν έναντι πολύπλοκων κωδικών πρόσβασης που ακολουθούν μοτίβα προβλέψιμα από τον άνθρωπο (π.χ. `John1984!`, `Sarah_Fluffy2022#`). Το CUPP το αντιμετωπίζει με:
- Συλλογή προσωπικής νοημοσύνης για συγκεκριμένους στόχους.
- Συνδυασμός ονομάτων, ετών γέννησης, σημαντικών ημερομηνιών και ονομάτων κατοικίδιων ζώων.
- Εφαρμογή μεταλλάξεων που βασίζονται σε κανόνες (κεφαλαία, αντικατάσταση γραμμάτων, ειδικοί χαρακτήρες, προσάρτηση τυχαίων αριθμών).
- Δημιουργία μιας βελτιστοποιημένης, στοχευμένης λίστας λέξεων δραματικά μικρότερη από την `rockyou.txt` αλλά με υψηλότερη συνάφεια στόχου.

---

## Εγκατάσταση
```bash
# Clone the repository
git clone https://github.com/Mebus/cupp.git
cd cupp

# Kali Linux / Debian repository (if available)
sudo apt-get update && sudo apt-get install -y cupp

# Check help options
python3 cupp.py -h
```
---

## Διαδραστικός οδηγός προφίλ (`-i`)

Εκκινήστε την προτροπή ερωτηματολογίου για να δημιουργήσετε μια εξατομικευμένη λίστα λέξεων:
```bash
python3 cupp.py -i
```
Παράδειγμα άμεσης διερεύνησης:
```text
[+] Insert the targets information:
> First Name: Alice
> Surname: Smith
> Nickname: ally
> Birthdate (DDMMYYYY): 15061992

[+] Insert partners information:
> Partners name: Bob
> Partners nickname: bobby
> Partners birthdate (DDMMYYYY): 04111990

[+] Insert child's information:
> Child's name: Charlie
> Child's nickname: char
> Child's birthdate (DDMMYYYY): 20082018

[+] Insert pet's info:
> Pet's name: Luna

[+] Insert company name:
> Company name: CyberCorp

[+] Do you want to add some special words about the victim? Y/n: y
> (comma separated): football, chelsea, guitar, devops

[+] Do you want to add special characters at the end of words? Y/n: y
[+] Do you want to add some random numbers at the end of words? Y/n: y
[+] Leet mode? (i.e. leet = 1337) Y/n: y
```
Το αρχείο εξόδου `alice.csv` (ή καθορισμένο αρχείο κειμένου) δημιουργείται με χιλιάδες προσαρμοσμένες μεταλλάξεις (π.χ. `Alice1992!`, `LunaCharlie2018`, `CyberCorp@2022`).

---

## Κανόνες διαμόρφωσης και Leet (`cupp.cfg`)

Η διαμόρφωση CUPP ελέγχεται μέσω `cupp.cfg`. Οι βασικές ενότητες περιλαμβάνουν:
```ini
[years]
# Set year range for automated suffix appending
years = 1970,1980,1985,1990,1995,2000,2010,2020,2023,2024,2025,2026

[nums]
# Common number patterns appended to passwords
nums = 1,12,123,1234,12345,123456,777,999,007,69

[special]
# Special character appending
chars = !,@,#,$,%,&,*,-,_

[leet]
# Leet speak substitutions
a = 4,@
b = 8
e = 3
i = 1,!
o = 0
s = 5,$
t = 7
```
Επεξεργαστείτε το `cupp.cfg` για προσαρμογή στη στόχευση τοπικών ρυθμίσεων και ιδιαιτεροτήτων γλώσσας.

---

## Λήψη και συγχώνευση λεξικών (`-l` & `-w`)

### 1. Λήψη προκατασκευασμένων λιστών λέξεων από τα αποθετήρια
Λήψη προεπιλεγμένων γλωσσικών λιστών λέξεων:
```bash
python3 cupp.py -l
```
### 2. Βελτιώστε μια υπάρχουσα λίστα λέξεων με Leet / Ειδικούς κανόνες
Εφαρμόστε κανόνες μετάλλαξης CUPP σε μια λίστα με υπάρχουσες λέξεις:
```bash
python3 cupp.py -w /path/to/base_words.txt
```
---

## Έξοδος σωληνώσεων σε Hashcat & Hydra

Τροφοδοτήστε το δημιουργημένο προσαρμοσμένο λεξικό απευθείας σε εργαλεία brute-force:

### Σπάσιμο κατακερματισμών στόχων με Hashcat
```bash
# Generate targeted list
python3 cupp.py -i

# Run Hashcat against target NTLM hash
hashcat -m 1000 target_ntlm.hash alice.txt -r /usr/share/hashcat/rules/best64.rule
```
### Διαδικτυακός έλεγχος ταυτότητας Brute-Force με Hydra
```bash
hydra -l alice -P alice.txt 192.168.1.50 ssh
```
---

## Defensive Insights & Οδηγίες πολιτικής κωδικών πρόσβασης

- **Αποκλεισμός προσωπικών χαρακτηριστικών:** Απαγόρευση κωδικών πρόσβασης χρηστών που περιέχουν ονόματα, επώνυμα, ονόματα χρήστη, επωνυμίες εταιρείας ή ημερομηνίες γέννησης.
- **Εφαρμογή μήκους σε σχέση με την πολυπλοκότητα:** Μια φράση πρόσβασης 16+ χαρακτήρων (π.χ. τέσσερις τυχαίες λέξεις λεξικού) είναι σημαντικά πιο ανθεκτική στα εργαλεία δημιουργίας προφίλ από ένα μοτίβο `NameYear!` 8 χαρακτήρων.
- **Επαλήθευση ταυτότητας πολλαπλών παραγόντων (MFA):** Επιβολή κουπονιών υλικού FIDO2 ή TOTP, έτσι ώστε η δημιουργία προσαρμοσμένων λεξικών από μόνη της να μην μπορεί να θέσει σε κίνδυνο τους λογαριασμούς.