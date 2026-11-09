<!-- language-home-navigation -->
[🏠 Norsk Hovedside](../../README.nb.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇬🇧 English](../README.md)

---


# wafw00f Jukseark

[wafw00f](https://github.com/EnableSecurity/wafw00f) er et åpen kildekode-kommandolinje for Web Application Firewall (WAF) fingeravtrykk og deteksjonsverktøy. Den identifiserer og tar fingeravtrykk over 150 kommersielle og åpen kildekode WAF-sikkerhetsløsninger som beskytter målnettsteder ved å sende normale og provoserende HTTP-forespørsler og analysere atferdsreaksjoner, overskrifter og informasjonskapsler.

---

## Innholdsfortegnelse

- [Hvorfor Fingerprint WAFs?](#why-fingerprint-wafs)
- [Installasjon](#installation)
– [Grunnleggende og målskanning](#grunnleggende--målskanning)
- [Utømmende multi-WAF-deteksjon (`-a`)](#exhaustive-multi-waf-detection--a)
- [Masseskanning fra fil (`-i`)](#bulk-målskanning-fra-fil--i)
- [Proxy- og Tor-ruting (`-p`)](#proxy--tor-routing--p)
- [Eksporter formater (JSON, CSV, tekst)](#export-formats-json-csv-text)
- [Liste støttede WAF-signaturer (`-l`)](#list-supported-waf-signatures--l)
- [Bug Bounty & Penetration Testing Integration](#bug-bounty--penetration-testing-integration)

---

## Hvorfor Fingerprint WAFs?

Å starte aktive nettangrep (SQL-injeksjon, XSS, SSRF) mot et beskyttet mål uten forutgående rekognosering resulterer ofte i umiddelbare IP-forbud eller falske negative sårbarhetsresultater. Å identifisere den nøyaktige WAF gjør det mulig:
- **Tilpasset unndragelse:** Velge leverandørspesifikke bypass-teknikker (f.eks. Unicode-normalisering for ModSecurity, chunked transfer-koding for Cloudflare).
- **Opprinnelses-IP-jakt:** Hvis Cloudflare eller Akamai blir oppdaget, pivotering for å finne opprinnelses-IP-en for backend før testing.
- **Regelterskelbevissthet:** Juster skannerens samtidighet og hastighetsgrenser under blokkeringsterskler.

---

## Installasjon
```bash
# Debian / Ubuntu / Kali Linux
sudo apt-get update && sudo apt-get install -y wafw00f

# Recommended: Install via pipx
pipx install wafw00f

# Via standard Python pip
python3 -m pip install --user wafw00f

# Verify installation
wafw00f --version
```
---

## Grunnleggende og målskanning

### 1. Skann et enkelt domene
```bash
wafw00f https://example.com
```
*wafw00f sender baseline HTTP-forespørsler etterfulgt av skreddersydde ondsinnede mønstre for å utløse sikkerhetsfiltre, identifisere signaturer i overskrifter, statuskoder og feilsider.*

### 2. Utførlig utgangsmodus (`-v` / `-vv`)
Inspiser spesifikke samsvarende overskrifter, HTTP-statuskoder og heuristikk:
```bash
wafw00f https://example.com -v
```
### 3. Deaktiver omdirigering (`-r`)
Hindre wafw00f fra å følge 301/302-omdirigeringer for å skille kant-omvendte proxyer fra backend-applikasjoner:
```bash
wafw00f https://example.com -r
```
---

## Uttømmende multi-WAF-deteksjon (`-a`)

Som standard stopper wafw00f skanningen så snart den første WAF-matchen er bekreftet. Bruk `-a` til å sjekke alle kjente leverandørsignaturer (nødvendig når en applikasjon sitter bak et flerlagsforsvar som Cloudflare CDN + AWS WAF):
```bash
wafw00f https://example.com -a
```
---

## Massemålskanning fra fil (`-i`)

Skann en hel liste over underdomener eller målressurser samlet fra `subfinder` eller `amass`:
```bash
wafw00f -i /path/to/domains.txt -o /tmp/waf_results.txt
```
---

## Proxy- og Tor-ruting (`-p`)

Ruter sonderingstrafikk gjennom proxyer for å forhindre IP-svartelisting eller inspiser spørringer i Burp Suite:

### 1. Rute gjennom Burp Suite Proxy
```bash
wafw00f https://example.com -p http://127.0.0.1:8080
```
### 2. Rute gjennom lokal Tor SOCKS5 Proxy
```bash
wafw00f https://example.com -p socks5://127.0.0.1:9050
```
---

## Eksportformater (JSON, CSV, tekst)

Eksporter resultater for automatisert rapportering eller nedstrøms rekognoseringsverktøy:

### 1. Eksporter til JSON-format
```bash
wafw00f https://example.com -o results.json --format json
```
### 2. Eksporter til CSV-regneark
```bash
wafw00f -i targets.txt -o results.csv --format csv
```
---

## Liste støttede WAF-signaturer (`-l`)

Se alle 150+ detekterbare WAF-produkter:
```bash
wafw00f -l
```
Viktige støttede produkter inkluderer:
- **Skyleverandører:** Cloudflare, AWS WAF, Akamai, Microsoft Azure Web Application Firewall, Google Cloud Armor.
- **Enterprise Appliances:** F5 BIG-IP ASM, Imperva SecureSphere, Barracuda, Fortinet FortiWeb, Citrix NetScaler.
- **Åpen kildekode / programvare:** ModSecurity, NAXSI, Shadow Daemon, OpenResty.

---

## Bug Bounty & Penetration Testing Integration

Integrer wafw00f i en automatisert nettangrepspipeline:
```bash
#!/usr/bin/env bash
TARGET="https://app.target.corp"

echo "[*] Detecting WAF for $TARGET..."
WAF=$(wafw00f "$TARGET" -o - --format json | jq -r '.[0].detected')

if [ "$WAF" == "true" ]; then
    WAF_NAME=$(wafw00f "$TARGET" -o - --format json | jq -r '.[0].firewall')
    echo "[!] Active WAF detected: $WAF_NAME"
    echo "[+] Adjusting sqlmap to use vendor-specific tamper scripts..."
    if [[ "$WAF_NAME" =~ "Cloudflare" ]]; then
        sqlmap -u "$TARGET/item?id=1" --tamper=between,randomcase,space2comment
    fi
else
    echo "[+] No active WAF detected. Proceeding with standard scanner."
    sqlmap -u "$TARGET/item?id=1" --batch
fi
```