<!-- language-home-navigation -->
[🏠 Norsk Hovedside](../../README.nb.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇬🇧 English](../README.md)

---


# 🔑 Gitleaks - Komplett jukseark

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
   ██████╗ ██╗████████╗██╗     ███████╗ █████╗ ██╗  ██╗███████╗
  ██╔════╝ ██║╚══██╔══╝██║     ██╔════╝██╔══██╗██║ ██╔╝██╔════╝
  ██║  ███╗██║   ██║   ██║     █████╗  ███████║█████╔╝ ███████╗
  ██║   ██║██║   ██║   ██║     ██╔══╝  ██╔══██║██╔═██╗ ╚════██║
  ╚██████╔╝██║   ██║   ███████╗███████╗██║  ██║██║  ██╗███████║
   ╚═════╝ ╚═╝   ╚═╝   ╚══════╝╚══════╝╚═╝  ╚═╝╚═╝  ╚═╝╚══════╝
           Fast & Automated Secret Detection for Git Repositories
```
<p align="center">
  <img src="https://img.shields.io/badge/Gitleaks-Secret_Scanner-blue?style=for-the-badge" alt="Gitleaks">
  <img src="https://img.shields.io/badge/Author-Zachary_Rice-orange?style=for-the-badge" alt="zricethezav">
  <img src="https://img.shields.io/badge/Category-Source_Code_&_Secrets-green?style=for-the-badge" alt="Hemmeligheter">
  <img src="https://img.shields.io/badge/Language-Go-cyan?style=for-the-badge" alt="Go">
</p>

<p align="center">
  <b>🔑 SAST-verktøy designet for å finne og oppdage hardkodede hemmeligheter som passord, API-nøkler og tokens i Git-repositorier og commit-historier</b>
</p>

---

## 📋 Innholdsfortegnelse

- [Oversikts- og deteksjonsmotor](#-oversikt--deteksjonsmotor)
- [Installasjon](#-installasjon)
- [Grunnleggende hemmelig skanning](#-grunnleggende-hemmelig skanning)
  - [Skanning av et Git-lager](#scanning-a-git-repository)
  - [Scanning Uncommitted Changes](#scanning-uncommitted-changes)
  - [Skanne vanlige filer og mapper](#skanning-vanlige-filer--mapper)
– [Targeting Commit Ranges & History](#-targeting-commit-ranges--history)
- [CI/CD & Pre-commit-integrasjon](#-cicd--pre-commit-integration)
– [Egendefinerte deteksjonsregler og tillatelseslisting](#-tilpasset-deteksjon-regler--tillatelseslisting)
- [Utdataformater og rapportering](#-utdataformater--rapportering)
- [Hurtigreferansetabell](#-hurtigreferansetabell)
– [Defensive Considerations & Remediation](#-defensive-considerations--remediation)
– [Juridisk ansvarsfraskrivelse](#-juridisk ansvarsfraskrivelse)

---

## 🎯 Oversikt og deteksjonsmotor

Hardkodede hemmeligheter (AWS-tilgangsnøkler, SSH private nøkler, GitHub-tokens, Slack webhooks, databaselegitimasjon) utilsiktet forpliktet til git-repositorier er en av de vanligste vektorene for innledende skykompromiss.

Laget av **Zachary Rice (@zricethezav)**, **Gitleaks** gir ultrarask regex-basert og Shannon-entropianalyse på tvers av:
- **Full Git History:** Reviderer hver gren, tag og historisk forpliktelse.
- **Pre-commit Hooks:** Blokkerer ingeniører fra ved et uhell å begå legitimasjon før koden når eksterne servere.
- **CI/CD-rørledninger:** Mislykkes automatisk pull-forespørsler som inneholder legitimasjonslekkasjer.

---

## 📥 Installasjon
```bash
# macOS (Homebrew)
brew install gitleaks

# Go install
go install github.com/zricethezav/gitleaks/v8@latest

# Docker
docker pull zricethezav/gitleaks:latest

# Linux binary download
curl -sSL https://github.com/zricethezav/gitleaks/releases/latest/download/gitleaks_linux_x64.tar.gz | tar -xz
sudo mv gitleaks /usr/local/bin/
```
---

## 🚀 Grunnleggende hemmelig skanning

### Skanner et Git-lager
Reviderer gjeldende depots forpliktelseshistorikk:
```bash
# Run detection in current working repo
gitleaks detect -v

# Audit an external local repo path
gitleaks detect --source /path/to/repo -v
```
### Skanner uforpliktede endringer
Reviderer trinnvise og uiscenesatte kodeendringer før forpliktelse:
```bash
# Inspect unstaged working directory changes
gitleaks protect -v

# Scan staged changes only
gitleaks protect --staged -v
```
### Skanner vanlige filer og mapper
Skann ikke-git-kataloger (f.eks. utpakket fastvare, dekompilerte mobilapper, konfigurasjonsmapper):
```bash
gitleaks dir /path/to/directory -v
```
---

## ⚡ Targeting Commit Ranges & History
```bash
# Scan commits made in the last 7 days
gitleaks detect --log-opts="--since='7 days ago'" -v

# Scan specific branch or tag comparison
gitleaks detect --log-opts="origin/main..feature-branch" -v

# Target the latest 5 commits
gitleaks detect --log-opts="-n 5" -v
```
---

## ⚙️ CI/CD & Pre-commit Integration

### Pre-commit Framework Configuration (`.pre-commit-config.yaml`)
```yaml
repos:
  - repo: https://github.com/gitleaks/gitleaks
    rev: v8.18.2
    hooks:
      - id: gitleaks
```
### GitHub Actions arbeidsflyt
```yaml
name: Gitleaks Security Scan
on: [push, pull_request]
jobs:
  gitleaks:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
        with:
          fetch-depth: 0
      - uses: gitleaks/gitleaks-action@v2
        env:
          GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}
```
---

## 🧩 Egendefinerte gjenkjenningsregler og tillatelsesliste

Konfigurer egendefinerte regex-mønstre eller ignorer falske positiver ved å bruke `.gitleaks.toml`:
```toml
[extend]
useDefault = true

[allowlist]
description = "Ignore test fixtures and demo tokens"
paths = [
  '''tests/.*''',
  '''fixtures/.*'''
]
regexes = [
  '''TEST_API_KEY_.*'''
]

[[rules]]
id = "custom-internal-token"
description = "Internal Company JWT Secret"
regex = '''(?i)(corp_secret_[a-z0-9]{32})'''
secretGroup = 1
entropy = 3.5
keywords = ["corp_secret_"]
```
Kjør med egendefinert konfigurasjon:
```bash
gitleaks detect -c .gitleaks.toml -v
```
---

## 📊 Utdataformater og rapportering
```bash
# Export results to clean JSON format
gitleaks detect --report-path gitleaks-report.json --report-format json

# Export to SARIF (for GitHub Security tab integration)
gitleaks detect --report-path report.sarif --report-format sarif

# Export to CSV
gitleaks detect --report-path report.csv --report-format csv
```
---

## ⚡ Hurtigreferansetabell

| Mål | Kommando |
|----------------|--------|
| **Skann hele Git-historien** | `gitleaks detect -v` |
| **Skann ikke-forpliktede endringer** | `gitleaks protect -v` |
| **Skann bare trinnvise endringer** | `gitleaks protect --staged -v` |
| **Scan Plain Directory (No Git)**| `gitleaks dir /path/to/folder -v` |
| **Eksporter til JSON** | `gitleaks detect -r report.json -f json` |
| **Eksport til SARIF** | `gitleaks detect -r report.sarif -f sarif` |
| **Bruk tilpassede reglerkonfigurasjon** | `gitleaks detect -c .gitleaks.toml` |
| **Skannspesifikk gren** | `gitleaks detect --log-opts="main..dev"` |

---

## 🛡️ Defensive vurderinger og utbedring

1. **Øyeblikkelig tilbakekalling:** Når en hemmelighet er oppdaget i git-historien, anser den som fullstendig kompromittert og roter/opphev den umiddelbart.
2. **Rewriting Git History:** Fjerning av commit eller bruk av `git filter-repo` / BFG Repo-Cleaner er nødvendig for å skrubbe artefakter, men aldri erstatte historikk omskrivninger for legitimasjonsrotasjon.

---

## ⚠️ Juridisk ansvarsfraskrivelse

> [!FORSIKTIG]
> Gitleaks er designet for autoriserte interne sikkerhetsrevisjoner, kontinuerlig integrasjonssårbarhetskontroll og defensiv kodegjennomgang. Skanning av uautoriserte proprietære arkiver uten tillatelse er strengt forbudt.