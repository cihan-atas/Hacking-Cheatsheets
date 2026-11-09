<!-- language-home-navigation -->
[🏠 Ελληνική Αρχική Σελίδα](../../README.el.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ⌨️ rlwrap - Πλήρες φύλλο εξαπάτησης

[🇹🇷 Türkçe Sürüm](μεταφράσεις/README.tr.md)
```
  ██████╗ ██╗     ██╗    ██╗██████╗  █████╗ ██████╗ 
  ██╔══██╗██║     ██║    ██║██╔══██╗██╔══██╗██╔══██╗
  ██████╔╝██║     ██║ █╗ ██║██████╔╝███████║██████╔╝
  ██╔══██╗██║     ██║███╗██║██╔══██╗██╔══██║██╔═══╝ 
  ██║  ██║███████╗╚███╔███╔╝██║  ██║██║  ██║██║     
  ╚═╝  ╚═╝╚══════╝ ╚══╝╚══╝ ╚═╝  ╚═╝╚═╝  ╚═╝╚═╝     
       Readline Wrapper for Reverse Shells & Dumb Terminals
```
<p align="center">
  <img src="https://img.shields.io/badge/rlwrap-Readline_Wrapper-blue?style=for-the-badge" alt="rlwrap">
  <img src="https://img.shields.io/badge/Utility-Shell_Stabilization-green?style=for-the-badge" alt="Σταθεροποίηση κελύφους">
  <img src="https://img.shields.io/badge/Category-Exploitation_&_Shells-orange?style=for-the-badge" alt="Exploitation & Shells">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="License GPLv2">
</p>

<p align="center">
  <b>🌐 Μετατρέψτε τους ωμούς ακροατές Netcat και τα ανόητα αντίστροφα κελύφη σε πλήρεις διεπαφές γραμμής εντολών με πλήκτρα βέλους, ιστορικό και αυτόματη συμπλήρωση</b>
</p>

---

## 📋 Πίνακας περιεχομένων

- [Εισαγωγή: Το πρόβλημα "Χαζό κέλυφος"](#-introduction-the-dumb-shell-problem)
- [Εγκατάσταση] (#-εγκατάσταση)
- [Βασική χρήση](#-βασική χρήση)
- [Catching Reverse Shells (`rlwrap + nc`)](#-catching-reverse-shells-rlwrap--nc)
- [Διαχείριση μόνιμου ιστορικού](#-persistent-history-management)
- [Αυτόματη συμπλήρωση εντολών (`-f`)](#-εντολή-αυτόματη συμπλήρωση--f)
- [Full 5-Step TTY Shell Upgrade Workflow](#-full-5-step-tty-shell-upgrade-workflow)
- [Χρήση του rlwrap με άλλα εργαλεία Pentest] (#-using-rlwrap-with-other-pentest-tools)
  - [Oracle SQL*Plus](#oracle-sqlplus)
  - [Redis & Telnet](#redis--telnet)
  - [Socat & Custom Sockets](#socat--custom-sockets)
- [Πίνακας γρήγορης αναφοράς] (#-ταχεία αναφορά-πίνακας)
- [Συμβουλές και βέλτιστες πρακτικές] (#-συμβουλές--βέλτιστες πρακτικές)
- [Νομική αποποίηση ευθυνών](#-νομική αποποίηση ευθυνών)

---

## 🎯 Εισαγωγή: Το Πρόβλημα "Χαζό Κέλυφος".

Όταν πιάνετε ένα αντίστροφο κέλυφος χρησιμοποιώντας παραδοσιακά εργαλεία όπως το Netcat (`nc -lvnp 4444`), αποκτάτε ένα **χαζό τερματικό**. Ένα ανόητο τερματικό δεν διαθέτει τυπικές δυνατότητες επεξεργασίας γραμμής:

- ❌ Πατώντας τα πλήκτρα βέλους **Επάνω/Κάτω** εκτυπώνονται αλληλουχίες διαφυγής, όπως `^[[A` ή `^[[B`, αντί να κάνετε κυκλική μετάβαση στο ιστορικό εντολών.
- ❌ Πατώντας **Backspace** ή **Delete** εκτυπώνεται το `^H` ή το `^?` αντί για τη διαγραφή χαρακτήρων.
- ❌ Πατώντας **Ctrl + C** σκοτώνει αμέσως τον ακροατή αντίστροφου κελύφους, χάνοντας την πρόσβαση που αποκτήθηκε με κόπο.
- ❌ Εντολές όπως `su`, `sudo`, `nano`, `vim` ή `passwd` αποτυγχάνουν επειδή δεν υπάρχει συνδεδεμένο ψευδοτερματικό (PTY).
- ❌ Δεν διατηρείται μόνιμο ιστορικό εντολών σε όλες τις περιόδους λειτουργίας.

Το **rlwrap** (Readline Wrapper, γραμμένο από τον Hans Lub) το λύνει αυτό τοποθετώντας ανάμεσα στο τερματικό σας και την εντολή στόχου. Αναχαιτίζει τις πινελιές του πληκτρολογίου, αξιοποιεί τη **GNU Readline** και παρέχει πλοήγηση με βέλη, επεξεργασία γραμμής, μόνιμα αρχεία ιστορικού εντολών και αυτόματη συμπλήρωση βάσει λεξικού!

---

## 📥 Εγκατάσταση

Το `rlwrap` είναι ελαφρύ και διαθέσιμο στα προεπιλεγμένα αποθετήρια πακέτων σχεδόν κάθε διανομής Linux:

### Kali Linux, Debian & Ubuntu
```bash
# Update and install rlwrap
sudo apt update && sudo apt install -y rlwrap

# Verify installation and version
rlwrap --version
```
### Fedora, CentOS & RHEL
```bash
# Fedora / CentOS / RHEL
sudo dnf install -y rlwrap || sudo yum install -y epel-release && sudo yum install -y rlwrap
```
### Arch Linux
```bash
# Arch Linux / Manjaro
sudo pacman -S rlwrap
```
### macOS (Homebrew)
```bash
# macOS
brew install rlwrap
```
---

## 🚀 Βασική χρήση

Η βασική σύνταξη είναι απλώς η αναδίπλωση οποιασδήποτε γραμμής εντολών με το `rlwrap`:
```bash
# General syntax
rlwrap [options] <command> [command-arguments]
```
### Κοινές σημαίες

| Σημαία | Περιγραφή |
|------|-------------|
| `-c` | Ενεργοποίηση ολοκλήρωσης ονόματος αρχείου και διαδρομής |
| `-A` | Αγνοήστε τους κωδικούς διαφυγής μορφοποίησης προτροπής (αποτρέπει δυσλειτουργίες γραμμής προτροπής) |
| `-r` | Θυμηθείτε όλες τις λέξεις που εισαγάγετε στην εισαγωγή και βάλτε τις στη λίστα λέξεων συμπλήρωσης |
| `-H <file>` | Καθορίστε ένα αποκλειστικό αρχείο ιστορικού για αποθήκευση και ανάκληση εντολών |
| `-s <num>` | Ορίστε τον μέγιστο αριθμό γραμμών ιστορικού (προεπιλογή: 300) |
| `-f <file>` | Φόρτωση προσαρμοσμένου αρχείου λεξικού ολοκλήρωσης |
| `-b <chars>` | Ορίστε χαρακτήρες διάσπασης λέξεων |
| `-m` | Ενεργοποίηση επεξεργασίας εισαγωγής πολλών γραμμών |
| `-p[color]` | Χρωματίστε τη γραμμή εντολών |

---

## 🎣 Πιάνοντας αντίστροφα κοχύλια (`rlwrap + nc`)

Η πιο συνηθισμένη περίπτωση χρήσης στην επιθετική ασφάλεια είναι η αναδίπλωση ενός ακροατή Netcat (δείτε το [Netcat Cheatsheet](../Netcat/README.md) για παραλλαγές πλήρους ακροατής και σημαίες):
```bash
# Basic Netcat listener with arrow-key editing & history
rlwrap nc -lvnp 4444

# Professional pentester flags: -c (file completion), -A (prompt fix), -r (remember words)
rlwrap -cAr nc -lvnp 4444
```
### Επεξήγηση των σημαιών `-cAr`:

1. **`-c` (Ολοκλήρωση αρχείου):** Επιτρέπει τη χρήση του `<TAB>` για την αυτόματη συμπλήρωση ονομάτων αρχείων και καταλόγων.
2. **`-A` (Προτροπές):** Αποτρέπει τις περίεργες αναδιπλώσεις γραμμών και την επανάληψη προτροπής όταν ο απομακρυσμένος διακομιστής στέλνει μη τυποποιημένες συμβολοσειρές μηνυμάτων (π.χ. `root@victim:/#`).
3. **`-r` (Remember Words):** Μαθαίνει τις λέξεις που πληκτρολογείτε στο κέλυφος, ώστε να μπορείτε να τις συμπληρώσετε με καρτέλα αργότερα.

---

## 💾 Διαχείριση επίμονης ιστορίας

Από προεπιλογή, το `rlwrap` αποθηκεύει το ιστορικό εντολών στο `~/.<command>_history`. Μπορείτε να διαμορφώσετε προσαρμοσμένα αρχεία ιστορικού ανά αφοσίωση:
```bash
# Save history to a dedicated engagement file
rlwrap -H ~/.history_target_corp nc -lvnp 4444

# Increase history size to 5,000 entries
rlwrap -H ~/.history_target -s 5000 nc -lvnp 4444

# Case-insensitive history search
rlwrap -i nc -lvnp 4444
```
---

## 🔍 Αυτόματη συμπλήρωση εντολών (`-f`)

Μπορείτε να δώσετε στο `rlwrap` ένα αρχείο κειμένου που περιέχει κοινές εντολές Linux ή Windows και θα τις συμπληρώσει αυτόματα όταν πατήσετε το `<TAB>`:
```bash
# Create a wordlist of essential post-exploitation commands
cat << 'EOF' > /tmp/pentest_commands.txt
whoami
id
hostname
uname -a
cat /etc/passwd
cat /etc/shadow
sudo -l
find / -perm -4000 2>/dev/null
netstat -tulnp
ss -tulnp
ps aux
ip a
route -n
crontab -l
EOF

# Start listener with custom tab-completion dictionary
rlwrap -f /tmp/pentest_commands.txt nc -lvnp 4444
```
---

## 🛠️ Πλήρης ροή εργασιών αναβάθμισης κελύφους TTY 5 βημάτων

Ενώ το `rlwrap` παρέχει άμεση πλοήγηση με βέλος και ιστορικό, ορισμένα διαδραστικά προγράμματα (`su`, `sudo`, `vim`) εξακολουθούν να απαιτούν ένα πλήρως εκχωρημένο τερματικό TTY. Εδώ είναι ο βιομηχανικός αγωγός αναβάθμισης 5 βημάτων:

### Βήμα 1: Ξεκινήστε το πρόγραμμα ακρόασης με το rlwrap
```bash
# Attacker machine: start enhanced listener
rlwrap -cAr nc -lvnp 4444
```
### Βήμα 2: Ενεργοποίηση Reverse Shell & Spawn PTY

Μόλις το αντίστροφο κέλυφος συνδεθεί με τον ακροατή σας, δημιουργήστε ένα ψευδοτερματικό στον στόχο:
```bash
# Target shell: Spawn PTY via Python
python3 -c 'import pty; pty.spawn("/bin/bash")'

# OR via script if Python is unavailable:
/usr/bin/script -qc /bin/bash /dev/null
```
### Βήμα 3: Φόντο του κελύφους
```bash
# Press Ctrl + Z to put the reverse shell into the background
[1]+  Stopped                 rlwrap -cAr nc -lvnp 4444
```
### Βήμα 4: Διαμόρφωση τοπικού τερματικού Raw Mode
```bash
# Attacker machine: disable local echo and enable raw keystrokes
stty raw -echo; fg
```
*(Πατήστε Enter δύο φορές μετά την εκτέλεση του `fg` για επαναφορά της προτροπής)*

### Βήμα 5: Επαναφορά και ρύθμιση διαστάσεων τερματικού
```bash
# Target shell: set terminal type and environment
reset
export TERM=xterm-256color
export SHELL=bash

# Check your attacker terminal size in another window:
# $ stty size  -> Output: 38 160

# Apply rows and columns to target shell:
stty rows 38 columns 160
```
>>

---

## 🔧 Χρήση του rlwrap με άλλα εργαλεία Pentest

Το `rlwrap` δεν περιορίζεται στη Netcat. Κάθε εργαλείο CLI που δεν έχει επεξεργασία γραμμής επωφελείται από αυτό:

### Oracle SQL*Plus
```bash
# Oracle's sqlplus utility notoriously lacks line editing and history on Linux
rlwrap sqlplus scott/tiger@10.10.10.50:1521/ORCL
```
### Redis & Telnet
```bash
# Redis CLI interactive console
rlwrap redis-cli -h 10.10.10.50 -p 6379

# Raw Telnet or socket interaction
rlwrap telnet 10.10.10.50 25
```
### Socat & Custom Sockets
```bash
# Socat listener with rlwrap
rlwrap socat file:`tty`,raw,echo=0 tcp-listen:4444

# Connecting to raw sockets / CTF pwn challenges
rlwrap nc ctf.challenge.local 1337
```
---

## ⚡ Γρήγορος Πίνακας Αναφοράς

| Εργασία | Εντολή |
|------|---------|
| **Βελτιωμένη συσκευή ακρόασης Netcat** | `rlwrap nc -lvnp 4444` |
| **Σημαίες πλήρους αρχείου και προτροπής** | `rlwrap -cAr nc -lvnp 4444` |
| **Αφιερωμένο αρχείο ιστορίας** | `rlwrap -H ~/.nc_history nc -lvnp 4444` |
| **Προσαρμοσμένη καρτέλα-Ολοκλήρωση** | `rlwrap -f /path/to/wordlist.txt nc -lvnp 4444` |
| **Αύξηση μεγέθους ιστορικού** | `rlwrap -s 10000 nc -lvnp 4444` |
| **Τυλίξτε Oracle SQLPlus** | `rlwrap sqlplus user/pass@host/db` |
| **Τυλίξτε Redis CLI** | `rlwrap redis-cli -h 10.10.10.50` |
| **Wrap Socat listener** | `rlwrap socat TCP-LISTEN:4444,reuseaddr -` |

---

## 💡 Συμβουλές και βέλτιστες πρακτικές

1. **Δημιουργήστε ένα ψευδώνυμο Bash:** Προσθέστε αυτό στο `~/.bashrc` ή στο `~/.zshrc` σας για να μην ξεχάσετε ποτέ να το χρησιμοποιήσετε:
   ```bash
   alias nc='rlwrap -cAr nc'
   ```
2. **Ποτέ μην χάσετε `Ctrl+C` Ασφάλεια:** Όταν πιάνετε ασταθή κελύφη, το `rlwrap` παρεμποδίζει τις βασικές δυσλειτουργίες του πληκτρολογίου, αλλά να θυμάστε πάντα να κάνετε την αναβάθμιση `stty raw -echo; fg` 5 βημάτων εάν σκοπεύετε να εκτελέσετε μεγάλες διαδραστικές εντολές.
3. **Απομόνωση ιστορικών πελατών:** Όταν δοκιμάζετε πολλούς πελάτες ή μηχανήματα, χρησιμοποιείτε πάντα το `-H ~/.client_name.hist` για να διατηρείτε ξεχωριστές διαδρομές ελέγχου και ιστορικά εντολών.

---

## ⚠️ Νομική αποποίηση ευθύνης

> **ΣΗΜΑΝΤΙΚΟ:** Τα αντίστροφα κελύφη και τα εργαλεία μετά την εκμετάλλευση πρέπει να χρησιμοποιούνται μόνο σε εξουσιοδοτημένες δοκιμές διείσδυσης, δεσμεύσεις κόκκινων ομάδων και περιβάλλοντα εκπαιδευτικού εργαστηρίου με ρητή γραπτή συγκατάθεση.