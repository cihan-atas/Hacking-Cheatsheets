# 🛡️ WPScan - Kapsamlı Başvuru Kılavuzu

[🇬🇧 English Version](../README.md)

```
  ██╗    ██╗██████╗ ███████╗ ██████╗ █████╗ ███╗   ██╗
  ██║    ██║██╔══██╗██╔════╝██╔════╝██╔══██╗████╗  ██║
  ██║ █╗ ██║██████╔╝███████╗██║     ███████║██╔██╗ ██║
  ██║███╗██║██╔═══╝ ╚════██║██║     ██╔══██║██║╚██╗██║
  ╚███╔███╔╝██║     ███████║╚██████╗██║  ██║██║ ╚████║
   ╚══╝╚══╝ ╚═╝     ╚══════╝ ╚═════╝╚═╝  ╚═╝╚═╝  ╚═══╝
       WordPress Güvenlik ve Zafiyet Tarayıcısı
```

<p align="center">
  <img src="https://img.shields.io/badge/WPScan-WordPress_Güvenliği-blue?style=for-the-badge" alt="WPScan">
  <img src="https://img.shields.io/badge/Kategori-Web_Güvenliği_&_API-orange?style=for-the-badge" alt="Web Güvenliği">
  <img src="https://img.shields.io/badge/Veritabanı-WPVDB_API-green?style=for-the-badge" alt="WPVDB API">
  <img src="https://img.shields.io/badge/Lisans-GPLv3-red?style=for-the-badge" alt="Lisans GPLv3">
</p>

<p align="center">
  <b>🌐 WordPress çekirdek, eklenti, tema ve kullanıcı zafiyetlerini tespit eden endüstri standardı kara kutu web güvenlik tarayıcısı</b>
</p>

---

## 📋 İçindekiler

- [Genel Bakış & Mimari](#-genel-bakış--mimari)
- [Kurulum](#-kurulum)
- [API Token Entegrasyonu (WPVDB)](#-api-token-entegrasyonu-wpvdb)
- [Temel Tarama & Hedef İnceleme](#-temel-tarama--hedef-inceleme)
- [Numaralandırma (Enumeration) Bayrakları (`-e`)](#-numaralandırma-enumeration-bayrakları--e)
  - [Zafiyetli Eklentiler (`-e vp`)](#zafiyetli-eklentiler--e-vp)
  - [Tüm Eklentiler (`-e ap`)](#tüm-eklentiler--e-ap)
  - [Temalar (`-e vt`, `-e at`)](#temalar--e-vt--e-at)
  - [Kullanıcı Adları (`-e u`)](#kullanıcı-adları--e-u)
  - [Hassas Yedek Dosyaları (`-e cb`, `-e dbe`)](#hassas-yedek-dosyaları--e-cb--e-dbe)
- [Eklenti Tespit Modları (`--plugins-detection`)](#-eklenti-tespit-modları---plugins-detection)
- [Parola Kaba Kuvvet Saldırıları (`wp-login.php` & XML-RPC)](#-parola-kaba-kuvvet-saldırıları-wp-loginphp--xml-rpc)
- [Gizlilik & WAF Atlama Seçenekleri](#-gizlilik--waf-atlama-seçenekleri)
- [Hızlı Referans Tablosu](#-hızlı-referans-tablosu)
- [Savunma Açısından Değerlendirme & Sıkılaştırma](#-savunma-açısından-değerlendirme--sıkılaştırma)
- [Yasal Uyarı](#-yasal-uyarı)

---

## 🎯 Genel Bakış & Mimari

İnternet üzerindeki en popüler 10 milyon web sitesinin %40'ından fazlası WordPress altyapısını kullanmaktadır. Bu yaygınlık, WordPress'i saldırganların en çok hedef aldığı içerik yönetim sistemi (CMS) haline getirir.

Ruby ile geliştirilen **WPScan**, WordPress sitelerine yönelik kara kutu güvenlik analizlerinde dünya standardıdır. Aktif çekirdek sürümünü kataloglar, kurulu eklenti ve temaları numaralandırır, yönetici kullanıcı adlarını çeker ve bulguları binlerce bilinen CVE kaydı içeren **WPScan Zafiyet Veritabanı (WPVDB)** ile eşleştirir.

---

## 📥 Kurulum

```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y wpscan

# Arch Linux
sudo pacman -S wpscan

# Ruby Gem ile (Tüm platformlar)
gem install wpscan

# Docker Konteyneri ile Çalıştırma
docker run -it --rm wpscan/wpscan --url https://hedef.local
```

---

## 🔑 API Token Entegrasyonu (WPVDB)

Zafiyetlerin CVE detaylarını ve CVSS risk skorlarını görüntüleyebilmek için [wpscan.com](https://wpscan.com/) adresinden ücretsiz bir API anahtarı alınmalıdır:

```bash
# Komut satırından API token belirtme
wpscan --url https://hedef.local --api-token SIZIN_API_TOKENINIZ

# Veya ortam değişkeni olarak tanımlama
export WPSCAN_API_TOKEN="SIZIN_API_TOKENINIZ"
```

---

## 🚀 Temel Tarama & Hedef İnceleme

```bash
# Hızlı ve temel WordPress taraması
wpscan --url https://hedef.local

# Taramaya başlamadan önce yerel veritabanını güncelleme
wpscan --update
```

---

## 🔍 Numaralandırma (Enumeration) Bayrakları (`-e`)

### Zafiyetli Eklentiler (`-e vp`)
Yalnızca WPVDB veritabanında bilinen güvenlik açığı bulunan eklentileri arar:
```bash
wpscan --url https://hedef.local -e vp --api-token TOKENINIZ
```

### Tüm Eklentiler (`-e ap`)
Sitede kurulu olan binlerce olası eklentiyi sözlük tabanlı kaba kuvvet ile arar:
```bash
wpscan --url https://hedef.local -e ap --plugins-detection aggressive
```

### Temalar (`-e vt`, `-e at`)
```bash
# Yalnızca zafiyet barındıran temaları tespit etme
wpscan --url https://hedef.local -e vt

# Kurulu tüm temaları listeleme
wpscan --url https://hedef.local -e at
```

### Kullanıcı Adları (`-e u`)
REST API uç noktalarından (`/wp-json/wp/v2/users`), yazar arşivlerinden (`/?author=1`) ve giriş hatalarından yazar kullanıcı adlarını ayıklar:
```bash
wpscan --url https://hedef.local -e u1-10
```

### Hassas Yedek Dosyaları (`-e cb`, `-e dbe`)
Dışa açık `wp-config.php.bak`, veritabanı dökümleri (.sql) ve yedek zip arşivlerini arar:
```bash
wpscan --url https://hedef.local -e cb,dbe
```

---

## ⚙️ Eklenti Tespit Modları (`--plugins-detection`)

```bash
# Pasif Mod (Hızlı; sayfa kaynağındaki script ve style etiketlerini inceler)
wpscan --url https://hedef.local --plugins-detection passive

# Agresif Mod (Binlerce eklenti dizinine doğrudan HTTP isteği atar)
wpscan --url https://hedef.local --plugins-detection aggressive

# Karışık Mod (Varsayılan ve önerilen; önce pasif sonra aktif tarar)
wpscan --url https://hedef.local --plugins-detection mixed
```

---

## 🔓 Parola Kaba Kuvvet Saldırıları (`wp-login.php` & XML-RPC)

Kullanıcı adları tespit edildikten sonra hedefli parola denemeleri yapılabilir:

```bash
# Tespit edilen kullanıcı listesine RockYou sözlüğü ile kaba kuvvet uygulama
wpscan --url https://hedef.local -U users.txt -P /usr/share/wordlists/rockyou.txt

# Tek bir parolayı tüm kullanıcılara karşı deneme (Password Spraying)
wpscan --url https://hedef.local -U users.txt -p 'Kış2026!'

# Çok iş parçacıklı hızlı saldırı (20 thread)
wpscan --url https://hedef.local -U admin -P passwords.txt -t 20
```

> [!NOTE]
> Sunucuda `xmlrpc.php` açıksa, WPScan otomatik olarak XML-RPC multicall özelliğini kullanarak tek bir HTTP isteği içinde yüzlerce parolayı aynı anda dener!

---

## 🥷 Gizlilik & WAF Atlama Seçenekleri

```bash
# Basit bot imzalarını atlatmak için rastgele User-Agent kullanma
wpscan --url https://hedef.local --random-user-agent

# Hız sınırlamalarını (Rate Limit) aşmak için istek aralarına bekleme ekleme (ms)
wpscan --url https://hedef.local --throttle 1000

# Tor / SOCKS proxy üzerinden yönlendirme
wpscan --url https://hedef.local --proxy socks5://127.0.0.1:9050

# Kendinden imzalı SSL/TLS sertifika hatalarını yok sayma
wpscan --url https://hedef.local --disable-tls-checks
```

---

## ⚡ Hızlı Referans Tablosu

| İşlem | Komut |
|-------|-------|
| **Standart Tarama** | `wpscan --url <URL> --api-token <TOKEN>` |
| **Zafiyetli Eklentileri Bul** | `wpscan --url <URL> -e vp --api-token <TOKEN>` |
| **Tüm Eklentileri Tara** | `wpscan --url <URL> -e ap --plugins-detection aggressive` |
| **Kullanıcıları Çıkar** | `wpscan --url <URL> -e u` |
| **Yedek Dosyalarını Ara** | `wpscan --url <URL> -e cb,dbe` |
| **Parola Kaba Kuvvet** | `wpscan --url <URL> -U <USERS> -P <PASSWORDS>` |
| **Gizli Tarama (Stealth)** | `wpscan --url <URL> --random-user-agent --stealthy` |
| **JSON Olarak Raporla** | `wpscan --url <URL> -f json -o wpscan_rapor.json` |

---

## 🛡️ Savunma Açısından Değerlendirme & Sıkılaştırma

1. **XML-RPC Servisini Kapatın:** `xmlrpc.php` dosyasına erişimi engelleyerek yüksek hızlı parola kaba kuvvet saldırılarını engelleyin.
2. **REST API Kullanıcı Listesini Gizleyin:** `/wp-json/wp/v2/users` uç noktasını anonim kullanıcılara kapatın.
3. **Eklentileri Düzenli Güncelleyin:** WordPress güvenlik açıklarının %90'ından fazlası çekirdek yazılımdan değil, güncellenmeyen üçüncü parti eklenti ve temalardan kaynaklanır.

---

## ⚠️ Yasal Uyarı

> [!CAUTION]
> Bu kılavuz yalnızca izinli güvenlik testleri, zafiyet değerlendirmeleri ve web denetimleri için hazırlanmıştır. İzin alınmaksızın web sitelerine yönelik zafiyet taraması veya parola denemeleri yapmak suçtur.