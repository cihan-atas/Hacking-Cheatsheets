<!-- language-home-navigation -->
[🏠 Deutsche Startseite](../../README.de.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🎯 Wfuzz – Vollständiger Cheatsheet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ██╗    ██╗███████╗██╗   ██╗███████╗███████╗
  ██║    ██║██╔════╝██║   ██║╚══███╔╝╚══███╔╝
  ██║ █╗ ██║█████╗  ██║   ██║  ███╔╝   ███╔╝ 
  ██║███╗██║██╔══╝  ██║   ██║ ███╔╝   ███╔╝  
  ╚███╔███╔╝██║     ╚██████╔╝███████╗███████╗
   ╚══╝╚══╝ ╚═╝      ╚═════╝ ╚══════╝╚══════╝
       The Flexible Web Application Fuzzer & Parameter Explorer
```
<p align="center">
  <img src="https://img.shields.io/badge/Wfuzz-Web_Fuzzer-blue?style=for-the-badge" alt="Wfuzz">
  <img src="https://img.shields.io/badge/Language-Python-orange?style=for-the-badge" alt="Language Python">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-green?style=for-the-badge" alt="Web-Sicherheit & API">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="License GPLv2">
</p>

<p align="center">
  <b>🌐 Hochgradig anpassbarer HTTP-Fuzzer zum Entdecken versteckter Parameter, Umgehungen, Header und Multi-Payload-Matrizen</b>
</p>

---

## 📋 Inhaltsverzeichnis

- [Überblick und die Keyword-Architektur](#-overview--the-keyword-architecture)
- [Wfuzz vs. ffuf-Vergleich](#-wfuzz-vs-ffuf-comparison)
- [Installation](#-Installation)
- [Basisverzeichnis und Dateierkennung](#-basic-directory--file-discovery)
- [Rauschen filtern und ausblenden (`--hc`, `--hl`, `--hw`, `--hh`)](#-filtering--hiding-noise---hc---hl---hw---hh)
- [Nutzlasttypen und dynamische Encoder (`-z`)](#-payload-types--dynamic-encoders--z)
- [Fuzzing von Parametern und versteckten Variablen](#-parameter--hidden-variable-fuzzing)
- [Header, Cookies und Authentifizierung](#-headers-cookies--authentication)
- [Multi-Variable Fuzzing (`FUZZ` und `FUZ2Z`)](#-multi-variable-fuzzing-fuzz-and-fuz2z)
- [Routing durch Burp Suite (`-p`)](#-routing-through-burp-suite--p)
- [Kurzreferenztabelle](#-quick-reference-table)
- [Verteidigungsüberlegungen und OPSEC](#-defensive-considerations--opsec)
- [Rechtlicher Haftungsausschluss](#-legal-disclaimer)

---

## 🎯 Übersicht und die Keyword-Architektur

**Wfuzz** wurde von **Xavi Mendez** in Python geschrieben und ist ein klassisches, kampferprobtes Tool zur Bewertung von Webanwendungen. Es ersetzt bestimmte Platzhalter (Standard: `FUZZ`) in URLs, Parametern, Headern und POST-Körpern durch Wörterbucheinträge oder algorithmisch generierte Nutzlasten.

Wfuzz zeichnet sich durch seine unübertroffene **Payload-Vielseitigkeit** (Bereiche, IP-Generatoren, Regex-Strings, On-the-Fly-Encoder wie MD5 oder Base64) und modulare Filtermechanismen aus.

---

## ⚖️ Wfuzz vs. ffuf Vergleich

| Funktion | Wfuzz | ffuf |
|---------|-------|------|
| **Implementierungssprache** | Python | Geh |
| **Rohe Geschwindigkeit** | ⭐⭐⭐ (Gut) | ⭐⭐⭐⭐⭐ (Blitzschnell) |
| **Dynamische Nutzlast-Encoder**| ✅ Native (`-z file,pass.txt,md5`)| ❌ Erfordert externe Skripte |
| **Kombinationen mit mehreren Nutzlasten**| ✅ Nativ (`FUZZ`, `FUZ2Z`, `FUZ3Z`)| ✅ Unterstützt (`-w w1:W1 -w w2:W2`)|
| **Eingebaute Nutzlastgeneratoren**| ✅ Bereiche, IPs, Hex, Wörterbücher| ❌ Nur Dateiwortlisten |
| **Ressourcennutzung** | Moderater RAM/CPU | Geringer Speicherbedarf |

---

## 📥 Installation
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y wfuzz

# Python Pip
pip3 install wfuzz

# Arch Linux
sudo pacman -S wfuzz
```
---

## 🚀 Grundlegende Verzeichnis- und Dateierkennung
```bash
# Basic directory fuzzing
wfuzz -w /usr/share/wordlists/dirb/common.txt http://target.local/FUZZ

# Fuzzing file extensions
wfuzz -w extensions.txt http://target.local/index.FUZZ

# Fuzzing hidden backup files
wfuzz -w words.txt http://target.local/FUZZ.bak
```
---

## 🎯 Rauschen filtern und ausblenden (`--hc`, `--hl`, `--hw`, `--hh`)

Reale Server geben Antworten auch für nicht vorhandene Seiten zurück (benutzerdefinierte 404-Seiten oder Weiterleitungen). Wfuzz bietet feinkörnige Filterflags:
```bash
# Hide specific HTTP status codes (e.g. 404, 403)
wfuzz -w wordlist.txt --hc 404,403 http://target.local/FUZZ

# Hide responses by line count (filters out static custom 404 pages)
wfuzz -w wordlist.txt --hl 42 http://target.local/FUZZ

# Hide responses by word count
wfuzz -w wordlist.txt --hw 120 http://target.local/FUZZ

# Hide responses by character count / body length
wfuzz -w wordlist.txt --hh 0,1052 http://target.local/FUZZ

# Show ONLY HTTP 200 and 302 responses
wfuzz -w wordlist.txt --sc 200,302 http://target.local/FUZZ
```
---

## 📦 Nutzlasttypen und dynamische Encoder (`-z`)

Wfuzz bietet integrierte Generatoren, die vorab generierte Textdateien überflüssig machen:
```bash
# Numeric Range Fuzzing (e.g., IDOR testing from 1 to 500)
wfuzz -z range,1-500 http://target.local/user.php?id=FUZZ

# Custom Inline List
wfuzz -z list,admin-root-test-dev http://target.local/FUZZ/

# On-the-fly MD5 encoding of wordlist entries
wfuzz -z file,passwords.txt,md5 http://target.local/login?hash=FUZZ

# On-the-fly Base64 encoding
wfuzz -z file,usernames.txt,base64 http://target.local/auth?token=FUZZ
```
---

## 🔍 Parameter- und versteckte Variablen-Fuzzing

Entdecken Sie nicht verknüpfte GET/POST-Parameter:
```bash
# GET parameter discovery
wfuzz -w /usr/share/seclists/Discovery/Web-Content/burp-parameter-names.txt \
  --hc 404 --hh 2340 "http://target.local/search.php?FUZZ=test"

# POST body parameter discovery
wfuzz -w parameters.txt -d "FUZZ=admin" --hc 404 http://target.local/api/update
```
---

## 🍪 Header, Cookies und Authentifizierung
```bash
# Cookie Fuzzing (Session hijacking / privilege testing)
wfuzz -w wordlist.txt -b "session=FUZZ" http://target.local/dashboard

# HTTP Header Fuzzing (IP bypasses via X-Forwarded-For)
wfuzz -z file,ips.txt -H "X-Forwarded-For: FUZZ" http://target.local/admin

# Basic Authentication brute force
wfuzz -z file,users.txt -z file,passwords.txt --basic "FUZZ:FUZ2Z" http://target.local/protected/
```
---

## ⚡ Multi-Variablen-Fuzzing (`FUZZ` und `FUZ2Z`)

Kombinieren Sie mehrere Wortlisten mit unterschiedlichen Platzhalter-Tags:
```bash
# Test username and password matrix simultaneously
wfuzz -w users.txt -w passwords.txt -d "user=FUZZ&pass=FUZ2Z" --hc 200 http://target.local/login.php

# Fuzz path and extension together
wfuzz -w words.txt -w extensions.txt http://target.local/FUZZ.FUZ2Z
```
---

## 🔁 Routing durch Burp Suite (`-p`)

Überprüfen Sie Nutzlasten und Antworten in der Burp Suite:
```bash
# Route requests through local Burp Suite proxy
wfuzz -p 127.0.0.1:8080:HTTP -w wordlist.txt http://target.local/FUZZ
```
---

## ⚡ Kurzreferenztabelle

| Ziel | Befehl |
|-----------|---------|
| **Verzeichnis-Fuzzing** | `wfuzz -w <DICT> <URL>/FUZZ` |
| **HTTP-Statuscodes ausblenden** | `wfuzz -w <DICT> --hc 404,403 <URL>/FUZZ` |
| **Nach Antwortlänge ausblenden** | `wfuzz -w <DICT> --hh <LENGTH> <URL>/FUZZ` |
| **Fuzz-GET-Parameter** | `wfuzz -w <PARAMS> "<URL>?FUZZ=test"` |
| **Fuzz POST Body** | `wfuzz -w <PARAMS> -d "FUZZ=test" <URL>` |
| **Nutzlasten im numerischen Bereich** | `wfuzz -z range,1-100 "<URL>?id=FUZZ"` |
| **Nutzlasten im Handumdrehen kodieren**| `wfuzz -z file,<DICT>,base64 "<URL>?token=FUZZ"` |
| **Multi-Keyword-Fuzzing** | `wfuzz -w <W1> -w <W2> -d "u=FUZZ&p=FUZ2Z" <URL>` |
| **Route über Burp-Proxy** | `wfuzz -p 127.0.0.1:8080:HTTP -w <DICT> <URL>/FUZZ` |

---

## 🛡️ Defensive Überlegungen und OPSEC

1. **WAF-Blockierung:** Das Fuzzing von Hunderten von Anfragen pro Sekunde löst eine Ratenbegrenzung durch Cloudflare, AWS WAF und ModSecurity aus. Verwenden Sie `-s <seconds>`, um die Anforderungsgeschwindigkeit zu drosseln.
2. **Ressourcenerschöpfung:** Starkes Fuzzing bei nicht indizierten Datenbankabfragen kann zu Denial of Service (DoS)-Bedingungen auf älteren Webservern führen.

---

## ⚠️ Rechtlicher Haftungsausschluss

> [!ACHTUNG]
> Dieses Cheatsheet ist nur für autorisierte Penetrationstests und Schwachstellenprüfungen bestimmt. Das Fuzzing nicht autorisierter Anwendungen ist strengstens untersagt.