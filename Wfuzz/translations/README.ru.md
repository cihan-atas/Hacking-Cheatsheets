<!-- language-home-navigation -->
[🏠 Русская главная страница](../../README.ru.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🎯 Wfuzz — Полная шпаргалка

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ██╗    ██╗███████╗██╗   ██╗███████╗███████╗
  ██║    ██║██╔════╝██║   ██║╚══███╔╝╚══███╔╝
  ██║ █╗ ██║█████╗  ██║   ██║  ███╔╝   ███╔╝ 
  ██║███╗██║██╔══╝  ██║   ██║ ███╔╝   ███╔╝  
  ╚███╔███╔╝██║     ╚██████╔╝███████╗███████╗
   ╚══╝╚══╝ ╚═╝      ╚═════╝ ╚══════╝╚══════╝
       The Flexible Web Application Fuzzer & Parameter Explorer
```
<p выравнивание="центр">
  <img src="https://img.shields.io/badge/Wfuzz-Web_Fuzzer-blue?style=for-the-badge" alt="Wfuzz">
  <img src="https://img.shields.io/badge/Language-Python-orange?style=for-the-badge" alt="Язык Python">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-green?style=for-the-badge" alt="Веб-безопасность и API">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="Лицензия GPLv2">
</p>

<p выравнивание="центр">
  <b>🌐 Гибко настраиваемый HTTP-фаззер, предназначенный для обнаружения скрытых параметров, обходов, заголовков и матриц с несколькими полезными нагрузками.</b>
</p>

---

## 📋 Содержание

– [Обзор и архитектура ключевых слов](#-overview--the-keyword-architecture)
- [Сравнение Wfuzz и ffuf] (#-wfuzz-vs-ffuf-сравнение)
- [Установка](#-установка)
- [Базовый каталог и обнаружение файлов](#-basic-directory--file-discovery)
- [Фильтрация и скрытие шума (`--hc`, `--hl`, `--hw`, `--hh`)](#-filtering--hiding-noise---hc---hl---hw---hh)
- [Типы полезной нагрузки и динамические кодировщики (`-z`)](#-payload-types--dynamic-encoders--z)
- [Фаззинг параметров и скрытых переменных](#-параметр--скрытая-переменная-фаззинг)
- [Заголовки, файлы cookie и аутентификация](#-headers-cookies--аутентификация)
- [Многопараметрический фаззинг (`FUZZ` и `FUZ2Z`)](#-multi-variable-fuzzing-fuzz-and-fuz2z)
- [Маршрутизация через Burp Suite (`-p`)](#-routing-through-burp-suite--p)
- [Краткая справочная таблица](#-краткая справочная-таблица)
- [Защитные соображения и OPSEC](#-защитные-соображения--opsec)
- [Юридическая оговорка](#-юридическая оговорка)

---

## 🎯 Обзор и архитектура ключевых слов

**Wfuzz**, написанный на Python **Хави Мендесом**, представляет собой классический, проверенный на практике инструмент для оценки веб-приложений. Он заменяет назначенные заполнители (по умолчанию: `FUZZ`) внутри URL-адресов, параметров, заголовков и тел POST на записи словаря или автоматически сгенерированные полезные данные.

Wfuzz выделяется своей непревзойденной **универсальностью полезной нагрузки** (диапазоны, генераторы IP, строки регулярных выражений, кодировщики «на лету», такие как MD5 или Base64) и модульной механикой фильтрации.

---

## ⚖️ Сравнение Wfuzz и ffuf

| Особенность | Вфузз | ффуф |
|---------|-------|------|
| **Язык реализации** | Питон | Перейти |
| **Чистая скорость** | ⭐⭐⭐ (Хорошо) | ⭐⭐⭐⭐⭐ (невероятно быстро) |
| **Динамические кодировщики полезной нагрузки**| ✅ Родной (`-z file,pass.txt,md5`) | ❌ Требуются внешние скрипты |
| **Комбинации нескольких полезных нагрузок**| ✅ Родной (`FUZZ`, `FUZ2Z`, `FUZ3Z`) | ✅ Поддерживается (`-w w1:W1 -w w2:W2`) |
| **Встроенные генераторы полезной нагрузки**| ✅ Диапазоны, IP-адреса, шестнадцатеричные значения, словари| ❌ Только списки слов файлов |
| **Использование ресурсов** | Умеренная ОЗУ/ЦП | Низкий объем памяти |

---

## 📥 Установка
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y wfuzz

# Python Pip
pip3 install wfuzz

# Arch Linux
sudo pacman -S wfuzz
```
---

## 🚀 Базовый каталог и обнаружение файлов
```bash
# Basic directory fuzzing
wfuzz -w /usr/share/wordlists/dirb/common.txt http://target.local/FUZZ

# Fuzzing file extensions
wfuzz -w extensions.txt http://target.local/index.FUZZ

# Fuzzing hidden backup files
wfuzz -w words.txt http://target.local/FUZZ.bak
```
---

## 🎯 Фильтрация и скрытие шума (`--hc`, `--hl`, `--hw`, `--hh`)

Реальные серверы возвращают ответы даже для несуществующих страниц (пользовательские страницы 404 или перенаправления). Wfuzz предоставляет флаги детальной фильтрации:
```bash
# Hide specific HTTP status codes (e.g. 404, 403)
wfuzz -w wordlist.txt --hc 404,403 http://target.local/FUZZ

# Hide responses by line count (filters out static custom 404 pages)
wfuzz -w wordlist.txt --hl 42 http://target.local/FUZZ

# Hide responses by word count
wfuzz -w wordlist.txt --hw 120 http://target.local/FUZZ

# Hide responses by character count / body length
wfuzz -w wordlist.txt --hh 0,1052 http://target.local/FUZZ

# Show ONLY HTTP 200 and 302 responses
wfuzz -w wordlist.txt --sc 200,302 http://target.local/FUZZ
```
---

## 📦 Типы полезной нагрузки и динамические кодировщики (`-z`)

Wfuzz предоставляет встроенные генераторы, которые устраняют необходимость в предварительно созданных текстовых файлах:
```bash
# Numeric Range Fuzzing (e.g., IDOR testing from 1 to 500)
wfuzz -z range,1-500 http://target.local/user.php?id=FUZZ

# Custom Inline List
wfuzz -z list,admin-root-test-dev http://target.local/FUZZ/

# On-the-fly MD5 encoding of wordlist entries
wfuzz -z file,passwords.txt,md5 http://target.local/login?hash=FUZZ

# On-the-fly Base64 encoding
wfuzz -z file,usernames.txt,base64 http://target.local/auth?token=FUZZ
```
---

## 🔍 Фаззинг параметров и скрытых переменных

Откройте для себя несвязанные параметры GET/POST:
```bash
# GET parameter discovery
wfuzz -w /usr/share/seclists/Discovery/Web-Content/burp-parameter-names.txt \
  --hc 404 --hh 2340 "http://target.local/search.php?FUZZ=test"

# POST body parameter discovery
wfuzz -w parameters.txt -d "FUZZ=admin" --hc 404 http://target.local/api/update
```
---

## 🍪 Заголовки, файлы cookie и аутентификация
```bash
# Cookie Fuzzing (Session hijacking / privilege testing)
wfuzz -w wordlist.txt -b "session=FUZZ" http://target.local/dashboard

# HTTP Header Fuzzing (IP bypasses via X-Forwarded-For)
wfuzz -z file,ips.txt -H "X-Forwarded-For: FUZZ" http://target.local/admin

# Basic Authentication brute force
wfuzz -z file,users.txt -z file,passwords.txt --basic "FUZZ:FUZ2Z" http://target.local/protected/
```
---

## ⚡ Многопараметрический фаззинг (`FUZZ` и `FUZ2Z`)

Объедините несколько списков слов, используя отдельные теги-заполнители:
```bash
# Test username and password matrix simultaneously
wfuzz -w users.txt -w passwords.txt -d "user=FUZZ&pass=FUZ2Z" --hc 200 http://target.local/login.php

# Fuzz path and extension together
wfuzz -w words.txt -w extensions.txt http://target.local/FUZZ.FUZ2Z
```
---

## 🔁 Маршрутизация через Burp Suite (`-p`)

Проверьте полезные данные и ответы внутри Burp Suite:
```bash
# Route requests through local Burp Suite proxy
wfuzz -p 127.0.0.1:8080:HTTP -w wordlist.txt http://target.local/FUZZ
```
---

## ⚡ Краткая справочная таблица

| Цель | Команда |
|-----------|---------|
| **Фаззинг каталогов** | `wfuzz -w <DICT> <URL>/FUZZ` |
| **Скрыть коды состояния HTTP** | `wfuzz -w <DICT> --hc 404,403 <URL>/FUZZ` |
| **Скрыть по длине ответа** | `wfuzz -w <DICT> --hh <LENGTH> <URL>/FUZZ` |
| **Параметр Fuzz GET** | `wfuzz -w <PARAMS> "<URL>?FUZZ=test"` |
| **Тело Fuzz POST** | `wfuzz -w <PARAMS> -d "FUZZ=test" <URL>` |
| **Полезные данные числового диапазона** | `wfuzz -z range,1-100 "<URL>?id=FUZZ"` |
| **Кодируйте полезные данные на лету**| `wfuzz -z file,<DICT>,base64 "<URL>?token=FUZZ"` |
| **Фаззинг по нескольким ключевым словам** | `wfuzz -w <W1> -w <W2> -d "u=FUZZ&p=FUZ2Z" <URL>` |
| **Маршрут через Burp Proxy** | `wfuzz -p 127.0.0.1:8080:HTTP -w <DICT> <URL>/FUZZ` |

---

## 🛡️ Оборонительные соображения и OPSEC

1. **Блокировка WAF.** Фаззинг сотен запросов в секунду активирует ограничение скорости Cloudflare, AWS WAF и ModSecurity. Используйте `-s <seconds>` для регулирования скорости запроса.
2. **Исчерпание ресурсов.** Тяжелый фаззинг неиндексированных запросов к базе данных может привести к отказу в обслуживании (DoS) на устаревших веб-серверах.

---

## ⚠️ Юридическая оговорка

> [!ВНИМАНИЕ]
> Эта шпаргалка предназначена только для авторизованного тестирования на проникновение и аудита уязвимостей. Фаззинг неавторизованных приложений строго запрещен.