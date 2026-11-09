<!-- language-home-navigation -->
[🏠 Home Page Italiana](../../README.it.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🔑 Gitleaks - Cheatsheet completo

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
<palign="centro">
  <img src="https://img.shields.io/badge/Gitleaks-Secret_Scanner-blue?style=for-the-badge" alt="Gitleaks">
  <img src="https://img.shields.io/badge/Author-Zachary_Rice-orange?style=for-the-badge" alt="zricethezav">
  <img src="https://img.shields.io/badge/Category-Source_Code_&_Secrets-green?style=for-the-badge" alt="Secrets">
  <img src="https://img.shields.io/badge/Language-Go-cyan?style=for-the-badge" alt="Vai">
</p>

<palign="centro">
  <b>🔑 Strumento SAST progettato per trovare e rilevare segreti codificati come password, chiavi API e token nei repository Git e nelle cronologie dei commit</b>
</p>

---

## 📋 Sommario

- [Panoramica e motore di rilevamento](#-overview--detection-engine)
- [Installazione](#-installazione)
- [Scansione segreta di base](#-basic-secret-scanning)
  - [Scansione di un repository Git](#scanning-a-git-repository)
  - [Scansione modifiche non confermate](#scansione-modifiche-non confermate)
  - [Scansione di file e cartelle semplici](#scanning-plain-files--folders)
- [Intervalli di impegno di targeting e cronologia](#-intervalli di impegno di targeting--cronologia)
- [Integrazione CI/CD e pre-commit](#-cicd--integrazione-pre-commit)
- [Regole di rilevamento personalizzate e lista consentita](#-custom-detection-rules--allowlist)
- [Formati di output e reportistica](#-formati-output--reporting)
- [Tabella di riferimento rapido](#-tabella-di-riferimento-rapido)
- [Considerazioni difensive e riparazione](#-considerazioni-difensive--riparazione)
- [Disclaimer legale](#-legal-disclaimer)

---

## 🎯 Panoramica e motore di rilevamento

I segreti hardcoded (chiavi di accesso AWS, chiavi private SSH, token GitHub, webhook Slack, credenziali del database) inavvertitamente impegnati nei repository git sono uno dei vettori più comuni per la compromissione iniziale del cloud.

Creato da **Zachary Rice (@zricethezav)**, **Gitleaks** fornisce analisi ultraveloci basate su regex ed entropia di Shannon su:
- **Cronologia Git completa:** controlla ogni ramo, tag e commit storico.
- **Hook pre-commit:** impedisce agli ingegneri di impegnare accidentalmente le credenziali prima che il codice raggiunga i server remoti.
- **Pipeline CI/CD:** fallisce automaticamente le richieste pull contenenti fughe di credenziali.

---

## 📥Installazione
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

## 🚀 Scansione segreta di base

### Scansione di un repository Git
Controlla la cronologia dei commit del repository corrente:
```bash
# Run detection in current working repo
gitleaks detect -v

# Audit an external local repo path
gitleaks detect --source /path/to/repo -v
```
### Scansione delle modifiche non confermate
Controlla le modifiche al codice in fasi e non prima del commit:
```bash
# Inspect unstaged working directory changes
gitleaks protect -v

# Scan staged changes only
gitleaks protect --staged -v
```
### Scansione di file e cartelle semplici
Scansione di directory non git (ad esempio firmware estratto, app mobili decompilate, cartelle di configurazione):
```bash
gitleaks dir /path/to/directory -v
```
---

## ⚡ Intervalli di commit e cronologia del targeting
```bash
# Scan commits made in the last 7 days
gitleaks detect --log-opts="--since='7 days ago'" -v

# Scan specific branch or tag comparison
gitleaks detect --log-opts="origin/main..feature-branch" -v

# Target the latest 5 commits
gitleaks detect --log-opts="-n 5" -v
```
---

## ⚙️ Integrazione CI/CD e pre-commit

### Configurazione del framework pre-commit (`.pre-commit-config.yaml`)
```yaml
repos:
  - repo: https://github.com/gitleaks/gitleaks
    rev: v8.18.2
    hooks:
      - id: gitleaks
```
### Flusso di lavoro delle azioni GitHub
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

## 🧩 Regole di rilevamento personalizzate e inserimento nella lista consentita

Configura modelli regex personalizzati o ignora i falsi positivi utilizzando `.gitleaks.toml`:
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
Esegui con configurazione personalizzata:
```bash
gitleaks detect -c .gitleaks.toml -v
```
---

## 📊 Formati di output e reportistica
```bash
# Export results to clean JSON format
gitleaks detect --report-path gitleaks-report.json --report-format json

# Export to SARIF (for GitHub Security tab integration)
gitleaks detect --report-path report.sarif --report-format sarif

# Export to CSV
gitleaks detect --report-path report.csv --report-format csv
```
---

## ⚡ Tabella di consultazione rapida

| Obiettivo | Comando |
|-----------|---------|
| **Scansiona l'intera cronologia Git** | `gitleaks detect -v` |
| **Scansione modifiche non confermate** | `gitleaks protect -v` |
| **Esegui la scansione solo delle modifiche graduali** | `gitleaks protect --staged -v` |
| **Scansiona directory semplice (senza Git)**| `gitleaks dir /path/to/folder -v` |
| **Esporta in JSON** | `gitleaks detect -r report.json -f json` |
| **Esporta in SARIF** | `gitleaks detect -r report.sarif -f sarif` |
| **Utilizza configurazione regole personalizzate** | `gitleaks detect -c .gitleaks.toml` |
| **Scansione ramo specifico** | `gitleaks detect --log-opts="main..dev"` |

---

## 🛡️ Considerazioni difensive e rimedi

1. **Revoca immediata:** una volta rilevato un segreto nella cronologia git, consideralo completamente compromesso e ruotalo/revocalo immediatamente.
2. **Riscrittura della cronologia Git:** è necessario rimuovere il commit o utilizzare `git filter-repo`/BFG Repo-Cleaner per pulire gli artefatti, ma non sostituire mai le riscritture della cronologia con la rotazione delle credenziali.

---

## ⚠️ Disclaimer legale

> [!ATTENZIONE]
> Gitleaks è progettato per audit di sicurezza interni autorizzati, controllo continuo delle vulnerabilità dell'integrazione e revisione difensiva del codice. È severamente vietato eseguire la scansione di repository proprietari non autorizzati senza autorizzazione.