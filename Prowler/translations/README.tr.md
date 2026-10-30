# 🌩️ Prowler - Kapsamlı Başvuru Rehberi (Cheatsheet)

[🇺🇸 English Version](../README.md)

```
  ██████╗ ██████╗  ██████╗ ██╗    ██╗██╗     ███████╗██████╗ 
  ██╔══██╗██╔══██╗██╔═══██╗██║    ██║██║     ██╔════╝██╔══██╗
  ██████╔╝██████╔╝██║   ██║██║ █╗ ██║██║     █████╗  ██████╔╝
  ██╔═══╝ ██╔══██╗██║   ██║██║███╗██║██║     ██╔══╝  ██╔══██╗
  ██║     ██║  ██║╚██████╔╝╚███╔███╔╝███████╗███████╗██║  ██║
  ╚═╝     ╚═╝  ╚═╝ ╚═════╝  ╚══╝╚══╝ ╚══════╝╚══════╝╚═╝  ╚═╝
     Çoklu Bulut Güvenlik Duruşu Değerlendirme & CIS Uyumluluk Denetçisi
```

<p align="center">
  <img src="https://img.shields.io/badge/Prowler-Bulut_Güvenliği-blue?style=for-the-badge" alt="Prowler">
  <img src="https://img.shields.io/badge/Geliştirici-Toni_de_la_Fuente-orange?style=for-the-badge" alt="toniblyx">
  <img src="https://img.shields.io/badge/Bulutlar-AWS_%7C_Azure_%7C_GCP_%7C_K8s-gold?style=for-the-badge" alt="Multi-Cloud">
  <img src="https://img.shields.io/badge/Uyumluluk-CIS_Benchmark-green?style=for-the-badge" alt="CIS">
</p>

<p align="center">
  <b>🌩️ AWS, Microsoft Azure, Google Cloud (GCP) ve Kubernetes ortamlarında güvenlik duruşu değerlendirmesi, yanlış yapılandırma denetimi ve mevzuat uyumluluğu için endüstri standardı açık kaynaklı araç</b>
</p>

---

## 📋 İçindekiler

- [Genel Bakış ve Mimari](#-genel-bakış-ve-mimari)
- [Kurulum](#-kurulum)
- [AWS Güvenlik Denetimi](#-aws-güvenlik-denetimi)
  - [Varsayılan Hesap Taraması](#varsayılan-hesap-taraması)
  - [Belirli Servisleri Hedefleme (S3, IAM, EC2)](#belirli-servisleri-hedefleme-s3-iam-ec2)
  - [Belirli Bölgeleri (Regions) Tarama](#belirli-bölgeleri-regions-tarama)
  - [AWS Organizations ile Çoklu Hesap Denetimi](#aws-organizations-ile-çoklu-hesap-denetimi)
- [Azure ve Google Cloud (GCP) Denetimi](#-azure-ve-google-cloud-gcp-denetimi)
  - [Microsoft Azure Güvenlik Analizi](#microsoft-azure-güvenlik-analizi)
  - [Google Cloud Platform (GCP) Analizi](#google-cloud-platform-gcp-analizi)
- [Uyumluluk ve Mevzuat Çerçeveleri (CIS, ISO27001, PCI-DSS)](#-uyumluluk-ve-mevzuat-çerçeveleri-cis-iso27001-pci-dss)
- [Raporlama ve Çıktı Formatları (HTML, JSON, CSV)](#-raporlama-ve-çıktı-formatları-html-json-csv)
- [Hızlı Referans Tablosu](#-hızlı-referans-tablosu)
- [Savunma Yöntemleri ve İyileştirme](#-savunma-yöntemleri-ve-iyileştirme)
- [Yasal Uyarı](#-yasal-uyarı)

---

## 🎯 Genel Bakış ve Mimari

**Toni de la Fuente (@toniblyx)** tarafından geliştirilen **Prowler**, modern çoklu bulut (multi-cloud) altyapılarında en yaygın kullanılan güvenlik denetim aracıdır:
- Herkese açık (public) S3 bucket veya depolama hesaplarını tespit eder.
- Çok faktörlü doğrulaması (MFA) bulunmayan root ve yönetici hesaplarını listeler.
- Aşırı yetkili (`*` joker karakterli) IAM ilkelerini analiz eder.
- Şifrelenmemiş veritabanları, diskler ve yedekleri bulur.
- Dış dünyaya (`0.0.0.0/0`) açık yönetim portlarını (SSH 22, RDP 3389) işaretler.
- CIS Benchmarks, ISO 27001, HIPAA, PCI-DSS ve GDPR standartlarına uyumluluğu ölçer.

---

## 📥 Kurulum

```bash
# pip veya pipx ile kurulum (Önerilen)
pipx install prowler
# Veya
pip install prowler

# Docker ile Çalıştırma
docker pull prowlercloud/prowler:latest

# Kurulumu doğrulama
prowler -v
```

---

## ☁️ AWS Güvenlik Denetimi

Ortamınızda geçerli AWS kimlik bilgilerinin tanımlı olduğundan emin olun (`aws configure`, ortam değişkenleri veya IAM rolü):

### Varsayılan Hesap Taraması
Tüm aktif servislerde ve bölgelerde kapsamlı tarama yürütür:
```bash
prowler aws
```

### Belirli Servisleri Hedefleme (S3, IAM, EC2)
```bash
# Yalnızca S3 kova kontrollerini çalıştır
prowler aws --services s3

# IAM ve EC2 kontrollerini birlikte çalıştır
prowler aws --services iam,ec2
```

### Belirli Bölgeleri (Regions) Tarama
Kullanılmayan boş bölgeleri atlayarak tarama hızını büyük ölçüde artırın:
```bash
prowler aws --region us-east-1,eu-west-1
```

### AWS Organizations ile Çoklu Hesap Denetimi
Merkezi bir denetim rolüne bürünerek (AssumeRole) kuruluştaki tüm hesapları otomatik tara:
```bash
prowler aws --sp-role-arn arn:aws:iam::111122223333:role/SecurityAuditRole
```

---

## 🔷 Azure ve Google Cloud (GCP) Denetimi

### Microsoft Azure Güvenlik Analizi
Azure CLI ile giriş yapın (`az login`):
```bash
# Tüm Azure aboneliğini tara
prowler azure --subscription-ids "00000000-0000-0000-0000-000000000000"

# Belirli servisleri (Storage, Entra ID / IAM) hedefle
prowler azure --services storage,iam
```

### Google Cloud Platform (GCP) Analizi
Google Cloud SDK ile oturum açın (`gcloud auth application-default login`):
```bash
# Belirli bir GCP projesini tara
prowler gcp --project-ids kurumsal-proje-id

# Yalnızca Cloud Storage servislerini denetle
prowler gcp --services storage
```

---

## 📜 Uyumluluk ve Mevzuat Çerçeveleri (CIS, ISO27001, PCI-DSS)

Prowler kontrolleri resmi endüstri standartlarına göre sınıflandırır:

```bash
# CIS AWS Foundations Benchmark v3.0 denetimi
prowler aws --compliance cis_3.0_aws

# PCI-DSS Kartlı Ödeme Güvenliği denetimi
prowler aws --compliance pci_3.2.1_aws

# ISO 27001 Bilgi Güvenliği denetimi
prowler aws --compliance iso27001_2013_aws

# Desteklenen tüm mevzuatları listele
prowler aws --list-compliance
```

---

## 📊 Raporlama ve Çıktı Formatları (HTML, JSON, CSV)

Prowler sonuçları varsayılan olarak `output/` dizinine kaydeder:
- **Etkileşimli HTML:** Başarılı, başarısız ve kritik bulguları görsel grafiklerle özetleyen web raporu.
- **CSV:** Excel veya bilet yönetim sistemlerine (Jira) aktarmak için ideal format.
- **JSON-ASFF / JSON-OCSF:** AWS Security Hub ve kurumsal SIEM sistemleriyle uyumlu veri akışı.

```bash
# Özel bir klasöre HTML, JSON ve CSV formatında rapor üretme
prowler aws -M html,json,csv -o /path/to/raporlar/
```

---

## ⚡ Hızlı Referans Tablosu

| Amaç | Komut |
|------|-------|
| **Tam AWS Denetimi** | `prowler aws` |
| **Belirli Servisleri Tara** | `prowler aws --services s3,iam` |
| **Tek Bölge Tara** | `prowler aws --region us-east-1` |
| **CIS Benchmark Denetimi** | `prowler aws --compliance cis_3.0_aws` |
| **Tam Azure Denetimi** | `prowler azure --subscription-ids <SUB_ID>` |
| **Tam GCP Denetimi** | `prowler gcp --project-ids <PROJECT_ID>` |
| **HTML/CSV/JSON Dışa Aktar** | `prowler aws -M html,csv,json -o ./raporlar` |
| **Tüm Kontrolleri Listele** | `prowler aws --list-checks` |

---

## 🛡️ Savunma Yöntemleri ve İyileştirme

1. **Otomatik İyileştirme:** Prowler raporundaki bulgular, tespit edilen güvenlik açığını kapatmak için gereken AWS CLI komutlarını veya Terraform parametrelerini doğrudan sunar.
2. **AWS Security Hub Entegrasyonu:** Prowler JSON-ASFF çıktılarını AWS Security Hub servisine besleyerek merkezi tehdit izleme paneli oluşturabilirsiniz.

---

## ⚠️ Yasal Uyarı

> [!CAUTION]
> Prowler bir güvenlik duruşu ve denetim aracıdır. Üretim ortamlarında veya müşteri hesaplarında tarama çalıştırmadan önce yetkili sistem sahiplerinden yazılı onay alındığından emin olun.
