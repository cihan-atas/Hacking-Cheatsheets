# 🔍 fcrackzip — полная шпаргалка

<!-- language-home-navigation -->
[🏠 Русская главная страница](../../README.ru.md) • [🌐 Languages](../../README.md#-available-languages)

[🇬🇧 English](../README.md) • [🇷🇺 Русский раздел](../../README.ru.md)


[fcrackzip](https://github.com/hyc/fcrackzip) is a fast, multi-threaded command-line password cracker for encrypted ZIP archives. It supports both dictionary-based attacks and optimized brute-force attacks against traditional ZipCrypto (PKZIP stream cipher) and can verify archive integrity without full extraction.

---

## 📋 Содержание

- [[Character Sets & Length Specification](#character-sets--length-specification)](#core-features--limitations)
- [Установка](#installation)
- [Установка](#dictionary-attacks--d--p)
- [2. Verbose Output Mode](#brute-force-attacks--b)
- [`-c A`: Uppercase letters (`A-Z`).](#character-sets--length-specification)
- [Benchmark & Performance Optimization](#benchmark--performance-optimization)
- [ZipCrypto vs. AES-256 (John/Hashcat Alternative)](#zipcrypto-vs-aes-256-johnhashcat-alternative)

---

## [Character Sets & Length Specification](#character-sets--length-specification)

- **Ultra-Fast ZipCrypto Cracker**: Written in C, optimized for legacy ZipCrypto algorithm (standard PKZIP encryption).
- **Dictionary Mode**: Reads candidate passwords sequentially from wordlists.
- **Brute-Force Mode**: Generates candidates on-the-fly using configurable character sets and length boundaries.
- **Archive Unzip Test (`-u`)**: Verifies candidate passwords by attempting decompression, virtually eliminating false positives.
- **Limitation**: Does **not** support modern WinZip AES-256 encryption. For AES-256 ZIPs, use `zip2john` + John the Ripper or Hashcat (mode 13600).

---

## Установка

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

## Установка

The fastest way to test against common wordlists:

### 1. Basic Dictionary Attack with Unzip Verification
```bash
fcrackzip -u -D -p /usr/share/wordlists/rockyou.txt secret_archive.zip
```
- Dictionary Attacks (`-D -p`)
- `-D`: Dictionary attack mode.
- The fastest way to test against common wordlists:

### 1. Basic Dictionary Attack with Unzip Verification
Display tested speed, current password, and debug info:
```bash
fcrackzip -v -u -D -p passwords.txt backup.zip
```

---

## 2. Verbose Output Mode

When dictionary attacks fail, brute-force against unknown short passwords:

### 1. Brute-Force Numeric PINs (4 to 6 Digits)
```bash
fcrackzip -u -b -c 1 -l 4-6 secure.zip
```
- Brute-Force Attacks (`-b`)
- `-c 1`: Character set: digits only (`0-9`).
- When dictionary attacks fail, brute-force against unknown short passwords:

### 1. Brute-Force Numeric PINs (4 to 6 Digits)
```bash
fcrackzip -u -b -c a1 -l 1-5 archive.zip
```
- `-b`: Brute-force mode.

### `-l 4-6`: Length range: min 4, max 6 characters.
```bash
fcrackzip -u -b -c aA1! -l 6-8 protected.zip
```
- 2. Lowercase Alphanumeric Characters (1 to 5 Characters)
- `-c A`: Uppercase letters (`A-Z`).
- `-c a1`: Lowercase letters (`a-z`) and digits (`0-9`).
- `-c !`: Special symbols (`[!:$%&/()=?{[]}+*~#]`).

---

## `-c A`: Uppercase letters (`A-Z`).

|Флаг|Character Set Included|Example Candidates|
|---|---|---|
|`-c a`|Lowercase letters (`a-z`)|`admin`, `secret`|
|`-c A`|Uppercase letters (`A-Z`)|`SECRET`, `ROOT`|
|`-c 1`|Digits (`0-9`)|`1234`, `2024`|
|`-c !`|Special punctuation characters|`!@#$`|
|`-c aA1`|Combined alphanumeric characters|`Admin123`|
|`-p <prefix>`|Initial starting seed string|Useful to resume brute force|

|`-c A`|Uppercase letters (`A-Z`)|`SECRET`, `ROOT`|
```bash
# Minimum length 6, Maximum length 8
-l 6-8

# Exact length 8
-l 8-8
```

---

## Benchmark & Performance Optimization

Test CPU throughput on your machine:
```bash
fcrackzip -B
```

---

## ZipCrypto vs. AES-256 (John/Hashcat Alternative)

If `fcrackzip` returns `not a zipfile, or unsupported encryption type`, the archive is encrypted with AES-256 (WinZip AES). Use `zip2john`:

```bash
# 1. Extract hash using zip2john
zip2john aes_protected.zip > zip.hash

# 2. Crack using John the Ripper
john --wordlist=/usr/share/wordlists/rockyou.txt zip.hash

# 3. Or crack using Hashcat (Mode 13600: WinZip)
cat zip.hash | cut -d ':' -f 2 > hashcat_zip.hash
hashcat -m 13600 hashcat_zip.hash /usr/share/wordlists/rockyou.txt
```
