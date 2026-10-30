# 🕷️ CeWL - Complete Cheatsheet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)

```
   ██████╗███████╗██╗    ██╗██╗     
  ██╔════╝██╔════╝██║    ██║██║     
  ██║     █████╗  ██║ █╗ ██║██║     
  ██║     ██╔══╝  ██║███╗██║██║     
  ╚██████╗███████╗╚███╔███╔╝███████╗
   ╚═════╝╚══════╝ ╚══╝╚══╝ ╚══════╝
       Custom Word List Generator by Robin Wood (DigiNinja)
```

<p align="center">
  <img src="https://img.shields.io/badge/CeWL-Custom_Wordlist_Generator-blue?style=for-the-badge" alt="CeWL">
  <img src="https://img.shields.io/badge/Author-DigiNinja-orange?style=for-the-badge" alt="DigiNinja">
  <img src="https://img.shields.io/badge/Category-Password_Cracking-green?style=for-the-badge" alt="Password Cracking">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="License GPLv2">
</p>

<p align="center">
  <b>🌐 Target-specific password dictionary generator that spiders websites to harvest specialized corporate terminology, brand names, and employee emails</b>
</p>

---

## 📋 Table of Contents

- [Overview & The Targeted Password Philosophy](#-overview--the-targeted-password-philosophy)
- [Installation](#-installation)
- [Basic Website Crawling](#-basic-website-crawling)
- [Spider Depth & Word Length Controls](#-spider-depth--word-length-controls)
- [Harvesting Email Addresses (`-e`)](#-harvesting-email-addresses--e)
- [Including Numbers & Lowercasing](#-including-numbers--lowercasing)
- [Document Metadata Extraction (`-a`)](#-document-metadata-extraction--a)
- [Word Count & Frequency Profiling (`-c`)](#-word-count--frequency-profiling--c)
- [Chaining CeWL with Hashcat & John Rules](#-chaining-cewl-with-hashcat--john-rules)
- [Quick Reference Table](#-quick-reference-table)
- [Defensive Considerations & OPSEC](#-defensive-considerations--opsec)
- [Legal Disclaimer](#-legal-disclaimer)

---

## 🎯 Overview & The Targeted Password Philosophy

Generic wordlists like `rockyou.txt` are effective for consumer passwords, but **they frequently fail against corporate password policies** requiring 12+ characters, special symbols, and seasonal rotations.

Employees routinely base their enterprise passwords on:
- Company project names, internal codenames, and slogans (`Pegasus2026!`).
- Specific industry terminology, technical jargon, or product brandings.
- Executive names, location landmarks, and office addresses.

Created by **Robin Wood (DigiNinja)**, **CeWL** (Custom Word List generator) is a Ruby spider that crawls a specified target URL to a defined depth, extracts unique words matching minimum length requirements, and builds an engagement-tailored password dictionary.

---

## 📥 Installation

```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y cewl

# Arch Linux (AUR)
yay -S cewl

# Ruby Gem
gem install cewl
```

---

## 🚀 Basic Website Crawling

```bash
# Crawl website and output words to console
cewl https://target.local

# Crawl and save to a custom wordlist file
cewl https://target.local -w company_wordlist.txt
```

---

## 🕸️ Spider Depth & Word Length Controls

Control how deeply CeWL follows links and what word sizes it captures:

```bash
# Crawl 2 links deep (-d 2) and enforce minimum word length of 8 characters (-m 8)
cewl https://target.local -d 2 -m 8 -w wordlist_min8.txt

# Aggressive 3-level deep crawl for comprehensive corporate terminology
cewl https://target.local -d 3 -m 7 --no-words -w deep_crawl.txt
```

---

## 📧 Harvesting Email Addresses (`-e`)

Extract corporate email addresses for subsequent phishing simulations or credential spraying:

```bash
# Extract emails to a separate dedicated file
cewl https://target.local -e --email_file emails.txt -w words.txt
```

---

## 🔡 Including Numbers & Lowercasing

```bash
# Include words containing alphanumeric combinations (e.g., product names like V300, X86)
cewl https://target.local --with-numbers -m 6 -w words_with_numbers.txt

# Convert all discovered words to lowercase (great for rule generation)
cewl https://target.local --lowercase -w lowercase_words.txt
```

---

## 📄 Document Metadata Extraction (`-a`)

Extract author and creator usernames from downloaded PDF and Microsoft Office documents:

```bash
# Download and analyze document metadata to extract employee usernames
cewl https://target.local -a --meta_file extracted_authors.txt
```

---

## 📊 Word Count & Frequency Profiling (`-c`)

Sort words by frequency to identify the most pervasive corporate buzzwords:

```bash
# Append occurrence count next to each word
cewl https://target.local -c -m 6 -w frequency.txt

# Sort by most frequent words in descending order
sort -k2 -nr frequency.txt | head -n 30
```

---

## ⚡ Chaining CeWL with Hashcat & John Rules

A raw CeWL wordlist contains base dictionary roots. To produce viable passwords, expand the wordlist using **Hashcat / John the Ripper mutation rules**:

```bash
# 1. Harvest base words from corporate website
cewl https://target.local -m 5 --lowercase -w base_words.txt

# 2. Mutate words using Hashcat's best64 rule set
hashcat --force base_words.txt -r /usr/share/hashcat/rules/best64.rule --stdout | sort -u > mutated_passwords.txt

# Example mutations produced:
# Acmetool -> Acmetool2026!, !Acmetool123, 4cmet00l, Acmetool#1
```

---

## ⚡ Quick Reference Table

| Objective | Command |
|-----------|---------|
| **Basic Crawl to File** | `cewl <URL> -w wordlist.txt` |
| **Set Min Length & Depth** | `cewl <URL> -m 8 -d 2 -w wordlist.txt` |
| **Extract Corporate Emails**| `cewl <URL> -e --email_file emails.txt` |
| **Extract Document Metadata**| `cewl <URL> -a --meta_file authors.txt` |
| **Include Alphanumeric Words**| `cewl <URL> --with-numbers -w wordlist.txt` |
| **Lowercase Output** | `cewl <URL> --lowercase -w wordlist.txt` |
| **Count Word Frequencies** | `cewl <URL> -c -w frequencies.txt` |
| **Authenticate Behind Login**| `cewl <URL> -a "Cookie: session=xyz"` |

---

## 🛡️ Defensive Considerations & OPSEC

1. **Web Server Access Logs:** CeWL generates hundreds of rapid HTTP GET requests, producing identifiable spider access logs. Use custom User-Agent strings (`-u "Mozilla/5.0..."`) to blend in.
2. **Honeyword Traps:** Defensive blue teams can embed invisible or hidden HTML links leading to canary credentials to flag automated spiders.

---

## ⚠️ Legal Disclaimer

> [!CAUTION]
> CeWL is designed for authorized security assessments, penetration testing, and password audit research only. Scraping or spidering unauthorized systems is strictly prohibited.