# 🐶 Snaffler - Kapsamlı Başvuru Kılavuzu

[🇬🇧 English Version](../README.md)

```
  ███████╗███╗   ██╗ █████╗ ███████╗███████╗██╗     ███████╗██████╗ 
  ██╔════╝████╗  ██║██╔══██╗██╔════╝██╔════╝██║     ██╔════╝██╔══██╗
  ███████╗██╔██╗ ██║███████║█████╗  █████╗  ██║     █████╗  ██████╔╝
  ╚════██║██║╚██╗██║██╔══██║██╔══╝  ██╔══╝  ██║     ██╔══╝  ██╔══██╗
  ███████║██║ ╚████║██║  ██║██║     ██║     ███████╗███████╗██║  ██║
  ╚══════╝╚═╝  ╚═══╝╚═╝  ╚═╝╚═╝     ╚═╝     ╚══════╝╚══════╝╚═╝  ╚═╝
       Active Directory SMB Paylaşımlarında Kimlik Bilgisi ve Gizli Dosya Avcısı
```

<p align="center">
  <img src="https://img.shields.io/badge/Snaffler-Gizli_Bilgi_Avcısı-blue?style=for-the-badge" alt="Snaffler">
  <img src="https://img.shields.io/badge/Yazıldığı_Dil-C%23_/_NET-orange?style=for-the-badge" alt="C# / .NET">
  <img src="https://img.shields.io/badge/Kategori-Active_Directory_&_Ağ-green?style=for-the-badge" alt="Active Directory">
  <img src="https://img.shields.io/badge/Lisans-GPLv3-red?style=for-the-badge" alt="Lisans GPLv3">
</p>

<p align="center">
  <b>🌐 Windows Active Directory etki alanındaki SMB ağ paylaşımlarını otomatik tarayarak KeePass veritabanlarını, yapılandırma dosyalarını ve açık metin parolaları bulan araç</b>
</p>

---

## 📋 İçindekiler

- [Genel Bakış & Paylaşım Avcılığı Mantığı](#-genel-bakış--paylaşım-avcılığı-mantığı)
- [Snaffler Nasıl Çalışır?](#-snaffler-nasıl-çalışır)
- [Kurulum & Çalıştırma Yöntemleri](#-kurulum--çalıştırma-yöntemleri)
- [Bulgu Önem Seviyeleri (Triage Levels)](#-bulgu-önem-seviyeleri-triage-levels)
- [Yaygın Tarama Senaryoları](#-yaygın-tarama-senaryoları)
  - [Domain Çapında Genel Tarama](#domain-çapında-genel-tarama)
  - [Belirli Sunucu veya Paylaşımları Hedefleme](#belirli-sunucu-veya-paylaşımları-hedefleme)
  - [Belirli Bilgisayarları Hariç Tutma](#belirli-bilgisayarları-hariç-tutma)
- [En Kritik Hedef Dosyalar & Uzantılar](#-en-kritik-hedef-dosyalar--uzantılar)
- [Raporlama & Çıktı Analizi](#-raporlama--çıktı-analizi)
- [Hızlı Referans Tablosu](#-hızlı-referans-tablosu)
- [Savunma Açısından Değerlendirme & OPSEC](#-savunma-açısından-değerlendirme--opsec)
- [Yasal Uyarı](#-yasal-uyarı)

---

## 🎯 Genel Bakış & Paylaşım Avcılığı Mantığı

Orta ve büyük ölçekli Active Directory ortamlarında, kurum içi dosya sunucularında binlerce açık SMB ağ paylaşımı yer alır. Sistem yöneticileri ve çalışanlar bu paylaşımlarda sıklıkla şunları unutur:

- 🔑 **KeePass Veritabanları (`.kdbx`):** Kurum parolalarını barındıran şifreli kasalar.
- ⚙️ **Yapılandırma Dosyaları (`web.config`, `appsettings.json`):** Açık metin SQL bağlantı dizgileri ve servis hesabı parolaları.
- 📜 **Kurulum Betikleri (`unattend.xml`, `.ps1`, `.bat`):** Otomatik Windows kurulumlarından kalan yerel admin parolaları.
- 🔏 **Özel Sertifikalar (`.pfx`, `.p12`, `.pem`, `.ppk`):** Yetki yükseltme ve yanal hareket için kullanılabilecek anahtarlar.

Binlerce klasörü elle gezmek imkansızdır. **l33tb455** tarafından geliştirilen **Snaffler**, LDAP üzerinden domain bilgisayarlarını sorgular, okunabilir paylaşımları bulur ve regex kurallarıyla kritik dosyaları anında raporlar.

---

## ⚙️ Snaffler Nasıl Çalışır?

```
[ Ele Geçirilen Domain Bilgisayarı ]
         │
         ├── 1. LDAP ile Domain Controller'dan Bilgisayar Nesnelerini Çeker
         ├── 2. Bulunan makinelerin 445 (SMB) portuna bağlanır
         ├── 3. C$, SYSVOL, NETLOGON ve özel paylaşımların izinlerini kontrol eder
         └── 4. Dosya isimlerini, uzantılarını ve içeriklerini kurallarla eşleştirir
```

---

## 📥 Kurulum & Çalıştırma Yöntemleri

Snaffler C# ile yazılmış bağımsız bir .NET ikilisidir:

```bash
# Kaynak koddan derleme
git clone https://github.com/SnaffCon/Snaffler.git
cd Snaffler
dotnet build -c Release
```

### Windows Üzerinde Çalıştırma:
```cmd
# Ekrana canlı çıktı basarak ve dosyaya kaydederek çalıştırma
Snaffler.exe -s -o snaffler_log.txt
```

### Cobalt Strike / C2 Üzerinden Bellekte Çalıştırma:
```text
beacon> execute-assembly C:\Tools\Snaffler.exe -s -o C:\Temp\loot.txt
```

---

## 🚦 Bulgu Önem Seviyeleri (Triage Levels)

- 🔴 **`{Red}` / Black (Kırmızı):** En yüksek değerdeki bulgular! Açık metin parolalar, özel sertifikalar (`.pfx`, `.id_rsa`), AWS token'ları, kurulum xml dosyaları.
- 🟡 **`{Yellow}` (Sarı):** Yüksek olasılıklı bulgular. Parola kelimesi geçen yapılandırma dosyaları, veritabanı bağlantıları.
- 🟢 **`{Green}` (Yeşil):** Bilgilendirme. İlgi çekici dosya isimleri (`parolalar.xlsx`, `vpn.ovpn`).

---

## 🚀 Yaygın Tarama Senaryoları

### Domain Çapında Genel Tarama

```cmd
# Tüm domain paylaşımlarını canlı konsol akışıyla tarama
Snaffler.exe -s -o C:\loot\snaffler_domain.txt

# İş parçacığı sayısını artırarak hızlı tarama (Varsayılan: 60)
Snaffler.exe -s -t 100 -o C:\loot\snaffler_hizli.txt
```

### Belirli Sunucu veya Paylaşımları Hedefleme

```cmd
# Tek bir dosya sunucusunu tarama
Snaffler.exe -c fs01.corp.local -s

# Virgülle ayrılmış kritik sunucuları tarama
Snaffler.exe -c dc01.corp.local,fs02.corp.local,sql01.corp.local -s

# Doğrudan belirli bir SMB paylaşım dizinini tarama
Snaffler.exe -i \\fs01.corp.local\Departmanlar\BilgiIslem\ -s
```

### Belirli Bilgisayarları Hariç Tutma

```cmd
# Yedekleme ve kamera sunucularını taramadan hariç tutma
Snaffler.exe -s -c- backup01.corp.local,kamera01.corp.local
```

---

## 🎯 En Kritik Hedef Dosyalar & Uzantılar

| Dosya / Uzantı | Bulunabilecek Kritik Veri |
|----------------|---------------------------|
| `.kdbx` / `.kdb` | KeePass parola yöneticisi kasaları |
| `web.config` / `app.config` | Açık veritabanı parolaları |
| `unattend.xml` / `sysprep.inf` | Windows yerel yönetici parolaları |
| `.ppk` / `.pem` / `id_rsa` | SSH özel anahtarları |
| `.pfx` / `.p12` | Active Directory kullanıcı/makine sertifikaları |
| `.rdp` / `.vnc` | Uzak masaüstü bağlantı profilleri |
| `passwords.xlsx` / `creds.txt` | Çalışanların parola listeleri |

---

## 📊 Raporlama & Çıktı Analizi

```bash
# Linux terminalinde kırmızı (RED) seviyedeki kritik parolaları süzme
grep -i "{Red}" snaffler_domain.txt

# Bulunan tüm KeePass dosyalarını listeleme
grep -i "\.kdbx" snaffler_domain.txt

# Veritabanı parolalarını yakalama
grep -i "password=" snaffler_domain.txt
```

---

## ⚡ Hızlı Referans Tablosu

| İşlem | Komut |
|-------|-------|
| **Domain Genelini Tara** | `Snaffler.exe -s -o cikti.log` |
| **Tek Bilgisayarı Tara** | `Snaffler.exe -c <HOST> -s` |
| **Tek Paylaşımı Tara** | `Snaffler.exe -i \\<HOST>\<PAYLASIM> -s` |
| **Thread Sayısını Artır** | `Snaffler.exe -s -t 120 -o cikti.log` |
| **Bilgisayar Hariç Tut** | `Snaffler.exe -s -c- <HARIC_HOST>` |
| **Maksimum Dosya Boyutu Sınırı**| `Snaffler.exe -s -z 5000000` *(Maks 5 MB dosya oku)* |
| **C2 Assembly ile Çalıştır** | `execute-assembly Snaffler.exe -s` |

---

## 🛡️ Savunma Açısından Değerlendirme & OPSEC

1. **Ağda Aşırı SMB Trafiği:** Snaffler dakikalar içinde tüm bilgisayarların 445 portuna bağlanır. Bu durum SIEM'de binlerce Event ID 4624 (Ağ Oturum Açma) ve Event ID 5140 (Paylaşıma Erişim) logu üretir.
2. **Yem Dosyalar (Canary / Honeyfiles):** Savunucuların paylaşımlara yerleştirdiği tuzak `.kdbx` dosyaları açıldığında SOC ekiplerine anında alarm gönderir.
3. **Maksimum Dosya Boyutu:** Ağın tıkanmaması için `-z` bayrağı ile taranacak dosyaların boyutu sınırlandırılmalıdır (örn. 5MB).

---

## ⚠️ Yasal Uyarı

> [!CAUTION]
> Snaffler yalnızca yetkili iç ağ sızma testleri, Red Team tatbikatları ve kurumsal denetimler için geliştirilmiştir. İzin alınmaksızın ağ paylaşımlarını taramak yasalara aykırıdır.