# 🦈 tshark - Kapsamlı Başvuru Kılavuzu

[🇺🇸 English Version](../README.md)

```
  ████████╗███████╗██╗  ██╗ █████╗ ██████╗ ██╗  ██╗
  ╚══██╔══╝██╔════╝██║  ██║██╔══██╗██╔══██╗██║ ██╔╝
     ██║   ███████╗███████║███████║██████╔╝█████╔╝ 
     ██║   ╚════██║██╔══██║██╔══██║██╔══██╗██╔═██╗ 
     ██║   ███████║██║  ██║██║  ██║██║  ██║██║  ██╗
     ╚═╝   ╚══════╝╚═╝  ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝
         Terminal Paket Analizi ve Ayrıştırma Motoru
```

<p align="center">
  <img src="https://img.shields.io/badge/tshark-Paket_Analizcisi-blue?style=for-the-badge" alt="tshark">
  <img src="https://img.shields.io/badge/CLI-Ag_Dinleme-red?style=for-the-badge" alt="CLI Ağ Dinleme">
  <img src="https://img.shields.io/badge/Wireshark-Motoru-green?style=for-the-badge" alt="Wireshark Motoru">
  <img src="https://img.shields.io/badge/PCAP-Ayrıştırma-orange?style=for-the-badge" alt="PCAP Ayrıştırma">
</p>

<p align="center">
  <b>🌐 Wireshark motoru ile terminal üzerinden derinlemesine ağ paketi analizi, akış takibi ve alan ayıklama rehberi</b>
</p>

---

## 📋 İçindekiler

- [Giriş](#-giriş)
- [Araç Karşılaştırması: tshark vs tcpdump vs Wireshark](#-araç-karşılaştırması)
- [Kurulum ve Yetkilendirme](#-kurulum-ve-yetkilendirme)
- [Temel Canlı Yakalama ve PCAP Okuma](#-temel-canlı-yakalama-ve-pcap-okuma)
- [Trafik Filtreleme: Yakalama (Capture) vs Görüntüleme (Display)](#-trafik-filtreleme)
- [Alan Ayıklama (`-T fields`)](#-alan-ayıklama--t-fields)
- [Pratik Pentest ve Adli Bilişim Senaryoları](#-pratik-pentest-ve-adli-bilişim-senaryoları)
  - [HTTP Kimlik Bilgisi ve URI Avı](#http-kimlik-bilgisi-ve-uri-avı)
  - [DNS Sorgusu Çıkarma ve Tünelleme Tespiti](#dns-sorgusu-çıkarma-ve-tünelleme-tespiti)
  - [TLS / HTTPS SNI (Server Name) Çıkarma](#tls--https-sni-server-name-çıkarma)
  - [Kerberos AS-REQ / AS-REP ve Bilet Çıkarma](#kerberos-as-req--as-rep-ve-bilet-çıkarma)
  - [SMB ve NTLM Kimlik Doğrulama Takibi](#smb-ve-ntlm-kimlik-doğrulama-takibi)
  - [Açık Metin Protokol Denetimi (FTP, Telnet, SMTP)](#açık-metin-protokol-denetimi)
  - [WPA / EAPOL 4-Way Handshake Yakalama](#wpa--eapol-4-way-handshake-yakalama)
- [Akış Takibi ve Oturumu Yeniden Birleştirme](#-akış-takibi-ve-oturumu-yeniden-birleştirme)
- [İstatistikler, Konuşmalar ve Uç Noktalar](#-i̇statistikler-konuşmalar-ve-uç-noktalar)
- [TLS / SSL Şifreli Trafiği Çözme](#-tls--ssl-şifreli-trafiği-çözme)
- [Dışa Aktarma Formatları (JSON, CSV, Elasticsearch)](#-dışa-aktarma-formatları)
- [Hızlı Başvuru Tablosu](#-hızlı-başvuru-tablosu)
- [İpuçları ve En İyi Pratikler](#-i̇puçları-ve-en-i̇yi-pratikler)
- [Yasal Uyarı](#-yasal-uyarı)

---

## 🎯 Giriş

**tshark**, popüler ağ analiz aracı **Wireshark**'ın komut satırı (CLI / Terminal) sürümüdür. Wireshark ile birebir aynı paket yakalama kütüphanesini (libpcap/Npcap) ve protokol çözümleme motorunu (dissector) kullanır. Bu sayede grafik arayüze (GUI) ihtiyaç duymadan, uzak sunucularda, SSH oturumlarında ve otomasyon betiklerinde benzersiz bir analiz gücü sunar.

Yalnızca temel IP/port başlıklarını gösteren basit sniffer araçlarının aksine `tshark`, binlerce uygulama protokolünü en derin katmanına kadar inceler, zengin Wireshark görüntüleme filtresi (`-Y`) sözdizimini çalıştırır ve hedeflenen verileri (`-T fields`) anında ayıklayıp `jq`, Python veya bash hatlarına aktarır.

### Temel Özellikler

| Özellik | Açıklama |
|---------|----------|
| **Derinlemesine Ayrıştırma** | Binlerce ağ ve uygulama protokolünün içeriğini tam olarak çözer |
| **Wireshark Filtreleri** | Wireshark'ın tüm gelişmiş görüntüleme filtresi kurallarını (`-Y`) destekler |
| **Özel Alan Ayıklama** | Protokol içerisindeki herhangi bir alanı doğrudan çeker (`-T fields -e ...`) |
| **Oturum / Akış Takibi** | TCP ve UDP veri akışlarını ASCII, Hex veya ham biçimde birleştirir |
| **Betik & CI/CD Uyumu** | `-T json` desteğiyle `jq`, Python ve bash betiklerine doğrudan veri sağlar |
| **Şifre Çözme (Decryption)**| `SSLKEYLOGFILE` dosyasıyla canlı TLS/HTTPS trafiğini şifresiz hale getirir |

---

## ⚖️ Araç Karşılaştırması

| Özellik | tshark | tcpdump | Wireshark |
|---------|--------|---------|-----------|
| **Arayüz** | CLI (Terminal) | CLI (Terminal) | GUI (Grafik) |
| **Kaynak Tüketimi** | Düşük / Orta | Son derece düşük | Daha yüksek (RAM & CPU) |
| **Uzak / Sunucu (SSH)** | Doğrudan SSH / Başsız (Headless) | Doğrudan SSH hattı | X11/VNC veya uzak ajan gerektirir |
| **Ayrıştırma (Dissection)**| Wireshark motorunun tamamı | Temel başlıklar | Wireshark motorunun tamamı |
| **Yakalama Filtresi** | Yalnızca BPF (`-f`) | Yalnızca BPF (`-i`) | Yalnızca BPF |
| **Görüntüleme Filtresi** | ✅ Tam Wireshark sözdizimi (`-Y`) | ❌ Yok | ✅ Görsel filtre çubuğu |
| **Çıktı Formatları** | PCAP, JSON, CSV, Elastic, XML | PCAP, ASCII, Hex | PCAP, CSV, XML, JSON |
| **Betik & Otomasyon** | Mükemmel (Shell, Python, jq) | Çok Kolay (Shell, awk) | Karmaşık (Lua eklentileri) |

---

## 📥 Kurulum ve Yetkilendirme

### Linux (Debian / Ubuntu / Kali Linux)

```bash
# tshark paketini kurun
sudo apt update && sudo apt install -y tshark

# Kurulumu ve sürümü doğrulayın
tshark --version
```

### Root Yetkisi Olmadan Paket Yakalama Ayarı

`tshark`'ı doğrudan `root` kullanıcısıyla çalıştırmak geniş dissector alanı nedeniyle güvenlik riski oluşturabilir. Kullanıcınızı `wireshark` grubuna ekleyerek yetkilendirebilirsiniz:

```bash
# wireshark-common paketini yeniden yapılandırın
sudo dpkg-reconfigure wireshark-common
# (Gelen ekranda "Yes" / "Evet" seçeneğini belirleyin)

# Kullanıcınızı wireshark grubuna ekleyin
sudo usermod -aG wireshark $USER

# Grup ayarlarını aktif oturuma uygulayın
newgrp wireshark
```

### macOS ve Windows Kurulumu

```bash
# macOS (Homebrew ile)
brew install wireshark

# Windows (Chocolatey ile)
choco install wireshark
```

---

## 🚀 Temel Canlı Yakalama ve PCAP Okuma

### Ağ Arayüzlerini Listeleme

```bash
# Mevcut tüm ağ kartlarını numaralarıyla listeler
tshark -D
```

### Canlı Trafik Dinleme

```bash
# eth0 arayüzündeki tüm paketleri canlı terminale yazdırır
tshark -i eth0

# Yalnızca 100 paket yakalayıp otomatik durdurur
tshark -i eth0 -c 100

# Trafiği doğrudan bir PCAP dosyasına kaydeder
tshark -i eth0 -w yakalama.pcap

# Ring buffer (Dairesel tampon): Her 100MB'ta yeni dosya açar, en fazla 5 dosya saklar
tshark -i eth0 -b filesize:100000 -b files:5 -w trafik_rotasyon.pcap

# Süre sınırlı yakalama: 60 saniye dinleyip otomatik durur
tshark -i eth0 -a duration:60 -w bir_dakika.pcap
```

### PCAP Dosyalarını İnceleme

```bash
# Kaydedilmiş pcap dosyasını terminalde okur
tshark -r yakalama.pcap

# IP adreslerini DNS çözümlemesi yapmadan gösterir (Çok daha hızlıdır)
tshark -r yakalama.pcap -n

# Paket özetlerini gizler (Yalnızca istatistik veya özel çıktılar için)
tshark -r yakalama.pcap -q
```

---

## 🔍 Trafik Filtreleme: Yakalama (Capture) vs Görüntüleme (Display)

`tshark` iki farklı filtreleme motoru kullanır:

1. **Yakalama Filtresi (`-f`):** Berkeley Packet Filter (BPF) motorudur. Paketler çekirdekten (kernel) kullanıcı alanına geçmeden önce filtrelenir; çok hızlıdır ancak derin uygulama katmanını tanımaz.
2. **Görüntüleme Filtresi (`-Y`):** Wireshark'ın gelişmiş filtreleme motorudur. Tüm protokol katmanlarını, dizgileri ve zengin boolean mantığını destekler.

```bash
# Yakalama filtresi: Yalnızca 80 veya 443 portundaki TCP paketlerini yakala
tshark -i eth0 -f "tcp port 80 or tcp port 443" -w web_trafigi.pcap

# Görüntüleme filtresi: Yalnızca HTTP GET isteklerine dönen 200 OK yanıtlarını göster
tshark -r yakalama.pcap -Y "http.request.method == \"GET\" and http.response.code == 200"

# Görüntüleme filtresi: Belirli bir alt ağı filtrele ve broadcast trafiğini hariç tut
tshark -r yakalama.pcap -Y "ip.addr == 192.168.1.0/24 and not eth.dst == ff:ff:ff:ff:ff:ff"

# İki aşamalı filtreleme (-R):
tshark -r yakalama.pcap -2 -R "dns.flags.response == 1"
```

---

## 📊 Alan Ayıklama (`-T fields`)

`-T fields` modu, sızma testlerinde, otomasyonda ve CTF yarışmalarında belirli protokol verilerini metin olarak çekmenin en verimli yoludur.

```bash
# Temel alan ayıklama sözdizimi
tshark -r yakalama.pcap -T fields -e <alan1> -e <alan2> -E header=y -E separator=,
```

### Önemli Biçimlendirme Parametreleri

| Parametre | Açıklama |
|-----------|----------|
| `-T fields` | Çıktıyı özel alan ayıklama moduna alır |
| `-e <alan>` | Ayıklanacak protokol alan adını belirtir (birden çok kez kullanılabilir) |
| `-E header=y` | Çıktının ilk satırına sütun başlıklarını yazar |
| `-E separator=,` | Sütunlar arasına virgül koyar (CSV formatı) |
| `-E occurrence=f` | Çoklu değerlerde yalnızca ilkini (`f`), sonuncusunu (`l`) veya hepsini (`a`) alır |
| `-E quote=d` | Değerleri çift tırnak (`"`) içine alır |

### Sık Kullanılan Protokol Alanları

```bash
# IP katmanı alanları
ip.src, ip.dst, ip.proto, ip.ttl

# TCP katmanı alanları
tcp.srcport, tcp.dstport, tcp.flags, tcp.stream, tcp.payload

# HTTP alanları
http.host, http.request.method, http.request.uri, http.response.code, http.user_agent, http.cookie

# DNS alanları
dns.qry.name, dns.qry.type, dns.a, dns.flags.response

# TLS alanları
tls.handshake.extensions_server_name, tls.handshake.ciphersuite
```

---

## 🎯 Pratik Pentest ve Adli Bilişim Senaryoları

### HTTP Kimlik Bilgisi ve URI Avı

```bash
# Ziyaret edilen tüm HTTP Host ve URI adreslerini listeler
tshark -r yakalama.pcap -Y "http.request" -T fields -e ip.src -e http.host -e http.request.method -e http.request.uri -E header=y

# Ağdaki cihazların User-Agent tarayıcı bilgilerini çıkarır
tshark -r yakalama.pcap -Y "http.user_agent" -T fields -e ip.src -e http.user_agent | sort -u

# HTTP POST ile gönderilen form verilerini (kullanıcı adı, şifre, token) yakalar
tshark -r yakalama.pcap -Y "http.request.method == \"POST\"" -T fields -e ip.src -e ip.dst -e http.host -e http.file_data

# HTTP Basic Authentication başlıklarını (Base64 şifreleri) yakalar
tshark -r yakalama.pcap -Y "http.authorization" -T fields -e ip.src -e http.authorization
```

### DNS Sorgusu Çıkarma ve Tünelleme Tespiti

```bash
# Ağdan yapılan tüm DNS sorgularını (istenen alan adlarını) döker
tshark -r yakalama.pcap -Y "dns.flags.response == 0" -T fields -e ip.src -e dns.qry.name | sort -u

# Olası DNS Tünelleme (DNS Tunneling) trafiğini tespit eder (30 karakterden uzun sorgular)
tshark -r yakalama.pcap -Y "dns.flags.response == 0" -T fields -e dns.qry.name | awk 'length($1) > 30' | sort -u

# DNS sorguları ile dönen IP yanıtlarını eşleştirir
tshark -r yakalama.pcap -Y "dns.flags.response == 1 and dns.a" -T fields -e dns.qry.name -e dns.a -E header=y
```

### TLS / HTTPS SNI (Server Name) Çıkarma

HTTPS trafiği şifreli olsa bile, ilk el sıkışma (Client Hello) sırasında sunucu adı **Server Name Indication (SNI)** alanında açık metin olarak gider:

```bash
# Şifreli HTTPS trafiğinde ziyaret edilen alan adlarını SNI ile listeler
tshark -r yakalama.pcap -Y "tls.handshake.type == 1" -T fields -e ip.src -e ip.dst -e tls.handshake.extensions_server_name | sort -u
```

### Kerberos AS-REQ / AS-REP ve Bilet Çıkarma

```bash
# Active Directory etki alanında oturum açan kullanıcı hesaplarını tespit eder
tshark -r yakalama.pcap -Y "kerberos.CNameString" -T fields -e ip.src -e kerberos.realm -e kerberos.CNameString | sort -u

# Kerberos hata kodlarını yakalar (Hatalı parola, pre-auth gereksinimi vb.)
tshark -r yakalama.pcap -Y "kerberos.error_code" -T fields -e ip.src -e ip.dst -e kerberos.error_code
```

### SMB ve NTLM Kimlik Doğrulama Takibi

```bash
# Ağ paylaşımlarında erişilen SMB dosya adlarını ve yollarını listeler
tshark -r yakalama.pcap -Y "smb2.filename" -T fields -e ip.src -e ip.dst -e smb2.filename | sort -u

# NTLMSSP üzerinden doğrulanan kullanıcı ve domain adlarını çıkarır
tshark -r yakalama.pcap -Y "ntlmssp.auth.username" -T fields -e ip.src -e ntlmssp.auth.domain -e ntlmssp.auth.username
```

### Açık Metin Protokol Denetimi

```bash
# FTP kullanıcı adı ve parolalarını açık metin olarak yakalar
tshark -r yakalama.pcap -Y "ftp.request.command == \"USER\" or ftp.request.command == \"PASS\"" -T fields -e ip.src -e ftp.request.command -e ftp.request.arg

# Telnet oturumunda yazılan komutları listeler
tshark -r yakalama.pcap -Y "telnet" -T fields -e telnet.data
```

### WPA / EAPOL 4-Way Handshake Yakalama

```bash
# WiFi pcap dosyasından kırma işlemi için EAPOL handshake paketlerini süzer
tshark -r wifi.pcap -Y "eapol" -w handshakes_temiz.pcap
```

---

## 🧵 Akış Takibi ve Oturumu Yeniden Birleştirme

Grafik arayüz olmadan TCP veya UDP oturumlarının tüm içeriğini birleştirip okuyabilirsiniz:

```bash
# PCAP içindeki TCP oturumlarını (Stream ID) listeler
tshark -r yakalama.pcap -q -z conv,tcp

# 0 numaralı TCP akışını ASCII olarak ekrana basar
tshark -r yakalama.pcap -q -z follow,tcp,ascii,0

# 2 numaralı TCP akışını Hex formatında görüntüler
tshark -r yakalama.pcap -q -z follow,tcp,hex,2

# 1 numaralı HTTP isteğini ve yanıtını eksiksiz görüntüler
tshark -r yakalama.pcap -q -z follow,http,ascii,1
```

---

## 📈 İstatistikler, Konuşmalar ve Uç Noktalar

```bash
# Protokol hiyerarşisi istatistiği (Trafikte hangi protokoller yüzde kaç yer kaplıyor)
tshark -r yakalama.pcap -q -z io,phs

# IP konuşmaları tablosu (Kim kiminle ne kadar veri alışverişi yapmış)
tshark -r yakalama.pcap -q -z conv,ip

# TCP port bazında trafik konuşmaları
tshark -r yakalama.pcap -q -z conv,tcp

# PCAP içindeki tüm benzersiz IP uç noktaları (Endpoints)
tshark -r yakalama.pcap -q -z endpoints,ip

# HTTP istek ağacı ve durum kodları dağılımı
tshark -r yakalama.pcap -q -z http,tree

# Wireshark Expert Info (Ağ anomalileri, hataları ve uyarıları)
tshark -r yakalama.pcap -q -z expert
```

---

## 🔐 TLS / SSL Şifreli Trafiği Çözme

Tarayıcıdan kaydedilen `SSLKEYLOGFILE` anahtar dosyası veya bellekten dökülen pre-master secret anahtarları mevcutsa:

```bash
# Anahtar dosyasıyla TLS şifresini canlı çözüp HTTP isteklerini gösterir
tshark -r https_trafigi.pcap -o "tls.keylog_file:/yol/keys.log" -Y "http" -T fields -e http.host -e http.request.uri

# Şifresi çözülen HTTPS oturumunu metin olarak baştan sona birleştirir
tshark -r https_trafigi.pcap -o "tls.keylog_file:/yol/keys.log" -q -z follow,http,ascii,0
```

---

## 📦 Dışa Aktarma Formatları

```bash
# Çıktıyı yapılandırılmış JSON formatında verir (jq ve Python ile işlemek için ideal)
tshark -r yakalama.pcap -c 5 -T json

# jq ile JSON içindeki kaynak IP alanını ayıklama
tshark -r yakalama.pcap -T json | jq '.[]._source.layers.ip["ip.src"]'

# Elasticsearch Bulk formatında dışa aktarma (-T ek)
tshark -r yakalama.pcap -T ek > elastic_icin_paketler.json

# Tablo biçiminde CSV dosyası oluşturma
tshark -r yakalama.pcap -T fields -e frame.number -e ip.src -e ip.dst -e _ws.col.Protocol -E header=y -E separator=, > trafik.csv
```

---

## ⚡ Hızlı Başvuru Tablosu

| İşlem | Komut |
|-------|-------|
| **Canlı trafik dinleme** | `tshark -i eth0 -w cikti.pcap` |
| **BPF yakalama filtresi** | `tshark -i eth0 -f "port 80" -w http.pcap` |
| **DNS çözmeden hızlı okuma** | `tshark -r yakalama.pcap -n` |
| **Görüntüleme filtresi uygulama**| `tshark -r yakalama.pcap -Y "http.response.code >= 400"` |
| **IP ve Portları ayıklama** | `tshark -r yakalama.pcap -T fields -e ip.src -e tcp.srcport -e ip.dst -e tcp.dstport` |
| **DNS sorgularını dökme** | `tshark -r yakalama.pcap -Y "dns.flags.response == 0" -T fields -e dns.qry.name` |
| **TLS SNI alan adlarını dökme**| `tshark -r yakalama.pcap -Y "tls.handshake.type == 1" -T fields -e tls.handshake.extensions_server_name` |
| **HTTP URL adreslerini çekme** | `tshark -r yakalama.pcap -Y "http.request" -T fields -e http.host -e http.request.uri` |
| **TCP oturumunu birleştirme** | `tshark -r yakalama.pcap -q -z follow,tcp,ascii,0` |
| **Protokol hiyerarşisi** | `tshark -r yakalama.pcap -q -z io,phs` |
| **TLS anahtarıyla şifre çözme**| `tshark -r trafik.pcap -o "tls.keylog_file:keys.log"` |
| **JSON formatında dışa aktarma**| `tshark -r yakalama.pcap -T json > paketler.json` |

---

## 💡 İpuçları ve En İyi Pratikler

1. **Büyük Dosyalarda Her Zaman `-n` Kullanın:** Varsayılan olarak tshark her IP için DNS sorgusu yapmaya çalışır. `-n` bayrağını eklemek dosya okuma süresini onlarca kat hızlandırır.
2. **`-f` ve `-Y` Ayrımını Doğru Yapın:** Canlı yakalama sırasında paket kaybını önlemek için kernel düzeyinde BPF filtreleri (`-f`), yakalanan dosyayı analiz ederken ise zengin Wireshark filtreleri (`-Y`) tercih edilmelidir.
3. **`jq` ile Güçlü Borulama:** `tshark -T json` çıktısını `jq` ile birleştirmek, karmaşık protokol verilerinde SQL benzeri sorgulamalar yapmanıza olanak tanır.
4. **Pivot Makinelerde Ring Buffer:** Pivot host üzerinde uzun süreli trafik dinlerken diski doldurmamak için döngüsel tampon sınırlarını (`-b filesize:50000 -b files:10`) mutlaka tanımlayın.

---

## ⚠️ Yasal Uyarı

> **ÖNEMLİ:** Yazılı izin ve yetkilendirme olmaksızın ağ trafiğini dinlemek, paket yakalamak veya kimlik bilgilerini ele geçirmek yasalara aykırıdır ve cezai sorumluluk doğurur. `tshark` aracını yalnızca yetkili olduğunuz sızma testlerinde ve kendi sistemlerinizde kullanınız.
