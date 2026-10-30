# ⚡ Masscan - Kapsamlı Başvuru Kılavuzu

[🇬🇧 English Version](../README.md)

```
  ███╗   ███╗ █████╗ ███████╗███████╗ ██████╗ █████╗ ███╗   ██╗
  ████╗ ████║██╔══██╗██╔════╝██╔════╝██╔════╝██╔══██╗████╗  ██║
  ██╔████╔██║███████║███████╗███████╗██║     ███████║██╔██╗ ██║
  ██║╚██╔╝██║██╔══██║╚════██║╚════██║██║     ██╔══██║██║╚██╗██║
  ██║ ╚═╝ ██║██║  ██║███████║███████║╚██████╗██║  ██║██║ ╚████║
  ╚═╝     ╚═╝╚═╝  ╚═╝╚══════╝╚══════╝ ╚═════╝╚═╝  ╚═╝╚═╝  ╚═══╝
       Dünyanın En Hızlı İnternet Port Tarayıcısı - Saniyede 10 Milyon Asenkron Paket
```

<p align="center">
  <img src="https://img.shields.io/badge/Masscan-İnternet_Port_Tarayıcısı-blue?style=for-the-badge" alt="Masscan">
  <img src="https://img.shields.io/badge/Yazıldığı_Dil-C-orange?style=for-the-badge" alt="Dil C">
  <img src="https://img.shields.io/badge/Kategori-Keşif_&_Tarama-green?style=for-the-badge" alt="Keşif & Tarama">
  <img src="https://img.shields.io/badge/Lisans-AGPLv3-red?style=for-the-badge" alt="Lisans AGPLv3">
</p>

<p align="center">
  <b>🌐 Tüm IPv4 internetini 5 dakikadan kısa sürede tarayabilen, bağımsız TCP/IP yığınına sahip yüksek performanslı asenkron port tarayıcısı</b>
</p>

---

## 📋 İçindekiler

- [Genel Bakış & Asenkron Mimari](#-genel-bakış--asenkron-mimari)
- [Araç Karşılaştırması: Masscan vs Nmap vs RustScan](#-araç-karşılaştırması-masscan-vs-nmap-vs-rustscan)
- [Kurulum](#-kurulum)
- [Temel Kullanım & Hedef Tarama](#-temel-kullanım--hedef-tarama)
- [Paket Gönderim Hızı Ayarı (`--rate`)](#-paket-gönderim-hızı-ayarı---rate)
- [Port Tanımlama Yöntemleri (TCP & UDP)](#-port-tanımlama-yöntemleri-tcp--udp)
- [Banner Bilgisi Çekme (`--banners`)](#-banner-bilgisi-çekme---banners)
- [Hedef Hariç Tutma Listeleri (`--excludefile`)](#-hedef-hariç-tutma-listeleri---excludefile)
- [Çıktı Formatları & Nmap'e Besleme](#-çıktı-formatları--nmape-besleme)
- [Hızlı Referans Tablosu](#-hızlı-referans-tablosu)
- [Savunma Açısından Değerlendirme & Donanım Limitleri](#-savunma-açısından-değerlendirme--donanım-limitleri)
- [Yasal Uyarı](#-yasal-uyarı)

---

## 🎯 Genel Bakış & Asenkron Mimari

**Robert Graham** tarafından geliştirilen **Masscan**, siber güvenlik dünyasında bilinen en hızlı port tarayıcısıdır. Nmap ile benzer çıktılar üretse de, dahili mimarisi `scanrand`, `unicornscan` ve `ZMap` gibi **asenkron paket iletimine** dayanır.

İşletim sisteminin standart çekirdek TCP yığınını kullanan ve yanıt bekleyen Nmap'in aksine Masscan; kendi optimize kullanıcı alanı (user-space) TCP/IP yığınını barındırır. Durum tablosu (state table) tutmadan, ham SYN paketlerini saniyede **10 milyon paketi aşan hızlarda** doğrudan ağa fırlatır.

---

## ⚖️ Araç Karşılaştırması: Masscan vs Nmap vs RustScan

| Özellik | Masscan | Nmap | RustScan |
|---------|---------|------|----------|
| **Maksimum Paket Hızı** | 10.000.000 paket/sn | ~1.000 paket/sn | İşletim sistemi limitinde |
| **Tüm İnternet Taraması (/0)**| ✅ 5 dakikanın altında | ❌ Günler / Haftalar | ❌ /0 için tasarlanmamıştır |
| **TCP Yığın Mimarisi** | Özel Ham SYN Yığını | İşletim Sistemi Çekirdeği | Asenkron Tokio TCP Soketleri |
| **Derin Versiyon & NSE Betikleri**| ❌ Yalnızca Basit Banner | ✅ 600+ NSE betiği | ✅ Nmap entegrasyonu ile |
| **Bağlantı Takibi** | Durumsuz (Blackrock PRNG)| Durumlu soket havuzu | Asenkron görevler |
| **İdeal Kullanım Alanı** | B/A sınıfı devasa ağlar (/8, /16)| Tekil hedefler & /24 ağlar | Tekil makineler & /24 ağlar |

---

## 📥 Kurulum

```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y masscan

# Arch Linux
sudo pacman -S masscan

# Kaynak koddan derleme (En son GitHub sürümü)
sudo apt install -y git gcc make libpcap-dev
git clone https://github.com/robertdavidgraham/masscan
cd masscan && make -j$(nproc)
sudo make install
```

---

## 🚀 Temel Kullanım & Hedef Tarama

```bash
# Tek bir IP adresini web portları için tarama
sudo masscan 10.10.10.50 -p80,443,8080 --rate 1000

# Tüm bir B sınıfı iç alt ağı (172.16.0.0/16) SMB portu (445) için tarama
sudo masscan 172.16.0.0/16 -p445 --rate 5000

# Belirli bir hedefteki 65.535 portun tamamını tarama
sudo masscan 10.10.10.50 -p1-65535 --rate 2000
```

> [!IMPORTANT]
> Masscan ham ağ paketleri ürettiği için mutlaka **root / sudo yetkileriyle** çalıştırılmalıdır.

---

## ⚡ Paket Gönderim Hızı Ayarı (`--rate`)

`--rate` parametresi saniyede kaç paket fırlatılacağını belirler:

```bash
# Ev interneti / modem / Wi-Fi için güvenli hız (100–500 paket/sn)
sudo masscan 10.10.10.0/24 -p1-1000 --rate 300

# Kurumsal gigabit iç ağlar için ideal hız (5.000–10.000 paket/sn)
sudo masscan 10.0.0.0/8 -p80,443 --rate 10000

# Veri merkezi 10 Gbps hatlar için maksimum hız (100.000+ paket/sn)
sudo masscan 0.0.0.0/0 -p443 --rate 100000
```

---

## 🔌 Port Tanımlama Yöntemleri (TCP & UDP)

```bash
# Belirli TCP portları
sudo masscan 10.10.10.50 -p21,22,80,443,445,3389

# En sık kullanılan 100 port
sudo masscan 10.10.10.50 --top-ports 100

# UDP portları (U: ön eki ile)
sudo masscan 10.10.10.50 -p U:53,U:161,U:123 --rate 500
```

---

## 🔍 Banner Bilgisi Çekme (`--banners`)

SYN el sıkışmasının ardından Masscan servisle iletişime geçip banner bilgilerini toplayabilir:

```bash
# HTTP, FTP ve SSH banner bilgilerini yakalama
sudo masscan 10.10.10.0/24 -p80,21,22 --banners --rate 1000
```

---

## 🚫 Hedef Hariç Tutma Listeleri (`--excludefile`)

Kapsam dışı veya hassas sunucuları taramanın dışında bırakmak için:

```bash
# Kritik ağ geçitlerini hariç tutma
sudo masscan 10.0.0.0/8 -p80 --rate 2000 --exclude 10.0.0.1,10.1.1.1

# Hariç tutma dosyasından okuma
sudo masscan 192.168.0.0/16 -p445 --rate 2000 --excludefile /etc/masscan/exclude.conf
```

---

## 📊 Çıktı Formatları & Nmap'e Besleme

```bash
# JSON formatında kaydetme
sudo masscan 10.10.10.0/24 -p80,443 -oJ masscan_sonuclari.json

# Basit liste formatında kaydetme (-oL)
sudo masscan 10.10.10.0/24 -p1-65535 --rate 2000 -oL acik_portlar.txt

# Açık portları ayrıştırıp doğrudan Nmap'e aktarma
cat acik_portlar.txt | awk '{print $4}' | sort -u | paste -sd, - | xargs -I{} nmap -sC -sV -p{} 10.10.10.50
```

---

## ⚡ Hızlı Referans Tablosu

| İşlem | Komut |
|-------|-------|
| **Tekil Hedef (Tüm Portlar)** | `sudo masscan <IP> -p1-65535 --rate 2000` |
| **Geniş Ağda Tek Port Tarama** | `sudo masscan <ALT_AG>/16 -p<PORT> --rate 5000` |
| **Banner Yakalama ile Tara** | `sudo masscan <IP> -p80,22 --banners --rate 1000` |
| **UDP Port Taraması** | `sudo masscan <IP> -p U:53,U:161 --rate 500` |
| **IP Hariç Tutma** | `sudo masscan <AG> -p80 --exclude <IP1>,<IP2>` |
| **JSON Çıktısı Al** | `sudo masscan <IP> -p80,443 -oJ cikti.json` |
| **Liste Çıktısı Al** | `sudo masscan <IP> -p80,443 -oL cikti.txt` |

---

## 🛡️ Savunma Açısından Değerlendirme & Donanım Limitleri

1. **Ev Modemi Çökmesi:** Standart ev modemlerinde veya Wi-Fi ağlarında `--rate 100000` verilirse, modemin NAT bağlantı izleme tablosu kilitlenir ve modem donar/yeniden başlar. Ev ağlarında hız 500-1000 aralığında tutulmalıdır.
2. **Bant Genişliği Tüketimi:** Saniyede 10 milyon paket göndermek yaklaşık 5.4 Gbps ağ trafiği yaratır.
3. **Çekirdek RST Müdahalesi:** Masscan kendi TCP yığınını kullandığı için yerel Linux çekirdeği gelen yanıtlardan haberdar olmaz ve hedefe otomatik TCP RST paketleri gönderebilir. Banner toplarken bunu engellemek için:
   ```bash
   sudo iptables -A INPUT -p tcp --dport 60000 -j DROP
   sudo masscan ... --source-port 60000
   ```

---

## ⚠️ Yasal Uyarı

> [!CAUTION]
> Masscan son derece yıkıcı hızlara ulaşabilen bir tarayıcıdır. İzin alınmamış genel internet bloklarını taramak servis kesintilerine (DDoS etkisi) yol açabilir ve bilişim suçları mevzuatları kapsamında cezalandırılır. Yalnızca yetkilendirildiğiniz ağlarda kullanınız.