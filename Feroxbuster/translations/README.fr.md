<!-- language-home-navigation -->
[🏠 Page d'accueil Française](../../README.fr.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🦀 Feroxbuster - Aide-mémoire complet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ███████╗███████╗██████╗  ██████╗ ██╗  ██╗██████╗ ██╗   ██╗███████╗████████╗███████╗██████╗ 
  ██╔════╝██╔════╝██╔══██╗██╔═══██╗╚██╗██╔╝██╔══██╗██║   ██║██╔════╝╚══██╔══╝██╔════╝██╔══██╗
  █████╗  █████╗  ██████╔╝██║   ██║ ╚███╔╝ ██████╔╝██║   ██║███████╗   ██║   █████╗  ██████╔╝
  ██╔══╝  ██╔══╝  ██╔══██╗██║   ██║ ██╔██╗ ██╔══██╗██║   ██║╚════██║   ██║   ██╔══╝  ██╔══██╗
  ██║     ███████╗██║  ██║╚██████╔╝██╔╝ ██╗██████╔╝╚██████╔╝███████║   ██║   ███████╗██║  ██║
  ╚═╝     ╚══════╝╚═╝  ╚═╝ ╚═════╝ ╚═╝  ╚═╝╚═════╝  ╚═════╝ ╚══════╝   ╚═╝   ╚══════╝╚═╝  ╚═╝
                Fast, Simple, Recursive Content Discovery
```
<p align="center">
  <img src="https://img.shields.io/badge/Feroxbuster-Content_Discovery-orange?style=for-the-badge" alt="Feroxbuster">
  <img src="https://img.shields.io/badge/Rust-Blazingly_Fast-blue?style=for-the-badge" alt="Rust">
  <img src="https://img.shields.io/badge/Auto_Tune-Recursive-green?style=for-the-badge" alt="Récursif">
  <img src="https://img.shields.io/badge/Bug_Bounty-Web_Pentest-red?style=for-the-badge" alt="Web Pentest">
</p>

<p align="center">
  <b>⚡ Un outil de découverte de contenu rapide, simple et récursif écrit en Rust</b>
</p>

---

## 📋 Table des matières

- [Introduction](#-introduction)
- [Principales fonctionnalités](#-key-features)
- [Comparaison d'outils](#-tool-comparison)
- [Installation](#-installation)
- [Syntaxe de base et démarrage rapide](#-basic-syntax--quick-start)
- [Options de commande principales](#-core-command-options)
  - [Contrôle de récursion](#recursion-control)
  - [Concurrency & Performance](#concurrency--performance)
  - [Extensions de fichiers](#extensions de fichiers)
  - [Filtrage et correspondance](#filtrage--correspondance)
  -[Fonctionnalités intelligentes (réglage automatique, extraction de liens, sauvegardes)](#smart-features)
  - [En-têtes, cookies et authentification](#headers-cookies--auth)
  - [Intégration de Proxy et Burp Suite](#proxy--burp-suite-integration)
- [Analyse et pipelines multi-cibles](#-analyse-multi-cibles--pipelines)
- [Flux de travail du monde réel](#-real-world-workflows)
- [Tableau de référence rapide](#-quick-reference-table)
- [Conseils et bonnes pratiques](#-conseils--meilleures pratiques)
- [Mentions légales](#-legal-disclaimer)
- [Ressources](#-ressources)

---

## 🎯Présentation

**Feroxbuster** est un scanner de contenu Web récursif, multithread et hautes performances, écrit en Rust. Conçu pour être rapide, simple et intuitif, il combine la vitesse des fuzzers basés sur Go/Rust avec la découverte de répertoires récursive automatique native.

Il brille lors des tests d'intrusion et des engagements de bug bounty où les arborescences de sites Web approfondies et les répertoires cachés doivent être découverts rapidement sans générer de scripts manuels complexes.

---

## 🌟 Principales fonctionnalités

- ⚡ **Incroyablement rapide :** Propulsé par Rust asynchrone (`tokio` et `reqwest`), capable d'effectuer des milliers de requêtes par seconde.
- 🔄 **Découverte récursive native :** Découvre automatiquement de nouveaux répertoires et les analyse de manière récursive jusqu'à des limites de profondeur configurables.
- 🧠 **Auto-Tune & Auto-Bail :** Limite automatiquement la vitesse d'analyse si le serveur Web cible ralentit ou s'interrompt si des erreurs de connexion excessives se produisent.
- 🔗 **Extraction de liens en ligne :** Analyse les réponses HTML et JavaScript pour découvrir les points de terminaison cachés et les renvoie dans la file d'attente d'analyse.
- 📦 **Recherche de sauvegarde automatisée :** Ajoute automatiquement les variantes `.bak`, `~`, `.old` et `.orig` aux fichiers découverts.
- 🎯 **Pipeable Input :** Accepte de manière transparente les listes de cibles de `stdin` (à partir d'outils comme `subfinder`, `httpx` ou `gau`).

---

## ⚖️ Comparaison d'outils

| Fonctionnalité | Feroxbuster | ffff | recherche | Gobuster | Dirb |
|---------|-------------|------|---------------|--------------|------|
| **Langue** | Rouille | Aller | Python | Aller | C |
| **Vitesse** | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐ |
| **Récursion par défaut** | ✅ Automatique | ❌ Drapeau manuel | ✅ Automatique (`-r`) | ❌ Aucun | ✅ Automatique |
| **Extraction de liens** | ✅ Oui (`--extract-links`) | ❌ Non | ❌ Non | ❌ Non | ❌ Non |
| **Réglage automatique du taux** | ✅ Oui (`--auto-tune`) | ❌ Non | ❌ Non | ❌ Non | ❌ Non |
| **POST Fuzzing** | ❌ Non | ✅Autochtone | ✅ Natif (`--data`) | ❌ Non | ❌ Non |
| **Stdin multi-cible** | ✅Autochtone | ❌ Limité | ✅ Oui (`-l`) | ❌ Non | ❌ Non |
| **Détection de fichiers de sauvegarde**| ✅ Natif (`--collect-backups`)| ❌ Non | ✅ Natif (`-e *`) | ❌ Non | ❌ Non |

---

## 📥Installation

### Kali Linux et Debian / Ubuntu
```bash
# Update repository and install
sudo apt update && sudo apt install -y feroxbuster
```
### Utilisation de Cargo (gestionnaire de packages Rust)
```bash
cargo install feroxbuster
```
### macOS (Homebrew)
```bash
brew install feroxbuster
```
### Docker
```bash
docker run --init -it epi052/feroxbuster -u https://target.com -w /usr/share/seclists/Discovery/Web-Content/common.txt
```
### Binaire précompilé (versions GitHub)
```bash
# Download latest Linux release
curl -sL https://raw.githubusercontent.com/epi052/feroxbuster/main/install-nix.sh | bash
sudo mv feroxbuster /usr/local/bin/
```
---

## 🚀 Syntaxe de base et démarrage rapide
```bash
feroxbuster -u <TARGET_URL> -w <WORDLIST> [OPTIONS]
```
### Exemples d'analyse rapide
```bash
# Scan a single target with default settings
feroxbuster -u http://10.10.10.10

# Scan with custom SecLists directory list
feroxbuster -u https://example.com -w /usr/share/seclists/Discovery/Web-Content/directory-list-2.3-medium.txt

# Scan target including specific file extensions
feroxbuster -u https://example.com -w /usr/share/wordlists/dirb/common.txt -x php,html,txt,json
```
---

## ⚙️ Options de commande principales

### Contrôle de récursion
```bash
# Specify maximum recursion depth (default is 4)
feroxbuster -u https://example.com -w common.txt -d 2

# Completely disable recursion (scan root directory only)
feroxbuster -u https://example.com -w common.txt -n

# Don't scan nested subdirectories automatically (interactive prompt)
feroxbuster -u https://example.com -w common.txt --dont-scan /api,/static
```
### Concurrence et performances
```bash
# Set number of concurrent threads per directory (default: 50)
feroxbuster -u https://example.com -t 100

# Limit total concurrent directory scans running simultaneously (default: unlimited)
feroxbuster -u https://example.com -L 3

# Rate limit requests per second to avoid triggering WAF/firewall blocks
feroxbuster -u https://example.com --rate-limit 150

# Request timeout in seconds (default: 7)
feroxbuster -u https://example.com -T 5
```
### Extensions de fichiers
```bash
# Append extensions to each wordlist item
feroxbuster -u https://example.com -x php,asp,aspx,jsp,txt,bak

# Automatically collect and fuzz file extensions found during crawling
feroxbuster -u https://example.com --collect-extensions
```
### Filtrage et correspondance
```bash
# Match only specific status codes (e.g., 200 OK and 301 Redirects)
feroxbuster -u https://example.com -s 200 301 302

# Filter out specific status codes (e.g., exclude 404 and 403)
feroxbuster -u https://example.com -C 404 403 500

# Filter responses matching exact size in bytes
feroxbuster -u https://example.com -S 4092 1024

# Filter responses matching exact word count
feroxbuster -u https://example.com -W 42

# Filter responses matching exact line count
feroxbuster -u https://example.com -N 12

# Filter responses matching regular expressions (e.g., generic error pages)
feroxbuster -u https://example.com --filter-regex "Under Construction|Page Not Found"

# Filter responses similar to a known 404/wildcard page
feroxbuster -u https://example.com --filter-similar-to https://example.com/definitelynotfoundpage404
```
### Fonctionnalités intelligentes
```bash
# Auto-tune: Automatically adjusts request concurrency based on server response latency
feroxbuster -u https://example.com --auto-tune

# Auto-bail: Aborts scanning automatically when more than 100 consecutive network errors occur
feroxbuster -u https://example.com --auto-bail

# Extract links: Parses HTML and JavaScript to automatically uncover endpoints
feroxbuster -u https://example.com --extract-links

# Collect backups: Automatically tests for backup copies (.bak, ~, .old) of every discovered file
feroxbuster -u https://example.com --collect-backups
```
### En-têtes, cookies et authentification
```bash
# Custom HTTP Header
feroxbuster -u https://example.com -H "Authorization: Bearer <TOKEN>" -H "X-Forwarded-For: 127.0.0.1"

# Session Cookie
feroxbuster -u https://example.com -b "session=abc123xyz; role=admin"

# Custom User-Agent string
feroxbuster -u https://example.com -a "Mozilla/5.0 (Windows NT 10.0; Win64; x64)"

# Ignore invalid / self-signed TLS certificates
feroxbuster -u https://10.10.10.10 -k
```
### Intégration de la suite proxy et Burp
```bash
# Route all requests through Burp Suite proxy
feroxbuster -u https://example.com -p http://127.0.0.1:8080 -k

# Convenience shortcut flag for Burp Suite (enables -k and sets proxy to localhost:8080)
feroxbuster -u https://example.com --burp
```
---

## 🔁 Analyse et pipelines multi-cibles

Feroxbuster lit nativement les URL cibles à partir de l'entrée standard (`stdin`), ce qui le rend idéal pour l'intégration avec les pipelines de découverte d'actifs :
```bash
# Pipeline: Subdomain discovery -> HTTP Probing -> Feroxbuster Fuzzing
subfinder -d example.com -silent | \
  httpx -silent -mc 200,301,302 | \
  feroxbuster --stdin -w /usr/share/seclists/Discovery/Web-Content/raft-medium-directories.txt -t 30 -L 4
```
### Analyser les URL à partir d'un fichier
```bash
cat targets.txt | feroxbuster --stdin -w common.txt -v
```
---

## 🎬 Flux de travail du monde réel

### Workflow 1 : Reconnaissance Web complète de Bug Bounty

Concurrence élevée, réglage automatique du débit, extraction de liens et détection de sauvegarde :
```bash
feroxbuster -u https://target.com \
  -w /usr/share/seclists/Discovery/Web-Content/directory-list-2.3-medium.txt \
  -x php,js,json,txt,html \
  --extract-links \
  --collect-backups \
  --auto-tune \
  -o ferox_target_scan.txt
```
### Workflow 2 : Découverte du portail interne de l'entreprise/Active Directory

Scan d'une application web interne via Burp Suite avec authentification et certificats auto-signés :
```bash
feroxbuster -u https://internal-portal.corp.local \
  -w /usr/share/seclists/Discovery/Web-Content/common.txt \
  -x aspx,ashx,asmx,config,txt \
  -b "ASP.NET_SessionId=k2m9fl3p8..." \
  --burp \
  -d 3
```
### Workflow 3 : API non récursive rapide et découverte des points de terminaison
```bash
feroxbuster -u https://api.target.com/v1 \
  -w /usr/share/seclists/Discovery/Web-Content/api/api-endpoints.txt \
  -n \
  -s 200,201,204,401,403 \
  -t 100 \
  --json \
  -o api_results.json
```
---

## 📊 Tableau de référence rapide

| Drapeau | Short | Descriptif | Exemple |
|------|-------|-------------|---------|
| `--url` | `-u` | URL cible à analyser | `feroxbuster -u https://target.com` |
| `--wordlist` | `-w` | Path to wordlist | `feroxbuster -w /path/to/list.txt` |
| `--threads` | `-t` | Nombre de threads simultanés par répertoire | `feroxbuster -t 80` |
| `--scan-limit` | `-L` | Limiter le nombre total d'analyses simultanées de répertoires | `feroxbuster -L 3` |
| `--depth` | `-d` | Profondeur de récursion maximale (par défaut 4) | `feroxbuster -d 2` |
| `--no-recursion` | `-n` | Désactiver l'analyse récursive des répertoires | `feroxbuster -n` |
| `--extensions` | `-x` | Extensions de fichiers à ajouter | `feroxbuster -x php,html,bak` |
| `--status-codes` | `-s` | Codes d'état à inclure | `feroxbuster -s 200 301 302` |
| `--filter-status` | `-C` | Codes de statut à exclure | `feroxbuster -C 404 500` |
| `--filter-size` | `-S` | Tailles de réponse à exclure | `feroxbuster -S 4096` |
| `--filter-words` | `-W` | Le nombre de mots à exclure | `feroxbuster -W 15` |
| `--filter-lines` | `-N` | Nombre de lignes à exclure | `feroxbuster -N 8` |
| `--auto-tune` | Aucun | Ajuster dynamiquement la vitesse en fonction de la latence | `feroxbuster --auto-tune` |
| `--auto-bail` | Aucun | Abandonner l'analyse en cas d'erreurs réseau excessives | `feroxbuster --auto-bail` |
| `--extract-links`| Aucun | Analyser HTML/JS pour les nouveaux points de terminaison | `feroxbuster --extract-links` |
| `--collect-backups` | Aucun | Rechercher les variantes de sauvegarde (`.bak`, `.old`) | `feroxbuster --collect-backups` |
| `--proxy` | `-p` | Envoyer du trafic via un proxy HTTP | `feroxbuster -p http://127.0.0.1:8080` |
| `--burp` | Aucun | Raccourci pour `-p http://127.0.0.1:8080 -k` | `feroxbuster --burp` |
| `--stdin` | Aucun | Lire les URL à partir de l'entrée standard | `cat urls.txt \| feroxbuster --stdin` |
| `--output` | `-o` | Chemin du fichier de sortie | `feroxbuster -o results.txt` |
| `--json` | Aucun | Formater la sortie au format JSON | `feroxbuster --json -o out.json` |

---

## 💡 Conseils et bonnes pratiques

1. **Utilisez `--auto-tune` pour les cibles distantes :**
   - Au lieu de deviner le nombre de threads (`-t`), `--auto-tune` mesure activement les temps de réponse cibles et limite ou accélère dynamiquement, évitant ainsi un déni de service accidentel du serveur.
2. **Combinez `--extract-links` et `--collect-backups` :**
   - Cette combinaison est exceptionnellement mortelle dans les évaluations de bug bounty : Feroxbuster extrait les routes JS cachées, les analyse et vérifie immédiatement si des sauvegardes de développeur (`.bak`, `.swp`, `~`) ont été laissées sur le serveur.
3. **Limiter les analyses sur les grandes listes de mots avec `-L` :**
   - Si vous utilisez un dictionnaire de plus de 200 000 mots, la récursion sans restriction peut lancer des dizaines de threads simultanés. Utilisez `-L 2` ou `-L 3` pour maintenir la mémoire et la bande passante dans des limites de fonctionnement sûres.
4. **Utilisez les fichiers d'état pour reprendre les audits importants :**
   - Feroxbuster enregistre périodiquement les fichiers d'état (`.state`). Si une analyse interrompue se produit, reprenez instantanément avec `feroxbuster --resume-from <FILE>.state`.

---

## ⚠️ Mentions légales

> **IMPORTANT :** Feroxbuster est conçu strictement pour les évaluations de sécurité autorisées, les compétitions CTF et les tests d'intrusion effectués avec autorisation écrite. L'analyse de cibles non autorisées peut saturer l'infrastructure réseau et est strictement interdite par la loi.

---

## 📚 Ressources

- [Dépôt Feroxbuster GitHub](https://github.com/epi052/feroxbuster)
- [Documentation officielle Feroxbuster](https://epi052.github.io/feroxbuster-docs/)
- [Listes de découverte SecLists](https://github.com/danielmiessler/SecLists)