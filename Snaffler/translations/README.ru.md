<!-- language-home-navigation -->
[🏠 Русская главная страница](../../README.ru.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🐶 Snaffler — полная шпаргалка

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ███████╗███╗   ██╗ █████╗ ███████╗███████╗██╗     ███████╗██████╗ 
  ██╔════╝████╗  ██║██╔══██╗██╔════╝██╔════╝██║     ██╔════╝██╔══██╗
  ███████╗██╔██╗ ██║███████║█████╗  █████╗  ██║     █████╗  ██████╔╝
  ╚════██║██║╚██╗██║██╔══██║██╔══╝  ██╔══╝  ██║     ██╔══╝  ██╔══██╗
  ███████║██║ ╚████║██║  ██║██║     ██║     ███████╗███████╗██║  ██║
  ╚══════╝╚═╝  ╚═══╝╚═╝  ╚═╝╚═╝     ╚═╝     ╚══════╝╚══════╝╚═╝  ╚═╝
       Active Directory Domain SMB Share Credential & Secret Hunter
```
<p выравнивание="центр">
  <img src="https://img.shields.io/badge/Snaffler-Secret_Hunter-blue?style=for-the-badge" alt="Snaffler">
  <img src="https://img.shields.io/badge/Language-C%23_/_NET-orange?style=for-the-badge" alt="C# / .NET">
  <img src="https://img.shields.io/badge/Category-Active_Directory_&_Network-green?style=for-the-badge" alt="Active Directory">
  <img src="https://img.shields.io/badge/License-GPLv3-red?style=for-the-badge" alt="Лицензия GPLv3">
</p>

<p выравнивание="центр">
  <b>🌐 Автоматизированный инструмент домена Windows, предназначенный для сканирования общих ресурсов SMB, поиска важных файлов конфигурации, баз данных KeePass и учетных данных в виде открытого текста.</b>
</p>

---

## 📋 Содержание

- [Обзор и проблема охоты за акциями](#-overview--проблема-охоты за акциями)
- [Как работает Snaffler](#-how-snaffler-works)
- [Параметры установки и выполнения](#-installation--execution-options)
- [Сортировка грызунов и уровни серьезности](#-snaffler-triage--severity-levels)
- [Общие рабочие процессы сканирования](#-common-scanning-workflows)
  - [Общее сканирование по всему домену](#domain-wide-share-crawl)
  - [Таргетинг на определенные хосты или общие ресурсы](#targeting-activate-hosts-or-shares)
  - [Исключая компьютеры и корни DFS](#exclude-computers--dfs-roots)
- [Juicy Targets & File Match Rules](#-juicy-targets-file-match-rules)
- [Регистрация и анализ вывода] (#-output-logging--анализ)
- [Краткая справочная таблица](#-краткая справочная-таблица)
- [Защитные соображения и OPSEC](#-защитные-соображения--opsec)
- [Юридическая оговорка](#-юридическая оговорка)

---

## 🎯 Обзор и проблема охоты за акциями

В средних и крупных средах Active Directory на корпоративных файловых серверах и рабочих станциях размещаются тысячи открытых сетевых ресурсов SMB. Системные администраторы, разработчики и сотрудники часто оставляют после себя:

- 🔑 **Базы данных KeePass (`.kdbx`):** часто содержат мастер-пароли или легко взломанные мастер-ключи.
- ⚙️ **Файлы конфигурации (`web.config`, `appsettings.json`):** содержат жестко закодированные строки подключения SQL, пароли сервисных учетных записей и ключи API.
- 📜 **Сценарии развертывания (`unattend.xml`, `.ps1`, `.bat`):** жестко запрограммированные учетные данные администратора.
- 🔏 **Частные сертификаты (`.pfx`, `.p12`, `.pem`, `.ppk`):** можно использовать для повышения привилегий или горизонтального перемещения.

Просматривать тысячи акций вручную невозможно. **Snaffler**, разработанный **l33tb455**, автоматизирует весь этот процесс: он запрашивает Active Directory на наличие компьютерных объектов, перечисляет доступные для чтения общие ресурсы SMB, просматривает дерево каталогов и оценивает файлы на основе тщательно подобранных наборов правил регулярных выражений.

---

## ⚙️ Как работает Снаффлер
```
[ Compromised Domain Machine ]
         │
         ├── 1. Queries Domain Controller via LDAP for Computer Objects
         ├── 2. Connects to port 445 (SMB) across all discovered hosts
         ├── 3. Enumerates Read/Write permissions on C$, SYSVOL, NETLOGON, Custom Shares
         └── 4. Evaluates file extensions, file names, and file contents against rules
```
---

## 📥 Варианты установки и выполнения

Snaffler компилируется как отдельный двоичный файл .NET:
```bash
# Clone and build using Visual Studio or dotnet CLI on Windows
git clone https://github.com/SnaffCon/Snaffler.git
cd Snaffler
dotnet build -c Release
```
### Работа в Windows:
```cmd
# Standard execution outputting to screen and log file
Snaffler.exe -s -o snaffler_log.txt
```
### Запуск через Cobalt Strike/C2 (Execute-Assembly):
```text
beacon> execute-assembly C:\Tools\Snaffler.exe -s -o C:\Temp\loot.txt
```
---

## 🚦 Сортировка снафлеров и уровни серьезности

Снаффлер классифицирует полученные результаты по сегментам срочности с цветовой кодировкой:

- 🔴 **`{Red}` / Черный:** Определенно ценная находка! Пароли в виде открытого текста, закрытые ключи (`.pfx`, `.id_rsa`), токены AWS, файлы автоматической установки.
- 🟡 **`{Yellow}`:** Вероятно, находка. Файлы конфигурации со строками паролей, строками подключения к базе данных, теневыми копиями.
- 🟢 **`{Green}`:** Информационно/Интересно. Имена файлов соответствуют административным шаблонам (`passwords.xlsx`, `vpn.ovpn`).

---

## 🚀 Общие рабочие процессы сканирования

### Сканирование общих ресурсов по всему домену

Сканируйте все доступные общие ресурсы во всем домене Active Directory:
```cmd
# Run with live console streaming (-s) and save to disk
Snaffler.exe -s -o C:\loot\snaffler_domain.txt

# Run with multi-threading tuning (Default: 60 threads)
Snaffler.exe -s -t 100 -o C:\loot\snaffler_fast.txt
```
### Таргетинг на определенные хосты или общие ресурсы

Если вы хотите избежать сканирования всего домена и сосредоточиться на файловом сервере или контроллере домена:
```cmd
# Scan a single targeted file server
Snaffler.exe -c fs01.corp.local -s

# Scan a comma-separated list of machines
Snaffler.exe -c dc01.corp.local,fs02.corp.local,sql01.corp.local -s

# Scan a specific shared folder path directly
Snaffler.exe -i \\fs01.corp.local\Departments\IT\ -s
```
### Исключая компьютеры и корни DFS

Избегайте сканирования медленных или чувствительных компьютеров:
```cmd
# Exclude specific backup or surveillance servers
Snaffler.exe -s -c- backup01.corp.local,cctv01.corp.local
```
---

## 🎯 Сочные цели и правила сопоставления файлов

По умолчанию Snaffler ищет следующие важные расширения:

| Расширение/Файл | Ценный контент |
|------------------|--------------------|
| `.kdbx` / `.kdb` | Сейфы с менеджером паролей KeePass |
| `web.config` / `app.config` | Пароли к базе данных в открытом виде |
| `unattend.xml` / `sysprep.inf` | Пароли администратора Windows |
| `.ppk` / `.pem` / `id_rsa` | Закрытые ключи SSH |
| `.pfx` / `.p12` | Сертификаты Active Directory |
| `.rdp` / `.vnc` | Профили подключения к удаленному рабочему столу |
| `passwords.xlsx` / `creds.txt` | Списки паролей сотрудников |

---

## 📊 Регистрация и анализ выходных данных
```bash
# Grep for immediate RED credentials from a Linux terminal
grep -i "{Red}" snaffler_domain.txt

# Find all discovered KeePass safes
grep -i "\.kdbx" snaffler_domain.txt

# Filter out database connection strings
grep -i "password=" snaffler_domain.txt
```
---

## ⚡ Краткая справочная таблица

| Цель | Команда |
|-----------|---------|
| **Сканирование по всему домену** | `Snaffler.exe -s -o out.log` |
| **Целевой компьютер** | `Snaffler.exe -c <HOSTNAME> -s` |
| **Целевая доля** | `Snaffler.exe -i \\<HOST>\<SHARE> -s` |
| **Настройка потоков параллелизма**| `Snaffler.exe -s -t 120 -o out.log` |
| **Исключить хост из сканирования** | `Snaffler.exe -s -c- <EXCLUDE_HOST>` |
| **Максимальный размер файла**| `Snaffler.exe -s -z 5000000` *(Ограничение файлов до 5 МБ)* |
| **Выполнение с помощью сборки C2**| `execute-assembly Snaffler.exe -s` |

---

## 🛡️ Оборонительные соображения и OPSEC

1. **Массовые скачки подключений SMB:** Snaffler подключается к порту 445 на каждом компьютере домена за считанные минуты. При этом создаются тысячи событий с кодом 4624 (тип успешного входа в систему 3 — сеть) и событий с идентификатором 5140 (обращение к общему сетевому объекту).
2. **Honeyfiles и приманки.** Современные защитники Active Directory развертывают общие ресурсы Canary и обманные файлы `.kdbx`, которые вызывают мгновенные оповещения SOC при открытии такими инструментами, как Snaffler.
3. **Ограничения выполнения:** Установите `-z` (максимальный размер файла в байтах), чтобы избежать сбоя сетевых ресурсов при чтении многогигабайтных файлов резервных копий ISO или VHD.

---

## ⚠️ Юридическая оговорка

> [!ВНИМАНИЕ]
> Snaffler предназначен исключительно для авторизованных тестов на проникновение, взаимодействия с красной командой и внутреннего аудита безопасности. Сканирование общих доменных ресурсов без авторизации представляет собой незаконное проникновение в сеть.