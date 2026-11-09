<!-- language-home-navigation -->
[🏠 Home Page Italiana](../../README.it.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🦈 tshark - Cheatsheet completo

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ████████╗███████╗██╗  ██╗ █████╗ ██████╗ ██╗  ██╗
  ╚══██╔══╝██╔════╝██║  ██║██╔══██╗██╔══██╗██║ ██╔╝
     ██║   ███████╗███████║███████║██████╔╝█████╔╝ 
     ██║   ╚════██║██╔══██║██╔══██║██╔══██╗██╔═██╗ 
     ██║   ███████║██║  ██║██║  ██║██║  ██║██║  ██╗
     ╚═╝   ╚══════╝╚═╝  ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝
         Terminal Packet Analysis & Dissection Engine
```
<palign="centro">
  <img src="https://img.shields.io/badge/tshark-Packet_Analyzer-blue?style=for-the-badge" alt="tshark">
  <img src="https://img.shields.io/badge/CLI-Network_Sniffer-red?style=for-the-badge" alt="CLI Network Sniffer">
  <img src="https://img.shields.io/badge/Wireshark-Engine-green?style=for-the-badge" alt="Wireshark Engine">
  <img src="https://img.shields.io/badge/PCAP-Dissection-orange?style=for-the-badge" alt="PCAP Dissection">
</p>

<palign="centro">
  <b>🌐 Dissezione approfondita dei pacchetti di rete basata su terminale, analisi del flusso ed estrazione dei campi con tecnologia Wireshark</b>
</p>

---

## 📋 Sommario

- [Introduzione](#-introduzione)
- [Confronto strumenti: tshark vs tcpdump vs Wireshark](#-tool-comparison)
- [Installazione e privilegi](#-installazione--privilegi)
- [Acquisizione e riproduzione di base](#-basic-capture--playback)
- [Filtraggio del traffico: acquisizione o visualizzazione](#-filtering-traffic)
- [Estrazione campo (`-T fields`)](#-estrazione campo)
- [Pentest pratico e ricette forensi](#-pentest-pratico--ricette-forensi)
  - [Credenziali HTTP e ricerca URI](#http-credential--uri-hunting)
  - [Estrazione query DNS e rilevamento tunneling](#dns-query-extraction--tunneling-detection)
  - [Estrazione SNI TLS/HTTPS](#tls--https-sni-estrazione)
  - [Estrazione Kerberos AS-REQ / AS-REP](#kerberos-as-req--as-rep-estrazione)
  - [Monitoraggio autenticazione SMB e NTLM](#smb--ntlm-authentication-tracking)
  - [Controllo del protocollo Cleartext (FTP, Telnet, SMTP)](#cleartext-protocol-auditing)
  - [Ricerca handshake a 4 vie WPA/EAPOL](#wpa--eapol-4-way-handshake-hunting)
- [Seguimento dello streaming e riassemblaggio](#-stream-following--riassemblaggio)
- [Statistiche, conversazioni ed endpoint](#-statistiche-conversazioni--endpoint)
- [Decrittografia traffico TLS/SSL](#-tls--ssl-traffic-decrittografia)
- [Formati di esportazione (JSON, CSV, Elasticsearch)](#-export-formats)
- [Tabella di riferimento rapido](#-tabella-di-riferimento-rapido)
- [Suggerimenti e best practice](#-tips--best-practice)
- [Disclaimer legale](#-legal-disclaimer)

---

## 🎯 Introduzione

**tshark** è la controparte da terminale e da riga di comando di **Wireshark**. Si basa sulla stessa libreria di acquisizione dei pacchetti (libpcap/Npcap) e sul motore di dissezione dei pacchetti del grafico Wireshark, rendendolo uno degli strumenti di analisi di rete più potenti disponibili in Linux, macOS e Windows.

A differenza degli strumenti di sniffer di pacchetti di base che ispezionano solo le intestazioni del protocollo, `tshark` offre funzionalità di ispezione approfondita dei pacchetti su migliaia di protocolli, sintassi nativa del filtro di visualizzazione Wireshark (`-Y`), solida estrazione dei campi del protocollo (`-T fields`) e formattazione flessibile dell'output per pipeline di automazione (`jq`, script di shell, Python).

### Caratteristiche principali

| Caratteristica | Descrizione |
|---------|-------------|
| **Dissezione profonda** | Ispeziona i dati del carico utile in migliaia di protocolli applicativi |
| **Filtri Wireshark** | Supporto completo per la ricca sintassi del filtro di visualizzazione Wireshark (`-Y`) |
| **Estrazione campo** | Estrarre direttamente i singoli campi del protocollo (`-T fields -e ...`) |
| **Streaming seguito** | Riassembla sessioni TCP/UDP complete nei formati ASCII, esadecimale o raw |
| **Analisi forense automatizzata**| Scriptabile in shell, Python o reindirizzato a `jq` tramite `-T json` |
| **Decifratura** | Supporta la decrittografia TLS in runtime con `SSLKEYLOGFILE` |

---

## ⚖️ Confronto degli strumenti

| Caratteristica | squalo | tcpdump | WireShark |
|---------|--------|---------|-----------|
| **Interfaccia** | CLI (terminale) | CLI (terminale) | GUI (grafica) |
| **Utilizzo delle risorse** | Basso/Moderato | Estremamente minimo | Superiore (RAM e CPU) |
| **Remoto (SSH/senza testa)** | Headless nativo/CLI | Tubo SSH nativo | Richiede X11/agente remoto |
| **Motore di dissezione** | Dissettore Wireshark completo | Intestazioni di base | Dissettore Wireshark completo |
| **Filtri di acquisizione** | Solo BPF (`-f`) | Solo BPF (`-i`) | Solo BPF |
| **Visualizza filtri** | ✅ Sintassi Wireshark completa (`-Y`) | ❌ Nessuno | ✅ Barra filtro GUI completa |
| **Formati di esportazione** | PCAP, JSON, CSV, Elastico, XML | PCAP, ASCII, Esadecimale | PCAP, CSV, XML, JSON |
| **Scripting e automazione**| Shell, Python, jq | Shell/Awk/Sed | Plugin Lua, GUI |

---

## 📥 Installazione e privilegi

### Linux (Debian/Ubuntu/Kali Linux)
```bash
# Update and install tshark
sudo apt update && sudo apt install -y tshark

# Verify installation and version
tshark --version
```
### Consentire la cattura di pacchetti non root

Per evitare di eseguire `tshark` come `root` (che può rappresentare un rischio per la sicurezza a causa dell'ampia superficie del dissettore), aggiungi il tuo utente al gruppo `wireshark`:
```bash
# Configure wireshark-common for non-superusers
sudo dpkg-reconfigure wireshark-common
# (Select "Yes" when prompted)

# Add your user to the wireshark group
sudo usermod -aG wireshark $USER

# Apply group changes to current session
newgrp wireshark
```
### macOS (Homebrew) e Windows (cioccolatoso)
```bash
# macOS
brew install wireshark

# Windows
choco install wireshark
```
---

## 🚀 Acquisizione e riproduzione di base

### Elenca le interfacce disponibili
```bash
# List interfaces with numeric IDs
tshark -D
```
### Acquisizione di pacchetti attivi
```bash
# Capture packets on interface eth0
tshark -i eth0

# Capture only 100 packets and stop
tshark -i eth0 -c 100

# Capture and save traffic directly to a pcap file
tshark -i eth0 -w capture.pcap

# Capture with ring buffer (rotate files every 100MB, keep max 5 files)
tshark -i eth0 -b filesize:100000 -b files:5 -w rolling_traffic.pcap

# Capture with a duration trigger (capture for 60 seconds)
tshark -i eth0 -a duration:60 -w one_minute.pcap
```
### Lettura di file PCAP
```bash
# Read and display packets from a pcap file
tshark -r capture.pcap

# Read PCAP without resolving IP addresses to DNS hostnames (speeds up processing)
tshark -r capture.pcap -n

# Suppress packet count summary upon completion
tshark -r capture.pcap -q
```
---

## 🔍 Filtraggio del traffico: acquisizione vs visualizzazione

`tshark` utilizza due distinti motori di filtraggio:

1. **Capture Filters (`-f`):** Berkeley Packet Filters (BPF) valutati dal kernel prima che i pacchetti raggiungano lo spazio utente. Altamente performante, ma limitato alle primitive IP/porta di base.
2. **Filtri di visualizzazione (`-Y`):** Filtri di protocollo approfonditi valutati dal motore di dissezione di Wireshark. Supporta livelli di applicazione, ricerca di stringhe, regex e flag di protocollo.
```bash
# Capture filter: Capture only TCP traffic to port 80 or 443
tshark -i eth0 -f "tcp port 80 or tcp port 443" -w web_traffic.pcap

# Display filter: Show only successful HTTP GET responses (Status 200)
tshark -r capture.pcap -Y "http.request.method == \"GET\" and http.response.code == 200"

# Display filter: Filter by specific subnet and exclude broadcast
tshark -r capture.pcap -Y "ip.addr == 192.168.1.0/24 and not eth.dst == ff:ff:ff:ff:ff:ff"

# Read-filter (-R): Single-pass filter used during capture
tshark -r capture.pcap -2 -R "dns.flags.response == 1"
```
---

## 📊 Estrazione campo (`-T fields`)

La modalità `-T fields` è la funzionalità più potente di `tshark` per le sfide di ricognizione automatizzata, scripting e CTF.
```bash
# Basic syntax for field extraction
tshark -r capture.pcap -T fields -e <field1> -e <field2> -E header=y -E separator=,
```
### Flag di formattazione dell'estrazione delle chiavi

| Bandiera | Descrizione |
|------|-------------|
| `-T fields` | Passa alla modalità di output per l'estrazione dei campi personalizzati |
| `-e <field>` | Denomina il campo del protocollo da estrarre (può essere ripetuto) |
| `-E header=y` | Stampa i nomi delle colonne come riga di intestazione |
| `-E separator=,` | Imposta il separatore di campo (`,` per CSV, `\t` per TSV) |
| `-E occurrence=f` | Estrae solo la prima (`f`), l'ultima (`l`) o tutte le occorrenze (`a`) |
| `-E quote=d` | Racchiude i valori tra virgolette doppie |

### Foglio informativo sui campi del protocollo principale
```bash
# IP layer fields
ip.src, ip.dst, ip.proto, ip.ttl

# TCP layer fields
tcp.srcport, tcp.dstport, tcp.flags, tcp.stream, tcp.payload

# HTTP fields
http.host, http.request.method, http.request.uri, http.response.code, http.user_agent, http.cookie

# DNS fields
dns.qry.name, dns.qry.type, dns.a, dns.flags.response

# TLS fields
tls.handshake.extensions_server_name, tls.handshake.ciphersuite
```
---

## 🎯 Ricette pratiche di Pentest e Forense

### Credenziali HTTP e ricerca URI
```bash
# Extract all visited URLs and HTTP Hosts
tshark -r capture.pcap -Y "http.request" -T fields -e ip.src -e http.host -e http.request.method -e http.request.uri -E header=y

# Extract User-Agent strings across internal clients
tshark -r capture.pcap -Y "http.user_agent" -T fields -e ip.src -e http.user_agent | sort -u

# Hunt for HTTP POST data (logins, passwords, tokens)
tshark -r capture.pcap -Y "http.request.method == \"POST\"" -T fields -e ip.src -e ip.dst -e http.host -e http.file_data

# Hunt for HTTP Basic Authentication headers (Base64 encoded)
tshark -r capture.pcap -Y "http.authorization" -T fields -e ip.src -e http.authorization
```
### Estrazione di query DNS e rilevamento del tunneling
```bash
# List all requested domain names (DNS queries)
tshark -r capture.pcap -Y "dns.flags.response == 0" -T fields -e ip.src -e dns.qry.name | sort -u

# Detect potential DNS tunneling (display long subdomains > 30 characters)
tshark -r capture.pcap -Y "dns.flags.response == 0" -T fields -e dns.qry.name | awk 'length($1) > 30' | sort -u

# Correlate DNS queries with their resolved IP addresses
tshark -r capture.pcap -Y "dns.flags.response == 1 and dns.a" -T fields -e dns.qry.name -e dns.a -E header=y
```
### Estrazione SNI TLS/HTTPS

Anche quando i payload HTTPS sono crittografati, il **Server Name Indication (SNI)** nell'handshake TLS Client Hello iniziale rivela i nomi di dominio di destinazione:
```bash
# Extract TLS Server Name Indication (SNI) hostnames
tshark -r capture.pcap -Y "tls.handshake.type == 1" -T fields -e ip.src -e ip.dst -e tls.handshake.extensions_server_name | sort -u
```
### Estrazione Kerberos AS-REQ / AS-REP
```bash
# Extract Kerberos domain client names (hunting for user accounts)
tshark -r capture.pcap -Y "kerberos.CNameString" -T fields -e ip.src -e kerberos.realm -e kerberos.CNameString | sort -u

# Hunt for Kerberos errors (e.g. KRB5KDC_ERR_PREAUTH_REQUIRED, bad passwords)
tshark -r capture.pcap -Y "kerberos.error_code" -T fields -e ip.src -e ip.dst -e kerberos.error_code
```
### Monitoraggio dell'autenticazione SMB e NTLM
```bash
# Extract SMB accessed file paths and shares
tshark -r capture.pcap -Y "smb2.filename" -T fields -e ip.src -e ip.dst -e smb2.filename | sort -u

# Extract NTLMSSP usernames and domain names during authentication
tshark -r capture.pcap -Y "ntlmssp.auth.username" -T fields -e ip.src -e ntlmssp.auth.domain -e ntlmssp.auth.username
```
### Controllo del protocollo del testo in chiaro
```bash
# Extract FTP usernames and passwords
tshark -r capture.pcap -Y "ftp.request.command == \"USER\" or ftp.request.command == \"PASS\"" -T fields -e ip.src -e ftp.request.command -e ftp.request.arg

# Extract Telnet session input
tshark -r capture.pcap -Y "telnet" -T fields -e telnet.data
```
### Caccia all'handshake a 4 vie WPA/EAPOL
```bash
# Filter for 802.11 EAPOL handshakes to crack with Hashcat/Aircrack-ng
tshark -r wifi.pcap -Y "eapol" -w handshakes_only.pcap
```
---

## 🧵 Segui lo streaming e rimonta

`tshark` consente di riassemblare interi flussi TCP e UDP senza bisogno della GUI di Wireshark.
```bash
# List all TCP conversations to find stream IDs
tshark -r capture.pcap -q -z conv,tcp

# Follow TCP stream 0 in ASCII format
tshark -r capture.pcap -q -z follow,tcp,ascii,0

# Follow TCP stream 2 in Hexdump format
tshark -r capture.pcap -q -z follow,tcp,hex,2

# Follow HTTP stream (stream index 1)
tshark -r capture.pcap -q -z follow,http,ascii,1
```
---

## 📈 Statistiche, conversazioni ed endpoint
```bash
# Protocol hierarchy statistics (breakdown of all protocols in PCAP)
tshark -r capture.pcap -q -z io,phs

# IP conversations table (who talked to whom, packet count, bytes)
tshark -r capture.pcap -q -z conv,ip

# Port conversation statistics (TCP ports)
tshark -r capture.pcap -q -z conv,tcp

# List all unique IP endpoints in PCAP
tshark -r capture.pcap -q -z endpoints,ip

# HTTP request statistics tree
tshark -r capture.pcap -q -z http,tree

# Wireshark Expert Info warnings and errors
tshark -r capture.pcap -q -z expert
```
---

## 🔐Decrittografia del traffico TLS/SSL

Se disponi di chiavi segrete pre-master (esportate tramite il browser `SSLKEYLOGFILE` o recuperate durante il sandboxing del malware):
```bash
# Decrypt TLS traffic on the fly using keylog file
tshark -r https_traffic.pcap -o "tls.keylog_file:/path/to/sslkeys.log" -Y "http" -T fields -e http.host -e http.request.uri

# Follow decrypted HTTP stream inside TLS
tshark -r https_traffic.pcap -o "tls.keylog_file:/path/to/sslkeys.log" -q -z follow,http,ascii,0
```
---

## 📦 Formati di esportazione
```bash
# Output as structured JSON (ideal for piping into jq or Python)
tshark -r capture.pcap -c 5 -T json

# Extract specific JSON field with jq
tshark -r capture.pcap -T json | jq '.[]._source.layers.ip["ip.src"]'

# Output in Elasticsearch Bulk Format (-T ek)
tshark -r capture.pcap -T ek > packets_for_elastic.json

# Output as CSV table
tshark -r capture.pcap -T fields -e frame.number -e ip.src -e ip.dst -e _ws.col.Protocol -E header=y -E separator=, > traffic.csv
```
---

## ⚡ Tabella di consultazione rapida

| Compito | Comando |
|------|---------|
| **Cattura il traffico in tempo reale** | `tshark -i eth0 -w output.pcap` |
| **Cattura con filtro BPF** | `tshark -i eth0 -f "port 80" -w http.pcap` |
| **Leggi PCAP senza DNS** | `tshark -r capture.pcap -n` |
| **Applica filtro di visualizzazione** | `tshark -r capture.pcap -Y "http.response.code >= 400"` |
| **Estrai IP e porte** | `tshark -r capture.pcap -T fields -e ip.src -e tcp.srcport -e ip.dst -e tcp.dstport` |
| **Estrai query DNS** | `tshark -r capture.pcap -Y "dns.flags.response == 0" -T fields -e dns.qry.name` |
| **Estrai nomi host SNI TLS**| `tshark -r capture.pcap -Y "tls.handshake.type == 1" -T fields -e tls.handshake.extensions_server_name` |
| **Estrai URL HTTP** | `tshark -r capture.pcap -Y "http.request" -T fields -e http.host -e http.request.uri` |
| **Segui flusso TCP** | `tshark -r capture.pcap -q -z follow,tcp,ascii,0` |
| **Gerarchia del protocollo** | `tshark -r capture.pcap -q -z io,phs` |
| **Decifra con Keylog** | `tshark -r traffic.pcap -o "tls.keylog_file:keys.log"` |
| **Esporta in JSON** | `tshark -r capture.pcap -T json > packets.json` |

---

## 💡 Suggerimenti e migliori pratiche

1. **Utilizza sempre `-n` per PCAP di grandi dimensioni:** la risoluzione dei nomi DNS rallenta notevolmente l'analisi. Aggiungi `-n` per impedire la risoluzione degli indirizzi IP.
2. **Combina `-f` e `-Y`:** utilizza i filtri di acquisizione `-f` durante l'acquisizione in tempo reale per ridurre la perdita di pacchetti e i filtri di visualizzazione `-Y` durante la post-analisi per un'ispezione approfondita del protocollo.
3. **Pipe con `jq`:** Il pipelining di `tshark -T json` in `jq` consente di eseguire query su campi di protocollo nidificati con logica booleana e trasformazione complete.
4. **Buffer ad anello per lo sniffing 24 ore su 24, 7 giorni su 7:** quando si eseguono acquisizioni di rete continue su un host pivot, applicare sempre limiti di dimensioni e file (`-b filesize:50000 -b files:10`) per evitare il crash del disco di destinazione.

---

## ⚠️ Disclaimer legale

> **IMPORTANTE:** l'acquisizione non autorizzata di pacchetti, l'intercettazione del traffico di rete e l'intercettazione di credenziali sulle reti senza un permesso esplicito e documentato sono illegali e soggetti a procedimenti penali. Utilizza `tshark` esclusivamente su sistemi e reti di tua proprietà o che sei contrattualmente autorizzato a valutare.