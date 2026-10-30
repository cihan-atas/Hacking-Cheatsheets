# ⚡ Radare2 (r2) - Kapsamlı Başvuru Rehberi (Cheatsheet)

[🇺🇸 English Version](../README.md)

```
  ██████╗  █████╗ ██████╗  █████╗ ██████╗ ███████╗██████╗ 
  ██╔══██╗██╔══██╗██╔══██╗██╔══██╗██╔══██╗██╔════╝╚════██╗
  ██████╔╝███████║██║  ██║███████║██████╔╝█████╗   █████╔╝
  ██╔══██╗██╔══██║██║  ██║██╔══██║██╔══██╗██╔══╝  ██╔═══╝ 
  ██║  ██║██║  ██║██████╔╝██║  ██║██║  ██║███████╗███████╗
  ╚═╝  ╚═╝╚═╝  ╚═╝╚═════╝ ╚═╝  ╚═╝╚═╝  ╚═╝╚══════╝╚══════╝
    UNIX Felsefesiyle Çalışan Komut Satırı Tersine Mühendislik Çerçevesi
```

<p align="center">
  <img src="https://img.shields.io/badge/Radare2-CLI_Tersine_Mühendislik-blue?style=for-the-badge" alt="Radare2">
  <img src="https://img.shields.io/badge/Geliştirici-Sergi_Álvarez-orange?style=for-the-badge" alt="pancake">
  <img src="https://img.shields.io/badge/Kategori-Tersine_Mühendislik-green?style=for-the-badge" alt="Tersine Mühendislik">
  <img src="https://img.shields.io/badge/Lisans-LGPLv3-purple?style=for-the-badge" alt="LGPLv3">
</p>

<p align="center">
  <b>⚡ İkili dosya analizi, tersine mühendislik, disassembly, hata ayıklama (debugging), onaltılık (hex) düzenleme ve ikili yama için terminal tabanlı açık kaynaklı çerçeve</b>
</p>

---

## 📋 İçindekiler

- [Genel Bakış ve Mimari](#-genel-bakış-ve-mimari)
- [Kurulum](#-kurulum)
- [Dosya Açma ve Oturum Modları](#-dosya-açma-ve-oturum-modları)
- [İkili Analiz Komutları (`a`)](#-ikili-analiz-komutları-a)
- [Gezinme ve Adres Atlama (`s`)](#-gezinme-ve-adres-atlama-s)
- [Yazdırma ve Disassembly (`p`)](#-yazdırma-ve-disassembly-p)
- [Etkileşimli Görsel ve Akış Grafiği Modları (`V` ve `VV`)](#-etkileşimli-görsel-ve-akış-grafiği-modları-v-ve-vv)
- [İkili Yamalama ve Kod Düzenleme (`w`)](#-ikili-yamalama-ve-kod-düzenleme-w)
- [Radare2 Yardımcı Araç Ailesi](#-radare2-yardımcı-araç-ailesi)
  - [rabin2: İkili Dosya Bilgilerini Çıkarma](#rabin2-ikili-dosya-bilgilerini-çıkarma)
  - [rasm2: Komut Satırında Assembly Derleme & Çözme](#rasm2-komut-satırında-assembly-derleme--çözme)
  - [radiff2: İkili Dosya Fark Karşılaştırma Motoru](#radiff2-ikili-dosya-fark-karşılaştırma-motoru)
  - [rafind2: Bayt ve Metin Arama](#rafind2-bayt-ve-metin-arama)
  - [rax2: Sayı ve Taban Dönüştürücü](#rax2-sayı-ve-taban-dönüştürücü)
- [r2 ile Temel Hata Ayıklama (`-d`)](#-r2-ile-temel-hata-ayıklama--d)
- [Hızlı Referans Tablosu](#-hızlı-referans-tablosu)
- [Savunma Yöntemleri ve Anti-Reversing](#-savunma-yöntemleri-ve-anti-reversing)
- [Yasal Uyarı](#-yasal-uyarı)

---

## 🎯 Genel Bakış ve Mimari

**Sergi Álvarez (pancake)** tarafından geliştirilen **Radare2** (`r2`), UNIX felsefesini ("her araç tek bir işi mükemmel yapsın") temel alan modüler ve taşınabilir bir tersine mühendislik çatısıdır. Grafik arayüz bağımlılığı olmaksızın doğrudan terminalde çalışır:
- **Geniş Mimari Desteği:** x86/x64, ARM, ARM64, MIPS, PowerPC, AVR, SPARC, WebAssembly, Java, Dalvik ve EVM.
- **Bileşen Araç Takımı:** Bağımsız CLI yardımcıları içerir (`rabin2`, `rasm2`, `radiff2`, `rafind2`, `rax2`).
- **Görsel Terminal Modları:** Bölünmüş ekran hex editörü, ASCII kontrol akış grafikleri (CFG), yazmaç monitörleri ve decompiler görünümleri.
- **Genişletilebilirlik:** Python, r2pipe, Ghidra (`r2ghidra`) ve Rizin entegrasyonu sunar.

---

## 📥 Kurulum

```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y radare2

# Arch Linux
sudo pacman -S radare2

# En güncel Git kaynağından kurulum (Önerilen)
git clone https://github.com/radareorg/radare2.git
cd radare2 && sys/install.sh

# r2ghidra decompiler eklentisini kurma
r2pm update
r2pm -i r2ghidra
```

---

## 🚀 Dosya Açma ve Oturum Modları

```bash
# Dosyayı salt okunur modda açma
r2 /bin/ls

# Dosyayı yazılabilir modda açma (yamalama için gereklidir)
r2 -w hedef.bin

# Dosyayı hata ayıklayıcı modunda açma
r2 -d hedef.bin

# Başlıkları ayrıştırmadan saf (raw) ikili/shellcode olarak açma
r2 -b 32 -n /path/to/raw.bin

# Sessiz mod (açılış afişini gösterme)
r2 -q hedef.bin
```

---

## 🔬 İkili Analiz Komutları (`a`)

`r2` komutları tek harfli veya kısa önekli hiyerarşik yapılardır. Komutun sonuna `?` ekleyerek yardım menüsünü görebilirsiniz:

```text
[0x00000000]> a?        # Analiz yardımını göster
[0x00000000]> aa        # Temel analiz (semboller, temel bloklar)
[0x00000000]> aaa       # Derin analiz (fonksiyonlar, çapraz referanslar, çağrılar)
[0x00000000]> aaaa      # Deneysel otomatik analiz

[0x00000000]> afl       # Tespit edilen tüm fonksiyonları listele
[0x00000000]> afi       # Mevcut adresteki fonksiyon bilgilerini göster
[0x00000000]> afvn      # Yerel değişkeni yeniden adlandır: afvn <yeni_ad> <eski_ad>
[0x00000000]> axt <adr> # Adrese gelen çapraz referansları (xrefs TO) bul
[0x00000000]> axf <adr> # Adresten çıkan çapraz referansları (xrefs FROM) bul
```

---

## 🧭 Gezinme ve Adres Atlama (`s`)

`seek` (`s`) komutu inceleme imlecinin adresini değiştirir:

```text
[0x00000000]> s main        # main fonksiyonunun başlangıcına git
[0x00401122]> s 0x401200    # Mutlak adrese git
[0x00401200]> s+ 32         # 32 bayt ileri git
[0x00401220]> s- 16         # 16 bayt geri git
[0x00401210]> s/ hit0_0     # Arama sonucuna git
[0x00401210]> u             # Önceki adrese geri dön (Undo seek)
[0x00401200]> U             # İleri git (Redo seek)
```

---

## 📜 Yazdırma ve Disassembly (`p`)

```text
[0x00401122]> pd 10     # Mevcut adresten itibaren 10 talimatı disassemble et
[0x00401122]> pdf       # Mevcut fonksiyonun tamamını disassemble et
[0x00401122]> pdc       # r2ghidra eklentisi ile fonksiyonu C pseudocode'a decompile et
[0x00401122]> px 64     # 64 baytlık alanı hex dump olarak göster
[0x00401122]> ps        # Adresteki metin dizgisini yazdır
[0x00401122]> pz        # Null-terminated dizgiyi yazdır
[0x00401122]> pi 5      # 5 talimatı sade formatta yazdır
```

---

## 🖥️ Etkileşimli Görsel ve Akış Grafiği Modları (`V` ve `VV`)

`V` veya `VV` tuşuna basarak Radare2'yi zengin bir konsol arayüzüne dönüştürebilirsiniz:

- **Görsel Mod (Visual Mode):** `V` yazın
  - `p` / `P` tuşlarıyla görünümleri değiştirin (Hex dökümü -> Disassembly -> Debugger -> Decompiler).
  - `c` ile imleç modunu açıp `hjkl` veya yön tuşlarıyla bayt seçin.
  - `d` tuşuyla seçili baytları kod, veri veya dizgi olarak yeniden tanımlayın.
  - `q` tuşuna basarak standart komut satırına dönün.
- **Akış Grafiği (Control Flow Graph):** `VV` yazın
  - ASCII tabanlı kod blok akış şemasını ekrana basar.
  - `Tab` / `Shift+Tab` ile koşullu dallar arasında gezinin.
  - `t` tuşuyla doğru (true), `f` tuşuyla yanlış (false) dala atlayın.
  - `u` tuşuyla önceki düğüme geri dönün.

---

## ✍️ İkili Yamalama ve Kod Düzenleme (`w`)

Diskteki ikili dosyayı doğrudan değiştirmek için r2'nin yazma modunda açıldığından emin olun (`r2 -w hedef` veya aktif oturumda `oo+`):

```text
[0x00401150]> oo+                           # Mevcut oturumu yazma izniyle yeniden aç
[0x00401150]> wa nop                        # Adrese 'nop' assembly talimatı yaz
[0x00401150]> wa jmp 0x401190               # Atlama talimatını değiştir
[0x00401150]> wx 90909090                   # Doğrudan hex bayt yaz (4 adet NOP)
[0x00401150]> w "Yeni Metin\x00"            # Dizgiyi ez ve sonuna null bayt koy
[0x00401150]> w0 16                         # 16 baytlık alanı sıfırla (NUL bayt)
```

---

## 🛠️ Radare2 Yardımcı Araç Ailesi

Radare2 ile birlikte gelen bağımsız CLI araçları:

### rabin2: İkili Dosya Bilgilerini Çıkarma
```bash
# Mimari, bit genişliği ve güvenlik önlemleri (NX, PIE, Canary)
rabin2 -I hedef.bin

# İçe aktarılan fonksiyonlar (libc çağrıları)
rabin2 -i hedef.bin

# Dışa aktarılan semboller
rabin2 -E hedef.bin

# Dosyadaki ASCII ve UTF-16 dizgiler
rabin2 -z hedef.bin

# Dosya bölümleri (.text, .data, .rodata)
rabin2 -S hedef.bin
```

### rasm2: Komut Satırında Assembly Derleme & Çözme
```bash
# x64 assembly talimatlarını hex opcode'a çevirme
rasm2 -a x86 -b 64 "xor eax, eax; ret"
# Çıktı: 31c0c3

# Hex opcode'ları assembly talimatlarına çözme
rasm2 -a x86 -b 64 -d "31c0c3"
# Çıktı:
# xor eax, eax
# ret
```

### radiff2: İkili Dosya Fark Karşılaştırma Motoru
```bash
# İki ikili dosya arasındaki bayt farkı sayısını dökme
radiff2 -c orijinal.bin yamalanmis.bin

# Fonksiyon bazında kod karşılaştırma grafiği
radiff2 -g main orijinal.bin yamalanmis.bin | xdot -
```

### rafind2: Bayt ve Metin Arama
```bash
# Dosya içinde düz metin arama
rafind2 -s "admin_parola" hedef.bin

# Hexadecimal desen arama
rafind2 -x "4889e5" hedef.bin
```

### rax2: Sayı ve Taban Dönüştürücü
```bash
# Hex'ten onluğa (decimal) çevirme
rax2 0x1337      # Çıktı: 4919

# Onluktan hex'e çevirme
rax2 4919        # Çıktı: 0x1337

# Düz metni hex'e çevirme
rax2 -S "admin"  # Çıktı: 61646d696e
```

---

## 🐛 r2 ile Temel Hata Ayıklama (`-d`)

```bash
# Argümanlarla birlikte hata ayıklayıcıyı başlatma
r2 -d hedef.bin arg1 arg2
```

Hata ayıklayıcı kabuğu içinde:
```text
[0x7f112233]> db main       # main fonksiyonuna breakpoint koy
[0x7f112233]> db 0x401150   # Belirli bir adrese breakpoint koy
[0x7f112233]> dbl           # Aktif breakpoint'leri listele
[0x7f112233]> dc            # Breakpoint'e kadar yürüt (continue)
[0x7f112233]> ds            # Tek talimat adım at (step into)
[0x7f112233]> dso           # Fonksiyon çağrısının üzerinden atla (step over)
[0x7f112233]> dr            # CPU yazmaçlarını listele
[0x7f112233]> dr rax        # Belirli yazmaç değerini göster
[0x7f112233]> dr rax = 0x1  # Yazmaç değerini değiştir
```

---

## ⚡ Hızlı Referans Tablosu

| Amaç | Komut |
|------|-------|
| **Derin Analiz** | `aaa` |
| **Fonksiyonları Listele** | `afl` |
| **Adrese / Fonksiyona Git** | `s <adres>` / `s main` |
| **Fonksiyonu Disassemble Et** | `pdf` |
| **Decompile Et (r2ghidra)** | `pdc` |
| **Görsel Disassembly Modu** | `V` |
| **ASCII Akış Şeması** | `VV` |
| **Assembly Talimatı Yaz** | `wa <talimat>` |
| **Hex Bayt Yaz** | `wx <baytlar>` |
| **Yazma Moduna Geç** | `oo+` |
| **İkili Bilgilerini Dök** | `rabin2 -I <dosya>` |
| **CLI'dan Assembly Çevir** | `rasm2 -a x86 -b 64 "<asm>"` |

---

## 🛡️ Savunma Yöntemleri ve Anti-Reversing

1. **Anti-Debugging Tespiti:** Radare2 hata ayıklayıcısı standart Linux `ptrace` çağrısını kullanır. Dosya içindeki `ptrace(PTRACE_TRACEME)` kontrolleri yamalanmadıkça bağlanmayı engeller.
2. **Kod Karıştırma (Obfuscation):** OLLVM gibi araçlarla kontrol akışı düzleştirilmiş fonksiyonları analiz etmek için sembolik yürütme araçlarından (angr gibi) faydalanılabilir.

---

## ⚠️ Yasal Uyarı

> [!CAUTION]
> Radare2 aracı yalnızca yetkili güvenlik testleri, zararlı yazılım analizi, zafiyet araştırmaları ve eğitim amacıyla kullanılmalıdır. İzniniz olmayan ticari yazılımları tersine mühendislikle incelemek son kullanıcı lisans anlaşmalarına (EULA) ve telif hakları yasalarına aykırı olabilir.
