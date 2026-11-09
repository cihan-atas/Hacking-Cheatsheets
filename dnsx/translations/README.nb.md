<!-- language-home-navigation -->
[🏠 Norsk Hovedside](../../README.nb.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇬🇧 English](../README.md)

---


# 📡 dnsx - Komplett jukseark

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ██████╗ ███╗   ██╗███████╗██╗  ██╗
  ██╔══██╗████╗  ██║██╔════╝╚██╗██╔╝
  ██║  ██║██╔██╗ ██║███████╗ ╚███╔╝ 
  ██║  ██║██║╚██╗██║╚════██║ ██╔██╗ 
  ██████╔╝██║ ╚████║███████║██╔╝ ██╗
  ╚═════╝ ╚═╝  ╚═══╝╚══════╝╚═╝  ╚═╝
       Fast & Multi-Purpose DNS Toolkit by ProjectDiscovery
```
<p align="center">
  <img src="https://img.shields.io/badge/dnsx-DNS_Toolkit-blue?style=for-the-badge" alt="dnsx">
  <img src="https://img.shields.io/badge/Developer-ProjectDiscovery-orange?style=for-the-badge" alt="ProjectDiscovery">
  <img src="https://img.shields.io/badge/Category-Recon_&_Scanning-green?style=for-the-badge" alt="Recon & Scanning">
  <img src="https://img.shields.io/badge/License-MIT-red?style=for-the-badge" alt="License MIT">
</p>

<p align="center">
  <b>🌐 Høyytelses DNS-oppløsning, jokertegnfiltrering, omvendte DNS-oppslag og utvinningsmotor for flere søk</b>
</p>

---

## 📋 Innholdsfortegnelse

- [Oversikt og arkitektur](#-oversikt--arkitektur)
- [Hvorfor dnsx? (Wildcard Handling & Speed)](#-why-dnsx-wildcard-handling--speed)
- [Installasjon](#-installasjon)
- [Basic Subdomain Probing & Resolution](#-basic-subdomain-probing--resolution)
- [Jokertegn DNS-filtrering (`-wd`)](#-jokertegn-dns-filtrering---wd)
- [Querying Specific DNS Record Types](#-querying-specific-dns-record-types)
- [Reverse DNS Lookups (`-ptr`)](#-reverse-dns-lookups---ptr)
– [Using Custom Resolvers & Rate Limiting](#-using-custom-resolvers--rate-limiting)
- [Pipeline Automation (Subfinder + dnsx + httpx)](#-pipeline-automation-subfinder--dnsx--httpx)
- [Hurtigreferansetabell](#-hurtigreferansetabell)
– [Defensive Considerations & OPSEC](#-defensive-considerations--opsec)
– [Juridisk ansvarsfraskrivelse](#-juridisk ansvarsfraskrivelse)

---

## 🎯 Oversikt og arkitektur

Konstruert i Go av **ProjectDiscovery**, **dnsx** er et raskt og flerbruks DNS-verktøysett designet for rekognosering, dusørjakt og nettverksoppdagelse.

Mens verktøy som `dig`, `nslookup` og `host` løser poster én etter én, behandler `dnsx` tusenvis av spørringer asynkront, prøver automatisk mislykkede løsere på nytt, trekker ut rene svar-IP-er og eliminerer jokertegnstøy som forurenser underdomeneoppregning.

---

## ⚡ Hvorfor dnsx? (Jokertegn håndtering og hastighet)

Under bug-bounty-rekognosering returnerer passive verktøy (`subfinder`, `amass`) ofte tusenvis av døde underdomener eller jokertegn. Testing av HTTP-endepunkter på døde DNS-oppføringer sløser med enorm båndbredde.

`dnsx` løser dette ved å:
1. **Filtrer ut jokertegn (`-wd`):** Oppdager om `*.domain.com` løses til en oppsamlingsadresse og filtrerer ut fantomunderdomener.
2. **Hastighet og samtidighet:** Løser 10 000+ domener per minutt på tvers av offentlige og private oppløsningsgrupper.
3. **Rengjør utgangsrør:** Mates direkte inn i `httpx` eller `nmap`.

---

## 📥 Installasjon
```bash
# Go Install (Recommended)
go install -v github.com/projectdiscovery/dnsx/cmd/dnsx@latest

# Kali Linux & Debian
sudo apt update && sudo apt install -y dnsx

# MacOS (Homebrew)
brew install dnsx

# Verify installation
dnsx -version
```
---

## 🚀 Grunnleggende underdomeneundersøkelse og oppløsning
```bash
# Resolve a single domain
echo "target.com" | dnsx

# Resolve a list of subdomains from file
dnsx -l subdomains.txt

# Display domain and its corresponding A record IP
dnsx -l subdomains.txt -a -resp

# Output only resolved IP addresses (ready for Nmap)
dnsx -l subdomains.txt -resp-only
```
---

## 🚫 Jokertegn DNS-filtrering (`-wd`)

Jokertegn returnerer en gyldig IP for ikke-eksisterende underdomener (`random123xyz.target.com`), forgiftende rekognoseringsdatasett.
```bash
# Filter wildcard subdomains and print only genuine hosts
dnsx -l subdomains.txt -wd target.com -o valid_hosts.txt

# Filter wildcards with custom threshold
dnsx -l subdomains.txt -wd target.com -wt 3
```
---

## 📋 Spørre om spesifikke DNS-posttyper

Søk etter spesifikke posttyper på tvers av tusenvis av domener:
```bash
# Query CNAME records (Crucial for Subdomain Takeover detection)
dnsx -l subdomains.txt -cname -resp

# Query Mail Exchanger (MX) records
dnsx -l domains.txt -mx -resp

# Query TXT records (SPF, DKIM, verification tokens)
dnsx -l domains.txt -txt -resp

# Query All standard records (A, AAAA, CNAME, NS, TXT, MX, SOA)
dnsx -l domains.txt -recon
```
---

## 🔄 Omvendt DNS-oppslag (`-ptr`)

Oppdag aktiv infrastruktur og virtuelle verter ved å utføre PTR-oppslag på et IP-område:
```bash
# Reverse resolve a CIDR IP range to hostnames
dnsx -ptr -l ip_ranges.txt -resp

# Reverse resolve a single subnet
echo "192.168.1.0/24" | dnsx -ptr -resp
```
---

## ⚙️ Bruk av egendefinerte løsere og ratebegrensning

Unngå å bli hastighetsbegrenset av Internett-leverandøren eller Google DNS ved å bruke en høyhastighets kuratert oppløsningspool:
```bash
# Use a custom trusted resolver list
dnsx -l subdomains.txt -r resolvers.txt

# Set thread concurrency and query rate limit (queries per second)
dnsx -l subdomains.txt -t 100 -rl 300
```
---

## ⛓️ Pipeline Automation (Subfinder + dnsx + httpx)

Den ultimate recon-arbeidsflyten for bug-premiejegere og penetrasjonstestere:
```bash
# Discover subdomains -> Validate via dnsx -> Probe live HTTP web services
subfinder -d target.com -silent | \
  dnsx -silent -wd target.com | \
  httpx -silent -status-code -title -tech-detect
```
---

## ⚡ Hurtigreferansetabell

| Mål | Kommando |
|----------------|--------|
| **Løs underdomener** | `dnsx -l subdomains.txt` |
| **Pak kun ut IP-adresser** | `dnsx -l subdomains.txt -resp-only` |
| **Filtrer jokertegndomener** | `dnsx -l subdomains.txt -wd <DOMAIN>` |
| ** Trekk ut CNAME (Takeover)** | `dnsx -l subdomains.txt -cname -resp` |
| **Søk alle poster (Recon)** | `dnsx -l domains.txt -recon` |
| **Omvendt DNS-oppslag (PTR)** | `dnsx -l ips.txt -ptr -resp` |
| **Egendefinerte løsere og tråder**| `dnsx -l list.txt -r resolvers.txt -t 150` |
| **JSON-utgang** | `dnsx -l list.txt -json -o results.json` |

---

## 🛡️ Defensive betraktninger og OPSEC

1. **DNS-oversvømmelse:** Å sende titusenvis av DNS-spørringer gjennom bedriftens interne DNS-servere vil øke CPU-belastningen og utløse SIEM-varsler for DNS-tunneling/DDoS.
2. **Resolverforgiftning:** Bruk alltid verifiserte, pålitelige resolverlister for å forhindre ondsinnede løsere fra å fange opp spørringer.

---

## ⚠️ Juridisk ansvarsfraskrivelse

> [!FORSIKTIG]
> Dette juksearket er kun for autorisert penetrasjonstesting og bug bounty-engasjementer. Det er strengt forbudt å utføre høyvolums DNS-sonder mot uautorisert infrastruktur.