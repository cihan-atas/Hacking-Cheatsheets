<!-- language-home-navigation -->
[🏠 Page d'accueil Française](../../README.fr.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# Aide-mémoire Searchsploit

[Searchsploit](https://www.exploit-db.com/searchsploit) est l'utilitaire de ligne de commande officiel pour [Exploit-Database](https://www.exploit-db.com/). Il permet aux professionnels de la sécurité, aux testeurs d'intrusion et aux chercheurs en vulnérabilités d'effectuer des recherches hors ligne parmi des dizaines de milliers d'exploits, de shellcodes et de livres blancs publiés sans envoyer de trafic réseau à des moteurs de recherche tiers.

---

## Table des matières

- [Vue d'ensemble et architecture](#overview--architecture)
- [Installation et mises à jour de la base de données locale](#installation--local-database-updates)
- [Recherche d'exploits de base et avancée](#basic--advanced-exploit-searching)
-[Examen et copie des exploits (`-m` / `-p` / `-x`)](#examen--copie-exploits--m---p---x)
- [Filtrer par plate-forme et type](#filtering-by-platform--type)
- [Intégration de numérisation Nmap XML / Grepable](#nmap-xml--grepable-scanning-integration)
- [Sortie JSON et automatisation des scripts](#json-output--script-automation)
-[Meilleures pratiques en matière de tests d'intrusion](#meilleures pratiques en matière de tests d'intrusion)

---

## Présentation et architecture

Searchsploit interroge un clone git local d'Exploit-DB stocké dans `/usr/share/exploitdb/`.
- **Zéro empreinte réseau :** Les recherches sont exécutées sur les structures CSV et de répertoires locales.
- **Fiabilité hors ligne :** Entièrement fonctionnel dans les environnements cibles et les réseaux d'engagement isolés.
- **Miroir intégré :** Copie les fichiers d'exploitation directement dans le répertoire de travail actuel pour examen et modification immédiats.

---

## Installation et mises à jour de la base de données locale

### Debian / Ubuntu / Kali Linux / Parrot OS
```bash
# Kali and Parrot include searchsploit by default
sudo apt-get update && sudo apt-get install -y exploitdb

# Update the local exploit database archive
searchsploit -u
```
### Installation du référentiel Git (n'importe quelle distribution Linux)
```bash
git clone https://gitlab.com/exploit-database/exploitdb.git /opt/exploitdb
sudo ln -sf /opt/exploitdb/searchsploit /usr/local/bin/searchsploit

# Check version and status
searchsploit -v
```
---

## Recherche d'exploits de base et avancée

### 1. Recherche simple par mot clé
Recherchez des services, démons ou logiciels spécifiques :
```bash
searchsploit apache 2.4.49
searchsploit openssh 7.2
searchsploit wordpress core 5.8
```
### 2. Correspondance exacte du titre (`-t`)
Restreindre les requêtes de recherche strictement pour exploiter les titres afin d'éliminer les correspondances de description bruyantes :
```bash
searchsploit -t "Privilege Escalation" linux kernel
searchsploit -t "Remote Code Execution" smb
```
### 3. Correspondance stricte des cas et des termes (`-e`)
Recherchez les correspondances exactes de version et de modèle :
```bash
searchsploit -e "Apache Struts"
```
### 4. Recherche par identifiant CVE
Exploits de requête mappés à des numéros spécifiques de vulnérabilités et d’expositions communes :
```bash
searchsploit --cve 2021-44228
searchsploit --cve 2017-0144
```
### 5. Exclure les résultats indésirables (`--exclude`)
Filtrez les scripts de déni de service (DoS) ou auxiliaires lorsque vous recherchez l'exécution de code à distance (RCE) :
```bash
searchsploit linux kernel 5.4 --exclude="Denial of Service"
```
---

## Examen et copie des exploits (`-m` / `-p` / `-x`)

Ne modifiez ou n'exécutez jamais d'exploits directement dans le répertoire `/usr/share/exploitdb/`. Utilisez les fonctions de copie et d'inspection de Searchsploit.

### 1. Copier (miroir) l'exploit dans le répertoire actuel (`-m`)
Copiez un fichier PoC d'exploit (Python, C, Ruby, bash) directement dans votre répertoire de travail local :
```bash
# Mirror by Exploit-DB ID (EBD-ID)
searchsploit -m 50383

# Mirror by search path pattern
searchsploit -m windows/remote/48537.py
```
### 2. Inspecter les métadonnées d'exploit et le chemin complet (`-p`)
Affichez le chemin absolu du système de fichiers et l'URL Exploit-DB pour un EDB-ID :
```bash
searchsploit -p 50383
```
Sortir:
```text
  Exploit: Apache HTTP Server 2.4.49 - Path Traversal & Remote Code Execution (RCE)
      URL: https://www.exploit-db.com/exploits/50383
     Path: /usr/share/exploitdb/exploits/multiple/webapps/50383.sh
File Type: Bourne-Again shell script, ASCII text executable
```
### 3. Lire le code d'exploitation dans le téléavertisseur du terminal (`-x`)
Ouvrez le code d'exploitation directement dans `less` pour une inspection rapide :
```bash
searchsploit -x 50383
```
---

## Filtrage par plate-forme et type

Searchsploit indexe les exploits, les shellcodes et les documents de recherche sur plusieurs architectures :

### 1. Filtrer par système d'exploitation cible
```bash
# Search only Linux exploits
searchsploit linux kernel 4.4

# Search only Windows exploits
searchsploit windows smb eternal

# Search web applications
searchsploit webapps tomcat
```
### 2. Filtrer uniquement les shellcodes bruts (`-s`)
Recherchez les charges utiles du shellcode spécifiques à l'architecture (par exemple, execve `/bin/sh` sans octets nuls) :
```bash
searchsploit -s linux/x64 "execve"
searchsploit -s windows/x86 "reverse shell"
```
### 3. Filtrer les documents de sécurité et les livres blancs (`-w`)
```bash
searchsploit -w "Active Directory Kerberos"
```
---

## Intégration de numérisation Nmap XML / Grepable

Searchsploit peut analyser automatiquement les résultats de l'analyse XML Nmap et faire correspondre les versions de logiciels découvertes avec les exploits connus :
```bash
# 1. Perform service detection and save XML output
nmap -sV -p- -T4 -oX /tmp/target_scan.xml 192.168.1.50

# 2. Feed the XML scan file directly to Searchsploit
searchsploit --nmap /tmp/target_scan.xml
```
*Searchsploit inspecte chaque bannière de port ouvert et chaque chaîne de version, interrogeant les exploits locaux pour un tri immédiat.*

---

## Sortie JSON et automatisation des scripts

Pour l’automatisation des pipelines et les outils de reconnaissance personnalisés, les résultats sont générés au format JSON structuré :
```bash
searchsploit "Apache Struts" --json > struts_exploits.json
```
Filtrez les chemins des fichiers d'exploitation à l'aide de `jq` :
```bash
searchsploit --cve 2021-41773 --json | jq -r '.RESULTS_EXPLOIT[].Path'
```
---

## Meilleures pratiques en matière de tests d'intrusion

1. **Vérifier avant l'exécution :** Inspectez toujours le code source de l'exploit (`searchsploit -x <id>`) avant la compilation ou l'exécution. Vérifiez le shellcode codé en dur, ciblez les adresses IP et assurez-vous qu’il n’y a pas de portes dérobées ou d’intermédiaires de charge utile malveillants.
2. **Compilez avec les indicateurs corrects :** De nombreux exploits Exploit-DB C nécessitent des indicateurs GCC spécifiques (par exemple, `-m32`, `-lpthread` ou `-lcrypto`) :
   ```bash
   searchsploit -m 40839
   gcc -pthread 40839.c -o dirtycow -lcrypt
   ```
3. **Gardez la base de données à jour :** Exécutez `searchsploit -u` régulièrement pour garantir que les Zero Days et les preuves de concept nouvellement publiés sont indexés localement.