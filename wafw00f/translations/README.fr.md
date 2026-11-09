<!-- language-home-navigation -->
[🏠 Page d'accueil Française](../../README.fr.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# wafw00f Aide-mémoire

[wafw00f](https://github.com/EnableSecurity/wafw00f) est un outil d'empreinte digitale et de détection de pare-feu d'application Web (WAF) en ligne de commande open source. Il identifie et prend en compte plus de 150 solutions de sécurité WAF commerciales et open source protégeant les sites Web cibles en envoyant des requêtes HTTP normales et provocatrices et en analysant les réponses comportementales, les en-têtes et les cookies.

---

## Table des matières

-[Pourquoi les WAF d'empreintes digitales ?](#why-fingerprint-wafs)
- [Installation](#installation)
- [Analyse de base et cible](#basic--target-scanning)
- [Détection multi-WAF exhaustive (`-a`)](#exhaustive-multi-waf-detection--a)
- [Analyse cible en masse à partir d'un fichier (`-i`)](#bulk-target-scanning-from-file--i)
-[Proxy et routage Tor (`-p`)](#proxy--tor-routing--p)
- [Formats d'exportation (JSON, CSV, Texte)](#export-formats-json-csv-text)
- [Liste des signatures WAF prises en charge (`-l`)](#list-supported-waf-signatures--l)
-[Bug Bounty et intégration des tests d'intrusion](#bug-bounty--penetration-testing-integration)

---

## Pourquoi les WAF à empreintes digitales ?

Le lancement d’attaques Web actives (injection SQL, XSS, SSRF) contre une cible protégée sans reconnaissance préalable entraîne souvent des bannissements IP instantanés ou des résultats de vulnérabilité faussement négatifs. L'identification du WAF exact permet :
- **Évasion sur mesure :** Sélection de techniques de contournement spécifiques au fournisseur (par exemple, normalisation Unicode pour ModSecurity, codage de transfert fragmenté pour Cloudflare).
- **Recherche d'adresse IP d'origine :** Si Cloudflare ou Akamai est détecté, pivotez pour trouver l'adresse IP d'origine du backend avant de tester.
- **Conscience des seuils de règles :** Réglage de la concurrence du scanner et des limites de débit en dessous des seuils de blocage.

---

##Installation
```bash
# Debian / Ubuntu / Kali Linux
sudo apt-get update && sudo apt-get install -y wafw00f

# Recommended: Install via pipx
pipx install wafw00f

# Via standard Python pip
python3 -m pip install --user wafw00f

# Verify installation
wafw00f --version
```
---

## Analyse de base et cible

### 1. Analyser un seul domaine
```bash
wafw00f https://example.com
```
*wafw00f envoie des requêtes HTTP de base suivies de modèles malveillants personnalisés pour déclencher des filtres de sécurité, identifiant les signatures dans les en-têtes, les codes d'état et les pages d'erreur.*

### 2. Mode de sortie détaillé (`-v` / `-vv`)
Inspectez les en-têtes correspondants spécifiques, les codes d'état HTTP et les heuristiques :
```bash
wafw00f https://example.com -v
```
### 3. Désactiver le suivi de redirection (`-r`)
Empêchez wafw00f de suivre les redirections 301/302 pour séparer les proxys inverses Edge des applications backend :
```bash
wafw00f https://example.com -r
```
---

## Détection multi-WAF exhaustive (`-a`)

Par défaut, wafw00f arrête l'analyse dès que la première correspondance WAF est confirmée. Utilisez `-a` pour vérifier toutes les signatures de fournisseurs connues (essentiel lorsqu'une application repose derrière une défense à plusieurs niveaux comme Cloudflare CDN + AWS WAF) :
```bash
wafw00f https://example.com -a
```
---

## Analyse cible en masse à partir d'un fichier (`-i`)

Analysez une liste complète de sous-domaines ou d'actifs cibles collectés à partir de `subfinder` ou `amass` :
```bash
wafw00f -i /path/to/domains.txt -o /tmp/waf_results.txt
```
---

## Routage proxy et Tor (`-p`)

Acheminez le trafic de sondage via des proxys pour empêcher la mise sur liste noire IP ou inspectez les requêtes dans Burp Suite :

### 1. Route via le proxy Burp Suite
```bash
wafw00f https://example.com -p http://127.0.0.1:8080
```
### 2. Acheminer via le proxy Tor SOCKS5 local
```bash
wafw00f https://example.com -p socks5://127.0.0.1:9050
```
---

## Formats d'exportation (JSON, CSV, Texte)

Exportez les résultats pour des rapports automatisés ou des outils de reconnaissance en aval :

### 1. Exporter au format JSON
```bash
wafw00f https://example.com -o results.json --format json
```
### 2. Exporter vers une feuille de calcul CSV
```bash
wafw00f -i targets.txt -o results.csv --format csv
```
---

## Liste des signatures WAF prises en charge (`-l`)

Afficher plus de 150 produits WAF détectables :
```bash
wafw00f -l
```
Les principaux produits pris en charge incluent :
- **Fournisseurs cloud :** Cloudflare, AWS WAF, Akamai, Microsoft Azure Web Application Firewall, Google Cloud Armor.
- **Appliances d'entreprise :** F5 BIG-IP ASM, Imperva SecureSphere, Barracuda, Fortinet FortiWeb, Citrix NetScaler.
- **Open Source/Logiciel :** ModSecurity, NAXSI, Shadow Daemon, OpenResty.

---

## Intégration du Bug Bounty et des tests d'intrusion

Intégrez wafw00f dans un pipeline d'attaque Web automatisé :
```bash
#!/usr/bin/env bash
TARGET="https://app.target.corp"

echo "[*] Detecting WAF for $TARGET..."
WAF=$(wafw00f "$TARGET" -o - --format json | jq -r '.[0].detected')

if [ "$WAF" == "true" ]; then
    WAF_NAME=$(wafw00f "$TARGET" -o - --format json | jq -r '.[0].firewall')
    echo "[!] Active WAF detected: $WAF_NAME"
    echo "[+] Adjusting sqlmap to use vendor-specific tamper scripts..."
    if [[ "$WAF_NAME" =~ "Cloudflare" ]]; then
        sqlmap -u "$TARGET/item?id=1" --tamper=between,randomcase,space2comment
    fi
else
    echo "[+] No active WAF detected. Proceeding with standard scanner."
    sqlmap -u "$TARGET/item?id=1" --batch
fi
```