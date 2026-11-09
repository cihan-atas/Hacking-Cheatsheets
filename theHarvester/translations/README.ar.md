<!-- language-home-navigation -->
[🏠 الصفحة الرئيسية العربية](../../README.ar.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ورقة الغش في الحصاد

[theHarvester](https://github.com/laramies/theHarvester) هي إحدى أدوات الاستخبارات مفتوحة المصدر (OSINT) الأكثر استخدامًا على نطاق واسع لجمع رسائل البريد الإلكتروني والنطاقات الفرعية وأسماء الموظفين والمضيفين الظاهريين والمنافذ المفتوحة ومفاتيح PGP للموظفين من مصادر عامة متنوعة (محركات البحث وخوادم مفاتيح PGP وShodan وCensys وCertspotter وما إلى ذلك).

---

## جدول المحتويات

- [مصادر البيانات المدعومة](#مصادر البيانات المدعومة)
- [التثبيت وتكوين مفتاح API](#installation--api-key-configuration)
- [استطلاع المجال الأساسي](#استطلاع المجال الأساسي)
- [الحصاد السلبي متعدد المصادر](#الحصاد السلبي متعدد المصادر)
- [التحقق النشط من DNS والفرض الغاشم](#active-dns-verification--brute-forcing)
- [تنسيقات الإخراج (HTML، XML، JSON)](#output-formats-html-xml-json)
- [تكامل سير عمل Pentest](#pentest-workflow-integration)

---

## مصادر البيانات المدعومة

يقوم TheHarvester بتجميع المعلومات الاستخبارية من عشرات المصادر السلبية والنشطة:
- **محركات البحث (مجانية / بدون مفتاح):** Google، Bing، DuckDuckGo، Yahoo، Baidu.
- **شفافية الشهادة:** CRT.sh, Certspotter.
- **مستودعات الأكواد والتهديدات:** GitHub، AlienVault OTX، ThreatCrowd، Rapid7، VirusTotal.
- **واجهات برمجة تطبيقات الاستطلاع وOSINT:** Shodan، وCensys، وHunter.io، وSecurityTrails، وIntelx، وFullContact.
- **الشبكة / DNS:** أنوبيس، HackerTarget، DNSDumpster.

---

## التثبيت وتكوين مفتاح API

### 1. التثبيت
```bash
# Debian / Ubuntu / Kali Linux
sudo apt-get update && sudo apt-get install -y theharvester

# From GitHub Source (Latest Version)
git clone https://github.com/laramies/theHarvester.git
cd theHarvester
python3 -m pip install -r requirements/base.txt

# Test binary
theHarvester -h
```
### 2. تكوين مفاتيح API (`api-keys.yaml`)
تتطلب العديد من المصادر (Hunter، وShodan، وCensys، وIntelx، وGitHub) مفاتيح API مجانية أو مدفوعة.
قم بإنشاء أو تحرير `api-keys.yaml` في مسار التكوين:
```yaml
apikeys:
  hunter:
    key: "YOUR_HUNTER_KEY"
  shodan:
    key: "YOUR_SHODAN_KEY"
  github:
    key: "YOUR_GITHUB_TOKEN"
  securityTrails:
    key: "YOUR_SECURITYTRAILS_KEY"
```
---

## استطلاع المجال الأساسي

### 1. حصاد رسائل البريد الإلكتروني والنطاقات الفرعية باستخدام محركات مجانية
```bash
theHarvester -d example.com -l 500 -b duckduckgo,bing,crtsh
```
- `-d <domain>`: النطاق المستهدف لإعادة اكتشافه.
- `-l <limit>`: عدد النتائج المطلوب الاستعلام عنها لكل محرك.
- `-b <sources>`: قائمة مصادر مفصولة بفواصل (أو `all`).

### 2. الاستعلام عن كافة المصادر التي تم تكوينها
```bash
theHarvester -d example.com -l 500 -b all
```
---

## الحصاد السلبي متعدد المصادر

الاستعلام عن قواعد بيانات خاصة بمعلومات التهديدات وشهادات الشفافية دون إرسال حركة المرور إلى الهدف:

### 1. شهادة الشفافية والتهديدات من Intel
```bash
theHarvester -d example.com -b crtsh,certspotter,anubis,hackertarget,otx
```
### 2. استهداف مجموعات مصادر محددة
```bash
# Query GitHub commits and issues for leaked emails and subdomains
theHarvester -d example.com -b github-code

# Query Shodan for discovered IP addresses and open ports
theHarvester -d example.com -b shodan
```
---

## التحقق النشط من DNS والقوة الغاشمة

### 1. حل النطاقات الفرعية المكتشفة (`-r`)
قم بإجراء عمليات بحث DNS نشطة للتحقق مما إذا كانت النطاقات الفرعية التي تم جمعها موجودة حاليًا:
```bash
theHarvester -d example.com -b crtsh,bing -r
```
### 2. القوة الغاشمة لنظام أسماء النطاقات (`-c`)
تنفيذ القوة الغاشمة لنطاق DNS الفرعي القائم على قائمة الكلمات مباشرة داخل theHarvester:
```bash
theHarvester -d example.com -b duckduckgo -c
```
### 3. التحقق من المضيف الظاهري (`-v`)
التحقق من المضيفين الظاهريين عبر طلبات HTTP:
```bash
theHarvester -d example.com -b duckduckgo -v
```
---

## تنسيقات الإخراج (HTML، XML، JSON)

تصدير النتائج لإعداد التقارير الآلية أو الاستطلاع النهائي:

### 1. تصدير إلى HTML وXML
```bash
theHarvester -d example.com -b duckduckgo,crtsh -f /tmp/theharvester_results
```
إنشاء `/tmp/theharvester_results.html` و`/tmp/theharvester_results.xml`.

### 2. تحليل النطاقات الفرعية المكتشفة باستخدام `jq`
في حالة تصدير أو تحليل JSON:
```bash
theHarvester -d example.com -b crtsh,duckduckgo -f recon_out
# Extract clean host list
cat recon_out.xml | grep -oP '(?<=<host>).*?(?=</host>)' | sort -u > live_hosts.txt
```
---

## تكامل سير العمل Pentest

قم بتسلسل نتائج theHarvester مباشرةً باستخدام أدوات فحص الثغرات النشطة:
```bash
# 1. Harvest passive subdomains
theHarvester -d target.corp -b crtsh,duckduckgo,bing -f target_recon

# 2. Extract unique domain names
grep -oP '(?<=<host>)[^<]+' target_recon.xml | sort -u > hosts.txt

# 3. Probe for live web services using httpx
cat hosts.txt | httpx -silent -status-code -title > live_web.txt

# 4. Scan live web services with Nuclei
nuclei -l live_web.txt -severity medium,high,critical
```