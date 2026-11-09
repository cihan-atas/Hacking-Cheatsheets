<!-- language-home-navigation -->
[🏠 Norsk Hovedside](../../README.nb.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇬🇧 English](../README.md)

---


# 🪁 Kiterunner - Komplett jukseark

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ██╗  ██╗██╗████████╗███████╗██████╗ ██╗   ██╗███╗   ██╗███╗   ██╗███████╗██████╗ 
  ██║ ██╔╝██║╚══██╔══╝██╔════╝██╔══██╗██║   ██║████╗  ██║████╗  ██║██╔════╝██╔══██╗
  █████╔╝ ██║   ██║   █████╗  ██████╔╝██║   ██║██╔██╗ ██║██╔██╗ ██║█████╗  ██████╔╝
  ██╔═██╗ ██║   ██║   ██╔══╝  ██╔══██╗██║   ██║██║╚██╗██║██║╚██╗██║██╔══╝  ██╔══██╗
  ██║  ██╗██║   ██║   ███████╗██║  ██║╚██████╔╝██║ ╚████║██║ ╚████║███████╗██║  ██║
  ╚═╝  ╚═╝╚═╝   ╚═╝   ╚══════╝╚═╝  ╚═╝ ╚═════╝ ╚═╝  ╚═══╝╚═╝  ╚═══╝╚══════╝╚═╝  ╚═╝
       Context-Aware API Endpoint & Route Discovery Engine by Assetnote
```
<p align="center">
  <img src="https://img.shields.io/badge/Kiterunner-API_Discovery-blue?style=for-the-badge" alt="Kiterunner">
  <img src="https://img.shields.io/badge/Author-Assetnote-orange?style=for-the-badge" alt="Assetnote">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-green?style=for-the-badge" alt="Nettsikkerhet og API">
  <img src="https://img.shields.io/badge/License-Apache_2.0-red?style=for-the-badge" alt="License Apache 2.0">
</p>

<p align="center">
  <b>🌐 Høyytelses REST og GraphQL API rute brute-forcer designet for å kartlegge moderne API-arkitekturer ved hjelp av kontekstuelle datasett</b>
</p>

---

## 📋 Innholdsfortegnelse

- [Oversikt og problem med API-oppdagelse](#-oversikt--api-oppdagelsesproblemet)
- [Verktøysammenligning: Kiterunner vs ffuf vs dirsearch](#-tool-comparison-kiterunner-vs-ffuf-vs-dirsearch)
- [Installasjon](#-installasjon)
- [Last ned Assetnote Kite-datasett](#-nedlasting-assetnote-kite-datasett)
- [Scanning APIs (`kr scan`)](#-scanning-apis-kr-scan)
- [Standard Wordlist Brute-Forcing (`kr brute`)](#-standard-wordlist-brute-forcing-kr-brute)
- [Overskrifter, informasjonskapsler og autentisering](#-headers-informasjonskapsler--autentisering)
- [Filtrering og statuskoder](#-filtrering--statuskoder)
- [Replaying Discovered Routes (`curl` output)](#-replaying-discovered-routes-curl-output)
- [Hurtigreferansetabell](#-hurtigreferansetabell)
– [Defensive Considerations & OPSEC](#-defensive-considerations--opsec)
– [Juridisk ansvarsfraskrivelse](#-juridisk ansvarsfraskrivelse)

---

## 🎯 Oversikt og API-oppdagelsesproblemet

Tradisjonelle verktøy for oppdagelse av nettinnhold (`ffuf`, `dirsearch`, `gobuster`) bruker flate ordbøker med ett ord (f.eks. `admin`, `api`, `login`). Selv om dette fungerer for tradisjonelle filhierarkier, **feiler det mot moderne REST og GraphQL APIer**.

Moderne API-er bruker nestede ruter med flere lag som:
`/api/v2/organizations/{org_id}/users/export?format=json`

**Kiterunner**, laget av **Assetnote**, løser dette ved å kompilere virkelige OpenAPI, Swagger, Postman og skjemadefinisjoner til kompakte binære datasett (`.kite`). I stedet for å gjette tilfeldige ord, rekonstruerer Kiterunner hele hierarkiske API-anropsstrukturer med korrekte HTTP-metoder (`GET`, `POST`, `PUT`, `DELETE`).

---

## ⚖️ Verktøysammenligning: Kiterunner vs ffuf vs dirsearch

| Funksjon | Kiterunner (`kr`) | ffuf | dirsearch |
|--------|----------------|------|--------|
| **Kjernemålfokus** | REST, GraphQL, OpenAPI | Generell Web Fuzzing | Katalog Brute-Forcing |
| **Rutehierarkibevissthet**| ✅ Native (`.kite`-filer) | ❌ Flat ordliste | ❌ Flat ordliste |
| **HTTP-verbbytte** | ✅ Tester GET, POST, PUT, DELETE| ❌ Manuell konfigurasjon | ❌ Begrenset |
| **Swagger / OpenAPI Parsing** | ✅ Inntak av naturlig skjema | ❌ Nei | ❌ Nei |
| **Hastighet og samtidighet** | ⭐⭐⭐⭐⭐ (Go Engine) | ⭐⭐⭐⭐⭐ (Go Engine) | ⭐⭐⭐⭐ (Python) |

---

## 📥 Installasjon
```bash
# Download precompiled binary from GitHub Releases
wget https://github.com/assetnote/kiterunner/releases/download/v1.0.2/kiterunner_1.0.2_linux_amd64.tar.gz
tar -xzvf kiterunner_1.0.2_linux_amd64.tar.gz
sudo mv kr /usr/local/bin/

# Verify installation
kr version
```
---

## 📦 Laster ned Assetnote Kite-datasett

Kiterunner er avhengig av forhåndsbygde `.kite`-rutedatasett laget av millioner av analyserte offentlige APIer:
```bash
# Create wordlists directory
mkdir -p ~/.kiterunner && cd ~/.kiterunner

# Small dataset (~40MB, 2.5 million API routes - Recommended for fast scans)
wget https://wordlists-cdn.assetnote.io/data/kiterunner/routes-small.kite.tar.gz
tar -xzvf routes-small.kite.tar.gz

# Large dataset (~130MB, 8.5 million API routes - Exhaustive testing)
wget https://wordlists-cdn.assetnote.io/data/kiterunner/routes-large.kite.tar.gz
tar -xzvf routes-large.kite.tar.gz
```
---

## 🚀 Skanne-APIer (`kr scan`)

`scan`-kommandoen bruker `.kite`-datasett for å undersøke mål ved å bruke kontekstuelle rutehierarkier:
```bash
# Scan a single target using the small dataset
kr scan https://target.local/api -w ~/.kiterunner/routes-small.kite

# Scan multiple hosts from a file with 100 concurrent workers
kr scan targets.txt -w ~/.kiterunner/routes-small.kite -c 100 -j 50

# Scan with maximum verbosity
kr scan https://target.local -w ~/.kiterunner/routes-small.kite -v
```
---

## 🔨 Standard ordliste Brute-Forcing (`kr brute`)

Hvis du vil bruke standard `.txt`-ordlister mens du drar nytte av Kiterunners multi-metode API-motor:
```bash
# Brute force standard paths using a text dictionary
kr brute https://target.local -w /usr/share/wordlists/dirb/common.txt

# Brute force with custom API prefixes
kr brute https://target.local/api/v1 -w endpoints.txt
```
---

## 🔑 Overskrifter, informasjonskapsler og autentisering

APIer krever ofte JWT-er, Bearer-tokens eller tilpassede organisasjonsoverskrifter:
```bash
# Pass custom Authorization header
kr scan https://target.local/api -w routes-small.kite -H "Authorization: Bearer eyJhbGci..."

# Pass custom API key and Cookie
kr scan https://target.local -w routes-small.kite -H "X-API-Key: secret123" -H "Cookie: session=xyz"
```
---

## 🎯 Filtrering og statuskoder
```bash
# Exclude standard error codes (404, 403, 502)
kr scan https://target.local -w routes-small.kite -x 404,403,502

# Filter out responses by length / content size
kr scan https://target.local -w routes-small.kite --filter-length 0,324
```
---

## 🔄 Spille oppdagede ruter på nytt (`curl`-utgang)

Kiterunner kan sende ut oppdagede ruter som handlingsbare `curl`-kommandoer, noe som tillater øyeblikkelig reproduksjon i terminal eller Burp Suite:
```bash
# Print reproducible curl command lines for all successful matches
kr scan https://target.local -w routes-small.kite -o json | jq -r '.[] | .curl'
```
---

## ⚡ Hurtigreferansetabell

| Mål | Kommando |
|----------------|--------|
| **Skann API med lite datasett** | `kr scan <URL> -w routes-small.kite` |
| **Skann API med stort datasett** | `kr scan <URL> -w routes-large.kite` |
| **Skann flere verter** | `kr scan hosts.txt -w routes-small.kite -c 100` |
| **Brute Force Text Wordlist** | `kr brute <URL> -w wordlist.txt` |
| **Autentiser med Bearer Token**| `kr scan <URL> -w routes-small.kite -H "Authorization: Bearer <TOKEN>"` |
| **Ekskluder statuskoder** | `kr scan <URL> -w routes-small.kite -x 404,403` |
| **Utgang til JSON-fil** | `kr scan <URL> -w routes-small.kite -o json -oFile results.json` |

---

## 🛡️ Defensive betraktninger og OPSEC

1. **Massive Request Volume:** `routes-large.kite`-ordboken genererer millioner av forespørsler. Sørg for at hastighetsbegrensning (`-q 200`) er aktivert når du tester produksjonsmiljøer.
2. **Statusendrende metoder:** Kiterunner sender `POST`-, `PUT`- og `DELETE`-forespørsler. På uherdede test-API-er kan dette utilsiktet endre eller slette backend-poster.

---

## ⚠️ Juridisk ansvarsfraskrivelse

> [!FORSIKTIG]
> Kiterunner er kun utviklet for autorisert API-sikkerhetstesting og bug bounty-engasjementer. Fuzzing av uautoriserte APIer er strengt ulovlig.