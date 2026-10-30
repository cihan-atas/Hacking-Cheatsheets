# ⚡ Naabu - Kapsamlı Başvuru Rehberi (Cheatsheet)

[🇺🇸 English Version](../README.md)

```
  ███╗   ██╗ █████╗  █████╗ ██████╗ ██╗   ██╗
  ████╗  ██║██╔══██╗██╔══██╗██╔══██╗██║   ██║
  ██╔██╗ ██║███████║███████║██████╔╝██║   ██║
  ██║╚██╗██║██╔══██║██╔══██║██╔══██╗██║   ██║
  ██║ ╚████║██║  ██║██║  ██║██████╔╝╚██████╔╝
  ╚═╝  ╚═══╝╚═╝  ╚═╝╚═╝  ╚═╝╚═════╝  ╚═════╝ 
        Hızlı ve Güvenilir SYN/CONNECT Port Tarayıcısı
```

<p align="center">
  <img src="https://img.shields.io/badge/Naabu-Port_Tarayıcı-blue?style=for-the-badge" alt="Naabu">
  <img src="https://img.shields.io/badge/Geliştirici-ProjectDiscovery-orange?style=for-the-badge" alt="ProjectDiscovery">
  <img src="https://img.shields.io/badge/Kategori-Keşif_&_Tarama-green?style=for-the-badge" alt="Keşif & Tarama">
  <img src="https://img.shields.io/badge/Dil-Go-cyan?style=for-the-badge" alt="Go">
</p>

<p align="center">
  <b>⚡ Basitlik, yüksek hız ve ProjectDiscovery araçlarıyla (httpx, nuclei) kesintisiz borulama (pipeline) uyumu için tasarlanmış port tarayıcısı</b>
</p>

---

## 📋 İçindekiler

- [Genel Bakış ve Temel Özellikler](#-genel-bakış-ve-temel-özellikler)
- [Kurulum](#-kurulum)
- [Temel Port Tarama](#-temel-port-tarama)
  - [Tek Hedef Tarama](#tek-hedef-tarama)
  - [En Popüler Portlar ve Tam Aralık](#en-popüler-portlar-ve-tam-aralık)
- [Tarama Modları: SYN (Raw Socket) ve CONNECT Karşılaştırması](#-tarama-modları-syn-raw-socket-ve-connect-karşılaştırması)
- [Hız Sınırlandırma ve Performans Optimizasyonu](#-hız-sınırlandırma-ve-performans-optimizasyonu)
- [Çoklu Hedef ve CIDR Blok Tarama](#-çoklu-hedef-ve-cidr-blok-tarama)
- [Uçtan Uca Borulama (Pipeline) İş Akışları](#-uçtan-uca-borulama-pipeline-iş-akışları)
  - [Naabu + Httpx + Nuclei](#naabu--httpx--nuclei)
  - [Subfinder + Naabu](#subfinder--naabu)
- [Nmap Entegrasyonu (`-nmap`)](#-nmap-entegrasyonu--nmap)
- [Hızlı Referans Tablosu](#-hızlı-referans-tablosu)
- [Savunma Yöntemleri ve Tespit](#-savunma-yöntemleri-ve-tespit)
- [Yasal Uyarı](#-yasal-uyarı)

---

## 🎯 Genel Bakış ve Temel Özellikler

**ProjectDiscovery** ekibi tarafından geliştirilen **Naabu**, modern keşif otomasyonlarında yüksek hız, kararlılık ve zincirleme borulama (pipeline) desteği sağlamak üzere inşa edilmiştir:
- **SYN ve CONNECT Taraması:** Root veya `libpcap` yetkisi yoksa sistem soketlerini kullanarak TCP CONNECT moduna otomatik geçer.
- **ProjectDiscovery Ekosistemi:** `subfinder` çıktısını doğrudan borulayabilir, açık portları `httpx` ve `nuclei` araçlarına aktarabilir.
- **Otomatik Nmap Tetikleme:** Keşfedilen açık portlar üzerinde Nmap servis/sürüm tespitini otomatik olarak çalıştırabilir.
- **CDN Hariç Tutma (Bypass):** Cloudflare, Akamai ve CloudFront gibi CDN uç noktalarını filtreleyerek gereksiz bant genişliği tüketimini önler.

---

## 📥 Kurulum

```bash
# Go ile Kurulum (Önerilen)
go install -v github.com/projectdiscovery/naabu/v2/cmd/naabu@latest

# pdtm (ProjectDiscovery Tool Manager) ile Kurulum
pdtm -i naabu

# Arch Linux (AUR)
yay -S naabu-bin

# Kali Linux
sudo apt update && sudo apt install -y naabu
```

> [!NOTE]
> Linux sistemlerde `CAP_NET_RAW` yetkisi vererek root olmadan da SYN taraması yapabilirsiniz:
> `sudo setcap cap_net_raw=+ep $(which naabu)`

---

## 🚀 Temel Port Tarama

### Tek Hedef Tarama
```bash
# Varsayılan en yaygın 100 portu tarama
naabu -host example.com

# Belirli portları tarama
naabu -host example.com -p 80,443,8080,8443
```

### En Popüler Portlar ve Tam Aralık
```bash
# En popüler 1000 portu tarama
naabu -host example.com -top-ports 1000

# Tüm portları (1-65535) tarama
naabu -host example.com -p -
```

---

## ⚡ Tarama Modları: SYN (Raw Socket) ve CONNECT Karşılaştırması

- **SYN Taraması (`-s syn` / root gerektirir):** 3 zamanlı el sıkışmayı (handshake) tamamlamadan yarı açık paketler gönderir. Çok daha hızlıdır ve hedef uygulama günlüklerine minimum iz bırakır.
- **CONNECT Taraması (`-s conn`):** Standart işletim sistemi `connect()` çağrısını kullanır. Root yetkisi gerektirmez ve kısıtlı sanal makinelerde çalışır.

```bash
# Açıkça SYN Taraması Belirtme (sudo gerektirir)
sudo naabu -host example.com -s syn

# Standart TCP Connect Taraması (sudo gerektirmez)
naabu -host example.com -s conn
```

---

## ⚙️ Hız Sınırlandırma ve Performans Optimizasyonu

```bash
# Saniyede gönderilecek paket sayısı (varsayılan: 1000)
sudo naabu -host example.com -rate 2500

# Zaman aşımı süresi (milisaniye cinsinden)
naabu -host example.com -timeout 1000

# CDN uç sunucularını filtreleme (Cloudflare, Akamai vb.)
naabu -host example.com -exclude-cdn
```

---

## 🌐 Çoklu Hedef ve CIDR Blok Tarama

```bash
# Dosyadaki hedef listesini tarama
naabu -list targets.txt -p 80,443,8080 -o open_ports.txt

# /24 alt ağ bloğunu tarama
sudo naabu -host 192.168.1.0/24 -p 22,80,443,445,3389
```

---

## 🔄 Uçtan Uca Borulama (Pipeline) İş Akışları

Naabu'nun en güçlü yanı diğer araçlarla boru hattı (pipe) kurabilmesidir:

### Subfinder + Naabu
Alt alan adlarını keşfedip doğrudan açık olanları listeleme:
```bash
subfinder -d example.com -silent | naabu -silent -p 80,443,8080,8443
```

### Naabu + Httpx + Nuclei
Bug bounty ve sızma testlerinde altın standart otomasyon hattı:
```bash
# 1. Alt alanları tara -> 2. Açık portları bul -> 3. HTTP servislerini doğrula -> 4. Zafiyetleri tara
subfinder -d target.com -silent \
  | naabu -silent -p 80,443,8080,8443 \
  | httpx -silent \
  | nuclei -severity medium,high,critical -o findings.txt
```

---

## 🔍 Nmap Entegrasyonu (`-nmap`)

Bulunan açık portları doğrudan Nmap'e aktararak servis sürüm tespiti (`-sV`) ve script çalıştırma (`-sC`) yapma:

```bash
sudo naabu -host target.local -top-ports 1000 -nmap-cli 'nmap -sV -sC -oN naabu_nmap.txt'
```

---

## ⚡ Hızlı Referans Tablosu

| Amaç | Komut |
|------|-------|
| **Standart Tarama (İlk 100)**| `naabu -host <HEDEF>` |
| **Tam Port Taraması (1-65535)**| `naabu -host <HEDEF> -p -` |
| **İlk 1000 Port** | `naabu -host <HEDEF> -top-ports 1000` |
| **SYN Taraması (Hızlı & Gizli)**| `sudo naabu -host <HEDEF> -s syn` |
| **CDN IP'lerini Dışlama** | `naabu -host <HEDEF> -exclude-cdn` |
| **JSON Çıktısı Alma** | `naabu -host <HEDEF> -json -o results.json` |
| **HTTPX'e Borulama** | `naabu -host <HEDEF> -silent \| httpx` |
| **Nmap Servis Taraması Tetikleme**| `sudo naabu -host <HEDEF> -nmap-cli 'nmap -sV'` |

---

## 🛡️ Savunma Yöntemleri ve Tespit

1. **Güvenlik Duvarı ve IDS:** Naabu tarafından üretilen yüksek oranlı SYN paketleri ve tamamlanmamış el sıkışmalar IDS/IPS sistemlerince kolayca yakalanır.
2. **Bağlantı Hızı Kısıtlama:** Gelen bağlantılarda oran sınırlandırması (`iptables -m limit --limit 50/s`) uygulayarak otomatik taramaları yavaşlatın ve engelleyin.

---

## ⚠️ Yasal Uyarı

> [!CAUTION]
> Naabu yalnızca yasal izin alınmış hedefler, sızma testleri ve güvenlik denetimleri için kullanılmalıdır. İzniniz olmayan ağları taramak suç teşkil edebilir.
