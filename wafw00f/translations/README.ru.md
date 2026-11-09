<!-- language-home-navigation -->
[🏠 Русская главная страница](../../README.ru.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# wafw00f Шпаргалка

[wafw00f](https://github.com/EnableSecurity/wafw00f) — это инструмент для снятия отпечатков и обнаружения брандмауэра веб-приложений (WAF) с открытым исходным кодом. Он идентифицирует и фиксирует более 150 коммерческих решений безопасности WAF с открытым исходным кодом, защищающих целевые веб-сайты, отправляя обычные и провокационные HTTP-запросы и анализируя поведенческие ответы, заголовки и файлы cookie.

---

## Содержание

- [Почему WAF отпечатков пальцев?](#why-fingerprint-wafs)
- [Установка](#установка)
- [Базовое и целевое сканирование](#basic--target-scanning)
- [Исчерпывающее обнаружение нескольких WAF (`-a`)](#exhaustive-multi-waf-detection--a)
- [Массовое сканирование целей из файла (`-i`)](#bulk-target-scanning-from-file--i)
- [Прокси и маршрутизация Tor (`-p`)](#proxy--tor-routing--p)
- [Форматы экспорта (JSON, CSV, текст)](#export-formats-json-csv-text)
- [Список поддерживаемых подписей WAF (`-l`)](#list-supported-waf-signatures--l)
- [Интеграция Bug Bounty и тестирования на проникновение](#bug-bounty--penetration-testing-integration)

---

## Зачем нужны WAF отпечатков пальцев?

Запуск активных веб-атак (SQL-инъекция, XSS, SSRF) против защищенной цели без предварительной разведки часто приводит к мгновенным банам IP или ложноотрицательным результатам уязвимости. Определение точного WAF позволяет:
- **Индивидуальное уклонение:** выбор методов обхода, зависящих от поставщика (например, нормализация Unicode для ModSecurity, кодирование передачи фрагментов для Cloudflare).
- **Охота за исходным IP-адресом.** Если обнаружены Cloudflare или Akamai, перед тестированием необходимо выполнить поворот, чтобы найти исходный IP-адрес серверной части.
- **Узнавание о пороговых значениях правил:** Настройка одновременной работы сканера и ограничений скорости ниже пороговых значений блокировки.

---

## Установка
```bash
# Debian / Ubuntu / Kali Linux
sudo apt-get update && sudo apt-get install -y wafw00f

# Recommended: Install via pipx
pipx install wafw00f

# Via standard Python pip
python3 -m pip install --user wafw00f

# Verify installation
wafw00f --version
```
---

## Базовое и целевое сканирование

### 1. Сканирование одного домена
```bash
wafw00f https://example.com
```
*wafw00f отправляет базовые HTTP-запросы, за которыми следуют адаптированные вредоносные шаблоны для активации фильтров безопасности, идентификации сигнатур в заголовках, кодах состояния и страницах ошибок.*

### 2. Режим подробного вывода (`-v` / `-vv`)
Проверьте конкретные совпадающие заголовки, коды состояния HTTP и эвристики:
```bash
wafw00f https://example.com -v
```
### 3. Отключить подписку на перенаправление (`-r`)
Запретите wafw00f следовать перенаправлениям 301/302, чтобы отделить пограничные обратные прокси от серверных приложений:
```bash
wafw00f https://example.com -r
```
---

## Полное обнаружение нескольких WAF (`-a`)

По умолчанию wafw00f прекращает сканирование, как только будет подтверждено первое совпадение с WAF. Используйте `-a` для проверки подписей всех известных поставщиков (это важно, когда приложение находится за многоуровневой защитой, например Cloudflare CDN + AWS WAF):
```bash
wafw00f https://example.com -a
```
---

## Массовое сканирование целей из файла (`-i`)

Сканируйте весь список поддоменов или целевых ресурсов, собранный из `subfinder` или `amass`:
```bash
wafw00f -i /path/to/domains.txt -o /tmp/waf_results.txt
```
---

## Прокси и маршрутизация Tor (`-p`)

Направляйте зондирующий трафик через прокси, чтобы предотвратить занесение IP-адресов в черный список или проверять запросы в Burp Suite:

### 1. Маршрутизация через прокси-сервер Burp Suite
```bash
wafw00f https://example.com -p http://127.0.0.1:8080
```
### 2. Маршрутизация через локальный прокси-сервер Tor SOCKS5
```bash
wafw00f https://example.com -p socks5://127.0.0.1:9050
```
---

## Форматы экспорта (JSON, CSV, текст)

Экспортируйте результаты для автоматизированных отчетов или последующих инструментов разведки:

### 1. Экспорт в формат JSON
```bash
wafw00f https://example.com -o results.json --format json
```
### 2. Экспорт в таблицу CSV
```bash
wafw00f -i targets.txt -o results.csv --format csv
```
---

## Список поддерживаемых подписей WAF (`-l`)

Просмотрите все более 150 обнаруживаемых продуктов WAF:
```bash
wafw00f -l
```
Ключевые поддерживаемые продукты включают:
– **Поставщики облачных услуг:** Cloudflare, AWS WAF, Akamai, брандмауэр веб-приложений Microsoft Azure, Google Cloud Armor.
- **Корпоративные устройства:** F5 BIG-IP ASM, Imperva SecureSphere, Barracuda, Fortinet FortiWeb, Citrix NetScaler.
- **Открытый исходный код/программное обеспечение:** ModSecurity, NAXSI, Shadow Daemon, OpenResty.

---

## Интеграция Bug Bounty и тестирования на проникновение

Интегрируйте wafw00f в автоматизированный конвейер веб-атак:
```bash
#!/usr/bin/env bash
TARGET="https://app.target.corp"

echo "[*] Detecting WAF for $TARGET..."
WAF=$(wafw00f "$TARGET" -o - --format json | jq -r '.[0].detected')

if [ "$WAF" == "true" ]; then
    WAF_NAME=$(wafw00f "$TARGET" -o - --format json | jq -r '.[0].firewall')
    echo "[!] Active WAF detected: $WAF_NAME"
    echo "[+] Adjusting sqlmap to use vendor-specific tamper scripts..."
    if [[ "$WAF_NAME" =~ "Cloudflare" ]]; then
        sqlmap -u "$TARGET/item?id=1" --tamper=between,randomcase,space2comment
    fi
else
    echo "[+] No active WAF detected. Proceeding with standard scanner."
    sqlmap -u "$TARGET/item?id=1" --batch
fi
```