# 📡 dnsx - Kapsamlı Başvuru Kılavuzu

[🇬🇧 English Version](../README.md)

```
  ██████╗ ███╗   ██╗███████╗██╗  ██╗
  ██╔══██╗████╗  ██║██╔════╝╚██╗██╔╝
  ██║  ██║██╔██╗ ██║███████╗ ╚███╔╝ 
  ██║  ██║██║╚██╗██║╚════██║ ██╔██╗ 
  ██████╔╝██║ ╚████║███████║██╔╝ ██╗
  ╚═════╝ ╚═╝  ╚═══╝╚══════╝╚═╝  ╚═╝
       ProjectDiscovery Tarafından Geliştirilen Hızlı & Çok Amaçlı DNS Araç Seti
```

<p align="center">
  <img src="https://img.shields.io/badge/dnsx-DNS_Araç_Seti-blue?style=for-the-badge" alt="dnsx">
  <img src="https://img.shields.io/badge/Geliştirici-ProjectDiscovery-orange?style=for-the-badge" alt="ProjectDiscovery">
  <img src="https://img.shields.io/badge/Kategori-Keşif_&_Tarama-green?style=for-the-badge" alt="Keşif & Tarama">
  <img src="https://img.shields.io/badge/Lisans-MIT-red?style=for-the-badge" alt="Lisans MIT">
</p>

<p align="center">
  <b>🌐 Yüksek performanslı DNS çözümleme, joker (wildcard) DNS filtreleme, ters DNS (PTR) sorguları ve kayıt ayıklama motoru</b>
</p>

---

## 📋 İçindekiler

- [Genel Bakış & Mimari](#-genel-bakış--mimari)
- [Neden dnsx? (Wildcard Filtreleme ve Hız)](#-neden-dnsx-wildcard-filtreleme-ve-hız)
- [Kurulum](#-kurulum)
- [Temel Alt Alan Adı Çözümleme](#-temel-alt-alan-adı-çözümleme)
- [Joker (Wildcard) DNS Filtreleme (`-wd`)](#-joker-wildcard-dns-filtreleme--wd)
- [Belirli DNS Kayıt Türlerini Sorgulama](#-belirli-dns-kayıt-türlerini-sorgulama)
- [Ters DNS Sorguları (Reverse PTR - `-ptr`)](#-ters-dns-sorguları-reverse-ptr---ptr)
- [Özel Çözümleyiciler (Resolvers) ve Hız Limiti](#-özel-çözümleyiciler-resolvers-ve-hız-limiti)
- [Boru Hattı Otomasyonu (Subfinder + dnsx + httpx)](#-boru-hattı-otomasyonu-subfinder--dnsx--httpx)
- [Hızlı Referans Tablosu](#-hızlı-referans-tablosu)
- [Savunma Açısından Değerlendirme & OPSEC](#-savunma-açısından-değerlendirme--opsec)
- [Yasal Uyarı](#-yasal-uyarı)

---

## 🎯 Genel Bakış & Mimari

**ProjectDiscovery** ekibi tarafından Go diliyle geliştirilen **dnsx**; sızma testleri, bug bounty ve dış ağ keşif süreçleri için tasarlanmış yüksek performanslı ve çok amaçlı bir DNS aracıdır.

`dig` ve `nslookup` gibi geleneksel araçlar sorguları tek tek çözerken, `dnsx` binlerce sorguyu asenkron eşzamanlılıkla işler, yanıt vermeyen DNS sunucularını eler, saf IP adreslerini ayıklar ve alt alan adı keşfinde veri setini kirleten joker (wildcard) kayıtları temizler.

---

## ⚡ Neden dnsx? (Wildcard Filtreleme ve Hız)

Pasif keşif araçları (`subfinder`, `amass`) sıklıkla ölü veya wildcard yapılandırmasına sahip yüzlerce sahte alan adı döndürür. Bu ölü hostlara HTTP istekleri atmak zaman ve bant genişliği kaybıdır.

`dnsx` bu sorunu çözer:
1. **Wildcard Tespiti (`-wd`):** `*.hedef.com` alan adının tek bir IP'ye yönlendiği durumları analiz ederek gerçekte var olmayan alt alan adlarını filtreler.
2. **Yüksek Hız:** Dakikada 10.000'den fazla sorguyu güvenle çözümler.
3. **Temiz Çıktı Akışı:** Çıktıyı doğrudan `httpx` veya `nmap` araçlarına borular.

---

## 📥 Kurulum

```bash
# Go ile Kurulum (Tavsiye Edilen)
go install -v github.com/projectdiscovery/dnsx/cmd/dnsx@latest

# Kali Linux & Debian
sudo apt update && sudo apt install -y dnsx

# MacOS (Homebrew)
brew install dnsx

# Sürüm doğrulaması
dnsx -version
```

---

## 🚀 Temel Alt Alan Adı Çözümleme

```bash
# Tek bir alan adını çözümleme
echo "hedef.com" | dnsx

# Dosyadan alt alan adı listesi çözümleme
dnsx -l subdomains.txt

# Alan adı ve karşılık gelen A kaydı IP adresini gösterme
dnsx -l subdomains.txt -a -resp

# Sadece çözümlenen temiz IP adreslerini listeleme (Nmap için hazır)
dnsx -l subdomains.txt -resp-only
```

---

## 🚫 Joker (Wildcard) DNS Filtreleme (`-wd`)

Wildcard DNS, var olmayan alan adlarına bile (`rastgele123.hedef.com`) sabit bir IP döndürerek keşif verisini bozar.

```bash
# Wildcard alt alan adlarını ayıklayıp sadece gerçek çalışan hostları yazdırma
dnsx -l subdomains.txt -wd hedef.com -o gercek_hostlar.txt

# Özel eşik değeri ile filtreleme
dnsx -l subdomains.txt -wd hedef.com -wt 3
```

---

## 📋 Belirli DNS Kayıt Türlerini Sorgulama

```bash
# CNAME kayıtlarını çekme (Subdomain Takeover tespiti için kritiktir)
dnsx -l subdomains.txt -cname -resp

# E-posta sunucu (MX) kayıtlarını sorgulama
dnsx -l domains.txt -mx -resp

# TXT kayıtlarını çekme (SPF, DKIM, doğrulama anahtarları)
dnsx -l domains.txt -txt -resp

# Tüm standart kayıtları (A, AAAA, CNAME, NS, TXT, MX, SOA) topluca çekme
dnsx -l domains.txt -recon
```

---

## 🔄 Ters DNS Sorguları (Reverse PTR - `-ptr`)

Bir IP aralığındaki sunucuların alan adlarını ve sanal sunucularını (vhost) PTR kayıtları üzerinden keşfetme:

```bash
# IP listesindeki adresleri ters DNS ile host adına dönüştürme
dnsx -ptr -l ip_listesi.txt -resp

# Tek bir alt ağ üzerinde ters DNS taraması
echo "192.168.1.0/24" | dnsx -ptr -resp
```

---

## ⚙️ Özel Çözümleyiciler (Resolvers) ve Hız Limiti

```bash
# Özel güvenilir resolver listesi kullanma
dnsx -l subdomains.txt -r resolvers.txt

# İş parçacığı ve saniye başına sorgu limiti belirleme
dnsx -l subdomains.txt -t 100 -rl 300
```

---

## ⛓️ Boru Hattı Otomasyonu (Subfinder + dnsx + httpx)

Modern Bug Bounty ve sızma testi keşif döngüsü:

```bash
# Keşfet -> dnsx ile doğrula ve wildcard'ları temizle -> Canlı HTTP servislerini tespit et
subfinder -d hedef.com -silent | \
  dnsx -silent -wd hedef.com | \
  httpx -silent -status-code -title -tech-detect
```

---

## ⚡ Hızlı Referans Tablosu

| İşlem | Komut |
|-------|-------|
| **Alt Alan Adlarını Çözümle** | `dnsx -l subdomains.txt` |
| **Yalnızca IP Adreslerini Al** | `dnsx -l subdomains.txt -resp-only` |
| **Wildcard Alan Adlarını Filtrele**| `dnsx -l subdomains.txt -wd <ALAN_ADI>` |
| **CNAME Kayıtlarını Ayıkla** | `dnsx -l subdomains.txt -cname -resp` |
| **Tüm Kayıtları Çek (Recon)** | `dnsx -l domains.txt -recon` |
| **Ters DNS (PTR) Sorgusu** | `dnsx -l ips.txt -ptr -resp` |
| **Özel Çözümleyici & Thread** | `dnsx -l liste.txt -r resolvers.txt -t 150` |
| **JSON Çıktısı Al** | `dnsx -l liste.txt -json -o sonuc.json` |

---

## 🛡️ Savunma Açısından Değerlendirme & OPSEC

1. **DNS Taşkını:** İç ağ DNS sunucularına binlerce sorgu basmak CPU yükünü artırır ve SIEM sistemlerinde DNS Tünelleme / DoS alarmlarına yol açar.
2. **Resolver Zehirlenmesi:** Bilinmeyen genel resolver listeleri sorgu içeriğinizi üçüncü taraflara ifşa edebilir. Doğrulanmış resolver havuzları kullanın.

---

## ⚠️ Yasal Uyarı

> [!CAUTION]
> Bu kılavuz yalnızca yetkili güvenlik testleri ve bug bounty kapsamı dahilindeki araştırmalar için hazırlanmıştır. Yetkisiz altyapılara yönelik kitlesel DNS sorguları göndermek suç teşkil edebilir.