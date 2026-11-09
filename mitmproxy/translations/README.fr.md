<!-- language-home-navigation -->
[🏠 Page d'accueil Française](../../README.fr.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🕶️ mitmproxy - Aide-mémoire complet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ███╗   ███╗██╗████████╗███╗   ███╗██████╗ ██████╗  ██████╗ ██╗  ██╗██╗   ██╗
  ████╗ ████║██║╚══██╔══╝████╗ ████║██╔══██╗██╔══██╗██╔═══██╗╚██╗██╔╝╚██╗ ██╔╝
  ██╔████╔██║██║   ██║   ██╔████╔██║██████╔╝██████╔╝██║   ██║ ╚███╔╝  ╚████╔╝ 
  ██║╚██╔╝██║██║   ██║   ██║╚██╔╝██║██╔═══╝ ██╔══██╗██║   ██║ ██╔██╗   ╚██╔╝  
  ██║ ╚═╝ ██║██║   ██║   ██║ ╚═╝ ██║██║     ██║  ██║╚██████╔╝██╔╝ ██╗   ██║   
  ╚═╝     ╚═╝╚═╝   ╚═╝   ╚═╝     ╚═╝╚═╝     ╚═╝  ╚═╝ ╚═════╝ ╚═╝  ╚═╝   ╚═╝   
       Interactive, Scriptable Terminal HTTP/HTTPS Proxy & Traffic Engine
```
<p align="center">
  <img src="https://img.shields.io/badge/mitmproxy-Terminal_Proxy-blue?style=for-the-badge" alt="mitmproxy">
  <img src="https://img.shields.io/badge/Author-mitmproxy_Project-orange?style=for-the-badge" alt="mitmproxy">
  <img src="https://img.shields.io/badge/Category-Network_&_Web_Analysis-green?style=for-the-badge" alt="Analyse du réseau">
  <img src="https://img.shields.io/badge/License-MIT-purple?style=for-the-badge" alt="MIT">
</p>

<p align="center">
  <b>🕶️ Un proxy HTTP d'interception interactif, compatible SSL/TLS pour les testeurs d'intrusion, les chercheurs en sécurité et les ingénieurs logiciels avec une interface de console riche et une API de script Python</b>
</p>

---

## 📋 Table des matières

- [Aperçu et les trois binaires](#-overview--les-trois-binaires)
- [Installation et configuration du certificat](#-installation--certificate-setup)
  - [Installation sous Linux / macOS](#installation-sur-linux--macos)
  - [Installation du certificat CA pour l'interception SSL/TLS](#installing-ca-certificate-for-ssltls-interception)
- [Modes de fonctionnement : Proxy régulier, transparent et inversé](#-operating-modes-regular-transparent--reverse-proxy)
-[Navigation TUI interactive (`mitmproxy`)](#-interactive-tui-navigation-mitmproxy)
  - [Raccourcis clavier](#keyboard-shortcuts)
  - [Filtrer et intercepter les flux](#filtering--intercepting-flows)
-[Automatisation non interactive (`mitmdump`)](#-non-interactive-automation-mitmdump)
  - [Enregistrement et relecture du trafic](#recording--replay-traffic)
- [Scripts et modules complémentaires Python](#-python-scripting--modules complémentaires)
  - [Script de manipulation d'en-tête automatisé](#automated-header-manipulation-script)
  -[Live Response Body Tampering](#live-response-body-falsification)
- [Tableau de référence rapide](#-quick-reference-table)
- [Considérations défensives et détection](#-considérations-defensives--détection)
- [Mentions légales](#-legal-disclaimer)

---

## 🎯 Aperçu et les trois binaires

Contrairement aux proxys de bureau graphiques volumineux, **mitmproxy** offre une interception HTTP/1, HTTP/2 et WebSockets ultra-rapide, scriptable et pleine fidélité directement dans votre terminal ou votre pipeline d'automatisation.

Il est livré avec trois binaires de point d'entrée :
1. **`mitmproxy` :** L'interface utilisateur de terminal (TUI) interactive basée sur ncurses.
2. **`mitmdump` :** La version en ligne de commande non interactive (l'équivalent `tcpdump` pour HTTP).
3. **`mitmweb` :** Une interface Web graphique légère basée sur un navigateur.

---

## 📥 Installation et configuration du certificat

### Installation sur Linux / macOS
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y mitmproxy

# Arch Linux
sudo pacman -S mitmproxy

# Python pip / pipx
pipx install mitmproxy
```
### Installation du certificat CA pour l'interception SSL/TLS
Pour intercepter le trafic HTTPS, les cibles doivent faire confiance à l'autorité de certification dynamique de mitmproxy :
1. Démarrez mitmproxy une fois : `mitmproxy` (crée des certificats dans `~/.mitmproxy/`).
2. Sur le périphérique client/navigateur, configurez le proxy `127.0.0.1:8080`.
3. Accédez à `http://mitm.it` et téléchargez/installez le certificat pour Android, iOS, Windows ou Linux.

---

## ⚙️ Modes de fonctionnement : proxy régulier, transparent et inversé

### Mode proxy régulier (par défaut)
Écoute du proxy de transfert standard sur le port 8080 :
```bash
mitmproxy -p 8080
```
### Mode proxy inverse
Relaye le trafic entrant vers un serveur cible spécifique (idéal pour les tests d'API) :
```bash
mitmproxy --mode reverse:https://api.target.com -p 8080
```
### Mode transparent
Intercepte le trafic acheminé via iptables sans configurer les paramètres de proxy client :
```bash
mitmproxy --mode transparent --showhost
```
---

## 🖥️ Navigation TUI interactive (`mitmproxy`)

### Raccourcis clavier
- `?` : Ouvrir la superposition d'aide.
- `Enter` : Inspecter le flux HTTP sélectionné (Demande/Réponse/Détail).
- `q` : Revenir en arrière/quitter.
- `d` : Supprimer le flux actuel.
- `r` : rejouer la requête sélectionnée au serveur.
- `e` : modifiez les en-têtes, le chemin ou le corps de la demande/réponse.
- `a` : Reprise du flux en pause (lors de l'interception).
- `A` : reprendre tous les flux suspendus.

### Filtrage et interception des flux
Appuyez sur `i` pour saisir une expression de filtre d'interception :
```text
~u /api/v1/ & ~m POST      # Intercept only POST requests to /api/v1/
~c 401 | ~c 403            # Filter flows by HTTP status code
~b "password"              # Filter flows where body contains "password"
```
---

## ⚡ Automatisation non interactive (`mitmdump`)

### Enregistrement et relecture du trafic
Capturez le trafic en direct directement dans un fichier de vidage binaire :
```bash
# Record all HTTP traffic to file
mitmdump -w traffic.dump

# Read and inspect captured dump
mitmdump -n -r traffic.dump

# Replay client requests from dump against target
mitmdump --client-replay traffic.dump
```
---

## 🐍 Scripts et modules complémentaires Python

mitmproxy propose une API Python native pour modifier le trafic par programmation :

### Script de manipulation d'en-tête automatisé (`add_headers.py`)
```python
from mitmproxy import http

class AddHeader:
    def request(self, flow: http.HTTPFlow):
        # Inject custom authorization or testing header into every outbound request
        flow.request.headers["X-Security-Audit"] = "Authorized-Pentest"
        flow.request.headers["Authorization"] = "Bearer injected_token_xyz"

addons = [AddHeader()]
```
Courez avec :
```bash
mitmproxy -s add_headers.py
```
### Falsification du corps de réponse en direct (`tamper_response.py`)
```python
from mitmproxy import http

def response(flow: http.HTTPFlow):
    # Force admin privilege in returned JSON response
    if "api/user/me" in flow.request.url:
        flow.response.text = flow.response.text.replace('"isAdmin": false', '"isAdmin": true')
```
Courez avec :
```bash
mitmdump -s tamper_response.py
```
---

## ⚡ Tableau de référence rapide

| Objectif | Commande |
|-----------|---------|
| **Démarrer TUI interactif** | `mitmproxy -p 8080` |
| **Démarrer l'interface graphique Web** | `mitmweb -p 8080` |
| **Enregistreur CLI silencieux** | `mitmdump -p 8080` |
| **Cible du proxy inverse** | `mitmproxy --mode reverse:https://target.com` |
| **Enregistrer le trafic dans un fichier** | `mitmdump -w capture.mitm` |
| **Charger le script du module complémentaire Python**| `mitmproxy -s script.py` |
| **Ignorer les hôtes cibles** | `mitmproxy --ignore-hosts ".*apple\.com"` |

---

## 🛡️ Considérations défensives et détection

1. **Épinglage de certificat :** Les applications mobiles appliquant l'épinglage SSL rejettent le certificat CA de mitmproxy. Associez-le à Frida / Objection pour contourner les contrôles côté client.
2. **Détection de proxy dans la prise de contact TLS :** Les proxys introduisent des suites de chiffrement TLS personnalisées et des empreintes digitales de négociation ALPN que les WAF et les gestionnaires de robots peuvent détecter.

---

## ⚠️ Mentions légales

> [!ATTENTION]
> mitmproxy intercepte et décrypte les communications réseau sensibles. Il doit être strictement déployé sur des systèmes et des réseaux pour lesquels une autorisation explicite a été obtenue. Intercepter des communications sans consentement est illégal.