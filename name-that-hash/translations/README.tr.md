# Name-That-Hash (nth) Bilgi Notu (Cheatsheet)

[Name-That-Hash](https://github.com/HashPals/Name-That-Hash) (`nth`), eski `hash-identifier` aracının modern ve ultra hızlı alternatifidir. Bilinmeyen parola özetlerini (hash), metin veya log dosyaları içerisine gömülü hash dizgilerini tespit eder ve doğrudan Hashcat (`-m`) ile John the Ripper (`--format`) kırma parametrelerini üretir.

---

## İçindekiler

- [Öne Çıkan Özellikler](#öne-çıkan-özellikler)
- [Kurulum](#kurulum)
- [Tekil Hash Analizi](#tekil-hash-analizi)
- [Dosya ve Çoklu Hash Taraması](#dosya-ve-çoklu-hash-taraması)
- [Hashcat & John the Ripper Mod Çıktıları](#hashcat--john-the-ripper-mod-çıktıları)
- [Otomatik Çözme (Base64 / Hex) & Popülerlik Filtresi](#otomatik-çözme-base64--hex--popülerlik-filtresi)
- [Otomasyon ve Kırma Hatlarına Entegrasyon](#otomasyon-ve-kırma-hatlarına-entegrasyon)

---

## Öne Çıkan Özellikler

- **300+ Hash Türü:** Modern algoritmalar, Active Directory yapıları (NTLM, Kerberos), veritabanı hashleri ve CMS imzaları.
- **Hashcat & John Eşleştirmesi:** Tespit anında doğrudan `-m <kod>` ve `--format=<biçim>` değerlerini verir.
- **Otomatik Kod Çözme:** Base64, Hex veya URL encode edilmiş hashleri otomatik temizleyip analiz eder.
- **Gömülü Metin Arama:** Log dosyaları veya bellek dökümleri içindeki hash dizgilerini regex ile ayıklar.
- **Popülerlik Sıralaması:** Çok nadir kullanılan algoritmaları eleyerek en olası gerçek dünya ihtimallerini öne çıkarır.

---

## Kurulum

```bash
# Pipx ile izole CLI kurulumu (Önerilen)
pipx install name-that-hash

# Standart Python pip ile kurulum
python3 -m pip install --user name-that-hash

# Kali Linux / Debian depolarından
sudo apt-get update && sudo apt-get install -y python3-name-that-hash

# Kurulumu doğrulayın
nth --version
```

---

## Tekil Hash Analizi

### 1. Basit Hash Analizi
```bash
nth -t "5d41402abc4b2a76b9719d911017c592"
```

### 2. Sadece Hashcat ve John Desteği Olanları Listeleme
```bash
nth -t "b8c37e33defde51cf91e1e03e51657da" --hashcat --john
```

### 3. Ayrıntılı Bilgi Alma (Açıklama, Hız, Örnekler)
```bash
nth -t "$6$rounds=5000$usesomesalt$..." -v
```

---

## Dosya ve Çoklu Hash Taraması

### 1. Hash Listesi Dosyasını Analiz Etme
```bash
nth -f /path/to/hashes.txt
```

### 2. Sadece En Popüler Hashleri Filtreleme (Gürültüyü Azaltma)
Nadir rastlanan şifreleme biçimlerini gizleyip yaygın olanları (MD5, SHA1, SHA256, NTLM, bcrypt vb.) getirin:
```bash
# Seviye 1: Yalnızca en popüler ve yaygın hashler
nth -f hashes.txt --popularity 1
```

### 3. Düz Metin / Log Dosyası İçindeki Hashleri Ayıklama
```bash
nth --text "Config dosyasında bulunan: 098f6bcd4621d373cade4e832627b4f6 port 8080"
```

---

## Hashcat & John the Ripper Mod Çıktıları

Yaygın hash türlerinin Hashcat ve John parametre karşılıkları:

| Örnek Hash Biçimi | Tespit Edilen Algoritma | Hashcat Modu (`-m`) | John Biçimi (`--format`) |
|---|---|---|---|
| `e80b5017098950fc58aad83c8c14978e` | MD5 | `0` | `raw-md5` |
| `b4b9b02e6f09a9bd760f388b67351e2b` | NTLM | `1000` | `nt` |
| `284ddc0d513372c7d764a0ff813473f2...` | SHA-256 | `1400` | `raw-sha256` |
| `$2a$12$DOW8W6...` | bcrypt | `3200` | `bcrypt` |
| `$6$salt$hash...` | SHA-512 (Unix) | `1800` | `sha512crypt` |
| `$krb5tgs$23$*...` | Kerberoast (TGS-REP) | `13100` | `krb5tgs` |
| `$krb5asrep$23$...` | AS-REP Roasting | `18200` | `krb5asrep` |

---

## Otomatik Çözme (Base64 / Hex) & Popülerlik Filtresi

Veritabanlarında hashler sıklıkla Base64 veya Hex formatında tutulur:

```bash
# Base64 ile kodlanmış hash analizi
nth -t "XUkQS8xLJnY5cZ0REXxFkg==" --base64

# Hex ile kodlanmış dizgi analizi
nth -t "3564343134303261626334623261373662393731396439313130313763353932" --hex
```

---

## Otomasyon ve Kırma Hatlarına Entegrasyon

Bilinmeyen bir hash'in algoritmasını tespit edip doğrudan Hashcat'e ileten tek satırlık bash boru hattı:

```bash
# 1. Hash türünü tespit et ve en olası Hashcat modunu çek
HASH="b4b9b02e6f09a9bd760f388b67351e2b"
MODE=$(nth -t "$HASH" --hashcat --popularity 1 | grep -oP '(?<=Hashcat: -m )\d+' | head -n 1)

# 2. Hashcat ile doğrudan kırma işlemine başla
hashcat -m "$MODE" -a 0 "$HASH" /usr/share/wordlists/rockyou.txt
```
