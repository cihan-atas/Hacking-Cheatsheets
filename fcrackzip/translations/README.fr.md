<!-- language-home-navigation -->
[🏠 Page d'accueil Française](../../README.fr.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# Aide-mémoire fcrackzip

[fcrackzip](https://github.com/hyc/fcrackzip) est un cracker de mot de passe en ligne de commande rapide et multithread pour les archives ZIP cryptées. Il prend en charge à la fois les attaques basées sur un dictionnaire et les attaques par force brute optimisées contre le ZipCrypto traditionnel (chiffrement de flux PKZIP) et peut vérifier l'intégrité des archives sans extraction complète.

---

## Table des matières

- [Fonctionnalités principales et limitations](#core-features--limitations)
- [Installation](#installation)
-[Attaques de dictionnaire (`-D -p`)](#dictionary-attacks--d--p)
- [Attaques par force brute (`-b`)](#attaques par force brute--b)
- [Jeux de caractères et spécification de longueur](#character-sets--length-spécification)
-[Benchmark et optimisation des performances](#benchmark--performance-optimization)
- [ZipCrypto contre AES-256 (Alternative John/Hashcat)](#zipcrypto-vs-aes-256-johnhashcat-alternative)

---

## Fonctionnalités et limitations de base

- **Cracker ZipCrypto ultra-rapide :** Écrit en C, optimisé pour l'algorithme ZipCrypto existant (cryptage PKZIP standard).
- **Mode Dictionnaire :** Lit les mots de passe des candidats de manière séquentielle à partir des listes de mots.
- **Mode Brute-Force :** génère des candidats à la volée à l'aide de jeux de caractères et de limites de longueur configurables.
- **Test de décompression des archives (`-u`) :** Vérifie les mots de passe des candidats en tentant de décompression, éliminant ainsi pratiquement les faux positifs.
- **Limitation :** Ne prend **pas** en charge le cryptage WinZip AES-256 moderne. Pour les ZIP AES-256, utilisez `zip2john` + John the Ripper ou Hashcat (mode 13600).

---

##Installation
```bash
# Debian / Ubuntu / Kali Linux
sudo apt-get update && sudo apt-get install -y fcrackzip

# Arch Linux / BlackArch
sudo pacman -S fcrackzip

# Fedora / CentOS / RHEL
sudo dnf install -y fcrackzip

# Verify command
fcrackzip -h
```
---

## Attaques par dictionnaire (`-D -p`)

Le moyen le plus rapide de tester des listes de mots courantes :

### 1. Attaque de base par dictionnaire avec vérification de décompression
```bash
fcrackzip -u -D -p /usr/share/wordlists/rockyou.txt secret_archive.zip
```
- `-u` : Mode Unzip (vérifie le fichier CRC extrait pour éviter les faux positifs).
- `-D` : Mode d'attaque par dictionnaire.
- `-p <file>` : Chemin d'accès au fichier dictionnaire.

### 2. Mode de sortie détaillé
Afficher la vitesse testée, le mot de passe actuel et les informations de débogage :
```bash
fcrackzip -v -u -D -p passwords.txt backup.zip
```
---

## Attaques par force brute (`-b`)

Lorsque les attaques par dictionnaire échouent, utilisez la force brute contre les mots de passe courts inconnus :

### 1. Codes PIN numériques Brute-Force (4 à 6 chiffres)
```bash
fcrackzip -u -b -c 1 -l 4-6 secure.zip
```
- `-b` : mode force brute.
- `-c 1` : Jeu de caractères : chiffres uniquement (`0-9`).
- `-l 4-6` : Plage de longueur : min 4, max 6 caractères.

### 2. Caractères alphanumériques minuscules (1 à 5 caractères)
```bash
fcrackzip -u -b -c a1 -l 1-5 archive.zip
```
- `-c a1` : Lettres minuscules (`a-z`) et chiffres (`0-9`).

### 3. Minuscules + Majuscules + Chiffres + Symboles
```bash
fcrackzip -u -b -c aA1! -l 6-8 protected.zip
```
- `-c a` : Lettres minuscules (`a-z`).
- `-c A` : Lettres majuscules (`A-Z`).
- `-c 1` : Chiffres (`0-9`).
- `-c !` : Symboles spéciaux (`[!:$%&/()=?{[]}+*~#]`).

---

## Jeux de caractères et spécification de longueur

| Drapeau | Jeu de caractères inclus | Exemples de candidats |
|---|---|---|
| `-c a` | Lettres minuscules (`a-z`) | `admin`, `secret` |
| `-c A` | Lettres majuscules (`A-Z`) | `SECRET`, `ROOT` |
| `-c 1` | Chiffres (`0-9`) | `1234`, `2024` |
| `-c !` | Caractères de ponctuation spéciaux | `!@#$` |
| `-c aA1` | Caractères alphanumériques combinés | `Admin123` |
| `-p <prefix>` | Chaîne de départ initiale | Utile pour reprendre la force brute |

Spécification des limites de longueur :
```bash
# Minimum length 6, Maximum length 8
-l 6-8

# Exact length 8
-l 8-8
```
---

## Benchmark et optimisation des performances

Testez le débit du processeur sur votre machine :
```bash
fcrackzip -B
```
---

## ZipCrypto contre AES-256 (alternative John/Hashcat)

Si `fcrackzip` renvoie `not a zipfile, or unsupported encryption type`, l'archive est cryptée avec AES-256 (WinZip AES). Utilisez `zip2john` :
```bash
# 1. Extract hash using zip2john
zip2john aes_protected.zip > zip.hash

# 2. Crack using John the Ripper
john --wordlist=/usr/share/wordlists/rockyou.txt zip.hash

# 3. Or crack using Hashcat (Mode 13600: WinZip)
cat zip.hash | cut -d ':' -f 2 > hashcat_zip.hash
hashcat -m 13600 hashcat_zip.hash /usr/share/wordlists/rockyou.txt
```