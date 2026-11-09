<!-- language-home-navigation -->
[🏠 Page d'accueil Française](../../README.fr.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 📡 dnsx - Aide-mémoire complet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ██████╗ ███╗   ██╗███████╗██╗  ██╗
  ██╔══██╗████╗  ██║██╔════╝╚██╗██╔╝
  ██║  ██║██╔██╗ ██║███████╗ ╚███╔╝ 
  ██║  ██║██║╚██╗██║╚════██║ ██╔██╗ 
  ██████╔╝██║ ╚████║███████║██╔╝ ██╗
  ╚═════╝ ╚═╝  ╚═══╝╚══════╝╚═╝  ╚═╝
       Fast & Multi-Purpose DNS Toolkit by ProjectDiscovery
```
<p align="center">
  <img src="https://img.shields.io/badge/dnsx-DNS_Toolkit-blue?style=for-the-badge" alt="dnsx">
  <img src="https://img.shields.io/badge/Developer-ProjectDiscovery-orange?style=for-the-badge" alt="ProjectDiscovery">
  <img src="https://img.shields.io/badge/Category-Recon_&_Scanning-green?style=for-the-badge" alt="Reconnaissance et numérisation">
  <img src="https://img.shields.io/badge/License-MIT-red?style=for-the-badge" alt="Licence MIT">
</p>

<p align="center">
  <b>🌐 Résolution DNS hautes performances, filtrage des caractères génériques, recherches DNS inversées et moteur d'extraction multi-requêtes</b>
</p>

---

## 📋 Table des matières

- [Vue d'ensemble et architecture](#-overview--architecture)
- [Pourquoi DNSX ? (Gestion et vitesse des caractères génériques)](#-pourquoi-dnsx-gestion des caractères génériques--vitesse)
- [Installation](#-installation)
- [Sonde et résolution de sous-domaine de base](#-basic-subdomain-probing--resolution)
- [Filtrage DNS générique (`-wd`)](#-wildcard-dns-filtering---wd)
- [Interrogation de types d'enregistrement DNS spécifiques](#-querying-special-dns-record-types)
- [Recherches DNS inversées (`-ptr`)](#-reverse-dns-lookups---ptr)
-[Utilisation de résolveurs personnalisés et limitation de débit](#-using-custom-resolvers--rate-limiting)
- [Automation du pipeline (Subfinder + dnsx + httpx)](#-pipeline-automation-subfinder--dnsx--httpx)
- [Tableau de référence rapide](#-quick-reference-table)
- [Considérations défensives et OPSEC](#-defensive-considérations--opsec)
- [Mentions légales](#-legal-disclaimer)

---

## 🎯 Présentation et architecture

Conçu dans Go par **ProjectDiscovery**, **dnsx** est une boîte à outils DNS rapide et polyvalente conçue pour la reconnaissance, la chasse aux bugs et la découverte de réseaux.

Alors que des outils tels que `dig`, `nslookup` et `host` résolvent les enregistrements un par un, `dnsx` traite des milliers de requêtes de manière asynchrone, réessaye automatiquement les résolveurs défaillants, extrait les adresses IP de réponse propres et élimine le bruit générique qui pollue l'énumération des sous-domaines.

---

## ⚡Pourquoi dnsx ? (Gestion et vitesse des caractères génériques)

Lors de la reconnaissance des bug bounty, les outils passifs (`subfinder`, `amass`) renvoient souvent des milliers de sous-domaines morts ou génériques. Tester les points de terminaison HTTP sur des entrées DNS mortes gaspille énormément de bande passante.

`dnsx` résout ce problème en :
1. ** Filtrage des caractères génériques (`-wd`) :** Détecte si `*.domain.com` se résout en une adresse IP fourre-tout et filtre les sous-domaines fantômes.
2. **Vitesse et concurrence :** Résout plus de 10 000 domaines par minute dans des pools de résolution publics et privés.
3. **Tuyauterie de sortie propre :** S'alimente directement dans le `httpx` ou le `nmap`.

---

## 📥Installation
```bash
# Go Install (Recommended)
go install -v github.com/projectdiscovery/dnsx/cmd/dnsx@latest

# Kali Linux & Debian
sudo apt update && sudo apt install -y dnsx

# MacOS (Homebrew)
brew install dnsx

# Verify installation
dnsx -version
```
---

## 🚀 Sondage et résolution de base des sous-domaines
```bash
# Resolve a single domain
echo "target.com" | dnsx

# Resolve a list of subdomains from file
dnsx -l subdomains.txt

# Display domain and its corresponding A record IP
dnsx -l subdomains.txt -a -resp

# Output only resolved IP addresses (ready for Nmap)
dnsx -l subdomains.txt -resp-only
```
---

## 🚫 Filtrage DNS générique (`-wd`)

Les caractères génériques renvoient une adresse IP valide pour les sous-domaines inexistants (`random123xyz.target.com`), empoisonnant ainsi les ensembles de données de reconnaissance.
```bash
# Filter wildcard subdomains and print only genuine hosts
dnsx -l subdomains.txt -wd target.com -o valid_hosts.txt

# Filter wildcards with custom threshold
dnsx -l subdomains.txt -wd target.com -wt 3
```
---

## 📋 Interrogation de types d'enregistrement DNS spécifiques

Interrogez des types d’enregistrements spécifiques sur des milliers de domaines :
```bash
# Query CNAME records (Crucial for Subdomain Takeover detection)
dnsx -l subdomains.txt -cname -resp

# Query Mail Exchanger (MX) records
dnsx -l domains.txt -mx -resp

# Query TXT records (SPF, DKIM, verification tokens)
dnsx -l domains.txt -txt -resp

# Query All standard records (A, AAAA, CNAME, NS, TXT, MX, SOA)
dnsx -l domains.txt -recon
```
---

## 🔄 Recherches DNS inversées (`-ptr`)

Découvrez l'infrastructure active et les hôtes virtuels en effectuant des recherches PTR sur une plage IP :
```bash
# Reverse resolve a CIDR IP range to hostnames
dnsx -ptr -l ip_ranges.txt -resp

# Reverse resolve a single subnet
echo "192.168.1.0/24" | dnsx -ptr -resp
```
---

## ⚙️ Utilisation de résolveurs personnalisés et de limitation de débit

Évitez d'être limité en débit par votre FAI ou Google DNS en utilisant un pool de résolution organisé à grande vitesse :
```bash
# Use a custom trusted resolver list
dnsx -l subdomains.txt -r resolvers.txt

# Set thread concurrency and query rate limit (queries per second)
dnsx -l subdomains.txt -t 100 -rl 300
```
---

## ⛓️ Automatisation du pipeline (Subfinder + dnsx + httpx)

Le workflow de reconnaissance ultime pour les chasseurs de bugs et les testeurs d'intrusion :
```bash
# Discover subdomains -> Validate via dnsx -> Probe live HTTP web services
subfinder -d target.com -silent | \
  dnsx -silent -wd target.com | \
  httpx -silent -status-code -title -tech-detect
```
---

## ⚡ Tableau de référence rapide

| Objectif | Commande |
|-----------|---------|
| **Résoudre les sous-domaines** | `dnsx -l subdomains.txt` |
| **Extraire les adresses IP uniquement** | `dnsx -l subdomains.txt -resp-only` |
| **Filtrer les domaines génériques** | `dnsx -l subdomains.txt -wd <DOMAIN>` |
| **Extraire CNAME (prise de contrôle)** | `dnsx -l subdomains.txt -cname -resp` |
| **Interroger tous les enregistrements (Recon)** | `dnsx -l domains.txt -recon` |
| **Recherche DNS inversée (PTR)** | `dnsx -l ips.txt -ptr -resp` |
| **Résolveurs et threads personnalisés**| `dnsx -l list.txt -r resolvers.txt -t 150` |
| **Sortie JSON** | `dnsx -l list.txt -json -o results.json` |

---

## 🛡️ Considérations défensives et OPSEC

1. **Inondation DNS :** Le lancement de dizaines de milliers de requêtes DNS via les serveurs DNS internes de l'entreprise augmentera la charge du processeur et déclenchera des alertes SIEM pour le tunneling DNS/DDoS.
2. **Empoisonnement des résolveurs :** Utilisez toujours des listes de résolveurs vérifiés et fiables pour empêcher les résolveurs malveillants d'intercepter les requêtes.

---

## ⚠️ Mentions légales

> [!ATTENTION]
> Cette aide-mémoire est destinée uniquement aux tests d'intrusion autorisés et aux engagements de bug bounty. Il est strictement interdit de mener des enquêtes DNS à grand volume contre des infrastructures non autorisées.