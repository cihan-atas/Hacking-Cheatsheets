# 🦀 RustScan - Kapsamlı Başvuru Kılavuzu

[🇬🇧 English Version](../README.md)

```
  ██████╗ ██╗   ██╗███████╗████████╗███████╗ ██████╗ █████╗ ███╗   ██╗
  ██╔══██╗██║   ██║██╔════╝╚══██╔══╝██╔════╝██╔════╝██╔══██╗████╗  ██║
  ██████╔╝██║   ██║███████╗   ██║   ███████╗██║     ███████║██╔██╗ ██║
  ██╔══██╗██║   ██║╚════██║   ██║   ╚════██║██║     ██╔══██║██║╚██╗██║
  ██║  ██║╚██████╔╝███████║   ██║   ███████║╚██████╗██║  ██║██║ ╚████║
  ╚═╝  ╚═╝ ╚═════╝ ╚══════╝   ╚═╝   ╚══════╝ ╚═════╝╚═╝  ╚═╝╚═╝  ╚═══╝
       Modern Port Tarayıcısı - 65.535 Portu 3 Saniyede Tarayıp Nmap'e Besleyin
```

<p align="center">
  <img src="https://img.shields.io/badge/RustScan-Port_Tarayıcısı-blue?style=for-the-badge" alt="RustScan">
  <img src="https://img.shields.io/badge/Yazıldığı_Dil-Rust-orange?style=for-the-badge" alt="Dil Rust">
  <img src="https://img.shields.io/badge/Kategori-Keşif_&_Tarama-green?style=for-the-badge" alt="Keşif & Tarama">
  <img src="https://img.shields.io/badge/Lisans-GPLv3-red?style=for-the-badge" alt="Lisans GPLv3">
</p>

<p align="center">
  <b>🌐 Hedef üzerindeki açık portları saniyeler içinde tespit edip anında derin Nmap betiklerine aktaran modern ve yüksek hızlı port tarayıcısı</b>
</p>

---

## 📋 İçindekiler

- [Genel Bakış & Mimari](#-genel-bakış--mimari)
- [Araç Karşılaştırması: RustScan vs Nmap vs Masscan](#-araç-karşılaştırması-rustscan-vs-nmap-vs-masscan)
- [Kurulum](#-kurulum)
- [Temel Kullanım & Tekil Hedef Taraması](#-temel-kullanım--tekil-hedef-taraması)
- [Otomatik Nmap Entegrasyonu](#-otomatik-nmap-entegrasyonu)
- [Performans Optimizasyonu (`ulimit` & Batch Boyutu)](#-performans-optimizasyonu-ulimit--batch-boyutu)
- [Port Aralıkları ve CIDR Alt Ağ Taraması](#-port-aralıkları-ve-cidr-alt-ağ-taraması)
- [Betikleme ve Çıktı Biçimlendirme](#-betikleme-ve-çıktı-biçimlendirme)
- [Hızlı Referans Tablosu](#-hızlı-referans-tablosu)
- [Savunma Açısından Değerlendirme & OPSEC](#-savunma-açısından-değerlendirme--opsec)
- [Yasal Uyarı](#-yasal-uyarı)

---

## 🎯 Genel Bakış & Mimari

**RustScan**, modern **Rust** diliyle geliştirilmiş ultra hızlı bir port tarayıcısıdır. Temel felsefesi oldukça nettir: **1-65535 arasındaki tüm portları yaklaşık 3 saniyede tara, açık olanları tespit et ve derinlemesine servis/versiyon analizi için bu portları beklemeden doğrudan Nmap'e devret**.

Klasik `nmap -p-` taramalarında 10 ila 25 dakika beklemek yerine RustScan, asenkron çoklu iş parçacığı (multi-threading) ve işletim sistemi soket kümeleme mekanizmasını kullanarak açık portları göz açıp kapayıncaya kadar bulur.

---

## ⚖️ Araç Karşılaştırması: RustScan vs Nmap vs Masscan

| Özellik | RustScan | Nmap | Masscan |
|---------|----------|------|---------|
| **Temel Görevi** | Hızlı keşif -> Nmap aktarımı | Derin servis & NSE analizi | İnternet çapında devasa SYN taraması |
| **Tüm Portlar (1-65535) Hızı**| ⭐⭐⭐⭐⭐ (~3 saniye) | ⭐⭐ (5–20 dakika) | ⭐⭐⭐⭐⭐ (~2 saniye) |
| **Servis Versiyon Tespiti**| Nmap borulaması ile | Doğal Var (`-sV`) | ❌ Çok Kısıtlı |
| **NSE Betik Motoru** | Nmap borulaması ile | Doğal Var (`-sC`, `--script`) | ❌ Yok |
| **Paket Motoru** | Asenkron TCP Soketleri | Ham IP / Özel TCP yığını | Özel Asenkron SYN yığını |
| **Otomasyon Kolaylığı** | Doğal CLI argümanları (`--`) | Zengin CLI bayrakları | Yüksek karmaşıklık |

---

## 📥 Kurulum

```bash
# Debian / Ubuntu / Kali Linux (.deb paketi)
wget https://github.com/RustScan/RustScan/releases/download/2.3.0/rustscan_2.3.0_amd64.deb
sudo dpkg -i rustscan_2.3.0_amd64.deb

# Arch Linux (AUR)
yay -S rustscan

# Cargo (Rust Paket Yöneticisi)
cargo install rustscan

# Docker Konteyneri ile Çalıştırma
docker run -it --rm --name rustscan rustscan/rustscan:2.1.1 -a 10.10.10.50
```

---

## 🚀 Temel Kullanım & Tekil Hedef Taraması

```bash
# Hedef IP üzerindeki 65.535 portun tamamını tarama
rustscan -a 10.10.10.50

# Tek satırlık greplenebilir çıktı üretme
rustscan -a 10.10.10.50 --greppable

# Sessiz mod (ASCII banner'ı gizleme)
rustscan -q -a 10.10.10.50
```

---

## 🔗 Otomatik Nmap Entegrasyonu

RustScan'in en güçlü yanı, açık bulunan portları `--` ayracından sonra girilen Nmap komutuna otomatik olarak parametre olarak geçmesidir:

```bash
# Açık portları bul ve Nmap'in varsayılan güvenli betiklerini & versiyon analizini çalıştır
rustscan -a 10.10.10.50 -- -sC -sV

# Nmap çıktısını dosyaya kaydetme
rustscan -a 10.10.10.50 -- -sC -sV -oN nmap_raporu.txt

# Zafiyet taraması betiklerini (vuln) otomatik çalıştırma
rustscan -a 10.10.10.50 -- --script vuln -oA hedef_zafiyetler
```

---

## ⚡ Performans Optimizasyonu (`ulimit` & Batch Boyutu)

RustScan işletim sisteminin dosya tanımlayıcı (socket limit) sınırlarını kullanır:

```bash
# Yerel ağda en yüksek hız için batch boyutunu ve ulimit değerini artırma
rustscan -a 10.10.10.50 -b 5000 -u 5000

# Paket kaybı yaşanan yüksek gecikmeli bağlantılarda batch boyutunu düşürme
rustscan -a 10.10.10.50 -b 500 --scan-order "random"
```

---

## 🌐 Port Aralıkları ve CIDR Alt Ağ Taraması

```bash
# Yalnızca belirli bir port aralığını tarama (örn. 1-1000)
rustscan -a 10.10.10.50 -r 1-1000

# Birden fazla hedef IP tarama
rustscan -a 10.10.10.50,10.10.10.51

# Tüm bir C sınıfı alt ağı tarama
rustscan -a 192.168.1.0/24
```

---

## 📊 Betikleme ve Çıktı Biçimlendirme

```bash
# JSON formatında çıktı alma
rustscan -a 10.10.10.50 --accessible

# Hedefleri bir dosyadan okuma
rustscan -a 'hedefler.txt'
```

---

## ⚡ Hızlı Referans Tablosu

| İşlem | Komut |
|-------|-------|
| **Tüm 65k Portu Tara** | `rustscan -a <IP>` |
| **Tüm Portlar + Nmap Betikleri** | `rustscan -a <IP> -- -sC -sV` |
| **CIDR Alt Ağ Taraması** | `rustscan -a <ALT_AG>/24` |
| **Port Aralığı Taraması** | `rustscan -a <IP> -r 1-1000` |
| **Batch & Ulimit Ayarı** | `rustscan -a <IP> -b 3000 -u 3000` |
| **Sessiz Mod (Banner'sız)** | `rustscan -q -a <IP>` |
| **Greplenebilir Çıktı** | `rustscan -a <IP> --greppable` |
| **Nmap Raporu Olarak Kaydet** | `rustscan -a <IP> -- -sC -sV -oN nmap.txt` |

---

## 🛡️ Savunma Açısından Değerlendirme & OPSEC

1. **Ağ Gürültüsü ve IDS Alarmları:** 65.535 portu 3 saniyede taramak binlerce eşzamanlı TCP paketi üretir. Snort, Suricata ve yeni nesil güvenlik duvarlarında (NGFW) anında alarm tetikler.
2. **Paket Kaybı (Drop):** Yavaş veya VPN tabanlı ağlarda (HTB, THM) yüksek batch değeri (`-b 4000`) paketlerin düşmesine ve açık portların kaçırılmasına yol açabilir. Bu durumlarda `-b 1000` seviyesine inilmelidir.

---

## ⚠️ Yasal Uyarı

> [!CAUTION]
> Bu kılavuz yalnızca izinli güvenlik testleri, sızma testleri ve eğitim amaçlı hazırlanmıştır. İzin alınmayan sistemlere yönelik port taraması gerçekleştirmek yasalara aykırıdır.