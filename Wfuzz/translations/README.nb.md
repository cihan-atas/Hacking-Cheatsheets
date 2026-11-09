<!-- language-home-navigation -->
[🏠 Norsk Hovedside](../../README.nb.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇬🇧 English](../README.md)

---


# 🎯 Wfuzz - Komplett jukseark

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
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-green?style=for-the-badge" alt="Nettsikkerhet og API">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="License GPLv2">
</p>

<p align="center">
  <b>🌐 Svært tilpassbar HTTP fuzzer designet for å oppdage skjulte parametere, omgåelser, overskrifter og multi-nyttelastmatriser</b>
</p>

---

## 📋 Innholdsfortegnelse

- [Oversikt og søkeordarkitekturen](#-oversikt--søkeordarkitekturen)
- [Wfuzz vs ffuf-sammenligning](#-wfuzz-vs-ffuf-sammenligning)
- [Installasjon](#-installasjon)
- [Basic Directory & File Discovery](#-basic-directory--file-discovery)
- [Filtrering og skjul støy (`--hc`, `--hl`, `--hw`, `--hh`)](#-filtrering--skjuler-støy---hc---hl---hw---hh)
- [Nyttelasttyper og dynamiske kodere (`-z`)](#-nyttelast-typer--dynamiske-kodere--z)
- [Parameter & Hidden Variable Fuzzing](#-parameter--hidden-variable-fuzzing)
- [Overskrifter, informasjonskapsler og autentisering](#-headers-informasjonskapsler--autentisering)
- [Multi-Variable Fuzzing (`FUZZ` og `FUZ2Z`)](#-multi-variable-fuzzing-fuzz-and-fuz2z)
- [Routing Through Burp Suite (`-p`)](#-routing-through-burp-suite--p)
- [Hurtigreferansetabell](#-hurtigreferansetabell)
– [Defensive Considerations & OPSEC](#-defensive-considerations--opsec)
– [Juridisk ansvarsfraskrivelse](#-juridisk ansvarsfraskrivelse)

---

## 🎯 Oversikt og nøkkelordarkitekturen

Skrevet i Python av **Xavi Mendez**, **Wfuzz** er et klassisk, kamptestet verktøy for å vurdere nettapplikasjoner. Den erstatter utpekte plassholdere (standard: `FUZZ`) i URL-er, parametere, overskrifter og POST-kropper med ordbokoppføringer eller algoritmisk genererte nyttelaster.

Wfuzz skiller seg ut for sin uovertrufne **nyttelastallsidighet** (serier, IP-generatorer, regex-strenger, on-the-fly-kodere som MD5 eller Base64) og modulær filtreringsmekanikk.

---

## ⚖️ Wfuzz vs ffuf sammenligning

| Funksjon | Wfuzz | ffuf |
|--------|--------|------|
| **Implementeringsspråk** | Python | Gå |
| **Rå hastighet** | ⭐⭐⭐ (Bra) | ⭐⭐⭐⭐⭐ (Braande rask) |
| **Dynamiske nyttelastkodere**| ✅ Innfødt (`-z file,pass.txt,md5`)| ❌ Krever eksterne skript |
| **Multi-nyttelastkombinasjoner**| ✅ Native (`FUZZ`, `FUZ2Z`, `FUZ3Z`)| ✅ Støttet (`-w w1:W1 -w w2:W2`)|
| **Innebygde nyttelastgeneratorer**| ✅ Områder, IP-er, Hex, Ordbøker| ❌ Bare filordlister |
| **Ressursbruk** | Moderat RAM / CPU | Lite minne fotavtrykk |

---

## 📥 Installasjon
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y wfuzz

# Python Pip
pip3 install wfuzz

# Arch Linux
sudo pacman -S wfuzz
```
---

## 🚀 Grunnleggende katalog og filoppdagelse
```bash
# Basic directory fuzzing
wfuzz -w /usr/share/wordlists/dirb/common.txt http://target.local/FUZZ

# Fuzzing file extensions
wfuzz -w extensions.txt http://target.local/index.FUZZ

# Fuzzing hidden backup files
wfuzz -w words.txt http://target.local/FUZZ.bak
```
---

## 🎯 Filtrering og skjule støy (`--hc`, `--hl`, `--hw`, `--hh`)

Virkelige servere returnerer svar selv for ikke-eksisterende sider (egendefinerte 404-sider eller omdirigeringer). Wfuzz tilbyr finkornede filtreringsflagg:
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

## 📦 Nyttelasttyper og dynamiske kodere (`-z`)

Wfuzz har innebygde generatorer som eliminerer behovet for forhåndsgenererte tekstfiler:
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

## 🔍 Parameter og skjult variabel fuzzing

Oppdag frakoblede GET/POST-parametere:
```bash
# GET parameter discovery
wfuzz -w /usr/share/seclists/Discovery/Web-Content/burp-parameter-names.txt \
  --hc 404 --hh 2340 "http://target.local/search.php?FUZZ=test"

# POST body parameter discovery
wfuzz -w parameters.txt -d "FUZZ=admin" --hc 404 http://target.local/api/update
```
---

## 🍪 Overskrifter, informasjonskapsler og autentisering
```bash
# Cookie Fuzzing (Session hijacking / privilege testing)
wfuzz -w wordlist.txt -b "session=FUZZ" http://target.local/dashboard

# HTTP Header Fuzzing (IP bypasses via X-Forwarded-For)
wfuzz -z file,ips.txt -H "X-Forwarded-For: FUZZ" http://target.local/admin

# Basic Authentication brute force
wfuzz -z file,users.txt -z file,passwords.txt --basic "FUZZ:FUZ2Z" http://target.local/protected/
```
---

## ⚡ Multivariabel fuzzing (`FUZZ` og `FUZ2Z`)

Kombiner flere ordlister ved å bruke distinkte plassholder-tagger:
```bash
# Test username and password matrix simultaneously
wfuzz -w users.txt -w passwords.txt -d "user=FUZZ&pass=FUZ2Z" --hc 200 http://target.local/login.php

# Fuzz path and extension together
wfuzz -w words.txt -w extensions.txt http://target.local/FUZZ.FUZ2Z
```
---

## 🔁 Ruting gjennom Burp Suite (`-p`)

Inspiser nyttelast og svar i Burp Suite:
```bash
# Route requests through local Burp Suite proxy
wfuzz -p 127.0.0.1:8080:HTTP -w wordlist.txt http://target.local/FUZZ
```
---

## ⚡ Hurtigreferansetabell

| Mål | Kommando |
|----------------|--------|
| **Directory Fuzzing** | `wfuzz -w <DICT> <URL>/FUZZ` |
| **Skjul HTTP-statuskoder** | `wfuzz -w <DICT> --hc 404,403 <URL>/FUZZ` |
| **Skjul etter svarlengde** | `wfuzz -w <DICT> --hh <LENGTH> <URL>/FUZZ` |
| **Fuzz GET-parameter** | `wfuzz -w <PARAMS> "<URL>?FUZZ=test"` |
| **Fuzz POST Body** | `wfuzz -w <PARAMS> -d "FUZZ=test" <URL>` |
| **Numerisk nyttelast** | `wfuzz -z range,1-100 "<URL>?id=FUZZ"` |
| **Kod nyttelast på farten**| `wfuzz -z file,<DICT>,base64 "<URL>?token=FUZZ"` |
| **Flere søkeord fuzzing** | `wfuzz -w <W1> -w <W2> -d "u=FUZZ&p=FUZ2Z" <URL>` |
| **Rute via Burp Proxy** | `wfuzz -p 127.0.0.1:8080:HTTP -w <DICT> <URL>/FUZZ` |

---

## 🛡️ Defensive betraktninger og OPSEC

1. **WAF-blokkering:** Fuzzing av hundrevis av forespørsler per sekund utløser Cloudflare, AWS WAF og ModSecurity ratebegrensning. Bruk `-s <seconds>` for å strupe forespørselshastigheten.
2. **Ressursutmattelse:** Kraftig fuzzing på uindekserte databasespørringer kan forårsake tjenestenekt-tilstander på eldre webservere.

---

## ⚠️ Juridisk ansvarsfraskrivelse

> [!FORSIKTIG]
> Dette juksearket er kun for autorisert penetrasjonstesting og sårbarhetsrevisjon. Fuzzing av uautoriserte applikasjoner er strengt forbudt.