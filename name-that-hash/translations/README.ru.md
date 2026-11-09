<!-- language-home-navigation -->
[🏠 Русская главная страница](../../README.ru.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# Шпаргалка Name-That-Hash (nth)

[Name-That-Hash](https://github.com/HashPals/Name-That-Hash) (`nth`) — это современный, сверхбыстрый механизм идентификации хэшей, который точно идентифицирует неизвестные хеши, хеши, встроенные в текст или файлы, и выводит прямые режимы Hashcat (`-m`) и John the Ripper (`--format`). Он автоматически декодирует вложенные кодировки, такие как Base64 и Hex.

---

## Содержание

- [Основные функции](#key-features)
- [Установка](#установка)
- [Идентификация хеша CLI](#cli-hash-identification)
- [Мультихэш и обработка файлов](#multi-hash--file-processing)
- [Вывод формата Hashcat и John the Ripper](#hashcat--john-the-ripper-format-output)
- [Автодекодирование и фильтры популярности](#auto-decode--popularity-filters)
- [Интеграция с Cracking Pipelines](#integration-with-cracking-pipelines)

---

## Ключевые особенности

- **более 300 типов хэшей:** охватывают современные алгоритмы, форматы Active Directory, хэши баз данных, подписи CMS и экзотические шифры.
- **Сопоставление флагов Hashcat и John:** Сразу отображаются значения `-m <mode>` и `--format=<format>`.
- **Автодекодирование:** перед вычислением типа хеша удаляется кодировка Base64, Hex и URL.
- **Подстрока/текстовый поиск:** находит хэши, спрятанные в журналах, дампах памяти или файлах конфигурации.
- **Сортировка по популярности:** результаты ранжируются по вероятности использования в реальном мире.

---

## Установка
```bash
# Via pipx (recommended for isolated CLI tools)
pipx install name-that-hash

# Via standard Python pip
python3 -m pip install --user name-that-hash

# Kali Linux / Debian repository
sudo apt-get update && sudo apt-get install -y python3-name-that-hash

# Verify installation
nth --version
```
---

## Идентификация хеша CLI

### 1. Определите одиночный хэш
```bash
nth -t "5d41402abc4b2a76b9719d911017c592"
```
### 2. Непосредственное определение режимов форматирования Hashcat и John
Отображать только те типы хэшей, которые поддерживают режимы взлома Hashcat или John:
```bash
nth -t "b8c37e33defde51cf91e1e03e51657da" --hashcat --john
```
### 3. Отображение подробной информации (описание, скорость, примеры)
```bash
nth -t "$6$rounds=5000$usesomesalt$..." -v
```
---

## Мультихэш и обработка файлов

### 1. Идентификация хэшей внутри файла (построчно)
```bash
nth -f /path/to/hashes.txt
```
### 2. Фильтруйте результаты только по самым популярным хешам
Уменьшите количество ложных срабатываний от непонятных типов хэшей, отфильтровав по популярности:
```bash
# Level 1: Most popular hashes only (MD5, SHA1, SHA256, NTLM, bcrypt, etc.)
nth -f hashes.txt --popularity 1
```
### 3. Поиск и извлечение хешей, встроенных в неструктурированный текст
Сканируйте необработанные строки памяти, файлы журналов или исходный код на наличие хэшей:
```bash
nth --text "Found hash in config: 098f6bcd4621d373cade4e832627b4f6 on port 8080"
```
---

## Вывод формата Hashcat и John the Ripper

`nth` упрощает передачу хэшей взломщикам без запоминания номеров режимов:

| Пример формата хеша | Обнаруженный алгоритм | Режим Hashcat (`-m`) | Джон Формат (`--format`) |
|---|---|---|---|
| `e80b5017098950fc58aad83c8c14978e` | MD5 | `0` | `raw-md5` |
| `b4b9b02e6f09a9bd760f388b67351e2b` | НТЛМ | `1000` | `nt` |
| `284ddc0d513372c7d764a0ff813473f2...` | ША-256 | `1400` | `raw-sha256` |
| `$2a$12$DOW8W6...` | bcrypt | `3200` | `bcrypt` |
| `$6$salt$hash...` | SHA-512 (Unix) | `1800` | `sha512crypt` |
| `$krb5tgs$23$*...` | Кербероаст (TGS-REP) | `13100` | `krb5tgs` |
| `$krb5asrep$23$...` | AS-REP Обжарка | `18200` | `krb5asrep` |

---

## Автодекодирование и фильтры популярности

Многие приложения перед вставкой в базу данных хранят хэши в формате Base64 или Hex. `nth` может отменить это автоматически:
```bash
# Decode Base64 hash before checking
nth -t "XUkQS8xLJnY5cZ0REXxFkg==" --base64

# Decode hex encoded string before checking
nth -t "3564343134303261626334623261373662393731396439313130313763353932" --hex
```
---

## Интеграция с трубопроводами крекинга

Быстро передайте неизвестный хеш в Hashcat, используя автоматическую идентификацию:
```bash
# 1. Check hash and capture Hashcat mode
HASH="b4b9b02e6f09a9bd760f388b67351e2b"
MODE=$(nth -t "$HASH" --hashcat --popularity 1 | grep -oP '(?<=Hashcat: -m )\d+' | head -n 1)

# 2. Launch Hashcat with identified mode
hashcat -m "$MODE" -a 0 "$HASH" /usr/share/wordlists/rockyou.txt
```