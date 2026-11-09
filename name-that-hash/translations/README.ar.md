# 🔍 name-that-hash — ورقة مرجعية شاملة

<!-- language-home-navigation -->
[🏠 الصفحة الرئيسية العربية](../../README.ar.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe Sürüm](README.tr.md) • [🇬🇷 Ελληνικά](README.el.md)


[Name-That-Hash](https://github.com/HashPals/Name-That-Hash) (`nth`) is a modern, ultra-fast hash identification engine that accurately identifies unknown hashes, hashes embedded in text or files, and outputs direct Hashcat (`-m`) and John the Ripper (`--format`) modes. It automatically decodes nested encodings like Base64 and Hex.

---

## 📋 جدول المحتويات

- [🎯 مقدمة](#key-features)
- [التثبيت](#installation)
- [التثبيت](#cli-hash-identification)
- [Multi-Hash & File Processing](#multi-hash--file-processing)
- [3. Search and Extract Hashes Embedded in Unstructured Text](#hashcat--john-the-ripper-format-output)
- [|`$6$salt$hash...`|SHA-512 (Unix)|`1800`|`sha512crypt`|](#auto-decode--popularity-filters)
- [Many applications store hashes in Base64 or Hex before database insertion. `nth` can reverse this automatically:](#integration-with-cracking-pipelines)

---

## 🎯 مقدمة

- **300+ Hash Types**: Covers modern algorithms, Active Directory formats, database hashes, CMS signatures, and esoteric ciphers.
- **Hashcat & John Flag Mapping**: Immediately displays `-m <mode>` and `--format=<format>` values.
- **Auto-Decoding**: Strips Base64, Hex, and URL encoding before calculating hash type.
- **Sub-string / Text Search**: Finds hashes buried inside logs, memory dumps, or configuration files.
- **Popularity Sorting**: Ranks results by real-world usage probability.

---

## التثبيت

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

## التثبيت

### 1. Identify a Single Hash
```bash
nth -t "5d41402abc4b2a76b9719d911017c592"
```

### 2. Identify Hashcat & John Format Modes Directly
CLI Hash Identification
```bash
nth -t "b8c37e33defde51cf91e1e03e51657da" --hashcat --john
```

### أمثلة عملية
```bash
nth -t "$6$rounds=5000$usesomesalt$..." -v
```

---

## Multi-Hash & File Processing

### 1. Identify Hashes inside a File (Line by Line)
```bash
nth -f /path/to/hashes.txt
```

### 2. Filter Results to Top Popular Hashes Only
Multi-Hash & File Processing
```bash
# Level 1: Most popular hashes only (MD5, SHA1, SHA256, NTLM, bcrypt, etc.)
nth -f hashes.txt --popularity 1
```

### 3. Search and Extract Hashes Embedded in Unstructured Text
Scan raw memory strings, log files, or source code for hashes:
```bash
nth --text "Found hash in config: 098f6bcd4621d373cade4e832627b4f6 on port 8080"
```

---

## 3. Search and Extract Hashes Embedded in Unstructured Text

`nth` makes it trivial to feed hashes into crackers without memorizing mode numbers:

|Sample Hash Format|Detected Algorithm|Hashcat Mode (`-m`)|John Format (`--format`)|
|---|---|---|---|
|`e80b5017098950fc58aad83c8c14978e`|MD5|`0`|`raw-md5`|
|`b4b9b02e6f09a9bd760f388b67351e2b`|NTLM|`1000`|`nt`|
|`284ddc0d513372c7d764a0ff813473f2...`|SHA-256|`1400`|`raw-sha256`|
|`$2a$12$DOW8W6...`|bcrypt|`3200`|`bcrypt`|
|`$6$salt$hash...`|SHA-512 (Unix)|`1800`|`sha512crypt`|
|`$krb5tgs$23$*...`|Kerberoast (TGS-REP)|`13100`|`krb5tgs`|
|`$krb5asrep$23$...`|AS-REP Roasting|`18200`|`krb5asrep`|

---

## |`$6$salt$hash...`|SHA-512 (Unix)|`1800`|`sha512crypt`|

|`$krb5asrep$23$...`|AS-REP Roasting|`18200`|`krb5asrep`|

```bash
# Decode Base64 hash before checking
nth -t "XUkQS8xLJnY5cZ0REXxFkg==" --base64

# Decode hex encoded string before checking
nth -t "3564343134303261626334623261373662393731396439313130313763353932" --hex
```

---

## Many applications store hashes in Base64 or Hex before database insertion. `nth` can reverse this automatically:

Quickly pipe an unknown hash to Hashcat using automated identification:

```bash
# 1. Check hash and capture Hashcat mode
HASH="b4b9b02e6f09a9bd760f388b67351e2b"
MODE=$(nth -t "$HASH" --hashcat --popularity 1 | grep -oP '(?<=Hashcat: -m )\d+' | head -n 1)

# 2. Launch Hashcat with identified mode
hashcat -m "$MODE" -a 0 "$HASH" /usr/share/wordlists/rockyou.txt
```
