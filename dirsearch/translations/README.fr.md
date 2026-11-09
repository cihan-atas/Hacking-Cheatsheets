<!-- language-home-navigation -->
[🏠 Page d'accueil Française](../../README.fr.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🔍 dirsearch - Aide-mémoire complet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ██████╗ ██╗██████╗ ███████╗███████╗ █████╗ ██████╗  ██████╗██╗  ██╗
  ██╔══██╗██║██╔══██╗██╔════╝██╔════╝██╔══██╗██╔══██╗██╔════╝██║  ██║
  ██║  ██║██║██████╔╝███████╗█████╗  ███████║██████╔╝██║     ███████║
  ██║  ██║██║██╔══██╗╚════██║██╔══╝  ██╔══██║██╔══██╗██║     ██╔══██║
  ██████╔╝██║██║  ██║███████║███████╗██║  ██║██║  ██║╚██████╗██║  ██║
  ╚═════╝ ╚═╝╚═╝  ╚═╝╚══════╝╚══════╝╚═╝  ╚═╝╚═╝  ╚═╝ ╚═════╝╚═╝  ╚═╝
       Advanced Web Path Scanner & Directory Brute-Forcer
```
<p align="center">
  <img src="https://img.shields.io/badge/dirsearch-Web_Bruteforcer-blue?style=for-the-badge" alt="dirsearch">
  <img src="https://img.shields.io/badge/Language-Python_Async-green?style=for-the-badge" alt="Python Async">
  <img src="https://img.shields.io/badge/Recon-Web_Discovery-orange?style=for-the-badge" alt="Découverte Web">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="Licence GPLv2">
</p>

<p align="center">
  <b>🌐 Scanner de chemin Web en ligne de commande rapide et riche en fonctionnalités avec gestion intelligente des extensions, récursivité et exploration</b>
</p>

---

## 📋 Table des matières

- [Introduction](#-introduction)
- [Comparaison d'outils](#-tool-comparison)
- [Installation](#-installation)
- [Utilisation de base et sélection de cible](#-basic-usage--target-selection)
- [Gestion des extensions et des suffixes](#-extension--gestion des suffixes)
- [Listes de mots et dictionnaires](#-listes de mots--dictionnaires)
- [Récursion et exploration du Web](#-récursion--exploration du Web)
- [Threading, limitation de débit et délais](#-threading-rate-limiting--delays)
- [Réponses de filtrage et de correspondance](#-filtrage--réponses-de-correspondance)
- [Personnalisation des requêtes HTTP](#-http-request-customization)
  - [En-têtes personnalisés et agents utilisateurs](#custom-headers--user-agents)
  - [Cookies et authentification](#cookies--authentification)
  - [Données POST et méthodes de requête](#post-data--request-methods)
- [Proxies, Tor & Evasion](#-proxies-tor--evasion)
- [Formats de sortie et rapports](#-output-formats--reporting)
- [Recettes de Pentest et Bug Bounty du monde réel](#-real-world-pentest--bug-bounty-recipes)
- [Tableau de référence rapide](#-quick-reference-table)
- [Conseils et bonnes pratiques](#-conseils--meilleures pratiques)
- [Mentions légales](#-legal-disclaimer)

---

## 🎯Présentation

**dirsearch** (créé par Mauro Soria) est l'un des outils de ligne de commande les plus populaires et les plus polyvalents pour découvrir des fichiers et des répertoires cachés sur des serveurs Web. Écrit en Python, il offre une flexibilité inégalée grâce à ses riches arguments de ligne de commande, sa sortie de terminal colorisée, son analyse récursive automatique, son extension intelligente d'extensions de caractères génériques et ses contrôles d'évasion intégrés.

Que vous effectuiez des tests d'intrusion Web, une recherche de bug bounty ou des évaluations internes du CTF, `dirsearch` est conçu pour découvrir les interfaces administratives cachées, les bases de données de sauvegarde, les points de terminaison d'API oubliés et les informations d'identification sensibles.

### Principales fonctionnalités

| Fonctionnalité | Descriptif |
|---------|-------------|
| **Multithread** | Moteur de requêtes asynchrone et threadé hautes performances |
| **Caractères génériques d'extension** | Développez `-e *` pour tester automatiquement toutes les extensions de fichiers Web connues |
| **Exploration intégrée** | Suit les liens HTML/JS pour amorcer des files d'attente de répertoires récursives |
| ** Filtrage riche ** | Filtrer les réponses par code d'état, taille d'octet, nombre de mots, nombre de lignes ou expression régulière |
| **Relecture de demande brute** | Charger les requêtes HTTP complètes de Burp Suite via `--raw` |
| **Formats multiples** | Génère des rapports en texte brut, JSON, CSV, Markdown et HTML |

---

## ⚖️ Comparaison d'outils

| Fonctionnalité | recherche | ffff | Feroxbuster | Gobuster | DIRB |
|---------|-----------|------|-------------|----------|------|
| **Langue** | Python | Aller | Rouille | Aller | C |
| **Vitesse** | ⭐⭐⭐⭐ (Rapide) | ⭐⭐⭐⭐⭐ (Extrêmement rapide) | ⭐⭐⭐⭐⭐ (Incroyablement rapide) | ⭐⭐⭐⭐ (Rapide) | ⭐⭐ (Fil bas) |
| **Récursion par défaut** | ✅ Automatique (`-r`) | En option (`-recursion`) | ✅ Automatique (`-d`) | En option (`-r`) | ✅ Automatique |
| **Caractère générique d'extension (`*`)**| ✅ Natif (`-e *`) | ❌ Non | ❌ Non | ❌ Non | ❌ Non |
| **Explorateur Web intégré** | ✅ Oui (`--crawl`) | ❌ Non | ✅ Oui (`--extract-links`) | ❌ Non | ❌ Non |
| **POST / Fuzzing des données** | ✅ Oui (`--data`) | ✅ HTTP complet | ❌ Limité | ❌ Non | ❌ Non |
| **Relecture de la demande Burp Raw**| ✅ Oui (`--raw`) | ✅ Oui (`-request`) | ❌ Non | ❌ Non | ❌ Non |
| **Liste de mots intégrée** | ✅ Organisé (`db/dicc.txt`)| ❌ Aucun | ❌ Aucun | ❌ Aucun | ✅Petite liste |

---

## 📥Installation

### Utilisation de pipx (recommandé pour Kali et Modern Linux)
```bash
# Install via pipx (isolated Python environment)
pipx install dirsearch

# Verify installation
dirsearch --version
```
### Utilisation de pip
```bash
# Install via standard pip
pip3 install dirsearch
```
### Utilisation de Git Clone (directement depuis la source)
```bash
# Clone the official repository
git clone https://github.com/maurosoria/dirsearch.git --depth 1
cd dirsearch

# Install requirements and run
pip3 install -r requirements.txt
python3 dirsearch.py --version
```
### Package Kali Linux et Docker
```bash
# Kali / Debian repository
sudo apt update && sudo apt install -y dirsearch

# Docker container
docker run -it --rm maurosoria/dirsearch -u https://example.com -e php,html
```
---

## 🚀 Utilisation de base et sélection de cibles

### Cible unique
```bash
# Basic scan against a single URL specifying extensions
dirsearch -u https://target.com -e php,html,js,txt

# Scan with trailing slash forced
dirsearch -u https://target.com -e php -f
```
### Cibles multiples
```bash
# Scan a list of targets from file
dirsearch -l targets.txt -e php,aspx,txt

# Scan stdin input from subfinder / httpx
cat live_hosts.txt | dirsearch --stdin -e php
```
### Analyse des ports et ciblage CIDR
```bash
# Scan specific ports on a host
dirsearch -u https://target.com --ports 80,443,8080,8443 -e php

# Scan an entire subnet range
dirsearch --cidr 192.168.1.0/24 -e html
```
---

## 🧩 Gestion des extensions et suffixes

`dirsearch` possède le système de gestion d'extensions le plus puissant parmi tous les scanners d'annuaire :
```bash
# Specify multiple extensions
dirsearch -u https://target.com -e php,html,json,txt,zip

# Use the wildcard (*) to test ALL known extensions automatically
dirsearch -u https://target.com -e *

# Add prefixes to wordlist entries (e.g. test_admin, dev_admin)
dirsearch -u https://target.com -e php --prefixes test_,dev_,staging_,old_

# Add suffixes to wordlist entries (e.g. admin_backup, admin_old)
dirsearch -u https://target.com -e php --suffixes _bak,~,_2026,_old
```
---

## 📚 Listes de mots et dictionnaires

`dirsearch` regroupe une liste de mots complète et testée au combat sur `db/dicc.txt` :
```bash
# Run using the default bundled dictionary
dirsearch -u https://target.com -e php

# Use SecLists Common or Medium wordlists
dirsearch -u https://target.com -e php -w /usr/share/seclists/Discovery/Web-Content/directory-list-2.3-medium.txt

# Use multiple wordlists simultaneously
dirsearch -u https://target.com -e php -w list1.txt,list2.txt

# Force all wordlist entries to lowercase or uppercase
dirsearch -u https://target.com -e php --lowercase
dirsearch -u https://target.com -e php --uppercase
```
---

## 🔄 Récursion et exploration du Web

### Analyse récursive
```bash
# Recursively scan newly found directories
dirsearch -u https://target.com -e php -r

# Limit recursion depth to 2 levels
dirsearch -u https://target.com -e php -r --recursion-depth 2

# Recursively scan only if HTTP status is 200, 301, or 302
dirsearch -u https://target.com -e php -r --recursion-status 200,301,302
```
### Robot d'exploration Web intégré
```bash
# Crawl the target website for internal URLs and feed them into the scan queue
dirsearch -u https://target.com -e php --crawl
```
---

## ⚡ Threading, limitation de débit et délais

Contrôlez l’agressivité de l’analyse pour éviter le déclenchement de la limitation de débit ou des pare-feu d’applications Web (WAF) :
```bash
# Set number of concurrent threads (default: 25)
dirsearch -u https://target.com -e php -t 50

# Add a delay between requests (e.g. 0.5 seconds)
dirsearch -u https://target.com -e php -s 0.5

# Enforce a maximum request rate per second (e.g. max 15 req/sec)
dirsearch -u https://target.com -e php --max-rate 15

# Customize HTTP connection timeouts and retries
dirsearch -u https://target.com -e php --timeout 8 --retries 3

# Exit scan immediately on excessive connection errors
dirsearch -u https://target.com -e php --exit-on-error
```
---

## 🎯 Filtrage et correspondance des réponses

### Inclusion et exclusion du code de statut
```bash
# Include only specific status codes (e.g., ignore everything except 200, 301, 403)
dirsearch -u https://target.com -e php -i 200,301,302,403

# Exclude specific status codes (e.g., exclude 404 and 500)
dirsearch -u https://target.com -e php -x 404,400,500,502,503
```
### Filtrage par taille, mots et contenu
```bash
# Exclude responses by specific byte size (e.g. ignore custom 404 pages)
dirsearch -u https://target.com -e php --exclude-sizes 0B,1542B,3421B

# Exclude responses matching specific text on the page
dirsearch -u https://target.com -e php --exclude-texts "Not Found","Access Denied"

# Exclude responses matching a regular expression
dirsearch -u https://target.com -e php --exclude-regex "^Error:.*"
```
---

## 🛠️ Personnalisation des requêtes HTTP

### En-têtes personnalisés et agents utilisateurs
```bash
# Set a custom header (e.g., authorization token or internal bypass header)
dirsearch -u https://target.com -e php -H "X-Forwarded-For: 127.0.0.1" -H "X-Requested-With: XMLHttpRequest"

# Load headers from a file
dirsearch -u https://target.com -e php --header-list custom_headers.txt

# Use a randomized User-Agent per request to bypass basic WAFs
dirsearch -u https://target.com -e php --random-agent

# Set a custom User-Agent
dirsearch -u https://target.com -e php --user-agent "Mozilla/5.0 (Windows NT 10.0; Win64; x64)"
```
### Cookies et authentification
```bash
# Provide session cookies for authenticated scanning
dirsearch -u https://target.com -e php -b "PHPSESSID=abc123xyz; admin_logged=true"

# HTTP Basic Authentication (username:password)
dirsearch -u https://target.com -e php --auth admin:SecretPass123 --auth-type basic

# NTLM Authentication for corporate Windows portals
dirsearch -u https://target.com -e php --auth CORP\\alice:Welcome2026 --auth-type ntlm
```
### Données POST et méthodes de requête
```bash
# Perform fuzzing using HTTP POST requests
dirsearch -u https://target.com/api -m POST -d "action=test&token=xyz" -e json

# Replay an entire raw HTTP request saved from Burp Suite
dirsearch -u https://target.com --raw request.txt -e php
```
---

## 🛡️ Proxies, Tor et évasion
```bash
# Route all traffic through an HTTP or SOCKS5 proxy (e.g., Burp Suite)
dirsearch -u https://target.com -e php --proxy http://127.0.0.1:8080

# Route all traffic through the local Tor SOCKS proxy
dirsearch -u https://target.com -e php --tor

# Skip SSL/TLS certificate verification
dirsearch -u https://target.com -e php --no-check-certificate
```
---

## 📄 Formats de sortie et rapports
```bash
# Save output as a clean text file
dirsearch -u https://target.com -e php -o results.txt

# Export results to JSON format
dirsearch -u https://target.com -e php -o results.json --format json

# Export results to HTML, CSV, or Markdown
dirsearch -u https://target.com -e php -o report.html --format html
dirsearch -u https://target.com -e php -o results.csv --format csv
dirsearch -u https://target.com -e php -o report.md --format markdown
```
---

## 🎯 Recettes réelles de Pentest et de Bug Bounty

### 1. Reconnaissance Web rapide et complète
```bash
# Scan with common web extensions, follow redirects, random UA, 40 threads
dirsearch -u https://target.com \
  -e php,html,js,json,txt,zip \
  -t 40 \
  --random-agent \
  -i 200,301,302,403 \
  -o target_recon.txt
```
### 2. Recherche de sauvegardes oubliées et de fuites de sources
```bash
# Test for database dumps, git repos, env files, and zip archives
dirsearch -u https://target.com \
  -e bak,old,orig,zip,tar.gz,sql,env,git \
  -w /usr/share/seclists/Discovery/Web-Content/raft-medium-files.txt \
  -i 200 \
  --exclude-sizes 0B
```
### 3. Découverte d'annuaire récursive en profondeur
```bash
# 2-level recursion, crawling pages for new paths, ignoring error status codes
dirsearch -u https://target.com \
  -e php,html \
  -r --recursion-depth 2 \
  --crawl \
  -x 404,500,502 \
  -o deep_scan.json --format json
```
### 4. Évaluation du portail interne authentifié
```bash
# Scan through Burp Suite with internal session cookies and WAF bypass headers
dirsearch -u https://intranet.corp.local \
  -e aspx,ashx,config,txt \
  -b "ASP.NET_SessionId=xyz123; AuthToken=ey..." \
  -H "X-Forwarded-For: 127.0.0.1" \
  --proxy http://127.0.0.1:8080
```
---

## ⚡ Tableau de référence rapide

| Tâche | Commande |
|------|--------------|
| **Analyse de base avec extensions** | `dirsearch -u https://target.com -e php,html,txt` |
| **Caractère générique pour toutes les extensions** | `dirsearch -u https://target.com -e *` |
| **Liste de mots personnalisée** | `dirsearch -u https://target.com -e php -w /path/to/wordlist.txt` |
| **Scanner plusieurs cibles** | `dirsearch -l targets.txt -e php,aspx` |
| **Découverte récursive** | `dirsearch -u https://target.com -e php -r --recursion-depth 2` |
| **Explorer les liens cibles** | `dirsearch -u https://target.com -e php --crawl` |
| **Inclure les codes d'état** | `dirsearch -u https://target.com -e php -i 200,301,302,403` |
| **Exclure les codes d'état** | `dirsearch -u https://target.com -e php -x 404,500,502` |
| **Exclure les tailles faussement positives**| `dirsearch -u https://target.com -e php --exclude-sizes 0B,1240B` |
| **Authentifier avec des cookies** | `dirsearch -u https://target.com -e php -b "session=abc123"` |
| **Itinéraire à travers Burp Suite** | `dirsearch -u https://target.com -e php --proxy http://127.0.0.1:8080` |
| **Enregistrer le rapport au format JSON** | `dirsearch -u https://target.com -e php -o report.json --format json` |

---

## 💡 Conseils et bonnes pratiques

1. **Utilisez `-e *` avec parcimonie sur les grandes listes de mots :** L'extension des extensions génériques multiplie chaque mot par plus de 20 extensions. Utilisez `-e *` uniquement avec des listes plus petites comme `common.txt` ou `dicc.txt`.
2. **Filtrer les tailles faussement positives :** Les frameworks Web modernes renvoient des pages 404 personnalisées avec HTTP 200. Inspectez la taille de la réponse par défaut et ajoutez `--exclude-sizes <size>` pour éliminer le bruit.
3. **Toujours vérifier les chemins interdits 403 :** Dans les environnements cloud et d'entreprise modernes, les réponses HTTP 403 indiquent que le répertoire ou le point de terminaison existe. Documentez tous les 403 pour les tests de contournement d'en-tête et de traversée de chemin.
4. **Tirez parti des préfixes et des suffixes pour les sauvegardes :** Les développeurs enregistrent fréquemment des copies de sauvegarde de fichiers sensibles (par exemple `config.php.bak`, `old_database.sql`). Utilisez `--prefixes` et `--suffixes` pour les découvrir automatiquement.

---

## ⚠️ Mentions légales

> **IMPORTANT :** L'analyse de port non autorisée, le forçage brutal de répertoires et l'énumération Web sur des serveurs sans autorisation écrite explicite sont illégaux. Utilisez `dirsearch` strictement dans les engagements légaux de bug bounty, les tests d'intrusion autorisés et les environnements de laboratoire CTF.