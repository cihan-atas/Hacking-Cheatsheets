<!-- language-home-navigation -->
[🏠 Page d'accueil Française](../../README.fr.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🪁 Kiterunner - Aide-mémoire complet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ██╗  ██╗██╗████████╗███████╗██████╗ ██╗   ██╗███╗   ██╗███╗   ██╗███████╗██████╗ 
  ██║ ██╔╝██║╚══██╔══╝██╔════╝██╔══██╗██║   ██║████╗  ██║████╗  ██║██╔════╝██╔══██╗
  █████╔╝ ██║   ██║   █████╗  ██████╔╝██║   ██║██╔██╗ ██║██╔██╗ ██║█████╗  ██████╔╝
  ██╔═██╗ ██║   ██║   ██╔══╝  ██╔══██╗██║   ██║██║╚██╗██║██║╚██╗██║██╔══╝  ██╔══██╗
  ██║  ██╗██║   ██║   ███████╗██║  ██║╚██████╔╝██║ ╚████║██║ ╚████║███████╗██║  ██║
  ╚═╝  ╚═╝╚═╝   ╚═╝   ╚══════╝╚═╝  ╚═╝ ╚═════╝ ╚═╝  ╚═══╝╚═╝  ╚═══╝╚══════╝╚═╝  ╚═╝
       Context-Aware API Endpoint & Route Discovery Engine by Assetnote
```
<p align="center">
  <img src="https://img.shields.io/badge/Kiterunner-API_Discovery-blue?style=for-the-badge" alt="Kiterunner">
  <img src="https://img.shields.io/badge/Author-Assetnote-orange?style=for-the-badge" alt="Assetnote">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-green?style=for-the-badge" alt="Sécurité Web et API">
  <img src="https://img.shields.io/badge/License-Apache_2.0-red?style=for-the-badge" alt="Licence Apache 2.0">
</p>

<p align="center">
  <b>🌐 Brute-forcer de routage d'API REST et GraphQL hautes performances conçu pour cartographier les architectures d'API modernes à l'aide d'ensembles de données contextuelles</b>
</p>

---

## 📋 Table des matières

- [Présentation et problème de découverte de l'API](#-overview--the-api-discovery-problem)
- [Comparaison d'outils : Kiterunner vs ffuf vs dirsearch](#-tool-comparison-kiterunner-vs-ffuf-vs-dirsearch)
- [Installation](#-installation)
- [Téléchargement des ensembles de données Assetnote Kite](#-downloading-assetnote-kite-datasets)
- [API d'analyse (`kr scan`)](#-scanning-apis-kr-scan)
- [Liste de mots standard Brute-Forcing (`kr brute`)](#-standard-wordlist-brute-forcing-kr-brute)
-[En-têtes, cookies et authentification](#-headers-cookies--authentification)
- [Filtrage et codes d'état] (#-filtering--statut-codes)
-[Relecture des itinéraires découverts (sortie `curl`)](#-replaying-discovered-routes-curl-output)
- [Tableau de référence rapide](#-quick-reference-table)
- [Considérations défensives et OPSEC](#-defensive-considérations--opsec)
- [Mentions légales](#-legal-disclaimer)

---

## 🎯 Présentation et problème de découverte d'API

Les outils traditionnels de découverte de contenu Web (`ffuf`, `dirsearch`, `gobuster`) utilisent des dictionnaires plats contenant un seul mot (par exemple, `admin`, `api`, `login`). Bien que cela fonctionne pour les hiérarchies de fichiers traditionnelles, **cela échoue avec les API REST et GraphQL modernes**.

Les API modernes utilisent des routes imbriquées à plusieurs niveaux telles que :
`/api/v2/organizations/{org_id}/users/export?format=json`

**Kiterunner**, créé par **Assetnote**, résout ce problème en compilant les définitions OpenAPI, Swagger, Postman et de schéma du monde réel dans des ensembles de données binaires compacts (`.kite`). Au lieu de deviner des mots aléatoires, Kiterunner reconstruit des structures d'appels d'API hiérarchiques entières avec les méthodes HTTP correctes (`GET`, `POST`, `PUT`, `DELETE`).

---

## ⚖️ Comparaison d'outils : Kiterunner vs ffuf vs dirsearch

| Fonctionnalité | Kiterunner (`kr`) | ffff | recherche |
|---------|---------|------|---------------|
| **Objectif principal** | REST, GraphQL, OpenAPI | Fuzzing général du Web | Annuaire Brute-Forcing |
| **Conscience de la hiérarchie des itinéraires**| ✅ Natif (fichiers `.kite`) | ❌ Liste de mots plate | ❌ Liste de mots plate |
| **Échange de verbe HTTP** | ✅ Tests GET, POST, PUT, DELETE| ❌ Configuration manuelle | ❌ Limité |
| **Analyse Swagger/OpenAPI** | ✅ Ingestion de schéma natif | ❌ Non | ❌ Non |
| **Vitesse et concurrence** | ⭐⭐⭐⭐⭐ (Go Moteur) | ⭐⭐⭐⭐⭐ (Go Moteur) | ⭐⭐⭐⭐ (Python) |

---

## 📥Installation
```bash
# Download precompiled binary from GitHub Releases
wget https://github.com/assetnote/kiterunner/releases/download/v1.0.2/kiterunner_1.0.2_linux_amd64.tar.gz
tar -xzvf kiterunner_1.0.2_linux_amd64.tar.gz
sudo mv kr /usr/local/bin/

# Verify installation
kr version
```
---

## 📦 Téléchargement des ensembles de données Assetnote Kite

Kiterunner s'appuie sur des ensembles de données d'itinéraire `.kite` prédéfinis créés à partir de millions d'API publiques analysées :
```bash
# Create wordlists directory
mkdir -p ~/.kiterunner && cd ~/.kiterunner

# Small dataset (~40MB, 2.5 million API routes - Recommended for fast scans)
wget https://wordlists-cdn.assetnote.io/data/kiterunner/routes-small.kite.tar.gz
tar -xzvf routes-small.kite.tar.gz

# Large dataset (~130MB, 8.5 million API routes - Exhaustive testing)
wget https://wordlists-cdn.assetnote.io/data/kiterunner/routes-large.kite.tar.gz
tar -xzvf routes-large.kite.tar.gz
```
---

## 🚀 API d'analyse (`kr scan`)

La commande `scan` utilise les jeux de données `.kite` pour sonder les cibles à l'aide de hiérarchies de routes contextuelles :
```bash
# Scan a single target using the small dataset
kr scan https://target.local/api -w ~/.kiterunner/routes-small.kite

# Scan multiple hosts from a file with 100 concurrent workers
kr scan targets.txt -w ~/.kiterunner/routes-small.kite -c 100 -j 50

# Scan with maximum verbosity
kr scan https://target.local -w ~/.kiterunner/routes-small.kite -v
```
---

## 🔨 Liste de mots standard Brute-Forcing (`kr brute`)

Si vous souhaitez utiliser les listes de mots standards `.txt` tout en profitant du moteur API multi-méthodes de Kiterunner :
```bash
# Brute force standard paths using a text dictionary
kr brute https://target.local -w /usr/share/wordlists/dirb/common.txt

# Brute force with custom API prefixes
kr brute https://target.local/api/v1 -w endpoints.txt
```
---

## 🔑 En-têtes, cookies et authentification

Les API nécessitent souvent des JWT, des jetons Bearer ou des en-têtes d'organisation personnalisés :
```bash
# Pass custom Authorization header
kr scan https://target.local/api -w routes-small.kite -H "Authorization: Bearer eyJhbGci..."

# Pass custom API key and Cookie
kr scan https://target.local -w routes-small.kite -H "X-API-Key: secret123" -H "Cookie: session=xyz"
```
---

## 🎯 Filtrage et codes d'état
```bash
# Exclude standard error codes (404, 403, 502)
kr scan https://target.local -w routes-small.kite -x 404,403,502

# Filter out responses by length / content size
kr scan https://target.local -w routes-small.kite --filter-length 0,324
```
---

## 🔄 Relecture des itinéraires découverts (sortie `curl`)

Kiterunner peut générer des itinéraires découverts sous forme de commandes `curl` exploitables, permettant une reproduction instantanée dans un terminal ou Burp Suite :
```bash
# Print reproducible curl command lines for all successful matches
kr scan https://target.local -w routes-small.kite -o json | jq -r '.[] | .curl'
```
---

## ⚡ Tableau de référence rapide

| Objectif | Commande |
|-----------|---------|
| **API Scan avec un petit ensemble de données** | `kr scan <URL> -w routes-small.kite` |
| **API Scan avec un grand ensemble de données** | `kr scan <URL> -w routes-large.kite` |
| **Analyser plusieurs hôtes** | `kr scan hosts.txt -w routes-small.kite -c 100` |
| **Liste de mots de texte Brute Force** | `kr brute <URL> -w wordlist.txt` |
| **Authentifier avec le jeton du porteur**| `kr scan <URL> -w routes-small.kite -H "Authorization: Bearer <TOKEN>"` |
| **Exclure les codes d'état** | `kr scan <URL> -w routes-small.kite -x 404,403` |
| **Sortie vers un fichier JSON** | `kr scan <URL> -w routes-small.kite -o json -oFile results.json` |

---

## 🛡️ Considérations défensives et OPSEC

1. **Volume massif de requêtes :** Le dictionnaire `routes-large.kite` génère des millions de requêtes. Assurez-vous que la limitation de débit (`-q 200`) est activée lors du test des environnements de production.
2. **Méthodes de changement d'état :** Kiterunner envoie des requêtes `POST`, `PUT` et `DELETE`. Sur les API de test non renforcées, cela peut modifier ou supprimer par inadvertance des enregistrements backend.

---

## ⚠️ Mentions légales

> [!ATTENTION]
> Kiterunner est conçu uniquement pour les tests de sécurité API autorisés et les engagements de bug bounty. Le fuzzing d’API non autorisées est strictement illégal.