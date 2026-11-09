<!-- language-home-navigation -->
[🏠 Page d'accueil Française](../../README.fr.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🧩 Ropper & ROPgadget - Aide-mémoire complet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ██████╗  ██████╗ ██████╗ ██████╗ ███████╗██████╗ 
  ██╔══██╗██╔═══██╗██╔══██╗██╔══██╗██╔════╝██╔══██╗
  ██████╔╝██║   ██║██████╔╝██████╔╝█████╗  ██████╔╝
  ██╔══██╗██║   ██║██╔═══╝ ██╔═══╝ ██╔══╝  ██╔══██╗
  ██║  ██║╚██████╔╝██║     ██║     ███████╗██║  ██║
  ╚═╝  ╚═╝ ╚═════╝ ╚═╝     ╚═╝     ╚══════╝╚═╝  ╚═╝
     Advanced ROP & JOP Gadget Finder and Chain Generator
```
<p align="center">
  <img src="https://img.shields.io/badge/Ropper-Gadget_Finder-blue?style=for-the-badge" alt="Ropper">
  <img src="https://img.shields.io/badge/ROPgadget-Exploit_Tool-orange?style=for-the-badge" alt="ROPgadget">
  <img src="https://img.shields.io/badge/Bypass-NX_%7C_DEP-red?style=for-the-badge" alt="Contournement NX">
  <img src="https://img.shields.io/badge/Category-Exploit_Development-green?style=for-the-badge" alt="Exploit Dev">
</p>

<p align="center">
  <b>🧩 Utilitaires de ligne de commande essentiels pour découvrir les gadgets de programmation orientée retour (ROP) et de programmation orientée saut (JOP) pour contourner les protections de mémoire non exécutables (NX/DEP)</b>
</p>

---

## 📋 Table des matières

- [Vue d'ensemble et théorie ROP](#-overview--rop-theory)
- [Installation](#-installation)
- [Découverte de gadgets de base](#-basic-gadget-discovery)
  - [Extraction de tous les gadgets](#extracting-all-gadgets)
  - [Profondeur d'instruction limitante](#limiting-instruction-profondeur)
- [Recherche avancée et filtrage de modèles](#-recherche-avancée--filtrage de modèles)
  - [Ciblage de registres spécifiques (`rdi`, `rsi`, `rdx`, `rax`)](#targeting-special-registers-rdi-rsi-rdx-rax)
  - [Gadgets d'écriture de mémoire (écrire-quoi-où)] (#memory-write-gadgets-write-what-where)
  - [Gadgets d'appel système et d'interruption](#syscall--interrupt-gadgets)
- [À l'exclusion des mauvais caractères (`--badbytes`)](#-à l'exclusion des mauvais caractères --- badbytes)
-[Génération automatisée de chaîne ROP](#-automated-rop-chain-generation)
  - [Chaînes automatisées Ropper](#ropper-automated-chains)
  - [Chaînes automatisées ROPgadget](#ropgadget-automated-chains)
- [Analyse des bibliothèques chargées (chaînes libc ROP)](#-analyzing-loaded-libraries-libc-rop-chains)
- [Tableau de référence rapide](#-quick-reference-table)
- [Considérations défensives et atténuations](#-considérations-défensives--atténuations)
- [Mentions légales](#-legal-disclaimer)

---

## 🎯 Présentation et théorie du ROP

Les systèmes d'exploitation modernes appliquent **NX (No-Execute) / DEP (Data Execution Prevention)**, qui empêche l'exécution directe du shellcode placé sur la pile ou le tas.

La **Programmation Orientée Retour (ROP)** contourne cette restriction en réutilisant de petites instructions machine existantes déjà présentes dans la mémoire exécutable (section `.text` du binaire ou bibliothèques partagées comme `libc.so`). Chaque séquence d'instructions se terminant par une instruction `ret` est appelée un **gadget**. En organisant les adresses sur la pile, les attaquants enchaînent les gadgets pour :
1. Chargez les arguments dans les registres du processeur (par exemple, `pop rdi; ret` pour les conventions d'appel x64).
2. Écrivez des chaînes (comme `/bin/sh`) dans des sections `.bss` ou `.data` inscriptibles.
3. Déclenchez `execve("/bin/sh", NULL, NULL)` ou appelez `system()`.

---

## 📥Installation
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y ropper python3-ropgadget

# Python pip (Latest Releases)
pip install ropper capstone
pip install ROPgadget

# Arch Linux
sudo pacman -S ropper python-ropgadget
```
---

## 🚀 Découverte de gadgets de base

### Extraction de tous les gadgets
```bash
# Using Ropper (Faster multi-threaded engine)
ropper --file ./target_binary --nocolor

# Using ROPgadget
ROPgadget --binary ./target_binary
```
### Limiter la profondeur des instructions
Réduisez les longues chaînes bruyantes en limitant la longueur du gadget (nombre d'instructions avant `ret`) :
```bash
# Limit to maximum 3 instructions before return
ropper --file ./target_binary --inst-count 3
ROPgadget --binary ./target_binary --depth 3
```
---

## 🔍 Recherche avancée et filtrage de modèles

### Ciblage de registres spécifiques (`rdi`, `rsi`, `rdx`, `rax`)
Dans la convention d'appel Linux 64 bits, les trois premiers arguments d'une fonction ou d'un appel système doivent résider dans `rdi`, `rsi` et `rdx` :
```bash
# Find pop rdi gadgets (1st argument)
ropper --file ./target_binary --search "pop rdi"

# Find pop rsi gadgets (2nd argument)
ropper --file ./target_binary --search "pop rsi"

# Find pop rdx gadgets (3rd argument)
ropper --file ./target_binary --search "pop rdx"

# Find gadgets that control RAX (Syscall number: 59 for execve)
ropper --file ./target_binary --search "pop rax"
```
Utilisation de la correspondance d'expressions régulières ROPgadget :
```bash
ROPgadget --binary ./target_binary --only "pop|ret" | grep -E "rdi|rsi|rdx|rax"
```
### Gadgets d'écriture en mémoire (écrire-quoi-où)
Lorsque vous devez stocker `/bin/sh` ou des arguments arbitraires en mémoire :
```bash
# Find instructions moving registers into pointer memory
ropper --file ./target_binary --search "mov [???], ???"
```
*Exemple de correspondance :* `mov qword ptr [rdi], rsi; ret` permet d'écrire tout ce qui se trouve dans `rsi` à l'adresse pointée par `rdi`.

### Gadgets d'appel système et d'interruption
```bash
# Find raw syscall instructions
ropper --file ./target_binary --search "syscall"

# x86 32-bit software interrupt
ropper --file ./target_binary --search "int 0x80"
```
---

## 🚫 Hors mauvais caractères (`--badbytes`)

Les vulnérabilités de formatage de chaîne ou les fonctions telles que `strcpy()` se terminent sur des octets nuls (`\x00`), des nouvelles lignes (`\x0a`) ou des retours chariot (`\x0d`). Excluez les adresses contenant ces octets :
```bash
# Filter out gadgets whose addresses contain bad characters
ropper --file ./target_binary --badbytes 000a0d --search "pop rdi"
ROPgadget --binary ./target_binary --badbytes "00|0a|0d"
```
---

## ⚡ Génération automatisée de chaînes ROP

Les deux outils peuvent créer automatiquement des modèles d'exploit Python fonctionnels qui invoquent `execve("/bin/sh")` :

### Chaînes automatisées Ropper
```bash
ropper --file ./target_binary --chain "execve"
```
### Chaînes automatisées ROPgadget
```bash
ROPgadget --binary ./target_binary --ropchain
```
*Cela produit un squelette de charge utile Python prêt à l'emploi chaînant `mprotect()` ou `execve()`.*

---

## 📚 Analyse des bibliothèques chargées (chaînes libc ROP)

Lorsque le binaire lui-même est compilé en **PIE** ou contient quelques gadgets utiles, extrayez les gadgets directement de la bibliothèque C partagée du système cible (`libc.so.6`) :
```bash
# Search gadgets in libc
ropper --file /lib/x86_64-linux-gnu/libc.so.6 --search "pop rdi; ret"

# Find "/bin/sh" string offset inside libc
strings -a -t x /lib/x86_64-linux-gnu/libc.so.6 | grep "/bin/sh"
```
*(Ajoutez les décalages découverts à l'adresse de base divulguée de la libc au moment de l'exécution).*

---

## ⚡ Tableau de référence rapide

| Objectif | Commande Ropper | Commande ROPgadget |
|-----------|----------------|-------------------|
| **Vider tous les gadgets** | `ropper --file <bin>` | `ROPgadget --binary <bin>` |
| **Rechercher un gadget spécifique** | `ropper --file <bin> --search "pop rdi"` | `ROPgadget --binary <bin> \| grep "pop rdi"` |
| **Filtrer les octets incorrects** | `ropper --file <bin> --badbytes 000a` | `ROPgadget --binary <bin> --badbytes "00\|0a"` |
| **Profondeur/longueur limite** | `ropper --file <bin> --inst-count 3` | `ROPgadget --binary <bin> --depth 3` |
| **Rechercher des gadgets JOP** | `ropper --file <bin> --type jop` | `ROPgadget --binary <bin> --norop` |
| **Chaîne ROP à construction automatique** | `ropper --file <bin> --chain "execve"`| `ROPgadget --binary <bin> --ropchain` |
| **Recherche d'écritures en mémoire** | `ropper --file <bin> --search "mov [???], ???"` | `ROPgadget --binary <bin> \| grep "mov \["` |

---

## 🛡️ Considérations défensives et atténuations

1. **Technologie d'application de flux de contrôle (Intel CET / Shadow Stack) :** Les processeurs modernes maintiennent une pile fantôme matérielle isolée qui vérifie les adresses de retour sur `ret`, mettant immédiatement fin aux chaînes ROP.
2. **Clang / GCC Control Flow Integrity (CFI) :** Valide les cibles du pointeur de fonction par rapport à un graphique avant avant les appels indirects.

---

## ⚠️ Mentions légales

> [!ATTENTION]
> Ropper et ROPgadget sont des utilitaires de développement d'exploits et d'évaluation de la sécurité des logiciels. Ils doivent strictement être utilisés dans le cadre de tests d’intrusion autorisés, de défis CTF ou de recherches de sécurité défensive. L’exploitation de logiciels sans autorisation explicite est illégale.