<!-- language-home-navigation -->
[🏠 Ελληνική Αρχική Σελίδα](../../README.el.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# Αναζήτηση sploit Cheatsheet

Το [Searchsploit](https://www.exploit-db.com/searchsploit) είναι το επίσημο βοηθητικό πρόγραμμα γραμμής εντολών για το [Exploit-Database](https://www.exploit-db.com/). Επιτρέπει σε επαγγελματίες ασφαλείας, ελεγκτές διείσδυσης και ερευνητές ευπάθειας να πραγματοποιούν αναζητήσεις εκτός σύνδεσης σε δεκάδες χιλιάδες δημοσιευμένα exploits, shellcodes και whitepapers χωρίς να στέλνουν κίνηση δικτύου σε μηχανές αναζήτησης τρίτων.

---

## Πίνακας περιεχομένων

- [Επισκόπηση & Αρχιτεκτονική](#overview--architecture)
- [Installation & Local Database Updates](#installation--local-database-updates)
- [Βασική και προηγμένη αναζήτηση εκμετάλλευσης](#basic--advanced-exploit-searching)
- [Εξέταση και αντιγραφή εκμεταλλεύσεων (`-m` / `-p` / `-x`)](#examining--copying-exploits--m---p---x)
- [Φιλτράρισμα ανά πλατφόρμα και τύπο] (#filtering-by-platform--type)
- [Nmap XML / Grepable Scanning Integration](#nmap-xml--grepable-scanning-integration)
- [JSON Output & Script Automation](#json-output--script-automation)
- [Best Practices in Penetration Testing] (#best-practices-in-penetration-testing)

---

## Επισκόπηση & Αρχιτεκτονική

Το Searchsploit ζητά έναν τοπικό κλώνο git του Exploit-DB που είναι αποθηκευμένος στο `/usr/share/exploitdb/`.
- **Δακτυλικό αποτύπωμα μηδενικού δικτύου:** Οι αναζητήσεις εκτελούνται σε τοπικές δομές CSV και καταλόγου.
- **Αξιοπιστία εκτός σύνδεσης:** Πλήρως λειτουργικό σε περιβάλλοντα στόχου με διάκενο αέρα και δίκτυα εμπλοκής.
- **Integrated Mirroring:** Αντιγράφει αρχεία εκμετάλλευσης απευθείας στον τρέχοντα κατάλογο εργασίας για άμεση εξέταση και τροποποίηση.

---

## Εγκατάσταση & Ενημερώσεις τοπικής βάσης δεδομένων

### Debian / Ubuntu / Kali Linux / Parrot OS
```bash
# Kali and Parrot include searchsploit by default
sudo apt-get update && sudo apt-get install -y exploitdb

# Update the local exploit database archive
searchsploit -u
```
### Εγκατάσταση Git Repository (Οποιαδήποτε διανομή Linux)
```bash
git clone https://gitlab.com/exploit-database/exploitdb.git /opt/exploitdb
sudo ln -sf /opt/exploitdb/searchsploit /usr/local/bin/searchsploit

# Check version and status
searchsploit -v
```
---

## Βασική και προηγμένη αναζήτηση εκμετάλλευσης

### 1. Απλή αναζήτηση λέξεων-κλειδιών
Αναζήτηση για συγκεκριμένες υπηρεσίες, δαίμονες ή λογισμικό:
```bash
searchsploit apache 2.4.49
searchsploit openssh 7.2
searchsploit wordpress core 5.8
```
### 2. Ακριβής αντιστοίχιση τίτλου (`-t`)
Περιορίστε τα ερωτήματα αναζήτησης αυστηρά για να εκμεταλλευτείτε τίτλους για να εξαλείψετε τις θορυβώδεις αντιστοιχίσεις περιγραφής:
```bash
searchsploit -t "Privilege Escalation" linux kernel
searchsploit -t "Remote Code Execution" smb
```
### 3. Αυστηρή αντιστοίχιση πεζών και όρων (`-e`)
Βρείτε ακριβείς αντιστοιχίσεις έκδοσης και μοτίβων:
```bash
searchsploit -e "Apache Struts"
```
### 4. Αναζήτηση κατά αναγνωριστικό CVE
Εκμεταλλεύσεις ερωτημάτων που αντιστοιχίζονται σε συγκεκριμένους αριθμούς κοινών ευπαθειών και εκθέσεων:
```bash
searchsploit --cve 2021-44228
searchsploit --cve 2017-0144
```
### 5. Εξαίρεση ανεπιθύμητων αποτελεσμάτων (`--exclude`)
Φιλτράρετε τα σενάρια άρνησης υπηρεσίας (DoS) ή βοηθητικά σενάρια κατά την αναζήτηση απομακρυσμένης εκτέλεσης κώδικα (RCE):
```bash
searchsploit linux kernel 5.4 --exclude="Denial of Service"
```
---

## Εξέταση και αντιγραφή εκμεταλλεύσεων (`-m` / `-p` / `-x`)

Ποτέ μην επεξεργάζεστε ή εκτελείτε εκμεταλλεύσεις απευθείας στον κατάλογο `/usr/share/exploitdb/`. Χρησιμοποιήστε τις λειτουργίες αντιγραφής και επιθεώρησης του Searchsploit.

### 1. Αντιγραφή (Mirror) Exploit στον τρέχοντα κατάλογο (`-m`)
Αντιγράψτε ένα αρχείο PoC exploit (Python, C, Ruby, bash) απευθείας στον τοπικό σας κατάλογο εργασίας:
```bash
# Mirror by Exploit-DB ID (EBD-ID)
searchsploit -m 50383

# Mirror by search path pattern
searchsploit -m windows/remote/48537.py
```
### 2. Επιθεώρηση μεταδεδομένων και πλήρους διαδρομής Exploit (`-p`)
Εμφάνιση της απόλυτης διαδρομής συστήματος αρχείων και της διεύθυνσης URL Exploit-DB για ένα EDB-ID:
```bash
searchsploit -p 50383
```
Παραγωγή:
```text
  Exploit: Apache HTTP Server 2.4.49 - Path Traversal & Remote Code Execution (RCE)
      URL: https://www.exploit-db.com/exploits/50383
     Path: /usr/share/exploitdb/exploits/multiple/webapps/50383.sh
File Type: Bourne-Again shell script, ASCII text executable
```
### 3. Διαβάστε τον Κώδικα Exploit στο Terminal Pager (`-x`)
Ανοίξτε τον κωδικό εκμετάλλευσης απευθείας στο `less` για γρήγορη επιθεώρηση:
```bash
searchsploit -x 50383
```
---

## Φιλτράρισμα ανά πλατφόρμα και τύπο

Το Searchsploit ευρετηριάζει εκμεταλλεύσεις, shellcode και ερευνητικές εργασίες σε πολλές αρχιτεκτονικές:

### 1. Φιλτράρισμα κατά στόχο λειτουργικό σύστημα
```bash
# Search only Linux exploits
searchsploit linux kernel 4.4

# Search only Windows exploits
searchsploit windows smb eternal

# Search web applications
searchsploit webapps tomcat
```
### 2. Φίλτρο μόνο για ακατέργαστους κώδικες κελύφους (`-s`)
Αναζητήστε ωφέλιμα φορτία κώδικα κελύφους για συγκεκριμένη αρχιτεκτονική (π.χ. execve `/bin/sh` χωρίς null byte):
```bash
searchsploit -s linux/x64 "execve"
searchsploit -s windows/x86 "reverse shell"
```
### 3. Φίλτρο για χαρτιά ασφαλείας και λευκές βίβλους (`-w`)
```bash
searchsploit -w "Active Directory Kerberos"
```
---

## Nmap XML / Ενσωμάτωση σάρωσης Grepable

Το Searchsploit μπορεί να αναλύσει αυτόματα τις εξόδους σάρωσης Nmap XML και να αντιστοιχίσει τις ανακαλυφθείσες εκδόσεις λογισμικού με γνωστά exploit:
```bash
# 1. Perform service detection and save XML output
nmap -sV -p- -T4 -oX /tmp/target_scan.xml 192.168.1.50

# 2. Feed the XML scan file directly to Searchsploit
searchsploit --nmap /tmp/target_scan.xml
```
*Το Searchsploit επιθεωρεί κάθε banner ανοιχτής θύρας και συμβολοσειρά έκδοσης, ζητώντας τοπικά exploit για άμεση διαλογή.*

---

## Αυτοματισμός εξόδου και σεναρίου JSON

Για τον αυτοματισμό αγωγών και τα προσαρμοσμένα εργαλεία αναγνώρισης, τα αποτελέσματα εξόδου σε δομημένο JSON:
```bash
searchsploit "Apache Struts" --json > struts_exploits.json
```
Φιλτράρετε διαδρομές αρχείων εκμετάλλευσης χρησιμοποιώντας το `jq`:
```bash
searchsploit --cve 2021-41773 --json | jq -r '.RESULTS_EXPLOIT[].Path'
```
---

## Βέλτιστες πρακτικές στις δοκιμές διείσδυσης

1. **Επαλήθευση πριν από την εκτέλεση:** Να ελέγχετε πάντα τον πηγαίο κώδικα εκμετάλλευσης (`searchsploit -x <id>`) πριν από τη μεταγλώττιση ή την εκτέλεση. Επαληθεύστε τον σκληρό κώδικα κελύφους, στοχεύστε τις διευθύνσεις IP και βεβαιωθείτε ότι δεν υπάρχουν κερκόπορτες ή κακόβουλοι σταθμοί ωφέλιμου φορτίου.
2. **Μεταγλωττίσεις με σωστές σημαίες:** Πολλά exploit-DB C απαιτούν συγκεκριμένες σημαίες GCC (π.χ. `-m32`, `-lpthread` ή `-lcrypto`):
   ```bash
   searchsploit -m 40839
   gcc -pthread 40839.c -o dirtycow -lcrypt
   ```
3. **Διατήρηση της βάσης δεδομένων ενημερωμένη:** Εκτελέστε το `searchsploit -u` τακτικά για να διασφαλίσετε ότι οι πρόσφατα δημοσιευμένες μηδενικές ημέρες και οι αποδείξεις εννοιών έχουν ευρετηριαστεί τοπικά.