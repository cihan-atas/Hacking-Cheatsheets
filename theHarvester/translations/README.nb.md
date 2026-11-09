<!-- language-home-navigation -->
[🏠 Norsk Hovedside](../../README.nb.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇬🇧 English](../README.md)

---


# theHarvester Cheatsheet

[theHarvester](https://github.com/laramies/theHarvester) er et av de mest brukte verktøyene for åpen kildekodeintelligens (OSINT) for å samle e-poster, underdomener, ansattes navn, virtuelle verter, åpne porter og ansattes PGP-nøkler fra ulike offentlige kilder (søkemotorer, PGP-nøkkelservere, Certspotterys, etc.).

---

## Innholdsfortegnelse

- [Støttede datakilder](#supported-data-sources)
- [Installasjon og API-nøkkelkonfigurasjon](#installation--api-key-configuration)
– [Basic Domain Reconnaissance](#basic-domain-reconnaissance)
– [Passiv multikildehøsting](#passiv-multikildehøsting)
- [Aktiv DNS-verifisering og brute-forcing](#active-dns-verification--brute-forcing)
- [Utdataformater (HTML, XML, JSON)](#output-formats-html-xml-json)
- [Pentest Workflow Integration](#pentest-workflow-integration)

---

## Støttede datakilder

theHarvester samler intelligens fra dusinvis av passive og aktive kilder:
- **Søkemotorer (gratis / uten nøkkel):** Google, Bing, DuckDuckGo, Yahoo, Baidu.
- **Sertifikatgjennomsiktighet:** CRT.sh, Certspotter.
- **Kode- og trussellager:** GitHub, AlienVault OTX, ThreatCrowd, Rapid7, VirusTotal.
- **Rekognoserings- og OSINT APIer:** Shodan, Censys, Hunter.io, SecurityTrails, Intelx, FullContact.
- **Nettverk / DNS:** Anubis, HackerTarget, DNSDumpster.

---

## Installasjon og API-nøkkelkonfigurasjon

### 1. Installasjon
```bash
# Debian / Ubuntu / Kali Linux
sudo apt-get update && sudo apt-get install -y theharvester

# From GitHub Source (Latest Version)
git clone https://github.com/laramies/theHarvester.git
cd theHarvester
python3 -m pip install -r requirements/base.txt

# Test binary
theHarvester -h
```
### 2. Konfigurer API-nøkler (`api-keys.yaml`)
Mange kilder (Hunter, Shodan, Censys, Intelx, GitHub) krever gratis eller betalte API-nøkler.
Opprett eller rediger `api-keys.yaml` i konfigurasjonsbanen:
```yaml
apikeys:
  hunter:
    key: "YOUR_HUNTER_KEY"
  shodan:
    key: "YOUR_SHODAN_KEY"
  github:
    key: "YOUR_GITHUB_TOKEN"
  securityTrails:
    key: "YOUR_SECURITYTRAILS_KEY"
```
---

## Grunnleggende domenerekognosering

### 1. Innhent e-poster og underdomener ved hjelp av gratis motorer
```bash
theHarvester -d example.com -l 500 -b duckduckgo,bing,crtsh
```
- `-d <domain>`: Mål domene for å rekon.
- `-l <limit>`: Antall resultater som skal søkes per motor.
- `-b <sources>`: Kommaseparert liste over kilder (eller `all`).

### 2. Spør etter alle konfigurerte kilder
```bash
theHarvester -d example.com -l 500 -b all
```
---

## Passiv flerkildehøsting

Søk etter spesifikk trusselintelligens og sertifikattransparensdatabaser uten å sende trafikk til målet:

### 1. Sertifikatgjennomsiktighet og trussel Intel
```bash
theHarvester -d example.com -b crtsh,certspotter,anubis,hackertarget,otx
```
### 2. Målrett mot spesifikke kildegrupper
```bash
# Query GitHub commits and issues for leaked emails and subdomains
theHarvester -d example.com -b github-code

# Query Shodan for discovered IP addresses and open ports
theHarvester -d example.com -b shodan
```
---

## Aktiv DNS-verifisering og brute-Forcing

### 1. Løs oppdagede underdomener (`-r`)
Utfør aktive DNS-oppslag for å bekrefte om innsamlede underdomener for øyeblikket er aktive:
```bash
theHarvester -d example.com -b crtsh,bing -r
```
### 2. DNS Brute-Force (`-c`)
Utfør ordlistebasert DNS-underdomene brute-forcing direkte i Harvester:
```bash
theHarvester -d example.com -b duckduckgo -c
```
### 3. Verifisering av virtuell vert (`-v`)
Bekreft virtuelle verter via HTTP-forespørsler:
```bash
theHarvester -d example.com -b duckduckgo -v
```
---

## Utdataformater (HTML, XML, JSON)

Eksporter resultater for automatisert rapportering eller nedstrøms rekognosering:

### 1. Eksporter til HTML og XML
```bash
theHarvester -d example.com -b duckduckgo,crtsh -f /tmp/theharvester_results
```
Oppretter `/tmp/theharvester_results.html` og `/tmp/theharvester_results.xml`.

### 2. Parse oppdagede underdomener med `jq`
Hvis du eksporterer eller analyserer JSON:
```bash
theHarvester -d example.com -b crtsh,duckduckgo -f recon_out
# Extract clean host list
cat recon_out.xml | grep -oP '(?<=<host>).*?(?=</host>)' | sort -u > live_hosts.txt
```
---

## Pentest arbeidsflytintegrasjon

Koble inn Harvester-resultatene direkte med aktive sårbarhetsskannere:
```bash
# 1. Harvest passive subdomains
theHarvester -d target.corp -b crtsh,duckduckgo,bing -f target_recon

# 2. Extract unique domain names
grep -oP '(?<=<host>)[^<]+' target_recon.xml | sort -u > hosts.txt

# 3. Probe for live web services using httpx
cat hosts.txt | httpx -silent -status-code -title > live_web.txt

# 4. Scan live web services with Nuclei
nuclei -l live_web.txt -severity medium,high,critical
```