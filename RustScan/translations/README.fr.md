<!-- language-home-navigation -->
[🏠 Page d'accueil Française](../../README.fr.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🦀 RustScan - Aide-mémoire complet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ██████╗ ██╗   ██╗███████╗████████╗███████╗ ██████╗ █████╗ ███╗   ██╗
  ██╔══██╗██║   ██║██╔════╝╚══██╔══╝██╔════╝██╔════╝██╔══██╗████╗  ██║
  ██████╔╝██║   ██║███████╗   ██║   ███████╗██║     ███████║██╔██╗ ██║
  ██╔══██╗██║   ██║╚════██║   ██║   ╚════██║██║     ██╔══██║██║╚██╗██║
  ██║  ██║╚██████╔╝███████║   ██║   ███████║╚██████╗██║  ██║██║ ╚████║
  ╚═╝  ╚═╝ ╚═════╝ ╚══════╝   ╚═╝   ╚══════╝ ╚═════╝╚═╝  ╚═╝╚═╝  ╚═══╝
       The Modern Port Scanner - Scan All 65k Ports in 3 Seconds into Nmap
```
<p align="center">
  <img src="https://img.shields.io/badge/RustScan-Port_Scanner-blue?style=for-the-badge" alt="RustScan">
  <img src="https://img.shields.io/badge/Language-Rust-orange?style=for-the-badge" alt="Language Rust">
  <img src="https://img.shields.io/badge/Category-Recon_&_Scanning-green?style=for-the-badge" alt="Reconnaissance et numérisation">
  <img src="https://img.shields.io/badge/License-GPLv3-red?style=for-the-badge" alt="Licence GPLv3">
</p>

<p align="center">
  <b>🌐 Scanner de ports rapide, personnalisable et automatisé conçu pour rediriger les ports ouverts découverts directement vers les scripts Nmap</b>
</p>

---

## 📋 Table des matières

- [Vue d'ensemble et architecture](#-overview--architecture)
- [Comparaison d'outils : RustScan vs Nmap vs Masscan](#-tool-comparison-rustscan-vs-nmap-vs-masscan)
- [Installation](#-installation)
-[Utilisation de base et analyse d'un hôte unique](#-basic-usage--single-host-scanning)
- [Intégration automatisée de Nmap](#-automated-nmap-integration)
-[Réglage des performances (`ulimit` et taille du lot)](#-performance-tuning-ulimit--batch-size)
- [Analyse des plages de ports et des sous-réseaux CIDR] (#-scanning-port-ranges--cidr-subnets)
- [Scripts et formatage de sortie](#-scripting--output-formatting)
- [Tableau de référence rapide](#-quick-reference-table)
- [Considérations défensives et OPSEC](#-defensive-considérations--opsec)
- [Mentions légales](#-legal-disclaimer)

---

## 🎯 Présentation et architecture

**RustScan** est un scanner de ports moderne et rapide conçu avec **Rust**. Sa philosophie de base est simple : **analysez les ports à une vitesse fulgurante, trouvez les ports ouverts sur l'ensemble du spectre 1-65535 en 3 secondes et transférez immédiatement ces ports ouverts à Nmap pour une analyse complète des services et des vulnérabilités**.

Au lieu d'attendre 10 à 20 minutes pour que `nmap -p-` termine ses balayages séquentiels de prise de contact TCP, RustScan exploite le multithreading asynchrone et le traitement par lots de sockets du système d'exploitation pour identifier les ports ouverts presque instantanément.

---

## ⚖️ Comparaison d'outils : RustScan vs Nmap vs Masscan

| Fonctionnalité | Analyse de rouille | Nmap | Masscan |
|---------|----------|------|---------|
| **Objectif principal** | Découverte rapide -> Pipe Nmap | Service profond et scripts NSE | Analyse SYN à haut débit sur Internet |
| **Vitesse sur tous les ports (1-65535)**| ⭐⭐⭐⭐⭐ (~3 secondes) | ⭐⭐ (5 à 20 minutes) | ⭐⭐⭐⭐⭐ (~2 secondes) |
| **Détection de version de service**| Via le tuyau Nmap | Natif (`-sV`) | ❌ Minime |
| **Moteur de scripts NSE** | Via le tuyau Nmap | Natif (`-sC`, `--script`) | ❌ Non |
| **Moteur de paquets** | Sockets TCP asynchrones | IP brute/pile TCP personnalisée | Pile SYN asynchrone personnalisée |
| **Facilité d'automatisation** | Arguments CLI natifs (`--`) | Options CLI étendues | Haute complexité |

---

## 📥Installation
```bash
# Debian / Ubuntu / Kali Linux (.deb package)
wget https://github.com/RustScan/RustScan/releases/download/2.3.0/rustscan_2.3.0_amd64.deb
sudo dpkg -i rustscan_2.3.0_amd64.deb

# Arch Linux (AUR)
yay -S rustscan

# Cargo (Rust Package Manager)
cargo install rustscan

# Docker Container
docker run -it --rm --name rustscan rustscan/rustscan:2.1.1 -a 10.10.10.50
```
---

## 🚀 Utilisation de base et analyse d'un hôte unique
```bash
# Scan all 65,535 ports on a single IP
rustscan -a 10.10.10.50

# Scan with greppable one-line output
rustscan -a 10.10.10.50 --greppable

# Quiet mode (suppresses the ASCII banner)
rustscan -q -a 10.10.10.50
```
---

## 🔗 Intégration automatisée de Nmap

La fonctionnalité la plus puissante de RustScan consiste à transmettre les ports ouverts directement à Nmap à l'aide du séparateur `--` final :
```bash
# Scan target and execute default safe scripts & version detection on open ports
rustscan -a 10.10.10.50 -- -sC -sV

# Pass custom Nmap NSE scripts and output options
rustscan -a 10.10.10.50 -- -sC -sV -oN nmap_report.txt

# Run vulnerability audit with Nmap Vuln scripts
rustscan -a 10.10.10.50 -- --script vuln -oA target_vulns
```
---

## ⚡ Réglage des performances (`ulimit` et taille du lot)

RustScan s'appuie sur les limites des descripteurs de fichiers de votre système d'exploitation. Par défaut, il s'ajuste automatiquement, mais le réglage manuel offre des performances optimales :
```bash
# Increase batch size and socket ulimit for high-speed local networks
rustscan -a 10.10.10.50 -b 5000 -u 5000

# Lower batch size for high-latency or packet-dropping WiFi networks
rustscan -a 10.10.10.50 -b 500 --scan-order "random"
```
---

## 🌐 Analyse des plages de ports et des sous-réseaux CIDR
```bash
# Scan a specific port range instead of all ports
rustscan -a 10.10.10.50 -r 1-1000

# Scan multiple IP addresses
rustscan -a 10.10.10.50,10.10.10.51

# Scan an entire CIDR subnet range
rustscan -a 192.168.1.0/24
```
---

## 📊 Script et formatage de sortie
```bash
# Output discovered open ports in JSON format
rustscan -a 10.10.10.50 --accessible

# Read target hosts from a file
rustscan -a 'hosts.txt'
```
---

## ⚡ Tableau de référence rapide

| Objectif | Commande |
|-----------|---------|
| **Analyser tous les 65 000 ports** | `rustscan -a <IP>` |
| **Analyser tous les ports + scripts Nmap**| `rustscan -a <IP> -- -sC -sV` |
| **Analyser le sous-réseau CIDR** | `rustscan -a <SUBNET>/24` |
| **Plage de ports de numérisation** | `rustscan -a <IP> -r 1-1000` |
| **Ajuster la taille du lot et la limite** | `rustscan -a <IP> -b 3000 -u 3000` |
| **Mode silencieux (pas de bannière)** | `rustscan -q -a <IP>` |
| **Sortie gréptable** | `rustscan -a <IP> --greppable` |
| **Exporter vers un fichier Nmap** | `rustscan -a <IP> -- -sC -sV -oN nmap.txt` |

---

## 🛡️ Considérations défensives et OPSEC

1. **Alertes de bruit massif et IDS :** L'analyse de 65 000 ports en 3 secondes génère instantanément des milliers de paquets TCP SYN. Cela déclenchera immédiatement la limitation du débit de Snort, Suricata et du pare-feu.
2. **Inondation SYN et paquets abandonnés :** Sur les liens VPN instables (par exemple, HackTheBox, TryHackMe), des tailles de lots élevées (`-b 4000`) peuvent entraîner une perte de paquets. Si les résultats semblent incohérents, réduisez la taille du lot à `-b 1000`.

---

## ⚠️ Mentions légales

> [!ATTENTION]
> Cette aide-mémoire est destinée uniquement aux tests d'intrusion et à la recherche pédagogique autorisés. La numérisation des réseaux sans autorisation est strictement interdite.