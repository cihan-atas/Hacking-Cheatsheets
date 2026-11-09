<!-- language-home-navigation -->
[🏠 الصفحة الرئيسية العربية](../../README.ar.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 📁 DIRB - ورقة الغش الكاملة

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ██████╗ ██╗██████╗ ██████╗ 
  ██╔══██╗██║██╔══██╗██╔══██╗
  ██║  ██║██║██████╔╝██████╔╝
  ██║  ██║██║██╔══██╗██╔══██╗
  ██████╔╝██║██║  ██║██████╔╝
  ╚═════╝ ╚═╝╚═╝  ╚═╝╚═════╝ 
        Web Content Scanner
```
<p align="center">
  <img src="https://img.shields.io/badge/DIRB-Content_Scanner-orange?style=for-the-badge" alt="DIRB">
  <img src="https://img.shields.io/badge/Wordlists-Bruteforce-blue?style=for-the-badge" alt="Wordlists">
  <img src="https://img.shields.io/badge/Kali_Linux-Preinstalled-green?style=for-the-badge" alt="كالي لينكس">
  <img src="https://img.shields.io/badge/Web-Pentesting-red?style=for-the-badge" alt="Web Pentesting">
</ص>

<p align="center">
  <b>🔍 يقوم ماسح محتوى الويب الكلاسيكي بشن هجمات تعتمد على القاموس ضد خوادم الويب</b>
</ص>

---

## 📋 جدول المحتويات

- [المقدمة](#-المقدمة)
- [DIRB مقابل البدائل الحديثة](#-dirb-vs-modern-alternatives)
- [التثبيت](#-التثبيت)
- [قوائم الكلمات الافتراضية](#-قوائم الكلمات الافتراضية)
- [بناء الجملة الأساسي](#-بناء الجملة الأساسي)
- [سيناريوهات المسح الشائعة](#-سيناريوهات المسح الشائعة)
- [الخيارات المتقدمة والضبط](#-خيارات---ضبط)
- [صيد الثغرات الأمنية باستخدام القوائم المضمنة](#-صيد الثغرات باستخدام القوائم المضمنة)
- [سير العمل في العالم الحقيقي](#-سير العمل في العالم الحقيقي)
- [الجدول المرجعي السريع](#-الجدول المرجعي السريع)
- [النصائح وأفضل الممارسات](#-نصائح--أفضل-الممارسات)
- [إخلاء المسؤولية القانونية](#-إخلاء المسؤولية القانونية)
- [الموارد](#-الموارد)

---

## 🎯 المقدمة

**DIRB** (Directory Buster) عبارة عن أداة فحص محتوى ويب كلاسيكية وخفيفة الوزن تعتمد على عنوان URL مكتوبة بلغة C. وهي تطلق هجمات قائمة على القاموس ضد خوادم الويب، وتبحث عن كائنات الويب الموجودة والمخفية (الملفات والأدلة والبرامج النصية ونقاط النهاية).

يعمل DIRB عن طريق إنشاء طلبات HTTP لكل كلمة في قائمة الكلمات، وتحليل رموز استجابة الخادم (200، 301، 302، 403، وما إلى ذلك)، وتحديد ما إذا كان المورد موجودًا أم لا.

### الخصائص الرئيسية

- ✅ **المسح العودي التلقائي:** يقوم المسح باكتشاف الدلائل الفرعية تلقائيًا بشكل افتراضي.
- ✅ **قوائم الكلمات المتخصصة المضمنة:** تأتي مع قوائم كلمات مستهدفة لخوادم الويب وأنظمة إدارة المحتوى ونقاط الضعف الشائعة.
- ✅ **بصمة منخفضة للموارد:** ثنائي خفيف الوزن يعمل بسلاسة حتى على الأجهزة منخفضة المواصفات أو الأنظمة القديمة.
- ✅ ** دعم مخصص للرأس والوكيل: ** سهولة التكامل مع Burp Suite ووكلاء HTTP الأساسيين.

---

## ⚖️ ديرب مقابل البدائل الحديثة

| ميزة | ديرب | جوبستر | ففوف | فيروكسبوستر | ديرسيرتش |
|---------|------|---------|-------------|------------|------|------|------|
| **اللغة** | ج | اذهب | اذهب | الصدأ | بايثون |
| **السرعة** | ⭐⭐(الخيط المنخفض) | ⭐⭐⭐⭐⭐ (سريع) | ⭐⭐⭐⭐⭐ (سريع للغاية) | ⭐⭐⭐⭐⭐ (Async Rust) | ⭐⭐⭐⭐ (سريع) |
| ** العودية الافتراضية ** | ✅ نعم (تلقائي) | ❌ لا | ❌ لا (اختياري `-recursion`) | ✅ نعم (تلقائي) | ✅ نعم (`-r`) |
| ** قوائم الكلمات المخصصة متضمنة ** | ✅ نعم (`/usr/share/dirb/`) | ❌ لا | ❌ لا | ❌ لا | ✅ نعم (`db/dicc.txt`) |
| ** VHost Fuzzing ** | ❌ لا | ✅ نعم | ✅ نعم | ❌ لا | ✅ نعم (`--subdirs`) |
| **POST / تشويش البيانات** | ❌ لا | ❌ لا | ✅ نعم | ❌ لا | ✅ نعم (`-d`) |
| ** استخدام الذاكرة / وحدة المعالجة المركزية ** | منخفض جدًا | منخفض | منخفض | منخفض جدًا | معتدل |

---

## 📥 التثبيت

### كالي لينكس ونظام تشغيل الببغاء

يأتي DIRB مثبتًا مسبقًا على نظامي Kali Linux وParrot OS:
```bash
# Verify installation
dirb

# Update to latest package
sudo apt update && sudo apt install -y dirb
```
### ديبيان / أوبونتو
```bash
sudo apt update
sudo apt install -y dirb
```
### التجميع من المصدر
```bash
# Install build dependencies
sudo apt install -y build-essential libcurl4-openssl-dev

# Download and extract source
wget https://sourceforge.net/projects/dirb/files/dirb/2.22/dirb222.tar.gz
tar -xzvf dirb222.tar.gz
cd dirb222

# Configure permissions and compile
chmod -R 755 conf
chmod 755 configure
./configure
make
sudo make install
```
---

## 📚 قوائم الكلمات الافتراضية

عند تثبيته على Kali Linux، يتضمن DIRB قوائم كلمات مخصصة موجودة في `/usr/share/dirb/wordlists/`:

| مسار قائمة الكلمات | مداخل | الغرض |
|---------------|---------|---------|
| `/usr/share/dirb/wordlists/common.txt` | ~4,614 | القائمة الافتراضية المستخدمة عندما لا يتم تحديد قائمة الكلمات |
| `/usr/share/dirb/wordlists/small.txt` | ~959 | الاكتشاف السريع للمسارات ذات الاحتمالية العالية |
| `/usr/share/dirb/wordlists/big.txt` | ~20,469 | دليل شامل والبحث عن الملفات |
| `/usr/share/dirb/wordlists/catala.txt` | ~6,846 | قائمة الكلمات الكاتالونية |
| `/usr/share/dirb/wordlists/spanish.txt` | ~11,720 | قائمة الكلمات الاسبانية |
| `/usr/share/dirb/wordlists/mutations_common.txt` | ~1000 | اختلافات البادئة/اللاحقة الشائعة |
| `/usr/share/dirb/wordlists/extensions_common.txt` | ~40 | ملحقات الويب القياسية (`.php`، `.asp`، `.html`، إلخ) |

### قوائم الكلمات الخاصة بالثغرات الأمنية (`/usr/share/dirb/wordlists/vulns/`)

- `apache.txt` - الملفات الخاصة بـ Apache وصفحات الحالة والأدلة المعروفة
- `iis.txt` - ملفات خادم الويب Microsoft IIS (`web.config`، `aspnet_client`، إلخ.)
- `tomcat.txt` - مسارات إدارة ومدير Apache Tomcat
- `cgis.txt` - البرامج النصية ونقاط النهاية لواجهة البوابة المشتركة (CGI).
- `fatwire.txt`، `hpsmh.txt`، `hyperion.txt`، `iplanet.txt`، `netware.txt`، `oracle.txt`

---

## ⌨️ بناء الجملة الأساسي
```bash
dirb <url_base> [<wordlist_file(s)>] [options]
```
### أمثلة أساسية
```bash
# Basic scan using default common.txt wordlist
dirb http://10.10.10.10

# Scan using a specific custom wordlist
dirb https://example.com /usr/share/wordlists/dirb/big.txt

# Scan with custom SecLists directory list
dirb http://target.local /usr/share/seclists/Discovery/Web-Content/directory-list-2.3-medium.txt
```
---

## 🔍 سيناريوهات المسح الشائعة

### 1. المسح الضوئي باستخدام امتدادات الملفات (`-X` و`-x`)

ابحث عن امتدادات ملفات محددة مرفقة بكل كلمة في القاموس:
```bash
# Test specific extensions on every wordlist entry
dirb http://target.local -X .php,.html,.txt,.bak

# Use an extensions list file
dirb http://target.local -x /usr/share/dirb/wordlists/extensions_common.txt

# Combine directory brute-force with extension brute-force
dirb http://target.local /usr/share/dirb/wordlists/common.txt -X .php,.json,.zip
```
### 2. التحكم في التكرار (`-r` و`-R`)

افتراضيًا، يقوم DIRB بالزحف بشكل متكرر إلى كل دليل تم اكتشافه. يمكنك التحكم في هذا السلوك:
```bash
# Disable recursion completely (scan root level only)
dirb http://target.local -r

# Interactive recursion (DIRB asks before descending into each directory)
dirb http://target.local -R
```
### 3. المسح الضوئي غير الحساس لحالة الأحرف (`-i`)

مفيد عند استهداف خوادم Windows / IIS حيث تكون مسارات الملفات غير حساسة لحالة الأحرف:
```bash
dirb http://target.local -i
```
### 4. تجاهل رموز حالة HTTP المحددة (`-N`)

تصفية رموز الاستجابة غير المرغوب فيها (على سبيل المثال، حلقات إعادة التوجيه أو المسارات المحظورة):
```bash
# Ignore 302 redirects and 403 forbidden responses
dirb http://target.local -N 302,403
```
---

## ⚙️ خيارات وضبط متقدم

### المصادقة (`-u`)

يدعم DIRB مصادقة HTTP الأساسية والملخصة:
```bash
# Supply HTTP authentication credentials
dirb http://target.local -u "admin:SecretP@ssw0rd!"

# Combine auth with custom wordlist
dirb http://target.local /usr/share/dirb/wordlists/big.txt -u "pentester:Summer2026"
```
### ملفات تعريف الارتباط المخصصة ورموز الجلسة (`-c`)

اختبار مسارات تطبيق الويب المصادق عليها من خلال توفير ملفات تعريف الارتباط للجلسة:
```bash
# Pass session cookie
dirb http://target.local -c "PHPSESSID=d92j4fn82mdf82ncb184; logged_in=true"
```
### وكيل مستخدم مخصص ورؤوس HTTP (`-a` و`-H`)

تجاوز قواعد WAF الأساسية أو محاكاة المتصفحات الشرعية:
```bash
# Set custom User-Agent
dirb http://target.local -a "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36"

# Add custom HTTP Header
dirb http://target.local -H "Authorization: Bearer eyJhbGciOi..."
dirb http://target.local -H "X-Forwarded-For: 127.0.0.1"
```
### توجيه الوكيل وتكامل Burp Suite (`-p` و`-P`)

أرسل حركة مرور DIRB عبر وكيل HTTP الأساسي مثل Burp Suite أو OWASP ZAP:
```bash
# Route through local Burp Suite proxy
dirb http://target.local -p 127.0.0.1:8080

# Authenticated proxy
dirb http://target.local -p 10.10.10.1:3128 -P "proxyuser:proxypass"
```
### تحديد المعدل والتهرب (`-z`)

تجنب تشغيل تنبيهات IDS/IPS أو خدمات الويب الهشة DoSing عن طريق إضافة تأخير بين الطلبات (بالمللي ثانية):
```bash
# Wait 250 milliseconds between each request
dirb http://target.local -z 250

# Slow, stealthy scan (1 second delay)
dirb http://target.local -z 1000
```
### تسجيل الإخراج (`-o` و`-w`)
```bash
# Save results to a file
dirb http://target.local -o /home/zeus/scans/dirb_target.txt

# Do not halt on WARNING messages (useful for uninterrupted scans)
dirb http://target.local -w -o dirb_output.txt
```
---

## 🎯 البحث عن الثغرات الأمنية باستخدام القوائم المدمجة

يتضمن DIRB قوائم مستهدفة في `/usr/share/dirb/wordlists/vulns/` لاكتشاف الخوادم غير المصححة ونقاط النهاية الخطيرة:
```bash
# Target Apache server vulnerabilities and status pages
dirb http://target.local /usr/share/dirb/wordlists/vulns/apache.txt

# Target IIS endpoints and misconfigurations
dirb http://target.local /usr/share/dirb/wordlists/vulns/iis.txt

# Target Tomcat management consoles
dirb http://target.local /usr/share/dirb/wordlists/vulns/tomcat.txt

# Search for old CGI scripts and shellshock entrypoints
dirb http://target.local /usr/share/dirb/wordlists/vulns/cgis.txt
```
---

## 🎬 سير العمل في العالم الحقيقي

### سير العمل 1: الاستطلاع الأولي السريع

عندما تواجه خادم ويب لأول مرة على المنفذ 80/443:
```bash
# Rapid scan without recursion using small wordlist
dirb http://10.10.10.50 /usr/share/dirb/wordlists/small.txt -r
```
### سير العمل 2: تقييم شامل لتطبيقات الويب

قم بإجراء فحص عميق للبحث عن النسخ الاحتياطية للتكوين، والتعليمة البرمجية المصدر، ونقاط نهاية واجهة برمجة التطبيقات:
```bash
dirb https://app.target.local /usr/share/dirb/wordlists/common.txt \
  -X .php,.bak,.sql,.config,.txt \
  -a "Mozilla/5.0" \
  -w \
  -o dirb_app_scan.txt
```
### سير العمل 3: الزحف إلى البوابة الإلكترونية المصادق عليها عبر Burp Suite
```bash
dirb https://internal.target.local/portal/ /usr/share/dirb/wordlists/big.txt \
  -c "session=s%3A_kJw81..." \
  -p 127.0.0.1:8080 \
  -X .jsp,.do,.action \
  -z 100
```
---

## 📊 جدول مرجعي سريع

| علم | حجة | الوصف | مثال |
|------|----------|------------|---------|
| `-a` | `<user_agent>` | حدد سلسلة وكيل المستخدم المخصصة | `dirb http://target -a "Mozilla/5.0"` |
| `-c` | `<cookie>` | قم بتعيين رأس ملف تعريف ارتباط HTTP | `dirb http://target -c "id=123"` |
| `-H` | `<header>` | أضف رأس HTTP مخصص | `dirb http://target -H "X-API-Key: secret"` |
| `-i` | لا شيء | بحث حساس لحالة الأحرف | `dirb http://target -i` |
| `-N` | `<codes>` | تجاهل الردود المطابقة لرمز الحالة | `dirb http://target -N 302,403` |
| `-o` | `<output_file>` | حفظ نتائج المسح في ملف | `dirb http://target -o results.txt` |
| `-p` | `<proxy:port>` | استخدم وكيل HTTP | `dirb http://target -p 127.0.0.1:8080` |
| `-P` | `<user:pass>` | بيانات اعتماد مصادقة الوكيل | `dirb http://target -p proxy:8080 -P u:p` |
| `-r` | لا شيء | لا تبحث بشكل متكرر | `dirb http://target -r` |
| `-R` | لا شيء | البحث العودي التفاعلي | `dirb http://target -R` |
| `-u` | `<user:pass>` | مصادقة HTTP الأساسية/الملخص | `dirb http://target -u "admin:1234"` |
| `-w` | لا شيء | لا تتوقف عن فحص رسائل التحذير | `dirb http://target -w` |
| `-X` | `<ext_list>` | إلحاق ملحقات مفصولة بفواصل | `dirb http://target -X .php,.html` |
| `-x` | `<ext_file>` | إلحاق الملحقات من الملف | `dirb http://target -x ext.txt` |
| `-z` | `<millisecs>` | إضافة تأخير بين الطلبات | `dirb http://target -z 250` |

---

## 💡 النصائح وأفضل الممارسات

1. **احترس من ردود Wildcard 200/302:**
   - تقوم بعض تطبيقات الويب بإرجاع HTTP 200 أو 302 لكل طلب (معالج 404 مخصص). سيقوم DIRB بالإبلاغ عن كل إدخال تم العثور عليه. استخدم `-N 302` أو قم بالتبديل إلى `ffuf` / `feroxbuster` للتصفية المتقدمة للحجم/الكلمات.
2. **ادمجها مع `-r` للقواميس الكبيرة:**
   - في حالة استخدام `directory-list-2.3-medium.txt` (220 ألف كلمة)، قم دائمًا بتعطيل التكرار باستخدام `-r`. بخلاف ذلك، قد يستغرق DIRB أيامًا للزحف إلى الدلائل المتداخلة بشدة.
3. **تحقق دائمًا من الإضافات:**
   - تعرض الواجهات الخلفية الحديثة بشكل متكرر الملفات الحساسة مثل `.env` أو `.git` أو `.bak` أو `.conf` أو `.sql`. اختبر دائمًا الامتدادات الشائعة مع `-X`.
4. **استخدم `-w` لعمليات الفحص غير المراقبة:**
   - افتراضيًا، قد يتوقف DIRB مؤقتًا إذا واجه تحذيرات SSL أو الشبكة. يضمن اجتياز `-w` اكتمال الاختبارات دون تدخل يدوي.

---

## ⚠️إخلاء المسؤولية القانونية

> **هام:** تم تصميم DIRB للتدقيق الأمني واختبار الاختراق والبحث الأخلاقي على الأنظمة التي تمتلكها أو لديك إذن صريح وموثق للاختبار. يمكن أن تتسبب هجمات القاموس غير المصرح بها ضد خوادم الويب في تعطيل الخدمة وهي غير قانونية بموجب تشريعات جرائم الكمبيوتر في جميع أنحاء العالم.

---

## 📚 المصادر

- [مشروع DIRB SourceForge](https://sourceforge.net/projects/dirb/)
- [وثائق حزمة Kali Linux DIRB](https://www.kali.org/tools/dirb/)
- [SecLists - قوائم الكلمات الخاصة باكتشاف محتوى الويب](https://github.com/danielmiessler/SecLists)