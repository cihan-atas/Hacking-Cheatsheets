<!-- language-home-navigation -->
[🏠 Русская главная страница](../../README.ru.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🦀 RustScan — Полная шпаргалка

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ██████╗ ██╗   ██╗███████╗████████╗███████╗ ██████╗ █████╗ ███╗   ██╗
  ██╔══██╗██║   ██║██╔════╝╚══██╔══╝██╔════╝██╔════╝██╔══██╗████╗  ██║
  ██████╔╝██║   ██║███████╗   ██║   ███████╗██║     ███████║██╔██╗ ██║
  ██╔══██╗██║   ██║╚════██║   ██║   ╚════██║██║     ██╔══██║██║╚██╗██║
  ██║  ██║╚██████╔╝███████║   ██║   ███████║╚██████╗██║  ██║██║ ╚████║
  ╚═╝  ╚═╝ ╚═════╝ ╚══════╝   ╚═╝   ╚══════╝ ╚═════╝╚═╝  ╚═╝╚═╝  ╚═══╝
       The Modern Port Scanner - Scan All 65k Ports in 3 Seconds into Nmap
```
<p выравнивание="центр">
  <img src="https://img.shields.io/badge/RustScan-Port_Scanner-blue?style=for-the-badge" alt="RustScan">
  <img src="https://img.shields.io/badge/Language-Rust-orange?style=for-the-badge" alt="Language Rust">
  <img src="https://img.shields.io/badge/Category-Recon_&_Scanning-green?style=for-the-badge" alt="Разведка и сканирование">
  <img src="https://img.shields.io/badge/License-GPLv3-red?style=for-the-badge" alt="Лицензия GPLv3">
</p>

<p выравнивание="центр">
  <b>🌐 Быстрый, настраиваемый и автоматизированный сканер портов, предназначенный для передачи обнаруженных открытых портов непосредственно в сценарии Nmap</b>
</p>

---

## 📋 Содержание

- [Обзор и архитектура](#-обзор--архитектура)
- [Сравнение инструментов: RustScan, Nmap и Masscan](#-tool-comparison-rustscan-vs-nmap-vs-masscan)
- [Установка](#-установка)
- [Базовое использование и сканирование с одним хостом](#-basic-usage--single-host-scanning)
- [Автоматическая интеграция Nmap](#-automated-nmap-integration)
- [Настройка производительности (`ulimit` и размер пакета)](#-performance-tuning-ulimit --batch-size)
- [Сканирование диапазонов портов и подсетей CIDR](#-scanning-port-ranges--cidr-subnets)
- [Сценарии и форматирование вывода](#-scripting--output-formatting)
- [Краткая справочная таблица](#-краткая справочная-таблица)
- [Защитные соображения и OPSEC](#-защитные-соображения--opsec)
- [Юридическая оговорка](#-юридическая оговорка)

---

## 🎯 Обзор и архитектура

**RustScan** — это современный высокоскоростной сканер портов, разработанный на **Rust**. Его основная философия проста: **сканировать порты невероятно быстро, находить открытые порты по всему спектру 1-65535 за 3 секунды и немедленно передавать эти открытые порты Nmap для комплексного обслуживания и сканирования уязвимостей**.

Вместо того, чтобы ждать 10–20 минут, пока `nmap -p-` завершит последовательные проверки TCP-подтверждения, RustScan использует асинхронную многопоточность и пакетную обработку сокетов ОС для практически мгновенного определения открытых портов.

---

## ⚖️ Сравнение инструментов: RustScan, Nmap и Masscan

| Особенность | РустСкан | Нмап | Масскан |
|---------|----------|------|---------|
| **Основная цель** | Быстрое обнаружение -> Канал Nmap | Глубокий сервис и сценарии NSE | Высокоскоростное сканирование SYN по всему Интернету |
| **Скорость всех портов (1-65535)**| ⭐⭐⭐⭐⭐ (~3 секунды) | ⭐⭐ (5–20 минут) | ⭐⭐⭐⭐⭐ (~2 секунды) |
| **Определение версии службы**| Через канал Nmap | Родной (`-sV`) | ❌ Минимальный |
| **Обработчик сценариев NSE** | Через канал Nmap | Родной (`-sC`, `--script`) | ❌ Нет |
| **Пакетный механизм** | Асинхронные TCP-сокеты | Необработанный IP/пользовательский стек TCP | Пользовательский стек асинхронного SYN |
| **Простота автоматизации** | Собственные аргументы CLI (`--`) | Обширные возможности CLI | Высокая сложность |

---

## 📥 Установка
```bash
# Debian / Ubuntu / Kali Linux (.deb package)
wget https://github.com/RustScan/RustScan/releases/download/2.3.0/rustscan_2.3.0_amd64.deb
sudo dpkg -i rustscan_2.3.0_amd64.deb

# Arch Linux (AUR)
yay -S rustscan

# Cargo (Rust Package Manager)
cargo install rustscan

# Docker Container
docker run -it --rm --name rustscan rustscan/rustscan:2.1.1 -a 10.10.10.50
```
---

## 🚀 Базовое использование и сканирование одного хоста
```bash
# Scan all 65,535 ports on a single IP
rustscan -a 10.10.10.50

# Scan with greppable one-line output
rustscan -a 10.10.10.50 --greppable

# Quiet mode (suppresses the ASCII banner)
rustscan -q -a 10.10.10.50
```
---

## 🔗 Автоматическая интеграция Nmap

Самая мощная функция RustScan — передача открытых портов напрямую в Nmap с использованием завершающего разделителя `--`:
```bash
# Scan target and execute default safe scripts & version detection on open ports
rustscan -a 10.10.10.50 -- -sC -sV

# Pass custom Nmap NSE scripts and output options
rustscan -a 10.10.10.50 -- -sC -sV -oN nmap_report.txt

# Run vulnerability audit with Nmap Vuln scripts
rustscan -a 10.10.10.50 -- --script vuln -oA target_vulns
```
---

## ⚡ Настройка производительности (`ulimit` и размер пакета)

RustScan опирается на ограничения файловых дескрипторов вашей операционной системы. По умолчанию он настраивается автоматически, но ручная настройка обеспечивает максимальную производительность:
```bash
# Increase batch size and socket ulimit for high-speed local networks
rustscan -a 10.10.10.50 -b 5000 -u 5000

# Lower batch size for high-latency or packet-dropping WiFi networks
rustscan -a 10.10.10.50 -b 500 --scan-order "random"
```
---

## 🌐 Сканирование диапазонов портов и подсетей CIDR
```bash
# Scan a specific port range instead of all ports
rustscan -a 10.10.10.50 -r 1-1000

# Scan multiple IP addresses
rustscan -a 10.10.10.50,10.10.10.51

# Scan an entire CIDR subnet range
rustscan -a 192.168.1.0/24
```
---

## 📊 Сценарии и форматирование вывода
```bash
# Output discovered open ports in JSON format
rustscan -a 10.10.10.50 --accessible

# Read target hosts from a file
rustscan -a 'hosts.txt'
```
---

## ⚡ Краткая справочная таблица

| Цель | Команда |
|-----------|---------|
| **Сканировать все порты 65 тыс.** | `rustscan -a <IP>` |
| **Сканировать все порты + сценарии Nmap**| `rustscan -a <IP> -- -sC -sV` |
| **Сканировать подсеть CIDR** | `rustscan -a <SUBNET>/24` |
| **Диапазон портов сканирования** | `rustscan -a <IP> -r 1-1000` |
| **Настройка размера пакета и ограничения** | `rustscan -a <IP> -b 3000 -u 3000` |
| **Тихий режим (без баннера)** | `rustscan -q -a <IP>` |
| **Греппируемый вывод** | `rustscan -a <IP> --greppable` |
| **Экспорт в файл Nmap** | `rustscan -a <IP> -- -sC -sV -oN nmap.txt` |

---

## 🛡️ Оборонительные соображения и OPSEC

1. **Массовый шум и оповещения IDS:** сканирование 65 тысяч портов за 3 секунды мгновенно генерирует тысячи пакетов TCP SYN. Это немедленно активирует ограничение скорости Snort, Suricata и брандмауэра.
2. **SYN-флуд и отброшенные пакеты:** На нестабильных VPN-каналах (например, HackTheBox, TryHackMe) большие размеры пакетов (`-b 4000`) могут привести к потере пакетов. Если результаты кажутся противоречивыми, уменьшите размер партии до `-b 1000`.

---

## ⚠️ Юридическая оговорка

> [!ВНИМАНИЕ]
> Эта шпаргалка предназначена только для авторизованного тестирования на проникновение и образовательных исследований. Сканирование сетей без авторизации строго запрещено.