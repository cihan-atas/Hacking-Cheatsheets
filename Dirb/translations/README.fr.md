<!-- language-home-navigation -->
[🏠 Page d'accueil Française](../../README.fr.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 📁 DIRB - Aide-mémoire complet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ██████╗ ██╗██████╗ ██████╗ 
  ██╔══██╗██║██╔══██╗██╔══██╗
  ██║  ██║██║██████╔╝██████╔╝
  ██║  ██║██║██╔══██╗██╔══██╗
  ██████╔╝██║██║  ██║██████╔╝
  ╚═════╝ ╚═╝╚═╝  ╚═╝╚═════╝ 
        Web Content Scanner
```
<p align="center">
  <img src="https://img.shields.io/badge/DIRB-Content_Scanner-orange?style=for-the-badge" alt="DIRB">
  <img src="https://img.shields.io/badge/Wordlists-Bruteforce-blue?style=for-the-badge" alt="Listes de mots">
  <img src="https://img.shields.io/badge/Kali_Linux-Preinstalled-green?style=for-the-badge" alt="Kali Linux">
  <img src="https://img.shields.io/badge/Web-Pentesting-red?style=for-the-badge" alt="Web Pentesting">
</p>

<p align="center">
  <b>🔍 Scanner de contenu Web classique lançant des attaques basées sur un dictionnaire contre les serveurs Web</b>
</p>

---

## 📋 Table des matières

- [Introduction](#-introduction)
- [DIRB vs Alternatives modernes] (#-dirb-vs-modern-alternatives)
- [Installation](#-installation)
- [Listes de mots par défaut](#-listes de mots par défaut)
- [Syntaxe de base](#-basic-syntaxe)
- [Scénarios d'analyse courants](#-common-scanning-scenarios)
- [Options avancées et réglage](#-options-avancées--réglage)
- [Chasse de vulnérabilités avec des listes intégrées](#-chasse-de-vulnérabilités-avec-listes intégrées)
- [Flux de travail du monde réel](#-real-world-workflows)
- [Tableau de référence rapide](#-quick-reference-table)
- [Conseils et bonnes pratiques](#-conseils--meilleures pratiques)
- [Mentions légales](#-legal-disclaimer)
- [Ressources](#-ressources)

---

## 🎯Présentation

**DIRB** (Directory Buster) est un analyseur de contenu Web classique et léger basé sur des URL, écrit en C. Il lance des attaques basées sur un dictionnaire contre les serveurs Web, à la recherche d'objets Web existants et cachés (fichiers, répertoires, scripts et points de terminaison).

DIRB fonctionne en générant des requêtes HTTP pour chaque mot d'une liste de mots, en analysant les codes de réponse du serveur (200, 301, 302, 403, etc.) et en déterminant si une ressource existe.

### Caractéristiques clés

- ✅ **Analyse récursive automatique :** Analyse automatiquement les sous-répertoires découverts par défaut.
- ✅ **Listes de mots spécialisées intégrées :** Livré avec des listes de mots ciblées pour les serveurs Web courants, les CMS et les vulnérabilités.
- ✅ **Faible empreinte en ressources :** Binaire léger qui fonctionne correctement même sur du matériel peu performant ou des systèmes plus anciens.
- ✅ **Prise en charge des en-têtes et proxy personnalisés :** Intégration facile avec Burp Suite et les proxys HTTP en amont.

---

## ⚖️ DIRB vs alternatives modernes

| Fonctionnalité | DIRB | Gobuster | ffff | Feroxbuster | recherche |
|---------|------|----------|------|-------------|---------------|
| **Langue** | C | Aller | Aller | Rouille | Python |
| **Vitesse** | ⭐⭐ (Fil bas) | ⭐⭐⭐⭐⭐ (Rapide) | ⭐⭐⭐⭐⭐ (Extrêmement rapide) | ⭐⭐⭐⭐⭐ (Rouille asynchrone) | ⭐⭐⭐⭐ (Rapide) |
| **Récursion par défaut** | ✅ Oui (Automatique) | ❌ Non | ❌ Non (`-recursion` en option) | ✅ Oui (Automatique) | ✅ Oui (`-r`) |
| **Listes de mots personnalisées incluses** | ✅ Oui (`/usr/share/dirb/`) | ❌ Non | ❌ Non | ❌ Non | ✅ Oui (`db/dicc.txt`) |
| **VHost Fuzzing** | ❌ Non | ✅ Oui | ✅ Oui | ❌ Non | ✅ Oui (`--subdirs`) |
| **POST / Fuzzing des données** | ❌ Non | ❌ Non | ✅ Oui | ❌ Non | ✅ Oui (`-d`) |
| **Utilisation de la mémoire/du processeur** | Très faible | Faible | Faible | Très faible | Modéré |

---

## 📥Installation

### Kali Linux et système d'exploitation Parrot

DIRB est préinstallé dans Kali Linux et Parrot OS :
```bash
# Verify installation
dirb

# Update to latest package
sudo apt update && sudo apt install -y dirb
```
### Debian/Ubuntu
```bash
sudo apt update
sudo apt install -y dirb
```
### Compilation à partir des sources
```bash
# Install build dependencies
sudo apt install -y build-essential libcurl4-openssl-dev

# Download and extract source
wget https://sourceforge.net/projects/dirb/files/dirb/2.22/dirb222.tar.gz
tar -xzvf dirb222.tar.gz
cd dirb222

# Configure permissions and compile
chmod -R 755 conf
chmod 755 configure
./configure
make
sudo make install
```
---

## 📚 Listes de mots par défaut

Lorsqu'il est installé sur Kali Linux, DIRB inclut des listes de mots dédiées situées sur `/usr/share/dirb/wordlists/` :

| Chemin de la liste de mots | Entrées | Objectif |
|---------------|---------|--------------|
| `/usr/share/dirb/wordlists/common.txt` | ~4 614 | Liste par défaut utilisée lorsqu'aucune liste de mots n'est spécifiée |
| `/usr/share/dirb/wordlists/small.txt` | ~959 | Découverte rapide de chemins à haute probabilité |
| `/usr/share/dirb/wordlists/big.txt` | ~20 469 | Recherche exhaustive de répertoires et de fichiers |
| `/usr/share/dirb/wordlists/catala.txt` | ~6 846 | Liste de mots catalan |
| `/usr/share/dirb/wordlists/spanish.txt` | ~11 720 | Liste de mots espagnol |
| `/usr/share/dirb/wordlists/mutations_common.txt` | ~1 000 | Variations courantes de préfixe/suffixe |
| `/usr/share/dirb/wordlists/extensions_common.txt` | ~40 | Extensions Web standards (`.php`, `.asp`, `.html`, etc.) |

### Listes de mots de vulnérabilité intégrées (`/usr/share/dirb/wordlists/vulns/`)

- `apache.txt` - Fichiers, pages d'état et répertoires connus spécifiques à Apache
- `iis.txt` - Fichiers du serveur Web Microsoft IIS (`web.config`, `aspnet_client`, etc.)
- `tomcat.txt` - Chemins d'administration et de gestionnaire Apache Tomcat
- `cgis.txt` - Scripts et points de terminaison Common Gateway Interface (CGI)
-`fatwire.txt`, `hpsmh.txt`, `hyperion.txt`, `iplanet.txt`, `netware.txt`, `oracle.txt`

---

## ⌨️ Syntaxe de base
```bash
dirb <url_base> [<wordlist_file(s)>] [options]
```
### Exemples essentiels
```bash
# Basic scan using default common.txt wordlist
dirb http://10.10.10.10

# Scan using a specific custom wordlist
dirb https://example.com /usr/share/wordlists/dirb/big.txt

# Scan with custom SecLists directory list
dirb http://target.local /usr/share/seclists/Discovery/Web-Content/directory-list-2.3-medium.txt
```
---

## 🔍 Scénarios d'analyse courants

### 1. Numérisation avec les extensions de fichier (`-X` et `-x`)

Recherchez des extensions de fichiers spécifiques attachées à chaque mot dans le dictionnaire :
```bash
# Test specific extensions on every wordlist entry
dirb http://target.local -X .php,.html,.txt,.bak

# Use an extensions list file
dirb http://target.local -x /usr/share/dirb/wordlists/extensions_common.txt

# Combine directory brute-force with extension brute-force
dirb http://target.local /usr/share/dirb/wordlists/common.txt -X .php,.json,.zip
```
### 2. Contrôle de la récursivité (`-r` et `-R`)

Par défaut, DIRB analyse de manière récursive chaque répertoire découvert. Vous pouvez contrôler ce comportement :
```bash
# Disable recursion completely (scan root level only)
dirb http://target.local -r

# Interactive recursion (DIRB asks before descending into each directory)
dirb http://target.local -R
```
### 3. Analyse insensible à la casse (`-i`)

Utile pour cibler des serveurs Windows/IIS où les chemins de fichiers ne sont pas sensibles à la casse :
```bash
dirb http://target.local -i
```
### 4. Ignorer les codes d'état HTTP spécifiques (`-N`)

Filtrez les codes de réponse indésirables (par exemple, les boucles de redirection ou les chemins interdits) :
```bash
# Ignore 302 redirects and 403 forbidden responses
dirb http://target.local -N 302,403
```
---

## ⚙️ Options et réglages avancés

### Authentification (`-u`)

DIRB prend en charge l'authentification HTTP Basic et Digest :
```bash
# Supply HTTP authentication credentials
dirb http://target.local -u "admin:SecretP@ssw0rd!"

# Combine auth with custom wordlist
dirb http://target.local /usr/share/dirb/wordlists/big.txt -u "pentester:Summer2026"
```
### Cookies personnalisés et jetons de session (`-c`)

Testez les routes d'applications Web authentifiées en fournissant des cookies de session :
```bash
# Pass session cookie
dirb http://target.local -c "PHPSESSID=d92j4fn82mdf82ncb184; logged_in=true"
```
### En-têtes User-Agent et HTTP personnalisés (`-a` et `-H`)

Contournez les règles WAF de base ou simulez des navigateurs légitimes :
```bash
# Set custom User-Agent
dirb http://target.local -a "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36"

# Add custom HTTP Header
dirb http://target.local -H "Authorization: Bearer eyJhbGciOi..."
dirb http://target.local -H "X-Forwarded-For: 127.0.0.1"
```
### Routage proxy et intégration de Burp Suite (`-p` et `-P`)

Envoyez le trafic DIRB via un proxy HTTP en amont comme Burp Suite ou OWASP ZAP :
```bash
# Route through local Burp Suite proxy
dirb http://target.local -p 127.0.0.1:8080

# Authenticated proxy
dirb http://target.local -p 10.10.10.1:3128 -P "proxyuser:proxypass"
```
### Limitation de débit et évasion (`-z`)

Évitez de déclencher des alertes IDS/IPS ou des services Web fragiles DoSing en ajoutant un délai entre les requêtes (en millisecondes) :
```bash
# Wait 250 milliseconds between each request
dirb http://target.local -z 250

# Slow, stealthy scan (1 second delay)
dirb http://target.local -z 1000
```
### Journalisation des sorties (`-o` et `-w`)
```bash
# Save results to a file
dirb http://target.local -o /home/zeus/scans/dirb_target.txt

# Do not halt on WARNING messages (useful for uninterrupted scans)
dirb http://target.local -w -o dirb_output.txt
```
---

## 🎯 Recherche de vulnérabilités avec des listes intégrées

DIRB inclut des listes ciblées dans `/usr/share/dirb/wordlists/vulns/` pour détecter les serveurs non corrigés et les points de terminaison dangereux :
```bash
# Target Apache server vulnerabilities and status pages
dirb http://target.local /usr/share/dirb/wordlists/vulns/apache.txt

# Target IIS endpoints and misconfigurations
dirb http://target.local /usr/share/dirb/wordlists/vulns/iis.txt

# Target Tomcat management consoles
dirb http://target.local /usr/share/dirb/wordlists/vulns/tomcat.txt

# Search for old CGI scripts and shellshock entrypoints
dirb http://target.local /usr/share/dirb/wordlists/vulns/cgis.txt
```
---

## 🎬 Flux de travail du monde réel

### Workflow 1 : Reconnaissance initiale rapide

Lorsque vous rencontrez pour la première fois un serveur Web sur le port 80/443 :
```bash
# Rapid scan without recursion using small wordlist
dirb http://10.10.10.50 /usr/share/dirb/wordlists/small.txt -r
```
### Workflow 2 : Évaluation exhaustive des applications Web

Effectuez une analyse approfondie à la recherche de sauvegardes de configuration, de code source et de points de terminaison d'API :
```bash
dirb https://app.target.local /usr/share/dirb/wordlists/common.txt \
  -X .php,.bak,.sql,.config,.txt \
  -a "Mozilla/5.0" \
  -w \
  -o dirb_app_scan.txt
```
### Workflow 3 : Exploration du portail authentifié via Burp Suite
```bash
dirb https://internal.target.local/portal/ /usr/share/dirb/wordlists/big.txt \
  -c "session=s%3A_kJw81..." \
  -p 127.0.0.1:8080 \
  -X .jsp,.do,.action \
  -z 100
```
---

## 📊 Tableau de référence rapide

| Drapeau | Arguments | Descriptif | Exemple |
|------|----------|-------------|---------|
| `-a` | `<user_agent>` | Spécifier une chaîne User-Agent personnalisée | `dirb http://target -a "Mozilla/5.0"` |
| `-c` | `<cookie>` | Définir l'en-tête du cookie HTTP | `dirb http://target -c "id=123"` |
| `-H` | `<header>` | Ajouter un en-tête HTTP personnalisé | `dirb http://target -H "X-API-Key: secret"` |
| `-i` | Aucun | Recherche insensible à la casse | `dirb http://target -i` |
| `-N` | `<codes>` | Ignorer les réponses correspondant au code d'état | `dirb http://target -N 302,403` |
| `-o` | `<output_file>` | Enregistrer les résultats de l'analyse dans un fichier | `dirb http://target -o results.txt` |
| `-p` | `<proxy:port>` | Utiliser un proxy HTTP | `dirb http://target -p 127.0.0.1:8080` |
| `-P` | `<user:pass>` | Informations d'authentification du proxy | `dirb http://target -p proxy:8080 -P u:p` |
| `-r` | Aucun | Ne cherchez pas de manière récursive | `dirb http://target -r` |
| `-R` | Aucun | Recherche récursive interactive | `dirb http://target -R` |
| `-u` | `<user:pass>` | Authentification HTTP Basic/Digest | `dirb http://target -u "admin:1234"` |
| `-w` | Aucun | Ne pas arrêter l'analyse en cas de messages d'AVERTISSEMENT | `dirb http://target -w` |
| `-X` | `<ext_list>` | Ajouter des extensions séparées par des virgules | `dirb http://target -X .php,.html` |
| `-x` | `<ext_file>` | Ajouter des extensions à partir du fichier | `dirb http://target -x ext.txt` |
| `-z` | `<millisecs>` | Ajouter un délai entre les demandes | `dirb http://target -z 250` |

---

## 💡 Conseils et bonnes pratiques

1. **Attention aux réponses Wildcard 200/302 :**
   - Certaines applications Web renvoient HTTP 200 ou 302 pour chaque requête (gestionnaire 404 personnalisé). DIRB signalera chaque entrée comme trouvée. Utilisez `-N 302` ou passez à `ffuf` / `feroxbuster` pour un filtrage avancé de taille/mot.
2. **Combinez avec `-r` pour les grands dictionnaires :**
   - Si vous utilisez `directory-list-2.3-medium.txt` (220 000 mots), désactivez toujours la récursivité avec `-r`. Sinon, DIRB peut prendre plusieurs jours pour explorer les répertoires profondément imbriqués.
3. **Toujours vérifier les extensions :**
   - Les backends modernes exposent fréquemment des fichiers sensibles comme `.env`, `.git`, `.bak`, `.conf` ou `.sql`. Testez toujours les extensions courantes avec `-X`.
4. **Utilisez `-w` pour les analyses sans surveillance :**
   - Par défaut, DIRB peut s'arrêter s'il rencontre des avertissements SSL ou réseau. La réussite du `-w` garantit que les tests sont terminés sans interaction manuelle.

---

## ⚠️ Mentions légales

> **IMPORTANT :** DIRB est conçu pour les audits de sécurité, les tests d'intrusion et la recherche éthique sur les systèmes que vous possédez ou que vous disposez d'une autorisation explicite et documentée pour tester. Les attaques non autorisées par dictionnaire contre les serveurs Web peuvent provoquer une interruption du service et sont illégales en vertu de la législation mondiale sur la criminalité informatique.

---

## 📚 Ressources

- [Projet DIRB SourceForge](https://sourceforge.net/projects/dirb/)
- [Documentation du package DIRB Kali Linux](https://www.kali.org/tools/dirb/)
- [SecLists - Listes de mots de découverte de contenu Web](https://github.com/danielmiessler/SecLists)