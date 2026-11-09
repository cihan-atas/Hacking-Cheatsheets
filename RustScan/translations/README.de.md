<!-- language-home-navigation -->
[🏠 Deutsche Startseite](../../README.de.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🦀 RustScan – Vollständiger Cheatsheet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ██████╗ ██╗   ██╗███████╗████████╗███████╗ ██████╗ █████╗ ███╗   ██╗
  ██╔══██╗██║   ██║██╔════╝╚══██╔══╝██╔════╝██╔════╝██╔══██╗████╗  ██║
  ██████╔╝██║   ██║███████╗   ██║   ███████╗██║     ███████║██╔██╗ ██║
  ██╔══██╗██║   ██║╚════██║   ██║   ╚════██║██║     ██╔══██║██║╚██╗██║
  ██║  ██║╚██████╔╝███████║   ██║   ███████║╚██████╗██║  ██║██║ ╚████║
  ╚═╝  ╚═╝ ╚═════╝ ╚══════╝   ╚═╝   ╚══════╝ ╚═════╝╚═╝  ╚═╝╚═╝  ╚═══╝
       The Modern Port Scanner - Scan All 65k Ports in 3 Seconds into Nmap
```
<p align="center">
  <img src="https://img.shields.io/badge/RustScan-Port_Scanner-blue?style=for-the-badge" alt="RustScan">
  <img src="https://img.shields.io/badge/Language-Rust-orange?style=for-the-badge" alt="Language Rust">
  <img src="https://img.shields.io/badge/Category-Recon_&_Scanning-green?style=for-the-badge" alt="Recon & Scanning">
  <img src="https://img.shields.io/badge/License-GPLv3-red?style=for-the-badge" alt="License GPLv3">
</p>

<p align="center">
  <b>🌐 Schneller, anpassbarer und automatisierter Port-Scanner, der entdeckte offene Ports direkt in Nmap-Skripte weiterleitet</b>
</p>

---

## 📋 Inhaltsverzeichnis

- [Übersicht & Architektur](#-overview--architecture)
- [Tool-Vergleich: RustScan vs. Nmap vs. Masscan](#-tool-comparison-rustscan-vs-nmap-vs-masscan)
- [Installation](#-Installation)
- [Grundlegende Nutzung und Single-Host-Scanning](#-basic-usage--single-host-scanning)
- [Automatisierte Nmap-Integration](#-automated-nmap-integration)
- [Leistungsoptimierung (`ulimit` & Batch-Größe)](#-performance-tuning-ulimit--batch-size)
- [Portbereiche und CIDR-Subnetze scannen](#-scanning-port-ranges--cidr-subnets)
- [Skripterstellung und Ausgabeformatierung](#-scripting--output-formatting)
- [Kurzreferenztabelle](#-quick-reference-table)
- [Verteidigungsüberlegungen und OPSEC](#-defensive-considerations--opsec)
- [Rechtlicher Haftungsausschluss](#-legal-disclaimer)

---

## 🎯 Übersicht & Architektur

**RustScan** ist ein moderner Hochgeschwindigkeits-Portscanner, der in **Rust** entwickelt wurde. Die Kernphilosophie ist einfach: **Scannen Sie Ports blitzschnell, finden Sie offene Ports im gesamten 1-65535-Spektrum in 3 Sekunden und übergeben Sie diese offenen Ports sofort an Nmap für umfassenden Service und Schwachstellenscan**.

Anstatt 10 bis 20 Minuten darauf zu warten, dass `nmap -p-` seine sequentiellen TCP-Handshake-Sweeps abschließt, nutzt RustScan asynchrones Multithreading und Betriebssystem-Socket-Batching, um offene Ports nahezu augenblicklich zu lokalisieren.

---

## ⚖️ Tool-Vergleich: RustScan vs. Nmap vs. Masscan

| Funktion | RustScan | Nmap | Massenscan |
|---------|----------|------|---------|
| **Hauptziel** | Schnelle Erkennung -> Nmap-Pipe | Deep Service & NSE-Skripte | Internetweiter SYN-Scan mit hoher Geschwindigkeit |
| **Geschwindigkeit aller Ports (1-65535)**| ⭐⭐⭐⭐⭐ (~3 Sekunden) | ⭐⭐ (5–20 Minuten) | ⭐⭐⭐⭐⭐ (~2 Sekunden) |
| **Erkennung der Dienstversion**| Über Nmap-Pipe | Nativ (`-sV`) | ❌ Minimal |
| **NSE-Skript-Engine** | Über Nmap-Pipe | Nativ (`-sC`, `--script`) | ❌ Nein |
| **Paket-Engine** | Asynchrone TCP-Sockets | Roh-IP/benutzerdefinierter TCP-Stack | Benutzerdefinierter asynchroner SYN-Stack |
| **Einfache Automatisierung** | Native CLI-Argumente (`--`) | Umfangreiche CLI-Optionen | Hohe Komplexität |

---

## 📥 Installation
```bash
# Debian / Ubuntu / Kali Linux (.deb package)
wget https://github.com/RustScan/RustScan/releases/download/2.3.0/rustscan_2.3.0_amd64.deb
sudo dpkg -i rustscan_2.3.0_amd64.deb

# Arch Linux (AUR)
yay -S rustscan

# Cargo (Rust Package Manager)
cargo install rustscan

# Docker Container
docker run -it --rm --name rustscan rustscan/rustscan:2.1.1 -a 10.10.10.50
```
---

## 🚀 Grundlegende Nutzung und Scannen einzelner Hosts
```bash
# Scan all 65,535 ports on a single IP
rustscan -a 10.10.10.50

# Scan with greppable one-line output
rustscan -a 10.10.10.50 --greppable

# Quiet mode (suppresses the ASCII banner)
rustscan -q -a 10.10.10.50
```
---

## 🔗 Automatisierte Nmap-Integration

Die leistungsstärkste Funktion von RustScan besteht darin, offene Ports mithilfe des nachgestellten `--`-Trennzeichens direkt an Nmap weiterzuleiten:
```bash
# Scan target and execute default safe scripts & version detection on open ports
rustscan -a 10.10.10.50 -- -sC -sV

# Pass custom Nmap NSE scripts and output options
rustscan -a 10.10.10.50 -- -sC -sV -oN nmap_report.txt

# Run vulnerability audit with Nmap Vuln scripts
rustscan -a 10.10.10.50 -- --script vuln -oA target_vulns
```
---

## ⚡ Leistungsoptimierung (`ulimit` & Batchgröße)

RustScan basiert auf den Dateideskriptorbeschränkungen Ihres Betriebssystems. Standardmäßig erfolgt die automatische Anpassung, aber die manuelle Abstimmung sorgt für Spitzenleistung:
```bash
# Increase batch size and socket ulimit for high-speed local networks
rustscan -a 10.10.10.50 -b 5000 -u 5000

# Lower batch size for high-latency or packet-dropping WiFi networks
rustscan -a 10.10.10.50 -b 500 --scan-order "random"
```
---

## 🌐 Portbereiche und CIDR-Subnetze scannen
```bash
# Scan a specific port range instead of all ports
rustscan -a 10.10.10.50 -r 1-1000

# Scan multiple IP addresses
rustscan -a 10.10.10.50,10.10.10.51

# Scan an entire CIDR subnet range
rustscan -a 192.168.1.0/24
```
---

## 📊 Skripterstellung und Ausgabeformatierung
```bash
# Output discovered open ports in JSON format
rustscan -a 10.10.10.50 --accessible

# Read target hosts from a file
rustscan -a 'hosts.txt'
```
---

## ⚡ Kurzreferenztabelle

| Ziel | Befehl |
|-----------|---------|
| **Alle 65.000 Ports scannen** | `rustscan -a <IP>` |
| **Alle Ports scannen + Nmap-Skripte**| `rustscan -a <IP> -- -sC -sV` |
| **CIDR-Subnetz scannen** | `rustscan -a <SUBNET>/24` |
| **Portbereich scannen** | `rustscan -a <IP> -r 1-1000` |
| **Stapelgröße und ulimit anpassen** | `rustscan -a <IP> -b 3000 -u 3000` |
| **Ruhemodus (kein Banner)** | `rustscan -q -a <IP>` |
| **Greifbare Ausgabe** | `rustscan -a <IP> --greppable` |
| **In Nmap-Datei exportieren** | `rustscan -a <IP> -- -sC -sV -oN nmap.txt` |

---

## 🛡️ Defensive Überlegungen und OPSEC

1. **Massives Rauschen und IDS-Warnungen:** Durch das Scannen von 65.000 Ports in 3 Sekunden werden sofort Tausende von TCP-SYN-Paketen generiert. Snort, Suricata und die Geschwindigkeitsbegrenzung durch die Firewall werden sofort ausgelöst.
2. **SYN-Flooding und verworfene Pakete:** Bei instabilen VPN-Verbindungen (z. B. HackTheBox, TryHackMe) können hohe Batch-Größen (`-b 4000`) zu Paketverlusten führen. Wenn die Ergebnisse inkonsistent erscheinen, verringern Sie die Batchgröße auf `-b 1000`.

---

## ⚠️ Rechtlicher Haftungsausschluss

> [!ACHTUNG]
> Dieses Spickzettel ist nur für autorisierte Penetrationstests und Bildungsforschung bestimmt. Das unerlaubte Scannen von Netzwerken ist strengstens untersagt.