<!-- language-home-navigation -->
[🏠 Русская главная страница](../../README.ru.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🌩️ Prowler — Полная шпаргалка

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ██████╗ ██████╗  ██████╗ ██╗    ██╗██╗     ███████╗██████╗ 
  ██╔══██╗██╔══██╗██╔═══██╗██║    ██║██║     ██╔════╝██╔══██╗
  ██████╔╝██████╔╝██║   ██║██║ █╗ ██║██║     █████╗  ██████╔╝
  ██╔═══╝ ██╔══██╗██║   ██║██║███╗██║██║     ██╔══╝  ██╔══██╗
  ██║     ██║  ██║╚██████╔╝╚███╔███╔╝███████╗███████╗██║  ██║
  ╚═╝     ╚═╝  ╚═╝ ╚═════╝  ╚══╝╚══╝ ╚══════╝╚══════╝╚═╝  ╚═╝
       Multi-Cloud Security Posture Assessment & CIS Benchmark Auditor
```
<p выравнивание="центр">
  <img src="https://img.shields.io/badge/Prowler-Cloud_Security-blue?style=for-the-badge" alt="Prowler">
  <img src="https://img.shields.io/badge/Author-Toni_de_la_Fuente-orange?style=for-the-badge" alt="toniblex">
  <img src="https://img.shields.io/badge/Clouds-AWS_%7C_Azure_%7C_GCP_%7C_K8s-gold?style=for-the-badge" alt="Multi-Cloud">
  <img src="https://img.shields.io/badge/Compliance-CIS_Benchmark-green?style=for-the-badge" alt="CIS">
</p>

<p выравнивание="центр">
  <b>🌩️ Стандартный инструмент безопасности с открытым исходным кодом для AWS, Azure, Google Cloud (GCP) и Kubernetes, оценка состояния, аудит неправильной конфигурации и проверка соответствия</b>
</p>

---

## 📋 Содержание

- [Обзор и архитектура](#-обзор--архитектура)
- [Установка](#-установка)
- [Аудит безопасности AWS](#-aws-security-auditing)
  - [Аудит учетной записи по умолчанию](#default-account-audit)
  - [Таргетинг на конкретные услуги (S3, IAM, EC2)](#targeting-special-services-s3-iam-ec2)
  – [Таргетинг на определенные регионы AWS](#targeting-special-aws-regions)
  - [Сканирование нескольких учетных записей с помощью организаций AWS](#сканирование с несколькими учетными записями-с-aws-организациями)
- [Аудит Azure и Google Cloud (GCP)](#-azure--google-cloud-gcp-auditing)
  - [Оценка Microsoft Azure](#microsoft-azure-assesment)
  – [Оценка Google Cloud Platform (GCP)](#google-cloud-platform-gcp-assessment)
- [Структуры соответствия (CIS, ISO27001, PCI-DSS)](#-compliance-frameworks-cis-iso27001-pci-dss)
- [Форматы отчетов и вывода (HTML, JSON, CSV)](#-reporting--output-formats-html-json-csv)
- [Краткая справочная таблица](#-краткая справочная-таблица)
- [Защитные соображения и исправление](#-защитные-соображения--восстановление)
- [Юридическая оговорка](#-юридическая оговорка)

---

## 🎯 Обзор и архитектура

**Prowler**, изначально созданный **Тони де ла Фуэнте (@tonilyx)**, представляет собой важный инструмент оценки безопасности мультиоблачной среды. Он выполняет автоматизированную оценку инфраструктуры как услуги (IaaS) и платформенных сервисов для обнаружения:
- Доступные для публичного чтения и записи сегменты S3 и учетные записи хранения.
- В корневых учетных записях отсутствует многофакторная аутентификация (MFA).
— Политики IAM с чрезмерными привилегиями и разрешениями `*` с подстановочными знаками.
- Незашифрованные базы данных, диски и резервные копии.
— Открытые группы безопасности, предоставляющие порты управления (SSH 22, RDP 3389) для `0.0.0.0/0`.
- Несоответствие стандартам СНГ, ISO 27001, HIPAA, PCI-DSS и GDPR.

---

## 📥 Установка
```bash
# Using pip / pipx (Recommended)
pipx install prowler
# Or
pip install prowler

# Using Docker
docker pull prowlercloud/prowler:latest

# Verify installation
prowler -v
```
---

## ☁️ Аудит безопасности AWS

Убедитесь, что в вашей среде настроены действительные учетные данные AWS (`aws configure`, переменные среды или профиль экземпляра IAM):

### Аудит учетной записи по умолчанию
Запускает все встроенные проверки во всех включенных службах и регионах:
```bash
prowler aws
```
### Ориентация на конкретные услуги (S3, IAM, EC2)
```bash
# Run only S3 bucket checks
prowler aws --services s3

# Run IAM and EC2 checks together
prowler aws --services iam,ec2
```
### Ориентация на определенные регионы AWS
Избегайте сканирования пустых неиспользуемых областей, чтобы значительно увеличить скорость сканирования:
```bash
prowler aws --region us-east-1,eu-west-1
```
### Сканирование нескольких аккаунтов с помощью организаций AWS
Проведите аудит всех учетных записей в организации AWS, взяв на себя роль централизованного управления:
```bash
prowler aws --sp-role-arn arn:aws:iam::111122223333:role/SecurityAuditRole
```
---

## 🔷 Аудит Azure и Google Cloud (GCP)

### Оценка Microsoft Azure
Аутентификация через Azure CLI (`az login`):
```bash
# Audit entire Azure subscription
prowler azure --subscription-ids "00000000-0000-0000-0000-000000000000"

# Target specific services (Storage, Entra ID / IAM)
prowler azure --services storage,iam
```
### Оценка Google Cloud Platform (GCP)
Аутентификация через Google Cloud SDK (`gcloud auth application-default login`):
```bash
# Audit specific GCP Project
prowler gcp --project-ids my-production-project

# Target Google Cloud Storage
prowler gcp --services storage
```
---

## 📜 Рамки соответствия (CIS, ISO27001, PCI-DSS)

Prowler классифицирует проверки по официальным стандартам соответствия:
```bash
# Run CIS AWS Foundations Benchmark v3.0
prowler aws --compliance cis_3.0_aws

# Run PCI-DSS Compliance Audit
prowler aws --compliance pci_3.2.1_aws

# Run ISO 27001 Evaluation
prowler aws --compliance iso27001_2013_aws

# List all supported compliance frameworks
prowler aws --list-compliance
```
---

## 📊 Форматы отчетов и вывода (HTML, JSON, CSV)

По умолчанию Prowler выводит отчеты в каталог `output/`:
– **Интерактивный HTML:** Визуальная панель мониторинга, показывающая неудачные, пройденные и отключенные проверки.
- **CSV:** Легко импортировать в электронные таблицы или системы продажи билетов.
- **JSON-ASFF / JSON-OCSF:** AWS Security Hub и телеметрия, совместимая с SIEM.
```bash
# Generate HTML and JSON reports in a custom folder
prowler aws -M html,json,csv -o /path/to/audit-reports/
```
---

## ⚡ Краткая справочная таблица

| Цель | Команда |
|-----------|---------|
| **Полный аудит AWS** | `prowler aws` |
| **Специальная услуга аудита** | `prowler aws --services s3,iam` |
| **Аудит отдельного региона** | `prowler aws --region us-east-1` |
| **Эталонный аудит стран СНГ** | `prowler aws --compliance cis_3.0_aws` |
| **Полный аудит Azure** | `prowler azure --subscription-ids <SUB_ID>` |
| **Полный аудит GCP** | `prowler gcp --project-ids <PROJECT_ID>` |
| **Экспортировать HTML/CSV/JSON** | `prowler aws -M html,csv,json -o ./reports` |
| **Список доступных чеков**| `prowler aws --list-checks` |

---

## 🛡️ Соображения по поводу защиты и исправления

1. **Автоматическое исправление.** Ознакомьтесь с рекомендациями Prowler по выходным данным, в которых указаны точные команды AWS CLI или параметры Terraform, необходимые для исправления каждой неправильной конфигурации.
2. **Интеграция с AWS Security Hub.** Передача результатов Prowler в формате JSON-ASFF непосредственно в AWS Security Hub для централизованного оповещения об уязвимостях для нескольких аккаунтов.

---

## ⚠️ Юридическая оговорка

> [!ВНИМАНИЕ]
> Prowler — это инструмент аудита и оценки состояния. Прежде чем выполнять сканирование на соответствие требованиям в производственных средах, убедитесь, что у вас есть необходимые разрешения от арендатора облака и владельцев подписки.