<!-- language-home-navigation -->
[🏠 الصفحة الرئيسية العربية](../../README.ar.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🦈 tshark - ورقة الغش كاملة

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ████████╗███████╗██╗  ██╗ █████╗ ██████╗ ██╗  ██╗
  ╚══██╔══╝██╔════╝██║  ██║██╔══██╗██╔══██╗██║ ██╔╝
     ██║   ███████╗███████║███████║██████╔╝█████╔╝ 
     ██║   ╚════██║██╔══██║██╔══██║██╔══██╗██╔═██╗ 
     ██║   ███████║██║  ██║██║  ██║██║  ██║██║  ██╗
     ╚═╝   ╚══════╝╚═╝  ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝
         Terminal Packet Analysis & Dissection Engine
```
<p align="center">
  <img src="https://img.shields.io/badge/tshark-Packet_Analyzer-blue?style=for-the-badge" alt="tshark">
  <img src="https://img.shields.io/badge/CLI-Network_Sniffer-red?style=for-the-badge" alt="CLI Network Sniffer">
  <img src="https://img.shields.io/badge/Wireshark-Engine-green?style=for-the-badge" alt="محرك Wireshark">
  <img src="https://img.shields.io/badge/PCAP-Dissection-orange?style=for-the-badge" alt="تشريح PCAP">
</ص>

<p align="center">
  <b>🌐 تشريح حزم الشبكة العميق القائم على المحطة، وتحليل التدفق، والاستخراج الميداني المدعوم من Wireshark</b>
</ص>

---

## 📋 جدول المحتويات

- [المقدمة](#-المقدمة)
- [مقارنة الأدوات: tshark vs tcpdump vs Wireshark](#-tool-comparison)
- [التثبيت والامتيازات](#-التثبيت--الامتيازات)
- [التقاط أساسي وتشغيل](#-التقاط أساسي--تشغيل)
- [تصفية حركة المرور: الالتقاط مقابل العرض](#-تصفية حركة المرور)
- [استخراج الحقل (`-T fields`)](#-استخراج الحقل)
- [الخماسية العملية ووصفات الطب الشرعي](#-البنتست العملية--وصفات-الطب الشرعي)
  - [بيانات اعتماد HTTP والبحث عن URI](#http-credential--uri-hunting)
  - [استخراج استعلام DNS واكتشاف الأنفاق](#dns-query-extraction--tunnel-detection)
  - [استخراج TLS / HTTPS SNI](#tls--https-sni-extraction)
  - [استخراج Kerberos AS-REQ / AS-REP](#kerberos-as-req--as-rep-extraction)
  - [تتبع مصادقة SMB وNTLM](#smb--ntlm-authentication-tracking)
  - [تدقيق بروتوكول Cleartext (FTP، Telnet، SMTP)](#cleartext-protocol-auditing)
  - [صيد المصافحة رباعية الاتجاهات WPA / EAPOL](#wpa--eapol-4-way-handshake-hunting)
- [متابعة البث وإعادة التجميع](#-متابعة-البث--إعادة التجميع)
- [الإحصائيات والمحادثات ونقاط النهاية](#-الإحصائيات-المحادثات--نقاط النهاية)
- [فك تشفير حركة مرور TLS / SSL](#-tls--ssl-traffic-decryption)
- [تنسيقات التصدير (JSON، CSV، Elasticsearch)](#-تنسيقات التصدير)
- [الجدول المرجعي السريع](#-الجدول المرجعي السريع)
- [النصائح وأفضل الممارسات](#-نصائح--أفضل-الممارسات)
- [إخلاء المسؤولية القانونية](#-إخلاء المسؤولية القانونية)

---

## 🎯 المقدمة

**tshark** هو الطرفية ونظير سطر الأوامر لـ **Wireshark**. وهو يعتمد على مكتبة التقاط الحزم المتطابقة (libpcap/Npcap) ومحرك تشريح الحزم مثل Wireshark الرسومي، مما يجعله أحد أقوى أدوات تحليل الشبكة المتوفرة في Linux وmacOS وWindows.

على عكس أدوات اكتشاف الحزم الأساسية التي تقوم فقط بفحص رؤوس البروتوكول، يتميز `tshark` بقدرات فحص عميقة للحزم عبر آلاف البروتوكولات، وبناء جملة مرشح العرض Wireshark الأصلي (`-Y`)، واستخراج حقل البروتوكول القوي (`-T fields`)، وتنسيق الإخراج المرن لخطوط أنابيب التشغيل الآلي (`jq`، وshell scripts، وPython).

### الميزات الرئيسية

| ميزة | الوصف |
|---------|------------|
| **تشريح عميق** | يفحص بيانات الحمولة عبر الآلاف من بروتوكولات التطبيق |
| **مرشحات Wireshark** | دعم كامل لصيغة مرشح عرض Wireshark الغنية (`-Y`) |
| **استخراج الحقل** | استخراج حقول البروتوكول الفردية مباشرة (`-T fields -e ...`) |
| ** متابعة البث ** | يعيد تجميع جلسات TCP/UDP الكاملة بتنسيقات ASCII أو Hex أو الأولية |
| **الطب الشرعي الآلي**| قابلة للبرمجة في shell، أو Python، أو يتم نقلها عبر الأنابيب إلى `jq` عبر `-T json` |
| **فك التشفير** | يدعم فك تشفير TLS في وقت التشغيل باستخدام `SSLKEYLOGFILE` |

---

## ⚖️ مقارنة الأدوات

| ميزة | تشارك | تكبدمب | ويرشارك |
|---------|--------|--------|-----------|
| **الواجهة** | سطر الأوامر (المحطة الطرفية) | سطر الأوامر (المحطة الطرفية) | واجهة المستخدم الرسومية (الرسومية) |
| **استخدام الموارد** | منخفض / معتدل | الحد الأدنى للغاية | أعلى (ذاكرة الوصول العشوائي ووحدة المعالجة المركزية) |
| **جهاز التحكم عن بعد (SSH / بدون رأس)** | أصلي مقطوع الرأس / CLI | أنبوب SSH الأصلي | يتطلب X11 / الوكيل البعيد |
| **محرك التشريح** | مشرحات Wireshark الكاملة | العناوين الأساسية | مشرحات Wireshark الكاملة |
| **التقاط المرشحات** | BPF فقط (`-f`) | BPF فقط (`-i`) | BPF فقط |
| **مرشحات العرض** | ✅ بناء جملة Wireshark الكامل (`-Y`) | ❌ لا يوجد | ✅ شريط مرشح واجهة المستخدم الرسومية الكامل |
| **تنسيقات التصدير** | PCAP، JSON، CSV، مطاطا، XML | PCAP، ASCII، سداسي عشري | PCAP، CSV، XML، JSON |
| ** البرمجة النصية والأتمتة **| شل، بايثون، جي كيو | شل / أوك / سيد | ملحقات Lua، واجهة المستخدم الرسومية |

---

## 📥 التثبيت والامتيازات

### لينكس (ديبيان / أوبونتو / كالي لينكس)
```bash
# Update and install tshark
sudo apt update && sudo apt install -y tshark

# Verify installation and version
tshark --version
```
### السماح بالتقاط الحزم غير الجذرية

لمنع تشغيل `tshark` كـ `root` (والذي يمكن أن يشكل خطرًا أمنيًا بسبب سطح المجزئ الواسع)، قم بإضافة المستخدم الخاص بك إلى مجموعة `wireshark`:
```bash
# Configure wireshark-common for non-superusers
sudo dpkg-reconfigure wireshark-common
# (Select "Yes" when prompted)

# Add your user to the wireshark group
sudo usermod -aG wireshark $USER

# Apply group changes to current session
newgrp wireshark
```
### macOS (Homebrew) وWindows (Chocolatey)
```bash
# macOS
brew install wireshark

# Windows
choco install wireshark
```
---

## 🚀 الالتقاط والتشغيل الأساسيان

### قائمة الواجهات المتاحة
```bash
# List interfaces with numeric IDs
tshark -D
```
### التقاط الحزم الحية
```bash
# Capture packets on interface eth0
tshark -i eth0

# Capture only 100 packets and stop
tshark -i eth0 -c 100

# Capture and save traffic directly to a pcap file
tshark -i eth0 -w capture.pcap

# Capture with ring buffer (rotate files every 100MB, keep max 5 files)
tshark -i eth0 -b filesize:100000 -b files:5 -w rolling_traffic.pcap

# Capture with a duration trigger (capture for 60 seconds)
tshark -i eth0 -a duration:60 -w one_minute.pcap
```
### قراءة ملفات PCAP
```bash
# Read and display packets from a pcap file
tshark -r capture.pcap

# Read PCAP without resolving IP addresses to DNS hostnames (speeds up processing)
tshark -r capture.pcap -n

# Suppress packet count summary upon completion
tshark -r capture.pcap -q
```
---

## 🔍 تصفية حركة المرور: الالتقاط مقابل العرض

يستخدم `tshark` محركين متميزين للتصفية:

1. **مرشحات الالتقاط (`-f`):** يتم تقييم مرشحات حزم بيركلي (BPF) بواسطة النواة قبل أن تصل الحزم إلى مساحة المستخدم. أداء عالي، ولكنه يقتصر على أساسيات IP/المنفذ الأساسية.
2. **مرشحات العرض (`-Y`):** يتم تقييم مرشحات البروتوكول العميق بواسطة محرك تشريح Wireshark. يدعم طبقات التطبيق، والبحث عن السلسلة، والتعبير العادي، وأعلام البروتوكول.
```bash
# Capture filter: Capture only TCP traffic to port 80 or 443
tshark -i eth0 -f "tcp port 80 or tcp port 443" -w web_traffic.pcap

# Display filter: Show only successful HTTP GET responses (Status 200)
tshark -r capture.pcap -Y "http.request.method == \"GET\" and http.response.code == 200"

# Display filter: Filter by specific subnet and exclude broadcast
tshark -r capture.pcap -Y "ip.addr == 192.168.1.0/24 and not eth.dst == ff:ff:ff:ff:ff:ff"

# Read-filter (-R): Single-pass filter used during capture
tshark -r capture.pcap -2 -R "dns.flags.response == 1"
```
---

## 📊 استخراج الحقل (`-T fields`)

يعد وضع `-T fields` أقوى ميزة في `tshark` للاستطلاع الآلي والبرمجة النصية وتحديات CTF.
```bash
# Basic syntax for field extraction
tshark -r capture.pcap -T fields -e <field1> -e <field2> -E header=y -E separator=,
```
### علامات تنسيق استخراج المفاتيح

| علم | الوصف |
|------|------------|
| `-T fields` | يقوم بتبديل وضع الإخراج إلى استخراج الحقل المخصص |
| `-e <field>` | تسمية حقل البروتوكول المراد استخراجه (يمكن تكراره) |
| `-E header=y` | طباعة أسماء الأعمدة كصف رأس |
| `-E separator=,` | يضبط فاصل الحقول (`,` لـ CSV، و`\t` لـ TSV) |
| `-E occurrence=f` | يستخرج فقط التكرارات الأولى (`f`) أو الأخيرة (`l`) أو جميع التكرارات (`a`) |
| `-E quote=d` | إحاطة القيم بعلامات اقتباس مزدوجة |

### ورقة الغش الخاصة بحقول البروتوكول الأساسية
```bash
# IP layer fields
ip.src, ip.dst, ip.proto, ip.ttl

# TCP layer fields
tcp.srcport, tcp.dstport, tcp.flags, tcp.stream, tcp.payload

# HTTP fields
http.host, http.request.method, http.request.uri, http.response.code, http.user_agent, http.cookie

# DNS fields
dns.qry.name, dns.qry.type, dns.a, dns.flags.response

# TLS fields
tls.handshake.extensions_server_name, tls.handshake.ciphersuite
```
---

## 🎯 Pentest العملية ووصفات الطب الشرعي

### البحث عن بيانات اعتماد HTTP ومعرف URI
```bash
# Extract all visited URLs and HTTP Hosts
tshark -r capture.pcap -Y "http.request" -T fields -e ip.src -e http.host -e http.request.method -e http.request.uri -E header=y

# Extract User-Agent strings across internal clients
tshark -r capture.pcap -Y "http.user_agent" -T fields -e ip.src -e http.user_agent | sort -u

# Hunt for HTTP POST data (logins, passwords, tokens)
tshark -r capture.pcap -Y "http.request.method == \"POST\"" -T fields -e ip.src -e ip.dst -e http.host -e http.file_data

# Hunt for HTTP Basic Authentication headers (Base64 encoded)
tshark -r capture.pcap -Y "http.authorization" -T fields -e ip.src -e http.authorization
```
### استخراج استعلام DNS واكتشاف الأنفاق
```bash
# List all requested domain names (DNS queries)
tshark -r capture.pcap -Y "dns.flags.response == 0" -T fields -e ip.src -e dns.qry.name | sort -u

# Detect potential DNS tunneling (display long subdomains > 30 characters)
tshark -r capture.pcap -Y "dns.flags.response == 0" -T fields -e dns.qry.name | awk 'length($1) > 30' | sort -u

# Correlate DNS queries with their resolved IP addresses
tshark -r capture.pcap -Y "dns.flags.response == 1 and dns.a" -T fields -e dns.qry.name -e dns.a -E header=y
```
### استخراج TLS / HTTPS SNI

حتى عندما يتم تشفير حمولات HTTPS، يكشف **مؤشر اسم الخادم (SNI)** في مصافحة TLS Client Hello الأولية عن أسماء النطاق الوجهة:
```bash
# Extract TLS Server Name Indication (SNI) hostnames
tshark -r capture.pcap -Y "tls.handshake.type == 1" -T fields -e ip.src -e ip.dst -e tls.handshake.extensions_server_name | sort -u
```
### استخراج Kerberos AS-REQ / AS-REP
```bash
# Extract Kerberos domain client names (hunting for user accounts)
tshark -r capture.pcap -Y "kerberos.CNameString" -T fields -e ip.src -e kerberos.realm -e kerberos.CNameString | sort -u

# Hunt for Kerberos errors (e.g. KRB5KDC_ERR_PREAUTH_REQUIRED, bad passwords)
tshark -r capture.pcap -Y "kerberos.error_code" -T fields -e ip.src -e ip.dst -e kerberos.error_code
```
### تتبع مصادقة SMB وNTLM
```bash
# Extract SMB accessed file paths and shares
tshark -r capture.pcap -Y "smb2.filename" -T fields -e ip.src -e ip.dst -e smb2.filename | sort -u

# Extract NTLMSSP usernames and domain names during authentication
tshark -r capture.pcap -Y "ntlmssp.auth.username" -T fields -e ip.src -e ntlmssp.auth.domain -e ntlmssp.auth.username
```
### تدقيق بروتوكول النص الواضح
```bash
# Extract FTP usernames and passwords
tshark -r capture.pcap -Y "ftp.request.command == \"USER\" or ftp.request.command == \"PASS\"" -T fields -e ip.src -e ftp.request.command -e ftp.request.arg

# Extract Telnet session input
tshark -r capture.pcap -Y "telnet" -T fields -e telnet.data
```
### صيد المصافحة رباعية الاتجاهات WPA / EAPOL
```bash
# Filter for 802.11 EAPOL handshakes to crack with Hashcat/Aircrack-ng
tshark -r wifi.pcap -Y "eapol" -w handshakes_only.pcap
```
---

## 🧵 متابعة البث وإعادة التجميع

يسمح `tshark` بإعادة تجميع تدفقات TCP وUDP بالكامل دون الحاجة إلى واجهة المستخدم الرسومية الخاصة بـ Wireshark.
```bash
# List all TCP conversations to find stream IDs
tshark -r capture.pcap -q -z conv,tcp

# Follow TCP stream 0 in ASCII format
tshark -r capture.pcap -q -z follow,tcp,ascii,0

# Follow TCP stream 2 in Hexdump format
tshark -r capture.pcap -q -z follow,tcp,hex,2

# Follow HTTP stream (stream index 1)
tshark -r capture.pcap -q -z follow,http,ascii,1
```
---

## 📈 الإحصائيات والمحادثات ونقاط النهاية
```bash
# Protocol hierarchy statistics (breakdown of all protocols in PCAP)
tshark -r capture.pcap -q -z io,phs

# IP conversations table (who talked to whom, packet count, bytes)
tshark -r capture.pcap -q -z conv,ip

# Port conversation statistics (TCP ports)
tshark -r capture.pcap -q -z conv,tcp

# List all unique IP endpoints in PCAP
tshark -r capture.pcap -q -z endpoints,ip

# HTTP request statistics tree
tshark -r capture.pcap -q -z http,tree

# Wireshark Expert Info warnings and errors
tshark -r capture.pcap -q -z expert
```
---

## 🔐 فك تشفير حركة مرور TLS / SSL

إذا كان لديك مفاتيح سرية أولية (تم تصديرها عبر المتصفح `SSLKEYLOGFILE` أو تم استردادها أثناء تحديد وضع الحماية للبرامج الضارة):
```bash
# Decrypt TLS traffic on the fly using keylog file
tshark -r https_traffic.pcap -o "tls.keylog_file:/path/to/sslkeys.log" -Y "http" -T fields -e http.host -e http.request.uri

# Follow decrypted HTTP stream inside TLS
tshark -r https_traffic.pcap -o "tls.keylog_file:/path/to/sslkeys.log" -q -z follow,http,ascii,0
```
---

## 📦 تنسيقات التصدير
```bash
# Output as structured JSON (ideal for piping into jq or Python)
tshark -r capture.pcap -c 5 -T json

# Extract specific JSON field with jq
tshark -r capture.pcap -T json | jq '.[]._source.layers.ip["ip.src"]'

# Output in Elasticsearch Bulk Format (-T ek)
tshark -r capture.pcap -T ek > packets_for_elastic.json

# Output as CSV table
tshark -r capture.pcap -T fields -e frame.number -e ip.src -e ip.dst -e _ws.col.Protocol -E header=y -E separator=, > traffic.csv
```
---

## ⚡ جدول مرجعي سريع

| مهمة | الأمر |
|------|---------|
| **التقاط حركة المرور المباشرة** | `tshark -i eth0 -w output.pcap` |
| **التقاط باستخدام مرشح BPF** | `tshark -i eth0 -f "port 80" -w http.pcap` |
| **اقرأ PCAP بدون DNS** | `tshark -r capture.pcap -n` |
| **تطبيق مرشح العرض** | `tshark -r capture.pcap -Y "http.response.code >= 400"` |
| **استخراج عناوين IP والمنافذ** | `tshark -r capture.pcap -T fields -e ip.src -e tcp.srcport -e ip.dst -e tcp.dstport` |
| **استخراج استعلامات DNS** | `tshark -r capture.pcap -Y "dns.flags.response == 0" -T fields -e dns.qry.name` |
| **استخرج أسماء مضيفي TLS SNI**| `tshark -r capture.pcap -Y "tls.handshake.type == 1" -T fields -e tls.handshake.extensions_server_name` |
| **استخراج عناوين URL لـ HTTP** | `tshark -r capture.pcap -Y "http.request" -T fields -e http.host -e http.request.uri` |
| ** اتبع تيار TCP ** | `tshark -r capture.pcap -q -z follow,tcp,ascii,0` |
| **التسلسل الهرمي للبروتوكول** | `tshark -r capture.pcap -q -z io,phs` |
| **فك التشفير باستخدام Keylog** | `tshark -r traffic.pcap -o "tls.keylog_file:keys.log"` |
| **تصدير إلى JSON** | `tshark -r capture.pcap -T json > packets.json` |

---

## 💡 النصائح وأفضل الممارسات

1. ** استخدم دائمًا `-n` لـ PCAPs الكبيرة: ** يؤدي تحليل اسم DNS إلى إبطاء عملية التحليل بشكل كبير. أضف `-n` لمنع حل عناوين IP.
2. **ادمج `-f` و`-Y`:** استخدم مرشحات الالتقاط `-f` أثناء الالتقاط المباشر لتقليل قطرات الحزم، ومرشحات العرض `-Y` أثناء التحليل اللاحق لإجراء فحص عميق للبروتوكول.
3. **المسار مع `jq`:** يسمح توصيل `tshark -T json` إلى `jq` بالاستعلام عن حقول البروتوكول المتداخلة مع المنطق والتحويل المنطقي الكامل.
4. **المخازن المؤقتة الحلقية للمراقبة على مدار 24 ساعة طوال أيام الأسبوع:** عند تشغيل عمليات التقاط مستمرة للشبكة على مضيف محوري، قم دائمًا بفرض حدود الحجم والملف (`-b filesize:50000 -b files:10`) لمنع تعطل قرص الهدف.

---

## ⚠️إخلاء المسؤولية القانونية

> **هام:** يعد التقاط الحزم غير المصرح به والتنصت على حركة مرور الشبكة واعتراض بيانات الاعتماد على الشبكات دون الحصول على إذن صريح وموثق أمرًا غير قانوني ويخضع للملاحقة الجنائية. استخدم `tshark` بشكل صارم على الأنظمة والشبكات التي تمتلكها أو المرخص لها تعاقديًا بتقييمها.