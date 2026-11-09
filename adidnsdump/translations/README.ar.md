<!-- language-home-navigation -->
[🏠 الصفحة الرئيسية العربية](../../README.ar.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🌲 adidnsdump - ورقة الغش كاملة

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
   █████╗ ██████╗ ██╗██████╗ ███╗   ██╗███████╗██████╗ ██╗   ██╗███╗   ███╗██████╗ 
  ██╔══██╗██╔══██╗██║██╔══██╗████╗  ██║██╔════╝██╔══██╗██║   ██║████╗ ████║██╔══██╗
  ███████║██║  ██║██║██║  ██║██╔██╗ ██║███████╗██║  ██║██║   ██║██╔████╔██║██████╔╝
  ██╔══██║██║  ██║██║██║  ██║██║╚██╗██║╚════██║██║  ██║██║   ██║██║╚██╔╝██║██╔═══╝ 
  ██║  ██║██████╔╝██║██████╔╝██║ ╚████║███████║██████╔╝╚██████╔╝██║ ╚═╝ ██║██║     
  ╚═╝  ╚═╝╚═════╝ ╚═╝╚═════╝ ╚═╝  ╚═══╝╚══════╝╚═════╝  ╚═════╝ ╚═╝     ╚═╝╚═╝     
           Active Directory Integrated DNS (ADIDNS) Reconnaissance Tool
```
<p align="center">
  <img src="https://img.shields.io/badge/Tool-adidnsdump-blue?style=for-the-badge" alt="adidnsdump">
  <img src="https://img.shields.io/badge/Author-Dirk--jan_Mollema-orange?style=for-the-badge" alt="dirkjanm">
  <img src="https://img.shields.io/badge/Category-Active_Directory-green?style=for-the-badge" alt="Active Directory">
  <img src="https://img.shields.io/badge/Protocol-LDAP_%2F_LDAPS-purple?style=for-the-badge" alt="LDAP">
</ص>

<p align="center">
  <b>🌲 تعداد وتفريغ مناطق وسجلات DNS المتكاملة (ADIDNS) للدليل النشط عبر LDAP دون عمليات نقل منطقة DNS (AXFR)</b>
</ص>

---

## 📋 جدول المحتويات

- [نظرة عامة وخلفية فنية](#-نظرة عامة--خلفية فنية)
- [التثبيت](#-التثبيت)
- [طرق المصادقة](#-أساليب المصادقة)
  - [مصادقة كلمة المرور](#مصادقة كلمة المرور)
  - [تمرير التجزئة (NTLM)](#تمرير التجزئة-ntlm)
  - [تذكرة Kerberos (ccache)](#kerberos-ticket-ccache)
- [أوامر الإغراق الأساسية](#-core-dumping-commands)
  - [تفريغ منطقة DNS للدليل النشط القياسي](#standard-active-directory-dns-zone-dump)
  - [إلقاء العلامات المميزة / السجلات المحذوفة](#dumping-tombstoned--deleted-records)
  - [تفريغ الغابات DNS ومناطق DNS المجال](#dumping-forest-dns--domain-dns-zones)
  - [استهداف مناطق DNS محددة](#targeting-special-dns-zones)
- [تنسيقات التصدير والمعالجة اللاحقة](#-تنسيقات التصدير--ما بعد المعالجة)
  - [إخراج CSV](#csv-output)
  - [التكامل مع BloodHound وNmap](#integrating-with-bloodhound--nmap)
- [الجدول المرجعي السريع](#-الجدول المرجعي السريع)
- [الاعتبارات الدفاعية والكشف](#-الاعتبارات الدفاعية--الكشف)
- [إخلاء المسؤولية القانونية](#-إخلاء المسؤولية القانونية)

---

## 🎯 نظرة عامة وخلفية فنية

في بيئات Windows Active Directory القياسية، يتم عادةً دمج DNS في Active Directory (**Active Directory-Integrated DNS / ADIDNS**). في هذه التكوينات، لا يتم تخزين سجلات DNS في ملفات نصية بسيطة، ولكن ككائنات LDAP داخل قسم المجال (مثل `DC=DomainDnsZones` و`DC=ForestDnsZones`).

يتم تعطيل عمليات نقل منطقة DNS التقليدية (`AXFR`) عالميًا تقريبًا على وحدات التحكم بالمجال. ومع ذلك، **أي مستخدم مجال تمت مصادقته** (حتى الحسابات ذات الامتيازات المنخفضة أو حسابات الخدمة المخترقة) لديه أذونات القراءة عبر عقد LDAP التي تخزن سجلات ADIDNS بشكل افتراضي.

تم تطويره بواسطة **Dirk-jan Mollema (@dirkjanm)**، **`adidnsdump`** يتصل بوحدة تحكم المجال عبر LDAP أو LDAPS، ويستعلم عن أقسام ADIDNS، ويستخرج جميع السجلات (A، AAAA، CNAME، SRV، TXT، MX، والسجلات المميزة)، مما يؤدي إلى إنشاء جرد دقيق لكل كمبيوتر ومحطة عمل وخدمة ويب داخلية في المجال.

---

## 📥 التثبيت
```bash
# Clone the repository and install with pip
git clone https://github.com/dirkjanm/adidnsdump.git
cd adidnsdump
pip install .

# Or install directly via pipx / pip
pipx install git+https://github.com/dirkjanm/adidnsdump.git
```
---

## 🔑 طرق التوثيق

### مصادقة كلمة المرور
```bash
adidnsdump -u 'CORP\jdoe' -p 'P@ssw0rd123!' 192.168.1.10
```
### تمرير التجزئة (NTLM)
إذا قمت باستخراج تجزئات NTLM عبر Mimikatz أو Secretsdump:
```bash
adidnsdump -u 'CORP\jdoe' --hashes :31d6cfe0d16ae931b73c59d7e0c089c0 192.168.1.10
```
### تذكرة Kerberos (ccache)
باستخدام تذكرة Kerberos TGT/TGS الموجودة والمخزنة في `KRB5CCNAME`:
```bash
export KRB5CCNAME=/tmp/jdoe.ccache
adidnsdump -k -u 'CORP\jdoe' dc01.corp.local
```
---

## 🚀 أوامر الإغراق الأساسية

### تفريغ منطقة DNS للدليل النشط القياسي
تفريغ كافة سجلات DNS النشطة من قسم DomainDnsZones الافتراضي:
```bash
adidnsdump -u 'corp.local\jdoe' -p 'P@ssw0rd123!' ldap://dc01.corp.local
```
يؤدي هذا إلى إنشاء ملف باسم `records.csv` في دليل العمل الحالي لديك.

### التخلص من السجلات المحذوفة / المحذوفة
يضع Windows DNS علامة على السجلات المحذوفة على أنها "علامة مميزة" لعدة أيام قبل الحذف الفعلي. تكشف هذه السجلات غالبًا عن الأنظمة التي تم إيقاف تشغيلها أو الخوادم القديمة أو بيئات الاختبار:
```bash
adidnsdump -u 'corp.local\jdoe' -p 'P@ssw0rd123!' --include-tombstoned ldap://dc01.corp.local
```
### إغراق الغابات DNS ومناطق DNS المجال
استخراج السجلات من أقسام DNS على مستوى الغابة:
```bash
adidnsdump -u 'corp.local\jdoe' -p 'P@ssw0rd123!' --forest ldap://dc01.corp.local
```
### استهداف مناطق DNS محددة
إذا كان الهدف يقوم بتشغيل مناطق DNS أو نطاقات فرعية متعددة:
```bash
adidnsdump -u 'corp.local\jdoe' -p 'P@ssw0rd123!' --zone dev.corp.local ldap://dc01.corp.local
```
### اتصالات مشفرة آمنة عبر LDAPS
منع بيانات الاعتماد من اجتياز الشبكة في نص عادي:
```bash
adidnsdump --ssl -u 'corp.local\jdoe' -p 'P@ssw0rd123!' ldaps://dc01.corp.local
```
---

## 📊 تنسيقات التصدير والمعالجة اللاحقة

### إخراج CSV
بشكل افتراضي، يقوم `adidnsdump` بإخراج `records.csv`. تشمل الحقول:
- `zone`: منطقة DNS التي ينتمي إليها السجل.
- `name`: تسجيل اسم المضيف أو التسمية.
- `type`: نوع السجل (A، AAAA، CNAME، SRV، وما إلى ذلك).
- `value`: عنوان IP المستهدف أو اسم المضيف أو السلسلة.

### التكامل مع BloodHound وNmap
قم باستخراج جميع عناوين IP الفريدة من السجلات التي تم تفريغها لإجراء المسح الآلي:
```bash
# Filter unique IP addresses (Type A records)
awk -F',' '$3=="A" {print $4}' records.csv | sort -u > live_ips.txt

# Run fast port scan against newly discovered internal hosts
nmap -sS -Pn -iL live_ips.txt -T4 -p 80,443,445,3389 -oA internal_assets
```
---

## ⚡ جدول مرجعي سريع

| الهدف | الأمر |
|-----------|--------|
| ** تفريغ ADIDNS القياسي ** | `adidnsdump -u 'DOMAIN\User' -p 'Pass' <DC_IP>` |
| ** تمرير التجزئة (NTLM) ** | `adidnsdump -u 'DOMAIN\User' --hashes :<NTLM> <DC_IP>` |
| **مصادقة تذكرة Kerberos** | `adidnsdump -k -u 'DOMAIN\User' <DC_FQDN>` |
| ** تفريغ السجلات المميزة **| `adidnsdump -u 'DOMAIN\User' -p 'Pass' --include-tombstoned <DC_IP>` |
| ** مناطق غابات التفريغ ** | `adidnsdump -u 'DOMAIN\User' -p 'Pass' --forest <DC_IP>` |
| **حدد المنطقة المستهدفة** | `adidnsdump -u 'DOMAIN\User' -p 'Pass' --zone <ZONE> <DC_IP>` |
| ** فرض LDAPS (SSL) ** | `adidnsdump --ssl -u 'DOMAIN\User' -p 'Pass' ldaps://<DC_IP>` |

---

## 🛡️ الاعتبارات الدفاعية والكشوفات

1. **مراقبة استعلام LDAP:** قم بتمكين سياسة التدقيق المتقدمة للوصول إلى خدمة الدليل (معرف الحدث 4662) للتنبيه بشأن الاستعلامات كبيرة الحجم ضد `CN=MicrosoftDNS,DC=DomainDnsZones`.
2. **تقييد قوائم ADIDNS ACL:** قم بتقييد أذونات القراءة في حاوية `MicrosoftDNS` لمسؤولي المجال أو أدوار إدارية محددة إذا لم يطلب غير المسؤولين استبطان DNS الداخلي.
3. **Honeytokens:** ازرع سجلات DNS جذابة (على سبيل المثال، `backup-vault.corp.local` أو `admin-portal.corp.local`) للإشارة إلى عناوين IP الخاصة بمصيدة الجذب.

---

## ⚠️إخلاء المسؤولية القانونية

> [!تنبيه]
> adidnsdump مخصص بشكل صارم للتقييمات الأمنية المعتمدة وعمليات تدقيق Active Directory واختبار الاختراق ضمن النطاقات المسموح بها. يعد الاستعلام عن خدمات الدليل على شبكات غير مصرح بها أمرًا غير قانوني.