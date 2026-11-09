<!-- language-home-navigation -->
[🏠 Ελληνική Αρχική Σελίδα](../../README.el.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🌲 adidnsdump - Πλήρες φύλλο εξαπάτησης

[🇹🇷 Türkçe Sürüm](μεταφράσεις/README.tr.md)
```
   █████╗ ██████╗ ██╗██████╗ ███╗   ██╗███████╗██████╗ ██╗   ██╗███╗   ███╗██████╗ 
  ██╔══██╗██╔══██╗██║██╔══██╗████╗  ██║██╔════╝██╔══██╗██║   ██║████╗ ████║██╔══██╗
  ███████║██║  ██║██║██║  ██║██╔██╗ ██║███████╗██║  ██║██║   ██║██╔████╔██║██████╔╝
  ██╔══██║██║  ██║██║██║  ██║██║╚██╗██║╚════██║██║  ██║██║   ██║██║╚██╔╝██║██╔═══╝ 
  ██║  ██║██████╔╝██║██████╔╝██║ ╚████║███████║██████╔╝╚██████╔╝██║ ╚═╝ ██║██║     
  ╚═╝  ╚═╝╚═════╝ ╚═╝╚═════╝ ╚═╝  ╚═══╝╚══════╝╚═════╝  ╚═════╝ ╚═╝     ╚═╝╚═╝     
           Active Directory Integrated DNS (ADIDNS) Reconnaissance Tool
```
<p align="center">
  <img src="https://img.shields.io/badge/Tool-adidnsdump-blue?style=for-the-badge" alt="adidnsdump">
  <img src="https://img.shields.io/badge/Author-Dirk--jan_Mollema-orange?style=for-the-badge" alt="dirkjanm">
  <img src="https://img.shields.io/badge/Category-Active_Directory-green?style=for-the-badge" alt="Active Directory">
  <img src="https://img.shields.io/badge/Protocol-LDAP_%2F_LDAPS-purple?style=for-the-badge" alt="LDAP">
</p>

<p align="center">
  <b>🌲 Απαριθμήστε και απορρίψτε τις ενσωματωμένες ζώνες και εγγραφές Active Directory DNS (ADIDNS) μέσω LDAP χωρίς μεταφορές ζώνης DNS (AXFR)</b>
</p>

---

## 📋 Πίνακας περιεχομένων

- [Επισκόπηση και τεχνικό υπόβαθρο] (#-επισκόπηση--τεχνικό-υπόβαθρο)
- [Εγκατάσταση] (#-εγκατάσταση)
- [Μέθοδοι ελέγχου ταυτότητας](#-authentication-methods)
  - [Επαλήθευση κωδικού πρόσβασης](#password-authentication)
  - [Pass-the-Hash (NTLM)](#pass-the-hash-ntlm)
  - [Εισιτήριο Kerberos (ccache)](#kerberos-ticket-ccache)
- [Εντολές πυρήνων ντάμπινγκ](#-core-dumping-commands)
  - [Standard Active Directory DNS Zone Dump](#standard-active-directory-dns-zone-dump)
  - [Damping Tombstoned / Deleted Records](#dumping-tombstoned--deleted-records)
  - [Damping Forest DNS & Domain DNS Zones](#dumping-forest-dns--domain-dns-zones)
  - [Targeting Specific DNS Zones](#targeting-specific-dns-zones)
- [Μορφές εξαγωγής και μετα-επεξεργασία](#-εξαγωγή-μορφές--μετα-επεξεργασία)
  - [Έξοδος CSV](#csv-output)
  - [Integrating with BloodHound & Nmap](#integrating-with-bloodhound--nmap)
- [Πίνακας γρήγορης αναφοράς] (#-ταχεία αναφορά-πίνακας)
- [Defensive Considerations & Detection](#-defensive-considerations--detection)
- [Νομική αποποίηση ευθυνών](#-νομική αποποίηση ευθυνών)

---

## 🎯 Επισκόπηση & τεχνικό υπόβαθρο

Σε τυπικά περιβάλλοντα Active Directory των Windows, το DNS συνήθως ενσωματώνεται στην υπηρεσία καταλόγου Active Directory (**Active Directory-Integrated DNS / ADIDNS**). Σε αυτές τις διαμορφώσεις, οι εγγραφές DNS δεν αποθηκεύονται σε απλά αρχεία κειμένου, αλλά ως αντικείμενα LDAP εντός του διαμερίσματος τομέα (όπως `DC=DomainDnsZones` και `DC=ForestDnsZones`).

Οι παραδοσιακές μεταφορές ζώνης DNS (`AXFR`) είναι σχεδόν καθολικά απενεργοποιημένες στους ελεγκτές τομέα. Ωστόσο, **οποιοσδήποτε χρήστης με έλεγχο ταυτότητας τομέα** (ακόμη και λογαριασμοί χαμηλών προνομίων ή λογαριασμοί υπηρεσιών σε κίνδυνο) έχει δικαιώματα ανάγνωσης στους κόμβους LDAP που αποθηκεύουν εγγραφές ADIDNS από προεπιλογή.

Αναπτύχθηκε από τον **Dirk-jan Mollema (@dirkjanm)**, ο **`adidnsdump`** συνδέεται με τον ελεγκτή τομέα μέσω LDAP ή LDAPS, υποβάλλει ερωτήματα στα διαμερίσματα ADIDNS και εξάγει όλες τις εγγραφές (A, AAAA, CNAME, SRV, TXT, MX και δημιουργώντας κάθε εσωτερική και ακριβή υπηρεσία υπολογιστών, εγγραφών ιστιοπλοΐας). τον τομέα.

---

## 📥 Εγκατάσταση
```bash
# Clone the repository and install with pip
git clone https://github.com/dirkjanm/adidnsdump.git
cd adidnsdump
pip install .

# Or install directly via pipx / pip
pipx install git+https://github.com/dirkjanm/adidnsdump.git
```
---

## 🔑 Μέθοδοι ελέγχου ταυτότητας

### Έλεγχος ταυτότητας με κωδικό πρόσβασης
```bash
adidnsdump -u 'CORP\jdoe' -p 'P@ssw0rd123!' 192.168.1.10
```
### Pass-the-Hash (NTLM)
Εάν έχετε εξαγάγει κατακερματισμούς NTLM μέσω Mimikatz ή Secretsdump:
```bash
adidnsdump -u 'CORP\jdoe' --hashes :31d6cfe0d16ae931b73c59d7e0c089c0 192.168.1.10
```
### Εισιτήριο Kerberos (cache)
Χρησιμοποιώντας ένα υπάρχον εισιτήριο Kerberos TGT/TGS που είναι αποθηκευμένο στο `KRB5CCNAME`:
```bash
export KRB5CCNAME=/tmp/jdoe.ccache
adidnsdump -k -u 'CORP\jdoe' dc01.corp.local
```
---

## 🚀 Εντολές Dumping Core

### Τυπική ένδειξη Active Directory DNS Zone Dump
Καταργεί όλες τις ενεργές εγγραφές DNS από το προεπιλεγμένο διαμέρισμα DomainDnsZones:
```bash
adidnsdump -u 'corp.local\jdoe' -p 'P@ssw0rd123!' ldap://dc01.corp.local
```
Αυτό δημιουργεί ένα αρχείο με το όνομα `records.csv` στον τρέχοντα κατάλογο εργασίας σας.

### Dumping Tombstoned / Διαγραμμένα αρχεία
Το DNS των Windows επισημαίνει τις διαγραμμένες εγγραφές ως "ταφόπλακες" για αρκετές ημέρες πριν από τη φυσική διαγραφή. Αυτές οι εγγραφές συχνά αποκαλύπτουν παροπλισμένα συστήματα, διακομιστές παλαιού τύπου ή περιβάλλοντα δοκιμής:
```bash
adidnsdump -u 'corp.local\jdoe' -p 'P@ssw0rd123!' --include-tombstoned ldap://dc01.corp.local
```
### Dumping Forest DNS & Domain DNS Zones
Εξαγωγή εγγραφών από κατατμήσεις DNS σε επίπεδο Forest:
```bash
adidnsdump -u 'corp.local\jdoe' -p 'P@ssw0rd123!' --forest ldap://dc01.corp.local
```
### Στόχευση συγκεκριμένων ζωνών DNS
Εάν ο στόχος λειτουργεί πολλαπλές ζώνες ή υποτομείς DNS διαχωρισμένου εγκεφάλου:
```bash
adidnsdump -u 'corp.local\jdoe' -p 'P@ssw0rd123!' --zone dev.corp.local ldap://dc01.corp.local
```
### Ασφαλής κρυπτογραφημένη επικοινωνία LDAPS
Αποτρέψτε τη διέλευση διαπιστευτηρίων στο δίκτυο σε απλό κείμενο:
```bash
adidnsdump --ssl -u 'corp.local\jdoe' -p 'P@ssw0rd123!' ldaps://dc01.corp.local
```
---

## 📊 Μορφές εξαγωγής και μετα-επεξεργασία

### Έξοδος CSV
Από προεπιλογή, το `adidnsdump` δίνει έξοδο `records.csv`. Τα πεδία περιλαμβάνουν:
- `zone`: Η ζώνη DNS στην οποία ανήκει η εγγραφή.
- `name`: Εγγραφή ονόματος κεντρικού υπολογιστή ή ετικέτας.
- `type`: Τύπος εγγραφής (A, AAAA, CNAME, SRV, κ.λπ.).
- `value`: Διεύθυνση IP στόχου, όνομα κεντρικού υπολογιστή ή συμβολοσειρά.

### Ενσωμάτωση με BloodHound & Nmap
Εξαγάγετε όλες τις μοναδικές διευθύνσεις IP από τις εγγραφές που απορρίπτονται για αυτοματοποιημένη σάρωση:
```bash
# Filter unique IP addresses (Type A records)
awk -F',' '$3=="A" {print $4}' records.csv | sort -u > live_ips.txt

# Run fast port scan against newly discovered internal hosts
nmap -sS -Pn -iL live_ips.txt -T4 -p 80,443,445,3389 -oA internal_assets
```
---

## ⚡ Γρήγορος Πίνακας Αναφοράς

| Στόχος | Εντολή |
|-----------|---------|
| **Τυπικό ADIDNS Dump** | `adidnsdump -u 'DOMAIN\User' -p 'Pass' <DC_IP>` |
| **Pass-the-Hash (NTLM)** | `adidnsdump -u 'DOMAIN\User' --hashes :<NTLM> <DC_IP>` |
| **Kerberos Ticket Auth** | `adidnsdump -k -u 'DOMAIN\User' <DC_FQDN>` |
| **Dump Tombstoned Records**| `adidnsdump -u 'DOMAIN\User' -p 'Pass' --include-tombstoned <DC_IP>` |
| **Χωματερές δασικές ζώνες** | `adidnsdump -u 'DOMAIN\User' -p 'Pass' --forest <DC_IP>` |
| **Καθορίστε τη ζώνη στόχο** | `adidnsdump -u 'DOMAIN\User' -p 'Pass' --zone <ZONE> <DC_IP>` |
| **Εφαρμογή LDAPS (SSL)** | `adidnsdump --ssl -u 'DOMAIN\User' -p 'Pass' ldaps://<DC_IP>` |

---

## 🛡️ Αμυντικές Θεωρήσεις & Ανίχνευση

1. **Παρακολούθηση ερωτήματος LDAP:** Ενεργοποιήστε τη σύνθετη πολιτική ελέγχου για την πρόσβαση στην υπηρεσία καταλόγου (Αναγνωριστικό συμβάντος 4662) για ειδοποίηση σε ερωτήματα μεγάλου όγκου έναντι του `CN=MicrosoftDNS,DC=DomainDnsZones`.
2. **Περιορισμός ADIDNS ACL:** Περιορίστε τα δικαιώματα ανάγνωσης στο κοντέινερ `MicrosoftDNS` σε διαχειριστές τομέα ή σε συγκεκριμένους ρόλους διαχείρισης, εάν οι μη διαχειριστές δεν απαιτούν εσωτερική ενδοσκόπηση του DNS.
3. **Honeytoken:** Εγκαταστήστε ελκυστικές εγγραφές DNS (π.χ. `backup-vault.corp.local` ή `admin-portal.corp.local`) που δείχνουν τις διευθύνσεις IP του honeypot.

---

## ⚠️ Νομική αποποίηση ευθύνης

> [!ΠΡΟΣΟΧΗ]
> Το adidnsdump προορίζεται αυστηρά για εξουσιοδοτημένες αξιολογήσεις ασφαλείας, ελέγχους Active Directory και δοκιμές διείσδυσης εντός των επιτρεπόμενων πλαισίων. Η υποβολή ερωτημάτων σε υπηρεσίες καταλόγου σε μη εξουσιοδοτημένα δίκτυα είναι παράνομη.