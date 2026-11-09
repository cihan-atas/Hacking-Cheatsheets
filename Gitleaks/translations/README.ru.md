<!-- language-home-navigation -->
[🏠 Русская главная страница](../../README.ru.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🔑 Gitleaks — полная шпаргалка

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
   ██████╗ ██╗████████╗██╗     ███████╗ █████╗ ██╗  ██╗███████╗
  ██╔════╝ ██║╚══██╔══╝██║     ██╔════╝██╔══██╗██║ ██╔╝██╔════╝
  ██║  ███╗██║   ██║   ██║     █████╗  ███████║█████╔╝ ███████╗
  ██║   ██║██║   ██║   ██║     ██╔══╝  ██╔══██║██╔═██╗ ╚════██║
  ╚██████╔╝██║   ██║   ███████╗███████╗██║  ██║██║  ██╗███████║
   ╚═════╝ ╚═╝   ╚═╝   ╚══════╝╚══════╝╚═╝  ╚═╝╚═╝  ╚═╝╚══════╝
           Fast & Automated Secret Detection for Git Repositories
```
<p выравнивание="центр">
  <img src="https://img.shields.io/badge/Gitleaks-Secret_Scanner-blue?style=for-the-badge" alt="Gitleaks">
  <img src="https://img.shields.io/badge/Author-Zachary_Rice-orange?style=for-the-badge" alt="zricethezav">
  <img src="https://img.shields.io/badge/Category-Source_Code_&_Secrets-green?style=for-the-badge" alt="Secrets">
  <img src="https://img.shields.io/badge/Language-Go-cyan?style=for-the-badge" alt="Go">
</p>

<p выравнивание="центр">
  <b>🔑 Инструмент SAST, предназначенный для поиска и обнаружения жестко запрограммированных секретов, таких как пароли, ключи API и токены, в репозиториях Git и истории коммитов.</b>
</p>

---

## 📋 Содержание

- [Обзор и механизм обнаружения](#-overview--detection-engine)
- [Установка](#-установка)
- [Базовое секретное сканирование](#-базовое-секретное-сканирование)
  - [Сканирование репозитория Git](#scanning-a-git-repository)
  - [Сканирование незафиксированных изменений](#scanning-uncommitted-changes)
  - [Сканирование простых файлов и папок](#scanning-plain-files--folders)
- [Диапазоны и история фиксации таргетинга](#-targeting-commit-ranges--history)
- [CI/CD и интеграция перед фиксацией] (#-cicd--pre-commit-integration)
- [Пользовательские правила обнаружения и белый список](#-custom-detection-rules--whitelisting)
- [Форматы вывода и отчеты](#-output-formats--reporting)
- [Краткая справочная таблица](#-краткая справочная-таблица)
- [Защитные соображения и исправление](#-защитные-соображения--восстановление)
- [Юридическая оговорка](#-юридическая оговорка)

---

## 🎯 Обзор и механизм обнаружения

Жестко закодированные секреты (ключи доступа AWS, закрытые ключи SSH, токены GitHub, веб-перехватчики Slack, учетные данные базы данных), случайно помещенные в репозитории git, являются одним из наиболее распространенных векторов первоначального взлома облака.

Созданный **Закари Райсом (@zricethezav)**, **Gitleaks** обеспечивает сверхбыстрый анализ на основе регулярных выражений и энтропии Шеннона для:
- **Полная история Git:** проверяет каждую ветку, тег и историческую фиксацию.
- **Перехватчики перед фиксацией:** не позволяют инженерам случайно зафиксировать учетные данные до того, как код достигнет удаленных серверов.
- **Конвейеры CI/CD:** автоматически отклоняет запросы на включение, содержащие утечки учетных данных.

---

## 📥 Установка
```bash
# macOS (Homebrew)
brew install gitleaks

# Go install
go install github.com/zricethezav/gitleaks/v8@latest

# Docker
docker pull zricethezav/gitleaks:latest

# Linux binary download
curl -sSL https://github.com/zricethezav/gitleaks/releases/latest/download/gitleaks_linux_x64.tar.gz | tar -xz
sudo mv gitleaks /usr/local/bin/
```
---

## 🚀 Базовое секретное сканирование

### Сканирование репозитория Git
Проверяет историю коммитов текущего репозитория:
```bash
# Run detection in current working repo
gitleaks detect -v

# Audit an external local repo path
gitleaks detect --source /path/to/repo -v
```
### Сканирование незафиксированных изменений
Аудит проиндексированных и непроиндексированных изменений кода перед фиксацией:
```bash
# Inspect unstaged working directory changes
gitleaks protect -v

# Scan staged changes only
gitleaks protect --staged -v
```
### Сканирование обычных файлов и папок
Сканировать каталоги, не относящиеся к git (например, извлеченные прошивки, декомпилированные мобильные приложения, папки конфигурации):
```bash
gitleaks dir /path/to/directory -v
```
---

## ⚡ Таргетинг на диапазоны фиксации и историю
```bash
# Scan commits made in the last 7 days
gitleaks detect --log-opts="--since='7 days ago'" -v

# Scan specific branch or tag comparison
gitleaks detect --log-opts="origin/main..feature-branch" -v

# Target the latest 5 commits
gitleaks detect --log-opts="-n 5" -v
```
---

## ⚙️ CI/CD и интеграция перед фиксацией

### Конфигурация платформы предварительной фиксации (`.pre-commit-config.yaml`)
```yaml
repos:
  - repo: https://github.com/gitleaks/gitleaks
    rev: v8.18.2
    hooks:
      - id: gitleaks
```
### Рабочий процесс действий GitHub
```yaml
name: Gitleaks Security Scan
on: [push, pull_request]
jobs:
  gitleaks:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
        with:
          fetch-depth: 0
      - uses: gitleaks/gitleaks-action@v2
        env:
          GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}
```
---

## 🧩 Пользовательские правила обнаружения и белый список

Настройте собственные шаблоны регулярных выражений или игнорируйте ложные срабатывания с помощью `.gitleaks.toml`:
```toml
[extend]
useDefault = true

[allowlist]
description = "Ignore test fixtures and demo tokens"
paths = [
  '''tests/.*''',
  '''fixtures/.*'''
]
regexes = [
  '''TEST_API_KEY_.*'''
]

[[rules]]
id = "custom-internal-token"
description = "Internal Company JWT Secret"
regex = '''(?i)(corp_secret_[a-z0-9]{32})'''
secretGroup = 1
entropy = 3.5
keywords = ["corp_secret_"]
```
Запуск с пользовательской конфигурацией:
```bash
gitleaks detect -c .gitleaks.toml -v
```
---

## 📊 Форматы вывода и отчеты
```bash
# Export results to clean JSON format
gitleaks detect --report-path gitleaks-report.json --report-format json

# Export to SARIF (for GitHub Security tab integration)
gitleaks detect --report-path report.sarif --report-format sarif

# Export to CSV
gitleaks detect --report-path report.csv --report-format csv
```
---

## ⚡ Краткая справочная таблица

| Цель | Команда |
|-----------|---------|
| **Сканировать всю историю Git** | `gitleaks detect -v` |
| **Сканировать незафиксированные изменения** | `gitleaks protect -v` |
| **Сканировать только поэтапные изменения** | `gitleaks protect --staged -v` |
| **Сканировать обычный каталог (без Git)**| `gitleaks dir /path/to/folder -v` |
| **Экспорт в JSON** | `gitleaks detect -r report.json -f json` |
| **Экспорт в SARIF** | `gitleaks detect -r report.sarif -f sarif` |
| **Использовать конфигурацию пользовательских правил** | `gitleaks detect -c .gitleaks.toml` |
| **Сканировать конкретную ветку** | `gitleaks detect --log-opts="main..dev"` |

---

## 🛡️ Соображения по поводу защиты и исправления

1. **Немедленный отзыв.** Как только секрет будет обнаружен в истории git, считайте его полностью скомпрометированным и немедленно отмените/отмените его.
2. **Перезапись истории Git.** Удаление коммита или использование `git filter-repo`/BFG Repo-Cleaner необходимо для очистки артефактов, но никогда не заменяйте перезапись истории ротацией учетных данных.

---

## ⚠️ Юридическая оговорка

> [!ВНИМАНИЕ]
> Gitleaks предназначен для авторизованного внутреннего аудита безопасности, непрерывной проверки уязвимостей интеграции и защитной проверки кода. Сканирование неавторизованных проприетарных репозиториев без разрешения строго запрещено.