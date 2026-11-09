<!-- language-home-navigation -->
[🏠 Page d'accueil Française](../../README.fr.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ⚡ Masscan - Aide-mémoire complet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ███╗   ███╗ █████╗ ███████╗███████╗ ██████╗ █████╗ ███╗   ██╗
  ████╗ ████║██╔══██╗██╔════╝██╔════╝██╔════╝██╔══██╗████╗  ██║
  ██╔████╔██║███████║███████╗███████╗██║     ███████║██╔██╗ ██║
  ██║╚██╔╝██║██╔══██║╚════██║╚════██║██║     ██╔══██║██║╚██╗██║
  ██║ ╚═╝ ██║██║  ██║███████║███████║╚██████╗██║  ██║██║ ╚████║
  ╚═╝     ╚═╝╚═╝  ╚═╝╚══════╝╚══════╝ ╚═════╝╚═╝  ╚═╝╚═╝  ╚═══╝
       The Fastest Internet Port Scanner - Asynchronous 10M Packets/Second
```
<p align="center">
  <img src="https://img.shields.io/badge/Masscan-Internet_Port_Scanner-blue?style=for-the-badge" alt="Masscan">
  <img src="https://img.shields.io/badge/Language-C-orange?style=for-the-badge" alt="Language C">
  <img src="https://img.shields.io/badge/Category-Recon_&_Scanning-green?style=for-the-badge" alt="Reconnaissance et numérisation">
  <img src="https://img.shields.io/badge/License-AGPLv3-red?style=for-the-badge" alt="Licence AGPLv3">
</p>

<p align="center">
  <b>🌐 Scanner de port TCP asynchrone hautes performances capable d'analyser l'intégralité de l'Internet IPv4 en moins de 5 minutes</b>
</p>

---

## 📋 Table des matières

- [Vue d'ensemble et l'architecture asynchrone](#-overview--l'architecture-asynchrone)
-[Comparaison d'outils : Masscan vs Nmap vs RustScan](#-tool-comparison-masscan-vs-nmap-vs-rustscan)
- [Installation](#-installation)
-[Utilisation de base et cibles d'analyse](#-basic-usage--scanning-targets)
-[Contrôle du taux de transmission (`--rate`)](#-transmission-rate-control---rate)
- [Spécifications du port (TCP et UDP)](#-port-spécifications-tcp--udp)
-[Saisie de bannières et sondage de protocole de base](#-banner-grabbing--basic-protocol-probing)
- [Exclusions cibles et listes noires (`--excludefile`)](#-target-exclusions--blacklists---excludefile)
- [Formats de sortie et canalisation vers Nmap](#-output-formats--piping-into-nmap)
- [Tableau de référence rapide](#-quick-reference-table)
- [Considérations défensives et limites matérielles](#-considérations-défensives--limites-matérielles)
- [Mentions légales](#-legal-disclaimer)

---

## 🎯 Présentation et architecture asynchrone

Créé par **Robert Graham**, **Masscan** est salué comme le scanner de ports le plus rapide qui existe. Il produit des résultats similaires à Nmap, mais fonctionne en interne comme `scanrand`, `unicornscan` et `ZMap`, en utilisant la **transmission asynchrone**.

Contrairement à Nmap, qui utilise la pile TCP du noyau du système d'exploitation et attend les réponses avant de sonder les ports suivants, Masscan intègre sa propre pile TCP/IP d'espace utilisateur personnalisée. Il diffuse les paquets SYN bruts à des taux supérieurs à **10 millions de paquets par seconde** sans maintenir les tables d'état de connexion.

---

## ⚖️ Comparaison d'outils : Masscan vs Nmap vs RustScan

| Fonctionnalité | Masscan | Nmap | Analyse de rouille |
|---------|---------|------|--------------|
| **Taux de paquets maximum** | 10 000 000 de paquets/sec | ~1 000 paquets/sec | Limite de socket du système d’exploitation |
| **Analyse Internet complète (/0)**| ✅ Moins de 5 minutes | ❌ Jours/Semaines | ❌ Non conçu pour /0 |
| **Implémentation de la pile TCP**| SYN brut de l’espace utilisateur personnalisé | Noyau du système d'exploitation / Sockets bruts | Sockets TCP Tokio asynchrones |
| **Version approfondie et scripts NSE**| ❌ Bannières primitives | ✅ Plus de 600 scripts NSE | ✅ Via le transfert Nmap |
| **Suivi de l'état** | Apatride (Blackrock PRNG) | Pool de connexions avec état | Tâches asynchrones avec état |
| **Portée cible idéale** | Blocs de classe A/B (/8, /16)| Cibles uniques et /24 sous-réseaux | Machines uniques et /24 sous-réseaux |

---

## 📥Installation
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y masscan

# Arch Linux
sudo pacman -S masscan

# Build from source (Latest GitHub version)
sudo apt install -y git gcc make libpcap-dev
git clone https://github.com/robertdavidgraham/masscan
cd masscan && make -j$(nproc)
sudo make install
```
---

## 🚀 Cibles d'utilisation et d'analyse de base
```bash
# Scan a single IP for top web ports
sudo masscan 10.10.10.50 -p80,443,8080 --rate 1000

# Scan an entire Class B internal subnet (172.16.0.0/16) for SMB port 445
sudo masscan 172.16.0.0/16 -p445 --rate 5000

# Scan all 65,535 ports on a specific IP
sudo masscan 10.10.10.50 -p1-65535 --rate 2000
```
> [!IMPORTANT]
> Étant donné que Masscan génère des paquets bruts via `libpcap`, il **doit être exécuté avec les privilèges root/sudo**.

---

## ⚡ Contrôle du débit de transmission (`--rate`)

Le paramètre `--rate` détermine le nombre de paquets transmis par Masscan par seconde. Son étalonnage est essentiel pour éviter de saturer les équipements réseau :
```bash
# Gentle rate for home routers / WiFi (100–500 pkts/sec)
sudo masscan 10.10.10.0/24 -p1-1000 --rate 300

# Standard corporate gigabit internal network rate (5,000–10,000 pkts/sec)
sudo masscan 10.0.0.0/8 -p80,443 --rate 10000

# High-speed data center 10Gbps uplink rate (100,000+ pkts/sec)
sudo masscan 0.0.0.0/0 -p443 --rate 100000
```
---

## 🔌 Spécifications des ports (TCP et UDP)

Masscan prend en charge simultanément les listes de ports TCP et UDP :
```bash
# Specific TCP ports
sudo masscan 10.10.10.50 -p21,22,80,443,445,3389

# Top ports shortcut (similar to Nmap)
sudo masscan 10.10.10.50 --top-ports 100

# UDP ports (prefixed with U:)
sudo masscan 10.10.10.50 -p U:53,U:161,U:123 --rate 500
```
---

## 🔍 Saisie de bannières et sondage de protocole de base

Bien que Masscan soit avant tout un scanner SYN, il peut effectuer des poignées de main à trois voies pour récupérer les bannières d'application :
```bash
# Grab HTTP, FTP, and SSH banners
sudo masscan 10.10.10.0/24 -p80,21,22 --banners --rate 1000
```
---

## 🚫 Exclusions de cibles et listes noires (`--excludefile`)

Lorsque vous effectuez des tests d’intrusion à grande échelle, excluez toujours les infrastructures critiques et les adresses interdites :
```bash
# Exclude government and military ranges or local gateway
sudo masscan 10.0.0.0/8 -p80 --rate 2000 --exclude 10.0.0.1,10.1.1.1

# Use an exclusion file (e.g., standard exclude.conf)
sudo masscan 192.168.0.0/16 -p445 --rate 2000 --excludefile /etc/masscan/exclude.conf
```
---

## 📊 Formats de sortie et canalisation vers Nmap

Enregistrez les hôtes découverts aux formats greppable ou JSON, puis introduisez-les dans Nmap pour une évaluation complète des vulnérabilités :
```bash
# Save to JSON format
sudo masscan 10.10.10.0/24 -p80,443 -oJ masscan_results.json

# Save to simple List format (-oL)
sudo masscan 10.10.10.0/24 -p1-65535 --rate 2000 -oL open_ports.txt

# Extract discovered ports and feed directly to Nmap
cat open_ports.txt | awk '{print $4}' | sort -u | paste -sd, - | xargs -I{} nmap -sC -sV -p{} 10.10.10.50
```
---

## ⚡ Tableau de référence rapide

| Objectif | Commande |
|-----------|---------|
| **Analyser un hôte unique (tous les ports)** | `sudo masscan <IP> -p1-65535 --rate 2000` |
| **Analyser le sous-réseau CIDR pour le port** | `sudo masscan <SUBNET>/16 -p<PORT> --rate 5000` |
| **Scanner avec Banner Grabbing** | `sudo masscan <IP> -p80,22 --banners --rate 1000` |
| **Analyser les ports UDP** | `sudo masscan <IP> -p U:53,U:161 --rate 500` |
| **Exclure les adresses IP** | `sudo masscan <NET> -p80 --exclude <IP1>,<IP2>` |
| **Sortie vers JSON** | `sudo masscan <IP> -p80,443 -oJ output.json` |
| **Sortie vers une liste simple** | `sudo masscan <IP> -p80,443 -oL output.txt` |

---

## 🛡️ Considérations défensives et limites matérielles

1. **Crash du routeur grand public :** La configuration de `--rate 100000` sur un routeur DSL/câble domestique ou sur un réseau Wi-Fi standard submergera sa table d'état de suivi de connexion NAT et fera planter le routeur. Gardez `--rate` en dessous de `1000` sur les connexions résidentielles.
2. **Consommation de bande passante :** La transmission à 10 millions de paquets par seconde consomme environ 5,4 Gbit/s de bande passante réseau.
3. **Rejet du pare-feu local (RST) :** Étant donné que Masscan utilise une pile TCP d'espace utilisateur personnalisée, votre noyau Linux ne connaît pas les connexions ouvertes et peut envoyer des paquets TCP RST automatiques aux cibles. Pour éviter cela lors des captures de bannières :
   ```bash
   sudo iptables -A INPUT -p tcp --dport 60000 -j DROP
   sudo masscan ... --source-port 60000
   ```
---

## ⚠️ Mentions légales

> [!ATTENTION]
> Masscan est un utilitaire de reconnaissance de réseau exceptionnellement puissant. L'analyse de réseaux non autorisés, de plages IP gouvernementales ou de sous-réseaux Internet publics peut enfreindre les lois sur les télécommunications et la cybersécurité. Vérifiez toujours l’autorisation écrite avant de lancer des analyses.