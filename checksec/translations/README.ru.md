<!-- language-home-navigation -->
[🏠 Русская главная страница](../../README.ru.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🛡️ checksec и Binutils — полная шпаргалка

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
   ██████╗██╗  ██╗███████╗ ██████╗██╗  ██╗███████╗███████╗ ██████╗
  ██╔════╝██║  ██║██╔════╝██╔════╝██║ ██╔╝██╔════╝██╔════╝██╔════╝
  ██║     ███████║█████╗  ██║     █████╔╝ ███████╗█████╗  ██║     
  ██║     ██╔══██║██╔══╝  ██║     ██╔═██╗ ╚════██║██╔══╝  ██║     
  ╚██████╗██║  ██║███████╗╚██████╗██║  ██╗███████║███████╗╚██████╗
   ╚═════╝╚═╝  ╚═╝╚══════╝ ╚═════╝╚═╝  ╚═╝╚══════╝╚══════╝ ╚═════╝
          Linux ELF Binary Mitigation Auditor & Inspection Toolkit
```
<p выравнивание="центр">
  <img src="https://img.shields.io/badge/checksec-Security_Mitigations-blue?style=for-the-badge" alt="checksec">
  <img src="https://img.shields.io/badge/readelf-ELF_Headers-orange?style=for-the-badge" alt="readelf">
  <img src="https://img.shields.io/badge/objdump-Disassembly-green?style=for-the-badge" alt="objdump">
  <img src="https://img.shields.io/badge/Platform-Linux_ELF-purple?style=for-the-badge" alt="Linux ELF">
</p>

<p выравнивание="центр">
  <b>🛡️ Основные инструменты командной строки для аудита мер безопасности исполняемых файлов Linux (RELRO, Stack Canary, NX, PIE, Fortify) и анализа двоичных заголовков, разделов и таблиц символов ELF</b>
</p>

---

## 📋 Содержание

- [Обзор и двоичная матрица смягчения последствий](#-overview--binary-mitigations-matrix)
- [Установка](#-установка)
- [checksec: Быстрый аудит устранения последствий](#-checksec-rapid-mitigation-auditing)
  - [Аудит одиночных и пакетных двоичных файлов](#auditing-single--batch-binaries)
  - [Аудит запущенных процессов](#auditing-running-processes)
  - [Вывод JSON и CSV](#json--csv-output)
- [readelf: Заголовок и раздел ELF](#-readelf-elf-header--section-dissection)
  - [Заголовок файла ELF (`-h`)](#elf-file-header--h)
  - [Заголовки и сегменты программ (`-l`)](#program-headers--segments--l)
  - [Заголовки разделов (`-S`)](#section-headers--s)
  - [Таблицы символов (`-s` / `--syms`)](#symbol-tables--s----syms)
  - [Динамические зависимости и библиотеки (`-d`)](#dynamic-dependents--libraries--d)
- [objdump: Разборка и перемещение](#-objdump-disassembly--relocations)
  - [Разборка Intel (`-d -M intel`)](#intel-disassembly--d--m-intel)
  - [Записи о перемещении и GOT (`-R`)](#relocation-entries--got--r)
- [нм и строки: разрешение символов и строки](#-нм--строки-символ-разрешение--строки)
  - [Перечисление разобранных символов с помощью `nm`](#listing-demangled-symbols-with-nm)
  - [Извлечение запутанных строк с помощью `strings`](#extracting-obfuscated-strings-with-strings)
- [Усиленная ссылка на компиляцию](#-hardened-compilation-reference)
- [Краткая справочная таблица](#-краткая справочная-таблица)
- [Юридическая оговорка](#-юридическая оговорка)

---

## 🎯 Обзор и двоичная матрица смягчения последствий

Прежде чем приступить к любому рабочему процессу обратного проектирования или эксплуатации, необходимо понять, какие меры безопасности включены:

| смягчение последствий | Полное имя | Механизм защиты | Стратегия обхода |
|------------|-----------|----------------------|-----------------|
| **РЭЛРО** | Перемещение Только для чтения | Делает `.got` доступным только для чтения (`Full RELRO`), чтобы предотвратить перезапись указателя | Целевые структуры кучи, адреса возврата или перехватчики функций |
| **Канарейка** | Защитник стека | Помещает рандомизированный файл cookie перед указателем возврата; проверяет целостность при возврате | Утечка канареечного значения из-за утечки памяти или грубой силы (разветвление демонов) |
| **НХ** | Без выполнения / DEP | Помечает стек, кучу и сегменты данных как неисполняемые | Возвратно-ориентированное программирование (ROP) или переходно-ориентированное программирование (JOP) |
| **ПИРОГ** | Независимый от позиции исполняемый файл | Загружает двоичный базовый адрес в случайном месте в виртуальной памяти (ASLR) | Указатель кода утечки для расчета смещения базового адреса |
| **Укрепить** | `_FORTIFY_SOURCE` | Заменяет небезопасные строковые функции (`strcpy` -> `__strcpy_chk`) проверкой границ | Логические ошибки без переполнения, строка формата без переполнения буфера |

---

## 📥 Установка
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y checksec binutils

# Arch Linux
sudo pacman -S checksec binutils

# Python pip (standalone checksec)
pip install checksec.py
```
---

## ⚡ checksec: быстрый аудит мер по устранению последствий

### Аудит одиночных и пакетных двоичных файлов
```bash
# Audit a single executable
checksec --file=./target_binary

# Audit all binaries in directory
checksec --dir=/usr/bin/
```
*Пример стандартного вывода:*
```text
RELRO           STACK CANARY      NX            PIE             RPATH      RUNPATH      FILE
Full RELRO      Canary found      NX enabled    PIE enabled     No RPATH   No RUNPATH   ./target_binary
```
### Аудит запущенных процессов
Аудит уменьшения памяти и состояния ASLR активного демона:
```bash
# Audit by Process ID (PID)
checksec --proc=1337

# Audit process by name
checksec --proc-all
```
### Вывод JSON и CSV
Экспорт результатов для автоматического анализа:
```bash
checksec --file=./target_binary --output=json > security.json
checksec --file=./target_binary --output=csv > security.csv
```
---

## 🔬 readelf: анализ заголовка и раздела ELF

Утилита `readelf` отображает информацию из файлов ELF, не вызывая никакого выполнения:

### Заголовок ELF-файла (`-h`)
Показывает архитектуру ЦП, 32- и 64-битный класс, адрес точки входа и порядок байтов:
```bash
readelf -h ./target_binary
```
### Заголовки и сегменты программы (`-l`)
Отображает сегменты, загруженные в память во время выполнения, и флаги (`R`, `W`, `E`). Крайне важно определить, имеет ли стек (`GNU_STACK`) разрешения на выполнение:
```bash
readelf -l ./target_binary
```
*(Если `GNU_STACK` имеет `RWE`, выполнение стека разрешено; NX отключен).*

### Заголовки разделов (`-S`)
Проверьте структуру памяти, адреса и смещения для `.text` (код), `.data` (инициализированные данные), `.rodata` (константы), `.bss` (неинициализированные данные), `.plt` и `.got`:
```bash
readelf -S ./target_binary
```
### Таблицы символов (`-s` / `--syms`)
Перечислите все экспортированные функции, глобальные переменные и импортированные символы libc:
```bash
# List all symbols
readelf -s ./target_binary

# Filter dynamic symbols used at runtime
readelf --dyn-syms ./target_binary
```
### Динамические зависимости и библиотеки (`-d`)
Перечисляет необходимые общие библиотеки (теги `NEEDED`, например `libc.so.6`, `libssl.so`):
```bash
readelf -d ./target_binary
```
---

## 🧩 objdump: разборка и перемещение

### Разборка Intel (`-d -M intel`)
Дизассемблируем исполняемые разделы в чистую Intel-синтаксическую сборку:
```bash
# Disassemble .text section in Intel syntax
objdump -d -M intel ./target_binary

# Disassemble specific function (e.g. main)
objdump -d -M intel ./target_binary | grep -A 30 "<main>:"
```
### Записи о перемещении и GOT (`-R`)
Отображение адресов перемещения глобальной таблицы смещений (GOT) для импортированных вызовов библиотеки:
```bash
objdump -R ./target_binary
```
*Показывает целевые адреса для таких функций, как `puts`, `printf` и `system`.*

---

## 🔍 nm и строки: разрешение символов и строки

### Перечисление расчлененных символов с помощью `nm`
```bash
# List defined symbols only (ignore external imports)
nm --defined-only -C ./target_binary

# List undefined external symbols (functions resolved by dynamic linker)
nm -u -C ./target_binary
```
### Извлечение запутанных строк с помощью `strings`
```bash
# Extract ASCII strings with minimum 8 characters
strings -a -n 8 ./target_binary

# Extract 16-bit Little Endian wide strings (UTF-16 e.g., Windows / Java)
strings -e l -a ./target_binary

# Print byte offset of each string in hex
strings -a -t x ./target_binary
```
---

## 🛡️ Усиленный справочник по компиляции

Чтобы создать двоичный файл ELF с максимальными мерами безопасности:
```bash
gcc -O2 \
  -Wall \
  -fstack-protector-strong \
  -D_FORTIFY_SOURCE=2 \
  -Wl,-z,relro,-z,now \
  -fPIE -pie \
  -Wl,-z,noexecstack \
  source.c -o secure_binary
```
---

## ⚡ Краткая справочная таблица

| Цель | Команда |
|-----------|---------|
| **Минимизация последствий аудита** | `checksec --file=<bin>` |
| **Аудит текущего PID** | `checksec --proc=<pid>` |
| **Просмотр заголовка ELF** | `readelf -h <bin>` |
| **Просмотр флага NX стека** | `readelf -l <bin> \| grep -A 1 GNU_STACK` |
| **Список разделов (.got, .plt)**| `readelf -S <bin>` |
| **Просмотр динамических символов** | `readelf --dyn-syms <bin>` |
| **Разборка Intel** | `objdump -d -M intel <bin>` |
| **Проверка перемещений GOT** | `objdump -R <bin>` |
| **Символы списка (разобранные)** | `nm -C <bin>` |
| **Извлечение строк UTF-16** | `strings -e l -a <bin>` |
| **Показать смещения строк** | `strings -a -t x <bin>` |

---

## ⚠️ Юридическая оговорка

> [!ВНИМАНИЕ]
> Checksec и GNU Binutils — это инструменты диагностики и обратного проектирования, предназначенные для аудита безопасности программного обеспечения, проверки защиты и авторизованного двоичного исследования. Проверка неавторизованного программного обеспечения должна осуществляться в соответствии с местными правилами интеллектуальной собственности.