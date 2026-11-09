<!-- language-home-navigation -->
[🏠 Page d'accueil Française](../../README.fr.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🎯 pwndbg & GEF - Aide-mémoire complet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ██████╗ ██╗    ██╗███╗   ██╗██████╗ ██████╗  ██████╗      ██████╗ ███████╗███████╗
  ██╔══██╗██║    ██║████╗  ██║██╔══██╗██╔══██╗██╔════╝     ██╔════╝ ██╔════╝██╔════╝
  ██████╔╝██║ █╗ ██║██╔██╗ ██║██║  ██║██████╔╝██║  ███╗    ██║  ███╗█████╗  █████╗  
  ██╔═══╝ ██║███╗██║██║╚██╗██║██║  ██║██╔══██╗██║   ██║    ██║   ██║██╔══╝  ██╔══╝  
  ██║     ╚███╔███╔╝██║ ╚████║██████╔╝██████╔╝╚██████╔╝    ╚██████╔╝███████╗██║     
  ╚═╝      ╚══╝╚══╝ ╚═╝  ╚═══╝╚═════╝ ╚═════╝  ╚═════╝      ╚═════╝ ╚══════╝╚═╝     
         Next-Gen GDB Enhancements for Reversing & Binary Exploitation
```
<p align="center">
  <img src="https://img.shields.io/badge/pwndbg-GDB_Plugin-blue?style=for-the-badge" alt="pwndbg">
  <img src="https://img.shields.io/badge/GEF-GDB_Enhanced_Features-orange?style=for-the-badge" alt="GEF">
  <img src="https://img.shields.io/badge/Category-Binary_Exploitation_&_RE-green?style=for-the-badge" alt="Reversing & Pwn">
  <img src="https://img.shields.io/badge/License-MIT_%7C_FOSS-purple?style=for-the-badge" alt="Licence FOSS">
</p>

<p align="center">
  <b>🎯 Les principales améliorations du terminal GDB open source conçues pour l'ingénierie inverse binaire, l'analyse de la corruption de la mémoire, l'inspection de la disposition du tas et le développement d'exploits CTF</b>
</p>

---

## 📋 Table des matières

- [Présentation et pourquoi Vanilla GDB a besoin d'améliorations](#-overview--pourquoi-vanilla-gdb-needs-enhancements)
-[Installation et commutation entre les frameworks](#-installation--switching-between-frameworks)
  - [Installation de pwndbg](#installing-pwndbg)
  - [Installation de GEF](#installing-gef)
  - [Gestion de `.gdbinit`](#managing-gdbinit)
- [Le moteur d'affichage contextuel](#-the-context-display-engine)
  - [Section Registres](#registers-section)
  - [Démontage et prédictions de sauts](#disassembly--jump-predictions)
  - [Pile et télescope](#pile--télescope)
  - [Backtrace & Threads](#backtrace--threads)
- [Inspection et navigation de la mémoire](#-memory-inspection--navigation)
  -[Pointeurs télescopiques (`telescope`)](#telescoping-pointers-telescope)
  - [Mappages de mémoire de processus (`vmmap`)](#process-memory-mappings-vmmap)
  - [Recherche dans la mémoire (`search`)](#searching-memory-search)
- [Exploitation et inspection du tas](#-heap-exploitation--inspection)
  - [Affichage des morceaux et des arènes](#viewing-chunks--arenas)
  -[Inspection des bacs gratuits (Tcache, Fastbins, Smallbins)](#inspecting-free-bins-tcache-fastbins-smallbins)
  - [Visualisation de la disposition du tas (`vis_heap_chunks`)](#visualizing-heap-layout-vis_heap_chunks)
- [Exploiter les aides au développement](#-exploit-development-helpers)
  - [Checksec et audit d'atténuation](#checksec--mitigation-audit)
  - [Localisateur de pile Canary (`canary`)](#stack-canary-locator-canary)
  - [Modèles cycliques et calcul de décalage](#cyclic-patterns--offset-calculation)
  - [Chasse aux gadgets ROP en mémoire] (#chasse aux gadgets ROP en mémoire)
- [Tableau de référence rapide](#-quick-reference-table)
- [Considérations défensives et protections binaires](#-considérations-défensives--protections-binaires)
- [Mentions légales](#-legal-disclaimer)

---

## 🎯 Présentation et pourquoi Vanilla GDB a besoin d'améliorations

Le débogueur GNU standard (`gdb`) est un outil de puissance industrielle, mais son expérience utilisateur prête à l'emploi est visuellement austère. Il ne met pas automatiquement à jour les états des registres, ne déréférence pas les pointeurs multi-niveaux ou n'affiche pas les vues de la pile lors du parcours des instructions.

**pwndbg** et **GEF** transforment GDB en une suite d'élite de rétro-ingénierie et d'exploitation binaire :
- **pwndbg:** Spécialement conçu pour l'exploitation de logiciels de bas niveau, le débogage de tas (glibc 2.27 - 2.39+ internes de l'allocateur) et le déréférencement automatisé de pointeur.
- **GEF (GDB Enhanced Features) :** Un seul script Python autonome sans dépendance externe, offrant des fonctionnalités indépendantes de l'architecture (ARM, MIPS, PowerPC, x86) et une analyse de structure dynamique.

---

## 📥 Installation et basculement entre les frameworks

### Installation de pwndbg
```bash
git clone https://github.com/pwndbg/pwndbg
cd pwndbg
./setup.sh
```
### Installation du FEM
```bash
# Automated one-liner download
bash -c "$(curl -fsSL https://gef.blah.cat/sh)"
```
### Gestion de `.gdbinit`
Pour basculer entre pwndbg et GEF sans conflit, configurez votre `~/.gdbinit` :
```bash
# To use pwndbg:
echo "source /path/to/pwndbg/gdbinit.py" > ~/.gdbinit

# To use GEF:
echo "source ~/.gdbinit-gef.py" > ~/.gdbinit
```
---

## 🖥️ Le moteur d'affichage contextuel

Chaque fois que l'exécution s'arrête à un point d'arrêt ou à une étape, pwndbg et GEF affichent une vue de terminal automatisée à plusieurs panneaux :
```text
──────────────────────────[ REGISTERS / FLAGS ]──────────────────────────
RAX: 0x0 
RBX: 0x7fffffffe000 
RCX: 0x7ffff7ec2000 --> 0x00010102464c457f 
RDX: 0x7fffffffe1a0 --> 0x7fffffffe494 ('USER=zeus')
RSI: 0x7fffffffe198 --> 0x7fffffffe47e ('/bin/target')
RDI: 0x1 
RBP: 0x7fffffffe0a0 --> 0x7fffffffe0c0 --> 0x0
RSP: 0x7fffffffe080 --> 0x401156 (<main+34>: mov eax, 0)
RIP: 0x401140 (<main+12>: cmp QWORD PTR [rbp-0x8], 0x0)
──────────────────────────[ DISASM / CODE ]──────────────────────────
   0x401135 <main+1>     push   rbp
   0x401136 <main+2>     mov    rbp, rsp
   0x401139 <main+5>     sub    rsp, 0x10
 ► 0x401140 <main+12>    cmp    QWORD PTR [rbp-0x8], 0
   0x401145 <main+17>    je     0x401151 <main+29>  <-- JUMP IS TAKEN
   0x401147 <main+19>    mov    eax, 0x1
──────────────────────────[ STACK ]──────────────────────────
00:0000│ rsp 0x7fffffffe080 --> 0x401156 (<main+34>: mov eax, 0)
01:0008│     0x7fffffffe088 --> 0x0
02:0010│ rbp 0x7fffffffe0a0 --> 0x7fffffffe0c0 --> 0x0
03:0018│     0x7fffffffe0a8 --> 0x7ffff7df6d90 (<__libc_start_call_main+128>: mov edi, eax)
──────────────────────────[ BACKTRACE ]──────────────────────────
 ► 0   0x401140 main+12
   1   0x7ffff7df6d90 __libc_start_call_main+128
   2   0x7ffff7df6e40 __libc_start_main+128
   3   0x401065 _start+37
```
---

## 🔍 Inspection et navigation de la mémoire

### Pointeurs télescopiques (`telescope`)
Déréférence les pointeurs de mémoire de manière récursive, affichant du texte ASCII, des symboles de fonction ou des pointeurs cibles :
```text
pwndbg> telescope $rsp 16       # Dereference 16 entries starting at RSP
pwndbg> telescope 0x404000 8    # Dereference data section
```
### Mappages de mémoire de processus (`vmmap`)
Affiche des segments de mémoire virtuelle complets avec des autorisations de lecture/écriture/exécution :
```text
pwndbg> vmmap
```
*Repérez rapidement les segments avec les autorisations `rwx` (vital pour l'injection de shellcode).*

### Recherche de mémoire (`search`)
Localisez des chaînes, des entiers ou des pointeurs n’importe où dans les pages mémoire chargées :
```text
pwndbg> search -s "/bin/sh"     # Search for string "/bin/sh"
pwndbg> search -p 0x401135      # Search for pointers to main()
pwndbg> search -x "41414141"    # Search for raw hexadecimal bytes
```
---

## 🧱 Exploitation et inspection du tas

L'analyse des allocateurs de tas `glibc` modernes est simple grâce aux commandes de tas dédiées :

### Affichage des morceaux et des arènes
```text
pwndbg> heap                    # Display entire heap layout
pwndbg> chunk 0x602000          # Parse chunk headers (size, flags: PREV_INUSE, IS_MMAPPED, NON_MAIN_ARENA)
pwndbg> arenas                  # List all active heap arenas
```
### Inspection des bacs gratuits (Tcache, Fastbins, Smallbins)
```text
pwndbg> bins                    # Show all free bins simultaneously
pwndbg> tcache                  # Display per-thread tcache entries
pwndbg> fastbins                # List single-linked fastbins
pwndbg> unsortedbin             # List unsorted bin chunks (used for libc leak)
```
### Visualisation de la disposition du tas (`vis_heap_chunks`)
```text
pwndbg> vis_heap_chunks 10      # Draw color-coded visual representation of consecutive heap chunks
```
---

## 🛠️ Exploitez les aides au développement

### Audit de sécurité et d'atténuation
Vérifiez les atténuations de sécurité du compilateur directement à partir de l'invite GDB :
```text
pwndbg> checksec
```
*Inspecte :*
- **RELRO :** RELRO partiel ou complet (protège la table de compensation globale `.got.plt`)
- **Pile :** Canari trouvé ou aucun canari
- **NX :** NX activé (pile/tas non exécutable)
- **PIE :** PIE activé (Position Independent Executable / ASLR)

### Localisateur Canary de pile (`canary`)
Localisez la valeur canari du thread dans le stockage local de thread (TLS) et vérifiez où elle réside sur le cadre de pile actuel :
```text
pwndbg> canary
```
### Modèles cycliques et calcul de décalage
Déterminez le décalage exact pour écraser le pointeur d’instruction (EIP/RIP) :
```text
# Generate a 100-byte De Bruijn cyclic sequence
pwndbg> cyclic 100

# After the crash (SIGSEGV), calculate offset from faulty address or register value
pwndbg> cyclic -l $rip
# Output: Finding cyclic pattern at offset 72
```
### Recherche de gadgets ROP en mémoire
Recherchez des segments exécutables chargés pour des gadgets spécifiques :
```text
pwndbg> rop --grep "pop rdi"
```
---

## ⚡ Tableau de référence rapide

| Objectif | Commande |
|-----------|---------|
| **Commencez avec pwndbg/GEF**| `gdb ./target_binary` |
| **Inspecter les registres/pile** | `context` |
| **Deref de pointeur récursif**| `telescope $rsp 10` |
| **Carte mémoire et vérification RWX**| `vmmap` |
| **Chaîne de recherche en mémoire**| `search -s "/bin/sh"` |
| **Atténuations d'audit** | `checksec` |
| **Trouver Stack Canary** | `canary` |
| **Présentation des fragments de tas** | `heap` |
| **Inspectez les bacs gratuits** | `bins` |
| **Calculer le décalage de la mémoire tampon**| `cyclic 100` -> `cyclic -l <val>` |
| **Définir le point d'arrêt au symbole**| `b *main` |
| **Exécuter/Continuer/Étape** | `r` / `c` / `si` / `ni` |

---

## 🛡️ Considérations défensives et protections binaires

1. **Full RELRO :** La compilation avec `-Wl,-z,relro,-z,now` rend `.got` entièrement en lecture seule, éliminant ainsi les attaques par écrasement GOT.
2. **Stack Canaries :** La compilation avec `-fstack-protector-all` place les cookies avant les adresses de retour, provoquant `__stack_chk_fail` en cas de dépassement de tampon.
3. **PIE & ASLR :** Randomise les adresses de base du code, du tas et de la bibliothèque à chaque exécution.

---

## ⚠️ Mentions légales

> [!ATTENTION]
> pwndbg et GEF sont des outils d'analyse binaire et d'exploitation destinés strictement à la recherche de vulnérabilités autorisée, à l'audit défensif de logiciels et aux compétitions académiques CTF. L'exploitation d'un logiciel sans autorisation est illégale.