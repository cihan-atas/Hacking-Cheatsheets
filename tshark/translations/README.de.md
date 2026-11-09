<!-- language-home-navigation -->
[🏠 Deutsche Startseite](../../README.de.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🦈 tshark – Vollständiger Cheatsheet

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
  <img src="https://img.shields.io/badge/PCAP-Dissection-orange?style=for-the-badge" alt="PCAP Dissection">
</p>

<p align="center">
  <b>🌐 Umfassende terminalbasierte Analyse von Netzwerkpaketen, Stream-Analyse und Feldextraktion mit Wireshark</b>
</p>

---

## 📋 Inhaltsverzeichnis

- [Einführung](#-Einführung)
- [Tool-Vergleich: tshark vs tcpdump vs Wireshark](#-tool-comparison)
- [Installation & Berechtigungen](#-installation--privileges)
- [Grundlegende Aufnahme und Wiedergabe](#-basic-capture--playback)
- [Traffic filtern: Erfassung vs. Anzeige](#-filtering-traffic)
- [Feldextraktion (`-T fields`)](#-Feldextraktion)
- [Praktische Pentest- und forensische Rezepte](#-practical-pentest--forensic-recipes)
  - [HTTP-Anmeldeinformationen und URI-Jagd](#http-credential--uri-hunting)
  - [DNS-Abfrageextraktion und Tunnelerkennung](#dns-query-extraction--tunneling-detection)
  - [TLS/HTTPS SNI-Extraktion](#tls--https-sni-extraction)
  - [Kerberos AS-REQ/AS-REP-Extraktion](#kerberos-as-req--as-rep-extraction)
  - [SMB- und NTLM-Authentifizierungsverfolgung](#smb--ntlm-authentication-tracking)
  - [Cleartext Protocol Auditing (FTP, Telnet, SMTP)](#cleartext-protocol-auditing)
  - [WPA / EAPOL 4-Way Handshake Hunting](#wpa--eapol-4-way-handshake-hunting)
- [Stream Following & Reassembly](#-stream-following--reassembly)
- [Statistiken, Gespräche und Endpunkte](#-statistics-conversations--endpoints)
- [TLS-/SSL-Verkehrsentschlüsselung](#-tls--ssl-traffic-decryption)
- [Exportformate (JSON, CSV, Elasticsearch)](#-export-formats)
- [Kurzreferenztabelle](#-quick-reference-table)
- [Tipps und Best Practices](#-tips--best-practices)
- [Rechtlicher Haftungsausschluss](#-legal-disclaimer)

---

## 🎯 Einführung

**tshark** ist das Terminal- und Befehlszeilen-Gegenstück zu **Wireshark**. Es basiert auf der gleichen Paketerfassungsbibliothek (libpcap/Npcap) und Paketzerlegungs-Engine wie das grafische Wireshark und ist damit eines der leistungsstärksten Netzwerkanalysetools, die unter Linux, macOS und Windows verfügbar sind.

Im Gegensatz zu einfachen Paket-Sniffer-Tools, die nur Protokoll-Header prüfen, bietet `tshark` umfassende Paketprüfungsfunktionen für Tausende von Protokollen, native Wireshark-Anzeigefiltersyntax (`-Y`), robuste Protokollfeldextraktion (`-T fields`) und flexible Ausgabeformatierung für Automatisierungspipelines (`jq`, Shell-Skripte, Python).

### Hauptmerkmale

| Funktion | Beschreibung |
|---------|-------------|
| **Tiefe Dissektion** | Überprüft Nutzdaten über Tausende von Anwendungsprotokollen hinweg |
| **Wireshark-Filter** | Volle Unterstützung für die umfangreiche Wireshark-Anzeigefiltersyntax (`-Y`) |
| **Feldextraktion** | Einzelne Protokollfelder direkt extrahieren (`-T fields -e ...`) |
| **Stream folgt** | Stellt vollständige TCP/UDP-Sitzungen in ASCII-, Hex- oder Rohformaten wieder zusammen |
| **Automatisierte Forensik**| Skriptfähig in Shell, Python oder über `-T json` | an `jq` weitergeleitet
| **Entschlüsselung** | Unterstützt Laufzeit-TLS-Entschlüsselung mit `SSLKEYLOGFILE` |

---

## ⚖️ Werkzeugvergleich

| Funktion | Tshark | tcpdump | Wireshark |
|---------|--------|---------|-----------|
| **Schnittstelle** | CLI (Terminal) | CLI (Terminal) | GUI (grafisch) |
| **Ressourcennutzung** | Niedrig / Moderat | Extrem minimal | Höher (RAM & CPU) |
| **Remote (SSH / Headless)** | Native Headless / CLI | Native SSH-Pipe | Erfordert X11 / Remote-Agent |
| **Sezierungsmaschine** | Vollständige Wireshark-Dissektoren | Grundlegende Überschriften | Vollständige Wireshark-Dissektoren |
| **Aufnahmefilter** | Nur BPF (`-f`) | Nur BPF (`-i`) | Nur BPF |
| **Anzeigefilter** | ✅ Vollständige Wireshark-Syntax (`-Y`) | ❌ Keine | ✅ Vollständige GUI-Filterleiste |
| **Formate exportieren** | PCAP, JSON, CSV, Elastic, XML | PCAP, ASCII, Hex | PCAP, CSV, XML, JSON |
| **Skripterstellung und Automatisierung**| Shell, Python, jq | Shell / Awk / Sed | Lua-Plugins, GUI |

---

## 📥 Installation und Berechtigungen

### Linux (Debian / Ubuntu / Kali Linux)
```bash
# Update and install tshark
sudo apt update && sudo apt install -y tshark

# Verify installation and version
tshark --version
```
### Nicht-Root-Paketerfassung zulassen

Um zu verhindern, dass `tshark` als `root` ausgeführt wird (was aufgrund der großen Dissektoroberfläche ein Sicherheitsrisiko darstellen kann), fügen Sie Ihren Benutzer der Gruppe `wireshark` hinzu:
```bash
# Configure wireshark-common for non-superusers
sudo dpkg-reconfigure wireshark-common
# (Select "Yes" when prompted)

# Add your user to the wireshark group
sudo usermod -aG wireshark $USER

# Apply group changes to current session
newgrp wireshark
```
### macOS (Homebrew) & Windows (schokoladig)
```bash
# macOS
brew install wireshark

# Windows
choco install wireshark
```
---

## 🚀 Einfache Aufnahme und Wiedergabe

### Verfügbare Schnittstellen auflisten
```bash
# List interfaces with numeric IDs
tshark -D
```
### Live-Pakete erfassen
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
### PCAP-Dateien lesen
```bash
# Read and display packets from a pcap file
tshark -r capture.pcap

# Read PCAP without resolving IP addresses to DNS hostnames (speeds up processing)
tshark -r capture.pcap -n

# Suppress packet count summary upon completion
tshark -r capture.pcap -q
```
---

## 🔍 Traffic filtern: Erfassung vs. Anzeige

`tshark` verwendet zwei unterschiedliche Filter-Engines:

1. **Capture Filters (`-f`):** Berkeley Packet Filters (BPF), die vom Kernel ausgewertet werden, bevor Pakete den Userspace erreichen. Sehr leistungsfähig, aber auf grundlegende IP-/Port-Primitive beschränkt.
2. **Anzeigefilter (`-Y`):** Tiefgreifende Protokollfilter, die von der Dissektions-Engine von Wireshark ausgewertet werden. Unterstützt Anwendungsschichten, String-Suche, Regex und Protokoll-Flags.
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

## 📊 Feldextraktion (`-T fields`)

Der `-T fields`-Modus ist die leistungsstärkste Funktion von `tshark` für automatisierte Aufklärung, Skripterstellung und CTF-Herausforderungen.
```bash
# Basic syntax for field extraction
tshark -r capture.pcap -T fields -e <field1> -e <field2> -E header=y -E separator=,
```
### Formatierungsflags für die Schlüsselextraktion

| Flagge | Beschreibung |
|------|-------------|
| `-T fields` | Schaltet den Ausgabemodus auf benutzerdefinierte Feldextraktion um |
| `-e <field>` | Benennt das zu extrahierende Protokollfeld (kann wiederholt werden) |
| `-E header=y` | Druckt Spaltennamen als Kopfzeile |
| `-E separator=,` | Legt Feldtrennzeichen fest (`,` für CSV, `\t` für TSV) |
| `-E occurrence=f` | Extrahiert nur das erste (`f`), das letzte (`l`) oder alle (`a`) Vorkommen |
| `-E quote=d` | Schließt Werte in doppelte Anführungszeichen | ein

### Cheatsheet zu Kernprotokollfeldern
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

## 🎯 Praktische Pentest- und forensische Rezepte

### HTTP-Anmeldeinformationen und URI-Jagd
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
### DNS-Abfrageextraktion und Tunnelerkennung
```bash
# List all requested domain names (DNS queries)
tshark -r capture.pcap -Y "dns.flags.response == 0" -T fields -e ip.src -e dns.qry.name | sort -u

# Detect potential DNS tunneling (display long subdomains > 30 characters)
tshark -r capture.pcap -Y "dns.flags.response == 0" -T fields -e dns.qry.name | awk 'length($1) > 30' | sort -u

# Correlate DNS queries with their resolved IP addresses
tshark -r capture.pcap -Y "dns.flags.response == 1 and dns.a" -T fields -e dns.qry.name -e dns.a -E header=y
```
### TLS/HTTPS SNI-Extraktion

Selbst wenn HTTPS-Nutzlasten verschlüsselt sind, zeigt die **Server Name Indication (SNI)** im ersten TLS-Client-Hello-Handshake Zieldomänennamen an:
```bash
# Extract TLS Server Name Indication (SNI) hostnames
tshark -r capture.pcap -Y "tls.handshake.type == 1" -T fields -e ip.src -e ip.dst -e tls.handshake.extensions_server_name | sort -u
```
### Kerberos AS-REQ/AS-REP-Extraktion
```bash
# Extract Kerberos domain client names (hunting for user accounts)
tshark -r capture.pcap -Y "kerberos.CNameString" -T fields -e ip.src -e kerberos.realm -e kerberos.CNameString | sort -u

# Hunt for Kerberos errors (e.g. KRB5KDC_ERR_PREAUTH_REQUIRED, bad passwords)
tshark -r capture.pcap -Y "kerberos.error_code" -T fields -e ip.src -e ip.dst -e kerberos.error_code
```
### SMB- und NTLM-Authentifizierungsverfolgung
```bash
# Extract SMB accessed file paths and shares
tshark -r capture.pcap -Y "smb2.filename" -T fields -e ip.src -e ip.dst -e smb2.filename | sort -u

# Extract NTLMSSP usernames and domain names during authentication
tshark -r capture.pcap -Y "ntlmssp.auth.username" -T fields -e ip.src -e ntlmssp.auth.domain -e ntlmssp.auth.username
```
### Prüfung des Klartextprotokolls
```bash
# Extract FTP usernames and passwords
tshark -r capture.pcap -Y "ftp.request.command == \"USER\" or ftp.request.command == \"PASS\"" -T fields -e ip.src -e ftp.request.command -e ftp.request.arg

# Extract Telnet session input
tshark -r capture.pcap -Y "telnet" -T fields -e telnet.data
```
### WPA/EAPOL 4-Wege-Handshake-Jagd
```bash
# Filter for 802.11 EAPOL handshakes to crack with Hashcat/Aircrack-ng
tshark -r wifi.pcap -Y "eapol" -w handshakes_only.pcap
```
---

## 🧵 Stream-Verfolgung und Zusammenbau

`tshark` ermöglicht die Neuzusammenstellung ganzer TCP- und UDP-Streams, ohne dass die GUI von Wireshark erforderlich ist.
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

## 📈 Statistiken, Gespräche und Endpunkte
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

## 🔐 TLS/SSL-Verkehrsentschlüsselung

Wenn Sie über Pre-Master-Geheimschlüssel verfügen (über den Browser `SSLKEYLOGFILE` exportiert oder während des Malware-Sandboxings abgerufen):
```bash
# Decrypt TLS traffic on the fly using keylog file
tshark -r https_traffic.pcap -o "tls.keylog_file:/path/to/sslkeys.log" -Y "http" -T fields -e http.host -e http.request.uri

# Follow decrypted HTTP stream inside TLS
tshark -r https_traffic.pcap -o "tls.keylog_file:/path/to/sslkeys.log" -q -z follow,http,ascii,0
```
---

## 📦 Exportformate
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

## ⚡ Kurzreferenztabelle

| Aufgabe | Befehl |
|------|---------|
| **Live-Verkehr erfassen** | `tshark -i eth0 -w output.pcap` |
| **Aufnahme mit BPF-Filter** | `tshark -i eth0 -f "port 80" -w http.pcap` |
| **PCAP ohne DNS lesen** | `tshark -r capture.pcap -n` |
| **Anzeigefilter anwenden** | `tshark -r capture.pcap -Y "http.response.code >= 400"` |
| **IPs und Ports extrahieren** | `tshark -r capture.pcap -T fields -e ip.src -e tcp.srcport -e ip.dst -e tcp.dstport` |
| **DNS-Abfragen extrahieren** | `tshark -r capture.pcap -Y "dns.flags.response == 0" -T fields -e dns.qry.name` |
| **TLS-SNI-Hostnamen extrahieren**| `tshark -r capture.pcap -Y "tls.handshake.type == 1" -T fields -e tls.handshake.extensions_server_name` |
| **HTTP-URLs extrahieren** | `tshark -r capture.pcap -Y "http.request" -T fields -e http.host -e http.request.uri` |
| **TCP-Stream folgen** | `tshark -r capture.pcap -q -z follow,tcp,ascii,0` |
| **Protokollhierarchie** | `tshark -r capture.pcap -q -z io,phs` |
| **Mit Keylog entschlüsseln** | `tshark -r traffic.pcap -o "tls.keylog_file:keys.log"` |
| **Nach JSON exportieren** | `tshark -r capture.pcap -T json > packets.json` |

---

## 💡 Tipps und Best Practices

1. **Verwenden Sie immer `-n` für große PCAPs:** Die DNS-Namensauflösung verlangsamt die Analyse erheblich. Fügen Sie `-n` hinzu, um die Auflösung von IP-Adressen zu verhindern.
2. **Kombinieren Sie `-f` und
3. **Pipe mit `jq`:** Durch die Pipelining von `tshark -T json` in `jq` können verschachtelte Protokollfelder mit vollständiger boolescher Logik und Transformation abgefragt werden.
4. **Ringpuffer für 24/7-Sniffing:** Wenn Sie kontinuierliche Netzwerkerfassungen auf einem Pivot-Host ausführen, erzwingen Sie immer Größen- und Dateibeschränkungen (`-b filesize:50000 -b files:10`), um einen Absturz der Festplatte des Ziels zu verhindern.

---

## ⚠️ Rechtlicher Haftungsausschluss

> **WICHTIG:** Das unbefugte Abfangen von Paketen, das Abhören des Netzwerkverkehrs und das Abfangen von Anmeldeinformationen in Netzwerken ohne ausdrückliche, dokumentierte Erlaubnis ist illegal und wird strafrechtlich verfolgt. Verwenden Sie `tshark` ausschließlich auf Systemen und Netzwerken, die Sie besitzen oder zu deren Bewertung Sie vertraglich berechtigt sind.