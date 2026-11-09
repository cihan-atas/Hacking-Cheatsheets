<!-- language-home-navigation -->
[🏠 Русская главная страница](../../README.ru.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🌐 WhatWeb — полная шпаргалка

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ██╗    ██╗██╗  ██╗ █████╗ ████████╗██╗    ██╗███████╗██████╗ 
  ██║    ██║██║  ██║██╔══██╗╚══██╔══╝██║    ██║██╔════╝██╔══██╗
  ██║ █╗ ██║███████║███████║   ██║   ██║ █╗ ██║█████╗  ██████╔╝
  ██║███╗██║██╔══██║██╔══██║   ██║   ██║███╗██║██╔══╝  ██╔══██╗
  ╚███╔███╔╝██║  ██║██║  ██║   ██║   ╚███╔███╔╝███████╗██████╔╝
   ╚══╝╚══╝ ╚═╝  ╚═╝╚═╝  ╚═╝   ╚═╝    ╚══╝╚══╝ ╚══════╝╚═════╝ 
       Next Generation Web Scanner & Technology Fingerprinter
```
<p выравнивание="центр">
  <img src="https://img.shields.io/badge/WhatWeb-Web_Fingerprinter-blue?style=for-the-badge" alt="WhatWeb">
  <img src="https://img.shields.io/badge/Author-Andrew_Horton-orange?style=for-the-badge" alt="urbanadventurer">
  <img src="https://img.shields.io/badge/Category-Recon_&_Scanning-green?style=for-the-badge" alt="Разведка и сканирование">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="Лицензия GPLv2">
</p>

<p выравнивание="центр">
  <b>🌐 Веб-сканер нового поколения, предназначенный для идентификации систем управления контентом (CMS), платформ для блогов, библиотек JavaScript и серверных технологий</b>
</p>

---

## 📋 Содержание

- [Обзор и технология идентификации](#-overview--technology-fingerprinting)
- [Установка](#-установка)
- [Базовый отпечаток веб-сайта](#-basic-website-fingerprinting)
- [Объяснение уровней агрессии (`-a`)](#-объяснение-уровней-агрессии--a)
  - [Скрытный пассивный режим (`-a 1`)](#стелси-пассивный-режим --a-1)
  - [Агрессивный режим (`-a 3`)](#aggressive-mode--a-3)
  - [Тяжелый/грубый режим (`-a 4`)](#heavy--грубый-режим--a-4)
- [Сканирование подсетей и целевых файлов](#-scanning-subnets--target-files)
- [Система плагинов и выборочный аудит](#-plugin-system--selective-auditing)
- [Журнал вывода и экспорт отчетов] (#-output-logging--report-exporting)
- [Краткая справочная таблица](#-краткая справочная-таблица)
- [Защитные соображения и OPSEC](#-защитные-соображения--opsec)
- [Юридическая оговорка](#-юридическая оговорка)

---

## 🎯 Обзор и технология снятия отпечатков пальцев

Прежде чем запускать эксплойты или фаззеры грубой силы против веб-приложения, необходимо точно определить его технологический стек.

Созданный **Эндрю Хортоном (городским искателем приключений)**, **WhatWeb** распознает более 1800 различных веб-технологий:
- **Системы управления контентом (CMS):** WordPress, Drupal, Joomla, Ghost, Magento.
- **Веб-серверы и прокси:** Apache, Nginx, Microsoft-IIS, Cloudflare, Envoy.
- **Бэкэнд-фреймворки:** Laravel, Django, Ruby on Rails, ASP.NET, Spring Boot.
- **Внешние библиотеки:** React, Vue.js, Angular, jQuery, Bootstrap.
- **Встроенные устройства:** веб-камеры, маршрутизаторы, принтеры, интерфейсы SCADA.

---

## 📥 Установка
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y whatweb

# Arch Linux
sudo pacman -S whatweb

# macOS (Homebrew)
brew install whatweb
```
---

## 🚀 Базовый отпечаток веб-сайта
```bash
# Scan a single domain
whatweb https://target.local

# Brief one-line output format
whatweb --color=never -q https://target.local

# Verbose detailed report showing evidence matches
whatweb -v https://target.local
```
---

## ⚡ Объяснение уровней агрессии (`-a`)

WhatWeb балансирует скрытность и глубину, используя четыре уровня агрессии:

### Скрытый пассивный режим (`-a 1`)
Отправляет один HTTP-запрос GET на предоставленный URL-адрес и анализирует возвращенные заголовки ответов, файлы cookie и теги HTML, не запрашивая дополнительные страницы:
```bash
whatweb -a 1 https://target.local
```
### Агрессивный режим (`-a 3`)
Рекомендуется для тестов на проникновение. Следит за перенаправлениями и запрашивает общие файлы плагинов (например, `wp-login.php`, `robots.txt`, хеши значков):
```bash
whatweb -a 3 https://target.local
```
### Тяжелый/грубый режим (`-a 4`)
Пытается угадать тысячи плагинов и известные URL-адреса отпечатков пальцев:
```bash
whatweb -a 4 https://target.local
```
---

## 🌐 Сканирование подсетей и целевых файлов
```bash
# Scan an entire CIDR network block
whatweb 192.168.1.0/24

# Scan multiple URLs listed in a text file
whatweb -i targets.txt

# Pipe live web endpoints from httpx into WhatWeb
cat live_hosts.txt | whatweb --no-errors
```
---

## 🧩 Система плагинов и выборочный аудит

WhatWeb имеет богатую архитектуру плагинов:
```bash
# List all 1,800+ available plugins
whatweb -l

# Search for specific plugins (e.g., WordPress or Apache)
whatweb --grep wordpress

# Run ONLY a specific plugin against a target
whatweb --plugins="apache,php,wordpress" https://target.local
```
---

## 📊 Регистрация выходных данных и экспорт отчетов
```bash
# Export results to clean JSON format
whatweb https://target.local --log-json results.json

# Export to XML format
whatweb https://target.local --log-xml results.xml

# Export to compact CSV format
whatweb -i targets.txt --log-brief results.txt
```
---

## ⚡ Краткая справочная таблица

| Цель | Команда |
|-----------|---------|
| **Стандартное сканирование** | `whatweb <URL>` |
| **Подробный подробный отчет** | `whatweb -v <URL>` |
| **Скрытое пассивное сканирование** | `whatweb -a 1 <URL>` |
| **Агрессивное глубокое сканирование** | `whatweb -a 3 <URL>` |
| **Сканировать подсеть (CIDR)** | `whatweb <SUBNET>/24` |
| **Сканировать цели из файла** | `whatweb -i targets.txt` |
| **Экспорт в JSON** | `whatweb <URL> --log-json report.json` |
| **Фильтр по конкретным плагинам**| `whatweb --plugins="wordpress,nginx" <URL>` |

---

## 🛡️ Оборонительные соображения и OPSEC

1. **Маскировка заголовка сервера.** Удалите или рандомизируйте заголовки HTTP-ответов `Server` и `X-Powered-By` в Apache/Nginx, чтобы избежать пассивного снятия отпечатков пальцев с баннера.
2. **Маскировка хэша Favicon:** WhatWeb использует хэши MD5 `/favicon.ico` для идентификации веб-платформ. Настройте значки фреймворка по умолчанию.

---

## ⚠️ Юридическая оговорка

> [!ВНИМАНИЕ]
> WhatWeb предназначен только для авторизованной разведки, аудита безопасности и инвентаризации системы. Сканирование неавторизованных сетей или веб-приложений без разрешения может привести к нарушению действующего законодательства.