<!-- language-home-navigation -->
[🏠 Page d'accueil Française](../../README.fr.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🕷️ CeWL - Aide-mémoire complet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
   ██████╗███████╗██╗    ██╗██╗     
  ██╔════╝██╔════╝██║    ██║██║     
  ██║     █████╗  ██║ █╗ ██║██║     
  ██║     ██╔══╝  ██║███╗██║██║     
  ╚██████╗███████╗╚███╔███╔╝███████╗
   ╚═════╝╚══════╝ ╚══╝╚══╝ ╚══════╝
       Custom Word List Generator by Robin Wood (DigiNinja)
```
<p align="center">
  <img src="https://img.shields.io/badge/CeWL-Custom_Wordlist_Generator-blue?style=for-the-badge" alt="CeWL">
  <img src="https://img.shields.io/badge/Author-DigiNinja-orange?style=for-the-badge" alt="DigiNinja">
  <img src="https://img.shields.io/badge/Category-Password_Cracking-green?style=for-the-badge" alt="Craquage de mot de passe">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="Licence GPLv2">
</p>

<p align="center">
  <b>🌐 Générateur de dictionnaire de mots de passe spécifique à une cible qui analyse les sites Web pour récolter la terminologie spécialisée de l'entreprise, les noms de marque et les e-mails des employés</b>
</p>

---

## 📋 Table des matières

- [Présentation et philosophie des mots de passe ciblés](#-overview--la-philosophie-des-mots-de-passe-ciblés)
- [Installation](#-installation)
- [Exploration de base du site Web](#-basic-website-crawling)
- [Contrôles de la profondeur de l'araignée et de la longueur des mots] (#-spider-degree--word-length-controls)
- [Récolte d'adresses e-mail (`-e`)](#-harvesting-email-addresses--e)
- [Y compris les chiffres et les minuscules](#-y compris les chiffres--minuscules)
- [Extraction de métadonnées de document (`-a`)](#-document-metadata-extraction--a)
- [Nombre de mots et profilage de fréquence (`-c`)](#-word-count--frequence-profiling--c)
-[Chaînage de CeWL avec Hashcat et John Rules](#-chaining-cewl-with-hashcat--john-rules)
- [Tableau de référence rapide](#-quick-reference-table)
- [Considérations défensives et OPSEC](#-defensive-considérations--opsec)
- [Mentions légales](#-legal-disclaimer)

---

## 🎯 Présentation et philosophie des mots de passe ciblés

Les listes de mots génériques telles que `rockyou.txt` sont efficaces pour les mots de passe grand public, mais **elles échouent fréquemment face aux politiques de mots de passe d'entreprise** nécessitant plus de 12 caractères, des symboles spéciaux et des rotations saisonnières.

Les employés basent régulièrement leurs mots de passe d'entreprise sur :
- Noms des projets de l'entreprise, noms de code internes et slogans (`Pegasus2026!`).
- Terminologie spécifique à l'industrie, jargon technique ou marque de produit.
- Noms des dirigeants, points de repère et adresses des bureaux.

Créé par **Robin Wood (DigiNinja)**, **CeWL** (générateur de liste de mots personnalisée) est une araignée Ruby qui explore une URL cible spécifiée à une profondeur définie, extrait des mots uniques correspondant aux exigences de longueur minimale et crée un dictionnaire de mots de passe adapté à l'engagement.

---

## 📥Installation
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y cewl

# Arch Linux (AUR)
yay -S cewl

# Ruby Gem
gem install cewl
```
---

## 🚀 Exploration de base du site Web
```bash
# Crawl website and output words to console
cewl https://target.local

# Crawl and save to a custom wordlist file
cewl https://target.local -w company_wordlist.txt
```
---

## 🕸️ Contrôles de la profondeur et de la longueur des mots de l'araignée

Contrôlez la profondeur avec laquelle CeWL suit les liens et la taille des mots qu'il capture :
```bash
# Crawl 2 links deep (-d 2) and enforce minimum word length of 8 characters (-m 8)
cewl https://target.local -d 2 -m 8 -w wordlist_min8.txt

# Aggressive 3-level deep crawl for comprehensive corporate terminology
cewl https://target.local -d 3 -m 7 --no-words -w deep_crawl.txt
```
---

## 📧 Récolte d'adresses e-mail (`-e`)

Extrayez les adresses e-mail d'entreprise pour des simulations de phishing ou une pulvérisation d'informations d'identification ultérieures :
```bash
# Extract emails to a separate dedicated file
cewl https://target.local -e --email_file emails.txt -w words.txt
```
---

## 🔡 Y compris les chiffres et les minuscules
```bash
# Include words containing alphanumeric combinations (e.g., product names like V300, X86)
cewl https://target.local --with-numbers -m 6 -w words_with_numbers.txt

# Convert all discovered words to lowercase (great for rule generation)
cewl https://target.local --lowercase -w lowercase_words.txt
```
---

## 📄 Extraction des métadonnées du document (`-a`)

Extrayez les noms d'utilisateur de l'auteur et du créateur des documents PDF et Microsoft Office téléchargés :
```bash
# Download and analyze document metadata to extract employee usernames
cewl https://target.local -a --meta_file extracted_authors.txt
```
---

## 📊 Nombre de mots et profilage de fréquence (`-c`)

Triez les mots par fréquence pour identifier les mots à la mode en entreprise les plus répandus :
```bash
# Append occurrence count next to each word
cewl https://target.local -c -m 6 -w frequency.txt

# Sort by most frequent words in descending order
sort -k2 -nr frequency.txt | head -n 30
```
---

## ⚡ Enchaînement de CeWL avec Hashcat & John Rules

Une liste de mots CeWL brute contient les racines du dictionnaire de base. Pour produire des mots de passe viables, développez la liste de mots en utilisant les **règles de mutation Hashcat / John the Ripper** :
```bash
# 1. Harvest base words from corporate website
cewl https://target.local -m 5 --lowercase -w base_words.txt

# 2. Mutate words using Hashcat's best64 rule set
hashcat --force base_words.txt -r /usr/share/hashcat/rules/best64.rule --stdout | sort -u > mutated_passwords.txt

# Example mutations produced:
# Acmetool -> Acmetool2026!, !Acmetool123, 4cmet00l, Acmetool#1
```
---

## ⚡ Tableau de référence rapide

| Objectif | Commande |
|-----------|---------|
| **Exploration de base vers un fichier** | `cewl <URL> -w wordlist.txt` |
| **Définir la longueur et la profondeur minimales** | `cewl <URL> -m 8 -d 2 -w wordlist.txt` |
| **Extraire les e-mails d'entreprise**| `cewl <URL> -e --email_file emails.txt` |
| **Extraire les métadonnées du document**| `cewl <URL> -a --meta_file authors.txt` |
| **Inclure les mots alphanumériques**| `cewl <URL> --with-numbers -w wordlist.txt` |
| **Sortie minuscule** | `cewl <URL> --lowercase -w wordlist.txt` |
| **Compter les fréquences des mots** | `cewl <URL> -c -w frequencies.txt` |
| **Authentifier derrière la connexion**| `cewl <URL> -a "Cookie: session=xyz"` |

---

## 🛡️ Considérations défensives et OPSEC

1. **Journaux d'accès au serveur Web :** CeWL génère des centaines de requêtes HTTP GET rapides, produisant des journaux d'accès Spider identifiables. Utilisez des chaînes User-Agent personnalisées (`-u "Mozilla/5.0..."`) pour vous fondre dans la masse.
2. **Pièges Honeyword :** Les équipes bleues défensives peuvent intégrer des liens HTML invisibles ou cachés menant aux informations d'identification Canary pour signaler les araignées automatisées.

---

## ⚠️ Mentions légales

> [!ATTENTION]
> CeWL est conçu uniquement pour les évaluations de sécurité autorisées, les tests d'intrusion et la recherche d'audit de mots de passe. Il est strictement interdit de gratter ou de nettoyer des systèmes non autorisés.