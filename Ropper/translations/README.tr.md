# 🧩 Ropper & ROPgadget - Kapsamlı Başvuru Rehberi (Cheatsheet)

[🇺🇸 English Version](../README.md)

```
  ██████╗  ██████╗ ██████╗ ██████╗ ███████╗██████╗ 
  ██╔══██╗██╔═══██╗██╔══██╗██╔══██╗██╔════╝██╔══██╗
  ██████╔╝██║   ██║██████╔╝██████╔╝█████╗  ██████╔╝
  ██╔══██╗██║   ██║██╔═══╝ ██╔═══╝ ██╔══╝  ██╔══██╗
  ██║  ██║╚██████╔╝██║     ██║     ███████╗██║  ██║
  ╚═╝  ╚═╝ ╚═════╝ ╚═╝     ╚═╝     ╚══════╝╚═╝  ╚═╝
     Gelişmiş ROP & JOP Gadget Bulucu ve Zincir Üretici
```

<p align="center">
  <img src="https://img.shields.io/badge/Ropper-Gadget_Bulucu-blue?style=for-the-badge" alt="Ropper">
  <img src="https://img.shields.io/badge/ROPgadget-Exploit_Aracı-orange?style=for-the-badge" alt="ROPgadget">
  <img src="https://img.shields.io/badge/Atlatma-NX_%7C_DEP-red?style=for-the-badge" alt="NX Atlatma">
  <img src="https://img.shields.io/badge/Kategori-Exploit_Geliştirme-green?style=for-the-badge" alt="Exploit Dev">
</p>

<p align="center">
  <b>🧩 İkili dosyalarda Yürütülemez Bellek (NX / DEP) korumalarını aşmak için Return-Oriented Programming (ROP) ve Jump-Oriented Programming (JOP) parçacıklarını bulan ve zincir oluşturan komut satırı araçları</b>
</p>

---

## 📋 İçindekiler

- [Genel Bakış ve ROP Mantığı](#-genel-bakış-ve-rop-mantığı)
- [Kurulum](#-kurulum)
- [Temel Gadget Keşfi](#-temel-gadget-keşfi)
  - [Tüm Gadget'ları Listeleme](#tüm-gadgetları-listeleme)
  - [Talimat Derinliğini Sınırlandırma](#talimat-derinliğini-sınırlandırma)
- [Gelişmiş Arama ve Desen Filtreleme](#-gelişmiş-arama-ve-desen-filtreleme)
  - [Belirli Yazmaçları Hedefleme (`rdi`, `rsi`, `rdx`, `rax`)](#belirli-yazmaçları-hedefleme-rdi-rsi-rdx-rax)
  - [Belleğe Yazma Gadget'ları (Write-What-Where)](#belleğe-yazma-gadgetları-write-what-where)
  - [Syscall ve Kesme Gadget'ları](#syscall-ve-kesme-gadgetları)
- [Bozuk Karakterleri Filtreleme (`--badbytes`)](#-bozuk-karakterleri-filtreleme---badbytes)
- [Otomatik ROP Zinciri (ROP Chain) Üretimi](#-otomatik-rop-zinciri-rop-chain-üretimi)
  - [Ropper ile Otomatik Zincir](#ropper-ile-otomatik-zincir)
  - [ROPgadget ile Otomatik Zincir](#ropgadget-ile-otomatik-zincir)
- [Yüklü Kütüphaneleri Analiz Etme (libc ROP)](#-yüklü-kütüphaneleri-analiz-etme-libc-rop)
- [Hızlı Referans Tablosu](#-hızlı-referans-tablosu)
- [Savunma Yöntemleri ve Önlemler](#-savunma-yöntemleri-ve-önlemler)
- [Yasal Uyarı](#-yasal-uyarı)

---

## 🎯 Genel Bakış ve ROP Mantığı

Modern işletim sistemleri, yığıt (stack) veya öbek (heap) alanına enjekte edilen kabuk kodunun (shellcode) doğrudan çalıştırılmasını **NX (No-Execute) / DEP (Data Execution Prevention)** mekanizmasıyla engeller.

**Return-Oriented Programming (ROP)**, belleğe yeni kod yazmak yerine, bellekte (`.text` bölümünde veya `libc.so` kütüphanesinde) zaten var olan ve sonu `ret` komutuyla biten küçük makine talimat parçacıklarını (**gadget**) ardışık olarak çağırarak bu korumayı aşar. Saldırgan bu parçacıkları birleştirerek:
1. CPU yazmaçlarına fonksiyon argümanlarını yükler (x64 ABI kuralı gereği `rdi`, `rsi`, `rdx`).
2. Bellekteki yazılabilir alana (`.data` veya `.bss`) `/bin/sh` metnini yazar.
3. `execve("/bin/sh", NULL, NULL)` sistem çağrısını tetikleyerek kabuk elde eder.

---

## 📥 Kurulum

```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y ropper python3-ropgadget

# Python pip ile Kurulum (En Güncel Sürüm)
pip install ropper capstone
pip install ROPgadget

# Arch Linux
sudo pacman -S ropper python-ropgadget
```

---

## 🚀 Temel Gadget Keşfi

### Tüm Gadget'ları Listeleme
```bash
# Ropper ile tarama (Çok çekirdekli ve hızlıdır)
ropper --file ./hedef_program --nocolor

# ROPgadget ile tarama
ROPgadget --binary ./hedef_program
```

### Talimat Derinliğini Sınırlandırma
Gereksiz uzun zincirleri elemek için `ret` öncesindeki maksimum talimat sayısını sınırlandırın:
```bash
# En fazla 3 talimat içeren gadget'ları listele
ropper --file ./hedef_program --inst-count 3
ROPgadget --binary ./hedef_program --depth 3
```

---

## 🔍 Gelişmiş Arama ve Desen Filtreleme

### Belirli Yazmaçları Hedefleme (`rdi`, `rsi`, `rdx`, `rax`)
64-bit Linux mimarisinde bir fonksiyona veya sistem çağrısına gönderilen ilk 3 parametre `rdi`, `rsi` ve `rdx` yazmaçlarına konmalıdır:

```bash
# pop rdi gadget'larını ara (1. parametre için)
ropper --file ./hedef_program --search "pop rdi"

# pop rsi gadget'larını ara (2. parametre için)
ropper --file ./hedef_program --search "pop rsi"

# pop rdx gadget'larını ara (3. parametre için)
ropper --file ./hedef_program --search "pop rdx"

# RAX yazmacını kontrol eden gadget'lar (execve için syscall numarası: 59)
ropper --file ./hedef_program --search "pop rax"
```

ROPgadget ile regex eşleştirmesi:
```bash
ROPgadget --binary ./hedef_program --only "pop|ret" | grep -E "rdi|rsi|rdx|rax"
```

### Belleğe Yazma Gadget'ları (Write-What-Where)
Belleğin yazılabilir bir bölgesine `/bin/sh` veya komut argümanı yazmak gerektiğinde:
```bash
# Yazmaç içeriğini gösterici adresine yazan talimatları ara
ropper --file ./hedef_program --search "mov [???], ???"
```
*Örnek Eşleşme:* `mov qword ptr [rdi], rsi; ret` talimatı `rsi` içerisindeki veriyi `rdi`'nin gösterdiği bellek adresine yazar.

### Syscall ve Kesme Gadget'ları
```bash
# Ham syscall talimatını arama
ropper --file ./hedef_program --search "syscall"

# 32-bit Linux x86 yazılım kesmesi
ropper --file ./hedef_program --search "int 0x80"
```

---

## 🚫 Bozuk Karakterleri Filtreleme (`--badbytes`)

Dizgi işleme fonksiyonları (`strcpy`, `scanf`) null bayt (`\x00`), yeni satır (`\x0a`) veya carriage return (`\x0d`) karakterlerinde kesilir. Adresinde bu baytlar bulunan gadget'ları filtreleyebilirsiniz:

```bash
# Bozuk bayt içeren adresleri hariç tut
ropper --file ./hedef_program --badbytes 000a0d --search "pop rdi"
ROPgadget --binary ./hedef_program --badbytes "00|0a|0d"
```

---

## ⚡ Otomatik ROP Zinciri (ROP Chain) Üretimi

İki araç da `execve("/bin/sh")` çağrısını otomatik inşa eden hazır Python exploit şablonları üretebilir:

### Ropper ile Otomatik Zincir
```bash
ropper --file ./hedef_program --chain "execve"
```

### ROPgadget ile Otomatik Zincir
```bash
ROPgadget --binary ./hedef_program --ropchain
```
*Bu komut, doğrudan Python betiğinize kopyalayabileceğiniz hazır ROP blokları dökümü sunar.*

---

## 📚 Yüklü Kütüphaneleri Analiz Etme (libc ROP)

Hedef ikili dosya **PIE** ile derlenmişse veya dosya içinde yeterli gadget yoksa, sistemin C standart kütüphanesini (`libc.so.6`) tarayın:

```bash
# libc içindeki pop rdi gadget'larını arama
ropper --file /lib/x86_64-linux-gnu/libc.so.6 --search "pop rdi; ret"

# libc içindeki "/bin/sh" dizgisinin ofsetini bulma
strings -a -t x /lib/x86_64-linux-gnu/libc.so.6 | grep "/bin/sh"
```
*(Çalışma anında sızdırılan libc taban adresine bulunan bu ofsetler eklenir).*

---

## ⚡ Hızlı Referans Tablosu

| Amaç | Ropper Komutu | ROPgadget Komutu |
|------|---------------|------------------|
| **Tüm Gadget'ları Dök** | `ropper --file <dosya>` | `ROPgadget --binary <dosya>` |
| **Özel Gadget Arama** | `ropper --file <dosya> --search "pop rdi"` | `ROPgadget --binary <dosya> \| grep "pop rdi"` |
| **Bozuk Baytları Filtrele** | `ropper --file <dosya> --badbytes 000a` | `ROPgadget --binary <dosya> --badbytes "00\|0a"` |
| **Derinlik / Uzunluk Sınırla** | `ropper --file <dosya> --inst-count 3` | `ROPgadget --binary <dosya> --depth 3` |
| **JOP Gadget'ları Ara** | `ropper --file <dosya> --type jop` | `ROPgadget --binary <dosya> --norop` |
| **Otomatik ROP Zinciri Kur**| `ropper --file <dosya> --chain "execve"` | `ROPgadget --binary <dosya> --ropchain` |
| **Belleğe Yazma Talimatı** | `ropper --file <dosya> --search "mov [???], ???"` | `ROPgadget --binary <dosya> \| grep "mov \["` |

---

## 🛡️ Savunma Yöntemleri ve Önlemler

1. **Donanım Destekli Gölge Yığıt (Intel CET / Shadow Stack):** Modern CPU'lar fonksiyon dönüş adreslerini ana yığıttan ayrı donanımsal bir alanda saklar; `ret` anında adres eşleşmezse ROP zincirini anında sonlandırır.
2. **Control Flow Integrity (CFI):** Derleyici seviyesinde uygulanan akış bütünlüğü, dolaylı atlamaların (`indirect calls`) izinli hedefler dışında çalışmasını engeller.

---

## ⚠️ Yasal Uyarı

> [!CAUTION]
> Ropper ve ROPgadget araçları exploit geliştirme, savunma mimarisi analizi ve eğitim amaçlı kullanılmalıdır. İzniniz olmayan sistemlerde bellek taşması zafiyetlerini istismar etmek yasalara aykırıdır.
