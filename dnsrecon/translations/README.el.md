<!-- language-home-navigation -->
[🏠 Ελληνική Αρχική Σελίδα](../../README.el.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# dnsrecon Cheatsheet

Το [dnsrecon](https://github.com/darkoperator/dnsrecon) είναι ένα ολοκληρωμένο εργαλείο αναγνώρισης και απαρίθμησης DNS γραμμένο σε Python. Υποστηρίζει μεταφορές ζωνών (AXFR), αντίστροφες αναζητήσεις, brute-forcing τομέα, ανακάλυψη εγγραφής SRV, ανίχνευση χαρακτήρων μπαλαντέρ, παρακολούθηση προσωρινής μνήμης και επέκταση TLD.

---

## Πίνακας περιεχομένων

- [Core Capabilities](#core-capabilities)
- [Εγκατάσταση](#installation)
- [Τυπική απαρίθμηση και μεταφορές ζωνών (`-d -t std / axfr`)](#standard-enumeration--zone-transfers--d--t-std--axfr)
- [DNS Subdomain Brute-Forcing (`-t brt`)](#dns-subdomain-brute-forcing--t-brt)
- [Σάρωση εύρους αντίστροφης αναζήτησης (`-t rvl`)](#reverse-lookup-range-scanning--t-rvl)
- [Υποβολή κρυφής μνήμης DNS (`-t snoop`)](#dns-cache-snooping--t-snoop)
- [Targeting Specific Nameservers (`-n`)](#targeting-specific-nameservers--n)
- [Μορφές εξόδου (CSV, JSON, SQLite, XML)](#output-formats-csv-json-sqlite-xml)
- [Defensive Hardening for DNS Servers](#defensive-hardening-for-dns-servers)

---

## Βασικές Δυνατότητες

- **Δοκιμή μεταφοράς ζώνης (AXFR):** Ελέγχει αυτόματα όλους τους έγκυρους διακομιστές ονομάτων για μη επαληθευμένες μεταφορές ζώνης.
- **Απαρίθμηση εγγραφών:** Ανακτά εγγραφές SOA, NS, A, AAAA, MX, TXT, SPF και SRV.
- **Subdomain Brute-Force:** Αναζητήσεις λεξικών πολλαπλών νημάτων με φιλτράρισμα χαρακτήρων μπαλαντέρ.
- **Αντίστροφη σάρωση εύρους IP:** Εξαγωγή εγγραφής PTR σε αυθαίρετα μπλοκ CIDR.
- **Υποβολή κρυφής μνήμης:** Ελέγχει εάν οι αναδρομικοί διακομιστές ονομάτων έχουν συγκεκριμένους τομείς αποθηκευμένους στη μνήμη.
- **Επέκταση TLD:** Ανακαλύπτει τομείς που ταιριάζουν μεταξύ κωδικών χώρας και γενικών τομέων ανώτατου επιπέδου.

---

## Εγκατάσταση
```bash
# Debian / Ubuntu / Kali Linux
sudo apt-get update && sudo apt-get install -y dnsrecon

# Python pip / pipx
pipx install dnsrecon

# From source
git clone https://github.com/darkoperator/dnsrecon.git
cd dnsrecon
pip install -r requirements.txt

# Verify installation
dnsrecon -h
```
---

## Τυπική απαρίθμηση και μεταφορές ζωνών (`-d -t std / axfr`)

### 1. Τυπική απαρίθμηση εγγραφών DNS
Συλλέγει εγγραφές SOA, NS, A, AAAA, MX και TXT και επιχειρεί αυτόματα μεταφορές ζώνης σε κάθε έγκυρο NS:
```bash
dnsrecon -d example.com -t std
```
### 2. Μόνο δοκιμή μεταφοράς ζώνης (AXFR).
Δοκιμάστε μόνο για ευπάθεια AXFR έναντι όλων των έγκυρων διακομιστών ονομάτων:
```bash
dnsrecon -d example.com -t axfr
```
*Εάν είναι επιτυχής, ολόκληρη η βάση δεδομένων DNS του στόχου απορρίπτεται χωρίς ωμή βία.*

---

## DNS Subdomain Brute-Forcing (`-t brt`)

Υποτομείς ωμής βίας που χρησιμοποιούν προσαρμοσμένες ή προεπιλεγμένες λίστες λέξεων:

### 1. Subdomain Brute-Force με προσαρμοσμένη λίστα λέξεων
```bash
dnsrecon -d example.com -t brt -D /usr/share/wordlists/dnsmap.txt
```
- `-d <domain>`: Τομέας στόχευσης.
- `-t brt`: Λειτουργία ωμής δύναμης.
- `-D <file>`: Αρχείο λεξικού υποτομέα.

### 2. Brute-Force με φιλτράρισμα χαρακτήρων μπαλαντέρ
Αποτρέπει τις διαμορφώσεις DNS με χαρακτήρες μπαλαντέρ (το `*.example.com` επιλύεται σε μια μοναδική IP) από ρυπογόνα αποτελέσματα:
```bash
dnsrecon -d example.com -t brt -D /usr/share/seclists/Discovery/DNS/subdomains-top1million-5000.txt --iw
```
- `--iw`: Αγνοήστε τα αποτελέσματα ανάλυσης χαρακτήρων μπαλαντέρ.

---

## Σάρωση εύρους αντίστροφης αναζήτησης (`-t rvl`)

Αριθμήστε εγγραφές PTR για ένα μπλοκ IP για να προσδιορίσετε εσωτερικούς κεντρικούς υπολογιστές, πύλες αλληλογραφίας και στιγμιότυπα σταδιακής λειτουργίας:
```bash
dnsrecon -r 192.168.1.0/24 -t rvl
```
- `-r <range>`: Εύρος IP σε συμβολισμό CIDR (π.χ. `192.168.1.0/24`) ή συμβολισμό έναρξης τέλους (`192.168.1.1-192.168.1.254`).
- `-t rvl`: Λειτουργία αντίστροφης αναζήτησης.

---

## Παρακολούθηση προσωρινής μνήμης DNS (`-t snoop`)

Προσδιορίστε εάν οι πελάτες σε ένα δίκτυο-στόχο έχουν επιλύσει πρόσφατα συγκεκριμένους τομείς μέσω του εσωτερικού τους προγράμματος επίλυσης:
```bash
dnsrecon -t snoop -n 192.168.1.1 -D sensitive_domains.txt
```
- `-n <resolver_ip>`: Στόχευση διακομιστή DNS για αναζήτηση.
- `-D <domains_file>`: Λίστα τομέων προς δοκιμή (π.χ. ανταγωνιστές, κακόβουλα C2, εσωτερικές πύλες).

---

## Στόχευση συγκεκριμένων διακομιστών ονομάτων (`-n`)

Υποβάλετε ερώτημα σε εσωτερικούς ή συγκεκριμένους διακομιστές DNS απευθείας αντί για δημόσιους αναλυτές ανάντη:
```bash
# Query the company's internal DNS server directly
dnsrecon -d corp.internal -n 10.10.10.2 -t std
```
---

## Μορφές εξόδου (CSV, JSON, SQLite, XML)

Αποθηκεύστε αποτελέσματα για ενσωμάτωση με έκδοση εισιτηρίων, SIEM ή περαιτέρω φάσεις επίθεσης:

### 1. Εξαγωγή σε JSON
```bash
dnsrecon -d example.com -t std --json /tmp/dns_recon.json
```
### 2. Εξαγωγή στη βάση δεδομένων SQLite
```bash
dnsrecon -d example.com -t std --db /tmp/dns_results.sqlite
```
### 3. Εξαγωγή σε CSV & XML
```bash
dnsrecon -d example.com -t std --csv /tmp/dns_results.csv --xml /tmp/dns_results.xml
```
---

## Defensive Hardening για διακομιστές DNS

| Ευπάθεια | Κίνδυνος εκμετάλλευσης | Προτεινόμενη άμυνα |
|---|---|---|
| **Απεριόριστο AXFR** | Πλήρης εσωτερική χαρτογράφηση υποδομής | Περιορίστε τις μεταφορές ζώνης μόνο σε εξουσιοδοτημένες δευτερεύουσες διευθύνσεις ονομάτων IP (`allow-transfer { 10.0.0.2; };` σε BIND). |
| **Άνοιγμα Recursive Resolver** | Ενίσχυση DNS DDoS & Snooping | Απενεργοποιήστε την εξωτερική αναδρομή (`recursion no;` σε BIND). |
| **Προβλέψιμα ονόματα κεντρικών υπολογιστών** | Υποτομέας brute-forcing | Χρησιμοποιήστε τυχαιοποιημένους υποτομείς για ευαίσθητα περιβάλλοντα σταδιοποίησης. |
| **Εξαγορά υποτομέα** | Το CNAME δείχνει σε μη διεκδικημένες υπηρεσίες AWS/GitHub/Azure | Ελέγξτε τις κρέμες εγγραφές DNS CNAME και αφαιρέστε αμέσως τις αχρησιμοποίητες αντιστοιχίσεις. |