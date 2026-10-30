# 🌲 adidnsdump - Kapsamlı Başvuru Rehberi (Cheatsheet)

[🇺🇸 English Version](../README.md)

```
   █████╗ ██████╗ ██╗██████╗ ███╗   ██╗███████╗██████╗ ██╗   ██╗███╗   ███╗██████╗ 
  ██╔══██╗██╔══██╗██║██╔══██╗████╗  ██║██╔════╝██╔══██╗██║   ██║████╗ ████║██╔══██╗
  ███████║██║  ██║██║██║  ██║██╔██╗ ██║███████╗██║  ██║██║   ██║██╔████╔██║██████╔╝
  ██╔══██║██║  ██║██║██║  ██║██║╚██╗██║╚════██║██║  ██║██║   ██║██║╚██╔╝██║██╔═══╝ 
  ██║  ██║██████╔╝██║██████╔╝██║ ╚████║███████║██████╔╝╚██████╔╝██║ ╚═╝ ██║██║     
  ╚═╝  ╚═╝╚═════╝ ╚═╝╚═════╝ ╚═╝  ╚═══╝╚══════╝╚═════╝  ╚═════╝ ╚═╝     ╚═╝╚═╝     
          Active Directory Entegre DNS (ADIDNS) Keşif ve Döküm Aracı
```

<p align="center">
  <img src="https://img.shields.io/badge/Araç-adidnsdump-blue?style=for-the-badge" alt="adidnsdump">
  <img src="https://img.shields.io/badge/Yazar-Dirk--jan_Mollema-orange?style=for-the-badge" alt="dirkjanm">
  <img src="https://img.shields.io/badge/Kategori-Active_Directory-green?style=for-the-badge" alt="Active Directory">
  <img src="https://img.shields.io/badge/Protokol-LDAP_%2F_LDAPS-purple?style=for-the-badge" alt="LDAP">
</p>

<p align="center">
  <b>🌲 DNS bölge transferine (AXFR) ihtiyaç duymadan, LDAP üzerinden Active Directory Entegre DNS (ADIDNS) kayıtlarının tamamını listeleyen ve döken keşif aracı</b>
</p>

---

## 📋 İçindekiler

- [Genel Bakış ve Teknik Altyapı](#-genel-bakış-ve-teknik-altyapı)
- [Kurulum](#-kurulum)
- [Kimlik Doğrulama Yöntemleri](#-kimlik-doğrulama-yöntemleri)
  - [Parola ile Doğrulama](#parola-ile-doğrulama)
  - [Pass-the-Hash (NTLM Hash)](#pass-the-hash-ntlm-hash)
  - [Kerberos Bileti (ccache)](#kerberos-bileti-ccache)
- [Temel Döküm Komutları](#-temel-döküm-komutları)
  - [Standart ADIDNS Kayıt Dökümü](#standart-adidns-kayıt-dökümü)
  - [Silinmiş (Tombstoned) Kayıtları Çıkarma](#silinmiş-tombstoned-kayıtları-çıkarma)
  - [Forest ve Domain DNS Bölgelerini Çıkarma](#forest-ve-domain-dns-bölgelerini-çıkarma)
  - [Belirli Bir DNS Bölgesini Hedefleme](#belirli-bir-dns-bölgesini-hedefleme)
- [Dışa Aktarma ve Sonrası İşlemler](#-dışa-aktarma-ve-sonrası-işlemler)
  - [CSV Çıktısı](#csv-çıktısı)
  - [Nmap ve BloodHound Entegrasyonu](#nmap-ve-bloodhound-entegrasyonu)
- [Hızlı Referans Tablosu](#-hızlı-referans-tablosu)
- [Savunma Yöntemleri ve Tespit](#-savunma-yöntemleri-ve-tespit)
- [Yasal Uyarı](#-yasal-uyarı)

---

## 🎯 Genel Bakış ve Teknik Altyapı

Standart Windows Active Directory ortamlarında DNS sunucusu genellikle etki alanıyla entegre çalışır (**ADIDNS - Active Directory-Integrated DNS**). Bu mimaride DNS kayıtları düz metin dosyalarında değil, Active Directory bölümündeki LDAP nesneleri olarak saklanır (`DC=DomainDnsZones` ve `DC=ForestDnsZones`).

Geleneksel DNS bölge transferleri (`AXFR`) Domain Controller makinelerinde neredeyse her zaman devre dışıdır. Ancak, **herhangi bir etki alanı kullanıcısı** (en düşük yetkili kullanıcı veya ele geçirilmiş bir servis hesabı dahi), varsayılan olarak bu LDAP dizinindeki DNS kayıtlarını okuma iznine sahiptir.

**Dirk-jan Mollema (@dirkjanm)** tarafından geliştirilen **`adidnsdump`**, Domain Controller'a LDAP veya LDAPS protokolü üzerinden bağlanır, ADIDNS bölümlerini sorgular ve tüm kayıtları (A, AAAA, CNAME, SRV, TXT, MX ve silinmiş tombstone kayıtları) çekerek etki alanındaki tüm sunucu, iş istasyonu ve iç web servislerinin eksiksiz bir envanterini sunar.

---

## 📥 Kurulum

```bash
# Depoyu klonlayıp pip ile kurma
git clone https://github.com/dirkjanm/adidnsdump.git
cd adidnsdump
pip install .

# Veya doğrudan pipx / pip ile kurulum
pipx install git+https://github.com/dirkjanm/adidnsdump.git
```

---

## 🔑 Kimlik Doğrulama Yöntemleri

### Parola ile Doğrulama
```bash
adidnsdump -u 'CORP\jdoe' -p 'P@ssw0rd123!' 192.168.1.10
```

### Pass-the-Hash (NTLM Hash)
Mimikatz veya Secretsdump ile NTLM hash'i elde edilmişse:
```bash
adidnsdump -u 'CORP\jdoe' --hashes :31d6cfe0d16ae931b73c59d7e0c089c0 192.168.1.10
```

### Kerberos Bileti (ccache)
Ortamda geçerli bir Kerberos TGT/TGS bileti varsa (`KRB5CCNAME`):
```bash
export KRB5CCNAME=/tmp/jdoe.ccache
adidnsdump -k -u 'CORP\jdoe' dc01.corp.local
```

---

## 🚀 Temel Döküm Komutları

### Standart ADIDNS Kayıt Dökümü
Varsayılan DomainDnsZones bölümündeki tüm etkin DNS kayıtlarını döker:
```bash
adidnsdump -u 'corp.local\jdoe' -p 'P@ssw0rd123!' ldap://dc01.corp.local
```
Bu işlem mevcut çalışma dizininde `records.csv` isimli bir dosya oluşturur.

### Silinmiş (Tombstoned) Kayıtları Çıkarma
Windows DNS, silinen kayıtları hemen yok etmez; birkaç gün boyunca "tombstoned" durumunda tutar. Bu kayıtlar eski sunucuları, hizmet dışı kalmış sistemleri veya test ortamlarını açığa çıkarabilir:
```bash
adidnsdump -u 'corp.local\jdoe' -p 'P@ssw0rd123!' --include-tombstoned ldap://dc01.corp.local
```

### Forest ve Domain DNS Bölgelerini Çıkarma
Orman (Forest) seviyesindeki DNS bölümlerini de sorgulamak için:
```bash
adidnsdump -u 'corp.local\jdoe' -p 'P@ssw0rd123!' --forest ldap://dc01.corp.local
```

### Belirli Bir DNS Bölgesini Hedefleme
Hedef ağda birden fazla alt etki alanı veya özel DNS bölgesi bulunuyorsa:
```bash
adidnsdump -u 'corp.local\jdoe' -p 'P@ssw0rd123!' --zone dev.corp.local ldap://dc01.corp.local
```

### Güvenli LDAPS Şifreli Bağlantı
Kimlik bilgilerinin ağda açık metin gitmesini engellemek için:
```bash
adidnsdump --ssl -u 'corp.local\jdoe' -p 'P@ssw0rd123!' ldaps://dc01.corp.local
```

---

## 📊 Dışa Aktarma ve Sonrası İşlemler

### CSV Çıktısı
`records.csv` dosyası şu sütunları içerir:
- `zone`: Kaydın ait olduğu DNS bölgesi.
- `name`: Makine veya kayıt adı.
- `type`: Kayıt türü (A, AAAA, CNAME, SRV vb.).
- `value`: Hedef IP adresi veya alan adı.

### Nmap ve BloodHound Entegrasyonu
Çıkarılan A kayıtlarından benzersiz IP listesi oluşturup hızlıca tarama başlatma:
```bash
# Yalnızca A kayıtlarındaki IP adreslerini ayıklama
awk -F',' '$3=="A" {print $4}' records.csv | sort -u > live_ips.txt

# Keşfedilen iç ağ hedeflerine hızlı port taraması
nmap -sS -Pn -iL live_ips.txt -T4 -p 80,443,445,3389 -oA internal_assets
```

---

## ⚡ Hızlı Referans Tablosu

| Amaç | Komut |
|------|-------|
| **Standart ADIDNS Dökümü** | `adidnsdump -u 'DOMAIN\User' -p 'Pass' <DC_IP>` |
| **Pass-the-Hash (NTLM)** | `adidnsdump -u 'DOMAIN\User' --hashes :<NTLM> <DC_IP>` |
| **Kerberos Bilet Doğrulama** | `adidnsdump -k -u 'DOMAIN\User' <DC_FQDN>` |
| **Silinmiş (Tombstone) Kayıtlar** | `adidnsdump -u 'DOMAIN\User' -p 'Pass' --include-tombstoned <DC_IP>` |
| **Forest Bölgelerini Tarama** | `adidnsdump -u 'DOMAIN\User' -p 'Pass' --forest <DC_IP>` |
| **Özel DNS Bölgesi Belirtme** | `adidnsdump -u 'DOMAIN\User' -p 'Pass' --zone <BÖLGE> <DC_IP>` |
| **LDAPS (SSL) Zorlama** | `adidnsdump --ssl -u 'DOMAIN\User' -p 'Pass' ldaps://<DC_IP>` |

---

## 🛡️ Savunma Yöntemleri ve Tespit

1. **LDAP Sorgu İzleme:** `CN=MicrosoftDNS,DC=DomainDnsZones` kapsayıcısına yönelik yoğun ve toplu LDAP sorgularını tespit etmek için Gelişmiş Denetim İlkesi (Directory Service Access - Event ID 4662) yapılandırılmalıdır.
2. **ADIDNS ACL Sıkılaştırma:** Standart kullanıcıların iç DNS nesnelerini topluca okuması zorunlu değilse, `MicrosoftDNS` kapsayıcısındaki okuma izinleri kısıtlanabilir.
3. **Honeytoken DNS Kayıtları:** Sahte ve çekici isimlerde (örneğin `backup-vault.corp.local` veya `admin-portal.corp.local`) bal küpü (honeypot) IP adreslerine yönlenen kayıtlar oluşturarak keşif aktiviteleri erkenden yakalanabilir.

---

## ⚠️ Yasal Uyarı

> [!CAUTION]
> adidnsdump aracı yalnızca yasal izin alınmış sızma testleri, iç ağ denetimleri ve Active Directory güvenlik analizlerinde kullanılmalıdır. İzin alınmayan sistemler üzerinde dizin sorgulaması yapmak yasalara aykırıdır.
