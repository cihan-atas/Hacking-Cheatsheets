# 🔢 Crunch - Complete Cheatsheet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)

```
   ██████╗██████╗ ██╗   ██╗███╗   ██╗ ██████╗██╗  ██╗
  ██╔════╝██╔══██╗██║   ██║████╗  ██║██╔════╝██║  ██║
  ██║     ██████╔╝██║   ██║██╔██╗ ██║██║     ███████║
  ██║     ██╔══██╗██║   ██║██║╚██╗██║██║     ██╔══██║
  ╚██████╗██║  ██║╚██████╔╝██║ ╚████║╚██████╗██║  ██║
   ╚═════╝╚═╝  ╚═╝ ╚═════╝ ╚═╝  ╚═══╝ ╚═════╝╚═╝  ╚═╝
       High-Speed Pattern-Based Wordlist & Character Matrix Generator
```

<p align="center">
  <img src="https://img.shields.io/badge/Crunch-Wordlist_Generator-blue?style=for-the-badge" alt="Crunch">
  <img src="https://img.shields.io/badge/Language-C-orange?style=for-the-badge" alt="Language C">
  <img src="https://img.shields.io/badge/Category-Password_Cracking-green?style=for-the-badge" alt="Password Cracking">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="License GPLv2">
</p>

<p align="center">
  <b>🌐 Wordlist generator that generates all possible combinations of character sets using custom lengths, patterns, and symbols</b>
</p>

---

## 📋 Table of Contents

- [Overview & The Permutation Engine](#-overview--the-permutation-engine)
- [Installation](#-installation)
- [Basic Syntax & Character Sets](#-basic-syntax--character-sets)
- [Pattern Matching & Placeholders (`-t`)](#-pattern-matching--placeholders--t)
- [Using System Charsets (`charset.lst`)](#-using-system-charsets-charsetlst)
- [Duplicate Character Limiting (`-d`)](#-duplicate-character-limiting--d)
- [Splitting Wordlists into Chunks (`-b` & `-c`)](#-splitting-wordlists-into-chunks--b---c)
- [Piping Directly into Attack Tools (Zero Disk Space)](#-piping-directly-into-attack-tools-zero-disk-space)
- [Quick Reference Table](#-quick-reference-table)
- [Defensive Considerations & Calculation Warnings](#-defensive-considerations--calculation-warnings)
- [Legal Disclaimer](#-legal-disclaimer)

---

## 🎯 Overview & The Permutation Engine

When attacking known password patterns (e.g., corporate PINs, phone numbers, or passwords conforming to standard complexity rules like `Word2026!`), standard static dictionaries like `rockyou.txt` are either missing the target or inefficient.

Written in C by **bofheaded**, **Crunch** is a blazingly fast wordlist generator. It calculates permutations mathematically, allowing you to define exact character positions, lengths, character pools, and duplication constraints.

---

## 📥 Installation

```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y crunch

# Arch Linux (AUR)
yay -S crunch
```

---

## 🚀 Basic Syntax & Character Sets

The core syntax requires minimum length, maximum length, and optionally an alphabet:

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

## 🎭 Pattern Matching & Placeholders (`-t`)

The `-t` flag allows fixing known characters while fuzzing the unknown slots using special placeholders:

| Placeholder | Character Type |
|-------------|----------------|
| `@` | Lowercase letters (`a-z`) |
| `,` | Uppercase letters (`A-Z`) |
| `%` | Numbers (`0-9`) |
| `^` | Special symbols / punctuation (`!@#$%^&*...`) |

### Practical Pattern Examples:

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

## 📚 Using System Charsets (`charset.lst`)

Crunch bundles pre-configured character matrices in `/usr/share/crunch/charset.lst`:

```bash
# Use pre-defined alphanumeric set (mixalpha-numeric)
crunch 6 6 -f /usr/share/crunch/charset.lst mixalpha-numeric -o out.txt

# Use hexadecimal lowercase characters (0-9, a-f)
crunch 8 8 -f /usr/share/crunch/charset.lst hex-lower -o hex_keys.txt
```

---

## 🚫 Duplicate Character Limiting (`-d`)

Enforce limits on consecutive duplicate characters to weed out unlikely passwords:

```bash
# Generate 8-character lowercase words with at most 2 consecutive identical letters
# (e.g., prevents 'aaabbb' while allowing 'aab')
crunch 8 8 abcdefghijklmnopqrstuvwxyz -d 2@ -o deduped.txt

# Limit consecutive numbers to at most 1 (prevents '11' or '22')
crunch 6 6 -t %%%%%% -d 1% -o no_repeated_digits.txt
```

---

## 📦 Splitting Wordlists into Chunks (`-b` & `-c`)

Prevent multi-terabyte files from exhausting disk space by splitting them into manageable volumes:

```bash
# Split wordlist into 500MB chunks
crunch 8 8 -f /usr/share/crunch/charset.lst mixalpha-numeric -b 500mb -o START

# Split by line count (every 1,000,000 lines)
crunch 8 8 0123456789 -c 1000000 -o START
```

---

## ⚡ Piping Directly into Attack Tools (Zero Disk Space)

Never write gigantic wordlists to disk when you can stream permutations directly into cracking tools:

```bash
# Stream 8-digit numeric PINs straight into Aircrack-ng
crunch 8 8 0123456789 | aircrack-ng -w - -b 00:11:22:33:44:55 capture.cap

# Stream policy passwords directly into Hydra SSH brute-force
crunch 8 8 -t Admin%%^ | hydra -l admin -P - 10.10.10.50 ssh

# Stream into John the Ripper
crunch 6 6 0123456789 | john --stdin --format=raw-md5 hashes.txt
```

---

## ⚡ Quick Reference Table

| Objective | Command |
|-----------|---------|
| **Numeric PINs (4-digit)** | `crunch 4 4 0123456789 -o pins.txt` |
| **Pattern: Capital + Word + Numbers**| `crunch 8 8 -t ,@@@@%%^ -o passwords.txt` |
| **Pattern: Specific Prefix** | `crunch 10 10 -t Company%%% -o wordlist.txt` |
| **Use Charset File** | `crunch 6 6 -f charset.lst mixalpha-numeric -o out.txt` |
| **Limit Consecutive Identical**| `crunch 8 8 -t @@@@@@@@ -d 2@ -o out.txt` |
| **Split into 1GB Files** | `crunch 8 8 <CHARS> -b 1gb -o START` |
| **Pipe to Aircrack-ng** | `crunch 8 8 0123456789 \| aircrack-ng -w - target.cap` |
| **Pipe to Hydra** | `crunch 7 7 -t User%%% \| hydra -l user -P - <IP> ssh` |

---

## 🛡️ Defensive Considerations & Calculation Warnings

> [!WARNING]
> **Permutation Explosions:** Always check Crunch's projected output size before confirming disk generation. An unconstrained 8-character alphanumeric wordlist produces **over 200 Terabytes** of text data! Pipe output whenever possible.

---

## ⚠️ Legal Disclaimer

> [!CAUTION]
> Crunch is intended for authorized password recovery, penetration testing, and security auditing only. Brute-forcing passwords on unauthorized systems is strictly illegal.