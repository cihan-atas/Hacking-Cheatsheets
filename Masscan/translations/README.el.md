<!-- language-home-navigation -->
[🏠 Ελληνική Αρχική Σελίδα](../../README.el.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ⚡ Masscan - Ολοκληρωμένο Cheatsheet

[🇹🇷 Türkçe Sürüm](μεταφράσεις/README.tr.md)
```
  ███╗   ███╗ █████╗ ███████╗███████╗ ██████╗ █████╗ ███╗   ██╗
  ████╗ ████║██╔══██╗██╔════╝██╔════╝██╔════╝██╔══██╗████╗  ██║
  ██╔████╔██║███████║███████╗███████╗██║     ███████║██╔██╗ ██║
  ██║╚██╔╝██║██╔══██║╚════██║╚════██║██║     ██╔══██║██║╚██╗██║
  ██║ ╚═╝ ██║██║  ██║███████║███████║╚██████╗██║  ██║██║ ╚████║
  ╚═╝     ╚═╝╚═╝  ╚═╝╚══════╝╚══════╝ ╚═════╝╚═╝  ╚═╝╚═╝  ╚═══╝
       The Fastest Internet Port Scanner - Asynchronous 10M Packets/Second
```
<p align="center">
  <img src="https://img.shields.io/badge/Masscan-Internet_Port_Scanner-blue?style=for-the-badge" alt="Masscan">
  <img src="https://img.shields.io/badge/Language-C-orange?style=for-the-badge" alt="Γλώσσα Γ">
  <img src="https://img.shields.io/badge/Category-Recon_&_Scanning-green?style=for-the-badge" alt="Recon & Scanning">
  <img src="https://img.shields.io/badge/License-AGPLv3-red?style=for-the-badge" alt="License AGPLv3">
</p>

<p align="center">
  <b>🌐 Ασύγχρονος σαρωτής θύρας TCP υψηλής απόδοσης που μπορεί να σαρώσει ολόκληρο το IPv4 Internet σε λιγότερο από 5 λεπτά</b>
</p>

---

## 📋 Πίνακας περιεχομένων

- [Επισκόπηση & Η Ασύγχρονη Αρχιτεκτονική](#-επισκόπηση--η-ασύγχρονη-αρχιτεκτονική)
- [Σύγκριση εργαλείων: Masscan vs Nmap vs RustScan](#-tool-comparison-masscan-vs-nmap-vs-rustscan)
- [Εγκατάσταση] (#-εγκατάσταση)
- [Βασικοί στόχοι χρήσης και σάρωσης](#-basic-usage--scanning-targets)
- [Έλεγχος ταχύτητας μετάδοσης (`--rate`)] (#-ρυθμός μετάδοσης-control---rate)
- [Προδιαγραφές θύρας (TCP & UDP)](#-port-specifications-tcp--udp)
- [Λήψη banner & ανίχνευση βασικού πρωτοκόλλου](#-banner-grabbing--basic-protocol-probing)
- [Target Exclusions & Blacklists (`--excludefile`)](#-target-exclusions--blacklists---excludefile)
- [Μορφές εξόδου και σωληνώσεις σε Nmap](#-μορφές εξόδου--σωλήνωση σε nmap)
- [Πίνακας γρήγορης αναφοράς] (#-ταχεία αναφορά-πίνακας)
- [Defensive Considerations & Hardware Limits](#-defensive-considerations--hardware-limits)
- [Νομική αποποίηση ευθυνών](#-νομική αποποίηση ευθυνών)

---

## 🎯 Επισκόπηση & Η Ασύγχρονη Αρχιτεκτονική

Δημιουργήθηκε από τον **Robert Graham**, το **Masscan** χαιρετίζεται ως ο ταχύτερος σαρωτής θυρών που υπάρχει. Παράγει αποτελέσματα παρόμοια με το Nmap, αλλά λειτουργεί εσωτερικά όπως τα `scanrand`, `unicornscan` και `ZMap`, χρησιμοποιώντας **ασύγχρονη μετάδοση**.

Σε αντίθεση με το Nmap, το οποίο χρησιμοποιεί τη στοίβα TCP του πυρήνα του λειτουργικού συστήματος και περιμένει για απαντήσεις πριν εξετάσει τις επόμενες θύρες, το Masscan ενσωματώνει τη δική του προσαρμοσμένη στοίβα TCP/IP χώρου χρήστη. Εκτοξεύει ακατέργαστα πακέτα SYN με ρυθμούς που υπερβαίνουν τα **10 εκατομμύρια πακέτα ανά δευτερόλεπτο** χωρίς να διατηρεί πίνακες καταστάσεων σύνδεσης.

---

## ⚖️ Σύγκριση εργαλείων: Masscan vs Nmap vs RustScan

| Χαρακτηριστικό | Masscan | Nmap | RustScan |
|---------|---------|------|----------|
| **Μέγιστος ρυθμός πακέτων** | 10.000.000 pkts/sec | ~1.000 pkts/sec | Όριο υποδοχής λειτουργικού συστήματος |
| **Ολόκληρη Σάρωση Διαδικτύου (/0)**| ✅ Κάτω από 5 λεπτά | ❌ Ημέρες / Εβδομάδες | ❌ Δεν έχει σχεδιαστεί για /0 |
| **Υλοποίηση στοίβας TCP**| Προσαρμοσμένος χώρος χρήστη Raw SYN| Πυρήνας OS / Raw Sockets| Async Tokio TCP Sockets |
| **Deep Version & NSE Scripts**| ❌ Πρωτόγονα Banners | ✅ 600+ σενάρια NSE | ✅ Μέσω μεταβίβασης Nmap |
| **Παρακολούθηση κατάστασης** | Απάτριδες (Blackrock PRNG) | Κρατική πισίνα σύνδεσης| Stateful async εργασίες |
| **Ιδανικό εύρος στόχου** | Μπλοκ κατηγορίας Α/Β (/8, /16)| Μεμονωμένοι στόχοι & /24 υποδίκτυα | Μεμονωμένα μηχανήματα & /24 υποδίκτυα |

---

## 📥 Εγκατάσταση
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y masscan

# Arch Linux
sudo pacman -S masscan

# Build from source (Latest GitHub version)
sudo apt install -y git gcc make libpcap-dev
git clone https://github.com/robertdavidgraham/masscan
cd masscan && make -j$(nproc)
sudo make install
```
---

## 🚀 Βασικοί στόχοι χρήσης και σάρωσης
```bash
# Scan a single IP for top web ports
sudo masscan 10.10.10.50 -p80,443,8080 --rate 1000

# Scan an entire Class B internal subnet (172.16.0.0/16) for SMB port 445
sudo masscan 172.16.0.0/16 -p445 --rate 5000

# Scan all 65,535 ports on a specific IP
sudo masscan 10.10.10.50 -p1-65535 --rate 2000
```
> [!ΣΗΜΑΝΤΙΚΟ]
> Επειδή το Masscan δημιουργεί ακατέργαστα πακέτα μέσω του `libpcap`, **πρέπει να εκτελείται με δικαιώματα root/sudo**.

---

## ⚡ Έλεγχος ταχύτητας μετάδοσης (`--rate`)

Η παράμετρος `--rate` υπαγορεύει πόσα πακέτα ανά δευτερόλεπτο εκπέμπει το Masscan. Η βαθμονόμηση αυτού είναι κρίσιμη για να αποφευχθεί ο κορεσμός του εξοπλισμού δικτύου:
```bash
# Gentle rate for home routers / WiFi (100–500 pkts/sec)
sudo masscan 10.10.10.0/24 -p1-1000 --rate 300

# Standard corporate gigabit internal network rate (5,000–10,000 pkts/sec)
sudo masscan 10.0.0.0/8 -p80,443 --rate 10000

# High-speed data center 10Gbps uplink rate (100,000+ pkts/sec)
sudo masscan 0.0.0.0/0 -p443 --rate 100000
```
---

## 🔌 Προδιαγραφές θύρας (TCP & UDP)

Το Masscan υποστηρίζει ταυτόχρονα λίστες θυρών TCP και UDP:
```bash
# Specific TCP ports
sudo masscan 10.10.10.50 -p21,22,80,443,445,3389

# Top ports shortcut (similar to Nmap)
sudo masscan 10.10.10.50 --top-ports 100

# UDP ports (prefixed with U:)
sudo masscan 10.10.10.50 -p U:53,U:161,U:123 --rate 500
```
---

## 🔍 Αρπαγή banner & ανίχνευση βασικού πρωτοκόλλου

Ενώ το Masscan είναι κυρίως ένας σαρωτής SYN, μπορεί να ολοκληρώσει χειραψίες τριών κατευθύνσεων για να αρπάξει τα banner εφαρμογών:
```bash
# Grab HTTP, FTP, and SSH banners
sudo masscan 10.10.10.0/24 -p80,21,22 --banners --rate 1000
```
---

## 🚫 Εξαιρέσεις στόχων και μαύρες λίστες (`--excludefile`)

Κατά τη διεξαγωγή δοκιμών διείσδυσης ευρείας εμβέλειας, να αποκλείετε πάντα κρίσιμες υποδομές και διευθύνσεις εκτός ορίου:
```bash
# Exclude government and military ranges or local gateway
sudo masscan 10.0.0.0/8 -p80 --rate 2000 --exclude 10.0.0.1,10.1.1.1

# Use an exclusion file (e.g., standard exclude.conf)
sudo masscan 192.168.0.0/16 -p445 --rate 2000 --excludefile /etc/masscan/exclude.conf
```
---

## 📊 Μορφές εξόδου και διοχέτευση σε Nmap

Αποθηκεύστε τους κεντρικούς υπολογιστές που εντοπίστηκαν σε μορφές greppable ή JSON και, στη συνέχεια, τροφοδοτήστε τους στο Nmap για πλήρη αξιολόγηση ευπάθειας:
```bash
# Save to JSON format
sudo masscan 10.10.10.0/24 -p80,443 -oJ masscan_results.json

# Save to simple List format (-oL)
sudo masscan 10.10.10.0/24 -p1-65535 --rate 2000 -oL open_ports.txt

# Extract discovered ports and feed directly to Nmap
cat open_ports.txt | awk '{print $4}' | sort -u | paste -sd, - | xargs -I{} nmap -sC -sV -p{} 10.10.10.50
```
---

## ⚡ Γρήγορος Πίνακας Αναφοράς

| Στόχος | Εντολή |
|-----------|---------|
| **Σάρωση ενός κεντρικού υπολογιστή (Όλες οι θύρες)** | `sudo masscan <IP> -p1-65535 --rate 2000` |
| **Σάρωση υποδικτύου CIDR για Θύρα** | `sudo masscan <SUBNET>/16 -p<PORT> --rate 5000` |
| **Σάρωση με λήψη banner** | `sudo masscan <IP> -p80,22 --banners --rate 1000` |
| **Σάρωση θυρών UDP** | `sudo masscan <IP> -p U:53,U:161 --rate 500` |
| **Εξαιρούνται οι διευθύνσεις IP** | `sudo masscan <NET> -p80 --exclude <IP1>,<IP2>` |
| **Έξοδος σε JSON** | `sudo masscan <IP> -p80,443 -oJ output.json` |
| **Έξοδος σε απλή λίστα** | `sudo masscan <IP> -p80,443 -oL output.txt` |

---

## 🛡️ Αμυντικές Θεωρήσεις & Όρια Υλικού

1. **Crash δρομολογητή καταναλωτή:** Η ρύθμιση του `--rate 100000` σε έναν οικιακό δρομολογητή DSL/καλωδιακό ή τυπικό Wi-Fi θα κατακλύσει τον πίνακα καταστάσεων παρακολούθησης της σύνδεσης NAT και θα διακόψει τη λειτουργία του δρομολογητή. Διατηρήστε το `--rate` κάτω από το `1000` σε οικιακές συνδέσεις.
2. **Κατανάλωση εύρους ζώνης:** Η μετάδοση με 10 εκατομμύρια πακέτα ανά δευτερόλεπτο καταναλώνει περίπου 5,4 Gbps εύρους ζώνης δικτύου.
3. **Τοπική απόρριψη τείχους προστασίας (RST):** Επειδή το Masscan χρησιμοποιεί μια προσαρμοσμένη στοίβα TCP χώρου χρήστη, ο πυρήνας Linux σας δεν γνωρίζει για ανοιχτές συνδέσεις και μπορεί να στέλνει αυτόματα πακέτα TCP RST σε στόχους. Για να το αποφύγετε αυτό κατά τη λήψη banner:
   ```bash
   sudo iptables -A INPUT -p tcp --dport 60000 -j DROP
   sudo masscan ... --source-port 60000
   ```
---

## ⚠️ Νομική αποποίηση ευθύνης

> [!ΠΡΟΣΟΧΗ]
> Το Masscan είναι ένα εξαιρετικά ισχυρό βοηθητικό πρόγραμμα αναγνώρισης δικτύου. Η σάρωση μη εξουσιοδοτημένων δικτύων, κυβερνητικών περιοχών IP ή δημόσιων υποδικτύων Διαδικτύου μπορεί να παραβιάζει τους νόμους για τις τηλεπικοινωνίες και την ασφάλεια στον κυβερνοχώρο. Επαληθεύετε πάντα τη γραπτή εξουσιοδότηση πριν ξεκινήσετε τις σαρώσεις.