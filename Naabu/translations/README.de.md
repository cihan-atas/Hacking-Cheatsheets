<!-- language-home-navigation -->
[🏠 Deutsche Startseite](../../README.de.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ⚡ Naabu – Vollständiger Cheatsheet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ███╗   ██╗ █████╗  █████╗ ██████╗ ██╗   ██╗
  ████╗  ██║██╔══██╗██╔══██╗██╔══██╗██║   ██║
  ██╔██╗ ██║███████║███████║██████╔╝██║   ██║
  ██║╚██╗██║██╔══██║██╔══██║██╔══██╗██║   ██║
  ██║ ╚████║██║  ██║██║  ██║██████╔╝╚██████╔╝
  ╚═╝  ╚═══╝╚═╝  ╚═╝╚═╝  ╚═╝╚═════╝  ╚═════╝ 
        Fast & Reliable SYN/CONNECT Port Scanner
```
<p align="center">
  <img src="https://img.shields.io/badge/Naabu-Port_Scanner-blue?style=for-the-badge" alt="Naabu">
  <img src="https://img.shields.io/badge/Author-ProjectDiscovery-orange?style=for-the-badge" alt="ProjectDiscovery">
  <img src="https://img.shields.io/badge/Category-Recon_&_Scanning-green?style=for-the-badge" alt="Recon & Scanning">
  <img src="https://img.shields.io/badge/Language-Go-cyan?style=for-the-badge" alt="Go">
</p>

<p align="center">
  <b>⚡ Schneller und zuverlässiger Port-Scanner mit Schwerpunkt auf Einfachheit und nahtloser Verrohrung mit ProjectDiscovery-Tools (httpx, nuclei)</b>
</p>

---

## 📋 Inhaltsverzeichnis

- [Übersicht und Hauptfunktionen](#-overview--key-features)
- [Installation](#-Installation)
- [Grundlegendes Port-Scanning](#-basic-port-scanning)
  - [Einzelner Host-Scan](#single-host-scan)
  - [Top-Ports und vollständiger Portbereich](#top-ports--full-port-range)
- [Scan-Modi: SYN (Raw Socket) vs. CONNECT](#-scan-modes-syn-raw-socket-vs-connect)
- [Ratenbegrenzung und Leistungsoptimierung](#-rate-limiting--performance-tuning)
- [Multi-Target-Scanning und CIDR-Blöcke](#-multi-target-scanning--cidr-blocks)
- [Nahtlose Pipeline-Workflows](#-seamless-pipeline-workflows)
  - [Naabu + Httpx + Nuclei](#naabu--httpx--nuclei)
  - [Subfinder + Naabu](#subfinder--naabu)
- [Nmap-Integration (`-nmap`)](#-nmap-integration--nmap)
- [Kurzreferenztabelle](#-quick-reference-table)
- [Verteidigungsüberlegungen und Erkennung](#-defensive-überlegungen--erkennung)
- [Rechtlicher Haftungsausschluss](#-legal-disclaimer)

---

## 🎯 Übersicht und Hauptfunktionen

**Naabu** wurde von **ProjectDiscovery** entwickelt und ist ein Port-Scanner, der schnell, genau und für die Integration in moderne Aufklärungsautomatisierungspipelines optimiert ist:
- **SYN- und CONNECT-Scans:** Automatischer Fallback auf TCP CONNECT, wenn Raw-Socket-Berechtigungen (`libpcap` / Root) fehlen.
- **ProjectDiscovery-Ökosystem:** Native Pipeline-Architektur, die eine nahtlose Weiterleitung von `subfinder` zu `naabu` und dann zu `httpx` und `nuclei` ermöglicht.
- **Integrierte Nmap-Engine:** Möglichkeit, aktive Nmap-Dienste und Versionsscans auf erkannten offenen Ports automatisch auszulösen.
- **CDN-Ausschluss:** Umgeht automatisch Cloud-CDN-Edge-Knoten (Cloudflare, Akamai, CloudFront), um Bandbreitenverschwendung und Fehlalarme zu verhindern.

---

## 📥 Installation
```bash
# Using Go (Recommended)
go install -v github.com/projectdiscovery/naabu/v2/cmd/naabu@latest

# Binary download via pdtm (ProjectDiscovery Tool Manager)
pdtm -i naabu

# Arch Linux (AUR)
yay -S naabu-bin

# Kali Linux
sudo apt update && sudo apt install -y naabu
```
> [!NOTE]
> Unter Linux ermöglicht die Gewährung von `CAP_NET_RAW` die Ausführung von SYN-Scans ohne Root:
> `sudo setcap cap_net_raw=+ep $(which naabu)`

---

## 🚀 Grundlegendes Port-Scannen

### Einzelner Host-Scan
```bash
# Scan default top 100 ports
naabu -host example.com

# Scan specific ports
naabu -host example.com -p 80,443,8080,8443
```
### Top-Ports und vollständiges Port-Sortiment
```bash
# Scan top 1000 ports
naabu -host example.com -top-ports 1000

# Full port scan (1-65535)
naabu -host example.com -p -
```
---

## ⚡ Scan-Modi: SYN (Raw Socket) vs. CONNECT

- **SYN-Scan (`-s syn` / Root erforderlich):** Sendet rohe TCP-SYN-Pakete, ohne den 3-Wege-Handshake abzuschließen (halboffener Scan). Viel schneller und weniger laut in Anwendungsprotokollen.
- **CONNECT Scan (`-s conn`):** Verwendet den Standard-OS-Socket-Aufruf `connect()`. Arbeitet ohne Root-Rechte und dringt in restriktive virtuelle Umgebungen ein.
```bash
# Explicit SYN Scan (Requires sudo or CAP_NET_RAW)
sudo naabu -host example.com -s syn

# Explicit TCP Connect Scan (No root required)
naabu -host example.com -s conn
```
---

## ⚙️ Ratenbegrenzung und Leistungsoptimierung
```bash
# Adjust packet rate per second (default: 1000)
sudo naabu -host example.com -rate 2500

# Set connection timeout in milliseconds
naabu -host example.com -timeout 1000

# Exclude CDN edge servers (Cloudflare, CloudFront, Fastly)
naabu -host example.com -exclude-cdn
```
---

## 🌐 Multi-Target-Scanning und CIDR-Blöcke
```bash
# Scan targets from list
naabu -list targets.txt -p 80,443,8080 -o open_ports.txt

# Scan a /24 subnet block
sudo naabu -host 192.168.1.0/24 -p 22,80,443,445,3389
```
---

## 🔄 Nahtlose Pipeline-Workflows

Naabu glänzt, wenn es mit anderen ProjectDiscovery-Tools kombiniert wird:

### Subfinder + Naabu
Zählen Sie Subdomains auf und entdecken Sie sofort, welche über offene Web-/SSH-Ports verfügen:
```bash
subfinder -d example.com -silent | naabu -silent -p 80,443,8080,8443
```
### Naabu + Httpx + Kerne
Die kanonische offensive Bug-Bounty-Aufklärungspipeline:
```bash
# 1. Port scan subdomains -> 2. Probe HTTP services -> 3. Vulnerability scan
subfinder -d target.com -silent \
  | naabu -silent -p 80,443,8080,8443 \
  | httpx -silent \
  | nuclei -severity medium,high,critical -o findings.txt
```
---

## 🔍 Nmap-Integration (`-nmap`)

Übergeben Sie erkannte offene Ports direkt an Nmap zur Diensterkennung (`-sV`) und zum Skript-Scannen (`-sC`):
```bash
sudo naabu -host target.local -top-ports 1000 -nmap-cli 'nmap -sV -sC -oN naabu_nmap.txt'
```
---

## ⚡ Kurzreferenztabelle

| Ziel | Befehl |
|-----------|---------|
| **Standardscan (Top 100)** | `naabu -host <TARGET>` |
| **Vollbereichsscan (1-65535)**| `naabu -host <TARGET> -p -` |
| **Top 1000 Häfen** | `naabu -host <TARGET> -top-ports 1000` |
| **SYN-Scan (schnell, heimlich)**| `sudo naabu -host <TARGET> -s syn` |
| **CDN-Edge-IPs ausschließen** | `naabu -host <TARGET> -exclude-cdn` |
| **Ausgabe an JSON** | `naabu -host <TARGET> -json -o results.json` |
| **Pipe to HTTPX** | `naabu -host <TARGET> -silent \| httpx` |
| **Nmap-Dienstscan auslösen**| `sudo naabu -host <TARGET> -nmap-cli 'nmap -sV'` |

---

## 🛡️ Defensive Überlegungen und Erkennung

1. **Firewall- und IDS-Regeln:** Von Naabu generierte SYN-Sweeps mit hoher Rate erzeugen hohe TCP-RST/SYN-Verhältnisse, die leicht von den Snort/Suricata-Regeln erfasst werden können.
2. **Begrenzung der Netzwerkrate:** Erzwingen Sie die Begrenzung der eingehenden Verbindungsrate (`iptables -m limit --limit 50/s`), um das automatische Scannen zu verlangsamen und zu unterbrechen.

---

## ⚠️ Rechtlicher Haftungsausschluss

> [!ACHTUNG]
> Naabu wurde ausschließlich für autorisierte Sicherheitsbewertungen, Penetrationstests und die Erkennung defensiver Vermögenswerte entwickelt. Das Scannen von Netzwerken ohne ausdrückliche Genehmigung ist strengstens untersagt.