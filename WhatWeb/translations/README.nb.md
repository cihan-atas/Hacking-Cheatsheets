<!-- language-home-navigation -->
[🏠 Norsk Hovedside](../../README.nb.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇬🇧 English](../README.md)

---


# 🌐 WhatWeb - Komplett jukseark

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ██╗    ██╗██╗  ██╗ █████╗ ████████╗██╗    ██╗███████╗██████╗ 
  ██║    ██║██║  ██║██╔══██╗╚══██╔══╝██║    ██║██╔════╝██╔══██╗
  ██║ █╗ ██║███████║███████║   ██║   ██║ █╗ ██║█████╗  ██████╔╝
  ██║███╗██║██╔══██║██╔══██║   ██║   ██║███╗██║██╔══╝  ██╔══██╗
  ╚███╔███╔╝██║  ██║██║  ██║   ██║   ╚███╔███╔╝███████╗██████╔╝
   ╚══╝╚══╝ ╚═╝  ╚═╝╚═╝  ╚═╝   ╚═╝    ╚══╝╚══╝ ╚══════╝╚═════╝ 
       Next Generation Web Scanner & Technology Fingerprinter
```
<p align="center">
  <img src="https://img.shields.io/badge/WhatWeb-Web_Fingerprinter-blue?style=for-the-badge" alt="WhatWeb">
  <img src="https://img.shields.io/badge/Author-Andrew_Horton-orange?style=for-the-badge" alt="urbanadventurer">
  <img src="https://img.shields.io/badge/Category-Recon_&_Scanning-green?style=for-the-badge" alt="Recon & Scanning">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="License GPLv2">
</p>

<p align="center">
  <b>🌐 Neste generasjons nettskanner designet for å identifisere innholdsstyringssystemer (CMS), bloggeplattformer, JavaScript-biblioteker og serverteknologier</b>
</p>

---

## 📋 Innholdsfortegnelse

- [Oversikt og teknologifingeravtrykk](#-oversikt--teknologi-fingeravtrykk)
- [Installasjon](#-installasjon)
– [Basic Website Fingerprinting](#-basic-website-fingerprinting)
- [Aggression Levels Explained (`-a`)](#-aggresjonsnivåer-forklart--a)
  - [Stealthy Passive Mode (`-a 1`)](#stealthy-passive-mode--a-1)
  - [Aggressiv modus (`-a 3`)](#aggressiv-modus--a-3)
  - [Heavy / Brute Mode (`-a 4`)](#heavy--brute-mode--a-4)
- [Skanning av undernett og målfiler](#-skanning-undernett--målfiler)
- [Plugin-system og selektiv revisjon](#-plugin-system--selektiv-revisjon)
- [Output-logging og rapporteksport](#-output-logging--report-exporting)
- [Hurtigreferansetabell](#-hurtigreferansetabell)
– [Defensive Considerations & OPSEC](#-defensive-considerations--opsec)
– [Juridisk ansvarsfraskrivelse](#-juridisk ansvarsfraskrivelse)

---

## 🎯 Oversikt og teknologi Fingeravtrykk

Før du lanserer utnyttelser eller brute-force fuzzers mot en nettapplikasjon, er det avgjørende å identifisere den nøyaktige teknologistabelen.

Laget av **Andrew Horton (urbanadventurer)**, **WhatWeb** gjenkjenner over 1800 forskjellige nettteknologier:
- **Content Management Systems (CMS):** WordPress, Drupal, Joomla, Ghost, Magento.
- **Nettservere og proxyer:** Apache, Nginx, Microsoft-IIS, Cloudflare, Envoy.
- **Backend Frameworks:** Laravel, Django, Ruby on Rails, ASP.NET, Spring Boot.
- **Frontendbiblioteker:** React, Vue.js, Angular, jQuery, Bootstrap.
- **Innebygde enheter:** Webkameraer, rutere, skrivere, SCADA-grensesnitt.

---

## 📥 Installasjon
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y whatweb

# Arch Linux
sudo pacman -S whatweb

# macOS (Homebrew)
brew install whatweb
```
---

## 🚀 Grunnleggende nettstedsfingeravtrykk
```bash
# Scan a single domain
whatweb https://target.local

# Brief one-line output format
whatweb --color=never -q https://target.local

# Verbose detailed report showing evidence matches
whatweb -v https://target.local
```
---

## ⚡ Aggresjonsnivåer forklart (`-a`)

WhatWeb balanserer sniking mot dybde ved å bruke fire aggresjonsnivåer:

### Stealthy passiv modus (`-a 1`)
Sender en enkelt HTTP GET-forespørsel til den angitte URL-adressen og analyserer de returnerte svarhodene, informasjonskapslene og HTML-taggene uten å be om ekstra sider:
```bash
whatweb -a 1 https://target.local
```
### Aggressiv modus (`-a 3`)
Anbefalt for penetrasjonstester. Følger omdirigeringer og ber om vanlige plugin-filer (f.eks. `wp-login.php`, `robots.txt`, favicon-hasher):
```bash
whatweb -a 3 https://target.local
```
### Heavy / Brute Mode (`-a 4`)
Forsøker tusenvis av plugin-gjettinger og kjente fingeravtrykksadresser:
```bash
whatweb -a 4 https://target.local
```
---

## 🌐 Skanning av undernett og målfiler
```bash
# Scan an entire CIDR network block
whatweb 192.168.1.0/24

# Scan multiple URLs listed in a text file
whatweb -i targets.txt

# Pipe live web endpoints from httpx into WhatWeb
cat live_hosts.txt | whatweb --no-errors
```
---

## 🧩 Plugin-system og selektiv revisjon

WhatWeb har en rik plugin-arkitektur:
```bash
# List all 1,800+ available plugins
whatweb -l

# Search for specific plugins (e.g., WordPress or Apache)
whatweb --grep wordpress

# Run ONLY a specific plugin against a target
whatweb --plugins="apache,php,wordpress" https://target.local
```
---

## 📊 Utdatalogging og rapporteksport
```bash
# Export results to clean JSON format
whatweb https://target.local --log-json results.json

# Export to XML format
whatweb https://target.local --log-xml results.xml

# Export to compact CSV format
whatweb -i targets.txt --log-brief results.txt
```
---

## ⚡ Hurtigreferansetabell

| Mål | Kommando |
|----------------|--------|
| **Standard skanning** | `whatweb <URL>` |
| ** Utførlig detaljert rapport** | `whatweb -v <URL>` |
| **Stealth Passive Scan** | `whatweb -a 1 <URL>` |
| **Aggressiv dyp skanning** | `whatweb -a 3 <URL>` |
| **Skann undernett (CIDR)** | `whatweb <SUBNET>/24` |
| **Skann mål fra fil** | `whatweb -i targets.txt` |
| **Eksporter til JSON** | `whatweb <URL> --log-json report.json` |
| **Filtrer etter spesifikke plugins**| `whatweb --plugins="wordpress,nginx" <URL>` |

---

## 🛡️ Defensive betraktninger og OPSEC

1. **Server Header Masking:** Fjern eller randomiser `Server` og `X-Powered-By` HTTP-svarhodene i Apache/Nginx for å bekjempe passiv bannerfingeravtrykk.
2. **Favicon Hash Masking:** WhatWeb bruker MD5-hasher av `/favicon.ico` for å identifisere nettrammeverk. Tilpass standard rammeverkfavikoner.

---

## ⚠️ Juridisk ansvarsfraskrivelse

> [!FORSIKTIG]
> WhatWeb er kun ment for autorisert rekognosering, sikkerhetsrevisjon og systeminventar. Skanning av uautoriserte nettverk eller nettapplikasjoner uten tillatelse kan være i strid med gjeldende lover.