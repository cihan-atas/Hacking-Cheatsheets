<!-- language-home-navigation -->
[🏠 الصفحة الرئيسية العربية](../../README.ar.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🦀 Feroxbuster - ورقة الغش الكاملة

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ███████╗███████╗██████╗  ██████╗ ██╗  ██╗██████╗ ██╗   ██╗███████╗████████╗███████╗██████╗ 
  ██╔════╝██╔════╝██╔══██╗██╔═══██╗╚██╗██╔╝██╔══██╗██║   ██║██╔════╝╚══██╔══╝██╔════╝██╔══██╗
  █████╗  █████╗  ██████╔╝██║   ██║ ╚███╔╝ ██████╔╝██║   ██║███████╗   ██║   █████╗  ██████╔╝
  ██╔══╝  ██╔══╝  ██╔══██╗██║   ██║ ██╔██╗ ██╔══██╗██║   ██║╚════██║   ██║   ██╔══╝  ██╔══██╗
  ██║     ███████╗██║  ██║╚██████╔╝██╔╝ ██╗██████╔╝╚██████╔╝███████║   ██║   ███████╗██║  ██║
  ╚═╝     ╚══════╝╚═╝  ╚═╝ ╚═════╝ ╚═╝  ╚═╝╚═════╝  ╚═════╝ ╚══════╝   ╚═╝   ╚══════╝╚═╝  ╚═╝
                Fast, Simple, Recursive Content Discovery
```
<p align="center">
  <img src="https://img.shields.io/badge/Feroxbuster-Content_Discovery-orange?style=for-the-badge" alt="Feroxbuster">
  <img src="https://img.shields.io/badge/Rust-Blazingly_Fast-blue?style=for-the-badge" alt="Rust">
  <img src="https://img.shields.io/badge/Auto_Tune-Recursive-green?style=for-the-badge" alt="Recursive">
  <img src="https://img.shields.io/badge/Bug_Bounty-Web_Pentest-red?style=for-the-badge" alt="Web Pentest">
</ص>

<p align="center">
  <b>⚡ أداة سريعة وبسيطة ومتكررة لاكتشاف المحتوى مكتوبة بلغة Rust</b>
</ص>

---

## 📋 جدول المحتويات

- [المقدمة](#-المقدمة)
- [الميزات الرئيسية](#-الميزات الرئيسية)
- [مقارنة الأدوات](#-مقارنة الأدوات)
- [التثبيت](#-التثبيت)
- [بناء الجملة الأساسي والبدء السريع](#-بناء الجملة الأساسي--البدء السريع)
- [خيارات الأوامر الأساسية](#-core-command-options)
  - [التحكم في العودية](#التحكم في العودية)
  - [التزامن والأداء](#التزامن--الأداء)
  - [امتدادات الملفات](#file-extensions)
  - [التصفية والمطابقة](#filtering--matching)
  - [الميزات الذكية (الضبط التلقائي، استخراج الارتباط، النسخ الاحتياطية)](#smart-features)
  - [العناوين وملفات تعريف الارتباط والمصادقة](#headers-cookies--auth)
  - [تكامل Proxy & Burp Suite](#proxy--burp-suite-integration)
- [المسح متعدد الأهداف وخطوط الأنابيب](#-مسح الأهداف المتعددة--خطوط الأنابيب)
- [سير العمل في العالم الحقيقي](#-سير العمل في العالم الحقيقي)
- [الجدول المرجعي السريع](#-الجدول المرجعي السريع)
- [النصائح وأفضل الممارسات](#-نصائح--أفضل-الممارسات)
- [إخلاء المسؤولية القانونية](#-إخلاء المسؤولية القانونية)
- [الموارد](#-الموارد)

---

## 🎯 المقدمة

**Feroxbuster** عبارة عن ماسح ضوئي متكرر لمحتوى الويب عالي الأداء ومتعدد الخيوط ومكتوب بلغة Rust. تم تصميمه ليكون سريعًا وبسيطًا وبديهيًا، فهو يجمع بين سرعة أجهزة الضباب المستندة إلى Go/Rust مع اكتشاف الدليل التكراري التلقائي الأصلي.

إنه يتألق أثناء اختبارات الاختراق وارتباطات مكافآت الأخطاء حيث يجب اكتشاف أشجار مواقع الويب العميقة والأدلة المخفية بسرعة دون إنتاج نصوص يدوية معقدة.

---

## 🌟 الميزات الرئيسية

- ⚡ **سريع للغاية:** مدعوم من Rust غير المتزامن (`tokio` و`reqwest`)، وهو قادر على تلبية آلاف الطلبات في الثانية.
- 🔄 **الاكتشاف العودي الأصلي:** يكتشف تلقائيًا الأدلة الجديدة ويفحصها بشكل متكرر حتى حدود العمق القابلة للتكوين.
- 🧠 **الضبط التلقائي والكفالة التلقائية:** يخنق سرعة المسح تلقائيًا إذا تباطأ خادم الويب المستهدف أو تم إحباطه في حالة حدوث أخطاء مفرطة في الاتصال.
- 🔗 **استخراج الارتباط المضمن:** يوزع استجابات HTML وJavaScript لاكتشاف نقاط النهاية المخفية وإعادتها إلى قائمة انتظار الفحص.
- 📦 **البحث الآلي عن النسخ الاحتياطية:** يقوم بإلحاق أشكال `.bak` و`~` و`.old` و`.orig` تلقائيًا بالملفات المكتشفة.
- 🎯 **Pipeable Input:** يقبل بسهولة قوائم الأهداف من `stdin` (من أدوات مثل `subfinder` أو `httpx` أو `gau`).

---

## ⚖️ مقارنة الأدوات

| ميزة | فيروكسبوستر | ففوف | ديرسيرتش | جوبستر | درب |
|---------|-------------|------|-----------|----------|------|
| **اللغة** | الصدأ | اذهب | بايثون | اذهب | ج |
| **السرعة** | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐ |
| ** العودية الافتراضية ** | ✅ اوتوماتيك | ❌ علم يدوي | ✅ أوتوماتيك (`-r`) | ❌ لا يوجد | ✅ اوتوماتيك |
| **استخراج الرابط** | ✅ نعم (`--extract-links`) | ❌ لا | ❌ لا | ❌ لا | ❌ لا |
| **ضبط المعدل التلقائي** | ✅ نعم (`--auto-tune`) | ❌ لا | ❌ لا | ❌ لا | ❌ لا |
| **ما بعد التشويش** | ❌ لا | ✅ أصلي | ✅ أصلي (`--data`) | ❌ لا | ❌ لا |
| ** معيار متعدد الأهداف ** | ✅ أصلي | ❌ محدودة | ✅ نعم (`-l`) | ❌ لا | ❌ لا |
| ** الكشف عن ملف النسخ الاحتياطي **| ✅ أصلي (`--collect-backups`)| ❌ لا | ✅ أصلي (`-e *`) | ❌ لا | ❌ لا |

---

## 📥 التثبيت

### كالي لينكس وديبيان/أوبونتو
```bash
# Update repository and install
sudo apt update && sudo apt install -y feroxbuster
```
### استخدام البضائع (مدير حزم الصدأ)
```bash
cargo install feroxbuster
```
### ماك (الجعة المنزلية)
```bash
brew install feroxbuster
```
### عامل الميناء
```bash
docker run --init -it epi052/feroxbuster -u https://target.com -w /usr/share/seclists/Discovery/Web-Content/common.txt
```
### الملف الثنائي المترجم مسبقًا (إصدارات GitHub)
```bash
# Download latest Linux release
curl -sL https://raw.githubusercontent.com/epi052/feroxbuster/main/install-nix.sh | bash
sudo mv feroxbuster /usr/local/bin/
```
---

## 🚀 بناء الجملة الأساسي والبدء السريع
```bash
feroxbuster -u <TARGET_URL> -w <WORDLIST> [OPTIONS]
```
### أمثلة للفحص السريع
```bash
# Scan a single target with default settings
feroxbuster -u http://10.10.10.10

# Scan with custom SecLists directory list
feroxbuster -u https://example.com -w /usr/share/seclists/Discovery/Web-Content/directory-list-2.3-medium.txt

# Scan target including specific file extensions
feroxbuster -u https://example.com -w /usr/share/wordlists/dirb/common.txt -x php,html,txt,json
```
---

## ⚙️ خيارات الأوامر الأساسية

### التحكم في التكرار
```bash
# Specify maximum recursion depth (default is 4)
feroxbuster -u https://example.com -w common.txt -d 2

# Completely disable recursion (scan root directory only)
feroxbuster -u https://example.com -w common.txt -n

# Don't scan nested subdirectories automatically (interactive prompt)
feroxbuster -u https://example.com -w common.txt --dont-scan /api,/static
```
### التزامن والأداء
```bash
# Set number of concurrent threads per directory (default: 50)
feroxbuster -u https://example.com -t 100

# Limit total concurrent directory scans running simultaneously (default: unlimited)
feroxbuster -u https://example.com -L 3

# Rate limit requests per second to avoid triggering WAF/firewall blocks
feroxbuster -u https://example.com --rate-limit 150

# Request timeout in seconds (default: 7)
feroxbuster -u https://example.com -T 5
```
### امتدادات الملفات
```bash
# Append extensions to each wordlist item
feroxbuster -u https://example.com -x php,asp,aspx,jsp,txt,bak

# Automatically collect and fuzz file extensions found during crawling
feroxbuster -u https://example.com --collect-extensions
```
### التصفية والمطابقة
```bash
# Match only specific status codes (e.g., 200 OK and 301 Redirects)
feroxbuster -u https://example.com -s 200 301 302

# Filter out specific status codes (e.g., exclude 404 and 403)
feroxbuster -u https://example.com -C 404 403 500

# Filter responses matching exact size in bytes
feroxbuster -u https://example.com -S 4092 1024

# Filter responses matching exact word count
feroxbuster -u https://example.com -W 42

# Filter responses matching exact line count
feroxbuster -u https://example.com -N 12

# Filter responses matching regular expressions (e.g., generic error pages)
feroxbuster -u https://example.com --filter-regex "Under Construction|Page Not Found"

# Filter responses similar to a known 404/wildcard page
feroxbuster -u https://example.com --filter-similar-to https://example.com/definitelynotfoundpage404
```
### الميزات الذكية
```bash
# Auto-tune: Automatically adjusts request concurrency based on server response latency
feroxbuster -u https://example.com --auto-tune

# Auto-bail: Aborts scanning automatically when more than 100 consecutive network errors occur
feroxbuster -u https://example.com --auto-bail

# Extract links: Parses HTML and JavaScript to automatically uncover endpoints
feroxbuster -u https://example.com --extract-links

# Collect backups: Automatically tests for backup copies (.bak, ~, .old) of every discovered file
feroxbuster -u https://example.com --collect-backups
```
### الرؤوس وملفات تعريف الارتباط والمصادقة
```bash
# Custom HTTP Header
feroxbuster -u https://example.com -H "Authorization: Bearer <TOKEN>" -H "X-Forwarded-For: 127.0.0.1"

# Session Cookie
feroxbuster -u https://example.com -b "session=abc123xyz; role=admin"

# Custom User-Agent string
feroxbuster -u https://example.com -a "Mozilla/5.0 (Windows NT 10.0; Win64; x64)"

# Ignore invalid / self-signed TLS certificates
feroxbuster -u https://10.10.10.10 -k
```
### تكامل Proxy & Burp Suite
```bash
# Route all requests through Burp Suite proxy
feroxbuster -u https://example.com -p http://127.0.0.1:8080 -k

# Convenience shortcut flag for Burp Suite (enables -k and sets proxy to localhost:8080)
feroxbuster -u https://example.com --burp
```
---

## 🔁 المسح متعدد الأهداف وخطوط الأنابيب

يقرأ Feroxbuster عناوين URL المستهدفة أصلاً من الإدخال القياسي (`stdin`)، مما يجعله مثاليًا للتكامل مع مسارات اكتشاف الأصول:
```bash
# Pipeline: Subdomain discovery -> HTTP Probing -> Feroxbuster Fuzzing
subfinder -d example.com -silent | \
  httpx -silent -mc 200,301,302 | \
  feroxbuster --stdin -w /usr/share/seclists/Discovery/Web-Content/raft-medium-directories.txt -t 30 -L 4
```
### فحص عناوين URL من ملف
```bash
cat targets.txt | feroxbuster --stdin -w common.txt -v
```
---

## 🎬 سير العمل في العالم الحقيقي

### سير العمل 1: استطلاع الويب الشامل لمكافأة الأخطاء

التزامن العالي، وضبط المعدل التلقائي، واستخراج الارتباط، والكشف عن النسخ الاحتياطي:
```bash
feroxbuster -u https://target.com \
  -w /usr/share/seclists/Discovery/Web-Content/directory-list-2.3-medium.txt \
  -x php,js,json,txt,html \
  --extract-links \
  --collect-backups \
  --auto-tune \
  -o ferox_target_scan.txt
```
### سير العمل 2: اكتشاف بوابة المؤسسة / Active Directory الداخلية

مسح تطبيق ويب داخلي عبر Burp Suite مع المصادقة والشهادات الموقعة ذاتيًا:
```bash
feroxbuster -u https://internal-portal.corp.local \
  -w /usr/share/seclists/Discovery/Web-Content/common.txt \
  -x aspx,ashx,asmx,config,txt \
  -b "ASP.NET_SessionId=k2m9fl3p8..." \
  --burp \
  -d 3
```
### سير العمل 3: واجهة برمجة تطبيقات سريعة غير متكررة واكتشاف نقطة النهاية
```bash
feroxbuster -u https://api.target.com/v1 \
  -w /usr/share/seclists/Discovery/Web-Content/api/api-endpoints.txt \
  -n \
  -s 200,201,204,401,403 \
  -t 100 \
  --json \
  -o api_results.json
```
---

## 📊 جدول مرجعي سريع

| علم | قصير | الوصف | مثال |
|------|-------|------------|---------|
| `--url` | `-u` | عنوان URL المستهدف للمسح | `feroxbuster -u https://target.com` |
| `--wordlist` | `-w` | المسار إلى قائمة الكلمات | `feroxbuster -w /path/to/list.txt` |
| `--threads` | `-t` | عدد المواضيع المتزامنة لكل دليل | `feroxbuster -t 80` |
| `--scan-limit` | `-L` | الحد من إجمالي عمليات فحص الدليل المتزامنة | `feroxbuster -L 3` |
| `--depth` | `-d` | الحد الأقصى لعمق العودية (الافتراضي 4) | `feroxbuster -d 2` |
| `--no-recursion` | `-n` | تعطيل فحص الدليل العودي | `feroxbuster -n` |
| `--extensions` | `-x` | امتدادات الملفات لإلحاق | `feroxbuster -x php,html,bak` |
| `--status-codes` | `-s` | رموز الحالة المراد تضمينها | `feroxbuster -s 200 301 302` |
| `--filter-status` | `-C` | رموز الحالة المراد استبعادها | `feroxbuster -C 404 500` |
| `--filter-size` | `-S` | أحجام الاستجابة المراد استبعادها | `feroxbuster -S 4096` |
| `--filter-words` | `-W` | عدد الكلمات المطلوب استبعاده | `feroxbuster -W 15` |
| `--filter-lines` | `-N` | عدد الأسطر المطلوب استبعاده | `feroxbuster -N 8` |
| `--auto-tune` | لا شيء | ضبط السرعة ديناميكيًا بناءً على زمن الوصول | `feroxbuster --auto-tune` |
| `--auto-bail` | لا شيء | إحباط الفحص على أخطاء الشبكة الزائدة | `feroxbuster --auto-bail` |
| `--extract-links`| لا شيء | تحليل HTML/JS لنقاط النهاية الجديدة | `feroxbuster --extract-links` |
| `--collect-backups` | لا شيء | التحقق من وجود اختلافات النسخ الاحتياطي (`.bak`، `.old`) | `feroxbuster --collect-backups` |
| `--proxy` | `-p` | إرسال حركة المرور من خلال وكيل HTTP | `feroxbuster -p http://127.0.0.1:8080` |
| `--burp` | لا شيء | اختصار `-p http://127.0.0.1:8080 -k` | `feroxbuster --burp` |
| `--stdin` | لا شيء | قراءة عناوين URL من الإدخال القياسي | `cat urls.txt \| feroxbuster --stdin` |
| `--output` | `-o` | مسار ملف الإخراج | `feroxbuster -o results.txt` |
| `--json` | لا شيء | تنسيق الإخراج كـ JSON | `feroxbuster --json -o out.json` |

---

## 💡 النصائح وأفضل الممارسات

1. **استخدم `--auto-tune` للأهداف البعيدة:**
   - بدلاً من تخمين عدد الخيوط (`-t`)، يقوم `--auto-tune` بقياس أوقات الاستجابة المستهدفة بشكل فعال ويخنق أو يسرع ديناميكيًا، مع تجنب رفض الخادم غير المقصود للخدمة.
2. **ادمج `--extract-links` و`--collect-backups`:**
   - هذا المزيج قاتل بشكل استثنائي في تقييمات مكافآت الأخطاء: يستخرج Feroxbuster مسارات JS المخفية، ويفحصها، ويتحقق على الفور من ترك النسخ الاحتياطية للمطورين (`.bak`، و`.swp`، و`~`) على الخادم.
3. **الحد من عمليات المسح على قوائم الكلمات الكبيرة باستخدام `-L`:**
   - في حالة استخدام قاموس يضم أكثر من 200000 كلمة، يمكن أن يؤدي التكرار غير المقيد إلى تشغيل العشرات من سلاسل الرسائل المتزامنة. استخدم `-L 2` أو `-L 3` للاحتفاظ بالذاكرة وعرض النطاق الترددي ضمن حدود التشغيل الآمنة.
4. **استخدم ملفات الحالة لاستئناف عمليات التدقيق الكبيرة:**
   - يقوم Feroxbuster بحفظ ملفات الحالة (`.state`) بشكل دوري. في حالة حدوث مقاطعة في الفحص، استأنف الفحص على الفور باستخدام `feroxbuster --resume-from <FILE>.state`.

---

## ⚠️إخلاء المسؤولية القانونية

> **هام:** تم تصميم Feroxbuster بشكل صارم للتقييمات الأمنية المعتمدة ومسابقات CTF واختبارات الاختراق التي يتم إجراؤها بإذن كتابي. يمكن أن يؤدي فحص الأهداف غير المصرح بها إلى إشباع البنية التحتية للشبكة وهو أمر محظور تمامًا بموجب القانون.

---

## 📚 المصادر

- [مستودع Feroxbuster GitHub](https://github.com/epi052/feroxbuster)
- [وثائق Feroxbuster الرسمية](https://epi052.github.io/feroxbuster-docs/)
- [قوائم اكتشاف SecLists](https://github.com/danielmiessler/SecLists)