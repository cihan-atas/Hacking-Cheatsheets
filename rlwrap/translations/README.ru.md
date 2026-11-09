<!-- language-home-navigation -->
[🏠 Русская главная страница](../../README.ru.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ⌨️ rlwrap — Полная шпаргалка

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ██████╗ ██╗     ██╗    ██╗██████╗  █████╗ ██████╗ 
  ██╔══██╗██║     ██║    ██║██╔══██╗██╔══██╗██╔══██╗
  ██████╔╝██║     ██║ █╗ ██║██████╔╝███████║██████╔╝
  ██╔══██╗██║     ██║███╗██║██╔══██╗██╔══██║██╔═══╝ 
  ██║  ██║███████╗╚███╔███╔╝██║  ██║██║  ██║██║     
  ╚═╝  ╚═╝╚══════╝ ╚══╝╚══╝ ╚═╝  ╚═╝╚═╝  ╚═╝╚═╝     
       Readline Wrapper for Reverse Shells & Dumb Terminals
```
<p выравнивание="центр">
  <img src="https://img.shields.io/badge/rlwrap-Readline_Wrapper-blue?style=for-the-badge" alt="rlwrap">
  <img src="https://img.shields.io/badge/Utility-Shell_Stabilization-green?style=for-the-badge" alt="Стабилизация оболочки">
  <img src="https://img.shields.io/badge/Category-Exploitation_&_Shells-orange?style=for-the-badge" alt="Эксплуатация и снаряды">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="Лицензия GPLv2">
</p>

<p выравнивание="центр">
  <b>🌐 Превратите необработанные прослушиватели Netcat и простые обратные оболочки в полноценные интерфейсы командной строки с клавишами со стрелками, историей и автозаполнением.</b>
</p>

---

## 📋 Содержание

- [Введение: проблема «тупой оболочки»](#-введение-проблема-тупой-оболочки)
- [Установка](#-установка)
- [Основное использование](#-базовое-использование)
- [Ловить обратные снаряды (`rlwrap + nc`)](#-catching-reverse-shells-rlwrap--nc)
- [Постоянное управление историей](#-persistent-history-management)
- [Автозаполнение команды (`-f`)](#-команда-автозаполнение--f)
- [Полный 5-этапный процесс обновления оболочки TTY](#-full-5-step-tty-shell-upgrade-workflow)
- [Использование rlwrap с другими инструментами для пентеста](#-using-rlwrap-with-other-pentest-tools)
  - [Oracle SQL*Plus](#oracle-sqlplus)
  - [Redis и Telnet](#redis--telnet)
  - [Socat и пользовательские сокеты](#socat--custom-sockets)
- [Краткая справочная таблица](#-краткая справочная-таблица)
- [Советы и рекомендации](#-tips--best-practices)
- [Юридическая оговорка](#-юридическая оговорка)

---

## 🎯 Введение: проблема «тупой оболочки»

Когда вы ловите обратную оболочку с помощью традиционных инструментов, таких как Netcat (`nc -lvnp 4444`), вы получаете **тупой терминал**. В тупом терминале отсутствуют стандартные возможности редактирования строк:

- ❌ Нажатие клавиш со стрелками вверх/вниз** печатает escape-последовательности, такие как `^[[A` или `^[[B`, вместо циклического просмотра истории команд.
- ❌ При нажатии **Backspace** или **Delete** вместо удаления символов печатается `^H` или `^?`.
- ❌ Нажатие **Ctrl + C** немедленно убивает прослушиватель обратной оболочки, теряя с трудом заработанный доступ.
- ❌ Такие команды, как `su`, `sudo`, `nano`, `vim` или `passwd`, завершаются неудачно, поскольку к ним не подключен псевдотерминал (PTY).
- ❌ Никакая постоянная история команд не сохраняется между сеансами.

**rlwrap** (Readline Wrapper, написанный Хансом Лубом) решает эту проблему, располагаясь между вашим терминалом и целевой командой. Он перехватывает нажатия клавиш, использует **GNU Readline** и обеспечивает навигацию с помощью клавиш со стрелками, редактирование строк, постоянные файлы истории команд и автозаполнение на основе словаря!

---

## 📥 Установка

`rlwrap` — легкий и доступный в репозиториях пакетов по умолчанию практически каждого дистрибутива Linux:

### Kali Linux, Debian и Ubuntu
```bash
# Update and install rlwrap
sudo apt update && sudo apt install -y rlwrap

# Verify installation and version
rlwrap --version
```
### Fedora, CentOS и RHEL
```bash
# Fedora / CentOS / RHEL
sudo dnf install -y rlwrap || sudo yum install -y epel-release && sudo yum install -y rlwrap
```
### Арч Линукс
```bash
# Arch Linux / Manjaro
sudo pacman -S rlwrap
```
### macOS (доморощенный)
```bash
# macOS
brew install rlwrap
```
---

## 🚀 Базовое использование

Основной синтаксис — это просто перенос любой командной строки с помощью `rlwrap`:
```bash
# General syntax
rlwrap [options] <command> [command-arguments]
```
### Общие флаги

| Флаг | Описание |
|------|-------------|
| `-c` | Включить завершение имени файла и пути |
| `-A` | Игнорировать escape-коды форматирования подсказок (предотвращает сбои в строке подсказок) |
| `-r` | Запомните все слова, введенные при вводе, и поместите их в список слов завершения |
| `-H <file>` | Укажите специальный файл истории для сохранения и вызова команд |
| `-s <num>` | Установить максимальное количество строк истории (по умолчанию: 300) |
| `-f <file>` | Загрузить файл пользовательского словаря завершения |
| `-b <chars>` | Определение символов разрыва слов |
| `-m` | Включить редактирование многострочного ввода |
| `-p[color]` | Раскрасить командную строку |

---

## 🎣 Ловля обратных снарядов (`rlwrap + nc`)

Наиболее распространенным вариантом использования при наступательной безопасности является упаковка прослушивателя Netcat (полные варианты и флаги прослушивателя см. в [Шпаргалке по Netcat](../Netcat/README.md):
```bash
# Basic Netcat listener with arrow-key editing & history
rlwrap nc -lvnp 4444

# Professional pentester flags: -c (file completion), -A (prompt fix), -r (remember words)
rlwrap -cAr nc -lvnp 4444
```
### Объяснение флагов `-cAr`:

1. **`-c` (дополнение файла):** позволяет использовать `<TAB>` для автоматического заполнения имен файлов и каталогов.
2. **`-A` (Подсказки):** Предотвращает странные переносы строк и дублирование подсказок, когда удаленный сервер отправляет нестандартные строки подсказок (например, `root@victim:/#`).
3. **`-r` (Запомнить слова):** запоминает слова, которые вы вводите в оболочку, чтобы вы могли позже заполнить их с помощью табуляции.

---

## 💾 Постоянное управление историей

По умолчанию `rlwrap` сохраняет историю команд в `~/.<command>_history`. Вы можете настроить собственные файлы истории для каждого взаимодействия:
```bash
# Save history to a dedicated engagement file
rlwrap -H ~/.history_target_corp nc -lvnp 4444

# Increase history size to 5,000 entries
rlwrap -H ~/.history_target -s 5000 nc -lvnp 4444

# Case-insensitive history search
rlwrap -i nc -lvnp 4444
```
---

## 🔍 Автозаполнение команд (`-f`)

Вы можете предоставить `rlwrap` текстовый файл, содержащий общие команды Linux или Windows, и он автоматически заполнит их при нажатии `<TAB>`:
```bash
# Create a wordlist of essential post-exploitation commands
cat << 'EOF' > /tmp/pentest_commands.txt
whoami
id
hostname
uname -a
cat /etc/passwd
cat /etc/shadow
sudo -l
find / -perm -4000 2>/dev/null
netstat -tulnp
ss -tulnp
ps aux
ip a
route -n
crontab -l
EOF

# Start listener with custom tab-completion dictionary
rlwrap -f /tmp/pentest_commands.txt nc -lvnp 4444
```
---

## 🛠️ Полный 5-этапный процесс обновления оболочки TTY

Хотя `rlwrap` обеспечивает немедленную навигацию и историю с помощью клавиш со стрелками, некоторые интерактивные программы (`su`, `sudo`, `vim`) по-прежнему требуют полностью выделенного терминала TTY. Вот стандартный пятиэтапный конвейер обновления:

### Шаг 1. Запустите прослушиватель с помощью rlwrap
```bash
# Attacker machine: start enhanced listener
rlwrap -cAr nc -lvnp 4444
```
### Шаг 2: Запустите обратную оболочку и создайте PTY

Как только ваша обратная оболочка подключится к вашему слушателю, создайте псевдотерминал на цели:
```bash
# Target shell: Spawn PTY via Python
python3 -c 'import pty; pty.spawn("/bin/bash")'

# OR via script if Python is unavailable:
/usr/bin/script -qc /bin/bash /dev/null
```
### Шаг 3: Фон оболочки
```bash
# Press Ctrl + Z to put the reverse shell into the background
[1]+  Stopped                 rlwrap -cAr nc -lvnp 4444
```
### Шаг 4. Настройка режима Raw локального терминала
```bash
# Attacker machine: disable local echo and enable raw keystrokes
stty raw -echo; fg
```
*(Нажмите Enter дважды после запуска `fg`, чтобы восстановить приглашение)*

### Шаг 5: Сброс и установка размеров терминала
```bash
# Target shell: set terminal type and environment
reset
export TERM=xterm-256color
export SHELL=bash

# Check your attacker terminal size in another window:
# $ stty size  -> Output: 38 160

# Apply rows and columns to target shell:
stty rows 38 columns 160
```
> ✅ **Результат:** Теперь у вас есть неразрушимая полнофункциональная интерактивная оболочка bash с завершением табуляции, чистым экраном (`Ctrl+L`), обработкой сигналов (`Ctrl+C` не ​​отключается) и полными размерами терминала!

---

## 🔧 Использование rlwrap с другими инструментами пентеста

`rlwrap` не ограничивается Netcat. Любой инструмент CLI, в котором отсутствует редактирование строк, выигрывает от этого:

### Oracle SQL*Plus
```bash
# Oracle's sqlplus utility notoriously lacks line editing and history on Linux
rlwrap sqlplus scott/tiger@10.10.10.50:1521/ORCL
```
### Redis и Telnet
```bash
# Redis CLI interactive console
rlwrap redis-cli -h 10.10.10.50 -p 6379

# Raw Telnet or socket interaction
rlwrap telnet 10.10.10.50 25
```
### Socat и пользовательские сокеты
```bash
# Socat listener with rlwrap
rlwrap socat file:`tty`,raw,echo=0 tcp-listen:4444

# Connecting to raw sockets / CTF pwn challenges
rlwrap nc ctf.challenge.local 1337
```
---

## ⚡ Краткая справочная таблица

| Задача | Команда |
|------|---------|
| **Улучшенный прослушиватель Netcat** | `rlwrap nc -lvnp 4444` |
| **Флаги полного файла и подсказки** | `rlwrap -cAr nc -lvnp 4444` |
| **Отдельный файл истории** | `rlwrap -H ~/.nc_history nc -lvnp 4444` |
| **Пользовательское завершение табуляции** | `rlwrap -f /path/to/wordlist.txt nc -lvnp 4444` |
| **Увеличить размер истории** | `rlwrap -s 10000 nc -lvnp 4444` |
| **Обертка Oracle SQLPlus** | `rlwrap sqlplus user/pass@host/db` |
| **Обернуть интерфейс командной строки Redis** | `rlwrap redis-cli -h 10.10.10.50` |
| **Прослушиватель Wrap Socat** | `rlwrap socat TCP-LISTEN:4444,reuseaddr -` |

---

## 💡 Советы и рекомендации

1. **Создайте псевдоним Bash:** добавьте его в свой `~/.bashrc` или `~/.zshrc`, чтобы никогда не забывать его использовать:
   ```bash
   alias nc='rlwrap -cAr nc'
   ```
2. **Никогда не пропустите безопасность `Ctrl+C`:** При обнаружении нестабильных оболочек `rlwrap` перехватывает основные сбои клавиатуры, но всегда не забывайте выполнять 5-этапное обновление `stty raw -echo; fg`, если вы планируете выполнять длинные интерактивные команды.
3. **Изолируйте истории клиентов.** При тестировании нескольких клиентов или компьютеров всегда используйте `-H ~/.client_name.hist` для ведения отдельных журналов аудита и истории команд.

---

## ⚠️ Юридическая оговорка

> **ВАЖНО:** Обратные оболочки и инструменты пост-эксплуатации должны использоваться только в авторизованных тестах на проникновение, в мероприятиях красной команды и в образовательных лабораториях только с явного письменного согласия.