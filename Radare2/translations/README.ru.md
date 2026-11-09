<!-- language-home-navigation -->
[🏠 Русская главная страница](../../README.ru.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ⚡ Radare2 (r2) — Полная шпаргалка

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ██████╗  █████╗ ██████╗  █████╗ ██████╗ ███████╗██████╗ 
  ██╔══██╗██╔══██╗██╔══██╗██╔══██╗██╔══██╗██╔════╝╚════██╗
  ██████╔╝███████║██║  ██║███████║██████╔╝█████╗   █████╔╝
  ██╔══██╗██╔══██║██║  ██║██╔══██║██╔══██╗██╔══╝  ██╔═══╝ 
  ██║  ██║██║  ██║██████╔╝██║  ██║██║  ██║███████╗███████╗
  ╚═╝  ╚═╝╚═╝  ╚═╝╚═════╝ ╚═╝  ╚═╝╚═╝  ╚═╝╚══════╝╚══════╝
      UNIX-like Command-Line Reverse Engineering Framework
```
<p выравнивание="центр">
  <img src="https://img.shields.io/badge/Radare2-CLI_Reversing-blue?style=for-the-badge" alt="Radare2">
  <img src="https://img.shields.io/badge/Author-Sergi_Álvarez-orange?style=for-the-badge" alt="pancake">
  <img src="https://img.shields.io/badge/Category-Reverse_Engineering-green?style=for-the-badge" alt="Обратное проектирование">
  <img src="https://img.shields.io/badge/License-LGPLv3-purple?style=for-the-badge" alt="LGPLv3">
</p>

<p выравнивание="центр">
  <b>⚡ Мощная платформа с открытым исходным кодом на базе терминала для двоичного анализа, дизассемблирования, отладки, шестнадцатеричного редактирования и двоичного исправления</b>
</p>

---

## 📋 Содержание

- [Обзор и архитектура](#-обзор--архитектура)
- [Установка](#-установка)
- [Открытие файлов и режимы сеанса](#-opening-files--session-modes)
- [Команды двоичного анализа (`a`)](#-двоичный-анализ-команды-a)
- [Навигация и поиск (`s`)](#-navigation--seeking-s)
- [Печать и разборка (`p`)](#-printing--disassembly-p)
- [Интерактивные визуальные и графические режимы (`V` и `VV`)](#-interactive-visual--graph-modes-v--vv)
- [Двоичное исправление и модификация (`w`)](#-binary-patching--modification-w)
- [Набор инструментов Radare2 Companion](#-the-radare2-companion-toolset)
  - [rabin2: Извлечение двоичной информации](#rabin2-binary-information-extraction)
  - [rasm2: Ассемблер и дизассемблер](#rasm2-assembler--дизассемблер)
  - [radiff2: Механизм двоичных разностей](#radiff2-binary-difference-engine)
  - [rafind2: Поиск по байтам и строкам](#rafind2-byte--string-search)
  - [rax2: Базовый и числовой преобразователь](#rax2-base--numeric-converter)
- [Основы отладки с помощью r2 (`-d`)](#-debugging-basics-with-r2--d)
- [Краткая справочная таблица](#-краткая справочная таблица)
- [Защитные соображения и противодействие реверсированию](#-защитные-соображения--анти-реверс)
- [Юридическая оговорка](#-юридическая оговорка)

---

## 🎯 Обзор и архитектура

**Radare2** (`r2`), созданный **Серхи Альваресом (блин)**, представляет собой портативную модульную среду обратного проектирования, полностью построенную на философии UNIX. Он полностью работает внутри вашего терминала, не требуя тяжелых графических зависимостей:
- **Полная поддержка архитектур:** x86/x64, ARM, ARM64, MIPS, PowerPC, AVR, SPARC, WebAssembly, Java, Dalvik и EVM.
- **Компонируемые утилиты CLI:** поставляются в комплекте со специализированными инструментами командной строки (`rabin2`, `rasm2`, `radiff2`, `rafind2`, `rax2`).
- **Интерактивные визуальные режимы:** шестнадцатеричные редакторы с разделенным экраном, графы потока управления ASCII, мониторы регистров и представления декомпилятора.
- **С возможностью сценариев и расширяемости:** интегрируется с Python, r2pipe, Ghidra (`r2ghidra`) и Rizin.

---

## 📥 Установка
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y radare2

# Arch Linux
sudo pacman -S radare2

# Install latest Git master (Recommended for most features)
git clone https://github.com/radareorg/radare2.git
cd radare2 && sys/install.sh

# Install r2ghidra decompiler plugin
r2pm update
r2pm -i r2ghidra
```
---

## 🚀 Открытие файлов и режимы сеанса
```bash
# Open binary in read-only mode
r2 /bin/ls

# Open binary in read-write mode (required for patching)
r2 -w target.bin

# Open binary in debugger mode
r2 -d target.bin

# Open binary without parsing headers (raw flat binary / shellcode)
r2 -b 32 -n /path/to/raw.bin

# Open in quiet mode (no greeting banner)
r2 -q target.bin
```
---

## 🔬 Команды двоичного анализа (`a`)

Внутри оболочки `r2` команды представляют собой отдельные буквы или короткие префиксы. Вы можете добавить `?` к любой команде, чтобы увидеть встроенную справку:
```text
[0x00000000]> a?        # Show analysis help
[0x00000000]> aa        # Basic analysis (symbols, basic blocks)
[0x00000000]> aaa       # Deep analysis (functions, cross-references, calls)
[0x00000000]> aaaa      # Experimental auto-analysis (takes longer)

[0x00000000]> afl       # List all identified functions
[0x00000000]> afi       # Display function info at current address
[0x00000000]> afvn      # Rename local variable: afvn <new_name> <old_name>
[0x00000000]> axt <addr># Find cross-references (xrefs) TO address
[0x00000000]> axf <addr># Find cross-references (xrefs) FROM address
```
---

## 🧭 Навигация и поиск (`s`)

Команда поиска изменяет текущий адрес курсора:
```text
[0x00000000]> s main        # Seek to main function
[0x00401122]> s 0x401200    # Seek to absolute address
[0x00401200]> s+ 32         # Seek 32 bytes forward
[0x00401220]> s- 16         # Seek 16 bytes backward
[0x00401210]> s/ hit0_0     # Seek to search hit
[0x00401210]> u             # Undo seek (go back to previous address)
[0x00401200]> U             # Redo seek
```
---

## 📜 Печать и разборка (`p`)
```text
[0x00401122]> pd 10     # Disassemble 10 instructions
[0x00401122]> pdf       # Disassemble entire current function
[0x00401122]> pdc       # Decompile function using r2ghidra plugin
[0x00401122]> px 64     # Print 64 bytes in hex dump format
[0x00401122]> ps        # Print string at current address
[0x00401122]> pz        # Print zero-terminated string
[0x00401122]> pi 5      # Print 5 instructions (concise format)
```
---

## 🖥️ Интерактивные визуальные и графические режимы (`V` и `VV`)

Нажатие `V` или `VV` превращает Radare2 в богатое интерактивное терминальное приложение:

- **Войдите в визуальный режим:** Введите `V`.
  - Нажмите `p` / `P` для циклического просмотра (шестнадцатеричный дамп -> Дизассемблирование -> Отладчик -> Декомпилятор).
  - Нажмите `c`, чтобы переключить режим курсора (используйте клавиши со стрелками или `hjkl` для выбора байтов).
  - Нажмите `d`, чтобы переопределить байты как код, данные или строки.
  - Нажмите `q`, чтобы вернуться к стандартной подсказке.
- **Вход в режим графика:** Введите `VV`.
  - Отображает граф потока управления ASCII (CFG).
  - Используйте `Tab`/`Shift+Tab` для перехода между узлами условного ветвления.
  - Нажмите `t`, чтобы следовать по истинной ветке; `f`, чтобы следовать по ложной ветке.
  - Нажмите `u`, чтобы выйти из ветки.

---

## ✍️ Бинарное исправление и модификация (`w`)

Чтобы изменить двоичный файл на диске, убедитесь, что r2 открыт в режиме записи (`r2 -w target` или введите `oo+` внутри активного сеанса):
```text
[0x00401150]> oo+                           # Re-open current binary with write permissions
[0x00401150]> wa nop                        # Assemble 'nop' instruction at current address
[0x00401150]> wa jmp 0x401190               # Patch a jump instruction
[0x00401150]> wx 90909090                   # Write hex bytes (4 NOPs)
[0x00401150]> w "Modified string\x00"       # Overwrite string with null-terminator
[0x00401150]> w0 16                         # Zero-fill 16 bytes (NUL padding)
```
---

## 🛠️ Дополнительный набор инструментов Radare2

Radare2 включает автономные утилиты CLI:

### rabin2: извлечение двоичной информации
```bash
# Print general binary architecture, bits, and security mitigations (NX, PIE, Canary)
rabin2 -I target.bin

# List imported symbols and functions (libc calls)
rabin2 -i target.bin

# List exported symbols
rabin2 -E target.bin

# Extract ASCII & UTF-16 strings
rabin2 -z target.bin

# List sections (.text, .data, .rodata)
rabin2 -S target.bin
```
### rasm2: Ассемблер и дизассемблер
```bash
# Assemble x64 instructions into hexadecimal opcode
rasm2 -a x86 -b 64 "xor eax, eax; ret"
# Output: 31c0c3

# Disassemble hex opcodes into human-readable assembly
rasm2 -a x86 -b 64 -d "31c0c3"
# Output:
# xor eax, eax
# ret
```
### radiff2: Двоичная разностная машина
```bash
# Compare two binaries and count differing bytes
radiff2 -c original.bin patched.bin

# Detailed code comparison graph
radiff2 -g main original.bin patched.bin | xdot -
```
### rafind2: поиск по байтам и строкам
```bash
# Search for ASCII string in file
rafind2 -s "admin_password" target.bin

# Search for hexadecimal pattern
rafind2 -x "4889e5" target.bin
```
### rax2: Конвертер счисления и счисления
```bash
# Convert hex to decimal
rax2 0x1337      # Output: 4919

# Convert decimal to hex
rax2 4919        # Output: 0x1337

# Convert string to hex
rax2 -S "admin"  # Output: 61646d696e
```
---

## 🐛 Основы отладки с помощью r2 (`-d`)
```bash
# Start debugging binary with arguments
r2 -d target.bin arg1 arg2
```
Внутри оболочки отладчика:
```text
[0x7f112233]> db main       # Set software breakpoint at main
[0x7f112233]> db 0x401150   # Set breakpoint at specific address
[0x7f112233]> dbl           # List all active breakpoints
[0x7f112233]> dc            # Continue execution until breakpoint
[0x7f112233]> ds            # Step into (single instruction)
[0x7f112233]> dso           # Step over function calls
[0x7f112233]> dr            # Print all CPU registers
[0x7f112233]> dr rax        # Print specific register value
[0x7f112233]> dr rax = 0x1  # Modify register value
```
---

## ⚡ Краткая справочная таблица

| Цель | Команда |
|-----------|---------|
| **Глубокий анализ** | `aaa` |
| **Список функций** | `afl` |
| **Искать функцию/адрес** | `s <addr>` / `s main` |
| **Функция дизассемблирования** | `pdf` |
| **Функция декомпиляции (r2ghidra)** | `pdc` |
| **Интерактивная визуальная разборка** | `V` |
| **Блок-график ASCII** | `VV` |
| **Инструкции по сборке** | `wa <instruction>` |
| **Запись шестнадцатеричных байтов** | `wx <bytes>` |
| **Повторно открыть в режиме чтения и записи** | `oo+` |
| **Извлечение двоичной информации** | `rabin2 -I <binary>` |
| **Собрать интерфейс командной строки** | `rasm2 -a x86 -b 64 "<asm>"` |

---

## 🛡️ Соображения по защите и предотвращению реверса

1. **Обнаружение антиотладки.** Отладчик Radare2 использует стандарт Linux `ptrace`. Двоичные файлы с самопроверкой `ptrace(PTRACE_TRACEME)` могут блокировать вложения, если они не исправлены.
2. **Запутывание и выравнивание потока управления:** Сильно запутанные двоичные файлы (OLLVM) затрудняют восстановление базового блока. Используйте `aaaa` или механизмы символьного выполнения (например, angr), чтобы упростить простые функции.

---

## ⚠️ Юридическая оговорка

> [!ВНИМАНИЕ]
> Radare2 — это набор инструментов для обратного проектирования и анализа, предназначенный исключительно для авторизованных оценок безопасности, анализа вредоносного ПО, исследования уязвимостей и использования в личных целях. Реверс-инжиниринг проприетарного программного обеспечения без разрешения может нарушать лицензионные соглашения с конечным пользователем и законы об авторских правах.