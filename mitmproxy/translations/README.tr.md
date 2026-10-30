# 🕶️ mitmproxy - Kapsamlı Başvuru Rehberi (Cheatsheet)

[🇺🇸 English Version](../README.md)

```
  ███╗   ███╗██╗████████╗███╗   ███╗██████╗ ██████╗  ██████╗ ██╗  ██╗██╗   ██╗
  ████╗ ████║██║╚══██╔══╝████╗ ████║██╔══██╗██╔══██╗██╔═══██╗╚██╗██╔╝╚██╗ ██╔╝
  ██╔████╔██║██║   ██║   ██╔████╔██║██████╔╝██████╔╝██║   ██║ ╚███╔╝  ╚████╔╝ 
  ██║╚██╔╝██║██║   ██║   ██║╚██╔╝██║██╔═══╝ ██╔══██╗██║   ██║ ██╔██╗   ╚██╔╝  
  ██║ ╚═╝ ██║██║   ██║   ██║ ╚═╝ ██║██║     ██║  ██║╚██████╔╝██╔╝ ██╗   ██║   
  ╚═╝     ╚═╝╚═╝   ╚═╝   ╚═╝     ╚═╝╚═╝     ╚═╝  ╚═╝ ╚═════╝ ╚═╝  ╚═╝   ╚═╝   
    Etkileşimli, Python Betiklenebilir Terminal HTTP/HTTPS Proxy & Trafik Motoru
```

<p align="center">
  <img src="https://img.shields.io/badge/mitmproxy-Terminal_Proxy-blue?style=for-the-badge" alt="mitmproxy">
  <img src="https://img.shields.io/badge/Geliştirici-mitmproxy_Project-orange?style=for-the-badge" alt="mitmproxy">
  <img src="https://img.shields.io/badge/Kategori-Ağ_&_Web_Analizi-green?style=for-the-badge" alt="Network Analysis">
  <img src="https://img.shields.io/badge/Lisans-MIT-purple?style=for-the-badge" alt="MIT">
</p>

<p align="center">
  <b>🕶️ Sızma testi uzmanları ve güvenlik araştırmacıları için terminal tabanlı, zengin konsol arayüzüne ve Python eklenti desteğine sahip etkileşimli SSL/TLS HTTP/HTTPS araya girme (proxy) aracı</b>
</p>

---

## 📋 İçindekiler

- [Genel Bakış ve Üç Temel Araç](#-genel-bakış-ve-üç-temel-araç)
- [Kurulum ve SSL Sertifikası Yapılandırması](#-kurulum-ve-ssl-sertifikası-yapılandırması)
  - [Linux ve macOS Kurulumu](#linux-ve-macos-kurulumu)
  - [HTTPS Şifre Çözme İçin CA Sertifikası Yükleme](#https-şifre-çözme-i̇çin-ca-sertifikası-yükleme)
- [Çalışma Modları: Normal, Şeffaf (Transparent) ve Reverse Proxy](#-çalışma-modları-normal-şeffaf-transparent-ve-reverse-proxy)
- [Etkileşimli TUI Konsol Kullanımı (`mitmproxy`)](#-etkileşimli-tui-konsol-kullanımı-mitmproxy)
  - [Klavye Kısayolları](#klavye-kısayolları)
  - [Filtreleme ve Akışları Durdurma (Interception)](#filtreleme-ve-akışları-durdurma-interception)
- [Komut Satırından Otomasyon (`mitmdump`)](#-komut-satırından-otomasyon-mitmdump)
  - [Trafiği Kaydetme ve Yeniden Oynatma](#trafiği-kaydetme-ve-yeniden-oynatma)
- [Python Betikleri ve Eklenti (Addon) Geliştirme](#-python-betikleri-ve-eklenti-addon-geliştirme)
  - [Otomatik İstek Başlığı Ekleme](#otomatik-i̇stek-başlığı-ekleme)
  - [Dönen Yanıt Gövdesini (Body) Canlı Değiştirme](#dönen-yanıt-gövdesini-body-canlı-değiştirme)
- [Hızlı Referans Tablosu](#-hızlı-referans-tablosu)
- [Savunma Yöntemleri ve Tespit](#-savunma-yöntemleri-ve-tespit)
- [Yasal Uyarı](#-yasal-uyarı)

---

## 🎯 Genel Bakış ve Üç Temel Araç

Ağır grafik arayüzlü masaüstü araçlarının aksine **mitmproxy**, terminal konforunda ultra hızlı, otomatikleştirilebilir ve tam yetenekli HTTP/1, HTTP/2 ve WebSockets trafiği yakalama imkanı sunar.

Sistem üç farklı çalıştırma dosyası ile gelir:
1. **`mitmproxy`:** Etkileşimli, ncurses tabanlı terminal konsol arayüzü (TUI).
2. **`mitmdump`:** Etkileşimsiz, komut satırı sürümü (HTTP trafiğinin `tcpdump` karşılığı).
3. **`mitmweb`:** Hafif, web tarayıcısı üzerinden yönetilen grafik arayüz.

---

## 📥 Kurulum ve SSL Sertifikası Yapılandırması

### Linux ve macOS Kurulumu
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y mitmproxy

# Arch Linux
sudo pacman -S mitmproxy

# Python pipx ile Kurulum
pipx install mitmproxy
```

### HTTPS Şifre Çözme İçin CA Sertifikası Yükleme
HTTPS trafiğini şeffaf incelemek için istemci cihazın mitmproxy CA sertifikasına güvenmesi gerekir:
1. mitmproxy'yi bir kez başlatın: `mitmproxy` (`~/.mitmproxy/` dizininde sertifikalar üretilir).
2. İstemci cihaz/tarayıcı vekil sunucusunu `127.0.0.1:8080` yapın.
3. Tarayıcıdan `http://mitm.it` adresine gidip Android, iOS, Windows veya Linux sertifikasını kurun.

---

## ⚙️ Çalışma Modları: Normal, Şeffaf (Transparent) ve Reverse Proxy

### Normal Proxy Modu (Varsayılan)
8080 portunu dinleyen standart ileri vekil sunucu:
```bash
mitmproxy -p 8080
```

### Reverse Proxy Modu
Gelen tüm trafiği doğrudan belirli bir hedef sunucuya aktarır (API testleri için ideal):
```bash
mitmproxy --mode reverse:https://api.hedef.com -p 8080
```

### Şeffaf (Transparent) Mod
İstemci tarafında proxy ayarı yapmadan, iptables yönlendirmesiyle trafiği yakalama:
```bash
mitmproxy --mode transparent --showhost
```

---

## 🖥️ Etkileşimli TUI Konsol Kullanımı (`mitmproxy`)

### Klavye Kısayolları
- `?`: Yardım ekranını açar.
- `Enter`: Seçili HTTP akışını (İstek / Yanıt / Detay) açar.
- `q`: Geri döner / çıkar.
- `d`: Seçili akışı siler.
- `r`: Seçili isteği sunucuya tekrar gönderir (Replay).
- `e`: İstek/yanıt başlıklarını, adresini veya gövdesini düzenler.
- `a`: Duraklatılan isteği/yanıtı onaylayıp devam ettirir.
- `A`: Tüm duraklatılan akışları serbest bırakır.

### Filtreleme ve Akışları Durdurma (Interception)
`i` tuşuna basarak durdurma filtresi girebilirsiniz:
```text
~u /api/v1/ & ~m POST      # Yalnızca /api/v1/ adresine giden POST isteklerini yakala ve durdur
~c 401 | ~c 403            # Yalnızca 401 ve 403 HTTP durum kodlarını filtrele
~b "parola"                # Gövdesinde "parola" geçen istekleri filtrele
```

---

## ⚡ Komut Satırından Otomasyon (`mitmdump`)

### Trafiği Kaydetme ve Yeniden Oynatma
Trafiği doğrudan ikili döküm dosyasına kaydetme:
```bash
# Tüm trafiği dosyaya kaydet
mitmdump -w trafik.dump

# Kaydedilen dökümü sessizce oku
mitmdump -n -r trafik.dump

# Kayıttaki istemci isteklerini hedefe tekrar oynat
mitmdump --client-replay trafik.dump
```

---

## 🐍 Python Betikleri ve Eklenti (Addon) Geliştirme

mitmproxy yerel Python API'si ile gelen/giden trafiği programatik olarak değiştirebilir:

### Otomatik İstek Başlığı Ekleme (`baslik_ekle.py`)
```python
from mitmproxy import http

class BaslikEkle:
    def request(self, flow: http.HTTPFlow):
        # Dışarı giden her isteğe özel başlık ve token enjekte et
        flow.request.headers["X-Security-Audit"] = "Yetkili-Sizma-Testi"
        flow.request.headers["Authorization"] = "Bearer enjekte_edilen_token"

addons = [BaslikEkle()]
```
Çalıştırmak için:
```bash
mitmproxy -s baslik_ekle.py
```

### Dönen Yanıt Gövdesini (Body) Canlı Değiştirme (`yanit_degistir.py`)
```python
from mitmproxy import http

def response(flow: http.HTTPFlow):
    # Dönen kullanıcı profilinde yetkiyi true yap
    if "api/user/me" in flow.request.url:
        flow.response.text = flow.response.text.replace('"isAdmin": false', '"isAdmin": true')
```
Çalıştırmak için:
```bash
mitmdump -s yanit_degistir.py
```

---

## ⚡ Hızlı Referans Tablosu

| Amaç | Komut |
|------|-------|
| **Konsol Arayüzü Başlat** | `mitmproxy -p 8080` |
| **Web Arayüzünü Başlat** | `mitmweb -p 8080` |
| **Sessiz CLI Yakalayıcı** | `mitmdump -p 8080` |
| **Reverse Proxy Başlat** | `mitmproxy --mode reverse:https://hedef.com` |
| **Trafiği Dosyaya Kaydet**| `mitmdump -w capture.mitm` |
| **Python Betiği Çalıştır**| `mitmproxy -s betik.py` |
| **Belirli Hedefleri Yoksay**| `mitmproxy --ignore-hosts ".*google\.com"` |

---

## 🛡️ Savunma Yöntemleri ve Tespit

1. **Certificate Pinning:** Mobil uygulamalarda SSL Pinning kullanılarak mitmproxy CA sertifikası reddedilir. Frida / Objection ile bu korumanın aşılması gerekir.
2. **TLS Parmak İzi Analizi:** Proxy'ler kendilerine has TLS şifreleme paketleri ve ALPN parametreleri sunar; kurumsal WAF'lar bu imzaları tespit edebilir.

---

## ⚠️ Yasal Uyarı

> [!CAUTION]
> mitmproxy hassas ağ trafiğini çözme ve değiştirme yeteneğine sahiptir. Yalnızca yazılı izin alınmış test ortamlarında ve kendi sistemlerinizde kullanılmalıdır. İzniniz olmayan trafiği dinlemek yasa dışıdır.
