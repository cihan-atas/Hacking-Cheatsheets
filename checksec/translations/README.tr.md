# 🛡️ checksec & Binutils - Kapsamlı Başvuru Rehberi (Cheatsheet)

[🇺🇸 English Version](../README.md)

```
   ██████╗██╗  ██╗███████╗ ██████╗██╗  ██╗███████╗███████╗ ██████╗
  ██╔════╝██║  ██║██╔════╝██╔════╝██║ ██╔╝██╔════╝██╔════╝██╔════╝
  ██║     ███████║█████╗  ██║     █████╔╝ ███████╗█████╗  ██║     
  ██║     ██╔══██║██╔══╝  ██║     ██╔═██╗ ╚════██║██╔══╝  ██║     
  ╚██████╗██║  ██║███████╗╚██████╗██║  ██╗███████║███████╗╚██████╗
   ╚═════╝╚═╝  ╚═╝╚══════╝ ╚═════╝╚═╝  ╚═╝╚══════╝╚══════╝ ╚═════╝
         Linux ELF İkili Dosya Güvenlik Önlemleri ve Analiz Araç Seti
```

<p align="center">
  <img src="https://img.shields.io/badge/checksec-Güvenlik_Korumaları-blue?style=for-the-badge" alt="checksec">
  <img src="https://img.shields.io/badge/readelf-ELF_Başlıkları-orange?style=for-the-badge" alt="readelf">
  <img src="https://img.shields.io/badge/objdump-Disassembly-green?style=for-the-badge" alt="objdump">
  <img src="https://img.shields.io/badge/Platform-Linux_ELF-purple?style=for-the-badge" alt="Linux ELF">
</p>

<p align="center">
  <b>🛡️ Linux ELF ikili dosyalarında derleyici güvenlik önlemlerini (RELRO, Stack Canary, NX, PIE, Fortify) denetleyen ve dosya başlıklarını, kesitlerini, sembol tablolarını ayrıştıran komut satırı araçları</b>
</p>

---

## 📋 İçindekiler

- [Genel Bakış ve İkili Koruma Matrisi](#-genel-bakış-ve-ikili-koruma-matrisi)
- [Kurulum](#-kurulum)
- [checksec: Hızlı Güvenlik Koruması Denetimi](#-checksec-hızlı-güvenlik-koruması-denetimi)
  - [Tekli ve Toplu Dosya Denetimi](#tekli-ve-toplu-dosya-denetimi)
  - [Çalışan Süreçleri Denetleme](#çalışan-süreçleri-denetleme)
  - [JSON ve CSV Formatında Çıktı](#json-ve-csv-formatında-çıktı)
- [readelf: ELF Başlık ve Kesit İnceleme](#-readelf-elf-başlık-ve-kesit-inceleme)
  - [ELF Dosya Başlığı (`-h`)](#elf-dosya-başlığı--h)
  - [Program Başlıkları ve Segmentler (`-l`)](#program-başlıkları-ve-segmentler--l)
  - [Kesit Başlıkları (`-S`)](#kesit-başlıkları--s)
  - [Sembol Tabloları (`-s` / `--syms`)](#sembol-tabloları--s----syms)
  - [Dinamik Bağımlılıklar ve Kütüphaneler (`-d`)](#dinamik-bağımlılıklar-ve-kütüphaneler--d)
- [objdump: Disassembly ve Yeniden Konumlandırma](#-objdump-disassembly-ve-yeniden-konumlandırma)
  - [Intel Sözdiziminde Disassembly (`-d -M intel`)](#intel-sözdiziminde-disassembly--d--m-intel)
  - [Yeniden Konumlandırma Girdileri ve GOT (`-R`)](#yeniden-konumlandırma-girdileri-ve-got--r)
- [nm ve strings: Sembol Çözümleme ve Dizgiler](#-nm-ve-strings-sembol-çözümleme-ve-dizgiler)
  - [`nm` ile Sembolleri Çözme (Demangle)](#nm-ile-sembolleri-çözme-demangle)
  - [`strings` ile Gizli ve Geniş Dizgileri Çıkarma](#strings-ile-gizli-ve-geniş-dizgileri-çıkarma)
- [Sıkılaştırılmış (Hardened) Derleme Referansı](#-sıkılaştırılmış-hardened-derleme-referansı)
- [Hızlı Referans Tablosu](#-hızlı-referans-tablosu)
- [Yasal Uyarı](#-yasal-uyarı)

---

## 🎯 Genel Bakış ve İkili Koruma Matrisi

Herhangi bir tersine mühendislik veya exploit geliştirme sürecine başlamadan önce hedef dosyada hangi derleyici korumalarının aktif olduğunu bilmek şarttır:

| Koruma | Tam Adı | Koruma Mekanizması | Atlatma Stratejisi |
|--------|---------|--------------------|--------------------|
| **RELRO** | Relocation Read-Only | `.got` tablosunu salt okunur yapar (`Full RELRO`); fonksiyon göstericisi ezilmesini engeller | Yığıt dönüş adreslerini veya heap yapılarını hedefleme |
| **Canary** | Stack Protector | Dönüş adresinden önce rastgele cookie koyar; fonksiyon çıkışında doğrular | Bellek sızıntısıyla (leak) canary değerini okuma veya brute-force |
| **NX** | No-Execute / DEP | Yığıt, heap ve veri alanlarını çalıştırılamaz (`non-executable`) olarak işaretler | ROP (Return-Oriented Programming) veya JOP teknikleri |
| **PIE** | Position Independent Executable | Program taban adresini belleğe her açılışta rastgele yükler (ASLR) | Kod göstericisi sızdırarak (leak) taban adresi hesaplama |
| **Fortify** | `_FORTIFY_SOURCE` | Güvensiz fonksiyonları (`strcpy` -> `__strcpy_chk`) sınır denetimli halleriyle değiştirir | Taşma içermeyen mantık hataları, format string zaafları |

---

## 📥 Kurulum

```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y checksec binutils

# Arch Linux
sudo pacman -S checksec binutils

# Python pip ile Kurulum (Bağımsız checksec)
pip install checksec.py
```

---

## ⚡ checksec: Hızlı Güvenlik Koruması Denetimi

### Tekli ve Toplu Dosya Denetimi
```bash
# Tek bir dosyayı denetleme
checksec --file=./hedef_program

# Klasördeki tüm ikili dosyaları topluca denetleme
checksec --dir=/usr/bin/
```
*Standart Çıktı Örneği:*
```text
RELRO           STACK CANARY      NX            PIE             RPATH      RUNPATH      FILE
Full RELRO      Canary found      NX enabled    PIE enabled     No RPATH   No RUNPATH   ./hedef_program
```

### Çalışan Süreçleri Denetleme
Arka planda çalışan bir servisin veya uygulamanın bellek korumalarını inceleme:
```bash
# Süreç Kimliğine (PID) göre denetleme
checksec --proc=1337

# Tüm süreçleri denetleme
checksec --proc-all
```

### JSON ve CSV Formatında Çıktı
Otomasyon betiklerine aktarmak için:
```bash
checksec --file=./hedef_program --output=json > guvenlik.json
checksec --file=./hedef_program --output=csv > guvenlik.csv
```

---

## 🔬 readelf: ELF Başlık ve Kesit İnceleme

`readelf`, programı çalıştırmadan ikili dosyanın iç yapısını güvenle inceler:

### ELF Dosya Başlığı (`-h`)
Mimariyi, 32/64 bit sınıfını, program giriş adresini (`Entry point`) ve endian yapısını gösterir:
```bash
readelf -h ./hedef_program
```

### Program Başlıkları ve Segmentler (`-l`)
Çalışma anında belleğe yüklenecek segmentleri ve izinlerini (`R`, `W`, `E`) gösterir:
```bash
readelf -l ./hedef_program
```
*(Eğer `GNU_STACK` satırında `RWE` izni varsa, yığıt üzerinde kod çalıştırılabilir; yani NX devre dışıdır).*

### Kesit Başlıkları (`-S`)
`.text` (kod), `.data` (başlangıç değerli veri), `.rodata` (sabitler), `.bss` (başlangıç değersiz veri), `.plt` ve `.got` kesitlerinin boyut ve adreslerini döker:
```bash
readelf -S ./hedef_program
```

### Sembol Tabloları (`-s` / `--syms`)
Program içindeki tüm fonksiyon isimlerini ve global değişkenleri listeler:
```bash
# Tüm sembolleri listele
readelf -s ./hedef_program

# Yalnızca dinamik sembolleri (libc çağrıları) listele
readelf --dyn-syms ./hedef_program
```

### Dinamik Bağımlılıklar ve Kütüphaneler (`-d`)
Programın çalışmak için ihtiyaç duyduğu paylaşılan kütüphaneleri (`NEEDED` etiketleri) listeler:
```bash
readelf -d ./hedef_program
```

---

## 🧩 objdump: Disassembly ve Yeniden Konumlandırma

### Intel Sözdiziminde Disassembly (`-d -M intel`)
Çalıştırılabilir kesitleri temiz Intel sözdiziminde makine talimatlarına döker:
```bash
# .text kesitini Intel sözdiziminde disassemble et
objdump -d -M intel ./hedef_program

# Belirli bir fonksiyonu inceleme (ör. main)
objdump -d -M intel ./hedef_program | grep -A 30 "<main>:"
```

### Yeniden Konumlandırma Girdileri ve GOT (`-R`)
Global Offset Table (GOT) içerisindeki kütüphane fonksiyon adreslerini görüntüler:
```bash
objdump -R ./hedef_program
```
*`puts`, `printf`, `system` gibi fonksiyonların GOT ofsetlerini döker.*

---

## 🔍 nm ve strings: Sembol Çözümleme ve Dizgiler

### `nm` ile Sembolleri Çözme (Demangle)
```bash
# Yalnızca dosya içinde tanımlanmış sembolleri listele
nm --defined-only -C ./hedef_program

# Dışarıdan bağlanan tanımsız sembolleri listele
nm -u -C ./hedef_program
```

### `strings` ile Gizli ve Geniş Dizgileri Çıkarma
```bash
# En az 8 karakter uzunluğundaki ASCII dizgileri dök
strings -a -n 8 ./hedef_program

# 16-bit Little Endian geniş (wide) dizgileri dök (UTF-16)
strings -e l -a ./hedef_program

# Her bir dizginin dosya içindeki hex ofsetini göster
strings -a -t x ./hedef_program
```

---

## 🛡️ Sıkılaştırılmış (Hardened) Derleme Referansı

Maksimum güvenlik korumalarına sahip bir ELF dosyası derlemek için:

```bash
gcc -O2 \
  -Wall \
  -fstack-protector-strong \
  -D_FORTIFY_SOURCE=2 \
  -Wl,-z,relro,-z,now \
  -fPIE -pie \
  -Wl,-z,noexecstack \
  kaynak.c -o guvenli_program
```

---

## ⚡ Hızlı Referans Tablosu

| Amaç | Komut |
|------|-------|
| **Korumaları Denetle** | `checksec --file=<dosya>` |
| **Süreç PID Koruması Denetle**| `checksec --proc=<pid>` |
| **ELF Başlığını İncele** | `readelf -h <dosya>` |
| **NX / Yığıt İznini Gör** | `readelf -l <dosya> \| grep -A 1 GNU_STACK` |
| **Kesitleri Listele (.got, .plt)**| `readelf -S <dosya>` |
| **Dinamik Sembolleri Listele**| `readelf --dyn-syms <dosya>` |
| **Intel Disassembly Al** | `objdump -d -M intel <dosya>` |
| **GOT Relocation Tablosu** | `objdump -R <dosya>` |
| **Sembolleri Çöz (Demangle)** | `nm -C <dosya>` |
| **UTF-16 Dizgileri Çıkar** | `strings -e l -a <dosya>` |
| **Dizgi Ofsetlerini Göster** | `strings -a -t x <dosya>` |

---

## ⚠️ Yasal Uyarı

> [!CAUTION]
> checksec ve GNU Binutils araçları yazılım güvenliği denetimi, sistem analizi ve yetkili araştırmalar için tasarlanmıştır. İzniniz olmayan ikili dosyaları incelemek yürürlükteki telif ve güvenlik yasalarına tabi olabilir.
