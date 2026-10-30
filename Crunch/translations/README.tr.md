# 🔢 Crunch - Kapsamlı Başvuru Kılavuzu

[🇬🇧 English Version](../README.md)

```
   ██████╗██████╗ ██╗   ██╗███╗   ██╗ ██████╗██╗  ██╗
  ██╔════╝██╔══██╗██║   ██║████╗  ██║██╔════╝██║  ██║
  ██║     ██████╔╝██║   ██║██╔██╗ ██║██║     ███████║
  ██║     ██╔══██╗██║   ██║██║╚██╗██║██║     ██╔══██║
  ╚██████╗██║  ██║╚██████╔╝██║ ╚████║╚██████╗██║  ██║
   ╚═════╝╚═╝  ╚═╝ ╚═════╝ ╚═╝  ╚═══╝ ╚═════╝╚═╝  ╚═╝
       Yüksek Hızlı Kalıp ve Karakter Matrisi Tabanlı Parola Listesi Üreteci
```

<p align="center">
  <img src="https://img.shields.io/badge/Crunch-Wordlist_Üreteci-blue?style=for-the-badge" alt="Crunch">
  <img src="https://img.shields.io/badge/Yazıldığı_Dil-C-orange?style=for-the-badge" alt="Dil C">
  <img src="https://img.shields.io/badge/Kategori-Parola_Kırma-green?style=for-the-badge" alt="Parola Kırma">
  <img src="https://img.shields.io/badge/Lisans-GPLv2-red?style=for-the-badge" alt="Lisans GPLv2">
</p>

<p align="center">
  <b>🌐 Özel uzunluklar, karakter setleri ve maske kalıpları kullanarak olası tüm parola permütasyonlarını matematiksel olarak üreten araç</b>
</p>

---

## 📋 İçindekiler

- [Genel Bakış & Permütasyon Motoru](#-genel-bakış--permütasyon-motoru)
- [Kurulum](#-kurulum)
- [Temel Sözdizimi & Karakter Kümeleri](#-temel-sözdizimi--karakter-kümeleri)
- [Kalıp Eşleştirme ve Yer Tutucular (`-t`)](#-kalıp-eşleştirme-ve-yer-tutucular--t)
- [Sistem Karakter Kümelerini Kullanma (`charset.lst`)](#-sistem-karakter-kümelerini-kullanma-charsetlst)
- [Tekrar Eden Karakterleri Sınırlandırma (`-d`)](#-tekrar-eden-karakterleri-sınırlandırma--d)
- [Büyük Sözlükleri Parçalara Bölme (`-b` & `-c`)](#-büyük-sözlükleri-parçalara-bölme--b---c)
- [Diske Yazmadan Doğrudan Araçlara Borulama (Zero Disk Space)](#-diske-yazmadan-doğrudan-araçlara-borulama-zero-disk-space)
- [Hızlı Referans Tablosu](#-hızlı-referans-tablosu)
- [Savunma Açısından Değerlendirme & Hesaplama Uyarıları](#-savunma-açısından-değerlendirme--hesaplama-uyarıları)
- [Yasal Uyarı](#-yasal-uyarı)

---

## 🎯 Genel Bakış & Permütasyon Motoru

Bilinen parola kalıplarına (örneğin telefon numaraları, şirket PIN kodları veya `Parola2026!` gibi kurumsal karmaşıklık kurallarına uyan parolalar) saldırırken, `rockyou.txt` gibi statik sözlükler ya hedefe uymaz ya da verimsiz kalır.

**bofheaded** tarafından C diliyle yazılan **Crunch**, yüksek hızlı bir wordlist üretecidir. Permütasyonları matematiksel olarak anında hesaplayarak tam karakter uzunlukları, şablonlar ve tekrar sınırları belirlemenize imkan tanır.

---

## 📥 Kurulum

```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y crunch

# Arch Linux (AUR)
yay -S crunch
```

---

## 🚀 Temel Sözdizimi & Karakter Kümeleri

Temel sözdizimi; minimum uzunluk, maksimum uzunluk ve isteğe bağlı karakter alfabesi gerektirir:

```bash
# crunch <min> <max> [karakter_kumesi] [secenekler]

# 4 haneli tüm sayısal PIN kombinasyonlarını üretme (0000 - 9999)
crunch 4 4 0123456789 -o 4haneli_pinler.txt

# Yalnızca belirli harflerden oluşan 5 karakterli kelimeler üretme
crunch 5 5 abcdef -o ozel_kelimeler.txt

# 3 ila 4 karakter uzunluğundaki küçük harf kombinasyonlarını üretme
crunch 3 4 abcdefghijklmnopqrstuvwxyz -o kucuk_harfler.txt
```

---

## 🎭 Kalıp Eşleştirme ve Yer Tutucular (`-t`)

`-t` bayrağı bilinen karakterleri sabit tutup, bilinmeyen haneleri özel yer tutucu sembollerle denemenizi sağlar:

| Yer Tutucu | Karakter Türü |
|------------|---------------|
| `@` | Küçük harfler (`a-z`) |
| `,` | Büyük harfler (`A-Z`) |
| `%` | Rakamlar (`0-9`) |
| `^` | Özel karakterler / semboller (`!@#$%^&*...`) |

### Pratik Kalıp Örnekleri:

```bash
# Türk GSM operatörü telefon numaraları üretme (örn. 0532XXXXXXX)
crunch 11 11 -t 0532%%%%%%% -o telefonlar.txt

# Parola politikası şablonu: 1 Büyük harf + 4 Küçük harf + 2 Sayı + 1 Sembol
# (Örn: Admin24! veya Super99#)
crunch 8 8 -t ,@@@@%%^ -o kurumsal_parolalar.txt

# Ön eki bilinen parola: Ilkbahar2026! veya Ilkbahar2026#
crunch 13 13 -t Ilkbahar2026^ -o mevsim_parolalari.txt
```

---

## 📚 Sistem Karakter Kümelerini Kullanma (`charset.lst`)

Crunch sistemde tanımlı hazır karakter matrislerini `/usr/share/crunch/charset.lst` dosyasında barındırır:

```bash
# Hazır alfanümerik karakter kümesini kullanma (mixalpha-numeric)
crunch 6 6 -f /usr/share/crunch/charset.lst mixalpha-numeric -o cikti.txt

# Hexadecimal küçük harf karakterlerini kullanma (0-9, a-f)
crunch 8 8 -f /usr/share/crunch/charset.lst hex-lower -o hex_anahtarlar.txt
```

---

## 🚫 Tekrar Eden Karakterleri Sınırlandırma (`-d`)

Gerçekçi olmayan aşırı ardışık karakter tekrarlarını engellemek için:

```bash
# En fazla 2 ardışık aynı harfe izin ver (örn: 'aaabbb' elenir, 'aab' kabul edilir)
crunch 8 8 abcdefghijklmnopqrstuvwxyz -d 2@ -o filtrelenmis.txt

# Ardışık aynı rakam tekrarını 1 ile sınırla ('11' veya '22' engellenir)
crunch 6 6 -t %%%%%% -d 1% -o tekrarsiz_rakamlar.txt
```

---

## 📦 Büyük Sözlükleri Parçalara Bölme (`-b` & `-c`)

Diski doldurmamak için çıktıyı yönetilebilir dosya boyutlarına bölün:

```bash
# Çıktıyı 500 MB'lık parçalara bölerek kaydetme
crunch 8 8 -f /usr/share/crunch/charset.lst mixalpha-numeric -b 500mb -o START

# Her 1.000.000 satırda bir yeni dosyaya geçme
crunch 8 8 0123456789 -c 1000000 -o START
```

---

## ⚡ Diske Yazmadan Doğrudan Araçlara Borulama (Zero Disk Space)

Devasa wordlist dosyalarını diske kaydedip yer kaplamak yerine, çıktıları doğrudan parola kırıcılara akıtın:

```bash
# 8 haneli PIN'leri diske yazmadan doğrudan Aircrack-ng'ye besleme
crunch 8 8 0123456789 | aircrack-ng -w - -b 00:11:22:33:44:55 hedef_wifi.cap

# Üretilen parolaları doğrudan Hydra SSH kaba kuvvet aracına aktarma
crunch 8 8 -t Admin%%^ | hydra -l admin -P - 10.10.10.50 ssh

# John the Ripper stdin akışına verme
crunch 6 6 0123456789 | john --stdin --format=raw-md5 hashler.txt
```

---

## ⚡ Hızlı Referans Tablosu

| İşlem | Komut |
|-------|-------|
| **Sayısal PIN (4 haneli)** | `crunch 4 4 0123456789 -o pinler.txt` |
| **Kalıp: Büyük+Küçük+Sayı+Özel**| `crunch 8 8 -t ,@@@@%%^ -o parolalar.txt` |
| **Sabit Ön Ekli Kalıp** | `crunch 10 10 -t Sirket%%% -o wordlist.txt` |
| **Hazır Charset Listesi** | `crunch 6 6 -f charset.lst mixalpha-numeric -o cikti.txt` |
| **Ardışık Tekrarı Sınırla** | `crunch 8 8 -t @@@@@@@@ -d 2@ -o cikti.txt` |
| **1 GB'lık Dosyalara Böl** | `crunch 8 8 <HARFLER> -b 1gb -o START` |
| **Aircrack-ng'ye Borula** | `crunch 8 8 0123456789 \| aircrack-ng -w - hedef.cap` |
| **Hydra'ya Borula** | `crunch 7 7 -t User%%% \| hydra -l user -P - <IP> ssh` |

---

## 🛡️ Savunma Açısından Değerlendirme & Hesaplama Uyarıları

> [!WARNING]
> **Permütasyon Patlaması:** Crunch ile üretim yapmadan önce ekranda beliren tahmini dosya boyutunu daima kontrol edin. 8 karakterli sınırsız alfanümerik bir liste diskinizde **200 Terabaytın üzerinde** yer kaplayabilir! Mümkün oldukça boru (pipe) mekanizmasını kullanın.

---

## ⚠️ Yasal Uyarı

> [!CAUTION]
> Crunch yalnızca izinli parola kurtarma operasyonları, sızma testleri ve güvenlik denetimleri için kullanılmalıdır. Yetkisiz sistemlere yönelik kaba kuvvet saldırıları düzenlemek yasal suçtur.