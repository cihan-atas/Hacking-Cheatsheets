<!-- language-home-navigation -->
[🏠 Русская главная страница](../../README.ru.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🛰️ sshuttle — Полная шпаргалка

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ███████╗███████╗██╗  ██╗██╗   ██╗████████╗████████╗██╗     ███████╗
  ██╔════╝██╔════╝██║  ██║██║   ██║╚══██╔══╝╚══██╔══╝██║     ██╔════╝
  ███████╗███████╗███████║██║   ██║   ██║      ██║   ██║     █████╗  
  ╚════██║╚════██║██╔══██║██║   ██║   ██║      ██║   ██║     ██╔══╝  
  ███████║███████║██║  ██║╚██████╔╝   ██║      ██║   ███████╗███████╗
  ╚══════╝╚══════╝╚═╝  ╚═╝ ╚═════╝    ╚═╝      ╚═╝   ╚══════╝╚══════╝
       Transparent Proxy Server that Works as a Poor Man's VPN over SSH
```
<p выравнивание="центр">
  <img src="https://img.shields.io/badge/sshuttle-Poor_Man's_VPN-blue?style=for-the-badge" alt="sshuttle">
  <img src="https://img.shields.io/badge/Category-Pivoting_&_Tunneling-orange?style=for-the-badge" alt="Поворот и туннелирование">
  <img src="https://img.shields.io/badge/Protocols-TCP_DNS_SSH-green?style=for-the-badge" alt="Протоколы">
  <img src="https://img.shields.io/badge/License-LGPLv2.1-red?style=for-the-badge" alt="Лицензия LGPLv2.1">
</p>

<p выравнивание="центр">
  <b>🌐 Перенаправлять все внутренние подсети и DNS-запросы через непривилегированный сеанс SSH без административного доступа на удаленном сервере</b>
</p>

---

## 📋 Содержание

- [Обзор и философия «VPN для бедных»](#-overview--the-poor-mans-vpn-philosophy)
- [Как работает шаттл](#-how-ssshuttle-works)
- [Сравнение инструментов: sshuttle, SOCKS, Ligolo-ng и OpenVPN](#-tool-comparison-sshuttle-vs-socks-vs-ligolo-ng-vs-openvpn)
- [Установка](#-установка)
- [Базовая маршрутизация подсети](#-basic-subnet-routing)
- [Пересылка DNS-трафика (`--dns`)](#-dns-traffic-forwarding---dns)
- [Полное перенаправление шлюза (маршрут по умолчанию)](#-full-gateway-redirection-default-route)
- [Исключения подсети (`-x`)](#-subnet-exclusions--x)
- [Параметры SSH и аутентификация на основе ключей (`-e`)](#-ssh-options--key-based-authentication--e)
- [Фоновое выполнение демона (`-D`)](#-daemon-background-execution--d)
- [Краткая справочная таблица](#-краткая справочная-таблица)
- [Защитные соображения и OPSEC](#-защитные-соображения--opsec)
- [Юридическая оговорка](#-юридическая оговорка)

---

## 🎯 Обзор и философия «VPN для бедняков»

**sshuttle** — это прозрачный прокси-сервер, который работает как облегченный VPN без настройки через стандартное SSH-соединение.

В типичном корпоративном тестировании на проникновение и оценках Red Team внедрение во внутренние сети затруднено строгими ограничениями:
- ❌ У вас редко есть доступ `root` или `SYSTEM` на начальном бастионе / опорном хосте SSH.
- ❌ Традиционные решения VPN (OpenVPN, WireGuard) требуют прав администратора и виртуальных сетевых устройств TUN/TAP на обеих конечных точках.
- ❌ Стандартные прокси-серверы SOCKS (например, `ssh -D`) требуют настройки `proxychains` или индивидуальных настроек прокси-сервера приложения для каждого отдельного инструмента.

**sshuttle устраняет все три ограничения одновременно:**
- ✅ **Удаленный root-доступ не требуется:** Для сводного сервера требуется только стандартная установка Python (`python3`).
- ✅ **Удаленный TUN/TAP не требуется:** использует чистое мультиплексирование потоков TCP в сеансе SSH.
- ✅ **Прозрачная маршрутизация в масштабах всей системы:** изменяет локальные `iptables` / `nftables` на компьютере злоумышленника, позволяя инструментам (`nmap`, `curl`, `smbclient`, веб-браузерам) нацеливаться на внутренние IP-адреса напрямую без оберток!

---

## ⚙️ Как работает шаттл
```
[ Attacker Box (Local Root) ]
  ├── iptables / nftables redirects internal IP traffic (10.10.10.0/24) to local port
  └── sshuttle client multiplexes TCP packets over SSH tunnel
              │
              ▼ (Encrypted SSH Channel - Port 22)
[ Remote Pivot Server (Unprivileged User + Python) ]
  └── Assembles TCP connections and forwards them directly to internal hosts
              │
              ▼
[ Target Internal Network (10.10.10.0/24) ]
```
---

## ⚖️ Сравнение инструментов: sshuttle, SOCKS, Ligolo-ng и OpenVPN

| Особенность | `sshuttle` | НОСКИ (`ssh -D`) | `Ligolo-ng` | `OpenVPN` |
|---------|------------|------------------|-------------|-----------|
| **Требуется удаленный root-доступ** | ❌ Нет | ❌ Нет | ❌ Нет | ✅ Да |
| **Требуется локальный root** | ✅ Да (iptables) | ❌ Нет | ✅ Да (интерфейс TUN) | ✅ Да |
| **Прозрачная маршрутизация** | ✅ Да | ❌ Требуются проксичейны | ✅ Да (Родной TUN) | ✅ Да |
| **Поддержка UDP-трафика** | ❌ Только DNS | ❌ Нет | ✅ Да | ✅ Полный UDP |
| **Требуется наличие Python**| ✅ Да (Python 3) | ❌ Нет | ❌ Нет (Автономный агент)| ❌ Нет |
| **Сканирование Nmap SYN (`-sS`)** | ❌ TCP-соединение (`-sT`) | ❌ Только TCP-соединение | ✅ Поддержка необработанных пакетов | ✅ Полные сырые пакеты |

---

## 📥 Установка
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y sshuttle

# Arch Linux
sudo pacman -S sshuttle

# macOS (Homebrew)
brew install sshuttle

# Python Pipx / Pip
pipx install sshuttle
```
---

## 🚀 Базовая маршрутизация подсети

Перенаправьте всю удаленную подсеть класса C через опорный хост SSH:
```bash
# Route 10.10.10.0/24 through user@192.168.1.50
sshuttle -r user@192.168.1.50 10.10.10.0/24

# Route multiple disjoint subnets simultaneously
sshuttle -r user@192.168.1.50 10.10.10.0/24 172.16.0.0/16 192.168.100.0/24
```
После подключения вы можете напрямую общаться с внутренними машинами:
```bash
# No proxychains needed!
curl http://10.10.10.25:8080/
smbclient -L //10.10.10.10/ -U 'domain/user%pass'
```
---

## 🌐 Переадресация DNS-трафика (`--dns`)

Перенаправить внутренние корпоративные DNS-запросы (`.corp`, `.local`) на внутренний DNS-сервер удаленной сети:
```bash
# Route subnet and proxy internal DNS queries
sshuttle --dns -r user@192.168.1.50 10.10.10.0/24

# Query internal domain directly via nslookup/dig
dig dc01.internal.corp
```
---

## 🌍 Полное перенаправление шлюза (маршрут по умолчанию)

Направьте весь исходящий трафик злоумышленников через соединение SSH (полное поведение VPN):
```bash
# Route all traffic through remote SSH server (0.0.0.0/0)
sshuttle -r user@remote-vps.com 0.0.0.0/0

# Route all traffic except local network
sshuttle -r user@remote-vps.com 0.0.0.0/0 -x 192.168.1.0/24
```
---

## 🚫 Исключения подсети (`-x`)

Предотвратите захват определенных конфиденциальных или кольцевых подсетей:
```bash
# Route entire 10.0.0.0/8 network, but exclude the pivot host itself
sshuttle -r user@10.0.0.50 10.0.0.0/8 -x 10.0.0.50/32
```
---

## 🔑 Параметры SSH и аутентификация на основе ключей (`-e`)

Передайте пользовательские флаги SSH (закрытые ключи идентификации, нестандартные порты SSH):
```bash
# Use custom identity file and non-standard SSH port 2222
sshuttle -e 'ssh -i /path/to/id_rsa -p 2222' -r user@192.168.1.50 10.10.10.0/24
```
---

## 🤖 Фоновое выполнение демона (`-D`)

Запускайте sshuttle в качестве фонового демона во время текущих сражений:
```bash
# Start background daemon with pidfile
sudo sshuttle -D --pidfile /tmp/sshuttle.pid -r user@192.168.1.50 10.10.10.0/24

# Terminate daemon when finished
sudo kill $(cat /tmp/sshuttle.pid)
```
---

## ⚡ Краткая справочная таблица

| Цель | Команда |
|-----------|---------|
| **Маршрут для одной подсети** | `sshuttle -r <USER>@<PIVOT> <SUBNET>/24` |
| **Подсеть маршрутизации с DNS** | `sshuttle --dns -r <USER>@<PIVOT> <SUBNET>/24` |
| **Маршрутизация нескольких подсетей** | `sshuttle -r <USER>@<PIVOT> <NET1>/24 <NET2>/16` |
| **Исключить IP/подсеть** | `sshuttle -r <USER>@<PIVOT> <SUBNET>/24 -x <EXCLUDE_IP>` |
| **Полное перенаправление трафика** | `sshuttle -r <USER>@<PIVOT> 0.0.0.0/0` |
| **Пользовательский SSH-ключ/порт** | `sshuttle -e 'ssh -i <KEY> -p <PORT>' -r <USER>@<PIVOT> <SUBNET>` |
| **Запуск в фоновом режиме** | `sshuttle -D --pidfile /tmp/sshuttle.pid -r <USER>@<PIVOT> <SUBNET>` |

---

## 🛡️ Оборонительные соображения и OPSEC

1. **Модификация брандмауэра:** sshuttle изменяет локальные таблицы брандмауэра (`iptables` в Linux, `pf` в macOS). Если sshuttle аварийно завершает работу, используйте `iptables -F` или перезапустите службу брандмауэра, чтобы очистить устаревшие правила перенаправления.
2. **Ограничение только для TCP:** UDP-пакеты, не относящиеся к DNS, эхо-запросы ICMP (ping) и сканирование SYN не поддерживаются. Всегда используйте `nmap -sT -Pn -n` для сканирования портов через sshuttle.
3. **Целевой объем:** sshuttle автоматически загружает небольшой скрипт сборки Python на удаленный основной хост во время инициализации сеанса и запускает его через `python3`.

---

## ⚠️ Юридическая оговорка

> [!ВНИМАНИЕ]
> Эта шпаргалка предназначена только для авторизованного тестирования на проникновение и системного администрирования. Несанкционированная маршрутизация и перехват сетевого трафика являются противозаконными.