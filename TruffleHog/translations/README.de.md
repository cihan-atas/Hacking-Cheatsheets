<!-- language-home-navigation -->
[🏠 Deutsche Startseite](../../README.de.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🐽 TruffleHog – Vollständiger Cheatsheet

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
  <img src="https://img.shields.io/badge/Category-OSINT_&_Code_Security-green?style=for-the-badge" alt="Code-Sicherheit">
  <img src="https://img.shields.io/badge/License-AGPLv3-red?style=for-the-badge" alt="License AGPLv3">
</p>

<p align="center">
  <b>🌐 Hochpräziser Geheimscanner zur Suche nach durchgesickerten API-Schlüsseln, Token und privaten Zertifikaten in Git, S3, Docker und Dateisystemen mit Live-Validierung</b>
</p>

---

## 📋 Inhaltsverzeichnis

- [Übersicht und die Supermacht der Live-Verifizierung](#-overview--the-live-verification-superpower)
- [TruffleHog vs. Gitleaks-Vergleich](#-trufflehog-vs-gitleaks-comparison)
- [Installation](#-Installation)
- [Git-Repositories scannen](#-scanning-git-repositories)
- [Lokale Dateisysteme und Verzeichnisse scannen](#-scanning-local-filesystems--directories)
- [Gesamte GitHub-Organisationen scannen](#-scanning-entire-github-organizations)
- [Scannen von S3-Buckets und Docker-Images](#-scanning-s3-buckets--docker-images)
- [False Positives filtern (`--only-verified`)](#-filtering-false-positives---only-verified)
- [JSON-Ausgabe und CI/CD-Integration](#-json-output--cicd-integration)
- [Kurzreferenztabelle](#-quick-reference-table)
- [Verteidigungsüberlegungen und OPSEC](#-defensive-considerations--opsec)
- [Rechtlicher Haftungsausschluss](#-legal-disclaimer)

---

## 🎯 Übersicht und die Supermacht der Live-Verifizierung

Entwickler übermitteln häufig hochprivilegierte Anmeldeinformationen in Git-Repositorys (AWS-Zugriffsschlüssel, private Stripe-Schlüssel, Slack-Webhooks, private SSH-Schlüssel, Datenbankkennwörter). Selbst wenn sie bei nachfolgenden Commits gelöscht werden, bleiben **diese Geheimnisse dauerhaft im Commit-Verlauf des Repositorys verborgen**.

**TruffleHog** wurde von **Truffle Security** in Go geschrieben und scannt Commit-Verläufe, Dateisysteme und Cloud-Speicher, um durchgesickerte Geheimnisse aufzudecken.

Seine entscheidende Superkraft ist **Live-Verifizierung**:
Im Gegensatz zu herkömmlichen Regex-Scannern, die Sicherheitsteams mit Tausenden von Fehlalarmen (z. B. Beispieldokumentationsschlüsseln) überschwemmen, kontaktiert TruffleHog den jeweiligen Cloud-Anbieter (AWS, GitHub, Slack, OpenAI), um **zu überprüfen, ob das extrahierte Geheimnis aktiv funktionsfähig ist**.

---

## ⚖️ Vergleich zwischen TruffleHog und Gitleaks

| Funktion | Trüffelschwein | Gitleaks |
|---------|------------|----------|
| **Live-Anmeldeinformationsüberprüfung** | ✅ Ja (750+ Detektoren live getestet)| ❌ Nein (nur Regex) |
| **Zieldatenquellen** | Git, GitHub, S3, Docker, Dateisystem| Git, Lokale Dateien |
| **Geschwindigkeit bei riesigen Repos** | Schnell (Multithreading) | Extrem schnell (Minimaler regulärer Ausdruck) |
| **Falsch-Positiv-Rate** | Nahe Null (mit `--only-verified`) | Moderat (Zulassungslisten erforderlich)|
| **Pre-Commit-Hook-freundlich** | ✅ Unterstützt | ✅ Hochoptimiert |

---

## 📥 Installation
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

## 🚀 Scannen von Git-Repositorys
```bash
# Scan a remote Git repository (including entire commit history)
trufflehog git https://github.com/targetcorp/public-project

# Scan an internal on-premise Git repository with authentication
trufflehog git https://username:token@gitlab.corp.local/repo.git

# Scan only the most recent commit (great for pre-commit hooks)
trufflehog git file://./ --since-commit HEAD~1
```
---

## 📂 Scannen lokaler Dateisysteme und Verzeichnisse

Scannen Sie lokale Verzeichnisse, extrahierte Backup-Archive oder deindizierte Web-Roots:
```bash
# Scan current directory recursively
trufflehog filesystem .

# Scan specific sensitive configuration directory
trufflehog filesystem /var/www/html/ --exclude-paths tests/,vendor/
```
---

## 🏢 Scannen ganzer GitHub-Organisationen

Überprüfen Sie alle öffentlichen Repositorys einer Organisation:
```bash
# Scan all public repos of a GitHub organization
trufflehog github --org=targetorg --token=YOUR_GITHUB_TOKEN

# Scan all private and public repositories across a personal account
trufflehog github --user=targetuser --token=YOUR_GITHUB_TOKEN
```
---

## ☁️ Scannen von S3-Buckets und Docker-Images
```bash
# Scan an AWS S3 bucket for exposed credentials in stored logs and backups
trufflehog s3 --bucket=target-backup-bucket

# Scan a Docker container image layers for baked-in secrets
trufflehog docker --image=targetcorp/api-server:latest
```
---

## 🎯 Falsch positive Ergebnisse filtern (`--only-verified`)

Unterdrücken Sie alle nicht bestätigten Regex-Treffer und zeigen Sie **nur bestätigte aktive Anmeldeinformationen** an:
```bash
# Report ONLY confirmed functional credentials verified against providers
trufflehog git https://github.com/target/repo --only-verified

# Scan filesystem for verified secrets only
trufflehog filesystem /opt/backups/ --only-verified
```
---

## 📊 JSON-Ausgabe und CI/CD-Integration
```bash
# Export findings to structured JSON format
trufflehog git https://github.com/target/repo --json > secrets_report.json

# Parse live AWS keys with jq
cat secrets_report.json | jq -r 'select(.Verified == true) | .Raw'
```
---

## ⚡ Kurzreferenztabelle

| Ziel | Befehl |
|-----------|---------|
| **Remote-Git-Repo scannen** | `trufflehog git <GIT_URL>` |
| **Nur verifizierte Geheimnisse scannen** | `trufflehog git <GIT_URL> --only-verified` |
| **Lokales Dateisystem scannen** | `trufflehog filesystem <DIRECTORY>` |
| **GitHub-Organisation scannen** | `trufflehog github --org=<ORG> --token=<PAT>` |
| **Docker-Image scannen** | `trufflehog docker --image=<IMAGE>` |
| **S3-Bucket scannen** | `trufflehog s3 --bucket=<BUCKET_NAME>` |
| **Nach JSON exportieren** | `trufflehog git <GIT_URL> --json` |

---

## 🛡️ Defensive Überlegungen und OPSEC

1. **Ausgehender API-Verifizierungsverkehr:** Wenn TruffleHog ohne `--no-verification` ausgeführt wird, stellt es eine Verbindung zu Endpunkten von Drittanbietern (Slack, AWS, Stripe) her, um Schlüssel zu testen. Dies löst Authentifizierungsprotokolle im Cloud-Mandanten des Opfers aus! Wenn Heimlichkeit im Vordergrund steht, deaktivieren Sie die Live-Validierung:
   ```bash
   trufflehog git <URL> --no-verification
   ```
2. **Schlüsselwiderruf und Reaktion auf Vorfälle:** Wenn gültige Geheimnisse in öffentlichen Repositorys entdeckt werden, rotieren Sie sofort die Anmeldeinformationen und überprüfen Sie CloudTrail/Audit-Protokolle auf unbefugten Zugriff.

---

## ⚠️ Rechtlicher Haftungsausschluss

> [!ACHTUNG]
> Dieses Spickzettel ist für autorisierte Sicherheitsüberprüfungen, Red-Team-Operationen und Bewertungen der Berechtigungshygiene gedacht. Die Verwendung entdeckter Anmeldeinformationen für den Zugriff auf nicht autorisierte Konten oder private Cloud-Umgebungen ist streng illegal.