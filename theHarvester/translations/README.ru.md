<!-- language-home-navigation -->
[🏠 Русская главная страница](../../README.ru.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# Шпаргалка по theHarvester

[theHarvester](https://github.com/laramies/theHarvester) — один из наиболее широко используемых инструментов анализа с открытым исходным кодом (OSINT) для сбора электронных писем, поддоменов, имен сотрудников, виртуальных хостов, открытых портов и ключей PGP сотрудников из различных общедоступных источников (поисковые системы, серверы ключей PGP, Shodan, Censys, Certspotter и т. д.).

---

## Содержание

- [Поддерживаемые источники данных](#supported-data-sources)
- [Установка и настройка ключа API](#installation--api-key-configuration)
- [Базовая разведка домена](#basic-domain-reconnaissance)
- [Пассивный сбор из нескольких источников](#passive-multi-source-harvesting)
- [Активная проверка DNS и брутфорс](#active-dns-verification--brute-forcing)
- [Форматы вывода (HTML, XML, JSON)](#output-formats-html-xml-json)
- [Интеграция рабочего процесса пентеста](#pentest-workflow-integration)

---

## Поддерживаемые источники данных

theHarvester собирает информацию из десятков пассивных и активных источников:
- **Поисковые системы (бесплатно/без ключа):** Google, Bing, DuckDuckGo, Yahoo, Baidu.
- **Прозрачность сертификата:** CRT.sh, Certspotter.
- **Репозитории кода и угроз:** GitHub, AlienVault OTX, ThreatCrowd, Rapid7, VirusTotal.
- **API-интерфейсы разведки и OSINT:** Shodan, Censys, Hunter.io, SecurityTrails, Intelx, FullContact.
- **Сеть/DNS:** Anubis, HackerTarget, DNSDumpster.

---

## Установка и настройка API-ключа

### 1. Установка
```bash
# Debian / Ubuntu / Kali Linux
sudo apt-get update && sudo apt-get install -y theharvester

# From GitHub Source (Latest Version)
git clone https://github.com/laramies/theHarvester.git
cd theHarvester
python3 -m pip install -r requirements/base.txt

# Test binary
theHarvester -h
```
### 2. Настройте ключи API (`api-keys.yaml`)
Многие источники (Hunter, Shodan, Censys, Intelx, GitHub) требуют бесплатных или платных ключей API.
Создайте или отредактируйте `api-keys.yaml` в пути конфигурации:
```yaml
apikeys:
  hunter:
    key: "YOUR_HUNTER_KEY"
  shodan:
    key: "YOUR_SHODAN_KEY"
  github:
    key: "YOUR_GITHUB_TOKEN"
  securityTrails:
    key: "YOUR_SECURITYTRAILS_KEY"
```
---

## Базовая разведка домена

### 1. Собирайте электронные письма и поддомены с помощью бесплатных движков
```bash
theHarvester -d example.com -l 500 -b duckduckgo,bing,crtsh
```
- `-d <domain>`: Целевой домен для разведки.
- `-l <limit>`: количество результатов запроса для каждого механизма.
- `-b <sources>`: список источников, разделенных запятыми (или `all`).

### 2. Запросить все настроенные источники
```bash
theHarvester -d example.com -l 500 -b all
```
---

## Пассивный сбор из нескольких источников

Запрашивайте конкретные базы данных анализа угроз и прозрачности сертификатов без отправки трафика в цель:

### 1. Прозрачность сертификатов и угрозы Intel
```bash
theHarvester -d example.com -b crtsh,certspotter,anubis,hackertarget,otx
```
### 2. Целевые группы источников
```bash
# Query GitHub commits and issues for leaked emails and subdomains
theHarvester -d example.com -b github-code

# Query Shodan for discovered IP addresses and open ports
theHarvester -d example.com -b shodan
```
---

## Активная проверка DNS и брутфорс

### 1. Разрешение обнаруженных поддоменов (`-r`)
Выполните активный поиск DNS, чтобы проверить, активны ли в настоящее время собранные поддомены:
```bash
theHarvester -d example.com -b crtsh,bing -r
```
### 2. DNS-брутфорс (`-c`)
Выполните перебор DNS-субдомена на основе списка слов непосредственно в Harvester:
```bash
theHarvester -d example.com -b duckduckgo -c
```
### 3. Проверка виртуального хоста (`-v`)
Проверьте виртуальные хосты через HTTP-запросы:
```bash
theHarvester -d example.com -b duckduckgo -v
```
---

## Форматы вывода (HTML, XML, JSON)

Экспортируйте результаты для автоматического составления отчетов или последующей разведки:

### 1. Экспорт в HTML и XML
```bash
theHarvester -d example.com -b duckduckgo,crtsh -f /tmp/theharvester_results
```
Создает `/tmp/theharvester_results.html` и `/tmp/theharvester_results.xml`.

### 2. Анализ обнаруженных поддоменов с помощью `jq`
При экспорте или анализе JSON:
```bash
theHarvester -d example.com -b crtsh,duckduckgo -f recon_out
# Extract clean host list
cat recon_out.xml | grep -oP '(?<=<host>).*?(?=</host>)' | sort -u > live_hosts.txt
```
---

## Интеграция рабочего процесса пентеста

Свяжите результаты theHarvester напрямую с активными сканерами уязвимостей:
```bash
# 1. Harvest passive subdomains
theHarvester -d target.corp -b crtsh,duckduckgo,bing -f target_recon

# 2. Extract unique domain names
grep -oP '(?<=<host>)[^<]+' target_recon.xml | sort -u > hosts.txt

# 3. Probe for live web services using httpx
cat hosts.txt | httpx -silent -status-code -title > live_web.txt

# 4. Scan live web services with Nuclei
nuclei -l live_web.txt -severity medium,high,critical
```