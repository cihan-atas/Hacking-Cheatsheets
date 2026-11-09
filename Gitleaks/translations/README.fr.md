<!-- language-home-navigation -->
[🏠 Page d'accueil Française](../../README.fr.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🔑 Gitleaks - Aide-mémoire complet

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
  <b>🔑 Outil SAST conçu pour rechercher et détecter les secrets codés en dur tels que les mots de passe, les clés API et les jetons dans les référentiels Git et les historiques de validation</b>
</p>

---

## 📋 Table des matières

- [Aperçu et moteur de détection](#-overview--detection-engine)
- [Installation](#-installation)
- [Analyse secrète de base](#-basic-secret-scanning)
  - [Analyse d'un référentiel Git](#scanning-a-git-repository)
  - [Analyse des modifications non validées](#analyse-des modifications non validées)
  - [Analyse des fichiers et dossiers bruts](#scanning-plain-files--folders)
- [Plages et historique de validation de ciblage](#-targeting-commit-ranges--history)
- [CI/CD et intégration pré-commit](#-cicd--pre-commit-integration)
-[Règles de détection personnalisées et liste autorisée](#-custom-detection-rules--liste autorisée)
- [Formats de sortie et rapports](#-output-formats--reporting)
- [Tableau de référence rapide](#-quick-reference-table)
- [Considérations défensives et mesures correctives](#-considérations-défensives--remédiation)
- [Mentions légales](#-legal-disclaimer)

---

## 🎯 Présentation et moteur de détection

Les secrets codés en dur (clés d'accès AWS, clés privées SSH, jetons GitHub, webhooks Slack, informations d'identification de base de données) commis par inadvertance dans les référentiels git sont l'un des vecteurs les plus courants de compromission initiale du cloud.

Créé par **Zachary Rice (@zricethezav)**, **Gitleaks** fournit une analyse ultra-rapide basée sur les expressions régulières et l'entropie de Shannon pour :
- **Historique Git complet :** Audite chaque branche, balise et validation historique.
- **Pre-commit Hooks :** empêche les ingénieurs de valider accidentellement les informations d'identification avant que le code n'atteigne les serveurs distants.
- **Pipelines CI/CD :** Échoue automatiquement les demandes d'extraction contenant des fuites d'informations d'identification.

---

## 📥Installation
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

## 🚀 Analyse secrète de base

### Analyser un référentiel Git
Audite l'historique des validations du référentiel actuel :
```bash
# Run detection in current working repo
gitleaks detect -v

# Audit an external local repo path
gitleaks detect --source /path/to/repo -v
```
### Analyse des modifications non validées
Audite les modifications de code planifiées et non planifiées avant de les valider :
```bash
# Inspect unstaged working directory changes
gitleaks protect -v

# Scan staged changes only
gitleaks protect --staged -v
```
### Analyse de fichiers et dossiers simples
Analysez les répertoires non-git (par exemple, micrologiciels extraits, applications mobiles décompilées, dossiers de configuration) :
```bash
gitleaks dir /path/to/directory -v
```
---

## ⚡ Ciblage des plages de validation et de l'historique
```bash
# Scan commits made in the last 7 days
gitleaks detect --log-opts="--since='7 days ago'" -v

# Scan specific branch or tag comparison
gitleaks detect --log-opts="origin/main..feature-branch" -v

# Target the latest 5 commits
gitleaks detect --log-opts="-n 5" -v
```
---

## ⚙️ Intégration CI/CD et pré-commit

### Configuration du framework de pré-validation (`.pre-commit-config.yaml`)
```yaml
repos:
  - repo: https://github.com/gitleaks/gitleaks
    rev: v8.18.2
    hooks:
      - id: gitleaks
```
### Flux de travail des actions GitHub
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

## 🧩 Règles de détection personnalisées et liste blanche

Configurez des modèles d'expression régulière personnalisés ou ignorez les faux positifs à l'aide de `.gitleaks.toml` :
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
Exécuter avec une configuration personnalisée :
```bash
gitleaks detect -c .gitleaks.toml -v
```
---

## 📊 Formats de sortie et rapports
```bash
# Export results to clean JSON format
gitleaks detect --report-path gitleaks-report.json --report-format json

# Export to SARIF (for GitHub Security tab integration)
gitleaks detect --report-path report.sarif --report-format sarif

# Export to CSV
gitleaks detect --report-path report.csv --report-format csv
```
---

## ⚡ Tableau de référence rapide

| Objectif | Commande |
|-----------|---------|
| **Analyser l'intégralité de l'historique Git** | `gitleaks detect -v` |
| **Analyser les modifications non validées** | `gitleaks protect -v` |
| **Analyser les modifications par étapes uniquement** | `gitleaks protect --staged -v` |
| **Analyser le répertoire simple (sans Git)**| `gitleaks dir /path/to/folder -v` |
| **Exporter vers JSON** | `gitleaks detect -r report.json -f json` |
| **Exporter vers SARIF** | `gitleaks detect -r report.sarif -f sarif` |
| **Utiliser la configuration des règles personnalisées** | `gitleaks detect -c .gitleaks.toml` |
| **Analyser une branche spécifique** | `gitleaks detect --log-opts="main..dev"` |

---

## 🛡️ Considérations défensives et mesures correctives

1. **Révocation immédiate :** Une fois qu'un secret est détecté dans l'historique git, considérez-le comme entièrement compromis et faites-le pivoter/révoquer immédiatement.
2. **Réécriture de l'historique Git :** La suppression de la validation ou l'utilisation de `git filter-repo` / BFG Repo-Cleaner est nécessaire pour nettoyer les artefacts, mais ne remplacez jamais la réécriture de l'historique par la rotation des informations d'identification.

---

## ⚠️ Mentions légales

> [!ATTENTION]
> Gitleaks est conçu pour les audits de sécurité internes autorisés, la vérification continue des vulnérabilités d'intégration et la révision défensive du code. L'analyse des référentiels propriétaires non autorisés sans autorisation est strictement interdite.