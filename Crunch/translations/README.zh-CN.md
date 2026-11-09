<!-- language-home-navigation -->
[🏠 中文主页](../../README.zh-CN.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🔢 Crunch - 完整备忘单

[🇹🇷 Türkçe Sürüm]（翻译/README.tr.md）
```
   ██████╗██████╗ ██╗   ██╗███╗   ██╗ ██████╗██╗  ██╗
  ██╔════╝██╔══██╗██║   ██║████╗  ██║██╔════╝██║  ██║
  ██║     ██████╔╝██║   ██║██╔██╗ ██║██║     ███████║
  ██║     ██╔══██╗██║   ██║██║╚██╗██║██║     ██╔══██║
  ╚██████╗██║  ██║╚██████╔╝██║ ╚████║╚██████╗██║  ██║
   ╚═════╝╚═╝  ╚═╝ ╚═════╝ ╚═╝  ╚═══╝ ╚═════╝╚═╝  ╚═╝
       High-Speed Pattern-Based Wordlist & Character Matrix Generator
```
<p对齐=“中心”>
  <img src="https://img.shields.io/badge/Crunch-Wordlist_Generator-blue?style=for-the-badge" alt="Crunch">
  <img src="https://img.shields.io/badge/Language-C-orange?style=for-the-badge" alt="语言 C">
  <img src="https://img.shields.io/badge/Category-Password_Cracking-green?style=for-the-badge" alt="密码破解">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="许可证 GPLv2">
</p>

<p对齐=“中心”>
  <b>🌐 单词列表生成器，使用自定义长度、模式和符号生成所有可能的字符集组合</b>
</p>

---

## 📋 目录

- [概述和排列引擎](#-overview--the-permutation-engine)
- [安装](#-安装)
- [基本语法和字符集](#-basic-syntax--character-sets)
- [模式匹配和占位符 (`-t`)](#-pattern-matching--placeholders--t)
- [使用系统字符集 (`charset.lst`)](#-using-system-charsets-charsetlst)
- [重复字符限制 (`-d`)](#-duplicate-character-limiting--d)
- [将单词列表拆分为块（`-b` 和 `-c`）](#-splitting-wordlists-into-chunks--b---c)
- [直接通过管道连接到攻击工具（零磁盘空间）](#-piping-directly-into-attack-tools-zero-disk-space)
- [快速参考表](#-quick-reference-table)
- [防御考虑和计算警告](#-defective-considerations--calculation-warnings)
- [法律免责声明](#-legal-disclaimer)

---

## 🎯 概述和排列引擎

当攻击已知的密码模式（例如，公司 PIN、电话号码或符合 `Word2026!` 等标准复杂性规则的密码）时，`rockyou.txt` 等标准静态字典要么无法达到目标，要么效率低下。

**Crunch** 由 **bofheaded** 用 C 语言编写，是一个速度极快的单词列表生成器。它以数学方式计算排列，允许您定义精确的字符位置、长度、字符池和重复约束。

---

## 📥 安装
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y crunch

# Arch Linux (AUR)
yay -S crunch
```
---

## 🚀 基本语法和字符集

核心语法需要最小长度、最大长度和可选的字母表：
```bash
# crunch <min> <max> [characterset] [options]

# Generate all 4-digit numeric PINs (0000 - 9999)
crunch 4 4 0123456789 -o 4digit_pins.txt

# Generate 5-character words using specific custom letters
crunch 5 5 abcdef -o custom_words.txt

# Generate lowercase alphabet combinations from length 3 to 4
crunch 3 4 abcdefghijklmnopqrstuvwxyz -o alpha_3_4.txt
```
---

## 🎭 模式匹配和占位符 (`-t`)

`-t` 标志允许修复已知字符，同时使用特殊占位符模糊未知槽：

|占位符|字符类型 |
|-------------|----------------|
| `@` |小写字母 (`a-z`) |
| `,` |大写字母 (`A-Z`) |
| `%` |数字 (`0-9`) |
| `^` |特殊符号/标点符号 (`!@#$%^&*...`) |

### 实用模式示例：
```bash
# Generate Turkish phone numbers (e.g., 0532XXXXXXX)
crunch 11 11 -t 0532%%%%%%% -o phone_numbers.txt

# Generate passwords conforming to: Capital letter + 4 lowercase + 2 digits + 1 symbol
# e.g., Admin24! or Super99#
crunch 8 8 -t ,@@@@%%^ -o policy_passwords.txt

# Target known prefix: Spring2026! or Spring2026#
crunch 11 11 -t Spring2026^ -o spring_passwords.txt
```
---

## 📚 使用系统字符集 (`charset.lst`)

Crunch 捆绑了 `/usr/share/crunch/charset.lst` 中预配置的字符矩阵：
```bash
# Use pre-defined alphanumeric set (mixalpha-numeric)
crunch 6 6 -f /usr/share/crunch/charset.lst mixalpha-numeric -o out.txt

# Use hexadecimal lowercase characters (0-9, a-f)
crunch 8 8 -f /usr/share/crunch/charset.lst hex-lower -o hex_keys.txt
```
---

## 🚫 重复字符限制 (`-d`)

对连续重复字符实施限制以清除不太可能的密码：
```bash
# Generate 8-character lowercase words with at most 2 consecutive identical letters
# (e.g., prevents 'aaabbb' while allowing 'aab')
crunch 8 8 abcdefghijklmnopqrstuvwxyz -d 2@ -o deduped.txt

# Limit consecutive numbers to at most 1 (prevents '11' or '22')
crunch 6 6 -t %%%%%% -d 1% -o no_repeated_digits.txt
```
---

## 📦 将单词列表拆分为块（`-b` 和 `-c`）

通过将多 TB 文件拆分为可管理的卷，防止它们耗尽磁盘空间：
```bash
# Split wordlist into 500MB chunks
crunch 8 8 -f /usr/share/crunch/charset.lst mixalpha-numeric -b 500mb -o START

# Split by line count (every 1,000,000 lines)
crunch 8 8 0123456789 -c 1000000 -o START
```
---

## ⚡ 直接通过管道连接到攻击工具（零磁盘空间）

当您可以将排列直接流式传输到破解工具中时，切勿将巨大的单词列表写入磁盘：
```bash
# Stream 8-digit numeric PINs straight into Aircrack-ng
crunch 8 8 0123456789 | aircrack-ng -w - -b 00:11:22:33:44:55 capture.cap

# Stream policy passwords directly into Hydra SSH brute-force
crunch 8 8 -t Admin%%^ | hydra -l admin -P - 10.10.10.50 ssh

# Stream into John the Ripper
crunch 6 6 0123456789 | john --stdin --format=raw-md5 hashes.txt
```
---

## ⚡ 快速参考表

|目标|命令 |
|------------|---------|
| **数字 PIN（4 位数字）** | `crunch 4 4 0123456789 -o pins.txt` |
| **模式：大写+单词+数字**| `crunch 8 8 -t ,@@@@%%^ -o passwords.txt` |
| **模式：特定前缀** | `crunch 10 10 -t Company%%% -o wordlist.txt` |
| **使用字符集文件** | `crunch 6 6 -f charset.lst mixalpha-numeric -o out.txt` |
| **限制连续相同**| `crunch 8 8 -t @@@@@@@@ -d 2@ -o out.txt` |
| **分割成 1GB 文件** | `crunch 8 8 <CHARS> -b 1gb -o START` |
| **管道到 Aircrack-ng** | `crunch 8 8 0123456789 \| aircrack-ng -w - target.cap` |
| **管道至九头蛇** | `crunch 7 7 -t User%%% \| hydra -l user -P - <IP> ssh` |

---

## 🛡️ 防守注意事项和计算警告

> [!警告]
> **排列爆炸：** 在确认磁盘生成之前始终检查 Crunch 的预计输出大小。不受约束的 8 字符字母数字单词列表可生成 **超过 200 TB** 的文本数据！尽可能通过管道输出。

---

## ⚠️ 法律免责声明

> [!警告]
> Crunch 仅适用于授权密码恢复、渗透测试和安全审核。在未经授权的系统上暴力破解密码是严格非法的。