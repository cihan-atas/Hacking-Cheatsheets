<!-- language-home-navigation -->
[🏠 Norsk Hovedside](../../README.nb.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇬🇧 English](../README.md)

---


# 🛰️ shuttle - komplett jukseark

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
  <img src="https://img.shields.io/badge/Category-Pivoting_&_Tunneling-orange?style=for-the-badge" alt="Driving og tunneling">
  <img src="https://img.shields.io/badge/Protocols-TCP_DNS_SSH-green?style=for-the-badge" alt="Protokoller">
  <img src="https://img.shields.io/badge/License-LGPLv2.1-red?style=for-the-badge" alt="License LGPLv2.1">
</p>

<p align="center">
  <b>🌐 Videresend hele interne undernett og DNS-forespørsler gjennom en uprivilegert SSH-økt uten administrativ tilgang på den eksterne serveren</b>
</p>

---

## 📋 Innholdsfortegnelse

- [Oversikt og "Poor Man's VPN"-filosofien](#-overview--the-poor-mans-vpn-philosophy)
- [Hvordan sshuttle-fungerer](#-how-sshuttle-works)
- [Verktøysammenligning: sshuttle vs SOCKS vs Ligolo-ng vs OpenVPN](#-tool-comparison-sshuttle-vs-socks-vs-ligolo-ng-vs-openvpn)
- [Installasjon](#-installasjon)
- [Grunnleggende undernettruting](#-grunnleggende undernettruting)
- [DNS-trafikkviderekobling (`--dns`)](#-dns-trafikk-viderekobling---dns)
- [Full Gateway Redirection (Default Route)](#-full-gateway-redirection-default-route)
- [Subnettekskluderinger (`-x`)](#-undernettekskluderinger--x)
- [SSH-alternativer og nøkkelbasert autentisering (`-e`)](#-ssh-alternativer--nøkkelbasert-autentisering--e)
- [Daemon Background Execution (`-D`)](#-daemon-background-execution--d)
- [Hurtigreferansetabell](#-hurtigreferansetabell)
– [Defensive Considerations & OPSEC](#-defensive-considerations--opsec)
– [Juridisk ansvarsfraskrivelse](#-juridisk ansvarsfraskrivelse)

---

## 🎯 Oversikt og "Poor Man's VPN"-filosofien

**sshuttle** er en gjennomsiktig proxy-server som fungerer som en lett, null-konfigurasjons-VPN over en standard SSH-tilkobling.

I typiske bedriftens penetrasjonstesting og Red Team-vurderinger, er det vanskelig å svinge inn i interne nettverk av strenge begrensninger:
- ❌ Du har sjelden `root`- eller `SYSTEM`-tilgang på den første SSH-bastionen/pivotverten.
- ❌ Tradisjonelle VPN-løsninger (OpenVPN, WireGuard) krever administrative rettigheter og virtuelle TUN/TAP-nettverksenheter på begge endepunktene.
- ❌ Standard SOCKS proxyer (som `ssh -D`) krever konfigurering av `proxychains` eller individuelle applikasjons proxy-innstillinger for hvert enkelt verktøy.

**sshuttle løser alle tre begrensningene samtidig:**
- ✅ **Ingen ekstern rot kreves:** Pivotserveren trenger bare en standard Python-installasjon (`python3`).
- ✅ **Ingen ekstern TUN/TAP nødvendig:** Bruker ren TCP-strømmultipleksing over SSH-økten.
- ✅ **Systemomfattende gjennomsiktig ruting:** Modifiserer lokale `iptables` / `nftables` på angriperboksen, slik at verktøy (`nmap`, `curl`, `smbclient`, nettlesere) kan målrettes mot interne IP-er direkte uten omslag!

---

## ⚙️ Hvordan shuttle fungerer
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

## ⚖️ Verktøysammenligning: sshuttle vs SOCKS vs Ligolo-ng vs OpenVPN

| Funksjon | `sshuttle` | SOKKER (`ssh -D`) | `Ligolo-ng` | `OpenVPN` |
|--------|------------|----------------|----------------|------------|
| **Remote Root påkrevd** | ❌ Nei | ❌ Nei | ❌ Nei | ✅ Ja |
| **Lokal rot kreves** | ✅ Ja (iptables) | ❌ Nei | ✅ Ja (TUN-grensesnitt) | ✅ Ja |
| **Transparent ruting** | ✅ Ja | ❌ Krever proxykjeder | ✅ Ja (Native TUN) | ✅ Ja |
| **UDP Trafikkstøtte** | ❌ Kun DNS | ❌ Nei | ✅ Ja | ✅ Full UDP |
| **Krever Python on Target**| ✅ Ja (Python 3) | ❌ Nei | ❌ Nei (frittstående agent)| ❌ Nei |
| **Nmap SYN Scan (`-sS`)** | ❌ TCP-tilkobling (`-sT`)| ❌ Kun TCP-tilkobling | ✅ Rå pakkestøtte | ✅ Fulle råpakker |

---

## 📥 Installasjon
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

## 🚀 Grunnleggende undernettruting

Videresend et helt eksternt klasse C-undernett gjennom SSH-pivotverten:
```bash
# Route 10.10.10.0/24 through user@192.168.1.50
sshuttle -r user@192.168.1.50 10.10.10.0/24

# Route multiple disjoint subnets simultaneously
sshuttle -r user@192.168.1.50 10.10.10.0/24 172.16.0.0/16 192.168.100.0/24
```
Når du er tilkoblet, kan du kommunisere med interne maskiner direkte:
```bash
# No proxychains needed!
curl http://10.10.10.25:8080/
smbclient -L //10.10.10.10/ -U 'domain/user%pass'
```
---

## 🌐 DNS-trafikkvideresending (`--dns`)

Videresend interne bedrifts-DNS-spørringer (`.corp`, `.local`) til det eksterne nettverkets interne DNS-server:
```bash
# Route subnet and proxy internal DNS queries
sshuttle --dns -r user@192.168.1.50 10.10.10.0/24

# Query internal domain directly via nslookup/dig
dig dc01.internal.corp
```
---

## 🌍 Full gateway-omdirigering (standardrute)

Ruter all utgående angripertrafikk gjennom SSH-tilkoblingen (full VPN-atferd):
```bash
# Route all traffic through remote SSH server (0.0.0.0/0)
sshuttle -r user@remote-vps.com 0.0.0.0/0

# Route all traffic except local network
sshuttle -r user@remote-vps.com 0.0.0.0/0 -x 192.168.1.0/24
```
---

## 🚫 Subnettekskluderinger (`-x`)

Forhindre at spesifikke sensitive eller loopback-undernett blir fanget opp:
```bash
# Route entire 10.0.0.0/8 network, but exclude the pivot host itself
sshuttle -r user@10.0.0.50 10.0.0.0/8 -x 10.0.0.50/32
```
---

## 🔑 SSH-alternativer og nøkkelbasert autentisering (`-e`)

Send tilpassede SSH-flagg (private identitetsnøkler, ikke-standard SSH-porter):
```bash
# Use custom identity file and non-standard SSH port 2222
sshuttle -e 'ssh -i /path/to/id_rsa -p 2222' -r user@192.168.1.50 10.10.10.0/24
```
---

## 🤖 Daemon bakgrunnsutførelse (`-D`)

Kjør shuttle som en bakgrunnsdemon under pågående engasjementer:
```bash
# Start background daemon with pidfile
sudo sshuttle -D --pidfile /tmp/sshuttle.pid -r user@192.168.1.50 10.10.10.0/24

# Terminate daemon when finished
sudo kill $(cat /tmp/sshuttle.pid)
```
---

## ⚡ Hurtigreferansetabell

| Mål | Kommando |
|----------------|--------|
| **Rute enkelt undernett** | `sshuttle -r <USER>@<PIVOT> <SUBNET>/24` |
| **Rut undernett med DNS** | `sshuttle --dns -r <USER>@<PIVOT> <SUBNET>/24` |
| **Ruter flere undernett** | `sshuttle -r <USER>@<PIVOT> <NET1>/24 <NET2>/16` |
| **Ekskluder IP/Subnett** | `sshuttle -r <USER>@<PIVOT> <SUBNET>/24 -x <EXCLUDE_IP>` |
| **Full trafikkomdirigering** | `sshuttle -r <USER>@<PIVOT> 0.0.0.0/0` |
| **Egendefinert SSH-nøkkel/port** | `sshuttle -e 'ssh -i <KEY> -p <PORT>' -r <USER>@<PIVOT> <SUBNET>` |
| **Kjør i bakgrunnen** | `sshuttle -D --pidfile /tmp/sshuttle.pid -r <USER>@<PIVOT> <SUBNET>` |

---

## 🛡️ Defensive betraktninger og OPSEC

1. **Brannmurendring:** sshuttle endrer lokale brannmurtabeller (`iptables` på Linux, `pf` på macOS). Hvis shuttle krasjer urent, bruk `iptables -F` eller start brannmurtjenesten på nytt for å fjerne foreldede omdirigeringsregler.
2. **Kun TCP-begrensning:** Ikke-DNS UDP-pakker, ICMP-ekkoforespørsler (ping) og SYN-skanninger støttes ikke. Bruk alltid `nmap -sT -Pn -n` for portskanning over sshuttle.
3. **Target Footprint:** sshuttle laster automatisk opp et lite Python-monteringsskript til den eksterne pivotverten under øktinitialisering og kjører det via `python3`.

---

## ⚠️ Juridisk ansvarsfraskrivelse

> [!FORSIKTIG]
> Dette juksearket er kun beregnet for autorisert penetrasjonstesting og systemadministrasjon. Uautorisert ruting og avskjæring av nettverkstrafikk er ulovlig.