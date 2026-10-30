# 🔑 Gitleaks - Kapsamlı Başvuru Rehberi (Cheatsheet)

[🇺🇸 English Version](../README.md)

```
   ██████╗ ██╗████████╗██╗     ███████╗ █████╗ ██╗  ██╗███████╗
  ██╔════╝ ██║╚══██╔══╝██║     ██╔════╝██╔══██╗██║ ██╔╝██╔════╝
  ██║  ███╗██║   ██║   ██║     █████╗  ███████║█████╔╝ ███████╗
  ██║   ██║██║   ██║   ██║     ██╔══╝  ██╔══██║██╔═██╗ ╚════██║
  ╚██████╔╝██║   ██║   ███████╗███████╗██║  ██║██║  ██╗███████║
   ╚═════╝ ╚═╝   ╚═╝   ╚══════╝╚══════╝╚═╝  ╚═╝╚═╝  ╚═╝╚══════╝
        Git Depoları İçin Hızlı ve Otomatik Gizli Bilgi (Secret) Tarayıcısı
```

<p align="center">
  <img src="https://img.shields.io/badge/Gitleaks-Gizli_Bilgi_Tarayıcı-blue?style=for-the-badge" alt="Gitleaks">
  <img src="https://img.shields.io/badge/Yazar-Zachary_Rice-orange?style=for-the-badge" alt="zricethezav">
  <img src="https://img.shields.io/badge/Kategori-Kaynak_Kod_&_Secret-green?style=for-the-badge" alt="Secrets">
  <img src="https://img.shields.io/badge/Dil-Go-cyan?style=for-the-badge" alt="Go">
</p>

<p align="center">
  <b>🔑 Git depolarında, commit geçmişinde ve yerel dizinlerde unutulmuş parola, API anahtarı ve özel token'ları tespit eden statik analiz (SAST) aracı</b>
</p>

---

## 📋 İçindekiler

- [Genel Bakış ve Tarama Motoru](#-genel-bakış-ve-tarama-motoru)
- [Kurulum](#-kurulum)
- [Temel Gizli Bilgi Taraması](#-temel-gizli-bilgi-taraması)
  - [Git Deposunu Tarama](#git-deposunu-tarama)
  - [Commit Edilmemiş Değişiklikleri Tarama](#commit-edilmemiş-değişiklikleri-tarama)
  - [Düz Dizin ve Klasörleri Tarama](#düz-dizin-ve-klasörleri-tarama)
- [Commit Aralıklarını ve Geçmişi Hedefleme](#-commit-aralıklarını-ve-geçmişi-hedefleme)
- [CI/CD ve Pre-commit Entegrasyonu](#-cicd-ve-pre-commit-entegrasyonu)
- [Özel Kural Tanımlama ve Hariç Tutma (Allowlist)](#-özel-kural-tanımlama-ve-hariç-tutma-allowlist)
- [Raporlama ve Çıktı Formatları](#-raporlama-ve-çıktı-formatları)
- [Hızlı Referans Tablosu](#-hızlı-referans-tablosu)
- [Savunma Yöntemleri ve İyileştirme](#-savunma-yöntemleri-ve-iyileştirme)
- [Yasal Uyarı](#-yasal-uyarı)

---

## 🎯 Genel Bakış ve Tarama Motoru

Kaynak kod içerisine gömülmüş gizli bilgiler (AWS erişim anahtarları, SSH gizli anahtarları, GitHub token'ları, veritabanı şifreleri) bulut ve iç ağ sistemlerinin ele geçirilmesinde en sık rastlanan başlangıç vektörlerinden biridir.

**Zachary Rice (@zricethezav)** tarafından Go ile geliştirilen **Gitleaks**, kurallara dayalı regex eşleştirmeleri ve Shannon entropi analizi kullanarak şunları denetler:
- **Tüm Git Geçmişi:** Tüm dallardaki (branches), etiketlerdeki (tags) ve geçmiş commit'lerdeki sızıntıları yakalar.
- **Pre-commit Kancaları:** Kod uzak depoya gönderilmeden önce yerel makinede engelleme yapar.
- **CI/CD Entegrasyonu:** Çekme isteklerinde (Pull Request) sızıntı tespit edilirse derleme sürecini otomatik durdurur.

---

## 📥 Kurulum

```bash
# macOS (Homebrew)
brew install gitleaks

# Go ile Kurulum
go install github.com/zricethezav/gitleaks/v8@latest

# Docker ile Çalıştırma
docker pull zricethezav/gitleaks:latest

# Linux İkili Dosya İndirme
curl -sSL https://github.com/zricethezav/gitleaks/releases/latest/download/gitleaks_linux_x64.tar.gz | tar -xz
sudo mv gitleaks /usr/local/bin/
```

---

## 🚀 Temel Gizli Bilgi Taraması

### Git Deposunu Tarama
Mevcut deponun tüm commit geçmişini taramak için:
```bash
# Bulunulan depoda tarama başlatma
gitleaks detect -v

# Farklı bir yerel dizindeki depoyu tarama
gitleaks detect --source /path/to/repo -v
```

### Commit Edilmemiş Değişiklikleri Tarama
Henüz commit atılmamış yerel değişiklikleri doğrulamak için:
```bash
# Çalışma alanındaki kaydedilmemiş tüm değişiklikleri denetleme
gitleaks protect -v

# Yalnızca 'git add' ile sahneye (stage) alınan dosyaları denetleme
gitleaks protect --staged -v
```

### Düz Dizin ve Klasörleri Tarama
Git deposu olmayan klasörleri (örneğin decompile edilmiş mobil uygulama dosyaları veya konfigürasyon dizinleri):
```bash
gitleaks dir /path/to/directory -v
```

---

## ⚡ Commit Aralıklarını ve Geçmişi Hedefleme

```bash
# Son 7 gün içerisinde yapılan commit'leri tarama
gitleaks detect --log-opts="--since='7 days ago'" -v

# Belirli iki dal (branch) arasındaki farkları tarama
gitleaks detect --log-opts="origin/main..feature-branch" -v

# Yalnızca son 5 commit'i inceleme
gitleaks detect --log-opts="-n 5" -v
```

---

## ⚙️ CI/CD ve Pre-commit Entegrasyonu

### Pre-commit Yapılandırması (`.pre-commit-config.yaml`)
```yaml
repos:
  - repo: https://github.com/gitleaks/gitleaks
    rev: v8.18.2
    hooks:
      - id: gitleaks
```

### GitHub Actions İş Akışı
```yaml
name: Gitleaks Security Scan
on: [push, pull_request]
jobs:
  gitleaks:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
        with:
          fetch-depth: 0
      - uses: gitleaks/gitleaks-action@v2
        env:
          GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}
```

---

## 🧩 Özel Kural Tanımlama ve Hariç Tutma (Allowlist)

`.gitleaks.toml` dosyası ile kuralları genişletebilir veya yanlış pozitifleri filtreleyebilirsiniz:

```toml
[extend]
useDefault = true

[allowlist]
description = "Test dosyalarını ve örnek token'ları yoksay"
paths = [
  '''tests/.*''',
  '''fixtures/.*'''
]
regexes = [
  '''TEST_API_KEY_.*'''
]

[[rules]]
id = "custom-internal-token"
description = "Kurum İçi JWT Gizli Anahtarı"
regex = '''(?i)(corp_secret_[a-z0-9]{32})'''
secretGroup = 1
entropy = 3.5
keywords = ["corp_secret_"]
```

Özel yapılandırma dosyasını çalıştırmak için:
```bash
gitleaks detect -c .gitleaks.toml -v
```

---

## 📊 Raporlama ve Çıktı Formatları

```bash
# JSON formatında rapor oluşturma
gitleaks detect --report-path gitleaks-report.json --report-format json

# SARIF formatında rapor (GitHub Güvenlik sekmesi için)
gitleaks detect --report-path report.sarif --report-format sarif

# CSV formatında çıktı
gitleaks detect --report-path report.csv --report-format csv
```

---

## ⚡ Hızlı Referans Tablosu

| Amaç | Komut |
|------|-------|
| **Tüm Depo Geçmişini Tara** | `gitleaks detect -v` |
| **Commit Öncesi Değişiklikleri Tara** | `gitleaks protect -v` |
| **Staged Değişiklikleri Tara** | `gitleaks protect --staged -v` |
| **Git Dışı Klasör Tara** | `gitleaks dir /path/to/folder -v` |
| **JSON Olarak Raporla** | `gitleaks detect -r report.json -f json` |
| **SARIF Olarak Raporla** | `gitleaks detect -r report.sarif -f sarif` |
| **Özel Kural Dosyası Kullan** | `gitleaks detect -c .gitleaks.toml` |
| **Dallar Arası Farkı Tara** | `gitleaks detect --log-opts="main..dev"` |

---

## 🛡️ Savunma Yöntemleri ve İyileştirme

1. **Derhal İptal (Revoke):** Git geçmişinde görünen herhangi bir gizli bilgi anında ele geçirilmiş kabul edilmeli ve vakit kaybetmeden iptal edilip yenilenmelidir (key rotation).
2. **Git Geçmişini Temizleme:** `git filter-repo` veya BFG Repo-Cleaner kullanarak sızdırılan commit'ler temizlenmelidir; ancak anahtarı iptal etmeden sadece geçmişi silmek kesinlikle yeterli değildir.

---

## ⚠️ Yasal Uyarı

> [!CAUTION]
> Gitleaks yalnızca yetkili güvenlik testleri, kaynak kod denetimleri ve savunma amaçlı analizler için kullanılmalıdır. İzniniz olmayan harici depoları taramak yasalara aykırı olabilir.
