# 📦 smuggler - Kapsamlı Başvuru Rehberi (Cheatsheet)

[🇺🇸 English Version](../README.md)

```
  ███████╗███╗   ███╗██╗   ██╗ ██████╗  ██████╗ ██╗     ███████╗██████╗ 
  ██╔════╝████╗ ████║██║   ██║██╔════╝ ██╔════╝ ██║     ██╔════╝██╔══██╗
  ███████╗██╔████╔██║██║   ██║██║  ███╗██║  ███╗██║     █████╗  ██████╔╝
  ╚════██║██║╚██╔╝██║██║   ██║██║   ██║██║   ██║██║     ██╔══╝  ██╔══██╗
  ███████║██║ ╚═╝ ██║╚██████╔╝╚██████╔╝╚██████╔╝███████╗███████╗██║  ██║
  ╚══════╝╚═╝     ╚═╝ ╚═════╝  ╚═════╝  ╚═════╝ ╚══════╝╚══════╝╚═╝  ╚═╝
             Yüksek Performanslı HTTP İstek Kaçakçılığı Tarayıcısı
```

<p align="center">
  <img src="https://img.shields.io/badge/smuggler-HTTP_Smuggling-blue?style=for-the-badge" alt="smuggler">
  <img src="https://img.shields.io/badge/Yazar-Evan_Ricaurte-orange?style=for-the-badge" alt="defparam">
  <img src="https://img.shields.io/badge/Kategori-Web_Güvenliği_&_API-green?style=for-the-badge" alt="Web Security">
  <img src="https://img.shields.io/badge/Lisans-MIT-purple?style=for-the-badge" alt="MIT">
</p>

<p align="center">
  <b>📦 Modern web altyapılarında (Reverse Proxy, CDN, Load Balancer) HTTP İstek Kaçakçılığı (HTTP Request Smuggling - CL.TE, TE.CL) açıklarını tespit ve teyit eden Python aracı</b>
</p>

---

## 📋 İçindekiler

- [Genel Bakış ve İstek Kaçakçılığı Mantığı](#-genel-bakış-ve-i̇stek-kaçakçılığı-mantığı)
  - [CL.TE ve TE.CL Uyuşmazlığı Nedir?](#clte-ve-tecl-uyuşmazlığı-nedir)
- [Kurulum](#-kurulum)
- [Temel Hedef Taraması](#-temel-hedef-taraması)
- [Tarama Profilleri ve Başlık Mutasyonları](#-tarama-profilleri-ve-başlık-mutasyonları)
  - [Varsayılan ve Kapsamlı (Exhaustive) Mod](#varsayılan-ve-kapsamlı-exhaustive-mod)
  - [Özel Başlıklar ve Çerezler Ekleme](#özel-başlıklar-ve-çerezler-ekleme)
- [Çoklu Hedef ve Boru Hattı (Pipeline) Taraması](#-çoklu-hedef-ve-boru-hattı-pipeline-taraması)
- [Sonuçları Yorumlama ve İstismar Senaryoları](#-sonuçları-yorumlama-ve-i̇stismar-senaryoları)
  - [Önbellek Zehirleme (Cache Poisoning)](#önbellek-zehirleme-cache-poisoning)
  - [Ön Uç Güvenlik Kontrollerini Aşma](#ön-uç-güvenlik-kontrollerini-aşma)
  - [Diğer Kullanıcıların İsteklerini Çalma](#diğer-kullanıcıların-i̇steklerini-çalma)
- [Hızlı Referans Tablosu](#-hızlı-referans-tablosu)
- [Savunma Yöntemleri ve Önlemler](#-savunma-yöntemleri-ve-önlemler)
- [Yasal Uyarı](#-yasal-uyarı)

---

## 🎯 Genel Bakış ve İstek Kaçakçılığı Mantığı

HTTP Request Smuggling, ön uç sunucusu (Reverse Proxy / CDN) ile arka uç sunucusu (Backend Server) bir HTTP isteğinin nerede bittiği konusunda anlaşmazlığa düştüğünde ortaya çıkar:

### CL.TE ve TE.CL Uyuşmazlığı Nedir?
- **CL.TE:** Ön uç `Content-Length` başlığına bakar, arka uç ise `Transfer-Encoding: chunked` başlığına bakar.
- **TE.CL:** Ön uç `Transfer-Encoding` başlığını dikkate alır, arka uç ise `Content-Length` başlığına göre okur.
- **TE.TE:** İki sunucu da `Transfer-Encoding` destekler ancak başlık gizleme (örneğin iki nokta öncesi boşluk, satır kaydırma) ile sunuculardan biri yanıltılır.

**Evan Ricaurte (defparam)** tarafından geliştirilen **smuggler**, arka uç servislerinin kararlılığını bozmadan zaman aşımı sinyalleri ve desync paketleri göndererek bu açıkları doğrular.

---

## 📥 Kurulum

```bash
# Depoyu klonlama
git clone https://github.com/defparam/smuggler.git
cd smuggler

# Python 3 ile doğrudan çalıştırma
python3 smuggler.py -h
```

---

## 🚀 Temel Hedef Taraması

Tek bir web uygulamasına karşı tarama başlatma:

```bash
# Tek bir hedefe karşı standart kontrol
python3 smuggler.py -u https://hedef.com/
```

Smuggler varsayılan olarak hem `CL.TE` hem de `TE.CL` desenlerini güvenli zamanlama paketleriyle test eder.

---

## ⚙️ Tarama Profilleri ve Başlık Mutasyonları

### Varsayılan ve Kapsamlı (Exhaustive) Mod
Smuggler iki farklı profil içerir:
- **Default:** Hızlı ve en sık rastlanan desync varyasyonları.
- **Exhaustive:** Yüzlerce farklı başlık mutasyonunu, sekme (tab) ve boşluk varyasyonunu dener:

```bash
# En derin mutasyon testlerini yürütme
python3 smuggler.py -u https://hedef.com/ -c exhaustive
```

### Özel Başlıklar ve Çerezler Ekleme
Kimlik doğrulamalı sayfalarda veya özel Host başlığı gereken durumlarda:
```bash
python3 smuggler.py -u https://hedef.com/api/ \
  -H "Cookie: session=xyz123" \
  -H "X-Forwarded-Host: hedef.com"
```

---

## 🌐 Çoklu Hedef ve Boru Hattı (Pipeline) Taraması

`httpx` veya `subfinder` ile keşfedilen URL listelerini doğrudan taratın:

```bash
# Dosyadaki tüm URL'leri 10 iş parçacığıyla tara
python3 smuggler.py -f hedefler.txt -t 10

# İstek zaman aşımı süresini ayarlama (varsayılan: 10s)
python3 smuggler.py -u https://hedef.com/ --timeout 5
```

---

## 💥 Sonuçları Yorumlama ve İstismar Senaryoları

Smuggler pozitif bir açık bulduğunda:
```text
[CRITICAL] CL.TE Desync Detected: https://hedef.com/
```

### Önbellek Zehirleme (Cache Poisoning)
Kaçırılan isteğe harici yönlendirme ekleyerek web önbelleğinin (varnish, cloudflare) zehirlenmesi ve siteyi ziyaret eden tüm kullanıcılara zararlı JS indirtilmesi.

### Ön Uç Güvenlik Kontrollerini Aşma
Ön uç proxy `/admin` panelini dış dünyaya kapatmışsa, kaçırılan ikinci bir iç istek ile doğrudan backend üzerinden admin paneline erişilebilir:
```http
POST / HTTP/1.1
Host: hedef.com
Content-Length: 48
Transfer-Encoding: chunked

0

GET /admin HTTP/1.1
Host: localhost
```

### Diğer Kullanıcıların İsteklerini Çalma
İsteğin gövdesine yarım bir HTTP isteği kaçırılır. Arkadan gelen meşru kullanıcının isteği bu yarım isteğin devamı gibi algılanır ve kullanıcının oturum çerezleri saldırganın görebileceği bir alana düşer.

---

## ⚡ Hızlı Referans Tablosu

| Amaç | Komut |
|------|-------|
| **Tek Hedef Hızlı Tarama** | `python3 smuggler.py -u https://hedef.com/` |
| **Kapsamlı (Exhaustive) Tarama**| `python3 smuggler.py -u https://hedef.com/ -c exhaustive` |
| **Listeden Çoklu Tarama** | `python3 smuggler.py -f liste.txt -t 15` |
| **Çerez / Özel Başlık Ekle** | `python3 smuggler.py -u <URL> -H "Cookie: a=b"` |
| **Zaman Aşımı Ayarı** | `python3 smuggler.py -u <URL> --timeout 15` |

---

## 🛡️ Savunma Yöntemleri ve Önlemler

1. **Uçtan Uca HTTP/2 veya HTTP/3:** HTTP/2 ikili çerçeve (binary framing) yapısı kullandığından başlık uzunlukları kesindir; sınır karmaşası yaşanmaz.
2. **Başlık Normalizasyonu:** Reverse proxy seviyesinde hem `Content-Length` hem de `Transfer-Encoding` içeren istekleri anında reddedin.
3. **Belirsiz Mutasyonları Engelleme:** İki nokta öncesinde boşluk olan veya geçersiz karakterler içeren bozuk `Transfer-Encoding` isteklerini doğrudan düşürün.

---

## ⚠️ Yasal Uyarı

> [!CAUTION]
> HTTP Request Smuggling saldırıları üretim ortamlarındaki kuyrukları bozabilir ve önbelleği kirletebilir. smuggler aracı yalnızca izin alınmış yetkili sızma testlerinde dikkatle kullanılmalıdır.
