<!-- language-home-navigation -->
[🏠 中文主页](../../README.zh-CN.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ⌨️ rlwrap - 完整备忘单

[🇹🇷 Türkçe Sürüm]（翻译/README.tr.md）
```
  ██████╗ ██╗     ██╗    ██╗██████╗  █████╗ ██████╗ 
  ██╔══██╗██║     ██║    ██║██╔══██╗██╔══██╗██╔══██╗
  ██████╔╝██║     ██║ █╗ ██║██████╔╝███████║██████╔╝
  ██╔══██╗██║     ██║███╗██║██╔══██╗██╔══██║██╔═══╝ 
  ██║  ██║███████╗╚███╔███╔╝██║  ██║██║  ██║██║     
  ╚═╝  ╚═╝╚══════╝ ╚══╝╚══╝ ╚═╝  ╚═╝╚═╝  ╚═╝╚═╝     
       Readline Wrapper for Reverse Shells & Dumb Terminals
```
<p对齐=“中心”>
  <img src="https://img.shields.io/badge/rlwrap-Readline_Wrapper-blue?style=for-the-badge" alt="rlwrap">
  <img src="https://img.shields.io/badge/Utility-Shell_Stabilization-green?style=for-the-badge" alt="外壳稳定">
  <img src="https://img.shields.io/badge/Category-Exploitation_&_Shells-orange?style=for-the-badge" alt="利用与贝壳">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="许可证 GPLv2">
</p>

<p对齐=“中心”>
  <b>🌐 将原始 Netcat 监听器和哑反向 shell 转变为带有箭头键、历史记录和自动完成功能的完整命令行界面</b>
</p>

---

## 📋 目录

- [简介：“哑壳”问题](#-introduction-the-dumb-shell-problem)
- [安装](#-安装)
- [基本用法](#-basic-usage)
- [捕捉反向炮弹 (`rlwrap + nc`)](#-捕捉-reverse-shells-rlwrap--nc)
- [持久历史记录管理](#-persistent-history-management)
- [命令自动完成 (`-f`)](#-command-auto-completion--f)
- [完整 5 步 TTY Shell 升级工作流程](#-full-5-step-tty-shell-upgrade-workflow)
- [将 rlwrap 与其他 Pentest 工具结合使用](#-using-rlwrap-with-other-pentest-tools)
  - [Oracle SQL*Plus](#oracle-sqlplus)
  - [Redis 与 Telnet](#redis--telnet)
  - [Socat 和自定义套接字](#socat--custom-sockets)
- [快速参考表](#-quick-reference-table)
- [提示和最佳实践](#-tips--best-practices)
- [法律免责声明](#-legal-disclaimer)

---

## 🎯 简介：“哑壳”问题

当您使用 Netcat (`nc -lvnp 4444`) 等传统工具捕获反向 shell 时，您将获得一个**哑终端**。哑终端缺乏标准的行编辑功能：

- ❌ 按 **向上/向下箭头键** 打印转义序列，如 `^[[A` 或 `^[[B`，而不是循环查看命令历史记录。
- ❌ 按 **Backspace** 或 **Delete** 打印 `^H` 或 `^?`，而不是删除字符。
- ❌ 按 **Ctrl + C** 立即杀死反向 shell 侦听器，失去来之不易的访问权限。
- ❌ `su`、`sudo`、`nano`、`vim` 或 `passwd` 等命令会失败，因为没有附加伪终端 (PTY)。
- ❌ 跨会话不保留持久命令历史记录。

**rlwrap** （Readline Wrapper，由 Hans Lub 编写）通过位于终端和目标命令之间解决了这个问题。它拦截键盘敲击，利用**GNU Readline**，并提供箭头键导航、行编辑、持久命令历史文件和基于字典的自动完成！

---

## 📥 安装

`rlwrap` 是轻量级的，几乎在每个 Linux 发行版的默认软件包存储库中都可用：

### Kali Linux、Debian 和 Ubuntu
```bash
# Update and install rlwrap
sudo apt update && sudo apt install -y rlwrap

# Verify installation and version
rlwrap --version
```
### Fedora、CentOS 和 RHEL
```bash
# Fedora / CentOS / RHEL
sudo dnf install -y rlwrap || sudo yum install -y epel-release && sudo yum install -y rlwrap
```
### Arch Linux
```bash
# Arch Linux / Manjaro
sudo pacman -S rlwrap
```
### macOS（自制）
```bash
# macOS
brew install rlwrap
```
---

## 🚀 基本用法

基本语法只是用 `rlwrap` 包装任何命令行：
```bash
# General syntax
rlwrap [options] <command> [command-arguments]
```
### 常用标志

|旗帜|描述 |
|------|-------------|
| `-c` |启用文件和路径名补全 |
| `-A` |忽略提示格式转义码（防止提示行故障）|
| `-r` |记住输入时输入的所有单词并将它们放入完成单词列表 |
| `-H <file>` |指定一个专用的历史文件来保存和调用命令 |
| `-s <num>` |设置最大历史行数（默认：300） |
| `-f <file>` |加载自定义完成字典文件 |
| `-b <chars>` |定义分词字符 |
| `-m` |启用多行输入编辑 |
| `-p[color]` |为命令提示符着色 |

---

## 🎣 捕捉反向贝壳 (`rlwrap + nc`)

进攻性安全中最常见的用例是包装 Netcat 侦听器（请参阅 [Netcat Cheatsheet](../Netcat/README.md) 了解完整的侦听器变体和标志）：
```bash
# Basic Netcat listener with arrow-key editing & history
rlwrap nc -lvnp 4444

# Professional pentester flags: -c (file completion), -A (prompt fix), -r (remember words)
rlwrap -cAr nc -lvnp 4444
```
### `-cAr` 标志的说明：

1. **`-c`（文件完成）：** 允许使用 `<TAB>` 自动完成文件和目录名称。
2. **`-A`（提示）：** 当远程服务器发送非标准化提示字符串（例如 `root@victim:/#`）时，防止奇怪的换行和提示重复。
3. **`-r`（记住单词）：** 学习您在 shell 中输入的单词，以便您稍后可以通过 Tab 键完成它们。

---

## 💾 持久历史记录管理

默认情况下，`rlwrap` 将命令历史记录保存在 `~/.<command>_history` 中。您可以配置自定义的每个参与历史文件：
```bash
# Save history to a dedicated engagement file
rlwrap -H ~/.history_target_corp nc -lvnp 4444

# Increase history size to 5,000 entries
rlwrap -H ~/.history_target -s 5000 nc -lvnp 4444

# Case-insensitive history search
rlwrap -i nc -lvnp 4444
```
---

## 🔍 命令自动完成 (`-f`)

您可以向 `rlwrap` 提供包含常见 Linux 或 Windows 命令的文本文件，当您按 `<TAB>` 时，它会自动完成它们：
```bash
# Create a wordlist of essential post-exploitation commands
cat << 'EOF' > /tmp/pentest_commands.txt
whoami
id
hostname
uname -a
cat /etc/passwd
cat /etc/shadow
sudo -l
find / -perm -4000 2>/dev/null
netstat -tulnp
ss -tulnp
ps aux
ip a
route -n
crontab -l
EOF

# Start listener with custom tab-completion dictionary
rlwrap -f /tmp/pentest_commands.txt nc -lvnp 4444
```
---

## 🛠️ 完整 5 步 TTY Shell 升级工作流程

虽然 `rlwrap` 提供即时箭头键导航和历史记录，但某些交互式程序（`su`、`sudo`、`vim`）仍然需要完全分配的 TTY 终端。以下是行业标准的 5 步升级流程：

### 第 1 步：使用 rlwrap 启动监听器
```bash
# Attacker machine: start enhanced listener
rlwrap -cAr nc -lvnp 4444
```
### 步骤 2：触发反向 Shell 和 Spawn PTY

一旦你的反向 shell 连接到你的监听器，就在目标上生成一个伪终端：
```bash
# Target shell: Spawn PTY via Python
python3 -c 'import pty; pty.spawn("/bin/bash")'

# OR via script if Python is unavailable:
/usr/bin/script -qc /bin/bash /dev/null
```
### 步骤 3：Shell 背景
```bash
# Press Ctrl + Z to put the reverse shell into the background
[1]+  Stopped                 rlwrap -cAr nc -lvnp 4444
```
### 步骤 4：配置本地终端原始模式
```bash
# Attacker machine: disable local echo and enable raw keystrokes
stty raw -echo; fg
```
*（运行`fg`后按两次回车即可恢复提示）*

### 步骤 5：重置和设置终端尺寸
```bash
# Target shell: set terminal type and environment
reset
export TERM=xterm-256color
export SHELL=bash

# Check your attacker terminal size in another window:
# $ stty size  -> Output: 38 160

# Apply rows and columns to target shell:
stty rows 38 columns 160
```
> ✅ **结果：** 您现在拥有一个坚不可摧、功能齐全的交互式 bash shell，具有制表符完成功能、清晰的屏幕 (`Ctrl+L`)、信号处理（`Ctrl+C` 不会断开连接）和完整的终端尺寸！

---

## 🔧 将 rlwrap 与其他渗透测试工具一起使用

`rlwrap`不限于Netcat。任何缺乏行编辑的 CLI 工具都可以从中受益：

### Oracle SQL*Plus
```bash
# Oracle's sqlplus utility notoriously lacks line editing and history on Linux
rlwrap sqlplus scott/tiger@10.10.10.50:1521/ORCL
```
### Redis 和 Telnet
```bash
# Redis CLI interactive console
rlwrap redis-cli -h 10.10.10.50 -p 6379

# Raw Telnet or socket interaction
rlwrap telnet 10.10.10.50 25
```
### Socat 和定制插座
```bash
# Socat listener with rlwrap
rlwrap socat file:`tty`,raw,echo=0 tcp-listen:4444

# Connecting to raw sockets / CTF pwn challenges
rlwrap nc ctf.challenge.local 1337
```
---

## ⚡ 快速参考表

|任务|命令 |
|------|---------|
| **增强型 Netcat 监听器** | `rlwrap nc -lvnp 4444` |
| **完整文件和提示标志** | `rlwrap -cAr nc -lvnp 4444` |
| **专用历史文件** | `rlwrap -H ~/.nc_history nc -lvnp 4444` |
| **自定义选项卡完成** | `rlwrap -f /path/to/wordlist.txt nc -lvnp 4444` |
| **增加历史记录大小** | `rlwrap -s 10000 nc -lvnp 4444` |
| **包装 Oracle SQLPlus** | `rlwrap sqlplus user/pass@host/db` |
| **包装 Redis CLI** | `rlwrap redis-cli -h 10.10.10.50` |
| **包装 Socat 监听器** | `rlwrap socat TCP-LISTEN:4444,reuseaddr -` |

---

## 💡 提示和最佳实践

1. **创建 Bash 别名：** 将其添加到您的 `~/.bashrc` 或 `~/.zshrc`，这样您就永远不会忘记使用它：
   ```bash
   alias nc='rlwrap -cAr nc'
   ```
2. **永远不要错过 `Ctrl+C` 安全性：** 当捕获不稳定的 shell 时，`rlwrap` 会拦截基本的键盘故障，但如果您打算运行较长的交互式命令，请始终记住执行 5 步 `stty raw -echo; fg` 升级。
3. **隔离客户端历史记录：** 测试多个客户端或计算机时，始终使用 `-H ~/.client_name.hist` 来维护单独的审核跟踪和命令历史记录。

---

## ⚠️ 法律免责声明

> **重要提示：** 反向 shell 和后利用工具只能在经过明确书面同意的情况下在授权渗透测试、红队参与和教育实验室环境中使用。