<!-- language-home-navigation -->
[🏠 Norsk Hovedside](../../README.nb.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇬🇧 English](../README.md)

---


# dnsrecon Jukseark

[dnsrecon](https://github.com/darkoperator/dnsrecon) er et omfattende DNS-rekognoserings- og opptellingsverktøy skrevet i Python. Den støtter soneoverføringer (AXFR), reverse lookups, domene brute-forcing, SRV-postoppdagelse, jokertegndeteksjon, cache snooping og TLD-utvidelse.

---

## Innholdsfortegnelse

- [Kjerneegenskaper](#kjernefunksjoner)
- [Installasjon](#installation)
- [Standard Enumeration & Zone Transfers (`-d -t std / axfr`)](#standard-enumeration--zone-transfers--d--t-std--axfr)
- [DNS Subdomain Brute-Forcing (`-t brt`)](#dns-subdomain-brute-forcing--t-brt)
- [Reverse Lookup Range Scanning (`-t rvl`)](#reverse-lookup-range-scanning--t-rvl)
- [DNS Cache Snooping (`-t snoop`)](#dns-cache-snooping--t-snoop)
– [Targeting Specific Nameservers (`-n`)](#targeting-specific-nameservers--n)
- [Utdataformater (CSV, JSON, SQLite, XML)](#output-formats-csv-json-sqlite-xml)
- [Defensive Hardening for DNS-servere](#defensive-hardening-for-dns-servers)

---

## Kjernefunksjoner

- **Sone Transfer Testing (AXFR):** Tester automatisk alle autoritative navneservere for uautentiserte soneoverføringer.
- **Rekordoppregning:** Henter SOA-, NS-, A-, AAAA-, MX-, TXT-, SPF- og SRV-poster.
- **Underdomene Brute-Force:** Flertrådede ordbokoppslag med jokertegnfiltrering.
- **Omvendt IP-områdeskanning:** PTR-postutvinning på tvers av vilkårlige CIDR-blokker.
- **Cache Snooping:** Tester om rekursive navneservere har spesifikke domener bufret i minnet.
- **TLD-utvidelse:** Oppdager matchende domener på tvers av landskoder og generiske toppdomener.

---

## Installasjon
```bash
# Debian / Ubuntu / Kali Linux
sudo apt-get update && sudo apt-get install -y dnsrecon

# Python pip / pipx
pipx install dnsrecon

# From source
git clone https://github.com/darkoperator/dnsrecon.git
cd dnsrecon
pip install -r requirements.txt

# Verify installation
dnsrecon -h
```
---

## Standard opptelling og soneoverføringer (`-d -t std / axfr`)

### 1. Standard DNS Record Enumeration
Samler SOA-, NS-, A-, AAAA-, MX- og TXT-poster og forsøker automatisk soneoverføringer mot hver autoritativ NS:
```bash
dnsrecon -d example.com -t std
```
### 2. Kun test for dedikert soneoverføring (AXFR).
Test kun for AXFR-sårbarhet mot alle autoritative navneservere:
```bash
dnsrecon -d example.com -t axfr
```
*Hvis det lykkes, blir hele DNS-databasen til målet dumpet uten brute force.*

---

## DNS Subdomain Brute-Forcing (`-t brt`)

Brute-force underdomener som bruker egendefinerte eller standard ordlister:

### 1. Subdomene Brute-Force med tilpasset ordliste
```bash
dnsrecon -d example.com -t brt -D /usr/share/wordlists/dnsmap.txt
```
- `-d <domain>`: Måldomene.
- `-t brt`: Brute-force-modus.
- `-D <file>`: Ordbokfil for underdomene.

### 2. Brute-Force med jokertegnfiltrering
Hindrer jokertegn DNS-konfigurasjoner (`*.example.com` løses til en enkelt oppsamlingsadresse) fra forurensende resultater:
```bash
dnsrecon -d example.com -t brt -D /usr/share/seclists/Discovery/DNS/subdomains-top1million-5000.txt --iw
```
- `--iw`: Ignorer resultater med jokertegnoppløsning.

---

## Reverse Lookup Range Scanning (`-t rvl`)

Oppregne PTR-poster for en IP-blokk for å identifisere interne verter, e-postgatewayer og oppsamlingsforekomster:
```bash
dnsrecon -r 192.168.1.0/24 -t rvl
```
- `-r <range>`: IP-område i CIDR-notasjon (f.eks. `192.168.1.0/24`) eller start-end-notasjon (`192.168.1.1-192.168.1.254`).
- `-t rvl`: Omvendt oppslagsmodus.

---

## DNS Cache Snooping (`-t snoop`)

Finn ut om klienter på et målnettverk nylig har løst spesifikke domener gjennom sin interne løser:
```bash
dnsrecon -t snoop -n 192.168.1.1 -D sensitive_domains.txt
```
- `-n <resolver_ip>`: Målrett DNS-serveren for å spørre.
- `-D <domains_file>`: Liste over domener som skal testes (f.eks. konkurrenter, ondsinnede C2-er, interne portaler).

---

## Målretting mot spesifikke navneservere (`-n`)

Spør interne eller målspesifikke DNS-servere direkte i stedet for offentlige oppstrømsløsere:
```bash
# Query the company's internal DNS server directly
dnsrecon -d corp.internal -n 10.10.10.2 -t std
```
---

## Utdataformater (CSV, JSON, SQLite, XML)

Lagre resultater for integrasjon med billettsalg, SIEM eller ytterligere angrepsfaser:

### 1. Eksporter til JSON
```bash
dnsrecon -d example.com -t std --json /tmp/dns_recon.json
```
### 2. Eksporter til SQLite-databasen
```bash
dnsrecon -d example.com -t std --db /tmp/dns_results.sqlite
```
### 3. Eksporter til CSV og XML
```bash
dnsrecon -d example.com -t std --csv /tmp/dns_results.csv --xml /tmp/dns_results.xml
```
---

## Defensiv herding for DNS-servere

| Sårbarhet | Utnyttelsesrisiko | Anbefalt forsvar |
|---|---|---|
| **Ubegrenset AXFR** | Full intern infrastrukturkartlegging | Begrens soneoverføringer til kun autoriserte sekundære navneserver-IP-er (`allow-transfer { 10.0.0.2; };` i BIND). |
| **Åpne rekursiv resolver** | DNS-forsterkning DDoS og snoking | Deaktiver ekstern rekursjon (`recursion no;` i BIND). |
| **Forutsigbare vertsnavn** | Subdomene brute-forcing | Bruk randomiserte underdomener for sensitive scenemiljøer. |
| **Overtakelse av underdomene** | CNAME som peker til AWS/GitHub/Azure-tjenester som ikke er gjort krav på | Revider dinglende DNS CNAME-poster og fjern ubrukte tilordninger umiddelbart. |