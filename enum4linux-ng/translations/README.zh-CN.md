<!-- language-home-navigation -->
[🏠 中文主页](../../README.zh-CN.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# enum4linux-ng 备忘单

[enum4linux-ng](https://github.com/cddmp/enum4linux-ng) 是用 Python 3 编写的旧版 `enum4linux` 工具的下一代重写。它通过 SMB（服务器消息块）、NetBIOS、RPC（远程过程调用）和 LDAP 执行全面的 Windows 和 Samba 信息收集。它提取用户帐户、组成员身份、共享文件夹、密码策略、操作系统信息和 RID 周期。

---

## 目录

- [相对于旧版 enum4linux 的关键改进](#key-improvements-over-legacy-enum4linux)
- [安装和要求](#installation--要求)
- [基本 SMB 和域枚举](#basic-smb--domain-enumeration)
- [身份验证模式（空会话与有效用户）](#authentication-modes-null-session-vs-valid-user)
- [目标枚举模块](#targeted-enumeration-modules)
- [RID 骑行和用户帐户收获](#rid-cycling--user-account-harvesting)
- [导出报告（JSON、YAML、文本）](#exporting-reports-json-yaml-text)
- [SMB 和 RPC 的防御强化](#defective-hardening-for-smb--rpc)

---

## 相对于旧版 enum4linux 的关键改进

- **现代Python 3引擎：** 基于`impacket`的多线程执行，消除旧的Perl脚本崩溃。
- **增强的操作系统检测：** Windows Server (2012–2025)、Windows 10/11 和现代 Samba 版本的精确检测。
- **结构化报告：** 本机导出到 JSON 和 YAML，以实现无缝 SIEM 或脚本集成。
- **更智能的 RID 循环：** 灵活的范围调整和自动管理员/访客 RID 检测。
- **彩色 CLI 输出：** 针对安全配置错误和空会话的清晰视觉指示器。

---

## 安装和要求
```bash
# Clone the repository
git clone https://github.com/cddmp/enum4linux-ng.git
cd enum4linux-ng

# Install Python dependencies (impacket)
pip3 install -r requirements.txt

# Or install globally as a symlink
sudo ln -sf "$(pwd)/enum4linux-ng.py" /usr/local/bin/enum4linux-ng

# Verify command
enum4linux-ng -h
```
---

## 基本 SMB 和域枚举

### 1. 默认枚举运行 (`-A`)
针对目标运行所有非侵入式枚举检查：
```bash
enum4linux-ng -A 192.168.1.50
```
该模块查询：
- NetBIOS 名称和工作组/域名。
- 操作系统版本和架构。
- 共享目录和访问权限。
- 密码策略（锁定阈值、最小长度、历史记录）。
- 通过 RPC/SAMR 的用户和本地组。

---

## 身份验证模式（空会话与有效用户）

### 1. 匿名/空会话 (`-u '' -p ''`)
测试目标是否允许未经身份验证的 IPC$ 连接：
```bash
enum4linux-ng -u "" -p "" 192.168.1.50
```
### 2.经过身份验证的域/本地帐户
提供有效的低权限凭据以显着扩展收集的数据：
```bash
enum4linux-ng -u "jdoe" -p "Password123!" 192.168.1.50
```
### 3. 提供特定域名（`-d`）
```bash
enum4linux-ng -u "jdoe" -p "Password123!" -d "CORP.LOCAL" 192.168.1.50
```
---

## 目标枚举模块

运行特定模块以最大限度地减少网络占用并避免入侵检测警报：

### 1.共享文件夹和访问权限(`-S`)
枚举共享、评论并检查读/写访问权限：
```bash
enum4linux-ng -S 192.168.1.50
```
### 2.密码策略提取（`-P`）
确定帐户锁定阈值以安全地计划密码喷洒：
```bash
enum4linux-ng -P 192.168.1.50
```
需要观察的关键输出：
- **最小密码长度：** 例如，8 个字符。
- **帐户锁定阈值：** 例如，5 次尝试（不要喷射得比锁定窗口更快！）。
- **重置锁定计数器：** 例如，30 分钟。

### 3. 仅用户和组（`-U` 和 `-G`）
```bash
# Enumerate user list
enum4linux-ng -U 192.168.1.50

# Enumerate domain and local groups
enum4linux-ng -G 192.168.1.50
```
### 4. 仅操作系统和 NetBIOS 信息（`-O` 和 `-I`）
```bash
enum4linux-ng -O -I 192.168.1.50
```
---

## RID 循环和用户帐户收集

当通过标准 RPC 查询进行的用户枚举受到限制时，通过 SAMR 查询调用执行相对标识符 (RID) 循环：

### 1. 默认 RID 循环（范围 500 - 1100）
```bash
enum4linux-ng -R 192.168.1.50
```
### 2. 自定义 RID 范围（例如，大型组织为 500 到 5000）
```bash
enum4linux-ng -r 500-5000 192.168.1.50
```
### 3. 提取干净的用户名单词列表
将发现的 RID 用户名过滤到文本文件中以进行密码喷射：
```bash
enum4linux-ng -R 192.168.1.50 -oJ /tmp/enum_out
jq -r '.users[].username' /tmp/enum_out.json | sort -u > /tmp/users.txt
```
---

## 导出报告（JSON、YAML、文本）

导出枚举结果以供文档或编程解析：

### 1.同时导出所有格式(`-oA`)
```bash
enum4linux-ng -A 192.168.1.50 -oA /tmp/target_recon
```
生成：
- `/tmp/target_recon.json`
- `/tmp/target_recon.yaml`
- `/tmp/target_recon.txt`

### 2.导出为特定格式
```bash
# JSON only
enum4linux-ng -A 192.168.1.50 -oJ /tmp/target.json

# YAML only
enum4linux-ng -A 192.168.1.50 -oY /tmp/target.yaml
```
---

## SMB 和 RPC 的防御强化

|矢量|风险|强化建议|
|---|---|---|
| **空会话（匿名 IPC$）** |未经身份验证的域侦察 |在 Windows 注册表中设置 `RestrictNullSessAccess=1`；在 SAM 中配置 `RestrictAnonymous=2`。 |
| **SMBv1 活动** | EternalBlue (MS17-010) 和 MITM 攻击 |在所有端点和域控制器上完全禁用 SMBv1。 |
| **明文共享权限** |敏感文件和凭据暴露 |审核共享权限；强制执行基于访问的枚举 (ABE)。 |
| **RID 循环枚举** |完整的用户帐户列表提取 |通过本地安全策略限制匿名 SAM 帐户枚举。 |