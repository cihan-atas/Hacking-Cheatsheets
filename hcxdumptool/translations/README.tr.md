# hcxdumptool Bilgi Notu (Cheatsheet)

[hcxdumptool](https://github.com/ZerBea/hcxdumptool), modern kablosuz ağ sızma testlerinde kullanılan yüksek performanslı bir Wi-Fi paket yakalama ve saldırı aracıdır. Eski Aircrack-ng yöntemlerinin aksine; ağa bağlı bir istemciye (kullanıcıya) ya da deauthentication (bağlantı koparma) saldırısına ihtiyaç duymadan, doğrudan erişim noktasından (AP) **PMKID** anahtar paketlerini yakalar ve `hcxtools` aracılığıyla modern Hashcat moduna (22000) dönüştürür.

---

## İçindekiler

- [Temel Avantajlar & Modern Mimari](#temel-avantajlar--modern-mimari)
- [Kurulum (`hcxdumptool` & `hcxtools`)](#kurulum-hcxdumptool--hcxtools)
- [Kablosuz Ağ Kartını Hazırlama](#kablosuz-ağ-kartını-hazırlama)
- [PMKID Yakalama (İstemcisiz Saldırı)](#pmkid-yakalama-istemcisiz-saldırı)
- [`hcxpcapngtool` ile Hashcat Formatına Dönüştürme](#hcxpcapngtool-ile-hashcat-formatına-dönüştürme)
- [Hashcat ile Parola Kırma (Mod 22000)](#hashcat-ile-parola-kırma-mod-22000)
- [Kanal ve BSSID Filtreleme](#kanal-ve-bssid-filtreleme)
- [Savunma Yöntemleri & WPA3](#savunma-yöntemleri--wpa3)

---

## Temel Avantajlar & Modern Mimari

- **İstemcisiz (Clientless) PMKID Saldırısı:** Modern yönlendiricilerin RSN IE (Robust Security Network Information Element) özelliğini istismar eder. Ağda hiçbir istemci bağlı olmasa dahi yönlendiriciyle el sıkışma başlatılarak PMKID elde edilir.
- **Tekil Hashcat Modu 22000:** Eski `hccapx` formatı yerine modern Hashcat `22000` biçimini üretir (hem PMKID hem de tam 4-way EAPOL el sıkışmalarını destekler).
- **Hızlı Kanal Atlama:** Birden fazla kanaldaki yönlendiricilere hızlı ilişkilendirme istekleri yollar.

---

## Kurulum (`hcxdumptool` & `hcxtools`)

```bash
# Debian / Ubuntu / Kali Linux
sudo apt-get update && sudo apt-get install -y hcxdumptool hcxtools

# Arch Linux / BlackArch
sudo pacman -S hcxdumptool hcxtools

# Kurulumu doğrulayın
hcxdumptool --version
hcxpcapngtool --version
```

---

## Kablosuz Ağ Kartını Hazırlama

Paket enjeksiyonunun engellenmemesi için arka plandaki ağ yöneticilerini kapatın ve kartı monitör moduna alın:

```bash
# 1. Çakışan ağ servislerini durdurun
sudo systemctl stop NetworkManager
sudo systemctl stop wpa_supplicant

# 2. Ağ kartını monitör moduna geçirin
sudo ip link set wlan0 down
sudo iw dev wlan0 set type monitor
sudo ip link set wlan0 up

# 3. Monitör modunu doğrulayın
iw dev
```

---

## PMKID Yakalama (İstemcisiz Saldırı)

### 1. Tüm Kanallarda Genel PMKID Taraması
Çevredeki tüm erişim noktalarını `.pcapng` formatında kaydedin:
```bash
sudo hcxdumptool -i wlan0 -w yakalama.pcapng --rds=1
```
- `-i wlan0`: Monitör modundaki kablosuz arayüz.
- `-w yakalama.pcapng`: Çıktı yakalama dosyası.
- `--rds=1`: Yönlendiricilerden yanıt almak için RTS paketleri yollar.

### 2. Yalnızca Belirli Bir Hedef Modemi (BSSID) Dinleme
Yalnızca yetkili olunan modemi hedefleyerek çevre ağları etkilemeyin:

1. Hedef filtre dosyası oluşturun (`hedef_bssid.txt`):
```text
001122334455
```
*(İki nokta üst üste olmadan MAC adresi)*

2. Hedefli yakalamayı başlatın:
```bash
sudo hcxdumptool -i wlan0 -w hedef.pcapng --bpfp=hedef_bssid.txt -c 6
```
- `-c 6`: Kartı sadece 6. kanala kilitler.
- `--bpfp`: Yalnızca hedef BSSID paketlerini filtreler.

---

## `hcxpcapngtool` ile Hashcat Formatına Dönüştürme

Yakalanan paketleri Hashcat tarafından kırılabilecek hash formatına dönüştürün:

```bash
hcxpcapngtool -o hash.hc22000 -E ag_isimleri.txt yakalama.pcapng
```
- `-o hash.hc22000`: Hashcat uyumlu çıktı dosyası.
- `-E ag_isimleri.txt`: Yakalanan ESSID (Wi-Fi adları) listesini parola denemelerinde kullanılmak üzere ayıklar.

---

## Hashcat ile Parola Kırma (Mod 22000)

`22000` modu, WPA/WPA2 PMKID ve EAPOL el sıkışmalarını kırmak için endüstri standardıdır:

### 1. Kelime Listesi (Sözlük) Saldırısı
```bash
hashcat -m 22000 hash.hc22000 /usr/share/wordlists/rockyou.txt
```

### 2. Kural Tabanlı Sözlük Saldırısı
```bash
hashcat -m 22000 hash.hc22000 /usr/share/wordlists/rockyou.txt -r /usr/share/hashcat/rules/best64.rule
```

### 3. Maske / Kaba Kuvvet (8 Haneli Sayısal Şifreler)
```bash
hashcat -m 22000 hash.hc22000 -a 3 ?d?d?d?d?d?d?d?d
```

---

## Kanal ve BSSID Filtreleme

Gereksiz bant genişliği ve tarama süresi kaybını önlemek için:

```bash
# Yalnızca belirli kanalları (örn. 1, 6, 11) tara
sudo hcxdumptool -i wlan0 -w filtrelenmis.pcapng -c 1,6,11
```

---

## Savunma Yöntemleri & WPA3

| Zafiyet Türü | Risk Nedeni | Savunma ve Önlem |
|---|---|---|
| **PMKID Zafiyeti** | Yönlendirici istemci olmadan 1. el sıkışma paketinde PMKID yayınlar | Gerekmiyorsa 802.11r (Fast Transition) kapatılmalı, modem firmware güncellenmelidir. |
| **Zayıf WPA2 Parolası** | Çevrim dışı PBKDF2 sözlük saldırısı (Hashcat 22000) | En az 16 karakterli tahmin edilemez rastgele parolalar kullanılmalıdır. |
| **WPA2 Protokolü** | PMKID ve 4-way handshake yakalanabilir | **WPA3-Personal (SAE)** protokolüne geçilmelidir; WPA3'te yakalanan paketler çevrim dışı kırılamaz. |
