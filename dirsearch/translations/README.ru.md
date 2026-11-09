<!-- language-home-navigation -->
[🏠 Русская главная страница](../../README.ru.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🔍 dirsearch — Полная шпаргалка

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ██████╗ ██╗██████╗ ███████╗███████╗ █████╗ ██████╗  ██████╗██╗  ██╗
  ██╔══██╗██║██╔══██╗██╔════╝██╔════╝██╔══██╗██╔══██╗██╔════╝██║  ██║
  ██║  ██║██║██████╔╝███████╗█████╗  ███████║██████╔╝██║     ███████║
  ██║  ██║██║██╔══██╗╚════██║██╔══╝  ██╔══██║██╔══██╗██║     ██╔══██║
  ██████╔╝██║██║  ██║███████║███████╗██║  ██║██║  ██║╚██████╗██║  ██║
  ╚═════╝ ╚═╝╚═╝  ╚═╝╚══════╝╚══════╝╚═╝  ╚═╝╚═╝  ╚═╝ ╚═════╝╚═╝  ╚═╝
       Advanced Web Path Scanner & Directory Brute-Forcer
```
<p выравнивание="центр">
  <img src="https://img.shields.io/badge/dirsearch-Web_Bruteforcer-blue?style=for-the-badge" alt="dirsearch">
  <img src="https://img.shields.io/badge/Language-Python_Async-green?style=for-the-badge" alt="Python Async">
  <img src="https://img.shields.io/badge/Recon-Web_Discovery-orange?style=for-the-badge" alt="Web Discovery">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="Лицензия GPLv2">
</p>

<p выравнивание="центр">
  <b>🌐 Быстрый, многофункциональный сканер веб-путей с командной строкой и интеллектуальной обработкой расширений, рекурсией и сканированием</b>
</p>

---

## 📋 Содержание

- [Введение](#-введение)
- [Сравнение инструментов](#-инструмент-сравнение)
- [Установка](#-установка)
- [Основное использование и выбор цели] (#-basic-usage--target-selection)
- [Управление расширениями и суффиксами](#-extension--suffix-management)
- [Списки слов и словари](#-списки слов--словари)
- [Рекурсия и веб-сканирование](#-recursion--web-crawling)
- [Поточность, ограничение скорости и задержки](#-поточность-ограничение скорости--задержки)
- [Фильтрация и сопоставление ответов](#-filtering--matching-responses)
- [Настройка HTTP-запроса] (#-http-request-customization)
  - [Пользовательские заголовки и пользовательские агенты](#custom-headers--user-agents)
  - [Файлы cookie и аутентификация](#cookies--аутентификация)
  - [POST-данные и методы запроса](#post-data--request-methods)
- [Прокси, Tor и уклонение](#-proxies-tor--evasion)
- [Форматы вывода и отчеты](#-output-formats--reporting)
- [Реальные рецепты пентестов и Bug Bounty](#-real-world-pentest--bug-bounty-recipes)
- [Краткая справочная таблица](#-краткая справочная-таблица)
- [Советы и рекомендации](#-tips--best-practices)
- [Юридическая оговорка](#-юридическая оговорка)

---

## 🎯 Введение

**dirsearch** (созданный Мауро Сориа) — один из самых популярных и универсальных инструментов командной строки для обнаружения скрытых файлов и каталогов на веб-серверах. Написанный на Python, он обеспечивает непревзойденную гибкость благодаря богатым аргументам командной строки, цветному выводу терминала, автоматическому рекурсивному сканированию, интеллектуальному расширению расширений с подстановочными знаками и встроенным элементам управления уклонением.

Независимо от того, проводите ли вы веб-тестирование на проникновение, поиск ошибок или внутреннюю оценку CTF, `dirsearch` предназначен для обнаружения скрытых административных интерфейсов, резервных баз данных, забытых конечных точек API и конфиденциальных учетных данных.

### Ключевые особенности

| Особенность | Описание |
|---------|-------------|
| **Многопоточный** | Высокопроизводительный асинхронный и многопоточный механизм запросов |
| **Подстановочные знаки расширения** | Разверните `-e *`, чтобы автоматически протестировать все известные расширения веб-файлов |
| **Интегрированное сканирование** | Следует ссылкам HTML/JS для создания рекурсивных очередей каталогов |
| **Расширенная фильтрация** | Фильтрация ответов по коду состояния, размеру байтов, количеству слов, количеству строк или регулярному выражению |
| **Повтор необработанного запроса** | Загрузите полные HTTP-запросы Burp Suite через `--raw` |
| **Несколько форматов** | Создает отчеты в формате Plaintext, JSON, CSV, Markdown и HTML |

---

## ⚖️ Сравнение инструментов

| Особенность | поиск | ффуф | Фероксбастер | Гобастер | ДИРБ |
|---------|-----------|------|-------------|----------|------|
| **Язык** | Питон | Перейти | Ржавчина | Перейти | С |
| **Скорость** | ⭐⭐⭐⭐ (Быстро) | ⭐⭐⭐⭐⭐ (Очень быстро) | ⭐⭐⭐⭐⭐ (Невероятно быстро) | ⭐⭐⭐⭐ (Быстро) | ⭐⭐ (Низкая резьба) |
| **Рекурсия по умолчанию** | ✅ Автомат (`-r`) | Дополнительно (`-recursion`) | ✅ Автомат (`-d`) | Дополнительно (`-r`) | ✅ Автомат |
| **Подстановочный знак расширения (`*`)**| ✅ Родной (`-e *`) | ❌ Нет | ❌ Нет | ❌ Нет | ❌ Нет |
| **Встроенный веб-сканер** | ✅ Да (`--crawl`) | ❌ Нет | ✅ Да (`--extract-links`) | ❌ Нет | ❌ Нет |
| **POST/Фаззинг данных** | ✅ Да (`--data`) | ✅ Полный HTTP | ❌ Ограниченная | ❌ Нет | ❌ Нет |
| **Повтор необработанного запроса Burp**| ✅ Да (`--raw`) | ✅ Да (`-request`) | ❌ Нет | ❌ Нет | ❌ Нет |
| **Встроенный список слов** | ✅ Куратор (`db/dicc.txt`)| ❌ Нет | ❌ Нет | ❌ Нет | ✅ Небольшой список |

---

## 📥 Установка

### Использование pipx (рекомендуется для Kali и современного Linux)
```bash
# Install via pipx (isolated Python environment)
pipx install dirsearch

# Verify installation
dirsearch --version
```
### Использование пипа
```bash
# Install via standard pip
pip3 install dirsearch
```
### Использование Git Clone (прямо из исходного кода)
```bash
# Clone the official repository
git clone https://github.com/maurosoria/dirsearch.git --depth 1
cd dirsearch

# Install requirements and run
pip3 install -r requirements.txt
python3 dirsearch.py --version
```
### Пакет Kali Linux и Docker
```bash
# Kali / Debian repository
sudo apt update && sudo apt install -y dirsearch

# Docker container
docker run -it --rm maurosoria/dirsearch -u https://example.com -e php,html
```
---

## 🚀 Базовое использование и выбор цели

### Одиночная цель
```bash
# Basic scan against a single URL specifying extensions
dirsearch -u https://target.com -e php,html,js,txt

# Scan with trailing slash forced
dirsearch -u https://target.com -e php -f
```
### Несколько целей
```bash
# Scan a list of targets from file
dirsearch -l targets.txt -e php,aspx,txt

# Scan stdin input from subfinder / httpx
cat live_hosts.txt | dirsearch --stdin -e php
```
### Сканирование портов и таргетинг CIDR
```bash
# Scan specific ports on a host
dirsearch -u https://target.com --ports 80,443,8080,8443 -e php

# Scan an entire subnet range
dirsearch --cidr 192.168.1.0/24 -e html
```
---

## 🧩 Управление расширениями и суффиксами

`dirsearch` имеет самую мощную систему управления расширениями среди всех сканеров каталогов:
```bash
# Specify multiple extensions
dirsearch -u https://target.com -e php,html,json,txt,zip

# Use the wildcard (*) to test ALL known extensions automatically
dirsearch -u https://target.com -e *

# Add prefixes to wordlist entries (e.g. test_admin, dev_admin)
dirsearch -u https://target.com -e php --prefixes test_,dev_,staging_,old_

# Add suffixes to wordlist entries (e.g. admin_backup, admin_old)
dirsearch -u https://target.com -e php --suffixes _bak,~,_2026,_old
```
---

## 📚 Списки слов и словари

`dirsearch` включает в себя полный, проверенный в боях список слов на `db/dicc.txt`:
```bash
# Run using the default bundled dictionary
dirsearch -u https://target.com -e php

# Use SecLists Common or Medium wordlists
dirsearch -u https://target.com -e php -w /usr/share/seclists/Discovery/Web-Content/directory-list-2.3-medium.txt

# Use multiple wordlists simultaneously
dirsearch -u https://target.com -e php -w list1.txt,list2.txt

# Force all wordlist entries to lowercase or uppercase
dirsearch -u https://target.com -e php --lowercase
dirsearch -u https://target.com -e php --uppercase
```
---

## 🔄 Рекурсия и веб-сканирование

### Рекурсивное сканирование
```bash
# Recursively scan newly found directories
dirsearch -u https://target.com -e php -r

# Limit recursion depth to 2 levels
dirsearch -u https://target.com -e php -r --recursion-depth 2

# Recursively scan only if HTTP status is 200, 301, or 302
dirsearch -u https://target.com -e php -r --recursion-status 200,301,302
```
### Встроенный веб-сканер
```bash
# Crawl the target website for internal URLs and feed them into the scan queue
dirsearch -u https://target.com -e php --crawl
```
---

## ⚡ Многопоточность, ограничение скорости и задержки

Управляйте агрессивностью сканирования, чтобы предотвратить срабатывание ограничения скорости или брандмауэров веб-приложений (WAF):
```bash
# Set number of concurrent threads (default: 25)
dirsearch -u https://target.com -e php -t 50

# Add a delay between requests (e.g. 0.5 seconds)
dirsearch -u https://target.com -e php -s 0.5

# Enforce a maximum request rate per second (e.g. max 15 req/sec)
dirsearch -u https://target.com -e php --max-rate 15

# Customize HTTP connection timeouts and retries
dirsearch -u https://target.com -e php --timeout 8 --retries 3

# Exit scan immediately on excessive connection errors
dirsearch -u https://target.com -e php --exit-on-error
```
---

## 🎯 Фильтрация и сопоставление ответов

### Включение и исключение кода состояния
```bash
# Include only specific status codes (e.g., ignore everything except 200, 301, 403)
dirsearch -u https://target.com -e php -i 200,301,302,403

# Exclude specific status codes (e.g., exclude 404 and 500)
dirsearch -u https://target.com -e php -x 404,400,500,502,503
```
### Фильтрация по размеру, словам и содержимому
```bash
# Exclude responses by specific byte size (e.g. ignore custom 404 pages)
dirsearch -u https://target.com -e php --exclude-sizes 0B,1542B,3421B

# Exclude responses matching specific text on the page
dirsearch -u https://target.com -e php --exclude-texts "Not Found","Access Denied"

# Exclude responses matching a regular expression
dirsearch -u https://target.com -e php --exclude-regex "^Error:.*"
```
---

## 🛠️ Настройка HTTP-запроса

### Пользовательские заголовки и пользовательские агенты
```bash
# Set a custom header (e.g., authorization token or internal bypass header)
dirsearch -u https://target.com -e php -H "X-Forwarded-For: 127.0.0.1" -H "X-Requested-With: XMLHttpRequest"

# Load headers from a file
dirsearch -u https://target.com -e php --header-list custom_headers.txt

# Use a randomized User-Agent per request to bypass basic WAFs
dirsearch -u https://target.com -e php --random-agent

# Set a custom User-Agent
dirsearch -u https://target.com -e php --user-agent "Mozilla/5.0 (Windows NT 10.0; Win64; x64)"
```
### Файлы cookie и аутентификация
```bash
# Provide session cookies for authenticated scanning
dirsearch -u https://target.com -e php -b "PHPSESSID=abc123xyz; admin_logged=true"

# HTTP Basic Authentication (username:password)
dirsearch -u https://target.com -e php --auth admin:SecretPass123 --auth-type basic

# NTLM Authentication for corporate Windows portals
dirsearch -u https://target.com -e php --auth CORP\\alice:Welcome2026 --auth-type ntlm
```
### POST-данные и методы запроса
```bash
# Perform fuzzing using HTTP POST requests
dirsearch -u https://target.com/api -m POST -d "action=test&token=xyz" -e json

# Replay an entire raw HTTP request saved from Burp Suite
dirsearch -u https://target.com --raw request.txt -e php
```
---

## 🛡️ Прокси, Tor и Evasion
```bash
# Route all traffic through an HTTP or SOCKS5 proxy (e.g., Burp Suite)
dirsearch -u https://target.com -e php --proxy http://127.0.0.1:8080

# Route all traffic through the local Tor SOCKS proxy
dirsearch -u https://target.com -e php --tor

# Skip SSL/TLS certificate verification
dirsearch -u https://target.com -e php --no-check-certificate
```
---

## 📄 Форматы вывода и отчеты
```bash
# Save output as a clean text file
dirsearch -u https://target.com -e php -o results.txt

# Export results to JSON format
dirsearch -u https://target.com -e php -o results.json --format json

# Export results to HTML, CSV, or Markdown
dirsearch -u https://target.com -e php -o report.html --format html
dirsearch -u https://target.com -e php -o results.csv --format csv
dirsearch -u https://target.com -e php -o report.md --format markdown
```
---

## 🎯 Реальные рецепты пентестов и Bug Bounty

### 1. Быстрая полномасштабная веб-разведка
```bash
# Scan with common web extensions, follow redirects, random UA, 40 threads
dirsearch -u https://target.com \
  -e php,html,js,json,txt,zip \
  -t 40 \
  --random-agent \
  -i 200,301,302,403 \
  -o target_recon.txt
```
### 2. Поиск забытых резервных копий и утечек исходного кода
```bash
# Test for database dumps, git repos, env files, and zip archives
dirsearch -u https://target.com \
  -e bak,old,orig,zip,tar.gz,sql,env,git \
  -w /usr/share/seclists/Discovery/Web-Content/raft-medium-files.txt \
  -i 200 \
  --exclude-sizes 0B
```
### 3. Глубокое рекурсивное обнаружение каталогов
```bash
# 2-level recursion, crawling pages for new paths, ignoring error status codes
dirsearch -u https://target.com \
  -e php,html \
  -r --recursion-depth 2 \
  --crawl \
  -x 404,500,502 \
  -o deep_scan.json --format json
```
### 4. Аутентифицированная внутренняя оценка портала
```bash
# Scan through Burp Suite with internal session cookies and WAF bypass headers
dirsearch -u https://intranet.corp.local \
  -e aspx,ashx,config,txt \
  -b "ASP.NET_SessionId=xyz123; AuthToken=ey..." \
  -H "X-Forwarded-For: 127.0.0.1" \
  --proxy http://127.0.0.1:8080
```
---

## ⚡ Краткая справочная таблица

| Задача | Команда |
|------|---------|
| **Базовое сканирование с расширениями** | `dirsearch -u https://target.com -e php,html,txt` |
| **Подстановочный знак для всех расширений** | `dirsearch -u https://target.com -e *` |
| **Пользовательский список слов** | `dirsearch -u https://target.com -e php -w /path/to/wordlist.txt` |
| **Сканировать несколько целей** | `dirsearch -l targets.txt -e php,aspx` |
| **Рекурсивное обнаружение** | `dirsearch -u https://target.com -e php -r --recursion-depth 2` |
| **Сканировать целевые ссылки** | `dirsearch -u https://target.com -e php --crawl` |
| **Включить коды состояния** | `dirsearch -u https://target.com -e php -i 200,301,302,403` |
| **Исключить коды состояния** | `dirsearch -u https://target.com -e php -x 404,500,502` |
| **Исключить ложноположительные размеры**| `dirsearch -u https://target.com -e php --exclude-sizes 0B,1240B` |
| **Аутентификация с помощью файлов cookie** | `dirsearch -u https://target.com -e php -b "session=abc123"` |
| **Маршрут через Burp Suite** | `dirsearch -u https://target.com -e php --proxy http://127.0.0.1:8080` |
| **Сохранить отчет в формате JSON** | `dirsearch -u https://target.com -e php -o report.json --format json` |

---

## 💡 Советы и рекомендации

1. ** Используйте `-e *` экономно в больших списках слов: ** Расширение с подстановочными знаками умножает каждое слово на 20+ расширений. Используйте `-e *` только с небольшими списками, такими как `common.txt` или `dicc.txt`.
2. **Отфильтровывайте ложноположительные размеры.** Современные веб-платформы возвращают пользовательские страницы 404 с HTTP 200. Проверьте размер ответа по умолчанию и добавьте `--exclude-sizes <size>`, чтобы устранить шум.
3. **Всегда проверяйте запрещенные пути 403.** В современных облачных и корпоративных средах ответы HTTP 403 указывают на существование каталога или конечной точки. Задокументируйте все ошибки 403 для обхода заголовка и тестирования обхода пути.
4. **Используйте префиксы и суффиксы для резервных копий.** Разработчики часто сохраняют резервные копии конфиденциальных файлов (например, `config.php.bak`, `old_database.sql`). Используйте `--prefixes` и `--suffixes` для их автоматического обнаружения.

---

## ⚠️ Юридическая оговорка

> **ВАЖНО:** Несанкционированное сканирование портов, перебор каталогов и веб-перебор серверов без явного письменного разрешения являются незаконными. Используйте `dirsearch` строго в рамках юридических обязательств по выявлению ошибок, авторизованных тестов на проникновение и лабораторных сред CTF.