<!-- language-home-navigation -->
[🏠 Русская главная страница](../../README.ru.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🕷️ CeWL — Полная шпаргалка

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
   ██████╗███████╗██╗    ██╗██╗     
  ██╔════╝██╔════╝██║    ██║██║     
  ██║     █████╗  ██║ █╗ ██║██║     
  ██║     ██╔══╝  ██║███╗██║██║     
  ╚██████╗███████╗╚███╔███╔╝███████╗
   ╚═════╝╚══════╝ ╚══╝╚══╝ ╚══════╝
       Custom Word List Generator by Robin Wood (DigiNinja)
```
<p выравнивание="центр">
  <img src="https://img.shields.io/badge/CeWL-Custom_Wordlist_Generator-blue?style=for-the-badge" alt="CeWL">
  <img src="https://img.shields.io/badge/Author-DigiNinja-orange?style=for-the-badge" alt="DigiNinja">
  <img src="https://img.shields.io/badge/Category-Password_Cracking-green?style=for-the-badge" alt="Взлом пароля">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="Лицензия GPLv2">
</p>

<p выравнивание="центр">
  <b>🌐 Генератор словаря паролей для конкретной цели, который сканирует веб-сайты для сбора специализированной корпоративной терминологии, названий брендов и электронных писем сотрудников.</b>
</p>

---

## 📋 Содержание

- [Обзор и философия целевого пароля](#-overview--the-targeted-password-philosophy)
- [Установка](#-установка)
- [Базовое сканирование веб-сайта](#-basic-website-crawling)
- [Элементы управления глубиной паука и длиной слова] (#-паук-глубина--слово-длина-контроля)
- [Сбор адресов электронной почты (`-e`)](#-harvesting-email-addresses--e)
- [Включая цифры и строчные буквы] (#-включая цифры--строчные буквы)
- [Извлечение метаданных документа (`-a`)](#-document-metadata-extraction--a)
- [Количество слов и частотное профилирование (`-c`)](#-word-count-- Frequency-profiling--c)
- [Объединение CeWL с правилами Hashcat и John](#-chaining-cewl-with-hashcat--john-rules)
- [Краткая справочная таблица](#-краткая справочная-таблица)
- [Защитные соображения и OPSEC](#-защитные-соображения--opsec)
- [Юридическая оговорка](#-юридическая оговорка)

---

## 🎯 Обзор и философия целевого пароля

Общие списки слов, такие как `rockyou.txt`, эффективны для потребительских паролей, но **они часто не соответствуют корпоративной политике паролей**, требующей более 12 символов, специальных символов и сезонной ротации.

Сотрудники обычно основывают свои корпоративные пароли на:
- Названия проектов компании, внутренние кодовые названия и слоганы (`Pegasus2026!`).
- Специфическая отраслевая терминология, технический жаргон или брендинг продукции.
- Названия руководителей, ориентиры и адреса офисов.

**CeWL** (генератор пользовательских списков слов), созданный **Робином Вудом (DigiNinja)**, представляет собой Ruby-паук, который сканирует указанный целевой URL-адрес на определенную глубину, извлекает уникальные слова, соответствующие минимальным требованиям к длине, и создает словарь паролей, адаптированный для взаимодействия.

---

## 📥 Установка
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y cewl

# Arch Linux (AUR)
yay -S cewl

# Ruby Gem
gem install cewl
```
---

## 🚀 Базовое сканирование веб-сайтов
```bash
# Crawl website and output words to console
cewl https://target.local

# Crawl and save to a custom wordlist file
cewl https://target.local -w company_wordlist.txt
```
---

## 🕸️ Регулировка глубины паука и длины слова

Контролируйте, насколько глубоко CeWL следует по ссылкам и какие размеры слов он захватывает:
```bash
# Crawl 2 links deep (-d 2) and enforce minimum word length of 8 characters (-m 8)
cewl https://target.local -d 2 -m 8 -w wordlist_min8.txt

# Aggressive 3-level deep crawl for comprehensive corporate terminology
cewl https://target.local -d 3 -m 7 --no-words -w deep_crawl.txt
```
---

## 📧 Сбор адресов электронной почты (`-e`)

Извлеките корпоративные адреса электронной почты для последующего моделирования фишинга или распыления учетных данных:
```bash
# Extract emails to a separate dedicated file
cewl https://target.local -e --email_file emails.txt -w words.txt
```
---

## 🔡 Включение цифр и строчных букв
```bash
# Include words containing alphanumeric combinations (e.g., product names like V300, X86)
cewl https://target.local --with-numbers -m 6 -w words_with_numbers.txt

# Convert all discovered words to lowercase (great for rule generation)
cewl https://target.local --lowercase -w lowercase_words.txt
```
---

## 📄 Извлечение метаданных документа (`-a`)

Извлеките имена авторов и создателей из загруженных документов PDF и Microsoft Office:
```bash
# Download and analyze document metadata to extract employee usernames
cewl https://target.local -a --meta_file extracted_authors.txt
```
---

## 📊 Количество слов и частотный профиль (`-c`)

Отсортируйте слова по частоте, чтобы определить наиболее распространенные корпоративные модные словечки:
```bash
# Append occurrence count next to each word
cewl https://target.local -c -m 6 -w frequency.txt

# Sort by most frequent words in descending order
sort -k2 -nr frequency.txt | head -n 30
```
---

## ⚡ Объединение CeWL с помощью правил Hashcat и John

Необработанный список слов CeWL содержит корни базового словаря. Чтобы создать надежные пароли, расширьте список слов, используя **правила мутации Hashcat/John the Ripper**:
```bash
# 1. Harvest base words from corporate website
cewl https://target.local -m 5 --lowercase -w base_words.txt

# 2. Mutate words using Hashcat's best64 rule set
hashcat --force base_words.txt -r /usr/share/hashcat/rules/best64.rule --stdout | sort -u > mutated_passwords.txt

# Example mutations produced:
# Acmetool -> Acmetool2026!, !Acmetool123, 4cmet00l, Acmetool#1
```
---

## ⚡ Краткая справочная таблица

| Цель | Команда |
|-----------|---------|
| **Базовое сканирование файла** | `cewl <URL> -w wordlist.txt` |
| **Установить минимальную длину и глубину** | `cewl <URL> -m 8 -d 2 -w wordlist.txt` |
| **Извлечение корпоративных писем**| `cewl <URL> -e --email_file emails.txt` |
| **Извлечение метаданных документа**| `cewl <URL> -a --meta_file authors.txt` |
| **Включить буквенно-цифровые слова**| `cewl <URL> --with-numbers -w wordlist.txt` |
| **Вывод в нижнем регистре** | `cewl <URL> --lowercase -w wordlist.txt` |
| **Подсчет частот слов** | `cewl <URL> -c -w frequencies.txt` |
| **Аутентификация за входом**| `cewl <URL> -a "Cookie: session=xyz"` |

---

## 🛡️ Оборонительные соображения и OPSEC

1. **Журналы доступа к веб-серверу.** CeWL генерирует сотни быстрых HTTP-запросов GET, создавая идентифицируемые журналы доступа пауков. Используйте пользовательские строки User-Agent (`-u "Mozilla/5.0..."`), чтобы гармонировать.
2. **Ловушки для медовых слов.** Синие оборонительные команды могут встраивать невидимые или скрытые HTML-ссылки, ведущие к учетным данным канарейки, чтобы пометить автоматических пауков.

---

## ⚠️ Юридическая оговорка

> [!ВНИМАНИЕ]
> CeWL предназначен только для авторизованных оценок безопасности, тестирования на проникновение и исследования аудита паролей. Скрапинг или просмотр неавторизованных систем строго запрещен.