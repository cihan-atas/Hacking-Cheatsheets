<!-- language-home-navigation -->
[🏠 Русская главная страница](../../README.ru.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🦈 tshark — Полная шпаргалка

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ████████╗███████╗██╗  ██╗ █████╗ ██████╗ ██╗  ██╗
  ╚══██╔══╝██╔════╝██║  ██║██╔══██╗██╔══██╗██║ ██╔╝
     ██║   ███████╗███████║███████║██████╔╝█████╔╝ 
     ██║   ╚════██║██╔══██║██╔══██║██╔══██╗██╔═██╗ 
     ██║   ███████║██║  ██║██║  ██║██║  ██║██║  ██╗
     ╚═╝   ╚══════╝╚═╝  ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝
         Terminal Packet Analysis & Dissection Engine
```
<p выравнивание="центр">
  <img src="https://img.shields.io/badge/tshark-Packet_Analyzer-blue?style=for-the-badge" alt="tshark">
  <img src="https://img.shields.io/badge/CLI-Network_Sniffer-red?style=for-the-badge" alt="Сетевой анализатор CLI">
  <img src="https://img.shields.io/badge/Wireshark-Engine-green?style=for-the-badge" alt="Wireshark Engine">
  <img src="https://img.shields.io/badge/PCAP-Dissection-orange?style=for-the-badge" alt="Рассечение PCAP">
</p>

<p выравнивание="центр">
  <b>🌐 Глубокое разделение сетевых пакетов на базе терминала, анализ потоков и извлечение полей на базе Wireshark</b>
</p>

---

## 📋 Содержание

- [Введение](#-введение)
- [Сравнение инструментов: tshark, tcpdump и Wireshark](#-tool-comparison)
- [Установка и привилегии](#-installation--privileges)
- [Базовый захват и воспроизведение] (#-basic-capture--воспроизведение)
- [Фильтрация трафика: захват или отображение](#-filtering-traffic)
- [Извлечение поля (`-T fields`)](#-извлечение поля)
- [Практические рецепты пентеста и криминалистики](#-practical-pentest--forensic-recipes)
  - [HTTP-учетные данные и поиск URI](#http-credential--uri-hunting)
  - [Извлечение DNS-запросов и обнаружение туннелирования](#dns-query-extraction--tunneling-detection)
  - [Извлечение TLS/HTTPS SNI](#tls--https-sni-extraction)
  - [Извлечение Kerberos AS-REQ / AS-REP](#kerberos-as-req--as-rep-extraction)
  - [Отслеживание аутентификации SMB и NTLM](#smb--ntlm-authentication-tracking)
  - [Аудит протокола открытого текста (FTP, Telnet, SMTP)](#cleartext-protocol-auditing)
  - [4-сторонний поиск рукопожатий WPA/EAPOL](#wpa--eapol-4-way-handshake-hunting)
- [Слежение за потоком и повторная сборка](#-stream-following--reassembly)
- [Статистика, разговоры и конечные точки](#-statistics-conversations--endpoints)
- [Расшифровка TLS/SSL-трафика](#-tls--ssl-traffic-decryption)
- [Форматы экспорта (JSON, CSV, Elasticsearch)](#-export-formats)
- [Краткая справочная таблица](#-краткая справочная-таблица)
- [Советы и рекомендации](#-tips--best-practices)
- [Юридическая оговорка](#-юридическая оговорка)

---

## 🎯 Введение

**tshark** — это аналог **Wireshark** для терминала и командной строки. Он использует ту же библиотеку захвата пакетов (libpcap/Npcap) и механизм анализа пакетов, что и графический Wireshark, что делает его одним из самых мощных инструментов сетевого анализа, доступных в Linux, macOS и Windows.

В отличие от базовых инструментов анализа пакетов, которые проверяют только заголовки протоколов, `tshark` имеет возможности глубокой проверки пакетов по тысячам протоколов, собственный синтаксис фильтра отображения Wireshark (`-Y`), надежное извлечение полей протокола (`-T fields`) и гибкое форматирование вывода для конвейеров автоматизации (`jq`, сценарии оболочки, Python).

### Ключевые особенности

| Особенность | Описание |
|---------|-------------|
| **Глубокое рассечение** | Проверяет полезные данные по тысячам протоколов приложений |
| **Фильтры Wireshark** | Полная поддержка расширенного синтаксиса фильтров отображения Wireshark (`-Y`) |
| **Добыча на местах** | Извлечение отдельных полей протокола напрямую (`-T fields -e ...`) |
| **Подписка на поток** | Собирает полные сеансы TCP/UDP в форматах ASCII, Hex или raw |
| **Автоматическая криминалистика**| Возможность написания сценариев в оболочке, Python или передача в `jq` через `-T json` |
| **Расшифровка** | Поддерживает расшифровку TLS во время выполнения с помощью `SSLKEYLOGFILE` |

---

## ⚖️ Сравнение инструментов

| Особенность | шарк | tcpдамп | Вайршарк |
|---------|--------|---------|-----------|
| **Интерфейс** | CLI (терминал) | CLI (терминал) | Графический интерфейс |
| **Использование ресурсов** | Низкий/Умеренный | Чрезвычайно минималистичный | Высшее (ОЗУ и ЦП) |
| **Удаленный доступ (SSH/без головы)** | Родной безголовый/CLI | Родная труба SSH | Требуется X11/Удаленный агент |
| **Рассекающий двигатель** | Полные диссекторы Wireshark | Основные заголовки | Полные диссекторы Wireshark |
| **Фильтры захвата** | Только БПФ (`-f`) | Только BPF (`-i`) | Только БПФ |
| **Фильтры отображения** | ✅ Полный синтаксис Wireshark (`-Y`) | ❌ Нет | ✅ Полная панель фильтров графического интерфейса |
| **Форматы экспорта** | PCAP, JSON, CSV, Elastic, XML | PCAP, ASCII, шестнадцатеричный | PCAP, CSV, XML, JSON |
| **Сценарии и автоматизация**| Shell, Python, jq | Шелл/Авк/Сед | Плагины Lua, графический интерфейс |

---

## 📥 Установка и привилегии

### Linux (Debian/Ubuntu/Kali Linux)
```bash
# Update and install tshark
sudo apt update && sudo apt install -y tshark

# Verify installation and version
tshark --version
```
### Разрешение захвата некорневых пакетов

Чтобы предотвратить запуск `tshark` как `root` (что может представлять угрозу безопасности из-за обширной поверхности диссектора), добавьте своего пользователя в группу `wireshark`:
```bash
# Configure wireshark-common for non-superusers
sudo dpkg-reconfigure wireshark-common
# (Select "Yes" when prompted)

# Add your user to the wireshark group
sudo usermod -aG wireshark $USER

# Apply group changes to current session
newgrp wireshark
```
### macOS (доморощенный) и Windows (шоколадный)
```bash
# macOS
brew install wireshark

# Windows
choco install wireshark
```
---

## 🚀 Базовый захват и воспроизведение

### Список доступных интерфейсов
```bash
# List interfaces with numeric IDs
tshark -D
```
### Захват живых пакетов
```bash
# Capture packets on interface eth0
tshark -i eth0

# Capture only 100 packets and stop
tshark -i eth0 -c 100

# Capture and save traffic directly to a pcap file
tshark -i eth0 -w capture.pcap

# Capture with ring buffer (rotate files every 100MB, keep max 5 files)
tshark -i eth0 -b filesize:100000 -b files:5 -w rolling_traffic.pcap

# Capture with a duration trigger (capture for 60 seconds)
tshark -i eth0 -a duration:60 -w one_minute.pcap
```
### Чтение файлов PCAP
```bash
# Read and display packets from a pcap file
tshark -r capture.pcap

# Read PCAP without resolving IP addresses to DNS hostnames (speeds up processing)
tshark -r capture.pcap -n

# Suppress packet count summary upon completion
tshark -r capture.pcap -q
```
---

## 🔍 Фильтрация трафика: захват или отображение

`tshark` использует два различных механизма фильтрации:

1. **Фильтры захвата (`-f`):** Фильтры пакетов Беркли (BPF), оцениваемые ядром до того, как пакеты попадают в пространство пользователя. Высокая производительность, но ограничена базовыми примитивами IP/порта.
2. **Фильтры отображения (`-Y`):** фильтры глубоких протоколов, оцениваемые механизмом анализа Wireshark. Поддерживает уровни приложения, поиск строк, регулярные выражения и флаги протокола.
```bash
# Capture filter: Capture only TCP traffic to port 80 or 443
tshark -i eth0 -f "tcp port 80 or tcp port 443" -w web_traffic.pcap

# Display filter: Show only successful HTTP GET responses (Status 200)
tshark -r capture.pcap -Y "http.request.method == \"GET\" and http.response.code == 200"

# Display filter: Filter by specific subnet and exclude broadcast
tshark -r capture.pcap -Y "ip.addr == 192.168.1.0/24 and not eth.dst == ff:ff:ff:ff:ff:ff"

# Read-filter (-R): Single-pass filter used during capture
tshark -r capture.pcap -2 -R "dns.flags.response == 1"
```
---

## 📊 Извлечение полей (`-T fields`)

Режим `-T fields` — это самая мощная функция `tshark` для автоматизированной разведки, написания сценариев и задач CTF.
```bash
# Basic syntax for field extraction
tshark -r capture.pcap -T fields -e <field1> -e <field2> -E header=y -E separator=,
```
### Флаги форматирования извлечения ключей

| Флаг | Описание |
|------|-------------|
| `-T fields` | Переключает режим вывода на выборочное извлечение полей |
| `-e <field>` | Называет поле протокола для извлечения (может повторяться) |
| `-E header=y` | Печатает имена столбцов в виде строки заголовка |
| `-E separator=,` | Устанавливает разделитель полей (`,` для CSV, `\t` для TSV) |
| `-E occurrence=f` | Извлекает только первое (`f`), последнее (`l`) или все (`a`) вхождения |
| `-E quote=d` | Заключает значения в двойные кавычки |

### Памятка по полям основного протокола
```bash
# IP layer fields
ip.src, ip.dst, ip.proto, ip.ttl

# TCP layer fields
tcp.srcport, tcp.dstport, tcp.flags, tcp.stream, tcp.payload

# HTTP fields
http.host, http.request.method, http.request.uri, http.response.code, http.user_agent, http.cookie

# DNS fields
dns.qry.name, dns.qry.type, dns.a, dns.flags.response

# TLS fields
tls.handshake.extensions_server_name, tls.handshake.ciphersuite
```
---

## 🎯 Практические рецепты пентестов и криминалистики

### HTTP-учетные данные и поиск URI
```bash
# Extract all visited URLs and HTTP Hosts
tshark -r capture.pcap -Y "http.request" -T fields -e ip.src -e http.host -e http.request.method -e http.request.uri -E header=y

# Extract User-Agent strings across internal clients
tshark -r capture.pcap -Y "http.user_agent" -T fields -e ip.src -e http.user_agent | sort -u

# Hunt for HTTP POST data (logins, passwords, tokens)
tshark -r capture.pcap -Y "http.request.method == \"POST\"" -T fields -e ip.src -e ip.dst -e http.host -e http.file_data

# Hunt for HTTP Basic Authentication headers (Base64 encoded)
tshark -r capture.pcap -Y "http.authorization" -T fields -e ip.src -e http.authorization
```
### Извлечение DNS-запросов и обнаружение туннелирования
```bash
# List all requested domain names (DNS queries)
tshark -r capture.pcap -Y "dns.flags.response == 0" -T fields -e ip.src -e dns.qry.name | sort -u

# Detect potential DNS tunneling (display long subdomains > 30 characters)
tshark -r capture.pcap -Y "dns.flags.response == 0" -T fields -e dns.qry.name | awk 'length($1) > 30' | sort -u

# Correlate DNS queries with their resolved IP addresses
tshark -r capture.pcap -Y "dns.flags.response == 1 and dns.a" -T fields -e dns.qry.name -e dns.a -E header=y
```
### Извлечение TLS/HTTPS SNI

Даже если полезные данные HTTPS зашифрованы, **Индикация имени сервера (SNI)** в начальном рукопожатии TLS Client Hello показывает имена доменов назначения:
```bash
# Extract TLS Server Name Indication (SNI) hostnames
tshark -r capture.pcap -Y "tls.handshake.type == 1" -T fields -e ip.src -e ip.dst -e tls.handshake.extensions_server_name | sort -u
```
### Извлечение Kerberos AS-REQ/AS-REP
```bash
# Extract Kerberos domain client names (hunting for user accounts)
tshark -r capture.pcap -Y "kerberos.CNameString" -T fields -e ip.src -e kerberos.realm -e kerberos.CNameString | sort -u

# Hunt for Kerberos errors (e.g. KRB5KDC_ERR_PREAUTH_REQUIRED, bad passwords)
tshark -r capture.pcap -Y "kerberos.error_code" -T fields -e ip.src -e ip.dst -e kerberos.error_code
```
### Отслеживание аутентификации SMB и NTLM
```bash
# Extract SMB accessed file paths and shares
tshark -r capture.pcap -Y "smb2.filename" -T fields -e ip.src -e ip.dst -e smb2.filename | sort -u

# Extract NTLMSSP usernames and domain names during authentication
tshark -r capture.pcap -Y "ntlmssp.auth.username" -T fields -e ip.src -e ntlmssp.auth.domain -e ntlmssp.auth.username
```
### Аудит протокола открытого текста
```bash
# Extract FTP usernames and passwords
tshark -r capture.pcap -Y "ftp.request.command == \"USER\" or ftp.request.command == \"PASS\"" -T fields -e ip.src -e ftp.request.command -e ftp.request.arg

# Extract Telnet session input
tshark -r capture.pcap -Y "telnet" -T fields -e telnet.data
```
### Поиск четырехстороннего рукопожатия WPA / EAPOL
```bash
# Filter for 802.11 EAPOL handshakes to crack with Hashcat/Aircrack-ng
tshark -r wifi.pcap -Y "eapol" -w handshakes_only.pcap
```
---

## 🧵 Следование за трансляцией и повторная сборка

`tshark` позволяет собирать целые потоки TCP и UDP без необходимости использования графического интерфейса Wireshark.
```bash
# List all TCP conversations to find stream IDs
tshark -r capture.pcap -q -z conv,tcp

# Follow TCP stream 0 in ASCII format
tshark -r capture.pcap -q -z follow,tcp,ascii,0

# Follow TCP stream 2 in Hexdump format
tshark -r capture.pcap -q -z follow,tcp,hex,2

# Follow HTTP stream (stream index 1)
tshark -r capture.pcap -q -z follow,http,ascii,1
```
---

## 📈 Статистика, разговоры и конечные точки
```bash
# Protocol hierarchy statistics (breakdown of all protocols in PCAP)
tshark -r capture.pcap -q -z io,phs

# IP conversations table (who talked to whom, packet count, bytes)
tshark -r capture.pcap -q -z conv,ip

# Port conversation statistics (TCP ports)
tshark -r capture.pcap -q -z conv,tcp

# List all unique IP endpoints in PCAP
tshark -r capture.pcap -q -z endpoints,ip

# HTTP request statistics tree
tshark -r capture.pcap -q -z http,tree

# Wireshark Expert Info warnings and errors
tshark -r capture.pcap -q -z expert
```
---

## 🔐 Расшифровка TLS/SSL-трафика

Если у вас есть предварительные секретные ключи (экспортированные через браузер `SSLKEYLOGFILE` или полученные во время изолированной программной среды вредоносного ПО):
```bash
# Decrypt TLS traffic on the fly using keylog file
tshark -r https_traffic.pcap -o "tls.keylog_file:/path/to/sslkeys.log" -Y "http" -T fields -e http.host -e http.request.uri

# Follow decrypted HTTP stream inside TLS
tshark -r https_traffic.pcap -o "tls.keylog_file:/path/to/sslkeys.log" -q -z follow,http,ascii,0
```
---

## 📦 Форматы экспорта
```bash
# Output as structured JSON (ideal for piping into jq or Python)
tshark -r capture.pcap -c 5 -T json

# Extract specific JSON field with jq
tshark -r capture.pcap -T json | jq '.[]._source.layers.ip["ip.src"]'

# Output in Elasticsearch Bulk Format (-T ek)
tshark -r capture.pcap -T ek > packets_for_elastic.json

# Output as CSV table
tshark -r capture.pcap -T fields -e frame.number -e ip.src -e ip.dst -e _ws.col.Protocol -E header=y -E separator=, > traffic.csv
```
---

## ⚡ Краткая справочная таблица

| Задача | Команда |
|------|---------|
| **Снимайте трафик в реальном времени** | `tshark -i eth0 -w output.pcap` |
| **Захват с помощью фильтра BPF** | `tshark -i eth0 -f "port 80" -w http.pcap` |
| **Чтение PCAP без DNS** | `tshark -r capture.pcap -n` |
| **Применить фильтр отображения** | `tshark -r capture.pcap -Y "http.response.code >= 400"` |
| **Извлечение IP-адресов и портов** | `tshark -r capture.pcap -T fields -e ip.src -e tcp.srcport -e ip.dst -e tcp.dstport` |
| **Извлечение DNS-запросов** | `tshark -r capture.pcap -Y "dns.flags.response == 0" -T fields -e dns.qry.name` |
| **Извлечение имен хостов TLS SNI**| `tshark -r capture.pcap -Y "tls.handshake.type == 1" -T fields -e tls.handshake.extensions_server_name` |
| **Извлечение URL-адресов HTTP** | `tshark -r capture.pcap -Y "http.request" -T fields -e http.host -e http.request.uri` |
| **Следовать TCP-потоку** | `tshark -r capture.pcap -q -z follow,tcp,ascii,0` |
| **Иерархия протоколов** | `tshark -r capture.pcap -q -z io,phs` |
| **Расшифровать с помощью Keylog** | `tshark -r traffic.pcap -o "tls.keylog_file:keys.log"` |
| **Экспорт в JSON** | `tshark -r capture.pcap -T json > packets.json` |

---

## 💡 Советы и рекомендации

1. **Всегда используйте `-n` для больших PCAP:** Разрешение имен DNS значительно замедляет анализ. Добавьте `-n`, чтобы предотвратить разрешение IP-адресов.
2. **Объедините `-f` и `-Y`:** используйте фильтры захвата `-f` во время захвата в реальном времени, чтобы уменьшить потери пакетов, а фильтры отображения `-Y` во время пост-анализа для глубокой проверки протокола.
3. **Конвейер с `jq`:** Конвейерная передача `tshark -T json` в `jq` позволяет запрашивать вложенные поля протокола с полной логической логикой и преобразованием.
4. **Кольцевые буферы для круглосуточного прослушивания 7 дней в неделю.** При непрерывном захвате сети на главном хосте всегда устанавливайте ограничения на размер и размер файла (`-b filesize:50000 -b files:10`), чтобы предотвратить сбой целевого диска.

---

## ⚠️ Юридическая оговорка

> **ВАЖНО:** Несанкционированный захват пакетов, прослушивание сетевого трафика и перехват учетных данных в сетях без явного, задокументированного разрешения являются незаконными и подлежат уголовному преследованию. Используйте `tshark` исключительно в системах и сетях, которыми вы владеете или которые уполномочены оценивать по контракту.