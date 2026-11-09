<!-- language-home-navigation -->
[🏠 Page d'accueil Française](../../README.fr.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🦈 tshark - Aide-mémoire complet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ████████╗███████╗██╗  ██╗ █████╗ ██████╗ ██╗  ██╗
  ╚══██╔══╝██╔════╝██║  ██║██╔══██╗██╔══██╗██║ ██╔╝
     ██║   ███████╗███████║███████║██████╔╝█████╔╝ 
     ██║   ╚════██║██╔══██║██╔══██║██╔══██╗██╔═██╗ 
     ██║   ███████║██║  ██║██║  ██║██║  ██║██║  ██╗
     ╚═╝   ╚══════╝╚═╝  ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝
         Terminal Packet Analysis & Dissection Engine
```
<p align="center">
  <img src="https://img.shields.io/badge/tshark-Packet_Analyzer-blue?style=for-the-badge" alt="tshark">
  <img src="https://img.shields.io/badge/CLI-Network_Sniffer-red?style=for-the-badge" alt="Renifleur de réseau CLI">
  <img src="https://img.shields.io/badge/Wireshark-Engine-green?style=for-the-badge" alt="Wireshark Engine">
  <img src="https://img.shields.io/badge/PCAP-Dissection-orange?style=for-the-badge" alt="PCAP Dissection">
</p>

<p align="center">
  <b>🌐 Dissection approfondie des paquets réseau basée sur un terminal, analyse de flux et extraction de champ optimisées par Wireshark</b>
</p>

---

## 📋 Table des matières

- [Introduction](#-introduction)
-[Comparaison d'outils : tshark vs tcpdump vs Wireshark](#-tool-comparison)
- [Installation et privilèges](#-installation--privilèges)
- [Capture et lecture de base](#-basic-capture--playback)
- [Filtrage du trafic : capture ou affichage](#-filtering-traffic)
- [Extraction de champ (`-T fields`)](#-field-extraction)
-[Practical Pentest & Forensic Recipes](#-practical-pentest--forensic-recettes)
  - [Recherche d'informations d'identification HTTP et d'URI](#http-credential--uri-hunting)
  - [Extraction de requêtes DNS et détection de tunneling](#dns-query-extraction--tunneling-detection)
  - [Extraction TLS / HTTPS SNI](#tls--https-sni-extraction)
  - [Extraction Kerberos AS-REQ / AS-REP](#kerberos-as-req--as-rep-extraction)
  - [Suivi de l'authentification SMB et NTLM](#smb--ntlm-authentication-tracking)
  - [Audit du protocole Cleartext (FTP, Telnet, SMTP)](#cleartext-protocol-auditing)
  -[WPA / EAPOL 4-Way Handshake Hunting](#wpa--eapol-4-way-handshake-hunting)
- [Suivi du flux et réassemblage](#-stream-following--réassemblage)
- [Statistiques, conversations et points de terminaison](#-statistics-conversations--endpoints)
- [Déchiffrement du trafic TLS / SSL](#-tls--ssl-traffic-decryption)
- [Formats d'exportation (JSON, CSV, Elasticsearch)](#-export-formats)
- [Tableau de référence rapide](#-quick-reference-table)
- [Conseils et bonnes pratiques](#-conseils--meilleures pratiques)
- [Mentions légales](#-legal-disclaimer)

---

## 🎯Présentation

**tshark** est l'équivalent en terminal et en ligne de commande de **Wireshark**. Il s'appuie sur la bibliothèque de capture de paquets (libpcap/Npcap) et le moteur de dissection de paquets identiques à ceux du Wireshark graphique, ce qui en fait l'un des outils d'analyse de réseau les plus puissants disponibles sous Linux, macOS et Windows.

Contrairement aux outils de renifleur de paquets de base qui inspectent uniquement les en-têtes de protocole, `tshark` offre des capacités d'inspection approfondie des paquets sur des milliers de protocoles, une syntaxe de filtre d'affichage Wireshark native (`-Y`), une extraction robuste des champs de protocole (`-T fields`) et un formatage de sortie flexible pour les pipelines d'automatisation (`jq`, scripts shell, Python).

### Principales fonctionnalités

| Fonctionnalité | Descriptif |
|---------|-------------|
| **Dissection profonde** | Inspecte les données utiles sur des milliers de protocoles d'application |
| **Filtres Wireshark** | Prise en charge complète de la syntaxe riche du filtre d'affichage Wireshark (`-Y`) |
| **Extraction sur le terrain** | Extraire directement les champs de protocole individuels (`-T fields -e ...`) |
| **Flux suivant** | Réassemble des sessions TCP/UDP complètes aux formats ASCII, Hex ou brut |
| **Médecine légale automatisée**| Scriptable en shell, Python ou redirigé vers `jq` via `-T json` |
| **Décryptage** | Prend en charge le déchiffrement TLS d'exécution avec `SSLKEYLOGFILE` |

---

## ⚖️ Comparaison d'outils

| Fonctionnalité | requin | tcpdump | Requin filaire |
|---------|--------|---------|---------------|
| **Interface** | CLI (Terminal) | CLI (Terminal) | Interface graphique (graphique) |
| **Utilisation des ressources** | Faible / Modéré | Extrêmement minimal | Supérieur (RAM et CPU) |
| **À distance (SSH / sans tête)** | Natif sans tête/CLI | Canal SSH natif | Nécessite X11 / Agent distant |
| **Moteur de dissection** | Dissecteurs Wireshark complets | En-têtes de base | Dissecteurs Wireshark complets |
| **Filtres de capture** | BPF uniquement (`-f`) | BPF uniquement (`-i`) | BPF uniquement |
| **Filtres d'affichage** | ✅ Syntaxe Wireshark complète (`-Y`) | ❌ Aucun | ✅ Barre de filtre GUI complète |
| **Formats d'exportation** | PCAP, JSON, CSV, élastique, XML | PCAP, ASCII, hexadécimal | PCAP, CSV, XML, JSON |
| **Scripts et automatisation**| Coquille, Python, jq | Coquille / Awk / Sed | Plugins Lua, interface graphique |

---

## 📥 Installation et privilèges

### Linux (Debian / Ubuntu / Kali Linux)
```bash
# Update and install tshark
sudo apt update && sudo apt install -y tshark

# Verify installation and version
tshark --version
```
### Autoriser la capture de paquets non root

Pour éviter d'exécuter `tshark` en tant que `root` (ce qui peut constituer un risque de sécurité en raison de la vaste surface du dissecteur), ajoutez votre utilisateur au groupe `wireshark` :
```bash
# Configure wireshark-common for non-superusers
sudo dpkg-reconfigure wireshark-common
# (Select "Yes" when prompted)

# Add your user to the wireshark group
sudo usermod -aG wireshark $USER

# Apply group changes to current session
newgrp wireshark
```
### macOS (Homebrew) et Windows (Chocolat)
```bash
# macOS
brew install wireshark

# Windows
choco install wireshark
```
---

## 🚀 Capture et lecture de base

### Liste des interfaces disponibles
```bash
# List interfaces with numeric IDs
tshark -D
```
### Capturer des paquets en direct
```bash
# Capture packets on interface eth0
tshark -i eth0

# Capture only 100 packets and stop
tshark -i eth0 -c 100

# Capture and save traffic directly to a pcap file
tshark -i eth0 -w capture.pcap

# Capture with ring buffer (rotate files every 100MB, keep max 5 files)
tshark -i eth0 -b filesize:100000 -b files:5 -w rolling_traffic.pcap

# Capture with a duration trigger (capture for 60 seconds)
tshark -i eth0 -a duration:60 -w one_minute.pcap
```

```bash
# Read and display packets from a pcap file
tshark -r capture.pcap

# Read PCAP without resolving IP addresses to DNS hostnames (speeds up processing)
tshark -r capture.pcap -n

# Suppress packet count summary upon completion
tshark -r capture.pcap -q
```
---

## 🔍 Filtrage du trafic : capture ou affichage

`tshark` utilise deux moteurs de filtrage distincts :

1. **Filtres de capture (`-f`) :** Filtres de paquets Berkeley (BPF) évalués par le noyau avant que les paquets n'atteignent l'espace utilisateur. Très performant, mais limité aux primitives IP/port de base.
2. **Filtres d'affichage (`-Y`) :** Filtres de protocole approfondis évalués par le moteur de dissection de Wireshark. Prend en charge les couches d'application, la recherche de chaînes, les expressions régulières et les indicateurs de protocole.
```bash
# Capture filter: Capture only TCP traffic to port 80 or 443
tshark -i eth0 -f "tcp port 80 or tcp port 443" -w web_traffic.pcap

# Display filter: Show only successful HTTP GET responses (Status 200)
tshark -r capture.pcap -Y "http.request.method == \"GET\" and http.response.code == 200"

# Display filter: Filter by specific subnet and exclude broadcast
tshark -r capture.pcap -Y "ip.addr == 192.168.1.0/24 and not eth.dst == ff:ff:ff:ff:ff:ff"

# Read-filter (-R): Single-pass filter used during capture
tshark -r capture.pcap -2 -R "dns.flags.response == 1"
```
---

## 📊 Extraction de champ (`-T fields`)

Le mode `-T fields` est la fonctionnalité la plus puissante de `tshark` pour les défis automatisés de reconnaissance, de script et de CTF.
```bash
# Basic syntax for field extraction
tshark -r capture.pcap -T fields -e <field1> -e <field2> -E header=y -E separator=,
```
### Indicateurs de formatage d'extraction de clé

| Drapeau | Descriptif |
|------|-------------|
| `-T fields` | Bascule le mode de sortie vers l'extraction de champs personnalisés |
| `-e <field>` | Nomme le champ de protocole à extraire (peut être répété) |
| `-E header=y` | Imprime les noms de colonnes sous forme de ligne d'en-tête |
| `-E separator=,` | Définit le séparateur de champs (`,` pour CSV, `\t` pour TSV) |
| `-E occurrence=f` | Extrait uniquement la première (`f`), la dernière (`l`) ou toutes les occurrences (`a`) |
| `-E quote=d` | Place les valeurs entre guillemets doubles |

### Aide-mémoire sur les champs du protocole principal
```bash
# IP layer fields
ip.src, ip.dst, ip.proto, ip.ttl

# TCP layer fields
tcp.srcport, tcp.dstport, tcp.flags, tcp.stream, tcp.payload

# HTTP fields
http.host, http.request.method, http.request.uri, http.response.code, http.user_agent, http.cookie

# DNS fields
dns.qry.name, dns.qry.type, dns.a, dns.flags.response

# TLS fields
tls.handshake.extensions_server_name, tls.handshake.ciphersuite
```
---

## 🎯 Recettes pratiques de Pentest et de médecine légale

### Recherche d'informations d'identification HTTP et d'URI
```bash
# Extract all visited URLs and HTTP Hosts
tshark -r capture.pcap -Y "http.request" -T fields -e ip.src -e http.host -e http.request.method -e http.request.uri -E header=y

# Extract User-Agent strings across internal clients
tshark -r capture.pcap -Y "http.user_agent" -T fields -e ip.src -e http.user_agent | sort -u

# Hunt for HTTP POST data (logins, passwords, tokens)
tshark -r capture.pcap -Y "http.request.method == \"POST\"" -T fields -e ip.src -e ip.dst -e http.host -e http.file_data

# Hunt for HTTP Basic Authentication headers (Base64 encoded)
tshark -r capture.pcap -Y "http.authorization" -T fields -e ip.src -e http.authorization
```
### Extraction de requêtes DNS et détection de tunneling
```bash
# List all requested domain names (DNS queries)
tshark -r capture.pcap -Y "dns.flags.response == 0" -T fields -e ip.src -e dns.qry.name | sort -u

# Detect potential DNS tunneling (display long subdomains > 30 characters)
tshark -r capture.pcap -Y "dns.flags.response == 0" -T fields -e dns.qry.name | awk 'length($1) > 30' | sort -u

# Correlate DNS queries with their resolved IP addresses
tshark -r capture.pcap -Y "dns.flags.response == 1 and dns.a" -T fields -e dns.qry.name -e dns.a -E header=y
```
### Extraction TLS/HTTPS SNI

Même lorsque les charges utiles HTTPS sont chiffrées, l'**Indication du nom du serveur (SNI)** dans la prise de contact initiale TLS Client Hello révèle les noms de domaine de destination :
```bash
# Extract TLS Server Name Indication (SNI) hostnames
tshark -r capture.pcap -Y "tls.handshake.type == 1" -T fields -e ip.src -e ip.dst -e tls.handshake.extensions_server_name | sort -u
```
### Extraction Kerberos AS-REQ / AS-REP
```bash
# Extract Kerberos domain client names (hunting for user accounts)
tshark -r capture.pcap -Y "kerberos.CNameString" -T fields -e ip.src -e kerberos.realm -e kerberos.CNameString | sort -u

# Hunt for Kerberos errors (e.g. KRB5KDC_ERR_PREAUTH_REQUIRED, bad passwords)
tshark -r capture.pcap -Y "kerberos.error_code" -T fields -e ip.src -e ip.dst -e kerberos.error_code
```
### Suivi de l'authentification SMB et NTLM
```bash
# Extract SMB accessed file paths and shares
tshark -r capture.pcap -Y "smb2.filename" -T fields -e ip.src -e ip.dst -e smb2.filename | sort -u

# Extract NTLMSSP usernames and domain names during authentication
tshark -r capture.pcap -Y "ntlmssp.auth.username" -T fields -e ip.src -e ntlmssp.auth.domain -e ntlmssp.auth.username
```
### Audit du protocole Cleartext
```bash
# Extract FTP usernames and passwords
tshark -r capture.pcap -Y "ftp.request.command == \"USER\" or ftp.request.command == \"PASS\"" -T fields -e ip.src -e ftp.request.command -e ftp.request.arg

# Extract Telnet session input
tshark -r capture.pcap -Y "telnet" -T fields -e telnet.data
```
### Recherche de poignée de main à 4 voies WPA/EAPOL
```bash
# Filter for 802.11 EAPOL handshakes to crack with Hashcat/Aircrack-ng
tshark -r wifi.pcap -Y "eapol" -w handshakes_only.pcap
```
---

## 🧵 Suivi et remontage du flux

`tshark` permet de réassembler des flux TCP et UDP entiers sans avoir besoin de l'interface graphique de Wireshark.
```bash
# List all TCP conversations to find stream IDs
tshark -r capture.pcap -q -z conv,tcp

# Follow TCP stream 0 in ASCII format
tshark -r capture.pcap -q -z follow,tcp,ascii,0

# Follow TCP stream 2 in Hexdump format
tshark -r capture.pcap -q -z follow,tcp,hex,2

# Follow HTTP stream (stream index 1)
tshark -r capture.pcap -q -z follow,http,ascii,1
```
---

## 📈 Statistiques, conversations et points de terminaison
```bash
# Protocol hierarchy statistics (breakdown of all protocols in PCAP)
tshark -r capture.pcap -q -z io,phs

# IP conversations table (who talked to whom, packet count, bytes)
tshark -r capture.pcap -q -z conv,ip

# Port conversation statistics (TCP ports)
tshark -r capture.pcap -q -z conv,tcp

# List all unique IP endpoints in PCAP
tshark -r capture.pcap -q -z endpoints,ip

# HTTP request statistics tree
tshark -r capture.pcap -q -z http,tree

# Wireshark Expert Info warnings and errors
tshark -r capture.pcap -q -z expert
```
---

## 🔐 Décryptage du trafic TLS/SSL

Si vous disposez de clés secrètes pré-maîtres (exportées via le navigateur `SSLKEYLOGFILE` ou récupérées lors du sandboxing de malware) :
```bash
# Decrypt TLS traffic on the fly using keylog file
tshark -r https_traffic.pcap -o "tls.keylog_file:/path/to/sslkeys.log" -Y "http" -T fields -e http.host -e http.request.uri

# Follow decrypted HTTP stream inside TLS
tshark -r https_traffic.pcap -o "tls.keylog_file:/path/to/sslkeys.log" -q -z follow,http,ascii,0
```
---

## 📦Formats d'exportation
```bash
# Output as structured JSON (ideal for piping into jq or Python)
tshark -r capture.pcap -c 5 -T json

# Extract specific JSON field with jq
tshark -r capture.pcap -T json | jq '.[]._source.layers.ip["ip.src"]'

# Output in Elasticsearch Bulk Format (-T ek)
tshark -r capture.pcap -T ek > packets_for_elastic.json

# Output as CSV table
tshark -r capture.pcap -T fields -e frame.number -e ip.src -e ip.dst -e _ws.col.Protocol -E header=y -E separator=, > traffic.csv
```
---

## ⚡ Tableau de référence rapide

| Tâche | Commande |
|------|--------------|
| **Capturez le trafic en direct** | `tshark -i eth0 -w output.pcap` |
| **Capturer avec le filtre BPF** | `tshark -i eth0 -f "port 80" -w http.pcap` |
| **Lire PCAP sans DNS** | `tshark -r capture.pcap -n` |
| **Appliquer le filtre d'affichage** | `tshark -r capture.pcap -Y "http.response.code >= 400"` |
| **Extraire les adresses IP et les ports** | `tshark -r capture.pcap -T fields -e ip.src -e tcp.srcport -e ip.dst -e tcp.dstport` |
| **Extraire les requêtes DNS** | `tshark -r capture.pcap -Y "dns.flags.response == 0" -T fields -e dns.qry.name` |
| **Extraire les noms d'hôtes TLS SNI**| `tshark -r capture.pcap -Y "tls.handshake.type == 1" -T fields -e tls.handshake.extensions_server_name` |
| **Extraire les URL HTTP** | `tshark -r capture.pcap -Y "http.request" -T fields -e http.host -e http.request.uri` |
| **Suivez le flux TCP** | `tshark -r capture.pcap -q -z follow,tcp,ascii,0` |
| **Hiérarchie des protocoles** | `tshark -r capture.pcap -q -z io,phs` |
| **Déchiffrer avec Keylog** | `tshark -r traffic.pcap -o "tls.keylog_file:keys.log"` |
| **Exporter vers JSON** | `tshark -r capture.pcap -T json > packets.json` |

---

## 💡 Conseils et bonnes pratiques

1. **Toujours utiliser `-n` pour les PCAP volumineux :** La résolution du nom DNS ralentit considérablement l'analyse. Ajoutez `-n` pour empêcher la résolution des adresses IP.
2. **Combinez `-f` et `-Y` :** Utilisez les filtres de capture `-f` pendant la capture en direct pour réduire les pertes de paquets, et les filtres d'affichage `-Y` pendant la post-analyse pour une inspection approfondie du protocole.
3. **Pipe avec `jq` :** Le pipeline de `tshark -T json` dans `jq` permet d'interroger les champs de protocole imbriqués avec une logique et une transformation booléennes complètes.
4. **Tampons en anneau pour une détection 24h/24 et 7j/7 :** Lors de l'exécution de captures réseau continues sur un hôte pivot, appliquez toujours les limites de taille et de fichier (`-b filesize:50000 -b files:10`) pour éviter de planter le disque de la cible.

---

## ⚠️ Mentions légales

> **IMPORTANT :** La capture de paquets non autorisée, l'écoute clandestine du trafic réseau et l'interception d'informations d'identification sur les réseaux sans autorisation explicite et documentée sont illégales et passibles de poursuites pénales. Utilisez le `tshark` strictement sur les systèmes et réseaux que vous possédez ou que vous êtes contractuellement autorisé à évaluer.