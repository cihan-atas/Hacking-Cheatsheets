# ⌨️ rlwrap - Kapsamlı Başvuru Kılavuzu

[🇺🇸 English Version](../README.md)

```
  ██████╗ ██╗     ██╗    ██╗██████╗  █████╗ ██████╗ 
  ██╔══██╗██║     ██║    ██║██╔══██╗██╔══██╗██╔══██╗
  ██████╔╝██║     ██║ █╗ ██║██████╔╝███████║██████╔╝
  ██╔══██╗██║     ██║███╗██║██╔══██╗██╔══██║██╔═══╝ 
  ██║  ██║███████╗╚███╔███╔╝██║  ██║██║  ██║██║     
  ╚═╝  ╚═╝╚══════╝ ╚══╝╚══╝ ╚═╝  ╚═╝╚═╝  ╚═╝╚═╝     
       Ters Bağlantı ve Ham Terminaller İçin Readline Sarmalayıcısı
```

<p align="center">
  <img src="https://img.shields.io/badge/rlwrap-Readline_Sarmalayici-blue?style=for-the-badge" alt="rlwrap">
  <img src="https://img.shields.io/badge/Yardımcı-Kabuk_Stabilizasyonu-green?style=for-the-badge" alt="Kabuk Stabilizasyonu">
  <img src="https://img.shields.io/badge/Kategori-İstismar_ve_Kabuklar-orange?style=for-the-badge" alt="İstismar ve Kabuklar">
  <img src="https://img.shields.io/badge/Lisans-GPLv2-red?style=for-the-badge" alt="Lisans GPLv2">
</p>

<p align="center">
  <b>🌐 Ham Netcat dinleyicilerini ve ilkel reverse shell oturumlarını yön tuşları, geçmiş ve otomatik tamamlama ile tam işlevli terminale dönüştürme rehberi</b>
</p>

---

## 📋 İçindekiler

- [Giriş: İlkel Kabuk ("Dumb Shell") Problemi](#-giriş-i̇lkel-kabuk-dumb-shell-problemi)
- [Kurulum](#-kurulum)
- [Temel Kullanım](#-temel-kullanım)
- [Reverse Shell Yakalama (`rlwrap + nc`)](#-reverse-shell-yakalama-rlwrap--nc)
- [Kalıcı Komut Geçmişi Yönetimi](#-kalıcı-komut-geçmişi-yönetimi)
- [Kelime Listesi ile Otomatik Tamamlama (`-f`)](#-kelime-listesi-ile-otomatik-tamamlama--f)
- [Tam Fonksiyonel TTY Kabuk Yükseltme (5 Adım)](#-tam-fonksiyonel-tty-kabuk-yükseltme-5-adım)
- [Diğer Pentest Araçlarıyla rlwrap Kullanımı](#-diğer-pentest-araçlarıyla-rlwrap-kullanımı)
  - [Oracle SQL*Plus](#oracle-sqlplus)
  - [Redis ve Telnet](#redis-ve-telnet)
  - [Socat ve Ham Soketler](#socat-ve-ham-soketler)
- [Hızlı Başvuru Tablosu](#-hızlı-başvuru-tablosu)
- [İpuçları ve En İyi Pratikler](#-i̇puçları-ve-en-i̇yi-pratikler)
- [Yasal Uyarı](#-yasal-uyarı)

---

## 🎯 Giriş: İlkel Kabuk ("Dumb Shell") Problemi

Geleneksel araçlarla (örneğin `nc -lvnp 4444`) bir ters bağlantı kabuğu (reverse shell) yakaladığınızda, elinize geçen oturum bir **ilkel terminaldir (dumb shell)**. İlkel kabuklarda modern uçbirim yetenekleri bulunmaz:

- ❌ **Yukarı/Aşağı yön tuşlarına** bastığınızda geçmiş komutlar gelmez; ekrana `^[[A` veya `^[[B` gibi anlamsız karakterler basılır.
- ❌ **Geri Silme (Backspace)** veya **Delete** tuşları karakteri silmek yerine `^H` ya da `^?` yazar.
- ❌ Yanlışlıkla **Ctrl + C** tuş kombinasyonuna bastığınız anda Netcat oturumu anında sonlanır ve elde edilen erişim kaybedilir.
- ❌ `su`, `sudo`, `passwd`, `nano` veya `vim` gibi etkileşimli komutlar arkada bir sözde uçbirim (pseudo-terminal / PTY) olmadığı için çalışmayı reddeder.
- ❌ Oturum kapandığında çalıştırılan hiçbir komut geçmişe kaydedilmez.

**rlwrap** (Hans Lub tarafından yazılmış Readline Wrapper), klavyeniz ile hedef komut arasına yerleşir. **GNU Readline** kütüphanesini kullanarak yön tuşlarıyla satır düzenleme, kalıcı komut geçmişi dosyaları ve sözlük tabanlı sekme (TAB) tamamlama özelliklerini ilkel kabuklara anında kazandırır!

---

## 📥 Kurulum

`rlwrap` oldukça küçük ve hafiftir; neredeyse tüm Linux dağıtımlarının resmi depolarında yer alır:

### Kali Linux, Debian ve Ubuntu

```bash
# Paket listesini güncelleyin ve rlwrap'i kurun
sudo apt update && sudo apt install -y rlwrap

# Kurulumu ve sürümü doğrulayın
rlwrap --version
```

### Fedora, CentOS ve RHEL

```bash
# Fedora / CentOS / RHEL
sudo dnf install -y rlwrap || sudo yum install -y epel-release && sudo yum install -y rlwrap
```

### Arch Linux ve Manjaro

```bash
# Arch Linux
sudo pacman -S rlwrap
```

### macOS (Homebrew)

```bash
# macOS
brew install rlwrap
```

---

## 🚀 Temel Kullanım

Temel sözdizimi, çalıştırmak istediğiniz komutun başına `rlwrap` eklemekten ibarettir:

```bash
# Genel kullanım sözdizimi
rlwrap [parametreler] <komut> [komut-argümanları]
```

### Sık Kullanılan Parametreler

| Parametre | Açıklama |
|-----------|----------|
| `-c` | Dosya ve dizin yolu tamamlamayı (TAB completion) etkinleştirir |
| `-A` | İstemci biçimlendirme kaçış kodlarını yok sayar (satır kaymalarını önler) |
| `-r` | Girdiğiniz tüm sözcükleri hafızaya alır ve tamamlama listesine ekler |
| `-H <dosya>` | Komut geçmişinin kaydedileceği özel geçmiş dosyasını belirtir |
| `-s <sayı>` | Saklanacak maksimum geçmiş satırı sayısı (Varsayılan: 300) |
| `-f <dosya>` | TAB ile tamamlanacak özel kelime listesi sözlüğü yükler |
| `-b <karakterler>`| Kelime bölme karakterlerini tanımlar |
| `-m` | Çok satırlı girdi düzenlemeyi etkinleştirir |
| `-p[renk]` | Komut satırı istemini renklendirir |

---

## 🎣 Reverse Shell Yakalama (`rlwrap + nc`)

Siber güvenlik operasyonlarında en yaygın kullanım şekli Netcat dinleyicisini sarmalamaktır:

```bash
# Yön tuşları ve geçmiş desteği olan temel dinleyici
rlwrap nc -lvnp 4444

# Profesyonel pentest bayrakları: -c (dosya tamamlama), -A (istem düzeltme), -r (sözcükleri hatırla)
rlwrap -cAr nc -lvnp 4444
```

### `-cAr` Bayraklarının Ayrıntılı Açıklaması:

1. **`-c` (File Completion):** `<TAB>` tuşuna bastığınızda uzak sunucudaki dizin ve dosya adlarını tamamlamaya yardımcı olur.
2. **`-A` (Prompts):** Uzak hedef standart dışı bir istemci dizgisi gönderdiğinde (örneğin `root@victim:/#`) satır başlarının üst üste binmesini ve görsel bozulmaları engeller.
3. **`-r` (Remember Words):** Oturum boyunca yazdığınız komut ve parametreleri hafızasında tutarak daha sonra TAB ile tamamlamanıza olanak tanır.

---

## 💾 Kalıcı Komut Geçmişi Yönetimi

`rlwrap`, komut geçmişini varsayılan olarak `~/.<komut>_history` dosyasında saklar. Farklı sızma testi projeleri veya hedefler için ayrı geçmiş dosyaları tanımlayabilirsiniz:

```bash
# Belirli bir hedef makineye özel geçmiş dosyası tanımlama
rlwrap -H ~/.hedef_musteri_gecmisi nc -lvnp 4444

# Geçmiş hafızasını 5.000 komuta yükseltme
rlwrap -H ~/.pentest_gecmisi -s 5000 nc -lvnp 4444

# Geçmiş aramasında büyük/küçük harf duyarlılığını kaldırma
rlwrap -i nc -lvnp 4444
```

---

## 🔍 Kelime Listesi ile Otomatik Tamamlama (`-f`)

`rlwrap`'e sık kullanılan Linux veya Windows komutlarını içeren bir metin dosyası vererek, ters bağlantı kabuğunda `<TAB>` tuşuna bastığınızda komutların otomatik tamamlanmasını sağlayabilirsiniz:

```bash
# Sık kullanılan sızma testi komutlarından oluşan sözlük oluşturun
cat << 'EOF' > /tmp/pentest_komutlari.txt
whoami
id
hostname
uname -a
cat /etc/passwd
cat /etc/shadow
sudo -l
find / -perm -4000 2>/dev/null
netstat -tulnp
ss -tulnp
ps aux
ip a
route -n
crontab -l
EOF

# Dinleyiciyi sözlük desteğiyle başlatın
rlwrap -f /tmp/pentest_komutlari.txt nc -lvnp 4444
```

---

## 🛠️ Tam Fonksiyonel TTY Kabuk Yükseltme (5 Adım)

`rlwrap` yön tuşları ve komut geçmişi sorununu anında çözse de, `su`, `sudo`, `vim` gibi etkileşimli programlar tam bir sanal uçbirim (TTY) gerektirir. Endüstri standardı 5 adımlı yükseltme prosedürü:

### 1. Adım: Dinleyiciyi rlwrap ile Başlatın

```bash
# Saldırgan makinesinde dinleyiciyi açın
rlwrap -cAr nc -lvnp 4444
```

### 2. Adım: Ters Bağlantıyı Alın ve PTY Başlatın

Hedef makineden bağlantı düştüğünde Python veya script ile sahte terminal açın:

```bash
# Hedef kabukta: Python ile PTY açma
python3 -c 'import pty; pty.spawn("/bin/bash")'

# Python yoksa script aracıyla:
/usr/bin/script -qc /bin/bash /dev/null
```

### 3. Adım: Kabuğu Arka Plana Alın

```bash
# Klavyeden Ctrl + Z tuşlarına basarak oturumu arka plana gönderin
[1]+  Stopped                 rlwrap -cAr nc -lvnp 4444
```

### 4. Adım: Yerel Uçbirimi Ham (Raw) Moda Alın

```bash
# Kendi terminalinizde yerel yankıyı kapatın ve kabuğu ön plana çağırın
stty raw -echo; fg
```

*(Komutu çalıştırdıktan sonra terminal yanıt vermiyor gibi görünürse art arda 2 kez Enter tuşuna basın)*

### 5. Adım: Terminal Türünü ve Boyutlarını Sıfırlayın

```bash
# Hedef kabukta: Terminal değişkenlerini tanımlayın
reset
export TERM=xterm-256color
export SHELL=bash

# Kendi makinenizde başka bir sekmede terminal boyutunuza bakın:
# $ stty size  -> Çıktı örneğin: 38 160

# Boyutları hedef kabuğa uygulayın:
stty rows 38 columns 160
```

> ✅ **Sonuç:** Artık `Ctrl+C` ile kesilmeyen, `Ctrl+L` ile ekranı temizleyen, `nano`/`vim`/`sudo` çalıştırabilen ve yön tuşları sorunsuz çalışan yok edilemez tam bir terminale sahipsiniz!

---

## 🔧 Diğer Pentest Araçlarıyla rlwrap Kullanımı

`rlwrap` yalnızca Netcat ile sınırlı değildir. Satır düzenleme desteği eksik olan tüm komut satırı araçlarında kullanılabilir:

### Oracle SQL*Plus

```bash
# Linux'ta Oracle sqlplus varsayılan olarak yön tuşlarını ve geçmişi desteklemez
rlwrap sqlplus scott/tiger@10.10.10.50:1521/ORCL
```

### Redis ve Telnet

```bash
# Redis etkileşimli komut konsolu
rlwrap redis-cli -h 10.10.10.50 -p 6379

# Açık metin portlara bağlanma (Telnet / SMTP / FTP)
rlwrap telnet 10.10.10.50 25
```

### Socat ve Ham Soketler

```bash
# Socat dinleyicisini rlwrap ile sarmalama
rlwrap socat file:`tty`,raw,echo=0 tcp-listen:4444

# CTF pwn veya ham soket meydan okumalarına bağlanma
rlwrap nc ctf.soru.local 1337
```

---

## ⚡ Hızlı Başvuru Tablosu

| İşlem | Komut |
|-------|-------|
| **Gelişmiş Netcat Dinleyicisi** | `rlwrap nc -lvnp 4444` |
| **Tam Bayrak Kombinasyonu** | `rlwrap -cAr nc -lvnp 4444` |
| **Özel Geçmiş Dosyası** | `rlwrap -H ~/.nc_gecmisi nc -lvnp 4444` |
| **Sözlükle TAB Tamamlama** | `rlwrap -f /yol/sozluk.txt nc -lvnp 4444` |
| **Geçmiş Satırını Artırma** | `rlwrap -s 10000 nc -lvnp 4444` |
| **Oracle SQLPlus Sarmalama** | `rlwrap sqlplus user/pass@host/db` |
| **Redis CLI Sarmalama** | `rlwrap redis-cli -h 10.10.10.50` |
| **Socat Sarmalama** | `rlwrap socat TCP-LISTEN:4444,reuseaddr -` |

---

## 💡 İpuçları ve En İyi Pratikler

1. **Bash / Zsh Alias Ekleyin:** Bir daha asla ham Netcat ile uğraşmamak için `~/.bashrc` veya `~/.zshrc` dosyanıza şu takma adı ekleyin:
   ```bash
   alias nc='rlwrap -cAr nc'
   ```
2. **`Ctrl+C` Korumasını Unutmayın:** `rlwrap` yön tuşları ve imleç sorunlarını çözer, ancak kararsız kabuklarda uzun süreli işlem yapacaksanız mutlaka 5 adımlı `stty raw -echo; fg` yükseltmesini uygulayın.
3. **Müşteri Bazlı Geçmiş İzolasyonu:** Farklı şirketlerde veya hedeflerde sızma testi yaparken komut geçmişlerinin birbirine karışmaması için her zaman `-H ~/.musteri_adi.hist` kullanın.

---

## ⚠️ Yasal Uyarı

> **ÖNEMLİ:** Ters bağlantı kabukları ve istismar sonrası araçlar yalnızca yasal izin alınmış sızma testlerinde, kırmızı takım operasyonlarında ve eğitim laboratuvarlarında kullanılmalıdır.
