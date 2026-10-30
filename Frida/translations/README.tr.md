# 📱 Frida & Objection - Kapsamlı Başvuru Rehberi (Cheatsheet)

[🇺🇸 English Version](../README.md)

```
  ███████╗██████╗ ██╗██████╗  █████╗      ██████╗ ██████╗      ██╗███████╗ ██████╗████████╗██╗ ██████╗ ███╗   ██╗
  ██╔════╝██╔══██╗██║██╔══██╗██╔══██╗    ██╔═══██╗██╔══██╗     ██║██╔════╝██╔════╝╚══██╔══╝██║██╔═══██╗████╗  ██║
  █████╗  ██████╔╝██║██║  ██║███████║    ██║   ██║██████╔╝     ██║█████╗  ██║        ██║   ██║██║   ██║██╔██╗ ██║
  ██╔══╝  ██╔══██╗██║██║  ██║██╔══██║    ██║   ██║██╔══██╗██   ██║██╔══╝  ██║        ██║   ██║██║   ██║██║╚██╗██║
  ██║     ██║  ██║██║██████╔╝██║  ██║    ╚██████╔╝██████╔╝╚█████╔╝███████╗╚██████╗   ██║   ██║╚██████╔╝██║ ╚████║
  ╚═╝     ╚═╝  ╚═╝╚═╝╚═════╝ ╚═╝  ╚═╝     ╚═════╝ ╚═════╝  ╚════╝ ╚══════╝ ╚═════╝   ╚═╝   ╚═╝ ╚═════╝ ╚═╝  ╚═══╝
         Dinamik İkili Enstrümantasyon ve Mobil Güvenlik Analizi Araç Seti
```

<p align="center">
  <img src="https://img.shields.io/badge/Frida-Çalışma_Zamanı_Kancalama-blue?style=for-the-badge" alt="Frida">
  <img src="https://img.shields.io/badge/Objection-Mobil_Analiz_Rehberi-orange?style=for-the-badge" alt="Objection">
  <img src="https://img.shields.io/badge/Platformlar-Android_%7C_iOS-green?style=for-the-badge" alt="Android | iOS">
  <img src="https://img.shields.io/badge/Kategori-Mobil_Güvenlik-red?style=for-the-badge" alt="Mobil Güvenlik">
</p>

<p align="center">
  <b>📱 Android ve iOS uygulamalarında çalışma zamanı fonksiyon kancalama (hooking), SSL Pinning atlatma, Root/Jailbreak tespiti aşma ve bellek manipülasyonu için endüstri standardı araç kiti</b>
</p>

---

## 📋 İçindekiler

- [Genel Bakış ve Mimari](#-genel-bakış-ve-mimari)
- [Kurulum ve Cihaz Hazırlığı](#-kurulum-ve-cihaz-hazırlığı)
  - [Ana Makine Kurulumu](#ana-makine-kurulumu)
  - [Android Cihaz Yapılandırması (`frida-server`)](#android-cihaz-yapılandırması-frida-server)
  - [iOS Cihaz Yapılandırması](#ios-cihaz-yapılandırması)
- [Frida Temel Komutları](#-frida-temel-komutları)
  - [Cihazları ve Çalışan Uygulamaları Listeleme](#cihazları-ve-çalışan-uygulamaları-listeleme)
  - [Attach ve Spawn Modları](#attach-ve-spawn-modları)
- [JavaScript Kancalama (Hooking) Kodları](#-javascript-kancalama-hooking-kodları)
  - [Android SSL Pinning Atlatma](#android-ssl-pinning-atlatma)
  - [Root Kontrolünü Atlatma](#root-kontrolünü-atlatma)
  - [Java Fonksiyon Parametrelerini Değiştirme](#java-fonksiyon-parametrelerini-değiştirme)
- [Objection Mobil Güvenlik Çerçevesi](#-objection-mobil-güvenlik-çerçevesi)
  - [Objection Oturumu Başlatma](#objection-oturumu-başlatma)
  - [Tek Komutla SSL Pinning ve Root Atlatma](#tek-komutla-ssl-pinning-ve-root-atlatma)
  - [Keystore, SQLite ve SharedPreferences İnceleme](#keystore-sqlite-ve-sharedpreferences-inceleme)
  - [Sınıfları İzleme ve Activity Listeleme](#sınıfları-izleme-ve-activity-listeleme)
- [Hızlı Referans Tablosu](#-hızlı-referans-tablosu)
- [Savunma Yöntemleri ve Anti-Frida](#-savunma-yöntemleri-ve-anti-frida)
- [Yasal Uyarı](#-yasal-uyarı)

---

## 🎯 Genel Bakış ve Mimari

**Frida**, Google V8 JavaScript motorunu Android, iOS, Windows ve Linux üzerindeki hedef süreçlerin belleğine enjekte eder. Güvenlik uzmanlarına şu yetenekleri sunar:
- Yerel (`libc`, OpenSSL) ve yönetilen (`Java`, `Objective-C`, `Swift`) fonksiyonları çalışma zamanında dinamik kancalama (hooking).
- Fonksiyon argümanlarını, dönüş değerlerini ve bellek verilerini anlık değiştirme.
- Şifreleme API'lerini izleyerek bellekte açık metin verileri yakalama.

**Objection**, Frida altyapısını kullanarak JavaScript kodu yazmaya gerek kalmadan SSL Pinning kapatma, biyometrik doğrulama atlatma ve bellek dökümü alma gibi işlemleri otomatikleştirir.

---

## 📥 Kurulum ve Cihaz Hazırlığı

### Ana Makine Kurulumu
```bash
pip install frida frida-tools objection
```

### Android Cihaz Yapılandırması (`frida-server`)
Ana makinedeki Frida sürümüyle ve cihazın CPU mimarisiyle (arm64, x86_64 vb.) eşleşen `frida-server` ikili dosyasını indirin:
```bash
# Cihaz mimarisini kontrol etme
adb shell getprop ro.product.cpu.abi

# Root yetkisiyle frida-server dosyasını yükleyip başlatma
unxz frida-server-*-android-arm64.xz
adb root
adb push frida-server-*-android-arm64 /data/local/tmp/frida-server
adb shell "chmod 755 /data/local/tmp/frida-server"
adb shell "/data/local/tmp/frida-server &"
```

### iOS Cihaz Yapılandırması
Jailbreak yapılmış iOS cihazlarda Cydia veya Sileo içerisine `https://build.frida.re` kaynak adresini ekleyin ve `Frida` paketini kurun.

---

## 🚀 Frida Temel Komutları

### Cihazları ve Çalışan Uygulamaları Listeleme
```bash
# USB ile bağlı cihazları listeleme
frida-ls-devices

# USB cihazındaki kurulu ve çalışan uygulamaları listeleme
frida-ps -Uai

# USB cihazındaki tüm sistem süreçlerini listeleme
frida-ps -U
```

### Attach ve Spawn Modları
- **Attach (`-n` veya `-p`):** Halihazırda çalışan bir sürece sonradan kanca atar.
- **Spawn (`-f`):** Uygulamayı sıfırdan başlatır, başlangıç sınıfları yüklenmeden önce duraklatıp kancaları yerleştirir:
```bash
# Çalışan uygulamaya kanca takma
frida -U -n com.example.app -l hook.js

# Uygulamayı sıfırdan spawn ederek kancalama
frida -U -f com.example.app -l hook.js
```

---

## 💻 JavaScript Kancalama (Hooking) Kodları

### Android SSL Pinning Atlatma
Evrensel TrustManager atlatma betiği (`ssl_bypass.js`):
```javascript
Java.perform(function() {
    var X509TrustManager = Java.use('javax.net.ssl.X509TrustManager');
    var SSLContext = Java.use('javax.net.ssl.SSLContext');

    var TrustManager = Java.registerClass({
        name: 'com.custom.TrustManager',
        implements: [X509TrustManager],
        methods: {
            checkClientTrusted: function(chain, authType) {},
            checkServerTrusted: function(chain, authType) {},
            getAcceptedIssuers: function() { return []; }
        }
    });

    var TrustManagers = [TrustManager.$new()];
    var SSLContext_init = SSLContext.init.overload(
        '[Ljavax.net.ssl.KeyManager;', '[Ljavax.net.ssl.TrustManager;', 'java.security.SecureRandom');
    
    SSLContext_init.implementation = function(km, tm, random) {
        console.log('[+] SSLContext.init() atlatıldı, özel TrustManager uygulandı.');
        SSLContext_init.call(this, km, TrustManagers, random);
    };
});
```

Frida ile çalıştırma:
```bash
frida -U -f com.example.app -l ssl_bypass.js
```

### Root Kontrolünü Atlatma
RootBeer kontrolünü ezme:
```javascript
Java.perform(function() {
    var RootBeer = Java.use("com.scottyab.rootbeer.RootBeer");
    RootBeer.isRooted.implementation = function() {
        console.log("[+] RootBeer.isRooted() ezildi -> false döndürülüyor.");
        return false;
    };
});
```

### Java Fonksiyon Parametrelerini Değiştirme
```javascript
Java.perform(function() {
    var AuthManager = Java.use("com.example.app.AuthManager");
    AuthManager.verifyToken.implementation = function(token) {
        console.log("[*] Orijinal Token: " + token);
        // Sunucu ne dönerse dönsün her zaman true döndür
        return true;
    };
});
```

---

## 🛠️ Objection Mobil Güvenlik Çerçevesi

Objection, interaktif komut satırıyla karmaşık tersine mühendislik işlerini tek komuta indirir:

### Objection Oturumu Başlatma
```bash
objection -g com.example.app explore
```

### Tek Komutla SSL Pinning ve Root Atlatma
Objection kabuğu içerisinde:
```text
# SSL Pinning korumasını anında kapatma
android sslpinning disable

# Root kontrollerini devre dışı bırakma
android root disable

# iOS cihazlar için karşılıkları
ios sslpinning disable
ios jailbreak disable
```

### Keystore, SQLite ve SharedPreferences İnceleme
```text
# Keystore içerisindeki sertifika ve anahtarları dökme
android keystore list

# SQLite veritabanlarını listeleme
sqlite sync
sqlite execute schema

# SharedPreferences XML yapılarını ve çevre değişkenlerini inceleme
env
android hooking list activities
```

### Sınıfları İzleme ve Activity Listeleme
```text
# Kelime eşleşmesiyle sınıfları arama
android hooking search classes auth

# Fonksiyon çağrılarını gerçek zamanlı izleme
android hooking watch class_method com.example.app.Auth.login --dump-args --dump-return
```

---

## ⚡ Hızlı Referans Tablosu

| Amaç | Frida Komutu | Objection Komutu |
|------|--------------|------------------|
| **Uygulamaları Listeleme** | `frida-ps -Uai` | `frida-ps -Uai` |
| **Uygulamayı Kancayla Başlatma** | `frida -U -f <PAKET> -l hook.js` | `objection -g <PAKET> explore` |
| **SSL Pinning Atlatma** | `frida -U -f <PAKET> -l ssl_bypass.js` | `android sslpinning disable` |
| **Root/Jailbreak Atlatma** | Özel Java hook betiği | `android root disable` |
| **Bellekteki Dizgileri Dökme** | `frida-dump` | `memory dump all strings.txt` |
| **Activity Listeleme** | `frida-ps` | `android hooking list activities` |
| **Fonksiyon İzleme (Trace)** | `frida-trace -U -j "com.pkg.Class!*"` | `android hooking watch class ...` |

---

## 🛡️ Savunma Yöntemleri ve Anti-Frida

1. **Frida Port Denetimi:** Yerel `27042` portunun açık olup olmadığını denetleyin ve `/proc/self/maps` dosyasında `frida-agent.so` kütüphanesini arayın.
2. **Ptrace Koruması:** C/C++ yerel kütüphanelerinde `ptrace(PTRACE_TRACEME, 0, 1, 0)` fonksiyonunu çağırarak hata ayıklayıcıların (debugger) bağlanmasını engelleyin.

---

## ⚠️ Yasal Uyarı

> [!CAUTION]
> Frida ve Objection araçları yalnızca yasal izin alınmış mobil sızma testleri, kod denetimleri ve güvenlik araştırmaları amacıyla kullanılmalıdır. İzniniz olmayan uygulamaları manipüle etmek yasalara aykırıdır.
