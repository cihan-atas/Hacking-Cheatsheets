<!-- language-home-navigation -->
[🏠 Page d'accueil Française](../../README.fr.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🐶 Snaffler - Aide-mémoire complet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ███████╗███╗   ██╗ █████╗ ███████╗███████╗██╗     ███████╗██████╗ 
  ██╔════╝████╗  ██║██╔══██╗██╔════╝██╔════╝██║     ██╔════╝██╔══██╗
  ███████╗██╔██╗ ██║███████║█████╗  █████╗  ██║     █████╗  ██████╔╝
  ╚════██║██║╚██╗██║██╔══██║██╔══╝  ██╔══╝  ██║     ██╔══╝  ██╔══██╗
  ███████║██║ ╚████║██║  ██║██║     ██║     ███████╗███████╗██║  ██║
  ╚══════╝╚═╝  ╚═══╝╚═╝  ╚═╝╚═╝     ╚═╝     ╚══════╝╚══════╝╚═╝  ╚═╝
       Active Directory Domain SMB Share Credential & Secret Hunter
```
<p align="center">
  <img src="https://img.shields.io/badge/Snaffler-Secret_Hunter-blue?style=for-the-badge" alt="Snaffler">
  <img src="https://img.shields.io/badge/Language-C%23_/_NET-orange?style=for-the-badge" alt="C# / .NET">
  <img src="https://img.shields.io/badge/Category-Active_Directory_&_Network-green?style=for-the-badge" alt="Active Directory">
  <img src="https://img.shields.io/badge/License-GPLv3-red?style=for-the-badge" alt="Licence GPLv3">
</p>

<p align="center">
  <b>🌐 Outil de domaine Windows automatisé conçu pour explorer les partages SMB, localiser les fichiers de configuration juteux, les bases de données KeePass et les informations d'identification en texte brut</b>
</p>

---

## 📋 Table des matières

- [Vue d'ensemble et problème de chasse aux partages](#-overview--the-share-hunting-problem)
- [Comment fonctionne Snaffler](#-comment-fonctionne-snaffler)
- [Options d'installation et d'exécution](#-installation--exécution-options)
- [Niveaux de triage et de gravité Snaffler] (#-snaffler-triage--niveaux de gravité)
- [Flux de travail d'analyse courants](#-common-scanning-workflows)
  - [Exploration du partage à l'échelle du domaine](#domain-wide-share-crawl)
  - [Ciblage d'hôtes ou de partages spécifiques](#ciblage-d'hôtes-ou-partages spécifiques)
  - [À l'exclusion des ordinateurs et des racines DFS](#excluant les ordinateurs--racines-dfs)
- [Cibles juteuses et règles de correspondance de fichiers] (#-juicy-targets--file-match-rules)
- [Journalisation et analyse des sorties](#-output-logging--analysis)
- [Tableau de référence rapide](#-quick-reference-table)
- [Considérations défensives et OPSEC](#-defensive-considérations--opsec)
- [Mentions légales](#-legal-disclaimer)

---

## 🎯 Présentation et problème de chasse aux partages

Dans les environnements Active Directory de taille moyenne à grande, les serveurs de fichiers et les postes de travail d'entreprise hébergent des milliers de partages réseau SMB ouverts. Les administrateurs système, les développeurs et les employés laissent souvent derrière eux :

- 🔑 **Bases de données KeepPass (`.kdbx`) :** Contenant souvent des mots de passe principaux ou des clés principales facilement déchiffrables.
- ⚙️ **Fichiers de configuration (`web.config`, `appsettings.json`) :** Contenant des chaînes de connexion SQL codées en dur, des mots de passe de compte de service et des clés API.
- 📜 **Scripts de déploiement (`unattend.xml`, `.ps1`, `.bat`) :** Identifiants administratifs codés en dur.
- 🔏 **Certificats privés (`.pfx`, `.p12`, `.pem`, `.ppk`) :** Utilisables pour une élévation de privilèges ou un mouvement latéral.

Parcourir manuellement des milliers de partages est impossible. **Snaffler**, développé par **l33tb455**, automatise l'ensemble de ce processus : il interroge Active Directory pour les objets informatiques, énumère les partages SMB lisibles, parcourt l'arborescence des répertoires et évalue les fichiers par rapport aux ensembles de règles regex organisées.

---

## ⚙️ Comment fonctionne Snaffler
```
[ Compromised Domain Machine ]
         │
         ├── 1. Queries Domain Controller via LDAP for Computer Objects
         ├── 2. Connects to port 445 (SMB) across all discovered hosts
         ├── 3. Enumerates Read/Write permissions on C$, SYSVOL, NETLOGON, Custom Shares
         └── 4. Evaluates file extensions, file names, and file contents against rules
```
---

## 📥 Options d'installation et d'exécution

Snaffler est compilé en tant que binaire .NET autonome :
```bash
# Clone and build using Visual Studio or dotnet CLI on Windows
git clone https://github.com/SnaffCon/Snaffler.git
cd Snaffler
dotnet build -c Release
```
### Exécuté sous Windows :
```cmd
# Standard execution outputting to screen and log file
Snaffler.exe -s -o snaffler_log.txt
```
### Exécution via Cobalt Strike / C2 (Execute-Assembly) :
```text
beacon> execute-assembly C:\Tools\Snaffler.exe -s -o C:\Temp\loot.txt
```
---

## 🚦 Niveaux de triage et de gravité Snaffler

Snaffler classe les résultats dans des catégories d'urgence codées par couleur :

- 🔴 **`{Red}` / Noir :** Une découverte de grande valeur ! Mots de passe en texte brut, clés privées (`.pfx`, `.id_rsa`), jetons AWS, fichiers d'installation sans assistance.
- 🟡 **`{Yellow}` :** Constatation probable. Fichiers de configuration avec chaînes de mot de passe, chaînes de connexion à la base de données, clichés instantanés.
- 🟢 **`{Green}` :** Informatif / Intérêt. Noms de fichiers correspondant aux modèles administratifs (`passwords.xlsx`, `vpn.ovpn`).

---

## 🚀 Flux de travail d'analyse courants

### Exploration du partage à l'échelle du domaine

Analysez tous les partages accessibles sur l'ensemble du domaine Active Directory :
```cmd
# Run with live console streaming (-s) and save to disk
Snaffler.exe -s -o C:\loot\snaffler_domain.txt

# Run with multi-threading tuning (Default: 60 threads)
Snaffler.exe -s -t 100 -o C:\loot\snaffler_fast.txt
```
### Ciblage d'hôtes ou de partages spécifiques

Lorsque vous souhaitez éviter d'analyser l'intégralité du domaine et vous concentrer sur un serveur de fichiers ou un contrôleur de domaine :
```cmd
# Scan a single targeted file server
Snaffler.exe -c fs01.corp.local -s

# Scan a comma-separated list of machines
Snaffler.exe -c dc01.corp.local,fs02.corp.local,sql01.corp.local -s

# Scan a specific shared folder path directly
Snaffler.exe -i \\fs01.corp.local\Departments\IT\ -s
```
### À l'exclusion des ordinateurs et des racines DFS

Évitez d'analyser les ordinateurs lents ou sensibles :
```cmd
# Exclude specific backup or surveillance servers
Snaffler.exe -s -c- backup01.corp.local,cctv01.corp.local
```
---

## 🎯 Cibles juteuses et règles de correspondance de fichiers

Par défaut, Snaffler recherche les extensions critiques suivantes :

| Extension/Fichier | Contenu de grande valeur |
|------------------|--------------------|
| `.kdbx` / `.kdb` | Coffres-forts du gestionnaire de mots de passe KeePass |
| `web.config` / `app.config` | Mots de passe de base de données en texte clair |
| `unattend.xml` / `sysprep.inf` | Mots de passe administrateur Windows |
| `.ppk` / `.pem` / `id_rsa` | Clés privées SSH |
| `.pfx` / `.p12` | Certificats Active Directory |
| `.rdp` / `.vnc` | Profils de connexion au bureau à distance |
| `passwords.xlsx` / `creds.txt` | Listes de mots de passe des employés |

---

## 📊 Journalisation et analyse des sorties
```bash
# Grep for immediate RED credentials from a Linux terminal
grep -i "{Red}" snaffler_domain.txt

# Find all discovered KeePass safes
grep -i "\.kdbx" snaffler_domain.txt

# Filter out database connection strings
grep -i "password=" snaffler_domain.txt
```
---

## ⚡ Tableau de référence rapide

| Objectif | Commande |
|-----------|---------|
| **Analyse à l'échelle du domaine** | `Snaffler.exe -s -o out.log` |
| **Ordinateur unique cible** | `Snaffler.exe -c <HOSTNAME> -s` |
| **Partage spécifique cible** | `Snaffler.exe -i \\<HOST>\<SHARE> -s` |
| **Régler les threads de concurrence**| `Snaffler.exe -s -t 120 -o out.log` |
| **Exclure l'hôte de l'analyse** | `Snaffler.exe -s -c- <EXCLUDE_HOST>` |
| **Limite maximale de taille de fichier**| `Snaffler.exe -s -z 5000000` *(Limite à 5 Mo de fichiers)* |
| **Exécuter via l'assemblage C2**| `execute-assembly Snaffler.exe -s` |

---

## 🛡️ Considérations défensives et OPSEC

1. **Pics de connexion SMB massifs :** Snaffler se connecte au port 445 sur chaque machine du domaine en quelques minutes. Cela génère des milliers d’ID d’événement 4624 (connexion réussie de type 3 – Réseau) et d’ID d’événement 5140 (un objet de partage réseau a été accédé).
2. **Honeyfiles & Decoys :** Les défenseurs modernes d'Active Directory déploient des partages Canary et des fichiers leurres `.kdbx` qui déclenchent des alertes SOC instantanées lorsqu'ils sont ouverts par des outils comme Snaffler.
3. **Limites d'exécution :** Définissez `-z` (taille maximale en octets du fichier) pour éviter de planter les partages réseau en lisant des fichiers de sauvegarde ISO ou VHD de plusieurs gigaoctets.

---

## ⚠️ Mentions légales

> [!ATTENTION]
> Snaffler est conçu exclusivement pour les tests d'intrusion autorisés, les missions des équipes rouges et les audits de sécurité internes. L'analyse des partages de domaine sans autorisation constitue une intrusion illégale sur le réseau.