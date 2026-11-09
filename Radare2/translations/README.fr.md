<!-- language-home-navigation -->
[🏠 Page d'accueil Française](../../README.fr.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ⚡ Radare2 (r2) - Aide-mémoire complet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ██████╗  █████╗ ██████╗  █████╗ ██████╗ ███████╗██████╗ 
  ██╔══██╗██╔══██╗██╔══██╗██╔══██╗██╔══██╗██╔════╝╚════██╗
  ██████╔╝███████║██║  ██║███████║██████╔╝█████╗   █████╔╝
  ██╔══██╗██╔══██║██║  ██║██╔══██║██╔══██╗██╔══╝  ██╔═══╝ 
  ██║  ██║██║  ██║██████╔╝██║  ██║██║  ██║███████╗███████╗
  ╚═╝  ╚═╝╚═╝  ╚═╝╚═════╝ ╚═╝  ╚═╝╚═╝  ╚═╝╚══════╝╚══════╝
      UNIX-like Command-Line Reverse Engineering Framework
```
<p align="center">
  <img src="https://img.shields.io/badge/Radare2-CLI_Reversing-blue?style=for-the-badge" alt="Radare2">
  <img src="https://img.shields.io/badge/Author-Sergi_Álvarez-orange?style=for-the-badge" alt="pancake">
  <img src="https://img.shields.io/badge/Category-Reverse_Engineering-green?style=for-the-badge" alt="Ingénierie inverse">
  <img src="https://img.shields.io/badge/License-LGPLv3-purple?style=for-the-badge" alt="LGPLv3">
</p>

<p align="center">
  <b>⚡ Framework puissant, open source, basé sur un terminal pour l'analyse binaire, le désassemblage, le débogage, l'édition hexadécimale et l'application de correctifs binaires</b>
</p>

---

## 📋 Table des matières

- [Vue d'ensemble et architecture](#-overview--architecture)
- [Installation](#-installation)
- [Ouverture des fichiers et des modes de session](#-opening-files--session-modes)
- [Commandes d'analyse binaire (`a`)](#-binary-analysis-commands-a)
- [Navigation et recherche (`s`)](#-navigation--seeking-s)
-[Impression et démontage (`p`)](#-printing--disassembly-p)
- [Modes visuels et graphiques interactifs (`V` et `VV`)](#-interactive-visual--graph-modes-v--vv)
- [Correction et modification binaires (`w`)](#-binary-patching--modification-w)
- [L'ensemble d'outils compagnon Radare2](#-the-radare2-companion-toolset)
  - [rabin2 : Extraction d'informations binaires](#rabin2-binary-information-extraction)
  - [rasm2 : Assembleur et désassembleur](#rasm2-assembler--désassembleur)
  - [radiff2 : Moteur de différence binaire](#radiff2-binary-difference-engine)
  - [rafind2 : recherche d'octets et de chaînes](#rafind2-byte--string-search)
  - [rax2 : Convertisseur de base et numérique](#rax2-base--numeric-converter)
- [Bases du débogage avec r2 (`-d`)](#-debugging-basics-with-r2--d)
- [Tableau de référence rapide](#-quick-reference-table)
- [Considérations défensives et anti-inversion](#-considérations-defensives--anti-inversion)
- [Mentions légales](#-legal-disclaimer)

---

## 🎯 Présentation et architecture

Créé par **Sergi Álvarez (pancake)**, **Radare2** (`r2`) est un framework d'ingénierie inverse portable et modulaire entièrement construit autour de la philosophie UNIX. Il s'exécute entièrement dans votre terminal sans nécessiter de lourdes dépendances graphiques :
- **Prise en charge complète de l'architecture :** x86/x64, ARM, ARM64, MIPS, PowerPC, AVR, SPARC, WebAssembly, Java, Dalvik et EVM.
- **Utilitaires CLI Composable :** Livré avec des outils de ligne de commande spécialisés (`rabin2`, `rasm2`, `radiff2`, `rafind2`, `rax2`).
- **Modes visuels interactifs :** Éditeurs hexadécimaux en écran partagé, graphiques de flux de contrôle ASCII, moniteurs de registre et vues de décompilateur.
- **Scriptable et extensible :** S'intègre à Python, r2pipe, Ghidra (`r2ghidra`) et Rizin.

---

## 📥Installation
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y radare2

# Arch Linux
sudo pacman -S radare2

# Install latest Git master (Recommended for most features)
git clone https://github.com/radareorg/radare2.git
cd radare2 && sys/install.sh

# Install r2ghidra decompiler plugin
r2pm update
r2pm -i r2ghidra
```
---

## 🚀 Ouverture des fichiers et des modes de session
```bash
# Open binary in read-only mode
r2 /bin/ls

# Open binary in read-write mode (required for patching)
r2 -w target.bin

# Open binary in debugger mode
r2 -d target.bin

# Open binary without parsing headers (raw flat binary / shellcode)
r2 -b 32 -n /path/to/raw.bin

# Open in quiet mode (no greeting banner)
r2 -q target.bin
```
---

## 🔬 Commandes d'analyse binaire (`a`)

Dans le shell `r2`, les commandes sont constituées de lettres simples ou de préfixes courts. Vous pouvez ajouter `?` à n'importe quelle commande pour afficher l'aide en ligne :
```text
[0x00000000]> a?        # Show analysis help
[0x00000000]> aa        # Basic analysis (symbols, basic blocks)
[0x00000000]> aaa       # Deep analysis (functions, cross-references, calls)
[0x00000000]> aaaa      # Experimental auto-analysis (takes longer)

[0x00000000]> afl       # List all identified functions
[0x00000000]> afi       # Display function info at current address
[0x00000000]> afvn      # Rename local variable: afvn <new_name> <old_name>
[0x00000000]> axt <addr># Find cross-references (xrefs) TO address
[0x00000000]> axf <addr># Find cross-references (xrefs) FROM address
```
---

## 🧭 Navigation et recherche (`s`)

La commande seek modifie l'adresse actuelle du curseur :
```text
[0x00000000]> s main        # Seek to main function
[0x00401122]> s 0x401200    # Seek to absolute address
[0x00401200]> s+ 32         # Seek 32 bytes forward
[0x00401220]> s- 16         # Seek 16 bytes backward
[0x00401210]> s/ hit0_0     # Seek to search hit
[0x00401210]> u             # Undo seek (go back to previous address)
[0x00401200]> U             # Redo seek
```
---

## 📜 Impression & Démontage (`p`)
```text
[0x00401122]> pd 10     # Disassemble 10 instructions
[0x00401122]> pdf       # Disassemble entire current function
[0x00401122]> pdc       # Decompile function using r2ghidra plugin
[0x00401122]> px 64     # Print 64 bytes in hex dump format
[0x00401122]> ps        # Print string at current address
[0x00401122]> pz        # Print zero-terminated string
[0x00401122]> pi 5      # Print 5 instructions (concise format)
```
---

## 🖥️ Modes visuels et graphiques interactifs (`V` et `VV`)

En appuyant sur `V` ou `VV`, Radare2 se transforme en une riche application de terminal interactif :

- **Entrez en mode visuel :** Tapez `V`
  - Appuyez sur `p` / `P` pour faire défiler les vues (Hex dump -> Désassemblage -> Débogueur -> Décompilateur).
  - Appuyez sur `c` pour basculer en mode curseur (utilisez les touches fléchées ou `hjkl` pour sélectionner les octets).
  - Appuyez sur `d` pour redéfinir les octets sous forme de code, de données ou de chaînes.
  - Appuyez sur `q` pour revenir à l'invite standard.
- **Entrez dans la vue graphique :** Tapez `VV`
  - Rend le graphique de flux de contrôle ASCII (CFG).
  - Utilisez `Tab` / `Shift+Tab` pour passer d'un nœud de branche conditionnel à l'autre.
  - Appuyez sur `t` pour suivre la vraie branche ; `f` pour suivre la fausse branche.
  - Appuyez sur `u` pour sortir d'une branche.

---

## ✍️ Correctifs et modifications binaires (`w`)

Pour modifier un binaire sur le disque, assurez-vous que r2 est ouvert en mode écriture (`r2 -w target` ou tapez `oo+` dans une session active) :
```text
[0x00401150]> oo+                           # Re-open current binary with write permissions
[0x00401150]> wa nop                        # Assemble 'nop' instruction at current address
[0x00401150]> wa jmp 0x401190               # Patch a jump instruction
[0x00401150]> wx 90909090                   # Write hex bytes (4 NOPs)
[0x00401150]> w "Modified string\x00"       # Overwrite string with null-terminator
[0x00401150]> w0 16                         # Zero-fill 16 bytes (NUL padding)
```
---

## 🛠️ L'ensemble d'outils compagnon Radare2

Radare2 comprend des utilitaires CLI autonomes :

### rabin2 : Extraction d'informations binaires
```bash
# Print general binary architecture, bits, and security mitigations (NX, PIE, Canary)
rabin2 -I target.bin

# List imported symbols and functions (libc calls)
rabin2 -i target.bin

# List exported symbols
rabin2 -E target.bin

# Extract ASCII & UTF-16 strings
rabin2 -z target.bin

# List sections (.text, .data, .rodata)
rabin2 -S target.bin
```
### rasm2 : Assembleur et désassembleur
```bash
# Assemble x64 instructions into hexadecimal opcode
rasm2 -a x86 -b 64 "xor eax, eax; ret"
# Output: 31c0c3

# Disassemble hex opcodes into human-readable assembly
rasm2 -a x86 -b 64 -d "31c0c3"
# Output:
# xor eax, eax
# ret
```
### radiff2 : Moteur de différence binaire
```bash
# Compare two binaries and count differing bytes
radiff2 -c original.bin patched.bin

# Detailed code comparison graph
radiff2 -g main original.bin patched.bin | xdot -
```
### rafind2 : recherche d'octets et de chaînes
```bash
# Search for ASCII string in file
rafind2 -s "admin_password" target.bin

# Search for hexadecimal pattern
rafind2 -x "4889e5" target.bin
```
### rax2 : Convertisseur de base et numérique
```bash
# Convert hex to decimal
rax2 0x1337      # Output: 4919

# Convert decimal to hex
rax2 4919        # Output: 0x1337

# Convert string to hex
rax2 -S "admin"  # Output: 61646d696e
```
---

## 🐛 Bases du débogage avec r2 (`-d`)
```bash
# Start debugging binary with arguments
r2 -d target.bin arg1 arg2
```
À l'intérieur du shell du débogueur :
```text
[0x7f112233]> db main       # Set software breakpoint at main
[0x7f112233]> db 0x401150   # Set breakpoint at specific address
[0x7f112233]> dbl           # List all active breakpoints
[0x7f112233]> dc            # Continue execution until breakpoint
[0x7f112233]> ds            # Step into (single instruction)
[0x7f112233]> dso           # Step over function calls
[0x7f112233]> dr            # Print all CPU registers
[0x7f112233]> dr rax        # Print specific register value
[0x7f112233]> dr rax = 0x1  # Modify register value
```
---

## ⚡ Tableau de référence rapide

| Objectif | Commande |
|-----------|---------|
| **Analyse approfondie** | `aaa` |
| **Fonctions de liste** | `afl` |
| **Rechercher une fonction/adresse** | `s <addr>` / `s main` |
| **Fonction de démontage** | `pdf` |
| **Fonction de décompilation (r2ghidra)** | `pdc` |
| **Démontage visuel interactif** | `V` |
| **Graphique de flux ASCII** | `VV` |
| **Instructions d'assemblage** | `wa <instruction>` |
| **Écrire des octets hexadécimaux** | `wx <bytes>` |
| **Réouvrir en lecture-écriture** | `oo+` |
| **Extraire les informations binaires** | `rabin2 -I <binary>` |
| **Assembler CLI** | `rasm2 -a x86 -b 64 "<asm>"` |

---

## 🛡️ Considérations défensives et anti-recul

1. **Détection anti-débogage :** Le débogueur de Radare2 s'appuie sur le standard Linux `ptrace`. Les binaires dotés d'auto-vérifications `ptrace(PTRACE_TRACEME)` peuvent bloquer les pièces jointes à moins qu'ils ne soient corrigés.
2. **Obfuscation et aplatissement du flux de contrôle :** Les binaires fortement obscurcis (OLLVM) confondent la récupération de bloc de base. Utilisez `aaaa` ou des moteurs d'exécution symbolique (comme angr) pour simplifier les fonctions aplaties.

---

## ⚠️ Mentions légales

> [!ATTENTION]
> Radare2 est une boîte à outils d'ingénierie inverse et d'analyse destinée strictement aux évaluations de sécurité autorisées, à l'analyse des logiciels malveillants, à la recherche de vulnérabilités et à une utilisation éducative personnelle. L'ingénierie inverse des logiciels propriétaires sans autorisation peut violer les accords de licence de l'utilisateur final et les lois sur les droits d'auteur.