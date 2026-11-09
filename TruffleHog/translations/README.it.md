<!-- language-home-navigation -->
[🏠 Home Page Italiana](../../README.it.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🐽 TruffleHog - Scheda completa

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
<palign="centro">
  <img src="https://img.shields.io/badge/TruffleHog-Secret_Scanner-blue?style=for-the-badge" alt="TruffleHog">
  <img src="https://img.shields.io/badge/Developer-Truffle_Security-orange?style=for-the-badge" alt="Truffle Security">
  <img src="https://img.shields.io/badge/Category-OSINT_&_Code_Security-green?style=for-the-badge" alt="Codice di sicurezza">
  <img src="https://img.shields.io/badge/License-AGPLv3-red?style=for-the-badge" alt="Licenza AGPLv3">
</p>

<palign="centro">
  <b>🌐 Scanner segreto ad alta precisione progettato per scovare chiavi API, token e certificati privati trapelati su Git, S3, Docker e file system con convalida in tempo reale</b>
</p>

---

## 📋 Sommario

- [Panoramica e superpotere della verifica dal vivo](#-panoramica--il-superpotere-della-verifica-dal-vivo)
- [Confronto TruffleHog vs Gitleaks](#-trufflehog-vs-gitleaks-confronto)
- [Installazione](#-installazione)
- [Scansione dei repository Git](#-scanning-git-repositories)
- [Scansione di file system e directory locali](#-scanning-local-filesystem--directories)
- [Scansione di intere organizzazioni GitHub](#-scanning-entire-github-organizations)
- [Scansione di bucket S3 e immagini Docker](#-scanning-s3-buckets--docker-images)
- [Filtro dei falsi positivi (`--only-verified`)](#-filtro-falsi-positivi---solo-verificato)
- [Uscita JSON e integrazione CI/CD](#-json-output--cicd-integration)
- [Tabella di riferimento rapido](#-tabella-di-riferimento-rapido)
- [Considerazioni difensive e OPSEC](#-considerazioni-difensive--opsec)
- [Disclaimer legale](#-legal-disclaimer)

---

## 🎯 Panoramica e il superpotere della verifica dal vivo

Gli sviluppatori spesso impegnano credenziali con privilegi elevati nei repository Git (chiavi di accesso AWS, chiavi private Stripe, webhook Slack, chiavi private SSH, password di database). Anche se eliminati nei commit successivi, **questi segreti rimangono permanentemente sepolti nella cronologia dei commit del repository**.

Scritto in Go da **Truffle Security**, **TruffleHog** analizza la cronologia dei commit, i file system e l'archivio cloud per scoprire i segreti trapelati.

Il suo superpotere che lo definisce è la **Verifica dal vivo**:
A differenza dei tradizionali scanner regex che inondano i team di sicurezza con migliaia di falsi positivi (ad esempio, chiavi di documentazione di esempio), TruffleHog contatta il rispettivo fornitore di servizi cloud (AWS, GitHub, Slack, OpenAI) per **verificare se il segreto estratto è attivamente funzionante**.

---

## ⚖️ Confronto tra TruffleHog e Gitleaks

| Caratteristica | TruffleHog | Gitleaks |
|---------|------------|----------|
| **Verifica delle credenziali in tempo reale** | ✅ Sì (oltre 750 rilevatori testati dal vivo)| ❌ No (solo Regex) |
| **Origini dati di destinazione** | Git, GitHub, S3, Docker, Filesystem| Git, file locali |
| **Velocità su enormi repository** | Veloce (vai in multi-thread) | Estremamente veloce (regex minima) |
| **Tasso di falsi positivi** | Vicino allo zero (con `--only-verified`) | Moderato (richiede liste consentite)|
| **Pre-Commit Hook Friendly** | ✅ Supportato | ✅ Altamente ottimizzato |

---

## 📥Installazione
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

## 🚀 Scansione dei repository Git
```bash
# Scan a remote Git repository (including entire commit history)
trufflehog git https://github.com/targetcorp/public-project

# Scan an internal on-premise Git repository with authentication
trufflehog git https://username:token@gitlab.corp.local/repo.git

# Scan only the most recent commit (great for pre-commit hooks)
trufflehog git file://./ --since-commit HEAD~1
```
---

## 📂 Scansione di file system e directory locali

Scansiona directory locali, archivi di backup estratti o radici web deindicizzate:
```bash
# Scan current directory recursively
trufflehog filesystem .

# Scan specific sensitive configuration directory
trufflehog filesystem /var/www/html/ --exclude-paths tests/,vendor/
```
---

## 🏢 Scansione di intere organizzazioni GitHub

Controlla tutti gli archivi pubblici appartenenti a un'organizzazione:
```bash
# Scan all public repos of a GitHub organization
trufflehog github --org=targetorg --token=YOUR_GITHUB_TOKEN

# Scan all private and public repositories across a personal account
trufflehog github --user=targetuser --token=YOUR_GITHUB_TOKEN
```
---

## ☁️ Scansione di bucket S3 e immagini Docker
```bash
# Scan an AWS S3 bucket for exposed credentials in stored logs and backups
trufflehog s3 --bucket=target-backup-bucket

# Scan a Docker container image layers for baked-in secrets
trufflehog docker --image=targetcorp/api-server:latest
```
---

## 🎯 Filtraggio dei falsi positivi (`--only-verified`)

Elimina tutti gli hit regex non verificati e visualizza **solo le credenziali attive confermate**:
```bash
# Report ONLY confirmed functional credentials verified against providers
trufflehog git https://github.com/target/repo --only-verified

# Scan filesystem for verified secrets only
trufflehog filesystem /opt/backups/ --only-verified
```
---

## 📊 Uscita JSON e integrazione CI/CD
```bash
# Export findings to structured JSON format
trufflehog git https://github.com/target/repo --json > secrets_report.json

# Parse live AWS keys with jq
cat secrets_report.json | jq -r 'select(.Verified == true) | .Raw'
```
---

## ⚡ Tabella di consultazione rapida

| Obiettivo | Comando |
|-----------|---------|
| **Scansiona repository Git remoto** | `trufflehog git <GIT_URL>` |
| **Scansiona solo i segreti verificati** | `trufflehog git <GIT_URL> --only-verified` |
| **Scansione file system locale** | `trufflehog filesystem <DIRECTORY>` |
| **Scansione organizzazione GitHub** | `trufflehog github --org=<ORG> --token=<PAT>` |
| **Scansiona immagine Docker** | `trufflehog docker --image=<IMAGE>` |
| **Scansiona contenitore S3** | `trufflehog s3 --bucket=<BUCKET_NAME>` |
| **Esporta in JSON** | `trufflehog git <GIT_URL> --json` |

---

## 🛡️ Considerazioni difensive e OPSEC

1. **Traffico di verifica API in uscita:** Quando viene eseguito senza `--no-verification`, TruffleHog si connette a endpoint di terze parti (Slack, AWS, Stripe) per testare le chiavi. Ciò attiva i log di autenticazione nel tenant cloud della vittima! Se la furtività è fondamentale, disabilita la convalida live:
   ```bash
   trufflehog git <URL> --no-verification
   ```
2. **Revoca di chiavi e risposta agli incidenti:** quando vengono scoperti segreti validi nei repository pubblici, ruota immediatamente le credenziali e controlla CloudTrail/log di controllo per accessi non autorizzati.

---

## ⚠️ Disclaimer legale

> [!ATTENZIONE]
> Questo cheatsheet è destinato ai controlli di sicurezza autorizzati, alle operazioni del Red Team e alle valutazioni di igiene delle credenziali. L'utilizzo delle credenziali scoperte per accedere ad account non autorizzati o ad ambienti cloud privati ​​è strettamente illegale.