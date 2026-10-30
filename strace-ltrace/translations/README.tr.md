# 🔬 strace & ltrace - Kapsamlı Başvuru Rehberi (Cheatsheet)

[🇺🇸 English Version](../README.md)

```
  ███████╗████████╗██████╗  █████╗  ██████╗███████╗    ██╗  ████████╗██████╗  █████╗  ██████╗███████╗
  ██╔════╝╚══██╔══╝██╔══██╗██╔══██╗██╔════╝██╔════╝    ██║  ╚══██╔══╝██╔══██╗██╔══██╗██╔════╝██╔════╝
  ███████╗   ██║   ██████╔╝███████║██║     █████╗      ██║     ██║   ██████╔╝███████║██║     █████╗  
  ╚════██║   ██║   ██╔══██╗██╔══██║██║     ██╔══╝      ██║     ██║   ██╔══██╗██╔══██║██║     ██╔══╝  
  ███████║   ██║   ██║  ██║██║  ██║╚██████╗███████╗    ███████╗██║   ██║  ██║██║  ██║╚██████╗███████╗
  ╚══════╝   ╚═╝   ╚═╝  ╚═╝╚═╝  ╚═╝ ╚═════╝╚══════╝    ╚══════╝╚═╝   ╚═╝  ╚═╝╚═╝  ╚═╝ ╚═════╝╚══════╝
       Linux Dinamik İkili Analizi: Sistem ve Dinamik Kütüphane Çağrısı İzleyicileri
```

<p align="center">
  <img src="https://img.shields.io/badge/strace-Sistem_Çağrıları-blue?style=for-the-badge" alt="strace">
  <img src="https://img.shields.io/badge/ltrace-Kütüphane_Çağrıları-orange?style=for-the-badge" alt="ltrace">
  <img src="https://img.shields.io/badge/Platform-Linux_ELF-green?style=for-the-badge" alt="Linux">
  <img src="https://img.shields.io/badge/Kategori-Dinamik_Analiz-purple?style=for-the-badge" alt="Dinamik Analiz">
</p>

<p align="center">
  <b>🔬 Derlenmiş Linux programlarında kernel sistem çağrılarını (strace) ve paylaşılan dinamik kütüphane fonksiyonlarını (ltrace) canlı izleyen temel komut satırı araçları</b>
</p>

---

## 📋 İçindekiler

- [Genel Bakış: Sistem Çağrıları ve Kütüphane Çağrıları Farkı](#-genel-bakış-sistem-çağrıları-ve-kütüphane-çağrıları-farkı)
- [Kurulum](#-kurulum)
- [strace: Sistem Çağrısı İzleme](#-strace-sistem-çağrısı-izleme)
  - [Temel İzleme Komutu](#temel-izleme-komutu)
  - [Sistem Çağrılarını Sınıfa veya İsme Göre Filtreleme (`-e`)](#sistem-çağrılarını-sınıfa-veya-isme-göre-filtreleme--e)
  - [Alt Süreçleri ve Thread'leri İzleme (`-f`)](#alt-süreçleri-ve-threadleri-izleme--f)
  - [Çalışan Bir Sürece Bağlanma (`-p`)](#çalışan-bir-sürece-bağlanma--p)
  - [Dizgi ve Bellek Tamponlarını Genişletme (`-s`)](#dizgi-ve-bellek-tamponlarını-genişletme--s)
  - [İstatistiksel Profilleme ve Hata Özeti (`-c`)](#istatistiksel-profilleme-ve-hata-özeti--c)
  - [Fuzzing İçin Hata Enjeksiyonu (`-e fault`)](#fuzzing-için-hata-enjeksiyonu--e-fault)
- [ltrace: Dinamik Kütüphane Çağrısı İzleme](#-ltrace-dinamik-kütüphane-çağrısı-izleme)
  - [Temel Kütüphane İzleme](#temel-kütüphane-izleme)
  - [Gömülü Parola Doğrulamalarını Yakalama (`strcmp`)](#gömülü-parola-doğrulamalarını-yakalama-strcmp)
  - [C++ İsimlerini Anlamlandırma (`-C` Demangling)](#c-isimlerini-anlamlandırma--c-demangling)
  - [Belirli Fonksiyonları Filtreleme](#belirli-fonksiyonları-filtreleme)
- [strace ve ltrace Birlikte Kullanım İş Akışı](#-strace-ve-ltrace-birlikte-kullanım-iş-akışı)
- [Hızlı Referans Tablosu](#-hızlı-referans-tablosu)
- [Savunma Yöntemleri ve Anti-Tracing](#-savunma-yöntemleri-ve-anti-tracing)
- [Yasal Uyarı](#-yasal-uyarı)

---

## 🎯 Genel Bakış: Sistem Çağrıları ve Kütüphane Çağrıları Farkı

Linux ELF ikili dosyalarını analiz ederken statik analiz; kod karıştırma (obfuscation), sembollerin silinmesi (stripped) veya paketleme (packing) nedeniyle zorlaşabilir. Dinamik izleme, programın çalışma anında ne yaptığını saniyeler içinde açığa çıkarır:

1. **Sistem Çağrıları (`strace`):**
   - Kullanıcı alanı (user-space) uygulamaları ile Linux çekirdeği (kernel) arasındaki köprüdür.
   - Dosya okuma/yazma (`openat`, `read`, `write`), ağ soketleri (`socket`, `connect`), bellek ayırma (`mmap`, `brk`) ve süreç yönetimini (`fork`, `execve`, `ptrace`) izler.
2. **Kütüphane Çağrıları (`ltrace`):**
   - Program ile paylaşılan dinamik kütüphaneler (`libc.so`, `libssl.so` vb.) arasındaki fonksiyon çağrılarını izler.
   - `strcmp()`, `strlen()`, `malloc()`, `system()` gibi fonksiyonları ve parametrelerini yakalar; parola doğrulamalarını açık metin olarak yakalamada son derece etkilidir.

---

## 📥 Kurulum

```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y strace ltrace

# Arch Linux
sudo pacman -S strace ltrace

# Fedora / Red Hat / CentOS
sudo dnf install -y strace ltrace
```

---

## ⚡ strace: Sistem Çağrısı İzleme

### Temel İzleme Komutu
Programı çalıştırıp ürettiği tüm sistem çağrılarını terminale dökme:
```bash
strace ./hedef_program
```

Program çıktısını kirletmemek için çağrıları bir dosyaya kaydetme:
```bash
strace -o izleme.log ./hedef_program
```

### Sistem Çağrılarını Sınıfa veya İsme Göre Filtreleme (`-e`)
İlgisiz çağrıları eleyerek analiz netliği sağlama:

```bash
# YALNIZCA dosya işlemlerini izle (open, read, write, stat, access)
strace -e trace=file ./hedef_program

# YALNIZCA ağ bağlantılarını ve veri transferlerini izle
strace -e trace=network ./hedef_program

# YALNIZCA süreç oluşturma ve yürütme çağrılarını izle
strace -e trace=process ./hedef_program

# İsme göre doğrudan belirli çağrıları izle
strace -e trace=open,openat,read,write ./hedef_program
```

### Alt Süreçleri ve Thread'leri İzleme (`-f`)
Modern ikili dosyalar veya zararlı yazılımlar alt iş parçacıkları (threads) veya alt süreçler (fork) oluşturur. `-f` bayrağı olmadan bunlar gözden kaçar:
```bash
# Tüm alt süreçleri ve thread'leri takip et
strace -f ./hedef_program

# Her bir sürecin çıktısını ayrı dosyalara kaydet (izleme.PID)
strace -ff -o izleme ./hedef_program
```

### Çalışan Bir Sürece Bağlanma (`-p`)
Çalışmakta olan bir servisi veya süreci yeniden başlatmadan canlı izleme:
```bash
# Süreç Kimliğine (PID) göre bağlan
sudo strace -p 1337

# Sürece ve sonradan açacağı tüm alt thread'lere bağlan
sudo strace -f -p 1337 -o baglanan.log
```

### Dizgi ve Bellek Tamponlarını Genişletme (`-s`)
Varsayılan olarak strace uzun dizgileri 32 karakterde keser (`...`). Dosya yollarını, web adreslerini veya buffer verilerini tam görmek için:
```bash
# Dizgilerin ilk 2048 karakterini eksiksiz göster
strace -s 2048 -e trace=read,write ./hedef_program
```

### İstatistiksel Profilleme ve Hata Özeti (`-c`)
Hangi çağrının kaç kez çalıştığını, geçen süreyi ve alınan hataları özet tablo olarak dökme:
```bash
strace -c ./hedef_program
```
*Özet Tablo Çıktısı:*
```text
% time     seconds  usecs/call     calls    errors syscall
------ ----------- ----------- --------- --------- ----------------
 54.32    0.001200          40        30           read
 32.10    0.000710          71        10         2 openat
 13.58    0.000300          15        20           write
```

### Fuzzing İçin Hata Enjeksiyonu (`-e fault`)
Programın beklenmedik durumlardaki davranışını test etmek için yapay hatalar enjekte etme:
```bash
# openat çağrısının 3. çalışmasında Erişim Engellendi (EACCES) hatası döndür
strace -e fault=openat:error=EACCES:when=3 ./hedef_program
```

---

## 🧩 ltrace: Dinamik Kütüphane Çağrısı İzleme

### Temel Kütüphane İzleme
Programın dinamik `.so` kütüphanelerine yaptığı çağrıları yakalama:
```bash
ltrace ./hedef_program
```

### Gömülü Parola Doğrulamalarını Yakalama (`strcmp`)
CTF ve tersine mühendislik sorularında parola doğrulama fonksiyonları (`strcmp`, `strncmp`, `memcmp`) doğru parolayı açık metin argüman olarak belleğe alır:
```bash
ltrace -s 128 -e strcmp,strncmp ./lisans_kontrol
```
*Örnek Çıktı:*
```text
strcmp("girilen_metin", "COK_GIZLI_BAYRAK_2026") = -1
```

### C++ İsimlerini Anlamlandırma (`-C` Demangling)
Derleyicinin karmaşıklaştırdığı C++ sembollerini (`_ZNK4Test3fooEv` -> `Test::foo()`) okunabilir hale getirme:
```bash
ltrace -C ./cpp_programi
```

### Belirli Fonksiyonları Filtreleme
```bash
# Bellek ayırma ve metin kopyalama çağrılarını izleme
ltrace -e malloc+free+strdup ./hedef_program

# Yalnızca belirli bir kütüphaneden gelen çağrıları yakalama
ltrace -l /lib/x86_64-linux-gnu/libcrypto.so.3 ./hedef_program
```

---

## 🔄 strace ve ltrace Birlikte Kullanım İş Akışı

Bilinmeyen bir ikili dosyayı hızlıca ön incelemeye (triage) alırken:

1. **Adım 1:** `strace -e trace=file,network ./program` ile hangi dosyalara eriştiğini ve ağa bağlanıp bağlanmadığını görün.
2. **Adım 2:** `ltrace -s 256 ./program` ile iç kütüphane karşılaştırmalarını ve şifreleme fonksiyonlarını dökün.
3. **Adım 3:** İki görünümü birleştirmek için ltrace'e `-S` bayrağını ekleyin:
   ```bash
   ltrace -S ./program
   ```
   *(Bu komut hem kütüphane çağrılarını hem de sistem çağrılarını tek ekranda kronolojik olarak gösterir).*

---

## ⚡ Hızlı Referans Tablosu

| Amaç | Komut |
|------|-------|
| **Tüm Sistem Çağrılarını İzle** | `strace ./program` |
| **Dosyaya Kaydet** | `strace -o log.txt ./program` |
| **Dosya İşlemlerini Filtrele** | `strace -e trace=file ./program` |
| **Ağ Etkinliğini Filtrele** | `strace -e trace=network ./program` |
| **Alt Süreçleri / Thread'leri İzle** | `strace -f ./program` |
| **Çalışan PID'ye Bağlan** | `sudo strace -f -p <PID>` |
| **Dizgi Uzunluğunu Artır** | `strace -s 2048 ./program` |
| **Çağrı İstatistik Tablosu** | `strace -c ./program` |
| **Kütüphane Çağrılarını İzle** | `ltrace ./program` |
| **Parola Karşılaştırmasını Yakala** | `ltrace -s 128 -e strcmp,strncmp ./program` |
| **C++ İsimlerini Çöz (Demangle)** | `ltrace -C ./program` |
| **Kütüphane + Syscall Birlikte** | `ltrace -S ./program` |

---

## 🛡️ Savunma Yöntemleri ve Anti-Tracing

1. **Ptrace Denetimi:** Linux çekirdeği bir sürece aynı anda yalnızca tek bir izleyicinin (tracer) `ptrace` ile bağlanmasına izin verir. Kod içerisine yerleştirilen `ptrace(PTRACE_TRACEME)` çağrısı `strace` veya `ltrace` bağlıysa hata verir ve program kendini kapatabilir.
2. **Statik Derleme (`-static`):** Statik derlenmiş ikili dosyalarda paylaşılan dinamik kütüphane bağları (`DT_NEEDED`) bulunmadığından `ltrace` işlevsiz kalır; bu durumda `strace` kullanılmalıdır.

---

## ⚠️ Yasal Uyarı

> [!CAUTION]
> strace ve ltrace araçları yazılım hata ayıklama, sistem yönetimi ve yetkili güvenlik testleri için tasarlanmıştır. İzniniz olmayan süreçleri dinlemek veya özel verileri ele geçirmek yasalara aykırı olabilir.
