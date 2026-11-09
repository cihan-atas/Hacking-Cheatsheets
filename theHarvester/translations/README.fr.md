<!-- language-home-navigation -->
[🏠 Page d'accueil Française](../../README.fr.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# Aide-mémoire theHarvester

[theHarvester](https://github.com/laramies/theHarvester) est l'un des outils de renseignement open source (OSINT) les plus largement utilisés pour collecter des e-mails, des sous-domaines, des noms d'employés, des hôtes virtuels, des ports ouverts et des clés PGP d'employés provenant de diverses sources publiques (moteurs de recherche, serveurs de clés PGP, Shodan, Censys, Certspotter, etc.).

---

## Table des matières

- [Sources de données prises en charge](#supported-data-sources)
- [Installation et configuration de la clé API](#installation--api-key-configuration)
- [Reconnaissance de domaine de base](#basic-domain-reconnaissance)
-[Récolte multi-sources passive](#passive-multi-source-harvesting)
- [Vérification DNS active et forçage brutal](#active-dns-verification--brute-forcing)
- [Formats de sortie (HTML, XML, JSON)](#output-formats-html-xml-json)
- [Intégration du workflow Pentest](#pentest-workflow-integration)

---

## Sources de données prises en charge

theHarvester regroupe les renseignements provenant de dizaines de sources passives et actives :
- **Moteurs de recherche (gratuits / sans clé) :** Google, Bing, DuckDuckGo, Yahoo, Baidu.
- **Transparence du certificat :** CRT.sh, Certspotter.
- **Dépôts de codes et de menaces :** GitHub, AlienVault OTX, ThreatCrowd, Rapid7, VirusTotal.
- **API de reconnaissance et OSINT :** Shodan, Censys, Hunter.io, SecurityTrails, Intelx, FullContact.
- **Réseau / DNS :** Anubis, HackerTarget, DNSDumpster.

---

## Installation et configuration de la clé API

### 1.Installation
```bash
# Debian / Ubuntu / Kali Linux
sudo apt-get update && sudo apt-get install -y theharvester

# From GitHub Source (Latest Version)
git clone https://github.com/laramies/theHarvester.git
cd theHarvester
python3 -m pip install -r requirements/base.txt

# Test binary
theHarvester -h
```
### 2. Configurer les clés API (`api-keys.yaml`)
De nombreuses sources (Hunter, Shodan, Censys, Intelx, GitHub) nécessitent des clés API gratuites ou payantes.
Créez ou modifiez `api-keys.yaml` dans le chemin de configuration :
```yaml
apikeys:
  hunter:
    key: "YOUR_HUNTER_KEY"
  shodan:
    key: "YOUR_SHODAN_KEY"
  github:
    key: "YOUR_GITHUB_TOKEN"
  securityTrails:
    key: "YOUR_SECURITYTRAILS_KEY"
```
---

## Reconnaissance de domaine de base

### 1. Récoltez des e-mails et des sous-domaines à l'aide de moteurs gratuits
```bash
theHarvester -d example.com -l 500 -b duckduckgo,bing,crtsh
```
- `-d <domain>` : Domaine cible à reconnaître.
- `-l <limit>` : Nombre de résultats à interroger par moteur.
- `-b <sources>` : Liste de sources séparées par des virgules (ou `all`).

### 2. Interroger toutes les sources configurées
```bash
theHarvester -d example.com -l 500 -b all
```
---

## Récolte multi-sources passive

Interrogez des bases de données spécifiques de renseignements sur les menaces et de transparence des certificats sans envoyer de trafic vers la cible :

### 1. Transparence des certificats et informations sur les menaces
```bash
theHarvester -d example.com -b crtsh,certspotter,anubis,hackertarget,otx
```
### 2. Cibler des groupes sources spécifiques
```bash
# Query GitHub commits and issues for leaked emails and subdomains
theHarvester -d example.com -b github-code

# Query Shodan for discovered IP addresses and open ports
theHarvester -d example.com -b shodan
```
---

## Vérification DNS active et forçage brutal

### 1. Résoudre les sous-domaines découverts (`-r`)
Effectuez des recherches DNS actives pour vérifier si les sous-domaines collectés sont actuellement actifs :
```bash
theHarvester -d example.com -b crtsh,bing -r
```
### 2. DNS Brute Force (`-c`)
Effectuez un forçage brut de sous-domaines DNS basé sur une liste de mots directement dans theHarvester :
```bash
theHarvester -d example.com -b duckduckgo -c
```
### 3. Vérification de l'hôte virtuel (`-v`)
Vérifiez les hôtes virtuels via des requêtes HTTP :
```bash
theHarvester -d example.com -b duckduckgo -v
```
---

## Formats de sortie (HTML, XML, JSON)

Exportez les résultats pour un reporting automatisé ou une reconnaissance en aval :

### 1. Exporter vers HTML et XML
```bash
theHarvester -d example.com -b duckduckgo,crtsh -f /tmp/theharvester_results
```
Crée `/tmp/theharvester_results.html` et `/tmp/theharvester_results.xml`.

### 2. Analyser les sous-domaines découverts avec `jq`
Si vous exportez ou analysez JSON :
```bash
theHarvester -d example.com -b crtsh,duckduckgo -f recon_out
# Extract clean host list
cat recon_out.xml | grep -oP '(?<=<host>).*?(?=</host>)' | sort -u > live_hosts.txt
```
---

## Intégration du flux de travail Pentest

Enchaînez les résultats de theHarvester directement avec les scanners de vulnérabilités actifs :
```bash
# 1. Harvest passive subdomains
theHarvester -d target.corp -b crtsh,duckduckgo,bing -f target_recon

# 2. Extract unique domain names
grep -oP '(?<=<host>)[^<]+' target_recon.xml | sort -u > hosts.txt

# 3. Probe for live web services using httpx
cat hosts.txt | httpx -silent -status-code -title > live_web.txt

# 4. Scan live web services with Nuclei
nuclei -l live_web.txt -severity medium,high,critical
```