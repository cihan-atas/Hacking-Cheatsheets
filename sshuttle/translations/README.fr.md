<!-- language-home-navigation -->
[🏠 Page d'accueil Française](../../README.fr.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🛰️ sshuttle - Aide-mémoire complet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ███████╗███████╗██╗  ██╗██╗   ██╗████████╗████████╗██╗     ███████╗
  ██╔════╝██╔════╝██║  ██║██║   ██║╚══██╔══╝╚══██╔══╝██║     ██╔════╝
  ███████╗███████╗███████║██║   ██║   ██║      ██║   ██║     █████╗  
  ╚════██║╚════██║██╔══██║██║   ██║   ██║      ██║   ██║     ██╔══╝  
  ███████║███████║██║  ██║╚██████╔╝   ██║      ██║   ███████╗███████╗
  ╚══════╝╚══════╝╚═╝  ╚═╝ ╚═════╝    ╚═╝      ╚═╝   ╚══════╝╚══════╝
       Transparent Proxy Server that Works as a Poor Man's VPN over SSH
```
<p align="center">
  <img src="https://img.shields.io/badge/sshuttle-Poor_Man's_VPN-blue?style=for-the-badge" alt="sshuttle">
  <img src="https://img.shields.io/badge/Category-Pivoting_&_Tunneling-orange?style=for-the-badge" alt="Pivotement et tunneling">
  <img src="https://img.shields.io/badge/Protocols-TCP_DNS_SSH-green?style=for-the-badge" alt="Protocoles">
  <img src="https://img.shields.io/badge/License-LGPLv2.1-red?style=for-the-badge" alt="Licence LGPLv2.1">
</p>

<p align="center">
  <b>🌐 Transférer des sous-réseaux internes entiers et des requêtes DNS via une session SSH non privilégiée sans accès administratif sur le serveur distant</b>
</p>

---

## 📋 Table des matières

-[Vue d'ensemble et philosophie du "VPN du pauvre"](#-overview--the-poor-mans-vpn-philosophy)
- [Comment fonctionne la navette](#-comment-fonctionne-la-shuttle)
- [Comparaison d'outils : sshuttle vs SOCKS vs Ligolo-ng vs OpenVPN](#-tool-comparison-sshuttle-vs-socks-vs-ligolo-ng-vs-openvpn)
- [Installation](#-installation)
- [Routage de sous-réseau de base](#-basic-subnet-routing)
- [Redirection du trafic DNS (`--dns`)](#-dns-traffic-forwarding---dns)
- [Redirection complète de la passerelle (route par défaut)](#-full-gateway-redirection-default-route)
- [Exclusions de sous-réseau (`-x`)](#-subnet-exclusions--x)
- [Options SSH et authentification par clé (`-e`)](#-ssh-options--key-based-authentication--e)
-[Exécution en arrière-plan du démon (`-D`)](#-daemon-background-execution--d)
- [Tableau de référence rapide](#-quick-reference-table)
- [Considérations défensives et OPSEC](#-defensive-considérations--opsec)
- [Mentions légales](#-legal-disclaimer)

---

## 🎯 Présentation et philosophie du « VPN du pauvre »

**sshuttle** est un serveur proxy transparent qui fonctionne comme un VPN léger sans configuration sur une connexion SSH standard.

Dans les tests d'intrusion typiques en entreprise et les évaluations Red Team, la transition vers les réseaux internes est entravée par des contraintes strictes :
- ❌ Vous possédez rarement un accès `root` ou `SYSTEM` sur l'hôte bastion/pivot SSH initial.
- ❌ Les solutions VPN traditionnelles (OpenVPN, WireGuard) nécessitent des droits d'administrateur et des périphériques réseau virtuels TUN/TAP sur les deux points de terminaison.
- ❌ Les proxys SOCKS standard (comme `ssh -D`) nécessitent la configuration de `proxychains` ou des paramètres de proxy d'application individuels pour chaque outil.

**sshuttle résout les trois limitations simultanément :**
- ✅ **Aucune racine distante requise :** Le serveur pivot n'a besoin que d'une installation Python standard (`python3`).
- ✅ **Aucun TUN/TAP distant n'est nécessaire :** Utilise le multiplexage de flux TCP pur sur la session SSH.
- ✅ **Routage transparent à l'échelle du système :** Modifie `iptables` / `nftables` local sur le boîtier de l'attaquant, permettant aux outils (`nmap`, `curl`, `smbclient`, navigateurs Web) de cibler les IP internes directement sans wrappers !

---

## ⚙️ Comment fonctionne la navette
```
[ Attacker Box (Local Root) ]
  ├── iptables / nftables redirects internal IP traffic (10.10.10.0/24) to local port
  └── sshuttle client multiplexes TCP packets over SSH tunnel
              │
              ▼ (Encrypted SSH Channel - Port 22)
[ Remote Pivot Server (Unprivileged User + Python) ]
  └── Assembles TCP connections and forwards them directly to internal hosts
              │
              ▼
[ Target Internal Network (10.10.10.0/24) ]
```
---

## ⚖️ Comparaison d'outils : sshuttle vs SOCKS vs Ligolo-ng vs OpenVPN

| Fonctionnalité | `sshuttle` | CHAUSSETTES (`ssh -D`) | `Ligolo-ng` | `OpenVPN` |
|---------|------------|--------|-------------|---------------|
| **Racine à distance requise** | ❌ Non | ❌ Non | ❌ Non | ✅ Oui |
| **Racine locale requise** | ✅ Oui (iptables) | ❌ Non | ✅ Oui (interface TUN) | ✅ Oui |
| **Routage transparent** | ✅ Oui | ❌ Nécessite des chaînes proxy | ✅ Oui (TUN natif) | ✅ Oui |
| **Prise en charge du trafic UDP** | ❌ DNS uniquement | ❌ Non | ✅ Oui | ✅ UDP complet |
| **Nécessite Python sur Target**| ✅ Oui (Python 3) | ❌ Non | ❌ Non (Agent autonome)| ❌ Non |
| **Nmap SYN Scan (`-sS`)** | ❌ Connexion TCP (`-sT`)| ❌ Connexion TCP uniquement | ✅ Prise en charge des paquets bruts | ✅ Paquets bruts complets |

---

## 📥Installation
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y sshuttle

# Arch Linux
sudo pacman -S sshuttle

# macOS (Homebrew)
brew install sshuttle

# Python Pipx / Pip
pipx install sshuttle
```
---

## 🚀 Routage de sous-réseau de base

Transférez l’intégralité d’un sous-réseau distant de classe C via l’hôte pivot SSH :
```bash
# Route 10.10.10.0/24 through user@192.168.1.50
sshuttle -r user@192.168.1.50 10.10.10.0/24

# Route multiple disjoint subnets simultaneously
sshuttle -r user@192.168.1.50 10.10.10.0/24 172.16.0.0/16 192.168.100.0/24
```
Une fois connecté, vous pouvez communiquer directement avec les machines internes :
```bash
# No proxychains needed!
curl http://10.10.10.25:8080/
smbclient -L //10.10.10.10/ -U 'domain/user%pass'
```
---

## 🌐 Transfert de trafic DNS (`--dns`)

Transférez les requêtes DNS internes de l'entreprise (`.corp`, `.local`) vers le serveur DNS interne du réseau distant :
```bash
# Route subnet and proxy internal DNS queries
sshuttle --dns -r user@192.168.1.50 10.10.10.0/24

# Query internal domain directly via nslookup/dig
dig dc01.internal.corp
```
---

## 🌍 Redirection complète de la passerelle (itinéraire par défaut)

Acheminez tout le trafic sortant des attaquants via la connexion SSH (comportement VPN complet) :
```bash
# Route all traffic through remote SSH server (0.0.0.0/0)
sshuttle -r user@remote-vps.com 0.0.0.0/0

# Route all traffic except local network
sshuttle -r user@remote-vps.com 0.0.0.0/0 -x 192.168.1.0/24
```
---

## 🚫 Exclusions de sous-réseau (`-x`)

Empêchez la capture de sous-réseaux sensibles ou de bouclage spécifiques :
```bash
# Route entire 10.0.0.0/8 network, but exclude the pivot host itself
sshuttle -r user@10.0.0.50 10.0.0.0/8 -x 10.0.0.50/32
```
---

## 🔑 Options SSH et authentification par clé (`-e`)

Transmettez les indicateurs SSH personnalisés (clés d'identité privées, ports SSH non standard) :
```bash
# Use custom identity file and non-standard SSH port 2222
sshuttle -e 'ssh -i /path/to/id_rsa -p 2222' -r user@192.168.1.50 10.10.10.0/24
```
---

## 🤖 Exécution en arrière-plan du démon (`-D`)

Exécutez sshuttle en tant que démon en arrière-plan pendant les engagements en cours :
```bash
# Start background daemon with pidfile
sudo sshuttle -D --pidfile /tmp/sshuttle.pid -r user@192.168.1.50 10.10.10.0/24

# Terminate daemon when finished
sudo kill $(cat /tmp/sshuttle.pid)
```
---

## ⚡ Tableau de référence rapide

| Objectif | Commande |
|-----------|---------|
| **Router un seul sous-réseau** | `sshuttle -r <USER>@<PIVOT> <SUBNET>/24` |
| **Sous-réseau d'acheminement avec DNS** | `sshuttle --dns -r <USER>@<PIVOT> <SUBNET>/24` |
| **Acheminer plusieurs sous-réseaux** | `sshuttle -r <USER>@<PIVOT> <NET1>/24 <NET2>/16` |
| **Exclure IP/sous-réseau** | `sshuttle -r <USER>@<PIVOT> <SUBNET>/24 -x <EXCLUDE_IP>` |
| **Redirection complète du trafic** | `sshuttle -r <USER>@<PIVOT> 0.0.0.0/0` |
| **Clé/Port SSH personnalisé** | `sshuttle -e 'ssh -i <KEY> -p <PORT>' -r <USER>@<PIVOT> <SUBNET>` |
| **Exécuter en arrière-plan** | `sshuttle -D --pidfile /tmp/sshuttle.pid -r <USER>@<PIVOT> <SUBNET>` |

---

## 🛡️ Considérations défensives et OPSEC

1. **Modification du pare-feu :** sshuttle modifie les tables de pare-feu locales (`iptables` sous Linux, `pf` sous macOS). Si sshuttle plante de manière incorrecte, utilisez `iptables -F` ou redémarrez le service de pare-feu pour supprimer les règles de redirection obsolètes.
2. **Limitation TCP uniquement :** Les paquets UDP non DNS, les demandes d'écho ICMP (ping) et les analyses SYN ne sont pas pris en charge. Utilisez toujours `nmap -sT -Pn -n` pour l'analyse des ports via Sshuttle.
3. **Target Footprint :** sshuttle télécharge automatiquement un petit script d'assemblage Python sur l'hôte pivot distant lors de l'initialisation de la session et l'exécute via `python3`.

---

## ⚠️ Mentions légales

> [!ATTENTION]
> Cette aide-mémoire est destinée uniquement aux tests d'intrusion autorisés et à l'administration du système. Le routage et l'interception non autorisés du trafic réseau sont illégaux.