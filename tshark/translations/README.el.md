<!-- language-home-navigation -->
[🏠 Ελληνική Αρχική Σελίδα](../../README.el.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🦈 tshark - Ολοκληρωμένο Cheatsheet

[🇹🇷 Türkçe Sürüm](μεταφράσεις/README.tr.md)
```
  ████████╗███████╗██╗  ██╗ █████╗ ██████╗ ██╗  ██╗
  ╚══██╔══╝██╔════╝██║  ██║██╔══██╗██╔══██╗██║ ██╔╝
     ██║   ███████╗███████║███████║██████╔╝█████╔╝ 
     ██║   ╚════██║██╔══██║██╔══██║██╔══██╗██╔═██╗ 
     ██║   ███████║██║  ██║██║  ██║██║  ██║██║  ██╗
     ╚═╝   ╚══════╝╚═╝  ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝
         Terminal Packet Analysis & Dissection Engine
```
<p align="center">
  <img src="https://img.shields.io/badge/tshark-Packet_Analyzer-blue?style=for-the-badge" alt="tshark">
  <img src="https://img.shields.io/badge/CLI-Network_Sniffer-red?style=for-the-badge" alt="CLI Network Sniffer">
  <img src="https://img.shields.io/badge/Wireshark-Engine-green?style=for-the-badge" alt="Wireshark Engine">
  <img src="https://img.shields.io/badge/PCAP-Dissection-orange?style=for-the-badge" alt="PCAP Dissection">
</p>

<p align="center">
  <b>🌐 Ανατομή πακέτων δικτύου βασισμένη σε βαθύ τερματικό, ανάλυση ροής και εξαγωγή πεδίου με την υποστήριξη του Wireshark</b>
</p>

---

## 📋 Πίνακας περιεχομένων

- [Εισαγωγή](#-εισαγωγή)
- [Σύγκριση εργαλείων: tshark vs tcpdump εναντίον Wireshark](#-tool-comparison)
- [Εγκατάσταση και προνόμια](#-installation--privileges)
- [Βασική λήψη και αναπαραγωγή] (#-βασική-σύλληψη--αναπαραγωγή)
- [Filtering Traffic: Capture vs Display](#-filtering-traffic)
- [Εξαγωγή πεδίου (`-T fields`)](#-εξαγωγή πεδίου)
- [Practical Pentest & Forensic Recipes](#-practical-pentest--forensic-recipes)
  - [HTTP Credential & URI Hunting](#http-credential--uri-hunting)
  - [Εξαγωγή ερωτήματος DNS και ανίχνευση σήραγγας](#dns-query-extraction--tunneling-detection)
  - [TLS / HTTPS SNI Extraction](#tls--https-sni-extraction)
  - [Kerberos AS-REQ / AS-REP Extraction](#kerberos-as-req--as-rep-extraction)
  - [Παρακολούθηση ελέγχου ταυτότητας SMB & NTLM](#smb--ntlm-authentication-tracking)
  - [Έλεγχος πρωτοκόλλου καθαρού κειμένου (FTP, Telnet, SMTP)] (#cleartext-protocol-auditing)
  - [WPA / EAPOL 4-Way Handshake Hunting](#wpa--eapol-4-way-handshake-hunting)
- [Παρακολούθηση ροής & επανασυναρμολόγηση](#-ροή-following--reassembly)
- [Στατιστικά στοιχεία, συνομιλίες και τελικά σημεία](#-statistics-conversations--endpoints)
- [Αποκρυπτογράφηση επισκεψιμότητας TLS / SSL](#-tls--ssl-traffic-decryption)
- [Μορφές εξαγωγής (JSON, CSV, Elasticsearch)](#-εξαγωγή-μορφές)
- [Πίνακας γρήγορης αναφοράς] (#-ταχεία αναφορά-πίνακας)
- [Συμβουλές και βέλτιστες πρακτικές] (#-συμβουλές--βέλτιστες πρακτικές)
- [Νομική αποποίηση ευθυνών](#-νομική αποποίηση ευθυνών)

---

## 🎯 Εισαγωγή

Το **tshark** είναι το αντίστοιχο τερματικό και γραμμής εντολών του **Wireshark**. Βασίζεται στην πανομοιότυπη βιβλιοθήκη λήψης πακέτων (libpcap/Npcap) και στη μηχανή ανατομής πακέτων με το γραφικό Wireshark, καθιστώντας το ένα από τα πιο ισχυρά εργαλεία ανάλυσης δικτύου που είναι διαθέσιμα σε Linux, macOS και Windows.

Σε αντίθεση με τα βασικά εργαλεία ανίχνευσης πακέτων που επιθεωρούν μόνο τις κεφαλίδες του πρωτοκόλλου, το `tshark` διαθέτει δυνατότητες βαθιάς επιθεώρησης πακέτων σε χιλιάδες πρωτόκολλα, εγγενή σύνταξη φίλτρου εμφάνισης Wireshark (`-Y`), ισχυρή εξαγωγή πεδίου πρωτοκόλλου (`-T fields`) και ευέλικτη μορφοποίηση εξόδου για αυτοματοποιημένες σωληνώσεις δέσμης ενεργειών, sheZC.

### Βασικά χαρακτηριστικά

| Χαρακτηριστικό | Περιγραφή |
|---------|-------------|
| **Βαθιά Ανατομή** | Επιθεωρεί δεδομένα ωφέλιμου φορτίου σε χιλιάδες πρωτόκολλα εφαρμογών |
| **Φίλτρα Wireshark** | Πλήρης υποστήριξη για πλούσια σύνταξη φίλτρου οθόνης Wireshark (`-Y`) |
| **Εξαγωγή πεδίου** | Εξαγωγή μεμονωμένων πεδίων πρωτοκόλλου απευθείας (`-T fields -e ...`) |
| **Ακολουθεί ροή** | Επανασυναρμολογεί πλήρεις συνεδρίες TCP/UDP σε μορφές ASCII, Hex ή raw |
| **Αυτοματοποιημένη Εγκληματολογία**| Δυνατότητα σεναρίου σε κέλυφος, Python ή με σωλήνωση σε `jq` μέσω `-T json` |
| **Αποκρυπτογράφηση** | Υποστηρίζει αποκρυπτογράφηση TLS χρόνου εκτέλεσης με `SSLKEYLOGFILE` |

---

## ⚖️ Σύγκριση εργαλείων

| Χαρακτηριστικό | καρχαρίας | tcpdump | Wireshark |
|---------|--------|---------|-----------|
| **Διεπαφή** | CLI (Τερματικό) | CLI (Τερματικό) | GUI (γραφικό) |
| **Χρήση πόρων** | Χαμηλή / Μέτρια | Εξαιρετικά Minimal | Higher (RAM & CPU) |
| **Τηλεχειριστήριο (SSH / Ακέφαλος)** | Εγγενής ακέφαλος / CLI | Εγγενής σωλήνας SSH | Απαιτείται X11 / Remote agent |
| **Μηχανή ανατομής** | Πλήρεις ανατομείς Wireshark | Βασικές κεφαλίδες | Πλήρεις ανατομείς Wireshark |
| **Φίλτρα Λήψης** | Μόνο BPF (`-f`) | Μόνο BPF (`-i`) | Μόνο BPF |
| **Φίλτρα εμφάνισης** | ✅ Πλήρης σύνταξη Wireshark (`-Y`) | ❌ Κανένα | ✅ Πλήρης γραμμή φίλτρου GUI |
| **Μορφές εξαγωγής** | PCAP, JSON, CSV, Elastic, XML | PCAP, ASCII, Hex | PCAP, CSV, XML, JSON |
| **Σενάρια & Αυτοματισμός**| Shell, Python, jq | Shell / Awk / Sed | Lua plugins, GUI |

---

## 📥 Εγκατάσταση & Προνόμια

### Linux (Debian / Ubuntu / Kali Linux)
```bash
# Update and install tshark
sudo apt update && sudo apt install -y tshark

# Verify installation and version
tshark --version
```
### Επιτρέπεται η λήψη πακέτων χωρίς ρίζα

Για να αποτρέψετε την εκτέλεση του `tshark` ως `root` (που μπορεί να αποτελεί κίνδυνο ασφαλείας λόγω της τεράστιας επιφάνειας ανατομέα), προσθέστε τον χρήστη σας στην ομάδα `wireshark`:
```bash
# Configure wireshark-common for non-superusers
sudo dpkg-reconfigure wireshark-common
# (Select "Yes" when prompted)

# Add your user to the wireshark group
sudo usermod -aG wireshark $USER

# Apply group changes to current session
newgrp wireshark
```
### macOS (Homebrew) & Windows (Chocolatey)
```bash
# macOS
brew install wireshark

# Windows
choco install wireshark
```
---

## 🚀 Βασική Λήψη & Αναπαραγωγή

### Λίστα διαθέσιμων διεπαφών
```bash
# List interfaces with numeric IDs
tshark -D
```
### Λήψη ζωντανών πακέτων
```bash
# Capture packets on interface eth0
tshark -i eth0

# Capture only 100 packets and stop
tshark -i eth0 -c 100

# Capture and save traffic directly to a pcap file
tshark -i eth0 -w capture.pcap

# Capture with ring buffer (rotate files every 100MB, keep max 5 files)
tshark -i eth0 -b filesize:100000 -b files:5 -w rolling_traffic.pcap

# Capture with a duration trigger (capture for 60 seconds)
tshark -i eth0 -a duration:60 -w one_minute.pcap
```
### Ανάγνωση αρχείων PCAP
```bash
# Read and display packets from a pcap file
tshark -r capture.pcap

# Read PCAP without resolving IP addresses to DNS hostnames (speeds up processing)
tshark -r capture.pcap -n

# Suppress packet count summary upon completion
tshark -r capture.pcap -q
```
---

## 🔍 Φιλτράρισμα επισκεψιμότητας: Λήψη έναντι προβολής

Το `tshark` χρησιμοποιεί δύο ξεχωριστούς κινητήρες φιλτραρίσματος:

1. **Φίλτρα σύλληψης (`-f`):** Φίλτρα πακέτων Berkeley (BPF) που αξιολογούνται από τον πυρήνα πριν τα πακέτα φτάσουν στον χώρο χρήστη. Υψηλή απόδοση, αλλά περιορίζεται σε βασικά πρωτόγονα IP/port.
2. **Φίλτρα οθόνης (`-Y`):** Φίλτρα βαθέων πρωτοκόλλων που αξιολογούνται από τη μηχανή ανατομής του Wireshark. Υποστηρίζει επίπεδα εφαρμογών, αναζήτηση συμβολοσειρών, regex και σημαίες πρωτοκόλλου.
```bash
# Capture filter: Capture only TCP traffic to port 80 or 443
tshark -i eth0 -f "tcp port 80 or tcp port 443" -w web_traffic.pcap

# Display filter: Show only successful HTTP GET responses (Status 200)
tshark -r capture.pcap -Y "http.request.method == \"GET\" and http.response.code == 200"

# Display filter: Filter by specific subnet and exclude broadcast
tshark -r capture.pcap -Y "ip.addr == 192.168.1.0/24 and not eth.dst == ff:ff:ff:ff:ff:ff"

# Read-filter (-R): Single-pass filter used during capture
tshark -r capture.pcap -2 -R "dns.flags.response == 1"
```
---

## 📊 Εξαγωγή πεδίου (`-T fields`)

Η λειτουργία `-T fields` είναι η πιο ισχυρή δυνατότητα του `tshark` για αυτοματοποιημένες προκλήσεις αναγνώρισης, δημιουργίας σεναρίων και CTF.
```bash
# Basic syntax for field extraction
tshark -r capture.pcap -T fields -e <field1> -e <field2> -E header=y -E separator=,
```
### Σημαίες μορφοποίησης εξαγωγής κλειδιού

| Σημαία | Περιγραφή |
|------|-------------|
| `-T fields` | Αλλάζει τη λειτουργία εξόδου σε προσαρμοσμένη εξαγωγή πεδίου |
| `-e <field>` | Ονομάζει το πεδίο πρωτοκόλλου προς εξαγωγή (μπορεί να επαναληφθεί) |
| `-E header=y` | Εκτυπώνει τα ονόματα στηλών ως γραμμή κεφαλίδας |
| `-E separator=,` | Σετ διαχωριστή πεδίων (`,` για CSV, `\t` για TSV) |
| `-E occurrence=f` | Εξάγει μόνο την πρώτη (`f`), την τελευταία (`l`) ή όλες τις (`a`) εμφανίσεις |
| `-E quote=d` | Περικλείει τιμές σε διπλά εισαγωγικά |

### Cheatsheet Πεδίων Πυρήνα Πρωτοκόλλου
```bash
# IP layer fields
ip.src, ip.dst, ip.proto, ip.ttl

# TCP layer fields
tcp.srcport, tcp.dstport, tcp.flags, tcp.stream, tcp.payload

# HTTP fields
http.host, http.request.method, http.request.uri, http.response.code, http.user_agent, http.cookie

# DNS fields
dns.qry.name, dns.qry.type, dns.a, dns.flags.response

# TLS fields
tls.handshake.extensions_server_name, tls.handshake.ciphersuite
```
---

## 🎯 Πρακτικές Πεντέτες & Ιατροδικαστικές Συνταγές

### Διαπιστευτήρια HTTP & Κυνήγι URI
```bash
# Extract all visited URLs and HTTP Hosts
tshark -r capture.pcap -Y "http.request" -T fields -e ip.src -e http.host -e http.request.method -e http.request.uri -E header=y

# Extract User-Agent strings across internal clients
tshark -r capture.pcap -Y "http.user_agent" -T fields -e ip.src -e http.user_agent | sort -u

# Hunt for HTTP POST data (logins, passwords, tokens)
tshark -r capture.pcap -Y "http.request.method == \"POST\"" -T fields -e ip.src -e ip.dst -e http.host -e http.file_data

# Hunt for HTTP Basic Authentication headers (Base64 encoded)
tshark -r capture.pcap -Y "http.authorization" -T fields -e ip.src -e http.authorization
```
### Εξαγωγή ερωτήματος DNS & Ανίχνευση σήραγγας
```bash
# List all requested domain names (DNS queries)
tshark -r capture.pcap -Y "dns.flags.response == 0" -T fields -e ip.src -e dns.qry.name | sort -u

# Detect potential DNS tunneling (display long subdomains > 30 characters)
tshark -r capture.pcap -Y "dns.flags.response == 0" -T fields -e dns.qry.name | awk 'length($1) > 30' | sort -u

# Correlate DNS queries with their resolved IP addresses
tshark -r capture.pcap -Y "dns.flags.response == 1 and dns.a" -T fields -e dns.qry.name -e dns.a -E header=y
```
### Εξαγωγή TLS / HTTPS SNI

Ακόμη και όταν τα ωφέλιμα φορτία HTTPS είναι κρυπτογραφημένα, η **Ένδειξη ονόματος διακομιστή (SNI)** στην αρχική χειραψία TLS Client Hello αποκαλύπτει ονόματα τομέα προορισμού:
```bash
# Extract TLS Server Name Indication (SNI) hostnames
tshark -r capture.pcap -Y "tls.handshake.type == 1" -T fields -e ip.src -e ip.dst -e tls.handshake.extensions_server_name | sort -u
```
### Kerberos AS-REQ / AS-REP Extraction
```bash
# Extract Kerberos domain client names (hunting for user accounts)
tshark -r capture.pcap -Y "kerberos.CNameString" -T fields -e ip.src -e kerberos.realm -e kerberos.CNameString | sort -u

# Hunt for Kerberos errors (e.g. KRB5KDC_ERR_PREAUTH_REQUIRED, bad passwords)
tshark -r capture.pcap -Y "kerberos.error_code" -T fields -e ip.src -e ip.dst -e kerberos.error_code
```
### Παρακολούθηση ελέγχου ταυτότητας SMB & NTLM
```bash
# Extract SMB accessed file paths and shares
tshark -r capture.pcap -Y "smb2.filename" -T fields -e ip.src -e ip.dst -e smb2.filename | sort -u

# Extract NTLMSSP usernames and domain names during authentication
tshark -r capture.pcap -Y "ntlmssp.auth.username" -T fields -e ip.src -e ntlmssp.auth.domain -e ntlmssp.auth.username
```
### Έλεγχος πρωτοκόλλου καθαρού κειμένου
```bash
# Extract FTP usernames and passwords
tshark -r capture.pcap -Y "ftp.request.command == \"USER\" or ftp.request.command == \"PASS\"" -T fields -e ip.src -e ftp.request.command -e ftp.request.arg

# Extract Telnet session input
tshark -r capture.pcap -Y "telnet" -T fields -e telnet.data
```
### WPA / EAPOL 4-Way Handshake Hunting
```bash
# Filter for 802.11 EAPOL handshakes to crack with Hashcat/Aircrack-ng
tshark -r wifi.pcap -Y "eapol" -w handshakes_only.pcap
```
---

## 🧵 Παρακολούθηση ροής & επανασυναρμολόγηση

Το `tshark` επιτρέπει τη συναρμολόγηση ολόκληρων ροών TCP και UDP χωρίς να χρειάζεται το γραφικό περιβάλλον χρήστη του Wireshark.
```bash
# List all TCP conversations to find stream IDs
tshark -r capture.pcap -q -z conv,tcp

# Follow TCP stream 0 in ASCII format
tshark -r capture.pcap -q -z follow,tcp,ascii,0

# Follow TCP stream 2 in Hexdump format
tshark -r capture.pcap -q -z follow,tcp,hex,2

# Follow HTTP stream (stream index 1)
tshark -r capture.pcap -q -z follow,http,ascii,1
```
---

## 📈 Στατιστικά στοιχεία, συνομιλίες και τελικά σημεία
```bash
# Protocol hierarchy statistics (breakdown of all protocols in PCAP)
tshark -r capture.pcap -q -z io,phs

# IP conversations table (who talked to whom, packet count, bytes)
tshark -r capture.pcap -q -z conv,ip

# Port conversation statistics (TCP ports)
tshark -r capture.pcap -q -z conv,tcp

# List all unique IP endpoints in PCAP
tshark -r capture.pcap -q -z endpoints,ip

# HTTP request statistics tree
tshark -r capture.pcap -q -z http,tree

# Wireshark Expert Info warnings and errors
tshark -r capture.pcap -q -z expert
```
---

## 🔐 Αποκρυπτογράφηση επισκεψιμότητας TLS / SSL

Εάν διαθέτετε μυστικά κλειδιά pre-master (εξάγονται μέσω του προγράμματος περιήγησης `SSLKEYLOGFILE` ή ανακτώνται κατά τη διάρκεια του sandboxing για κακόβουλο λογισμικό):
```bash
# Decrypt TLS traffic on the fly using keylog file
tshark -r https_traffic.pcap -o "tls.keylog_file:/path/to/sslkeys.log" -Y "http" -T fields -e http.host -e http.request.uri

# Follow decrypted HTTP stream inside TLS
tshark -r https_traffic.pcap -o "tls.keylog_file:/path/to/sslkeys.log" -q -z follow,http,ascii,0
```
---

## 📦 Μορφές εξαγωγής
```bash
# Output as structured JSON (ideal for piping into jq or Python)
tshark -r capture.pcap -c 5 -T json

# Extract specific JSON field with jq
tshark -r capture.pcap -T json | jq '.[]._source.layers.ip["ip.src"]'

# Output in Elasticsearch Bulk Format (-T ek)
tshark -r capture.pcap -T ek > packets_for_elastic.json

# Output as CSV table
tshark -r capture.pcap -T fields -e frame.number -e ip.src -e ip.dst -e _ws.col.Protocol -E header=y -E separator=, > traffic.csv
```
---

## ⚡ Γρήγορος Πίνακας Αναφοράς

| Εργασία | Εντολή |
|------|---------|
| **Καταλάβετε ζωντανή κίνηση** | `tshark -i eth0 -w output.pcap` |
| **Λήψη με φίλτρο BPF** | `tshark -i eth0 -f "port 80" -w http.pcap` |
| **Διαβάστε PCAP χωρίς DNS** | `tshark -r capture.pcap -n` |
| **Εφαρμογή φίλτρου οθόνης** | `tshark -r capture.pcap -Y "http.response.code >= 400"` |
| **Εξαγωγή IP και Ports** | `tshark -r capture.pcap -T fields -e ip.src -e tcp.srcport -e ip.dst -e tcp.dstport` |
| **Εξαγωγή ερωτημάτων DNS** | `tshark -r capture.pcap -Y "dns.flags.response == 0" -T fields -e dns.qry.name` |
| **Εξαγωγή ονομάτων κεντρικού υπολογιστή TLS SNI**| `tshark -r capture.pcap -Y "tls.handshake.type == 1" -T fields -e tls.handshake.extensions_server_name` |
| **Εξαγωγή διευθύνσεων URL HTTP** | `tshark -r capture.pcap -Y "http.request" -T fields -e http.host -e http.request.uri` |
| **Ακολουθήστε τη ροή TCP** | `tshark -r capture.pcap -q -z follow,tcp,ascii,0` |
| **Ιεραρχία Πρωτοκόλλου** | `tshark -r capture.pcap -q -z io,phs` |
| **Αποκρυπτογράφηση με Keylog** | `tshark -r traffic.pcap -o "tls.keylog_file:keys.log"` |
| **Εξαγωγή σε JSON** | `tshark -r capture.pcap -T json > packets.json` |

---

## 💡 Συμβουλές και βέλτιστες πρακτικές

1. **Να χρησιμοποιείτε πάντα το `-n` για μεγάλα PCAP:** Η ανάλυση ονόματος DNS επιβραδύνει δραματικά την ανάλυση. Προσθέστε `-n` για να αποτρέψετε την επίλυση διευθύνσεων IP.
2. **Συνδυάστε `-f` και `-Y`:** Χρησιμοποιήστε φίλτρα λήψης `-f` κατά τη διάρκεια της ζωντανής λήψης για να μειώσετε τις πτώσεις πακέτων και φίλτρα οθόνης `-Y` κατά τη διάρκεια της μετά την ανάλυση για επιθεώρηση σε βάθος πρωτοκόλλου.
3. **Σωλήνας με `jq`:** Η διοχέτευση του `tshark -T json` στο `jq` επιτρέπει την υποβολή ερωτημάτων σε ένθετα πεδία πρωτοκόλλου με πλήρη λογική και μετασχηματισμό boolean.
4. **Αποστολές Ring για 24/7 Sniffing:** Όταν εκτελείτε συνεχείς λήψεις δικτύου σε κεντρικό κεντρικό υπολογιστή, επιβάλλετε πάντα όρια μεγέθους και αρχείων (`-b filesize:50000 -b files:10`) για να αποφύγετε τη συντριβή του δίσκου του στόχου.

---

## ⚠️ Νομική αποποίηση ευθύνης

> **ΣΗΜΑΝΤΙΚΟ:** Η μη εξουσιοδοτημένη λήψη πακέτων, η υποκλοπή της κυκλοφορίας του δικτύου και η υποκλοπή διαπιστευτηρίων σε δίκτυα χωρίς ρητή, τεκμηριωμένη άδεια είναι παράνομη και υπόκειται σε ποινική δίωξη. Χρησιμοποιήστε το `tshark` αυστηρά σε συστήματα και δίκτυα που κατέχετε ή έχετε εξουσιοδοτηθεί να αξιολογήσετε βάσει σύμβασης.