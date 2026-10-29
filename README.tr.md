# 🔴 Hacking Cheatsheets

[🇬🇧 English](README.md) • [🇮🇹 Versione Italiana](README.it.md)

```
    ██╗  ██╗ █████╗  ██████╗██╗  ██╗██╗███╗   ██╗ ██████╗ 
    ██║  ██║██╔══██╗██╔════╝██║ ██╔╝██║████╗  ██║██╔════╝ 
    ███████║███████║██║     █████╔╝ ██║██╔██╗ ██║██║  ███╗
    ██╔══██║██╔══██║██║     ██╔═██╗ ██║██║╚██╗██║██║   ██║
    ██║  ██║██║  ██║╚██████╗██║  ██╗██║██║ ╚████║╚██████╔╝
    ╚═╝  ╚═╝╚═╝  ╚═╝ ╚═════╝╚═╝  ╚═╝╚═╝╚═╝  ╚═══╝ ╚═════╝ 
     ██████╗██╗  ██╗███████╗ █████╗ ████████╗███████╗██╗  ██╗███████╗███████╗████████╗███████╗
    ██╔════╝██║  ██║██╔════╝██╔══██╗╚══██╔══╝██╔════╝██║  ██║██╔════╝██╔════╝╚══██╔══╝██╔════╝
    ██║     ███████║█████╗  ███████║   ██║   ███████╗███████║█████╗  █████╗     ██║   ███████╗
    ██║     ██╔══██║██╔══╝  ██╔══██║   ██║   ╚════██║██╔══██║██╔══╝  ██╔══╝     ██║   ╚════██║
    ╚██████╗██║  ██║███████╗██║  ██║   ██║   ███████║██║  ██║███████╗███████╗   ██║   ███████║
     ╚═════╝╚═╝  ╚═╝╚══════╝╚═╝  ╚═╝   ╚═╝   ╚══════╝╚═╝  ╚═╝╚══════╝╚══════╝   ╚═╝   ╚══════╝
```

<p align="center">
  <img src="https://img.shields.io/badge/Penetration-Testing-red?style=for-the-badge" alt="Penetration Testing">
  <img src="https://img.shields.io/badge/Ethical-Hacking-orange?style=for-the-badge" alt="Ethical Hacking">
  <img src="https://img.shields.io/badge/Cybersecurity-blue?style=for-the-badge" alt="Cybersecurity">
  <img src="https://img.shields.io/badge/License-MIT-green?style=for-the-badge" alt="License">
</p>

<p align="center">
  <b>📚 Güvenlik profesyonelleri için kapsamlı sızma testi (penetration testing) başvuru kılavuzları</b>
</p>

<p align="center">
  <a href="#-kılavuzlar-cheatsheets">Kılavuzlar</a> •
  <a href="#-hızlı-başlangıç">Hızlı Başlangıç</a> •
  <a href="#-katkıda-bulunma">Katkıda Bulunma</a> •
  <a href="#-lisans">Lisans</a>
</p>

> ⚠️ **Bilgilendirme:** Bu depo Türkçe dil desteğine kavuşturulmaktadır. Henüz çevirisi tamamlanmamış dokümanlar orijinal İngilizce sürümlerine yönlendirilir. Çeviri süreci devam ettikçe Türkçe kılavuz bağlantıları güncellenecektir.

---

## 🎯 Proje Hakkında

**Hacking Cheatsheets**, sızma testi ve etik hackerlık araçları için derlenmiş kapsamlı bir hızlı başvuru rehberi koleksiyonudur. Her bir kılavuz şunları içerir:

- ✅ Araç işlevlerinin **açık ve net açıklamaları**
- ✅ Pratik örneklerle **komut sözdizimi (syntax)**
- ✅ **Gerçek hayat senaryoları** ve kullanım alanları
- ✅ Hızlı arama için **özet başvuru tabloları**
- ✅ Deneyimli pentester'lardan **ipuçları ve en iyi pratikler**

---

## 🎯 Saldırı Metodolojisi (Kill Chain)

> MITRE ATT&CK çerçevesini temel alan, sızma testleri için eksiksiz adım adım rehber.

| Aşama | Açıklama | Kılavuz |
|-------|----------|---------|
| **1. İlk Erişim (Initial Access)** | Exploitler, oltalama (phishing), kimlik bilgileri | [📄 Görüntüle](./Attack-Methodology/translations/01-Initial-Access.tr.md) |
| **2. Keşif (Enumeration)** | Sistem ve ağ keşfi | [📄 Görüntüle](./Attack-Methodology/translations/02-Enumeration.tr.md) |
| **3. Yetki Yükseltme (PrivEsc)** | root / SYSTEM yetkisi elde etme | [📄 Görüntüle](./Attack-Methodology/translations/03-Privilege-Escalation.tr.md) |
| **4. Yatay İlerleme (Lateral Movement)** | Ağ içerisinde yayılma | [📄 Görüntüle](./Attack-Methodology/translations/04-Lateral-Movement.tr.md) |
| **5. Kalıcılık (Persistence)** | Erişimi kalıcı hale getirme | [📄 Görüntüle](./Attack-Methodology/translations/05-Persistence.tr.md) |
| **6. Savunma Atlatma (Defense Evasion)** | AV / EDR / AMSI atlatma | [📄 Görüntüle](./Attack-Methodology/translations/06-Defense-Evasion.tr.md) |
| **7. Hedef Eylemler (Actions on Objectives)** | Veri sızdırma ve hedef etki | [📄 Görüntüle](./Attack-Methodology/translations/07-Actions-Objectives.tr.md) |

👉 **[Tüm Kill Chain Özeti](./Attack-Methodology/translations/README.tr.md)**

---

## 🛡️ Mavi Takım (Blue Team & Savunma)

> SOC analistleri ve olay müdahale (IR) ekipleri için eksiksiz savunma güvenliği kılavuzları.

| Konu | Açıklama | Kılavuz |
|------|----------|---------|
| **Olay Müdahalesi (Incident Response)** | IR yaşam döngüsü, izolasyon, prosedürler | [📄 Görüntüle](./Blue-Team/Incident-Response.md) |
| **Log Analizi** | Windows/Linux günlük analizi ve Kritik Event ID'ler | [📄 Görüntüle](./Blue-Team/Log-Analysis.md) |
| **SIEM Tespiti** | Splunk / ELK sorguları ve gösterge panelleri | [📄 Görüntüle](./Blue-Team/SIEM-Detection.md) |
| **Tehdit Avcılığı (Threat Hunting)** | Proaktif tehdit avlama teknikleri | [📄 Görüntüle](./Blue-Team/Threat-Hunting.md) |
| **Sıkılaştırma (Hardening)** | Windows ve Linux sistem sıkılaştırma kontrol listeleri | [📄 Görüntüle](./Blue-Team/Hardening.md) |
| **Sigma Kuralları** | Platform bağımsız tespit kuralları | [📄 Görüntüle](./Blue-Team/Sigma-Rules.md) |
| **YARA Kuralları** | Zararlı yazılım ve IOC tespit kalıpları | [📄 Görüntüle](./Blue-Team/YARA-Rules.md) |

👉 **[Tüm Mavi Takım Özeti](./Blue-Team/README.md)**

---

## ☁️ Bulut Güvenliği (Cloud Security)

> AWS, Azure ve GCP için bulut sızma testi kılavuzları.

| Sağlayıcı | Açıklama | Kılavuz |
|-----------|----------|---------|
| **AWS** | S3, IAM, Lambda, EC2, IMDSv1/v2 | [📄 Görüntüle](./Cloud-Security/AWS-Pentesting.md) |
| **Azure** | Azure AD, Blob Storage, Sanal Makineler, Key Vault | [📄 Görüntüle](./Cloud-Security/Azure-Pentesting.md) |
| **GCP** | GCS, IAM, Compute Engine, Cloud Functions | [📄 Görüntüle](./Cloud-Security/GCP-Pentesting.md) |

👉 **[Tüm Bulut Güvenliği Özeti](./Cloud-Security/README.md)**

---

## 📱 Mobil Güvenlik (Mobile Security)

> Android ve iOS için mobil uygulama sızma testi rehberleri.

| Platform | Açıklama | Kılavuz |
|----------|----------|---------|
| **Android** | APK analizi, Frida, root tespiti atlatma | [📄 Görüntüle](./Mobile-Security/Android-Pentesting.md) |
| **iOS** | IPA analizi, jailbreak, Objection, Keychain analizi | [📄 Görüntüle](./Mobile-Security/iOS-Pentesting.md) |

👉 **[Tüm Mobil Güvenlik Özeti](./Mobile-Security/README.md)**

---

## 🐳 Konteyner Güvenliği (Container Security)

> Docker ve Kubernetes sızma testi ve izolasyon atlatma rehberleri.

| Platform | Açıklama | Kılavuz |
|----------|----------|---------|
| **Docker** | Konteynerden kaçış (escape), imaj analizi, daemon istismarı | [📄 Görüntüle](./Container-Security/Docker-Pentesting.md) |
| **Kubernetes** | RBAC atlatma, pod'dan kaçış, gizli veri (secrets) çıkarma | [📄 Görüntüle](./Container-Security/Kubernetes-Pentesting.md) |

👉 **[Tüm Konteyner Güvenliği Özeti](./Container-Security/README.md)**

---

## 🎭 Sosyal Mühendislik (Social Engineering)

> Sosyal mühendislik teknikleri, oltalama senaryoları ve pretexting kılavuzları.

| Konu | Açıklama | Kılavuz |
|------|----------|---------|
| **Oltalama (Phishing)** | E-posta oltalaması, GoPhish, Evilginx2, vishing, smishing | [📄 Görüntüle](./Social-Engineering/Phishing.md) |
| **Pretexting** | Karakterler, senaryolar, psikolojik manipülasyon | [📄 Görüntüle](./Social-Engineering/Pretexting.md) |

👉 **[Tüm Sosyal Mühendislik Özeti](./Social-Engineering/README.md)**

---

## 📝 Raporlama Şablonları (Reporting Templates)

> Güvenlik uzmanları ve bug bounty avcıları için profesyonel rapor şablonları.

| Şablon | Açıklama | Kılavuz |
|--------|----------|---------|
| **Pentest Raporu** | Eksiksiz sızma testi teknik rapor yapısı | [📄 Görüntüle](./Reporting/Pentest-Report-Template.md) |
| **Bug Bounty Raporu** | HackerOne / Bugcrowd bulgu bildirim şablonu | [📄 Görüntüle](./Reporting/Bug-Bounty-Report-Template.md) |
| **Yönetici Özeti (Executive Summary)** | C-Level / üst yönetim için teknik olmayan özet | [📄 Görüntüle](./Reporting/Executive-Summary-Template.md) |

---

## 🔍 Açık Kaynak İstihbaratı (OSINT)

> Eksiksiz OSINT metodolojisi ve istihbarat toplama araçları.

| Konu | Açıklama | Kılavuz |
|------|----------|---------|
| **Kişi Arama** | İnternette hedef kişi analizi, telefon/adres araştırması | [📄 Görüntüle](./OSINT/People-Search.md) |
| **E-posta OSINT** | E-posta keşfi, veri sızıntısı (breach) kontrolü, doğrulama | [📄 Görüntüle](./OSINT/Email-OSINT.md) |
| **Sosyal Medya** | Kullanıcı adı tarama, platform spesifik OSINT | [📄 Görüntüle](./OSINT/Social-Media-OSINT.md) |
| **Domain & IP** | WHOIS, DNS, alt alan adı, IP istihbaratı | [📄 Görüntüle](./OSINT/Domain-IP-OSINT.md) |
| **Görsel OSINT** | Tersine görsel arama, EXIF metaveri analizi | [📄 Görüntüle](./OSINT/Image-OSINT.md) |

👉 **[Tüm OSINT Özeti](./OSINT/README.md)**

---

## 🌐 Ağ Sızma Testi (Network Pentesting)

> Kapsamlı yerel ve kurumsal ağ sızma testi rehberleri.

| Konu | Açıklama | Kılavuz |
|------|----------|---------|
| **Port Tarama** | Nmap, Masscan, RustScan | [📄 Görüntüle](./Network-Pentesting/Port-Scanning.md) |
| **Ağ Keşfi (Enumeration)** | SMB, SNMP, NFS, LDAP, DNS analizi | [📄 Görüntüle](./Network-Pentesting/Network-Enumeration.md) |
| **Ortadaki Adam (MITM)** | ARP spoofing, DNS spoofing, SSL strip | [📄 Görüntüle](./Network-Pentesting/MITM-Attacks.md) |
| **Servis İstismarı** | FTP, SSH, SMB, RDP, veritabanları | [📄 Görüntüle](./Network-Pentesting/Service-Exploitation.md) |

👉 **[Tüm Ağ Sızma Testi Özeti](./Network-Pentesting/README.md)**

---

## 🏁 CTF Kılavuzları (Capture The Flag)

> HackTheBox, TryHackMe ve CTF yarışmaları için kapsamlı çözme taktikleri.

| Kategori | Açıklama | Kılavuz |
|----------|----------|---------|
| **Web** | SQLi, XSS, SSTI, LFI, Kimlik doğrulama atlatma | [📄 Görüntüle](./CTF/Web-CTF.md) |
| **Kriptografi** | RSA, AES, hash türleri, kodlama, XOR | [📄 Görüntüle](./CTF/Crypto-CTF.md) |
| **Tersine Mühendislik** | Ghidra, IDA Pro, GDB, ikili dosya yamalama | [📄 Görüntüle](./CTF/Reverse-Engineering-CTF.md) |
| **Adli Bilişim (Forensics)** | Steganografi, bellek (memory), disk, PCAP analizi | [📄 Görüntüle](./CTF/Forensics-CTF.md) |
| **Pwn / İkili İstismar** | Yığın taşması (Buffer Overflow), ROP, kabuk kodu (shellcode) | [📄 Görüntüle](./CTF/Pwn-CTF.md) |

👉 **[Tüm CTF Özeti](./CTF/README.md)**

---

## 📡 IoT & Donanım Hacking

> Nesnelerin İnterneti (IoT) cihazları, bellenim (firmware) ve donanım inceleme rehberleri.

| Konu | Açıklama | Kılavuz |
|------|----------|---------|
| **Firmware Analizi** | Binwalk, bellenim çıkarma, tersine mühendislik, gizli veri avı | [📄 Görüntüle](./IoT-Hacking/Firmware-Analysis.md) |
| **Donanım Hacking** | UART, JTAG, SPI, I2C, hata ayıklama portları | [📄 Görüntüle](./IoT-Hacking/Hardware-Hacking.md) |

👉 **[Tüm IoT Hacking Özeti](./IoT-Hacking/README.md)**

---

## 📖 Kılavuzlar (Cheatsheets)

### 🔴 İstismar Çerçeveleri (Exploitation Frameworks)

| Araç | Açıklama | Kılavuz |
|------|----------|---------|
| **Metasploit Framework** | Dünyanın en popüler sızma testi çerçevesi | [📄 Görüntüle](./Metasploit/translations/README.tr.md) |
| **Meterpreter** | Gelişmiş istismar sonrası (post-exploitation) yükü | [📄 Görüntüle](./Metasploit/translations/Meterpreter.tr.md) |
| **Mimikatz** | Windows parola ve bilet çıkarma aracı | [📄 Görüntüle](./Mimikatz/translations/README.tr.md) |
| **PowerShell** | Sızma testleri için PowerShell komutları | [📄 Görüntüle](./PowerShell/README.md) |
| **Linux Komutları** | Sızma testleri için Linux & Bash kullanımı | [📄 Görüntüle](./Linux-Commands/README.md) |

### 🔍 Keşif & Tarama (Reconnaissance & Scanning)

| Araç | Açıklama | Kılavuz |
|------|----------|---------|
| **Nmap** | Ağ keşfi, port tarama ve güvenlik denetimi | [📄 Görüntüle](./Nmap/translations/README.tr.md) |
| **Gobuster** | Dizin, DNS ve sanal host (VHost) brute-force aracı | [📄 Görüntüle](./Gobuster/translations/README.tr.md) |
| **Nikto** | Web sunucusu güvenlik tarayıcısı | [📄 Görüntüle](./Nikto/translations/README.tr.md) |

### 🌐 Web Uygulama Güvenlik Testi

| Araç | Açıklama | Kılavuz |
|------|----------|---------|
| **SQLMap** | Otomatik SQL enjeksiyonu ve veritabanı ele geçirme | [📄 Görüntüle](./SQLMap/translations/README.tr.md) |
| **Burp Suite** | Web uygulaması güvenlik testi platformu | [📄 Görüntüle](./Burp-Suite/translations/README.tr.md) |
| **OWASP ZAP** | Açık kaynaklı web güvenlik tarayıcısı | [📄 Görüntüle](./OWASP-ZAP/translations/README.tr.md) |

### 🔓 Parola Kırma (Password Cracking)

| Araç | Açıklama | Kılavuz |
|------|----------|---------|
| **Hydra** | Hızlı ağ servisi parola deneme aracı | [📄 Görüntüle](./Hydra/translations/README.tr.md) |
| **John the Ripper** | Efsanevi çevrimdışı parola kırıcı | [📄 Görüntüle](./John-The-Ripper/translations/README.tr.md) |
| **Hashcat** | Dünyanın en hızlı GPU destekli parola kırıcısı | [📄 Görüntüle](./Hashcat/translations/README.tr.md) |

### 📡 Ağ Analizi (Network Analysis)

| Araç | Açıklama | Kılavuz |
|------|----------|---------|
| **Wireshark** | Ağ protokolü ve paket inceleme aracı | [📄 Görüntüle](./Wireshark/translations/README.tr.md) |
| **tcpdump** | Komut satırı ağ paketi yakalama ve inceleme | [📄 Görüntüle](./tcpdump/translations/README.tr.md) |

### 🐛 Bug Bounty & Varlık Keşfi

| Araç | Açıklama | Kılavuz |
|------|----------|---------|
| **⭐ BB Metodolojisi** | Eksiksiz bug bounty avcılık metodolojisi | [📄 Görüntüle](./Bug-Bounty-Methodology/translations/README.tr.md) |
| **Nuclei** | Şablon (template) tabanlı hızlı zafiyet tarayıcı | [📄 Görüntüle](./Nuclei/translations/README.tr.md) |
| **ffuf** | Yüksek performanslı web fuzzing aracı | [📄 Görüntüle](./ffuf/translations/README.tr.md) |
| **Subfinder** | Pasif alt alan adı (subdomain) keşfi | [📄 Görüntüle](./Subfinder/translations/README.tr.md) |
| **httpx** | Çok amaçlı hızlı HTTP araştırma ve durum aracı | [📄 Görüntüle](./httpx/translations/README.tr.md) |
| **Amass** | Derinlemesine saldırı yüzeyi haritalama | [📄 Görüntüle](./Amass/translations/README.tr.md) |
| **GAU** | Web arşivlerinden URL toplama (Get All URLs) | [📄 Görüntüle](./GAU/translations/README.tr.md) |
| **Katana** | Yeni nesil web tarama (crawler) aracı | [📄 Görüntüle](./Katana/translations/README.tr.md) |
| **Arjun** | Gizli HTTP parametre keşif aracı | [📄 Görüntüle](./Arjun/translations/README.tr.md) |
| **Dalfox** | Güçlü XSS tarayıcısı ve parametre analizcisi | [📄 Görüntüle](./Dalfox/translations/README.tr.md) |

### 💉 Payload (Saldırı Yükü) Koleksiyonu

| Zafiyet | Açıklama | Kılavuz |
|---------|----------|---------|
| **XSS** | Siteler Arası Betik Çalıştırma payload'ları | [📄 Görüntüle](./Payloads/translations/XSS.tr.md) |
| **SQLi** | SQL Enjeksiyonu payload'ları | [📄 Görüntüle](./Payloads/translations/SQLi.tr.md) |
| **LFI** | Yerel Dosya Dahil Etme (Local File Inclusion) | [📄 Görüntüle](./Payloads/translations/LFI.tr.md) |
| **SSTI** | Sunucu Taraflı Şablon Enjeksiyonu | [📄 Görüntüle](./Payloads/translations/SSTI.tr.md) |
| **Komut Enjeksiyonu** | İşletim sistemi komut enjeksiyonu | [📄 Görüntüle](./Payloads/translations/Command-Injection.tr.md) |
| **NoSQL Enjeksiyonu** | MongoDB, CouchDB, Redis payload'ları | [📄 Görüntüle](./Payloads/translations/NoSQL-Injection.tr.md) |
| **Deserialization** | Java, PHP, Python, .NET serileştirme zafiyetleri | [📄 Görüntüle](./Payloads/translations/Deserialization.tr.md) |
| **WebSocket Saldırıları** | CSWSH, soket enjeksiyonu, oturum çalma | [📄 Görüntüle](./Payloads/translations/WebSocket-Attacks.tr.md) |
| **GraphQL Enjeksiyonu** | Introspection, IDOR, sorgu manipülasyonu | [📄 Görüntüle](./Payloads/translations/GraphQL-Injection.tr.md) |

### 🔴 Web Zafiyetleri

| Zafiyet | Açıklama | Kılavuz |
|---------|----------|---------|
| **API Güvenliği** | REST / GraphQL / JWT test kılavuzu | [📄 Görüntüle](./API-Security/translations/README.tr.md) |
| **IDOR** | Güvensiz Doğrudan Nesne Başvurusu | [📄 Görüntüle](./IDOR/translations/README.tr.md) |
| **SSRF** | Sunucu Taraflı İstek Sahteciliği | [📄 Görüntüle](./SSRF/translations/README.tr.md) |
| **XXE** | XML Dış Varlık (External Entity) Enjeksiyonu | [📄 Görüntüle](./XXE/translations/README.tr.md) |
| **Yarış Durumları (Race Conditions)** | Zamanlama ve eşzamanlılık açıkları | [📄 Görüntüle](./Race-Conditions/translations/README.tr.md) |
| **Kimlik Doğrulama Atlatma** | Auth Bypass teknikleri ve oturum yönetimi | [📄 Görüntüle](./Auth-Bypass/translations/README.tr.md) |
| **CORS** | Çapraz Kaynak Paylaşımı yapılandırma hataları | [📄 Görüntüle](./CORS/translations/README.tr.md) |
| **Açık Yönlendirme (Open Redirect)** | Güvensiz URL yönlendirme zaafları | [📄 Görüntüle](./Open-Redirect/translations/README.tr.md) |

### 🛡️ İleri Düzey Saldırı Teknikleri

| Konu | Açıklama | Kılavuz |
|------|----------|---------|
| **WAF Bypass** | Gerçek kaynak IP tespiti ve WAF filtrelerini aşma | [📄 Görüntüle](./WAF-Bypass/translations/README.tr.md) |
| **Cloudflare Bypass** | Cloudflare arkasındaki gerçek sunucuyu bulma | [📄 Görüntüle](./Cloudflare-Bypass/translations/README.tr.md) |
| **Subdomain Takeover** | Sahipsiz CNAME kayıtlarını ele geçirme | [📄 Görüntüle](./Subdomain-Takeover/translations/README.tr.md) |
| **Web Önbellek Zehirleme** | Cache Poisoning ve Cache Deception | [📄 Görüntüle](./Cache-Poisoning/translations/README.tr.md) |
| **HTTP İstek Kaçakçılığı** | HTTP Request Smuggling (CL.TE / TE.CL) | [📄 Görüntüle](./HTTP-Request-Smuggling/translations/README.tr.md) |
| **Prototype Pollution** | JavaScript prototip manipülasyonu | [📄 Görüntüle](./Prototype-Pollution/translations/README.tr.md) |

### 🔎 Dorking & İstihbarat

| Araç | Açıklama | Kılavuz |
|------|----------|---------|
| **Google Dorking** | İleri düzey Google arama operatörleri | [📄 Görüntüle](./Google-Dorking/README.md) |
| **Shodan** | İnternete bağlı cihaz arama motoru | [📄 Görüntüle](./Shodan/README.md) |
| **GitHub Dorking** | Depolarda gizli anahtar ve parola avı | [📄 Görüntüle](./GitHub-Dorking/README.md) |

### 🔝 Yetki Yükseltme (Privilege Escalation)

| Konu | Açıklama | Kılavuz |
|------|----------|---------|
| **Linux PrivEsc** | Linux yetki yükseltme teknikleri | [📄 Görüntüle](./Linux-PrivEsc/translations/README.tr.md) |
| **Windows PrivEsc** | Windows yetki yükseltme teknikleri | [📄 Görüntüle](./Windows-PrivEsc/translations/README.tr.md) |

### 🔬 Dijital Adli Bilişim (Digital Forensics)

| Araç | Açıklama | Kılavuz |
|------|----------|---------|
| **Volatility** | Bellek (RAM) analizi çerçevesi | [📄 Görüntüle](./Volatility/README.md) |
| **Autopsy** | Grafik arayüzlü dijital adli tıp platformu | [📄 Görüntüle](./Autopsy/README.md) |
| **ExifTool** | Dosya üstveri (metadata) okuma ve düzenleme | [📄 Görüntüle](./ExifTool/README.md) |
| **Binwalk** | Bellenim ve dosya gömme analizi | [📄 Görüntüle](./Binwalk/README.md) |

### 🔄 Tersine Mühendislik (Reverse Engineering)

| Araç | Açıklama | Kılavuz |
|------|----------|---------|
| **Ghidra** | NSA tarafından geliştirilen tersine mühendislik paketi | [📄 Görüntüle](./Ghidra/README.md) |
| **GDB** | GNU Hata Ayıklayıcı (Linux Debugging) | [📄 Görüntüle](./GDB/README.md) |
| **x64dbg** | Windows x64/x32 hata ayıklayıcısı | [📄 Görüntüle](./x64dbg/README.md) |

### 📶 WiFi Güvenliği

| Araç | Açıklama | Kılavuz |
|------|----------|---------|
| **Aircrack-ng** | Kablosuz ağ güvenlik paketi (WPA/WPA2) | [📄 Görüntüle](./Aircrack-ng/README.md) |
| **Wifite** | Otomatik kablosuz ağ denetim aracı | [📄 Görüntüle](./Wifite/README.md) |
| **Bettercap** | Ağ saldırı çerçevesi (MITM / WiFi) | [📄 Görüntüle](./Bettercap/README.md) |

### 🏢 Active Directory Pentesting

| Araç | Açıklama | Kılavuz |
|------|----------|---------|
| **⭐ AD Metodolojisi** | Adım adım etki alanı ele geçirme kılavuzu | [📄 Görüntüle](./AD-Attack-Methodology/translations/README.tr.md) |
| **BloodHound** | AD saldırı yolu görselleştirme | [📄 Görüntüle](./BloodHound/translations/README.tr.md) |
| **Impacket** | Python tabanlı AD saldırı araç takımı | [📄 Görüntüle](./Impacket/translations/README.tr.md) |
| **CrackMapExec** | AD ağlarının İsviçre çakısı | [📄 Görüntüle](./CrackMapExec/translations/README.tr.md) |
| **Rubeus** | Kerberos istismar araç seti | [📄 Görüntüle](./Rubeus/translations/README.tr.md) |
| **PowerView** | PowerShell ile AD keşfi | [📄 Görüntüle](./PowerView/translations/README.tr.md) |
| **Responder** | LLMNR / NBT-NS zehirleme | [📄 Görüntüle](./Responder/translations/README.tr.md) |
| **Evil-WinRM** | Sızma testleri için WinRM kabuğu | [📄 Görüntüle](./Evil-WinRM/translations/README.tr.md) |
| **Kerbrute** | Kerberos kullanıcı doğrulama ve püskürtme | [📄 Görüntüle](./Kerbrute/translations/README.tr.md) |

### 📚 Ek Kaynaklar

| Kaynak | Açıklama | Kılavuz |
|--------|----------|---------|
| **Wordlists** | Kapsamlı kelime listesi rehberi | [📄 Görüntüle](./Wordlists/README.md) |
| **Kali Linux Araçları** | Kategorilerine göre 600+ Kali aracı | [📄 Görüntüle](./Kali-Linux-Tools/README.md) |

---

## 🚀 Hızlı Başlangıç

### Depoyu Klonlayın

```bash
git clone https://github.com/Ilias1988/Hacking-Cheatsheets.git
cd Hacking-Cheatsheets
```

### Kılavuzları İnceleyin

İstediğiniz aracın klasörüne gidip `README.md` (veya varsa `translations/README.tr.md`) dosyasını açabilirsiniz:

```bash
# Metasploit kılavuzunu görüntüleyin
cat Metasploit/README.md

# Veya favori editörünüzde açın
code Metasploit/
```

### Çevrimdışı (Offline) Erişim

Tüm kılavuzlar saf Markdown formatındadır, bu da onları:
- 📱 **Mobil uyumlu** hale getirir (her cihazda rahat okunur)
- 🔌 **Çevrimdışı erişilebilir** kılar (internet gerekmez)
- 🖨️ **Yazdırılabilir** yapar (fiziksel çıktı alabilirsiniz)
- 🔍 **Aranabilir** yapar (`grep`, `fzf` veya editör aramasını kullanabilirsiniz)

---

## 📂 Depo Yapısı

```
Hacking-Cheatsheets/
│
├── README.md                # Ana Dizin (İngilizce)
├── README.it.md             # İtalyanca Dizin
├── README.tr.md             # Türkçe Dizin
├── LICENSE                  # MIT Lisansı
├── CONTRIBUTING.md          # Katkı Kılavuzu (İngilizce)
├── CONTRIBUTING.it.md       # Katkı Kılavuzu (İtalyanca)
├── CONTRIBUTING.tr.md       # Katkı Kılavuzu (Türkçe)
├── .gitignore               # Git dışlama kuralları
│
├── Metasploit/              # Metasploit Framework
│   ├── README.md            # Kapsamlı msfconsole kılavuzu
│   └── Meterpreter.md       # Meterpreter başvuru sayfası
│
├── Nmap/                    # Ağ Tarayıcısı
│   └── README.md            # Kapsamlı Nmap kılavuzu
│
├── Gobuster/                # Dizin / DNS Numaralandırma
│   └── README.md            # Kapsamlı Gobuster kılavuzu
│
├── Nikto/                   # Web Sunucu Tarayıcısı
│   └── README.md            # Kapsamlı Nikto kılavuzu
│
├── SQLMap/                  # SQL Injection Aracı
│   └── README.md            # Kapsamlı SQLMap kılavuzu
│
├── Burp-Suite/              # Web Uygulama Güvenlik Testi
│   └── README.md            # Kapsamlı Burp Suite kılavuzu
│
└── ...
```

---

## 🤝 Katkıda Bulunma

Katkılarınızı memnuniyetle bekliyoruz! Bir pull request göndermeden önce lütfen [Katkıda Bulunma Kılavuzu'nu (CONTRIBUTING.tr.md)](CONTRIBUTING.tr.md) okuyun.

### Katkı Sağlama Yolları

- 📝 Henüz yer almayan araçlar için **yeni kılavuzlar ekleyin**
- 🔧 Mevcut kılavuzları daha iyi örneklerle **iyileştirin**
- 🐛 **Hataları bildirin** veya öneriler sunun
- 🌐 Kılavuzları **farklı dillere çevirin**
- ⭐ Desteğinizi göstermek için **bu depoya yıldız (star) verin!**

---

## ⚠️ Yasal Uyarı (Legal Disclaimer)

> **ÖNEMLİ:** Bu kılavuzlar yalnızca **eğitim amaçlı** ve **yetkili güvenlik testleri** için hazırlanmıştır.
> 
> - ✅ Sahip olduğunuz sistemlerde kullanın
> - ✅ Açık ve yazılı izin bulunan ortamlarda kullanın
> - ✅ Yasal sızma testi sözleşmeleri kapsamında kullanın
> - ❌ Yetkisiz erişim sağlamak için kesinlikle kullanmayın
> - ❌ Kötü niyetli faaliyetler için kullanmayın
> 
> **Bilgisayar sistemlerine izinsiz erişim sağlamak yasa dışıdır.** Yazarlar bu bilgilerin kötüye kullanılmasından sorumlu tutulamaz.

---

## 📜 Lisans

Bu proje MIT Lisansı altında lisanslanmıştır - ayrıntılar için [LICENSE](LICENSE) dosyasına bakın.

---

## 🌟 Destek Olun

Bu kılavuzları faydalı bulduysanız lütfen:

- Bu depoya **⭐ Yıldız (Star)** verin
- Katkıda bulunmak için **🍴 Fork** edin
- Güvenlik camiasındaki diğer meslektaşlarınızla **📢 Paylaşın**
- İyileştirme önerileri için **💬 Geri bildirimde bulunun**

---

<p align="center">
  <b>Happy Hacking! 🔴</b><br>
  <i>Unutmayın: Sorumlu davranın, etik hackleyin!</i>
</p>

---

<p align="center">
  Siber güvenlik topluluğu için ❤️ ile hazırlandı
</p>
