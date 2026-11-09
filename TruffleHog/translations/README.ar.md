<!-- language-home-navigation -->
[🏠 الصفحة الرئيسية العربية](../../README.ar.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🐽 TruffleHog - ورقة الغش الكاملة

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
<p align="center">
  <img src="https://img.shields.io/badge/TruffleHog-Secret_Scanner-blue?style=for-the-badge" alt="TruffleHog">
  <img src="https://img.shields.io/badge/Developer-Truffle_Security-orange?style=for-the-badge" alt="Truffle Security">
  <img src="https://img.shields.io/badge/Category-OSINT_&_Code_Security-green?style=for-the-badge" alt="Code Security">
  <img src="https://img.shields.io/badge/License-AGPLv3-red?style=for-the-badge" alt="License AGPLv3">
</ص>

<p align="center">
  <b>🌐 ماسح ضوئي سري عالي الدقة مصمم لتعقب مفاتيح واجهة برمجة التطبيقات والرموز المميزة والشهادات الخاصة المسربة عبر Git وS3 وDocker وأنظمة الملفات من خلال التحقق المباشر</b>
</ص>

---

## 📋 جدول المحتويات

- [نظرة عامة والقوة العظمى للتحقق المباشر](#-نظرة عامة--القوة العظمى للتحقق المباشر)
- [مقارنة بين TruffleHog وGitleaks](#-مقارنة بين trufflehog وGitleaks)
- [التثبيت](#-التثبيت)
- [مسح مستودعات Git](#-scanning-git-repositories)
- [فحص أنظمة الملفات والدلائل المحلية](#-scanning-local-filsystems--directories)
- [فحص مؤسسات GitHub بالكامل](#-scanning-entire-github-organizations)
- [مسح جرافات S3 وصور عامل الإرساء](#-scanning-s3-buckets--docker-images)
- [تصفية الإيجابيات الكاذبة (`--only-verified`)](#-تصفية الإيجابيات الكاذبة --- تم التحقق منها فقط)
- [إخراج JSON وتكامل CI/CD](#-json-output--cicd-integration)
- [الجدول المرجعي السريع](#-الجدول المرجعي السريع)
- [الاعتبارات الدفاعية وOPSEC](#-الاعتبارات الدفاعية--opsec)
- [إخلاء المسؤولية القانونية](#-إخلاء المسؤولية القانونية)

---

## 🎯 نظرة عامة والقوة العظمى للتحقق المباشر

كثيرًا ما يلتزم المطورون ببيانات اعتماد ذات امتيازات عالية في مستودعات Git (مفاتيح وصول AWS، ومفاتيح Stripe الخاصة، وخطافات الويب Slack، ومفاتيح SSH الخاصة، وكلمات مرور قاعدة البيانات). حتى عند حذفها في عمليات الالتزام اللاحقة، **تظل هذه الأسرار مدفونة بشكل دائم في سجل التزامات المستودع**.

كتبه **Truffle Security** في Go، تُلزم عمليات فحص **TruffleHog** بالتواريخ وأنظمة الملفات والتخزين السحابي للكشف عن الأسرار المسربة.

القوة العظمى المميزة لها هي **التحقق المباشر**:
على عكس أدوات فحص regex التقليدية التي تغمر فرق الأمان بآلاف من النتائج الإيجابية الزائفة (على سبيل المثال، أمثلة لمفاتيح التوثيق)، يتصل TruffleHog بموفر السحابة المعني (AWS، وGitHub، وSlack، وOpenAI) **للتحقق مما إذا كان السر المستخرج يعمل بشكل نشط**.

---

## ⚖️ مقارنة بين TruffleHog وGitleaks

| ميزة | الكمأة | جيتليكس |
|---------|----------|----------|
| ** التحقق المباشر من بيانات الاعتماد ** | ✅ نعم (تم اختبار أكثر من 750 جهاز كشف على الهواء مباشرة)| ❌ لا (التعبير العادي فقط) |
| **مصادر البيانات المستهدفة** | جيت، جيثب، S3، دوكر، نظام الملفات | جيت، الملفات المحلية |
| **السرعة في عمليات إعادة الشراء الضخمة** | سريع (انتقل إلى مؤشرات ترابط متعددة) | سريع للغاية (الحد الأدنى من التعبير العادي) |
| **معدل إيجابي كاذب** | بالقرب من الصفر (مع `--only-verified`) | معتدل (يتطلب قوائم مسموح بها)|
| **الارتباط المسبق بالخطاف** | ✅ مدعوم | ✅ الأمثل للغاية |

---

## 📥 التثبيت
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

## 🚀 فحص مستودعات Git
```bash
# Scan a remote Git repository (including entire commit history)
trufflehog git https://github.com/targetcorp/public-project

# Scan an internal on-premise Git repository with authentication
trufflehog git https://username:token@gitlab.corp.local/repo.git

# Scan only the most recent commit (great for pre-commit hooks)
trufflehog git file://./ --since-commit HEAD~1
```
---

## 📂 فحص أنظمة الملفات والأدلة المحلية

قم بمسح الدلائل المحلية أو أرشيفات النسخ الاحتياطي المستخرجة أو جذور الويب التي تم إلغاء فهرستها:
```bash
# Scan current directory recursively
trufflehog filesystem .

# Scan specific sensitive configuration directory
trufflehog filesystem /var/www/html/ --exclude-paths tests/,vendor/
```
---

## 🏢 فحص مؤسسات GitHub بالكامل

تدقيق جميع المستودعات العامة التابعة للمؤسسة:
```bash
# Scan all public repos of a GitHub organization
trufflehog github --org=targetorg --token=YOUR_GITHUB_TOKEN

# Scan all private and public repositories across a personal account
trufflehog github --user=targetuser --token=YOUR_GITHUB_TOKEN
```
---

## ☁️ مسح دلاء S3 وصور Docker
```bash
# Scan an AWS S3 bucket for exposed credentials in stored logs and backups
trufflehog s3 --bucket=target-backup-bucket

# Scan a Docker container image layers for baked-in secrets
trufflehog docker --image=targetcorp/api-server:latest
```
---

## 🎯 تصفية الإيجابيات الكاذبة (`--only-verified`)

منع جميع نتائج التعبير العادي التي لم يتم التحقق منها وعرض **بيانات الاعتماد النشطة المؤكدة فقط**:
```bash
# Report ONLY confirmed functional credentials verified against providers
trufflehog git https://github.com/target/repo --only-verified

# Scan filesystem for verified secrets only
trufflehog filesystem /opt/backups/ --only-verified
```
---

## 📊 إخراج JSON وتكامل CI/CD
```bash
# Export findings to structured JSON format
trufflehog git https://github.com/target/repo --json > secrets_report.json

# Parse live AWS keys with jq
cat secrets_report.json | jq -r 'select(.Verified == true) | .Raw'
```
---

## ⚡ جدول مرجعي سريع

| الهدف | الأمر |
|-----------|--------|
| ** المسح عن بعد لـ Git Repo ** | `trufflehog git <GIT_URL>` |
| ** مسح الأسرار التي تم التحقق منها فقط ** | `trufflehog git <GIT_URL> --only-verified` |
| **فحص نظام الملفات المحلي** | `trufflehog filesystem <DIRECTORY>` |
| ** مسح منظمة جيثب ** | `trufflehog github --org=<ORG> --token=<PAT>` |
| ** مسح صورة عامل الميناء ** | `trufflehog docker --image=<IMAGE>` |
| ** مسح دلو S3 ** | `trufflehog s3 --bucket=<BUCKET_NAME>` |
| **تصدير إلى JSON** | `trufflehog git <GIT_URL> --json` |

---

## 🛡️ الاعتبارات الدفاعية وOPSEC

1. **حركة مرور التحقق من واجهة برمجة التطبيقات الصادرة:** عند التشغيل بدون `--no-verification`، يتصل TruffleHog بنقاط النهاية التابعة لجهات خارجية (Slack وAWS وStripe) لاختبار المفاتيح. يؤدي هذا إلى تشغيل سجلات المصادقة في المستأجر السحابي للضحية! إذا كان التخفي أمرًا بالغ الأهمية، فقم بتعطيل التحقق المباشر:
   ```bash
   trufflehog git <URL> --no-verification
   ```
2. ** إبطال المفتاح والاستجابة للحوادث: ** عند اكتشاف أسرار صالحة في المستودعات العامة، قم بتدوير بيانات الاعتماد على الفور وتحقق من سجلات CloudTrail / التدقيق بحثًا عن وصول غير مصرح به.

---

## ⚠️إخلاء المسؤولية القانونية

> [!تنبيه]
> تم تصميم ورقة الغش هذه لعمليات التدقيق الأمني المعتمدة وعمليات الفريق الأحمر وتقييمات سلامة بيانات الاعتماد. يعد استخدام بيانات الاعتماد المكتشفة للوصول إلى الحسابات غير المصرح بها أو البيئات السحابية الخاصة أمرًا غير قانوني تمامًا.