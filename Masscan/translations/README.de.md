<!-- language-home-navigation -->
[🏠 Deutsche Startseite](../../README.de.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ⚡ Massscan – Vollständiger Cheatsheet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ███╗   ███╗ █████╗ ███████╗███████╗ ██████╗ █████╗ ███╗   ██╗
  ████╗ ████║██╔══██╗██╔════╝██╔════╝██╔════╝██╔══██╗████╗  ██║
  ██╔████╔██║███████║███████╗███████╗██║     ███████║██╔██╗ ██║
  ██║╚██╔╝██║██╔══██║╚════██║╚════██║██║     ██╔══██║██║╚██╗██║
  ██║ ╚═╝ ██║██║  ██║███████║███████║╚██████╗██║  ██║██║ ╚████║
  ╚═╝     ╚═╝╚═╝  ╚═╝╚══════╝╚══════╝ ╚═════╝╚═╝  ╚═╝╚═╝  ╚═══╝
       The Fastest Internet Port Scanner - Asynchronous 10M Packets/Second
```
<p align="center">
  <img src="https://img.shields.io/badge/Masscan-Internet_Port_Scanner-blue?style=for-the-badge" alt="Masscan">
  <img src="https://img.shields.io/badge/Language-C-orange?style=for-the-badge" alt="Language C">
  <img src="https://img.shields.io/badge/Category-Recon_&_Scanning-green?style=for-the-badge" alt="Recon & Scanning">
  <img src="https://img.shields.io/badge/License-AGPLv3-red?style=for-the-badge" alt="License AGPLv3">
</p>

<p align="center">
  <b>🌐 Leistungsstarker asynchroner TCP-Port-Scanner, der das gesamte IPv4-Internet in weniger als 5 Minuten scannen kann</b>
</p>

---

## 📋 Inhaltsverzeichnis

- [Überblick und die asynchrone Architektur](#-overview--the-asynchronous-architecture)
- [Tool-Vergleich: Masscan vs. Nmap vs. RustScan](#-tool-comparison-masscan-vs-nmap-vs-rustscan)
- [Installation](#-Installation)
- [Grundlegende Nutzung und Scanziele](#-basic-usage--scanning-targets)
- [Transmission Rate Control (`--rate`)](#-transmission-rate-control---rate)
- [Portspezifikationen (TCP & UDP)](#-port-spezifikationen-tcp--udp)
- [Banner Grabbing & Basic Protocol Probing](#-banner-grabbing--basic-protocol-probing)
- [Zielausschlüsse und Blacklists (`--excludefile`)](#-target-exclusions--blacklists---excludefile)
- [Ausgabeformate und Weiterleitung an Nmap](#-output-formats--piping-into-nmap)
- [Kurzreferenztabelle](#-quick-reference-table)
- [Defensive Überlegungen und Hardware-Limits](#-defensive-considerations--hardware-limits)
- [Rechtlicher Haftungsausschluss](#-legal-disclaimer)

---

## 🎯 Überblick und die asynchrone Architektur

**Masscan** wurde von **Robert Graham** entwickelt und gilt als der schnellste Port-Scanner, den es gibt. Es liefert ähnliche Ergebnisse wie Nmap, funktioniert aber intern wie `scanrand`, `unicornscan` und `ZMap` und nutzt **asynchrone Übertragung**.

Im Gegensatz zu Nmap, das den Kernel-TCP-Stack des Betriebssystems verwendet und auf Antworten wartet, bevor es nachfolgende Ports prüft, enthält Masscan seinen eigenen benutzerdefinierten TCP/IP-Stack für den Benutzerbereich. Es sendet rohe SYN-Pakete mit Raten von mehr als **10 Millionen Paketen pro Sekunde**, ohne Verbindungsstatustabellen zu pflegen.

---

## ⚖️ Tool-Vergleich: Massscan vs. Nmap vs. RustScan

| Funktion | Massenscan | Nmap | RustScan |
|---------|---------|------|----------|
| **Maximale Paketrate** | 10.000.000 Pakete/Sek. | ~1.000 Pakete/Sek. | Betriebssystem-Socket-Limit |
| **Gesamter Internet-Scan (/0)**| ✅ Unter 5 Minuten | ❌ Tage / Wochen | ❌ Nicht für /0 | konzipiert
| **TCP-Stack-Implementierung**| Benutzerdefinierter User-Space Raw SYN| Betriebssystemkernel/Raw-Sockets| Asynchrone Tokio TCP-Sockets |
| **Deep Version & NSE-Skripte**| ❌ Primitive Banner | ✅ Über 600 NSE-Skripte | ✅ Über Nmap-Übergabe |
| **Statusverfolgung** | Staatenlos (Blackrock PRNG) | Zustandsbehafteter Verbindungspool| Zustandsbehaftete asynchrone Aufgaben |
| **Ideales Zielfernrohr** | Blöcke der Klasse A/B (/8, /16)| Einzelne Ziele und /24-Subnetze | Einzelne Maschinen und /24 Subnetze |

---

## 📥 Installation
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y masscan

# Arch Linux
sudo pacman -S masscan

# Build from source (Latest GitHub version)
sudo apt install -y git gcc make libpcap-dev
git clone https://github.com/robertdavidgraham/masscan
cd masscan && make -j$(nproc)
sudo make install
```
---

## 🚀 Grundlegende Nutzungs- und Scanziele
```bash
# Scan a single IP for top web ports
sudo masscan 10.10.10.50 -p80,443,8080 --rate 1000

# Scan an entire Class B internal subnet (172.16.0.0/16) for SMB port 445
sudo masscan 172.16.0.0/16 -p445 --rate 5000

# Scan all 65,535 ports on a specific IP
sudo masscan 10.10.10.50 -p1-65535 --rate 2000
```
> [!IMPORTANT]
> Da Masscan Rohpakete über `libpcap` generiert, muss es **mit Root-/Sudo-Berechtigungen ausgeführt werden**.

---

## ⚡ Übertragungsratensteuerung (`--rate`)

Der Parameter `--rate` bestimmt, wie viele Pakete pro Sekunde Masscan überträgt. Die Kalibrierung ist wichtig, um eine Überlastung der Netzwerkgeräte zu vermeiden:
```bash
# Gentle rate for home routers / WiFi (100–500 pkts/sec)
sudo masscan 10.10.10.0/24 -p1-1000 --rate 300

# Standard corporate gigabit internal network rate (5,000–10,000 pkts/sec)
sudo masscan 10.0.0.0/8 -p80,443 --rate 10000

# High-speed data center 10Gbps uplink rate (100,000+ pkts/sec)
sudo masscan 0.0.0.0/0 -p443 --rate 100000
```
---

## 🔌 Port-Spezifikationen (TCP & UDP)

Masscan unterstützt gleichzeitig TCP- und UDP-Portlisten:
```bash
# Specific TCP ports
sudo masscan 10.10.10.50 -p21,22,80,443,445,3389

# Top ports shortcut (similar to Nmap)
sudo masscan 10.10.10.50 --top-ports 100

# UDP ports (prefixed with U:)
sudo masscan 10.10.10.50 -p U:53,U:161,U:123 --rate 500
```
---

## 🔍 Banner-Grabbing und grundlegende Protokollprüfung

Während Masscan in erster Linie ein SYN-Scanner ist, kann es Drei-Wege-Handshakes durchführen, um Anwendungsbanner abzurufen:
```bash
# Grab HTTP, FTP, and SSH banners
sudo masscan 10.10.10.0/24 -p80,21,22 --banners --rate 1000
```
---

## 🚫 Zielausschlüsse und Blacklists (`--excludefile`)

Schließen Sie bei der Durchführung weitreichender Penetrationstests immer kritische Infrastrukturen und Off-Limit-Adressen aus:
```bash
# Exclude government and military ranges or local gateway
sudo masscan 10.0.0.0/8 -p80 --rate 2000 --exclude 10.0.0.1,10.1.1.1

# Use an exclusion file (e.g., standard exclude.conf)
sudo masscan 192.168.0.0/16 -p445 --rate 2000 --excludefile /etc/masscan/exclude.conf
```
---

## 📊 Ausgabeformate und Weiterleitung an Nmap

Speichern Sie entdeckte Hosts in Greppable- oder JSON-Formaten und geben Sie sie dann zur vollständigen Schwachstellenbewertung in Nmap ein:
```bash
# Save to JSON format
sudo masscan 10.10.10.0/24 -p80,443 -oJ masscan_results.json

# Save to simple List format (-oL)
sudo masscan 10.10.10.0/24 -p1-65535 --rate 2000 -oL open_ports.txt

# Extract discovered ports and feed directly to Nmap
cat open_ports.txt | awk '{print $4}' | sort -u | paste -sd, - | xargs -I{} nmap -sC -sV -p{} 10.10.10.50
```
---

## ⚡ Kurzreferenztabelle

| Ziel | Befehl |
|-----------|---------|
| **Einzelnen Host scannen (alle Ports)** | `sudo masscan <IP> -p1-65535 --rate 2000` |
| **CIDR-Subnetz nach Port durchsuchen** | `sudo masscan <SUBNET>/16 -p<PORT> --rate 5000` |
| **Scannen mit Bannergrabbing** | `sudo masscan <IP> -p80,22 --banners --rate 1000` |
| **UDP-Ports scannen** | `sudo masscan <IP> -p U:53,U:161 --rate 500` |
| **IP-Adressen ausschließen** | `sudo masscan <NET> -p80 --exclude <IP1>,<IP2>` |
| **Ausgabe an JSON** | `sudo masscan <IP> -p80,443 -oJ output.json` |
| **Ausgabe in einfache Liste** | `sudo masscan <IP> -p80,443 -oL output.txt` |

---

## 🛡️ Defensive Überlegungen und Hardware-Limits

1. **Absturz des Consumer-Routers:** Die Einstellung von `--rate 100000` auf einem Heim-DSL-/Kabelrouter oder einem Standard-WLAN führt zu einer Überlastung der NAT-Verbindungsverfolgungsstatustabelle und zum Absturz des Routers. Halten Sie `--rate` bei Wohnanschlüssen unter `1000`.
2. **Bandbreitenverbrauch:** Die Übertragung mit 10 Millionen Paketen pro Sekunde verbraucht etwa 5,4 Gbit/s Netzwerkbandbreite.
3. **Local Firewall Rejection (RST):** Da Masscan einen benutzerdefinierten User-Space-TCP-Stack verwendet, weiß Ihr Linux-Kernel nichts von offenen Verbindungen und sendet möglicherweise automatische TCP-RST-Pakete an Ziele. Um dies bei Banner-Grabs zu vermeiden:
   ```bash
   sudo iptables -A INPUT -p tcp --dport 60000 -j DROP
   sudo masscan ... --source-port 60000
   ```
---

## ⚠️ Rechtlicher Haftungsausschluss

> [!ACHTUNG]
> Masscan ist ein außergewöhnlich leistungsstarkes Dienstprogramm zur Netzwerkaufklärung. Das Scannen nicht autorisierter Netzwerke, staatlicher IP-Bereiche oder öffentlicher Internet-Subnetze kann gegen Telekommunikations- und Cybersicherheitsgesetze verstoßen. Überprüfen Sie immer die schriftliche Genehmigung, bevor Sie mit Scans beginnen.