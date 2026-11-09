<!-- language-home-navigation -->
[🏠 الصفحة الرئيسية العربية](../../README.ar.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ورقة الغش wafw00f

[wafw00f](https://github.com/EnableSecurity/wafw00f) عبارة عن أداة مفتوحة المصدر لجدار حماية تطبيقات الويب (WAF) لأخذ بصمات الأصابع واكتشافها. فهو يحدد ويأخذ بصمات أكثر من 150 حلًا أمنيًا تجاريًا ومفتوح المصدر لـ WAF لحماية مواقع الويب المستهدفة عن طريق إرسال طلبات HTTP العادية والاستفزازية وتحليل الاستجابات السلوكية والعناوين وملفات تعريف الارتباط.

---

## جدول المحتويات

- [لماذا WAFs لبصمات الأصابع؟](#why-fingerprint-wafs)
- [التثبيت](#التثبيت)
- [المسح الأساسي والمسح المستهدف](#basic--target-scanning)
- [الكشف الشامل عن WAF المتعدد (`-a`)](#exhaustive-multi-waf-detection--a)
- [المسح الشامل للهدف من ملف (`-i`)](#bulk-target-scanning-from-file--i)
- [توجيه الوكيل وتور (`-p`)](#proxy--tor-routing--p)
- [تنسيقات التصدير (JSON، CSV، نص)](#export-formats-json-csv-text)
- [قائمة توقيعات WAF المدعومة (`-l`)](#list-supported-waf-signatures--l)
- [مكافأة الأخطاء وتكامل اختبار الاختراق](#bug-bounty--penetration-testing-integration)

---

## لماذا WAFs ذات بصمة الإصبع؟

إن إطلاق هجمات الويب النشطة (حقن SQL، XSS، SSRF) ضد هدف محمي دون استطلاع مسبق غالبًا ما يؤدي إلى حظر IP فوري أو نتائج ثغرة أمنية سلبية كاذبة. يتيح تحديد WAF الدقيق ما يلي:
- **التهرب المصمم:** تحديد تقنيات التجاوز الخاصة بالبائع (على سبيل المثال، تسوية Unicode لـ ModSecurity، وترميز النقل المقسم لـ Cloudflare).
- **Origin IP Hunting:** إذا تم اكتشاف Cloudflare أو Akamai، قم بالتمحور للعثور على عنوان IP الأصلي للواجهة الخلفية قبل الاختبار.
- **الوعي بحدود القاعدة:** ضبط تزامن الماسح الضوئي وحدود المعدل تحت حدود الحظر.

---

## تثبيت
```bash
# Debian / Ubuntu / Kali Linux
sudo apt-get update && sudo apt-get install -y wafw00f

# Recommended: Install via pipx
pipx install wafw00f

# Via standard Python pip
python3 -m pip install --user wafw00f

# Verify installation
wafw00f --version
```
---

## المسح الأساسي والهدف

### 1. مسح مجال واحد
```bash
wafw00f https://example.com
```
*يرسل wafw00f طلبات HTTP الأساسية متبوعة بأنماط ضارة مخصصة لتشغيل مرشحات الأمان وتحديد التوقيعات في الرؤوس ورموز الحالة وصفحات الخطأ.*

### 2. وضع الإخراج المطول (`-v` / `-vv`)
افحص الرؤوس المطابقة المحددة، ورموز حالة HTTP، والاستدلالات:
```bash
wafw00f https://example.com -v
```
### 3. تعطيل إعادة التوجيه للمتابعة (`-r`)
منع wafw00f من اتباع عمليات إعادة التوجيه 301/302 لفصل الوكلاء العكسيين للحافة عن تطبيقات الواجهة الخلفية:
```bash
wafw00f https://example.com -r
```
---

## اكتشاف شامل لـ WAF المتعدد (`-a`)

افتراضيًا، يتوقف wafw00f عن المسح بمجرد تأكيد تطابق WAF الأول. استخدم `-a` للتحقق من جميع توقيعات البائعين المعروفة (ضرورية عندما يكون التطبيق خلف دفاع متعدد المستويات مثل Cloudflare CDN + AWS WAF):
```bash
wafw00f https://example.com -a
```
---

## المسح الشامل للهدف من الملف (`-i`)

قم بمسح قائمة كاملة من النطاقات الفرعية أو الأصول المستهدفة المجمعة من `subfinder` أو `amass`:
```bash
wafw00f -i /path/to/domains.txt -o /tmp/waf_results.txt
```
---

## توجيه الوكيل وTor (`-p`)

قم بتوجيه حركة المرور من خلال الوكلاء لمنع إدراج عناوين IP في القائمة السوداء أو فحص الاستعلامات في Burp Suite:

### 1. التوجيه عبر وكيل Burp Suite
```bash
wafw00f https://example.com -p http://127.0.0.1:8080
```
### 2. التوجيه عبر وكيل Tor SOCKS5 المحلي
```bash
wafw00f https://example.com -p socks5://127.0.0.1:9050
```
---

## تنسيقات التصدير (JSON، CSV، نص)

تصدير النتائج لإعداد التقارير الآلية أو أدوات الاستطلاع النهائية:

### 1. تصدير إلى تنسيق JSON
```bash
wafw00f https://example.com -o results.json --format json
```
### 2. تصدير إلى جدول بيانات CSV
```bash
wafw00f -i targets.txt -o results.csv --format csv
```
---

## قائمة توقيعات WAF المدعومة (`-l`)

عرض جميع منتجات WAF التي يزيد عددها عن 150+:
```bash
wafw00f -l
```
تشمل المنتجات الرئيسية المدعومة ما يلي:
- **موفرو الخدمات السحابية:** Cloudflare، وAWS WAF، وAkamai، وMicrosoft Azure Web Application Firewall، وGoogle Cloud Armor.
- **أجهزة المؤسسات:** F5 BIG-IP ASM، وImperva SecureSphere، وBarracuda، وFortinet FortiWeb، وCitrix NetScaler.
- **المصدر/البرمجيات المفتوحة:** ModSecurity، NAXSI، Shadow Daemon، OpenResty.

---

## مكافأة الأخطاء والتكامل مع اختبار الاختراق

دمج wafw00f في خط أنابيب هجوم الويب الآلي:
```bash
#!/usr/bin/env bash
TARGET="https://app.target.corp"

echo "[*] Detecting WAF for $TARGET..."
WAF=$(wafw00f "$TARGET" -o - --format json | jq -r '.[0].detected')

if [ "$WAF" == "true" ]; then
    WAF_NAME=$(wafw00f "$TARGET" -o - --format json | jq -r '.[0].firewall')
    echo "[!] Active WAF detected: $WAF_NAME"
    echo "[+] Adjusting sqlmap to use vendor-specific tamper scripts..."
    if [[ "$WAF_NAME" =~ "Cloudflare" ]]; then
        sqlmap -u "$TARGET/item?id=1" --tamper=between,randomcase,space2comment
    fi
else
    echo "[+] No active WAF detected. Proceeding with standard scanner."
    sqlmap -u "$TARGET/item?id=1" --batch
fi
```