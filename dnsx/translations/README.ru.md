<!-- language-home-navigation -->
[🏠 Русская главная страница](../../README.ru.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 📡 dnsx — Полная шпаргалка

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ██████╗ ███╗   ██╗███████╗██╗  ██╗
  ██╔══██╗████╗  ██║██╔════╝╚██╗██╔╝
  ██║  ██║██╔██╗ ██║███████╗ ╚███╔╝ 
  ██║  ██║██║╚██╗██║╚════██║ ██╔██╗ 
  ██████╔╝██║ ╚████║███████║██╔╝ ██╗
  ╚═════╝ ╚═╝  ╚═══╝╚══════╝╚═╝  ╚═╝
       Fast & Multi-Purpose DNS Toolkit by ProjectDiscovery
```
<p выравнивание="центр">
  <img src="https://img.shields.io/badge/dnsx-DNS_Toolkit-blue?style=for-the-badge" alt="dnsx">
  <img src="https://img.shields.io/badge/Developer-ProjectDiscovery-orange?style=for-the-badge" alt="ProjectDiscovery">
  <img src="https://img.shields.io/badge/Category-Recon_&_Scanning-green?style=for-the-badge" alt="Разведка и сканирование">
  <img src="https://img.shields.io/badge/License-MIT-red?style=for-the-badge" alt="Лицензия MIT">
</p>

<p выравнивание="центр">
  <b>🌐 Высокопроизводительное разрешение DNS, фильтрация по шаблонам, обратный поиск DNS и механизм извлечения нескольких запросов</b>
</p>

---

## 📋 Содержание

- [Обзор и архитектура](#-обзор--архитектура)
- [Почему dnsx? (Обработка подстановочных знаков и скорость)](#-why-dnsx-wildcard-handling--speed)
- [Установка](#-установка)
- [Базовое зондирование и разрешение субдомена](#-basic-subdomain-probing--разрешение)
- [Фильтрация DNS по подстановочным знакам (`-wd`)](#-wildcard-dns-filtering---wd)
- [Запрос определенных типов DNS-записей] (#-querying-специфические-dns-record-types)
- [Обратный поиск DNS (`-ptr`)](#-reverse-dns-lookups---ptr)
- [Использование пользовательских резольверов и ограничения скорости](#-using-custom-resolvers--rate-limiting)
- [Автоматизация конвейера (подпоиск + dnsx + httpx)](#-pipeline-automation-subfinder--dnsx--httpx)
- [Краткая справочная таблица](#-краткая справочная-таблица)
- [Защитные соображения и OPSEC](#-защитные-соображения--opsec)
- [Юридическая оговорка](#-юридическая оговорка)

---

## 🎯 Обзор и архитектура

**dnsx**, разработанный в Go компанией **ProjectDiscovery**, представляет собой быстрый и многофункциональный набор инструментов DNS, предназначенный для разведки, поиска ошибок и обнаружения сетей.

В то время как такие инструменты, как `dig`, `nslookup` и `host`, разрешают записи одну за другой, `dnsx` асинхронно обрабатывает тысячи запросов, автоматически повторяет неудачные преобразователи, извлекает чистые IP-адреса ответов и устраняет шум подстановочных знаков, который загрязняет перечисление поддоменов.

---

## ⚡ Почему dnsx? (Обработка подстановочных знаков и скорость)

Во время исследования ошибок пассивные инструменты (`subfinder`, `amass`) часто возвращают тысячи мертвых или подстановочных поддоменов. Тестирование конечных точек HTTP на мертвых записях DNS приводит к потере огромной пропускной способности.

`dnsx` решает эту проблему следующим образом:
1. **Фильтрация подстановочных знаков (`-wd`):** Определяет, разрешается ли `*.domain.com` в универсальный IP-адрес, и отфильтровывает фантомные поддомены.
2. **Скорость и параллелизм.** Разрешает более 10 000 доменов в минуту в общедоступных и частных пулах преобразователей.
3. **Очистка выходного трубопровода:** подается непосредственно в `httpx` или `nmap`.

---

## 📥 Установка
```bash
# Go Install (Recommended)
go install -v github.com/projectdiscovery/dnsx/cmd/dnsx@latest

# Kali Linux & Debian
sudo apt update && sudo apt install -y dnsx

# MacOS (Homebrew)
brew install dnsx

# Verify installation
dnsx -version
```
---

## 🚀 Базовое зондирование и разрешение поддоменов
```bash
# Resolve a single domain
echo "target.com" | dnsx

# Resolve a list of subdomains from file
dnsx -l subdomains.txt

# Display domain and its corresponding A record IP
dnsx -l subdomains.txt -a -resp

# Output only resolved IP addresses (ready for Nmap)
dnsx -l subdomains.txt -resp-only
```
---

## 🚫 DNS-фильтрация с использованием подстановочных знаков (`-wd`)

Подстановочные знаки возвращают действительный IP-адрес для несуществующих поддоменов (`random123xyz.target.com`), что отравляет наборы разведывательных данных.
```bash
# Filter wildcard subdomains and print only genuine hosts
dnsx -l subdomains.txt -wd target.com -o valid_hosts.txt

# Filter wildcards with custom threshold
dnsx -l subdomains.txt -wd target.com -wt 3
```
---

## 📋 Запрос определенных типов записей DNS

Запросите конкретные типы записей в тысячах доменов:
```bash
# Query CNAME records (Crucial for Subdomain Takeover detection)
dnsx -l subdomains.txt -cname -resp

# Query Mail Exchanger (MX) records
dnsx -l domains.txt -mx -resp

# Query TXT records (SPF, DKIM, verification tokens)
dnsx -l domains.txt -txt -resp

# Query All standard records (A, AAAA, CNAME, NS, TXT, MX, SOA)
dnsx -l domains.txt -recon
```
---

## 🔄 Обратный поиск DNS (`-ptr`)

Обнаруживайте активную инфраструктуру и виртуальные хосты, выполняя поиск PTR в диапазоне IP-адресов:
```bash
# Reverse resolve a CIDR IP range to hostnames
dnsx -ptr -l ip_ranges.txt -resp

# Reverse resolve a single subnet
echo "192.168.1.0/24" | dnsx -ptr -resp
```
---

## ⚙️ Использование пользовательских резольверов и ограничения скорости

Избегайте ограничения скорости со стороны вашего интернет-провайдера или Google DNS, используя высокоскоростной курируемый пул преобразователей:
```bash
# Use a custom trusted resolver list
dnsx -l subdomains.txt -r resolvers.txt

# Set thread concurrency and query rate limit (queries per second)
dnsx -l subdomains.txt -t 100 -rl 300
```
---

## ⛓️ Автоматизация конвейеров (подпоиск + dnsx + httpx)

Оптимальный рабочий процесс разведки для охотников за ошибками и тестеров на проникновение:
```bash
# Discover subdomains -> Validate via dnsx -> Probe live HTTP web services
subfinder -d target.com -silent | \
  dnsx -silent -wd target.com | \
  httpx -silent -status-code -title -tech-detect
```
---

## ⚡ Краткая справочная таблица

| Цель | Команда |
|-----------|---------|
| **Решение поддоменов** | `dnsx -l subdomains.txt` |
| **Извлекать только IP-адреса** | `dnsx -l subdomains.txt -resp-only` |
| **Фильтрация доменов с подстановочными знаками** | `dnsx -l subdomains.txt -wd <DOMAIN>` |
| **Извлечение CNAME (Поглощение)** | `dnsx -l subdomains.txt -cname -resp` |
| **Запросить все записи (разведка)** | `dnsx -l domains.txt -recon` |
| **Обратный поиск DNS (PTR)** | `dnsx -l ips.txt -ptr -resp` |
| **Пользовательские резольверы и потоки**| `dnsx -l list.txt -r resolvers.txt -t 150` |
| **Вывод JSON** | `dnsx -l list.txt -json -o results.json` |

---

## 🛡️ Оборонительные соображения и OPSEC

1. **DNS-флуд.** Выполнение десятков тысяч DNS-запросов через корпоративные внутренние DNS-серверы приведет к резкому увеличению нагрузки на процессор и вызову предупреждений SIEM о DNS-туннелировании или DDoS-атаках.
2. **Отравление резолвера.** Всегда используйте проверенные, доверенные списки резолверов, чтобы предотвратить перехват запросов злонамеренными резолверами.

---

## ⚠️ Юридическая оговорка

> [!ВНИМАНИЕ]
> Эта шпаргалка предназначена только для авторизованного тестирования на проникновение и участия в программе «Bug Bounty». Проведение массовых DNS-зондов несанкционированной инфраструктуры строго запрещено.