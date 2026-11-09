<!-- language-home-navigation -->
[🏠 Page d'accueil Française](../../README.fr.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🐽 TruffleHog - Aide-mémoire complet

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
  <img src="https://img.shields.io/badge/Category-OSINT_&_Code_Security-green?style=for-the-badge" alt="Code Sécurité">
  <img src="https://img.shields.io/badge/License-AGPLv3-red?style=for-the-badge" alt="Licence AGPLv3">
</p>

<p align="center">
  <b>🌐 Scanner secret de haute précision conçu pour traquer les clés API, les jetons et les certificats privés divulgués sur Git, S3, Docker et les systèmes de fichiers avec validation en direct</b>
</p>

---

## 📋 Table des matières

-[Présentation et superpuissance de la vérification en direct](#-overview--la-superpuissance-de-vérification-en direct)
- [Comparaison TruffleHog vs Gitleaks] (#-trufflehog-vs-gitleaks-comparison)
- [Installation](#-installation)
- [Analyse des référentiels Git](#-scanning-git-repositories)
- [Analyse des systèmes de fichiers et répertoires locaux](#-scanning-local-filesystems--directories)
- [Analyse d'organisations GitHub entières](#-scanning-entire-github-organizations)
- [Analyse des compartiments S3 et des images Docker](#-scanning-s3-buckets--docker-images)
-[Filtrage des faux positifs (`--only-verified`)](#-filtrage-faux-positifs---seulement-vérifié)
- [Sortie JSON et intégration CI/CD](#-json-output--cicd-integration)
- [Tableau de référence rapide](#-quick-reference-table)
- [Considérations défensives et OPSEC](#-defensive-considérations--opsec)
- [Mentions légales](#-legal-disclaimer)

---

## 🎯 Présentation et superpuissance de la vérification en direct

Les développeurs confient fréquemment des informations d'identification à privilèges élevés dans les référentiels Git (clés d'accès AWS, clés privées Stripe, webhooks Slack, clés privées SSH, mots de passe de base de données). Même lorsqu'ils sont supprimés lors des validations ultérieures, **ces secrets restent définitivement enfouis dans l'historique des validations du référentiel**.

Écrit en Go par **Truffle Security**, **TruffleHog** analyse les historiques de validation, les systèmes de fichiers et le stockage cloud pour découvrir les secrets divulgués.

Son super pouvoir déterminant est la **vérification en direct** :
Contrairement aux scanners d'expressions régulières traditionnels qui inondent les équipes de sécurité de milliers de faux positifs (par exemple, des exemples de clés de documentation), TruffleHog contacte le fournisseur de cloud concerné (AWS, GitHub, Slack, OpenAI) pour **vérifier si le secret extrait est activement fonctionnel**.

---

## ⚖️ Comparaison TruffleHog vs Gitleaks

| Fonctionnalité | TruffeHog | Gitleaks |
|---------|------------|----------|
| **Vérification des informations d'identification en direct** | ✅ Oui (plus de 750 détecteurs testés en direct)| ❌ Non (Regex uniquement) |
| **Sources de données cibles** | Git, GitHub, S3, Docker, système de fichiers| Git, fichiers locaux |
| **Vitesse sur les énormes dépôts** | Rapide (passez au multithread) | Extrêmement rapide (regex minimale) |
| **Taux de faux positifs** | Proche de zéro (avec `--only-verified`) | Modéré (nécessite des listes autorisées) |
| **Pré-Commit Hook Friendly** | ✅ Pris en charge | ✅ Hautement optimisé |

---

## 📥Installation
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

## 🚀 Analyse des référentiels Git
```bash
# Scan a remote Git repository (including entire commit history)
trufflehog git https://github.com/targetcorp/public-project

# Scan an internal on-premise Git repository with authentication
trufflehog git https://username:token@gitlab.corp.local/repo.git

# Scan only the most recent commit (great for pre-commit hooks)
trufflehog git file://./ --since-commit HEAD~1
```
---

## 📂 Analyse des systèmes de fichiers et des répertoires locaux

Analysez les répertoires locaux, les archives de sauvegarde extraites ou les racines Web désindexées :
```bash
# Scan current directory recursively
trufflehog filesystem .

# Scan specific sensitive configuration directory
trufflehog filesystem /var/www/html/ --exclude-paths tests/,vendor/
```
---

## 🏢 Analyse d'organisations GitHub entières

Auditer tous les référentiels publics appartenant à une organisation :
```bash
# Scan all public repos of a GitHub organization
trufflehog github --org=targetorg --token=YOUR_GITHUB_TOKEN

# Scan all private and public repositories across a personal account
trufflehog github --user=targetuser --token=YOUR_GITHUB_TOKEN
```
---

## ☁️ Analyse des compartiments S3 et des images Docker
```bash
# Scan an AWS S3 bucket for exposed credentials in stored logs and backups
trufflehog s3 --bucket=target-backup-bucket

# Scan a Docker container image layers for baked-in secrets
trufflehog docker --image=targetcorp/api-server:latest
```
---

## 🎯 Filtrage des faux positifs (`--only-verified`)

Supprimez tous les accès regex non vérifiés et affichez **uniquement les informations d'identification actives confirmées** :
```bash
# Report ONLY confirmed functional credentials verified against providers
trufflehog git https://github.com/target/repo --only-verified

# Scan filesystem for verified secrets only
trufflehog filesystem /opt/backups/ --only-verified
```
---

## 📊 Sortie JSON et intégration CI/CD
```bash
# Export findings to structured JSON format
trufflehog git https://github.com/target/repo --json > secrets_report.json

# Parse live AWS keys with jq
cat secrets_report.json | jq -r 'select(.Verified == true) | .Raw'
```
---

## ⚡ Tableau de référence rapide

| Objectif | Commande |
|-----------|---------|
| **Analyser le dépôt Git à distance** | `trufflehog git <GIT_URL>` |
| **Analyser uniquement les secrets vérifiés** | `trufflehog git <GIT_URL> --only-verified` |
| **Analyser le système de fichiers local** | `trufflehog filesystem <DIRECTORY>` |
| **Analyser l'organisation GitHub** | `trufflehog github --org=<ORG> --token=<PAT>` |
| **Analyser l'image Docker** | `trufflehog docker --image=<IMAGE>` |
| **Scanner le compartiment S3** | `trufflehog s3 --bucket=<BUCKET_NAME>` |
| **Exporter vers JSON** | `trufflehog git <GIT_URL> --json` |

---

## 🛡️ Considérations défensives et OPSEC

1. **Trafic de vérification de l'API sortant :** Lorsqu'il est exécuté sans `--no-verification`, TruffleHog se connecte à des points de terminaison tiers (Slack, AWS, Stripe) pour tester les clés. Cela déclenche les journaux d'authentification dans le locataire cloud de la victime ! Si la furtivité est primordiale, désactivez la validation en direct :
   ```bash
   trufflehog git <URL> --no-verification
   ```
2. **Révocation de clé et réponse aux incidents :** Lorsque des secrets valides sont découverts dans des référentiels publics, alternez immédiatement les informations d'identification et vérifiez les journaux CloudTrail/d'audit pour tout accès non autorisé.

---

## ⚠️ Mentions légales

> [!ATTENTION]
> Cette aide-mémoire est destinée aux audits de sécurité autorisés, aux opérations Red Team et aux évaluations d'hygiène des informations d'identification. L'utilisation d'informations d'identification découvertes pour accéder à des comptes non autorisés ou à des environnements de cloud privé est strictement illégale.