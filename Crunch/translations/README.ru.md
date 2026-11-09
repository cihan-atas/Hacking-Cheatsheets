<!-- language-home-navigation -->
[🏠 Русская главная страница](../../README.ru.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🔢 Crunch — полная шпаргалка

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
   ██████╗██████╗ ██╗   ██╗███╗   ██╗ ██████╗██╗  ██╗
  ██╔════╝██╔══██╗██║   ██║████╗  ██║██╔════╝██║  ██║
  ██║     ██████╔╝██║   ██║██╔██╗ ██║██║     ███████║
  ██║     ██╔══██╗██║   ██║██║╚██╗██║██║     ██╔══██║
  ╚██████╗██║  ██║╚██████╔╝██║ ╚████║╚██████╗██║  ██║
   ╚═════╝╚═╝  ╚═╝ ╚═════╝ ╚═╝  ╚═══╝ ╚═════╝╚═╝  ╚═╝
       High-Speed Pattern-Based Wordlist & Character Matrix Generator
```
<p выравнивание="центр">
  <img src="https://img.shields.io/badge/Crunch-Wordlist_Generator-blue?style=for-the-badge" alt="Crunch">
  <img src="https://img.shields.io/badge/Language-C-orange?style=for-the-badge" alt="Язык C">
  <img src="https://img.shields.io/badge/Category-Password_Cracking-green?style=for-the-badge" alt="Взлом пароля">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="Лицензия GPLv2">
</p>

<p выравнивание="центр">
  <b>🌐 Генератор списков слов, который генерирует все возможные комбинации наборов символов с использованием произвольной длины, шаблонов и символов.</b>
</p>

---

## 📋 Содержание

- [Обзор и механизм перестановок](#-overview--the-permutation-engine)
- [Установка](#-установка)
- [Основной синтаксис и наборы символов](#-basic-syntax--character-sets)
- [Соответствие шаблону и заполнители (`-t`)](#-pattern-matching--placeholders--t)
- [Использование системных кодировок (`charset.lst`)](#-using-system-charsets-charsetlst)
- [Ограничение дублирования символов (`-d`)](#-ограничение дублирования символов--d)
- [Разделение списков слов на фрагменты (`-b` и `-c`)](#-разбиение-списков-слов на фрагменты--b---c)
- [Прямое подключение к инструментам атаки (нулевое дисковое пространство)](#-piping-directly-into-attack-tools-zero-disk-space)
- [Краткая справочная таблица](#-краткая справочная-таблица)
- [Защитные соображения и предупреждения о расчетах] (#-защитные соображения--расчеты-предупреждения)
- [Юридическая оговорка](#-юридическая оговорка)

---

## 🎯 Обзор и механизм перестановок

При атаке на известные шаблоны паролей (например, корпоративные PIN-коды, номера телефонов или пароли, соответствующие стандартным правилам сложности, например `Word2026!`), стандартные статические словари, такие как `rockyou.txt`, либо не достигают цели, либо неэффективны.

**Crunch**, написанный на языке C **bofheaded**, представляет собой невероятно быстрый генератор списков слов. Он математически рассчитывает перестановки, позволяя вам определять точные позиции символов, длину, пулы символов и ограничения дублирования.

---

## 📥 Установка
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y crunch

# Arch Linux (AUR)
yay -S crunch
```
---

## 🚀 Базовый синтаксис и наборы символов

Основной синтаксис требует минимальной длины, максимальной длины и, возможно, алфавита:
```bash
# crunch <min> <max> [characterset] [options]

# Generate all 4-digit numeric PINs (0000 - 9999)
crunch 4 4 0123456789 -o 4digit_pins.txt

# Generate 5-character words using specific custom letters
crunch 5 5 abcdef -o custom_words.txt

# Generate lowercase alphabet combinations from length 3 to 4
crunch 3 4 abcdefghijklmnopqrstuvwxyz -o alpha_3_4.txt
```
---

## 🎭 Сопоставление с образцом и заполнители (`-t`)

Флаг `-t` позволяет исправлять известные символы и фаззить неизвестные слоты с помощью специальных заполнителей:

| Заполнитель | Тип персонажа |
|-------------|----------------|
| `@` | Строчные буквы (`a-z`) |
| `,` | Прописные буквы (`A-Z`) |
| `%` | Числа (`0-9`) |
| `^` | Специальные символы/пунктуация (`!@#$%^&*...`) |

### Практические примеры шаблонов:
```bash
# Generate Turkish phone numbers (e.g., 0532XXXXXXX)
crunch 11 11 -t 0532%%%%%%% -o phone_numbers.txt

# Generate passwords conforming to: Capital letter + 4 lowercase + 2 digits + 1 symbol
# e.g., Admin24! or Super99#
crunch 8 8 -t ,@@@@%%^ -o policy_passwords.txt

# Target known prefix: Spring2026! or Spring2026#
crunch 11 11 -t Spring2026^ -o spring_passwords.txt
```
---

## 📚 Использование системных кодировок (`charset.lst`)

Crunch объединяет предварительно настроенные матрицы символов в `/usr/share/crunch/charset.lst`:
```bash
# Use pre-defined alphanumeric set (mixalpha-numeric)
crunch 6 6 -f /usr/share/crunch/charset.lst mixalpha-numeric -o out.txt

# Use hexadecimal lowercase characters (0-9, a-f)
crunch 8 8 -f /usr/share/crunch/charset.lst hex-lower -o hex_keys.txt
```
---

## 🚫 Ограничение дублирования символов (`-d`)

Установите ограничения на количество последовательных повторяющихся символов, чтобы отсеять маловероятные пароли:
```bash
# Generate 8-character lowercase words with at most 2 consecutive identical letters
# (e.g., prevents 'aaabbb' while allowing 'aab')
crunch 8 8 abcdefghijklmnopqrstuvwxyz -d 2@ -o deduped.txt

# Limit consecutive numbers to at most 1 (prevents '11' or '22')
crunch 6 6 -t %%%%%% -d 1% -o no_repeated_digits.txt
```
---

## 📦 Разделение списков слов на части (`-b` и `-c`)

Предотвратите использование дискового пространства файлами размером в несколько терабайт, разделив их на управляемые тома:
```bash
# Split wordlist into 500MB chunks
crunch 8 8 -f /usr/share/crunch/charset.lst mixalpha-numeric -b 500mb -o START

# Split by line count (every 1,000,000 lines)
crunch 8 8 0123456789 -c 1000000 -o START
```
---

## ⚡ Прямое подключение к инструментам атаки (нулевое дисковое пространство)

Никогда не записывайте на диск гигантские списки слов, если вы можете передавать их перестановки прямо в инструменты для взлома:
```bash
# Stream 8-digit numeric PINs straight into Aircrack-ng
crunch 8 8 0123456789 | aircrack-ng -w - -b 00:11:22:33:44:55 capture.cap

# Stream policy passwords directly into Hydra SSH brute-force
crunch 8 8 -t Admin%%^ | hydra -l admin -P - 10.10.10.50 ssh

# Stream into John the Ripper
crunch 6 6 0123456789 | john --stdin --format=raw-md5 hashes.txt
```
---

## ⚡ Краткая справочная таблица

| Цель | Команда |
|-----------|---------|
| **Цифровые PIN-коды (4-значные)** | `crunch 4 4 0123456789 -o pins.txt` |
| **Шаблон: заглавная буква + слово + цифры**| `crunch 8 8 -t ,@@@@%%^ -o passwords.txt` |
| **Шаблон: конкретный префикс** | `crunch 10 10 -t Company%%% -o wordlist.txt` |
| **Использовать файл кодировки** | `crunch 6 6 -f charset.lst mixalpha-numeric -o out.txt` |
| **Ограничить количество последовательных идентичных**| `crunch 8 8 -t @@@@@@@@ -d 2@ -o out.txt` |
| **Разделено на файлы по 1 ГБ** | `crunch 8 8 <CHARS> -b 1gb -o START` |
| **Переход к Aircrack-ng** | `crunch 8 8 0123456789 \| aircrack-ng -w - target.cap` |
| **Труба к Гидре** | `crunch 7 7 -t User%%% \| hydra -l user -P - <IP> ssh` |

---

## 🛡️ Соображения по поводу защиты и предупреждения о расчетах

> [!ВНИМАНИЕ]
> **Взрывы перестановок:** Всегда проверяйте прогнозируемый размер вывода Crunch перед подтверждением создания диска. Неограниченный 8-значный буквенно-цифровой список слов создает **более 200 терабайт** текстовых данных! Вывод трубы по возможности.

---

## ⚠️ Юридическая оговорка

> [!ВНИМАНИЕ]
> Crunch предназначен только для авторизованного восстановления паролей, тестирования на проникновение и аудита безопасности. Подбор паролей в неавторизованных системах строго противозаконен.