# CUPP (Common User Passwords Profiler) Cheatsheet

[CUPP](https://github.com/Mebus/cupp) (Common User Passwords Profiler) is a specialized password dictionary generator designed for targeted human profiling. Rather than brute-forcing with generic dictionaries, CUPP generates permutations based on personal details (target name, partner, pets, birth dates, company, hobbies, leet speak variations, and special character combinations).

---

## Table of Contents

- [Overview & Use Cases](#overview--use-cases)
- [Installation](#installation)
- [Interactive Profiling Wizard (`-i`)](#interactive-profiling-wizard--i)
- [Configuration & Leet Rules (`cupp.cfg`)](#configuration--leet-rules-cuppcfg)
- [Downloading & Merging Dictionaries (`-l` & `-w`)](#downloading--merging-dictionaries--l---w)
- [Piping Output to Hashcat & Hydra](#piping-output-to-hashcat--hydra)
- [Defensive Insights & Password Policy Guidelines](#defensive-insights--password-policy-guidelines)

---

## Overview & Use Cases

Traditional dictionary attacks often fail against complex passwords that follow human-predictable patterns (e.g., `John1984!`, `Sarah_Fluffy2022#`). CUPP addresses this by:
- Collecting target-specific personal intelligence.
- Combining names, birth years, significant dates, and pet names.
- Applying rule-based mutations (capitalization, leet speak replacement, special characters, random number appending).
- Producing an optimized, targeted wordlist dramatically smaller than `rockyou.txt` but with higher target relevance.

---

## Installation

```bash
# Clone the repository
git clone https://github.com/Mebus/cupp.git
cd cupp

# Kali Linux / Debian repository (if available)
sudo apt-get update && sudo apt-get install -y cupp

# Check help options
python3 cupp.py -h
```

---

## Interactive Profiling Wizard (`-i`)

Launch the questionnaire prompt to build a personalized wordlist:

```bash
python3 cupp.py -i
```

Example prompt walkthrough:
```text
[+] Insert the targets information:
> First Name: Alice
> Surname: Smith
> Nickname: ally
> Birthdate (DDMMYYYY): 15061992

[+] Insert partners information:
> Partners name: Bob
> Partners nickname: bobby
> Partners birthdate (DDMMYYYY): 04111990

[+] Insert child's information:
> Child's name: Charlie
> Child's nickname: char
> Child's birthdate (DDMMYYYY): 20082018

[+] Insert pet's info:
> Pet's name: Luna

[+] Insert company name:
> Company name: CyberCorp

[+] Do you want to add some special words about the victim? Y/n: y
> (comma separated): football, chelsea, guitar, devops

[+] Do you want to add special characters at the end of words? Y/n: y
[+] Do you want to add some random numbers at the end of words? Y/n: y
[+] Leet mode? (i.e. leet = 1337) Y/n: y
```

Output file `alice.csv` (or specified text file) is generated with thousands of customized mutations (e.g., `Alice1992!`, `LunaCharlie2018`, `CyberCorp@2022`).

---

## Configuration & Leet Rules (`cupp.cfg`)

CUPP configuration is controlled via `cupp.cfg`. Key sections include:
```ini
[years]
# Set year range for automated suffix appending
years = 1970,1980,1985,1990,1995,2000,2010,2020,2023,2024,2025,2026

[nums]
# Common number patterns appended to passwords
nums = 1,12,123,1234,12345,123456,777,999,007,69

[special]
# Special character appending
chars = !,@,#,$,%,&,*,-,_

[leet]
# Leet speak substitutions
a = 4,@
b = 8
e = 3
i = 1,!
o = 0
s = 5,$
t = 7
```

Edit `cupp.cfg` to adjust to target locale and language specifics.

---

## Downloading & Merging Dictionaries (`-l` & `-w`)

### 1. Download Pre-Compiled Wordlists from Repositories
Download default linguistic wordlists:
```bash
python3 cupp.py -l
```

### 2. Improve an Existing Wordlist with Leet / Special Rules
Apply CUPP mutation rules to a list of existing words:
```bash
python3 cupp.py -w /path/to/base_words.txt
```

---

## Piping Output to Hashcat & Hydra

Feed the generated customized dictionary directly into brute-force tools:

### Cracking Target Hashes with Hashcat
```bash
# Generate targeted list
python3 cupp.py -i

# Run Hashcat against target NTLM hash
hashcat -m 1000 target_ntlm.hash alice.txt -r /usr/share/hashcat/rules/best64.rule
```

### Online Authentication Brute-Force with Hydra
```bash
hydra -l alice -P alice.txt 192.168.1.50 ssh
```

---

## Defensive Insights & Password Policy Guidelines

- **Ban Personal Attributes:** Prohibit user passwords containing employee first names, last names, usernames, company brand names, or birth dates.
- **Enforce Length over Complexity:** A 16+ character passphrase (e.g., four random dictionary words) is significantly more resilient against profiling tools than an 8-character `NameYear!` pattern.
- **Multi-Factor Authentication (MFA):** Enforce hardware FIDO2 or TOTP tokens so customized dictionary generation alone cannot compromise accounts.
