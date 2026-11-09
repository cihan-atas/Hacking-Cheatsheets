<!-- language-home-navigation -->
[🏠 Русская главная страница](../../README.ru.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ⚡ Masscan — полная шпаргалка

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ███╗   ███╗ █████╗ ███████╗███████╗ ██████╗ █████╗ ███╗   ██╗
  ████╗ ████║██╔══██╗██╔════╝██╔════╝██╔════╝██╔══██╗████╗  ██║
  ██╔████╔██║███████║███████╗███████╗██║     ███████║██╔██╗ ██║
  ██║╚██╔╝██║██╔══██║╚════██║╚════██║██║     ██╔══██║██║╚██╗██║
  ██║ ╚═╝ ██║██║  ██║███████║███████║╚██████╗██║  ██║██║ ╚████║
  ╚═╝     ╚═╝╚═╝  ╚═╝╚══════╝╚══════╝ ╚═════╝╚═╝  ╚═╝╚═╝  ╚═══╝
       The Fastest Internet Port Scanner - Asynchronous 10M Packets/Second
```
<p выравнивание="центр">
  <img src="https://img.shields.io/badge/Masscan-Internet_Port_Scanner-blue?style=for-the-badge" alt="Masscan">
  <img src="https://img.shields.io/badge/Language-C-orange?style=for-the-badge" alt="Язык C">
  <img src="https://img.shields.io/badge/Category-Recon_&_Scanning-green?style=for-the-badge" alt="Разведка и сканирование">
  <img src="https://img.shields.io/badge/License-AGPLv3-red?style=for-the-badge" alt="Лицензия AGPLv3">
</p>

<p выравнивание="центр">
  <b>🌐 Высокопроизводительный асинхронный сканер TCP-портов, способный сканировать весь Интернет IPv4 менее чем за 5 минут</b>
</p>

---

## 📋 Содержание

- [Обзор и асинхронная архитектура](#-overview--asynchronous-architecture)
- [Сравнение инструментов: Masscan, Nmap и RustScan](#-tool-comparison-masscan-vs-nmap-vs-rustscan)
- [Установка](#-установка)
- [Основное использование и цели сканирования](#-basic-usage--scanning-targets)
- [Контроль скорости передачи (`--rate`)](#-управление-скоростью-передачи---скорость)
- [Спецификации портов (TCP и UDP)](#-port-спецификации-tcp--udp)
- [Захват баннеров и базовое зондирование протокола](#-banner-grabbing--basic-protocol-probing)
- [Целевые исключения и черные списки (`--excludefile`)](#-target-exclusions--blacklists---excludefile)
- [Форматы вывода и передача в Nmap](#-output-formats--piping-into-nmap)
- [Краткая справочная таблица](#-краткая справочная-таблица)
- [Защитные соображения и аппаратные ограничения](#-защитные-соображения--аппаратные ограничения)
- [Юридическая оговорка](#-юридическая оговорка)

---

## 🎯 Обзор и асинхронная архитектура

**Masscan**, созданный **Робертом Грэмом**, считается самым быстрым из существующих сканеров портов. Он дает результаты, аналогичные Nmap, но внутренне функционирует как `scanrand`, `unicornscan` и `ZMap`, используя **асинхронную передачу**.

В отличие от Nmap, который использует стек TCP ядра операционной системы и ожидает ответов, прежде чем проверять последующие порты, Masscan включает свой собственный стек TCP/IP в пользовательском пространстве. Он передает необработанные пакеты SYN со скоростью, превышающей **10 миллионов пакетов в секунду**, без ведения таблиц состояний соединений.

---

## ⚖️ Сравнение инструментов: Masscan, Nmap и RustScan

| Особенность | Масскан | Нмап | РустСкан |
|---------|---------|------|----------|
| **Максимальная скорость передачи пакетов** | 10 000 000 пикселей/сек | ~1000 пикс/сек | Ограничение сокетов ОС |
| **Все сканирование Интернета (/0)**| ✅ Менее 5 минут | ❌ Дни/Недели | ❌ Не предназначено для /0 |
| **Реализация стека TCP**| Пользовательское пространство Raw SYN | Ядро ОС/необработанные сокеты | Асинхронные TCP-сокеты Токио |
| **Глубокая версия и сценарии NSE**| ❌ Примитивные баннеры | ✅ 600+ скриптов NSE | ✅ Через передачу обслуживания Nmap |
| **Отслеживание состояния** | Без гражданства (Blackrock PRNG) | Пул соединений с отслеживанием состояния | Асинхронные задачи с отслеживанием состояния |
| **Идеальный целевой объем** | Блоки класса A/B (/8, /16)| Отдельные цели и /24 подсети | Отдельные машины и /24 подсети |

---

## 📥 Установка
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y masscan

# Arch Linux
sudo pacman -S masscan

# Build from source (Latest GitHub version)
sudo apt install -y git gcc make libpcap-dev
git clone https://github.com/robertdavidgraham/masscan
cd masscan && make -j$(nproc)
sudo make install
```
---

## 🚀 Базовое использование и цели сканирования
```bash
# Scan a single IP for top web ports
sudo masscan 10.10.10.50 -p80,443,8080 --rate 1000

# Scan an entire Class B internal subnet (172.16.0.0/16) for SMB port 445
sudo masscan 172.16.0.0/16 -p445 --rate 5000

# Scan all 65,535 ports on a specific IP
sudo masscan 10.10.10.50 -p1-65535 --rate 2000
```
> [!ВАЖНО]
> Поскольку Masscan генерирует необработанные пакеты через `libpcap`, его **необходимо запускать с правами root/sudo**.

---

## ⚡ Управление скоростью передачи (`--rate`)

Параметр `--rate` определяет, сколько пакетов в секунду передает Masscan. Калибровка имеет решающее значение, чтобы избежать перегрузки сетевого оборудования:
```bash
# Gentle rate for home routers / WiFi (100–500 pkts/sec)
sudo masscan 10.10.10.0/24 -p1-1000 --rate 300

# Standard corporate gigabit internal network rate (5,000–10,000 pkts/sec)
sudo masscan 10.0.0.0/8 -p80,443 --rate 10000

# High-speed data center 10Gbps uplink rate (100,000+ pkts/sec)
sudo masscan 0.0.0.0/0 -p443 --rate 100000
```
---

## 🔌 Спецификации портов (TCP и UDP)

Masscan поддерживает списки портов TCP и UDP одновременно:
```bash
# Specific TCP ports
sudo masscan 10.10.10.50 -p21,22,80,443,445,3389

# Top ports shortcut (similar to Nmap)
sudo masscan 10.10.10.50 --top-ports 100

# UDP ports (prefixed with U:)
sudo masscan 10.10.10.50 -p U:53,U:161,U:123 --rate 500
```
---

## 🔍 Захват баннеров и базовое зондирование протоколов

Хотя Masscan в первую очередь является сканером SYN, он может выполнять трехэтапное рукопожатие для захвата баннеров приложений:
```bash
# Grab HTTP, FTP, and SSH banners
sudo masscan 10.10.10.0/24 -p80,21,22 --banners --rate 1000
```
---

## 🚫 Целевые исключения и черные списки (`--excludefile`)

При проведении широкомасштабных тестов на проникновение всегда исключайте критическую инфраструктуру и запрещенные адреса:
```bash
# Exclude government and military ranges or local gateway
sudo masscan 10.0.0.0/8 -p80 --rate 2000 --exclude 10.0.0.1,10.1.1.1

# Use an exclusion file (e.g., standard exclude.conf)
sudo masscan 192.168.0.0/16 -p445 --rate 2000 --excludefile /etc/masscan/exclude.conf
```
---

## 📊 Форматы вывода и передача в Nmap

Сохраните обнаруженные хосты в форматах greppable или JSON, а затем передайте их в Nmap для полной оценки уязвимостей:
```bash
# Save to JSON format
sudo masscan 10.10.10.0/24 -p80,443 -oJ masscan_results.json

# Save to simple List format (-oL)
sudo masscan 10.10.10.0/24 -p1-65535 --rate 2000 -oL open_ports.txt

# Extract discovered ports and feed directly to Nmap
cat open_ports.txt | awk '{print $4}' | sort -u | paste -sd, - | xargs -I{} nmap -sC -sV -p{} 10.10.10.50
```
---

## ⚡ Краткая справочная таблица

| Цель | Команда |
|-----------|---------|
| **Сканировать один хост (все порты)** | `sudo masscan <IP> -p1-65535 --rate 2000` |
| **Сканировать подсеть CIDR на наличие порта** | `sudo masscan <SUBNET>/16 -p<PORT> --rate 5000` |
| **Сканирование с захватом баннера** | `sudo masscan <IP> -p80,22 --banners --rate 1000` |
| **Сканировать UDP-порты** | `sudo masscan <IP> -p U:53,U:161 --rate 500` |
| **Исключить IP-адреса** | `sudo masscan <NET> -p80 --exclude <IP1>,<IP2>` |
| **Вывод в JSON** | `sudo masscan <IP> -p80,443 -oJ output.json` |
| **Вывод в простой список** | `sudo masscan <IP> -p80,443 -oL output.txt` |

---

## 🛡️ Соображения по защите и аппаратные ограничения

1. **Сбой потребительского маршрутизатора.** Установка `--rate 100000` на домашнем DSL/кабельном маршрутизаторе или стандартном Wi-Fi приведет к перегрузке таблицы состояний отслеживания соединений NAT и сбою маршрутизатора. Держите `--rate` ниже `1000` на подключениях к жилым домам.
2. **Потребление пропускной способности.** Передача со скоростью 10 миллионов пакетов в секунду требует около 5,4 Гбит/с пропускной способности сети.
3. **Отклонение локального брандмауэра (RST):** Поскольку Masscan использует собственный стек TCP в пользовательском пространстве, ваше ядро ​​Linux не знает об открытых соединениях и может отправлять автоматические пакеты TCP RST целевым объектам. Чтобы избежать этого во время захвата баннера:
   ```bash
   sudo iptables -A INPUT -p tcp --dport 60000 -j DROP
   sudo masscan ... --source-port 60000
   ```
---

## ⚠️ Юридическая оговорка

> [!ВНИМАНИЕ]
> Masscan — исключительно мощная утилита сетевой разведки. Сканирование неавторизованных сетей, государственных диапазонов IP-адресов или общедоступных подсетей Интернета может нарушать законы о телекоммуникациях и кибербезопасности. Всегда проверяйте письменное разрешение перед началом сканирования.