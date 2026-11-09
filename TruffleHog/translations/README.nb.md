<!-- language-home-navigation -->
[🏠 Norsk Hovedside](../../README.nb.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇬🇧 English](../README.md)

---


# 🐽 TruffleHog - Komplett jukseark

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ████████╗██████╗ ██╗   ██╗███████╗███████╗██╗     ███████╗██╗  ██╗ ██████╗  ██████╗ 
  ╚══██╔══╝██╔══██╗██║   ██║██╔════╝██╔════╝██║     ██╔════╝██║  ██║██╔═══██╗██╔════╝ 
     ██║   ██████╔╝██║   ██║█████╗  █████╗  ██║     █████╗  ███████║██║   ██║██║  ███╗
     ██║   ██╔══██╗██║   ██║██╔══╝  ██╔══╝  ██║     ██╔══╝  ██╔══██║██║   ██║██║   ██║
     ██║   ██║  ██║╚██████╔╝██║     ██║     ███████╗███████╗██║  ██║╚██████╔╝╚██████╔╝
     ╚═╝   ╚═╝  ╚═╝ ╚═════╝ ╚═╝     ╚═╝     ╚══════╝╚══════╝╚═╝  ╚═╝ ╚═════╝  ╚═════╝ 
       Find Exposed Credentials & Secrets with Real-Time Active Verification
```
<p align="center">
  <img src="https://img.shields.io/badge/TruffleHog-Secret_Scanner-blue?style=for-the-badge" alt="TruffleHog">
  <img src="https://img.shields.io/badge/Developer-Truffle_Security-orange?style=for-the-badge" alt="Truffle Security">
  <img src="https://img.shields.io/badge/Category-OSINT_&_Code_Security-green?style=for-the-badge" alt="Kodesikkerhet">
  <img src="https://img.shields.io/badge/License-AGPLv3-red?style=for-the-badge" alt="License AGPLv3">
</p>

<p align="center">
  <b>🌐 Hemmelig skanner med høy nøyaktighet designet for å lete etter lekke API-nøkler, tokens og private sertifikater på tvers av Git, S3, Docker og filsystemer med live validering</b>
</p>

---

## 📋 Innholdsfortegnelse

- [Oversikt og The Live Verification Superpower](#-overview--the-live-verification-superpower)
- [TruffleHog vs Gitleaks-sammenligning](#-trufflehog-vs-gitleaks-comparison)
- [Installasjon](#-installasjon)
- [Scanning Git Repositories](#-scanning-git-repositories)
- [Skanning av lokale filsystemer og kataloger](#-skanning-lokale-filsystemer--kataloger)
- [Skanning av hele GitHub-organisasjoner](#-skanning-hele-github-organisasjoner)
- [Skanning av S3 Buckets & Docker Images](#-scanning-s3-buckets--docker-images)
- [Filtrering av falske positiver (`--only-verified`)](#-filtering-false-positives---only-verified)
- [JSON Output & CI/CD Integration](#-json-output--cid-integration)
- [Hurtigreferansetabell](#-hurtigreferansetabell)
– [Defensive Considerations & OPSEC](#-defensive-considerations--opsec)
– [Juridisk ansvarsfraskrivelse](#-juridisk ansvarsfraskrivelse)

---

## 🎯 Oversikt & The Live Verification Superpower

Utviklere bruker ofte legitimasjon med høye privilegier i Git-repositorier (AWS-tilgangsnøkler, Stripe private keys, Slack webhooks, SSH private keys, databasepassord). Selv når de slettes i påfølgende forpliktelser, forblir **disse hemmelighetene permanent begravet i depotets innføringshistorikk**.

Skrevet i Go av **Truffle Security**, **TruffleHog** skanner commit historier, filsystemer og skylagring for å avdekke lekkede hemmeligheter.

Dens definerende superkraft er **Live Verification**:
I motsetning til tradisjonelle regex-skannere som oversvømmer sikkerhetsteam med tusenvis av falske positiver (f.eks. eksempeldokumentasjonsnøkler), kontakter TruffleHog den respektive skyleverandøren (AWS, GitHub, Slack, OpenAI) for å **verifisere om den utpakkede hemmeligheten er aktivt funksjonell**.

---

## ⚖️ TruffleHog vs Gitleaks sammenligning

| Funksjon | TruffleHog | Gitleaks |
|--------|------------|--------|
| **Live-legitimasjonsbekreftelse** | ✅ Ja (750+ detektorer testet live)| ❌ Nei (kun regex) |
| **Måldatakilder** | Git, GitHub, S3, Docker, Filsystem| Git, lokale filer |
| **Fart på enorme repos** | Rask (Gå flertråds) | Ekstremt rask (Minimal regex) |
| **Falsk positiv rate** | Nær null (med `--only-verified`) | Moderat (krever godkjenningslister)|
| **Pre-Commit Hook Friendly** | ✅ Støttet | ✅ Svært optimalisert |

---

## 📥 Installasjon
```bash
# Debian / Ubuntu / Kali Linux (via curl installer)
curl -sSfL https://raw.githubusercontent.com/trufflesecurity/trufflehog/main/scripts/install.sh | sudo sh -s -- -b /usr/local/bin

# Arch Linux (AUR)
yay -S trufflehog-bin

# Homebrew (macOS / Linux)
brew install trufflehog

# Docker Container
docker run -it --rm trufflesecurity/trufflehog:latest git https://github.com/target/repo
```
---

## 🚀 Skanner Git-lagre
```bash
# Scan a remote Git repository (including entire commit history)
trufflehog git https://github.com/targetcorp/public-project

# Scan an internal on-premise Git repository with authentication
trufflehog git https://username:token@gitlab.corp.local/repo.git

# Scan only the most recent commit (great for pre-commit hooks)
trufflehog git file://./ --since-commit HEAD~1
```
---

## 📂 Skanning av lokale filsystemer og kataloger

Skann lokale kataloger, utpakkede sikkerhetskopieringsarkiver eller deindekserte webrøtter:
```bash
# Scan current directory recursively
trufflehog filesystem .

# Scan specific sensitive configuration directory
trufflehog filesystem /var/www/html/ --exclude-paths tests/,vendor/
```
---

## 🏢 Skanner hele GitHub-organisasjoner

Revidere alle offentlige depoter som tilhører en organisasjon:
```bash
# Scan all public repos of a GitHub organization
trufflehog github --org=targetorg --token=YOUR_GITHUB_TOKEN

# Scan all private and public repositories across a personal account
trufflehog github --user=targetuser --token=YOUR_GITHUB_TOKEN
```
---

## ☁️ Skanner S3-bøtter og Docker-bilder
```bash
# Scan an AWS S3 bucket for exposed credentials in stored logs and backups
trufflehog s3 --bucket=target-backup-bucket

# Scan a Docker container image layers for baked-in secrets
trufflehog docker --image=targetcorp/api-server:latest
```
---

## 🎯 Filtrering av falske positiver (`--only-verified`)

Undertrykk alle ubekreftede regex-treff og vis **bare bekreftet aktiv legitimasjon**:
```bash
# Report ONLY confirmed functional credentials verified against providers
trufflehog git https://github.com/target/repo --only-verified

# Scan filesystem for verified secrets only
trufflehog filesystem /opt/backups/ --only-verified
```
---

## 📊 JSON-utgang og CI/CD-integrasjon
```bash
# Export findings to structured JSON format
trufflehog git https://github.com/target/repo --json > secrets_report.json

# Parse live AWS keys with jq
cat secrets_report.json | jq -r 'select(.Verified == true) | .Raw'
```
---

## ⚡ Hurtigreferansetabell

| Mål | Kommando |
|----------------|--------|
| **Scan Remote Git Repo** | `trufflehog git <GIT_URL>` |
| **Skann kun bekreftede hemmeligheter** | `trufflehog git <GIT_URL> --only-verified` |
| **Skann lokalt filsystem** | `trufflehog filesystem <DIRECTORY>` |
| **Skann GitHub-organisasjon** | `trufflehog github --org=<ORG> --token=<PAT>` |
| **Skann Docker-bilde** | `trufflehog docker --image=<IMAGE>` |
| **Skann S3 bøtte** | `trufflehog s3 --bucket=<BUCKET_NAME>` |
| **Eksporter til JSON** | `trufflehog git <GIT_URL> --json` |

---

## 🛡️ Defensive betraktninger og OPSEC

1. **Utgående API-verifiseringstrafikk:** Når du kjører uten `--no-verification`, kobler TruffleHog seg til tredjeparts endepunkter (Slack, AWS, Stripe) for å teste nøkler. Dette utløser autentiseringslogger i offerets skyleietaker! Hvis stealth er viktigst, deaktiver live-validering:
   ```bash
   trufflehog git <URL> --no-verification
   ```
2. **Nøkkeltilbakekalling og hendelsesrespons:** Når gyldige hemmeligheter oppdages i offentlige depoter, roter legitimasjonen umiddelbart og kontroller CloudTrail / revisjonslogger for uautorisert tilgang.

---

## ⚠️ Juridisk ansvarsfraskrivelse

> [!FORSIKTIG]
> Dette juksearket er ment for autoriserte sikkerhetsrevisjoner, Red Team-operasjoner og legitimasjonshygienevurderinger. Å bruke oppdaget legitimasjon for å få tilgang til uautoriserte kontoer eller private skymiljøer er strengt ulovlig.