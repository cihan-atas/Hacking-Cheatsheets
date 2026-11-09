<!-- language-home-navigation -->
[🏠 Page d'accueil Française](../../README.fr.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🌐 WhatWeb - Aide-mémoire complet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ██╗    ██╗██╗  ██╗ █████╗ ████████╗██╗    ██╗███████╗██████╗ 
  ██║    ██║██║  ██║██╔══██╗╚══██╔══╝██║    ██║██╔════╝██╔══██╗
  ██║ █╗ ██║███████║███████║   ██║   ██║ █╗ ██║█████╗  ██████╔╝
  ██║███╗██║██╔══██║██╔══██║   ██║   ██║███╗██║██╔══╝  ██╔══██╗
  ╚███╔███╔╝██║  ██║██║  ██║   ██║   ╚███╔███╔╝███████╗██████╔╝
   ╚══╝╚══╝ ╚═╝  ╚═╝╚═╝  ╚═╝   ╚═╝    ╚══╝╚══╝ ╚══════╝╚═════╝ 
       Next Generation Web Scanner & Technology Fingerprinter
```
<p align="center">
  <img src="https://img.shields.io/badge/WhatWeb-Web_Fingerprinter-blue?style=for-the-badge" alt="WhatWeb">
  <img src="https://img.shields.io/badge/Author-Andrew_Horton-orange?style=for-the-badge" alt="urbanadventurer">
  <img src="https://img.shields.io/badge/Category-Recon_&_Scanning-green?style=for-the-badge" alt="Reconnaissance et numérisation">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="Licence GPLv2">
</p>

<p align="center">
  <b>🌐 Scanner Web de nouvelle génération conçu pour identifier les systèmes de gestion de contenu (CMS), les plateformes de blogs, les bibliothèques JavaScript et les technologies de serveur</b>
</p>

---

## 📋 Table des matières

-[Vue d'ensemble et empreintes digitales technologiques](#-overview--technology-fingerprinting)
- [Installation](#-installation)
- [Empreintes digitales de base du site Web](#-basic-website-fingerprinting)
- [Niveaux d'agression expliqués (`-a`)](#-niveaux-d'agression-expliqués--a)
  - [Mode passif furtif (`-a 1`)](#stealthy-passive-mode--a-1)
  - [Mode agressif (`-a 3`)](#aggressive-mode--a-3)
  - [Mode lourd/brute (`-a 4`)](#heavy--brute-mode--a-4)
- [Analyse des sous-réseaux et des fichiers cibles](#-scanning-subnets--target-files)
- [Système de plugins et audit sélectif](#-plugin-system--selective-auditing)
- [Journalisation des sorties et exportation de rapports](#-output-logging--report-exporting)
- [Tableau de référence rapide](#-quick-reference-table)
- [Considérations défensives et OPSEC](#-defensive-considérations--opsec)
- [Mentions légales](#-legal-disclaimer)

---

## 🎯 Présentation et empreintes technologiques

Avant de lancer des exploits ou des fuzzers par force brute contre une application Web, il est impératif d’identifier sa pile technologique exacte.

Créé par **Andrew Horton (urbanadventurer)**, **WhatWeb** reconnaît plus de 1 800 technologies Web distinctes :
- **Systèmes de gestion de contenu (CMS) :** WordPress, Drupal, Joomla, Ghost, Magento.
- **Serveurs Web et proxys :** Apache, Nginx, Microsoft-IIS, Cloudflare, Envoy.
- **Frameworks backend :** Laravel, Django, Ruby on Rails, ASP.NET, Spring Boot.
- **Bibliothèques frontales :** React, Vue.js, Angular, jQuery, Bootstrap.
- **Périphériques intégrés :** Webcams, routeurs, imprimantes, interfaces SCADA.

---

## 📥Installation
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y whatweb

# Arch Linux
sudo pacman -S whatweb

# macOS (Homebrew)
brew install whatweb
```
---

## 🚀 Prise d'empreintes digitales de base du site Web
```bash
# Scan a single domain
whatweb https://target.local

# Brief one-line output format
whatweb --color=never -q https://target.local

# Verbose detailed report showing evidence matches
whatweb -v https://target.local
```
---

## ⚡ Niveaux d'agression expliqués (`-a`)

WhatWeb équilibre la furtivité et la profondeur en utilisant quatre niveaux d'agression :

### Mode passif furtif (`-a 1`)
Envoie une seule requête HTTP GET à l'URL fournie et analyse les en-têtes de réponse, les cookies et les balises HTML renvoyés sans demander de pages supplémentaires :
```bash
whatweb -a 1 https://target.local
```
### Mode agressif (`-a 3`)
Recommandé pour les tests d'intrusion. Suit les redirections et demande les fichiers de plug-in courants (par exemple, `wp-login.php`, `robots.txt`, hachages de favicon) :
```bash
whatweb -a 3 https://target.local
```
### Mode lourd/brute (`-a 4`)
Tente des milliers de suppositions de plugins et d'URL d'empreintes digitales connues :
```bash
whatweb -a 4 https://target.local
```
---

## 🌐 Analyse des sous-réseaux et des fichiers cibles
```bash
# Scan an entire CIDR network block
whatweb 192.168.1.0/24

# Scan multiple URLs listed in a text file
whatweb -i targets.txt

# Pipe live web endpoints from httpx into WhatWeb
cat live_hosts.txt | whatweb --no-errors
```
---

## 🧩 Système de plugins et audit sélectif

WhatWeb propose une architecture de plugin riche :
```bash
# List all 1,800+ available plugins
whatweb -l

# Search for specific plugins (e.g., WordPress or Apache)
whatweb --grep wordpress

# Run ONLY a specific plugin against a target
whatweb --plugins="apache,php,wordpress" https://target.local
```
---

## 📊 Journalisation des sorties et exportation de rapports
```bash
# Export results to clean JSON format
whatweb https://target.local --log-json results.json

# Export to XML format
whatweb https://target.local --log-xml results.xml

# Export to compact CSV format
whatweb -i targets.txt --log-brief results.txt
```
---

## ⚡ Tableau de référence rapide

| Objectif | Commande |
|-----------|---------|
| **Analyse standard** | `whatweb <URL>` |
| **Rapport détaillé détaillé** | `whatweb -v <URL>` |
| **Analyse passive furtive** | `whatweb -a 1 <URL>` |
| **Analyse approfondie agressive** | `whatweb -a 3 <URL>` |
| **Analyser le sous-réseau (CIDR)** | `whatweb <SUBNET>/24` |
| **Analyser les cibles à partir d'un fichier** | `whatweb -i targets.txt` |
| **Exporter vers JSON** | `whatweb <URL> --log-json report.json` |
| **Filtrer par plugins spécifiques**| `whatweb --plugins="wordpress,nginx" <URL>` |

---

## 🛡️ Considérations défensives et OPSEC

1. **Masquage des en-têtes de serveur :** Supprimez ou randomisez les en-têtes de réponse HTTP `Server` et `X-Powered-By` dans Apache/Nginx pour vaincre les empreintes digitales de bannière passive.
2. **Masquage de hachage Favicon :** WhatWeb utilise les hachages MD5 de `/favicon.ico` pour identifier les frameworks Web. Personnalisez les favicons du framework par défaut.

---

## ⚠️ Mentions légales

> [!ATTENTION]
> WhatWeb est destiné uniquement à la reconnaissance autorisée, à l'audit de sécurité et à l'inventaire du système. L'analyse de réseaux ou d'applications Web non autorisés sans autorisation peut enfreindre les lois applicables.