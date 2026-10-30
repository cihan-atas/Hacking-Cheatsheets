# 🪁 Kiterunner - Kapsamlı Başvuru Kılavuzu

[🇬🇧 English Version](../README.md)

```
  ██╗  ██╗██╗████████╗███████╗██████╗ ██╗   ██╗███╗   ██╗███╗   ██╗███████╗██████╗ 
  ██║ ██╔╝██║╚══██╔══╝██╔════╝██╔══██╗██║   ██║████╗  ██║████╗  ██║██╔════╝██╔══██╗
  █████╔╝ ██║   ██║   █████╗  ██████╔╝██║   ██║██╔██╗ ██║██╔██╗ ██║█████╗  ██████╔╝
  ██╔═██╗ ██║   ██║   ██╔══╝  ██╔══██╗██║   ██║██║╚██╗██║██║╚██╗██║██╔══╝  ██╔══██╗
  ██║  ██╗██║   ██║   ███████╗██║  ██║╚██████╔╝██║ ╚████║██║ ╚████║███████╗██║  ██║
  ╚═╝  ╚═╝╚═╝   ╚═╝   ╚══════╝╚═╝  ╚═╝ ╚═════╝ ╚═╝  ╚═══╝╚═╝  ╚═══╝╚══════╝╚═╝  ╚═╝
       Assetnote Tarafından Geliştirilen Bağlam Duyarlı API Uç Noktası Keşif Motoru
```

<p align="center">
  <img src="https://img.shields.io/badge/Kiterunner-API_Keşfi-blue?style=for-the-badge" alt="Kiterunner">
  <img src="https://img.shields.io/badge/Geliştirici-Assetnote-orange?style=for-the-badge" alt="Assetnote">
  <img src="https://img.shields.io/badge/Kategori-Web_Güvenliği_&_API-green?style=for-the-badge" alt="Web Güvenliği & API">
  <img src="https://img.shields.io/badge/Lisans-Apache_2.0-red?style=for-the-badge" alt="Lisans Apache 2.0">
</p>

<p align="center">
  <b>🌐 Modern REST ve GraphQL API mimarilerini bağlamsal veri setleriyle haritalamak için tasarlanmış yüksek hızlı API rota keşif aracı</b>
</p>

---

## 📋 İçindekiler

- [Genel Bakış & API Keşif Problemi](#-genel-bakış--api-keşif-problemi)
- [Araç Karşılaştırması: Kiterunner vs ffuf vs dirsearch](#-araç-karşılaştırması-kiterunner-vs-ffuf-vs-dirsearch)
- [Kurulum](#-kurulum)
- [Assetnote Kite Veri Setlerini İndirme](#-assetnote-kite-veri-setlerini-indirme)
- [API Taraması Gerçekleştirme (`kr scan`)](#-api-taraması-gerçekleştirme-kr-scan)
- [Standart Sözlük ile Kaba Kuvvet (`kr brute`)](#-standart-sözlük-ile-kaba-kuvvet-kr-brute)
- [Başlıklar (Headers), Çerezler ve Kimlik Doğrulama](#-başlıklar-headers-çerezler-ve-kimlik-doğrulama)
- [Filtreleme & Durum Kodları](#-filtreleme--durum-kodları)
- [Keşfedilen Rotaları Tekrar Oynatma (`curl` çıktısı)](#-keşfedilen-rotaları-tekrar-oynatma-curl-çıktısı)
- [Hızlı Referans Tablosu](#-hızlı-referans-tablosu)
- [Savunma Açısından Değerlendirme & OPSEC](#-savunma-açısından-değerlendirme--opsec)
- [Yasal Uyarı](#-yasal-uyarı)

---

## 🎯 Genel Bakış & API Keşif Problemi

Klasik web içerik keşif araçları (`ffuf`, `dirsearch`, `gobuster`), düz ve tek kelimelik sözlükler (`admin`, `api`, `login`) kullanır. Bu yöntem geleneksel dizin yapılarında başarılı olsa da, **modern REST ve GraphQL API'lerinde yetersiz kalır**.

Modern API'ler şu şekilde iç içe geçmiş hiyerarşik rotalara sahiptir:
`/api/v2/organizations/{org_id}/users/export?format=json`

**Assetnote** ekibi tarafından geliştirilen **Kiterunner**, halka açık milyonlarca Swagger, OpenAPI ve Postman şemasını analiz ederek derlediği özel ikili veri setleri (`.kite`) ile bu sorunu çözer. Rastgele kelimeler tahmin etmek yerine, tüm API rotasını doğru HTTP fiilleri (`GET`, `POST`, `PUT`, `DELETE`) ile baştan sona yeniden kurgular.

---

## ⚖️ Araç Karşılaştırması: Kiterunner vs ffuf vs dirsearch

| Özellik | Kiterunner (`kr`) | ffuf | dirsearch |
|---------|-------------------|------|-----------|
| **Temel Odak Noktası** | REST, GraphQL, OpenAPI | Genel Web Fuzzing | Dizin ve Dosya Taraması |
| **Hiyerarşik Rota Algısı**| ✅ Doğal Var (`.kite` dosyaları)| ❌ Düz sözlük | ❌ Düz sözlük |
| **HTTP Metot Değişimi** | ✅ GET, POST, PUT, DELETE dener | ❌ Manuel ayar | ❌ Sınırlı |
| **Swagger / OpenAPI Ayrıştırma**| ✅ Doğal şema okuma | ❌ Yok | ❌ Yok |
| **Hız ve Eşzamanlılık** | ⭐⭐⭐⭐⭐ (Go Motoru) | ⭐⭐⭐⭐⭐ (Go Motoru) | ⭐⭐⭐⭐ (Python) |

---

## 📥 Kurulum

```bash
# GitHub Releases üzerinden derlenmiş ikiliyi indirme
wget https://github.com/assetnote/kiterunner/releases/download/v1.0.2/kiterunner_1.0.2_linux_amd64.tar.gz
tar -xzvf kiterunner_1.0.2_linux_amd64.tar.gz
sudo mv kr /usr/local/bin/

# Sürüm kontrolü
kr version
```

---

## 📦 Assetnote Kite Veri Setlerini İndirme

Kiterunner'ın gücü `.kite` formatındaki optimize rota veritabanından gelir:

```bash
# Wordlists dizini oluşturma
mkdir -p ~/.kiterunner && cd ~/.kiterunner

# Küçük veri seti (~40MB, 2.5 milyon API rotası - Hızlı taramalar için önerilir)
wget https://wordlists-cdn.assetnote.io/data/kiterunner/routes-small.kite.tar.gz
tar -xzvf routes-small.kite.tar.gz

# Büyük veri seti (~130MB, 8.5 milyon API rotası - Kapsamlı testler için)
wget https://wordlists-cdn.assetnote.io/data/kiterunner/routes-large.kite.tar.gz
tar -xzvf routes-large.kite.tar.gz
```

---

## 🚀 API Taraması Gerçekleştirme (`kr scan`)

```bash
# Tek bir hedef API'yi küçük veri seti ile tarama
kr scan https://hedef.local/api -w ~/.kiterunner/routes-small.kite

# Dosyadaki çoklu hedefleri 100 eşzamanlı iş parçacığıyla tarama
kr scan hedefler.txt -w ~/.kiterunner/routes-small.kite -c 100 -j 50

# Detaylı çıktı ile tarama
kr scan https://hedef.local -w ~/.kiterunner/routes-small.kite -v
```

---

## 🔨 Standart Sözlük ile Kaba Kuvvet (`kr brute`)

Düz `.txt` sözlüklerini Kiterunner'ın çoklu HTTP fiil motoruyla çalıştırmak için:

```bash
# Metin sözlüğü ile standart yolları tarama
kr brute https://hedef.local -w /usr/share/wordlists/dirb/common.txt

# Özel API ön eki ekleyerek tarama
kr brute https://hedef.local/api/v1 -w endpoints.txt
```

---

## 🔑 Başlıklar (Headers), Çerezler ve Kimlik Doğrulama

```bash
# Bearer Token ile yetkilendirilmiş API taraması
kr scan https://hedef.local/api -w routes-small.kite -H "Authorization: Bearer eyJhbGci..."

# Özel API anahtarı ve oturum çerezi gönderme
kr scan https://hedef.local/api -w routes-small.kite -H "X-API-Key: secret123" -H "Cookie: session=xyz"
```

---

## 🎯 Filtreleme & Durum Kodları

```bash
# Standart hata kodlarını sonuçlardan gizleme (404, 403, 502)
kr scan https://hedef.local -w routes-small.kite -x 404,403,502

# Yanıt boyutuna göre filtreleme (0 veya 324 baytlık boş yanıtları ele)
kr scan https://hedef.local -w routes-small.kite --filter-length 0,324
```

---

## 🔄 Keşfedilen Rotaları Tekrar Oynatma (`curl` çıktısı)

Bulunan gizli rotaları anında kopyalayıp Burp Suite veya terminalde denemek için `curl` komutları üretin:

```bash
# Başarılı rotaları doğrudan çalıştırılabilir curl komutları olarak dökme
kr scan https://hedef.local -w routes-small.kite -o json | jq -r '.[] | .curl'
```

---

## ⚡ Hızlı Referans Tablosu

| İşlem | Komut |
|-------|-------|
| **Küçük Veri Setiyle API Tara** | `kr scan <URL> -w routes-small.kite` |
| **Büyük Veri Setiyle API Tara** | `kr scan <URL> -w routes-large.kite` |
| **Çoklu Hedefleri Tara** | `kr scan hedefler.txt -w routes-small.kite -c 100` |
| **Metin Sözlüğü ile Tara** | `kr brute <URL> -w wordlist.txt` |
| **Bearer Token Ekle** | `kr scan <URL> -w routes-small.kite -H "Authorization: Bearer <TOKEN>"` |
| **Durum Kodlarını Filtrele** | `kr scan <URL> -w routes-small.kite -x 404,403` |
| **JSON Dosyasına Kaydet** | `kr scan <URL> -w routes-small.kite -o json -oFile sonuclar.json` |

---

## 🛡️ Savunma Açısından Değerlendirme & OPSEC

1. **İstek Sayısı:** `routes-large.kite` veri seti hedefe milyonlarca istek atar. Canlı üretim ortamlarında hız sınırlaması (`-q 200`) tanımlanmalıdır.
2. **Kayıt Değiştirici Metotlar:** Kiterunner `POST`, `PUT` ve `DELETE` istekleri de gönderir. Yetkilendirmesi eksik geliştirme API'lerinde veri tabanındaki kayıtların silinmesine veya değişmesine neden olabilir.

---

## ⚠️ Yasal Uyarı

> [!CAUTION]
> Kiterunner yalnızca yetkili API güvenlik değerlendirmeleri ve bug bounty programları için geliştirilmiştir. İzin alınmaksızın API uç noktalarını taramak suç teşkil eder.