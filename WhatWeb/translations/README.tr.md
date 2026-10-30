# 🌐 WhatWeb - Kapsamlı Başvuru Rehberi (Cheatsheet)

[🇺🇸 English Version](../README.md)

```
  ██╗    ██╗██╗  ██╗ █████╗ ████████╗██╗    ██╗███████╗██████╗ 
  ██║    ██║██║  ██║██╔══██╗╚══██╔══╝██║    ██║██╔════╝██╔══██╗
  ██║ █╗ ██║███████║███████║   ██║   ██║ █╗ ██║█████╗  ██████╔╝
  ██║███╗██║██╔══██║██╔══██║   ██║   ██║███╗██║██╔══╝  ██╔══██╗
  ╚███╔███╔╝██║  ██║██║  ██║   ██║   ╚███╔███╔╝███████╗██████╔╝
   ╚══╝╚══╝ ╚═╝  ╚═╝╚═╝  ╚═╝   ╚═╝    ╚══╝╚══╝ ╚══════╝╚═════╝ 
       Yeni Nesil Web Tarayıcısı ve Teknoloji Parmak İzi Çıkarıcı
```

<p align="center">
  <img src="https://img.shields.io/badge/WhatWeb-Web_Parmak_İzi-blue?style=for-the-badge" alt="WhatWeb">
  <img src="https://img.shields.io/badge/Yazar-Andrew_Horton-orange?style=for-the-badge" alt="urbanadventurer">
  <img src="https://img.shields.io/badge/Kategori-Keşif_&_Tarama-green?style=for-the-badge" alt="Keşif & Tarama">
  <img src="https://img.shields.io/badge/Lisans-GPLv2-red?style=for-the-badge" alt="Lisans GPLv2">
</p>

<p align="center">
  <b>🌐 Web uygulamalarında içerik yönetim sistemlerini (CMS), blog platformlarını, JavaScript kütüphanelerini ve sunucu teknolojilerini tespit etmek için tasarlanmış yeni nesil web tarayıcısı</b>
</p>

---

## 📋 İçindekiler

- [Genel Bakış ve Teknoloji Tespiti](#-genel-bakış-ve-teknoloji-tespiti)
- [Kurulum](#-kurulum)
- [Temel Web Sitesi Parmak İzi Çıkarma](#-temel-web-sitesi-parmak-izi-çıkarma)
- [Saldırganlık Düzeyleri (`-a`)](#-saldırganlık-düzeyleri--a)
  - [Gizli Pasif Mod (`-a 1`)](#gizli-pasif-mod--a-1)
  - [Agresif Mod (`-a 3`)](#agresif-mod--a-3)
  - [Kapsamlı / Kaba Kuvvet Modu (`-a 4`)](#kapsamlı--kaba-kuvvet-modu--a-4)
- [Alt Ağları ve Hedef Dosyalarını Tarama](#-alt-ağları-ve-hedef-dosyalarını-tarama)
- [Eklenti (Plugin) Sistemi ve Özel Denetim](#-eklenti-plugin-sistemi-ve-özel-denetim)
- [Çıktı Kaydetme ve Raporlama](#-çıktı-kaydetme-ve-raporlama)
- [Hızlı Referans Tablosu](#-hızlı-referans-tablosu)
- [Savunma Yöntemleri ve OPSEC](#-savunma-yöntemleri-ve-opsec)
- [Yasal Uyarı](#-yasal-uyarı)

---

## 🎯 Genel Bakış ve Teknoloji Tespiti

Bir web uygulamasına karşı exploit çalıştırmadan veya kaba kuvvet fuzzing başlatmadan önce, kullanılan teknoloji yığınını (tech stack) kesin olarak tespit etmek gerekir.

**Andrew Horton (urbanadventurer)** tarafından geliştirilen **WhatWeb**, 1.800'ün üzerinde farklı web teknolojisini tanır:
- **İçerik Yönetim Sistemleri (CMS):** WordPress, Drupal, Joomla, Ghost, Magento.
- **Web Sunucuları ve Proxy'ler:** Apache, Nginx, Microsoft-IIS, Cloudflare, Envoy.
- **Backend Framework'leri:** Laravel, Django, Ruby on Rails, ASP.NET, Spring Boot.
- **Frontend Kütüphaneleri:** React, Vue.js, Angular, jQuery, Bootstrap.
- **Gömülü Cihazlar:** IP kameralar, modemler, yazıcılar, SCADA arayüzleri.

---

## 📥 Kurulum

```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y whatweb

# Arch Linux
sudo pacman -S whatweb

# macOS (Homebrew)
brew install whatweb
```

---

## 🚀 Temel Web Sitesi Parmak İzi Çıkarma

```bash
# Tek bir etki alanını tarama
whatweb https://target.local

# Tek satırlık sade çıktı formatı
whatweb --color=never -q https://target.local

# Kanıt eşleşmelerini gösteren ayrıntılı (verbose) rapor
whatweb -v https://target.local
```

---

## ⚡ Saldırganlık Düzeyleri (`-a`)

WhatWeb, gizlilik ile derinlik arasındaki dengeyi dört saldırganlık seviyesiyle sunar:

### Gizli Pasif Mod (`-a 1`)
Belirtilen URL'ye tek bir HTTP GET isteği gönderir; ek sayfa istemeden dönen yanıt başlıklarını, çerezleri ve HTML etiketlerini inceler:
```bash
whatweb -a 1 https://target.local
```

### Agresif Mod (`-a 3`)
Sızma testleri için önerilen moddur. Yönlendirmeleri takip eder ve yaygın eklenti dosyalarını (örneğin `wp-login.php`, `robots.txt`, favicon hash'leri) sorgular:
```bash
whatweb -a 3 https://target.local
```

### Kapsamlı / Kaba Kuvvet Modu (`-a 4`)
Binlerce bilinen eklenti ve parmak izi URL'sini sorgulayarak en derin analizi yapar:
```bash
whatweb -a 4 https://target.local
```

---

## 🌐 Alt Ağları ve Hedef Dosyalarını Tarama

```bash
# Tüm bir CIDR ağ bloğunu tarama
whatweb 192.168.1.0/24

# Metin dosyasında listelenen hedefleri tarama
whatweb -i targets.txt

# httpx ile keşfedilen canlı adresleri WhatWeb'e borulama (pipe)
cat live_hosts.txt | whatweb --no-errors
```

---

## 🧩 Eklenti (Plugin) Sistemi ve Özel Denetim

WhatWeb zengin bir eklenti mimarisine sahiptir:

```bash
# Mevcut 1.800+ eklentiyi listeleme
whatweb -l

# Belirli eklentileri arama (ör. WordPress veya Apache)
whatweb --grep wordpress

# Hedefe karşı SADECE seçilen eklentileri çalıştırma
whatweb --plugins="apache,php,wordpress" https://target.local
```

---

## 📊 Çıktı Kaydetme ve Raporlama

```bash
# Sonuçları JSON formatında dışa aktarma
whatweb https://target.local --log-json results.json

# XML formatında dışa aktarma
whatweb https://target.local --log-xml results.xml

# Sade metin formatında dışa aktarma
whatweb -i targets.txt --log-brief results.txt
```

---

## ⚡ Hızlı Referans Tablosu

| Amaç | Komut |
|------|-------|
| **Standart Tarama** | `whatweb <URL>` |
| **Ayrıntılı Rapor** | `whatweb -v <URL>` |
| **Gizli Pasif Tarama** | `whatweb -a 1 <URL>` |
| **Agresif Derin Tarama** | `whatweb -a 3 <URL>` |
| **Alt Ağ Tarama (CIDR)** | `whatweb <SUBNET>/24` |
| **Dosyadan Hedef Tarama** | `whatweb -i targets.txt` |
| **JSON Olarak Kaydetme** | `whatweb <URL> --log-json report.json` |
| **Belirli Eklentileri Filtreleme** | `whatweb --plugins="wordpress,nginx" <URL>` |

---

## 🛡️ Savunma Yöntemleri ve OPSEC

1. **Sunucu Başlıklarını Gizleme:** Pasif parmak izi çıkarılmasını engellemek için Apache/Nginx üzerindeki `Server` ve `X-Powered-By` HTTP başlıklarını gizleyin veya rastgele değerlerle değiştirin.
2. **Favicon Hash Koruması:** WhatWeb, `/favicon.ico` dosyasının MD5 hash değerini kullanarak framework'leri tanır. Varsayılan favicon ikonlarını özelleştirin.

---

## ⚠️ Yasal Uyarı

> [!CAUTION]
> WhatWeb yalnızca yetkili güvenlik testleri, zafiyet denetimleri ve sistem envanteri çıkarma amacıyla kullanılmalıdır. İzin alınmamış ağları veya web uygulamalarını taramak yasalara aykırı olabilir.
