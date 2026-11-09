<!-- language-home-navigation -->
[🏠 Deutsche Startseite](../../README.de.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🔑 Gitleaks – Vollständiger Cheatsheet

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
  <img src="https://img.shields.io/badge/Category-Source_Code_&_Secrets-green?style=for-the-badge" alt="Secrets">
  <img src="https://img.shields.io/badge/Language-Go-cyan?style=for-the-badge" alt="Go">
</p>

<p align="center">
  <b>🔑 SAST-Tool zum Suchen und Erkennen hartcodierter Geheimnisse wie Passwörter, API-Schlüssel und Token in Git-Repositorys und Commit-Verläufen</b>
</p>

---

## 📋 Inhaltsverzeichnis

- [Übersicht & Erkennungs-Engine](#-overview--detection-engine)
- [Installation](#-Installation)
- [Basic Secret Scanning](#-basic-secret-scanning)
  - [Scannen eines Git-Repositorys](#scanning-a-git-repository)
  - [Nicht übertragene Änderungen scannen](#scanning-uncommitted-changes)
  - [Einfache Dateien und Ordner scannen](#scanning-plain-files--folders)
- [Targeting-Commit-Bereiche und Verlauf](#-targeting-commit-ranges--history)
- [CI/CD & Pre-Commit-Integration](#-cicd--pre-commit-integration)
- [Benutzerdefinierte Erkennungsregeln und Zulassungslisten](#-custom-detection-rules--allowlisting)
- [Ausgabeformate und Berichterstattung](#-output-formats--reporting)
- [Kurzreferenztabelle](#-quick-reference-table)
- [Verteidigungsüberlegungen und Abhilfemaßnahmen](#-defensive-überlegungen--remediation)
- [Rechtlicher Haftungsausschluss](#-legal-disclaimer)

---

## 🎯 Übersicht und Erkennungs-Engine

Hartcodierte Geheimnisse (AWS-Zugriffsschlüssel, private SSH-Schlüssel, GitHub-Tokens, Slack-Webhooks, Datenbankanmeldeinformationen), die versehentlich in Git-Repositorys übertragen werden, sind einer der häufigsten Vektoren für eine anfängliche Cloud-Kompromittierung.

**Gitleaks** wurde von **Zachary Rice (@zricethezav)** erstellt und bietet ultraschnelle Regex-basierte und Shannon-Entropieanalyse für:
- **Vollständiger Git-Verlauf:** Überprüft jeden Zweig, jedes Tag und jeden historischen Commit.
- **Pre-Commit-Hooks:** Verhindert, dass Ingenieure versehentlich Anmeldeinformationen festschreiben, bevor der Code Remote-Server erreicht.
- **CI/CD-Pipelines:** Pull-Requests mit Anmeldeinformationslecks schlagen automatisch fehl.

---

## 📥 Installation
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

## 🚀 Grundlegendes geheimes Scannen

### Scannen eines Git-Repositorys
Prüft den Commit-Verlauf des aktuellen Repositorys:
```bash
# Run detection in current working repo
gitleaks detect -v

# Audit an external local repo path
gitleaks detect --source /path/to/repo -v
```
### Scannen nicht festgeschriebener Änderungen
Überprüft bereitgestellte und nicht bereitgestellte Codeänderungen vor dem Festschreiben:
```bash
# Inspect unstaged working directory changes
gitleaks protect -v

# Scan staged changes only
gitleaks protect --staged -v
```
### Einfache Dateien und Ordner scannen
Nicht-Git-Verzeichnisse scannen (z. B. extrahierte Firmware, dekompilierte mobile Apps, Konfigurationsordner):
```bash
gitleaks dir /path/to/directory -v
```
---

## ⚡ Targeting-Commit-Bereiche und -Verlauf
```bash
# Scan commits made in the last 7 days
gitleaks detect --log-opts="--since='7 days ago'" -v

# Scan specific branch or tag comparison
gitleaks detect --log-opts="origin/main..feature-branch" -v

# Target the latest 5 commits
gitleaks detect --log-opts="-n 5" -v
```
---

## ⚙️ CI/CD- und Pre-Commit-Integration

### Pre-Commit-Framework-Konfiguration (`.pre-commit-config.yaml`)
```yaml
repos:
  - repo: https://github.com/gitleaks/gitleaks
    rev: v8.18.2
    hooks:
      - id: gitleaks
```
### GitHub-Aktionsworkflow
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

## 🧩 Benutzerdefinierte Erkennungsregeln und Zulassungslisten

Konfigurieren Sie benutzerdefinierte Regex-Muster oder ignorieren Sie Fehlalarme mit `.gitleaks.toml`:
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
Mit benutzerdefinierter Konfiguration ausführen:
```bash
gitleaks detect -c .gitleaks.toml -v
```
---

## 📊 Ausgabeformate und Berichte
```bash
# Export results to clean JSON format
gitleaks detect --report-path gitleaks-report.json --report-format json

# Export to SARIF (for GitHub Security tab integration)
gitleaks detect --report-path report.sarif --report-format sarif

# Export to CSV
gitleaks detect --report-path report.csv --report-format csv
```
---

## ⚡ Kurzreferenztabelle

| Ziel | Befehl |
|-----------|---------|
| **Gesamten Git-Verlauf scannen** | `gitleaks detect -v` |
| **Nicht festgeschriebene Änderungen scannen** | `gitleaks protect -v` |
| **Nur bereitgestellte Änderungen scannen** | `gitleaks protect --staged -v` |
| **Einfaches Verzeichnis scannen (kein Git)**| `gitleaks dir /path/to/folder -v` |
| **Nach JSON exportieren** | `gitleaks detect -r report.json -f json` |
| **Nach SARIF exportieren** | `gitleaks detect -r report.sarif -f sarif` |
| **Benutzerdefinierte Regelkonfiguration verwenden** | `gitleaks detect -c .gitleaks.toml` |
| **Bestimmten Zweig scannen** | `gitleaks detect --log-opts="main..dev"` |

---

## 🛡️ Defensive Überlegungen und Abhilfemaßnahmen

1. **Sofortiger Widerruf:** Sobald ein Geheimnis im Git-Verlauf erkannt wird, betrachten Sie es als vollständig kompromittiert und rotieren/widerrufen Sie es sofort.
2. **Git-Verlauf neu schreiben:** Das Entfernen des Commits oder die Verwendung von `git filter-repo` / BFG Repo-Cleaner ist erforderlich, um Artefakte zu bereinigen, aber ersetzen Sie niemals die Neuschreibung des Verlaufs durch die Rotation der Anmeldeinformationen.

---

## ⚠️ Rechtlicher Haftungsausschluss

> [!ACHTUNG]
> Gitleaks ist für autorisierte interne Sicherheitsüberprüfungen, kontinuierliche Integrations-Schwachstellenprüfungen und defensive Codeüberprüfungen konzipiert. Das Scannen nicht autorisierter proprietärer Repositories ohne Erlaubnis ist strengstens untersagt.