<!-- language-home-navigation -->
[🏠 Norsk Hovedside](../../README.nb.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇬🇧 English](../README.md)

---


# 🛡️ WPScan - Komplett jukseark

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ██╗    ██╗██████╗ ███████╗ ██████╗ █████╗ ███╗   ██╗
  ██║    ██║██╔══██╗██╔════╝██╔════╝██╔══██╗████╗  ██║
  ██║ █╗ ██║██████╔╝███████╗██║     ███████║██╔██╗ ██║
  ██║███╗██║██╔═══╝ ╚════██║██║     ██╔══██║██║╚██╗██║
  ╚███╔███╔╝██║     ███████║╚██████╗██║  ██║██║ ╚████║
   ╚══╝╚══╝ ╚═╝     ╚══════╝ ╚═════╝╚═╝  ╚═╝╚═╝  ╚═══╝
       The WordPress Security & Vulnerability Scanner
```
<p align="center">
  <img src="https://img.shields.io/badge/WPScan-WordPress_Security-blue?style=for-the-badge" alt="WPScan">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-orange?style=for-the-badge" alt="Nettsikkerhet">
  <img src="https://img.shields.io/badge/Database-WPVDB_API-green?style=for-the-badge" alt="WPVDB API">
  <img src="https://img.shields.io/badge/License-GPLv3-red?style=for-the-badge" alt="License GPLv3">
</p>

<p align="center">
  <b>🌐 Black box-sårbarhetsskanner designet for å oppdage WordPress-kjerne-, plugin-, tema- og brukersårbarheter</b>
</p>

---

## 📋 Innholdsfortegnelse

- [Oversikt og arkitektur](#-oversikt--arkitektur)
- [Installasjon](#-installasjon)
- [API Token Integration (WPVDB)](#-api-token-integration-wpvdb)
- [Basic Scan & Target Enumeration](#-basic-scan--target-numeration)
- [Enumeration Flags Breakdown (`-e`)](#-enumeration-flags-breakdown--e)
  - [Sårbare plugins (`-e vp`)](#vulnerable-plugins--e-vp)
  - [Alle plugins (`-e ap`)](#all-plugins--e-ap)
  - [Temaer (`-e vt`, `-e at`)](#themes--e-vt--e-at)
  - [Brukernavn (`-e u`)](#brukernavn--e-u)
  - [Sensitive Backups (`-e cb`, `-e dbe`)](#sensitive-backups--e-cb--e-dbe)
- [Deteksjonsmoduser (`--plugins-detection`)](#-deteksjonsmoduser---plugins-deteksjon)
- [Password Brute-Forcing (`wp-login.php` & XML-RPC)](#-password-brute-forcing-wp-loginphp--xml-rpc)
- [Stealth & WAF Evasion](#-stealth--waf-evasion)
- [Hurtigreferansetabell](#-hurtigreferansetabell)
– [Defensive Considerations & Hardening](#-defensive-considerations--hardening)
– [Juridisk ansvarsfraskrivelse](#-juridisk ansvarsfraskrivelse)

---

## 🎯 Oversikt og arkitektur

Med WordPress som driver mer enn 40 % av de 10 millioner beste nettstedene, er det det mest målrettede innholdsstyringssystemet (CMS) i verden.

Skrevet i Ruby, **WPScan** er industristandarden svart boks sårbarhetsskanner for WordPress-installasjoner. Den katalogiserer aktive kjerneversjoner, oppregner installerte plugins og temaer, trekker ut registrerte administrative brukernavn og korrelerer dem mot **WPScan Vulnerability Database (WPVDB)** som inneholder tusenvis av kjente CVEer.

---

## 📥 Installasjon
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y wpscan

# Arch Linux
sudo pacman -S wpscan

# Ruby Gem (Any platform)
gem install wpscan

# Docker Container
docker run -it --rm wpscan/wpscan --url https://target.local
```
---

## 🔑 API Token Integration (WPVDB)

For å vise kjente sårbarheter og CVE-score, krever WPScan et gratis API-token fra [wpscan.com](https://wpscan.com/):
```bash
# Provide API token via CLI
wpscan --url https://target.local --api-token YOUR_API_TOKEN

# Or export to environment variable
export WPSCAN_API_TOKEN="YOUR_API_TOKEN"
```
---

## 🚀 Grunnleggende skanning og måloppregning
```bash
# Quick non-intrusive scan
wpscan --url https://target.local

# Update the local database before scanning
wpscan --update
```
---

## 🔍 Oppregning av flaggoversikt (`-e`)

WPScan tillater modulær måloppregning ved å bruke `--enumerate` (eller `-e`) flagget:

### Sårbare plugins (`-e vp`)
Skanner etter plugins som har kjente sikkerhetssårbarheter i WPVDB-databasen:
```bash
wpscan --url https://target.local -e vp --api-token YOUR_TOKEN
```
### Alle plugins (`-e ap`)
Sonder for tusenvis av kjente plugins via dictionary brute-force:
```bash
wpscan --url https://target.local -e ap --plugins-detection aggressive
```
### Temaer (`-e vt`, `-e at`)
```bash
# Enumerate vulnerable themes
wpscan --url https://target.local -e vt

# Enumerate all installed themes
wpscan --url https://target.local -e at
```
### Brukernavn (`-e u`)
Trekker ut registrerte forfatterbrukernavn gjennom REST API-endepunkter (`/wp-json/wp/v2/users`), forfatterarkiver (`/?author=1`) og påloggingsfeilmeldinger:
```bash
wpscan --url https://target.local -e u1-10
```
### Sensitive sikkerhetskopier (`-e cb`, `-e dbe`)
Søker etter eksponert `wp-config.php.bak`, databasedumper og sikkerhetskopiarkiver:
```bash
wpscan --url https://target.local -e cb,dbe
```
---

## ⚙️ Deteksjonsmoduser (`--plugins-detection`)

Kontrollerer hvor grundig WPScan søker etter plugins:
```bash
# Passive (Fast, inspects HTML source, script tags, style links)
wpscan --url https://target.local --plugins-detection passive

# Aggressive (Sends HTTP requests for thousands of plugin directories)
wpscan --url https://target.local --plugins-detection aggressive

# Mixed (Recommended default - passive followed by targeted active requests)
wpscan --url https://target.local --plugins-detection mixed
```
---

## 🔓 Passord Brute-Forcing (`wp-login.php` & XML-RPC)

Når gyldige brukernavn er oppregnet, start målrettet credential brute-forcing:
```bash
# Brute force login using enumerated username list
wpscan --url https://target.local -U users.txt -P /usr/share/wordlists/rockyou.txt

# Password spray a single password across all users
wpscan --url https://target.local -U users.txt -p 'Winter2026!'

# Multi-threaded password attack
wpscan --url https://target.local -U admin -P passwords.txt -t 20
```
> [!MERK]
> Hvis `xmlrpc.php` er aktivert på serveren, bruker WPScan automatisk XML-RPC multicall-metoder for å teste hundrevis av passordkombinasjoner per enkelt HTTP-forespørsel!

---

## 🥷 Stealth & WAF Evasion
```bash
# Randomize User-Agent header to evade simple bot signatures
wpscan --url https://target.local --random-user-agent

# Throttle request rate (milliseconds between requests) to bypass rate limits
wpscan --url https://target.local --throttle 1000

# Route through SOCKS proxy / Tor
wpscan --url https://target.local --proxy socks5://127.0.0.1:9050

# Ignore SSL/TLS certificate warnings
wpscan --url https://target.local --disable-tls-checks
```
---

## ⚡ Hurtigreferansetabell

| Mål | Kommando |
|----------------|--------|
| **Standard skanning** | `wpscan --url <URL> --api-token <TOKEN>` |
| **Regn opp sårbare plugins** | `wpscan --url <URL> -e vp --api-token <TOKEN>` |
| **Oppreg alle plugins** | `wpscan --url <URL> -e ap --plugins-detection aggressive` |
| **Oppreg brukere** | `wpscan --url <URL> -e u` |
| **Oppreg sikkerhetskopier og konfigurasjoner** | `wpscan --url <URL> -e cb,dbe` |
| **Passord Brute Force** | `wpscan --url <URL> -U <USERS> -P <PASSWORDS>` |
| **Tilfeldig brukeragent og stealth** | `wpscan --url <URL> --random-user-agent --stealthy` |
| **Eksporter til JSON-fil** | `wpscan --url <URL> -f json -o wpscan_report.json` |

---

## 🛡️ Defensive betraktninger og herding

1. **Deaktiver XML-RPC:** Blokker tilgang til `xmlrpc.php` via `.htaccess`- eller Nginx-konfigurasjon for å nøytralisere høyhastighets multicall brute-force-angrep.
2. **Begrens REST API-brukere endepunkt:** Deaktiver offentlig tilgang til `/wp-json/wp/v2/users` for å forhindre triviell oppregning av forfattere.
3. **Patch plugins regelmessig:** Over 90 % av WordPress-sårbarhetene ligger i tredjeparts plugins og temaer i stedet for WordPress-kjernen.

---

## ⚠️ Juridisk ansvarsfraskrivelse

> [!FORSIKTIG]
> Dette juksearket er kun ment for autorisert penetrasjonstesting, sårbarhetsvurderinger og revisjon av nettapplikasjoner. Brute-tvingende passord eller skannesystemer uten eksplisitt autorisasjon er ulovlig.