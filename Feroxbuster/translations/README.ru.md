<!-- language-home-navigation -->
[🏠 Русская главная страница](../../README.ru.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🦀 Feroxbuster — Полная шпаргалка

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ███████╗███████╗██████╗  ██████╗ ██╗  ██╗██████╗ ██╗   ██╗███████╗████████╗███████╗██████╗ 
  ██╔════╝██╔════╝██╔══██╗██╔═══██╗╚██╗██╔╝██╔══██╗██║   ██║██╔════╝╚══██╔══╝██╔════╝██╔══██╗
  █████╗  █████╗  ██████╔╝██║   ██║ ╚███╔╝ ██████╔╝██║   ██║███████╗   ██║   █████╗  ██████╔╝
  ██╔══╝  ██╔══╝  ██╔══██╗██║   ██║ ██╔██╗ ██╔══██╗██║   ██║╚════██║   ██║   ██╔══╝  ██╔══██╗
  ██║     ███████╗██║  ██║╚██████╔╝██╔╝ ██╗██████╔╝╚██████╔╝███████║   ██║   ███████╗██║  ██║
  ╚═╝     ╚══════╝╚═╝  ╚═╝ ╚═════╝ ╚═╝  ╚═╝╚═════╝  ╚═════╝ ╚══════╝   ╚═╝   ╚══════╝╚═╝  ╚═╝
                Fast, Simple, Recursive Content Discovery
```
<p выравнивание="центр">
  <img src="https://img.shields.io/badge/Feroxbuster-Content_Discovery-orange?style=for-the-badge" alt="Feroxbuster">
  <img src="https://img.shields.io/badge/Rust-Blazingly_Fast-blue?style=for-the-badge" alt="Rust">
  <img src="https://img.shields.io/badge/Auto_Tune-Recursive-green?style=for-the-badge" alt="Recursive">
  <img src="https://img.shields.io/badge/Bug_Bounty-Web_Pentest-red?style=for-the-badge" alt="Веб-пентест">
</p>

<p выравнивание="центр">
  <b>⚡ Быстрый, простой и рекурсивный инструмент обнаружения контента, написанный на Rust</b>
</p>

---

## 📋 Содержание

- [Введение](#-введение)
- [Основные функции](#-ключевые функции)
- [Сравнение инструментов](#-инструмент-сравнение)
- [Установка](#-установка)
- [Основной синтаксис и быстрый старт](#-basic-syntax--quick-start)
- [Основные параметры команды](#-core-command-options)
  - [Управление рекурсией](#recursion-control)
  - [Параллелизм и производительность](#concurrency-- Performance)
  - [Расширения файлов](#расширения-файлов)
  - [Фильтрация и сопоставление](#filtering--сопоставление)
  - [Интеллектуальные функции (автонастройка, извлечение ссылок, резервное копирование)](#smart-features)
  - [Заголовки, файлы cookie и аутентификация](#headers-cookies--auth)
  - [Интеграция прокси и Burp Suite](#proxy--burp-suite-integration)
- [Многоцелевое сканирование и конвейеры](#-multi-target-scanning--pipelines)
- [Реальные рабочие процессы](#-реальные-рабочие процессы)
- [Краткая справочная таблица](#-краткая справочная-таблица)
- [Советы и рекомендации](#-tips--best-practices)
- [Юридическая оговорка](#-юридическая оговорка)
- [Ресурсы](#-ресурсы)

---

## 🎯 Введение

**Feroxbuster** — это высокопроизводительный многопоточный рекурсивный сканер веб-контента, написанный на Rust. Разработанный как быстрый, простой и интуитивно понятный, он сочетает в себе скорость фаззеров на основе Go/Rust со встроенным автоматическим рекурсивным обнаружением каталогов.

Он особенно эффективен во время тестов на проникновение и вознаграждений за обнаружение ошибок, когда необходимо быстро обнаружить глубокие деревья веб-сайтов и скрытые каталоги, не создавая сложных ручных сценариев.

---

## 🌟 Ключевые особенности

- ⚡ **Невероятно быстро:** Работает на асинхронном Rust (`tokio` и `reqwest`), способном обрабатывать тысячи запросов в секунду.
- 🔄 **Встроенное рекурсивное обнаружение:** автоматически обнаруживает новые каталоги и рекурсивно сканирует их до настраиваемых пределов глубины.
- 🧠 **Автонастройка и автоматическая блокировка:** автоматически регулирует скорость сканирования, если целевой веб-сервер замедляется или прерывается в случае возникновения чрезмерных ошибок соединения.
- 🔗 **Извлечение ссылок в режиме реального времени:** анализирует ответы HTML и JavaScript для обнаружения скрытых конечных точек и возвращает их в очередь сканирования.
- 📦 **Автоматический поиск резервных копий:** автоматически добавляет варианты `.bak`, `~`, `.old` и `.orig` к обнаруженным файлам.
- 🎯 **Конвейерный ввод:** легко принимает списки целей из `stdin` (из таких инструментов, как `subfinder`, `httpx` или `gau`).

---

## ⚖️ Сравнение инструментов

| Особенность | Фероксбастер | ффуф | поиск | Гобастер | Дирб |
|---------|-------------|------|-----------|----------|------|
| **Язык** | Ржавчина | Перейти | Питон | Перейти | С |
| **Скорость** | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐ |
| **Рекурсия по умолчанию** | ✅ Автомат | ❌ Ручной флаг | ✅ Автомат (`-r`) | ❌ Нет | ✅ Автомат |
| **Извлечение ссылок** | ✅ Да (`--extract-links`) | ❌ Нет | ❌ Нет | ❌ Нет | ❌ Нет |
| **Автоматическая настройка ставки** | ✅ Да (`--auto-tune`) | ❌ Нет | ❌ Нет | ❌ Нет | ❌ Нет |
| **ПОСТ-ФАЗЗИНГ** | ❌ Нет | ✅ Родной | ✅ Родной (`--data`) | ❌ Нет | ❌ Нет |
| **Многоцелевой стандартный ввод** | ✅ Родной | ❌ Ограниченная | ✅ Да (`-l`) | ❌ Нет | ❌ Нет |
| **Обнаружение резервных файлов**| ✅ Родной (`--collect-backups`) | ❌ Нет | ✅ Родной (`-e *`) | ❌ Нет | ❌ Нет |

---

## 📥 Установка

### Kali Linux и Debian/Ubuntu
```bash
# Update repository and install
sudo apt update && sudo apt install -y feroxbuster
```
### Использование Cargo (Менеджер пакетов Rust)
```bash
cargo install feroxbuster
```
### macOS (доморощенный)
```bash
brew install feroxbuster
```
### Докер
```bash
docker run --init -it epi052/feroxbuster -u https://target.com -w /usr/share/seclists/Discovery/Web-Content/common.txt
```
### Предварительно скомпилированный двоичный файл (выпуски GitHub)
```bash
# Download latest Linux release
curl -sL https://raw.githubusercontent.com/epi052/feroxbuster/main/install-nix.sh | bash
sudo mv feroxbuster /usr/local/bin/
```
---

## 🚀 Базовый синтаксис и быстрый старт
```bash
feroxbuster -u <TARGET_URL> -w <WORDLIST> [OPTIONS]
```
### Примеры быстрого сканирования
```bash
# Scan a single target with default settings
feroxbuster -u http://10.10.10.10

# Scan with custom SecLists directory list
feroxbuster -u https://example.com -w /usr/share/seclists/Discovery/Web-Content/directory-list-2.3-medium.txt

# Scan target including specific file extensions
feroxbuster -u https://example.com -w /usr/share/wordlists/dirb/common.txt -x php,html,txt,json
```
---

## ⚙️ Основные параметры команды

### Управление рекурсией
```bash
# Specify maximum recursion depth (default is 4)
feroxbuster -u https://example.com -w common.txt -d 2

# Completely disable recursion (scan root directory only)
feroxbuster -u https://example.com -w common.txt -n

# Don't scan nested subdirectories automatically (interactive prompt)
feroxbuster -u https://example.com -w common.txt --dont-scan /api,/static
```
### Параллелизм и производительность
```bash
# Set number of concurrent threads per directory (default: 50)
feroxbuster -u https://example.com -t 100

# Limit total concurrent directory scans running simultaneously (default: unlimited)
feroxbuster -u https://example.com -L 3

# Rate limit requests per second to avoid triggering WAF/firewall blocks
feroxbuster -u https://example.com --rate-limit 150

# Request timeout in seconds (default: 7)
feroxbuster -u https://example.com -T 5
```
### Расширения файлов
```bash
# Append extensions to each wordlist item
feroxbuster -u https://example.com -x php,asp,aspx,jsp,txt,bak

# Automatically collect and fuzz file extensions found during crawling
feroxbuster -u https://example.com --collect-extensions
```
### Фильтрация и сопоставление
```bash
# Match only specific status codes (e.g., 200 OK and 301 Redirects)
feroxbuster -u https://example.com -s 200 301 302

# Filter out specific status codes (e.g., exclude 404 and 403)
feroxbuster -u https://example.com -C 404 403 500

# Filter responses matching exact size in bytes
feroxbuster -u https://example.com -S 4092 1024

# Filter responses matching exact word count
feroxbuster -u https://example.com -W 42

# Filter responses matching exact line count
feroxbuster -u https://example.com -N 12

# Filter responses matching regular expressions (e.g., generic error pages)
feroxbuster -u https://example.com --filter-regex "Under Construction|Page Not Found"

# Filter responses similar to a known 404/wildcard page
feroxbuster -u https://example.com --filter-similar-to https://example.com/definitelynotfoundpage404
```
### Умные функции
```bash
# Auto-tune: Automatically adjusts request concurrency based on server response latency
feroxbuster -u https://example.com --auto-tune

# Auto-bail: Aborts scanning automatically when more than 100 consecutive network errors occur
feroxbuster -u https://example.com --auto-bail

# Extract links: Parses HTML and JavaScript to automatically uncover endpoints
feroxbuster -u https://example.com --extract-links

# Collect backups: Automatically tests for backup copies (.bak, ~, .old) of every discovered file
feroxbuster -u https://example.com --collect-backups
```
### Заголовки, файлы cookie и аутентификация
```bash
# Custom HTTP Header
feroxbuster -u https://example.com -H "Authorization: Bearer <TOKEN>" -H "X-Forwarded-For: 127.0.0.1"

# Session Cookie
feroxbuster -u https://example.com -b "session=abc123xyz; role=admin"

# Custom User-Agent string
feroxbuster -u https://example.com -a "Mozilla/5.0 (Windows NT 10.0; Win64; x64)"

# Ignore invalid / self-signed TLS certificates
feroxbuster -u https://10.10.10.10 -k
```
### Интеграция прокси и Burp Suite
```bash
# Route all requests through Burp Suite proxy
feroxbuster -u https://example.com -p http://127.0.0.1:8080 -k

# Convenience shortcut flag for Burp Suite (enables -k and sets proxy to localhost:8080)
feroxbuster -u https://example.com --burp
```
---

## 🔁 Многоцелевое сканирование и конвейеры

Feroxbuster изначально считывает целевые URL-адреса из стандартного ввода (`stdin`), что делает его идеальным для интеграции с конвейерами обнаружения активов:
```bash
# Pipeline: Subdomain discovery -> HTTP Probing -> Feroxbuster Fuzzing
subfinder -d example.com -silent | \
  httpx -silent -mc 200,301,302 | \
  feroxbuster --stdin -w /usr/share/seclists/Discovery/Web-Content/raft-medium-directories.txt -t 30 -L 4
```
### Сканирование URL-адресов из файла
```bash
cat targets.txt | feroxbuster --stdin -w common.txt -v
```
---

## 🎬 Реальные рабочие процессы

### Рабочий процесс 1: Комплексная веб-разведка Bug Bounty

Высокий уровень параллелизма, автоматическая настройка скорости, извлечение ссылок и обнаружение резервных копий:
```bash
feroxbuster -u https://target.com \
  -w /usr/share/seclists/Discovery/Web-Content/directory-list-2.3-medium.txt \
  -x php,js,json,txt,html \
  --extract-links \
  --collect-backups \
  --auto-tune \
  -o ferox_target_scan.txt
```
### Рабочий процесс 2: обнаружение внутреннего портала предприятия/Active Directory

Сканирование внутреннего веб-приложения через Burp Suite с аутентификацией и самозаверяющими сертификатами:
```bash
feroxbuster -u https://internal-portal.corp.local \
  -w /usr/share/seclists/Discovery/Web-Content/common.txt \
  -x aspx,ashx,asmx,config,txt \
  -b "ASP.NET_SessionId=k2m9fl3p8..." \
  --burp \
  -d 3
```
### Рабочий процесс 3: быстрое нерекурсивное API и обнаружение конечных точек
```bash
feroxbuster -u https://api.target.com/v1 \
  -w /usr/share/seclists/Discovery/Web-Content/api/api-endpoints.txt \
  -n \
  -s 200,201,204,401,403 \
  -t 100 \
  --json \
  -o api_results.json
```
---

## 📊 Краткая справочная таблица

| Флаг | Короткие | Описание | Пример |
|------|-------|-------------|---------|
| `--url` | `-u` | Целевой URL-адрес для сканирования | `feroxbuster -u https://target.com` |
| `--wordlist` | `-w` | Путь к списку слов | `feroxbuster -w /path/to/list.txt` |
| `--threads` | `-t` | Количество одновременных потоков на каталог | `feroxbuster -t 80` |
| `--scan-limit` | `-L` | Ограничить общее количество одновременных сканирований каталогов | `feroxbuster -L 3` |
| `--depth` | `-d` | Максимальная глубина рекурсии (по умолчанию 4) | `feroxbuster -d 2` |
| `--no-recursion` | `-n` | Отключить рекурсивное сканирование каталогов | `feroxbuster -n` |
| `--extensions` | `-x` | Расширения файлов для добавления | `feroxbuster -x php,html,bak` |
| `--status-codes` | `-s` | Коды состояния для включения | `feroxbuster -s 200 301 302` |
| `--filter-status` | `-C` | Коды состояния, которые следует исключить | `feroxbuster -C 404 500` |
| `--filter-size` | `-S` | Размеры ответа для исключения | `feroxbuster -S 4096` |
| `--filter-words` | `-W` | Количество слов для исключения | `feroxbuster -W 15` |
| `--filter-lines` | `-N` | Количество строк для исключения | `feroxbuster -N 8` |
| `--auto-tune` | Нет | Динамическая регулировка скорости в зависимости от задержки | `feroxbuster --auto-tune` |
| `--auto-bail` | Нет | Прерывание сканирования при возникновении чрезмерных сетевых ошибок | `feroxbuster --auto-bail` |
| `--extract-links`| Нет | Разобрать HTML/JS на наличие новых конечных точек | `feroxbuster --extract-links` |
| `--collect-backups` | Нет | Проверьте варианты резервных копий (`.bak`, `.old`) | `feroxbuster --collect-backups` |
| `--proxy` | `-p` | Отправлять трафик через HTTP-прокси | `feroxbuster -p http://127.0.0.1:8080` |
| `--burp` | Нет | Ярлык для `-p http://127.0.0.1:8080 -k` | `feroxbuster --burp` |
| `--stdin` | Нет | Чтение URL-адресов со стандартного ввода | `cat urls.txt \| feroxbuster --stdin` |
| `--output` | `-o` | Путь к выходному файлу | `feroxbuster -o results.txt` |
| `--json` | Нет | Форматировать вывод как JSON | `feroxbuster --json -o out.json` |

---

## 💡 Советы и рекомендации

1. **Используйте `--auto-tune` для удаленных целей:**
   - Вместо угадывания количества потоков (`-t`), `--auto-tune` активно измеряет целевое время отклика и динамически регулирует или ускоряет работу, избегая случайного отказа сервера в обслуживании.
2. **Объедините `--extract-links` и `--collect-backups`:**
   — Эта комбинация исключительно смертельна при оценке количества ошибок: Feroxbuster извлекает скрытые JS-маршруты, сканирует их и немедленно проверяет, остались ли на сервере резервные копии разработчиков (`.bak`, `.swp`, `~`).
3. **Ограничьте сканирование больших списков слов с помощью `-L`:**
   - При использовании словаря, содержащего более 200 000 слов, неограниченная рекурсия может запускать десятки одновременных потоков. Используйте `-L 2` или `-L 3`, чтобы поддерживать память и пропускную способность в безопасных рабочих пределах.
4. **Используйте файлы состояния для возобновления крупных проверок:**
   - Feroxbuster периодически сохраняет файлы состояния (`.state`). Если сканирование прервалось, немедленно возобновите его с помощью `feroxbuster --resume-from <FILE>.state`.

---

## ⚠️ Юридическая оговорка

> **ВАЖНО:** Feroxbuster создан исключительно для авторизованных оценок безопасности, соревнований CTF и тестов на проникновение, проводимых с письменного разрешения. Сканирование неавторизованных целей может привести к перегрузке сетевой инфраструктуры и строго запрещено законом.

---

## 📚 Ресурсы

- [Репозиторий Feroxbuster на GitHub](https://github.com/epi052/feroxbuster)
- [Официальная документация Feroxbuster](https://epi052.github.io/feroxbuster-docs/)
- [Списки обнаружения SecLists] (https://github.com/danielmiessler/SecLists)