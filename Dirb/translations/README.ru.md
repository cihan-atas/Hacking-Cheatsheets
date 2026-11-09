<!-- language-home-navigation -->
[🏠 Русская главная страница](../../README.ru.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 📁 DIRB — Полная шпаргалка

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ██████╗ ██╗██████╗ ██████╗ 
  ██╔══██╗██║██╔══██╗██╔══██╗
  ██║  ██║██║██████╔╝██████╔╝
  ██║  ██║██║██╔══██╗██╔══██╗
  ██████╔╝██║██║  ██║██████╔╝
  ╚═════╝ ╚═╝╚═╝  ╚═╝╚═════╝ 
        Web Content Scanner
```
<p выравнивание="центр">
  <img src="https://img.shields.io/badge/DIRB-Content_Scanner-orange?style=for-the-badge" alt="DIRB">
  <img src="https://img.shields.io/badge/Wordlists-Bruteforce-blue?style=for-the-badge" alt="Списки слов">
  <img src="https://img.shields.io/badge/Kali_Linux-Preinstalled-green?style=for-the-badge" alt="Kali Linux">
  <img src="https://img.shields.io/badge/Web-Pentesting-red?style=for-the-badge" alt="Веб-пентестинг">
</p>

<p выравнивание="центр">
  <b>🔍 Классический сканер веб-контента, запускающий атаки на веб-серверы на основе словаря</b>
</p>

---

## 📋 Содержание

- [Введение](#-введение)
- [DIRB против современных альтернатив](#-dirb-vs-modern-alternatives)
- [Установка](#-установка)
- [Списки слов по умолчанию](#-default-wordlists)
- [Основной синтаксис](#-базовый-синтаксис)
- [Общие сценарии сканирования](#-common-scanning-scenarios)
- [Дополнительные параметры и настройка](#-advanced-options--tuning)
- [Охота на уязвимости с помощью встроенных списков](#-vulnerability-hunting-with-built-in-lists)
- [Реальные рабочие процессы](#-реальные-рабочие процессы)
- [Краткая справочная таблица](#-краткая справочная-таблица)
- [Советы и рекомендации](#-tips--best-practices)
- [Юридическая оговорка](#-юридическая оговорка)
- [Ресурсы](#-ресурсы)

---

## 🎯 Введение

**DIRB** (Directory Buster) — это классический, легкий сканер веб-контента на основе URL-адресов, написанный на C. Он запускает атаки на основе словаря против веб-серверов, отыскивая существующие и скрытые веб-объекты (файлы, каталоги, сценарии и конечные точки).

DIRB работает, генерируя HTTP-запросы для каждого слова в списке слов, анализируя коды ответа сервера (200, 301, 302, 403 и т. д.) и определяя, существует ли ресурс.

### Ключевые характеристики

- ✅ **Автоматическое рекурсивное сканирование:** обнаруженные подкаталоги сканируются автоматически по умолчанию.
- ✅ **Встроенные специализированные списки слов:** поставляется с целевыми списками слов для распространенных веб-серверов, CMS и уязвимостей.
- ✅ **Малое потребление ресурсов:** Легкий двоичный файл, который работает без сбоев даже на оборудовании с низкими характеристиками или на старых системах.
- ✅ **Поддержка пользовательских заголовков и прокси:** Простая интеграция с Burp Suite и вышестоящими HTTP-прокси.

---

## ⚖️ DIRB против современных альтернатив

| Особенность | ДИРБ | Гобастер | ффуф | Фероксбастер | поиск |
|---------|------|----------|------|-------------|-----------|
| **Язык** | С | Перейти | Перейти | Ржавчина | Питон |
| **Скорость** | ⭐⭐ (Низкая резьба) | ⭐⭐⭐⭐⭐ (Быстро) | ⭐⭐⭐⭐⭐ (Очень быстро) | ⭐⭐⭐⭐⭐ (Async Rust) | ⭐⭐⭐⭐ (Быстро) |
| **Рекурсия по умолчанию** | ✅ Да (автоматически) | ❌ Нет | ❌ Нет (Необязательно `-recursion`) | ✅ Да (автоматически) | ✅ Да (`-r`) |
| **Включены пользовательские списки слов** | ✅ Да (`/usr/share/dirb/`) | ❌ Нет | ❌ Нет | ❌ Нет | ✅ Да (`db/dicc.txt`) |
| **Фаззинг VHost** | ❌ Нет | ✅ Да | ✅ Да | ❌ Нет | ✅ Да (`--subdirs`) |
| **POST/Фаззинг данных** | ❌ Нет | ❌ Нет | ✅ Да | ❌ Нет | ✅ Да (`-d`) |
| **Загрузка памяти/ЦП** | Очень низкий | Низкий | Низкий | Очень низкий | Умеренный |

---

## 📥 Установка

### Kali Linux и ОС Parrot

DIRB предустановлен в Kali Linux и Parrot OS:
```bash
# Verify installation
dirb

# Update to latest package
sudo apt update && sudo apt install -y dirb
```
### Дебиан/Убунту
```bash
sudo apt update
sudo apt install -y dirb
```
### Компиляция из исходного кода
```bash
# Install build dependencies
sudo apt install -y build-essential libcurl4-openssl-dev

# Download and extract source
wget https://sourceforge.net/projects/dirb/files/dirb/2.22/dirb222.tar.gz
tar -xzvf dirb222.tar.gz
cd dirb222

# Configure permissions and compile
chmod -R 755 conf
chmod 755 configure
./configure
make
sudo make install
```
---

## 📚 Списки слов по умолчанию

При установке в Kali Linux DIRB включает специальные списки слов, расположенные по адресу `/usr/share/dirb/wordlists/`:

| Путь к списку слов | Записи | Цель |
|---------------|---------|---------|
| `/usr/share/dirb/wordlists/common.txt` | ~4614 | Список по умолчанию, используемый, когда список слов не указан |
| `/usr/share/dirb/wordlists/small.txt` | ~959 | Быстрое открытие путей с высокой вероятностью |
| `/usr/share/dirb/wordlists/big.txt` | ~20 469 | Полный поиск по каталогам и файлам |
| `/usr/share/dirb/wordlists/catala.txt` | ~6846 | Каталонский список слов |
| `/usr/share/dirb/wordlists/spanish.txt` | ~11 720 | Испанский список слов |
| `/usr/share/dirb/wordlists/mutations_common.txt` | ~1000 | Распространенные варианты префикса/суффикса |
| `/usr/share/dirb/wordlists/extensions_common.txt` | ~40 | Стандартные веб-расширения (`.php`, `.asp`, `.html` и т. д.) |

### Списки слов встроенных уязвимостей (`/usr/share/dirb/wordlists/vulns/`)

- `apache.txt` — файлы, страницы состояния и известные каталоги, специфичные для Apache.
- `iis.txt` - файлы веб-сервера Microsoft IIS (`web.config`, `aspnet_client` и т. д.)
- `tomcat.txt` — пути администрирования и менеджера Apache Tomcat.
- `cgis.txt` — сценарии и конечные точки общего интерфейса шлюза (CGI).
- `fatwire.txt`, `hpsmh.txt`, `hyperion.txt`, `iplanet.txt`, `netware.txt`, `oracle.txt`

---

## ⌨️ Базовый синтаксис
```bash
dirb <url_base> [<wordlist_file(s)>] [options]
```
### Основные примеры
```bash
# Basic scan using default common.txt wordlist
dirb http://10.10.10.10

# Scan using a specific custom wordlist
dirb https://example.com /usr/share/wordlists/dirb/big.txt

# Scan with custom SecLists directory list
dirb http://target.local /usr/share/seclists/Discovery/Web-Content/directory-list-2.3-medium.txt
```
---

## 🔍 Распространенные сценарии сканирования

### 1. Сканирование с использованием расширений файлов (`-X` и `-x`)

Найдите определенные расширения файлов, прикрепленные к каждому слову в словаре:
```bash
# Test specific extensions on every wordlist entry
dirb http://target.local -X .php,.html,.txt,.bak

# Use an extensions list file
dirb http://target.local -x /usr/share/dirb/wordlists/extensions_common.txt

# Combine directory brute-force with extension brute-force
dirb http://target.local /usr/share/dirb/wordlists/common.txt -X .php,.json,.zip
```
### 2. Управление рекурсией (`-r` и `-R`)

По умолчанию DIRB рекурсивно сканирует каждый обнаруженный каталог. Вы можете контролировать это поведение:
```bash
# Disable recursion completely (scan root level only)
dirb http://target.local -r

# Interactive recursion (DIRB asks before descending into each directory)
dirb http://target.local -R
```
### 3. Сканирование без учета регистра (`-i`)

Полезно при настройке серверов Windows/IIS, где пути к файлам не чувствительны к регистру:
```bash
dirb http://target.local -i
```
### 4. Игнорирование определенных кодов состояния HTTP (`-N`)

Отфильтруйте нежелательные коды ответов (например, циклы перенаправления или запрещенные пути):
```bash
# Ignore 302 redirects and 403 forbidden responses
dirb http://target.local -N 302,403
```
---

## ⚙️ Дополнительные параметры и настройка

### Аутентификация (`-u`)

DIRB поддерживает базовую и дайджест-аутентификацию HTTP:
```bash
# Supply HTTP authentication credentials
dirb http://target.local -u "admin:SecretP@ssw0rd!"

# Combine auth with custom wordlist
dirb http://target.local /usr/share/dirb/wordlists/big.txt -u "pentester:Summer2026"
```
### Пользовательские файлы cookie и токены сеанса (`-c`)

Проверьте аутентифицированные маршруты веб-приложений, предоставив файлы cookie сеанса:
```bash
# Pass session cookie
dirb http://target.local -c "PHPSESSID=d92j4fn82mdf82ncb184; logged_in=true"
```
### Пользовательский агент пользователя и заголовки HTTP (`-a` и `-H`)

Обходите базовые правила WAF или имитируйте легальные браузеры:
```bash
# Set custom User-Agent
dirb http://target.local -a "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36"

# Add custom HTTP Header
dirb http://target.local -H "Authorization: Bearer eyJhbGciOi..."
dirb http://target.local -H "X-Forwarded-For: 127.0.0.1"
```
### Маршрутизация прокси и интеграция с пакетом Burp (`-p` и `-P`)

Отправьте трафик DIRB через восходящий HTTP-прокси, такой как Burp Suite или OWASP ZAP:
```bash
# Route through local Burp Suite proxy
dirb http://target.local -p 127.0.0.1:8080

# Authenticated proxy
dirb http://target.local -p 10.10.10.1:3128 -P "proxyuser:proxypass"
```
### Ограничение скорости и уклонение (`-z`)

Избегайте запуска предупреждений IDS/IPS или DoS-атак уязвимых веб-сервисов, добавляя задержку между запросами (в миллисекундах):
```bash
# Wait 250 milliseconds between each request
dirb http://target.local -z 250

# Slow, stealthy scan (1 second delay)
dirb http://target.local -z 1000
```
### Журнал вывода (`-o` и `-w`)
```bash
# Save results to a file
dirb http://target.local -o /home/zeus/scans/dirb_target.txt

# Do not halt on WARNING messages (useful for uninterrupted scans)
dirb http://target.local -w -o dirb_output.txt
```
---

## 🎯 Охота на уязвимости с помощью встроенных списков

DIRB включает в себя целевые списки в `/usr/share/dirb/wordlists/vulns/` для обнаружения неисправленных серверов и опасных конечных точек:
```bash
# Target Apache server vulnerabilities and status pages
dirb http://target.local /usr/share/dirb/wordlists/vulns/apache.txt

# Target IIS endpoints and misconfigurations
dirb http://target.local /usr/share/dirb/wordlists/vulns/iis.txt

# Target Tomcat management consoles
dirb http://target.local /usr/share/dirb/wordlists/vulns/tomcat.txt

# Search for old CGI scripts and shellshock entrypoints
dirb http://target.local /usr/share/dirb/wordlists/vulns/cgis.txt
```
---

## 🎬 Реальные рабочие процессы

### Рабочий процесс 1: быстрая первоначальная разведка

Когда вы впервые встречаете веб-сервер на порту 80/443:
```bash
# Rapid scan without recursion using small wordlist
dirb http://10.10.10.50 /usr/share/dirb/wordlists/small.txt -r
```
### Рабочий процесс 2: исчерпывающая оценка веб-приложений

Выполните глубокое сканирование в поисках резервных копий конфигурации, исходного кода и конечных точек API:
```bash
dirb https://app.target.local /usr/share/dirb/wordlists/common.txt \
  -X .php,.bak,.sql,.config,.txt \
  -a "Mozilla/5.0" \
  -w \
  -o dirb_app_scan.txt
```
### Рабочий процесс 3: сканирование портала с аутентификацией через Burp Suite
```bash
dirb https://internal.target.local/portal/ /usr/share/dirb/wordlists/big.txt \
  -c "session=s%3A_kJw81..." \
  -p 127.0.0.1:8080 \
  -X .jsp,.do,.action \
  -z 100
```
---

## 📊 Краткая справочная таблица

| Флаг | Аргумент | Описание | Пример |
|------|----------|-------------|---------|
| `-a` | `<user_agent>` | Укажите пользовательскую строку User-Agent | `dirb http://target -a "Mozilla/5.0"` |
| `-c` | `<cookie>` | Установить заголовок HTTP Cookie | `dirb http://target -c "id=123"` |
| `-H` | `<header>` | Добавить пользовательский HTTP-заголовок | `dirb http://target -H "X-API-Key: secret"` |
| `-i` | Нет | Поиск без учета регистра | `dirb http://target -i` |
| `-N` | `<codes>` | Игнорировать ответы, соответствующие коду состояния | `dirb http://target -N 302,403` |
| `-o` | `<output_file>` | Сохранить результаты сканирования в файл | `dirb http://target -o results.txt` |
| `-p` | `<proxy:port>` | Использовать HTTP-прокси | `dirb http://target -p 127.0.0.1:8080` |
| `-P` | `<user:pass>` | Учетные данные для аутентификации прокси | `dirb http://target -p proxy:8080 -P u:p` |
| `-r` | Нет | Не ищите рекурсивно | `dirb http://target -r` |
| `-R` | Нет | Интерактивный рекурсивный поиск | `dirb http://target -R` |
| `-u` | `<user:pass>` | Базовая/дайджест-аутентификация HTTP | `dirb http://target -u "admin:1234"` |
| `-w` | Нет | Не останавливать сканирование ПРЕДУПРЕЖДАЮЩИХ сообщений | `dirb http://target -w` |
| `-X` | `<ext_list>` | Добавить расширения, разделенные запятыми | `dirb http://target -X .php,.html` |
| `-x` | `<ext_file>` | Добавить расширения из файла | `dirb http://target -x ext.txt` |
| `-z` | `<millisecs>` | Добавить задержку между запросами | `dirb http://target -z 250` |

---

## 💡 Советы и рекомендации

1. **Остерегайтесь ответов с подстановочными знаками 200/302:**
   - Некоторые веб-приложения возвращают HTTP 200 или 302 для каждого запроса (пользовательский обработчик 404). DIRB сообщит о каждой найденной записи. Используйте `-N 302` или переключитесь на `ffuf`/`feroxbuster` для расширенной фильтрации по размеру/слову.
2. **Объедините с `-r` для больших словарей:**
   - При использовании `directory-list-2.3-medium.txt` (220 тыс. слов) всегда отключайте рекурсию с помощью `-r`. В противном случае DIRB может потребоваться несколько дней для сканирования глубоко вложенных каталогов.
3. **Всегда проверяйте расширения:**
   - Современные серверные программы часто предоставляют конфиденциальные файлы, такие как `.env`, `.git`, `.bak`, `.conf` или `.sql`. Всегда проверяйте общие расширения с помощью `-X`.
4. **Используйте `-w` для автоматического сканирования:**
   - По умолчанию DIRB может приостановить работу, если обнаружит предупреждения SSL или сети. Прохождение `-w` гарантирует завершение тестов без ручного вмешательства.

---

## ⚠️ Юридическая оговорка

> **ВАЖНО:** DIRB предназначен для аудита безопасности, тестирования на проникновение и этических исследований в системах, которыми вы владеете или имеете явное документированное разрешение на тестирование. Несанкционированные словарные атаки на веб-серверы могут привести к сбоям в работе служб и являются незаконными в соответствии с законодательством о компьютерных преступлениях во всем мире.

---

## 📚 Ресурсы

- [Проект DIRB SourceForge](https://sourceforge.net/projects/dirb/)
- [Документация пакета Kali Linux DIRB] (https://www.kali.org/tools/dirb/)
- [SecLists — списки слов для обнаружения веб-контента] (https://github.com/danielmiessler/SecLists)