<!-- language-home-navigation -->
[🏠 Norsk Hovedside](../../README.nb.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇬🇧 English](../README.md)

---


# 🦀 Feroxbuster - Komplett jukseark

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ███████╗███████╗██████╗  ██████╗ ██╗  ██╗██████╗ ██╗   ██╗███████╗████████╗███████╗██████╗ 
  ██╔════╝██╔════╝██╔══██╗██╔═══██╗╚██╗██╔╝██╔══██╗██║   ██║██╔════╝╚══██╔══╝██╔════╝██╔══██╗
  █████╗  █████╗  ██████╔╝██║   ██║ ╚███╔╝ ██████╔╝██║   ██║███████╗   ██║   █████╗  ██████╔╝
  ██╔══╝  ██╔══╝  ██╔══██╗██║   ██║ ██╔██╗ ██╔══██╗██║   ██║╚════██║   ██║   ██╔══╝  ██╔══██╗
  ██║     ███████╗██║  ██║╚██████╔╝██╔╝ ██╗██████╔╝╚██████╔╝███████║   ██║   ███████╗██║  ██║
  ╚═╝     ╚══════╝╚═╝  ╚═╝ ╚═════╝ ╚═╝  ╚═╝╚═════╝  ╚═════╝ ╚══════╝   ╚═╝   ╚══════╝╚═╝  ╚═╝
                Fast, Simple, Recursive Content Discovery
```
<p align="center">
  <img src="https://img.shields.io/badge/Feroxbuster-Content_Discovery-orange?style=for-the-badge" alt="Feroxbuster">
  <img src="https://img.shields.io/badge/Rust-Blazingly_Fast-blue?style=for-the-badge" alt="Rust">
  <img src="https://img.shields.io/badge/Auto_Tune-Recursive-green?style=for-the-badge" alt="Recursive">
  <img src="https://img.shields.io/badge/Bug_Bounty-Web_Pentest-red?style=for-the-badge" alt="Web Pentest">
</p>

<p align="center">
  <b>⚡ Et raskt, enkelt, rekursivt innholdsoppdagingsverktøy skrevet i Rust</b>
</p>

---

## 📋 Innholdsfortegnelse

– [Introduksjon](#-introduksjon)
- [Nøkkelfunksjoner](#-nøkkelfunksjoner)
- [Verktøysammenligning](#-verktøy-sammenligning)
- [Installasjon](#-installasjon)
- [Grunnleggende syntaks og hurtigstart](#-grunnsyntaks--hurtigstart)
- [Kjernekommandoalternativer](#-kjernekommandoalternativer)
  - [Rekursjonskontroll](#rekursjonskontroll)
  - [Samtidighet og ytelse](#samtidig--ytelse)
  - [Filutvidelser](#filutvidelser)
  - [Filtrering og samsvar](#filtrering--samsvar)
  - [Smarte funksjoner (Auto-Tune, Link Extraction, Backups)](#smart-features)
  - [Headers, Cookies & Auth](#headers-cookies--auth)
  - [Proxy & Burp Suite Integration](#proxy--burp-suite-integration)
- [Multi-Target Scanning & Pipelines](#-multi-target-scanning--pipelines)
- [Real-World Workflows](#-real-world-workflows)
- [Hurtigreferansetabell](#-hurtigreferansetabell)
– [Tips og beste fremgangsmåter](#-tips--beste praksis)
– [Juridisk ansvarsfraskrivelse](#-juridisk ansvarsfraskrivelse)
- [Ressurser](#-ressurser)

---

## 🎯 Introduksjon

**Feroxbuster** er en høyytelses, flertråds, rekursiv nettinnholdsskanner skrevet i Rust. Den er designet for å være rask, enkel og intuitiv, og kombinerer hastigheten til Go/Rust-baserte fuzzere med innfødt automatisk rekursiv katalogoppdagelse.

Det skinner under penetrasjonstester og bug-bounty-engasjementer der dype nettsidetrær og skjulte kataloger må oppdages raskt uten å skape komplekse manuelle skript.

---

## 🌟 Nøkkelfunksjoner

- ⚡ **Bragende rask:** Drevet av asynkron rust (`tokio` og `reqwest`), som kan utføre tusenvis av forespørsler per sekund.
- 🔄 **Native Recursive Discovery:** Oppdager automatisk nye kataloger og skanner dem rekursivt opp til konfigurerbare dybdegrenser.
- 🧠 **Auto-Tune & Auto-Bail:** Begrenser skannehastigheten automatisk hvis målnettserveren bremser ned eller avbryter hvis overdreven tilkoblingsfeil oppstår.
- 🔗 **In-Line Link Extraction:** Parser HTML- og JavaScript-svar for å oppdage skjulte endepunkter og mater dem tilbake til skannekøen.
- 📦 **Automatisert sikkerhetskopijakt:** Legger til `.bak`, `~`, `.old` og `.orig` varianter automatisk til oppdagede filer.
- 🎯 **Rørbar inngang:** Godtar sømløst mållister fra `stdin` (fra verktøy som `subfinder`, `httpx` eller `gau`).

---

## ⚖️ Verktøysammenligning

| Funksjon | Feroxbuster | ffuf | dirsearch | Gobuster | Dirb |
|--------|-------------|------|--------|--------|------|
| **Språk** | Rust | Gå | Python | Gå | C |
| **Hastighet** | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐ |
| **Standard rekursjon** | ✅ Automatisk | ❌ Manuelt flagg | ✅ Automatisk (`-r`) | ❌ Ingen | ✅ Automatisk |
| **Linkekstraksjon** | ✅ Ja (`--extract-links`) | ❌ Nei | ❌ Nei | ❌ Nei | ❌ Nei |
| **Automatisk frekvensinnstilling** | ✅ Ja (`--auto-tune`) | ❌ Nei | ❌ Nei | ❌ Nei | ❌ Nei |
| **POST Fuzzing** | ❌ Nei | ✅ Innfødt | ✅ Innfødt (`--data`) | ❌ Nei | ❌ Nei |
| **Multi-Target Stdin** | ✅ Innfødt | ❌ Begrenset | ✅ Ja (`-l`) | ❌ Nei | ❌ Nei |
| **Sikkerhetskopieringsfilgjenkjenning**| ✅ Native (`--collect-backups`)| ❌ Nei | ✅ Innfødt (`-e *`) | ❌ Nei | ❌ Nei |

---

## 📥 Installasjon

### Kali Linux & Debian / Ubuntu
```bash
# Update repository and install
sudo apt update && sudo apt install -y feroxbuster
```
### Bruke last (Rust Package Manager)
```bash
cargo install feroxbuster
```
### macOS (hjemmebrygget)
```bash
brew install feroxbuster
```
### Docker
```bash
docker run --init -it epi052/feroxbuster -u https://target.com -w /usr/share/seclists/Discovery/Web-Content/common.txt
```
### Forhåndskompilert binær (GitHub-utgivelser)
```bash
# Download latest Linux release
curl -sL https://raw.githubusercontent.com/epi052/feroxbuster/main/install-nix.sh | bash
sudo mv feroxbuster /usr/local/bin/
```
---

## 🚀 Grunnleggende syntaks og hurtigstart
```bash
feroxbuster -u <TARGET_URL> -w <WORDLIST> [OPTIONS]
```
### Eksempler på hurtigskanning
```bash
# Scan a single target with default settings
feroxbuster -u http://10.10.10.10

# Scan with custom SecLists directory list
feroxbuster -u https://example.com -w /usr/share/seclists/Discovery/Web-Content/directory-list-2.3-medium.txt

# Scan target including specific file extensions
feroxbuster -u https://example.com -w /usr/share/wordlists/dirb/common.txt -x php,html,txt,json
```
---

## ⚙️ Kjernekommandoalternativer

### Rekursjonskontroll
```bash
# Specify maximum recursion depth (default is 4)
feroxbuster -u https://example.com -w common.txt -d 2

# Completely disable recursion (scan root directory only)
feroxbuster -u https://example.com -w common.txt -n

# Don't scan nested subdirectories automatically (interactive prompt)
feroxbuster -u https://example.com -w common.txt --dont-scan /api,/static
```
### Samtidighet og ytelse
```bash
# Set number of concurrent threads per directory (default: 50)
feroxbuster -u https://example.com -t 100

# Limit total concurrent directory scans running simultaneously (default: unlimited)
feroxbuster -u https://example.com -L 3

# Rate limit requests per second to avoid triggering WAF/firewall blocks
feroxbuster -u https://example.com --rate-limit 150

# Request timeout in seconds (default: 7)
feroxbuster -u https://example.com -T 5
```
### Filutvidelser
```bash
# Append extensions to each wordlist item
feroxbuster -u https://example.com -x php,asp,aspx,jsp,txt,bak

# Automatically collect and fuzz file extensions found during crawling
feroxbuster -u https://example.com --collect-extensions
```
### Filtrering og matching
```bash
# Match only specific status codes (e.g., 200 OK and 301 Redirects)
feroxbuster -u https://example.com -s 200 301 302

# Filter out specific status codes (e.g., exclude 404 and 403)
feroxbuster -u https://example.com -C 404 403 500

# Filter responses matching exact size in bytes
feroxbuster -u https://example.com -S 4092 1024

# Filter responses matching exact word count
feroxbuster -u https://example.com -W 42

# Filter responses matching exact line count
feroxbuster -u https://example.com -N 12

# Filter responses matching regular expressions (e.g., generic error pages)
feroxbuster -u https://example.com --filter-regex "Under Construction|Page Not Found"

# Filter responses similar to a known 404/wildcard page
feroxbuster -u https://example.com --filter-similar-to https://example.com/definitelynotfoundpage404
```
### Smarte funksjoner
```bash
# Auto-tune: Automatically adjusts request concurrency based on server response latency
feroxbuster -u https://example.com --auto-tune

# Auto-bail: Aborts scanning automatically when more than 100 consecutive network errors occur
feroxbuster -u https://example.com --auto-bail

# Extract links: Parses HTML and JavaScript to automatically uncover endpoints
feroxbuster -u https://example.com --extract-links

# Collect backups: Automatically tests for backup copies (.bak, ~, .old) of every discovered file
feroxbuster -u https://example.com --collect-backups
```
### Overskrifter, informasjonskapsler og godkjenning
```bash
# Custom HTTP Header
feroxbuster -u https://example.com -H "Authorization: Bearer <TOKEN>" -H "X-Forwarded-For: 127.0.0.1"

# Session Cookie
feroxbuster -u https://example.com -b "session=abc123xyz; role=admin"

# Custom User-Agent string
feroxbuster -u https://example.com -a "Mozilla/5.0 (Windows NT 10.0; Win64; x64)"

# Ignore invalid / self-signed TLS certificates
feroxbuster -u https://10.10.10.10 -k
```
### Proxy & Burp Suite-integrering
```bash
# Route all requests through Burp Suite proxy
feroxbuster -u https://example.com -p http://127.0.0.1:8080 -k

# Convenience shortcut flag for Burp Suite (enables -k and sets proxy to localhost:8080)
feroxbuster -u https://example.com --burp
```
---

## 🔁 Multi-Target Scanning & Pipelines

Feroxbuster leser mål-URL-er fra standardinndata (`stdin`), noe som gjør den ideell for integrasjon med eiendelsoppdagelsespipelines:
```bash
# Pipeline: Subdomain discovery -> HTTP Probing -> Feroxbuster Fuzzing
subfinder -d example.com -silent | \
  httpx -silent -mc 200,301,302 | \
  feroxbuster --stdin -w /usr/share/seclists/Discovery/Web-Content/raft-medium-directories.txt -t 30 -L 4
```
### Skanner URL-er fra en fil
```bash
cat targets.txt | feroxbuster --stdin -w common.txt -v
```
---

## 🎬 Virkelige arbeidsflyter

### Arbeidsflyt 1: Omfattende Bug Bounty Web Reconnaissance

Høy samtidighet, automatisk hastighetsinnstilling, koblingsutvinning og sikkerhetskopiering:
```bash
feroxbuster -u https://target.com \
  -w /usr/share/seclists/Discovery/Web-Content/directory-list-2.3-medium.txt \
  -x php,js,json,txt,html \
  --extract-links \
  --collect-backups \
  --auto-tune \
  -o ferox_target_scan.txt
```
### Arbeidsflyt 2: Intern Enterprise / Active Directory Portal Discovery

Skanne en intern nettapplikasjon via Burp Suite med autentisering og selvsignerte sertifikater:
```bash
feroxbuster -u https://internal-portal.corp.local \
  -w /usr/share/seclists/Discovery/Web-Content/common.txt \
  -x aspx,ashx,asmx,config,txt \
  -b "ASP.NET_SessionId=k2m9fl3p8..." \
  --burp \
  -d 3
```
### Arbeidsflyt 3: Rask ikke-rekursiv API og endepunktoppdagelse
```bash
feroxbuster -u https://api.target.com/v1 \
  -w /usr/share/seclists/Discovery/Web-Content/api/api-endpoints.txt \
  -n \
  -s 200,201,204,401,403 \
  -t 100 \
  --json \
  -o api_results.json
```
---

## 📊 Hurtigreferansetabell

| Flagg | Kort | Beskrivelse | Eksempel |
|------|-------|------------|--------|
| `--url` | `-u` | Mål-URL for å skanne | `feroxbuster -u https://target.com` |
| `--wordlist` | `-w` | Vei til ordliste | `feroxbuster -w /path/to/list.txt` |
| `--threads` | `-t` | Antall samtidige tråder per katalog | `feroxbuster -t 80` |
| `--scan-limit` | `-L` | Begrens totalt antall samtidige katalogskanninger | `feroxbuster -L 3` |
| `--depth` | `-d` | Maksimal rekursjonsdybde (standard 4) | `feroxbuster -d 2` |
| `--no-recursion` | `-n` | Deaktiver rekursiv katalogskanning | `feroxbuster -n` |
| `--extensions` | `-x` | Filutvidelser å legge til | `feroxbuster -x php,html,bak` |
| `--status-codes` | `-s` | Statuskoder som skal inkluderes | `feroxbuster -s 200 301 302` |
| `--filter-status` | `-C` | Statuskoder som skal ekskluderes | `feroxbuster -C 404 500` |
| `--filter-size` | `-S` | Svarstørrelser som skal ekskluderes | `feroxbuster -S 4096` |
| `--filter-words` | `-W` | Ord teller for å ekskludere | `feroxbuster -W 15` |
| `--filter-lines` | `-N` | Linjeteller for å ekskludere | `feroxbuster -N 8` |
| `--auto-tune` | Ingen | Juster hastigheten dynamisk basert på latens | `feroxbuster --auto-tune` |
| `--auto-bail` | Ingen | Avbryt skanning ved overdreven nettverksfeil | `feroxbuster --auto-bail` |
| `--extract-links`| Ingen | Parse HTML/JS for nye endepunkter | `feroxbuster --extract-links` |
| `--collect-backups` | Ingen | Se etter sikkerhetskopieringsvariasjoner (`.bak`, `.old`) | `feroxbuster --collect-backups` |
| `--proxy` | `-p` | Send trafikk gjennom HTTP-proxy | `feroxbuster -p http://127.0.0.1:8080` |
| `--burp` | Ingen | Snarvei for `-p http://127.0.0.1:8080 -k` | `feroxbuster --burp` |
| `--stdin` | Ingen | Les URL-er fra standardinndata | `cat urls.txt \| feroxbuster --stdin` |
| `--output` | `-o` | Utdatafilbane | `feroxbuster -o results.txt` |
| `--json` | Ingen | Formater utdata som JSON | `feroxbuster --json -o out.json` |

---

## 💡 Tips og beste fremgangsmåter

1. **Bruk `--auto-tune` for eksterne mål:**
   - I stedet for å gjette trådteller (`-t`), måler `--auto-tune` aktivt målresponstider og struper eller akselererer dynamisk, og unngår utilsiktet servernektelse.
2. **Kombiner `--extract-links` og `--collect-backups`:**
   - Denne kombinasjonen er eksepsjonelt dødelig i bug-bounty-vurderinger: Feroxbuster trekker ut skjulte JS-ruter, skanner dem og sjekker umiddelbart om utviklersikkerhetskopier (`.bak`, `.swp`, `~`) var igjen på serveren.
3. **Begrens skanninger på store ordlister med `-L`:**
   - Hvis du bruker en 200 000+ ord ordbok, kan ubegrenset rekursjon starte dusinvis av samtidige tråder. Bruk `-L 2` eller `-L 3` for å holde minne og båndbredde innenfor sikre driftsgrenser.
4. **Bruk tilstandsfiler for å gjenoppta store revisjoner:**
   - Feroxbuster lagrer periodisk tilstandsfiler (`.state`). Hvis en avbrutt skanning oppstår, gjenoppta umiddelbart med `feroxbuster --resume-from <FILE>.state`.

---

## ⚠️ Juridisk ansvarsfraskrivelse

> **VIKTIG:** Feroxbuster er bygget utelukkende for autoriserte sikkerhetsvurderinger, CTF-konkurranser og penetrasjonstester utført med skriftlig tillatelse. Skanning av uautoriserte mål kan mette nettverksinfrastruktur og er strengt forbudt ved lov.

---

## 📚 Ressurser

- [Feroxbuster GitHub Repository](https://github.com/epi052/feroxbuster)
- [Offisiell Feroxbuster-dokumentasjon](https://epi052.github.io/feroxbuster-docs/)
- [SecLists Discovery Lists](https://github.com/danielmiessler/SecLists)