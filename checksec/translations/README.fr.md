<!-- language-home-navigation -->
[🏠 Page d'accueil Française](../../README.fr.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🛡️ checksec & Binutils - Aide-mémoire complet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
   ██████╗██╗  ██╗███████╗ ██████╗██╗  ██╗███████╗███████╗ ██████╗
  ██╔════╝██║  ██║██╔════╝██╔════╝██║ ██╔╝██╔════╝██╔════╝██╔════╝
  ██║     ███████║█████╗  ██║     █████╔╝ ███████╗█████╗  ██║     
  ██║     ██╔══██║██╔══╝  ██║     ██╔═██╗ ╚════██║██╔══╝  ██║     
  ╚██████╗██║  ██║███████╗╚██████╗██║  ██╗███████║███████╗╚██████╗
   ╚═════╝╚═╝  ╚═╝╚══════╝ ╚═════╝╚═╝  ╚═╝╚══════╝╚══════╝ ╚═════╝
          Linux ELF Binary Mitigation Auditor & Inspection Toolkit
```
<p align="center">
  <img src="https://img.shields.io/badge/checksec-Security_Mitigations-blue?style=for-the-badge" alt="checksec">
  <img src="https://img.shields.io/badge/readelf-ELF_Headers-orange?style=for-the-badge" alt="readelf">
  <img src="https://img.shields.io/badge/objdump-Disassembly-green?style=for-the-badge" alt="objdump">
  <img src="https://img.shields.io/badge/Platform-Linux_ELF-purple?style=for-the-badge" alt="Linux ELF">
</p>

<p align="center">
  <b>🛡️ Outils de ligne de commande essentiels pour auditer les atténuations de sécurité des exécutables Linux (RELRO, Stack Canary, NX, PIE, Fortify) et disséquer les en-têtes, sections et tables de symboles binaires ELF</b>
</p>

---

## 📋 Table des matières

- [Vue d'ensemble et matrice d'atténuation binaire](#-overview--binary-mitigations-matrix)
- [Installation](#-installation)
- [checksec : Audit d'atténuation rapide](#-checksec-rapid-mitigation-auditing)
  - [Audit des binaires simples et par lots](#auditing-single--batch-binaries)
  - [Audit des processus en cours](#auditing-running-processes)
  - [Sortie JSON et CSV](#json--csv-output)
- [readelf : En-tête ELF et dissection de section](#-readelf-elf-header--section-dissection)
  - [En-tête de fichier ELF (`-h`)](#elf-file-header--h)
  -[En-têtes et segments du programme (`-l`)](#program-headers--segments--l)
  - [En-têtes de section (`-S`)](#section-headers--s)
  - [Tableaux de symboles (`-s` / `--syms`)](#symbol-tables--s----syms)
  - [Dépendances et bibliothèques dynamiques (`-d`)](#dynamic-dependencies--libraries--d)
- [objdump : Démontage et relocalisations](#-objdump-disassembly--relocations)
  - [Démontage Intel (`-d -M intel`)](#intel-disassembly--d--m-intel)
  - [Entrées de relocalisation et GOT (`-R`)](#relocation-entries--got--r)
- [nm et chaînes : résolution des symboles et chaînes](#-nm--strings-symbol-resolution--strings)
  - [Liste des symboles démangles avec `nm`](#listing-demangled-symbols-with-nm)
  - [Extraction de chaînes obscurcies avec `strings`](#extracting-obscurcated-strings-with-strings)
- [Référence de compilation renforcée](#-hardened-compilation-reference)
- [Tableau de référence rapide](#-quick-reference-table)
- [Mentions légales](#-legal-disclaimer)

---

## 🎯 Présentation et matrice d'atténuation binaire

Avant de commencer tout workflow d'ingénierie inverse ou d'exploitation, il est obligatoire de comprendre quelles atténuations de sécurité sont activées :

| Atténuation | Nom complet | Mécanisme de protection | Stratégie de contournement |
|------------|-----------|----------------------|------------------|
| **RELRO** | Délocalisation Lecture seule | Rend `.got` en lecture seule (`Full RELRO`) pour empêcher l'écrasement du pointeur | Structures de tas cibles, adresses de retour ou hooks de fonction |
| **Canari** | Protecteur de pile | Place un cookie aléatoire avant le pointeur de retour ; vérifie l'intégrité au retour | Fuite de la valeur Canary via une fuite de mémoire ou par force brute (démons fork) |
| **NX** | Non-exécution / DEP | Marque la pile, le tas et les segments de données comme non exécutables | Programmation orientée retour (ROP) ou programmation orientée saut (JOP) |
| **TARTE** | Poste Exécutable Indépendant | Charge l'adresse de base binaire à un emplacement aléatoire dans la mémoire virtuelle (ASLR) | Pointeur de code de fuite pour calculer le décalage d'adresse de base |
| **Fortifier** | `_FORTIFY_SOURCE` | Remplace les fonctions de chaîne non sécurisées (`strcpy` -> `__strcpy_chk`) par la vérification des limites | Bogues de logique sans débordement, formatage de chaîne sans débordement de tampon |

---

## 📥Installation
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y checksec binutils

# Arch Linux
sudo pacman -S checksec binutils

# Python pip (standalone checksec)
pip install checksec.py
```
---

## ⚡ checksec : audit d'atténuation rapide

### Audit des binaires uniques et par lots
```bash
# Audit a single executable
checksec --file=./target_binary

# Audit all binaries in directory
checksec --dir=/usr/bin/
```
*Exemple de sortie standard :*
```text
RELRO           STACK CANARY      NX            PIE             RPATH      RUNPATH      FILE
Full RELRO      Canary found      NX enabled    PIE enabled     No RPATH   No RUNPATH   ./target_binary
```
### Audit des processus en cours d'exécution
Auditez les atténuations de mémoire et l'état ASLR d'un démon actif :
```bash
# Audit by Process ID (PID)
checksec --proc=1337

# Audit process by name
checksec --proc-all
```
### Sortie JSON et CSV
Exportez les résultats pour une analyse automatisée :
```bash
checksec --file=./target_binary --output=json > security.json
checksec --file=./target_binary --output=csv > security.csv
```
---

## 🔬 readelf : Dissection d'en-tête et de section ELF

L'utilitaire `readelf` affiche les informations des fichiers ELF sans appeler d'exécution :

### En-tête de fichier ELF (`-h`)
Révèle l'architecture du processeur, la classe 32 ou 64 bits, l'adresse du point d'entrée et l'endianité :
```bash
readelf -h ./target_binary
```
### En-têtes et segments de programme (`-l`)
Affiche les segments chargés en mémoire au moment de l'exécution et les indicateurs (`R`, `W`, `E`). Crucial pour identifier si la pile (`GNU_STACK`) dispose des autorisations d'exécution :
```bash
readelf -l ./target_binary
```
*(Si `GNU_STACK` a `RWE`, l'exécution de la pile est autorisée ; NX est désactivé).*

### En-têtes de section (`-S`)
Inspectez les dispositions de mémoire, les adresses et les décalages pour `.text` (code), `.data` (données initialisées), `.rodata` (constantes), `.bss` (données non initialisées), `.plt` et `.got` :
```bash
readelf -S ./target_binary
```
### Tableaux de symboles (`-s` / `--syms`)
Répertoriez toutes les fonctions exportées, les variables globales et les symboles libc importés :
```bash
# List all symbols
readelf -s ./target_binary

# Filter dynamic symbols used at runtime
readelf --dyn-syms ./target_binary
```
### Dépendances et bibliothèques dynamiques (`-d`)
Répertorie les bibliothèques partagées requises (balises `NEEDED`, par exemple `libc.so.6`, `libssl.so`) :
```bash
readelf -d ./target_binary
```
---

## 🧩 objdump : Démontage et déménagements

### Démontage Intel (`-d -M intel`)
Démontez les sections exécutables en un assemblage de syntaxe Intel propre :
```bash
# Disassemble .text section in Intel syntax
objdump -d -M intel ./target_binary

# Disassemble specific function (e.g. main)
objdump -d -M intel ./target_binary | grep -A 30 "<main>:"
```
### Entrées de réinstallation et GOT (`-R`)
Affichez les adresses de relocalisation Global Offset Table (GOT) pour les appels de bibliothèque importés :
```bash
objdump -R ./target_binary
```
*Affiche les adresses cibles pour les fonctions telles que `puts`, `printf` et `system`.*

---

## 🔍 nm & chaînes : résolution des symboles et chaînes

### Liste des symboles démêlés avec `nm`
```bash
# List defined symbols only (ignore external imports)
nm --defined-only -C ./target_binary

# List undefined external symbols (functions resolved by dynamic linker)
nm -u -C ./target_binary
```
### Extraction de chaînes obscurcies avec `strings`
```bash
# Extract ASCII strings with minimum 8 characters
strings -a -n 8 ./target_binary

# Extract 16-bit Little Endian wide strings (UTF-16 e.g., Windows / Java)
strings -e l -a ./target_binary

# Print byte offset of each string in hex
strings -a -t x ./target_binary
```
---

## 🛡️ Référence de compilation renforcée

Pour créer un binaire ELF avec des atténuations de sécurité maximales :
```bash
gcc -O2 \
  -Wall \
  -fstack-protector-strong \
  -D_FORTIFY_SOURCE=2 \
  -Wl,-z,relro,-z,now \
  -fPIE -pie \
  -Wl,-z,noexecstack \
  source.c -o secure_binary
```
---

## ⚡ Tableau de référence rapide

| Objectif | Commande |
|-----------|---------|
| **Atténuations d'audit** | `checksec --file=<bin>` |
| **Audit en cours d'exécution du PID** | `checksec --proc=<pid>` |
| **Voir l'en-tête ELF** | `readelf -h <bin>` |
| **Afficher l'indicateur Stack NX** | `readelf -l <bin> \| grep -A 1 GNU_STACK` |
| **Sections de la liste (.got, .plt)**| `readelf -S <bin>` |
| **Afficher les symboles dynamiques** | `readelf --dyn-syms <bin>` |
| **Démontage Intel** | `objdump -d -M intel <bin>` |
| **Inspecter les déménagements GOT** | `objdump -R <bin>` |
| **Symboles de liste (démêlés)** | `nm -C <bin>` |
| **Extraire les chaînes UTF-16** | `strings -e l -a <bin>` |
| **Afficher les décalages de chaîne** | `strings -a -t x <bin>` |

---

## ⚠️ Mentions légales

> [!ATTENTION]
> checksec et GNU Binutils sont des outils de diagnostic et d'ingénierie inverse destinés à l'audit de sécurité des logiciels, à la validation de la défense et à la recherche binaire autorisée. L’inspection des logiciels non autorisés doit respecter les réglementations locales en matière de propriété intellectuelle.