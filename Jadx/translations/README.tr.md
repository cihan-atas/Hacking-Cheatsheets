# 🔍 Jadx & Apktool - Kapsamlı Başvuru Rehberi (Cheatsheet)

[🇺🇸 English Version](../README.md)

```
       ██╗ █████╗ ██████╗ ██╗  ██╗    ███████╗    █████╗ ██████╗ ██╗  ██╗████████╗ ██████╗  ██████╗ ██╗     
       ██║██╔══██╗██╔══██╗╚██╗██╔╝    ██╔════╝   ██╔══██╗██╔══██╗██║ ██╔╝╚══██╔══╝██╔═══██╗██╔═══██╗██║     
       ██║███████║██║  ██║ ╚███╔╝     ███████╗   ███████║██████╔╝█████╔╝    ██║   ██║   ██║██║   ██║██║     
  ██   ██║██╔══██║██║  ██║ ██╔██╗     ╚════██║   ██╔══██║██╔═══╝ ██╔═██╗    ██║   ██║   ██║██║   ██║██║     
  ╚█████╔╝██║  ██║██████╔╝██╔╝ ██╗    ███████║██╗██║  ██║██║     ██║  ██╗   ██║   ╚██████╔╝╚██████╔╝███████╗
   ╚════╝ ╚═╝  ╚═╝╚═════╝ ╚═╝  ╚═╝    ╚══════╝╚═╝╚═╝  ╚═╝╚═╝     ╚═╝  ╚═╝   ╚═╝    ╚═════╝  ╚═════╝ ╚══════╝
           Android APK Tersine Mühendislik, Smali Yamalama ve Yeniden Paketleme
```

<p align="center">
  <img src="https://img.shields.io/badge/Jadx-DEX'ten_Java'ya-blue?style=for-the-badge" alt="Jadx">
  <img src="https://img.shields.io/badge/Apktool-Smali_Çözücü-orange?style=for-the-badge" alt="Apktool">
  <img src="https://img.shields.io/badge/Kategori-Mobil_Tersine_Mühendislik-green?style=for-the-badge" alt="Mobile RE">
  <img src="https://img.shields.io/badge/Platform-Android-purple?style=for-the-badge" alt="Android">
</p>

<p align="center">
  <b>🔍 Android uygulamalarını tersine mühendislik ile analiz etme: Jadx ile APK/DEX dosyalarını okunabilir Java kaynak koduna dönüştürme; Apktool ile Smali kodlarını ayrıştırma, düzenleme ve yeniden imzalama</b>
</p>

---

## 📋 İçindekiler

- [Genel Bakış ve Mimari](#-genel-bakış-ve-mimari)
- [Kurulum](#-kurulum)
- [Jadx: Java Kaynak Koduna Geri Derleme](#-jadx-java-kaynak-koduna-geri-derleme)
  - [Jadx Grafiksel Arayüz (GUI) Modu](#jadx-grafiksel-arayüz-gui-modu)
  - [Jadx CLI Toplu Dışa Aktarma](#jadx-cli-toplu-dışa-aktarma)
  - [Kritik Varlıkların ve Manifest Dosyasının İncelenmesi](#kritik-varlıkların-ve-manifest-dosyasının-incelenmesi)
- [Apktool: Ayrıştırma ve Smali Kod Çıkarma](#-apktool-ayrıştırma-ve-smali-kod-çıkarma)
  - [Smali ve Kaynak Dosyalarına Ayrıştırma](#smali-ve-kaynak-dosyalarına-ayrıştırma)
  - [Smali Kodlarını İnceleme ve Yamalama](#smali-kodlarını-inceleme-ve-yamalama)
  - [Network Security Config ile Burp Sertifikası Ekleme](#network-security-config-ile-burp-sertifikası-ekleme)
- [Yamalanan APK'yı Yeniden Derleme, Hizalama ve İmzalama](#-yamalanan-apkyı-yeniden-derleme-hizalama-ve-imzalama)
  - [1. Apktool ile Yeniden Derleme](#1-apktool-ile-yeniden-derleme)
  - [2. Keystore Sertifikası Üretme](#2-keystore-sertifikası-üretme)
  - [3. Zipalign ile Bellek Hizalaması](#3-zipalign-ile-bellek-hizalaması)
  - [4. Apksigner ile İmzalama](#4-apksigner-ile-imzalama)
- [Hızlı Referans Tablosu](#-hızlı-referans-tablosu)
- [Savunma Yöntemleri ve Kod Karıştırma (Obfuscation)](#-savunma-yöntemleri-ve-kod-karıştırma-obfuscation)
- [Yasal Uyarı](#-yasal-uyarı)

---

## 🎯 Genel Bakış ve Mimari

Android uygulamaları (`.apk`), derlenmiş Dalvik baytkodu (`classes.dex`), kaynak tabloları (`resources.arsc`), ikili XML manifest (`AndroidManifest.xml`) ve yerel C/C++ kütüphanelerinden (`lib/`) oluşan sıkıştırılmış bir ZIP arşividir.

Android güvenlik analizinde iki temel tamamlayıcı araç kullanılır:
1. **Jadx (Dex to Java decompiler):** Dalvik baytkodunu doğrudan insan tarafından okunabilir, temiz Java koduna dönüştürür. Çapraz referans (xref) arama, değişken/fonksiyon isimlendirme ve deobfuscation desteğiyle analiz hızını katlar.
2. **Apktool:** APK kaynaklarını (XML, resimler, değerler) neredeyse orijinal haline döndürür ve DEX baytkodunu **Smali** koduna dönüştürür. Mantıksal akışları düzenlemeye, kod yamamaya ve uygulayı tekrar paketlemeye olanak tanır.

---

## 📥 Kurulum

```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y jadx apktool apksigner zipalign

# macOS (Homebrew)
brew install jadx apktool android-commandlinetools

# Arch Linux
sudo pacman -S jadx apktool
```

---

## ☕ Jadx: Java Kaynak Koduna Geri Derleme

### Jadx Grafiksel Arayüz (GUI) Modu
Görsel olarak APK incelemesi yapmak için:
```bash
jadx-gui hedef.apk
```
*Jadx-GUI Kısayolları:*
- `Ctrl + Shift + F`: Global metin, sınıf ve fonksiyon araması.
- `R`: Çapraz Referans (Cross-Reference) bulma (seçili fonksiyon nerede çağrılıyor?).
- `N`: Değişken veya fonksiyonu yeniden adlandırma (tüm projede refactor eder).

### Jadx CLI Toplu Dışa Aktarma
Grafik arayüz açmadan kaynak kodları bir klasöre dökmek:
```bash
# Tüm APK'yı src/ klasörüne Java olarak dökme
jadx -d cikti_src hedef.apk

# Kaynakları ayıklamadan sadece Java kodunu çıkarma (çok daha hızlı)
jadx -r -d cikti_src hedef.apk

# İsimleri gizlenmiş değişkenleri otomatik deobfuscate etme (a, b, c -> p001_a)
jadx --deobf -d cikti_deobf hedef.apk
```

### Kritik Varlıkların ve Manifest Dosyasının İncelenmesi
- `resources/AndroidManifest.xml`: Dışa açık (exported) activity, service ve broadcast receiver bileşenlerini tespit edin.
- `resources/res/values/strings.xml`: Kod içine gömülü unutulmuş API anahtarları, sunucu adresleri ve gizli parolaları arayın.

---

## 🔨 Apktool: Ayrıştırma ve Smali Kod Çıkarma

### Smali ve Kaynak Dosyalarına Ayrıştırma
```bash
# APK dosyasını klasöre ayrıştırma
apktool d hedef.apk -o apk_calisma

# Hatalı kaynakları yoksayarak ayrıştırma
apktool d -r hedef.apk -o apk_calisma
```

Oluşan `apk_calisma` dizininde:
- `smali/`: Dalvik assembly (Smali) kodları.
- `res/`: Açık XML kaynakları, dizgiler ve tasarımlar.
- `AndroidManifest.xml`: Okunabilir XML manifesti.

### Smali Kodlarını İnceleme ve Yamalama
Uygulama içerisindeki bir kontrolü atlatmak için (örneğin `isRooted()` veya `checkLicense()`):
İlgili `.smali` dosyasını açıp fonksiyonu bulun:

```smali
.method public isRooted()Z
    .registers 2

    # Orijinal denetim kodları...
    # Her zaman false (0x0) döndür
    const/4 v0, 0x0
    return v0
.end method
```
`const/4 v0, 0x1` değerini `0x0` yaparak metodun her zaman `false` dönmesini sağlayabilirsiniz.

### Network Security Config ile Burp Sertifikası Ekleme
Android 7+ (API 24+) sürümlerinde kullanıcı sertifikalarına güvenilmediği için Burp Suite trafiğini yakalayamazsınız. Bunu aşmak için:
1. `AndroidManifest.xml` dosyasında `application` etiketine ekleyin:
   ```xml
   android:networkSecurityConfig="@xml/network_security_config"
   ```
2. `res/xml/network_security_config.xml` dosyasını oluşturup kullanıcı sertifikalarına izin verin:
   ```xml
   <?xml version="1.0" encoding="utf-8"?>
   <network-security-config>
       <base-config>
           <trust-anchors>
               <certificates src="system" />
               <certificates src="user" />
           </trust-anchors>
       </base-config>
   </network-security-config>
   ```

---

## 🔄 Yamalanan APK'yı Yeniden Derleme, Hizalama ve İmzalama

Android, imzalanmamış ve bellek hizalaması yapılmamış APK'ların kurulmasına izin vermez.

### 1. Apktool ile Yeniden Derleme
```bash
apktool b apk_calisma -o degistirilmis.apk
```

### 2. Keystore Sertifikası Üretme (Tek Seferlik)
```bash
keytool -genkey -v -keystore test-anahtar.jks -alias test \
  -keyalg RSA -keysize 2048 -validity 10000 -storepass parola123 -keypass parola123 \
  -dname "CN=Test, OU=Audit, O=Security, L=City, S=State, C=TR"
```

### 3. Zipalign ile Bellek Hizalaması
Bellek verimini artırmak için 4-byte hizalama uygulayın:
```bash
zipalign -p -f -v 4 degistirilmis.apk hizalanmis.apk
```

### 4. Apksigner ile İmzalama
APK Signature v2/v3 şemasıyla imzalama:
```bash
apksigner sign --ks test-anahtar.jks --ks-pass pass:parola123 --out son_imzali.apk hizalanmis.apk

# İmzayı doğrulama
apksigner verify son_imzali.apk
```

Cihaza yükleme:
```bash
adb install -r son_imzali.apk
```

---

## ⚡ Hızlı Referans Tablosu

| Amaç | Komut |
|------|-------|
| **Grafik Arayüzle Decompile** | `jadx-gui <hedef.apk>` |
| **CLI ile Java Dışa Aktarma** | `jadx -d cikti_dizin <hedef.apk>` |
| **CLI Deobfuscation Modu** | `jadx --deobf -d cikti_dizin <hedef.apk>` |
| **Smali'ye Ayrıştırma** | `apktool d <hedef.apk> -o calisma_alani` |
| **APK'yı Yeniden Derleme** | `apktool b calisma_alani -o yama.apk` |
| **Zipalign Bellek Hizalama** | `zipalign -p -f -v 4 yama.apk hizali.apk` |
| **APK İmzalama** | `apksigner sign --ks key.jks --out imzali.apk hizali.apk` |
| **İmza Doğrulama** | `apksigner verify imzali.apk` |

---

## 🛡️ Savunma Yöntemleri ve Kod Karıştırma (Obfuscation)

1. **R8 / ProGuard Küçültme:** Kullanılmayan sınıfları temizleyin ve sınıf/fonksiyon isimlerini tek harflik anlamsız karakterlere (`a.b.c`) dönüştürün.
2. **Ticari Obfuscation (DexGuard):** Dizgileri şifreleyin, API çağrılarını dinamikleştirin ve APK bütünlüğünü denetleyen yerel C/C++ kütüphaneleri (tamper detection) entegre edin.

---

## ⚠️ Yasal Uyarı

> [!CAUTION]
> Jadx ve Apktool araçları yalnızca yetkili güvenlik testleri, kaynak kod denetimleri ve lisansı size ait yazılımların analizi amacıyla kullanılmalıdır. İzniniz olmayan ticari uygulamaları geri derlemek telif hakları ihlaline yol açabilir.
