# theHarvester Bilgi Notu (Cheatsheet)

[theHarvester](https://github.com/laramies/theHarvester), açık kaynak istihbaratı (OSINT) ve dış keşif aşamalarında hedef etki alanına (domain) ait e-posta adreslerini, alt alan adlarını (subdomain), çalışan isimlerini, açık portları ve sanal sunucu (vhost) kayıtlarını onlarca farklı kamuya açık kaynaktan toplayan popüler bir keşif aracıdır.

---

## İçindekiler

- [Desteklenen Veri Kaynakları](#desteklenen-veri-kaynakları)
- [Kurulum & API Anahtarı Yapılandırması](#kurulum--api-anahtarı-yapılandırması)
- [Temel Alan Adı Keşfi](#temel-alan-adı-keşfi)
- [Pasif Çoklu Kaynak Taraması](#pasif-çoklu-kaynak-taraması)
- [Aktif DNS Doğrulama & Kaba Kuvvet](#aktif-dns-doğrulama--kaba-kuvvet)
- [Raporlama & Çıktı Formatları (HTML, XML)](#raporlama--çıktı-formatları-html-xml)
- [Sızma Testi İş Akışlarına Entegrasyon](#sızma-testi-iş-akışlarına-entegrasyon)

---

## Desteklenen Veri Kaynakları

theHarvester aktif ve pasif düzinelerce arama ve istihbarat motorunu destekler:
- **Ücretsiz Arama Motorları (API Gerekmez):** DuckDuckGo, Bing, Google, Yahoo, Baidu.
- **Sertifika Saydamlığı (Certificate Transparency):** CRT.sh, Certspotter.
- **Tehdit İstihbaratı & Kod Depoları:** GitHub, AlienVault OTX, ThreatCrowd, Rapid7, VirusTotal.
- **OSINT & Ağ API'leri:** Shodan, Censys, Hunter.io, SecurityTrails, Intelx, FullContact.
- **DNS & Ağ Veritabanları:** Anubis, HackerTarget, DNSDumpster.

---

## Kurulum & API Anahtarı Yapılandırması

### 1. Kurulum
```bash
# Debian / Ubuntu / Kali Linux
sudo apt-get update && sudo apt-get install -y theharvester

# GitHub Kaynak Kodundan (En Güncel Sürüm)
git clone https://github.com/laramies/theHarvester.git
cd theHarvester
python3 -m pip install -r requirements/base.txt

# Yardım menüsünü kontrol edin
theHarvester -h
```

### 2. API Anahtarlarını Tanımlama (`api-keys.yaml`)
Shodan, Hunter, Censys ve GitHub gibi servislerden tam verim almak için `api-keys.yaml` dosyasını düzenleyin:
```yaml
apikeys:
  hunter:
    key: "HUNTER_API_KEYINIZ"
  shodan:
    key: "SHODAN_API_KEYINIZ"
  github:
    key: "GITHUB_TOKENINIZ"
  securityTrails:
    key: "SECURITYTRAILS_KEYINIZ"
```

---

## Temel Alan Adı Keşfi

### 1. Ücretsiz Motorlarla E-posta ve Alt Alan Adı Toplama
```bash
theHarvester -d hedef.com -l 500 -b duckduckgo,bing,crtsh
```
- `-d <domain>`: Hedef alan adı.
- `-l <limit>`: Her arama motorundan çekilecek maksimum sonuç sayısı.
- `-b <kaynaklar>`: Sorgulanacak arama motorları listesi (veya `all`).

### 2. Yapılandırılmış Tüm Kaynakları Sorgulama
```bash
theHarvester -d hedef.com -l 500 -b all
```

---

## Pasif Çoklu Kaynak Taraması

Hedef sunucuya doğrudan tek bir TCP paketi bile göndermeden yalnızca açık istihbarat havuzlarını sorgulayın:

### 1. Sertifika Saydamlığı ve Tehdit Veritabanları
```bash
theHarvester -d hedef.com -b crtsh,certspotter,anubis,hackertarget,otx
```

### 2. Belirli Kaynak Gruplarını Sorgulama
```bash
# GitHub üzerindeki sızdırılmış e-posta ve subdomian kayıtları
theHarvester -d hedef.com -b github-code

# Hedef IP adreslerinin açık portlarını Shodan üzerinden çekme
theHarvester -d hedef.com -b shodan
```

---

## Aktif DNS Doğrulama & Kaba Kuvvet

### 1. Bulunan Subdomain'leri DNS ile Doğrulama (`-r`)
Listelenen alt alan adlarının gerçekten aktif bir IP adresine çözümlenip çözümlenmediğini kontrol edin:
```bash
theHarvester -d hedef.com -b crtsh,bing -r
```

### 2. Yerleşik DNS Brute-Force (`-c`)
theHarvester kelime listesiyle DNS kaba kuvvet taraması yapın:
```bash
theHarvester -d hedef.com -b duckduckgo -c
```

### 3. Sanal Sunucu Doğrulama (`-v`)
Sanal ana bilgisayarları (virtual hosts) HTTP sorguları ile doğrulayın:
```bash
theHarvester -d hedef.com -b duckduckgo -v
```

---

## Raporlama & Çıktı Formatları (HTML, XML)

Sonuçları raporlamak veya diğer araçlara iletmek için dışa aktarın:

### 1. HTML ve XML Olarak Kaydetme
```bash
theHarvester -d hedef.com -b duckduckgo,crtsh -f /tmp/theharvester_sonuclari
```
Komut `/tmp/theharvester_sonuclari.html` ve `.xml` dosyalarını oluşturur.

### 2. Canlı Alt Alan Adlarını Filtreleme
```bash
theHarvester -d hedef.com -b crtsh,duckduckgo -f recon_cikti
# Temiz host listesi ayıklama
cat recon_cikti.xml | grep -oP '(?<=<host>).*?(?=</host>)' | sort -u > canli_hostlar.txt
```

---

## Sızma Testi İş Akışlarına Entegrasyon

theHarvester sonuçlarını doğrudan zafiyet tarayıcılarına borulayın:

```bash
# 1. Pasif alt alan adlarını toplayın
theHarvester -d hedef.corp -b crtsh,duckduckgo,bing -f hedef_recon

# 2. Benzersiz alan adlarını ayıklayın
grep -oP '(?<=<host>)[^<]+' hedef_recon.xml | sort -u > hostlar.txt

# 3. Canlı web servislerini httpx ile tespit edin
cat hostlar.txt | httpx -silent -status-code -title > aktif_web.txt

# 4. Nuclei ile kritik zafiyetleri tarayın
nuclei -l aktif_web.txt -severity medium,high,critical
```
