# 🎯 pwndbg & GEF - Kapsamlı Başvuru Rehberi (Cheatsheet)

[🇺🇸 English Version](../README.md)

```
  ██████╗ ██╗    ██╗███╗   ██╗██████╗ ██████╗  ██████╗      ██████╗ ███████╗███████╗
  ██╔══██╗██║    ██║████╗  ██║██╔══██╗██╔══██╗██╔════╝     ██╔════╝ ██╔════╝██╔════╝
  ██████╔╝██║ █╗ ██║██╔██╗ ██║██║  ██║██████╔╝██║  ███╗    ██║  ███╗█████╗  █████╗  
  ██╔═══╝ ██║███╗██║██║╚██╗██║██║  ██║██╔══██╗██║   ██║    ██║   ██║██╔══╝  ██╔══╝  
  ██║     ╚███╔███╔╝██║ ╚████║██████╔╝██████╔╝╚██████╔╝    ╚██████╔╝███████╗██║     
  ╚═╝      ╚══╝╚══╝ ╚═╝  ╚═══╝╚═════╝ ╚═════╝  ╚═════╝      ╚═════╝ ╚══════╝╚═╝     
    Tersine Mühendislik ve İkili İstismar (Pwn) İçin Yeni Nesil GDB Eklentileri
```

<p align="center">
  <img src="https://img.shields.io/badge/pwndbg-GDB_Eklentisi-blue?style=for-the-badge" alt="pwndbg">
  <img src="https://img.shields.io/badge/GEF-Gelişmiş_GDB_Özellikleri-orange?style=for-the-badge" alt="GEF">
  <img src="https://img.shields.io/badge/Kategori-Tersine_Mühendislik_&_Pwn-green?style=for-the-badge" alt="Reversing & Pwn">
  <img src="https://img.shields.io/badge/Lisans-MIT_%7C_Açık_Kaynak-purple?style=for-the-badge" alt="Açık Kaynak">
</p>

<p align="center">
  <b>🎯 Standart GNU Debugger (GDB) aracını modern bir tersine mühendislik, bellek taşması (buffer overflow), heap analizi ve CTF exploit geliştirme canavarına dönüştüren açık kaynaklı CLI eklentileri</b>
</p>

---

## 📋 İçindekiler

- [Genel Bakış: Standart GDB Neden Yetersiz Kalır?](#-genel-bakış-standart-gdb-neden-yetersiz-kalır)
- [Kurulum ve Eklentiler Arasında Geçiş](#-kurulum-ve-eklentiler-arasında-geçiş)
  - [pwndbg Kurulumu](#pwndbg-kurulumu)
  - [GEF Kurulumu](#gef-kurulumu)
  - [`.gdbinit` Dosyasını Yönetme](#gdbinit-dosyasını-yönetme)
- [Görsel Context Paneli](#-görsel-context-paneli)
  - [Yazmaçlar (Registers) ve Bayraklar](#yazmaçlar-registers-ve-bayraklar)
  - [Disassembly ve Atlama Tahminleri](#disassembly-ve-atlama-tahminleri)
  - [Yığıt (Stack) ve Telescope](#yığıt-stack-ve-telescope)
  - [Çağrı İzi (Backtrace)](#çağrı-izi-backtrace)
- [Bellek İnceleme ve Gezinme](#-bellek-inceleme-ve-gezinme)
  - [Özyinelemeli Gösterici Çözme (`telescope`)](#özyinelemeli-gösterici-çözme-telescope)
  - [Süreç Bellek Haritası (`vmmap`)](#süreç-bellek-haritası-vmmap)
  - [Bellekte Arama Yapma (`search`)](#bellekte-arama-yapma-search)
- [Heap İstismarı ve İnceleme](#-heap-istismarı-ve-inceleme)
  - [Chunk ve Arena Yapılarını İnceleme](#chunk-ve-arena-yapılarını-inceleme)
  - [Serbest Kutuları İnceleme (Tcache, Fastbins, Unsortedbin)](#serbest-kutuları-inceleme-tcache-fastbins-unsortedbin)
  - [Heap Belleğini Görselleştirme (`vis_heap_chunks`)](#heap-belleğini-görselleştirme-vis_heap_chunks)
- [Exploit Geliştirme Yardımcıları](#-exploit-geliştirme-yardımcıları)
  - [Checksec ile İkili Güvenlik Korumalarını Denetleme](#checksec-ile-ikili-güvenlik-korumalarını-denetleme)
  - [Stack Canary Değerini Bulma (`canary`)](#stack-canary-değerini-bulma-canary)
  - [Cyclic Desen ile Bellek Taşması Mesafesini Hesaplama](#cyclic-desen-ile-bellek-taşması-mesafesini-hesaplama)
  - [Bellekte Canlı ROP Gadget Avı](#bellekte-canlı-rop-gadget-avı)
- [Hızlı Referans Tablosu](#-hızlı-referans-tablosu)
- [Savunma Yöntemleri ve İkili Koruma Mekanizmaları](#-savunma-yöntemleri-ve-ikili-koruma-mekanizmaları)
- [Yasal Uyarı](#-yasal-uyarı)

---

## 🎯 Genel Bakış: Standart GDB Neden Yetersiz Kalır?

Linux sistemlerde standart gelen GNU Debugger (`gdb`), endüstri standardı bir hata ayıklayıcı olmasına rağmen arayüzü son derece sadedir. Her adımda yazmaç durumlarını otomatik yenilemez, iç içe işaretçileri (multi-level pointers) otomatik çözümlemez ve yığıtın (stack) o anki durumunu görsel olarak ekrana basmaz.

**pwndbg** ve **GEF**, GDB'yi üst düzey bir ikili analiz ve zafiyet istismar paketine dönüştürür:
- **pwndbg:** Düşük seviyeli bellek sömürüsü, glibc heap mimarisi (tcache, fastbins vb.) ve otomatik işaretçi çözümü için optimize edilmiştir.
- **GEF (GDB Enhanced Features):** Bağımsız tek bir Python betiği olarak çalışır; harici kütüphane bağımlılığı yoktur ve mimari bağımsız (x86, ARM, MIPS) analiz yetenekleri sunar.

---

## 📥 Kurulum ve Eklentiler Arasında Geçiş

### pwndbg Kurulumu
```bash
git clone https://github.com/pwndbg/pwndbg
cd pwndbg
./setup.sh
```

### GEF Kurulumu
```bash
# Tek satırda otomatik kurulum
bash -c "$(curl -fsSL https://gef.blah.cat/sh)"
```

### `.gdbinit` Dosyasını Yönetme
`~/.gdbinit` dosyanız üzerinden eklentiler arasında anında geçiş yapabilirsiniz:
```bash
# pwndbg'yi aktif etmek için:
echo "source /path/to/pwndbg/gdbinit.py" > ~/.gdbinit

# GEF'i aktif etmek için:
echo "source ~/.gdbinit-gef.py" > ~/.gdbinit
```

---

## 🖥️ Görsel Context Paneli

Program bir kesme noktasında (breakpoint) durduğunda veya tek adım ilerlediğinde, ekran otomatik olarak çok bölmeli renkli panellerle güncellenir:

```text
──────────────────────────[ REGISTERS / FLAGS ]──────────────────────────
RAX: 0x0 
RBX: 0x7fffffffe000 
RCX: 0x7ffff7ec2000 --> 0x00010102464c457f 
RDX: 0x7fffffffe1a0 --> 0x7fffffffe494 ('USER=zeus')
RSI: 0x7fffffffe198 --> 0x7fffffffe47e ('/bin/target')
RDI: 0x1 
RBP: 0x7fffffffe0a0 --> 0x7fffffffe0c0 --> 0x0
RSP: 0x7fffffffe080 --> 0x401156 (<main+34>: mov eax, 0)
RIP: 0x401140 (<main+12>: cmp QWORD PTR [rbp-0x8], 0x0)
──────────────────────────[ DISASM / CODE ]──────────────────────────
   0x401135 <main+1>     push   rbp
   0x401136 <main+2>     mov    rbp, rsp
   0x401139 <main+5>     sub    rsp, 0x10
 ► 0x401140 <main+12>    cmp    QWORD PTR [rbp-0x8], 0
   0x401145 <main+17>    je     0x401151 <main+29>  <-- ŞART SAĞLANDI (ATLANACAK)
   0x401147 <main+19>    mov    eax, 0x1
──────────────────────────[ STACK ]──────────────────────────
00:0000│ rsp 0x7fffffffe080 --> 0x401156 (<main+34>: mov eax, 0)
01:0008│     0x7fffffffe088 --> 0x0
02:0010│ rbp 0x7fffffffe0a0 --> 0x7fffffffe0c0 --> 0x0
03:0018│     0x7fffffffe0a8 --> 0x7ffff7df6d90 (<__libc_start_call_main+128>: mov edi, eax)
──────────────────────────[ BACKTRACE ]──────────────────────────
 ► 0   0x401140 main+12
   1   0x7ffff7df6d90 __libc_start_call_main+128
   2   0x7ffff7df6e40 __libc_start_main+128
   3   0x401065 _start+37
```

---

## 🔍 Bellek İnceleme ve Gezinme

### Özyinelemeli Gösterici Çözme (`telescope`)
İşaretçileri iç içe çözerek bellekteki metinleri, sembolleri ve adresleri zincirleme ekrana basar:
```text
pwndbg> telescope $rsp 16       # RSP adresinden itibaren 16 kaydı zincirleme çöz
pwndbg> telescope 0x404000 8    # Veri (data) bölümünü incele
```

### Süreç Bellek Haritası (`vmmap`)
Sürecin tüm sanal bellek alanlarını ve okuma/yazma/yürütme (`rwx`) izinlerini dökme:
```text
pwndbg> vmmap
```
*`rwx` izinli sayfalar doğrudan tespit edilebilir (kabuk kodu enjeksiyonu için kritik).*

### Bellekte Arama Yapma (`search`)
Bellek sayfalarında dizgi, sayı veya işaretçi arama:
```text
pwndbg> search -s "/bin/sh"     # Bellekte "/bin/sh" dizgisini ara
pwndbg> search -p 0x401135      # main() adresine işaret eden göstericileri bul
pwndbg> search -x "41414141"    # Ham hex baytlarını ara
```

---

## 🧱 Heap İstismarı ve İnceleme

Modern `glibc` bellek yöneticisini incelemek için hazır komutlar:

### Chunk ve Arena Yapılarını İnceleme
```text
pwndbg> heap                    # Tüm heap haritasını dök
pwndbg> chunk 0x602000          # Belirli chunk başlığını (boyut ve bayraklar) analiz et
pwndbg> arenas                  # Aktif heap arena yapılarını listele
```

### Serbest Kutuları İnceleme (Tcache, Fastbins, Unsortedbin)
```text
pwndbg> bins                    # Tüm serbest heap listelerini aynı anda göster
pwndbg> tcache                  # İş parçacığına özel tcache yapısını dök
pwndbg> fastbins                # Tek yönlü bağlı fastbin listesini dök
pwndbg> unsortedbin             # Unsortedbin chunk'larını listele (libc sızıntısı için)
```

### Heap Belleğini Görselleştirme (`vis_heap_chunks`)
```text
pwndbg> vis_heap_chunks 10      # Ardışık heap bloklarını renkli görsel kutular olarak ekrana çiz
```

---

## 🛠️ Exploit Geliştirme Yardımcıları

### Checksec ile İkili Güvenlik Korumalarını Denetleme
Derleyici güvenlik önlemlerini doğrudan GDB içinden görüntüleyin:
```text
pwndbg> checksec
```
*Denetlenenler:*
- **RELRO:** Partial veya Full RELRO (Global Offset Table koruması)
- **Stack:** Canary koruması aktif mi?
- **NX:** Yürütülemez yığıt/heap koruması
- **PIE:** Pozisyondan bağımsız ikili dosya (ASLR desteği)

### Stack Canary Değerini Bulma (`canary`)
Thread Local Storage (TLS) içerisindeki canary değerini ve o anki yığıt üzerindeki konumunu doğrudan tespit edin:
```text
pwndbg> canary
```

### Cyclic Desen ile Bellek Taşması Mesafesini Hesaplama
EIP / RIP yazmacını ezmek için gereken tam bayt sayısını hesaplama:
```text
# 100 baytlık De Bruijn deseni üret
pwndbg> cyclic 100

# Çökme anında (SIGSEGV), RIP değerinden ofseti bul
pwndbg> cyclic -l $rip
# Çıktı: Finding cyclic pattern at offset 72
```

### Bellekte Canlı ROP Gadget Avı
Yüklü çalıştırılabilir bellek alanlarında ROP parçacıklarını arama:
```text
pwndbg> rop --grep "pop rdi"
```

---

## ⚡ Hızlı Referans Tablosu

| Amaç | Komut |
|------|-------|
| **GDB'yi Başlat** | `gdb ./hedef_program` |
| **Görsel Paneli Yenile** | `context` |
| **Zincirleme Gösterici Çöz** | `telescope $rsp 10` |
| **Bellek Haritası & İzinler**| `vmmap` |
| **Bellekte Dizgi Arama** | `search -s "/bin/sh"` |
| **Güvenlik Önlemlerini Oku** | `checksec` |
| **Stack Canary Değerini Bul**| `canary` |
| **Heap Haritasını Dök** | `heap` |
| **Serbest Kutuları İncele** | `bins` |
| **Taşma Ofseti Hesapla** | `cyclic 100` -> `cyclic -l <değer>` |
| **Kesme Noktası Koy** | `b *main` |
| **Çalıştır / Devam / Adım**| `r` / `c` / `si` / `ni` |

---

## 🛡️ Savunma Yöntemleri ve İkili Koruma Mekanizmaları

1. **Full RELRO:** `-Wl,-z,relro,-z,now` parametreleriyle derleme yapılarak `.got` alanı salt okunur hale getirilir ve GOT overwrite saldırıları engellenir.
2. **Stack Canary:** `-fstack-protector-all` ile derlenerek dönüş adresinden hemen önce rastgele kontrol baytı konur; taşma anında `__stack_chk_fail` çağrılarak program anında durdurulur.
3. **PIE ve ASLR:** Kod, kütüphane ve yığıt adreslerinin her çalıştırmada rastgele belleğe yüklenmesini sağlar.

---

## ⚠️ Yasal Uyarı

> [!CAUTION]
> pwndbg ve GEF araçları yalnızca yetkili güvenlik araştırmaları, savunma amaçlı yazılım denetimleri ve CTF yarışmaları için kullanılmalıdır. İzniniz olmayan yazılımları istismar etmek suçtur.
