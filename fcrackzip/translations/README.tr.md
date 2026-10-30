# fcrackzip Bilgi Notu (Cheatsheet)

[fcrackzip](https://github.com/hyc/fcrackzip), parola korumalı ZIP arşivlerini kırmak için geliştirilmiş C tabanlı, hafif ve çok hızlı bir komut satırı aracıdır. Geleneksel ZipCrypto (PKZIP akış şifreleme) şifrelemesine sahip arşivlere karşı sözlük (dictionary) ve kaba kuvvet (brute-force) saldırılarını destekler.

---

## İçindekiler

- [Temel Özellikler & Kısıtlamalar](#temel-özellikler--kısıtlamalar)
- [Kurulum](#kurulum)
- [Sözlük Saldırıları (`-D -p`)](#sözlük-saldırıları--d--p)
- [Kaba Kuvvet (Brute-Force) Saldırıları (`-b`)](#kaba-kuvvet-brute-force-saldırıları--b)
- [Karakter Kümeleri & Uzunluk Tanımlama](#karakter-kümeleri--uzunluk-tanımlama)
- [Performans Testi (Benchmark)](#performans-testi-benchmark)
- [ZipCrypto ve AES-256 Farkı (John / Hashcat Alternatifi)](#zipcrypto-ve-aes-256-farkı-john--hashcat-alternatifi)

---

## Temel Özellikler & Kısıtlamalar

- **Ultra Hızlı ZipCrypto Kırıcı:** C ile yazılmış olup geleneksel PKZIP ZipCrypto algoritmasında saniyede milyonlarca deneme yapabilir.
- **Sözlük Modu:** Kelime listelerindeki aday parolaları sırayla dener.
- **Brute-Force Modu:** Karakter kümesi ve uzunluk sınırlarına göre anlık parola üretimi yapar.
- **Unzip Doğrulaması (`-u`):** Parolanın doğruluğunu arşivdeki dosyayı decompress edip CRC kontrolü yaparak test eder; yanlış pozitifleri (false-positive) eler.
- **Kısıtlama:** Modern WinZip AES-256 şifreli arşivleri desteklemez. AES-256 için `zip2john` + John the Ripper veya Hashcat (mod 13600) kullanılmalıdır.

---

## Kurulum

```bash
# Debian / Ubuntu / Kali Linux
sudo apt-get update && sudo apt-get install -y fcrackzip

# Arch Linux / BlackArch
sudo pacman -S fcrackzip

# Fedora / CentOS / RHEL
sudo dnf install -y fcrackzip

# Komutu doğrulayın
fcrackzip -h
```

---

## Sözlük Saldırıları (`-D -p`)

Hedefe yönelik veya genel kelime listeleriyle arşivi kırmak için:

### 1. Temel Sözlük Saldırısı (CRC Doğrulamalı)
```bash
fcrackzip -u -D -p /usr/share/wordlists/rockyou.txt gizli_arsiv.zip
```
- `-u`: Decompress doğrulaması yaparak yalancı pozitifleri engeller.
- `-D`: Sözlük (Dictionary) saldırı modunu seçer.
- `-p <dosya>`: Kullanılacak parola sözlüğü yolu.

### 2. Ayrıntılı (Verbose) Çıktı Modu
```bash
fcrackzip -v -u -D -p rockyou.txt yedek.zip
```

---

## Kaba Kuvvet (Brute-Force) Saldırıları (`-b`)

Sözlükte yer almayan kısa ve tahmin edilemeyen parolalar için:

### 1. Sayısal PIN Kırma (4 ile 6 Hane Arası)
```bash
fcrackzip -u -b -c 1 -l 4-6 guvenli.zip
```
- `-b`: Kaba kuvvet (brute-force) modu.
- `-c 1`: Yalnızca rakamlar (`0-9`).
- `-l 4-6`: Minimum 4, maksimum 6 karakter uzunluğu.

### 2. Küçük Harf ve Rakamlar (1 ile 5 Karakter Arası)
```bash
fcrackzip -u -b -c a1 -l 1-5 arsiv.zip
```
- `-c a1`: Küçük harfler (`a-z`) ve sayılar (`0-9`).

### 3. Tüm Karakter Kümeleri (Küçük, Büyük, Rakam, Özel Karakter)
```bash
fcrackzip -u -b -c aA1! -l 6-8 korumali.zip
```

---

## Karakter Kümeleri & Uzunluk Tanımlama

| Parametre | Karakter Kümesi | Örnek Parolalar |
|---|---|---|
| `-c a` | Küçük harfler (`a-z`) | `admin`, `gizli` |
| `-c A` | Büyük harfler (`A-Z`) | `GIZLI`, `ADMIN` |
| `-c 1` | Rakamlar (`0-9`) | `1234`, `2024` |
| `-c !` | Özel karakterler (`[!:$%&/()=?{[]}+*~#]`) | `!@#$` |
| `-c aA1` | Alfanumerik (Küçük, Büyük, Sayı) | `Admin123` |
| `-p <on_ek>` | Başlangıç tohumu (kaldığı yerden devam ettirme) | Uzun taramalarda yararlıdır |

Uzunluk aralığı belirtme:
```bash
# Minimum 6, Maksimum 8 karakter
-l 6-8

# Sabit 8 karakter
-l 8-8
```

---

## Performans Testi (Benchmark)

İşlemcinizin fcrackzip hızını ölçün:
```bash
fcrackzip -B
```

---

## ZipCrypto ve AES-256 Farkı (John / Hashcat Alternatifi)

Arşiv AES-256 ile şifrelendiğinde `fcrackzip` desteklemez (`unsupported encryption type` uyarısı). Bu durumda `zip2john` kullanılır:

```bash
# 1. Arşiv hash'ini zip2john ile çıkarın
zip2john aes_arsiv.zip > zip.hash

# 2. John the Ripper ile kırın
john --wordlist=/usr/share/wordlists/rockyou.txt zip.hash

# 3. Veya Hashcat ile kırın (Mod 13600: WinZip AES-256)
cat zip.hash | cut -d ':' -f 2 > hashcat_zip.hash
hashcat -m 13600 hashcat_zip.hash /usr/share/wordlists/rockyou.txt
```
