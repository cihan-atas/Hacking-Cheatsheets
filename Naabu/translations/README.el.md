<!-- language-home-navigation -->
[🏠 Ελληνική Αρχική Σελίδα](../../README.el.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ⚡ Naabu - Πλήρες φύλλο εξαπάτησης

[🇹🇷 Türkçe Sürüm](μεταφράσεις/README.tr.md)
```
  ███╗   ██╗ █████╗  █████╗ ██████╗ ██╗   ██╗
  ████╗  ██║██╔══██╗██╔══██╗██╔══██╗██║   ██║
  ██╔██╗ ██║███████║███████║██████╔╝██║   ██║
  ██║╚██╗██║██╔══██║██╔══██║██╔══██╗██║   ██║
  ██║ ╚████║██║  ██║██║  ██║██████╔╝╚██████╔╝
  ╚═╝  ╚═══╝╚═╝  ╚═╝╚═╝  ╚═╝╚═════╝  ╚═════╝ 
        Fast & Reliable SYN/CONNECT Port Scanner
```
<p align="center">
  <img src="https://img.shields.io/badge/Naabu-Port_Scanner-blue?style=for-the-badge" alt="Naabu">
  <img src="https://img.shields.io/badge/Author-ProjectDiscovery-orange?style=for-the-badge" alt="ProjectDiscovery">
  <img src="https://img.shields.io/badge/Category-Recon_&_Scanning-green?style=for-the-badge" alt="Recon & Scanning">
  <img src="https://img.shields.io/badge/Language-Go-cyan?style=for-the-badge" alt="Go">
</p>

<p align="center">
  <b>⚡ Γρήγορος και αξιόπιστος σαρωτής θυρών με έμφαση στην απλότητα και την απρόσκοπτη σωληνώσεις με τα εργαλεία ProjectDiscovery (httpx, πυρήνες)</b>
</p>

---

## 📋 Πίνακας περιεχομένων

- [Επισκόπηση και βασικά χαρακτηριστικά] (#-επισκόπηση--κλειδί-χαρακτηριστικά)
- [Εγκατάσταση] (#-εγκατάσταση)
- [Βασική σάρωση θυρών] (#-basic-port-scanning)
  - [Single Host Scan] (#single-host-scan)
  - [Top Ports & Full Port Range](#top-ports--full-port-range)
- [Λειτουργίες σάρωσης: SYN (Raw Socket) vs CONNECT](#-scan-modes-syn-raw-socket-vs-connect)
- [Rate Limiting & Performance Tuning](#-rate-limiting--performance-tuning)
- [Σάρωση πολλαπλών στόχων και μπλοκ CIDR](#-σάρωση πολλαπλών στόχων--cidr-blocks)
- [Ροές εργασιών χωρίς συγκόλληση αγωγών](#-ροές εργασίας χωρίς ραφές)
  - [Naabu + Httpx + Nuclei](#naabu--httpx--nuclei)
  - [Subfinder + Naabu](#subfinder--naabu)
- [Ενσωμάτωση Nmap (`-nmap`)](#-nmap-integration--nmap)
- [Πίνακας γρήγορης αναφοράς] (#-ταχεία αναφορά-πίνακας)
- [Defensive Considerations & Detection](#-defensive-considerations--detection)
- [Νομική αποποίηση ευθυνών](#-νομική αποποίηση ευθυνών)

---

## 🎯 Επισκόπηση & Βασικά Χαρακτηριστικά

Αναπτύχθηκε από το **ProjectDiscovery**, το **Naabu** είναι ένας σαρωτής θυρών που έχει σχεδιαστεί για να είναι γρήγορος, ακριβής και βελτιστοποιημένος για ενσωμάτωση σε σύγχρονους αγωγούς αυτοματισμού αναγνώρισης:
- **Σαρώσεις SYN & CONNECT:** Αυτόματη εναλλακτική στο TCP CONNECT όταν δεν υπάρχουν προνόμια raw socket (`libpcap` / root).
- **Οικοσύστημα ProjectDiscovery:** Εγγενής αρχιτεκτονική αγωγών που επιτρέπει την απρόσκοπτη διοχέτευση από το `subfinder` στο `naabu`, μετά στο `httpx` και το `nuclei`.
- **Ενσωματωμένη μηχανή Nmap:** Δυνατότητα ενεργοποίησης ενεργής υπηρεσίας Nmap και σάρωσης έκδοσης σε ανοιχτές θύρες που ανακαλύφθηκαν αυτόματα.
- **Εξαίρεση CDN:** Παρακάμπτει αυτόματα τους κόμβους άκρης CDN του cloud (Cloudflare, Akamai, CloudFront) για να αποτρέψει τη σπατάλη εύρους ζώνης και τα ψευδώς θετικά αποτελέσματα.

---

## 📥 Εγκατάσταση
```bash
# Using Go (Recommended)
go install -v github.com/projectdiscovery/naabu/v2/cmd/naabu@latest

# Binary download via pdtm (ProjectDiscovery Tool Manager)
pdtm -i naabu

# Arch Linux (AUR)
yay -S naabu-bin

# Kali Linux
sudo apt update && sudo apt install -y naabu
```
> [!ΣΗΜΕΙΩΣΗ]
> Στο Linux, η παραχώρηση `CAP_NET_RAW` επιτρέπει την εκτέλεση σαρώσεων SYN χωρίς root:
> `sudo setcap cap_net_raw=+ep $(which naabu)`

---

## 🚀 Βασική σάρωση θύρας

### Σάρωση μεμονωμένου κεντρικού υπολογιστή
```bash
# Scan default top 100 ports
naabu -host example.com

# Scan specific ports
naabu -host example.com -p 80,443,8080,8443
```
### Κορυφαία λιμάνια & Πλήρης εμβέλεια λιμένων
```bash
# Scan top 1000 ports
naabu -host example.com -top-ports 1000

# Full port scan (1-65535)
naabu -host example.com -p -
```
---

## ⚡ Λειτουργίες σάρωσης: SYN (Raw Socket) vs CONNECT

- **Σάρωση SYN (Απαιτείται `-s syn` / root):** Στέλνει ακατέργαστα πακέτα TCP SYN χωρίς να τελειώσει η 3-way χειραψία (μισάνοιχτη σάρωση). Πολύ πιο γρήγορα και λιγότερο θορυβώδη στα αρχεία καταγραφής εφαρμογών.
- **CONNECT Scan (`-s conn`):** Χρησιμοποιεί την τυπική κλήση υποδοχής OS `connect()`. Λειτουργεί χωρίς δικαιώματα root και διεισδύει σε περιοριστικά εικονικά περιβάλλοντα.
```bash
# Explicit SYN Scan (Requires sudo or CAP_NET_RAW)
sudo naabu -host example.com -s syn

# Explicit TCP Connect Scan (No root required)
naabu -host example.com -s conn
```
---

## ⚙️ Περιορισμός τιμών και συντονισμός απόδοσης
```bash
# Adjust packet rate per second (default: 1000)
sudo naabu -host example.com -rate 2500

# Set connection timeout in milliseconds
naabu -host example.com -timeout 1000

# Exclude CDN edge servers (Cloudflare, CloudFront, Fastly)
naabu -host example.com -exclude-cdn
```
---

## 🌐 Σάρωση πολλαπλών στόχων και μπλοκ CIDR
```bash
# Scan targets from list
naabu -list targets.txt -p 80,443,8080 -o open_ports.txt

# Scan a /24 subnet block
sudo naabu -host 192.168.1.0/24 -p 22,80,443,445,3389
```
---

## 🔄 Απρόσκοπτες ροές εργασίας αγωγών

Το Naabu λάμπει όταν συνοδεύεται από άλλα εργαλεία ProjectDiscovery:

### Subfinder + Naabu
Απαριθμήστε υποτομείς και ανακαλύψτε αμέσως ποιοι έχουν ανοιχτές θύρες web/SSH:
```bash
subfinder -d example.com -silent | naabu -silent -p 80,443,8080,8443
```
### Naabu + Httpx + Πυρήνες
Ο κανονικός αγωγός αναγνώρισης επιθετικού bug bounty:
```bash
# 1. Port scan subdomains -> 2. Probe HTTP services -> 3. Vulnerability scan
subfinder -d target.com -silent \
  | naabu -silent -p 80,443,8080,8443 \
  | httpx -silent \
  | nuclei -severity medium,high,critical -o findings.txt
```
---

## 🔍 Ενσωμάτωση Nmap (`-nmap`)

Περάστε τις ανοιχτές θύρες απευθείας στο Nmap για ανίχνευση υπηρεσίας (`-sV`) και σάρωση σεναρίων (`-sC`):
```bash
sudo naabu -host target.local -top-ports 1000 -nmap-cli 'nmap -sV -sC -oN naabu_nmap.txt'
```
---

## ⚡ Γρήγορος Πίνακας Αναφοράς

| Στόχος | Εντολή |
|-----------|---------|
| **Προεπιλεγμένη σάρωση (Κορυφαία 100)** | `naabu -host <TARGET>` |
| **Σάρωση πλήρους εύρους (1-65535)**| `naabu -host <TARGET> -p -` |
| **Κορυφαίες 1000 θύρες** | `naabu -host <TARGET> -top-ports 1000` |
| **Σάρωση SYN (Γρήγορη, κρυφή)**| `sudo naabu -host <TARGET> -s syn` |
| **Εξαίρεση CDN Edge IP** | `naabu -host <TARGET> -exclude-cdn` |
| **Έξοδος σε JSON** | `naabu -host <TARGET> -json -o results.json` |
| **Σωλήνωση στο HTTPX** | `naabu -host <TARGET> -silent \| httpx` |
| **Ενεργοποίηση σάρωσης υπηρεσίας Nmap**| `sudo naabu -host <TARGET> -nmap-cli 'nmap -sV'` |

---

## 🛡️ Αμυντικές Θεωρήσεις & Ανίχνευση

1. **Κανόνες Τείχους προστασίας και IDS:** Οι σαρώσεις SYN υψηλής ταχύτητας που δημιουργούνται από το Naabu παράγουν υψηλές αναλογίες TCP RST/SYN που συλλαμβάνονται εύκολα από τους κανόνες Snort/Suricata.
2. **Περιορισμός ρυθμού δικτύου:** Επιβολή περιορισμού ρυθμού εισερχόμενης σύνδεσης (`iptables -m limit --limit 50/s`) για επιβράδυνση και διακοπή της αυτοματοποιημένης σάρωσης.

---

## ⚠️ Νομική αποποίηση ευθύνης

> [!ΠΡΟΣΟΧΗ]
> Το Naabu έχει αναπτυχθεί αυστηρά για εξουσιοδοτημένες αξιολογήσεις ασφαλείας, δοκιμές διείσδυσης και ανακάλυψη αμυντικών στοιχείων. Η σάρωση δικτύων χωρίς ρητή άδεια απαγορεύεται αυστηρά.