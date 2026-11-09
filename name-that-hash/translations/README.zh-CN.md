<!-- language-home-navigation -->
[🏠 中文主页](../../README.zh-CN.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# Name-That-Hash（第 n 个）备忘单

[Name-That-Hash](https://github.com/HashPals/Name-That-Hash) (`nth`) 是一种现代化的超快速哈希识别引擎，可以准确识别未知哈希、文本或文件中嵌入的哈希，并输出直接 Hashcat (`-m`) 和 John the Ripper (`--format`) 模式。它会自动解码 Base64 和 Hex 等嵌套编码。

---

## 目录

- [主要特点](#key-features)
- [安装](#安装)
- [CLI 哈希识别](#cli-hash-identification)
- [多重哈希和文件处理](#多重哈希--文件处理)
- [Hashcat 和 John the Ripper 格式输出](#hashcat--john-the-ripper-format-output)
- [自动解码和流行度过滤器](#auto-decode--popularity-filters)
- [与破解管道集成](#integration-with-cracking-pipelines)

---

## 主要特点

- **300 多种哈希类型：** 涵盖现代算法、Active Directory 格式、数据库哈希、CMS 签名和深奥密码。
- **Hashcat 和 John Flag 映射：** 立即显示 `-m <mode>` 和 `--format=<format>` 值。
- **自动解码：** 在计算哈希类型之前剥离 Base64、十六进制和 URL 编码。
- **子字符串/文本搜索：**查找隐藏在日志、内存转储或配置文件中的哈希值。
- **流行度排序：** 按实际使用概率对结果进行排名。

 - -

＃＃ 安装
```bash
# Via pipx (recommended for isolated CLI tools)
pipx install name-that-hash

# Via standard Python pip
python3 -m pip install --user name-that-hash

# Kali Linux / Debian repository
sudo apt-get update && sudo apt-get install -y python3-name-that-hash

# Verify installation
nth --version
```
---

## CLI 哈希识别

### 1. 识别单个哈希值
```bash
nth -t "5d41402abc4b2a76b9719d911017c592"
```
### 2.直接识别Hashcat和John格式模式
只显示支持Hashcat或John破解模式的哈希类型：
```bash
nth -t "b8c37e33defde51cf91e1e03e51657da" --hashcat --john
```
### 3. 显示详细信息（描述、速度、示例）
```bash
nth -t "$6$rounds=5000$usesomesalt$..." -v
```
---

## 多重哈希和文件处理

### 1. 识别文件内的哈希值（逐行）
```bash
nth -f /path/to/hashes.txt
```
### 2. 仅筛选最热门哈希值的结果
通过按受欢迎程度进行过滤，减少模糊哈希类型的误报：
```bash
# Level 1: Most popular hashes only (MD5, SHA1, SHA256, NTLM, bcrypt, etc.)
nth -f hashes.txt --popularity 1
```
### 3. 搜索并提取嵌入在非结构化文本中的哈希值
扫描原始内存字符串、日志文件或源代码以获取哈希值：
```bash
nth --text "Found hash in config: 098f6bcd4621d373cade4e832627b4f6 on port 8080"
```
---

## Hashcat 和 John the Ripper 格式输出

`nth` 使得将哈希值输入破解程序变得很简单，而无需记住模式编号：

|示例哈希格式 |检测算法 | Hashcat 模式 (`-m`) |约翰格式 (`--format`) |
|---|---|---|---|
| `e80b5017098950fc58aad83c8c14978e` | MD5 | `0` | `raw-md5` |
| `b4b9b02e6f09a9bd760f388b67351e2b` | NTLM | `1000` | `nt` |
| `284ddc0d513372c7d764a0ff813473f2...` | SHA-256 | `1400`| `raw-sha256`|
| `$2a$12$DOW8W6...` | bcrypt | `3200` | `bcrypt` |
| `$6$salt$hash...`| SHA-512（Unix）| `1800`| `sha512crypt`|
| `$krb5tgs$23$*...`| Kerberoast (TGS-REP) | `13100` | `krb5tgs`|
| `$krb5asrep$23$...` | AS-REP 烘焙 | `18200` | `krb5asrep` |

---

## 自动解码和流行度过滤器

许多应用程序在数据库插入之前将哈希值存储为 Base64 或 Hex。 `nth` 可以自动反转：
```bash
# Decode Base64 hash before checking
nth -t "XUkQS8xLJnY5cZ0REXxFkg==" --base64

# Decode hex encoded string before checking
nth -t "3564343134303261626334623261373662393731396439313130313763353932" --hex
```
---

## Integration with Cracking Pipelines

使用自动识别快速将未知哈希传送到 Hashcat：
```bash
# 1. Check hash and capture Hashcat mode
HASH="b4b9b02e6f09a9bd760f388b67351e2b"
MODE=$(nth -t "$HASH" --hashcat --popularity 1 | grep -oP '(?<=Hashcat: -m )\d+' | head -n 1)

# 2. Launch Hashcat with identified mode
hashcat -m "$MODE" -a 0 "$HASH" /usr/share/wordlists/rockyou.txt
```