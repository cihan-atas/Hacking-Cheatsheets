<!-- language-home-navigation -->
[🏠 الصفحة الرئيسية العربية](../../README.ar.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ورقة الغش شيرلوك

[Sherlock](https://github.com/sherlock-project/sherlock) هي أداة استطلاع OSINT التي تتعقب حسابات الوسائط الاجتماعية عن طريق اسم المستخدم عبر أكثر من 400 منصة اجتماعية ومنتديات ومواقع استضافة الأكواد والمجتمعات عبر الإنترنت في وقت واحد.

---

## جدول المحتويات

- [القدرات الرئيسية](#القدرات الرئيسية)
- [التثبيت](#التثبيت)
- [البحث عن اسم مستخدم فردي ومتعدد](#single--multiple-username-search)
- [تصفية المواقع والمنصات](#filtering-sites--platforms)
- [توجيه الوكيل وتور](#proxy--tor-routing)
- [ضبط المهلة والترابط](#timeout--ضبط الخيط)
- [تنسيقات التصدير (CSV، XLSX، JSON)](#export-formats-csv-xlsx-json)
- [الأتمتة وتكامل خطوط أنابيب OSINT](#automation--osint-pipeline-integration)

---

## القدرات الرئيسية

- **400+ منصة:** الاستعلامات GitHub وTwitter/X وInstagram وReddit وTikTok وLinkedIn وTelegram وDiscord وSteam وSpotify وDocker Hub وPastebin ومئات آخرين.
- **التزامن غير المتزامن:** مسح متوازي عالي السرعة باستخدام طلبات Python غير المتزامنة.
- **الكشف الدقيق:** يكتشف رموز حالة HTTP ورسائل الخطأ وعمليات إعادة التوجيه لتجنب النتائج الإيجابية الخاطئة.
- **دعم Tor & SOCKS5:** يحمي سرية هوية المحقق ويتجاوز حدود المعدل.

---

## تثبيت
```bash
# Recommended: Install via pipx
pipx install sherlock-project

# Or install from GitHub repository
git clone https://github.com/sherlock-project/sherlock.git
cd sherlock
python3 -m pip install -r requirements.txt

# Debian / Kali Linux repository
sudo apt-get update && sudo apt-get install -y sherlock

# Verify command
sherlock --version
```
---

## البحث عن اسم مستخدم فردي ومتعدد

### 1. البحث الأساسي عن اسم المستخدم
ابحث عن مقبض هدف واحد عبر جميع المواقع التي يزيد عددها عن 400 موقع:
```bash
sherlock target_user
```
### 2. ابحث عن أسماء مستخدمين متعددة في وقت واحد
```bash
sherlock target_user1 target_user2 alias_dev
```
### 3. طباعة الحسابات التي تم العثور عليها فقط (منع عدم العثور عليها / الأخطاء)
```bash
sherlock --print-found target_user
```
---

## تصفية المواقع والمنصات

تضييق عمليات البحث إلى منصات محددة لتوفير الوقت أو التركيز على فئات استخباراتية محددة:

### 1. مواقع الاستعلام المحددة فقط
```bash
sherlock --site github --site twitter --site reddit --site gitlab target_user
```
### 2. البحث بدون منصات محددة (تجاهل المواقع ذات النتائج الإيجابية الزائفة الثقيلة)
```bash
sherlock target_user --site-list targets.txt
```
---

## توجيه الوكيل وTor

لمنع حظر IP، وحدود الأسعار، والحفاظ على أمان التشغيل:

### 1. التوجيه عبر شبكة Tor
توجيه الطلبات تلقائيًا عبر وكيل Tor SOCKS المحلي (`127.0.0.1:9050`):
```bash
sherlock --tor target_user
```
### 2. وكيل HTTP / SOCKS5 مخصص
```bash
# SOCKS5 proxy
sherlock --proxy socks5://127.0.0.1:1080 target_user

# Authenticated HTTP proxy
sherlock --proxy http://user:pass@proxy.corp.internal:8080 target_user
```
---

## ضبط المهلة والخيوط

التحكم في سرعات الطلب وتفاوت زمن الوصول:
```bash
# Set HTTP timeout to 5 seconds per site (default is usually 60s)
sherlock target_user --timeout 5

# Add delay between requests to avoid rate limits
sherlock target_user --delay 1
```
---

## تنسيقات التصدير (CSV، XLSX، JSON)

حفظ الاكتشافات للتحقيقات أو حزم الأدلة أو إعداد التقارير:

### 1. احفظ مباشرة في ملف CSV
```bash
sherlock --csv --output /tmp/sherlock_results target_user
```
### 2. حفظ المخرجات في دليل محدد
```bash
sherlock --folderoutput /tmp/recon_data/ target_user
```
يُنشئ `/tmp/recon_data/target_user.txt` الذي يحتوي على عناوين URL كاملة للملفات الشخصية المؤكدة.

### 3. حفظ كجدول بيانات Excel (XLSX)
```bash
sherlock --xlsx target_user
```
---

## الأتمتة وتكامل خطوط أنابيب OSINT

قم بسلسلة شيرلوك إلى نص محقق آلي:
```bash
#!/usr/bin/env bash
TARGET="$1"
OUTDIR="/tmp/osint_${TARGET}"
mkdir -p "$OUTDIR"

echo "[*] Scanning for username: $TARGET"
sherlock --print-found --timeout 10 --folderoutput "$OUTDIR" "$TARGET"

if [ -f "$OUTDIR/${TARGET}.txt" ]; then
    echo "[+] Found $(wc -l < "$OUTDIR/${TARGET}.txt") accounts:"
    cat "$OUTDIR/${TARGET}.txt"
fi
```