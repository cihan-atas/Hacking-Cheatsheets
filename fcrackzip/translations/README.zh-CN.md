<!-- language-home-navigation -->
[🏠 中文主页](../../README.zh-CN.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# fcrackzip 备忘单

[fcrackzip](https://github.com/hyc/fcrackzip) 是一个快速、多线程的命令行密码破解器，用于加密的 ZIP 档案。它支持基于字典的攻击和针对传统 ZipCrypto（PKZIP 流密码）的优化暴力攻击，并且可以在不完全提取的情况下验证存档完整性。

---

## 目录

- [核心功能和限制](#core-features--limitations)
- [安装](#安装)
- [字典攻击 (`-D -p`)](#dictionary-attacks--d--p)
- [暴力攻击 (`-b`)](#brute-force-attacks--b)
- [字符集和长度规范](#character-sets--length-specification)
- [基准测试和性能优化](#benchmark--performance-optimization)
- [ZipCrypto 与 AES-256（John/Hashcat 替代方案）](#zipcrypto-vs-aes-256-johnhashcat-alternative)

---

## 核心特性和限制

- **超快速 ZipCrypto 破解器：** 用 C 语言编写，针对传统 ZipCrypto 算法（标准 PKZIP 加密）进行了优化。
- **字典模式：** 从单词列表中顺序读取候选密码。
- **暴力模式：** 使用可配置的字符集和长度边界即时生成候选。
- **存档解压缩测试 (`-u`)：** 通过尝试解压缩来验证候选密码，几乎消除误报。
- **限制：** **不** 支持现代 WinZip AES-256 加密。对于 AES-256 ZIP，请使用 `zip2john` + John the Ripper 或 Hashcat（模式 13600）。

 - -

＃＃ 安装
```bash
# Debian / Ubuntu / Kali Linux
sudo apt-get update && sudo apt-get install -y fcrackzip

# Arch Linux / BlackArch
sudo pacman -S fcrackzip

# Fedora / CentOS / RHEL
sudo dnf install -y fcrackzip

# Verify command
fcrackzip -h
```
---

## 字典攻击 (`-D -p`)

测试常见单词列表的最快方法：

### 1. 带有解压缩验证的基本字典攻击
```bash
fcrackzip -u -D -p /usr/share/wordlists/rockyou.txt secret_archive.zip
```
- `-u`：解压缩模式（验证提取的文件 CRC 以避免误报）。
- `-D`：字典攻击模式。
- `-p <file>`：字典文件的路径。

### 2.详细输出模式
显示测试速度、当前密码和调试信息：
```bash
fcrackzip -v -u -D -p passwords.txt backup.zip
```
---

## 暴力攻击 (`-b`)

当字典攻击失败时，对未知的短密码进行暴力破解：

### 1. 暴力破解数字 PIN（4 到 6 位数字）
```bash
fcrackzip -u -b -c 1 -l 4-6 secure.zip
```
- `-b`：暴力模式。
- `-c 1`：字符集：仅限数字 (`0-9`)。
- `-l 4-6`：长度范围：最少 4 个字符，最多 6 个字符。

### 2. 小写字母数字字符（1 到 5 个字符）
```bash
fcrackzip -u -b -c a1 -l 1-5 archive.zip
```
- `-c a1`：小写字母 (`a-z`) 和数字 (`0-9`)。

### 3.小写+大写+数字+符号
```bash
fcrackzip -u -b -c aA1! -l 6-8 protected.zip
```
- `-c a`：小写字母 (`a-z`)。
- `-c A`：大写字母 (`A-Z`)。
- `-c 1`：数字（`0-9`）。
- `-c !`：特殊符号（`[!:$%&/()=?{[]}+*~#]`）。

---

## 字符集和长度规范

|旗帜|包含的字符集 |候选人示例 |
|---|---|---|
| `-c a` |小写字母 (`a-z`) | `admin`、`secret` |
| `-c A` |大写字母 (`A-Z`) | `SECRET`、`ROOT` |
| `-c 1`|数字 (`0-9`) | `1234`、`2024` |
| `-c !`|特殊标点符号 | `!@#$` |
| `-c aA1` |组合字母数字字符 | `Admin123` |
| `-p <prefix>` |初始起始种子字符串 |有用恢复暴力破解 |

指定长度边界：
```bash
# Minimum length 6, Maximum length 8
-l 6-8

# Exact length 8
-l 8-8
```
---

## 基准测试和性能优化

测试您机器上的 CPU 吞吐量：
```bash
fcrackzip -B
```
---

## ZipCrypto 与 AES-256（John/Hashcat 替代方案）

如果 `fcrackzip` 返回 `not a zipfile, or unsupported encryption type`，则说明存档已使用 AES-256 (WinZip AES) 加密。使用`zip2john`：
```bash
# 1. Extract hash using zip2john
zip2john aes_protected.zip > zip.hash

# 2. Crack using John the Ripper
john --wordlist=/usr/share/wordlists/rockyou.txt zip.hash

# 3. Or crack using Hashcat (Mode 13600: WinZip)
cat zip.hash | cut -d ':' -f 2 > hashcat_zip.hash
hashcat -m 13600 hashcat_zip.hash /usr/share/wordlists/rockyou.txt
```