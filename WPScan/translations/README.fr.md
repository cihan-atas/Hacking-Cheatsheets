<!-- language-home-navigation -->
[🏠 Page d'accueil Française](../../README.fr.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🛡️ WPScan - Aide-mémoire complet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ██╗    ██╗██████╗ ███████╗ ██████╗ █████╗ ███╗   ██╗
  ██║    ██║██╔══██╗██╔════╝██╔════╝██╔══██╗████╗  ██║
  ██║ █╗ ██║██████╔╝███████╗██║     ███████║██╔██╗ ██║
  ██║███╗██║██╔═══╝ ╚════██║██║     ██╔══██║██║╚██╗██║
  ╚███╔███╔╝██║     ███████║╚██████╗██║  ██║██║ ╚████║
   ╚══╝╚══╝ ╚═╝     ╚══════╝ ╚═════╝╚═╝  ╚═╝╚═╝  ╚═══╝
       The WordPress Security & Vulnerability Scanner
```
<p align="center">
  <img src="https://img.shields.io/badge/WPScan-WordPress_Security-blue?style=for-the-badge" alt="WPScan">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-orange?style=for-the-badge" alt="Sécurité Web">
  <img src="https://img.shields.io/badge/Database-WPVDB_API-green?style=for-the-badge" alt="API WPVDB">
  <img src="https://img.shields.io/badge/License-GPLv3-red?style=for-the-badge" alt="Licence GPLv3">
</p>

<p align="center">
  <b>🌐 Scanner de vulnérabilités de boîte noire conçu pour détecter les vulnérabilités du cœur, des plugins, des thèmes et des utilisateurs de WordPress</b>
</p>

---

## 📋 Table des matières

- [Vue d'ensemble et architecture](#-overview--architecture)
- [Installation](#-installation)
- [Intégration de jetons API (WPVDB)](#-api-token-integration-wpvdb)
- [Analyse de base et énumération des cibles](#-basic-scan--target-enumeration)
- [Répartition des indicateurs d'énumération (`-e`)](#-enumeration-flags-breakdown--e)
  - [Plugins vulnérables (`-e vp`)](#vulnerable-plugins--e-vp)
  -[Tous les plugins (`-e ap`)](#all-plugins--e-ap)
  -[Thèmes (`-e vt`, `-e at`)](#themes--e-vt--e-at)
  - [Noms d'utilisateur (`-e u`)](#usernames--e-u)
  - [Sauvegardes sensibles (`-e cb`, `-e dbe`)](#sensitive-backups--e-cb--e-dbe)
- [Modes de détection (`--plugins-detection`)](#-modes-de-détection---détection-plugins)
- [Mot de passe Brute-Forcing (`wp-login.php` et XML-RPC)](#-password-brute-forcing-wp-loginphp--xml-rpc)
- [Furtif et évasion WAF](#-stealth--waf-evasion)
- [Tableau de référence rapide](#-quick-reference-table)
- [Considérations défensives et durcissement](#-considérations-defensives--durcissement)
- [Mentions légales](#-legal-disclaimer)

---

## 🎯 Présentation et architecture

Avec WordPress qui alimente plus de 40 % des 10 millions de sites Web les plus performants, il s’agit du système de gestion de contenu (CMS) le plus ciblé au monde.

Écrit en Ruby, **WPScan** est le scanner de vulnérabilités de boîte noire standard de l'industrie pour les installations WordPress. Il catalogue les versions principales actives, énumère les plugins et les thèmes installés, extrait les noms d'utilisateur administratifs enregistrés et les met en corrélation avec la **base de données de vulnérabilités WPScan (WPVDB)** contenant des milliers de CVE connus.

---

## 📥Installation
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y wpscan

# Arch Linux
sudo pacman -S wpscan

# Ruby Gem (Any platform)
gem install wpscan

# Docker Container
docker run -it --rm wpscan/wpscan --url https://target.local
```
---

## 🔑 Intégration de jetons API (WPVDB)

Pour afficher les vulnérabilités connues et les scores CVE, WPScan nécessite un jeton API gratuit de [wpscan.com](https://wpscan.com/) :
```bash
# Provide API token via CLI
wpscan --url https://target.local --api-token YOUR_API_TOKEN

# Or export to environment variable
export WPSCAN_API_TOKEN="YOUR_API_TOKEN"
```
---

## 🚀 Analyse de base et énumération des cibles
```bash
# Quick non-intrusive scan
wpscan --url https://target.local

# Update the local database before scanning
wpscan --update
```
---

## 🔍 Répartition des indicateurs d'énumération (`-e`)

WPScan permet une énumération de cibles modulaire à l'aide de l'indicateur `--enumerate` (ou `-e`) :

### Plugins vulnérables (`-e vp`)
Analyse les plugins présentant des vulnérabilités de sécurité connues dans la base de données WPVDB :
```bash
wpscan --url https://target.local -e vp --api-token YOUR_TOKEN
```
### Tous les plugins (`-e ap`)
Sonde des milliers de plugins connus via la force brute du dictionnaire :
```bash
wpscan --url https://target.local -e ap --plugins-detection aggressive
```
### Thèmes (`-e vt`, `-e at`)
```bash
# Enumerate vulnerable themes
wpscan --url https://target.local -e vt

# Enumerate all installed themes
wpscan --url https://target.local -e at
```
### Noms d'utilisateur (`-e u`)
Extrait les noms d'utilisateur des auteurs enregistrés via les points de terminaison de l'API REST (`/wp-json/wp/v2/users`), les archives d'auteur (`/?author=1`) et les messages d'erreur de connexion :
```bash
wpscan --url https://target.local -e u1-10
```
### Sauvegardes sensibles (`-e cb`, `-e dbe`)
Recherche les `wp-config.php.bak` exposés, les sauvegardes de base de données et les archives de sauvegarde :
```bash
wpscan --url https://target.local -e cb,dbe
```
---

## ⚙️ Modes de détection (`--plugins-detection`)

Contrôle la profondeur avec laquelle WPScan recherche les plugins :
```bash
# Passive (Fast, inspects HTML source, script tags, style links)
wpscan --url https://target.local --plugins-detection passive

# Aggressive (Sends HTTP requests for thousands of plugin directories)
wpscan --url https://target.local --plugins-detection aggressive

# Mixed (Recommended default - passive followed by targeted active requests)
wpscan --url https://target.local --plugins-detection mixed
```
---

## 🔓 Forçage brutal des mots de passe (`wp-login.php` et XML-RPC)

Une fois les noms d’utilisateur valides énumérés, lancez le forçage brutal des informations d’identification ciblées :
```bash
# Brute force login using enumerated username list
wpscan --url https://target.local -U users.txt -P /usr/share/wordlists/rockyou.txt

# Password spray a single password across all users
wpscan --url https://target.local -U users.txt -p 'Winter2026!'

# Multi-threaded password attack
wpscan --url https://target.local -U admin -P passwords.txt -t 20
```
> [!NOTE]
> Si `xmlrpc.php` est activé sur le serveur, WPScan exploite automatiquement les méthodes multiappels XML-RPC pour tester des centaines de combinaisons de mots de passe par requête HTTP unique !

---

## 🥷 Furtivité et évasion WAF
```bash
# Randomize User-Agent header to evade simple bot signatures
wpscan --url https://target.local --random-user-agent

# Throttle request rate (milliseconds between requests) to bypass rate limits
wpscan --url https://target.local --throttle 1000

# Route through SOCKS proxy / Tor
wpscan --url https://target.local --proxy socks5://127.0.0.1:9050

# Ignore SSL/TLS certificate warnings
wpscan --url https://target.local --disable-tls-checks
```
---

## ⚡ Tableau de référence rapide

| Objectif | Commande |
|-----------|---------|
| **Analyse standard** | `wpscan --url <URL> --api-token <TOKEN>` |
| **Énumérer les plugins vulnérables** | `wpscan --url <URL> -e vp --api-token <TOKEN>` |
| **Énumérer tous les plugins** | `wpscan --url <URL> -e ap --plugins-detection aggressive` |
| **Énumérer les utilisateurs** | `wpscan --url <URL> -e u` |
| **Énumérer les sauvegardes et les configurations** | `wpscan --url <URL> -e cb,dbe` |
| **Mot de passe Brute Force** | `wpscan --url <URL> -U <USERS> -P <PASSWORDS>` |
| **Agent utilisateur aléatoire et furtif** | `wpscan --url <URL> --random-user-agent --stealthy` |
| **Exporter vers un fichier JSON** | `wpscan --url <URL> -f json -o wpscan_report.json` |

---

## 🛡️ Considérations défensives et durcissement

1. **Désactivez XML-RPC :** Bloquez l'accès à `xmlrpc.php` via la configuration `.htaccess` ou Nginx pour neutraliser les attaques par force brute multiappels à grande vitesse.
2. **Restreindre le point de terminaison des utilisateurs de l'API REST :** Désactivez l'accès public à `/wp-json/wp/v2/users` pour empêcher une énumération triviale des auteurs.
3. **Corrigez régulièrement les plugins :** Plus de 90 % des vulnérabilités de WordPress résident dans des plugins et des thèmes tiers plutôt que dans le cœur de WordPress.

---

## ⚠️ Mentions légales

> [!ATTENTION]
> Cette aide-mémoire est destinée uniquement aux tests d'intrusion autorisés, aux évaluations de vulnérabilité et à l'audit des applications Web. Le forçage brutal de mots de passe ou l'analyse de systèmes sans autorisation explicite sont illégaux.