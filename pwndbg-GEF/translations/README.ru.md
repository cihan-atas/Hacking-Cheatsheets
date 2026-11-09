<!-- language-home-navigation -->
[🏠 Русская главная страница](../../README.ru.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🎯 pwndbg & GEF — Полная шпаргалка

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ██████╗ ██╗    ██╗███╗   ██╗██████╗ ██████╗  ██████╗      ██████╗ ███████╗███████╗
  ██╔══██╗██║    ██║████╗  ██║██╔══██╗██╔══██╗██╔════╝     ██╔════╝ ██╔════╝██╔════╝
  ██████╔╝██║ █╗ ██║██╔██╗ ██║██║  ██║██████╔╝██║  ███╗    ██║  ███╗█████╗  █████╗  
  ██╔═══╝ ██║███╗██║██║╚██╗██║██║  ██║██╔══██╗██║   ██║    ██║   ██║██╔══╝  ██╔══╝  
  ██║     ╚███╔███╔╝██║ ╚████║██████╔╝██████╔╝╚██████╔╝    ╚██████╔╝███████╗██║     
  ╚═╝      ╚══╝╚══╝ ╚═╝  ╚═══╝╚═════╝ ╚═════╝  ╚═════╝      ╚═════╝ ╚══════╝╚═╝     
         Next-Gen GDB Enhancements for Reversing & Binary Exploitation
```
<p выравнивание="центр">
  <img src="https://img.shields.io/badge/pwndbg-GDB_Plugin-blue?style=for-the-badge" alt="pwndbg">
  <img src="https://img.shields.io/badge/GEF-GDB_Enhanced_Features-orange?style=for-the-badge" alt="GEF">
  <img src="https://img.shields.io/badge/Category-Binary_Exploitation_&_RE-green?style=for-the-badge" alt="Реверс и Pwn">
  <img src="https://img.shields.io/badge/License-MIT_%7C_FOSS-purple?style=for-the-badge" alt="Лицензия FOSS">
</p>

<p выравнивание="центр">
  <b>🎯 Основные усовершенствования терминала GDB с открытым исходным кодом, предназначенные для обратного проектирования двоичных файлов, анализа повреждений памяти, проверки структуры кучи и разработки эксплойтов CTF.</b>
</p>

---

## 📋 Содержание

- [Обзор и почему Vanilla GDB нуждается в улучшениях](#-overview--why-vanilla-gdb-needs-enhancements)
- [Установка и переключение между фреймворками](#-installation--переключение-между-фреймворками)
  - [Установка pwndbg](#installing-pwndbg)
  - [Установка GEF](#installing-gef)
  - [Управление `.gdbinit`](#managing-gdbinit)
- [Механизм отображения контекста](#-the-context-display-engine)
  - [Раздел регистров](#registers-section)
  - [Прогнозы дизассемблирования и прыжков](#disassembly--jump-predictions)
  - [Стек и телескоп](#stack--телескоп)
  - [Backtrace & Threads](#backtrace--threads)
- [Проверка памяти и навигация](#-memory-inspection--navigation)
  - [Телескопические указатели (`telescope`)](#телескопические указатели-телескоп)
  - [Сопоставления памяти процесса (`vmmap`)](#process-memory-mappings-vmmap)
  - [Поиск в памяти (`search`)](#searching-memory-search)
- [Использование и проверка кучи](#-heap-exploitation--inspection)
  - [Просмотр фрагментов и арен](#viewing-chunks--arenas)
  - [Проверка свободных контейнеров (Tcache, Fastbins, Smallbins)](#inspecting-free-bins-tcache-fastbins-smallbins)
  - [Визуализация макета кучи (`vis_heap_chunks`)](#visualizing-heap-layout-vis_heap_chunks)
- [Помощники разработки эксплойтов](#-exploit-development-helpers)
  - [Checksec и аудит смягчения последствий] (#checksec--mitigation-audit)
  - [Stack Canary Locator (`canary`)](#stack-canary-locator-canary)
  - [Циклические шаблоны и расчет смещения](#cycl-patterns--offset-calculation)
  - [Охота за гаджетами ROP в памяти](#in-memory-rop-gadget-hunting)
- [Краткая справочная таблица](#-краткая справочная-таблица)
- [Защитные соображения и двоичная защита](#-защитные-соображения--бинарные-защиты)
- [Юридическая оговорка](#-юридическая оговорка)

---

## 🎯 Обзор и почему Vanilla GDB нуждается в улучшениях

Стандартный отладчик GNU (`gdb`) — это инструмент промышленного уровня, но его готовый к использованию пользовательский интерфейс визуально аскетичен. Он не обновляет автоматически состояния регистров, не разыменовывает многоуровневые указатели и не отображает представления стека при выполнении инструкций.

**pwndbg** и **GEF** превращают GDB в элитный набор для обратного проектирования и двоичной эксплуатации:
- **pwndbg:** Специально разработан для низкоуровневой эксплуатации программного обеспечения, отладки кучи (внутренние функции распределителя glibc 2.27–2.39+) и автоматического разыменования указателя.
- **GEF (расширенные функции GDB):** один автономный скрипт Python с нулевыми внешними зависимостями, предлагающий независимые от архитектуры функции (ARM, MIPS, PowerPC, x86) и динамический анализ структуры.

---

## 📥 Установка и переключение между фреймворками

### Установка pwndbg
```bash
git clone https://github.com/pwndbg/pwndbg
cd pwndbg
./setup.sh
```
### Установка ГЭФ
```bash
# Automated one-liner download
bash -c "$(curl -fsSL https://gef.blah.cat/sh)"
```
### Управление `.gdbinit`
Чтобы переключаться между pwndbg и GEF без конфликтов, настройте `~/.gdbinit`:
```bash
# To use pwndbg:
echo "source /path/to/pwndbg/gdbinit.py" > ~/.gdbinit

# To use GEF:
echo "source ~/.gdbinit-gef.py" > ~/.gdbinit
```
---

## 🖥️ Механизм контекстного отображения

Каждый раз, когда выполнение останавливается на точке останова или шаге, pwndbg и GEF печатают автоматизированное многопанельное представление терминала:
```text
──────────────────────────[ REGISTERS / FLAGS ]──────────────────────────
RAX: 0x0 
RBX: 0x7fffffffe000 
RCX: 0x7ffff7ec2000 --> 0x00010102464c457f 
RDX: 0x7fffffffe1a0 --> 0x7fffffffe494 ('USER=zeus')
RSI: 0x7fffffffe198 --> 0x7fffffffe47e ('/bin/target')
RDI: 0x1 
RBP: 0x7fffffffe0a0 --> 0x7fffffffe0c0 --> 0x0
RSP: 0x7fffffffe080 --> 0x401156 (<main+34>: mov eax, 0)
RIP: 0x401140 (<main+12>: cmp QWORD PTR [rbp-0x8], 0x0)
──────────────────────────[ DISASM / CODE ]──────────────────────────
   0x401135 <main+1>     push   rbp
   0x401136 <main+2>     mov    rbp, rsp
   0x401139 <main+5>     sub    rsp, 0x10
 ► 0x401140 <main+12>    cmp    QWORD PTR [rbp-0x8], 0
   0x401145 <main+17>    je     0x401151 <main+29>  <-- JUMP IS TAKEN
   0x401147 <main+19>    mov    eax, 0x1
──────────────────────────[ STACK ]──────────────────────────
00:0000│ rsp 0x7fffffffe080 --> 0x401156 (<main+34>: mov eax, 0)
01:0008│     0x7fffffffe088 --> 0x0
02:0010│ rbp 0x7fffffffe0a0 --> 0x7fffffffe0c0 --> 0x0
03:0018│     0x7fffffffe0a8 --> 0x7ffff7df6d90 (<__libc_start_call_main+128>: mov edi, eax)
──────────────────────────[ BACKTRACE ]──────────────────────────
 ► 0   0x401140 main+12
   1   0x7ffff7df6d90 __libc_start_call_main+128
   2   0x7ffff7df6e40 __libc_start_main+128
   3   0x401065 _start+37
```
---

## 🔍 Проверка памяти и навигация

### Телескопические указатели (`telescope`)
Рекурсивно разыменовывает указатели памяти, отображая текст ASCII, функциональные символы или целевые указатели:
```text
pwndbg> telescope $rsp 16       # Dereference 16 entries starting at RSP
pwndbg> telescope 0x404000 8    # Dereference data section
```
### Отображения памяти процесса (`vmmap`)
Отображает полные сегменты виртуальной памяти с разрешениями на чтение/запись/выполнение:
```text
pwndbg> vmmap
```
*Быстро находите сегменты с разрешениями `rwx` (важно для внедрения шеллкода).*

### Поиск в памяти (`search`)
Найдите строки, целые числа или указатели в любом месте загруженных страниц памяти:
```text
pwndbg> search -s "/bin/sh"     # Search for string "/bin/sh"
pwndbg> search -p 0x401135      # Search for pointers to main()
pwndbg> search -x "41414141"    # Search for raw hexadecimal bytes
```
---

## 🧱 Эксплуатация и проверка кучи

Анализировать современные распределители кучи `glibc` очень просто с помощью специальных команд для кучи:

### Просмотр фрагментов и арен
```text
pwndbg> heap                    # Display entire heap layout
pwndbg> chunk 0x602000          # Parse chunk headers (size, flags: PREV_INUSE, IS_MMAPPED, NON_MAIN_ARENA)
pwndbg> arenas                  # List all active heap arenas
```
### Проверка свободных контейнеров (Tcache, Fastbins, Smallbins)
```text
pwndbg> bins                    # Show all free bins simultaneously
pwndbg> tcache                  # Display per-thread tcache entries
pwndbg> fastbins                # List single-linked fastbins
pwndbg> unsortedbin             # List unsorted bin chunks (used for libc leak)
```
### Визуализация структуры кучи (`vis_heap_chunks`)
```text
pwndbg> vis_heap_chunks 10      # Draw color-coded visual representation of consecutive heap chunks
```
---

## 🛠️ Использование помощников разработки

### Checksec и аудит смягчения последствий
Проверьте меры безопасности компилятора непосредственно из приглашения GDB:
```text
pwndbg> checksec
```
*Осматривает:*
- **RELRO:** Частичный или полный RELRO (защищает глобальную таблицу смещений `.got.plt`)
- **Стек:** Канарейка найдена против канарейки нет.
- **NX:** NX включен (неисполняемый стек/куча)
- **PIE:** PIE включен (независимый от положения исполняемый файл/ASLR)

### Канарский локатор стека (`canary`)
Найдите канареечное значение потока в локальном хранилище потока (TLS) и проверьте, где оно находится в текущем кадре стека:
```text
pwndbg> canary
```
### Циклические шаблоны и расчет смещения
Определите точное смещение для перезаписи указателя инструкции (EIP/RIP):
```text
# Generate a 100-byte De Bruijn cyclic sequence
pwndbg> cyclic 100

# After the crash (SIGSEGV), calculate offset from faulty address or register value
pwndbg> cyclic -l $rip
# Output: Finding cyclic pattern at offset 72
```
### Поиск гаджетов ROP в памяти
Найдите загруженные исполняемые сегменты для конкретных гаджетов:
```text
pwndbg> rop --grep "pop rdi"
```
---

## ⚡ Краткая справочная таблица

| Цель | Команда |
|-----------|---------|
| **Начните с pwndbg/GEF**| `gdb ./target_binary` |
| **Проверить регистры/стек** | `context` |
| **Рекурсивное разыменование указателя**| `telescope $rsp 10` |
| **Карта памяти и проверка RWX**| `vmmap` |
| **Искать строку в памяти**| `search -s "/bin/sh"` |
| **Минимизация последствий аудита** | `checksec` |
| **Найти канарейку стека** | `canary` |
| **Обзор блоков кучи** | `heap` |
| **Осмотрите свободные контейнеры** | `bins` |
| **Рассчитать смещение буфера**| `cyclic 100` -> `cyclic -l <val>` |
| **Установить точку останова на символе**| `b *main` |
| **Выполнить/Продолжить/Шаг** | `r` / `c` / `si` / `ni` |

---

## 🛡️ Оборонительные соображения и двоичная защита

1. **Полный RELRO:** Компиляция с помощью `-Wl,-z,relro,-z,now` делает `.got` полностью доступным только для чтения, что предотвращает атаки перезаписи GOT.
2. **Stack Canaries:** При компиляции с `-fstack-protector-all` файлы cookie помещаются перед адресами возврата, что приводит к переполнению буфера `__stack_chk_fail`.
3. **PIE и ASLR:** Рандомизирует базовые адреса кода, кучи и библиотеки при каждом выполнении.

---

## ⚠️ Юридическая оговорка

> [!ВНИМАНИЕ]
> pwndbg и GEF — это инструменты двоичного анализа и разработки эксплойтов, предназначенные исключительно для авторизованных исследований уязвимостей, защитного аудита программного обеспечения и академических соревнований CTF. Эксплуатация программного обеспечения без разрешения является незаконной.