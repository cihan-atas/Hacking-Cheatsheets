<!-- language-home-navigation -->
[🏠 Page d'accueil Française](../../README.fr.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🌩️ Prowler - Aide-mémoire complet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ██████╗ ██████╗  ██████╗ ██╗    ██╗██╗     ███████╗██████╗ 
  ██╔══██╗██╔══██╗██╔═══██╗██║    ██║██║     ██╔════╝██╔══██╗
  ██████╔╝██████╔╝██║   ██║██║ █╗ ██║██║     █████╗  ██████╔╝
  ██╔═══╝ ██╔══██╗██║   ██║██║███╗██║██║     ██╔══╝  ██╔══██╗
  ██║     ██║  ██║╚██████╔╝╚███╔███╔╝███████╗███████╗██║  ██║
  ╚═╝     ╚═╝  ╚═╝ ╚═════╝  ╚══╝╚══╝ ╚══════╝╚══════╝╚═╝  ╚═╝
       Multi-Cloud Security Posture Assessment & CIS Benchmark Auditor
```
<p align="center">
  <img src="https://img.shields.io/badge/Prowler-Cloud_Security-blue?style=for-the-badge" alt="Prowler">
  <img src="https://img.shields.io/badge/Author-Toni_de_la_Fuente-orange?style=for-the-badge" alt="tonablyx">
  <img src="https://img.shields.io/badge/Clouds-AWS_%7C_Azure_%7C_GCP_%7C_K8s-gold?style=for-the-badge" alt="Multi-Cloud">
  <img src="https://img.shields.io/badge/Compliance-CIS_Benchmark-green?style=for-the-badge" alt="CIS">
</p>

<p align="center">
  <b>🌩️ Outil de sécurité open source standard pour l'évaluation de la posture AWS, Azure, Google Cloud (GCP) et Kubernetes, l'audit des erreurs de configuration et la vérification de la conformité</b>
</p>

---

## 📋 Table des matières

- [Vue d'ensemble et architecture](#-overview--architecture)
- [Installation](#-installation)
- [Audit de sécurité AWS](#-aws-security-auditing)
  - [Audit de compte par défaut](#default-account-audit)
  - [Ciblage de services spécifiques (S3, IAM, EC2)](#targeting-special-services-s3-iam-ec2)
  - [Ciblage de régions AWS spécifiques](#targeting-special-aws-regions)
  - [Analyse multi-comptes avec AWS Organizations](#analyse-multi-comptes-avec-aws-organizations)
- [Audit Azure et Google Cloud (GCP)](#-azure--google-cloud-gcp-auditing)
  - [Évaluation Microsoft Azure](#microsoft-azure-assessment)
  - [Évaluation Google Cloud Platform (GCP)](#google-cloud-platform-gcp-assessment)
- [Cadres de conformité (CIS, ISO27001, PCI-DSS)](#-compliance-frameworks-cis-iso27001-pci-dss)
- [Formats de rapports et de sortie (HTML, JSON, CSV)](#-reporting--output-formats-html-json-csv)
- [Tableau de référence rapide](#-quick-reference-table)
- [Considérations défensives et mesures correctives](#-considérations-défensives--remédiation)
- [Mentions légales](#-legal-disclaimer)

---

## 🎯 Présentation et architecture

Créé à l'origine par **Toni de la Fuente (@tonablyx)**, **Prowler** est un outil essentiel d'évaluation de la sécurité multi-cloud. Il effectue des évaluations automatisées sur les services d'infrastructure en tant que service (IaaS) et de plateforme pour détecter :
- Compartiments S3 et comptes de stockage lisibles/inscriptibles publiquement.
- Comptes racine manquant d'authentification multifacteur (MFA).
- Politiques IAM surprivilégiées avec autorisations génériques `*`.
- Bases de données, disques et sauvegardes non chiffrés.
- Groupes de sécurité ouverts exposant les ports de gestion (SSH 22, RDP 3389) à `0.0.0.0/0`.
- Non-conformité aux benchmarks CIS, ISO 27001, HIPAA, PCI-DSS et GDPR.

---

## 📥Installation
```bash
# Using pip / pipx (Recommended)
pipx install prowler
# Or
pip install prowler

# Using Docker
docker pull prowlercloud/prowler:latest

# Verify installation
prowler -v
```
---

## ☁️ Audit de sécurité AWS

Assurez-vous que votre environnement dispose d'informations d'identification AWS valides configurées (`aws configure`, variables d'environnement ou profil d'instance IAM) :

### Audit de compte par défaut
Exécute toutes les vérifications intégrées sur tous les services et régions activés :
```bash
prowler aws
```
### Ciblage de services spécifiques (S3, IAM, EC2)
```bash
# Run only S3 bucket checks
prowler aws --services s3

# Run IAM and EC2 checks together
prowler aws --services iam,ec2
```
### Ciblage de régions AWS spécifiques
Évitez d'analyser les régions vides et inutilisées pour augmenter considérablement la vitesse d'analyse :
```bash
prowler aws --region us-east-1,eu-west-1
```
### Analyse multi-comptes avec AWS Organizations
Auditez tous les comptes d'une organisation AWS en assumant un rôle de gestion centralisé :
```bash
prowler aws --sp-role-arn arn:aws:iam::111122223333:role/SecurityAuditRole
```
---

## 🔷 Audit Azure et Google Cloud (GCP)

### Évaluation Microsoft Azure
Authentifiez-vous via Azure CLI (`az login`) :
```bash
# Audit entire Azure subscription
prowler azure --subscription-ids "00000000-0000-0000-0000-000000000000"

# Target specific services (Storage, Entra ID / IAM)
prowler azure --services storage,iam
```
### Évaluation de Google Cloud Platform (GCP)
Authentifiez-vous via le SDK Google Cloud (`gcloud auth application-default login`) :
```bash
# Audit specific GCP Project
prowler gcp --project-ids my-production-project

# Target Google Cloud Storage
prowler gcp --services storage
```
---

## 📜 Cadres de conformité (CIS, ISO27001, PCI-DSS)

Prowler classe les contrôles selon les normes de conformité officielles :
```bash
# Run CIS AWS Foundations Benchmark v3.0
prowler aws --compliance cis_3.0_aws

# Run PCI-DSS Compliance Audit
prowler aws --compliance pci_3.2.1_aws

# Run ISO 27001 Evaluation
prowler aws --compliance iso27001_2013_aws

# List all supported compliance frameworks
prowler aws --list-compliance
```
---

## 📊 Formats de rapport et de sortie (HTML, JSON, CSV)

Par défaut, Prowler génère des rapports dans un répertoire `output/` :
- **HTML interactif :** Tableau de bord visuel affichant les vérifications échouées, réussies et ignorées.
- **CSV :** Facile à importer dans des feuilles de calcul ou des systèmes de billetterie.
- **JSON-ASFF / JSON-OCSF :** AWS Security Hub et télémétrie compatible SIEM.
```bash
# Generate HTML and JSON reports in a custom folder
prowler aws -M html,json,csv -o /path/to/audit-reports/
```
---

## ⚡ Tableau de référence rapide

| Objectif | Commande |
|-----------|---------|
| **Audit AWS complet** | `prowler aws` |
| **Service spécifique à l'audit** | `prowler aws --services s3,iam` |
| **Audit d'une seule région** | `prowler aws --region us-east-1` |
| **Audit de référence CIS** | `prowler aws --compliance cis_3.0_aws` |
| **Audit Azure complet** | `prowler azure --subscription-ids <SUB_ID>` |
| **Audit GCP complet** | `prowler gcp --project-ids <PROJECT_ID>` |
| **Exporter HTML/CSV/JSON** | `prowler aws -M html,csv,json -o ./reports` |
| **Liste des chèques disponibles**| `prowler aws --list-checks` |

---

## 🛡️ Considérations défensives et mesures correctives

1. **Remédiation automatisée :** Consultez les recommandations de sortie de Prowler qui fournissent la commande AWS CLI ou le paramètre Terraform exact requis pour corriger chaque erreur de configuration.
2. **Intégration d'AWS Security Hub :** Introduisez les résultats Prowler JSON-ASFF directement dans AWS Security Hub pour des alertes de vulnérabilité multi-comptes centralisées.

---

## ⚠️ Mentions légales

> [!ATTENTION]
> Prowler est un outil d'audit et d'évaluation de posture. Assurez-vous que vous disposez de l'autorisation appropriée du locataire cloud et des propriétaires d'abonnements avant d'exécuter des analyses de conformité dans les environnements de production.