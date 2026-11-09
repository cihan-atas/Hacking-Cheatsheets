<!-- language-home-navigation -->
[🏠 Norsk Hovedside](../../README.nb.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇬🇧 English](../README.md)

---


# ⚡ Naabu - Komplett jukseark

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
  <b>⚡ Rask og pålitelig portskanner fokusert på enkelhet og sømløs rørføring med ProjectDiscovery-verktøy (httpx, nuclei)</b>
</p>

---

## 📋 Innholdsfortegnelse

- [Oversikt og nøkkelfunksjoner](#-oversikt--nøkkelfunksjoner)
- [Installasjon](#-installasjon)
- [Grunnleggende portskanning](#-grunnleggende portskanning)
  - [Single Host Scan](#single-host-scan)
  - [Topporter og fullt portområde](#topports--full-port-range)
- [Skannemoduser: SYN (Raw Socket) vs CONNECT](#-scan-modes-syn-raw-socket-vs-connect)
- [Rate Limiting & Performance Tuning](#-rate-limiting--performance-tuning)
- [Multi-Target Scanning & CIDR Blocks](#-multi-target-scanning--cidr-blocks)
- [Sømløse arbeidsflyter for rørledning](#-sømløse arbeidsflyter)
  - [Naabu + Httpx + Nuclei](#naabu--httpx--nuclei)
  - [Subfinder + Naabu](#subfinder--naabu)
- [Nmap-integrasjon (`-nmap`)](#-nmap-integration--nmap)
- [Hurtigreferansetabell](#-hurtigreferansetabell)
– [Defensive Considerations & Detection](#-defensive-considerations--detection)
– [Juridisk ansvarsfraskrivelse](#-juridisk ansvarsfraskrivelse)

---

## 🎯 Oversikt og nøkkelfunksjoner

Utviklet av **ProjectDiscovery**, **Naabu** er en portskanner designet for å være rask, nøyaktig og optimalisert for integrering i moderne rekognoseringsautomatiseringsrørledninger:
- **SYN & CONNECT-skanninger:** Automatisk fallback til TCP CONNECT når rå socket-privilegier (`libpcap` / root) er fraværende.
- **ProjectDiscovery Ecosystem:** Innebygd rørledningsarkitektur som tillater sømløs rørføring fra `subfinder` til `naabu`, deretter inn i `httpx` og `nuclei`.
- **Integrert Nmap Engine:** Mulighet til å utløse aktiv Nmap-tjeneste og versjonsskanning på oppdagede åpne porter automatisk.
- **CDN-ekskludering:** Omgår automatisk sky-CDN-kantnoder (Cloudflare, Akamai, CloudFront) for å forhindre bortkastet båndbredde og falske positiver.

---

## 📥 Installasjon
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
> [!MERK]
> På Linux, tillater `CAP_NET_RAW` å kjøre SYN-skanninger uten rot:
> `sudo setcap cap_net_raw=+ep $(which naabu)`

---

## 🚀 Grunnleggende portskanning

### Single Host Scan
```bash
# Scan default top 100 ports
naabu -host example.com

# Scan specific ports
naabu -host example.com -p 80,443,8080,8443
```
### Toppporter og fullt portområde
```bash
# Scan top 1000 ports
naabu -host example.com -top-ports 1000

# Full port scan (1-65535)
naabu -host example.com -p -
```
---

## ⚡ Skannemoduser: SYN (Raw Socket) vs CONNECT

- **SYN-skanning (`-s syn` / rot kreves):** Sender rå TCP SYN-pakker uten å fullføre 3-veis håndtrykk (halvåpen skanning). Mye raskere og mindre støyende i applikasjonslogger.
- **CONNECT Scan (`-s conn`):** Bruker standard OS `connect()` socket call. Fungerer uten root-privilegier og trenger gjennom restriktive virtuelle miljøer.
```bash
# Explicit SYN Scan (Requires sudo or CAP_NET_RAW)
sudo naabu -host example.com -s syn

# Explicit TCP Connect Scan (No root required)
naabu -host example.com -s conn
```
---

## ⚙️ Hastighetsbegrensning og ytelsesjustering
```bash
# Adjust packet rate per second (default: 1000)
sudo naabu -host example.com -rate 2500

# Set connection timeout in milliseconds
naabu -host example.com -timeout 1000

# Exclude CDN edge servers (Cloudflare, CloudFront, Fastly)
naabu -host example.com -exclude-cdn
```
---

## 🌐 Multi-Target Scanning og CIDR-blokker
```bash
# Scan targets from list
naabu -list targets.txt -p 80,443,8080 -o open_ports.txt

# Scan a /24 subnet block
sudo naabu -host 192.168.1.0/24 -p 22,80,443,445,3389
```
---

## 🔄 Sømløse arbeidsflyter for rørledninger

Naabu skinner når den kobles til andre ProjectDiscovery-verktøy:

### Subfinder + Naabu
Tell opp underdomener og oppdag umiddelbart hvilke som har åpne nett-/SSH-porter:
```bash
subfinder -d example.com -silent | naabu -silent -p 80,443,8080,8443
```
### Naabu + Httpx + Nuclei
Den kanoniske offensive bug bounty rekognoseringsrørledningen:
```bash
# 1. Port scan subdomains -> 2. Probe HTTP services -> 3. Vulnerability scan
subfinder -d target.com -silent \
  | naabu -silent -p 80,443,8080,8443 \
  | httpx -silent \
  | nuclei -severity medium,high,critical -o findings.txt
```
---

## 🔍 Nmap-integrasjon (`-nmap`)

Send oppdagede åpne porter direkte til Nmap for tjenestedeteksjon (`-sV`) og skriptskanning (`-sC`):
```bash
sudo naabu -host target.local -top-ports 1000 -nmap-cli 'nmap -sV -sC -oN naabu_nmap.txt'
```
---

## ⚡ Hurtigreferansetabell

| Mål | Kommando |
|----------------|--------|
| **Standardskanning (Topp 100)** | `naabu -host <TARGET>` |
| **Full rekkeviddeskanning (1-65535)**| `naabu -host <TARGET> -p -` |
| **Topp 1000 porter** | `naabu -host <TARGET> -top-ports 1000` |
| **SYN-skanning (rask, snikende)**| `sudo naabu -host <TARGET> -s syn` |
| **Ekskluder CDN Edge IP-er** | `naabu -host <TARGET> -exclude-cdn` |
| **Utgang til JSON** | `naabu -host <TARGET> -json -o results.json` |
| **Rør til HTTPX** | `naabu -host <TARGET> -silent \| httpx` |
| **Trigger Nmap Service Scan**| `sudo naabu -host <TARGET> -nmap-cli 'nmap -sV'` |

---

## 🛡️ Defensive vurderinger og gjenkjenning

1. **Brannmur- og IDS-regler:** Høyhastighets SYN-sveip generert av Naabu produserer høye TCP RST/SYN-forhold som lett fanges opp av Snort/Suricata-regler.
2. **Nettverkshastighetsbegrensning:** Håndhev innkommende tilkoblingshastighetsbegrensning (`iptables -m limit --limit 50/s`) for å redusere hastigheten og forstyrre automatisert skanning.

---

## ⚠️ Juridisk ansvarsfraskrivelse

> [!FORSIKTIG]
> Naabu er utviklet kun for autoriserte sikkerhetsvurderinger, penetrasjonstesting og defensiv aktivafunn. Det er strengt forbudt å skanne nettverk uten eksplisitt tillatelse.