# dnsrecon Bilgi Notu (Cheatsheet)

[dnsrecon](https://github.com/darkoperator/dnsrecon), Python ile geliştirilmiş kapsamlı bir DNS keşif ve numaralandırma (enumeration) aracıdır. Alan adı bölge transferi (Zone Transfer - AXFR), ters DNS (Reverse PTR), kaba kuvvet (brute-force) alt alan adı tespiti, SRV kayıtları analizi ve DNS önbellek gözetleme (cache snooping) yeteneklerine sahiptir.

---

## İçindekiler

- [Temel Yetenekler](#temel-yetenekler)
- [Kurulum](#kurulum)
- [Standart Keşif & Bölge Transferi (`-d -t std / axfr`)](#standart-keşif--bölge-transferi--d--t-std--axfr)
- [Alt Alan Adı Kaba Kuvvet Taraması (`-t brt`)](#alt-alan-adı-kaba-kuvvet-taraması--t-brt)
- [Ters DNS Aralık Taraması (`-t rvl`)](#ters-dns-aralık-taraması--t-rvl)
- [DNS Önbellek Gözetleme (`-t snoop`)](#dns-önbellek-gözetleme--t-snoop)
- [Özel DNS Sunucusu Belirtme (`-n`)](#özel-dns-sunucusu-belirtme--n)
- [Çıktı Formatları (JSON, SQLite, CSV)](#çıktı-formatları-json-sqlite-csv)
- [DNS Sunucu Güvenliği & Sertleştirme](#dns-sunucu-güvenliği--sertleştirme)

---

## Temel Yetenekler

- **Bölge Transferi Tespiti (AXFR):** Yetkili tüm DNS sunucularında yetkisiz bölge transferi açığını otomatik dener.
- **Kapsamlı Kayıt Analizi:** SOA, NS, A, AAAA, MX, TXT, SPF ve SRV kayıtlarını toplar.
- **Alt Alan Adı Brute-Force:** Joker karakter (wildcard) filtrelemeli çok iş parçacıklı kelime listesi taraması.
- **Ters PTR Taraması:** Verilen IP bloklarında aktif barındırılan sunucuların adlarını çeker.
- **Önbellek Gözetleme (Cache Snooping):** Hedef DNS önbelleğinde belirli alan adlarının sorgulanıp sorgulanmadığını inceler.
- **TLD Genişletme:** Farklı ülke uzantılarındaki eşleşen alan adlarını tespit eder.

---

## Kurulum

```bash
# Debian / Ubuntu / Kali Linux
sudo apt-get update && sudo apt-get install -y dnsrecon

# Python pip / pipx ile kurulum
pipx install dnsrecon

# Kaynak koddan kurulum
git clone https://github.com/darkoperator/dnsrecon.git
cd dnsrecon
pip install -r requirements.txt

# Yardım menüsünü görüntüleyin
dnsrecon -h
```

---

## Standart Keşif & Bölge Transferi (`-d -t std / axfr`)

### 1. Standart DNS Kayıt Taraması
SOA, NS, A, MX ve TXT kayıtlarını toplar ve tespit ettiği yetkili ad sunucularında bölge transferi açığını dener:
```bash
dnsrecon -d hedef.com -t std
```

### 2. Yalnızca Bölge Transferi (AXFR) Testi
Hedef alan adının tüm DNS veritabanını tek seferde çekmeyi dener:
```bash
dnsrecon -d hedef.com -t axfr
```
*Eğer sunucu hatalı yapılandırılmışsa, hedef kurumun tüm iç ve dış DNS kayıtları saniyeler içinde dökülür.*

---

## Alt Alan Adı Kaba Kuvvet Taraması (`-t brt`)

Özel sözlük dosyalarıyla gizli alt alan adlarını bulun:

### 1. Sözlük ile Alt Alan Adı Taraması
```bash
dnsrecon -d hedef.com -t brt -D /usr/share/wordlists/dnsmap.txt
```
- `-d <domain>`: Hedef alan adı.
- `-t brt`: Kaba kuvvet (brute-force) modu.
- `-D <dosya>`: Kullanılacak alt alan adı listesi.

### 2. Joker Karakter (Wildcard) Filtrelemeli Tarama
Tüm alt alan adlarının tek bir IP'ye yönlendirildiği durumlarda yanlış sonuçları engeller:
```bash
dnsrecon -d hedef.com -t brt -D /usr/share/seclists/Discovery/DNS/subdomains-top1million-5000.txt --iw
```
- `--iw`: Joker DNS eşleşmelerini yok say.

---

## Ters DNS Aralık Taraması (`-t rvl`)

Belirli bir IP bloğundaki PTR kayıtlarını çözerek gizli sunucuları belirleyin:

```bash
dnsrecon -r 192.168.1.0/24 -t rvl
```
- `-r <aralık>`: CIDR notasyonu (`192.168.1.0/24`) veya başlangıç-bitiş (`192.168.1.1-192.168.1.254`).
- `-t rvl`: Ters DNS arama kipi.

---

## DNS Önbellek Gözetleme (`-t snoop`)

Hedef kurum çalışanlarının belirli kritik siteleri (örn. rakip şirketler, sızıntı portalları, C2 sunucuları) ziyaret edip etmediğini iç DNS önbelleğinden sorgulayın:

```bash
dnsrecon -t snoop -n 192.168.1.1 -D kritik_siteler.txt
```
- `-n <dns_ip>`: Sorgulanacak kurum içi DNS sunucusu.
- `-D <liste>`: Önbellekte aranacak alan adları.

---

## Özel DNS Sunucusu Belirtme (`-n`)

Genel DNS sunucuları yerine kurum içi veya hedef özel DNS sunucusunu doğrudan sorgulayın:

```bash
dnsrecon -d sirket.yerel -n 10.10.10.2 -t std
```

---

## Çıktı Formatları (JSON, SQLite, CSV)

Elde edilen DNS verilerini yapılandırılmış formatlarda kaydedin:

### 1. JSON Olarak Dışa Aktarma
```bash
dnsrecon -d hedef.com -t std --json /tmp/dns_rapor.json
```

### 2. SQLite Veritabanı Olarak Kaydetme
```bash
dnsrecon -d hedef.com -t std --db /tmp/dns_veritabani.sqlite
```

### 3. CSV ve XML Çıktısı
```bash
dnsrecon -d hedef.com -t std --csv /tmp/dns_sonuc.csv --xml /tmp/dns_sonuc.xml
```

---

## DNS Sunucu Güvenliği & Sertleştirme

| Zafiyet | Tehdit & Risk | Savunma ve Sertleştirme Önlemi |
|---|---|---|
| **Açık AXFR Transferi** | Tüm iç ağ topolojisinin ifşası | Bölge transferini yalnızca yetkili ikincil DNS IP adreslerine kısıtlayın (`allow-transfer { 10.0.0.2; };`). |
| **Açık Yinelemeli (Recursive) DNS** | DNS Amplification DDoS ve Snooping | Dış ağlara kapalı tutun (`recursion no;`). |
| **Tahmin Edilebilir İsimler** | Kolay alt alan adı keşfi | Test ve geliştirme ortamlarında rastgele oluşturulmuş karmaşık isimler kullanın. |
| **Dangling CNAME Kayıtları** | Subdomain Takeover saldırısı | İptal edilen bulut kaynaklarına (AWS S3, Azure) işaret eden artık CNAME kayıtlarını derhal silin. |
