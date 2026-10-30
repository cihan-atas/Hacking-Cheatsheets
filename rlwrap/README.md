# ⌨️ rlwrap - Complete Cheatsheet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)

```
  ██████╗ ██╗     ██╗    ██╗██████╗  █████╗ ██████╗ 
  ██╔══██╗██║     ██║    ██║██╔══██╗██╔══██╗██╔══██╗
  ██████╔╝██║     ██║ █╗ ██║██████╔╝███████║██████╔╝
  ██╔══██╗██║     ██║███╗██║██╔══██╗██╔══██║██╔═══╝ 
  ██║  ██║███████╗╚███╔███╔╝██║  ██║██║  ██║██║     
  ╚═╝  ╚═╝╚══════╝ ╚══╝╚══╝ ╚═╝  ╚═╝╚═╝  ╚═╝╚═╝     
       Readline Wrapper for Reverse Shells & Dumb Terminals
```

<p align="center">
  <img src="https://img.shields.io/badge/rlwrap-Readline_Wrapper-blue?style=for-the-badge" alt="rlwrap">
  <img src="https://img.shields.io/badge/Utility-Shell_Stabilization-green?style=for-the-badge" alt="Shell Stabilization">
  <img src="https://img.shields.io/badge/Category-Exploitation_&_Shells-orange?style=for-the-badge" alt="Exploitation & Shells">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="License GPLv2">
</p>

<p align="center">
  <b>🌐 Turn raw Netcat listeners and dumb reverse shells into full command-line interfaces with arrow keys, history, and auto-completion</b>
</p>

---

## 📋 Table of Contents

- [Introduction: The "Dumb Shell" Problem](#-introduction-the-dumb-shell-problem)
- [Installation](#-installation)
- [Basic Usage](#-basic-usage)
- [Catching Reverse Shells (`rlwrap + nc`)](#-catching-reverse-shells-rlwrap--nc)
- [Persistent History Management](#-persistent-history-management)
- [Command Auto-Completion (`-f`)](#-command-auto-completion--f)
- [Full 5-Step TTY Shell Upgrade Workflow](#-full-5-step-tty-shell-upgrade-workflow)
- [Using rlwrap with Other Pentest Tools](#-using-rlwrap-with-other-pentest-tools)
  - [Oracle SQL*Plus](#oracle-sqlplus)
  - [Redis & Telnet](#redis--telnet)
  - [Socat & Custom Sockets](#socat--custom-sockets)
- [Quick Reference Table](#-quick-reference-table)
- [Tips & Best Practices](#-tips--best-practices)
- [Legal Disclaimer](#-legal-disclaimer)

---

## 🎯 Introduction: The "Dumb Shell" Problem

When you catch a reverse shell using traditional tools like Netcat (`nc -lvnp 4444`), you obtain a **dumb terminal**. A dumb terminal lacks standard line-editing capabilities:

- ❌ Pressing the **Up/Down arrow keys** prints escape sequences like `^[[A` or `^[[B` instead of cycling through command history.
- ❌ Pressing **Backspace** or **Delete** prints `^H` or `^?` instead of deleting characters.
- ❌ Pressing **Ctrl + C** immediately kills the reverse shell listener, losing hard-earned access.
- ❌ Commands like `su`, `sudo`, `nano`, `vim`, or `passwd` fail because there is no pseudo-terminal (PTY) attached.
- ❌ No persistent command history is retained across sessions.

**rlwrap** (Readline Wrapper, written by Hans Lub) solves this by sitting between your terminal and the target command. It intercepts keyboard strokes, leverages **GNU Readline**, and provides arrow-key navigation, line editing, persistent command history files, and dictionary-based auto-completion!

---

## 📥 Installation

`rlwrap` is lightweight and available in the default package repositories of virtually every Linux distribution:

### Kali Linux, Debian & Ubuntu

```bash
# Update and install rlwrap
sudo apt update && sudo apt install -y rlwrap

# Verify installation and version
rlwrap --version
```

### Fedora, CentOS & RHEL

```bash
# Fedora / CentOS / RHEL
sudo dnf install -y rlwrap || sudo yum install -y epel-release && sudo yum install -y rlwrap
```

### Arch Linux

```bash
# Arch Linux / Manjaro
sudo pacman -S rlwrap
```

### macOS (Homebrew)

```bash
# macOS
brew install rlwrap
```

---

## 🚀 Basic Usage

The basic syntax is simply wrapping any command line with `rlwrap`:

```bash
# General syntax
rlwrap [options] <command> [command-arguments]
```

### Common Flags

| Flag | Description |
|------|-------------|
| `-c` | Enable file and path name completion |
| `-A` | Ignore prompt formatting escape codes (prevents prompt line glitches) |
| `-r` | Remember all words entered on input and put them into the completion wordlist |
| `-H <file>` | Specify a dedicated history file for saving and recalling commands |
| `-s <num>` | Set the maximum number of history lines (default: 300) |
| `-f <file>` | Load a custom completion dictionary file |
| `-b <chars>` | Define word-break characters |
| `-m` | Enable multi-line input editing |
| `-p[color]` | Colorize the command prompt |

---

## 🎣 Catching Reverse Shells (`rlwrap + nc`)

The most common use case in offensive security is wrapping a Netcat listener (see the [Netcat Cheatsheet](../Netcat/README.md) for full listener variants and flags):

```bash
# Basic Netcat listener with arrow-key editing & history
rlwrap nc -lvnp 4444

# Professional pentester flags: -c (file completion), -A (prompt fix), -r (remember words)
rlwrap -cAr nc -lvnp 4444
```

### Explanation of the `-cAr` Flags:

1. **`-c` (File Completion):** Allows using `<TAB>` to auto-complete file and directory names.
2. **`-A` (Prompts):** Prevents weird line wraps and prompt duplication when the remote server sends unstandardized prompt strings (e.g. `root@victim:/#`).
3. **`-r` (Remember Words):** Learns words you type into the shell so you can tab-complete them later.

---

## 💾 Persistent History Management

By default, `rlwrap` saves command history in `~/.<command>_history`. You can configure custom, per-engagement history files:

```bash
# Save history to a dedicated engagement file
rlwrap -H ~/.history_target_corp nc -lvnp 4444

# Increase history size to 5,000 entries
rlwrap -H ~/.history_target -s 5000 nc -lvnp 4444

# Case-insensitive history search
rlwrap -i nc -lvnp 4444
```

---

## 🔍 Command Auto-Completion (`-f`)

You can provide `rlwrap` with a text file containing common Linux or Windows commands, and it will auto-complete them when you press `<TAB>`:

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

## 🛠️ Full 5-Step TTY Shell Upgrade Workflow

While `rlwrap` provides immediate arrow-key navigation and history, certain interactive programs (`su`, `sudo`, `vim`) still require a fully allocated TTY terminal. Here is the industry-standard 5-step upgrade pipeline:

### Step 1: Start Listener with rlwrap

```bash
# Attacker machine: start enhanced listener
rlwrap -cAr nc -lvnp 4444
```

### Step 2: Trigger Reverse Shell & Spawn PTY

Once your reverse shell connects to your listener, spawn a pseudo-terminal on the target:

```bash
# Target shell: Spawn PTY via Python
python3 -c 'import pty; pty.spawn("/bin/bash")'

# OR via script if Python is unavailable:
/usr/bin/script -qc /bin/bash /dev/null
```

### Step 3: Background the Shell

```bash
# Press Ctrl + Z to put the reverse shell into the background
[1]+  Stopped                 rlwrap -cAr nc -lvnp 4444
```

### Step 4: Configure Local Terminal Raw Mode

```bash
# Attacker machine: disable local echo and enable raw keystrokes
stty raw -echo; fg
```

*(Press Enter twice after running `fg` to restore the prompt)*

### Step 5: Reset & Set Terminal Dimensions

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

> ✅ **Result:** You now have an indestructible, full-featured interactive bash shell with tab-completion, clear screen (`Ctrl+L`), signal handling (`Ctrl+C` does not disconnect), and full terminal dimensions!

---

## 🔧 Using rlwrap with Other Pentest Tools

`rlwrap` is not limited to Netcat. Any CLI tool lacking line editing benefits from it:

### Oracle SQL*Plus

```bash
# Oracle's sqlplus utility notoriously lacks line editing and history on Linux
rlwrap sqlplus scott/tiger@10.10.10.50:1521/ORCL
```

### Redis & Telnet

```bash
# Redis CLI interactive console
rlwrap redis-cli -h 10.10.10.50 -p 6379

# Raw Telnet or socket interaction
rlwrap telnet 10.10.10.50 25
```

### Socat & Custom Sockets

```bash
# Socat listener with rlwrap
rlwrap socat file:`tty`,raw,echo=0 tcp-listen:4444

# Connecting to raw sockets / CTF pwn challenges
rlwrap nc ctf.challenge.local 1337
```

---

## ⚡ Quick Reference Table

| Task | Command |
|------|---------|
| **Enhanced Netcat Listener** | `rlwrap nc -lvnp 4444` |
| **Full File & Prompt Flags** | `rlwrap -cAr nc -lvnp 4444` |
| **Dedicated History File** | `rlwrap -H ~/.nc_history nc -lvnp 4444` |
| **Custom Tab-Completion** | `rlwrap -f /path/to/wordlist.txt nc -lvnp 4444` |
| **Increase History Size** | `rlwrap -s 10000 nc -lvnp 4444` |
| **Wrap Oracle SQLPlus** | `rlwrap sqlplus user/pass@host/db` |
| **Wrap Redis CLI** | `rlwrap redis-cli -h 10.10.10.50` |
| **Wrap Socat Listener** | `rlwrap socat TCP-LISTEN:4444,reuseaddr -` |

---

## 💡 Tips & Best Practices

1. **Create a Bash Alias:** Add this to your `~/.bashrc` or `~/.zshrc` so you never forget to use it:
   ```bash
   alias nc='rlwrap -cAr nc'
   ```
2. **Never Miss `Ctrl+C` Safety:** When catching unstable shells, `rlwrap` intercepts basic keyboard glitches, but always remember to do the 5-step `stty raw -echo; fg` upgrade if you plan on running long interactive commands.
3. **Isolate Client Histories:** When testing multiple clients or machines, always use `-H ~/.client_name.hist` to maintain separate audit trails and command histories.

---

## ⚠️ Legal Disclaimer

> **IMPORTANT:** Reverse shells and post-exploitation tools must only be used in authorized penetration tests, red team engagements, and educational lab environments with explicit written consent.
