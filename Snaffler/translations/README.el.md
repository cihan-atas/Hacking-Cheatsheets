<!-- language-home-navigation -->
[🏠 Ελληνική Αρχική Σελίδα](../../README.el.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🐶 Snaffler - Ολοκληρωμένο Cheatsheet

[🇹🇷 Türkçe Sürüm](μεταφράσεις/README.tr.md)
```
  ███████╗███╗   ██╗ █████╗ ███████╗███████╗██╗     ███████╗██████╗ 
  ██╔════╝████╗  ██║██╔══██╗██╔════╝██╔════╝██║     ██╔════╝██╔══██╗
  ███████╗██╔██╗ ██║███████║█████╗  █████╗  ██║     █████╗  ██████╔╝
  ╚════██║██║╚██╗██║██╔══██║██╔══╝  ██╔══╝  ██║     ██╔══╝  ██╔══██╗
  ███████║██║ ╚████║██║  ██║██║     ██║     ███████╗███████╗██║  ██║
  ╚══════╝╚═╝  ╚═══╝╚═╝  ╚═╝╚═╝     ╚═╝     ╚══════╝╚══════╝╚═╝  ╚═╝
       Active Directory Domain SMB Share Credential & Secret Hunter
```
<p align="center">
  <img src="https://img.shields.io/badge/Snaffler-Secret_Hunter-blue?style=for-the-badge" alt="Snaffler">
  <img src="https://img.shields.io/badge/Language-C%23_/_NET-orange?style=for-the-badge" alt="C# / .NET">
  <img src="https://img.shields.io/badge/Category-Active_Directory_&_Network-green?style=for-the-badge" alt="Active Directory">
  <img src="https://img.shields.io/badge/License-GPLv3-red?style=for-the-badge" alt="License GPLv3">
</p>

<p align="center">
  <b>🌐 Αυτοματοποιημένο εργαλείο τομέα των Windows που έχει σχεδιαστεί για ανίχνευση κοινών χρήσεων SMB, εντοπισμό juicy αρχείων διαμόρφωσης, βάσεις δεδομένων KeePass και διαπιστευτήρια απλού κειμένου</b>
</p>

---

## 📋 Πίνακας περιεχομένων

- [Overview & The Share Hunting Problem](#-overview--the-share-hunting-problem)
- [Πώς λειτουργεί το Snaffler] (#-how-snaffler-works)
- [Επιλογές εγκατάστασης και εκτέλεσης](#-installation--execution-options)
- [Επίπεδα Snaffler Triage & Severity Levels] (#-snaffler-triage--severity-levels)
- [Κοινές ροές εργασίας σάρωσης] (#-κοινές ροές εργασίας σάρωσης)
  - [Ανίχνευση κοινής χρήσης σε επίπεδο τομέα](#domain-wide-share-crawl)
  - [Targeting Specific Hosts or Shares](#targeting-specific-hosts-or-shares)
  - [Εξαιρούνται οι υπολογιστές και οι ρίζες DFS](#excluding-computers--dfs-roots)
- [Juicy Targets & File Match Rules](#-juicy-targets--file-match-rules)
- [Καταγραφή και ανάλυση εξόδου](#-output-logging--analysis)
- [Πίνακας γρήγορης αναφοράς] (#-ταχεία αναφορά-πίνακας)
- [Defensive Considerations & OPSEC](#-defensive-considerations--opsec)
- [Νομική αποποίηση ευθυνών](#-νομική αποποίηση ευθυνών)

---

## 🎯 Επισκόπηση & Το Πρόβλημα Κυνηγιού Μεριδίων

Σε μεσαία έως μεγάλα περιβάλλοντα Active Directory, οι διακομιστές αρχείων και οι σταθμοί εργασίας για επιχειρήσεις φιλοξενούν χιλιάδες ανοιχτά κοινόχρηστα στοιχεία δικτύου SMB. Οι διαχειριστές συστήματος, οι προγραμματιστές και οι υπάλληλοι συχνά αφήνουν πίσω τους:

- 🔑 **Βάσεις δεδομένων KeePass (`.kdbx`):** Διατηρούν συχνά κύριους κωδικούς πρόσβασης ή κύρια κλειδιά που μπορούν να σπάσουν εύκολα.
- ⚙️ **Αρχεία διαμόρφωσης (`web.config`, `appsettings.json`):** Περιέχουν κωδικοποιημένες συμβολοσειρές σύνδεσης SQL, κωδικούς πρόσβασης λογαριασμού υπηρεσίας και κλειδιά API.
- 📜 **Σενάρια ανάπτυξης (`unattend.xml`, `.ps1`, `.bat`):** Διαπιστευτήρια διαχείρισης με σκληρό κώδικα.
- 🔏 **Ιδιωτικά πιστοποιητικά (`.pfx`, `.p12`, `.pem`, `.ppk`):** Μπορεί να χρησιμοποιηθεί για κλιμάκωση προνομίων ή πλευρική μετακίνηση.

Η μη αυτόματη περιήγηση σε χιλιάδες κοινές χρήσεις είναι αδύνατη. Το **Snaffler**, που αναπτύχθηκε από τον **l33tb455**, αυτοματοποιεί όλη αυτή τη διαδικασία: ζητά από την υπηρεσία καταλόγου Active Directory για αντικείμενα υπολογιστή, απαριθμεί αναγνώσιμα κοινόχρηστα στοιχεία SMB, περπατά στο δέντρο καταλόγου και αξιολογεί τα αρχεία σε σχέση με επιλεγμένα σύνολα κανόνων regex.

---

## ⚙️ Πώς λειτουργεί το Snaffler
```
[ Compromised Domain Machine ]
         │
         ├── 1. Queries Domain Controller via LDAP for Computer Objects
         ├── 2. Connects to port 445 (SMB) across all discovered hosts
         ├── 3. Enumerates Read/Write permissions on C$, SYSVOL, NETLOGON, Custom Shares
         └── 4. Evaluates file extensions, file names, and file contents against rules
```
---

## 📥 Επιλογές εγκατάστασης και εκτέλεσης

Το Snaffler έχει μεταγλωττιστεί ως αυτόνομο δυαδικό αρχείο .NET:
```bash
# Clone and build using Visual Studio or dotnet CLI on Windows
git clone https://github.com/SnaffCon/Snaffler.git
cd Snaffler
dotnet build -c Release
```
### Εκτέλεση σε Windows:
```cmd
# Standard execution outputting to screen and log file
Snaffler.exe -s -o snaffler_log.txt
```
### Εκτέλεση μέσω Cobalt Strike / C2 (Execute-Assembly):
```text
beacon> execute-assembly C:\Tools\Snaffler.exe -s -o C:\Temp\loot.txt
```
---

## 🚦 Snaffler Triage & Severity Levels

Ο Snaffler κατηγοριοποιεί τα ευρήματα σε κουβάδες επείγουσας ανάγκης με χρωματική κωδικοποίηση:

- 🔴 **`{Red}` / Μαύρο:** Σίγουρο εύρημα υψηλής αξίας! Κωδικοί πρόσβασης απλού κειμένου, ιδιωτικά κλειδιά (`.pfx`, `.id_rsa`), διακριτικά AWS, αρχεία εγκατάστασης χωρίς παρακολούθηση.
- **`{Yellow}`:** Πιθανό εύρημα. Αρχεία διαμόρφωσης με συμβολοσειρές κωδικού πρόσβασης, συμβολοσειρές σύνδεσης βάσης δεδομένων, σκιώδη αντίγραφα.
- **`{Green}`:** Ενημερωτικό / Ενδιαφέρον. Ονόματα αρχείων που ταιριάζουν με μοτίβα διαχείρισης (`passwords.xlsx`, `vpn.ovpn`).

---

## 🚀 Κοινές ροές εργασίας σάρωσης

### Ανίχνευση κοινής χρήσης σε επίπεδο τομέα

Σαρώστε όλα τα προσβάσιμα κοινόχρηστα στοιχεία σε ολόκληρο τον τομέα της υπηρεσίας καταλόγου Active Directory:
```cmd
# Run with live console streaming (-s) and save to disk
Snaffler.exe -s -o C:\loot\snaffler_domain.txt

# Run with multi-threading tuning (Default: 60 threads)
Snaffler.exe -s -t 100 -o C:\loot\snaffler_fast.txt
```
### Στόχευση συγκεκριμένων κεντρικών υπολογιστών ή κοινών χρήσεων

Όταν θέλετε να αποφύγετε τη σάρωση ολόκληρου του τομέα και να εστιάσετε σε διακομιστή αρχείων ή ελεγκτή τομέα:
```cmd
# Scan a single targeted file server
Snaffler.exe -c fs01.corp.local -s

# Scan a comma-separated list of machines
Snaffler.exe -c dc01.corp.local,fs02.corp.local,sql01.corp.local -s

# Scan a specific shared folder path directly
Snaffler.exe -i \\fs01.corp.local\Departments\IT\ -s
```
### Εξαιρούνται οι υπολογιστές και οι ρίζες DFS

Αποφύγετε τη σάρωση αργών ή ευαίσθητων υπολογιστών:
```cmd
# Exclude specific backup or surveillance servers
Snaffler.exe -s -c- backup01.corp.local,cctv01.corp.local
```
---

## 🎯 Juicy Targets & File Match Rules

Από προεπιλογή, το Snaffler αναζητά τις ακόλουθες κρίσιμες επεκτάσεις:

| Επέκταση / Αρχείο | Περιεχόμενο υψηλής αξίας |
|------------------|---------------------|
| `.kdbx` / `.kdb` | Χρηματοκιβώτια διαχείρισης κωδικών πρόσβασης KeePass |
| `web.config` / `app.config` | Κωδικοί πρόσβασης βάσης δεδομένων καθαρού κειμένου |
| `unattend.xml` / `sysprep.inf` | Κωδικοί πρόσβασης διαχειριστή των Windows |
| `.ppk` / `.pem` / `id_rsa` | Ιδιωτικά κλειδιά SSH |
| `.pfx` / `.p12` | Πιστοποιητικά Active Directory |
| `.rdp` / `.vnc` | Προφίλ σύνδεσης απομακρυσμένης επιφάνειας εργασίας |
| `passwords.xlsx` / `creds.txt` | Λίστες κωδικών πρόσβασης υπαλλήλων |

---

## 📊 Καταγραφή και ανάλυση εξόδων
```bash
# Grep for immediate RED credentials from a Linux terminal
grep -i "{Red}" snaffler_domain.txt

# Find all discovered KeePass safes
grep -i "\.kdbx" snaffler_domain.txt

# Filter out database connection strings
grep -i "password=" snaffler_domain.txt
```
---

## ⚡ Γρήγορος Πίνακας Αναφοράς

| Στόχος | Εντολή |
|-----------|---------|
| **Σάρωση σε επίπεδο τομέα** | `Snaffler.exe -s -o out.log` |
| **Στόχος μεμονωμένος υπολογιστής** | `Snaffler.exe -c <HOSTNAME> -s` |
| **Συγκεκριμένο Μερίδιο Στόχου** | `Snaffler.exe -i \\<HOST>\<SHARE> -s` |
| **Συντονισμός νημάτων ταυτόχρονης χρήσης**| `Snaffler.exe -s -t 120 -o out.log` |
| **Εξαίρεση κεντρικού υπολογιστή από τη σάρωση** | `Snaffler.exe -s -c- <EXCLUDE_HOST>` |
| **Μέγιστο όριο μεγέθους αρχείου**| `Snaffler.exe -s -z 5000000` *(Περιορισμός σε 5 MB αρχεία)* |
| **Εκτέλεση μέσω C2 Assembly**| `execute-assembly Snaffler.exe -s` |

---

## 🛡️ Αμυντικές Θεωρήσεις & OPSEC

1. **Μαζικές ακίδες σύνδεσης SMB:** Το Snaffler συνδέεται στη θύρα 445 σε κάθε υπολογιστή τομέα μέσα σε λίγα λεπτά. Αυτό δημιουργεί χιλιάδες Αναγνωριστικό συμβάντος 4624 (Επιτυχής Τύπος σύνδεσης 3 - Δίκτυο) και Αναγνωριστικό συμβάντος 5140 (Έγινε πρόσβαση σε ένα αντικείμενο κοινής χρήσης δικτύου).
2. **Honeyfiles & Decoys:** Οι σύγχρονοι υπερασπιστές Active Directory αναπτύσσουν κοινές χρήσεις Canary και παραπλανούν αρχεία `.kdbx` που ενεργοποιούν στιγμιαίες ειδοποιήσεις SOC όταν ανοίγονται από εργαλεία όπως το Snaffler.
3. **Όρια εκτέλεσης:** Ορίστε το `-z` (μέγιστο μέγεθος byte αρχείου) για να αποφύγετε τη συντριβή των κοινόχρηστων στοιχείων δικτύου διαβάζοντας αντίγραφα ασφαλείας πολλών gigabyte ISO ή VHD.

---

## ⚠️ Νομική αποποίηση ευθύνης

> [!ΠΡΟΣΟΧΗ]
> Το Snaffler έχει σχεδιαστεί αποκλειστικά για εξουσιοδοτημένες δοκιμές διείσδυσης, εμπλοκές κόκκινων ομάδων και εσωτερικούς ελέγχους ασφάλειας. Η σάρωση κοινών χρήσεων τομέα χωρίς εξουσιοδότηση συνιστά παράνομη παραβίαση δικτύου.