<!-- language-home-navigation -->
[🏠 Русская главная страница](../../README.ru.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🛡️ WPScan — полная шпаргалка

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ██╗    ██╗██████╗ ███████╗ ██████╗ █████╗ ███╗   ██╗
  ██║    ██║██╔══██╗██╔════╝██╔════╝██╔══██╗████╗  ██║
  ██║ █╗ ██║██████╔╝███████╗██║     ███████║██╔██╗ ██║
  ██║███╗██║██╔═══╝ ╚════██║██║     ██╔══██║██║╚██╗██║
  ╚███╔███╔╝██║     ███████║╚██████╗██║  ██║██║ ╚████║
   ╚══╝╚══╝ ╚═╝     ╚══════╝ ╚═════╝╚═╝  ╚═╝╚═╝  ╚═══╝
       The WordPress Security & Vulnerability Scanner
```
<p выравнивание="центр">
  <img src="https://img.shields.io/badge/WPScan-WordPress_Security-blue?style=for-the-badge" alt="WPScan">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-orange?style=for-the-badge" alt="Веб-безопасность">
  <img src="https://img.shields.io/badge/Database-WPVDB_API-green?style=for-the-badge" alt="WPVDB API">
  <img src="https://img.shields.io/badge/License-GPLv3-red?style=for-the-badge" alt="Лицензия GPLv3">
</p>

<p выравнивание="центр">
  <b>🌐 Сканер уязвимостей «черный ящик», предназначенный для обнаружения уязвимостей ядра, плагинов, тем и пользователей WordPress</b>
</p>

---

## 📋 Содержание

- [Обзор и архитектура](#-обзор--архитектура)
- [Установка](#-установка)
- [Интеграция токенов API (WPVDB)](#-api-token-integration-wpvdb)
- [Базовое сканирование и перечисление целей](#-basic-scan--target-enumeration)
- [Разбивка флагов перечисления (`-e`)](#-enumeration-flags-breakdown--e)
  - [Уязвимые плагины (`-e vp`)](#vulnerable-plugins--e-vp)
  - [Все плагины (`-e ap`)](#all-plugins--e-ap)
  - [Темы (`-e vt`, `-e at`)](#themes--e-vt--e-at)
  - [Имена пользователей (`-e u`)](#usernames--e-u)
  - [Конфиденциальные резервные копии (`-e cb`, `-e dbe`)](#sensitive-backups--e-cb--e-dbe)
- [Режимы обнаружения (`--plugins-detection`)](#-detection-modes---plugins-detection)
- [Подбор пароля (`wp-login.php` и XML-RPC)](#-password-brute-forcing-wp-loginphp--xml-rpc)
- [Скрытность и уклонение от WAF](#-стелс--waf-уклонение)
- [Краткая справочная таблица](#-краткая справочная-таблица)
- [Защитные соображения и усиление](#-защитные-соображения--укрепление)
- [Юридическая оговорка](#-юридическая оговорка)

---

## 🎯 Обзор и архитектура

Поскольку WordPress используется более чем на 40% из 10 миллионов лучших веб-сайтов, это наиболее целевая система управления контентом (CMS) в мире.

**WPScan**, написанный на Ruby, представляет собой стандартный сканер уязвимостей «черного ящика» для установок WordPress. Он каталогизирует активные основные версии, перечисляет установленные плагины и темы, извлекает зарегистрированные имена пользователей-администраторов и сопоставляет их с **базой данных уязвимостей WPScan (WPVDB)**, содержащей тысячи известных CVE.

---

## 📥 Установка
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y wpscan

# Arch Linux
sudo pacman -S wpscan

# Ruby Gem (Any platform)
gem install wpscan

# Docker Container
docker run -it --rm wpscan/wpscan --url https://target.local
```
---

## 🔑 Интеграция токенов API (WPVDB)

Чтобы отобразить известные уязвимости и оценки CVE, WPScan требует бесплатный токен API от [wpscan.com](https://wpscan.com/):
```bash
# Provide API token via CLI
wpscan --url https://target.local --api-token YOUR_API_TOKEN

# Or export to environment variable
export WPSCAN_API_TOKEN="YOUR_API_TOKEN"
```
---

## 🚀 Базовое сканирование и перечисление целей
```bash
# Quick non-intrusive scan
wpscan --url https://target.local

# Update the local database before scanning
wpscan --update
```
---

## 🔍 Разбивка флагов перечисления (`-e`)

WPScan позволяет модульное перечисление целей с использованием флага `--enumerate` (или `-e`):

### Уязвимые плагины (`-e vp`)
Сканирует плагины, имеющие известные уязвимости безопасности в базе данных WPVDB:
```bash
wpscan --url https://target.local -e vp --api-token YOUR_TOKEN
```
### Все плагины (`-e ap`)
Проверка тысяч известных плагинов методом перебора по словарю:
```bash
wpscan --url https://target.local -e ap --plugins-detection aggressive
```
### Темы (`-e vt`, `-e at`)
```bash
# Enumerate vulnerable themes
wpscan --url https://target.local -e vt

# Enumerate all installed themes
wpscan --url https://target.local -e at
```
### Имена пользователей (`-e u`)
Извлекает имена пользователей зарегистрированных авторов через конечные точки REST API (`/wp-json/wp/v2/users`), архивы авторов (`/?author=1`) и сообщения об ошибках входа:
```bash
wpscan --url https://target.local -e u1-10
```
### Конфиденциальные резервные копии (`-e cb`, `-e dbe`)
Поиск открытых `wp-config.php.bak`, дампов баз данных и архивов резервных копий:
```bash
wpscan --url https://target.local -e cb,dbe
```
---

## ⚙️ Режимы обнаружения (`--plugins-detection`)

Управляет тщательностью поиска плагинов WPScan:
```bash
# Passive (Fast, inspects HTML source, script tags, style links)
wpscan --url https://target.local --plugins-detection passive

# Aggressive (Sends HTTP requests for thousands of plugin directories)
wpscan --url https://target.local --plugins-detection aggressive

# Mixed (Recommended default - passive followed by targeted active requests)
wpscan --url https://target.local --plugins-detection mixed
```
---

## 🔓 Подбор пароля (`wp-login.php` и XML-RPC)

После того, как действительные имена пользователей будут перечислены, запустите целевой подбор учетных данных:
```bash
# Brute force login using enumerated username list
wpscan --url https://target.local -U users.txt -P /usr/share/wordlists/rockyou.txt

# Password spray a single password across all users
wpscan --url https://target.local -U users.txt -p 'Winter2026!'

# Multi-threaded password attack
wpscan --url https://target.local -U admin -P passwords.txt -t 20
```
> [!ПРИМЕЧАНИЕ]
> Если на сервере включен `xmlrpc.php`, WPScan автоматически использует методы многовызова XML-RPC для проверки сотен комбинаций паролей за один HTTP-запрос!

---

## 🥷 Скрытность и уклонение от WAF
```bash
# Randomize User-Agent header to evade simple bot signatures
wpscan --url https://target.local --random-user-agent

# Throttle request rate (milliseconds between requests) to bypass rate limits
wpscan --url https://target.local --throttle 1000

# Route through SOCKS proxy / Tor
wpscan --url https://target.local --proxy socks5://127.0.0.1:9050

# Ignore SSL/TLS certificate warnings
wpscan --url https://target.local --disable-tls-checks
```
---

## ⚡ Краткая справочная таблица

| Цель | Команда |
|-----------|---------|
| **Стандартное сканирование** | `wpscan --url <URL> --api-token <TOKEN>` |
| **Перечень уязвимых плагинов** | `wpscan --url <URL> -e vp --api-token <TOKEN>` |
| **Перечислить все плагины** | `wpscan --url <URL> -e ap --plugins-detection aggressive` |
| **Перечислить пользователей** | `wpscan --url <URL> -e u` |
| **Перечислить резервные копии и конфигурации** | `wpscan --url <URL> -e cb,dbe` |
| **Перебор пароля** | `wpscan --url <URL> -U <USERS> -P <PASSWORDS>` |
| **Случайный пользовательский агент и скрытность** | `wpscan --url <URL> --random-user-agent --stealthy` |
| **Экспорт в файл JSON** | `wpscan --url <URL> -f json -o wpscan_report.json` |

---

## 🛡️ Соображения по защите и закалке

1. **Отключить XML-RPC:** Заблокируйте доступ к `xmlrpc.php` через конфигурацию `.htaccess` или Nginx, чтобы нейтрализовать высокоскоростные атаки перебора с несколькими вызовами.
2. **Ограничить конечную точку пользователей REST API.** Отключите публичный доступ к `/wp-json/wp/v2/users`, чтобы предотвратить тривиальное перечисление авторов.
3. **Регулярно обновляйте плагины.** Более 90% уязвимостей WordPress находятся в сторонних плагинах и темах, а не в ядре WordPress.

---

## ⚠️ Юридическая оговорка

> [!ВНИМАНИЕ]
> Эта шпаргалка предназначена только для авторизованного тестирования на проникновение, оценки уязвимостей и аудита веб-приложений. Подбор паролей или сканирование систем без явного разрешения являются незаконными.