<!-- language-home-navigation -->
[🏠 Русская главная страница](../../README.ru.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🐽 TruffleHog — Полная шпаргалка

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ████████╗██████╗ ██╗   ██╗███████╗███████╗██╗     ███████╗██╗  ██╗ ██████╗  ██████╗ 
  ╚══██╔══╝██╔══██╗██║   ██║██╔════╝██╔════╝██║     ██╔════╝██║  ██║██╔═══██╗██╔════╝ 
     ██║   ██████╔╝██║   ██║█████╗  █████╗  ██║     █████╗  ███████║██║   ██║██║  ███╗
     ██║   ██╔══██╗██║   ██║██╔══╝  ██╔══╝  ██║     ██╔══╝  ██╔══██║██║   ██║██║   ██║
     ██║   ██║  ██║╚██████╔╝██║     ██║     ███████╗███████╗██║  ██║╚██████╔╝╚██████╔╝
     ╚═╝   ╚═╝  ╚═╝ ╚═════╝ ╚═╝     ╚═╝     ╚══════╝╚══════╝╚═╝  ╚═╝ ╚═════╝  ╚═════╝ 
       Find Exposed Credentials & Secrets with Real-Time Active Verification
```
<p выравнивание="центр">
  <img src="https://img.shields.io/badge/TruffleHog-Secret_Scanner-blue?style=for-the-badge" alt="TruffleHog">
  <img src="https://img.shields.io/badge/Developer-Truffle_Security-orange?style=for-the-badge" alt="Truffle Security">
  <img src="https://img.shields.io/badge/Category-OSINT_&_Code_Security-green?style=for-the-badge" alt="Безопасность кода">
  <img src="https://img.shields.io/badge/License-AGPLv3-red?style=for-the-badge" alt="Лицензия AGPLv3">
</p>

<p выравнивание="центр">
  <b>🌐 Высокоточный секретный сканер, предназначенный для поиска утечек ключей API, токенов и частных сертификатов в Git, S3, Docker и файловых системах с проверкой в реальном времени.</b>
</p>

---

## 📋 Содержание

- [Обзор и суперсила живой проверки](#-overview--the-live-verification-superpower)
- [Сравнение TruffleHog и Gitleaks](#-trufflehog-vs-gitleaks-сравнение)
- [Установка](#-установка)
- [Сканирование репозиториев Git](#-scanning-git-repositories)
- [Сканирование локальных файловых систем и каталогов](#-scanning-local-filesystems--directory)
- [Сканирование целых организаций GitHub](#-сканирование-всех-github-организаций)
- [Сканирование сегментов S3 и изображений Docker](#-scanning-s3-buckets--docker-images)
- [Фильтрация ложных срабатываний (`--only-verified`)](#-filtering-false-positives---только проверенные)
- [Вывод JSON и интеграция CI/CD](#-json-output--cicd-integration)
- [Краткая справочная таблица](#-краткая справочная-таблица)
- [Защитные соображения и OPSEC](#-защитные-соображения--opsec)
- [Юридическая оговорка](#-юридическая оговорка)

---

## 🎯 Обзор и суперсила живой проверки

Разработчики часто передают учетные данные с высоким уровнем привилегий в репозитории Git (ключи доступа AWS, закрытые ключи Stripe, веб-перехватчики Slack, закрытые ключи SSH, пароли базы данных). Даже будучи удаленными при последующих коммитах, **эти секреты остаются навсегда похороненными в истории коммитов репозитория**.

Написанный на Go **Truffle Security**, **TruffleHog** сканирует историю коммитов, файловые системы и облачные хранилища для обнаружения утекших секретов.

Его определяющей суперсилой является **Живая проверка**:
В отличие от традиционных сканеров регулярных выражений, которые заваливают команды безопасности тысячами ложных срабатываний (например, примеров ключей документации), TruffleHog связывается с соответствующим поставщиком облачных услуг (AWS, GitHub, Slack, OpenAI), чтобы **проверить, активно ли работает извлеченный секрет**.

---

## ⚖️ Сравнение TruffleHog и Gitleaks

| Особенность | ТрюфельХог | Гитликс |
|---------|------------|----------|
| **Живая проверка учетных данных** | ✅ Да (более 750 детекторов протестированы вживую)| ❌ Нет (только регулярное выражение) |
| **Целевые источники данных** | Git, GitHub, S3, Docker, файловая система| Git, локальные файлы |
| **Скорость работы с огромными репозиториями** | Быстро (многопоточный режим) | Чрезвычайно быстро (минимальное регулярное выражение) |
| **Доля ложноположительных результатов** | Около нуля (с `--only-verified`) | Умеренный (требуются белые списки)|
| **Доступность к перехвату перед фиксацией** | ✅ Поддерживается | ✅ Высокая оптимизация |

---

## 📥 Установка
```bash
# Debian / Ubuntu / Kali Linux (via curl installer)
curl -sSfL https://raw.githubusercontent.com/trufflesecurity/trufflehog/main/scripts/install.sh | sudo sh -s -- -b /usr/local/bin

# Arch Linux (AUR)
yay -S trufflehog-bin

# Homebrew (macOS / Linux)
brew install trufflehog

# Docker Container
docker run -it --rm trufflesecurity/trufflehog:latest git https://github.com/target/repo
```
---

## 🚀 Сканирование репозиториев Git
```bash
# Scan a remote Git repository (including entire commit history)
trufflehog git https://github.com/targetcorp/public-project

# Scan an internal on-premise Git repository with authentication
trufflehog git https://username:token@gitlab.corp.local/repo.git

# Scan only the most recent commit (great for pre-commit hooks)
trufflehog git file://./ --since-commit HEAD~1
```
---

## 📂 Сканирование локальных файловых систем и каталогов

Сканируйте локальные каталоги, извлеченные резервные архивы или деиндексированные корни веб-страниц:
```bash
# Scan current directory recursively
trufflehog filesystem .

# Scan specific sensitive configuration directory
trufflehog filesystem /var/www/html/ --exclude-paths tests/,vendor/
```
---

## 🏢 Сканирование целых организаций GitHub

Проведите аудит всех публичных репозиториев, принадлежащих организации:
```bash
# Scan all public repos of a GitHub organization
trufflehog github --org=targetorg --token=YOUR_GITHUB_TOKEN

# Scan all private and public repositories across a personal account
trufflehog github --user=targetuser --token=YOUR_GITHUB_TOKEN
```
---

## ☁️ Сканирование сегментов S3 и образов Docker
```bash
# Scan an AWS S3 bucket for exposed credentials in stored logs and backups
trufflehog s3 --bucket=target-backup-bucket

# Scan a Docker container image layers for baked-in secrets
trufflehog docker --image=targetcorp/api-server:latest
```
---

## 🎯 Фильтрация ложных срабатываний (`--only-verified`)

Подавить все непроверенные попадания в регулярные выражения и отображать **только подтвержденные активные учетные данные**:
```bash
# Report ONLY confirmed functional credentials verified against providers
trufflehog git https://github.com/target/repo --only-verified

# Scan filesystem for verified secrets only
trufflehog filesystem /opt/backups/ --only-verified
```
---

## 📊 Вывод JSON и интеграция CI/CD
```bash
# Export findings to structured JSON format
trufflehog git https://github.com/target/repo --json > secrets_report.json

# Parse live AWS keys with jq
cat secrets_report.json | jq -r 'select(.Verified == true) | .Raw'
```
---

## ⚡ Краткая справочная таблица

| Цель | Команда |
|-----------|---------|
| **Сканировать удаленный репозиторий Git** | `trufflehog git <GIT_URL>` |
| **Сканировать только проверенные секреты** | `trufflehog git <GIT_URL> --only-verified` |
| **Сканировать локальную файловую систему** | `trufflehog filesystem <DIRECTORY>` |
| **Сканировать организацию GitHub** | `trufflehog github --org=<ORG> --token=<PAT>` |
| **Сканировать изображение Docker** | `trufflehog docker --image=<IMAGE>` |
| **Сканирование сегмента S3** | `trufflehog s3 --bucket=<BUCKET_NAME>` |
| **Экспорт в JSON** | `trufflehog git <GIT_URL> --json` |

---

## 🛡️ Оборонительные соображения и OPSEC

1. **Исходящий трафик проверки API:** При работе без `--no-verification` TruffleHog подключается к сторонним конечным точкам (Slack, AWS, Stripe) для проверки ключей. Это запускает журналы аутентификации в клиенте облака жертвы! Если скрытность имеет первостепенное значение, отключите живую проверку:
   ```bash
   trufflehog git <URL> --no-verification
   ```
2. **Отзыв ключа и реагирование на инциденты.** При обнаружении действительных секретов в общедоступных репозиториях немедленно замените учетные данные и проверьте журналы CloudTrail/аудит на предмет несанкционированного доступа.

---

## ⚠️ Юридическая оговорка

> [!ВНИМАНИЕ]
> Эта шпаргалка предназначена для авторизованных проверок безопасности, операций Red Team и оценки гигиены учетных данных. Использование обнаруженных учетных данных для доступа к неавторизованным учетным записям или частным облачным средам строго незаконно.