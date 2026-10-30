# 🛰️ sshuttle - Kapsamlı Başvuru Kılavuzu

[🇬🇧 English Version](../README.md)

```
  ███████╗███████╗██╗  ██╗██╗   ██╗████████╗████████╗██╗     ███████╗
  ██╔════╝██╔════╝██║  ██║██║   ██║╚══██╔══╝╚══██╔══╝██║     ██╔════╝
  ███████╗███████╗███████║██║   ██║   ██║      ██║   ██║     █████╗  
  ╚════██║╚════██║██╔══██║██║   ██║   ██║      ██║   ██║     ██╔══╝  
  ███████║███████║██║  ██║╚██████╔╝   ██║      ██║   ███████╗███████╗
  ╚══════╝╚══════╝╚═╝  ╚═╝ ╚═════╝    ╚═╝      ╚═╝   ╚══════╝╚══════╝
       SSH Üzerinden Sıfır Yapılandırmayla Çalışan Şeffaf VPN ve Pivotlama Aracı
```

<p align="center">
  <img src="https://img.shields.io/badge/sshuttle-Fakir_Adamın_VPN'i-blue?style=for-the-badge" alt="sshuttle">
  <img src="https://img.shields.io/badge/Kategori-Pivotlama_&_Tünelleme-orange?style=for-the-badge" alt="Pivotlama & Tünelleme">
  <img src="https://img.shields.io/badge/Protokoller-TCP_DNS_SSH-green?style=for-the-badge" alt="Protokoller">
  <img src="https://img.shields.io/badge/Lisans-LGPLv2.1-red?style=for-the-badge" alt="Lisans LGPLv2.1">
</p>

<p align="center">
  <b>🌐 Uzak pivot sunucusunda root yetkisi gerekmeden, standart bir SSH oturumu üzerinden tüm iç ağ alt ağlarını ve DNS trafiğini şeffaf olarak tünelleme aracı</b>
</p>

---

## 📋 İçindekiler

- [Genel Bakış & "Fakir Adamın VPN'i" Felsefesi](#-genel-bakış--fakir-adamın-vpni-felsefesi)
- [sshuttle Nasıl Çalışır?](#-sshuttle-nasıl-çalışır)
- [Araç Karşılaştırması: sshuttle vs SOCKS vs Ligolo-ng vs OpenVPN](#-araç-karşılaştırması-sshuttle-vs-socks-vs-ligolo-ng-vs-openvpn)
- [Kurulum](#-kurulum)
- [Temel Alt Ağ (Subnet) Yönlendirme](#-temel-alt-ağ-subnet-yönlendirme)
- [DNS Trafiğini Yönlendirme (`--dns`)](#-dns-trafiğini-yönlendirme---dns)
- [Tüm Trafiği Tünelleme (Varsayılan Ağ Geçidi)](#-tüm-trafiği-tünelleme-varsayılan-ağ-geçidi)
- [Belirli IP/Alt Ağları Hariç Tutma (`-x`)](#-belirli-ipalt-ağları-hariç-tutma--x)
- [Özel SSH Parametreleri & Anahtar Kullanımı (`-e`)](#-özel-ssh-parametreleri--anahtar-kullanımı--e)
- [Arka Planda Daemon Olarak Çalıştırma (`-D`)](#-arka-planda-daemon-olarak-çalıştırma--d)
- [Hızlı Referans Tablosu](#-hızlı-referans-tablosu)
- [Savunma Açısından Değerlendirme & OPSEC](#-savunma-açısından-değerlendirme--opsec)
- [Yasal Uyarı](#-yasal-uyarı)

---

## 🎯 Genel Bakış & "Fakir Adamın VPN'i" Felsefesi

**sshuttle**, standart bir SSH bağlantısı üzerinden çalışan ve istemci makinede yapılandırma gerektirmeyen şeffaf bir vekil sunucu / VPN yazılımıdır.

Geleneksel kurumsal sızma testlerinde iç ağlara pivotlama yaparken üç büyük engelle karşılaşılır:
- ❌ Ele geçirilen pivot sunucusunda genellikle `root` veya `SYSTEM` yetkisi bulunmaz.
- ❌ OpenVPN veya WireGuard gibi gerçek VPN çözümleri her iki tarafta da yönetici yetkisi ve TUN/TAP sanal ağ aygıtları gerektirir.
- ❌ Standart SOCKS vekilleri (`ssh -D`), her bir araç için ayrı ayrı `proxychains` yapılandırmayı zorunlu kılar.

**sshuttle bu üç engeli aynı anda ortadan kaldırır:**
- ✅ **Uzakta Root Gerekmez:** Pivot sunucuda yalnızca standart Python (`python3`) bulunması yeterlidir.
- ✅ **Uzakta TUN/TAP Aygıtı Gerekmez:** Trafiği SSH kanalı üzerinden saf TCP akışı halinde çoklar (multiplex).
- ✅ **Sistem Geneli Şeffaf Yönlendirme:** Saldırganın yerel makinesindeki `iptables` kurallarını düzenleyerek `nmap`, `curl`, `smbclient` ve web tarayıcılarının ara katman olmadan doğrudan iç IP'lere erişmesini sağlar!

---

## ⚙️ sshuttle Nasıl Çalışır?

```
[ Saldırgan Makinesi (Yerel Root) ]
  ├── iptables kuralları iç ağ hedeflerini (10.10.10.0/24) yerel sshuttle portuna saptırır
  └── sshuttle istemcisi TCP akışlarını SSH tüneline basar
              │
              ▼ (Şifreli SSH Kanalı - Port 22)
[ Uzak Pivot Sunucusu (Yetkisiz Kullanıcı + Python) ]
  └── Gelen akışları iç ağdaki hedef IP'lere yeniden oluşturup yollar
              │
              ▼
[ Hedef İç Ağ (10.10.10.0/24) ]
```

---

## ⚖️ Araç Karşılaştırması: sshuttle vs SOCKS vs Ligolo-ng vs OpenVPN

| Özellik | `sshuttle` | SOCKS (`ssh -D`) | `Ligolo-ng` | `OpenVPN` |
|---------|------------|------------------|-------------|-----------|
| **Uzakta Root Yetkisi** | ❌ Gerekmez | ❌ Gerekmez | ❌ Gerekmez | ✅ Şart |
| **Yerelde Root Yetkisi** | ✅ Şart (iptables) | ❌ Gerekmez | ✅ Şart (TUN arayüzü) | ✅ Şart |
| **Şeffaf Yönlendirme** | ✅ Evet | ❌ Proxychains Şart | ✅ Evet (Doğal TUN) | ✅ Evet |
| **UDP Desteği** | ❌ Yalnızca DNS | ❌ Yok | ✅ Var | ✅ Tam UDP |
| **Hedefte Python Şartı**| ✅ Evet (Python 3) | ❌ Hayır | ❌ Hayır (Ajan binary)| ❌ Hayır |
| **Nmap SYN Taraması (`-sS`)**| ❌ TCP Bağlantı (`-sT`)| ❌ TCP Bağlantı | ✅ Ham paket desteği | ✅ Tam Ham Paket |

---

## 📥 Kurulum

```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y sshuttle

# Arch Linux
sudo pacman -S sshuttle

# macOS (Homebrew)
brew install sshuttle

# Python Pipx
pipx install sshuttle
```

---

## 🚀 Temel Alt Ağ (Subnet) Yönlendirme

Hedef iç C sınıfı alt ağı pivot sunucu üzerinden tünelleme:

```bash
# 10.10.10.0/24 ağını user@192.168.1.50 üzerinden tünelleme
sshuttle -r user@192.168.1.50 10.10.10.0/24

# Birden fazla alt ağı aynı anda yönlendirme
sshuttle -r user@192.168.1.50 10.10.10.0/24 172.16.0.0/16 192.168.100.0/24
```

Bağlantı kurulduktan sonra proxychains kullanmadan doğrudan iç ağ makinelerine erişebilirsiniz:
```bash
# Doğrudan tarayıcı veya curl ile iç web sayfasına istek atma
curl http://10.10.10.25:8080/
smbclient -L //10.10.10.10/ -U 'domain/user%pass'
```

---

## 🌐 DNS Trafiğini Yönlendirme (`--dns`)

İç ağdaki Active Directory domain adlarını (`.corp`, `.local`) iç DNS sunucusundan çözmek için:

```bash
# Alt ağı yönlendir ve DNS sorgularını pivot sunucuya ilet
sshuttle --dns -r user@192.168.1.50 10.10.10.0/24

# İç alan adını doğrudan sorgulama
dig dc01.internal.corp
```

---

## 🌍 Tüm Trafiği Tünelleme (Varsayılan Ağ Geçidi)

Saldırgan makinenin tüm internet trafiğini uzak SSH sunucusuna yönlendirme (Tam VPN):

```bash
# Tüm trafiği uzak sunucuya yönlendirme (0.0.0.0/0)
sshuttle -r user@remote-vps.com 0.0.0.0/0

# Yerel ağı koruyup kalan tüm trafiği yönlendirme
sshuttle -r user@remote-vps.com 0.0.0.0/0 -x 192.168.1.0/24
```

---

## 🚫 Belirli IP/Alt Ağları Hariç Tutma (`-x`)

Pivot sunucunun kendi IP adresine veya kritik sunuculara yapılan bağlantıların döngüye girmesini engellemek için:

```bash
# 10.0.0.0/8 ağını yönlendir, ancak pivot makinenin kendisini hariç tut
sshuttle -r user@10.0.0.50 10.0.0.0/8 -x 10.0.0.50/32
```

---

## 🔑 Özel SSH Parametreleri & Anahtar Kullanımı (`-e`)

Farklı SSH portu veya özel anahtar (identity file) belirtmek için:

```bash
# Özel SSH anahtarı ve 2222 portu ile bağlanma
sshuttle -e 'ssh -i /yol/id_rsa -p 2222' -r user@192.168.1.50 10.10.10.0/24
```

---

## 🤖 Arka Planda Daemon Olarak Çalıştırma (`-D`)

Sızma testi süresince sshuttle'ı arka planda servis gibi çalıştırmak için:

```bash
# Arka plan servisi olarak başlatma
sudo sshuttle -D --pidfile /tmp/sshuttle.pid -r user@192.168.1.50 10.10.10.0/24

# İşlem bittiğinde tüneli kapatma
sudo kill $(cat /tmp/sshuttle.pid)
```

---

## ⚡ Hızlı Referans Tablosu

| İşlem | Komut |
|-------|-------|
| **Tek Bir Alt Ağı Yönlendir** | `sshuttle -r <KULLANICI>@<PIVOT> <ALT_AG>/24` |
| **DNS Sorgularıyla Yönlendir** | `sshuttle --dns -r <KULLANICI>@<PIVOT> <ALT_AG>/24` |
| **Çoklu Alt Ağ Yönlendir** | `sshuttle -r <KULLANICI>@<PIVOT> <AG1>/24 <AG2>/16` |
| **IP/Alt Ağ Hariç Tut** | `sshuttle -r <KULLANICI>@<PIVOT> <ALT_AG>/24 -x <HARIC_IP>` |
| **Tüm İnterneti Tünelle** | `sshuttle -r <KULLANICI>@<PIVOT> 0.0.0.0/0` |
| **Özel Anahtar / Port ile** | `sshuttle -e 'ssh -i <KEY> -p <PORT>' -r <USER>@<PIVOT> <AG>` |
| **Arka Planda Çalıştır** | `sshuttle -D --pidfile /tmp/sshuttle.pid -r <USER>@<PIVOT> <AG>` |

---

## 🛡️ Savunma Açısından Değerlendirme & OPSEC

1. **Güvenlik Duvarı Kuralları:** sshuttle yerel `iptables` kurallarını geçici olarak modifiye eder. Program beklenmedik şekilde sonlanırsa kuralları sıfırlamak için `iptables -F` çalıştırmanız gerekebilir.
2. **Yalnızca TCP Sınırlaması:** DNS dışındaki UDP paketleri ve ICMP (ping) desteklenmez. Nmap ile tarama yaparken daima `nmap -sT -Pn -n` parametreleri kullanılmalıdır.
3. **Hedefte Bırakılan İz:** sshuttle bağlantı sırasında pivot makineye küçük bir Python betiği aktarır ve bunu `python3` ile arka planda çalıştırır.

---

## ⚠️ Yasal Uyarı

> [!CAUTION]
> Bu kılavuz yalnızca izinli sızma testleri, sistem yönetimi ve güvenlik araştırmaları amacıyla hazırlanmıştır. Yetkisiz ağ tünellemesi ve veri yönlendirmesi yasal suçtur.