<!-- language-home-navigation -->
[🏠 Norsk Hovedside](../../README.nb.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇬🇧 English](../README.md)

---


# 📁 DIRB - Komplett jukseark

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ██████╗ ██╗██████╗ ██████╗ 
  ██╔══██╗██║██╔══██╗██╔══██╗
  ██║  ██║██║██████╔╝██████╔╝
  ██║  ██║██║██╔══██╗██╔══██╗
  ██████╔╝██║██║  ██║██████╔╝
  ╚═════╝ ╚═╝╚═╝  ╚═╝╚═════╝ 
        Web Content Scanner
```
<p align="center">
  <img src="https://img.shields.io/badge/DIRB-Content_Scanner-orange?style=for-the-badge" alt="DIRB">
  <img src="https://img.shields.io/badge/Wordlists-Bruteforce-blue?style=for-the-badge" alt="Ordlister">
  <img src="https://img.shields.io/badge/Kali_Linux-Preinstalled-green?style=for-the-badge" alt="Kali Linux">
  <img src="https://img.shields.io/badge/Web-Pentesting-red?style=for-the-badge" alt="Web Pentesting">
</p>

<p align="center">
  <b>> Klassisk nettinnholdsskanner som starter ordbokbaserte angrep mot webservere</b>
</p>

---

## 📋 Innholdsfortegnelse

– [Introduksjon](#-introduksjon)
- [DIRB vs moderne alternativer](#-dirb-vs-moderne-alternativer)
- [Installasjon](#-installasjon)
- [Standardordlister](#-standardordlister)
- [Grunnleggende syntaks](#-grunnsyntaks)
- [Vanlige skanningsscenarioer](#-vanlige skanningsscenarioer)
- [Avanserte alternativer og justering](#-avanserte-alternativer--innstilling)
- [Sårbarhetsjakt med innebygde lister](#-sårbarhetsjakt-med-innebygde lister)
- [Real-World Workflows](#-real-world-workflows)
- [Hurtigreferansetabell](#-hurtigreferansetabell)
– [Tips og beste fremgangsmåter](#-tips--beste praksis)
– [Juridisk ansvarsfraskrivelse](#-juridisk ansvarsfraskrivelse)
- [Ressurser](#-ressurser)

---

## 🎯 Introduksjon

**DIRB** (Directory Buster) er en klassisk, lett, URL-basert webinnholdsskanner skrevet i C. Den lanserer ordbokbaserte angrep mot webservere, på jakt etter eksisterende og skjulte webobjekter (filer, kataloger, skript og endepunkter).

DIRB fungerer ved å generere HTTP-forespørsler for hvert ord i en ordliste, analysere serverens svarkoder (200, 301, 302, 403, etc.), og bestemme om det finnes en ressurs.

### Nøkkelegenskaper

- ✅ **Automatisk rekursiv skanning:** Skanner oppdagede underkataloger automatisk som standard.
- ✅ **Innebygde spesialiserte ordlister:** Sendes med målrettede ordlister for vanlige webservere, CMS-er og sårbarheter.
- ✅ **Low Resource Footprint:** Lett binær som kjører jevnt selv på lavspesifisert maskinvare eller eldre systemer.
- ✅ ** Custom Header & Proxy Support:** Enkel integrasjon med Burp Suite og oppstrøms HTTP-proxyer.

---

## ⚖️ DIRB vs moderne alternativer

| Funksjon | DIRB | Gobuster | ffuf | Feroxbuster | dirsearch |
|--------|------|--------|------|--------|--------|
| **Språk** | C | Gå | Gå | Rust | Python |
| **Hastighet** | ⭐⭐ (Lav tråd) | ⭐⭐⭐⭐⭐ (Rask) | ⭐⭐⭐⭐⭐ (Ekstremt rask) | ⭐⭐⭐⭐⭐ (Async Rust) | ⭐⭐⭐⭐ (Rask) |
| **Standard rekursjon** | ✅ Ja (automatisk) | ❌ Nei | ❌ Nei (valgfritt `-recursion`) | ✅ Ja (automatisk) | ✅ Ja (`-r`) |
| **Egendefinerte ordlister inkludert** | ✅ Ja (`/usr/share/dirb/`) | ❌ Nei | ❌ Nei | ❌ Nei | ✅ Ja (`db/dicc.txt`) |
| **VHost Fuzzing** | ❌ Nei | ✅ Ja | ✅ Ja | ❌ Nei | ✅ Ja (`--subdirs`) |
| **POST / Data fuzzing** | ❌ Nei | ❌ Nei | ✅ Ja | ❌ Nei | ✅ Ja (`-d`) |
| **Minne / CPU-bruk** | Veldig lav | Lav | Lav | Veldig lav | Moderat |

---

## 📥 Installasjon

### Kali Linux & Parrot OS

DIRB kommer forhåndsinstallert i Kali Linux og Parrot OS:
```bash
# Verify installation
dirb

# Update to latest package
sudo apt update && sudo apt install -y dirb
```
### Debian / Ubuntu
```bash
sudo apt update
sudo apt install -y dirb
```
### Kompilering fra kilde
```bash
# Install build dependencies
sudo apt install -y build-essential libcurl4-openssl-dev

# Download and extract source
wget https://sourceforge.net/projects/dirb/files/dirb/2.22/dirb222.tar.gz
tar -xzvf dirb222.tar.gz
cd dirb222

# Configure permissions and compile
chmod -R 755 conf
chmod 755 configure
./configure
make
sudo make install
```
---

## 📚 Standard ordlister

Når den er installert på Kali Linux, inkluderer DIRB dedikerte ordlister på `/usr/share/dirb/wordlists/`:

| Ordlistesti | Oppføringer | Formål |
|--------------|--------|--------|
| `/usr/share/dirb/wordlists/common.txt` | ~4,614 | Standardliste brukt når ingen ordliste er spesifisert |
| `/usr/share/dirb/wordlists/small.txt` | ~959 | Rask oppdagelse av høysannsynlige baner |
| `/usr/share/dirb/wordlists/big.txt` | ~20.469 | Uttømmende katalog og filsøk |
| `/usr/share/dirb/wordlists/catala.txt` | ~6.846 | Katalansk ordliste |
| `/usr/share/dirb/wordlists/spanish.txt` | ~11.720 | Spansk ordliste |
| `/usr/share/dirb/wordlists/mutations_common.txt` | ~1000 | Vanlige prefiks-/suffiksvariasjoner |
| `/usr/share/dirb/wordlists/extensions_common.txt` | ~40 | Standard nettutvidelser (`.php`, `.asp`, `.html`, etc.) |

### Innebygde sårbarhetsordlister (`/usr/share/dirb/wordlists/vulns/`)

- `apache.txt` - Apache-spesifikke filer, statussider og kjente kataloger
- `iis.txt` - Microsoft IIS webserverfiler (`web.config`, `aspnet_client`, etc.)
- `tomcat.txt` - Apache Tomcat administrasjon og lederbaner
- `cgis.txt` - Common Gateway Interface (CGI) skript og endepunkter
- `fatwire.txt`, `hpsmh.txt`, `hyperion.txt`, `iplanet.txt`, `netware.txt`, `oracle.txt`

---

## ⌨️ Grunnleggende syntaks
```bash
dirb <url_base> [<wordlist_file(s)>] [options]
```
### Viktige eksempler
```bash
# Basic scan using default common.txt wordlist
dirb http://10.10.10.10

# Scan using a specific custom wordlist
dirb https://example.com /usr/share/wordlists/dirb/big.txt

# Scan with custom SecLists directory list
dirb http://target.local /usr/share/seclists/Discovery/Web-Content/directory-list-2.3-medium.txt
```
---

## 🔍 Vanlige skannescenarier

### 1. Skanning med filutvidelser (`-X` og `-x`)

Søk etter spesifikke filtyper knyttet til hvert ord i ordboken:
```bash
# Test specific extensions on every wordlist entry
dirb http://target.local -X .php,.html,.txt,.bak

# Use an extensions list file
dirb http://target.local -x /usr/share/dirb/wordlists/extensions_common.txt

# Combine directory brute-force with extension brute-force
dirb http://target.local /usr/share/dirb/wordlists/common.txt -X .php,.json,.zip
```
### 2. Kontrollere rekursjon (`-r` og `-R`)

Som standard gjennomsøker DIRB rekursivt hver oppdaget katalog. Du kan kontrollere denne oppførselen:
```bash
# Disable recursion completely (scan root level only)
dirb http://target.local -r

# Interactive recursion (DIRB asks before descending into each directory)
dirb http://target.local -R
```
### 3. Skanning uten store og små bokstaver (`-i`)

Nyttig når du målretter mot Windows / IIS-servere der filbaner ikke skiller mellom store og små bokstaver:
```bash
dirb http://target.local -i
```
### 4. Ignorerer spesifikke HTTP-statuskoder (`-N`)

Filtrer ut uønskede svarkoder (f.eks. omdirigeringssløyfer eller forbudte baner):
```bash
# Ignore 302 redirects and 403 forbidden responses
dirb http://target.local -N 302,403
```
---

## ⚙️ Avanserte alternativer og innstilling

### Autentisering (`-u`)

DIRB støtter HTTP Basic og Digest-autentisering:
```bash
# Supply HTTP authentication credentials
dirb http://target.local -u "admin:SecretP@ssw0rd!"

# Combine auth with custom wordlist
dirb http://target.local /usr/share/dirb/wordlists/big.txt -u "pentester:Summer2026"
```
### Egendefinerte informasjonskapsler og økttokens (`-c`)

Test autentiserte nettapplikasjonsruter ved å gi øktinformasjonskapsler:
```bash
# Pass session cookie
dirb http://target.local -c "PHPSESSID=d92j4fn82mdf82ncb184; logged_in=true"
```
### Egendefinerte brukeragent- og HTTP-hoder (`-a` og `-H`)

Omgå grunnleggende WAF-regler eller simuler legitime nettlesere:
```bash
# Set custom User-Agent
dirb http://target.local -a "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36"

# Add custom HTTP Header
dirb http://target.local -H "Authorization: Bearer eyJhbGciOi..."
dirb http://target.local -H "X-Forwarded-For: 127.0.0.1"
```
### Proxy-ruting og Burp Suite-integrering (`-p` og `-P`)

Send DIRB-trafikk gjennom en oppstrøms HTTP-proxy som Burp Suite eller OWASP ZAP:
```bash
# Route through local Burp Suite proxy
dirb http://target.local -p 127.0.0.1:8080

# Authenticated proxy
dirb http://target.local -p 10.10.10.1:3128 -P "proxyuser:proxypass"
```
### Prisbegrensning og unndragelse (`-z`)

Unngå å utløse IDS/IPS-varsler eller DoSing skjøre nettjenester ved å legge til en forsinkelse mellom forespørsler (i millisekunder):
```bash
# Wait 250 milliseconds between each request
dirb http://target.local -z 250

# Slow, stealthy scan (1 second delay)
dirb http://target.local -z 1000
```
### Utdatalogging (`-o` og `-w`)
```bash
# Save results to a file
dirb http://target.local -o /home/zeus/scans/dirb_target.txt

# Do not halt on WARNING messages (useful for uninterrupted scans)
dirb http://target.local -w -o dirb_output.txt
```
---

## 🎯 Sårbarhetsjakt med innebygde lister

DIRB inkluderer målrettede lister i `/usr/share/dirb/wordlists/vulns/` for å oppdage uopprettede servere og farlige endepunkter:
```bash
# Target Apache server vulnerabilities and status pages
dirb http://target.local /usr/share/dirb/wordlists/vulns/apache.txt

# Target IIS endpoints and misconfigurations
dirb http://target.local /usr/share/dirb/wordlists/vulns/iis.txt

# Target Tomcat management consoles
dirb http://target.local /usr/share/dirb/wordlists/vulns/tomcat.txt

# Search for old CGI scripts and shellshock entrypoints
dirb http://target.local /usr/share/dirb/wordlists/vulns/cgis.txt
```
---

## 🎬 Virkelige arbeidsflyter

### Arbeidsflyt 1: Rask innledende rekognosering

Når du først møter en webserver på port 80/443:
```bash
# Rapid scan without recursion using small wordlist
dirb http://10.10.10.50 /usr/share/dirb/wordlists/small.txt -r
```
### Arbeidsflyt 2: Uttømmende webapplikasjonsvurdering

Utfør en dyp skanning på jakt etter konfigurasjonssikkerhetskopier, kildekode og API-endepunkter:
```bash
dirb https://app.target.local /usr/share/dirb/wordlists/common.txt \
  -X .php,.bak,.sql,.config,.txt \
  -a "Mozilla/5.0" \
  -w \
  -o dirb_app_scan.txt
```
### Arbeidsflyt 3: Autentisert portalgjennomgang via Burp Suite
```bash
dirb https://internal.target.local/portal/ /usr/share/dirb/wordlists/big.txt \
  -c "session=s%3A_kJw81..." \
  -p 127.0.0.1:8080 \
  -X .jsp,.do,.action \
  -z 100
```
---

## 📊 Hurtigreferansetabell

| Flagg | Argument | Beskrivelse | Eksempel |
|------|--------|-------------|--------|
| `-a` | `<user_agent>` | Angi egendefinert User-Agent-streng | `dirb http://target -a "Mozilla/5.0"` |
| `-c` | `<cookie>` | Angi HTTP Cookie header | `dirb http://target -c "id=123"` |
| `-H` | `<header>` | Legg til egendefinert HTTP-header | `dirb http://target -H "X-API-Key: secret"` |
| `-i` | Ingen | Uavhengig av store og små bokstaver | `dirb http://target -i` |
| `-N` | `<codes>` | Ignorer svar som samsvarer med statuskode | `dirb http://target -N 302,403` |
| `-o` | `<output_file>` | Lagre skanneresultater til fil | `dirb http://target -o results.txt` |
| `-p` | `<proxy:port>` | Bruk HTTP-proxy | `dirb http://target -p 127.0.0.1:8080` |
| `-P` | `<user:pass>` | Påloggingsinformasjon for proxy-autentisering | `dirb http://target -p proxy:8080 -P u:p` |
| `-r` | Ingen | Ikke søk rekursivt | `dirb http://target -r` |
| `-R` | Ingen | Interaktivt rekursivt søk | `dirb http://target -R` |
| `-u` | `<user:pass>` | HTTP Basic/Digest-autentisering | `dirb http://target -u "admin:1234"` |
| `-w` | Ingen | Ikke stopp skanning på ADVARSEL-meldinger | `dirb http://target -w` |
| `-X` | `<ext_list>` | Legg til kommadelte utvidelser | `dirb http://target -X .php,.html` |
| `-x` | `<ext_file>` | Legg til utvidelser fra fil | `dirb http://target -x ext.txt` |
| `-z` | `<millisecs>` | Legg til forsinkelse mellom forespørsler | `dirb http://target -z 250` |

---

## 💡 Tips og beste fremgangsmåter

1. **Se opp for Wildcard 200/302-svar:**
   - Noen nettapplikasjoner returnerer HTTP 200 eller 302 for hver forespørsel (tilpasset 404-behandler). DIRB vil rapportere hver enkelt oppføring som funnet. Bruk `-N 302` eller bytt til `ffuf` / `feroxbuster` for avansert størrelse/ordfiltrering.
2. **Kombiner med `-r` for store ordbøker:**
   - Hvis du bruker `directory-list-2.3-medium.txt` (220 000 ord), deaktiver alltid rekursjon med `-r`. Ellers kan det ta dager før DIRB gjennomsøker dypt nestede kataloger.
3. **Sjekk alltid utvidelser:**
   - Moderne backends avslører ofte sensitive filer som `.env`, `.git`, `.bak`, `.conf` eller `.sql`. Test alltid vanlige utvidelser med `-X`.
4. **Bruk `-w` for uovervåket skanning:**
   - Som standard kan DIRB pause hvis den støter på SSL eller nettverksvarsler. Å bestå `-w` sikrer at testene fullføres uten manuell interaksjon.

---

## ⚠️ Juridisk ansvarsfraskrivelse

> **VIKTIG:** DIRB er utviklet for sikkerhetsrevisjon, penetrasjonstesting og etisk forskning på systemer du eier eller har eksplisitt, dokumentert tillatelse til å teste. Uautoriserte ordbokangrep mot webservere kan forårsake tjenesteforstyrrelser og er ulovlige i henhold til datakriminalitetslovgivning over hele verden.

---

## 📚 Ressurser

- [DIRB SourceForge Project](https://sourceforge.net/projects/dirb/)
- [Kali Linux DIRB-pakkedokumentasjon](https://www.kali.org/tools/dirb/)
- [SecLists - Web Content Discovery Wordlists](https://github.com/danielmiessler/SecLists)