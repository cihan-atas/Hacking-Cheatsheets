<!-- language-home-navigation -->
[🏠 Русская главная страница](../../README.ru.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# Шпаргалка по Шерлоку

[Sherlock](https://github.com/sherlock-project/sherlock) — это инструмент OSINT-разведки, который отслеживает учетные записи социальных сетей по имени пользователя на более чем 400 социальных платформах, форумах, сайтах хостинга кода и онлайн-сообществах одновременно.

---

## Содержание

- [Ключевые возможности](#key-capabilities)
- [Установка](#установка)
- [Поиск одного и нескольких имен пользователей](#single--multiple-username-search)
- [Фильтрация сайтов и платформ](#filtering-sites--platforms)
- [Прокси и маршрутизация Tor](#proxy--tor-routing)
- [Настройка таймаута и потоков](#timeout--threading-tuning)
- [Форматы экспорта (CSV, XLSX, JSON)](#export-formats-csv-xlsx-json)
- [Интеграция автоматизации и OSINT-конвейера](#automation--osint-pipeline-integration)

---

## Ключевые возможности

- **более 400 платформ:** запросы GitHub, Twitter/X, Instagram, Reddit, TikTok, LinkedIn, Telegram, Discord, Steam, Spotify, Docker Hub, Pastebin и сотни других.
- **Асинхронный параллелизм.** Высокоскоростное параллельное сканирование с использованием асинхронных запросов Python.
- **Точное обнаружение:** обнаруживает коды состояния HTTP, сообщения об ошибках и перенаправления, чтобы избежать ложных срабатываний.
- **Поддержка Tor и SOCKS5:** защищает анонимность следователей и обходит ограничения скорости.

---

## Установка
```bash
# Recommended: Install via pipx
pipx install sherlock-project

# Or install from GitHub repository
git clone https://github.com/sherlock-project/sherlock.git
cd sherlock
python3 -m pip install -r requirements.txt

# Debian / Kali Linux repository
sudo apt-get update && sudo apt-get install -y sherlock

# Verify command
sherlock --version
```
---

## Поиск одного и нескольких имен пользователей

### 1. Базовый поиск имени пользователя
Найдите один целевой дескриптор на всех более чем 400 сайтах:
```bash
sherlock target_user
```
### 2. Поиск по нескольким именам пользователей одновременно
```bash
sherlock target_user1 target_user2 alias_dev
```
### 3. Печатать только найденные учетные записи (подавлять сообщения «Не найдено»/ошибки)
```bash
sherlock --print-found target_user
```
---

## Фильтрация сайтов и платформ

Ограничьте поиск конкретными платформами, чтобы сэкономить время или сосредоточиться на определенных категориях аналитики:

### 1. Запрашивайте только определенные сайты
```bash
sherlock --site github --site twitter --site reddit --site gitlab target_user
```
### 2. Поиск без конкретных платформ (игнорировать ложноположительные тяжелые сайты)
```bash
sherlock target_user --site-list targets.txt
```
---

## Прокси и маршрутизация Tor

Чтобы предотвратить блокировку IP-адресов, ограничение скорости и обеспечить операционную безопасность:

### 1. Маршрут через сеть Tor
Автоматически маршрутизировать запросы через локальный прокси-сервер Tor SOCKS (`127.0.0.1:9050`):
```bash
sherlock --tor target_user
```
### 2. Пользовательский HTTP/SOCKS5-прокси
```bash
# SOCKS5 proxy
sherlock --proxy socks5://127.0.0.1:1080 target_user

# Authenticated HTTP proxy
sherlock --proxy http://user:pass@proxy.corp.internal:8080 target_user
```
---

## Тайм-аут и настройка потоков

Скорость запроса управления и допуски на задержку:
```bash
# Set HTTP timeout to 5 seconds per site (default is usually 60s)
sherlock target_user --timeout 5

# Add delay between requests to avoid rate limits
sherlock target_user --delay 1
```
---

## Форматы экспорта (CSV, XLSX, JSON)

Сохраняйте открытия для расследований, сбора доказательств или составления отчетов:

### 1. Сохраните непосредственно в файл CSV.
```bash
sherlock --csv --output /tmp/sherlock_results target_user
```
### 2. Сохранение вывода в определенный каталог
```bash
sherlock --folderoutput /tmp/recon_data/ target_user
```
Генерирует `/tmp/recon_data/target_user.txt`, содержащий полные URL-адреса подтвержденных профилей.

### 3. Сохранить как таблицу Excel (XLSX)
```bash
sherlock --xlsx target_user
```
---

## Интеграция автоматизации и OSINT-конвейера

Присоедините Шерлока к сценарию автоматического следователя:
```bash
#!/usr/bin/env bash
TARGET="$1"
OUTDIR="/tmp/osint_${TARGET}"
mkdir -p "$OUTDIR"

echo "[*] Scanning for username: $TARGET"
sherlock --print-found --timeout 10 --folderoutput "$OUTDIR" "$TARGET"

if [ -f "$OUTDIR/${TARGET}.txt" ]; then
    echo "[+] Found $(wc -l < "$OUTDIR/${TARGET}.txt") accounts:"
    cat "$OUTDIR/${TARGET}.txt"
fi
```