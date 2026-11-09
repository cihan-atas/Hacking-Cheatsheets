<!-- language-home-navigation -->
[🏠 الصفحة الرئيسية العربية](../../README.ar.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ورقة الغش Trivy

[Trivy](https://github.com/aquasecurity/trivy) من Aqua Security هو ماسح أمني شامل ومتعدد الأغراض للحاويات وأنظمة الملفات ومستودعات Git وصور الأجهزة الافتراضية ومجموعات Kubernetes وتكوينات البنية التحتية كرمز (IaC). فهو يكتشف نقاط الضعف في نظام التشغيل واللغة (CVEs)، والأسرار المسربة، والتكوينات الخاطئة، وينشئ قائمة مواد البرامج (SBOM).

---

## جدول المحتويات

- [القدرات الأساسية](#القدرات الأساسية)
- [التثبيت](#التثبيت)
- [مسح صورة الحاوية (`trivy image`)](#container-image-scanning-trivy-image)
- [نظام الملفات وفحص المستودعات (`trivy fs / repo`)](#filesystem--repository-scanning-trivy-fs--repo)
- [اكتشاف السر وبيانات الاعتماد (`--scanners secret`)](#secret--كشف بيانات الاعتماد---الماسحات الضوئية-سر)
- [تدقيق مجموعة Kubernetes (`trivy k8s`)](#kubernetes-cluster-auditing-trivy-k8s)
- [فحص التكوين الخاطئ للبنية التحتية كرمز (IaC)](#infrastructure-as-code-iac-misconfiguration-scanning)
- [أكواد الخروج وتكامل CI/CD](#cicd-integration--exit-codes)
- [جيل SBOM (CycloneDX وSPDX)](#sbom-Generation-cyclonedx--spdx)

---

## القدرات الأساسية

- **نقاط الضعف:** تكتشف التهديدات الشائعة عبر Alpine وDebian وRHEL وUbuntu وAmazon Linux وحزم اللغات (Go وPython وNode.js وJava وRust وRuby وPHP و.NET).
- **التكوينات الخاطئة:** تدقيق ملفات Dockerfiles، وبيانات Kubernetes، وTerraform، وCloudFormation، ومخططات Helm مقابل معايير CIS.
- **الفحص السري:** يقوم بوضع علامات على مفاتيح API المكشوفة، والشهادات الخاصة، ورموز JWT، وكلمات المرور.
- **المسح العنقودي:** يقوم بتدقيق أعباء عمل Kubernetes ومستويات التحكم بشكل مباشر.
- **سريع وخفيف الوزن:** يتم تحديث قاعدة بيانات SQLite المحلية بشفافية دون الحاجة إلى متطلبات خارجية.

---

## تثبيت
```bash
# Debian / Ubuntu
sudo apt-get install wget apt-transport-https gnupg
wget -qO - https://aquasecurity.github.io/trivy-repo/deb/public.key | gpg --dearmor | sudo tee /usr/share/keyrings/trivy.gpg > /dev/null
echo "deb [signed-by=/usr/share/keyrings/trivy.gpg] https://aquasecurity.github.io/trivy-repo/deb generic main" | sudo tee -a /etc/apt/sources.list.d/trivy.list
sudo apt-get update && sudo apt-get install -y trivy

# Arch Linux
sudo pacman -S trivy

# Homebrew (macOS / Linux)
brew install trivy

# Docker container
docker run --rm -v /var/run/docker.sock:/var/run/docker.sock aquasec/trivy image alpine:latest
```
---

## مسح صورة الحاوية (`trivy image`)

### 1. مسح الصور الأساسي
مسح صورة Docker محلية أو بعيدة:
```bash
trivy image nginx:latest
```
### 2. التصفية حسب مستوى الخطورة
التركيز على نقاط الضعف الحرجة والعالية القابلة للتنفيذ فقط:
```bash
trivy image --severity HIGH,CRITICAL python:3.9-slim
```
### 3. عرض الثغرات الأمنية القابلة للإصلاح فقط
قم بتصفية الأيام الصفرية غير المصححة أو مشكلات المنبع غير القابلة للإصلاح:
```bash
trivy image --ignore-unfixed --severity HIGH,CRITICAL node:18-alpine
```
### 4. مسح أرشيف القطران المخزن
مسح صور الحاويات غير المتصلة بالإنترنت المصدرة عبر `docker save`:
```bash
docker save myapp:v1 -o myapp.tar
trivy image --input myapp.tar
```
---

## فحص نظام الملفات والمستودعات (`trivy fs / repo`)

مسح أشجار التعليمات البرمجية المصدر وملفات قفل التبعية (`package-lock.json`، `poetry.lock`، `go.sum`، `pom.xml`):

### 1. مسح الدليل المحلي / المشروع
```bash
trivy fs /path/to/project
```
### 2. مسح مستودع Git عن بعد مباشرة
```bash
trivy repo https://github.com/example/vulnerable-app
```
---

## كشف السر وبيانات الاعتماد (`--scanners secret`)

اكتشف مفاتيح API المشفرة والرموز المميزة والمفاتيح الخاصة المضمنة في طبقات التعليمات البرمجية أو الحاوية:
```bash
# Scan directory for secrets
trivy fs --scanners secret .

# Scan container image for secrets and vulnerabilities simultaneously
trivy image --scanners vuln,secret mycompany/api:latest
```
---

## تدقيق مجموعة Kubernetes (`trivy k8s`)

قم بمراجعة أعباء العمل النشطة والتكوين في مجموعة Kubernetes المباشرة باستخدام سياق `kubectl` النشط الخاص بك:

### 1. تدقيق أعباء عمل المجموعة بالكامل
```bash
trivy k8s --report summary cluster
```
### 2. قم بمراجعة مساحة اسم محددة
```bash
trivy k8s --namespace production --severity HIGH,CRITICAL all
```
### 3. الامتثال لمعايير CIS Kubernetes
```bash
trivy k8s --compliance k8s-cis cluster
```
---

## فحص التكوين الخاطئ للبنية التحتية كرمز (IaC).

اكتشف التكوينات الخطرة في Dockerfiles وTerraform وKubernetes YAML:
```bash
# Scan Terraform files for cloud security violations
trivy config ./terraform/

# Scan Dockerfile for root user or insecure exposed ports
trivy config Dockerfile
```
---

## تكامل CI/CD وأكواد الخروج

أتمتة بوابات خطوط الأنابيب لمنع عمليات البناء الضعيفة من الشحن:
```bash
# Break build (Exit code 1) if CRITICAL vulnerabilities exist
trivy image --exit-code 1 --severity CRITICAL myapp:build-42

# Generate SARIF report for GitHub Code Scanning
trivy image --format sarif --output trivy-results.sarif myapp:latest
```
---

## إنشاء SBOM (CycloneDX وSPDX)

إنشاء قائمة مواد البرامج (SBOM) للامتثال ومراجعة سلسلة التوريد:
```bash
# Generate CycloneDX JSON SBOM
trivy image --format cyclonedx --output sbom.json myapp:latest

# Generate SPDX JSON SBOM
trivy image --format spdx-json --output sbom-spdx.json myapp:latest
```