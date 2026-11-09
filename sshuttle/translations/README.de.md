<!-- language-home-navigation -->
[🏠 Deutsche Startseite](../../README.de.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🛰️ sshuttle – Vollständiger Cheatsheet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ███████╗███████╗██╗  ██╗██╗   ██╗████████╗████████╗██╗     ███████╗
  ██╔════╝██╔════╝██║  ██║██║   ██║╚══██╔══╝╚══██╔══╝██║     ██╔════╝
  ███████╗███████╗███████║██║   ██║   ██║      ██║   ██║     █████╗  
  ╚════██║╚════██║██╔══██║██║   ██║   ██║      ██║   ██║     ██╔══╝  
  ███████║███████║██║  ██║╚██████╔╝   ██║      ██║   ███████╗███████╗
  ╚══════╝╚══════╝╚═╝  ╚═╝ ╚═════╝    ╚═╝      ╚═╝   ╚══════╝╚══════╝
       Transparent Proxy Server that Works as a Poor Man's VPN over SSH
```
<p align="center">
  <img src="https://img.shields.io/badge/sshuttle-Poor_Man's_VPN-blue?style=for-the-badge" alt="sshuttle">
  <img src="https://img.shields.io/badge/Category-Pivoting_&_Tunneling-orange?style=for-the-badge" alt="Pivoting & Tunneling">
  <img src="https://img.shields.io/badge/Protocols-TCP_DNS_SSH-green?style=for-the-badge" alt="Protocols">
  <img src="https://img.shields.io/badge/License-LGPLv2.1-red?style=for-the-badge" alt="License LGPLv2.1">
</p>

<p align="center">
  <b>🌐 Leiten Sie ganze interne Subnetze und DNS-Anfragen über eine unprivilegierte SSH-Sitzung ohne administrativen Zugriff auf den Remote-Server weiter</b>
</p>

---

## 📋 Inhaltsverzeichnis

- [Überblick und die „Poor Man's VPN“-Philosophie](#-overview--the-poor-mans-vpn-philosophy)
- [So funktioniert sshuttle](#-how-shuttle-works)
- [Tool-Vergleich: sshuttle vs SOCKS vs Ligolo-ng vs OpenVPN](#-tool-comparison-sshuttle-vs-socks-vs-ligolo-ng-vs-openvpn)
- [Installation](#-Installation)
- [Grundlegendes Subnetz-Routing](#-basic-subnet-routing)
- [DNS-Verkehrsweiterleitung (`--dns`)](#-dns-traffic-forwarding---dns)
- [Vollständige Gateway-Umleitung (Standardroute)](#-full-gateway-redirection-default-route)
- [Subnetz-Ausschlüsse (`-x`)](#-subnet-exclusions--x)
- [SSH-Optionen und schlüsselbasierte Authentifizierung (`-e`)](#-ssh-options--key-based-authentication--e)
- [Daemon-Hintergrundausführung (`-D`)](#-daemon-background-execution--d)
- [Kurzreferenztabelle](#-quick-reference-table)
- [Verteidigungsüberlegungen und OPSEC](#-defensive-considerations--opsec)
- [Rechtlicher Haftungsausschluss](#-legal-disclaimer)

---

## 🎯 Überblick und die „VPN des armen Mannes“-Philosophie

**sshuttle** ist ein transparenter Proxyserver, der als leichtes, konfigurationsfreies VPN über eine Standard-SSH-Verbindung fungiert.

Bei typischen Penetrationstests und Red-Team-Bewertungen von Unternehmen wird die Einbindung in interne Netzwerke durch strenge Einschränkungen behindert:
- ❌ Sie verfügen selten über `root`- oder `SYSTEM`-Zugriff auf den ursprünglichen SSH-Bastion-/Pivot-Host.
- ❌ Herkömmliche VPN-Lösungen (OpenVPN, WireGuard) erfordern Administratorrechte und virtuelle TUN/TAP-Netzwerkgeräte auf beiden Endpunkten.
- ❌ Standard-SOCKS-Proxys (wie `ssh -D`) erfordern die Konfiguration von `proxychains` oder individuellen Anwendungs-Proxy-Einstellungen für jedes einzelne Tool.

**sshuttle löst alle drei Einschränkungen gleichzeitig:**
- ✅ **Kein Remote-Root erforderlich:** Der Pivot-Server benötigt nur eine Standard-Python-Installation (`python3`).
- ✅ **Kein Remote-TUN/TAP erforderlich:** Verwendet reines TCP-Stream-Multiplexing über die SSH-Sitzung.
- ✅ **Systemweites transparentes Routing:** Ändert lokales `iptables` / `nftables` auf der Angreifer-Box und ermöglicht es Tools (`nmap`,

---

## ⚙️ So funktioniert der Shuttle
```
[ Attacker Box (Local Root) ]
  ├── iptables / nftables redirects internal IP traffic (10.10.10.0/24) to local port
  └── sshuttle client multiplexes TCP packets over SSH tunnel
              │
              ▼ (Encrypted SSH Channel - Port 22)
[ Remote Pivot Server (Unprivileged User + Python) ]
  └── Assembles TCP connections and forwards them directly to internal hosts
              │
              ▼
[ Target Internal Network (10.10.10.0/24) ]
```
---

## ⚖️ Tool-Vergleich: sshuttle vs. SOCKS vs. Ligolo-ng vs. OpenVPN

| Funktion | `sshuttle` | SOCKEN (`ssh -D`) | `Ligolo-ng` | `OpenVPN` |
|---------|------------|----|-------------|-----------|
| **Remote Root erforderlich** | ❌ Nein | ❌ Nein | ❌ Nein | ✅ Ja |
| **Lokaler Root erforderlich** | ✅ Ja (iptables) | ❌ Nein | ✅ Ja (TUN-Schnittstelle) | ✅ Ja |
| **Transparentes Routing** | ✅ Ja | ❌ Erfordert Proxychains | ✅ Ja (Native TUN) | ✅ Ja |
| **UDP-Verkehrsunterstützung** | ❌ Nur DNS | ❌ Nein | ✅ Ja | ✅ Vollständiges UDP |
| **Erfordert Python auf dem Ziel**| ✅ Ja (Python 3) | ❌ Nein | ❌ Nein (Standalone-Agent)| ❌ Nein |
| **Nmap SYN-Scan (`-sS`)** | ❌ TCP-Verbindung (`-sT`)| ❌ Nur TCP-Verbindung | ✅ Unterstützung für Rohpakete | ✅ Vollständige Rohpakete |

---

## 📥 Installation
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y sshuttle

# Arch Linux
sudo pacman -S sshuttle

# macOS (Homebrew)
brew install sshuttle

# Python Pipx / Pip
pipx install sshuttle
```
---

## 🚀 Grundlegendes Subnetz-Routing

Leiten Sie ein gesamtes Remote-Klasse-C-Subnetz über den SSH-Pivot-Host weiter:
```bash
# Route 10.10.10.0/24 through user@192.168.1.50
sshuttle -r user@192.168.1.50 10.10.10.0/24

# Route multiple disjoint subnets simultaneously
sshuttle -r user@192.168.1.50 10.10.10.0/24 172.16.0.0/16 192.168.100.0/24
```
Sobald die Verbindung hergestellt ist, können Sie direkt mit internen Maschinen kommunizieren:
```bash
# No proxychains needed!
curl http://10.10.10.25:8080/
smbclient -L //10.10.10.10/ -U 'domain/user%pass'
```
---

## 🌐 DNS-Verkehrsweiterleitung (`--dns`)

Leiten Sie unternehmensinterne DNS-Abfragen (`.corp`, `.local`) an den internen DNS-Server des Remote-Netzwerks weiter:
```bash
# Route subnet and proxy internal DNS queries
sshuttle --dns -r user@192.168.1.50 10.10.10.0/24

# Query internal domain directly via nslookup/dig
dig dc01.internal.corp
```
---

## 🌍 Vollständige Gateway-Umleitung (Standardroute)

Leiten Sie den gesamten ausgehenden Angreiferverkehr über die SSH-Verbindung weiter (vollständiges VPN-Verhalten):
```bash
# Route all traffic through remote SSH server (0.0.0.0/0)
sshuttle -r user@remote-vps.com 0.0.0.0/0

# Route all traffic except local network
sshuttle -r user@remote-vps.com 0.0.0.0/0 -x 192.168.1.0/24
```
---

## 🚫 Subnetz-Ausschlüsse (`-x`)

Verhindern Sie, dass bestimmte sensible oder Loopback-Subnetze erfasst werden:
```bash
# Route entire 10.0.0.0/8 network, but exclude the pivot host itself
sshuttle -r user@10.0.0.50 10.0.0.0/8 -x 10.0.0.50/32
```
---

## 🔑 SSH-Optionen und schlüsselbasierte Authentifizierung (`-e`)

Übergeben Sie benutzerdefinierte SSH-Flags (private Identitätsschlüssel, nicht standardmäßige SSH-Ports):
```bash
# Use custom identity file and non-standard SSH port 2222
sshuttle -e 'ssh -i /path/to/id_rsa -p 2222' -r user@192.168.1.50 10.10.10.0/24
```
---

## 🤖 Daemon-Hintergrundausführung (`-D`)

Führen Sie sshuttle während laufender Einsätze als Hintergrund-Daemon aus:
```bash
# Start background daemon with pidfile
sudo sshuttle -D --pidfile /tmp/sshuttle.pid -r user@192.168.1.50 10.10.10.0/24

# Terminate daemon when finished
sudo kill $(cat /tmp/sshuttle.pid)
```
---

## ⚡ Kurzreferenztabelle

| Ziel | Befehl |
|-----------|---------|
| **Einzelnes Subnetz weiterleiten** | `sshuttle -r <USER>@<PIVOT> <SUBNET>/24` |
| **Subnetz mit DNS weiterleiten** | `sshuttle --dns -r <USER>@<PIVOT> <SUBNET>/24` |
| **Mehrere Subnetze weiterleiten** | `sshuttle -r <USER>@<PIVOT> <NET1>/24 <NET2>/16` |
| **IP/Subnetz ausschließen** | `sshuttle -r <USER>@<PIVOT> <SUBNET>/24 -x <EXCLUDE_IP>` |
| **Vollständige Verkehrsumleitung** | `sshuttle -r <USER>@<PIVOT> 0.0.0.0/0` |
| **Benutzerdefinierter SSH-Schlüssel/Port** | `sshuttle -e 'ssh -i <KEY> -p <PORT>' -r <USER>@<PIVOT> <SUBNET>` |
| **Im Hintergrund ausführen** | `sshuttle -D --pidfile /tmp/sshuttle.pid -r <USER>@<PIVOT> <SUBNET>` |

---

## 🛡️ Defensive Überlegungen und OPSEC

1. **Firewall-Änderung:** sshuttle ändert lokale Firewall-Tabellen (`iptables` unter Linux, `pf` unter macOS). Wenn sshuttle unsauber abstürzt, verwenden Sie `iptables -F` oder starten Sie den Firewall-Dienst neu, um veraltete Umleitungsregeln zu löschen.
2. **Nur TCP-Einschränkung:** Nicht-DNS-UDP-Pakete, ICMP-Echoanfragen (Ping) und SYN-Scans werden nicht unterstützt. Verwenden Sie immer `nmap -sT -Pn -n` für das Port-Scannen über Sshuttle.
3. **Ziel-Footprint:** sshuttle lädt während der Sitzungsinitialisierung automatisch ein kleines Python-Assembly-Skript auf den Remote-Pivot-Host hoch und führt es über `python3` aus.

---

## ⚠️ Rechtlicher Haftungsausschluss

> [!ACHTUNG]
> Dieses Cheatsheet ist nur für autorisierte Penetrationstests und Systemadministratoren bestimmt. Das unbefugte Weiterleiten und Abfangen des Netzwerkverkehrs ist illegal.