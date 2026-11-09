<!-- language-home-navigation -->
[🏠 Page d'accueil Française](../../README.fr.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🔬 strace & ltrace - Aide-mémoire complet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ███████╗████████╗██████╗  █████╗  ██████╗███████╗    ██╗  ████████╗██████╗  █████╗  ██████╗███████╗
  ██╔════╝╚══██╔══╝██╔══██╗██╔══██╗██╔════╝██╔════╝    ██║  ╚══██╔══╝██╔══██╗██╔══██╗██╔════╝██╔════╝
  ███████╗   ██║   ██████╔╝███████║██║     █████╗      ██║     ██║   ██████╔╝███████║██║     █████╗  
  ╚════██║   ██║   ██╔══██╗██╔══██║██║     ██╔══╝      ██║     ██║   ██╔══██╗██╔══██║██║     ██╔══╝  
  ███████║   ██║   ██║  ██║██║  ██║╚██████╗███████╗    ███████╗██║   ██║  ██║██║  ██║╚██████╗███████╗
  ╚══════╝   ╚═╝   ╚═╝  ╚═╝╚═╝  ╚═╝ ╚═════╝╚══════╝    ╚══════╝╚═╝   ╚═╝  ╚═╝╚═╝  ╚═╝ ╚═════╝╚══════╝
            Linux Dynamic Binary Analysis: System & Library Call Tracers
```
<p align="center">
  <img src="https://img.shields.io/badge/strace-System_Calls-blue?style=for-the-badge" alt="strace">
  <img src="https://img.shields.io/badge/ltrace-Library_Calls-orange?style=for-the-badge" alt="ltrace">
  <img src="https://img.shields.io/badge/Platform-Linux_ELF-green?style=for-the-badge" alt="Linux">
  <img src="https://img.shields.io/badge/Category-Dynamic_Analysis-purple?style=for-the-badge" alt="Analyse dynamique">
</p>

<p align="center">
  <b>🔬 Utilitaires de ligne de commande essentiels pour l'analyse binaire dynamique : intercepter les appels système du noyau Linux (strace) et les appels de bibliothèque dynamique de l'espace utilisateur (ltrace)</b>
</p>

---

## 📋 Table des matières

- [Présentation : appels système et appels de bibliothèque](#-overview-system-calls-vs-library-calls)
- [Installation](#-installation)
- [strace : Suivi des appels système](#-strace-system-call-tracing)
  - [Exécution de traçage de base](#basic-tracing-execution)
  - [Filtrage des appels système par classe ou nom (`-e`)](#filtering-syscalls-by-class-or-name--e)
  -[Traçage des processus enfants et multithreads (`-f`)](#tracing-child--multi-threaded-processes--f)
  - [Attachement aux processus en cours d'exécution (`-p`)](#attachement aux processus en cours d'exécution--p)
  - [Extension de chaînes et de sorties tampon (`-s`)](#expanding-strings--buffer-outputs--s)
  -[Profilage statistique et analyse des goulots d'étranglement (`-c`)](#statistical-profiling--bottleneck-analysis--c)
  -[Injection de défauts pour le fuzzing et les tests (`-e fault`)](#fault-injection-for-fuzzing--testing--e-fault)
- [ltrace : suivi des appels de bibliothèque dynamique](#-ltrace-dynamic-library-call-tracing)
  - [Interception de base de la bibliothèque](#basic-library-interception)
  - [Cracking des contrôles de mots de passe codés en dur (`strcmp`)](#cracking-hardcoded-password-checks-strcmp)
  - [Démangling des symboles C++ (`-C`)](#demangling-c-symbols--c)
  -[Filtrer les fonctions spécifiques de la bibliothèque](#filtering-special-library-functions)
- [Combinaison des workflows strace et ltrace](#-combining-strace-and-ltrace-workflows)
- [Tableau de référence rapide](#-quick-reference-table)
- [Considérations défensives et anti-traçage](#-considérations-defensives--anti-traçage)
- [Mentions légales](#-legal-disclaimer)

---

## 🎯 Présentation : appels système et appels de bibliothèque

Lors de l'analyse des binaires Linux ELF compilés, l'analyse statique peut être entravée par l'obscurcissement, le décapage ou le compactage. Le traçage dynamique offre une visibilité immédiate sur les actions d'exécution :

1. **Appels système (`strace`) :**
   - L'interface entre les applications de l'espace utilisateur et le noyau Linux Linux.
   - Trace les E/S de disque (`openat`, `read`, `write`), la mise en réseau (`socket`, `connect`), l'allocation de mémoire (`mmap`, `brk`) et la gestion des processus (`fork`, `execve`, `ptrace`).
2. **Appels à la bibliothèque (`ltrace`) :**
   - L'interface entre les bibliothèques partagées binaires et dynamiques (telles que `libc.so`, `libssl.so`).
   - Intercepte les appels tels que `strcmp()`, `strlen()`, `malloc()`, `system()` et les routines cryptographiques, exposant fréquemment les informations d'identification en texte clair en mémoire avant le cryptage.

---

## 📥Installation
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y strace ltrace

# Arch Linux
sudo pacman -S strace ltrace

# Fedora / Red Hat / CentOS
sudo dnf install -y strace ltrace
```
---

## ⚡ strace : suivi des appels système

### Exécution de traçage de base
Exécutez un binaire cible et diffusez tous les appels système vers l'erreur standard :
```bash
strace ./target_binary
```
Redirigez la sortie vers un fichier afin qu'elle n'encombre pas l'exécution du programme :
```bash
strace -o trace.log ./target_binary
```
### Filtrage des appels système par classe ou nom (`-e`)
Évitez le bruit en filtrant uniquement les classes d'appels système pertinentes :
```bash
# Trace ONLY file-related operations (open, read, write, stat, access)
strace -e trace=file ./target_binary

# Trace ONLY network socket connections and data transfers
strace -e trace=network ./target_binary

# Trace process execution and fork/clone operations
strace -e trace=process ./target_binary

# Filter specific syscalls by exact name
strace -e trace=open,openat,read,write ./target_binary
```
### Traçage des processus enfants et multithreads (`-f`)
Les binaires et les logiciels malveillants modernes engendrent des processus ou des threads de travail enfants. Sans `-f`, Strace les manquera :
```bash
# Trace all child forks and threads
strace -f ./target_binary

# Trace children and save each process output into separate files (trace.pid)
strace -ff -o trace ./target_binary
```
### Attachement aux processus en cours d'exécution (`-p`)
Surveillez un service ou un processus de malware actif sans le redémarrer :
```bash
# Attach by Process ID (PID)
sudo strace -p 1337

# Attach to process and follow all newly spawned threads
sudo strace -f -p 1337 -o attached.log
```
### Extension des chaînes et des sorties tampon (`-s`)
Par défaut, strace tronque les chaînes imprimées à 32 caractères. Augmentez la longueur de la chaîne pour inspecter les chemins de fichiers complets, les URL ou les charges utiles :
```bash
# Display up to 2048 characters of strings and buffers
strace -s 2048 -e trace=read,write ./target_binary
```
### Profilage statistique et analyse des goulots d'étranglement (`-c`)
Comptez le temps passé, le nombre d'appels et les erreurs par appel système :
```bash
strace -c ./target_binary
```
*Exemple de résumé de sortie :*
```text
% time     seconds  usecs/call     calls    errors syscall
------ ----------- ----------- --------- --------- ----------------
 54.32    0.001200          40        30           read
 32.10    0.000710          71        10         2 openat
 13.58    0.000300          15        20           write
```
### Injection de défauts pour le fuzzing et les tests (`-e fault`)
Injectez des erreurs arbitraires dans les appels système pour évaluer la résilience :
```bash
# Force the 3rd invocation of openat() to fail with Permission Denied (EACCES)
strace -e fault=openat:error=EACCES:when=3 ./target_binary
```
---

## 🧩 ltrace : traçage dynamique des appels de la bibliothèque

### Interception de base de la bibliothèque
Intercepter les appels effectués vers les bibliothèques dynamiques `.so` :
```bash
ltrace ./target_binary
```
### Crack des contrôles de mots de passe codés en dur (`strcmp`)
Dans l'ingénierie inverse des CTF et des binaires de défi, les fonctions de comparaison de mots de passe (`strcmp`, `strncmp`, `memcmp`) révèlent le mot de passe cible dans des arguments en texte brut :
```bash
ltrace -s 128 -e strcmp,strncmp ./license_check
```
*Exemple de sortie :*
```text
strcmp("user_input", "SUPER_SECRET_FLAG_2026") = -1
```
### Démêlage des symboles C++ (`-C`)
Démêler les noms de méthodes C++ mutilés par le compilateur (`_ZNK4Test3fooEv` -> `Test::foo()`) :
```bash
ltrace -C ./cpp_binary
```
### Filtrage des fonctions spécifiques de la bibliothèque
```bash
# Intercept only memory allocation and string functions
ltrace -e malloc+free+strdup ./target_binary

# Intercept calls originating from specific dynamic libraries
ltrace -l /lib/x86_64-linux-gnu/libcrypto.so.3 ./target_binary
```
---

## 🔄 Combiner les workflows strace et ltrace

Lors d'un tri initial sur un binaire inconnu :

1. **Étape 1 :** Exécutez `strace -e trace=file,network ./binary` pour voir avec quels fichiers et sockets il interagit.
2. **Étape 2 :** Exécutez `ltrace -s 256 ./binary` pour découvrir les comparaisons de chaînes de bibliothèque internes, les appels de chiffrement et la logique de la mémoire.
3. **Étape 3 :** Combinez les deux outils :
   ```bash
   ltrace -S ./binary
   ```
*(L'indicateur `-S` indique à ltrace d'afficher les appels système aux côtés des appels de bibliothèque dans une seule vue unifiée).*

---

## ⚡ Tableau de référence rapide

| Objectif | Commande |
|-----------|---------|
| **Tracez tous les appels système** | `strace ./binary` |
| **Se connecter au fichier** | `strace -o log.txt ./binary` |
| **Opérations sur les fichiers de trace** | `strace -e trace=file ./binary` |
| **Trace l'activité du réseau** | `strace -e trace=network ./binary` |
| **Suivez les processus enfants** | `strace -f ./binary` |
| ** Attacher au PID en cours d'exécution ** | `sudo strace -f -p <PID>` |
| **Agrandir la longueur de la chaîne** | `strace -s 2048 ./binary` |
| **Statistiques récapitulatives des appels système** | `strace -c ./binary` |
| **Tracer les appels de la bibliothèque** | `ltrace ./binary` |
| **Attrapez la vérification du mot de passe** | `ltrace -s 128 -e strcmp,strncmp ./binary` |
| **Démêler les noms C++** | `ltrace -C ./binary` |
| ** Libération combinée + Syscall ** | `ltrace -S ./binary` |

---

## 🛡️ Considérations défensives et anti-traçage

1. **Ptrace Self-Checking :** Linux permet à un seul traceur de s'attacher à un processus via `ptrace`. L’appel de `ptrace(PTRACE_TRACEME, 0, 1, 0)` échouera si `strace` ou `ltrace` est déjà connecté.
2. **Liaison statique (`-static`) :** Les binaires compilés avec une liaison statique ne contiennent aucune référence de bibliothèque dynamique (`DT_NEEDED`), ce qui rend `ltrace` complètement inefficace (utilisez plutôt `strace`).

---

## ⚠️ Mentions légales

> [!ATTENTION]
> strace et ltrace sont des outils de diagnostic dynamiques conçus pour le développement de logiciels, le débogage et les évaluations de sécurité autorisées. Il est strictement interdit de tracer des processus non autorisés ou d'intercepter des communications exclusives sans autorisation.