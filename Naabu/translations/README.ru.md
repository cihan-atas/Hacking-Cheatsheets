<!-- language-home-navigation -->
[🏠 Русская главная страница](../../README.ru.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ⚡ Наабу — Полная шпаргалка

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ███╗   ██╗ █████╗  █████╗ ██████╗ ██╗   ██╗
  ████╗  ██║██╔══██╗██╔══██╗██╔══██╗██║   ██║
  ██╔██╗ ██║███████║███████║██████╔╝██║   ██║
  ██║╚██╗██║██╔══██║██╔══██║██╔══██╗██║   ██║
  ██║ ╚████║██║  ██║██║  ██║██████╔╝╚██████╔╝
  ╚═╝  ╚═══╝╚═╝  ╚═╝╚═╝  ╚═╝╚═════╝  ╚═════╝ 
        Fast & Reliable SYN/CONNECT Port Scanner
```
<p выравнивание="центр">
  <img src="https://img.shields.io/badge/Naabu-Port_Scanner-blue?style=for-the-badge" alt="Наабу">
  <img src="https://img.shields.io/badge/Author-ProjectDiscovery-orange?style=for-the-badge" alt="ProjectDiscovery">
  <img src="https://img.shields.io/badge/Category-Recon_&_Scanning-green?style=for-the-badge" alt="Разведка и сканирование">
  <img src="https://img.shields.io/badge/Language-Go-cyan?style=for-the-badge" alt="Go">
</p>

<p выравнивание="центр">
  <b>⚡ Быстрый и надежный сканер портов, ориентированный на простоту и бесперебойность работы с инструментами ProjectDiscovery (httpx, ядра)</b>
</p>

---

## 📋 Содержание

- [Обзор и основные функции](#-overview--key-features)
- [Установка](#-установка)
- [Базовое сканирование портов](#-basic-port-scanning)
  - [Сканирование одного хоста](#single-host-scan)
  - [Основные порты и полный диапазон портов](#top-ports--full-port-range)
- [Режимы сканирования: SYN (Raw Socket) vs CONNECT] (#-scan-modes-syn-raw-socket-vs-connect)
- [Ограничение скорости и настройка производительности] (#-rate-limiting-- Performance-tuning)
- [Многоцелевое сканирование и блоки CIDR](#-multi-target-scanning--cidr-blocks)
- [Бесшовные рабочие процессы конвейера](#-seamless-pipeline-workflows)
  - [Наабу + Httpx + Ядра](#naabu--httpx--nuclei)
  - [Подпоиск + Наабу](#подпоиск--наабу)
- [Интеграция Nmap (`-nmap`)](#-nmap-integration--nmap)
- [Краткая справочная таблица](#-краткая справочная-таблица)
- [Защитные соображения и обнаружение](#-защитные-соображения--обнаружение)
- [Юридическая оговорка](#-юридическая оговорка)

---

## 🎯 Обзор и ключевые особенности

Разработанный **ProjectDiscovery**, **Naabu** — это быстрый, точный и оптимизированный для интеграции в современные конвейеры автоматизации разведки портовый сканер:
- **Сканирование SYN & CONNECT:** Автоматический переход к TCP CONNECT при отсутствии привилегий необработанного сокета (`libpcap` / root).
- **Экосистема ProjectDiscovery.** Собственная архитектура конвейера, обеспечивающая плавный переход от `subfinder` к `naabu`, а затем к `httpx` и `nuclei`.
- **Интегрированный механизм Nmap:** Возможность автоматического запуска активной службы Nmap и сканирования версий обнаруженных открытых портов.
- **Исключение CDN:** автоматически обходит пограничные узлы облачной CDN (Cloudflare, Akamai, CloudFront), чтобы предотвратить потерю пропускной способности и ложные срабатывания.

---

## 📥 Установка
```bash
# Using Go (Recommended)
go install -v github.com/projectdiscovery/naabu/v2/cmd/naabu@latest

# Binary download via pdtm (ProjectDiscovery Tool Manager)
pdtm -i naabu

# Arch Linux (AUR)
yay -S naabu-bin

# Kali Linux
sudo apt update && sudo apt install -y naabu
```
> [!ПРИМЕЧАНИЕ]
> В Linux предоставление `CAP_NET_RAW` позволяет запускать сканирование SYN без root:
> `sudo setcap cap_net_raw=+ep $(which naabu)`

---

## 🚀 Базовое сканирование портов

### Сканирование одного хоста
```bash
# Scan default top 100 ports
naabu -host example.com

# Scan specific ports
naabu -host example.com -p 80,443,8080,8443
```
### Лучшие порты и полный диапазон портов
```bash
# Scan top 1000 ports
naabu -host example.com -top-ports 1000

# Full port scan (1-65535)
naabu -host example.com -p -
```
---

## ⚡ Режимы сканирования: SYN (Raw Socket) или CONNECT

- **Сканирование SYN (требуется `-s syn` / root):** отправляет необработанные пакеты TCP SYN без завершения трехстороннего рукопожатия (полуоткрытое сканирование). Гораздо быстрее и меньше шума в журналах приложений.
- **Сканирование CONNECT (`-s conn`):** Использует стандартный вызов сокета `connect()` ОС. Работает без root-прав и проникает в виртуальные среды с ограничениями.
```bash
# Explicit SYN Scan (Requires sudo or CAP_NET_RAW)
sudo naabu -host example.com -s syn

# Explicit TCP Connect Scan (No root required)
naabu -host example.com -s conn
```
---

## ⚙️ Ограничение скорости и настройка производительности
```bash
# Adjust packet rate per second (default: 1000)
sudo naabu -host example.com -rate 2500

# Set connection timeout in milliseconds
naabu -host example.com -timeout 1000

# Exclude CDN edge servers (Cloudflare, CloudFront, Fastly)
naabu -host example.com -exclude-cdn
```
---

## 🌐 Многоцелевое сканирование и блоки CIDR
```bash
# Scan targets from list
naabu -list targets.txt -p 80,443,8080 -o open_ports.txt

# Scan a /24 subnet block
sudo naabu -host 192.168.1.0/24 -p 22,80,443,445,3389
```
---

## 🔄 Бесшовные рабочие процессы конвейера

Naabu сияет при работе с другими инструментами ProjectDiscovery:

### Подпоиск + Наабу
Перечислите поддомены и сразу узнайте, какие из них имеют открытые порты Web/SSH:
```bash
subfinder -d example.com -silent | naabu -silent -p 80,443,8080,8443
```
### Наабу + Httpx + Ядра
Канонический конвейер наступательной разведки ошибок:
```bash
# 1. Port scan subdomains -> 2. Probe HTTP services -> 3. Vulnerability scan
subfinder -d target.com -silent \
  | naabu -silent -p 80,443,8080,8443 \
  | httpx -silent \
  | nuclei -severity medium,high,critical -o findings.txt
```
---

## 🔍 Интеграция Nmap (`-nmap`)

Передайте обнаруженные открытые порты непосредственно в Nmap для обнаружения сервисов (`-sV`) и сканирования скриптов (`-sC`):
```bash
sudo naabu -host target.local -top-ports 1000 -nmap-cli 'nmap -sV -sC -oN naabu_nmap.txt'
```
---

## ⚡ Краткая справочная таблица

| Цель | Команда |
|-----------|---------|
| **Сканирование по умолчанию (100 лучших)** | `naabu -host <TARGET>` |
| **Полное сканирование (1-65535)**| `naabu -host <TARGET> -p -` |
| **1000 лучших портов** | `naabu -host <TARGET> -top-ports 1000` |
| **SYN-сканирование (быстрое, скрытное)**| `sudo naabu -host <TARGET> -s syn` |
| **Исключить IP-адреса Edge CDN** | `naabu -host <TARGET> -exclude-cdn` |
| **Вывод в JSON** | `naabu -host <TARGET> -json -o results.json` |
| **Переход к HTTPX** | `naabu -host <TARGET> -silent \| httpx` |
| **Запустить сканирование службы Nmap**| `sudo naabu -host <TARGET> -nmap-cli 'nmap -sV'` |

---

## 🛡️ Соображения по защите и обнаружению

1. **Правила брандмауэра и IDS:** Высокоскоростные проверки SYN, генерируемые Naabu, обеспечивают высокие соотношения TCP RST/SYN, которые легко фиксируются правилами Snort/Suricata.
2. **Ограничение скорости сети.** Включите ограничение скорости входящего соединения (`iptables -m limit --limit 50/s`), чтобы замедлить и нарушить автоматическое сканирование.

---

## ⚠️ Юридическая оговорка

> [!ВНИМАНИЕ]
> Naabu разработан исключительно для авторизованных оценок безопасности, тестирования на проникновение и обнаружения защитных активов. Сканирование сетей без явного разрешения строго запрещено.