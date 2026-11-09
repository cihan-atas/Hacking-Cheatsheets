<!-- language-home-navigation -->
[🏠 الصفحة الرئيسية العربية](../../README.ar.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 📡 dnsx - ورقة الغش الكاملة

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ██████╗ ███╗   ██╗███████╗██╗  ██╗
  ██╔══██╗████╗  ██║██╔════╝╚██╗██╔╝
  ██║  ██║██╔██╗ ██║███████╗ ╚███╔╝ 
  ██║  ██║██║╚██╗██║╚════██║ ██╔██╗ 
  ██████╔╝██║ ╚████║███████║██╔╝ ██╗
  ╚═════╝ ╚═╝  ╚═══╝╚══════╝╚═╝  ╚═╝
       Fast & Multi-Purpose DNS Toolkit by ProjectDiscovery
```
<p align="center">
  <img src="https://img.shields.io/badge/dnsx-DNS_Toolkit-blue?style=for-the-badge" alt="dnsx">
  <img src="https://img.shields.io/badge/Developer-ProjectDiscovery-orange?style=for-the-badge" alt="ProjectDiscovery">
  <img src="https://img.shields.io/badge/Category-Recon_&_Scanning-green?style=for-the-badge" alt="Recon & Scanning">
  <img src="https://img.shields.io/badge/License-MIT-red?style=for-the-badge" alt="License MIT">
</ص>

<p align="center">
  <b>🌐 دقة DNS عالية الأداء، وتصفية أحرف البدل، وعمليات البحث العكسي لنظام أسماء النطاقات، ومحرك استخراج الاستعلامات المتعددة</b>
</ص>

---

## 📋 جدول المحتويات

- [نظرة عامة والهندسة المعمارية](#-نظرة عامة--الهندسة المعمارية)
- [لماذا DNSX؟ (معالجة أحرف البدل وسرعتها)](#-لماذا-dnsx-معالجة أحرف البدل--السرعة)
- [التثبيت](#-التثبيت)
- [فحص النطاق الفرعي الأساسي وحله](#-basic-subdomain-probing--resolution)
- [تصفية DNS لأحرف البدل (`-wd`)](#-wildcard-dns-filtering---wd)
- [الاستعلام عن أنواع سجلات DNS المحددة](#-الاستعلام عن أنواع سجلات DNS المحددة)
- [عمليات بحث DNS العكسية (`-ptr`)](#-reverse-dns-lookups---ptr)
- [استخدام الحلول المخصصة وتحديد المعدل](#-استخدام-الحلول المخصصة--تحديد المعدل)
- [أتمتة خطوط الأنابيب (Subfinder + dnsx + httpx)](#-pipeline-automation-subfinder--dnsx--httpx)
- [الجدول المرجعي السريع](#-الجدول المرجعي السريع)
- [الاعتبارات الدفاعية وOPSEC](#-الاعتبارات الدفاعية--opsec)
- [إخلاء المسؤولية القانونية](#-إخلاء المسؤولية القانونية)

---

## 🎯 نظرة عامة والهندسة المعمارية

تم تصميمه في Go بواسطة **ProjectDiscovery**، **dnsx** عبارة عن مجموعة أدوات DNS سريعة ومتعددة الأغراض مصممة للاستطلاع وصيد الأخطاء واكتشاف الشبكة.

في حين أن أدوات مثل `dig`، و`nslookup`، و`host` تحل السجلات واحدًا تلو الآخر، فإن `dnsx` تعالج آلاف الاستعلامات بشكل غير متزامن، وتعيد محاولة أدوات الحل الفاشلة تلقائيًا، وتستخرج عناوين IP للاستجابة النظيفة، وتزيل ضوضاء أحرف البدل التي تلوث تعداد النطاق الفرعي.

---

## ⚡ لماذا dnsx؟ (التعامل مع أحرف البدل والسرعة)

أثناء استطلاع مكافآت الأخطاء، غالبًا ما تقوم الأدوات السلبية (`subfinder`، `amass`) بإرجاع آلاف النطاقات الفرعية الميتة أو أحرف البدل. يؤدي اختبار نقاط نهاية HTTP على إدخالات DNS الميتة إلى إهدار نطاق ترددي هائل.

`dnsx` يحل هذه المشكلة عن طريق:
1. **تصفية أحرف البدل (`-wd`):** تكتشف ما إذا كان `*.domain.com` يتحول إلى عنوان IP شامل ويقوم بتصفية النطاقات الفرعية الوهمية.
2. **السرعة والتزامن:** يحل أكثر من 10000 نطاق في الدقيقة عبر مجموعات المحللين العامة والخاصة.
3. ** تنظيف أنابيب الإخراج: ** يتم تغذيتها مباشرة إلى `httpx` أو `nmap`.

---

## 📥 التثبيت
```bash
# Go Install (Recommended)
go install -v github.com/projectdiscovery/dnsx/cmd/dnsx@latest

# Kali Linux & Debian
sudo apt update && sudo apt install -y dnsx

# MacOS (Homebrew)
brew install dnsx

# Verify installation
dnsx -version
```
---

## 🚀 فحص النطاق الفرعي الأساسي وحله
```bash
# Resolve a single domain
echo "target.com" | dnsx

# Resolve a list of subdomains from file
dnsx -l subdomains.txt

# Display domain and its corresponding A record IP
dnsx -l subdomains.txt -a -resp

# Output only resolved IP addresses (ready for Nmap)
dnsx -l subdomains.txt -resp-only
```
---

## 🚫 تصفية DNS الخاصة بأحرف البدل (`-wd`)

تقوم أحرف البدل بإرجاع عنوان IP صالح للنطاقات الفرعية غير الموجودة (`random123xyz.target.com`)، مما يؤدي إلى تسميم مجموعات بيانات الاستطلاع.
```bash
# Filter wildcard subdomains and print only genuine hosts
dnsx -l subdomains.txt -wd target.com -o valid_hosts.txt

# Filter wildcards with custom threshold
dnsx -l subdomains.txt -wd target.com -wt 3
```
---

## 📋 الاستعلام عن أنواع محددة من سجلات DNS

الاستعلام عن أنواع سجلات محددة عبر آلاف المجالات:
```bash
# Query CNAME records (Crucial for Subdomain Takeover detection)
dnsx -l subdomains.txt -cname -resp

# Query Mail Exchanger (MX) records
dnsx -l domains.txt -mx -resp

# Query TXT records (SPF, DKIM, verification tokens)
dnsx -l domains.txt -txt -resp

# Query All standard records (A, AAAA, CNAME, NS, TXT, MX, SOA)
dnsx -l domains.txt -recon
```
---

## 🔄 عمليات بحث DNS العكسية (`-ptr`)

اكتشف البنية التحتية النشطة والمضيفين الظاهريين من خلال إجراء عمليات بحث PTR على نطاق IP:
```bash
# Reverse resolve a CIDR IP range to hostnames
dnsx -ptr -l ip_ranges.txt -resp

# Reverse resolve a single subnet
echo "192.168.1.0/24" | dnsx -ptr -resp
```
---

## ⚙️ استخدام أدوات الحل المخصصة وتحديد المعدل

تجنب أن تكون محدودًا بالمعدل بواسطة مزود خدمة الإنترنت أو Google DNS الخاص بك عن طريق استخدام مجموعة محللات منسقة عالية السرعة:
```bash
# Use a custom trusted resolver list
dnsx -l subdomains.txt -r resolvers.txt

# Set thread concurrency and query rate limit (queries per second)
dnsx -l subdomains.txt -t 100 -rl 300
```
---

## ⛓️ أتمتة خطوط الأنابيب (Subfinder + dnsx + httpx)

سير عمل الاستطلاع النهائي لصائدي مكافآت الأخطاء ومختبري الاختراق:
```bash
# Discover subdomains -> Validate via dnsx -> Probe live HTTP web services
subfinder -d target.com -silent | \
  dnsx -silent -wd target.com | \
  httpx -silent -status-code -title -tech-detect
```
---

## ⚡ جدول مرجعي سريع

| الهدف | الأمر |
|-----------|--------|
| ** حل النطاقات الفرعية ** | `dnsx -l subdomains.txt` |
| **استخراج عناوين IP فقط** | `dnsx -l subdomains.txt -resp-only` |
| ** تصفية مجالات البدل ** | `dnsx -l subdomains.txt -wd <DOMAIN>` |
| **استخراج CNAME (الاستحواذ)** | `dnsx -l subdomains.txt -cname -resp` |
| ** الاستعلام عن كافة السجلات (ريكون) ** | `dnsx -l domains.txt -recon` |
| **البحث العكسي عن DNS (PTR)** | `dnsx -l ips.txt -ptr -resp` |
| ** الحلول والمواضيع المخصصة **| `dnsx -l list.txt -r resolvers.txt -t 150` |
| ** إخراج JSON ** | `dnsx -l list.txt -json -o results.json` |

---

## 🛡️ الاعتبارات الدفاعية وOPSEC

1. **إغراق نظام أسماء النطاقات:** سيؤدي إطلاق عشرات الآلاف من استعلامات DNS من خلال خوادم DNS الداخلية للشركة إلى زيادة أحمال وحدة المعالجة المركزية وإطلاق تنبيهات SIEM لنفق DNS/DDoS.
2. **تسمم المحلل:** استخدم دائمًا قوائم المحللين الموثوقة والموثقة لمنع برامج الحل الضارة من اعتراض الاستعلامات.

---

## ⚠️إخلاء المسؤولية القانونية

> [!تنبيه]
> ورقة الغش هذه مخصصة لاختبارات الاختراق المعتمدة وارتباطات مكافآت الأخطاء فقط. يُحظر تمامًا إجراء تحقيقات DNS كبيرة الحجم ضد البنية التحتية غير المصرح بها.