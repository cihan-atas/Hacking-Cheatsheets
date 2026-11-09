<!-- language-home-navigation -->
[🏠 Русская главная страница](../../README.ru.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 📂 GitTools — полная шпаргалка

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
   ██████╗ ██╗████████╗████████╗ ██████╗  ██████╗ ██╗     ███████╗
  ██╔════╝ ██║╚══██╔══╝╚══██╔══╝██╔═══██╗██╔═══██╗██║     ██╔════╝
  ██║  ███╗██║   ██║      ██║   ██║   ██║██║   ██║██║     ███████╗
  ██║   ██║██║   ██║      ██║   ██║   ██║██║   ██║██║     ╚════██║
  ╚██████╔╝██║   ██║      ██║   ╚██████╔╝╚██████╔╝███████╗███████║
   ╚═════╝ ╚═╝   ╚═╝      ╚═╝    ╚═════╝  ╚═════╝ ╚══════╝╚══════╝
      Exposed /.git/ Repository Dumping, Extraction & Secret Hunting
```
<p выравнивание="центр">
  <img src="https://img.shields.io/badge/GitTools-Git_Dumping-blue?style=for-the-badge" alt="GitTools">
  <img src="https://img.shields.io/badge/Author-Internetwache-orange?style=for-the-badge" alt="internetwache">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_Secrets-green?style=for-the-badge" alt="Веб-безопасность">
  <img src="https://img.shields.io/badge/License-MIT-purple?style=for-the-badge" alt="MIT">
</p>

<p выравнивание="центр">
  <b>📂 Необходимый набор инструментов для обнаружения открытых папок <code>/.git/</code> на веб-серверах, загрузки необработанных хранилищ объектов Git и полной реконструкции исходного исходного кода и истории коммитов</b>
</p>

---

## 📋 Содержание

- [Обзор и выявленная опасность .git](#-overview--the-expose-git-hazard)
- [Установка](#-установка)
- [Три модуля GitTools](#-the-three-gittools-modules)
  - [1. Дампер (`gitdumper.sh`)](#1-dumper-gitdumpersh)
  - [2. Экстрактор (`extractor.sh`)](#2-экстрактор-экстракторш)
  - [3. Finder (`finder.py`)](#3-finder-finderpy)
- [Альтернативный дамп Python: `git-dumper`](#-alternative-python-dumper-git-dumper)
- [Поиск секретов и учетных данных после реконструкции](#-post-reconstruction-secret--credential-hunting)
- [Краткая справочная таблица](#-краткая справочная-таблица)
- [Защитные соображения и усиление защиты веб-сервера](#-defensive-considerations--web-server-hardening)
- [Юридическая оговорка](#-юридическая оговорка)

---

## 🎯 Обзор и выявленная опасность .git

Когда разработчики развертывают веб-приложения через `git clone` или автоматизированные сценарии без надлежащего ограничения доступа к скрытым каталогам, папка **`/.git/`** становится доступной через HTTP/HTTPS:
`https://target.com/.git/`

Даже если список каталогов (индексирование) отключен, детерминированная структура Git позволяет злоумышленнику загрузить `/.git/HEAD`, `/.git/config`, `/.git/index` и пройти через все свободные объекты Git (`.git/objects/xx/xxxx...`). Это позволяет провести полную реконструкцию:
- Полный исходный код приложения и внутренние API.
— Учетные данные базы данных, токены API и частные ключи SSH в файлах конфигурации.
- Полные журналы коммитов git, электронные письма авторов и ветки разработки.

---

## 📥 Установка
```bash
# Clone GitTools repository
git clone https://github.com/internetwache/GitTools.git
cd GitTools
chmod +x Dumper/gitdumper.sh Extractor/extractor.sh

# Install alternative standalone Python git-dumper via pip
pip install git-dumper
```
---

## 🚀 Три модуля GitTools

GitTools состоит из трех специализированных утилит:

### 1. Самосвал (`gitdumper.sh`)
Загружает как можно большую часть каталога `.git` с веб-сервера:
```bash
# Syntax: ./gitdumper.sh <URL_TO_GIT> <LOCAL_DESTINATION>
./Dumper/gitdumper.sh https://target.com/.git/ /tmp/target_repo/
```
### 2. Экстрактор (`extractor.sh`)
Восстанавливает исходные файлы, деревья коммитов и ветки из сброшенных незакрепленных объектов `.git`:
```bash
# Syntax: ./extractor.sh <DUMPED_DIR> <OUTPUT_CODE_DIR>
./Extractor/extractor.sh /tmp/target_repo/ /tmp/extracted_source/
```
Внутри `/tmp/extracted_source/` каждый коммит восстанавливается в изолированный каталог, содержащий полное дерево файлов на данный момент времени.

### 3. Искатель (`finder.py`)
Сканирует списки доменов или блоки CIDR для обнаружения открытых каталогов `.git`:
```bash
python3 Finder/finder.py -i targets.txt -u "Mozilla/5.0"
```
---

## 🐍 Альтернативный дамп Python: `git-dumper`

`git-dumper` — это многопоточная альтернатива Python, которая автоматически реконструирует работающий git-репозиторий за один шаг:
```bash
# Dump and reconstruct working repository in one command
git-dumper https://target.com/.git/ ./recovered_repo/

# Run through HTTP/SOCKS proxy
git-dumper --proxy http://127.0.0.1:8080 https://target.com/.git/ ./recovered_repo/
```
После завершения вы можете использовать обычные команды `git` внутри папки:
```bash
cd ./recovered_repo/
git log -p              # Review all commit diffs and historical edits
git status              # Check untracked files
git branch -a           # List all remote and local branches
```
---

## 🔍Поиск секретов и учетных данных после реконструкции

После восстановления репозитория найдите учетные данные и скрытые файлы:
```bash
cd ./recovered_repo/

# Search for database strings and API keys
grep -Erio "password|secret|api_key|token|AWS" .

# Scan the reconstructed repo with Gitleaks / TruffleHog
gitleaks detect -v
trufflehog git file://$(pwd)
```
---

## ⚡ Краткая справочная таблица

| Цель | Команда |
|-----------|---------|
| **Дамп с помощью git-dumper (Python)**| `git-dumper https://target.com/.git/ ./repo/` |
| **Дамп с помощью GitTools Bash** | `./Dumper/gitdumper.sh https://target.com/.git/ ./dump/` |
| **Извлечение коммитов в исходный код** | `./Extractor/extractor.sh ./dump/ ./source/` |
| **Сканировать список хостов на наличие .git** | `python3 Finder/finder.py -i domains.txt` |
| **Просмотр истории коммитов** | `git log -p --color` |
| **Ищите запрограммированные секреты** | `gitleaks detect -v` |

---

## 🛡️ Соображения по защите и усиление защиты веб-сервера

1. **Усиление защиты Nginx:**
   ```nginx
   location ~ /\.git {
       deny all;
       return 404;
   }
   ```
2. **Усиление защиты Apache (`.htaccess`):**
   ```apache
   RedirectMatch 404 /\.git
   ```
3. **Рекомендации по CI/CD:** Никогда не развертывайте код путем клонирования git непосредственно в корень веб-сайта (`/var/www/html/`). Используйте чистые пакеты артефактов или контейнеры Docker.

---

## ⚠️ Юридическая оговорка

> [!ВНИМАНИЕ]
> GitTools и git-dumper предназначены для авторизованной оценки уязвимостей и аудита защитной инфраструктуры. Загрузка проприетарного исходного кода из неавторизованных веб-приложений без разрешения является незаконной.