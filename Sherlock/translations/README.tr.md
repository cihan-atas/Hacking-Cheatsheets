# Sherlock Bilgi Notu (Cheatsheet)

[Sherlock](https://github.com/sherlock-project/sherlock), açık kaynak istihbaratı (OSINT) araştırmalarında hedef kullanıcı adını (username) 400'den fazla sosyal medya platformunda, forumda, kod deposunda ve çevrim içi toplulukta eşzamanlı olarak tarayan popüler bir kullanıcı profili avlama aracıdır.

---

## İçindekiler

- [Temel Yetenekler](#temel-yetenekler)
- [Kurulum](#kurulum)
- [Tekil ve Çoklu Kullanıcı Adı Taraması](#tekil-ve-çoklu-kullanıcı-adı-taraması)
- [Platform ve Site Filtreleme](#platform-ve-site-filtreleme)
- [Proxy & Tor Ağı Üzerinden Yönlendirme](#proxy--tor-ağı-üzerinden-yönlendirme)
- [Zaman Aşımı ve Hız Yapılandırması](#zaman-aşımı-ve-hız-yapılandırması)
- [Dışa Aktarma Biçimleri (CSV, XLSX, TXT)](#dışa-aktarma-biçimleri-csv-xlsx-txt)
- [Otomasyon & OSINT Boru Hattı Entegrasyonu](#otomasyon--osint-boru-hattı-entegrasyonu)

---

## Temel Yetenekler

- **400+ Platform Desteği:** GitHub, Twitter/X, Instagram, Reddit, TikTok, LinkedIn, Telegram, Discord, Steam, Spotify, Docker Hub, Pastebin ve yüzlerce diğer servis.
- **Asenkron Hız:** Python'un asenkron kütüphaneleriyle yüzlerce siteye paralel istek göndererek saniyeler içinde sonuç üretir.
- **Düşük Yanlış Pozitif:** HTTP durum kodları, hata mesajı gövdeleri ve yönlendirmeleri denetler.
- **Tor & SOCKS5 Entegrasyonu:** Araştırmacının gerçek IP adresini gizler ve hız limitlerini (rate-limit) aşar.

---

## Kurulum

```bash
# Pipx ile izole kurulum (Önerilen)
pipx install sherlock-project

# Kaynak koddan kurulum
git clone https://github.com/sherlock-project/sherlock.git
cd sherlock
python3 -m pip install -r requirements.txt

# Debian / Kali Linux depolarından kurulum
sudo apt-get update && sudo apt-get install -y sherlock

# Sürümü kontrol edin
sherlock --version
```

---

## Tekil ve Çoklu Kullanıcı Adı Taraması

### 1. Temel Kullanıcı Adı Araması
Hedef takma adı 400+ platformda arayın:
```bash
sherlock hedef_kullanici
```

### 2. Birden Fazla Kullanıcı Adını Eşzamanlı Tarama
```bash
sherlock hedef_kullanici1 hedef_kullanici2 dev_alias
```

### 3. Yalnızca Bulunan Hesapları Ekrana Basma
Bulunamayan veya hata veren siteleri gizleyip sadece doğrulanmış profilleri listeleyin:
```bash
sherlock --print-found hedef_kullanici
```

---

## Platform ve Site Filtreleme

Gereksiz istekleri eleyip doğrudan ilgilenilen servislere odaklanın:

### 1. Yalnızca Belirli Siteleri Taramak
```bash
sherlock --site github --site twitter --site reddit --site gitlab hedef_kullanici
```

### 2. Liste Dosyasıyla Belirli Siteleri Hedefleme
```bash
sherlock hedef_kullanici --site-list siteler.txt
```

---

## Proxy & Tor Ağı Üzerinden Yönlendirme

IP adresinin engellenmesini önlemek ve gizlilik sağlamak için:

### 1. Tor Ağı Üzerinden Otomatik Yönlendirme
Yerel Tor servisini (`127.0.0.1:9050`) kullanarak tüm istekleri anonimleştirin:
```bash
sherlock --tor hedef_kullanici
```

### 2. Özel HTTP / SOCKS5 Proxy Kullanımı
```bash
# SOCKS5 proxy
sherlock --proxy socks5://127.0.0.1:1080 hedef_kullanici

# Kimlik doğrulamalı HTTP proxy
sherlock --proxy http://kullanici:sifre@proxy.kurum.yerel:8080 hedef_kullanici
```

---

## Zaman Aşımı ve Hız Yapılandırması

İstek gecikmelerini ve hız limitlerini yönetin:

```bash
# Yanıt beklemeyi 5 saniyeye düşürün (varsayılan genelde 60 saniyedir)
sherlock hedef_kullanici --timeout 5

# İstekler arasına 1 saniye bekleme süresi koyun
sherlock hedef_kullanici --delay 1
```

---

## Dışa Aktarma Biçimleri (CSV, XLSX, TXT)

Kanıt dosyası veya analiz raporu oluşturmak için:

### 1. CSV Formatında Kaydetme
```bash
sherlock --csv --output /tmp/sherlock_sonuclari hedef_kullanici
```

### 2. Çıktı Klasörü Tanımlama
```bash
sherlock --folderoutput /tmp/osint_verileri/ hedef_kullanici
```
Komut `/tmp/osint_verileri/hedef_kullanici.txt` dosyasını oluşturur.

### 3. Excel (XLSX) Formatında Dışa Aktarma
```bash
sherlock --xlsx hedef_kullanici
```

---

## Otomasyon & OSINT Boru Hattı Entegrasyonu

Sherlock'u otomatik bir keşif betiğine bağlayın:

```bash
#!/usr/bin/env bash
HEDEF="$1"
CIKTI_DIZINI="/tmp/osint_${HEDEF}"
mkdir -p "$CIKTI_DIZINI"

echo "[*] Kullanıcı adı taranıyor: $HEDEF"
sherlock --print-found --timeout 10 --folderoutput "$CIKTI_DIZINI" "$HEDEF"

if [ -f "$CIKTI_DIZINI/${HEDEF}.txt" ]; then
    echo "[+] Bulunan aktif hesap sayısı: $(wc -l < "$CIKTI_DIZINI/${HEDEF}.txt")"
    cat "$CIKTI_DIZINI/${HEDEF}.txt"
fi
```
