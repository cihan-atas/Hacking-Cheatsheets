<!-- language-home-navigation -->
[🏠 Русская главная страница](../../README.ru.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# Шпаргалка по Searchsploit

[Searchsploit](https://www.exploit-db.com/searchsploit) — официальная утилита командной строки для [Exploit-Database](https://www.exploit-db.com/). Оно позволяет специалистам по безопасности, тестерам на проникновение и исследователям уязвимостей проводить автономный поиск по десяткам тысяч опубликованных эксплойтов, шелл-кодов и технических документов без отправки сетевого трафика сторонним поисковым системам.

---

## Содержание

- [Обзор и архитектура](#overview--architecture)
- [Установка и обновления локальных баз данных](#installation--local-database-updates)
- [Базовый и расширенный поиск эксплойтов](#basic--advanced-exploit-searching)
- [Проверка и копирование эксплойтов (`-m` / `-p` / `-x`)](#examineing--copying-exploits--m---p---x)
- [Фильтрация по платформе и типу](#filtering-by-platform--type)
- [Интеграция сканирования Nmap XML/Grepable](#nmap-xml--grepable-scanning-integration)
- [Вывод JSON и автоматизация сценариев](#json-output--script-automation)
- [Лучшие практики в тестировании на проникновение](#best-practices-in-penetration-testing)

---

## Обзор и архитектура

Searchsploit запрашивает локальный git-клон Exploit-DB, хранящийся в `/usr/share/exploitdb/`.
- **Нулевой сетевой идентификатор:** поиск выполняется по локальным CSV-файлам и структурам каталогов.
- **Надежность в автономном режиме:** Полная функциональность в изолированных целевых средах и сетях взаимодействия.
- **Встроенное зеркалирование:** копирует файлы эксплойтов непосредственно в текущий рабочий каталог для немедленного просмотра и изменения.

---

## Установка и обновление локальной базы данных

### Debian / Ubuntu / Kali Linux / Parrot OS
```bash
# Kali and Parrot include searchsploit by default
sudo apt-get update && sudo apt-get install -y exploitdb

# Update the local exploit database archive
searchsploit -u
```
### Установка репозитория Git (любой дистрибутив Linux)
```bash
git clone https://gitlab.com/exploit-database/exploitdb.git /opt/exploitdb
sudo ln -sf /opt/exploitdb/searchsploit /usr/local/bin/searchsploit

# Check version and status
searchsploit -v
```
---

## Базовый и расширенный поиск эксплойтов

### 1. Простой поиск по ключевым словам
Найдите конкретные службы, демоны или программное обеспечение:
```bash
searchsploit apache 2.4.49
searchsploit openssh 7.2
searchsploit wordpress core 5.8
```
### 2. Точное соответствие заголовка (`-t`)
Строго ограничьте поисковые запросы, чтобы использовать заголовки для устранения шумных совпадений описаний:
```bash
searchsploit -t "Privilege Escalation" linux kernel
searchsploit -t "Remote Code Execution" smb
```
### 3. Строгое соответствие регистра и термина (`-e`)
Найдите точную версию и совпадения с шаблоном:
```bash
searchsploit -e "Apache Struts"
```
### 4. Поиск по идентификатору CVE
Эксплойты запросов, сопоставленные с конкретными номерами распространенных уязвимостей и подверженностей:
```bash
searchsploit --cve 2021-44228
searchsploit --cve 2017-0144
```
### 5. Исключение нежелательных результатов (`--exclude`)
Отфильтровывайте сценарии отказа в обслуживании (DoS) или вспомогательные сценарии при удаленном выполнении кода (RCE):
```bash
searchsploit linux kernel 5.4 --exclude="Denial of Service"
```
---

## Проверка и копирование эксплойтов (`-m` / `-p` / `-x`)

Никогда не редактируйте и не запускайте эксплойты непосредственно в каталоге `/usr/share/exploitdb/`. Используйте функции копирования и проверки Searchsploit.

### 1. Копировать (зеркально) эксплойт в текущий каталог (`-m`)
Скопируйте PoC-файл эксплойта (Python, C, Ruby, bash) непосредственно в локальный рабочий каталог:
```bash
# Mirror by Exploit-DB ID (EBD-ID)
searchsploit -m 50383

# Mirror by search path pattern
searchsploit -m windows/remote/48537.py
```
### 2. Проверьте метаданные эксплойта и полный путь (`-p`)
Отобразите абсолютный путь к файловой системе и URL-адрес Exploit-DB для EDB-ID:
```bash
searchsploit -p 50383
```
Выход:
```text
  Exploit: Apache HTTP Server 2.4.49 - Path Traversal & Remote Code Execution (RCE)
      URL: https://www.exploit-db.com/exploits/50383
     Path: /usr/share/exploitdb/exploits/multiple/webapps/50383.sh
File Type: Bourne-Again shell script, ASCII text executable
```
### 3. Прочитайте код эксплойта в пейджере терминала (`-x`)
Откройте код эксплойта непосредственно в `less` для быстрой проверки:
```bash
searchsploit -x 50383
```
---

## Фильтрация по платформе и типу

Searchsploit индексирует эксплойты, шелл-код и исследовательские работы на разных архитектурах:

### 1. Фильтрация по целевой операционной системе
```bash
# Search only Linux exploits
searchsploit linux kernel 4.4

# Search only Windows exploits
searchsploit windows smb eternal

# Search web applications
searchsploit webapps tomcat
```
### 2. Фильтровать только необработанные шеллкоды (`-s`)
Найдите полезные данные шеллкода, специфичные для архитектуры (например, execve `/bin/sh` без нулевых байтов):
```bash
searchsploit -s linux/x64 "execve"
searchsploit -s windows/x86 "reverse shell"
```
### 3. Фильтр документов и технических документов по безопасности (`-w`)
```bash
searchsploit -w "Active Directory Kerberos"
```
---

## Интеграция сканирования Nmap XML/Grepable

Searchsploit может автоматически анализировать результаты сканирования Nmap XML и сопоставлять обнаруженные версии программного обеспечения с известными эксплойтами:
```bash
# 1. Perform service detection and save XML output
nmap -sV -p- -T4 -oX /tmp/target_scan.xml 192.168.1.50

# 2. Feed the XML scan file directly to Searchsploit
searchsploit --nmap /tmp/target_scan.xml
```
*Searchsploit проверяет каждый баннер открытого порта и строку версии, запрашивая локальные эксплойты для немедленной сортировки.*

---

## Вывод JSON и автоматизация сценариев

Для автоматизации конвейера и пользовательских инструментов разведки результаты выводятся в структурированном формате JSON:
```bash
searchsploit "Apache Struts" --json > struts_exploits.json
```
Отфильтруйте пути к файлам эксплойтов с помощью `jq`:
```bash
searchsploit --cve 2021-41773 --json | jq -r '.RESULTS_EXPLOIT[].Path'
```
---

## Лучшие практики тестирования на проникновение

1. **Проверка перед запуском.** Всегда проверяйте исходный код эксплойта (`searchsploit -x <id>`) перед компиляцией или выполнением. Проверьте жестко закодированный шеллкод, целевые IP-адреса и убедитесь в отсутствии бэкдоров или вредоносных промежуточных модулей полезной нагрузки.
2. **Компилируйте с правильными флагами.** Многие эксплойты Exploit-DB C требуют определенных флагов GCC (например, `-m32`, `-lpthread` или `-lcrypto`):
   ```bash
   searchsploit -m 40839
   gcc -pthread 40839.c -o dirtycow -lcrypt
   ```
3. **Обновляйте базу данных.** Регулярно запускайте `searchsploit -u`, чтобы обеспечить локальную индексацию недавно опубликованных данных нулевого дня и доказательств концепции.