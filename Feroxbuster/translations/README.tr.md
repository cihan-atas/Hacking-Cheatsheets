# 🦀 Feroxbuster - Kapsamlı Başvuru Kılavuzu

[🇺🇸 English Version](../README.md)

```
  ███████╗███████╗██████╗  ██████╗ ██╗  ██╗██████╗ ██╗   ██╗███████╗████████╗███████╗██████╗ 
  ██╔════╝██╔════╝██╔══██╗██╔═══██╗╚██╗██╔╝██╔══██╗██║   ██║██╔════╝╚══██╔══╝██╔════╝██╔══██╗
  █████╗  █████╗  ██████╔╝██║   ██║ ╚███╔╝ ██████╔╝██║   ██║███████╗   ██║   █████╗  ██████╔╝
  ██╔══╝  ██╔══╝  ██╔══██╗██║   ██║ ██╔██╗ ██╔══██╗██║   ██║╚════██║   ██║   ██╔══╝  ██╔══██╗
  ██║     ███████╗██║  ██║╚██████╔╝██╔╝ ██╗██████╔╝╚██████╔╝███████║   ██║   ███████╗██║  ██║
  ╚═╝     ╚══════╝╚═╝  ╚═╝ ╚═════╝ ╚═╝  ╚═╝╚═════╝  ╚═════╝ ╚══════╝   ╚═╝   ╚══════╝╚═╝  ╚═╝
                Fast, Simple, Recursive Content Discovery
```

<p align="center">
  <img src="https://img.shields.io/badge/Feroxbuster-Content_Discovery-orange?style=for-the-badge" alt="Feroxbuster">
  <img src="https://img.shields.io/badge/Rust-Blazingly_Fast-blue?style=for-the-badge" alt="Rust">
  <img src="https://img.shields.io/badge/Auto_Tune-Recursive-green?style=for-the-badge" alt="Recursive">
  <img src="https://img.shields.io/badge/Bug_Bounty-Web_Pentest-red?style=for-the-badge" alt="Web Pentest">
</p>

<p align="center">
  <b>⚡ Rust ile yazılmış hızlı, basit ve özyinelemeli (recursive) web içerik keşif aracı</b>
</p>

---

## 📋 İçindekiler

- [Giriş](#-giriş)
- [Temel Özellikler](#-temel-özellikler)
- [Araç Karşılaştırması](#-araç-karşılaştırması)
- [Kurulum](#-kurulum)
- [Temel Sözdizimi ve Hızlı Başlangıç](#-temel-sözdizimi-ve-hızlı-başlangıç)
- [Temel Komut Seçenekleri](#-temel-komut-seçenekleri)
  - [Özyineleme (Recursion) Kontrolü](#özyineleme-recursion-kontrolü)
  - [Eşzamanlılık ve Performans](#eşzamanlılık-ve-performans)
  - [Dosya Uzantıları](#dosya-uzantıları)
  - [Filtreleme ve Eşleme](#filtreleme-ve-eşleme)
  - [Akıllı Özellikler (Auto-Tune, Link Çıkarma, Yedek Arama)](#akıllı-özellikler)
  - [Başlıklar, Çerezler ve Kimlik Doğrulama](#başlıklar-çerezler-ve-kimlik-doğrulama)
  - [Proxy ve Burp Suite Entegrasyonu](#proxy-ve-burp-suite-entegrasyonu)
- [Çoklu Hedef Taraması ve Boru Hatları (Pipelines)](#-çoklu-hedef-taraması-ve-boru-hatları-pipelines)
- [Gerçek Dünya İş Akışları](#-gerçek-dünya-iş-akışları)
- [Hızlı Referans Tablosu](#-hızlı-referans-tablosu)
- [İpuçları ve En İyi Pratikler](#-i̇puçları-ve-en-i̇yi-pratikler)
- [Yasal Uyarı](#-yasal-uyarı)
- [Kaynaklar](#-kaynaklar)

---

## 🎯 Giriş

**Feroxbuster**, Rust programlama diliyle geliştirilmiş yüksek performanslı, çok iş parçacıklı (multi-threaded) ve özyinelemeli (recursive) bir web içerik tarayıcısıdır. Hızlı, sade ve sezgisel bir kullanım sunmak üzere tasarlanmıştır; Go/Rust tabanlı fuzzer'ların hızını yerel özyinelemeli dizin keşfi kabiliyetiyle birleştirir.

Sızma testlerinde ve bug bounty çalışmalarında, derin web sitesi dizin ağaçlarını ve gizli uç noktaları karmaşık betiklere gerek kalmadan saniyeler içinde ortaya çıkarmak için idealdir.

---

## 🌟 Temel Özellikler

- ⚡ **Son Derece Hızlı:** Asenkron Rust (`tokio` ve `reqwest`) altyapısıyla saniyede binlerce HTTP isteği gerçekleştirebilir.
- 🔄 **Yerleşik Özyinelemeli (Recursive) Keşif:** Keşfedilen yeni dizinleri otomatik olarak algılar ve yapılandırılabilir derinlik sınırlarına kadar alt dizinleri taramaya devam eder.
- 🧠 **Auto-Tune ve Auto-Bail:** Hedef sunucu yavaşladığında istek hızını otomatik olarak düşürür; ardışık bağlantı hataları arttığında taramayı güvenli biçimde durdurur.
- 🔗 **Sayfa İçi Bağlantı Çıkarma (Link Extraction):** Gelen HTML ve JavaScript yanıtlarını ayrıştırarak gizli rotaları yakalar ve otomatik olarak tarama kuyruğuna ekler.
- 📦 **Otomatik Yedek Dosyası Avı:** Bulunan dosyaların `.bak`, `~`, `.old` ve `.orig` gibi yedek kopyalarını arka planda otomatik araştırır.
- 🎯 **Girdi Yönlendirme (Stdin):** `subfinder`, `httpx` veya `gau` gibi araçların çıktılarını doğrudan boru hattı (`stdin`) üzerinden kabul eder.

---

## ⚖️ Araç Karşılaştırması

| Özellik | Feroxbuster | ffuf | Gobuster | Dirb |
|---------|-------------|------|----------|------|
| **Yazıldığı Dil** | Rust | Go | Go | C |
| **Hız** | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐ |
| **Varsayılan Özyineleme** | ✅ Otomatik | ❌ Manuel bayrak | ❌ Yok | ✅ Otomatik |
| **Sayfa İçi Link Çıkarma**| ✅ Var (`--extract-links`) | ❌ Yok | ❌ Yok | ❌ Yok |
| **Otomatik Hız Ayarı** | ✅ Var (`--auto-tune`) | ❌ Yok | ❌ Yok | ❌ Yok |
| **Çoklu Hedef (Stdin)** | ✅ Doğal Destek | ❌ Sınırlı | ❌ Yok | ❌ Yok |
| **Yedek Dosyası Tespiti** | ✅ Var (`--collect-backups`)| ❌ Yok | ❌ Yok | ❌ Yok |

---

## 📥 Kurulum

### Kali Linux & Debian / Ubuntu

```bash
# Paket depolarını güncelleyin ve kurun
sudo apt update && sudo apt install -y feroxbuster
```

### Cargo (Rust Paket Yöneticisi) ile Kurulum

```bash
cargo install feroxbuster
```

### macOS (Homebrew)

```bash
brew install feroxbuster
```

### Docker ile Çalıştırma

```bash
docker run --init -it epi052/feroxbuster -u https://target.com -w /usr/share/seclists/Discovery/Web-Content/common.txt
```

### Derlenmiş İkili (GitHub Releases)

```bash
# En güncel Linux sürümünü kurun
curl -sL https://raw.githubusercontent.com/epi052/feroxbuster/main/install-nix.sh | bash
sudo mv feroxbuster /usr/local/bin/
```

---

## 🚀 Temel Sözdizimi ve Hızlı Başlangıç

```bash
feroxbuster -u <HEDEF_URL> -w <KELIME_LISTESI> [SEÇENEKLER]
```

### Hızlı Tarama Örnekleri

```bash
# Varsayılan ayarlarla tek bir hedefi tarayın
feroxbuster -u http://10.10.10.10

# SecLists dizin listesiyle tarama yapın
feroxbuster -u https://example.com -w /usr/share/seclists/Discovery/Web-Content/directory-list-2.3-medium.txt

# Belirli dosya uzantılarını ekleyerek tarayın
feroxbuster -u https://example.com -w /usr/share/wordlists/dirb/common.txt -x php,html,txt,json
```

---

## ⚙️ Temel Komut Seçenekleri

### Özyineleme (Recursion) Kontrolü

```bash
# Maksimum özyineleme derinliğini belirleyin (varsayılan: 4)
feroxbuster -u https://example.com -w common.txt -d 2

# Özyinelemeyi tamamen kapatın (yalnızca kök dizini tarayın)
feroxbuster -u https://example.com -w common.txt -n

# Belirli alt dizinleri otomatik taramadan hariç tutun
feroxbuster -u https://example.com -w common.txt --dont-scan /api,/static
```

### Eşzamanlılık ve Performans

```bash
# Dizin başına eşzamanlı iş parçacığı (thread) sayısı (varsayılan: 50)
feroxbuster -u https://example.com -t 100

# Aynı anda taranacak maksimum eşzamanlı dizin sayısı
feroxbuster -u https://example.com -L 3

# WAF engellerine takılmamak için saniye başına istek sınırlandırması (rate limit)
feroxbuster -u https://example.com --rate-limit 150

# İstek zaman aşımı süresi (saniye, varsayılan: 7)
feroxbuster -u https://example.com -T 5
```

### Dosya Uzantıları

```bash
# Sözlükteki her kelimeye eklenecek dosya uzantıları
feroxbuster -u https://example.com -x php,asp,aspx,jsp,txt,bak

# Tarama sırasında keşfedilen uzantıları otomatik toplayıp fuzzing yapın
feroxbuster -u https://example.com --collect-extensions
```

### Filtreleme ve Eşleme

```bash
# Yalnızca belirli durum kodlarını sonuçlara dahil edin (örn: 200 OK ve 301 Redirect)
feroxbuster -u https://example.com -s 200 301 302

# Belirli durum kodlarını sonuçlardan hariç tutun (örn: 404 ve 500)
feroxbuster -u https://example.com -C 404 403 500

# Belirli bayt boyutundaki yanıtları filtreleyin
feroxbuster -u https://example.com -S 4092 1024

# Belirli kelime sayısına sahip yanıtları filtreleyin
feroxbuster -u https://example.com -W 42

# Belirli satır sayısına sahip yanıtları filtreleyin
feroxbuster -u https://example.com -N 12

# Düzenli ifadelere (Regex) uyan hata sayfalarını filtreleyin
feroxbuster -u https://example.com --filter-regex "Yapım Aşamasında|Sayfa Bulunamadı"

# Bilinen bir 404 sayfasına benzer tüm yanıtları filtreleyin
feroxbuster -u https://example.com --filter-similar-to https://example.com/olmayan_sayfa_404
```

### Akıllı Özellikler

```bash
# Auto-tune: Sunucu yanıt gecikmesine göre tarama hızını dinamik olarak dengeler
feroxbuster -u https://example.com --auto-tune

# Auto-bail: 100'den fazla ardışık ağ hatası alındığında taramayı güvenle durdurur
feroxbuster -u https://example.com --auto-bail

# Extract links: HTML ve JavaScript dosyalarını ayrıştırıp yeni uç noktaları yakalar
feroxbuster -u https://example.com --extract-links

# Collect backups: Bulunan her dosyanın yedek türevlerini (.bak, ~, .old) otomatik dener
feroxbuster -u https://example.com --collect-backups
```

### Başlıklar, Çerezler ve Kimlik Doğrulama

```bash
# Özel HTTP Başlığı ekleme
feroxbuster -u https://example.com -H "Authorization: Bearer <TOKEN>" -H "X-Forwarded-For: 127.0.0.1"

# Oturum çerezi ile tarama
feroxbuster -u https://example.com -b "session=abc123xyz; role=admin"

# Özel User-Agent dizesi belirleme
feroxbuster -u https://example.com -a "Mozilla/5.0 (Windows NT 10.0; Win64; x64)"

# Geçersiz / kendinden imzalı SSL sertifikalarını yoksayma
feroxbuster -u https://10.10.10.10 -k
```

### Proxy ve Burp Suite Entegrasyonu

```bash
# İstekleri Burp Suite proxy'sine yönlendirin
feroxbuster -u https://example.com -p http://127.0.0.1:8080 -k

# Burp Suite için kısayol parametresi (-k ve localhost:8080 proxy'sini otomatik açar)
feroxbuster -u https://example.com --burp
```

---

## 🔁 Çoklu Hedef Taraması ve Boru Hatları (Pipelines)

Feroxbuster, standart girdi (`stdin`) üzerinden URL listelerini okuyabilir; böylece keşif araçlarıyla doğrudan zincirlenebilir:

```bash
# Boru hattı: Subdomain keşfi -> Canlı sunucu tespiti -> Feroxbuster içerik taraması
subfinder -d example.com -silent | \
  httpx -silent -mc 200,301,302 | \
  feroxbuster --stdin -w /usr/share/seclists/Discovery/Web-Content/raft-medium-directories.txt -t 30 -L 4
```

### Dosyadan URL Okuyarak Tarama

```bash
cat hedefler.txt | feroxbuster --stdin -w common.txt -v
```

---

## 🎬 Gerçek Dünya İş Akışları

### İş Akışı 1: Kapsamlı Bug Bounty Web Keşfi

Yüksek eşzamanlılık, otomatik hız dengeleme, link çıkarma ve yedek tespiti:

```bash
feroxbuster -u https://target.com \
  -w /usr/share/seclists/Discovery/Web-Content/directory-list-2.3-medium.txt \
  -x php,js,json,txt,html \
  --extract-links \
  --collect-backups \
  --auto-tune \
  -o ferox_target_scan.txt
```

### İş Akışı 2: Kurumsal İç Ağ / Active Directory Portalı Keşfi

Burp Suite üzerinden kimlik doğrulamalı ve kendinden imzalı sertifikalı iç portal taraması:

```bash
feroxbuster -u https://internal-portal.corp.local \
  -w /usr/share/seclists/Discovery/Web-Content/common.txt \
  -x aspx,ashx,asmx,config,txt \
  -b "ASP.NET_SessionId=k2m9fl3p8..." \
  --burp \
  -d 3
```

### İş Akışı 3: Hızlı Özyinelemesiz API ve Uç Nokta Taraması

```bash
feroxbuster -u https://api.target.com/v1 \
  -w /usr/share/seclists/Discovery/Web-Content/api/api-endpoints.txt \
  -n \
  -s 200,201,204,401,403 \
  -t 100 \
  --json \
  -o api_results.json
```

---

## 📊 Hızlı Referans Tablosu

| Parametre | Kısa Yol | Açıklama | Örnek |
|-----------|----------|----------|-------|
| `--url` | `-u` | Taranacak hedef URL | `feroxbuster -u https://target.com` |
| `--wordlist` | `-w` | Kelime listesi dosya yolu | `feroxbuster -w /path/to/list.txt` |
| `--threads` | `-t` | Dizin başına eşzamanlı iş parçacığı sayısı | `feroxbuster -t 80` |
| `--scan-limit` | `-L` | Eşzamanlı taranacak maksimum dizin sayısı | `feroxbuster -L 3` |
| `--depth` | `-d` | Maksimum özyineleme derinliği (varsayılan: 4) | `feroxbuster -d 2` |
| `--no-recursion` | `-n` | Özyinelemeli dizin taramasını kapatır | `feroxbuster -n` |
| `--extensions` | `-x` | Kelimelerin sonuna eklenecek dosya uzantıları | `feroxbuster -x php,html,bak` |
| `--status-codes` | `-s` | Dahil edilecek HTTP durum kodları | `feroxbuster -s 200 301 302` |
| `--filter-status` | `-C` | Hariç tutulacak HTTP durum kodları | `feroxbuster -C 404 500` |
| `--filter-size` | `-S` | Hariç tutulacak yanıt boyutları (bayt) | `feroxbuster -S 4096` |
| `--filter-words` | `-W` | Hariç tutulacak kelime sayıları | `feroxbuster -W 15` |
| `--filter-lines` | `-N` | Hariç tutulacak satır sayıları | `feroxbuster -N 8` |
| `--auto-tune` | Yok | Yanıt gecikmesine göre hızı dinamik ayarlar | `feroxbuster --auto-tune` |
| `--auto-bail` | Yok | Ardışık ağ hatalarında taramayı durdurur | `feroxbuster --auto-bail` |
| `--extract-links`| Yok | HTML/JS yanıtlarından yeni uç noktaları çıkarır | `feroxbuster --extract-links` |
| `--collect-backups` | Yok | Dosyaların yedek türevlerini arar (`.bak`, `.old`) | `feroxbuster --collect-backups` |
| `--proxy` | `-p` | İstekleri HTTP proxy üzerinden yönlendirir | `feroxbuster -p http://127.0.0.1:8080` |
| `--burp` | Yok | `-p http://127.0.0.1:8080 -k` için pratik kısayol | `feroxbuster --burp` |
| `--stdin` | Yok | URL listesini standart girdiden okur | `cat urls.txt \| feroxbuster --stdin` |
| `--output` | `-o` | Sonuçların kaydedileceği dosya yolu | `feroxbuster -o results.txt` |
| `--json` | Yok | Çıktıyı JSON biçiminde kaydeder | `feroxbuster --json -o out.json` |

---

## 💡 İpuçları ve En İyi Pratikler

1. **Uzak Sunucularda `--auto-tune` Kullanın:**
   - Rastgele iş parçacığı (`-t`) tahmin etmek yerine `--auto-tune` parametresini açın; Feroxbuster sunucu gecikmesini anlık ölçerek istek hızını otomatik ayarlar ve hedefi kilitlemenizi önler.
2. **`--extract-links` ve `--collect-backups` Kombinasyonu:**
   - Bug bounty hedeflerinde bu ikili son derece etkilidir: JS dosyalarındaki gizli rotaları çözer, bu rotaları tarar ve sunucuda unutulmuş `.bak` veya `~` geliştirici yedeklerini yakalar.
3. **Büyük Listelerde `-L` ile Sınırlandırın:**
   - 200.000+ satırlık listelerde kontrolsüz özyineleme çok fazla alt dizin açarak bellek tüketimini artırabilir. `-L 2` veya `-L 3` ile aynı anda taranan dizin sayısını kontrol altında tutun.
4. **Kesilen Taramaları Sürdürmek İçin State Dosyalarını Kullanın:**
   - Feroxbuster periyodik olarak durum dosyaları (`.state`) kaydeder. Kesilen bir taramayı baştan başlatmak yerine `feroxbuster --resume-from <DOSYA>.state` ile kaldığı yerden sürdürebilirsiniz.

---

## ⚠️ Yasal Uyarı

> **ÖNEMLİ:** Feroxbuster yalnızca yetkili güvenlik testleri, CTF yarışmaları ve yazılı izin alınmış penetrasyon testlerinde kullanılmak üzere tasarlanmıştır. İzin alınmamış sistemlerde içerik taraması yapmak ağ altyapısını zorlayabilir ve yasal olarak suç teşkil eder.

---

## 📚 Kaynaklar

- [Feroxbuster GitHub Deposu](https://github.com/epi052/feroxbuster)
- [Resmi Feroxbuster Dokümantasyonu](https://epi052.github.io/feroxbuster-docs/)
- [SecLists Keşif Listeleri](https://github.com/danielmiessler/SecLists)
