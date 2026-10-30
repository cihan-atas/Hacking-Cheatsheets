# ⛓️ Proxychains - Kapsamlı Başvuru Kılavuzu

[🇬🇧 English Version](../README.md)

```
  ██████╗ ██████╗  ██████╗ ██╗  ██╗██╗   ██╗ ██████╗██╗  ██╗ █████╗ ██╗███╗   ██╗███████╗
  ██╔══██╗██╔══██╗██╔═══██╗╚██╗██╔╝╚██╗ ██╔╝██╔════╝██║  ██║██╔══██╗██║████╗  ██║██╔════╝
  ██████╔╝██████╔╝██║   ██║ ╚███╔╝  ╚████╔╝ ██║     ███████║███████║██║██╔██╗ ██║███████╗
  ██╔═══╝ ██╔══██╗██║   ██║ ██╔██╗   ╚██╔╝  ██║     ██╔══██║██╔══██║██║██║╚██╗██║╚════██║
  ██║     ██║  ██║╚██████╔╝██╔╝ ██╗   ██║   ╚██████╗██║  ██║██║  ██║██║██║ ╚████║███████║
  ╚═╝     ╚═╝  ╚═╝ ╚═════╝ ╚═╝  ╚═╝   ╚═╝    ╚═════╝╚═╝  ╚═╝╚═╝  ╚═╝╚═╝╚═╝  ╚═══╝╚══════╝
       Herhangi Bir TCP Bağlantısını SOCKS4, SOCKS5 ve HTTP Proxy Üzerinden Tünelleme Aracı
```

<p align="center">
  <img src="https://img.shields.io/badge/Proxychains-Proxychains--NG-blue?style=for-the-badge" alt="Proxychains">
  <img src="https://img.shields.io/badge/Kategori-Pivotlama_&_Tünelleme-orange?style=for-the-badge" alt="Pivotlama & Tünelleme">
  <img src="https://img.shields.io/badge/Protokoller-SOCKS4_SOCKS5_HTTP-green?style=for-the-badge" alt="Protokoller">
  <img src="https://img.shields.io/badge/Lisans-GPLv2-red?style=for-the-badge" alt="Lisans GPLv2">
</p>

<p align="center">
  <b>🌐 Linux komut satırı araçlarının ağ trafiğini LD_PRELOAD kancasıyla SOCKS ve HTTP vekilleri üzerinden dinamik olarak yönlendirme aracı</b>
</p>

---

## 📋 İçindekiler

- [Genel Bakış & Proxychains Nasıl Çalışır?](#-genel-bakış--proxychains-nasıl-çalışır)
- [Kurulum](#-kurulum)
- [Yapılandırma Dosyaları & Arama Önceliği](#-yapılandırma-dosyaları--arama-önceliği)
- [Zincirleme Modları (Chaining Modes)](#-zincirleme-modları-chaining-modes)
  - [`dynamic_chain` (Tavsiye Edilen)](#dynamic_chain-tavsiye-edilen)
  - [`strict_chain`](#strict_chain)
  - [`round_robin_chain`](#round_robin_chain)
  - [`random_chain`](#random_chain)
- [DNS Sızıntısını Önleme (`proxy_dns`)](#-dns-sızıntısını-önleme-proxy_dns)
- [Proxy Listesi Tanımlama](#-proxy-listesi-tanımlama)
- [Pivotlama Senaryoları](#-pivotlama-senaryoları)
  - [SSH Dinamik SOCKS Proxy (`ssh -D`)](#ssh-dinamik-socks-proxy-ssh--d)
  - [Chisel & Ligolo-ng SOCKS Entegrasyonu](#chisel--ligolo-ng-socks-entegrasyonu)
- [Araç Entegrasyonları & Kritik İpuçları](#-araç-entegrasyonları--kritik-ipuçları)
  - [Proxychains ile Nmap (Neden `-sS` Çalışmaz?)](#proxychains-ile-nmap-neden--ss-çalışmaz)
  - [Active Directory Araçları (NetExec, Evil-WinRM, Impacket)](#active-directory-araçları-netexec-evil-winrm-impacket)
- [Hızlı Referans Tablosu](#-hızlı-referans-tablosu)
- [Savunma Açısından Değerlendirme & OPSEC](#-savunma-açısından-değerlendirme--opsec)
- [Yasal Uyarı](#-yasal-uyarı)

---

## 🎯 Genel Bakış & Proxychains Nasıl Çalışır?

**Proxychains** (güncel sürüm: **Proxychains-NG** / `proxychains4`), herhangi bir Linux komut satırı aracının ağ trafiğini tekli veya zincirleme SOCKS4, SOCKS5 ya da HTTP vekil sunucuları (proxy) üzerinden geçiren kritik bir sızma testi aracıdır.

Çalışma prensibi, dinamik bağlayıcı (`LD_PRELOAD`) mekanizmasıyla `libproxychains4.so` kütüphanesini hedef uygulamanın önüne yüklemektir. Bu sayede uygulamanın yaptığı `connect()` ve `gethostbyname()` gibi C soket çağrıları kancalanır (hook) ve trafik fark ettirilmeden yapılandırılmış proxy tüneline yönlendirilir.

---

## 📥 Kurulum

```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y proxychains4

# Arch Linux
sudo pacman -S proxychains-ng

# macOS (Homebrew)
brew install proxychains-ng
```

---

## ⚙️ Yapılandırma Dosyaları & Arama Önceliği

Proxychains yapılandırma dosyasını şu öncelik sırasına göre arar:

1. `PROXYCHAINS_CONF_FILE` ortam değişkeninde tanımlı dosya yolu.
2. `-f` parametresiyle doğrudan verilen dosya (`proxychains4 -f ./ozel.conf <komut>`).
3. Çalışılan geçerli dizindeki `./proxychains.conf`.
4. `$(HOME)/.proxychains/proxychains.conf`.
5. Sistem geneli varsayılan: `/etc/proxychains4.conf`.

---

## 🔗 Zincirleme Modları (Chaining Modes)

`/etc/proxychains4.conf` dosyasını düzenleyerek yönlendirme mantığını belirleyebilirsiniz:

### `dynamic_chain` (Tavsiye Edilen)
Listede yanıt vermeyen (ölü) proxy'leri otomatik atlar ve bağlantıyı koparmaz. Proxy 1 aktif, Proxy 2 kapalı, Proxy 3 aktifse trafik 1 -> 3 şeklinde akar.
```ini
dynamic_chain
# strict_chain
```

### `strict_chain`
Listede belirtilen proxy sırasına katı şekilde uyar. Zincirdeki tek bir vekil bile yanıt vermezse tüm bağlantı iptal edilir.
```ini
strict_chain
```

### `round_robin_chain`
Her yeni TCP bağlantı talebinde proxy listesini sırayla döner.
```ini
round_robin_chain
chain_len = 2
```

### `random_chain`
Listedeki proxy'lerden rastgele seçim yapar (`chain_len` kadar).

---

## 🛡️ DNS Sızıntısını Önleme (`proxy_dns`)

Operasyonel güvenlikte (OPSEC) en yaygın hatalardan biri DNS sorgularının yerel ISP'ye sızmasıdır. Proxychains'in DNS sorgularını da SOCKS vekili üzerinden yapması hayati önem taşır.

`/etc/proxychains4.conf` içinde şu satırların aktif olduğundan emin olun:
```ini
# DNS sorgularını SOCKS5 üzerinden tünelle
proxy_dns

# Konsola basılan gereksiz kütüphane kanca loglarını gizle
quiet_mode
```

---

## 📝 Proxy Listesi Tanımlama

`/etc/proxychains4.conf` dosyasının en altındaki `[ProxyList]` bölümüne vekil sunucuları ekleyin:

```ini
[ProxyList]
# Protokol   IP               Port   [Kullanıcı]  [Parola]
socks5       127.0.0.1        1080
socks4       192.168.1.100    1080
http         10.10.10.50      8080   admin        GizliParola123
```

---

## 🚀 Pivotlama Senaryoları

### SSH Dinamik SOCKS Proxy (`ssh -D`)

```bash
# 1. Ele geçirilen ara sunucu üzerinden yerel 1080 portunda arka plan SOCKS5 vekili açma
ssh -f -N -D 127.0.0.1:1080 kurban@10.10.10.50

# 2. /etc/proxychains4.conf dosyasına ekleme yapın:
# socks5 127.0.0.1 1080

# 3. Saldırgan makineden doğrudan erişilemeyen iç ağ web sunucusuna erişim
proxychains4 curl http://172.16.20.10
```

### Chisel & Ligolo-ng SOCKS Entegrasyonu

```bash
# Chisel ters SOCKS tüneli bağlandığında
chisel client 10.10.14.5:8000 R:1080:socks

# Araçları iç ağa yönlendirme
proxychains4 crackmapexec smb 172.16.50.0/24
```

---

## ⚠️ Araç Entegrasyonları & Kritik İpuçları

### Proxychains ile Nmap (Neden `-sS` Çalışmaz?)

> [!WARNING]
> SOCKS vekilleri yalnızca tam el sıkışmalı TCP oturumlarını destekler. **SYN Stealth taramaları (`-sS`) ham soket kullandığı için SOCKS üzerinden ÇALIŞMAZ veya takılı kalır!**

```bash
# Doğru Nmap Sözdizimi (Tam TCP Bağlantısı, Ping Yok, DNS Yok)
proxychains4 nmap -sT -Pn -n -sV -p 80,443,445,3389 172.16.20.10

# Vekil üzerinden en popüler 100 portu tarama
proxychains4 nmap -sT -Pn -n --top-ports 100 172.16.20.10
```

### Active Directory Araçları (NetExec, Evil-WinRM, Impacket)

```bash
# İç ağ SMB keşfi
proxychains4 nxc smb 172.16.20.10 -u Administrator -p 'Parola123'

# Tünel üzerinden PowerShell WinRM oturumu
proxychains4 evil-winrm -i 172.16.20.10 -u Administrator -p 'Parola123'

# Impacket secretsdump ile hash çekme
proxychains4 impacket-secretsdump domain.local/Administrator:Pass@172.16.20.10 -no-pass
```

---

## ⚡ Hızlı Referans Tablosu

| İşlem | Komut |
|-------|-------|
| **Komutu Proxy ile Çalıştırma** | `proxychains4 <komut> [argümanlar]` |
| **Özel Yapılandırma Dosyası** | `proxychains4 -f ./ozel.conf <komut>` |
| **Sessiz Çalıştırma (Logsuz)** | `proxychains4 -q <komut>` |
| **İç Ağı Nmap ile Tarama** | `proxychains4 nmap -sT -Pn -n -p 80,445 <IP>` |
| **İç Ağ Web Sayfasına İstek** | `proxychains4 curl -i http://<IC_AG_IP>` |
| **İç Ağda SQLMap Çalıştırma** | `proxychains4 sqlmap -u "http://<IP>/id=1" --batch` |
| **İç Ağda NetExec Taraması** | `proxychains4 nxc smb <ALT_AG>/24` |

---

## 🛡️ Savunma Açısından Değerlendirme & OPSEC

1. **DNS Sızıntıları:** `proxy_dns` kapalıysa işletim sistemi DNS çözümlemelerini yerel ağ geçidinden yapar ve hedef alan adlarını ifşa eder.
2. **Gecikme (Latency):** Çoklu proxy zincirleri yanıt süresini uzatır. Zaman aşımı değerlerini (`tcp_connect_time_out 8000`) ihtiyaca göre optimize edin.
3. **Statik İkililer:** Go veya Rust ile derlenmiş ve `libc.so` bağlantısı olmayan statik programlar `LD_PRELOAD` kancasını tetiklemediği için Proxychains tarafından yakalanmaz ve doğrudan yerel ağdan çıkar!

---

## ⚠️ Yasal Uyarı

> [!CAUTION]
> Bu kılavuz yalnızca izinli sızma testleri, iç ağ denetimleri ve siber güvenlik eğitimleri amacıyla hazırlanmıştır. Yetkisiz ağ tünellemesi ve erişim girişimleri suç teşkil eder.