# 🔍 dirsearch - Kapsamlı Başvuru Kılavuzu

[🇺🇸 English Version](../README.md)

```
  ██████╗ ██╗██████╗ ███████╗███████╗ █████╗ ██████╗  ██████╗██╗  ██╗
  ██╔══██╗██║██╔══██╗██╔════╝██╔════╝██╔══██╗██╔══██╗██╔════╝██║  ██║
  ██║  ██║██║██████╔╝███████╗█████╗  ███████║██████╔╝██║     ███████║
  ██║  ██║██║██╔══██╗╚════██║██╔══╝  ██╔══██║██╔══██╗██║     ██╔══██║
  ██████╔╝██║██║  ██║███████║███████╗██║  ██║██║  ██║╚██████╗██║  ██║
  ╚═════╝ ╚═╝╚═╝  ╚═╝╚══════╝╚══════╝╚═╝  ╚═╝╚═╝  ╚═╝ ╚═════╝╚═╝  ╚═╝
       Gelişmiş Web Dizin ve Dosya Kaba Kuvvet (Brute-Force) Aracı
```

<p align="center">
  <img src="https://img.shields.io/badge/dirsearch-Web_Bruteforcer-blue?style=for-the-badge" alt="dirsearch">
  <img src="https://img.shields.io/badge/Dil-Python_Async-green?style=for-the-badge" alt="Python Async">
  <img src="https://img.shields.io/badge/Keşif-Web_Dizin_Tarama-orange?style=for-the-badge" alt="Web Dizin Tarama">
  <img src="https://img.shields.io/badge/Lisans-GPLv2-red?style=for-the-badge" alt="Lisans GPLv2">
</p>

<p align="center">
  <b>🌐 Akıllı uzantı yönetimi, özyinelemeli tarama ve sayfa içi tarayıcı (crawler) desteği sunan gelişmiş web yol keşif kılavuzu</b>
</p>

---

## 📋 İçindekiler

- [Giriş](#-giriş)
- [Araç Karşılaştırması](#-araç-karşılaştırması)
- [Kurulum](#-kurulum)
- [Temel Kullanım ve Hedef Belirleme](#-temel-kullanım-ve-hedef-belirleme)
- [Uzantı (Extension) ve Sonek Yönetimi](#-uzantı-extension-ve-sonek-yönetimi)
- [Kelime Listeleri (Wordlists) ve Sözlükler](#-kelime-listeleri-wordlists-ve-sözlükler)
- [Özyineleme (Recursion) ve Web Crawling](#-özyineleme-recursion-ve-web-crawling)
- [İş Parçacığı (Threads), Hız Sınırı ve Gecikmeler](#-i̇ş-parçacığı-threads-hız-sınırı-ve-gecikmeler)
- [Yanıtları Filtreleme ve Eşleştirme](#-yanıtları-filtreleme-ve-eşleştirme)
- [HTTP İsteği Özelleştirme](#-http-i̇steği-özelleştirme)
  - [Özel Başlıklar (Headers) ve User-Agent](#özel-başlıklar-headers-ve-user-agent)
  - [Çerezler (Cookies) ve Kimlik Doğrulama](#çerezler-cookies-ve-kimlik-doğrulama)
  - [POST Verisi ve İstek Metotları](#post-verisi-ve-i̇stek-metotları)
- [Proxy, Tor ve WAF Atlatma](#-proxy-tor-ve-waf-atlatma)
- [Çıktı Formatları ve Raporlama](#-çıktı-formatları-ve-raporlama)
- [Pratik Pentest ve Bug Bounty Senaryoları](#-pratik-pentest-ve-bug-bounty-senaryoları)
- [Hızlı Başvuru Tablosu](#-hızlı-başvuru-tablosu)
- [İpuçları ve En İyi Pratikler](#-i̇puçları-ve-en-i̇yi-pratikler)
- [Yasal Uyarı](#-yasal-uyarı)

---

## 🎯 Giriş

**dirsearch** (Mauro Soria tarafından geliştirilmiştir), web sunucularında gizli kalmış dosya ve dizinleri tespit etmek için kullanılan en popüler komut satırı araçlarından biridir. Python ile yazılmış olan bu araç; renkli terminal arayüzü, otomatik özyinelemeli (recursive) derinlemesine tarama, joker karakterli (`*`) uzantı tamamlama ve esnek filtreleme yetenekleriyle öne çıkar.

Sızma testlerinde ve bug bounty araştırmalarında unutulmuş yedek dosyalarını (`.bak`, `.old`), yönetim panellerini (`/admin`), gizli API rotalarını ve veritabanı dökümlerini bulmak için standart bir keşif aracıdır.

### Temel Özellikler

| Özellik | Açıklama |
|---------|----------|
| **Yüksek Eşzamanlılık** | Asenkron ve çok iş parçacıklı (multi-threaded) istek altyapısı |
| **Joker Uzantı Desteği**| `-e *` parametresiyle bilinen tüm web uzantılarını otomatik dener |
| **Dahili Web Crawler** | Keşfedilen HTML/JS dosyalarındaki bağlantıları ayrıştırıp tarama sırasına ekler |
| **Gelişmiş Filtreleme** | Boyut, kelime sayısı, satır sayısı, durum kodu ve regex ile filtreleme |
| **Ham İstek Desteği** | Burp Suite'ten kaydedilen HTTP isteklerini (`--raw`) doğrudan oynatabilir |
| **Çoklu Rapor Formatı**| Sonuçları TXT, JSON, CSV, Markdown ve HTML formatında dışa aktarır |

---

## ⚖️ Araç Karşılaştırması

| Özellik | dirsearch | ffuf | Feroxbuster | Gobuster | DIRB |
|---------|-----------|------|-------------|----------|------|
| **Yazıldığı Dil** | Python | Go | Rust | Go | C |
| **Hız** | ⭐⭐⭐⭐ (Hızlı) | ⭐⭐⭐⭐⭐ (Son derece hızlı) | ⭐⭐⭐⭐⭐ (Olağanüstü hızlı) | ⭐⭐⭐⭐ (Hızlı) | ⭐⭐ (Düşük thread) |
| **Varsayılan Özyineleme**| ✅ Otomatik (`-r`) | İsteğe bağlı (`-recursion`) | ✅ Otomatik (`-d`) | İsteğe bağlı (`-r`) | ✅ Otomatik |
| **Joker Uzantı (`-e *`)** | ✅ Doğal Destek | ❌ Yok | ❌ Yok | ❌ Yok | ❌ Yok |
| **Dahili Web Crawler** | ✅ Var (`--crawl`) | ❌ Yok | ✅ Var (`--extract-links`) | ❌ Yok | ❌ Yok |
| **POST / Veri Fuzzing** | ✅ Var (`--data`) | ✅ Tam HTTP | ❌ Sınırlı | ❌ Yok | ❌ Yok |
| **Burp Ham İstek Dosyası**| ✅ Var (`--raw`) | ✅ Var (`-request`) | ❌ Yok | ❌ Yok | ❌ Yok |
| **Dahili Kelime Listesi** | ✅ Zengin (`db/dicc.txt`)| ❌ Yok | ❌ Yok | ❌ Yok | ✅ Küçük liste |

---

## 📥 Kurulum

### pipx ile Kurulum (Kali & Modern Linux İçin Önerilen)

```bash
# pipx ile izole Python ortamında kurulum
pipx install dirsearch

# Kurulumu doğrulayın
dirsearch --version
```

### pip ile Kurulum

```bash
# Standart pip ile kurulum
pip3 install dirsearch
```

### Kaynak Koddan Kurulum (Git Clone)

```bash
# Depoyu klonlayın
git clone https://github.com/maurosoria/dirsearch.git --depth 1
cd dirsearch

# Bağımlılıkları yükleyin ve çalıştırın
pip3 install -r requirements.txt
python3 dirsearch.py --version
```

### Kali Linux Paket Yöneticisi ve Docker

```bash
# Kali Linux deposundan doğrudan kurma
sudo apt update && sudo apt install -y dirsearch

# Docker imajı üzerinden çalıştırma
docker run -it --rm maurosoria/dirsearch -u https://example.com -e php,html
```

---

## 🚀 Temel Kullanım ve Hedef Belirleme

### Tek Hedef Tarama

```bash
# Belirli dosya uzantılarıyla tek bir adresi tarama
dirsearch -u https://hedef.com -e php,html,js,txt

# Yolların sonuna otomatik eğik çizgi (/) eklemeyi zorunlu kılma
dirsearch -u https://hedef.com -e php -f
```

### Çoklu Hedef Tarama

```bash
# Dosyadaki tüm URL adreslerini sırayla tarama
dirsearch -l hedefler.txt -e php,aspx,txt

# subfinder / httpx çıktısını boru hattıyla (stdin) doğrudan besleme
cat aktif_hostlar.txt | dirsearch --stdin -e php
```

### Port ve CIDR Tabanlı Tarama

```bash
# Belirtilen portlarda tarama yapma
dirsearch -u https://hedef.com --ports 80,443,8080,8443 -e php

# Bir IP bloğunun tamamını (CIDR) tarama
dirsearch --cidr 192.168.1.0/24 -e html
```

---

## 🧩 Uzantı (Extension) ve Sonek Yönetimi

`dirsearch`, uzantı kombinasyonlarında en esnek araçlardan biridir:

```bash
# Birden fazla uzantı tanımlama
dirsearch -u https://hedef.com -e php,html,json,txt,zip

# Joker karakter (*) ile bilinen TÜM uzantıları otomatik deneme
dirsearch -u https://hedef.com -e *

# Kelime listesindeki sözcüklerin başına önek ekleme (test_admin, dev_admin)
dirsearch -u https://hedef.com -e php --prefixes test_,dev_,staging_,old_

# Kelime listesindeki sözcüklerin sonuna sonek ekleme (admin_bak, admin_old)
dirsearch -u https://hedef.com -e php --suffixes _bak,~,_2026,_old
```

---

## 📚 Kelime Listeleri (Wordlists) ve Sözlükler

`dirsearch`, kurulumuyla birlikte gelen kapsamlı bir kelime listesi içerir (`db/dicc.txt`):

```bash
# Dahili kelime listesiyle doğrudan çalıştırma
dirsearch -u https://hedef.com -e php

# SecLists içerisinden özel bir liste kullanma
dirsearch -u https://hedef.com -e php -w /usr/share/seclists/Discovery/Web-Content/directory-list-2.3-medium.txt

# Birden çok kelime listesini birleştirerek kullanma
dirsearch -u https://hedef.com -e php -w liste1.txt,liste2.txt

# Kelimelerin tamamını küçük veya büyük harfe dönüştürerek deneme
dirsearch -u https://hedef.com -e php --lowercase
dirsearch -u https://hedef.com -e php --uppercase
```

---

## 🔄 Özyineleme (Recursion) ve Web Crawling

### Özyinelemeli (Recursive) Tarama

```bash
# Bulunan yeni dizinlerin içine otomatik olarak girip tarama
dirsearch -u https://hedef.com -e php -r

# Özyineleme derinliğini sınırlandırma (Maksimum 2 katman derinlik)
dirsearch -u https://hedef.com -e php -r --recursion-depth 2

# Yalnızca belirli durum kodları (200, 301, 302) dönen dizinlerde derinleşme
dirsearch -u https://hedef.com -e php -r --recursion-status 200,301,302
```

### Dahili Sayfa İçi Tarayıcı (Crawler)

```bash
# Hedef web sitesinin sayfalarını tarayıp bulduğu bağlantıları tarama kuyruğuna ekler
dirsearch -u https://hedef.com -e php --crawl
```

---

## ⚡ İş Parçacığı (Threads), Hız Sınırı ve Gecikmeler

WAF veya hız sınırlamalarını (rate-limiting) tetiklememek için tarama hızını ayarlayabilirsiniz:

```bash
# Eşzamanlı thread sayısını ayarlama (Varsayılan: 25)
dirsearch -u https://hedef.com -e php -t 50

# İstekler arasına saniye cinsinden gecikme ekleme (Örn: 0.5 saniye)
dirsearch -u https://hedef.com -e php -s 0.5

# Saniye başına maksimum istek sayısını sınırlama (Örn: En fazla 15 istek/sn)
dirsearch -u https://hedef.com -e php --max-rate 15

# Zaman aşımı (timeout) ve yeniden deneme (retries) sürelerini belirleme
dirsearch -u https://hedef.com -e php --timeout 8 --retries 3

# Aşırı bağlantı hatası oluşursa taramayı otomatik durdurma
dirsearch -u https://hedef.com -e php --exit-on-error
```

---

## 🎯 Yanıtları Filtreleme ve Eşleştirme

### Durum Kodu Dahil Etme ve Hariç Tutma

```bash
# Yalnızca belirli durum kodlarını listeleme (Diğerlerini görmezden gelir)
dirsearch -u https://hedef.com -e php -i 200,301,302,403

# İstenmeyen durum kodlarını gizleme (Örn: 404, 500 hatalarını ele)
dirsearch -u https://hedef.com -e php -x 404,400,500,502,503
```

### Boyut, Metin ve Regex ile Filtreleme

```bash
# Belirli yanıt boyutlarını gizleme (Yanıltıcı özel 404 sayfalarını filtrelemek için)
dirsearch -u https://hedef.com -e php --exclude-sizes 0B,1542B,3421B

# Sayfa gövdesinde belirli bir metin geçen yanıtları hariç tutma
dirsearch -u https://hedef.com -e php --exclude-texts "Not Found","Access Denied"

# Düzenli ifadeye (Regex) uyan yanıtları hariç tutma
dirsearch -u https://hedef.com -e php --exclude-regex "^Error:.*"
```

---

## 🛠️ HTTP İsteği Özelleştirme

### Özel Başlıklar (Headers) ve User-Agent

```bash
# İsteğe özel başlıklar ekleme (Yetkilendirme token'ı veya WAF atlatma başlığı)
dirsearch -u https://hedef.com -e php -H "X-Forwarded-For: 127.0.0.1" -H "X-Requested-With: XMLHttpRequest"

# Başlıkları doğrudan bir dosyadan yükleme
dirsearch -u https://hedef.com -e php --header-list ozel_basliklar.txt

# Her istekte rastgele User-Agent kullanarak WAF tespitini zorlaştırma
dirsearch -u https://hedef.com -e php --random-agent

# Sabit bir User-Agent belirleme
dirsearch -u https://hedef.com -e php --user-agent "Mozilla/5.0 (Windows NT 10.0; Win64; x64)"
```

### Çerezler (Cookies) ve Kimlik Doğrulama

```bash
# Giriş yapılmış oturum çerezleriyle (Authenticated) tarama yapma
dirsearch -u https://hedef.com -e php -b "PHPSESSID=abc123xyz; admin_logged=true"

# HTTP Basic Authentication ile korunan alanları tarama
dirsearch -u https://hedef.com -e php --auth admin:SecretPass123 --auth-type basic

# Kurumsal Windows portalları için NTLM kimlik doğrulama
dirsearch -u https://hedef.com -e php --auth CORP\\alice:Welcome2026 --auth-type ntlm
```

### POST Verisi ve İstek Metotları

```bash
# HTTP POST metodu kullanarak veri fuzzing işlemi gerçekleştirme
dirsearch -u https://hedef.com/api -m POST -d "action=test&token=xyz" -e json

# Burp Suite'ten kaydedilmiş ham (raw) HTTP isteğini oynatma
dirsearch -u https://hedef.com --raw istek.txt -e php
```

---

## 🛡️ Proxy, Tor ve WAF Atlatma

```bash
# Tüm trafiği HTTP veya SOCKS5 vekil sunucusu üzerinden (Örn: Burp Suite) geçirme
dirsearch -u https://hedef.com -e php --proxy http://127.0.0.1:8080

# Tüm trafiği Tor ağı üzerinden anonim olarak yönlendirme
dirsearch -u https://hedef.com -e php --tor

# Geçersiz SSL/TLS sertifikalarını yoksayma
dirsearch -u https://hedef.com -e php --no-check-certificate
```

---

## 📄 Çıktı Formatları ve Raporlama

```bash
# Sonuçları temiz bir metin (txt) dosyasına kaydetme
dirsearch -u https://hedef.com -e php -o sonuclar.txt

# Sonuçları JSON formatında dışa aktarma (Otomasyon için ideal)
dirsearch -u https://hedef.com -e php -o sonuclar.json --format json

# HTML, CSV veya Markdown formatında rapor oluşturma
dirsearch -u https://hedef.com -e php -o rapor.html --format html
dirsearch -u https://hedef.com -e php -o sonuclar.csv --format csv
dirsearch -u https://hedef.com -e php -o rapor.md --format markdown
```

---

## 🎯 Pratik Pentest ve Bug Bounty Senaryoları

### 1. Hızlı ve Kapsamlı Web Keşfi

```bash
# Yaygın uzantılar, rastgele User-Agent ve 40 thread ile tam tarama
dirsearch -u https://hedef.com \
  -e php,html,js,json,txt,zip \
  -t 40 \
  --random-agent \
  -i 200,301,302,403 \
  -o hedef_kesif.txt
```

### 2. Unutulmuş Yedek ve Kaynak Kodu Sızıntısı Avı

```bash
# Veritabanı dökümleri, git depoları, env ve zip arşivlerini arama
dirsearch -u https://hedef.com \
  -e bak,old,orig,zip,tar.gz,sql,env,git \
  -w /usr/share/seclists/Discovery/Web-Content/raft-medium-files.txt \
  -i 200 \
  --exclude-sizes 0B
```

### 3. Derinlemesine Özyinelemeli ve Crawler Destekli Tarama

```bash
# 2 katman derinlik, sayfa içi crawler ve hata kodlarını filtreleme
dirsearch -u https://hedef.com \
  -e php,html \
  -r --recursion-depth 2 \
  --crawl \
  -x 404,500,502 \
  -o derin_tarama.json --format json
```

### 4. Kimlik Doğrulamalı Kurumsal Portal Denetimi

```bash
# Burp Suite üzerinden oturum çerezi ve WAF atlatma başlıklarıyla tarama
dirsearch -u https://intranet.sirket.local \
  -e aspx,ashx,config,txt \
  -b "ASP.NET_SessionId=xyz123; AuthToken=ey..." \
  -H "X-Forwarded-For: 127.0.0.1" \
  --proxy http://127.0.0.1:8080
```

---

## ⚡ Hızlı Başvuru Tablosu

| İşlem | Komut |
|-------|-------|
| **Uzantılarla temel tarama** | `dirsearch -u https://hedef.com -e php,html,txt` |
| **Tüm uzantılar (Joker `*`)**| `dirsearch -u https://hedef.com -e *` |
| **Özel kelime listesi** | `dirsearch -u https://hedef.com -e php -w /yol/liste.txt` |
| **Çoklu hedef tarama** | `dirsearch -l hedefler.txt -e php,aspx` |
| **Özyinelemeli tarama** | `dirsearch -u https://hedef.com -e php -r --recursion-depth 2` |
| **Sayfa içi crawler ile tarama**| `dirsearch -u https://hedef.com -e php --crawl` |
| **Durum kodlarını filtreleme**| `dirsearch -u https://hedef.com -e php -i 200,301,302,403` |
| **Hatalı kodları gizleme** | `dirsearch -u https://hedef.com -e php -x 404,500,502` |
| **Yanıltıcı boyutları eleme** | `dirsearch -u https://hedef.com -e php --exclude-sizes 0B,1240B` |
| **Çerezle oturum açarak tarama**| `dirsearch -u https://hedef.com -e php -b "session=abc123"` |
| **Burp Suite proxy'sine bağlama**| `dirsearch -u https://hedef.com -e php --proxy http://127.0.0.1:8080` |
| **JSON formatında kaydetme** | `dirsearch -u https://hedef.com -e php -o rapor.json --format json` |

---

## 💡 İpuçları ve En İyi Pratikler

1. **Büyük Listelerde `-e *` Parametresini Dikkatli Kullanın:** Joker uzantı, listedeki her kelimeyi onlarca uzantıyla çarpar. Büyük listelerde taramayı yavaşlatabileceği için `common.txt` veya `dicc.txt` gibi odaklı sözlüklerle kullanılması önerilir.
2. **Yanıltıcı 200 OK Boyutlarını Mutlaka Ele:** Birçok modern web çatısı bulunamayan sayfalar için HTTP 200 kodlu özel 404 şablonu döner. Sayfa boyutunu tespit edip `--exclude-sizes` ile filtrelemek gereksiz gürültüyü tamamen yok eder.
3. **403 Forbidden Yanıtlarını Not Alın:** 403 yanıtı, aranan dosya veya dizinin gerçekten var olduğunu ancak yetkilendirme gerektirdiğini gösterir. Bu yollar başlık manipülasyonu (`X-Forwarded-For`), path traversal ve bypass testleri için birincil hedeftir.
4. **Önek ve Soneklerle Gizli Yedekleri Yakalayın:** Yazılımcılar konfigürasyon ve veritabanı dosyalarını sık sık `config.php.bak`, `database_old.sql` gibi adlarla kaydeder. `--prefixes` ve `--suffixes` parametreleri bu sızıntıları yakalamak için idealdir.

---

## ⚠️ Yasal Uyarı

> **ÖNEMLİ:** Yazılı izin ve yetkilendirme olmaksızın web sunucularında dizin taraması ve kaba kuvvet saldırıları yapmak yasalara aykırıdır ve adli yaptırımlara tabidir. `dirsearch` aracını yalnızca yasal çerçevesi çizilmiş sızma testlerinde, ödül avcılığı (bug bounty) kapsamlarında ve laboratuvar ortamlarında kullanınız.
