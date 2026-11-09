<!-- language-home-navigation -->
[🏠 Page d'accueil Française](../../README.fr.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ⌨️ rlwrap - Aide-mémoire complet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ██████╗ ██╗     ██╗    ██╗██████╗  █████╗ ██████╗ 
  ██╔══██╗██║     ██║    ██║██╔══██╗██╔══██╗██╔══██╗
  ██████╔╝██║     ██║ █╗ ██║██████╔╝███████║██████╔╝
  ██╔══██╗██║     ██║███╗██║██╔══██╗██╔══██║██╔═══╝ 
  ██║  ██║███████╗╚███╔███╔╝██║  ██║██║  ██║██║     
  ╚═╝  ╚═╝╚══════╝ ╚══╝╚══╝ ╚═╝  ╚═╝╚═╝  ╚═╝╚═╝     
       Readline Wrapper for Reverse Shells & Dumb Terminals
```
<p align="center">
  <img src="https://img.shields.io/badge/rlwrap-Readline_Wrapper-blue?style=for-the-badge" alt="rlwrap">
  <img src="https://img.shields.io/badge/Utility-Shell_Stabilization-green?style=for-the-badge" alt="Stabilisation de la coque">
  <img src="https://img.shields.io/badge/Category-Exploitation_&_Shells-orange?style=for-the-badge" alt="Exploitation et obus">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="Licence GPLv2">
</p>

<p align="center">
  <b>🌐 Transformez les écouteurs Netcat bruts et les shells inversés stupides en interfaces de ligne de commande complètes avec touches fléchées, historique et saisie semi-automatique</b>
</p>

---

## 📋 Table des matières

- [Introduction : Le problème du "Dumb Shell"](#-introduction-the-dumb-shell-problem)
- [Installation](#-installation)
- [Utilisation de base](#-basic-usage)
- [Attraper des coques inversées (`rlwrap + nc`)](#-catcher-reverse-shells-rlwrap--nc)
- [Gestion de l'historique persistant](#-persistent-history-management)
- [Commande d'auto-complétion (`-f`)](#-command-auto-completion--f)
- [Workflow complet de mise à niveau du shell TTY en 5 étapes] (#-full-5-step-tty-shell-upgrade-workflow)
-[Utilisation de rlwrap avec d'autres outils Pentest](#-using-rlwrap-with-other-pentest-tools)
  - [Oracle SQL*Plus](#oracle-sqlplus)
  - [Redis et Telnet](#redis--telnet)
  - [Socat et sockets personnalisés](#socat--custom-sockets)
- [Tableau de référence rapide](#-quick-reference-table)
- [Conseils et bonnes pratiques](#-conseils--meilleures pratiques)
- [Mentions légales](#-legal-disclaimer)

---

## 🎯 Introduction : Le problème du "Dumb Shell"

Lorsque vous interceptez un shell inversé à l'aide d'outils traditionnels comme Netcat (`nc -lvnp 4444`), vous obtenez un **terminal stupide**. Un terminal stupide ne dispose pas de capacités d'édition de ligne standard :

- ❌ Appuyer sur les **touches fléchées haut/bas** imprime les séquences d'échappement comme `^[[A` ou `^[[B` au lieu de parcourir l'historique des commandes.
- ❌ Appuyer sur **Retour arrière** ou **Supprimer** imprime `^H` ou `^?` au lieu de supprimer des caractères.
- ❌ Appuyer sur **Ctrl + C** tue immédiatement l'écouteur du shell inversé, perdant ainsi l'accès durement gagné.
- ❌ Les commandes telles que `su`, `sudo`, `nano`, `vim` ou `passwd` échouent car aucun pseudo-terminal (PTY) n'est connecté.
- ❌ Aucun historique de commandes persistant n'est conservé d'une session à l'autre.

**rlwrap** (Readline Wrapper, écrit par Hans Lub) résout ce problème en s'asseyant entre votre terminal et la commande cible. Il intercepte les frappes au clavier, exploite **GNU Readline** et fournit une navigation par touches fléchées, une édition de ligne, des fichiers d'historique de commandes persistants et une saisie semi-automatique basée sur un dictionnaire !

---

## 📥Installation

`rlwrap` est léger et disponible dans les référentiels de packages par défaut de pratiquement toutes les distributions Linux :

### Kali Linux, Debian et Ubuntu
```bash
# Update and install rlwrap
sudo apt update && sudo apt install -y rlwrap

# Verify installation and version
rlwrap --version
```
### Fedora, CentOS et RHEL
```bash
# Fedora / CentOS / RHEL
sudo dnf install -y rlwrap || sudo yum install -y epel-release && sudo yum install -y rlwrap
```
### Arch Linux
```bash
# Arch Linux / Manjaro
sudo pacman -S rlwrap
```
### macOS (Homebrew)
```bash
# macOS
brew install rlwrap
```
---

## 🚀 Utilisation de base

La syntaxe de base consiste simplement à encapsuler n'importe quelle ligne de commande avec `rlwrap` :
```bash
# General syntax
rlwrap [options] <command> [command-arguments]
```
### Drapeaux communs

| Drapeau | Descriptif |
|------|-------------|
| `-c` | Activer la complétion des noms de fichiers et de chemins |
| `-A` | Ignorer les codes d'échappement de formatage des invites (évite les problèmes de ligne d'invite) |
| `-r` | Mémorisez tous les mots saisis lors de la saisie et placez-les dans la liste de mots de complétion |
| `-H <file>` | Spécifier un fichier historique dédié pour enregistrer et rappeler les commandes |
| `-s <num>` | Définir le nombre maximum de lignes d'historique (par défaut : 300) |
| `-f <file>` | Charger un fichier de dictionnaire de complétion personnalisé |
| `-b <chars>` | Définir les caractères de saut de mot |
| `-m` | Activer l'édition d'entrée multiligne |
| `-p[color]` | Coloriser l'invite de commande |

---

## 🎣 Attraper des obus inversés (`rlwrap + nc`)

Le cas d'utilisation le plus courant en matière de sécurité offensive consiste à encapsuler un écouteur Netcat (voir la [Cheatsheet Netcat](../Netcat/README.md) pour les variantes et indicateurs complets de l'écouteur) :
```bash
# Basic Netcat listener with arrow-key editing & history
rlwrap nc -lvnp 4444

# Professional pentester flags: -c (file completion), -A (prompt fix), -r (remember words)
rlwrap -cAr nc -lvnp 4444
```
### Explication des drapeaux `-cAr` :

1. **`-c` (achèvement de fichiers) :** Permet d'utiliser `<TAB>` pour compléter automatiquement les noms de fichiers et de répertoires.
2. **`-A` (Invites) :** Empêche les retours à la ligne étranges et la duplication d'invite lorsque le serveur distant envoie des chaînes d'invite non standardisées (par exemple `root@victim:/#`).
3. **`-r` (Mémoriser les mots) :** Apprend les mots que vous tapez dans le shell afin que vous puissiez les compléter plus tard.

---

## 💾 Gestion de l'historique persistant

Par défaut, `rlwrap` enregistre l'historique des commandes dans `~/.<command>_history`. Vous pouvez configurer des fichiers d'historique personnalisés par engagement :
```bash
# Save history to a dedicated engagement file
rlwrap -H ~/.history_target_corp nc -lvnp 4444

# Increase history size to 5,000 entries
rlwrap -H ~/.history_target -s 5000 nc -lvnp 4444

# Case-insensitive history search
rlwrap -i nc -lvnp 4444
```
---

## 🔍 Achèvement automatique des commandes (`-f`)

Vous pouvez fournir à `rlwrap` un fichier texte contenant des commandes Linux ou Windows courantes, et il les complétera automatiquement lorsque vous appuierez sur `<TAB>` :
```bash
# Create a wordlist of essential post-exploitation commands
cat << 'EOF' > /tmp/pentest_commands.txt
whoami
id
hostname
uname -a
cat /etc/passwd
cat /etc/shadow
sudo -l
find / -perm -4000 2>/dev/null
netstat -tulnp
ss -tulnp
ps aux
ip a
route -n
crontab -l
EOF

# Start listener with custom tab-completion dictionary
rlwrap -f /tmp/pentest_commands.txt nc -lvnp 4444
```
---

## 🛠️ Flux de travail complet de mise à niveau du shell TTY en 5 étapes

Alors que `rlwrap` fournit une navigation et un historique immédiats à l'aide des touches fléchées, certains programmes interactifs (`su`, `sudo`, `vim`) nécessitent toujours un terminal ATS entièrement alloué. Voici le pipeline de mise à niveau en 5 étapes standard de l’industrie :

### Étape 1 : Démarrez l'écouteur avec rlwrap
```bash
# Attacker machine: start enhanced listener
rlwrap -cAr nc -lvnp 4444
```
### Étape 2 : Déclenchez le Reverse Shell et Spawn PTY

Une fois votre reverse shell connecté à votre écouteur, générez un pseudo-terminal sur la cible :
```bash
# Target shell: Spawn PTY via Python
python3 -c 'import pty; pty.spawn("/bin/bash")'

# OR via script if Python is unavailable:
/usr/bin/script -qc /bin/bash /dev/null
```
### Étape 3 : Contexte du shell
```bash
# Press Ctrl + Z to put the reverse shell into the background
[1]+  Stopped                 rlwrap -cAr nc -lvnp 4444
```
### Étape 4 : Configurer le mode brut du terminal local
```bash
# Attacker machine: disable local echo and enable raw keystrokes
stty raw -echo; fg
```
*(Appuyez deux fois sur Entrée après avoir exécuté `fg` pour restaurer l'invite)*

### Étape 5 : Réinitialiser et définir les dimensions du terminal
```bash
# Target shell: set terminal type and environment
reset
export TERM=xterm-256color
export SHELL=bash

# Check your attacker terminal size in another window:
# $ stty size  -> Output: 38 160

# Apply rows and columns to target shell:
stty rows 38 columns 160
```
> ✅ **Résultat :** Vous disposez désormais d'un shell bash interactif indestructible et complet avec complétion par onglets, écran clair (`Ctrl+L`), gestion du signal (`Ctrl+C` ne se déconnecte pas) et dimensions complètes du terminal !

---

## 🔧 Utilisation de rlwrap avec d'autres outils Pentest

`rlwrap` ne se limite pas à Netcat. Tout outil CLI dépourvu d’édition de ligne en bénéficie :

### Oracle SQL*Plus
```bash
# Oracle's sqlplus utility notoriously lacks line editing and history on Linux
rlwrap sqlplus scott/tiger@10.10.10.50:1521/ORCL
```
### Redis et Telnet
```bash
# Redis CLI interactive console
rlwrap redis-cli -h 10.10.10.50 -p 6379

# Raw Telnet or socket interaction
rlwrap telnet 10.10.10.50 25
```
### Socat et sockets personnalisés
```bash
# Socat listener with rlwrap
rlwrap socat file:`tty`,raw,echo=0 tcp-listen:4444

# Connecting to raw sockets / CTF pwn challenges
rlwrap nc ctf.challenge.local 1337
```
---

## ⚡ Tableau de référence rapide

| Tâche | Commande |
|------|--------------|
| **Écouteur Netcat amélioré** | `rlwrap nc -lvnp 4444` |
| **Fichier complet et indicateurs d'invite** | `rlwrap -cAr nc -lvnp 4444` |
| **Fichier historique dédié** | `rlwrap -H ~/.nc_history nc -lvnp 4444` |
| **Achèvement des onglets personnalisés** | `rlwrap -f /path/to/wordlist.txt nc -lvnp 4444` |
| **Augmenter la taille de l'historique** | `rlwrap -s 10000 nc -lvnp 4444` |
| **Envelopper Oracle SQLPlus** | `rlwrap sqlplus user/pass@host/db` |
| **Envelopper la CLI Redis** | `rlwrap redis-cli -h 10.10.10.50` |
| **Envelopper l'écouteur Socat** | `rlwrap socat TCP-LISTEN:4444,reuseaddr -` |

---

## 💡 Conseils et bonnes pratiques

1. **Créez un alias Bash :** Ajoutez-le à votre `~/.bashrc` ou `~/.zshrc` pour ne jamais oublier de l'utiliser :
   ```bash
   alias nc='rlwrap -cAr nc'
   ```
2. **Ne manquez jamais la sécurité du `Ctrl+C` :** Lors de la capture d'obus instables, le `rlwrap` intercepte les problèmes de base du clavier, mais n'oubliez pas d'effectuer la mise à niveau en 5 étapes du `stty raw -echo; fg` si vous prévoyez d'exécuter de longues commandes interactives.
3. **Isolez les historiques des clients :** Lorsque vous testez plusieurs clients ou machines, utilisez toujours `-H ~/.client_name.hist` pour conserver des pistes d'audit et des historiques de commandes séparés.

---

## ⚠️ Mentions légales

> **IMPORTANT :** Les reverse shells et les outils de post-exploitation ne doivent être utilisés que dans les tests d'intrusion autorisés, les engagements de l'équipe rouge et les environnements de laboratoire pédagogique avec un consentement écrit explicite.