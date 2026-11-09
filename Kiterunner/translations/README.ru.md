<!-- language-home-navigation -->
[🏠 Русская главная страница](../../README.ru.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🪁 Kiterunner — Полная шпаргалка

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ██╗  ██╗██╗████████╗███████╗██████╗ ██╗   ██╗███╗   ██╗███╗   ██╗███████╗██████╗ 
  ██║ ██╔╝██║╚══██╔══╝██╔════╝██╔══██╗██║   ██║████╗  ██║████╗  ██║██╔════╝██╔══██╗
  █████╔╝ ██║   ██║   █████╗  ██████╔╝██║   ██║██╔██╗ ██║██╔██╗ ██║█████╗  ██████╔╝
  ██╔═██╗ ██║   ██║   ██╔══╝  ██╔══██╗██║   ██║██║╚██╗██║██║╚██╗██║██╔══╝  ██╔══██╗
  ██║  ██╗██║   ██║   ███████╗██║  ██║╚██████╔╝██║ ╚████║██║ ╚████║███████╗██║  ██║
  ╚═╝  ╚═╝╚═╝   ╚═╝   ╚══════╝╚═╝  ╚═╝ ╚═════╝ ╚═╝  ╚═══╝╚═╝  ╚═══╝╚══════╝╚═╝  ╚═╝
       Context-Aware API Endpoint & Route Discovery Engine by Assetnote
```
<p выравнивание="центр">
  <img src="https://img.shields.io/badge/Kiterunner-API_Discovery-blue?style=for-the-badge" alt="Kiterunner">
  <img src="https://img.shields.io/badge/Author-Assetnote-orange?style=for-the-badge" alt="Assetnote">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-green?style=for-the-badge" alt="Веб-безопасность и API">
  <img src="https://img.shields.io/badge/License-Apache_2.0-red?style=for-the-badge" alt="Лицензия Apache 2.0">
</p>

<p выравнивание="центр">
  <b>🌐 Высокопроизводительный перебор маршрутов REST и GraphQL API, предназначенный для сопоставления современных архитектур API с использованием контекстных наборов данных.</b>
</p>

---

## 📋 Содержание

- [Обзор и проблема обнаружения API](#-overview--the-api-discovery-problem)
- [Сравнение инструментов: Kiterunner, ffuf и dirsearch](#-tool-comparison-kiterunner-vs-ffuf-vs-dirsearch)
- [Установка](#-установка)
- [Загрузка наборов данных Assetnote Kite](#-downloading-assetnote-kite-datasets)
- [API сканирования (`kr scan`)](#-scanning-apis-kr-scan)
- [Стандартный перебор списка слов (`kr brute`)](#-standard-wordlist-brute-forcing-kr-brute)
- [Заголовки, файлы cookie и аутентификация](#-headers-cookies--аутентификация)
- [Фильтрация и коды состояния](#-filtering--status-codes)
- [Воспроизведение обнаруженных маршрутов (выходные данные `curl`)](#-replaying-discovered-routes-curl-output)
- [Краткая справочная таблица](#-краткая справочная-таблица)
- [Защитные соображения и OPSEC](#-защитные-соображения--opsec)
- [Юридическая оговорка](#-юридическая оговорка)

---

## 🎯 Обзор и проблема обнаружения API

Традиционные инструменты обнаружения веб-контента (`ffuf`, `dirsearch`, `gobuster`) используют плоские словари из одного слова (например, `admin`, `api`, `login`). Хотя это работает для традиционных файловых иерархий, **но не работает с современными API-интерфейсами REST и GraphQL**.

Современные API используют вложенные многоуровневые маршруты, такие как:
`/api/v2/organizations/{org_id}/users/export?format=json`

**Kiterunner**, созданный **Assetnote**, решает эту проблему путем компиляции реальных определений OpenAPI, Swagger, Postman и схемы в компактные двоичные наборы данных (`.kite`). Вместо того, чтобы угадывать случайные слова, Kiterunner реконструирует целые иерархические структуры вызовов API с помощью правильных методов HTTP (`GET`, `POST`, `PUT`, `DELETE`).

---

## ⚖️ Сравнение инструментов: Kiterunner, ffuf и dirsearch

| Особенность | Кайтраннер (`kr`) | ффуф | поиск |
|---------|-------------------|------|-----------|
| **Основная цель** | REST, GraphQL, OpenAPI | Общий веб-фаззинг | Перебор каталогов |
| **Информация об иерархии маршрутов**| ✅ Собственный (файлы `.kite`) | ❌ Плоский список слов | ❌ Плоский список слов |
| **Обмен HTTP-глаголами** | ✅ Тесты GET, POST, PUT, DELETE | ❌ Ручная настройка | ❌ Ограниченная |
| **Разбор Swagger/OpenAPI** | ✅ Прием встроенной схемы | ❌ Нет | ❌ Нет |
| **Скорость и параллелизм** | ⭐⭐⭐⭐⭐ (Go Engine) | ⭐⭐⭐⭐⭐ (Go Engine) | ⭐⭐⭐⭐ (Питон) |

---

## 📥 Установка
```bash
# Download precompiled binary from GitHub Releases
wget https://github.com/assetnote/kiterunner/releases/download/v1.0.2/kiterunner_1.0.2_linux_amd64.tar.gz
tar -xzvf kiterunner_1.0.2_linux_amd64.tar.gz
sudo mv kr /usr/local/bin/

# Verify installation
kr version
```
---

## 📦 Загрузка наборов данных Assetnote Kite

Kiterunner использует готовые наборы данных маршрутов `.kite`, созданные на основе миллионов проанализированных общедоступных API:
```bash
# Create wordlists directory
mkdir -p ~/.kiterunner && cd ~/.kiterunner

# Small dataset (~40MB, 2.5 million API routes - Recommended for fast scans)
wget https://wordlists-cdn.assetnote.io/data/kiterunner/routes-small.kite.tar.gz
tar -xzvf routes-small.kite.tar.gz

# Large dataset (~130MB, 8.5 million API routes - Exhaustive testing)
wget https://wordlists-cdn.assetnote.io/data/kiterunner/routes-large.kite.tar.gz
tar -xzvf routes-large.kite.tar.gz
```
---

## 🚀 API сканирования (`kr scan`)

Команда `scan` использует наборы данных `.kite` для проверки целей с использованием контекстных иерархий маршрутов:
```bash
# Scan a single target using the small dataset
kr scan https://target.local/api -w ~/.kiterunner/routes-small.kite

# Scan multiple hosts from a file with 100 concurrent workers
kr scan targets.txt -w ~/.kiterunner/routes-small.kite -c 100 -j 50

# Scan with maximum verbosity
kr scan https://target.local -w ~/.kiterunner/routes-small.kite -v
```
---

## 🔨 Стандартный перебор списка слов (`kr brute`)

Если вы хотите использовать стандартные списки слов `.txt`, одновременно используя преимущества многометодного API-движка Kiterunner:
```bash
# Brute force standard paths using a text dictionary
kr brute https://target.local -w /usr/share/wordlists/dirb/common.txt

# Brute force with custom API prefixes
kr brute https://target.local/api/v1 -w endpoints.txt
```
---

## 🔑 Заголовки, файлы cookie и аутентификация

Для API часто требуются JWT, токены Bearer или пользовательские заголовки организации:
```bash
# Pass custom Authorization header
kr scan https://target.local/api -w routes-small.kite -H "Authorization: Bearer eyJhbGci..."

# Pass custom API key and Cookie
kr scan https://target.local -w routes-small.kite -H "X-API-Key: secret123" -H "Cookie: session=xyz"
```
---

## 🎯 Фильтрация и коды состояния
```bash
# Exclude standard error codes (404, 403, 502)
kr scan https://target.local -w routes-small.kite -x 404,403,502

# Filter out responses by length / content size
kr scan https://target.local -w routes-small.kite --filter-length 0,324
```
---

## 🔄 Воспроизведение обнаруженных маршрутов (выходные данные `curl`)

Kiterunner может выводить обнаруженные маршруты в виде активных команд `curl`, что позволяет мгновенно воспроизводить их в терминале или Burp Suite:
```bash
# Print reproducible curl command lines for all successful matches
kr scan https://target.local -w routes-small.kite -o json | jq -r '.[] | .curl'
```
---

## ⚡ Краткая справочная таблица

| Цель | Команда |
|-----------|---------|
| **API сканирования с небольшим набором данных** | `kr scan <URL> -w routes-small.kite` |
| **API сканирования с большим набором данных** | `kr scan <URL> -w routes-large.kite` |
| **Сканировать несколько хостов** | `kr scan hosts.txt -w routes-small.kite -c 100` |
| **Список слов для текста методом грубой силы** | `kr brute <URL> -w wordlist.txt` |
| **Аутентификация с помощью токена на предъявителя**| `kr scan <URL> -w routes-small.kite -H "Authorization: Bearer <TOKEN>"` |
| **Исключить коды состояния** | `kr scan <URL> -w routes-small.kite -x 404,403` |
| **Вывод в файл JSON** | `kr scan <URL> -w routes-small.kite -o json -oFile results.json` |

---

## 🛡️ Оборонительные соображения и OPSEC

1. **Огромный объем запросов.** Словарь `routes-large.kite` генерирует миллионы запросов. Убедитесь, что ограничение скорости (`-q 200`) включено при тестировании производственных сред.
2. **Методы изменения состояния.** Kiterunner отправляет запросы `POST`, `PUT` и `DELETE`. В незащищенных тестовых API это может привести к непреднамеренному изменению или удалению записей серверной части.

---

## ⚠️ Юридическая оговорка

> [!ВНИМАНИЕ]
> Kiterunner предназначен только для авторизованного тестирования безопасности API и участия в программе «Bug Bounty». Фаззинг неавторизованных API строго противозаконен.