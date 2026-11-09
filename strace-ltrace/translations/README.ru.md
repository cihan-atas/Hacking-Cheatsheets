<!-- language-home-navigation -->
[🏠 Русская главная страница](../../README.ru.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🔬 strace & ltrace — Полная шпаргалка

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ███████╗████████╗██████╗  █████╗  ██████╗███████╗    ██╗  ████████╗██████╗  █████╗  ██████╗███████╗
  ██╔════╝╚══██╔══╝██╔══██╗██╔══██╗██╔════╝██╔════╝    ██║  ╚══██╔══╝██╔══██╗██╔══██╗██╔════╝██╔════╝
  ███████╗   ██║   ██████╔╝███████║██║     █████╗      ██║     ██║   ██████╔╝███████║██║     █████╗  
  ╚════██║   ██║   ██╔══██╗██╔══██║██║     ██╔══╝      ██║     ██║   ██╔══██╗██╔══██║██║     ██╔══╝  
  ███████║   ██║   ██║  ██║██║  ██║╚██████╗███████╗    ███████╗██║   ██║  ██║██║  ██║╚██████╗███████╗
  ╚══════╝   ╚═╝   ╚═╝  ╚═╝╚═╝  ╚═╝ ╚═════╝╚══════╝    ╚══════╝╚═╝   ╚═╝  ╚═╝╚═╝  ╚═╝ ╚═════╝╚══════╝
            Linux Dynamic Binary Analysis: System & Library Call Tracers
```
<p выравнивание="центр">
  <img src="https://img.shields.io/badge/strace-System_Calls-blue?style=for-the-badge" alt="strace">
  <img src="https://img.shields.io/badge/ltrace-Library_Calls-orange?style=for-the-badge" alt="ltrace">
  <img src="https://img.shields.io/badge/Platform-Linux_ELF-green?style=for-the-badge" alt="Linux">
  <img src="https://img.shields.io/badge/Category-Dynamic_Anaанализ-purple?style=for-the-badge" alt="Динамический анализ">
</p>

<p выравнивание="центр">
  <b>🔬 Основные утилиты командной строки для динамического двоичного анализа: перехват системных вызовов ядра Linux (strace) и вызовов динамических библиотек пользовательского пространства (ltrace)</b>
</p>

---

## 📋 Содержание

- [Обзор: системные вызовы и вызовы библиотеки](#-overview-system-calls-vs-library-calls)
- [Установка](#-установка)
- [strace: Трассировка системных вызовов](#-strace-system-call-tracing)
  - [Базовое выполнение трассировки](#basic-tracing-execution)
  - [Фильтрация системных вызовов по классу или имени (`-e`)](#filtering-syscalls-by-class-or-name--e)
  - [Трассировка дочерних и многопоточных процессов (`-f`)](#tracing-child--multi-threaded-processes--f)
  - [Присоединение к запущенным процессам (`-p`)](#attaching-to-running-processes--p)
  - [Расширяющиеся строки и выходные данные буфера (`-s`)](#expanding-strings--buffer-outputs--s)
  - [Статистическое профилирование и анализ узких мест (`-c`)](#statistical-profiling-bottleneck-analysis--c)
  - [Внедрение ошибок для фаззинга и тестирования (`-e fault`)](#fault-injection-for-fuzzing--testing--e-fault)
- [ltrace: отслеживание вызовов динамической библиотеки](#-ltrace-dynamic-library-call-tracing)
  - [Базовый перехват библиотеки](#basic-library-interception)
  - [Взлом жестко запрограммированных проверок паролей (`strcmp`)](#cracking-hardcoded-password-checks-strcmp)
  - [Расчленение символов C++ (`-C`)](#demangling-c-symbols--c)
  - [Фильтрация определенных библиотечных функций](#filtering-special-library-functions)
- [Объединение рабочих процессов strace и ltrace](#-combining-strace-and-ltrace-workflows)
- [Краткая справочная таблица](#-краткая справочная-таблица)
- [Защитные соображения и борьба с отслеживанием] (#-защитные-соображения--анти-отслеживание)
- [Юридическая оговорка](#-юридическая оговорка)

---

## 🎯 Обзор: системные вызовы и вызовы библиотеки

При анализе скомпилированных двоичных файлов Linux ELF статический анализ может быть затруднен из-за обфускации, удаления или упаковки. Динамическая трассировка обеспечивает немедленную видимость действий во время выполнения:

1. **Системные вызовы (`strace`):**
   - Интерфейс между приложениями пользовательского пространства и ядром Linux Linux.
   - Отслеживает дисковый ввод-вывод (`openat`, `read`, `write`), работу сети (`socket`, `connect`), распределение памяти (`mmap`, `brk`) и управление процессами (`fork`, `execve`, `ptrace`).
2. **Вызовы библиотеки (`ltrace`):**
   - Интерфейс между бинарными и динамическими общими библиотеками (такими как `libc.so`, `libssl.so`).
   - Перехватывает вызовы, такие как `strcmp()`, `strlen()`, `malloc()`, `system()`, а также криптографические процедуры, часто раскрывая учетные данные в виде открытого текста в памяти перед шифрованием.

---

## 📥 Установка
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y strace ltrace

# Arch Linux
sudo pacman -S strace ltrace

# Fedora / Red Hat / CentOS
sudo dnf install -y strace ltrace
```
---

## ⚡ strace: отслеживание системных вызовов

### Базовое выполнение трассировки
Запустите целевой двоичный файл и выполните потоковую передачу всех системных вызовов в стандартную ошибку:
```bash
strace ./target_binary
```
Перенаправьте вывод в файл, чтобы он не мешал выполнению программы:
```bash
strace -o trace.log ./target_binary
```
### Фильтрация системных вызовов по классу или имени (`-e`)
Избегайте шума, фильтруя только соответствующие классы системных вызовов:
```bash
# Trace ONLY file-related operations (open, read, write, stat, access)
strace -e trace=file ./target_binary

# Trace ONLY network socket connections and data transfers
strace -e trace=network ./target_binary

# Trace process execution and fork/clone operations
strace -e trace=process ./target_binary

# Filter specific syscalls by exact name
strace -e trace=open,openat,read,write ./target_binary
```
### Отслеживание дочерних и многопоточных процессов (`-f`)
Современные двоичные файлы и вредоносные программы порождают дочерние рабочие процессы или потоки. Без `-f` strace их пропустит:
```bash
# Trace all child forks and threads
strace -f ./target_binary

# Trace children and save each process output into separate files (trace.pid)
strace -ff -o trace ./target_binary
```
### Привязка к запущенным процессам (`-p`)
Отслеживайте службу или активный вредоносный процесс, не перезапуская его:
```bash
# Attach by Process ID (PID)
sudo strace -p 1337

# Attach to process and follow all newly spawned threads
sudo strace -f -p 1337 -o attached.log
```
### Раскрытие строк и выводов буфера (`-s`)
По умолчанию strace обрезает напечатанные строки до 32 символов. Увеличьте длину строки, чтобы проверить полные пути к файлам, URL-адреса или полезные данные:
```bash
# Display up to 2048 characters of strings and buffers
strace -s 2048 -e trace=read,write ./target_binary
```
### Статистическое профилирование и анализ узких мест (`-c`)
Подсчитайте затраченное время, количество вызовов и ошибок на системный вызов:
```bash
strace -c ./target_binary
```
*Пример сводки вывода:*
```text
% time     seconds  usecs/call     calls    errors syscall
------ ----------- ----------- --------- --------- ----------------
 54.32    0.001200          40        30           read
 32.10    0.000710          71        10         2 openat
 13.58    0.000300          15        20           write
```
### Внесение ошибок для фаззинга и тестирования (`-e fault`)
Вносите произвольные ошибки в системные вызовы для оценки устойчивости:
```bash
# Force the 3rd invocation of openat() to fail with Permission Denied (EACCES)
strace -e fault=openat:error=EACCES:when=3 ./target_binary
```
---

## 🧩 ltrace: отслеживание вызовов динамической библиотеки

### Базовый перехват библиотеки
Перехват вызовов, сделанных к динамическим библиотекам `.so`:
```bash
ltrace ./target_binary
```
### Взлом жестко запрограммированных проверок паролей (`strcmp`)
При реверс-инжиниринге CTF и двоичных файлах вызовов функции сравнения паролей (`strcmp`, `strncmp`, `memcmp`) выявляют целевой пароль в виде аргументов открытого текста:
```bash
ltrace -s 128 -e strcmp,strncmp ./license_check
```
*Пример вывода:*
```text
strcmp("user_input", "SUPER_SECRET_FLAG_2026") = -1
```
### Демонтаж символов C++ (`-C`)
Устранить искаженные компилятором имена методов C++ (`_ZNK4Test3fooEv` -> `Test::foo()`):
```bash
ltrace -C ./cpp_binary
```
### Фильтрация определенных библиотечных функций
```bash
# Intercept only memory allocation and string functions
ltrace -e malloc+free+strdup ./target_binary

# Intercept calls originating from specific dynamic libraries
ltrace -l /lib/x86_64-linux-gnu/libcrypto.so.3 ./target_binary
```
---

## 🔄 Объединение рабочих процессов strace и ltrace

При выполнении первоначальной сортировки неизвестного двоичного файла:

1. **Шаг 1.** Запустите `strace -e trace=file,network ./binary`, чтобы увидеть, с какими файлами и сокетами он взаимодействует.
2. **Шаг 2.** Запустите `ltrace -s 256 ./binary`, чтобы обнаружить сравнения строк внутренней библиотеки, криптовызовы и логику памяти.
3. **Шаг 3.** Объедините оба инструмента:
   ```bash
   ltrace -S ./binary
   ```
*(Флаг `-S` указывает ltrace отображать системные вызовы вместе с библиотечными вызовами в одном унифицированном представлении).*

---

## ⚡ Краткая справочная таблица

| Цель | Команда |
|-----------|---------|
| **Отслеживать все системные вызовы** | `strace ./binary` |
| **Войти в файл** | `strace -o log.txt ./binary` |
| **Отслеживание операций с файлами** | `strace -e trace=file ./binary` |
| **Отслеживание сетевой активности** | `strace -e trace=network ./binary` |
| **Следить за дочерними процессами** | `strace -f ./binary` |
| **Присоединиться к работающему PID** | `sudo strace -f -p <PID>` |
| **Увеличить длину строки** | `strace -s 2048 ./binary` |
| **Сводная статистика системных вызовов** | `strace -c ./binary` |
| **Отслеживание вызовов библиотеки** | `ltrace ./binary` |
| **Получить проверку пароля** | `ltrace -s 128 -e strcmp,strncmp ./binary` |
| **Устранение имен C++** | `ltrace -C ./binary` |
| **Комбинация библиотеки + системный вызов**| `ltrace -S ./binary` |

---

## 🛡️ Соображения по защите и борьбе с отслеживанием

1. **Самопроверка Ptrace:** Linux позволяет подключать к процессу через `ptrace` только один трассировщик. Вызов `ptrace(PTRACE_TRACEME, 0, 1, 0)` не ​​удастся, если `strace` или `ltrace` уже подключены.
2. **Статическое связывание (`-static`):** Двоичные файлы, скомпилированные с использованием статического связывания, не содержат ссылок на динамические библиотеки (`DT_NEEDED`), что делает `ltrace` совершенно неэффективным (вместо этого используйте `strace`).

---

## ⚠️ Юридическая оговорка

> [!ВНИМАНИЕ]
> strace и ltrace — это инструменты динамической диагностики, предназначенные для разработки программного обеспечения, отладки и авторизованных оценок безопасности. Отслеживание несанкционированных процессов или перехват конфиденциальной информации без разрешения строго запрещено.