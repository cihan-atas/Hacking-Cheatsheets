# Trivy Bilgi Notu (Cheatsheet)

[Trivy](https://github.com/aquasecurity/trivy), Aqua Security tarafından geliştirilmiş açık kaynaklı ve çok amaçlı bir güvenlik tarayıcısıdır. Konteyner imajları (Docker/OCI), dosya sistemleri, Git depoları, sanal makine diskleri, Kubernetes kümeleri ve Kod Olarak Altyapı (IaC - Terraform, Dockerfile, Helm) yapılandırmalarındaki güvenlik açıklarını (CVE), sızdırılmış sırları ve hatalı yapılandırmaları tespit eder; ayrıca SBOM üretir.

---

## İçindekiler

- [Temel Yetenekler](#temel-yetenekler)
- [Kurulum](#kurulum)
- [Konteyner İmajı Taraması (`trivy image`)](#konteyner-imajı-taraması-trivy-image)
- [Dosya Sistemi ve Depo Taraması (`trivy fs / repo`)](#dosya-sistemi-ve-depo-taraması-trivy-fs--repo)
- [Hassas Veri & Parola Tespiti (`--scanners secret`)](#hassas-veri--parola-tespiti---scanners-secret)
- [Kubernetes Küme Denetimi (`trivy k8s`)](#kubernetes-küme-denetimi-trivy-k8s)
- [Kod Olarak Altyapı (IaC) Güvenlik Analizi](#kod-olarak-altyapı-iac-güvenlik-analizi)
- [CI/CD Boru Hattı & Hata Kodları (`--exit-code`)](#cicd-boru-hattı--hata-kodları---exit-code)
- [SBOM Üretimi (CycloneDX & SPDX)](#sbom-üretimi-cyclonedx--spdx)

---

## Temel Yetenekler

- **Zafiyet Analizi (CVE):** İşletim sistemi paketleri (Alpine, Debian, RHEL, Ubuntu vb.) ve programlama dili bağımlılıklarındaki (Go, Python, Node.js, Java, Rust, .NET vb.) açıkları listeler.
- **Hatalı Yapılandırma (Misconfiguration):** Dockerfile, Kubernetes YAML, Terraform ve CloudFormation şablonlarını CIS kurallarına göre denetler.
- **Sır / Gizli Bilgi Tespiti:** Kod veya imaj katmanlarına gömülü API anahtarlarını, sertifikaları ve parolaları yakalar.
- **Kubernetes Canlı Denetimi:** Aktif kümedeki pod ve servisleri canlı analiz eder.
- **Hafif ve Hızlı:** Dış bir sunucu veya daemon gerektirmeden yerel SQLite veritabanı üzerinden saniyeler içinde çalışır.

---

## Kurulum

```bash
# Debian / Ubuntu
sudo apt-get install wget apt-transport-https gnupg
wget -qO - https://aquasecurity.github.io/trivy-repo/deb/public.key | gpg --dearmor | sudo tee /usr/share/keyrings/trivy.gpg > /dev/null
echo "deb [signed-by=/usr/share/keyrings/trivy.gpg] https://aquasecurity.github.io/trivy-repo/deb generic main" | sudo tee -a /etc/apt/sources.list.d/trivy.list
sudo apt-get update && sudo apt-get install -y trivy

# Arch Linux
sudo pacman -S trivy

# Homebrew (macOS / Linux)
brew install trivy

# Docker konteyneri ile doğrudan çalıştırma
docker run --rm -v /var/run/docker.sock:/var/run/docker.sock aquasec/trivy image alpine:latest
```

---

## Konteyner İmajı Taraması (`trivy image`)

### 1. Basit İmaj Taraması
Yerel veya uzak Docker imajını analiz edin:
```bash
trivy image nginx:latest
```

### 2. Kritiklik Seviyesine Göre Filtreleme
Yalnızca aksiyon alınması gereken Yüksek ve Kritik zafiyetleri listeleyin:
```bash
trivy image --severity HIGH,CRITICAL python:3.9-slim
```

### 3. Yalnızca Yaması Olan Zafiyetleri Gösterme
Henüz üretici tarafından düzeltmesi yayınlanmamış açıkları eleyerek raporu sadeleştirin:
```bash
trivy image --ignore-unfixed --severity HIGH,CRITICAL node:18-alpine
```

### 4. Dışa Aktarılmış Tar Arşivini Tarama
`docker save` ile diske kaydedilmiş çevrim dışı imajları tarayın:
```bash
docker save myapp:v1 -o myapp.tar
trivy image --input myapp.tar
```

---

## Dosya Sistemi ve Depo Taraması (`trivy fs / repo`)

Kaynak kod dizinlerini ve kütüphane bağımlılık kilit dosyalarını (`package-lock.json`, `poetry.lock`, `go.sum`, `pom.xml`) tarayın:

### 1. Yerel Proje Dizinini Tarama
```bash
trivy fs /path/to/project
```

### 2. Uzak Git Deposunu Doğrudan Tarama
```bash
trivy repo https://github.com/example/vulnerable-app
```

---

## Hassas Veri & Parola Tespiti (`--scanners secret`)

Koda veya imaj katmanlarına yanlışlıkla gömülmüş gizli anahtarları yakalayın:

```bash
# Dizin içinde sızdırılmış anahtarları tara
trivy fs --scanners secret .

# İmajda hem zafiyetleri hem de gizli anahtarları birlikte tara
trivy image --scanners vuln,secret sirketim/api:latest
```

---

## Kubernetes Küme Denetimi (`trivy k8s`)

Aktif `kubectl` yetkinizi kullanarak canlı cluster iş yüklerini denetleyin:

### 1. Tüm Küme Özeti
```bash
trivy k8s --report summary cluster
```

### 2. Belirli Bir İsim Alanını (Namespace) Tarama
```bash
trivy k8s --namespace production --severity HIGH,CRITICAL all
```

### 3. CIS Kubernetes Benchmark Uyumluluk Raporu
```bash
trivy k8s --compliance k8s-cis cluster
```

---

## Kod Olarak Altyapı (IaC) Güvenlik Analizi

Dockerfile, Terraform ve Kubernetes manifest dosyalarındaki riskli ayarları bulun:

```bash
# Terraform altyapı kodlarını tara
trivy config ./terraform/

# Dockerfile içindeki root kullanıcı veya güvensiz portları yakala
trivy config Dockerfile
```

---

## CI/CD Boru Hattı & Hata Kodları (`--exit-code`)

Güvensiz yapıların üretime geçmesini engellemek için CI/CD pipeline süreçlerine entegre edin:

```bash
# Kritik açık varsa derlemeyi durdur (Exit code 1)
trivy image --exit-code 1 --severity CRITICAL myapp:build-42

# GitHub Code Scanning için SARIF formatında rapor üret
trivy image --format sarif --output trivy-results.sarif myapp:latest
```

---

## SBOM Üretimi (CycloneDX & SPDX)

Yazılım Tedarik Zinciri ve uyumluluk denetimleri için Yazılım Malzeme Listesi (SBOM) oluşturun:

```bash
# CycloneDX JSON formatında SBOM üret
trivy image --format cyclonedx --output sbom.json myapp:latest

# SPDX JSON formatında SBOM üret
trivy image --format spdx-json --output sbom-spdx.json myapp:latest
```
