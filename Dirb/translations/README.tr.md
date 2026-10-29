# 📁 DIRB - Kapsamlı Başvuru Kılavuzu

[🇺🇸 English Version](../README.md)

```
  ██████╗ ██╗██████╗ ██████╗ 
  ██╔══██╗██║██╔══██╗██╔══██╗
  ██║  ██║██║██████╔╝██████╔╝
  ██║  ██║██║██╔══██╗██╔══██╗
  ██████╔╝██║██║  ██║██████╔╝
  ╚═════╝ ╚═╝╚═╝  ╚═╝╚═════╝ 
        Web Content Scanner
```

<p align="center">
  <img src="https://img.shields.io/badge/DIRB-Content_Scanner-orange?style=for-the-badge" alt="DIRB">
  <img src="https://img.shields.io/badge/Wordlists-Bruteforce-blue?style=for-the-badge" alt="Wordlists">
  <img src="https://img.shields.io/badge/Kali_Linux-Preinstalled-green?style=for-the-badge" alt="Kali Linux">
  <img src="https://img.shields.io/badge/Web-Pentesting-red?style=for-the-badge" alt="Web Pentesting">
</p>

<p align="center">
  <b>🔍 Web sunucularına karşı sözlük tabanlı dizin ve dosya keşif saldırıları düzenleyen klasik içerik tarayıcısı</b>
</p>

---

## 📋 İçindekiler

- [Giriş](#-giriş)
- [DIRB ve Modern Alternatiflerin Karşılaştırması](#-dirb-ve-modern-alternatiflerin-karşılaştırması)
- [Kurulum](#-kurulum)
- [Varsayılan Kelime Listeleri (Wordlists)](#-varsayılan-kelime-listeleri-wordlists)
- [Temel Sözdizimi](#-temel-sözdizimi)
- [Yaygın Tarama Senaryoları](#-yaygın-tarama-senaryoları)
- [Gelişmiş Seçenekler ve İnce Ayar](#-gelişmiş-seçenekler-ve-ince-ayar)
- [Dahili Listelerle Zafiyet Taraması](#-dahili-listelerle-zafiyet-taraması)
- [Gerçek Dünya İş Akışları](#-gerçek-dünya-iş-akışları)
- [Hızlı Referans Tablosu](#-hızlı-referans-tablosu)
- [İpuçları ve En İyi Pratikler](#-i̇puçları-ve-en-i̇yi-pratikler)
- [Yasal Uyarı](#-yasal-uyarı)
- [Kaynaklar](#-kaynaklar)

---

## 🎯 Giriş

**DIRB** (Directory Buster), C diliyle yazılmış, URL tabanlı klasik ve hafif bir web içerik tarayıcısıdır. Web sunucularında mevcut olan ancak doğrudan link verilmeyen gizli web nesnelerini (dizinler, dosyalar, scriptler ve uç noktalar) tespit etmek amacıyla sözlük tabanlı (dictionary-based) kaba kuvvet saldırıları düzenler.

DIRB, belirlenen kelime listesindeki her bir girdiyi hedef URL'ye ekleyerek HTTP istekleri gönderir; sunucudan dönen yanıt kodlarını (200, 301, 302, 403 vb.) analiz ederek kaynağın var olup olmadığını belirler.

### Temel Özellikler

- ✅ **Otomatik Özyinelemeli (Recursive) Tarama:** Bulunan alt dizinleri varsayılan olarak otomatik derinlemesine tarar.
- ✅ **Dahili Özel Kelime Listeleri:** Popüler web sunucuları, CMS'ler ve bilinen zafiyetler için hedefe yönelik kelime listeleri içerir.
- ✅ **Düşük Sistem Kaynağı Tüketimi:** Düşük donanımlı veya eski sistemlerde bile sorunsuz çalışan son derece hafif bir ikilidir.
- ✅ **Özel Başlık ve Proxy Desteği:** Burp Suite ve harici HTTP proxy sunucularıyla kolayca entegre olur.

---

## ⚖️ DIRB ve Modern Alternatiflerin Karşılaştırması

| Özellik | DIRB | Gobuster | ffuf | Feroxbuster |
|---------|------|----------|------|-------------|
| **Yazıldığı Dil** | C | Go | Go | Rust |
| **Hız** | ⭐⭐ (Tek / Düşük iş parçacığı) | ⭐⭐⭐⭐⭐ (Yüksek eşzamanlılık) | ⭐⭐⭐⭐⭐ (Son derece hızlı) | ⭐⭐⭐⭐⭐ (Asenkron Rust) |
| **Varsayılan Özyineleme (Recursion)** | ✅ Evet (Otomatik) | ❌ Hayır | ❌ Hayır (İsteğe bağlı `-recursion`) | ✅ Evet (Otomatik) |
| **Dahili Kelime Listeleri** | ✅ Evet (`/usr/share/dirb/`) | ❌ Hayır | ❌ Hayır | ❌ Hayır |
| **VHost Fuzzing** | ❌ Hayır | ✅ Evet | ✅ Evet | ❌ Hayır |
| **POST / Gövde Verisi Fuzzing** | ❌ Hayır | ❌ Hayır | ✅ Evet | ❌ Hayır |
| **Bellek / CPU Tüketimi** | Çok Düşük | Düşük | Düşük | Çok Düşük |

---

## 📥 Kurulum

### Kali Linux & Parrot OS

DIRB, Kali Linux ve Parrot OS dağıtımlarında önceden yüklü olarak gelir:

```bash
# Kurulumu doğrulayın
dirb

# En son sürüme güncelleyin
sudo apt update && sudo apt install -y dirb
```

### Debian / Ubuntu

```bash
sudo apt update
sudo apt install -y dirb
```

### Kaynak Koddan Derleme

```bash
# Gerekli derleme bağımlılıklarını kurun
sudo apt install -y build-essential libcurl4-openssl-dev

# Kaynak kodu indirin ve arşivden çıkarın
wget https://sourceforge.net/projects/dirb/files/dirb/2.22/dirb222.tar.gz
tar -xzvf dirb222.tar.gz
cd dirb222

# İzinleri yapılandırın ve derleyin
chmod -R 755 conf
chmod 755 configure
./configure
make
sudo make install
```

---

## 📚 Varsayılan Kelime Listeleri (Wordlists)

Kali Linux üzerinde DIRB kurulduğunda, `/usr/share/dirb/wordlists/` dizini altında kullanıma hazır listeler yer alır:

| Liste Yolu | Satır Sayısı | Kullanım Amacı |
|------------|--------------|----------------|
| `/usr/share/dirb/wordlists/common.txt` | ~4.614 | Kelime listesi belirtilmediğinde kullanılan varsayılan liste |
| `/usr/share/dirb/wordlists/small.txt` | ~959 | Yüksek olasılıklı yolların hızlı keşfi |
| `/usr/share/dirb/wordlists/big.txt` | ~20.469 | Kapsamlı ve derinlemesine dizin/dosya taraması |
| `/usr/share/dirb/wordlists/catala.txt` | ~6.846 | Katalanca kelime listesi |
| `/usr/share/dirb/wordlists/spanish.txt` | ~11.720 | İspanyolca kelime listesi |
| `/usr/share/dirb/wordlists/mutations_common.txt` | ~1.000 | Yaygın önek ve sonek türetmeleri |
| `/usr/share/dirb/wordlists/extensions_common.txt` | ~40 | Standart web uzantıları (`.php`, `.asp`, `.html` vb.) |

### Dahili Zafiyet Listeleri (`/usr/share/dirb/wordlists/vulns/`)

- `apache.txt` - Apache sunucusuna özgü dosyalar, durum sayfaları ve bilinen dizinler
- `iis.txt` - Microsoft IIS web sunucusu dosyaları (`web.config`, `aspnet_client` vb.)
- `tomcat.txt` - Apache Tomcat yönetim ve yönetici yolları
- `cgis.txt` - Common Gateway Interface (CGI) betikleri ve uç noktaları
- `fatwire.txt`, `hpsmh.txt`, `hyperion.txt`, `iplanet.txt`, `netware.txt`, `oracle.txt`

---

## ⌨️ Temel Sözdizimi

```bash
dirb <url_base> [<wordlist_dosyası>] [seçenekler]
```

### Temel Örnekler

```bash
# Varsayılan common.txt listesiyle temel tarama
dirb http://10.10.10.10

# Belirli bir kelime listesiyle tarama
dirb https://example.com /usr/share/wordlists/dirb/big.txt

# SecLists dizin listesiyle tarama
dirb http://target.local /usr/share/seclists/Discovery/Web-Content/directory-list-2.3-medium.txt
```

---

## 🔍 Yaygın Tarama Senaryoları

### 1. Dosya Uzantılarıyla Tarama Yapma (`-X` ve `-x`)

Sözlükteki her girdinin sonuna belirli dosya uzantılarını ekleyerek arama yapın:

```bash
# Her kelime için belirli uzantıları test edin
dirb http://target.local -X .php,.html,.txt,.bak

# Uzantı listesi içeren bir dosya kullanın
dirb http://target.local -x /usr/share/dirb/wordlists/extensions_common.txt

# Dizin brute-force ile uzantı aramalarını birleştirin
dirb http://target.local /usr/share/dirb/wordlists/common.txt -X .php,.json,.zip
```

### 2. Özyinelemeyi (Recursion) Kontrol Etme (`-r` ve `-R`)

Varsayılan olarak DIRB, bulduğu her alt dizinin içine otomatik olarak girer. Bu davranışı sınırlandırabilirsiniz:

```bash
# Özyinelemeyi tamamen devre dışı bırakın (sadece kök dizini tarayın)
dirb http://target.local -r

# Etkileşimli özyineleme (her alt dizine girmeden önce onay ister)
dirb http://target.local -R
```

### 3. Büyük/Küçük Harf Duyarsız Tarama (`-i`)

Dosya yollarının büyük/küçük harfe duyarsız olduğu Windows / IIS sunucularını hedeflerken yararlıdır:

```bash
dirb http://target.local -i
```

### 4. Belirli HTTP Durum Kodlarını Yoksayma (`-N`)

İstenmeyen durum kodlarını (örneğin yönlendirme döngüleri veya 403 yasak yanıtları) filtreleyin:

```bash
# 302 yönlendirmelerini ve 403 yasak kodlarını yoksayın
dirb http://target.local -N 302,403
```

---

## ⚙️ Gelişmiş Seçenekler ve İnce Ayar

### Kimlik Doğrulama (`-u`)

DIRB, HTTP Basic ve Digest kimlik doğrulamasını destekler:

```bash
# HTTP kimlik doğrulama bilgilerini iletin
dirb http://target.local -u "admin:SecretP@ssw0rd!"

# Özel kelime listesiyle birlikte kimlik doğrulama kullanın
dirb http://target.local /usr/share/dirb/wordlists/big.txt -u "pentester:Summer2026"
```

### Özel Çerezler ve Oturum Belirteçleri (`-c`)

Oturum açılmış web uygulaması rotalarını test etmek için oturum çerezlerini ekleyin:

```bash
# Oturum çerezini tanımlayın
dirb http://target.local -c "PHPSESSID=d92j4fn82mdf82ncb184; logged_in=true"
```

### Özel User-Agent ve HTTP Başlıkları (`-a` ve `-H`)

Temel WAF kurallarını atlatmak veya meşru tarayıcıları taklit etmek için:

```bash
# Özel User-Agent belirleyin
dirb http://target.local -a "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36"

# Özel HTTP Başlığı ekleyin
dirb http://target.local -H "Authorization: Bearer eyJhbGciOi..."
dirb http://target.local -H "X-Forwarded-For: 127.0.0.1"
```

### Proxy Yönlendirmesi ve Burp Suite Entegrasyonu (`-p` ve `-P`)

DIRB trafiğini Burp Suite veya OWASP ZAP gibi bir HTTP proxy üzerinden geçirin:

```bash
# Yerel Burp Suite proxy'sine yönlendirin
dirb http://target.local -p 127.0.0.1:8080

# Kimlik doğrulamalı proxy kullanımı
dirb http://target.local -p 10.10.10.1:3128 -P "proxyuser:proxypass"
```

### Hız Sınırlama ve Atlatma (Evasion) (`-z`)

IDS/IPS sistemlerini tetiklememek veya hassas web servislerini aşırı yüklememek için istekler arasına milisaniye cinsinden gecikme ekleyin:

```bash
# İstekler arasında 250 ms bekleyin
dirb http://target.local -z 250

# Yavaş ve sessiz tarama (1 saniye gecikme)
dirb http://target.local -z 1000
```

### Çıktıyı Dosyaya Kaydetme (`-o` ve `-w`)

```bash
# Tarama sonuçlarını bir dosyaya kaydedin
dirb http://target.local -o /home/zeus/scans/dirb_target.txt

# UYARI (WARNING) mesajlarında taramayı durdurmayın
dirb http://target.local -w -o dirb_output.txt
```

---

## 🎯 Dahili Listelerle Zafiyet Taraması

DIRB, `/usr/share/dirb/wordlists/vulns/` dizininde yamanmamış sunucuları ve tehlikeli uç noktaları bulmak için hazırlanmış listeler barındırır:

```bash
# Apache sunucu zafiyetleri ve durum sayfaları için tarama
dirb http://target.local /usr/share/dirb/wordlists/vulns/apache.txt

# IIS uç noktaları ve yanlış yapılandırmaları için tarama
dirb http://target.local /usr/share/dirb/wordlists/vulns/iis.txt

# Tomcat yönetim panelleri için tarama
dirb http://target.local /usr/share/dirb/wordlists/vulns/tomcat.txt

# Eski CGI betikleri ve Shellshock giriş noktalarını arama
dirb http://target.local /usr/share/dirb/wordlists/vulns/cgis.txt
```

---

## 🎬 Gerçek Dünya İş Akışları

### İş Akışı 1: Hızlı İlk Keşif

80 veya 443 portunda bir web sunucusu ile ilk kez karşılaştığınızda:

```bash
# Küçük liste ile özyinelemesiz hızlı tarama
dirb http://10.10.10.50 /usr/share/dirb/wordlists/small.txt -r
```

### İş Akışı 2: Kapsamlı Web Uygulama Değerlendirmesi

Yapılandırma yedekleri, kaynak kodlar ve API uç noktalarını arayan derin tarama:

```bash
dirb https://app.target.local /usr/share/dirb/wordlists/common.txt \
  -X .php,.bak,.sql,.config,.txt \
  -a "Mozilla/5.0" \
  -w \
  -o dirb_app_scan.txt
```

### İş Akışı 3: Burp Suite Üzerinden Kimlik Doğrulamalı Portal Taraması

```bash
dirb https://internal.target.local/portal/ /usr/share/dirb/wordlists/big.txt \
  -c "session=s%3A_kJw81..." \
  -p 127.0.0.1:8080 \
  -X .jsp,.do,.action \
  -z 100
```

---

## 📊 Hızlı Referans Tablosu

| Parametre | Bağımsız Değişken | Açıklama | Örnek |
|-----------|-------------------|----------|-------|
| `-a` | `<user_agent>` | Özel User-Agent dizesi belirler | `dirb http://target -a "Mozilla/5.0"` |
| `-c` | `<cookie>` | HTTP Cookie başlığı tanımlar | `dirb http://target -c "id=123"` |
| `-H` | `<header>` | Özel HTTP başlığı ekler | `dirb http://target -H "X-API-Key: secret"` |
| `-i` | Yok | Büyük/küçük harf duyarsız arama yapar | `dirb http://target -i` |
| `-N` | `<kodlar>` | Belirtilen durum koduna sahip yanıtları yoksayar | `dirb http://target -N 302,403` |
| `-o` | `<dosya>` | Tarama sonuçlarını dosyaya kaydeder | `dirb http://target -o results.txt` |
| `-p` | `<proxy:port>` | HTTP proxy kullanır | `dirb http://target -p 127.0.0.1:8080` |
| `-P` | `<kullanici:sifre>` | Proxy kimlik doğrulama bilgileri | `dirb http://target -p proxy:8080 -P u:p` |
| `-r` | Yok | Özyinelemeli (recursive) tarama yapmaz | `dirb http://target -r` |
| `-R` | Yok | Etkileşimli özyinelemeli arama yapar | `dirb http://target -R` |
| `-u` | `<kullanici:sifre>` | HTTP Basic/Digest kimlik doğrulaması | `dirb http://target -u "admin:1234"` |
| `-w` | Yok | UYARI (WARNING) mesajlarında taramayı durdurmaz | `dirb http://target -w` |
| `-X` | `<uzanti_listesi>` | Virgülle ayrılmış uzantıları kelimelere ekler | `dirb http://target -X .php,.html` |
| `-x` | `<uzanti_dosyasi>` | Dosyadan okunan uzantıları ekler | `dirb http://target -x ext.txt` |
| `-z` | `<milisaniye>` | İstekler arasına bekleme süresi ekler | `dirb http://target -z 250` |

---

## 💡 İpuçları ve En İyi Pratikler

1. **Joker (Wildcard) 200/302 Yanıtlarına Dikkat Edin:**
   - Bazı web uygulamaları bulunamayan sayfalar için de 200 veya 302 döner (özel 404 sayfası). DIRB bu durumda her girdiyi "bulundu" olarak raporlar. `-N 302` parametresini kullanın veya gelişmiş boyut/kelime filtreleme için `ffuf` ya da `feroxbuster` araçlarına geçin.
2. **Büyük Listelerle `-r` Kullanımını Sınırlandırın:**
   - Eğer `directory-list-2.3-medium.txt` (220.000 kelime) gibi devasa listeler kullanıyorsanız, mutlaka `-r` ile özyinelemeyi kapatın. Aksi takdirde bulunan her dizin için liste baştan taranır ve işlem günler sürebilir.
3. **Kritik Uzantıları Her Zaman Dahil Edin:**
   - Modern uygulamalarda `.env`, `.git`, `.bak`, `.conf` veya `.sql` gibi hassas dosyalar sıkça unutulur. `-X` ile bu uzantıları mutlaka tarayın.
4. **Müdahalesiz Taramalar İçin `-w` Kullanın:**
   - DIRB bazı SSL ve ağ uyarılarında kullanıcı onayı bekleyebilir. Otomasyonda veya arka planda çalışırken takılmaması için `-w` parametresini ekleyin.

---

## ⚠️ Yasal Uyarı

> **ÖNEMLİ:** DIRB aracı yalnızca sahibi olduğunuz veya test etmek için açık, yazılı izne sahip olduğunuz sistemlerde güvenlik denetimleri ve sızma testleri gerçekleştirmek amacıyla tasarlanmıştır. Yetkisiz web tarama ve kaba kuvvet saldırıları yasalara aykırıdır ve cezai yaptırımlara tabidir.

---

## 📚 Kaynaklar

- [DIRB SourceForge Projesi](https://sourceforge.net/projects/dirb/)
- [Kali Linux DIRB Paket Dokümantasyonu](https://www.kali.org/tools/dirb/)
- [SecLists - Web İçerik Keşfi Kelime Listeleri](https://github.com/danielmiessler/SecLists)
