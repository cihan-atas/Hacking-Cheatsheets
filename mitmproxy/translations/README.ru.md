<!-- language-home-navigation -->
[🏠 Русская главная страница](../../README.ru.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🕶️ mitmproxy — Полная шпаргалка

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ███╗   ███╗██╗████████╗███╗   ███╗██████╗ ██████╗  ██████╗ ██╗  ██╗██╗   ██╗
  ████╗ ████║██║╚══██╔══╝████╗ ████║██╔══██╗██╔══██╗██╔═══██╗╚██╗██╔╝╚██╗ ██╔╝
  ██╔████╔██║██║   ██║   ██╔████╔██║██████╔╝██████╔╝██║   ██║ ╚███╔╝  ╚████╔╝ 
  ██║╚██╔╝██║██║   ██║   ██║╚██╔╝██║██╔═══╝ ██╔══██╗██║   ██║ ██╔██╗   ╚██╔╝  
  ██║ ╚═╝ ██║██║   ██║   ██║ ╚═╝ ██║██║     ██║  ██║╚██████╔╝██╔╝ ██╗   ██║   
  ╚═╝     ╚═╝╚═╝   ╚═╝   ╚═╝     ╚═╝╚═╝     ╚═╝  ╚═╝ ╚═════╝ ╚═╝  ╚═╝   ╚═╝   
       Interactive, Scriptable Terminal HTTP/HTTPS Proxy & Traffic Engine
```
<p выравнивание="центр">
  <img src="https://img.shields.io/badge/mitmproxy-Terminal_Proxy-blue?style=for-the-badge" alt="mitmproxy">
  <img src="https://img.shields.io/badge/Author-mitmproxy_Project-orange?style=for-the-badge" alt="mitmproxy">
  <img src="https://img.shields.io/badge/Category-Network_&_Web_Analysis-green?style=for-the-badge" alt="Сетевой анализ">
  <img src="https://img.shields.io/badge/License-MIT-purple?style=for-the-badge" alt="MIT">
</p>

<p выравнивание="центр">
  <b>🕶️ Интерактивный HTTP-прокси с поддержкой SSL/TLS для перехвата HTTP-прокси для тестеров на проникновение, исследователей безопасности и инженеров-программистов с богатым консольным интерфейсом и API сценариев Python</b>
</p>

---

## 📋 Содержание

- [Обзор и три двоичных файла](#-обзор--три-двоичных файла)
- [Установка и настройка сертификата](#-installation--certificate-setup)
  - [Установка на Linux/macOS](#installing-on-linux--macos)
  - [Установка сертификата CA для перехвата SSL/TLS](#installing-ca-certificate-for-ssltls-interception)
- [Режимы работы: обычный, прозрачный и обратный прокси](#-operating-modes-regular-transparent--reverse-proxy)
- [Интерактивная навигация TUI (`mitmproxy`)](#-interactive-tui-navigation-mitmproxy)
  - [Сочетания клавиш](#keyboard-shortcuts)
  - [Фильтрация и перехват потоков](#filtering--intercepting-flows)
- [Неинтерактивная автоматизация (`mitmdump`)](#-non-interactive-automation-mitmdump)
  - [Запись и воспроизведение трафика](#recording--replaying-traffic)
- [Скрипты и дополнения Python](#-python-scripting--addons)
  - [Скрипт автоматического манипулирования заголовком](#automated-header-manipulation-script)
  - [Вмешательство в тело Live Response](#live-response-body-tampering)
- [Краткая справочная таблица](#-краткая справочная-таблица)
- [Защитные соображения и обнаружение](#-защитные-соображения--обнаружение)
- [Юридическая оговорка](#-юридическая оговорка)

---

## 🎯 Обзор и три двоичных файла

В отличие от громоздких графических прокси-серверов для настольных компьютеров, **mitmproxy** обеспечивает молниеносный, полноценный перехват HTTP/1, HTTP/2 и WebSockets с поддержкой сценариев непосредственно внутри вашего терминала или конвейера автоматизации.

Он поставляется с тремя двоичными файлами точки входа:
1. **`mitmproxy`:** Интерактивный пользовательский интерфейс терминала (TUI) на основе ncurses.
2. **`mitmdump`:** Неинтерактивная версия командной строки (эквивалент `tcpdump` для HTTP).
3. **`mitmweb`:** Легкий графический веб-интерфейс на основе браузера.

---

## 📥 Установка и настройка сертификата

### Установка на Linux/macOS
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y mitmproxy

# Arch Linux
sudo pacman -S mitmproxy

# Python pip / pipx
pipx install mitmproxy
```
### Установка сертификата CA для перехвата SSL/TLS
Чтобы перехватить HTTPS-трафик, цели должны доверять динамическому центру сертификации mitmproxy:
1. Запустите mitmproxy один раз: `mitmproxy` (создает сертификаты в `~/.mitmproxy/`).
2. На клиентском устройстве/браузере настройте прокси-сервер `127.0.0.1:8080`.
3. Перейдите к `http://mitm.it` и загрузите/установите сертификат для Android, iOS, Windows или Linux.

---

## ⚙️ Режимы работы: обычный, прозрачный и обратный прокси.

### Обычный режим прокси (по умолчанию)
Стандартное перенаправление прокси-сервера на порту 8080:
```bash
mitmproxy -p 8080
```
### Режим обратного прокси
Перенаправляет входящий трафик на определенный целевой сервер (идеально подходит для тестирования API):
```bash
mitmproxy --mode reverse:https://api.target.com -p 8080
```
### Прозрачный режим
Перехватывает трафик, маршрутизируемый через iptables, без настройки параметров клиентского прокси:
```bash
mitmproxy --mode transparent --showhost
```
---

## 🖥️ Интерактивная навигация TUI (`mitmproxy`)

### Сочетания клавиш
- `?`: открыть наложение справки.
- `Enter`: проверка выбранного потока HTTP (запрос/ответ/детализация).
- `q`: Вернуться/выйти.
- `d`: удалить текущий поток.
- `r`: Воспроизвести выбранный запрос на сервер.
- `e`: редактирование заголовков, пути или тела запроса/ответа.
- `a`: Возобновить приостановленный поток (при перехвате).
- `A`: возобновить все приостановленные потоки.

### Фильтрация и перехват потоков
Нажмите `i`, чтобы ввести выражение фильтра перехвата:
```text
~u /api/v1/ & ~m POST      # Intercept only POST requests to /api/v1/
~c 401 | ~c 403            # Filter flows by HTTP status code
~b "password"              # Filter flows where body contains "password"
```
---

## ⚡ Неинтерактивная автоматизация (`mitmdump`)

### Запись и воспроизведение трафика
Захватывайте живой трафик непосредственно в двоичный файл дампа:
```bash
# Record all HTTP traffic to file
mitmdump -w traffic.dump

# Read and inspect captured dump
mitmdump -n -r traffic.dump

# Replay client requests from dump against target
mitmdump --client-replay traffic.dump
```
---

## 🐍 Скрипты и дополнения Python

mitmproxy имеет собственный Python API для программного изменения трафика:

### Скрипт автоматического манипулирования заголовками (`add_headers.py`)
```python
from mitmproxy import http

class AddHeader:
    def request(self, flow: http.HTTPFlow):
        # Inject custom authorization or testing header into every outbound request
        flow.request.headers["X-Security-Audit"] = "Authorized-Pentest"
        flow.request.headers["Authorization"] = "Bearer injected_token_xyz"

addons = [AddHeader()]
```
Запустить с:
```bash
mitmproxy -s add_headers.py
```
### Вмешательство в тело Live Response (`tamper_response.py`)
```python
from mitmproxy import http

def response(flow: http.HTTPFlow):
    # Force admin privilege in returned JSON response
    if "api/user/me" in flow.request.url:
        flow.response.text = flow.response.text.replace('"isAdmin": false', '"isAdmin": true')
```
Запустить с:
```bash
mitmdump -s tamper_response.py
```
---

## ⚡ Краткая справочная таблица

| Цель | Команда |
|-----------|---------|
| **Запустить интерактивный TUI** | `mitmproxy -p 8080` |
| **Запустить веб-интерфейс** | `mitmweb -p 8080` |
| **Тихий регистратор командной строки** | `mitmdump -p 8080` |
| **Обратный прокси-сервер** | `mitmproxy --mode reverse:https://target.com` |
| **Запись трафика в файл** | `mitmdump -w capture.mitm` |
| **Загрузить дополнительный скрипт Python**| `mitmproxy -s script.py` |
| **Игнорировать целевые хосты** | `mitmproxy --ignore-hosts ".*apple\.com"` |

---

## 🛡️ Соображения по защите и обнаружению

1. **Закрепление сертификата.** Мобильные приложения, обеспечивающие закрепление SSL, отклоняют сертификат CA mitmproxy. Сопряжение с Фридой / Возражение для обхода проверок на стороне клиента.
2. **Обнаружение прокси-сервера при установлении связи TLS.** Прокси-серверы предоставляют специальные наборы шифров TLS и отпечатки пальцев согласования ALPN, которые могут обнаружить WAF и менеджеры ботов.

---

## ⚠️ Юридическая оговорка

> [!ВНИМАНИЕ]
> mitmproxy перехватывает и расшифровывает конфиденциальные сетевые сообщения. Его необходимо строго развертывать в системах и сетях, где обеспечена явная авторизация. Перехват сообщений без согласия является незаконным.