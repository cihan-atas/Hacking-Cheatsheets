<!-- language-home-navigation -->
[🏠 中文主页](../../README.zh-CN.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# Searchsploit 备忘单

[Searchsploit](https://www.exploit-db.com/searchsploit) 是 [Exploit-Database](https://www.exploit-db.com/) 的官方命令行实用程序。它允许安全专业人员、渗透测试人员和漏洞研究人员对数以万计的已发布漏洞、shellcode 和白皮书进行离线搜索，而无需将网络流量发送到第三方搜索引擎。

---

## 目录

- [概述与架构](#overview--architecture)
- [安装和本地数据库更新](#installation--local-database-updates)
- [基本和高级漏洞利用搜索](#basic--advanced-exploit-searching)
- [检查和复制漏洞(`-m` / `-p` / `-x`)](#examining--copying-exploits--m---p---x)
- [按平台和类型过滤](#filtering-by-platform--type)
- [Nmap XML / Grepable 扫描集成](#nmap-xml--grepable-scanning-integration)
- [JSON 输出和脚本自动化](#json-output--script-automation)
- [渗透测试最佳实践](#best-practices-in-penetration-testing)

---

## 概述与架构

Searchsploit 查询存储在 `/usr/share/exploitdb/` 中的 Exploit-DB 的本地 git 克隆。
- **零网络指纹：** 针对本地 CSV 和目录结构运行搜索。
- **离线可靠性：** 在气隙目标环境和参与网络中功能齐全。
- **集成镜像：** 将漏洞利用文件直接复制到当前工作目录，以便立即查看和修改。

---

## 安装和本地数据库更新

### Debian / Ubuntu / Kali Linux / Parrot 操作系统
```bash
# Kali and Parrot include searchsploit by default
sudo apt-get update && sudo apt-get install -y exploitdb

# Update the local exploit database archive
searchsploit -u
```
### Git 存储库安装（任何 Linux 发行版）
```bash
git clone https://gitlab.com/exploit-database/exploitdb.git /opt/exploitdb
sudo ln -sf /opt/exploitdb/searchsploit /usr/local/bin/searchsploit

# Check version and status
searchsploit -v
```
---

## 基本和高级漏洞利用搜索

### 1.简单的关键字搜索
搜索特定服务、守护程序或软件：
```bash
searchsploit apache 2.4.49
searchsploit openssh 7.2
searchsploit wordpress core 5.8
```
### 2. 标题精确匹配 (`-t`)
严格限制搜索查询以利用标题来消除嘈杂的描述匹配：
```bash
searchsploit -t "Privilege Escalation" linux kernel
searchsploit -t "Remote Code Execution" smb
```
### 3. 严格大小写和术语匹配 (`-e`)
查找确切的版本和模式匹配：
```bash
searchsploit -e "Apache Struts"
```
### 4. 按 CVE 标识符搜索
查询映射到特定常见漏洞和暴露编号的漏洞利用：
```bash
searchsploit --cve 2021-44228
searchsploit --cve 2017-0144
```
### 5. 排除不需要的结果 (`--exclude`)
在寻求远程代码执行 (RCE) 时过滤掉拒绝服务 (DoS) 或辅助脚本：
```bash
searchsploit linux kernel 5.4 --exclude="Denial of Service"
```
---

## 检查和复制漏洞（`-m` / `-p` / `-x`）

切勿直接在 `/usr/share/exploitdb/` 目录中编辑或执行漏洞利用。使用 Searchsploit 的复制和检查功能。

### 1. 将漏洞复制（镜像）到当前目录 (`-m`)
将漏洞利用 PoC 文件（Python、C、Ruby、bash）直接复制到本地工作目录：
```bash
# Mirror by Exploit-DB ID (EBD-ID)
searchsploit -m 50383

# Mirror by search path pattern
searchsploit -m windows/remote/48537.py
```
### 2. 检查漏洞利用元数据和完整路径 (`-p`)
显示 EDB-ID 的绝对文件系统路径和 Exploit-DB URL：
```bash
searchsploit -p 50383
```
输出：
```text
  Exploit: Apache HTTP Server 2.4.49 - Path Traversal & Remote Code Execution (RCE)
      URL: https://www.exploit-db.com/exploits/50383
     Path: /usr/share/exploitdb/exploits/multiple/webapps/50383.sh
File Type: Bourne-Again shell script, ASCII text executable
```
### 3. 在终端寻呼机中读取漏洞利用代码 (`-x`)
直接在`less`中打开漏洞利用代码进行快速检查：
```bash
searchsploit -x 50383
```
---

## 按平台和类型过滤

Searchsploit 索引跨多个架构的漏洞利用、shellcode 和研究论文：

### 1. 按目标操作系统过滤
```bash
# Search only Linux exploits
searchsploit linux kernel 4.4

# Search only Windows exploits
searchsploit windows smb eternal

# Search web applications
searchsploit webapps tomcat
```
### 2. 仅过滤原始 Shellcode (`-s`)
查找特定于体系结构的 shellcode 有效负载（例如，不带空字节的 execve `/bin/sh`）：
```bash
searchsploit -s linux/x64 "execve"
searchsploit -s windows/x86 "reverse shell"
```
### 3. 安全文件和白皮书过滤器 (`-w`)
```bash
searchsploit -w "Active Directory Kerberos"
```
---

## Nmap XML / Grepable 扫描集成

Searchsploit 可以自动解析 Nmap XML 扫描输出，并将发现的软件版本与已知漏洞进行匹配：
```bash
# 1. Perform service detection and save XML output
nmap -sV -p- -T4 -oX /tmp/target_scan.xml 192.168.1.50

# 2. Feed the XML scan file directly to Searchsploit
searchsploit --nmap /tmp/target_scan.xml
```
*Searchsploit 检查每个开放端口横幅和版本字符串，查询本地漏洞以立即分类。*

---

## JSON 输出和脚本自动化

对于管道自动化和自定义侦察工具，以结构化 JSON 输出结果：
```bash
searchsploit "Apache Struts" --json > struts_exploits.json
```
使用 `jq` 过滤漏洞利用文件路径：
```bash
searchsploit --cve 2021-41773 --json | jq -r '.RESULTS_EXPLOIT[].Path'
```
---

## 渗透测试最佳实践

1. **运行前验证：** 在编译或执行之前始终检查漏洞利用源代码（`searchsploit -x <id>`）。验证硬编码的 shellcode、目标 IP 地址，并确保没有后门或恶意有效负载阶段。
2. **使用正确的标志进行编译：** 许多 Exploit-DB C 漏洞利用需要特定的 GCC 标志（例如 `-m32`、`-lpthread` 或 `-lcrypto`）：
   ```bash
   searchsploit -m 40839
   gcc -pthread 40839.c -o dirtycow -lcrypt
   ```
3. **保持数据库更新：** 定期运行 `searchsploit -u` 以确保新发布的零日和概念验证在本地建立索引。