<!-- language-home-navigation -->
[🏠 Page d'accueil Française](../../README.fr.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# Aide-mémoire Name-That-Hash (nième)

[Name-That-Hash](https://github.com/HashPals/Name-That-Hash) (`nth`) est un moteur d'identification de hachage moderne et ultra-rapide qui identifie avec précision les hachages inconnus, les hachages intégrés dans du texte ou des fichiers, et génère les modes directs Hashcat (`-m`) et John the Ripper (`--format`). Il décode automatiquement les encodages imbriqués comme Base64 et Hex.

---

## Table des matières

- [Principales fonctionnalités](#key-features)
- [Installation](#installation)
- [Identification du hachage CLI](#cli-hash-identification)
- [Traitement multi-hash et fichiers](#multi-hash--file-processing)
- [Sortie au format Hashcat et John l'Éventreur](#hashcat--john-the-ripper-format-output)
- [Filtres de décodage automatique et de popularité] (#auto-decode--popularity-filters)
- [Intégration avec Cracking Pipelines](#integration-with-cracking-pipelines)

---

## Principales fonctionnalités

- **Plus de 300 types de hachage :** Couvre les algorithmes modernes, les formats Active Directory, les hachages de bases de données, les signatures CMS et les chiffrements ésotériques.
- **Hashcat & John Flag Mapping :** Affiche immédiatement les valeurs `-m <mode>` et `--format=<format>`.
- **Décodage automatique :** supprime l'encodage Base64, Hex et URL avant de calculer le type de hachage.
- **Recherche de sous-chaîne/texte :** Recherche les hachages enfouis dans les journaux, les vidages de mémoire ou les fichiers de configuration.
- **Tri par popularité :** Classe les résultats selon la probabilité d'utilisation réelle.

---

##Installation
```bash
# Via pipx (recommended for isolated CLI tools)
pipx install name-that-hash

# Via standard Python pip
python3 -m pip install --user name-that-hash

# Kali Linux / Debian repository
sudo apt-get update && sudo apt-get install -y python3-name-that-hash

# Verify installation
nth --version
```
---

## Identification de hachage CLI

### 1. Identifiez un seul hachage
```bash
nth -t "5d41402abc4b2a76b9719d911017c592"
```
### 2. Identifiez directement les modes de format Hashcat et John
Afficher uniquement les types de hachage prenant en charge les modes de craquage Hashcat ou John :
```bash
nth -t "b8c37e33defde51cf91e1e03e51657da" --hashcat --john
```
### 3. Afficher des informations détaillées (description, vitesse, exemples)
```bash
nth -t "$6$rounds=5000$usesomesalt$..." -v
```
---

## Traitement multi-hachage et fichiers

### 1. Identifiez les hachages à l'intérieur d'un fichier (ligne par ligne)
```bash
nth -f /path/to/hashes.txt
```
### 2. Filtrer les résultats vers les hachages les plus populaires uniquement
Réduisez les faux positifs provenant de types de hachage obscurs en filtrant par popularité :
```bash
# Level 1: Most popular hashes only (MD5, SHA1, SHA256, NTLM, bcrypt, etc.)
nth -f hashes.txt --popularity 1
```
### 3. Rechercher et extraire les hachages intégrés dans du texte non structuré
Analysez les chaînes de mémoire brute, les fichiers journaux ou le code source à la recherche de hachages :
```bash
nth --text "Found hash in config: 098f6bcd4621d373cade4e832627b4f6 on port 8080"
```
---

## Sortie au format Hashcat et John l'Éventreur

`nth` rend trivial l'introduction de hachages dans des crackers sans mémoriser les numéros de mode :

| Exemple de format de hachage | Algorithme détecté | Mode Hashcat (`-m`) | Format John (`--format`) |
|---|---|---|---|
| `e80b5017098950fc58aad83c8c14978e` | MD5 | `0` | `raw-md5` |
| `b4b9b02e6f09a9bd760f388b67351e2b` | NTLM | `1000` | `nt` |
| `284ddc0d513372c7d764a0ff813473f2...` | SHA-256 | `1400` | `raw-sha256` |
| `$2a$12$DOW8W6...` | bcrypt | `3200` | `bcrypt` |
| `$6$salt$hash...` | SHA-512 (Unix) | `1800` | `sha512crypt` |
| `$krb5tgs$23$*...` | Kerberoast (TGS-REP) | `13100` | `krb5tgs` |
| `$krb5asrep$23$...` | AS-REP Torréfaction | `18200` | `krb5asrep` |

---

## Filtres de décodage automatique et de popularité

De nombreuses applications stockent les hachages en Base64 ou Hex avant l'insertion dans la base de données. `nth` peut inverser cela automatiquement :
```bash
# Decode Base64 hash before checking
nth -t "XUkQS8xLJnY5cZ0REXxFkg==" --base64

# Decode hex encoded string before checking
nth -t "3564343134303261626334623261373662393731396439313130313763353932" --hex
```
---

## Intégration avec Cracking Pipelines

Transférez rapidement un hachage inconnu vers Hashcat à l’aide de l’identification automatisée :
```bash
# 1. Check hash and capture Hashcat mode
HASH="b4b9b02e6f09a9bd760f388b67351e2b"
MODE=$(nth -t "$HASH" --hashcat --popularity 1 | grep -oP '(?<=Hashcat: -m )\d+' | head -n 1)

# 2. Launch Hashcat with identified mode
hashcat -m "$MODE" -a 0 "$HASH" /usr/share/wordlists/rockyou.txt
```