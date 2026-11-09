<!-- language-home-navigation -->
[🏠 Page d'accueil Française](../../README.fr.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ⚡ Naabu - Aide-mémoire complet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ███╗   ██╗ █████╗  █████╗ ██████╗ ██╗   ██╗
  ████╗  ██║██╔══██╗██╔══██╗██╔══██╗██║   ██║
  ██╔██╗ ██║███████║███████║██████╔╝██║   ██║
  ██║╚██╗██║██╔══██║██╔══██║██╔══██╗██║   ██║
  ██║ ╚████║██║  ██║██║  ██║██████╔╝╚██████╔╝
  ╚═╝  ╚═══╝╚═╝  ╚═╝╚═╝  ╚═╝╚═════╝  ╚═════╝ 
        Fast & Reliable SYN/CONNECT Port Scanner
```
<p align="center">
  <img src="https://img.shields.io/badge/Naabu-Port_Scanner-blue?style=for-the-badge" alt="Naabu">
  <img src="https://img.shields.io/badge/Author-ProjectDiscovery-orange?style=for-the-badge" alt="ProjectDiscovery">
  <img src="https://img.shields.io/badge/Category-Recon_&_Scanning-green?style=for-the-badge" alt="Reconnaissance et numérisation">
  <img src="https://img.shields.io/badge/Language-Go-cyan?style=for-the-badge" alt="Go">
</p>

<p align="center">
  <b>⚡ Scanner de ports rapide et fiable axé sur la simplicité et la tuyauterie transparente avec les outils ProjectDiscovery (httpx, nuclei)</b>
</p>

---

## 📋 Table des matières

- [Aperçu et fonctionnalités clés](#-overview--key-features)
- [Installation](#-installation)
- [Analyse de base des ports](#-basic-port-scanning)
  - [Analyse d'hôte unique](#analyse d'hôte unique)
  - [Principaux ports et gamme complète de ports](#top-ports--full-port-range)
- [Modes de numérisation : SYN (Raw Socket) vs CONNECT](#-scan-modes-syn-raw-socket-vs-connect)
- [Limitation de débit et réglage des performances] (#-rate-limiting--performance-tuning)
-[Analyse multi-cibles et blocs CIDR](#-multi-target-scanning--cidr-blocks)
- [Flux de travail de pipeline transparents] (#-flux de travail de pipeline sans couture)
  - [Naabu + Httpx + Nuclei](#naabu--httpx--noyaux)
  - [Subfinder + Naabu](#subfinder--naabu)
- [Intégration Nmap (`-nmap`)](#-nmap-integration--nmap)
- [Tableau de référence rapide](#-quick-reference-table)
- [Considérations défensives et détection](#-considérations-defensives--détection)
- [Mentions légales](#-legal-disclaimer)

---

## 🎯 Présentation et fonctionnalités clés

Développé par **ProjectDiscovery**, **Naabu** est un scanner de ports conçu pour être rapide, précis et optimisé pour l'intégration dans les pipelines d'automatisation de reconnaissance modernes :
- **SYN & CONNECT Scans :** Repli automatique vers TCP CONNECT lorsque les privilèges de socket bruts (`libpcap` / root) sont absents.
- **Écosystème ProjectDiscovery :** Architecture de pipeline native permettant une tuyauterie transparente de `subfinder` vers `naabu`, puis vers `httpx` et `nuclei`.
- **Moteur Nmap intégré :** Possibilité de déclencher automatiquement le service Nmap actif et l'analyse des versions sur les ports ouverts découverts.
- **Exclusion CDN :** contourne automatiquement les nœuds périphériques Cloud CDN (Cloudflare, Akamai, CloudFront) pour éviter le gaspillage de bande passante et les faux positifs.

---

## 📥Installation
```bash
# Using Go (Recommended)
go install -v github.com/projectdiscovery/naabu/v2/cmd/naabu@latest

# Binary download via pdtm (ProjectDiscovery Tool Manager)
pdtm -i naabu

# Arch Linux (AUR)
yay -S naabu-bin

# Kali Linux
sudo apt update && sudo apt install -y naabu
```
> [!NOTE]
> Sous Linux, accorder `CAP_NET_RAW` permet d'exécuter des analyses SYN sans root :
> `sudo setcap cap_net_raw=+ep $(which naabu)`

---

## 🚀 Analyse de base des ports

### Analyse d'un hôte unique
```bash
# Scan default top 100 ports
naabu -host example.com

# Scan specific ports
naabu -host example.com -p 80,443,8080,8443
```
### Meilleurs ports et gamme complète de ports
```bash
# Scan top 1000 ports
naabu -host example.com -top-ports 1000

# Full port scan (1-65535)
naabu -host example.com -p -
```
---

## ⚡ Modes de numérisation : SYN (Raw Socket) vs CONNECT

- **SYN Scan (`-s syn` / root requis) :** Envoie des paquets TCP SYN bruts sans terminer la négociation à trois voies (analyse à moitié ouverte). Beaucoup plus rapide et moins bruyant dans les journaux d'applications.
- **CONNECT Scan (`-s conn`) :** Utilise l'appel de socket standard du système d'exploitation `connect()`. Fonctionne sans privilèges root et pénètre dans les environnements virtuels restrictifs.
```bash
# Explicit SYN Scan (Requires sudo or CAP_NET_RAW)
sudo naabu -host example.com -s syn

# Explicit TCP Connect Scan (No root required)
naabu -host example.com -s conn
```
---

## ⚙️ Limitation du débit et réglage des performances
```bash
# Adjust packet rate per second (default: 1000)
sudo naabu -host example.com -rate 2500

# Set connection timeout in milliseconds
naabu -host example.com -timeout 1000

# Exclude CDN edge servers (Cloudflare, CloudFront, Fastly)
naabu -host example.com -exclude-cdn
```
---

## 🌐 Analyse multi-cibles et blocs CIDR
```bash
# Scan targets from list
naabu -list targets.txt -p 80,443,8080 -o open_ports.txt

# Scan a /24 subnet block
sudo naabu -host 192.168.1.0/24 -p 22,80,443,445,3389
```
---

## 🔄 Flux de travail de pipeline transparents

Naabu brille lorsqu'il est associé à d'autres outils ProjectDiscovery :

### Sous-chercheur + Naabu
Énumérez les sous-domaines et découvrez immédiatement ceux qui ont des ports Web/SSH ouverts :
```bash
subfinder -d example.com -silent | naabu -silent -p 80,443,8080,8443
```
### Naabu + Httpx + Noyaux
Le pipeline canonique de reconnaissance offensive des bug bounty :
```bash
# 1. Port scan subdomains -> 2. Probe HTTP services -> 3. Vulnerability scan
subfinder -d target.com -silent \
  | naabu -silent -p 80,443,8080,8443 \
  | httpx -silent \
  | nuclei -severity medium,high,critical -o findings.txt
```
---

## 🔍 Intégration Nmap (`-nmap`)

Transmettez les ports ouverts découverts directement à Nmap pour la détection de service (`-sV`) et l'analyse de script (`-sC`) :
```bash
sudo naabu -host target.local -top-ports 1000 -nmap-cli 'nmap -sV -sC -oN naabu_nmap.txt'
```
---

## ⚡ Tableau de référence rapide

| Objectif | Commande |
|-----------|---------|
| **Analyse par défaut (Top 100)** | `naabu -host <TARGET>` |
| **Balayage complet (1-65535)**| `naabu -host <TARGET> -p -` |
| **1 000 meilleurs ports** | `naabu -host <TARGET> -top-ports 1000` |
| **SYN Scan (rapide, furtif)**| `sudo naabu -host <TARGET> -s syn` |
| **Exclure les adresses IP CDN Edge** | `naabu -host <TARGET> -exclude-cdn` |
| **Sortie vers JSON** | `naabu -host <TARGET> -json -o results.json` |
| **Diriger vers HTTPX** | `naabu -host <TARGET> -silent \| httpx` |
| **Déclencher l'analyse du service Nmap**| `sudo naabu -host <TARGET> -nmap-cli 'nmap -sV'` |

---

## 🛡️ Considérations défensives et détection

1. **Règles de pare-feu et IDS :** Les balayages SYN à haut débit générés par Naabu produisent des ratios TCP RST/SYN élevés facilement détectés par les règles Snort/Suricata.
2. **Limitation du débit réseau :** Appliquez la limitation du débit des connexions entrantes (`iptables -m limit --limit 50/s`) pour ralentir et interrompre l'analyse automatisée.

---

## ⚠️ Mentions légales

> [!ATTENTION]
> Naabu est développé strictement pour les évaluations de sécurité autorisées, les tests d'intrusion et la découverte d'actifs défensifs. L'analyse des réseaux sans autorisation explicite est strictement interdite.