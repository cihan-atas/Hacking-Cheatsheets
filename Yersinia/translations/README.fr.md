<!-- language-home-navigation -->
[🏠 Page d'accueil Française](../../README.fr.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# Aide-mémoire de Yersinia

[Yersinia](https://github.com/tomac/yersinia) est un puissant cadre de test d'intrusion réseau de couche 2 (couche de liaison de données) conçu pour analyser et exploiter les faiblesses des protocoles de couche 2 tels que STP (Spanning Tree Protocol), CDP (Cisco Discovery Protocol), DTP (Dynamic Trunking Protocol), DHCP, HSRP et 802.1Q VLAN trunking.

---

## Table des matières

- [Protocoles et modes pris en charge](#supported-protocols--modes)
- [Installation](#installation)
- [Mode Malédictions interactives (`-I`)](#interactive-curses-mode--i)
- [Attaques du protocole Spanning Tree (STP)](#spanning-tree-protocol-stp-attacks)
-[Attaques Cisco Discovery Protocol (CDP)](#cisco-discovery-protocol-cdp-attacks)
- [Protocole de liaison dynamique (DTP) et saut de VLAN](#dynamic-trunking-protocol-dtp--vlan-hopping)
- [DHCP Rogue & Starvation Attacks](#dhcp-rogue--starvation-attacks)
- [Détournement HSRP et VRRP](#hsrp--vrrp-hijacking)
- [Atténuation et durcissement](#atténuation--durcissement)

---

## Protocoles et modes pris en charge

Yersinia cible les protocoles fonctionnant sous IP :
- **STP :** Spanning Tree Protocol (reprise du rôle de pont racine, notifications de changement de topologie).
- **CDP :** Cisco Discovery Protocol (inondation de périphériques, injection de faux voisins).
- **DTP :** Dynamic Trunking Protocol (négociation des liaisons réseau pour accéder à tous les VLAN).
- **DHCP :** Protocole de configuration d'hôte dynamique (serveurs DHCP Starvation et malveillants).
- **HSRP :** Hot Standby Router Protocol (prise de contrôle active de la passerelle).
- **802.1Q / 802.1X :** Saut de VLAN Dot1Q et épuisement EAPOL.
- **VTP :** VLAN Trunking Protocol (suppression/écrasement de la base de données VLAN).

Modes d'exécution :
- **`-I` (Interactive Curses) :** Interface utilisateur du terminal plein écran pour sélectionner les protocoles, configurer les attaques et surveiller les paquets en direct.
- **CLI non interactive :** Exécution directe pour les scripts automatisés.

---

##Installation
```bash
# Debian / Ubuntu / Kali Linux
sudo apt-get update && sudo apt-get install -y yersinia

# Arch Linux / BlackArch
sudo pacman -S yersinia

# Run in terminal
yersinia -h
```
---

## Mode Malédictions interactives (`-I`)

Le moyen le plus pratique et le plus sûr d'utiliser Yersinia dans le terminal sans interface graphique :
```bash
sudo yersinia -I
```
- **`h`** : Afficher l'aide et les raccourcis clavier.
- **`g`** : Protocoles de commutation (STP, CDP, DTP, DHCP, HSRP, etc.).
- **`x`** : Exécute le menu d'attaque pour le protocole actuel.
- **`c`** : Annule l'attaque en cours.
- **`l`** : activez la journalisation des paquets.
- **`q`** : Quittez Yersinia.

---

## Attaques du protocole Spanning Tree (STP)

STP choisit un pont racine avec l'ID de pont le plus bas (Priorité + MAC). Yersinia peut injecter de faux BPDU (Bridge Protocol Data Units) pour revendiquer le statut root et rediriger le trafic via l'hôte de l'attaquant.

### 1. Réclamer le rôle racine (injection BPDU)
Envoyez des BPDU avec la priorité `0` et une adresse MAC inférieure pour devenir le pont racine STP :
```bash
sudo yersinia stp -attack 1 -i eth0
```
### 2. Réclamez le rôle racine avec le changement de topologie (TCN)
Forcez tous les commutateurs à vider les tables CAM et à reconverger vers l'attaquant :
```bash
sudo yersinia stp -attack 2 -i eth0
```
### 3. Attaque DoS STP (BPDU de configuration Flood)
Crash ou saturation du processeur du commutateur avec des trames BPDU en conflit massif :
```bash
sudo yersinia stp -attack 0 -i eth0
```
---

## Attaques du protocole de découverte Cisco (CDP)

CDP partage les noms, modèles, fonctionnalités et versions logicielles des appareils non authentifiés sur les appareils Cisco.

### 1. Inonder les faux voisins du CDP
Épuisez la mémoire du commutateur et créez des appareils fantômes dans `show cdp neighbors` :
```bash
sudo yersinia cdp -attack 0 -i eth0
```
### 2. Capturez et inspectez les paquets CDP légitimes
Exécutez le mode Cursus interactif (`yersinia -I`), appuyez sur `g`, sélectionnez `CDP` et observez le nom d'hôte du commutateur, l'adresse IP de gestion, le modèle matériel et l'ID de VLAN natif.

---

## Protocole de liaison dynamique (DTP) et saut de VLAN

DTP négocie automatiquement les liaisons trunking si les ports du commutateur sont configurés en mode `dynamic desirable` ou `dynamic auto`.

### 1. Négocier la liaison trunking (paquet DTP souhaité)
Astuce pour que le port du commutateur se transforme en joncteur réseau 802.1Q :
```bash
sudo yersinia dtp -attack 0 -i eth0
```
Une fois le tronc négocié, capturez les trames de tous les VLAN ou marquez les paquets dans des sous-réseaux isolés à l'aide des interfaces Linux 802.1Q :
```bash
# Add VLAN subinterface for newly accessed VLAN (e.g. VLAN 20)
sudo ip link add link eth0 name eth0.20 type vlan id 20
sudo ip link set dev eth0.20 up
sudo dhclient eth0.20
```
---

## Attaques malveillantes et de famine DHCP

### 1. Attaque de famine DHCP (pool d'adresses IP d'échappement)
Envoyez des milliers de requêtes DHCP DISCOVER avec des adresses MAC aléatoires pour consommer toutes les adresses IP disponibles dans le pool :
```bash
sudo yersinia dhcp -attack 1 -i eth0
```
### 2. Configurer un serveur DHCP malveillant
Déployez un serveur DHCP malveillant avec un DNS et une passerelle personnalisés :
```bash
sudo yersinia dhcp -attack 2 -i eth0
```
---

## Détournement HSRP et VRRP

HSRP permet à plusieurs routeurs de présenter une seule passerelle virtuelle par défaut.

### 1. Réclamez la priorité du routeur actif
Injectez des paquets HSRP HELLO avec la priorité 255 pour prendre en charge l'adresse IP de la passerelle par défaut :
```bash
sudo yersinia hsrp -attack 0 -i eth0
```
---

## Atténuation et durcissement

| Protocole | Vecteur d'attaque | Cisco / Atténuation standard |
|---|---|---|
| **STP** | Injection de faux pont radiculaire | `spanning-tree bpduguard enable` sur les ports d'accès ; `spanning-tree rootguard` sur les liaisons inter-commutateurs. |
| **PAO** | Négociation de troncs malveillants | `switchport mode access` et `switchport nonegotiate` sur tous les ports d'accès destinés aux utilisateurs. |
| **CDP** | Inondations / Divulgation d'informations | `no cdp run` (globalement) ou `no cdp enable` sur les ports d'interface non approuvés. |
| **DHCP** | Famine et serveur voyou | Activez `ip dhcp snooping`, limitez le débit par port, configurez les ports de liaison montante de confiance. |
| **HSRP** | Détournement prioritaire | Configurez l'authentification MD5 avec des clés secrètes (`standby 1 authentication md5 key-string <SECRET>`). |