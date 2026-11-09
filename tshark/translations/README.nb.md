<!-- language-home-navigation -->
[🏠 Norsk Hovedside](../../README.nb.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇬🇧 English](../README.md)

---


# 🦈 tshark - Komplett jukseark

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
<p align="center">
  <img src="https://img.shields.io/badge/tshark-Packet_Analyzer-blue?style=for-the-badge" alt="tshark">
  <img src="https://img.shields.io/badge/CLI-Network_Sniffer-red?style=for-the-badge" alt="CLI Network Sniffer">
  <img src="https://img.shields.io/badge/Wireshark-Engine-green?style=for-the-badge" alt="Wireshark Engine">
  <img src="https://img.shields.io/badge/PCAP-Dissection-orange?style=for-the-badge" alt="PCAP-disseksjon">
</p>

<p align="center">
  <b>🌐 Dyp terminalbasert nettverkspakkedisseksjon, strømanalyse og feltekstraksjon drevet av Wireshark</b>
</p>

---

## 📋 Innholdsfortegnelse

– [Introduksjon](#-introduksjon)
- [Verktøysammenligning: tshark vs tcpdump vs Wireshark](#-tool-comparison)
- [Installasjon og rettigheter](#-installasjon--privilegier)
- [Grunnleggende opptak og avspilling](#-grunnleggende opptak--avspilling)
- [Filtrering av trafikk: Fang vs visning](#-filtrering-trafikk)
- [Feltutvinning (`-T fields`)](#-feltutvinning)
- [Praktisk undersøkelse og rettsmedisinske oppskrifter](#-practical-pentest--rettsmedisinske-oppskrifter)
  - [HTTP-legitimasjon og URI-jakt](#http-legitimasjon--uri-jakt)
  - [DNS Query Extraction & Tunneling Detection](#dns-query-extraction--tunneling-detection)
  - [TLS / HTTPS SNI-utvinning](#tls--https-sni-ekstraksjon)
  - [Kerberos AS-REQ / AS-REP Extraction](#kerberos-as-req--as-rep-extraction)
  - [SMB- og NTLM-autentiseringssporing](#smb--ntlm-autentiseringssporing)
  - [Cleartext Protocol Auditing (FTP, Telnet, SMTP)](#cleartext-protocol-auditing)
  - [WPA / EAPOL 4-veis håndtrykkjakt](#wpa--eapol-4-veis-håndtrykk-jakt)
- [Stream Following & Reassembly](#-stream-following--reassembly)
- [Statistics, Conversations & Endpoints](#-statistics-conversations--endpoints)
- [TLS / SSL Traffic Decryption](#-tls--ssl-traffic-decryption)
- [Eksporter formater (JSON, CSV, Elasticsearch)](#-eksport-formater)
- [Hurtigreferansetabell](#-hurtigreferansetabell)
– [Tips og beste fremgangsmåter](#-tips--beste praksis)
– [Juridisk ansvarsfraskrivelse](#-juridisk ansvarsfraskrivelse)

---

## 🎯 Introduksjon

**tshark** er terminal- og kommandolinjemotstykket til **Wireshark**. Den er avhengig av det identiske pakkefangstbiblioteket (libpcap/Npcap) og pakkedisseksjonsmotoren som den grafiske Wireshark, noe som gjør det til et av de kraftigste nettverksanalyseverktøyene som er tilgjengelige i Linux, macOS og Windows.

I motsetning til grunnleggende pakkesnifferverktøy som kun inspiserer protokollhoder, har `tshark` dype pakkeinspeksjonsmuligheter på tvers av tusenvis av protokoller, innebygd Wireshark-skjermfiltersyntaks (`-Y`), robust protokollfeltutvinning (`-T fields`) og fleksibel utdataformatering for automatiseringsrørledninger (`jq`, shell scripts).

### Nøkkelfunksjoner

| Funksjon | Beskrivelse |
|--------|-------------|
| **Dyp disseksjon** | Inspiserer nyttelastdata på tvers av tusenvis av applikasjonsprotokoller |
| **Wireshark-filtre** | Full støtte for rik Wireshark-skjermfiltersyntaks (`-Y`) |
| **Feltutvinning** | Trekk ut individuelle protokollfelt direkte (`-T fields -e ...`) |
| **Strøm følger** | Setter sammen hele TCP/UDP-sesjoner i ASCII-, Hex- eller råformater |
| **Automatisert etterforskning**| Skriptbar i shell, Python eller piped til `jq` via `-T json` |
| **Dekryptering** | Støtter runtime TLS-dekryptering med `SSLKEYLOGFILE` |

---

## ⚖️ Verktøysammenligning

| Funksjon | tshark | tcpdump | Wireshark |
|--------|--------|--------|--------|
| **Grensesnitt** | CLI (Terminal) | CLI (Terminal) | GUI (grafisk) |
| **Ressursbruk** | Lav / Moderat | Ekstremt minimal | Høyere (RAM & CPU) |
| **Fjernkontroll (SSH / Hodeløs)** | Innfødt hodeløs / CLI | Native SSH-rør | Krever X11 / ekstern agent |
| **Diseksjonsmotor** | Full Wireshark-dissektorer | Grunnleggende overskrifter | Full Wireshark-dissektorer |
| **Fangstfiltre** | Kun BPF (`-f`) | Bare BPF (`-i`) | Bare BPF |
| **Visningsfiltre** | ✅ Full Wireshark-syntaks (`-Y`) | ❌ Ingen | ✅ Full GUI-filterlinje |
| **Eksporter formater** | PCAP, JSON, CSV, Elastic, XML | PCAP, ASCII, Hex | PCAP, CSV, XML, JSON |
| **Skript og automatisering**| Shell, Python, jq | Shell / Awk / Sed | Lua-plugins, GUI |

---

## 📥 Installasjon og rettigheter

### Linux (Debian / Ubuntu / Kali Linux)
```bash
# Update and install tshark
sudo apt update && sudo apt install -y tshark

# Verify installation and version
tshark --version
```
### Tillater ikke-rotpakkefangst

For å unngå å kjøre `tshark` som `root` (som kan være en sikkerhetsrisiko på grunn av den enorme dissektoroverflaten), legg til brukeren din i `wireshark`-gruppen:
```bash
# Configure wireshark-common for non-superusers
sudo dpkg-reconfigure wireshark-common
# (Select "Yes" when prompted)

# Add your user to the wireshark group
sudo usermod -aG wireshark $USER

# Apply group changes to current session
newgrp wireshark
```
### macOS (hjemmebrygget) og Windows (sjokoladeaktig)
```bash
# macOS
brew install wireshark

# Windows
choco install wireshark
```
---

## 🚀 Grunnleggende opptak og avspilling

### Liste tilgjengelige grensesnitt
```bash
# List interfaces with numeric IDs
tshark -D
```
### Ta opp live-pakker
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
### Leser PCAP-filer
```bash
# Read and display packets from a pcap file
tshark -r capture.pcap

# Read PCAP without resolving IP addresses to DNS hostnames (speeds up processing)
tshark -r capture.pcap -n

# Suppress packet count summary upon completion
tshark -r capture.pcap -q
```
---

## 🔍 Filtrering av trafikk: Capture vs Display

`tshark` bruker to forskjellige filtreringsmotorer:

1. **Fangstfiltre (`-f`):** Berkeley-pakkefiltre (BPF) evaluert av kjernen før pakker traff brukerområdet. Svært ytelse, men begrenset til grunnleggende IP/port primitiver.
2. **Visningsfiltre (`-Y`):** Dypprotokollfiltre evaluert av Wiresharks disseksjonsmotor. Støtter applikasjonslag, strengsøking, regulært uttrykk og protokollflagg.
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

## 📊 Feltutvinning (`-T fields`)

`-T fields`-modusen er `tshark`s kraftigste funksjon for automatisert rekognosering, skripting og CTF-utfordringer.
```bash
# Basic syntax for field extraction
tshark -r capture.pcap -T fields -e <field1> -e <field2> -E header=y -E separator=,
```
### Formateringsflagg for nøkkelutvinning

| Flagg | Beskrivelse |
|------|-------------|
| `-T fields` | Bytter utgangsmodus til egendefinert feltutvinning |
| `-e <field>` | Navngir protokollfeltet som skal trekkes ut (kan gjentas) |
| `-E header=y` | Skriver ut kolonnenavn som overskriftsrad |
| `-E separator=,` | Angir feltseparator (`,` for CSV, `\t` for TSV) |
| `-E occurrence=f` | Trekker bare ut den første (`f`), siste (`l`), eller alle (`a`) forekomstene |
| `-E quote=d` | Omgir verdier i doble anførselstegn |

### Kjerneprotokollfelt Cheatsheet
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

## 🎯 Praktiske Pentest og rettsmedisinske oppskrifter

### HTTP-legitimasjon og URI-jakt
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
### DNS Query Extraction & Tunneling Detection
```bash
# List all requested domain names (DNS queries)
tshark -r capture.pcap -Y "dns.flags.response == 0" -T fields -e ip.src -e dns.qry.name | sort -u

# Detect potential DNS tunneling (display long subdomains > 30 characters)
tshark -r capture.pcap -Y "dns.flags.response == 0" -T fields -e dns.qry.name | awk 'length($1) > 30' | sort -u

# Correlate DNS queries with their resolved IP addresses
tshark -r capture.pcap -Y "dns.flags.response == 1 and dns.a" -T fields -e dns.qry.name -e dns.a -E header=y
```
### TLS / HTTPS SNI-utvinning

Selv når HTTPS-nyttelaster er kryptert, avslører **Server Name Indication (SNI)** i det innledende TLS Client Hello-håndtrykket destinasjonsdomenenavn:
```bash
# Extract TLS Server Name Indication (SNI) hostnames
tshark -r capture.pcap -Y "tls.handshake.type == 1" -T fields -e ip.src -e ip.dst -e tls.handshake.extensions_server_name | sort -u
```
### Kerberos AS-REQ / AS-REP-ekstraksjon
```bash
# Extract Kerberos domain client names (hunting for user accounts)
tshark -r capture.pcap -Y "kerberos.CNameString" -T fields -e ip.src -e kerberos.realm -e kerberos.CNameString | sort -u

# Hunt for Kerberos errors (e.g. KRB5KDC_ERR_PREAUTH_REQUIRED, bad passwords)
tshark -r capture.pcap -Y "kerberos.error_code" -T fields -e ip.src -e ip.dst -e kerberos.error_code
```
### SMB- og NTLM-autentiseringssporing
```bash
# Extract SMB accessed file paths and shares
tshark -r capture.pcap -Y "smb2.filename" -T fields -e ip.src -e ip.dst -e smb2.filename | sort -u

# Extract NTLMSSP usernames and domain names during authentication
tshark -r capture.pcap -Y "ntlmssp.auth.username" -T fields -e ip.src -e ntlmssp.auth.domain -e ntlmssp.auth.username
```
### Klartekstprotokollrevisjon
```bash
# Extract FTP usernames and passwords
tshark -r capture.pcap -Y "ftp.request.command == \"USER\" or ftp.request.command == \"PASS\"" -T fields -e ip.src -e ftp.request.command -e ftp.request.arg

# Extract Telnet session input
tshark -r capture.pcap -Y "telnet" -T fields -e telnet.data
```
### WPA / EAPOL 4-veis håndtrykkjakt
```bash
# Filter for 802.11 EAPOL handshakes to crack with Hashcat/Aircrack-ng
tshark -r wifi.pcap -Y "eapol" -w handshakes_only.pcap
```
---

## 🧵 Strømfølging og montering på nytt

`tshark` tillater å sette sammen hele TCP- og UDP-strømmer uten å trenge Wiresharks GUI.
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

## 📈 Statistikk, samtaler og endepunkter
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

## 🔐 TLS / SSL trafikkdekryptering

Hvis du har pre-master hemmelige nøkler (eksportert via nettleseren `SSLKEYLOGFILE` eller hentet under malware sandboxing):
```bash
# Decrypt TLS traffic on the fly using keylog file
tshark -r https_traffic.pcap -o "tls.keylog_file:/path/to/sslkeys.log" -Y "http" -T fields -e http.host -e http.request.uri

# Follow decrypted HTTP stream inside TLS
tshark -r https_traffic.pcap -o "tls.keylog_file:/path/to/sslkeys.log" -q -z follow,http,ascii,0
```
---

## 📦 Eksporter formater
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

## ⚡ Hurtigreferansetabell

| Oppgave | Kommando |
|------|--------|
| **Fang direkte trafikk** | `tshark -i eth0 -w output.pcap` |
| **Fangst med BPF-filter** | `tshark -i eth0 -f "port 80" -w http.pcap` |
| **Les PCAP uten DNS** | `tshark -r capture.pcap -n` |
| **Bruk visningsfilter** | `tshark -r capture.pcap -Y "http.response.code >= 400"` |
| ** Trekk ut IP-er og porter** | `tshark -r capture.pcap -T fields -e ip.src -e tcp.srcport -e ip.dst -e tcp.dstport` |
| ** Trekk ut DNS-spørringer** | `tshark -r capture.pcap -Y "dns.flags.response == 0" -T fields -e dns.qry.name` |
| ** Trekk ut TLS SNI-vertsnavn**| `tshark -r capture.pcap -Y "tls.handshake.type == 1" -T fields -e tls.handshake.extensions_server_name` |
| **Takk ut HTTP-URLer** | `tshark -r capture.pcap -Y "http.request" -T fields -e http.host -e http.request.uri` |
| **Følg TCP Stream** | `tshark -r capture.pcap -q -z follow,tcp,ascii,0` |
| **Protokollhierarki** | `tshark -r capture.pcap -q -z io,phs` |
| **Dekrypter med nøkkellogg** | `tshark -r traffic.pcap -o "tls.keylog_file:keys.log"` |
| **Eksporter til JSON** | `tshark -r capture.pcap -T json > packets.json` |

---

## 💡 Tips og beste fremgangsmåter

1. **Bruk alltid `-n` for store PCAP-er:** DNS-navneoppløsning reduserer parsingen dramatisk. Legg til `-n` for å forhindre oppløsning av IP-adresser.
2. **Kombiner `-f` og `-Y`:** Bruk `-f`-fangstfiltre under direkte fangst for å redusere pakkefall, og `-Y`-visningsfiltre under etteranalyse for dyp protokollinspeksjon.
3. **Pipe med `jq`:** Pipelining av `tshark -T json` til `jq` gjør det mulig å spørre nestede protokollfelt med fullstendig boolsk logikk og transformasjon.
4. **Ringbuffere for 24/7 sniffing:** Når du kjører kontinuerlig nettverksfangst på en pivotvert, må du alltid håndheve størrelses- og filgrenser (`-b filesize:50000 -b files:10`) for å forhindre at målets disk krasjer.

---

## ⚠️ Juridisk ansvarsfraskrivelse

> **VIKTIG:** Uautorisert pakkefangst, avlytting av nettverkstrafikk og avskjæring av legitimasjon på nettverk uten eksplisitt, dokumentert tillatelse er ulovlig og gjenstand for straffeforfølgelse. Bruk `tshark` strengt tatt på systemer og nettverk du eier eller er kontraktsmessig autorisert til å vurdere.