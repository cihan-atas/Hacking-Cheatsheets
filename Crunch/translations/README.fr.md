<!-- language-home-navigation -->
[🏠 Page d'accueil Française](../../README.fr.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🔢 Crunch - Aide-mémoire complet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
   ██████╗██████╗ ██╗   ██╗███╗   ██╗ ██████╗██╗  ██╗
  ██╔════╝██╔══██╗██║   ██║████╗  ██║██╔════╝██║  ██║
  ██║     ██████╔╝██║   ██║██╔██╗ ██║██║     ███████║
  ██║     ██╔══██╗██║   ██║██║╚██╗██║██║     ██╔══██║
  ╚██████╗██║  ██║╚██████╔╝██║ ╚████║╚██████╗██║  ██║
   ╚═════╝╚═╝  ╚═╝ ╚═════╝ ╚═╝  ╚═══╝ ╚═════╝╚═╝  ╚═╝
       High-Speed Pattern-Based Wordlist & Character Matrix Generator
```
<p align="center">
  <img src="https://img.shields.io/badge/Crunch-Wordlist_Generator-blue?style=for-the-badge" alt="Crunch">
  <img src="https://img.shields.io/badge/Language-C-orange?style=for-the-badge" alt="Language C">
  <img src="https://img.shields.io/badge/Category-Password_Cracking-green?style=for-the-badge" alt="Craquage de mot de passe">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="Licence GPLv2">
</p>

<p align="center">
  <b>🌐 Générateur de liste de mots qui génère toutes les combinaisons possibles de jeux de caractères à l'aide de longueurs, de motifs et de symboles personnalisés</b>
</p>

---

## 📋 Table des matières

- [Présentation et moteur de permutation](#-overview--the-permutation-engine)
- [Installation](#-installation)
- [Syntaxe de base et jeux de caractères](#-basic-syntax--character-sets)
- [Correspondance de modèles et espaces réservés (`-t`)](#-pattern-matching--placeholders--t)
- [Utilisation des jeux de caractères système (`charset.lst`)](#-using-system-charsets-charsetlst)
- [Limitation des caractères en double (`-d`)](#-duplicate-character-limiting--d)
- [Diviser les listes de mots en morceaux (`-b` et `-c`)](#-splitting-wordlists-into-chunks--b---c)
-[Piping directement dans les outils d'attaque (espace disque nul)](#-piping-directly-into-attack-tools-zero-disk-space)
- [Tableau de référence rapide](#-quick-reference-table)
- [Considérations défensives et avertissements de calcul](#-considérations-defensives--avertissements-de-calcul)
- [Mentions légales](#-legal-disclaimer)

---

## 🎯 Présentation et moteur de permutation

Lorsque vous attaquez des modèles de mots de passe connus (par exemple, des codes PIN d'entreprise, des numéros de téléphone ou des mots de passe conformes aux règles de complexité standard comme `Word2026!`), les dictionnaires statiques standard comme `rockyou.txt` manquent la cible ou sont inefficaces.

Écrit en C par **bofheaded**, **Crunch** est un générateur de listes de mots incroyablement rapide. Il calcule mathématiquement les permutations, vous permettant de définir les positions exactes des caractères, les longueurs, les pools de caractères et les contraintes de duplication.

---

## 📥Installation
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y crunch

# Arch Linux (AUR)
yay -S crunch
```
---

## 🚀 Syntaxe de base et jeux de caractères

La syntaxe de base nécessite une longueur minimale, une longueur maximale et éventuellement un alphabet :
```bash
# crunch <min> <max> [characterset] [options]

# Generate all 4-digit numeric PINs (0000 - 9999)
crunch 4 4 0123456789 -o 4digit_pins.txt

# Generate 5-character words using specific custom letters
crunch 5 5 abcdef -o custom_words.txt

# Generate lowercase alphabet combinations from length 3 to 4
crunch 3 4 abcdefghijklmnopqrstuvwxyz -o alpha_3_4.txt
```
---

## 🎭 Correspondance de motifs et espaces réservés (`-t`)

Le drapeau `-t` permet de corriger les caractères connus tout en fuzzant les emplacements inconnus à l'aide d'espaces réservés spéciaux :

| Espace réservé | Type de caractère |
|-------------|----------------|
| `@` | Lettres minuscules (`a-z`) |
| `,` | Lettres majuscules (`A-Z`) |
| `%` | Numéros (`0-9`) |
| `^` | Symboles spéciaux / ponctuation (`!@#$%^&*...`) |

### Exemples de modèles pratiques :
```bash
# Generate Turkish phone numbers (e.g., 0532XXXXXXX)
crunch 11 11 -t 0532%%%%%%% -o phone_numbers.txt

# Generate passwords conforming to: Capital letter + 4 lowercase + 2 digits + 1 symbol
# e.g., Admin24! or Super99#
crunch 8 8 -t ,@@@@%%^ -o policy_passwords.txt

# Target known prefix: Spring2026! or Spring2026#
crunch 11 11 -t Spring2026^ -o spring_passwords.txt
```
---

## 📚 Utilisation des jeux de caractères système (`charset.lst`)

Crunch regroupe des matrices de caractères préconfigurées dans `/usr/share/crunch/charset.lst` :
```bash
# Use pre-defined alphanumeric set (mixalpha-numeric)
crunch 6 6 -f /usr/share/crunch/charset.lst mixalpha-numeric -o out.txt

# Use hexadecimal lowercase characters (0-9, a-f)
crunch 8 8 -f /usr/share/crunch/charset.lst hex-lower -o hex_keys.txt
```
---

## 🚫 Limitation des caractères en double (`-d`)

Appliquez des limites aux caractères en double consécutifs pour éliminer les mots de passe improbables :
```bash
# Generate 8-character lowercase words with at most 2 consecutive identical letters
# (e.g., prevents 'aaabbb' while allowing 'aab')
crunch 8 8 abcdefghijklmnopqrstuvwxyz -d 2@ -o deduped.txt

# Limit consecutive numbers to at most 1 (prevents '11' or '22')
crunch 6 6 -t %%%%%% -d 1% -o no_repeated_digits.txt
```
---

## 📦 Diviser les listes de mots en morceaux (`-b` et `-c`)

Empêchez les fichiers de plusieurs téraoctets d'épuiser l'espace disque en les divisant en volumes gérables :
```bash
# Split wordlist into 500MB chunks
crunch 8 8 -f /usr/share/crunch/charset.lst mixalpha-numeric -b 500mb -o START

# Split by line count (every 1,000,000 lines)
crunch 8 8 0123456789 -c 1000000 -o START
```
---

## ⚡ Connexion directe aux outils d'attaque (espace disque nul)

N'écrivez jamais de gigantesques listes de mots sur le disque lorsque vous pouvez diffuser des permutations directement dans des outils de craquage :
```bash
# Stream 8-digit numeric PINs straight into Aircrack-ng
crunch 8 8 0123456789 | aircrack-ng -w - -b 00:11:22:33:44:55 capture.cap

# Stream policy passwords directly into Hydra SSH brute-force
crunch 8 8 -t Admin%%^ | hydra -l admin -P - 10.10.10.50 ssh

# Stream into John the Ripper
crunch 6 6 0123456789 | john --stdin --format=raw-md5 hashes.txt
```
---

## ⚡ Tableau de référence rapide

| Objectif | Commande |
|-----------|---------|
| **PIN numériques (4 chiffres)** | `crunch 4 4 0123456789 -o pins.txt` |
| **Modèle : Majuscule + Mot + Chiffres**| `crunch 8 8 -t ,@@@@%%^ -o passwords.txt` |
| **Modèle : Préfixe spécifique** | `crunch 10 10 -t Company%%% -o wordlist.txt` |
| **Utiliser le fichier Charset** | `crunch 6 6 -f charset.lst mixalpha-numeric -o out.txt` |
| **Limite Consécutive Identique**| `crunch 8 8 -t @@@@@@@@ -d 2@ -o out.txt` |
| ** Divisé en fichiers de 1 Go ** | `crunch 8 8 <CHARS> -b 1gb -o START` |
| **Tuyau vers Aircrack-ng** | `crunch 8 8 0123456789 \| aircrack-ng -w - target.cap` |
| **Tuyau vers Hydra** | `crunch 7 7 -t User%%% \| hydra -l user -P - <IP> ssh` |

---

## 🛡️ Considérations défensives et avertissements de calcul

> [!AVERTISSEMENT]
> **Explosions de permutation :** Vérifiez toujours la taille de sortie projetée de Crunch avant de confirmer la génération du disque. Une liste de mots alphanumériques sans contrainte de 8 caractères produit **plus de 200 téraoctets** de données texte ! Sortie de tuyau autant que possible.

---

## ⚠️ Mentions légales

> [!ATTENTION]
> Crunch est destiné uniquement à la récupération de mot de passe autorisée, aux tests d'intrusion et à l'audit de sécurité. Le forçage brutal de mots de passe sur des systèmes non autorisés est strictement illégal.