<!-- language-home-navigation -->
[🏠 Home Page Italiana](../../README.it.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ⚡ Naabu - Scheda completa

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
<palign="centro">
  <img src="https://img.shields.io/badge/Naabu-Port_Scanner-blue?style=for-the-badge" alt="Naabu">
  <img src="https://img.shields.io/badge/Author-ProjectDiscovery-orange?style=for-the-badge" alt="ProjectDiscovery">
  <img src="https://img.shields.io/badge/Category-Recon_&_Scanning-green?style=for-the-badge" alt="Ricognizione e scansione">
  <img src="https://img.shields.io/badge/Language-Go-cyan?style=for-the-badge" alt="Vai">
</p>

<palign="centro">
  <b>⚡ Scanner di porte veloce e affidabile incentrato sulla semplicità e sulle tubazioni senza interruzioni con gli strumenti ProjectDiscovery (httpx, nuclei)</b>
</p>

---

## 📋 Sommario

- [Panoramica e funzionalità principali](#-panoramica--funzionalità principali)
- [Installazione](#-installazione)
- [Scansione di base delle porte](#-basic-port-scanning)
  - [Scansione host singolo](#scansione-host-singolo)
  - [Porte principali e intervallo completo di porte](#porte-superiori-intervallo-porte-intero)
- [Modalità di scansione: SYN (Raw Socket) vs CONNECT](#-scan-modes-syn-raw-socket-vs-connect)
- [Limitazione della velocità e ottimizzazione delle prestazioni](#-limitazione della velocità--ottimizzazione delle prestazioni)
- [Scansione multi-target e blocchi CIDR](#-multi-target-scanning--cidr-blocks)
- [Flussi di lavoro pipeline senza soluzione di continuità](#-seamless-pipeline-workflows)
  - [Naabu + Httpx + Nuclei](#naabu--httpx--nuclei)
  - [Subfinder + Naabu](#subfinder--naabu)
- [Integrazione Nmap (`-nmap`)](#-nmap-integration--nmap)
- [Tabella di riferimento rapido](#-tabella-di-riferimento-rapido)
- [Considerazioni difensive e rilevamento](#-considerazioni-difensive--rilevamento)
- [Disclaimer legale](#-legal-disclaimer)

---

## 🎯 Panoramica e caratteristiche principali

Sviluppato da **ProjectDiscovery**, **Naabu** è uno scanner di porte progettato per essere veloce, accurato e ottimizzato per l'integrazione nelle moderne pipeline di automazione della ricognizione:
- **Scansioni SYN e CONNECT:** fallback automatico su TCP CONNECT quando i privilegi del socket raw (`libpcap` / root) sono assenti.
- **Ecosistema ProjectDiscovery:** Architettura nativa della pipeline che consente tubazioni senza soluzione di continuità da `subfinder` a `naabu`, quindi a `httpx` e `nuclei`.
- **Motore Nmap integrato:** Capacità di attivare automaticamente il servizio Nmap attivo e la scansione delle versioni sulle porte aperte rilevate.
- **Esclusione CDN:** bypassa automaticamente i nodi edge CDN cloud (Cloudflare, Akamai, CloudFront) per evitare sprechi di larghezza di banda e falsi positivi.

---

## 📥Installazione
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
> [!NOTA]
> Su Linux, concedere `CAP_NET_RAW` consente di eseguire scansioni SYN senza root:
> `sudo setcap cap_net_raw=+ep $(which naabu)`

---

## 🚀 Scansione di base delle porte

### Scansione host singolo
```bash
# Scan default top 100 ports
naabu -host example.com

# Scan specific ports
naabu -host example.com -p 80,443,8080,8443
```
### Porte principali e gamma completa di porte
```bash
# Scan top 1000 ports
naabu -host example.com -top-ports 1000

# Full port scan (1-65535)
naabu -host example.com -p -
```
---

## ⚡ Modalità di scansione: SYN (Raw Socket) vs CONNECT

- **Scansione SYN (`-s syn` / root richiesto):** Invia pacchetti TCP SYN grezzi senza completare l'handshake a 3 vie (scansione semiaperta). Molto più veloce e meno rumoroso nei log delle applicazioni.
- **CONNECT Scan (`-s conn`):** Utilizza la chiamata socket `connect()` del sistema operativo standard. Funziona senza privilegi di root e penetra in ambienti virtuali restrittivi.
```bash
# Explicit SYN Scan (Requires sudo or CAP_NET_RAW)
sudo naabu -host example.com -s syn

# Explicit TCP Connect Scan (No root required)
naabu -host example.com -s conn
```
---

## ⚙️ Limitazione della velocità e ottimizzazione delle prestazioni
```bash
# Adjust packet rate per second (default: 1000)
sudo naabu -host example.com -rate 2500

# Set connection timeout in milliseconds
naabu -host example.com -timeout 1000

# Exclude CDN edge servers (Cloudflare, CloudFront, Fastly)
naabu -host example.com -exclude-cdn
```
---

## 🌐 Scansione multi-target e blocchi CIDR
```bash
# Scan targets from list
naabu -list targets.txt -p 80,443,8080 -o open_ports.txt

# Scan a /24 subnet block
sudo naabu -host 192.168.1.0/24 -p 22,80,443,445,3389
```
---

## 🔄 Flussi di lavoro della pipeline senza soluzione di continuità

Naabu brilla se collegato ad altri strumenti ProjectDiscovery:

### Subfinder + Naabu
Enumera i sottodomini e scopri subito quali hanno porte web/SSH aperte:
```bash
subfinder -d example.com -silent | naabu -silent -p 80,443,8080,8443
```
### Naabu + Httpx + Nuclei
La pipeline canonica di ricognizione offensiva del bug bounty:
```bash
# 1. Port scan subdomains -> 2. Probe HTTP services -> 3. Vulnerability scan
subfinder -d target.com -silent \
  | naabu -silent -p 80,443,8080,8443 \
  | httpx -silent \
  | nuclei -severity medium,high,critical -o findings.txt
```
---

## 🔍 Integrazione Nmap (`-nmap`)

Passa le porte aperte scoperte direttamente a Nmap per il rilevamento del servizio (`-sV`) e la scansione degli script (`-sC`):
```bash
sudo naabu -host target.local -top-ports 1000 -nmap-cli 'nmap -sV -sC -oN naabu_nmap.txt'
```
---

## ⚡ Tabella di consultazione rapida

| Obiettivo | Comando |
|-----------|---------|
| **Scansione predefinita (Top 100)** | `naabu -host <TARGET>` |
| **Scansione a gamma completa (1-65535)**| `naabu -host <TARGET> -p -` |
| **Primi 1000 porti** | `naabu -host <TARGET> -top-ports 1000` |
| **SYN Scan (veloce, invisibile)**| `sudo naabu -host <TARGET> -s syn` |
| **Escludi IP CDN Edge** | `naabu -host <TARGET> -exclude-cdn` |
| **Output in JSON** | `naabu -host <TARGET> -json -o results.json` |
| **Invia pipe a HTTPX** | `naabu -host <TARGET> -silent \| httpx` |
| **Attiva la scansione del servizio Nmap**| `sudo naabu -host <TARGET> -nmap-cli 'nmap -sV'` |

---

## 🛡️ Considerazioni difensive e rilevamento

1. **Regole Firewall e IDS:** Gli sweep SYN ad alta velocità generati da Naabu producono rapporti TCP RST/SYN elevati facilmente rilevabili dalle regole Snort/Suricata.
2. **Limitazione della velocità di rete:** applica la limitazione della velocità di connessione in entrata (`iptables -m limit --limit 50/s`) per rallentare e interrompere la scansione automatizzata.

---

## ⚠️ Disclaimer legale

> [!ATTENZIONE]
> Naabu è sviluppato esclusivamente per valutazioni di sicurezza autorizzate, test di penetrazione e rilevamento di risorse difensive. La scansione delle reti senza autorizzazione esplicita è severamente vietata.