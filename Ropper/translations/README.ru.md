<!-- language-home-navigation -->
[🏠 Русская главная страница](../../README.ru.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🧩 Ropper & ROPgadget — полная шпаргалка

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ██████╗  ██████╗ ██████╗ ██████╗ ███████╗██████╗ 
  ██╔══██╗██╔═══██╗██╔══██╗██╔══██╗██╔════╝██╔══██╗
  ██████╔╝██║   ██║██████╔╝██████╔╝█████╗  ██████╔╝
  ██╔══██╗██║   ██║██╔═══╝ ██╔═══╝ ██╔══╝  ██╔══██╗
  ██║  ██║╚██████╔╝██║     ██║     ███████╗██║  ██║
  ╚═╝  ╚═╝ ╚═════╝ ╚═╝     ╚═╝     ╚══════╝╚═╝  ╚═╝
     Advanced ROP & JOP Gadget Finder and Chain Generator
```
<p выравнивание="центр">
  <img src="https://img.shields.io/badge/Ropper-Gadget_Finder-blue?style=for-the-badge" alt="Ropper">
  <img src="https://img.shields.io/badge/ROPgadget-Exploit_Tool-orange?style=for-the-badge" alt="ROPgadget">
  <img src="https://img.shields.io/badge/Bypass-NX_%7C_DEP-red?style=for-the-badge" alt="Обход NX">
  <img src="https://img.shields.io/badge/Category-Exploit_Development-green?style=for-the-badge" alt="Exploit Dev">
</p>

<p выравнивание="центр">
  <b>🧩 Основные утилиты командной строки для обнаружения гаджетов возвратно-ориентированного программирования (ROP) и Jump-Oriented Programming (JOP) для обхода защиты неисполняемых файлов (NX/DEP) памяти</b>
</p>

---

## 📋 Содержание

- [Обзор и теория ROP](#-overview--rop-theory)
- [Установка](#-установка)
- [Базовое обнаружение гаджетов](#-basic-gadget-discovery)
  - [Извлечение всех гаджетов](#extracting-all-gadgets)
  - [Ограничение глубины инструкций](#limiting-instruction-глубина)
- [Расширенный поиск и фильтрация шаблонов](#-advanced-search--pattern-filtering)
  - [Нацеливание на определенные регистры (`rdi`, `rsi`, `rdx`, `rax`)](#targeting-special-registers-rdi-rsi-rdx-rax)
  - [Гаджеты записи в память (запись-что-куда)](#memory-write-gadgets-write-what-where)
  - [Гаджеты системных вызовов и прерываний](#syscall--interrupt-gadgets)
- [Исключая плохие символы (`--badbytes`)](#-исключая-плохие-символы---плохие байты)
- [Автоматическое создание цепочки ROP](#-automated-rop-chain-generation)
  - [Автоматические цепочки Ropper](#ropper-automated-chains)
  - [Автоматические цепочки ROPgadget](#ropgadget-automated-chains)
- [Анализ загруженных библиотек (цепочки ROP libc)](#-analyzing-loaded-libraries-libc-rop-chains)
- [Краткая справочная таблица](#-краткая справочная-таблица)
- [Защитные соображения и меры по смягчению последствий](#-защитные-соображения--смягчения)
- [Юридическая оговорка](#-юридическая оговорка)

---

## 🎯 Обзор и теория ROP

В современных операционных системах применяется **NX (без выполнения) / DEP (предотвращение выполнения данных)**, что предотвращает прямое выполнение шелл-кода, помещенного в стек или кучу.

**Возвратно-ориентированное программирование (ROP)** обходит это ограничение за счет повторного использования небольших существующих машинных инструкций, уже присутствующих в исполняемой памяти (раздел `.text` двоичных или разделяемых библиотек, таких как `libc.so`). Каждая последовательность инструкций, заканчивающаяся инструкцией `ret`, называется **гаджетом**. Располагая адреса в стеке, злоумышленники объединяют гаджеты в цепочку, чтобы:
1. Загрузите аргументы в регистры ЦП (например, `pop rdi; ret` для соглашений о вызовах x64).
2. Запишите строки (например, `/bin/sh`) в доступные для записи разделы `.bss` или `.data`.
3. Запустите `execve("/bin/sh", NULL, NULL)` или вызовите `system()`.

---

## 📥 Установка
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y ropper python3-ropgadget

# Python pip (Latest Releases)
pip install ropper capstone
pip install ROPgadget

# Arch Linux
sudo pacman -S ropper python-ropgadget
```
---

## 🚀 Базовое обнаружение гаджетов

### Извлечение всех гаджетов
```bash
# Using Ropper (Faster multi-threaded engine)
ropper --file ./target_binary --nocolor

# Using ROPgadget
ROPgadget --binary ./target_binary
```
### Ограничение глубины инструкций
Уменьшите шумные длинные цепочки, ограничив длину гаджета (количество инструкций до `ret`):
```bash
# Limit to maximum 3 instructions before return
ropper --file ./target_binary --inst-count 3
ROPgadget --binary ./target_binary --depth 3
```
---

## 🔍 Расширенный поиск и фильтрация шаблонов

### Выбор конкретных регистров (`rdi`, `rsi`, `rdx`, `rax`)
В соглашении о вызовах 64-битной Linux первые три аргумента функции или системного вызова должны находиться в `rdi`, `rsi` и `rdx`:
```bash
# Find pop rdi gadgets (1st argument)
ropper --file ./target_binary --search "pop rdi"

# Find pop rsi gadgets (2nd argument)
ropper --file ./target_binary --search "pop rsi"

# Find pop rdx gadgets (3rd argument)
ropper --file ./target_binary --search "pop rdx"

# Find gadgets that control RAX (Syscall number: 59 for execve)
ropper --file ./target_binary --search "pop rax"
```
Использование сопоставления регулярных выражений ROPgadget:
```bash
ROPgadget --binary ./target_binary --only "pop|ret" | grep -E "rdi|rsi|rdx|rax"
```
### Гаджеты записи в память (запись-что-куда)
Когда вам нужно сохранить `/bin/sh` или произвольные аргументы в памяти:
```bash
# Find instructions moving registers into pointer memory
ropper --file ./target_binary --search "mov [???], ???"
```
*Пример соответствия:* `mov qword ptr [rdi], rsi; ret` позволяет записывать все, что находится в `rsi`, по адресу, указанному `rdi`.

### Гаджеты системных вызовов и прерываний
```bash
# Find raw syscall instructions
ropper --file ./target_binary --search "syscall"

# x86 32-bit software interrupt
ropper --file ./target_binary --search "int 0x80"
```
---

## 🚫 Исключение плохих персонажей (`--badbytes`)

Уязвимости форматирования строк или функции, такие как `strcpy()`, завершаются на нулевых байтах (`\x00`), новой строке (`\x0a`) или возврате каретки (`\x0d`). Исключите адреса, содержащие эти байты:
```bash
# Filter out gadgets whose addresses contain bad characters
ropper --file ./target_binary --badbytes 000a0d --search "pop rdi"
ROPgadget --binary ./target_binary --badbytes "00|0a|0d"
```
---

## ⚡ Автоматизированное создание цепочки ROP

Оба инструмента могут автоматически создавать функциональные шаблоны эксплойтов Python, вызывающие `execve("/bin/sh")`:

### Автоматизированные цепи Ropper
```bash
ropper --file ./target_binary --chain "execve"
```
### Автоматизированные цепочки ROPgadget
```bash
ROPgadget --binary ./target_binary --ropchain
```
*В результате получается готовый к использованию скелет полезной нагрузки Python, объединяющий `mprotect()` или `execve()`.*

---

## 📚 Анализ загруженных библиотек (цепочки ROP libc)

Если сам двоичный файл скомпилирован как **PIE** или содержит мало полезных гаджетов, извлеките гаджеты непосредственно из общей библиотеки C целевой системы (`libc.so.6`):
```bash
# Search gadgets in libc
ropper --file /lib/x86_64-linux-gnu/libc.so.6 --search "pop rdi; ret"

# Find "/bin/sh" string offset inside libc
strings -a -t x /lib/x86_64-linux-gnu/libc.so.6 | grep "/bin/sh"
```
*(Добавьте обнаруженные смещения к утекшему базовому адресу libc во время выполнения).*

---

## ⚡ Краткая справочная таблица

| Цель | Команда Роппера | Команда ROPgadget |
|-----------|----------------|-------------------|
| **Удалить все гаджеты** | `ropper --file <bin>` | `ROPgadget --binary <bin>` |
| **Гаджет для конкретного поиска** | `ropper --file <bin> --search "pop rdi"` | `ROPgadget --binary <bin> \| grep "pop rdi"` |
| **Фильтровать неверные байты** | `ropper --file <bin> --badbytes 000a` | `ROPgadget --binary <bin> --badbytes "00\|0a"` |
| **Ограничить глубину/длину** | `ropper --file <bin> --inst-count 3` | `ROPgadget --binary <bin> --depth 3` |
| **Поиск гаджетов JOP** | `ropper --file <bin> --type jop` | `ROPgadget --binary <bin> --norop` |
| **Автоматическое построение цепочки ROP** | `ropper --file <bin> --chain "execve"`| `ROPgadget --binary <bin> --ropchain` |
| **Поиск записей в памяти** | `ropper --file <bin> --search "mov [???], ???"` | `ROPgadget --binary <bin> \| grep "mov \["` |

---

## 🛡️ Соображения по защите и смягчению последствий

1. **Технология контроля потока управления (Intel CET/Shadow Stack):** Современные процессоры поддерживают изолированный аппаратный теневой стек, который проверяет адреса возврата на `ret`, немедленно завершая цепочки ROP.
2. **Целостность потока управления Clang/GCC (CFI):** проверяет целевые указатели функций на соответствие графу переднего края перед косвенными вызовами.

---

## ⚠️ Юридическая оговорка

> [!ВНИМАНИЕ]
> Ropper и ROPgadget — утилиты для разработки эксплойтов и оценки безопасности программного обеспечения. Их строго следует использовать в авторизованных тестах на проникновение, CTF-проблемах или исследованиях защитной безопасности. Эксплуатация программного обеспечения без явного разрешения является незаконной.