<!-- language-home-navigation -->
[🏠 Page d'accueil Française](../../README.fr.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🎯 Wfuzz - Aide-mémoire complet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ██╗    ██╗███████╗██╗   ██╗███████╗███████╗
  ██║    ██║██╔════╝██║   ██║╚══███╔╝╚══███╔╝
  ██║ █╗ ██║█████╗  ██║   ██║  ███╔╝   ███╔╝ 
  ██║███╗██║██╔══╝  ██║   ██║ ███╔╝   ███╔╝  
  ╚███╔███╔╝██║     ╚██████╔╝███████╗███████╗
   ╚══╝╚══╝ ╚═╝      ╚═════╝ ╚══════╝╚══════╝
       The Flexible Web Application Fuzzer & Parameter Explorer
```
<p align="center">
  <img src="https://img.shields.io/badge/Wfuzz-Web_Fuzzer-blue?style=for-the-badge" alt="Wfuzz">
  <img src="https://img.shields.io/badge/Language-Python-orange?style=for-the-badge" alt="Language Python">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-green?style=for-the-badge" alt="Sécurité Web et API">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="Licence GPLv2">
</p>

<p align="center">
  <b>🌐 Fuzzer HTTP hautement personnalisable conçu pour découvrir les paramètres cachés, les contournements, les en-têtes et les matrices multi-charges utiles</b>
</p>

---

## 📋 Table des matières

- [Vue d'ensemble et architecture des mots-clés](#-overview--the-keyword-architecture)
- [Comparaison Wfuzz vs ffuf] (#-wfuzz-vs-ffuf-comparison)
- [Installation](#-installation)
- [Répertoire de base et découverte de fichiers](#-basic-directory--file-discovery)
-[Filtrage et masquage du bruit (`--hc`, `--hl`, `--hw`, `--hh`)](#-filtrage--masquage-bruit---hc---hl---hw---hh)
-[Types de charge utile et encodeurs dynamiques (`-z`)](#-payload-types--dynamic-encoders--z)
- [Fuzzing des paramètres et variables cachées](#-paramètre--variable-cachée-fuzzing)
-[En-têtes, cookies et authentification](#-headers-cookies--authentification)
- [Fuzzing multi-variable (`FUZZ` et `FUZ2Z`)](#-multi-variable-fuzzing-fuzz-and-fuz2z)
-[Routage via Burp Suite (`-p`)](#-routing-through-burp-suite--p)
- [Tableau de référence rapide](#-quick-reference-table)
- [Considérations défensives et OPSEC](#-defensive-considérations--opsec)
- [Mentions légales](#-legal-disclaimer)

---

## 🎯 Présentation et architecture des mots clés

Écrit en Python par **Xavi Mendez**, **Wfuzz** est un outil classique et éprouvé pour évaluer les applications Web. Il remplace les espaces réservés désignés (par défaut : `FUZZ`) dans les URL, les paramètres, les en-têtes et les corps POST par des entrées de dictionnaire ou des charges utiles générées de manière algorithmique.

Wfuzz se distingue par sa **polyvalence de charge utile** inégalée (plages, générateurs IP, chaînes d'expressions régulières, encodeurs à la volée comme MD5 ou Base64) et sa mécanique de filtrage modulaire.

---

## ⚖️ Comparaison Wfuzz vs ffuf

| Fonctionnalité | Wfuzz | ffff |
|---------|-------|------|
| **Langage d'implémentation** | Python | Aller |
| **Vitesse brute** | ⭐⭐⭐ (Bon) | ⭐⭐⭐⭐⭐ (Incroyablement rapide) |
| **Encodeurs de charge utile dynamiques**| ✅ Natif (`-z file,pass.txt,md5`)| ❌ Nécessite des scripts externes |
| **Combinaisons multi-charges utiles**| ✅ Natif (`FUZZ`, `FUZ2Z`, `FUZ3Z`)| ✅ Pris en charge (`-w w1:W1 -w w2:W2`)|
| **Générateurs de charge utile intégrés**| ✅ Plages, IP, Hex, Dictionnaires| ❌ Fichiers de listes de mots uniquement |
| **Utilisation des ressources** | RAM/CPU modérés | Faible empreinte mémoire |

---

## 📥Installation
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y wfuzz

# Python Pip
pip3 install wfuzz

# Arch Linux
sudo pacman -S wfuzz
```
---

## 🚀 Répertoire de base et découverte de fichiers
```bash
# Basic directory fuzzing
wfuzz -w /usr/share/wordlists/dirb/common.txt http://target.local/FUZZ

# Fuzzing file extensions
wfuzz -w extensions.txt http://target.local/index.FUZZ

# Fuzzing hidden backup files
wfuzz -w words.txt http://target.local/FUZZ.bak
```
---

## 🎯 Filtrage et masquage du bruit (`--hc`, `--hl`, `--hw`, `--hh`)

Les serveurs du monde réel renvoient des réponses même pour les pages inexistantes (pages 404 personnalisées ou redirections). Wfuzz fournit des indicateurs de filtrage précis :
```bash
# Hide specific HTTP status codes (e.g. 404, 403)
wfuzz -w wordlist.txt --hc 404,403 http://target.local/FUZZ

# Hide responses by line count (filters out static custom 404 pages)
wfuzz -w wordlist.txt --hl 42 http://target.local/FUZZ

# Hide responses by word count
wfuzz -w wordlist.txt --hw 120 http://target.local/FUZZ

# Hide responses by character count / body length
wfuzz -w wordlist.txt --hh 0,1052 http://target.local/FUZZ

# Show ONLY HTTP 200 and 302 responses
wfuzz -w wordlist.txt --sc 200,302 http://target.local/FUZZ
```
---

## 📦 Types de charge utile et encodeurs dynamiques (`-z`)

Wfuzz fournit des générateurs intégrés qui éliminent le besoin de fichiers texte pré-générés :
```bash
# Numeric Range Fuzzing (e.g., IDOR testing from 1 to 500)
wfuzz -z range,1-500 http://target.local/user.php?id=FUZZ

# Custom Inline List
wfuzz -z list,admin-root-test-dev http://target.local/FUZZ/

# On-the-fly MD5 encoding of wordlist entries
wfuzz -z file,passwords.txt,md5 http://target.local/login?hash=FUZZ

# On-the-fly Base64 encoding
wfuzz -z file,usernames.txt,base64 http://target.local/auth?token=FUZZ
```
---

## 🔍 Fuzzing des paramètres et des variables cachées

Découvrez les paramètres GET/POST non liés :
```bash
# GET parameter discovery
wfuzz -w /usr/share/seclists/Discovery/Web-Content/burp-parameter-names.txt \
  --hc 404 --hh 2340 "http://target.local/search.php?FUZZ=test"

# POST body parameter discovery
wfuzz -w parameters.txt -d "FUZZ=admin" --hc 404 http://target.local/api/update
```
---

## 🍪 En-têtes, cookies et authentification
```bash
# Cookie Fuzzing (Session hijacking / privilege testing)
wfuzz -w wordlist.txt -b "session=FUZZ" http://target.local/dashboard

# HTTP Header Fuzzing (IP bypasses via X-Forwarded-For)
wfuzz -z file,ips.txt -H "X-Forwarded-For: FUZZ" http://target.local/admin

# Basic Authentication brute force
wfuzz -z file,users.txt -z file,passwords.txt --basic "FUZZ:FUZ2Z" http://target.local/protected/
```
---

## ⚡ Fuzzing multi-variable (`FUZZ` et `FUZ2Z`)

Combinez plusieurs listes de mots à l'aide de balises d'espace réservé distinctes :
```bash
# Test username and password matrix simultaneously
wfuzz -w users.txt -w passwords.txt -d "user=FUZZ&pass=FUZ2Z" --hc 200 http://target.local/login.php

# Fuzz path and extension together
wfuzz -w words.txt -w extensions.txt http://target.local/FUZZ.FUZ2Z
```
---

## 🔁 Routage via Burp Suite (`-p`)

Inspectez les charges utiles et les réponses dans Burp Suite :
```bash
# Route requests through local Burp Suite proxy
wfuzz -p 127.0.0.1:8080:HTTP -w wordlist.txt http://target.local/FUZZ
```
---

## ⚡ Tableau de référence rapide

| Objectif | Commande |
|-----------|---------|
| **Fuzzing de répertoire** | `wfuzz -w <DICT> <URL>/FUZZ` |
| **Masquer les codes d'état HTTP** | `wfuzz -w <DICT> --hc 404,403 <URL>/FUZZ` |
| **Masquer par longueur de réponse** | `wfuzz -w <DICT> --hh <LENGTH> <URL>/FUZZ` |
| **Paramètre Fuzz GET** | `wfuzz -w <PARAMS> "<URL>?FUZZ=test"` |
| **Corps Fuzz POST** | `wfuzz -w <PARAMS> -d "FUZZ=test" <URL>` |
| **Charges utiles à plage numérique** | `wfuzz -z range,1-100 "<URL>?id=FUZZ"` |
| **Encodez les charges utiles à la volée**| `wfuzz -z file,<DICT>,base64 "<URL>?token=FUZZ"` |
| **Fuzzing multi-mots-clés** | `wfuzz -w <W1> -w <W2> -d "u=FUZZ&p=FUZ2Z" <URL>` |
| **Itinéraire via Burp Proxy** | `wfuzz -p 127.0.0.1:8080:HTTP -w <DICT> <URL>/FUZZ` |

---

## 🛡️ Considérations défensives et OPSEC

1. **Blocage WAF :** Le fuzzing de centaines de requêtes par seconde déclenche la limitation du débit Cloudflare, AWS WAF et ModSecurity. Utilisez `-s <seconds>` pour limiter la vitesse des requêtes.
2. **Épuisement des ressources :** Un fuzz important sur les requêtes de bases de données non indexées peut entraîner des conditions de déni de service (DoS) sur les serveurs Web existants.

---

## ⚠️ Mentions légales

> [!ATTENTION]
> Cette aide-mémoire est destinée uniquement aux tests d'intrusion et aux audits de vulnérabilité autorisés. Le fuzzing d’applications non autorisées est strictement interdit.