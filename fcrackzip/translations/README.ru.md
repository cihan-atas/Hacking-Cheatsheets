<!-- language-home-navigation -->
[🏠 Русская главная страница](../../README.ru.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# Шпаргалка по fcrackzip

[fcrackzip](https://github.com/hyc/fcrackzip) — это быстрый многопоточный взломщик паролей командной строки для зашифрованных ZIP-архивов. Он поддерживает как атаки на основе словаря, так и оптимизированные атаки методом перебора против традиционного ZipCrypto (потоковый шифр PKZIP) и может проверять целостность архива без полного извлечения.

---

## Содержание

- [Основные функции и ограничения](#core-features--limitations)
- [Установка](#установка)
- [Атаки по словарю (`-D -p`)](#dictionary-attacks--d--p)
- [Атаки грубой силы (`-b`)](#brute-force-attacks--b)
- [Наборы символов и спецификация длины](#character-sets--length-specification)
- [Бенчмарк и оптимизация производительности](#benchmark--performation-optimization)
- [ZipCrypto против AES-256 (альтернатива John/Hashcat)](#zipcrypto-vs-aes-256-johnhashcat-alternative)

---

## Основные функции и ограничения

- **Сверхбыстрый взломщик ZipCrypto:** написан на C, оптимизирован для устаревшего алгоритма ZipCrypto (стандартное шифрование PKZIP).
- **Режим словаря:** последовательно считывает пароли-кандидаты из списков слов.
- **Режим грубой силы:** генерирует кандидатов на лету, используя настраиваемые наборы символов и границы длины.
- **Тест распаковки архива (`-u`):** проверяет пароли-кандидаты путем попытки распаковки, практически исключая ложные срабатывания.
- **Ограничение:** **не** поддерживает современное шифрование WinZip AES-256. Для ZIP-файлов AES-256 используйте `zip2john` + John the Ripper или Hashcat (режим 13600).

---

## Установка
```bash
# Debian / Ubuntu / Kali Linux
sudo apt-get update && sudo apt-get install -y fcrackzip

# Arch Linux / BlackArch
sudo pacman -S fcrackzip

# Fedora / CentOS / RHEL
sudo dnf install -y fcrackzip

# Verify command
fcrackzip -h
```
---

## Атаки по словарю (`-D -p`)

Самый быстрый способ протестировать общие списки слов:

### 1. Базовая атака по словарю с проверкой распаковки
```bash
fcrackzip -u -D -p /usr/share/wordlists/rockyou.txt secret_archive.zip
```
- `-u`: режим распаковки (проверяет CRC извлеченного файла, чтобы избежать ложных срабатываний).
- `-D`: режим атаки по словарю.
- `-p <file>`: Путь к файлу словаря.

### 2. Режим подробного вывода
Отображение проверенной скорости, текущего пароля и информации об отладке:
```bash
fcrackzip -v -u -D -p passwords.txt backup.zip
```
---

## Атаки грубой силы (`-b`)

Если атака по словарю не удалась, используйте брутфорс против неизвестных коротких паролей:

### 1. Цифровые PIN-коды методом перебора (от 4 до 6 цифр)
```bash
fcrackzip -u -b -c 1 -l 4-6 secure.zip
```
- `-b`: режим грубой силы.
- `-c 1`: Набор символов: только цифры (`0-9`).
- `-l 4-6`: Диапазон длины: минимум 4, максимум 6 символов.

### 2. Строчные буквенно-цифровые символы (от 1 до 5 символов)
```bash
fcrackzip -u -b -c a1 -l 1-5 archive.zip
```
- `-c a1`: строчные буквы (`a-z`) и цифры (`0-9`).

### 3. Строчные + Прописные + Цифры + Символы
```bash
fcrackzip -u -b -c aA1! -l 6-8 protected.zip
```
- `-c a`: строчные буквы (`a-z`).
- `-c A`: Прописные буквы (`A-Z`).
- `-c 1`: Числа (`0-9`).
- `-c !`: специальные символы (`[!:$%&/()=?{[]}+*~#]`).

---

## Наборы символов и спецификация длины

| Флаг | Набор символов включен | Примеры кандидатов |
|---|---|---|
| `-c a` | Строчные буквы (`a-z`) | `admin`, `secret` |
| `-c A` | Прописные буквы (`A-Z`) | `SECRET`, `ROOT` |
| `-c 1` | Цифры (`0-9`) | `1234`, `2024` |
| `-c !` | Специальные знаки пунктуации | `!@#$` |
| `-c aA1` | Комбинированные буквенно-цифровые символы | `Admin123` |
| `-p <prefix>` | Начальная начальная исходная строка | Полезно возобновить перебор |

Указание границ длины:
```bash
# Minimum length 6, Maximum length 8
-l 6-8

# Exact length 8
-l 8-8
```
---

## Тестирование и оптимизация производительности

Проверьте производительность процессора на вашем компьютере:
```bash
fcrackzip -B
```
---

## ZipCrypto против AES-256 (альтернатива John/Hashcat)

Если `fcrackzip` возвращает `not a zipfile, or unsupported encryption type`, архив зашифрован с помощью AES-256 (WinZip AES). Используйте `zip2john`:
```bash
# 1. Extract hash using zip2john
zip2john aes_protected.zip > zip.hash

# 2. Crack using John the Ripper
john --wordlist=/usr/share/wordlists/rockyou.txt zip.hash

# 3. Or crack using Hashcat (Mode 13600: WinZip)
cat zip.hash | cut -d ':' -f 2 > hashcat_zip.hash
hashcat -m 13600 hashcat_zip.hash /usr/share/wordlists/rockyou.txt
```