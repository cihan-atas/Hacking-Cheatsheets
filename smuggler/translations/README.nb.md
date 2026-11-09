<!-- language-home-navigation -->
[🏠 Norsk Hovedside](../../README.nb.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇬🇧 English](../README.md)

---


# 📦 smugler - komplett jukseark

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ███████╗███╗   ███╗██╗   ██╗ ██████╗  ██████╗ ██╗     ███████╗██████╗ 
  ██╔════╝████╗ ████║██║   ██║██╔════╝ ██╔════╝ ██║     ██╔════╝██╔══██╗
  ███████╗██╔████╔██║██║   ██║██║  ███╗██║  ███╗██║     █████╗  ██████╔╝
  ╚════██║██║╚██╔╝██║██║   ██║██║   ██║██║   ██║██║     ██╔══╝  ██╔══██╗
  ███████║██║ ╚═╝ ██║╚██████╔╝╚██████╔╝╚██████╔╝███████╗███████╗██║  ██║
  ╚══════╝╚═╝     ╚═╝ ╚═════╝  ╚═════╝  ╚═════╝ ╚══════╝╚══════╝╚═╝  ╚═╝
             High-Performance HTTP Request Smuggling Scanner
```
<p align="center">
  <img src="https://img.shields.io/badge/smuggler-HTTP_Smuggling-blue?style=for-the-badge" alt="smuggler">
  <img src="https://img.shields.io/badge/Author-Evan_Ricaurte-orange?style=for-the-badge" alt="defparam">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-green?style=for-the-badge" alt="Nettsikkerhet">
  <img src="https://img.shields.io/badge/License-MIT-purple?style=for-the-badge" alt="MIT">
</p>

<p align="center">
  <b>📦 Ultraraskt, modulært Python-verktøy designet for å oppdage og verifisere sårbarheter i HTTP-forespørselssmugling (CL.TE, TE.CL og header-mutasjonsdesynkroniseringer) på tvers av moderne nettinfrastruktur</b>
</p>

---

## 📋 Innholdsfortegnelse

- [Oversikt og HTTP-smuglingsteori](#-oversikt--http-smuggling-teori)
  - [CL.TE vs TE.CL-avvik](#clte-vs-tecl-avvik)
- [Installasjon](#-installasjon)
– [Grunnleggende målskanning](#-grunnleggende målskanning)
- [Skannekonfigurasjoner og mutasjonsmotorer](#-skannekonfigurasjoner--mutasjonsmotorer)
  - [Default vs Exhaustive Scan](#default-vs-exhaustive-scan)
  - [Egendefinerte overskrifter og informasjonskapsler](#tilpassede overskrifter--informasjonskapsler)
- [Multi-Target & Pipeline Scanning](#-multi-target--pipeline-scanning)
- [Analyse av resultater og utnyttelsesveier](#-analyse-resultater--utnyttelsesbaner)
  - [Cache-forgiftning via smugling](#cache-forgiftning-via-smugling)
  - [Omgå front-end-sikkerhetskontroller](#bypassing-front-end-security-controls)
  - [Hijacking Other Users's Requests](#hijacking-other-users-requests)
- [Hurtigreferansetabell](#-hurtigreferansetabell)
– [Defensive Considerations & Remediation](#-defensive-considerations--remediation)
– [Juridisk ansvarsfraskrivelse](#-juridisk ansvarsfraskrivelse)

---

## 🎯 Oversikt og HTTP-smuglingsteori

HTTP-forespørselssmugling skjer når en front-end proxy/load balancer og en back-end webserver er uenige om grensene for en HTTP-forespørsel:

### CL.TE vs TE.CL Uoverensstemmelser
- **CL.TE:** Front-end bruker `Content-Length`, back-end bruker `Transfer-Encoding: chunked`.
- **TE.CL:** Front-end bruker `Transfer-Encoding: chunked`, back-end bruker `Content-Length`.
- **TE.TE:** Begge støtter `Transfer-Encoding`, men man kan bli lurt til å ignorere det via header-obfuskering (f.eks. `Transfer-Encoding: xchunked`, tabulatorer eller linjefolding).

Laget av **defparam (Evan Ricaurte)**, **smugler** injiserer nøye utformede nyttelaster for å observere tidsforsinkelser eller desynkroniseringssvar uten å bryte stabiliteten i backend-tjenesten.

---

## 📥 Installasjon
```bash
# Clone the repository
git clone https://github.com/defparam/smuggler.git
cd smuggler

# Run using Python 3
python3 smuggler.py -h
```
---

## 🚀 Grunnleggende målskanning

Skann et enkelt nettapplikasjonsendepunkt:
```bash
# Basic scan against a single target
python3 smuggler.py -u https://target.com/
```
Som standard tester smugler for både `CL.TE`- og `TE.CL`-atferd ved hjelp av sikker timing-nyttelast.

---

## ⚙️ Skannekonfigurasjoner og mutasjonsmotorer

### Standard kontra uttømmende skanning
Smugler inkluderer forskjellige nyttelastprofiler:
- **Standardprofil:** Raske, vanlige desynkroniseringsmutasjoner.
- **Utømmende profil:** Omfattende mutasjoner som tester mellomromsvariasjoner, flere overskrifter og obfuskerte overføringskodingsoverskrifter:
```bash
# Run exhaustive scan testing all mutation gadgets
python3 smuggler.py -u https://target.com/ -c exhaustive
```
### Egendefinerte overskrifter og informasjonskapsler
Når autentisering eller spesifikke vertsoverskrifter kreves:
```bash
python3 smuggler.py -u https://target.com/api/ \
  -H "Cookie: session=xyz123" \
  -H "X-Forwarded-Host: target.com"
```
---

## 🌐 Multi-Target & Pipeline Scanning

Rør endepunkter oppdaget fra `httpx` eller `subfinder` direkte til smugler:
```bash
# Scan a list of URLs from file
python3 smuggler.py -f live_hosts.txt -t 10

# Adjust timeout per probe (default: 10s)
python3 smuggler.py -u https://target.com/ --timeout 5
```
---

## 💥 Analyserer resultater og utnyttelsesveier

Når smugler rapporterer en positiv kamp:
```text
[CRITICAL] CL.TE Desync Detected: https://target.com/
```
### Cacheforgiftning via smugling
Smugle en forespørsel som genererer en omdirigering utenfor nettstedet (f.eks. vertshodemanipulering) for å forgifte nettbuffere som serverer JavaScript-biblioteker til alle brukere.

### Omgå front-end sikkerhetskontroller
Hvis front-end proxy blokkerer `/admin`, smugler du en sekundær intern forespørsel som når back-end direkte uten proxy inspeksjon:
```http
POST / HTTP/1.1
Host: target.com
Content-Length: 48
Transfer-Encoding: chunked

0

GET /admin HTTP/1.1
Host: localhost
```
### Kapring andre brukeres forespørsler
Smugle et prefiks til en HTTP-forespørsel. Når neste bruker sender forespørselen sin, blir den lagt til den smuglede forespørselen din, og avslører informasjonskapslene og økttokenene deres.

---

## ⚡ Hurtigreferansetabell

| Mål | Kommando |
|----------------|--------|
| **Rask enkeltmål** | `python3 smuggler.py -u https://target.com/` |
| **Utømmende dypskanning**| `python3 smuggler.py -u https://target.com/ -c exhaustive` |
| **Skann målliste** | `python3 smuggler.py -f targets.txt -t 15` |
| **Egendefinerte overskrifter/informasjonskapsler**| `python3 smuggler.py -u <URL> -H "Cookie: sess=123"` |
| **Egendefinert tidsavbrudd** | `python3 smuggler.py -u <URL> --timeout 15` |

---

## 🛡️ Defensive vurderinger og utbedring

1. **Tvinge HTTP/2 eller HTTP/3 End-to-End:** HTTP/2 definerer entydige rammelengder, og eliminerer tvetydighet i overskriften.
2. **Normaliser HTTP-hoder:** Konfigurer omvendte proxyer for å avvise forespørsler som inneholder både `Content-Length` og `Transfer-Encoding`.
3. **Slipp tvetydige mutasjoner:** Slipp forespørsler med obfuskerte `Transfer-Encoding`-overskrifter (f.eks. mellomrom før kolon, flere verdier).

---

## ⚠️ Juridisk ansvarsfraskrivelse

> [!FORSIKTIG]
> HTTP-forespørselssmugling kan forstyrre live backend-køer og forgifte delte caching-noder. smugler må utelukkende brukes på eksplisitt autoriserte testmål med streng tillatelse fra eiendeler.