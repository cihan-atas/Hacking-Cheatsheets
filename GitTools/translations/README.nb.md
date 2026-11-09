<!-- language-home-navigation -->
[🏠 Norsk Hovedside](../../README.nb.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇬🇧 English](../README.md)

---


# 📂 GitTools - Komplett jukseark

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
   ██████╗ ██╗████████╗████████╗ ██████╗  ██████╗ ██╗     ███████╗
  ██╔════╝ ██║╚══██╔══╝╚══██╔══╝██╔═══██╗██╔═══██╗██║     ██╔════╝
  ██║  ███╗██║   ██║      ██║   ██║   ██║██║   ██║██║     ███████╗
  ██║   ██║██║   ██║      ██║   ██║   ██║██║   ██║██║     ╚════██║
  ╚██████╔╝██║   ██║      ██║   ╚██████╔╝╚██████╔╝███████╗███████║
   ╚═════╝ ╚═╝   ╚═╝      ╚═╝    ╚═════╝  ╚═════╝ ╚══════╝╚══════╝
      Exposed /.git/ Repository Dumping, Extraction & Secret Hunting
```
<p align="center">
  <img src="https://img.shields.io/badge/GitTools-Git_Dumping-blue?style=for-the-badge" alt="GitTools">
  <img src="https://img.shields.io/badge/Author-Internetwache-orange?style=for-the-badge" alt="internetwache">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_Secrets-green?style=for-the-badge" alt="Nettsikkerhet">
  <img src="https://img.shields.io/badge/License-MIT-purple?style=for-the-badge" alt="MIT">
</p>

<p align="center">
  <b>📂 Essensielt verktøysett for å oppdage utsatte <code>/.git/</code>-mapper på nettservere, laste ned rå Git-objektlagre og fullstendig rekonstruere original kildekode og commit-historikk</b>
</p>

---

## 📋 Innholdsfortegnelse

- [Overview & The Exposed .git Hazard](#-overview--the-exposed-git-hazard)
- [Installasjon](#-installasjon)
- [De tre GitTools-modulene](#-de-tre-gittools-modulene)
  - [1. Dumper (`gitdumper.sh`)](#1-dumper-gitdumpersh)
  - [2. Extractor (`extractor.sh`)](#2-extractor-extractorsh)
  - [3. Finder (`finder.py`)](#3-finder-finderpy)
- [Alternativ Python Dumper: `git-dumper`](#-alternative-python-dumper-git-dumper)
- [Post-Reconstruction Secret & Credential Hunting](#-post-reconstruction-secret--credential-jakt)
- [Hurtigreferansetabell](#-hurtigreferansetabell)
– [Defensive Betraktninger og Web Server Hardening](#-defensive-considerations--web-server-hardening)
– [Juridisk ansvarsfraskrivelse](#-juridisk ansvarsfraskrivelse)

---

## 🎯 Oversikt & The Exposed .git Hazard

Når utviklere distribuerer nettapplikasjoner via `git clone` eller automatiserte skript uten å begrense tilgangen til skjulte kataloger på riktig måte, blir **`/.git/`**-mappen tilgjengelig over HTTP/HTTPS:
`https://target.com/.git/`

Selv om katalogoppføring (indeksering) er deaktivert, tillater Gits deterministiske struktur en angriper å laste ned `/.git/HEAD`, `/.git/config`, `/.git/index` og krysse alle Git-løse objekter (`.git/objects/xx/xxxx...`). Dette muliggjør fullstendig rekonstruksjon av:
- Full applikasjonskildekode og interne APIer.
- Databaselegitimasjon, API-tokens og private SSH-nøkler i konfigurasjonsfiler.
- Full git commit-logger, forfattere-poster og utviklingsgrener.

---

## 📥 Installasjon
```bash
# Clone GitTools repository
git clone https://github.com/internetwache/GitTools.git
cd GitTools
chmod +x Dumper/gitdumper.sh Extractor/extractor.sh

# Install alternative standalone Python git-dumper via pip
pip install git-dumper
```
---

## 🚀 De tre GitTools-modulene

GitTools er organisert i tre spesialiserte verktøy:

### 1. Dumper (`gitdumper.sh`)
Laster ned så mye av `.git`-katalogen som mulig fra webserveren:
```bash
# Syntax: ./gitdumper.sh <URL_TO_GIT> <LOCAL_DESTINATION>
./Dumper/gitdumper.sh https://target.com/.git/ /tmp/target_repo/
```
### 2. Extraktor (`extractor.sh`)
Rekonstruerer originalfiler, commit trær og grener fra de dumpede `.git` løse objektene:
```bash
# Syntax: ./extractor.sh <DUMPED_DIR> <OUTPUT_CODE_DIR>
./Extractor/extractor.sh /tmp/target_repo/ /tmp/extracted_source/
```
Inne i `/tmp/extracted_source/` blir hver commit gjenopprettet til en isolert katalog som inneholder hele filtreet på det tidspunktet.

### 3. Finder (`finder.py`)
Skanner lister over domener eller CIDR-blokker for å oppdage eksponerte `.git`-kataloger:
```bash
python3 Finder/finder.py -i targets.txt -u "Mozilla/5.0"
```
---

## 🐍 Alternativ Python Dumper: `git-dumper`

`git-dumper` er et flertråds Python-alternativ som rekonstruerer et levende, fungerende git-depot automatisk i ett trinn:
```bash
# Dump and reconstruct working repository in one command
git-dumper https://target.com/.git/ ./recovered_repo/

# Run through HTTP/SOCKS proxy
git-dumper --proxy http://127.0.0.1:8080 https://target.com/.git/ ./recovered_repo/
```
Når du er ferdig, kan du bruke vanlige `git`-kommandoer i mappen:
```bash
cd ./recovered_repo/
git log -p              # Review all commit diffs and historical edits
git status              # Check untracked files
git branch -a           # List all remote and local branches
```
---

## 🔍 Hemmelig- og legitimasjonsjakt etter gjenoppbygging

Når depotet er gjenopprettet, søk etter legitimasjon og skjulte filer:
```bash
cd ./recovered_repo/

# Search for database strings and API keys
grep -Erio "password|secret|api_key|token|AWS" .

# Scan the reconstructed repo with Gitleaks / TruffleHog
gitleaks detect -v
trufflehog git file://$(pwd)
```
---

## ⚡ Hurtigreferansetabell

| Mål | Kommando |
|----------------|--------|
| **Dump med git-dumper (Python)**| `git-dumper https://target.com/.git/ ./repo/` |
| **Dump med GitTools Bash** | `./Dumper/gitdumper.sh https://target.com/.git/ ./dump/` |
| **Uttrekk forplikter seg til kilde** | `./Extractor/extractor.sh ./dump/ ./source/` |
| **Skann vertsliste for .git** | `python3 Finder/finder.py -i domains.txt` |
| **Se forpliktelseshistorikk** | `git log -p --color` |
| **Jakt hardkodede hemmeligheter** | `gitleaks detect -v` |

---

## 🛡️ Defensive vurderinger og herding av nettserver

1. **Nginx-herding:**
   ```nginx
   location ~ /\.git {
       deny all;
       return 404;
   }
   ```
2. **Apache-herding (`.htaccess`):**
   ```apache
   RedirectMatch 404 /\.git
   ```
3. ** Beste praksis for CI/CD:** Aldri distribuer kode ved å klone git direkte inn i webroten (`/var/www/html/`). Bruk rene artefaktbunter eller Docker-beholdere.

---

## ⚠️ Juridisk ansvarsfraskrivelse

> [!FORSIKTIG]
> GitTools og git-dumper er designet for autoriserte sårbarhetsvurderinger og defensiv infrastrukturrevisjon. Nedlasting av proprietær kildekode fra uautoriserte nettapplikasjoner uten tillatelse er ulovlig.