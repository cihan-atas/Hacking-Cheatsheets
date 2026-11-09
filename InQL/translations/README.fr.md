<!-- language-home-navigation -->
[🏠 Page d'accueil Française](../../README.fr.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🕸️ InQL - Aide-mémoire complet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ██╗███╗   ██╗ ██████╗ ██╗     
  ██║████╗  ██║██╔═══██╗██║     
  ██║██╔██╗ ██║██║   ██║██║     
  ██║██║╚██╗██║██║▄▄ ██║██║     
  ██║██║ ╚████║╚██████╔╝███████╗
  ╚═╝╚═╝  ╚═══╝ ╚══▀▀═╝ ╚══════╝
      Comprehensive GraphQL Security Assessment & Introspection Engine
```
<p align="center">
  <img src="https://img.shields.io/badge/InQL-GraphQL_Security-blue?style=for-the-badge" alt="InQL">
  <img src="https://img.shields.io/badge/Author-Doyensec-orange?style=for-the-badge" alt="Doyensec">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-green?style=for-the-badge" alt="Sécurité Web">
  <img src="https://img.shields.io/badge/License-BSD--3--Clause-purple?style=for-the-badge" alt="BSD">
</p>

<p align="center">
  <b>🕸️ Cadre de sécurité CLI avancé et extension Burp Suite développés par Doyensec pour vider les schémas d'introspection GraphQL, générer des modèles de requête et auditer la sécurité de l'API GraphQL</b>
</p>

---

## 📋 Table des matières

- [Présentation et principes fondamentaux de la sécurité GraphQL](#-overview--graphql-security-fundamentals)
  - [Dangers des requêtes d'introspection](#introspection-query-hazards)
- [Installation](#-installation)
-[Introspection Dumping & Schema Generation](#-introspection-dumping--schema-génération)
  - [Requête d'introspection CLI](#cli-introspection-query)
  - [Dumping vers le langage de définition de schéma (SDL)](#dumping-to-schema-definition-langage-sdl)
  - [Génération de modèles de requête prêts à l'exécution](#génération de modèles de requête prêts à l'exécution)
-[Audit des mutations et des requêtes sensibles](#-auditing-mutations--sensitive-queries)
- [Détection des déni de service et des défauts de traitement par lots](#-detecting-denial-of-service--batching-flaws)
  - [Requête par lots (Array Batching)](#batch-querying-array-batching)
  - [Références circulaires et requêtes imbriquées](#circular-references--nested-queries)
- [Tableau de référence rapide](#-quick-reference-table)
- [Considérations défensives et durcissement](#-considérations-defensives--durcissement)
- [Mentions légales](#-legal-disclaimer)

---

## 🎯 Présentation et principes fondamentaux de la sécurité GraphQL

Contrairement aux API REST standard avec des URL discrètes (`/api/v1/users`), GraphQL expose un seul point de terminaison HTTP (généralement `/graphql`) où les clients demandent des formes de données personnalisées via des requêtes et des mutations.

### Risques liés aux requêtes d'introspection
GraphQL dispose d'un système d'introspection intégré (`__schema`, `__type`) conçu pour les outils de développement. Si laissé activé en production :
- Les attaquants peuvent interroger l'intégralité du schéma backend.
- Les fonctionnalités inédites, les mutations administratives internes (par exemple `deleteUser`, `changeRole`) et les champs privés sont complètement exposés.

Créé par **Doyensec**, **InQL** automatise l'extraction d'introspection, analyse les schémas, génère de la documentation et crée des modèles d'exploit prêts à l'emploi.

---

## 📥Installation
```bash
# Using Python pip (Recommended)
pip install inql

# Or clone from GitHub
git clone https://github.com/doyensec/inql.git
cd inql && pip install .

# Verify installation
inql -h
```
---

## 🚀 Dumping d'introspection et génération de schémas

### Requête d'introspection CLI
Interrogez un point de terminaison GraphQL distant, extrayez le schéma complet et enregistrez-le dans un dossier de sortie :
```bash
inql -t https://target.com/graphql -o /tmp/graphql_audit/
```
*Généré dans le répertoire de sortie :*
- `schema.json` : dump d'introspection JSON brut.
- `schema.graphql` : langage de définition de schéma (SDL) lisible par l'homme.
- `queries/` : modèles de requête générés automatiquement pour chaque requête disponible.
- `mutations/` : modèles de mutation générés automatiquement pour la modification des données.

### Analyse authentifiée et en-têtes personnalisés
Transmettez les cookies de session, les jetons d'autorisation ou les en-têtes personnalisés :
```bash
inql -t https://target.com/graphql \
  -H "Authorization: Bearer <TOKEN>" \
  -H "X-API-Key: secret123" \
  -o ./audit_report/
```
### Dumping vers le langage de définition de schéma (SDL)
Convertissez le schéma JSON brut existant en SDL :
```bash
inql -f schema.json --generate-sdl -o ./sdl_output/
```
---

## 💥 Audit des mutations et des requêtes sensibles

Examinez les fichiers de mutation générés automatiquement dans le dossier de sortie :
```text
mutations/
├── registerUser.graphql
├── resetPassword.graphql
├── updateProfile.graphql
└── adminPromoteUser.graphql
```
Envoyez des mutations à fort impact à l’aide de curl ou Burp Suite :
```bash
curl -X POST https://target.com/graphql \
  -H "Content-Type: application/json" \
  -d '{"query": "mutation { adminPromoteUser(userId: 10, role: \"SUPERADMIN\") { success } }"}'
```
---

## ⚡ Détection des déni de service et des défauts de traitement par lots

### Requête par lots (Array Batching)
Testez si le serveur traite des tableaux de requêtes dans une seule requête HTTP (contourne la limitation de débit) :
```json
[
  {"query": "query { getUser(id: 1) { email } }"},
  {"query": "query { getUser(id: 2) { email } }"},
  {"query": "query { getUser(id: 3) { email } }"}
]
```
### Références circulaires et requêtes imbriquées
Test des plantages de relations récursives :
```graphql
query {
  thread {
    messages {
      thread {
        messages {
          thread {
            messages {
              id
            }
          }
        }
      }
    }
  }
}
```
---

## ⚡ Tableau de référence rapide

| Objectif | Commande |
|-----------|---------|
| **Introspection de décharge** | `inql -t https://target.com/graphql -o ./out/` |
| **Introspection authentifiée**| `inql -t <URL> -H "Authorization: Bearer <TOKEN>" -o ./out/` |
| **Convertir le schéma JSON en SDL** | `inql -f schema.json --generate-sdl -o ./sdl/` |
| **Générer des documents HTML** | `inql -t <URL> --generate-html -o ./docs/` |

---

## 🛡️ Considérations défensives et durcissement

1. **Désactivez l'introspection en production :** Désactivez `introspection` dans la configuration de votre serveur GraphQL (Apollo, Yoga, GraphQL-Java).
2. **Appliquer l'analyse de la profondeur et des coûts des requêtes :** Implémentez des bibliothèques telles que `graphql-depth-limit` ou une analyse des coûts des requêtes pour supprimer les requêtes profondément imbriquées.
3. **Désactiver le traitement par lots :** Limitez ou désactivez l'exécution de requêtes par lots basées sur un tableau si cela n'est pas strictement nécessaire.

---

## ⚠️ Mentions légales

> [!ATTENTION]
> InQL est développé pour les évaluations de sécurité autorisées, les tests d'intrusion et l'audit de sécurité des API. L'extraction de schémas ou la modification de données sur des points de terminaison non autorisés est illégale.