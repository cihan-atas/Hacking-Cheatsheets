<!-- language-home-navigation -->
[🏠 Norsk Hovedside](../../README.nb.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇬🇧 English](../README.md)

---


# 🦀 RustScan - Komplett jukseark

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
  <img src="https://img.shields.io/badge/Language-Rust-orange?style=for-the-badge" alt="Språkrust">
  <img src="https://img.shields.io/badge/Category-Recon_&_Scanning-green?style=for-the-badge" alt="Recon & Scanning">
  <img src="https://img.shields.io/badge/License-GPLv3-red?style=for-the-badge" alt="License GPLv3">
</p>

<p align="center">
  <b>🌐 Rask, tilpassbar og automatisert portskanner designet for å overføre oppdagede åpne porter direkte inn i Nmap-skript</b>
</p>

---

## 📋 Innholdsfortegnelse

- [Oversikt og arkitektur](#-oversikt--arkitektur)
- [Verktøysammenligning: RustScan vs Nmap vs Masscan](#-tool-comparison-rustscan-vs-nmap-vs-masscan)
- [Installasjon](#-installasjon)
– [Grunnleggende bruk og skanning med enkelt vert](#-grunnleggende bruk--single-vert-skanning)
- [Automatisert Nmap-integrasjon](#-automated-nmap-integration)
- [Performance Tuning (`ulimit` & Batch Size)](#-performance-tuning-ulimit--batch-size)
- [Skanningsportområder og CIDR-undernett](#-skanneportområder--cidr-undernett)
- [Skript og utdataformatering](#-skripting--utdataformatering)
- [Hurtigreferansetabell](#-hurtigreferansetabell)
– [Defensive Considerations & OPSEC](#-defensive-considerations--opsec)
– [Juridisk ansvarsfraskrivelse](#-juridisk ansvarsfraskrivelse)

---

## 🎯 Oversikt og arkitektur

**RustScan** er en moderne, høyhastighets portskanner konstruert i **Rust**. Kjernefilosofien er enkel: **skann porter lynraskt, finn åpne porter på tvers av hele 1-65535-spekteret på 3 sekunder, og overlever umiddelbart disse åpne portene til Nmap for omfattende service- og sårbarhetsskanning**.

I stedet for å vente 10–20 minutter på at `nmap -p-` skal fullføre sine sekvensielle TCP-håndtrykk, utnytter RustScan asynkron multi-threading og OS-socket-batching for å finne åpne porter nesten umiddelbart.

---

## ⚖️ Verktøysammenligning: RustScan vs Nmap vs Masscan

| Funksjon | RustScan | Nmap | Masscan |
|--------|--------|------|--------|
| **Primært mål** | Rask oppdagelse -> Nmap pipe | Deep service & NSE-skript | Høyhastighets SYN-skanning for hele Internett |
| **All-ports-hastighet (1-65535)**| ⭐⭐⭐⭐⭐ (~3 sekunder) | ⭐⭐ (5–20 minutter) | ⭐⭐⭐⭐⭐ (~2 sekunder) |
| **Deteksjon av tjenesteversjon**| Via Nmap pipe | Innfødt (`-sV`) | ❌ Minimum |
| **NSE-skriptmotor** | Via Nmap pipe | Innfødt (`-sC`, `--script`) | ❌ Nei |
| **Pakkemotor** | Asynkrone TCP-kontakter | Rå IP / egendefinert TCP-stabel | Egendefinert asynkron SYN-stabel |
| **Enkel automatisering** | Innfødte CLI-argumenter (`--`) | Omfattende CLI-alternativer | Høy kompleksitet |

---

## 📥 Installasjon
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

## 🚀 Grunnleggende bruk og enkelt vertsskanning
```bash
# Scan all 65,535 ports on a single IP
rustscan -a 10.10.10.50

# Scan with greppable one-line output
rustscan -a 10.10.10.50 --greppable

# Quiet mode (suppresses the ASCII banner)
rustscan -q -a 10.10.10.50
```
---

## 🔗 Automatisert Nmap-integrasjon

RustScans kraftigste funksjon er å sende åpne porter rett til Nmap ved å bruke den etterfølgende `--`-separatoren:
```bash
# Scan target and execute default safe scripts & version detection on open ports
rustscan -a 10.10.10.50 -- -sC -sV

# Pass custom Nmap NSE scripts and output options
rustscan -a 10.10.10.50 -- -sC -sV -oN nmap_report.txt

# Run vulnerability audit with Nmap Vuln scripts
rustscan -a 10.10.10.50 -- --script vuln -oA target_vulns
```
---

## ⚡ Ytelsesjustering (`ulimit` og batchstørrelse)

RustScan er avhengig av operativsystemets fildeskriptorgrenser. Som standard justeres den automatisk, men manuell innstilling gir topp ytelse:
```bash
# Increase batch size and socket ulimit for high-speed local networks
rustscan -a 10.10.10.50 -b 5000 -u 5000

# Lower batch size for high-latency or packet-dropping WiFi networks
rustscan -a 10.10.10.50 -b 500 --scan-order "random"
```
---

## 🌐 Skanne portområder og CIDR-undernett
```bash
# Scan a specific port range instead of all ports
rustscan -a 10.10.10.50 -r 1-1000

# Scan multiple IP addresses
rustscan -a 10.10.10.50,10.10.10.51

# Scan an entire CIDR subnet range
rustscan -a 192.168.1.0/24
```
---

## 📊 Skripting og utdataformatering
```bash
# Output discovered open ports in JSON format
rustscan -a 10.10.10.50 --accessible

# Read target hosts from a file
rustscan -a 'hosts.txt'
```
---

## ⚡ Hurtigreferansetabell

| Mål | Kommando |
|----------------|--------|
| **Skann alle 65k-porter** | `rustscan -a <IP>` |
| **Skann alle porter + Nmap-skript**| `rustscan -a <IP> -- -sC -sV` |
| **Skann CIDR-undernett** | `rustscan -a <SUBNET>/24` |
| **Scan Port Range** | `rustscan -a <IP> -r 1-1000` |
| **Tune batchstørrelse og ugrense** | `rustscan -a <IP> -b 3000 -u 3000` |
| **Stille modus (ingen banner)** | `rustscan -q -a <IP>` |
| **Greppable Output** | `rustscan -a <IP> --greppable` |
| **Eksporter til Nmap-fil** | `rustscan -a <IP> -- -sC -sV -oN nmap.txt` |

---

## 🛡️ Defensive betraktninger og OPSEC

1. **Massive støy- og IDS-varsler:** Skanning av 65k porter på 3 sekunder genererer tusenvis av TCP SYN-pakker umiddelbart. Det vil utløse hastighetsbegrensning av Snort, Suricata og brannmur umiddelbart.
2. **SYN Flooding & Dropped Packets:** På ustabile VPN-koblinger (f.eks. HackTheBox, TryHackMe), kan høye batchstørrelser (`-b 4000`) forårsake pakketap. Hvis resultatene virker inkonsekvente, senk batchstørrelsen til `-b 1000`.

---

## ⚠️ Juridisk ansvarsfraskrivelse

> [!FORSIKTIG]
> Dette juksearket er kun for autorisert penetrasjonstesting og pedagogisk forskning. Det er strengt forbudt å skanne nettverk uten autorisasjon.