# 🎯 Wfuzz - Kapsamlı Başvuru Kılavuzu

[🇬🇧 English Version](../README.md)

```
  ██╗    ██╗███████╗██╗   ██╗███████╗███████╗
  ██║    ██║██╔════╝██║   ██║╚══███╔╝╚══███╔╝
  ██║ █╗ ██║█████╗  ██║   ██║  ███╔╝   ███╔╝ 
  ██║███╗██║██╔══╝  ██║   ██║ ███╔╝   ███╔╝  
  ╚███╔███╔╝██║     ╚██████╔╝███████╗███████╗
   ╚══╝╚══╝ ╚═╝      ╚═════╝ ╚══════╝╚══════╝
       Esnek Web Uygulaması Fuzzer'ı ve Parametre Keşif Aracı
```

<p align="center">
  <img src="https://img.shields.io/badge/Wfuzz-Web_Fuzzer-blue?style=for-the-badge" alt="Wfuzz">
  <img src="https://img.shields.io/badge/Yazıldığı_Dil-Python-orange?style=for-the-badge" alt="Dil Python">
  <img src="https://img.shields.io/badge/Kategori-Web_Güvenliği_&_API-green?style=for-the-badge" alt="Web Güvenliği & API">
  <img src="https://img.shields.io/badge/Lisans-GPLv2-red?style=for-the-badge" alt="Lisans GPLv2">
</p>

<p align="center">
  <b>🌐 Gizli parametreleri, kimlik doğrulama bypass yöntemlerini, başlıkları ve çoklu yük matrislerini keşfetmek için tasarlanmış gelişmiş HTTP fuzzer</b>
</p>

---

## 📋 İçindekiler

- [Genel Bakış & Anahtar Kelime Mimarisi](#-genel-bakış--anahtar-kelime-mimarisi)
- [Wfuzz vs ffuf Karşılaştırması](#-wfuzz-vs-ffuf-karşılaştırması)
- [Kurulum](#-kurulum)
- [Temel Dizin ve Dosya Keşfi](#-temel-dizin-ve-dosya-keşfi)
- [Filtreleme & Gürültüyü Gizleme (`--hc`, `--hl`, `--hw`, `--hh`)](#-filtreleme--gürültüyü-gizleme---hc---hl---hw---hh)
- [Yük Tipleri & Anlık Kodlayıcılar (`-z`)](#-yük-tipleri--anlık-kodlayıcılar--z)
- [Parametre & Gizli Değişken Keşfi](#-parametre--gizli-değişken-keşfi)
- [Başlıklar, Çerezler ve Temel Kimlik Doğrulama](#-başlıklar-çerezler-ve-temel-kimlik-doğrulama)
- [Çok Değişkenli Fuzzing (`FUZZ` ve `FUZ2Z`)](#-çok-değişkenli-fuzzing-fuzz-ve-fuz2z)
- [Trafiği Burp Suite Üzerinden Geçirme (`-p`)](#-trafiği-burp-suite-üzerinden-geçirme--p)
- [Hızlı Referans Tablosu](#-hızlı-referans-tablosu)
- [Savunma Açısından Değerlendirme & OPSEC](#-savunma-açısından-değerlendirme--opsec)
- [Yasal Uyarı](#-yasal-uyarı)

---

## 🎯 Genel Bakış & Anahtar Kelime Mimarisi

**Xavi Mendez** tarafından Python ile geliştirilen **Wfuzz**, web uygulaması güvenlik testlerinde uzun yıllardır kullanılan klasik ve güvenilir bir fuzzer'dır. URL, parametre, başlık veya POST gövdesi içerisine yerleştirilen özel yer tutucu etiketleri (varsayılan: `FUZZ`) dinamik sözlük girdileriyle veya matematiksel yüklerle değiştirerek istekler gönderir.

Wfuzz'ın en büyük gücü; **dahili yük üreteçleri** (sayısal aralıklar, IP jeneratörleri) ve istek anında çalışan dinamik kodlayıcılarıdır (MD5, Base64, URL encode).

---

## ⚖️ Wfuzz vs ffuf Karşılaştırması

| Özellik | Wfuzz | ffuf |
|---------|-------|------|
| **Yazıldığı Dil** | Python | Go |
| **Ham İstek Hızı** | ⭐⭐⭐ (İyi) | ⭐⭐⭐⭐⭐ (Son Derece Hızlı) |
| **Anlık Yük Kodlayıcılar (Encoders)**| ✅ Doğal Var (`-z file,pass.txt,md5`)| ❌ Harici betik gerektirir |
| **Çoklu Yük Kombinasyonları**| ✅ Doğal Var (`FUZZ`, `FUZ2Z`, `FUZ3Z`)| ✅ Destekler (`-w w1:W1 -w w2:W2`)|
| **Dahili Yük Üreteçleri** | ✅ Sayı aralıkları, IP'ler, Hex listeler | ❌ Yalnızca harici dosyalar |
| **Kaynak Tüketimi** | Orta Düzey Bellek / CPU | Çok Düşük Bellek |

---

## 📥 Kurulum

```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y wfuzz

# Python Pip ile Kurulum
pip3 install wfuzz

# Arch Linux
sudo pacman -S wfuzz
```

---

## 🚀 Temel Dizin ve Dosya Keşfi

```bash
# Temel dizin taraması
wfuzz -w /usr/share/wordlists/dirb/common.txt http://hedef.local/FUZZ

# Dosya uzantısı keşfi
wfuzz -w uzantilar.txt http://hedef.local/index.FUZZ

# Gizli yedek dosyalarını arama
wfuzz -w kelimeler.txt http://hedef.local/FUZZ.bak
```

---

## 🎯 Filtreleme & Gürültüyü Gizleme (`--hc`, `--hl`, `--hw`, `--hh`)

Sunucular var olmayan sayfalara bile özel 404 sayfaları döndürerek çıktıyı kirletebilir:

```bash
# Belirli HTTP durum kodlarını gizleme (404, 403)
wfuzz -w wordlist.txt --hc 404,403 http://hedef.local/FUZZ

# Satır sayısına göre gizleme (statik hata sayfalarını filtreler)
wfuzz -w wordlist.txt --hl 42 http://hedef.local/FUZZ

# Kelime sayısına göre filtreleme
wfuzz -w wordlist.txt --hw 120 http://hedef.local/FUZZ

# Yanıt karakter/boyutuna göre gizleme
wfuzz -w wordlist.txt --hh 0,1052 http://hedef.local/FUZZ

# YALNIZCA HTTP 200 ve 302 yanıtlarını gösterme
wfuzz -w wordlist.txt --sc 200,302 http://hedef.local/FUZZ
```

---

## 📦 Yük Tipleri & Anlık Kodlayıcılar (`-z`)

Wfuzz harici dosya hazırlamaya gerek kalmadan dahili üreteçler sunar:

```bash
# Sayısal Aralık Fuzzing'i (1'den 500'e IDOR testi)
wfuzz -z range,1-500 http://hedef.local/kullanici.php?id=FUZZ

# Komut Satırından Anlık Liste Tanımlama
wfuzz -z list,admin-root-test-dev http://hedef.local/FUZZ/

# Sözlük girdilerini anında MD5 ile hash'leme
wfuzz -z file,parolalar.txt,md5 http://hedef.local/login?hash=FUZZ

# Girdileri anında Base64 ile kodlama
wfuzz -z file,kullanicilar.txt,base64 http://hedef.local/auth?token=FUZZ
```

---

## 🔍 Parametre & Gizli Değişken Keşfi

```bash
# GET parametresi keşfi
wfuzz -w /usr/share/seclists/Discovery/Web-Content/burp-parameter-names.txt \
  --hc 404 --hh 2340 "http://hedef.local/arama.php?FUZZ=test"

# POST gövdesi parametre keşfi
wfuzz -w parametreler.txt -d "FUZZ=admin" --hc 404 http://hedef.local/api/guncelle
```

---

## 🍪 Başlıklar, Çerezler ve Temel Kimlik Doğrulama

```bash
# Çerez (Cookie) Fuzzing
wfuzz -w wordlist.txt -b "session=FUZZ" http://hedef.local/panel

# HTTP Başlığı Fuzzing (IP bypass testi)
wfuzz -z file,ipler.txt -H "X-Forwarded-For: FUZZ" http://hedef.local/admin

# HTTP Basic Auth Kaba Kuvvet Saldırısı
wfuzz -z file,users.txt -z file,passwords.txt --basic "FUZZ:FUZ2Z" http://hedef.local/korumali/
```

---

## ⚡ Çok Değişkenli Fuzzing (`FUZZ` ve `FUZ2Z`)

```bash
# Kullanıcı adı ve parola matrisini aynı anda deneme
wfuzz -w users.txt -w passwords.txt -d "user=FUZZ&pass=FUZ2Z" --hc 200 http://hedef.local/login.php

# Dizin ve uzantıyı birlikte deneme
wfuzz -w kelimeler.txt -w uzantilar.txt http://hedef.local/FUZZ.FUZ2Z
```

---

## 🔁 Trafiği Burp Suite Üzerinden Geçirme (`-p`)

```bash
# İstekleri yerel Burp Suite proxy'sine yönlendirme
wfuzz -p 127.0.0.1:8080:HTTP -w wordlist.txt http://hedef.local/FUZZ
```

---

## ⚡ Hızlı Referans Tablosu

| İşlem | Komut |
|-------|-------|
| **Dizin Fuzzing** | `wfuzz -w <SOZLUK> <URL>/FUZZ` |
| **Durum Kodunu Gizle** | `wfuzz -w <SOZLUK> --hc 404,403 <URL>/FUZZ` |
| **Boyuta Göre Gizle** | `wfuzz -w <SOZLUK> --hh <BOYUT> <URL>/FUZZ` |
| **GET Parametresi Bul** | `wfuzz -w <PARAMETRELER> "<URL>?FUZZ=test"` |
| **POST Parametresi Bul** | `wfuzz -w <PARAMETRELER> -d "FUZZ=test" <URL>` |
| **Sayısal Aralık Yükü** | `wfuzz -z range,1-100 "<URL>?id=FUZZ"` |
| **Anlık Base64 Kodla** | `wfuzz -z file,<SOZLUK>,base64 "<URL>?token=FUZZ"` |
| **Çoklu Değişken Fuzzing** | `wfuzz -w <S1> -w <S2> -d "u=FUZZ&p=FUZ2Z" <URL>` |
| **Burp Proxy'ye Yönlendir** | `wfuzz -p 127.0.0.1:8080:HTTP -w <SOZLUK> <URL>/FUZZ` |

---

## 🛡️ Savunma Açısından Değerlendirme & OPSEC

1. **WAF Engellemeleri:** Saniyede yüzlerce istek atmak Cloudflare ve ModSecurity gibi koruma sistemlerinde bloklanmaya yol açar. İstek aralıklarını yavaşlatmak için `-s <saniye>` bayrağını kullanın.
2. **Sunucu Kaynak Tüketimi:** Ağır veritabanı sorgularını tetikleyen parametreleri kontrolsüz fuzz'lamak sistemlerde geçici hizmet kesintilerine (DDoS) sebep olabilir.

---

## ⚠️ Yasal Uyarı

> [!CAUTION]
> Bu kılavuz yalnızca izinli güvenlik testleri ve zafiyet analizleri için hazırlanmıştır. İzin alınmaksızın web sistemlerine yönelik fuzzing saldırıları gerçekleştirmek yasalara aykırıdır.