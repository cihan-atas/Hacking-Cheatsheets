<!-- language-home-navigation -->
[🏠 Home Page Italiana](../../README.it.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ⚡ Masscan - Scheda completa

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
<palign="centro">
  <img src="https://img.shields.io/badge/Masscan-Internet_Port_Scanner-blue?style=for-the-badge" alt="Masscan">
  <img src="https://img.shields.io/badge/Language-C-orange?style=for-the-badge" alt="Lingua C">
  <img src="https://img.shields.io/badge/Category-Recon_&_Scanning-green?style=for-the-badge" alt="Ricognizione e scansione">
  <img src="https://img.shields.io/badge/License-AGPLv3-red?style=for-the-badge" alt="Licenza AGPLv3">
</p>

<palign="centro">
  <b>🌐 Scanner di porte TCP asincrono ad alte prestazioni in grado di scansionare l'intera Internet IPv4 in meno di 5 minuti</b>
</p>

---

## 📋 Sommario

- [Panoramica e architettura asincrona](#-panoramica--l'architettura-asincrona)
- [Confronto strumenti: Masscan vs Nmap vs RustScan](#-tool-comparison-masscan-vs-nmap-vs-rustscan)
- [Installazione](#-installazione)
- [Utilizzo di base e target di scansione](#-basic-usage--scanning-targets)
- [Controllo velocità di trasmissione (`--rate`)](#-controllo-velocità-di-trasmissione---velocità)
- [Specifiche della porta (TCP e UDP)](#-port-specifications-tcp--udp)
- [Cattura banner e sondaggio del protocollo di base](#-banner-cabbing--basic-prob-probing)
- [Esclusioni target e liste nere (`--excludefile`)](#-target-exclusions--blacklists---excludefile)
- [Formati di output e piping in Nmap](#-output-formats--piping-into-nmap)
- [Tabella di riferimento rapido](#-tabella-di-riferimento-rapido)
- [Considerazioni difensive e limiti hardware](#-considerazioni-difensive--limiti-hardware)
- [Disclaimer legale](#-legal-disclaimer)

---

## 🎯 Panoramica e architettura asincrona

Creato da **Robert Graham**, **Masscan** è considerato lo scanner di porte più veloce esistente. Produce risultati simili a Nmap, ma funziona internamente come `scanrand`, `unicornscan` e `ZMap`, utilizzando la **trasmissione asincrona**.

A differenza di Nmap, che utilizza lo stack TCP del kernel del sistema operativo e attende risposte prima di sondare le porte successive, Masscan incorpora il proprio stack TCP/IP personalizzato nello spazio utente. Invia pacchetti SYN grezzi a velocità superiori a **10 milioni di pacchetti al secondo** senza mantenere tabelle di stato della connessione.

---

## ⚖️ Confronto strumenti: Masscan vs Nmap vs RustScan

| Caratteristica | Massano | Nmap | RustScan |
|---------|---------|------|----------|
| **Tasso massimo dei pacchetti** | 10.000.000 di pkt/sec | ~1.000 pkt/sec | Limite socket del sistema operativo |
| **Scansione Internet completa (/0)**| ✅ Meno di 5 minuti | ❌ Giorni/Settimane | ❌ Non progettato per /0 |
| **Implementazione dello stack TCP**| Spazio utente personalizzato Raw SYN| Kernel del sistema operativo/socket grezzi| Socket TCP Tokio asincroni |
| **Versione approfondita e script NSE**| ❌ Banner primitivi | ✅ Oltre 600 script NSE | ✅ Tramite Nmap handoff |
| **Monitoraggio dello stato** | Apolide (Blackrock PRNG) | Pool di connessioni con stato| Attività asincrone con stato |
| **Ambito di mira ideale** | Blocchi di classe A/B (/8, /16)| Destinazioni singole e /24 sottoreti | Macchine singole e /24 sottoreti |

---

## 📥Installazione
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

## 🚀 Utilizzo di base e obiettivi di scansione
```bash
# Scan a single IP for top web ports
sudo masscan 10.10.10.50 -p80,443,8080 --rate 1000

# Scan an entire Class B internal subnet (172.16.0.0/16) for SMB port 445
sudo masscan 172.16.0.0/16 -p445 --rate 5000

# Scan all 65,535 ports on a specific IP
sudo masscan 10.10.10.50 -p1-65535 --rate 2000
```
> [!IMPORTANTE]
> Poiché Masscan genera pacchetti grezzi tramite `libpcap`, **deve essere eseguito con privilegi root/sudo**.

---

## ⚡ Controllo della velocità di trasmissione (`--rate`)

Il parametro `--rate` determina quanti pacchetti al secondo trasmette Masscan. La calibrazione è fondamentale per evitare di saturare le apparecchiature di rete:
```bash
# Gentle rate for home routers / WiFi (100–500 pkts/sec)
sudo masscan 10.10.10.0/24 -p1-1000 --rate 300

# Standard corporate gigabit internal network rate (5,000–10,000 pkts/sec)
sudo masscan 10.0.0.0/8 -p80,443 --rate 10000

# High-speed data center 10Gbps uplink rate (100,000+ pkts/sec)
sudo masscan 0.0.0.0/0 -p443 --rate 100000
```
---

## 🔌 Specifiche della porta (TCP e UDP)

Masscan supporta contemporaneamente sia gli elenchi di porte TCP che UDP:
```bash
# Specific TCP ports
sudo masscan 10.10.10.50 -p21,22,80,443,445,3389

# Top ports shortcut (similar to Nmap)
sudo masscan 10.10.10.50 --top-ports 100

# UDP ports (prefixed with U:)
sudo masscan 10.10.10.50 -p U:53,U:161,U:123 --rate 500
```
---

## 🔍 Acquisizione di banner e sondaggio del protocollo di base

Sebbene Masscan sia principalmente uno scanner SYN, può completare handshake a tre vie per acquisire i banner delle applicazioni:
```bash
# Grab HTTP, FTP, and SSH banners
sudo masscan 10.10.10.0/24 -p80,21,22 --banners --rate 1000
```
---

## 🚫 Esclusioni target e liste nere (`--excludefile`)

Quando si eseguono test di penetrazione ad ampio raggio, escludere sempre le infrastrutture critiche e gli indirizzi off-limit:
```bash
# Exclude government and military ranges or local gateway
sudo masscan 10.0.0.0/8 -p80 --rate 2000 --exclude 10.0.0.1,10.1.1.1

# Use an exclusion file (e.g., standard exclude.conf)
sudo masscan 192.168.0.0/16 -p445 --rate 2000 --excludefile /etc/masscan/exclude.conf
```
---

## 📊 Formati di output e piping in Nmap

Salva gli host rilevati in formati greppable o JSON, quindi inseriscili in Nmap per una valutazione completa della vulnerabilità:
```bash
# Save to JSON format
sudo masscan 10.10.10.0/24 -p80,443 -oJ masscan_results.json

# Save to simple List format (-oL)
sudo masscan 10.10.10.0/24 -p1-65535 --rate 2000 -oL open_ports.txt

# Extract discovered ports and feed directly to Nmap
cat open_ports.txt | awk '{print $4}' | sort -u | paste -sd, - | xargs -I{} nmap -sC -sV -p{} 10.10.10.50
```
---

## ⚡ Tabella di consultazione rapida

| Obiettivo | Comando |
|-----------|---------|
| **Scansione host singolo (tutte le porte)** | `sudo masscan <IP> -p1-65535 --rate 2000` |
| **Scansiona la sottorete CIDR per la porta** | `sudo masscan <SUBNET>/16 -p<PORT> --rate 5000` |
| **Scansione con cattura banner** | `sudo masscan <IP> -p80,22 --banners --rate 1000` |
| **Scansiona porte UDP** | `sudo masscan <IP> -p U:53,U:161 --rate 500` |
| **Escludi indirizzi IP** | `sudo masscan <NET> -p80 --exclude <IP1>,<IP2>` |
| **Output in JSON** | `sudo masscan <IP> -p80,443 -oJ output.json` |
| **Output nell'elenco semplice** | `sudo masscan <IP> -p80,443 -oL output.txt` |

---

## 🛡️ Considerazioni difensive e limiti hardware

1. **Arresto del router consumer:** l'impostazione di `--rate 100000` su un router domestico DSL/cavo o Wi-Fi standard sovraccarica la tabella dello stato di tracciamento della connessione NAT e provoca l'arresto anomalo del router. Mantenere `--rate` sotto `1000` sui collegamenti residenziali.
2. **Consumo di larghezza di banda:** La trasmissione a 10 milioni di pacchetti al secondo consuma circa 5,4 Gbps di larghezza di banda della rete.
3. **Rifiuto del firewall locale (RST):** Poiché Masscan utilizza uno stack TCP personalizzato nello spazio utente, il kernel Linux non riconosce le connessioni aperte e potrebbe inviare pacchetti TCP RST automatici alle destinazioni. Per evitare ciò durante la cattura dei banner:
   ```bash
   sudo iptables -A INPUT -p tcp --dport 60000 -j DROP
   sudo masscan ... --source-port 60000
   ```
---

## ⚠️ Disclaimer legale

> [!ATTENZIONE]
> Masscan è un'utilità di ricognizione di rete eccezionalmente potente. La scansione di reti non autorizzate, intervalli IP governativi o sottoreti Internet pubbliche può violare le leggi sulle telecomunicazioni e sulla sicurezza informatica. Verificare sempre l'autorizzazione scritta prima di avviare le scansioni.