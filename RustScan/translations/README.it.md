<!-- language-home-navigation -->
[🏠 Home Page Italiana](../../README.it.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🦀 RustScan - Cheatsheet completo

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
<palign="centro">
  <img src="https://img.shields.io/badge/RustScan-Port_Scanner-blue?style=for-the-badge" alt="RustScan">
  <img src="https://img.shields.io/badge/Language-Rust-orange?style=for-the-badge" alt="Language Rust">
  <img src="https://img.shields.io/badge/Category-Recon_&_Scanning-green?style=for-the-badge" alt="Ricognizione e scansione">
  <img src="https://img.shields.io/badge/License-GPLv3-red?style=for-the-badge" alt="Licenza GPLv3">
</p>

<palign="centro">
  <b>🌐 Scanner di porte veloce, personalizzabile e automatizzato progettato per convogliare le porte aperte scoperte direttamente negli script Nmap</b>
</p>

---

## 📋 Sommario

- [Panoramica e architettura](#-panoramica--architettura)
- [Confronto strumenti: RustScan vs Nmap vs Masscan](#-tool-comparison-rustscan-vs-nmap-vs-masscan)
- [Installazione](#-installazione)
- [Utilizzo di base e scansione a host singolo](#-basic-usage--scansione a host singolo)
- [Integrazione automatizzata di Nmap](#-automated-nmap-integration)
- [Ottimizzazione delle prestazioni (`ulimit` e dimensione batch)](#-performance-tuning-ulimit--batch-size)
- [Intervalli di porte di scansione e sottoreti CIDR](#-scanning-port-ranges--cidr-subnets)
- [Scripting e formattazione dell'output](#-scripting--output-formatting)
- [Tabella di riferimento rapido](#-tabella-di-riferimento-rapido)
- [Considerazioni difensive e OPSEC](#-considerazioni-difensive--opsec)
- [Disclaimer legale](#-legal-disclaimer)

---

## 🎯 Panoramica e architettura

**RustScan** è uno scanner di porte moderno e ad alta velocità progettato in **Rust**. La sua filosofia di base è semplice: **scansiona le porte in modo incredibilmente veloce, trova le porte aperte nell'intero spettro 1-65535 in 3 secondi e trasferisci immediatamente quelle porte aperte a Nmap per un servizio completo e la scansione delle vulnerabilità**.

Invece di attendere 10-20 minuti affinché `nmap -p-` completi le operazioni sequenziali di handshake TCP, RustScan sfrutta il multi-threading asincrono e il batching dei socket del sistema operativo per individuare le porte aperte quasi istantaneamente.

---

## ⚖️ Confronto strumenti: RustScan vs Nmap vs Masscan

| Caratteristica | RustScan | Nmap | Massano |
|---------|----------|------|---------|
| **Obiettivo primario** | Scoperta veloce -> Pipe Nmap | Servizio profondo e script NSE | Scansione SYN ad alta velocità su tutta la rete Internet |
| **Velocità di tutte le porte (1-65535)**| ⭐⭐⭐⭐⭐ (~3 secondi) | ⭐⭐ (5–20 minuti) | ⭐⭐⭐⭐⭐ (~2 secondi) |
| **Rilevamento della versione del servizio**| Tramite pipe Nmap | Nativo (`-sV`) | ❌ Minimo |
| **Motore di script NSE** | Tramite pipe Nmap | Nativo (`-sC`, `--script`) | ❌No |
| **Motore a pacchetti** | Socket TCP asincroni | IP grezzo/stack TCP personalizzato | Stack SYN asincrono personalizzato |
| **Facilità di automazione** | Argomenti CLI nativi (`--`) | Ampie opzioni CLI | Elevata complessità |

---

## 📥Installazione
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

## 🚀 Utilizzo di base e scansione di un singolo host
```bash
# Scan all 65,535 ports on a single IP
rustscan -a 10.10.10.50

# Scan with greppable one-line output
rustscan -a 10.10.10.50 --greppable

# Quiet mode (suppresses the ASCII banner)
rustscan -q -a 10.10.10.50
```
---

## 🔗 Integrazione Nmap automatizzata

La funzionalità più potente di RustScan è il passaggio delle porte aperte direttamente a Nmap utilizzando il separatore `--` finale:
```bash
# Scan target and execute default safe scripts & version detection on open ports
rustscan -a 10.10.10.50 -- -sC -sV

# Pass custom Nmap NSE scripts and output options
rustscan -a 10.10.10.50 -- -sC -sV -oN nmap_report.txt

# Run vulnerability audit with Nmap Vuln scripts
rustscan -a 10.10.10.50 -- --script vuln -oA target_vulns
```
---

## ⚡ Ottimizzazione delle prestazioni (`ulimit` e dimensione batch)

RustScan si basa sui limiti dei descrittori di file del tuo sistema operativo. Per impostazione predefinita, si regola automaticamente, ma la regolazione manuale fornisce le massime prestazioni:
```bash
# Increase batch size and socket ulimit for high-speed local networks
rustscan -a 10.10.10.50 -b 5000 -u 5000

# Lower batch size for high-latency or packet-dropping WiFi networks
rustscan -a 10.10.10.50 -b 500 --scan-order "random"
```
---

## 🌐 Scansione degli intervalli di porte e delle sottoreti CIDR
```bash
# Scan a specific port range instead of all ports
rustscan -a 10.10.10.50 -r 1-1000

# Scan multiple IP addresses
rustscan -a 10.10.10.50,10.10.10.51

# Scan an entire CIDR subnet range
rustscan -a 192.168.1.0/24
```
---

## 📊 Script e formattazione dell'output
```bash
# Output discovered open ports in JSON format
rustscan -a 10.10.10.50 --accessible

# Read target hosts from a file
rustscan -a 'hosts.txt'
```
---

## ⚡ Tabella di consultazione rapida

| Obiettivo | Comando |
|-----------|---------|
| **Scansiona tutte le porte 65k** | `rustscan -a <IP>` |
| **Scansiona tutte le porte + script Nmap**| `rustscan -a <IP> -- -sC -sV` |
| **Scansiona sottorete CIDR** | `rustscan -a <SUBNET>/24` |
| **Intervallo porta di scansione** | `rustscan -a <IP> -r 1-1000` |
| **Ottimizza dimensione batch e limite** | `rustscan -a <IP> -b 3000 -u 3000` |
| **Modalità silenziosa (senza banner)** | `rustscan -q -a <IP>` |
| **Uscita greppable** | `rustscan -a <IP> --greppable` |
| **Esporta in file Nmap** | `rustscan -a <IP> -- -sC -sV -oN nmap.txt` |

---

## 🛡️ Considerazioni difensive e OPSEC

1. **Rumore massiccio e avvisi IDS:** La scansione di 65.000 porte in 3 secondi genera migliaia di pacchetti TCP SYN istantaneamente. Attiverà immediatamente Snort, Suricata e la limitazione della velocità del firewall.
2. **Flooding SYN e pacchetti eliminati:** Su collegamenti VPN instabili (ad esempio HackTheBox, TryHackMe), batch di dimensioni elevate (`-b 4000`) possono causare la perdita di pacchetti. Se i risultati sembrano incoerenti, ridurre la dimensione del batch a `-b 1000`.

---

## ⚠️ Disclaimer legale

> [!ATTENZIONE]
> Questo cheatsheet è destinato esclusivamente ai test di penetrazione autorizzati e alla ricerca educativa. La scansione delle reti senza autorizzazione è severamente vietata.