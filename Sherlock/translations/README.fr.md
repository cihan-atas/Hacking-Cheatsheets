<!-- language-home-navigation -->
[🏠 Page d'accueil Française](../../README.fr.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# Aide-mémoire Sherlock

[Sherlock](https://github.com/sherlock-project/sherlock) est un outil de reconnaissance OSINT qui recherche simultanément les comptes de réseaux sociaux par nom d'utilisateur sur plus de 400 plateformes sociales, forums, sites d'hébergement de code et communautés en ligne.

---

## Table des matières

- [Capacités clés](#key-capabilities)
- [Installation](#installation)
- [Recherche de nom d'utilisateur unique et multiple] (#single--multiple-username-search)
- [Filtrer les sites et les plateformes](#filtering-sites--plateformes)
- [Proxy et routage Tor](#proxy--tor-routing)
- [Timeout et réglage du thread](#timeout--threading-tuning)
- [Formats d'exportation (CSV, XLSX, JSON)](#export-formats-csv-xlsx-json)
-[Automation et intégration du pipeline OSINT](#automation--osint-pipeline-integration)

---

## Capacités clés

- **Plus de 400 plates-formes :** Requêtes GitHub, Twitter/X, Instagram, Reddit, TikTok, LinkedIn, Telegram, Discord, Steam, Spotify, Docker Hub, Pastebin et des centaines d'autres.
- **Concurrence asynchrone :** Analyse parallèle à grande vitesse à l'aide de requêtes Python asynchrones.
- **Détection précise :** Détecte les codes d'état HTTP, les messages d'erreur et les redirections pour éviter les faux positifs.
- **Support Tor & SOCKS5 :** Protège l'anonymat des enquêteurs et contourne les limites de taux.

---

##Installation
```bash
# Recommended: Install via pipx
pipx install sherlock-project

# Or install from GitHub repository
git clone https://github.com/sherlock-project/sherlock.git
cd sherlock
python3 -m pip install -r requirements.txt

# Debian / Kali Linux repository
sudo apt-get update && sudo apt-get install -y sherlock

# Verify command
sherlock --version
```
---

## Recherche de noms d'utilisateur uniques et multiples

### 1. Recherche de base de nom d'utilisateur
Recherchez un identifiant cible unique sur plus de 400 sites :
```bash
sherlock target_user
```
### 2. Rechercher plusieurs noms d'utilisateur simultanément
```bash
sherlock target_user1 target_user2 alias_dev
```
### 3. Imprimer uniquement les comptes trouvés (supprimer les comptes introuvables/erreurs)
```bash
sherlock --print-found target_user
```
---

## Filtrage des sites et des plateformes

Limitez les recherches à des plateformes spécifiques pour gagner du temps ou concentrez-vous sur des catégories de renseignements spécifiques :

### 1. Interroger des sites spécifiques uniquement
```bash
sherlock --site github --site twitter --site reddit --site gitlab target_user
```
### 2. Rechercher sans plates-formes spécifiques (ignorer les sites lourds à faux positifs)
```bash
sherlock target_user --site-list targets.txt
```
---

## Routage proxy et Tor

Pour éviter les interdictions IP, les limites de débit et maintenir la sécurité opérationnelle :

### 1. Route via le réseau Tor
Acheminer automatiquement les requêtes via le proxy Tor SOCKS local (`127.0.0.1:9050`) :
```bash
sherlock --tor target_user
```
### 2. Proxy HTTP/SOCKS5 personnalisé
```bash
# SOCKS5 proxy
sherlock --proxy socks5://127.0.0.1:1080 target_user

# Authenticated HTTP proxy
sherlock --proxy http://user:pass@proxy.corp.internal:8080 target_user
```
---

## Délai d'expiration et réglage du thread

Contrôlez les vitesses des requêtes et les tolérances de latence :
```bash
# Set HTTP timeout to 5 seconds per site (default is usually 60s)
sherlock target_user --timeout 5

# Add delay between requests to avoid rate limits
sherlock target_user --delay 1
```
---

## Formats d'exportation (CSV, XLSX, JSON)

Enregistrez les découvertes pour des enquêtes, des ensembles de preuves ou des rapports :

### 1. Enregistrez directement dans un fichier CSV
```bash
sherlock --csv --output /tmp/sherlock_results target_user
```
### 2. Enregistrez la sortie dans un répertoire spécifique
```bash
sherlock --folderoutput /tmp/recon_data/ target_user
```
Génère `/tmp/recon_data/target_user.txt` contenant les URL complètes des profils confirmés.

### 3. Enregistrer sous forme de feuille de calcul Excel (XLSX)
```bash
sherlock --xlsx target_user
```
---

## Automatisation et intégration du pipeline OSINT

Enchaînez Sherlock dans un script d'enquêteur automatisé :
```bash
#!/usr/bin/env bash
TARGET="$1"
OUTDIR="/tmp/osint_${TARGET}"
mkdir -p "$OUTDIR"

echo "[*] Scanning for username: $TARGET"
sherlock --print-found --timeout 10 --folderoutput "$OUTDIR" "$TARGET"

if [ -f "$OUTDIR/${TARGET}.txt" ]; then
    echo "[+] Found $(wc -l < "$OUTDIR/${TARGET}.txt") accounts:"
    cat "$OUTDIR/${TARGET}.txt"
fi
```