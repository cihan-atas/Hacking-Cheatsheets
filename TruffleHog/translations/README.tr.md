# 🐽 TruffleHog - Kapsamlı Başvuru Kılavuzu

[🇬🇧 English Version](../README.md)

```
  ████████╗██████╗ ██╗   ██╗███████╗███████╗██╗     ███████╗██╗  ██╗ ██████╗  ██████╗ 
  ╚══██╔══╝██╔══██╗██║   ██║██╔════╝██╔════╝██║     ██╔════╝██║  ██║██╔═══██╗██╔════╝ 
     ██║   ██████╔╝██║   ██║█████╗  █████╗  ██║     █████╗  ███████║██║   ██║██║  ███╗
     ██║   ██╔══██╗██║   ██║██╔══╝  ██╔══╝  ██║     ██╔══╝  ██╔══██║██║   ██║██║   ██║
     ██║   ██║  ██║╚██████╔╝██║     ██║     ███████╗███████╗██║  ██║╚██████╔╝╚██████╔╝
     ╚═╝   ╚═╝  ╚═╝ ╚═════╝ ╚═╝     ╚═╝     ╚══════╝╚══════╝╚═╝  ╚═╝ ╚═════╝  ╚═════╝ 
       Gerçek Zamanlı Canlı Doğrulama ile Açıkta Kalan Gizli Bilgileri ve API Anahtarlarını Bulma
```

<p align="center">
  <img src="https://img.shields.io/badge/TruffleHog-Gizli_Bilgi_Tarayıcısı-blue?style=for-the-badge" alt="TruffleHog">
  <img src="https://img.shields.io/badge/Geliştirici-Truffle_Security-orange?style=for-the-badge" alt="Truffle Security">
  <img src="https://img.shields.io/badge/Kategori-OSINT_&_Kod_Güvenliği-green?style=for-the-badge" alt="Kod Güvenliği">
  <img src="https://img.shields.io/badge/Lisans-AGPLv3-red?style=for-the-badge" alt="Lisans AGPLv3">
</p>

<p align="center">
  <b>🌐 Git depolarında, S3 kovalarında, Docker imajlarında ve dosya sistemlerinde unutulan API anahtarlarını, parolaları ve sertifikaları canlı doğrulama ile bulan araç</b>
</p>

---

## 📋 İçindekiler

- [Genel Bakış & Canlı Doğrulama (Live Verification) Süper Gücü](#-genel-bakış--canlı-doğrulama-live-verification-süper-gücü)
- [TruffleHog vs Gitleaks Karşılaştırması](#-trufflehog-vs-gitleaks-karşılaştırması)
- [Kurulum](#-kurulum)
- [Git Depolarını Tarama](#-git-depolarını-tarama)
- [Yerel Dosya Sistemi & Dizin Taraması](#-yerel-dosya-sistemi--dizin-taraması)
- [Tüm GitHub Organizasyonunu Tarama](#-tüm-github-organizasyonunu-tarama)
- [S3 Kovaları & Docker İmajlarını Tarama](#-s3-kovaları--docker-imajlarını-tarama)
- [Yanlış Alarmları Eleme (`--only-verified`)](#-yanlış-alarmları-eleme---only-verified)
- [JSON Çıktısı & CI/CD Entegrasyonu](#-json-çıktısı--cicd-entegrasyonu)
- [Hızlı Referans Tablosu](#-hızlı-referans-tablosu)
- [Savunma Açısından Değerlendirme & OPSEC](#-savunma-açısından-değerlendirme--opsec)
- [Yasal Uyarı](#-yasal-uyarı)

---

## 🎯 Genel Bakış & Canlı Doğrulama (Live Verification) Süper Gücü

Yazılımcılar dikkatsizlik sonucu AWS erişim anahtarlarını, Stripe gizli anahtarlarını, Slack webhook adreslerini ve özel SSH anahtarlarını sıklıkla Git depolarına gönderir (commit). Sonraki commit'lerde bu dosyalar silinse bile, **tüm sırlar Git commit geçmişinde kalıcı olarak yaşamaya devam eder**.

**Truffle Security** tarafından Go ile geliştirilen **TruffleHog**, commit geçmişlerini, dosya sistemlerini ve bulut depolama alanlarını tarayarak bu sızdırılmış anahtarları tespit eder.

TruffleHog'un en belirgin süper gücü **Canlı Doğrulama (Live Verification)** özelliğidir:
Güvenlik ekiplerini binlerce sahte pozitif (dokümantasyon örnekleri, geçersiz anahtarlar) ile boğan klasik regex tarayıcılarının aksine TruffleHog; bulduğu anahtarın aktif olup olmadığını ilgili bulut sağlayıcısına (AWS, GitHub, OpenAI, Slack) anlık istek atarak test eder ve **yalnızca gerçekten çalışan canlı anahtarları raporlar**.

---

## ⚖️ TruffleHog vs Gitleaks Karşılaştırması

| Özellik | TruffleHog | Gitleaks |
|---------|------------|----------|
| **Canlı Anahtar Doğrulama** | ✅ Var (750+ sağlayıcı canlı test edilir)| ❌ Yok (Yalnızca Regex) |
| **Hedef Kaynaklar** | Git, GitHub, S3, Docker, Dosya Sistemi| Git, Yerel Dosyalar |
| **Devasa Repolarda Hız** | Hızlı (Go Çoklu İş Parçacığı) | Çok Hızlı (Minimal Regex) |
| **Yanlış Pozitif (False Positive)**| Neredeyse Sıfır (`--only-verified` ile) | Orta Düzey (İstisna listesi gerekir)|
| **Pre-Commit Hook Desteği**| ✅ Destekler | ✅ Optimize |

---

## 📥 Kurulum

```bash
# Debian / Ubuntu / Kali Linux (curl betiği ile)
curl -sSfL https://raw.githubusercontent.com/trufflesecurity/trufflehog/main/scripts/install.sh | sudo sh -s -- -b /usr/local/bin

# Arch Linux (AUR)
yay -S trufflehog-bin

# Homebrew (macOS / Linux)
brew install trufflehog

# Docker Konteyneri ile
docker run -it --rm trufflesecurity/trufflehog:latest git https://github.com/hedef/repo
```

---

## 🚀 Git Depolarını Tarama

```bash
# Uzaktaki bir Git deposunu tüm commit geçmişiyle tarama
trufflehog git https://github.com/hedefsirket/kamuya-acik-proje

# Kimlik doğrulama gerektiren özel bir GitLab/GitHub deposunu tarama
trufflehog git https://kullanici:token@gitlab.corp.local/repo.git

# Yalnızca en son commit'i tarama (Pre-commit hook'ları için)
trufflehog git file://./ --since-commit HEAD~1
```

---

## 📂 Yerel Dosya Sistemi & Dizin Taraması

```bash
# Geçerli dizini ve alt klasörleri tarama
trufflehog filesystem .

# Belirli dizinleri hariç tutarak web kök dizinini tarama
trufflehog filesystem /var/www/html/ --exclude-paths tests/,vendor/
```

---

## 🏢 Tüm GitHub Organizasyonunu Tarama

Bir şirkete ait tüm açık GitHub depolarını tek komutla denetleyin:

```bash
# Bir GitHub organizasyonunun tüm açık depolarını tarama
trufflehog github --org=hedefkurum --token=SIZIN_GITHUB_TOKENINIZ

# Kişisel bir hesabın açık ve kapalı tüm depolarını tarama
trufflehog github --user=hedefkullanici --token=SIZIN_GITHUB_TOKENINIZ
```

---

## ☁️ S3 Kovaları & Docker İmajlarını Tarama

```bash
# Açık bir AWS S3 kovasında unutulmuş log ve yedekleri tarama
trufflehog s3 --bucket=hedef-yedek-kovasi

# Bir Docker imaj katmanlarında gömülü kalan sırları arama
trufflehog docker --image=hedefkurum/api-sunucu:latest
```

---

## 🎯 Yanlış Alarmları Eleme (`--only-verified`)

Doğrulanmamış tüm şüpheli eşleşmeleri gizleyip **yalnızca sağlayıcı tarafından onaylanan aktif canlı anahtarları** ekrana basın:

```bash
# SADECE canlı çalışan doğrulanmış sırları raporlama
trufflehog git https://github.com/hedef/repo --only-verified

# Dosya sisteminde yalnızca aktif anahtarları bulma
trufflehog filesystem /opt/yedekler/ --only-verified
```

---

## 📊 JSON Çıktısı & CI/CD Entegrasyonu

```bash
# Bulguları yapılandırılmış JSON dosyasına aktarma
trufflehog git https://github.com/hedef/repo --json > gizli_bilgiler.json

# jq ile canlı AWS anahtarlarını ayıklama
cat gizli_bilgiler.json | jq -r 'select(.Verified == true) | .Raw'
```

---

## ⚡ Hızlı Referans Tablosu

| İşlem | Komut |
|-------|-------|
| **Uzak Git Deposunu Tara** | `trufflehog git <GIT_URL>` |
| **Yalnızca Canlı Anahtarları Göster**| `trufflehog git <GIT_URL> --only-verified` |
| **Yerel Dizini Tara** | `trufflehog filesystem <DIZIN>` |
| **GitHub Organizasyonu Tara** | `trufflehog github --org=<ORG> --token=<PAT>` |
| **Docker İmajını Tara** | `trufflehog docker --image=<IMAJ>` |
| **S3 Kovasını Tara** | `trufflehog s3 --bucket=<KOVA_ADI>` |
| **JSON Olarak Kaydet** | `trufflehog git <GIT_URL> --json` |

---

## 🛡️ Savunma Açısından Değerlendirme & OPSEC

1. **Dışa Giden Doğrulama İstekleri:** `--no-verification` bayrağı verilmediğinde TruffleHog bulduğu anahtarları test etmek için dış sağlayıcılara (AWS, Slack, GitHub) bağlanır. Bu durum hedef kurumun CloudTrail veya denetim loglarında alarm oluşturabilir. Gizli sızma testlerinde bu özelliği kapatabilirsiniz:
   ```bash
   trufflehog git <URL> --no-verification
   ```
2. **Anahtar İptali (Revocation):** Açık depolarda tespit edilen anahtarlar acilen iptal edilmeli ve loglar geçmişe dönük yetkisiz erişimler için taranmalıdır.

---

## ⚠️ Yasal Uyarı

> [!CAUTION]
> TruffleHog yalnızca yetkili güvenlik denetimleri ve siber güvenlik araştırmaları için kullanılmalıdır. Ele geçirilen anahtarlarla yetkisiz sistemlere veya bulut altyapılarına erişmek bilişim suçudur.