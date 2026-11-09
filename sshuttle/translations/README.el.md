<!-- language-home-navigation -->
[🏠 Ελληνική Αρχική Σελίδα](../../README.el.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🛰️ sshuttle - Ολοκληρωμένο Cheatsheet

[🇹🇷 Türkçe Sürüm](μεταφράσεις/README.tr.md)
```
  ███████╗███████╗██╗  ██╗██╗   ██╗████████╗████████╗██╗     ███████╗
  ██╔════╝██╔════╝██║  ██║██║   ██║╚══██╔══╝╚══██╔══╝██║     ██╔════╝
  ███████╗███████╗███████║██║   ██║   ██║      ██║   ██║     █████╗  
  ╚════██║╚════██║██╔══██║██║   ██║   ██║      ██║   ██║     ██╔══╝  
  ███████║███████║██║  ██║╚██████╔╝   ██║      ██║   ███████╗███████╗
  ╚══════╝╚══════╝╚═╝  ╚═╝ ╚═════╝    ╚═╝      ╚═╝   ╚══════╝╚══════╝
       Transparent Proxy Server that Works as a Poor Man's VPN over SSH
```
<p align="center">
  <img src="https://img.shields.io/badge/sshuttle-Poor_Man's_VPN-blue?style=for-the-badge" alt="sshuttle">
  <img src="https://img.shields.io/badge/Category-Pivoting_&_Tunneling-orange?style=for-the-badge" alt="Pivoting & Tunneling">
  <img src="https://img.shields.io/badge/Protocols-TCP_DNS_SSH-green?style=for-the-badge" alt="Protocols">
  <img src="https://img.shields.io/badge/License-LGPLv2.1-red?style=for-the-badge" alt="Άδεια χρήσης LGPLv2.1">
</p>

<p align="center">
  <b>🌐 Προώθηση ολόκληρων εσωτερικών υποδικτύων και αιτημάτων DNS μέσω μιας μη προνομιούχου περιόδου λειτουργίας SSH χωρίς πρόσβαση διαχειριστή στον απομακρυσμένο διακομιστή</b>
</p>

---

## 📋 Πίνακας περιεχομένων

- [Επισκόπηση & Φιλοσοφία του "Poor Man's VPN"](#-επισκόπηση--the-poor-mans-vpn-philosophy)
- [Πώς λειτουργεί το sshuttle](#-how-sshuttle-works)
- [Σύγκριση εργαλείων: sshuttle vs SOCKS vs Ligolo-ng εναντίον OpenVPN](#-tool-comparison-sshuttle-vs-socks-vs-ligolo-ng-vs-openvpn)
- [Εγκατάσταση] (#-εγκατάσταση)
- [Βασική δρομολόγηση υποδικτύου](#-basic-subnet-routing)
- [Προώθηση επισκεψιμότητας DNS (`--dns`)](#-dns-traffic-forwarding---dns)
- [Πλήρης ανακατεύθυνση πύλης (Προεπιλεγμένη διαδρομή)](#-full-gateway-redirection-default-route)
- [Εξαιρέσεις υποδικτύου (`-x`)](#-εξαιρέσεις υποδικτύου--x)
- [SSH Options & Key-Based Authentication (`-e`)](#-ssh-options--key-based-authentication--e)
- [Εκτέλεση παρασκηνίου Daemon (`-D`)](#-daemon-background-execution--d)
- [Πίνακας γρήγορης αναφοράς] (#-ταχεία αναφορά-πίνακας)
- [Defensive Considerations & OPSEC](#-defensive-considerations--opsec)
- [Νομική αποποίηση ευθυνών](#-νομική αποποίηση ευθυνών)

---

## 🎯 Επισκόπηση & Η φιλοσοφία του "Poor Man's VPN".

Το **sshuttle** είναι ένας διαφανής διακομιστής μεσολάβησης που λειτουργεί ως ένα ελαφρύ VPN μηδενικής διαμόρφωσης μέσω μιας τυπικής σύνδεσης SSH.

Σε τυπικές δοκιμές εταιρικής διείσδυσης και αξιολογήσεις Red Team, η περιστροφή σε εσωτερικά δίκτυα παρεμποδίζεται από αυστηρούς περιορισμούς:
- ❌ Σπάνια έχετε πρόσβαση `root` ή `SYSTEM` στον αρχικό προμαχώνα SSH/κεντρικό κεντρικό υπολογιστή.
- ❌ Οι παραδοσιακές λύσεις VPN (OpenVPN, WireGuard) απαιτούν δικαιώματα διαχειριστή και εικονικές συσκευές δικτύου TUN/TAP και στα δύο τελικά σημεία.
- ❌ Οι τυπικοί διακομιστής μεσολάβησης SOCKS (όπως το `ssh -D`) απαιτούν τη διαμόρφωση του `proxychains` ή των μεμονωμένων ρυθμίσεων διακομιστή μεσολάβησης εφαρμογής για κάθε εργαλείο.

** Το sshuttle λύνει και τους τρεις περιορισμούς ταυτόχρονα:**
- ✅ **Δεν απαιτείται απομακρυσμένη ρίζα:** Ο κεντρικός διακομιστής χρειάζεται μόνο μια τυπική εγκατάσταση Python (`python3`).
- ✅ **Δεν απαιτείται απομακρυσμένο TUN/TAP:** Χρησιμοποιεί καθαρή πολυπλεξία ροής TCP κατά τη διάρκεια της περιόδου λειτουργίας SSH.
- ✅ **Διαφανής δρομολόγηση σε όλο το σύστημα:** Τροποποιεί τα τοπικά `iptables` / `nftables` στο πλαίσιο εισβολέα, επιτρέποντας στα εργαλεία (`nmap`, `curl`, `smbclient`, προγράμματα περιήγησης ιστού) να στοχεύουν εσωτερικές IP απευθείας χωρίς περιτυλίγματα!

---

## ⚙️ Πώς λειτουργεί το sshuttle
```
[ Attacker Box (Local Root) ]
  ├── iptables / nftables redirects internal IP traffic (10.10.10.0/24) to local port
  └── sshuttle client multiplexes TCP packets over SSH tunnel
              │
              ▼ (Encrypted SSH Channel - Port 22)
[ Remote Pivot Server (Unprivileged User + Python) ]
  └── Assembles TCP connections and forwards them directly to internal hosts
              │
              ▼
[ Target Internal Network (10.10.10.0/24) ]
```
---

## ⚖️ Σύγκριση εργαλείων: sshuttle vs SOCKS vs Ligolo-ng vs OpenVPN

| Χαρακτηριστικό | `sshuttle` | ΚΑΛΤΣΕΣ (`ssh -D`) | `Ligolo-ng` | `OpenVPN` |
|---------|------------|-----------------|-------------|-----------|
| **Απαιτείται απομακρυσμένη ρίζα** | ❌ Όχι | ❌ Όχι | ❌ Όχι | ✅ Ναι |
| **Απαιτείται τοπική ρίζα** | ✅ Ναι (iptables) | ❌ Όχι | ✅ Ναι (διεπαφή TUN) | ✅ Ναι |
| **Διαφανής δρομολόγηση** | ✅ Ναι | ❌ Απαιτεί Proxychains | ✅ Ναι (Native TUN) | ✅ Ναι |
| **Υποστήριξη κυκλοφορίας UDP** | ❌ Μόνο DNS | ❌ Όχι | ✅ Ναι | ✅ Πλήρες UDP |
| **Απαιτείται Python στο Target**| ✅ Ναι (Python 3) | ❌ Όχι | ❌ Όχι (Ανεξάρτητος πράκτορας)| ❌ Όχι |
| **Σάρωση Nmap SYN (`-sS`)** | ❌ Σύνδεση TCP (`-sT`)| ❌ Μόνο σύνδεση TCP | ✅ Υποστήριξη ακατέργαστων πακέτων | ✅ Ολόκληρα ακατέργαστα πακέτα |

---

## 📥 Εγκατάσταση
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y sshuttle

# Arch Linux
sudo pacman -S sshuttle

# macOS (Homebrew)
brew install sshuttle

# Python Pipx / Pip
pipx install sshuttle
```
---

## 🚀 Βασική δρομολόγηση υποδικτύου

Προωθήστε ένα ολόκληρο απομακρυσμένο υποδίκτυο Κατηγορίας C μέσω του συγκεντρωτικού κεντρικού υπολογιστή SSH:
```bash
# Route 10.10.10.0/24 through user@192.168.1.50
sshuttle -r user@192.168.1.50 10.10.10.0/24

# Route multiple disjoint subnets simultaneously
sshuttle -r user@192.168.1.50 10.10.10.0/24 172.16.0.0/16 192.168.100.0/24
```
Μόλις συνδεθείτε, μπορείτε να επικοινωνήσετε απευθείας με εσωτερικά μηχανήματα:
```bash
# No proxychains needed!
curl http://10.10.10.25:8080/
smbclient -L //10.10.10.10/ -U 'domain/user%pass'
```
---

## 🌐 Προώθηση επισκεψιμότητας DNS (`--dns`)

Προώθηση εσωτερικών εταιρικών ερωτημάτων DNS (`.corp`, `.local`) στον εσωτερικό διακομιστή DNS του απομακρυσμένου δικτύου:
```bash
# Route subnet and proxy internal DNS queries
sshuttle --dns -r user@192.168.1.50 10.10.10.0/24

# Query internal domain directly via nslookup/dig
dig dc01.internal.corp
```
---

## 🌍 Ανακατεύθυνση πλήρους πύλης (Προεπιλεγμένη διαδρομή)

Δρομολογήστε όλη την εξερχόμενη κίνηση εισβολέων μέσω της σύνδεσης SSH (πλήρης συμπεριφορά VPN):
```bash
# Route all traffic through remote SSH server (0.0.0.0/0)
sshuttle -r user@remote-vps.com 0.0.0.0/0

# Route all traffic except local network
sshuttle -r user@remote-vps.com 0.0.0.0/0 -x 192.168.1.0/24
```
---

## 🚫 Εξαιρέσεις υποδικτύου (`-x`)

Αποτρέψτε τη σύλληψη συγκεκριμένων ευαίσθητων υποδικτύων ή υποδικτύων επαναφοράς:
```bash
# Route entire 10.0.0.0/8 network, but exclude the pivot host itself
sshuttle -r user@10.0.0.50 10.0.0.0/8 -x 10.0.0.50/32
```
---

## 🔑 Επιλογές SSH & έλεγχος ταυτότητας βάσει κλειδιού (`-e`)

Περάστε προσαρμοσμένες σημαίες SSH (ιδιωτικά κλειδιά ταυτότητας, μη τυπικές θύρες SSH):
```bash
# Use custom identity file and non-standard SSH port 2222
sshuttle -e 'ssh -i /path/to/id_rsa -p 2222' -r user@192.168.1.50 10.10.10.0/24
```
---

## 🤖 Εκτέλεση στο παρασκήνιο Daemon (`-D`)

Εκτελέστε το sshuttle ως δαίμονα φόντου κατά τη διάρκεια των συνεχιζόμενων δεσμεύσεων:
```bash
# Start background daemon with pidfile
sudo sshuttle -D --pidfile /tmp/sshuttle.pid -r user@192.168.1.50 10.10.10.0/24

# Terminate daemon when finished
sudo kill $(cat /tmp/sshuttle.pid)
```
---

## ⚡ Γρήγορος Πίνακας Αναφοράς

| Στόχος | Εντολή |
|-----------|---------|
| **Μονό υποδίκτυο διαδρομής** | `sshuttle -r <USER>@<PIVOT> <SUBNET>/24` |
| **Δρομολόγηση υποδικτύου με DNS** | `sshuttle --dns -r <USER>@<PIVOT> <SUBNET>/24` |
| **Δρομολόγηση πολλαπλών υποδικτύων** | `sshuttle -r <USER>@<PIVOT> <NET1>/24 <NET2>/16` |
| **Εξαίρεση IP/Υποδίκτυο** | `sshuttle -r <USER>@<PIVOT> <SUBNET>/24 -x <EXCLUDE_IP>` |
| **Πλήρης ανακατεύθυνση κυκλοφορίας** | `sshuttle -r <USER>@<PIVOT> 0.0.0.0/0` |
| **Προσαρμοσμένο κλειδί SSH/Θύρα** | `sshuttle -e 'ssh -i <KEY> -p <PORT>' -r <USER>@<PIVOT> <SUBNET>` |
| **Εκτέλεση στο παρασκήνιο** | `sshuttle -D --pidfile /tmp/sshuttle.pid -r <USER>@<PIVOT> <SUBNET>` |

---

## 🛡️ Αμυντικές Θεωρήσεις & OPSEC

1. **Τροποποίηση τείχους προστασίας:** Το sshuttle τροποποιεί πίνακες τοπικού τείχους προστασίας (`iptables` σε Linux, `pf` σε macOS). Εάν το sshuttle διακοπεί ακάθαρτα, χρησιμοποιήστε το `iptables -F` ή επανεκκινήστε την υπηρεσία τείχους προστασίας για να ξεπλύνετε τους παλιούς κανόνες ανακατεύθυνσης.
2. **Περιορισμός μόνο για TCP:** Πακέτα UDP που δεν είναι DNS, αιτήματα ηχούς ICMP (ping) και σαρώσεις SYN δεν υποστηρίζονται. Να χρησιμοποιείτε πάντα το `nmap -sT -Pn -n` για σάρωση θυρών μέσω shuttle.
3. **Target Footprint:** Το sshuttle ανεβάζει αυτόματα ένα μικρό σενάριο συναρμολόγησης Python στον απομακρυσμένο κεντρικό κεντρικό υπολογιστή κατά την προετοιμασία της περιόδου λειτουργίας και το εκτελεί μέσω του `python3`.

---

## ⚠️ Νομική αποποίηση ευθύνης

> [!ΠΡΟΣΟΧΗ]
> Αυτό το cheatsheet προορίζεται μόνο για εξουσιοδοτημένες δοκιμές διείσδυσης και διαχείριση συστήματος. Η μη εξουσιοδοτημένη δρομολόγηση και υποκλοπή κυκλοφορίας δικτύου είναι παράνομη.