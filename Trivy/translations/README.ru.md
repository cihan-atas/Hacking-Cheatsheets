<!-- language-home-navigation -->
[🏠 Русская главная страница](../../README.ru.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# Простая шпаргалка

[Trivy](https://github.com/aquasecurity/trivy) от Aqua Security — это комплексный многоцелевой сканер безопасности для контейнеров, файловых систем, репозиториев Git, образов виртуальных машин, кластеров Kubernetes и конфигураций инфраструктуры как кода (IaC). Он обнаруживает уязвимости ОС и языка (CVE), утечку секретов, неправильные конфигурации и генерирует спецификацию программного обеспечения (SBOM).

---

## Содержание

- [Основные возможности](#core-capabilities)
- [Установка](#установка)
- [Сканирование образа контейнера (`trivy image`)](#container-image-scanning-trivy-image)
- [Сканирование файловой системы и репозитория (`trivy fs / repo`)](#filesystem--repository-scanning-trivy-fs--repo)
- [Обнаружение секретов и учетных данных (`--scanners secret`)](#secret--credential-detection---scanners-secret)
- [Аудит кластера Kubernetes (`trivy k8s`)](#kubernetes-cluster-auditing-trivy-k8s)
- [Сканирование неправильной конфигурации инфраструктуры как кода (IaC)](#infrastructure-as-code-iac-misconfiguration-scanning)
- [Интеграция CI/CD и коды выхода](#cicd-integration--exit-codes)
- [Генерация SBOM (CycloneDX и SPDX)](#sbom-генерация-циклонедкс--spdx)

---

## Основные возможности

- **Уязвимости:** обнаруживает CVE в Alpine, Debian, RHEL, Ubuntu, Amazon Linux и языковых пакетах (Go, Python, Node.js, Java, Rust, Ruby, PHP, .NET).
- **Неправильные конфигурации**: проверяет файлы Dockerfile, манифесты Kubernetes, диаграммы Terraform, CloudFormation и Helm на соответствие CIS Benchmarks.
- **Секретное сканирование.** Помечает открытые ключи API, частные сертификаты, токены JWT и пароли.
- **Сканирование кластера:** проверяет рабочие нагрузки Kubernetes и плоскости управления в реальном времени.
- **Быстро и легко:** Локальная база данных SQLite обновляется прозрачно, без необходимости использования внешнего демона.

---

## Установка
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

## Сканирование образа контейнера (`trivy image`)

### 1. Базовое сканирование изображения
Сканируйте локальный или удаленный образ Docker:
```bash
trivy image nginx:latest
```
### 2. Фильтровать по уровню серьезности
Сосредоточьтесь только на критических и высоких уязвимостях, требующих принятия мер:
```bash
trivy image --severity HIGH,CRITICAL python:3.9-slim
```
### 3. Отображать только исправимые уязвимости
Отфильтруйте неисправленные нулевые или неустранимые проблемы исходного кода:
```bash
trivy image --ignore-unfixed --severity HIGH,CRITICAL node:18-alpine
```
### 4. Сканирование сохраненного архива Tar
Сканируйте автономные образы контейнеров, экспортированные через `docker save`:
```bash
docker save myapp:v1 -o myapp.tar
trivy image --input myapp.tar
```
---

## Сканирование файловой системы и репозитория (`trivy fs / repo`)

Сканировать деревья исходного кода, файлы блокировки зависимостей (`package-lock.json`, `poetry.lock`, `go.sum`, `pom.xml`):

### 1. Сканировать локальный каталог/проект
```bash
trivy fs /path/to/project
```
### 2. Сканировать удаленный репозиторий Git напрямую
```bash
trivy repo https://github.com/example/vulnerable-app
```
---

## Обнаружение секретов и учетных данных (`--scanners secret`)

Обнаруживайте жестко запрограммированные ключи API, токены и закрытые ключи, встроенные в уровни кода или контейнера:
```bash
# Scan directory for secrets
trivy fs --scanners secret .

# Scan container image for secrets and vulnerabilities simultaneously
trivy image --scanners vuln,secret mycompany/api:latest
```
---

## Аудит кластера Kubernetes (`trivy k8s`)

Аудит активных рабочих нагрузок и конфигурации в работающем кластере Kubernetes с использованием активного контекста `kubectl`:

### 1. Аудит всей рабочей нагрузки кластера
```bash
trivy k8s --report summary cluster
```
### 2. Аудит определенного пространства имен
```bash
trivy k8s --namespace production --severity HIGH,CRITICAL all
```
### 3. Соответствие критериям CIS Kubernetes Benchmark
```bash
trivy k8s --compliance k8s-cis cluster
```
---

## Сканирование неправильной конфигурации инфраструктуры как кода (IaC)

Обнаруживайте рискованные конфигурации в Dockerfiles, Terraform и Kubernetes YAML:
```bash
# Scan Terraform files for cloud security violations
trivy config ./terraform/

# Scan Dockerfile for root user or insecure exposed ports
trivy config Dockerfile
```
---

## Интеграция CI/CD и коды выхода

Автоматизируйте шлюзы конвейера, чтобы заблокировать доставку уязвимых сборок:
```bash
# Break build (Exit code 1) if CRITICAL vulnerabilities exist
trivy image --exit-code 1 --severity CRITICAL myapp:build-42

# Generate SARIF report for GitHub Code Scanning
trivy image --format sarif --output trivy-results.sarif myapp:latest
```
---

## Генерация SBOM (CycloneDX и SPDX)

Создайте спецификацию программного обеспечения (SBOM) для проверки соответствия требованиям и цепочки поставок:
```bash
# Generate CycloneDX JSON SBOM
trivy image --format cyclonedx --output sbom.json myapp:latest

# Generate SPDX JSON SBOM
trivy image --format spdx-json --output sbom-spdx.json myapp:latest
```