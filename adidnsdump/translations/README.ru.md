<!-- language-home-navigation -->
[🏠 Русская главная страница](../../README.ru.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🌲 adidnsdump — Полная шпаргалка

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
   █████╗ ██████╗ ██╗██████╗ ███╗   ██╗███████╗██████╗ ██╗   ██╗███╗   ███╗██████╗ 
  ██╔══██╗██╔══██╗██║██╔══██╗████╗  ██║██╔════╝██╔══██╗██║   ██║████╗ ████║██╔══██╗
  ███████║██║  ██║██║██║  ██║██╔██╗ ██║███████╗██║  ██║██║   ██║██╔████╔██║██████╔╝
  ██╔══██║██║  ██║██║██║  ██║██║╚██╗██║╚════██║██║  ██║██║   ██║██║╚██╔╝██║██╔═══╝ 
  ██║  ██║██████╔╝██║██████╔╝██║ ╚████║███████║██████╔╝╚██████╔╝██║ ╚═╝ ██║██║     
  ╚═╝  ╚═╝╚═════╝ ╚═╝╚═════╝ ╚═╝  ╚═══╝╚══════╝╚═════╝  ╚═════╝ ╚═╝     ╚═╝╚═╝     
           Active Directory Integrated DNS (ADIDNS) Reconnaissance Tool
```
<p выравнивание="центр">
  <img src="https://img.shields.io/badge/Tool-adidnsdump-blue?style=for-the-badge" alt="adidnsdump">
  <img src="https://img.shields.io/badge/Author-Dirk--jan_Mollema-orange?style=for-the-badge" alt="dirkjanm">
  <img src="https://img.shields.io/badge/Category-Active_Directory-green?style=for-the-badge" alt="Active Directory">
  <img src="https://img.shields.io/badge/Protocol-LDAP_%2F_LDAPS-purple?style=for-the-badge" alt="LDAP">
</p>

<p выравнивание="центр">
  <b>🌲 Перечислять и выгружать зоны и записи интегрированного DNS Active Directory (ADIDNS) через LDAP без передачи зон DNS (AXFR)</b>
</p>

---

## 📋 Содержание

- [Обзор и техническая информация](#-overview--technical-background)
- [Установка](#-установка)
- [Методы аутентификации](#-методы-аутентификации)
  - [Аутентификация по паролю](#пароль-аутентификация)
  - [Передать хеш (NTLM)](#pass-the-hash-ntlm)
  - [Билет Kerberos (ccache)](#kerberos-ticket-ccache)
- [Команды дампа ядра](#-core-dumping-commands)
  - [Стандартный дамп зоны DNS Active Directory](#standard-active-directory-dns-zone-dump)
  - [Сброс надгробных/удалённых записей](#dumping-tombstoned--deleted-records)
  - [Сброс лесных DNS и доменных DNS-зон](#dumping-forest-dns--domain-dns-zones)
  - [Нацеливание на определенные зоны DNS](#targeting-special-dns-zones)
- [Форматы экспорта и постобработка](#-export-formats--постобработка)
  - [Вывод CSV](#csv-вывод)
  - [Интеграция с BloodHound и Nmap](#интеграция-с-bloodhound--nmap)
- [Краткая справочная таблица](#-краткая справочная-таблица)
- [Защитные соображения и обнаружение](#-защитные-соображения--обнаружение)
- [Юридическая оговорка](#-юридическая оговорка)

---

## 🎯 Обзор и техническая информация

В стандартных средах Windows Active Directory DNS обычно интегрирован в Active Directory (**DNS, интегрированный в Active Directory/ADIDNS**). В этих конфигурациях записи DNS хранятся не в простых текстовых файлах, а в виде объектов LDAP внутри раздела домена (например, `DC=DomainDnsZones` и `DC=ForestDnsZones`).

Традиционная передача зоны DNS (`AXFR`) почти повсеместно отключена на контроллерах домена. Однако **любой прошедший проверку подлинности пользователь домена** (даже учетные записи с низким уровнем привилегий или скомпрометированные учетные записи служб) имеет разрешения на чтение узлов LDAP, хранящих записи ADIDNS по умолчанию.

Разработанный **Dirk-jan Mollema (@dirkjanm)**, **`adidnsdump`** подключается к контроллеру домена через LDAP или LDAPS, запрашивает разделы ADIDNS и извлекает все записи (A, AAAA, CNAME, SRV, TXT, MX и записи-захоронения), создавая точную инвентаризацию каждого компьютера, рабочей станции и внутренней веб-службы в домене.

---

## 📥 Установка
```bash
# Clone the repository and install with pip
git clone https://github.com/dirkjanm/adidnsdump.git
cd adidnsdump
pip install .

# Or install directly via pipx / pip
pipx install git+https://github.com/dirkjanm/adidnsdump.git
```
---

## 🔑 Методы аутентификации

### Аутентификация по паролю
```bash
adidnsdump -u 'CORP\jdoe' -p 'P@ssw0rd123!' 192.168.1.10
```
### Передача хеша (NTLM)
Если вы извлекли хэши NTLM с помощью Mimikatz или Secretsdump:
```bash
adidnsdump -u 'CORP\jdoe' --hashes :31d6cfe0d16ae931b73c59d7e0c089c0 192.168.1.10
```
### Билет Kerberos (ccache)
Использование существующего билета Kerberos TGT/TGS, хранящегося в `KRB5CCNAME`:
```bash
export KRB5CCNAME=/tmp/jdoe.ccache
adidnsdump -k -u 'CORP\jdoe' dc01.corp.local
```
---

## 🚀 Команды дампа ядра

### Стандартный дамп зоны DNS Active Directory
Выводит все активные записи DNS из раздела DomainDnsZones по умолчанию:
```bash
adidnsdump -u 'corp.local\jdoe' -p 'P@ssw0rd123!' ldap://dc01.corp.local
```
Это создаст файл с именем `records.csv` в вашем текущем рабочем каталоге.

### Удаление захороненных/удалённых записей
DNS Windows помечает удаленные записи как «захороненные» за несколько дней до физического удаления. Эти записи часто показывают выведенные из эксплуатации системы, устаревшие серверы или тестовые среды:
```bash
adidnsdump -u 'corp.local\jdoe' -p 'P@ssw0rd123!' --include-tombstoned ldap://dc01.corp.local
```
### Сброс лесных DNS и DNS-зон домена
Извлеките записи из разделов DNS на уровне леса:
```bash
adidnsdump -u 'corp.local\jdoe' -p 'P@ssw0rd123!' --forest ldap://dc01.corp.local
```
### Таргетинг на определенные зоны DNS
Если цель управляет несколькими разделенными DNS-зонами или поддоменами:
```bash
adidnsdump -u 'corp.local\jdoe' -p 'P@ssw0rd123!' --zone dev.corp.local ldap://dc01.corp.local
```
### Безопасная шифрованная связь LDAPS
Запретите передачу учетных данных по сети в виде открытого текста:
```bash
adidnsdump --ssl -u 'corp.local\jdoe' -p 'P@ssw0rd123!' ldaps://dc01.corp.local
```
---

## 📊 Форматы экспорта и постобработка

### CSV-вывод
По умолчанию `adidnsdump` выводит `records.csv`. Поля включают в себя:
- `zone`: зона DNS, к которой принадлежит запись.
- `name`: Запишите имя хоста или метку.
- `type`: тип записи (A, AAAA, CNAME, SRV и т. д.).
- `value`: целевой IP-адрес, имя хоста или строка.

### Интеграция с BloodHound и Nmap
Извлеките все уникальные IP-адреса из дампов записей для автоматического сканирования:
```bash
# Filter unique IP addresses (Type A records)
awk -F',' '$3=="A" {print $4}' records.csv | sort -u > live_ips.txt

# Run fast port scan against newly discovered internal hosts
nmap -sS -Pn -iL live_ips.txt -T4 -p 80,443,445,3389 -oA internal_assets
```
---

## ⚡ Краткая справочная таблица

| Цель | Команда |
|-----------|---------|
| **Стандартный дамп ADIDNS** | `adidnsdump -u 'DOMAIN\User' -p 'Pass' <DC_IP>` |
| **Передача хэша (NTLM)** | `adidnsdump -u 'DOMAIN\User' --hashes :<NTLM> <DC_IP>` |
| **Аутентификация билета Kerberos** | `adidnsdump -k -u 'DOMAIN\User' <DC_FQDN>` |
| **Удалить надгробные записи**| `adidnsdump -u 'DOMAIN\User' -p 'Pass' --include-tombstoned <DC_IP>` |
| **Свалка лесных зон** | `adidnsdump -u 'DOMAIN\User' -p 'Pass' --forest <DC_IP>` |
| **Укажите целевую зону** | `adidnsdump -u 'DOMAIN\User' -p 'Pass' --zone <ZONE> <DC_IP>` |
| **Принудительное использование LDAPS (SSL)** | `adidnsdump --ssl -u 'DOMAIN\User' -p 'Pass' ldaps://<DC_IP>` |

---

## 🛡️ Соображения по защите и обнаружению

1. **Мониторинг запросов LDAP.** Включите политику расширенного аудита для доступа к службе каталогов (идентификатор события 4662), чтобы получать оповещения о больших объемах запросов к `CN=MicrosoftDNS,DC=DomainDnsZones`.
2. **Ограничить списки ACL ADIDNS.** Ограничьте разрешения на чтение в контейнере `MicrosoftDNS` администраторами домена или определенными административными ролями, если администраторам, не являющимся администраторами, не требуется внутренний самоанализ DNS.
3. **Honeytokens.** Размещайте привлекательные DNS-записи (например, `backup-vault.corp.local` или `admin-portal.corp.local`), указывающие на IP-адреса ловушек.

---

## ⚠️ Юридическая оговорка

> [!ВНИМАНИЕ]
> adidnsdump предназначен исключительно для авторизованных оценок безопасности, аудита Active Directory и тестирования на проникновение в пределах разрешенных областей. Запрос к службам каталогов в неавторизованных сетях является незаконным.