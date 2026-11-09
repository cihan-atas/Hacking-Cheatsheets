<!-- language-home-navigation -->
[🏠 الصفحة الرئيسية العربية](../../README.ar.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🔑 Gitleaks - ورقة الغش الكاملة

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
<p align="center">
  <img src="https://img.shields.io/badge/Gitleaks-Secret_Scanner-blue?style=for-the-badge" alt="Gitleaks">
  <img src="https://img.shields.io/badge/Author-Zachary_Rice-orange?style=for-the-badge" alt="zricethezav">
  <img src="https://img.shields.io/badge/Category-Source_Code_&_Secrets-green?style=for-the-badge" alt="الأسرار">
  <img src="https://img.shields.io/badge/Language-Go-cyan?style=for-the-badge" alt="Go">
</ص>

<p align="center">
  <b>🔑 أداة SAST مصممة للعثور على الأسرار المشفرة واكتشافها مثل كلمات المرور ومفاتيح API والرموز المميزة في مستودعات Git وسجلات الالتزام</b>
</ص>

---

## 📋 جدول المحتويات

- [محرك النظرة العامة والكشف](#-نظرة عامة--محرك-الكشف)
- [التثبيت](#-التثبيت)
- [المسح السري الأساسي](#-المسح السري الأساسي)
  - [مسح مستودع Git](#scanning-a-git-repository)
  - [فحص التغييرات غير الملتزم بها](#scanning-uncommitted-changes)
  - [مسح الملفات والمجلدات العادية](#scanning-plain-files--folders)
- [نطاقات الالتزام والتاريخ](#-استهداف-نطاقات الالتزام--التاريخ)
- [CI/CD والتكامل المسبق](#-cicd--التكامل المسبق)
- [قواعد الكشف المخصصة والقائمة المسموح بها](#-قواعد-الاكتشاف المخصص--القائمة المسموح بها)
- [تنسيقات الإخراج وإعداد التقارير](#-تنسيقات الإخراج--التقارير)
- [الجدول المرجعي السريع](#-الجدول المرجعي السريع)
- [الاعتبارات الدفاعية والمعالجة](#-الاعتبارات الدفاعية--العلاج)
- [إخلاء المسؤولية القانونية](#-إخلاء المسؤولية القانونية)

---

## 🎯 محرك النظرة العامة والكشف

تُعد الأسرار المشفرة (مفاتيح وصول AWS، ومفاتيح SSH الخاصة، ورموز GitHub المميزة، وخطافات ويب Slack، وبيانات اعتماد قاعدة البيانات) الملتزمة عن غير قصد بمستودعات git واحدة من أكثر المتجهات شيوعًا للتسوية السحابية الأولية.

تم إنشاء **Gitleaks** بواسطة **Zachary Rice (@zricethezav)**، ويوفر تحليلًا سريعًا للغاية يعتمد على التعبيرات المنطقية وإنتروبيا شانون عبر:
- **سجل Git الكامل:** يقوم بتدقيق كل فرع وعلامة والالتزام التاريخي.
- **خطافات الالتزام المسبق:** تمنع المهندسين من تنفيذ بيانات الاعتماد عن طريق الخطأ قبل وصول التعليمات البرمجية إلى الخوادم البعيدة.
- **خطوط أنابيب CI/CD:** تفشل تلقائيًا طلبات السحب التي تحتوي على تسريبات لبيانات الاعتماد.

---

## 📥 التثبيت
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

## 🚀 المسح السري الأساسي

### فحص مستودع Git
تدقيق سجل الالتزام للمستودع الحالي:
```bash
# Run detection in current working repo
gitleaks detect -v

# Audit an external local repo path
gitleaks detect --source /path/to/repo -v
```
### فحص التغييرات غير الملتزم بها
عمليات التدقيق المرحلية وتغييرات التعليمات البرمجية غير المرحلية قبل الالتزام:
```bash
# Inspect unstaged working directory changes
gitleaks protect -v

# Scan staged changes only
gitleaks protect --staged -v
```
### فحص الملفات والمجلدات العادية
فحص الدلائل غير التابعة لـ git (على سبيل المثال، البرامج الثابتة المستخرجة، وتطبيقات الهاتف المحمول التي تم فك ترجمتها، ومجلدات التكوين):
```bash
gitleaks dir /path/to/directory -v
```
---

## ⚡ استهداف نطاقات الالتزام والتاريخ
```bash
# Scan commits made in the last 7 days
gitleaks detect --log-opts="--since='7 days ago'" -v

# Scan specific branch or tag comparison
gitleaks detect --log-opts="origin/main..feature-branch" -v

# Target the latest 5 commits
gitleaks detect --log-opts="-n 5" -v
```
---

## ⚙️ CI/CD وتكامل ما قبل الالتزام

### تكوين إطار الالتزام المسبق (`.pre-commit-config.yaml`)
```yaml
repos:
  - repo: https://github.com/gitleaks/gitleaks
    rev: v8.18.2
    hooks:
      - id: gitleaks
```
### سير عمل إجراءات GitHub
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

## 🧩 قواعد الكشف المخصصة والقائمة المسموح بها

قم بتكوين أنماط regex المخصصة أو تجاهل الإيجابيات الخاطئة باستخدام `.gitleaks.toml`:
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
تشغيل مع التكوين المخصص:
```bash
gitleaks detect -c .gitleaks.toml -v
```
---

## 📊 تنسيقات الإخراج وإعداد التقارير
```bash
# Export results to clean JSON format
gitleaks detect --report-path gitleaks-report.json --report-format json

# Export to SARIF (for GitHub Security tab integration)
gitleaks detect --report-path report.sarif --report-format sarif

# Export to CSV
gitleaks detect --report-path report.csv --report-format csv
```
---

## ⚡ جدول مرجعي سريع

| الهدف | الأمر |
|-----------|--------|
| ** مسح سجل Git بالكامل ** | `gitleaks detect -v` |
| **مسح التغييرات غير الملتزم بها** | `gitleaks protect -v` |
| **مسح التغييرات المرحلية فقط** | `gitleaks protect --staged -v` |
| ** مسح الدليل العادي (بدون بوابة) **| `gitleaks dir /path/to/folder -v` |
| **تصدير إلى JSON** | `gitleaks detect -r report.json -f json` |
| **التصدير إلى الصريف** | `gitleaks detect -r report.sarif -f sarif` |
| ** استخدم تكوين القواعد المخصصة ** | `gitleaks detect -c .gitleaks.toml` |
| **مسح فرع معين** | `gitleaks detect --log-opts="main..dev"` |

---

## 🛡️ الاعتبارات الدفاعية والعلاج

1. **الإلغاء الفوري:** بمجرد اكتشاف سر في سجل git، اعتبره مخترقًا بالكامل وقم بتدويره/إبطاله على الفور.
2. **إعادة كتابة سجل Git:** يلزم إزالة الالتزام أو استخدام `git filter-repo` / BFG Repo-Cleaner لتنقية العناصر، ولكن لا تستبدل أبدًا عمليات إعادة كتابة السجل لتدوير بيانات الاعتماد.

---

## ⚠️إخلاء المسؤولية القانونية

> [!تنبيه]
> تم تصميم Gitleaks لعمليات تدقيق الأمان الداخلي المعتمدة، والتحقق المستمر من ثغرات التكامل، ومراجعة التعليمات البرمجية الدفاعية. يُحظر تمامًا فحص مستودعات الملكية غير المصرح بها دون إذن.