# CUPP (Common User Passwords Profiler) Bilgi Notu (Cheatsheet)

[CUPP](https://github.com/Mebus/cupp) (Common User Passwords Profiler), hedefe yönelik sosyal mühendislik ve insan profillemesine dayalı özel parola sözlüğü (wordlist) üreten açık kaynaklı bir Python aracıdır. Rastgele veya genel sözlükler yerine; hedef kişinin adı, eşi, çocukları, evcil hayvanları, doğum tarihleri, şirketi ve hobileri üzerinden kombinasyonlar oluşturur.

---

## İçindekiler

- [Genel Bakış & Kullanım Alanları](#genel-bakış--kullanım-alanları)
- [Kurulum](#kurulum)
- [Etkileşimli Profil Sihirbazı (`-i`)](#etkileşimli-profil-sihirbazı--i)
- [Yapılandırma & Leet Kuralları (`cupp.cfg`)](#yapılandırma--leet-kuralları-cuppcfg)
- [Hazır Sözlük İndirme & Genişletme (`-l` & `-w`)](#hazır-sözlük-indirme--genişletme--l---w)
- [Hashcat & Hydra ile Birlikte Kullanım](#hashcat--hydra-ile-birlikte-kullanım)
- [Savunma Stratejileri & Parola Politikası](#savunma-stratejileri--parola-politikası)

---

## Genel Bakış & Kullanım Alanları

Genel kelime listeleri (`rockyou.txt` vb.) hedefe özgü oluşturulmuş karmaşık parolaları (örn. `Ahmet1987!`, `Karabas2021*`) kırmakta yetersiz kalabilir. CUPP bu sorunu hedefe özgü istihbaratla çözer:
- Hedefe ait kişisel bilgileri toplar.
- İsimleri, doğum yıllarını, önemli tarihleri ve sevilen şeyleri çaprazlar.
- Büyük/küçük harf, leet speak (1337) ve özel karakter ekleme kurallarını uygular.
- Gigabaytlarca boyut tutan genel sözlükler yerine çok daha küçük, hedef odaklı ve yüksek başarı oranına sahip bir kelime listesi üretir.

---

## Kurulum

```bash
# Depoyu klonlayın
git clone https://github.com/Mebus/cupp.git
cd cupp

# Kali Linux / Debian depolarından kurulum (mevcutsa)
sudo apt-get update && sudo apt-get install -y cupp

# Yardım menüsünü kontrol edin
python3 cupp.py -h
```

---

## Etkileşimli Profil Sihirbazı (`-i`)

Hedef kişi hakkında toplanan OSINT bilgilerini girmek için sihirbazı başlatın:

```bash
python3 cupp.py -i
```

Örnek bilgi girişi adımları:
```text
[+] Insert the targets information:
> First Name: Ahmet
> Surname: Yilmaz
> Nickname: ayilmaz
> Birthdate (DDMMYYYY): 15061990

[+] Insert partners information:
> Partners name: Ayse
> Partners nickname: ays
> Partners birthdate (DDMMYYYY): 04111992

[+] Insert child's information:
> Child's name: Can
> Child's nickname: cano
> Child's birthdate (DDMMYYYY): 20082018

[+] Insert pet's info:
> Pet's name: Karabas

[+] Insert company name:
> Company name: SiberHolding

[+] Do you want to add some special words about the victim? Y/n: y
> (virgülle ayırarak): besiktas, bjk, gitar, yazilim

[+] Do you want to add special characters at the end of words? Y/n: y
[+] Do you want to add some random numbers at the end of words? Y/n: y
[+] Leet mode? (örn. leet = 1337) Y/n: y
```

İşlem tamamlandığında binlerce filtrelenmiş kombinasyonu içeren bir `.txt` sözlük dosyası üretilir (örn. `Ahmet1990!`, `KarabasCan2018`, `SiberHolding@2023`).

---

## Yapılandırma & Leet Kuralları (`cupp.cfg`)

CUPP davranışları `cupp.cfg` dosyası üzerinden özelleştirilebilir:
```ini
[years]
# Kelimelerin sonuna otomatik eklenecek yıl aralığı
years = 1970,1980,1985,1990,1995,2000,2010,2020,2023,2024,2025,2026

[nums]
# Yaygın sayı kalıpları
nums = 1,12,123,1234,12345,123456,1903,1905,1907

[special]
# Eklenecek özel karakterler
chars = !,@,#,$,%,&,*,-,_

[leet]
# Leet speak karakter değişimleri
a = 4,@
e = 3
i = 1,!
o = 0
s = 5,$
```

---

## Hazır Sözlük İndirme & Genişletme (`-l` & `-w`)

### 1. Hazır Sözlük Paketlerini İndirme
```bash
python3 cupp.py -l
```

### 2. Mevcut Bir Sözlüğü Kurallarla Genişletme
Elinizdeki temel bir kelime listesine CUPP leet ve ekleme kurallarını uygulayın:
```bash
python3 cupp.py -w /path/to/base_words.txt
```

---

## Hashcat & Hydra ile Birlikte Kullanım

Üretilen sözlüğü doğrudan parola kırma araçlarına besleyin:

### Hashcat ile Hash Kırma
```bash
# Hedefe özel sözlük üretildikten sonra:
hashcat -m 1000 hedef_ntlm.hash ahmet.txt -r /usr/share/hashcat/rules/best64.rule
```

### Hydra ile Çevrim İçi SSH Brute-Force
```bash
hydra -l ahmet -P ahmet.txt 192.168.1.50 ssh
```

---

## Savunma Stratejileri & Parola Politikası

- **Kişisel Veri Yasağı:** Parola politikalarında kullanıcı adı, ad, soyad, doğum tarihi veya şirket adı içeren parolaların seçimi engellenmelidir.
- **Uzunluk Odaklı Parolalar (Passphrase):** 8 karakterli karmaşık parolalar yerine 16+ karakterden oluşan ve birbiriyle ilişkisiz en az 3-4 kelimelik parola cümleleri teşvik edilmelidir.
- **Çok Faktörlü Kimlik Doğrulama (MFA):** Hedefe özel oluşturulan sözlüklerin tek başına sistemlere sızmasını engellemek için FIDO2 veya TOTP tabanlı MFA zorunlu tutulmalıdır.
