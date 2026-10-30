# 🕷️ CeWL - Kapsamlı Başvuru Kılavuzu

[🇬🇧 English Version](../README.md)

```
   ██████╗███████╗██╗    ██╗██╗     
  ██╔════╝██╔════╝██║    ██║██║     
  ██║     █████╗  ██║ █╗ ██║██║     
  ██║     ██╔══╝  ██║███╗██║██║     
  ╚██████╗███████╗╚███╔███╔╝███████╗
   ╚═════╝╚══════╝ ╚══╝╚══╝ ╚══════╝
       Robin Wood (DigiNinja) Tarafından Geliştirilen Özel Parola Sözlüğü Üreteci
```

<p align="center">
  <img src="https://img.shields.io/badge/CeWL-Özel_Wordlist_Üreteci-blue?style=for-the-badge" alt="CeWL">
  <img src="https://img.shields.io/badge/Geliştirici-DigiNinja-orange?style=for-the-badge" alt="DigiNinja">
  <img src="https://img.shields.io/badge/Kategori-Parola_Kırma-green?style=for-the-badge" alt="Parola Kırma">
  <img src="https://img.shields.io/badge/Lisans-GPLv2-red?style=for-the-badge" alt="Lisans GPLv2">
</p>

<p align="center">
  <b>🌐 Hedef web sitelerini örümcek gibi tarayarak kurumsal terimleri, marka isimlerini ve e-postaları toplayıp hedefe özel parola sözlükleri oluşturan araç</b>
</p>

---

## 📋 İçindekiler

- [Genel Bakış & Hedefe Özel Parola Felsefesi](#-genel-bakış--hedefe-özel-parola-felsefesi)
- [Kurulum](#-kurulum)
- [Temel Web Sitesi Taraması](#-temel-web-sitesi-taraması)
- [Tarama Derinliği ve Kelime Boyutu Denetimi](#-tarama-derinliği-ve-kelime-boyutu-denetimi)
- [E-Posta Adreslerini Toplama (`-e`)](#-e-posta-adreslerini-toplama--e)
- [Sayıları Dahil Etme ve Küçük Harfe Dönüştürme](#-sayıları-dahil-etme-ve-küçük-harfe-dönüştürme)
- [Belge Üstveri (Metadata) Analizi (`-a`)](#-belge-üstveri-metadata-analizi--a)
- [Kelime Sıklığı ve Frekans Analizi (`-c`)](#-kelime-sıklığı-ve-frekans-analizi--c)
- [CeWL Sözlüklerini Hashcat ve John Kurallarıyla Genişletme](#-cewl-sözlüklerini-hashcat-ve-john-kurallarıyla-genişletme)
- [Hızlı Referans Tablosu](#-hızlı-referans-tablosu)
- [Savunma Açısından Değerlendirme & OPSEC](#-savunma-açısından-değerlendirme--opsec)
- [Yasal Uyarı](#-yasal-uyarı)

---

## 🎯 Genel Bakış & Hedefe Özel Parola Felsefesi

`rockyou.txt` gibi genel parola sözlükleri standart kullanıcı parolalarında etkili olsa da, **en az 12-16 karakter zorunluluğu olan kurumsal parola politikalarında genellikle yetersiz kalır**.

Çalışanlar kurumsal parolalarını belirlerken sıklıkla şunlardan ilham alır:
- Şirketin proje adları, iç kod adları ve sloganları (`Anka2026!`).
- Sektöre özgü teknik terimler, ürün markaları ve hizmet adları.
- Yönetici isimleri, şirket yerleşkesinin bulunduğu semt/şehir adları.

**Robin Wood (DigiNinja)** tarafından geliştirilen **CeWL**, hedef web sitesini belirlenen derinliğe kadar tarayan bir Ruby örümceğidir. Sayfalardaki benzersiz kelimeleri ayıklar ve kuruma özel nokta atışı parola sözlükleri üretir.

---

## 📥 Kurulum

```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y cewl

# Arch Linux (AUR)
yay -S cewl

# Ruby Gem ile
gem install cewl
```

---

## 🚀 Temel Web Sitesi Taraması

```bash
# Web sitesini tara ve kelimeleri ekrana bas
cewl https://hedef.local

# Taramayı dosyaya kaydetme
cewl https://hedef.local -w sirket_wordlist.txt
```

---

## 🕸️ Tarama Derinliği ve Kelime Boyutu Denetimi

```bash
# 2 bağlantı derinliğine in (-d 2) ve en az 8 karakterli kelimeleri al (-m 8)
cewl https://hedef.local -d 2 -m 8 -w wordlist_min8.txt

# Derin ve kapsamlı bir kurumsal sözlük için 3 seviye derinlik
cewl https://hedef.local -d 3 -m 7 -w derin_tarama.txt
```

---

## 📧 E-Posta Adreslerini Toplama (`-e`)

Kimlik avı (phishing) veya parola püskürtme (password spraying) testleri için kurumsal e-postaları ayıklayın:

```bash
# E-postaları ayrı bir dosyaya kaydetme
cewl https://hedef.local -e --email_file epostalar.txt -w kelimeler.txt
```

---

## 🔡 Sayıları Dahil Etme ve Küçük Harfe Dönüştürme

```bash
# Alfanümerik kelimeleri de dahil etme (V300, X86 gibi ürün kodları)
cewl https://hedef.local --with-numbers -m 6 -w kelimeler_ve_sayilar.txt

# Tüm kelimeleri küçük harfe çevirme (Hashcat kurallarına beslemek için idealdir)
cewl https://hedef.local --lowercase -w kucuk_harf_kelimeler.txt
```

---

## 📄 Belge Üstveri (Metadata) Analizi (`-a`)

Sitedeki PDF ve Office belgelerini indirerek yazar/kullanıcı adlarını çıkarma:

```bash
# Belge üstverilerini çekerek çalışan kullanıcı adlarını bulma
cewl https://hedef.local -a --meta_file yazarlar.txt
```

---

## 📊 Kelime Sıklığı ve Frekans Analizi (`-c`)

Şirket içinde en çok tekrarlanan anahtar kelimeleri tespit etmek için frekans sayacını açın:

```bash
# Her kelimenin kaç kez geçtiğini yanına yaz
cewl https://hedef.local -c -m 6 -w frekans.txt

# En çok geçen kelimeleri sıralama
sort -k2 -nr frekans.txt | head -n 30
```

---

## ⚡ CeWL Sözlüklerini Hashcat ve John Kurallarıyla Genişletme

CeWL'in ürettiği ham kelimeler kök sözcüklerdir. Bunları çalışanların tipik parola kalıplarına dönüştürmek için **Hashcat kurallarını (rules)** kullanın:

```bash
# 1. Şirket sitesinden kök kelimeleri toplayın
cewl https://hedef.local -m 5 --lowercase -w kok_kelimeler.txt

# 2. Hashcat best64 kural setiyle kelimeleri türetin
hashcat --force kok_kelimeler.txt -r /usr/share/hashcat/rules/best64.rule --stdout | sort -u > turetilen_parolalar.txt

# Üretilen örnek parolalar:
# Sirket -> Sirket2026!, !Sirket123, 51rk3t, Sirket#1
```

---

## ⚡ Hızlı Referans Tablosu

| İşlem | Komut |
|-------|-------|
| **Temel Tarama ve Kaydetme** | `cewl <URL> -w wordlist.txt` |
| **Minimum Uzunluk & Derinlik** | `cewl <URL> -m 8 -d 2 -w wordlist.txt` |
| **Kurumsal E-Postaları Topla**| `cewl <URL> -e --email_file epostalar.txt` |
| **Belge Üstverilerini Çek** | `cewl <URL> -a --meta_file yazarlar.txt` |
| **Sayı İçeren Kelimeleri Al** | `cewl <URL> --with-numbers -w wordlist.txt` |
| **Küçük Harfe Dönüştür** | `cewl <URL> --lowercase -w wordlist.txt` |
| **Frekans Sayacını Aç** | `cewl <URL> -c -w frekans.txt` |
| **Oturum Açarak Tara (Cookie)**| `cewl <URL> -a "Cookie: session=xyz"` |

---

## 🛡️ Savunma Açısından Değerlendirme & OPSEC

1. **Erişim Logları:** CeWL kısa sürede yüzlerce HTTP GET isteği üretir. Standart User-Agent yerine gerçekçi bir tarayıcı kimliği (`-u "Mozilla/5.0..."`) tanımlanmalıdır.
2. **Tuzak Linkler (Honeyword):** Savunucuların siteye gizlediği tuzak bağlantılar web örümceklerini anında tespit edip SOC ekiplerine bildirebilir.

---

## ⚠️ Yasal Uyarı

> [!CAUTION]
> CeWL yalnızca izinli güvenlik testleri ve parola denetimleri için kullanılmalıdır. İzin alınmaksızın web sitelerini taramak ve veri kazımak suç teşkil edebilir.