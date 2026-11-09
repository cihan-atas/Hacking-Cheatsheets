<!-- language-home-navigation -->
[🏠 الصفحة الرئيسية العربية](../../README.ar.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ورقة الغش لنظام dnsrecon

[dnsrecon](https://github.com/darkoperator/dnsrecon) عبارة عن أداة شاملة لاستطلاع وتعداد DNS مكتوبة بلغة Python. وهو يدعم عمليات نقل المنطقة (AXFR)، وعمليات البحث العكسي، والتأثير الغاشم للمجال، واكتشاف سجل SRV، واكتشاف أحرف البدل، والتطفل على ذاكرة التخزين المؤقت، وتوسيع نطاق TLD.

---

## جدول المحتويات

- [القدرات الأساسية](#القدرات الأساسية)
- [التثبيت](#التثبيت)
- [التعداد القياسي ونقل المنطقة (`-d -t std / axfr`)](#standard-enumeration--zone-transfer--d--t-std--axfr)
- [التأثير الغاشم لنطاق DNS الفرعي (`-t brt`)](#dns-subdomain-brute-forcing--t-brt)
- [مسح نطاق البحث العكسي (`-t rvl`)](#reverse-lookup-range-scanning--t-rvl)
- [التطفل على ذاكرة التخزين المؤقت لنظام أسماء النطاقات (`-t snoop`)](#dns-cache-snooping--t-snoop)
- [استهداف خوادم الأسماء المحددة (`-n`)](#targeting-special-nameservers--n)
- [تنسيقات الإخراج (CSV، JSON، SQLite، XML)](#output-formats-csv-json-sqlite-xml)
- [التقوية الدفاعية لخوادم DNS](#التقوية الدفاعية لخوادم DNS)

---

## القدرات الأساسية

- **اختبار نقل المنطقة (AXFR):** يختبر تلقائيًا جميع خوادم الأسماء الموثوقة لعمليات نقل المنطقة غير المصادق عليها.
- **تعداد السجلات:** استرداد سجلات SOA وNS وA وAAAA وMX وTXT وSPF وSRV.
- ** القوة الغاشمة للنطاق الفرعي: ** عمليات البحث في القاموس متعددة الخيوط مع تصفية أحرف البدل.
- ** المسح العكسي لنطاق IP: ** استخراج سجل PTR عبر كتل CIDR التعسفية.
- **التطفل على ذاكرة التخزين المؤقت:** يختبر ما إذا كانت خوادم الأسماء العودية تحتوي على مجالات محددة مخزنة مؤقتًا في الذاكرة.
- **توسيع TLD:** يكتشف النطاقات المطابقة عبر رمز البلد ونطاقات المستوى الأعلى العامة.

---

## تثبيت
```bash
# Debian / Ubuntu / Kali Linux
sudo apt-get update && sudo apt-get install -y dnsrecon

# Python pip / pipx
pipx install dnsrecon

# From source
git clone https://github.com/darkoperator/dnsrecon.git
cd dnsrecon
pip install -r requirements.txt

# Verify installation
dnsrecon -h
```
---

## التعداد القياسي ونقل المناطق (`-d -t std / axfr`)

### 1. تعداد سجل DNS القياسي
يجمع سجلات SOA وNS وA وAAAA وMX وTXT، ويحاول تلقائيًا إجراء عمليات نقل المنطقة ضد كل NS موثوق:
```bash
dnsrecon -d example.com -t std
```
### 2. اختبار نقل المنطقة المخصصة (AXFR) فقط
اختبار فقط لثغرة AXFR ضد كافة خوادم الأسماء الموثوقة:
```bash
dnsrecon -d example.com -t axfr
```
*في حالة النجاح، يتم التخلص من قاعدة بيانات DNS الخاصة بالهدف بالكامل دون استخدام القوة الغاشمة.*

---

## التأثير الغاشم لنطاق DNS الفرعي (`-t brt`)

نطاقات القوة الغاشمة الفرعية باستخدام قوائم الكلمات المخصصة أو الافتراضية:

### 1. القوة الغاشمة للنطاق الفرعي باستخدام قائمة الكلمات المخصصة
```bash
dnsrecon -d example.com -t brt -D /usr/share/wordlists/dnsmap.txt
```
- `-d <domain>`: المجال المستهدف.
- `-t brt`: وضع القوة الغاشمة.
- `-D <file>`: ملف قاموس النطاق الفرعي.

### 2. القوة الغاشمة مع تصفية أحرف البدل
يمنع تكوينات DNS الخاصة بأحرف البدل (الحل `*.example.com` لعنوان IP واحد شامل) من تلويث النتائج:
```bash
dnsrecon -d example.com -t brt -D /usr/share/seclists/Discovery/DNS/subdomains-top1million-5000.txt --iw
```
- `--iw`: تجاهل نتائج دقة أحرف البدل.

---

## مسح نطاق البحث العكسي (`-t rvl`)

تعداد سجلات PTR لكتلة IP لتحديد المضيفين الداخليين وبوابات البريد والمثيلات المرحلية:
```bash
dnsrecon -r 192.168.1.0/24 -t rvl
```
- `-r <range>`: نطاق IP في تدوين CIDR (على سبيل المثال، `192.168.1.0/24`) أو تدوين البداية (`192.168.1.1-192.168.1.254`).
- `-t rvl`: وضع البحث العكسي.

---

## التطفل على ذاكرة التخزين المؤقت لنظام أسماء النطاقات (`-t snoop`)

تحديد ما إذا كان العملاء الموجودون على شبكة مستهدفة قد قاموا مؤخرًا بحل مجالات معينة من خلال محللهم الداخلي:
```bash
dnsrecon -t snoop -n 192.168.1.1 -D sensitive_domains.txt
```
- `-n <resolver_ip>`: خادم DNS المستهدف للاستعلام.
- `-D <domains_file>`: قائمة النطاقات المطلوب اختبارها (مثل المنافسين، وC2s الخبيثة، والبوابات الداخلية).

---

## استهداف خوادم أسماء محددة (`-n`)

الاستعلام عن خوادم DNS الداخلية أو الخاصة بالهدف مباشرةً بدلاً من وحدات الحل العامة:
```bash
# Query the company's internal DNS server directly
dnsrecon -d corp.internal -n 10.10.10.2 -t std
```
---

## تنسيقات الإخراج (CSV، JSON، SQLite، XML)

حفظ النتائج للتكامل مع التذاكر أو SIEM أو مراحل الهجوم الإضافية:

### 1. تصدير إلى JSON
```bash
dnsrecon -d example.com -t std --json /tmp/dns_recon.json
```
### 2. تصدير إلى قاعدة بيانات SQLite
```bash
dnsrecon -d example.com -t std --db /tmp/dns_results.sqlite
```
### 3. تصدير إلى CSV وXML
```bash
dnsrecon -d example.com -t std --csv /tmp/dns_results.csv --xml /tmp/dns_results.xml
```
---

## التعزيز الدفاعي لخوادم DNS

| الضعف | خطر الاستغلال | الموصى بها الدفاع |
|---|---|---|
| ** AXFR غير مقيد ** | رسم خرائط البنية التحتية الداخلية الكاملة | تقييد عمليات نقل المنطقة إلى عناوين IP لخادم الأسماء الثانوية المعتمدة فقط (`allow-transfer { 10.0.0.2; };` في BIND). |
| ** فتح محلل عودي ** | تضخيم DNS DDoS والتطفل | تعطيل العودية الخارجية (`recursion no;` في BIND). |
| **أسماء المضيفين المتوقعة** | التأثير الغاشم للنطاق الفرعي | استخدم النطاقات الفرعية العشوائية لبيئات التدريج الحساسة. |
| ** الاستيلاء على النطاق الفرعي ** | يشير CNAME إلى خدمات AWS/GitHub/Azure التي لم تتم المطالبة بها | قم بمراجعة سجلات DNS CNAME المتدلية وإزالة التعيينات غير المستخدمة على الفور. |