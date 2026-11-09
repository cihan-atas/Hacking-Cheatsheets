<!-- language-home-navigation -->
[🏠 الصفحة الرئيسية العربية](../../README.ar.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ورقة الغش Searchsploit

[Searchsploit](https://www.exploit-db.com/searchsploit) هي الأداة المساعدة الرسمية لسطر الأوامر لـ [Exploit-Database](https://www.exploit-db.com/). فهو يسمح لمحترفي الأمن، واختبار الاختراق، والباحثين عن الثغرات الأمنية بإجراء عمليات بحث دون الاتصال بالإنترنت عبر عشرات الآلاف من عمليات الاستغلال المنشورة، وأكواد القشرة، والمستندات البيضاء دون إرسال حركة مرور الشبكة إلى محركات بحث تابعة لجهات خارجية.

---

## جدول المحتويات

- [نظرة عامة والهندسة المعمارية](#overview--architecture)
- [التثبيت وتحديثات قاعدة البيانات المحلية](#installation--local-database-updates)
- [البحث الأساسي والمتقدم عن برمجيات استغلال الثغرات](#basic--advanced-exploit-searching)
- [فحص ونسخ برمجيات إكسبلويت (`-m` / `-p` / `-x`)](#examing--copying-exploits--m---p---x)
- [التصفية حسب النظام الأساسي والنوع](#filtering-by-platform--type)
- [Nmap XML / تكامل المسح Grepable](#nmap-xml--grepable-scanning-integration)
- [إخراج JSON وأتمتة البرنامج النصي](#json-output--script-automation)
- [أفضل الممارسات في اختبار الاختراق](#أفضل-الممارسات-في-اختبار-الاختراق)

---

## نظرة عامة والهندسة المعمارية

يستعلم Searchsploit عن نسخة git محلية من Exploit-DB المخزنة في `/usr/share/exploitdb/`.
- ** بصمة الشبكة الصفرية: ** يتم إجراء عمليات البحث على هياكل الدليل وملف CSV المحلي.
- **الموثوقية في وضع عدم الاتصال:** تعمل بكامل طاقتها في البيئات المستهدفة وشبكات المشاركة ذات الفجوات الهوائية.
- **النسخ المتطابق المتكامل:** نسخ الملفات المستغلة مباشرة إلى دليل العمل الحالي للمراجعة والتعديل الفوري.

---

## التثبيت وتحديثات قاعدة البيانات المحلية

### ديبيان / أوبونتو / كالي لينكس / نظام تشغيل باروت
```bash
# Kali and Parrot include searchsploit by default
sudo apt-get update && sudo apt-get install -y exploitdb

# Update the local exploit database archive
searchsploit -u
```
### تثبيت مستودع Git (أي توزيعة Linux)
```bash
git clone https://gitlab.com/exploit-database/exploitdb.git /opt/exploitdb
sudo ln -sf /opt/exploitdb/searchsploit /usr/local/bin/searchsploit

# Check version and status
searchsploit -v
```
---

## البحث الأساسي والمتقدم عن الثغرات

### 1. بحث بسيط عن الكلمات الرئيسية
ابحث عن خدمات أو شياطين أو برامج محددة:
```bash
searchsploit apache 2.4.49
searchsploit openssh 7.2
searchsploit wordpress core 5.8
```
### 2. المطابقة الدقيقة للعناوين (`-t`)
قم بتقييد استعلامات البحث بشكل صارم لاستغلال العناوين لإزالة مطابقات الوصف المزعجة:
```bash
searchsploit -t "Privilege Escalation" linux kernel
searchsploit -t "Remote Code Execution" smb
```
### 3. مطابقة صارمة للحالة والمصطلحات (`-e`)
ابحث عن النسخة الدقيقة وتطابقات النمط:
```bash
searchsploit -e "Apache Struts"
```
### 4. البحث عن طريق معرف CVE
تم تعيين عمليات استغلال الاستعلام لنقاط ضعف وأرقام تعرض محددة:
```bash
searchsploit --cve 2021-44228
searchsploit --cve 2017-0144
```
### 5. استبعاد النتائج غير المرغوب فيها (`--exclude`)
قم بتصفية رفض الخدمة (DoS) أو البرامج النصية المساعدة عند طلب تنفيذ التعليمات البرمجية عن بعد (RCE):
```bash
searchsploit linux kernel 5.4 --exclude="Denial of Service"
```
---

## فحص ونسخ برمجيات إكسبلويت (`-m` / `-p` / `-x`)

لا تقم أبدًا بتحرير أو تنفيذ عمليات استغلال مباشرة في دليل `/usr/share/exploitdb/`. استخدم نسخة Searchsploit وفحص الوظائف.

### 1. انسخ (مرآة) برنامج إكسبلويت إلى الدليل الحالي (`-m`)
انسخ ملف استغلال PoC (Python، C، Ruby، bash) مباشرة إلى دليل العمل المحلي الخاص بك:
```bash
# Mirror by Exploit-DB ID (EBD-ID)
searchsploit -m 50383

# Mirror by search path pattern
searchsploit -m windows/remote/48537.py
```
### 2. فحص بيانات تعريف الاستغلال والمسار الكامل (`-p`)
اعرض المسار المطلق لنظام الملفات وعنوان URL الخاص بـ Exploit-DB لمعرف EDB:
```bash
searchsploit -p 50383
```
الإخراج:
```text
  Exploit: Apache HTTP Server 2.4.49 - Path Traversal & Remote Code Execution (RCE)
      URL: https://www.exploit-db.com/exploits/50383
     Path: /usr/share/exploitdb/exploits/multiple/webapps/50383.sh
File Type: Bourne-Again shell script, ASCII text executable
```
### 3. قراءة كود الاستغلال في جهاز النداء الطرفي (`-x`)
افتح كود الاستغلال مباشرة في `less` للفحص السريع:
```bash
searchsploit -x 50383
```
---

## التصفية حسب النظام الأساسي والنوع

يقوم Searchsploit بفهرسة عمليات الاستغلال وكود القشرة والأوراق البحثية عبر بنيات متعددة:

### 1. التصفية حسب نظام التشغيل المستهدف
```bash
# Search only Linux exploits
searchsploit linux kernel 4.4

# Search only Windows exploits
searchsploit windows smb eternal

# Search web applications
searchsploit webapps tomcat
```
### 2. مرشح لأكواد القشرة الأولية فقط (`-s`)
ابحث عن حمولات كود القشرة الخاصة بالبنية (على سبيل المثال، execve `/bin/sh` بدون بايتات فارغة):
```bash
searchsploit -s linux/x64 "execve"
searchsploit -s windows/x86 "reverse shell"
```
### 3. مرشح لأوراق الأمان والمستندات التقنية (`-w`)
```bash
searchsploit -w "Active Directory Kerberos"
```
---

## تكامل المسح Nmap XML / Grepable

يمكن لـ Searchsploit تحليل مخرجات فحص Nmap XML تلقائيًا ومطابقة إصدارات البرامج المكتشفة مع عمليات الاستغلال المعروفة:
```bash
# 1. Perform service detection and save XML output
nmap -sV -p- -T4 -oX /tmp/target_scan.xml 192.168.1.50

# 2. Feed the XML scan file directly to Searchsploit
searchsploit --nmap /tmp/target_scan.xml
```
*يقوم Searchsploit بفحص كل شعار منفذ مفتوح وسلسلة إصدار، والاستعلام عن عمليات استغلال محلية للفرز الفوري.*

---

## إخراج JSON وأتمتة البرنامج النصي

بالنسبة لأتمتة خطوط الأنابيب وأدوات الاستطلاع المخصصة، ينتج عن الإخراج تنسيق JSON منظم:
```bash
searchsploit "Apache Struts" --json > struts_exploits.json
```
تصفية مسارات ملفات الاستغلال باستخدام `jq`:
```bash
searchsploit --cve 2021-41773 --json | jq -r '.RESULTS_EXPLOIT[].Path'
```
---

## أفضل الممارسات في اختبار الاختراق

1. **التحقق قبل التشغيل:** افحص دائمًا كود مصدر الاستغلال (`searchsploit -x <id>`) قبل التجميع أو التنفيذ. تحقق من كود القشرة المشفر، وعناوين IP المستهدفة، وتأكد من عدم وجود أبواب خلفية أو أجهزة تنظيم حمولة ضارة.
2. **التجميع باستخدام العلامات الصحيحة:** تتطلب العديد من عمليات استغلال Exploit-DB C علامات دول مجلس التعاون الخليجي المحددة (على سبيل المثال، `-m32`، أو `-lpthread`، أو `-lcrypto`):
   ```bash
   searchsploit -m 40839
   gcc -pthread 40839.c -o dirtycow -lcrypt
   ```
3. **حافظ على تحديث قاعدة البيانات:** قم بتشغيل `searchsploit -u` بانتظام لضمان فهرسة الأيام الصفرية وإثبات المفاهيم المنشورة حديثًا محليًا.