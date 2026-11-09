<!-- language-home-navigation -->
[🏠 Русская главная страница](../../README.ru.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🕸️ InQL — полная шпаргалка

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ██╗███╗   ██╗ ██████╗ ██╗     
  ██║████╗  ██║██╔═══██╗██║     
  ██║██╔██╗ ██║██║   ██║██║     
  ██║██║╚██╗██║██║▄▄ ██║██║     
  ██║██║ ╚████║╚██████╔╝███████╗
  ╚═╝╚═╝  ╚═══╝ ╚══▀▀═╝ ╚══════╝
      Comprehensive GraphQL Security Assessment & Introspection Engine
```
<p выравнивание="центр">
  <img src="https://img.shields.io/badge/InQL-GraphQL_Security-blue?style=for-the-badge" alt="InQL">
  <img src="https://img.shields.io/badge/Author-Doyensec-orange?style=for-the-badge" alt="Doyensec">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-green?style=for-the-badge" alt="Веб-безопасность">
  <img src="https://img.shields.io/badge/License-BSD--3--Clause-purple?style=for-the-badge" alt="BSD">
</p>

<p выравнивание="центр">
  <b>🕸️ Расширенная система безопасности CLI и расширение Burp Suite, разработанное Doyensec для создания дампов схем интроспекции GraphQL, создания шаблонов запросов и аудита безопасности GraphQL API</b>
</p>

---

## 📋 Содержание

- [Обзор и основы безопасности GraphQL](#-overview--graphql-security-fundamentals)
  - [Опасности запросов самоанализа](#introspection-query-hazards)
- [Установка](#-установка)
- [Сброс интроспекции и генерация схемы](#-интроспекция-дампинг-генерация схемы)
  - [Запрос CLI-интроспекции](#cli-introspection-query)
  - [Сброс в язык определения схемы (SDL)](#dumping-to-schema-definition-language-sdl)
  - [Создание готовых к выполнению шаблонов запросов](#генерация-готовых-к-запросам-шаблонов)
- [Аудит мутаций и конфиденциальных запросов](#-auditing-mutations--sensitive-queries)
- [Обнаружение ошибок отказа в обслуживании и пакетной обработки](#-detecting-denial-of-service--batching-flaws)
  - [Пакетный запрос (пакетирование массивов)](#batch-querying-array-batching)
  - [Циркулярные ссылки и вложенные запросы](#circular-references--nested-queries)
- [Краткая справочная таблица](#-краткая справочная-таблица)
- [Защитные соображения и усиление](#-защитные-соображения--укрепление)
- [Юридическая оговорка](#-юридическая оговорка)

---

## 🎯 Обзор и основы безопасности GraphQL

В отличие от стандартных REST API с дискретными URL-адресами (`/api/v1/users`), GraphQL предоставляет одну конечную точку HTTP (обычно `/graphql`), где клиенты запрашивают пользовательские формы данных посредством запросов и мутаций.

### Опасности запросов самоанализа
GraphQL имеет встроенную систему самоанализа (`__schema`, `__type`), предназначенную для инструментов разработчика. Если оставить включенным в производстве:
- Злоумышленники могут запросить всю внутреннюю схему.
- Невыпущенные функции, внутренние административные изменения (например, `deleteUser`, `changeRole`) и частные поля полностью раскрыты.

**InQL**, созданный **Doyensec**, автоматизирует самоанализ, анализирует схемы, генерирует документацию и создает готовые к использованию шаблоны эксплойтов.

---

## 📥 Установка
```bash
# Using Python pip (Recommended)
pip install inql

# Or clone from GitHub
git clone https://github.com/doyensec/inql.git
cd inql && pip install .

# Verify installation
inql -h
```
---

## 🚀 Самоанализ и генерация схемы

### Запрос самоанализа CLI
Опросите удаленную конечную точку GraphQL, извлеките полную схему и сохраните ее в выходной папке:
```bash
inql -t https://target.com/graphql -o /tmp/graphql_audit/
```
*Сгенерировано в выходном каталоге:*
- `schema.json`: необработанный дамп самоанализа JSON.
- `schema.graphql`: удобочитаемый язык определения схемы (SDL).
- `queries/`: автоматически создаваемые шаблоны запросов для каждого доступного запроса.
- `mutations/`: автоматически генерируемые шаблоны мутаций для модификации данных.

### Аутентифицированное сканирование и пользовательские заголовки
Передавайте файлы cookie сеанса, токены авторизации или пользовательские заголовки:
```bash
inql -t https://target.com/graphql \
  -H "Authorization: Bearer <TOKEN>" \
  -H "X-API-Key: secret123" \
  -o ./audit_report/
```
### Сброс на язык определения схемы (SDL)
Преобразуйте существующую необработанную схему JSON в SDL:
```bash
inql -f schema.json --generate-sdl -o ./sdl_output/
```
---

## 💥 Аудит мутаций и конфиденциальных запросов

Просмотрите автоматически созданные файлы мутаций в выходной папке:
```text
mutations/
├── registerUser.graphql
├── resetPassword.graphql
├── updateProfile.graphql
└── adminPromoteUser.graphql
```
Отправляйте высокоэффективные мутации с помощью Curl или Burp Suite:
```bash
curl -X POST https://target.com/graphql \
  -H "Content-Type: application/json" \
  -d '{"query": "mutation { adminPromoteUser(userId: 10, role: \"SUPERADMIN\") { success } }"}'
```
---

## ⚡ Обнаружение ошибок отказа в обслуживании и пакетной обработки

### Пакетный запрос (пакетирование массивов)
Проверьте, обрабатывает ли сервер массивы запросов в одном HTTP-запросе (обходит ограничение скорости):
```json
[
  {"query": "query { getUser(id: 1) { email } }"},
  {"query": "query { getUser(id: 2) { email } }"},
  {"query": "query { getUser(id: 3) { email } }"}
]
```
### Циклические ссылки и вложенные запросы
Тест на сбои рекурсивных отношений:
```graphql
query {
  thread {
    messages {
      thread {
        messages {
          thread {
            messages {
              id
            }
          }
        }
      }
    }
  }
}
```
---

## ⚡ Краткая справочная таблица

| Цель | Команда |
|-----------|---------|
| **Дамп самоанализа** | `inql -t https://target.com/graphql -o ./out/` |
| **Аутентифицированный самоанализ**| `inql -t <URL> -H "Authorization: Bearer <TOKEN>" -o ./out/` |
| **Преобразование схемы JSON в SDL** | `inql -f schema.json --generate-sdl -o ./sdl/` |
| **Создание HTML-документов** | `inql -t <URL> --generate-html -o ./docs/` |

---

## 🛡️ Соображения по защите и закалке

1. **Отключите интроспекцию в производстве.** Отключите `introspection` в конфигурации вашего сервера GraphQL (Apollo, Yoga, GraphQL-Java).
2. **Обеспечение глубины запроса и анализа стоимости.** Внедрите такие библиотеки, как `graphql-depth-limit`, или анализ стоимости запросов, чтобы исключить глубоко вложенные запросы.
3. **Отключить пакетную обработку.** Ограничьте или отключите пакетное выполнение запросов на основе массива, если это не является строго необходимым.

---

## ⚠️ Юридическая оговорка

> [!ВНИМАНИЕ]
> InQL разработан для авторизованных оценок безопасности, тестов на проникновение и аудита безопасности API. Извлечение схем или изменение данных на неавторизованных конечных точках является незаконным.